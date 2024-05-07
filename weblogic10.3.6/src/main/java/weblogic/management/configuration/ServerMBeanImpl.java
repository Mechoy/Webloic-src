package weblogic.management.configuration;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.CRC32;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import weblogic.cluster.ClusterValidator;
import weblogic.deploy.internal.targetserver.DeployHelper;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.BeanRemoveRejectedException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BootstrapProperties;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorValidateException;
import weblogic.descriptor.beangen.StringHelper;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.ReferenceManager;
import weblogic.descriptor.internal.ResolvedReference;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.DistributedManagementException;
import weblogic.management.ManagementException;
import weblogic.management.mbeans.custom.Server;
import weblogic.management.runtime.ClusterRuntimeMBean;
import weblogic.management.runtime.ServerLifeCycleRuntimeMBean;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.ArrayIterator;
import weblogic.utils.collections.CombinedIterator;

public class ServerMBeanImpl extends KernelMBeanImpl implements ServerMBean, Serializable {
   private String _81StyleDefaultStagingDirName;
   private int _AcceptBacklog;
   private String _ActiveDirectoryName;
   private int _AdminReconnectIntervalSeconds;
   private int _AdministrationPort;
   private boolean _AdministrationPortEnabled;
   private String _AutoJDBCConnectionClose;
   private boolean _AutoKillIfFailed;
   private boolean _AutoMigrationEnabled;
   private boolean _AutoRestart;
   private COMMBean _COM;
   private boolean _COMEnabled;
   private MachineMBean[] _CandidateMachines;
   private boolean _ClasspathServletDisabled;
   private boolean _ClientCertProxyEnabled;
   private ClusterMBean _Cluster;
   private ClusterRuntimeMBean _ClusterRuntime;
   private int _ClusterWeight;
   private CoherenceClusterSystemResourceMBean _CoherenceClusterSystemResource;
   private int _ConsensusProcessIdentifier;
   private boolean _ConsoleInputEnabled;
   private String _CustomIdentityKeyStoreFileName;
   private String _CustomIdentityKeyStorePassPhrase;
   private byte[] _CustomIdentityKeyStorePassPhraseEncrypted;
   private String _CustomIdentityKeyStoreType;
   private String _CustomTrustKeyStoreFileName;
   private String _CustomTrustKeyStorePassPhrase;
   private byte[] _CustomTrustKeyStorePassPhraseEncrypted;
   private String _CustomTrustKeyStoreType;
   private DataSourceMBean _DataSource;
   private DefaultFileStoreMBean _DefaultFileStore;
   private String _DefaultIIOPPassword;
   private byte[] _DefaultIIOPPasswordEncrypted;
   private String _DefaultIIOPUser;
   private boolean _DefaultInternalServletsDisabled;
   private String _DefaultStagingDirName;
   private String _DefaultTGIOPPassword;
   private byte[] _DefaultTGIOPPasswordEncrypted;
   private String _DefaultTGIOPUser;
   private DomainLogFilterMBean _DomainLogFilter;
   private boolean _EnabledForDomainLog;
   private boolean _ExpectedToRun;
   private String _ExternalDNSName;
   private String _ExtraEjbcOptions;
   private String _ExtraRmicOptions;
   private FederationServicesMBean _FederationServices;
   private int _GracefulShutdownTimeout;
   private int _HealthCheckIntervalSeconds;
   private int _HealthCheckStartDelaySeconds;
   private int _HealthCheckTimeoutSeconds;
   private boolean _HostsMigratableServices;
   private boolean _HttpTraceSupportEnabled;
   private boolean _HttpdEnabled;
   private Map _IIOPConnectionPools;
   private boolean _IIOPEnabled;
   private boolean _IgnoreSessionsDuringShutdown;
   private String _InterfaceAddress;
   private boolean _J2EE12OnlyModeEnabled;
   private boolean _J2EE13WarningEnabled;
   private String _JDBCLLRTableName;
   private int _JDBCLLRTablePoolColumnSize;
   private int _JDBCLLRTableRecordColumnSize;
   private int _JDBCLLRTableXIDColumnSize;
   private String _JDBCLogFileName;
   private boolean _JDBCLoggingEnabled;
   private int _JDBCLoginTimeoutSeconds;
   private boolean _JMSDefaultConnectionFactoriesEnabled;
   private String[] _JNDITransportableObjectFactoryList;
   private boolean _JRMPEnabled;
   private JTAMigratableTargetMBean _JTAMigratableTarget;
   private String _JavaCompiler;
   private String _JavaCompilerPostClassPath;
   private String _JavaCompilerPreClassPath;
   private String _JavaStandardTrustKeyStorePassPhrase;
   private byte[] _JavaStandardTrustKeyStorePassPhraseEncrypted;
   private KernelDebugMBean _KernelDebug;
   private String _KeyStores;
   private String _ListenAddress;
   private int _ListenDelaySecs;
   private int _ListenPort;
   private boolean _ListenPortEnabled;
   private int _ListenThreadStartDelaySecs;
   private boolean _ListenersBindEarly;
   private int _LoginTimeout;
   private int _LoginTimeoutMillis;
   private int _LowMemoryGCThreshold;
   private int _LowMemoryGranularityLevel;
   private int _LowMemorySampleSize;
   private int _LowMemoryTimeInterval;
   private boolean _MSIFileReplicationEnabled;
   private MachineMBean _Machine;
   private boolean _ManagedServerIndependenceEnabled;
   private int _MaxBackoffBetweenFailures;
   private boolean _MessageIdPrefixEnabled;
   private int _NMSocketCreateTimeoutInMillis;
   private String _Name;
   private NetworkAccessPointMBean[] _NetworkAccessPoints;
   private boolean _NetworkClassLoadingEnabled;
   private OverloadProtectionMBean _OverloadProtection;
   private String _PreferredSecondaryGroup;
   private WSReliableDeliveryPolicyMBean _ReliableDeliveryPolicy;
   private String _ReplicationGroup;
   private String _ReplicationPorts;
   private int _RestartDelaySeconds;
   private int _RestartIntervalSeconds;
   private int _RestartMax;
   private String _RootDirectory;
   private ServerDebugMBean _ServerDebug;
   private WLDFServerDiagnosticMBean _ServerDiagnosticConfig;
   private int _ServerLifeCycleTimeoutVal;
   private Set _ServerNames;
   private ServerStartMBean _ServerStart;
   private String _ServerVersion;
   private SingleSignOnServicesMBean _SingleSignOnServices;
   private String _StagingDirectoryName;
   private String _StagingMode;
   private String _StartupMode;
   private int _StartupTimeout;
   private boolean _StdoutDebugEnabled;
   private boolean _StdoutEnabled;
   private String _StdoutFormat;
   private boolean _StdoutLogStack;
   private int _StdoutSeverityLevel;
   private String[] _SupportedProtocols;
   private String _SystemPassword;
   private byte[] _SystemPasswordEncrypted;
   private boolean _TGIOPEnabled;
   private int _ThreadPoolSize;
   private String _TransactionLogFilePrefix;
   private String _TransactionLogFileWritePolicy;
   private TransactionLogJDBCStoreMBean _TransactionLogJDBCStore;
   private int _TunnelingClientPingSecs;
   private int _TunnelingClientTimeoutSecs;
   private boolean _TunnelingEnabled;
   private String _UploadDirectoryName;
   private boolean _UseFusionForLLR;
   private String _VerboseEJBDeploymentEnabled;
   private String _VirtualMachineName;
   private WebServerMBean _WebServer;
   private WebServiceMBean _WebService;
   private boolean _WeblogicPluginEnabled;
   private XMLEntityCacheMBean _XMLEntityCache;
   private XMLRegistryMBean _XMLRegistry;
   private Server _customizer;
   private static SchemaHelper2 _schemaHelper;

   public ServerMBeanImpl() {
      try {
         this._customizer = new Server(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public ServerMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new Server(this);
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         throw new UndeclaredThrowableException(var4);
      }

      this._initializeProperty(-1);
   }

   public String getName() {
      return this._customizer.getName();
   }

   public String getRootDirectory() {
      return this._customizer.getRootDirectory();
   }

   public Set getServerNames() {
      return this._customizer.getServerNames();
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public boolean isRootDirectorySet() {
      return this._isSet(78);
   }

   public boolean isServerNamesSet() {
      return this._isSet(77);
   }

   public void setServerNames(Set var1) throws InvalidAttributeValueException {
      this._ServerNames = var1;
   }

   public void setName(String var1) throws InvalidAttributeValueException, ManagementException {
      var1 = var1 == null ? null : var1.trim();
      ConfigurationValidator.validateName(var1);
      String var2 = this.getName();
      this._customizer.setName(var1);
      this._postSet(2, var2, var1);
   }

   public void setRootDirectory(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._RootDirectory = var1;
   }

   public MachineMBean getMachine() {
      return this._Machine;
   }

   public String getMachineAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getMachine();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isMachineSet() {
      return this._isSet(79);
   }

   public void setMachineAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, MachineMBean.class, new ReferenceManager.Resolver(this, 79) {
            public void resolveReference(Object var1) {
               try {
                  ServerMBeanImpl.this.setMachine((MachineMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         MachineMBean var2 = this._Machine;
         this._initializeProperty(79);
         this._postSet(79, var2, this._Machine);
      }

   }

   public void setMachine(MachineMBean var1) throws InvalidAttributeValueException {
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 79, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return ServerMBeanImpl.this.getMachine();
            }
         };
         this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1, var2);
      }

      MachineMBean var3 = this._Machine;
      this._Machine = var1;
      this._postSet(79, var3, var1);
   }

   public int getListenPort() {
      return this._ListenPort;
   }

   public boolean isListenPortSet() {
      return this._isSet(80);
   }

   public void setListenPort(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("ListenPort", (long)var1, 1L, 65535L);
      int var2 = this._ListenPort;
      this._ListenPort = var1;
      this._postSet(80, var2, var1);
   }

   public boolean isListenPortEnabled() {
      return this._ListenPortEnabled;
   }

   public boolean isListenPortEnabledSet() {
      return this._isSet(81);
   }

   public void setListenPortEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._ListenPortEnabled;
      this._ListenPortEnabled = var1;
      this._postSet(81, var2, var1);
   }

   public int getLoginTimeout() {
      return this._LoginTimeout;
   }

   public boolean isLoginTimeoutSet() {
      return this._isSet(82);
   }

   public int getThreadPoolSize() {
      return this._customizer.getThreadPoolSize();
   }

   public boolean isThreadPoolSizeSet() {
      return this._isSet(12);
   }

   public void setLoginTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      int var2 = this._LoginTimeout;
      this._LoginTimeout = var1;
      this._postSet(82, var2, var1);
   }

   public ClusterMBean getCluster() {
      return this._customizer.getCluster();
   }

   public String getClusterAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getCluster();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isClusterSet() {
      return this._isSet(83);
   }

   public void setClusterAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, ClusterMBean.class, new ReferenceManager.Resolver(this, 83) {
            public void resolveReference(Object var1) {
               try {
                  ServerMBeanImpl.this.setCluster((ClusterMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         ClusterMBean var2 = this._Cluster;
         this._initializeProperty(83);
         this._postSet(83, var2, this._Cluster);
      }

   }

   public void setThreadPoolSize(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("ThreadPoolSize", (long)var1, 0L, 65534L);
      int var2 = this.getThreadPoolSize();
      this._customizer.setThreadPoolSize(var1);
      this._postSet(12, var2, var1);
   }

   public void touch() throws ConfigurationException {
      this._customizer.touch();
   }

   public void freezeCurrentValue(String var1) throws AttributeNotFoundException, MBeanException {
      this._customizer.freezeCurrentValue(var1);
   }

   public void setCluster(ClusterMBean var1) throws InvalidAttributeValueException {
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 83, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return ServerMBeanImpl.this.getCluster();
            }
         };
         this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1, var2);
      }

      ClusterMBean var3 = this.getCluster();
      this._customizer.setCluster(var1);
      this._postSet(83, var3, var1);
   }

   public int getClusterWeight() {
      return this._ClusterWeight;
   }

   public boolean isClusterWeightSet() {
      return this._isSet(84);
   }

   public void restoreDefaultValue(String var1) throws AttributeNotFoundException {
      this._customizer.restoreDefaultValue(var1);
   }

   public void setClusterWeight(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("ClusterWeight", (long)var1, 1L, 100L);
      int var2 = this._ClusterWeight;
      this._ClusterWeight = var1;
      this._postSet(84, var2, var1);
   }

   public String getReplicationGroup() {
      return this._ReplicationGroup;
   }

   public boolean isReplicationGroupSet() {
      return this._isSet(85);
   }

   public void setReplicationGroup(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ReplicationGroup;
      this._ReplicationGroup = var1;
      this._postSet(85, var2, var1);
   }

   public String getPreferredSecondaryGroup() {
      return this._PreferredSecondaryGroup;
   }

   public boolean isPreferredSecondaryGroupSet() {
      return this._isSet(86);
   }

   public void setPreferredSecondaryGroup(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._PreferredSecondaryGroup;
      this._PreferredSecondaryGroup = var1;
      this._postSet(86, var2, var1);
   }

   public int getConsensusProcessIdentifier() {
      return this._ConsensusProcessIdentifier;
   }

   public boolean isConsensusProcessIdentifierSet() {
      return this._isSet(87);
   }

   public void setConsensusProcessIdentifier(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("ConsensusProcessIdentifier", (long)var1, -1L, 65535L);
      int var2 = this._ConsensusProcessIdentifier;
      this._ConsensusProcessIdentifier = var1;
      this._postSet(87, var2, var1);
   }

   public boolean isAutoMigrationEnabled() {
      return this._AutoMigrationEnabled;
   }

   public boolean isAutoMigrationEnabledSet() {
      return this._isSet(88);
   }

   public void setAutoMigrationEnabled(boolean var1) {
      ClusterValidator.validateAutoMigration(var1);
      boolean var2 = this._AutoMigrationEnabled;
      this._AutoMigrationEnabled = var1;
      this._postSet(88, var2, var1);
   }

   public ClusterRuntimeMBean getClusterRuntime() {
      return this._ClusterRuntime;
   }

   public boolean isClusterRuntimeSet() {
      return this._isSet(89);
   }

   public void setClusterRuntime(ClusterRuntimeMBean var1) {
      this._ClusterRuntime = var1;
   }

   public WebServerMBean getWebServer() {
      return this._WebServer;
   }

   public boolean isWebServerSet() {
      return this._isSet(90) || this._isAnythingSet((AbstractDescriptorBean)this.getWebServer());
   }

   public void setWebServer(WebServerMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 90)) {
         this._postCreate(var2);
      }

      WebServerMBean var3 = this._WebServer;
      this._WebServer = var1;
      this._postSet(90, var3, var1);
   }

   public boolean getExpectedToRun() {
      return this._ExpectedToRun;
   }

   public boolean isExpectedToRunSet() {
      return this._isSet(91);
   }

   public void setExpectedToRun(boolean var1) {
      this._ExpectedToRun = var1;
   }

   public String synchronousStart() {
      return this._customizer.synchronousStart();
   }

   public String synchronousKill() {
      return this._customizer.synchronousKill();
   }

   public boolean isJDBCLoggingEnabled() {
      return this._JDBCLoggingEnabled;
   }

   public boolean isJDBCLoggingEnabledSet() {
      return this._isSet(92);
   }

   public void setJDBCLoggingEnabled(boolean var1) {
      boolean var2 = this._JDBCLoggingEnabled;
      this._JDBCLoggingEnabled = var1;
      this._postSet(92, var2, var1);
   }

   public String getJDBCLogFileName() {
      return this._JDBCLogFileName;
   }

   public boolean isJDBCLogFileNameSet() {
      return this._isSet(93);
   }

   public void setJDBCLogFileName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._JDBCLogFileName;
      this._JDBCLogFileName = var1;
      this._postSet(93, var2, var1);
   }

   public boolean isJ2EE12OnlyModeEnabled() {
      return this._J2EE12OnlyModeEnabled;
   }

   public boolean isJ2EE12OnlyModeEnabledSet() {
      return this._isSet(94);
   }

   public void setJ2EE12OnlyModeEnabled(boolean var1) {
      boolean var2 = this._J2EE12OnlyModeEnabled;
      this._J2EE12OnlyModeEnabled = var1;
      this._postSet(94, var2, var1);
   }

   public boolean isJ2EE13WarningEnabled() {
      return this._J2EE13WarningEnabled;
   }

   public boolean isJ2EE13WarningEnabledSet() {
      return this._isSet(95);
   }

   public void setJ2EE13WarningEnabled(boolean var1) {
      boolean var2 = this._J2EE13WarningEnabled;
      this._J2EE13WarningEnabled = var1;
      this._postSet(95, var2, var1);
   }

   public boolean isIIOPEnabled() {
      return this._IIOPEnabled;
   }

   public boolean isIIOPEnabledSet() {
      return this._isSet(96);
   }

   public void setIIOPEnabled(boolean var1) {
      boolean var2 = this._IIOPEnabled;
      this._IIOPEnabled = var1;
      this._postSet(96, var2, var1);
   }

   public String getDefaultIIOPUser() {
      return this._DefaultIIOPUser;
   }

   public boolean isDefaultIIOPUserSet() {
      return this._isSet(97);
   }

   public void setDefaultIIOPUser(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._DefaultIIOPUser;
      this._DefaultIIOPUser = var1;
      this._postSet(97, var2, var1);
   }

   public String getDefaultIIOPPassword() {
      byte[] var1 = this.getDefaultIIOPPasswordEncrypted();
      return var1 == null ? null : this._decrypt("DefaultIIOPPassword", var1);
   }

   public boolean isDefaultIIOPPasswordSet() {
      return this.isDefaultIIOPPasswordEncryptedSet();
   }

   public void setDefaultIIOPPassword(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this.setDefaultIIOPPasswordEncrypted(var1 == null ? null : this._encrypt("DefaultIIOPPassword", var1));
   }

   public byte[] getDefaultIIOPPasswordEncrypted() {
      return this._getHelper()._cloneArray(this._DefaultIIOPPasswordEncrypted);
   }

   public String getDefaultIIOPPasswordEncryptedAsString() {
      byte[] var1 = this.getDefaultIIOPPasswordEncrypted();
      return var1 == null ? null : new String(var1);
   }

   public boolean isDefaultIIOPPasswordEncryptedSet() {
      return this._isSet(99);
   }

   public void setDefaultIIOPPasswordEncryptedAsString(String var1) {
      try {
         byte[] var2 = var1 == null ? null : var1.getBytes();
         this.setDefaultIIOPPasswordEncrypted(var2);
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public boolean isTGIOPEnabled() {
      return this._TGIOPEnabled;
   }

   public boolean isTGIOPEnabledSet() {
      return this._isSet(100);
   }

   public void setTGIOPEnabled(boolean var1) {
      boolean var2 = this._TGIOPEnabled;
      this._TGIOPEnabled = var1;
      this._postSet(100, var2, var1);
   }

   public String getDefaultTGIOPUser() {
      return this._DefaultTGIOPUser;
   }

   public boolean isDefaultTGIOPUserSet() {
      return this._isSet(101);
   }

   public void setDefaultTGIOPUser(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._DefaultTGIOPUser;
      this._DefaultTGIOPUser = var1;
      this._postSet(101, var2, var1);
   }

   public String getDefaultTGIOPPassword() {
      byte[] var1 = this.getDefaultTGIOPPasswordEncrypted();
      return var1 == null ? null : this._decrypt("DefaultTGIOPPassword", var1);
   }

   public boolean isDefaultTGIOPPasswordSet() {
      return this.isDefaultTGIOPPasswordEncryptedSet();
   }

   public void setDefaultTGIOPPassword(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this.setDefaultTGIOPPasswordEncrypted(var1 == null ? null : this._encrypt("DefaultTGIOPPassword", var1));
   }

   public byte[] getDefaultTGIOPPasswordEncrypted() {
      return this._getHelper()._cloneArray(this._DefaultTGIOPPasswordEncrypted);
   }

   public String getDefaultTGIOPPasswordEncryptedAsString() {
      byte[] var1 = this.getDefaultTGIOPPasswordEncrypted();
      return var1 == null ? null : new String(var1);
   }

   public boolean isDefaultTGIOPPasswordEncryptedSet() {
      return this._isSet(103);
   }

   public void setDefaultTGIOPPasswordEncryptedAsString(String var1) {
      try {
         byte[] var2 = var1 == null ? null : var1.getBytes();
         this.setDefaultTGIOPPasswordEncrypted(var2);
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public boolean isCOMEnabled() {
      return this._COMEnabled;
   }

   public boolean isCOMEnabledSet() {
      return this._isSet(104);
   }

   public void setCOMEnabled(boolean var1) {
      boolean var2 = this._COMEnabled;
      this._COMEnabled = var1;
      this._postSet(104, var2, var1);
   }

   public boolean isJRMPEnabled() {
      return this._JRMPEnabled;
   }

   public boolean isJRMPEnabledSet() {
      return this._isSet(105);
   }

   public void setJRMPEnabled(boolean var1) {
      boolean var2 = this._JRMPEnabled;
      this._JRMPEnabled = var1;
      this._postSet(105, var2, var1);
   }

   public COMMBean getCOM() {
      return this._COM;
   }

   public boolean isCOMSet() {
      return this._isSet(106) || this._isAnythingSet((AbstractDescriptorBean)this.getCOM());
   }

   public void setCOM(COMMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 106)) {
         this._postCreate(var2);
      }

      COMMBean var3 = this._COM;
      this._COM = var1;
      this._postSet(106, var3, var1);
   }

   public ServerDebugMBean getServerDebug() {
      return this._ServerDebug;
   }

   public boolean isServerDebugSet() {
      return this._isSet(107) || this._isAnythingSet((AbstractDescriptorBean)this.getServerDebug());
   }

   public void setServerDebug(ServerDebugMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 107)) {
         this._postCreate(var2);
      }

      ServerDebugMBean var3 = this._ServerDebug;
      this._ServerDebug = var1;
      this._postSet(107, var3, var1);
   }

   public boolean isHttpdEnabled() {
      return this._HttpdEnabled;
   }

   public boolean isHttpdEnabledSet() {
      return this._isSet(108);
   }

   public void setHttpdEnabled(boolean var1) {
      boolean var2 = this._HttpdEnabled;
      this._HttpdEnabled = var1;
      this._postSet(108, var2, var1);
   }

   public String getSystemPassword() {
      byte[] var1 = this.getSystemPasswordEncrypted();
      return var1 == null ? null : this._decrypt("SystemPassword", var1);
   }

   public boolean isSystemPasswordSet() {
      return this.isSystemPasswordEncryptedSet();
   }

   public void setSystemPassword(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this.setSystemPasswordEncrypted(var1 == null ? null : this._encrypt("SystemPassword", var1));
   }

   public byte[] getSystemPasswordEncrypted() {
      return this._getHelper()._cloneArray(this._SystemPasswordEncrypted);
   }

   public String getSystemPasswordEncryptedAsString() {
      byte[] var1 = this.getSystemPasswordEncrypted();
      return var1 == null ? null : new String(var1);
   }

   public boolean isSystemPasswordEncryptedSet() {
      return this._isSet(110);
   }

   public void setSystemPasswordEncryptedAsString(String var1) {
      try {
         byte[] var2 = var1 == null ? null : var1.getBytes();
         this.setSystemPasswordEncrypted(var2);
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public boolean isConsoleInputEnabled() {
      return this._ConsoleInputEnabled;
   }

   public boolean isConsoleInputEnabledSet() {
      return this._isSet(111);
   }

   public void setConsoleInputEnabled(boolean var1) {
      boolean var2 = this._ConsoleInputEnabled;
      this._ConsoleInputEnabled = var1;
      this._postSet(111, var2, var1);
   }

   public int getListenThreadStartDelaySecs() {
      return this._ListenThreadStartDelaySecs;
   }

   public boolean isListenThreadStartDelaySecsSet() {
      return this._isSet(112);
   }

   public void setListenThreadStartDelaySecs(int var1) throws InvalidAttributeValueException {
      int var2 = this._ListenThreadStartDelaySecs;
      this._ListenThreadStartDelaySecs = var1;
      this._postSet(112, var2, var1);
   }

   public boolean getListenersBindEarly() {
      return this._ListenersBindEarly;
   }

   public boolean isListenersBindEarlySet() {
      return this._isSet(113);
   }

   public void setListenersBindEarly(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._ListenersBindEarly;
      this._ListenersBindEarly = var1;
      this._postSet(113, var2, var1);
   }

   public String getListenAddress() {
      return this._ListenAddress;
   }

   public boolean isListenAddressSet() {
      return this._isSet(114);
   }

   public void setListenAddress(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ListenAddress;
      this._ListenAddress = var1;
      this._postSet(114, var2, var1);
   }

   public String getExternalDNSName() {
      return this._ExternalDNSName;
   }

   public boolean isExternalDNSNameSet() {
      return this._isSet(115);
   }

   public void setExternalDNSName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ExternalDNSName;
      this._ExternalDNSName = var1;
      this._postSet(115, var2, var1);
   }

   public String getInterfaceAddress() {
      return this._InterfaceAddress;
   }

   public boolean isInterfaceAddressSet() {
      return this._isSet(116);
   }

   public void setInterfaceAddress(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._InterfaceAddress;
      this._InterfaceAddress = var1;
      this._postSet(116, var2, var1);
   }

   public NetworkAccessPointMBean[] getNetworkAccessPoints() {
      return this._NetworkAccessPoints;
   }

   public boolean isNetworkAccessPointsSet() {
      return this._isSet(117);
   }

   public NetworkAccessPointMBean lookupNetworkAccessPoint(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._NetworkAccessPoints).iterator();

      NetworkAccessPointMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (NetworkAccessPointMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public NetworkAccessPointMBean createNetworkAccessPoint(String var1) {
      NetworkAccessPointMBeanImpl var2 = new NetworkAccessPointMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addNetworkAccessPoint(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyNetworkAccessPoint(NetworkAccessPointMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 117);
         NetworkAccessPointMBean[] var2 = this.getNetworkAccessPoints();
         NetworkAccessPointMBean[] var3 = (NetworkAccessPointMBean[])((NetworkAccessPointMBean[])this._getHelper()._removeElement(var2, NetworkAccessPointMBean.class, var1));
         if (var2.length != var3.length) {
            this._preDestroy((AbstractDescriptorBean)var1);

            try {
               AbstractDescriptorBean var4 = (AbstractDescriptorBean)var1;
               if (var4 == null) {
                  return;
               }

               List var5 = this._getReferenceManager().getResolvedReferences(var4);
               if (var5 != null && var5.size() > 0) {
                  throw new BeanRemoveRejectedException(var4, var5);
               }

               this._getReferenceManager().unregisterBean(var4);
               this._markDestroyed(var4);
               this.setNetworkAccessPoints(var3);
            } catch (Exception var6) {
               if (var6 instanceof RuntimeException) {
                  throw (RuntimeException)var6;
               }

               throw new UndeclaredThrowableException(var6);
            }
         }

      } catch (Exception var7) {
         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         } else {
            throw new UndeclaredThrowableException(var7);
         }
      }
   }

   public KernelDebugMBean getKernelDebug() {
      return this._customizer.getKernelDebug();
   }

   public boolean isKernelDebugSet() {
      return this._isSet(48);
   }

   public void setKernelDebug(KernelDebugMBean var1) throws InvalidAttributeValueException {
      this._KernelDebug = var1;
   }

   public void setNetworkAccessPoints(NetworkAccessPointMBean[] var1) throws InvalidAttributeValueException, ConfigurationException {
      Object var4 = var1 == null ? new NetworkAccessPointMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 117)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      NetworkAccessPointMBean[] var5 = this._NetworkAccessPoints;
      this._NetworkAccessPoints = (NetworkAccessPointMBean[])var4;
      this._postSet(117, var5, var4);
   }

   public boolean addNetworkAccessPoint(NetworkAccessPointMBean var1) throws InvalidAttributeValueException, ConfigurationException {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 117)) {
         NetworkAccessPointMBean[] var2;
         if (this._isSet(117)) {
            var2 = (NetworkAccessPointMBean[])((NetworkAccessPointMBean[])this._getHelper()._extendArray(this.getNetworkAccessPoints(), NetworkAccessPointMBean.class, var1));
         } else {
            var2 = new NetworkAccessPointMBean[]{var1};
         }

         try {
            this.setNetworkAccessPoints(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            if (var4 instanceof InvalidAttributeValueException) {
               throw (InvalidAttributeValueException)var4;
            }

            if (var4 instanceof ConfigurationException) {
               throw (ConfigurationException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

      return true;
   }

   public boolean removeNetworkAccessPoint(NetworkAccessPointMBean var1) throws InvalidAttributeValueException, ConfigurationException {
      this.destroyNetworkAccessPoint(var1);
      return true;
   }

   public int getAcceptBacklog() {
      return this._AcceptBacklog;
   }

   public boolean isAcceptBacklogSet() {
      return this._isSet(118);
   }

   public void setAcceptBacklog(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("AcceptBacklog", var1, 0);
      int var2 = this._AcceptBacklog;
      this._AcceptBacklog = var1;
      this._postSet(118, var2, var1);
   }

   public int getMaxBackoffBetweenFailures() {
      return this._MaxBackoffBetweenFailures;
   }

   public boolean isMaxBackoffBetweenFailuresSet() {
      return this._isSet(119);
   }

   public boolean isStdoutEnabled() {
      return this._customizer.isStdoutEnabled();
   }

   public boolean isStdoutEnabledSet() {
      return this._isSet(53);
   }

   public void setMaxBackoffBetweenFailures(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("MaxBackoffBetweenFailures", var1, 0);
      int var2 = this._MaxBackoffBetweenFailures;
      this._MaxBackoffBetweenFailures = var1;
      this._postSet(119, var2, var1);
   }

   public int getLoginTimeoutMillis() {
      return this._LoginTimeoutMillis;
   }

   public boolean isLoginTimeoutMillisSet() {
      return this._isSet(120);
   }

   public void setStdoutEnabled(boolean var1) throws DistributedManagementException {
      boolean var2 = this.isStdoutEnabled();
      this._customizer.setStdoutEnabled(var1);
      this._postSet(53, var2, var1);
   }

   public int getStdoutSeverityLevel() {
      return this._customizer.getStdoutSeverityLevel();
   }

   public boolean isStdoutSeverityLevelSet() {
      return this._isSet(54);
   }

   public void setLoginTimeoutMillis(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("LoginTimeoutMillis", (long)var1, 0L, 100000L);
      int var2 = this._LoginTimeoutMillis;
      this._LoginTimeoutMillis = var1;
      this._postSet(120, var2, var1);
   }

   public boolean isAdministrationPortEnabled() {
      if (!this._isSet(121)) {
         try {
            return ((DomainMBean)this.getParent()).isAdministrationPortEnabled();
         } catch (NullPointerException var2) {
         }
      }

      return this._AdministrationPortEnabled;
   }

   public boolean isAdministrationPortEnabledSet() {
      return this._isSet(121);
   }

   public void setStdoutSeverityLevel(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      int[] var2 = new int[]{256, 128, 64, 16, 8, 32, 4, 2, 1, 0};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("StdoutSeverityLevel", var1, var2);
      int var3 = this.getStdoutSeverityLevel();
      this._customizer.setStdoutSeverityLevel(var1);
      this._postSet(54, var3, var1);
   }

   public boolean isStdoutDebugEnabled() {
      return this._customizer.isStdoutDebugEnabled();
   }

   public boolean isStdoutDebugEnabledSet() {
      return this._isSet(55);
   }

   public void setAdministrationPortEnabled(boolean var1) {
      boolean var2 = this._AdministrationPortEnabled;
      this._AdministrationPortEnabled = var1;
      this._postSet(121, var2, var1);
   }

   public int getAdministrationPort() {
      if (!this._isSet(122)) {
         try {
            return ((DomainMBean)this.getParent()).getAdministrationPort();
         } catch (NullPointerException var2) {
         }
      }

      return this._customizer.getAdministrationPort();
   }

   public boolean isAdministrationPortSet() {
      return this._isSet(122);
   }

   public void setStdoutDebugEnabled(boolean var1) throws DistributedManagementException {
      boolean var2 = this.isStdoutDebugEnabled();
      this._customizer.setStdoutDebugEnabled(var1);
      this._postSet(55, var2, var1);
   }

   public void setAdministrationPort(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("AdministrationPort", (long)var1, 0L, 65535L);
      int var2 = this.getAdministrationPort();
      this._customizer.setAdministrationPort(var1);
      this._postSet(122, var2, var1);
   }

   public String[] getJNDITransportableObjectFactoryList() {
      return this._JNDITransportableObjectFactoryList;
   }

   public boolean isJNDITransportableObjectFactoryListSet() {
      return this._isSet(123);
   }

   public void setJNDITransportableObjectFactoryList(String[] var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      String[] var2 = this._JNDITransportableObjectFactoryList;
      this._JNDITransportableObjectFactoryList = var1;
      this._postSet(123, var2, var1);
   }

   public Map getIIOPConnectionPools() {
      return this._IIOPConnectionPools;
   }

   public String getIIOPConnectionPoolsAsString() {
      return StringHelper.objectToString(this.getIIOPConnectionPools());
   }

   public boolean isIIOPConnectionPoolsSet() {
      return this._isSet(124);
   }

   public void setIIOPConnectionPoolsAsString(String var1) {
      try {
         this.setIIOPConnectionPools(StringHelper.stringToMap(var1));
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public void setIIOPConnectionPools(Map var1) throws InvalidAttributeValueException {
      Map var2 = this._IIOPConnectionPools;
      this._IIOPConnectionPools = var1;
      this._postSet(124, var2, var1);
   }

   public XMLRegistryMBean getXMLRegistry() {
      return this._XMLRegistry;
   }

   public String getXMLRegistryAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getXMLRegistry();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isXMLRegistrySet() {
      return this._isSet(125);
   }

   public void setXMLRegistryAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, XMLRegistryMBean.class, new ReferenceManager.Resolver(this, 125) {
            public void resolveReference(Object var1) {
               try {
                  ServerMBeanImpl.this.setXMLRegistry((XMLRegistryMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         XMLRegistryMBean var2 = this._XMLRegistry;
         this._initializeProperty(125);
         this._postSet(125, var2, this._XMLRegistry);
      }

   }

   public void setXMLEntityCache(XMLEntityCacheMBean var1) throws InvalidAttributeValueException {
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 126, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return ServerMBeanImpl.this.getXMLEntityCache();
            }
         };
         this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1, var2);
      }

      XMLEntityCacheMBean var3 = this._XMLEntityCache;
      this._XMLEntityCache = var1;
      this._postSet(126, var3, var1);
   }

   public String getStdoutFormat() {
      return this._customizer.getStdoutFormat();
   }

   public XMLEntityCacheMBean getXMLEntityCache() {
      return this._XMLEntityCache;
   }

   public String getXMLEntityCacheAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getXMLEntityCache();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isStdoutFormatSet() {
      return this._isSet(60);
   }

   public boolean isXMLEntityCacheSet() {
      return this._isSet(126);
   }

   public void setXMLEntityCacheAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, XMLEntityCacheMBean.class, new ReferenceManager.Resolver(this, 126) {
            public void resolveReference(Object var1) {
               try {
                  ServerMBeanImpl.this.setXMLEntityCache((XMLEntityCacheMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         XMLEntityCacheMBean var2 = this._XMLEntityCache;
         this._initializeProperty(126);
         this._postSet(126, var2, this._XMLEntityCache);
      }

   }

   public void setStdoutFormat(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"standard", "noid"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("StdoutFormat", var1, var2);
      String var3 = this.getStdoutFormat();
      this._customizer.setStdoutFormat(var1);
      this._postSet(60, var3, var1);
   }

   public void setXMLRegistry(XMLRegistryMBean var1) throws InvalidAttributeValueException {
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 125, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return ServerMBeanImpl.this.getXMLRegistry();
            }
         };
         this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1, var2);
      }

      XMLRegistryMBean var3 = this._XMLRegistry;
      this._XMLRegistry = var1;
      this._postSet(125, var3, var1);
   }

   public String getJavaCompiler() {
      return this._JavaCompiler;
   }

   public boolean isJavaCompilerSet() {
      return this._isSet(127);
   }

   public boolean isStdoutLogStack() {
      return this._customizer.isStdoutLogStack();
   }

   public boolean isStdoutLogStackSet() {
      return this._isSet(61);
   }

   public void setJavaCompiler(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._JavaCompiler;
      this._JavaCompiler = var1;
      this._postSet(127, var2, var1);
   }

   public void setStdoutLogStack(boolean var1) {
      boolean var2 = this.isStdoutLogStack();
      this._customizer.setStdoutLogStack(var1);
      this._postSet(61, var2, var1);
   }

   public String getJavaCompilerPreClassPath() {
      return this._JavaCompilerPreClassPath;
   }

   public boolean isJavaCompilerPreClassPathSet() {
      return this._isSet(128);
   }

   public void setJavaCompilerPreClassPath(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._JavaCompilerPreClassPath;
      this._JavaCompilerPreClassPath = var1;
      this._postSet(128, var2, var1);
   }

   public String getJavaCompilerPostClassPath() {
      return this._JavaCompilerPostClassPath;
   }

   public boolean isJavaCompilerPostClassPathSet() {
      return this._isSet(129);
   }

   public void setJavaCompilerPostClassPath(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._JavaCompilerPostClassPath;
      this._JavaCompilerPostClassPath = var1;
      this._postSet(129, var2, var1);
   }

   public String getExtraRmicOptions() {
      return this._ExtraRmicOptions;
   }

   public boolean isExtraRmicOptionsSet() {
      return this._isSet(130);
   }

   public void setExtraRmicOptions(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ExtraRmicOptions;
      this._ExtraRmicOptions = var1;
      this._postSet(130, var2, var1);
   }

   public String getExtraEjbcOptions() {
      return this._ExtraEjbcOptions;
   }

   public boolean isExtraEjbcOptionsSet() {
      return this._isSet(131);
   }

   public void setExtraEjbcOptions(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ExtraEjbcOptions;
      this._ExtraEjbcOptions = var1;
      this._postSet(131, var2, var1);
   }

   public String getVerboseEJBDeploymentEnabled() {
      return this._VerboseEJBDeploymentEnabled;
   }

   public boolean isVerboseEJBDeploymentEnabledSet() {
      return this._isSet(132);
   }

   public void setVerboseEJBDeploymentEnabled(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._VerboseEJBDeploymentEnabled;
      this._VerboseEJBDeploymentEnabled = var1;
      this._postSet(132, var2, var1);
   }

   public String getTransactionLogFilePrefix() {
      return this._TransactionLogFilePrefix;
   }

   public boolean isTransactionLogFilePrefixSet() {
      return this._isSet(133);
   }

   public void setTransactionLogFilePrefix(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._TransactionLogFilePrefix;
      this._TransactionLogFilePrefix = var1;
      this._postSet(133, var2, var1);
   }

   public String getTransactionLogFileWritePolicy() {
      return this._TransactionLogFileWritePolicy;
   }

   public boolean isTransactionLogFileWritePolicySet() {
      return this._isSet(134);
   }

   public void setTransactionLogFileWritePolicy(String var1) throws InvalidAttributeValueException, DistributedManagementException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"Cache-Flush", "Direct-Write"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("TransactionLogFileWritePolicy", var1, var2);
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("TransactionLogFileWritePolicy", var1);
      String var3 = this._TransactionLogFileWritePolicy;
      this._TransactionLogFileWritePolicy = var1;
      this._postSet(134, var3, var1);
   }

   public boolean isNetworkClassLoadingEnabled() {
      return this._NetworkClassLoadingEnabled;
   }

   public boolean isNetworkClassLoadingEnabledSet() {
      return this._isSet(135);
   }

   public void setNetworkClassLoadingEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._NetworkClassLoadingEnabled;
      this._NetworkClassLoadingEnabled = var1;
      this._postSet(135, var2, var1);
   }

   public boolean isEnabledForDomainLog() {
      return this._customizer.isEnabledForDomainLog();
   }

   public boolean isEnabledForDomainLogSet() {
      return this._isSet(136);
   }

   public void setEnabledForDomainLog(boolean var1) throws InvalidAttributeValueException, DistributedManagementException {
      boolean var2 = this.isEnabledForDomainLog();
      this._customizer.setEnabledForDomainLog(var1);
      this._postSet(136, var2, var1);
   }

   public DomainLogFilterMBean getDomainLogFilter() {
      return this._customizer.getDomainLogFilter();
   }

   public String getDomainLogFilterAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getDomainLogFilter();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isDomainLogFilterSet() {
      return this._isSet(137);
   }

   public void setDomainLogFilterAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, DomainLogFilterMBean.class, new ReferenceManager.Resolver(this, 137) {
            public void resolveReference(Object var1) {
               try {
                  ServerMBeanImpl.this.setDomainLogFilter((DomainLogFilterMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         DomainLogFilterMBean var2 = this._DomainLogFilter;
         this._initializeProperty(137);
         this._postSet(137, var2, this._DomainLogFilter);
      }

   }

   public void setDomainLogFilter(DomainLogFilterMBean var1) throws InvalidAttributeValueException, DistributedManagementException {
      DomainLogFilterMBean var2 = this.getDomainLogFilter();
      this._customizer.setDomainLogFilter(var1);
      this._postSet(137, var2, var1);
   }

   public boolean isTunnelingEnabled() {
      return this._TunnelingEnabled;
   }

   public boolean isTunnelingEnabledSet() {
      return this._isSet(138);
   }

   public void setTunnelingEnabled(boolean var1) throws DistributedManagementException {
      boolean var2 = this._TunnelingEnabled;
      this._TunnelingEnabled = var1;
      this._postSet(138, var2, var1);
   }

   public int getTunnelingClientPingSecs() {
      return this._TunnelingClientPingSecs;
   }

   public boolean isTunnelingClientPingSecsSet() {
      return this._isSet(139);
   }

   public void setTunnelingClientPingSecs(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("TunnelingClientPingSecs", var1, 1);
      int var2 = this._TunnelingClientPingSecs;
      this._TunnelingClientPingSecs = var1;
      this._postSet(139, var2, var1);
   }

   public int getTunnelingClientTimeoutSecs() {
      return this._TunnelingClientTimeoutSecs;
   }

   public boolean isTunnelingClientTimeoutSecsSet() {
      return this._isSet(140);
   }

   public void setTunnelingClientTimeoutSecs(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("TunnelingClientTimeoutSecs", var1, 1);
      int var2 = this._TunnelingClientTimeoutSecs;
      this._TunnelingClientTimeoutSecs = var1;
      this._postSet(140, var2, var1);
   }

   public int getAdminReconnectIntervalSeconds() {
      return this._AdminReconnectIntervalSeconds;
   }

   public boolean isAdminReconnectIntervalSecondsSet() {
      return this._isSet(141);
   }

   public void setAdminReconnectIntervalSeconds(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("AdminReconnectIntervalSeconds", (long)var1, 0L, 2147483647L);
      int var2 = this._AdminReconnectIntervalSeconds;
      this._AdminReconnectIntervalSeconds = var1;
      this._postSet(141, var2, var1);
   }

   public boolean isJMSDefaultConnectionFactoriesEnabled() {
      return this._JMSDefaultConnectionFactoriesEnabled;
   }

   public boolean isJMSDefaultConnectionFactoriesEnabledSet() {
      return this._isSet(142);
   }

   public void setJMSDefaultConnectionFactoriesEnabled(boolean var1) throws DistributedManagementException {
      boolean var2 = this._JMSDefaultConnectionFactoriesEnabled;
      this._JMSDefaultConnectionFactoriesEnabled = var1;
      this._postSet(142, var2, var1);
   }

   public ServerLifeCycleRuntimeMBean lookupServerLifeCycleRuntime() {
      return this._customizer.lookupServerLifeCycleRuntime();
   }

   public ServerStartMBean getServerStart() {
      return this._ServerStart;
   }

   public boolean isServerStartSet() {
      return this._isSet(143) || this._isAnythingSet((AbstractDescriptorBean)this.getServerStart());
   }

   public void setServerStart(ServerStartMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 143)) {
         this._postCreate(var2);
      }

      ServerStartMBean var3 = this._ServerStart;
      this._ServerStart = var1;
      this._postSet(143, var3, var1);
   }

   public int getListenDelaySecs() {
      return this._ListenDelaySecs;
   }

   public boolean isListenDelaySecsSet() {
      return this._isSet(144);
   }

   public void setListenDelaySecs(int var1) {
      int var2 = this._ListenDelaySecs;
      this._ListenDelaySecs = var1;
      this._postSet(144, var2, var1);
   }

   public JTAMigratableTargetMBean getJTAMigratableTarget() {
      return this._JTAMigratableTarget;
   }

   public boolean isJTAMigratableTargetSet() {
      return this._isSet(145);
   }

   public void setJTAMigratableTarget(JTAMigratableTargetMBean var1) throws InvalidAttributeValueException {
      if (var1 != null && this.getJTAMigratableTarget() != null && var1 != this.getJTAMigratableTarget()) {
         throw new BeanAlreadyExistsException(this.getJTAMigratableTarget() + " has already been created");
      } else {
         if (var1 != null) {
            AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
            if (this._setParent(var2, this, 145)) {
               this._getReferenceManager().registerBean(var2, true);
               this._postCreate(var2);
            }
         }

         JTAMigratableTargetMBean var3 = this._JTAMigratableTarget;
         this._JTAMigratableTarget = var1;
         this._postSet(145, var3, var1);
      }
   }

   public JTAMigratableTargetMBean createJTAMigratableTarget() {
      JTAMigratableTargetMBeanImpl var1 = new JTAMigratableTargetMBeanImpl(this, -1);

      try {
         this.setJTAMigratableTarget(var1);
         return var1;
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public void destroyJTAMigratableTarget() {
      try {
         AbstractDescriptorBean var1 = (AbstractDescriptorBean)this._JTAMigratableTarget;
         if (var1 != null) {
            List var2 = this._getReferenceManager().getResolvedReferences(var1);
            if (var2 != null && var2.size() > 0) {
               throw new BeanRemoveRejectedException(var1, var2);
            } else {
               this._getReferenceManager().unregisterBean(var1);
               this._markDestroyed(var1);
               this.setJTAMigratableTarget((JTAMigratableTargetMBean)null);
               this._unSet(145);
            }
         }
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public int getLowMemoryTimeInterval() {
      return this._LowMemoryTimeInterval;
   }

   public boolean isLowMemoryTimeIntervalSet() {
      return this._isSet(146);
   }

   public void setLowMemoryTimeInterval(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("LowMemoryTimeInterval", (long)var1, 300L, 2147483647L);
      int var2 = this._LowMemoryTimeInterval;
      this._LowMemoryTimeInterval = var1;
      this._postSet(146, var2, var1);
   }

   public int getLowMemorySampleSize() {
      return this._LowMemorySampleSize;
   }

   public boolean isLowMemorySampleSizeSet() {
      return this._isSet(147);
   }

   public void setLowMemorySampleSize(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("LowMemorySampleSize", (long)var1, 1L, 2147483647L);
      int var2 = this._LowMemorySampleSize;
      this._LowMemorySampleSize = var1;
      this._postSet(147, var2, var1);
   }

   public int getLowMemoryGranularityLevel() {
      return this._LowMemoryGranularityLevel;
   }

   public boolean isLowMemoryGranularityLevelSet() {
      return this._isSet(148);
   }

   public void setLowMemoryGranularityLevel(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("LowMemoryGranularityLevel", (long)var1, 1L, 100L);
      int var2 = this._LowMemoryGranularityLevel;
      this._LowMemoryGranularityLevel = var1;
      this._postSet(148, var2, var1);
   }

   public int getLowMemoryGCThreshold() {
      return this._LowMemoryGCThreshold;
   }

   public boolean isLowMemoryGCThresholdSet() {
      return this._isSet(149);
   }

   public void setLowMemoryGCThreshold(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("LowMemoryGCThreshold", (long)var1, 0L, 99L);
      int var2 = this._LowMemoryGCThreshold;
      this._LowMemoryGCThreshold = var1;
      this._postSet(149, var2, var1);
   }

   public String getStagingDirectoryName() {
      return this._customizer.getStagingDirectoryName();
   }

   public boolean isStagingDirectoryNameSet() {
      return this._isSet(150);
   }

   public void setStagingDirectoryName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this.getStagingDirectoryName();
      this._customizer.setStagingDirectoryName(var1);
      this._postSet(150, var2, var1);
   }

   public String getUploadDirectoryName() {
      return this._customizer.getUploadDirectoryName();
   }

   public boolean isUploadDirectoryNameSet() {
      return this._isSet(151);
   }

   public void setUploadDirectoryName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this.getUploadDirectoryName();
      this._customizer.setUploadDirectoryName(var1);
      this._postSet(151, var2, var1);
   }

   public String getActiveDirectoryName() {
      return this._customizer.getActiveDirectoryName();
   }

   public boolean isActiveDirectoryNameSet() {
      return this._isSet(152);
   }

   public void setActiveDirectoryName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this.getActiveDirectoryName();
      this._customizer.setActiveDirectoryName(var1);
      this._postSet(152, var2, var1);
   }

   public String getStagingMode() {
      if (!this._isSet(153)) {
         try {
            return DeployHelper.determineDefaultStagingMode(this.getName());
         } catch (NullPointerException var2) {
         }
      }

      return this._StagingMode;
   }

   public boolean isStagingModeSet() {
      return this._isSet(153);
   }

   public void setStagingMode(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{ServerMBean.DEFAULT_STAGE, "stage", "nostage", "external_stage"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("StagingMode", var1, var2);
      String var3 = this._StagingMode;
      this._StagingMode = var1;
      this._postSet(153, var3, var1);
   }

   public boolean getAutoRestart() {
      return this._AutoRestart;
   }

   public boolean isAutoRestartSet() {
      return this._isSet(154);
   }

   public void setAutoRestart(boolean var1) {
      boolean var2 = this._AutoRestart;
      this._AutoRestart = var1;
      this._postSet(154, var2, var1);
   }

   public boolean getAutoKillIfFailed() {
      return this._AutoKillIfFailed;
   }

   public boolean isAutoKillIfFailedSet() {
      return this._isSet(155);
   }

   public void setAutoKillIfFailed(boolean var1) {
      boolean var2 = this._AutoKillIfFailed;
      this._AutoKillIfFailed = var1;
      this._postSet(155, var2, var1);
   }

   public int getRestartIntervalSeconds() {
      return this._RestartIntervalSeconds;
   }

   public boolean isRestartIntervalSecondsSet() {
      return this._isSet(156);
   }

   public void setRestartIntervalSeconds(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("RestartIntervalSeconds", (long)var1, 300L, 2147483647L);
      int var2 = this._RestartIntervalSeconds;
      this._RestartIntervalSeconds = var1;
      this._postSet(156, var2, var1);
   }

   public int getRestartMax() {
      return this._RestartMax;
   }

   public boolean isRestartMaxSet() {
      return this._isSet(157);
   }

   public void setRestartMax(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("RestartMax", (long)var1, 0L, 2147483647L);
      int var2 = this._RestartMax;
      this._RestartMax = var1;
      this._postSet(157, var2, var1);
   }

   public int getHealthCheckIntervalSeconds() {
      return this._HealthCheckIntervalSeconds;
   }

   public boolean isHealthCheckIntervalSecondsSet() {
      return this._isSet(158);
   }

   public void setHealthCheckIntervalSeconds(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("HealthCheckIntervalSeconds", (long)var1, 1L, 2147483647L);
      int var2 = this._HealthCheckIntervalSeconds;
      this._HealthCheckIntervalSeconds = var1;
      this._postSet(158, var2, var1);
   }

   public int getHealthCheckTimeoutSeconds() {
      return this._HealthCheckTimeoutSeconds;
   }

   public boolean isHealthCheckTimeoutSecondsSet() {
      return this._isSet(159);
   }

   public void setHealthCheckTimeoutSeconds(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("HealthCheckTimeoutSeconds", (long)var1, 1L, 2147483647L);
      int var2 = this._HealthCheckTimeoutSeconds;
      this._HealthCheckTimeoutSeconds = var1;
      this._postSet(159, var2, var1);
   }

   public int getHealthCheckStartDelaySeconds() {
      return this._HealthCheckStartDelaySeconds;
   }

   public boolean isHealthCheckStartDelaySecondsSet() {
      return this._isSet(160);
   }

   public void setHealthCheckStartDelaySeconds(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("HealthCheckStartDelaySeconds", (long)var1, 0L, 2147483647L);
      int var2 = this._HealthCheckStartDelaySeconds;
      this._HealthCheckStartDelaySeconds = var1;
      this._postSet(160, var2, var1);
   }

   public int getRestartDelaySeconds() {
      return this._RestartDelaySeconds;
   }

   public boolean isRestartDelaySecondsSet() {
      return this._isSet(161);
   }

   public void setRestartDelaySeconds(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("RestartDelaySeconds", (long)var1, 0L, 2147483647L);
      int var2 = this._RestartDelaySeconds;
      this._RestartDelaySeconds = var1;
      this._postSet(161, var2, var1);
   }

   public void setClasspathServletDisabled(boolean var1) {
      boolean var2 = this._ClasspathServletDisabled;
      this._ClasspathServletDisabled = var1;
      this._postSet(162, var2, var1);
   }

   public boolean isClasspathServletDisabled() {
      return this._ClasspathServletDisabled;
   }

   public boolean isClasspathServletDisabledSet() {
      return this._isSet(162);
   }

   public void setDefaultInternalServletsDisabled(boolean var1) {
      boolean var2 = this._DefaultInternalServletsDisabled;
      this._DefaultInternalServletsDisabled = var1;
      this._postSet(163, var2, var1);
   }

   public boolean isDefaultInternalServletsDisabled() {
      return this._DefaultInternalServletsDisabled;
   }

   public boolean isDefaultInternalServletsDisabledSet() {
      return this._isSet(163);
   }

   public String getServerVersion() {
      return this._ServerVersion;
   }

   public boolean isServerVersionSet() {
      return this._isSet(164);
   }

   public void setServerVersion(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ServerVersion;
      this._ServerVersion = var1;
      this._postSet(164, var2, var1);
   }

   public void setStartupMode(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this.getStartupMode();
      this._customizer.setStartupMode(var1);
      this._postSet(165, var2, var1);
   }

   public String getStartupMode() {
      return this._customizer.getStartupMode();
   }

   public boolean isStartupModeSet() {
      return this._isSet(165);
   }

   public void setServerLifeCycleTimeoutVal(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkMin("ServerLifeCycleTimeoutVal", var1, 0);
      int var2 = this._ServerLifeCycleTimeoutVal;
      this._ServerLifeCycleTimeoutVal = var1;
      this._postSet(166, var2, var1);
   }

   public int getServerLifeCycleTimeoutVal() {
      if (!this._isSet(166)) {
         return this._isProductionModeEnabled() ? 120 : 30;
      } else {
         return this._ServerLifeCycleTimeoutVal;
      }
   }

   public boolean isServerLifeCycleTimeoutValSet() {
      return this._isSet(166);
   }

   public void setGracefulShutdownTimeout(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkMin("GracefulShutdownTimeout", var1, 0);
      int var2 = this._GracefulShutdownTimeout;
      this._GracefulShutdownTimeout = var1;
      this._postSet(168, var2, var1);
   }

   public void setStartupTimeout(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkMin("StartupTimeout", var1, 0);
      int var2 = this._StartupTimeout;
      this._StartupTimeout = var1;
      this._postSet(167, var2, var1);
   }

   public int getStartupTimeout() {
      if (!this._isSet(167)) {
         return this._isProductionModeEnabled() ? 0 : 0;
      } else {
         return this._StartupTimeout;
      }
   }

   public boolean isStartupTimeoutSet() {
      return this._isSet(167);
   }

   public int getGracefulShutdownTimeout() {
      return this._GracefulShutdownTimeout;
   }

   public boolean isGracefulShutdownTimeoutSet() {
      return this._isSet(168);
   }

   public boolean isIgnoreSessionsDuringShutdown() {
      return this._IgnoreSessionsDuringShutdown;
   }

   public boolean isIgnoreSessionsDuringShutdownSet() {
      return this._isSet(169);
   }

   public void setIgnoreSessionsDuringShutdown(boolean var1) {
      boolean var2 = this._IgnoreSessionsDuringShutdown;
      this._IgnoreSessionsDuringShutdown = var1;
      this._postSet(169, var2, var1);
   }

   public boolean isManagedServerIndependenceEnabled() {
      return this._ManagedServerIndependenceEnabled;
   }

   public boolean isManagedServerIndependenceEnabledSet() {
      return this._isSet(170);
   }

   public void setManagedServerIndependenceEnabled(boolean var1) {
      boolean var2 = this._ManagedServerIndependenceEnabled;
      this._ManagedServerIndependenceEnabled = var1;
      this._postSet(170, var2, var1);
   }

   public boolean isMSIFileReplicationEnabled() {
      return this._MSIFileReplicationEnabled;
   }

   public boolean isMSIFileReplicationEnabledSet() {
      return this._isSet(171);
   }

   public void setMSIFileReplicationEnabled(boolean var1) {
      boolean var2 = this._MSIFileReplicationEnabled;
      this._MSIFileReplicationEnabled = var1;
      this._postSet(171, var2, var1);
   }

   public void setClientCertProxyEnabled(boolean var1) {
      boolean var2 = this._ClientCertProxyEnabled;
      this._ClientCertProxyEnabled = var1;
      this._postSet(172, var2, var1);
   }

   public boolean isClientCertProxyEnabled() {
      return this._ClientCertProxyEnabled;
   }

   public boolean isClientCertProxyEnabledSet() {
      return this._isSet(172);
   }

   public void setWeblogicPluginEnabled(boolean var1) {
      boolean var2 = this._WeblogicPluginEnabled;
      this._WeblogicPluginEnabled = var1;
      this._postSet(173, var2, var1);
   }

   public boolean isWeblogicPluginEnabled() {
      return this._WeblogicPluginEnabled;
   }

   public boolean isWeblogicPluginEnabledSet() {
      return this._isSet(173);
   }

   public void setHostsMigratableServices(boolean var1) {
      boolean var2 = this._HostsMigratableServices;
      this._HostsMigratableServices = var1;
      this._postSet(174, var2, var1);
   }

   public boolean getHostsMigratableServices() {
      return this._HostsMigratableServices;
   }

   public boolean isHostsMigratableServicesSet() {
      return this._isSet(174);
   }

   public void setHttpTraceSupportEnabled(boolean var1) {
      boolean var2 = this._HttpTraceSupportEnabled;
      this._HttpTraceSupportEnabled = var1;
      this._postSet(175, var2, var1);
   }

   public boolean isHttpTraceSupportEnabled() {
      return this._HttpTraceSupportEnabled;
   }

   public boolean isHttpTraceSupportEnabledSet() {
      return this._isSet(175);
   }

   public String getKeyStores() {
      return this._KeyStores;
   }

   public boolean isKeyStoresSet() {
      return this._isSet(176);
   }

   public void setKeyStores(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"DemoIdentityAndDemoTrust", "CustomIdentityAndJavaStandardTrust", "CustomIdentityAndCustomTrust", "CustomIdentityAndCommandLineTrust"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("KeyStores", var1, var2);
      String var3 = this._KeyStores;
      this._KeyStores = var1;
      this._postSet(176, var3, var1);
   }

   public String getCustomIdentityKeyStoreFileName() {
      return this._CustomIdentityKeyStoreFileName;
   }

   public boolean isCustomIdentityKeyStoreFileNameSet() {
      return this._isSet(177);
   }

   public void setCustomIdentityKeyStoreFileName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._CustomIdentityKeyStoreFileName;
      this._CustomIdentityKeyStoreFileName = var1;
      this._postSet(177, var2, var1);
   }

   public String getCustomIdentityKeyStoreType() {
      return this._CustomIdentityKeyStoreType;
   }

   public boolean isCustomIdentityKeyStoreTypeSet() {
      return this._isSet(178);
   }

   public void setCustomIdentityKeyStoreType(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._CustomIdentityKeyStoreType;
      this._CustomIdentityKeyStoreType = var1;
      this._postSet(178, var2, var1);
   }

   public String getCustomIdentityKeyStorePassPhrase() {
      byte[] var1 = this.getCustomIdentityKeyStorePassPhraseEncrypted();
      return var1 == null ? null : this._decrypt("CustomIdentityKeyStorePassPhrase", var1);
   }

   public boolean isCustomIdentityKeyStorePassPhraseSet() {
      return this.isCustomIdentityKeyStorePassPhraseEncryptedSet();
   }

   public void setCustomIdentityKeyStorePassPhrase(String var1) {
      var1 = var1 == null ? null : var1.trim();
      this.setCustomIdentityKeyStorePassPhraseEncrypted(var1 == null ? null : this._encrypt("CustomIdentityKeyStorePassPhrase", var1));
   }

   public byte[] getCustomIdentityKeyStorePassPhraseEncrypted() {
      return this._getHelper()._cloneArray(this._CustomIdentityKeyStorePassPhraseEncrypted);
   }

   public String getCustomIdentityKeyStorePassPhraseEncryptedAsString() {
      byte[] var1 = this.getCustomIdentityKeyStorePassPhraseEncrypted();
      return var1 == null ? null : new String(var1);
   }

   public boolean isCustomIdentityKeyStorePassPhraseEncryptedSet() {
      return this._isSet(180);
   }

   public void setCustomIdentityKeyStorePassPhraseEncryptedAsString(String var1) {
      try {
         byte[] var2 = var1 == null ? null : var1.getBytes();
         this.setCustomIdentityKeyStorePassPhraseEncrypted(var2);
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public String getCustomTrustKeyStoreFileName() {
      return this._CustomTrustKeyStoreFileName;
   }

   public boolean isCustomTrustKeyStoreFileNameSet() {
      return this._isSet(181);
   }

   public void setCustomTrustKeyStoreFileName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._CustomTrustKeyStoreFileName;
      this._CustomTrustKeyStoreFileName = var1;
      this._postSet(181, var2, var1);
   }

   public String getCustomTrustKeyStoreType() {
      return this._CustomTrustKeyStoreType;
   }

   public boolean isCustomTrustKeyStoreTypeSet() {
      return this._isSet(182);
   }

   public void setCustomTrustKeyStoreType(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._CustomTrustKeyStoreType;
      this._CustomTrustKeyStoreType = var1;
      this._postSet(182, var2, var1);
   }

   public String getCustomTrustKeyStorePassPhrase() {
      byte[] var1 = this.getCustomTrustKeyStorePassPhraseEncrypted();
      return var1 == null ? null : this._decrypt("CustomTrustKeyStorePassPhrase", var1);
   }

   public boolean isCustomTrustKeyStorePassPhraseSet() {
      return this.isCustomTrustKeyStorePassPhraseEncryptedSet();
   }

   public void setCustomTrustKeyStorePassPhrase(String var1) {
      var1 = var1 == null ? null : var1.trim();
      this.setCustomTrustKeyStorePassPhraseEncrypted(var1 == null ? null : this._encrypt("CustomTrustKeyStorePassPhrase", var1));
   }

   public byte[] getCustomTrustKeyStorePassPhraseEncrypted() {
      return this._getHelper()._cloneArray(this._CustomTrustKeyStorePassPhraseEncrypted);
   }

   public String getCustomTrustKeyStorePassPhraseEncryptedAsString() {
      byte[] var1 = this.getCustomTrustKeyStorePassPhraseEncrypted();
      return var1 == null ? null : new String(var1);
   }

   public boolean isCustomTrustKeyStorePassPhraseEncryptedSet() {
      return this._isSet(184);
   }

   public void setCustomTrustKeyStorePassPhraseEncryptedAsString(String var1) {
      try {
         byte[] var2 = var1 == null ? null : var1.getBytes();
         this.setCustomTrustKeyStorePassPhraseEncrypted(var2);
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public String getJavaStandardTrustKeyStorePassPhrase() {
      byte[] var1 = this.getJavaStandardTrustKeyStorePassPhraseEncrypted();
      return var1 == null ? null : this._decrypt("JavaStandardTrustKeyStorePassPhrase", var1);
   }

   public boolean isJavaStandardTrustKeyStorePassPhraseSet() {
      return this.isJavaStandardTrustKeyStorePassPhraseEncryptedSet();
   }

   public void setJavaStandardTrustKeyStorePassPhrase(String var1) {
      var1 = var1 == null ? null : var1.trim();
      this.setJavaStandardTrustKeyStorePassPhraseEncrypted(var1 == null ? null : this._encrypt("JavaStandardTrustKeyStorePassPhrase", var1));
   }

   public byte[] getJavaStandardTrustKeyStorePassPhraseEncrypted() {
      return this._getHelper()._cloneArray(this._JavaStandardTrustKeyStorePassPhraseEncrypted);
   }

   public String getJavaStandardTrustKeyStorePassPhraseEncryptedAsString() {
      byte[] var1 = this.getJavaStandardTrustKeyStorePassPhraseEncrypted();
      return var1 == null ? null : new String(var1);
   }

   public boolean isJavaStandardTrustKeyStorePassPhraseEncryptedSet() {
      return this._isSet(186);
   }

   public void setJavaStandardTrustKeyStorePassPhraseEncryptedAsString(String var1) {
      try {
         byte[] var2 = var1 == null ? null : var1.getBytes();
         this.setJavaStandardTrustKeyStorePassPhraseEncrypted(var2);
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public void setReliableDeliveryPolicy(WSReliableDeliveryPolicyMBean var1) {
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 187, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return ServerMBeanImpl.this.getReliableDeliveryPolicy();
            }
         };
         this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1, var2);
      }

      WSReliableDeliveryPolicyMBean var3 = this._ReliableDeliveryPolicy;
      this._ReliableDeliveryPolicy = var1;
      this._postSet(187, var3, var1);
   }

   public WSReliableDeliveryPolicyMBean getReliableDeliveryPolicy() {
      return this._ReliableDeliveryPolicy;
   }

   public String getReliableDeliveryPolicyAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getReliableDeliveryPolicy();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isReliableDeliveryPolicySet() {
      return this._isSet(187);
   }

   public void setReliableDeliveryPolicyAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, WSReliableDeliveryPolicyMBean.class, new ReferenceManager.Resolver(this, 187) {
            public void resolveReference(Object var1) {
               try {
                  ServerMBeanImpl.this.setReliableDeliveryPolicy((WSReliableDeliveryPolicyMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         WSReliableDeliveryPolicyMBean var2 = this._ReliableDeliveryPolicy;
         this._initializeProperty(187);
         this._postSet(187, var2, this._ReliableDeliveryPolicy);
      }

   }

   public boolean isMessageIdPrefixEnabled() {
      return this._MessageIdPrefixEnabled;
   }

   public boolean isMessageIdPrefixEnabledSet() {
      return this._isSet(188);
   }

   public void setMessageIdPrefixEnabled(boolean var1) {
      boolean var2 = this.isMessageIdPrefixEnabled();
      this._customizer.setMessageIdPrefixEnabled(var1);
      this._postSet(188, var2, var1);
   }

   public DefaultFileStoreMBean getDefaultFileStore() {
      return this._DefaultFileStore;
   }

   public boolean isDefaultFileStoreSet() {
      return this._isSet(189) || this._isAnythingSet((AbstractDescriptorBean)this.getDefaultFileStore());
   }

   public void setDefaultFileStore(DefaultFileStoreMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 189)) {
         this._postCreate(var2);
      }

      DefaultFileStoreMBean var3 = this._DefaultFileStore;
      this._DefaultFileStore = var1;
      this._postSet(189, var3, var1);
   }

   public void addCandidateMachine(MachineMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 190)) {
         MachineMBean[] var2;
         if (this._isSet(190)) {
            var2 = (MachineMBean[])((MachineMBean[])this._getHelper()._extendArray(this.getCandidateMachines(), MachineMBean.class, var1));
         } else {
            var2 = new MachineMBean[]{var1};
         }

         try {
            this.setCandidateMachines(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public MachineMBean[] getCandidateMachines() {
      return this._CandidateMachines;
   }

   public String getCandidateMachinesAsString() {
      return this._getHelper()._serializeKeyList(this.getCandidateMachines());
   }

   public boolean isCandidateMachinesSet() {
      return this._isSet(190);
   }

   public void removeCandidateMachine(MachineMBean var1) {
      MachineMBean[] var2 = this.getCandidateMachines();
      MachineMBean[] var3 = (MachineMBean[])((MachineMBean[])this._getHelper()._removeElement(var2, MachineMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setCandidateMachines(var3);
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            }

            throw new UndeclaredThrowableException(var5);
         }
      }

   }

   public void setCandidateMachinesAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         String[] var13 = this._getHelper()._splitKeyList(var1);
         List var3 = this._getHelper()._getKeyList(this._CandidateMachines);

         String var5;
         for(int var4 = 0; var4 < var13.length; ++var4) {
            var5 = var13[var4];
            var5 = var5 == null ? null : var5.trim();
            if (var3.contains(var5)) {
               var3.remove(var5);
            } else {
               this._getReferenceManager().registerUnresolvedReference(var5, MachineMBean.class, new ReferenceManager.Resolver(this, 190) {
                  public void resolveReference(Object var1) {
                     try {
                        ServerMBeanImpl.this.addCandidateMachine((MachineMBean)var1);
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
               MachineMBean[] var6 = this._CandidateMachines;
               int var7 = var6.length;

               for(int var8 = 0; var8 < var7; ++var8) {
                  MachineMBean var9 = var6[var8];
                  if (var5.equals(var9.getName())) {
                     try {
                        this.removeCandidateMachine(var9);
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
         MachineMBean[] var2 = this._CandidateMachines;
         this._initializeProperty(190);
         this._postSet(190, var2, this._CandidateMachines);
      }
   }

   public void setCandidateMachines(MachineMBean[] var1) {
      Object var4 = var1 == null ? new MachineMBeanImpl[0] : var1;
      var1 = (MachineMBean[])((MachineMBean[])this._getHelper()._cleanAndValidateArray(var4, MachineMBean.class));

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2] != null) {
            ResolvedReference var3 = new ResolvedReference(this, 190, (AbstractDescriptorBean)var1[var2]) {
               protected Object getPropertyValue() {
                  return ServerMBeanImpl.this.getCandidateMachines();
               }
            };
            this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1[var2], var3);
         }
      }

      MachineMBean[] var5 = this._CandidateMachines;
      this._CandidateMachines = var1;
      this._postSet(190, var5, var1);
   }

   public OverloadProtectionMBean getOverloadProtection() {
      return this._OverloadProtection;
   }

   public boolean isOverloadProtectionSet() {
      return this._isSet(191) || this._isAnythingSet((AbstractDescriptorBean)this.getOverloadProtection());
   }

   public void setOverloadProtection(OverloadProtectionMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 191)) {
         this._postCreate(var2);
      }

      OverloadProtectionMBean var3 = this._OverloadProtection;
      this._OverloadProtection = var1;
      this._postSet(191, var3, var1);
   }

   public String getJDBCLLRTableName() {
      return this._JDBCLLRTableName;
   }

   public boolean isJDBCLLRTableNameSet() {
      return this._isSet(192);
   }

   public void setJDBCLLRTableName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._JDBCLLRTableName;
      this._JDBCLLRTableName = var1;
      this._postSet(192, var2, var1);
   }

   public boolean isUseFusionForLLR() {
      return this._UseFusionForLLR;
   }

   public boolean isUseFusionForLLRSet() {
      return this._isSet(193);
   }

   public void setUseFusionForLLR(boolean var1) {
      boolean var2 = this._UseFusionForLLR;
      this._UseFusionForLLR = var1;
      this._postSet(193, var2, var1);
   }

   public int getJDBCLLRTableXIDColumnSize() {
      return this._JDBCLLRTableXIDColumnSize;
   }

   public boolean isJDBCLLRTableXIDColumnSizeSet() {
      return this._isSet(194);
   }

   public void setJDBCLLRTableXIDColumnSize(int var1) {
      int var2 = this._JDBCLLRTableXIDColumnSize;
      this._JDBCLLRTableXIDColumnSize = var1;
      this._postSet(194, var2, var1);
   }

   public int getJDBCLLRTablePoolColumnSize() {
      return this._JDBCLLRTablePoolColumnSize;
   }

   public boolean isJDBCLLRTablePoolColumnSizeSet() {
      return this._isSet(195);
   }

   public void setJDBCLLRTablePoolColumnSize(int var1) {
      int var2 = this._JDBCLLRTablePoolColumnSize;
      this._JDBCLLRTablePoolColumnSize = var1;
      this._postSet(195, var2, var1);
   }

   public int getJDBCLLRTableRecordColumnSize() {
      return this._JDBCLLRTableRecordColumnSize;
   }

   public boolean isJDBCLLRTableRecordColumnSizeSet() {
      return this._isSet(196);
   }

   public void setJDBCLLRTableRecordColumnSize(int var1) {
      int var2 = this._JDBCLLRTableRecordColumnSize;
      this._JDBCLLRTableRecordColumnSize = var1;
      this._postSet(196, var2, var1);
   }

   public int getJDBCLoginTimeoutSeconds() {
      return this._JDBCLoginTimeoutSeconds;
   }

   public boolean isJDBCLoginTimeoutSecondsSet() {
      return this._isSet(197);
   }

   public void setJDBCLoginTimeoutSeconds(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("JDBCLoginTimeoutSeconds", (long)var1, 0L, 300L);
      int var2 = this._JDBCLoginTimeoutSeconds;
      this._JDBCLoginTimeoutSeconds = var1;
      this._postSet(197, var2, var1);
   }

   public WLDFServerDiagnosticMBean getServerDiagnosticConfig() {
      return this._ServerDiagnosticConfig;
   }

   public boolean isServerDiagnosticConfigSet() {
      return this._isSet(198) || this._isAnythingSet((AbstractDescriptorBean)this.getServerDiagnosticConfig());
   }

   public void setServerDiagnosticConfig(WLDFServerDiagnosticMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 198)) {
         this._postCreate(var2);
      }

      WLDFServerDiagnosticMBean var3 = this._ServerDiagnosticConfig;
      this._ServerDiagnosticConfig = var1;
      this._postSet(198, var3, var1);
   }

   public void setAutoJDBCConnectionClose(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._AutoJDBCConnectionClose;
      this._AutoJDBCConnectionClose = var1;
      this._postSet(199, var2, var1);
   }

   public String getAutoJDBCConnectionClose() {
      return this._AutoJDBCConnectionClose;
   }

   public boolean isAutoJDBCConnectionCloseSet() {
      return this._isSet(199);
   }

   public String[] getSupportedProtocols() {
      return this._customizer.getSupportedProtocols();
   }

   public boolean isSupportedProtocolsSet() {
      return this._isSet(200);
   }

   public void setSupportedProtocols(String[] var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      String[] var2 = this._SupportedProtocols;
      this._SupportedProtocols = var1;
      this._postSet(200, var2, var1);
   }

   public String getDefaultStagingDirName() {
      return this._customizer.getDefaultStagingDirName();
   }

   public boolean isDefaultStagingDirNameSet() {
      return this._isSet(201);
   }

   public void setDefaultStagingDirName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._DefaultStagingDirName = var1;
   }

   public String get81StyleDefaultStagingDirName() {
      return this._customizer.get81StyleDefaultStagingDirName();
   }

   public boolean is81StyleDefaultStagingDirNameSet() {
      return this._isSet(202);
   }

   public void set81StyleDefaultStagingDirName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._81StyleDefaultStagingDirName = var1;
   }

   public FederationServicesMBean getFederationServices() {
      return this._FederationServices;
   }

   public boolean isFederationServicesSet() {
      return this._isSet(203) || this._isAnythingSet((AbstractDescriptorBean)this.getFederationServices());
   }

   public void setFederationServices(FederationServicesMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 203)) {
         this._postCreate(var2);
      }

      FederationServicesMBean var3 = this._FederationServices;
      this._FederationServices = var1;
      this._postSet(203, var3, var1);
   }

   public SingleSignOnServicesMBean getSingleSignOnServices() {
      return this._SingleSignOnServices;
   }

   public boolean isSingleSignOnServicesSet() {
      return this._isSet(204) || this._isAnythingSet((AbstractDescriptorBean)this.getSingleSignOnServices());
   }

   public void setSingleSignOnServices(SingleSignOnServicesMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 204)) {
         this._postCreate(var2);
      }

      SingleSignOnServicesMBean var3 = this._SingleSignOnServices;
      this._SingleSignOnServices = var1;
      this._postSet(204, var3, var1);
   }

   public WebServiceMBean getWebService() {
      return this._WebService;
   }

   public boolean isWebServiceSet() {
      return this._isSet(205) || this._isAnythingSet((AbstractDescriptorBean)this.getWebService());
   }

   public void setWebService(WebServiceMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 205)) {
         this._postCreate(var2);
      }

      WebServiceMBean var3 = this._WebService;
      this._WebService = var1;
      this._postSet(205, var3, var1);
   }

   public int getNMSocketCreateTimeoutInMillis() {
      return this._NMSocketCreateTimeoutInMillis;
   }

   public boolean isNMSocketCreateTimeoutInMillisSet() {
      return this._isSet(206);
   }

   public void setNMSocketCreateTimeoutInMillis(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("NMSocketCreateTimeoutInMillis", var1, 0);
      int var2 = this._NMSocketCreateTimeoutInMillis;
      this._NMSocketCreateTimeoutInMillis = var1;
      this._postSet(206, var2, var1);
   }

   public void setCoherenceClusterSystemResource(CoherenceClusterSystemResourceMBean var1) {
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 207, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return ServerMBeanImpl.this.getCoherenceClusterSystemResource();
            }
         };
         this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1, var2);
      }

      CoherenceClusterSystemResourceMBean var3 = this.getCoherenceClusterSystemResource();
      this._customizer.setCoherenceClusterSystemResource(var1);
      this._postSet(207, var3, var1);
   }

   public CoherenceClusterSystemResourceMBean getCoherenceClusterSystemResource() {
      return this._customizer.getCoherenceClusterSystemResource();
   }

   public String getCoherenceClusterSystemResourceAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getCoherenceClusterSystemResource();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isCoherenceClusterSystemResourceSet() {
      return this._isSet(207);
   }

   public void setCoherenceClusterSystemResourceAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, CoherenceClusterSystemResourceMBean.class, new ReferenceManager.Resolver(this, 207) {
            public void resolveReference(Object var1) {
               try {
                  ServerMBeanImpl.this.setCoherenceClusterSystemResource((CoherenceClusterSystemResourceMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         CoherenceClusterSystemResourceMBean var2 = this._CoherenceClusterSystemResource;
         this._initializeProperty(207);
         this._postSet(207, var2, this._CoherenceClusterSystemResource);
      }

   }

   public void setVirtualMachineName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._VirtualMachineName;
      this._VirtualMachineName = var1;
      this._postSet(208, var2, var1);
   }

   public String getVirtualMachineName() {
      if (!this._isSet(208)) {
         try {
            return ((DomainMBean)this.getParent()).getName() + "_" + this.getName();
         } catch (NullPointerException var2) {
         }
      }

      return this._VirtualMachineName;
   }

   public boolean isVirtualMachineNameSet() {
      return this._isSet(208);
   }

   public String getReplicationPorts() {
      return this._ReplicationPorts;
   }

   public boolean isReplicationPortsSet() {
      return this._isSet(209);
   }

   public void setReplicationPorts(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ReplicationPorts;
      this._ReplicationPorts = var1;
      this._postSet(209, var2, var1);
   }

   public TransactionLogJDBCStoreMBean getTransactionLogJDBCStore() {
      return this._TransactionLogJDBCStore;
   }

   public boolean isTransactionLogJDBCStoreSet() {
      return this._isSet(210) || this._isAnythingSet((AbstractDescriptorBean)this.getTransactionLogJDBCStore());
   }

   public void setTransactionLogJDBCStore(TransactionLogJDBCStoreMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 210)) {
         this._postCreate(var2);
      }

      TransactionLogJDBCStoreMBean var3 = this._TransactionLogJDBCStore;
      this._TransactionLogJDBCStore = var1;
      this._postSet(210, var3, var1);
   }

   public DataSourceMBean getDataSource() {
      return this._DataSource;
   }

   public boolean isDataSourceSet() {
      return this._isSet(211) || this._isAnythingSet((AbstractDescriptorBean)this.getDataSource());
   }

   public void setDataSource(DataSourceMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 211)) {
         this._postCreate(var2);
      }

      DataSourceMBean var3 = this._DataSource;
      this._DataSource = var1;
      this._postSet(211, var3, var1);
   }

   public Object _getKey() {
      return this.getName();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
      ServerLegalHelper.validateServer(this);
      LegalHelper.validateListenPorts(this);
   }

   public void setCustomIdentityKeyStorePassPhraseEncrypted(byte[] var1) {
      byte[] var2 = this._CustomIdentityKeyStorePassPhraseEncrypted;
      if (this._isProductionModeEnabled() && var1 != null && !this._isEncrypted(var1)) {
         throw new IllegalArgumentException("In production mode, it's not allowed to set a clear text value to the property: CustomIdentityKeyStorePassPhraseEncrypted of ServerMBean");
      } else {
         this._getHelper()._clearArray(this._CustomIdentityKeyStorePassPhraseEncrypted);
         this._CustomIdentityKeyStorePassPhraseEncrypted = this._getHelper()._cloneArray(var1);
         this._postSet(180, var2, var1);
      }
   }

   public void setCustomTrustKeyStorePassPhraseEncrypted(byte[] var1) {
      byte[] var2 = this._CustomTrustKeyStorePassPhraseEncrypted;
      if (this._isProductionModeEnabled() && var1 != null && !this._isEncrypted(var1)) {
         throw new IllegalArgumentException("In production mode, it's not allowed to set a clear text value to the property: CustomTrustKeyStorePassPhraseEncrypted of ServerMBean");
      } else {
         this._getHelper()._clearArray(this._CustomTrustKeyStorePassPhraseEncrypted);
         this._CustomTrustKeyStorePassPhraseEncrypted = this._getHelper()._cloneArray(var1);
         this._postSet(184, var2, var1);
      }
   }

   public void setDefaultIIOPPasswordEncrypted(byte[] var1) {
      byte[] var2 = this._DefaultIIOPPasswordEncrypted;
      if (this._isProductionModeEnabled() && var1 != null && !this._isEncrypted(var1)) {
         throw new IllegalArgumentException("In production mode, it's not allowed to set a clear text value to the property: DefaultIIOPPasswordEncrypted of ServerMBean");
      } else {
         this._getHelper()._clearArray(this._DefaultIIOPPasswordEncrypted);
         this._DefaultIIOPPasswordEncrypted = this._getHelper()._cloneArray(var1);
         this._postSet(99, var2, var1);
      }
   }

   public void setDefaultTGIOPPasswordEncrypted(byte[] var1) {
      byte[] var2 = this._DefaultTGIOPPasswordEncrypted;
      if (this._isProductionModeEnabled() && var1 != null && !this._isEncrypted(var1)) {
         throw new IllegalArgumentException("In production mode, it's not allowed to set a clear text value to the property: DefaultTGIOPPasswordEncrypted of ServerMBean");
      } else {
         this._getHelper()._clearArray(this._DefaultTGIOPPasswordEncrypted);
         this._DefaultTGIOPPasswordEncrypted = this._getHelper()._cloneArray(var1);
         this._postSet(103, var2, var1);
      }
   }

   public void setJavaStandardTrustKeyStorePassPhraseEncrypted(byte[] var1) {
      byte[] var2 = this._JavaStandardTrustKeyStorePassPhraseEncrypted;
      if (this._isProductionModeEnabled() && var1 != null && !this._isEncrypted(var1)) {
         throw new IllegalArgumentException("In production mode, it's not allowed to set a clear text value to the property: JavaStandardTrustKeyStorePassPhraseEncrypted of ServerMBean");
      } else {
         this._getHelper()._clearArray(this._JavaStandardTrustKeyStorePassPhraseEncrypted);
         this._JavaStandardTrustKeyStorePassPhraseEncrypted = this._getHelper()._cloneArray(var1);
         this._postSet(186, var2, var1);
      }
   }

   public void setSystemPasswordEncrypted(byte[] var1) {
      byte[] var2 = this._SystemPasswordEncrypted;
      if (this._isProductionModeEnabled() && var1 != null && !this._isEncrypted(var1)) {
         throw new IllegalArgumentException("In production mode, it's not allowed to set a clear text value to the property: SystemPasswordEncrypted of ServerMBean");
      } else {
         this._getHelper()._clearArray(this._SystemPasswordEncrypted);
         this._SystemPasswordEncrypted = this._getHelper()._cloneArray(var1);
         this._postSet(110, var2, var1);
      }
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
         if (var1 == 179) {
            this._markSet(180, false);
         }

         if (var1 == 183) {
            this._markSet(184, false);
         }

         if (var1 == 98) {
            this._markSet(99, false);
         }

         if (var1 == 102) {
            this._markSet(103, false);
         }

         if (var1 == 185) {
            this._markSet(186, false);
         }

         if (var1 == 109) {
            this._markSet(110, false);
         }
      }

   }

   protected AbstractDescriptorBeanHelper _createHelper() {
      return new Helper(this);
   }

   public boolean _isAnythingSet() {
      return super._isAnythingSet() || this.isCOMSet() || this.isDataSourceSet() || this.isDefaultFileStoreSet() || this.isFederationServicesSet() || this.isOverloadProtectionSet() || this.isServerDebugSet() || this.isServerDiagnosticConfigSet() || this.isServerStartSet() || this.isSingleSignOnServicesSet() || this.isTransactionLogJDBCStoreSet() || this.isWebServerSet() || this.isWebServiceSet();
   }

   private boolean _initializeProperty(int var1) {
      boolean var2 = var1 > -1;
      if (!var2) {
         var1 = 202;
      }

      try {
         switch (var1) {
            case 202:
               this._81StyleDefaultStagingDirName = null;
               if (var2) {
                  break;
               }
            case 118:
               this._AcceptBacklog = 300;
               if (var2) {
                  break;
               }
            case 152:
               this._customizer.setActiveDirectoryName((String)null);
               if (var2) {
                  break;
               }
            case 141:
               this._AdminReconnectIntervalSeconds = 10;
               if (var2) {
                  break;
               }
            case 122:
               this._customizer.setAdministrationPort(0);
               if (var2) {
                  break;
               }
            case 199:
               this._AutoJDBCConnectionClose = "false";
               if (var2) {
                  break;
               }
            case 155:
               this._AutoKillIfFailed = false;
               if (var2) {
                  break;
               }
            case 154:
               this._AutoRestart = true;
               if (var2) {
                  break;
               }
            case 106:
               this._COM = new COMMBeanImpl(this, 106);
               this._postCreate((AbstractDescriptorBean)this._COM);
               if (var2) {
                  break;
               }
            case 190:
               this._CandidateMachines = new MachineMBean[0];
               if (var2) {
                  break;
               }
            case 83:
               this._customizer.setCluster((ClusterMBean)null);
               if (var2) {
                  break;
               }
            case 89:
               this._ClusterRuntime = null;
               if (var2) {
                  break;
               }
            case 84:
               this._ClusterWeight = 100;
               if (var2) {
                  break;
               }
            case 207:
               this._customizer.setCoherenceClusterSystemResource((CoherenceClusterSystemResourceMBean)null);
               if (var2) {
                  break;
               }
            case 87:
               this._ConsensusProcessIdentifier = -1;
               if (var2) {
                  break;
               }
            case 177:
               this._CustomIdentityKeyStoreFileName = null;
               if (var2) {
                  break;
               }
            case 179:
               this._CustomIdentityKeyStorePassPhraseEncrypted = null;
               if (var2) {
                  break;
               }
            case 180:
               this._CustomIdentityKeyStorePassPhraseEncrypted = null;
               if (var2) {
                  break;
               }
            case 178:
               this._CustomIdentityKeyStoreType = null;
               if (var2) {
                  break;
               }
            case 181:
               this._CustomTrustKeyStoreFileName = null;
               if (var2) {
                  break;
               }
            case 183:
               this._CustomTrustKeyStorePassPhraseEncrypted = null;
               if (var2) {
                  break;
               }
            case 184:
               this._CustomTrustKeyStorePassPhraseEncrypted = null;
               if (var2) {
                  break;
               }
            case 182:
               this._CustomTrustKeyStoreType = null;
               if (var2) {
                  break;
               }
            case 211:
               this._DataSource = new DataSourceMBeanImpl(this, 211);
               this._postCreate((AbstractDescriptorBean)this._DataSource);
               if (var2) {
                  break;
               }
            case 189:
               this._DefaultFileStore = new DefaultFileStoreMBeanImpl(this, 189);
               this._postCreate((AbstractDescriptorBean)this._DefaultFileStore);
               if (var2) {
                  break;
               }
            case 98:
               this._DefaultIIOPPasswordEncrypted = null;
               if (var2) {
                  break;
               }
            case 99:
               this._DefaultIIOPPasswordEncrypted = null;
               if (var2) {
                  break;
               }
            case 97:
               this._DefaultIIOPUser = null;
               if (var2) {
                  break;
               }
            case 201:
               this._DefaultStagingDirName = null;
               if (var2) {
                  break;
               }
            case 102:
               this._DefaultTGIOPPasswordEncrypted = null;
               if (var2) {
                  break;
               }
            case 103:
               this._DefaultTGIOPPasswordEncrypted = null;
               if (var2) {
                  break;
               }
            case 101:
               this._DefaultTGIOPUser = "guest";
               if (var2) {
                  break;
               }
            case 137:
               this._customizer.setDomainLogFilter((DomainLogFilterMBean)null);
               if (var2) {
                  break;
               }
            case 91:
               this._ExpectedToRun = true;
               if (var2) {
                  break;
               }
            case 115:
               this._ExternalDNSName = null;
               if (var2) {
                  break;
               }
            case 131:
               this._ExtraEjbcOptions = null;
               if (var2) {
                  break;
               }
            case 130:
               this._ExtraRmicOptions = null;
               if (var2) {
                  break;
               }
            case 203:
               this._FederationServices = new FederationServicesMBeanImpl(this, 203);
               this._postCreate((AbstractDescriptorBean)this._FederationServices);
               if (var2) {
                  break;
               }
            case 168:
               this._GracefulShutdownTimeout = 0;
               if (var2) {
                  break;
               }
            case 158:
               this._HealthCheckIntervalSeconds = 180;
               if (var2) {
                  break;
               }
            case 160:
               this._HealthCheckStartDelaySeconds = 120;
               if (var2) {
                  break;
               }
            case 159:
               this._HealthCheckTimeoutSeconds = 60;
               if (var2) {
                  break;
               }
            case 174:
               this._HostsMigratableServices = true;
               if (var2) {
                  break;
               }
            case 124:
               this._IIOPConnectionPools = null;
               if (var2) {
                  break;
               }
            case 116:
               this._InterfaceAddress = null;
               if (var2) {
                  break;
               }
            case 192:
               this._JDBCLLRTableName = null;
               if (var2) {
                  break;
               }
            case 195:
               this._JDBCLLRTablePoolColumnSize = 64;
               if (var2) {
                  break;
               }
            case 196:
               this._JDBCLLRTableRecordColumnSize = 1000;
               if (var2) {
                  break;
               }
            case 194:
               this._JDBCLLRTableXIDColumnSize = 40;
               if (var2) {
                  break;
               }
            case 93:
               this._JDBCLogFileName = "jdbc.log";
               if (var2) {
                  break;
               }
            case 197:
               this._JDBCLoginTimeoutSeconds = 0;
               if (var2) {
                  break;
               }
            case 123:
               this._JNDITransportableObjectFactoryList = new String[0];
               if (var2) {
                  break;
               }
            case 145:
               this._JTAMigratableTarget = null;
               if (var2) {
                  break;
               }
            case 127:
               this._JavaCompiler = "javac";
               if (var2) {
                  break;
               }
            case 129:
               this._JavaCompilerPostClassPath = null;
               if (var2) {
                  break;
               }
            case 128:
               this._JavaCompilerPreClassPath = null;
               if (var2) {
                  break;
               }
            case 185:
               this._JavaStandardTrustKeyStorePassPhraseEncrypted = null;
               if (var2) {
                  break;
               }
            case 186:
               this._JavaStandardTrustKeyStorePassPhraseEncrypted = null;
               if (var2) {
                  break;
               }
            case 48:
               this._KernelDebug = null;
               if (var2) {
                  break;
               }
            case 176:
               this._KeyStores = "DemoIdentityAndDemoTrust";
               if (var2) {
                  break;
               }
            case 114:
               this._ListenAddress = null;
               if (var2) {
                  break;
               }
            case 144:
               this._ListenDelaySecs = 0;
               if (var2) {
                  break;
               }
            case 80:
               this._ListenPort = 7001;
               if (var2) {
                  break;
               }
            case 112:
               this._ListenThreadStartDelaySecs = 60;
               if (var2) {
                  break;
               }
            case 113:
               this._ListenersBindEarly = false;
               if (var2) {
                  break;
               }
            case 82:
               this._LoginTimeout = 1000;
               if (var2) {
                  break;
               }
            case 120:
               this._LoginTimeoutMillis = 5000;
               if (var2) {
                  break;
               }
            case 149:
               this._LowMemoryGCThreshold = 5;
               if (var2) {
                  break;
               }
            case 148:
               this._LowMemoryGranularityLevel = 5;
               if (var2) {
                  break;
               }
            case 147:
               this._LowMemorySampleSize = 10;
               if (var2) {
                  break;
               }
            case 146:
               this._LowMemoryTimeInterval = 3600;
               if (var2) {
                  break;
               }
            case 79:
               this._Machine = null;
               if (var2) {
                  break;
               }
            case 119:
               this._MaxBackoffBetweenFailures = 10000;
               if (var2) {
                  break;
               }
            case 206:
               this._NMSocketCreateTimeoutInMillis = 180000;
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 117:
               this._NetworkAccessPoints = new NetworkAccessPointMBean[0];
               if (var2) {
                  break;
               }
            case 191:
               this._OverloadProtection = new OverloadProtectionMBeanImpl(this, 191);
               this._postCreate((AbstractDescriptorBean)this._OverloadProtection);
               if (var2) {
                  break;
               }
            case 86:
               this._PreferredSecondaryGroup = null;
               if (var2) {
                  break;
               }
            case 187:
               this._ReliableDeliveryPolicy = null;
               if (var2) {
                  break;
               }
            case 85:
               this._ReplicationGroup = null;
               if (var2) {
                  break;
               }
            case 209:
               this._ReplicationPorts = null;
               if (var2) {
                  break;
               }
            case 161:
               this._RestartDelaySeconds = 0;
               if (var2) {
                  break;
               }
            case 156:
               this._RestartIntervalSeconds = 3600;
               if (var2) {
                  break;
               }
            case 157:
               this._RestartMax = 2;
               if (var2) {
                  break;
               }
            case 78:
               this._RootDirectory = ".";
               if (var2) {
                  break;
               }
            case 107:
               this._ServerDebug = new ServerDebugMBeanImpl(this, 107);
               this._postCreate((AbstractDescriptorBean)this._ServerDebug);
               if (var2) {
                  break;
               }
            case 198:
               this._ServerDiagnosticConfig = new WLDFServerDiagnosticMBeanImpl(this, 198);
               this._postCreate((AbstractDescriptorBean)this._ServerDiagnosticConfig);
               if (var2) {
                  break;
               }
            case 166:
               this._ServerLifeCycleTimeoutVal = 30;
               if (var2) {
                  break;
               }
            case 77:
               this._ServerNames = null;
               if (var2) {
                  break;
               }
            case 143:
               this._ServerStart = new ServerStartMBeanImpl(this, 143);
               this._postCreate((AbstractDescriptorBean)this._ServerStart);
               if (var2) {
                  break;
               }
            case 164:
               this._ServerVersion = "unknown";
               if (var2) {
                  break;
               }
            case 204:
               this._SingleSignOnServices = new SingleSignOnServicesMBeanImpl(this, 204);
               this._postCreate((AbstractDescriptorBean)this._SingleSignOnServices);
               if (var2) {
                  break;
               }
            case 150:
               this._customizer.setStagingDirectoryName((String)null);
               if (var2) {
                  break;
               }
            case 153:
               this._StagingMode = null;
               if (var2) {
                  break;
               }
            case 165:
               this._customizer.setStartupMode("RUNNING");
               if (var2) {
                  break;
               }
            case 167:
               this._StartupTimeout = 0;
               if (var2) {
                  break;
               }
            case 60:
               this._customizer.setStdoutFormat("standard");
               if (var2) {
                  break;
               }
            case 54:
               this._customizer.setStdoutSeverityLevel(32);
               if (var2) {
                  break;
               }
            case 200:
               this._SupportedProtocols = new String[0];
               if (var2) {
                  break;
               }
            case 109:
               this._SystemPasswordEncrypted = null;
               if (var2) {
                  break;
               }
            case 110:
               this._SystemPasswordEncrypted = null;
               if (var2) {
                  break;
               }
            case 12:
               this._customizer.setThreadPoolSize(15);
               if (var2) {
                  break;
               }
            case 133:
               this._TransactionLogFilePrefix = "./";
               if (var2) {
                  break;
               }
            case 134:
               this._TransactionLogFileWritePolicy = "Direct-Write";
               if (var2) {
                  break;
               }
            case 210:
               this._TransactionLogJDBCStore = new TransactionLogJDBCStoreMBeanImpl(this, 210);
               this._postCreate((AbstractDescriptorBean)this._TransactionLogJDBCStore);
               if (var2) {
                  break;
               }
            case 139:
               this._TunnelingClientPingSecs = 45;
               if (var2) {
                  break;
               }
            case 140:
               this._TunnelingClientTimeoutSecs = 40;
               if (var2) {
                  break;
               }
            case 151:
               this._customizer.setUploadDirectoryName((String)null);
               if (var2) {
                  break;
               }
            case 132:
               this._VerboseEJBDeploymentEnabled = "false";
               if (var2) {
                  break;
               }
            case 208:
               this._VirtualMachineName = null;
               if (var2) {
                  break;
               }
            case 90:
               this._WebServer = new WebServerMBeanImpl(this, 90);
               this._postCreate((AbstractDescriptorBean)this._WebServer);
               if (var2) {
                  break;
               }
            case 205:
               this._WebService = new WebServiceMBeanImpl(this, 205);
               this._postCreate((AbstractDescriptorBean)this._WebService);
               if (var2) {
                  break;
               }
            case 126:
               this._XMLEntityCache = null;
               if (var2) {
                  break;
               }
            case 125:
               this._XMLRegistry = null;
               if (var2) {
                  break;
               }
            case 121:
               this._AdministrationPortEnabled = false;
               if (var2) {
                  break;
               }
            case 88:
               this._AutoMigrationEnabled = false;
               if (var2) {
                  break;
               }
            case 104:
               this._COMEnabled = false;
               if (var2) {
                  break;
               }
            case 162:
               this._ClasspathServletDisabled = false;
               if (var2) {
                  break;
               }
            case 172:
               this._ClientCertProxyEnabled = false;
               if (var2) {
                  break;
               }
            case 111:
               this._ConsoleInputEnabled = false;
               if (var2) {
                  break;
               }
            case 163:
               this._DefaultInternalServletsDisabled = false;
               if (var2) {
                  break;
               }
            case 136:
               this._customizer.setEnabledForDomainLog(true);
               if (var2) {
                  break;
               }
            case 175:
               this._HttpTraceSupportEnabled = false;
               if (var2) {
                  break;
               }
            case 108:
               this._HttpdEnabled = true;
               if (var2) {
                  break;
               }
            case 96:
               this._IIOPEnabled = true;
               if (var2) {
                  break;
               }
            case 169:
               this._IgnoreSessionsDuringShutdown = false;
               if (var2) {
                  break;
               }
            case 94:
               this._J2EE12OnlyModeEnabled = false;
               if (var2) {
                  break;
               }
            case 95:
               this._J2EE13WarningEnabled = false;
               if (var2) {
                  break;
               }
            case 92:
               this._JDBCLoggingEnabled = false;
               if (var2) {
                  break;
               }
            case 142:
               this._JMSDefaultConnectionFactoriesEnabled = true;
               if (var2) {
                  break;
               }
            case 105:
               this._JRMPEnabled = false;
               if (var2) {
                  break;
               }
            case 81:
               this._ListenPortEnabled = true;
               if (var2) {
                  break;
               }
            case 171:
               this._MSIFileReplicationEnabled = false;
               if (var2) {
                  break;
               }
            case 170:
               this._ManagedServerIndependenceEnabled = true;
               if (var2) {
                  break;
               }
            case 188:
               this._customizer.setMessageIdPrefixEnabled(true);
               if (var2) {
                  break;
               }
            case 135:
               this._NetworkClassLoadingEnabled = false;
               if (var2) {
                  break;
               }
            case 55:
               this._customizer.setStdoutDebugEnabled(false);
               if (var2) {
                  break;
               }
            case 53:
               this._customizer.setStdoutEnabled(true);
               if (var2) {
                  break;
               }
            case 61:
               this._customizer.setStdoutLogStack(true);
               if (var2) {
                  break;
               }
            case 100:
               this._TGIOPEnabled = true;
               if (var2) {
                  break;
               }
            case 138:
               this._TunnelingEnabled = false;
               if (var2) {
                  break;
               }
            case 193:
               this._UseFusionForLLR = false;
               if (var2) {
                  break;
               }
            case 173:
               this._WeblogicPluginEnabled = false;
               if (var2) {
                  break;
               }
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
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
            case 49:
            case 50:
            case 51:
            case 52:
            case 56:
            case 57:
            case 58:
            case 59:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 68:
            case 69:
            case 70:
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
            case 76:
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
      return "Server";
   }

   public void putValue(String var1, Object var2) {
      String var9;
      if (var1.equals("81StyleDefaultStagingDirName")) {
         var9 = this._81StyleDefaultStagingDirName;
         this._81StyleDefaultStagingDirName = (String)var2;
         this._postSet(202, var9, this._81StyleDefaultStagingDirName);
      } else {
         int var10;
         if (var1.equals("AcceptBacklog")) {
            var10 = this._AcceptBacklog;
            this._AcceptBacklog = (Integer)var2;
            this._postSet(118, var10, this._AcceptBacklog);
         } else if (var1.equals("ActiveDirectoryName")) {
            var9 = this._ActiveDirectoryName;
            this._ActiveDirectoryName = (String)var2;
            this._postSet(152, var9, this._ActiveDirectoryName);
         } else if (var1.equals("AdminReconnectIntervalSeconds")) {
            var10 = this._AdminReconnectIntervalSeconds;
            this._AdminReconnectIntervalSeconds = (Integer)var2;
            this._postSet(141, var10, this._AdminReconnectIntervalSeconds);
         } else if (var1.equals("AdministrationPort")) {
            var10 = this._AdministrationPort;
            this._AdministrationPort = (Integer)var2;
            this._postSet(122, var10, this._AdministrationPort);
         } else {
            boolean var6;
            if (var1.equals("AdministrationPortEnabled")) {
               var6 = this._AdministrationPortEnabled;
               this._AdministrationPortEnabled = (Boolean)var2;
               this._postSet(121, var6, this._AdministrationPortEnabled);
            } else if (var1.equals("AutoJDBCConnectionClose")) {
               var9 = this._AutoJDBCConnectionClose;
               this._AutoJDBCConnectionClose = (String)var2;
               this._postSet(199, var9, this._AutoJDBCConnectionClose);
            } else if (var1.equals("AutoKillIfFailed")) {
               var6 = this._AutoKillIfFailed;
               this._AutoKillIfFailed = (Boolean)var2;
               this._postSet(155, var6, this._AutoKillIfFailed);
            } else if (var1.equals("AutoMigrationEnabled")) {
               var6 = this._AutoMigrationEnabled;
               this._AutoMigrationEnabled = (Boolean)var2;
               this._postSet(88, var6, this._AutoMigrationEnabled);
            } else if (var1.equals("AutoRestart")) {
               var6 = this._AutoRestart;
               this._AutoRestart = (Boolean)var2;
               this._postSet(154, var6, this._AutoRestart);
            } else if (var1.equals("COM")) {
               COMMBean var34 = this._COM;
               this._COM = (COMMBean)var2;
               this._postSet(106, var34, this._COM);
            } else if (var1.equals("COMEnabled")) {
               var6 = this._COMEnabled;
               this._COMEnabled = (Boolean)var2;
               this._postSet(104, var6, this._COMEnabled);
            } else if (var1.equals("CandidateMachines")) {
               MachineMBean[] var33 = this._CandidateMachines;
               this._CandidateMachines = (MachineMBean[])((MachineMBean[])var2);
               this._postSet(190, var33, this._CandidateMachines);
            } else if (var1.equals("ClasspathServletDisabled")) {
               var6 = this._ClasspathServletDisabled;
               this._ClasspathServletDisabled = (Boolean)var2;
               this._postSet(162, var6, this._ClasspathServletDisabled);
            } else if (var1.equals("ClientCertProxyEnabled")) {
               var6 = this._ClientCertProxyEnabled;
               this._ClientCertProxyEnabled = (Boolean)var2;
               this._postSet(172, var6, this._ClientCertProxyEnabled);
            } else if (var1.equals("Cluster")) {
               ClusterMBean var32 = this._Cluster;
               this._Cluster = (ClusterMBean)var2;
               this._postSet(83, var32, this._Cluster);
            } else if (var1.equals("ClusterRuntime")) {
               ClusterRuntimeMBean var31 = this._ClusterRuntime;
               this._ClusterRuntime = (ClusterRuntimeMBean)var2;
               this._postSet(89, var31, this._ClusterRuntime);
            } else if (var1.equals("ClusterWeight")) {
               var10 = this._ClusterWeight;
               this._ClusterWeight = (Integer)var2;
               this._postSet(84, var10, this._ClusterWeight);
            } else if (var1.equals("CoherenceClusterSystemResource")) {
               CoherenceClusterSystemResourceMBean var30 = this._CoherenceClusterSystemResource;
               this._CoherenceClusterSystemResource = (CoherenceClusterSystemResourceMBean)var2;
               this._postSet(207, var30, this._CoherenceClusterSystemResource);
            } else if (var1.equals("ConsensusProcessIdentifier")) {
               var10 = this._ConsensusProcessIdentifier;
               this._ConsensusProcessIdentifier = (Integer)var2;
               this._postSet(87, var10, this._ConsensusProcessIdentifier);
            } else if (var1.equals("ConsoleInputEnabled")) {
               var6 = this._ConsoleInputEnabled;
               this._ConsoleInputEnabled = (Boolean)var2;
               this._postSet(111, var6, this._ConsoleInputEnabled);
            } else if (var1.equals("CustomIdentityKeyStoreFileName")) {
               var9 = this._CustomIdentityKeyStoreFileName;
               this._CustomIdentityKeyStoreFileName = (String)var2;
               this._postSet(177, var9, this._CustomIdentityKeyStoreFileName);
            } else if (var1.equals("CustomIdentityKeyStorePassPhrase")) {
               var9 = this._CustomIdentityKeyStorePassPhrase;
               this._CustomIdentityKeyStorePassPhrase = (String)var2;
               this._postSet(179, var9, this._CustomIdentityKeyStorePassPhrase);
            } else {
               byte[] var12;
               if (var1.equals("CustomIdentityKeyStorePassPhraseEncrypted")) {
                  var12 = this._CustomIdentityKeyStorePassPhraseEncrypted;
                  this._CustomIdentityKeyStorePassPhraseEncrypted = (byte[])((byte[])var2);
                  this._postSet(180, var12, this._CustomIdentityKeyStorePassPhraseEncrypted);
               } else if (var1.equals("CustomIdentityKeyStoreType")) {
                  var9 = this._CustomIdentityKeyStoreType;
                  this._CustomIdentityKeyStoreType = (String)var2;
                  this._postSet(178, var9, this._CustomIdentityKeyStoreType);
               } else if (var1.equals("CustomTrustKeyStoreFileName")) {
                  var9 = this._CustomTrustKeyStoreFileName;
                  this._CustomTrustKeyStoreFileName = (String)var2;
                  this._postSet(181, var9, this._CustomTrustKeyStoreFileName);
               } else if (var1.equals("CustomTrustKeyStorePassPhrase")) {
                  var9 = this._CustomTrustKeyStorePassPhrase;
                  this._CustomTrustKeyStorePassPhrase = (String)var2;
                  this._postSet(183, var9, this._CustomTrustKeyStorePassPhrase);
               } else if (var1.equals("CustomTrustKeyStorePassPhraseEncrypted")) {
                  var12 = this._CustomTrustKeyStorePassPhraseEncrypted;
                  this._CustomTrustKeyStorePassPhraseEncrypted = (byte[])((byte[])var2);
                  this._postSet(184, var12, this._CustomTrustKeyStorePassPhraseEncrypted);
               } else if (var1.equals("CustomTrustKeyStoreType")) {
                  var9 = this._CustomTrustKeyStoreType;
                  this._CustomTrustKeyStoreType = (String)var2;
                  this._postSet(182, var9, this._CustomTrustKeyStoreType);
               } else if (var1.equals("DataSource")) {
                  DataSourceMBean var29 = this._DataSource;
                  this._DataSource = (DataSourceMBean)var2;
                  this._postSet(211, var29, this._DataSource);
               } else if (var1.equals("DefaultFileStore")) {
                  DefaultFileStoreMBean var28 = this._DefaultFileStore;
                  this._DefaultFileStore = (DefaultFileStoreMBean)var2;
                  this._postSet(189, var28, this._DefaultFileStore);
               } else if (var1.equals("DefaultIIOPPassword")) {
                  var9 = this._DefaultIIOPPassword;
                  this._DefaultIIOPPassword = (String)var2;
                  this._postSet(98, var9, this._DefaultIIOPPassword);
               } else if (var1.equals("DefaultIIOPPasswordEncrypted")) {
                  var12 = this._DefaultIIOPPasswordEncrypted;
                  this._DefaultIIOPPasswordEncrypted = (byte[])((byte[])var2);
                  this._postSet(99, var12, this._DefaultIIOPPasswordEncrypted);
               } else if (var1.equals("DefaultIIOPUser")) {
                  var9 = this._DefaultIIOPUser;
                  this._DefaultIIOPUser = (String)var2;
                  this._postSet(97, var9, this._DefaultIIOPUser);
               } else if (var1.equals("DefaultInternalServletsDisabled")) {
                  var6 = this._DefaultInternalServletsDisabled;
                  this._DefaultInternalServletsDisabled = (Boolean)var2;
                  this._postSet(163, var6, this._DefaultInternalServletsDisabled);
               } else if (var1.equals("DefaultStagingDirName")) {
                  var9 = this._DefaultStagingDirName;
                  this._DefaultStagingDirName = (String)var2;
                  this._postSet(201, var9, this._DefaultStagingDirName);
               } else if (var1.equals("DefaultTGIOPPassword")) {
                  var9 = this._DefaultTGIOPPassword;
                  this._DefaultTGIOPPassword = (String)var2;
                  this._postSet(102, var9, this._DefaultTGIOPPassword);
               } else if (var1.equals("DefaultTGIOPPasswordEncrypted")) {
                  var12 = this._DefaultTGIOPPasswordEncrypted;
                  this._DefaultTGIOPPasswordEncrypted = (byte[])((byte[])var2);
                  this._postSet(103, var12, this._DefaultTGIOPPasswordEncrypted);
               } else if (var1.equals("DefaultTGIOPUser")) {
                  var9 = this._DefaultTGIOPUser;
                  this._DefaultTGIOPUser = (String)var2;
                  this._postSet(101, var9, this._DefaultTGIOPUser);
               } else if (var1.equals("DomainLogFilter")) {
                  DomainLogFilterMBean var27 = this._DomainLogFilter;
                  this._DomainLogFilter = (DomainLogFilterMBean)var2;
                  this._postSet(137, var27, this._DomainLogFilter);
               } else if (var1.equals("EnabledForDomainLog")) {
                  var6 = this._EnabledForDomainLog;
                  this._EnabledForDomainLog = (Boolean)var2;
                  this._postSet(136, var6, this._EnabledForDomainLog);
               } else if (var1.equals("ExpectedToRun")) {
                  var6 = this._ExpectedToRun;
                  this._ExpectedToRun = (Boolean)var2;
                  this._postSet(91, var6, this._ExpectedToRun);
               } else if (var1.equals("ExternalDNSName")) {
                  var9 = this._ExternalDNSName;
                  this._ExternalDNSName = (String)var2;
                  this._postSet(115, var9, this._ExternalDNSName);
               } else if (var1.equals("ExtraEjbcOptions")) {
                  var9 = this._ExtraEjbcOptions;
                  this._ExtraEjbcOptions = (String)var2;
                  this._postSet(131, var9, this._ExtraEjbcOptions);
               } else if (var1.equals("ExtraRmicOptions")) {
                  var9 = this._ExtraRmicOptions;
                  this._ExtraRmicOptions = (String)var2;
                  this._postSet(130, var9, this._ExtraRmicOptions);
               } else if (var1.equals("FederationServices")) {
                  FederationServicesMBean var26 = this._FederationServices;
                  this._FederationServices = (FederationServicesMBean)var2;
                  this._postSet(203, var26, this._FederationServices);
               } else if (var1.equals("GracefulShutdownTimeout")) {
                  var10 = this._GracefulShutdownTimeout;
                  this._GracefulShutdownTimeout = (Integer)var2;
                  this._postSet(168, var10, this._GracefulShutdownTimeout);
               } else if (var1.equals("HealthCheckIntervalSeconds")) {
                  var10 = this._HealthCheckIntervalSeconds;
                  this._HealthCheckIntervalSeconds = (Integer)var2;
                  this._postSet(158, var10, this._HealthCheckIntervalSeconds);
               } else if (var1.equals("HealthCheckStartDelaySeconds")) {
                  var10 = this._HealthCheckStartDelaySeconds;
                  this._HealthCheckStartDelaySeconds = (Integer)var2;
                  this._postSet(160, var10, this._HealthCheckStartDelaySeconds);
               } else if (var1.equals("HealthCheckTimeoutSeconds")) {
                  var10 = this._HealthCheckTimeoutSeconds;
                  this._HealthCheckTimeoutSeconds = (Integer)var2;
                  this._postSet(159, var10, this._HealthCheckTimeoutSeconds);
               } else if (var1.equals("HostsMigratableServices")) {
                  var6 = this._HostsMigratableServices;
                  this._HostsMigratableServices = (Boolean)var2;
                  this._postSet(174, var6, this._HostsMigratableServices);
               } else if (var1.equals("HttpTraceSupportEnabled")) {
                  var6 = this._HttpTraceSupportEnabled;
                  this._HttpTraceSupportEnabled = (Boolean)var2;
                  this._postSet(175, var6, this._HttpTraceSupportEnabled);
               } else if (var1.equals("HttpdEnabled")) {
                  var6 = this._HttpdEnabled;
                  this._HttpdEnabled = (Boolean)var2;
                  this._postSet(108, var6, this._HttpdEnabled);
               } else if (var1.equals("IIOPConnectionPools")) {
                  Map var25 = this._IIOPConnectionPools;
                  this._IIOPConnectionPools = (Map)var2;
                  this._postSet(124, var25, this._IIOPConnectionPools);
               } else if (var1.equals("IIOPEnabled")) {
                  var6 = this._IIOPEnabled;
                  this._IIOPEnabled = (Boolean)var2;
                  this._postSet(96, var6, this._IIOPEnabled);
               } else if (var1.equals("IgnoreSessionsDuringShutdown")) {
                  var6 = this._IgnoreSessionsDuringShutdown;
                  this._IgnoreSessionsDuringShutdown = (Boolean)var2;
                  this._postSet(169, var6, this._IgnoreSessionsDuringShutdown);
               } else if (var1.equals("InterfaceAddress")) {
                  var9 = this._InterfaceAddress;
                  this._InterfaceAddress = (String)var2;
                  this._postSet(116, var9, this._InterfaceAddress);
               } else if (var1.equals("J2EE12OnlyModeEnabled")) {
                  var6 = this._J2EE12OnlyModeEnabled;
                  this._J2EE12OnlyModeEnabled = (Boolean)var2;
                  this._postSet(94, var6, this._J2EE12OnlyModeEnabled);
               } else if (var1.equals("J2EE13WarningEnabled")) {
                  var6 = this._J2EE13WarningEnabled;
                  this._J2EE13WarningEnabled = (Boolean)var2;
                  this._postSet(95, var6, this._J2EE13WarningEnabled);
               } else if (var1.equals("JDBCLLRTableName")) {
                  var9 = this._JDBCLLRTableName;
                  this._JDBCLLRTableName = (String)var2;
                  this._postSet(192, var9, this._JDBCLLRTableName);
               } else if (var1.equals("JDBCLLRTablePoolColumnSize")) {
                  var10 = this._JDBCLLRTablePoolColumnSize;
                  this._JDBCLLRTablePoolColumnSize = (Integer)var2;
                  this._postSet(195, var10, this._JDBCLLRTablePoolColumnSize);
               } else if (var1.equals("JDBCLLRTableRecordColumnSize")) {
                  var10 = this._JDBCLLRTableRecordColumnSize;
                  this._JDBCLLRTableRecordColumnSize = (Integer)var2;
                  this._postSet(196, var10, this._JDBCLLRTableRecordColumnSize);
               } else if (var1.equals("JDBCLLRTableXIDColumnSize")) {
                  var10 = this._JDBCLLRTableXIDColumnSize;
                  this._JDBCLLRTableXIDColumnSize = (Integer)var2;
                  this._postSet(194, var10, this._JDBCLLRTableXIDColumnSize);
               } else if (var1.equals("JDBCLogFileName")) {
                  var9 = this._JDBCLogFileName;
                  this._JDBCLogFileName = (String)var2;
                  this._postSet(93, var9, this._JDBCLogFileName);
               } else if (var1.equals("JDBCLoggingEnabled")) {
                  var6 = this._JDBCLoggingEnabled;
                  this._JDBCLoggingEnabled = (Boolean)var2;
                  this._postSet(92, var6, this._JDBCLoggingEnabled);
               } else if (var1.equals("JDBCLoginTimeoutSeconds")) {
                  var10 = this._JDBCLoginTimeoutSeconds;
                  this._JDBCLoginTimeoutSeconds = (Integer)var2;
                  this._postSet(197, var10, this._JDBCLoginTimeoutSeconds);
               } else if (var1.equals("JMSDefaultConnectionFactoriesEnabled")) {
                  var6 = this._JMSDefaultConnectionFactoriesEnabled;
                  this._JMSDefaultConnectionFactoriesEnabled = (Boolean)var2;
                  this._postSet(142, var6, this._JMSDefaultConnectionFactoriesEnabled);
               } else {
                  String[] var13;
                  if (var1.equals("JNDITransportableObjectFactoryList")) {
                     var13 = this._JNDITransportableObjectFactoryList;
                     this._JNDITransportableObjectFactoryList = (String[])((String[])var2);
                     this._postSet(123, var13, this._JNDITransportableObjectFactoryList);
                  } else if (var1.equals("JRMPEnabled")) {
                     var6 = this._JRMPEnabled;
                     this._JRMPEnabled = (Boolean)var2;
                     this._postSet(105, var6, this._JRMPEnabled);
                  } else if (var1.equals("JTAMigratableTarget")) {
                     JTAMigratableTargetMBean var24 = this._JTAMigratableTarget;
                     this._JTAMigratableTarget = (JTAMigratableTargetMBean)var2;
                     this._postSet(145, var24, this._JTAMigratableTarget);
                  } else if (var1.equals("JavaCompiler")) {
                     var9 = this._JavaCompiler;
                     this._JavaCompiler = (String)var2;
                     this._postSet(127, var9, this._JavaCompiler);
                  } else if (var1.equals("JavaCompilerPostClassPath")) {
                     var9 = this._JavaCompilerPostClassPath;
                     this._JavaCompilerPostClassPath = (String)var2;
                     this._postSet(129, var9, this._JavaCompilerPostClassPath);
                  } else if (var1.equals("JavaCompilerPreClassPath")) {
                     var9 = this._JavaCompilerPreClassPath;
                     this._JavaCompilerPreClassPath = (String)var2;
                     this._postSet(128, var9, this._JavaCompilerPreClassPath);
                  } else if (var1.equals("JavaStandardTrustKeyStorePassPhrase")) {
                     var9 = this._JavaStandardTrustKeyStorePassPhrase;
                     this._JavaStandardTrustKeyStorePassPhrase = (String)var2;
                     this._postSet(185, var9, this._JavaStandardTrustKeyStorePassPhrase);
                  } else if (var1.equals("JavaStandardTrustKeyStorePassPhraseEncrypted")) {
                     var12 = this._JavaStandardTrustKeyStorePassPhraseEncrypted;
                     this._JavaStandardTrustKeyStorePassPhraseEncrypted = (byte[])((byte[])var2);
                     this._postSet(186, var12, this._JavaStandardTrustKeyStorePassPhraseEncrypted);
                  } else if (var1.equals("KernelDebug")) {
                     KernelDebugMBean var23 = this._KernelDebug;
                     this._KernelDebug = (KernelDebugMBean)var2;
                     this._postSet(48, var23, this._KernelDebug);
                  } else if (var1.equals("KeyStores")) {
                     var9 = this._KeyStores;
                     this._KeyStores = (String)var2;
                     this._postSet(176, var9, this._KeyStores);
                  } else if (var1.equals("ListenAddress")) {
                     var9 = this._ListenAddress;
                     this._ListenAddress = (String)var2;
                     this._postSet(114, var9, this._ListenAddress);
                  } else if (var1.equals("ListenDelaySecs")) {
                     var10 = this._ListenDelaySecs;
                     this._ListenDelaySecs = (Integer)var2;
                     this._postSet(144, var10, this._ListenDelaySecs);
                  } else if (var1.equals("ListenPort")) {
                     var10 = this._ListenPort;
                     this._ListenPort = (Integer)var2;
                     this._postSet(80, var10, this._ListenPort);
                  } else if (var1.equals("ListenPortEnabled")) {
                     var6 = this._ListenPortEnabled;
                     this._ListenPortEnabled = (Boolean)var2;
                     this._postSet(81, var6, this._ListenPortEnabled);
                  } else if (var1.equals("ListenThreadStartDelaySecs")) {
                     var10 = this._ListenThreadStartDelaySecs;
                     this._ListenThreadStartDelaySecs = (Integer)var2;
                     this._postSet(112, var10, this._ListenThreadStartDelaySecs);
                  } else if (var1.equals("ListenersBindEarly")) {
                     var6 = this._ListenersBindEarly;
                     this._ListenersBindEarly = (Boolean)var2;
                     this._postSet(113, var6, this._ListenersBindEarly);
                  } else if (var1.equals("LoginTimeout")) {
                     var10 = this._LoginTimeout;
                     this._LoginTimeout = (Integer)var2;
                     this._postSet(82, var10, this._LoginTimeout);
                  } else if (var1.equals("LoginTimeoutMillis")) {
                     var10 = this._LoginTimeoutMillis;
                     this._LoginTimeoutMillis = (Integer)var2;
                     this._postSet(120, var10, this._LoginTimeoutMillis);
                  } else if (var1.equals("LowMemoryGCThreshold")) {
                     var10 = this._LowMemoryGCThreshold;
                     this._LowMemoryGCThreshold = (Integer)var2;
                     this._postSet(149, var10, this._LowMemoryGCThreshold);
                  } else if (var1.equals("LowMemoryGranularityLevel")) {
                     var10 = this._LowMemoryGranularityLevel;
                     this._LowMemoryGranularityLevel = (Integer)var2;
                     this._postSet(148, var10, this._LowMemoryGranularityLevel);
                  } else if (var1.equals("LowMemorySampleSize")) {
                     var10 = this._LowMemorySampleSize;
                     this._LowMemorySampleSize = (Integer)var2;
                     this._postSet(147, var10, this._LowMemorySampleSize);
                  } else if (var1.equals("LowMemoryTimeInterval")) {
                     var10 = this._LowMemoryTimeInterval;
                     this._LowMemoryTimeInterval = (Integer)var2;
                     this._postSet(146, var10, this._LowMemoryTimeInterval);
                  } else if (var1.equals("MSIFileReplicationEnabled")) {
                     var6 = this._MSIFileReplicationEnabled;
                     this._MSIFileReplicationEnabled = (Boolean)var2;
                     this._postSet(171, var6, this._MSIFileReplicationEnabled);
                  } else if (var1.equals("Machine")) {
                     MachineMBean var22 = this._Machine;
                     this._Machine = (MachineMBean)var2;
                     this._postSet(79, var22, this._Machine);
                  } else if (var1.equals("ManagedServerIndependenceEnabled")) {
                     var6 = this._ManagedServerIndependenceEnabled;
                     this._ManagedServerIndependenceEnabled = (Boolean)var2;
                     this._postSet(170, var6, this._ManagedServerIndependenceEnabled);
                  } else if (var1.equals("MaxBackoffBetweenFailures")) {
                     var10 = this._MaxBackoffBetweenFailures;
                     this._MaxBackoffBetweenFailures = (Integer)var2;
                     this._postSet(119, var10, this._MaxBackoffBetweenFailures);
                  } else if (var1.equals("MessageIdPrefixEnabled")) {
                     var6 = this._MessageIdPrefixEnabled;
                     this._MessageIdPrefixEnabled = (Boolean)var2;
                     this._postSet(188, var6, this._MessageIdPrefixEnabled);
                  } else if (var1.equals("NMSocketCreateTimeoutInMillis")) {
                     var10 = this._NMSocketCreateTimeoutInMillis;
                     this._NMSocketCreateTimeoutInMillis = (Integer)var2;
                     this._postSet(206, var10, this._NMSocketCreateTimeoutInMillis);
                  } else if (var1.equals("Name")) {
                     var9 = this._Name;
                     this._Name = (String)var2;
                     this._postSet(2, var9, this._Name);
                  } else if (var1.equals("NetworkAccessPoints")) {
                     NetworkAccessPointMBean[] var21 = this._NetworkAccessPoints;
                     this._NetworkAccessPoints = (NetworkAccessPointMBean[])((NetworkAccessPointMBean[])var2);
                     this._postSet(117, var21, this._NetworkAccessPoints);
                  } else if (var1.equals("NetworkClassLoadingEnabled")) {
                     var6 = this._NetworkClassLoadingEnabled;
                     this._NetworkClassLoadingEnabled = (Boolean)var2;
                     this._postSet(135, var6, this._NetworkClassLoadingEnabled);
                  } else if (var1.equals("OverloadProtection")) {
                     OverloadProtectionMBean var20 = this._OverloadProtection;
                     this._OverloadProtection = (OverloadProtectionMBean)var2;
                     this._postSet(191, var20, this._OverloadProtection);
                  } else if (var1.equals("PreferredSecondaryGroup")) {
                     var9 = this._PreferredSecondaryGroup;
                     this._PreferredSecondaryGroup = (String)var2;
                     this._postSet(86, var9, this._PreferredSecondaryGroup);
                  } else if (var1.equals("ReliableDeliveryPolicy")) {
                     WSReliableDeliveryPolicyMBean var19 = this._ReliableDeliveryPolicy;
                     this._ReliableDeliveryPolicy = (WSReliableDeliveryPolicyMBean)var2;
                     this._postSet(187, var19, this._ReliableDeliveryPolicy);
                  } else if (var1.equals("ReplicationGroup")) {
                     var9 = this._ReplicationGroup;
                     this._ReplicationGroup = (String)var2;
                     this._postSet(85, var9, this._ReplicationGroup);
                  } else if (var1.equals("ReplicationPorts")) {
                     var9 = this._ReplicationPorts;
                     this._ReplicationPorts = (String)var2;
                     this._postSet(209, var9, this._ReplicationPorts);
                  } else if (var1.equals("RestartDelaySeconds")) {
                     var10 = this._RestartDelaySeconds;
                     this._RestartDelaySeconds = (Integer)var2;
                     this._postSet(161, var10, this._RestartDelaySeconds);
                  } else if (var1.equals("RestartIntervalSeconds")) {
                     var10 = this._RestartIntervalSeconds;
                     this._RestartIntervalSeconds = (Integer)var2;
                     this._postSet(156, var10, this._RestartIntervalSeconds);
                  } else if (var1.equals("RestartMax")) {
                     var10 = this._RestartMax;
                     this._RestartMax = (Integer)var2;
                     this._postSet(157, var10, this._RestartMax);
                  } else if (var1.equals("RootDirectory")) {
                     var9 = this._RootDirectory;
                     this._RootDirectory = (String)var2;
                     this._postSet(78, var9, this._RootDirectory);
                  } else if (var1.equals("ServerDebug")) {
                     ServerDebugMBean var18 = this._ServerDebug;
                     this._ServerDebug = (ServerDebugMBean)var2;
                     this._postSet(107, var18, this._ServerDebug);
                  } else if (var1.equals("ServerDiagnosticConfig")) {
                     WLDFServerDiagnosticMBean var17 = this._ServerDiagnosticConfig;
                     this._ServerDiagnosticConfig = (WLDFServerDiagnosticMBean)var2;
                     this._postSet(198, var17, this._ServerDiagnosticConfig);
                  } else if (var1.equals("ServerLifeCycleTimeoutVal")) {
                     var10 = this._ServerLifeCycleTimeoutVal;
                     this._ServerLifeCycleTimeoutVal = (Integer)var2;
                     this._postSet(166, var10, this._ServerLifeCycleTimeoutVal);
                  } else if (var1.equals("ServerNames")) {
                     Set var16 = this._ServerNames;
                     this._ServerNames = (Set)var2;
                     this._postSet(77, var16, this._ServerNames);
                  } else if (var1.equals("ServerStart")) {
                     ServerStartMBean var15 = this._ServerStart;
                     this._ServerStart = (ServerStartMBean)var2;
                     this._postSet(143, var15, this._ServerStart);
                  } else if (var1.equals("ServerVersion")) {
                     var9 = this._ServerVersion;
                     this._ServerVersion = (String)var2;
                     this._postSet(164, var9, this._ServerVersion);
                  } else if (var1.equals("SingleSignOnServices")) {
                     SingleSignOnServicesMBean var14 = this._SingleSignOnServices;
                     this._SingleSignOnServices = (SingleSignOnServicesMBean)var2;
                     this._postSet(204, var14, this._SingleSignOnServices);
                  } else if (var1.equals("StagingDirectoryName")) {
                     var9 = this._StagingDirectoryName;
                     this._StagingDirectoryName = (String)var2;
                     this._postSet(150, var9, this._StagingDirectoryName);
                  } else if (var1.equals("StagingMode")) {
                     var9 = this._StagingMode;
                     this._StagingMode = (String)var2;
                     this._postSet(153, var9, this._StagingMode);
                  } else if (var1.equals("StartupMode")) {
                     var9 = this._StartupMode;
                     this._StartupMode = (String)var2;
                     this._postSet(165, var9, this._StartupMode);
                  } else if (var1.equals("StartupTimeout")) {
                     var10 = this._StartupTimeout;
                     this._StartupTimeout = (Integer)var2;
                     this._postSet(167, var10, this._StartupTimeout);
                  } else if (var1.equals("StdoutDebugEnabled")) {
                     var6 = this._StdoutDebugEnabled;
                     this._StdoutDebugEnabled = (Boolean)var2;
                     this._postSet(55, var6, this._StdoutDebugEnabled);
                  } else if (var1.equals("StdoutEnabled")) {
                     var6 = this._StdoutEnabled;
                     this._StdoutEnabled = (Boolean)var2;
                     this._postSet(53, var6, this._StdoutEnabled);
                  } else if (var1.equals("StdoutFormat")) {
                     var9 = this._StdoutFormat;
                     this._StdoutFormat = (String)var2;
                     this._postSet(60, var9, this._StdoutFormat);
                  } else if (var1.equals("StdoutLogStack")) {
                     var6 = this._StdoutLogStack;
                     this._StdoutLogStack = (Boolean)var2;
                     this._postSet(61, var6, this._StdoutLogStack);
                  } else if (var1.equals("StdoutSeverityLevel")) {
                     var10 = this._StdoutSeverityLevel;
                     this._StdoutSeverityLevel = (Integer)var2;
                     this._postSet(54, var10, this._StdoutSeverityLevel);
                  } else if (var1.equals("SupportedProtocols")) {
                     var13 = this._SupportedProtocols;
                     this._SupportedProtocols = (String[])((String[])var2);
                     this._postSet(200, var13, this._SupportedProtocols);
                  } else if (var1.equals("SystemPassword")) {
                     var9 = this._SystemPassword;
                     this._SystemPassword = (String)var2;
                     this._postSet(109, var9, this._SystemPassword);
                  } else if (var1.equals("SystemPasswordEncrypted")) {
                     var12 = this._SystemPasswordEncrypted;
                     this._SystemPasswordEncrypted = (byte[])((byte[])var2);
                     this._postSet(110, var12, this._SystemPasswordEncrypted);
                  } else if (var1.equals("TGIOPEnabled")) {
                     var6 = this._TGIOPEnabled;
                     this._TGIOPEnabled = (Boolean)var2;
                     this._postSet(100, var6, this._TGIOPEnabled);
                  } else if (var1.equals("ThreadPoolSize")) {
                     var10 = this._ThreadPoolSize;
                     this._ThreadPoolSize = (Integer)var2;
                     this._postSet(12, var10, this._ThreadPoolSize);
                  } else if (var1.equals("TransactionLogFilePrefix")) {
                     var9 = this._TransactionLogFilePrefix;
                     this._TransactionLogFilePrefix = (String)var2;
                     this._postSet(133, var9, this._TransactionLogFilePrefix);
                  } else if (var1.equals("TransactionLogFileWritePolicy")) {
                     var9 = this._TransactionLogFileWritePolicy;
                     this._TransactionLogFileWritePolicy = (String)var2;
                     this._postSet(134, var9, this._TransactionLogFileWritePolicy);
                  } else if (var1.equals("TransactionLogJDBCStore")) {
                     TransactionLogJDBCStoreMBean var11 = this._TransactionLogJDBCStore;
                     this._TransactionLogJDBCStore = (TransactionLogJDBCStoreMBean)var2;
                     this._postSet(210, var11, this._TransactionLogJDBCStore);
                  } else if (var1.equals("TunnelingClientPingSecs")) {
                     var10 = this._TunnelingClientPingSecs;
                     this._TunnelingClientPingSecs = (Integer)var2;
                     this._postSet(139, var10, this._TunnelingClientPingSecs);
                  } else if (var1.equals("TunnelingClientTimeoutSecs")) {
                     var10 = this._TunnelingClientTimeoutSecs;
                     this._TunnelingClientTimeoutSecs = (Integer)var2;
                     this._postSet(140, var10, this._TunnelingClientTimeoutSecs);
                  } else if (var1.equals("TunnelingEnabled")) {
                     var6 = this._TunnelingEnabled;
                     this._TunnelingEnabled = (Boolean)var2;
                     this._postSet(138, var6, this._TunnelingEnabled);
                  } else if (var1.equals("UploadDirectoryName")) {
                     var9 = this._UploadDirectoryName;
                     this._UploadDirectoryName = (String)var2;
                     this._postSet(151, var9, this._UploadDirectoryName);
                  } else if (var1.equals("UseFusionForLLR")) {
                     var6 = this._UseFusionForLLR;
                     this._UseFusionForLLR = (Boolean)var2;
                     this._postSet(193, var6, this._UseFusionForLLR);
                  } else if (var1.equals("VerboseEJBDeploymentEnabled")) {
                     var9 = this._VerboseEJBDeploymentEnabled;
                     this._VerboseEJBDeploymentEnabled = (String)var2;
                     this._postSet(132, var9, this._VerboseEJBDeploymentEnabled);
                  } else if (var1.equals("VirtualMachineName")) {
                     var9 = this._VirtualMachineName;
                     this._VirtualMachineName = (String)var2;
                     this._postSet(208, var9, this._VirtualMachineName);
                  } else if (var1.equals("WebServer")) {
                     WebServerMBean var8 = this._WebServer;
                     this._WebServer = (WebServerMBean)var2;
                     this._postSet(90, var8, this._WebServer);
                  } else if (var1.equals("WebService")) {
                     WebServiceMBean var7 = this._WebService;
                     this._WebService = (WebServiceMBean)var2;
                     this._postSet(205, var7, this._WebService);
                  } else if (var1.equals("WeblogicPluginEnabled")) {
                     var6 = this._WeblogicPluginEnabled;
                     this._WeblogicPluginEnabled = (Boolean)var2;
                     this._postSet(173, var6, this._WeblogicPluginEnabled);
                  } else if (var1.equals("XMLEntityCache")) {
                     XMLEntityCacheMBean var5 = this._XMLEntityCache;
                     this._XMLEntityCache = (XMLEntityCacheMBean)var2;
                     this._postSet(126, var5, this._XMLEntityCache);
                  } else if (var1.equals("XMLRegistry")) {
                     XMLRegistryMBean var4 = this._XMLRegistry;
                     this._XMLRegistry = (XMLRegistryMBean)var2;
                     this._postSet(125, var4, this._XMLRegistry);
                  } else if (var1.equals("customizer")) {
                     Server var3 = this._customizer;
                     this._customizer = (Server)var2;
                  } else {
                     super.putValue(var1, var2);
                  }
               }
            }
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("81StyleDefaultStagingDirName")) {
         return this._81StyleDefaultStagingDirName;
      } else if (var1.equals("AcceptBacklog")) {
         return new Integer(this._AcceptBacklog);
      } else if (var1.equals("ActiveDirectoryName")) {
         return this._ActiveDirectoryName;
      } else if (var1.equals("AdminReconnectIntervalSeconds")) {
         return new Integer(this._AdminReconnectIntervalSeconds);
      } else if (var1.equals("AdministrationPort")) {
         return new Integer(this._AdministrationPort);
      } else if (var1.equals("AdministrationPortEnabled")) {
         return new Boolean(this._AdministrationPortEnabled);
      } else if (var1.equals("AutoJDBCConnectionClose")) {
         return this._AutoJDBCConnectionClose;
      } else if (var1.equals("AutoKillIfFailed")) {
         return new Boolean(this._AutoKillIfFailed);
      } else if (var1.equals("AutoMigrationEnabled")) {
         return new Boolean(this._AutoMigrationEnabled);
      } else if (var1.equals("AutoRestart")) {
         return new Boolean(this._AutoRestart);
      } else if (var1.equals("COM")) {
         return this._COM;
      } else if (var1.equals("COMEnabled")) {
         return new Boolean(this._COMEnabled);
      } else if (var1.equals("CandidateMachines")) {
         return this._CandidateMachines;
      } else if (var1.equals("ClasspathServletDisabled")) {
         return new Boolean(this._ClasspathServletDisabled);
      } else if (var1.equals("ClientCertProxyEnabled")) {
         return new Boolean(this._ClientCertProxyEnabled);
      } else if (var1.equals("Cluster")) {
         return this._Cluster;
      } else if (var1.equals("ClusterRuntime")) {
         return this._ClusterRuntime;
      } else if (var1.equals("ClusterWeight")) {
         return new Integer(this._ClusterWeight);
      } else if (var1.equals("CoherenceClusterSystemResource")) {
         return this._CoherenceClusterSystemResource;
      } else if (var1.equals("ConsensusProcessIdentifier")) {
         return new Integer(this._ConsensusProcessIdentifier);
      } else if (var1.equals("ConsoleInputEnabled")) {
         return new Boolean(this._ConsoleInputEnabled);
      } else if (var1.equals("CustomIdentityKeyStoreFileName")) {
         return this._CustomIdentityKeyStoreFileName;
      } else if (var1.equals("CustomIdentityKeyStorePassPhrase")) {
         return this._CustomIdentityKeyStorePassPhrase;
      } else if (var1.equals("CustomIdentityKeyStorePassPhraseEncrypted")) {
         return this._CustomIdentityKeyStorePassPhraseEncrypted;
      } else if (var1.equals("CustomIdentityKeyStoreType")) {
         return this._CustomIdentityKeyStoreType;
      } else if (var1.equals("CustomTrustKeyStoreFileName")) {
         return this._CustomTrustKeyStoreFileName;
      } else if (var1.equals("CustomTrustKeyStorePassPhrase")) {
         return this._CustomTrustKeyStorePassPhrase;
      } else if (var1.equals("CustomTrustKeyStorePassPhraseEncrypted")) {
         return this._CustomTrustKeyStorePassPhraseEncrypted;
      } else if (var1.equals("CustomTrustKeyStoreType")) {
         return this._CustomTrustKeyStoreType;
      } else if (var1.equals("DataSource")) {
         return this._DataSource;
      } else if (var1.equals("DefaultFileStore")) {
         return this._DefaultFileStore;
      } else if (var1.equals("DefaultIIOPPassword")) {
         return this._DefaultIIOPPassword;
      } else if (var1.equals("DefaultIIOPPasswordEncrypted")) {
         return this._DefaultIIOPPasswordEncrypted;
      } else if (var1.equals("DefaultIIOPUser")) {
         return this._DefaultIIOPUser;
      } else if (var1.equals("DefaultInternalServletsDisabled")) {
         return new Boolean(this._DefaultInternalServletsDisabled);
      } else if (var1.equals("DefaultStagingDirName")) {
         return this._DefaultStagingDirName;
      } else if (var1.equals("DefaultTGIOPPassword")) {
         return this._DefaultTGIOPPassword;
      } else if (var1.equals("DefaultTGIOPPasswordEncrypted")) {
         return this._DefaultTGIOPPasswordEncrypted;
      } else if (var1.equals("DefaultTGIOPUser")) {
         return this._DefaultTGIOPUser;
      } else if (var1.equals("DomainLogFilter")) {
         return this._DomainLogFilter;
      } else if (var1.equals("EnabledForDomainLog")) {
         return new Boolean(this._EnabledForDomainLog);
      } else if (var1.equals("ExpectedToRun")) {
         return new Boolean(this._ExpectedToRun);
      } else if (var1.equals("ExternalDNSName")) {
         return this._ExternalDNSName;
      } else if (var1.equals("ExtraEjbcOptions")) {
         return this._ExtraEjbcOptions;
      } else if (var1.equals("ExtraRmicOptions")) {
         return this._ExtraRmicOptions;
      } else if (var1.equals("FederationServices")) {
         return this._FederationServices;
      } else if (var1.equals("GracefulShutdownTimeout")) {
         return new Integer(this._GracefulShutdownTimeout);
      } else if (var1.equals("HealthCheckIntervalSeconds")) {
         return new Integer(this._HealthCheckIntervalSeconds);
      } else if (var1.equals("HealthCheckStartDelaySeconds")) {
         return new Integer(this._HealthCheckStartDelaySeconds);
      } else if (var1.equals("HealthCheckTimeoutSeconds")) {
         return new Integer(this._HealthCheckTimeoutSeconds);
      } else if (var1.equals("HostsMigratableServices")) {
         return new Boolean(this._HostsMigratableServices);
      } else if (var1.equals("HttpTraceSupportEnabled")) {
         return new Boolean(this._HttpTraceSupportEnabled);
      } else if (var1.equals("HttpdEnabled")) {
         return new Boolean(this._HttpdEnabled);
      } else if (var1.equals("IIOPConnectionPools")) {
         return this._IIOPConnectionPools;
      } else if (var1.equals("IIOPEnabled")) {
         return new Boolean(this._IIOPEnabled);
      } else if (var1.equals("IgnoreSessionsDuringShutdown")) {
         return new Boolean(this._IgnoreSessionsDuringShutdown);
      } else if (var1.equals("InterfaceAddress")) {
         return this._InterfaceAddress;
      } else if (var1.equals("J2EE12OnlyModeEnabled")) {
         return new Boolean(this._J2EE12OnlyModeEnabled);
      } else if (var1.equals("J2EE13WarningEnabled")) {
         return new Boolean(this._J2EE13WarningEnabled);
      } else if (var1.equals("JDBCLLRTableName")) {
         return this._JDBCLLRTableName;
      } else if (var1.equals("JDBCLLRTablePoolColumnSize")) {
         return new Integer(this._JDBCLLRTablePoolColumnSize);
      } else if (var1.equals("JDBCLLRTableRecordColumnSize")) {
         return new Integer(this._JDBCLLRTableRecordColumnSize);
      } else if (var1.equals("JDBCLLRTableXIDColumnSize")) {
         return new Integer(this._JDBCLLRTableXIDColumnSize);
      } else if (var1.equals("JDBCLogFileName")) {
         return this._JDBCLogFileName;
      } else if (var1.equals("JDBCLoggingEnabled")) {
         return new Boolean(this._JDBCLoggingEnabled);
      } else if (var1.equals("JDBCLoginTimeoutSeconds")) {
         return new Integer(this._JDBCLoginTimeoutSeconds);
      } else if (var1.equals("JMSDefaultConnectionFactoriesEnabled")) {
         return new Boolean(this._JMSDefaultConnectionFactoriesEnabled);
      } else if (var1.equals("JNDITransportableObjectFactoryList")) {
         return this._JNDITransportableObjectFactoryList;
      } else if (var1.equals("JRMPEnabled")) {
         return new Boolean(this._JRMPEnabled);
      } else if (var1.equals("JTAMigratableTarget")) {
         return this._JTAMigratableTarget;
      } else if (var1.equals("JavaCompiler")) {
         return this._JavaCompiler;
      } else if (var1.equals("JavaCompilerPostClassPath")) {
         return this._JavaCompilerPostClassPath;
      } else if (var1.equals("JavaCompilerPreClassPath")) {
         return this._JavaCompilerPreClassPath;
      } else if (var1.equals("JavaStandardTrustKeyStorePassPhrase")) {
         return this._JavaStandardTrustKeyStorePassPhrase;
      } else if (var1.equals("JavaStandardTrustKeyStorePassPhraseEncrypted")) {
         return this._JavaStandardTrustKeyStorePassPhraseEncrypted;
      } else if (var1.equals("KernelDebug")) {
         return this._KernelDebug;
      } else if (var1.equals("KeyStores")) {
         return this._KeyStores;
      } else if (var1.equals("ListenAddress")) {
         return this._ListenAddress;
      } else if (var1.equals("ListenDelaySecs")) {
         return new Integer(this._ListenDelaySecs);
      } else if (var1.equals("ListenPort")) {
         return new Integer(this._ListenPort);
      } else if (var1.equals("ListenPortEnabled")) {
         return new Boolean(this._ListenPortEnabled);
      } else if (var1.equals("ListenThreadStartDelaySecs")) {
         return new Integer(this._ListenThreadStartDelaySecs);
      } else if (var1.equals("ListenersBindEarly")) {
         return new Boolean(this._ListenersBindEarly);
      } else if (var1.equals("LoginTimeout")) {
         return new Integer(this._LoginTimeout);
      } else if (var1.equals("LoginTimeoutMillis")) {
         return new Integer(this._LoginTimeoutMillis);
      } else if (var1.equals("LowMemoryGCThreshold")) {
         return new Integer(this._LowMemoryGCThreshold);
      } else if (var1.equals("LowMemoryGranularityLevel")) {
         return new Integer(this._LowMemoryGranularityLevel);
      } else if (var1.equals("LowMemorySampleSize")) {
         return new Integer(this._LowMemorySampleSize);
      } else if (var1.equals("LowMemoryTimeInterval")) {
         return new Integer(this._LowMemoryTimeInterval);
      } else if (var1.equals("MSIFileReplicationEnabled")) {
         return new Boolean(this._MSIFileReplicationEnabled);
      } else if (var1.equals("Machine")) {
         return this._Machine;
      } else if (var1.equals("ManagedServerIndependenceEnabled")) {
         return new Boolean(this._ManagedServerIndependenceEnabled);
      } else if (var1.equals("MaxBackoffBetweenFailures")) {
         return new Integer(this._MaxBackoffBetweenFailures);
      } else if (var1.equals("MessageIdPrefixEnabled")) {
         return new Boolean(this._MessageIdPrefixEnabled);
      } else if (var1.equals("NMSocketCreateTimeoutInMillis")) {
         return new Integer(this._NMSocketCreateTimeoutInMillis);
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("NetworkAccessPoints")) {
         return this._NetworkAccessPoints;
      } else if (var1.equals("NetworkClassLoadingEnabled")) {
         return new Boolean(this._NetworkClassLoadingEnabled);
      } else if (var1.equals("OverloadProtection")) {
         return this._OverloadProtection;
      } else if (var1.equals("PreferredSecondaryGroup")) {
         return this._PreferredSecondaryGroup;
      } else if (var1.equals("ReliableDeliveryPolicy")) {
         return this._ReliableDeliveryPolicy;
      } else if (var1.equals("ReplicationGroup")) {
         return this._ReplicationGroup;
      } else if (var1.equals("ReplicationPorts")) {
         return this._ReplicationPorts;
      } else if (var1.equals("RestartDelaySeconds")) {
         return new Integer(this._RestartDelaySeconds);
      } else if (var1.equals("RestartIntervalSeconds")) {
         return new Integer(this._RestartIntervalSeconds);
      } else if (var1.equals("RestartMax")) {
         return new Integer(this._RestartMax);
      } else if (var1.equals("RootDirectory")) {
         return this._RootDirectory;
      } else if (var1.equals("ServerDebug")) {
         return this._ServerDebug;
      } else if (var1.equals("ServerDiagnosticConfig")) {
         return this._ServerDiagnosticConfig;
      } else if (var1.equals("ServerLifeCycleTimeoutVal")) {
         return new Integer(this._ServerLifeCycleTimeoutVal);
      } else if (var1.equals("ServerNames")) {
         return this._ServerNames;
      } else if (var1.equals("ServerStart")) {
         return this._ServerStart;
      } else if (var1.equals("ServerVersion")) {
         return this._ServerVersion;
      } else if (var1.equals("SingleSignOnServices")) {
         return this._SingleSignOnServices;
      } else if (var1.equals("StagingDirectoryName")) {
         return this._StagingDirectoryName;
      } else if (var1.equals("StagingMode")) {
         return this._StagingMode;
      } else if (var1.equals("StartupMode")) {
         return this._StartupMode;
      } else if (var1.equals("StartupTimeout")) {
         return new Integer(this._StartupTimeout);
      } else if (var1.equals("StdoutDebugEnabled")) {
         return new Boolean(this._StdoutDebugEnabled);
      } else if (var1.equals("StdoutEnabled")) {
         return new Boolean(this._StdoutEnabled);
      } else if (var1.equals("StdoutFormat")) {
         return this._StdoutFormat;
      } else if (var1.equals("StdoutLogStack")) {
         return new Boolean(this._StdoutLogStack);
      } else if (var1.equals("StdoutSeverityLevel")) {
         return new Integer(this._StdoutSeverityLevel);
      } else if (var1.equals("SupportedProtocols")) {
         return this._SupportedProtocols;
      } else if (var1.equals("SystemPassword")) {
         return this._SystemPassword;
      } else if (var1.equals("SystemPasswordEncrypted")) {
         return this._SystemPasswordEncrypted;
      } else if (var1.equals("TGIOPEnabled")) {
         return new Boolean(this._TGIOPEnabled);
      } else if (var1.equals("ThreadPoolSize")) {
         return new Integer(this._ThreadPoolSize);
      } else if (var1.equals("TransactionLogFilePrefix")) {
         return this._TransactionLogFilePrefix;
      } else if (var1.equals("TransactionLogFileWritePolicy")) {
         return this._TransactionLogFileWritePolicy;
      } else if (var1.equals("TransactionLogJDBCStore")) {
         return this._TransactionLogJDBCStore;
      } else if (var1.equals("TunnelingClientPingSecs")) {
         return new Integer(this._TunnelingClientPingSecs);
      } else if (var1.equals("TunnelingClientTimeoutSecs")) {
         return new Integer(this._TunnelingClientTimeoutSecs);
      } else if (var1.equals("TunnelingEnabled")) {
         return new Boolean(this._TunnelingEnabled);
      } else if (var1.equals("UploadDirectoryName")) {
         return this._UploadDirectoryName;
      } else if (var1.equals("UseFusionForLLR")) {
         return new Boolean(this._UseFusionForLLR);
      } else if (var1.equals("VerboseEJBDeploymentEnabled")) {
         return this._VerboseEJBDeploymentEnabled;
      } else if (var1.equals("VirtualMachineName")) {
         return this._VirtualMachineName;
      } else if (var1.equals("WebServer")) {
         return this._WebServer;
      } else if (var1.equals("WebService")) {
         return this._WebService;
      } else if (var1.equals("WeblogicPluginEnabled")) {
         return new Boolean(this._WeblogicPluginEnabled);
      } else if (var1.equals("XMLEntityCache")) {
         return this._XMLEntityCache;
      } else if (var1.equals("XMLRegistry")) {
         return this._XMLRegistry;
      } else {
         return var1.equals("customizer") ? this._customizer : super.getValue(var1);
      }
   }

   public static void validateGeneration() {
      try {
         weblogic.descriptor.beangen.LegalChecks.checkNonNull("TransactionLogFileWritePolicy", "Direct-Write");
      } catch (IllegalArgumentException var1) {
         throw new DescriptorValidateException("The default value for the property  is null. Properties annotated with false value on @legalZeroLength or @legalNull  should either have @required/@derivedDefault annotations or have a non-null value on @default annotation. Refer annotation legalNull on property TransactionLogFileWritePolicy in ServerMBean" + var1.getMessage());
      }
   }

   public static class SchemaHelper2 extends KernelMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 3:
               if (var1.equals("com")) {
                  return 106;
               }
               break;
            case 4:
               if (var1.equals("name")) {
                  return 2;
               }
            case 5:
            case 6:
            case 8:
            case 9:
            case 36:
            case 39:
            case 42:
            case 43:
            case 45:
            case 46:
            case 48:
            case 49:
            case 50:
            default:
               break;
            case 7:
               if (var1.equals("cluster")) {
                  return 83;
               }

               if (var1.equals("machine")) {
                  return 79;
               }
               break;
            case 10:
               if (var1.equals("key-stores")) {
                  return 176;
               }

               if (var1.equals("web-server")) {
                  return 90;
               }
               break;
            case 11:
               if (var1.equals("data-source")) {
                  return 211;
               }

               if (var1.equals("listen-port")) {
                  return 80;
               }

               if (var1.equals("restart-max")) {
                  return 157;
               }

               if (var1.equals("web-service")) {
                  return 205;
               }

               if (var1.equals("com-enabled")) {
                  return 104;
               }
               break;
            case 12:
               if (var1.equals("auto-restart")) {
                  return 154;
               }

               if (var1.equals("kernel-debug")) {
                  return 48;
               }

               if (var1.equals("server-debug")) {
                  return 107;
               }

               if (var1.equals("server-names")) {
                  return 77;
               }

               if (var1.equals("server-start")) {
                  return 143;
               }

               if (var1.equals("staging-mode")) {
                  return 153;
               }

               if (var1.equals("startup-mode")) {
                  return 165;
               }

               if (var1.equals("xml-registry")) {
                  return 125;
               }

               if (var1.equals("iiop-enabled")) {
                  return 96;
               }

               if (var1.equals("jrmp-enabled")) {
                  return 105;
               }
               break;
            case 13:
               if (var1.equals("java-compiler")) {
                  return 127;
               }

               if (var1.equals("login-timeout")) {
                  return 82;
               }

               if (var1.equals("stdout-format")) {
                  return 60;
               }

               if (var1.equals("httpd-enabled")) {
                  return 108;
               }

               if (var1.equals("tgiop-enabled")) {
                  return 100;
               }
               break;
            case 14:
               if (var1.equals("accept-backlog")) {
                  return 118;
               }

               if (var1.equals("cluster-weight")) {
                  return 84;
               }

               if (var1.equals("listen-address")) {
                  return 114;
               }

               if (var1.equals("root-directory")) {
                  return 78;
               }

               if (var1.equals("server-version")) {
                  return 164;
               }

               if (var1.equals("stdout-enabled")) {
                  return 53;
               }
               break;
            case 15:
               if (var1.equals("cluster-runtime")) {
                  return 89;
               }

               if (var1.equals("expected-to-run")) {
                  return 91;
               }

               if (var1.equals("startup-timeout")) {
                  return 167;
               }

               if (var1.equals("system-password")) {
                  return 109;
               }
               break;
            case 16:
               if (var1.equals("defaultiiop-user")) {
                  return 97;
               }

               if (var1.equals("externaldns-name")) {
                  return 115;
               }

               if (var1.equals("thread-pool-size")) {
                  return 12;
               }

               if (var1.equals("xml-entity-cache")) {
                  return 126;
               }

               if (var1.equals("stdout-log-stack")) {
                  return 61;
               }
               break;
            case 17:
               if (var1.equals("candidate-machine")) {
                  return 190;
               }

               if (var1.equals("defaulttgiop-user")) {
                  return 101;
               }

               if (var1.equals("domain-log-filter")) {
                  return 137;
               }

               if (var1.equals("interface-address")) {
                  return 116;
               }

               if (var1.equals("listen-delay-secs")) {
                  return 144;
               }

               if (var1.equals("replication-group")) {
                  return 85;
               }

               if (var1.equals("replication-ports")) {
                  return 209;
               }

               if (var1.equals("tunneling-enabled")) {
                  return 138;
               }

               if (var1.equals("use-fusion-forllr")) {
                  return 193;
               }
               break;
            case 18:
               if (var1.equals("default-file-store")) {
                  return 189;
               }

               if (var1.equals("extra-ejbc-options")) {
                  return 131;
               }

               if (var1.equals("extra-rmic-options")) {
                  return 130;
               }

               if (var1.equals("jdbcllr-table-name")) {
                  return 192;
               }

               if (var1.equals("jdbc-log-file-name")) {
                  return 93;
               }

               if (var1.equals("supported-protocol")) {
                  return 200;
               }
               break;
            case 19:
               if (var1.equals("administration-port")) {
                  return 122;
               }

               if (var1.equals("auto-kill-if-failed")) {
                  return 155;
               }

               if (var1.equals("federation-services")) {
                  return 203;
               }

               if (var1.equals("overload-protection")) {
                  return 191;
               }

               if (var1.equals("listen-port-enabled")) {
                  return 81;
               }
               break;
            case 20:
               if (var1.equals("defaultiiop-password")) {
                  return 98;
               }

               if (var1.equals("listeners-bind-early")) {
                  return 113;
               }

               if (var1.equals("login-timeout-millis")) {
                  return 120;
               }

               if (var1.equals("network-access-point")) {
                  return 117;
               }

               if (var1.equals("virtual-machine-name")) {
                  return 208;
               }

               if (var1.equals("jdbc-logging-enabled")) {
                  return 92;
               }

               if (var1.equals("stdout-debug-enabled")) {
                  return 55;
               }
               break;
            case 21:
               if (var1.equals("active-directory-name")) {
                  return 152;
               }

               if (var1.equals("defaulttgiop-password")) {
                  return 102;
               }

               if (var1.equals("iiop-connection-pools")) {
                  return 124;
               }

               if (var1.equals("jta-migratable-target")) {
                  return 145;
               }

               if (var1.equals("restart-delay-seconds")) {
                  return 161;
               }

               if (var1.equals("stdout-severity-level")) {
                  return 54;
               }

               if (var1.equals("upload-directory-name")) {
                  return 151;
               }

               if (var1.equals("console-input-enabled")) {
                  return 111;
               }
               break;
            case 22:
               if (var1.equals("low-memorygc-threshold")) {
                  return 149;
               }

               if (var1.equals("low-memory-sample-size")) {
                  return 147;
               }

               if (var1.equals("staging-directory-name")) {
                  return 150;
               }

               if (var1.equals("auto-migration-enabled")) {
                  return 88;
               }

               if (var1.equals("enabled-for-domain-log")) {
                  return 136;
               }

               if (var1.equals("j2ee13-warning-enabled")) {
                  return 95;
               }
               break;
            case 23:
               if (var1.equals("single-sign-on-services")) {
                  return 204;
               }

               if (var1.equals("weblogic-plugin-enabled")) {
                  return 173;
               }
               break;
            case 24:
               if (var1.equals("default-staging-dir-name")) {
                  return 201;
               }

               if (var1.equals("low-memory-time-interval")) {
                  return 146;
               }

               if (var1.equals("reliable-delivery-policy")) {
                  return 187;
               }

               if (var1.equals("restart-interval-seconds")) {
                  return 156;
               }

               if (var1.equals("server-diagnostic-config")) {
                  return 198;
               }

               if (var1.equals("j2ee12-only-mode-enabled")) {
                  return 94;
               }
               break;
            case 25:
               if (var1.equals("graceful-shutdown-timeout")) {
                  return 168;
               }

               if (var1.equals("hosts-migratable-services")) {
                  return 174;
               }

               if (var1.equals("preferred-secondary-group")) {
                  return 86;
               }

               if (var1.equals("system-password-encrypted")) {
                  return 110;
               }

               if (var1.equals("client-cert-proxy-enabled")) {
                  return 172;
               }

               if (var1.equals("message-id-prefix-enabled")) {
                  return 188;
               }
               break;
            case 26:
               if (var1.equals("auto-jdbc-connection-close")) {
                  return 199;
               }

               if (var1.equals("jdbc-login-timeout-seconds")) {
                  return 197;
               }

               if (var1.equals("transaction-log-jdbc-store")) {
                  return 210;
               }

               if (var1.equals("tunneling-client-ping-secs")) {
                  return 139;
               }

               if (var1.equals("classpath-servlet-disabled")) {
                  return 162;
               }

               if (var1.equals("http-trace-support-enabled")) {
                  return 175;
               }
               break;
            case 27:
               if (var1.equals("custom-trust-key-store-type")) {
                  return 182;
               }

               if (var1.equals("transaction-log-file-prefix")) {
                  return 133;
               }

               if (var1.equals("administration-port-enabled")) {
                  return 121;
               }
               break;
            case 28:
               if (var1.equals("consensus-process-identifier")) {
                  return 87;
               }

               if (var1.equals("health-check-timeout-seconds")) {
                  return 159;
               }

               if (var1.equals("jdbcllr-tablexid-column-size")) {
                  return 194;
               }

               if (var1.equals("java-compiler-pre-class-path")) {
                  return 128;
               }

               if (var1.equals("low-memory-granularity-level")) {
                  return 148;
               }

               if (var1.equals("max-backoff-between-failures")) {
                  return 119;
               }

               if (var1.equals("msi-file-replication-enabled")) {
                  return 171;
               }
               break;
            case 29:
               if (var1.equals("health-check-interval-seconds")) {
                  return 158;
               }

               if (var1.equals("java-compiler-post-class-path")) {
                  return 129;
               }

               if (var1.equals("server-life-cycle-timeout-val")) {
                  return 166;
               }

               if (var1.equals("tunneling-client-timeout-secs")) {
                  return 140;
               }

               if (var1.equals("verboseejb-deployment-enabled")) {
                  return 132;
               }

               if (var1.equals("network-class-loading-enabled")) {
                  return 135;
               }
               break;
            case 30:
               if (var1.equals("custom-identity-key-store-type")) {
                  return 178;
               }

               if (var1.equals("defaultiiop-password-encrypted")) {
                  return 99;
               }

               if (var1.equals("jdbcllr-table-pool-column-size")) {
                  return 195;
               }

               if (var1.equals("listen-thread-start-delay-secs")) {
                  return 112;
               }
               break;
            case 31:
               if (var1.equals("defaulttgiop-password-encrypted")) {
                  return 103;
               }

               if (var1.equals("ignore-sessions-during-shutdown")) {
                  return 169;
               }
               break;
            case 32:
               if (var1.equals("admin-reconnect-interval-seconds")) {
                  return 141;
               }

               if (var1.equals("custom-trust-key-store-file-name")) {
                  return 181;
               }

               if (var1.equals("health-check-start-delay-seconds")) {
                  return 160;
               }

               if (var1.equals("jdbcllr-table-record-column-size")) {
                  return 196;
               }
               break;
            case 33:
               if (var1.equals("81-style-default-staging-dir-name")) {
                  return 202;
               }

               if (var1.equals("coherence-cluster-system-resource")) {
                  return 207;
               }

               if (var1.equals("transaction-log-file-write-policy")) {
                  return 134;
               }
               break;
            case 34:
               if (var1.equals("custom-trust-key-store-pass-phrase")) {
                  return 183;
               }

               if (var1.equals("nm-socket-create-timeout-in-millis")) {
                  return 206;
               }

               if (var1.equals("default-internal-servlets-disabled")) {
                  return 163;
               }
               break;
            case 35:
               if (var1.equals("custom-identity-key-store-file-name")) {
                  return 177;
               }

               if (var1.equals("managed-server-independence-enabled")) {
                  return 170;
               }
               break;
            case 37:
               if (var1.equals("custom-identity-key-store-pass-phrase")) {
                  return 179;
               }
               break;
            case 38:
               if (var1.equals("jndi-transportable-object-factory-list")) {
                  return 123;
               }
               break;
            case 40:
               if (var1.equals("jms-default-connection-factories-enabled")) {
                  return 142;
               }
               break;
            case 41:
               if (var1.equals("java-standard-trust-key-store-pass-phrase")) {
                  return 185;
               }
               break;
            case 44:
               if (var1.equals("custom-trust-key-store-pass-phrase-encrypted")) {
                  return 184;
               }
               break;
            case 47:
               if (var1.equals("custom-identity-key-store-pass-phrase-encrypted")) {
                  return 180;
               }
               break;
            case 51:
               if (var1.equals("java-standard-trust-key-store-pass-phrase-encrypted")) {
                  return 186;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 50:
               return new SSLMBeanImpl.SchemaHelper2();
            case 51:
               return new IIOPMBeanImpl.SchemaHelper2();
            case 52:
               return new LogMBeanImpl.SchemaHelper2();
            case 58:
               return new ExecuteQueueMBeanImpl.SchemaHelper2();
            case 90:
               return new WebServerMBeanImpl.SchemaHelper2();
            case 106:
               return new COMMBeanImpl.SchemaHelper2();
            case 107:
               return new ServerDebugMBeanImpl.SchemaHelper2();
            case 117:
               return new NetworkAccessPointMBeanImpl.SchemaHelper2();
            case 143:
               return new ServerStartMBeanImpl.SchemaHelper2();
            case 145:
               return new JTAMigratableTargetMBeanImpl.SchemaHelper2();
            case 189:
               return new DefaultFileStoreMBeanImpl.SchemaHelper2();
            case 191:
               return new OverloadProtectionMBeanImpl.SchemaHelper2();
            case 198:
               return new WLDFServerDiagnosticMBeanImpl.SchemaHelper2();
            case 203:
               return new FederationServicesMBeanImpl.SchemaHelper2();
            case 204:
               return new SingleSignOnServicesMBeanImpl.SchemaHelper2();
            case 205:
               return new WebServiceMBeanImpl.SchemaHelper2();
            case 210:
               return new TransactionLogJDBCStoreMBeanImpl.SchemaHelper2();
            case 211:
               return new DataSourceMBeanImpl.SchemaHelper2();
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
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
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
            case 49:
            case 50:
            case 51:
            case 52:
            case 56:
            case 57:
            case 58:
            case 59:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 68:
            case 69:
            case 70:
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
            case 76:
            default:
               return super.getElementName(var1);
            case 12:
               return "thread-pool-size";
            case 48:
               return "kernel-debug";
            case 53:
               return "stdout-enabled";
            case 54:
               return "stdout-severity-level";
            case 55:
               return "stdout-debug-enabled";
            case 60:
               return "stdout-format";
            case 61:
               return "stdout-log-stack";
            case 77:
               return "server-names";
            case 78:
               return "root-directory";
            case 79:
               return "machine";
            case 80:
               return "listen-port";
            case 81:
               return "listen-port-enabled";
            case 82:
               return "login-timeout";
            case 83:
               return "cluster";
            case 84:
               return "cluster-weight";
            case 85:
               return "replication-group";
            case 86:
               return "preferred-secondary-group";
            case 87:
               return "consensus-process-identifier";
            case 88:
               return "auto-migration-enabled";
            case 89:
               return "cluster-runtime";
            case 90:
               return "web-server";
            case 91:
               return "expected-to-run";
            case 92:
               return "jdbc-logging-enabled";
            case 93:
               return "jdbc-log-file-name";
            case 94:
               return "j2ee12-only-mode-enabled";
            case 95:
               return "j2ee13-warning-enabled";
            case 96:
               return "iiop-enabled";
            case 97:
               return "defaultiiop-user";
            case 98:
               return "defaultiiop-password";
            case 99:
               return "defaultiiop-password-encrypted";
            case 100:
               return "tgiop-enabled";
            case 101:
               return "defaulttgiop-user";
            case 102:
               return "defaulttgiop-password";
            case 103:
               return "defaulttgiop-password-encrypted";
            case 104:
               return "com-enabled";
            case 105:
               return "jrmp-enabled";
            case 106:
               return "com";
            case 107:
               return "server-debug";
            case 108:
               return "httpd-enabled";
            case 109:
               return "system-password";
            case 110:
               return "system-password-encrypted";
            case 111:
               return "console-input-enabled";
            case 112:
               return "listen-thread-start-delay-secs";
            case 113:
               return "listeners-bind-early";
            case 114:
               return "listen-address";
            case 115:
               return "externaldns-name";
            case 116:
               return "interface-address";
            case 117:
               return "network-access-point";
            case 118:
               return "accept-backlog";
            case 119:
               return "max-backoff-between-failures";
            case 120:
               return "login-timeout-millis";
            case 121:
               return "administration-port-enabled";
            case 122:
               return "administration-port";
            case 123:
               return "jndi-transportable-object-factory-list";
            case 124:
               return "iiop-connection-pools";
            case 125:
               return "xml-registry";
            case 126:
               return "xml-entity-cache";
            case 127:
               return "java-compiler";
            case 128:
               return "java-compiler-pre-class-path";
            case 129:
               return "java-compiler-post-class-path";
            case 130:
               return "extra-rmic-options";
            case 131:
               return "extra-ejbc-options";
            case 132:
               return "verboseejb-deployment-enabled";
            case 133:
               return "transaction-log-file-prefix";
            case 134:
               return "transaction-log-file-write-policy";
            case 135:
               return "network-class-loading-enabled";
            case 136:
               return "enabled-for-domain-log";
            case 137:
               return "domain-log-filter";
            case 138:
               return "tunneling-enabled";
            case 139:
               return "tunneling-client-ping-secs";
            case 140:
               return "tunneling-client-timeout-secs";
            case 141:
               return "admin-reconnect-interval-seconds";
            case 142:
               return "jms-default-connection-factories-enabled";
            case 143:
               return "server-start";
            case 144:
               return "listen-delay-secs";
            case 145:
               return "jta-migratable-target";
            case 146:
               return "low-memory-time-interval";
            case 147:
               return "low-memory-sample-size";
            case 148:
               return "low-memory-granularity-level";
            case 149:
               return "low-memorygc-threshold";
            case 150:
               return "staging-directory-name";
            case 151:
               return "upload-directory-name";
            case 152:
               return "active-directory-name";
            case 153:
               return "staging-mode";
            case 154:
               return "auto-restart";
            case 155:
               return "auto-kill-if-failed";
            case 156:
               return "restart-interval-seconds";
            case 157:
               return "restart-max";
            case 158:
               return "health-check-interval-seconds";
            case 159:
               return "health-check-timeout-seconds";
            case 160:
               return "health-check-start-delay-seconds";
            case 161:
               return "restart-delay-seconds";
            case 162:
               return "classpath-servlet-disabled";
            case 163:
               return "default-internal-servlets-disabled";
            case 164:
               return "server-version";
            case 165:
               return "startup-mode";
            case 166:
               return "server-life-cycle-timeout-val";
            case 167:
               return "startup-timeout";
            case 168:
               return "graceful-shutdown-timeout";
            case 169:
               return "ignore-sessions-during-shutdown";
            case 170:
               return "managed-server-independence-enabled";
            case 171:
               return "msi-file-replication-enabled";
            case 172:
               return "client-cert-proxy-enabled";
            case 173:
               return "weblogic-plugin-enabled";
            case 174:
               return "hosts-migratable-services";
            case 175:
               return "http-trace-support-enabled";
            case 176:
               return "key-stores";
            case 177:
               return "custom-identity-key-store-file-name";
            case 178:
               return "custom-identity-key-store-type";
            case 179:
               return "custom-identity-key-store-pass-phrase";
            case 180:
               return "custom-identity-key-store-pass-phrase-encrypted";
            case 181:
               return "custom-trust-key-store-file-name";
            case 182:
               return "custom-trust-key-store-type";
            case 183:
               return "custom-trust-key-store-pass-phrase";
            case 184:
               return "custom-trust-key-store-pass-phrase-encrypted";
            case 185:
               return "java-standard-trust-key-store-pass-phrase";
            case 186:
               return "java-standard-trust-key-store-pass-phrase-encrypted";
            case 187:
               return "reliable-delivery-policy";
            case 188:
               return "message-id-prefix-enabled";
            case 189:
               return "default-file-store";
            case 190:
               return "candidate-machine";
            case 191:
               return "overload-protection";
            case 192:
               return "jdbcllr-table-name";
            case 193:
               return "use-fusion-forllr";
            case 194:
               return "jdbcllr-tablexid-column-size";
            case 195:
               return "jdbcllr-table-pool-column-size";
            case 196:
               return "jdbcllr-table-record-column-size";
            case 197:
               return "jdbc-login-timeout-seconds";
            case 198:
               return "server-diagnostic-config";
            case 199:
               return "auto-jdbc-connection-close";
            case 200:
               return "supported-protocol";
            case 201:
               return "default-staging-dir-name";
            case 202:
               return "81-style-default-staging-dir-name";
            case 203:
               return "federation-services";
            case 204:
               return "single-sign-on-services";
            case 205:
               return "web-service";
            case 206:
               return "nm-socket-create-timeout-in-millis";
            case 207:
               return "coherence-cluster-system-resource";
            case 208:
               return "virtual-machine-name";
            case 209:
               return "replication-ports";
            case 210:
               return "transaction-log-jdbc-store";
            case 211:
               return "data-source";
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 58:
               return true;
            case 117:
               return true;
            case 123:
               return true;
            case 190:
               return true;
            case 200:
               return true;
            default:
               return super.isArray(var1);
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            case 50:
               return true;
            case 51:
               return true;
            case 52:
               return true;
            case 58:
               return true;
            case 90:
               return true;
            case 106:
               return true;
            case 107:
               return true;
            case 117:
               return true;
            case 143:
               return true;
            case 145:
               return true;
            case 189:
               return true;
            case 191:
               return true;
            case 198:
               return true;
            case 203:
               return true;
            case 204:
               return true;
            case 205:
               return true;
            case 210:
               return true;
            case 211:
               return true;
            default:
               return super.isBean(var1);
         }
      }

      public boolean isConfigurable(int var1) {
         switch (var1) {
            case 28:
               return true;
            case 79:
               return true;
            case 80:
               return true;
            case 83:
               return true;
            case 84:
               return true;
            case 87:
               return true;
            case 91:
               return true;
            case 115:
               return true;
            case 116:
               return true;
            case 125:
               return true;
            case 126:
               return true;
            case 135:
               return true;
            case 141:
               return true;
            case 144:
               return true;
            case 146:
               return true;
            case 147:
               return true;
            case 148:
               return true;
            case 149:
               return true;
            case 154:
               return true;
            case 155:
               return true;
            case 156:
               return true;
            case 157:
               return true;
            case 158:
               return true;
            case 159:
               return true;
            case 160:
               return true;
            case 161:
               return true;
            case 165:
               return true;
            case 166:
               return true;
            case 167:
               return true;
            case 168:
               return true;
            case 169:
               return true;
            case 174:
               return true;
            case 187:
               return true;
            case 188:
               return true;
            case 210:
               return true;
            default:
               return super.isConfigurable(var1);
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

   protected static class Helper extends KernelMBeanImpl.Helper {
      private ServerMBeanImpl bean;

      protected Helper(ServerMBeanImpl var1) {
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
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
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
            case 49:
            case 50:
            case 51:
            case 52:
            case 56:
            case 57:
            case 58:
            case 59:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 68:
            case 69:
            case 70:
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
            case 76:
            default:
               return super.getPropertyName(var1);
            case 12:
               return "ThreadPoolSize";
            case 48:
               return "KernelDebug";
            case 53:
               return "StdoutEnabled";
            case 54:
               return "StdoutSeverityLevel";
            case 55:
               return "StdoutDebugEnabled";
            case 60:
               return "StdoutFormat";
            case 61:
               return "StdoutLogStack";
            case 77:
               return "ServerNames";
            case 78:
               return "RootDirectory";
            case 79:
               return "Machine";
            case 80:
               return "ListenPort";
            case 81:
               return "ListenPortEnabled";
            case 82:
               return "LoginTimeout";
            case 83:
               return "Cluster";
            case 84:
               return "ClusterWeight";
            case 85:
               return "ReplicationGroup";
            case 86:
               return "PreferredSecondaryGroup";
            case 87:
               return "ConsensusProcessIdentifier";
            case 88:
               return "AutoMigrationEnabled";
            case 89:
               return "ClusterRuntime";
            case 90:
               return "WebServer";
            case 91:
               return "ExpectedToRun";
            case 92:
               return "JDBCLoggingEnabled";
            case 93:
               return "JDBCLogFileName";
            case 94:
               return "J2EE12OnlyModeEnabled";
            case 95:
               return "J2EE13WarningEnabled";
            case 96:
               return "IIOPEnabled";
            case 97:
               return "DefaultIIOPUser";
            case 98:
               return "DefaultIIOPPassword";
            case 99:
               return "DefaultIIOPPasswordEncrypted";
            case 100:
               return "TGIOPEnabled";
            case 101:
               return "DefaultTGIOPUser";
            case 102:
               return "DefaultTGIOPPassword";
            case 103:
               return "DefaultTGIOPPasswordEncrypted";
            case 104:
               return "COMEnabled";
            case 105:
               return "JRMPEnabled";
            case 106:
               return "COM";
            case 107:
               return "ServerDebug";
            case 108:
               return "HttpdEnabled";
            case 109:
               return "SystemPassword";
            case 110:
               return "SystemPasswordEncrypted";
            case 111:
               return "ConsoleInputEnabled";
            case 112:
               return "ListenThreadStartDelaySecs";
            case 113:
               return "ListenersBindEarly";
            case 114:
               return "ListenAddress";
            case 115:
               return "ExternalDNSName";
            case 116:
               return "InterfaceAddress";
            case 117:
               return "NetworkAccessPoints";
            case 118:
               return "AcceptBacklog";
            case 119:
               return "MaxBackoffBetweenFailures";
            case 120:
               return "LoginTimeoutMillis";
            case 121:
               return "AdministrationPortEnabled";
            case 122:
               return "AdministrationPort";
            case 123:
               return "JNDITransportableObjectFactoryList";
            case 124:
               return "IIOPConnectionPools";
            case 125:
               return "XMLRegistry";
            case 126:
               return "XMLEntityCache";
            case 127:
               return "JavaCompiler";
            case 128:
               return "JavaCompilerPreClassPath";
            case 129:
               return "JavaCompilerPostClassPath";
            case 130:
               return "ExtraRmicOptions";
            case 131:
               return "ExtraEjbcOptions";
            case 132:
               return "VerboseEJBDeploymentEnabled";
            case 133:
               return "TransactionLogFilePrefix";
            case 134:
               return "TransactionLogFileWritePolicy";
            case 135:
               return "NetworkClassLoadingEnabled";
            case 136:
               return "EnabledForDomainLog";
            case 137:
               return "DomainLogFilter";
            case 138:
               return "TunnelingEnabled";
            case 139:
               return "TunnelingClientPingSecs";
            case 140:
               return "TunnelingClientTimeoutSecs";
            case 141:
               return "AdminReconnectIntervalSeconds";
            case 142:
               return "JMSDefaultConnectionFactoriesEnabled";
            case 143:
               return "ServerStart";
            case 144:
               return "ListenDelaySecs";
            case 145:
               return "JTAMigratableTarget";
            case 146:
               return "LowMemoryTimeInterval";
            case 147:
               return "LowMemorySampleSize";
            case 148:
               return "LowMemoryGranularityLevel";
            case 149:
               return "LowMemoryGCThreshold";
            case 150:
               return "StagingDirectoryName";
            case 151:
               return "UploadDirectoryName";
            case 152:
               return "ActiveDirectoryName";
            case 153:
               return "StagingMode";
            case 154:
               return "AutoRestart";
            case 155:
               return "AutoKillIfFailed";
            case 156:
               return "RestartIntervalSeconds";
            case 157:
               return "RestartMax";
            case 158:
               return "HealthCheckIntervalSeconds";
            case 159:
               return "HealthCheckTimeoutSeconds";
            case 160:
               return "HealthCheckStartDelaySeconds";
            case 161:
               return "RestartDelaySeconds";
            case 162:
               return "ClasspathServletDisabled";
            case 163:
               return "DefaultInternalServletsDisabled";
            case 164:
               return "ServerVersion";
            case 165:
               return "StartupMode";
            case 166:
               return "ServerLifeCycleTimeoutVal";
            case 167:
               return "StartupTimeout";
            case 168:
               return "GracefulShutdownTimeout";
            case 169:
               return "IgnoreSessionsDuringShutdown";
            case 170:
               return "ManagedServerIndependenceEnabled";
            case 171:
               return "MSIFileReplicationEnabled";
            case 172:
               return "ClientCertProxyEnabled";
            case 173:
               return "WeblogicPluginEnabled";
            case 174:
               return "HostsMigratableServices";
            case 175:
               return "HttpTraceSupportEnabled";
            case 176:
               return "KeyStores";
            case 177:
               return "CustomIdentityKeyStoreFileName";
            case 178:
               return "CustomIdentityKeyStoreType";
            case 179:
               return "CustomIdentityKeyStorePassPhrase";
            case 180:
               return "CustomIdentityKeyStorePassPhraseEncrypted";
            case 181:
               return "CustomTrustKeyStoreFileName";
            case 182:
               return "CustomTrustKeyStoreType";
            case 183:
               return "CustomTrustKeyStorePassPhrase";
            case 184:
               return "CustomTrustKeyStorePassPhraseEncrypted";
            case 185:
               return "JavaStandardTrustKeyStorePassPhrase";
            case 186:
               return "JavaStandardTrustKeyStorePassPhraseEncrypted";
            case 187:
               return "ReliableDeliveryPolicy";
            case 188:
               return "MessageIdPrefixEnabled";
            case 189:
               return "DefaultFileStore";
            case 190:
               return "CandidateMachines";
            case 191:
               return "OverloadProtection";
            case 192:
               return "JDBCLLRTableName";
            case 193:
               return "UseFusionForLLR";
            case 194:
               return "JDBCLLRTableXIDColumnSize";
            case 195:
               return "JDBCLLRTablePoolColumnSize";
            case 196:
               return "JDBCLLRTableRecordColumnSize";
            case 197:
               return "JDBCLoginTimeoutSeconds";
            case 198:
               return "ServerDiagnosticConfig";
            case 199:
               return "AutoJDBCConnectionClose";
            case 200:
               return "SupportedProtocols";
            case 201:
               return "DefaultStagingDirName";
            case 202:
               return "81StyleDefaultStagingDirName";
            case 203:
               return "FederationServices";
            case 204:
               return "SingleSignOnServices";
            case 205:
               return "WebService";
            case 206:
               return "NMSocketCreateTimeoutInMillis";
            case 207:
               return "CoherenceClusterSystemResource";
            case 208:
               return "VirtualMachineName";
            case 209:
               return "ReplicationPorts";
            case 210:
               return "TransactionLogJDBCStore";
            case 211:
               return "DataSource";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("81StyleDefaultStagingDirName")) {
            return 202;
         } else if (var1.equals("AcceptBacklog")) {
            return 118;
         } else if (var1.equals("ActiveDirectoryName")) {
            return 152;
         } else if (var1.equals("AdminReconnectIntervalSeconds")) {
            return 141;
         } else if (var1.equals("AdministrationPort")) {
            return 122;
         } else if (var1.equals("AutoJDBCConnectionClose")) {
            return 199;
         } else if (var1.equals("AutoKillIfFailed")) {
            return 155;
         } else if (var1.equals("AutoRestart")) {
            return 154;
         } else if (var1.equals("COM")) {
            return 106;
         } else if (var1.equals("CandidateMachines")) {
            return 190;
         } else if (var1.equals("Cluster")) {
            return 83;
         } else if (var1.equals("ClusterRuntime")) {
            return 89;
         } else if (var1.equals("ClusterWeight")) {
            return 84;
         } else if (var1.equals("CoherenceClusterSystemResource")) {
            return 207;
         } else if (var1.equals("ConsensusProcessIdentifier")) {
            return 87;
         } else if (var1.equals("CustomIdentityKeyStoreFileName")) {
            return 177;
         } else if (var1.equals("CustomIdentityKeyStorePassPhrase")) {
            return 179;
         } else if (var1.equals("CustomIdentityKeyStorePassPhraseEncrypted")) {
            return 180;
         } else if (var1.equals("CustomIdentityKeyStoreType")) {
            return 178;
         } else if (var1.equals("CustomTrustKeyStoreFileName")) {
            return 181;
         } else if (var1.equals("CustomTrustKeyStorePassPhrase")) {
            return 183;
         } else if (var1.equals("CustomTrustKeyStorePassPhraseEncrypted")) {
            return 184;
         } else if (var1.equals("CustomTrustKeyStoreType")) {
            return 182;
         } else if (var1.equals("DataSource")) {
            return 211;
         } else if (var1.equals("DefaultFileStore")) {
            return 189;
         } else if (var1.equals("DefaultIIOPPassword")) {
            return 98;
         } else if (var1.equals("DefaultIIOPPasswordEncrypted")) {
            return 99;
         } else if (var1.equals("DefaultIIOPUser")) {
            return 97;
         } else if (var1.equals("DefaultStagingDirName")) {
            return 201;
         } else if (var1.equals("DefaultTGIOPPassword")) {
            return 102;
         } else if (var1.equals("DefaultTGIOPPasswordEncrypted")) {
            return 103;
         } else if (var1.equals("DefaultTGIOPUser")) {
            return 101;
         } else if (var1.equals("DomainLogFilter")) {
            return 137;
         } else if (var1.equals("ExpectedToRun")) {
            return 91;
         } else if (var1.equals("ExternalDNSName")) {
            return 115;
         } else if (var1.equals("ExtraEjbcOptions")) {
            return 131;
         } else if (var1.equals("ExtraRmicOptions")) {
            return 130;
         } else if (var1.equals("FederationServices")) {
            return 203;
         } else if (var1.equals("GracefulShutdownTimeout")) {
            return 168;
         } else if (var1.equals("HealthCheckIntervalSeconds")) {
            return 158;
         } else if (var1.equals("HealthCheckStartDelaySeconds")) {
            return 160;
         } else if (var1.equals("HealthCheckTimeoutSeconds")) {
            return 159;
         } else if (var1.equals("HostsMigratableServices")) {
            return 174;
         } else if (var1.equals("IIOPConnectionPools")) {
            return 124;
         } else if (var1.equals("InterfaceAddress")) {
            return 116;
         } else if (var1.equals("JDBCLLRTableName")) {
            return 192;
         } else if (var1.equals("JDBCLLRTablePoolColumnSize")) {
            return 195;
         } else if (var1.equals("JDBCLLRTableRecordColumnSize")) {
            return 196;
         } else if (var1.equals("JDBCLLRTableXIDColumnSize")) {
            return 194;
         } else if (var1.equals("JDBCLogFileName")) {
            return 93;
         } else if (var1.equals("JDBCLoginTimeoutSeconds")) {
            return 197;
         } else if (var1.equals("JNDITransportableObjectFactoryList")) {
            return 123;
         } else if (var1.equals("JTAMigratableTarget")) {
            return 145;
         } else if (var1.equals("JavaCompiler")) {
            return 127;
         } else if (var1.equals("JavaCompilerPostClassPath")) {
            return 129;
         } else if (var1.equals("JavaCompilerPreClassPath")) {
            return 128;
         } else if (var1.equals("JavaStandardTrustKeyStorePassPhrase")) {
            return 185;
         } else if (var1.equals("JavaStandardTrustKeyStorePassPhraseEncrypted")) {
            return 186;
         } else if (var1.equals("KernelDebug")) {
            return 48;
         } else if (var1.equals("KeyStores")) {
            return 176;
         } else if (var1.equals("ListenAddress")) {
            return 114;
         } else if (var1.equals("ListenDelaySecs")) {
            return 144;
         } else if (var1.equals("ListenPort")) {
            return 80;
         } else if (var1.equals("ListenThreadStartDelaySecs")) {
            return 112;
         } else if (var1.equals("ListenersBindEarly")) {
            return 113;
         } else if (var1.equals("LoginTimeout")) {
            return 82;
         } else if (var1.equals("LoginTimeoutMillis")) {
            return 120;
         } else if (var1.equals("LowMemoryGCThreshold")) {
            return 149;
         } else if (var1.equals("LowMemoryGranularityLevel")) {
            return 148;
         } else if (var1.equals("LowMemorySampleSize")) {
            return 147;
         } else if (var1.equals("LowMemoryTimeInterval")) {
            return 146;
         } else if (var1.equals("Machine")) {
            return 79;
         } else if (var1.equals("MaxBackoffBetweenFailures")) {
            return 119;
         } else if (var1.equals("NMSocketCreateTimeoutInMillis")) {
            return 206;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("NetworkAccessPoints")) {
            return 117;
         } else if (var1.equals("OverloadProtection")) {
            return 191;
         } else if (var1.equals("PreferredSecondaryGroup")) {
            return 86;
         } else if (var1.equals("ReliableDeliveryPolicy")) {
            return 187;
         } else if (var1.equals("ReplicationGroup")) {
            return 85;
         } else if (var1.equals("ReplicationPorts")) {
            return 209;
         } else if (var1.equals("RestartDelaySeconds")) {
            return 161;
         } else if (var1.equals("RestartIntervalSeconds")) {
            return 156;
         } else if (var1.equals("RestartMax")) {
            return 157;
         } else if (var1.equals("RootDirectory")) {
            return 78;
         } else if (var1.equals("ServerDebug")) {
            return 107;
         } else if (var1.equals("ServerDiagnosticConfig")) {
            return 198;
         } else if (var1.equals("ServerLifeCycleTimeoutVal")) {
            return 166;
         } else if (var1.equals("ServerNames")) {
            return 77;
         } else if (var1.equals("ServerStart")) {
            return 143;
         } else if (var1.equals("ServerVersion")) {
            return 164;
         } else if (var1.equals("SingleSignOnServices")) {
            return 204;
         } else if (var1.equals("StagingDirectoryName")) {
            return 150;
         } else if (var1.equals("StagingMode")) {
            return 153;
         } else if (var1.equals("StartupMode")) {
            return 165;
         } else if (var1.equals("StartupTimeout")) {
            return 167;
         } else if (var1.equals("StdoutFormat")) {
            return 60;
         } else if (var1.equals("StdoutSeverityLevel")) {
            return 54;
         } else if (var1.equals("SupportedProtocols")) {
            return 200;
         } else if (var1.equals("SystemPassword")) {
            return 109;
         } else if (var1.equals("SystemPasswordEncrypted")) {
            return 110;
         } else if (var1.equals("ThreadPoolSize")) {
            return 12;
         } else if (var1.equals("TransactionLogFilePrefix")) {
            return 133;
         } else if (var1.equals("TransactionLogFileWritePolicy")) {
            return 134;
         } else if (var1.equals("TransactionLogJDBCStore")) {
            return 210;
         } else if (var1.equals("TunnelingClientPingSecs")) {
            return 139;
         } else if (var1.equals("TunnelingClientTimeoutSecs")) {
            return 140;
         } else if (var1.equals("UploadDirectoryName")) {
            return 151;
         } else if (var1.equals("VerboseEJBDeploymentEnabled")) {
            return 132;
         } else if (var1.equals("VirtualMachineName")) {
            return 208;
         } else if (var1.equals("WebServer")) {
            return 90;
         } else if (var1.equals("WebService")) {
            return 205;
         } else if (var1.equals("XMLEntityCache")) {
            return 126;
         } else if (var1.equals("XMLRegistry")) {
            return 125;
         } else if (var1.equals("AdministrationPortEnabled")) {
            return 121;
         } else if (var1.equals("AutoMigrationEnabled")) {
            return 88;
         } else if (var1.equals("COMEnabled")) {
            return 104;
         } else if (var1.equals("ClasspathServletDisabled")) {
            return 162;
         } else if (var1.equals("ClientCertProxyEnabled")) {
            return 172;
         } else if (var1.equals("ConsoleInputEnabled")) {
            return 111;
         } else if (var1.equals("DefaultInternalServletsDisabled")) {
            return 163;
         } else if (var1.equals("EnabledForDomainLog")) {
            return 136;
         } else if (var1.equals("HttpTraceSupportEnabled")) {
            return 175;
         } else if (var1.equals("HttpdEnabled")) {
            return 108;
         } else if (var1.equals("IIOPEnabled")) {
            return 96;
         } else if (var1.equals("IgnoreSessionsDuringShutdown")) {
            return 169;
         } else if (var1.equals("J2EE12OnlyModeEnabled")) {
            return 94;
         } else if (var1.equals("J2EE13WarningEnabled")) {
            return 95;
         } else if (var1.equals("JDBCLoggingEnabled")) {
            return 92;
         } else if (var1.equals("JMSDefaultConnectionFactoriesEnabled")) {
            return 142;
         } else if (var1.equals("JRMPEnabled")) {
            return 105;
         } else if (var1.equals("ListenPortEnabled")) {
            return 81;
         } else if (var1.equals("MSIFileReplicationEnabled")) {
            return 171;
         } else if (var1.equals("ManagedServerIndependenceEnabled")) {
            return 170;
         } else if (var1.equals("MessageIdPrefixEnabled")) {
            return 188;
         } else if (var1.equals("NetworkClassLoadingEnabled")) {
            return 135;
         } else if (var1.equals("StdoutDebugEnabled")) {
            return 55;
         } else if (var1.equals("StdoutEnabled")) {
            return 53;
         } else if (var1.equals("StdoutLogStack")) {
            return 61;
         } else if (var1.equals("TGIOPEnabled")) {
            return 100;
         } else if (var1.equals("TunnelingEnabled")) {
            return 138;
         } else if (var1.equals("UseFusionForLLR")) {
            return 193;
         } else {
            return var1.equals("WeblogicPluginEnabled") ? 173 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         if (this.bean.getCOM() != null) {
            var1.add(new ArrayIterator(new COMMBean[]{this.bean.getCOM()}));
         }

         if (this.bean.getDataSource() != null) {
            var1.add(new ArrayIterator(new DataSourceMBean[]{this.bean.getDataSource()}));
         }

         if (this.bean.getDefaultFileStore() != null) {
            var1.add(new ArrayIterator(new DefaultFileStoreMBean[]{this.bean.getDefaultFileStore()}));
         }

         var1.add(new ArrayIterator(this.bean.getExecuteQueues()));
         if (this.bean.getFederationServices() != null) {
            var1.add(new ArrayIterator(new FederationServicesMBean[]{this.bean.getFederationServices()}));
         }

         if (this.bean.getIIOP() != null) {
            var1.add(new ArrayIterator(new IIOPMBean[]{this.bean.getIIOP()}));
         }

         if (this.bean.getJTAMigratableTarget() != null) {
            var1.add(new ArrayIterator(new JTAMigratableTargetMBean[]{this.bean.getJTAMigratableTarget()}));
         }

         if (this.bean.getLog() != null) {
            var1.add(new ArrayIterator(new LogMBean[]{this.bean.getLog()}));
         }

         var1.add(new ArrayIterator(this.bean.getNetworkAccessPoints()));
         if (this.bean.getOverloadProtection() != null) {
            var1.add(new ArrayIterator(new OverloadProtectionMBean[]{this.bean.getOverloadProtection()}));
         }

         if (this.bean.getSSL() != null) {
            var1.add(new ArrayIterator(new SSLMBean[]{this.bean.getSSL()}));
         }

         if (this.bean.getServerDebug() != null) {
            var1.add(new ArrayIterator(new ServerDebugMBean[]{this.bean.getServerDebug()}));
         }

         if (this.bean.getServerDiagnosticConfig() != null) {
            var1.add(new ArrayIterator(new WLDFServerDiagnosticMBean[]{this.bean.getServerDiagnosticConfig()}));
         }

         if (this.bean.getServerStart() != null) {
            var1.add(new ArrayIterator(new ServerStartMBean[]{this.bean.getServerStart()}));
         }

         if (this.bean.getSingleSignOnServices() != null) {
            var1.add(new ArrayIterator(new SingleSignOnServicesMBean[]{this.bean.getSingleSignOnServices()}));
         }

         if (this.bean.getTransactionLogJDBCStore() != null) {
            var1.add(new ArrayIterator(new TransactionLogJDBCStoreMBean[]{this.bean.getTransactionLogJDBCStore()}));
         }

         if (this.bean.getWebServer() != null) {
            var1.add(new ArrayIterator(new WebServerMBean[]{this.bean.getWebServer()}));
         }

         if (this.bean.getWebService() != null) {
            var1.add(new ArrayIterator(new WebServiceMBean[]{this.bean.getWebService()}));
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
            if (this.bean.is81StyleDefaultStagingDirNameSet()) {
               var2.append("81StyleDefaultStagingDirName");
               var2.append(String.valueOf(this.bean.get81StyleDefaultStagingDirName()));
            }

            if (this.bean.isAcceptBacklogSet()) {
               var2.append("AcceptBacklog");
               var2.append(String.valueOf(this.bean.getAcceptBacklog()));
            }

            if (this.bean.isActiveDirectoryNameSet()) {
               var2.append("ActiveDirectoryName");
               var2.append(String.valueOf(this.bean.getActiveDirectoryName()));
            }

            if (this.bean.isAdminReconnectIntervalSecondsSet()) {
               var2.append("AdminReconnectIntervalSeconds");
               var2.append(String.valueOf(this.bean.getAdminReconnectIntervalSeconds()));
            }

            if (this.bean.isAdministrationPortSet()) {
               var2.append("AdministrationPort");
               var2.append(String.valueOf(this.bean.getAdministrationPort()));
            }

            if (this.bean.isAutoJDBCConnectionCloseSet()) {
               var2.append("AutoJDBCConnectionClose");
               var2.append(String.valueOf(this.bean.getAutoJDBCConnectionClose()));
            }

            if (this.bean.isAutoKillIfFailedSet()) {
               var2.append("AutoKillIfFailed");
               var2.append(String.valueOf(this.bean.getAutoKillIfFailed()));
            }

            if (this.bean.isAutoRestartSet()) {
               var2.append("AutoRestart");
               var2.append(String.valueOf(this.bean.getAutoRestart()));
            }

            var5 = this.computeChildHashValue(this.bean.getCOM());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isCandidateMachinesSet()) {
               var2.append("CandidateMachines");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getCandidateMachines())));
            }

            if (this.bean.isClusterSet()) {
               var2.append("Cluster");
               var2.append(String.valueOf(this.bean.getCluster()));
            }

            if (this.bean.isClusterRuntimeSet()) {
               var2.append("ClusterRuntime");
               var2.append(String.valueOf(this.bean.getClusterRuntime()));
            }

            if (this.bean.isClusterWeightSet()) {
               var2.append("ClusterWeight");
               var2.append(String.valueOf(this.bean.getClusterWeight()));
            }

            if (this.bean.isCoherenceClusterSystemResourceSet()) {
               var2.append("CoherenceClusterSystemResource");
               var2.append(String.valueOf(this.bean.getCoherenceClusterSystemResource()));
            }

            if (this.bean.isConsensusProcessIdentifierSet()) {
               var2.append("ConsensusProcessIdentifier");
               var2.append(String.valueOf(this.bean.getConsensusProcessIdentifier()));
            }

            if (this.bean.isCustomIdentityKeyStoreFileNameSet()) {
               var2.append("CustomIdentityKeyStoreFileName");
               var2.append(String.valueOf(this.bean.getCustomIdentityKeyStoreFileName()));
            }

            if (this.bean.isCustomIdentityKeyStorePassPhraseSet()) {
               var2.append("CustomIdentityKeyStorePassPhrase");
               var2.append(String.valueOf(this.bean.getCustomIdentityKeyStorePassPhrase()));
            }

            if (this.bean.isCustomIdentityKeyStorePassPhraseEncryptedSet()) {
               var2.append("CustomIdentityKeyStorePassPhraseEncrypted");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getCustomIdentityKeyStorePassPhraseEncrypted())));
            }

            if (this.bean.isCustomIdentityKeyStoreTypeSet()) {
               var2.append("CustomIdentityKeyStoreType");
               var2.append(String.valueOf(this.bean.getCustomIdentityKeyStoreType()));
            }

            if (this.bean.isCustomTrustKeyStoreFileNameSet()) {
               var2.append("CustomTrustKeyStoreFileName");
               var2.append(String.valueOf(this.bean.getCustomTrustKeyStoreFileName()));
            }

            if (this.bean.isCustomTrustKeyStorePassPhraseSet()) {
               var2.append("CustomTrustKeyStorePassPhrase");
               var2.append(String.valueOf(this.bean.getCustomTrustKeyStorePassPhrase()));
            }

            if (this.bean.isCustomTrustKeyStorePassPhraseEncryptedSet()) {
               var2.append("CustomTrustKeyStorePassPhraseEncrypted");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getCustomTrustKeyStorePassPhraseEncrypted())));
            }

            if (this.bean.isCustomTrustKeyStoreTypeSet()) {
               var2.append("CustomTrustKeyStoreType");
               var2.append(String.valueOf(this.bean.getCustomTrustKeyStoreType()));
            }

            var5 = this.computeChildHashValue(this.bean.getDataSource());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = this.computeChildHashValue(this.bean.getDefaultFileStore());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isDefaultIIOPPasswordSet()) {
               var2.append("DefaultIIOPPassword");
               var2.append(String.valueOf(this.bean.getDefaultIIOPPassword()));
            }

            if (this.bean.isDefaultIIOPPasswordEncryptedSet()) {
               var2.append("DefaultIIOPPasswordEncrypted");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getDefaultIIOPPasswordEncrypted())));
            }

            if (this.bean.isDefaultIIOPUserSet()) {
               var2.append("DefaultIIOPUser");
               var2.append(String.valueOf(this.bean.getDefaultIIOPUser()));
            }

            if (this.bean.isDefaultStagingDirNameSet()) {
               var2.append("DefaultStagingDirName");
               var2.append(String.valueOf(this.bean.getDefaultStagingDirName()));
            }

            if (this.bean.isDefaultTGIOPPasswordSet()) {
               var2.append("DefaultTGIOPPassword");
               var2.append(String.valueOf(this.bean.getDefaultTGIOPPassword()));
            }

            if (this.bean.isDefaultTGIOPPasswordEncryptedSet()) {
               var2.append("DefaultTGIOPPasswordEncrypted");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getDefaultTGIOPPasswordEncrypted())));
            }

            if (this.bean.isDefaultTGIOPUserSet()) {
               var2.append("DefaultTGIOPUser");
               var2.append(String.valueOf(this.bean.getDefaultTGIOPUser()));
            }

            if (this.bean.isDomainLogFilterSet()) {
               var2.append("DomainLogFilter");
               var2.append(String.valueOf(this.bean.getDomainLogFilter()));
            }

            if (this.bean.isExpectedToRunSet()) {
               var2.append("ExpectedToRun");
               var2.append(String.valueOf(this.bean.getExpectedToRun()));
            }

            if (this.bean.isExternalDNSNameSet()) {
               var2.append("ExternalDNSName");
               var2.append(String.valueOf(this.bean.getExternalDNSName()));
            }

            if (this.bean.isExtraEjbcOptionsSet()) {
               var2.append("ExtraEjbcOptions");
               var2.append(String.valueOf(this.bean.getExtraEjbcOptions()));
            }

            if (this.bean.isExtraRmicOptionsSet()) {
               var2.append("ExtraRmicOptions");
               var2.append(String.valueOf(this.bean.getExtraRmicOptions()));
            }

            var5 = this.computeChildHashValue(this.bean.getFederationServices());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isGracefulShutdownTimeoutSet()) {
               var2.append("GracefulShutdownTimeout");
               var2.append(String.valueOf(this.bean.getGracefulShutdownTimeout()));
            }

            if (this.bean.isHealthCheckIntervalSecondsSet()) {
               var2.append("HealthCheckIntervalSeconds");
               var2.append(String.valueOf(this.bean.getHealthCheckIntervalSeconds()));
            }

            if (this.bean.isHealthCheckStartDelaySecondsSet()) {
               var2.append("HealthCheckStartDelaySeconds");
               var2.append(String.valueOf(this.bean.getHealthCheckStartDelaySeconds()));
            }

            if (this.bean.isHealthCheckTimeoutSecondsSet()) {
               var2.append("HealthCheckTimeoutSeconds");
               var2.append(String.valueOf(this.bean.getHealthCheckTimeoutSeconds()));
            }

            if (this.bean.isHostsMigratableServicesSet()) {
               var2.append("HostsMigratableServices");
               var2.append(String.valueOf(this.bean.getHostsMigratableServices()));
            }

            if (this.bean.isIIOPConnectionPoolsSet()) {
               var2.append("IIOPConnectionPools");
               var2.append(String.valueOf(this.bean.getIIOPConnectionPools()));
            }

            if (this.bean.isInterfaceAddressSet()) {
               var2.append("InterfaceAddress");
               var2.append(String.valueOf(this.bean.getInterfaceAddress()));
            }

            if (this.bean.isJDBCLLRTableNameSet()) {
               var2.append("JDBCLLRTableName");
               var2.append(String.valueOf(this.bean.getJDBCLLRTableName()));
            }

            if (this.bean.isJDBCLLRTablePoolColumnSizeSet()) {
               var2.append("JDBCLLRTablePoolColumnSize");
               var2.append(String.valueOf(this.bean.getJDBCLLRTablePoolColumnSize()));
            }

            if (this.bean.isJDBCLLRTableRecordColumnSizeSet()) {
               var2.append("JDBCLLRTableRecordColumnSize");
               var2.append(String.valueOf(this.bean.getJDBCLLRTableRecordColumnSize()));
            }

            if (this.bean.isJDBCLLRTableXIDColumnSizeSet()) {
               var2.append("JDBCLLRTableXIDColumnSize");
               var2.append(String.valueOf(this.bean.getJDBCLLRTableXIDColumnSize()));
            }

            if (this.bean.isJDBCLogFileNameSet()) {
               var2.append("JDBCLogFileName");
               var2.append(String.valueOf(this.bean.getJDBCLogFileName()));
            }

            if (this.bean.isJDBCLoginTimeoutSecondsSet()) {
               var2.append("JDBCLoginTimeoutSeconds");
               var2.append(String.valueOf(this.bean.getJDBCLoginTimeoutSeconds()));
            }

            if (this.bean.isJNDITransportableObjectFactoryListSet()) {
               var2.append("JNDITransportableObjectFactoryList");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getJNDITransportableObjectFactoryList())));
            }

            var5 = this.computeChildHashValue(this.bean.getJTAMigratableTarget());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isJavaCompilerSet()) {
               var2.append("JavaCompiler");
               var2.append(String.valueOf(this.bean.getJavaCompiler()));
            }

            if (this.bean.isJavaCompilerPostClassPathSet()) {
               var2.append("JavaCompilerPostClassPath");
               var2.append(String.valueOf(this.bean.getJavaCompilerPostClassPath()));
            }

            if (this.bean.isJavaCompilerPreClassPathSet()) {
               var2.append("JavaCompilerPreClassPath");
               var2.append(String.valueOf(this.bean.getJavaCompilerPreClassPath()));
            }

            if (this.bean.isJavaStandardTrustKeyStorePassPhraseSet()) {
               var2.append("JavaStandardTrustKeyStorePassPhrase");
               var2.append(String.valueOf(this.bean.getJavaStandardTrustKeyStorePassPhrase()));
            }

            if (this.bean.isJavaStandardTrustKeyStorePassPhraseEncryptedSet()) {
               var2.append("JavaStandardTrustKeyStorePassPhraseEncrypted");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getJavaStandardTrustKeyStorePassPhraseEncrypted())));
            }

            if (this.bean.isKernelDebugSet()) {
               var2.append("KernelDebug");
               var2.append(String.valueOf(this.bean.getKernelDebug()));
            }

            if (this.bean.isKeyStoresSet()) {
               var2.append("KeyStores");
               var2.append(String.valueOf(this.bean.getKeyStores()));
            }

            if (this.bean.isListenAddressSet()) {
               var2.append("ListenAddress");
               var2.append(String.valueOf(this.bean.getListenAddress()));
            }

            if (this.bean.isListenDelaySecsSet()) {
               var2.append("ListenDelaySecs");
               var2.append(String.valueOf(this.bean.getListenDelaySecs()));
            }

            if (this.bean.isListenPortSet()) {
               var2.append("ListenPort");
               var2.append(String.valueOf(this.bean.getListenPort()));
            }

            if (this.bean.isListenThreadStartDelaySecsSet()) {
               var2.append("ListenThreadStartDelaySecs");
               var2.append(String.valueOf(this.bean.getListenThreadStartDelaySecs()));
            }

            if (this.bean.isListenersBindEarlySet()) {
               var2.append("ListenersBindEarly");
               var2.append(String.valueOf(this.bean.getListenersBindEarly()));
            }

            if (this.bean.isLoginTimeoutSet()) {
               var2.append("LoginTimeout");
               var2.append(String.valueOf(this.bean.getLoginTimeout()));
            }

            if (this.bean.isLoginTimeoutMillisSet()) {
               var2.append("LoginTimeoutMillis");
               var2.append(String.valueOf(this.bean.getLoginTimeoutMillis()));
            }

            if (this.bean.isLowMemoryGCThresholdSet()) {
               var2.append("LowMemoryGCThreshold");
               var2.append(String.valueOf(this.bean.getLowMemoryGCThreshold()));
            }

            if (this.bean.isLowMemoryGranularityLevelSet()) {
               var2.append("LowMemoryGranularityLevel");
               var2.append(String.valueOf(this.bean.getLowMemoryGranularityLevel()));
            }

            if (this.bean.isLowMemorySampleSizeSet()) {
               var2.append("LowMemorySampleSize");
               var2.append(String.valueOf(this.bean.getLowMemorySampleSize()));
            }

            if (this.bean.isLowMemoryTimeIntervalSet()) {
               var2.append("LowMemoryTimeInterval");
               var2.append(String.valueOf(this.bean.getLowMemoryTimeInterval()));
            }

            if (this.bean.isMachineSet()) {
               var2.append("Machine");
               var2.append(String.valueOf(this.bean.getMachine()));
            }

            if (this.bean.isMaxBackoffBetweenFailuresSet()) {
               var2.append("MaxBackoffBetweenFailures");
               var2.append(String.valueOf(this.bean.getMaxBackoffBetweenFailures()));
            }

            if (this.bean.isNMSocketCreateTimeoutInMillisSet()) {
               var2.append("NMSocketCreateTimeoutInMillis");
               var2.append(String.valueOf(this.bean.getNMSocketCreateTimeoutInMillis()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            var5 = 0L;

            for(int var7 = 0; var7 < this.bean.getNetworkAccessPoints().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getNetworkAccessPoints()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = this.computeChildHashValue(this.bean.getOverloadProtection());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isPreferredSecondaryGroupSet()) {
               var2.append("PreferredSecondaryGroup");
               var2.append(String.valueOf(this.bean.getPreferredSecondaryGroup()));
            }

            if (this.bean.isReliableDeliveryPolicySet()) {
               var2.append("ReliableDeliveryPolicy");
               var2.append(String.valueOf(this.bean.getReliableDeliveryPolicy()));
            }

            if (this.bean.isReplicationGroupSet()) {
               var2.append("ReplicationGroup");
               var2.append(String.valueOf(this.bean.getReplicationGroup()));
            }

            if (this.bean.isReplicationPortsSet()) {
               var2.append("ReplicationPorts");
               var2.append(String.valueOf(this.bean.getReplicationPorts()));
            }

            if (this.bean.isRestartDelaySecondsSet()) {
               var2.append("RestartDelaySeconds");
               var2.append(String.valueOf(this.bean.getRestartDelaySeconds()));
            }

            if (this.bean.isRestartIntervalSecondsSet()) {
               var2.append("RestartIntervalSeconds");
               var2.append(String.valueOf(this.bean.getRestartIntervalSeconds()));
            }

            if (this.bean.isRestartMaxSet()) {
               var2.append("RestartMax");
               var2.append(String.valueOf(this.bean.getRestartMax()));
            }

            if (this.bean.isRootDirectorySet()) {
               var2.append("RootDirectory");
               var2.append(String.valueOf(this.bean.getRootDirectory()));
            }

            var5 = this.computeChildHashValue(this.bean.getServerDebug());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = this.computeChildHashValue(this.bean.getServerDiagnosticConfig());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isServerLifeCycleTimeoutValSet()) {
               var2.append("ServerLifeCycleTimeoutVal");
               var2.append(String.valueOf(this.bean.getServerLifeCycleTimeoutVal()));
            }

            if (this.bean.isServerNamesSet()) {
               var2.append("ServerNames");
               var2.append(String.valueOf(this.bean.getServerNames()));
            }

            var5 = this.computeChildHashValue(this.bean.getServerStart());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isServerVersionSet()) {
               var2.append("ServerVersion");
               var2.append(String.valueOf(this.bean.getServerVersion()));
            }

            var5 = this.computeChildHashValue(this.bean.getSingleSignOnServices());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isStagingDirectoryNameSet()) {
               var2.append("StagingDirectoryName");
               var2.append(String.valueOf(this.bean.getStagingDirectoryName()));
            }

            if (this.bean.isStagingModeSet()) {
               var2.append("StagingMode");
               var2.append(String.valueOf(this.bean.getStagingMode()));
            }

            if (this.bean.isStartupModeSet()) {
               var2.append("StartupMode");
               var2.append(String.valueOf(this.bean.getStartupMode()));
            }

            if (this.bean.isStartupTimeoutSet()) {
               var2.append("StartupTimeout");
               var2.append(String.valueOf(this.bean.getStartupTimeout()));
            }

            if (this.bean.isStdoutFormatSet()) {
               var2.append("StdoutFormat");
               var2.append(String.valueOf(this.bean.getStdoutFormat()));
            }

            if (this.bean.isStdoutSeverityLevelSet()) {
               var2.append("StdoutSeverityLevel");
               var2.append(String.valueOf(this.bean.getStdoutSeverityLevel()));
            }

            if (this.bean.isSupportedProtocolsSet()) {
               var2.append("SupportedProtocols");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getSupportedProtocols())));
            }

            if (this.bean.isSystemPasswordSet()) {
               var2.append("SystemPassword");
               var2.append(String.valueOf(this.bean.getSystemPassword()));
            }

            if (this.bean.isSystemPasswordEncryptedSet()) {
               var2.append("SystemPasswordEncrypted");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getSystemPasswordEncrypted())));
            }

            if (this.bean.isThreadPoolSizeSet()) {
               var2.append("ThreadPoolSize");
               var2.append(String.valueOf(this.bean.getThreadPoolSize()));
            }

            if (this.bean.isTransactionLogFilePrefixSet()) {
               var2.append("TransactionLogFilePrefix");
               var2.append(String.valueOf(this.bean.getTransactionLogFilePrefix()));
            }

            if (this.bean.isTransactionLogFileWritePolicySet()) {
               var2.append("TransactionLogFileWritePolicy");
               var2.append(String.valueOf(this.bean.getTransactionLogFileWritePolicy()));
            }

            var5 = this.computeChildHashValue(this.bean.getTransactionLogJDBCStore());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isTunnelingClientPingSecsSet()) {
               var2.append("TunnelingClientPingSecs");
               var2.append(String.valueOf(this.bean.getTunnelingClientPingSecs()));
            }

            if (this.bean.isTunnelingClientTimeoutSecsSet()) {
               var2.append("TunnelingClientTimeoutSecs");
               var2.append(String.valueOf(this.bean.getTunnelingClientTimeoutSecs()));
            }

            if (this.bean.isUploadDirectoryNameSet()) {
               var2.append("UploadDirectoryName");
               var2.append(String.valueOf(this.bean.getUploadDirectoryName()));
            }

            if (this.bean.isVerboseEJBDeploymentEnabledSet()) {
               var2.append("VerboseEJBDeploymentEnabled");
               var2.append(String.valueOf(this.bean.getVerboseEJBDeploymentEnabled()));
            }

            if (this.bean.isVirtualMachineNameSet()) {
               var2.append("VirtualMachineName");
               var2.append(String.valueOf(this.bean.getVirtualMachineName()));
            }

            var5 = this.computeChildHashValue(this.bean.getWebServer());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = this.computeChildHashValue(this.bean.getWebService());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isXMLEntityCacheSet()) {
               var2.append("XMLEntityCache");
               var2.append(String.valueOf(this.bean.getXMLEntityCache()));
            }

            if (this.bean.isXMLRegistrySet()) {
               var2.append("XMLRegistry");
               var2.append(String.valueOf(this.bean.getXMLRegistry()));
            }

            if (this.bean.isAdministrationPortEnabledSet()) {
               var2.append("AdministrationPortEnabled");
               var2.append(String.valueOf(this.bean.isAdministrationPortEnabled()));
            }

            if (this.bean.isAutoMigrationEnabledSet()) {
               var2.append("AutoMigrationEnabled");
               var2.append(String.valueOf(this.bean.isAutoMigrationEnabled()));
            }

            if (this.bean.isCOMEnabledSet()) {
               var2.append("COMEnabled");
               var2.append(String.valueOf(this.bean.isCOMEnabled()));
            }

            if (this.bean.isClasspathServletDisabledSet()) {
               var2.append("ClasspathServletDisabled");
               var2.append(String.valueOf(this.bean.isClasspathServletDisabled()));
            }

            if (this.bean.isClientCertProxyEnabledSet()) {
               var2.append("ClientCertProxyEnabled");
               var2.append(String.valueOf(this.bean.isClientCertProxyEnabled()));
            }

            if (this.bean.isConsoleInputEnabledSet()) {
               var2.append("ConsoleInputEnabled");
               var2.append(String.valueOf(this.bean.isConsoleInputEnabled()));
            }

            if (this.bean.isDefaultInternalServletsDisabledSet()) {
               var2.append("DefaultInternalServletsDisabled");
               var2.append(String.valueOf(this.bean.isDefaultInternalServletsDisabled()));
            }

            if (this.bean.isEnabledForDomainLogSet()) {
               var2.append("EnabledForDomainLog");
               var2.append(String.valueOf(this.bean.isEnabledForDomainLog()));
            }

            if (this.bean.isHttpTraceSupportEnabledSet()) {
               var2.append("HttpTraceSupportEnabled");
               var2.append(String.valueOf(this.bean.isHttpTraceSupportEnabled()));
            }

            if (this.bean.isHttpdEnabledSet()) {
               var2.append("HttpdEnabled");
               var2.append(String.valueOf(this.bean.isHttpdEnabled()));
            }

            if (this.bean.isIIOPEnabledSet()) {
               var2.append("IIOPEnabled");
               var2.append(String.valueOf(this.bean.isIIOPEnabled()));
            }

            if (this.bean.isIgnoreSessionsDuringShutdownSet()) {
               var2.append("IgnoreSessionsDuringShutdown");
               var2.append(String.valueOf(this.bean.isIgnoreSessionsDuringShutdown()));
            }

            if (this.bean.isJ2EE12OnlyModeEnabledSet()) {
               var2.append("J2EE12OnlyModeEnabled");
               var2.append(String.valueOf(this.bean.isJ2EE12OnlyModeEnabled()));
            }

            if (this.bean.isJ2EE13WarningEnabledSet()) {
               var2.append("J2EE13WarningEnabled");
               var2.append(String.valueOf(this.bean.isJ2EE13WarningEnabled()));
            }

            if (this.bean.isJDBCLoggingEnabledSet()) {
               var2.append("JDBCLoggingEnabled");
               var2.append(String.valueOf(this.bean.isJDBCLoggingEnabled()));
            }

            if (this.bean.isJMSDefaultConnectionFactoriesEnabledSet()) {
               var2.append("JMSDefaultConnectionFactoriesEnabled");
               var2.append(String.valueOf(this.bean.isJMSDefaultConnectionFactoriesEnabled()));
            }

            if (this.bean.isJRMPEnabledSet()) {
               var2.append("JRMPEnabled");
               var2.append(String.valueOf(this.bean.isJRMPEnabled()));
            }

            if (this.bean.isListenPortEnabledSet()) {
               var2.append("ListenPortEnabled");
               var2.append(String.valueOf(this.bean.isListenPortEnabled()));
            }

            if (this.bean.isMSIFileReplicationEnabledSet()) {
               var2.append("MSIFileReplicationEnabled");
               var2.append(String.valueOf(this.bean.isMSIFileReplicationEnabled()));
            }

            if (this.bean.isManagedServerIndependenceEnabledSet()) {
               var2.append("ManagedServerIndependenceEnabled");
               var2.append(String.valueOf(this.bean.isManagedServerIndependenceEnabled()));
            }

            if (this.bean.isMessageIdPrefixEnabledSet()) {
               var2.append("MessageIdPrefixEnabled");
               var2.append(String.valueOf(this.bean.isMessageIdPrefixEnabled()));
            }

            if (this.bean.isNetworkClassLoadingEnabledSet()) {
               var2.append("NetworkClassLoadingEnabled");
               var2.append(String.valueOf(this.bean.isNetworkClassLoadingEnabled()));
            }

            if (this.bean.isStdoutDebugEnabledSet()) {
               var2.append("StdoutDebugEnabled");
               var2.append(String.valueOf(this.bean.isStdoutDebugEnabled()));
            }

            if (this.bean.isStdoutEnabledSet()) {
               var2.append("StdoutEnabled");
               var2.append(String.valueOf(this.bean.isStdoutEnabled()));
            }

            if (this.bean.isStdoutLogStackSet()) {
               var2.append("StdoutLogStack");
               var2.append(String.valueOf(this.bean.isStdoutLogStack()));
            }

            if (this.bean.isTGIOPEnabledSet()) {
               var2.append("TGIOPEnabled");
               var2.append(String.valueOf(this.bean.isTGIOPEnabled()));
            }

            if (this.bean.isTunnelingEnabledSet()) {
               var2.append("TunnelingEnabled");
               var2.append(String.valueOf(this.bean.isTunnelingEnabled()));
            }

            if (this.bean.isUseFusionForLLRSet()) {
               var2.append("UseFusionForLLR");
               var2.append(String.valueOf(this.bean.isUseFusionForLLR()));
            }

            if (this.bean.isWeblogicPluginEnabledSet()) {
               var2.append("WeblogicPluginEnabled");
               var2.append(String.valueOf(this.bean.isWeblogicPluginEnabled()));
            }

            var1.update(var2.toString().getBytes());
            return var1.getValue();
         } catch (Exception var8) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var8);
         }
      }

      protected void computeDiff(AbstractDescriptorBean var1) {
         try {
            super.computeDiff(var1);
            ServerMBeanImpl var2 = (ServerMBeanImpl)var1;
            this.computeDiff("AcceptBacklog", this.bean.getAcceptBacklog(), var2.getAcceptBacklog(), true);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("ActiveDirectoryName", this.bean.getActiveDirectoryName(), var2.getActiveDirectoryName(), false);
            }

            this.computeDiff("AdminReconnectIntervalSeconds", this.bean.getAdminReconnectIntervalSeconds(), var2.getAdminReconnectIntervalSeconds(), true);
            this.computeDiff("AdministrationPort", this.bean.getAdministrationPort(), var2.getAdministrationPort(), true);
            this.computeDiff("AutoJDBCConnectionClose", this.bean.getAutoJDBCConnectionClose(), var2.getAutoJDBCConnectionClose(), false);
            this.computeDiff("AutoKillIfFailed", this.bean.getAutoKillIfFailed(), var2.getAutoKillIfFailed(), true);
            this.computeDiff("AutoRestart", this.bean.getAutoRestart(), var2.getAutoRestart(), true);
            this.computeSubDiff("COM", this.bean.getCOM(), var2.getCOM());
            this.computeDiff("CandidateMachines", this.bean.getCandidateMachines(), var2.getCandidateMachines(), false, true);
            this.computeDiff("Cluster", this.bean.getCluster(), var2.getCluster(), false);
            this.computeDiff("ClusterWeight", this.bean.getClusterWeight(), var2.getClusterWeight(), false);
            this.computeDiff("CoherenceClusterSystemResource", this.bean.getCoherenceClusterSystemResource(), var2.getCoherenceClusterSystemResource(), false);
            this.computeDiff("ConsensusProcessIdentifier", this.bean.getConsensusProcessIdentifier(), var2.getConsensusProcessIdentifier(), false);
            this.computeDiff("CustomIdentityKeyStoreFileName", this.bean.getCustomIdentityKeyStoreFileName(), var2.getCustomIdentityKeyStoreFileName(), true);
            this.computeDiff("CustomIdentityKeyStorePassPhraseEncrypted", this.bean.getCustomIdentityKeyStorePassPhraseEncrypted(), var2.getCustomIdentityKeyStorePassPhraseEncrypted(), true);
            this.computeDiff("CustomIdentityKeyStoreType", this.bean.getCustomIdentityKeyStoreType(), var2.getCustomIdentityKeyStoreType(), true);
            this.computeDiff("CustomTrustKeyStoreFileName", this.bean.getCustomTrustKeyStoreFileName(), var2.getCustomTrustKeyStoreFileName(), true);
            this.computeDiff("CustomTrustKeyStorePassPhraseEncrypted", this.bean.getCustomTrustKeyStorePassPhraseEncrypted(), var2.getCustomTrustKeyStorePassPhraseEncrypted(), true);
            this.computeDiff("CustomTrustKeyStoreType", this.bean.getCustomTrustKeyStoreType(), var2.getCustomTrustKeyStoreType(), true);
            this.computeSubDiff("DataSource", this.bean.getDataSource(), var2.getDataSource());
            this.computeSubDiff("DefaultFileStore", this.bean.getDefaultFileStore(), var2.getDefaultFileStore());
            this.computeDiff("DefaultIIOPPasswordEncrypted", this.bean.getDefaultIIOPPasswordEncrypted(), var2.getDefaultIIOPPasswordEncrypted(), false);
            this.computeDiff("DefaultIIOPUser", this.bean.getDefaultIIOPUser(), var2.getDefaultIIOPUser(), false);
            this.computeDiff("DefaultTGIOPPasswordEncrypted", this.bean.getDefaultTGIOPPasswordEncrypted(), var2.getDefaultTGIOPPasswordEncrypted(), false);
            this.computeDiff("DefaultTGIOPUser", this.bean.getDefaultTGIOPUser(), var2.getDefaultTGIOPUser(), false);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("DomainLogFilter", this.bean.getDomainLogFilter(), var2.getDomainLogFilter(), true);
            }

            this.computeDiff("ExternalDNSName", this.bean.getExternalDNSName(), var2.getExternalDNSName(), false);
            this.computeDiff("ExtraEjbcOptions", this.bean.getExtraEjbcOptions(), var2.getExtraEjbcOptions(), false);
            this.computeDiff("ExtraRmicOptions", this.bean.getExtraRmicOptions(), var2.getExtraRmicOptions(), false);
            this.computeSubDiff("FederationServices", this.bean.getFederationServices(), var2.getFederationServices());
            this.computeDiff("GracefulShutdownTimeout", this.bean.getGracefulShutdownTimeout(), var2.getGracefulShutdownTimeout(), true);
            this.computeDiff("HealthCheckIntervalSeconds", this.bean.getHealthCheckIntervalSeconds(), var2.getHealthCheckIntervalSeconds(), true);
            this.computeDiff("HealthCheckStartDelaySeconds", this.bean.getHealthCheckStartDelaySeconds(), var2.getHealthCheckStartDelaySeconds(), true);
            this.computeDiff("HealthCheckTimeoutSeconds", this.bean.getHealthCheckTimeoutSeconds(), var2.getHealthCheckTimeoutSeconds(), true);
            this.computeDiff("HostsMigratableServices", this.bean.getHostsMigratableServices(), var2.getHostsMigratableServices(), false);
            this.computeDiff("IIOPConnectionPools", this.bean.getIIOPConnectionPools(), var2.getIIOPConnectionPools(), false);
            this.computeDiff("InterfaceAddress", this.bean.getInterfaceAddress(), var2.getInterfaceAddress(), false);
            this.computeDiff("JDBCLLRTableName", this.bean.getJDBCLLRTableName(), var2.getJDBCLLRTableName(), false);
            this.computeDiff("JDBCLLRTablePoolColumnSize", this.bean.getJDBCLLRTablePoolColumnSize(), var2.getJDBCLLRTablePoolColumnSize(), false);
            this.computeDiff("JDBCLLRTableRecordColumnSize", this.bean.getJDBCLLRTableRecordColumnSize(), var2.getJDBCLLRTableRecordColumnSize(), false);
            this.computeDiff("JDBCLLRTableXIDColumnSize", this.bean.getJDBCLLRTableXIDColumnSize(), var2.getJDBCLLRTableXIDColumnSize(), false);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("JDBCLogFileName", this.bean.getJDBCLogFileName(), var2.getJDBCLogFileName(), false);
            }

            this.computeDiff("JDBCLoginTimeoutSeconds", this.bean.getJDBCLoginTimeoutSeconds(), var2.getJDBCLoginTimeoutSeconds(), false);
            this.computeDiff("JNDITransportableObjectFactoryList", this.bean.getJNDITransportableObjectFactoryList(), var2.getJNDITransportableObjectFactoryList(), false);
            this.computeChildDiff("JTAMigratableTarget", this.bean.getJTAMigratableTarget(), var2.getJTAMigratableTarget(), false);
            this.computeDiff("JavaCompiler", this.bean.getJavaCompiler(), var2.getJavaCompiler(), true);
            this.computeDiff("JavaCompilerPostClassPath", this.bean.getJavaCompilerPostClassPath(), var2.getJavaCompilerPostClassPath(), false);
            this.computeDiff("JavaCompilerPreClassPath", this.bean.getJavaCompilerPreClassPath(), var2.getJavaCompilerPreClassPath(), false);
            this.computeDiff("JavaStandardTrustKeyStorePassPhraseEncrypted", this.bean.getJavaStandardTrustKeyStorePassPhraseEncrypted(), var2.getJavaStandardTrustKeyStorePassPhraseEncrypted(), true);
            this.computeDiff("KeyStores", this.bean.getKeyStores(), var2.getKeyStores(), true);
            this.computeDiff("ListenAddress", this.bean.getListenAddress(), var2.getListenAddress(), false);
            this.computeDiff("ListenDelaySecs", this.bean.getListenDelaySecs(), var2.getListenDelaySecs(), false);
            this.computeDiff("ListenPort", this.bean.getListenPort(), var2.getListenPort(), true);
            this.computeDiff("ListenThreadStartDelaySecs", this.bean.getListenThreadStartDelaySecs(), var2.getListenThreadStartDelaySecs(), false);
            this.computeDiff("ListenersBindEarly", this.bean.getListenersBindEarly(), var2.getListenersBindEarly(), false);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("LoginTimeout", this.bean.getLoginTimeout(), var2.getLoginTimeout(), false);
            }

            this.computeDiff("LoginTimeoutMillis", this.bean.getLoginTimeoutMillis(), var2.getLoginTimeoutMillis(), true);
            this.computeDiff("LowMemoryGCThreshold", this.bean.getLowMemoryGCThreshold(), var2.getLowMemoryGCThreshold(), false);
            this.computeDiff("LowMemoryGranularityLevel", this.bean.getLowMemoryGranularityLevel(), var2.getLowMemoryGranularityLevel(), false);
            this.computeDiff("LowMemorySampleSize", this.bean.getLowMemorySampleSize(), var2.getLowMemorySampleSize(), false);
            this.computeDiff("LowMemoryTimeInterval", this.bean.getLowMemoryTimeInterval(), var2.getLowMemoryTimeInterval(), false);
            this.computeDiff("Machine", this.bean.getMachine(), var2.getMachine(), false);
            this.computeDiff("MaxBackoffBetweenFailures", this.bean.getMaxBackoffBetweenFailures(), var2.getMaxBackoffBetweenFailures(), false);
            this.computeDiff("NMSocketCreateTimeoutInMillis", this.bean.getNMSocketCreateTimeoutInMillis(), var2.getNMSocketCreateTimeoutInMillis(), false);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeChildDiff("NetworkAccessPoints", this.bean.getNetworkAccessPoints(), var2.getNetworkAccessPoints(), true);
            this.computeSubDiff("OverloadProtection", this.bean.getOverloadProtection(), var2.getOverloadProtection());
            this.computeDiff("PreferredSecondaryGroup", this.bean.getPreferredSecondaryGroup(), var2.getPreferredSecondaryGroup(), false);
            this.computeDiff("ReliableDeliveryPolicy", this.bean.getReliableDeliveryPolicy(), var2.getReliableDeliveryPolicy(), false);
            this.computeDiff("ReplicationGroup", this.bean.getReplicationGroup(), var2.getReplicationGroup(), false);
            this.computeDiff("ReplicationPorts", this.bean.getReplicationPorts(), var2.getReplicationPorts(), false);
            this.computeDiff("RestartDelaySeconds", this.bean.getRestartDelaySeconds(), var2.getRestartDelaySeconds(), true);
            this.computeDiff("RestartIntervalSeconds", this.bean.getRestartIntervalSeconds(), var2.getRestartIntervalSeconds(), true);
            this.computeDiff("RestartMax", this.bean.getRestartMax(), var2.getRestartMax(), true);
            this.computeSubDiff("ServerDebug", this.bean.getServerDebug(), var2.getServerDebug());
            this.computeSubDiff("ServerDiagnosticConfig", this.bean.getServerDiagnosticConfig(), var2.getServerDiagnosticConfig());
            this.computeDiff("ServerLifeCycleTimeoutVal", this.bean.getServerLifeCycleTimeoutVal(), var2.getServerLifeCycleTimeoutVal(), true);
            this.computeSubDiff("ServerStart", this.bean.getServerStart(), var2.getServerStart());
            this.computeDiff("ServerVersion", this.bean.getServerVersion(), var2.getServerVersion(), false);
            this.computeSubDiff("SingleSignOnServices", this.bean.getSingleSignOnServices(), var2.getSingleSignOnServices());
            this.computeDiff("StagingDirectoryName", this.bean.getStagingDirectoryName(), var2.getStagingDirectoryName(), false);
            this.computeDiff("StagingMode", this.bean.getStagingMode(), var2.getStagingMode(), false);
            this.computeDiff("StartupMode", this.bean.getStartupMode(), var2.getStartupMode(), true);
            this.computeDiff("StartupTimeout", this.bean.getStartupTimeout(), var2.getStartupTimeout(), false);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("StdoutFormat", this.bean.getStdoutFormat(), var2.getStdoutFormat(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("StdoutSeverityLevel", this.bean.getStdoutSeverityLevel(), var2.getStdoutSeverityLevel(), true);
            }

            this.computeDiff("SupportedProtocols", this.bean.getSupportedProtocols(), var2.getSupportedProtocols(), false);
            this.computeDiff("SystemPasswordEncrypted", this.bean.getSystemPasswordEncrypted(), var2.getSystemPasswordEncrypted(), false);
            this.computeDiff("ThreadPoolSize", this.bean.getThreadPoolSize(), var2.getThreadPoolSize(), false);
            this.computeDiff("TransactionLogFilePrefix", this.bean.getTransactionLogFilePrefix(), var2.getTransactionLogFilePrefix(), false);
            this.computeDiff("TransactionLogFileWritePolicy", this.bean.getTransactionLogFileWritePolicy(), var2.getTransactionLogFileWritePolicy(), false);
            this.computeSubDiff("TransactionLogJDBCStore", this.bean.getTransactionLogJDBCStore(), var2.getTransactionLogJDBCStore());
            this.computeDiff("TunnelingClientPingSecs", this.bean.getTunnelingClientPingSecs(), var2.getTunnelingClientPingSecs(), true);
            this.computeDiff("TunnelingClientTimeoutSecs", this.bean.getTunnelingClientTimeoutSecs(), var2.getTunnelingClientTimeoutSecs(), true);
            this.computeDiff("UploadDirectoryName", this.bean.getUploadDirectoryName(), var2.getUploadDirectoryName(), true);
            this.computeDiff("VerboseEJBDeploymentEnabled", this.bean.getVerboseEJBDeploymentEnabled(), var2.getVerboseEJBDeploymentEnabled(), false);
            this.computeDiff("VirtualMachineName", this.bean.getVirtualMachineName(), var2.getVirtualMachineName(), true);
            this.computeSubDiff("WebServer", this.bean.getWebServer(), var2.getWebServer());
            this.computeSubDiff("WebService", this.bean.getWebService(), var2.getWebService());
            this.computeDiff("XMLEntityCache", this.bean.getXMLEntityCache(), var2.getXMLEntityCache(), false);
            this.computeDiff("XMLRegistry", this.bean.getXMLRegistry(), var2.getXMLRegistry(), false);
            this.computeDiff("AdministrationPortEnabled", this.bean.isAdministrationPortEnabled(), var2.isAdministrationPortEnabled(), true);
            this.computeDiff("AutoMigrationEnabled", this.bean.isAutoMigrationEnabled(), var2.isAutoMigrationEnabled(), false);
            this.computeDiff("COMEnabled", this.bean.isCOMEnabled(), var2.isCOMEnabled(), false);
            this.computeDiff("ClasspathServletDisabled", this.bean.isClasspathServletDisabled(), var2.isClasspathServletDisabled(), false);
            this.computeDiff("ClientCertProxyEnabled", this.bean.isClientCertProxyEnabled(), var2.isClientCertProxyEnabled(), false);
            this.computeDiff("ConsoleInputEnabled", this.bean.isConsoleInputEnabled(), var2.isConsoleInputEnabled(), false);
            this.computeDiff("DefaultInternalServletsDisabled", this.bean.isDefaultInternalServletsDisabled(), var2.isDefaultInternalServletsDisabled(), false);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("EnabledForDomainLog", this.bean.isEnabledForDomainLog(), var2.isEnabledForDomainLog(), true);
            }

            this.computeDiff("HttpTraceSupportEnabled", this.bean.isHttpTraceSupportEnabled(), var2.isHttpTraceSupportEnabled(), false);
            this.computeDiff("HttpdEnabled", this.bean.isHttpdEnabled(), var2.isHttpdEnabled(), false);
            this.computeDiff("IIOPEnabled", this.bean.isIIOPEnabled(), var2.isIIOPEnabled(), false);
            this.computeDiff("IgnoreSessionsDuringShutdown", this.bean.isIgnoreSessionsDuringShutdown(), var2.isIgnoreSessionsDuringShutdown(), true);
            this.computeDiff("J2EE12OnlyModeEnabled", this.bean.isJ2EE12OnlyModeEnabled(), var2.isJ2EE12OnlyModeEnabled(), false);
            this.computeDiff("J2EE13WarningEnabled", this.bean.isJ2EE13WarningEnabled(), var2.isJ2EE13WarningEnabled(), false);
            this.computeDiff("JDBCLoggingEnabled", this.bean.isJDBCLoggingEnabled(), var2.isJDBCLoggingEnabled(), false);
            this.computeDiff("JMSDefaultConnectionFactoriesEnabled", this.bean.isJMSDefaultConnectionFactoriesEnabled(), var2.isJMSDefaultConnectionFactoriesEnabled(), true);
            this.computeDiff("JRMPEnabled", this.bean.isJRMPEnabled(), var2.isJRMPEnabled(), false);
            this.computeDiff("ListenPortEnabled", this.bean.isListenPortEnabled(), var2.isListenPortEnabled(), true);
            this.computeDiff("MSIFileReplicationEnabled", this.bean.isMSIFileReplicationEnabled(), var2.isMSIFileReplicationEnabled(), false);
            this.computeDiff("ManagedServerIndependenceEnabled", this.bean.isManagedServerIndependenceEnabled(), var2.isManagedServerIndependenceEnabled(), false);
            this.computeDiff("MessageIdPrefixEnabled", this.bean.isMessageIdPrefixEnabled(), var2.isMessageIdPrefixEnabled(), false);
            this.computeDiff("NetworkClassLoadingEnabled", this.bean.isNetworkClassLoadingEnabled(), var2.isNetworkClassLoadingEnabled(), false);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("StdoutDebugEnabled", this.bean.isStdoutDebugEnabled(), var2.isStdoutDebugEnabled(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("StdoutEnabled", this.bean.isStdoutEnabled(), var2.isStdoutEnabled(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("StdoutLogStack", this.bean.isStdoutLogStack(), var2.isStdoutLogStack(), true);
            }

            this.computeDiff("TGIOPEnabled", this.bean.isTGIOPEnabled(), var2.isTGIOPEnabled(), false);
            this.computeDiff("TunnelingEnabled", this.bean.isTunnelingEnabled(), var2.isTunnelingEnabled(), true);
            this.computeDiff("UseFusionForLLR", this.bean.isUseFusionForLLR(), var2.isUseFusionForLLR(), false);
            this.computeDiff("WeblogicPluginEnabled", this.bean.isWeblogicPluginEnabled(), var2.isWeblogicPluginEnabled(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            ServerMBeanImpl var3 = (ServerMBeanImpl)var1.getSourceBean();
            ServerMBeanImpl var4 = (ServerMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (!var5.equals("81StyleDefaultStagingDirName")) {
                  if (var5.equals("AcceptBacklog")) {
                     var3.setAcceptBacklog(var4.getAcceptBacklog());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 118);
                  } else if (var5.equals("ActiveDirectoryName")) {
                     var3.setActiveDirectoryName(var4.getActiveDirectoryName());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 152);
                  } else if (var5.equals("AdminReconnectIntervalSeconds")) {
                     var3.setAdminReconnectIntervalSeconds(var4.getAdminReconnectIntervalSeconds());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 141);
                  } else if (var5.equals("AdministrationPort")) {
                     var3.setAdministrationPort(var4.getAdministrationPort());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 122);
                  } else if (var5.equals("AutoJDBCConnectionClose")) {
                     var3.setAutoJDBCConnectionClose(var4.getAutoJDBCConnectionClose());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 199);
                  } else if (var5.equals("AutoKillIfFailed")) {
                     var3.setAutoKillIfFailed(var4.getAutoKillIfFailed());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 155);
                  } else if (var5.equals("AutoRestart")) {
                     var3.setAutoRestart(var4.getAutoRestart());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 154);
                  } else if (var5.equals("COM")) {
                     if (var6 == 2) {
                        var3.setCOM((COMMBean)this.createCopy((AbstractDescriptorBean)var4.getCOM()));
                     } else {
                        if (var6 != 3) {
                           throw new AssertionError("Invalid type: " + var6);
                        }

                        var3._destroySingleton("COM", var3.getCOM());
                     }

                     var3._conditionalUnset(var2.isUnsetUpdate(), 106);
                  } else if (var5.equals("CandidateMachines")) {
                     var3.setCandidateMachinesAsString(var4.getCandidateMachinesAsString());
                     this.reorderArrayObjects(var3.getCandidateMachines(), var4.getCandidateMachines());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 190);
                  } else if (var5.equals("Cluster")) {
                     var3.setClusterAsString(var4.getClusterAsString());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 83);
                  } else if (!var5.equals("ClusterRuntime")) {
                     if (var5.equals("ClusterWeight")) {
                        var3.setClusterWeight(var4.getClusterWeight());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 84);
                     } else if (var5.equals("CoherenceClusterSystemResource")) {
                        var3.setCoherenceClusterSystemResourceAsString(var4.getCoherenceClusterSystemResourceAsString());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 207);
                     } else if (var5.equals("ConsensusProcessIdentifier")) {
                        var3.setConsensusProcessIdentifier(var4.getConsensusProcessIdentifier());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 87);
                     } else if (var5.equals("CustomIdentityKeyStoreFileName")) {
                        var3.setCustomIdentityKeyStoreFileName(var4.getCustomIdentityKeyStoreFileName());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 177);
                     } else if (!var5.equals("CustomIdentityKeyStorePassPhrase")) {
                        if (var5.equals("CustomIdentityKeyStorePassPhraseEncrypted")) {
                           var3.setCustomIdentityKeyStorePassPhraseEncrypted(var4.getCustomIdentityKeyStorePassPhraseEncrypted());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 180);
                        } else if (var5.equals("CustomIdentityKeyStoreType")) {
                           var3.setCustomIdentityKeyStoreType(var4.getCustomIdentityKeyStoreType());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 178);
                        } else if (var5.equals("CustomTrustKeyStoreFileName")) {
                           var3.setCustomTrustKeyStoreFileName(var4.getCustomTrustKeyStoreFileName());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 181);
                        } else if (!var5.equals("CustomTrustKeyStorePassPhrase")) {
                           if (var5.equals("CustomTrustKeyStorePassPhraseEncrypted")) {
                              var3.setCustomTrustKeyStorePassPhraseEncrypted(var4.getCustomTrustKeyStorePassPhraseEncrypted());
                              var3._conditionalUnset(var2.isUnsetUpdate(), 184);
                           } else if (var5.equals("CustomTrustKeyStoreType")) {
                              var3.setCustomTrustKeyStoreType(var4.getCustomTrustKeyStoreType());
                              var3._conditionalUnset(var2.isUnsetUpdate(), 182);
                           } else if (var5.equals("DataSource")) {
                              if (var6 == 2) {
                                 var3.setDataSource((DataSourceMBean)this.createCopy((AbstractDescriptorBean)var4.getDataSource()));
                              } else {
                                 if (var6 != 3) {
                                    throw new AssertionError("Invalid type: " + var6);
                                 }

                                 var3._destroySingleton("DataSource", var3.getDataSource());
                              }

                              var3._conditionalUnset(var2.isUnsetUpdate(), 211);
                           } else if (var5.equals("DefaultFileStore")) {
                              if (var6 == 2) {
                                 var3.setDefaultFileStore((DefaultFileStoreMBean)this.createCopy((AbstractDescriptorBean)var4.getDefaultFileStore()));
                              } else {
                                 if (var6 != 3) {
                                    throw new AssertionError("Invalid type: " + var6);
                                 }

                                 var3._destroySingleton("DefaultFileStore", var3.getDefaultFileStore());
                              }

                              var3._conditionalUnset(var2.isUnsetUpdate(), 189);
                           } else if (!var5.equals("DefaultIIOPPassword")) {
                              if (var5.equals("DefaultIIOPPasswordEncrypted")) {
                                 var3.setDefaultIIOPPasswordEncrypted(var4.getDefaultIIOPPasswordEncrypted());
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 99);
                              } else if (var5.equals("DefaultIIOPUser")) {
                                 var3.setDefaultIIOPUser(var4.getDefaultIIOPUser());
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 97);
                              } else if (!var5.equals("DefaultStagingDirName") && !var5.equals("DefaultTGIOPPassword")) {
                                 if (var5.equals("DefaultTGIOPPasswordEncrypted")) {
                                    var3.setDefaultTGIOPPasswordEncrypted(var4.getDefaultTGIOPPasswordEncrypted());
                                    var3._conditionalUnset(var2.isUnsetUpdate(), 103);
                                 } else if (var5.equals("DefaultTGIOPUser")) {
                                    var3.setDefaultTGIOPUser(var4.getDefaultTGIOPUser());
                                    var3._conditionalUnset(var2.isUnsetUpdate(), 101);
                                 } else if (var5.equals("DomainLogFilter")) {
                                    var3.setDomainLogFilterAsString(var4.getDomainLogFilterAsString());
                                    var3._conditionalUnset(var2.isUnsetUpdate(), 137);
                                 } else if (!var5.equals("ExpectedToRun")) {
                                    if (var5.equals("ExternalDNSName")) {
                                       var3.setExternalDNSName(var4.getExternalDNSName());
                                       var3._conditionalUnset(var2.isUnsetUpdate(), 115);
                                    } else if (var5.equals("ExtraEjbcOptions")) {
                                       var3.setExtraEjbcOptions(var4.getExtraEjbcOptions());
                                       var3._conditionalUnset(var2.isUnsetUpdate(), 131);
                                    } else if (var5.equals("ExtraRmicOptions")) {
                                       var3.setExtraRmicOptions(var4.getExtraRmicOptions());
                                       var3._conditionalUnset(var2.isUnsetUpdate(), 130);
                                    } else if (var5.equals("FederationServices")) {
                                       if (var6 == 2) {
                                          var3.setFederationServices((FederationServicesMBean)this.createCopy((AbstractDescriptorBean)var4.getFederationServices()));
                                       } else {
                                          if (var6 != 3) {
                                             throw new AssertionError("Invalid type: " + var6);
                                          }

                                          var3._destroySingleton("FederationServices", var3.getFederationServices());
                                       }

                                       var3._conditionalUnset(var2.isUnsetUpdate(), 203);
                                    } else if (var5.equals("GracefulShutdownTimeout")) {
                                       var3.setGracefulShutdownTimeout(var4.getGracefulShutdownTimeout());
                                       var3._conditionalUnset(var2.isUnsetUpdate(), 168);
                                    } else if (var5.equals("HealthCheckIntervalSeconds")) {
                                       var3.setHealthCheckIntervalSeconds(var4.getHealthCheckIntervalSeconds());
                                       var3._conditionalUnset(var2.isUnsetUpdate(), 158);
                                    } else if (var5.equals("HealthCheckStartDelaySeconds")) {
                                       var3.setHealthCheckStartDelaySeconds(var4.getHealthCheckStartDelaySeconds());
                                       var3._conditionalUnset(var2.isUnsetUpdate(), 160);
                                    } else if (var5.equals("HealthCheckTimeoutSeconds")) {
                                       var3.setHealthCheckTimeoutSeconds(var4.getHealthCheckTimeoutSeconds());
                                       var3._conditionalUnset(var2.isUnsetUpdate(), 159);
                                    } else if (var5.equals("HostsMigratableServices")) {
                                       var3.setHostsMigratableServices(var4.getHostsMigratableServices());
                                       var3._conditionalUnset(var2.isUnsetUpdate(), 174);
                                    } else if (var5.equals("IIOPConnectionPools")) {
                                       var3.setIIOPConnectionPools(var4.getIIOPConnectionPools());
                                       var3._conditionalUnset(var2.isUnsetUpdate(), 124);
                                    } else if (var5.equals("InterfaceAddress")) {
                                       var3.setInterfaceAddress(var4.getInterfaceAddress());
                                       var3._conditionalUnset(var2.isUnsetUpdate(), 116);
                                    } else if (var5.equals("JDBCLLRTableName")) {
                                       var3.setJDBCLLRTableName(var4.getJDBCLLRTableName());
                                       var3._conditionalUnset(var2.isUnsetUpdate(), 192);
                                    } else if (var5.equals("JDBCLLRTablePoolColumnSize")) {
                                       var3.setJDBCLLRTablePoolColumnSize(var4.getJDBCLLRTablePoolColumnSize());
                                       var3._conditionalUnset(var2.isUnsetUpdate(), 195);
                                    } else if (var5.equals("JDBCLLRTableRecordColumnSize")) {
                                       var3.setJDBCLLRTableRecordColumnSize(var4.getJDBCLLRTableRecordColumnSize());
                                       var3._conditionalUnset(var2.isUnsetUpdate(), 196);
                                    } else if (var5.equals("JDBCLLRTableXIDColumnSize")) {
                                       var3.setJDBCLLRTableXIDColumnSize(var4.getJDBCLLRTableXIDColumnSize());
                                       var3._conditionalUnset(var2.isUnsetUpdate(), 194);
                                    } else if (var5.equals("JDBCLogFileName")) {
                                       var3.setJDBCLogFileName(var4.getJDBCLogFileName());
                                       var3._conditionalUnset(var2.isUnsetUpdate(), 93);
                                    } else if (var5.equals("JDBCLoginTimeoutSeconds")) {
                                       var3.setJDBCLoginTimeoutSeconds(var4.getJDBCLoginTimeoutSeconds());
                                       var3._conditionalUnset(var2.isUnsetUpdate(), 197);
                                    } else if (var5.equals("JNDITransportableObjectFactoryList")) {
                                       var3.setJNDITransportableObjectFactoryList(var4.getJNDITransportableObjectFactoryList());
                                       var3._conditionalUnset(var2.isUnsetUpdate(), 123);
                                    } else if (var5.equals("JTAMigratableTarget")) {
                                       if (var6 == 2) {
                                          var3.setJTAMigratableTarget((JTAMigratableTargetMBean)this.createCopy((AbstractDescriptorBean)var4.getJTAMigratableTarget()));
                                       } else {
                                          if (var6 != 3) {
                                             throw new AssertionError("Invalid type: " + var6);
                                          }

                                          var3._destroySingleton("JTAMigratableTarget", var3.getJTAMigratableTarget());
                                       }

                                       var3._conditionalUnset(var2.isUnsetUpdate(), 145);
                                    } else if (var5.equals("JavaCompiler")) {
                                       var3.setJavaCompiler(var4.getJavaCompiler());
                                       var3._conditionalUnset(var2.isUnsetUpdate(), 127);
                                    } else if (var5.equals("JavaCompilerPostClassPath")) {
                                       var3.setJavaCompilerPostClassPath(var4.getJavaCompilerPostClassPath());
                                       var3._conditionalUnset(var2.isUnsetUpdate(), 129);
                                    } else if (var5.equals("JavaCompilerPreClassPath")) {
                                       var3.setJavaCompilerPreClassPath(var4.getJavaCompilerPreClassPath());
                                       var3._conditionalUnset(var2.isUnsetUpdate(), 128);
                                    } else if (!var5.equals("JavaStandardTrustKeyStorePassPhrase")) {
                                       if (var5.equals("JavaStandardTrustKeyStorePassPhraseEncrypted")) {
                                          var3.setJavaStandardTrustKeyStorePassPhraseEncrypted(var4.getJavaStandardTrustKeyStorePassPhraseEncrypted());
                                          var3._conditionalUnset(var2.isUnsetUpdate(), 186);
                                       } else if (!var5.equals("KernelDebug")) {
                                          if (var5.equals("KeyStores")) {
                                             var3.setKeyStores(var4.getKeyStores());
                                             var3._conditionalUnset(var2.isUnsetUpdate(), 176);
                                          } else if (var5.equals("ListenAddress")) {
                                             var3.setListenAddress(var4.getListenAddress());
                                             var3._conditionalUnset(var2.isUnsetUpdate(), 114);
                                          } else if (var5.equals("ListenDelaySecs")) {
                                             var3.setListenDelaySecs(var4.getListenDelaySecs());
                                             var3._conditionalUnset(var2.isUnsetUpdate(), 144);
                                          } else if (var5.equals("ListenPort")) {
                                             var3.setListenPort(var4.getListenPort());
                                             var3._conditionalUnset(var2.isUnsetUpdate(), 80);
                                          } else if (var5.equals("ListenThreadStartDelaySecs")) {
                                             var3.setListenThreadStartDelaySecs(var4.getListenThreadStartDelaySecs());
                                             var3._conditionalUnset(var2.isUnsetUpdate(), 112);
                                          } else if (var5.equals("ListenersBindEarly")) {
                                             var3.setListenersBindEarly(var4.getListenersBindEarly());
                                             var3._conditionalUnset(var2.isUnsetUpdate(), 113);
                                          } else if (var5.equals("LoginTimeout")) {
                                             var3.setLoginTimeout(var4.getLoginTimeout());
                                             var3._conditionalUnset(var2.isUnsetUpdate(), 82);
                                          } else if (var5.equals("LoginTimeoutMillis")) {
                                             var3.setLoginTimeoutMillis(var4.getLoginTimeoutMillis());
                                             var3._conditionalUnset(var2.isUnsetUpdate(), 120);
                                          } else if (var5.equals("LowMemoryGCThreshold")) {
                                             var3.setLowMemoryGCThreshold(var4.getLowMemoryGCThreshold());
                                             var3._conditionalUnset(var2.isUnsetUpdate(), 149);
                                          } else if (var5.equals("LowMemoryGranularityLevel")) {
                                             var3.setLowMemoryGranularityLevel(var4.getLowMemoryGranularityLevel());
                                             var3._conditionalUnset(var2.isUnsetUpdate(), 148);
                                          } else if (var5.equals("LowMemorySampleSize")) {
                                             var3.setLowMemorySampleSize(var4.getLowMemorySampleSize());
                                             var3._conditionalUnset(var2.isUnsetUpdate(), 147);
                                          } else if (var5.equals("LowMemoryTimeInterval")) {
                                             var3.setLowMemoryTimeInterval(var4.getLowMemoryTimeInterval());
                                             var3._conditionalUnset(var2.isUnsetUpdate(), 146);
                                          } else if (var5.equals("Machine")) {
                                             var3.setMachineAsString(var4.getMachineAsString());
                                             var3._conditionalUnset(var2.isUnsetUpdate(), 79);
                                          } else if (var5.equals("MaxBackoffBetweenFailures")) {
                                             var3.setMaxBackoffBetweenFailures(var4.getMaxBackoffBetweenFailures());
                                             var3._conditionalUnset(var2.isUnsetUpdate(), 119);
                                          } else if (var5.equals("NMSocketCreateTimeoutInMillis")) {
                                             var3.setNMSocketCreateTimeoutInMillis(var4.getNMSocketCreateTimeoutInMillis());
                                             var3._conditionalUnset(var2.isUnsetUpdate(), 206);
                                          } else if (var5.equals("Name")) {
                                             var3.setName(var4.getName());
                                             var3._conditionalUnset(var2.isUnsetUpdate(), 2);
                                          } else if (var5.equals("NetworkAccessPoints")) {
                                             if (var6 == 2) {
                                                var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                                var3.addNetworkAccessPoint((NetworkAccessPointMBean)var2.getAddedObject());
                                             } else {
                                                if (var6 != 3) {
                                                   throw new AssertionError("Invalid type: " + var6);
                                                }

                                                var3.removeNetworkAccessPoint((NetworkAccessPointMBean)var2.getRemovedObject());
                                             }

                                             if (var3.getNetworkAccessPoints() == null || var3.getNetworkAccessPoints().length == 0) {
                                                var3._conditionalUnset(var2.isUnsetUpdate(), 117);
                                             }
                                          } else if (var5.equals("OverloadProtection")) {
                                             if (var6 == 2) {
                                                var3.setOverloadProtection((OverloadProtectionMBean)this.createCopy((AbstractDescriptorBean)var4.getOverloadProtection()));
                                             } else {
                                                if (var6 != 3) {
                                                   throw new AssertionError("Invalid type: " + var6);
                                                }

                                                var3._destroySingleton("OverloadProtection", var3.getOverloadProtection());
                                             }

                                             var3._conditionalUnset(var2.isUnsetUpdate(), 191);
                                          } else if (var5.equals("PreferredSecondaryGroup")) {
                                             var3.setPreferredSecondaryGroup(var4.getPreferredSecondaryGroup());
                                             var3._conditionalUnset(var2.isUnsetUpdate(), 86);
                                          } else if (var5.equals("ReliableDeliveryPolicy")) {
                                             var3.setReliableDeliveryPolicyAsString(var4.getReliableDeliveryPolicyAsString());
                                             var3._conditionalUnset(var2.isUnsetUpdate(), 187);
                                          } else if (var5.equals("ReplicationGroup")) {
                                             var3.setReplicationGroup(var4.getReplicationGroup());
                                             var3._conditionalUnset(var2.isUnsetUpdate(), 85);
                                          } else if (var5.equals("ReplicationPorts")) {
                                             var3.setReplicationPorts(var4.getReplicationPorts());
                                             var3._conditionalUnset(var2.isUnsetUpdate(), 209);
                                          } else if (var5.equals("RestartDelaySeconds")) {
                                             var3.setRestartDelaySeconds(var4.getRestartDelaySeconds());
                                             var3._conditionalUnset(var2.isUnsetUpdate(), 161);
                                          } else if (var5.equals("RestartIntervalSeconds")) {
                                             var3.setRestartIntervalSeconds(var4.getRestartIntervalSeconds());
                                             var3._conditionalUnset(var2.isUnsetUpdate(), 156);
                                          } else if (var5.equals("RestartMax")) {
                                             var3.setRestartMax(var4.getRestartMax());
                                             var3._conditionalUnset(var2.isUnsetUpdate(), 157);
                                          } else if (!var5.equals("RootDirectory")) {
                                             if (var5.equals("ServerDebug")) {
                                                if (var6 == 2) {
                                                   var3.setServerDebug((ServerDebugMBean)this.createCopy((AbstractDescriptorBean)var4.getServerDebug()));
                                                } else {
                                                   if (var6 != 3) {
                                                      throw new AssertionError("Invalid type: " + var6);
                                                   }

                                                   var3._destroySingleton("ServerDebug", var3.getServerDebug());
                                                }

                                                var3._conditionalUnset(var2.isUnsetUpdate(), 107);
                                             } else if (var5.equals("ServerDiagnosticConfig")) {
                                                if (var6 == 2) {
                                                   var3.setServerDiagnosticConfig((WLDFServerDiagnosticMBean)this.createCopy((AbstractDescriptorBean)var4.getServerDiagnosticConfig()));
                                                } else {
                                                   if (var6 != 3) {
                                                      throw new AssertionError("Invalid type: " + var6);
                                                   }

                                                   var3._destroySingleton("ServerDiagnosticConfig", var3.getServerDiagnosticConfig());
                                                }

                                                var3._conditionalUnset(var2.isUnsetUpdate(), 198);
                                             } else if (var5.equals("ServerLifeCycleTimeoutVal")) {
                                                var3.setServerLifeCycleTimeoutVal(var4.getServerLifeCycleTimeoutVal());
                                                var3._conditionalUnset(var2.isUnsetUpdate(), 166);
                                             } else if (!var5.equals("ServerNames")) {
                                                if (var5.equals("ServerStart")) {
                                                   if (var6 == 2) {
                                                      var3.setServerStart((ServerStartMBean)this.createCopy((AbstractDescriptorBean)var4.getServerStart()));
                                                   } else {
                                                      if (var6 != 3) {
                                                         throw new AssertionError("Invalid type: " + var6);
                                                      }

                                                      var3._destroySingleton("ServerStart", var3.getServerStart());
                                                   }

                                                   var3._conditionalUnset(var2.isUnsetUpdate(), 143);
                                                } else if (var5.equals("ServerVersion")) {
                                                   var3.setServerVersion(var4.getServerVersion());
                                                   var3._conditionalUnset(var2.isUnsetUpdate(), 164);
                                                } else if (var5.equals("SingleSignOnServices")) {
                                                   if (var6 == 2) {
                                                      var3.setSingleSignOnServices((SingleSignOnServicesMBean)this.createCopy((AbstractDescriptorBean)var4.getSingleSignOnServices()));
                                                   } else {
                                                      if (var6 != 3) {
                                                         throw new AssertionError("Invalid type: " + var6);
                                                      }

                                                      var3._destroySingleton("SingleSignOnServices", var3.getSingleSignOnServices());
                                                   }

                                                   var3._conditionalUnset(var2.isUnsetUpdate(), 204);
                                                } else if (var5.equals("StagingDirectoryName")) {
                                                   var3.setStagingDirectoryName(var4.getStagingDirectoryName());
                                                   var3._conditionalUnset(var2.isUnsetUpdate(), 150);
                                                } else if (var5.equals("StagingMode")) {
                                                   var3.setStagingMode(var4.getStagingMode());
                                                   var3._conditionalUnset(var2.isUnsetUpdate(), 153);
                                                } else if (var5.equals("StartupMode")) {
                                                   var3.setStartupMode(var4.getStartupMode());
                                                   var3._conditionalUnset(var2.isUnsetUpdate(), 165);
                                                } else if (var5.equals("StartupTimeout")) {
                                                   var3.setStartupTimeout(var4.getStartupTimeout());
                                                   var3._conditionalUnset(var2.isUnsetUpdate(), 167);
                                                } else if (var5.equals("StdoutFormat")) {
                                                   var3.setStdoutFormat(var4.getStdoutFormat());
                                                   var3._conditionalUnset(var2.isUnsetUpdate(), 60);
                                                } else if (var5.equals("StdoutSeverityLevel")) {
                                                   var3.setStdoutSeverityLevel(var4.getStdoutSeverityLevel());
                                                   var3._conditionalUnset(var2.isUnsetUpdate(), 54);
                                                } else if (var5.equals("SupportedProtocols")) {
                                                   var3._conditionalUnset(var2.isUnsetUpdate(), 200);
                                                } else if (!var5.equals("SystemPassword")) {
                                                   if (var5.equals("SystemPasswordEncrypted")) {
                                                      var3.setSystemPasswordEncrypted(var4.getSystemPasswordEncrypted());
                                                      var3._conditionalUnset(var2.isUnsetUpdate(), 110);
                                                   } else if (var5.equals("ThreadPoolSize")) {
                                                      var3.setThreadPoolSize(var4.getThreadPoolSize());
                                                      var3._conditionalUnset(var2.isUnsetUpdate(), 12);
                                                   } else if (var5.equals("TransactionLogFilePrefix")) {
                                                      var3.setTransactionLogFilePrefix(var4.getTransactionLogFilePrefix());
                                                      var3._conditionalUnset(var2.isUnsetUpdate(), 133);
                                                   } else if (var5.equals("TransactionLogFileWritePolicy")) {
                                                      var3.setTransactionLogFileWritePolicy(var4.getTransactionLogFileWritePolicy());
                                                      var3._conditionalUnset(var2.isUnsetUpdate(), 134);
                                                   } else if (var5.equals("TransactionLogJDBCStore")) {
                                                      if (var6 == 2) {
                                                         var3.setTransactionLogJDBCStore((TransactionLogJDBCStoreMBean)this.createCopy((AbstractDescriptorBean)var4.getTransactionLogJDBCStore()));
                                                      } else {
                                                         if (var6 != 3) {
                                                            throw new AssertionError("Invalid type: " + var6);
                                                         }

                                                         var3._destroySingleton("TransactionLogJDBCStore", var3.getTransactionLogJDBCStore());
                                                      }

                                                      var3._conditionalUnset(var2.isUnsetUpdate(), 210);
                                                   } else if (var5.equals("TunnelingClientPingSecs")) {
                                                      var3.setTunnelingClientPingSecs(var4.getTunnelingClientPingSecs());
                                                      var3._conditionalUnset(var2.isUnsetUpdate(), 139);
                                                   } else if (var5.equals("TunnelingClientTimeoutSecs")) {
                                                      var3.setTunnelingClientTimeoutSecs(var4.getTunnelingClientTimeoutSecs());
                                                      var3._conditionalUnset(var2.isUnsetUpdate(), 140);
                                                   } else if (var5.equals("UploadDirectoryName")) {
                                                      var3.setUploadDirectoryName(var4.getUploadDirectoryName());
                                                      var3._conditionalUnset(var2.isUnsetUpdate(), 151);
                                                   } else if (var5.equals("VerboseEJBDeploymentEnabled")) {
                                                      var3.setVerboseEJBDeploymentEnabled(var4.getVerboseEJBDeploymentEnabled());
                                                      var3._conditionalUnset(var2.isUnsetUpdate(), 132);
                                                   } else if (var5.equals("VirtualMachineName")) {
                                                      var3.setVirtualMachineName(var4.getVirtualMachineName());
                                                      var3._conditionalUnset(var2.isUnsetUpdate(), 208);
                                                   } else if (var5.equals("WebServer")) {
                                                      if (var6 == 2) {
                                                         var3.setWebServer((WebServerMBean)this.createCopy((AbstractDescriptorBean)var4.getWebServer()));
                                                      } else {
                                                         if (var6 != 3) {
                                                            throw new AssertionError("Invalid type: " + var6);
                                                         }

                                                         var3._destroySingleton("WebServer", var3.getWebServer());
                                                      }

                                                      var3._conditionalUnset(var2.isUnsetUpdate(), 90);
                                                   } else if (var5.equals("WebService")) {
                                                      if (var6 == 2) {
                                                         var3.setWebService((WebServiceMBean)this.createCopy((AbstractDescriptorBean)var4.getWebService()));
                                                      } else {
                                                         if (var6 != 3) {
                                                            throw new AssertionError("Invalid type: " + var6);
                                                         }

                                                         var3._destroySingleton("WebService", var3.getWebService());
                                                      }

                                                      var3._conditionalUnset(var2.isUnsetUpdate(), 205);
                                                   } else if (var5.equals("XMLEntityCache")) {
                                                      var3.setXMLEntityCacheAsString(var4.getXMLEntityCacheAsString());
                                                      var3._conditionalUnset(var2.isUnsetUpdate(), 126);
                                                   } else if (var5.equals("XMLRegistry")) {
                                                      var3.setXMLRegistryAsString(var4.getXMLRegistryAsString());
                                                      var3._conditionalUnset(var2.isUnsetUpdate(), 125);
                                                   } else if (var5.equals("AdministrationPortEnabled")) {
                                                      var3.setAdministrationPortEnabled(var4.isAdministrationPortEnabled());
                                                      var3._conditionalUnset(var2.isUnsetUpdate(), 121);
                                                   } else if (var5.equals("AutoMigrationEnabled")) {
                                                      var3.setAutoMigrationEnabled(var4.isAutoMigrationEnabled());
                                                      var3._conditionalUnset(var2.isUnsetUpdate(), 88);
                                                   } else if (var5.equals("COMEnabled")) {
                                                      var3.setCOMEnabled(var4.isCOMEnabled());
                                                      var3._conditionalUnset(var2.isUnsetUpdate(), 104);
                                                   } else if (var5.equals("ClasspathServletDisabled")) {
                                                      var3.setClasspathServletDisabled(var4.isClasspathServletDisabled());
                                                      var3._conditionalUnset(var2.isUnsetUpdate(), 162);
                                                   } else if (var5.equals("ClientCertProxyEnabled")) {
                                                      var3.setClientCertProxyEnabled(var4.isClientCertProxyEnabled());
                                                      var3._conditionalUnset(var2.isUnsetUpdate(), 172);
                                                   } else if (var5.equals("ConsoleInputEnabled")) {
                                                      var3.setConsoleInputEnabled(var4.isConsoleInputEnabled());
                                                      var3._conditionalUnset(var2.isUnsetUpdate(), 111);
                                                   } else if (var5.equals("DefaultInternalServletsDisabled")) {
                                                      var3.setDefaultInternalServletsDisabled(var4.isDefaultInternalServletsDisabled());
                                                      var3._conditionalUnset(var2.isUnsetUpdate(), 163);
                                                   } else if (var5.equals("EnabledForDomainLog")) {
                                                      var3.setEnabledForDomainLog(var4.isEnabledForDomainLog());
                                                      var3._conditionalUnset(var2.isUnsetUpdate(), 136);
                                                   } else if (var5.equals("HttpTraceSupportEnabled")) {
                                                      var3.setHttpTraceSupportEnabled(var4.isHttpTraceSupportEnabled());
                                                      var3._conditionalUnset(var2.isUnsetUpdate(), 175);
                                                   } else if (var5.equals("HttpdEnabled")) {
                                                      var3.setHttpdEnabled(var4.isHttpdEnabled());
                                                      var3._conditionalUnset(var2.isUnsetUpdate(), 108);
                                                   } else if (var5.equals("IIOPEnabled")) {
                                                      var3.setIIOPEnabled(var4.isIIOPEnabled());
                                                      var3._conditionalUnset(var2.isUnsetUpdate(), 96);
                                                   } else if (var5.equals("IgnoreSessionsDuringShutdown")) {
                                                      var3.setIgnoreSessionsDuringShutdown(var4.isIgnoreSessionsDuringShutdown());
                                                      var3._conditionalUnset(var2.isUnsetUpdate(), 169);
                                                   } else if (var5.equals("J2EE12OnlyModeEnabled")) {
                                                      var3.setJ2EE12OnlyModeEnabled(var4.isJ2EE12OnlyModeEnabled());
                                                      var3._conditionalUnset(var2.isUnsetUpdate(), 94);
                                                   } else if (var5.equals("J2EE13WarningEnabled")) {
                                                      var3.setJ2EE13WarningEnabled(var4.isJ2EE13WarningEnabled());
                                                      var3._conditionalUnset(var2.isUnsetUpdate(), 95);
                                                   } else if (var5.equals("JDBCLoggingEnabled")) {
                                                      var3.setJDBCLoggingEnabled(var4.isJDBCLoggingEnabled());
                                                      var3._conditionalUnset(var2.isUnsetUpdate(), 92);
                                                   } else if (var5.equals("JMSDefaultConnectionFactoriesEnabled")) {
                                                      var3.setJMSDefaultConnectionFactoriesEnabled(var4.isJMSDefaultConnectionFactoriesEnabled());
                                                      var3._conditionalUnset(var2.isUnsetUpdate(), 142);
                                                   } else if (var5.equals("JRMPEnabled")) {
                                                      var3.setJRMPEnabled(var4.isJRMPEnabled());
                                                      var3._conditionalUnset(var2.isUnsetUpdate(), 105);
                                                   } else if (var5.equals("ListenPortEnabled")) {
                                                      var3.setListenPortEnabled(var4.isListenPortEnabled());
                                                      var3._conditionalUnset(var2.isUnsetUpdate(), 81);
                                                   } else if (var5.equals("MSIFileReplicationEnabled")) {
                                                      var3.setMSIFileReplicationEnabled(var4.isMSIFileReplicationEnabled());
                                                      var3._conditionalUnset(var2.isUnsetUpdate(), 171);
                                                   } else if (var5.equals("ManagedServerIndependenceEnabled")) {
                                                      var3.setManagedServerIndependenceEnabled(var4.isManagedServerIndependenceEnabled());
                                                      var3._conditionalUnset(var2.isUnsetUpdate(), 170);
                                                   } else if (var5.equals("MessageIdPrefixEnabled")) {
                                                      var3.setMessageIdPrefixEnabled(var4.isMessageIdPrefixEnabled());
                                                      var3._conditionalUnset(var2.isUnsetUpdate(), 188);
                                                   } else if (var5.equals("NetworkClassLoadingEnabled")) {
                                                      var3.setNetworkClassLoadingEnabled(var4.isNetworkClassLoadingEnabled());
                                                      var3._conditionalUnset(var2.isUnsetUpdate(), 135);
                                                   } else if (var5.equals("StdoutDebugEnabled")) {
                                                      var3.setStdoutDebugEnabled(var4.isStdoutDebugEnabled());
                                                      var3._conditionalUnset(var2.isUnsetUpdate(), 55);
                                                   } else if (var5.equals("StdoutEnabled")) {
                                                      var3.setStdoutEnabled(var4.isStdoutEnabled());
                                                      var3._conditionalUnset(var2.isUnsetUpdate(), 53);
                                                   } else if (var5.equals("StdoutLogStack")) {
                                                      var3.setStdoutLogStack(var4.isStdoutLogStack());
                                                      var3._conditionalUnset(var2.isUnsetUpdate(), 61);
                                                   } else if (var5.equals("TGIOPEnabled")) {
                                                      var3.setTGIOPEnabled(var4.isTGIOPEnabled());
                                                      var3._conditionalUnset(var2.isUnsetUpdate(), 100);
                                                   } else if (var5.equals("TunnelingEnabled")) {
                                                      var3.setTunnelingEnabled(var4.isTunnelingEnabled());
                                                      var3._conditionalUnset(var2.isUnsetUpdate(), 138);
                                                   } else if (var5.equals("UseFusionForLLR")) {
                                                      var3.setUseFusionForLLR(var4.isUseFusionForLLR());
                                                      var3._conditionalUnset(var2.isUnsetUpdate(), 193);
                                                   } else if (var5.equals("WeblogicPluginEnabled")) {
                                                      var3.setWeblogicPluginEnabled(var4.isWeblogicPluginEnabled());
                                                      var3._conditionalUnset(var2.isUnsetUpdate(), 173);
                                                   } else {
                                                      super.applyPropertyUpdate(var1, var2);
                                                   }
                                                }
                                             }
                                          }
                                       }
                                    }
                                 }
                              }
                           }
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
            ServerMBeanImpl var5 = (ServerMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("AcceptBacklog")) && this.bean.isAcceptBacklogSet()) {
               var5.setAcceptBacklog(this.bean.getAcceptBacklog());
            }

            if (var2 && (var3 == null || !var3.contains("ActiveDirectoryName")) && this.bean.isActiveDirectoryNameSet()) {
               var5.setActiveDirectoryName(this.bean.getActiveDirectoryName());
            }

            if ((var3 == null || !var3.contains("AdminReconnectIntervalSeconds")) && this.bean.isAdminReconnectIntervalSecondsSet()) {
               var5.setAdminReconnectIntervalSeconds(this.bean.getAdminReconnectIntervalSeconds());
            }

            if ((var3 == null || !var3.contains("AdministrationPort")) && this.bean.isAdministrationPortSet()) {
               var5.setAdministrationPort(this.bean.getAdministrationPort());
            }

            if ((var3 == null || !var3.contains("AutoJDBCConnectionClose")) && this.bean.isAutoJDBCConnectionCloseSet()) {
               var5.setAutoJDBCConnectionClose(this.bean.getAutoJDBCConnectionClose());
            }

            if ((var3 == null || !var3.contains("AutoKillIfFailed")) && this.bean.isAutoKillIfFailedSet()) {
               var5.setAutoKillIfFailed(this.bean.getAutoKillIfFailed());
            }

            if ((var3 == null || !var3.contains("AutoRestart")) && this.bean.isAutoRestartSet()) {
               var5.setAutoRestart(this.bean.getAutoRestart());
            }

            if ((var3 == null || !var3.contains("COM")) && this.bean.isCOMSet() && !var5._isSet(106)) {
               COMMBean var4 = this.bean.getCOM();
               var5.setCOM((COMMBean)null);
               var5.setCOM(var4 == null ? null : (COMMBean)this.createCopy((AbstractDescriptorBean)var4, var2));
            }

            if ((var3 == null || !var3.contains("CandidateMachines")) && this.bean.isCandidateMachinesSet()) {
               var5._unSet(var5, 190);
               var5.setCandidateMachinesAsString(this.bean.getCandidateMachinesAsString());
            }

            if ((var3 == null || !var3.contains("Cluster")) && this.bean.isClusterSet()) {
               var5._unSet(var5, 83);
               var5.setClusterAsString(this.bean.getClusterAsString());
            }

            if ((var3 == null || !var3.contains("ClusterWeight")) && this.bean.isClusterWeightSet()) {
               var5.setClusterWeight(this.bean.getClusterWeight());
            }

            if ((var3 == null || !var3.contains("CoherenceClusterSystemResource")) && this.bean.isCoherenceClusterSystemResourceSet()) {
               var5._unSet(var5, 207);
               var5.setCoherenceClusterSystemResourceAsString(this.bean.getCoherenceClusterSystemResourceAsString());
            }

            if ((var3 == null || !var3.contains("ConsensusProcessIdentifier")) && this.bean.isConsensusProcessIdentifierSet()) {
               var5.setConsensusProcessIdentifier(this.bean.getConsensusProcessIdentifier());
            }

            if ((var3 == null || !var3.contains("CustomIdentityKeyStoreFileName")) && this.bean.isCustomIdentityKeyStoreFileNameSet()) {
               var5.setCustomIdentityKeyStoreFileName(this.bean.getCustomIdentityKeyStoreFileName());
            }

            byte[] var11;
            if ((var3 == null || !var3.contains("CustomIdentityKeyStorePassPhraseEncrypted")) && this.bean.isCustomIdentityKeyStorePassPhraseEncryptedSet()) {
               var11 = this.bean.getCustomIdentityKeyStorePassPhraseEncrypted();
               var5.setCustomIdentityKeyStorePassPhraseEncrypted(var11 == null ? null : (byte[])((byte[])((byte[])((byte[])var11)).clone()));
            }

            if ((var3 == null || !var3.contains("CustomIdentityKeyStoreType")) && this.bean.isCustomIdentityKeyStoreTypeSet()) {
               var5.setCustomIdentityKeyStoreType(this.bean.getCustomIdentityKeyStoreType());
            }

            if ((var3 == null || !var3.contains("CustomTrustKeyStoreFileName")) && this.bean.isCustomTrustKeyStoreFileNameSet()) {
               var5.setCustomTrustKeyStoreFileName(this.bean.getCustomTrustKeyStoreFileName());
            }

            if ((var3 == null || !var3.contains("CustomTrustKeyStorePassPhraseEncrypted")) && this.bean.isCustomTrustKeyStorePassPhraseEncryptedSet()) {
               var11 = this.bean.getCustomTrustKeyStorePassPhraseEncrypted();
               var5.setCustomTrustKeyStorePassPhraseEncrypted(var11 == null ? null : (byte[])((byte[])((byte[])((byte[])var11)).clone()));
            }

            if ((var3 == null || !var3.contains("CustomTrustKeyStoreType")) && this.bean.isCustomTrustKeyStoreTypeSet()) {
               var5.setCustomTrustKeyStoreType(this.bean.getCustomTrustKeyStoreType());
            }

            if ((var3 == null || !var3.contains("DataSource")) && this.bean.isDataSourceSet() && !var5._isSet(211)) {
               DataSourceMBean var12 = this.bean.getDataSource();
               var5.setDataSource((DataSourceMBean)null);
               var5.setDataSource(var12 == null ? null : (DataSourceMBean)this.createCopy((AbstractDescriptorBean)var12, var2));
            }

            if ((var3 == null || !var3.contains("DefaultFileStore")) && this.bean.isDefaultFileStoreSet() && !var5._isSet(189)) {
               DefaultFileStoreMBean var13 = this.bean.getDefaultFileStore();
               var5.setDefaultFileStore((DefaultFileStoreMBean)null);
               var5.setDefaultFileStore(var13 == null ? null : (DefaultFileStoreMBean)this.createCopy((AbstractDescriptorBean)var13, var2));
            }

            if ((var3 == null || !var3.contains("DefaultIIOPPasswordEncrypted")) && this.bean.isDefaultIIOPPasswordEncryptedSet()) {
               var11 = this.bean.getDefaultIIOPPasswordEncrypted();
               var5.setDefaultIIOPPasswordEncrypted(var11 == null ? null : (byte[])((byte[])((byte[])((byte[])var11)).clone()));
            }

            if ((var3 == null || !var3.contains("DefaultIIOPUser")) && this.bean.isDefaultIIOPUserSet()) {
               var5.setDefaultIIOPUser(this.bean.getDefaultIIOPUser());
            }

            if ((var3 == null || !var3.contains("DefaultTGIOPPasswordEncrypted")) && this.bean.isDefaultTGIOPPasswordEncryptedSet()) {
               var11 = this.bean.getDefaultTGIOPPasswordEncrypted();
               var5.setDefaultTGIOPPasswordEncrypted(var11 == null ? null : (byte[])((byte[])((byte[])((byte[])var11)).clone()));
            }

            if ((var3 == null || !var3.contains("DefaultTGIOPUser")) && this.bean.isDefaultTGIOPUserSet()) {
               var5.setDefaultTGIOPUser(this.bean.getDefaultTGIOPUser());
            }

            if (var2 && (var3 == null || !var3.contains("DomainLogFilter")) && this.bean.isDomainLogFilterSet()) {
               var5._unSet(var5, 137);
               var5.setDomainLogFilterAsString(this.bean.getDomainLogFilterAsString());
            }

            if ((var3 == null || !var3.contains("ExternalDNSName")) && this.bean.isExternalDNSNameSet()) {
               var5.setExternalDNSName(this.bean.getExternalDNSName());
            }

            if ((var3 == null || !var3.contains("ExtraEjbcOptions")) && this.bean.isExtraEjbcOptionsSet()) {
               var5.setExtraEjbcOptions(this.bean.getExtraEjbcOptions());
            }

            if ((var3 == null || !var3.contains("ExtraRmicOptions")) && this.bean.isExtraRmicOptionsSet()) {
               var5.setExtraRmicOptions(this.bean.getExtraRmicOptions());
            }

            if ((var3 == null || !var3.contains("FederationServices")) && this.bean.isFederationServicesSet() && !var5._isSet(203)) {
               FederationServicesMBean var14 = this.bean.getFederationServices();
               var5.setFederationServices((FederationServicesMBean)null);
               var5.setFederationServices(var14 == null ? null : (FederationServicesMBean)this.createCopy((AbstractDescriptorBean)var14, var2));
            }

            if ((var3 == null || !var3.contains("GracefulShutdownTimeout")) && this.bean.isGracefulShutdownTimeoutSet()) {
               var5.setGracefulShutdownTimeout(this.bean.getGracefulShutdownTimeout());
            }

            if ((var3 == null || !var3.contains("HealthCheckIntervalSeconds")) && this.bean.isHealthCheckIntervalSecondsSet()) {
               var5.setHealthCheckIntervalSeconds(this.bean.getHealthCheckIntervalSeconds());
            }

            if ((var3 == null || !var3.contains("HealthCheckStartDelaySeconds")) && this.bean.isHealthCheckStartDelaySecondsSet()) {
               var5.setHealthCheckStartDelaySeconds(this.bean.getHealthCheckStartDelaySeconds());
            }

            if ((var3 == null || !var3.contains("HealthCheckTimeoutSeconds")) && this.bean.isHealthCheckTimeoutSecondsSet()) {
               var5.setHealthCheckTimeoutSeconds(this.bean.getHealthCheckTimeoutSeconds());
            }

            if ((var3 == null || !var3.contains("HostsMigratableServices")) && this.bean.isHostsMigratableServicesSet()) {
               var5.setHostsMigratableServices(this.bean.getHostsMigratableServices());
            }

            if ((var3 == null || !var3.contains("IIOPConnectionPools")) && this.bean.isIIOPConnectionPoolsSet()) {
               var5.setIIOPConnectionPools(this.bean.getIIOPConnectionPools());
            }

            if ((var3 == null || !var3.contains("InterfaceAddress")) && this.bean.isInterfaceAddressSet()) {
               var5.setInterfaceAddress(this.bean.getInterfaceAddress());
            }

            if ((var3 == null || !var3.contains("JDBCLLRTableName")) && this.bean.isJDBCLLRTableNameSet()) {
               var5.setJDBCLLRTableName(this.bean.getJDBCLLRTableName());
            }

            if ((var3 == null || !var3.contains("JDBCLLRTablePoolColumnSize")) && this.bean.isJDBCLLRTablePoolColumnSizeSet()) {
               var5.setJDBCLLRTablePoolColumnSize(this.bean.getJDBCLLRTablePoolColumnSize());
            }

            if ((var3 == null || !var3.contains("JDBCLLRTableRecordColumnSize")) && this.bean.isJDBCLLRTableRecordColumnSizeSet()) {
               var5.setJDBCLLRTableRecordColumnSize(this.bean.getJDBCLLRTableRecordColumnSize());
            }

            if ((var3 == null || !var3.contains("JDBCLLRTableXIDColumnSize")) && this.bean.isJDBCLLRTableXIDColumnSizeSet()) {
               var5.setJDBCLLRTableXIDColumnSize(this.bean.getJDBCLLRTableXIDColumnSize());
            }

            if (var2 && (var3 == null || !var3.contains("JDBCLogFileName")) && this.bean.isJDBCLogFileNameSet()) {
               var5.setJDBCLogFileName(this.bean.getJDBCLogFileName());
            }

            if ((var3 == null || !var3.contains("JDBCLoginTimeoutSeconds")) && this.bean.isJDBCLoginTimeoutSecondsSet()) {
               var5.setJDBCLoginTimeoutSeconds(this.bean.getJDBCLoginTimeoutSeconds());
            }

            String[] var15;
            if ((var3 == null || !var3.contains("JNDITransportableObjectFactoryList")) && this.bean.isJNDITransportableObjectFactoryListSet()) {
               var15 = this.bean.getJNDITransportableObjectFactoryList();
               var5.setJNDITransportableObjectFactoryList(var15 == null ? null : (String[])((String[])((String[])((String[])var15)).clone()));
            }

            if ((var3 == null || !var3.contains("JTAMigratableTarget")) && this.bean.isJTAMigratableTargetSet() && !var5._isSet(145)) {
               JTAMigratableTargetMBean var16 = this.bean.getJTAMigratableTarget();
               var5.setJTAMigratableTarget((JTAMigratableTargetMBean)null);
               var5.setJTAMigratableTarget(var16 == null ? null : (JTAMigratableTargetMBean)this.createCopy((AbstractDescriptorBean)var16, var2));
            }

            if ((var3 == null || !var3.contains("JavaCompiler")) && this.bean.isJavaCompilerSet()) {
               var5.setJavaCompiler(this.bean.getJavaCompiler());
            }

            if ((var3 == null || !var3.contains("JavaCompilerPostClassPath")) && this.bean.isJavaCompilerPostClassPathSet()) {
               var5.setJavaCompilerPostClassPath(this.bean.getJavaCompilerPostClassPath());
            }

            if ((var3 == null || !var3.contains("JavaCompilerPreClassPath")) && this.bean.isJavaCompilerPreClassPathSet()) {
               var5.setJavaCompilerPreClassPath(this.bean.getJavaCompilerPreClassPath());
            }

            if ((var3 == null || !var3.contains("JavaStandardTrustKeyStorePassPhraseEncrypted")) && this.bean.isJavaStandardTrustKeyStorePassPhraseEncryptedSet()) {
               var11 = this.bean.getJavaStandardTrustKeyStorePassPhraseEncrypted();
               var5.setJavaStandardTrustKeyStorePassPhraseEncrypted(var11 == null ? null : (byte[])((byte[])((byte[])((byte[])var11)).clone()));
            }

            if ((var3 == null || !var3.contains("KeyStores")) && this.bean.isKeyStoresSet()) {
               var5.setKeyStores(this.bean.getKeyStores());
            }

            if ((var3 == null || !var3.contains("ListenAddress")) && this.bean.isListenAddressSet()) {
               var5.setListenAddress(this.bean.getListenAddress());
            }

            if ((var3 == null || !var3.contains("ListenDelaySecs")) && this.bean.isListenDelaySecsSet()) {
               var5.setListenDelaySecs(this.bean.getListenDelaySecs());
            }

            if ((var3 == null || !var3.contains("ListenPort")) && this.bean.isListenPortSet()) {
               var5.setListenPort(this.bean.getListenPort());
            }

            if ((var3 == null || !var3.contains("ListenThreadStartDelaySecs")) && this.bean.isListenThreadStartDelaySecsSet()) {
               var5.setListenThreadStartDelaySecs(this.bean.getListenThreadStartDelaySecs());
            }

            if ((var3 == null || !var3.contains("ListenersBindEarly")) && this.bean.isListenersBindEarlySet()) {
               var5.setListenersBindEarly(this.bean.getListenersBindEarly());
            }

            if (var2 && (var3 == null || !var3.contains("LoginTimeout")) && this.bean.isLoginTimeoutSet()) {
               var5.setLoginTimeout(this.bean.getLoginTimeout());
            }

            if ((var3 == null || !var3.contains("LoginTimeoutMillis")) && this.bean.isLoginTimeoutMillisSet()) {
               var5.setLoginTimeoutMillis(this.bean.getLoginTimeoutMillis());
            }

            if ((var3 == null || !var3.contains("LowMemoryGCThreshold")) && this.bean.isLowMemoryGCThresholdSet()) {
               var5.setLowMemoryGCThreshold(this.bean.getLowMemoryGCThreshold());
            }

            if ((var3 == null || !var3.contains("LowMemoryGranularityLevel")) && this.bean.isLowMemoryGranularityLevelSet()) {
               var5.setLowMemoryGranularityLevel(this.bean.getLowMemoryGranularityLevel());
            }

            if ((var3 == null || !var3.contains("LowMemorySampleSize")) && this.bean.isLowMemorySampleSizeSet()) {
               var5.setLowMemorySampleSize(this.bean.getLowMemorySampleSize());
            }

            if ((var3 == null || !var3.contains("LowMemoryTimeInterval")) && this.bean.isLowMemoryTimeIntervalSet()) {
               var5.setLowMemoryTimeInterval(this.bean.getLowMemoryTimeInterval());
            }

            if ((var3 == null || !var3.contains("Machine")) && this.bean.isMachineSet()) {
               var5._unSet(var5, 79);
               var5.setMachineAsString(this.bean.getMachineAsString());
            }

            if ((var3 == null || !var3.contains("MaxBackoffBetweenFailures")) && this.bean.isMaxBackoffBetweenFailuresSet()) {
               var5.setMaxBackoffBetweenFailures(this.bean.getMaxBackoffBetweenFailures());
            }

            if ((var3 == null || !var3.contains("NMSocketCreateTimeoutInMillis")) && this.bean.isNMSocketCreateTimeoutInMillisSet()) {
               var5.setNMSocketCreateTimeoutInMillis(this.bean.getNMSocketCreateTimeoutInMillis());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("NetworkAccessPoints")) && this.bean.isNetworkAccessPointsSet() && !var5._isSet(117)) {
               NetworkAccessPointMBean[] var6 = this.bean.getNetworkAccessPoints();
               NetworkAccessPointMBean[] var7 = new NetworkAccessPointMBean[var6.length];

               for(int var8 = 0; var8 < var7.length; ++var8) {
                  var7[var8] = (NetworkAccessPointMBean)((NetworkAccessPointMBean)this.createCopy((AbstractDescriptorBean)var6[var8], var2));
               }

               var5.setNetworkAccessPoints(var7);
            }

            if ((var3 == null || !var3.contains("OverloadProtection")) && this.bean.isOverloadProtectionSet() && !var5._isSet(191)) {
               OverloadProtectionMBean var17 = this.bean.getOverloadProtection();
               var5.setOverloadProtection((OverloadProtectionMBean)null);
               var5.setOverloadProtection(var17 == null ? null : (OverloadProtectionMBean)this.createCopy((AbstractDescriptorBean)var17, var2));
            }

            if ((var3 == null || !var3.contains("PreferredSecondaryGroup")) && this.bean.isPreferredSecondaryGroupSet()) {
               var5.setPreferredSecondaryGroup(this.bean.getPreferredSecondaryGroup());
            }

            if ((var3 == null || !var3.contains("ReliableDeliveryPolicy")) && this.bean.isReliableDeliveryPolicySet()) {
               var5._unSet(var5, 187);
               var5.setReliableDeliveryPolicyAsString(this.bean.getReliableDeliveryPolicyAsString());
            }

            if ((var3 == null || !var3.contains("ReplicationGroup")) && this.bean.isReplicationGroupSet()) {
               var5.setReplicationGroup(this.bean.getReplicationGroup());
            }

            if ((var3 == null || !var3.contains("ReplicationPorts")) && this.bean.isReplicationPortsSet()) {
               var5.setReplicationPorts(this.bean.getReplicationPorts());
            }

            if ((var3 == null || !var3.contains("RestartDelaySeconds")) && this.bean.isRestartDelaySecondsSet()) {
               var5.setRestartDelaySeconds(this.bean.getRestartDelaySeconds());
            }

            if ((var3 == null || !var3.contains("RestartIntervalSeconds")) && this.bean.isRestartIntervalSecondsSet()) {
               var5.setRestartIntervalSeconds(this.bean.getRestartIntervalSeconds());
            }

            if ((var3 == null || !var3.contains("RestartMax")) && this.bean.isRestartMaxSet()) {
               var5.setRestartMax(this.bean.getRestartMax());
            }

            if ((var3 == null || !var3.contains("ServerDebug")) && this.bean.isServerDebugSet() && !var5._isSet(107)) {
               ServerDebugMBean var18 = this.bean.getServerDebug();
               var5.setServerDebug((ServerDebugMBean)null);
               var5.setServerDebug(var18 == null ? null : (ServerDebugMBean)this.createCopy((AbstractDescriptorBean)var18, var2));
            }

            if ((var3 == null || !var3.contains("ServerDiagnosticConfig")) && this.bean.isServerDiagnosticConfigSet() && !var5._isSet(198)) {
               WLDFServerDiagnosticMBean var19 = this.bean.getServerDiagnosticConfig();
               var5.setServerDiagnosticConfig((WLDFServerDiagnosticMBean)null);
               var5.setServerDiagnosticConfig(var19 == null ? null : (WLDFServerDiagnosticMBean)this.createCopy((AbstractDescriptorBean)var19, var2));
            }

            if ((var3 == null || !var3.contains("ServerLifeCycleTimeoutVal")) && this.bean.isServerLifeCycleTimeoutValSet()) {
               var5.setServerLifeCycleTimeoutVal(this.bean.getServerLifeCycleTimeoutVal());
            }

            if ((var3 == null || !var3.contains("ServerStart")) && this.bean.isServerStartSet() && !var5._isSet(143)) {
               ServerStartMBean var20 = this.bean.getServerStart();
               var5.setServerStart((ServerStartMBean)null);
               var5.setServerStart(var20 == null ? null : (ServerStartMBean)this.createCopy((AbstractDescriptorBean)var20, var2));
            }

            if ((var3 == null || !var3.contains("ServerVersion")) && this.bean.isServerVersionSet()) {
               var5.setServerVersion(this.bean.getServerVersion());
            }

            if ((var3 == null || !var3.contains("SingleSignOnServices")) && this.bean.isSingleSignOnServicesSet() && !var5._isSet(204)) {
               SingleSignOnServicesMBean var21 = this.bean.getSingleSignOnServices();
               var5.setSingleSignOnServices((SingleSignOnServicesMBean)null);
               var5.setSingleSignOnServices(var21 == null ? null : (SingleSignOnServicesMBean)this.createCopy((AbstractDescriptorBean)var21, var2));
            }

            if ((var3 == null || !var3.contains("StagingDirectoryName")) && this.bean.isStagingDirectoryNameSet()) {
               var5.setStagingDirectoryName(this.bean.getStagingDirectoryName());
            }

            if ((var3 == null || !var3.contains("StagingMode")) && this.bean.isStagingModeSet()) {
               var5.setStagingMode(this.bean.getStagingMode());
            }

            if ((var3 == null || !var3.contains("StartupMode")) && this.bean.isStartupModeSet()) {
               var5.setStartupMode(this.bean.getStartupMode());
            }

            if ((var3 == null || !var3.contains("StartupTimeout")) && this.bean.isStartupTimeoutSet()) {
               var5.setStartupTimeout(this.bean.getStartupTimeout());
            }

            if (var2 && (var3 == null || !var3.contains("StdoutFormat")) && this.bean.isStdoutFormatSet()) {
               var5.setStdoutFormat(this.bean.getStdoutFormat());
            }

            if (var2 && (var3 == null || !var3.contains("StdoutSeverityLevel")) && this.bean.isStdoutSeverityLevelSet()) {
               var5.setStdoutSeverityLevel(this.bean.getStdoutSeverityLevel());
            }

            if ((var3 == null || !var3.contains("SupportedProtocols")) && this.bean.isSupportedProtocolsSet()) {
               var15 = this.bean.getSupportedProtocols();
               var5.setSupportedProtocols(var15 == null ? null : (String[])((String[])((String[])((String[])var15)).clone()));
            }

            if ((var3 == null || !var3.contains("SystemPasswordEncrypted")) && this.bean.isSystemPasswordEncryptedSet()) {
               var11 = this.bean.getSystemPasswordEncrypted();
               var5.setSystemPasswordEncrypted(var11 == null ? null : (byte[])((byte[])((byte[])((byte[])var11)).clone()));
            }

            if ((var3 == null || !var3.contains("ThreadPoolSize")) && this.bean.isThreadPoolSizeSet()) {
               var5.setThreadPoolSize(this.bean.getThreadPoolSize());
            }

            if ((var3 == null || !var3.contains("TransactionLogFilePrefix")) && this.bean.isTransactionLogFilePrefixSet()) {
               var5.setTransactionLogFilePrefix(this.bean.getTransactionLogFilePrefix());
            }

            if ((var3 == null || !var3.contains("TransactionLogFileWritePolicy")) && this.bean.isTransactionLogFileWritePolicySet()) {
               var5.setTransactionLogFileWritePolicy(this.bean.getTransactionLogFileWritePolicy());
            }

            if ((var3 == null || !var3.contains("TransactionLogJDBCStore")) && this.bean.isTransactionLogJDBCStoreSet() && !var5._isSet(210)) {
               TransactionLogJDBCStoreMBean var22 = this.bean.getTransactionLogJDBCStore();
               var5.setTransactionLogJDBCStore((TransactionLogJDBCStoreMBean)null);
               var5.setTransactionLogJDBCStore(var22 == null ? null : (TransactionLogJDBCStoreMBean)this.createCopy((AbstractDescriptorBean)var22, var2));
            }

            if ((var3 == null || !var3.contains("TunnelingClientPingSecs")) && this.bean.isTunnelingClientPingSecsSet()) {
               var5.setTunnelingClientPingSecs(this.bean.getTunnelingClientPingSecs());
            }

            if ((var3 == null || !var3.contains("TunnelingClientTimeoutSecs")) && this.bean.isTunnelingClientTimeoutSecsSet()) {
               var5.setTunnelingClientTimeoutSecs(this.bean.getTunnelingClientTimeoutSecs());
            }

            if ((var3 == null || !var3.contains("UploadDirectoryName")) && this.bean.isUploadDirectoryNameSet()) {
               var5.setUploadDirectoryName(this.bean.getUploadDirectoryName());
            }

            if ((var3 == null || !var3.contains("VerboseEJBDeploymentEnabled")) && this.bean.isVerboseEJBDeploymentEnabledSet()) {
               var5.setVerboseEJBDeploymentEnabled(this.bean.getVerboseEJBDeploymentEnabled());
            }

            if ((var3 == null || !var3.contains("VirtualMachineName")) && this.bean.isVirtualMachineNameSet()) {
               var5.setVirtualMachineName(this.bean.getVirtualMachineName());
            }

            if ((var3 == null || !var3.contains("WebServer")) && this.bean.isWebServerSet() && !var5._isSet(90)) {
               WebServerMBean var23 = this.bean.getWebServer();
               var5.setWebServer((WebServerMBean)null);
               var5.setWebServer(var23 == null ? null : (WebServerMBean)this.createCopy((AbstractDescriptorBean)var23, var2));
            }

            if ((var3 == null || !var3.contains("WebService")) && this.bean.isWebServiceSet() && !var5._isSet(205)) {
               WebServiceMBean var24 = this.bean.getWebService();
               var5.setWebService((WebServiceMBean)null);
               var5.setWebService(var24 == null ? null : (WebServiceMBean)this.createCopy((AbstractDescriptorBean)var24, var2));
            }

            if ((var3 == null || !var3.contains("XMLEntityCache")) && this.bean.isXMLEntityCacheSet()) {
               var5._unSet(var5, 126);
               var5.setXMLEntityCacheAsString(this.bean.getXMLEntityCacheAsString());
            }

            if ((var3 == null || !var3.contains("XMLRegistry")) && this.bean.isXMLRegistrySet()) {
               var5._unSet(var5, 125);
               var5.setXMLRegistryAsString(this.bean.getXMLRegistryAsString());
            }

            if ((var3 == null || !var3.contains("AdministrationPortEnabled")) && this.bean.isAdministrationPortEnabledSet()) {
               var5.setAdministrationPortEnabled(this.bean.isAdministrationPortEnabled());
            }

            if ((var3 == null || !var3.contains("AutoMigrationEnabled")) && this.bean.isAutoMigrationEnabledSet()) {
               var5.setAutoMigrationEnabled(this.bean.isAutoMigrationEnabled());
            }

            if ((var3 == null || !var3.contains("COMEnabled")) && this.bean.isCOMEnabledSet()) {
               var5.setCOMEnabled(this.bean.isCOMEnabled());
            }

            if ((var3 == null || !var3.contains("ClasspathServletDisabled")) && this.bean.isClasspathServletDisabledSet()) {
               var5.setClasspathServletDisabled(this.bean.isClasspathServletDisabled());
            }

            if ((var3 == null || !var3.contains("ClientCertProxyEnabled")) && this.bean.isClientCertProxyEnabledSet()) {
               var5.setClientCertProxyEnabled(this.bean.isClientCertProxyEnabled());
            }

            if ((var3 == null || !var3.contains("ConsoleInputEnabled")) && this.bean.isConsoleInputEnabledSet()) {
               var5.setConsoleInputEnabled(this.bean.isConsoleInputEnabled());
            }

            if ((var3 == null || !var3.contains("DefaultInternalServletsDisabled")) && this.bean.isDefaultInternalServletsDisabledSet()) {
               var5.setDefaultInternalServletsDisabled(this.bean.isDefaultInternalServletsDisabled());
            }

            if (var2 && (var3 == null || !var3.contains("EnabledForDomainLog")) && this.bean.isEnabledForDomainLogSet()) {
               var5.setEnabledForDomainLog(this.bean.isEnabledForDomainLog());
            }

            if ((var3 == null || !var3.contains("HttpTraceSupportEnabled")) && this.bean.isHttpTraceSupportEnabledSet()) {
               var5.setHttpTraceSupportEnabled(this.bean.isHttpTraceSupportEnabled());
            }

            if ((var3 == null || !var3.contains("HttpdEnabled")) && this.bean.isHttpdEnabledSet()) {
               var5.setHttpdEnabled(this.bean.isHttpdEnabled());
            }

            if ((var3 == null || !var3.contains("IIOPEnabled")) && this.bean.isIIOPEnabledSet()) {
               var5.setIIOPEnabled(this.bean.isIIOPEnabled());
            }

            if ((var3 == null || !var3.contains("IgnoreSessionsDuringShutdown")) && this.bean.isIgnoreSessionsDuringShutdownSet()) {
               var5.setIgnoreSessionsDuringShutdown(this.bean.isIgnoreSessionsDuringShutdown());
            }

            if ((var3 == null || !var3.contains("J2EE12OnlyModeEnabled")) && this.bean.isJ2EE12OnlyModeEnabledSet()) {
               var5.setJ2EE12OnlyModeEnabled(this.bean.isJ2EE12OnlyModeEnabled());
            }

            if ((var3 == null || !var3.contains("J2EE13WarningEnabled")) && this.bean.isJ2EE13WarningEnabledSet()) {
               var5.setJ2EE13WarningEnabled(this.bean.isJ2EE13WarningEnabled());
            }

            if ((var3 == null || !var3.contains("JDBCLoggingEnabled")) && this.bean.isJDBCLoggingEnabledSet()) {
               var5.setJDBCLoggingEnabled(this.bean.isJDBCLoggingEnabled());
            }

            if ((var3 == null || !var3.contains("JMSDefaultConnectionFactoriesEnabled")) && this.bean.isJMSDefaultConnectionFactoriesEnabledSet()) {
               var5.setJMSDefaultConnectionFactoriesEnabled(this.bean.isJMSDefaultConnectionFactoriesEnabled());
            }

            if ((var3 == null || !var3.contains("JRMPEnabled")) && this.bean.isJRMPEnabledSet()) {
               var5.setJRMPEnabled(this.bean.isJRMPEnabled());
            }

            if ((var3 == null || !var3.contains("ListenPortEnabled")) && this.bean.isListenPortEnabledSet()) {
               var5.setListenPortEnabled(this.bean.isListenPortEnabled());
            }

            if ((var3 == null || !var3.contains("MSIFileReplicationEnabled")) && this.bean.isMSIFileReplicationEnabledSet()) {
               var5.setMSIFileReplicationEnabled(this.bean.isMSIFileReplicationEnabled());
            }

            if ((var3 == null || !var3.contains("ManagedServerIndependenceEnabled")) && this.bean.isManagedServerIndependenceEnabledSet()) {
               var5.setManagedServerIndependenceEnabled(this.bean.isManagedServerIndependenceEnabled());
            }

            if ((var3 == null || !var3.contains("MessageIdPrefixEnabled")) && this.bean.isMessageIdPrefixEnabledSet()) {
               var5.setMessageIdPrefixEnabled(this.bean.isMessageIdPrefixEnabled());
            }

            if ((var3 == null || !var3.contains("NetworkClassLoadingEnabled")) && this.bean.isNetworkClassLoadingEnabledSet()) {
               var5.setNetworkClassLoadingEnabled(this.bean.isNetworkClassLoadingEnabled());
            }

            if (var2 && (var3 == null || !var3.contains("StdoutDebugEnabled")) && this.bean.isStdoutDebugEnabledSet()) {
               var5.setStdoutDebugEnabled(this.bean.isStdoutDebugEnabled());
            }

            if (var2 && (var3 == null || !var3.contains("StdoutEnabled")) && this.bean.isStdoutEnabledSet()) {
               var5.setStdoutEnabled(this.bean.isStdoutEnabled());
            }

            if (var2 && (var3 == null || !var3.contains("StdoutLogStack")) && this.bean.isStdoutLogStackSet()) {
               var5.setStdoutLogStack(this.bean.isStdoutLogStack());
            }

            if ((var3 == null || !var3.contains("TGIOPEnabled")) && this.bean.isTGIOPEnabledSet()) {
               var5.setTGIOPEnabled(this.bean.isTGIOPEnabled());
            }

            if ((var3 == null || !var3.contains("TunnelingEnabled")) && this.bean.isTunnelingEnabledSet()) {
               var5.setTunnelingEnabled(this.bean.isTunnelingEnabled());
            }

            if ((var3 == null || !var3.contains("UseFusionForLLR")) && this.bean.isUseFusionForLLRSet()) {
               var5.setUseFusionForLLR(this.bean.isUseFusionForLLR());
            }

            if ((var3 == null || !var3.contains("WeblogicPluginEnabled")) && this.bean.isWeblogicPluginEnabledSet()) {
               var5.setWeblogicPluginEnabled(this.bean.isWeblogicPluginEnabled());
            }

            return var5;
         } catch (RuntimeException var9) {
            throw var9;
         } catch (Exception var10) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var10);
         }
      }

      protected void inferSubTree(Class var1, Object var2) {
         super.inferSubTree(var1, var2);
         Object var3 = null;
         this.inferSubTree(this.bean.getCOM(), var1, var2);
         this.inferSubTree(this.bean.getCandidateMachines(), var1, var2);
         this.inferSubTree(this.bean.getCluster(), var1, var2);
         this.inferSubTree(this.bean.getCoherenceClusterSystemResource(), var1, var2);
         this.inferSubTree(this.bean.getDataSource(), var1, var2);
         this.inferSubTree(this.bean.getDefaultFileStore(), var1, var2);
         this.inferSubTree(this.bean.getDomainLogFilter(), var1, var2);
         this.inferSubTree(this.bean.getFederationServices(), var1, var2);
         this.inferSubTree(this.bean.getJTAMigratableTarget(), var1, var2);
         this.inferSubTree(this.bean.getKernelDebug(), var1, var2);
         this.inferSubTree(this.bean.getMachine(), var1, var2);
         this.inferSubTree(this.bean.getNetworkAccessPoints(), var1, var2);
         this.inferSubTree(this.bean.getOverloadProtection(), var1, var2);
         this.inferSubTree(this.bean.getReliableDeliveryPolicy(), var1, var2);
         this.inferSubTree(this.bean.getServerDebug(), var1, var2);
         this.inferSubTree(this.bean.getServerDiagnosticConfig(), var1, var2);
         this.inferSubTree(this.bean.getServerStart(), var1, var2);
         this.inferSubTree(this.bean.getSingleSignOnServices(), var1, var2);
         this.inferSubTree(this.bean.getTransactionLogJDBCStore(), var1, var2);
         this.inferSubTree(this.bean.getWebServer(), var1, var2);
         this.inferSubTree(this.bean.getWebService(), var1, var2);
         this.inferSubTree(this.bean.getXMLEntityCache(), var1, var2);
         this.inferSubTree(this.bean.getXMLRegistry(), var1, var2);
      }
   }
}
