package weblogic.management.configuration;

import java.util.HashMap;
import javax.management.InvalidAttributeValueException;
import weblogic.management.DistributedManagementException;

public interface ClusterMBean extends TargetMBean {
   ServerMBean[] getServers();

   String getClusterAddress();

   void setClusterAddress(String var1) throws InvalidAttributeValueException;

   String getMulticastAddress();

   void setMulticastAddress(String var1) throws InvalidAttributeValueException;

   void setMulticastBufferSize(int var1);

   int getMulticastBufferSize();

   int getMulticastPort();

   void setMulticastPort(int var1) throws InvalidAttributeValueException;

   int getMulticastTTL();

   void setMulticastTTL(int var1) throws InvalidAttributeValueException;

   int getMulticastSendDelay();

   void setMulticastSendDelay(int var1) throws InvalidAttributeValueException;

   String getDefaultLoadAlgorithm();

   void setDefaultLoadAlgorithm(String var1) throws InvalidAttributeValueException;

   String getClusterMessagingMode();

   void setClusterMessagingMode(String var1) throws InvalidAttributeValueException;

   String getClusterBroadcastChannel();

   void setClusterBroadcastChannel(String var1) throws InvalidAttributeValueException;

   int getServiceAgeThresholdSeconds();

   void setServiceAgeThresholdSeconds(int var1) throws InvalidAttributeValueException, DistributedManagementException;

   /** @deprecated */
   HashMap start();

   /** @deprecated */
   HashMap kill();

   void setClientCertProxyEnabled(boolean var1);

   boolean isClientCertProxyEnabled();

   void setWeblogicPluginEnabled(boolean var1);

   boolean isWeblogicPluginEnabled();

   MigratableTargetMBean[] getMigratableTargets();

   int getMemberWarmupTimeoutSeconds();

   void setMemberWarmupTimeoutSeconds(int var1);

   void setHttpTraceSupportEnabled(boolean var1);

   boolean isHttpTraceSupportEnabled();

   String getFrontendHost();

   void setFrontendHost(String var1) throws InvalidAttributeValueException;

   int getFrontendHTTPPort();

   void setFrontendHTTPPort(int var1) throws InvalidAttributeValueException;

   int getFrontendHTTPSPort();

   void setFrontendHTTPSPort(int var1) throws InvalidAttributeValueException;

   int getIdlePeriodsUntilTimeout();

   void setIdlePeriodsUntilTimeout(int var1);

   void setRemoteClusterAddress(String var1);

   String getRemoteClusterAddress();

   void setWANSessionPersistenceTableName(String var1);

   String getWANSessionPersistenceTableName();

   String getReplicationChannel();

   void setReplicationChannel(String var1);

   int getInterClusterCommLinkHealthCheckInterval();

   void setInterClusterCommLinkHealthCheckInterval(int var1);

   void setDataSourceForSessionPersistence(JDBCSystemResourceMBean var1);

   JDBCSystemResourceMBean getDataSourceForSessionPersistence();

   void setDataSourceForJobScheduler(JDBCSystemResourceMBean var1);

   JDBCSystemResourceMBean getDataSourceForJobScheduler();

   String getJobSchedulerTableName();

   void setJobSchedulerTableName(String var1);

   boolean getPersistSessionsOnShutdown();

   void setPersistSessionsOnShutdown(boolean var1);

   void setAsyncSessionQueueTimeout(int var1);

   int getAsyncSessionQueueTimeout();

   void setGreedySessionFlushInterval(int var1);

   int getGreedySessionFlushInterval();

   void setSessionFlushInterval(int var1);

   int getSessionFlushInterval();

   void setSessionFlushThreshold(int var1);

   int getSessionFlushThreshold();

   MachineMBean[] getCandidateMachinesForMigratableServers();

   void setCandidateMachinesForMigratableServers(MachineMBean[] var1);

   JDBCSystemResourceMBean getDataSourceForAutomaticMigration();

   void setDataSourceForAutomaticMigration(JDBCSystemResourceMBean var1);

   int getHealthCheckIntervalMillis();

   void setHealthCheckIntervalMillis(int var1);

   int getHealthCheckPeriodsUntilFencing();

   void setHealthCheckPeriodsUntilFencing(int var1);

   int getFencingGracePeriodMillis();

   void setFencingGracePeriodMillis(int var1);

   String getSingletonSQLQueryHelper();

   void setSingletonSQLQueryHelper(String var1);

   int getNumberOfServersInClusterAddress();

   void setNumberOfServersInClusterAddress(int var1);

   void setClusterType(String var1);

   String getClusterType();

   void setMulticastDataEncryption(boolean var1);

   boolean getMulticastDataEncryption();

   void setAutoMigrationTableName(String var1);

   String getAutoMigrationTableName();

   int getAdditionalAutoMigrationAttempts();

   void setAdditionalAutoMigrationAttempts(int var1);

   long getMillisToSleepBetweenAutoMigrationAttempts();

   void setMillisToSleepBetweenAutoMigrationAttempts(long var1);

   void setReplicationTimeoutEnabled(boolean var1);

   String getMigrationBasis();

   void setMigrationBasis(String var1);

   int getConsensusParticipants();

   void setConsensusParticipants(int var1);

   boolean isReplicationTimeoutEnabled();

   OverloadProtectionMBean getOverloadProtection();

   DatabaseLessLeasingBasisMBean getDatabaseLessLeasingBasis();

   void setHTTPPingRetryCount(int var1);

   int getHTTPPingRetryCount();

   void setMaxServerCountForHttpPing(int var1);

   int getMaxServerCountForHttpPing();

   boolean isSecureReplicationEnabled();

   void setSecureReplicationEnabled(boolean var1);

   void setUnicastDiscoveryPeriodMillis(int var1);

   int getUnicastDiscoveryPeriodMillis();

   void setMessageOrderingEnabled(boolean var1);

   boolean isMessageOrderingEnabled();

   void setOneWayRmiForReplicationEnabled(boolean var1);

   boolean isOneWayRmiForReplicationEnabled();

   void setSessionLazyDeserializationEnabled(boolean var1);

   boolean isSessionLazyDeserializationEnabled();

   void setDeathDetectorHeartbeatPeriod(int var1);

   int getDeathDetectorHeartbeatPeriod();

   void setMemberDeathDetectorEnabled(boolean var1);

   boolean isMemberDeathDetectorEnabled();
}
