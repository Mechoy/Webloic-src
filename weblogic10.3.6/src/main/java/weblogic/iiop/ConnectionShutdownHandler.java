package weblogic.iiop;

import java.io.IOException;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;
import weblogic.utils.net.SocketResetException;
import weblogic.work.WorkAdapter;
import weblogic.work.WorkManagerFactory;

public final class ConnectionShutdownHandler extends WorkAdapter {
   private static final DebugCategory debugMarshal = Debug.getCategory("weblogic.iiop.marshal");
   private static final DebugCategory debugTransport = Debug.getCategory("weblogic.iiop.transport");
   private static final DebugLogger debugIIOPMarshal = DebugLogger.getDebugLogger("DebugIIOPMarshal");
   private static final DebugLogger debugIIOPTransport = DebugLogger.getDebugLogger("DebugIIOPTransport");
   private final Connection c;
   private final Throwable th;
   private final boolean sendClose;

   public ConnectionShutdownHandler(Connection var1, Throwable var2) {
      this(var1, var2, true);
   }

   public ConnectionShutdownHandler(Connection var1, Throwable var2, boolean var3) {
      this.c = var1;
      this.th = var2;
      this.sendClose = var3;
      WorkManagerFactory.getInstance().getSystem().schedule(this);
   }

   public void run() {
      if (this.sendClose) {
         this.closeConnection();
      }

      ConnectionManager.getConnectionManager().handleConnectionShutdown(this.c, this.th);
   }

   private void closeConnection() {
      EndPoint var1 = EndPointManager.removeConnection(this.c);
      if (var1 != null) {
         var1.cleanupPendingResponses(this.th);
      }

      if (var1 != null && var1.getMinorVersion() > 0 && (!(this.th instanceof IOException) || !SocketResetException.isResetException((IOException)this.th))) {
         CloseConnectionMessage var2 = new CloseConnectionMessage(var1);
         IIOPOutputStream var3 = var2.getOutputStream();
         var2.write(var3);

         try {
            var1.send(var3);
         } catch (Exception var5) {
            if ((debugMarshal.isEnabled() || debugIIOPMarshal.isDebugEnabled()) && this.th != null) {
               IIOPLogger.logDebugMarshalError("cannot deliver", this.th);
            }
         }

         var3.close();
         if (debugTransport.isEnabled() || debugIIOPTransport.isDebugEnabled()) {
            IIOPLogger.logDebugTransport("CLOSE_CONNECTION: sent");
         }
      }

   }
}
