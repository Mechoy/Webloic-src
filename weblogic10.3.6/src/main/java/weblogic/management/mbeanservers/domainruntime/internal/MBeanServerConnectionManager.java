package weblogic.management.mbeanservers.domainruntime.internal;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.UnknownHostException;
import java.security.AccessController;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.context.JMXContext;
import weblogic.management.context.JMXContextHelper;
import weblogic.management.jmx.JMXLogger;
import weblogic.management.mbeanservers.Service;
import weblogic.management.mbeanservers.domainruntime.MBeanServerConnectionManagerMBean;
import weblogic.management.mbeanservers.internal.DomainServiceImpl;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.ConnectMonitorFactory;
import weblogic.protocol.ServerURL;
import weblogic.protocol.URLManager;
import weblogic.rmi.extensions.ConnectEvent;
import weblogic.rmi.extensions.ConnectListener;
import weblogic.rmi.extensions.ConnectMonitor;
import weblogic.rmi.extensions.DisconnectEvent;
import weblogic.rmi.extensions.DisconnectListener;
import weblogic.rmi.extensions.ServerDisconnectEvent;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;

public class MBeanServerConnectionManager extends DomainServiceImpl implements MBeanServerConnectionManagerMBean {
   private static final String JNDI = "/jndi/";
   private static DebugLogger debug = DebugLogger.getDebugLogger("DebugJMXDomain");
   private final Map locationToMBeanServerMap = new ConcurrentHashMap();
   private final Map connectionToServerNameMap = new ConcurrentHashMap();
   private final Map connectorsByConnectionMap = new ConcurrentHashMap();
   private final Map doubleConnectMap = new ConcurrentHashMap();
   private final ConcurrentHashMap connectingManagedServers = new ConcurrentHashMap();
   private final CopyOnWriteArrayList callbacks = new CopyOnWriteArrayList();
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public MBeanServerConnection lookupMBeanServerConnection(String var1) {
      return (MBeanServerConnection)this.locationToMBeanServerMap.get(var1);
   }

   MBeanServerConnectionManager() {
      super("MBeanServerConnectionManager", MBeanServerConnectionManagerMBean.class.getName(), (Service)null);
   }

   void initializeConnectivity() {
      this.registerLocalMBeanServer();
      this.pollAllManagedServers();
      this.startListening();
   }

   private void registerLocalMBeanServer() {
      String var1 = ManagementService.getRuntimeAccess(kernelId).getServerName();
      MBeanServer var2 = ManagementService.getRuntimeMBeanServer(kernelId);
      ManagedMBeanServerConnection var3 = new ManagedMBeanServerConnection(var2, var1, (JMXExecutor)null);
      this.locationToMBeanServerMap.put(var1, var3);
      this.connectionToServerNameMap.put(var3, var1);
      this.invokeConnectCallbacks(var1, var3);
   }

   void shutdown() {
      Set var1 = this.connectorsByConnectionMap.keySet();
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         JMXConnector var3 = (JMXConnector)var2.next();

         try {
            var3.close();
         } catch (IOException var5) {
         }
      }

      this.locationToMBeanServerMap.clear();
      this.connectionToServerNameMap.clear();
      this.connectorsByConnectionMap.clear();
   }

   void addCallback(MBeanServerConnectionListener var1) {
      this.callbacks.add(var1);
   }

   void removeCallback(MBeanServerConnectionListener var1) {
      this.callbacks.remove(var1);
   }

   private void startListening() {
      ConnectMonitor var1 = ConnectMonitorFactory.getConnectMonitor();
      var1.addConnectDisconnectListener(this.createConnectListener(), this.createDisconnectListener());
   }

   private void pollAllManagedServers() {
      if (debug.isDebugEnabled()) {
         debug.debug("Polling managed servers ");
      }

      ServerMBean[] var1 = ManagementService.getRuntimeAccess(kernelId).getDomain().getServers();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         String var3 = var1[var2].getName();
         MBeanServerConnection var4 = this.lookupMBeanServerConnection(var3);
         if (var4 == null) {
            CountDownLatch var5 = this.connectToManagedServer(var3);
            if (var5 != null) {
               try {
                  if (debug.isDebugEnabled()) {
                     debug.debug("CountDownLatch is not null ");
                  }

                  var5.await();
               } catch (InterruptedException var7) {
               }
            }
         }

         if (debug.isDebugEnabled()) {
            debug.debug("pollAllManagedServers: connect to server \"" + var3);
         }
      }

   }

   private ConnectListener createConnectListener() {
      return new ConnectListener() {
         public void onConnect(ConnectEvent var1) {
            String var2 = var1.getServerName();
            synchronized(MBeanServerConnectionManager.this.doubleConnectMap) {
               if (MBeanServerConnectionManager.this.locationToMBeanServerMap.containsKey(var2)) {
                  CountDownLatch var4 = (CountDownLatch)MBeanServerConnectionManager.this.connectingManagedServers.get(var2);
                  if (var4 != null) {
                     if (MBeanServerConnectionManager.debug.isDebugEnabled()) {
                        MBeanServerConnectionManager.debug.debug("ConnectListener: skipping connect since connect already in progress to " + var2);
                     }

                     return;
                  }

                  Integer var5 = (Integer)MBeanServerConnectionManager.this.doubleConnectMap.get(var2);
                  var5 = MBeanServerConnectionManager.this.incrementInteger(var5);
                  MBeanServerConnectionManager.this.doubleConnectMap.put(var2, var5);
                  JMXLogger.logDisconnectedJMXConnectionWithManagedServer(var2);
                  MBeanServerConnectionManager.this.cleanupDisconnectedServer(var2);
               }
            }

            CountDownLatch var3 = MBeanServerConnectionManager.this.connectToManagedServer(var2);
            if (var3 != null && MBeanServerConnectionManager.debug.isDebugEnabled()) {
               MBeanServerConnectionManager.debug.debug("ConnectListener: connect to server \"" + var2);
            }

         }
      };
   }

   private Integer decrementInteger(Integer var1) {
      return var1 == null ? new Integer(-1) : new Integer(var1 + 1);
   }

   private Integer incrementInteger(Integer var1) {
      return var1 == null ? new Integer(1) : new Integer(var1 - 1);
   }

   private CountDownLatch connectToManagedServer(String var1) {
      CountDownLatch var2 = new CountDownLatch(1);
      CountDownLatch var3 = (CountDownLatch)this.connectingManagedServers.putIfAbsent(var1, var2);
      if (var3 != null) {
         if (debug.isDebugEnabled()) {
            debug.debug("connectToManagedServer return oldLatch when connecting to " + var1);
         }

         return var3;
      } else {
         try {
            JMXServiceURL var4;
            if (this.locationToMBeanServerMap.containsKey(var1)) {
               var4 = null;
               return var4;
            } else {
               var4 = null;
               String var5 = null;

               String var7;
               Exception var8;
               try {
                  var5 = URLManager.findAdministrationURL(var1);
                  ServerURL var6;
                  if (var5 == null) {
                     var6 = null;
                     return var6;
                  }

                  var6 = new ServerURL(var5);
                  var4 = new JMXServiceURL(var6.getProtocol(), var6.getHost(), var6.getPort(), "/jndi/weblogic.management.mbeanservers.runtime");
               } catch (MalformedURLException var27) {
                  var7 = var4 != null ? var4.toString() : "<none>";
                  JMXLogger.logManagedServerURLMalformed(var1, var7, var27);
                  throw new AssertionError(" Malformed URL" + var27);
               } catch (UnknownHostException var28) {
                  var7 = var4 != null ? var4.toString() : "<none>";
                  JMXLogger.logManagedServerNotAvailable(var1, var7);
                  var8 = null;
                  return var8;
               }

               ManagedMBeanServerConnection var30;
               JMXConnector var31;
               try {
                  HashMap var9;
                  try {
                     JMXContext var32 = JMXContextHelper.getJMXContext(true);
                     var32.setSubject(SecurityServiceManager.sendASToWire(kernelId).getSubject());
                     JMXContextHelper.putJMXContext(var32);
                     var9 = new HashMap(3);
                     var9.put("jmx.remote.protocol.provider.pkgs", "weblogic.management.remote");
                     JMXExecutor var10 = new JMXExecutor();
                     var9.put("jmx.remote.x.fetch.notifications.executor", var10);
                     var9.put("jmx.remote.x.notification.buffer.size", 50000);
                     int var11 = ManagementService.getRuntimeAccess(kernelId).getDomain().getJMX().getInvocationTimeoutSeconds();
                     if (var11 > 0) {
                        var9.put("jmx.remote.x.request.waiting.timeout", new Long((long)(var11 * 1000)));
                     }

                     if (var5 != null && var5.length() > 0) {
                        var9.put("java.naming.provider.url", var5);
                     }

                     if (debug.isDebugEnabled()) {
                        debug.debug("Connect to service URL " + var4);
                     }

                     var31 = JMXConnectorFactory.connect(var4, var9);
                     MBeanServerConnection var12 = var31.getMBeanServerConnection();
                     var30 = new ManagedMBeanServerConnection(var12, var1, var10);
                  } catch (Exception var25) {
                     var8 = var25;
                     JMXLogger.logUnableToContactManagedServer(var1, var4.toString(), var25);
                     var9 = null;
                     return var9;
                  }
               } finally {
                  JMXContextHelper.removeJMXContext();
               }

               JMXLogger.logEstablishedJMXConnectionWithManagedServer(var1, var4.toString());
               this.registerConnection(var1, var30, var31);
               var8 = null;
               return var8;
            }
         } finally {
            var2.countDown();
            this.connectingManagedServers.remove(var1);
         }
      }
   }

   private void registerConnection(String var1, ManagedMBeanServerConnection var2, JMXConnector var3) {
      synchronized(this) {
         this.locationToMBeanServerMap.put(var1, var2);
         this.connectionToServerNameMap.put(var2, var1);
         this.connectorsByConnectionMap.put(var2, var3);
      }

      this.invokeConnectCallbacks(var1, var2);
   }

   private DisconnectListener createDisconnectListener() {
      return new DisconnectListener() {
         public void onDisconnect(DisconnectEvent var1) {
            String var2 = ((ServerDisconnectEvent)var1).getServerName();
            synchronized(MBeanServerConnectionManager.this.doubleConnectMap) {
               Integer var4 = (Integer)MBeanServerConnectionManager.this.doubleConnectMap.get(var2);
               if (var4 != null) {
                  var4 = MBeanServerConnectionManager.this.decrementInteger(var4);
                  if (var4 < 1) {
                     MBeanServerConnectionManager.this.doubleConnectMap.remove(var2);
                  } else {
                     MBeanServerConnectionManager.this.doubleConnectMap.put(var2, var4);
                  }

                  return;
               }
            }

            JMXLogger.logDisconnectedJMXConnectionWithManagedServer(var2);
            MBeanServerConnectionManager.this.cleanupDisconnectedServer(var2);
         }
      };
   }

   private void cleanupDisconnectedServer(String var1) {
      synchronized(this) {
         ManagedMBeanServerConnection var3 = (ManagedMBeanServerConnection)this.locationToMBeanServerMap.remove(var1);
         if (var3 == null) {
            return;
         }

         var3.disconnected();
         this.connectionToServerNameMap.remove(var3);
         this.connectorsByConnectionMap.remove(var3);
      }

      this.invokeDisconnectCallbacks(var1);
   }

   private void invokeDisconnectCallbacks(String var1) {
      synchronized(this.callbacks) {
         Iterator var3 = this.callbacks.iterator();

         while(var3.hasNext()) {
            MBeanServerConnectionListener var4 = (MBeanServerConnectionListener)var3.next();

            try {
               var4.disconnect(var1);
            } catch (Exception var7) {
               JMXLogger.logExceptionDuringJMXConnectivity(var7);
            }
         }

      }
   }

   private void invokeConnectCallbacks(String var1, MBeanServerConnection var2) {
      synchronized(this.callbacks) {
         Iterator var4 = this.callbacks.iterator();

         while(var4.hasNext()) {
            MBeanServerConnectionListener var5 = (MBeanServerConnectionListener)var4.next();

            try {
               var5.connect(var1, var2);
            } catch (Exception var8) {
               JMXLogger.logExceptionDuringJMXConnectivity(var8);
            }
         }

      }
   }

   public void iterateConnections(ConnectionCallback var1, boolean var2) throws IOException {
      Iterator var3 = this.locationToMBeanServerMap.values().iterator();

      while(var3.hasNext()) {
         MBeanServerConnection var4 = (MBeanServerConnection)var3.next();

         try {
            var1.connection(var4);
         } catch (IOException var6) {
            if (debug.isDebugEnabled()) {
               debug.debug("Failed while iterating remote connections ", var6);
            }

            if (var2) {
               throw var6;
            }
         }
      }

   }

   public void notifyNewMBeanServer(String var1) {
      CountDownLatch var2 = this.connectToManagedServer(var1);
      if (var2 != null) {
         try {
            var2.await();
         } catch (InterruptedException var4) {
         }
      }

   }

   public String lookupServerName(MBeanServerConnection var1) {
      return (String)this.connectionToServerNameMap.get(var1);
   }

   public synchronized void stop() {
      JMXConnector[] var1 = (JMXConnector[])((JMXConnector[])this.connectorsByConnectionMap.values().toArray(new JMXConnector[this.connectorsByConnectionMap.size()]));

      for(int var2 = 0; var2 < var1.length; ++var2) {
         try {
            var1[var2].close();
         } catch (IOException var4) {
         }
      }

   }

   public interface ConnectionCallback {
      void connection(MBeanServerConnection var1) throws IOException;
   }

   public interface MBeanServerConnectionListener {
      void connect(String var1, MBeanServerConnection var2);

      void disconnect(String var1);
   }
}
