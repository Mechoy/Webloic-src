package weblogic.cluster.replication;

import java.security.AccessController;
import weblogic.management.ManagementException;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.MANReplicationRuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.protocol.ServerIdentity;
import weblogic.rmi.spi.HostID;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class MANReplicationRuntime extends RuntimeMBeanDelegate implements MANReplicationRuntimeMBean {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   protected ReplicationManager manager;

   public MANReplicationRuntime(String var1, ReplicationManager var2) throws ManagementException {
      super(var1);
      this.manager = var2;
      this.registerRuntime();
   }

   protected void registerRuntime() {
      ManagementService.getRuntimeAccess(kernelId).getServerRuntime().setMANReplicationRuntime(this);
   }

   public String[] getActiveServersInRemoteCluster() {
      return RemoteClusterSecondarySelector.getSecondarySelector().getActiveServersInRemoteCluster();
   }

   public String[] getDetailedSecondariesDistribution() {
      return this.manager.getSecondaryDistributionNames();
   }

   public long getPrimaryCount() {
      return this.manager.getPrimaryCount();
   }

   public long getSecondaryCount() {
      return this.manager.getSecondaryCount();
   }

   public boolean getRemoteClusterReachable() {
      return RemoteClusterSecondarySelector.getSecondarySelector().canReplicateToRemoteCluster();
   }

   public String getSecondaryServerDetails() {
      HostID var1 = RemoteClusterSecondarySelector.getSecondarySelector().getSecondarySrvr();
      return var1 != null ? var1.toString() : "";
   }

   public String getSecondaryServerName() {
      ServerIdentity var1 = (ServerIdentity)RemoteClusterSecondarySelector.getSecondarySelector().getSecondarySrvr();
      return var1 != null ? var1.getServerName() : "";
   }
}
