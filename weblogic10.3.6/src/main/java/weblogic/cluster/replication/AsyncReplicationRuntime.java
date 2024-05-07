package weblogic.cluster.replication;

import java.security.AccessController;
import weblogic.management.ManagementException;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.AsyncReplicationRuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.rmi.spi.HostID;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class AsyncReplicationRuntime extends RuntimeMBeanDelegate implements AsyncReplicationRuntimeMBean {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public AsyncReplicationRuntime() throws ManagementException {
      super("AsyncReplication");
      ManagementService.getRuntimeAccess(kernelId).getServerRuntime().setAsyncReplicationRuntime(this);
   }

   public String[] getDetailedSecondariesDistribution() {
      return AsyncReplicationManager.theOne().getSecondaryDistributionNames();
   }

   public long getPrimaryCount() {
      return AsyncReplicationManager.theOne().getPrimaryCount();
   }

   public long getSecondaryCount() {
      return AsyncReplicationManager.theOne().getSecondaryCount();
   }

   public String getSecondaryServerDetails() {
      HostID var1 = AsyncReplicationManager.theOne().getSecondarySelector().getSecondarySrvr();
      return var1 != null ? var1.toString() : "";
   }

   public int getSessionsWaitingForFlushCount() {
      return ((AsyncReplicationManager)AsyncReplicationManager.theOne()).getSessionsWaitingForFlushCount();
   }

   public long getLastSessionsFlushTime() {
      return ((AsyncReplicationManager)AsyncReplicationManager.theOne()).getTimeAtLastUpdateFlush();
   }
}
