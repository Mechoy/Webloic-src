package weblogic.cluster;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.AccessController;
import java.util.Collection;
import java.util.Collections;
import weblogic.cluster.replication.ReplicationManager;
import weblogic.cluster.singleton.AbstractConsensusService;
import weblogic.cluster.singleton.DatabaseLeasingBasis;
import weblogic.cluster.singleton.LeaseManager;
import weblogic.cluster.singleton.LeaseManagerFactory;
import weblogic.cluster.singleton.LeasingBasis;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.VersionInfo;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.diagnostics.image.ImageManager;
import weblogic.diagnostics.image.ImageSource;
import weblogic.diagnostics.image.ImageSourceNotFoundException;
import weblogic.health.HealthMonitorService;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JTAMigratableTargetMBean;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.SingletonServiceMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.management.runtime.ClusterRuntimeMBean;
import weblogic.protocol.LocalServerIdentity;
import weblogic.protocol.configuration.ChannelHelper;
import weblogic.rjvm.JVMID;
import weblogic.rmi.cluster.ServerInfoManager;
import weblogic.security.HMAC;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.internal.SerializedSystemIni;
import weblogic.security.internal.encryption.EncryptionService;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.ActivatedService;
import weblogic.server.ServiceFailureException;
import weblogic.utils.ByteArrayDiffChecker;
import weblogic.utils.StringUtils;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

public final class ClusterService extends ActivatedService implements ClusterServices, BeanUpdateListener {
   private static ClusterService singleton = null;
   static final String MULTICAST_QUEUE = "ClusterMessaging";
   static WorkManager MULTICAST_WORKMANAGER;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final SecurityHolder securityHolder = new SecurityHolder();
   private static final ClusterTextTextFormatter FORMATTER = new ClusterTextTextFormatter();
   private ImageSource clusterDiagnosticImageSource = new ClusterDiagnosticImageSource();
   private boolean isMemberDeathDetectorEnabled = false;
   private LeasingBasis defaultLeasingBasis;
   private ClusterMBean clusterMBean;
   private static boolean isServerInCluster = false;
   private String clusterName;
   private boolean useOneWayRMI;

   public ClusterService() {
      singleton = this;
      RuntimeAccess var1 = ManagementService.getRuntimeAccess(kernelId);
      if (var1 != null) {
         var1.getServer().addBeanUpdateListener(this);
      }

   }

   public static ClusterService getClusterService() {
      return singleton;
   }

   public boolean startService() throws ServiceFailureException {
      boolean var1 = false;
      securityHolder.init();
      ServerMBean var2 = ManagementService.getRuntimeAccess(kernelId).getServer();
      this.clusterMBean = var2.getCluster();
      String var3 = var2.getMachine() == null ? ClusterHelper.getMachineName() : var2.getMachine().getName();
      if (this.clusterMBean != null) {
         this.useOneWayRMI = this.clusterMBean.isOneWayRmiForReplicationEnabled();
         this.isMemberDeathDetectorEnabled = this.clusterMBean.isMemberDeathDetectorEnabled() && this.verifyMemberDeathDetectorConfiguration();
         ServerInfoManager.theOne().addServer(var2.getName(), LocalServerIdentity.getIdentity(), var2.getClusterWeight());
         this.clusterName = this.clusterMBean.getName();
         String var4 = this.clusterMBean.getMulticastAddress();
         if (var4 == null || var4.equals("")) {
            ClusterLogger.logMissingClusterMulticastAddressError(this.clusterName);
            throw new ServiceFailureException("configuration problem - missing multicast address for cluster: " + this.clusterName);
         }

         String var5 = this.clusterMBean.getClusterAddress();
         int var8;
         if (var5 != null) {
            int var6 = 0;
            String[] var7 = StringUtils.splitCompletely(var5, ",", false);
            var8 = (new String(":")).charAt(0);

            try {
               if (var7.length > 1) {
                  while(var6 < var7.length) {
                     String var9 = StringUtils.upto(var7[var6], (char)var8);
                     InetAddress.getByName(var9);
                     ++var6;
                  }
               } else {
                  InetAddress.getByName(var5);
               }

               JVMID.localID().setClusterAddress(var5);
            } catch (UnknownHostException var12) {
               if (var7.length > 1) {
                  ClusterLogger.logCannotResolveClusterAddressWarning(var5 + ": Unknown host: " + var7[var6]);
               } else {
                  ClusterLogger.logCannotResolveClusterAddressWarning(var5);
               }
            }
         }

         try {
            FORMATTER.startingClusterService();
            MemberAttributes var14 = new MemberAttributes(var2.getListenAddress(), var3, VersionInfo.theOne().getReleaseVersion(), System.currentTimeMillis(), var2.getClusterWeight(), var2.getReplicationGroup(), var2.getPreferredSecondaryGroup(), this.clusterName, var2.isAutoMigrationEnabled(), this.clusterMBean.getReplicationChannel(), PeerInfo.getPeerInfo());
            MulticastManager.initialize(this.clusterMBean.getMulticastAddress(), ManagementService.getRuntimeAccess(kernelId).getServer().getInterfaceAddress(), this.clusterMBean.getMulticastPort(), (byte)this.clusterMBean.getMulticastTTL(), (long)this.clusterMBean.getMulticastSendDelay());
            AnnouncementManager.initialize((long)this.clusterMBean.getServiceAgeThresholdSeconds());
            MemberManager.initialize(var14.joinTime(), this.clusterMBean.getIdlePeriodsUntilTimeout());
            UpgradeUtils.getInstance();
            ClusterAddressHelper.getInstance().initialize(this.clusterMBean);
            AttributeManager.initialize(var14);
            ClusterRuntime.initialize(this.clusterName);
            MULTICAST_WORKMANAGER = WorkManagerFactory.getInstance().findOrCreate("ClusterMessaging", -1, 1);
            isServerInCluster = true;
            ClusterRuntimeMBean var15 = ManagementService.getRuntimeAccess(kernelId).getServerRuntime().getClusterRuntime();
            HealthMonitorService.register("Cluster", var15, true);
            ReplicationManager.theOne();
            var8 = this.clusterMBean.getHealthCheckIntervalMillis();
            int var16 = this.clusterMBean.getHealthCheckPeriodsUntilFencing();
            boolean var10 = this.isLeasingBasisNeeded(this.clusterMBean);
            if (var10 && this.clusterMBean.getMigrationBasis().equals("database") && this.clusterMBean.getDataSourceForAutomaticMigration() == null) {
               ClusterExtensionLogger.logDataSourceForDatabaseLeasingNotSet(this.clusterMBean.getName());
               throw new ServiceFailureException("Cluster " + this.clusterMBean.getName() + " uses database as the migration basis but no data source for " + " migration has been configured");
            }

            if (this.clusterMBean.getMigrationBasis().equals("database") && (var10 || this.clusterMBean.getDataSourceForAutomaticMigration() != null)) {
               this.defaultLeasingBasis = DatabaseLeasingBasis.createBasis(var2, this.clusterMBean.getDataSourceForAutomaticMigration(), var16 * var8 / 1000, this.clusterMBean.getAutoMigrationTableName());
            } else if (this.clusterMBean.getMigrationBasis().equals("consensus")) {
               this.defaultLeasingBasis = AbstractConsensusService.getInstance().createConsensusBasis(var8, var16 * var8);
            } else if (this.clusterMBean.getDataSourceForJobScheduler() != null) {
               this.defaultLeasingBasis = DatabaseLeasingBasis.createBasis(var2, this.clusterMBean.getDataSourceForJobScheduler(), var16 * var8 / 1000, this.clusterMBean.getAutoMigrationTableName());
            }

            if (this.defaultLeasingBasis != null) {
               LeaseManagerFactory.singleton().initialize(this.defaultLeasingBasis, var8, var16 * var8, this.clusterMBean.getFencingGracePeriodMillis());
            }

            var1 = true;
         } catch (IOException var11) {
            ClusterLogger.logFailedToJoinClusterError(this.clusterName, this.clusterMBean.getMulticastAddress(), var11);
            throw new ServiceFailureException(var11);
         }
      }

      ImageManager var13 = ImageManager.getInstance();
      var13.registerImageSource("Cluster", this.clusterDiagnosticImageSource);
      return var1;
   }

   private boolean verifyMemberDeathDetectorConfiguration() {
      ClusterMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServer().getCluster();
      ServerMBean[] var2 = var1.getServers();
      boolean var3 = true;

      for(int var4 = 0; var4 < var2.length; ++var4) {
         ServerMBean var5 = var2[var4];
         if (var5.getMachine() == null) {
            var3 = false;
            ClusterExtensionLogger.logServerWithNoMachineConfigured(var5.getName());
         }
      }

      return var3;
   }

   public void stopService() throws ServiceFailureException {
      this.haltService();
   }

   public synchronized void haltService() throws ServiceFailureException {
      if (this.clusterMBean != null && isServerInCluster) {
         ClusterLogger.logLeavingCluster(this.clusterMBean.getName());
         MulticastManager.theOne().forceSuspend();
         MulticastManager.theOne().stopListening();
         MemberManager.theOne().shutdown();
         AnnouncementManager.theOne().shutdown();
         HealthMonitorService.unregister("Cluster");
         ImageManager var1 = ImageManager.getInstance();

         try {
            var1.unregisterImageSource("Cluster");
         } catch (ImageSourceNotFoundException var3) {
         }
      }

   }

   public static ClusterServices getServices() {
      return isServerInCluster ? getClusterService() : null;
   }

   MulticastSession createMulticastSession(int var1, RecoverListener var2, int var3, boolean var4) {
      return MulticastManager.theOne().createSender(var1, var2, var3, var4);
   }

   public MulticastSession createMulticastSession(RecoverListener var1, int var2, boolean var3) {
      return this.createMulticastSession(-1, var1, var2, var3);
   }

   public MulticastSession createMulticastSession(RecoverListener var1, int var2) {
      return this.createMulticastSession(-1, var1, var2, false);
   }

   public MulticastSession createMulticastSession(RecoverListener var1, int var2, boolean var3, boolean var4) {
      return MulticastManager.theOne().createSender(-1, var1, var2, var3, var4);
   }

   public ClusterMemberInfo getLocalMember() {
      return isServerInCluster ? AttributeManager.theOne().getLocalAttributes() : null;
   }

   public Collection getRemoteMembers() {
      return MemberManager.theOne().getRemoteMembers();
   }

   public Collection getAllRemoteMembers() {
      return MemberManager.theOne().getRemoteMembers(true);
   }

   public Collection getClusterMasterMembers() {
      return Collections.EMPTY_SET;
   }

   public void addClusterMembersListener(ClusterMembersChangeListener var1) {
      MemberManager.theOne().addClusterMembersListener(var1);
   }

   public void removeClusterMembersListener(ClusterMembersChangeListener var1) {
      MemberManager.theOne().removeClusterMembersListener(var1);
   }

   public void addHeartbeatMessage(GroupMessage var1) {
      MulticastManager.theOne().addItem(var1);
   }

   public void removeHeartbeatMessage(GroupMessage var1) {
      MulticastManager.theOne().removeItem(var1);
   }

   public int getHeartbeatTimeoutMillis() {
      return this.clusterMBean.getIdlePeriodsUntilTimeout() * 10000;
   }

   public void resendLocalAttributes() {
      try {
         AttributeManager.theOne().sendAttributes();
      } catch (IOException var2) {
         ClusterLogger.logFailureUpdatingServerInTheCluster(ManagementService.getRuntimeAccess(kernelId).getServer().getName(), var2);
      }

   }

   public LeaseManager getDefaultLeaseManager(String var1) {
      return LeaseManagerFactory.singleton().getLeaseManager(var1);
   }

   public LeasingBasis getDefaultLeasingBasis() {
      return this.defaultLeasingBasis;
   }

   private boolean isLeasingBasisNeeded(ClusterMBean var1) {
      return isMigratableCluster(var1) || isAutoServiceMigrationEnabled(var1);
   }

   private static boolean isMigratableCluster(ClusterMBean var0) {
      if (var0 == null) {
         return false;
      } else {
         ServerMBean[] var1 = var0.getServers();

         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (var1[var2].isAutoMigrationEnabled()) {
               return true;
            }
         }

         return false;
      }
   }

   private static boolean isAutoServiceMigrationEnabled(ClusterMBean var0) {
      MigratableTargetMBean[] var1 = var0.getMigratableTargets();

      for(int var2 = 0; var1 != null && var2 < var1.length; ++var2) {
         if (!"manual".equals(var1[var2].getMigrationPolicy())) {
            return true;
         }
      }

      DomainMBean var7 = (DomainMBean)var0.getParent();
      SingletonServiceMBean[] var3 = var7.getSingletonServices();

      for(int var4 = 0; var3 != null && var4 < var3.length; ++var4) {
         if (var3[var4].getCluster() != null && var3[var4].getCluster().getName().equals(var0.getName())) {
            return true;
         }
      }

      ServerMBean[] var8 = var0.getServers();

      for(int var5 = 0; var8 != null && var5 < var8.length; ++var5) {
         JTAMigratableTargetMBean var6 = var8[var5].getJTAMigratableTarget();
         if (var6 != null && var6.getMigrationPolicy().equals("failure-recovery")) {
            return true;
         }
      }

      return false;
   }

   public void prepareUpdate(BeanUpdateEvent var1) throws BeanUpdateRejectedException {
      BeanUpdateEvent.PropertyUpdate[] var2 = var1.getUpdateList();
      boolean var3 = false;
      if (ManagementService.getRuntimeAccess(kernelId).isAdminServer()) {
         var3 = true;
      }

      for(int var4 = 0; var4 < var2.length; ++var4) {
         String var5 = var2[var4].getPropertyName();
         if (var5.equals("Cluster")) {
            throw new BeanUpdateRejectedException("Cannot update '" + var5 + "' while the server is running");
         }

         if (var5.equals("Machine")) {
            if (!var3) {
               throw new BeanUpdateRejectedException("Cannot update '" + var5 + "' while the server is running");
            }

            ClusterExtensionLogger.logUpdatingNonDynamicPropertyOnAdminServer(var5);
         }
      }

   }

   public void activateUpdate(BeanUpdateEvent var1) {
   }

   public void rollbackUpdate(BeanUpdateEvent var1) {
   }

   byte[] getSecureHash() {
      return securityHolder.getSecretHash();
   }

   boolean checkRequest(String var1, byte[] var2) {
      byte[] var3 = securityHolder.getEncryptionService().encryptString(var1);
      byte[] var4 = HMAC.digest(var3, securityHolder.getSecret(), securityHolder.getSalt());
      ByteArrayDiffChecker var5 = new ByteArrayDiffChecker();
      return var5.diffByteArrays(var2, var4) == null;
   }

   boolean multicastDataEncryptionEnabled() {
      return ManagementService.getRuntimeAccess(kernelId).getServer().getCluster().getMulticastDataEncryption();
   }

   public boolean isReplicationTimeoutEnabled() {
      return this.clusterMBean.isReplicationTimeoutEnabled();
   }

   String getLocalServerDetails() {
      String var1 = ManagementService.getRuntimeAccess(kernelId).getServerName();
      String var2 = ManagementService.getRuntimeAccess(kernelId).getDomainName();
      return "Server '" + var1 + "', cluster '" + this.clusterName + "', domain '" + var2 + "',  with attributes " + "multicastDataEncryptionEnabled '" + this.multicastDataEncryptionEnabled() + "' and Admin channel available '" + ChannelHelper.isLocalAdminChannelEnabled() + "'";
   }

   boolean isUnicastMessagingModeEnabled() {
      return this.clusterMBean.getClusterMessagingMode().equals("unicast");
   }

   public boolean useOneWayRMI() {
      return this.useOneWayRMI;
   }

   public boolean isMemberDeathDetectorEnabled() {
      return this.isMemberDeathDetectorEnabled;
   }

   private static final class SecurityHolder {
      private boolean initialized;
      private EncryptionService es;
      private byte[] SALT;
      private byte[] SECRET;
      private byte[] SECRET_HASH;

      private SecurityHolder() {
         this.initialized = false;
         this.es = null;
         this.SALT = null;
         this.SECRET = null;
         this.SECRET_HASH = null;
      }

      private EncryptionService getEncryptionService() {
         return this.es;
      }

      private byte[] getSecretHash() {
         return this.SECRET_HASH;
      }

      private byte[] getSecret() {
         return this.SECRET;
      }

      private byte[] getSalt() {
         return this.SALT;
      }

      private synchronized void init() {
         if (!this.initialized) {
            this.es = SerializedSystemIni.getEncryptionService();
            this.SALT = SerializedSystemIni.getSalt();
            this.SECRET = SerializedSystemIni.getEncryptedSecretKey();
            this.SECRET_HASH = HMAC.digest(this.es.encryptString(ManagementService.getRuntimeAccess(ClusterService.kernelId).getServer().getName()), this.SECRET, this.SALT);
            this.initialized = true;
         }
      }

      // $FF: synthetic method
      SecurityHolder(Object var1) {
         this();
      }
   }
}
