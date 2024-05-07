package weblogic.cluster;

import java.lang.reflect.Method;
import java.security.AccessController;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import weblogic.cluster.replication.LocalSecondarySelector;
import weblogic.cluster.replication.MANReplicationManager;
import weblogic.cluster.replication.RemoteClusterSecondarySelector;
import weblogic.cluster.replication.ReplicationManager;
import weblogic.cluster.singleton.MigratableServerService;
import weblogic.cluster.singleton.ServerMigrationRuntimeMBeanImpl;
import weblogic.cluster.singleton.SingletonServicesManager;
import weblogic.health.HealthState;
import weblogic.management.ManagementException;
import weblogic.management.configuration.MachineMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.ClusterRuntimeMBean;
import weblogic.management.runtime.JobSchedulerRuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.ServerMigrationRuntimeMBean;
import weblogic.management.runtime.UnicastMessagingRuntimeMBean;
import weblogic.rmi.spi.HostID;
import weblogic.scheduler.JobSchedulerRuntimeMBeanImpl;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.AssertionError;

public final class ClusterRuntime extends RuntimeMBeanDelegate implements ClusterRuntimeMBean {
   private static final long serialVersionUID = 7321104020611342137L;
   private static int invocationCounter = 0;
   private String clusterType;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public static void initialize(String var0) {
      try {
         ClusterRuntime var1 = new ClusterRuntime(var0);
         var1.clusterType = ManagementService.getRuntimeAccess(kernelId).getServer().getCluster().getClusterType();
      } catch (ManagementException var2) {
         ClusterLogger.logErrorCreatingClusterRuntime(var2);
      }

   }

   public ClusterRuntime() throws ManagementException {
      throw new AssertionError("for JMX compliance only");
   }

   private ClusterRuntime(String var1) throws ManagementException {
      super(var1);
      ManagementService.getRuntimeAccess(kernelId).getServerRuntime().setClusterRuntime(this);
   }

   public int getAliveServerCount() {
      return ClusterService.getServices().getRemoteMembers().size() + 1;
   }

   public long getResendRequestsCount() {
      return MulticastManager.theOne().getResendRequestsCount();
   }

   public long getFragmentsSentCount() {
      return MulticastManager.theOne().getFragmentsSentCount();
   }

   public long getFragmentsReceivedCount() {
      return MulticastManager.theOne().getFragmentsReceivedCount();
   }

   public long getMulticastMessagesLostCount() {
      return MulticastManager.theOne().getMulticastMessagesLostCount();
   }

   public String[] getServerNames() {
      Collection var1 = ClusterService.getServices().getRemoteMembers();
      String[] var2 = new String[var1.size() + 1];
      Iterator var3 = var1.iterator();

      int var4;
      ClusterMemberInfo var5;
      for(var4 = 0; var3.hasNext(); var2[var4++] = var5.serverName()) {
         var5 = (ClusterMemberInfo)var3.next();
      }

      var2[var4] = ClusterService.getServices().getLocalMember().serverName();
      return var2;
   }

   public String[] getSecondaryDistributionNames() {
      return this.clusterType.equals("man") ? MANReplicationManager.theOne().getSecondaryDistributionNames() : ReplicationManager.theOne().getSecondaryDistributionNames();
   }

   public String[] getDetailedSecondariesDistribution() {
      return this.getSecondaryDistributionNames();
   }

   public long getPrimaryCount() {
      return this.clusterType.equals("man") ? MANReplicationManager.theOne().getPrimaryCount() : ReplicationManager.theOne().getPrimaryCount();
   }

   public long getSecondaryCount() {
      return ReplicationManager.theOne().getSecondaryCount();
   }

   public long getForeignFragmentsDroppedCount() {
      return MulticastManager.theOne().getForeignFragmentsDroppedCount();
   }

   public String getCurrentSecondaryServer() {
      HostID var1;
      if (this.clusterType.equals("man")) {
         var1 = RemoteClusterSecondarySelector.getSecondarySelector().getSecondarySrvr();
         return var1 != null ? var1.toString() : "";
      } else {
         var1 = LocalSecondarySelector.getSecondarySelector().getSecondarySrvr();
         return var1 != null ? var1.toString() : "";
      }
   }

   public String getSecondaryServerDetails() {
      return this.getCurrentSecondaryServer();
   }

   public HashMap getUnreliableServers() {
      return ClusterDropoutListener.theOne().getDropoutCounts();
   }

   public HealthState getHealthState() {
      return new HealthState(0);
   }

   public MachineMBean getCurrentMachine() {
      return MigratableServerService.theOne().getCurrentMachine();
   }

   public ServerMigrationRuntimeMBean getServerMigrationRuntime() {
      return ServerMigrationRuntimeMBeanImpl.getInstance();
   }

   public JobSchedulerRuntimeMBean getJobSchedulerRuntime() {
      return JobSchedulerRuntimeMBeanImpl.getInstance();
   }

   public UnicastMessagingRuntimeMBean getUnicastMessaging() {
      try {
         Class var1 = Class.forName("weblogic.cluster.messaging.internal.server.UnicastMessagingRuntimeMBeanImpl");
         Method var2 = var1.getMethod("getInstance");
         var2.setAccessible(true);
         return (UnicastMessagingRuntimeMBean)var2.invoke((Object)null);
      } catch (Exception var3) {
         return null;
      }
   }

   public String[] getActiveSingletonServices() {
      return SingletonServicesManager.getInstance().getActiveServiceNames();
   }
}
