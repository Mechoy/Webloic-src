package weblogic.management.configuration;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.zip.CRC32;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.RuntimeOperationsException;
import weblogic.cluster.ClusterValidator;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.ReferenceManager;
import weblogic.descriptor.internal.ResolvedReference;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.DistributedManagementException;
import weblogic.management.ManagementException;
import weblogic.management.mbeans.custom.Cluster;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.ArrayIterator;
import weblogic.utils.collections.CombinedIterator;

public class ClusterMBeanImpl extends TargetMBeanImpl implements ClusterMBean, Serializable {
   private int _AdditionalAutoMigrationAttempts;
   private int _AsyncSessionQueueTimeout;
   private String _AutoMigrationTableName;
   private MachineMBean[] _CandidateMachinesForMigratableServers;
   private boolean _ClientCertProxyEnabled;
   private String _ClusterAddress;
   private String _ClusterBroadcastChannel;
   private String _ClusterMessagingMode;
   private String _ClusterType;
   private int _ConsensusParticipants;
   private JDBCSystemResourceMBean _DataSourceForAutomaticMigration;
   private JDBCSystemResourceMBean _DataSourceForJobScheduler;
   private JDBCSystemResourceMBean _DataSourceForSessionPersistence;
   private DatabaseLessLeasingBasisMBean _DatabaseLessLeasingBasis;
   private int _DeathDetectorHeartbeatPeriod;
   private String _DefaultLoadAlgorithm;
   private int _FencingGracePeriodMillis;
   private int _FrontendHTTPPort;
   private int _FrontendHTTPSPort;
   private String _FrontendHost;
   private int _GreedySessionFlushInterval;
   private int _HTTPPingRetryCount;
   private int _HealthCheckIntervalMillis;
   private int _HealthCheckPeriodsUntilFencing;
   private boolean _HttpTraceSupportEnabled;
   private int _IdlePeriodsUntilTimeout;
   private int _InterClusterCommLinkHealthCheckInterval;
   private String _JobSchedulerTableName;
   private int _MaxServerCountForHttpPing;
   private boolean _MemberDeathDetectorEnabled;
   private int _MemberWarmupTimeoutSeconds;
   private boolean _MessageOrderingEnabled;
   private MigratableTargetMBean[] _MigratableTargets;
   private String _MigrationBasis;
   private long _MillisToSleepBetweenAutoMigrationAttempts;
   private String _MulticastAddress;
   private int _MulticastBufferSize;
   private boolean _MulticastDataEncryption;
   private int _MulticastPort;
   private int _MulticastSendDelay;
   private int _MulticastTTL;
   private String _Name;
   private int _NumberOfServersInClusterAddress;
   private boolean _OneWayRmiForReplicationEnabled;
   private OverloadProtectionMBean _OverloadProtection;
   private boolean _PersistSessionsOnShutdown;
   private String _RemoteClusterAddress;
   private String _ReplicationChannel;
   private boolean _ReplicationTimeoutEnabled;
   private boolean _SecureReplicationEnabled;
   private Set _ServerNames;
   private ServerMBean[] _Servers;
   private int _ServiceAgeThresholdSeconds;
   private int _SessionFlushInterval;
   private int _SessionFlushThreshold;
   private boolean _SessionLazyDeserializationEnabled;
   private String _SingletonSQLQueryHelper;
   private int _UnicastDiscoveryPeriodMillis;
   private String _WANSessionPersistenceTableName;
   private boolean _WeblogicPluginEnabled;
   private Cluster _customizer;
   private static SchemaHelper2 _schemaHelper;

   public ClusterMBeanImpl() {
      try {
         this._customizer = new Cluster(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public ClusterMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new Cluster(this);
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         throw new UndeclaredThrowableException(var4);
      }

      this._initializeProperty(-1);
   }

   public void addServer(ServerMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 8)) {
         ServerMBean[] var2 = (ServerMBean[])((ServerMBean[])this._getHelper()._extendArray(this.getServers(), ServerMBean.class, var1));

         try {
            this.setServers(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public String getName() {
      if (!this._isSet(2)) {
         try {
            return ((ConfigurationMBean)this.getParent()).getName();
         } catch (NullPointerException var2) {
         }
      }

      return this._customizer.getName();
   }

   public Set getServerNames() {
      return this._customizer.getServerNames();
   }

   public ServerMBean[] getServers() {
      return this._customizer.getServers();
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public boolean isServerNamesSet() {
      return this._isSet(7);
   }

   public boolean isServersSet() {
      return this._isSet(8);
   }

   public void removeServer(ServerMBean var1) {
      ServerMBean[] var2 = this.getServers();
      ServerMBean[] var3 = (ServerMBean[])((ServerMBean[])this._getHelper()._removeElement(var2, ServerMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setServers(var3);
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            }

            throw new UndeclaredThrowableException(var5);
         }
      }

   }

   public void setServerNames(Set var1) throws InvalidAttributeValueException {
      this._ServerNames = var1;
   }

   public void setServers(ServerMBean[] var1) throws InvalidAttributeValueException {
      Object var2 = var1 == null ? new ServerMBeanImpl[0] : var1;
      this._Servers = (ServerMBean[])var2;
   }

   public String getClusterAddress() {
      return this._ClusterAddress;
   }

   public boolean isClusterAddressSet() {
      return this._isSet(9);
   }

   public void setName(String var1) throws InvalidAttributeValueException, ManagementException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonEmptyString("Name", var1);
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("Name", var1);
      ConfigurationValidator.validateName(var1);
      String var2 = this.getName();
      this._customizer.setName(var1);
      this._postSet(2, var2, var1);
   }

   public void setClusterAddress(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ClusterAddress;
      this._ClusterAddress = var1;
      this._postSet(9, var2, var1);
   }

   public String getMulticastAddress() {
      return this._customizer.getMulticastAddress();
   }

   public boolean isMulticastAddressSet() {
      return this._isSet(10);
   }

   public void setMulticastAddress(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      ClusterValidator.validateMulticastAddress(var1);
      String var2 = this.getMulticastAddress();
      this._customizer.setMulticastAddress(var1);
      this._postSet(10, var2, var1);
   }

   public void setMulticastBufferSize(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkMin("MulticastBufferSize", var1, 64);
      int var2 = this._MulticastBufferSize;
      this._MulticastBufferSize = var1;
      this._postSet(11, var2, var1);
   }

   public int getMulticastBufferSize() {
      return this._MulticastBufferSize;
   }

   public boolean isMulticastBufferSizeSet() {
      return this._isSet(11);
   }

   public int getMulticastPort() {
      return this._MulticastPort;
   }

   public boolean isMulticastPortSet() {
      return this._isSet(12);
   }

   public void setMulticastPort(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MulticastPort", (long)var1, 1L, 65535L);
      int var2 = this._MulticastPort;
      this._MulticastPort = var1;
      this._postSet(12, var2, var1);
   }

   public int getMulticastTTL() {
      return this._MulticastTTL;
   }

   public boolean isMulticastTTLSet() {
      return this._isSet(13);
   }

   public void setMulticastTTL(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MulticastTTL", (long)var1, 1L, 255L);
      int var2 = this._MulticastTTL;
      this._MulticastTTL = var1;
      this._postSet(13, var2, var1);
   }

   public void touch() throws ConfigurationException {
      this._customizer.touch();
   }

   public void freezeCurrentValue(String var1) throws AttributeNotFoundException, MBeanException {
      this._customizer.freezeCurrentValue(var1);
   }

   public int getMulticastSendDelay() {
      return this._MulticastSendDelay;
   }

   public boolean isMulticastSendDelaySet() {
      return this._isSet(14);
   }

   public void restoreDefaultValue(String var1) throws AttributeNotFoundException {
      this._customizer.restoreDefaultValue(var1);
   }

   public void setMulticastSendDelay(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MulticastSendDelay", (long)var1, 0L, 250L);
      int var2 = this._MulticastSendDelay;
      this._MulticastSendDelay = var1;
      this._postSet(14, var2, var1);
   }

   public String getDefaultLoadAlgorithm() {
      return this._DefaultLoadAlgorithm;
   }

   public boolean isDefaultLoadAlgorithmSet() {
      return this._isSet(15);
   }

   public void setDefaultLoadAlgorithm(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"round-robin", "weight-based", "random", "round-robin-affinity", "weight-based-affinity", "random-affinity"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("DefaultLoadAlgorithm", var1, var2);
      String var3 = this._DefaultLoadAlgorithm;
      this._DefaultLoadAlgorithm = var1;
      this._postSet(15, var3, var1);
   }

   public String getClusterMessagingMode() {
      return this._ClusterMessagingMode;
   }

   public boolean isClusterMessagingModeSet() {
      return this._isSet(16);
   }

   public void setClusterMessagingMode(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"multicast", "unicast"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("ClusterMessagingMode", var1, var2);
      String var3 = this._ClusterMessagingMode;
      this._ClusterMessagingMode = var1;
      this._postSet(16, var3, var1);
   }

   public String getClusterBroadcastChannel() {
      return this._ClusterBroadcastChannel;
   }

   public boolean isClusterBroadcastChannelSet() {
      return this._isSet(17);
   }

   public void setClusterBroadcastChannel(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ClusterBroadcastChannel;
      this._ClusterBroadcastChannel = var1;
      this._postSet(17, var2, var1);
   }

   public int getServiceAgeThresholdSeconds() {
      return this._ServiceAgeThresholdSeconds;
   }

   public boolean isServiceAgeThresholdSecondsSet() {
      return this._isSet(18);
   }

   public void setServiceAgeThresholdSeconds(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("ServiceAgeThresholdSeconds", (long)var1, 0L, 65534L);
      int var2 = this._ServiceAgeThresholdSeconds;
      this._ServiceAgeThresholdSeconds = var1;
      this._postSet(18, var2, var1);
   }

   public HashMap start() {
      try {
         return this._customizer.start();
      } catch (RuntimeOperationsException var2) {
         throw new UndeclaredThrowableException(var2);
      }
   }

   public HashMap kill() {
      try {
         return this._customizer.kill();
      } catch (RuntimeOperationsException var2) {
         throw new UndeclaredThrowableException(var2);
      }
   }

   public void setClientCertProxyEnabled(boolean var1) {
      boolean var2 = this._ClientCertProxyEnabled;
      this._ClientCertProxyEnabled = var1;
      this._postSet(19, var2, var1);
   }

   public boolean isClientCertProxyEnabled() {
      return this._ClientCertProxyEnabled;
   }

   public boolean isClientCertProxyEnabledSet() {
      return this._isSet(19);
   }

   public void setWeblogicPluginEnabled(boolean var1) {
      boolean var2 = this._WeblogicPluginEnabled;
      this._WeblogicPluginEnabled = var1;
      this._postSet(20, var2, var1);
   }

   public boolean isWeblogicPluginEnabled() {
      return this._WeblogicPluginEnabled;
   }

   public boolean isWeblogicPluginEnabledSet() {
      return this._isSet(20);
   }

   public void addMigratableTarget(MigratableTargetMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 21)) {
         MigratableTargetMBean[] var2 = (MigratableTargetMBean[])((MigratableTargetMBean[])this._getHelper()._extendArray(this.getMigratableTargets(), MigratableTargetMBean.class, var1));

         try {
            this.setMigratableTargets(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public MigratableTargetMBean[] getMigratableTargets() {
      return this._customizer.getMigratableTargets();
   }

   public boolean isMigratableTargetsSet() {
      return this._isSet(21);
   }

   public void removeMigratableTarget(MigratableTargetMBean var1) {
      MigratableTargetMBean[] var2 = this.getMigratableTargets();
      MigratableTargetMBean[] var3 = (MigratableTargetMBean[])((MigratableTargetMBean[])this._getHelper()._removeElement(var2, MigratableTargetMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setMigratableTargets(var3);
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            }

            throw new UndeclaredThrowableException(var5);
         }
      }

   }

   public void setMigratableTargets(MigratableTargetMBean[] var1) throws InvalidAttributeValueException {
      Object var2 = var1 == null ? new MigratableTargetMBeanImpl[0] : var1;
      this._MigratableTargets = (MigratableTargetMBean[])var2;
   }

   public int getMemberWarmupTimeoutSeconds() {
      return this._MemberWarmupTimeoutSeconds;
   }

   public boolean isMemberWarmupTimeoutSecondsSet() {
      return this._isSet(22);
   }

   public void setMemberWarmupTimeoutSeconds(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkMin("MemberWarmupTimeoutSeconds", var1, 0);
      int var2 = this._MemberWarmupTimeoutSeconds;
      this._MemberWarmupTimeoutSeconds = var1;
      this._postSet(22, var2, var1);
   }

   public void setHttpTraceSupportEnabled(boolean var1) {
      boolean var2 = this._HttpTraceSupportEnabled;
      this._HttpTraceSupportEnabled = var1;
      this._postSet(23, var2, var1);
   }

   public boolean isHttpTraceSupportEnabled() {
      return this._HttpTraceSupportEnabled;
   }

   public boolean isHttpTraceSupportEnabledSet() {
      return this._isSet(23);
   }

   public String getFrontendHost() {
      return this._FrontendHost;
   }

   public boolean isFrontendHostSet() {
      return this._isSet(24);
   }

   public void setFrontendHost(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._FrontendHost;
      this._FrontendHost = var1;
      this._postSet(24, var2, var1);
   }

   public int getFrontendHTTPPort() {
      return this._FrontendHTTPPort;
   }

   public boolean isFrontendHTTPPortSet() {
      return this._isSet(25);
   }

   public void setFrontendHTTPPort(int var1) throws InvalidAttributeValueException {
      int var2 = this._FrontendHTTPPort;
      this._FrontendHTTPPort = var1;
      this._postSet(25, var2, var1);
   }

   public int getFrontendHTTPSPort() {
      return this._FrontendHTTPSPort;
   }

   public boolean isFrontendHTTPSPortSet() {
      return this._isSet(26);
   }

   public void setFrontendHTTPSPort(int var1) throws InvalidAttributeValueException {
      int var2 = this._FrontendHTTPSPort;
      this._FrontendHTTPSPort = var1;
      this._postSet(26, var2, var1);
   }

   public int getIdlePeriodsUntilTimeout() {
      return this._IdlePeriodsUntilTimeout;
   }

   public boolean isIdlePeriodsUntilTimeoutSet() {
      return this._isSet(27);
   }

   public void setIdlePeriodsUntilTimeout(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkMin("IdlePeriodsUntilTimeout", var1, 3);
      int var2 = this._IdlePeriodsUntilTimeout;
      this._IdlePeriodsUntilTimeout = var1;
      this._postSet(27, var2, var1);
   }

   public void setRemoteClusterAddress(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._RemoteClusterAddress;
      this._RemoteClusterAddress = var1;
      this._postSet(28, var2, var1);
   }

   public String getRemoteClusterAddress() {
      return this._RemoteClusterAddress;
   }

   public boolean isRemoteClusterAddressSet() {
      return this._isSet(28);
   }

   public void setWANSessionPersistenceTableName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._WANSessionPersistenceTableName;
      this._WANSessionPersistenceTableName = var1;
      this._postSet(29, var2, var1);
   }

   public String getWANSessionPersistenceTableName() {
      return this._WANSessionPersistenceTableName;
   }

   public boolean isWANSessionPersistenceTableNameSet() {
      return this._isSet(29);
   }

   public String getReplicationChannel() {
      return this._ReplicationChannel;
   }

   public boolean isReplicationChannelSet() {
      return this._isSet(30);
   }

   public void setReplicationChannel(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ReplicationChannel;
      this._ReplicationChannel = var1;
      this._postSet(30, var2, var1);
   }

   public int getInterClusterCommLinkHealthCheckInterval() {
      return this._InterClusterCommLinkHealthCheckInterval;
   }

   public boolean isInterClusterCommLinkHealthCheckIntervalSet() {
      return this._isSet(31);
   }

   public void setInterClusterCommLinkHealthCheckInterval(int var1) {
      int var2 = this._InterClusterCommLinkHealthCheckInterval;
      this._InterClusterCommLinkHealthCheckInterval = var1;
      this._postSet(31, var2, var1);
   }

   public void setDataSourceForSessionPersistence(JDBCSystemResourceMBean var1) {
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 32, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return ClusterMBeanImpl.this.getDataSourceForSessionPersistence();
            }
         };
         this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1, var2);
      }

      JDBCSystemResourceMBean var3 = this._DataSourceForSessionPersistence;
      this._DataSourceForSessionPersistence = var1;
      this._postSet(32, var3, var1);
   }

   public JDBCSystemResourceMBean getDataSourceForSessionPersistence() {
      return this._DataSourceForSessionPersistence;
   }

   public String getDataSourceForSessionPersistenceAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getDataSourceForSessionPersistence();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isDataSourceForSessionPersistenceSet() {
      return this._isSet(32);
   }

   public void setDataSourceForSessionPersistenceAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, JDBCSystemResourceMBean.class, new ReferenceManager.Resolver(this, 32) {
            public void resolveReference(Object var1) {
               try {
                  ClusterMBeanImpl.this.setDataSourceForSessionPersistence((JDBCSystemResourceMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         JDBCSystemResourceMBean var2 = this._DataSourceForSessionPersistence;
         this._initializeProperty(32);
         this._postSet(32, var2, this._DataSourceForSessionPersistence);
      }

   }

   public void setDataSourceForJobScheduler(JDBCSystemResourceMBean var1) {
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 33, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return ClusterMBeanImpl.this.getDataSourceForJobScheduler();
            }
         };
         this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1, var2);
      }

      JDBCSystemResourceMBean var3 = this._DataSourceForJobScheduler;
      this._DataSourceForJobScheduler = var1;
      this._postSet(33, var3, var1);
   }

   public JDBCSystemResourceMBean getDataSourceForJobScheduler() {
      return this._DataSourceForJobScheduler;
   }

   public String getDataSourceForJobSchedulerAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getDataSourceForJobScheduler();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isDataSourceForJobSchedulerSet() {
      return this._isSet(33);
   }

   public void setDataSourceForJobSchedulerAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, JDBCSystemResourceMBean.class, new ReferenceManager.Resolver(this, 33) {
            public void resolveReference(Object var1) {
               try {
                  ClusterMBeanImpl.this.setDataSourceForJobScheduler((JDBCSystemResourceMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         JDBCSystemResourceMBean var2 = this._DataSourceForJobScheduler;
         this._initializeProperty(33);
         this._postSet(33, var2, this._DataSourceForJobScheduler);
      }

   }

   public String getJobSchedulerTableName() {
      return this._JobSchedulerTableName;
   }

   public boolean isJobSchedulerTableNameSet() {
      return this._isSet(34);
   }

   public void setJobSchedulerTableName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._JobSchedulerTableName;
      this._JobSchedulerTableName = var1;
      this._postSet(34, var2, var1);
   }

   public boolean getPersistSessionsOnShutdown() {
      return this._PersistSessionsOnShutdown;
   }

   public boolean isPersistSessionsOnShutdownSet() {
      return this._isSet(35);
   }

   public void setPersistSessionsOnShutdown(boolean var1) {
      boolean var2 = this._PersistSessionsOnShutdown;
      this._PersistSessionsOnShutdown = var1;
      this._postSet(35, var2, var1);
   }

   public void setAsyncSessionQueueTimeout(int var1) {
      int var2 = this._AsyncSessionQueueTimeout;
      this._AsyncSessionQueueTimeout = var1;
      this._postSet(36, var2, var1);
   }

   public int getAsyncSessionQueueTimeout() {
      return this._AsyncSessionQueueTimeout;
   }

   public boolean isAsyncSessionQueueTimeoutSet() {
      return this._isSet(36);
   }

   public void setGreedySessionFlushInterval(int var1) {
      int var2 = this._GreedySessionFlushInterval;
      this._GreedySessionFlushInterval = var1;
      this._postSet(37, var2, var1);
   }

   public int getGreedySessionFlushInterval() {
      return this._GreedySessionFlushInterval;
   }

   public boolean isGreedySessionFlushIntervalSet() {
      return this._isSet(37);
   }

   public void setSessionFlushInterval(int var1) {
      int var2 = this._SessionFlushInterval;
      this._SessionFlushInterval = var1;
      this._postSet(38, var2, var1);
   }

   public int getSessionFlushInterval() {
      return this._SessionFlushInterval;
   }

   public boolean isSessionFlushIntervalSet() {
      return this._isSet(38);
   }

   public void setSessionFlushThreshold(int var1) {
      int var2 = this._SessionFlushThreshold;
      this._SessionFlushThreshold = var1;
      this._postSet(39, var2, var1);
   }

   public int getSessionFlushThreshold() {
      return this._SessionFlushThreshold;
   }

   public boolean isSessionFlushThresholdSet() {
      return this._isSet(39);
   }

   public void addCandidateMachinesForMigratableServer(MachineMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 40)) {
         MachineMBean[] var2;
         if (this._isSet(40)) {
            var2 = (MachineMBean[])((MachineMBean[])this._getHelper()._extendArray(this.getCandidateMachinesForMigratableServers(), MachineMBean.class, var1));
         } else {
            var2 = new MachineMBean[]{var1};
         }

         try {
            this.setCandidateMachinesForMigratableServers(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public MachineMBean[] getCandidateMachinesForMigratableServers() {
      return this._CandidateMachinesForMigratableServers;
   }

   public String getCandidateMachinesForMigratableServersAsString() {
      return this._getHelper()._serializeKeyList(this.getCandidateMachinesForMigratableServers());
   }

   public boolean isCandidateMachinesForMigratableServersSet() {
      return this._isSet(40);
   }

   public void removeCandidateMachinesForMigratableServer(MachineMBean var1) {
      MachineMBean[] var2 = this.getCandidateMachinesForMigratableServers();
      MachineMBean[] var3 = (MachineMBean[])((MachineMBean[])this._getHelper()._removeElement(var2, MachineMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setCandidateMachinesForMigratableServers(var3);
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            }

            throw new UndeclaredThrowableException(var5);
         }
      }

   }

   public void setCandidateMachinesForMigratableServersAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         String[] var13 = this._getHelper()._splitKeyList(var1);
         List var3 = this._getHelper()._getKeyList(this._CandidateMachinesForMigratableServers);

         String var5;
         for(int var4 = 0; var4 < var13.length; ++var4) {
            var5 = var13[var4];
            var5 = var5 == null ? null : var5.trim();
            if (var3.contains(var5)) {
               var3.remove(var5);
            } else {
               this._getReferenceManager().registerUnresolvedReference(var5, MachineMBean.class, new ReferenceManager.Resolver(this, 40) {
                  public void resolveReference(Object var1) {
                     try {
                        ClusterMBeanImpl.this.addCandidateMachinesForMigratableServer((MachineMBean)var1);
                     } catch (RuntimeException var3) {
                        throw var3;
                     } catch (Exception var4) {
                        throw new AssertionError("Impossible exception: " + var4);
                     }
                  }
               });
            }
         }

         Iterator var14 = var3.iterator();

         while(true) {
            while(var14.hasNext()) {
               var5 = (String)var14.next();
               MachineMBean[] var6 = this._CandidateMachinesForMigratableServers;
               int var7 = var6.length;

               for(int var8 = 0; var8 < var7; ++var8) {
                  MachineMBean var9 = var6[var8];
                  if (var5.equals(var9.getName())) {
                     try {
                        this.removeCandidateMachinesForMigratableServer(var9);
                        break;
                     } catch (RuntimeException var11) {
                        throw var11;
                     } catch (Exception var12) {
                        throw new AssertionError("Impossible exception: " + var12);
                     }
                  }
               }
            }

            return;
         }
      } else {
         MachineMBean[] var2 = this._CandidateMachinesForMigratableServers;
         this._initializeProperty(40);
         this._postSet(40, var2, this._CandidateMachinesForMigratableServers);
      }
   }

   public void setCandidateMachinesForMigratableServers(MachineMBean[] var1) {
      Object var4 = var1 == null ? new MachineMBeanImpl[0] : var1;
      var1 = (MachineMBean[])((MachineMBean[])this._getHelper()._cleanAndValidateArray(var4, MachineMBean.class));

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2] != null) {
            ResolvedReference var3 = new ResolvedReference(this, 40, (AbstractDescriptorBean)var1[var2]) {
               protected Object getPropertyValue() {
                  return ClusterMBeanImpl.this.getCandidateMachinesForMigratableServers();
               }
            };
            this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1[var2], var3);
         }
      }

      MachineMBean[] var5 = this._CandidateMachinesForMigratableServers;
      this._CandidateMachinesForMigratableServers = var1;
      this._postSet(40, var5, var1);
   }

   public JDBCSystemResourceMBean getDataSourceForAutomaticMigration() {
      return this._DataSourceForAutomaticMigration;
   }

   public String getDataSourceForAutomaticMigrationAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getDataSourceForAutomaticMigration();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isDataSourceForAutomaticMigrationSet() {
      return this._isSet(41);
   }

   public void setDataSourceForAutomaticMigrationAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, JDBCSystemResourceMBean.class, new ReferenceManager.Resolver(this, 41) {
            public void resolveReference(Object var1) {
               try {
                  ClusterMBeanImpl.this.setDataSourceForAutomaticMigration((JDBCSystemResourceMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         JDBCSystemResourceMBean var2 = this._DataSourceForAutomaticMigration;
         this._initializeProperty(41);
         this._postSet(41, var2, this._DataSourceForAutomaticMigration);
      }

   }

   public void setDataSourceForAutomaticMigration(JDBCSystemResourceMBean var1) {
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 41, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return ClusterMBeanImpl.this.getDataSourceForAutomaticMigration();
            }
         };
         this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1, var2);
      }

      JDBCSystemResourceMBean var3 = this._DataSourceForAutomaticMigration;
      this._DataSourceForAutomaticMigration = var1;
      this._postSet(41, var3, var1);
   }

   public int getHealthCheckIntervalMillis() {
      return this._HealthCheckIntervalMillis;
   }

   public boolean isHealthCheckIntervalMillisSet() {
      return this._isSet(42);
   }

   public void setHealthCheckIntervalMillis(int var1) {
      int var2 = this._HealthCheckIntervalMillis;
      this._HealthCheckIntervalMillis = var1;
      this._postSet(42, var2, var1);
   }

   public int getHealthCheckPeriodsUntilFencing() {
      return this._HealthCheckPeriodsUntilFencing;
   }

   public boolean isHealthCheckPeriodsUntilFencingSet() {
      return this._isSet(43);
   }

   public void setHealthCheckPeriodsUntilFencing(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkMin("HealthCheckPeriodsUntilFencing", var1, 2);
      int var2 = this._HealthCheckPeriodsUntilFencing;
      this._HealthCheckPeriodsUntilFencing = var1;
      this._postSet(43, var2, var1);
   }

   public int getFencingGracePeriodMillis() {
      return this._FencingGracePeriodMillis;
   }

   public boolean isFencingGracePeriodMillisSet() {
      return this._isSet(44);
   }

   public void setFencingGracePeriodMillis(int var1) {
      int var2 = this._FencingGracePeriodMillis;
      this._FencingGracePeriodMillis = var1;
      this._postSet(44, var2, var1);
   }

   public String getSingletonSQLQueryHelper() {
      return this._SingletonSQLQueryHelper;
   }

   public boolean isSingletonSQLQueryHelperSet() {
      return this._isSet(45);
   }

   public void setSingletonSQLQueryHelper(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._SingletonSQLQueryHelper;
      this._SingletonSQLQueryHelper = var1;
      this._postSet(45, var2, var1);
   }

   public int getNumberOfServersInClusterAddress() {
      return this._NumberOfServersInClusterAddress;
   }

   public boolean isNumberOfServersInClusterAddressSet() {
      return this._isSet(46);
   }

   public void setNumberOfServersInClusterAddress(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkMin("NumberOfServersInClusterAddress", var1, 1);
      int var2 = this._NumberOfServersInClusterAddress;
      this._NumberOfServersInClusterAddress = var1;
      this._postSet(46, var2, var1);
   }

   public void setClusterType(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"none", "wan", "man"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("ClusterType", var1, var2);
      String var3 = this._ClusterType;
      this._ClusterType = var1;
      this._postSet(47, var3, var1);
   }

   public String getClusterType() {
      return this._ClusterType;
   }

   public boolean isClusterTypeSet() {
      return this._isSet(47);
   }

   public void setMulticastDataEncryption(boolean var1) {
      boolean var2 = this._MulticastDataEncryption;
      this._MulticastDataEncryption = var1;
      this._postSet(48, var2, var1);
   }

   public boolean getMulticastDataEncryption() {
      return this._MulticastDataEncryption;
   }

   public boolean isMulticastDataEncryptionSet() {
      return this._isSet(48);
   }

   public void setAutoMigrationTableName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._AutoMigrationTableName;
      this._AutoMigrationTableName = var1;
      this._postSet(49, var2, var1);
   }

   public String getAutoMigrationTableName() {
      return this._AutoMigrationTableName;
   }

   public boolean isAutoMigrationTableNameSet() {
      return this._isSet(49);
   }

   public int getAdditionalAutoMigrationAttempts() {
      return this._AdditionalAutoMigrationAttempts;
   }

   public boolean isAdditionalAutoMigrationAttemptsSet() {
      return this._isSet(50);
   }

   public void setAdditionalAutoMigrationAttempts(int var1) {
      int var2 = this._AdditionalAutoMigrationAttempts;
      this._AdditionalAutoMigrationAttempts = var1;
      this._postSet(50, var2, var1);
   }

   public long getMillisToSleepBetweenAutoMigrationAttempts() {
      return this._MillisToSleepBetweenAutoMigrationAttempts;
   }

   public boolean isMillisToSleepBetweenAutoMigrationAttemptsSet() {
      return this._isSet(51);
   }

   public void setMillisToSleepBetweenAutoMigrationAttempts(long var1) {
      long var3 = this._MillisToSleepBetweenAutoMigrationAttempts;
      this._MillisToSleepBetweenAutoMigrationAttempts = var1;
      this._postSet(51, var3, var1);
   }

   public void setReplicationTimeoutEnabled(boolean var1) {
      boolean var2 = this._ReplicationTimeoutEnabled;
      this._ReplicationTimeoutEnabled = var1;
      this._postSet(54, var2, var1);
   }

   public String getMigrationBasis() {
      return this._MigrationBasis;
   }

   public boolean isMigrationBasisSet() {
      return this._isSet(52);
   }

   public void setMigrationBasis(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"database", "consensus"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("MigrationBasis", var1, var2);
      String var3 = this._MigrationBasis;
      this._MigrationBasis = var1;
      this._postSet(52, var3, var1);
   }

   public int getConsensusParticipants() {
      return this._ConsensusParticipants;
   }

   public boolean isConsensusParticipantsSet() {
      return this._isSet(53);
   }

   public void setConsensusParticipants(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("ConsensusParticipants", (long)var1, 0L, 65536L);
      int var2 = this._ConsensusParticipants;
      this._ConsensusParticipants = var1;
      this._postSet(53, var2, var1);
   }

   public boolean isReplicationTimeoutEnabled() {
      return this._ReplicationTimeoutEnabled;
   }

   public boolean isReplicationTimeoutEnabledSet() {
      return this._isSet(54);
   }

   public OverloadProtectionMBean getOverloadProtection() {
      return this._OverloadProtection;
   }

   public boolean isOverloadProtectionSet() {
      return this._isSet(55) || this._isAnythingSet((AbstractDescriptorBean)this.getOverloadProtection());
   }

   public void setOverloadProtection(OverloadProtectionMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 55)) {
         this._postCreate(var2);
      }

      OverloadProtectionMBean var3 = this._OverloadProtection;
      this._OverloadProtection = var1;
      this._postSet(55, var3, var1);
   }

   public DatabaseLessLeasingBasisMBean getDatabaseLessLeasingBasis() {
      return this._DatabaseLessLeasingBasis;
   }

   public boolean isDatabaseLessLeasingBasisSet() {
      return this._isSet(56) || this._isAnythingSet((AbstractDescriptorBean)this.getDatabaseLessLeasingBasis());
   }

   public void setDatabaseLessLeasingBasis(DatabaseLessLeasingBasisMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 56)) {
         this._postCreate(var2);
      }

      DatabaseLessLeasingBasisMBean var3 = this._DatabaseLessLeasingBasis;
      this._DatabaseLessLeasingBasis = var1;
      this._postSet(56, var3, var1);
   }

   public void setHTTPPingRetryCount(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkMin("HTTPPingRetryCount", var1, 0);
      int var2 = this._HTTPPingRetryCount;
      this._HTTPPingRetryCount = var1;
      this._postSet(57, var2, var1);
   }

   public int getHTTPPingRetryCount() {
      return this._HTTPPingRetryCount;
   }

   public boolean isHTTPPingRetryCountSet() {
      return this._isSet(57);
   }

   public void setMaxServerCountForHttpPing(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkMin("MaxServerCountForHttpPing", var1, 0);
      int var2 = this._MaxServerCountForHttpPing;
      this._MaxServerCountForHttpPing = var1;
      this._postSet(58, var2, var1);
   }

   public int getMaxServerCountForHttpPing() {
      return this._MaxServerCountForHttpPing;
   }

   public boolean isMaxServerCountForHttpPingSet() {
      return this._isSet(58);
   }

   public boolean isSecureReplicationEnabled() {
      return this._SecureReplicationEnabled;
   }

   public boolean isSecureReplicationEnabledSet() {
      return this._isSet(59);
   }

   public void setSecureReplicationEnabled(boolean var1) {
      boolean var2 = this._SecureReplicationEnabled;
      this._SecureReplicationEnabled = var1;
      this._postSet(59, var2, var1);
   }

   public void setUnicastDiscoveryPeriodMillis(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkMin("UnicastDiscoveryPeriodMillis", var1, 1000);
      int var2 = this._UnicastDiscoveryPeriodMillis;
      this._UnicastDiscoveryPeriodMillis = var1;
      this._postSet(60, var2, var1);
   }

   public int getUnicastDiscoveryPeriodMillis() {
      return this._UnicastDiscoveryPeriodMillis;
   }

   public boolean isUnicastDiscoveryPeriodMillisSet() {
      return this._isSet(60);
   }

   public void setMessageOrderingEnabled(boolean var1) {
      boolean var2 = this._MessageOrderingEnabled;
      this._MessageOrderingEnabled = var1;
      this._postSet(61, var2, var1);
   }

   public boolean isMessageOrderingEnabled() {
      return this._MessageOrderingEnabled;
   }

   public boolean isMessageOrderingEnabledSet() {
      return this._isSet(61);
   }

   public void setOneWayRmiForReplicationEnabled(boolean var1) {
      boolean var2 = this._OneWayRmiForReplicationEnabled;
      this._OneWayRmiForReplicationEnabled = var1;
      this._postSet(62, var2, var1);
   }

   public boolean isOneWayRmiForReplicationEnabled() {
      return this._OneWayRmiForReplicationEnabled;
   }

   public boolean isOneWayRmiForReplicationEnabledSet() {
      return this._isSet(62);
   }

   public void setSessionLazyDeserializationEnabled(boolean var1) {
      boolean var2 = this._SessionLazyDeserializationEnabled;
      this._SessionLazyDeserializationEnabled = var1;
      this._postSet(63, var2, var1);
   }

   public boolean isSessionLazyDeserializationEnabled() {
      if (!this._isSet(63)) {
         try {
            return ((DomainMBean)this.getParent()).isExalogicOptimizationsEnabled();
         } catch (NullPointerException var2) {
         }
      }

      return this._SessionLazyDeserializationEnabled;
   }

   public boolean isSessionLazyDeserializationEnabledSet() {
      return this._isSet(63);
   }

   public void setDeathDetectorHeartbeatPeriod(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkMin("DeathDetectorHeartbeatPeriod", var1, 1);
      int var2 = this._DeathDetectorHeartbeatPeriod;
      this._DeathDetectorHeartbeatPeriod = var1;
      this._postSet(64, var2, var1);
   }

   public int getDeathDetectorHeartbeatPeriod() {
      return this._DeathDetectorHeartbeatPeriod;
   }

   public boolean isDeathDetectorHeartbeatPeriodSet() {
      return this._isSet(64);
   }

   public void setMemberDeathDetectorEnabled(boolean var1) {
      boolean var2 = this._MemberDeathDetectorEnabled;
      this._MemberDeathDetectorEnabled = var1;
      this._postSet(65, var2, var1);
   }

   public boolean isMemberDeathDetectorEnabled() {
      return this._MemberDeathDetectorEnabled;
   }

   public boolean isMemberDeathDetectorEnabledSet() {
      return this._isSet(65);
   }

   public Object _getKey() {
      return this.getName();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
   }

   public boolean _hasKey() {
      return true;
   }

   public boolean _isPropertyAKey(Munger.ReaderEventInfo var1) {
      String var2 = var1.getElementName();
      switch (var2.length()) {
         case 4:
            if (var2.equals("name")) {
               return var1.compareXpaths(this._getPropertyXpath("name"));
            }

            return super._isPropertyAKey(var1);
         default:
            return super._isPropertyAKey(var1);
      }
   }

   protected void _unSet(int var1) {
      if (!this._initializeProperty(var1)) {
         super._unSet(var1);
      } else {
         this._markSet(var1, false);
      }

   }

   protected AbstractDescriptorBeanHelper _createHelper() {
      return new Helper(this);
   }

   public boolean _isAnythingSet() {
      return super._isAnythingSet() || this.isDatabaseLessLeasingBasisSet() || this.isOverloadProtectionSet();
   }

   private boolean _initializeProperty(int var1) {
      boolean var2 = var1 > -1;
      if (!var2) {
         var1 = 50;
      }

      try {
         switch (var1) {
            case 50:
               this._AdditionalAutoMigrationAttempts = 3;
               if (var2) {
                  break;
               }
            case 36:
               this._AsyncSessionQueueTimeout = 30;
               if (var2) {
                  break;
               }
            case 49:
               this._AutoMigrationTableName = "ACTIVE";
               if (var2) {
                  break;
               }
            case 40:
               this._CandidateMachinesForMigratableServers = new MachineMBean[0];
               if (var2) {
                  break;
               }
            case 9:
               this._ClusterAddress = null;
               if (var2) {
                  break;
               }
            case 17:
               this._ClusterBroadcastChannel = null;
               if (var2) {
                  break;
               }
            case 16:
               this._ClusterMessagingMode = "multicast";
               if (var2) {
                  break;
               }
            case 47:
               this._ClusterType = "none";
               if (var2) {
                  break;
               }
            case 53:
               this._ConsensusParticipants = 0;
               if (var2) {
                  break;
               }
            case 41:
               this._DataSourceForAutomaticMigration = null;
               if (var2) {
                  break;
               }
            case 33:
               this._DataSourceForJobScheduler = null;
               if (var2) {
                  break;
               }
            case 32:
               this._DataSourceForSessionPersistence = null;
               if (var2) {
                  break;
               }
            case 56:
               this._DatabaseLessLeasingBasis = new DatabaseLessLeasingBasisMBeanImpl(this, 56);
               this._postCreate((AbstractDescriptorBean)this._DatabaseLessLeasingBasis);
               if (var2) {
                  break;
               }
            case 64:
               this._DeathDetectorHeartbeatPeriod = 1;
               if (var2) {
                  break;
               }
            case 15:
               this._DefaultLoadAlgorithm = "round-robin";
               if (var2) {
                  break;
               }
            case 44:
               this._FencingGracePeriodMillis = 30000;
               if (var2) {
                  break;
               }
            case 25:
               this._FrontendHTTPPort = 0;
               if (var2) {
                  break;
               }
            case 26:
               this._FrontendHTTPSPort = 0;
               if (var2) {
                  break;
               }
            case 24:
               this._FrontendHost = null;
               if (var2) {
                  break;
               }
            case 37:
               this._GreedySessionFlushInterval = 3;
               if (var2) {
                  break;
               }
            case 57:
               this._HTTPPingRetryCount = 3;
               if (var2) {
                  break;
               }
            case 42:
               this._HealthCheckIntervalMillis = 10000;
               if (var2) {
                  break;
               }
            case 43:
               this._HealthCheckPeriodsUntilFencing = 3;
               if (var2) {
                  break;
               }
            case 27:
               this._IdlePeriodsUntilTimeout = 3;
               if (var2) {
                  break;
               }
            case 31:
               this._InterClusterCommLinkHealthCheckInterval = 30000;
               if (var2) {
                  break;
               }
            case 34:
               this._JobSchedulerTableName = "weblogic_timers";
               if (var2) {
                  break;
               }
            case 58:
               this._MaxServerCountForHttpPing = 0;
               if (var2) {
                  break;
               }
            case 22:
               this._MemberWarmupTimeoutSeconds = 30;
               if (var2) {
                  break;
               }
            case 21:
               this._MigratableTargets = new MigratableTargetMBean[0];
               if (var2) {
                  break;
               }
            case 52:
               this._MigrationBasis = "database";
               if (var2) {
                  break;
               }
            case 51:
               this._MillisToSleepBetweenAutoMigrationAttempts = 180000L;
               if (var2) {
                  break;
               }
            case 10:
               this._customizer.setMulticastAddress("239.192.0.0");
               if (var2) {
                  break;
               }
            case 11:
               this._MulticastBufferSize = 64;
               if (var2) {
                  break;
               }
            case 48:
               this._MulticastDataEncryption = false;
               if (var2) {
                  break;
               }
            case 12:
               this._MulticastPort = 7001;
               if (var2) {
                  break;
               }
            case 14:
               this._MulticastSendDelay = 3;
               if (var2) {
                  break;
               }
            case 13:
               this._MulticastTTL = 1;
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 46:
               this._NumberOfServersInClusterAddress = 3;
               if (var2) {
                  break;
               }
            case 55:
               this._OverloadProtection = new OverloadProtectionMBeanImpl(this, 55);
               this._postCreate((AbstractDescriptorBean)this._OverloadProtection);
               if (var2) {
                  break;
               }
            case 35:
               this._PersistSessionsOnShutdown = false;
               if (var2) {
                  break;
               }
            case 28:
               this._RemoteClusterAddress = null;
               if (var2) {
                  break;
               }
            case 30:
               this._ReplicationChannel = "ReplicationChannel";
               if (var2) {
                  break;
               }
            case 7:
               this._ServerNames = null;
               if (var2) {
                  break;
               }
            case 8:
               this._Servers = new ServerMBean[0];
               if (var2) {
                  break;
               }
            case 18:
               this._ServiceAgeThresholdSeconds = 180;
               if (var2) {
                  break;
               }
            case 38:
               this._SessionFlushInterval = 180;
               if (var2) {
                  break;
               }
            case 39:
               this._SessionFlushThreshold = 10000;
               if (var2) {
                  break;
               }
            case 45:
               this._SingletonSQLQueryHelper = "";
               if (var2) {
                  break;
               }
            case 60:
               this._UnicastDiscoveryPeriodMillis = 3000;
               if (var2) {
                  break;
               }
            case 29:
               this._WANSessionPersistenceTableName = "WLS_WAN_PERSISTENCE_TABLE";
               if (var2) {
                  break;
               }
            case 19:
               this._ClientCertProxyEnabled = false;
               if (var2) {
                  break;
               }
            case 23:
               this._HttpTraceSupportEnabled = false;
               if (var2) {
                  break;
               }
            case 65:
               this._MemberDeathDetectorEnabled = false;
               if (var2) {
                  break;
               }
            case 61:
               this._MessageOrderingEnabled = false;
               if (var2) {
                  break;
               }
            case 62:
               this._OneWayRmiForReplicationEnabled = false;
               if (var2) {
                  break;
               }
            case 54:
               this._ReplicationTimeoutEnabled = true;
               if (var2) {
                  break;
               }
            case 59:
               this._SecureReplicationEnabled = false;
               if (var2) {
                  break;
               }
            case 63:
               this._SessionLazyDeserializationEnabled = false;
               if (var2) {
                  break;
               }
            case 20:
               this._WeblogicPluginEnabled = false;
               if (var2) {
                  break;
               }
            case 3:
            case 4:
            case 5:
            case 6:
            default:
               if (var2) {
                  return false;
               }
         }

         return true;
      } catch (RuntimeException var4) {
         throw var4;
      } catch (Exception var5) {
         throw (Error)(new AssertionError("Impossible Exception")).initCause(var5);
      }
   }

   public Munger.SchemaHelper _getSchemaHelper() {
      return null;
   }

   public String _getElementName(int var1) {
      return this._getSchemaHelper2().getElementName(var1);
   }

   protected String getSchemaLocation() {
      return "http://xmlns.oracle.com/weblogic/1.0/domain.xsd";
   }

   protected String getTargetNamespace() {
      return "http://xmlns.oracle.com/weblogic/domain";
   }

   public SchemaHelper _getSchemaHelper2() {
      if (_schemaHelper == null) {
         _schemaHelper = new SchemaHelper2();
      }

      return _schemaHelper;
   }

   public String getType() {
      return "Cluster";
   }

   public void putValue(String var1, Object var2) {
      int var7;
      if (var1.equals("AdditionalAutoMigrationAttempts")) {
         var7 = this._AdditionalAutoMigrationAttempts;
         this._AdditionalAutoMigrationAttempts = (Integer)var2;
         this._postSet(50, var7, this._AdditionalAutoMigrationAttempts);
      } else if (var1.equals("AsyncSessionQueueTimeout")) {
         var7 = this._AsyncSessionQueueTimeout;
         this._AsyncSessionQueueTimeout = (Integer)var2;
         this._postSet(36, var7, this._AsyncSessionQueueTimeout);
      } else {
         String var6;
         if (var1.equals("AutoMigrationTableName")) {
            var6 = this._AutoMigrationTableName;
            this._AutoMigrationTableName = (String)var2;
            this._postSet(49, var6, this._AutoMigrationTableName);
         } else if (var1.equals("CandidateMachinesForMigratableServers")) {
            MachineMBean[] var15 = this._CandidateMachinesForMigratableServers;
            this._CandidateMachinesForMigratableServers = (MachineMBean[])((MachineMBean[])var2);
            this._postSet(40, var15, this._CandidateMachinesForMigratableServers);
         } else {
            boolean var5;
            if (var1.equals("ClientCertProxyEnabled")) {
               var5 = this._ClientCertProxyEnabled;
               this._ClientCertProxyEnabled = (Boolean)var2;
               this._postSet(19, var5, this._ClientCertProxyEnabled);
            } else if (var1.equals("ClusterAddress")) {
               var6 = this._ClusterAddress;
               this._ClusterAddress = (String)var2;
               this._postSet(9, var6, this._ClusterAddress);
            } else if (var1.equals("ClusterBroadcastChannel")) {
               var6 = this._ClusterBroadcastChannel;
               this._ClusterBroadcastChannel = (String)var2;
               this._postSet(17, var6, this._ClusterBroadcastChannel);
            } else if (var1.equals("ClusterMessagingMode")) {
               var6 = this._ClusterMessagingMode;
               this._ClusterMessagingMode = (String)var2;
               this._postSet(16, var6, this._ClusterMessagingMode);
            } else if (var1.equals("ClusterType")) {
               var6 = this._ClusterType;
               this._ClusterType = (String)var2;
               this._postSet(47, var6, this._ClusterType);
            } else if (var1.equals("ConsensusParticipants")) {
               var7 = this._ConsensusParticipants;
               this._ConsensusParticipants = (Integer)var2;
               this._postSet(53, var7, this._ConsensusParticipants);
            } else {
               JDBCSystemResourceMBean var14;
               if (var1.equals("DataSourceForAutomaticMigration")) {
                  var14 = this._DataSourceForAutomaticMigration;
                  this._DataSourceForAutomaticMigration = (JDBCSystemResourceMBean)var2;
                  this._postSet(41, var14, this._DataSourceForAutomaticMigration);
               } else if (var1.equals("DataSourceForJobScheduler")) {
                  var14 = this._DataSourceForJobScheduler;
                  this._DataSourceForJobScheduler = (JDBCSystemResourceMBean)var2;
                  this._postSet(33, var14, this._DataSourceForJobScheduler);
               } else if (var1.equals("DataSourceForSessionPersistence")) {
                  var14 = this._DataSourceForSessionPersistence;
                  this._DataSourceForSessionPersistence = (JDBCSystemResourceMBean)var2;
                  this._postSet(32, var14, this._DataSourceForSessionPersistence);
               } else if (var1.equals("DatabaseLessLeasingBasis")) {
                  DatabaseLessLeasingBasisMBean var13 = this._DatabaseLessLeasingBasis;
                  this._DatabaseLessLeasingBasis = (DatabaseLessLeasingBasisMBean)var2;
                  this._postSet(56, var13, this._DatabaseLessLeasingBasis);
               } else if (var1.equals("DeathDetectorHeartbeatPeriod")) {
                  var7 = this._DeathDetectorHeartbeatPeriod;
                  this._DeathDetectorHeartbeatPeriod = (Integer)var2;
                  this._postSet(64, var7, this._DeathDetectorHeartbeatPeriod);
               } else if (var1.equals("DefaultLoadAlgorithm")) {
                  var6 = this._DefaultLoadAlgorithm;
                  this._DefaultLoadAlgorithm = (String)var2;
                  this._postSet(15, var6, this._DefaultLoadAlgorithm);
               } else if (var1.equals("FencingGracePeriodMillis")) {
                  var7 = this._FencingGracePeriodMillis;
                  this._FencingGracePeriodMillis = (Integer)var2;
                  this._postSet(44, var7, this._FencingGracePeriodMillis);
               } else if (var1.equals("FrontendHTTPPort")) {
                  var7 = this._FrontendHTTPPort;
                  this._FrontendHTTPPort = (Integer)var2;
                  this._postSet(25, var7, this._FrontendHTTPPort);
               } else if (var1.equals("FrontendHTTPSPort")) {
                  var7 = this._FrontendHTTPSPort;
                  this._FrontendHTTPSPort = (Integer)var2;
                  this._postSet(26, var7, this._FrontendHTTPSPort);
               } else if (var1.equals("FrontendHost")) {
                  var6 = this._FrontendHost;
                  this._FrontendHost = (String)var2;
                  this._postSet(24, var6, this._FrontendHost);
               } else if (var1.equals("GreedySessionFlushInterval")) {
                  var7 = this._GreedySessionFlushInterval;
                  this._GreedySessionFlushInterval = (Integer)var2;
                  this._postSet(37, var7, this._GreedySessionFlushInterval);
               } else if (var1.equals("HTTPPingRetryCount")) {
                  var7 = this._HTTPPingRetryCount;
                  this._HTTPPingRetryCount = (Integer)var2;
                  this._postSet(57, var7, this._HTTPPingRetryCount);
               } else if (var1.equals("HealthCheckIntervalMillis")) {
                  var7 = this._HealthCheckIntervalMillis;
                  this._HealthCheckIntervalMillis = (Integer)var2;
                  this._postSet(42, var7, this._HealthCheckIntervalMillis);
               } else if (var1.equals("HealthCheckPeriodsUntilFencing")) {
                  var7 = this._HealthCheckPeriodsUntilFencing;
                  this._HealthCheckPeriodsUntilFencing = (Integer)var2;
                  this._postSet(43, var7, this._HealthCheckPeriodsUntilFencing);
               } else if (var1.equals("HttpTraceSupportEnabled")) {
                  var5 = this._HttpTraceSupportEnabled;
                  this._HttpTraceSupportEnabled = (Boolean)var2;
                  this._postSet(23, var5, this._HttpTraceSupportEnabled);
               } else if (var1.equals("IdlePeriodsUntilTimeout")) {
                  var7 = this._IdlePeriodsUntilTimeout;
                  this._IdlePeriodsUntilTimeout = (Integer)var2;
                  this._postSet(27, var7, this._IdlePeriodsUntilTimeout);
               } else if (var1.equals("InterClusterCommLinkHealthCheckInterval")) {
                  var7 = this._InterClusterCommLinkHealthCheckInterval;
                  this._InterClusterCommLinkHealthCheckInterval = (Integer)var2;
                  this._postSet(31, var7, this._InterClusterCommLinkHealthCheckInterval);
               } else if (var1.equals("JobSchedulerTableName")) {
                  var6 = this._JobSchedulerTableName;
                  this._JobSchedulerTableName = (String)var2;
                  this._postSet(34, var6, this._JobSchedulerTableName);
               } else if (var1.equals("MaxServerCountForHttpPing")) {
                  var7 = this._MaxServerCountForHttpPing;
                  this._MaxServerCountForHttpPing = (Integer)var2;
                  this._postSet(58, var7, this._MaxServerCountForHttpPing);
               } else if (var1.equals("MemberDeathDetectorEnabled")) {
                  var5 = this._MemberDeathDetectorEnabled;
                  this._MemberDeathDetectorEnabled = (Boolean)var2;
                  this._postSet(65, var5, this._MemberDeathDetectorEnabled);
               } else if (var1.equals("MemberWarmupTimeoutSeconds")) {
                  var7 = this._MemberWarmupTimeoutSeconds;
                  this._MemberWarmupTimeoutSeconds = (Integer)var2;
                  this._postSet(22, var7, this._MemberWarmupTimeoutSeconds);
               } else if (var1.equals("MessageOrderingEnabled")) {
                  var5 = this._MessageOrderingEnabled;
                  this._MessageOrderingEnabled = (Boolean)var2;
                  this._postSet(61, var5, this._MessageOrderingEnabled);
               } else if (var1.equals("MigratableTargets")) {
                  MigratableTargetMBean[] var12 = this._MigratableTargets;
                  this._MigratableTargets = (MigratableTargetMBean[])((MigratableTargetMBean[])var2);
                  this._postSet(21, var12, this._MigratableTargets);
               } else if (var1.equals("MigrationBasis")) {
                  var6 = this._MigrationBasis;
                  this._MigrationBasis = (String)var2;
                  this._postSet(52, var6, this._MigrationBasis);
               } else if (var1.equals("MillisToSleepBetweenAutoMigrationAttempts")) {
                  long var11 = this._MillisToSleepBetweenAutoMigrationAttempts;
                  this._MillisToSleepBetweenAutoMigrationAttempts = (Long)var2;
                  this._postSet(51, var11, this._MillisToSleepBetweenAutoMigrationAttempts);
               } else if (var1.equals("MulticastAddress")) {
                  var6 = this._MulticastAddress;
                  this._MulticastAddress = (String)var2;
                  this._postSet(10, var6, this._MulticastAddress);
               } else if (var1.equals("MulticastBufferSize")) {
                  var7 = this._MulticastBufferSize;
                  this._MulticastBufferSize = (Integer)var2;
                  this._postSet(11, var7, this._MulticastBufferSize);
               } else if (var1.equals("MulticastDataEncryption")) {
                  var5 = this._MulticastDataEncryption;
                  this._MulticastDataEncryption = (Boolean)var2;
                  this._postSet(48, var5, this._MulticastDataEncryption);
               } else if (var1.equals("MulticastPort")) {
                  var7 = this._MulticastPort;
                  this._MulticastPort = (Integer)var2;
                  this._postSet(12, var7, this._MulticastPort);
               } else if (var1.equals("MulticastSendDelay")) {
                  var7 = this._MulticastSendDelay;
                  this._MulticastSendDelay = (Integer)var2;
                  this._postSet(14, var7, this._MulticastSendDelay);
               } else if (var1.equals("MulticastTTL")) {
                  var7 = this._MulticastTTL;
                  this._MulticastTTL = (Integer)var2;
                  this._postSet(13, var7, this._MulticastTTL);
               } else if (var1.equals("Name")) {
                  var6 = this._Name;
                  this._Name = (String)var2;
                  this._postSet(2, var6, this._Name);
               } else if (var1.equals("NumberOfServersInClusterAddress")) {
                  var7 = this._NumberOfServersInClusterAddress;
                  this._NumberOfServersInClusterAddress = (Integer)var2;
                  this._postSet(46, var7, this._NumberOfServersInClusterAddress);
               } else if (var1.equals("OneWayRmiForReplicationEnabled")) {
                  var5 = this._OneWayRmiForReplicationEnabled;
                  this._OneWayRmiForReplicationEnabled = (Boolean)var2;
                  this._postSet(62, var5, this._OneWayRmiForReplicationEnabled);
               } else if (var1.equals("OverloadProtection")) {
                  OverloadProtectionMBean var10 = this._OverloadProtection;
                  this._OverloadProtection = (OverloadProtectionMBean)var2;
                  this._postSet(55, var10, this._OverloadProtection);
               } else if (var1.equals("PersistSessionsOnShutdown")) {
                  var5 = this._PersistSessionsOnShutdown;
                  this._PersistSessionsOnShutdown = (Boolean)var2;
                  this._postSet(35, var5, this._PersistSessionsOnShutdown);
               } else if (var1.equals("RemoteClusterAddress")) {
                  var6 = this._RemoteClusterAddress;
                  this._RemoteClusterAddress = (String)var2;
                  this._postSet(28, var6, this._RemoteClusterAddress);
               } else if (var1.equals("ReplicationChannel")) {
                  var6 = this._ReplicationChannel;
                  this._ReplicationChannel = (String)var2;
                  this._postSet(30, var6, this._ReplicationChannel);
               } else if (var1.equals("ReplicationTimeoutEnabled")) {
                  var5 = this._ReplicationTimeoutEnabled;
                  this._ReplicationTimeoutEnabled = (Boolean)var2;
                  this._postSet(54, var5, this._ReplicationTimeoutEnabled);
               } else if (var1.equals("SecureReplicationEnabled")) {
                  var5 = this._SecureReplicationEnabled;
                  this._SecureReplicationEnabled = (Boolean)var2;
                  this._postSet(59, var5, this._SecureReplicationEnabled);
               } else if (var1.equals("ServerNames")) {
                  Set var9 = this._ServerNames;
                  this._ServerNames = (Set)var2;
                  this._postSet(7, var9, this._ServerNames);
               } else if (var1.equals("Servers")) {
                  ServerMBean[] var8 = this._Servers;
                  this._Servers = (ServerMBean[])((ServerMBean[])var2);
                  this._postSet(8, var8, this._Servers);
               } else if (var1.equals("ServiceAgeThresholdSeconds")) {
                  var7 = this._ServiceAgeThresholdSeconds;
                  this._ServiceAgeThresholdSeconds = (Integer)var2;
                  this._postSet(18, var7, this._ServiceAgeThresholdSeconds);
               } else if (var1.equals("SessionFlushInterval")) {
                  var7 = this._SessionFlushInterval;
                  this._SessionFlushInterval = (Integer)var2;
                  this._postSet(38, var7, this._SessionFlushInterval);
               } else if (var1.equals("SessionFlushThreshold")) {
                  var7 = this._SessionFlushThreshold;
                  this._SessionFlushThreshold = (Integer)var2;
                  this._postSet(39, var7, this._SessionFlushThreshold);
               } else if (var1.equals("SessionLazyDeserializationEnabled")) {
                  var5 = this._SessionLazyDeserializationEnabled;
                  this._SessionLazyDeserializationEnabled = (Boolean)var2;
                  this._postSet(63, var5, this._SessionLazyDeserializationEnabled);
               } else if (var1.equals("SingletonSQLQueryHelper")) {
                  var6 = this._SingletonSQLQueryHelper;
                  this._SingletonSQLQueryHelper = (String)var2;
                  this._postSet(45, var6, this._SingletonSQLQueryHelper);
               } else if (var1.equals("UnicastDiscoveryPeriodMillis")) {
                  var7 = this._UnicastDiscoveryPeriodMillis;
                  this._UnicastDiscoveryPeriodMillis = (Integer)var2;
                  this._postSet(60, var7, this._UnicastDiscoveryPeriodMillis);
               } else if (var1.equals("WANSessionPersistenceTableName")) {
                  var6 = this._WANSessionPersistenceTableName;
                  this._WANSessionPersistenceTableName = (String)var2;
                  this._postSet(29, var6, this._WANSessionPersistenceTableName);
               } else if (var1.equals("WeblogicPluginEnabled")) {
                  var5 = this._WeblogicPluginEnabled;
                  this._WeblogicPluginEnabled = (Boolean)var2;
                  this._postSet(20, var5, this._WeblogicPluginEnabled);
               } else if (var1.equals("customizer")) {
                  Cluster var3 = this._customizer;
                  this._customizer = (Cluster)var2;
               } else {
                  super.putValue(var1, var2);
               }
            }
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("AdditionalAutoMigrationAttempts")) {
         return new Integer(this._AdditionalAutoMigrationAttempts);
      } else if (var1.equals("AsyncSessionQueueTimeout")) {
         return new Integer(this._AsyncSessionQueueTimeout);
      } else if (var1.equals("AutoMigrationTableName")) {
         return this._AutoMigrationTableName;
      } else if (var1.equals("CandidateMachinesForMigratableServers")) {
         return this._CandidateMachinesForMigratableServers;
      } else if (var1.equals("ClientCertProxyEnabled")) {
         return new Boolean(this._ClientCertProxyEnabled);
      } else if (var1.equals("ClusterAddress")) {
         return this._ClusterAddress;
      } else if (var1.equals("ClusterBroadcastChannel")) {
         return this._ClusterBroadcastChannel;
      } else if (var1.equals("ClusterMessagingMode")) {
         return this._ClusterMessagingMode;
      } else if (var1.equals("ClusterType")) {
         return this._ClusterType;
      } else if (var1.equals("ConsensusParticipants")) {
         return new Integer(this._ConsensusParticipants);
      } else if (var1.equals("DataSourceForAutomaticMigration")) {
         return this._DataSourceForAutomaticMigration;
      } else if (var1.equals("DataSourceForJobScheduler")) {
         return this._DataSourceForJobScheduler;
      } else if (var1.equals("DataSourceForSessionPersistence")) {
         return this._DataSourceForSessionPersistence;
      } else if (var1.equals("DatabaseLessLeasingBasis")) {
         return this._DatabaseLessLeasingBasis;
      } else if (var1.equals("DeathDetectorHeartbeatPeriod")) {
         return new Integer(this._DeathDetectorHeartbeatPeriod);
      } else if (var1.equals("DefaultLoadAlgorithm")) {
         return this._DefaultLoadAlgorithm;
      } else if (var1.equals("FencingGracePeriodMillis")) {
         return new Integer(this._FencingGracePeriodMillis);
      } else if (var1.equals("FrontendHTTPPort")) {
         return new Integer(this._FrontendHTTPPort);
      } else if (var1.equals("FrontendHTTPSPort")) {
         return new Integer(this._FrontendHTTPSPort);
      } else if (var1.equals("FrontendHost")) {
         return this._FrontendHost;
      } else if (var1.equals("GreedySessionFlushInterval")) {
         return new Integer(this._GreedySessionFlushInterval);
      } else if (var1.equals("HTTPPingRetryCount")) {
         return new Integer(this._HTTPPingRetryCount);
      } else if (var1.equals("HealthCheckIntervalMillis")) {
         return new Integer(this._HealthCheckIntervalMillis);
      } else if (var1.equals("HealthCheckPeriodsUntilFencing")) {
         return new Integer(this._HealthCheckPeriodsUntilFencing);
      } else if (var1.equals("HttpTraceSupportEnabled")) {
         return new Boolean(this._HttpTraceSupportEnabled);
      } else if (var1.equals("IdlePeriodsUntilTimeout")) {
         return new Integer(this._IdlePeriodsUntilTimeout);
      } else if (var1.equals("InterClusterCommLinkHealthCheckInterval")) {
         return new Integer(this._InterClusterCommLinkHealthCheckInterval);
      } else if (var1.equals("JobSchedulerTableName")) {
         return this._JobSchedulerTableName;
      } else if (var1.equals("MaxServerCountForHttpPing")) {
         return new Integer(this._MaxServerCountForHttpPing);
      } else if (var1.equals("MemberDeathDetectorEnabled")) {
         return new Boolean(this._MemberDeathDetectorEnabled);
      } else if (var1.equals("MemberWarmupTimeoutSeconds")) {
         return new Integer(this._MemberWarmupTimeoutSeconds);
      } else if (var1.equals("MessageOrderingEnabled")) {
         return new Boolean(this._MessageOrderingEnabled);
      } else if (var1.equals("MigratableTargets")) {
         return this._MigratableTargets;
      } else if (var1.equals("MigrationBasis")) {
         return this._MigrationBasis;
      } else if (var1.equals("MillisToSleepBetweenAutoMigrationAttempts")) {
         return new Long(this._MillisToSleepBetweenAutoMigrationAttempts);
      } else if (var1.equals("MulticastAddress")) {
         return this._MulticastAddress;
      } else if (var1.equals("MulticastBufferSize")) {
         return new Integer(this._MulticastBufferSize);
      } else if (var1.equals("MulticastDataEncryption")) {
         return new Boolean(this._MulticastDataEncryption);
      } else if (var1.equals("MulticastPort")) {
         return new Integer(this._MulticastPort);
      } else if (var1.equals("MulticastSendDelay")) {
         return new Integer(this._MulticastSendDelay);
      } else if (var1.equals("MulticastTTL")) {
         return new Integer(this._MulticastTTL);
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("NumberOfServersInClusterAddress")) {
         return new Integer(this._NumberOfServersInClusterAddress);
      } else if (var1.equals("OneWayRmiForReplicationEnabled")) {
         return new Boolean(this._OneWayRmiForReplicationEnabled);
      } else if (var1.equals("OverloadProtection")) {
         return this._OverloadProtection;
      } else if (var1.equals("PersistSessionsOnShutdown")) {
         return new Boolean(this._PersistSessionsOnShutdown);
      } else if (var1.equals("RemoteClusterAddress")) {
         return this._RemoteClusterAddress;
      } else if (var1.equals("ReplicationChannel")) {
         return this._ReplicationChannel;
      } else if (var1.equals("ReplicationTimeoutEnabled")) {
         return new Boolean(this._ReplicationTimeoutEnabled);
      } else if (var1.equals("SecureReplicationEnabled")) {
         return new Boolean(this._SecureReplicationEnabled);
      } else if (var1.equals("ServerNames")) {
         return this._ServerNames;
      } else if (var1.equals("Servers")) {
         return this._Servers;
      } else if (var1.equals("ServiceAgeThresholdSeconds")) {
         return new Integer(this._ServiceAgeThresholdSeconds);
      } else if (var1.equals("SessionFlushInterval")) {
         return new Integer(this._SessionFlushInterval);
      } else if (var1.equals("SessionFlushThreshold")) {
         return new Integer(this._SessionFlushThreshold);
      } else if (var1.equals("SessionLazyDeserializationEnabled")) {
         return new Boolean(this._SessionLazyDeserializationEnabled);
      } else if (var1.equals("SingletonSQLQueryHelper")) {
         return this._SingletonSQLQueryHelper;
      } else if (var1.equals("UnicastDiscoveryPeriodMillis")) {
         return new Integer(this._UnicastDiscoveryPeriodMillis);
      } else if (var1.equals("WANSessionPersistenceTableName")) {
         return this._WANSessionPersistenceTableName;
      } else if (var1.equals("WeblogicPluginEnabled")) {
         return new Boolean(this._WeblogicPluginEnabled);
      } else {
         return var1.equals("customizer") ? this._customizer : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends TargetMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("name")) {
                  return 2;
               }
            case 5:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 16:
            case 32:
            case 33:
            case 37:
            case 38:
            case 39:
            case 41:
            case 42:
            case 43:
            case 44:
            case 46:
            default:
               break;
            case 6:
               if (var1.equals("server")) {
                  return 8;
               }
               break;
            case 12:
               if (var1.equals("cluster-type")) {
                  return 47;
               }

               if (var1.equals("multicastttl")) {
                  return 13;
               }

               if (var1.equals("server-names")) {
                  return 7;
               }
               break;
            case 13:
               if (var1.equals("frontend-host")) {
                  return 24;
               }
               break;
            case 14:
               if (var1.equals("multicast-port")) {
                  return 12;
               }
               break;
            case 15:
               if (var1.equals("cluster-address")) {
                  return 9;
               }

               if (var1.equals("migration-basis")) {
                  return 52;
               }
               break;
            case 17:
               if (var1.equals("frontendhttp-port")) {
                  return 25;
               }

               if (var1.equals("migratable-target")) {
                  return 21;
               }

               if (var1.equals("multicast-address")) {
                  return 10;
               }
               break;
            case 18:
               if (var1.equals("frontendhttps-port")) {
                  return 26;
               }
               break;
            case 19:
               if (var1.equals("overload-protection")) {
                  return 55;
               }

               if (var1.equals("replication-channel")) {
                  return 30;
               }
               break;
            case 20:
               if (var1.equals("multicast-send-delay")) {
                  return 14;
               }
               break;
            case 21:
               if (var1.equals("http-ping-retry-count")) {
                  return 57;
               }

               if (var1.equals("multicast-buffer-size")) {
                  return 11;
               }
               break;
            case 22:
               if (var1.equals("cluster-messaging-mode")) {
                  return 16;
               }

               if (var1.equals("consensus-participants")) {
                  return 53;
               }

               if (var1.equals("default-load-algorithm")) {
                  return 15;
               }

               if (var1.equals("remote-cluster-address")) {
                  return 28;
               }

               if (var1.equals("session-flush-interval")) {
                  return 38;
               }
               break;
            case 23:
               if (var1.equals("session-flush-threshold")) {
                  return 39;
               }

               if (var1.equals("weblogic-plugin-enabled")) {
                  return 20;
               }
               break;
            case 24:
               if (var1.equals("job-scheduler-table-name")) {
                  return 34;
               }

               if (var1.equals("message-ordering-enabled")) {
                  return 61;
               }
               break;
            case 25:
               if (var1.equals("auto-migration-table-name")) {
                  return 49;
               }

               if (var1.equals("cluster-broadcast-channel")) {
                  return 17;
               }

               if (var1.equals("multicast-data-encryption")) {
                  return 48;
               }

               if (var1.equals("client-cert-proxy-enabled")) {
                  return 19;
               }
               break;
            case 26:
               if (var1.equals("idle-periods-until-timeout")) {
                  return 27;
               }

               if (var1.equals("singleton-sql-query-helper")) {
                  return 45;
               }

               if (var1.equals("http-trace-support-enabled")) {
                  return 23;
               }

               if (var1.equals("secure-replication-enabled")) {
                  return 59;
               }
               break;
            case 27:
               if (var1.equals("async-session-queue-timeout")) {
                  return 36;
               }

               if (var1.equals("database-less-leasing-basis")) {
                  return 56;
               }

               if (var1.equals("fencing-grace-period-millis")) {
                  return 44;
               }

               if (var1.equals("replication-timeout-enabled")) {
                  return 54;
               }
               break;
            case 28:
               if (var1.equals("health-check-interval-millis")) {
                  return 42;
               }

               if (var1.equals("persist-sessions-on-shutdown")) {
                  return 35;
               }
               break;
            case 29:
               if (var1.equals("data-source-for-job-scheduler")) {
                  return 33;
               }

               if (var1.equals("greedy-session-flush-interval")) {
                  return 37;
               }

               if (var1.equals("member-warmup-timeout-seconds")) {
                  return 22;
               }

               if (var1.equals("service-age-threshold-seconds")) {
                  return 18;
               }

               if (var1.equals("member-death-detector-enabled")) {
                  return 65;
               }
               break;
            case 30:
               if (var1.equals("max-server-count-for-http-ping")) {
                  return 58;
               }
               break;
            case 31:
               if (var1.equals("death-detector-heartbeat-period")) {
                  return 64;
               }

               if (var1.equals("unicast-discovery-period-millis")) {
                  return 60;
               }
               break;
            case 34:
               if (var1.equals("additional-auto-migration-attempts")) {
                  return 50;
               }

               if (var1.equals("health-check-periods-until-fencing")) {
                  return 43;
               }

               if (var1.equals("wan-session-persistence-table-name")) {
                  return 29;
               }
               break;
            case 35:
               if (var1.equals("data-source-for-automatic-migration")) {
                  return 41;
               }

               if (var1.equals("data-source-for-session-persistence")) {
                  return 32;
               }

               if (var1.equals("one-way-rmi-for-replication-enabled")) {
                  return 62;
               }
               break;
            case 36:
               if (var1.equals("number-of-servers-in-cluster-address")) {
                  return 46;
               }

               if (var1.equals("session-lazy-deserialization-enabled")) {
                  return 63;
               }
               break;
            case 40:
               if (var1.equals("candidate-machines-for-migratable-server")) {
                  return 40;
               }
               break;
            case 45:
               if (var1.equals("inter-cluster-comm-link-health-check-interval")) {
                  return 31;
               }
               break;
            case 47:
               if (var1.equals("millis-to-sleep-between-auto-migration-attempts")) {
                  return 51;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 55:
               return new OverloadProtectionMBeanImpl.SchemaHelper2();
            case 56:
               return new DatabaseLessLeasingBasisMBeanImpl.SchemaHelper2();
            default:
               return super.getSchemaHelper(var1);
         }
      }

      public String getElementName(int var1) {
         switch (var1) {
            case 2:
               return "name";
            case 3:
            case 4:
            case 5:
            case 6:
            default:
               return super.getElementName(var1);
            case 7:
               return "server-names";
            case 8:
               return "server";
            case 9:
               return "cluster-address";
            case 10:
               return "multicast-address";
            case 11:
               return "multicast-buffer-size";
            case 12:
               return "multicast-port";
            case 13:
               return "multicastttl";
            case 14:
               return "multicast-send-delay";
            case 15:
               return "default-load-algorithm";
            case 16:
               return "cluster-messaging-mode";
            case 17:
               return "cluster-broadcast-channel";
            case 18:
               return "service-age-threshold-seconds";
            case 19:
               return "client-cert-proxy-enabled";
            case 20:
               return "weblogic-plugin-enabled";
            case 21:
               return "migratable-target";
            case 22:
               return "member-warmup-timeout-seconds";
            case 23:
               return "http-trace-support-enabled";
            case 24:
               return "frontend-host";
            case 25:
               return "frontendhttp-port";
            case 26:
               return "frontendhttps-port";
            case 27:
               return "idle-periods-until-timeout";
            case 28:
               return "remote-cluster-address";
            case 29:
               return "wan-session-persistence-table-name";
            case 30:
               return "replication-channel";
            case 31:
               return "inter-cluster-comm-link-health-check-interval";
            case 32:
               return "data-source-for-session-persistence";
            case 33:
               return "data-source-for-job-scheduler";
            case 34:
               return "job-scheduler-table-name";
            case 35:
               return "persist-sessions-on-shutdown";
            case 36:
               return "async-session-queue-timeout";
            case 37:
               return "greedy-session-flush-interval";
            case 38:
               return "session-flush-interval";
            case 39:
               return "session-flush-threshold";
            case 40:
               return "candidate-machines-for-migratable-server";
            case 41:
               return "data-source-for-automatic-migration";
            case 42:
               return "health-check-interval-millis";
            case 43:
               return "health-check-periods-until-fencing";
            case 44:
               return "fencing-grace-period-millis";
            case 45:
               return "singleton-sql-query-helper";
            case 46:
               return "number-of-servers-in-cluster-address";
            case 47:
               return "cluster-type";
            case 48:
               return "multicast-data-encryption";
            case 49:
               return "auto-migration-table-name";
            case 50:
               return "additional-auto-migration-attempts";
            case 51:
               return "millis-to-sleep-between-auto-migration-attempts";
            case 52:
               return "migration-basis";
            case 53:
               return "consensus-participants";
            case 54:
               return "replication-timeout-enabled";
            case 55:
               return "overload-protection";
            case 56:
               return "database-less-leasing-basis";
            case 57:
               return "http-ping-retry-count";
            case 58:
               return "max-server-count-for-http-ping";
            case 59:
               return "secure-replication-enabled";
            case 60:
               return "unicast-discovery-period-millis";
            case 61:
               return "message-ordering-enabled";
            case 62:
               return "one-way-rmi-for-replication-enabled";
            case 63:
               return "session-lazy-deserialization-enabled";
            case 64:
               return "death-detector-heartbeat-period";
            case 65:
               return "member-death-detector-enabled";
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 8:
               return true;
            case 21:
               return true;
            case 40:
               return true;
            default:
               return super.isArray(var1);
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            case 55:
               return true;
            case 56:
               return true;
            default:
               return super.isBean(var1);
         }
      }

      public boolean isConfigurable(int var1) {
         switch (var1) {
            case 9:
               return true;
            case 10:
               return true;
            case 11:
               return true;
            case 12:
               return true;
            case 13:
               return true;
            case 14:
               return true;
            case 15:
               return true;
            case 16:
               return true;
            case 17:
               return true;
            case 18:
               return true;
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 54:
            case 55:
            case 56:
            case 58:
            case 61:
            case 62:
            case 63:
            default:
               return super.isConfigurable(var1);
            case 24:
               return true;
            case 25:
               return true;
            case 26:
               return true;
            case 52:
               return true;
            case 53:
               return true;
            case 57:
               return true;
            case 59:
               return true;
            case 60:
               return true;
            case 64:
               return true;
         }
      }

      public boolean isKey(int var1) {
         switch (var1) {
            case 2:
               return true;
            default:
               return super.isKey(var1);
         }
      }

      public boolean hasKey() {
         return true;
      }

      public String[] getKeyElementNames() {
         ArrayList var1 = new ArrayList();
         var1.add("name");
         return (String[])((String[])var1.toArray(new String[0]));
      }
   }

   protected static class Helper extends TargetMBeanImpl.Helper {
      private ClusterMBeanImpl bean;

      protected Helper(ClusterMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 2:
               return "Name";
            case 3:
            case 4:
            case 5:
            case 6:
            default:
               return super.getPropertyName(var1);
            case 7:
               return "ServerNames";
            case 8:
               return "Servers";
            case 9:
               return "ClusterAddress";
            case 10:
               return "MulticastAddress";
            case 11:
               return "MulticastBufferSize";
            case 12:
               return "MulticastPort";
            case 13:
               return "MulticastTTL";
            case 14:
               return "MulticastSendDelay";
            case 15:
               return "DefaultLoadAlgorithm";
            case 16:
               return "ClusterMessagingMode";
            case 17:
               return "ClusterBroadcastChannel";
            case 18:
               return "ServiceAgeThresholdSeconds";
            case 19:
               return "ClientCertProxyEnabled";
            case 20:
               return "WeblogicPluginEnabled";
            case 21:
               return "MigratableTargets";
            case 22:
               return "MemberWarmupTimeoutSeconds";
            case 23:
               return "HttpTraceSupportEnabled";
            case 24:
               return "FrontendHost";
            case 25:
               return "FrontendHTTPPort";
            case 26:
               return "FrontendHTTPSPort";
            case 27:
               return "IdlePeriodsUntilTimeout";
            case 28:
               return "RemoteClusterAddress";
            case 29:
               return "WANSessionPersistenceTableName";
            case 30:
               return "ReplicationChannel";
            case 31:
               return "InterClusterCommLinkHealthCheckInterval";
            case 32:
               return "DataSourceForSessionPersistence";
            case 33:
               return "DataSourceForJobScheduler";
            case 34:
               return "JobSchedulerTableName";
            case 35:
               return "PersistSessionsOnShutdown";
            case 36:
               return "AsyncSessionQueueTimeout";
            case 37:
               return "GreedySessionFlushInterval";
            case 38:
               return "SessionFlushInterval";
            case 39:
               return "SessionFlushThreshold";
            case 40:
               return "CandidateMachinesForMigratableServers";
            case 41:
               return "DataSourceForAutomaticMigration";
            case 42:
               return "HealthCheckIntervalMillis";
            case 43:
               return "HealthCheckPeriodsUntilFencing";
            case 44:
               return "FencingGracePeriodMillis";
            case 45:
               return "SingletonSQLQueryHelper";
            case 46:
               return "NumberOfServersInClusterAddress";
            case 47:
               return "ClusterType";
            case 48:
               return "MulticastDataEncryption";
            case 49:
               return "AutoMigrationTableName";
            case 50:
               return "AdditionalAutoMigrationAttempts";
            case 51:
               return "MillisToSleepBetweenAutoMigrationAttempts";
            case 52:
               return "MigrationBasis";
            case 53:
               return "ConsensusParticipants";
            case 54:
               return "ReplicationTimeoutEnabled";
            case 55:
               return "OverloadProtection";
            case 56:
               return "DatabaseLessLeasingBasis";
            case 57:
               return "HTTPPingRetryCount";
            case 58:
               return "MaxServerCountForHttpPing";
            case 59:
               return "SecureReplicationEnabled";
            case 60:
               return "UnicastDiscoveryPeriodMillis";
            case 61:
               return "MessageOrderingEnabled";
            case 62:
               return "OneWayRmiForReplicationEnabled";
            case 63:
               return "SessionLazyDeserializationEnabled";
            case 64:
               return "DeathDetectorHeartbeatPeriod";
            case 65:
               return "MemberDeathDetectorEnabled";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("AdditionalAutoMigrationAttempts")) {
            return 50;
         } else if (var1.equals("AsyncSessionQueueTimeout")) {
            return 36;
         } else if (var1.equals("AutoMigrationTableName")) {
            return 49;
         } else if (var1.equals("CandidateMachinesForMigratableServers")) {
            return 40;
         } else if (var1.equals("ClusterAddress")) {
            return 9;
         } else if (var1.equals("ClusterBroadcastChannel")) {
            return 17;
         } else if (var1.equals("ClusterMessagingMode")) {
            return 16;
         } else if (var1.equals("ClusterType")) {
            return 47;
         } else if (var1.equals("ConsensusParticipants")) {
            return 53;
         } else if (var1.equals("DataSourceForAutomaticMigration")) {
            return 41;
         } else if (var1.equals("DataSourceForJobScheduler")) {
            return 33;
         } else if (var1.equals("DataSourceForSessionPersistence")) {
            return 32;
         } else if (var1.equals("DatabaseLessLeasingBasis")) {
            return 56;
         } else if (var1.equals("DeathDetectorHeartbeatPeriod")) {
            return 64;
         } else if (var1.equals("DefaultLoadAlgorithm")) {
            return 15;
         } else if (var1.equals("FencingGracePeriodMillis")) {
            return 44;
         } else if (var1.equals("FrontendHTTPPort")) {
            return 25;
         } else if (var1.equals("FrontendHTTPSPort")) {
            return 26;
         } else if (var1.equals("FrontendHost")) {
            return 24;
         } else if (var1.equals("GreedySessionFlushInterval")) {
            return 37;
         } else if (var1.equals("HTTPPingRetryCount")) {
            return 57;
         } else if (var1.equals("HealthCheckIntervalMillis")) {
            return 42;
         } else if (var1.equals("HealthCheckPeriodsUntilFencing")) {
            return 43;
         } else if (var1.equals("IdlePeriodsUntilTimeout")) {
            return 27;
         } else if (var1.equals("InterClusterCommLinkHealthCheckInterval")) {
            return 31;
         } else if (var1.equals("JobSchedulerTableName")) {
            return 34;
         } else if (var1.equals("MaxServerCountForHttpPing")) {
            return 58;
         } else if (var1.equals("MemberWarmupTimeoutSeconds")) {
            return 22;
         } else if (var1.equals("MigratableTargets")) {
            return 21;
         } else if (var1.equals("MigrationBasis")) {
            return 52;
         } else if (var1.equals("MillisToSleepBetweenAutoMigrationAttempts")) {
            return 51;
         } else if (var1.equals("MulticastAddress")) {
            return 10;
         } else if (var1.equals("MulticastBufferSize")) {
            return 11;
         } else if (var1.equals("MulticastDataEncryption")) {
            return 48;
         } else if (var1.equals("MulticastPort")) {
            return 12;
         } else if (var1.equals("MulticastSendDelay")) {
            return 14;
         } else if (var1.equals("MulticastTTL")) {
            return 13;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("NumberOfServersInClusterAddress")) {
            return 46;
         } else if (var1.equals("OverloadProtection")) {
            return 55;
         } else if (var1.equals("PersistSessionsOnShutdown")) {
            return 35;
         } else if (var1.equals("RemoteClusterAddress")) {
            return 28;
         } else if (var1.equals("ReplicationChannel")) {
            return 30;
         } else if (var1.equals("ServerNames")) {
            return 7;
         } else if (var1.equals("Servers")) {
            return 8;
         } else if (var1.equals("ServiceAgeThresholdSeconds")) {
            return 18;
         } else if (var1.equals("SessionFlushInterval")) {
            return 38;
         } else if (var1.equals("SessionFlushThreshold")) {
            return 39;
         } else if (var1.equals("SingletonSQLQueryHelper")) {
            return 45;
         } else if (var1.equals("UnicastDiscoveryPeriodMillis")) {
            return 60;
         } else if (var1.equals("WANSessionPersistenceTableName")) {
            return 29;
         } else if (var1.equals("ClientCertProxyEnabled")) {
            return 19;
         } else if (var1.equals("HttpTraceSupportEnabled")) {
            return 23;
         } else if (var1.equals("MemberDeathDetectorEnabled")) {
            return 65;
         } else if (var1.equals("MessageOrderingEnabled")) {
            return 61;
         } else if (var1.equals("OneWayRmiForReplicationEnabled")) {
            return 62;
         } else if (var1.equals("ReplicationTimeoutEnabled")) {
            return 54;
         } else if (var1.equals("SecureReplicationEnabled")) {
            return 59;
         } else if (var1.equals("SessionLazyDeserializationEnabled")) {
            return 63;
         } else {
            return var1.equals("WeblogicPluginEnabled") ? 20 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         if (this.bean.getDatabaseLessLeasingBasis() != null) {
            var1.add(new ArrayIterator(new DatabaseLessLeasingBasisMBean[]{this.bean.getDatabaseLessLeasingBasis()}));
         }

         if (this.bean.getOverloadProtection() != null) {
            var1.add(new ArrayIterator(new OverloadProtectionMBean[]{this.bean.getOverloadProtection()}));
         }

         return new CombinedIterator(var1);
      }

      protected long computeHashValue(CRC32 var1) {
         try {
            StringBuffer var2 = new StringBuffer();
            long var3 = super.computeHashValue(var1);
            if (var3 != 0L) {
               var2.append(String.valueOf(var3));
            }

            long var5 = 0L;
            if (this.bean.isAdditionalAutoMigrationAttemptsSet()) {
               var2.append("AdditionalAutoMigrationAttempts");
               var2.append(String.valueOf(this.bean.getAdditionalAutoMigrationAttempts()));
            }

            if (this.bean.isAsyncSessionQueueTimeoutSet()) {
               var2.append("AsyncSessionQueueTimeout");
               var2.append(String.valueOf(this.bean.getAsyncSessionQueueTimeout()));
            }

            if (this.bean.isAutoMigrationTableNameSet()) {
               var2.append("AutoMigrationTableName");
               var2.append(String.valueOf(this.bean.getAutoMigrationTableName()));
            }

            if (this.bean.isCandidateMachinesForMigratableServersSet()) {
               var2.append("CandidateMachinesForMigratableServers");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getCandidateMachinesForMigratableServers())));
            }

            if (this.bean.isClusterAddressSet()) {
               var2.append("ClusterAddress");
               var2.append(String.valueOf(this.bean.getClusterAddress()));
            }

            if (this.bean.isClusterBroadcastChannelSet()) {
               var2.append("ClusterBroadcastChannel");
               var2.append(String.valueOf(this.bean.getClusterBroadcastChannel()));
            }

            if (this.bean.isClusterMessagingModeSet()) {
               var2.append("ClusterMessagingMode");
               var2.append(String.valueOf(this.bean.getClusterMessagingMode()));
            }

            if (this.bean.isClusterTypeSet()) {
               var2.append("ClusterType");
               var2.append(String.valueOf(this.bean.getClusterType()));
            }

            if (this.bean.isConsensusParticipantsSet()) {
               var2.append("ConsensusParticipants");
               var2.append(String.valueOf(this.bean.getConsensusParticipants()));
            }

            if (this.bean.isDataSourceForAutomaticMigrationSet()) {
               var2.append("DataSourceForAutomaticMigration");
               var2.append(String.valueOf(this.bean.getDataSourceForAutomaticMigration()));
            }

            if (this.bean.isDataSourceForJobSchedulerSet()) {
               var2.append("DataSourceForJobScheduler");
               var2.append(String.valueOf(this.bean.getDataSourceForJobScheduler()));
            }

            if (this.bean.isDataSourceForSessionPersistenceSet()) {
               var2.append("DataSourceForSessionPersistence");
               var2.append(String.valueOf(this.bean.getDataSourceForSessionPersistence()));
            }

            var5 = this.computeChildHashValue(this.bean.getDatabaseLessLeasingBasis());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isDeathDetectorHeartbeatPeriodSet()) {
               var2.append("DeathDetectorHeartbeatPeriod");
               var2.append(String.valueOf(this.bean.getDeathDetectorHeartbeatPeriod()));
            }

            if (this.bean.isDefaultLoadAlgorithmSet()) {
               var2.append("DefaultLoadAlgorithm");
               var2.append(String.valueOf(this.bean.getDefaultLoadAlgorithm()));
            }

            if (this.bean.isFencingGracePeriodMillisSet()) {
               var2.append("FencingGracePeriodMillis");
               var2.append(String.valueOf(this.bean.getFencingGracePeriodMillis()));
            }

            if (this.bean.isFrontendHTTPPortSet()) {
               var2.append("FrontendHTTPPort");
               var2.append(String.valueOf(this.bean.getFrontendHTTPPort()));
            }

            if (this.bean.isFrontendHTTPSPortSet()) {
               var2.append("FrontendHTTPSPort");
               var2.append(String.valueOf(this.bean.getFrontendHTTPSPort()));
            }

            if (this.bean.isFrontendHostSet()) {
               var2.append("FrontendHost");
               var2.append(String.valueOf(this.bean.getFrontendHost()));
            }

            if (this.bean.isGreedySessionFlushIntervalSet()) {
               var2.append("GreedySessionFlushInterval");
               var2.append(String.valueOf(this.bean.getGreedySessionFlushInterval()));
            }

            if (this.bean.isHTTPPingRetryCountSet()) {
               var2.append("HTTPPingRetryCount");
               var2.append(String.valueOf(this.bean.getHTTPPingRetryCount()));
            }

            if (this.bean.isHealthCheckIntervalMillisSet()) {
               var2.append("HealthCheckIntervalMillis");
               var2.append(String.valueOf(this.bean.getHealthCheckIntervalMillis()));
            }

            if (this.bean.isHealthCheckPeriodsUntilFencingSet()) {
               var2.append("HealthCheckPeriodsUntilFencing");
               var2.append(String.valueOf(this.bean.getHealthCheckPeriodsUntilFencing()));
            }

            if (this.bean.isIdlePeriodsUntilTimeoutSet()) {
               var2.append("IdlePeriodsUntilTimeout");
               var2.append(String.valueOf(this.bean.getIdlePeriodsUntilTimeout()));
            }

            if (this.bean.isInterClusterCommLinkHealthCheckIntervalSet()) {
               var2.append("InterClusterCommLinkHealthCheckInterval");
               var2.append(String.valueOf(this.bean.getInterClusterCommLinkHealthCheckInterval()));
            }

            if (this.bean.isJobSchedulerTableNameSet()) {
               var2.append("JobSchedulerTableName");
               var2.append(String.valueOf(this.bean.getJobSchedulerTableName()));
            }

            if (this.bean.isMaxServerCountForHttpPingSet()) {
               var2.append("MaxServerCountForHttpPing");
               var2.append(String.valueOf(this.bean.getMaxServerCountForHttpPing()));
            }

            if (this.bean.isMemberWarmupTimeoutSecondsSet()) {
               var2.append("MemberWarmupTimeoutSeconds");
               var2.append(String.valueOf(this.bean.getMemberWarmupTimeoutSeconds()));
            }

            if (this.bean.isMigratableTargetsSet()) {
               var2.append("MigratableTargets");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getMigratableTargets())));
            }

            if (this.bean.isMigrationBasisSet()) {
               var2.append("MigrationBasis");
               var2.append(String.valueOf(this.bean.getMigrationBasis()));
            }

            if (this.bean.isMillisToSleepBetweenAutoMigrationAttemptsSet()) {
               var2.append("MillisToSleepBetweenAutoMigrationAttempts");
               var2.append(String.valueOf(this.bean.getMillisToSleepBetweenAutoMigrationAttempts()));
            }

            if (this.bean.isMulticastAddressSet()) {
               var2.append("MulticastAddress");
               var2.append(String.valueOf(this.bean.getMulticastAddress()));
            }

            if (this.bean.isMulticastBufferSizeSet()) {
               var2.append("MulticastBufferSize");
               var2.append(String.valueOf(this.bean.getMulticastBufferSize()));
            }

            if (this.bean.isMulticastDataEncryptionSet()) {
               var2.append("MulticastDataEncryption");
               var2.append(String.valueOf(this.bean.getMulticastDataEncryption()));
            }

            if (this.bean.isMulticastPortSet()) {
               var2.append("MulticastPort");
               var2.append(String.valueOf(this.bean.getMulticastPort()));
            }

            if (this.bean.isMulticastSendDelaySet()) {
               var2.append("MulticastSendDelay");
               var2.append(String.valueOf(this.bean.getMulticastSendDelay()));
            }

            if (this.bean.isMulticastTTLSet()) {
               var2.append("MulticastTTL");
               var2.append(String.valueOf(this.bean.getMulticastTTL()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isNumberOfServersInClusterAddressSet()) {
               var2.append("NumberOfServersInClusterAddress");
               var2.append(String.valueOf(this.bean.getNumberOfServersInClusterAddress()));
            }

            var5 = this.computeChildHashValue(this.bean.getOverloadProtection());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isPersistSessionsOnShutdownSet()) {
               var2.append("PersistSessionsOnShutdown");
               var2.append(String.valueOf(this.bean.getPersistSessionsOnShutdown()));
            }

            if (this.bean.isRemoteClusterAddressSet()) {
               var2.append("RemoteClusterAddress");
               var2.append(String.valueOf(this.bean.getRemoteClusterAddress()));
            }

            if (this.bean.isReplicationChannelSet()) {
               var2.append("ReplicationChannel");
               var2.append(String.valueOf(this.bean.getReplicationChannel()));
            }

            if (this.bean.isServerNamesSet()) {
               var2.append("ServerNames");
               var2.append(String.valueOf(this.bean.getServerNames()));
            }

            if (this.bean.isServersSet()) {
               var2.append("Servers");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getServers())));
            }

            if (this.bean.isServiceAgeThresholdSecondsSet()) {
               var2.append("ServiceAgeThresholdSeconds");
               var2.append(String.valueOf(this.bean.getServiceAgeThresholdSeconds()));
            }

            if (this.bean.isSessionFlushIntervalSet()) {
               var2.append("SessionFlushInterval");
               var2.append(String.valueOf(this.bean.getSessionFlushInterval()));
            }

            if (this.bean.isSessionFlushThresholdSet()) {
               var2.append("SessionFlushThreshold");
               var2.append(String.valueOf(this.bean.getSessionFlushThreshold()));
            }

            if (this.bean.isSingletonSQLQueryHelperSet()) {
               var2.append("SingletonSQLQueryHelper");
               var2.append(String.valueOf(this.bean.getSingletonSQLQueryHelper()));
            }

            if (this.bean.isUnicastDiscoveryPeriodMillisSet()) {
               var2.append("UnicastDiscoveryPeriodMillis");
               var2.append(String.valueOf(this.bean.getUnicastDiscoveryPeriodMillis()));
            }

            if (this.bean.isWANSessionPersistenceTableNameSet()) {
               var2.append("WANSessionPersistenceTableName");
               var2.append(String.valueOf(this.bean.getWANSessionPersistenceTableName()));
            }

            if (this.bean.isClientCertProxyEnabledSet()) {
               var2.append("ClientCertProxyEnabled");
               var2.append(String.valueOf(this.bean.isClientCertProxyEnabled()));
            }

            if (this.bean.isHttpTraceSupportEnabledSet()) {
               var2.append("HttpTraceSupportEnabled");
               var2.append(String.valueOf(this.bean.isHttpTraceSupportEnabled()));
            }

            if (this.bean.isMemberDeathDetectorEnabledSet()) {
               var2.append("MemberDeathDetectorEnabled");
               var2.append(String.valueOf(this.bean.isMemberDeathDetectorEnabled()));
            }

            if (this.bean.isMessageOrderingEnabledSet()) {
               var2.append("MessageOrderingEnabled");
               var2.append(String.valueOf(this.bean.isMessageOrderingEnabled()));
            }

            if (this.bean.isOneWayRmiForReplicationEnabledSet()) {
               var2.append("OneWayRmiForReplicationEnabled");
               var2.append(String.valueOf(this.bean.isOneWayRmiForReplicationEnabled()));
            }

            if (this.bean.isReplicationTimeoutEnabledSet()) {
               var2.append("ReplicationTimeoutEnabled");
               var2.append(String.valueOf(this.bean.isReplicationTimeoutEnabled()));
            }

            if (this.bean.isSecureReplicationEnabledSet()) {
               var2.append("SecureReplicationEnabled");
               var2.append(String.valueOf(this.bean.isSecureReplicationEnabled()));
            }

            if (this.bean.isSessionLazyDeserializationEnabledSet()) {
               var2.append("SessionLazyDeserializationEnabled");
               var2.append(String.valueOf(this.bean.isSessionLazyDeserializationEnabled()));
            }

            if (this.bean.isWeblogicPluginEnabledSet()) {
               var2.append("WeblogicPluginEnabled");
               var2.append(String.valueOf(this.bean.isWeblogicPluginEnabled()));
            }

            var1.update(var2.toString().getBytes());
            return var1.getValue();
         } catch (Exception var7) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var7);
         }
      }

      protected void computeDiff(AbstractDescriptorBean var1) {
         try {
            super.computeDiff(var1);
            ClusterMBeanImpl var2 = (ClusterMBeanImpl)var1;
            this.computeDiff("AdditionalAutoMigrationAttempts", this.bean.getAdditionalAutoMigrationAttempts(), var2.getAdditionalAutoMigrationAttempts(), false);
            this.computeDiff("AsyncSessionQueueTimeout", this.bean.getAsyncSessionQueueTimeout(), var2.getAsyncSessionQueueTimeout(), true);
            this.computeDiff("AutoMigrationTableName", this.bean.getAutoMigrationTableName(), var2.getAutoMigrationTableName(), false);
            this.computeDiff("CandidateMachinesForMigratableServers", this.bean.getCandidateMachinesForMigratableServers(), var2.getCandidateMachinesForMigratableServers(), false);
            this.computeDiff("ClusterAddress", this.bean.getClusterAddress(), var2.getClusterAddress(), false);
            this.computeDiff("ClusterBroadcastChannel", this.bean.getClusterBroadcastChannel(), var2.getClusterBroadcastChannel(), false);
            this.computeDiff("ClusterMessagingMode", this.bean.getClusterMessagingMode(), var2.getClusterMessagingMode(), true);
            this.computeDiff("ClusterType", this.bean.getClusterType(), var2.getClusterType(), false);
            this.computeDiff("ConsensusParticipants", this.bean.getConsensusParticipants(), var2.getConsensusParticipants(), false);
            this.computeDiff("DataSourceForAutomaticMigration", this.bean.getDataSourceForAutomaticMigration(), var2.getDataSourceForAutomaticMigration(), false);
            this.computeDiff("DataSourceForJobScheduler", this.bean.getDataSourceForJobScheduler(), var2.getDataSourceForJobScheduler(), false);
            this.computeDiff("DataSourceForSessionPersistence", this.bean.getDataSourceForSessionPersistence(), var2.getDataSourceForSessionPersistence(), false);
            this.computeSubDiff("DatabaseLessLeasingBasis", this.bean.getDatabaseLessLeasingBasis(), var2.getDatabaseLessLeasingBasis());
            this.computeDiff("DeathDetectorHeartbeatPeriod", this.bean.getDeathDetectorHeartbeatPeriod(), var2.getDeathDetectorHeartbeatPeriod(), false);
            this.computeDiff("DefaultLoadAlgorithm", this.bean.getDefaultLoadAlgorithm(), var2.getDefaultLoadAlgorithm(), true);
            this.computeDiff("FencingGracePeriodMillis", this.bean.getFencingGracePeriodMillis(), var2.getFencingGracePeriodMillis(), false);
            this.computeDiff("FrontendHTTPPort", this.bean.getFrontendHTTPPort(), var2.getFrontendHTTPPort(), false);
            this.computeDiff("FrontendHTTPSPort", this.bean.getFrontendHTTPSPort(), var2.getFrontendHTTPSPort(), false);
            this.computeDiff("FrontendHost", this.bean.getFrontendHost(), var2.getFrontendHost(), false);
            this.computeDiff("GreedySessionFlushInterval", this.bean.getGreedySessionFlushInterval(), var2.getGreedySessionFlushInterval(), true);
            this.computeDiff("HTTPPingRetryCount", this.bean.getHTTPPingRetryCount(), var2.getHTTPPingRetryCount(), true);
            this.computeDiff("HealthCheckIntervalMillis", this.bean.getHealthCheckIntervalMillis(), var2.getHealthCheckIntervalMillis(), false);
            this.computeDiff("HealthCheckPeriodsUntilFencing", this.bean.getHealthCheckPeriodsUntilFencing(), var2.getHealthCheckPeriodsUntilFencing(), false);
            this.computeDiff("IdlePeriodsUntilTimeout", this.bean.getIdlePeriodsUntilTimeout(), var2.getIdlePeriodsUntilTimeout(), false);
            this.computeDiff("InterClusterCommLinkHealthCheckInterval", this.bean.getInterClusterCommLinkHealthCheckInterval(), var2.getInterClusterCommLinkHealthCheckInterval(), true);
            this.computeDiff("JobSchedulerTableName", this.bean.getJobSchedulerTableName(), var2.getJobSchedulerTableName(), false);
            this.computeDiff("MaxServerCountForHttpPing", this.bean.getMaxServerCountForHttpPing(), var2.getMaxServerCountForHttpPing(), true);
            this.computeDiff("MemberWarmupTimeoutSeconds", this.bean.getMemberWarmupTimeoutSeconds(), var2.getMemberWarmupTimeoutSeconds(), false);
            this.computeDiff("MigrationBasis", this.bean.getMigrationBasis(), var2.getMigrationBasis(), false);
            this.computeDiff("MillisToSleepBetweenAutoMigrationAttempts", this.bean.getMillisToSleepBetweenAutoMigrationAttempts(), var2.getMillisToSleepBetweenAutoMigrationAttempts(), false);
            this.computeDiff("MulticastAddress", this.bean.getMulticastAddress(), var2.getMulticastAddress(), false);
            this.computeDiff("MulticastBufferSize", this.bean.getMulticastBufferSize(), var2.getMulticastBufferSize(), false);
            this.computeDiff("MulticastDataEncryption", this.bean.getMulticastDataEncryption(), var2.getMulticastDataEncryption(), false);
            this.computeDiff("MulticastPort", this.bean.getMulticastPort(), var2.getMulticastPort(), false);
            this.computeDiff("MulticastSendDelay", this.bean.getMulticastSendDelay(), var2.getMulticastSendDelay(), false);
            this.computeDiff("MulticastTTL", this.bean.getMulticastTTL(), var2.getMulticastTTL(), false);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("NumberOfServersInClusterAddress", this.bean.getNumberOfServersInClusterAddress(), var2.getNumberOfServersInClusterAddress(), false);
            this.computeSubDiff("OverloadProtection", this.bean.getOverloadProtection(), var2.getOverloadProtection());
            this.computeDiff("PersistSessionsOnShutdown", this.bean.getPersistSessionsOnShutdown(), var2.getPersistSessionsOnShutdown(), false);
            this.computeDiff("RemoteClusterAddress", this.bean.getRemoteClusterAddress(), var2.getRemoteClusterAddress(), false);
            this.computeDiff("ReplicationChannel", this.bean.getReplicationChannel(), var2.getReplicationChannel(), false);
            this.computeDiff("ServiceAgeThresholdSeconds", this.bean.getServiceAgeThresholdSeconds(), var2.getServiceAgeThresholdSeconds(), true);
            this.computeDiff("SessionFlushInterval", this.bean.getSessionFlushInterval(), var2.getSessionFlushInterval(), true);
            this.computeDiff("SessionFlushThreshold", this.bean.getSessionFlushThreshold(), var2.getSessionFlushThreshold(), true);
            this.computeDiff("SingletonSQLQueryHelper", this.bean.getSingletonSQLQueryHelper(), var2.getSingletonSQLQueryHelper(), false);
            this.computeDiff("UnicastDiscoveryPeriodMillis", this.bean.getUnicastDiscoveryPeriodMillis(), var2.getUnicastDiscoveryPeriodMillis(), true);
            this.computeDiff("WANSessionPersistenceTableName", this.bean.getWANSessionPersistenceTableName(), var2.getWANSessionPersistenceTableName(), false);
            this.computeDiff("ClientCertProxyEnabled", this.bean.isClientCertProxyEnabled(), var2.isClientCertProxyEnabled(), false);
            this.computeDiff("HttpTraceSupportEnabled", this.bean.isHttpTraceSupportEnabled(), var2.isHttpTraceSupportEnabled(), false);
            this.computeDiff("MemberDeathDetectorEnabled", this.bean.isMemberDeathDetectorEnabled(), var2.isMemberDeathDetectorEnabled(), false);
            this.computeDiff("MessageOrderingEnabled", this.bean.isMessageOrderingEnabled(), var2.isMessageOrderingEnabled(), false);
            this.computeDiff("OneWayRmiForReplicationEnabled", this.bean.isOneWayRmiForReplicationEnabled(), var2.isOneWayRmiForReplicationEnabled(), false);
            this.computeDiff("ReplicationTimeoutEnabled", this.bean.isReplicationTimeoutEnabled(), var2.isReplicationTimeoutEnabled(), false);
            this.computeDiff("SecureReplicationEnabled", this.bean.isSecureReplicationEnabled(), var2.isSecureReplicationEnabled(), false);
            this.computeDiff("SessionLazyDeserializationEnabled", this.bean.isSessionLazyDeserializationEnabled(), var2.isSessionLazyDeserializationEnabled(), false);
            this.computeDiff("WeblogicPluginEnabled", this.bean.isWeblogicPluginEnabled(), var2.isWeblogicPluginEnabled(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            ClusterMBeanImpl var3 = (ClusterMBeanImpl)var1.getSourceBean();
            ClusterMBeanImpl var4 = (ClusterMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("AdditionalAutoMigrationAttempts")) {
                  var3.setAdditionalAutoMigrationAttempts(var4.getAdditionalAutoMigrationAttempts());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 50);
               } else if (var5.equals("AsyncSessionQueueTimeout")) {
                  var3.setAsyncSessionQueueTimeout(var4.getAsyncSessionQueueTimeout());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 36);
               } else if (var5.equals("AutoMigrationTableName")) {
                  var3.setAutoMigrationTableName(var4.getAutoMigrationTableName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 49);
               } else if (var5.equals("CandidateMachinesForMigratableServers")) {
                  var3.setCandidateMachinesForMigratableServersAsString(var4.getCandidateMachinesForMigratableServersAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 40);
               } else if (var5.equals("ClusterAddress")) {
                  var3.setClusterAddress(var4.getClusterAddress());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("ClusterBroadcastChannel")) {
                  var3.setClusterBroadcastChannel(var4.getClusterBroadcastChannel());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 17);
               } else if (var5.equals("ClusterMessagingMode")) {
                  var3.setClusterMessagingMode(var4.getClusterMessagingMode());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 16);
               } else if (var5.equals("ClusterType")) {
                  var3.setClusterType(var4.getClusterType());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 47);
               } else if (var5.equals("ConsensusParticipants")) {
                  var3.setConsensusParticipants(var4.getConsensusParticipants());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 53);
               } else if (var5.equals("DataSourceForAutomaticMigration")) {
                  var3.setDataSourceForAutomaticMigrationAsString(var4.getDataSourceForAutomaticMigrationAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 41);
               } else if (var5.equals("DataSourceForJobScheduler")) {
                  var3.setDataSourceForJobSchedulerAsString(var4.getDataSourceForJobSchedulerAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 33);
               } else if (var5.equals("DataSourceForSessionPersistence")) {
                  var3.setDataSourceForSessionPersistenceAsString(var4.getDataSourceForSessionPersistenceAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 32);
               } else if (var5.equals("DatabaseLessLeasingBasis")) {
                  if (var6 == 2) {
                     var3.setDatabaseLessLeasingBasis((DatabaseLessLeasingBasisMBean)this.createCopy((AbstractDescriptorBean)var4.getDatabaseLessLeasingBasis()));
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3._destroySingleton("DatabaseLessLeasingBasis", var3.getDatabaseLessLeasingBasis());
                  }

                  var3._conditionalUnset(var2.isUnsetUpdate(), 56);
               } else if (var5.equals("DeathDetectorHeartbeatPeriod")) {
                  var3.setDeathDetectorHeartbeatPeriod(var4.getDeathDetectorHeartbeatPeriod());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 64);
               } else if (var5.equals("DefaultLoadAlgorithm")) {
                  var3.setDefaultLoadAlgorithm(var4.getDefaultLoadAlgorithm());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 15);
               } else if (var5.equals("FencingGracePeriodMillis")) {
                  var3.setFencingGracePeriodMillis(var4.getFencingGracePeriodMillis());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 44);
               } else if (var5.equals("FrontendHTTPPort")) {
                  var3.setFrontendHTTPPort(var4.getFrontendHTTPPort());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 25);
               } else if (var5.equals("FrontendHTTPSPort")) {
                  var3.setFrontendHTTPSPort(var4.getFrontendHTTPSPort());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 26);
               } else if (var5.equals("FrontendHost")) {
                  var3.setFrontendHost(var4.getFrontendHost());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 24);
               } else if (var5.equals("GreedySessionFlushInterval")) {
                  var3.setGreedySessionFlushInterval(var4.getGreedySessionFlushInterval());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 37);
               } else if (var5.equals("HTTPPingRetryCount")) {
                  var3.setHTTPPingRetryCount(var4.getHTTPPingRetryCount());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 57);
               } else if (var5.equals("HealthCheckIntervalMillis")) {
                  var3.setHealthCheckIntervalMillis(var4.getHealthCheckIntervalMillis());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 42);
               } else if (var5.equals("HealthCheckPeriodsUntilFencing")) {
                  var3.setHealthCheckPeriodsUntilFencing(var4.getHealthCheckPeriodsUntilFencing());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 43);
               } else if (var5.equals("IdlePeriodsUntilTimeout")) {
                  var3.setIdlePeriodsUntilTimeout(var4.getIdlePeriodsUntilTimeout());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 27);
               } else if (var5.equals("InterClusterCommLinkHealthCheckInterval")) {
                  var3.setInterClusterCommLinkHealthCheckInterval(var4.getInterClusterCommLinkHealthCheckInterval());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 31);
               } else if (var5.equals("JobSchedulerTableName")) {
                  var3.setJobSchedulerTableName(var4.getJobSchedulerTableName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 34);
               } else if (var5.equals("MaxServerCountForHttpPing")) {
                  var3.setMaxServerCountForHttpPing(var4.getMaxServerCountForHttpPing());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 58);
               } else if (var5.equals("MemberWarmupTimeoutSeconds")) {
                  var3.setMemberWarmupTimeoutSeconds(var4.getMemberWarmupTimeoutSeconds());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 22);
               } else if (!var5.equals("MigratableTargets")) {
                  if (var5.equals("MigrationBasis")) {
                     var3.setMigrationBasis(var4.getMigrationBasis());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 52);
                  } else if (var5.equals("MillisToSleepBetweenAutoMigrationAttempts")) {
                     var3.setMillisToSleepBetweenAutoMigrationAttempts(var4.getMillisToSleepBetweenAutoMigrationAttempts());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 51);
                  } else if (var5.equals("MulticastAddress")) {
                     var3.setMulticastAddress(var4.getMulticastAddress());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 10);
                  } else if (var5.equals("MulticastBufferSize")) {
                     var3.setMulticastBufferSize(var4.getMulticastBufferSize());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 11);
                  } else if (var5.equals("MulticastDataEncryption")) {
                     var3.setMulticastDataEncryption(var4.getMulticastDataEncryption());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 48);
                  } else if (var5.equals("MulticastPort")) {
                     var3.setMulticastPort(var4.getMulticastPort());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 12);
                  } else if (var5.equals("MulticastSendDelay")) {
                     var3.setMulticastSendDelay(var4.getMulticastSendDelay());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 14);
                  } else if (var5.equals("MulticastTTL")) {
                     var3.setMulticastTTL(var4.getMulticastTTL());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 13);
                  } else if (var5.equals("Name")) {
                     var3.setName(var4.getName());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 2);
                  } else if (var5.equals("NumberOfServersInClusterAddress")) {
                     var3.setNumberOfServersInClusterAddress(var4.getNumberOfServersInClusterAddress());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 46);
                  } else if (var5.equals("OverloadProtection")) {
                     if (var6 == 2) {
                        var3.setOverloadProtection((OverloadProtectionMBean)this.createCopy((AbstractDescriptorBean)var4.getOverloadProtection()));
                     } else {
                        if (var6 != 3) {
                           throw new AssertionError("Invalid type: " + var6);
                        }

                        var3._destroySingleton("OverloadProtection", var3.getOverloadProtection());
                     }

                     var3._conditionalUnset(var2.isUnsetUpdate(), 55);
                  } else if (var5.equals("PersistSessionsOnShutdown")) {
                     var3.setPersistSessionsOnShutdown(var4.getPersistSessionsOnShutdown());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 35);
                  } else if (var5.equals("RemoteClusterAddress")) {
                     var3.setRemoteClusterAddress(var4.getRemoteClusterAddress());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 28);
                  } else if (var5.equals("ReplicationChannel")) {
                     var3.setReplicationChannel(var4.getReplicationChannel());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 30);
                  } else if (!var5.equals("ServerNames") && !var5.equals("Servers")) {
                     if (var5.equals("ServiceAgeThresholdSeconds")) {
                        var3.setServiceAgeThresholdSeconds(var4.getServiceAgeThresholdSeconds());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 18);
                     } else if (var5.equals("SessionFlushInterval")) {
                        var3.setSessionFlushInterval(var4.getSessionFlushInterval());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 38);
                     } else if (var5.equals("SessionFlushThreshold")) {
                        var3.setSessionFlushThreshold(var4.getSessionFlushThreshold());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 39);
                     } else if (var5.equals("SingletonSQLQueryHelper")) {
                        var3.setSingletonSQLQueryHelper(var4.getSingletonSQLQueryHelper());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 45);
                     } else if (var5.equals("UnicastDiscoveryPeriodMillis")) {
                        var3.setUnicastDiscoveryPeriodMillis(var4.getUnicastDiscoveryPeriodMillis());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 60);
                     } else if (var5.equals("WANSessionPersistenceTableName")) {
                        var3.setWANSessionPersistenceTableName(var4.getWANSessionPersistenceTableName());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 29);
                     } else if (var5.equals("ClientCertProxyEnabled")) {
                        var3.setClientCertProxyEnabled(var4.isClientCertProxyEnabled());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 19);
                     } else if (var5.equals("HttpTraceSupportEnabled")) {
                        var3.setHttpTraceSupportEnabled(var4.isHttpTraceSupportEnabled());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 23);
                     } else if (var5.equals("MemberDeathDetectorEnabled")) {
                        var3.setMemberDeathDetectorEnabled(var4.isMemberDeathDetectorEnabled());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 65);
                     } else if (var5.equals("MessageOrderingEnabled")) {
                        var3.setMessageOrderingEnabled(var4.isMessageOrderingEnabled());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 61);
                     } else if (var5.equals("OneWayRmiForReplicationEnabled")) {
                        var3.setOneWayRmiForReplicationEnabled(var4.isOneWayRmiForReplicationEnabled());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 62);
                     } else if (var5.equals("ReplicationTimeoutEnabled")) {
                        var3.setReplicationTimeoutEnabled(var4.isReplicationTimeoutEnabled());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 54);
                     } else if (var5.equals("SecureReplicationEnabled")) {
                        var3.setSecureReplicationEnabled(var4.isSecureReplicationEnabled());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 59);
                     } else if (var5.equals("SessionLazyDeserializationEnabled")) {
                        var3.setSessionLazyDeserializationEnabled(var4.isSessionLazyDeserializationEnabled());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 63);
                     } else if (var5.equals("WeblogicPluginEnabled")) {
                        var3.setWeblogicPluginEnabled(var4.isWeblogicPluginEnabled());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 20);
                     } else {
                        super.applyPropertyUpdate(var1, var2);
                     }
                  }
               }

            }
         } catch (RuntimeException var7) {
            throw var7;
         } catch (Exception var8) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var8);
         }
      }

      protected AbstractDescriptorBean finishCopy(AbstractDescriptorBean var1, boolean var2, List var3) {
         try {
            ClusterMBeanImpl var5 = (ClusterMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("AdditionalAutoMigrationAttempts")) && this.bean.isAdditionalAutoMigrationAttemptsSet()) {
               var5.setAdditionalAutoMigrationAttempts(this.bean.getAdditionalAutoMigrationAttempts());
            }

            if ((var3 == null || !var3.contains("AsyncSessionQueueTimeout")) && this.bean.isAsyncSessionQueueTimeoutSet()) {
               var5.setAsyncSessionQueueTimeout(this.bean.getAsyncSessionQueueTimeout());
            }

            if ((var3 == null || !var3.contains("AutoMigrationTableName")) && this.bean.isAutoMigrationTableNameSet()) {
               var5.setAutoMigrationTableName(this.bean.getAutoMigrationTableName());
            }

            if ((var3 == null || !var3.contains("CandidateMachinesForMigratableServers")) && this.bean.isCandidateMachinesForMigratableServersSet()) {
               var5._unSet(var5, 40);
               var5.setCandidateMachinesForMigratableServersAsString(this.bean.getCandidateMachinesForMigratableServersAsString());
            }

            if ((var3 == null || !var3.contains("ClusterAddress")) && this.bean.isClusterAddressSet()) {
               var5.setClusterAddress(this.bean.getClusterAddress());
            }

            if ((var3 == null || !var3.contains("ClusterBroadcastChannel")) && this.bean.isClusterBroadcastChannelSet()) {
               var5.setClusterBroadcastChannel(this.bean.getClusterBroadcastChannel());
            }

            if ((var3 == null || !var3.contains("ClusterMessagingMode")) && this.bean.isClusterMessagingModeSet()) {
               var5.setClusterMessagingMode(this.bean.getClusterMessagingMode());
            }

            if ((var3 == null || !var3.contains("ClusterType")) && this.bean.isClusterTypeSet()) {
               var5.setClusterType(this.bean.getClusterType());
            }

            if ((var3 == null || !var3.contains("ConsensusParticipants")) && this.bean.isConsensusParticipantsSet()) {
               var5.setConsensusParticipants(this.bean.getConsensusParticipants());
            }

            if ((var3 == null || !var3.contains("DataSourceForAutomaticMigration")) && this.bean.isDataSourceForAutomaticMigrationSet()) {
               var5._unSet(var5, 41);
               var5.setDataSourceForAutomaticMigrationAsString(this.bean.getDataSourceForAutomaticMigrationAsString());
            }

            if ((var3 == null || !var3.contains("DataSourceForJobScheduler")) && this.bean.isDataSourceForJobSchedulerSet()) {
               var5._unSet(var5, 33);
               var5.setDataSourceForJobSchedulerAsString(this.bean.getDataSourceForJobSchedulerAsString());
            }

            if ((var3 == null || !var3.contains("DataSourceForSessionPersistence")) && this.bean.isDataSourceForSessionPersistenceSet()) {
               var5._unSet(var5, 32);
               var5.setDataSourceForSessionPersistenceAsString(this.bean.getDataSourceForSessionPersistenceAsString());
            }

            if ((var3 == null || !var3.contains("DatabaseLessLeasingBasis")) && this.bean.isDatabaseLessLeasingBasisSet() && !var5._isSet(56)) {
               DatabaseLessLeasingBasisMBean var4 = this.bean.getDatabaseLessLeasingBasis();
               var5.setDatabaseLessLeasingBasis((DatabaseLessLeasingBasisMBean)null);
               var5.setDatabaseLessLeasingBasis(var4 == null ? null : (DatabaseLessLeasingBasisMBean)this.createCopy((AbstractDescriptorBean)var4, var2));
            }

            if ((var3 == null || !var3.contains("DeathDetectorHeartbeatPeriod")) && this.bean.isDeathDetectorHeartbeatPeriodSet()) {
               var5.setDeathDetectorHeartbeatPeriod(this.bean.getDeathDetectorHeartbeatPeriod());
            }

            if ((var3 == null || !var3.contains("DefaultLoadAlgorithm")) && this.bean.isDefaultLoadAlgorithmSet()) {
               var5.setDefaultLoadAlgorithm(this.bean.getDefaultLoadAlgorithm());
            }

            if ((var3 == null || !var3.contains("FencingGracePeriodMillis")) && this.bean.isFencingGracePeriodMillisSet()) {
               var5.setFencingGracePeriodMillis(this.bean.getFencingGracePeriodMillis());
            }

            if ((var3 == null || !var3.contains("FrontendHTTPPort")) && this.bean.isFrontendHTTPPortSet()) {
               var5.setFrontendHTTPPort(this.bean.getFrontendHTTPPort());
            }

            if ((var3 == null || !var3.contains("FrontendHTTPSPort")) && this.bean.isFrontendHTTPSPortSet()) {
               var5.setFrontendHTTPSPort(this.bean.getFrontendHTTPSPort());
            }

            if ((var3 == null || !var3.contains("FrontendHost")) && this.bean.isFrontendHostSet()) {
               var5.setFrontendHost(this.bean.getFrontendHost());
            }

            if ((var3 == null || !var3.contains("GreedySessionFlushInterval")) && this.bean.isGreedySessionFlushIntervalSet()) {
               var5.setGreedySessionFlushInterval(this.bean.getGreedySessionFlushInterval());
            }

            if ((var3 == null || !var3.contains("HTTPPingRetryCount")) && this.bean.isHTTPPingRetryCountSet()) {
               var5.setHTTPPingRetryCount(this.bean.getHTTPPingRetryCount());
            }

            if ((var3 == null || !var3.contains("HealthCheckIntervalMillis")) && this.bean.isHealthCheckIntervalMillisSet()) {
               var5.setHealthCheckIntervalMillis(this.bean.getHealthCheckIntervalMillis());
            }

            if ((var3 == null || !var3.contains("HealthCheckPeriodsUntilFencing")) && this.bean.isHealthCheckPeriodsUntilFencingSet()) {
               var5.setHealthCheckPeriodsUntilFencing(this.bean.getHealthCheckPeriodsUntilFencing());
            }

            if ((var3 == null || !var3.contains("IdlePeriodsUntilTimeout")) && this.bean.isIdlePeriodsUntilTimeoutSet()) {
               var5.setIdlePeriodsUntilTimeout(this.bean.getIdlePeriodsUntilTimeout());
            }

            if ((var3 == null || !var3.contains("InterClusterCommLinkHealthCheckInterval")) && this.bean.isInterClusterCommLinkHealthCheckIntervalSet()) {
               var5.setInterClusterCommLinkHealthCheckInterval(this.bean.getInterClusterCommLinkHealthCheckInterval());
            }

            if ((var3 == null || !var3.contains("JobSchedulerTableName")) && this.bean.isJobSchedulerTableNameSet()) {
               var5.setJobSchedulerTableName(this.bean.getJobSchedulerTableName());
            }

            if ((var3 == null || !var3.contains("MaxServerCountForHttpPing")) && this.bean.isMaxServerCountForHttpPingSet()) {
               var5.setMaxServerCountForHttpPing(this.bean.getMaxServerCountForHttpPing());
            }

            if ((var3 == null || !var3.contains("MemberWarmupTimeoutSeconds")) && this.bean.isMemberWarmupTimeoutSecondsSet()) {
               var5.setMemberWarmupTimeoutSeconds(this.bean.getMemberWarmupTimeoutSeconds());
            }

            if ((var3 == null || !var3.contains("MigrationBasis")) && this.bean.isMigrationBasisSet()) {
               var5.setMigrationBasis(this.bean.getMigrationBasis());
            }

            if ((var3 == null || !var3.contains("MillisToSleepBetweenAutoMigrationAttempts")) && this.bean.isMillisToSleepBetweenAutoMigrationAttemptsSet()) {
               var5.setMillisToSleepBetweenAutoMigrationAttempts(this.bean.getMillisToSleepBetweenAutoMigrationAttempts());
            }

            if ((var3 == null || !var3.contains("MulticastAddress")) && this.bean.isMulticastAddressSet()) {
               var5.setMulticastAddress(this.bean.getMulticastAddress());
            }

            if ((var3 == null || !var3.contains("MulticastBufferSize")) && this.bean.isMulticastBufferSizeSet()) {
               var5.setMulticastBufferSize(this.bean.getMulticastBufferSize());
            }

            if ((var3 == null || !var3.contains("MulticastDataEncryption")) && this.bean.isMulticastDataEncryptionSet()) {
               var5.setMulticastDataEncryption(this.bean.getMulticastDataEncryption());
            }

            if ((var3 == null || !var3.contains("MulticastPort")) && this.bean.isMulticastPortSet()) {
               var5.setMulticastPort(this.bean.getMulticastPort());
            }

            if ((var3 == null || !var3.contains("MulticastSendDelay")) && this.bean.isMulticastSendDelaySet()) {
               var5.setMulticastSendDelay(this.bean.getMulticastSendDelay());
            }

            if ((var3 == null || !var3.contains("MulticastTTL")) && this.bean.isMulticastTTLSet()) {
               var5.setMulticastTTL(this.bean.getMulticastTTL());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("NumberOfServersInClusterAddress")) && this.bean.isNumberOfServersInClusterAddressSet()) {
               var5.setNumberOfServersInClusterAddress(this.bean.getNumberOfServersInClusterAddress());
            }

            if ((var3 == null || !var3.contains("OverloadProtection")) && this.bean.isOverloadProtectionSet() && !var5._isSet(55)) {
               OverloadProtectionMBean var8 = this.bean.getOverloadProtection();
               var5.setOverloadProtection((OverloadProtectionMBean)null);
               var5.setOverloadProtection(var8 == null ? null : (OverloadProtectionMBean)this.createCopy((AbstractDescriptorBean)var8, var2));
            }

            if ((var3 == null || !var3.contains("PersistSessionsOnShutdown")) && this.bean.isPersistSessionsOnShutdownSet()) {
               var5.setPersistSessionsOnShutdown(this.bean.getPersistSessionsOnShutdown());
            }

            if ((var3 == null || !var3.contains("RemoteClusterAddress")) && this.bean.isRemoteClusterAddressSet()) {
               var5.setRemoteClusterAddress(this.bean.getRemoteClusterAddress());
            }

            if ((var3 == null || !var3.contains("ReplicationChannel")) && this.bean.isReplicationChannelSet()) {
               var5.setReplicationChannel(this.bean.getReplicationChannel());
            }

            if ((var3 == null || !var3.contains("ServiceAgeThresholdSeconds")) && this.bean.isServiceAgeThresholdSecondsSet()) {
               var5.setServiceAgeThresholdSeconds(this.bean.getServiceAgeThresholdSeconds());
            }

            if ((var3 == null || !var3.contains("SessionFlushInterval")) && this.bean.isSessionFlushIntervalSet()) {
               var5.setSessionFlushInterval(this.bean.getSessionFlushInterval());
            }

            if ((var3 == null || !var3.contains("SessionFlushThreshold")) && this.bean.isSessionFlushThresholdSet()) {
               var5.setSessionFlushThreshold(this.bean.getSessionFlushThreshold());
            }

            if ((var3 == null || !var3.contains("SingletonSQLQueryHelper")) && this.bean.isSingletonSQLQueryHelperSet()) {
               var5.setSingletonSQLQueryHelper(this.bean.getSingletonSQLQueryHelper());
            }

            if ((var3 == null || !var3.contains("UnicastDiscoveryPeriodMillis")) && this.bean.isUnicastDiscoveryPeriodMillisSet()) {
               var5.setUnicastDiscoveryPeriodMillis(this.bean.getUnicastDiscoveryPeriodMillis());
            }

            if ((var3 == null || !var3.contains("WANSessionPersistenceTableName")) && this.bean.isWANSessionPersistenceTableNameSet()) {
               var5.setWANSessionPersistenceTableName(this.bean.getWANSessionPersistenceTableName());
            }

            if ((var3 == null || !var3.contains("ClientCertProxyEnabled")) && this.bean.isClientCertProxyEnabledSet()) {
               var5.setClientCertProxyEnabled(this.bean.isClientCertProxyEnabled());
            }

            if ((var3 == null || !var3.contains("HttpTraceSupportEnabled")) && this.bean.isHttpTraceSupportEnabledSet()) {
               var5.setHttpTraceSupportEnabled(this.bean.isHttpTraceSupportEnabled());
            }

            if ((var3 == null || !var3.contains("MemberDeathDetectorEnabled")) && this.bean.isMemberDeathDetectorEnabledSet()) {
               var5.setMemberDeathDetectorEnabled(this.bean.isMemberDeathDetectorEnabled());
            }

            if ((var3 == null || !var3.contains("MessageOrderingEnabled")) && this.bean.isMessageOrderingEnabledSet()) {
               var5.setMessageOrderingEnabled(this.bean.isMessageOrderingEnabled());
            }

            if ((var3 == null || !var3.contains("OneWayRmiForReplicationEnabled")) && this.bean.isOneWayRmiForReplicationEnabledSet()) {
               var5.setOneWayRmiForReplicationEnabled(this.bean.isOneWayRmiForReplicationEnabled());
            }

            if ((var3 == null || !var3.contains("ReplicationTimeoutEnabled")) && this.bean.isReplicationTimeoutEnabledSet()) {
               var5.setReplicationTimeoutEnabled(this.bean.isReplicationTimeoutEnabled());
            }

            if ((var3 == null || !var3.contains("SecureReplicationEnabled")) && this.bean.isSecureReplicationEnabledSet()) {
               var5.setSecureReplicationEnabled(this.bean.isSecureReplicationEnabled());
            }

            if ((var3 == null || !var3.contains("SessionLazyDeserializationEnabled")) && this.bean.isSessionLazyDeserializationEnabledSet()) {
               var5.setSessionLazyDeserializationEnabled(this.bean.isSessionLazyDeserializationEnabled());
            }

            if ((var3 == null || !var3.contains("WeblogicPluginEnabled")) && this.bean.isWeblogicPluginEnabledSet()) {
               var5.setWeblogicPluginEnabled(this.bean.isWeblogicPluginEnabled());
            }

            return var5;
         } catch (RuntimeException var6) {
            throw var6;
         } catch (Exception var7) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var7);
         }
      }

      protected void inferSubTree(Class var1, Object var2) {
         super.inferSubTree(var1, var2);
         Object var3 = null;
         this.inferSubTree(this.bean.getCandidateMachinesForMigratableServers(), var1, var2);
         this.inferSubTree(this.bean.getDataSourceForAutomaticMigration(), var1, var2);
         this.inferSubTree(this.bean.getDataSourceForJobScheduler(), var1, var2);
         this.inferSubTree(this.bean.getDataSourceForSessionPersistence(), var1, var2);
         this.inferSubTree(this.bean.getDatabaseLessLeasingBasis(), var1, var2);
         this.inferSubTree(this.bean.getMigratableTargets(), var1, var2);
         this.inferSubTree(this.bean.getOverloadProtection(), var1, var2);
         this.inferSubTree(this.bean.getServers(), var1, var2);
      }
   }
}
