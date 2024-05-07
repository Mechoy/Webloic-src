package weblogic.servlet.internal;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import javax.servlet.ServletOutputStream;
import weblogic.logging.Loggable;
import weblogic.management.DeploymentException;
import weblogic.protocol.ServerChannel;
import weblogic.servlet.HTTPLogger;
import weblogic.servlet.HTTPTextTextFormatter;
import weblogic.socket.AbstractMuxableSocket;
import weblogic.socket.MaxMessageSizeExceededException;
import weblogic.socket.SSLFilter;
import weblogic.socket.SocketMuxer;
import weblogic.utils.http.HttpChunkInputStream;
import weblogic.utils.http.HttpRequestParseException;
import weblogic.utils.http.HttpRequestParser;
import weblogic.utils.io.Chunk;
import weblogic.utils.io.NullInputStream;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

public final class MuxableSocketHTTP extends AbstractMuxableSocket {
   private static final int MAX_PIPELINED_REQUESTS = 100;
   private final boolean https;
   private ServletRequestImpl request;
   private ServletResponseImpl response;
   private HttpServer httpServer;
   private byte[] buf = null;
   private int pos = 0;
   private int headerEndPos;
   private int pipelinedRequests;
   private boolean doNotPipeline = false;
   private long messagesReceived = 0L;
   private long bytesReceived = 0L;
   private boolean reuseRequestResponse;
   private static boolean doNotSendContinueHeader = false;
   ByteBuffer extraDataForJSSERead;

   public MuxableSocketHTTP(Chunk var1, Socket var2, boolean var3, ServerChannel var4) throws IOException {
      super(var1, var4);
      this.connect(var2);
      this.setSoTimeout(this.getSocket().getSoTimeout());
      this.https = var3;
      this.httpServer = WebService.defaultHttpServer();
      if (var1.next == null) {
         this.buf = var1.buf;
         this.incrementBufferOffset(var1.end);
      } else {
         Chunk var5 = null;

         for(Chunk var6 = var1; var6 != null; var6 = var6.next) {
            int var10;
            for(int var7 = 0; var7 < var6.end; var7 += var10) {
               byte[] var8 = this.getBuffer();
               int var9 = this.getBufferOffset();
               var10 = Math.min(var8.length - var9, var6.end - var7);
               System.arraycopy(var6.buf, var7, var8, var9, var10);
               this.incrementBufferOffset(var10);
            }

            if (var5 != null) {
               Chunk.releaseChunk(var5);
            }

            var5 = var6;
         }

         if (var5 != null) {
            Chunk.releaseChunk(var5);
         }
      }

      this.reuseRequestResponse = true;
      this.request = new ServletRequestImpl(this);
      this.response = new ServletResponseImpl(this.request, this.getSocketOutputStream());
      this.addSenderStatistics(this.response);
   }

   public String toString() {
      return super.toString() + " - idle timeout: '" + this.getIdleTimeoutMillis() + "' ms, socket timeout: '" + this.getSoTimeout() + "' ms";
   }

   boolean isHttps() {
      return this.https;
   }

   HttpServer getHttpServer() {
      return this.httpServer;
   }

   public int getIdleTimeoutMillis() {
      return this.https ? this.httpServer.getMBean().getHttpsKeepAliveSecs() * 1000 : this.httpServer.getMBean().getKeepAliveSecs() * 1000;
   }

   public byte[] getBuffer() {
      if (this.buf != null) {
         if (this.pos < this.buf.length) {
            return this.buf;
         } else {
            byte[] var1 = new byte[this.pos << 1];
            System.arraycopy(this.buf, 0, var1, 0, this.buf.length);
            this.buf = var1;
            if (this.head != null) {
               Chunk.releaseChunk(this.head);
               this.head = null;
            }

            return this.buf;
         }
      } else {
         this.head = Chunk.getChunk();
         return this.buf = this.head.buf;
      }
   }

   public ByteBuffer[] getAvailableBufferofSize(int var1) {
      ByteBuffer var2 = ByteBuffer.wrap(this.getBuffer());
      var2.position(this.pos);
      ArrayList var3 = new ArrayList();
      var3.add(var2);
      if (var2.remaining() < var1) {
         this.extraDataForJSSERead = ByteBuffer.allocate(var1 - var2.remaining());
         var3.add(this.extraDataForJSSERead);
      }

      return (ByteBuffer[])var3.toArray(new ByteBuffer[var3.size()]);
   }

   public int getBufferOffset() {
      return this.pos;
   }

   public void incrementBufferOffset(int var1) throws MaxMessageSizeExceededException {
      this.pos += var1;
      if (this.pos > this.buf.length) {
         this.copyDataIntoLargerByteArray(this.buf.length);
      }

      int var2 = this.getChannel().getMaxMessageSize();
      if (var2 > -1 && this.pos > var2) {
         throw new MaxMessageSizeExceededException(this.pos, var2, this.https ? "https" : "http");
      }
   }

   private void copyDataIntoLargerByteArray(int var1) {
      this.buf = this.getBuffer();
      System.arraycopy(this.extraDataForJSSERead.array(), 0, this.buf, var1, this.extraDataForJSSERead.position());
      this.extraDataForJSSERead = null;
   }

   public void hasException(Throwable var1) {
      if (var1 instanceof SocketException) {
         if (HTTPDebugLogger.isEnabled()) {
            HTTPDebugLogger.debug("Socket issue", (SocketException)var1);
         }
      } else {
         HTTPLogger.logConnectionFailure(var1);
      }

      super.hasException(var1);
   }

   public boolean timeout() {
      SocketMuxer.getMuxer().deliverEndOfStream(this.getSocketFilter());
      return super.timeout();
   }

   public void endOfStream() {
      this.blowAllChunks();
      super.endOfStream();
   }

   void requeue() {
      if (this.getSocket().isClosed()) {
         this.blowAllChunks();
         SocketMuxer.getMuxer().deliverEndOfStream(this.getSocketFilter());
      } else {
         boolean var1 = this.request.getInputHelper().getRequestParser().isMethodSafe();
         if (this.reuseRequestResponse) {
            this.request.reset();
            this.response.init();
         } else {
            this.request.skipUnreadBody();
            this.request = new ServletRequestImpl(this);
            this.response = new ServletResponseImpl(this.request, this.getSocketOutputStream());
         }

         int var2 = this.headerEndPos + 1;
         this.headerEndPos = 0;
         if (var1 && var2 < this.pos) {
            if (this.doNotPipeline) {
               SocketMuxer.getMuxer().deliverEndOfStream(this.getSocketFilter());
               return;
            }

            byte[] var3 = new byte[this.buf.length];
            System.arraycopy(this.buf, var2, var3, 0, this.pos - var2);
            this.buf = var3;
            this.pos -= var2;
            if (this.isMessageComplete()) {
               ++this.pipelinedRequests;
               this.dispatch();
               return;
            }
         } else {
            this.blowAllChunks();
         }

         if (this.getSocketFilter() instanceof SSLFilter && this.https) {
            ((SSLFilter)this.getSocketFilter()).asyncOn();
         }

         SocketMuxer.getMuxer().read(this.getSocketFilter());
      }
   }

   public boolean isMessageComplete() {
      if (this.buf == null) {
         return false;
      } else {
         boolean var1 = false;

         for(int var2 = 0; var2 < this.pos; ++var2) {
            switch (this.buf[var2]) {
               case 10:
                  if (var1) {
                     this.headerEndPos = var2;
                     return true;
                  }

                  var1 = true;
               case 13:
                  break;
               default:
                  var1 = false;
            }
         }

         return false;
      }
   }

   public void dispatch() {
      if (this.getSocketFilter() instanceof SSLFilter && this.https) {
         ((SSLFilter)this.getSocketFilter()).asyncOff();
      }

      this.request.getHttpAccountingInfo().setInvokeTime(System.currentTimeMillis());

      try {
         try {
            HttpRequestParser var1 = this.request.getInputHelper().getRequestParser();
            var1.parse(this.buf, this.pos);
            this.request.initFromRequestParser(var1);
            if (HTTPDebugLogger.isEnabled()) {
               HTTPDebugLogger.debug("Request received from: " + this.getRemoteAddress() + ", Secure: " + this.https + ", Request: " + this.request.toString());
            }

            this.response.getServletOutputStream().setWriteEnabled(!this.request.getInputHelper().getRequestParser().isMethodHead());
            this.incrementMessagesReceivedCount();
            this.incrementBytesReceivedCount((long)(this.headerEndPos + 1));
            if (this.request.getInputHelper().getRequestParser().isMethodTrace() && !this.httpServer.isHttpTraceSupportEnabled()) {
               this.sendError(this.response, 501);
               return;
            }
         } catch (HttpRequestParseException var7) {
            HttpRequestParser var2 = new HttpRequestParser(var7.getMethod(), var7.getProtocol(), var7.getURI());
            this.request.initFromRequestParser(var2);
            HTTPLogger.logMalformedRequest(var7.getURI(), -1);
            this.send400Response(var7);
            return;
         } catch (IllegalArgumentException var8) {
            this.send400Response(var8);
            return;
         }

         String var10 = this.request.getRequestHeaders().getHost();
         this.httpServer = this.findHttpServer(var10);
         this.response.initHttpServer(this.httpServer);
         if (HTTPDebugLogger.isEnabled()) {
            HTTPDebugLogger.debug("Http Server: '" + this.httpServer + "' is resolved for request: '" + this.request.toStringSimple() + "'");
         }

         String var11 = this.request.getInputHelper().getNormalizedURI();
         OnDemandManager var3 = this.httpServer.getOnDemandManager();
         OnDemandContext var4 = var3.lookupOnDemandContext(var11);
         if (var4 != null) {
            try {
               if (var4.isDisplayRefresh()) {
                  var3.loadOnDemandURI(var4, true);
                  this.sendRefreshPage(var11, var4.updateProgressIndicator());
                  return;
               }

               this.handleSyncOnDemandLoad(var3, var4, var11, this);
               return;
            } catch (DeploymentException var6) {
               this.sendError(this.response, 503);
               HTTPLogger.logDispatchError(var6);
               return;
            }
         }

         this.resolveServletContext(var11);
      } catch (IOException var9) {
         HTTPLogger.logDispatchError(var9);
         SocketMuxer.getMuxer().deliverEndOfStream(this.getSocketFilter());
      }

   }

   public long getMessagesReceivedCount() {
      return this.messagesReceived;
   }

   public long getBytesReceivedCount() {
      return this.bytesReceived;
   }

   void disableRequestResponseReuse() {
      this.reuseRequestResponse = false;
   }

   private void resolveServletContext(String var1) throws IOException {
      ServletContextManager var2 = this.httpServer.getServletContextManager();
      ContextVersionManager var3 = null;
      if (var1 != null) {
         var3 = var2.resolveVersionManagerForURI(var1);
      }

      if (var3 == null) {
         this.handleNoContext();
      } else {
         WebAppServletContext var4;
         if (var3.isVersioned()) {
            this.request.initContextManager(var3);
            var4 = this.request.getContext();
            this.request.getSessionHelper().resetSession(true);
            this.request.getRequestParameters().resetQueryParams();
         } else {
            var4 = var3.getContext(this.request.isAdminChannelRequest());
            this.request.initContext(var4);
         }

         this.response.initContext(var4);
         if (HTTPDebugLogger.isEnabled()) {
            HTTPDebugLogger.debug("Servlet Context: " + var4 + " is resolved for request: '" + this.request.toStringSimple() + "'");
         }

         if (this.initAndValidateRequest(var4)) {
            WorkManager var5 = this.request.getServletStub().getWorkManager();
            if (var5 == null) {
               throw new AssertionError("Could not determine WorkManager for : " + this.request);
            } else {
               var5.schedule(this.request);
            }
         }
      }
   }

   private void send400Response(Exception var1) throws IOException {
      this.response.setStatus(400);
      if (HTTPDebugLogger.isEnabled()) {
         HTTPDebugLogger.debug("Request parsing failed.", var1);
      }

      String var2 = this.request.getProtocol();
      if (var2 == null && var1 instanceof HttpRequestParseException) {
         var2 = ((HttpRequestParseException)var1).getProtocol();
      }

      if (var2 == null) {
         var2 = "HTTP/1.1";
      }

      String var3 = var2 + " " + 400 + " Bad Request\r\nConnection: close\r\n\r\n";

      try {
         this.getSocketOutputStream().write(var3.getBytes());
      } finally {
         this.httpServer.getLogManager().log(this.request, this.response);
      }

      SocketMuxer.getMuxer().deliverEndOfStream(this.getSocketFilter());
   }

   private HttpServer findHttpServer(String var1) {
      HttpServer var2 = WebService.getVirtualHost(this.getChannel());
      if (var2 != null) {
         if (HTTPDebugLogger.isEnabled()) {
            HTTPDebugLogger.debug("Found virtual host: " + var2.getName() + " for channel: " + this.getChannel().getChannelName());
         }

         return var2;
      } else {
         return WebService.findHttpServer(var1);
      }
   }

   private void blowAllChunks() {
      if (this.head != null) {
         Chunk.releaseChunks(this.head);
         this.buf = null;
         this.head = null;
      }

      this.pos = 0;
   }

   private void initInputStream() throws IOException {
      int var1 = this.request.getContentLength();
      boolean var2 = this.request.getRequestHeaders().isChunked();
      if (!this.request.getInputHelper().getRequestParser().isMethodSafe()) {
         if (var1 != -1 && var2) {
            if (HTTPDebugLogger.isEnabled()) {
               HTTPDebugLogger.debug("Content-Length header ignored as the Chunked Transfer-Encoding header exists.");
            }

            var1 = -1;
            this.request.getRequestHeaders().ignoreContentLength();
         }

         if (var1 == -1) {
            if (!this.request.getInputHelper().getRequestParser().isMethodPost() && !this.request.getInputHelper().getRequestParser().isMethodOptions()) {
               this.response.disableKeepAlive();
            }

            if (!var2) {
               this.request.setInputStream((InputStream)NullInputStream.getInstance());
               return;
            }
         }

         PostInputStream var3 = new PostInputStream(this, var1, this.buf, this.headerEndPos + 1, this.pos);
         this.setSoTimeout(this.httpServer.getPostTimeoutSecs() * 1000);
         if (var2) {
            this.request.setInputStream((InputStream)(new HttpChunkInputStream(var3)));
         } else {
            this.request.setInputStream((InputStream)var3);
         }

      } else {
         this.request.setInputStream((InputStream)NullInputStream.getInstance());
         if (var1 > 0 || var2) {
            if (HTTPDebugLogger.isEnabled()) {
               HTTPDebugLogger.debug(this.request.getInputHelper().getRequestParser().getMethod() + " request has Content-Length or Chunked Transfer-Encoding. " + "Pipelining is turned off for this request.");
            }

            this.response.disableKeepAlive();
            this.doNotPipeline = true;
         }

      }
   }

   boolean initAndValidateRequest(WebAppServletContext var1) throws IOException {
      if (!this.request.validate(this.httpServer)) {
         return false;
      } else if (this.request.getInputHelper().getRequestParser().isMethodTrace() && !this.httpServer.isHttpTraceSupportEnabled()) {
         this.sendError(this.response, 501);
         return false;
      } else {
         this.request.initInputEncoding();
         if (var1.getConfigManager().useDefaultEncoding()) {
            this.response.setDefaultEncoding(var1.getConfigManager().getDefaultEncoding());
         }

         if (this.pipelinedRequests >= 100) {
            this.response.disableKeepAlive();
         }

         this.initInputStream();
         if (!doNotSendContinueHeader && this.request.getInputHelper().getRequestParser().isProtocolVersion_1_1() && (var1.getSecurityManager().getWebAppSecurity().getAuthMethod() == null || var1.getSecurityManager().getWebAppSecurity().isFormAuth())) {
            String var2 = this.request.getRequestHeaders().getExpect();
            if ("100-continue".equalsIgnoreCase(var2)) {
               this.request.send100ContinueResponse();
            }
         }

         try {
            this.request.setServletStub(var1.resolveDirectRequest(this.request));
            return true;
         } catch (SecurityException var3) {
            this.response.setStatus(403);
            HTTPLogger.logException(var1.getLogContext(), var3);
            SocketMuxer.getMuxer().deliverEndOfStream(this.getSocketFilter());
            return false;
         }
      }
   }

   void incrementBytesReceivedCount(long var1) {
      this.bytesReceived += var1;
   }

   void incrementMessagesReceivedCount() {
      ++this.messagesReceived;
   }

   private void handleNoContext() throws IOException {
      this.response.setStatus(404);
      if (HTTPDebugLogger.isEnabled()) {
         Loggable var1 = HTTPLogger.logNoContextLoggable(this.httpServer.toString(), this.request.getInputHelper().getNormalizedURI());
         HTTPDebugLogger.debug(var1.getMessage());
      }

      this.response.getServletOutputStream().setWriteEnabled(!this.request.getInputHelper().getRequestParser().isMethodHead());
      if (!WebAppShutdownService.isSuspending() && !WebAppShutdownService.isSuspended()) {
         this.sendError(this.response, 404);
      } else {
         this.sendError(this.response, 503);
      }

   }

   void sendError(final ServletResponseImpl var1, final int var2) {
      Runnable var3 = new Runnable() {
         public void run() {
            try {
               var1.sendError(var2);
            } catch (Throwable var41) {
            } finally {
               try {
                  var1.sendError(var2);
               } catch (Throwable var39) {
               } finally {
                  MuxableSocketHTTP.this.httpServer.getLogManager().log(MuxableSocketHTTP.this.request, MuxableSocketHTTP.this.response);
                  SocketMuxer.getMuxer().deliverEndOfStream(MuxableSocketHTTP.this.getSocketFilter());
               }

            }

         }
      };
      WorkManagerFactory.getInstance().getDefault().schedule(var3);
   }

   private void sendRefreshPage(String var1, int var2) throws IOException {
      if (HTTPDebugLogger.isEnabled()) {
         HTTPDebugLogger.debug("Sending refresh screen for on demand deploy.");
      }

      char[] var3 = new char[var2];

      for(int var4 = 0; var4 < var2; ++var4) {
         var3[var4] = '.';
      }

      HTTPTextTextFormatter var7 = new HTTPTextTextFormatter(this.request.getLocale());
      String var5 = var7.getRefreshPageHTML(var1, new String(var3));
      ServletOutputStream var6 = this.response.getOutputStream();
      this.response.setContentLength(var5.length());
      this.response.setHeader("Connection", "close");
      var6.write(var5.getBytes());
      var6.flush();
      SocketMuxer.getMuxer().deliverEndOfStream(this.getSocketFilter());
   }

   void handleSyncOnDemandLoad(final OnDemandManager var1, final OnDemandContext var2, final String var3, final MuxableSocketHTTP var4) {
      Runnable var5 = new Runnable() {
         public void run() {
            try {
               var1.loadOnDemandURI(var2, false);
               if (HTTPDebugLogger.isEnabled()) {
                  HTTPDebugLogger.debug("About to perform rest of servlet context processing for " + var2.getAppName());
               }

               var4.resolveServletContext(var3);
            } catch (IOException var2x) {
               HTTPLogger.logDispatchError(var2x);
               SocketMuxer.getMuxer().deliverEndOfStream(MuxableSocketHTTP.this.getSocketFilter());
            } catch (DeploymentException var3x) {
               HTTPLogger.logDispatchError(var3x);
               SocketMuxer.getMuxer().deliverEndOfStream(MuxableSocketHTTP.this.getSocketFilter());
            }

         }
      };
      if (HTTPDebugLogger.isEnabled()) {
         HTTPDebugLogger.debug("Scheduling runnable for on demand load of " + var2.getAppName());
      }

      WorkManagerFactory.getInstance().getDefault().schedule(var5);
   }

   static {
      doNotSendContinueHeader = Boolean.getBoolean("doNotSendContinueHeader");
   }
}
