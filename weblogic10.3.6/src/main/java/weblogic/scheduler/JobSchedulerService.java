package weblogic.scheduler;

import java.security.AccessController;
import weblogic.cluster.ClusterLogger;
import weblogic.management.ManagementException;
import weblogic.management.configuration.JDBCSystemResourceMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.management.runtime.ClusterRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;
import weblogic.utils.Debug;

public final class JobSchedulerService extends AbstractServerService {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final boolean DEBUG = Debug.getCategory("weblogic.JobScheduler").isEnabled();
   private static boolean initialized;

   public void start() throws ServiceFailureException {
      try {
         if (markInitialized()) {
            RuntimeAccess var1 = ManagementService.getRuntimeAccess(kernelId);
            ServerMBean var2 = var1.getServer();
            if (var2.getCluster() != null) {
               JDBCSystemResourceMBean var3 = var2.getCluster().getDataSourceForJobScheduler();
               if (var3 != null) {
                  registerJobSchedulerRuntime();
                  TimerMaster.initialize();
                  TimerExecutor.initialize();
               }
            }
         }
      } catch (ManagementException var4) {
         throw new ServiceFailureException("JobScheduler failed to start!", var4);
      }
   }

   private static synchronized boolean markInitialized() {
      if (initialized) {
         return false;
      } else {
         initialized = true;
         return true;
      }
   }

   private static synchronized void registerJobSchedulerRuntime() throws ManagementException {
      ClusterRuntimeMBean var0 = ManagementService.getRuntimeAccess(kernelId).getServerRuntime().getClusterRuntime();
      new JobSchedulerRuntimeMBeanImpl(var0);
   }

   private static void debug(String var0) {
      ClusterLogger.logDebug("[JobScheduler] " + var0);
   }
}
