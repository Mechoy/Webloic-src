package weblogic.net.http;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.net.SocketTimeoutException;
import javax.net.ssl.SSLSocket;
import weblogic.kernel.KernelStatus;
import weblogic.protocol.ServerChannelManager;
import weblogic.security.utils.SSLIOContext;
import weblogic.security.utils.SSLIOContextTable;
import weblogic.socket.AbstractMuxableSocket;
import weblogic.socket.JSSESocket;
import weblogic.socket.MuxableSocket;
import weblogic.socket.SSLFilter;
import weblogic.socket.SocketMuxer;
import weblogic.socket.utils.JSSEUtils;
import weblogic.utils.Debug;
import weblogic.utils.io.Chunk;
import weblogic.utils.io.ChunkedInputStream;
import weblogic.work.ContextWrap;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

public final class AsyncResponseHandler {
   private static boolean disableAsyncResponse = Boolean.getBoolean("weblogic.http.DisableAsyncResponse");
   private static boolean disableAsyncResponseForHTTPS = Boolean.getBoolean("weblogic.http.DisableAsyncResponseForHTTPS");

   private AsyncResponseHandler() {
   }

   public static AsyncResponseHandler getInstance() {
      return AsyncResponseHandler.Factory.THE_ONE;
   }

   public static void disableAsyncResponse() {
      disableAsyncResponse = true;
   }

   public static void disableAsyncResponseForHTTPS() {
      disableAsyncResponseForHTTPS = true;
   }

   public void writeRequestAndRegister(HttpURLConnection var1, AsyncResponseCallback var2) throws IOException {
      this.writeRequestAndRegister(var1, var2, WorkManagerFactory.getInstance().getDefault());
   }

   public void writeRequestAndRegister(HttpURLConnection var1, AsyncResponseCallback var2, String var3) throws IOException {
      WorkManager var4 = WorkManagerFactory.getInstance().find(var3);
      if (var4 == null) {
         throw new IllegalArgumentException("WorkManager with name '" + var3 + "' cannot be found");
      } else {
         this.writeRequestAndRegister(var1, var2, var4);
      }
   }

   public void writeRequestAndRegister(HttpURLConnection var1, AsyncResponseCallback var2, WorkManager var3) throws IOException {
      if (!this.handleSynchronously(var1, var2, var3)) {
         Debug.assertion(KernelStatus.isServer(), "HTTP async response API cannot be used on clients");
         Debug.assertion(var1.isConnected(), "HTTP async response API cannot be invoked on a disconnected HttpURLConnection");
         if (var3 == null) {
            throw new IllegalArgumentException("WorkManager cannot be null !");
         } else {
            var1.writeRequestForAsyncResponse();
            MuxableSocketHTTPAsyncResponse var4 = this.getExistingMuxableSocket(var1);
            if (var4 != null) {
               var4.reRegister(var1, var2, var3);
               reRegisterWithMuxer(var4);
            }

            if (var4 == null) {
               this.registerWithMuxer(var1, var2, var3);
            }

         }
      }
   }

   private void registerWithMuxer(HttpURLConnection var1, AsyncResponseCallback var2, WorkManager var3) throws IOException {
      MuxableSocketHTTPAsyncResponse var4 = new MuxableSocketHTTPAsyncResponse(var1, var2, var3);
      var1.setMuxableSocket(var4);
      if (var1 instanceof HttpsURLConnection) {
         SSLSocket var5 = (SSLSocket)var1.getSocket();
         JSSESocket var6 = JSSEUtils.getJSSESocket(var5);
         if (var6 != null) {
            JSSEUtils.registerJSSEFilter(var6, var4);
            if (var4.isMessageComplete()) {
               var4.dispatch();
            } else {
               SocketMuxer.getMuxer().read(var6.getFilter());
            }

            var1.setScavenger(new Scavenger(var6.getFilter()));
         } else {
            SSLIOContext var7 = SSLIOContextTable.findContext(var5);
            SSLFilter var8 = (SSLFilter)var7.getFilter();
            var4.setSocketFilter(var8);
            var8.setDelegate(var4);
            var8.activateNoRegister();
            SocketMuxer.getMuxer().register(var8);
            SocketMuxer.getMuxer().read(var8);
            var1.setScavenger(new Scavenger(var8));
         }
      } else {
         SocketMuxer.getMuxer().register(var4);
         var1.setScavenger(new Scavenger(var4));
         SocketMuxer.getMuxer().read(var4);
      }

   }

   private static void reRegisterWithMuxer(MuxableSocket var0) {
      MuxableSocket var1 = var0.getSocketFilter();
      if (var1 instanceof SSLFilter) {
         ((SSLFilter)var1).asyncOn();
      }

      SocketMuxer.getMuxer().read(var1);
   }

   private MuxableSocketHTTPAsyncResponse getExistingMuxableSocket(HttpURLConnection var1) {
      return (MuxableSocketHTTPAsyncResponse)var1.getMuxableSocket();
   }

   private boolean handleSynchronously(HttpURLConnection var1, AsyncResponseCallback var2, WorkManager var3) throws IOException {
      if (disableAsyncResponse) {
         this.bypassMuxer(var1, var2, var3);
         return true;
      } else if (disableAsyncResponseForHTTPS && var1 instanceof HttpsURLConnection) {
         this.bypassMuxer(var1, var2, var3);
         return true;
      } else {
         return false;
      }
   }

   private void bypassMuxer(HttpURLConnection var1, AsyncResponseCallback var2, WorkManager var3) throws IOException {
      var1.writeRequests();
      var3.schedule(new MuxableSocketHTTPAsyncResponse.RunnableCallback(var2, var1));
   }

   // $FF: synthetic method
   AsyncResponseHandler(Object var1) {
      this();
   }

   private static class Scavenger implements Runnable {
      private MuxableSocket ms;

      Scavenger(MuxableSocket var1) {
         this.ms = var1;
      }

      public void run() {
         SocketMuxer.getMuxer().closeSocket(this.ms);
      }
   }

   static class MuxableSocketHTTPAsyncResponse extends AbstractMuxableSocket {
      private static final boolean DEBUG = false;
      private AsyncResponseCallback callback;
      private HttpURLConnection connection;
      private InputStream socketInputStream;
      private WorkManager workManager;
      private Runnable runnable;
      private Throwable exception;
      private boolean responseAvailable;

      MuxableSocketHTTPAsyncResponse(HttpURLConnection var1, AsyncResponseCallback var2, WorkManager var3) throws IOException {
         super(Chunk.getChunk(), var1.getSocket(), ServerChannelManager.findDefaultLocalServerChannel());
         this.init(var1, var2, var3);
      }

      public synchronized int getIdleTimeoutMillis() {
         return this.connection != null && this.connection.getTimeout() > 0 ? this.connection.getTimeout() : super.getIdleTimeoutMillis();
      }

      public boolean isMessageComplete() {
         return true;
      }

      public synchronized void dispatch() {
         if (this.connection != null && this.workManager != null) {
            this.invokeCallback();
         } else {
            this.responseAvailable = true;
         }

      }

      private void invokeCallback() {
         try {
            if (this.getSocketFilter() instanceof SSLFilter) {
               SSLFilter var1 = (SSLFilter)this.getSocketFilter();
               var1.asyncOff();
            }

            SequenceInputStream var5 = new SequenceInputStream(new ChunkedInputStream(this.head, 0), this.socketInputStream);
            this.connection.setInputStream(var5);
            this.responseAvailable = false;
            this.workManager.schedule(this.runnable);
         } finally {
            this.reset();
         }

      }

      private synchronized void handleError(Throwable var1) {
         if (this.workManager != null) {
            this.invokeCallbackWithErrorStream(new SocketClosedNotification());
         } else {
            this.exception = var1;
         }

      }

      private synchronized void handleTimeout() {
         if (this.connection != null && this.workManager != null) {
            this.invokeCallbackWithErrorStream(new SocketTimeoutNotification());
         }
      }

      private void invokeCallbackWithErrorStream(InputStream var1) {
         try {
            this.connection.setInputStream(var1);
            this.responseAvailable = false;
            this.workManager.schedule(this.runnable);
         } finally {
            this.reset();
         }

      }

      private void init(HttpURLConnection var1, AsyncResponseCallback var2, WorkManager var3) throws IOException {
         this.callback = var2;
         this.connection = var1;
         this.socketInputStream = new BufferedInputStream(var1.getSocket().getInputStream());
         this.workManager = var3;
         this.runnable = this.getRunnable();
      }

      public boolean closeSocketOnError() {
         return false;
      }

      private void reset() {
         this.callback = null;
         this.connection = null;
         this.workManager = null;
         this.runnable = null;
         this.socketInputStream = null;
         this.responseAvailable = false;
         this.prepareForReuse();
      }

      synchronized void reRegister(HttpURLConnection var1, AsyncResponseCallback var2, WorkManager var3) throws IOException {
         if (this.exception != null) {
            String var4 = this.exception.getMessage();
            this.exception = null;
            throw new IOException(var4);
         } else {
            this.init(var1, var2, var3);
            if (this.responseAvailable) {
               this.invokeCallback();
            }

         }
      }

      public void hasException(Throwable var1) {
         super.hasException(var1);
         this.handleError(var1);
      }

      public void endOfStream() {
         super.endOfStream();
         this.handleError(new IOException("End of stream delivered on " + this));
      }

      public boolean timeout() {
         this.handleTimeout();
         return true;
      }

      private Runnable getRunnable() {
         RunnableCallback var1 = new RunnableCallback(this.callback, this.connection);
         return (Runnable)(this.callback instanceof ContextAwareAsyncResponseCallback ? new ContextWrap(var1) : var1);
      }

      private void debug(String var1) {
      }

      private static class SocketClosedNotification extends InputStream {
         private static final IOException IOE = new IOException("SocketMuxer detected socket closure while waiting for a response");

         private SocketClosedNotification() {
         }

         public int read() throws IOException {
            throw IOE;
         }

         // $FF: synthetic method
         SocketClosedNotification(Object var1) {
            this();
         }
      }

      private static class SocketTimeoutNotification extends InputStream {
         private static final SocketTimeoutException STOE = new SocketTimeoutException();

         private SocketTimeoutNotification() {
         }

         public int read() throws IOException {
            throw STOE;
         }

         // $FF: synthetic method
         SocketTimeoutNotification(Object var1) {
            this();
         }
      }

      private static class RunnableCallback implements Runnable {
         private final AsyncResponseCallback callback;
         private final HttpURLConnection connection;

         RunnableCallback(AsyncResponseCallback var1, HttpURLConnection var2) {
            this.callback = var1;
            this.connection = var2;
         }

         public void run() {
            this.callback.handleResponse(this.connection);
         }
      }
   }

   private static final class Factory {
      static final AsyncResponseHandler THE_ONE = new AsyncResponseHandler();
   }
}
