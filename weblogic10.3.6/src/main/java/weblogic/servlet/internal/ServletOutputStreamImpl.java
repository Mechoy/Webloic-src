package weblogic.servlet.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ProtocolException;
import java.net.SocketException;
import javax.servlet.ServletOutputStream;
import weblogic.socket.NIOConnection;
import weblogic.utils.io.Chunk;
import weblogic.utils.io.ChunkedDataOutputStream;

public final class ServletOutputStreamImpl extends ServletOutputStream {
   private static final int DEFAULT_BUFFER_SIZE = 8192;
   private final ServletResponseImpl response;
   private final OutputStream out;
   private boolean headersSent;
   private boolean inFinish = false;
   private ChunkOutputWrapper co;
   private int clen;
   private boolean nativeControlsPipe = false;
   private boolean enforceCL;
   private boolean flushOK = true;
   private boolean doFinish = true;
   private HttpServer httper;
   private boolean commitCalled = false;
   private boolean writeBody;
   private static final byte[] FINAL_CHUNK = new byte[]{48, 48, 48, 48, 13, 10, 13, 10};

   public ServletOutputStreamImpl(OutputStream var1, ServletResponseImpl var2) {
      this.setHttpServer(WebService.defaultHttpServer());
      this.response = var2;
      this.out = var1;
      int var3 = 8192;
      if (var1 instanceof NIOConnection) {
         NIOConnection var4 = (NIOConnection)var1;
         if (var4.supportsGatheredWrites()) {
            var3 = var4.getOptimalNumberOfBuffers() * Chunk.CHUNK_SIZE;
         }
      }

      this.co = new ChunkOutputWrapper(ChunkOutput.create(var3, true, this.out, this));
      this.setBufferSize(var3);
      this.co.setChunking(false);
      this.clen = -1;
   }

   public void flush() throws IOException {
      try {
         if (this.flushOK) {
            if (!this.headersSent) {
               this.sendHeaders();
            }

            this.co.flush();
         }
      } catch (IOException var2) {
         if (!this.handleIOException(var2)) {
            throw var2;
         }
      } catch (RuntimeException var3) {
         if (!this.handleRuntimeException(var3)) {
            throw var3;
         }
      }

   }

   public void write(int var1) throws IOException {
      if (this.flushOK) {
         this.checkCL(1);

         try {
            this.co.writeByte(var1);
         } catch (IOException var3) {
            if (!this.handleIOException(var3)) {
               throw var3;
            }
         } catch (RuntimeException var4) {
            if (!this.handleRuntimeException(var4)) {
               throw var4;
            }
         }
      }

   }

   public final void write(byte[] var1, int var2, int var3) throws IOException {
      if (this.flushOK) {
         this.checkCL(var3);

         try {
            this.co.write(var1, var2, var3);
         } catch (IOException var5) {
            if (!this.handleIOException(var5)) {
               throw var5;
            }
         } catch (RuntimeException var6) {
            if (!this.handleRuntimeException(var6)) {
               throw var6;
            }
         }
      }

   }

   static boolean supportsGatheredWrites(OutputStream var0) {
      return var0 instanceof NIOConnection ? ((NIOConnection)var0).supportsGatheredWrites() : false;
   }

   void writeHeader(ChunkedDataOutputStream var1) throws IOException {
      if (supportsGatheredWrites(this.out) && this.writeBody) {
         this.co.setHttpHeaders(var1.getChunks());
      } else {
         var1.writeTo(this.out);
      }

   }

   public void print(String var1) throws IOException {
      if (this.flushOK) {
         if (var1 == null) {
            this.checkCL("null".length());
            this.co.print("null");
            return;
         }

         int var2 = var1.length();
         this.checkCL(var2);
         this.co.print(var1);
      }

   }

   private final void checkCL(int var1) throws IOException {
      if (this.enforceCL) {
         if (this.co.getTotal() + this.co.getCount() + var1 > this.clen) {
            throw new ProtocolException("Exceeded stated content-length of: '" + this.clen + "' bytes");
         }
      }
   }

   private boolean handleIOException(IOException var1) {
      this.flushOK = false;
      if (this.inFinish && var1 instanceof SocketException) {
         if (HTTPDebugLogger.isEnabled()) {
            HTTPDebugLogger.debug("Socket issue", var1);
         }

         return true;
      } else {
         return false;
      }
   }

   private boolean handleRuntimeException(RuntimeException var1) {
      if (this.inFinish && isNestedSocketException(var1)) {
         if (HTTPDebugLogger.isEnabled()) {
            HTTPDebugLogger.debug("Socket issue", var1);
         }

         this.flushOK = false;
         return true;
      } else {
         return false;
      }
   }

   private static boolean isNestedSocketException(RuntimeException var0) {
      Object var1;
      for(var1 = var0; var1 != null && !(var1 instanceof SocketException); var1 = ((Throwable)var1).getCause()) {
      }

      return var1 instanceof SocketException;
   }

   void sendHeaders() throws IOException {
      try {
         if (this.flushOK) {
            if (this.headersSent) {
               return;
            }

            if (this.response.getStatus() == 304) {
               this.clearCurrentBuffer();
               this.setWriteEnabled(false);
            } else if (!this.inFinish && this.clen == -1) {
               if (this.response.getRequest().getInputHelper().getRequestParser().isProtocolVersion_1_1() && !this.httper.getMBean().isChunkedTransferDisabled()) {
                  this.co.setChunking(true);
                  this.response.addHeader("Transfer-Encoding", "chunked");
               } else {
                  this.response.disableKeepAlive();
               }
            }

            this.response.writeHeaders();
            this.headersSent = true;
         }
      } catch (IOException var2) {
         if (this.flushOK && !this.handleIOException(var2)) {
            throw var2;
         }
      }

   }

   boolean headersSent() {
      return this.headersSent;
   }

   void flushBuffer() throws IOException {
      this.flush();
   }

   boolean isCommitted() {
      return this.headersSent;
   }

   void reset() throws IllegalStateException {
      this.clearBuffer();
      this.enforceCL = false;
      this.clen = -1;
      this.co.setChunking(false);
      this.nativeControlsPipe = false;
   }

   int getBufferSize() {
      return this.co.getBufferSize();
   }

   void setBufferSize(int var1) {
      this.co.setBufferSize(var1);
   }

   public void setNativeControlsPipe(boolean var1) throws IOException {
      if (!this.nativeControlsPipe && var1) {
         this.clearCurrentBuffer();
         this.setEnforceContentLength(false);
         this.response.writeHeaders();
         this.headersSent = true;
         this.co.setNativeControlsPipe(true);
      } else if (this.nativeControlsPipe && !var1) {
         this.clearCurrentBuffer();
         this.setEnforceContentLength(false);
         this.clen = -1;
         this.co.setChunking(false);
         this.co.setNativeControlsPipe(false);
      }

      this.nativeControlsPipe = var1;
   }

   private void setEnforceContentLength(boolean var1) {
      if (!this.writeBody) {
         this.enforceCL = false;
      } else {
         this.enforceCL = var1;
      }
   }

   public void clearBuffer() {
      if (this.isCommitted()) {
         throw new IllegalStateException("Response already committed");
      } else {
         this.clearCurrentBuffer();
      }
   }

   public void clearCurrentBuffer() {
      this.co.clearBuffer();
   }

   public int getCount() {
      return this.co.getCount();
   }

   int getTotal() {
      return this.co.getTotal();
   }

   void setOutput(ChunkOutputWrapper var1) {
      this.co = var1;
   }

   public ChunkOutputWrapper getOutput() {
      return this.co;
   }

   void setHttpServer(HttpServer var1) {
      this.httper = var1;
   }

   public void setContentLength(int var1) throws ProtocolException {
      if (this.enforceCL && this.headersSent) {
         throw new ProtocolException("Content-Length already set");
      } else {
         if (this.writeBody) {
            this.enforceCL = true;
         }

         this.clen = var1;
         this.co.setCL(this.clen);
         if (this.useKeepAliveHeader()) {
            this.response.setHeaderInternal("Connection", "Keep-Alive");
         }

      }
   }

   private boolean useKeepAliveHeader() {
      if (this.response.getRequest().getInputHelper().getRequestParser().isProtocolVersion_1_1()) {
         return false;
      } else if (this.response.hasKeepAliveHeader()) {
         return false;
      } else {
         return this.response.getUseKeepAlive();
      }
   }

   boolean getDoFinish() {
      return this.doFinish;
   }

   public void setDoFinish(boolean var1) {
      this.doFinish = var1;
   }

   static void p(String var0) {
      System.err.println("[ServletOS]: " + var0);
   }

   void ensureContentLength(long var1) throws IOException {
      if (this.enforceCL && this.flushOK && this.writeBody) {
         long var3 = (long)(this.co.getTotal() + this.co.getCount()) + var1;
         if ((long)this.clen != var3) {
            if (!this.isCommitted()) {
               try {
                  this.response.sendError(500);
               } catch (Throwable var6) {
               }
            }

            throw new ProtocolException("Didn't meet stated Content-Length, wrote: '" + var3 + "' bytes instead of stated: '" + this.clen + "' bytes.");
         }
      }
   }

   public void commit() throws IOException {
      if (!this.commitCalled) {
         if (this.doFinish) {
            if (!this.nativeControlsPipe) {
               if (!this.headersSent && !this.enforceCL) {
                  if (!this.writeBody) {
                     this.response.setContentLength(this.clen);
                  } else if (this.response.getStatus() != 304) {
                     this.response.setContentLength(this.co.getTotal() + this.co.getCount());
                  }
               }

               try {
                  this.inFinish = true;
                  this.flush();
                  if (this.co.isChunking() && this.flushOK && this.writeBody) {
                     try {
                        this.out.write(FINAL_CHUNK);
                     } catch (IOException var6) {
                        if (!this.handleIOException(var6)) {
                           throw var6;
                        }
                     } catch (RuntimeException var7) {
                        if (!this.handleRuntimeException(var7)) {
                           throw var7;
                        }
                     }

                     this.response.incrementBytesSentCount((long)FINAL_CHUNK.length);
                  }
               } finally {
                  this.inFinish = false;
                  this.co.setChunking(false);
                  this.clen = -1;
                  this.enforceCL = false;
                  this.co.setWriteEnabled(false);
                  this.commitCalled = true;
               }

            }
         }
      }
   }

   public void finish() throws IOException {
      if (!this.commitCalled) {
         this.commit();
      }

      this.response.resetOutputState();
      this.commitCalled = false;
      this.co.setWriteEnabled(true);
      this.co.setAutoFlush(true);
      if (this.flushOK && this.response.getUseKeepAlive()) {
         this.co.reset();
         if (this.co.getEncoding() != null) {
            this.co.changeToCharset((String)null, this.response.getContext().getConfigManager().getCharsetMap());
         }

         this.headersSent = false;
      } else {
         this.co.release();
      }

   }

   public void writeStream(InputStream var1) throws IOException {
      this.writeStream(var1, -1);
   }

   public void writeStream(InputStream var1, int var2) throws IOException {
      try {
         this.co.writeStream(var1, var2);
      } catch (IOException var4) {
         if (!this.handleIOException(var4)) {
            throw var4;
         }
      } catch (RuntimeException var5) {
         if (!this.handleRuntimeException(var5)) {
            throw var5;
         }
      }

   }

   public void writeStreamWithEncoding(InputStream var1) throws IOException {
      this.writeStream(var1, -1);
   }

   OutputStream getRawOutputStream() {
      return this.out;
   }

   void setWriteEnabled(boolean var1) {
      this.writeBody = var1;
      this.co.setWriteEnabled(this.writeBody);
   }
}
