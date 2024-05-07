package weblogic.iiop;

import java.io.IOException;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.kernel.ExecuteRequest;
import weblogic.kernel.ExecuteThread;
import weblogic.kernel.Kernel;
import weblogic.rmi.spi.RMIRuntime;
import weblogic.utils.StackTraceUtils;
import weblogic.utils.io.Chunk;

public final class ConnectionManager implements MessageDispatcher {
   private static final DebugLogger debugIIOPDetail = DebugLogger.getDebugLogger("DebugIIOPDetail");

   static void p(String var0) {
      System.err.println("<ConnectionManager: " + System.currentTimeMillis() + ">: " + var0);
   }

   private static ConnectionManager createDispatcher() {
      RMIRuntime.getRMIRuntime().addEndPointFinder(new EndPointManager());
      return new ConnectionManager();
   }

   public static ConnectionManager getConnectionManager() {
      return ConnectionManager.DispatcherMaker.dispatcher;
   }

   private ConnectionManager() {
   }

   public void dispatch(Connection var1, Chunk var2) {
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         p("dispatch");
      }

      try {
         EndPoint var3 = EndPointManager.findOrCreateEndPoint(var1);
         var3.dispatch(var2);
      } catch (Throwable var4) {
         this.gotExceptionReceiving(var1, var4);
      }

   }

   public void gotExceptionReceiving(Connection var1, Throwable var2) {
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled() || Kernel.getDebug().getDebugIIOP()) {
         IIOPLogger.logExceptionReceiving(var2);
      }

      this.handleConnectionShutdown(var1, var2);
   }

   public void gotExceptionSending(Connection var1, IOException var2) {
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled() || Kernel.getDebug().getDebugIIOP()) {
         IIOPLogger.logExceptionSending(var2);
      }

      this.handleConnectionShutdown(var1, var2);
   }

   synchronized void handleConnectionShutdown(Connection var1, Throwable var2) {
      EndPoint var3 = EndPointManager.removeConnection(var1);
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         p("shutting down " + var1 + " because <" + StackTraceUtils.throwable2StackTrace(var2) + "\n>");
      }

      if (var3 != null) {
         var3.cleanupPendingResponses(var2);
      }

      var1.close();
      if (var2 instanceof Error) {
         if (var2 instanceof OutOfMemoryError) {
            IIOPLogger.logOutOfMemory(var2);
            throw (OutOfMemoryError)var2;
         }

         if (var2 instanceof ThreadDeath) {
            throw (ThreadDeath)var2;
         }
      }

   }

   synchronized void forceConnectionShutdown(Connection var1, Throwable var2) {
      EndPoint var3 = EndPointManager.removeConnection(var1);
      if (var3 != null) {
         var3.cleanupPendingResponses(var2);
      }

      var1.close();
   }

   public void handleExceptionSending(final Connection var1, final IOException var2) {
      Kernel.execute(new ExecuteRequest() {
         public void execute(ExecuteThread var1x) throws Exception {
            ConnectionManager.this.gotExceptionSending(var1, var2);
         }
      });
   }

   private static final class DispatcherMaker {
      private static final ConnectionManager dispatcher = ConnectionManager.createDispatcher();
   }
}
