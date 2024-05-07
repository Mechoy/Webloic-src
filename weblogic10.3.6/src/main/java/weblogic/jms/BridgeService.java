package weblogic.jms;

import java.security.AccessController;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import javax.management.NotificationBroadcaster;
import javax.management.NotificationListener;
import javax.naming.NamingException;
import javax.naming.event.EventContext;
import javax.naming.event.NamingEvent;
import javax.naming.event.NamingExceptionEvent;
import javax.naming.event.ObjectChangeListener;
import weblogic.cluster.migration.MigrationException;
import weblogic.cluster.migration.MigrationManager;
import weblogic.jms.bridge.internal.BridgeDebug;
import weblogic.jms.bridge.internal.MessagingBridge;
import weblogic.jms.bridge.internal.MessagingBridgeException;
import weblogic.jndi.Environment;
import weblogic.kernel.Kernel;
import weblogic.management.DeploymentException;
import weblogic.management.UndeploymentException;
import weblogic.management.configuration.DeploymentMBean;
import weblogic.management.configuration.MessagingBridgeMBean;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.internal.DeploymentHandler;
import weblogic.management.internal.DeploymentHandlerContext;
import weblogic.management.internal.DeploymentHandlerHome;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

public final class BridgeService extends AbstractServerService implements DeploymentHandler {
   private static final int STATE_INITIALIZING = 0;
   private static final int STATE_SUSPENDING = 1;
   private static final int STATE_SUSPENDED = 2;
   private static final int STATE_STARTED = 4;
   private static final int STATE_SHUTTING_DOWN = 8;
   private static final int STATE_CLOSED = 16;
   private static final String BRIDGE_WORK_MANAGER_NAME = "weblogic.jms.MessagingBridge";
   private int state = 0;
   private boolean initialized;
   private final HashMap bridges;
   private long bridgesHighCount;
   private long bridgesTotalCount;
   private final HashSet adapterMBeans;
   private ServerMBean serverMBean;
   private String domainName;
   private static BridgeService bridgeService;
   private final HashMap registrations;
   private WorkManager workManager;
   private EventContext src;
   final BridgeDebug bridgeDebug = new BridgeDebug();

   public BridgeService() {
      bridgeService = this;
      this.adapterMBeans = new HashSet();
      this.bridges = new HashMap();
      this.registrations = new HashMap();
   }

   private void initialize() throws ServiceFailureException {
      try {
         Environment var1 = new Environment();
         var1.setCreateIntermediateContexts(true);
         this.src = (EventContext)var1.getInitialContext();
      } catch (Exception var4) {
         throw new ServiceFailureException(var4);
      }

      DeploymentHandlerHome.addDeploymentHandler(this);
      synchronized(this) {
         this.state = 2;
      }

      if (BridgeDebug.MessagingBridgeStartup.isDebugEnabled()) {
         BridgeDebug.MessagingBridgeStartup.debug("Bridge is initialized");
      }

   }

   public void stop() throws ServiceFailureException {
      this.suspend(false);
      this.shutdown();
   }

   public void halt() throws ServiceFailureException {
      this.suspend(true);
      this.shutdown();
   }

   public void start() throws ServiceFailureException {
      this.initialize();
      Iterator var1;
      synchronized(this) {
         if ((this.state & 4) != 0) {
            return;
         }

         if ((this.state & 16) != 0) {
            BridgeLogger.logBridgeFailedInit();
            return;
         }

         this.state = 4;
         var1 = ((HashMap)this.bridges.clone()).values().iterator();
      }

      if (BridgeDebug.MessagingBridgeStartup.isDebugEnabled()) {
         BridgeDebug.MessagingBridgeStartup.debug("About to start bridge service: state=" + this.state);
      }

      while(var1.hasNext()) {
         MessagingBridge var2 = (MessagingBridge)var1.next();

         try {
            var2.resume();
         } catch (Exception var4) {
            BridgeLogger.logErrorStartBridge(var2.getName(), var4);
         }
      }

   }

   private void suspend(boolean var1) {
      Iterator var2;
      synchronized(this) {
         if ((this.state & 25) != 0) {
            return;
         }

         this.state = 1;
         var2 = ((HashMap)this.bridges.clone()).values().iterator();
      }

      DeploymentHandlerHome.removeDeploymentHandler(this);

      while(true) {
         boolean var13 = false;

         try {
            var13 = true;
            if (!var2.hasNext()) {
               var13 = false;
               break;
            }

            MessagingBridge var3 = (MessagingBridge)var2.next();
            var3.suspend(var1);
         } finally {
            if (var13) {
               synchronized(this) {
                  this.state = 2;
               }
            }
         }
      }

      synchronized(this) {
         this.state = 2;
      }
   }

   private synchronized boolean isShutdown() {
      return (this.state & 24) != 0;
   }

   private synchronized void checkShutdown() throws MessagingBridgeException {
      if (this.isShutdown()) {
         throw new MessagingBridgeException("Messaging Bridge Service is shutdown.");
      }
   }

   private void shutdown() {
      boolean var18 = false;

      label164: {
         try {
            var18 = true;
            Iterator var1;
            synchronized(this) {
               if ((this.state & 16 | 8) != 0) {
                  var18 = false;
                  break label164;
               }

               this.state = 8;
               Iterator var3 = this.bridges.values().iterator();
               var1 = ((HashMap)this.bridges.clone()).values().iterator();

               while(var3.hasNext()) {
                  ((MessagingBridge)var3.next()).markShuttingDown();
               }
            }

            while(var1.hasNext()) {
               try {
                  ((MessagingBridge)var1.next()).shutdown();
               } catch (Throwable var23) {
               }
            }

            synchronized(this) {
               this.bridges.clear();
               this.adapterMBeans.clear();
               var18 = false;
            }
         } finally {
            if (var18) {
               synchronized(this) {
                  this.state = 16;
               }

               BridgeLogger.logBridgeShutdown();
            }
         }

         synchronized(this) {
            this.state = 16;
         }

         BridgeLogger.logBridgeShutdown();
         return;
      }

      synchronized(this) {
         this.state = 16;
      }

      BridgeLogger.logBridgeShutdown();
   }

   public String getDomainName() {
      return this.domainName;
   }

   public static BridgeService getBridgeService() {
      return bridgeService;
   }

   private MessagingBridge createMessagingBridge(MessagingBridgeMBean var1) throws DeploymentException {
      if (BridgeDebug.MessagingBridgeStartup.isDebugEnabled()) {
         BridgeDebug.MessagingBridgeStartup.debug("creating bridge " + var1.getName());
      }

      synchronized(this) {
         try {
            this.checkShutdown();
         } catch (MessagingBridgeException var5) {
            BridgeLogger.logErrorCreateBridgeWhenShutdown(var1.getName());
            throwDeploymentException("Error creating messaging bridge " + var1.getName(), var5);
         }
      }

      MessagingBridge var2 = null;

      try {
         var2 = new MessagingBridge(var1, this);
      } catch (Exception var7) {
         if (BridgeDebug.MessagingBridgeStartup.isDebugEnabled()) {
            BridgeDebug.MessagingBridgeStartup.debug("Error creating bridge " + var1.getName(), var7);
         }

         BridgeLogger.logErrorCreateBridge(var1.getName(), var7);
         throwDeploymentException("Error deploying messaging bridge " + var1.getName(), var7);
      }

      return var2;
   }

   private synchronized void addMessagingBridge(MessagingBridge var1) throws MessagingBridgeException {
      this.checkShutdown();
      if (this.bridges.put(var1.getName(), var1) == null) {
         this.bridgesHighCount = Math.max((long)this.bridges.size(), this.bridgesHighCount);
         ++this.bridgesTotalCount;
      }

   }

   private void removeMessagingBridge(MessagingBridge var1) {
      if (var1 != null) {
         synchronized(this) {
            var1.markShuttingDown();
            this.bridges.remove(var1.getName());
         }

         try {
            var1.shutdown();
         } catch (Exception var4) {
         }
      }

   }

   public MessagingBridge[] getMessagingBridges() {
      MessagingBridge[] var1;
      Iterator var2;
      synchronized(this) {
         var1 = new MessagingBridge[this.bridges.size()];
         var2 = ((HashMap)this.bridges.clone()).values().iterator();
      }

      for(int var3 = 0; var2.hasNext(); var1[var3++] = (MessagingBridge)var2.next()) {
      }

      return var1;
   }

   private synchronized MessagingBridge findBridge(String var1) {
      return (MessagingBridge)this.bridges.get(var1);
   }

   private void addAdapter(String var1) {
      synchronized(this.adapterMBeans) {
         this.adapterMBeans.add(var1);
      }

      synchronized(this.registrations) {
         HashMap var2 = (HashMap)this.registrations.remove(var1);
         if (var2 != null) {
            Iterator var4 = var2.values().iterator();

            while(var4.hasNext()) {
               MessagingBridge var5 = (MessagingBridge)var4.next();

               try {
                  var5.resume();
               } catch (MessagingBridgeException var10) {
                  BridgeLogger.logErrorStartBridge(var5.getName(), var10);

                  try {
                     var5.shutdown();
                  } catch (Exception var9) {
                  }

                  var10.printStackTrace();
               }
            }

         }
      }
   }

   private void removeAdapter(String var1) {
      if (!this.isShutdown()) {
         Iterator var2;
         synchronized(this) {
            var2 = ((HashMap)this.bridges.clone()).values().iterator();
         }

         while(true) {
            MessagingBridge var3;
            MessagingBridgeMBean var4;
            do {
               if (!var2.hasNext()) {
                  synchronized(this.adapterMBeans) {
                     this.adapterMBeans.remove(var1);
                     return;
                  }
               }

               var3 = (MessagingBridge)var2.next();
               var4 = var3.getMBean();
            } while(!seperatedBySlash(var4.getSourceDestination().getAdapterJNDIName()).equals(var1) && !seperatedBySlash(var4.getTargetDestination().getAdapterJNDIName()).equals(var1));

            var3.suspend(false);
            this.registerForAdapterDeployment(var1, var3);
         }
      }
   }

   public boolean findAdapterAndRegister(String var1, MessagingBridge var2) {
      synchronized(this.adapterMBeans) {
         if (this.adapterMBeans.contains(seperatedBySlash(var1))) {
            return true;
         } else {
            this.registerForAdapterDeployment(var1, var2);
            return false;
         }
      }
   }

   private void registerForAdapterDeployment(String var1, MessagingBridge var2) {
      String var3 = seperatedBySlash(var1);
      synchronized(this.registrations) {
         HashMap var5 = (HashMap)this.registrations.get(var3);
         if (var5 == null) {
            var5 = new HashMap();
            this.registrations.put(var3, var5);

            try {
               this.src.addNamingListener(var1, 0, new AdapterChangeHandler());
            } catch (NamingException var8) {
               var8.printStackTrace();
            }
         }

         var5.put(var2.getName(), var2);
      }
   }

   private static String seperatedBySlash(String var0) {
      String var1 = "";

      for(int var2 = var0.indexOf("."); var2 >= 0; var2 = var0.indexOf(".")) {
         var1 = var1 + var0.substring(0, var2) + "/";
         var0 = var0.substring(var2 + 1);
      }

      var1 = var1 + var0;
      return var1;
   }

   public static String seperatedByDot(String var0) {
      String var1 = "";

      for(int var2 = var0.indexOf("/"); var2 >= 0; var2 = var0.indexOf("/")) {
         var1 = var1 + var0.substring(0, var2) + ".";
         var0 = var0.substring(var2 + 1);
      }

      var1 = var1 + var0;
      return var1;
   }

   public static void removeNotificationListener(NotificationBroadcaster var0, NotificationListener var1) {
      try {
         var0.removeNotificationListener(var1);
      } catch (Exception var3) {
      }

   }

   public void prepareDeployment(DeploymentMBean var1, DeploymentHandlerContext var2) throws DeploymentException {
   }

   public void activateDeployment(DeploymentMBean var1, DeploymentHandlerContext var2) throws DeploymentException {
      if (var1 instanceof MessagingBridgeMBean) {
         this.addBridge((MessagingBridgeMBean)var1);
      }

   }

   public void deactivateDeployment(DeploymentMBean var1, DeploymentHandlerContext var2) throws UndeploymentException {
      if (!this.isShutdown()) {
         if (var1 instanceof MessagingBridgeMBean) {
            this.removeBridge((MessagingBridgeMBean)var1);
         }

      }
   }

   public void unprepareDeployment(DeploymentMBean var1, DeploymentHandlerContext var2) throws UndeploymentException {
   }

   private void addBridge(MessagingBridgeMBean var1) throws DeploymentException {
      if (this.findBridge(var1.getName()) == null) {
         MessagingBridge var2 = this.createMessagingBridge(var1);
         if (var1.getTargets()[0] instanceof MigratableTargetMBean) {
            if (BridgeDebug.MessagingBridgeStartup.isDebugEnabled()) {
               BridgeDebug.MessagingBridgeStartup.debug("Bridge " + var1.getName() + " is deployed as a migratable");
            }

            try {
               this.addMessagingBridge(var2);
               MigrationManager.singleton().register(var2, (MigratableTargetMBean)((MigratableTargetMBean)var1.getTargets()[0]));
            } catch (MigrationException var8) {
               var8.printStackTrace();
               throwDeploymentException("Failed to register with MigrationManager", var8);
            } catch (MessagingBridgeException var9) {
               var9.printStackTrace();
               throwDeploymentException("Failed to add bridge " + var1.getName(), var9);
            }
         } else {
            try {
               var2.initialize();
               this.addMessagingBridge(var2);
            } catch (Exception var10) {
               if (BridgeDebug.MessagingBridgeStartup.isDebugEnabled()) {
                  BridgeDebug.MessagingBridgeStartup.debug("Error creating bridge " + var1.getName(), var10);
               }

               BridgeLogger.logErrorCreateBridge(var2.getName(), var10);

               try {
                  var2.shutdown();
               } catch (Exception var7) {
               }

               throwDeploymentException("Error deploying messaging bridge " + var2.getName(), var10);
            }

            try {
               var2.resume();
               BridgeLogger.logBridgeDeployed(var2.getName());
            } catch (Exception var6) {
               BridgeLogger.logErrorStartBridge(var2.getName(), var6);

               try {
                  var2.shutdown();
               } catch (Exception var5) {
               }

               this.removeMessagingBridge(var2);
               throwDeploymentException("Error deploying Bridge " + var1.getName(), var6);
            }
         }
      } else {
         if (BridgeDebug.MessagingBridgeStartup.isDebugEnabled()) {
            BridgeDebug.MessagingBridgeStartup.debug("Bridge " + var1.getName() + " already exists");
         }

         throwDeploymentException("Bridge " + var1.getName() + " already exists", (Exception)null);
      }

   }

   private void removeBridge(MessagingBridgeMBean var1) throws UndeploymentException {
      MessagingBridge var2 = this.findBridge(var1.getName());
      if (var2 != null) {
         this.removeMessagingBridge(var2);
      } else if (BridgeDebug.MessagingBridgeStartup.isDebugEnabled()) {
         BridgeDebug.MessagingBridgeStartup.debug("Error removing Bridge " + var2 + ": instance doesn't exist");
      }

   }

   public synchronized WorkManager getWorkManager() {
      if (this.workManager == null) {
         int var1 = -1;
         AuthenticatedSubject var2 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
         ServerMBean var3 = ManagementService.getRuntimeAccess(var2).getServer();
         if (var3.getUse81StyleExecuteQueues()) {
            var1 = Kernel.getConfig().getMessagingBridgeThreadPoolSize();
            if (var1 <= 0) {
               var1 = 10;
            }
         }

         this.workManager = WorkManagerFactory.getInstance().findOrCreate("weblogic.jms.MessagingBridge", 1, var1);
      }

      return this.workManager;
   }

   private static void throwDeploymentException(String var0, Exception var1) throws DeploymentException {
      DeploymentException var2;
      if (var1 != null) {
         var2 = new DeploymentException(var0, var1);
      } else {
         var2 = new DeploymentException(var0);
      }

      throw var2;
   }

   private static void throwUndeploymentException(String var0, Exception var1) throws UndeploymentException {
      throw new UndeploymentException(var0);
   }

   private final class AdapterChangeHandler implements ObjectChangeListener {
      private AdapterChangeHandler() {
      }

      public void objectChanged(NamingEvent var1) {
         switch (var1.getType()) {
            case 0:
               BridgeService.this.addAdapter(BridgeService.seperatedBySlash(var1.getNewBinding().getName()));
               break;
            case 1:
               BridgeService.this.removeAdapter(BridgeService.seperatedBySlash(var1.getNewBinding().getName()));
            case 2:
            case 3:
         }

      }

      public void namingExceptionThrown(NamingExceptionEvent var1) {
         System.out.println(var1.getException());
      }

      // $FF: synthetic method
      AdapterChangeHandler(Object var2) {
         this();
      }
   }
}
