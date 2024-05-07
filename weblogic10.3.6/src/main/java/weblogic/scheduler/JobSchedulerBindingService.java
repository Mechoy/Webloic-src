package weblogic.scheduler;

import java.security.AccessController;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.cluster.ClusterLogger;
import weblogic.jndi.Environment;
import weblogic.management.configuration.JDBCSystemResourceMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;
import weblogic.timers.TimerManagerFactory;
import weblogic.utils.Debug;

public final class JobSchedulerBindingService extends AbstractServerService {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final String JNDI_NAME = "weblogic.JobScheduler";
   private static final boolean DEBUG = Debug.getCategory("weblogic.JobScheduler").isEnabled();

   public void start() throws ServiceFailureException {
      RuntimeAccess var1 = ManagementService.getRuntimeAccess(kernelId);
      ServerMBean var2 = var1.getServer();
      if (var2.getCluster() != null) {
         try {
            JDBCSystemResourceMBean var3 = var2.getCluster().getDataSourceForJobScheduler();
            if (DEBUG) {
               debug("datasource for job scheduler " + var3);
            }

            if (var3 != null) {
               this.bindJNDI();
            }
         } catch (NamingException var4) {
            throw new ServiceFailureException(var4);
         }
      }
   }

   private void bindJNDI() throws NamingException {
      Environment var1 = new Environment();
      var1.setReplicateBindings(false);
      var1.setCreateIntermediateContexts(true);
      Context var2 = var1.getInitialContext();
      var2.bind("weblogic.JobScheduler", TimerManagerFactory.getTimerManagerFactory().getCommonjTimerManager(TimerServiceImpl.create("weblogic.JobScheduler")));
      if (DEBUG) {
         debug("bound JobScheduler into JNDI");
      }

   }

   private static void debug(String var0) {
      ClusterLogger.logDebug("[JobScheduler] " + var0);
   }
}
