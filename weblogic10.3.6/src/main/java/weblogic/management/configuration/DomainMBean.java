package weblogic.management.configuration;

import java.util.HashMap;
import javax.management.InvalidAttributeValueException;

public interface DomainMBean extends ConfigurationMBean {
   String UPLOAD_DEFAULT = "upload";
   String CONFIG_CHANGE_LOG = "log";
   String CONFIG_CHANGE_AUDIT = "audit";
   String CONFIG_CHANGE_LOG_AND_AUDIT = "logaudit";
   String CONFIG_CHANGE_NONE = "none";

   String getDomainVersion();

   void setDomainVersion(String var1);

   long getLastModificationTime();

   /** @deprecated */
   boolean isActive();

   SecurityConfigurationMBean getSecurityConfiguration();

   /** @deprecated */
   SecurityMBean getSecurity();

   JTAMBean getJTA();

   JPAMBean getJPA();

   DeploymentConfigurationMBean getDeploymentConfiguration();

   WTCServerMBean[] getWTCServers();

   WTCServerMBean createWTCServer(String var1);

   void destroyWTCServer(WTCServerMBean var1);

   WTCServerMBean lookupWTCServer(String var1);

   LogMBean getLog();

   SNMPAgentMBean getSNMPAgent();

   SNMPAgentDeploymentMBean[] getSNMPAgentDeployments();

   SNMPAgentDeploymentMBean createSNMPAgentDeployment(String var1);

   void destroySNMPAgentDeployment(SNMPAgentDeploymentMBean var1);

   SNMPAgentDeploymentMBean lookupSNMPAgentDeployment(String var1);

   String getRootDirectory();

   void discoverManagedServers();

   boolean discoverManagedServer(String var1);

   /** @deprecated */
   Object[] getDisconnectedManagedServers();

   boolean isConsoleEnabled();

   void setConsoleEnabled(boolean var1);

   String getConsoleContextPath();

   void setConsoleContextPath(String var1);

   String getConsoleExtensionDirectory();

   void setConsoleExtensionDirectory(String var1);

   /** @deprecated */
   boolean isAutoConfigurationSaveEnabled();

   void setAutoConfigurationSaveEnabled(boolean var1);

   ServerMBean[] getServers();

   ServerMBean createServer(String var1);

   void destroyServer(ServerMBean var1);

   ServerMBean lookupServer(String var1);

   CoherenceServerMBean[] getCoherenceServers();

   CoherenceServerMBean createCoherenceServer(String var1);

   void destroyCoherenceServer(CoherenceServerMBean var1);

   CoherenceServerMBean lookupCoherenceServer(String var1);

   ClusterMBean[] getClusters();

   ClusterMBean createCluster(String var1);

   void destroyCluster(ClusterMBean var1);

   ClusterMBean lookupCluster(String var1);

   DeploymentMBean[] getDeployments();

   /** @deprecated */
   FileT3MBean[] getFileT3s();

   /** @deprecated */
   FileT3MBean createFileT3(String var1);

   /** @deprecated */
   void destroyFileT3(FileT3MBean var1);

   /** @deprecated */
   FileT3MBean lookupFileT3(String var1);

   /** @deprecated */
   JDBCConnectionPoolMBean[] getJDBCConnectionPools();

   /** @deprecated */
   JDBCConnectionPoolMBean createJDBCConnectionPool(String var1);

   /** @deprecated */
   void destroyJDBCConnectionPool(JDBCConnectionPoolMBean var1);

   /** @deprecated */
   JDBCConnectionPoolMBean lookupJDBCConnectionPool(String var1);

   /** @deprecated */
   JDBCDataSourceMBean[] getJDBCDataSources();

   /** @deprecated */
   JDBCDataSourceMBean createJDBCDataSource(String var1);

   /** @deprecated */
   void destroyJDBCDataSource(JDBCDataSourceMBean var1);

   /** @deprecated */
   JDBCDataSourceMBean lookupJDBCDataSource(String var1);

   /** @deprecated */
   JDBCTxDataSourceMBean[] getJDBCTxDataSources();

   /** @deprecated */
   JDBCTxDataSourceMBean createJDBCTxDataSource(String var1);

   /** @deprecated */
   void destroyJDBCTxDataSource(JDBCTxDataSourceMBean var1);

   /** @deprecated */
   JDBCTxDataSourceMBean lookupJDBCTxDataSource(String var1);

   /** @deprecated */
   JDBCMultiPoolMBean[] getJDBCMultiPools();

   /** @deprecated */
   JDBCMultiPoolMBean createJDBCMultiPool(String var1);

   /** @deprecated */
   void destroyJDBCMultiPool(JDBCMultiPoolMBean var1);

   /** @deprecated */
   JDBCMultiPoolMBean lookupJDBCMultiPool(String var1);

   MessagingBridgeMBean[] getMessagingBridges();

   MessagingBridgeMBean createMessagingBridge(String var1);

   void destroyMessagingBridge(MessagingBridgeMBean var1);

   MessagingBridgeMBean lookupMessagingBridge(String var1);

   /** @deprecated */
   HashMap start();

   /** @deprecated */
   HashMap kill();

   void setProductionModeEnabled(boolean var1);

   boolean isProductionModeEnabled();

   EmbeddedLDAPMBean getEmbeddedLDAP();

   boolean isAdministrationPortEnabled();

   void setAdministrationPortEnabled(boolean var1) throws InvalidAttributeValueException;

   int getAdministrationPort();

   void setExalogicOptimizationsEnabled(boolean var1);

   boolean isExalogicOptimizationsEnabled();

   void setAdministrationPort(int var1) throws InvalidAttributeValueException;

   int getArchiveConfigurationCount();

   void setArchiveConfigurationCount(int var1);

   boolean isConfigBackupEnabled();

   void setConfigBackupEnabled(boolean var1);

   String getConfigurationVersion();

   void setConfigurationVersion(String var1);

   /** @deprecated */
   boolean isAdministrationMBeanAuditingEnabled();

   /** @deprecated */
   void setAdministrationMBeanAuditingEnabled(boolean var1);

   String getConfigurationAuditType();

   void setConfigurationAuditType(String var1) throws InvalidAttributeValueException;

   boolean isClusterConstraintsEnabled();

   void setClusterConstraintsEnabled(boolean var1);

   /** @deprecated */
   ApplicationMBean[] getApplications();

   /** @deprecated */
   ApplicationMBean createApplication(String var1);

   /** @deprecated */
   void destroyApplication(ApplicationMBean var1);

   /** @deprecated */
   ApplicationMBean lookupApplication(String var1);

   AppDeploymentMBean[] getAppDeployments();

   AppDeploymentMBean lookupAppDeployment(String var1);

   AppDeploymentMBean createAppDeployment(String var1, String var2) throws IllegalArgumentException;

   void destroyAppDeployment(AppDeploymentMBean var1);

   AppDeploymentMBean[] getInternalAppDeployments();

   AppDeploymentMBean lookupInternalAppDeployment(String var1);

   AppDeploymentMBean createInternalAppDeployment(String var1, String var2) throws IllegalArgumentException;

   void destroyInternalAppDeployment(AppDeploymentMBean var1);

   LibraryMBean[] getLibraries();

   LibraryMBean lookupLibrary(String var1);

   LibraryMBean createLibrary(String var1, String var2);

   void destroyLibrary(LibraryMBean var1);

   DomainLibraryMBean[] getDomainLibraries();

   DomainLibraryMBean lookupDomainLibrary(String var1);

   DomainLibraryMBean createDomainLibrary(String var1, String var2);

   void destroyDomainLibrary(DomainLibraryMBean var1);

   LibraryMBean[] getInternalLibraries();

   LibraryMBean lookupInternalLibrary(String var1);

   LibraryMBean createInternalLibrary(String var1, String var2);

   void destroyInternalLibrary(LibraryMBean var1);

   BasicDeploymentMBean[] getBasicDeployments();

   WSReliableDeliveryPolicyMBean[] getWSReliableDeliveryPolicies();

   WSReliableDeliveryPolicyMBean lookupWSReliableDeliveryPolicy(String var1);

   WSReliableDeliveryPolicyMBean createWSReliableDeliveryPolicy(String var1);

   void destroyWSReliableDeliveryPolicy(WSReliableDeliveryPolicyMBean var1);

   /** @deprecated */
   JDBCDataSourceFactoryMBean[] getJDBCDataSourceFactories();

   /** @deprecated */
   JDBCDataSourceFactoryMBean createJDBCDataSourceFactory(String var1);

   /** @deprecated */
   void destroyJDBCDataSourceFactory(JDBCDataSourceFactoryMBean var1);

   /** @deprecated */
   JDBCDataSourceFactoryMBean lookupJDBCDataSourceFactory(String var1);

   MachineMBean[] getMachines();

   MachineMBean createMachine(String var1);

   UnixMachineMBean createUnixMachine(String var1);

   void destroyMachine(MachineMBean var1);

   MachineMBean lookupMachine(String var1);

   XMLEntityCacheMBean[] getXMLEntityCaches();

   XMLEntityCacheMBean createXMLEntityCache(String var1);

   XMLEntityCacheMBean lookupXMLEntityCache(String var1);

   void destroyXMLEntityCache(XMLEntityCacheMBean var1);

   XMLRegistryMBean[] getXMLRegistries();

   XMLRegistryMBean createXMLRegistry(String var1);

   void destroyXMLRegistry(XMLRegistryMBean var1);

   XMLRegistryMBean lookupXMLRegistry(String var1);

   /** @deprecated */
   FileRealmMBean[] getFileRealms();

   /** @deprecated */
   FileRealmMBean createFileRealm(String var1);

   /** @deprecated */
   void destroyFileRealm(FileRealmMBean var1);

   /** @deprecated */
   FileRealmMBean lookupFileRealm(String var1);

   /** @deprecated */
   CachingRealmMBean[] getCachingRealms();

   /** @deprecated */
   CachingRealmMBean createCachingRealm(String var1);

   /** @deprecated */
   void destroyCachingRealm(CachingRealmMBean var1);

   /** @deprecated */
   CachingRealmMBean lookupCachingRealm(String var1);

   /** @deprecated */
   RealmMBean[] getRealms();

   /** @deprecated */
   RealmMBean createRealm(String var1);

   /** @deprecated */
   void destroyRealm(RealmMBean var1);

   /** @deprecated */
   RealmMBean lookupRealm(String var1);

   /** @deprecated */
   PasswordPolicyMBean[] getPasswordPolicies();

   /** @deprecated */
   PasswordPolicyMBean createPasswordPolicy(String var1);

   /** @deprecated */
   void destroyPasswordPolicy(PasswordPolicyMBean var1);

   /** @deprecated */
   PasswordPolicyMBean lookupPasswordPolicy(String var1);

   /** @deprecated */
   BasicRealmMBean[] getBasicRealms();

   /** @deprecated */
   CustomRealmMBean[] getCustomRealms();

   /** @deprecated */
   CustomRealmMBean createCustomRealm(String var1);

   /** @deprecated */
   void destroyCustomRealm(CustomRealmMBean var1);

   /** @deprecated */
   CustomRealmMBean lookupCustomRealm(String var1);

   /** @deprecated */
   LDAPRealmMBean[] getLDAPRealms();

   /** @deprecated */
   LDAPRealmMBean createLDAPRealm(String var1);

   /** @deprecated */
   void destroyLDAPRealm(LDAPRealmMBean var1);

   /** @deprecated */
   LDAPRealmMBean lookupLDAPRealm(String var1);

   /** @deprecated */
   NTRealmMBean[] getNTRealms();

   /** @deprecated */
   NTRealmMBean createNTRealm(String var1);

   /** @deprecated */
   void destroyNTRealm(NTRealmMBean var1);

   /** @deprecated */
   NTRealmMBean lookupNTRealm(String var1);

   /** @deprecated */
   RDBMSRealmMBean[] getRDBMSRealms();

   /** @deprecated */
   RDBMSRealmMBean createRDBMSRealm(String var1);

   /** @deprecated */
   void destroyRDBMSRealm(RDBMSRealmMBean var1);

   /** @deprecated */
   RDBMSRealmMBean lookupRDBMSRealm(String var1);

   /** @deprecated */
   UnixRealmMBean[] getUnixRealms();

   /** @deprecated */
   UnixRealmMBean createUnixRealm(String var1);

   /** @deprecated */
   void destroyUnixRealm(UnixRealmMBean var1);

   /** @deprecated */
   UnixRealmMBean lookupUnixRealm(String var1);

   TargetMBean[] getTargets();

   TargetMBean lookupTarget(String var1) throws IllegalArgumentException;

   JMSServerMBean[] getJMSServers();

   JMSServerMBean createJMSServer(String var1);

   void destroyJMSServer(JMSServerMBean var1);

   JMSServerMBean lookupJMSServer(String var1);

   /** @deprecated */
   JMSStoreMBean[] getJMSStores();

   /** @deprecated */
   JMSStoreMBean lookupJMSStore(String var1);

   /** @deprecated */
   JMSJDBCStoreMBean[] getJMSJDBCStores();

   /** @deprecated */
   JMSJDBCStoreMBean createJMSJDBCStore(String var1);

   /** @deprecated */
   void destroyJMSJDBCStore(JMSJDBCStoreMBean var1);

   /** @deprecated */
   JMSJDBCStoreMBean lookupJMSJDBCStore(String var1);

   /** @deprecated */
   JMSFileStoreMBean[] getJMSFileStores();

   /** @deprecated */
   JMSFileStoreMBean createJMSFileStore(String var1);

   /** @deprecated */
   void destroyJMSFileStore(JMSFileStoreMBean var1);

   /** @deprecated */
   JMSFileStoreMBean lookupJMSFileStore(String var1);

   /** @deprecated */
   JMSDestinationMBean[] getJMSDestinations();

   JMSDestinationMBean lookupJMSDestination(String var1);

   /** @deprecated */
   JMSQueueMBean[] getJMSQueues();

   /** @deprecated */
   JMSQueueMBean createJMSQueue(String var1);

   /** @deprecated */
   void destroyJMSQueue(JMSQueueMBean var1);

   /** @deprecated */
   JMSQueueMBean lookupJMSQueue(String var1);

   /** @deprecated */
   JMSTopicMBean[] getJMSTopics();

   /** @deprecated */
   JMSTopicMBean createJMSTopic(String var1);

   /** @deprecated */
   void destroyJMSTopic(JMSTopicMBean var1);

   /** @deprecated */
   JMSTopicMBean lookupJMSTopic(String var1);

   /** @deprecated */
   JMSDistributedQueueMBean[] getJMSDistributedQueues();

   /** @deprecated */
   JMSDistributedQueueMBean createJMSDistributedQueue(String var1);

   /** @deprecated */
   void destroyJMSDistributedQueue(JMSDistributedQueueMBean var1);

   /** @deprecated */
   JMSDistributedQueueMBean lookupJMSDistributedQueue(String var1);

   /** @deprecated */
   JMSDistributedTopicMBean[] getJMSDistributedTopics();

   /** @deprecated */
   JMSDistributedTopicMBean createJMSDistributedTopic(String var1);

   /** @deprecated */
   void destroyJMSDistributedTopic(JMSDistributedTopicMBean var1);

   /** @deprecated */
   JMSDistributedTopicMBean lookupJMSDistributedTopic(String var1);

   /** @deprecated */
   JMSTemplateMBean[] getJMSTemplates();

   /** @deprecated */
   JMSTemplateMBean createJMSTemplate(String var1);

   /** @deprecated */
   void destroyJMSTemplate(JMSTemplateMBean var1);

   /** @deprecated */
   JMSTemplateMBean lookupJMSTemplate(String var1);

   /** @deprecated */
   NetworkChannelMBean[] getNetworkChannels();

   /** @deprecated */
   NetworkChannelMBean createNetworkChannel(String var1);

   /** @deprecated */
   void destroyNetworkChannel(NetworkChannelMBean var1);

   /** @deprecated */
   NetworkChannelMBean lookupNetworkChannel(String var1);

   VirtualHostMBean[] getVirtualHosts();

   VirtualHostMBean createVirtualHost(String var1);

   void destroyVirtualHost(VirtualHostMBean var1);

   VirtualHostMBean lookupVirtualHost(String var1);

   MigratableTargetMBean[] getMigratableTargets();

   MigratableTargetMBean createMigratableTarget(String var1);

   void destroyMigratableTarget(MigratableTargetMBean var1);

   MigratableTargetMBean lookupMigratableTarget(String var1);

   EJBContainerMBean getEJBContainer();

   EJBContainerMBean createEJBContainer();

   void destroyEJBContainer();

   WebAppContainerMBean getWebAppContainer();

   JMXMBean getJMX();

   SelfTuningMBean getSelfTuning();

   PathServiceMBean[] getPathServices();

   PathServiceMBean createPathService(String var1);

   void destroyPathService(PathServiceMBean var1);

   PathServiceMBean lookupPathService(String var1);

   /** @deprecated */
   JMSDestinationKeyMBean[] getJMSDestinationKeys();

   /** @deprecated */
   JMSDestinationKeyMBean createJMSDestinationKey(String var1);

   /** @deprecated */
   void destroyJMSDestinationKey(JMSDestinationKeyMBean var1);

   /** @deprecated */
   JMSDestinationKeyMBean lookupJMSDestinationKey(String var1);

   /** @deprecated */
   JMSConnectionFactoryMBean[] getJMSConnectionFactories();

   /** @deprecated */
   JMSConnectionFactoryMBean createJMSConnectionFactory(String var1);

   /** @deprecated */
   void destroyJMSConnectionFactory(JMSConnectionFactoryMBean var1);

   /** @deprecated */
   JMSConnectionFactoryMBean lookupJMSConnectionFactory(String var1);

   /** @deprecated */
   JMSSessionPoolMBean[] getJMSSessionPools();

   /** @deprecated */
   JMSSessionPoolMBean createJMSSessionPool(String var1);

   /** @deprecated */
   JMSSessionPoolMBean createJMSSessionPool(String var1, JMSSessionPoolMBean var2);

   /** @deprecated */
   void destroyJMSSessionPool(JMSSessionPoolMBean var1);

   /** @deprecated */
   JMSSessionPoolMBean lookupJMSSessionPool(String var1);

   JMSBridgeDestinationMBean createJMSBridgeDestination(String var1);

   void destroyJMSBridgeDestination(JMSBridgeDestinationMBean var1);

   JMSBridgeDestinationMBean lookupJMSBridgeDestination(String var1);

   JMSBridgeDestinationMBean[] getJMSBridgeDestinations();

   BridgeDestinationMBean createBridgeDestination(String var1);

   void destroyBridgeDestination(BridgeDestinationMBean var1);

   BridgeDestinationMBean lookupBridgeDestination(String var1);

   /** @deprecated */
   BridgeDestinationMBean[] getBridgeDestinations();

   /** @deprecated */
   ForeignJMSServerMBean[] getForeignJMSServers();

   /** @deprecated */
   ForeignJMSServerMBean createForeignJMSServer(String var1);

   /** @deprecated */
   void destroyForeignJMSServer(ForeignJMSServerMBean var1);

   /** @deprecated */
   ForeignJMSServerMBean lookupForeignJMSServer(String var1);

   ShutdownClassMBean[] getShutdownClasses();

   ShutdownClassMBean createShutdownClass(String var1);

   void destroyShutdownClass(ShutdownClassMBean var1);

   ShutdownClassMBean lookupShutdownClass(String var1);

   StartupClassMBean[] getStartupClasses();

   StartupClassMBean createStartupClass(String var1);

   void destroyStartupClass(StartupClassMBean var1);

   StartupClassMBean lookupStartupClass(String var1);

   SingletonServiceMBean[] getSingletonServices();

   SingletonServiceMBean createSingletonService(String var1);

   void destroySingletonService(SingletonServiceMBean var1);

   SingletonServiceMBean lookupSingletonService(String var1);

   MailSessionMBean[] getMailSessions();

   MailSessionMBean createMailSession(String var1);

   void destroyMailSession(MailSessionMBean var1);

   MailSessionMBean lookupMailSession(String var1);

   JoltConnectionPoolMBean[] getJoltConnectionPools();

   JoltConnectionPoolMBean createJoltConnectionPool(String var1);

   void destroyJoltConnectionPool(JoltConnectionPoolMBean var1);

   JoltConnectionPoolMBean lookupJoltConnectionPool(String var1);

   LogFilterMBean[] getLogFilters();

   LogFilterMBean createLogFilter(String var1);

   void destroyLogFilter(LogFilterMBean var1);

   LogFilterMBean lookupLogFilter(String var1);

   /** @deprecated */
   DomainLogFilterMBean[] getDomainLogFilters();

   /** @deprecated */
   DomainLogFilterMBean createDomainLogFilter(String var1);

   /** @deprecated */
   void destroyDomainLogFilter(DomainLogFilterMBean var1);

   /** @deprecated */
   DomainLogFilterMBean lookupDomainLogFilter(String var1);

   FileStoreMBean[] getFileStores();

   FileStoreMBean createFileStore(String var1);

   void destroyFileStore(FileStoreMBean var1);

   FileStoreMBean lookupFileStore(String var1);

   JDBCStoreMBean[] getJDBCStores();

   JDBCStoreMBean createJDBCStore(String var1);

   void destroyJDBCStore(JDBCStoreMBean var1);

   JDBCStoreMBean lookupJDBCStore(String var1);

   JMSInteropModuleMBean[] getJMSInteropModules();

   JMSInteropModuleMBean createJMSInteropModule(String var1);

   void destroyJMSInteropModule(JMSInteropModuleMBean var1);

   JMSInteropModuleMBean lookupJMSInteropModule(String var1);

   JMSSystemResourceMBean[] getJMSSystemResources();

   JMSSystemResourceMBean createJMSSystemResource(String var1);

   JMSSystemResourceMBean createJMSSystemResource(String var1, String var2);

   void destroyJMSSystemResource(JMSSystemResourceMBean var1);

   JMSSystemResourceMBean lookupJMSSystemResource(String var1);

   CustomResourceMBean[] getCustomResources();

   CustomResourceMBean createCustomResource(String var1, String var2, String var3);

   CustomResourceMBean createCustomResource(String var1, String var2, String var3, String var4);

   void destroyCustomResource(CustomResourceMBean var1);

   CustomResourceMBean lookupCustomResource(String var1);

   ForeignJNDIProviderMBean[] getForeignJNDIProviders();

   ForeignJNDIProviderMBean lookupForeignJNDIProvider(String var1);

   ForeignJNDIProviderMBean createForeignJNDIProvider(String var1);

   void destroyForeignJNDIProvider(ForeignJNDIProviderMBean var1);

   String getAdminServerName();

   void setAdminServerName(String var1);

   String getAdministrationProtocol();

   void setAdministrationProtocol(String var1);

   WLDFSystemResourceMBean[] getWLDFSystemResources();

   WLDFSystemResourceMBean createWLDFSystemResource(String var1);

   WLDFSystemResourceMBean createWLDFSystemResource(String var1, String var2);

   void destroyWLDFSystemResource(WLDFSystemResourceMBean var1);

   WLDFSystemResourceMBean lookupWLDFSystemResource(String var1);

   JDBCSystemResourceMBean[] getJDBCSystemResources();

   JDBCSystemResourceMBean createJDBCSystemResource(String var1);

   JDBCSystemResourceMBean createJDBCSystemResource(String var1, String var2);

   JDBCSystemResourceMBean lookupJDBCSystemResource(String var1);

   void destroyJDBCSystemResource(JDBCSystemResourceMBean var1);

   SystemResourceMBean[] getSystemResources();

   SystemResourceMBean lookupSystemResource(String var1);

   SAFAgentMBean[] getSAFAgents();

   SAFAgentMBean createSAFAgent(String var1);

   void destroySAFAgent(SAFAgentMBean var1);

   SAFAgentMBean lookupSAFAgent(String var1);

   WLECConnectionPoolMBean[] getWLECConnectionPools();

   WLECConnectionPoolMBean createWLECConnectionPool(String var1);

   void destroyWLECConnectionPool(WLECConnectionPoolMBean var1);

   WLECConnectionPoolMBean lookupWLECConnectionPool(String var1);

   ErrorHandlingMBean[] getErrorHandlings();

   ErrorHandlingMBean createErrorHandling(String var1);

   void destroyErrorHandling(ErrorHandlingMBean var1);

   ErrorHandlingMBean lookupErrorHandling(String var1);

   RemoteSAFContextMBean[] getRemoteSAFContexts();

   RemoteSAFContextMBean createRemoteSAFContext(String var1);

   void destroyRemoteSAFContext(RemoteSAFContextMBean var1);

   RemoteSAFContextMBean lookupRemoteSAFContext(String var1);

   MigratableRMIServiceMBean[] getMigratableRMIServices();

   MigratableRMIServiceMBean createMigratableRMIService(String var1);

   void destroyMigratableRMIService(MigratableRMIServiceMBean var1);

   MigratableRMIServiceMBean lookupMigratableRMIService(String var1);

   AdminServerMBean getAdminServerMBean();

   AdminServerMBean createAdminServerMBean();

   void destroyAdminServerMBean();

   /** @deprecated */
   JMSDistributedQueueMemberMBean[] getJMSDistributedQueueMembers();

   /** @deprecated */
   JMSDistributedQueueMemberMBean createJMSDistributedQueueMember(String var1);

   /** @deprecated */
   void destroyJMSDistributedQueueMember(JMSDistributedQueueMemberMBean var1);

   /** @deprecated */
   JMSDistributedQueueMemberMBean lookupJMSDistributedQueueMember(String var1);

   /** @deprecated */
   JMSDistributedTopicMemberMBean[] getJMSDistributedTopicMembers();

   /** @deprecated */
   JMSDistributedTopicMemberMBean createJMSDistributedTopicMember(String var1);

   /** @deprecated */
   void destroyJMSDistributedTopicMember(JMSDistributedTopicMemberMBean var1);

   /** @deprecated */
   JMSDistributedTopicMemberMBean lookupJMSDistributedTopicMember(String var1);

   SNMPTrapDestinationMBean createSNMPTrapDestination(String var1);

   void destroySNMPTrapDestination(SNMPTrapDestinationMBean var1);

   SNMPTrapDestinationMBean[] getSNMPTrapDestinations();

   SNMPProxyMBean[] getSNMPProxies();

   SNMPProxyMBean createSNMPProxy(String var1);

   void destroySNMPProxy(SNMPProxyMBean var1);

   SNMPGaugeMonitorMBean[] getSNMPGaugeMonitors();

   SNMPGaugeMonitorMBean createSNMPGaugeMonitor(String var1);

   void destroySNMPGaugeMonitor(SNMPGaugeMonitorMBean var1);

   SNMPStringMonitorMBean[] getSNMPStringMonitors();

   SNMPStringMonitorMBean createSNMPStringMonitor(String var1);

   void destroySNMPStringMonitor(SNMPStringMonitorMBean var1);

   SNMPCounterMonitorMBean[] getSNMPCounterMonitors();

   SNMPCounterMonitorMBean createSNMPCounterMonitor(String var1);

   void destroySNMPCounterMonitor(SNMPCounterMonitorMBean var1);

   SNMPLogFilterMBean[] getSNMPLogFilters();

   SNMPLogFilterMBean createSNMPLogFilter(String var1);

   void destroySNMPLogFilter(SNMPLogFilterMBean var1);

   SNMPAttributeChangeMBean[] getSNMPAttributeChanges();

   SNMPAttributeChangeMBean createSNMPAttributeChange(String var1);

   void destroySNMPAttributeChange(SNMPAttributeChangeMBean var1);

   WebserviceSecurityMBean[] getWebserviceSecurities();

   WebserviceSecurityMBean lookupWebserviceSecurity(String var1);

   WebserviceSecurityMBean createWebserviceSecurity(String var1);

   void destroyWebserviceSecurity(WebserviceSecurityMBean var1);

   ForeignJMSConnectionFactoryMBean[] getForeignJMSConnectionFactories();

   ForeignJMSConnectionFactoryMBean lookupForeignJMSConnectionFactory(String var1);

   ForeignJMSConnectionFactoryMBean createForeignJMSConnectionFactory(String var1);

   void destroyForeignJMSConnectionFactory(ForeignJMSConnectionFactoryMBean var1);

   ForeignJMSDestinationMBean[] getForeignJMSDestinations();

   ForeignJMSDestinationMBean lookupForeignJMSDestination(String var1);

   ForeignJMSDestinationMBean createForeignJMSDestination(String var1);

   void destroyForeignJMSDestination(ForeignJMSDestinationMBean var1);

   /** @deprecated */
   JMSConnectionConsumerMBean[] getJMSConnectionConsumers();

   JMSConnectionConsumerMBean lookupJMSConnectionConsumer(String var1);

   JMSConnectionConsumerMBean createJMSConnectionConsumer(String var1);

   void destroyJMSConnectionConsumer(JMSConnectionConsumerMBean var1);

   ForeignJMSDestinationMBean createForeignJMSDestination(String var1, ForeignJMSDestinationMBean var2);

   ForeignJMSConnectionFactoryMBean createForeignJMSConnectionFactory(String var1, ForeignJMSConnectionFactoryMBean var2);

   JMSDistributedQueueMemberMBean createJMSDistributedQueueMember(String var1, JMSDistributedQueueMemberMBean var2);

   JMSDistributedTopicMemberMBean createJMSDistributedTopicMember(String var1, JMSDistributedTopicMemberMBean var2);

   JMSTopicMBean createJMSTopic(String var1, JMSTopicMBean var2);

   JMSQueueMBean createJMSQueue(String var1, JMSQueueMBean var2);

   boolean isAutoDeployForSubmodulesEnabled();

   void setAutoDeployForSubmodulesEnabled(boolean var1);

   AdminConsoleMBean getAdminConsole();

   boolean isInternalAppsDeployOnDemandEnabled();

   void setInternalAppsDeployOnDemandEnabled(boolean var1);

   boolean isGuardianEnabled();

   void setGuardianEnabled(boolean var1);

   boolean isOCMEnabled();

   void setOCMEnabled(boolean var1);

   boolean isMsgIdPrefixCompatibilityEnabled();

   void setMsgIdPrefixCompatibilityEnabled(boolean var1);

   CoherenceClusterSystemResourceMBean[] getCoherenceClusterSystemResources();

   CoherenceClusterSystemResourceMBean createCoherenceClusterSystemResource(String var1);

   void destroyCoherenceClusterSystemResource(CoherenceClusterSystemResourceMBean var1);

   CoherenceClusterSystemResourceMBean lookupCoherenceClusterSystemResource(String var1);

   RestfulManagementServicesMBean getRestfulManagementServices();
}
