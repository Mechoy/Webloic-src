package weblogic.management.configuration;

import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BootstrapProperties;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorValidateException;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.ReferenceManager;
import weblogic.descriptor.internal.ResolvedReference;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.diagnostics.context.DiagnosticContextHelper;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.ArrayIterator;
import weblogic.utils.collections.CombinedIterator;

public class ServerDebugMBeanImpl extends KernelDebugMBeanImpl implements ServerDebugMBean, Serializable {
   private boolean _ApplicationContainer;
   private String _BugReportServiceWsdlUrl;
   private boolean _ClassChangeNotifier;
   private boolean _ClassFinder;
   private boolean _ClassLoader;
   private boolean _ClassLoaderVerbose;
   private boolean _ClassloaderWebApp;
   private boolean _ClasspathServlet;
   private boolean _DebugAppContainer;
   private boolean _DebugAsyncQueue;
   private boolean _DebugBootstrapServlet;
   private boolean _DebugCertRevocCheck;
   private boolean _DebugClassRedef;
   private boolean _DebugClassSize;
   private boolean _DebugCluster;
   private boolean _DebugClusterAnnouncements;
   private boolean _DebugClusterFragments;
   private boolean _DebugClusterHeartbeats;
   private boolean _DebugConfigurationEdit;
   private boolean _DebugConfigurationRuntime;
   private boolean _DebugConnectorService;
   private boolean _DebugConsensusLeasing;
   private boolean _DebugDRSCalls;
   private boolean _DebugDRSHeartbeats;
   private boolean _DebugDRSMessages;
   private boolean _DebugDRSQueues;
   private boolean _DebugDRSStateTransitions;
   private boolean _DebugDRSUpdateStatus;
   private boolean _DebugDeploy;
   private boolean _DebugDeployment;
   private boolean _DebugDeploymentService;
   private boolean _DebugDeploymentServiceInternal;
   private boolean _DebugDeploymentServiceStatusUpdates;
   private boolean _DebugDeploymentServiceTransport;
   private boolean _DebugDeploymentServiceTransportHttp;
   private boolean _DebugDescriptor;
   private boolean _DebugDiagnosticAccessor;
   private boolean _DebugDiagnosticArchive;
   private boolean _DebugDiagnosticArchiveRetirement;
   private boolean _DebugDiagnosticCollections;
   private boolean _DebugDiagnosticContext;
   private boolean _DebugDiagnosticDataGathering;
   private boolean _DebugDiagnosticFileArchive;
   private boolean _DebugDiagnosticImage;
   private boolean _DebugDiagnosticInstrumentation;
   private boolean _DebugDiagnosticInstrumentationActions;
   private boolean _DebugDiagnosticInstrumentationConfig;
   private boolean _DebugDiagnosticInstrumentationEvents;
   private boolean _DebugDiagnosticInstrumentationWeaving;
   private boolean _DebugDiagnosticInstrumentationWeavingMatches;
   private boolean _DebugDiagnosticJdbcArchive;
   private boolean _DebugDiagnosticLifecycleHandlers;
   private boolean _DebugDiagnosticQuery;
   private boolean _DebugDiagnosticWatch;
   private boolean _DebugDiagnosticWlstoreArchive;
   private boolean _DebugDiagnosticsHarvester;
   private boolean _DebugDiagnosticsHarvesterData;
   private boolean _DebugDiagnosticsHarvesterMBeanPlugin;
   private boolean _DebugDiagnosticsHarvesterTreeBeanPlugin;
   private boolean _DebugDiagnosticsModule;
   private boolean _DebugDomainLogHandler;
   private boolean _DebugEjbCaching;
   private boolean _DebugEjbCmpDeployment;
   private boolean _DebugEjbCmpRuntime;
   private boolean _DebugEjbCompilation;
   private boolean _DebugEjbDeployment;
   private boolean _DebugEjbInvoke;
   private boolean _DebugEjbLocking;
   private boolean _DebugEjbMdbConnection;
   private boolean _DebugEjbPooling;
   private boolean _DebugEjbSecurity;
   private boolean _DebugEjbSwapping;
   private boolean _DebugEjbTimers;
   private boolean _DebugEmbeddedLDAP;
   private int _DebugEmbeddedLDAPLogLevel;
   private boolean _DebugEmbeddedLDAPLogToConsole;
   private boolean _DebugEmbeddedLDAPWriteOverrideProps;
   private boolean _DebugEventManager;
   private boolean _DebugFileDistributionServlet;
   private boolean _DebugHttp;
   private boolean _DebugHttpLogging;
   private boolean _DebugHttpSessions;
   private boolean _DebugIIOPNaming;
   private boolean _DebugIIOPTunneling;
   private boolean _DebugJ2EEManagement;
   private int _DebugJAXPDebugLevel;
   private String _DebugJAXPDebugName;
   private boolean _DebugJAXPIncludeClass;
   private boolean _DebugJAXPIncludeLocation;
   private boolean _DebugJAXPIncludeName;
   private boolean _DebugJAXPIncludeTime;
   private OutputStream _DebugJAXPOutputStream;
   private boolean _DebugJAXPUseShortClass;
   private boolean _DebugJDBCConn;
   private boolean _DebugJDBCDriverLogging;
   private boolean _DebugJDBCInternal;
   private boolean _DebugJDBCONS;
   private boolean _DebugJDBCRAC;
   private boolean _DebugJDBCREPLAY;
   private boolean _DebugJDBCRMI;
   private boolean _DebugJDBCSQL;
   private boolean _DebugJDBCUCP;
   private boolean _DebugJMSAME;
   private boolean _DebugJMSBackEnd;
   private boolean _DebugJMSBoot;
   private boolean _DebugJMSCDS;
   private boolean _DebugJMSCommon;
   private boolean _DebugJMSConfig;
   private boolean _DebugJMSDispatcher;
   private boolean _DebugJMSDistTopic;
   private boolean _DebugJMSDurableSubscribers;
   private boolean _DebugJMSFrontEnd;
   private boolean _DebugJMSJDBCScavengeOnFlush;
   private boolean _DebugJMSLocking;
   private boolean _DebugJMSMessagePath;
   private boolean _DebugJMSModule;
   private boolean _DebugJMSPauseResume;
   private boolean _DebugJMSSAF;
   private boolean _DebugJMSStore;
   private boolean _DebugJMST3Server;
   private boolean _DebugJMSWrappers;
   private boolean _DebugJMSXA;
   private boolean _DebugJMX;
   private boolean _DebugJMXCompatibility;
   private boolean _DebugJMXCore;
   private boolean _DebugJMXDomain;
   private boolean _DebugJMXEdit;
   private boolean _DebugJMXRuntime;
   private boolean _DebugJNDI;
   private boolean _DebugJNDIFactories;
   private boolean _DebugJNDIResolution;
   private boolean _DebugJTA2PC;
   private boolean _DebugJTA2PCStackTrace;
   private boolean _DebugJTAAPI;
   private boolean _DebugJTAGateway;
   private boolean _DebugJTAGatewayStackTrace;
   private boolean _DebugJTAHealth;
   private boolean _DebugJTAJDBC;
   private boolean _DebugJTALLR;
   private boolean _DebugJTALifecycle;
   private boolean _DebugJTAMigration;
   private boolean _DebugJTANaming;
   private boolean _DebugJTANamingStackTrace;
   private boolean _DebugJTANonXA;
   private boolean _DebugJTAPropagate;
   private boolean _DebugJTARMI;
   private boolean _DebugJTARecovery;
   private boolean _DebugJTARecoveryStackTrace;
   private boolean _DebugJTAResourceHealth;
   private String _DebugJTAResourceName;
   private boolean _DebugJTATLOG;
   private String _DebugJTATransactionName;
   private boolean _DebugJTAXA;
   private boolean _DebugJTAXAStackTrace;
   private boolean _DebugJpaDataCache;
   private boolean _DebugJpaEnhance;
   private boolean _DebugJpaJdbcJdbc;
   private boolean _DebugJpaJdbcSchema;
   private boolean _DebugJpaJdbcSql;
   private boolean _DebugJpaManage;
   private boolean _DebugJpaMetaData;
   private boolean _DebugJpaProfile;
   private boolean _DebugJpaQuery;
   private boolean _DebugJpaRuntime;
   private boolean _DebugJpaTool;
   private boolean _DebugLeaderElection;
   private boolean _DebugLibraries;
   private boolean _DebugLoggingConfiguration;
   private boolean _DebugManagementServicesResource;
   private String[] _DebugMaskCriterias;
   private boolean _DebugMessagingBridgeDumpToConsole;
   private boolean _DebugMessagingBridgeDumpToLog;
   private boolean _DebugMessagingBridgeRuntime;
   private boolean _DebugMessagingBridgeRuntimeVerbose;
   private boolean _DebugMessagingBridgeStartup;
   private boolean _DebugMessagingKernel;
   private boolean _DebugMessagingKernelBoot;
   private boolean _DebugPathSvc;
   private boolean _DebugPathSvcVerbose;
   private boolean _DebugRA;
   private boolean _DebugRAClassloader;
   private boolean _DebugRAConnEvents;
   private boolean _DebugRAConnections;
   private boolean _DebugRADeployment;
   private boolean _DebugRALifecycle;
   private boolean _DebugRALocalOut;
   private boolean _DebugRAParsing;
   private boolean _DebugRAPoolVerbose;
   private boolean _DebugRAPooling;
   private boolean _DebugRASecurityCtx;
   private boolean _DebugRAWork;
   private boolean _DebugRAWorkEvents;
   private boolean _DebugRAXAin;
   private boolean _DebugRAXAout;
   private boolean _DebugRAXAwork;
   private boolean _DebugReplication;
   private boolean _DebugReplicationDetails;
   private boolean _DebugSAFAdmin;
   private boolean _DebugSAFLifeCycle;
   private boolean _DebugSAFManager;
   private boolean _DebugSAFMessagePath;
   private boolean _DebugSAFReceivingAgent;
   private boolean _DebugSAFSendingAgent;
   private boolean _DebugSAFStore;
   private boolean _DebugSAFTransport;
   private boolean _DebugSAFVerbose;
   private boolean _DebugSNMPAgent;
   private boolean _DebugSNMPExtensionProvider;
   private boolean _DebugSNMPProtocolTCP;
   private boolean _DebugSNMPToolkit;
   private boolean _DebugScaContainer;
   private boolean _DebugSecurity;
   private boolean _DebugSecurityAdjudicator;
   private boolean _DebugSecurityAtn;
   private boolean _DebugSecurityAtz;
   private boolean _DebugSecurityAuditor;
   private boolean _DebugSecurityCertPath;
   private boolean _DebugSecurityCredMap;
   private boolean _DebugSecurityEEngine;
   private boolean _DebugSecurityEncryptionService;
   private boolean _DebugSecurityJACC;
   private boolean _DebugSecurityJACCNonPolicy;
   private boolean _DebugSecurityJACCPolicy;
   private boolean _DebugSecurityKeyStore;
   private boolean _DebugSecurityPasswordPolicy;
   private boolean _DebugSecurityPredicate;
   private boolean _DebugSecurityProfiler;
   private boolean _DebugSecurityRealm;
   private boolean _DebugSecurityRoleMap;
   private boolean _DebugSecuritySAML2Atn;
   private boolean _DebugSecuritySAML2CredMap;
   private boolean _DebugSecuritySAML2Lib;
   private boolean _DebugSecuritySAML2Service;
   private boolean _DebugSecuritySAMLAtn;
   private boolean _DebugSecuritySAMLCredMap;
   private boolean _DebugSecuritySAMLLib;
   private boolean _DebugSecuritySAMLService;
   private boolean _DebugSecuritySSL;
   private boolean _DebugSecuritySSLEaten;
   private boolean _DebugSecurityService;
   private boolean _DebugSecurityUserLockout;
   private boolean _DebugSelfTuning;
   private boolean _DebugServerLifeCycle;
   private boolean _DebugServerMigration;
   private boolean _DebugServerStartStatistics;
   private boolean _DebugStoreAdmin;
   private boolean _DebugStoreIOLogical;
   private boolean _DebugStoreIOLogicalBoot;
   private boolean _DebugStoreIOPhysical;
   private boolean _DebugStoreIOPhysicalVerbose;
   private boolean _DebugStoreXA;
   private boolean _DebugStoreXAVerbose;
   private boolean _DebugTunnelingConnection;
   private boolean _DebugTunnelingConnectionTimeout;
   private boolean _DebugURLResolution;
   private boolean _DebugWANReplicationDetails;
   private boolean _DebugWTCConfig;
   private boolean _DebugWTCCorbaEx;
   private boolean _DebugWTCGwtEx;
   private boolean _DebugWTCJatmiEx;
   private boolean _DebugWTCTDomPdu;
   private boolean _DebugWTCUData;
   private boolean _DebugWTCtBridgeEx;
   private boolean _DebugWebAppIdentityAssertion;
   private boolean _DebugWebAppModule;
   private boolean _DebugWebAppSecurity;
   private int _DebugXMLEntityCacheDebugLevel;
   private String _DebugXMLEntityCacheDebugName;
   private boolean _DebugXMLEntityCacheIncludeClass;
   private boolean _DebugXMLEntityCacheIncludeLocation;
   private boolean _DebugXMLEntityCacheIncludeName;
   private boolean _DebugXMLEntityCacheIncludeTime;
   private OutputStream _DebugXMLEntityCacheOutputStream;
   private boolean _DebugXMLEntityCacheUseShortClass;
   private int _DebugXMLRegistryDebugLevel;
   private String _DebugXMLRegistryDebugName;
   private boolean _DebugXMLRegistryIncludeClass;
   private boolean _DebugXMLRegistryIncludeLocation;
   private boolean _DebugXMLRegistryIncludeName;
   private boolean _DebugXMLRegistryIncludeTime;
   private OutputStream _DebugXMLRegistryOutputStream;
   private boolean _DebugXMLRegistryUseShortClass;
   private boolean _DefaultStore;
   private String _DiagnosticContextDebugMode;
   private boolean _ListenThreadDebug;
   private boolean _MagicThreadDumpBackToSocket;
   private boolean _MagicThreadDumpEnabled;
   private String _MagicThreadDumpFile;
   private String _MagicThreadDumpHost;
   private boolean _MasterDeployer;
   private boolean _RedefiningClassLoader;
   private ServerMBean _Server;
   private boolean _SlaveDeployer;
   private boolean _WebModule;
   private static SchemaHelper2 _schemaHelper;

   public ServerDebugMBeanImpl() {
      this._initializeProperty(-1);
   }

   public ServerDebugMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getDiagnosticContextDebugMode() {
      return this._DiagnosticContextDebugMode;
   }

   public boolean isDiagnosticContextDebugModeSet() {
      return this._isSet(35);
   }

   public void setDiagnosticContextDebugMode(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"Off", "And", "Or"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("DiagnosticContextDebugMode", var1, var2);
      String var3 = this._DiagnosticContextDebugMode;
      this._DiagnosticContextDebugMode = var1;
      this._postSet(35, var3, var1);
   }

   public String[] getDebugMaskCriterias() {
      return this._DebugMaskCriterias;
   }

   public boolean isDebugMaskCriteriasSet() {
      return this._isSet(36);
   }

   public void setDebugMaskCriterias(String[] var1) {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      DiagnosticContextHelper.validateDyeFlagNames(var1);
      String[] var2 = this._DebugMaskCriterias;
      this._DebugMaskCriterias = var1;
      this._postSet(36, var2, var1);
   }

   public ServerMBean getServer() {
      return this._Server;
   }

   public String getServerAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getServer();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isServerSet() {
      return this._isSet(37);
   }

   public void setServerAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, ServerMBean.class, new ReferenceManager.Resolver(this, 37) {
            public void resolveReference(Object var1) {
               try {
                  ServerDebugMBeanImpl.this.setServer((ServerMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         ServerMBean var2 = this._Server;
         this._initializeProperty(37);
         this._postSet(37, var2, this._Server);
      }

   }

   public void setServer(ServerMBean var1) throws InvalidAttributeValueException {
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 37, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return ServerDebugMBeanImpl.this.getServer();
            }
         };
         this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1, var2);
      }

      ServerMBean var3 = this._Server;
      this._Server = var1;
      this._postSet(37, var3, var1);
   }

   public boolean getListenThreadDebug() {
      return this._ListenThreadDebug;
   }

   public boolean isListenThreadDebugSet() {
      return this._isSet(38);
   }

   public void setListenThreadDebug(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._ListenThreadDebug;
      this._ListenThreadDebug = var1;
      this._postSet(38, var2, var1);
   }

   public boolean isMagicThreadDumpEnabled() {
      return this._MagicThreadDumpEnabled;
   }

   public boolean isMagicThreadDumpEnabledSet() {
      return this._isSet(39);
   }

   public void setMagicThreadDumpEnabled(boolean var1) {
      boolean var2 = this._MagicThreadDumpEnabled;
      this._MagicThreadDumpEnabled = var1;
      this._postSet(39, var2, var1);
   }

   public String getMagicThreadDumpHost() {
      return this._MagicThreadDumpHost;
   }

   public boolean isMagicThreadDumpHostSet() {
      return this._isSet(40);
   }

   public void setMagicThreadDumpHost(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("MagicThreadDumpHost", var1);
      String var2 = this._MagicThreadDumpHost;
      this._MagicThreadDumpHost = var1;
      this._postSet(40, var2, var1);
   }

   public String getMagicThreadDumpFile() {
      return this._MagicThreadDumpFile;
   }

   public boolean isMagicThreadDumpFileSet() {
      return this._isSet(41);
   }

   public void setMagicThreadDumpFile(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._MagicThreadDumpFile;
      this._MagicThreadDumpFile = var1;
      this._postSet(41, var2, var1);
   }

   public boolean getMagicThreadDumpBackToSocket() {
      return this._MagicThreadDumpBackToSocket;
   }

   public boolean isMagicThreadDumpBackToSocketSet() {
      return this._isSet(42);
   }

   public void setMagicThreadDumpBackToSocket(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._MagicThreadDumpBackToSocket;
      this._MagicThreadDumpBackToSocket = var1;
      this._postSet(42, var2, var1);
   }

   public void setBugReportServiceWsdlUrl(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._BugReportServiceWsdlUrl;
      this._BugReportServiceWsdlUrl = var1;
      this._postSet(43, var2, var1);
   }

   public String getBugReportServiceWsdlUrl() {
      return this._BugReportServiceWsdlUrl;
   }

   public boolean isBugReportServiceWsdlUrlSet() {
      return this._isSet(43);
   }

   public boolean getDebugAppContainer() {
      return this._DebugAppContainer;
   }

   public boolean isDebugAppContainerSet() {
      return this._isSet(44);
   }

   public void setDebugAppContainer(boolean var1) {
      boolean var2 = this._DebugAppContainer;
      this._DebugAppContainer = var1;
      this._postSet(44, var2, var1);
   }

   public boolean getDebugLibraries() {
      return this._DebugLibraries;
   }

   public boolean isDebugLibrariesSet() {
      return this._isSet(45);
   }

   public void setDebugLibraries(boolean var1) {
      boolean var2 = this._DebugLibraries;
      this._DebugLibraries = var1;
      this._postSet(45, var2, var1);
   }

   public boolean getDebugClassRedef() {
      return this._DebugClassRedef;
   }

   public boolean isDebugClassRedefSet() {
      return this._isSet(46);
   }

   public void setDebugClassRedef(boolean var1) {
      boolean var2 = this._DebugClassRedef;
      this._DebugClassRedef = var1;
      this._postSet(46, var2, var1);
   }

   public boolean getRedefiningClassLoader() {
      return this._RedefiningClassLoader;
   }

   public boolean isRedefiningClassLoaderSet() {
      return this._isSet(47);
   }

   public void setRedefiningClassLoader(boolean var1) {
      boolean var2 = this._RedefiningClassLoader;
      this._RedefiningClassLoader = var1;
      this._postSet(47, var2, var1);
   }

   public boolean getDebugClassSize() {
      return this._DebugClassSize;
   }

   public boolean isDebugClassSizeSet() {
      return this._isSet(48);
   }

   public void setDebugClassSize(boolean var1) {
      boolean var2 = this._DebugClassSize;
      this._DebugClassSize = var1;
      this._postSet(48, var2, var1);
   }

   public boolean getDefaultStore() {
      return this._DefaultStore;
   }

   public boolean isDefaultStoreSet() {
      return this._isSet(49);
   }

   public void setDefaultStore(boolean var1) {
      boolean var2 = this._DefaultStore;
      this._DefaultStore = var1;
      this._postSet(49, var2, var1);
   }

   public boolean getClassChangeNotifier() {
      return this._ClassChangeNotifier;
   }

   public boolean isClassChangeNotifierSet() {
      return this._isSet(50);
   }

   public void setClassChangeNotifier(boolean var1) {
      boolean var2 = this._ClassChangeNotifier;
      this._ClassChangeNotifier = var1;
      this._postSet(50, var2, var1);
   }

   public boolean getDebugHttp() {
      return this._DebugHttp;
   }

   public boolean isDebugHttpSet() {
      return this._isSet(51);
   }

   public void setDebugHttp(boolean var1) {
      boolean var2 = this._DebugHttp;
      this._DebugHttp = var1;
      this._postSet(51, var2, var1);
   }

   public boolean getDebugURLResolution() {
      return this._DebugURLResolution;
   }

   public boolean isDebugURLResolutionSet() {
      return this._isSet(52);
   }

   public void setDebugURLResolution(boolean var1) {
      boolean var2 = this._DebugURLResolution;
      this._DebugURLResolution = var1;
      this._postSet(52, var2, var1);
   }

   public boolean getDebugHttpSessions() {
      return this._DebugHttpSessions;
   }

   public boolean isDebugHttpSessionsSet() {
      return this._isSet(53);
   }

   public void setDebugHttpSessions(boolean var1) {
      boolean var2 = this._DebugHttpSessions;
      this._DebugHttpSessions = var1;
      this._postSet(53, var2, var1);
   }

   public boolean getDebugHttpLogging() {
      return this._DebugHttpLogging;
   }

   public boolean isDebugHttpLoggingSet() {
      return this._isSet(54);
   }

   public void setDebugHttpLogging(boolean var1) {
      boolean var2 = this._DebugHttpLogging;
      this._DebugHttpLogging = var1;
      this._postSet(54, var2, var1);
   }

   public boolean getDebugWebAppIdentityAssertion() {
      return this._DebugWebAppIdentityAssertion;
   }

   public boolean isDebugWebAppIdentityAssertionSet() {
      return this._isSet(55);
   }

   public void setDebugWebAppIdentityAssertion(boolean var1) {
      boolean var2 = this._DebugWebAppIdentityAssertion;
      this._DebugWebAppIdentityAssertion = var1;
      this._postSet(55, var2, var1);
   }

   public boolean getDebugWebAppSecurity() {
      return this._DebugWebAppSecurity;
   }

   public boolean isDebugWebAppSecuritySet() {
      return this._isSet(56);
   }

   public void setDebugWebAppSecurity(boolean var1) {
      boolean var2 = this._DebugWebAppSecurity;
      this._DebugWebAppSecurity = var1;
      this._postSet(56, var2, var1);
   }

   public boolean getDebugWebAppModule() {
      return this._DebugWebAppModule;
   }

   public boolean isDebugWebAppModuleSet() {
      return this._isSet(57);
   }

   public void setDebugWebAppModule(boolean var1) {
      boolean var2 = this._DebugWebAppModule;
      this._DebugWebAppModule = var1;
      this._postSet(57, var2, var1);
   }

   public boolean getDebugEjbCompilation() {
      return this._DebugEjbCompilation;
   }

   public boolean isDebugEjbCompilationSet() {
      return this._isSet(58);
   }

   public void setDebugEjbCompilation(boolean var1) {
      boolean var2 = this._DebugEjbCompilation;
      this._DebugEjbCompilation = var1;
      this._postSet(58, var2, var1);
   }

   public boolean getDebugEjbDeployment() {
      return this._DebugEjbDeployment;
   }

   public boolean isDebugEjbDeploymentSet() {
      return this._isSet(59);
   }

   public void setDebugEjbDeployment(boolean var1) {
      boolean var2 = this._DebugEjbDeployment;
      this._DebugEjbDeployment = var1;
      this._postSet(59, var2, var1);
   }

   public boolean getDebugEjbMdbConnection() {
      return this._DebugEjbMdbConnection;
   }

   public boolean isDebugEjbMdbConnectionSet() {
      return this._isSet(60);
   }

   public void setDebugEjbMdbConnection(boolean var1) {
      boolean var2 = this._DebugEjbMdbConnection;
      this._DebugEjbMdbConnection = var1;
      this._postSet(60, var2, var1);
   }

   public boolean getDebugEjbCaching() {
      return this._DebugEjbCaching;
   }

   public boolean getDebugSelfTuning() {
      return this._DebugSelfTuning;
   }

   public boolean isDebugEjbCachingSet() {
      return this._isSet(61);
   }

   public boolean isDebugSelfTuningSet() {
      return this._isSet(34);
   }

   public void setDebugEjbCaching(boolean var1) {
      boolean var2 = this._DebugEjbCaching;
      this._DebugEjbCaching = var1;
      this._postSet(61, var2, var1);
   }

   public void setDebugSelfTuning(boolean var1) {
      boolean var2 = this._DebugSelfTuning;
      this._DebugSelfTuning = var1;
      this._postSet(34, var2, var1);
   }

   public boolean getDebugEjbSwapping() {
      return this._DebugEjbSwapping;
   }

   public boolean isDebugEjbSwappingSet() {
      return this._isSet(62);
   }

   public void setDebugEjbSwapping(boolean var1) {
      boolean var2 = this._DebugEjbSwapping;
      this._DebugEjbSwapping = var1;
      this._postSet(62, var2, var1);
   }

   public boolean getDebugEjbLocking() {
      return this._DebugEjbLocking;
   }

   public boolean isDebugEjbLockingSet() {
      return this._isSet(63);
   }

   public void setDebugEjbLocking(boolean var1) {
      boolean var2 = this._DebugEjbLocking;
      this._DebugEjbLocking = var1;
      this._postSet(63, var2, var1);
   }

   public boolean getDebugEjbPooling() {
      return this._DebugEjbPooling;
   }

   public boolean isDebugEjbPoolingSet() {
      return this._isSet(64);
   }

   public void setDebugEjbPooling(boolean var1) {
      boolean var2 = this._DebugEjbPooling;
      this._DebugEjbPooling = var1;
      this._postSet(64, var2, var1);
   }

   public boolean getDebugEjbTimers() {
      return this._DebugEjbTimers;
   }

   public boolean isDebugEjbTimersSet() {
      return this._isSet(65);
   }

   public void setDebugEjbTimers(boolean var1) {
      boolean var2 = this._DebugEjbTimers;
      this._DebugEjbTimers = var1;
      this._postSet(65, var2, var1);
   }

   public boolean getDebugEjbInvoke() {
      return this._DebugEjbInvoke;
   }

   public boolean isDebugEjbInvokeSet() {
      return this._isSet(66);
   }

   public void setDebugEjbInvoke(boolean var1) {
      boolean var2 = this._DebugEjbInvoke;
      this._DebugEjbInvoke = var1;
      this._postSet(66, var2, var1);
   }

   public boolean getDebugEjbSecurity() {
      return this._DebugEjbSecurity;
   }

   public boolean isDebugEjbSecuritySet() {
      return this._isSet(67);
   }

   public void setDebugEjbSecurity(boolean var1) {
      boolean var2 = this._DebugEjbSecurity;
      this._DebugEjbSecurity = var1;
      this._postSet(67, var2, var1);
   }

   public boolean getDebugEjbCmpDeployment() {
      return this._DebugEjbCmpDeployment;
   }

   public boolean isDebugEjbCmpDeploymentSet() {
      return this._isSet(68);
   }

   public void setDebugEjbCmpDeployment(boolean var1) {
      boolean var2 = this._DebugEjbCmpDeployment;
      this._DebugEjbCmpDeployment = var1;
      this._postSet(68, var2, var1);
   }

   public boolean getDebugEjbCmpRuntime() {
      return this._DebugEjbCmpRuntime;
   }

   public boolean isDebugEjbCmpRuntimeSet() {
      return this._isSet(69);
   }

   public void setDebugEjbCmpRuntime(boolean var1) {
      boolean var2 = this._DebugEjbCmpRuntime;
      this._DebugEjbCmpRuntime = var1;
      this._postSet(69, var2, var1);
   }

   public boolean getDebugEventManager() {
      return this._DebugEventManager;
   }

   public boolean isDebugEventManagerSet() {
      return this._isSet(70);
   }

   public void setDebugEventManager(boolean var1) {
      boolean var2 = this._DebugEventManager;
      this._DebugEventManager = var1;
      this._postSet(70, var2, var1);
   }

   public boolean getDebugServerMigration() {
      return this._DebugServerMigration;
   }

   public boolean isDebugServerMigrationSet() {
      return this._isSet(71);
   }

   public void setDebugServerMigration(boolean var1) {
      boolean var2 = this._DebugServerMigration;
      this._DebugServerMigration = var1;
      this._postSet(71, var2, var1);
   }

   public boolean getDebugClusterFragments() {
      return this._DebugClusterFragments;
   }

   public boolean isDebugClusterFragmentsSet() {
      return this._isSet(72);
   }

   public void setDebugClusterFragments(boolean var1) {
      boolean var2 = this._DebugClusterFragments;
      this._DebugClusterFragments = var1;
      this._postSet(72, var2, var1);
   }

   public boolean getDebugCluster() {
      return this._DebugCluster;
   }

   public boolean isDebugClusterSet() {
      return this._isSet(73);
   }

   public void setDebugCluster(boolean var1) {
      boolean var2 = this._DebugCluster;
      this._DebugCluster = var1;
      this._postSet(73, var2, var1);
   }

   public boolean getDebugClusterHeartbeats() {
      return this._DebugClusterHeartbeats;
   }

   public boolean isDebugClusterHeartbeatsSet() {
      return this._isSet(74);
   }

   public void setDebugClusterHeartbeats(boolean var1) {
      boolean var2 = this._DebugClusterHeartbeats;
      this._DebugClusterHeartbeats = var1;
      this._postSet(74, var2, var1);
   }

   public boolean getDebugClusterAnnouncements() {
      return this._DebugClusterAnnouncements;
   }

   public boolean isDebugClusterAnnouncementsSet() {
      return this._isSet(75);
   }

   public void setDebugClusterAnnouncements(boolean var1) {
      boolean var2 = this._DebugClusterAnnouncements;
      this._DebugClusterAnnouncements = var1;
      this._postSet(75, var2, var1);
   }

   public boolean getDebugReplication() {
      return this._DebugReplication;
   }

   public boolean isDebugReplicationSet() {
      return this._isSet(76);
   }

   public void setDebugReplication(boolean var1) {
      boolean var2 = this._DebugReplication;
      this._DebugReplication = var1;
      this._postSet(76, var2, var1);
   }

   public boolean getDebugReplicationDetails() {
      return this._DebugReplicationDetails;
   }

   public boolean isDebugReplicationDetailsSet() {
      return this._isSet(77);
   }

   public void setDebugReplicationDetails(boolean var1) {
      boolean var2 = this._DebugReplicationDetails;
      this._DebugReplicationDetails = var1;
      this._postSet(77, var2, var1);
   }

   public boolean getDebugAsyncQueue() {
      return this._DebugAsyncQueue;
   }

   public boolean isDebugAsyncQueueSet() {
      return this._isSet(78);
   }

   public void setDebugAsyncQueue(boolean var1) {
      boolean var2 = this._DebugAsyncQueue;
      this._DebugAsyncQueue = var1;
      this._postSet(78, var2, var1);
   }

   public boolean getDebugLeaderElection() {
      return this._DebugLeaderElection;
   }

   public boolean isDebugLeaderElectionSet() {
      return this._isSet(79);
   }

   public void setDebugLeaderElection(boolean var1) {
      boolean var2 = this._DebugLeaderElection;
      this._DebugLeaderElection = var1;
      this._postSet(79, var2, var1);
   }

   public boolean getDebugDRSCalls() {
      return this._DebugDRSCalls;
   }

   public boolean isDebugDRSCallsSet() {
      return this._isSet(80);
   }

   public void setDebugDRSCalls(boolean var1) {
      boolean var2 = this._DebugDRSCalls;
      this._DebugDRSCalls = var1;
      this._postSet(80, var2, var1);
   }

   public boolean getDebugDRSHeartbeats() {
      return this._DebugDRSHeartbeats;
   }

   public boolean isDebugDRSHeartbeatsSet() {
      return this._isSet(81);
   }

   public void setDebugDRSHeartbeats(boolean var1) {
      boolean var2 = this._DebugDRSHeartbeats;
      this._DebugDRSHeartbeats = var1;
      this._postSet(81, var2, var1);
   }

   public boolean getDebugDRSMessages() {
      return this._DebugDRSMessages;
   }

   public boolean isDebugDRSMessagesSet() {
      return this._isSet(82);
   }

   public void setDebugDRSMessages(boolean var1) {
      boolean var2 = this._DebugDRSMessages;
      this._DebugDRSMessages = var1;
      this._postSet(82, var2, var1);
   }

   public boolean getDebugDRSUpdateStatus() {
      return this._DebugDRSUpdateStatus;
   }

   public boolean isDebugDRSUpdateStatusSet() {
      return this._isSet(83);
   }

   public void setDebugDRSUpdateStatus(boolean var1) {
      boolean var2 = this._DebugDRSUpdateStatus;
      this._DebugDRSUpdateStatus = var1;
      this._postSet(83, var2, var1);
   }

   public boolean getDebugDRSStateTransitions() {
      return this._DebugDRSStateTransitions;
   }

   public boolean isDebugDRSStateTransitionsSet() {
      return this._isSet(84);
   }

   public void setDebugDRSStateTransitions(boolean var1) {
      boolean var2 = this._DebugDRSStateTransitions;
      this._DebugDRSStateTransitions = var1;
      this._postSet(84, var2, var1);
   }

   public boolean getDebugDRSQueues() {
      return this._DebugDRSQueues;
   }

   public boolean isDebugDRSQueuesSet() {
      return this._isSet(85);
   }

   public void setDebugDRSQueues(boolean var1) {
      boolean var2 = this._DebugDRSQueues;
      this._DebugDRSQueues = var1;
      this._postSet(85, var2, var1);
   }

   public boolean getDebugJNDI() {
      return this._DebugJNDI;
   }

   public boolean isDebugJNDISet() {
      return this._isSet(86);
   }

   public void setDebugJNDI(boolean var1) {
      boolean var2 = this._DebugJNDI;
      this._DebugJNDI = var1;
      this._postSet(86, var2, var1);
   }

   public boolean getDebugJNDIResolution() {
      return this._DebugJNDIResolution;
   }

   public boolean isDebugJNDIResolutionSet() {
      return this._isSet(87);
   }

   public void setDebugJNDIResolution(boolean var1) {
      boolean var2 = this._DebugJNDIResolution;
      this._DebugJNDIResolution = var1;
      this._postSet(87, var2, var1);
   }

   public boolean getDebugJNDIFactories() {
      return this._DebugJNDIFactories;
   }

   public boolean isDebugJNDIFactoriesSet() {
      return this._isSet(88);
   }

   public void setDebugJNDIFactories(boolean var1) {
      boolean var2 = this._DebugJNDIFactories;
      this._DebugJNDIFactories = var1;
      this._postSet(88, var2, var1);
   }

   public boolean getDebugTunnelingConnectionTimeout() {
      return this._DebugTunnelingConnectionTimeout;
   }

   public boolean isDebugTunnelingConnectionTimeoutSet() {
      return this._isSet(89);
   }

   public void setDebugTunnelingConnectionTimeout(boolean var1) {
      boolean var2 = this._DebugTunnelingConnectionTimeout;
      this._DebugTunnelingConnectionTimeout = var1;
      this._postSet(89, var2, var1);
   }

   public boolean getDebugTunnelingConnection() {
      return this._DebugTunnelingConnection;
   }

   public boolean isDebugTunnelingConnectionSet() {
      return this._isSet(90);
   }

   public void setDebugTunnelingConnection(boolean var1) {
      boolean var2 = this._DebugTunnelingConnection;
      this._DebugTunnelingConnection = var1;
      this._postSet(90, var2, var1);
   }

   public boolean getDebugJMSBackEnd() {
      return this._DebugJMSBackEnd;
   }

   public boolean isDebugJMSBackEndSet() {
      return this._isSet(91);
   }

   public void setDebugJMSBackEnd(boolean var1) {
      boolean var2 = this._DebugJMSBackEnd;
      this._DebugJMSBackEnd = var1;
      this._postSet(91, var2, var1);
   }

   public boolean getDebugJMSFrontEnd() {
      return this._DebugJMSFrontEnd;
   }

   public boolean isDebugJMSFrontEndSet() {
      return this._isSet(92);
   }

   public void setDebugJMSFrontEnd(boolean var1) {
      boolean var2 = this._DebugJMSFrontEnd;
      this._DebugJMSFrontEnd = var1;
      this._postSet(92, var2, var1);
   }

   public boolean getDebugJMSCommon() {
      return this._DebugJMSCommon;
   }

   public boolean isDebugJMSCommonSet() {
      return this._isSet(93);
   }

   public void setDebugJMSCommon(boolean var1) {
      boolean var2 = this._DebugJMSCommon;
      this._DebugJMSCommon = var1;
      this._postSet(93, var2, var1);
   }

   public boolean getDebugJMSConfig() {
      return this._DebugJMSConfig;
   }

   public boolean isDebugJMSConfigSet() {
      return this._isSet(94);
   }

   public void setDebugJMSConfig(boolean var1) {
      boolean var2 = this._DebugJMSConfig;
      this._DebugJMSConfig = var1;
      this._postSet(94, var2, var1);
   }

   public boolean getDebugJMSDistTopic() {
      return this._DebugJMSDistTopic;
   }

   public boolean isDebugJMSDistTopicSet() {
      return this._isSet(95);
   }

   public void setDebugJMSDistTopic(boolean var1) {
      boolean var2 = this._DebugJMSDistTopic;
      this._DebugJMSDistTopic = var1;
      this._postSet(95, var2, var1);
   }

   public boolean getDebugJMSLocking() {
      return this._DebugJMSLocking;
   }

   public boolean isDebugJMSLockingSet() {
      return this._isSet(96);
   }

   public void setDebugJMSLocking(boolean var1) {
      boolean var2 = this._DebugJMSLocking;
      this._DebugJMSLocking = var1;
      this._postSet(96, var2, var1);
   }

   public boolean getDebugJMSXA() {
      return this._DebugJMSXA;
   }

   public boolean isDebugJMSXASet() {
      return this._isSet(97);
   }

   public void setDebugJMSXA(boolean var1) {
      boolean var2 = this._DebugJMSXA;
      this._DebugJMSXA = var1;
      this._postSet(97, var2, var1);
   }

   public boolean getDebugJMSDispatcher() {
      return this._DebugJMSDispatcher;
   }

   public boolean isDebugJMSDispatcherSet() {
      return this._isSet(98);
   }

   public void setDebugJMSDispatcher(boolean var1) {
      boolean var2 = this._DebugJMSDispatcher;
      this._DebugJMSDispatcher = var1;
      this._postSet(98, var2, var1);
   }

   public boolean getDebugJMSStore() {
      return this._DebugJMSStore;
   }

   public boolean isDebugJMSStoreSet() {
      return this._isSet(99);
   }

   public void setDebugJMSStore(boolean var1) {
      boolean var2 = this._DebugJMSStore;
      this._DebugJMSStore = var1;
      this._postSet(99, var2, var1);
   }

   public boolean getDebugJMSBoot() {
      return this._DebugJMSBoot;
   }

   public boolean isDebugJMSBootSet() {
      return this._isSet(100);
   }

   public void setDebugJMSBoot(boolean var1) {
      boolean var2 = this._DebugJMSBoot;
      this._DebugJMSBoot = var1;
      this._postSet(100, var2, var1);
   }

   public boolean getDebugJMSDurableSubscribers() {
      return this._DebugJMSDurableSubscribers;
   }

   public boolean isDebugJMSDurableSubscribersSet() {
      return this._isSet(101);
   }

   public void setDebugJMSDurableSubscribers(boolean var1) {
      boolean var2 = this._DebugJMSDurableSubscribers;
      this._DebugJMSDurableSubscribers = var1;
      this._postSet(101, var2, var1);
   }

   public boolean getDebugJMSJDBCScavengeOnFlush() {
      return this._DebugJMSJDBCScavengeOnFlush;
   }

   public boolean isDebugJMSJDBCScavengeOnFlushSet() {
      return this._isSet(102);
   }

   public void setDebugJMSJDBCScavengeOnFlush(boolean var1) {
      boolean var2 = this._DebugJMSJDBCScavengeOnFlush;
      this._DebugJMSJDBCScavengeOnFlush = var1;
      this._postSet(102, var2, var1);
   }

   public boolean getDebugJMSAME() {
      return this._DebugJMSAME;
   }

   public boolean isDebugJMSAMESet() {
      return this._isSet(103);
   }

   public void setDebugJMSAME(boolean var1) {
      boolean var2 = this._DebugJMSAME;
      this._DebugJMSAME = var1;
      this._postSet(103, var2, var1);
   }

   public boolean getDebugJMSPauseResume() {
      return this._DebugJMSPauseResume;
   }

   public boolean isDebugJMSPauseResumeSet() {
      return this._isSet(104);
   }

   public void setDebugJMSPauseResume(boolean var1) {
      boolean var2 = this._DebugJMSPauseResume;
      this._DebugJMSPauseResume = var1;
      this._postSet(104, var2, var1);
   }

   public boolean getDebugJMSModule() {
      return this._DebugJMSModule;
   }

   public boolean isDebugJMSModuleSet() {
      return this._isSet(105);
   }

   public void setDebugJMSModule(boolean var1) {
      boolean var2 = this._DebugJMSModule;
      this._DebugJMSModule = var1;
      this._postSet(105, var2, var1);
   }

   public boolean getDebugJMSMessagePath() {
      return this._DebugJMSMessagePath;
   }

   public boolean isDebugJMSMessagePathSet() {
      return this._isSet(106);
   }

   public void setDebugJMSMessagePath(boolean var1) {
      boolean var2 = this._DebugJMSMessagePath;
      this._DebugJMSMessagePath = var1;
      this._postSet(106, var2, var1);
   }

   public boolean getDebugJMSSAF() {
      return this._DebugJMSSAF;
   }

   public boolean isDebugJMSSAFSet() {
      return this._isSet(107);
   }

   public void setDebugJMSSAF(boolean var1) {
      boolean var2 = this._DebugJMSSAF;
      this._DebugJMSSAF = var1;
      this._postSet(107, var2, var1);
   }

   public boolean getDebugJMSWrappers() {
      return this._DebugJMSWrappers;
   }

   public boolean isDebugJMSWrappersSet() {
      return this._isSet(108);
   }

   public void setDebugJMSWrappers(boolean var1) {
      boolean var2 = this._DebugJMSWrappers;
      this._DebugJMSWrappers = var1;
      this._postSet(108, var2, var1);
   }

   public boolean getDebugJMSCDS() {
      return this._DebugJMSCDS;
   }

   public boolean isDebugJMSCDSSet() {
      return this._isSet(109);
   }

   public void setDebugJMSCDS(boolean var1) {
      boolean var2 = this._DebugJMSCDS;
      this._DebugJMSCDS = var1;
      this._postSet(109, var2, var1);
   }

   public boolean getDebugJTAXA() {
      return this._DebugJTAXA;
   }

   public boolean isDebugJTAXASet() {
      return this._isSet(110);
   }

   public void setDebugJTAXA(boolean var1) {
      boolean var2 = this._DebugJTAXA;
      this._DebugJTAXA = var1;
      this._postSet(110, var2, var1);
   }

   public boolean getDebugJTANonXA() {
      return this._DebugJTANonXA;
   }

   public boolean isDebugJTANonXASet() {
      return this._isSet(111);
   }

   public void setDebugJTANonXA(boolean var1) {
      boolean var2 = this._DebugJTANonXA;
      this._DebugJTANonXA = var1;
      this._postSet(111, var2, var1);
   }

   public boolean getDebugJTAXAStackTrace() {
      return this._DebugJTAXAStackTrace;
   }

   public boolean isDebugJTAXAStackTraceSet() {
      return this._isSet(112);
   }

   public void setDebugJTAXAStackTrace(boolean var1) {
      boolean var2 = this._DebugJTAXAStackTrace;
      this._DebugJTAXAStackTrace = var1;
      this._postSet(112, var2, var1);
   }

   public boolean getDebugJTARMI() {
      return this._DebugJTARMI;
   }

   public boolean isDebugJTARMISet() {
      return this._isSet(113);
   }

   public void setDebugJTARMI(boolean var1) {
      boolean var2 = this._DebugJTARMI;
      this._DebugJTARMI = var1;
      this._postSet(113, var2, var1);
   }

   public boolean getDebugJTA2PC() {
      return this._DebugJTA2PC;
   }

   public boolean isDebugJTA2PCSet() {
      return this._isSet(114);
   }

   public void setDebugJTA2PC(boolean var1) {
      boolean var2 = this._DebugJTA2PC;
      this._DebugJTA2PC = var1;
      this._postSet(114, var2, var1);
   }

   public boolean getDebugJTA2PCStackTrace() {
      return this._DebugJTA2PCStackTrace;
   }

   public boolean isDebugJTA2PCStackTraceSet() {
      return this._isSet(115);
   }

   public void setDebugJTA2PCStackTrace(boolean var1) {
      boolean var2 = this._DebugJTA2PCStackTrace;
      this._DebugJTA2PCStackTrace = var1;
      this._postSet(115, var2, var1);
   }

   public boolean getDebugJTATLOG() {
      return this._DebugJTATLOG;
   }

   public boolean isDebugJTATLOGSet() {
      return this._isSet(116);
   }

   public void setDebugJTATLOG(boolean var1) {
      boolean var2 = this._DebugJTATLOG;
      this._DebugJTATLOG = var1;
      this._postSet(116, var2, var1);
   }

   public boolean getDebugJTAJDBC() {
      return this._DebugJTAJDBC;
   }

   public boolean isDebugJTAJDBCSet() {
      return this._isSet(117);
   }

   public void setDebugJTAJDBC(boolean var1) {
      boolean var2 = this._DebugJTAJDBC;
      this._DebugJTAJDBC = var1;
      this._postSet(117, var2, var1);
   }

   public boolean getDebugJTARecovery() {
      return this._DebugJTARecovery;
   }

   public boolean isDebugJTARecoverySet() {
      return this._isSet(118);
   }

   public void setDebugJTARecovery(boolean var1) {
      boolean var2 = this._DebugJTARecovery;
      this._DebugJTARecovery = var1;
      this._postSet(118, var2, var1);
   }

   public boolean getDebugJTARecoveryStackTrace() {
      return this._DebugJTARecoveryStackTrace;
   }

   public boolean isDebugJTARecoveryStackTraceSet() {
      return this._isSet(119);
   }

   public void setDebugJTARecoveryStackTrace(boolean var1) {
      boolean var2 = this._DebugJTARecoveryStackTrace;
      this._DebugJTARecoveryStackTrace = var1;
      this._postSet(119, var2, var1);
   }

   public boolean getDebugJTAAPI() {
      return this._DebugJTAAPI;
   }

   public boolean isDebugJTAAPISet() {
      return this._isSet(120);
   }

   public void setDebugJTAAPI(boolean var1) {
      boolean var2 = this._DebugJTAAPI;
      this._DebugJTAAPI = var1;
      this._postSet(120, var2, var1);
   }

   public boolean getDebugJTAPropagate() {
      return this._DebugJTAPropagate;
   }

   public boolean isDebugJTAPropagateSet() {
      return this._isSet(121);
   }

   public void setDebugJTAPropagate(boolean var1) {
      boolean var2 = this._DebugJTAPropagate;
      this._DebugJTAPropagate = var1;
      this._postSet(121, var2, var1);
   }

   public boolean getDebugJTAGateway() {
      return this._DebugJTAGateway;
   }

   public boolean isDebugJTAGatewaySet() {
      return this._isSet(122);
   }

   public void setDebugJTAGateway(boolean var1) {
      boolean var2 = this._DebugJTAGateway;
      this._DebugJTAGateway = var1;
      this._postSet(122, var2, var1);
   }

   public boolean getDebugJTAGatewayStackTrace() {
      return this._DebugJTAGatewayStackTrace;
   }

   public boolean isDebugJTAGatewayStackTraceSet() {
      return this._isSet(123);
   }

   public void setDebugJTAGatewayStackTrace(boolean var1) {
      boolean var2 = this._DebugJTAGatewayStackTrace;
      this._DebugJTAGatewayStackTrace = var1;
      this._postSet(123, var2, var1);
   }

   public boolean getDebugJTANaming() {
      return this._DebugJTANaming;
   }

   public boolean isDebugJTANamingSet() {
      return this._isSet(124);
   }

   public void setDebugJTANaming(boolean var1) {
      boolean var2 = this._DebugJTANaming;
      this._DebugJTANaming = var1;
      this._postSet(124, var2, var1);
   }

   public boolean getDebugJTANamingStackTrace() {
      return this._DebugJTANamingStackTrace;
   }

   public boolean isDebugJTANamingStackTraceSet() {
      return this._isSet(125);
   }

   public void setDebugJTANamingStackTrace(boolean var1) {
      boolean var2 = this._DebugJTANamingStackTrace;
      this._DebugJTANamingStackTrace = var1;
      this._postSet(125, var2, var1);
   }

   public boolean getDebugJTAResourceHealth() {
      return this._DebugJTAResourceHealth;
   }

   public boolean isDebugJTAResourceHealthSet() {
      return this._isSet(126);
   }

   public void setDebugJTAResourceHealth(boolean var1) {
      boolean var2 = this._DebugJTAResourceHealth;
      this._DebugJTAResourceHealth = var1;
      this._postSet(126, var2, var1);
   }

   public boolean getDebugJTAMigration() {
      return this._DebugJTAMigration;
   }

   public boolean isDebugJTAMigrationSet() {
      return this._isSet(127);
   }

   public void setDebugJTAMigration(boolean var1) {
      boolean var2 = this._DebugJTAMigration;
      this._DebugJTAMigration = var1;
      this._postSet(127, var2, var1);
   }

   public boolean getDebugJTALifecycle() {
      return this._DebugJTALifecycle;
   }

   public boolean isDebugJTALifecycleSet() {
      return this._isSet(128);
   }

   public void setDebugJTALifecycle(boolean var1) {
      boolean var2 = this._DebugJTALifecycle;
      this._DebugJTALifecycle = var1;
      this._postSet(128, var2, var1);
   }

   public boolean getDebugJTALLR() {
      return this._DebugJTALLR;
   }

   public boolean isDebugJTALLRSet() {
      return this._isSet(129);
   }

   public void setDebugJTALLR(boolean var1) {
      boolean var2 = this._DebugJTALLR;
      this._DebugJTALLR = var1;
      this._postSet(129, var2, var1);
   }

   public boolean getDebugJTAHealth() {
      return this._DebugJTAHealth;
   }

   public boolean isDebugJTAHealthSet() {
      return this._isSet(130);
   }

   public void setDebugJTAHealth(boolean var1) {
      boolean var2 = this._DebugJTAHealth;
      this._DebugJTAHealth = var1;
      this._postSet(130, var2, var1);
   }

   public String getDebugJTATransactionName() {
      return this._DebugJTATransactionName;
   }

   public boolean isDebugJTATransactionNameSet() {
      return this._isSet(131);
   }

   public void setDebugJTATransactionName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._DebugJTATransactionName;
      this._DebugJTATransactionName = var1;
      this._postSet(131, var2, var1);
   }

   public String getDebugJTAResourceName() {
      return this._DebugJTAResourceName;
   }

   public boolean isDebugJTAResourceNameSet() {
      return this._isSet(132);
   }

   public void setDebugJTAResourceName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._DebugJTAResourceName;
      this._DebugJTAResourceName = var1;
      this._postSet(132, var2, var1);
   }

   public void setDebugMessagingKernel(boolean var1) {
      boolean var2 = this._DebugMessagingKernel;
      this._DebugMessagingKernel = var1;
      this._postSet(133, var2, var1);
   }

   public boolean getDebugMessagingKernel() {
      return this._DebugMessagingKernel;
   }

   public boolean isDebugMessagingKernelSet() {
      return this._isSet(133);
   }

   public void setDebugMessagingKernelBoot(boolean var1) {
      boolean var2 = this._DebugMessagingKernelBoot;
      this._DebugMessagingKernelBoot = var1;
      this._postSet(134, var2, var1);
   }

   public boolean getDebugMessagingKernelBoot() {
      return this._DebugMessagingKernelBoot;
   }

   public boolean isDebugMessagingKernelBootSet() {
      return this._isSet(134);
   }

   public boolean getDebugSAFLifeCycle() {
      return this._DebugSAFLifeCycle;
   }

   public boolean isDebugSAFLifeCycleSet() {
      return this._isSet(135);
   }

   public void setDebugSAFLifeCycle(boolean var1) {
      boolean var2 = this._DebugSAFLifeCycle;
      this._DebugSAFLifeCycle = var1;
      this._postSet(135, var2, var1);
   }

   public boolean getDebugSAFAdmin() {
      return this._DebugSAFAdmin;
   }

   public boolean isDebugSAFAdminSet() {
      return this._isSet(136);
   }

   public void setDebugSAFAdmin(boolean var1) {
      boolean var2 = this._DebugSAFAdmin;
      this._DebugSAFAdmin = var1;
      this._postSet(136, var2, var1);
   }

   public boolean getDebugSAFManager() {
      return this._DebugSAFManager;
   }

   public boolean isDebugSAFManagerSet() {
      return this._isSet(137);
   }

   public void setDebugSAFManager(boolean var1) {
      boolean var2 = this._DebugSAFManager;
      this._DebugSAFManager = var1;
      this._postSet(137, var2, var1);
   }

   public boolean getDebugSAFSendingAgent() {
      return this._DebugSAFSendingAgent;
   }

   public boolean isDebugSAFSendingAgentSet() {
      return this._isSet(138);
   }

   public void setDebugSAFSendingAgent(boolean var1) {
      boolean var2 = this._DebugSAFSendingAgent;
      this._DebugSAFSendingAgent = var1;
      this._postSet(138, var2, var1);
   }

   public boolean getDebugSAFReceivingAgent() {
      return this._DebugSAFReceivingAgent;
   }

   public boolean isDebugSAFReceivingAgentSet() {
      return this._isSet(139);
   }

   public void setDebugSAFReceivingAgent(boolean var1) {
      boolean var2 = this._DebugSAFReceivingAgent;
      this._DebugSAFReceivingAgent = var1;
      this._postSet(139, var2, var1);
   }

   public boolean getDebugSAFTransport() {
      return this._DebugSAFTransport;
   }

   public boolean isDebugSAFTransportSet() {
      return this._isSet(140);
   }

   public void setDebugSAFTransport(boolean var1) {
      boolean var2 = this._DebugSAFTransport;
      this._DebugSAFTransport = var1;
      this._postSet(140, var2, var1);
   }

   public boolean getDebugSAFMessagePath() {
      return this._DebugSAFMessagePath;
   }

   public boolean isDebugSAFMessagePathSet() {
      return this._isSet(141);
   }

   public void setDebugSAFMessagePath(boolean var1) {
      boolean var2 = this._DebugSAFMessagePath;
      this._DebugSAFMessagePath = var1;
      this._postSet(141, var2, var1);
   }

   public boolean getDebugSAFStore() {
      return this._DebugSAFStore;
   }

   public boolean isDebugSAFStoreSet() {
      return this._isSet(142);
   }

   public void setDebugSAFStore(boolean var1) {
      boolean var2 = this._DebugSAFStore;
      this._DebugSAFStore = var1;
      this._postSet(142, var2, var1);
   }

   public boolean getDebugSAFVerbose() {
      return this._DebugSAFVerbose;
   }

   public boolean isDebugSAFVerboseSet() {
      return this._isSet(143);
   }

   public void setDebugSAFVerbose(boolean var1) {
      boolean var2 = this._DebugSAFVerbose;
      this._DebugSAFVerbose = var1;
      this._postSet(143, var2, var1);
   }

   public void setDebugPathSvc(boolean var1) {
      boolean var2 = this._DebugPathSvc;
      this._DebugPathSvc = var1;
      this._postSet(144, var2, var1);
   }

   public boolean getDebugPathSvc() {
      return this._DebugPathSvc;
   }

   public boolean isDebugPathSvcSet() {
      return this._isSet(144);
   }

   public void setDebugPathSvcVerbose(boolean var1) {
      boolean var2 = this._DebugPathSvcVerbose;
      this._DebugPathSvcVerbose = var1;
      this._postSet(145, var2, var1);
   }

   public boolean getDebugPathSvcVerbose() {
      return this._DebugPathSvcVerbose;
   }

   public boolean isDebugPathSvcVerboseSet() {
      return this._isSet(145);
   }

   public boolean getDebugScaContainer() {
      return this._DebugScaContainer;
   }

   public boolean isDebugScaContainerSet() {
      return this._isSet(146);
   }

   public void setDebugScaContainer(boolean var1) {
      boolean var2 = this._DebugScaContainer;
      this._DebugScaContainer = var1;
      this._postSet(146, var2, var1);
   }

   public boolean getDebugSecurityRealm() {
      return this._DebugSecurityRealm;
   }

   public boolean isDebugSecurityRealmSet() {
      return this._isSet(147);
   }

   public void setDebugSecurityRealm(boolean var1) {
      boolean var2 = this._DebugSecurityRealm;
      this._DebugSecurityRealm = var1;
      this._postSet(147, var2, var1);
   }

   public boolean getDebugSecurity() {
      return this._DebugSecurity;
   }

   public boolean isDebugSecuritySet() {
      return this._isSet(148);
   }

   public void setDebugSecurity(boolean var1) {
      boolean var2 = this._DebugSecurity;
      this._DebugSecurity = var1;
      this._postSet(148, var2, var1);
   }

   public boolean getDebugSecurityPasswordPolicy() {
      return this._DebugSecurityPasswordPolicy;
   }

   public boolean isDebugSecurityPasswordPolicySet() {
      return this._isSet(149);
   }

   public void setDebugSecurityPasswordPolicy(boolean var1) {
      boolean var2 = this._DebugSecurityPasswordPolicy;
      this._DebugSecurityPasswordPolicy = var1;
      this._postSet(149, var2, var1);
   }

   public boolean getDebugSecurityUserLockout() {
      return this._DebugSecurityUserLockout;
   }

   public boolean isDebugSecurityUserLockoutSet() {
      return this._isSet(150);
   }

   public void setDebugSecurityUserLockout(boolean var1) {
      boolean var2 = this._DebugSecurityUserLockout;
      this._DebugSecurityUserLockout = var1;
      this._postSet(150, var2, var1);
   }

   public boolean getDebugSecurityService() {
      return this._DebugSecurityService;
   }

   public boolean isDebugSecurityServiceSet() {
      return this._isSet(151);
   }

   public void setDebugSecurityService(boolean var1) {
      boolean var2 = this._DebugSecurityService;
      this._DebugSecurityService = var1;
      this._postSet(151, var2, var1);
   }

   public boolean getDebugSecurityPredicate() {
      return this._DebugSecurityPredicate;
   }

   public boolean isDebugSecurityPredicateSet() {
      return this._isSet(152);
   }

   public void setDebugSecurityPredicate(boolean var1) {
      boolean var2 = this._DebugSecurityPredicate;
      this._DebugSecurityPredicate = var1;
      this._postSet(152, var2, var1);
   }

   public boolean getDebugSecuritySSL() {
      return this._DebugSecuritySSL;
   }

   public boolean isDebugSecuritySSLSet() {
      return this._isSet(153);
   }

   public void setDebugSecuritySSL(boolean var1) {
      boolean var2 = this._DebugSecuritySSL;
      this._DebugSecuritySSL = var1;
      this._postSet(153, var2, var1);
   }

   public boolean getDebugSecuritySSLEaten() {
      return this._DebugSecuritySSLEaten;
   }

   public boolean isDebugSecuritySSLEatenSet() {
      return this._isSet(154);
   }

   public void setDebugSecuritySSLEaten(boolean var1) {
      boolean var2 = this._DebugSecuritySSLEaten;
      this._DebugSecuritySSLEaten = var1;
      this._postSet(154, var2, var1);
   }

   public boolean getDebugCertRevocCheck() {
      return this._DebugCertRevocCheck;
   }

   public boolean isDebugCertRevocCheckSet() {
      return this._isSet(155);
   }

   public void setDebugCertRevocCheck(boolean var1) {
      boolean var2 = this._DebugCertRevocCheck;
      this._DebugCertRevocCheck = var1;
      this._postSet(155, var2, var1);
   }

   public boolean getDebugEmbeddedLDAP() {
      return this._DebugEmbeddedLDAP;
   }

   public boolean isDebugEmbeddedLDAPSet() {
      return this._isSet(156);
   }

   public void setDebugEmbeddedLDAP(boolean var1) {
      boolean var2 = this._DebugEmbeddedLDAP;
      this._DebugEmbeddedLDAP = var1;
      this._postSet(156, var2, var1);
   }

   public boolean getDebugEmbeddedLDAPLogToConsole() {
      return this._DebugEmbeddedLDAPLogToConsole;
   }

   public boolean isDebugEmbeddedLDAPLogToConsoleSet() {
      return this._isSet(157);
   }

   public void setDebugEmbeddedLDAPLogToConsole(boolean var1) {
      boolean var2 = this._DebugEmbeddedLDAPLogToConsole;
      this._DebugEmbeddedLDAPLogToConsole = var1;
      this._postSet(157, var2, var1);
   }

   public int getDebugEmbeddedLDAPLogLevel() {
      return this._DebugEmbeddedLDAPLogLevel;
   }

   public boolean isDebugEmbeddedLDAPLogLevelSet() {
      return this._isSet(158);
   }

   public void setDebugEmbeddedLDAPLogLevel(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("DebugEmbeddedLDAPLogLevel", (long)var1, 0L, 11L);
      int var2 = this._DebugEmbeddedLDAPLogLevel;
      this._DebugEmbeddedLDAPLogLevel = var1;
      this._postSet(158, var2, var1);
   }

   public boolean getDebugEmbeddedLDAPWriteOverrideProps() {
      return this._DebugEmbeddedLDAPWriteOverrideProps;
   }

   public boolean isDebugEmbeddedLDAPWriteOverridePropsSet() {
      return this._isSet(159);
   }

   public void setDebugEmbeddedLDAPWriteOverrideProps(boolean var1) {
      boolean var2 = this._DebugEmbeddedLDAPWriteOverrideProps;
      this._DebugEmbeddedLDAPWriteOverrideProps = var1;
      this._postSet(159, var2, var1);
   }

   public boolean getDebugSecurityAdjudicator() {
      return this._DebugSecurityAdjudicator;
   }

   public boolean isDebugSecurityAdjudicatorSet() {
      return this._isSet(160);
   }

   public void setDebugSecurityAdjudicator(boolean var1) {
      boolean var2 = this._DebugSecurityAdjudicator;
      this._DebugSecurityAdjudicator = var1;
      this._postSet(160, var2, var1);
   }

   public boolean getDebugSecurityAtn() {
      return this._DebugSecurityAtn;
   }

   public boolean isDebugSecurityAtnSet() {
      return this._isSet(161);
   }

   public void setDebugSecurityAtn(boolean var1) {
      boolean var2 = this._DebugSecurityAtn;
      this._DebugSecurityAtn = var1;
      this._postSet(161, var2, var1);
   }

   public boolean getDebugSecurityAtz() {
      return this._DebugSecurityAtz;
   }

   public boolean isDebugSecurityAtzSet() {
      return this._isSet(162);
   }

   public void setDebugSecurityAtz(boolean var1) {
      boolean var2 = this._DebugSecurityAtz;
      this._DebugSecurityAtz = var1;
      this._postSet(162, var2, var1);
   }

   public boolean getDebugSecurityAuditor() {
      return this._DebugSecurityAuditor;
   }

   public boolean isDebugSecurityAuditorSet() {
      return this._isSet(163);
   }

   public void setDebugSecurityAuditor(boolean var1) {
      boolean var2 = this._DebugSecurityAuditor;
      this._DebugSecurityAuditor = var1;
      this._postSet(163, var2, var1);
   }

   public boolean getDebugSecurityCredMap() {
      return this._DebugSecurityCredMap;
   }

   public boolean isDebugSecurityCredMapSet() {
      return this._isSet(164);
   }

   public void setDebugSecurityCredMap(boolean var1) {
      boolean var2 = this._DebugSecurityCredMap;
      this._DebugSecurityCredMap = var1;
      this._postSet(164, var2, var1);
   }

   public boolean getDebugSecurityEncryptionService() {
      return this._DebugSecurityEncryptionService;
   }

   public boolean isDebugSecurityEncryptionServiceSet() {
      return this._isSet(165);
   }

   public void setDebugSecurityEncryptionService(boolean var1) {
      boolean var2 = this._DebugSecurityEncryptionService;
      this._DebugSecurityEncryptionService = var1;
      this._postSet(165, var2, var1);
   }

   public boolean getDebugSecurityKeyStore() {
      return this._DebugSecurityKeyStore;
   }

   public boolean isDebugSecurityKeyStoreSet() {
      return this._isSet(166);
   }

   public void setDebugSecurityKeyStore(boolean var1) {
      boolean var2 = this._DebugSecurityKeyStore;
      this._DebugSecurityKeyStore = var1;
      this._postSet(166, var2, var1);
   }

   public boolean getDebugSecurityCertPath() {
      return this._DebugSecurityCertPath;
   }

   public boolean isDebugSecurityCertPathSet() {
      return this._isSet(167);
   }

   public void setDebugSecurityCertPath(boolean var1) {
      boolean var2 = this._DebugSecurityCertPath;
      this._DebugSecurityCertPath = var1;
      this._postSet(167, var2, var1);
   }

   public boolean getDebugSecurityProfiler() {
      return this._DebugSecurityProfiler;
   }

   public boolean isDebugSecurityProfilerSet() {
      return this._isSet(168);
   }

   public void setDebugSecurityProfiler(boolean var1) {
      boolean var2 = this._DebugSecurityProfiler;
      this._DebugSecurityProfiler = var1;
      this._postSet(168, var2, var1);
   }

   public boolean getDebugSecurityRoleMap() {
      return this._DebugSecurityRoleMap;
   }

   public boolean isDebugSecurityRoleMapSet() {
      return this._isSet(169);
   }

   public void setDebugSecurityRoleMap(boolean var1) {
      boolean var2 = this._DebugSecurityRoleMap;
      this._DebugSecurityRoleMap = var1;
      this._postSet(169, var2, var1);
   }

   public boolean getDebugSecurityEEngine() {
      return this._DebugSecurityEEngine;
   }

   public boolean isDebugSecurityEEngineSet() {
      return this._isSet(170);
   }

   public void setDebugSecurityEEngine(boolean var1) {
      boolean var2 = this._DebugSecurityEEngine;
      this._DebugSecurityEEngine = var1;
      this._postSet(170, var2, var1);
   }

   public boolean getDebugSecurityJACC() {
      return this._DebugSecurityJACC;
   }

   public boolean isDebugSecurityJACCSet() {
      return this._isSet(171);
   }

   public void setDebugSecurityJACC(boolean var1) {
      boolean var2 = this._DebugSecurityJACC;
      this._DebugSecurityJACC = var1;
      this._postSet(171, var2, var1);
   }

   public boolean getDebugSecurityJACCNonPolicy() {
      return this._DebugSecurityJACCNonPolicy;
   }

   public boolean isDebugSecurityJACCNonPolicySet() {
      return this._isSet(172);
   }

   public void setDebugSecurityJACCNonPolicy(boolean var1) {
      boolean var2 = this._DebugSecurityJACCNonPolicy;
      this._DebugSecurityJACCNonPolicy = var1;
      this._postSet(172, var2, var1);
   }

   public boolean getDebugSecurityJACCPolicy() {
      return this._DebugSecurityJACCPolicy;
   }

   public boolean isDebugSecurityJACCPolicySet() {
      return this._isSet(173);
   }

   public void setDebugSecurityJACCPolicy(boolean var1) {
      boolean var2 = this._DebugSecurityJACCPolicy;
      this._DebugSecurityJACCPolicy = var1;
      this._postSet(173, var2, var1);
   }

   public boolean getDebugSecuritySAMLLib() {
      return this._DebugSecuritySAMLLib;
   }

   public boolean isDebugSecuritySAMLLibSet() {
      return this._isSet(174);
   }

   public void setDebugSecuritySAMLLib(boolean var1) {
      boolean var2 = this._DebugSecuritySAMLLib;
      this._DebugSecuritySAMLLib = var1;
      this._postSet(174, var2, var1);
   }

   public boolean getDebugSecuritySAMLAtn() {
      return this._DebugSecuritySAMLAtn;
   }

   public boolean isDebugSecuritySAMLAtnSet() {
      return this._isSet(175);
   }

   public void setDebugSecuritySAMLAtn(boolean var1) {
      boolean var2 = this._DebugSecuritySAMLAtn;
      this._DebugSecuritySAMLAtn = var1;
      this._postSet(175, var2, var1);
   }

   public boolean getDebugSecuritySAMLCredMap() {
      return this._DebugSecuritySAMLCredMap;
   }

   public boolean isDebugSecuritySAMLCredMapSet() {
      return this._isSet(176);
   }

   public void setDebugSecuritySAMLCredMap(boolean var1) {
      boolean var2 = this._DebugSecuritySAMLCredMap;
      this._DebugSecuritySAMLCredMap = var1;
      this._postSet(176, var2, var1);
   }

   public boolean getDebugSecuritySAMLService() {
      return this._DebugSecuritySAMLService;
   }

   public boolean isDebugSecuritySAMLServiceSet() {
      return this._isSet(177);
   }

   public void setDebugSecuritySAMLService(boolean var1) {
      boolean var2 = this._DebugSecuritySAMLService;
      this._DebugSecuritySAMLService = var1;
      this._postSet(177, var2, var1);
   }

   public boolean getDebugSecuritySAML2Lib() {
      return this._DebugSecuritySAML2Lib;
   }

   public boolean isDebugSecuritySAML2LibSet() {
      return this._isSet(178);
   }

   public void setDebugSecuritySAML2Lib(boolean var1) {
      boolean var2 = this._DebugSecuritySAML2Lib;
      this._DebugSecuritySAML2Lib = var1;
      this._postSet(178, var2, var1);
   }

   public boolean getDebugSecuritySAML2Atn() {
      return this._DebugSecuritySAML2Atn;
   }

   public boolean isDebugSecuritySAML2AtnSet() {
      return this._isSet(179);
   }

   public void setDebugSecuritySAML2Atn(boolean var1) {
      boolean var2 = this._DebugSecuritySAML2Atn;
      this._DebugSecuritySAML2Atn = var1;
      this._postSet(179, var2, var1);
   }

   public boolean getDebugSecuritySAML2CredMap() {
      return this._DebugSecuritySAML2CredMap;
   }

   public boolean isDebugSecuritySAML2CredMapSet() {
      return this._isSet(180);
   }

   public void setDebugSecuritySAML2CredMap(boolean var1) {
      boolean var2 = this._DebugSecuritySAML2CredMap;
      this._DebugSecuritySAML2CredMap = var1;
      this._postSet(180, var2, var1);
   }

   public boolean getDebugSecuritySAML2Service() {
      return this._DebugSecuritySAML2Service;
   }

   public boolean isDebugSecuritySAML2ServiceSet() {
      return this._isSet(181);
   }

   public void setDebugSecuritySAML2Service(boolean var1) {
      boolean var2 = this._DebugSecuritySAML2Service;
      this._DebugSecuritySAML2Service = var1;
      this._postSet(181, var2, var1);
   }

   public boolean getDebugJDBCConn() {
      return this._DebugJDBCConn;
   }

   public boolean isDebugJDBCConnSet() {
      return this._isSet(182);
   }

   public void setDebugJDBCConn(boolean var1) {
      boolean var2 = this._DebugJDBCConn;
      this._DebugJDBCConn = var1;
      this._postSet(182, var2, var1);
   }

   public boolean getDebugJDBCSQL() {
      return this._DebugJDBCSQL;
   }

   public boolean isDebugJDBCSQLSet() {
      return this._isSet(183);
   }

   public void setDebugJDBCSQL(boolean var1) {
      boolean var2 = this._DebugJDBCSQL;
      this._DebugJDBCSQL = var1;
      this._postSet(183, var2, var1);
   }

   public boolean getDebugJDBCRMI() {
      return this._DebugJDBCRMI;
   }

   public boolean isDebugJDBCRMISet() {
      return this._isSet(184);
   }

   public void setDebugJDBCRMI(boolean var1) {
      boolean var2 = this._DebugJDBCRMI;
      this._DebugJDBCRMI = var1;
      this._postSet(184, var2, var1);
   }

   public boolean getDebugJDBCDriverLogging() {
      return this._DebugJDBCDriverLogging;
   }

   public boolean isDebugJDBCDriverLoggingSet() {
      return this._isSet(185);
   }

   public void setDebugJDBCDriverLogging(boolean var1) {
      boolean var2 = this._DebugJDBCDriverLogging;
      this._DebugJDBCDriverLogging = var1;
      this._postSet(185, var2, var1);
   }

   public boolean getDebugJDBCInternal() {
      return this._DebugJDBCInternal;
   }

   public boolean isDebugJDBCInternalSet() {
      return this._isSet(186);
   }

   public void setDebugJDBCInternal(boolean var1) {
      boolean var2 = this._DebugJDBCInternal;
      this._DebugJDBCInternal = var1;
      this._postSet(186, var2, var1);
   }

   public boolean getDebugJDBCRAC() {
      return this._DebugJDBCRAC;
   }

   public boolean isDebugJDBCRACSet() {
      return this._isSet(187);
   }

   public void setDebugJDBCRAC(boolean var1) {
      boolean var2 = this._DebugJDBCRAC;
      this._DebugJDBCRAC = var1;
      this._postSet(187, var2, var1);
   }

   public boolean getDebugJDBCONS() {
      return this._DebugJDBCONS;
   }

   public boolean isDebugJDBCONSSet() {
      return this._isSet(188);
   }

   public void setDebugJDBCONS(boolean var1) {
      boolean var2 = this._DebugJDBCONS;
      this._DebugJDBCONS = var1;
      this._postSet(188, var2, var1);
   }

   public boolean getDebugJDBCUCP() {
      return this._DebugJDBCUCP;
   }

   public boolean isDebugJDBCUCPSet() {
      return this._isSet(189);
   }

   public void setDebugJDBCUCP(boolean var1) {
      boolean var2 = this._DebugJDBCUCP;
      this._DebugJDBCUCP = var1;
      this._postSet(189, var2, var1);
   }

   public boolean getDebugJDBCREPLAY() {
      return this._DebugJDBCREPLAY;
   }

   public boolean isDebugJDBCREPLAYSet() {
      return this._isSet(190);
   }

   public void setDebugJDBCREPLAY(boolean var1) {
      boolean var2 = this._DebugJDBCREPLAY;
      this._DebugJDBCREPLAY = var1;
      this._postSet(190, var2, var1);
   }

   public boolean getDebugMessagingBridgeStartup() {
      return this._DebugMessagingBridgeStartup;
   }

   public boolean isDebugMessagingBridgeStartupSet() {
      return this._isSet(191);
   }

   public void setDebugMessagingBridgeStartup(boolean var1) {
      boolean var2 = this._DebugMessagingBridgeStartup;
      this._DebugMessagingBridgeStartup = var1;
      this._postSet(191, var2, var1);
   }

   public boolean getDebugMessagingBridgeRuntime() {
      return this._DebugMessagingBridgeRuntime;
   }

   public boolean isDebugMessagingBridgeRuntimeSet() {
      return this._isSet(192);
   }

   public void setDebugMessagingBridgeRuntime(boolean var1) {
      boolean var2 = this._DebugMessagingBridgeRuntime;
      this._DebugMessagingBridgeRuntime = var1;
      this._postSet(192, var2, var1);
   }

   public boolean getDebugMessagingBridgeRuntimeVerbose() {
      return this._DebugMessagingBridgeRuntimeVerbose;
   }

   public boolean isDebugMessagingBridgeRuntimeVerboseSet() {
      return this._isSet(193);
   }

   public void setDebugMessagingBridgeRuntimeVerbose(boolean var1) {
      boolean var2 = this._DebugMessagingBridgeRuntimeVerbose;
      this._DebugMessagingBridgeRuntimeVerbose = var1;
      this._postSet(193, var2, var1);
   }

   public boolean getDebugMessagingBridgeDumpToLog() {
      return this._DebugMessagingBridgeDumpToLog;
   }

   public boolean isDebugMessagingBridgeDumpToLogSet() {
      return this._isSet(194);
   }

   public void setDebugMessagingBridgeDumpToLog(boolean var1) {
      boolean var2 = this._DebugMessagingBridgeDumpToLog;
      this._DebugMessagingBridgeDumpToLog = var1;
      this._postSet(194, var2, var1);
   }

   public boolean getDebugMessagingBridgeDumpToConsole() {
      return this._DebugMessagingBridgeDumpToConsole;
   }

   public boolean isDebugMessagingBridgeDumpToConsoleSet() {
      return this._isSet(195);
   }

   public void setDebugMessagingBridgeDumpToConsole(boolean var1) {
      boolean var2 = this._DebugMessagingBridgeDumpToConsole;
      this._DebugMessagingBridgeDumpToConsole = var1;
      this._postSet(195, var2, var1);
   }

   public void setDebugStoreIOLogical(boolean var1) {
      boolean var2 = this._DebugStoreIOLogical;
      this._DebugStoreIOLogical = var1;
      this._postSet(196, var2, var1);
   }

   public boolean getDebugStoreIOLogical() {
      return this._DebugStoreIOLogical;
   }

   public boolean isDebugStoreIOLogicalSet() {
      return this._isSet(196);
   }

   public void setDebugStoreIOLogicalBoot(boolean var1) {
      boolean var2 = this._DebugStoreIOLogicalBoot;
      this._DebugStoreIOLogicalBoot = var1;
      this._postSet(197, var2, var1);
   }

   public boolean getDebugStoreIOLogicalBoot() {
      return this._DebugStoreIOLogicalBoot;
   }

   public boolean isDebugStoreIOLogicalBootSet() {
      return this._isSet(197);
   }

   public void setDebugStoreIOPhysical(boolean var1) {
      boolean var2 = this._DebugStoreIOPhysical;
      this._DebugStoreIOPhysical = var1;
      this._postSet(198, var2, var1);
   }

   public boolean getDebugStoreIOPhysical() {
      return this._DebugStoreIOPhysical;
   }

   public boolean isDebugStoreIOPhysicalSet() {
      return this._isSet(198);
   }

   public void setDebugStoreIOPhysicalVerbose(boolean var1) {
      boolean var2 = this._DebugStoreIOPhysicalVerbose;
      this._DebugStoreIOPhysicalVerbose = var1;
      this._postSet(199, var2, var1);
   }

   public boolean getDebugStoreIOPhysicalVerbose() {
      return this._DebugStoreIOPhysicalVerbose;
   }

   public boolean isDebugStoreIOPhysicalVerboseSet() {
      return this._isSet(199);
   }

   public void setDebugStoreXA(boolean var1) {
      boolean var2 = this._DebugStoreXA;
      this._DebugStoreXA = var1;
      this._postSet(200, var2, var1);
   }

   public boolean getDebugStoreXA() {
      return this._DebugStoreXA;
   }

   public boolean isDebugStoreXASet() {
      return this._isSet(200);
   }

   public void setDebugStoreXAVerbose(boolean var1) {
      boolean var2 = this._DebugStoreXAVerbose;
      this._DebugStoreXAVerbose = var1;
      this._postSet(201, var2, var1);
   }

   public boolean getDebugStoreXAVerbose() {
      return this._DebugStoreXAVerbose;
   }

   public boolean isDebugStoreXAVerboseSet() {
      return this._isSet(201);
   }

   public void setDebugStoreAdmin(boolean var1) {
      boolean var2 = this._DebugStoreAdmin;
      this._DebugStoreAdmin = var1;
      this._postSet(202, var2, var1);
   }

   public boolean getDebugStoreAdmin() {
      return this._DebugStoreAdmin;
   }

   public boolean isDebugStoreAdminSet() {
      return this._isSet(202);
   }

   public int getDebugXMLRegistryDebugLevel() {
      return this._DebugXMLRegistryDebugLevel;
   }

   public boolean isDebugXMLRegistryDebugLevelSet() {
      return this._isSet(203);
   }

   public void setDebugXMLRegistryDebugLevel(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("DebugXMLRegistryDebugLevel", (long)var1, 0L, 3L);
      int var2 = this._DebugXMLRegistryDebugLevel;
      this._DebugXMLRegistryDebugLevel = var1;
      this._postSet(203, var2, var1);
   }

   public String getDebugXMLRegistryDebugName() {
      return this._DebugXMLRegistryDebugName;
   }

   public boolean isDebugXMLRegistryDebugNameSet() {
      return this._isSet(204);
   }

   public void setDebugXMLRegistryDebugName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._DebugXMLRegistryDebugName;
      this._DebugXMLRegistryDebugName = var1;
      this._postSet(204, var2, var1);
   }

   public OutputStream getDebugXMLRegistryOutputStream() {
      return this._DebugXMLRegistryOutputStream;
   }

   public boolean isDebugXMLRegistryOutputStreamSet() {
      return this._isSet(205);
   }

   public void setDebugXMLRegistryOutputStream(OutputStream var1) {
      this._DebugXMLRegistryOutputStream = var1;
   }

   public boolean getDebugXMLRegistryIncludeTime() {
      return this._DebugXMLRegistryIncludeTime;
   }

   public boolean isDebugXMLRegistryIncludeTimeSet() {
      return this._isSet(206);
   }

   public void setDebugXMLRegistryIncludeTime(boolean var1) {
      boolean var2 = this._DebugXMLRegistryIncludeTime;
      this._DebugXMLRegistryIncludeTime = var1;
      this._postSet(206, var2, var1);
   }

   public boolean getDebugXMLRegistryIncludeName() {
      return this._DebugXMLRegistryIncludeName;
   }

   public boolean isDebugXMLRegistryIncludeNameSet() {
      return this._isSet(207);
   }

   public void setDebugXMLRegistryIncludeName(boolean var1) {
      boolean var2 = this._DebugXMLRegistryIncludeName;
      this._DebugXMLRegistryIncludeName = var1;
      this._postSet(207, var2, var1);
   }

   public boolean getDebugXMLRegistryIncludeClass() {
      return this._DebugXMLRegistryIncludeClass;
   }

   public boolean isDebugXMLRegistryIncludeClassSet() {
      return this._isSet(208);
   }

   public void setDebugXMLRegistryIncludeClass(boolean var1) {
      boolean var2 = this._DebugXMLRegistryIncludeClass;
      this._DebugXMLRegistryIncludeClass = var1;
      this._postSet(208, var2, var1);
   }

   public boolean getDebugXMLRegistryIncludeLocation() {
      return this._DebugXMLRegistryIncludeLocation;
   }

   public boolean isDebugXMLRegistryIncludeLocationSet() {
      return this._isSet(209);
   }

   public void setDebugXMLRegistryIncludeLocation(boolean var1) {
      boolean var2 = this._DebugXMLRegistryIncludeLocation;
      this._DebugXMLRegistryIncludeLocation = var1;
      this._postSet(209, var2, var1);
   }

   public boolean getDebugXMLRegistryUseShortClass() {
      return this._DebugXMLRegistryUseShortClass;
   }

   public boolean isDebugXMLRegistryUseShortClassSet() {
      return this._isSet(210);
   }

   public void setDebugXMLRegistryUseShortClass(boolean var1) {
      boolean var2 = this._DebugXMLRegistryUseShortClass;
      this._DebugXMLRegistryUseShortClass = var1;
      this._postSet(210, var2, var1);
   }

   public int getDebugJAXPDebugLevel() {
      return this._DebugJAXPDebugLevel;
   }

   public boolean isDebugJAXPDebugLevelSet() {
      return this._isSet(211);
   }

   public void setDebugJAXPDebugLevel(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("DebugJAXPDebugLevel", (long)var1, 0L, 3L);
      int var2 = this._DebugJAXPDebugLevel;
      this._DebugJAXPDebugLevel = var1;
      this._postSet(211, var2, var1);
   }

   public String getDebugJAXPDebugName() {
      return this._DebugJAXPDebugName;
   }

   public boolean isDebugJAXPDebugNameSet() {
      return this._isSet(212);
   }

   public void setDebugJAXPDebugName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._DebugJAXPDebugName;
      this._DebugJAXPDebugName = var1;
      this._postSet(212, var2, var1);
   }

   public OutputStream getDebugJAXPOutputStream() {
      return this._DebugJAXPOutputStream;
   }

   public boolean isDebugJAXPOutputStreamSet() {
      return this._isSet(213);
   }

   public void setDebugJAXPOutputStream(OutputStream var1) {
      this._DebugJAXPOutputStream = var1;
   }

   public boolean getDebugJAXPIncludeTime() {
      return this._DebugJAXPIncludeTime;
   }

   public boolean isDebugJAXPIncludeTimeSet() {
      return this._isSet(214);
   }

   public void setDebugJAXPIncludeTime(boolean var1) {
      boolean var2 = this._DebugJAXPIncludeTime;
      this._DebugJAXPIncludeTime = var1;
      this._postSet(214, var2, var1);
   }

   public boolean getDebugJAXPIncludeName() {
      return this._DebugJAXPIncludeName;
   }

   public boolean isDebugJAXPIncludeNameSet() {
      return this._isSet(215);
   }

   public void setDebugJAXPIncludeName(boolean var1) {
      boolean var2 = this._DebugJAXPIncludeName;
      this._DebugJAXPIncludeName = var1;
      this._postSet(215, var2, var1);
   }

   public boolean getDebugJAXPIncludeClass() {
      return this._DebugJAXPIncludeClass;
   }

   public boolean isDebugJAXPIncludeClassSet() {
      return this._isSet(216);
   }

   public void setDebugJAXPIncludeClass(boolean var1) {
      boolean var2 = this._DebugJAXPIncludeClass;
      this._DebugJAXPIncludeClass = var1;
      this._postSet(216, var2, var1);
   }

   public boolean getDebugJAXPIncludeLocation() {
      return this._DebugJAXPIncludeLocation;
   }

   public boolean isDebugJAXPIncludeLocationSet() {
      return this._isSet(217);
   }

   public void setDebugJAXPIncludeLocation(boolean var1) {
      boolean var2 = this._DebugJAXPIncludeLocation;
      this._DebugJAXPIncludeLocation = var1;
      this._postSet(217, var2, var1);
   }

   public boolean getDebugJAXPUseShortClass() {
      return this._DebugJAXPUseShortClass;
   }

   public boolean isDebugJAXPUseShortClassSet() {
      return this._isSet(218);
   }

   public void setDebugJAXPUseShortClass(boolean var1) {
      boolean var2 = this._DebugJAXPUseShortClass;
      this._DebugJAXPUseShortClass = var1;
      this._postSet(218, var2, var1);
   }

   public int getDebugXMLEntityCacheDebugLevel() {
      return this._DebugXMLEntityCacheDebugLevel;
   }

   public boolean isDebugXMLEntityCacheDebugLevelSet() {
      return this._isSet(219);
   }

   public void setDebugXMLEntityCacheDebugLevel(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("DebugXMLEntityCacheDebugLevel", (long)var1, 0L, 3L);
      int var2 = this._DebugXMLEntityCacheDebugLevel;
      this._DebugXMLEntityCacheDebugLevel = var1;
      this._postSet(219, var2, var1);
   }

   public String getDebugXMLEntityCacheDebugName() {
      return this._DebugXMLEntityCacheDebugName;
   }

   public boolean isDebugXMLEntityCacheDebugNameSet() {
      return this._isSet(220);
   }

   public void setDebugXMLEntityCacheDebugName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._DebugXMLEntityCacheDebugName;
      this._DebugXMLEntityCacheDebugName = var1;
      this._postSet(220, var2, var1);
   }

   public OutputStream getDebugXMLEntityCacheOutputStream() {
      return this._DebugXMLEntityCacheOutputStream;
   }

   public boolean isDebugXMLEntityCacheOutputStreamSet() {
      return this._isSet(221);
   }

   public void setDebugXMLEntityCacheOutputStream(OutputStream var1) {
      this._DebugXMLEntityCacheOutputStream = var1;
   }

   public boolean getDebugXMLEntityCacheIncludeTime() {
      return this._DebugXMLEntityCacheIncludeTime;
   }

   public boolean isDebugXMLEntityCacheIncludeTimeSet() {
      return this._isSet(222);
   }

   public void setDebugXMLEntityCacheIncludeTime(boolean var1) {
      boolean var2 = this._DebugXMLEntityCacheIncludeTime;
      this._DebugXMLEntityCacheIncludeTime = var1;
      this._postSet(222, var2, var1);
   }

   public boolean getDebugXMLEntityCacheIncludeName() {
      return this._DebugXMLEntityCacheIncludeName;
   }

   public boolean isDebugXMLEntityCacheIncludeNameSet() {
      return this._isSet(223);
   }

   public void setDebugXMLEntityCacheIncludeName(boolean var1) {
      boolean var2 = this._DebugXMLEntityCacheIncludeName;
      this._DebugXMLEntityCacheIncludeName = var1;
      this._postSet(223, var2, var1);
   }

   public boolean getDebugXMLEntityCacheIncludeClass() {
      return this._DebugXMLEntityCacheIncludeClass;
   }

   public boolean isDebugXMLEntityCacheIncludeClassSet() {
      return this._isSet(224);
   }

   public void setDebugXMLEntityCacheIncludeClass(boolean var1) {
      boolean var2 = this._DebugXMLEntityCacheIncludeClass;
      this._DebugXMLEntityCacheIncludeClass = var1;
      this._postSet(224, var2, var1);
   }

   public boolean getDebugXMLEntityCacheIncludeLocation() {
      return this._DebugXMLEntityCacheIncludeLocation;
   }

   public boolean isDebugXMLEntityCacheIncludeLocationSet() {
      return this._isSet(225);
   }

   public void setDebugXMLEntityCacheIncludeLocation(boolean var1) {
      boolean var2 = this._DebugXMLEntityCacheIncludeLocation;
      this._DebugXMLEntityCacheIncludeLocation = var1;
      this._postSet(225, var2, var1);
   }

   public boolean getDebugXMLEntityCacheUseShortClass() {
      return this._DebugXMLEntityCacheUseShortClass;
   }

   public boolean isDebugXMLEntityCacheUseShortClassSet() {
      return this._isSet(226);
   }

   public void setDebugXMLEntityCacheUseShortClass(boolean var1) {
      boolean var2 = this._DebugXMLEntityCacheUseShortClass;
      this._DebugXMLEntityCacheUseShortClass = var1;
      this._postSet(226, var2, var1);
   }

   public boolean getDebugDeploy() {
      return this._DebugDeploy;
   }

   public boolean isDebugDeploySet() {
      return this._isSet(227);
   }

   public void setDebugDeploy(boolean var1) {
      boolean var2 = this._DebugDeploy;
      this._DebugDeploy = var1;
      this._postSet(227, var2, var1);
   }

   public boolean getDebugDeployment() {
      return this._DebugDeployment;
   }

   public boolean isDebugDeploymentSet() {
      return this._isSet(228);
   }

   public void setDebugDeployment(boolean var1) {
      boolean var2 = this._DebugDeployment;
      this._DebugDeployment = var1;
      this._postSet(228, var2, var1);
   }

   public boolean getDebugDeploymentService() {
      return this._DebugDeploymentService;
   }

   public boolean isDebugDeploymentServiceSet() {
      return this._isSet(229);
   }

   public void setDebugDeploymentService(boolean var1) {
      boolean var2 = this._DebugDeploymentService;
      this._DebugDeploymentService = var1;
      this._postSet(229, var2, var1);
   }

   public boolean getDebugDeploymentServiceStatusUpdates() {
      return this._DebugDeploymentServiceStatusUpdates;
   }

   public boolean isDebugDeploymentServiceStatusUpdatesSet() {
      return this._isSet(230);
   }

   public void setDebugDeploymentServiceStatusUpdates(boolean var1) {
      boolean var2 = this._DebugDeploymentServiceStatusUpdates;
      this._DebugDeploymentServiceStatusUpdates = var1;
      this._postSet(230, var2, var1);
   }

   public boolean getDebugDeploymentServiceInternal() {
      return this._DebugDeploymentServiceInternal;
   }

   public boolean isDebugDeploymentServiceInternalSet() {
      return this._isSet(231);
   }

   public void setDebugDeploymentServiceInternal(boolean var1) {
      boolean var2 = this._DebugDeploymentServiceInternal;
      this._DebugDeploymentServiceInternal = var1;
      this._postSet(231, var2, var1);
   }

   public boolean getDebugDeploymentServiceTransport() {
      return this._DebugDeploymentServiceTransport;
   }

   public boolean isDebugDeploymentServiceTransportSet() {
      return this._isSet(232);
   }

   public void setDebugDeploymentServiceTransport(boolean var1) {
      boolean var2 = this._DebugDeploymentServiceTransport;
      this._DebugDeploymentServiceTransport = var1;
      this._postSet(232, var2, var1);
   }

   public boolean getDebugDeploymentServiceTransportHttp() {
      return this._DebugDeploymentServiceTransportHttp;
   }

   public boolean isDebugDeploymentServiceTransportHttpSet() {
      return this._isSet(233);
   }

   public void setDebugDeploymentServiceTransportHttp(boolean var1) {
      boolean var2 = this._DebugDeploymentServiceTransportHttp;
      this._DebugDeploymentServiceTransportHttp = var1;
      this._postSet(233, var2, var1);
   }

   public boolean getMasterDeployer() {
      return this._MasterDeployer;
   }

   public boolean isMasterDeployerSet() {
      return this._isSet(234);
   }

   public void setMasterDeployer(boolean var1) {
      boolean var2 = this._MasterDeployer;
      this._MasterDeployer = var1;
      this._postSet(234, var2, var1);
   }

   public boolean getSlaveDeployer() {
      return this._SlaveDeployer;
   }

   public boolean isSlaveDeployerSet() {
      return this._isSet(235);
   }

   public void setSlaveDeployer(boolean var1) {
      boolean var2 = this._SlaveDeployer;
      this._SlaveDeployer = var1;
      this._postSet(235, var2, var1);
   }

   public boolean getApplicationContainer() {
      return this._ApplicationContainer;
   }

   public boolean isApplicationContainerSet() {
      return this._isSet(236);
   }

   public void setApplicationContainer(boolean var1) {
      boolean var2 = this._ApplicationContainer;
      this._ApplicationContainer = var1;
      this._postSet(236, var2, var1);
   }

   public boolean getClassFinder() {
      return this._ClassFinder;
   }

   public boolean isClassFinderSet() {
      return this._isSet(237);
   }

   public void setClassFinder(boolean var1) {
      boolean var2 = this._ClassFinder;
      this._ClassFinder = var1;
      this._postSet(237, var2, var1);
   }

   public boolean getClasspathServlet() {
      return this._ClasspathServlet;
   }

   public boolean isClasspathServletSet() {
      return this._isSet(238);
   }

   public void setClasspathServlet(boolean var1) {
      boolean var2 = this._ClasspathServlet;
      this._ClasspathServlet = var1;
      this._postSet(238, var2, var1);
   }

   public boolean getWebModule() {
      return this._WebModule;
   }

   public boolean isWebModuleSet() {
      return this._isSet(239);
   }

   public void setWebModule(boolean var1) {
      boolean var2 = this._WebModule;
      this._WebModule = var1;
      this._postSet(239, var2, var1);
   }

   public boolean getClassLoader() {
      return this._ClassLoader;
   }

   public boolean isClassLoaderSet() {
      return this._isSet(240);
   }

   public void setClassLoader(boolean var1) {
      boolean var2 = this._ClassLoader;
      this._ClassLoader = var1;
      this._postSet(240, var2, var1);
   }

   public boolean getClassLoaderVerbose() {
      return this._ClassLoaderVerbose;
   }

   public boolean isClassLoaderVerboseSet() {
      return this._isSet(241);
   }

   public void setClassLoaderVerbose(boolean var1) {
      boolean var2 = this._ClassLoaderVerbose;
      this._ClassLoaderVerbose = var1;
      this._postSet(241, var2, var1);
   }

   public boolean getClassloaderWebApp() {
      return this._ClassloaderWebApp;
   }

   public boolean isClassloaderWebAppSet() {
      return this._isSet(242);
   }

   public void setClassloaderWebApp(boolean var1) {
      boolean var2 = this._ClassloaderWebApp;
      this._ClassloaderWebApp = var1;
      this._postSet(242, var2, var1);
   }

   public boolean getDebugBootstrapServlet() {
      return this._DebugBootstrapServlet;
   }

   public boolean isDebugBootstrapServletSet() {
      return this._isSet(243);
   }

   public void setDebugBootstrapServlet(boolean var1) {
      boolean var2 = this._DebugBootstrapServlet;
      this._DebugBootstrapServlet = var1;
      this._postSet(243, var2, var1);
   }

   public boolean getDebugFileDistributionServlet() {
      return this._DebugFileDistributionServlet;
   }

   public boolean isDebugFileDistributionServletSet() {
      return this._isSet(244);
   }

   public void setDebugFileDistributionServlet(boolean var1) {
      boolean var2 = this._DebugFileDistributionServlet;
      this._DebugFileDistributionServlet = var1;
      this._postSet(244, var2, var1);
   }

   public boolean getDebugDiagnosticLifecycleHandlers() {
      return this._DebugDiagnosticLifecycleHandlers;
   }

   public boolean isDebugDiagnosticLifecycleHandlersSet() {
      return this._isSet(245);
   }

   public void setDebugDiagnosticLifecycleHandlers(boolean var1) {
      boolean var2 = this._DebugDiagnosticLifecycleHandlers;
      this._DebugDiagnosticLifecycleHandlers = var1;
      this._postSet(245, var2, var1);
   }

   public boolean getDebugDiagnosticDataGathering() {
      return this._DebugDiagnosticDataGathering;
   }

   public boolean isDebugDiagnosticDataGatheringSet() {
      return this._isSet(246);
   }

   public void setDebugDiagnosticDataGathering(boolean var1) {
      boolean var2 = this._DebugDiagnosticDataGathering;
      this._DebugDiagnosticDataGathering = var1;
      this._postSet(246, var2, var1);
   }

   public boolean getDebugDiagnosticInstrumentation() {
      return this._DebugDiagnosticInstrumentation;
   }

   public boolean isDebugDiagnosticInstrumentationSet() {
      return this._isSet(247);
   }

   public void setDebugDiagnosticInstrumentation(boolean var1) {
      boolean var2 = this._DebugDiagnosticInstrumentation;
      this._DebugDiagnosticInstrumentation = var1;
      this._postSet(247, var2, var1);
   }

   public boolean getDebugDiagnosticInstrumentationWeaving() {
      return this._DebugDiagnosticInstrumentationWeaving;
   }

   public boolean isDebugDiagnosticInstrumentationWeavingSet() {
      return this._isSet(248);
   }

   public void setDebugDiagnosticInstrumentationWeaving(boolean var1) {
      boolean var2 = this._DebugDiagnosticInstrumentationWeaving;
      this._DebugDiagnosticInstrumentationWeaving = var1;
      this._postSet(248, var2, var1);
   }

   public boolean getDebugDiagnosticInstrumentationWeavingMatches() {
      return this._DebugDiagnosticInstrumentationWeavingMatches;
   }

   public boolean isDebugDiagnosticInstrumentationWeavingMatchesSet() {
      return this._isSet(249);
   }

   public void setDebugDiagnosticInstrumentationWeavingMatches(boolean var1) {
      boolean var2 = this._DebugDiagnosticInstrumentationWeavingMatches;
      this._DebugDiagnosticInstrumentationWeavingMatches = var1;
      this._postSet(249, var2, var1);
   }

   public boolean getDebugDiagnosticInstrumentationActions() {
      return this._DebugDiagnosticInstrumentationActions;
   }

   public boolean isDebugDiagnosticInstrumentationActionsSet() {
      return this._isSet(250);
   }

   public void setDebugDiagnosticInstrumentationActions(boolean var1) {
      boolean var2 = this._DebugDiagnosticInstrumentationActions;
      this._DebugDiagnosticInstrumentationActions = var1;
      this._postSet(250, var2, var1);
   }

   public boolean getDebugDiagnosticInstrumentationEvents() {
      return this._DebugDiagnosticInstrumentationEvents;
   }

   public boolean isDebugDiagnosticInstrumentationEventsSet() {
      return this._isSet(251);
   }

   public void setDebugDiagnosticInstrumentationEvents(boolean var1) {
      boolean var2 = this._DebugDiagnosticInstrumentationEvents;
      this._DebugDiagnosticInstrumentationEvents = var1;
      this._postSet(251, var2, var1);
   }

   public boolean getDebugDiagnosticInstrumentationConfig() {
      return this._DebugDiagnosticInstrumentationConfig;
   }

   public boolean isDebugDiagnosticInstrumentationConfigSet() {
      return this._isSet(252);
   }

   public void setDebugDiagnosticInstrumentationConfig(boolean var1) {
      boolean var2 = this._DebugDiagnosticInstrumentationConfig;
      this._DebugDiagnosticInstrumentationConfig = var1;
      this._postSet(252, var2, var1);
   }

   public boolean getDebugDiagnosticArchive() {
      return this._DebugDiagnosticArchive;
   }

   public boolean isDebugDiagnosticArchiveSet() {
      return this._isSet(253);
   }

   public void setDebugDiagnosticArchive(boolean var1) {
      boolean var2 = this._DebugDiagnosticArchive;
      this._DebugDiagnosticArchive = var1;
      this._postSet(253, var2, var1);
   }

   public boolean getDebugDiagnosticFileArchive() {
      return this._DebugDiagnosticFileArchive;
   }

   public boolean isDebugDiagnosticFileArchiveSet() {
      return this._isSet(254);
   }

   public void setDebugDiagnosticFileArchive(boolean var1) {
      boolean var2 = this._DebugDiagnosticFileArchive;
      this._DebugDiagnosticFileArchive = var1;
      this._postSet(254, var2, var1);
   }

   public boolean getDebugDiagnosticWlstoreArchive() {
      return this._DebugDiagnosticWlstoreArchive;
   }

   public boolean isDebugDiagnosticWlstoreArchiveSet() {
      return this._isSet(255);
   }

   public void setDebugDiagnosticWlstoreArchive(boolean var1) {
      boolean var2 = this._DebugDiagnosticWlstoreArchive;
      this._DebugDiagnosticWlstoreArchive = var1;
      this._postSet(255, var2, var1);
   }

   public boolean getDebugDiagnosticJdbcArchive() {
      return this._DebugDiagnosticJdbcArchive;
   }

   public boolean isDebugDiagnosticJdbcArchiveSet() {
      return this._isSet(256);
   }

   public void setDebugDiagnosticJdbcArchive(boolean var1) {
      boolean var2 = this._DebugDiagnosticJdbcArchive;
      this._DebugDiagnosticJdbcArchive = var1;
      this._postSet(256, var2, var1);
   }

   public boolean getDebugDiagnosticArchiveRetirement() {
      return this._DebugDiagnosticArchiveRetirement;
   }

   public boolean isDebugDiagnosticArchiveRetirementSet() {
      return this._isSet(257);
   }

   public void setDebugDiagnosticArchiveRetirement(boolean var1) {
      boolean var2 = this._DebugDiagnosticArchiveRetirement;
      this._DebugDiagnosticArchiveRetirement = var1;
      this._postSet(257, var2, var1);
   }

   public boolean getDebugDiagnosticsModule() {
      return this._DebugDiagnosticsModule;
   }

   public boolean isDebugDiagnosticsModuleSet() {
      return this._isSet(258);
   }

   public void setDebugDiagnosticsModule(boolean var1) {
      boolean var2 = this._DebugDiagnosticsModule;
      this._DebugDiagnosticsModule = var1;
      this._postSet(258, var2, var1);
   }

   public boolean getDebugDiagnosticsHarvester() {
      return this._DebugDiagnosticsHarvester;
   }

   public boolean isDebugDiagnosticsHarvesterSet() {
      return this._isSet(259);
   }

   public void setDebugDiagnosticsHarvester(boolean var1) {
      boolean var2 = this._DebugDiagnosticsHarvester;
      this._DebugDiagnosticsHarvester = var1;
      this._postSet(259, var2, var1);
   }

   public boolean getDebugDiagnosticsHarvesterData() {
      return this._DebugDiagnosticsHarvesterData;
   }

   public boolean isDebugDiagnosticsHarvesterDataSet() {
      return this._isSet(260);
   }

   public void setDebugDiagnosticsHarvesterData(boolean var1) {
      boolean var2 = this._DebugDiagnosticsHarvesterData;
      this._DebugDiagnosticsHarvesterData = var1;
      this._postSet(260, var2, var1);
   }

   public boolean getDebugDiagnosticsHarvesterMBeanPlugin() {
      return this._DebugDiagnosticsHarvesterMBeanPlugin;
   }

   public boolean isDebugDiagnosticsHarvesterMBeanPluginSet() {
      return this._isSet(261);
   }

   public void setDebugDiagnosticsHarvesterMBeanPlugin(boolean var1) {
      boolean var2 = this._DebugDiagnosticsHarvesterMBeanPlugin;
      this._DebugDiagnosticsHarvesterMBeanPlugin = var1;
      this._postSet(261, var2, var1);
   }

   public boolean getDebugDiagnosticsHarvesterTreeBeanPlugin() {
      return this._DebugDiagnosticsHarvesterTreeBeanPlugin;
   }

   public boolean isDebugDiagnosticsHarvesterTreeBeanPluginSet() {
      return this._isSet(262);
   }

   public void setDebugDiagnosticsHarvesterTreeBeanPlugin(boolean var1) {
      boolean var2 = this._DebugDiagnosticsHarvesterTreeBeanPlugin;
      this._DebugDiagnosticsHarvesterTreeBeanPlugin = var1;
      this._postSet(262, var2, var1);
   }

   public boolean getDebugDiagnosticImage() {
      return this._DebugDiagnosticImage;
   }

   public boolean isDebugDiagnosticImageSet() {
      return this._isSet(263);
   }

   public void setDebugDiagnosticImage(boolean var1) {
      boolean var2 = this._DebugDiagnosticImage;
      this._DebugDiagnosticImage = var1;
      this._postSet(263, var2, var1);
   }

   public boolean getDebugDiagnosticQuery() {
      return this._DebugDiagnosticQuery;
   }

   public boolean isDebugDiagnosticQuerySet() {
      return this._isSet(264);
   }

   public void setDebugDiagnosticQuery(boolean var1) {
      boolean var2 = this._DebugDiagnosticQuery;
      this._DebugDiagnosticQuery = var1;
      this._postSet(264, var2, var1);
   }

   public boolean getDebugDiagnosticAccessor() {
      return this._DebugDiagnosticAccessor;
   }

   public boolean isDebugDiagnosticAccessorSet() {
      return this._isSet(265);
   }

   public void setDebugDiagnosticAccessor(boolean var1) {
      boolean var2 = this._DebugDiagnosticAccessor;
      this._DebugDiagnosticAccessor = var1;
      this._postSet(265, var2, var1);
   }

   public boolean getDebugDiagnosticCollections() {
      return this._DebugDiagnosticCollections;
   }

   public boolean isDebugDiagnosticCollectionsSet() {
      return this._isSet(266);
   }

   public void setDebugDiagnosticCollections(boolean var1) {
      boolean var2 = this._DebugDiagnosticCollections;
      this._DebugDiagnosticCollections = var1;
      this._postSet(266, var2, var1);
   }

   public boolean getDebugDiagnosticContext() {
      return this._DebugDiagnosticContext;
   }

   public boolean isDebugDiagnosticContextSet() {
      return this._isSet(267);
   }

   public void setDebugDiagnosticContext(boolean var1) {
      boolean var2 = this._DebugDiagnosticContext;
      this._DebugDiagnosticContext = var1;
      this._postSet(267, var2, var1);
   }

   public boolean getDebugSNMPToolkit() {
      return this._DebugSNMPToolkit;
   }

   public boolean isDebugSNMPToolkitSet() {
      return this._isSet(268);
   }

   public void setDebugSNMPToolkit(boolean var1) {
      boolean var2 = this._DebugSNMPToolkit;
      this._DebugSNMPToolkit = var1;
      this._postSet(268, var2, var1);
   }

   public boolean getDebugSNMPAgent() {
      return this._DebugSNMPAgent;
   }

   public boolean isDebugSNMPAgentSet() {
      return this._isSet(269);
   }

   public void setDebugSNMPAgent(boolean var1) {
      boolean var2 = this._DebugSNMPAgent;
      this._DebugSNMPAgent = var1;
      this._postSet(269, var2, var1);
   }

   public boolean getDebugSNMPProtocolTCP() {
      return this._DebugSNMPProtocolTCP;
   }

   public boolean isDebugSNMPProtocolTCPSet() {
      return this._isSet(270);
   }

   public boolean getDebugSNMPExtensionProvider() {
      return this._DebugSNMPExtensionProvider;
   }

   public boolean isDebugSNMPExtensionProviderSet() {
      return this._isSet(271);
   }

   public void setDebugSNMPExtensionProvider(boolean var1) {
      boolean var2 = this._DebugSNMPExtensionProvider;
      this._DebugSNMPExtensionProvider = var1;
      this._postSet(271, var2, var1);
   }

   public void setDebugSNMPProtocolTCP(boolean var1) {
      boolean var2 = this._DebugSNMPProtocolTCP;
      this._DebugSNMPProtocolTCP = var1;
      this._postSet(270, var2, var1);
   }

   public boolean getDebugDomainLogHandler() {
      return this._DebugDomainLogHandler;
   }

   public boolean isDebugDomainLogHandlerSet() {
      return this._isSet(272);
   }

   public void setDebugDomainLogHandler(boolean var1) {
      boolean var2 = this._DebugDomainLogHandler;
      this._DebugDomainLogHandler = var1;
      this._postSet(272, var2, var1);
   }

   public boolean getDebugLoggingConfiguration() {
      return this._DebugLoggingConfiguration;
   }

   public boolean isDebugLoggingConfigurationSet() {
      return this._isSet(273);
   }

   public void setDebugLoggingConfiguration(boolean var1) {
      boolean var2 = this._DebugLoggingConfiguration;
      this._DebugLoggingConfiguration = var1;
      this._postSet(273, var2, var1);
   }

   public boolean getDebugDiagnosticWatch() {
      return this._DebugDiagnosticWatch;
   }

   public boolean isDebugDiagnosticWatchSet() {
      return this._isSet(274);
   }

   public void setDebugDiagnosticWatch(boolean var1) {
      boolean var2 = this._DebugDiagnosticWatch;
      this._DebugDiagnosticWatch = var1;
      this._postSet(274, var2, var1);
   }

   public boolean getDebugRAPoolVerbose() {
      return this._DebugRAPoolVerbose;
   }

   public boolean isDebugRAPoolVerboseSet() {
      return this._isSet(275);
   }

   public void setDebugRAPoolVerbose(boolean var1) {
      boolean var2 = this._DebugRAPoolVerbose;
      this._DebugRAPoolVerbose = var1;
      this._postSet(275, var2, var1);
   }

   public boolean getDebugRA() {
      return this._DebugRA;
   }

   public boolean isDebugRASet() {
      return this._isSet(276);
   }

   public void setDebugRA(boolean var1) {
      boolean var2 = this._DebugRA;
      this._DebugRA = var1;
      this._postSet(276, var2, var1);
   }

   public boolean getDebugRAXAin() {
      return this._DebugRAXAin;
   }

   public boolean isDebugRAXAinSet() {
      return this._isSet(277);
   }

   public void setDebugRAXAin(boolean var1) {
      boolean var2 = this._DebugRAXAin;
      this._DebugRAXAin = var1;
      this._postSet(277, var2, var1);
   }

   public boolean getDebugRAXAout() {
      return this._DebugRAXAout;
   }

   public boolean isDebugRAXAoutSet() {
      return this._isSet(278);
   }

   public void setDebugRAXAout(boolean var1) {
      boolean var2 = this._DebugRAXAout;
      this._DebugRAXAout = var1;
      this._postSet(278, var2, var1);
   }

   public boolean getDebugRAXAwork() {
      return this._DebugRAXAwork;
   }

   public boolean isDebugRAXAworkSet() {
      return this._isSet(279);
   }

   public void setDebugRAXAwork(boolean var1) {
      boolean var2 = this._DebugRAXAwork;
      this._DebugRAXAwork = var1;
      this._postSet(279, var2, var1);
   }

   public boolean getDebugRALocalOut() {
      return this._DebugRALocalOut;
   }

   public boolean isDebugRALocalOutSet() {
      return this._isSet(280);
   }

   public void setDebugRALocalOut(boolean var1) {
      boolean var2 = this._DebugRALocalOut;
      this._DebugRALocalOut = var1;
      this._postSet(280, var2, var1);
   }

   public boolean getDebugRALifecycle() {
      return this._DebugRALifecycle;
   }

   public boolean isDebugRALifecycleSet() {
      return this._isSet(281);
   }

   public void setDebugRALifecycle(boolean var1) {
      boolean var2 = this._DebugRALifecycle;
      this._DebugRALifecycle = var1;
      this._postSet(281, var2, var1);
   }

   public boolean getDebugConnectorService() {
      return this._DebugConnectorService;
   }

   public boolean isDebugConnectorServiceSet() {
      return this._isSet(282);
   }

   public void setDebugConnectorService(boolean var1) {
      boolean var2 = this._DebugConnectorService;
      this._DebugConnectorService = var1;
      this._postSet(282, var2, var1);
   }

   public boolean getDebugRADeployment() {
      return this._DebugRADeployment;
   }

   public boolean isDebugRADeploymentSet() {
      return this._isSet(283);
   }

   public void setDebugRADeployment(boolean var1) {
      boolean var2 = this._DebugRADeployment;
      this._DebugRADeployment = var1;
      this._postSet(283, var2, var1);
   }

   public boolean getDebugRAParsing() {
      return this._DebugRAParsing;
   }

   public boolean isDebugRAParsingSet() {
      return this._isSet(284);
   }

   public void setDebugRAParsing(boolean var1) {
      boolean var2 = this._DebugRAParsing;
      this._DebugRAParsing = var1;
      this._postSet(284, var2, var1);
   }

   public Object _getKey() {
      return super._getKey();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
   }

   public boolean getDebugRASecurityCtx() {
      return this._DebugRASecurityCtx;
   }

   public boolean isDebugRASecurityCtxSet() {
      return this._isSet(285);
   }

   public void setDebugRASecurityCtx(boolean var1) {
      boolean var2 = this._DebugRASecurityCtx;
      this._DebugRASecurityCtx = var1;
      this._postSet(285, var2, var1);
   }

   public boolean getDebugRAPooling() {
      return this._DebugRAPooling;
   }

   public boolean isDebugRAPoolingSet() {
      return this._isSet(286);
   }

   public void setDebugRAPooling(boolean var1) {
      boolean var2 = this._DebugRAPooling;
      this._DebugRAPooling = var1;
      this._postSet(286, var2, var1);
   }

   public boolean getDebugRAConnections() {
      return this._DebugRAConnections;
   }

   public boolean isDebugRAConnectionsSet() {
      return this._isSet(287);
   }

   public void setDebugRAConnections(boolean var1) {
      boolean var2 = this._DebugRAConnections;
      this._DebugRAConnections = var1;
      this._postSet(287, var2, var1);
   }

   public boolean getDebugRAConnEvents() {
      return this._DebugRAConnEvents;
   }

   public boolean isDebugRAConnEventsSet() {
      return this._isSet(288);
   }

   public void setDebugRAConnEvents(boolean var1) {
      boolean var2 = this._DebugRAConnEvents;
      this._DebugRAConnEvents = var1;
      this._postSet(288, var2, var1);
   }

   public boolean getDebugRAWork() {
      return this._DebugRAWork;
   }

   public boolean isDebugRAWorkSet() {
      return this._isSet(289);
   }

   public void setDebugRAWork(boolean var1) {
      boolean var2 = this._DebugRAWork;
      this._DebugRAWork = var1;
      this._postSet(289, var2, var1);
   }

   public boolean getDebugRAWorkEvents() {
      return this._DebugRAWorkEvents;
   }

   public boolean isDebugRAWorkEventsSet() {
      return this._isSet(290);
   }

   public void setDebugRAWorkEvents(boolean var1) {
      boolean var2 = this._DebugRAWorkEvents;
      this._DebugRAWorkEvents = var1;
      this._postSet(290, var2, var1);
   }

   public boolean getDebugRAClassloader() {
      return this._DebugRAClassloader;
   }

   public boolean isDebugRAClassloaderSet() {
      return this._isSet(291);
   }

   public void setDebugRAClassloader(boolean var1) {
      boolean var2 = this._DebugRAClassloader;
      this._DebugRAClassloader = var1;
      this._postSet(291, var2, var1);
   }

   public boolean getDebugWANReplicationDetails() {
      return this._DebugWANReplicationDetails;
   }

   public boolean isDebugWANReplicationDetailsSet() {
      return this._isSet(292);
   }

   public void setDebugWANReplicationDetails(boolean var1) {
      boolean var2 = this._DebugWANReplicationDetails;
      this._DebugWANReplicationDetails = var1;
      this._postSet(292, var2, var1);
   }

   public boolean getDebugJMX() {
      return this._DebugJMX;
   }

   public boolean isDebugJMXSet() {
      return this._isSet(293);
   }

   public void setDebugJMX(boolean var1) {
      boolean var2 = this._DebugJMX;
      this._DebugJMX = var1;
      this._postSet(293, var2, var1);
   }

   public boolean getDebugJMXCore() {
      return this._DebugJMXCore;
   }

   public boolean isDebugJMXCoreSet() {
      return this._isSet(294);
   }

   public void setDebugJMXCore(boolean var1) {
      boolean var2 = this._DebugJMXCore;
      this._DebugJMXCore = var1;
      this._postSet(294, var2, var1);
   }

   public boolean getDebugJMXRuntime() {
      return this._DebugJMXRuntime;
   }

   public boolean isDebugJMXRuntimeSet() {
      return this._isSet(295);
   }

   public void setDebugJMXRuntime(boolean var1) {
      boolean var2 = this._DebugJMXRuntime;
      this._DebugJMXRuntime = var1;
      this._postSet(295, var2, var1);
   }

   public boolean getDebugJMXDomain() {
      return this._DebugJMXDomain;
   }

   public boolean isDebugJMXDomainSet() {
      return this._isSet(296);
   }

   public void setDebugJMXDomain(boolean var1) {
      boolean var2 = this._DebugJMXDomain;
      this._DebugJMXDomain = var1;
      this._postSet(296, var2, var1);
   }

   public boolean getDebugJMXEdit() {
      return this._DebugJMXEdit;
   }

   public boolean isDebugJMXEditSet() {
      return this._isSet(297);
   }

   public void setDebugJMXEdit(boolean var1) {
      boolean var2 = this._DebugJMXEdit;
      this._DebugJMXEdit = var1;
      this._postSet(297, var2, var1);
   }

   public boolean getDebugJMXCompatibility() {
      return this._DebugJMXCompatibility;
   }

   public boolean isDebugJMXCompatibilitySet() {
      return this._isSet(298);
   }

   public void setDebugJMXCompatibility(boolean var1) {
      boolean var2 = this._DebugJMXCompatibility;
      this._DebugJMXCompatibility = var1;
      this._postSet(298, var2, var1);
   }

   public boolean getDebugConfigurationEdit() {
      return this._DebugConfigurationEdit;
   }

   public boolean isDebugConfigurationEditSet() {
      return this._isSet(299);
   }

   public void setDebugConfigurationEdit(boolean var1) {
      boolean var2 = this._DebugConfigurationEdit;
      this._DebugConfigurationEdit = var1;
      this._postSet(299, var2, var1);
   }

   public boolean getDebugConfigurationRuntime() {
      return this._DebugConfigurationRuntime;
   }

   public boolean isDebugConfigurationRuntimeSet() {
      return this._isSet(300);
   }

   public void setDebugConfigurationRuntime(boolean var1) {
      boolean var2 = this._DebugConfigurationRuntime;
      this._DebugConfigurationRuntime = var1;
      this._postSet(300, var2, var1);
   }

   public boolean getDebugJ2EEManagement() {
      return this._DebugJ2EEManagement;
   }

   public boolean isDebugJ2EEManagementSet() {
      return this._isSet(301);
   }

   public void setDebugJ2EEManagement(boolean var1) {
      boolean var2 = this._DebugJ2EEManagement;
      this._DebugJ2EEManagement = var1;
      this._postSet(301, var2, var1);
   }

   public boolean getDebugIIOPNaming() {
      return this._DebugIIOPNaming;
   }

   public boolean isDebugIIOPNamingSet() {
      return this._isSet(302);
   }

   public void setDebugIIOPNaming(boolean var1) {
      boolean var2 = this._DebugIIOPNaming;
      this._DebugIIOPNaming = var1;
      this._postSet(302, var2, var1);
   }

   public boolean getDebugIIOPTunneling() {
      return this._DebugIIOPTunneling;
   }

   public boolean isDebugIIOPTunnelingSet() {
      return this._isSet(303);
   }

   public void setDebugIIOPTunneling(boolean var1) {
      boolean var2 = this._DebugIIOPTunneling;
      this._DebugIIOPTunneling = var1;
      this._postSet(303, var2, var1);
   }

   public boolean getDebugConsensusLeasing() {
      return this._DebugConsensusLeasing;
   }

   public boolean isDebugConsensusLeasingSet() {
      return this._isSet(304);
   }

   public void setDebugConsensusLeasing(boolean var1) {
      boolean var2 = this._DebugConsensusLeasing;
      this._DebugConsensusLeasing = var1;
      this._postSet(304, var2, var1);
   }

   public boolean getDebugServerLifeCycle() {
      return this._DebugServerLifeCycle;
   }

   public boolean isDebugServerLifeCycleSet() {
      return this._isSet(305);
   }

   public void setDebugServerLifeCycle(boolean var1) {
      boolean var2 = this._DebugServerLifeCycle;
      this._DebugServerLifeCycle = var1;
      this._postSet(305, var2, var1);
   }

   public boolean getDebugWTCConfig() {
      return this._DebugWTCConfig;
   }

   public boolean isDebugWTCConfigSet() {
      return this._isSet(306);
   }

   public void setDebugWTCConfig(boolean var1) {
      boolean var2 = this._DebugWTCConfig;
      this._DebugWTCConfig = var1;
      this._postSet(306, var2, var1);
   }

   public boolean getDebugWTCTDomPdu() {
      return this._DebugWTCTDomPdu;
   }

   public boolean isDebugWTCTDomPduSet() {
      return this._isSet(307);
   }

   public void setDebugWTCTDomPdu(boolean var1) {
      boolean var2 = this._DebugWTCTDomPdu;
      this._DebugWTCTDomPdu = var1;
      this._postSet(307, var2, var1);
   }

   public boolean getDebugWTCUData() {
      return this._DebugWTCUData;
   }

   public boolean isDebugWTCUDataSet() {
      return this._isSet(308);
   }

   public void setDebugWTCUData(boolean var1) {
      boolean var2 = this._DebugWTCUData;
      this._DebugWTCUData = var1;
      this._postSet(308, var2, var1);
   }

   public boolean getDebugWTCGwtEx() {
      return this._DebugWTCGwtEx;
   }

   public boolean isDebugWTCGwtExSet() {
      return this._isSet(309);
   }

   public void setDebugWTCGwtEx(boolean var1) {
      boolean var2 = this._DebugWTCGwtEx;
      this._DebugWTCGwtEx = var1;
      this._postSet(309, var2, var1);
   }

   public boolean getDebugWTCJatmiEx() {
      return this._DebugWTCJatmiEx;
   }

   public boolean isDebugWTCJatmiExSet() {
      return this._isSet(310);
   }

   public void setDebugWTCJatmiEx(boolean var1) {
      boolean var2 = this._DebugWTCJatmiEx;
      this._DebugWTCJatmiEx = var1;
      this._postSet(310, var2, var1);
   }

   public boolean getDebugWTCCorbaEx() {
      return this._DebugWTCCorbaEx;
   }

   public boolean isDebugWTCCorbaExSet() {
      return this._isSet(311);
   }

   public void setDebugWTCCorbaEx(boolean var1) {
      boolean var2 = this._DebugWTCCorbaEx;
      this._DebugWTCCorbaEx = var1;
      this._postSet(311, var2, var1);
   }

   public boolean getDebugWTCtBridgeEx() {
      return this._DebugWTCtBridgeEx;
   }

   public boolean isDebugWTCtBridgeExSet() {
      return this._isSet(312);
   }

   public void setDebugWTCtBridgeEx(boolean var1) {
      boolean var2 = this._DebugWTCtBridgeEx;
      this._DebugWTCtBridgeEx = var1;
      this._postSet(312, var2, var1);
   }

   public boolean getDebugJpaMetaData() {
      return this._DebugJpaMetaData;
   }

   public boolean isDebugJpaMetaDataSet() {
      return this._isSet(313);
   }

   public void setDebugJpaMetaData(boolean var1) {
      boolean var2 = this._DebugJpaMetaData;
      this._DebugJpaMetaData = var1;
      this._postSet(313, var2, var1);
   }

   public boolean getDebugJpaEnhance() {
      return this._DebugJpaEnhance;
   }

   public boolean isDebugJpaEnhanceSet() {
      return this._isSet(314);
   }

   public void setDebugJpaEnhance(boolean var1) {
      boolean var2 = this._DebugJpaEnhance;
      this._DebugJpaEnhance = var1;
      this._postSet(314, var2, var1);
   }

   public boolean getDebugJpaRuntime() {
      return this._DebugJpaRuntime;
   }

   public boolean isDebugJpaRuntimeSet() {
      return this._isSet(315);
   }

   public void setDebugJpaRuntime(boolean var1) {
      boolean var2 = this._DebugJpaRuntime;
      this._DebugJpaRuntime = var1;
      this._postSet(315, var2, var1);
   }

   public boolean getDebugJpaQuery() {
      return this._DebugJpaQuery;
   }

   public boolean isDebugJpaQuerySet() {
      return this._isSet(316);
   }

   public void setDebugJpaQuery(boolean var1) {
      boolean var2 = this._DebugJpaQuery;
      this._DebugJpaQuery = var1;
      this._postSet(316, var2, var1);
   }

   public boolean getDebugJpaDataCache() {
      return this._DebugJpaDataCache;
   }

   public boolean isDebugJpaDataCacheSet() {
      return this._isSet(317);
   }

   public void setDebugJpaDataCache(boolean var1) {
      boolean var2 = this._DebugJpaDataCache;
      this._DebugJpaDataCache = var1;
      this._postSet(317, var2, var1);
   }

   public boolean getDebugJpaTool() {
      return this._DebugJpaTool;
   }

   public boolean isDebugJpaToolSet() {
      return this._isSet(318);
   }

   public void setDebugJpaTool(boolean var1) {
      boolean var2 = this._DebugJpaTool;
      this._DebugJpaTool = var1;
      this._postSet(318, var2, var1);
   }

   public boolean getDebugJpaManage() {
      return this._DebugJpaManage;
   }

   public boolean isDebugJpaManageSet() {
      return this._isSet(319);
   }

   public void setDebugJpaManage(boolean var1) {
      boolean var2 = this._DebugJpaManage;
      this._DebugJpaManage = var1;
      this._postSet(319, var2, var1);
   }

   public boolean getDebugJpaProfile() {
      return this._DebugJpaProfile;
   }

   public boolean isDebugJpaProfileSet() {
      return this._isSet(320);
   }

   public void setDebugJpaProfile(boolean var1) {
      boolean var2 = this._DebugJpaProfile;
      this._DebugJpaProfile = var1;
      this._postSet(320, var2, var1);
   }

   public boolean getDebugJpaJdbcSql() {
      return this._DebugJpaJdbcSql;
   }

   public boolean isDebugJpaJdbcSqlSet() {
      return this._isSet(321);
   }

   public void setDebugJpaJdbcSql(boolean var1) {
      boolean var2 = this._DebugJpaJdbcSql;
      this._DebugJpaJdbcSql = var1;
      this._postSet(321, var2, var1);
   }

   public boolean getDebugJpaJdbcJdbc() {
      return this._DebugJpaJdbcJdbc;
   }

   public boolean isDebugJpaJdbcJdbcSet() {
      return this._isSet(322);
   }

   public void setDebugJpaJdbcJdbc(boolean var1) {
      boolean var2 = this._DebugJpaJdbcJdbc;
      this._DebugJpaJdbcJdbc = var1;
      this._postSet(322, var2, var1);
   }

   public boolean getDebugJpaJdbcSchema() {
      return this._DebugJpaJdbcSchema;
   }

   public boolean isDebugJpaJdbcSchemaSet() {
      return this._isSet(323);
   }

   public void setDebugJpaJdbcSchema(boolean var1) {
      boolean var2 = this._DebugJpaJdbcSchema;
      this._DebugJpaJdbcSchema = var1;
      this._postSet(323, var2, var1);
   }

   public void setDebugJMST3Server(boolean var1) {
      boolean var2 = this._DebugJMST3Server;
      this._DebugJMST3Server = var1;
      this._postSet(324, var2, var1);
   }

   public boolean getDebugJMST3Server() {
      return this._DebugJMST3Server;
   }

   public boolean isDebugJMST3ServerSet() {
      return this._isSet(324);
   }

   public boolean getDebugDescriptor() {
      return this._DebugDescriptor;
   }

   public boolean isDebugDescriptorSet() {
      return this._isSet(325);
   }

   public void setDebugDescriptor(boolean var1) {
      boolean var2 = this._DebugDescriptor;
      this._DebugDescriptor = var1;
      this._postSet(325, var2, var1);
   }

   public boolean getDebugServerStartStatistics() {
      return this._DebugServerStartStatistics;
   }

   public boolean isDebugServerStartStatisticsSet() {
      return this._isSet(326);
   }

   public void setDebugServerStartStatistics(boolean var1) {
      boolean var2 = this._DebugServerStartStatistics;
      this._DebugServerStartStatistics = var1;
      this._postSet(326, var2, var1);
   }

   public boolean getDebugManagementServicesResource() {
      return this._DebugManagementServicesResource;
   }

   public boolean isDebugManagementServicesResourceSet() {
      return this._isSet(327);
   }

   public void setDebugManagementServicesResource(boolean var1) {
      boolean var2 = this._DebugManagementServicesResource;
      this._DebugManagementServicesResource = var1;
      this._postSet(327, var2, var1);
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
      return super._isAnythingSet();
   }

   private boolean _initializeProperty(int var1) {
      boolean var2 = var1 > -1;
      if (!var2) {
         var1 = 236;
      }

      try {
         switch (var1) {
            case 236:
               this._ApplicationContainer = false;
               if (var2) {
                  break;
               }
            case 43:
               this._BugReportServiceWsdlUrl = null;
               if (var2) {
                  break;
               }
            case 50:
               this._ClassChangeNotifier = false;
               if (var2) {
                  break;
               }
            case 237:
               this._ClassFinder = false;
               if (var2) {
                  break;
               }
            case 240:
               this._ClassLoader = false;
               if (var2) {
                  break;
               }
            case 241:
               this._ClassLoaderVerbose = false;
               if (var2) {
                  break;
               }
            case 242:
               this._ClassloaderWebApp = false;
               if (var2) {
                  break;
               }
            case 238:
               this._ClasspathServlet = false;
               if (var2) {
                  break;
               }
            case 44:
               this._DebugAppContainer = false;
               if (var2) {
                  break;
               }
            case 78:
               this._DebugAsyncQueue = false;
               if (var2) {
                  break;
               }
            case 243:
               this._DebugBootstrapServlet = false;
               if (var2) {
                  break;
               }
            case 155:
               this._DebugCertRevocCheck = false;
               if (var2) {
                  break;
               }
            case 46:
               this._DebugClassRedef = false;
               if (var2) {
                  break;
               }
            case 48:
               this._DebugClassSize = false;
               if (var2) {
                  break;
               }
            case 73:
               this._DebugCluster = false;
               if (var2) {
                  break;
               }
            case 75:
               this._DebugClusterAnnouncements = false;
               if (var2) {
                  break;
               }
            case 72:
               this._DebugClusterFragments = false;
               if (var2) {
                  break;
               }
            case 74:
               this._DebugClusterHeartbeats = false;
               if (var2) {
                  break;
               }
            case 299:
               this._DebugConfigurationEdit = false;
               if (var2) {
                  break;
               }
            case 300:
               this._DebugConfigurationRuntime = false;
               if (var2) {
                  break;
               }
            case 282:
               this._DebugConnectorService = false;
               if (var2) {
                  break;
               }
            case 304:
               this._DebugConsensusLeasing = false;
               if (var2) {
                  break;
               }
            case 80:
               this._DebugDRSCalls = false;
               if (var2) {
                  break;
               }
            case 81:
               this._DebugDRSHeartbeats = false;
               if (var2) {
                  break;
               }
            case 82:
               this._DebugDRSMessages = false;
               if (var2) {
                  break;
               }
            case 85:
               this._DebugDRSQueues = false;
               if (var2) {
                  break;
               }
            case 84:
               this._DebugDRSStateTransitions = false;
               if (var2) {
                  break;
               }
            case 83:
               this._DebugDRSUpdateStatus = false;
               if (var2) {
                  break;
               }
            case 227:
               this._DebugDeploy = false;
               if (var2) {
                  break;
               }
            case 228:
               this._DebugDeployment = false;
               if (var2) {
                  break;
               }
            case 229:
               this._DebugDeploymentService = false;
               if (var2) {
                  break;
               }
            case 231:
               this._DebugDeploymentServiceInternal = false;
               if (var2) {
                  break;
               }
            case 230:
               this._DebugDeploymentServiceStatusUpdates = false;
               if (var2) {
                  break;
               }
            case 232:
               this._DebugDeploymentServiceTransport = false;
               if (var2) {
                  break;
               }
            case 233:
               this._DebugDeploymentServiceTransportHttp = false;
               if (var2) {
                  break;
               }
            case 325:
               this._DebugDescriptor = false;
               if (var2) {
                  break;
               }
            case 265:
               this._DebugDiagnosticAccessor = false;
               if (var2) {
                  break;
               }
            case 253:
               this._DebugDiagnosticArchive = false;
               if (var2) {
                  break;
               }
            case 257:
               this._DebugDiagnosticArchiveRetirement = false;
               if (var2) {
                  break;
               }
            case 266:
               this._DebugDiagnosticCollections = false;
               if (var2) {
                  break;
               }
            case 267:
               this._DebugDiagnosticContext = false;
               if (var2) {
                  break;
               }
            case 246:
               this._DebugDiagnosticDataGathering = false;
               if (var2) {
                  break;
               }
            case 254:
               this._DebugDiagnosticFileArchive = false;
               if (var2) {
                  break;
               }
            case 263:
               this._DebugDiagnosticImage = false;
               if (var2) {
                  break;
               }
            case 247:
               this._DebugDiagnosticInstrumentation = false;
               if (var2) {
                  break;
               }
            case 250:
               this._DebugDiagnosticInstrumentationActions = false;
               if (var2) {
                  break;
               }
            case 252:
               this._DebugDiagnosticInstrumentationConfig = false;
               if (var2) {
                  break;
               }
            case 251:
               this._DebugDiagnosticInstrumentationEvents = false;
               if (var2) {
                  break;
               }
            case 248:
               this._DebugDiagnosticInstrumentationWeaving = false;
               if (var2) {
                  break;
               }
            case 249:
               this._DebugDiagnosticInstrumentationWeavingMatches = false;
               if (var2) {
                  break;
               }
            case 256:
               this._DebugDiagnosticJdbcArchive = false;
               if (var2) {
                  break;
               }
            case 245:
               this._DebugDiagnosticLifecycleHandlers = false;
               if (var2) {
                  break;
               }
            case 264:
               this._DebugDiagnosticQuery = false;
               if (var2) {
                  break;
               }
            case 274:
               this._DebugDiagnosticWatch = false;
               if (var2) {
                  break;
               }
            case 255:
               this._DebugDiagnosticWlstoreArchive = false;
               if (var2) {
                  break;
               }
            case 259:
               this._DebugDiagnosticsHarvester = false;
               if (var2) {
                  break;
               }
            case 260:
               this._DebugDiagnosticsHarvesterData = false;
               if (var2) {
                  break;
               }
            case 261:
               this._DebugDiagnosticsHarvesterMBeanPlugin = false;
               if (var2) {
                  break;
               }
            case 262:
               this._DebugDiagnosticsHarvesterTreeBeanPlugin = false;
               if (var2) {
                  break;
               }
            case 258:
               this._DebugDiagnosticsModule = false;
               if (var2) {
                  break;
               }
            case 272:
               this._DebugDomainLogHandler = false;
               if (var2) {
                  break;
               }
            case 61:
               this._DebugEjbCaching = false;
               if (var2) {
                  break;
               }
            case 68:
               this._DebugEjbCmpDeployment = false;
               if (var2) {
                  break;
               }
            case 69:
               this._DebugEjbCmpRuntime = false;
               if (var2) {
                  break;
               }
            case 58:
               this._DebugEjbCompilation = false;
               if (var2) {
                  break;
               }
            case 59:
               this._DebugEjbDeployment = false;
               if (var2) {
                  break;
               }
            case 66:
               this._DebugEjbInvoke = false;
               if (var2) {
                  break;
               }
            case 63:
               this._DebugEjbLocking = false;
               if (var2) {
                  break;
               }
            case 60:
               this._DebugEjbMdbConnection = false;
               if (var2) {
                  break;
               }
            case 64:
               this._DebugEjbPooling = false;
               if (var2) {
                  break;
               }
            case 67:
               this._DebugEjbSecurity = false;
               if (var2) {
                  break;
               }
            case 62:
               this._DebugEjbSwapping = false;
               if (var2) {
                  break;
               }
            case 65:
               this._DebugEjbTimers = false;
               if (var2) {
                  break;
               }
            case 156:
               this._DebugEmbeddedLDAP = false;
               if (var2) {
                  break;
               }
            case 158:
               this._DebugEmbeddedLDAPLogLevel = 0;
               if (var2) {
                  break;
               }
            case 157:
               this._DebugEmbeddedLDAPLogToConsole = false;
               if (var2) {
                  break;
               }
            case 159:
               this._DebugEmbeddedLDAPWriteOverrideProps = false;
               if (var2) {
                  break;
               }
            case 70:
               this._DebugEventManager = false;
               if (var2) {
                  break;
               }
            case 244:
               this._DebugFileDistributionServlet = false;
               if (var2) {
                  break;
               }
            case 51:
               this._DebugHttp = false;
               if (var2) {
                  break;
               }
            case 54:
               this._DebugHttpLogging = false;
               if (var2) {
                  break;
               }
            case 53:
               this._DebugHttpSessions = false;
               if (var2) {
                  break;
               }
            case 302:
               this._DebugIIOPNaming = false;
               if (var2) {
                  break;
               }
            case 303:
               this._DebugIIOPTunneling = false;
               if (var2) {
                  break;
               }
            case 301:
               this._DebugJ2EEManagement = false;
               if (var2) {
                  break;
               }
            case 211:
               this._DebugJAXPDebugLevel = 0;
               if (var2) {
                  break;
               }
            case 212:
               this._DebugJAXPDebugName = null;
               if (var2) {
                  break;
               }
            case 216:
               this._DebugJAXPIncludeClass = false;
               if (var2) {
                  break;
               }
            case 217:
               this._DebugJAXPIncludeLocation = false;
               if (var2) {
                  break;
               }
            case 215:
               this._DebugJAXPIncludeName = false;
               if (var2) {
                  break;
               }
            case 214:
               this._DebugJAXPIncludeTime = false;
               if (var2) {
                  break;
               }
            case 213:
               this._DebugJAXPOutputStream = null;
               if (var2) {
                  break;
               }
            case 218:
               this._DebugJAXPUseShortClass = false;
               if (var2) {
                  break;
               }
            case 182:
               this._DebugJDBCConn = false;
               if (var2) {
                  break;
               }
            case 185:
               this._DebugJDBCDriverLogging = false;
               if (var2) {
                  break;
               }
            case 186:
               this._DebugJDBCInternal = false;
               if (var2) {
                  break;
               }
            case 188:
               this._DebugJDBCONS = false;
               if (var2) {
                  break;
               }
            case 187:
               this._DebugJDBCRAC = false;
               if (var2) {
                  break;
               }
            case 190:
               this._DebugJDBCREPLAY = false;
               if (var2) {
                  break;
               }
            case 184:
               this._DebugJDBCRMI = false;
               if (var2) {
                  break;
               }
            case 183:
               this._DebugJDBCSQL = false;
               if (var2) {
                  break;
               }
            case 189:
               this._DebugJDBCUCP = false;
               if (var2) {
                  break;
               }
            case 103:
               this._DebugJMSAME = false;
               if (var2) {
                  break;
               }
            case 91:
               this._DebugJMSBackEnd = false;
               if (var2) {
                  break;
               }
            case 100:
               this._DebugJMSBoot = false;
               if (var2) {
                  break;
               }
            case 109:
               this._DebugJMSCDS = false;
               if (var2) {
                  break;
               }
            case 93:
               this._DebugJMSCommon = false;
               if (var2) {
                  break;
               }
            case 94:
               this._DebugJMSConfig = false;
               if (var2) {
                  break;
               }
            case 98:
               this._DebugJMSDispatcher = false;
               if (var2) {
                  break;
               }
            case 95:
               this._DebugJMSDistTopic = false;
               if (var2) {
                  break;
               }
            case 101:
               this._DebugJMSDurableSubscribers = false;
               if (var2) {
                  break;
               }
            case 92:
               this._DebugJMSFrontEnd = false;
               if (var2) {
                  break;
               }
            case 102:
               this._DebugJMSJDBCScavengeOnFlush = false;
               if (var2) {
                  break;
               }
            case 96:
               this._DebugJMSLocking = false;
               if (var2) {
                  break;
               }
            case 106:
               this._DebugJMSMessagePath = false;
               if (var2) {
                  break;
               }
            case 105:
               this._DebugJMSModule = false;
               if (var2) {
                  break;
               }
            case 104:
               this._DebugJMSPauseResume = false;
               if (var2) {
                  break;
               }
            case 107:
               this._DebugJMSSAF = false;
               if (var2) {
                  break;
               }
            case 99:
               this._DebugJMSStore = false;
               if (var2) {
                  break;
               }
            case 324:
               this._DebugJMST3Server = false;
               if (var2) {
                  break;
               }
            case 108:
               this._DebugJMSWrappers = false;
               if (var2) {
                  break;
               }
            case 97:
               this._DebugJMSXA = false;
               if (var2) {
                  break;
               }
            case 293:
               this._DebugJMX = false;
               if (var2) {
                  break;
               }
            case 298:
               this._DebugJMXCompatibility = false;
               if (var2) {
                  break;
               }
            case 294:
               this._DebugJMXCore = false;
               if (var2) {
                  break;
               }
            case 296:
               this._DebugJMXDomain = false;
               if (var2) {
                  break;
               }
            case 297:
               this._DebugJMXEdit = false;
               if (var2) {
                  break;
               }
            case 295:
               this._DebugJMXRuntime = false;
               if (var2) {
                  break;
               }
            case 86:
               this._DebugJNDI = false;
               if (var2) {
                  break;
               }
            case 88:
               this._DebugJNDIFactories = false;
               if (var2) {
                  break;
               }
            case 87:
               this._DebugJNDIResolution = false;
               if (var2) {
                  break;
               }
            case 114:
               this._DebugJTA2PC = false;
               if (var2) {
                  break;
               }
            case 115:
               this._DebugJTA2PCStackTrace = false;
               if (var2) {
                  break;
               }
            case 120:
               this._DebugJTAAPI = false;
               if (var2) {
                  break;
               }
            case 122:
               this._DebugJTAGateway = false;
               if (var2) {
                  break;
               }
            case 123:
               this._DebugJTAGatewayStackTrace = false;
               if (var2) {
                  break;
               }
            case 130:
               this._DebugJTAHealth = false;
               if (var2) {
                  break;
               }
            case 117:
               this._DebugJTAJDBC = false;
               if (var2) {
                  break;
               }
            case 129:
               this._DebugJTALLR = false;
               if (var2) {
                  break;
               }
            case 128:
               this._DebugJTALifecycle = false;
               if (var2) {
                  break;
               }
            case 127:
               this._DebugJTAMigration = false;
               if (var2) {
                  break;
               }
            case 124:
               this._DebugJTANaming = false;
               if (var2) {
                  break;
               }
            case 125:
               this._DebugJTANamingStackTrace = false;
               if (var2) {
                  break;
               }
            case 111:
               this._DebugJTANonXA = false;
               if (var2) {
                  break;
               }
            case 121:
               this._DebugJTAPropagate = false;
               if (var2) {
                  break;
               }
            case 113:
               this._DebugJTARMI = false;
               if (var2) {
                  break;
               }
            case 118:
               this._DebugJTARecovery = false;
               if (var2) {
                  break;
               }
            case 119:
               this._DebugJTARecoveryStackTrace = false;
               if (var2) {
                  break;
               }
            case 126:
               this._DebugJTAResourceHealth = false;
               if (var2) {
                  break;
               }
            case 132:
               this._DebugJTAResourceName = null;
               if (var2) {
                  break;
               }
            case 116:
               this._DebugJTATLOG = false;
               if (var2) {
                  break;
               }
            case 131:
               this._DebugJTATransactionName = null;
               if (var2) {
                  break;
               }
            case 110:
               this._DebugJTAXA = false;
               if (var2) {
                  break;
               }
            case 112:
               this._DebugJTAXAStackTrace = false;
               if (var2) {
                  break;
               }
            case 317:
               this._DebugJpaDataCache = false;
               if (var2) {
                  break;
               }
            case 314:
               this._DebugJpaEnhance = false;
               if (var2) {
                  break;
               }
            case 322:
               this._DebugJpaJdbcJdbc = false;
               if (var2) {
                  break;
               }
            case 323:
               this._DebugJpaJdbcSchema = false;
               if (var2) {
                  break;
               }
            case 321:
               this._DebugJpaJdbcSql = false;
               if (var2) {
                  break;
               }
            case 319:
               this._DebugJpaManage = false;
               if (var2) {
                  break;
               }
            case 313:
               this._DebugJpaMetaData = false;
               if (var2) {
                  break;
               }
            case 320:
               this._DebugJpaProfile = false;
               if (var2) {
                  break;
               }
            case 316:
               this._DebugJpaQuery = false;
               if (var2) {
                  break;
               }
            case 315:
               this._DebugJpaRuntime = false;
               if (var2) {
                  break;
               }
            case 318:
               this._DebugJpaTool = false;
               if (var2) {
                  break;
               }
            case 79:
               this._DebugLeaderElection = false;
               if (var2) {
                  break;
               }
            case 45:
               this._DebugLibraries = false;
               if (var2) {
                  break;
               }
            case 273:
               this._DebugLoggingConfiguration = false;
               if (var2) {
                  break;
               }
            case 327:
               this._DebugManagementServicesResource = false;
               if (var2) {
                  break;
               }
            case 36:
               this._DebugMaskCriterias = new String[0];
               if (var2) {
                  break;
               }
            case 195:
               this._DebugMessagingBridgeDumpToConsole = false;
               if (var2) {
                  break;
               }
            case 194:
               this._DebugMessagingBridgeDumpToLog = false;
               if (var2) {
                  break;
               }
            case 192:
               this._DebugMessagingBridgeRuntime = false;
               if (var2) {
                  break;
               }
            case 193:
               this._DebugMessagingBridgeRuntimeVerbose = false;
               if (var2) {
                  break;
               }
            case 191:
               this._DebugMessagingBridgeStartup = false;
               if (var2) {
                  break;
               }
            case 133:
               this._DebugMessagingKernel = false;
               if (var2) {
                  break;
               }
            case 134:
               this._DebugMessagingKernelBoot = false;
               if (var2) {
                  break;
               }
            case 144:
               this._DebugPathSvc = false;
               if (var2) {
                  break;
               }
            case 145:
               this._DebugPathSvcVerbose = false;
               if (var2) {
                  break;
               }
            case 276:
               this._DebugRA = false;
               if (var2) {
                  break;
               }
            case 291:
               this._DebugRAClassloader = false;
               if (var2) {
                  break;
               }
            case 288:
               this._DebugRAConnEvents = false;
               if (var2) {
                  break;
               }
            case 287:
               this._DebugRAConnections = false;
               if (var2) {
                  break;
               }
            case 283:
               this._DebugRADeployment = false;
               if (var2) {
                  break;
               }
            case 281:
               this._DebugRALifecycle = false;
               if (var2) {
                  break;
               }
            case 280:
               this._DebugRALocalOut = false;
               if (var2) {
                  break;
               }
            case 284:
               this._DebugRAParsing = false;
               if (var2) {
                  break;
               }
            case 275:
               this._DebugRAPoolVerbose = false;
               if (var2) {
                  break;
               }
            case 286:
               this._DebugRAPooling = false;
               if (var2) {
                  break;
               }
            case 285:
               this._DebugRASecurityCtx = false;
               if (var2) {
                  break;
               }
            case 289:
               this._DebugRAWork = false;
               if (var2) {
                  break;
               }
            case 290:
               this._DebugRAWorkEvents = false;
               if (var2) {
                  break;
               }
            case 277:
               this._DebugRAXAin = false;
               if (var2) {
                  break;
               }
            case 278:
               this._DebugRAXAout = false;
               if (var2) {
                  break;
               }
            case 279:
               this._DebugRAXAwork = false;
               if (var2) {
                  break;
               }
            case 76:
               this._DebugReplication = false;
               if (var2) {
                  break;
               }
            case 77:
               this._DebugReplicationDetails = false;
               if (var2) {
                  break;
               }
            case 136:
               this._DebugSAFAdmin = false;
               if (var2) {
                  break;
               }
            case 135:
               this._DebugSAFLifeCycle = false;
               if (var2) {
                  break;
               }
            case 137:
               this._DebugSAFManager = false;
               if (var2) {
                  break;
               }
            case 141:
               this._DebugSAFMessagePath = false;
               if (var2) {
                  break;
               }
            case 139:
               this._DebugSAFReceivingAgent = false;
               if (var2) {
                  break;
               }
            case 138:
               this._DebugSAFSendingAgent = false;
               if (var2) {
                  break;
               }
            case 142:
               this._DebugSAFStore = false;
               if (var2) {
                  break;
               }
            case 140:
               this._DebugSAFTransport = false;
               if (var2) {
                  break;
               }
            case 143:
               this._DebugSAFVerbose = false;
               if (var2) {
                  break;
               }
            case 269:
               this._DebugSNMPAgent = false;
               if (var2) {
                  break;
               }
            case 271:
               this._DebugSNMPExtensionProvider = false;
               if (var2) {
                  break;
               }
            case 270:
               this._DebugSNMPProtocolTCP = false;
               if (var2) {
                  break;
               }
            case 268:
               this._DebugSNMPToolkit = false;
               if (var2) {
                  break;
               }
            case 146:
               this._DebugScaContainer = false;
               if (var2) {
                  break;
               }
            case 148:
               this._DebugSecurity = false;
               if (var2) {
                  break;
               }
            case 160:
               this._DebugSecurityAdjudicator = false;
               if (var2) {
                  break;
               }
            case 161:
               this._DebugSecurityAtn = false;
               if (var2) {
                  break;
               }
            case 162:
               this._DebugSecurityAtz = false;
               if (var2) {
                  break;
               }
            case 163:
               this._DebugSecurityAuditor = false;
               if (var2) {
                  break;
               }
            case 167:
               this._DebugSecurityCertPath = false;
               if (var2) {
                  break;
               }
            case 164:
               this._DebugSecurityCredMap = false;
               if (var2) {
                  break;
               }
            case 170:
               this._DebugSecurityEEngine = false;
               if (var2) {
                  break;
               }
            case 165:
               this._DebugSecurityEncryptionService = false;
               if (var2) {
                  break;
               }
            case 171:
               this._DebugSecurityJACC = false;
               if (var2) {
                  break;
               }
            case 172:
               this._DebugSecurityJACCNonPolicy = false;
               if (var2) {
                  break;
               }
            case 173:
               this._DebugSecurityJACCPolicy = false;
               if (var2) {
                  break;
               }
            case 166:
               this._DebugSecurityKeyStore = false;
               if (var2) {
                  break;
               }
            case 149:
               this._DebugSecurityPasswordPolicy = false;
               if (var2) {
                  break;
               }
            case 152:
               this._DebugSecurityPredicate = false;
               if (var2) {
                  break;
               }
            case 168:
               this._DebugSecurityProfiler = false;
               if (var2) {
                  break;
               }
            case 147:
               this._DebugSecurityRealm = false;
               if (var2) {
                  break;
               }
            case 169:
               this._DebugSecurityRoleMap = false;
               if (var2) {
                  break;
               }
            case 179:
               this._DebugSecuritySAML2Atn = false;
               if (var2) {
                  break;
               }
            case 180:
               this._DebugSecuritySAML2CredMap = false;
               if (var2) {
                  break;
               }
            case 178:
               this._DebugSecuritySAML2Lib = false;
               if (var2) {
                  break;
               }
            case 181:
               this._DebugSecuritySAML2Service = false;
               if (var2) {
                  break;
               }
            case 175:
               this._DebugSecuritySAMLAtn = false;
               if (var2) {
                  break;
               }
            case 176:
               this._DebugSecuritySAMLCredMap = false;
               if (var2) {
                  break;
               }
            case 174:
               this._DebugSecuritySAMLLib = false;
               if (var2) {
                  break;
               }
            case 177:
               this._DebugSecuritySAMLService = false;
               if (var2) {
                  break;
               }
            case 153:
               this._DebugSecuritySSL = false;
               if (var2) {
                  break;
               }
            case 154:
               this._DebugSecuritySSLEaten = false;
               if (var2) {
                  break;
               }
            case 151:
               this._DebugSecurityService = false;
               if (var2) {
                  break;
               }
            case 150:
               this._DebugSecurityUserLockout = false;
               if (var2) {
                  break;
               }
            case 34:
               this._DebugSelfTuning = false;
               if (var2) {
                  break;
               }
            case 305:
               this._DebugServerLifeCycle = false;
               if (var2) {
                  break;
               }
            case 71:
               this._DebugServerMigration = false;
               if (var2) {
                  break;
               }
            case 326:
               this._DebugServerStartStatistics = false;
               if (var2) {
                  break;
               }
            case 202:
               this._DebugStoreAdmin = false;
               if (var2) {
                  break;
               }
            case 196:
               this._DebugStoreIOLogical = false;
               if (var2) {
                  break;
               }
            case 197:
               this._DebugStoreIOLogicalBoot = false;
               if (var2) {
                  break;
               }
            case 198:
               this._DebugStoreIOPhysical = false;
               if (var2) {
                  break;
               }
            case 199:
               this._DebugStoreIOPhysicalVerbose = false;
               if (var2) {
                  break;
               }
            case 200:
               this._DebugStoreXA = false;
               if (var2) {
                  break;
               }
            case 201:
               this._DebugStoreXAVerbose = false;
               if (var2) {
                  break;
               }
            case 90:
               this._DebugTunnelingConnection = false;
               if (var2) {
                  break;
               }
            case 89:
               this._DebugTunnelingConnectionTimeout = false;
               if (var2) {
                  break;
               }
            case 52:
               this._DebugURLResolution = false;
               if (var2) {
                  break;
               }
            case 292:
               this._DebugWANReplicationDetails = false;
               if (var2) {
                  break;
               }
            case 306:
               this._DebugWTCConfig = false;
               if (var2) {
                  break;
               }
            case 311:
               this._DebugWTCCorbaEx = false;
               if (var2) {
                  break;
               }
            case 309:
               this._DebugWTCGwtEx = false;
               if (var2) {
                  break;
               }
            case 310:
               this._DebugWTCJatmiEx = false;
               if (var2) {
                  break;
               }
            case 307:
               this._DebugWTCTDomPdu = false;
               if (var2) {
                  break;
               }
            case 308:
               this._DebugWTCUData = false;
               if (var2) {
                  break;
               }
            case 312:
               this._DebugWTCtBridgeEx = false;
               if (var2) {
                  break;
               }
            case 55:
               this._DebugWebAppIdentityAssertion = false;
               if (var2) {
                  break;
               }
            case 57:
               this._DebugWebAppModule = false;
               if (var2) {
                  break;
               }
            case 56:
               this._DebugWebAppSecurity = false;
               if (var2) {
                  break;
               }
            case 219:
               this._DebugXMLEntityCacheDebugLevel = 0;
               if (var2) {
                  break;
               }
            case 220:
               this._DebugXMLEntityCacheDebugName = null;
               if (var2) {
                  break;
               }
            case 224:
               this._DebugXMLEntityCacheIncludeClass = false;
               if (var2) {
                  break;
               }
            case 225:
               this._DebugXMLEntityCacheIncludeLocation = false;
               if (var2) {
                  break;
               }
            case 223:
               this._DebugXMLEntityCacheIncludeName = false;
               if (var2) {
                  break;
               }
            case 222:
               this._DebugXMLEntityCacheIncludeTime = false;
               if (var2) {
                  break;
               }
            case 221:
               this._DebugXMLEntityCacheOutputStream = null;
               if (var2) {
                  break;
               }
            case 226:
               this._DebugXMLEntityCacheUseShortClass = false;
               if (var2) {
                  break;
               }
            case 203:
               this._DebugXMLRegistryDebugLevel = 0;
               if (var2) {
                  break;
               }
            case 204:
               this._DebugXMLRegistryDebugName = null;
               if (var2) {
                  break;
               }
            case 208:
               this._DebugXMLRegistryIncludeClass = false;
               if (var2) {
                  break;
               }
            case 209:
               this._DebugXMLRegistryIncludeLocation = false;
               if (var2) {
                  break;
               }
            case 207:
               this._DebugXMLRegistryIncludeName = false;
               if (var2) {
                  break;
               }
            case 206:
               this._DebugXMLRegistryIncludeTime = false;
               if (var2) {
                  break;
               }
            case 205:
               this._DebugXMLRegistryOutputStream = null;
               if (var2) {
                  break;
               }
            case 210:
               this._DebugXMLRegistryUseShortClass = false;
               if (var2) {
                  break;
               }
            case 49:
               this._DefaultStore = false;
               if (var2) {
                  break;
               }
            case 35:
               this._DiagnosticContextDebugMode = "Off";
               if (var2) {
                  break;
               }
            case 38:
               this._ListenThreadDebug = false;
               if (var2) {
                  break;
               }
            case 42:
               this._MagicThreadDumpBackToSocket = false;
               if (var2) {
                  break;
               }
            case 41:
               this._MagicThreadDumpFile = "debugMagicThreadDumpFile";
               if (var2) {
                  break;
               }
            case 40:
               this._MagicThreadDumpHost = "localhost";
               if (var2) {
                  break;
               }
            case 234:
               this._MasterDeployer = false;
               if (var2) {
                  break;
               }
            case 47:
               this._RedefiningClassLoader = false;
               if (var2) {
                  break;
               }
            case 37:
               this._Server = null;
               if (var2) {
                  break;
               }
            case 235:
               this._SlaveDeployer = false;
               if (var2) {
                  break;
               }
            case 239:
               this._WebModule = false;
               if (var2) {
                  break;
               }
            case 39:
               this._MagicThreadDumpEnabled = false;
               if (var2) {
                  break;
               }
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
      return "ServerDebug";
   }

   public void putValue(String var1, Object var2) {
      boolean var3;
      if (var1.equals("ApplicationContainer")) {
         var3 = this._ApplicationContainer;
         this._ApplicationContainer = (Boolean)var2;
         this._postSet(236, var3, this._ApplicationContainer);
      } else {
         String var5;
         if (var1.equals("BugReportServiceWsdlUrl")) {
            var5 = this._BugReportServiceWsdlUrl;
            this._BugReportServiceWsdlUrl = (String)var2;
            this._postSet(43, var5, this._BugReportServiceWsdlUrl);
         } else if (var1.equals("ClassChangeNotifier")) {
            var3 = this._ClassChangeNotifier;
            this._ClassChangeNotifier = (Boolean)var2;
            this._postSet(50, var3, this._ClassChangeNotifier);
         } else if (var1.equals("ClassFinder")) {
            var3 = this._ClassFinder;
            this._ClassFinder = (Boolean)var2;
            this._postSet(237, var3, this._ClassFinder);
         } else if (var1.equals("ClassLoader")) {
            var3 = this._ClassLoader;
            this._ClassLoader = (Boolean)var2;
            this._postSet(240, var3, this._ClassLoader);
         } else if (var1.equals("ClassLoaderVerbose")) {
            var3 = this._ClassLoaderVerbose;
            this._ClassLoaderVerbose = (Boolean)var2;
            this._postSet(241, var3, this._ClassLoaderVerbose);
         } else if (var1.equals("ClassloaderWebApp")) {
            var3 = this._ClassloaderWebApp;
            this._ClassloaderWebApp = (Boolean)var2;
            this._postSet(242, var3, this._ClassloaderWebApp);
         } else if (var1.equals("ClasspathServlet")) {
            var3 = this._ClasspathServlet;
            this._ClasspathServlet = (Boolean)var2;
            this._postSet(238, var3, this._ClasspathServlet);
         } else if (var1.equals("DebugAppContainer")) {
            var3 = this._DebugAppContainer;
            this._DebugAppContainer = (Boolean)var2;
            this._postSet(44, var3, this._DebugAppContainer);
         } else if (var1.equals("DebugAsyncQueue")) {
            var3 = this._DebugAsyncQueue;
            this._DebugAsyncQueue = (Boolean)var2;
            this._postSet(78, var3, this._DebugAsyncQueue);
         } else if (var1.equals("DebugBootstrapServlet")) {
            var3 = this._DebugBootstrapServlet;
            this._DebugBootstrapServlet = (Boolean)var2;
            this._postSet(243, var3, this._DebugBootstrapServlet);
         } else if (var1.equals("DebugCertRevocCheck")) {
            var3 = this._DebugCertRevocCheck;
            this._DebugCertRevocCheck = (Boolean)var2;
            this._postSet(155, var3, this._DebugCertRevocCheck);
         } else if (var1.equals("DebugClassRedef")) {
            var3 = this._DebugClassRedef;
            this._DebugClassRedef = (Boolean)var2;
            this._postSet(46, var3, this._DebugClassRedef);
         } else if (var1.equals("DebugClassSize")) {
            var3 = this._DebugClassSize;
            this._DebugClassSize = (Boolean)var2;
            this._postSet(48, var3, this._DebugClassSize);
         } else if (var1.equals("DebugCluster")) {
            var3 = this._DebugCluster;
            this._DebugCluster = (Boolean)var2;
            this._postSet(73, var3, this._DebugCluster);
         } else if (var1.equals("DebugClusterAnnouncements")) {
            var3 = this._DebugClusterAnnouncements;
            this._DebugClusterAnnouncements = (Boolean)var2;
            this._postSet(75, var3, this._DebugClusterAnnouncements);
         } else if (var1.equals("DebugClusterFragments")) {
            var3 = this._DebugClusterFragments;
            this._DebugClusterFragments = (Boolean)var2;
            this._postSet(72, var3, this._DebugClusterFragments);
         } else if (var1.equals("DebugClusterHeartbeats")) {
            var3 = this._DebugClusterHeartbeats;
            this._DebugClusterHeartbeats = (Boolean)var2;
            this._postSet(74, var3, this._DebugClusterHeartbeats);
         } else if (var1.equals("DebugConfigurationEdit")) {
            var3 = this._DebugConfigurationEdit;
            this._DebugConfigurationEdit = (Boolean)var2;
            this._postSet(299, var3, this._DebugConfigurationEdit);
         } else if (var1.equals("DebugConfigurationRuntime")) {
            var3 = this._DebugConfigurationRuntime;
            this._DebugConfigurationRuntime = (Boolean)var2;
            this._postSet(300, var3, this._DebugConfigurationRuntime);
         } else if (var1.equals("DebugConnectorService")) {
            var3 = this._DebugConnectorService;
            this._DebugConnectorService = (Boolean)var2;
            this._postSet(282, var3, this._DebugConnectorService);
         } else if (var1.equals("DebugConsensusLeasing")) {
            var3 = this._DebugConsensusLeasing;
            this._DebugConsensusLeasing = (Boolean)var2;
            this._postSet(304, var3, this._DebugConsensusLeasing);
         } else if (var1.equals("DebugDRSCalls")) {
            var3 = this._DebugDRSCalls;
            this._DebugDRSCalls = (Boolean)var2;
            this._postSet(80, var3, this._DebugDRSCalls);
         } else if (var1.equals("DebugDRSHeartbeats")) {
            var3 = this._DebugDRSHeartbeats;
            this._DebugDRSHeartbeats = (Boolean)var2;
            this._postSet(81, var3, this._DebugDRSHeartbeats);
         } else if (var1.equals("DebugDRSMessages")) {
            var3 = this._DebugDRSMessages;
            this._DebugDRSMessages = (Boolean)var2;
            this._postSet(82, var3, this._DebugDRSMessages);
         } else if (var1.equals("DebugDRSQueues")) {
            var3 = this._DebugDRSQueues;
            this._DebugDRSQueues = (Boolean)var2;
            this._postSet(85, var3, this._DebugDRSQueues);
         } else if (var1.equals("DebugDRSStateTransitions")) {
            var3 = this._DebugDRSStateTransitions;
            this._DebugDRSStateTransitions = (Boolean)var2;
            this._postSet(84, var3, this._DebugDRSStateTransitions);
         } else if (var1.equals("DebugDRSUpdateStatus")) {
            var3 = this._DebugDRSUpdateStatus;
            this._DebugDRSUpdateStatus = (Boolean)var2;
            this._postSet(83, var3, this._DebugDRSUpdateStatus);
         } else if (var1.equals("DebugDeploy")) {
            var3 = this._DebugDeploy;
            this._DebugDeploy = (Boolean)var2;
            this._postSet(227, var3, this._DebugDeploy);
         } else if (var1.equals("DebugDeployment")) {
            var3 = this._DebugDeployment;
            this._DebugDeployment = (Boolean)var2;
            this._postSet(228, var3, this._DebugDeployment);
         } else if (var1.equals("DebugDeploymentService")) {
            var3 = this._DebugDeploymentService;
            this._DebugDeploymentService = (Boolean)var2;
            this._postSet(229, var3, this._DebugDeploymentService);
         } else if (var1.equals("DebugDeploymentServiceInternal")) {
            var3 = this._DebugDeploymentServiceInternal;
            this._DebugDeploymentServiceInternal = (Boolean)var2;
            this._postSet(231, var3, this._DebugDeploymentServiceInternal);
         } else if (var1.equals("DebugDeploymentServiceStatusUpdates")) {
            var3 = this._DebugDeploymentServiceStatusUpdates;
            this._DebugDeploymentServiceStatusUpdates = (Boolean)var2;
            this._postSet(230, var3, this._DebugDeploymentServiceStatusUpdates);
         } else if (var1.equals("DebugDeploymentServiceTransport")) {
            var3 = this._DebugDeploymentServiceTransport;
            this._DebugDeploymentServiceTransport = (Boolean)var2;
            this._postSet(232, var3, this._DebugDeploymentServiceTransport);
         } else if (var1.equals("DebugDeploymentServiceTransportHttp")) {
            var3 = this._DebugDeploymentServiceTransportHttp;
            this._DebugDeploymentServiceTransportHttp = (Boolean)var2;
            this._postSet(233, var3, this._DebugDeploymentServiceTransportHttp);
         } else if (var1.equals("DebugDescriptor")) {
            var3 = this._DebugDescriptor;
            this._DebugDescriptor = (Boolean)var2;
            this._postSet(325, var3, this._DebugDescriptor);
         } else if (var1.equals("DebugDiagnosticAccessor")) {
            var3 = this._DebugDiagnosticAccessor;
            this._DebugDiagnosticAccessor = (Boolean)var2;
            this._postSet(265, var3, this._DebugDiagnosticAccessor);
         } else if (var1.equals("DebugDiagnosticArchive")) {
            var3 = this._DebugDiagnosticArchive;
            this._DebugDiagnosticArchive = (Boolean)var2;
            this._postSet(253, var3, this._DebugDiagnosticArchive);
         } else if (var1.equals("DebugDiagnosticArchiveRetirement")) {
            var3 = this._DebugDiagnosticArchiveRetirement;
            this._DebugDiagnosticArchiveRetirement = (Boolean)var2;
            this._postSet(257, var3, this._DebugDiagnosticArchiveRetirement);
         } else if (var1.equals("DebugDiagnosticCollections")) {
            var3 = this._DebugDiagnosticCollections;
            this._DebugDiagnosticCollections = (Boolean)var2;
            this._postSet(266, var3, this._DebugDiagnosticCollections);
         } else if (var1.equals("DebugDiagnosticContext")) {
            var3 = this._DebugDiagnosticContext;
            this._DebugDiagnosticContext = (Boolean)var2;
            this._postSet(267, var3, this._DebugDiagnosticContext);
         } else if (var1.equals("DebugDiagnosticDataGathering")) {
            var3 = this._DebugDiagnosticDataGathering;
            this._DebugDiagnosticDataGathering = (Boolean)var2;
            this._postSet(246, var3, this._DebugDiagnosticDataGathering);
         } else if (var1.equals("DebugDiagnosticFileArchive")) {
            var3 = this._DebugDiagnosticFileArchive;
            this._DebugDiagnosticFileArchive = (Boolean)var2;
            this._postSet(254, var3, this._DebugDiagnosticFileArchive);
         } else if (var1.equals("DebugDiagnosticImage")) {
            var3 = this._DebugDiagnosticImage;
            this._DebugDiagnosticImage = (Boolean)var2;
            this._postSet(263, var3, this._DebugDiagnosticImage);
         } else if (var1.equals("DebugDiagnosticInstrumentation")) {
            var3 = this._DebugDiagnosticInstrumentation;
            this._DebugDiagnosticInstrumentation = (Boolean)var2;
            this._postSet(247, var3, this._DebugDiagnosticInstrumentation);
         } else if (var1.equals("DebugDiagnosticInstrumentationActions")) {
            var3 = this._DebugDiagnosticInstrumentationActions;
            this._DebugDiagnosticInstrumentationActions = (Boolean)var2;
            this._postSet(250, var3, this._DebugDiagnosticInstrumentationActions);
         } else if (var1.equals("DebugDiagnosticInstrumentationConfig")) {
            var3 = this._DebugDiagnosticInstrumentationConfig;
            this._DebugDiagnosticInstrumentationConfig = (Boolean)var2;
            this._postSet(252, var3, this._DebugDiagnosticInstrumentationConfig);
         } else if (var1.equals("DebugDiagnosticInstrumentationEvents")) {
            var3 = this._DebugDiagnosticInstrumentationEvents;
            this._DebugDiagnosticInstrumentationEvents = (Boolean)var2;
            this._postSet(251, var3, this._DebugDiagnosticInstrumentationEvents);
         } else if (var1.equals("DebugDiagnosticInstrumentationWeaving")) {
            var3 = this._DebugDiagnosticInstrumentationWeaving;
            this._DebugDiagnosticInstrumentationWeaving = (Boolean)var2;
            this._postSet(248, var3, this._DebugDiagnosticInstrumentationWeaving);
         } else if (var1.equals("DebugDiagnosticInstrumentationWeavingMatches")) {
            var3 = this._DebugDiagnosticInstrumentationWeavingMatches;
            this._DebugDiagnosticInstrumentationWeavingMatches = (Boolean)var2;
            this._postSet(249, var3, this._DebugDiagnosticInstrumentationWeavingMatches);
         } else if (var1.equals("DebugDiagnosticJdbcArchive")) {
            var3 = this._DebugDiagnosticJdbcArchive;
            this._DebugDiagnosticJdbcArchive = (Boolean)var2;
            this._postSet(256, var3, this._DebugDiagnosticJdbcArchive);
         } else if (var1.equals("DebugDiagnosticLifecycleHandlers")) {
            var3 = this._DebugDiagnosticLifecycleHandlers;
            this._DebugDiagnosticLifecycleHandlers = (Boolean)var2;
            this._postSet(245, var3, this._DebugDiagnosticLifecycleHandlers);
         } else if (var1.equals("DebugDiagnosticQuery")) {
            var3 = this._DebugDiagnosticQuery;
            this._DebugDiagnosticQuery = (Boolean)var2;
            this._postSet(264, var3, this._DebugDiagnosticQuery);
         } else if (var1.equals("DebugDiagnosticWatch")) {
            var3 = this._DebugDiagnosticWatch;
            this._DebugDiagnosticWatch = (Boolean)var2;
            this._postSet(274, var3, this._DebugDiagnosticWatch);
         } else if (var1.equals("DebugDiagnosticWlstoreArchive")) {
            var3 = this._DebugDiagnosticWlstoreArchive;
            this._DebugDiagnosticWlstoreArchive = (Boolean)var2;
            this._postSet(255, var3, this._DebugDiagnosticWlstoreArchive);
         } else if (var1.equals("DebugDiagnosticsHarvester")) {
            var3 = this._DebugDiagnosticsHarvester;
            this._DebugDiagnosticsHarvester = (Boolean)var2;
            this._postSet(259, var3, this._DebugDiagnosticsHarvester);
         } else if (var1.equals("DebugDiagnosticsHarvesterData")) {
            var3 = this._DebugDiagnosticsHarvesterData;
            this._DebugDiagnosticsHarvesterData = (Boolean)var2;
            this._postSet(260, var3, this._DebugDiagnosticsHarvesterData);
         } else if (var1.equals("DebugDiagnosticsHarvesterMBeanPlugin")) {
            var3 = this._DebugDiagnosticsHarvesterMBeanPlugin;
            this._DebugDiagnosticsHarvesterMBeanPlugin = (Boolean)var2;
            this._postSet(261, var3, this._DebugDiagnosticsHarvesterMBeanPlugin);
         } else if (var1.equals("DebugDiagnosticsHarvesterTreeBeanPlugin")) {
            var3 = this._DebugDiagnosticsHarvesterTreeBeanPlugin;
            this._DebugDiagnosticsHarvesterTreeBeanPlugin = (Boolean)var2;
            this._postSet(262, var3, this._DebugDiagnosticsHarvesterTreeBeanPlugin);
         } else if (var1.equals("DebugDiagnosticsModule")) {
            var3 = this._DebugDiagnosticsModule;
            this._DebugDiagnosticsModule = (Boolean)var2;
            this._postSet(258, var3, this._DebugDiagnosticsModule);
         } else if (var1.equals("DebugDomainLogHandler")) {
            var3 = this._DebugDomainLogHandler;
            this._DebugDomainLogHandler = (Boolean)var2;
            this._postSet(272, var3, this._DebugDomainLogHandler);
         } else if (var1.equals("DebugEjbCaching")) {
            var3 = this._DebugEjbCaching;
            this._DebugEjbCaching = (Boolean)var2;
            this._postSet(61, var3, this._DebugEjbCaching);
         } else if (var1.equals("DebugEjbCmpDeployment")) {
            var3 = this._DebugEjbCmpDeployment;
            this._DebugEjbCmpDeployment = (Boolean)var2;
            this._postSet(68, var3, this._DebugEjbCmpDeployment);
         } else if (var1.equals("DebugEjbCmpRuntime")) {
            var3 = this._DebugEjbCmpRuntime;
            this._DebugEjbCmpRuntime = (Boolean)var2;
            this._postSet(69, var3, this._DebugEjbCmpRuntime);
         } else if (var1.equals("DebugEjbCompilation")) {
            var3 = this._DebugEjbCompilation;
            this._DebugEjbCompilation = (Boolean)var2;
            this._postSet(58, var3, this._DebugEjbCompilation);
         } else if (var1.equals("DebugEjbDeployment")) {
            var3 = this._DebugEjbDeployment;
            this._DebugEjbDeployment = (Boolean)var2;
            this._postSet(59, var3, this._DebugEjbDeployment);
         } else if (var1.equals("DebugEjbInvoke")) {
            var3 = this._DebugEjbInvoke;
            this._DebugEjbInvoke = (Boolean)var2;
            this._postSet(66, var3, this._DebugEjbInvoke);
         } else if (var1.equals("DebugEjbLocking")) {
            var3 = this._DebugEjbLocking;
            this._DebugEjbLocking = (Boolean)var2;
            this._postSet(63, var3, this._DebugEjbLocking);
         } else if (var1.equals("DebugEjbMdbConnection")) {
            var3 = this._DebugEjbMdbConnection;
            this._DebugEjbMdbConnection = (Boolean)var2;
            this._postSet(60, var3, this._DebugEjbMdbConnection);
         } else if (var1.equals("DebugEjbPooling")) {
            var3 = this._DebugEjbPooling;
            this._DebugEjbPooling = (Boolean)var2;
            this._postSet(64, var3, this._DebugEjbPooling);
         } else if (var1.equals("DebugEjbSecurity")) {
            var3 = this._DebugEjbSecurity;
            this._DebugEjbSecurity = (Boolean)var2;
            this._postSet(67, var3, this._DebugEjbSecurity);
         } else if (var1.equals("DebugEjbSwapping")) {
            var3 = this._DebugEjbSwapping;
            this._DebugEjbSwapping = (Boolean)var2;
            this._postSet(62, var3, this._DebugEjbSwapping);
         } else if (var1.equals("DebugEjbTimers")) {
            var3 = this._DebugEjbTimers;
            this._DebugEjbTimers = (Boolean)var2;
            this._postSet(65, var3, this._DebugEjbTimers);
         } else if (var1.equals("DebugEmbeddedLDAP")) {
            var3 = this._DebugEmbeddedLDAP;
            this._DebugEmbeddedLDAP = (Boolean)var2;
            this._postSet(156, var3, this._DebugEmbeddedLDAP);
         } else {
            int var7;
            if (var1.equals("DebugEmbeddedLDAPLogLevel")) {
               var7 = this._DebugEmbeddedLDAPLogLevel;
               this._DebugEmbeddedLDAPLogLevel = (Integer)var2;
               this._postSet(158, var7, this._DebugEmbeddedLDAPLogLevel);
            } else if (var1.equals("DebugEmbeddedLDAPLogToConsole")) {
               var3 = this._DebugEmbeddedLDAPLogToConsole;
               this._DebugEmbeddedLDAPLogToConsole = (Boolean)var2;
               this._postSet(157, var3, this._DebugEmbeddedLDAPLogToConsole);
            } else if (var1.equals("DebugEmbeddedLDAPWriteOverrideProps")) {
               var3 = this._DebugEmbeddedLDAPWriteOverrideProps;
               this._DebugEmbeddedLDAPWriteOverrideProps = (Boolean)var2;
               this._postSet(159, var3, this._DebugEmbeddedLDAPWriteOverrideProps);
            } else if (var1.equals("DebugEventManager")) {
               var3 = this._DebugEventManager;
               this._DebugEventManager = (Boolean)var2;
               this._postSet(70, var3, this._DebugEventManager);
            } else if (var1.equals("DebugFileDistributionServlet")) {
               var3 = this._DebugFileDistributionServlet;
               this._DebugFileDistributionServlet = (Boolean)var2;
               this._postSet(244, var3, this._DebugFileDistributionServlet);
            } else if (var1.equals("DebugHttp")) {
               var3 = this._DebugHttp;
               this._DebugHttp = (Boolean)var2;
               this._postSet(51, var3, this._DebugHttp);
            } else if (var1.equals("DebugHttpLogging")) {
               var3 = this._DebugHttpLogging;
               this._DebugHttpLogging = (Boolean)var2;
               this._postSet(54, var3, this._DebugHttpLogging);
            } else if (var1.equals("DebugHttpSessions")) {
               var3 = this._DebugHttpSessions;
               this._DebugHttpSessions = (Boolean)var2;
               this._postSet(53, var3, this._DebugHttpSessions);
            } else if (var1.equals("DebugIIOPNaming")) {
               var3 = this._DebugIIOPNaming;
               this._DebugIIOPNaming = (Boolean)var2;
               this._postSet(302, var3, this._DebugIIOPNaming);
            } else if (var1.equals("DebugIIOPTunneling")) {
               var3 = this._DebugIIOPTunneling;
               this._DebugIIOPTunneling = (Boolean)var2;
               this._postSet(303, var3, this._DebugIIOPTunneling);
            } else if (var1.equals("DebugJ2EEManagement")) {
               var3 = this._DebugJ2EEManagement;
               this._DebugJ2EEManagement = (Boolean)var2;
               this._postSet(301, var3, this._DebugJ2EEManagement);
            } else if (var1.equals("DebugJAXPDebugLevel")) {
               var7 = this._DebugJAXPDebugLevel;
               this._DebugJAXPDebugLevel = (Integer)var2;
               this._postSet(211, var7, this._DebugJAXPDebugLevel);
            } else if (var1.equals("DebugJAXPDebugName")) {
               var5 = this._DebugJAXPDebugName;
               this._DebugJAXPDebugName = (String)var2;
               this._postSet(212, var5, this._DebugJAXPDebugName);
            } else if (var1.equals("DebugJAXPIncludeClass")) {
               var3 = this._DebugJAXPIncludeClass;
               this._DebugJAXPIncludeClass = (Boolean)var2;
               this._postSet(216, var3, this._DebugJAXPIncludeClass);
            } else if (var1.equals("DebugJAXPIncludeLocation")) {
               var3 = this._DebugJAXPIncludeLocation;
               this._DebugJAXPIncludeLocation = (Boolean)var2;
               this._postSet(217, var3, this._DebugJAXPIncludeLocation);
            } else if (var1.equals("DebugJAXPIncludeName")) {
               var3 = this._DebugJAXPIncludeName;
               this._DebugJAXPIncludeName = (Boolean)var2;
               this._postSet(215, var3, this._DebugJAXPIncludeName);
            } else if (var1.equals("DebugJAXPIncludeTime")) {
               var3 = this._DebugJAXPIncludeTime;
               this._DebugJAXPIncludeTime = (Boolean)var2;
               this._postSet(214, var3, this._DebugJAXPIncludeTime);
            } else {
               OutputStream var6;
               if (var1.equals("DebugJAXPOutputStream")) {
                  var6 = this._DebugJAXPOutputStream;
                  this._DebugJAXPOutputStream = (OutputStream)var2;
                  this._postSet(213, var6, this._DebugJAXPOutputStream);
               } else if (var1.equals("DebugJAXPUseShortClass")) {
                  var3 = this._DebugJAXPUseShortClass;
                  this._DebugJAXPUseShortClass = (Boolean)var2;
                  this._postSet(218, var3, this._DebugJAXPUseShortClass);
               } else if (var1.equals("DebugJDBCConn")) {
                  var3 = this._DebugJDBCConn;
                  this._DebugJDBCConn = (Boolean)var2;
                  this._postSet(182, var3, this._DebugJDBCConn);
               } else if (var1.equals("DebugJDBCDriverLogging")) {
                  var3 = this._DebugJDBCDriverLogging;
                  this._DebugJDBCDriverLogging = (Boolean)var2;
                  this._postSet(185, var3, this._DebugJDBCDriverLogging);
               } else if (var1.equals("DebugJDBCInternal")) {
                  var3 = this._DebugJDBCInternal;
                  this._DebugJDBCInternal = (Boolean)var2;
                  this._postSet(186, var3, this._DebugJDBCInternal);
               } else if (var1.equals("DebugJDBCONS")) {
                  var3 = this._DebugJDBCONS;
                  this._DebugJDBCONS = (Boolean)var2;
                  this._postSet(188, var3, this._DebugJDBCONS);
               } else if (var1.equals("DebugJDBCRAC")) {
                  var3 = this._DebugJDBCRAC;
                  this._DebugJDBCRAC = (Boolean)var2;
                  this._postSet(187, var3, this._DebugJDBCRAC);
               } else if (var1.equals("DebugJDBCREPLAY")) {
                  var3 = this._DebugJDBCREPLAY;
                  this._DebugJDBCREPLAY = (Boolean)var2;
                  this._postSet(190, var3, this._DebugJDBCREPLAY);
               } else if (var1.equals("DebugJDBCRMI")) {
                  var3 = this._DebugJDBCRMI;
                  this._DebugJDBCRMI = (Boolean)var2;
                  this._postSet(184, var3, this._DebugJDBCRMI);
               } else if (var1.equals("DebugJDBCSQL")) {
                  var3 = this._DebugJDBCSQL;
                  this._DebugJDBCSQL = (Boolean)var2;
                  this._postSet(183, var3, this._DebugJDBCSQL);
               } else if (var1.equals("DebugJDBCUCP")) {
                  var3 = this._DebugJDBCUCP;
                  this._DebugJDBCUCP = (Boolean)var2;
                  this._postSet(189, var3, this._DebugJDBCUCP);
               } else if (var1.equals("DebugJMSAME")) {
                  var3 = this._DebugJMSAME;
                  this._DebugJMSAME = (Boolean)var2;
                  this._postSet(103, var3, this._DebugJMSAME);
               } else if (var1.equals("DebugJMSBackEnd")) {
                  var3 = this._DebugJMSBackEnd;
                  this._DebugJMSBackEnd = (Boolean)var2;
                  this._postSet(91, var3, this._DebugJMSBackEnd);
               } else if (var1.equals("DebugJMSBoot")) {
                  var3 = this._DebugJMSBoot;
                  this._DebugJMSBoot = (Boolean)var2;
                  this._postSet(100, var3, this._DebugJMSBoot);
               } else if (var1.equals("DebugJMSCDS")) {
                  var3 = this._DebugJMSCDS;
                  this._DebugJMSCDS = (Boolean)var2;
                  this._postSet(109, var3, this._DebugJMSCDS);
               } else if (var1.equals("DebugJMSCommon")) {
                  var3 = this._DebugJMSCommon;
                  this._DebugJMSCommon = (Boolean)var2;
                  this._postSet(93, var3, this._DebugJMSCommon);
               } else if (var1.equals("DebugJMSConfig")) {
                  var3 = this._DebugJMSConfig;
                  this._DebugJMSConfig = (Boolean)var2;
                  this._postSet(94, var3, this._DebugJMSConfig);
               } else if (var1.equals("DebugJMSDispatcher")) {
                  var3 = this._DebugJMSDispatcher;
                  this._DebugJMSDispatcher = (Boolean)var2;
                  this._postSet(98, var3, this._DebugJMSDispatcher);
               } else if (var1.equals("DebugJMSDistTopic")) {
                  var3 = this._DebugJMSDistTopic;
                  this._DebugJMSDistTopic = (Boolean)var2;
                  this._postSet(95, var3, this._DebugJMSDistTopic);
               } else if (var1.equals("DebugJMSDurableSubscribers")) {
                  var3 = this._DebugJMSDurableSubscribers;
                  this._DebugJMSDurableSubscribers = (Boolean)var2;
                  this._postSet(101, var3, this._DebugJMSDurableSubscribers);
               } else if (var1.equals("DebugJMSFrontEnd")) {
                  var3 = this._DebugJMSFrontEnd;
                  this._DebugJMSFrontEnd = (Boolean)var2;
                  this._postSet(92, var3, this._DebugJMSFrontEnd);
               } else if (var1.equals("DebugJMSJDBCScavengeOnFlush")) {
                  var3 = this._DebugJMSJDBCScavengeOnFlush;
                  this._DebugJMSJDBCScavengeOnFlush = (Boolean)var2;
                  this._postSet(102, var3, this._DebugJMSJDBCScavengeOnFlush);
               } else if (var1.equals("DebugJMSLocking")) {
                  var3 = this._DebugJMSLocking;
                  this._DebugJMSLocking = (Boolean)var2;
                  this._postSet(96, var3, this._DebugJMSLocking);
               } else if (var1.equals("DebugJMSMessagePath")) {
                  var3 = this._DebugJMSMessagePath;
                  this._DebugJMSMessagePath = (Boolean)var2;
                  this._postSet(106, var3, this._DebugJMSMessagePath);
               } else if (var1.equals("DebugJMSModule")) {
                  var3 = this._DebugJMSModule;
                  this._DebugJMSModule = (Boolean)var2;
                  this._postSet(105, var3, this._DebugJMSModule);
               } else if (var1.equals("DebugJMSPauseResume")) {
                  var3 = this._DebugJMSPauseResume;
                  this._DebugJMSPauseResume = (Boolean)var2;
                  this._postSet(104, var3, this._DebugJMSPauseResume);
               } else if (var1.equals("DebugJMSSAF")) {
                  var3 = this._DebugJMSSAF;
                  this._DebugJMSSAF = (Boolean)var2;
                  this._postSet(107, var3, this._DebugJMSSAF);
               } else if (var1.equals("DebugJMSStore")) {
                  var3 = this._DebugJMSStore;
                  this._DebugJMSStore = (Boolean)var2;
                  this._postSet(99, var3, this._DebugJMSStore);
               } else if (var1.equals("DebugJMST3Server")) {
                  var3 = this._DebugJMST3Server;
                  this._DebugJMST3Server = (Boolean)var2;
                  this._postSet(324, var3, this._DebugJMST3Server);
               } else if (var1.equals("DebugJMSWrappers")) {
                  var3 = this._DebugJMSWrappers;
                  this._DebugJMSWrappers = (Boolean)var2;
                  this._postSet(108, var3, this._DebugJMSWrappers);
               } else if (var1.equals("DebugJMSXA")) {
                  var3 = this._DebugJMSXA;
                  this._DebugJMSXA = (Boolean)var2;
                  this._postSet(97, var3, this._DebugJMSXA);
               } else if (var1.equals("DebugJMX")) {
                  var3 = this._DebugJMX;
                  this._DebugJMX = (Boolean)var2;
                  this._postSet(293, var3, this._DebugJMX);
               } else if (var1.equals("DebugJMXCompatibility")) {
                  var3 = this._DebugJMXCompatibility;
                  this._DebugJMXCompatibility = (Boolean)var2;
                  this._postSet(298, var3, this._DebugJMXCompatibility);
               } else if (var1.equals("DebugJMXCore")) {
                  var3 = this._DebugJMXCore;
                  this._DebugJMXCore = (Boolean)var2;
                  this._postSet(294, var3, this._DebugJMXCore);
               } else if (var1.equals("DebugJMXDomain")) {
                  var3 = this._DebugJMXDomain;
                  this._DebugJMXDomain = (Boolean)var2;
                  this._postSet(296, var3, this._DebugJMXDomain);
               } else if (var1.equals("DebugJMXEdit")) {
                  var3 = this._DebugJMXEdit;
                  this._DebugJMXEdit = (Boolean)var2;
                  this._postSet(297, var3, this._DebugJMXEdit);
               } else if (var1.equals("DebugJMXRuntime")) {
                  var3 = this._DebugJMXRuntime;
                  this._DebugJMXRuntime = (Boolean)var2;
                  this._postSet(295, var3, this._DebugJMXRuntime);
               } else if (var1.equals("DebugJNDI")) {
                  var3 = this._DebugJNDI;
                  this._DebugJNDI = (Boolean)var2;
                  this._postSet(86, var3, this._DebugJNDI);
               } else if (var1.equals("DebugJNDIFactories")) {
                  var3 = this._DebugJNDIFactories;
                  this._DebugJNDIFactories = (Boolean)var2;
                  this._postSet(88, var3, this._DebugJNDIFactories);
               } else if (var1.equals("DebugJNDIResolution")) {
                  var3 = this._DebugJNDIResolution;
                  this._DebugJNDIResolution = (Boolean)var2;
                  this._postSet(87, var3, this._DebugJNDIResolution);
               } else if (var1.equals("DebugJTA2PC")) {
                  var3 = this._DebugJTA2PC;
                  this._DebugJTA2PC = (Boolean)var2;
                  this._postSet(114, var3, this._DebugJTA2PC);
               } else if (var1.equals("DebugJTA2PCStackTrace")) {
                  var3 = this._DebugJTA2PCStackTrace;
                  this._DebugJTA2PCStackTrace = (Boolean)var2;
                  this._postSet(115, var3, this._DebugJTA2PCStackTrace);
               } else if (var1.equals("DebugJTAAPI")) {
                  var3 = this._DebugJTAAPI;
                  this._DebugJTAAPI = (Boolean)var2;
                  this._postSet(120, var3, this._DebugJTAAPI);
               } else if (var1.equals("DebugJTAGateway")) {
                  var3 = this._DebugJTAGateway;
                  this._DebugJTAGateway = (Boolean)var2;
                  this._postSet(122, var3, this._DebugJTAGateway);
               } else if (var1.equals("DebugJTAGatewayStackTrace")) {
                  var3 = this._DebugJTAGatewayStackTrace;
                  this._DebugJTAGatewayStackTrace = (Boolean)var2;
                  this._postSet(123, var3, this._DebugJTAGatewayStackTrace);
               } else if (var1.equals("DebugJTAHealth")) {
                  var3 = this._DebugJTAHealth;
                  this._DebugJTAHealth = (Boolean)var2;
                  this._postSet(130, var3, this._DebugJTAHealth);
               } else if (var1.equals("DebugJTAJDBC")) {
                  var3 = this._DebugJTAJDBC;
                  this._DebugJTAJDBC = (Boolean)var2;
                  this._postSet(117, var3, this._DebugJTAJDBC);
               } else if (var1.equals("DebugJTALLR")) {
                  var3 = this._DebugJTALLR;
                  this._DebugJTALLR = (Boolean)var2;
                  this._postSet(129, var3, this._DebugJTALLR);
               } else if (var1.equals("DebugJTALifecycle")) {
                  var3 = this._DebugJTALifecycle;
                  this._DebugJTALifecycle = (Boolean)var2;
                  this._postSet(128, var3, this._DebugJTALifecycle);
               } else if (var1.equals("DebugJTAMigration")) {
                  var3 = this._DebugJTAMigration;
                  this._DebugJTAMigration = (Boolean)var2;
                  this._postSet(127, var3, this._DebugJTAMigration);
               } else if (var1.equals("DebugJTANaming")) {
                  var3 = this._DebugJTANaming;
                  this._DebugJTANaming = (Boolean)var2;
                  this._postSet(124, var3, this._DebugJTANaming);
               } else if (var1.equals("DebugJTANamingStackTrace")) {
                  var3 = this._DebugJTANamingStackTrace;
                  this._DebugJTANamingStackTrace = (Boolean)var2;
                  this._postSet(125, var3, this._DebugJTANamingStackTrace);
               } else if (var1.equals("DebugJTANonXA")) {
                  var3 = this._DebugJTANonXA;
                  this._DebugJTANonXA = (Boolean)var2;
                  this._postSet(111, var3, this._DebugJTANonXA);
               } else if (var1.equals("DebugJTAPropagate")) {
                  var3 = this._DebugJTAPropagate;
                  this._DebugJTAPropagate = (Boolean)var2;
                  this._postSet(121, var3, this._DebugJTAPropagate);
               } else if (var1.equals("DebugJTARMI")) {
                  var3 = this._DebugJTARMI;
                  this._DebugJTARMI = (Boolean)var2;
                  this._postSet(113, var3, this._DebugJTARMI);
               } else if (var1.equals("DebugJTARecovery")) {
                  var3 = this._DebugJTARecovery;
                  this._DebugJTARecovery = (Boolean)var2;
                  this._postSet(118, var3, this._DebugJTARecovery);
               } else if (var1.equals("DebugJTARecoveryStackTrace")) {
                  var3 = this._DebugJTARecoveryStackTrace;
                  this._DebugJTARecoveryStackTrace = (Boolean)var2;
                  this._postSet(119, var3, this._DebugJTARecoveryStackTrace);
               } else if (var1.equals("DebugJTAResourceHealth")) {
                  var3 = this._DebugJTAResourceHealth;
                  this._DebugJTAResourceHealth = (Boolean)var2;
                  this._postSet(126, var3, this._DebugJTAResourceHealth);
               } else if (var1.equals("DebugJTAResourceName")) {
                  var5 = this._DebugJTAResourceName;
                  this._DebugJTAResourceName = (String)var2;
                  this._postSet(132, var5, this._DebugJTAResourceName);
               } else if (var1.equals("DebugJTATLOG")) {
                  var3 = this._DebugJTATLOG;
                  this._DebugJTATLOG = (Boolean)var2;
                  this._postSet(116, var3, this._DebugJTATLOG);
               } else if (var1.equals("DebugJTATransactionName")) {
                  var5 = this._DebugJTATransactionName;
                  this._DebugJTATransactionName = (String)var2;
                  this._postSet(131, var5, this._DebugJTATransactionName);
               } else if (var1.equals("DebugJTAXA")) {
                  var3 = this._DebugJTAXA;
                  this._DebugJTAXA = (Boolean)var2;
                  this._postSet(110, var3, this._DebugJTAXA);
               } else if (var1.equals("DebugJTAXAStackTrace")) {
                  var3 = this._DebugJTAXAStackTrace;
                  this._DebugJTAXAStackTrace = (Boolean)var2;
                  this._postSet(112, var3, this._DebugJTAXAStackTrace);
               } else if (var1.equals("DebugJpaDataCache")) {
                  var3 = this._DebugJpaDataCache;
                  this._DebugJpaDataCache = (Boolean)var2;
                  this._postSet(317, var3, this._DebugJpaDataCache);
               } else if (var1.equals("DebugJpaEnhance")) {
                  var3 = this._DebugJpaEnhance;
                  this._DebugJpaEnhance = (Boolean)var2;
                  this._postSet(314, var3, this._DebugJpaEnhance);
               } else if (var1.equals("DebugJpaJdbcJdbc")) {
                  var3 = this._DebugJpaJdbcJdbc;
                  this._DebugJpaJdbcJdbc = (Boolean)var2;
                  this._postSet(322, var3, this._DebugJpaJdbcJdbc);
               } else if (var1.equals("DebugJpaJdbcSchema")) {
                  var3 = this._DebugJpaJdbcSchema;
                  this._DebugJpaJdbcSchema = (Boolean)var2;
                  this._postSet(323, var3, this._DebugJpaJdbcSchema);
               } else if (var1.equals("DebugJpaJdbcSql")) {
                  var3 = this._DebugJpaJdbcSql;
                  this._DebugJpaJdbcSql = (Boolean)var2;
                  this._postSet(321, var3, this._DebugJpaJdbcSql);
               } else if (var1.equals("DebugJpaManage")) {
                  var3 = this._DebugJpaManage;
                  this._DebugJpaManage = (Boolean)var2;
                  this._postSet(319, var3, this._DebugJpaManage);
               } else if (var1.equals("DebugJpaMetaData")) {
                  var3 = this._DebugJpaMetaData;
                  this._DebugJpaMetaData = (Boolean)var2;
                  this._postSet(313, var3, this._DebugJpaMetaData);
               } else if (var1.equals("DebugJpaProfile")) {
                  var3 = this._DebugJpaProfile;
                  this._DebugJpaProfile = (Boolean)var2;
                  this._postSet(320, var3, this._DebugJpaProfile);
               } else if (var1.equals("DebugJpaQuery")) {
                  var3 = this._DebugJpaQuery;
                  this._DebugJpaQuery = (Boolean)var2;
                  this._postSet(316, var3, this._DebugJpaQuery);
               } else if (var1.equals("DebugJpaRuntime")) {
                  var3 = this._DebugJpaRuntime;
                  this._DebugJpaRuntime = (Boolean)var2;
                  this._postSet(315, var3, this._DebugJpaRuntime);
               } else if (var1.equals("DebugJpaTool")) {
                  var3 = this._DebugJpaTool;
                  this._DebugJpaTool = (Boolean)var2;
                  this._postSet(318, var3, this._DebugJpaTool);
               } else if (var1.equals("DebugLeaderElection")) {
                  var3 = this._DebugLeaderElection;
                  this._DebugLeaderElection = (Boolean)var2;
                  this._postSet(79, var3, this._DebugLeaderElection);
               } else if (var1.equals("DebugLibraries")) {
                  var3 = this._DebugLibraries;
                  this._DebugLibraries = (Boolean)var2;
                  this._postSet(45, var3, this._DebugLibraries);
               } else if (var1.equals("DebugLoggingConfiguration")) {
                  var3 = this._DebugLoggingConfiguration;
                  this._DebugLoggingConfiguration = (Boolean)var2;
                  this._postSet(273, var3, this._DebugLoggingConfiguration);
               } else if (var1.equals("DebugManagementServicesResource")) {
                  var3 = this._DebugManagementServicesResource;
                  this._DebugManagementServicesResource = (Boolean)var2;
                  this._postSet(327, var3, this._DebugManagementServicesResource);
               } else if (var1.equals("DebugMaskCriterias")) {
                  String[] var8 = this._DebugMaskCriterias;
                  this._DebugMaskCriterias = (String[])((String[])var2);
                  this._postSet(36, var8, this._DebugMaskCriterias);
               } else if (var1.equals("DebugMessagingBridgeDumpToConsole")) {
                  var3 = this._DebugMessagingBridgeDumpToConsole;
                  this._DebugMessagingBridgeDumpToConsole = (Boolean)var2;
                  this._postSet(195, var3, this._DebugMessagingBridgeDumpToConsole);
               } else if (var1.equals("DebugMessagingBridgeDumpToLog")) {
                  var3 = this._DebugMessagingBridgeDumpToLog;
                  this._DebugMessagingBridgeDumpToLog = (Boolean)var2;
                  this._postSet(194, var3, this._DebugMessagingBridgeDumpToLog);
               } else if (var1.equals("DebugMessagingBridgeRuntime")) {
                  var3 = this._DebugMessagingBridgeRuntime;
                  this._DebugMessagingBridgeRuntime = (Boolean)var2;
                  this._postSet(192, var3, this._DebugMessagingBridgeRuntime);
               } else if (var1.equals("DebugMessagingBridgeRuntimeVerbose")) {
                  var3 = this._DebugMessagingBridgeRuntimeVerbose;
                  this._DebugMessagingBridgeRuntimeVerbose = (Boolean)var2;
                  this._postSet(193, var3, this._DebugMessagingBridgeRuntimeVerbose);
               } else if (var1.equals("DebugMessagingBridgeStartup")) {
                  var3 = this._DebugMessagingBridgeStartup;
                  this._DebugMessagingBridgeStartup = (Boolean)var2;
                  this._postSet(191, var3, this._DebugMessagingBridgeStartup);
               } else if (var1.equals("DebugMessagingKernel")) {
                  var3 = this._DebugMessagingKernel;
                  this._DebugMessagingKernel = (Boolean)var2;
                  this._postSet(133, var3, this._DebugMessagingKernel);
               } else if (var1.equals("DebugMessagingKernelBoot")) {
                  var3 = this._DebugMessagingKernelBoot;
                  this._DebugMessagingKernelBoot = (Boolean)var2;
                  this._postSet(134, var3, this._DebugMessagingKernelBoot);
               } else if (var1.equals("DebugPathSvc")) {
                  var3 = this._DebugPathSvc;
                  this._DebugPathSvc = (Boolean)var2;
                  this._postSet(144, var3, this._DebugPathSvc);
               } else if (var1.equals("DebugPathSvcVerbose")) {
                  var3 = this._DebugPathSvcVerbose;
                  this._DebugPathSvcVerbose = (Boolean)var2;
                  this._postSet(145, var3, this._DebugPathSvcVerbose);
               } else if (var1.equals("DebugRA")) {
                  var3 = this._DebugRA;
                  this._DebugRA = (Boolean)var2;
                  this._postSet(276, var3, this._DebugRA);
               } else if (var1.equals("DebugRAClassloader")) {
                  var3 = this._DebugRAClassloader;
                  this._DebugRAClassloader = (Boolean)var2;
                  this._postSet(291, var3, this._DebugRAClassloader);
               } else if (var1.equals("DebugRAConnEvents")) {
                  var3 = this._DebugRAConnEvents;
                  this._DebugRAConnEvents = (Boolean)var2;
                  this._postSet(288, var3, this._DebugRAConnEvents);
               } else if (var1.equals("DebugRAConnections")) {
                  var3 = this._DebugRAConnections;
                  this._DebugRAConnections = (Boolean)var2;
                  this._postSet(287, var3, this._DebugRAConnections);
               } else if (var1.equals("DebugRADeployment")) {
                  var3 = this._DebugRADeployment;
                  this._DebugRADeployment = (Boolean)var2;
                  this._postSet(283, var3, this._DebugRADeployment);
               } else if (var1.equals("DebugRALifecycle")) {
                  var3 = this._DebugRALifecycle;
                  this._DebugRALifecycle = (Boolean)var2;
                  this._postSet(281, var3, this._DebugRALifecycle);
               } else if (var1.equals("DebugRALocalOut")) {
                  var3 = this._DebugRALocalOut;
                  this._DebugRALocalOut = (Boolean)var2;
                  this._postSet(280, var3, this._DebugRALocalOut);
               } else if (var1.equals("DebugRAParsing")) {
                  var3 = this._DebugRAParsing;
                  this._DebugRAParsing = (Boolean)var2;
                  this._postSet(284, var3, this._DebugRAParsing);
               } else if (var1.equals("DebugRAPoolVerbose")) {
                  var3 = this._DebugRAPoolVerbose;
                  this._DebugRAPoolVerbose = (Boolean)var2;
                  this._postSet(275, var3, this._DebugRAPoolVerbose);
               } else if (var1.equals("DebugRAPooling")) {
                  var3 = this._DebugRAPooling;
                  this._DebugRAPooling = (Boolean)var2;
                  this._postSet(286, var3, this._DebugRAPooling);
               } else if (var1.equals("DebugRASecurityCtx")) {
                  var3 = this._DebugRASecurityCtx;
                  this._DebugRASecurityCtx = (Boolean)var2;
                  this._postSet(285, var3, this._DebugRASecurityCtx);
               } else if (var1.equals("DebugRAWork")) {
                  var3 = this._DebugRAWork;
                  this._DebugRAWork = (Boolean)var2;
                  this._postSet(289, var3, this._DebugRAWork);
               } else if (var1.equals("DebugRAWorkEvents")) {
                  var3 = this._DebugRAWorkEvents;
                  this._DebugRAWorkEvents = (Boolean)var2;
                  this._postSet(290, var3, this._DebugRAWorkEvents);
               } else if (var1.equals("DebugRAXAin")) {
                  var3 = this._DebugRAXAin;
                  this._DebugRAXAin = (Boolean)var2;
                  this._postSet(277, var3, this._DebugRAXAin);
               } else if (var1.equals("DebugRAXAout")) {
                  var3 = this._DebugRAXAout;
                  this._DebugRAXAout = (Boolean)var2;
                  this._postSet(278, var3, this._DebugRAXAout);
               } else if (var1.equals("DebugRAXAwork")) {
                  var3 = this._DebugRAXAwork;
                  this._DebugRAXAwork = (Boolean)var2;
                  this._postSet(279, var3, this._DebugRAXAwork);
               } else if (var1.equals("DebugReplication")) {
                  var3 = this._DebugReplication;
                  this._DebugReplication = (Boolean)var2;
                  this._postSet(76, var3, this._DebugReplication);
               } else if (var1.equals("DebugReplicationDetails")) {
                  var3 = this._DebugReplicationDetails;
                  this._DebugReplicationDetails = (Boolean)var2;
                  this._postSet(77, var3, this._DebugReplicationDetails);
               } else if (var1.equals("DebugSAFAdmin")) {
                  var3 = this._DebugSAFAdmin;
                  this._DebugSAFAdmin = (Boolean)var2;
                  this._postSet(136, var3, this._DebugSAFAdmin);
               } else if (var1.equals("DebugSAFLifeCycle")) {
                  var3 = this._DebugSAFLifeCycle;
                  this._DebugSAFLifeCycle = (Boolean)var2;
                  this._postSet(135, var3, this._DebugSAFLifeCycle);
               } else if (var1.equals("DebugSAFManager")) {
                  var3 = this._DebugSAFManager;
                  this._DebugSAFManager = (Boolean)var2;
                  this._postSet(137, var3, this._DebugSAFManager);
               } else if (var1.equals("DebugSAFMessagePath")) {
                  var3 = this._DebugSAFMessagePath;
                  this._DebugSAFMessagePath = (Boolean)var2;
                  this._postSet(141, var3, this._DebugSAFMessagePath);
               } else if (var1.equals("DebugSAFReceivingAgent")) {
                  var3 = this._DebugSAFReceivingAgent;
                  this._DebugSAFReceivingAgent = (Boolean)var2;
                  this._postSet(139, var3, this._DebugSAFReceivingAgent);
               } else if (var1.equals("DebugSAFSendingAgent")) {
                  var3 = this._DebugSAFSendingAgent;
                  this._DebugSAFSendingAgent = (Boolean)var2;
                  this._postSet(138, var3, this._DebugSAFSendingAgent);
               } else if (var1.equals("DebugSAFStore")) {
                  var3 = this._DebugSAFStore;
                  this._DebugSAFStore = (Boolean)var2;
                  this._postSet(142, var3, this._DebugSAFStore);
               } else if (var1.equals("DebugSAFTransport")) {
                  var3 = this._DebugSAFTransport;
                  this._DebugSAFTransport = (Boolean)var2;
                  this._postSet(140, var3, this._DebugSAFTransport);
               } else if (var1.equals("DebugSAFVerbose")) {
                  var3 = this._DebugSAFVerbose;
                  this._DebugSAFVerbose = (Boolean)var2;
                  this._postSet(143, var3, this._DebugSAFVerbose);
               } else if (var1.equals("DebugSNMPAgent")) {
                  var3 = this._DebugSNMPAgent;
                  this._DebugSNMPAgent = (Boolean)var2;
                  this._postSet(269, var3, this._DebugSNMPAgent);
               } else if (var1.equals("DebugSNMPExtensionProvider")) {
                  var3 = this._DebugSNMPExtensionProvider;
                  this._DebugSNMPExtensionProvider = (Boolean)var2;
                  this._postSet(271, var3, this._DebugSNMPExtensionProvider);
               } else if (var1.equals("DebugSNMPProtocolTCP")) {
                  var3 = this._DebugSNMPProtocolTCP;
                  this._DebugSNMPProtocolTCP = (Boolean)var2;
                  this._postSet(270, var3, this._DebugSNMPProtocolTCP);
               } else if (var1.equals("DebugSNMPToolkit")) {
                  var3 = this._DebugSNMPToolkit;
                  this._DebugSNMPToolkit = (Boolean)var2;
                  this._postSet(268, var3, this._DebugSNMPToolkit);
               } else if (var1.equals("DebugScaContainer")) {
                  var3 = this._DebugScaContainer;
                  this._DebugScaContainer = (Boolean)var2;
                  this._postSet(146, var3, this._DebugScaContainer);
               } else if (var1.equals("DebugSecurity")) {
                  var3 = this._DebugSecurity;
                  this._DebugSecurity = (Boolean)var2;
                  this._postSet(148, var3, this._DebugSecurity);
               } else if (var1.equals("DebugSecurityAdjudicator")) {
                  var3 = this._DebugSecurityAdjudicator;
                  this._DebugSecurityAdjudicator = (Boolean)var2;
                  this._postSet(160, var3, this._DebugSecurityAdjudicator);
               } else if (var1.equals("DebugSecurityAtn")) {
                  var3 = this._DebugSecurityAtn;
                  this._DebugSecurityAtn = (Boolean)var2;
                  this._postSet(161, var3, this._DebugSecurityAtn);
               } else if (var1.equals("DebugSecurityAtz")) {
                  var3 = this._DebugSecurityAtz;
                  this._DebugSecurityAtz = (Boolean)var2;
                  this._postSet(162, var3, this._DebugSecurityAtz);
               } else if (var1.equals("DebugSecurityAuditor")) {
                  var3 = this._DebugSecurityAuditor;
                  this._DebugSecurityAuditor = (Boolean)var2;
                  this._postSet(163, var3, this._DebugSecurityAuditor);
               } else if (var1.equals("DebugSecurityCertPath")) {
                  var3 = this._DebugSecurityCertPath;
                  this._DebugSecurityCertPath = (Boolean)var2;
                  this._postSet(167, var3, this._DebugSecurityCertPath);
               } else if (var1.equals("DebugSecurityCredMap")) {
                  var3 = this._DebugSecurityCredMap;
                  this._DebugSecurityCredMap = (Boolean)var2;
                  this._postSet(164, var3, this._DebugSecurityCredMap);
               } else if (var1.equals("DebugSecurityEEngine")) {
                  var3 = this._DebugSecurityEEngine;
                  this._DebugSecurityEEngine = (Boolean)var2;
                  this._postSet(170, var3, this._DebugSecurityEEngine);
               } else if (var1.equals("DebugSecurityEncryptionService")) {
                  var3 = this._DebugSecurityEncryptionService;
                  this._DebugSecurityEncryptionService = (Boolean)var2;
                  this._postSet(165, var3, this._DebugSecurityEncryptionService);
               } else if (var1.equals("DebugSecurityJACC")) {
                  var3 = this._DebugSecurityJACC;
                  this._DebugSecurityJACC = (Boolean)var2;
                  this._postSet(171, var3, this._DebugSecurityJACC);
               } else if (var1.equals("DebugSecurityJACCNonPolicy")) {
                  var3 = this._DebugSecurityJACCNonPolicy;
                  this._DebugSecurityJACCNonPolicy = (Boolean)var2;
                  this._postSet(172, var3, this._DebugSecurityJACCNonPolicy);
               } else if (var1.equals("DebugSecurityJACCPolicy")) {
                  var3 = this._DebugSecurityJACCPolicy;
                  this._DebugSecurityJACCPolicy = (Boolean)var2;
                  this._postSet(173, var3, this._DebugSecurityJACCPolicy);
               } else if (var1.equals("DebugSecurityKeyStore")) {
                  var3 = this._DebugSecurityKeyStore;
                  this._DebugSecurityKeyStore = (Boolean)var2;
                  this._postSet(166, var3, this._DebugSecurityKeyStore);
               } else if (var1.equals("DebugSecurityPasswordPolicy")) {
                  var3 = this._DebugSecurityPasswordPolicy;
                  this._DebugSecurityPasswordPolicy = (Boolean)var2;
                  this._postSet(149, var3, this._DebugSecurityPasswordPolicy);
               } else if (var1.equals("DebugSecurityPredicate")) {
                  var3 = this._DebugSecurityPredicate;
                  this._DebugSecurityPredicate = (Boolean)var2;
                  this._postSet(152, var3, this._DebugSecurityPredicate);
               } else if (var1.equals("DebugSecurityProfiler")) {
                  var3 = this._DebugSecurityProfiler;
                  this._DebugSecurityProfiler = (Boolean)var2;
                  this._postSet(168, var3, this._DebugSecurityProfiler);
               } else if (var1.equals("DebugSecurityRealm")) {
                  var3 = this._DebugSecurityRealm;
                  this._DebugSecurityRealm = (Boolean)var2;
                  this._postSet(147, var3, this._DebugSecurityRealm);
               } else if (var1.equals("DebugSecurityRoleMap")) {
                  var3 = this._DebugSecurityRoleMap;
                  this._DebugSecurityRoleMap = (Boolean)var2;
                  this._postSet(169, var3, this._DebugSecurityRoleMap);
               } else if (var1.equals("DebugSecuritySAML2Atn")) {
                  var3 = this._DebugSecuritySAML2Atn;
                  this._DebugSecuritySAML2Atn = (Boolean)var2;
                  this._postSet(179, var3, this._DebugSecuritySAML2Atn);
               } else if (var1.equals("DebugSecuritySAML2CredMap")) {
                  var3 = this._DebugSecuritySAML2CredMap;
                  this._DebugSecuritySAML2CredMap = (Boolean)var2;
                  this._postSet(180, var3, this._DebugSecuritySAML2CredMap);
               } else if (var1.equals("DebugSecuritySAML2Lib")) {
                  var3 = this._DebugSecuritySAML2Lib;
                  this._DebugSecuritySAML2Lib = (Boolean)var2;
                  this._postSet(178, var3, this._DebugSecuritySAML2Lib);
               } else if (var1.equals("DebugSecuritySAML2Service")) {
                  var3 = this._DebugSecuritySAML2Service;
                  this._DebugSecuritySAML2Service = (Boolean)var2;
                  this._postSet(181, var3, this._DebugSecuritySAML2Service);
               } else if (var1.equals("DebugSecuritySAMLAtn")) {
                  var3 = this._DebugSecuritySAMLAtn;
                  this._DebugSecuritySAMLAtn = (Boolean)var2;
                  this._postSet(175, var3, this._DebugSecuritySAMLAtn);
               } else if (var1.equals("DebugSecuritySAMLCredMap")) {
                  var3 = this._DebugSecuritySAMLCredMap;
                  this._DebugSecuritySAMLCredMap = (Boolean)var2;
                  this._postSet(176, var3, this._DebugSecuritySAMLCredMap);
               } else if (var1.equals("DebugSecuritySAMLLib")) {
                  var3 = this._DebugSecuritySAMLLib;
                  this._DebugSecuritySAMLLib = (Boolean)var2;
                  this._postSet(174, var3, this._DebugSecuritySAMLLib);
               } else if (var1.equals("DebugSecuritySAMLService")) {
                  var3 = this._DebugSecuritySAMLService;
                  this._DebugSecuritySAMLService = (Boolean)var2;
                  this._postSet(177, var3, this._DebugSecuritySAMLService);
               } else if (var1.equals("DebugSecuritySSL")) {
                  var3 = this._DebugSecuritySSL;
                  this._DebugSecuritySSL = (Boolean)var2;
                  this._postSet(153, var3, this._DebugSecuritySSL);
               } else if (var1.equals("DebugSecuritySSLEaten")) {
                  var3 = this._DebugSecuritySSLEaten;
                  this._DebugSecuritySSLEaten = (Boolean)var2;
                  this._postSet(154, var3, this._DebugSecuritySSLEaten);
               } else if (var1.equals("DebugSecurityService")) {
                  var3 = this._DebugSecurityService;
                  this._DebugSecurityService = (Boolean)var2;
                  this._postSet(151, var3, this._DebugSecurityService);
               } else if (var1.equals("DebugSecurityUserLockout")) {
                  var3 = this._DebugSecurityUserLockout;
                  this._DebugSecurityUserLockout = (Boolean)var2;
                  this._postSet(150, var3, this._DebugSecurityUserLockout);
               } else if (var1.equals("DebugSelfTuning")) {
                  var3 = this._DebugSelfTuning;
                  this._DebugSelfTuning = (Boolean)var2;
                  this._postSet(34, var3, this._DebugSelfTuning);
               } else if (var1.equals("DebugServerLifeCycle")) {
                  var3 = this._DebugServerLifeCycle;
                  this._DebugServerLifeCycle = (Boolean)var2;
                  this._postSet(305, var3, this._DebugServerLifeCycle);
               } else if (var1.equals("DebugServerMigration")) {
                  var3 = this._DebugServerMigration;
                  this._DebugServerMigration = (Boolean)var2;
                  this._postSet(71, var3, this._DebugServerMigration);
               } else if (var1.equals("DebugServerStartStatistics")) {
                  var3 = this._DebugServerStartStatistics;
                  this._DebugServerStartStatistics = (Boolean)var2;
                  this._postSet(326, var3, this._DebugServerStartStatistics);
               } else if (var1.equals("DebugStoreAdmin")) {
                  var3 = this._DebugStoreAdmin;
                  this._DebugStoreAdmin = (Boolean)var2;
                  this._postSet(202, var3, this._DebugStoreAdmin);
               } else if (var1.equals("DebugStoreIOLogical")) {
                  var3 = this._DebugStoreIOLogical;
                  this._DebugStoreIOLogical = (Boolean)var2;
                  this._postSet(196, var3, this._DebugStoreIOLogical);
               } else if (var1.equals("DebugStoreIOLogicalBoot")) {
                  var3 = this._DebugStoreIOLogicalBoot;
                  this._DebugStoreIOLogicalBoot = (Boolean)var2;
                  this._postSet(197, var3, this._DebugStoreIOLogicalBoot);
               } else if (var1.equals("DebugStoreIOPhysical")) {
                  var3 = this._DebugStoreIOPhysical;
                  this._DebugStoreIOPhysical = (Boolean)var2;
                  this._postSet(198, var3, this._DebugStoreIOPhysical);
               } else if (var1.equals("DebugStoreIOPhysicalVerbose")) {
                  var3 = this._DebugStoreIOPhysicalVerbose;
                  this._DebugStoreIOPhysicalVerbose = (Boolean)var2;
                  this._postSet(199, var3, this._DebugStoreIOPhysicalVerbose);
               } else if (var1.equals("DebugStoreXA")) {
                  var3 = this._DebugStoreXA;
                  this._DebugStoreXA = (Boolean)var2;
                  this._postSet(200, var3, this._DebugStoreXA);
               } else if (var1.equals("DebugStoreXAVerbose")) {
                  var3 = this._DebugStoreXAVerbose;
                  this._DebugStoreXAVerbose = (Boolean)var2;
                  this._postSet(201, var3, this._DebugStoreXAVerbose);
               } else if (var1.equals("DebugTunnelingConnection")) {
                  var3 = this._DebugTunnelingConnection;
                  this._DebugTunnelingConnection = (Boolean)var2;
                  this._postSet(90, var3, this._DebugTunnelingConnection);
               } else if (var1.equals("DebugTunnelingConnectionTimeout")) {
                  var3 = this._DebugTunnelingConnectionTimeout;
                  this._DebugTunnelingConnectionTimeout = (Boolean)var2;
                  this._postSet(89, var3, this._DebugTunnelingConnectionTimeout);
               } else if (var1.equals("DebugURLResolution")) {
                  var3 = this._DebugURLResolution;
                  this._DebugURLResolution = (Boolean)var2;
                  this._postSet(52, var3, this._DebugURLResolution);
               } else if (var1.equals("DebugWANReplicationDetails")) {
                  var3 = this._DebugWANReplicationDetails;
                  this._DebugWANReplicationDetails = (Boolean)var2;
                  this._postSet(292, var3, this._DebugWANReplicationDetails);
               } else if (var1.equals("DebugWTCConfig")) {
                  var3 = this._DebugWTCConfig;
                  this._DebugWTCConfig = (Boolean)var2;
                  this._postSet(306, var3, this._DebugWTCConfig);
               } else if (var1.equals("DebugWTCCorbaEx")) {
                  var3 = this._DebugWTCCorbaEx;
                  this._DebugWTCCorbaEx = (Boolean)var2;
                  this._postSet(311, var3, this._DebugWTCCorbaEx);
               } else if (var1.equals("DebugWTCGwtEx")) {
                  var3 = this._DebugWTCGwtEx;
                  this._DebugWTCGwtEx = (Boolean)var2;
                  this._postSet(309, var3, this._DebugWTCGwtEx);
               } else if (var1.equals("DebugWTCJatmiEx")) {
                  var3 = this._DebugWTCJatmiEx;
                  this._DebugWTCJatmiEx = (Boolean)var2;
                  this._postSet(310, var3, this._DebugWTCJatmiEx);
               } else if (var1.equals("DebugWTCTDomPdu")) {
                  var3 = this._DebugWTCTDomPdu;
                  this._DebugWTCTDomPdu = (Boolean)var2;
                  this._postSet(307, var3, this._DebugWTCTDomPdu);
               } else if (var1.equals("DebugWTCUData")) {
                  var3 = this._DebugWTCUData;
                  this._DebugWTCUData = (Boolean)var2;
                  this._postSet(308, var3, this._DebugWTCUData);
               } else if (var1.equals("DebugWTCtBridgeEx")) {
                  var3 = this._DebugWTCtBridgeEx;
                  this._DebugWTCtBridgeEx = (Boolean)var2;
                  this._postSet(312, var3, this._DebugWTCtBridgeEx);
               } else if (var1.equals("DebugWebAppIdentityAssertion")) {
                  var3 = this._DebugWebAppIdentityAssertion;
                  this._DebugWebAppIdentityAssertion = (Boolean)var2;
                  this._postSet(55, var3, this._DebugWebAppIdentityAssertion);
               } else if (var1.equals("DebugWebAppModule")) {
                  var3 = this._DebugWebAppModule;
                  this._DebugWebAppModule = (Boolean)var2;
                  this._postSet(57, var3, this._DebugWebAppModule);
               } else if (var1.equals("DebugWebAppSecurity")) {
                  var3 = this._DebugWebAppSecurity;
                  this._DebugWebAppSecurity = (Boolean)var2;
                  this._postSet(56, var3, this._DebugWebAppSecurity);
               } else if (var1.equals("DebugXMLEntityCacheDebugLevel")) {
                  var7 = this._DebugXMLEntityCacheDebugLevel;
                  this._DebugXMLEntityCacheDebugLevel = (Integer)var2;
                  this._postSet(219, var7, this._DebugXMLEntityCacheDebugLevel);
               } else if (var1.equals("DebugXMLEntityCacheDebugName")) {
                  var5 = this._DebugXMLEntityCacheDebugName;
                  this._DebugXMLEntityCacheDebugName = (String)var2;
                  this._postSet(220, var5, this._DebugXMLEntityCacheDebugName);
               } else if (var1.equals("DebugXMLEntityCacheIncludeClass")) {
                  var3 = this._DebugXMLEntityCacheIncludeClass;
                  this._DebugXMLEntityCacheIncludeClass = (Boolean)var2;
                  this._postSet(224, var3, this._DebugXMLEntityCacheIncludeClass);
               } else if (var1.equals("DebugXMLEntityCacheIncludeLocation")) {
                  var3 = this._DebugXMLEntityCacheIncludeLocation;
                  this._DebugXMLEntityCacheIncludeLocation = (Boolean)var2;
                  this._postSet(225, var3, this._DebugXMLEntityCacheIncludeLocation);
               } else if (var1.equals("DebugXMLEntityCacheIncludeName")) {
                  var3 = this._DebugXMLEntityCacheIncludeName;
                  this._DebugXMLEntityCacheIncludeName = (Boolean)var2;
                  this._postSet(223, var3, this._DebugXMLEntityCacheIncludeName);
               } else if (var1.equals("DebugXMLEntityCacheIncludeTime")) {
                  var3 = this._DebugXMLEntityCacheIncludeTime;
                  this._DebugXMLEntityCacheIncludeTime = (Boolean)var2;
                  this._postSet(222, var3, this._DebugXMLEntityCacheIncludeTime);
               } else if (var1.equals("DebugXMLEntityCacheOutputStream")) {
                  var6 = this._DebugXMLEntityCacheOutputStream;
                  this._DebugXMLEntityCacheOutputStream = (OutputStream)var2;
                  this._postSet(221, var6, this._DebugXMLEntityCacheOutputStream);
               } else if (var1.equals("DebugXMLEntityCacheUseShortClass")) {
                  var3 = this._DebugXMLEntityCacheUseShortClass;
                  this._DebugXMLEntityCacheUseShortClass = (Boolean)var2;
                  this._postSet(226, var3, this._DebugXMLEntityCacheUseShortClass);
               } else if (var1.equals("DebugXMLRegistryDebugLevel")) {
                  var7 = this._DebugXMLRegistryDebugLevel;
                  this._DebugXMLRegistryDebugLevel = (Integer)var2;
                  this._postSet(203, var7, this._DebugXMLRegistryDebugLevel);
               } else if (var1.equals("DebugXMLRegistryDebugName")) {
                  var5 = this._DebugXMLRegistryDebugName;
                  this._DebugXMLRegistryDebugName = (String)var2;
                  this._postSet(204, var5, this._DebugXMLRegistryDebugName);
               } else if (var1.equals("DebugXMLRegistryIncludeClass")) {
                  var3 = this._DebugXMLRegistryIncludeClass;
                  this._DebugXMLRegistryIncludeClass = (Boolean)var2;
                  this._postSet(208, var3, this._DebugXMLRegistryIncludeClass);
               } else if (var1.equals("DebugXMLRegistryIncludeLocation")) {
                  var3 = this._DebugXMLRegistryIncludeLocation;
                  this._DebugXMLRegistryIncludeLocation = (Boolean)var2;
                  this._postSet(209, var3, this._DebugXMLRegistryIncludeLocation);
               } else if (var1.equals("DebugXMLRegistryIncludeName")) {
                  var3 = this._DebugXMLRegistryIncludeName;
                  this._DebugXMLRegistryIncludeName = (Boolean)var2;
                  this._postSet(207, var3, this._DebugXMLRegistryIncludeName);
               } else if (var1.equals("DebugXMLRegistryIncludeTime")) {
                  var3 = this._DebugXMLRegistryIncludeTime;
                  this._DebugXMLRegistryIncludeTime = (Boolean)var2;
                  this._postSet(206, var3, this._DebugXMLRegistryIncludeTime);
               } else if (var1.equals("DebugXMLRegistryOutputStream")) {
                  var6 = this._DebugXMLRegistryOutputStream;
                  this._DebugXMLRegistryOutputStream = (OutputStream)var2;
                  this._postSet(205, var6, this._DebugXMLRegistryOutputStream);
               } else if (var1.equals("DebugXMLRegistryUseShortClass")) {
                  var3 = this._DebugXMLRegistryUseShortClass;
                  this._DebugXMLRegistryUseShortClass = (Boolean)var2;
                  this._postSet(210, var3, this._DebugXMLRegistryUseShortClass);
               } else if (var1.equals("DefaultStore")) {
                  var3 = this._DefaultStore;
                  this._DefaultStore = (Boolean)var2;
                  this._postSet(49, var3, this._DefaultStore);
               } else if (var1.equals("DiagnosticContextDebugMode")) {
                  var5 = this._DiagnosticContextDebugMode;
                  this._DiagnosticContextDebugMode = (String)var2;
                  this._postSet(35, var5, this._DiagnosticContextDebugMode);
               } else if (var1.equals("ListenThreadDebug")) {
                  var3 = this._ListenThreadDebug;
                  this._ListenThreadDebug = (Boolean)var2;
                  this._postSet(38, var3, this._ListenThreadDebug);
               } else if (var1.equals("MagicThreadDumpBackToSocket")) {
                  var3 = this._MagicThreadDumpBackToSocket;
                  this._MagicThreadDumpBackToSocket = (Boolean)var2;
                  this._postSet(42, var3, this._MagicThreadDumpBackToSocket);
               } else if (var1.equals("MagicThreadDumpEnabled")) {
                  var3 = this._MagicThreadDumpEnabled;
                  this._MagicThreadDumpEnabled = (Boolean)var2;
                  this._postSet(39, var3, this._MagicThreadDumpEnabled);
               } else if (var1.equals("MagicThreadDumpFile")) {
                  var5 = this._MagicThreadDumpFile;
                  this._MagicThreadDumpFile = (String)var2;
                  this._postSet(41, var5, this._MagicThreadDumpFile);
               } else if (var1.equals("MagicThreadDumpHost")) {
                  var5 = this._MagicThreadDumpHost;
                  this._MagicThreadDumpHost = (String)var2;
                  this._postSet(40, var5, this._MagicThreadDumpHost);
               } else if (var1.equals("MasterDeployer")) {
                  var3 = this._MasterDeployer;
                  this._MasterDeployer = (Boolean)var2;
                  this._postSet(234, var3, this._MasterDeployer);
               } else if (var1.equals("RedefiningClassLoader")) {
                  var3 = this._RedefiningClassLoader;
                  this._RedefiningClassLoader = (Boolean)var2;
                  this._postSet(47, var3, this._RedefiningClassLoader);
               } else if (var1.equals("Server")) {
                  ServerMBean var4 = this._Server;
                  this._Server = (ServerMBean)var2;
                  this._postSet(37, var4, this._Server);
               } else if (var1.equals("SlaveDeployer")) {
                  var3 = this._SlaveDeployer;
                  this._SlaveDeployer = (Boolean)var2;
                  this._postSet(235, var3, this._SlaveDeployer);
               } else if (var1.equals("WebModule")) {
                  var3 = this._WebModule;
                  this._WebModule = (Boolean)var2;
                  this._postSet(239, var3, this._WebModule);
               } else {
                  super.putValue(var1, var2);
               }
            }
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("ApplicationContainer")) {
         return new Boolean(this._ApplicationContainer);
      } else if (var1.equals("BugReportServiceWsdlUrl")) {
         return this._BugReportServiceWsdlUrl;
      } else if (var1.equals("ClassChangeNotifier")) {
         return new Boolean(this._ClassChangeNotifier);
      } else if (var1.equals("ClassFinder")) {
         return new Boolean(this._ClassFinder);
      } else if (var1.equals("ClassLoader")) {
         return new Boolean(this._ClassLoader);
      } else if (var1.equals("ClassLoaderVerbose")) {
         return new Boolean(this._ClassLoaderVerbose);
      } else if (var1.equals("ClassloaderWebApp")) {
         return new Boolean(this._ClassloaderWebApp);
      } else if (var1.equals("ClasspathServlet")) {
         return new Boolean(this._ClasspathServlet);
      } else if (var1.equals("DebugAppContainer")) {
         return new Boolean(this._DebugAppContainer);
      } else if (var1.equals("DebugAsyncQueue")) {
         return new Boolean(this._DebugAsyncQueue);
      } else if (var1.equals("DebugBootstrapServlet")) {
         return new Boolean(this._DebugBootstrapServlet);
      } else if (var1.equals("DebugCertRevocCheck")) {
         return new Boolean(this._DebugCertRevocCheck);
      } else if (var1.equals("DebugClassRedef")) {
         return new Boolean(this._DebugClassRedef);
      } else if (var1.equals("DebugClassSize")) {
         return new Boolean(this._DebugClassSize);
      } else if (var1.equals("DebugCluster")) {
         return new Boolean(this._DebugCluster);
      } else if (var1.equals("DebugClusterAnnouncements")) {
         return new Boolean(this._DebugClusterAnnouncements);
      } else if (var1.equals("DebugClusterFragments")) {
         return new Boolean(this._DebugClusterFragments);
      } else if (var1.equals("DebugClusterHeartbeats")) {
         return new Boolean(this._DebugClusterHeartbeats);
      } else if (var1.equals("DebugConfigurationEdit")) {
         return new Boolean(this._DebugConfigurationEdit);
      } else if (var1.equals("DebugConfigurationRuntime")) {
         return new Boolean(this._DebugConfigurationRuntime);
      } else if (var1.equals("DebugConnectorService")) {
         return new Boolean(this._DebugConnectorService);
      } else if (var1.equals("DebugConsensusLeasing")) {
         return new Boolean(this._DebugConsensusLeasing);
      } else if (var1.equals("DebugDRSCalls")) {
         return new Boolean(this._DebugDRSCalls);
      } else if (var1.equals("DebugDRSHeartbeats")) {
         return new Boolean(this._DebugDRSHeartbeats);
      } else if (var1.equals("DebugDRSMessages")) {
         return new Boolean(this._DebugDRSMessages);
      } else if (var1.equals("DebugDRSQueues")) {
         return new Boolean(this._DebugDRSQueues);
      } else if (var1.equals("DebugDRSStateTransitions")) {
         return new Boolean(this._DebugDRSStateTransitions);
      } else if (var1.equals("DebugDRSUpdateStatus")) {
         return new Boolean(this._DebugDRSUpdateStatus);
      } else if (var1.equals("DebugDeploy")) {
         return new Boolean(this._DebugDeploy);
      } else if (var1.equals("DebugDeployment")) {
         return new Boolean(this._DebugDeployment);
      } else if (var1.equals("DebugDeploymentService")) {
         return new Boolean(this._DebugDeploymentService);
      } else if (var1.equals("DebugDeploymentServiceInternal")) {
         return new Boolean(this._DebugDeploymentServiceInternal);
      } else if (var1.equals("DebugDeploymentServiceStatusUpdates")) {
         return new Boolean(this._DebugDeploymentServiceStatusUpdates);
      } else if (var1.equals("DebugDeploymentServiceTransport")) {
         return new Boolean(this._DebugDeploymentServiceTransport);
      } else if (var1.equals("DebugDeploymentServiceTransportHttp")) {
         return new Boolean(this._DebugDeploymentServiceTransportHttp);
      } else if (var1.equals("DebugDescriptor")) {
         return new Boolean(this._DebugDescriptor);
      } else if (var1.equals("DebugDiagnosticAccessor")) {
         return new Boolean(this._DebugDiagnosticAccessor);
      } else if (var1.equals("DebugDiagnosticArchive")) {
         return new Boolean(this._DebugDiagnosticArchive);
      } else if (var1.equals("DebugDiagnosticArchiveRetirement")) {
         return new Boolean(this._DebugDiagnosticArchiveRetirement);
      } else if (var1.equals("DebugDiagnosticCollections")) {
         return new Boolean(this._DebugDiagnosticCollections);
      } else if (var1.equals("DebugDiagnosticContext")) {
         return new Boolean(this._DebugDiagnosticContext);
      } else if (var1.equals("DebugDiagnosticDataGathering")) {
         return new Boolean(this._DebugDiagnosticDataGathering);
      } else if (var1.equals("DebugDiagnosticFileArchive")) {
         return new Boolean(this._DebugDiagnosticFileArchive);
      } else if (var1.equals("DebugDiagnosticImage")) {
         return new Boolean(this._DebugDiagnosticImage);
      } else if (var1.equals("DebugDiagnosticInstrumentation")) {
         return new Boolean(this._DebugDiagnosticInstrumentation);
      } else if (var1.equals("DebugDiagnosticInstrumentationActions")) {
         return new Boolean(this._DebugDiagnosticInstrumentationActions);
      } else if (var1.equals("DebugDiagnosticInstrumentationConfig")) {
         return new Boolean(this._DebugDiagnosticInstrumentationConfig);
      } else if (var1.equals("DebugDiagnosticInstrumentationEvents")) {
         return new Boolean(this._DebugDiagnosticInstrumentationEvents);
      } else if (var1.equals("DebugDiagnosticInstrumentationWeaving")) {
         return new Boolean(this._DebugDiagnosticInstrumentationWeaving);
      } else if (var1.equals("DebugDiagnosticInstrumentationWeavingMatches")) {
         return new Boolean(this._DebugDiagnosticInstrumentationWeavingMatches);
      } else if (var1.equals("DebugDiagnosticJdbcArchive")) {
         return new Boolean(this._DebugDiagnosticJdbcArchive);
      } else if (var1.equals("DebugDiagnosticLifecycleHandlers")) {
         return new Boolean(this._DebugDiagnosticLifecycleHandlers);
      } else if (var1.equals("DebugDiagnosticQuery")) {
         return new Boolean(this._DebugDiagnosticQuery);
      } else if (var1.equals("DebugDiagnosticWatch")) {
         return new Boolean(this._DebugDiagnosticWatch);
      } else if (var1.equals("DebugDiagnosticWlstoreArchive")) {
         return new Boolean(this._DebugDiagnosticWlstoreArchive);
      } else if (var1.equals("DebugDiagnosticsHarvester")) {
         return new Boolean(this._DebugDiagnosticsHarvester);
      } else if (var1.equals("DebugDiagnosticsHarvesterData")) {
         return new Boolean(this._DebugDiagnosticsHarvesterData);
      } else if (var1.equals("DebugDiagnosticsHarvesterMBeanPlugin")) {
         return new Boolean(this._DebugDiagnosticsHarvesterMBeanPlugin);
      } else if (var1.equals("DebugDiagnosticsHarvesterTreeBeanPlugin")) {
         return new Boolean(this._DebugDiagnosticsHarvesterTreeBeanPlugin);
      } else if (var1.equals("DebugDiagnosticsModule")) {
         return new Boolean(this._DebugDiagnosticsModule);
      } else if (var1.equals("DebugDomainLogHandler")) {
         return new Boolean(this._DebugDomainLogHandler);
      } else if (var1.equals("DebugEjbCaching")) {
         return new Boolean(this._DebugEjbCaching);
      } else if (var1.equals("DebugEjbCmpDeployment")) {
         return new Boolean(this._DebugEjbCmpDeployment);
      } else if (var1.equals("DebugEjbCmpRuntime")) {
         return new Boolean(this._DebugEjbCmpRuntime);
      } else if (var1.equals("DebugEjbCompilation")) {
         return new Boolean(this._DebugEjbCompilation);
      } else if (var1.equals("DebugEjbDeployment")) {
         return new Boolean(this._DebugEjbDeployment);
      } else if (var1.equals("DebugEjbInvoke")) {
         return new Boolean(this._DebugEjbInvoke);
      } else if (var1.equals("DebugEjbLocking")) {
         return new Boolean(this._DebugEjbLocking);
      } else if (var1.equals("DebugEjbMdbConnection")) {
         return new Boolean(this._DebugEjbMdbConnection);
      } else if (var1.equals("DebugEjbPooling")) {
         return new Boolean(this._DebugEjbPooling);
      } else if (var1.equals("DebugEjbSecurity")) {
         return new Boolean(this._DebugEjbSecurity);
      } else if (var1.equals("DebugEjbSwapping")) {
         return new Boolean(this._DebugEjbSwapping);
      } else if (var1.equals("DebugEjbTimers")) {
         return new Boolean(this._DebugEjbTimers);
      } else if (var1.equals("DebugEmbeddedLDAP")) {
         return new Boolean(this._DebugEmbeddedLDAP);
      } else if (var1.equals("DebugEmbeddedLDAPLogLevel")) {
         return new Integer(this._DebugEmbeddedLDAPLogLevel);
      } else if (var1.equals("DebugEmbeddedLDAPLogToConsole")) {
         return new Boolean(this._DebugEmbeddedLDAPLogToConsole);
      } else if (var1.equals("DebugEmbeddedLDAPWriteOverrideProps")) {
         return new Boolean(this._DebugEmbeddedLDAPWriteOverrideProps);
      } else if (var1.equals("DebugEventManager")) {
         return new Boolean(this._DebugEventManager);
      } else if (var1.equals("DebugFileDistributionServlet")) {
         return new Boolean(this._DebugFileDistributionServlet);
      } else if (var1.equals("DebugHttp")) {
         return new Boolean(this._DebugHttp);
      } else if (var1.equals("DebugHttpLogging")) {
         return new Boolean(this._DebugHttpLogging);
      } else if (var1.equals("DebugHttpSessions")) {
         return new Boolean(this._DebugHttpSessions);
      } else if (var1.equals("DebugIIOPNaming")) {
         return new Boolean(this._DebugIIOPNaming);
      } else if (var1.equals("DebugIIOPTunneling")) {
         return new Boolean(this._DebugIIOPTunneling);
      } else if (var1.equals("DebugJ2EEManagement")) {
         return new Boolean(this._DebugJ2EEManagement);
      } else if (var1.equals("DebugJAXPDebugLevel")) {
         return new Integer(this._DebugJAXPDebugLevel);
      } else if (var1.equals("DebugJAXPDebugName")) {
         return this._DebugJAXPDebugName;
      } else if (var1.equals("DebugJAXPIncludeClass")) {
         return new Boolean(this._DebugJAXPIncludeClass);
      } else if (var1.equals("DebugJAXPIncludeLocation")) {
         return new Boolean(this._DebugJAXPIncludeLocation);
      } else if (var1.equals("DebugJAXPIncludeName")) {
         return new Boolean(this._DebugJAXPIncludeName);
      } else if (var1.equals("DebugJAXPIncludeTime")) {
         return new Boolean(this._DebugJAXPIncludeTime);
      } else if (var1.equals("DebugJAXPOutputStream")) {
         return this._DebugJAXPOutputStream;
      } else if (var1.equals("DebugJAXPUseShortClass")) {
         return new Boolean(this._DebugJAXPUseShortClass);
      } else if (var1.equals("DebugJDBCConn")) {
         return new Boolean(this._DebugJDBCConn);
      } else if (var1.equals("DebugJDBCDriverLogging")) {
         return new Boolean(this._DebugJDBCDriverLogging);
      } else if (var1.equals("DebugJDBCInternal")) {
         return new Boolean(this._DebugJDBCInternal);
      } else if (var1.equals("DebugJDBCONS")) {
         return new Boolean(this._DebugJDBCONS);
      } else if (var1.equals("DebugJDBCRAC")) {
         return new Boolean(this._DebugJDBCRAC);
      } else if (var1.equals("DebugJDBCREPLAY")) {
         return new Boolean(this._DebugJDBCREPLAY);
      } else if (var1.equals("DebugJDBCRMI")) {
         return new Boolean(this._DebugJDBCRMI);
      } else if (var1.equals("DebugJDBCSQL")) {
         return new Boolean(this._DebugJDBCSQL);
      } else if (var1.equals("DebugJDBCUCP")) {
         return new Boolean(this._DebugJDBCUCP);
      } else if (var1.equals("DebugJMSAME")) {
         return new Boolean(this._DebugJMSAME);
      } else if (var1.equals("DebugJMSBackEnd")) {
         return new Boolean(this._DebugJMSBackEnd);
      } else if (var1.equals("DebugJMSBoot")) {
         return new Boolean(this._DebugJMSBoot);
      } else if (var1.equals("DebugJMSCDS")) {
         return new Boolean(this._DebugJMSCDS);
      } else if (var1.equals("DebugJMSCommon")) {
         return new Boolean(this._DebugJMSCommon);
      } else if (var1.equals("DebugJMSConfig")) {
         return new Boolean(this._DebugJMSConfig);
      } else if (var1.equals("DebugJMSDispatcher")) {
         return new Boolean(this._DebugJMSDispatcher);
      } else if (var1.equals("DebugJMSDistTopic")) {
         return new Boolean(this._DebugJMSDistTopic);
      } else if (var1.equals("DebugJMSDurableSubscribers")) {
         return new Boolean(this._DebugJMSDurableSubscribers);
      } else if (var1.equals("DebugJMSFrontEnd")) {
         return new Boolean(this._DebugJMSFrontEnd);
      } else if (var1.equals("DebugJMSJDBCScavengeOnFlush")) {
         return new Boolean(this._DebugJMSJDBCScavengeOnFlush);
      } else if (var1.equals("DebugJMSLocking")) {
         return new Boolean(this._DebugJMSLocking);
      } else if (var1.equals("DebugJMSMessagePath")) {
         return new Boolean(this._DebugJMSMessagePath);
      } else if (var1.equals("DebugJMSModule")) {
         return new Boolean(this._DebugJMSModule);
      } else if (var1.equals("DebugJMSPauseResume")) {
         return new Boolean(this._DebugJMSPauseResume);
      } else if (var1.equals("DebugJMSSAF")) {
         return new Boolean(this._DebugJMSSAF);
      } else if (var1.equals("DebugJMSStore")) {
         return new Boolean(this._DebugJMSStore);
      } else if (var1.equals("DebugJMST3Server")) {
         return new Boolean(this._DebugJMST3Server);
      } else if (var1.equals("DebugJMSWrappers")) {
         return new Boolean(this._DebugJMSWrappers);
      } else if (var1.equals("DebugJMSXA")) {
         return new Boolean(this._DebugJMSXA);
      } else if (var1.equals("DebugJMX")) {
         return new Boolean(this._DebugJMX);
      } else if (var1.equals("DebugJMXCompatibility")) {
         return new Boolean(this._DebugJMXCompatibility);
      } else if (var1.equals("DebugJMXCore")) {
         return new Boolean(this._DebugJMXCore);
      } else if (var1.equals("DebugJMXDomain")) {
         return new Boolean(this._DebugJMXDomain);
      } else if (var1.equals("DebugJMXEdit")) {
         return new Boolean(this._DebugJMXEdit);
      } else if (var1.equals("DebugJMXRuntime")) {
         return new Boolean(this._DebugJMXRuntime);
      } else if (var1.equals("DebugJNDI")) {
         return new Boolean(this._DebugJNDI);
      } else if (var1.equals("DebugJNDIFactories")) {
         return new Boolean(this._DebugJNDIFactories);
      } else if (var1.equals("DebugJNDIResolution")) {
         return new Boolean(this._DebugJNDIResolution);
      } else if (var1.equals("DebugJTA2PC")) {
         return new Boolean(this._DebugJTA2PC);
      } else if (var1.equals("DebugJTA2PCStackTrace")) {
         return new Boolean(this._DebugJTA2PCStackTrace);
      } else if (var1.equals("DebugJTAAPI")) {
         return new Boolean(this._DebugJTAAPI);
      } else if (var1.equals("DebugJTAGateway")) {
         return new Boolean(this._DebugJTAGateway);
      } else if (var1.equals("DebugJTAGatewayStackTrace")) {
         return new Boolean(this._DebugJTAGatewayStackTrace);
      } else if (var1.equals("DebugJTAHealth")) {
         return new Boolean(this._DebugJTAHealth);
      } else if (var1.equals("DebugJTAJDBC")) {
         return new Boolean(this._DebugJTAJDBC);
      } else if (var1.equals("DebugJTALLR")) {
         return new Boolean(this._DebugJTALLR);
      } else if (var1.equals("DebugJTALifecycle")) {
         return new Boolean(this._DebugJTALifecycle);
      } else if (var1.equals("DebugJTAMigration")) {
         return new Boolean(this._DebugJTAMigration);
      } else if (var1.equals("DebugJTANaming")) {
         return new Boolean(this._DebugJTANaming);
      } else if (var1.equals("DebugJTANamingStackTrace")) {
         return new Boolean(this._DebugJTANamingStackTrace);
      } else if (var1.equals("DebugJTANonXA")) {
         return new Boolean(this._DebugJTANonXA);
      } else if (var1.equals("DebugJTAPropagate")) {
         return new Boolean(this._DebugJTAPropagate);
      } else if (var1.equals("DebugJTARMI")) {
         return new Boolean(this._DebugJTARMI);
      } else if (var1.equals("DebugJTARecovery")) {
         return new Boolean(this._DebugJTARecovery);
      } else if (var1.equals("DebugJTARecoveryStackTrace")) {
         return new Boolean(this._DebugJTARecoveryStackTrace);
      } else if (var1.equals("DebugJTAResourceHealth")) {
         return new Boolean(this._DebugJTAResourceHealth);
      } else if (var1.equals("DebugJTAResourceName")) {
         return this._DebugJTAResourceName;
      } else if (var1.equals("DebugJTATLOG")) {
         return new Boolean(this._DebugJTATLOG);
      } else if (var1.equals("DebugJTATransactionName")) {
         return this._DebugJTATransactionName;
      } else if (var1.equals("DebugJTAXA")) {
         return new Boolean(this._DebugJTAXA);
      } else if (var1.equals("DebugJTAXAStackTrace")) {
         return new Boolean(this._DebugJTAXAStackTrace);
      } else if (var1.equals("DebugJpaDataCache")) {
         return new Boolean(this._DebugJpaDataCache);
      } else if (var1.equals("DebugJpaEnhance")) {
         return new Boolean(this._DebugJpaEnhance);
      } else if (var1.equals("DebugJpaJdbcJdbc")) {
         return new Boolean(this._DebugJpaJdbcJdbc);
      } else if (var1.equals("DebugJpaJdbcSchema")) {
         return new Boolean(this._DebugJpaJdbcSchema);
      } else if (var1.equals("DebugJpaJdbcSql")) {
         return new Boolean(this._DebugJpaJdbcSql);
      } else if (var1.equals("DebugJpaManage")) {
         return new Boolean(this._DebugJpaManage);
      } else if (var1.equals("DebugJpaMetaData")) {
         return new Boolean(this._DebugJpaMetaData);
      } else if (var1.equals("DebugJpaProfile")) {
         return new Boolean(this._DebugJpaProfile);
      } else if (var1.equals("DebugJpaQuery")) {
         return new Boolean(this._DebugJpaQuery);
      } else if (var1.equals("DebugJpaRuntime")) {
         return new Boolean(this._DebugJpaRuntime);
      } else if (var1.equals("DebugJpaTool")) {
         return new Boolean(this._DebugJpaTool);
      } else if (var1.equals("DebugLeaderElection")) {
         return new Boolean(this._DebugLeaderElection);
      } else if (var1.equals("DebugLibraries")) {
         return new Boolean(this._DebugLibraries);
      } else if (var1.equals("DebugLoggingConfiguration")) {
         return new Boolean(this._DebugLoggingConfiguration);
      } else if (var1.equals("DebugManagementServicesResource")) {
         return new Boolean(this._DebugManagementServicesResource);
      } else if (var1.equals("DebugMaskCriterias")) {
         return this._DebugMaskCriterias;
      } else if (var1.equals("DebugMessagingBridgeDumpToConsole")) {
         return new Boolean(this._DebugMessagingBridgeDumpToConsole);
      } else if (var1.equals("DebugMessagingBridgeDumpToLog")) {
         return new Boolean(this._DebugMessagingBridgeDumpToLog);
      } else if (var1.equals("DebugMessagingBridgeRuntime")) {
         return new Boolean(this._DebugMessagingBridgeRuntime);
      } else if (var1.equals("DebugMessagingBridgeRuntimeVerbose")) {
         return new Boolean(this._DebugMessagingBridgeRuntimeVerbose);
      } else if (var1.equals("DebugMessagingBridgeStartup")) {
         return new Boolean(this._DebugMessagingBridgeStartup);
      } else if (var1.equals("DebugMessagingKernel")) {
         return new Boolean(this._DebugMessagingKernel);
      } else if (var1.equals("DebugMessagingKernelBoot")) {
         return new Boolean(this._DebugMessagingKernelBoot);
      } else if (var1.equals("DebugPathSvc")) {
         return new Boolean(this._DebugPathSvc);
      } else if (var1.equals("DebugPathSvcVerbose")) {
         return new Boolean(this._DebugPathSvcVerbose);
      } else if (var1.equals("DebugRA")) {
         return new Boolean(this._DebugRA);
      } else if (var1.equals("DebugRAClassloader")) {
         return new Boolean(this._DebugRAClassloader);
      } else if (var1.equals("DebugRAConnEvents")) {
         return new Boolean(this._DebugRAConnEvents);
      } else if (var1.equals("DebugRAConnections")) {
         return new Boolean(this._DebugRAConnections);
      } else if (var1.equals("DebugRADeployment")) {
         return new Boolean(this._DebugRADeployment);
      } else if (var1.equals("DebugRALifecycle")) {
         return new Boolean(this._DebugRALifecycle);
      } else if (var1.equals("DebugRALocalOut")) {
         return new Boolean(this._DebugRALocalOut);
      } else if (var1.equals("DebugRAParsing")) {
         return new Boolean(this._DebugRAParsing);
      } else if (var1.equals("DebugRAPoolVerbose")) {
         return new Boolean(this._DebugRAPoolVerbose);
      } else if (var1.equals("DebugRAPooling")) {
         return new Boolean(this._DebugRAPooling);
      } else if (var1.equals("DebugRASecurityCtx")) {
         return new Boolean(this._DebugRASecurityCtx);
      } else if (var1.equals("DebugRAWork")) {
         return new Boolean(this._DebugRAWork);
      } else if (var1.equals("DebugRAWorkEvents")) {
         return new Boolean(this._DebugRAWorkEvents);
      } else if (var1.equals("DebugRAXAin")) {
         return new Boolean(this._DebugRAXAin);
      } else if (var1.equals("DebugRAXAout")) {
         return new Boolean(this._DebugRAXAout);
      } else if (var1.equals("DebugRAXAwork")) {
         return new Boolean(this._DebugRAXAwork);
      } else if (var1.equals("DebugReplication")) {
         return new Boolean(this._DebugReplication);
      } else if (var1.equals("DebugReplicationDetails")) {
         return new Boolean(this._DebugReplicationDetails);
      } else if (var1.equals("DebugSAFAdmin")) {
         return new Boolean(this._DebugSAFAdmin);
      } else if (var1.equals("DebugSAFLifeCycle")) {
         return new Boolean(this._DebugSAFLifeCycle);
      } else if (var1.equals("DebugSAFManager")) {
         return new Boolean(this._DebugSAFManager);
      } else if (var1.equals("DebugSAFMessagePath")) {
         return new Boolean(this._DebugSAFMessagePath);
      } else if (var1.equals("DebugSAFReceivingAgent")) {
         return new Boolean(this._DebugSAFReceivingAgent);
      } else if (var1.equals("DebugSAFSendingAgent")) {
         return new Boolean(this._DebugSAFSendingAgent);
      } else if (var1.equals("DebugSAFStore")) {
         return new Boolean(this._DebugSAFStore);
      } else if (var1.equals("DebugSAFTransport")) {
         return new Boolean(this._DebugSAFTransport);
      } else if (var1.equals("DebugSAFVerbose")) {
         return new Boolean(this._DebugSAFVerbose);
      } else if (var1.equals("DebugSNMPAgent")) {
         return new Boolean(this._DebugSNMPAgent);
      } else if (var1.equals("DebugSNMPExtensionProvider")) {
         return new Boolean(this._DebugSNMPExtensionProvider);
      } else if (var1.equals("DebugSNMPProtocolTCP")) {
         return new Boolean(this._DebugSNMPProtocolTCP);
      } else if (var1.equals("DebugSNMPToolkit")) {
         return new Boolean(this._DebugSNMPToolkit);
      } else if (var1.equals("DebugScaContainer")) {
         return new Boolean(this._DebugScaContainer);
      } else if (var1.equals("DebugSecurity")) {
         return new Boolean(this._DebugSecurity);
      } else if (var1.equals("DebugSecurityAdjudicator")) {
         return new Boolean(this._DebugSecurityAdjudicator);
      } else if (var1.equals("DebugSecurityAtn")) {
         return new Boolean(this._DebugSecurityAtn);
      } else if (var1.equals("DebugSecurityAtz")) {
         return new Boolean(this._DebugSecurityAtz);
      } else if (var1.equals("DebugSecurityAuditor")) {
         return new Boolean(this._DebugSecurityAuditor);
      } else if (var1.equals("DebugSecurityCertPath")) {
         return new Boolean(this._DebugSecurityCertPath);
      } else if (var1.equals("DebugSecurityCredMap")) {
         return new Boolean(this._DebugSecurityCredMap);
      } else if (var1.equals("DebugSecurityEEngine")) {
         return new Boolean(this._DebugSecurityEEngine);
      } else if (var1.equals("DebugSecurityEncryptionService")) {
         return new Boolean(this._DebugSecurityEncryptionService);
      } else if (var1.equals("DebugSecurityJACC")) {
         return new Boolean(this._DebugSecurityJACC);
      } else if (var1.equals("DebugSecurityJACCNonPolicy")) {
         return new Boolean(this._DebugSecurityJACCNonPolicy);
      } else if (var1.equals("DebugSecurityJACCPolicy")) {
         return new Boolean(this._DebugSecurityJACCPolicy);
      } else if (var1.equals("DebugSecurityKeyStore")) {
         return new Boolean(this._DebugSecurityKeyStore);
      } else if (var1.equals("DebugSecurityPasswordPolicy")) {
         return new Boolean(this._DebugSecurityPasswordPolicy);
      } else if (var1.equals("DebugSecurityPredicate")) {
         return new Boolean(this._DebugSecurityPredicate);
      } else if (var1.equals("DebugSecurityProfiler")) {
         return new Boolean(this._DebugSecurityProfiler);
      } else if (var1.equals("DebugSecurityRealm")) {
         return new Boolean(this._DebugSecurityRealm);
      } else if (var1.equals("DebugSecurityRoleMap")) {
         return new Boolean(this._DebugSecurityRoleMap);
      } else if (var1.equals("DebugSecuritySAML2Atn")) {
         return new Boolean(this._DebugSecuritySAML2Atn);
      } else if (var1.equals("DebugSecuritySAML2CredMap")) {
         return new Boolean(this._DebugSecuritySAML2CredMap);
      } else if (var1.equals("DebugSecuritySAML2Lib")) {
         return new Boolean(this._DebugSecuritySAML2Lib);
      } else if (var1.equals("DebugSecuritySAML2Service")) {
         return new Boolean(this._DebugSecuritySAML2Service);
      } else if (var1.equals("DebugSecuritySAMLAtn")) {
         return new Boolean(this._DebugSecuritySAMLAtn);
      } else if (var1.equals("DebugSecuritySAMLCredMap")) {
         return new Boolean(this._DebugSecuritySAMLCredMap);
      } else if (var1.equals("DebugSecuritySAMLLib")) {
         return new Boolean(this._DebugSecuritySAMLLib);
      } else if (var1.equals("DebugSecuritySAMLService")) {
         return new Boolean(this._DebugSecuritySAMLService);
      } else if (var1.equals("DebugSecuritySSL")) {
         return new Boolean(this._DebugSecuritySSL);
      } else if (var1.equals("DebugSecuritySSLEaten")) {
         return new Boolean(this._DebugSecuritySSLEaten);
      } else if (var1.equals("DebugSecurityService")) {
         return new Boolean(this._DebugSecurityService);
      } else if (var1.equals("DebugSecurityUserLockout")) {
         return new Boolean(this._DebugSecurityUserLockout);
      } else if (var1.equals("DebugSelfTuning")) {
         return new Boolean(this._DebugSelfTuning);
      } else if (var1.equals("DebugServerLifeCycle")) {
         return new Boolean(this._DebugServerLifeCycle);
      } else if (var1.equals("DebugServerMigration")) {
         return new Boolean(this._DebugServerMigration);
      } else if (var1.equals("DebugServerStartStatistics")) {
         return new Boolean(this._DebugServerStartStatistics);
      } else if (var1.equals("DebugStoreAdmin")) {
         return new Boolean(this._DebugStoreAdmin);
      } else if (var1.equals("DebugStoreIOLogical")) {
         return new Boolean(this._DebugStoreIOLogical);
      } else if (var1.equals("DebugStoreIOLogicalBoot")) {
         return new Boolean(this._DebugStoreIOLogicalBoot);
      } else if (var1.equals("DebugStoreIOPhysical")) {
         return new Boolean(this._DebugStoreIOPhysical);
      } else if (var1.equals("DebugStoreIOPhysicalVerbose")) {
         return new Boolean(this._DebugStoreIOPhysicalVerbose);
      } else if (var1.equals("DebugStoreXA")) {
         return new Boolean(this._DebugStoreXA);
      } else if (var1.equals("DebugStoreXAVerbose")) {
         return new Boolean(this._DebugStoreXAVerbose);
      } else if (var1.equals("DebugTunnelingConnection")) {
         return new Boolean(this._DebugTunnelingConnection);
      } else if (var1.equals("DebugTunnelingConnectionTimeout")) {
         return new Boolean(this._DebugTunnelingConnectionTimeout);
      } else if (var1.equals("DebugURLResolution")) {
         return new Boolean(this._DebugURLResolution);
      } else if (var1.equals("DebugWANReplicationDetails")) {
         return new Boolean(this._DebugWANReplicationDetails);
      } else if (var1.equals("DebugWTCConfig")) {
         return new Boolean(this._DebugWTCConfig);
      } else if (var1.equals("DebugWTCCorbaEx")) {
         return new Boolean(this._DebugWTCCorbaEx);
      } else if (var1.equals("DebugWTCGwtEx")) {
         return new Boolean(this._DebugWTCGwtEx);
      } else if (var1.equals("DebugWTCJatmiEx")) {
         return new Boolean(this._DebugWTCJatmiEx);
      } else if (var1.equals("DebugWTCTDomPdu")) {
         return new Boolean(this._DebugWTCTDomPdu);
      } else if (var1.equals("DebugWTCUData")) {
         return new Boolean(this._DebugWTCUData);
      } else if (var1.equals("DebugWTCtBridgeEx")) {
         return new Boolean(this._DebugWTCtBridgeEx);
      } else if (var1.equals("DebugWebAppIdentityAssertion")) {
         return new Boolean(this._DebugWebAppIdentityAssertion);
      } else if (var1.equals("DebugWebAppModule")) {
         return new Boolean(this._DebugWebAppModule);
      } else if (var1.equals("DebugWebAppSecurity")) {
         return new Boolean(this._DebugWebAppSecurity);
      } else if (var1.equals("DebugXMLEntityCacheDebugLevel")) {
         return new Integer(this._DebugXMLEntityCacheDebugLevel);
      } else if (var1.equals("DebugXMLEntityCacheDebugName")) {
         return this._DebugXMLEntityCacheDebugName;
      } else if (var1.equals("DebugXMLEntityCacheIncludeClass")) {
         return new Boolean(this._DebugXMLEntityCacheIncludeClass);
      } else if (var1.equals("DebugXMLEntityCacheIncludeLocation")) {
         return new Boolean(this._DebugXMLEntityCacheIncludeLocation);
      } else if (var1.equals("DebugXMLEntityCacheIncludeName")) {
         return new Boolean(this._DebugXMLEntityCacheIncludeName);
      } else if (var1.equals("DebugXMLEntityCacheIncludeTime")) {
         return new Boolean(this._DebugXMLEntityCacheIncludeTime);
      } else if (var1.equals("DebugXMLEntityCacheOutputStream")) {
         return this._DebugXMLEntityCacheOutputStream;
      } else if (var1.equals("DebugXMLEntityCacheUseShortClass")) {
         return new Boolean(this._DebugXMLEntityCacheUseShortClass);
      } else if (var1.equals("DebugXMLRegistryDebugLevel")) {
         return new Integer(this._DebugXMLRegistryDebugLevel);
      } else if (var1.equals("DebugXMLRegistryDebugName")) {
         return this._DebugXMLRegistryDebugName;
      } else if (var1.equals("DebugXMLRegistryIncludeClass")) {
         return new Boolean(this._DebugXMLRegistryIncludeClass);
      } else if (var1.equals("DebugXMLRegistryIncludeLocation")) {
         return new Boolean(this._DebugXMLRegistryIncludeLocation);
      } else if (var1.equals("DebugXMLRegistryIncludeName")) {
         return new Boolean(this._DebugXMLRegistryIncludeName);
      } else if (var1.equals("DebugXMLRegistryIncludeTime")) {
         return new Boolean(this._DebugXMLRegistryIncludeTime);
      } else if (var1.equals("DebugXMLRegistryOutputStream")) {
         return this._DebugXMLRegistryOutputStream;
      } else if (var1.equals("DebugXMLRegistryUseShortClass")) {
         return new Boolean(this._DebugXMLRegistryUseShortClass);
      } else if (var1.equals("DefaultStore")) {
         return new Boolean(this._DefaultStore);
      } else if (var1.equals("DiagnosticContextDebugMode")) {
         return this._DiagnosticContextDebugMode;
      } else if (var1.equals("ListenThreadDebug")) {
         return new Boolean(this._ListenThreadDebug);
      } else if (var1.equals("MagicThreadDumpBackToSocket")) {
         return new Boolean(this._MagicThreadDumpBackToSocket);
      } else if (var1.equals("MagicThreadDumpEnabled")) {
         return new Boolean(this._MagicThreadDumpEnabled);
      } else if (var1.equals("MagicThreadDumpFile")) {
         return this._MagicThreadDumpFile;
      } else if (var1.equals("MagicThreadDumpHost")) {
         return this._MagicThreadDumpHost;
      } else if (var1.equals("MasterDeployer")) {
         return new Boolean(this._MasterDeployer);
      } else if (var1.equals("RedefiningClassLoader")) {
         return new Boolean(this._RedefiningClassLoader);
      } else if (var1.equals("Server")) {
         return this._Server;
      } else if (var1.equals("SlaveDeployer")) {
         return new Boolean(this._SlaveDeployer);
      } else {
         return var1.equals("WebModule") ? new Boolean(this._WebModule) : super.getValue(var1);
      }
   }

   public static void validateGeneration() {
      try {
         weblogic.descriptor.beangen.LegalChecks.checkNonNull("MagicThreadDumpHost", "localhost");
      } catch (IllegalArgumentException var1) {
         throw new DescriptorValidateException("The default value for the property  is null. Properties annotated with false value on @legalZeroLength or @legalNull  should either have @required/@derivedDefault annotations or have a non-null value on @default annotation. Refer annotation legalNull on property MagicThreadDumpHost in ServerDebugMBean" + var1.getMessage());
      }
   }

   public static class SchemaHelper2 extends KernelDebugMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 6:
               if (var1.equals("server")) {
                  return 37;
               }
               break;
            case 7:
               if (var1.equals("debugra")) {
                  return 276;
               }
               break;
            case 8:
               if (var1.equals("debugjmx")) {
                  return 293;
               }
            case 9:
            case 36:
            case 41:
            case 42:
            case 43:
            case 45:
            case 46:
            case 47:
            default:
               break;
            case 10:
               if (var1.equals("debug-http")) {
                  return 51;
               }

               if (var1.equals("debug-jndi")) {
                  return 86;
               }

               if (var1.equals("web-module")) {
                  return 239;
               }
               break;
            case 11:
               if (var1.equals("debugjta2pc")) {
                  return 114;
               }

               if (var1.equals("debugjtaapi")) {
                  return 120;
               }

               if (var1.equals("debugjtallr")) {
                  return 129;
               }

               if (var1.equals("debugjtarmi")) {
                  return 113;
               }

               if (var1.equals("debugjta-xa")) {
                  return 110;
               }
               break;
            case 12:
               if (var1.equals("class-finder")) {
                  return 237;
               }

               if (var1.equals("class-loader")) {
                  return 240;
               }

               if (var1.equals("debug-deploy")) {
                  return 227;
               }

               if (var1.equals("debug-jmsame")) {
                  return 103;
               }

               if (var1.equals("debug-jmscds")) {
                  return 109;
               }

               if (var1.equals("debug-jms-xa")) {
                  return 97;
               }

               if (var1.equals("debugjtatlog")) {
                  return 116;
               }

               if (var1.equals("debugra-work")) {
                  return 289;
               }

               if (var1.equals("debugra-xain")) {
                  return 277;
               }
               break;
            case 13:
               if (var1.equals("debug-cluster")) {
                  return 73;
               }

               if (var1.equals("debug-jdbcons")) {
                  return 188;
               }

               if (var1.equals("debug-jdbcrac")) {
                  return 187;
               }

               if (var1.equals("debug-jdbcrmi")) {
                  return 184;
               }

               if (var1.equals("debug-jdbcucp")) {
                  return 189;
               }

               if (var1.equals("debug-jms-saf")) {
                  return 107;
               }

               if (var1.equals("debugjmx-core")) {
                  return 294;
               }

               if (var1.equals("debugjmx-edit")) {
                  return 297;
               }

               if (var1.equals("debugjta-jdbc")) {
                  return 117;
               }

               if (var1.equals("debugra-xaout")) {
                  return 278;
               }

               if (var1.equals("default-store")) {
                  return 49;
               }
               break;
            case 14:
               if (var1.equals("debugdrs-calls")) {
                  return 80;
               }

               if (var1.equals("debug-jdbc-sql")) {
                  return 183;
               }

               if (var1.equals("debug-jms-boot")) {
                  return 100;
               }

               if (var1.equals("debug-jpa-tool")) {
                  return 318;
               }

               if (var1.equals("debug-path-svc")) {
                  return 144;
               }

               if (var1.equals("debugra-xawork")) {
                  return 279;
               }

               if (var1.equals("debug-security")) {
                  return 148;
               }

               if (var1.equals("debug-store-xa")) {
                  return 200;
               }

               if (var1.equals("debugwtcu-data")) {
                  return 308;
               }

               if (var1.equals("slave-deployer")) {
                  return 235;
               }
               break;
            case 15:
               if (var1.equals("debugdrs-queues")) {
                  return 85;
               }

               if (var1.equals("debug-jdbc-conn")) {
                  return 182;
               }

               if (var1.equals("debug-jms-store")) {
                  return 99;
               }

               if (var1.equals("debugjmx-domain")) {
                  return 296;
               }

               if (var1.equals("debugjta-health")) {
                  return 130;
               }

               if (var1.equals("debugjta-naming")) {
                  return 124;
               }

               if (var1.equals("debugjta-non-xa")) {
                  return 111;
               }

               if (var1.equals("debug-jpa-query")) {
                  return 316;
               }

               if (var1.equals("debug-libraries")) {
                  return 45;
               }

               if (var1.equals("debugra-parsing")) {
                  return 284;
               }

               if (var1.equals("debugra-pooling")) {
                  return 286;
               }

               if (var1.equals("debug-saf-admin")) {
                  return 136;
               }

               if (var1.equals("debug-saf-store")) {
                  return 142;
               }

               if (var1.equals("debugsnmp-agent")) {
                  return 269;
               }

               if (var1.equals("debugwtc-config")) {
                  return 306;
               }

               if (var1.equals("debugwtc-gwt-ex")) {
                  return 309;
               }

               if (var1.equals("master-deployer")) {
                  return 234;
               }
               break;
            case 16:
               if (var1.equals("debug-class-size")) {
                  return 48;
               }

               if (var1.equals("debug-deployment")) {
                  return 228;
               }

               if (var1.equals("debug-descriptor")) {
                  return 325;
               }

               if (var1.equals("debug-ejb-invoke")) {
                  return 66;
               }

               if (var1.equals("debug-ejb-timers")) {
                  return 65;
               }

               if (var1.equals("debugiiop-naming")) {
                  return 302;
               }

               if (var1.equals("debug-jdbcreplay")) {
                  return 190;
               }

               if (var1.equals("debug-jms-common")) {
                  return 93;
               }

               if (var1.equals("debug-jms-config")) {
                  return 94;
               }

               if (var1.equals("debug-jms-module")) {
                  return 105;
               }

               if (var1.equals("debugjmx-runtime")) {
                  return 295;
               }

               if (var1.equals("debugjta-gateway")) {
                  return 122;
               }

               if (var1.equals("debug-jpa-manage")) {
                  return 319;
               }
               break;
            case 17:
               if (var1.equals("classpath-servlet")) {
                  return 238;
               }

               if (var1.equals("debug-async-queue")) {
                  return 78;
               }

               if (var1.equals("debug-class-redef")) {
                  return 46;
               }

               if (var1.equals("debugdrs-messages")) {
                  return 82;
               }

               if (var1.equals("debug-ejb-caching")) {
                  return 61;
               }

               if (var1.equals("debug-ejb-locking")) {
                  return 63;
               }

               if (var1.equals("debug-ejb-pooling")) {
                  return 64;
               }

               if (var1.equals("debug-jms-locking")) {
                  return 96;
               }

               if (var1.equals("debugjta-recovery")) {
                  return 118;
               }

               if (var1.equals("debug-jpa-enhance")) {
                  return 314;
               }

               if (var1.equals("debug-jpa-profile")) {
                  return 320;
               }

               if (var1.equals("debug-jpa-runtime")) {
                  return 315;
               }

               if (var1.equals("debugra-lifecycle")) {
                  return 281;
               }

               if (var1.equals("debugra-local-out")) {
                  return 280;
               }

               if (var1.equals("debug-replication")) {
                  return 76;
               }

               if (var1.equals("debug-saf-manager")) {
                  return 137;
               }

               if (var1.equals("debug-saf-verbose")) {
                  return 143;
               }

               if (var1.equals("debugsnmp-toolkit")) {
                  return 268;
               }

               if (var1.equals("debug-securityssl")) {
                  return 153;
               }

               if (var1.equals("debug-self-tuning")) {
                  return 34;
               }

               if (var1.equals("debug-store-admin")) {
                  return 202;
               }

               if (var1.equals("debugwtc-corba-ex")) {
                  return 311;
               }

               if (var1.equals("debugwtc-jatmi-ex")) {
                  return 310;
               }

               if (var1.equals("debugwtct-dom-pdu")) {
                  return 307;
               }
               break;
            case 18:
               if (var1.equals("debug-ejb-security")) {
                  return 67;
               }

               if (var1.equals("debug-ejb-swapping")) {
                  return 62;
               }

               if (var1.equals("debug-embeddedldap")) {
                  return 156;
               }

               if (var1.equals("debug-http-logging")) {
                  return 54;
               }

               if (var1.equals("debug-jms-back-end")) {
                  return 91;
               }

               if (var1.equals("debug-jmst3-server")) {
                  return 324;
               }

               if (var1.equals("debug-jms-wrappers")) {
                  return 108;
               }

               if (var1.equals("debugjta-lifecycle")) {
                  return 128;
               }

               if (var1.equals("debugjta-migration")) {
                  return 127;
               }

               if (var1.equals("debugjta-propagate")) {
                  return 121;
               }

               if (var1.equals("debug-jpa-jdbc-sql")) {
                  return 321;
               }

               if (var1.equals("debugra-deployment")) {
                  return 283;
               }

               if (var1.equals("debug-security-atn")) {
                  return 161;
               }

               if (var1.equals("debug-security-atz")) {
                  return 162;
               }

               if (var1.equals("debug-securityjacc")) {
                  return 171;
               }
               break;
            case 19:
               if (var1.equals("classloader-web-app")) {
                  return 242;
               }

               if (var1.equals("debug-app-container")) {
                  return 44;
               }

               if (var1.equals("debugdrs-heartbeats")) {
                  return 81;
               }

               if (var1.equals("debug-event-manager")) {
                  return 70;
               }

               if (var1.equals("debug-http-sessions")) {
                  return 53;
               }

               if (var1.equals("debugiiop-tunneling")) {
                  return 303;
               }

               if (var1.equals("debug-jdbc-internal")) {
                  return 186;
               }

               if (var1.equals("debug-jms-front-end")) {
                  return 92;
               }

               if (var1.equals("debug-jpa-jdbc-jdbc")) {
                  return 322;
               }

               if (var1.equals("debug-jpa-meta-data")) {
                  return 313;
               }

               if (var1.equals("debug-mask-criteria")) {
                  return 36;
               }

               if (var1.equals("debugra-classloader")) {
                  return 291;
               }

               if (var1.equals("debugra-conn-events")) {
                  return 288;
               }

               if (var1.equals("debugra-connections")) {
                  return 287;
               }

               if (var1.equals("debugra-work-events")) {
                  return 290;
               }

               if (var1.equals("debug-saf-transport")) {
                  return 140;
               }

               if (var1.equals("debug-sca-container")) {
                  return 146;
               }

               if (var1.equals("listen-thread-debug")) {
                  return 38;
               }
               break;
            case 20:
               if (var1.equals("class-loader-verbose")) {
                  return 241;
               }

               if (var1.equals("debug-ejb-deployment")) {
                  return 59;
               }

               if (var1.equals("debugj2ee-management")) {
                  return 301;
               }

               if (var1.equals("debugjaxp-debug-name")) {
                  return 212;
               }

               if (var1.equals("debug-jms-dispatcher")) {
                  return 98;
               }

               if (var1.equals("debug-jms-dist-topic")) {
                  return 95;
               }

               if (var1.equals("debug-jndi-factories")) {
                  return 88;
               }

               if (var1.equals("debug-jpa-data-cache")) {
                  return 317;
               }

               if (var1.equals("debugra-pool-verbose")) {
                  return 275;
               }

               if (var1.equals("debugra-security-ctx")) {
                  return 285;
               }

               if (var1.equals("debug-saf-life-cycle")) {
                  return 135;
               }

               if (var1.equals("debug-security-realm")) {
                  return 147;
               }

               if (var1.equals("debug-url-resolution")) {
                  return 52;
               }

               if (var1.equals("debugwt-ct-bridge-ex")) {
                  return 312;
               }

               if (var1.equals("debug-web-app-module")) {
                  return 57;
               }
               break;
            case 21:
               if (var1.equals("application-container")) {
                  return 236;
               }

               if (var1.equals("class-change-notifier")) {
                  return 50;
               }

               if (var1.equals("debug-ejb-cmp-runtime")) {
                  return 69;
               }

               if (var1.equals("debug-ejb-compilation")) {
                  return 58;
               }

               if (var1.equals("debugjaxp-debug-level")) {
                  return 211;
               }

               if (var1.equals("debug-jndi-resolution")) {
                  return 87;
               }

               if (var1.equals("debug-jpa-jdbc-schema")) {
                  return 323;
               }

               if (var1.equals("debug-leader-election")) {
                  return 79;
               }

               if (var1.equals("debugsnmp-protocoltcp")) {
                  return 270;
               }

               if (var1.equals("debug-storeio-logical")) {
                  return 196;
               }
               break;
            case 22:
               if (var1.equals("debug-cert-revoc-check")) {
                  return 155;
               }

               if (var1.equals("debugdrs-update-status")) {
                  return 83;
               }

               if (var1.equals("debug-diagnostic-image")) {
                  return 263;
               }

               if (var1.equals("debug-diagnostic-query")) {
                  return 264;
               }

               if (var1.equals("debug-diagnostic-watch")) {
                  return 274;
               }

               if (var1.equals("debugjaxp-include-name")) {
                  return 215;
               }

               if (var1.equals("debugjaxp-include-time")) {
                  return 214;
               }

               if (var1.equals("debug-jms-message-path")) {
                  return 106;
               }

               if (var1.equals("debug-jms-pause-resume")) {
                  return 104;
               }

               if (var1.equals("debugjmx-compatibility")) {
                  return 298;
               }

               if (var1.equals("debugjta-resource-name")) {
                  return 132;
               }

               if (var1.equals("debug-messaging-kernel")) {
                  return 133;
               }

               if (var1.equals("debug-path-svc-verbose")) {
                  return 145;
               }

               if (var1.equals("debug-saf-message-path")) {
                  return 141;
               }

               if (var1.equals("debug-security-auditor")) {
                  return 163;
               }

               if (var1.equals("debug-securitye-engine")) {
                  return 170;
               }

               if (var1.equals("debug-securitysaml-atn")) {
                  return 175;
               }

               if (var1.equals("debug-securitysaml-lib")) {
                  return 174;
               }

               if (var1.equals("debug-security-service")) {
                  return 151;
               }

               if (var1.equals("debug-server-migration")) {
                  return 71;
               }

               if (var1.equals("debug-storeio-physical")) {
                  return 198;
               }

               if (var1.equals("debug-store-xa-verbose")) {
                  return 201;
               }

               if (var1.equals("debug-web-app-security")) {
                  return 56;
               }

               if (var1.equals("magic-thread-dump-file")) {
                  return 41;
               }

               if (var1.equals("magic-thread-dump-host")) {
                  return 40;
               }
               break;
            case 23:
               if (var1.equals("debug-bootstrap-servlet")) {
                  return 243;
               }

               if (var1.equals("debug-cluster-fragments")) {
                  return 72;
               }

               if (var1.equals("debug-connector-service")) {
                  return 282;
               }

               if (var1.equals("debug-consensus-leasing")) {
                  return 304;
               }

               if (var1.equals("debugjaxp-include-class")) {
                  return 216;
               }

               if (var1.equals("debugjaxp-output-stream")) {
                  return 213;
               }

               if (var1.equals("debugjta2pc-stack-trace")) {
                  return 115;
               }

               if (var1.equals("debugjta-xa-stack-trace")) {
                  return 112;
               }

               if (var1.equals("debug-saf-sending-agent")) {
                  return 138;
               }

               if (var1.equals("debug-security-cred-map")) {
                  return 164;
               }

               if (var1.equals("debug-security-profiler")) {
                  return 168;
               }

               if (var1.equals("debug-security-role-map")) {
                  return 169;
               }

               if (var1.equals("debug-securitysaml2-atn")) {
                  return 179;
               }

               if (var1.equals("debug-securitysaml2-lib")) {
                  return 178;
               }

               if (var1.equals("debug-securityssl-eaten")) {
                  return 154;
               }

               if (var1.equals("debug-server-life-cycle")) {
                  return 305;
               }

               if (var1.equals("redefining-class-loader")) {
                  return 47;
               }
               break;
            case 24:
               if (var1.equals("debug-cluster-heartbeats")) {
                  return 74;
               }

               if (var1.equals("debug-configuration-edit")) {
                  return 299;
               }

               if (var1.equals("debug-deployment-service")) {
                  return 229;
               }

               if (var1.equals("debug-diagnostic-archive")) {
                  return 253;
               }

               if (var1.equals("debug-diagnostic-context")) {
                  return 267;
               }

               if (var1.equals("debug-diagnostics-module")) {
                  return 258;
               }

               if (var1.equals("debug-domain-log-handler")) {
                  return 272;
               }

               if (var1.equals("debug-ejb-cmp-deployment")) {
                  return 68;
               }

               if (var1.equals("debug-ejb-mdb-connection")) {
                  return 60;
               }

               if (var1.equals("debugjta-resource-health")) {
                  return 126;
               }

               if (var1.equals("debug-security-cert-path")) {
                  return 167;
               }

               if (var1.equals("debug-security-key-store")) {
                  return 166;
               }

               if (var1.equals("debug-security-predicate")) {
                  return 152;
               }
               break;
            case 25:
               if (var1.equals("debug-diagnostic-accessor")) {
                  return 265;
               }

               if (var1.equals("debugjaxp-use-short-class")) {
                  return 218;
               }

               if (var1.equals("debug-jdbc-driver-logging")) {
                  return 185;
               }

               if (var1.equals("debugjta-transaction-name")) {
                  return 131;
               }

               if (var1.equals("debug-replication-details")) {
                  return 77;
               }

               if (var1.equals("debug-saf-receiving-agent")) {
                  return 139;
               }

               if (var1.equals("debug-securityjacc-policy")) {
                  return 173;
               }

               if (var1.equals("magic-thread-dump-enabled")) {
                  return 39;
               }
               break;
            case 26:
               if (var1.equals("debugdrs-state-transitions")) {
                  return 84;
               }

               if (var1.equals("debugjaxp-include-location")) {
                  return 217;
               }

               if (var1.equals("debug-security-adjudicator")) {
                  return 160;
               }

               if (var1.equals("debug-securitysaml-service")) {
                  return 177;
               }

               if (var1.equals("debug-storeio-logical-boot")) {
                  return 197;
               }

               if (var1.equals("debug-tunneling-connection")) {
                  return 90;
               }
               break;
            case 27:
               if (var1.equals("bug-report-service-wsdl-url")) {
                  return 43;
               }

               if (var1.equals("debug-cluster-announcements")) {
                  return 75;
               }

               if (var1.equals("debug-configuration-runtime")) {
                  return 300;
               }

               if (var1.equals("debug-diagnostics-harvester")) {
                  return 259;
               }

               if (var1.equals("debugjta-naming-stack-trace")) {
                  return 125;
               }

               if (var1.equals("debug-logging-configuration")) {
                  return 273;
               }

               if (var1.equals("debug-messaging-kernel-boot")) {
                  return 134;
               }

               if (var1.equals("debug-securitysaml2-service")) {
                  return 181;
               }

               if (var1.equals("debug-securitysaml-cred-map")) {
                  return 176;
               }

               if (var1.equals("debug-security-user-lockout")) {
                  return 150;
               }
               break;
            case 28:
               if (var1.equals("debug-diagnostic-collections")) {
                  return 266;
               }

               if (var1.equals("debug-embeddedldap-log-level")) {
                  return 158;
               }

               if (var1.equals("debugjta-gateway-stack-trace")) {
                  return 123;
               }

               if (var1.equals("debugsnmp-extension-provider")) {
                  return 271;
               }

               if (var1.equals("debug-securitysaml2-cred-map")) {
                  return 180;
               }

               if (var1.equals("debugwan-replication-details")) {
                  return 292;
               }

               if (var1.equals("debugxml-registry-debug-name")) {
                  return 204;
               }
               break;
            case 29:
               if (var1.equals("debug-diagnostic-file-archive")) {
                  return 254;
               }

               if (var1.equals("debug-diagnostic-jdbc-archive")) {
                  return 256;
               }

               if (var1.equals("debug-jms-durable-subscribers")) {
                  return 101;
               }

               if (var1.equals("debugjta-recovery-stack-trace")) {
                  return 119;
               }

               if (var1.equals("debug-securityjacc-non-policy")) {
                  return 172;
               }

               if (var1.equals("debug-server-start-statistics")) {
                  return 326;
               }

               if (var1.equals("debugxml-registry-debug-level")) {
                  return 203;
               }

               if (var1.equals("diagnostic-context-debug-mode")) {
                  return 35;
               }
               break;
            case 30:
               if (var1.equals("debug-messaging-bridge-runtime")) {
                  return 192;
               }

               if (var1.equals("debug-messaging-bridge-startup")) {
                  return 191;
               }

               if (var1.equals("debug-security-password-policy")) {
                  return 149;
               }

               if (var1.equals("debug-storeio-physical-verbose")) {
                  return 199;
               }

               if (var1.equals("debugxml-registry-include-name")) {
                  return 207;
               }

               if (var1.equals("debugxml-registry-include-time")) {
                  return 206;
               }
               break;
            case 31:
               if (var1.equals("debug-diagnostic-data-gathering")) {
                  return 246;
               }

               if (var1.equals("debug-file-distribution-servlet")) {
                  return 244;
               }

               if (var1.equals("debugxml-registry-include-class")) {
                  return 208;
               }

               if (var1.equals("debugxml-registry-output-stream")) {
                  return 205;
               }
               break;
            case 32:
               if (var1.equals("debug-diagnostic-instrumentation")) {
                  return 247;
               }

               if (var1.equals("debug-diagnostic-wlstore-archive")) {
                  return 255;
               }

               if (var1.equals("debug-diagnostics-harvester-data")) {
                  return 260;
               }

               if (var1.equals("debug-jms-jdbc-scavenge-on-flush")) {
                  return 102;
               }

               if (var1.equals("debug-web-app-identity-assertion")) {
                  return 55;
               }

               if (var1.equals("debugxml-entity-cache-debug-name")) {
                  return 220;
               }

               if (var1.equals("magic-thread-dump-back-to-socket")) {
                  return 42;
               }
               break;
            case 33:
               if (var1.equals("debug-deployment-service-internal")) {
                  return 231;
               }

               if (var1.equals("debug-embeddedldap-log-to-console")) {
                  return 157;
               }

               if (var1.equals("debug-security-encryption-service")) {
                  return 165;
               }

               if (var1.equals("debugxml-entity-cache-debug-level")) {
                  return 219;
               }

               if (var1.equals("debugxml-registry-use-short-class")) {
                  return 210;
               }
               break;
            case 34:
               if (var1.equals("debug-deployment-service-transport")) {
                  return 232;
               }

               if (var1.equals("debug-management-services-resource")) {
                  return 327;
               }

               if (var1.equals("debug-messaging-bridge-dump-to-log")) {
                  return 194;
               }

               if (var1.equals("debug-tunneling-connection-timeout")) {
                  return 89;
               }

               if (var1.equals("debugxml-entity-cache-include-name")) {
                  return 223;
               }

               if (var1.equals("debugxml-entity-cache-include-time")) {
                  return 222;
               }

               if (var1.equals("debugxml-registry-include-location")) {
                  return 209;
               }
               break;
            case 35:
               if (var1.equals("debug-diagnostic-archive-retirement")) {
                  return 257;
               }

               if (var1.equals("debug-diagnostic-lifecycle-handlers")) {
                  return 245;
               }

               if (var1.equals("debugxml-entity-cache-include-class")) {
                  return 224;
               }

               if (var1.equals("debugxml-entity-cache-output-stream")) {
                  return 221;
               }
               break;
            case 37:
               if (var1.equals("debugxml-entity-cache-use-short-class")) {
                  return 226;
               }
               break;
            case 38:
               if (var1.equals("debug-messaging-bridge-dump-to-console")) {
                  return 195;
               }

               if (var1.equals("debug-messaging-bridge-runtime-verbose")) {
                  return 193;
               }

               if (var1.equals("debugxml-entity-cache-include-location")) {
                  return 225;
               }
               break;
            case 39:
               if (var1.equals("debug-deployment-service-status-updates")) {
                  return 230;
               }

               if (var1.equals("debug-deployment-service-transport-http")) {
                  return 233;
               }

               if (var1.equals("debug-diagnostic-instrumentation-config")) {
                  return 252;
               }

               if (var1.equals("debug-diagnostic-instrumentation-events")) {
                  return 251;
               }

               if (var1.equals("debug-embeddedldap-write-override-props")) {
                  return 159;
               }
               break;
            case 40:
               if (var1.equals("debug-diagnostic-instrumentation-actions")) {
                  return 250;
               }

               if (var1.equals("debug-diagnostic-instrumentation-weaving")) {
                  return 248;
               }

               if (var1.equals("debug-diagnostics-harvesterm-bean-plugin")) {
                  return 261;
               }
               break;
            case 44:
               if (var1.equals("debug-diagnostics-harvester-tree-bean-plugin")) {
                  return 262;
               }
               break;
            case 48:
               if (var1.equals("debug-diagnostic-instrumentation-weaving-matches")) {
                  return 249;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 7:
               return new DebugScopeMBeanImpl.SchemaHelper2();
            default:
               return super.getSchemaHelper(var1);
         }
      }

      public String getElementName(int var1) {
         switch (var1) {
            case 34:
               return "debug-self-tuning";
            case 35:
               return "diagnostic-context-debug-mode";
            case 36:
               return "debug-mask-criteria";
            case 37:
               return "server";
            case 38:
               return "listen-thread-debug";
            case 39:
               return "magic-thread-dump-enabled";
            case 40:
               return "magic-thread-dump-host";
            case 41:
               return "magic-thread-dump-file";
            case 42:
               return "magic-thread-dump-back-to-socket";
            case 43:
               return "bug-report-service-wsdl-url";
            case 44:
               return "debug-app-container";
            case 45:
               return "debug-libraries";
            case 46:
               return "debug-class-redef";
            case 47:
               return "redefining-class-loader";
            case 48:
               return "debug-class-size";
            case 49:
               return "default-store";
            case 50:
               return "class-change-notifier";
            case 51:
               return "debug-http";
            case 52:
               return "debug-url-resolution";
            case 53:
               return "debug-http-sessions";
            case 54:
               return "debug-http-logging";
            case 55:
               return "debug-web-app-identity-assertion";
            case 56:
               return "debug-web-app-security";
            case 57:
               return "debug-web-app-module";
            case 58:
               return "debug-ejb-compilation";
            case 59:
               return "debug-ejb-deployment";
            case 60:
               return "debug-ejb-mdb-connection";
            case 61:
               return "debug-ejb-caching";
            case 62:
               return "debug-ejb-swapping";
            case 63:
               return "debug-ejb-locking";
            case 64:
               return "debug-ejb-pooling";
            case 65:
               return "debug-ejb-timers";
            case 66:
               return "debug-ejb-invoke";
            case 67:
               return "debug-ejb-security";
            case 68:
               return "debug-ejb-cmp-deployment";
            case 69:
               return "debug-ejb-cmp-runtime";
            case 70:
               return "debug-event-manager";
            case 71:
               return "debug-server-migration";
            case 72:
               return "debug-cluster-fragments";
            case 73:
               return "debug-cluster";
            case 74:
               return "debug-cluster-heartbeats";
            case 75:
               return "debug-cluster-announcements";
            case 76:
               return "debug-replication";
            case 77:
               return "debug-replication-details";
            case 78:
               return "debug-async-queue";
            case 79:
               return "debug-leader-election";
            case 80:
               return "debugdrs-calls";
            case 81:
               return "debugdrs-heartbeats";
            case 82:
               return "debugdrs-messages";
            case 83:
               return "debugdrs-update-status";
            case 84:
               return "debugdrs-state-transitions";
            case 85:
               return "debugdrs-queues";
            case 86:
               return "debug-jndi";
            case 87:
               return "debug-jndi-resolution";
            case 88:
               return "debug-jndi-factories";
            case 89:
               return "debug-tunneling-connection-timeout";
            case 90:
               return "debug-tunneling-connection";
            case 91:
               return "debug-jms-back-end";
            case 92:
               return "debug-jms-front-end";
            case 93:
               return "debug-jms-common";
            case 94:
               return "debug-jms-config";
            case 95:
               return "debug-jms-dist-topic";
            case 96:
               return "debug-jms-locking";
            case 97:
               return "debug-jms-xa";
            case 98:
               return "debug-jms-dispatcher";
            case 99:
               return "debug-jms-store";
            case 100:
               return "debug-jms-boot";
            case 101:
               return "debug-jms-durable-subscribers";
            case 102:
               return "debug-jms-jdbc-scavenge-on-flush";
            case 103:
               return "debug-jmsame";
            case 104:
               return "debug-jms-pause-resume";
            case 105:
               return "debug-jms-module";
            case 106:
               return "debug-jms-message-path";
            case 107:
               return "debug-jms-saf";
            case 108:
               return "debug-jms-wrappers";
            case 109:
               return "debug-jmscds";
            case 110:
               return "debugjta-xa";
            case 111:
               return "debugjta-non-xa";
            case 112:
               return "debugjta-xa-stack-trace";
            case 113:
               return "debugjtarmi";
            case 114:
               return "debugjta2pc";
            case 115:
               return "debugjta2pc-stack-trace";
            case 116:
               return "debugjtatlog";
            case 117:
               return "debugjta-jdbc";
            case 118:
               return "debugjta-recovery";
            case 119:
               return "debugjta-recovery-stack-trace";
            case 120:
               return "debugjtaapi";
            case 121:
               return "debugjta-propagate";
            case 122:
               return "debugjta-gateway";
            case 123:
               return "debugjta-gateway-stack-trace";
            case 124:
               return "debugjta-naming";
            case 125:
               return "debugjta-naming-stack-trace";
            case 126:
               return "debugjta-resource-health";
            case 127:
               return "debugjta-migration";
            case 128:
               return "debugjta-lifecycle";
            case 129:
               return "debugjtallr";
            case 130:
               return "debugjta-health";
            case 131:
               return "debugjta-transaction-name";
            case 132:
               return "debugjta-resource-name";
            case 133:
               return "debug-messaging-kernel";
            case 134:
               return "debug-messaging-kernel-boot";
            case 135:
               return "debug-saf-life-cycle";
            case 136:
               return "debug-saf-admin";
            case 137:
               return "debug-saf-manager";
            case 138:
               return "debug-saf-sending-agent";
            case 139:
               return "debug-saf-receiving-agent";
            case 140:
               return "debug-saf-transport";
            case 141:
               return "debug-saf-message-path";
            case 142:
               return "debug-saf-store";
            case 143:
               return "debug-saf-verbose";
            case 144:
               return "debug-path-svc";
            case 145:
               return "debug-path-svc-verbose";
            case 146:
               return "debug-sca-container";
            case 147:
               return "debug-security-realm";
            case 148:
               return "debug-security";
            case 149:
               return "debug-security-password-policy";
            case 150:
               return "debug-security-user-lockout";
            case 151:
               return "debug-security-service";
            case 152:
               return "debug-security-predicate";
            case 153:
               return "debug-securityssl";
            case 154:
               return "debug-securityssl-eaten";
            case 155:
               return "debug-cert-revoc-check";
            case 156:
               return "debug-embeddedldap";
            case 157:
               return "debug-embeddedldap-log-to-console";
            case 158:
               return "debug-embeddedldap-log-level";
            case 159:
               return "debug-embeddedldap-write-override-props";
            case 160:
               return "debug-security-adjudicator";
            case 161:
               return "debug-security-atn";
            case 162:
               return "debug-security-atz";
            case 163:
               return "debug-security-auditor";
            case 164:
               return "debug-security-cred-map";
            case 165:
               return "debug-security-encryption-service";
            case 166:
               return "debug-security-key-store";
            case 167:
               return "debug-security-cert-path";
            case 168:
               return "debug-security-profiler";
            case 169:
               return "debug-security-role-map";
            case 170:
               return "debug-securitye-engine";
            case 171:
               return "debug-securityjacc";
            case 172:
               return "debug-securityjacc-non-policy";
            case 173:
               return "debug-securityjacc-policy";
            case 174:
               return "debug-securitysaml-lib";
            case 175:
               return "debug-securitysaml-atn";
            case 176:
               return "debug-securitysaml-cred-map";
            case 177:
               return "debug-securitysaml-service";
            case 178:
               return "debug-securitysaml2-lib";
            case 179:
               return "debug-securitysaml2-atn";
            case 180:
               return "debug-securitysaml2-cred-map";
            case 181:
               return "debug-securitysaml2-service";
            case 182:
               return "debug-jdbc-conn";
            case 183:
               return "debug-jdbc-sql";
            case 184:
               return "debug-jdbcrmi";
            case 185:
               return "debug-jdbc-driver-logging";
            case 186:
               return "debug-jdbc-internal";
            case 187:
               return "debug-jdbcrac";
            case 188:
               return "debug-jdbcons";
            case 189:
               return "debug-jdbcucp";
            case 190:
               return "debug-jdbcreplay";
            case 191:
               return "debug-messaging-bridge-startup";
            case 192:
               return "debug-messaging-bridge-runtime";
            case 193:
               return "debug-messaging-bridge-runtime-verbose";
            case 194:
               return "debug-messaging-bridge-dump-to-log";
            case 195:
               return "debug-messaging-bridge-dump-to-console";
            case 196:
               return "debug-storeio-logical";
            case 197:
               return "debug-storeio-logical-boot";
            case 198:
               return "debug-storeio-physical";
            case 199:
               return "debug-storeio-physical-verbose";
            case 200:
               return "debug-store-xa";
            case 201:
               return "debug-store-xa-verbose";
            case 202:
               return "debug-store-admin";
            case 203:
               return "debugxml-registry-debug-level";
            case 204:
               return "debugxml-registry-debug-name";
            case 205:
               return "debugxml-registry-output-stream";
            case 206:
               return "debugxml-registry-include-time";
            case 207:
               return "debugxml-registry-include-name";
            case 208:
               return "debugxml-registry-include-class";
            case 209:
               return "debugxml-registry-include-location";
            case 210:
               return "debugxml-registry-use-short-class";
            case 211:
               return "debugjaxp-debug-level";
            case 212:
               return "debugjaxp-debug-name";
            case 213:
               return "debugjaxp-output-stream";
            case 214:
               return "debugjaxp-include-time";
            case 215:
               return "debugjaxp-include-name";
            case 216:
               return "debugjaxp-include-class";
            case 217:
               return "debugjaxp-include-location";
            case 218:
               return "debugjaxp-use-short-class";
            case 219:
               return "debugxml-entity-cache-debug-level";
            case 220:
               return "debugxml-entity-cache-debug-name";
            case 221:
               return "debugxml-entity-cache-output-stream";
            case 222:
               return "debugxml-entity-cache-include-time";
            case 223:
               return "debugxml-entity-cache-include-name";
            case 224:
               return "debugxml-entity-cache-include-class";
            case 225:
               return "debugxml-entity-cache-include-location";
            case 226:
               return "debugxml-entity-cache-use-short-class";
            case 227:
               return "debug-deploy";
            case 228:
               return "debug-deployment";
            case 229:
               return "debug-deployment-service";
            case 230:
               return "debug-deployment-service-status-updates";
            case 231:
               return "debug-deployment-service-internal";
            case 232:
               return "debug-deployment-service-transport";
            case 233:
               return "debug-deployment-service-transport-http";
            case 234:
               return "master-deployer";
            case 235:
               return "slave-deployer";
            case 236:
               return "application-container";
            case 237:
               return "class-finder";
            case 238:
               return "classpath-servlet";
            case 239:
               return "web-module";
            case 240:
               return "class-loader";
            case 241:
               return "class-loader-verbose";
            case 242:
               return "classloader-web-app";
            case 243:
               return "debug-bootstrap-servlet";
            case 244:
               return "debug-file-distribution-servlet";
            case 245:
               return "debug-diagnostic-lifecycle-handlers";
            case 246:
               return "debug-diagnostic-data-gathering";
            case 247:
               return "debug-diagnostic-instrumentation";
            case 248:
               return "debug-diagnostic-instrumentation-weaving";
            case 249:
               return "debug-diagnostic-instrumentation-weaving-matches";
            case 250:
               return "debug-diagnostic-instrumentation-actions";
            case 251:
               return "debug-diagnostic-instrumentation-events";
            case 252:
               return "debug-diagnostic-instrumentation-config";
            case 253:
               return "debug-diagnostic-archive";
            case 254:
               return "debug-diagnostic-file-archive";
            case 255:
               return "debug-diagnostic-wlstore-archive";
            case 256:
               return "debug-diagnostic-jdbc-archive";
            case 257:
               return "debug-diagnostic-archive-retirement";
            case 258:
               return "debug-diagnostics-module";
            case 259:
               return "debug-diagnostics-harvester";
            case 260:
               return "debug-diagnostics-harvester-data";
            case 261:
               return "debug-diagnostics-harvesterm-bean-plugin";
            case 262:
               return "debug-diagnostics-harvester-tree-bean-plugin";
            case 263:
               return "debug-diagnostic-image";
            case 264:
               return "debug-diagnostic-query";
            case 265:
               return "debug-diagnostic-accessor";
            case 266:
               return "debug-diagnostic-collections";
            case 267:
               return "debug-diagnostic-context";
            case 268:
               return "debugsnmp-toolkit";
            case 269:
               return "debugsnmp-agent";
            case 270:
               return "debugsnmp-protocoltcp";
            case 271:
               return "debugsnmp-extension-provider";
            case 272:
               return "debug-domain-log-handler";
            case 273:
               return "debug-logging-configuration";
            case 274:
               return "debug-diagnostic-watch";
            case 275:
               return "debugra-pool-verbose";
            case 276:
               return "debugra";
            case 277:
               return "debugra-xain";
            case 278:
               return "debugra-xaout";
            case 279:
               return "debugra-xawork";
            case 280:
               return "debugra-local-out";
            case 281:
               return "debugra-lifecycle";
            case 282:
               return "debug-connector-service";
            case 283:
               return "debugra-deployment";
            case 284:
               return "debugra-parsing";
            case 285:
               return "debugra-security-ctx";
            case 286:
               return "debugra-pooling";
            case 287:
               return "debugra-connections";
            case 288:
               return "debugra-conn-events";
            case 289:
               return "debugra-work";
            case 290:
               return "debugra-work-events";
            case 291:
               return "debugra-classloader";
            case 292:
               return "debugwan-replication-details";
            case 293:
               return "debugjmx";
            case 294:
               return "debugjmx-core";
            case 295:
               return "debugjmx-runtime";
            case 296:
               return "debugjmx-domain";
            case 297:
               return "debugjmx-edit";
            case 298:
               return "debugjmx-compatibility";
            case 299:
               return "debug-configuration-edit";
            case 300:
               return "debug-configuration-runtime";
            case 301:
               return "debugj2ee-management";
            case 302:
               return "debugiiop-naming";
            case 303:
               return "debugiiop-tunneling";
            case 304:
               return "debug-consensus-leasing";
            case 305:
               return "debug-server-life-cycle";
            case 306:
               return "debugwtc-config";
            case 307:
               return "debugwtct-dom-pdu";
            case 308:
               return "debugwtcu-data";
            case 309:
               return "debugwtc-gwt-ex";
            case 310:
               return "debugwtc-jatmi-ex";
            case 311:
               return "debugwtc-corba-ex";
            case 312:
               return "debugwt-ct-bridge-ex";
            case 313:
               return "debug-jpa-meta-data";
            case 314:
               return "debug-jpa-enhance";
            case 315:
               return "debug-jpa-runtime";
            case 316:
               return "debug-jpa-query";
            case 317:
               return "debug-jpa-data-cache";
            case 318:
               return "debug-jpa-tool";
            case 319:
               return "debug-jpa-manage";
            case 320:
               return "debug-jpa-profile";
            case 321:
               return "debug-jpa-jdbc-sql";
            case 322:
               return "debug-jpa-jdbc-jdbc";
            case 323:
               return "debug-jpa-jdbc-schema";
            case 324:
               return "debug-jmst3-server";
            case 325:
               return "debug-descriptor";
            case 326:
               return "debug-server-start-statistics";
            case 327:
               return "debug-management-services-resource";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 7:
               return true;
            case 36:
               return true;
            default:
               return super.isArray(var1);
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            case 7:
               return true;
            default:
               return super.isBean(var1);
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

      public String[] getKeyElementNames() {
         ArrayList var1 = new ArrayList();
         var1.add("name");
         return (String[])((String[])var1.toArray(new String[0]));
      }
   }

   protected static class Helper extends KernelDebugMBeanImpl.Helper {
      private ServerDebugMBeanImpl bean;

      protected Helper(ServerDebugMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 34:
               return "DebugSelfTuning";
            case 35:
               return "DiagnosticContextDebugMode";
            case 36:
               return "DebugMaskCriterias";
            case 37:
               return "Server";
            case 38:
               return "ListenThreadDebug";
            case 39:
               return "MagicThreadDumpEnabled";
            case 40:
               return "MagicThreadDumpHost";
            case 41:
               return "MagicThreadDumpFile";
            case 42:
               return "MagicThreadDumpBackToSocket";
            case 43:
               return "BugReportServiceWsdlUrl";
            case 44:
               return "DebugAppContainer";
            case 45:
               return "DebugLibraries";
            case 46:
               return "DebugClassRedef";
            case 47:
               return "RedefiningClassLoader";
            case 48:
               return "DebugClassSize";
            case 49:
               return "DefaultStore";
            case 50:
               return "ClassChangeNotifier";
            case 51:
               return "DebugHttp";
            case 52:
               return "DebugURLResolution";
            case 53:
               return "DebugHttpSessions";
            case 54:
               return "DebugHttpLogging";
            case 55:
               return "DebugWebAppIdentityAssertion";
            case 56:
               return "DebugWebAppSecurity";
            case 57:
               return "DebugWebAppModule";
            case 58:
               return "DebugEjbCompilation";
            case 59:
               return "DebugEjbDeployment";
            case 60:
               return "DebugEjbMdbConnection";
            case 61:
               return "DebugEjbCaching";
            case 62:
               return "DebugEjbSwapping";
            case 63:
               return "DebugEjbLocking";
            case 64:
               return "DebugEjbPooling";
            case 65:
               return "DebugEjbTimers";
            case 66:
               return "DebugEjbInvoke";
            case 67:
               return "DebugEjbSecurity";
            case 68:
               return "DebugEjbCmpDeployment";
            case 69:
               return "DebugEjbCmpRuntime";
            case 70:
               return "DebugEventManager";
            case 71:
               return "DebugServerMigration";
            case 72:
               return "DebugClusterFragments";
            case 73:
               return "DebugCluster";
            case 74:
               return "DebugClusterHeartbeats";
            case 75:
               return "DebugClusterAnnouncements";
            case 76:
               return "DebugReplication";
            case 77:
               return "DebugReplicationDetails";
            case 78:
               return "DebugAsyncQueue";
            case 79:
               return "DebugLeaderElection";
            case 80:
               return "DebugDRSCalls";
            case 81:
               return "DebugDRSHeartbeats";
            case 82:
               return "DebugDRSMessages";
            case 83:
               return "DebugDRSUpdateStatus";
            case 84:
               return "DebugDRSStateTransitions";
            case 85:
               return "DebugDRSQueues";
            case 86:
               return "DebugJNDI";
            case 87:
               return "DebugJNDIResolution";
            case 88:
               return "DebugJNDIFactories";
            case 89:
               return "DebugTunnelingConnectionTimeout";
            case 90:
               return "DebugTunnelingConnection";
            case 91:
               return "DebugJMSBackEnd";
            case 92:
               return "DebugJMSFrontEnd";
            case 93:
               return "DebugJMSCommon";
            case 94:
               return "DebugJMSConfig";
            case 95:
               return "DebugJMSDistTopic";
            case 96:
               return "DebugJMSLocking";
            case 97:
               return "DebugJMSXA";
            case 98:
               return "DebugJMSDispatcher";
            case 99:
               return "DebugJMSStore";
            case 100:
               return "DebugJMSBoot";
            case 101:
               return "DebugJMSDurableSubscribers";
            case 102:
               return "DebugJMSJDBCScavengeOnFlush";
            case 103:
               return "DebugJMSAME";
            case 104:
               return "DebugJMSPauseResume";
            case 105:
               return "DebugJMSModule";
            case 106:
               return "DebugJMSMessagePath";
            case 107:
               return "DebugJMSSAF";
            case 108:
               return "DebugJMSWrappers";
            case 109:
               return "DebugJMSCDS";
            case 110:
               return "DebugJTAXA";
            case 111:
               return "DebugJTANonXA";
            case 112:
               return "DebugJTAXAStackTrace";
            case 113:
               return "DebugJTARMI";
            case 114:
               return "DebugJTA2PC";
            case 115:
               return "DebugJTA2PCStackTrace";
            case 116:
               return "DebugJTATLOG";
            case 117:
               return "DebugJTAJDBC";
            case 118:
               return "DebugJTARecovery";
            case 119:
               return "DebugJTARecoveryStackTrace";
            case 120:
               return "DebugJTAAPI";
            case 121:
               return "DebugJTAPropagate";
            case 122:
               return "DebugJTAGateway";
            case 123:
               return "DebugJTAGatewayStackTrace";
            case 124:
               return "DebugJTANaming";
            case 125:
               return "DebugJTANamingStackTrace";
            case 126:
               return "DebugJTAResourceHealth";
            case 127:
               return "DebugJTAMigration";
            case 128:
               return "DebugJTALifecycle";
            case 129:
               return "DebugJTALLR";
            case 130:
               return "DebugJTAHealth";
            case 131:
               return "DebugJTATransactionName";
            case 132:
               return "DebugJTAResourceName";
            case 133:
               return "DebugMessagingKernel";
            case 134:
               return "DebugMessagingKernelBoot";
            case 135:
               return "DebugSAFLifeCycle";
            case 136:
               return "DebugSAFAdmin";
            case 137:
               return "DebugSAFManager";
            case 138:
               return "DebugSAFSendingAgent";
            case 139:
               return "DebugSAFReceivingAgent";
            case 140:
               return "DebugSAFTransport";
            case 141:
               return "DebugSAFMessagePath";
            case 142:
               return "DebugSAFStore";
            case 143:
               return "DebugSAFVerbose";
            case 144:
               return "DebugPathSvc";
            case 145:
               return "DebugPathSvcVerbose";
            case 146:
               return "DebugScaContainer";
            case 147:
               return "DebugSecurityRealm";
            case 148:
               return "DebugSecurity";
            case 149:
               return "DebugSecurityPasswordPolicy";
            case 150:
               return "DebugSecurityUserLockout";
            case 151:
               return "DebugSecurityService";
            case 152:
               return "DebugSecurityPredicate";
            case 153:
               return "DebugSecuritySSL";
            case 154:
               return "DebugSecuritySSLEaten";
            case 155:
               return "DebugCertRevocCheck";
            case 156:
               return "DebugEmbeddedLDAP";
            case 157:
               return "DebugEmbeddedLDAPLogToConsole";
            case 158:
               return "DebugEmbeddedLDAPLogLevel";
            case 159:
               return "DebugEmbeddedLDAPWriteOverrideProps";
            case 160:
               return "DebugSecurityAdjudicator";
            case 161:
               return "DebugSecurityAtn";
            case 162:
               return "DebugSecurityAtz";
            case 163:
               return "DebugSecurityAuditor";
            case 164:
               return "DebugSecurityCredMap";
            case 165:
               return "DebugSecurityEncryptionService";
            case 166:
               return "DebugSecurityKeyStore";
            case 167:
               return "DebugSecurityCertPath";
            case 168:
               return "DebugSecurityProfiler";
            case 169:
               return "DebugSecurityRoleMap";
            case 170:
               return "DebugSecurityEEngine";
            case 171:
               return "DebugSecurityJACC";
            case 172:
               return "DebugSecurityJACCNonPolicy";
            case 173:
               return "DebugSecurityJACCPolicy";
            case 174:
               return "DebugSecuritySAMLLib";
            case 175:
               return "DebugSecuritySAMLAtn";
            case 176:
               return "DebugSecuritySAMLCredMap";
            case 177:
               return "DebugSecuritySAMLService";
            case 178:
               return "DebugSecuritySAML2Lib";
            case 179:
               return "DebugSecuritySAML2Atn";
            case 180:
               return "DebugSecuritySAML2CredMap";
            case 181:
               return "DebugSecuritySAML2Service";
            case 182:
               return "DebugJDBCConn";
            case 183:
               return "DebugJDBCSQL";
            case 184:
               return "DebugJDBCRMI";
            case 185:
               return "DebugJDBCDriverLogging";
            case 186:
               return "DebugJDBCInternal";
            case 187:
               return "DebugJDBCRAC";
            case 188:
               return "DebugJDBCONS";
            case 189:
               return "DebugJDBCUCP";
            case 190:
               return "DebugJDBCREPLAY";
            case 191:
               return "DebugMessagingBridgeStartup";
            case 192:
               return "DebugMessagingBridgeRuntime";
            case 193:
               return "DebugMessagingBridgeRuntimeVerbose";
            case 194:
               return "DebugMessagingBridgeDumpToLog";
            case 195:
               return "DebugMessagingBridgeDumpToConsole";
            case 196:
               return "DebugStoreIOLogical";
            case 197:
               return "DebugStoreIOLogicalBoot";
            case 198:
               return "DebugStoreIOPhysical";
            case 199:
               return "DebugStoreIOPhysicalVerbose";
            case 200:
               return "DebugStoreXA";
            case 201:
               return "DebugStoreXAVerbose";
            case 202:
               return "DebugStoreAdmin";
            case 203:
               return "DebugXMLRegistryDebugLevel";
            case 204:
               return "DebugXMLRegistryDebugName";
            case 205:
               return "DebugXMLRegistryOutputStream";
            case 206:
               return "DebugXMLRegistryIncludeTime";
            case 207:
               return "DebugXMLRegistryIncludeName";
            case 208:
               return "DebugXMLRegistryIncludeClass";
            case 209:
               return "DebugXMLRegistryIncludeLocation";
            case 210:
               return "DebugXMLRegistryUseShortClass";
            case 211:
               return "DebugJAXPDebugLevel";
            case 212:
               return "DebugJAXPDebugName";
            case 213:
               return "DebugJAXPOutputStream";
            case 214:
               return "DebugJAXPIncludeTime";
            case 215:
               return "DebugJAXPIncludeName";
            case 216:
               return "DebugJAXPIncludeClass";
            case 217:
               return "DebugJAXPIncludeLocation";
            case 218:
               return "DebugJAXPUseShortClass";
            case 219:
               return "DebugXMLEntityCacheDebugLevel";
            case 220:
               return "DebugXMLEntityCacheDebugName";
            case 221:
               return "DebugXMLEntityCacheOutputStream";
            case 222:
               return "DebugXMLEntityCacheIncludeTime";
            case 223:
               return "DebugXMLEntityCacheIncludeName";
            case 224:
               return "DebugXMLEntityCacheIncludeClass";
            case 225:
               return "DebugXMLEntityCacheIncludeLocation";
            case 226:
               return "DebugXMLEntityCacheUseShortClass";
            case 227:
               return "DebugDeploy";
            case 228:
               return "DebugDeployment";
            case 229:
               return "DebugDeploymentService";
            case 230:
               return "DebugDeploymentServiceStatusUpdates";
            case 231:
               return "DebugDeploymentServiceInternal";
            case 232:
               return "DebugDeploymentServiceTransport";
            case 233:
               return "DebugDeploymentServiceTransportHttp";
            case 234:
               return "MasterDeployer";
            case 235:
               return "SlaveDeployer";
            case 236:
               return "ApplicationContainer";
            case 237:
               return "ClassFinder";
            case 238:
               return "ClasspathServlet";
            case 239:
               return "WebModule";
            case 240:
               return "ClassLoader";
            case 241:
               return "ClassLoaderVerbose";
            case 242:
               return "ClassloaderWebApp";
            case 243:
               return "DebugBootstrapServlet";
            case 244:
               return "DebugFileDistributionServlet";
            case 245:
               return "DebugDiagnosticLifecycleHandlers";
            case 246:
               return "DebugDiagnosticDataGathering";
            case 247:
               return "DebugDiagnosticInstrumentation";
            case 248:
               return "DebugDiagnosticInstrumentationWeaving";
            case 249:
               return "DebugDiagnosticInstrumentationWeavingMatches";
            case 250:
               return "DebugDiagnosticInstrumentationActions";
            case 251:
               return "DebugDiagnosticInstrumentationEvents";
            case 252:
               return "DebugDiagnosticInstrumentationConfig";
            case 253:
               return "DebugDiagnosticArchive";
            case 254:
               return "DebugDiagnosticFileArchive";
            case 255:
               return "DebugDiagnosticWlstoreArchive";
            case 256:
               return "DebugDiagnosticJdbcArchive";
            case 257:
               return "DebugDiagnosticArchiveRetirement";
            case 258:
               return "DebugDiagnosticsModule";
            case 259:
               return "DebugDiagnosticsHarvester";
            case 260:
               return "DebugDiagnosticsHarvesterData";
            case 261:
               return "DebugDiagnosticsHarvesterMBeanPlugin";
            case 262:
               return "DebugDiagnosticsHarvesterTreeBeanPlugin";
            case 263:
               return "DebugDiagnosticImage";
            case 264:
               return "DebugDiagnosticQuery";
            case 265:
               return "DebugDiagnosticAccessor";
            case 266:
               return "DebugDiagnosticCollections";
            case 267:
               return "DebugDiagnosticContext";
            case 268:
               return "DebugSNMPToolkit";
            case 269:
               return "DebugSNMPAgent";
            case 270:
               return "DebugSNMPProtocolTCP";
            case 271:
               return "DebugSNMPExtensionProvider";
            case 272:
               return "DebugDomainLogHandler";
            case 273:
               return "DebugLoggingConfiguration";
            case 274:
               return "DebugDiagnosticWatch";
            case 275:
               return "DebugRAPoolVerbose";
            case 276:
               return "DebugRA";
            case 277:
               return "DebugRAXAin";
            case 278:
               return "DebugRAXAout";
            case 279:
               return "DebugRAXAwork";
            case 280:
               return "DebugRALocalOut";
            case 281:
               return "DebugRALifecycle";
            case 282:
               return "DebugConnectorService";
            case 283:
               return "DebugRADeployment";
            case 284:
               return "DebugRAParsing";
            case 285:
               return "DebugRASecurityCtx";
            case 286:
               return "DebugRAPooling";
            case 287:
               return "DebugRAConnections";
            case 288:
               return "DebugRAConnEvents";
            case 289:
               return "DebugRAWork";
            case 290:
               return "DebugRAWorkEvents";
            case 291:
               return "DebugRAClassloader";
            case 292:
               return "DebugWANReplicationDetails";
            case 293:
               return "DebugJMX";
            case 294:
               return "DebugJMXCore";
            case 295:
               return "DebugJMXRuntime";
            case 296:
               return "DebugJMXDomain";
            case 297:
               return "DebugJMXEdit";
            case 298:
               return "DebugJMXCompatibility";
            case 299:
               return "DebugConfigurationEdit";
            case 300:
               return "DebugConfigurationRuntime";
            case 301:
               return "DebugJ2EEManagement";
            case 302:
               return "DebugIIOPNaming";
            case 303:
               return "DebugIIOPTunneling";
            case 304:
               return "DebugConsensusLeasing";
            case 305:
               return "DebugServerLifeCycle";
            case 306:
               return "DebugWTCConfig";
            case 307:
               return "DebugWTCTDomPdu";
            case 308:
               return "DebugWTCUData";
            case 309:
               return "DebugWTCGwtEx";
            case 310:
               return "DebugWTCJatmiEx";
            case 311:
               return "DebugWTCCorbaEx";
            case 312:
               return "DebugWTCtBridgeEx";
            case 313:
               return "DebugJpaMetaData";
            case 314:
               return "DebugJpaEnhance";
            case 315:
               return "DebugJpaRuntime";
            case 316:
               return "DebugJpaQuery";
            case 317:
               return "DebugJpaDataCache";
            case 318:
               return "DebugJpaTool";
            case 319:
               return "DebugJpaManage";
            case 320:
               return "DebugJpaProfile";
            case 321:
               return "DebugJpaJdbcSql";
            case 322:
               return "DebugJpaJdbcJdbc";
            case 323:
               return "DebugJpaJdbcSchema";
            case 324:
               return "DebugJMST3Server";
            case 325:
               return "DebugDescriptor";
            case 326:
               return "DebugServerStartStatistics";
            case 327:
               return "DebugManagementServicesResource";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("ApplicationContainer")) {
            return 236;
         } else if (var1.equals("BugReportServiceWsdlUrl")) {
            return 43;
         } else if (var1.equals("ClassChangeNotifier")) {
            return 50;
         } else if (var1.equals("ClassFinder")) {
            return 237;
         } else if (var1.equals("ClassLoader")) {
            return 240;
         } else if (var1.equals("ClassLoaderVerbose")) {
            return 241;
         } else if (var1.equals("ClassloaderWebApp")) {
            return 242;
         } else if (var1.equals("ClasspathServlet")) {
            return 238;
         } else if (var1.equals("DebugAppContainer")) {
            return 44;
         } else if (var1.equals("DebugAsyncQueue")) {
            return 78;
         } else if (var1.equals("DebugBootstrapServlet")) {
            return 243;
         } else if (var1.equals("DebugCertRevocCheck")) {
            return 155;
         } else if (var1.equals("DebugClassRedef")) {
            return 46;
         } else if (var1.equals("DebugClassSize")) {
            return 48;
         } else if (var1.equals("DebugCluster")) {
            return 73;
         } else if (var1.equals("DebugClusterAnnouncements")) {
            return 75;
         } else if (var1.equals("DebugClusterFragments")) {
            return 72;
         } else if (var1.equals("DebugClusterHeartbeats")) {
            return 74;
         } else if (var1.equals("DebugConfigurationEdit")) {
            return 299;
         } else if (var1.equals("DebugConfigurationRuntime")) {
            return 300;
         } else if (var1.equals("DebugConnectorService")) {
            return 282;
         } else if (var1.equals("DebugConsensusLeasing")) {
            return 304;
         } else if (var1.equals("DebugDRSCalls")) {
            return 80;
         } else if (var1.equals("DebugDRSHeartbeats")) {
            return 81;
         } else if (var1.equals("DebugDRSMessages")) {
            return 82;
         } else if (var1.equals("DebugDRSQueues")) {
            return 85;
         } else if (var1.equals("DebugDRSStateTransitions")) {
            return 84;
         } else if (var1.equals("DebugDRSUpdateStatus")) {
            return 83;
         } else if (var1.equals("DebugDeploy")) {
            return 227;
         } else if (var1.equals("DebugDeployment")) {
            return 228;
         } else if (var1.equals("DebugDeploymentService")) {
            return 229;
         } else if (var1.equals("DebugDeploymentServiceInternal")) {
            return 231;
         } else if (var1.equals("DebugDeploymentServiceStatusUpdates")) {
            return 230;
         } else if (var1.equals("DebugDeploymentServiceTransport")) {
            return 232;
         } else if (var1.equals("DebugDeploymentServiceTransportHttp")) {
            return 233;
         } else if (var1.equals("DebugDescriptor")) {
            return 325;
         } else if (var1.equals("DebugDiagnosticAccessor")) {
            return 265;
         } else if (var1.equals("DebugDiagnosticArchive")) {
            return 253;
         } else if (var1.equals("DebugDiagnosticArchiveRetirement")) {
            return 257;
         } else if (var1.equals("DebugDiagnosticCollections")) {
            return 266;
         } else if (var1.equals("DebugDiagnosticContext")) {
            return 267;
         } else if (var1.equals("DebugDiagnosticDataGathering")) {
            return 246;
         } else if (var1.equals("DebugDiagnosticFileArchive")) {
            return 254;
         } else if (var1.equals("DebugDiagnosticImage")) {
            return 263;
         } else if (var1.equals("DebugDiagnosticInstrumentation")) {
            return 247;
         } else if (var1.equals("DebugDiagnosticInstrumentationActions")) {
            return 250;
         } else if (var1.equals("DebugDiagnosticInstrumentationConfig")) {
            return 252;
         } else if (var1.equals("DebugDiagnosticInstrumentationEvents")) {
            return 251;
         } else if (var1.equals("DebugDiagnosticInstrumentationWeaving")) {
            return 248;
         } else if (var1.equals("DebugDiagnosticInstrumentationWeavingMatches")) {
            return 249;
         } else if (var1.equals("DebugDiagnosticJdbcArchive")) {
            return 256;
         } else if (var1.equals("DebugDiagnosticLifecycleHandlers")) {
            return 245;
         } else if (var1.equals("DebugDiagnosticQuery")) {
            return 264;
         } else if (var1.equals("DebugDiagnosticWatch")) {
            return 274;
         } else if (var1.equals("DebugDiagnosticWlstoreArchive")) {
            return 255;
         } else if (var1.equals("DebugDiagnosticsHarvester")) {
            return 259;
         } else if (var1.equals("DebugDiagnosticsHarvesterData")) {
            return 260;
         } else if (var1.equals("DebugDiagnosticsHarvesterMBeanPlugin")) {
            return 261;
         } else if (var1.equals("DebugDiagnosticsHarvesterTreeBeanPlugin")) {
            return 262;
         } else if (var1.equals("DebugDiagnosticsModule")) {
            return 258;
         } else if (var1.equals("DebugDomainLogHandler")) {
            return 272;
         } else if (var1.equals("DebugEjbCaching")) {
            return 61;
         } else if (var1.equals("DebugEjbCmpDeployment")) {
            return 68;
         } else if (var1.equals("DebugEjbCmpRuntime")) {
            return 69;
         } else if (var1.equals("DebugEjbCompilation")) {
            return 58;
         } else if (var1.equals("DebugEjbDeployment")) {
            return 59;
         } else if (var1.equals("DebugEjbInvoke")) {
            return 66;
         } else if (var1.equals("DebugEjbLocking")) {
            return 63;
         } else if (var1.equals("DebugEjbMdbConnection")) {
            return 60;
         } else if (var1.equals("DebugEjbPooling")) {
            return 64;
         } else if (var1.equals("DebugEjbSecurity")) {
            return 67;
         } else if (var1.equals("DebugEjbSwapping")) {
            return 62;
         } else if (var1.equals("DebugEjbTimers")) {
            return 65;
         } else if (var1.equals("DebugEmbeddedLDAP")) {
            return 156;
         } else if (var1.equals("DebugEmbeddedLDAPLogLevel")) {
            return 158;
         } else if (var1.equals("DebugEmbeddedLDAPLogToConsole")) {
            return 157;
         } else if (var1.equals("DebugEmbeddedLDAPWriteOverrideProps")) {
            return 159;
         } else if (var1.equals("DebugEventManager")) {
            return 70;
         } else if (var1.equals("DebugFileDistributionServlet")) {
            return 244;
         } else if (var1.equals("DebugHttp")) {
            return 51;
         } else if (var1.equals("DebugHttpLogging")) {
            return 54;
         } else if (var1.equals("DebugHttpSessions")) {
            return 53;
         } else if (var1.equals("DebugIIOPNaming")) {
            return 302;
         } else if (var1.equals("DebugIIOPTunneling")) {
            return 303;
         } else if (var1.equals("DebugJ2EEManagement")) {
            return 301;
         } else if (var1.equals("DebugJAXPDebugLevel")) {
            return 211;
         } else if (var1.equals("DebugJAXPDebugName")) {
            return 212;
         } else if (var1.equals("DebugJAXPIncludeClass")) {
            return 216;
         } else if (var1.equals("DebugJAXPIncludeLocation")) {
            return 217;
         } else if (var1.equals("DebugJAXPIncludeName")) {
            return 215;
         } else if (var1.equals("DebugJAXPIncludeTime")) {
            return 214;
         } else if (var1.equals("DebugJAXPOutputStream")) {
            return 213;
         } else if (var1.equals("DebugJAXPUseShortClass")) {
            return 218;
         } else if (var1.equals("DebugJDBCConn")) {
            return 182;
         } else if (var1.equals("DebugJDBCDriverLogging")) {
            return 185;
         } else if (var1.equals("DebugJDBCInternal")) {
            return 186;
         } else if (var1.equals("DebugJDBCONS")) {
            return 188;
         } else if (var1.equals("DebugJDBCRAC")) {
            return 187;
         } else if (var1.equals("DebugJDBCREPLAY")) {
            return 190;
         } else if (var1.equals("DebugJDBCRMI")) {
            return 184;
         } else if (var1.equals("DebugJDBCSQL")) {
            return 183;
         } else if (var1.equals("DebugJDBCUCP")) {
            return 189;
         } else if (var1.equals("DebugJMSAME")) {
            return 103;
         } else if (var1.equals("DebugJMSBackEnd")) {
            return 91;
         } else if (var1.equals("DebugJMSBoot")) {
            return 100;
         } else if (var1.equals("DebugJMSCDS")) {
            return 109;
         } else if (var1.equals("DebugJMSCommon")) {
            return 93;
         } else if (var1.equals("DebugJMSConfig")) {
            return 94;
         } else if (var1.equals("DebugJMSDispatcher")) {
            return 98;
         } else if (var1.equals("DebugJMSDistTopic")) {
            return 95;
         } else if (var1.equals("DebugJMSDurableSubscribers")) {
            return 101;
         } else if (var1.equals("DebugJMSFrontEnd")) {
            return 92;
         } else if (var1.equals("DebugJMSJDBCScavengeOnFlush")) {
            return 102;
         } else if (var1.equals("DebugJMSLocking")) {
            return 96;
         } else if (var1.equals("DebugJMSMessagePath")) {
            return 106;
         } else if (var1.equals("DebugJMSModule")) {
            return 105;
         } else if (var1.equals("DebugJMSPauseResume")) {
            return 104;
         } else if (var1.equals("DebugJMSSAF")) {
            return 107;
         } else if (var1.equals("DebugJMSStore")) {
            return 99;
         } else if (var1.equals("DebugJMST3Server")) {
            return 324;
         } else if (var1.equals("DebugJMSWrappers")) {
            return 108;
         } else if (var1.equals("DebugJMSXA")) {
            return 97;
         } else if (var1.equals("DebugJMX")) {
            return 293;
         } else if (var1.equals("DebugJMXCompatibility")) {
            return 298;
         } else if (var1.equals("DebugJMXCore")) {
            return 294;
         } else if (var1.equals("DebugJMXDomain")) {
            return 296;
         } else if (var1.equals("DebugJMXEdit")) {
            return 297;
         } else if (var1.equals("DebugJMXRuntime")) {
            return 295;
         } else if (var1.equals("DebugJNDI")) {
            return 86;
         } else if (var1.equals("DebugJNDIFactories")) {
            return 88;
         } else if (var1.equals("DebugJNDIResolution")) {
            return 87;
         } else if (var1.equals("DebugJTA2PC")) {
            return 114;
         } else if (var1.equals("DebugJTA2PCStackTrace")) {
            return 115;
         } else if (var1.equals("DebugJTAAPI")) {
            return 120;
         } else if (var1.equals("DebugJTAGateway")) {
            return 122;
         } else if (var1.equals("DebugJTAGatewayStackTrace")) {
            return 123;
         } else if (var1.equals("DebugJTAHealth")) {
            return 130;
         } else if (var1.equals("DebugJTAJDBC")) {
            return 117;
         } else if (var1.equals("DebugJTALLR")) {
            return 129;
         } else if (var1.equals("DebugJTALifecycle")) {
            return 128;
         } else if (var1.equals("DebugJTAMigration")) {
            return 127;
         } else if (var1.equals("DebugJTANaming")) {
            return 124;
         } else if (var1.equals("DebugJTANamingStackTrace")) {
            return 125;
         } else if (var1.equals("DebugJTANonXA")) {
            return 111;
         } else if (var1.equals("DebugJTAPropagate")) {
            return 121;
         } else if (var1.equals("DebugJTARMI")) {
            return 113;
         } else if (var1.equals("DebugJTARecovery")) {
            return 118;
         } else if (var1.equals("DebugJTARecoveryStackTrace")) {
            return 119;
         } else if (var1.equals("DebugJTAResourceHealth")) {
            return 126;
         } else if (var1.equals("DebugJTAResourceName")) {
            return 132;
         } else if (var1.equals("DebugJTATLOG")) {
            return 116;
         } else if (var1.equals("DebugJTATransactionName")) {
            return 131;
         } else if (var1.equals("DebugJTAXA")) {
            return 110;
         } else if (var1.equals("DebugJTAXAStackTrace")) {
            return 112;
         } else if (var1.equals("DebugJpaDataCache")) {
            return 317;
         } else if (var1.equals("DebugJpaEnhance")) {
            return 314;
         } else if (var1.equals("DebugJpaJdbcJdbc")) {
            return 322;
         } else if (var1.equals("DebugJpaJdbcSchema")) {
            return 323;
         } else if (var1.equals("DebugJpaJdbcSql")) {
            return 321;
         } else if (var1.equals("DebugJpaManage")) {
            return 319;
         } else if (var1.equals("DebugJpaMetaData")) {
            return 313;
         } else if (var1.equals("DebugJpaProfile")) {
            return 320;
         } else if (var1.equals("DebugJpaQuery")) {
            return 316;
         } else if (var1.equals("DebugJpaRuntime")) {
            return 315;
         } else if (var1.equals("DebugJpaTool")) {
            return 318;
         } else if (var1.equals("DebugLeaderElection")) {
            return 79;
         } else if (var1.equals("DebugLibraries")) {
            return 45;
         } else if (var1.equals("DebugLoggingConfiguration")) {
            return 273;
         } else if (var1.equals("DebugManagementServicesResource")) {
            return 327;
         } else if (var1.equals("DebugMaskCriterias")) {
            return 36;
         } else if (var1.equals("DebugMessagingBridgeDumpToConsole")) {
            return 195;
         } else if (var1.equals("DebugMessagingBridgeDumpToLog")) {
            return 194;
         } else if (var1.equals("DebugMessagingBridgeRuntime")) {
            return 192;
         } else if (var1.equals("DebugMessagingBridgeRuntimeVerbose")) {
            return 193;
         } else if (var1.equals("DebugMessagingBridgeStartup")) {
            return 191;
         } else if (var1.equals("DebugMessagingKernel")) {
            return 133;
         } else if (var1.equals("DebugMessagingKernelBoot")) {
            return 134;
         } else if (var1.equals("DebugPathSvc")) {
            return 144;
         } else if (var1.equals("DebugPathSvcVerbose")) {
            return 145;
         } else if (var1.equals("DebugRA")) {
            return 276;
         } else if (var1.equals("DebugRAClassloader")) {
            return 291;
         } else if (var1.equals("DebugRAConnEvents")) {
            return 288;
         } else if (var1.equals("DebugRAConnections")) {
            return 287;
         } else if (var1.equals("DebugRADeployment")) {
            return 283;
         } else if (var1.equals("DebugRALifecycle")) {
            return 281;
         } else if (var1.equals("DebugRALocalOut")) {
            return 280;
         } else if (var1.equals("DebugRAParsing")) {
            return 284;
         } else if (var1.equals("DebugRAPoolVerbose")) {
            return 275;
         } else if (var1.equals("DebugRAPooling")) {
            return 286;
         } else if (var1.equals("DebugRASecurityCtx")) {
            return 285;
         } else if (var1.equals("DebugRAWork")) {
            return 289;
         } else if (var1.equals("DebugRAWorkEvents")) {
            return 290;
         } else if (var1.equals("DebugRAXAin")) {
            return 277;
         } else if (var1.equals("DebugRAXAout")) {
            return 278;
         } else if (var1.equals("DebugRAXAwork")) {
            return 279;
         } else if (var1.equals("DebugReplication")) {
            return 76;
         } else if (var1.equals("DebugReplicationDetails")) {
            return 77;
         } else if (var1.equals("DebugSAFAdmin")) {
            return 136;
         } else if (var1.equals("DebugSAFLifeCycle")) {
            return 135;
         } else if (var1.equals("DebugSAFManager")) {
            return 137;
         } else if (var1.equals("DebugSAFMessagePath")) {
            return 141;
         } else if (var1.equals("DebugSAFReceivingAgent")) {
            return 139;
         } else if (var1.equals("DebugSAFSendingAgent")) {
            return 138;
         } else if (var1.equals("DebugSAFStore")) {
            return 142;
         } else if (var1.equals("DebugSAFTransport")) {
            return 140;
         } else if (var1.equals("DebugSAFVerbose")) {
            return 143;
         } else if (var1.equals("DebugSNMPAgent")) {
            return 269;
         } else if (var1.equals("DebugSNMPExtensionProvider")) {
            return 271;
         } else if (var1.equals("DebugSNMPProtocolTCP")) {
            return 270;
         } else if (var1.equals("DebugSNMPToolkit")) {
            return 268;
         } else if (var1.equals("DebugScaContainer")) {
            return 146;
         } else if (var1.equals("DebugSecurity")) {
            return 148;
         } else if (var1.equals("DebugSecurityAdjudicator")) {
            return 160;
         } else if (var1.equals("DebugSecurityAtn")) {
            return 161;
         } else if (var1.equals("DebugSecurityAtz")) {
            return 162;
         } else if (var1.equals("DebugSecurityAuditor")) {
            return 163;
         } else if (var1.equals("DebugSecurityCertPath")) {
            return 167;
         } else if (var1.equals("DebugSecurityCredMap")) {
            return 164;
         } else if (var1.equals("DebugSecurityEEngine")) {
            return 170;
         } else if (var1.equals("DebugSecurityEncryptionService")) {
            return 165;
         } else if (var1.equals("DebugSecurityJACC")) {
            return 171;
         } else if (var1.equals("DebugSecurityJACCNonPolicy")) {
            return 172;
         } else if (var1.equals("DebugSecurityJACCPolicy")) {
            return 173;
         } else if (var1.equals("DebugSecurityKeyStore")) {
            return 166;
         } else if (var1.equals("DebugSecurityPasswordPolicy")) {
            return 149;
         } else if (var1.equals("DebugSecurityPredicate")) {
            return 152;
         } else if (var1.equals("DebugSecurityProfiler")) {
            return 168;
         } else if (var1.equals("DebugSecurityRealm")) {
            return 147;
         } else if (var1.equals("DebugSecurityRoleMap")) {
            return 169;
         } else if (var1.equals("DebugSecuritySAML2Atn")) {
            return 179;
         } else if (var1.equals("DebugSecuritySAML2CredMap")) {
            return 180;
         } else if (var1.equals("DebugSecuritySAML2Lib")) {
            return 178;
         } else if (var1.equals("DebugSecuritySAML2Service")) {
            return 181;
         } else if (var1.equals("DebugSecuritySAMLAtn")) {
            return 175;
         } else if (var1.equals("DebugSecuritySAMLCredMap")) {
            return 176;
         } else if (var1.equals("DebugSecuritySAMLLib")) {
            return 174;
         } else if (var1.equals("DebugSecuritySAMLService")) {
            return 177;
         } else if (var1.equals("DebugSecuritySSL")) {
            return 153;
         } else if (var1.equals("DebugSecuritySSLEaten")) {
            return 154;
         } else if (var1.equals("DebugSecurityService")) {
            return 151;
         } else if (var1.equals("DebugSecurityUserLockout")) {
            return 150;
         } else if (var1.equals("DebugSelfTuning")) {
            return 34;
         } else if (var1.equals("DebugServerLifeCycle")) {
            return 305;
         } else if (var1.equals("DebugServerMigration")) {
            return 71;
         } else if (var1.equals("DebugServerStartStatistics")) {
            return 326;
         } else if (var1.equals("DebugStoreAdmin")) {
            return 202;
         } else if (var1.equals("DebugStoreIOLogical")) {
            return 196;
         } else if (var1.equals("DebugStoreIOLogicalBoot")) {
            return 197;
         } else if (var1.equals("DebugStoreIOPhysical")) {
            return 198;
         } else if (var1.equals("DebugStoreIOPhysicalVerbose")) {
            return 199;
         } else if (var1.equals("DebugStoreXA")) {
            return 200;
         } else if (var1.equals("DebugStoreXAVerbose")) {
            return 201;
         } else if (var1.equals("DebugTunnelingConnection")) {
            return 90;
         } else if (var1.equals("DebugTunnelingConnectionTimeout")) {
            return 89;
         } else if (var1.equals("DebugURLResolution")) {
            return 52;
         } else if (var1.equals("DebugWANReplicationDetails")) {
            return 292;
         } else if (var1.equals("DebugWTCConfig")) {
            return 306;
         } else if (var1.equals("DebugWTCCorbaEx")) {
            return 311;
         } else if (var1.equals("DebugWTCGwtEx")) {
            return 309;
         } else if (var1.equals("DebugWTCJatmiEx")) {
            return 310;
         } else if (var1.equals("DebugWTCTDomPdu")) {
            return 307;
         } else if (var1.equals("DebugWTCUData")) {
            return 308;
         } else if (var1.equals("DebugWTCtBridgeEx")) {
            return 312;
         } else if (var1.equals("DebugWebAppIdentityAssertion")) {
            return 55;
         } else if (var1.equals("DebugWebAppModule")) {
            return 57;
         } else if (var1.equals("DebugWebAppSecurity")) {
            return 56;
         } else if (var1.equals("DebugXMLEntityCacheDebugLevel")) {
            return 219;
         } else if (var1.equals("DebugXMLEntityCacheDebugName")) {
            return 220;
         } else if (var1.equals("DebugXMLEntityCacheIncludeClass")) {
            return 224;
         } else if (var1.equals("DebugXMLEntityCacheIncludeLocation")) {
            return 225;
         } else if (var1.equals("DebugXMLEntityCacheIncludeName")) {
            return 223;
         } else if (var1.equals("DebugXMLEntityCacheIncludeTime")) {
            return 222;
         } else if (var1.equals("DebugXMLEntityCacheOutputStream")) {
            return 221;
         } else if (var1.equals("DebugXMLEntityCacheUseShortClass")) {
            return 226;
         } else if (var1.equals("DebugXMLRegistryDebugLevel")) {
            return 203;
         } else if (var1.equals("DebugXMLRegistryDebugName")) {
            return 204;
         } else if (var1.equals("DebugXMLRegistryIncludeClass")) {
            return 208;
         } else if (var1.equals("DebugXMLRegistryIncludeLocation")) {
            return 209;
         } else if (var1.equals("DebugXMLRegistryIncludeName")) {
            return 207;
         } else if (var1.equals("DebugXMLRegistryIncludeTime")) {
            return 206;
         } else if (var1.equals("DebugXMLRegistryOutputStream")) {
            return 205;
         } else if (var1.equals("DebugXMLRegistryUseShortClass")) {
            return 210;
         } else if (var1.equals("DefaultStore")) {
            return 49;
         } else if (var1.equals("DiagnosticContextDebugMode")) {
            return 35;
         } else if (var1.equals("ListenThreadDebug")) {
            return 38;
         } else if (var1.equals("MagicThreadDumpBackToSocket")) {
            return 42;
         } else if (var1.equals("MagicThreadDumpFile")) {
            return 41;
         } else if (var1.equals("MagicThreadDumpHost")) {
            return 40;
         } else if (var1.equals("MasterDeployer")) {
            return 234;
         } else if (var1.equals("RedefiningClassLoader")) {
            return 47;
         } else if (var1.equals("Server")) {
            return 37;
         } else if (var1.equals("SlaveDeployer")) {
            return 235;
         } else if (var1.equals("WebModule")) {
            return 239;
         } else {
            return var1.equals("MagicThreadDumpEnabled") ? 39 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         var1.add(new ArrayIterator(this.bean.getDebugScopes()));
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
            if (this.bean.isApplicationContainerSet()) {
               var2.append("ApplicationContainer");
               var2.append(String.valueOf(this.bean.getApplicationContainer()));
            }

            if (this.bean.isBugReportServiceWsdlUrlSet()) {
               var2.append("BugReportServiceWsdlUrl");
               var2.append(String.valueOf(this.bean.getBugReportServiceWsdlUrl()));
            }

            if (this.bean.isClassChangeNotifierSet()) {
               var2.append("ClassChangeNotifier");
               var2.append(String.valueOf(this.bean.getClassChangeNotifier()));
            }

            if (this.bean.isClassFinderSet()) {
               var2.append("ClassFinder");
               var2.append(String.valueOf(this.bean.getClassFinder()));
            }

            if (this.bean.isClassLoaderSet()) {
               var2.append("ClassLoader");
               var2.append(String.valueOf(this.bean.getClassLoader()));
            }

            if (this.bean.isClassLoaderVerboseSet()) {
               var2.append("ClassLoaderVerbose");
               var2.append(String.valueOf(this.bean.getClassLoaderVerbose()));
            }

            if (this.bean.isClassloaderWebAppSet()) {
               var2.append("ClassloaderWebApp");
               var2.append(String.valueOf(this.bean.getClassloaderWebApp()));
            }

            if (this.bean.isClasspathServletSet()) {
               var2.append("ClasspathServlet");
               var2.append(String.valueOf(this.bean.getClasspathServlet()));
            }

            if (this.bean.isDebugAppContainerSet()) {
               var2.append("DebugAppContainer");
               var2.append(String.valueOf(this.bean.getDebugAppContainer()));
            }

            if (this.bean.isDebugAsyncQueueSet()) {
               var2.append("DebugAsyncQueue");
               var2.append(String.valueOf(this.bean.getDebugAsyncQueue()));
            }

            if (this.bean.isDebugBootstrapServletSet()) {
               var2.append("DebugBootstrapServlet");
               var2.append(String.valueOf(this.bean.getDebugBootstrapServlet()));
            }

            if (this.bean.isDebugCertRevocCheckSet()) {
               var2.append("DebugCertRevocCheck");
               var2.append(String.valueOf(this.bean.getDebugCertRevocCheck()));
            }

            if (this.bean.isDebugClassRedefSet()) {
               var2.append("DebugClassRedef");
               var2.append(String.valueOf(this.bean.getDebugClassRedef()));
            }

            if (this.bean.isDebugClassSizeSet()) {
               var2.append("DebugClassSize");
               var2.append(String.valueOf(this.bean.getDebugClassSize()));
            }

            if (this.bean.isDebugClusterSet()) {
               var2.append("DebugCluster");
               var2.append(String.valueOf(this.bean.getDebugCluster()));
            }

            if (this.bean.isDebugClusterAnnouncementsSet()) {
               var2.append("DebugClusterAnnouncements");
               var2.append(String.valueOf(this.bean.getDebugClusterAnnouncements()));
            }

            if (this.bean.isDebugClusterFragmentsSet()) {
               var2.append("DebugClusterFragments");
               var2.append(String.valueOf(this.bean.getDebugClusterFragments()));
            }

            if (this.bean.isDebugClusterHeartbeatsSet()) {
               var2.append("DebugClusterHeartbeats");
               var2.append(String.valueOf(this.bean.getDebugClusterHeartbeats()));
            }

            if (this.bean.isDebugConfigurationEditSet()) {
               var2.append("DebugConfigurationEdit");
               var2.append(String.valueOf(this.bean.getDebugConfigurationEdit()));
            }

            if (this.bean.isDebugConfigurationRuntimeSet()) {
               var2.append("DebugConfigurationRuntime");
               var2.append(String.valueOf(this.bean.getDebugConfigurationRuntime()));
            }

            if (this.bean.isDebugConnectorServiceSet()) {
               var2.append("DebugConnectorService");
               var2.append(String.valueOf(this.bean.getDebugConnectorService()));
            }

            if (this.bean.isDebugConsensusLeasingSet()) {
               var2.append("DebugConsensusLeasing");
               var2.append(String.valueOf(this.bean.getDebugConsensusLeasing()));
            }

            if (this.bean.isDebugDRSCallsSet()) {
               var2.append("DebugDRSCalls");
               var2.append(String.valueOf(this.bean.getDebugDRSCalls()));
            }

            if (this.bean.isDebugDRSHeartbeatsSet()) {
               var2.append("DebugDRSHeartbeats");
               var2.append(String.valueOf(this.bean.getDebugDRSHeartbeats()));
            }

            if (this.bean.isDebugDRSMessagesSet()) {
               var2.append("DebugDRSMessages");
               var2.append(String.valueOf(this.bean.getDebugDRSMessages()));
            }

            if (this.bean.isDebugDRSQueuesSet()) {
               var2.append("DebugDRSQueues");
               var2.append(String.valueOf(this.bean.getDebugDRSQueues()));
            }

            if (this.bean.isDebugDRSStateTransitionsSet()) {
               var2.append("DebugDRSStateTransitions");
               var2.append(String.valueOf(this.bean.getDebugDRSStateTransitions()));
            }

            if (this.bean.isDebugDRSUpdateStatusSet()) {
               var2.append("DebugDRSUpdateStatus");
               var2.append(String.valueOf(this.bean.getDebugDRSUpdateStatus()));
            }

            if (this.bean.isDebugDeploySet()) {
               var2.append("DebugDeploy");
               var2.append(String.valueOf(this.bean.getDebugDeploy()));
            }

            if (this.bean.isDebugDeploymentSet()) {
               var2.append("DebugDeployment");
               var2.append(String.valueOf(this.bean.getDebugDeployment()));
            }

            if (this.bean.isDebugDeploymentServiceSet()) {
               var2.append("DebugDeploymentService");
               var2.append(String.valueOf(this.bean.getDebugDeploymentService()));
            }

            if (this.bean.isDebugDeploymentServiceInternalSet()) {
               var2.append("DebugDeploymentServiceInternal");
               var2.append(String.valueOf(this.bean.getDebugDeploymentServiceInternal()));
            }

            if (this.bean.isDebugDeploymentServiceStatusUpdatesSet()) {
               var2.append("DebugDeploymentServiceStatusUpdates");
               var2.append(String.valueOf(this.bean.getDebugDeploymentServiceStatusUpdates()));
            }

            if (this.bean.isDebugDeploymentServiceTransportSet()) {
               var2.append("DebugDeploymentServiceTransport");
               var2.append(String.valueOf(this.bean.getDebugDeploymentServiceTransport()));
            }

            if (this.bean.isDebugDeploymentServiceTransportHttpSet()) {
               var2.append("DebugDeploymentServiceTransportHttp");
               var2.append(String.valueOf(this.bean.getDebugDeploymentServiceTransportHttp()));
            }

            if (this.bean.isDebugDescriptorSet()) {
               var2.append("DebugDescriptor");
               var2.append(String.valueOf(this.bean.getDebugDescriptor()));
            }

            if (this.bean.isDebugDiagnosticAccessorSet()) {
               var2.append("DebugDiagnosticAccessor");
               var2.append(String.valueOf(this.bean.getDebugDiagnosticAccessor()));
            }

            if (this.bean.isDebugDiagnosticArchiveSet()) {
               var2.append("DebugDiagnosticArchive");
               var2.append(String.valueOf(this.bean.getDebugDiagnosticArchive()));
            }

            if (this.bean.isDebugDiagnosticArchiveRetirementSet()) {
               var2.append("DebugDiagnosticArchiveRetirement");
               var2.append(String.valueOf(this.bean.getDebugDiagnosticArchiveRetirement()));
            }

            if (this.bean.isDebugDiagnosticCollectionsSet()) {
               var2.append("DebugDiagnosticCollections");
               var2.append(String.valueOf(this.bean.getDebugDiagnosticCollections()));
            }

            if (this.bean.isDebugDiagnosticContextSet()) {
               var2.append("DebugDiagnosticContext");
               var2.append(String.valueOf(this.bean.getDebugDiagnosticContext()));
            }

            if (this.bean.isDebugDiagnosticDataGatheringSet()) {
               var2.append("DebugDiagnosticDataGathering");
               var2.append(String.valueOf(this.bean.getDebugDiagnosticDataGathering()));
            }

            if (this.bean.isDebugDiagnosticFileArchiveSet()) {
               var2.append("DebugDiagnosticFileArchive");
               var2.append(String.valueOf(this.bean.getDebugDiagnosticFileArchive()));
            }

            if (this.bean.isDebugDiagnosticImageSet()) {
               var2.append("DebugDiagnosticImage");
               var2.append(String.valueOf(this.bean.getDebugDiagnosticImage()));
            }

            if (this.bean.isDebugDiagnosticInstrumentationSet()) {
               var2.append("DebugDiagnosticInstrumentation");
               var2.append(String.valueOf(this.bean.getDebugDiagnosticInstrumentation()));
            }

            if (this.bean.isDebugDiagnosticInstrumentationActionsSet()) {
               var2.append("DebugDiagnosticInstrumentationActions");
               var2.append(String.valueOf(this.bean.getDebugDiagnosticInstrumentationActions()));
            }

            if (this.bean.isDebugDiagnosticInstrumentationConfigSet()) {
               var2.append("DebugDiagnosticInstrumentationConfig");
               var2.append(String.valueOf(this.bean.getDebugDiagnosticInstrumentationConfig()));
            }

            if (this.bean.isDebugDiagnosticInstrumentationEventsSet()) {
               var2.append("DebugDiagnosticInstrumentationEvents");
               var2.append(String.valueOf(this.bean.getDebugDiagnosticInstrumentationEvents()));
            }

            if (this.bean.isDebugDiagnosticInstrumentationWeavingSet()) {
               var2.append("DebugDiagnosticInstrumentationWeaving");
               var2.append(String.valueOf(this.bean.getDebugDiagnosticInstrumentationWeaving()));
            }

            if (this.bean.isDebugDiagnosticInstrumentationWeavingMatchesSet()) {
               var2.append("DebugDiagnosticInstrumentationWeavingMatches");
               var2.append(String.valueOf(this.bean.getDebugDiagnosticInstrumentationWeavingMatches()));
            }

            if (this.bean.isDebugDiagnosticJdbcArchiveSet()) {
               var2.append("DebugDiagnosticJdbcArchive");
               var2.append(String.valueOf(this.bean.getDebugDiagnosticJdbcArchive()));
            }

            if (this.bean.isDebugDiagnosticLifecycleHandlersSet()) {
               var2.append("DebugDiagnosticLifecycleHandlers");
               var2.append(String.valueOf(this.bean.getDebugDiagnosticLifecycleHandlers()));
            }

            if (this.bean.isDebugDiagnosticQuerySet()) {
               var2.append("DebugDiagnosticQuery");
               var2.append(String.valueOf(this.bean.getDebugDiagnosticQuery()));
            }

            if (this.bean.isDebugDiagnosticWatchSet()) {
               var2.append("DebugDiagnosticWatch");
               var2.append(String.valueOf(this.bean.getDebugDiagnosticWatch()));
            }

            if (this.bean.isDebugDiagnosticWlstoreArchiveSet()) {
               var2.append("DebugDiagnosticWlstoreArchive");
               var2.append(String.valueOf(this.bean.getDebugDiagnosticWlstoreArchive()));
            }

            if (this.bean.isDebugDiagnosticsHarvesterSet()) {
               var2.append("DebugDiagnosticsHarvester");
               var2.append(String.valueOf(this.bean.getDebugDiagnosticsHarvester()));
            }

            if (this.bean.isDebugDiagnosticsHarvesterDataSet()) {
               var2.append("DebugDiagnosticsHarvesterData");
               var2.append(String.valueOf(this.bean.getDebugDiagnosticsHarvesterData()));
            }

            if (this.bean.isDebugDiagnosticsHarvesterMBeanPluginSet()) {
               var2.append("DebugDiagnosticsHarvesterMBeanPlugin");
               var2.append(String.valueOf(this.bean.getDebugDiagnosticsHarvesterMBeanPlugin()));
            }

            if (this.bean.isDebugDiagnosticsHarvesterTreeBeanPluginSet()) {
               var2.append("DebugDiagnosticsHarvesterTreeBeanPlugin");
               var2.append(String.valueOf(this.bean.getDebugDiagnosticsHarvesterTreeBeanPlugin()));
            }

            if (this.bean.isDebugDiagnosticsModuleSet()) {
               var2.append("DebugDiagnosticsModule");
               var2.append(String.valueOf(this.bean.getDebugDiagnosticsModule()));
            }

            if (this.bean.isDebugDomainLogHandlerSet()) {
               var2.append("DebugDomainLogHandler");
               var2.append(String.valueOf(this.bean.getDebugDomainLogHandler()));
            }

            if (this.bean.isDebugEjbCachingSet()) {
               var2.append("DebugEjbCaching");
               var2.append(String.valueOf(this.bean.getDebugEjbCaching()));
            }

            if (this.bean.isDebugEjbCmpDeploymentSet()) {
               var2.append("DebugEjbCmpDeployment");
               var2.append(String.valueOf(this.bean.getDebugEjbCmpDeployment()));
            }

            if (this.bean.isDebugEjbCmpRuntimeSet()) {
               var2.append("DebugEjbCmpRuntime");
               var2.append(String.valueOf(this.bean.getDebugEjbCmpRuntime()));
            }

            if (this.bean.isDebugEjbCompilationSet()) {
               var2.append("DebugEjbCompilation");
               var2.append(String.valueOf(this.bean.getDebugEjbCompilation()));
            }

            if (this.bean.isDebugEjbDeploymentSet()) {
               var2.append("DebugEjbDeployment");
               var2.append(String.valueOf(this.bean.getDebugEjbDeployment()));
            }

            if (this.bean.isDebugEjbInvokeSet()) {
               var2.append("DebugEjbInvoke");
               var2.append(String.valueOf(this.bean.getDebugEjbInvoke()));
            }

            if (this.bean.isDebugEjbLockingSet()) {
               var2.append("DebugEjbLocking");
               var2.append(String.valueOf(this.bean.getDebugEjbLocking()));
            }

            if (this.bean.isDebugEjbMdbConnectionSet()) {
               var2.append("DebugEjbMdbConnection");
               var2.append(String.valueOf(this.bean.getDebugEjbMdbConnection()));
            }

            if (this.bean.isDebugEjbPoolingSet()) {
               var2.append("DebugEjbPooling");
               var2.append(String.valueOf(this.bean.getDebugEjbPooling()));
            }

            if (this.bean.isDebugEjbSecuritySet()) {
               var2.append("DebugEjbSecurity");
               var2.append(String.valueOf(this.bean.getDebugEjbSecurity()));
            }

            if (this.bean.isDebugEjbSwappingSet()) {
               var2.append("DebugEjbSwapping");
               var2.append(String.valueOf(this.bean.getDebugEjbSwapping()));
            }

            if (this.bean.isDebugEjbTimersSet()) {
               var2.append("DebugEjbTimers");
               var2.append(String.valueOf(this.bean.getDebugEjbTimers()));
            }

            if (this.bean.isDebugEmbeddedLDAPSet()) {
               var2.append("DebugEmbeddedLDAP");
               var2.append(String.valueOf(this.bean.getDebugEmbeddedLDAP()));
            }

            if (this.bean.isDebugEmbeddedLDAPLogLevelSet()) {
               var2.append("DebugEmbeddedLDAPLogLevel");
               var2.append(String.valueOf(this.bean.getDebugEmbeddedLDAPLogLevel()));
            }

            if (this.bean.isDebugEmbeddedLDAPLogToConsoleSet()) {
               var2.append("DebugEmbeddedLDAPLogToConsole");
               var2.append(String.valueOf(this.bean.getDebugEmbeddedLDAPLogToConsole()));
            }

            if (this.bean.isDebugEmbeddedLDAPWriteOverridePropsSet()) {
               var2.append("DebugEmbeddedLDAPWriteOverrideProps");
               var2.append(String.valueOf(this.bean.getDebugEmbeddedLDAPWriteOverrideProps()));
            }

            if (this.bean.isDebugEventManagerSet()) {
               var2.append("DebugEventManager");
               var2.append(String.valueOf(this.bean.getDebugEventManager()));
            }

            if (this.bean.isDebugFileDistributionServletSet()) {
               var2.append("DebugFileDistributionServlet");
               var2.append(String.valueOf(this.bean.getDebugFileDistributionServlet()));
            }

            if (this.bean.isDebugHttpSet()) {
               var2.append("DebugHttp");
               var2.append(String.valueOf(this.bean.getDebugHttp()));
            }

            if (this.bean.isDebugHttpLoggingSet()) {
               var2.append("DebugHttpLogging");
               var2.append(String.valueOf(this.bean.getDebugHttpLogging()));
            }

            if (this.bean.isDebugHttpSessionsSet()) {
               var2.append("DebugHttpSessions");
               var2.append(String.valueOf(this.bean.getDebugHttpSessions()));
            }

            if (this.bean.isDebugIIOPNamingSet()) {
               var2.append("DebugIIOPNaming");
               var2.append(String.valueOf(this.bean.getDebugIIOPNaming()));
            }

            if (this.bean.isDebugIIOPTunnelingSet()) {
               var2.append("DebugIIOPTunneling");
               var2.append(String.valueOf(this.bean.getDebugIIOPTunneling()));
            }

            if (this.bean.isDebugJ2EEManagementSet()) {
               var2.append("DebugJ2EEManagement");
               var2.append(String.valueOf(this.bean.getDebugJ2EEManagement()));
            }

            if (this.bean.isDebugJAXPDebugLevelSet()) {
               var2.append("DebugJAXPDebugLevel");
               var2.append(String.valueOf(this.bean.getDebugJAXPDebugLevel()));
            }

            if (this.bean.isDebugJAXPDebugNameSet()) {
               var2.append("DebugJAXPDebugName");
               var2.append(String.valueOf(this.bean.getDebugJAXPDebugName()));
            }

            if (this.bean.isDebugJAXPIncludeClassSet()) {
               var2.append("DebugJAXPIncludeClass");
               var2.append(String.valueOf(this.bean.getDebugJAXPIncludeClass()));
            }

            if (this.bean.isDebugJAXPIncludeLocationSet()) {
               var2.append("DebugJAXPIncludeLocation");
               var2.append(String.valueOf(this.bean.getDebugJAXPIncludeLocation()));
            }

            if (this.bean.isDebugJAXPIncludeNameSet()) {
               var2.append("DebugJAXPIncludeName");
               var2.append(String.valueOf(this.bean.getDebugJAXPIncludeName()));
            }

            if (this.bean.isDebugJAXPIncludeTimeSet()) {
               var2.append("DebugJAXPIncludeTime");
               var2.append(String.valueOf(this.bean.getDebugJAXPIncludeTime()));
            }

            if (this.bean.isDebugJAXPOutputStreamSet()) {
               var2.append("DebugJAXPOutputStream");
               var2.append(String.valueOf(this.bean.getDebugJAXPOutputStream()));
            }

            if (this.bean.isDebugJAXPUseShortClassSet()) {
               var2.append("DebugJAXPUseShortClass");
               var2.append(String.valueOf(this.bean.getDebugJAXPUseShortClass()));
            }

            if (this.bean.isDebugJDBCConnSet()) {
               var2.append("DebugJDBCConn");
               var2.append(String.valueOf(this.bean.getDebugJDBCConn()));
            }

            if (this.bean.isDebugJDBCDriverLoggingSet()) {
               var2.append("DebugJDBCDriverLogging");
               var2.append(String.valueOf(this.bean.getDebugJDBCDriverLogging()));
            }

            if (this.bean.isDebugJDBCInternalSet()) {
               var2.append("DebugJDBCInternal");
               var2.append(String.valueOf(this.bean.getDebugJDBCInternal()));
            }

            if (this.bean.isDebugJDBCONSSet()) {
               var2.append("DebugJDBCONS");
               var2.append(String.valueOf(this.bean.getDebugJDBCONS()));
            }

            if (this.bean.isDebugJDBCRACSet()) {
               var2.append("DebugJDBCRAC");
               var2.append(String.valueOf(this.bean.getDebugJDBCRAC()));
            }

            if (this.bean.isDebugJDBCREPLAYSet()) {
               var2.append("DebugJDBCREPLAY");
               var2.append(String.valueOf(this.bean.getDebugJDBCREPLAY()));
            }

            if (this.bean.isDebugJDBCRMISet()) {
               var2.append("DebugJDBCRMI");
               var2.append(String.valueOf(this.bean.getDebugJDBCRMI()));
            }

            if (this.bean.isDebugJDBCSQLSet()) {
               var2.append("DebugJDBCSQL");
               var2.append(String.valueOf(this.bean.getDebugJDBCSQL()));
            }

            if (this.bean.isDebugJDBCUCPSet()) {
               var2.append("DebugJDBCUCP");
               var2.append(String.valueOf(this.bean.getDebugJDBCUCP()));
            }

            if (this.bean.isDebugJMSAMESet()) {
               var2.append("DebugJMSAME");
               var2.append(String.valueOf(this.bean.getDebugJMSAME()));
            }

            if (this.bean.isDebugJMSBackEndSet()) {
               var2.append("DebugJMSBackEnd");
               var2.append(String.valueOf(this.bean.getDebugJMSBackEnd()));
            }

            if (this.bean.isDebugJMSBootSet()) {
               var2.append("DebugJMSBoot");
               var2.append(String.valueOf(this.bean.getDebugJMSBoot()));
            }

            if (this.bean.isDebugJMSCDSSet()) {
               var2.append("DebugJMSCDS");
               var2.append(String.valueOf(this.bean.getDebugJMSCDS()));
            }

            if (this.bean.isDebugJMSCommonSet()) {
               var2.append("DebugJMSCommon");
               var2.append(String.valueOf(this.bean.getDebugJMSCommon()));
            }

            if (this.bean.isDebugJMSConfigSet()) {
               var2.append("DebugJMSConfig");
               var2.append(String.valueOf(this.bean.getDebugJMSConfig()));
            }

            if (this.bean.isDebugJMSDispatcherSet()) {
               var2.append("DebugJMSDispatcher");
               var2.append(String.valueOf(this.bean.getDebugJMSDispatcher()));
            }

            if (this.bean.isDebugJMSDistTopicSet()) {
               var2.append("DebugJMSDistTopic");
               var2.append(String.valueOf(this.bean.getDebugJMSDistTopic()));
            }

            if (this.bean.isDebugJMSDurableSubscribersSet()) {
               var2.append("DebugJMSDurableSubscribers");
               var2.append(String.valueOf(this.bean.getDebugJMSDurableSubscribers()));
            }

            if (this.bean.isDebugJMSFrontEndSet()) {
               var2.append("DebugJMSFrontEnd");
               var2.append(String.valueOf(this.bean.getDebugJMSFrontEnd()));
            }

            if (this.bean.isDebugJMSJDBCScavengeOnFlushSet()) {
               var2.append("DebugJMSJDBCScavengeOnFlush");
               var2.append(String.valueOf(this.bean.getDebugJMSJDBCScavengeOnFlush()));
            }

            if (this.bean.isDebugJMSLockingSet()) {
               var2.append("DebugJMSLocking");
               var2.append(String.valueOf(this.bean.getDebugJMSLocking()));
            }

            if (this.bean.isDebugJMSMessagePathSet()) {
               var2.append("DebugJMSMessagePath");
               var2.append(String.valueOf(this.bean.getDebugJMSMessagePath()));
            }

            if (this.bean.isDebugJMSModuleSet()) {
               var2.append("DebugJMSModule");
               var2.append(String.valueOf(this.bean.getDebugJMSModule()));
            }

            if (this.bean.isDebugJMSPauseResumeSet()) {
               var2.append("DebugJMSPauseResume");
               var2.append(String.valueOf(this.bean.getDebugJMSPauseResume()));
            }

            if (this.bean.isDebugJMSSAFSet()) {
               var2.append("DebugJMSSAF");
               var2.append(String.valueOf(this.bean.getDebugJMSSAF()));
            }

            if (this.bean.isDebugJMSStoreSet()) {
               var2.append("DebugJMSStore");
               var2.append(String.valueOf(this.bean.getDebugJMSStore()));
            }

            if (this.bean.isDebugJMST3ServerSet()) {
               var2.append("DebugJMST3Server");
               var2.append(String.valueOf(this.bean.getDebugJMST3Server()));
            }

            if (this.bean.isDebugJMSWrappersSet()) {
               var2.append("DebugJMSWrappers");
               var2.append(String.valueOf(this.bean.getDebugJMSWrappers()));
            }

            if (this.bean.isDebugJMSXASet()) {
               var2.append("DebugJMSXA");
               var2.append(String.valueOf(this.bean.getDebugJMSXA()));
            }

            if (this.bean.isDebugJMXSet()) {
               var2.append("DebugJMX");
               var2.append(String.valueOf(this.bean.getDebugJMX()));
            }

            if (this.bean.isDebugJMXCompatibilitySet()) {
               var2.append("DebugJMXCompatibility");
               var2.append(String.valueOf(this.bean.getDebugJMXCompatibility()));
            }

            if (this.bean.isDebugJMXCoreSet()) {
               var2.append("DebugJMXCore");
               var2.append(String.valueOf(this.bean.getDebugJMXCore()));
            }

            if (this.bean.isDebugJMXDomainSet()) {
               var2.append("DebugJMXDomain");
               var2.append(String.valueOf(this.bean.getDebugJMXDomain()));
            }

            if (this.bean.isDebugJMXEditSet()) {
               var2.append("DebugJMXEdit");
               var2.append(String.valueOf(this.bean.getDebugJMXEdit()));
            }

            if (this.bean.isDebugJMXRuntimeSet()) {
               var2.append("DebugJMXRuntime");
               var2.append(String.valueOf(this.bean.getDebugJMXRuntime()));
            }

            if (this.bean.isDebugJNDISet()) {
               var2.append("DebugJNDI");
               var2.append(String.valueOf(this.bean.getDebugJNDI()));
            }

            if (this.bean.isDebugJNDIFactoriesSet()) {
               var2.append("DebugJNDIFactories");
               var2.append(String.valueOf(this.bean.getDebugJNDIFactories()));
            }

            if (this.bean.isDebugJNDIResolutionSet()) {
               var2.append("DebugJNDIResolution");
               var2.append(String.valueOf(this.bean.getDebugJNDIResolution()));
            }

            if (this.bean.isDebugJTA2PCSet()) {
               var2.append("DebugJTA2PC");
               var2.append(String.valueOf(this.bean.getDebugJTA2PC()));
            }

            if (this.bean.isDebugJTA2PCStackTraceSet()) {
               var2.append("DebugJTA2PCStackTrace");
               var2.append(String.valueOf(this.bean.getDebugJTA2PCStackTrace()));
            }

            if (this.bean.isDebugJTAAPISet()) {
               var2.append("DebugJTAAPI");
               var2.append(String.valueOf(this.bean.getDebugJTAAPI()));
            }

            if (this.bean.isDebugJTAGatewaySet()) {
               var2.append("DebugJTAGateway");
               var2.append(String.valueOf(this.bean.getDebugJTAGateway()));
            }

            if (this.bean.isDebugJTAGatewayStackTraceSet()) {
               var2.append("DebugJTAGatewayStackTrace");
               var2.append(String.valueOf(this.bean.getDebugJTAGatewayStackTrace()));
            }

            if (this.bean.isDebugJTAHealthSet()) {
               var2.append("DebugJTAHealth");
               var2.append(String.valueOf(this.bean.getDebugJTAHealth()));
            }

            if (this.bean.isDebugJTAJDBCSet()) {
               var2.append("DebugJTAJDBC");
               var2.append(String.valueOf(this.bean.getDebugJTAJDBC()));
            }

            if (this.bean.isDebugJTALLRSet()) {
               var2.append("DebugJTALLR");
               var2.append(String.valueOf(this.bean.getDebugJTALLR()));
            }

            if (this.bean.isDebugJTALifecycleSet()) {
               var2.append("DebugJTALifecycle");
               var2.append(String.valueOf(this.bean.getDebugJTALifecycle()));
            }

            if (this.bean.isDebugJTAMigrationSet()) {
               var2.append("DebugJTAMigration");
               var2.append(String.valueOf(this.bean.getDebugJTAMigration()));
            }

            if (this.bean.isDebugJTANamingSet()) {
               var2.append("DebugJTANaming");
               var2.append(String.valueOf(this.bean.getDebugJTANaming()));
            }

            if (this.bean.isDebugJTANamingStackTraceSet()) {
               var2.append("DebugJTANamingStackTrace");
               var2.append(String.valueOf(this.bean.getDebugJTANamingStackTrace()));
            }

            if (this.bean.isDebugJTANonXASet()) {
               var2.append("DebugJTANonXA");
               var2.append(String.valueOf(this.bean.getDebugJTANonXA()));
            }

            if (this.bean.isDebugJTAPropagateSet()) {
               var2.append("DebugJTAPropagate");
               var2.append(String.valueOf(this.bean.getDebugJTAPropagate()));
            }

            if (this.bean.isDebugJTARMISet()) {
               var2.append("DebugJTARMI");
               var2.append(String.valueOf(this.bean.getDebugJTARMI()));
            }

            if (this.bean.isDebugJTARecoverySet()) {
               var2.append("DebugJTARecovery");
               var2.append(String.valueOf(this.bean.getDebugJTARecovery()));
            }

            if (this.bean.isDebugJTARecoveryStackTraceSet()) {
               var2.append("DebugJTARecoveryStackTrace");
               var2.append(String.valueOf(this.bean.getDebugJTARecoveryStackTrace()));
            }

            if (this.bean.isDebugJTAResourceHealthSet()) {
               var2.append("DebugJTAResourceHealth");
               var2.append(String.valueOf(this.bean.getDebugJTAResourceHealth()));
            }

            if (this.bean.isDebugJTAResourceNameSet()) {
               var2.append("DebugJTAResourceName");
               var2.append(String.valueOf(this.bean.getDebugJTAResourceName()));
            }

            if (this.bean.isDebugJTATLOGSet()) {
               var2.append("DebugJTATLOG");
               var2.append(String.valueOf(this.bean.getDebugJTATLOG()));
            }

            if (this.bean.isDebugJTATransactionNameSet()) {
               var2.append("DebugJTATransactionName");
               var2.append(String.valueOf(this.bean.getDebugJTATransactionName()));
            }

            if (this.bean.isDebugJTAXASet()) {
               var2.append("DebugJTAXA");
               var2.append(String.valueOf(this.bean.getDebugJTAXA()));
            }

            if (this.bean.isDebugJTAXAStackTraceSet()) {
               var2.append("DebugJTAXAStackTrace");
               var2.append(String.valueOf(this.bean.getDebugJTAXAStackTrace()));
            }

            if (this.bean.isDebugJpaDataCacheSet()) {
               var2.append("DebugJpaDataCache");
               var2.append(String.valueOf(this.bean.getDebugJpaDataCache()));
            }

            if (this.bean.isDebugJpaEnhanceSet()) {
               var2.append("DebugJpaEnhance");
               var2.append(String.valueOf(this.bean.getDebugJpaEnhance()));
            }

            if (this.bean.isDebugJpaJdbcJdbcSet()) {
               var2.append("DebugJpaJdbcJdbc");
               var2.append(String.valueOf(this.bean.getDebugJpaJdbcJdbc()));
            }

            if (this.bean.isDebugJpaJdbcSchemaSet()) {
               var2.append("DebugJpaJdbcSchema");
               var2.append(String.valueOf(this.bean.getDebugJpaJdbcSchema()));
            }

            if (this.bean.isDebugJpaJdbcSqlSet()) {
               var2.append("DebugJpaJdbcSql");
               var2.append(String.valueOf(this.bean.getDebugJpaJdbcSql()));
            }

            if (this.bean.isDebugJpaManageSet()) {
               var2.append("DebugJpaManage");
               var2.append(String.valueOf(this.bean.getDebugJpaManage()));
            }

            if (this.bean.isDebugJpaMetaDataSet()) {
               var2.append("DebugJpaMetaData");
               var2.append(String.valueOf(this.bean.getDebugJpaMetaData()));
            }

            if (this.bean.isDebugJpaProfileSet()) {
               var2.append("DebugJpaProfile");
               var2.append(String.valueOf(this.bean.getDebugJpaProfile()));
            }

            if (this.bean.isDebugJpaQuerySet()) {
               var2.append("DebugJpaQuery");
               var2.append(String.valueOf(this.bean.getDebugJpaQuery()));
            }

            if (this.bean.isDebugJpaRuntimeSet()) {
               var2.append("DebugJpaRuntime");
               var2.append(String.valueOf(this.bean.getDebugJpaRuntime()));
            }

            if (this.bean.isDebugJpaToolSet()) {
               var2.append("DebugJpaTool");
               var2.append(String.valueOf(this.bean.getDebugJpaTool()));
            }

            if (this.bean.isDebugLeaderElectionSet()) {
               var2.append("DebugLeaderElection");
               var2.append(String.valueOf(this.bean.getDebugLeaderElection()));
            }

            if (this.bean.isDebugLibrariesSet()) {
               var2.append("DebugLibraries");
               var2.append(String.valueOf(this.bean.getDebugLibraries()));
            }

            if (this.bean.isDebugLoggingConfigurationSet()) {
               var2.append("DebugLoggingConfiguration");
               var2.append(String.valueOf(this.bean.getDebugLoggingConfiguration()));
            }

            if (this.bean.isDebugManagementServicesResourceSet()) {
               var2.append("DebugManagementServicesResource");
               var2.append(String.valueOf(this.bean.getDebugManagementServicesResource()));
            }

            if (this.bean.isDebugMaskCriteriasSet()) {
               var2.append("DebugMaskCriterias");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getDebugMaskCriterias())));
            }

            if (this.bean.isDebugMessagingBridgeDumpToConsoleSet()) {
               var2.append("DebugMessagingBridgeDumpToConsole");
               var2.append(String.valueOf(this.bean.getDebugMessagingBridgeDumpToConsole()));
            }

            if (this.bean.isDebugMessagingBridgeDumpToLogSet()) {
               var2.append("DebugMessagingBridgeDumpToLog");
               var2.append(String.valueOf(this.bean.getDebugMessagingBridgeDumpToLog()));
            }

            if (this.bean.isDebugMessagingBridgeRuntimeSet()) {
               var2.append("DebugMessagingBridgeRuntime");
               var2.append(String.valueOf(this.bean.getDebugMessagingBridgeRuntime()));
            }

            if (this.bean.isDebugMessagingBridgeRuntimeVerboseSet()) {
               var2.append("DebugMessagingBridgeRuntimeVerbose");
               var2.append(String.valueOf(this.bean.getDebugMessagingBridgeRuntimeVerbose()));
            }

            if (this.bean.isDebugMessagingBridgeStartupSet()) {
               var2.append("DebugMessagingBridgeStartup");
               var2.append(String.valueOf(this.bean.getDebugMessagingBridgeStartup()));
            }

            if (this.bean.isDebugMessagingKernelSet()) {
               var2.append("DebugMessagingKernel");
               var2.append(String.valueOf(this.bean.getDebugMessagingKernel()));
            }

            if (this.bean.isDebugMessagingKernelBootSet()) {
               var2.append("DebugMessagingKernelBoot");
               var2.append(String.valueOf(this.bean.getDebugMessagingKernelBoot()));
            }

            if (this.bean.isDebugPathSvcSet()) {
               var2.append("DebugPathSvc");
               var2.append(String.valueOf(this.bean.getDebugPathSvc()));
            }

            if (this.bean.isDebugPathSvcVerboseSet()) {
               var2.append("DebugPathSvcVerbose");
               var2.append(String.valueOf(this.bean.getDebugPathSvcVerbose()));
            }

            if (this.bean.isDebugRASet()) {
               var2.append("DebugRA");
               var2.append(String.valueOf(this.bean.getDebugRA()));
            }

            if (this.bean.isDebugRAClassloaderSet()) {
               var2.append("DebugRAClassloader");
               var2.append(String.valueOf(this.bean.getDebugRAClassloader()));
            }

            if (this.bean.isDebugRAConnEventsSet()) {
               var2.append("DebugRAConnEvents");
               var2.append(String.valueOf(this.bean.getDebugRAConnEvents()));
            }

            if (this.bean.isDebugRAConnectionsSet()) {
               var2.append("DebugRAConnections");
               var2.append(String.valueOf(this.bean.getDebugRAConnections()));
            }

            if (this.bean.isDebugRADeploymentSet()) {
               var2.append("DebugRADeployment");
               var2.append(String.valueOf(this.bean.getDebugRADeployment()));
            }

            if (this.bean.isDebugRALifecycleSet()) {
               var2.append("DebugRALifecycle");
               var2.append(String.valueOf(this.bean.getDebugRALifecycle()));
            }

            if (this.bean.isDebugRALocalOutSet()) {
               var2.append("DebugRALocalOut");
               var2.append(String.valueOf(this.bean.getDebugRALocalOut()));
            }

            if (this.bean.isDebugRAParsingSet()) {
               var2.append("DebugRAParsing");
               var2.append(String.valueOf(this.bean.getDebugRAParsing()));
            }

            if (this.bean.isDebugRAPoolVerboseSet()) {
               var2.append("DebugRAPoolVerbose");
               var2.append(String.valueOf(this.bean.getDebugRAPoolVerbose()));
            }

            if (this.bean.isDebugRAPoolingSet()) {
               var2.append("DebugRAPooling");
               var2.append(String.valueOf(this.bean.getDebugRAPooling()));
            }

            if (this.bean.isDebugRASecurityCtxSet()) {
               var2.append("DebugRASecurityCtx");
               var2.append(String.valueOf(this.bean.getDebugRASecurityCtx()));
            }

            if (this.bean.isDebugRAWorkSet()) {
               var2.append("DebugRAWork");
               var2.append(String.valueOf(this.bean.getDebugRAWork()));
            }

            if (this.bean.isDebugRAWorkEventsSet()) {
               var2.append("DebugRAWorkEvents");
               var2.append(String.valueOf(this.bean.getDebugRAWorkEvents()));
            }

            if (this.bean.isDebugRAXAinSet()) {
               var2.append("DebugRAXAin");
               var2.append(String.valueOf(this.bean.getDebugRAXAin()));
            }

            if (this.bean.isDebugRAXAoutSet()) {
               var2.append("DebugRAXAout");
               var2.append(String.valueOf(this.bean.getDebugRAXAout()));
            }

            if (this.bean.isDebugRAXAworkSet()) {
               var2.append("DebugRAXAwork");
               var2.append(String.valueOf(this.bean.getDebugRAXAwork()));
            }

            if (this.bean.isDebugReplicationSet()) {
               var2.append("DebugReplication");
               var2.append(String.valueOf(this.bean.getDebugReplication()));
            }

            if (this.bean.isDebugReplicationDetailsSet()) {
               var2.append("DebugReplicationDetails");
               var2.append(String.valueOf(this.bean.getDebugReplicationDetails()));
            }

            if (this.bean.isDebugSAFAdminSet()) {
               var2.append("DebugSAFAdmin");
               var2.append(String.valueOf(this.bean.getDebugSAFAdmin()));
            }

            if (this.bean.isDebugSAFLifeCycleSet()) {
               var2.append("DebugSAFLifeCycle");
               var2.append(String.valueOf(this.bean.getDebugSAFLifeCycle()));
            }

            if (this.bean.isDebugSAFManagerSet()) {
               var2.append("DebugSAFManager");
               var2.append(String.valueOf(this.bean.getDebugSAFManager()));
            }

            if (this.bean.isDebugSAFMessagePathSet()) {
               var2.append("DebugSAFMessagePath");
               var2.append(String.valueOf(this.bean.getDebugSAFMessagePath()));
            }

            if (this.bean.isDebugSAFReceivingAgentSet()) {
               var2.append("DebugSAFReceivingAgent");
               var2.append(String.valueOf(this.bean.getDebugSAFReceivingAgent()));
            }

            if (this.bean.isDebugSAFSendingAgentSet()) {
               var2.append("DebugSAFSendingAgent");
               var2.append(String.valueOf(this.bean.getDebugSAFSendingAgent()));
            }

            if (this.bean.isDebugSAFStoreSet()) {
               var2.append("DebugSAFStore");
               var2.append(String.valueOf(this.bean.getDebugSAFStore()));
            }

            if (this.bean.isDebugSAFTransportSet()) {
               var2.append("DebugSAFTransport");
               var2.append(String.valueOf(this.bean.getDebugSAFTransport()));
            }

            if (this.bean.isDebugSAFVerboseSet()) {
               var2.append("DebugSAFVerbose");
               var2.append(String.valueOf(this.bean.getDebugSAFVerbose()));
            }

            if (this.bean.isDebugSNMPAgentSet()) {
               var2.append("DebugSNMPAgent");
               var2.append(String.valueOf(this.bean.getDebugSNMPAgent()));
            }

            if (this.bean.isDebugSNMPExtensionProviderSet()) {
               var2.append("DebugSNMPExtensionProvider");
               var2.append(String.valueOf(this.bean.getDebugSNMPExtensionProvider()));
            }

            if (this.bean.isDebugSNMPProtocolTCPSet()) {
               var2.append("DebugSNMPProtocolTCP");
               var2.append(String.valueOf(this.bean.getDebugSNMPProtocolTCP()));
            }

            if (this.bean.isDebugSNMPToolkitSet()) {
               var2.append("DebugSNMPToolkit");
               var2.append(String.valueOf(this.bean.getDebugSNMPToolkit()));
            }

            if (this.bean.isDebugScaContainerSet()) {
               var2.append("DebugScaContainer");
               var2.append(String.valueOf(this.bean.getDebugScaContainer()));
            }

            if (this.bean.isDebugSecuritySet()) {
               var2.append("DebugSecurity");
               var2.append(String.valueOf(this.bean.getDebugSecurity()));
            }

            if (this.bean.isDebugSecurityAdjudicatorSet()) {
               var2.append("DebugSecurityAdjudicator");
               var2.append(String.valueOf(this.bean.getDebugSecurityAdjudicator()));
            }

            if (this.bean.isDebugSecurityAtnSet()) {
               var2.append("DebugSecurityAtn");
               var2.append(String.valueOf(this.bean.getDebugSecurityAtn()));
            }

            if (this.bean.isDebugSecurityAtzSet()) {
               var2.append("DebugSecurityAtz");
               var2.append(String.valueOf(this.bean.getDebugSecurityAtz()));
            }

            if (this.bean.isDebugSecurityAuditorSet()) {
               var2.append("DebugSecurityAuditor");
               var2.append(String.valueOf(this.bean.getDebugSecurityAuditor()));
            }

            if (this.bean.isDebugSecurityCertPathSet()) {
               var2.append("DebugSecurityCertPath");
               var2.append(String.valueOf(this.bean.getDebugSecurityCertPath()));
            }

            if (this.bean.isDebugSecurityCredMapSet()) {
               var2.append("DebugSecurityCredMap");
               var2.append(String.valueOf(this.bean.getDebugSecurityCredMap()));
            }

            if (this.bean.isDebugSecurityEEngineSet()) {
               var2.append("DebugSecurityEEngine");
               var2.append(String.valueOf(this.bean.getDebugSecurityEEngine()));
            }

            if (this.bean.isDebugSecurityEncryptionServiceSet()) {
               var2.append("DebugSecurityEncryptionService");
               var2.append(String.valueOf(this.bean.getDebugSecurityEncryptionService()));
            }

            if (this.bean.isDebugSecurityJACCSet()) {
               var2.append("DebugSecurityJACC");
               var2.append(String.valueOf(this.bean.getDebugSecurityJACC()));
            }

            if (this.bean.isDebugSecurityJACCNonPolicySet()) {
               var2.append("DebugSecurityJACCNonPolicy");
               var2.append(String.valueOf(this.bean.getDebugSecurityJACCNonPolicy()));
            }

            if (this.bean.isDebugSecurityJACCPolicySet()) {
               var2.append("DebugSecurityJACCPolicy");
               var2.append(String.valueOf(this.bean.getDebugSecurityJACCPolicy()));
            }

            if (this.bean.isDebugSecurityKeyStoreSet()) {
               var2.append("DebugSecurityKeyStore");
               var2.append(String.valueOf(this.bean.getDebugSecurityKeyStore()));
            }

            if (this.bean.isDebugSecurityPasswordPolicySet()) {
               var2.append("DebugSecurityPasswordPolicy");
               var2.append(String.valueOf(this.bean.getDebugSecurityPasswordPolicy()));
            }

            if (this.bean.isDebugSecurityPredicateSet()) {
               var2.append("DebugSecurityPredicate");
               var2.append(String.valueOf(this.bean.getDebugSecurityPredicate()));
            }

            if (this.bean.isDebugSecurityProfilerSet()) {
               var2.append("DebugSecurityProfiler");
               var2.append(String.valueOf(this.bean.getDebugSecurityProfiler()));
            }

            if (this.bean.isDebugSecurityRealmSet()) {
               var2.append("DebugSecurityRealm");
               var2.append(String.valueOf(this.bean.getDebugSecurityRealm()));
            }

            if (this.bean.isDebugSecurityRoleMapSet()) {
               var2.append("DebugSecurityRoleMap");
               var2.append(String.valueOf(this.bean.getDebugSecurityRoleMap()));
            }

            if (this.bean.isDebugSecuritySAML2AtnSet()) {
               var2.append("DebugSecuritySAML2Atn");
               var2.append(String.valueOf(this.bean.getDebugSecuritySAML2Atn()));
            }

            if (this.bean.isDebugSecuritySAML2CredMapSet()) {
               var2.append("DebugSecuritySAML2CredMap");
               var2.append(String.valueOf(this.bean.getDebugSecuritySAML2CredMap()));
            }

            if (this.bean.isDebugSecuritySAML2LibSet()) {
               var2.append("DebugSecuritySAML2Lib");
               var2.append(String.valueOf(this.bean.getDebugSecuritySAML2Lib()));
            }

            if (this.bean.isDebugSecuritySAML2ServiceSet()) {
               var2.append("DebugSecuritySAML2Service");
               var2.append(String.valueOf(this.bean.getDebugSecuritySAML2Service()));
            }

            if (this.bean.isDebugSecuritySAMLAtnSet()) {
               var2.append("DebugSecuritySAMLAtn");
               var2.append(String.valueOf(this.bean.getDebugSecuritySAMLAtn()));
            }

            if (this.bean.isDebugSecuritySAMLCredMapSet()) {
               var2.append("DebugSecuritySAMLCredMap");
               var2.append(String.valueOf(this.bean.getDebugSecuritySAMLCredMap()));
            }

            if (this.bean.isDebugSecuritySAMLLibSet()) {
               var2.append("DebugSecuritySAMLLib");
               var2.append(String.valueOf(this.bean.getDebugSecuritySAMLLib()));
            }

            if (this.bean.isDebugSecuritySAMLServiceSet()) {
               var2.append("DebugSecuritySAMLService");
               var2.append(String.valueOf(this.bean.getDebugSecuritySAMLService()));
            }

            if (this.bean.isDebugSecuritySSLSet()) {
               var2.append("DebugSecuritySSL");
               var2.append(String.valueOf(this.bean.getDebugSecuritySSL()));
            }

            if (this.bean.isDebugSecuritySSLEatenSet()) {
               var2.append("DebugSecuritySSLEaten");
               var2.append(String.valueOf(this.bean.getDebugSecuritySSLEaten()));
            }

            if (this.bean.isDebugSecurityServiceSet()) {
               var2.append("DebugSecurityService");
               var2.append(String.valueOf(this.bean.getDebugSecurityService()));
            }

            if (this.bean.isDebugSecurityUserLockoutSet()) {
               var2.append("DebugSecurityUserLockout");
               var2.append(String.valueOf(this.bean.getDebugSecurityUserLockout()));
            }

            if (this.bean.isDebugSelfTuningSet()) {
               var2.append("DebugSelfTuning");
               var2.append(String.valueOf(this.bean.getDebugSelfTuning()));
            }

            if (this.bean.isDebugServerLifeCycleSet()) {
               var2.append("DebugServerLifeCycle");
               var2.append(String.valueOf(this.bean.getDebugServerLifeCycle()));
            }

            if (this.bean.isDebugServerMigrationSet()) {
               var2.append("DebugServerMigration");
               var2.append(String.valueOf(this.bean.getDebugServerMigration()));
            }

            if (this.bean.isDebugServerStartStatisticsSet()) {
               var2.append("DebugServerStartStatistics");
               var2.append(String.valueOf(this.bean.getDebugServerStartStatistics()));
            }

            if (this.bean.isDebugStoreAdminSet()) {
               var2.append("DebugStoreAdmin");
               var2.append(String.valueOf(this.bean.getDebugStoreAdmin()));
            }

            if (this.bean.isDebugStoreIOLogicalSet()) {
               var2.append("DebugStoreIOLogical");
               var2.append(String.valueOf(this.bean.getDebugStoreIOLogical()));
            }

            if (this.bean.isDebugStoreIOLogicalBootSet()) {
               var2.append("DebugStoreIOLogicalBoot");
               var2.append(String.valueOf(this.bean.getDebugStoreIOLogicalBoot()));
            }

            if (this.bean.isDebugStoreIOPhysicalSet()) {
               var2.append("DebugStoreIOPhysical");
               var2.append(String.valueOf(this.bean.getDebugStoreIOPhysical()));
            }

            if (this.bean.isDebugStoreIOPhysicalVerboseSet()) {
               var2.append("DebugStoreIOPhysicalVerbose");
               var2.append(String.valueOf(this.bean.getDebugStoreIOPhysicalVerbose()));
            }

            if (this.bean.isDebugStoreXASet()) {
               var2.append("DebugStoreXA");
               var2.append(String.valueOf(this.bean.getDebugStoreXA()));
            }

            if (this.bean.isDebugStoreXAVerboseSet()) {
               var2.append("DebugStoreXAVerbose");
               var2.append(String.valueOf(this.bean.getDebugStoreXAVerbose()));
            }

            if (this.bean.isDebugTunnelingConnectionSet()) {
               var2.append("DebugTunnelingConnection");
               var2.append(String.valueOf(this.bean.getDebugTunnelingConnection()));
            }

            if (this.bean.isDebugTunnelingConnectionTimeoutSet()) {
               var2.append("DebugTunnelingConnectionTimeout");
               var2.append(String.valueOf(this.bean.getDebugTunnelingConnectionTimeout()));
            }

            if (this.bean.isDebugURLResolutionSet()) {
               var2.append("DebugURLResolution");
               var2.append(String.valueOf(this.bean.getDebugURLResolution()));
            }

            if (this.bean.isDebugWANReplicationDetailsSet()) {
               var2.append("DebugWANReplicationDetails");
               var2.append(String.valueOf(this.bean.getDebugWANReplicationDetails()));
            }

            if (this.bean.isDebugWTCConfigSet()) {
               var2.append("DebugWTCConfig");
               var2.append(String.valueOf(this.bean.getDebugWTCConfig()));
            }

            if (this.bean.isDebugWTCCorbaExSet()) {
               var2.append("DebugWTCCorbaEx");
               var2.append(String.valueOf(this.bean.getDebugWTCCorbaEx()));
            }

            if (this.bean.isDebugWTCGwtExSet()) {
               var2.append("DebugWTCGwtEx");
               var2.append(String.valueOf(this.bean.getDebugWTCGwtEx()));
            }

            if (this.bean.isDebugWTCJatmiExSet()) {
               var2.append("DebugWTCJatmiEx");
               var2.append(String.valueOf(this.bean.getDebugWTCJatmiEx()));
            }

            if (this.bean.isDebugWTCTDomPduSet()) {
               var2.append("DebugWTCTDomPdu");
               var2.append(String.valueOf(this.bean.getDebugWTCTDomPdu()));
            }

            if (this.bean.isDebugWTCUDataSet()) {
               var2.append("DebugWTCUData");
               var2.append(String.valueOf(this.bean.getDebugWTCUData()));
            }

            if (this.bean.isDebugWTCtBridgeExSet()) {
               var2.append("DebugWTCtBridgeEx");
               var2.append(String.valueOf(this.bean.getDebugWTCtBridgeEx()));
            }

            if (this.bean.isDebugWebAppIdentityAssertionSet()) {
               var2.append("DebugWebAppIdentityAssertion");
               var2.append(String.valueOf(this.bean.getDebugWebAppIdentityAssertion()));
            }

            if (this.bean.isDebugWebAppModuleSet()) {
               var2.append("DebugWebAppModule");
               var2.append(String.valueOf(this.bean.getDebugWebAppModule()));
            }

            if (this.bean.isDebugWebAppSecuritySet()) {
               var2.append("DebugWebAppSecurity");
               var2.append(String.valueOf(this.bean.getDebugWebAppSecurity()));
            }

            if (this.bean.isDebugXMLEntityCacheDebugLevelSet()) {
               var2.append("DebugXMLEntityCacheDebugLevel");
               var2.append(String.valueOf(this.bean.getDebugXMLEntityCacheDebugLevel()));
            }

            if (this.bean.isDebugXMLEntityCacheDebugNameSet()) {
               var2.append("DebugXMLEntityCacheDebugName");
               var2.append(String.valueOf(this.bean.getDebugXMLEntityCacheDebugName()));
            }

            if (this.bean.isDebugXMLEntityCacheIncludeClassSet()) {
               var2.append("DebugXMLEntityCacheIncludeClass");
               var2.append(String.valueOf(this.bean.getDebugXMLEntityCacheIncludeClass()));
            }

            if (this.bean.isDebugXMLEntityCacheIncludeLocationSet()) {
               var2.append("DebugXMLEntityCacheIncludeLocation");
               var2.append(String.valueOf(this.bean.getDebugXMLEntityCacheIncludeLocation()));
            }

            if (this.bean.isDebugXMLEntityCacheIncludeNameSet()) {
               var2.append("DebugXMLEntityCacheIncludeName");
               var2.append(String.valueOf(this.bean.getDebugXMLEntityCacheIncludeName()));
            }

            if (this.bean.isDebugXMLEntityCacheIncludeTimeSet()) {
               var2.append("DebugXMLEntityCacheIncludeTime");
               var2.append(String.valueOf(this.bean.getDebugXMLEntityCacheIncludeTime()));
            }

            if (this.bean.isDebugXMLEntityCacheOutputStreamSet()) {
               var2.append("DebugXMLEntityCacheOutputStream");
               var2.append(String.valueOf(this.bean.getDebugXMLEntityCacheOutputStream()));
            }

            if (this.bean.isDebugXMLEntityCacheUseShortClassSet()) {
               var2.append("DebugXMLEntityCacheUseShortClass");
               var2.append(String.valueOf(this.bean.getDebugXMLEntityCacheUseShortClass()));
            }

            if (this.bean.isDebugXMLRegistryDebugLevelSet()) {
               var2.append("DebugXMLRegistryDebugLevel");
               var2.append(String.valueOf(this.bean.getDebugXMLRegistryDebugLevel()));
            }

            if (this.bean.isDebugXMLRegistryDebugNameSet()) {
               var2.append("DebugXMLRegistryDebugName");
               var2.append(String.valueOf(this.bean.getDebugXMLRegistryDebugName()));
            }

            if (this.bean.isDebugXMLRegistryIncludeClassSet()) {
               var2.append("DebugXMLRegistryIncludeClass");
               var2.append(String.valueOf(this.bean.getDebugXMLRegistryIncludeClass()));
            }

            if (this.bean.isDebugXMLRegistryIncludeLocationSet()) {
               var2.append("DebugXMLRegistryIncludeLocation");
               var2.append(String.valueOf(this.bean.getDebugXMLRegistryIncludeLocation()));
            }

            if (this.bean.isDebugXMLRegistryIncludeNameSet()) {
               var2.append("DebugXMLRegistryIncludeName");
               var2.append(String.valueOf(this.bean.getDebugXMLRegistryIncludeName()));
            }

            if (this.bean.isDebugXMLRegistryIncludeTimeSet()) {
               var2.append("DebugXMLRegistryIncludeTime");
               var2.append(String.valueOf(this.bean.getDebugXMLRegistryIncludeTime()));
            }

            if (this.bean.isDebugXMLRegistryOutputStreamSet()) {
               var2.append("DebugXMLRegistryOutputStream");
               var2.append(String.valueOf(this.bean.getDebugXMLRegistryOutputStream()));
            }

            if (this.bean.isDebugXMLRegistryUseShortClassSet()) {
               var2.append("DebugXMLRegistryUseShortClass");
               var2.append(String.valueOf(this.bean.getDebugXMLRegistryUseShortClass()));
            }

            if (this.bean.isDefaultStoreSet()) {
               var2.append("DefaultStore");
               var2.append(String.valueOf(this.bean.getDefaultStore()));
            }

            if (this.bean.isDiagnosticContextDebugModeSet()) {
               var2.append("DiagnosticContextDebugMode");
               var2.append(String.valueOf(this.bean.getDiagnosticContextDebugMode()));
            }

            if (this.bean.isListenThreadDebugSet()) {
               var2.append("ListenThreadDebug");
               var2.append(String.valueOf(this.bean.getListenThreadDebug()));
            }

            if (this.bean.isMagicThreadDumpBackToSocketSet()) {
               var2.append("MagicThreadDumpBackToSocket");
               var2.append(String.valueOf(this.bean.getMagicThreadDumpBackToSocket()));
            }

            if (this.bean.isMagicThreadDumpFileSet()) {
               var2.append("MagicThreadDumpFile");
               var2.append(String.valueOf(this.bean.getMagicThreadDumpFile()));
            }

            if (this.bean.isMagicThreadDumpHostSet()) {
               var2.append("MagicThreadDumpHost");
               var2.append(String.valueOf(this.bean.getMagicThreadDumpHost()));
            }

            if (this.bean.isMasterDeployerSet()) {
               var2.append("MasterDeployer");
               var2.append(String.valueOf(this.bean.getMasterDeployer()));
            }

            if (this.bean.isRedefiningClassLoaderSet()) {
               var2.append("RedefiningClassLoader");
               var2.append(String.valueOf(this.bean.getRedefiningClassLoader()));
            }

            if (this.bean.isServerSet()) {
               var2.append("Server");
               var2.append(String.valueOf(this.bean.getServer()));
            }

            if (this.bean.isSlaveDeployerSet()) {
               var2.append("SlaveDeployer");
               var2.append(String.valueOf(this.bean.getSlaveDeployer()));
            }

            if (this.bean.isWebModuleSet()) {
               var2.append("WebModule");
               var2.append(String.valueOf(this.bean.getWebModule()));
            }

            if (this.bean.isMagicThreadDumpEnabledSet()) {
               var2.append("MagicThreadDumpEnabled");
               var2.append(String.valueOf(this.bean.isMagicThreadDumpEnabled()));
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
            ServerDebugMBeanImpl var2 = (ServerDebugMBeanImpl)var1;
            this.computeDiff("ApplicationContainer", this.bean.getApplicationContainer(), var2.getApplicationContainer(), false);
            this.computeDiff("BugReportServiceWsdlUrl", this.bean.getBugReportServiceWsdlUrl(), var2.getBugReportServiceWsdlUrl(), false);
            this.computeDiff("ClassChangeNotifier", this.bean.getClassChangeNotifier(), var2.getClassChangeNotifier(), true);
            this.computeDiff("ClassFinder", this.bean.getClassFinder(), var2.getClassFinder(), false);
            this.computeDiff("ClassLoader", this.bean.getClassLoader(), var2.getClassLoader(), true);
            this.computeDiff("ClassLoaderVerbose", this.bean.getClassLoaderVerbose(), var2.getClassLoaderVerbose(), true);
            this.computeDiff("ClassloaderWebApp", this.bean.getClassloaderWebApp(), var2.getClassloaderWebApp(), true);
            this.computeDiff("ClasspathServlet", this.bean.getClasspathServlet(), var2.getClasspathServlet(), false);
            this.computeDiff("DebugAppContainer", this.bean.getDebugAppContainer(), var2.getDebugAppContainer(), true);
            this.computeDiff("DebugAsyncQueue", this.bean.getDebugAsyncQueue(), var2.getDebugAsyncQueue(), true);
            this.computeDiff("DebugBootstrapServlet", this.bean.getDebugBootstrapServlet(), var2.getDebugBootstrapServlet(), true);
            this.computeDiff("DebugCertRevocCheck", this.bean.getDebugCertRevocCheck(), var2.getDebugCertRevocCheck(), true);
            this.computeDiff("DebugClassRedef", this.bean.getDebugClassRedef(), var2.getDebugClassRedef(), true);
            this.computeDiff("DebugClassSize", this.bean.getDebugClassSize(), var2.getDebugClassSize(), true);
            this.computeDiff("DebugCluster", this.bean.getDebugCluster(), var2.getDebugCluster(), true);
            this.computeDiff("DebugClusterAnnouncements", this.bean.getDebugClusterAnnouncements(), var2.getDebugClusterAnnouncements(), true);
            this.computeDiff("DebugClusterFragments", this.bean.getDebugClusterFragments(), var2.getDebugClusterFragments(), false);
            this.computeDiff("DebugClusterHeartbeats", this.bean.getDebugClusterHeartbeats(), var2.getDebugClusterHeartbeats(), true);
            this.computeDiff("DebugConfigurationEdit", this.bean.getDebugConfigurationEdit(), var2.getDebugConfigurationEdit(), true);
            this.computeDiff("DebugConfigurationRuntime", this.bean.getDebugConfigurationRuntime(), var2.getDebugConfigurationRuntime(), true);
            this.computeDiff("DebugConnectorService", this.bean.getDebugConnectorService(), var2.getDebugConnectorService(), true);
            this.computeDiff("DebugConsensusLeasing", this.bean.getDebugConsensusLeasing(), var2.getDebugConsensusLeasing(), true);
            this.computeDiff("DebugDRSCalls", this.bean.getDebugDRSCalls(), var2.getDebugDRSCalls(), false);
            this.computeDiff("DebugDRSHeartbeats", this.bean.getDebugDRSHeartbeats(), var2.getDebugDRSHeartbeats(), false);
            this.computeDiff("DebugDRSMessages", this.bean.getDebugDRSMessages(), var2.getDebugDRSMessages(), false);
            this.computeDiff("DebugDRSQueues", this.bean.getDebugDRSQueues(), var2.getDebugDRSQueues(), false);
            this.computeDiff("DebugDRSStateTransitions", this.bean.getDebugDRSStateTransitions(), var2.getDebugDRSStateTransitions(), false);
            this.computeDiff("DebugDRSUpdateStatus", this.bean.getDebugDRSUpdateStatus(), var2.getDebugDRSUpdateStatus(), false);
            this.computeDiff("DebugDeploy", this.bean.getDebugDeploy(), var2.getDebugDeploy(), true);
            this.computeDiff("DebugDeployment", this.bean.getDebugDeployment(), var2.getDebugDeployment(), true);
            this.computeDiff("DebugDeploymentService", this.bean.getDebugDeploymentService(), var2.getDebugDeploymentService(), true);
            this.computeDiff("DebugDeploymentServiceInternal", this.bean.getDebugDeploymentServiceInternal(), var2.getDebugDeploymentServiceInternal(), true);
            this.computeDiff("DebugDeploymentServiceStatusUpdates", this.bean.getDebugDeploymentServiceStatusUpdates(), var2.getDebugDeploymentServiceStatusUpdates(), true);
            this.computeDiff("DebugDeploymentServiceTransport", this.bean.getDebugDeploymentServiceTransport(), var2.getDebugDeploymentServiceTransport(), true);
            this.computeDiff("DebugDeploymentServiceTransportHttp", this.bean.getDebugDeploymentServiceTransportHttp(), var2.getDebugDeploymentServiceTransportHttp(), true);
            this.computeDiff("DebugDescriptor", this.bean.getDebugDescriptor(), var2.getDebugDescriptor(), true);
            this.computeDiff("DebugDiagnosticAccessor", this.bean.getDebugDiagnosticAccessor(), var2.getDebugDiagnosticAccessor(), true);
            this.computeDiff("DebugDiagnosticArchive", this.bean.getDebugDiagnosticArchive(), var2.getDebugDiagnosticArchive(), true);
            this.computeDiff("DebugDiagnosticArchiveRetirement", this.bean.getDebugDiagnosticArchiveRetirement(), var2.getDebugDiagnosticArchiveRetirement(), true);
            this.computeDiff("DebugDiagnosticCollections", this.bean.getDebugDiagnosticCollections(), var2.getDebugDiagnosticCollections(), true);
            this.computeDiff("DebugDiagnosticContext", this.bean.getDebugDiagnosticContext(), var2.getDebugDiagnosticContext(), true);
            this.computeDiff("DebugDiagnosticDataGathering", this.bean.getDebugDiagnosticDataGathering(), var2.getDebugDiagnosticDataGathering(), true);
            this.computeDiff("DebugDiagnosticFileArchive", this.bean.getDebugDiagnosticFileArchive(), var2.getDebugDiagnosticFileArchive(), true);
            this.computeDiff("DebugDiagnosticImage", this.bean.getDebugDiagnosticImage(), var2.getDebugDiagnosticImage(), true);
            this.computeDiff("DebugDiagnosticInstrumentation", this.bean.getDebugDiagnosticInstrumentation(), var2.getDebugDiagnosticInstrumentation(), true);
            this.computeDiff("DebugDiagnosticInstrumentationActions", this.bean.getDebugDiagnosticInstrumentationActions(), var2.getDebugDiagnosticInstrumentationActions(), true);
            this.computeDiff("DebugDiagnosticInstrumentationConfig", this.bean.getDebugDiagnosticInstrumentationConfig(), var2.getDebugDiagnosticInstrumentationConfig(), true);
            this.computeDiff("DebugDiagnosticInstrumentationEvents", this.bean.getDebugDiagnosticInstrumentationEvents(), var2.getDebugDiagnosticInstrumentationEvents(), true);
            this.computeDiff("DebugDiagnosticInstrumentationWeaving", this.bean.getDebugDiagnosticInstrumentationWeaving(), var2.getDebugDiagnosticInstrumentationWeaving(), true);
            this.computeDiff("DebugDiagnosticInstrumentationWeavingMatches", this.bean.getDebugDiagnosticInstrumentationWeavingMatches(), var2.getDebugDiagnosticInstrumentationWeavingMatches(), true);
            this.computeDiff("DebugDiagnosticJdbcArchive", this.bean.getDebugDiagnosticJdbcArchive(), var2.getDebugDiagnosticJdbcArchive(), true);
            this.computeDiff("DebugDiagnosticLifecycleHandlers", this.bean.getDebugDiagnosticLifecycleHandlers(), var2.getDebugDiagnosticLifecycleHandlers(), true);
            this.computeDiff("DebugDiagnosticQuery", this.bean.getDebugDiagnosticQuery(), var2.getDebugDiagnosticQuery(), true);
            this.computeDiff("DebugDiagnosticWatch", this.bean.getDebugDiagnosticWatch(), var2.getDebugDiagnosticWatch(), true);
            this.computeDiff("DebugDiagnosticWlstoreArchive", this.bean.getDebugDiagnosticWlstoreArchive(), var2.getDebugDiagnosticWlstoreArchive(), true);
            this.computeDiff("DebugDiagnosticsHarvester", this.bean.getDebugDiagnosticsHarvester(), var2.getDebugDiagnosticsHarvester(), true);
            this.computeDiff("DebugDiagnosticsHarvesterData", this.bean.getDebugDiagnosticsHarvesterData(), var2.getDebugDiagnosticsHarvesterData(), true);
            this.computeDiff("DebugDiagnosticsHarvesterMBeanPlugin", this.bean.getDebugDiagnosticsHarvesterMBeanPlugin(), var2.getDebugDiagnosticsHarvesterMBeanPlugin(), true);
            this.computeDiff("DebugDiagnosticsHarvesterTreeBeanPlugin", this.bean.getDebugDiagnosticsHarvesterTreeBeanPlugin(), var2.getDebugDiagnosticsHarvesterTreeBeanPlugin(), true);
            this.computeDiff("DebugDiagnosticsModule", this.bean.getDebugDiagnosticsModule(), var2.getDebugDiagnosticsModule(), true);
            this.computeDiff("DebugDomainLogHandler", this.bean.getDebugDomainLogHandler(), var2.getDebugDomainLogHandler(), true);
            this.computeDiff("DebugEjbCaching", this.bean.getDebugEjbCaching(), var2.getDebugEjbCaching(), true);
            this.computeDiff("DebugEjbCmpDeployment", this.bean.getDebugEjbCmpDeployment(), var2.getDebugEjbCmpDeployment(), true);
            this.computeDiff("DebugEjbCmpRuntime", this.bean.getDebugEjbCmpRuntime(), var2.getDebugEjbCmpRuntime(), true);
            this.computeDiff("DebugEjbCompilation", this.bean.getDebugEjbCompilation(), var2.getDebugEjbCompilation(), true);
            this.computeDiff("DebugEjbDeployment", this.bean.getDebugEjbDeployment(), var2.getDebugEjbDeployment(), true);
            this.computeDiff("DebugEjbInvoke", this.bean.getDebugEjbInvoke(), var2.getDebugEjbInvoke(), true);
            this.computeDiff("DebugEjbLocking", this.bean.getDebugEjbLocking(), var2.getDebugEjbLocking(), true);
            this.computeDiff("DebugEjbMdbConnection", this.bean.getDebugEjbMdbConnection(), var2.getDebugEjbMdbConnection(), true);
            this.computeDiff("DebugEjbPooling", this.bean.getDebugEjbPooling(), var2.getDebugEjbPooling(), true);
            this.computeDiff("DebugEjbSecurity", this.bean.getDebugEjbSecurity(), var2.getDebugEjbSecurity(), true);
            this.computeDiff("DebugEjbSwapping", this.bean.getDebugEjbSwapping(), var2.getDebugEjbSwapping(), true);
            this.computeDiff("DebugEjbTimers", this.bean.getDebugEjbTimers(), var2.getDebugEjbTimers(), true);
            this.computeDiff("DebugEmbeddedLDAP", this.bean.getDebugEmbeddedLDAP(), var2.getDebugEmbeddedLDAP(), false);
            this.computeDiff("DebugEmbeddedLDAPLogLevel", this.bean.getDebugEmbeddedLDAPLogLevel(), var2.getDebugEmbeddedLDAPLogLevel(), false);
            this.computeDiff("DebugEmbeddedLDAPLogToConsole", this.bean.getDebugEmbeddedLDAPLogToConsole(), var2.getDebugEmbeddedLDAPLogToConsole(), false);
            this.computeDiff("DebugEmbeddedLDAPWriteOverrideProps", this.bean.getDebugEmbeddedLDAPWriteOverrideProps(), var2.getDebugEmbeddedLDAPWriteOverrideProps(), false);
            this.computeDiff("DebugEventManager", this.bean.getDebugEventManager(), var2.getDebugEventManager(), false);
            this.computeDiff("DebugFileDistributionServlet", this.bean.getDebugFileDistributionServlet(), var2.getDebugFileDistributionServlet(), true);
            this.computeDiff("DebugHttp", this.bean.getDebugHttp(), var2.getDebugHttp(), true);
            this.computeDiff("DebugHttpLogging", this.bean.getDebugHttpLogging(), var2.getDebugHttpLogging(), true);
            this.computeDiff("DebugHttpSessions", this.bean.getDebugHttpSessions(), var2.getDebugHttpSessions(), true);
            this.computeDiff("DebugIIOPNaming", this.bean.getDebugIIOPNaming(), var2.getDebugIIOPNaming(), true);
            this.computeDiff("DebugIIOPTunneling", this.bean.getDebugIIOPTunneling(), var2.getDebugIIOPTunneling(), true);
            this.computeDiff("DebugJ2EEManagement", this.bean.getDebugJ2EEManagement(), var2.getDebugJ2EEManagement(), true);
            this.computeDiff("DebugJAXPDebugLevel", this.bean.getDebugJAXPDebugLevel(), var2.getDebugJAXPDebugLevel(), false);
            this.computeDiff("DebugJAXPDebugName", this.bean.getDebugJAXPDebugName(), var2.getDebugJAXPDebugName(), false);
            this.computeDiff("DebugJAXPIncludeClass", this.bean.getDebugJAXPIncludeClass(), var2.getDebugJAXPIncludeClass(), false);
            this.computeDiff("DebugJAXPIncludeLocation", this.bean.getDebugJAXPIncludeLocation(), var2.getDebugJAXPIncludeLocation(), false);
            this.computeDiff("DebugJAXPIncludeName", this.bean.getDebugJAXPIncludeName(), var2.getDebugJAXPIncludeName(), false);
            this.computeDiff("DebugJAXPIncludeTime", this.bean.getDebugJAXPIncludeTime(), var2.getDebugJAXPIncludeTime(), false);
            this.computeDiff("DebugJAXPUseShortClass", this.bean.getDebugJAXPUseShortClass(), var2.getDebugJAXPUseShortClass(), false);
            this.computeDiff("DebugJDBCConn", this.bean.getDebugJDBCConn(), var2.getDebugJDBCConn(), true);
            this.computeDiff("DebugJDBCDriverLogging", this.bean.getDebugJDBCDriverLogging(), var2.getDebugJDBCDriverLogging(), true);
            this.computeDiff("DebugJDBCInternal", this.bean.getDebugJDBCInternal(), var2.getDebugJDBCInternal(), true);
            this.computeDiff("DebugJDBCONS", this.bean.getDebugJDBCONS(), var2.getDebugJDBCONS(), true);
            this.computeDiff("DebugJDBCRAC", this.bean.getDebugJDBCRAC(), var2.getDebugJDBCRAC(), true);
            this.computeDiff("DebugJDBCREPLAY", this.bean.getDebugJDBCREPLAY(), var2.getDebugJDBCREPLAY(), true);
            this.computeDiff("DebugJDBCRMI", this.bean.getDebugJDBCRMI(), var2.getDebugJDBCRMI(), true);
            this.computeDiff("DebugJDBCSQL", this.bean.getDebugJDBCSQL(), var2.getDebugJDBCSQL(), true);
            this.computeDiff("DebugJDBCUCP", this.bean.getDebugJDBCUCP(), var2.getDebugJDBCUCP(), true);
            this.computeDiff("DebugJMSAME", this.bean.getDebugJMSAME(), var2.getDebugJMSAME(), true);
            this.computeDiff("DebugJMSBackEnd", this.bean.getDebugJMSBackEnd(), var2.getDebugJMSBackEnd(), true);
            this.computeDiff("DebugJMSBoot", this.bean.getDebugJMSBoot(), var2.getDebugJMSBoot(), true);
            this.computeDiff("DebugJMSCDS", this.bean.getDebugJMSCDS(), var2.getDebugJMSCDS(), true);
            this.computeDiff("DebugJMSCommon", this.bean.getDebugJMSCommon(), var2.getDebugJMSCommon(), true);
            this.computeDiff("DebugJMSConfig", this.bean.getDebugJMSConfig(), var2.getDebugJMSConfig(), true);
            this.computeDiff("DebugJMSDispatcher", this.bean.getDebugJMSDispatcher(), var2.getDebugJMSDispatcher(), true);
            this.computeDiff("DebugJMSDistTopic", this.bean.getDebugJMSDistTopic(), var2.getDebugJMSDistTopic(), true);
            this.computeDiff("DebugJMSDurableSubscribers", this.bean.getDebugJMSDurableSubscribers(), var2.getDebugJMSDurableSubscribers(), true);
            this.computeDiff("DebugJMSFrontEnd", this.bean.getDebugJMSFrontEnd(), var2.getDebugJMSFrontEnd(), true);
            this.computeDiff("DebugJMSJDBCScavengeOnFlush", this.bean.getDebugJMSJDBCScavengeOnFlush(), var2.getDebugJMSJDBCScavengeOnFlush(), true);
            this.computeDiff("DebugJMSLocking", this.bean.getDebugJMSLocking(), var2.getDebugJMSLocking(), true);
            this.computeDiff("DebugJMSMessagePath", this.bean.getDebugJMSMessagePath(), var2.getDebugJMSMessagePath(), true);
            this.computeDiff("DebugJMSModule", this.bean.getDebugJMSModule(), var2.getDebugJMSModule(), true);
            this.computeDiff("DebugJMSPauseResume", this.bean.getDebugJMSPauseResume(), var2.getDebugJMSPauseResume(), true);
            this.computeDiff("DebugJMSSAF", this.bean.getDebugJMSSAF(), var2.getDebugJMSSAF(), true);
            this.computeDiff("DebugJMSStore", this.bean.getDebugJMSStore(), var2.getDebugJMSStore(), true);
            this.computeDiff("DebugJMST3Server", this.bean.getDebugJMST3Server(), var2.getDebugJMST3Server(), true);
            this.computeDiff("DebugJMSWrappers", this.bean.getDebugJMSWrappers(), var2.getDebugJMSWrappers(), true);
            this.computeDiff("DebugJMSXA", this.bean.getDebugJMSXA(), var2.getDebugJMSXA(), true);
            this.computeDiff("DebugJMX", this.bean.getDebugJMX(), var2.getDebugJMX(), true);
            this.computeDiff("DebugJMXCompatibility", this.bean.getDebugJMXCompatibility(), var2.getDebugJMXCompatibility(), true);
            this.computeDiff("DebugJMXCore", this.bean.getDebugJMXCore(), var2.getDebugJMXCore(), true);
            this.computeDiff("DebugJMXDomain", this.bean.getDebugJMXDomain(), var2.getDebugJMXDomain(), true);
            this.computeDiff("DebugJMXEdit", this.bean.getDebugJMXEdit(), var2.getDebugJMXEdit(), true);
            this.computeDiff("DebugJMXRuntime", this.bean.getDebugJMXRuntime(), var2.getDebugJMXRuntime(), true);
            this.computeDiff("DebugJNDI", this.bean.getDebugJNDI(), var2.getDebugJNDI(), false);
            this.computeDiff("DebugJNDIFactories", this.bean.getDebugJNDIFactories(), var2.getDebugJNDIFactories(), false);
            this.computeDiff("DebugJNDIResolution", this.bean.getDebugJNDIResolution(), var2.getDebugJNDIResolution(), false);
            this.computeDiff("DebugJTA2PC", this.bean.getDebugJTA2PC(), var2.getDebugJTA2PC(), true);
            this.computeDiff("DebugJTA2PCStackTrace", this.bean.getDebugJTA2PCStackTrace(), var2.getDebugJTA2PCStackTrace(), true);
            this.computeDiff("DebugJTAAPI", this.bean.getDebugJTAAPI(), var2.getDebugJTAAPI(), true);
            this.computeDiff("DebugJTAGateway", this.bean.getDebugJTAGateway(), var2.getDebugJTAGateway(), true);
            this.computeDiff("DebugJTAGatewayStackTrace", this.bean.getDebugJTAGatewayStackTrace(), var2.getDebugJTAGatewayStackTrace(), true);
            this.computeDiff("DebugJTAHealth", this.bean.getDebugJTAHealth(), var2.getDebugJTAHealth(), true);
            this.computeDiff("DebugJTAJDBC", this.bean.getDebugJTAJDBC(), var2.getDebugJTAJDBC(), true);
            this.computeDiff("DebugJTALLR", this.bean.getDebugJTALLR(), var2.getDebugJTALLR(), true);
            this.computeDiff("DebugJTALifecycle", this.bean.getDebugJTALifecycle(), var2.getDebugJTALifecycle(), true);
            this.computeDiff("DebugJTAMigration", this.bean.getDebugJTAMigration(), var2.getDebugJTAMigration(), true);
            this.computeDiff("DebugJTANaming", this.bean.getDebugJTANaming(), var2.getDebugJTANaming(), true);
            this.computeDiff("DebugJTANamingStackTrace", this.bean.getDebugJTANamingStackTrace(), var2.getDebugJTANamingStackTrace(), true);
            this.computeDiff("DebugJTANonXA", this.bean.getDebugJTANonXA(), var2.getDebugJTANonXA(), true);
            this.computeDiff("DebugJTAPropagate", this.bean.getDebugJTAPropagate(), var2.getDebugJTAPropagate(), true);
            this.computeDiff("DebugJTARMI", this.bean.getDebugJTARMI(), var2.getDebugJTARMI(), true);
            this.computeDiff("DebugJTARecovery", this.bean.getDebugJTARecovery(), var2.getDebugJTARecovery(), true);
            this.computeDiff("DebugJTARecoveryStackTrace", this.bean.getDebugJTARecoveryStackTrace(), var2.getDebugJTARecoveryStackTrace(), true);
            this.computeDiff("DebugJTAResourceHealth", this.bean.getDebugJTAResourceHealth(), var2.getDebugJTAResourceHealth(), true);
            this.computeDiff("DebugJTAResourceName", this.bean.getDebugJTAResourceName(), var2.getDebugJTAResourceName(), true);
            this.computeDiff("DebugJTATLOG", this.bean.getDebugJTATLOG(), var2.getDebugJTATLOG(), true);
            this.computeDiff("DebugJTATransactionName", this.bean.getDebugJTATransactionName(), var2.getDebugJTATransactionName(), true);
            this.computeDiff("DebugJTAXA", this.bean.getDebugJTAXA(), var2.getDebugJTAXA(), true);
            this.computeDiff("DebugJTAXAStackTrace", this.bean.getDebugJTAXAStackTrace(), var2.getDebugJTAXAStackTrace(), true);
            this.computeDiff("DebugJpaDataCache", this.bean.getDebugJpaDataCache(), var2.getDebugJpaDataCache(), true);
            this.computeDiff("DebugJpaEnhance", this.bean.getDebugJpaEnhance(), var2.getDebugJpaEnhance(), true);
            this.computeDiff("DebugJpaJdbcJdbc", this.bean.getDebugJpaJdbcJdbc(), var2.getDebugJpaJdbcJdbc(), true);
            this.computeDiff("DebugJpaJdbcSchema", this.bean.getDebugJpaJdbcSchema(), var2.getDebugJpaJdbcSchema(), true);
            this.computeDiff("DebugJpaJdbcSql", this.bean.getDebugJpaJdbcSql(), var2.getDebugJpaJdbcSql(), true);
            this.computeDiff("DebugJpaManage", this.bean.getDebugJpaManage(), var2.getDebugJpaManage(), true);
            this.computeDiff("DebugJpaMetaData", this.bean.getDebugJpaMetaData(), var2.getDebugJpaMetaData(), true);
            this.computeDiff("DebugJpaProfile", this.bean.getDebugJpaProfile(), var2.getDebugJpaProfile(), true);
            this.computeDiff("DebugJpaQuery", this.bean.getDebugJpaQuery(), var2.getDebugJpaQuery(), true);
            this.computeDiff("DebugJpaRuntime", this.bean.getDebugJpaRuntime(), var2.getDebugJpaRuntime(), true);
            this.computeDiff("DebugJpaTool", this.bean.getDebugJpaTool(), var2.getDebugJpaTool(), true);
            this.computeDiff("DebugLeaderElection", this.bean.getDebugLeaderElection(), var2.getDebugLeaderElection(), false);
            this.computeDiff("DebugLibraries", this.bean.getDebugLibraries(), var2.getDebugLibraries(), true);
            this.computeDiff("DebugLoggingConfiguration", this.bean.getDebugLoggingConfiguration(), var2.getDebugLoggingConfiguration(), true);
            this.computeDiff("DebugManagementServicesResource", this.bean.getDebugManagementServicesResource(), var2.getDebugManagementServicesResource(), true);
            this.computeDiff("DebugMaskCriterias", this.bean.getDebugMaskCriterias(), var2.getDebugMaskCriterias(), true);
            this.computeDiff("DebugMessagingBridgeDumpToConsole", this.bean.getDebugMessagingBridgeDumpToConsole(), var2.getDebugMessagingBridgeDumpToConsole(), true);
            this.computeDiff("DebugMessagingBridgeDumpToLog", this.bean.getDebugMessagingBridgeDumpToLog(), var2.getDebugMessagingBridgeDumpToLog(), true);
            this.computeDiff("DebugMessagingBridgeRuntime", this.bean.getDebugMessagingBridgeRuntime(), var2.getDebugMessagingBridgeRuntime(), true);
            this.computeDiff("DebugMessagingBridgeRuntimeVerbose", this.bean.getDebugMessagingBridgeRuntimeVerbose(), var2.getDebugMessagingBridgeRuntimeVerbose(), true);
            this.computeDiff("DebugMessagingBridgeStartup", this.bean.getDebugMessagingBridgeStartup(), var2.getDebugMessagingBridgeStartup(), true);
            this.computeDiff("DebugMessagingKernel", this.bean.getDebugMessagingKernel(), var2.getDebugMessagingKernel(), true);
            this.computeDiff("DebugMessagingKernelBoot", this.bean.getDebugMessagingKernelBoot(), var2.getDebugMessagingKernelBoot(), true);
            this.computeDiff("DebugPathSvc", this.bean.getDebugPathSvc(), var2.getDebugPathSvc(), true);
            this.computeDiff("DebugPathSvcVerbose", this.bean.getDebugPathSvcVerbose(), var2.getDebugPathSvcVerbose(), true);
            this.computeDiff("DebugRA", this.bean.getDebugRA(), var2.getDebugRA(), true);
            this.computeDiff("DebugRAClassloader", this.bean.getDebugRAClassloader(), var2.getDebugRAClassloader(), true);
            this.computeDiff("DebugRAConnEvents", this.bean.getDebugRAConnEvents(), var2.getDebugRAConnEvents(), true);
            this.computeDiff("DebugRAConnections", this.bean.getDebugRAConnections(), var2.getDebugRAConnections(), true);
            this.computeDiff("DebugRADeployment", this.bean.getDebugRADeployment(), var2.getDebugRADeployment(), true);
            this.computeDiff("DebugRALifecycle", this.bean.getDebugRALifecycle(), var2.getDebugRALifecycle(), true);
            this.computeDiff("DebugRALocalOut", this.bean.getDebugRALocalOut(), var2.getDebugRALocalOut(), true);
            this.computeDiff("DebugRAParsing", this.bean.getDebugRAParsing(), var2.getDebugRAParsing(), true);
            this.computeDiff("DebugRAPoolVerbose", this.bean.getDebugRAPoolVerbose(), var2.getDebugRAPoolVerbose(), true);
            this.computeDiff("DebugRAPooling", this.bean.getDebugRAPooling(), var2.getDebugRAPooling(), true);
            this.computeDiff("DebugRASecurityCtx", this.bean.getDebugRASecurityCtx(), var2.getDebugRASecurityCtx(), true);
            this.computeDiff("DebugRAWork", this.bean.getDebugRAWork(), var2.getDebugRAWork(), true);
            this.computeDiff("DebugRAWorkEvents", this.bean.getDebugRAWorkEvents(), var2.getDebugRAWorkEvents(), true);
            this.computeDiff("DebugRAXAin", this.bean.getDebugRAXAin(), var2.getDebugRAXAin(), true);
            this.computeDiff("DebugRAXAout", this.bean.getDebugRAXAout(), var2.getDebugRAXAout(), true);
            this.computeDiff("DebugRAXAwork", this.bean.getDebugRAXAwork(), var2.getDebugRAXAwork(), true);
            this.computeDiff("DebugReplication", this.bean.getDebugReplication(), var2.getDebugReplication(), true);
            this.computeDiff("DebugReplicationDetails", this.bean.getDebugReplicationDetails(), var2.getDebugReplicationDetails(), true);
            this.computeDiff("DebugSAFAdmin", this.bean.getDebugSAFAdmin(), var2.getDebugSAFAdmin(), true);
            this.computeDiff("DebugSAFLifeCycle", this.bean.getDebugSAFLifeCycle(), var2.getDebugSAFLifeCycle(), true);
            this.computeDiff("DebugSAFManager", this.bean.getDebugSAFManager(), var2.getDebugSAFManager(), true);
            this.computeDiff("DebugSAFMessagePath", this.bean.getDebugSAFMessagePath(), var2.getDebugSAFMessagePath(), true);
            this.computeDiff("DebugSAFReceivingAgent", this.bean.getDebugSAFReceivingAgent(), var2.getDebugSAFReceivingAgent(), true);
            this.computeDiff("DebugSAFSendingAgent", this.bean.getDebugSAFSendingAgent(), var2.getDebugSAFSendingAgent(), true);
            this.computeDiff("DebugSAFStore", this.bean.getDebugSAFStore(), var2.getDebugSAFStore(), true);
            this.computeDiff("DebugSAFTransport", this.bean.getDebugSAFTransport(), var2.getDebugSAFTransport(), true);
            this.computeDiff("DebugSAFVerbose", this.bean.getDebugSAFVerbose(), var2.getDebugSAFVerbose(), true);
            this.computeDiff("DebugSNMPAgent", this.bean.getDebugSNMPAgent(), var2.getDebugSNMPAgent(), true);
            this.computeDiff("DebugSNMPExtensionProvider", this.bean.getDebugSNMPExtensionProvider(), var2.getDebugSNMPExtensionProvider(), true);
            this.computeDiff("DebugSNMPProtocolTCP", this.bean.getDebugSNMPProtocolTCP(), var2.getDebugSNMPProtocolTCP(), true);
            this.computeDiff("DebugSNMPToolkit", this.bean.getDebugSNMPToolkit(), var2.getDebugSNMPToolkit(), true);
            this.computeDiff("DebugScaContainer", this.bean.getDebugScaContainer(), var2.getDebugScaContainer(), true);
            this.computeDiff("DebugSecurity", this.bean.getDebugSecurity(), var2.getDebugSecurity(), true);
            this.computeDiff("DebugSecurityAdjudicator", this.bean.getDebugSecurityAdjudicator(), var2.getDebugSecurityAdjudicator(), true);
            this.computeDiff("DebugSecurityAtn", this.bean.getDebugSecurityAtn(), var2.getDebugSecurityAtn(), true);
            this.computeDiff("DebugSecurityAtz", this.bean.getDebugSecurityAtz(), var2.getDebugSecurityAtz(), true);
            this.computeDiff("DebugSecurityAuditor", this.bean.getDebugSecurityAuditor(), var2.getDebugSecurityAuditor(), true);
            this.computeDiff("DebugSecurityCertPath", this.bean.getDebugSecurityCertPath(), var2.getDebugSecurityCertPath(), true);
            this.computeDiff("DebugSecurityCredMap", this.bean.getDebugSecurityCredMap(), var2.getDebugSecurityCredMap(), true);
            this.computeDiff("DebugSecurityEEngine", this.bean.getDebugSecurityEEngine(), var2.getDebugSecurityEEngine(), true);
            this.computeDiff("DebugSecurityEncryptionService", this.bean.getDebugSecurityEncryptionService(), var2.getDebugSecurityEncryptionService(), true);
            this.computeDiff("DebugSecurityJACC", this.bean.getDebugSecurityJACC(), var2.getDebugSecurityJACC(), true);
            this.computeDiff("DebugSecurityJACCNonPolicy", this.bean.getDebugSecurityJACCNonPolicy(), var2.getDebugSecurityJACCNonPolicy(), true);
            this.computeDiff("DebugSecurityJACCPolicy", this.bean.getDebugSecurityJACCPolicy(), var2.getDebugSecurityJACCPolicy(), true);
            this.computeDiff("DebugSecurityKeyStore", this.bean.getDebugSecurityKeyStore(), var2.getDebugSecurityKeyStore(), true);
            this.computeDiff("DebugSecurityPasswordPolicy", this.bean.getDebugSecurityPasswordPolicy(), var2.getDebugSecurityPasswordPolicy(), true);
            this.computeDiff("DebugSecurityPredicate", this.bean.getDebugSecurityPredicate(), var2.getDebugSecurityPredicate(), true);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("DebugSecurityProfiler", this.bean.getDebugSecurityProfiler(), var2.getDebugSecurityProfiler(), false);
            }

            this.computeDiff("DebugSecurityRealm", this.bean.getDebugSecurityRealm(), var2.getDebugSecurityRealm(), true);
            this.computeDiff("DebugSecurityRoleMap", this.bean.getDebugSecurityRoleMap(), var2.getDebugSecurityRoleMap(), true);
            this.computeDiff("DebugSecuritySAML2Atn", this.bean.getDebugSecuritySAML2Atn(), var2.getDebugSecuritySAML2Atn(), true);
            this.computeDiff("DebugSecuritySAML2CredMap", this.bean.getDebugSecuritySAML2CredMap(), var2.getDebugSecuritySAML2CredMap(), true);
            this.computeDiff("DebugSecuritySAML2Lib", this.bean.getDebugSecuritySAML2Lib(), var2.getDebugSecuritySAML2Lib(), true);
            this.computeDiff("DebugSecuritySAML2Service", this.bean.getDebugSecuritySAML2Service(), var2.getDebugSecuritySAML2Service(), true);
            this.computeDiff("DebugSecuritySAMLAtn", this.bean.getDebugSecuritySAMLAtn(), var2.getDebugSecuritySAMLAtn(), true);
            this.computeDiff("DebugSecuritySAMLCredMap", this.bean.getDebugSecuritySAMLCredMap(), var2.getDebugSecuritySAMLCredMap(), true);
            this.computeDiff("DebugSecuritySAMLLib", this.bean.getDebugSecuritySAMLLib(), var2.getDebugSecuritySAMLLib(), true);
            this.computeDiff("DebugSecuritySAMLService", this.bean.getDebugSecuritySAMLService(), var2.getDebugSecuritySAMLService(), true);
            this.computeDiff("DebugSecuritySSL", this.bean.getDebugSecuritySSL(), var2.getDebugSecuritySSL(), true);
            this.computeDiff("DebugSecuritySSLEaten", this.bean.getDebugSecuritySSLEaten(), var2.getDebugSecuritySSLEaten(), true);
            this.computeDiff("DebugSecurityService", this.bean.getDebugSecurityService(), var2.getDebugSecurityService(), true);
            this.computeDiff("DebugSecurityUserLockout", this.bean.getDebugSecurityUserLockout(), var2.getDebugSecurityUserLockout(), true);
            this.computeDiff("DebugSelfTuning", this.bean.getDebugSelfTuning(), var2.getDebugSelfTuning(), true);
            this.computeDiff("DebugServerLifeCycle", this.bean.getDebugServerLifeCycle(), var2.getDebugServerLifeCycle(), true);
            this.computeDiff("DebugServerMigration", this.bean.getDebugServerMigration(), var2.getDebugServerMigration(), true);
            this.computeDiff("DebugServerStartStatistics", this.bean.getDebugServerStartStatistics(), var2.getDebugServerStartStatistics(), true);
            this.computeDiff("DebugStoreAdmin", this.bean.getDebugStoreAdmin(), var2.getDebugStoreAdmin(), true);
            this.computeDiff("DebugStoreIOLogical", this.bean.getDebugStoreIOLogical(), var2.getDebugStoreIOLogical(), true);
            this.computeDiff("DebugStoreIOLogicalBoot", this.bean.getDebugStoreIOLogicalBoot(), var2.getDebugStoreIOLogicalBoot(), true);
            this.computeDiff("DebugStoreIOPhysical", this.bean.getDebugStoreIOPhysical(), var2.getDebugStoreIOPhysical(), true);
            this.computeDiff("DebugStoreIOPhysicalVerbose", this.bean.getDebugStoreIOPhysicalVerbose(), var2.getDebugStoreIOPhysicalVerbose(), true);
            this.computeDiff("DebugStoreXA", this.bean.getDebugStoreXA(), var2.getDebugStoreXA(), true);
            this.computeDiff("DebugStoreXAVerbose", this.bean.getDebugStoreXAVerbose(), var2.getDebugStoreXAVerbose(), true);
            this.computeDiff("DebugTunnelingConnection", this.bean.getDebugTunnelingConnection(), var2.getDebugTunnelingConnection(), false);
            this.computeDiff("DebugTunnelingConnectionTimeout", this.bean.getDebugTunnelingConnectionTimeout(), var2.getDebugTunnelingConnectionTimeout(), false);
            this.computeDiff("DebugURLResolution", this.bean.getDebugURLResolution(), var2.getDebugURLResolution(), true);
            this.computeDiff("DebugWANReplicationDetails", this.bean.getDebugWANReplicationDetails(), var2.getDebugWANReplicationDetails(), true);
            this.computeDiff("DebugWTCConfig", this.bean.getDebugWTCConfig(), var2.getDebugWTCConfig(), true);
            this.computeDiff("DebugWTCCorbaEx", this.bean.getDebugWTCCorbaEx(), var2.getDebugWTCCorbaEx(), true);
            this.computeDiff("DebugWTCGwtEx", this.bean.getDebugWTCGwtEx(), var2.getDebugWTCGwtEx(), true);
            this.computeDiff("DebugWTCJatmiEx", this.bean.getDebugWTCJatmiEx(), var2.getDebugWTCJatmiEx(), true);
            this.computeDiff("DebugWTCTDomPdu", this.bean.getDebugWTCTDomPdu(), var2.getDebugWTCTDomPdu(), true);
            this.computeDiff("DebugWTCUData", this.bean.getDebugWTCUData(), var2.getDebugWTCUData(), true);
            this.computeDiff("DebugWTCtBridgeEx", this.bean.getDebugWTCtBridgeEx(), var2.getDebugWTCtBridgeEx(), true);
            this.computeDiff("DebugWebAppIdentityAssertion", this.bean.getDebugWebAppIdentityAssertion(), var2.getDebugWebAppIdentityAssertion(), true);
            this.computeDiff("DebugWebAppModule", this.bean.getDebugWebAppModule(), var2.getDebugWebAppModule(), true);
            this.computeDiff("DebugWebAppSecurity", this.bean.getDebugWebAppSecurity(), var2.getDebugWebAppSecurity(), true);
            this.computeDiff("DebugXMLEntityCacheDebugLevel", this.bean.getDebugXMLEntityCacheDebugLevel(), var2.getDebugXMLEntityCacheDebugLevel(), false);
            this.computeDiff("DebugXMLEntityCacheDebugName", this.bean.getDebugXMLEntityCacheDebugName(), var2.getDebugXMLEntityCacheDebugName(), false);
            this.computeDiff("DebugXMLEntityCacheIncludeClass", this.bean.getDebugXMLEntityCacheIncludeClass(), var2.getDebugXMLEntityCacheIncludeClass(), false);
            this.computeDiff("DebugXMLEntityCacheIncludeLocation", this.bean.getDebugXMLEntityCacheIncludeLocation(), var2.getDebugXMLEntityCacheIncludeLocation(), false);
            this.computeDiff("DebugXMLEntityCacheIncludeName", this.bean.getDebugXMLEntityCacheIncludeName(), var2.getDebugXMLEntityCacheIncludeName(), false);
            this.computeDiff("DebugXMLEntityCacheIncludeTime", this.bean.getDebugXMLEntityCacheIncludeTime(), var2.getDebugXMLEntityCacheIncludeTime(), false);
            this.computeDiff("DebugXMLEntityCacheUseShortClass", this.bean.getDebugXMLEntityCacheUseShortClass(), var2.getDebugXMLEntityCacheUseShortClass(), false);
            this.computeDiff("DebugXMLRegistryDebugLevel", this.bean.getDebugXMLRegistryDebugLevel(), var2.getDebugXMLRegistryDebugLevel(), false);
            this.computeDiff("DebugXMLRegistryDebugName", this.bean.getDebugXMLRegistryDebugName(), var2.getDebugXMLRegistryDebugName(), false);
            this.computeDiff("DebugXMLRegistryIncludeClass", this.bean.getDebugXMLRegistryIncludeClass(), var2.getDebugXMLRegistryIncludeClass(), false);
            this.computeDiff("DebugXMLRegistryIncludeLocation", this.bean.getDebugXMLRegistryIncludeLocation(), var2.getDebugXMLRegistryIncludeLocation(), false);
            this.computeDiff("DebugXMLRegistryIncludeName", this.bean.getDebugXMLRegistryIncludeName(), var2.getDebugXMLRegistryIncludeName(), false);
            this.computeDiff("DebugXMLRegistryIncludeTime", this.bean.getDebugXMLRegistryIncludeTime(), var2.getDebugXMLRegistryIncludeTime(), false);
            this.computeDiff("DebugXMLRegistryUseShortClass", this.bean.getDebugXMLRegistryUseShortClass(), var2.getDebugXMLRegistryUseShortClass(), false);
            this.computeDiff("DefaultStore", this.bean.getDefaultStore(), var2.getDefaultStore(), true);
            this.computeDiff("DiagnosticContextDebugMode", this.bean.getDiagnosticContextDebugMode(), var2.getDiagnosticContextDebugMode(), true);
            this.computeDiff("ListenThreadDebug", this.bean.getListenThreadDebug(), var2.getListenThreadDebug(), false);
            this.computeDiff("MagicThreadDumpBackToSocket", this.bean.getMagicThreadDumpBackToSocket(), var2.getMagicThreadDumpBackToSocket(), false);
            this.computeDiff("MagicThreadDumpFile", this.bean.getMagicThreadDumpFile(), var2.getMagicThreadDumpFile(), false);
            this.computeDiff("MagicThreadDumpHost", this.bean.getMagicThreadDumpHost(), var2.getMagicThreadDumpHost(), false);
            this.computeDiff("MasterDeployer", this.bean.getMasterDeployer(), var2.getMasterDeployer(), false);
            this.computeDiff("RedefiningClassLoader", this.bean.getRedefiningClassLoader(), var2.getRedefiningClassLoader(), true);
            this.computeDiff("Server", this.bean.getServer(), var2.getServer(), false);
            this.computeDiff("SlaveDeployer", this.bean.getSlaveDeployer(), var2.getSlaveDeployer(), false);
            this.computeDiff("WebModule", this.bean.getWebModule(), var2.getWebModule(), false);
            this.computeDiff("MagicThreadDumpEnabled", this.bean.isMagicThreadDumpEnabled(), var2.isMagicThreadDumpEnabled(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            ServerDebugMBeanImpl var3 = (ServerDebugMBeanImpl)var1.getSourceBean();
            ServerDebugMBeanImpl var4 = (ServerDebugMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("ApplicationContainer")) {
                  var3.setApplicationContainer(var4.getApplicationContainer());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 236);
               } else if (var5.equals("BugReportServiceWsdlUrl")) {
                  var3.setBugReportServiceWsdlUrl(var4.getBugReportServiceWsdlUrl());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 43);
               } else if (var5.equals("ClassChangeNotifier")) {
                  var3.setClassChangeNotifier(var4.getClassChangeNotifier());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 50);
               } else if (var5.equals("ClassFinder")) {
                  var3.setClassFinder(var4.getClassFinder());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 237);
               } else if (var5.equals("ClassLoader")) {
                  var3.setClassLoader(var4.getClassLoader());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 240);
               } else if (var5.equals("ClassLoaderVerbose")) {
                  var3.setClassLoaderVerbose(var4.getClassLoaderVerbose());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 241);
               } else if (var5.equals("ClassloaderWebApp")) {
                  var3.setClassloaderWebApp(var4.getClassloaderWebApp());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 242);
               } else if (var5.equals("ClasspathServlet")) {
                  var3.setClasspathServlet(var4.getClasspathServlet());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 238);
               } else if (var5.equals("DebugAppContainer")) {
                  var3.setDebugAppContainer(var4.getDebugAppContainer());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 44);
               } else if (var5.equals("DebugAsyncQueue")) {
                  var3.setDebugAsyncQueue(var4.getDebugAsyncQueue());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 78);
               } else if (var5.equals("DebugBootstrapServlet")) {
                  var3.setDebugBootstrapServlet(var4.getDebugBootstrapServlet());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 243);
               } else if (var5.equals("DebugCertRevocCheck")) {
                  var3.setDebugCertRevocCheck(var4.getDebugCertRevocCheck());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 155);
               } else if (var5.equals("DebugClassRedef")) {
                  var3.setDebugClassRedef(var4.getDebugClassRedef());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 46);
               } else if (var5.equals("DebugClassSize")) {
                  var3.setDebugClassSize(var4.getDebugClassSize());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 48);
               } else if (var5.equals("DebugCluster")) {
                  var3.setDebugCluster(var4.getDebugCluster());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 73);
               } else if (var5.equals("DebugClusterAnnouncements")) {
                  var3.setDebugClusterAnnouncements(var4.getDebugClusterAnnouncements());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 75);
               } else if (var5.equals("DebugClusterFragments")) {
                  var3.setDebugClusterFragments(var4.getDebugClusterFragments());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 72);
               } else if (var5.equals("DebugClusterHeartbeats")) {
                  var3.setDebugClusterHeartbeats(var4.getDebugClusterHeartbeats());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 74);
               } else if (var5.equals("DebugConfigurationEdit")) {
                  var3.setDebugConfigurationEdit(var4.getDebugConfigurationEdit());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 299);
               } else if (var5.equals("DebugConfigurationRuntime")) {
                  var3.setDebugConfigurationRuntime(var4.getDebugConfigurationRuntime());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 300);
               } else if (var5.equals("DebugConnectorService")) {
                  var3.setDebugConnectorService(var4.getDebugConnectorService());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 282);
               } else if (var5.equals("DebugConsensusLeasing")) {
                  var3.setDebugConsensusLeasing(var4.getDebugConsensusLeasing());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 304);
               } else if (var5.equals("DebugDRSCalls")) {
                  var3.setDebugDRSCalls(var4.getDebugDRSCalls());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 80);
               } else if (var5.equals("DebugDRSHeartbeats")) {
                  var3.setDebugDRSHeartbeats(var4.getDebugDRSHeartbeats());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 81);
               } else if (var5.equals("DebugDRSMessages")) {
                  var3.setDebugDRSMessages(var4.getDebugDRSMessages());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 82);
               } else if (var5.equals("DebugDRSQueues")) {
                  var3.setDebugDRSQueues(var4.getDebugDRSQueues());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 85);
               } else if (var5.equals("DebugDRSStateTransitions")) {
                  var3.setDebugDRSStateTransitions(var4.getDebugDRSStateTransitions());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 84);
               } else if (var5.equals("DebugDRSUpdateStatus")) {
                  var3.setDebugDRSUpdateStatus(var4.getDebugDRSUpdateStatus());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 83);
               } else if (var5.equals("DebugDeploy")) {
                  var3.setDebugDeploy(var4.getDebugDeploy());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 227);
               } else if (var5.equals("DebugDeployment")) {
                  var3.setDebugDeployment(var4.getDebugDeployment());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 228);
               } else if (var5.equals("DebugDeploymentService")) {
                  var3.setDebugDeploymentService(var4.getDebugDeploymentService());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 229);
               } else if (var5.equals("DebugDeploymentServiceInternal")) {
                  var3.setDebugDeploymentServiceInternal(var4.getDebugDeploymentServiceInternal());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 231);
               } else if (var5.equals("DebugDeploymentServiceStatusUpdates")) {
                  var3.setDebugDeploymentServiceStatusUpdates(var4.getDebugDeploymentServiceStatusUpdates());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 230);
               } else if (var5.equals("DebugDeploymentServiceTransport")) {
                  var3.setDebugDeploymentServiceTransport(var4.getDebugDeploymentServiceTransport());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 232);
               } else if (var5.equals("DebugDeploymentServiceTransportHttp")) {
                  var3.setDebugDeploymentServiceTransportHttp(var4.getDebugDeploymentServiceTransportHttp());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 233);
               } else if (var5.equals("DebugDescriptor")) {
                  var3.setDebugDescriptor(var4.getDebugDescriptor());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 325);
               } else if (var5.equals("DebugDiagnosticAccessor")) {
                  var3.setDebugDiagnosticAccessor(var4.getDebugDiagnosticAccessor());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 265);
               } else if (var5.equals("DebugDiagnosticArchive")) {
                  var3.setDebugDiagnosticArchive(var4.getDebugDiagnosticArchive());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 253);
               } else if (var5.equals("DebugDiagnosticArchiveRetirement")) {
                  var3.setDebugDiagnosticArchiveRetirement(var4.getDebugDiagnosticArchiveRetirement());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 257);
               } else if (var5.equals("DebugDiagnosticCollections")) {
                  var3.setDebugDiagnosticCollections(var4.getDebugDiagnosticCollections());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 266);
               } else if (var5.equals("DebugDiagnosticContext")) {
                  var3.setDebugDiagnosticContext(var4.getDebugDiagnosticContext());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 267);
               } else if (var5.equals("DebugDiagnosticDataGathering")) {
                  var3.setDebugDiagnosticDataGathering(var4.getDebugDiagnosticDataGathering());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 246);
               } else if (var5.equals("DebugDiagnosticFileArchive")) {
                  var3.setDebugDiagnosticFileArchive(var4.getDebugDiagnosticFileArchive());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 254);
               } else if (var5.equals("DebugDiagnosticImage")) {
                  var3.setDebugDiagnosticImage(var4.getDebugDiagnosticImage());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 263);
               } else if (var5.equals("DebugDiagnosticInstrumentation")) {
                  var3.setDebugDiagnosticInstrumentation(var4.getDebugDiagnosticInstrumentation());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 247);
               } else if (var5.equals("DebugDiagnosticInstrumentationActions")) {
                  var3.setDebugDiagnosticInstrumentationActions(var4.getDebugDiagnosticInstrumentationActions());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 250);
               } else if (var5.equals("DebugDiagnosticInstrumentationConfig")) {
                  var3.setDebugDiagnosticInstrumentationConfig(var4.getDebugDiagnosticInstrumentationConfig());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 252);
               } else if (var5.equals("DebugDiagnosticInstrumentationEvents")) {
                  var3.setDebugDiagnosticInstrumentationEvents(var4.getDebugDiagnosticInstrumentationEvents());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 251);
               } else if (var5.equals("DebugDiagnosticInstrumentationWeaving")) {
                  var3.setDebugDiagnosticInstrumentationWeaving(var4.getDebugDiagnosticInstrumentationWeaving());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 248);
               } else if (var5.equals("DebugDiagnosticInstrumentationWeavingMatches")) {
                  var3.setDebugDiagnosticInstrumentationWeavingMatches(var4.getDebugDiagnosticInstrumentationWeavingMatches());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 249);
               } else if (var5.equals("DebugDiagnosticJdbcArchive")) {
                  var3.setDebugDiagnosticJdbcArchive(var4.getDebugDiagnosticJdbcArchive());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 256);
               } else if (var5.equals("DebugDiagnosticLifecycleHandlers")) {
                  var3.setDebugDiagnosticLifecycleHandlers(var4.getDebugDiagnosticLifecycleHandlers());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 245);
               } else if (var5.equals("DebugDiagnosticQuery")) {
                  var3.setDebugDiagnosticQuery(var4.getDebugDiagnosticQuery());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 264);
               } else if (var5.equals("DebugDiagnosticWatch")) {
                  var3.setDebugDiagnosticWatch(var4.getDebugDiagnosticWatch());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 274);
               } else if (var5.equals("DebugDiagnosticWlstoreArchive")) {
                  var3.setDebugDiagnosticWlstoreArchive(var4.getDebugDiagnosticWlstoreArchive());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 255);
               } else if (var5.equals("DebugDiagnosticsHarvester")) {
                  var3.setDebugDiagnosticsHarvester(var4.getDebugDiagnosticsHarvester());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 259);
               } else if (var5.equals("DebugDiagnosticsHarvesterData")) {
                  var3.setDebugDiagnosticsHarvesterData(var4.getDebugDiagnosticsHarvesterData());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 260);
               } else if (var5.equals("DebugDiagnosticsHarvesterMBeanPlugin")) {
                  var3.setDebugDiagnosticsHarvesterMBeanPlugin(var4.getDebugDiagnosticsHarvesterMBeanPlugin());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 261);
               } else if (var5.equals("DebugDiagnosticsHarvesterTreeBeanPlugin")) {
                  var3.setDebugDiagnosticsHarvesterTreeBeanPlugin(var4.getDebugDiagnosticsHarvesterTreeBeanPlugin());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 262);
               } else if (var5.equals("DebugDiagnosticsModule")) {
                  var3.setDebugDiagnosticsModule(var4.getDebugDiagnosticsModule());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 258);
               } else if (var5.equals("DebugDomainLogHandler")) {
                  var3.setDebugDomainLogHandler(var4.getDebugDomainLogHandler());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 272);
               } else if (var5.equals("DebugEjbCaching")) {
                  var3.setDebugEjbCaching(var4.getDebugEjbCaching());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 61);
               } else if (var5.equals("DebugEjbCmpDeployment")) {
                  var3.setDebugEjbCmpDeployment(var4.getDebugEjbCmpDeployment());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 68);
               } else if (var5.equals("DebugEjbCmpRuntime")) {
                  var3.setDebugEjbCmpRuntime(var4.getDebugEjbCmpRuntime());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 69);
               } else if (var5.equals("DebugEjbCompilation")) {
                  var3.setDebugEjbCompilation(var4.getDebugEjbCompilation());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 58);
               } else if (var5.equals("DebugEjbDeployment")) {
                  var3.setDebugEjbDeployment(var4.getDebugEjbDeployment());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 59);
               } else if (var5.equals("DebugEjbInvoke")) {
                  var3.setDebugEjbInvoke(var4.getDebugEjbInvoke());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 66);
               } else if (var5.equals("DebugEjbLocking")) {
                  var3.setDebugEjbLocking(var4.getDebugEjbLocking());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 63);
               } else if (var5.equals("DebugEjbMdbConnection")) {
                  var3.setDebugEjbMdbConnection(var4.getDebugEjbMdbConnection());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 60);
               } else if (var5.equals("DebugEjbPooling")) {
                  var3.setDebugEjbPooling(var4.getDebugEjbPooling());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 64);
               } else if (var5.equals("DebugEjbSecurity")) {
                  var3.setDebugEjbSecurity(var4.getDebugEjbSecurity());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 67);
               } else if (var5.equals("DebugEjbSwapping")) {
                  var3.setDebugEjbSwapping(var4.getDebugEjbSwapping());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 62);
               } else if (var5.equals("DebugEjbTimers")) {
                  var3.setDebugEjbTimers(var4.getDebugEjbTimers());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 65);
               } else if (var5.equals("DebugEmbeddedLDAP")) {
                  var3.setDebugEmbeddedLDAP(var4.getDebugEmbeddedLDAP());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 156);
               } else if (var5.equals("DebugEmbeddedLDAPLogLevel")) {
                  var3.setDebugEmbeddedLDAPLogLevel(var4.getDebugEmbeddedLDAPLogLevel());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 158);
               } else if (var5.equals("DebugEmbeddedLDAPLogToConsole")) {
                  var3.setDebugEmbeddedLDAPLogToConsole(var4.getDebugEmbeddedLDAPLogToConsole());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 157);
               } else if (var5.equals("DebugEmbeddedLDAPWriteOverrideProps")) {
                  var3.setDebugEmbeddedLDAPWriteOverrideProps(var4.getDebugEmbeddedLDAPWriteOverrideProps());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 159);
               } else if (var5.equals("DebugEventManager")) {
                  var3.setDebugEventManager(var4.getDebugEventManager());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 70);
               } else if (var5.equals("DebugFileDistributionServlet")) {
                  var3.setDebugFileDistributionServlet(var4.getDebugFileDistributionServlet());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 244);
               } else if (var5.equals("DebugHttp")) {
                  var3.setDebugHttp(var4.getDebugHttp());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 51);
               } else if (var5.equals("DebugHttpLogging")) {
                  var3.setDebugHttpLogging(var4.getDebugHttpLogging());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 54);
               } else if (var5.equals("DebugHttpSessions")) {
                  var3.setDebugHttpSessions(var4.getDebugHttpSessions());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 53);
               } else if (var5.equals("DebugIIOPNaming")) {
                  var3.setDebugIIOPNaming(var4.getDebugIIOPNaming());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 302);
               } else if (var5.equals("DebugIIOPTunneling")) {
                  var3.setDebugIIOPTunneling(var4.getDebugIIOPTunneling());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 303);
               } else if (var5.equals("DebugJ2EEManagement")) {
                  var3.setDebugJ2EEManagement(var4.getDebugJ2EEManagement());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 301);
               } else if (var5.equals("DebugJAXPDebugLevel")) {
                  var3.setDebugJAXPDebugLevel(var4.getDebugJAXPDebugLevel());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 211);
               } else if (var5.equals("DebugJAXPDebugName")) {
                  var3.setDebugJAXPDebugName(var4.getDebugJAXPDebugName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 212);
               } else if (var5.equals("DebugJAXPIncludeClass")) {
                  var3.setDebugJAXPIncludeClass(var4.getDebugJAXPIncludeClass());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 216);
               } else if (var5.equals("DebugJAXPIncludeLocation")) {
                  var3.setDebugJAXPIncludeLocation(var4.getDebugJAXPIncludeLocation());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 217);
               } else if (var5.equals("DebugJAXPIncludeName")) {
                  var3.setDebugJAXPIncludeName(var4.getDebugJAXPIncludeName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 215);
               } else if (var5.equals("DebugJAXPIncludeTime")) {
                  var3.setDebugJAXPIncludeTime(var4.getDebugJAXPIncludeTime());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 214);
               } else if (!var5.equals("DebugJAXPOutputStream")) {
                  if (var5.equals("DebugJAXPUseShortClass")) {
                     var3.setDebugJAXPUseShortClass(var4.getDebugJAXPUseShortClass());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 218);
                  } else if (var5.equals("DebugJDBCConn")) {
                     var3.setDebugJDBCConn(var4.getDebugJDBCConn());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 182);
                  } else if (var5.equals("DebugJDBCDriverLogging")) {
                     var3.setDebugJDBCDriverLogging(var4.getDebugJDBCDriverLogging());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 185);
                  } else if (var5.equals("DebugJDBCInternal")) {
                     var3.setDebugJDBCInternal(var4.getDebugJDBCInternal());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 186);
                  } else if (var5.equals("DebugJDBCONS")) {
                     var3.setDebugJDBCONS(var4.getDebugJDBCONS());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 188);
                  } else if (var5.equals("DebugJDBCRAC")) {
                     var3.setDebugJDBCRAC(var4.getDebugJDBCRAC());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 187);
                  } else if (var5.equals("DebugJDBCREPLAY")) {
                     var3.setDebugJDBCREPLAY(var4.getDebugJDBCREPLAY());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 190);
                  } else if (var5.equals("DebugJDBCRMI")) {
                     var3.setDebugJDBCRMI(var4.getDebugJDBCRMI());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 184);
                  } else if (var5.equals("DebugJDBCSQL")) {
                     var3.setDebugJDBCSQL(var4.getDebugJDBCSQL());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 183);
                  } else if (var5.equals("DebugJDBCUCP")) {
                     var3.setDebugJDBCUCP(var4.getDebugJDBCUCP());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 189);
                  } else if (var5.equals("DebugJMSAME")) {
                     var3.setDebugJMSAME(var4.getDebugJMSAME());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 103);
                  } else if (var5.equals("DebugJMSBackEnd")) {
                     var3.setDebugJMSBackEnd(var4.getDebugJMSBackEnd());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 91);
                  } else if (var5.equals("DebugJMSBoot")) {
                     var3.setDebugJMSBoot(var4.getDebugJMSBoot());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 100);
                  } else if (var5.equals("DebugJMSCDS")) {
                     var3.setDebugJMSCDS(var4.getDebugJMSCDS());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 109);
                  } else if (var5.equals("DebugJMSCommon")) {
                     var3.setDebugJMSCommon(var4.getDebugJMSCommon());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 93);
                  } else if (var5.equals("DebugJMSConfig")) {
                     var3.setDebugJMSConfig(var4.getDebugJMSConfig());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 94);
                  } else if (var5.equals("DebugJMSDispatcher")) {
                     var3.setDebugJMSDispatcher(var4.getDebugJMSDispatcher());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 98);
                  } else if (var5.equals("DebugJMSDistTopic")) {
                     var3.setDebugJMSDistTopic(var4.getDebugJMSDistTopic());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 95);
                  } else if (var5.equals("DebugJMSDurableSubscribers")) {
                     var3.setDebugJMSDurableSubscribers(var4.getDebugJMSDurableSubscribers());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 101);
                  } else if (var5.equals("DebugJMSFrontEnd")) {
                     var3.setDebugJMSFrontEnd(var4.getDebugJMSFrontEnd());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 92);
                  } else if (var5.equals("DebugJMSJDBCScavengeOnFlush")) {
                     var3.setDebugJMSJDBCScavengeOnFlush(var4.getDebugJMSJDBCScavengeOnFlush());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 102);
                  } else if (var5.equals("DebugJMSLocking")) {
                     var3.setDebugJMSLocking(var4.getDebugJMSLocking());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 96);
                  } else if (var5.equals("DebugJMSMessagePath")) {
                     var3.setDebugJMSMessagePath(var4.getDebugJMSMessagePath());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 106);
                  } else if (var5.equals("DebugJMSModule")) {
                     var3.setDebugJMSModule(var4.getDebugJMSModule());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 105);
                  } else if (var5.equals("DebugJMSPauseResume")) {
                     var3.setDebugJMSPauseResume(var4.getDebugJMSPauseResume());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 104);
                  } else if (var5.equals("DebugJMSSAF")) {
                     var3.setDebugJMSSAF(var4.getDebugJMSSAF());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 107);
                  } else if (var5.equals("DebugJMSStore")) {
                     var3.setDebugJMSStore(var4.getDebugJMSStore());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 99);
                  } else if (var5.equals("DebugJMST3Server")) {
                     var3.setDebugJMST3Server(var4.getDebugJMST3Server());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 324);
                  } else if (var5.equals("DebugJMSWrappers")) {
                     var3.setDebugJMSWrappers(var4.getDebugJMSWrappers());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 108);
                  } else if (var5.equals("DebugJMSXA")) {
                     var3.setDebugJMSXA(var4.getDebugJMSXA());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 97);
                  } else if (var5.equals("DebugJMX")) {
                     var3.setDebugJMX(var4.getDebugJMX());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 293);
                  } else if (var5.equals("DebugJMXCompatibility")) {
                     var3.setDebugJMXCompatibility(var4.getDebugJMXCompatibility());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 298);
                  } else if (var5.equals("DebugJMXCore")) {
                     var3.setDebugJMXCore(var4.getDebugJMXCore());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 294);
                  } else if (var5.equals("DebugJMXDomain")) {
                     var3.setDebugJMXDomain(var4.getDebugJMXDomain());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 296);
                  } else if (var5.equals("DebugJMXEdit")) {
                     var3.setDebugJMXEdit(var4.getDebugJMXEdit());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 297);
                  } else if (var5.equals("DebugJMXRuntime")) {
                     var3.setDebugJMXRuntime(var4.getDebugJMXRuntime());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 295);
                  } else if (var5.equals("DebugJNDI")) {
                     var3.setDebugJNDI(var4.getDebugJNDI());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 86);
                  } else if (var5.equals("DebugJNDIFactories")) {
                     var3.setDebugJNDIFactories(var4.getDebugJNDIFactories());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 88);
                  } else if (var5.equals("DebugJNDIResolution")) {
                     var3.setDebugJNDIResolution(var4.getDebugJNDIResolution());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 87);
                  } else if (var5.equals("DebugJTA2PC")) {
                     var3.setDebugJTA2PC(var4.getDebugJTA2PC());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 114);
                  } else if (var5.equals("DebugJTA2PCStackTrace")) {
                     var3.setDebugJTA2PCStackTrace(var4.getDebugJTA2PCStackTrace());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 115);
                  } else if (var5.equals("DebugJTAAPI")) {
                     var3.setDebugJTAAPI(var4.getDebugJTAAPI());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 120);
                  } else if (var5.equals("DebugJTAGateway")) {
                     var3.setDebugJTAGateway(var4.getDebugJTAGateway());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 122);
                  } else if (var5.equals("DebugJTAGatewayStackTrace")) {
                     var3.setDebugJTAGatewayStackTrace(var4.getDebugJTAGatewayStackTrace());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 123);
                  } else if (var5.equals("DebugJTAHealth")) {
                     var3.setDebugJTAHealth(var4.getDebugJTAHealth());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 130);
                  } else if (var5.equals("DebugJTAJDBC")) {
                     var3.setDebugJTAJDBC(var4.getDebugJTAJDBC());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 117);
                  } else if (var5.equals("DebugJTALLR")) {
                     var3.setDebugJTALLR(var4.getDebugJTALLR());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 129);
                  } else if (var5.equals("DebugJTALifecycle")) {
                     var3.setDebugJTALifecycle(var4.getDebugJTALifecycle());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 128);
                  } else if (var5.equals("DebugJTAMigration")) {
                     var3.setDebugJTAMigration(var4.getDebugJTAMigration());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 127);
                  } else if (var5.equals("DebugJTANaming")) {
                     var3.setDebugJTANaming(var4.getDebugJTANaming());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 124);
                  } else if (var5.equals("DebugJTANamingStackTrace")) {
                     var3.setDebugJTANamingStackTrace(var4.getDebugJTANamingStackTrace());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 125);
                  } else if (var5.equals("DebugJTANonXA")) {
                     var3.setDebugJTANonXA(var4.getDebugJTANonXA());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 111);
                  } else if (var5.equals("DebugJTAPropagate")) {
                     var3.setDebugJTAPropagate(var4.getDebugJTAPropagate());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 121);
                  } else if (var5.equals("DebugJTARMI")) {
                     var3.setDebugJTARMI(var4.getDebugJTARMI());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 113);
                  } else if (var5.equals("DebugJTARecovery")) {
                     var3.setDebugJTARecovery(var4.getDebugJTARecovery());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 118);
                  } else if (var5.equals("DebugJTARecoveryStackTrace")) {
                     var3.setDebugJTARecoveryStackTrace(var4.getDebugJTARecoveryStackTrace());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 119);
                  } else if (var5.equals("DebugJTAResourceHealth")) {
                     var3.setDebugJTAResourceHealth(var4.getDebugJTAResourceHealth());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 126);
                  } else if (var5.equals("DebugJTAResourceName")) {
                     var3.setDebugJTAResourceName(var4.getDebugJTAResourceName());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 132);
                  } else if (var5.equals("DebugJTATLOG")) {
                     var3.setDebugJTATLOG(var4.getDebugJTATLOG());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 116);
                  } else if (var5.equals("DebugJTATransactionName")) {
                     var3.setDebugJTATransactionName(var4.getDebugJTATransactionName());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 131);
                  } else if (var5.equals("DebugJTAXA")) {
                     var3.setDebugJTAXA(var4.getDebugJTAXA());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 110);
                  } else if (var5.equals("DebugJTAXAStackTrace")) {
                     var3.setDebugJTAXAStackTrace(var4.getDebugJTAXAStackTrace());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 112);
                  } else if (var5.equals("DebugJpaDataCache")) {
                     var3.setDebugJpaDataCache(var4.getDebugJpaDataCache());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 317);
                  } else if (var5.equals("DebugJpaEnhance")) {
                     var3.setDebugJpaEnhance(var4.getDebugJpaEnhance());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 314);
                  } else if (var5.equals("DebugJpaJdbcJdbc")) {
                     var3.setDebugJpaJdbcJdbc(var4.getDebugJpaJdbcJdbc());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 322);
                  } else if (var5.equals("DebugJpaJdbcSchema")) {
                     var3.setDebugJpaJdbcSchema(var4.getDebugJpaJdbcSchema());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 323);
                  } else if (var5.equals("DebugJpaJdbcSql")) {
                     var3.setDebugJpaJdbcSql(var4.getDebugJpaJdbcSql());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 321);
                  } else if (var5.equals("DebugJpaManage")) {
                     var3.setDebugJpaManage(var4.getDebugJpaManage());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 319);
                  } else if (var5.equals("DebugJpaMetaData")) {
                     var3.setDebugJpaMetaData(var4.getDebugJpaMetaData());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 313);
                  } else if (var5.equals("DebugJpaProfile")) {
                     var3.setDebugJpaProfile(var4.getDebugJpaProfile());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 320);
                  } else if (var5.equals("DebugJpaQuery")) {
                     var3.setDebugJpaQuery(var4.getDebugJpaQuery());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 316);
                  } else if (var5.equals("DebugJpaRuntime")) {
                     var3.setDebugJpaRuntime(var4.getDebugJpaRuntime());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 315);
                  } else if (var5.equals("DebugJpaTool")) {
                     var3.setDebugJpaTool(var4.getDebugJpaTool());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 318);
                  } else if (var5.equals("DebugLeaderElection")) {
                     var3.setDebugLeaderElection(var4.getDebugLeaderElection());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 79);
                  } else if (var5.equals("DebugLibraries")) {
                     var3.setDebugLibraries(var4.getDebugLibraries());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 45);
                  } else if (var5.equals("DebugLoggingConfiguration")) {
                     var3.setDebugLoggingConfiguration(var4.getDebugLoggingConfiguration());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 273);
                  } else if (var5.equals("DebugManagementServicesResource")) {
                     var3.setDebugManagementServicesResource(var4.getDebugManagementServicesResource());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 327);
                  } else if (var5.equals("DebugMaskCriterias")) {
                     var3.setDebugMaskCriterias(var4.getDebugMaskCriterias());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 36);
                  } else if (var5.equals("DebugMessagingBridgeDumpToConsole")) {
                     var3.setDebugMessagingBridgeDumpToConsole(var4.getDebugMessagingBridgeDumpToConsole());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 195);
                  } else if (var5.equals("DebugMessagingBridgeDumpToLog")) {
                     var3.setDebugMessagingBridgeDumpToLog(var4.getDebugMessagingBridgeDumpToLog());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 194);
                  } else if (var5.equals("DebugMessagingBridgeRuntime")) {
                     var3.setDebugMessagingBridgeRuntime(var4.getDebugMessagingBridgeRuntime());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 192);
                  } else if (var5.equals("DebugMessagingBridgeRuntimeVerbose")) {
                     var3.setDebugMessagingBridgeRuntimeVerbose(var4.getDebugMessagingBridgeRuntimeVerbose());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 193);
                  } else if (var5.equals("DebugMessagingBridgeStartup")) {
                     var3.setDebugMessagingBridgeStartup(var4.getDebugMessagingBridgeStartup());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 191);
                  } else if (var5.equals("DebugMessagingKernel")) {
                     var3.setDebugMessagingKernel(var4.getDebugMessagingKernel());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 133);
                  } else if (var5.equals("DebugMessagingKernelBoot")) {
                     var3.setDebugMessagingKernelBoot(var4.getDebugMessagingKernelBoot());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 134);
                  } else if (var5.equals("DebugPathSvc")) {
                     var3.setDebugPathSvc(var4.getDebugPathSvc());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 144);
                  } else if (var5.equals("DebugPathSvcVerbose")) {
                     var3.setDebugPathSvcVerbose(var4.getDebugPathSvcVerbose());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 145);
                  } else if (var5.equals("DebugRA")) {
                     var3.setDebugRA(var4.getDebugRA());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 276);
                  } else if (var5.equals("DebugRAClassloader")) {
                     var3.setDebugRAClassloader(var4.getDebugRAClassloader());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 291);
                  } else if (var5.equals("DebugRAConnEvents")) {
                     var3.setDebugRAConnEvents(var4.getDebugRAConnEvents());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 288);
                  } else if (var5.equals("DebugRAConnections")) {
                     var3.setDebugRAConnections(var4.getDebugRAConnections());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 287);
                  } else if (var5.equals("DebugRADeployment")) {
                     var3.setDebugRADeployment(var4.getDebugRADeployment());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 283);
                  } else if (var5.equals("DebugRALifecycle")) {
                     var3.setDebugRALifecycle(var4.getDebugRALifecycle());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 281);
                  } else if (var5.equals("DebugRALocalOut")) {
                     var3.setDebugRALocalOut(var4.getDebugRALocalOut());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 280);
                  } else if (var5.equals("DebugRAParsing")) {
                     var3.setDebugRAParsing(var4.getDebugRAParsing());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 284);
                  } else if (var5.equals("DebugRAPoolVerbose")) {
                     var3.setDebugRAPoolVerbose(var4.getDebugRAPoolVerbose());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 275);
                  } else if (var5.equals("DebugRAPooling")) {
                     var3.setDebugRAPooling(var4.getDebugRAPooling());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 286);
                  } else if (var5.equals("DebugRASecurityCtx")) {
                     var3.setDebugRASecurityCtx(var4.getDebugRASecurityCtx());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 285);
                  } else if (var5.equals("DebugRAWork")) {
                     var3.setDebugRAWork(var4.getDebugRAWork());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 289);
                  } else if (var5.equals("DebugRAWorkEvents")) {
                     var3.setDebugRAWorkEvents(var4.getDebugRAWorkEvents());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 290);
                  } else if (var5.equals("DebugRAXAin")) {
                     var3.setDebugRAXAin(var4.getDebugRAXAin());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 277);
                  } else if (var5.equals("DebugRAXAout")) {
                     var3.setDebugRAXAout(var4.getDebugRAXAout());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 278);
                  } else if (var5.equals("DebugRAXAwork")) {
                     var3.setDebugRAXAwork(var4.getDebugRAXAwork());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 279);
                  } else if (var5.equals("DebugReplication")) {
                     var3.setDebugReplication(var4.getDebugReplication());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 76);
                  } else if (var5.equals("DebugReplicationDetails")) {
                     var3.setDebugReplicationDetails(var4.getDebugReplicationDetails());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 77);
                  } else if (var5.equals("DebugSAFAdmin")) {
                     var3.setDebugSAFAdmin(var4.getDebugSAFAdmin());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 136);
                  } else if (var5.equals("DebugSAFLifeCycle")) {
                     var3.setDebugSAFLifeCycle(var4.getDebugSAFLifeCycle());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 135);
                  } else if (var5.equals("DebugSAFManager")) {
                     var3.setDebugSAFManager(var4.getDebugSAFManager());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 137);
                  } else if (var5.equals("DebugSAFMessagePath")) {
                     var3.setDebugSAFMessagePath(var4.getDebugSAFMessagePath());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 141);
                  } else if (var5.equals("DebugSAFReceivingAgent")) {
                     var3.setDebugSAFReceivingAgent(var4.getDebugSAFReceivingAgent());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 139);
                  } else if (var5.equals("DebugSAFSendingAgent")) {
                     var3.setDebugSAFSendingAgent(var4.getDebugSAFSendingAgent());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 138);
                  } else if (var5.equals("DebugSAFStore")) {
                     var3.setDebugSAFStore(var4.getDebugSAFStore());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 142);
                  } else if (var5.equals("DebugSAFTransport")) {
                     var3.setDebugSAFTransport(var4.getDebugSAFTransport());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 140);
                  } else if (var5.equals("DebugSAFVerbose")) {
                     var3.setDebugSAFVerbose(var4.getDebugSAFVerbose());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 143);
                  } else if (var5.equals("DebugSNMPAgent")) {
                     var3.setDebugSNMPAgent(var4.getDebugSNMPAgent());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 269);
                  } else if (var5.equals("DebugSNMPExtensionProvider")) {
                     var3.setDebugSNMPExtensionProvider(var4.getDebugSNMPExtensionProvider());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 271);
                  } else if (var5.equals("DebugSNMPProtocolTCP")) {
                     var3.setDebugSNMPProtocolTCP(var4.getDebugSNMPProtocolTCP());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 270);
                  } else if (var5.equals("DebugSNMPToolkit")) {
                     var3.setDebugSNMPToolkit(var4.getDebugSNMPToolkit());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 268);
                  } else if (var5.equals("DebugScaContainer")) {
                     var3.setDebugScaContainer(var4.getDebugScaContainer());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 146);
                  } else if (var5.equals("DebugSecurity")) {
                     var3.setDebugSecurity(var4.getDebugSecurity());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 148);
                  } else if (var5.equals("DebugSecurityAdjudicator")) {
                     var3.setDebugSecurityAdjudicator(var4.getDebugSecurityAdjudicator());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 160);
                  } else if (var5.equals("DebugSecurityAtn")) {
                     var3.setDebugSecurityAtn(var4.getDebugSecurityAtn());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 161);
                  } else if (var5.equals("DebugSecurityAtz")) {
                     var3.setDebugSecurityAtz(var4.getDebugSecurityAtz());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 162);
                  } else if (var5.equals("DebugSecurityAuditor")) {
                     var3.setDebugSecurityAuditor(var4.getDebugSecurityAuditor());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 163);
                  } else if (var5.equals("DebugSecurityCertPath")) {
                     var3.setDebugSecurityCertPath(var4.getDebugSecurityCertPath());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 167);
                  } else if (var5.equals("DebugSecurityCredMap")) {
                     var3.setDebugSecurityCredMap(var4.getDebugSecurityCredMap());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 164);
                  } else if (var5.equals("DebugSecurityEEngine")) {
                     var3.setDebugSecurityEEngine(var4.getDebugSecurityEEngine());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 170);
                  } else if (var5.equals("DebugSecurityEncryptionService")) {
                     var3.setDebugSecurityEncryptionService(var4.getDebugSecurityEncryptionService());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 165);
                  } else if (var5.equals("DebugSecurityJACC")) {
                     var3.setDebugSecurityJACC(var4.getDebugSecurityJACC());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 171);
                  } else if (var5.equals("DebugSecurityJACCNonPolicy")) {
                     var3.setDebugSecurityJACCNonPolicy(var4.getDebugSecurityJACCNonPolicy());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 172);
                  } else if (var5.equals("DebugSecurityJACCPolicy")) {
                     var3.setDebugSecurityJACCPolicy(var4.getDebugSecurityJACCPolicy());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 173);
                  } else if (var5.equals("DebugSecurityKeyStore")) {
                     var3.setDebugSecurityKeyStore(var4.getDebugSecurityKeyStore());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 166);
                  } else if (var5.equals("DebugSecurityPasswordPolicy")) {
                     var3.setDebugSecurityPasswordPolicy(var4.getDebugSecurityPasswordPolicy());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 149);
                  } else if (var5.equals("DebugSecurityPredicate")) {
                     var3.setDebugSecurityPredicate(var4.getDebugSecurityPredicate());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 152);
                  } else if (var5.equals("DebugSecurityProfiler")) {
                     var3.setDebugSecurityProfiler(var4.getDebugSecurityProfiler());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 168);
                  } else if (var5.equals("DebugSecurityRealm")) {
                     var3.setDebugSecurityRealm(var4.getDebugSecurityRealm());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 147);
                  } else if (var5.equals("DebugSecurityRoleMap")) {
                     var3.setDebugSecurityRoleMap(var4.getDebugSecurityRoleMap());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 169);
                  } else if (var5.equals("DebugSecuritySAML2Atn")) {
                     var3.setDebugSecuritySAML2Atn(var4.getDebugSecuritySAML2Atn());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 179);
                  } else if (var5.equals("DebugSecuritySAML2CredMap")) {
                     var3.setDebugSecuritySAML2CredMap(var4.getDebugSecuritySAML2CredMap());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 180);
                  } else if (var5.equals("DebugSecuritySAML2Lib")) {
                     var3.setDebugSecuritySAML2Lib(var4.getDebugSecuritySAML2Lib());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 178);
                  } else if (var5.equals("DebugSecuritySAML2Service")) {
                     var3.setDebugSecuritySAML2Service(var4.getDebugSecuritySAML2Service());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 181);
                  } else if (var5.equals("DebugSecuritySAMLAtn")) {
                     var3.setDebugSecuritySAMLAtn(var4.getDebugSecuritySAMLAtn());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 175);
                  } else if (var5.equals("DebugSecuritySAMLCredMap")) {
                     var3.setDebugSecuritySAMLCredMap(var4.getDebugSecuritySAMLCredMap());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 176);
                  } else if (var5.equals("DebugSecuritySAMLLib")) {
                     var3.setDebugSecuritySAMLLib(var4.getDebugSecuritySAMLLib());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 174);
                  } else if (var5.equals("DebugSecuritySAMLService")) {
                     var3.setDebugSecuritySAMLService(var4.getDebugSecuritySAMLService());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 177);
                  } else if (var5.equals("DebugSecuritySSL")) {
                     var3.setDebugSecuritySSL(var4.getDebugSecuritySSL());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 153);
                  } else if (var5.equals("DebugSecuritySSLEaten")) {
                     var3.setDebugSecuritySSLEaten(var4.getDebugSecuritySSLEaten());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 154);
                  } else if (var5.equals("DebugSecurityService")) {
                     var3.setDebugSecurityService(var4.getDebugSecurityService());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 151);
                  } else if (var5.equals("DebugSecurityUserLockout")) {
                     var3.setDebugSecurityUserLockout(var4.getDebugSecurityUserLockout());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 150);
                  } else if (var5.equals("DebugSelfTuning")) {
                     var3.setDebugSelfTuning(var4.getDebugSelfTuning());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 34);
                  } else if (var5.equals("DebugServerLifeCycle")) {
                     var3.setDebugServerLifeCycle(var4.getDebugServerLifeCycle());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 305);
                  } else if (var5.equals("DebugServerMigration")) {
                     var3.setDebugServerMigration(var4.getDebugServerMigration());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 71);
                  } else if (var5.equals("DebugServerStartStatistics")) {
                     var3.setDebugServerStartStatistics(var4.getDebugServerStartStatistics());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 326);
                  } else if (var5.equals("DebugStoreAdmin")) {
                     var3.setDebugStoreAdmin(var4.getDebugStoreAdmin());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 202);
                  } else if (var5.equals("DebugStoreIOLogical")) {
                     var3.setDebugStoreIOLogical(var4.getDebugStoreIOLogical());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 196);
                  } else if (var5.equals("DebugStoreIOLogicalBoot")) {
                     var3.setDebugStoreIOLogicalBoot(var4.getDebugStoreIOLogicalBoot());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 197);
                  } else if (var5.equals("DebugStoreIOPhysical")) {
                     var3.setDebugStoreIOPhysical(var4.getDebugStoreIOPhysical());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 198);
                  } else if (var5.equals("DebugStoreIOPhysicalVerbose")) {
                     var3.setDebugStoreIOPhysicalVerbose(var4.getDebugStoreIOPhysicalVerbose());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 199);
                  } else if (var5.equals("DebugStoreXA")) {
                     var3.setDebugStoreXA(var4.getDebugStoreXA());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 200);
                  } else if (var5.equals("DebugStoreXAVerbose")) {
                     var3.setDebugStoreXAVerbose(var4.getDebugStoreXAVerbose());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 201);
                  } else if (var5.equals("DebugTunnelingConnection")) {
                     var3.setDebugTunnelingConnection(var4.getDebugTunnelingConnection());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 90);
                  } else if (var5.equals("DebugTunnelingConnectionTimeout")) {
                     var3.setDebugTunnelingConnectionTimeout(var4.getDebugTunnelingConnectionTimeout());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 89);
                  } else if (var5.equals("DebugURLResolution")) {
                     var3.setDebugURLResolution(var4.getDebugURLResolution());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 52);
                  } else if (var5.equals("DebugWANReplicationDetails")) {
                     var3.setDebugWANReplicationDetails(var4.getDebugWANReplicationDetails());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 292);
                  } else if (var5.equals("DebugWTCConfig")) {
                     var3.setDebugWTCConfig(var4.getDebugWTCConfig());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 306);
                  } else if (var5.equals("DebugWTCCorbaEx")) {
                     var3.setDebugWTCCorbaEx(var4.getDebugWTCCorbaEx());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 311);
                  } else if (var5.equals("DebugWTCGwtEx")) {
                     var3.setDebugWTCGwtEx(var4.getDebugWTCGwtEx());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 309);
                  } else if (var5.equals("DebugWTCJatmiEx")) {
                     var3.setDebugWTCJatmiEx(var4.getDebugWTCJatmiEx());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 310);
                  } else if (var5.equals("DebugWTCTDomPdu")) {
                     var3.setDebugWTCTDomPdu(var4.getDebugWTCTDomPdu());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 307);
                  } else if (var5.equals("DebugWTCUData")) {
                     var3.setDebugWTCUData(var4.getDebugWTCUData());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 308);
                  } else if (var5.equals("DebugWTCtBridgeEx")) {
                     var3.setDebugWTCtBridgeEx(var4.getDebugWTCtBridgeEx());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 312);
                  } else if (var5.equals("DebugWebAppIdentityAssertion")) {
                     var3.setDebugWebAppIdentityAssertion(var4.getDebugWebAppIdentityAssertion());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 55);
                  } else if (var5.equals("DebugWebAppModule")) {
                     var3.setDebugWebAppModule(var4.getDebugWebAppModule());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 57);
                  } else if (var5.equals("DebugWebAppSecurity")) {
                     var3.setDebugWebAppSecurity(var4.getDebugWebAppSecurity());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 56);
                  } else if (var5.equals("DebugXMLEntityCacheDebugLevel")) {
                     var3.setDebugXMLEntityCacheDebugLevel(var4.getDebugXMLEntityCacheDebugLevel());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 219);
                  } else if (var5.equals("DebugXMLEntityCacheDebugName")) {
                     var3.setDebugXMLEntityCacheDebugName(var4.getDebugXMLEntityCacheDebugName());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 220);
                  } else if (var5.equals("DebugXMLEntityCacheIncludeClass")) {
                     var3.setDebugXMLEntityCacheIncludeClass(var4.getDebugXMLEntityCacheIncludeClass());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 224);
                  } else if (var5.equals("DebugXMLEntityCacheIncludeLocation")) {
                     var3.setDebugXMLEntityCacheIncludeLocation(var4.getDebugXMLEntityCacheIncludeLocation());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 225);
                  } else if (var5.equals("DebugXMLEntityCacheIncludeName")) {
                     var3.setDebugXMLEntityCacheIncludeName(var4.getDebugXMLEntityCacheIncludeName());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 223);
                  } else if (var5.equals("DebugXMLEntityCacheIncludeTime")) {
                     var3.setDebugXMLEntityCacheIncludeTime(var4.getDebugXMLEntityCacheIncludeTime());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 222);
                  } else if (!var5.equals("DebugXMLEntityCacheOutputStream")) {
                     if (var5.equals("DebugXMLEntityCacheUseShortClass")) {
                        var3.setDebugXMLEntityCacheUseShortClass(var4.getDebugXMLEntityCacheUseShortClass());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 226);
                     } else if (var5.equals("DebugXMLRegistryDebugLevel")) {
                        var3.setDebugXMLRegistryDebugLevel(var4.getDebugXMLRegistryDebugLevel());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 203);
                     } else if (var5.equals("DebugXMLRegistryDebugName")) {
                        var3.setDebugXMLRegistryDebugName(var4.getDebugXMLRegistryDebugName());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 204);
                     } else if (var5.equals("DebugXMLRegistryIncludeClass")) {
                        var3.setDebugXMLRegistryIncludeClass(var4.getDebugXMLRegistryIncludeClass());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 208);
                     } else if (var5.equals("DebugXMLRegistryIncludeLocation")) {
                        var3.setDebugXMLRegistryIncludeLocation(var4.getDebugXMLRegistryIncludeLocation());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 209);
                     } else if (var5.equals("DebugXMLRegistryIncludeName")) {
                        var3.setDebugXMLRegistryIncludeName(var4.getDebugXMLRegistryIncludeName());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 207);
                     } else if (var5.equals("DebugXMLRegistryIncludeTime")) {
                        var3.setDebugXMLRegistryIncludeTime(var4.getDebugXMLRegistryIncludeTime());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 206);
                     } else if (!var5.equals("DebugXMLRegistryOutputStream")) {
                        if (var5.equals("DebugXMLRegistryUseShortClass")) {
                           var3.setDebugXMLRegistryUseShortClass(var4.getDebugXMLRegistryUseShortClass());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 210);
                        } else if (var5.equals("DefaultStore")) {
                           var3.setDefaultStore(var4.getDefaultStore());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 49);
                        } else if (var5.equals("DiagnosticContextDebugMode")) {
                           var3.setDiagnosticContextDebugMode(var4.getDiagnosticContextDebugMode());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 35);
                        } else if (var5.equals("ListenThreadDebug")) {
                           var3.setListenThreadDebug(var4.getListenThreadDebug());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 38);
                        } else if (var5.equals("MagicThreadDumpBackToSocket")) {
                           var3.setMagicThreadDumpBackToSocket(var4.getMagicThreadDumpBackToSocket());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 42);
                        } else if (var5.equals("MagicThreadDumpFile")) {
                           var3.setMagicThreadDumpFile(var4.getMagicThreadDumpFile());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 41);
                        } else if (var5.equals("MagicThreadDumpHost")) {
                           var3.setMagicThreadDumpHost(var4.getMagicThreadDumpHost());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 40);
                        } else if (var5.equals("MasterDeployer")) {
                           var3.setMasterDeployer(var4.getMasterDeployer());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 234);
                        } else if (var5.equals("RedefiningClassLoader")) {
                           var3.setRedefiningClassLoader(var4.getRedefiningClassLoader());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 47);
                        } else if (var5.equals("Server")) {
                           var3.setServerAsString(var4.getServerAsString());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 37);
                        } else if (var5.equals("SlaveDeployer")) {
                           var3.setSlaveDeployer(var4.getSlaveDeployer());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 235);
                        } else if (var5.equals("WebModule")) {
                           var3.setWebModule(var4.getWebModule());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 239);
                        } else if (var5.equals("MagicThreadDumpEnabled")) {
                           var3.setMagicThreadDumpEnabled(var4.isMagicThreadDumpEnabled());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 39);
                        } else {
                           super.applyPropertyUpdate(var1, var2);
                        }
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
            ServerDebugMBeanImpl var5 = (ServerDebugMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("ApplicationContainer")) && this.bean.isApplicationContainerSet()) {
               var5.setApplicationContainer(this.bean.getApplicationContainer());
            }

            if ((var3 == null || !var3.contains("BugReportServiceWsdlUrl")) && this.bean.isBugReportServiceWsdlUrlSet()) {
               var5.setBugReportServiceWsdlUrl(this.bean.getBugReportServiceWsdlUrl());
            }

            if ((var3 == null || !var3.contains("ClassChangeNotifier")) && this.bean.isClassChangeNotifierSet()) {
               var5.setClassChangeNotifier(this.bean.getClassChangeNotifier());
            }

            if ((var3 == null || !var3.contains("ClassFinder")) && this.bean.isClassFinderSet()) {
               var5.setClassFinder(this.bean.getClassFinder());
            }

            if ((var3 == null || !var3.contains("ClassLoader")) && this.bean.isClassLoaderSet()) {
               var5.setClassLoader(this.bean.getClassLoader());
            }

            if ((var3 == null || !var3.contains("ClassLoaderVerbose")) && this.bean.isClassLoaderVerboseSet()) {
               var5.setClassLoaderVerbose(this.bean.getClassLoaderVerbose());
            }

            if ((var3 == null || !var3.contains("ClassloaderWebApp")) && this.bean.isClassloaderWebAppSet()) {
               var5.setClassloaderWebApp(this.bean.getClassloaderWebApp());
            }

            if ((var3 == null || !var3.contains("ClasspathServlet")) && this.bean.isClasspathServletSet()) {
               var5.setClasspathServlet(this.bean.getClasspathServlet());
            }

            if ((var3 == null || !var3.contains("DebugAppContainer")) && this.bean.isDebugAppContainerSet()) {
               var5.setDebugAppContainer(this.bean.getDebugAppContainer());
            }

            if ((var3 == null || !var3.contains("DebugAsyncQueue")) && this.bean.isDebugAsyncQueueSet()) {
               var5.setDebugAsyncQueue(this.bean.getDebugAsyncQueue());
            }

            if ((var3 == null || !var3.contains("DebugBootstrapServlet")) && this.bean.isDebugBootstrapServletSet()) {
               var5.setDebugBootstrapServlet(this.bean.getDebugBootstrapServlet());
            }

            if ((var3 == null || !var3.contains("DebugCertRevocCheck")) && this.bean.isDebugCertRevocCheckSet()) {
               var5.setDebugCertRevocCheck(this.bean.getDebugCertRevocCheck());
            }

            if ((var3 == null || !var3.contains("DebugClassRedef")) && this.bean.isDebugClassRedefSet()) {
               var5.setDebugClassRedef(this.bean.getDebugClassRedef());
            }

            if ((var3 == null || !var3.contains("DebugClassSize")) && this.bean.isDebugClassSizeSet()) {
               var5.setDebugClassSize(this.bean.getDebugClassSize());
            }

            if ((var3 == null || !var3.contains("DebugCluster")) && this.bean.isDebugClusterSet()) {
               var5.setDebugCluster(this.bean.getDebugCluster());
            }

            if ((var3 == null || !var3.contains("DebugClusterAnnouncements")) && this.bean.isDebugClusterAnnouncementsSet()) {
               var5.setDebugClusterAnnouncements(this.bean.getDebugClusterAnnouncements());
            }

            if ((var3 == null || !var3.contains("DebugClusterFragments")) && this.bean.isDebugClusterFragmentsSet()) {
               var5.setDebugClusterFragments(this.bean.getDebugClusterFragments());
            }

            if ((var3 == null || !var3.contains("DebugClusterHeartbeats")) && this.bean.isDebugClusterHeartbeatsSet()) {
               var5.setDebugClusterHeartbeats(this.bean.getDebugClusterHeartbeats());
            }

            if ((var3 == null || !var3.contains("DebugConfigurationEdit")) && this.bean.isDebugConfigurationEditSet()) {
               var5.setDebugConfigurationEdit(this.bean.getDebugConfigurationEdit());
            }

            if ((var3 == null || !var3.contains("DebugConfigurationRuntime")) && this.bean.isDebugConfigurationRuntimeSet()) {
               var5.setDebugConfigurationRuntime(this.bean.getDebugConfigurationRuntime());
            }

            if ((var3 == null || !var3.contains("DebugConnectorService")) && this.bean.isDebugConnectorServiceSet()) {
               var5.setDebugConnectorService(this.bean.getDebugConnectorService());
            }

            if ((var3 == null || !var3.contains("DebugConsensusLeasing")) && this.bean.isDebugConsensusLeasingSet()) {
               var5.setDebugConsensusLeasing(this.bean.getDebugConsensusLeasing());
            }

            if ((var3 == null || !var3.contains("DebugDRSCalls")) && this.bean.isDebugDRSCallsSet()) {
               var5.setDebugDRSCalls(this.bean.getDebugDRSCalls());
            }

            if ((var3 == null || !var3.contains("DebugDRSHeartbeats")) && this.bean.isDebugDRSHeartbeatsSet()) {
               var5.setDebugDRSHeartbeats(this.bean.getDebugDRSHeartbeats());
            }

            if ((var3 == null || !var3.contains("DebugDRSMessages")) && this.bean.isDebugDRSMessagesSet()) {
               var5.setDebugDRSMessages(this.bean.getDebugDRSMessages());
            }

            if ((var3 == null || !var3.contains("DebugDRSQueues")) && this.bean.isDebugDRSQueuesSet()) {
               var5.setDebugDRSQueues(this.bean.getDebugDRSQueues());
            }

            if ((var3 == null || !var3.contains("DebugDRSStateTransitions")) && this.bean.isDebugDRSStateTransitionsSet()) {
               var5.setDebugDRSStateTransitions(this.bean.getDebugDRSStateTransitions());
            }

            if ((var3 == null || !var3.contains("DebugDRSUpdateStatus")) && this.bean.isDebugDRSUpdateStatusSet()) {
               var5.setDebugDRSUpdateStatus(this.bean.getDebugDRSUpdateStatus());
            }

            if ((var3 == null || !var3.contains("DebugDeploy")) && this.bean.isDebugDeploySet()) {
               var5.setDebugDeploy(this.bean.getDebugDeploy());
            }

            if ((var3 == null || !var3.contains("DebugDeployment")) && this.bean.isDebugDeploymentSet()) {
               var5.setDebugDeployment(this.bean.getDebugDeployment());
            }

            if ((var3 == null || !var3.contains("DebugDeploymentService")) && this.bean.isDebugDeploymentServiceSet()) {
               var5.setDebugDeploymentService(this.bean.getDebugDeploymentService());
            }

            if ((var3 == null || !var3.contains("DebugDeploymentServiceInternal")) && this.bean.isDebugDeploymentServiceInternalSet()) {
               var5.setDebugDeploymentServiceInternal(this.bean.getDebugDeploymentServiceInternal());
            }

            if ((var3 == null || !var3.contains("DebugDeploymentServiceStatusUpdates")) && this.bean.isDebugDeploymentServiceStatusUpdatesSet()) {
               var5.setDebugDeploymentServiceStatusUpdates(this.bean.getDebugDeploymentServiceStatusUpdates());
            }

            if ((var3 == null || !var3.contains("DebugDeploymentServiceTransport")) && this.bean.isDebugDeploymentServiceTransportSet()) {
               var5.setDebugDeploymentServiceTransport(this.bean.getDebugDeploymentServiceTransport());
            }

            if ((var3 == null || !var3.contains("DebugDeploymentServiceTransportHttp")) && this.bean.isDebugDeploymentServiceTransportHttpSet()) {
               var5.setDebugDeploymentServiceTransportHttp(this.bean.getDebugDeploymentServiceTransportHttp());
            }

            if ((var3 == null || !var3.contains("DebugDescriptor")) && this.bean.isDebugDescriptorSet()) {
               var5.setDebugDescriptor(this.bean.getDebugDescriptor());
            }

            if ((var3 == null || !var3.contains("DebugDiagnosticAccessor")) && this.bean.isDebugDiagnosticAccessorSet()) {
               var5.setDebugDiagnosticAccessor(this.bean.getDebugDiagnosticAccessor());
            }

            if ((var3 == null || !var3.contains("DebugDiagnosticArchive")) && this.bean.isDebugDiagnosticArchiveSet()) {
               var5.setDebugDiagnosticArchive(this.bean.getDebugDiagnosticArchive());
            }

            if ((var3 == null || !var3.contains("DebugDiagnosticArchiveRetirement")) && this.bean.isDebugDiagnosticArchiveRetirementSet()) {
               var5.setDebugDiagnosticArchiveRetirement(this.bean.getDebugDiagnosticArchiveRetirement());
            }

            if ((var3 == null || !var3.contains("DebugDiagnosticCollections")) && this.bean.isDebugDiagnosticCollectionsSet()) {
               var5.setDebugDiagnosticCollections(this.bean.getDebugDiagnosticCollections());
            }

            if ((var3 == null || !var3.contains("DebugDiagnosticContext")) && this.bean.isDebugDiagnosticContextSet()) {
               var5.setDebugDiagnosticContext(this.bean.getDebugDiagnosticContext());
            }

            if ((var3 == null || !var3.contains("DebugDiagnosticDataGathering")) && this.bean.isDebugDiagnosticDataGatheringSet()) {
               var5.setDebugDiagnosticDataGathering(this.bean.getDebugDiagnosticDataGathering());
            }

            if ((var3 == null || !var3.contains("DebugDiagnosticFileArchive")) && this.bean.isDebugDiagnosticFileArchiveSet()) {
               var5.setDebugDiagnosticFileArchive(this.bean.getDebugDiagnosticFileArchive());
            }

            if ((var3 == null || !var3.contains("DebugDiagnosticImage")) && this.bean.isDebugDiagnosticImageSet()) {
               var5.setDebugDiagnosticImage(this.bean.getDebugDiagnosticImage());
            }

            if ((var3 == null || !var3.contains("DebugDiagnosticInstrumentation")) && this.bean.isDebugDiagnosticInstrumentationSet()) {
               var5.setDebugDiagnosticInstrumentation(this.bean.getDebugDiagnosticInstrumentation());
            }

            if ((var3 == null || !var3.contains("DebugDiagnosticInstrumentationActions")) && this.bean.isDebugDiagnosticInstrumentationActionsSet()) {
               var5.setDebugDiagnosticInstrumentationActions(this.bean.getDebugDiagnosticInstrumentationActions());
            }

            if ((var3 == null || !var3.contains("DebugDiagnosticInstrumentationConfig")) && this.bean.isDebugDiagnosticInstrumentationConfigSet()) {
               var5.setDebugDiagnosticInstrumentationConfig(this.bean.getDebugDiagnosticInstrumentationConfig());
            }

            if ((var3 == null || !var3.contains("DebugDiagnosticInstrumentationEvents")) && this.bean.isDebugDiagnosticInstrumentationEventsSet()) {
               var5.setDebugDiagnosticInstrumentationEvents(this.bean.getDebugDiagnosticInstrumentationEvents());
            }

            if ((var3 == null || !var3.contains("DebugDiagnosticInstrumentationWeaving")) && this.bean.isDebugDiagnosticInstrumentationWeavingSet()) {
               var5.setDebugDiagnosticInstrumentationWeaving(this.bean.getDebugDiagnosticInstrumentationWeaving());
            }

            if ((var3 == null || !var3.contains("DebugDiagnosticInstrumentationWeavingMatches")) && this.bean.isDebugDiagnosticInstrumentationWeavingMatchesSet()) {
               var5.setDebugDiagnosticInstrumentationWeavingMatches(this.bean.getDebugDiagnosticInstrumentationWeavingMatches());
            }

            if ((var3 == null || !var3.contains("DebugDiagnosticJdbcArchive")) && this.bean.isDebugDiagnosticJdbcArchiveSet()) {
               var5.setDebugDiagnosticJdbcArchive(this.bean.getDebugDiagnosticJdbcArchive());
            }

            if ((var3 == null || !var3.contains("DebugDiagnosticLifecycleHandlers")) && this.bean.isDebugDiagnosticLifecycleHandlersSet()) {
               var5.setDebugDiagnosticLifecycleHandlers(this.bean.getDebugDiagnosticLifecycleHandlers());
            }

            if ((var3 == null || !var3.contains("DebugDiagnosticQuery")) && this.bean.isDebugDiagnosticQuerySet()) {
               var5.setDebugDiagnosticQuery(this.bean.getDebugDiagnosticQuery());
            }

            if ((var3 == null || !var3.contains("DebugDiagnosticWatch")) && this.bean.isDebugDiagnosticWatchSet()) {
               var5.setDebugDiagnosticWatch(this.bean.getDebugDiagnosticWatch());
            }

            if ((var3 == null || !var3.contains("DebugDiagnosticWlstoreArchive")) && this.bean.isDebugDiagnosticWlstoreArchiveSet()) {
               var5.setDebugDiagnosticWlstoreArchive(this.bean.getDebugDiagnosticWlstoreArchive());
            }

            if ((var3 == null || !var3.contains("DebugDiagnosticsHarvester")) && this.bean.isDebugDiagnosticsHarvesterSet()) {
               var5.setDebugDiagnosticsHarvester(this.bean.getDebugDiagnosticsHarvester());
            }

            if ((var3 == null || !var3.contains("DebugDiagnosticsHarvesterData")) && this.bean.isDebugDiagnosticsHarvesterDataSet()) {
               var5.setDebugDiagnosticsHarvesterData(this.bean.getDebugDiagnosticsHarvesterData());
            }

            if ((var3 == null || !var3.contains("DebugDiagnosticsHarvesterMBeanPlugin")) && this.bean.isDebugDiagnosticsHarvesterMBeanPluginSet()) {
               var5.setDebugDiagnosticsHarvesterMBeanPlugin(this.bean.getDebugDiagnosticsHarvesterMBeanPlugin());
            }

            if ((var3 == null || !var3.contains("DebugDiagnosticsHarvesterTreeBeanPlugin")) && this.bean.isDebugDiagnosticsHarvesterTreeBeanPluginSet()) {
               var5.setDebugDiagnosticsHarvesterTreeBeanPlugin(this.bean.getDebugDiagnosticsHarvesterTreeBeanPlugin());
            }

            if ((var3 == null || !var3.contains("DebugDiagnosticsModule")) && this.bean.isDebugDiagnosticsModuleSet()) {
               var5.setDebugDiagnosticsModule(this.bean.getDebugDiagnosticsModule());
            }

            if ((var3 == null || !var3.contains("DebugDomainLogHandler")) && this.bean.isDebugDomainLogHandlerSet()) {
               var5.setDebugDomainLogHandler(this.bean.getDebugDomainLogHandler());
            }

            if ((var3 == null || !var3.contains("DebugEjbCaching")) && this.bean.isDebugEjbCachingSet()) {
               var5.setDebugEjbCaching(this.bean.getDebugEjbCaching());
            }

            if ((var3 == null || !var3.contains("DebugEjbCmpDeployment")) && this.bean.isDebugEjbCmpDeploymentSet()) {
               var5.setDebugEjbCmpDeployment(this.bean.getDebugEjbCmpDeployment());
            }

            if ((var3 == null || !var3.contains("DebugEjbCmpRuntime")) && this.bean.isDebugEjbCmpRuntimeSet()) {
               var5.setDebugEjbCmpRuntime(this.bean.getDebugEjbCmpRuntime());
            }

            if ((var3 == null || !var3.contains("DebugEjbCompilation")) && this.bean.isDebugEjbCompilationSet()) {
               var5.setDebugEjbCompilation(this.bean.getDebugEjbCompilation());
            }

            if ((var3 == null || !var3.contains("DebugEjbDeployment")) && this.bean.isDebugEjbDeploymentSet()) {
               var5.setDebugEjbDeployment(this.bean.getDebugEjbDeployment());
            }

            if ((var3 == null || !var3.contains("DebugEjbInvoke")) && this.bean.isDebugEjbInvokeSet()) {
               var5.setDebugEjbInvoke(this.bean.getDebugEjbInvoke());
            }

            if ((var3 == null || !var3.contains("DebugEjbLocking")) && this.bean.isDebugEjbLockingSet()) {
               var5.setDebugEjbLocking(this.bean.getDebugEjbLocking());
            }

            if ((var3 == null || !var3.contains("DebugEjbMdbConnection")) && this.bean.isDebugEjbMdbConnectionSet()) {
               var5.setDebugEjbMdbConnection(this.bean.getDebugEjbMdbConnection());
            }

            if ((var3 == null || !var3.contains("DebugEjbPooling")) && this.bean.isDebugEjbPoolingSet()) {
               var5.setDebugEjbPooling(this.bean.getDebugEjbPooling());
            }

            if ((var3 == null || !var3.contains("DebugEjbSecurity")) && this.bean.isDebugEjbSecuritySet()) {
               var5.setDebugEjbSecurity(this.bean.getDebugEjbSecurity());
            }

            if ((var3 == null || !var3.contains("DebugEjbSwapping")) && this.bean.isDebugEjbSwappingSet()) {
               var5.setDebugEjbSwapping(this.bean.getDebugEjbSwapping());
            }

            if ((var3 == null || !var3.contains("DebugEjbTimers")) && this.bean.isDebugEjbTimersSet()) {
               var5.setDebugEjbTimers(this.bean.getDebugEjbTimers());
            }

            if ((var3 == null || !var3.contains("DebugEmbeddedLDAP")) && this.bean.isDebugEmbeddedLDAPSet()) {
               var5.setDebugEmbeddedLDAP(this.bean.getDebugEmbeddedLDAP());
            }

            if ((var3 == null || !var3.contains("DebugEmbeddedLDAPLogLevel")) && this.bean.isDebugEmbeddedLDAPLogLevelSet()) {
               var5.setDebugEmbeddedLDAPLogLevel(this.bean.getDebugEmbeddedLDAPLogLevel());
            }

            if ((var3 == null || !var3.contains("DebugEmbeddedLDAPLogToConsole")) && this.bean.isDebugEmbeddedLDAPLogToConsoleSet()) {
               var5.setDebugEmbeddedLDAPLogToConsole(this.bean.getDebugEmbeddedLDAPLogToConsole());
            }

            if ((var3 == null || !var3.contains("DebugEmbeddedLDAPWriteOverrideProps")) && this.bean.isDebugEmbeddedLDAPWriteOverridePropsSet()) {
               var5.setDebugEmbeddedLDAPWriteOverrideProps(this.bean.getDebugEmbeddedLDAPWriteOverrideProps());
            }

            if ((var3 == null || !var3.contains("DebugEventManager")) && this.bean.isDebugEventManagerSet()) {
               var5.setDebugEventManager(this.bean.getDebugEventManager());
            }

            if ((var3 == null || !var3.contains("DebugFileDistributionServlet")) && this.bean.isDebugFileDistributionServletSet()) {
               var5.setDebugFileDistributionServlet(this.bean.getDebugFileDistributionServlet());
            }

            if ((var3 == null || !var3.contains("DebugHttp")) && this.bean.isDebugHttpSet()) {
               var5.setDebugHttp(this.bean.getDebugHttp());
            }

            if ((var3 == null || !var3.contains("DebugHttpLogging")) && this.bean.isDebugHttpLoggingSet()) {
               var5.setDebugHttpLogging(this.bean.getDebugHttpLogging());
            }

            if ((var3 == null || !var3.contains("DebugHttpSessions")) && this.bean.isDebugHttpSessionsSet()) {
               var5.setDebugHttpSessions(this.bean.getDebugHttpSessions());
            }

            if ((var3 == null || !var3.contains("DebugIIOPNaming")) && this.bean.isDebugIIOPNamingSet()) {
               var5.setDebugIIOPNaming(this.bean.getDebugIIOPNaming());
            }

            if ((var3 == null || !var3.contains("DebugIIOPTunneling")) && this.bean.isDebugIIOPTunnelingSet()) {
               var5.setDebugIIOPTunneling(this.bean.getDebugIIOPTunneling());
            }

            if ((var3 == null || !var3.contains("DebugJ2EEManagement")) && this.bean.isDebugJ2EEManagementSet()) {
               var5.setDebugJ2EEManagement(this.bean.getDebugJ2EEManagement());
            }

            if ((var3 == null || !var3.contains("DebugJAXPDebugLevel")) && this.bean.isDebugJAXPDebugLevelSet()) {
               var5.setDebugJAXPDebugLevel(this.bean.getDebugJAXPDebugLevel());
            }

            if ((var3 == null || !var3.contains("DebugJAXPDebugName")) && this.bean.isDebugJAXPDebugNameSet()) {
               var5.setDebugJAXPDebugName(this.bean.getDebugJAXPDebugName());
            }

            if ((var3 == null || !var3.contains("DebugJAXPIncludeClass")) && this.bean.isDebugJAXPIncludeClassSet()) {
               var5.setDebugJAXPIncludeClass(this.bean.getDebugJAXPIncludeClass());
            }

            if ((var3 == null || !var3.contains("DebugJAXPIncludeLocation")) && this.bean.isDebugJAXPIncludeLocationSet()) {
               var5.setDebugJAXPIncludeLocation(this.bean.getDebugJAXPIncludeLocation());
            }

            if ((var3 == null || !var3.contains("DebugJAXPIncludeName")) && this.bean.isDebugJAXPIncludeNameSet()) {
               var5.setDebugJAXPIncludeName(this.bean.getDebugJAXPIncludeName());
            }

            if ((var3 == null || !var3.contains("DebugJAXPIncludeTime")) && this.bean.isDebugJAXPIncludeTimeSet()) {
               var5.setDebugJAXPIncludeTime(this.bean.getDebugJAXPIncludeTime());
            }

            if ((var3 == null || !var3.contains("DebugJAXPUseShortClass")) && this.bean.isDebugJAXPUseShortClassSet()) {
               var5.setDebugJAXPUseShortClass(this.bean.getDebugJAXPUseShortClass());
            }

            if ((var3 == null || !var3.contains("DebugJDBCConn")) && this.bean.isDebugJDBCConnSet()) {
               var5.setDebugJDBCConn(this.bean.getDebugJDBCConn());
            }

            if ((var3 == null || !var3.contains("DebugJDBCDriverLogging")) && this.bean.isDebugJDBCDriverLoggingSet()) {
               var5.setDebugJDBCDriverLogging(this.bean.getDebugJDBCDriverLogging());
            }

            if ((var3 == null || !var3.contains("DebugJDBCInternal")) && this.bean.isDebugJDBCInternalSet()) {
               var5.setDebugJDBCInternal(this.bean.getDebugJDBCInternal());
            }

            if ((var3 == null || !var3.contains("DebugJDBCONS")) && this.bean.isDebugJDBCONSSet()) {
               var5.setDebugJDBCONS(this.bean.getDebugJDBCONS());
            }

            if ((var3 == null || !var3.contains("DebugJDBCRAC")) && this.bean.isDebugJDBCRACSet()) {
               var5.setDebugJDBCRAC(this.bean.getDebugJDBCRAC());
            }

            if ((var3 == null || !var3.contains("DebugJDBCREPLAY")) && this.bean.isDebugJDBCREPLAYSet()) {
               var5.setDebugJDBCREPLAY(this.bean.getDebugJDBCREPLAY());
            }

            if ((var3 == null || !var3.contains("DebugJDBCRMI")) && this.bean.isDebugJDBCRMISet()) {
               var5.setDebugJDBCRMI(this.bean.getDebugJDBCRMI());
            }

            if ((var3 == null || !var3.contains("DebugJDBCSQL")) && this.bean.isDebugJDBCSQLSet()) {
               var5.setDebugJDBCSQL(this.bean.getDebugJDBCSQL());
            }

            if ((var3 == null || !var3.contains("DebugJDBCUCP")) && this.bean.isDebugJDBCUCPSet()) {
               var5.setDebugJDBCUCP(this.bean.getDebugJDBCUCP());
            }

            if ((var3 == null || !var3.contains("DebugJMSAME")) && this.bean.isDebugJMSAMESet()) {
               var5.setDebugJMSAME(this.bean.getDebugJMSAME());
            }

            if ((var3 == null || !var3.contains("DebugJMSBackEnd")) && this.bean.isDebugJMSBackEndSet()) {
               var5.setDebugJMSBackEnd(this.bean.getDebugJMSBackEnd());
            }

            if ((var3 == null || !var3.contains("DebugJMSBoot")) && this.bean.isDebugJMSBootSet()) {
               var5.setDebugJMSBoot(this.bean.getDebugJMSBoot());
            }

            if ((var3 == null || !var3.contains("DebugJMSCDS")) && this.bean.isDebugJMSCDSSet()) {
               var5.setDebugJMSCDS(this.bean.getDebugJMSCDS());
            }

            if ((var3 == null || !var3.contains("DebugJMSCommon")) && this.bean.isDebugJMSCommonSet()) {
               var5.setDebugJMSCommon(this.bean.getDebugJMSCommon());
            }

            if ((var3 == null || !var3.contains("DebugJMSConfig")) && this.bean.isDebugJMSConfigSet()) {
               var5.setDebugJMSConfig(this.bean.getDebugJMSConfig());
            }

            if ((var3 == null || !var3.contains("DebugJMSDispatcher")) && this.bean.isDebugJMSDispatcherSet()) {
               var5.setDebugJMSDispatcher(this.bean.getDebugJMSDispatcher());
            }

            if ((var3 == null || !var3.contains("DebugJMSDistTopic")) && this.bean.isDebugJMSDistTopicSet()) {
               var5.setDebugJMSDistTopic(this.bean.getDebugJMSDistTopic());
            }

            if ((var3 == null || !var3.contains("DebugJMSDurableSubscribers")) && this.bean.isDebugJMSDurableSubscribersSet()) {
               var5.setDebugJMSDurableSubscribers(this.bean.getDebugJMSDurableSubscribers());
            }

            if ((var3 == null || !var3.contains("DebugJMSFrontEnd")) && this.bean.isDebugJMSFrontEndSet()) {
               var5.setDebugJMSFrontEnd(this.bean.getDebugJMSFrontEnd());
            }

            if ((var3 == null || !var3.contains("DebugJMSJDBCScavengeOnFlush")) && this.bean.isDebugJMSJDBCScavengeOnFlushSet()) {
               var5.setDebugJMSJDBCScavengeOnFlush(this.bean.getDebugJMSJDBCScavengeOnFlush());
            }

            if ((var3 == null || !var3.contains("DebugJMSLocking")) && this.bean.isDebugJMSLockingSet()) {
               var5.setDebugJMSLocking(this.bean.getDebugJMSLocking());
            }

            if ((var3 == null || !var3.contains("DebugJMSMessagePath")) && this.bean.isDebugJMSMessagePathSet()) {
               var5.setDebugJMSMessagePath(this.bean.getDebugJMSMessagePath());
            }

            if ((var3 == null || !var3.contains("DebugJMSModule")) && this.bean.isDebugJMSModuleSet()) {
               var5.setDebugJMSModule(this.bean.getDebugJMSModule());
            }

            if ((var3 == null || !var3.contains("DebugJMSPauseResume")) && this.bean.isDebugJMSPauseResumeSet()) {
               var5.setDebugJMSPauseResume(this.bean.getDebugJMSPauseResume());
            }

            if ((var3 == null || !var3.contains("DebugJMSSAF")) && this.bean.isDebugJMSSAFSet()) {
               var5.setDebugJMSSAF(this.bean.getDebugJMSSAF());
            }

            if ((var3 == null || !var3.contains("DebugJMSStore")) && this.bean.isDebugJMSStoreSet()) {
               var5.setDebugJMSStore(this.bean.getDebugJMSStore());
            }

            if ((var3 == null || !var3.contains("DebugJMST3Server")) && this.bean.isDebugJMST3ServerSet()) {
               var5.setDebugJMST3Server(this.bean.getDebugJMST3Server());
            }

            if ((var3 == null || !var3.contains("DebugJMSWrappers")) && this.bean.isDebugJMSWrappersSet()) {
               var5.setDebugJMSWrappers(this.bean.getDebugJMSWrappers());
            }

            if ((var3 == null || !var3.contains("DebugJMSXA")) && this.bean.isDebugJMSXASet()) {
               var5.setDebugJMSXA(this.bean.getDebugJMSXA());
            }

            if ((var3 == null || !var3.contains("DebugJMX")) && this.bean.isDebugJMXSet()) {
               var5.setDebugJMX(this.bean.getDebugJMX());
            }

            if ((var3 == null || !var3.contains("DebugJMXCompatibility")) && this.bean.isDebugJMXCompatibilitySet()) {
               var5.setDebugJMXCompatibility(this.bean.getDebugJMXCompatibility());
            }

            if ((var3 == null || !var3.contains("DebugJMXCore")) && this.bean.isDebugJMXCoreSet()) {
               var5.setDebugJMXCore(this.bean.getDebugJMXCore());
            }

            if ((var3 == null || !var3.contains("DebugJMXDomain")) && this.bean.isDebugJMXDomainSet()) {
               var5.setDebugJMXDomain(this.bean.getDebugJMXDomain());
            }

            if ((var3 == null || !var3.contains("DebugJMXEdit")) && this.bean.isDebugJMXEditSet()) {
               var5.setDebugJMXEdit(this.bean.getDebugJMXEdit());
            }

            if ((var3 == null || !var3.contains("DebugJMXRuntime")) && this.bean.isDebugJMXRuntimeSet()) {
               var5.setDebugJMXRuntime(this.bean.getDebugJMXRuntime());
            }

            if ((var3 == null || !var3.contains("DebugJNDI")) && this.bean.isDebugJNDISet()) {
               var5.setDebugJNDI(this.bean.getDebugJNDI());
            }

            if ((var3 == null || !var3.contains("DebugJNDIFactories")) && this.bean.isDebugJNDIFactoriesSet()) {
               var5.setDebugJNDIFactories(this.bean.getDebugJNDIFactories());
            }

            if ((var3 == null || !var3.contains("DebugJNDIResolution")) && this.bean.isDebugJNDIResolutionSet()) {
               var5.setDebugJNDIResolution(this.bean.getDebugJNDIResolution());
            }

            if ((var3 == null || !var3.contains("DebugJTA2PC")) && this.bean.isDebugJTA2PCSet()) {
               var5.setDebugJTA2PC(this.bean.getDebugJTA2PC());
            }

            if ((var3 == null || !var3.contains("DebugJTA2PCStackTrace")) && this.bean.isDebugJTA2PCStackTraceSet()) {
               var5.setDebugJTA2PCStackTrace(this.bean.getDebugJTA2PCStackTrace());
            }

            if ((var3 == null || !var3.contains("DebugJTAAPI")) && this.bean.isDebugJTAAPISet()) {
               var5.setDebugJTAAPI(this.bean.getDebugJTAAPI());
            }

            if ((var3 == null || !var3.contains("DebugJTAGateway")) && this.bean.isDebugJTAGatewaySet()) {
               var5.setDebugJTAGateway(this.bean.getDebugJTAGateway());
            }

            if ((var3 == null || !var3.contains("DebugJTAGatewayStackTrace")) && this.bean.isDebugJTAGatewayStackTraceSet()) {
               var5.setDebugJTAGatewayStackTrace(this.bean.getDebugJTAGatewayStackTrace());
            }

            if ((var3 == null || !var3.contains("DebugJTAHealth")) && this.bean.isDebugJTAHealthSet()) {
               var5.setDebugJTAHealth(this.bean.getDebugJTAHealth());
            }

            if ((var3 == null || !var3.contains("DebugJTAJDBC")) && this.bean.isDebugJTAJDBCSet()) {
               var5.setDebugJTAJDBC(this.bean.getDebugJTAJDBC());
            }

            if ((var3 == null || !var3.contains("DebugJTALLR")) && this.bean.isDebugJTALLRSet()) {
               var5.setDebugJTALLR(this.bean.getDebugJTALLR());
            }

            if ((var3 == null || !var3.contains("DebugJTALifecycle")) && this.bean.isDebugJTALifecycleSet()) {
               var5.setDebugJTALifecycle(this.bean.getDebugJTALifecycle());
            }

            if ((var3 == null || !var3.contains("DebugJTAMigration")) && this.bean.isDebugJTAMigrationSet()) {
               var5.setDebugJTAMigration(this.bean.getDebugJTAMigration());
            }

            if ((var3 == null || !var3.contains("DebugJTANaming")) && this.bean.isDebugJTANamingSet()) {
               var5.setDebugJTANaming(this.bean.getDebugJTANaming());
            }

            if ((var3 == null || !var3.contains("DebugJTANamingStackTrace")) && this.bean.isDebugJTANamingStackTraceSet()) {
               var5.setDebugJTANamingStackTrace(this.bean.getDebugJTANamingStackTrace());
            }

            if ((var3 == null || !var3.contains("DebugJTANonXA")) && this.bean.isDebugJTANonXASet()) {
               var5.setDebugJTANonXA(this.bean.getDebugJTANonXA());
            }

            if ((var3 == null || !var3.contains("DebugJTAPropagate")) && this.bean.isDebugJTAPropagateSet()) {
               var5.setDebugJTAPropagate(this.bean.getDebugJTAPropagate());
            }

            if ((var3 == null || !var3.contains("DebugJTARMI")) && this.bean.isDebugJTARMISet()) {
               var5.setDebugJTARMI(this.bean.getDebugJTARMI());
            }

            if ((var3 == null || !var3.contains("DebugJTARecovery")) && this.bean.isDebugJTARecoverySet()) {
               var5.setDebugJTARecovery(this.bean.getDebugJTARecovery());
            }

            if ((var3 == null || !var3.contains("DebugJTARecoveryStackTrace")) && this.bean.isDebugJTARecoveryStackTraceSet()) {
               var5.setDebugJTARecoveryStackTrace(this.bean.getDebugJTARecoveryStackTrace());
            }

            if ((var3 == null || !var3.contains("DebugJTAResourceHealth")) && this.bean.isDebugJTAResourceHealthSet()) {
               var5.setDebugJTAResourceHealth(this.bean.getDebugJTAResourceHealth());
            }

            if ((var3 == null || !var3.contains("DebugJTAResourceName")) && this.bean.isDebugJTAResourceNameSet()) {
               var5.setDebugJTAResourceName(this.bean.getDebugJTAResourceName());
            }

            if ((var3 == null || !var3.contains("DebugJTATLOG")) && this.bean.isDebugJTATLOGSet()) {
               var5.setDebugJTATLOG(this.bean.getDebugJTATLOG());
            }

            if ((var3 == null || !var3.contains("DebugJTATransactionName")) && this.bean.isDebugJTATransactionNameSet()) {
               var5.setDebugJTATransactionName(this.bean.getDebugJTATransactionName());
            }

            if ((var3 == null || !var3.contains("DebugJTAXA")) && this.bean.isDebugJTAXASet()) {
               var5.setDebugJTAXA(this.bean.getDebugJTAXA());
            }

            if ((var3 == null || !var3.contains("DebugJTAXAStackTrace")) && this.bean.isDebugJTAXAStackTraceSet()) {
               var5.setDebugJTAXAStackTrace(this.bean.getDebugJTAXAStackTrace());
            }

            if ((var3 == null || !var3.contains("DebugJpaDataCache")) && this.bean.isDebugJpaDataCacheSet()) {
               var5.setDebugJpaDataCache(this.bean.getDebugJpaDataCache());
            }

            if ((var3 == null || !var3.contains("DebugJpaEnhance")) && this.bean.isDebugJpaEnhanceSet()) {
               var5.setDebugJpaEnhance(this.bean.getDebugJpaEnhance());
            }

            if ((var3 == null || !var3.contains("DebugJpaJdbcJdbc")) && this.bean.isDebugJpaJdbcJdbcSet()) {
               var5.setDebugJpaJdbcJdbc(this.bean.getDebugJpaJdbcJdbc());
            }

            if ((var3 == null || !var3.contains("DebugJpaJdbcSchema")) && this.bean.isDebugJpaJdbcSchemaSet()) {
               var5.setDebugJpaJdbcSchema(this.bean.getDebugJpaJdbcSchema());
            }

            if ((var3 == null || !var3.contains("DebugJpaJdbcSql")) && this.bean.isDebugJpaJdbcSqlSet()) {
               var5.setDebugJpaJdbcSql(this.bean.getDebugJpaJdbcSql());
            }

            if ((var3 == null || !var3.contains("DebugJpaManage")) && this.bean.isDebugJpaManageSet()) {
               var5.setDebugJpaManage(this.bean.getDebugJpaManage());
            }

            if ((var3 == null || !var3.contains("DebugJpaMetaData")) && this.bean.isDebugJpaMetaDataSet()) {
               var5.setDebugJpaMetaData(this.bean.getDebugJpaMetaData());
            }

            if ((var3 == null || !var3.contains("DebugJpaProfile")) && this.bean.isDebugJpaProfileSet()) {
               var5.setDebugJpaProfile(this.bean.getDebugJpaProfile());
            }

            if ((var3 == null || !var3.contains("DebugJpaQuery")) && this.bean.isDebugJpaQuerySet()) {
               var5.setDebugJpaQuery(this.bean.getDebugJpaQuery());
            }

            if ((var3 == null || !var3.contains("DebugJpaRuntime")) && this.bean.isDebugJpaRuntimeSet()) {
               var5.setDebugJpaRuntime(this.bean.getDebugJpaRuntime());
            }

            if ((var3 == null || !var3.contains("DebugJpaTool")) && this.bean.isDebugJpaToolSet()) {
               var5.setDebugJpaTool(this.bean.getDebugJpaTool());
            }

            if ((var3 == null || !var3.contains("DebugLeaderElection")) && this.bean.isDebugLeaderElectionSet()) {
               var5.setDebugLeaderElection(this.bean.getDebugLeaderElection());
            }

            if ((var3 == null || !var3.contains("DebugLibraries")) && this.bean.isDebugLibrariesSet()) {
               var5.setDebugLibraries(this.bean.getDebugLibraries());
            }

            if ((var3 == null || !var3.contains("DebugLoggingConfiguration")) && this.bean.isDebugLoggingConfigurationSet()) {
               var5.setDebugLoggingConfiguration(this.bean.getDebugLoggingConfiguration());
            }

            if ((var3 == null || !var3.contains("DebugManagementServicesResource")) && this.bean.isDebugManagementServicesResourceSet()) {
               var5.setDebugManagementServicesResource(this.bean.getDebugManagementServicesResource());
            }

            if ((var3 == null || !var3.contains("DebugMaskCriterias")) && this.bean.isDebugMaskCriteriasSet()) {
               String[] var4 = this.bean.getDebugMaskCriterias();
               var5.setDebugMaskCriterias(var4 == null ? null : (String[])((String[])((String[])((String[])var4)).clone()));
            }

            if ((var3 == null || !var3.contains("DebugMessagingBridgeDumpToConsole")) && this.bean.isDebugMessagingBridgeDumpToConsoleSet()) {
               var5.setDebugMessagingBridgeDumpToConsole(this.bean.getDebugMessagingBridgeDumpToConsole());
            }

            if ((var3 == null || !var3.contains("DebugMessagingBridgeDumpToLog")) && this.bean.isDebugMessagingBridgeDumpToLogSet()) {
               var5.setDebugMessagingBridgeDumpToLog(this.bean.getDebugMessagingBridgeDumpToLog());
            }

            if ((var3 == null || !var3.contains("DebugMessagingBridgeRuntime")) && this.bean.isDebugMessagingBridgeRuntimeSet()) {
               var5.setDebugMessagingBridgeRuntime(this.bean.getDebugMessagingBridgeRuntime());
            }

            if ((var3 == null || !var3.contains("DebugMessagingBridgeRuntimeVerbose")) && this.bean.isDebugMessagingBridgeRuntimeVerboseSet()) {
               var5.setDebugMessagingBridgeRuntimeVerbose(this.bean.getDebugMessagingBridgeRuntimeVerbose());
            }

            if ((var3 == null || !var3.contains("DebugMessagingBridgeStartup")) && this.bean.isDebugMessagingBridgeStartupSet()) {
               var5.setDebugMessagingBridgeStartup(this.bean.getDebugMessagingBridgeStartup());
            }

            if ((var3 == null || !var3.contains("DebugMessagingKernel")) && this.bean.isDebugMessagingKernelSet()) {
               var5.setDebugMessagingKernel(this.bean.getDebugMessagingKernel());
            }

            if ((var3 == null || !var3.contains("DebugMessagingKernelBoot")) && this.bean.isDebugMessagingKernelBootSet()) {
               var5.setDebugMessagingKernelBoot(this.bean.getDebugMessagingKernelBoot());
            }

            if ((var3 == null || !var3.contains("DebugPathSvc")) && this.bean.isDebugPathSvcSet()) {
               var5.setDebugPathSvc(this.bean.getDebugPathSvc());
            }

            if ((var3 == null || !var3.contains("DebugPathSvcVerbose")) && this.bean.isDebugPathSvcVerboseSet()) {
               var5.setDebugPathSvcVerbose(this.bean.getDebugPathSvcVerbose());
            }

            if ((var3 == null || !var3.contains("DebugRA")) && this.bean.isDebugRASet()) {
               var5.setDebugRA(this.bean.getDebugRA());
            }

            if ((var3 == null || !var3.contains("DebugRAClassloader")) && this.bean.isDebugRAClassloaderSet()) {
               var5.setDebugRAClassloader(this.bean.getDebugRAClassloader());
            }

            if ((var3 == null || !var3.contains("DebugRAConnEvents")) && this.bean.isDebugRAConnEventsSet()) {
               var5.setDebugRAConnEvents(this.bean.getDebugRAConnEvents());
            }

            if ((var3 == null || !var3.contains("DebugRAConnections")) && this.bean.isDebugRAConnectionsSet()) {
               var5.setDebugRAConnections(this.bean.getDebugRAConnections());
            }

            if ((var3 == null || !var3.contains("DebugRADeployment")) && this.bean.isDebugRADeploymentSet()) {
               var5.setDebugRADeployment(this.bean.getDebugRADeployment());
            }

            if ((var3 == null || !var3.contains("DebugRALifecycle")) && this.bean.isDebugRALifecycleSet()) {
               var5.setDebugRALifecycle(this.bean.getDebugRALifecycle());
            }

            if ((var3 == null || !var3.contains("DebugRALocalOut")) && this.bean.isDebugRALocalOutSet()) {
               var5.setDebugRALocalOut(this.bean.getDebugRALocalOut());
            }

            if ((var3 == null || !var3.contains("DebugRAParsing")) && this.bean.isDebugRAParsingSet()) {
               var5.setDebugRAParsing(this.bean.getDebugRAParsing());
            }

            if ((var3 == null || !var3.contains("DebugRAPoolVerbose")) && this.bean.isDebugRAPoolVerboseSet()) {
               var5.setDebugRAPoolVerbose(this.bean.getDebugRAPoolVerbose());
            }

            if ((var3 == null || !var3.contains("DebugRAPooling")) && this.bean.isDebugRAPoolingSet()) {
               var5.setDebugRAPooling(this.bean.getDebugRAPooling());
            }

            if ((var3 == null || !var3.contains("DebugRASecurityCtx")) && this.bean.isDebugRASecurityCtxSet()) {
               var5.setDebugRASecurityCtx(this.bean.getDebugRASecurityCtx());
            }

            if ((var3 == null || !var3.contains("DebugRAWork")) && this.bean.isDebugRAWorkSet()) {
               var5.setDebugRAWork(this.bean.getDebugRAWork());
            }

            if ((var3 == null || !var3.contains("DebugRAWorkEvents")) && this.bean.isDebugRAWorkEventsSet()) {
               var5.setDebugRAWorkEvents(this.bean.getDebugRAWorkEvents());
            }

            if ((var3 == null || !var3.contains("DebugRAXAin")) && this.bean.isDebugRAXAinSet()) {
               var5.setDebugRAXAin(this.bean.getDebugRAXAin());
            }

            if ((var3 == null || !var3.contains("DebugRAXAout")) && this.bean.isDebugRAXAoutSet()) {
               var5.setDebugRAXAout(this.bean.getDebugRAXAout());
            }

            if ((var3 == null || !var3.contains("DebugRAXAwork")) && this.bean.isDebugRAXAworkSet()) {
               var5.setDebugRAXAwork(this.bean.getDebugRAXAwork());
            }

            if ((var3 == null || !var3.contains("DebugReplication")) && this.bean.isDebugReplicationSet()) {
               var5.setDebugReplication(this.bean.getDebugReplication());
            }

            if ((var3 == null || !var3.contains("DebugReplicationDetails")) && this.bean.isDebugReplicationDetailsSet()) {
               var5.setDebugReplicationDetails(this.bean.getDebugReplicationDetails());
            }

            if ((var3 == null || !var3.contains("DebugSAFAdmin")) && this.bean.isDebugSAFAdminSet()) {
               var5.setDebugSAFAdmin(this.bean.getDebugSAFAdmin());
            }

            if ((var3 == null || !var3.contains("DebugSAFLifeCycle")) && this.bean.isDebugSAFLifeCycleSet()) {
               var5.setDebugSAFLifeCycle(this.bean.getDebugSAFLifeCycle());
            }

            if ((var3 == null || !var3.contains("DebugSAFManager")) && this.bean.isDebugSAFManagerSet()) {
               var5.setDebugSAFManager(this.bean.getDebugSAFManager());
            }

            if ((var3 == null || !var3.contains("DebugSAFMessagePath")) && this.bean.isDebugSAFMessagePathSet()) {
               var5.setDebugSAFMessagePath(this.bean.getDebugSAFMessagePath());
            }

            if ((var3 == null || !var3.contains("DebugSAFReceivingAgent")) && this.bean.isDebugSAFReceivingAgentSet()) {
               var5.setDebugSAFReceivingAgent(this.bean.getDebugSAFReceivingAgent());
            }

            if ((var3 == null || !var3.contains("DebugSAFSendingAgent")) && this.bean.isDebugSAFSendingAgentSet()) {
               var5.setDebugSAFSendingAgent(this.bean.getDebugSAFSendingAgent());
            }

            if ((var3 == null || !var3.contains("DebugSAFStore")) && this.bean.isDebugSAFStoreSet()) {
               var5.setDebugSAFStore(this.bean.getDebugSAFStore());
            }

            if ((var3 == null || !var3.contains("DebugSAFTransport")) && this.bean.isDebugSAFTransportSet()) {
               var5.setDebugSAFTransport(this.bean.getDebugSAFTransport());
            }

            if ((var3 == null || !var3.contains("DebugSAFVerbose")) && this.bean.isDebugSAFVerboseSet()) {
               var5.setDebugSAFVerbose(this.bean.getDebugSAFVerbose());
            }

            if ((var3 == null || !var3.contains("DebugSNMPAgent")) && this.bean.isDebugSNMPAgentSet()) {
               var5.setDebugSNMPAgent(this.bean.getDebugSNMPAgent());
            }

            if ((var3 == null || !var3.contains("DebugSNMPExtensionProvider")) && this.bean.isDebugSNMPExtensionProviderSet()) {
               var5.setDebugSNMPExtensionProvider(this.bean.getDebugSNMPExtensionProvider());
            }

            if ((var3 == null || !var3.contains("DebugSNMPProtocolTCP")) && this.bean.isDebugSNMPProtocolTCPSet()) {
               var5.setDebugSNMPProtocolTCP(this.bean.getDebugSNMPProtocolTCP());
            }

            if ((var3 == null || !var3.contains("DebugSNMPToolkit")) && this.bean.isDebugSNMPToolkitSet()) {
               var5.setDebugSNMPToolkit(this.bean.getDebugSNMPToolkit());
            }

            if ((var3 == null || !var3.contains("DebugScaContainer")) && this.bean.isDebugScaContainerSet()) {
               var5.setDebugScaContainer(this.bean.getDebugScaContainer());
            }

            if ((var3 == null || !var3.contains("DebugSecurity")) && this.bean.isDebugSecuritySet()) {
               var5.setDebugSecurity(this.bean.getDebugSecurity());
            }

            if ((var3 == null || !var3.contains("DebugSecurityAdjudicator")) && this.bean.isDebugSecurityAdjudicatorSet()) {
               var5.setDebugSecurityAdjudicator(this.bean.getDebugSecurityAdjudicator());
            }

            if ((var3 == null || !var3.contains("DebugSecurityAtn")) && this.bean.isDebugSecurityAtnSet()) {
               var5.setDebugSecurityAtn(this.bean.getDebugSecurityAtn());
            }

            if ((var3 == null || !var3.contains("DebugSecurityAtz")) && this.bean.isDebugSecurityAtzSet()) {
               var5.setDebugSecurityAtz(this.bean.getDebugSecurityAtz());
            }

            if ((var3 == null || !var3.contains("DebugSecurityAuditor")) && this.bean.isDebugSecurityAuditorSet()) {
               var5.setDebugSecurityAuditor(this.bean.getDebugSecurityAuditor());
            }

            if ((var3 == null || !var3.contains("DebugSecurityCertPath")) && this.bean.isDebugSecurityCertPathSet()) {
               var5.setDebugSecurityCertPath(this.bean.getDebugSecurityCertPath());
            }

            if ((var3 == null || !var3.contains("DebugSecurityCredMap")) && this.bean.isDebugSecurityCredMapSet()) {
               var5.setDebugSecurityCredMap(this.bean.getDebugSecurityCredMap());
            }

            if ((var3 == null || !var3.contains("DebugSecurityEEngine")) && this.bean.isDebugSecurityEEngineSet()) {
               var5.setDebugSecurityEEngine(this.bean.getDebugSecurityEEngine());
            }

            if ((var3 == null || !var3.contains("DebugSecurityEncryptionService")) && this.bean.isDebugSecurityEncryptionServiceSet()) {
               var5.setDebugSecurityEncryptionService(this.bean.getDebugSecurityEncryptionService());
            }

            if ((var3 == null || !var3.contains("DebugSecurityJACC")) && this.bean.isDebugSecurityJACCSet()) {
               var5.setDebugSecurityJACC(this.bean.getDebugSecurityJACC());
            }

            if ((var3 == null || !var3.contains("DebugSecurityJACCNonPolicy")) && this.bean.isDebugSecurityJACCNonPolicySet()) {
               var5.setDebugSecurityJACCNonPolicy(this.bean.getDebugSecurityJACCNonPolicy());
            }

            if ((var3 == null || !var3.contains("DebugSecurityJACCPolicy")) && this.bean.isDebugSecurityJACCPolicySet()) {
               var5.setDebugSecurityJACCPolicy(this.bean.getDebugSecurityJACCPolicy());
            }

            if ((var3 == null || !var3.contains("DebugSecurityKeyStore")) && this.bean.isDebugSecurityKeyStoreSet()) {
               var5.setDebugSecurityKeyStore(this.bean.getDebugSecurityKeyStore());
            }

            if ((var3 == null || !var3.contains("DebugSecurityPasswordPolicy")) && this.bean.isDebugSecurityPasswordPolicySet()) {
               var5.setDebugSecurityPasswordPolicy(this.bean.getDebugSecurityPasswordPolicy());
            }

            if ((var3 == null || !var3.contains("DebugSecurityPredicate")) && this.bean.isDebugSecurityPredicateSet()) {
               var5.setDebugSecurityPredicate(this.bean.getDebugSecurityPredicate());
            }

            if (var2 && (var3 == null || !var3.contains("DebugSecurityProfiler")) && this.bean.isDebugSecurityProfilerSet()) {
               var5.setDebugSecurityProfiler(this.bean.getDebugSecurityProfiler());
            }

            if ((var3 == null || !var3.contains("DebugSecurityRealm")) && this.bean.isDebugSecurityRealmSet()) {
               var5.setDebugSecurityRealm(this.bean.getDebugSecurityRealm());
            }

            if ((var3 == null || !var3.contains("DebugSecurityRoleMap")) && this.bean.isDebugSecurityRoleMapSet()) {
               var5.setDebugSecurityRoleMap(this.bean.getDebugSecurityRoleMap());
            }

            if ((var3 == null || !var3.contains("DebugSecuritySAML2Atn")) && this.bean.isDebugSecuritySAML2AtnSet()) {
               var5.setDebugSecuritySAML2Atn(this.bean.getDebugSecuritySAML2Atn());
            }

            if ((var3 == null || !var3.contains("DebugSecuritySAML2CredMap")) && this.bean.isDebugSecuritySAML2CredMapSet()) {
               var5.setDebugSecuritySAML2CredMap(this.bean.getDebugSecuritySAML2CredMap());
            }

            if ((var3 == null || !var3.contains("DebugSecuritySAML2Lib")) && this.bean.isDebugSecuritySAML2LibSet()) {
               var5.setDebugSecuritySAML2Lib(this.bean.getDebugSecuritySAML2Lib());
            }

            if ((var3 == null || !var3.contains("DebugSecuritySAML2Service")) && this.bean.isDebugSecuritySAML2ServiceSet()) {
               var5.setDebugSecuritySAML2Service(this.bean.getDebugSecuritySAML2Service());
            }

            if ((var3 == null || !var3.contains("DebugSecuritySAMLAtn")) && this.bean.isDebugSecuritySAMLAtnSet()) {
               var5.setDebugSecuritySAMLAtn(this.bean.getDebugSecuritySAMLAtn());
            }

            if ((var3 == null || !var3.contains("DebugSecuritySAMLCredMap")) && this.bean.isDebugSecuritySAMLCredMapSet()) {
               var5.setDebugSecuritySAMLCredMap(this.bean.getDebugSecuritySAMLCredMap());
            }

            if ((var3 == null || !var3.contains("DebugSecuritySAMLLib")) && this.bean.isDebugSecuritySAMLLibSet()) {
               var5.setDebugSecuritySAMLLib(this.bean.getDebugSecuritySAMLLib());
            }

            if ((var3 == null || !var3.contains("DebugSecuritySAMLService")) && this.bean.isDebugSecuritySAMLServiceSet()) {
               var5.setDebugSecuritySAMLService(this.bean.getDebugSecuritySAMLService());
            }

            if ((var3 == null || !var3.contains("DebugSecuritySSL")) && this.bean.isDebugSecuritySSLSet()) {
               var5.setDebugSecuritySSL(this.bean.getDebugSecuritySSL());
            }

            if ((var3 == null || !var3.contains("DebugSecuritySSLEaten")) && this.bean.isDebugSecuritySSLEatenSet()) {
               var5.setDebugSecuritySSLEaten(this.bean.getDebugSecuritySSLEaten());
            }

            if ((var3 == null || !var3.contains("DebugSecurityService")) && this.bean.isDebugSecurityServiceSet()) {
               var5.setDebugSecurityService(this.bean.getDebugSecurityService());
            }

            if ((var3 == null || !var3.contains("DebugSecurityUserLockout")) && this.bean.isDebugSecurityUserLockoutSet()) {
               var5.setDebugSecurityUserLockout(this.bean.getDebugSecurityUserLockout());
            }

            if ((var3 == null || !var3.contains("DebugSelfTuning")) && this.bean.isDebugSelfTuningSet()) {
               var5.setDebugSelfTuning(this.bean.getDebugSelfTuning());
            }

            if ((var3 == null || !var3.contains("DebugServerLifeCycle")) && this.bean.isDebugServerLifeCycleSet()) {
               var5.setDebugServerLifeCycle(this.bean.getDebugServerLifeCycle());
            }

            if ((var3 == null || !var3.contains("DebugServerMigration")) && this.bean.isDebugServerMigrationSet()) {
               var5.setDebugServerMigration(this.bean.getDebugServerMigration());
            }

            if ((var3 == null || !var3.contains("DebugServerStartStatistics")) && this.bean.isDebugServerStartStatisticsSet()) {
               var5.setDebugServerStartStatistics(this.bean.getDebugServerStartStatistics());
            }

            if ((var3 == null || !var3.contains("DebugStoreAdmin")) && this.bean.isDebugStoreAdminSet()) {
               var5.setDebugStoreAdmin(this.bean.getDebugStoreAdmin());
            }

            if ((var3 == null || !var3.contains("DebugStoreIOLogical")) && this.bean.isDebugStoreIOLogicalSet()) {
               var5.setDebugStoreIOLogical(this.bean.getDebugStoreIOLogical());
            }

            if ((var3 == null || !var3.contains("DebugStoreIOLogicalBoot")) && this.bean.isDebugStoreIOLogicalBootSet()) {
               var5.setDebugStoreIOLogicalBoot(this.bean.getDebugStoreIOLogicalBoot());
            }

            if ((var3 == null || !var3.contains("DebugStoreIOPhysical")) && this.bean.isDebugStoreIOPhysicalSet()) {
               var5.setDebugStoreIOPhysical(this.bean.getDebugStoreIOPhysical());
            }

            if ((var3 == null || !var3.contains("DebugStoreIOPhysicalVerbose")) && this.bean.isDebugStoreIOPhysicalVerboseSet()) {
               var5.setDebugStoreIOPhysicalVerbose(this.bean.getDebugStoreIOPhysicalVerbose());
            }

            if ((var3 == null || !var3.contains("DebugStoreXA")) && this.bean.isDebugStoreXASet()) {
               var5.setDebugStoreXA(this.bean.getDebugStoreXA());
            }

            if ((var3 == null || !var3.contains("DebugStoreXAVerbose")) && this.bean.isDebugStoreXAVerboseSet()) {
               var5.setDebugStoreXAVerbose(this.bean.getDebugStoreXAVerbose());
            }

            if ((var3 == null || !var3.contains("DebugTunnelingConnection")) && this.bean.isDebugTunnelingConnectionSet()) {
               var5.setDebugTunnelingConnection(this.bean.getDebugTunnelingConnection());
            }

            if ((var3 == null || !var3.contains("DebugTunnelingConnectionTimeout")) && this.bean.isDebugTunnelingConnectionTimeoutSet()) {
               var5.setDebugTunnelingConnectionTimeout(this.bean.getDebugTunnelingConnectionTimeout());
            }

            if ((var3 == null || !var3.contains("DebugURLResolution")) && this.bean.isDebugURLResolutionSet()) {
               var5.setDebugURLResolution(this.bean.getDebugURLResolution());
            }

            if ((var3 == null || !var3.contains("DebugWANReplicationDetails")) && this.bean.isDebugWANReplicationDetailsSet()) {
               var5.setDebugWANReplicationDetails(this.bean.getDebugWANReplicationDetails());
            }

            if ((var3 == null || !var3.contains("DebugWTCConfig")) && this.bean.isDebugWTCConfigSet()) {
               var5.setDebugWTCConfig(this.bean.getDebugWTCConfig());
            }

            if ((var3 == null || !var3.contains("DebugWTCCorbaEx")) && this.bean.isDebugWTCCorbaExSet()) {
               var5.setDebugWTCCorbaEx(this.bean.getDebugWTCCorbaEx());
            }

            if ((var3 == null || !var3.contains("DebugWTCGwtEx")) && this.bean.isDebugWTCGwtExSet()) {
               var5.setDebugWTCGwtEx(this.bean.getDebugWTCGwtEx());
            }

            if ((var3 == null || !var3.contains("DebugWTCJatmiEx")) && this.bean.isDebugWTCJatmiExSet()) {
               var5.setDebugWTCJatmiEx(this.bean.getDebugWTCJatmiEx());
            }

            if ((var3 == null || !var3.contains("DebugWTCTDomPdu")) && this.bean.isDebugWTCTDomPduSet()) {
               var5.setDebugWTCTDomPdu(this.bean.getDebugWTCTDomPdu());
            }

            if ((var3 == null || !var3.contains("DebugWTCUData")) && this.bean.isDebugWTCUDataSet()) {
               var5.setDebugWTCUData(this.bean.getDebugWTCUData());
            }

            if ((var3 == null || !var3.contains("DebugWTCtBridgeEx")) && this.bean.isDebugWTCtBridgeExSet()) {
               var5.setDebugWTCtBridgeEx(this.bean.getDebugWTCtBridgeEx());
            }

            if ((var3 == null || !var3.contains("DebugWebAppIdentityAssertion")) && this.bean.isDebugWebAppIdentityAssertionSet()) {
               var5.setDebugWebAppIdentityAssertion(this.bean.getDebugWebAppIdentityAssertion());
            }

            if ((var3 == null || !var3.contains("DebugWebAppModule")) && this.bean.isDebugWebAppModuleSet()) {
               var5.setDebugWebAppModule(this.bean.getDebugWebAppModule());
            }

            if ((var3 == null || !var3.contains("DebugWebAppSecurity")) && this.bean.isDebugWebAppSecuritySet()) {
               var5.setDebugWebAppSecurity(this.bean.getDebugWebAppSecurity());
            }

            if ((var3 == null || !var3.contains("DebugXMLEntityCacheDebugLevel")) && this.bean.isDebugXMLEntityCacheDebugLevelSet()) {
               var5.setDebugXMLEntityCacheDebugLevel(this.bean.getDebugXMLEntityCacheDebugLevel());
            }

            if ((var3 == null || !var3.contains("DebugXMLEntityCacheDebugName")) && this.bean.isDebugXMLEntityCacheDebugNameSet()) {
               var5.setDebugXMLEntityCacheDebugName(this.bean.getDebugXMLEntityCacheDebugName());
            }

            if ((var3 == null || !var3.contains("DebugXMLEntityCacheIncludeClass")) && this.bean.isDebugXMLEntityCacheIncludeClassSet()) {
               var5.setDebugXMLEntityCacheIncludeClass(this.bean.getDebugXMLEntityCacheIncludeClass());
            }

            if ((var3 == null || !var3.contains("DebugXMLEntityCacheIncludeLocation")) && this.bean.isDebugXMLEntityCacheIncludeLocationSet()) {
               var5.setDebugXMLEntityCacheIncludeLocation(this.bean.getDebugXMLEntityCacheIncludeLocation());
            }

            if ((var3 == null || !var3.contains("DebugXMLEntityCacheIncludeName")) && this.bean.isDebugXMLEntityCacheIncludeNameSet()) {
               var5.setDebugXMLEntityCacheIncludeName(this.bean.getDebugXMLEntityCacheIncludeName());
            }

            if ((var3 == null || !var3.contains("DebugXMLEntityCacheIncludeTime")) && this.bean.isDebugXMLEntityCacheIncludeTimeSet()) {
               var5.setDebugXMLEntityCacheIncludeTime(this.bean.getDebugXMLEntityCacheIncludeTime());
            }

            if ((var3 == null || !var3.contains("DebugXMLEntityCacheUseShortClass")) && this.bean.isDebugXMLEntityCacheUseShortClassSet()) {
               var5.setDebugXMLEntityCacheUseShortClass(this.bean.getDebugXMLEntityCacheUseShortClass());
            }

            if ((var3 == null || !var3.contains("DebugXMLRegistryDebugLevel")) && this.bean.isDebugXMLRegistryDebugLevelSet()) {
               var5.setDebugXMLRegistryDebugLevel(this.bean.getDebugXMLRegistryDebugLevel());
            }

            if ((var3 == null || !var3.contains("DebugXMLRegistryDebugName")) && this.bean.isDebugXMLRegistryDebugNameSet()) {
               var5.setDebugXMLRegistryDebugName(this.bean.getDebugXMLRegistryDebugName());
            }

            if ((var3 == null || !var3.contains("DebugXMLRegistryIncludeClass")) && this.bean.isDebugXMLRegistryIncludeClassSet()) {
               var5.setDebugXMLRegistryIncludeClass(this.bean.getDebugXMLRegistryIncludeClass());
            }

            if ((var3 == null || !var3.contains("DebugXMLRegistryIncludeLocation")) && this.bean.isDebugXMLRegistryIncludeLocationSet()) {
               var5.setDebugXMLRegistryIncludeLocation(this.bean.getDebugXMLRegistryIncludeLocation());
            }

            if ((var3 == null || !var3.contains("DebugXMLRegistryIncludeName")) && this.bean.isDebugXMLRegistryIncludeNameSet()) {
               var5.setDebugXMLRegistryIncludeName(this.bean.getDebugXMLRegistryIncludeName());
            }

            if ((var3 == null || !var3.contains("DebugXMLRegistryIncludeTime")) && this.bean.isDebugXMLRegistryIncludeTimeSet()) {
               var5.setDebugXMLRegistryIncludeTime(this.bean.getDebugXMLRegistryIncludeTime());
            }

            if ((var3 == null || !var3.contains("DebugXMLRegistryUseShortClass")) && this.bean.isDebugXMLRegistryUseShortClassSet()) {
               var5.setDebugXMLRegistryUseShortClass(this.bean.getDebugXMLRegistryUseShortClass());
            }

            if ((var3 == null || !var3.contains("DefaultStore")) && this.bean.isDefaultStoreSet()) {
               var5.setDefaultStore(this.bean.getDefaultStore());
            }

            if ((var3 == null || !var3.contains("DiagnosticContextDebugMode")) && this.bean.isDiagnosticContextDebugModeSet()) {
               var5.setDiagnosticContextDebugMode(this.bean.getDiagnosticContextDebugMode());
            }

            if ((var3 == null || !var3.contains("ListenThreadDebug")) && this.bean.isListenThreadDebugSet()) {
               var5.setListenThreadDebug(this.bean.getListenThreadDebug());
            }

            if ((var3 == null || !var3.contains("MagicThreadDumpBackToSocket")) && this.bean.isMagicThreadDumpBackToSocketSet()) {
               var5.setMagicThreadDumpBackToSocket(this.bean.getMagicThreadDumpBackToSocket());
            }

            if ((var3 == null || !var3.contains("MagicThreadDumpFile")) && this.bean.isMagicThreadDumpFileSet()) {
               var5.setMagicThreadDumpFile(this.bean.getMagicThreadDumpFile());
            }

            if ((var3 == null || !var3.contains("MagicThreadDumpHost")) && this.bean.isMagicThreadDumpHostSet()) {
               var5.setMagicThreadDumpHost(this.bean.getMagicThreadDumpHost());
            }

            if ((var3 == null || !var3.contains("MasterDeployer")) && this.bean.isMasterDeployerSet()) {
               var5.setMasterDeployer(this.bean.getMasterDeployer());
            }

            if ((var3 == null || !var3.contains("RedefiningClassLoader")) && this.bean.isRedefiningClassLoaderSet()) {
               var5.setRedefiningClassLoader(this.bean.getRedefiningClassLoader());
            }

            if ((var3 == null || !var3.contains("Server")) && this.bean.isServerSet()) {
               var5._unSet(var5, 37);
               var5.setServerAsString(this.bean.getServerAsString());
            }

            if ((var3 == null || !var3.contains("SlaveDeployer")) && this.bean.isSlaveDeployerSet()) {
               var5.setSlaveDeployer(this.bean.getSlaveDeployer());
            }

            if ((var3 == null || !var3.contains("WebModule")) && this.bean.isWebModuleSet()) {
               var5.setWebModule(this.bean.getWebModule());
            }

            if ((var3 == null || !var3.contains("MagicThreadDumpEnabled")) && this.bean.isMagicThreadDumpEnabledSet()) {
               var5.setMagicThreadDumpEnabled(this.bean.isMagicThreadDumpEnabled());
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
         this.inferSubTree(this.bean.getServer(), var1, var2);
      }
   }
}
