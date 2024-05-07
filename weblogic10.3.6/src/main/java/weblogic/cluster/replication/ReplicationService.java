package weblogic.cluster.replication;

import java.security.AccessController;
import weblogic.cluster.ClusterLogger;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;
import weblogic.servlet.cluster.WANPersistenceManager;

public class ReplicationService extends AbstractServerService {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public void start() throws ServiceFailureException {
      ClusterMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServer().getCluster();
      if (var1 != null) {
         ReplicationManager.start();
         ClusterLogger.logStartingReplicationService("async", var1.getClusterAddress());
         AsyncReplicationManager.start();
         if (var1.getClusterType().equals("man")) {
            ClusterLogger.logStartingReplicationService("man", var1.getRemoteClusterAddress());
            MANReplicationManager.start();
            ClusterLogger.logStartingReplicationService("man-async", var1.getRemoteClusterAddress());
            MANAsyncReplicationManager.start();
         } else if (var1.getClusterType().equals("wan")) {
            ClusterLogger.logStartingReplicationService("wan", var1.getRemoteClusterAddress());
            WANPersistenceManager.getControlInstance().start();
         }
      }

   }

   public void stop() throws ServiceFailureException {
      this.halt();
   }

   public void halt() throws ServiceFailureException {
      ClusterMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServer().getCluster();
      if (var1 != null) {
         ReplicationManager.stop();
         ClusterLogger.logStoppingReplicationService("async");
         AsyncReplicationManager.stop();
         if (var1.getClusterType().equals("man")) {
            ClusterLogger.logStoppingReplicationService("man");
            MANReplicationManager.stop();
            ClusterLogger.logStoppingReplicationService("man-async");
            MANReplicationManager.stop();
         } else if (var1.getClusterType().equals("wan")) {
            ClusterLogger.logStoppingReplicationService("wan");
            WANPersistenceManager.getControlInstance().stop();
         }
      }

   }
}
