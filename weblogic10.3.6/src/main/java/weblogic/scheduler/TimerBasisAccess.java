package weblogic.scheduler;

import java.security.AccessController;
import java.sql.SQLException;
import weblogic.management.configuration.JDBCSystemResourceMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public final class TimerBasisAccess {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static TimerBasis basis;

   public static synchronized TimerBasis getTimerBasis() throws TimerException {
      if (basis != null) {
         return basis;
      } else {
         RuntimeAccess var0 = ManagementService.getRuntimeAccess(kernelId);
         ServerMBean var1 = var0.getServer();

         assert var1.getCluster() != null;

         JDBCSystemResourceMBean var2 = var1.getCluster().getDataSourceForJobScheduler();

         assert var2 != null;

         try {
            basis = new DBTimerBasisImpl(var2, var1.getCluster().getJobSchedulerTableName(), var0.getDomainName(), var1.getCluster().getName(), var0.getServerName());
            return basis;
         } catch (SQLException var4) {
            throw new TimerException("Unable to access data source '" + var2.getName() + "'", var4);
         }
      }
   }
}
