package weblogic.management.remote.iiop;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.server.ServerNotActiveException;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.management.remote.rmi.RMIConnection;
import javax.management.remote.rmi.RMIConnectionImpl;
import javax.management.remote.rmi.RMIServerImpl;
import javax.security.auth.Subject;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.rmi.extensions.DisconnectEvent;
import weblogic.rmi.extensions.DisconnectListener;
import weblogic.rmi.extensions.PortableRemoteObject;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.rmi.spi.EndPoint;

public class IIOPServerImpl extends RMIServerImpl {
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugJMXCore");
   private final Map env;
   private Map disconnectListeners = new ConcurrentHashMap();

   public IIOPServerImpl(Map var1) {
      super(var1);
      this.env = var1 == null ? Collections.EMPTY_MAP : var1;
   }

   protected void export() throws IOException {
      PortableRemoteObject.exportObject(this);
   }

   public Remote toStub() throws IOException {
      Remote var1 = PortableRemoteObject.toStub(this);
      return var1;
   }

   protected RMIConnection makeClient(String var1, Subject var2) throws IOException {
      if (var1 == null) {
         throw new NullPointerException("Null connectionId");
      } else {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Make client for " + var1 + " subject " + var2);
         }

         RMIConnectionImpl var3 = new RMIConnectionImpl(this, var1, this.getDefaultClassLoader(), var2, this.env);
         PortableRemoteObject.exportObject(var3);

         try {
            EndPoint var4 = ServerHelper.getClientEndPoint();
            if (!var4.getHostID().isLocal()) {
               ConnectorListener var5 = new ConnectorListener(var3, var4);
               var4.addDisconnectListener((Remote)null, var5);
               this.disconnectListeners.put(var3, var5);
            }
         } catch (ServerNotActiveException var6) {
         }

         return var3;
      }
   }

   protected void closeClient(RMIConnection var1) throws IOException {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("IIOPServerImpl close client " + var1);
      }

      DisconnectListener var2 = (DisconnectListener)this.disconnectListeners.remove(var1);
      if (var2 != null) {
         var2.onDisconnect((DisconnectEvent)null);
      }

      PortableRemoteObject.unexportObject(var1);
   }

   protected String getProtocol() {
      return "iiop";
   }

   protected void closeServer() throws IOException {
      PortableRemoteObject.unexportObject(this);
   }

   private class ConnectorListener implements DisconnectListener {
      RMIConnection toMonitor;
      private EndPoint endPoint;

      ConnectorListener(RMIConnection var2, EndPoint var3) {
         this.toMonitor = var2;
         this.endPoint = var3;
      }

      public void onDisconnect(DisconnectEvent var1) {
         try {
            if (IIOPServerImpl.debugLogger.isDebugEnabled()) {
               IIOPServerImpl.debugLogger.debug("IIOPServerImpl onDisconnect called");
            }

            try {
               this.endPoint.removeDisconnectListener((Remote)null, this);
            } catch (Exception var3) {
               var3.printStackTrace();
            }

            this.toMonitor.close();
         } catch (IOException var4) {
            throw new AssertionError(var4);
         }
      }
   }
}
