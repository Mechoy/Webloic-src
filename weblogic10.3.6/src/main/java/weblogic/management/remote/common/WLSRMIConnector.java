package weblogic.management.remote.common;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.Remote;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import javax.management.MBeanServerConnection;
import javax.management.remote.rmi.RMIConnector;
import javax.management.remote.rmi.RMIServer;
import javax.naming.NamingException;
import javax.security.auth.Subject;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.rmi.extensions.DisconnectEvent;
import weblogic.rmi.extensions.DisconnectListener;
import weblogic.rmi.extensions.DisconnectMonitor;
import weblogic.rmi.extensions.DisconnectMonitorListImpl;
import weblogic.rmi.extensions.DisconnectMonitorUnavailableException;
import weblogic.security.Security;

public class WLSRMIConnector extends RMIConnector implements DisconnectListener, WLSJMXConnector {
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugJMXCore");
   private Subject subject;
   private RMIServerWrapper server;
   private DisconnectMonitor monitor;
   private static Method isLocalMethod;

   public WLSRMIConnector(RMIServerWrapper var1, Map var2, Subject var3) {
      super(var1, var2);
      this.subject = var3;
      this.server = var1;
      if (!this.checkLocalWithReflection(var1)) {
         try {
            this.monitor = DisconnectMonitorListImpl.getDisconnectMonitor();
            this.monitor.addDisconnectListener(var1, this);
         } catch (DisconnectMonitorUnavailableException var5) {
         }

      }
   }

   private boolean checkLocalWithReflection(RMIServer var1) {
      if (isLocalMethod != null) {
         try {
            return (Boolean)isLocalMethod.invoke((Object)null, var1);
         } catch (IllegalAccessException var3) {
            throw new RuntimeException(var3);
         } catch (InvocationTargetException var4) {
            throw new RuntimeException(var4);
         }
      } else {
         return false;
      }
   }

   public void onDisconnect(DisconnectEvent var1) {
      try {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("WLSRMIConnector: onDisconnect ");
         }

         this.server.disconnected();
         this.close();
      } catch (IOException var3) {
      }

   }

   public void connect() throws IOException {
      try {
         Security.runAs(this.subject, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               WLSRMIConnector.this.doConnect();
               return null;
            }
         });
      } catch (PrivilegedActionException var2) {
         throw (IOException)var2.getException();
      }
   }

   private void doConnect() throws IOException {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("WLSRMIConnector: doConnect ");
      }

      super.connect();
   }

   public synchronized void connect(Map var1) throws IOException {
      final Map var2 = var1;

      try {
         Security.runAs(this.subject, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               WLSRMIConnector.this.doConnect(var2);
               return null;
            }
         });
      } catch (PrivilegedActionException var4) {
         throw (IOException)var4.getException();
      }
   }

   private synchronized void doConnect(Map var1) throws IOException {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("WLSRMIConnector: doConnect ");
      }

      super.connect(var1);
   }

   public void close() throws IOException {
      try {
         Security.runAs(this.subject, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               WLSRMIConnector.this.removeDisconnectListener();
               WLSRMIConnector.this.doClose();
               return null;
            }
         });
         if (this.server.ctx != null) {
            this.server.ctx.close();
            this.server.ctx = null;
         }
      } catch (PrivilegedActionException var2) {
         throw (IOException)var2.getException();
      } catch (NamingException var3) {
      }

   }

   private void doClose() throws IOException {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("WLSRMIConnector: doClose ");
      }

      super.close();
   }

   private void removeDisconnectListener() {
      try {
         if (this.monitor != null) {
            this.monitor.removeDisconnectListener(this.server, this);
         }
      } catch (DisconnectMonitorUnavailableException var2) {
      }

   }

   public synchronized MBeanServerConnection getMBeanServerConnection(Locale var1) throws IOException {
      MBeanServerConnection var2 = super.getMBeanServerConnection();
      if (this.server != null && this.server.connectionList != null) {
         String var3 = this.getConnectionId();
         synchronized(this.server.connectionList) {
            Iterator var5 = this.server.connectionList.iterator();

            while(var5.hasNext()) {
               RMIConnectionWrapper var6 = (RMIConnectionWrapper)((WeakReference)var5.next()).get();
               if (var6 != null && var3.equals(var6.getConnectionId())) {
                  var6.setLocale(var1);
                  break;
               }
            }

            return var2;
         }
      } else {
         return var2;
      }
   }

   public synchronized MBeanServerConnection getMBeanServerConnection(Subject var1, Locale var2) throws IOException {
      MBeanServerConnection var3 = super.getMBeanServerConnection(var1);
      if (this.server != null && this.server.connectionList != null) {
         String var4 = this.getConnectionId();
         Iterator var5 = this.server.connectionList.iterator();

         while(var5.hasNext()) {
            RMIConnectionWrapper var6 = (RMIConnectionWrapper)var5.next();
            if (var4.equals(var6.getConnectionId())) {
               var6.setLocale(var1, var2);
               break;
            }
         }

         return var3;
      } else {
         return var3;
      }
   }

   static {
      Class var0 = null;

      try {
         var0 = Class.forName("weblogic.rmi.extensions.server.ServerHelper");
      } catch (ClassNotFoundException var4) {
         isLocalMethod = null;
      }

      if (var0 != null) {
         Class[] var1 = new Class[]{Remote.class};

         try {
            isLocalMethod = var0.getDeclaredMethod("isLocal", var1);
         } catch (NoSuchMethodException var3) {
            isLocalMethod = null;
         }
      }

   }
}
