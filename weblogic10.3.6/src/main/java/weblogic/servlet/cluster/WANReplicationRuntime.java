package weblogic.servlet.cluster;

import java.security.AccessController;
import weblogic.cluster.replication.LocalSecondarySelector;
import weblogic.cluster.replication.ReplicationManager;
import weblogic.cluster.replication.SecondarySelector;
import weblogic.management.ManagementException;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.WANReplicationRuntimeMBean;
import weblogic.protocol.ServerIdentity;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class WANReplicationRuntime extends RuntimeMBeanDelegate implements WANReplicationRuntimeMBean {
   private long numberOfSessionsFlushed = 0L;
   private long numberOfSessionRetrieved = 0L;
   private boolean remoteClusterReachable;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   WANReplicationRuntime(String var1) throws ManagementException {
      super(var1);
      ManagementService.getRuntimeAccess(kernelId).getServerRuntime().setWANReplicationRuntime(this);
   }

   public long getNumberOfSessionsFlushedToTheDatabase() {
      return this.numberOfSessionsFlushed;
   }

   public long getNumberOfSessionsRetrievedFromTheDatabase() {
      return this.numberOfSessionRetrieved;
   }

   public void cleanupExpiredSessionsInTheDatabase() {
   }

   public String getSecondaryServerName() {
      SecondarySelector var1 = LocalSecondarySelector.getSecondarySelector();
      if (var1 != null) {
         ServerIdentity var2 = (ServerIdentity)var1.getSecondarySrvr();
         if (var2 != null) {
            return var2.getServerName();
         }
      }

      return "";
   }

   public String getSecondaryServerDetails() {
      return this.getSecondaryServerName();
   }

   public long getPrimaryCount() {
      return ReplicationManager.theOne().getPrimaryCount();
   }

   public long getSecondaryCount() {
      return ReplicationManager.theOne().getSecondaryCount();
   }

   public String[] getDetailedSecondariesDistribution() {
      return ReplicationManager.theOne().getSecondaryDistributionNames();
   }

   public synchronized void incrementNumberOfSessionsFlushedToTheDatabase() {
      ++this.numberOfSessionsFlushed;
   }

   public synchronized void incrementNumberOfSessionsRetrievedFromTheDatabase() {
      ++this.numberOfSessionRetrieved;
   }

   public boolean getRemoteClusterReachable() {
      return this.remoteClusterReachable;
   }

   public void setRemoteClusterReachable(boolean var1) {
      this.remoteClusterReachable = var1;
   }
}
