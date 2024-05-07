package weblogic.cluster.messaging.internal;

import java.security.AccessController;
import java.util.Timer;
import java.util.TimerTask;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

public final class Environment {
   public static final boolean HTTP_PING = initProperty("weblogic.unicast.HttpPing");
   public static final boolean DEBUG = initProperty("weblogic.debug.DebugUnicastMessaging");
   private static ConfiguredServersMonitor configuration;
   private static ConnectionManager connectionManager;
   private static LogService logService;
   private static PropertyService propertyService;
   private static WorkManager dispatchWM;
   private static WorkManager forwardingWM;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   private static boolean initProperty(String var0) {
      try {
         return Boolean.getBoolean(var0);
      } catch (SecurityException var2) {
         return false;
      }
   }

   public static synchronized void initialize(ConfiguredServersMonitor var0, ConnectionManager var1) {
      setLogService(LogServiceImpl.getInstance());
      initialize(var0, var1, PropertyServiceImpl.getInstance());
   }

   public static synchronized boolean isInitialized() {
      return configuration != null;
   }

   public static synchronized void initialize(ConfiguredServersMonitor var0, ConnectionManager var1, PropertyService var2) {
      if (configuration != null) {
         throw new AssertionError("double initialization of unicast messaging!");
      } else {
         configuration = var0;
         connectionManager = var1;
         propertyService = var2;
         ClusterMBean var3 = ManagementService.getRuntimeAccess(kernelId).getServer().getCluster();
         if (var3.isMessageOrderingEnabled()) {
            dispatchWM = WorkManagerFactory.getInstance().findOrCreate("weblogic.unicast.DispatchWorkManager", 1, 1);
            forwardingWM = WorkManagerFactory.getInstance().findOrCreate("weblogic.unicast.ForwardingWorkManager", 1, 1);
         } else {
            dispatchWM = WorkManagerFactory.getInstance().findOrCreate("weblogic.unicast.DispatchWorkManager", 5, -1);
            forwardingWM = WorkManagerFactory.getInstance().findOrCreate("weblogic.unicast.ForwardingWorkManager", 5, -1);
         }

      }
   }

   public static void setLogService(LogService var0) {
      logService = var0;
   }

   public static ConfiguredServersMonitor getConfiguredServersMonitor() {
      return configuration;
   }

   public static void executeForwardMessage(Runnable var0) {
      forwardingWM.schedule(var0);
   }

   public static void executeDispatchMessage(Runnable var0) {
      dispatchWM.schedule(var0);
   }

   public static Object startTimer(final Runnable var0, int var1, int var2) {
      Timer var3 = new Timer();
      var3.schedule(new TimerTask() {
         public void run() {
            var0.run();
         }
      }, (long)var1, (long)var2);
      return var3;
   }

   public static PingRoutine getPingRoutine() {
      return (PingRoutine)(HTTP_PING ? HttpPingRoutineImpl.getInstance() : PingRoutineImpl.getInstance());
   }

   public static void stopTimer(Object var0) {
      ((Timer)var0).cancel();
   }

   public static ConnectionManager getConnectionManager() {
      return connectionManager;
   }

   public static GroupManager getGroupManager() {
      return GroupManagerImpl.getInstance();
   }

   public static LogService getLogService() {
      return logService;
   }

   public static PropertyService getPropertyService() {
      return propertyService;
   }
}
