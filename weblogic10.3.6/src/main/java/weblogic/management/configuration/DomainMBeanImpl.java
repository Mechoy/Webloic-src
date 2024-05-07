package weblogic.management.configuration;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.BeanRemoveRejectedException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BootstrapProperties;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.ReferenceManager;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.ManagementException;
import weblogic.management.mbeans.custom.Domain;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.ArrayIterator;
import weblogic.utils.collections.CombinedIterator;

public class DomainMBeanImpl extends ConfigurationMBeanImpl implements DomainMBean, Serializable {
   private boolean _Active;
   private AdminConsoleMBean _AdminConsole;
   private AdminServerMBean _AdminServerMBean;
   private String _AdminServerName;
   private boolean _AdministrationMBeanAuditingEnabled;
   private int _AdministrationPort;
   private boolean _AdministrationPortEnabled;
   private String _AdministrationProtocol;
   private AppDeploymentMBean[] _AppDeployments;
   private ApplicationMBean[] _Applications;
   private int _ArchiveConfigurationCount;
   private boolean _AutoConfigurationSaveEnabled;
   private boolean _AutoDeployForSubmodulesEnabled;
   private BasicRealmMBean[] _BasicRealms;
   private BridgeDestinationMBean[] _BridgeDestinations;
   private CachingRealmMBean[] _CachingRealms;
   private boolean _ClusterConstraintsEnabled;
   private ClusterMBean[] _Clusters;
   private CoherenceClusterSystemResourceMBean[] _CoherenceClusterSystemResources;
   private CoherenceServerMBean[] _CoherenceServers;
   private boolean _ConfigBackupEnabled;
   private String _ConfigurationAuditType;
   private String _ConfigurationVersion;
   private String _ConsoleContextPath;
   private boolean _ConsoleEnabled;
   private String _ConsoleExtensionDirectory;
   private CustomRealmMBean[] _CustomRealms;
   private CustomResourceMBean[] _CustomResources;
   private DeploymentConfigurationMBean _DeploymentConfiguration;
   private DeploymentMBean[] _Deployments;
   private DomainLibraryMBean[] _DomainLibraries;
   private DomainLogFilterMBean[] _DomainLogFilters;
   private String _DomainVersion;
   private EJBContainerMBean _EJBContainer;
   private EmbeddedLDAPMBean _EmbeddedLDAP;
   private ErrorHandlingMBean[] _ErrorHandlings;
   private boolean _ExalogicOptimizationsEnabled;
   private FileRealmMBean[] _FileRealms;
   private FileStoreMBean[] _FileStores;
   private FileT3MBean[] _FileT3s;
   private ForeignJMSConnectionFactoryMBean[] _ForeignJMSConnectionFactories;
   private ForeignJMSDestinationMBean[] _ForeignJMSDestinations;
   private ForeignJMSServerMBean[] _ForeignJMSServers;
   private ForeignJNDIProviderMBean[] _ForeignJNDIProviders;
   private boolean _GuardianEnabled;
   private AppDeploymentMBean[] _InternalAppDeployments;
   private boolean _InternalAppsDeployOnDemandEnabled;
   private LibraryMBean[] _InternalLibraries;
   private JDBCConnectionPoolMBean[] _JDBCConnectionPools;
   private JDBCDataSourceFactoryMBean[] _JDBCDataSourceFactories;
   private JDBCDataSourceMBean[] _JDBCDataSources;
   private JDBCMultiPoolMBean[] _JDBCMultiPools;
   private JDBCStoreMBean[] _JDBCStores;
   private JDBCSystemResourceMBean[] _JDBCSystemResources;
   private JDBCTxDataSourceMBean[] _JDBCTxDataSources;
   private JMSBridgeDestinationMBean[] _JMSBridgeDestinations;
   private JMSConnectionConsumerMBean[] _JMSConnectionConsumers;
   private JMSConnectionFactoryMBean[] _JMSConnectionFactories;
   private JMSDestinationKeyMBean[] _JMSDestinationKeys;
   private JMSDestinationMBean[] _JMSDestinations;
   private JMSDistributedQueueMemberMBean[] _JMSDistributedQueueMembers;
   private JMSDistributedQueueMBean[] _JMSDistributedQueues;
   private JMSDistributedTopicMemberMBean[] _JMSDistributedTopicMembers;
   private JMSDistributedTopicMBean[] _JMSDistributedTopics;
   private JMSFileStoreMBean[] _JMSFileStores;
   private JMSInteropModuleMBean[] _JMSInteropModules;
   private JMSJDBCStoreMBean[] _JMSJDBCStores;
   private JMSQueueMBean[] _JMSQueues;
   private JMSServerMBean[] _JMSServers;
   private JMSSessionPoolMBean[] _JMSSessionPools;
   private JMSStoreMBean[] _JMSStores;
   private JMSSystemResourceMBean[] _JMSSystemResources;
   private JMSTemplateMBean[] _JMSTemplates;
   private JMSTopicMBean[] _JMSTopics;
   private JMXMBean _JMX;
   private JPAMBean _JPA;
   private JTAMBean _JTA;
   private JoltConnectionPoolMBean[] _JoltConnectionPools;
   private LDAPRealmMBean[] _LDAPRealms;
   private long _LastModificationTime;
   private LibraryMBean[] _Libraries;
   private LogMBean _Log;
   private LogFilterMBean[] _LogFilters;
   private MachineMBean[] _Machines;
   private MailSessionMBean[] _MailSessions;
   private MessagingBridgeMBean[] _MessagingBridges;
   private MigratableRMIServiceMBean[] _MigratableRMIServices;
   private MigratableTargetMBean[] _MigratableTargets;
   private boolean _MsgIdPrefixCompatibilityEnabled;
   private NTRealmMBean[] _NTRealms;
   private String _Name;
   private NetworkChannelMBean[] _NetworkChannels;
   private boolean _OCMEnabled;
   private PasswordPolicyMBean[] _PasswordPolicies;
   private PathServiceMBean[] _PathServices;
   private boolean _ProductionModeEnabled;
   private RDBMSRealmMBean[] _RDBMSRealms;
   private RealmMBean[] _Realms;
   private RemoteSAFContextMBean[] _RemoteSAFContexts;
   private RestfulManagementServicesMBean _RestfulManagementServices;
   private String _RootDirectory;
   private SAFAgentMBean[] _SAFAgents;
   private SNMPAgentMBean _SNMPAgent;
   private SNMPAgentDeploymentMBean[] _SNMPAgentDeployments;
   private SNMPAttributeChangeMBean[] _SNMPAttributeChanges;
   private SNMPCounterMonitorMBean[] _SNMPCounterMonitors;
   private SNMPGaugeMonitorMBean[] _SNMPGaugeMonitors;
   private SNMPLogFilterMBean[] _SNMPLogFilters;
   private SNMPProxyMBean[] _SNMPProxies;
   private SNMPStringMonitorMBean[] _SNMPStringMonitors;
   private SNMPTrapDestinationMBean[] _SNMPTrapDestinations;
   private SecurityMBean _Security;
   private SecurityConfigurationMBean _SecurityConfiguration;
   private SelfTuningMBean _SelfTuning;
   private ServerMBean[] _Servers;
   private ShutdownClassMBean[] _ShutdownClasses;
   private SingletonServiceMBean[] _SingletonServices;
   private StartupClassMBean[] _StartupClasses;
   private SystemResourceMBean[] _SystemResources;
   private TargetMBean[] _Targets;
   private UnixRealmMBean[] _UnixRealms;
   private VirtualHostMBean[] _VirtualHosts;
   private WLDFSystemResourceMBean[] _WLDFSystemResources;
   private WLECConnectionPoolMBean[] _WLECConnectionPools;
   private WSReliableDeliveryPolicyMBean[] _WSReliableDeliveryPolicies;
   private WTCServerMBean[] _WTCServers;
   private WebAppContainerMBean _WebAppContainer;
   private WebserviceSecurityMBean[] _WebserviceSecurities;
   private XMLEntityCacheMBean[] _XMLEntityCaches;
   private XMLRegistryMBean[] _XMLRegistries;
   private Domain _customizer;
   private static SchemaHelper2 _schemaHelper;

   public DomainMBeanImpl() {
      this._initializeRootBean(this.getDescriptor());

      try {
         this._customizer = new Domain(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public DomainMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeRootBean(this.getDescriptor());

      try {
         this._customizer = new Domain(this);
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         throw new UndeclaredThrowableException(var4);
      }

      this._initializeProperty(-1);
   }

   public String getDomainVersion() {
      return this._DomainVersion;
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

   public boolean isDomainVersionSet() {
      return this._isSet(7);
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public void setDomainVersion(String var1) {
      var1 = var1 == null ? null : var1.trim();
      DomainValidator.validateVersionString(var1);
      String var2 = this._DomainVersion;
      this._DomainVersion = var1;
      this._postSet(7, var2, var1);
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

   public long getLastModificationTime() {
      return this._LastModificationTime;
   }

   public boolean isLastModificationTimeSet() {
      return this._isSet(8);
   }

   public void setLastModificationTime(long var1) throws InvalidAttributeValueException {
      long var3 = this._LastModificationTime;
      this._LastModificationTime = var1;
      this._postSet(8, var3, var1);
   }

   public boolean isActive() {
      return this._Active;
   }

   public boolean isActiveSet() {
      return this._isSet(9);
   }

   public void setActive(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._Active;
      this._Active = var1;
      this._postSet(9, var2, var1);
   }

   public SecurityConfigurationMBean getSecurityConfiguration() {
      return this._SecurityConfiguration;
   }

   public boolean isSecurityConfigurationSet() {
      return this._isSet(10) || this._isAnythingSet((AbstractDescriptorBean)this.getSecurityConfiguration());
   }

   public void setSecurityConfiguration(SecurityConfigurationMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 10)) {
         this._postCreate(var2);
      }

      SecurityConfigurationMBean var3 = this._SecurityConfiguration;
      this._SecurityConfiguration = var1;
      this._postSet(10, var3, var1);
   }

   public SecurityMBean getSecurity() {
      return this._Security;
   }

   public boolean isSecuritySet() {
      return this._isSet(11) || this._isAnythingSet((AbstractDescriptorBean)this.getSecurity());
   }

   public void setSecurity(SecurityMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 11)) {
         this._postCreate(var2);
      }

      SecurityMBean var3 = this._Security;
      this._Security = var1;
      this._postSet(11, var3, var1);
   }

   public JTAMBean getJTA() {
      return this._JTA;
   }

   public boolean isJTASet() {
      return this._isSet(12) || this._isAnythingSet((AbstractDescriptorBean)this.getJTA());
   }

   public void setJTA(JTAMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 12)) {
         this._postCreate(var2);
      }

      JTAMBean var3 = this._JTA;
      this._JTA = var1;
      this._postSet(12, var3, var1);
   }

   public JPAMBean getJPA() {
      return this._JPA;
   }

   public boolean isJPASet() {
      return this._isSet(13) || this._isAnythingSet((AbstractDescriptorBean)this.getJPA());
   }

   public void setJPA(JPAMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 13)) {
         this._postCreate(var2);
      }

      JPAMBean var3 = this._JPA;
      this._JPA = var1;
      this._postSet(13, var3, var1);
   }

   public DeploymentConfigurationMBean getDeploymentConfiguration() {
      return this._DeploymentConfiguration;
   }

   public boolean isDeploymentConfigurationSet() {
      return this._isSet(14) || this._isAnythingSet((AbstractDescriptorBean)this.getDeploymentConfiguration());
   }

   public void setDeploymentConfiguration(DeploymentConfigurationMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 14)) {
         this._postCreate(var2);
      }

      DeploymentConfigurationMBean var3 = this._DeploymentConfiguration;
      this._DeploymentConfiguration = var1;
      this._postSet(14, var3, var1);
   }

   public void addWTCServer(WTCServerMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 15)) {
         WTCServerMBean[] var2;
         if (this._isSet(15)) {
            var2 = (WTCServerMBean[])((WTCServerMBean[])this._getHelper()._extendArray(this.getWTCServers(), WTCServerMBean.class, var1));
         } else {
            var2 = new WTCServerMBean[]{var1};
         }

         try {
            this.setWTCServers(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public WTCServerMBean[] getWTCServers() {
      return this._WTCServers;
   }

   public boolean isWTCServersSet() {
      return this._isSet(15);
   }

   public void removeWTCServer(WTCServerMBean var1) {
      this.destroyWTCServer(var1);
   }

   public void setWTCServers(WTCServerMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new WTCServerMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 15)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      WTCServerMBean[] var5 = this._WTCServers;
      this._WTCServers = (WTCServerMBean[])var4;
      this._postSet(15, var5, var4);
   }

   public WTCServerMBean createWTCServer(String var1) {
      WTCServerMBeanImpl var2 = new WTCServerMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addWTCServer(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void touch() throws ConfigurationException {
      this._customizer.touch();
   }

   public void destroyWTCServer(WTCServerMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 15);
         WTCServerMBean[] var2 = this.getWTCServers();
         WTCServerMBean[] var3 = (WTCServerMBean[])((WTCServerMBean[])this._getHelper()._removeElement(var2, WTCServerMBean.class, var1));
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
               this.setWTCServers(var3);
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

   public void freezeCurrentValue(String var1) throws AttributeNotFoundException, MBeanException {
      this._customizer.freezeCurrentValue(var1);
   }

   public WTCServerMBean lookupWTCServer(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._WTCServers).iterator();

      WTCServerMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (WTCServerMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void restoreDefaultValue(String var1) throws AttributeNotFoundException {
      this._customizer.restoreDefaultValue(var1);
   }

   public LogMBean getLog() {
      return this._Log;
   }

   public boolean isLogSet() {
      return this._isSet(16) || this._isAnythingSet((AbstractDescriptorBean)this.getLog());
   }

   public void setLog(LogMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 16)) {
         this._postCreate(var2);
      }

      LogMBean var3 = this._Log;
      this._Log = var1;
      this._postSet(16, var3, var1);
   }

   public SNMPAgentMBean getSNMPAgent() {
      return this._SNMPAgent;
   }

   public boolean isSNMPAgentSet() {
      return this._isSet(17) || this._isAnythingSet((AbstractDescriptorBean)this.getSNMPAgent());
   }

   public void setSNMPAgent(SNMPAgentMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 17)) {
         this._postCreate(var2);
      }

      SNMPAgentMBean var3 = this._SNMPAgent;
      this._SNMPAgent = var1;
      this._postSet(17, var3, var1);
   }

   public void addSNMPAgentDeployment(SNMPAgentDeploymentMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 18)) {
         SNMPAgentDeploymentMBean[] var2;
         if (this._isSet(18)) {
            var2 = (SNMPAgentDeploymentMBean[])((SNMPAgentDeploymentMBean[])this._getHelper()._extendArray(this.getSNMPAgentDeployments(), SNMPAgentDeploymentMBean.class, var1));
         } else {
            var2 = new SNMPAgentDeploymentMBean[]{var1};
         }

         try {
            this.setSNMPAgentDeployments(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public SNMPAgentDeploymentMBean[] getSNMPAgentDeployments() {
      return this._SNMPAgentDeployments;
   }

   public boolean isSNMPAgentDeploymentsSet() {
      return this._isSet(18);
   }

   public void removeSNMPAgentDeployment(SNMPAgentDeploymentMBean var1) {
      this.destroySNMPAgentDeployment(var1);
   }

   public void setSNMPAgentDeployments(SNMPAgentDeploymentMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new SNMPAgentDeploymentMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 18)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      SNMPAgentDeploymentMBean[] var5 = this._SNMPAgentDeployments;
      this._SNMPAgentDeployments = (SNMPAgentDeploymentMBean[])var4;
      this._postSet(18, var5, var4);
   }

   public SNMPAgentDeploymentMBean createSNMPAgentDeployment(String var1) {
      SNMPAgentDeploymentMBeanImpl var2 = new SNMPAgentDeploymentMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addSNMPAgentDeployment(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroySNMPAgentDeployment(SNMPAgentDeploymentMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 18);
         SNMPAgentDeploymentMBean[] var2 = this.getSNMPAgentDeployments();
         SNMPAgentDeploymentMBean[] var3 = (SNMPAgentDeploymentMBean[])((SNMPAgentDeploymentMBean[])this._getHelper()._removeElement(var2, SNMPAgentDeploymentMBean.class, var1));
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
               this.setSNMPAgentDeployments(var3);
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

   public SNMPAgentDeploymentMBean lookupSNMPAgentDeployment(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._SNMPAgentDeployments).iterator();

      SNMPAgentDeploymentMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (SNMPAgentDeploymentMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public String getRootDirectory() {
      return this._customizer.getRootDirectory();
   }

   public boolean isRootDirectorySet() {
      return this._isSet(19);
   }

   public void setRootDirectory(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._RootDirectory;
      this._RootDirectory = var1;
      this._postSet(19, var2, var1);
   }

   public void discoverManagedServers() {
      this._customizer.discoverManagedServers();
   }

   public boolean discoverManagedServer(String var1) {
      return this._customizer.discoverManagedServer(var1);
   }

   public Object[] getDisconnectedManagedServers() {
      return this._customizer.getDisconnectedManagedServers();
   }

   public boolean isConsoleEnabled() {
      return this._ConsoleEnabled;
   }

   public boolean isConsoleEnabledSet() {
      return this._isSet(20);
   }

   public void setConsoleEnabled(boolean var1) {
      boolean var2 = this._ConsoleEnabled;
      this._ConsoleEnabled = var1;
      this._postSet(20, var2, var1);
   }

   public String getConsoleContextPath() {
      return this._ConsoleContextPath;
   }

   public boolean isConsoleContextPathSet() {
      return this._isSet(21);
   }

   public void setConsoleContextPath(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ConsoleContextPath;
      this._ConsoleContextPath = var1;
      this._postSet(21, var2, var1);
   }

   public String getConsoleExtensionDirectory() {
      return this._ConsoleExtensionDirectory;
   }

   public boolean isConsoleExtensionDirectorySet() {
      return this._isSet(22);
   }

   public void setConsoleExtensionDirectory(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ConsoleExtensionDirectory;
      this._ConsoleExtensionDirectory = var1;
      this._postSet(22, var2, var1);
   }

   public boolean isAutoConfigurationSaveEnabled() {
      return this._AutoConfigurationSaveEnabled;
   }

   public boolean isAutoConfigurationSaveEnabledSet() {
      return this._isSet(23);
   }

   public void setAutoConfigurationSaveEnabled(boolean var1) {
      boolean var2 = this._AutoConfigurationSaveEnabled;
      this._AutoConfigurationSaveEnabled = var1;
      this._postSet(23, var2, var1);
   }

   public void addServer(ServerMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 24)) {
         ServerMBean[] var2;
         if (this._isSet(24)) {
            var2 = (ServerMBean[])((ServerMBean[])this._getHelper()._extendArray(this.getServers(), ServerMBean.class, var1));
         } else {
            var2 = new ServerMBean[]{var1};
         }

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

   public ServerMBean[] getServers() {
      return this._Servers;
   }

   public boolean isServersSet() {
      return this._isSet(24);
   }

   public void removeServer(ServerMBean var1) {
      this.destroyServer(var1);
   }

   public void setServers(ServerMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new ServerMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 24)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      ServerMBean[] var5 = this._Servers;
      this._Servers = (ServerMBean[])var4;
      this._postSet(24, var5, var4);
   }

   public ServerMBean createServer(String var1) {
      ServerMBeanImpl var2 = new ServerMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addServer(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyServer(ServerMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 24);
         ServerMBean[] var2 = this.getServers();
         ServerMBean[] var3 = (ServerMBean[])((ServerMBean[])this._getHelper()._removeElement(var2, ServerMBean.class, var1));
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
               this.setServers(var3);
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

   public ServerMBean lookupServer(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._Servers).iterator();

      ServerMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (ServerMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addCoherenceServer(CoherenceServerMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 25)) {
         CoherenceServerMBean[] var2;
         if (this._isSet(25)) {
            var2 = (CoherenceServerMBean[])((CoherenceServerMBean[])this._getHelper()._extendArray(this.getCoherenceServers(), CoherenceServerMBean.class, var1));
         } else {
            var2 = new CoherenceServerMBean[]{var1};
         }

         try {
            this.setCoherenceServers(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public CoherenceServerMBean[] getCoherenceServers() {
      return this._CoherenceServers;
   }

   public boolean isCoherenceServersSet() {
      return this._isSet(25);
   }

   public void removeCoherenceServer(CoherenceServerMBean var1) {
      this.destroyCoherenceServer(var1);
   }

   public void setCoherenceServers(CoherenceServerMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new CoherenceServerMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 25)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      CoherenceServerMBean[] var5 = this._CoherenceServers;
      this._CoherenceServers = (CoherenceServerMBean[])var4;
      this._postSet(25, var5, var4);
   }

   public CoherenceServerMBean createCoherenceServer(String var1) {
      CoherenceServerMBeanImpl var2 = new CoherenceServerMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addCoherenceServer(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyCoherenceServer(CoherenceServerMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 25);
         CoherenceServerMBean[] var2 = this.getCoherenceServers();
         CoherenceServerMBean[] var3 = (CoherenceServerMBean[])((CoherenceServerMBean[])this._getHelper()._removeElement(var2, CoherenceServerMBean.class, var1));
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
               this.setCoherenceServers(var3);
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

   public CoherenceServerMBean lookupCoherenceServer(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._CoherenceServers).iterator();

      CoherenceServerMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (CoherenceServerMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addCluster(ClusterMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 26)) {
         ClusterMBean[] var2;
         if (this._isSet(26)) {
            var2 = (ClusterMBean[])((ClusterMBean[])this._getHelper()._extendArray(this.getClusters(), ClusterMBean.class, var1));
         } else {
            var2 = new ClusterMBean[]{var1};
         }

         try {
            this.setClusters(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public ClusterMBean[] getClusters() {
      return this._Clusters;
   }

   public boolean isClustersSet() {
      return this._isSet(26);
   }

   public void removeCluster(ClusterMBean var1) {
      this.destroyCluster(var1);
   }

   public void setClusters(ClusterMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new ClusterMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 26)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      ClusterMBean[] var5 = this._Clusters;
      this._Clusters = (ClusterMBean[])var4;
      this._postSet(26, var5, var4);
   }

   public ClusterMBean createCluster(String var1) {
      ClusterMBeanImpl var2 = new ClusterMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addCluster(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyCluster(ClusterMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 26);
         ClusterMBean[] var2 = this.getClusters();
         ClusterMBean[] var3 = (ClusterMBean[])((ClusterMBean[])this._getHelper()._removeElement(var2, ClusterMBean.class, var1));
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
               this.setClusters(var3);
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

   public ClusterMBean lookupCluster(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._Clusters).iterator();

      ClusterMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (ClusterMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addDeployment(DeploymentMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 27)) {
         DeploymentMBean[] var2 = (DeploymentMBean[])((DeploymentMBean[])this._getHelper()._extendArray(this.getDeployments(), DeploymentMBean.class, var1));

         try {
            this.setDeployments(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public DeploymentMBean[] getDeployments() {
      return this._customizer.getDeployments();
   }

   public boolean isDeploymentsSet() {
      return this._isSet(27);
   }

   public void removeDeployment(DeploymentMBean var1) {
      DeploymentMBean[] var2 = this.getDeployments();
      DeploymentMBean[] var3 = (DeploymentMBean[])((DeploymentMBean[])this._getHelper()._removeElement(var2, DeploymentMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setDeployments(var3);
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            }

            throw new UndeclaredThrowableException(var5);
         }
      }

   }

   public void setDeployments(DeploymentMBean[] var1) throws InvalidAttributeValueException {
      Object var2 = var1 == null ? new DeploymentMBeanImpl[0] : var1;
      this._Deployments = (DeploymentMBean[])var2;
   }

   public void addFileT3(FileT3MBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 28)) {
         FileT3MBean[] var2;
         if (this._isSet(28)) {
            var2 = (FileT3MBean[])((FileT3MBean[])this._getHelper()._extendArray(this.getFileT3s(), FileT3MBean.class, var1));
         } else {
            var2 = new FileT3MBean[]{var1};
         }

         try {
            this.setFileT3s(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public FileT3MBean[] getFileT3s() {
      return this._FileT3s;
   }

   public boolean isFileT3sSet() {
      return this._isSet(28);
   }

   public void removeFileT3(FileT3MBean var1) {
      this.destroyFileT3(var1);
   }

   public void setFileT3s(FileT3MBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new FileT3MBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 28)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      FileT3MBean[] var5 = this._FileT3s;
      this._FileT3s = (FileT3MBean[])var4;
      this._postSet(28, var5, var4);
   }

   public FileT3MBean createFileT3(String var1) {
      FileT3MBeanImpl var2 = new FileT3MBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addFileT3(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyFileT3(FileT3MBean var1) {
      try {
         this._checkIsPotentialChild(var1, 28);
         FileT3MBean[] var2 = this.getFileT3s();
         FileT3MBean[] var3 = (FileT3MBean[])((FileT3MBean[])this._getHelper()._removeElement(var2, FileT3MBean.class, var1));
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
               this.setFileT3s(var3);
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

   public FileT3MBean lookupFileT3(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._FileT3s).iterator();

      FileT3MBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (FileT3MBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addJDBCConnectionPool(JDBCConnectionPoolMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 29)) {
         JDBCConnectionPoolMBean[] var2;
         if (this._isSet(29)) {
            var2 = (JDBCConnectionPoolMBean[])((JDBCConnectionPoolMBean[])this._getHelper()._extendArray(this.getJDBCConnectionPools(), JDBCConnectionPoolMBean.class, var1));
         } else {
            var2 = new JDBCConnectionPoolMBean[]{var1};
         }

         try {
            this.setJDBCConnectionPools(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public JDBCConnectionPoolMBean[] getJDBCConnectionPools() {
      return this._JDBCConnectionPools;
   }

   public boolean isJDBCConnectionPoolsSet() {
      return this._isSet(29);
   }

   public void removeJDBCConnectionPool(JDBCConnectionPoolMBean var1) {
      this.destroyJDBCConnectionPool(var1);
   }

   public void setJDBCConnectionPools(JDBCConnectionPoolMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new JDBCConnectionPoolMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 29)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      JDBCConnectionPoolMBean[] var5 = this._JDBCConnectionPools;
      this._JDBCConnectionPools = (JDBCConnectionPoolMBean[])var4;
      this._postSet(29, var5, var4);
   }

   public JDBCConnectionPoolMBean createJDBCConnectionPool(String var1) {
      JDBCConnectionPoolMBeanImpl var2 = new JDBCConnectionPoolMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addJDBCConnectionPool(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyJDBCConnectionPool(JDBCConnectionPoolMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 29);
         JDBCConnectionPoolMBean[] var2 = this.getJDBCConnectionPools();
         JDBCConnectionPoolMBean[] var3 = (JDBCConnectionPoolMBean[])((JDBCConnectionPoolMBean[])this._getHelper()._removeElement(var2, JDBCConnectionPoolMBean.class, var1));
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
               this.setJDBCConnectionPools(var3);
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

   public JDBCConnectionPoolMBean lookupJDBCConnectionPool(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._JDBCConnectionPools).iterator();

      JDBCConnectionPoolMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (JDBCConnectionPoolMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addJDBCDataSource(JDBCDataSourceMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 30)) {
         JDBCDataSourceMBean[] var2;
         if (this._isSet(30)) {
            var2 = (JDBCDataSourceMBean[])((JDBCDataSourceMBean[])this._getHelper()._extendArray(this.getJDBCDataSources(), JDBCDataSourceMBean.class, var1));
         } else {
            var2 = new JDBCDataSourceMBean[]{var1};
         }

         try {
            this.setJDBCDataSources(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public JDBCDataSourceMBean[] getJDBCDataSources() {
      return this._JDBCDataSources;
   }

   public boolean isJDBCDataSourcesSet() {
      return this._isSet(30);
   }

   public void removeJDBCDataSource(JDBCDataSourceMBean var1) {
      this.destroyJDBCDataSource(var1);
   }

   public void setJDBCDataSources(JDBCDataSourceMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new JDBCDataSourceMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 30)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      JDBCDataSourceMBean[] var5 = this._JDBCDataSources;
      this._JDBCDataSources = (JDBCDataSourceMBean[])var4;
      this._postSet(30, var5, var4);
   }

   public JDBCDataSourceMBean createJDBCDataSource(String var1) {
      JDBCDataSourceMBeanImpl var2 = new JDBCDataSourceMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addJDBCDataSource(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyJDBCDataSource(JDBCDataSourceMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 30);
         JDBCDataSourceMBean[] var2 = this.getJDBCDataSources();
         JDBCDataSourceMBean[] var3 = (JDBCDataSourceMBean[])((JDBCDataSourceMBean[])this._getHelper()._removeElement(var2, JDBCDataSourceMBean.class, var1));
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
               this.setJDBCDataSources(var3);
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

   public JDBCDataSourceMBean lookupJDBCDataSource(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._JDBCDataSources).iterator();

      JDBCDataSourceMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (JDBCDataSourceMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addJDBCTxDataSource(JDBCTxDataSourceMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 31)) {
         JDBCTxDataSourceMBean[] var2;
         if (this._isSet(31)) {
            var2 = (JDBCTxDataSourceMBean[])((JDBCTxDataSourceMBean[])this._getHelper()._extendArray(this.getJDBCTxDataSources(), JDBCTxDataSourceMBean.class, var1));
         } else {
            var2 = new JDBCTxDataSourceMBean[]{var1};
         }

         try {
            this.setJDBCTxDataSources(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public JDBCTxDataSourceMBean[] getJDBCTxDataSources() {
      return this._JDBCTxDataSources;
   }

   public boolean isJDBCTxDataSourcesSet() {
      return this._isSet(31);
   }

   public void removeJDBCTxDataSource(JDBCTxDataSourceMBean var1) {
      this.destroyJDBCTxDataSource(var1);
   }

   public void setJDBCTxDataSources(JDBCTxDataSourceMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new JDBCTxDataSourceMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 31)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      JDBCTxDataSourceMBean[] var5 = this._JDBCTxDataSources;
      this._JDBCTxDataSources = (JDBCTxDataSourceMBean[])var4;
      this._postSet(31, var5, var4);
   }

   public JDBCTxDataSourceMBean createJDBCTxDataSource(String var1) {
      JDBCTxDataSourceMBeanImpl var2 = new JDBCTxDataSourceMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addJDBCTxDataSource(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyJDBCTxDataSource(JDBCTxDataSourceMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 31);
         JDBCTxDataSourceMBean[] var2 = this.getJDBCTxDataSources();
         JDBCTxDataSourceMBean[] var3 = (JDBCTxDataSourceMBean[])((JDBCTxDataSourceMBean[])this._getHelper()._removeElement(var2, JDBCTxDataSourceMBean.class, var1));
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
               this.setJDBCTxDataSources(var3);
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

   public JDBCTxDataSourceMBean lookupJDBCTxDataSource(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._JDBCTxDataSources).iterator();

      JDBCTxDataSourceMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (JDBCTxDataSourceMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addJDBCMultiPool(JDBCMultiPoolMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 32)) {
         JDBCMultiPoolMBean[] var2;
         if (this._isSet(32)) {
            var2 = (JDBCMultiPoolMBean[])((JDBCMultiPoolMBean[])this._getHelper()._extendArray(this.getJDBCMultiPools(), JDBCMultiPoolMBean.class, var1));
         } else {
            var2 = new JDBCMultiPoolMBean[]{var1};
         }

         try {
            this.setJDBCMultiPools(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public JDBCMultiPoolMBean[] getJDBCMultiPools() {
      return this._JDBCMultiPools;
   }

   public boolean isJDBCMultiPoolsSet() {
      return this._isSet(32);
   }

   public void removeJDBCMultiPool(JDBCMultiPoolMBean var1) {
      this.destroyJDBCMultiPool(var1);
   }

   public void setJDBCMultiPools(JDBCMultiPoolMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new JDBCMultiPoolMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 32)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      JDBCMultiPoolMBean[] var5 = this._JDBCMultiPools;
      this._JDBCMultiPools = (JDBCMultiPoolMBean[])var4;
      this._postSet(32, var5, var4);
   }

   public JDBCMultiPoolMBean createJDBCMultiPool(String var1) {
      JDBCMultiPoolMBeanImpl var2 = new JDBCMultiPoolMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addJDBCMultiPool(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyJDBCMultiPool(JDBCMultiPoolMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 32);
         JDBCMultiPoolMBean[] var2 = this.getJDBCMultiPools();
         JDBCMultiPoolMBean[] var3 = (JDBCMultiPoolMBean[])((JDBCMultiPoolMBean[])this._getHelper()._removeElement(var2, JDBCMultiPoolMBean.class, var1));
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
               this.setJDBCMultiPools(var3);
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

   public JDBCMultiPoolMBean lookupJDBCMultiPool(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._JDBCMultiPools).iterator();

      JDBCMultiPoolMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (JDBCMultiPoolMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addMessagingBridge(MessagingBridgeMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 33)) {
         MessagingBridgeMBean[] var2;
         if (this._isSet(33)) {
            var2 = (MessagingBridgeMBean[])((MessagingBridgeMBean[])this._getHelper()._extendArray(this.getMessagingBridges(), MessagingBridgeMBean.class, var1));
         } else {
            var2 = new MessagingBridgeMBean[]{var1};
         }

         try {
            this.setMessagingBridges(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public MessagingBridgeMBean[] getMessagingBridges() {
      return this._MessagingBridges;
   }

   public boolean isMessagingBridgesSet() {
      return this._isSet(33);
   }

   public void removeMessagingBridge(MessagingBridgeMBean var1) {
      this.destroyMessagingBridge(var1);
   }

   public void setMessagingBridges(MessagingBridgeMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new MessagingBridgeMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 33)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      MessagingBridgeMBean[] var5 = this._MessagingBridges;
      this._MessagingBridges = (MessagingBridgeMBean[])var4;
      this._postSet(33, var5, var4);
   }

   public MessagingBridgeMBean createMessagingBridge(String var1) {
      MessagingBridgeMBeanImpl var2 = new MessagingBridgeMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addMessagingBridge(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyMessagingBridge(MessagingBridgeMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 33);
         MessagingBridgeMBean[] var2 = this.getMessagingBridges();
         MessagingBridgeMBean[] var3 = (MessagingBridgeMBean[])((MessagingBridgeMBean[])this._getHelper()._removeElement(var2, MessagingBridgeMBean.class, var1));
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
               this.setMessagingBridges(var3);
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

   public MessagingBridgeMBean lookupMessagingBridge(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._MessagingBridges).iterator();

      MessagingBridgeMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (MessagingBridgeMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public HashMap start() {
      return this._customizer.start();
   }

   public HashMap kill() {
      return this._customizer.kill();
   }

   public void setProductionModeEnabled(boolean var1) {
      boolean var2 = this.isProductionModeEnabled();
      this._customizer.setProductionModeEnabled(var1);
      this._postSet(34, var2, var1);
   }

   public boolean isProductionModeEnabled() {
      return this._customizer.isProductionModeEnabled();
   }

   public boolean isProductionModeEnabledSet() {
      return this._isSet(34);
   }

   public EmbeddedLDAPMBean getEmbeddedLDAP() {
      return this._EmbeddedLDAP;
   }

   public boolean isEmbeddedLDAPSet() {
      return this._isSet(35) || this._isAnythingSet((AbstractDescriptorBean)this.getEmbeddedLDAP());
   }

   public void setEmbeddedLDAP(EmbeddedLDAPMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 35)) {
         this._postCreate(var2);
      }

      EmbeddedLDAPMBean var3 = this._EmbeddedLDAP;
      this._EmbeddedLDAP = var1;
      this._postSet(35, var3, var1);
   }

   public boolean isAdministrationPortEnabled() {
      return this._AdministrationPortEnabled;
   }

   public boolean isAdministrationPortEnabledSet() {
      return this._isSet(36);
   }

   public void setAdministrationPortEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._AdministrationPortEnabled;
      this._AdministrationPortEnabled = var1;
      this._postSet(36, var2, var1);
   }

   public int getAdministrationPort() {
      return this._AdministrationPort;
   }

   public boolean isAdministrationPortSet() {
      return this._isSet(37);
   }

   public void setExalogicOptimizationsEnabled(boolean var1) {
      boolean var2 = this._ExalogicOptimizationsEnabled;
      this._ExalogicOptimizationsEnabled = var1;
      this._postSet(38, var2, var1);
   }

   public boolean isExalogicOptimizationsEnabled() {
      return this._ExalogicOptimizationsEnabled;
   }

   public boolean isExalogicOptimizationsEnabledSet() {
      return this._isSet(38);
   }

   public void setAdministrationPort(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("AdministrationPort", (long)var1, 1L, 65535L);
      int var2 = this._AdministrationPort;
      this._AdministrationPort = var1;
      this._postSet(37, var2, var1);
   }

   public int getArchiveConfigurationCount() {
      return this._ArchiveConfigurationCount;
   }

   public boolean isArchiveConfigurationCountSet() {
      return this._isSet(39);
   }

   public void setArchiveConfigurationCount(int var1) {
      int var2 = this._ArchiveConfigurationCount;
      this._ArchiveConfigurationCount = var1;
      this._postSet(39, var2, var1);
   }

   public boolean isConfigBackupEnabled() {
      return this._ConfigBackupEnabled;
   }

   public boolean isConfigBackupEnabledSet() {
      return this._isSet(40);
   }

   public void setConfigBackupEnabled(boolean var1) {
      boolean var2 = this._ConfigBackupEnabled;
      this._ConfigBackupEnabled = var1;
      this._postSet(40, var2, var1);
   }

   public String getConfigurationVersion() {
      return this._ConfigurationVersion;
   }

   public boolean isConfigurationVersionSet() {
      return this._isSet(41);
   }

   public void setConfigurationVersion(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ConfigurationVersion;
      this._ConfigurationVersion = var1;
      this._postSet(41, var2, var1);
   }

   public boolean isAdministrationMBeanAuditingEnabled() {
      return this._AdministrationMBeanAuditingEnabled;
   }

   public boolean isAdministrationMBeanAuditingEnabledSet() {
      return this._isSet(42);
   }

   public void setAdministrationMBeanAuditingEnabled(boolean var1) {
      boolean var2 = this._AdministrationMBeanAuditingEnabled;
      this._AdministrationMBeanAuditingEnabled = var1;
      this._postSet(42, var2, var1);
   }

   public String getConfigurationAuditType() {
      return this._ConfigurationAuditType;
   }

   public boolean isConfigurationAuditTypeSet() {
      return this._isSet(43);
   }

   public void setConfigurationAuditType(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"none", "log", "audit", "logaudit"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("ConfigurationAuditType", var1, var2);
      String var3 = this._ConfigurationAuditType;
      this._ConfigurationAuditType = var1;
      this._postSet(43, var3, var1);
   }

   public boolean isClusterConstraintsEnabled() {
      return this._customizer.isClusterConstraintsEnabled();
   }

   public boolean isClusterConstraintsEnabledSet() {
      return this._isSet(44);
   }

   public void setClusterConstraintsEnabled(boolean var1) {
      boolean var2 = this.isClusterConstraintsEnabled();
      this._customizer.setClusterConstraintsEnabled(var1);
      this._postSet(44, var2, var1);
   }

   public void addApplication(ApplicationMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 45)) {
         ApplicationMBean[] var2;
         if (this._isSet(45)) {
            var2 = (ApplicationMBean[])((ApplicationMBean[])this._getHelper()._extendArray(this.getApplications(), ApplicationMBean.class, var1));
         } else {
            var2 = new ApplicationMBean[]{var1};
         }

         try {
            this.setApplications(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public ApplicationMBean[] getApplications() {
      return this._Applications;
   }

   public boolean isApplicationsSet() {
      return this._isSet(45);
   }

   public void removeApplication(ApplicationMBean var1) {
      this.destroyApplication(var1);
   }

   public void setApplications(ApplicationMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new ApplicationMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 45)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      ApplicationMBean[] var5 = this._Applications;
      this._Applications = (ApplicationMBean[])var4;
      this._postSet(45, var5, var4);
   }

   public ApplicationMBean createApplication(String var1) {
      ApplicationMBeanImpl var2 = new ApplicationMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addApplication(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyApplication(ApplicationMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 45);
         ApplicationMBean[] var2 = this.getApplications();
         ApplicationMBean[] var3 = (ApplicationMBean[])((ApplicationMBean[])this._getHelper()._removeElement(var2, ApplicationMBean.class, var1));
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
               this.setApplications(var3);
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

   public ApplicationMBean lookupApplication(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._Applications).iterator();

      ApplicationMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (ApplicationMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addAppDeployment(AppDeploymentMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 46)) {
         AppDeploymentMBean[] var2;
         if (this._isSet(46)) {
            var2 = (AppDeploymentMBean[])((AppDeploymentMBean[])this._getHelper()._extendArray(this.getAppDeployments(), AppDeploymentMBean.class, var1));
         } else {
            var2 = new AppDeploymentMBean[]{var1};
         }

         try {
            this.setAppDeployments(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public AppDeploymentMBean[] getAppDeployments() {
      return this._AppDeployments;
   }

   public boolean isAppDeploymentsSet() {
      return this._isSet(46);
   }

   public void removeAppDeployment(AppDeploymentMBean var1) {
      this.destroyAppDeployment(var1);
   }

   public void setAppDeployments(AppDeploymentMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new AppDeploymentMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 46)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      AppDeploymentMBean[] var5 = this._AppDeployments;
      this._AppDeployments = (AppDeploymentMBean[])var4;
      this._postSet(46, var5, var4);
   }

   public AppDeploymentMBean lookupAppDeployment(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._AppDeployments).iterator();

      AppDeploymentMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (AppDeploymentMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public AppDeploymentMBean createAppDeployment(String var1, String var2) throws IllegalArgumentException {
      AppDeploymentMBeanImpl var3 = new AppDeploymentMBeanImpl(this, -1);

      try {
         var3.setName(var1);
         var3.setSourcePath(var2);
         this.addAppDeployment(var3);
         return var3;
      } catch (Exception var5) {
         if (var5 instanceof RuntimeException) {
            throw (RuntimeException)var5;
         } else if (var5 instanceof IllegalArgumentException) {
            throw (IllegalArgumentException)var5;
         } else {
            throw new UndeclaredThrowableException(var5);
         }
      }
   }

   public void destroyAppDeployment(AppDeploymentMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 46);
         AppDeploymentMBean[] var2 = this.getAppDeployments();
         AppDeploymentMBean[] var3 = (AppDeploymentMBean[])((AppDeploymentMBean[])this._getHelper()._removeElement(var2, AppDeploymentMBean.class, var1));
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
               this.setAppDeployments(var3);
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

   public void addInternalAppDeployment(AppDeploymentMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 47)) {
         AppDeploymentMBean[] var2 = (AppDeploymentMBean[])((AppDeploymentMBean[])this._getHelper()._extendArray(this.getInternalAppDeployments(), AppDeploymentMBean.class, var1));

         try {
            this.setInternalAppDeployments(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public AppDeploymentMBean[] getInternalAppDeployments() {
      return this._InternalAppDeployments;
   }

   public boolean isInternalAppDeploymentsSet() {
      return this._isSet(47);
   }

   public void removeInternalAppDeployment(AppDeploymentMBean var1) {
      AppDeploymentMBean[] var2 = this.getInternalAppDeployments();
      AppDeploymentMBean[] var3 = (AppDeploymentMBean[])((AppDeploymentMBean[])this._getHelper()._removeElement(var2, AppDeploymentMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setInternalAppDeployments(var3);
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            }

            throw new UndeclaredThrowableException(var5);
         }
      }

   }

   public void setInternalAppDeployments(AppDeploymentMBean[] var1) throws InvalidAttributeValueException {
      Object var2 = var1 == null ? new AppDeploymentMBeanImpl[0] : var1;
      this._InternalAppDeployments = (AppDeploymentMBean[])var2;
   }

   public AppDeploymentMBean lookupInternalAppDeployment(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._InternalAppDeployments).iterator();

      AppDeploymentMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (AppDeploymentMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public AppDeploymentMBean createInternalAppDeployment(String var1, String var2) throws IllegalArgumentException {
      AppDeploymentMBeanImpl var3 = new AppDeploymentMBeanImpl(this, -1);

      try {
         var3.setName(var1);
         var3.setSourcePath(var2);
         this.addInternalAppDeployment(var3);
         return var3;
      } catch (Exception var5) {
         if (var5 instanceof RuntimeException) {
            throw (RuntimeException)var5;
         } else if (var5 instanceof IllegalArgumentException) {
            throw (IllegalArgumentException)var5;
         } else {
            throw new UndeclaredThrowableException(var5);
         }
      }
   }

   public void destroyInternalAppDeployment(AppDeploymentMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 47);
         AppDeploymentMBean[] var2 = this.getInternalAppDeployments();
         AppDeploymentMBean[] var3 = (AppDeploymentMBean[])((AppDeploymentMBean[])this._getHelper()._removeElement(var2, AppDeploymentMBean.class, var1));
         if (var2.length != var3.length) {
         }

      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void addLibrary(LibraryMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 48)) {
         LibraryMBean[] var2;
         if (this._isSet(48)) {
            var2 = (LibraryMBean[])((LibraryMBean[])this._getHelper()._extendArray(this.getLibraries(), LibraryMBean.class, var1));
         } else {
            var2 = new LibraryMBean[]{var1};
         }

         try {
            this.setLibraries(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public LibraryMBean[] getLibraries() {
      return this._Libraries;
   }

   public boolean isLibrariesSet() {
      return this._isSet(48);
   }

   public void removeLibrary(LibraryMBean var1) {
      this.destroyLibrary(var1);
   }

   public void setLibraries(LibraryMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new LibraryMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 48)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      LibraryMBean[] var5 = this._Libraries;
      this._Libraries = (LibraryMBean[])var4;
      this._postSet(48, var5, var4);
   }

   public LibraryMBean lookupLibrary(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._Libraries).iterator();

      LibraryMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (LibraryMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public LibraryMBean createLibrary(String var1, String var2) {
      LibraryMBeanImpl var3 = new LibraryMBeanImpl(this, -1);

      try {
         var3.setName(var1);
         var3.setSourcePath(var2);
         this.addLibrary(var3);
         return var3;
      } catch (Exception var5) {
         if (var5 instanceof RuntimeException) {
            throw (RuntimeException)var5;
         } else {
            throw new UndeclaredThrowableException(var5);
         }
      }
   }

   public void destroyLibrary(LibraryMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 48);
         LibraryMBean[] var2 = this.getLibraries();
         LibraryMBean[] var3 = (LibraryMBean[])((LibraryMBean[])this._getHelper()._removeElement(var2, LibraryMBean.class, var1));
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
               this.setLibraries(var3);
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

   public void addDomainLibrary(DomainLibraryMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 49)) {
         DomainLibraryMBean[] var2;
         if (this._isSet(49)) {
            var2 = (DomainLibraryMBean[])((DomainLibraryMBean[])this._getHelper()._extendArray(this.getDomainLibraries(), DomainLibraryMBean.class, var1));
         } else {
            var2 = new DomainLibraryMBean[]{var1};
         }

         try {
            this.setDomainLibraries(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public DomainLibraryMBean[] getDomainLibraries() {
      return this._DomainLibraries;
   }

   public boolean isDomainLibrariesSet() {
      return this._isSet(49);
   }

   public void removeDomainLibrary(DomainLibraryMBean var1) {
      this.destroyDomainLibrary(var1);
   }

   public void setDomainLibraries(DomainLibraryMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new DomainLibraryMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 49)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      DomainLibraryMBean[] var5 = this._DomainLibraries;
      this._DomainLibraries = (DomainLibraryMBean[])var4;
      this._postSet(49, var5, var4);
   }

   public DomainLibraryMBean lookupDomainLibrary(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._DomainLibraries).iterator();

      DomainLibraryMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (DomainLibraryMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public DomainLibraryMBean createDomainLibrary(String var1, String var2) {
      DomainLibraryMBeanImpl var3 = new DomainLibraryMBeanImpl(this, -1);

      try {
         var3.setName(var1);
         var3.setSourcePath(var2);
         this.addDomainLibrary(var3);
         return var3;
      } catch (Exception var5) {
         if (var5 instanceof RuntimeException) {
            throw (RuntimeException)var5;
         } else {
            throw new UndeclaredThrowableException(var5);
         }
      }
   }

   public void destroyDomainLibrary(DomainLibraryMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 49);
         DomainLibraryMBean[] var2 = this.getDomainLibraries();
         DomainLibraryMBean[] var3 = (DomainLibraryMBean[])((DomainLibraryMBean[])this._getHelper()._removeElement(var2, DomainLibraryMBean.class, var1));
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
               this.setDomainLibraries(var3);
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

   public void addInternalLibrary(LibraryMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 50)) {
         LibraryMBean[] var2 = (LibraryMBean[])((LibraryMBean[])this._getHelper()._extendArray(this.getInternalLibraries(), LibraryMBean.class, var1));

         try {
            this.setInternalLibraries(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public LibraryMBean[] getInternalLibraries() {
      return this._InternalLibraries;
   }

   public boolean isInternalLibrariesSet() {
      return this._isSet(50);
   }

   public void removeInternalLibrary(LibraryMBean var1) {
      LibraryMBean[] var2 = this.getInternalLibraries();
      LibraryMBean[] var3 = (LibraryMBean[])((LibraryMBean[])this._getHelper()._removeElement(var2, LibraryMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setInternalLibraries(var3);
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            }

            throw new UndeclaredThrowableException(var5);
         }
      }

   }

   public void setInternalLibraries(LibraryMBean[] var1) throws InvalidAttributeValueException {
      Object var2 = var1 == null ? new LibraryMBeanImpl[0] : var1;
      this._InternalLibraries = (LibraryMBean[])var2;
   }

   public LibraryMBean lookupInternalLibrary(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._InternalLibraries).iterator();

      LibraryMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (LibraryMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public LibraryMBean createInternalLibrary(String var1, String var2) {
      LibraryMBeanImpl var3 = new LibraryMBeanImpl(this, -1);

      try {
         var3.setName(var1);
         var3.setSourcePath(var2);
         this.addInternalLibrary(var3);
         return var3;
      } catch (Exception var5) {
         if (var5 instanceof RuntimeException) {
            throw (RuntimeException)var5;
         } else {
            throw new UndeclaredThrowableException(var5);
         }
      }
   }

   public void destroyInternalLibrary(LibraryMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 50);
         LibraryMBean[] var2 = this.getInternalLibraries();
         LibraryMBean[] var3 = (LibraryMBean[])((LibraryMBean[])this._getHelper()._removeElement(var2, LibraryMBean.class, var1));
         if (var2.length != var3.length) {
         }

      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public BasicDeploymentMBean[] getBasicDeployments() {
      return this._customizer.getBasicDeployments();
   }

   public void addWSReliableDeliveryPolicy(WSReliableDeliveryPolicyMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 51)) {
         WSReliableDeliveryPolicyMBean[] var2;
         if (this._isSet(51)) {
            var2 = (WSReliableDeliveryPolicyMBean[])((WSReliableDeliveryPolicyMBean[])this._getHelper()._extendArray(this.getWSReliableDeliveryPolicies(), WSReliableDeliveryPolicyMBean.class, var1));
         } else {
            var2 = new WSReliableDeliveryPolicyMBean[]{var1};
         }

         try {
            this.setWSReliableDeliveryPolicies(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public WSReliableDeliveryPolicyMBean[] getWSReliableDeliveryPolicies() {
      return this._WSReliableDeliveryPolicies;
   }

   public boolean isWSReliableDeliveryPoliciesSet() {
      return this._isSet(51);
   }

   public void removeWSReliableDeliveryPolicy(WSReliableDeliveryPolicyMBean var1) {
      this.destroyWSReliableDeliveryPolicy(var1);
   }

   public void setWSReliableDeliveryPolicies(WSReliableDeliveryPolicyMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new WSReliableDeliveryPolicyMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 51)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      WSReliableDeliveryPolicyMBean[] var5 = this._WSReliableDeliveryPolicies;
      this._WSReliableDeliveryPolicies = (WSReliableDeliveryPolicyMBean[])var4;
      this._postSet(51, var5, var4);
   }

   public WSReliableDeliveryPolicyMBean lookupWSReliableDeliveryPolicy(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._WSReliableDeliveryPolicies).iterator();

      WSReliableDeliveryPolicyMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (WSReliableDeliveryPolicyMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public WSReliableDeliveryPolicyMBean createWSReliableDeliveryPolicy(String var1) {
      WSReliableDeliveryPolicyMBeanImpl var2 = new WSReliableDeliveryPolicyMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addWSReliableDeliveryPolicy(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyWSReliableDeliveryPolicy(WSReliableDeliveryPolicyMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 51);
         WSReliableDeliveryPolicyMBean[] var2 = this.getWSReliableDeliveryPolicies();
         WSReliableDeliveryPolicyMBean[] var3 = (WSReliableDeliveryPolicyMBean[])((WSReliableDeliveryPolicyMBean[])this._getHelper()._removeElement(var2, WSReliableDeliveryPolicyMBean.class, var1));
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
               this.setWSReliableDeliveryPolicies(var3);
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

   public void addJDBCDataSourceFactory(JDBCDataSourceFactoryMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 52)) {
         JDBCDataSourceFactoryMBean[] var2;
         if (this._isSet(52)) {
            var2 = (JDBCDataSourceFactoryMBean[])((JDBCDataSourceFactoryMBean[])this._getHelper()._extendArray(this.getJDBCDataSourceFactories(), JDBCDataSourceFactoryMBean.class, var1));
         } else {
            var2 = new JDBCDataSourceFactoryMBean[]{var1};
         }

         try {
            this.setJDBCDataSourceFactories(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public JDBCDataSourceFactoryMBean[] getJDBCDataSourceFactories() {
      return this._JDBCDataSourceFactories;
   }

   public boolean isJDBCDataSourceFactoriesSet() {
      return this._isSet(52);
   }

   public void removeJDBCDataSourceFactory(JDBCDataSourceFactoryMBean var1) {
      this.destroyJDBCDataSourceFactory(var1);
   }

   public void setJDBCDataSourceFactories(JDBCDataSourceFactoryMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new JDBCDataSourceFactoryMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 52)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      JDBCDataSourceFactoryMBean[] var5 = this._JDBCDataSourceFactories;
      this._JDBCDataSourceFactories = (JDBCDataSourceFactoryMBean[])var4;
      this._postSet(52, var5, var4);
   }

   public JDBCDataSourceFactoryMBean createJDBCDataSourceFactory(String var1) {
      JDBCDataSourceFactoryMBeanImpl var2 = new JDBCDataSourceFactoryMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addJDBCDataSourceFactory(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyJDBCDataSourceFactory(JDBCDataSourceFactoryMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 52);
         JDBCDataSourceFactoryMBean[] var2 = this.getJDBCDataSourceFactories();
         JDBCDataSourceFactoryMBean[] var3 = (JDBCDataSourceFactoryMBean[])((JDBCDataSourceFactoryMBean[])this._getHelper()._removeElement(var2, JDBCDataSourceFactoryMBean.class, var1));
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
               this.setJDBCDataSourceFactories(var3);
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

   public JDBCDataSourceFactoryMBean lookupJDBCDataSourceFactory(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._JDBCDataSourceFactories).iterator();

      JDBCDataSourceFactoryMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (JDBCDataSourceFactoryMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addMachine(MachineMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 53)) {
         MachineMBean[] var2;
         if (this._isSet(53)) {
            var2 = (MachineMBean[])((MachineMBean[])this._getHelper()._extendArray(this.getMachines(), MachineMBean.class, var1));
         } else {
            var2 = new MachineMBean[]{var1};
         }

         try {
            this.setMachines(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public MachineMBean[] getMachines() {
      return this._Machines;
   }

   public boolean isMachinesSet() {
      return this._isSet(53);
   }

   public void removeMachine(MachineMBean var1) {
      this.destroyMachine(var1);
   }

   public void setMachines(MachineMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new MachineMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 53)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      MachineMBean[] var5 = this._Machines;
      this._Machines = (MachineMBean[])var4;
      this._postSet(53, var5, var4);
   }

   public MachineMBean createMachine(String var1) {
      MachineMBeanImpl var2 = new MachineMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addMachine(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public UnixMachineMBean createUnixMachine(String var1) {
      UnixMachineMBeanImpl var2 = new UnixMachineMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addMachine(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyMachine(MachineMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 53);
         MachineMBean[] var2 = this.getMachines();
         MachineMBean[] var3 = (MachineMBean[])((MachineMBean[])this._getHelper()._removeElement(var2, MachineMBean.class, var1));
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
               this.setMachines(var3);
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

   public MachineMBean lookupMachine(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._Machines).iterator();

      MachineMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (MachineMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addXMLEntityCache(XMLEntityCacheMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 54)) {
         XMLEntityCacheMBean[] var2;
         if (this._isSet(54)) {
            var2 = (XMLEntityCacheMBean[])((XMLEntityCacheMBean[])this._getHelper()._extendArray(this.getXMLEntityCaches(), XMLEntityCacheMBean.class, var1));
         } else {
            var2 = new XMLEntityCacheMBean[]{var1};
         }

         try {
            this.setXMLEntityCaches(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public XMLEntityCacheMBean[] getXMLEntityCaches() {
      return this._XMLEntityCaches;
   }

   public boolean isXMLEntityCachesSet() {
      return this._isSet(54);
   }

   public void removeXMLEntityCache(XMLEntityCacheMBean var1) {
      this.destroyXMLEntityCache(var1);
   }

   public void setXMLEntityCaches(XMLEntityCacheMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new XMLEntityCacheMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 54)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      XMLEntityCacheMBean[] var5 = this._XMLEntityCaches;
      this._XMLEntityCaches = (XMLEntityCacheMBean[])var4;
      this._postSet(54, var5, var4);
   }

   public XMLEntityCacheMBean createXMLEntityCache(String var1) {
      XMLEntityCacheMBeanImpl var2 = new XMLEntityCacheMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addXMLEntityCache(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public XMLEntityCacheMBean lookupXMLEntityCache(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._XMLEntityCaches).iterator();

      XMLEntityCacheMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (XMLEntityCacheMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void destroyXMLEntityCache(XMLEntityCacheMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 54);
         XMLEntityCacheMBean[] var2 = this.getXMLEntityCaches();
         XMLEntityCacheMBean[] var3 = (XMLEntityCacheMBean[])((XMLEntityCacheMBean[])this._getHelper()._removeElement(var2, XMLEntityCacheMBean.class, var1));
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
               this.setXMLEntityCaches(var3);
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

   public void addXMLRegistry(XMLRegistryMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 55)) {
         XMLRegistryMBean[] var2;
         if (this._isSet(55)) {
            var2 = (XMLRegistryMBean[])((XMLRegistryMBean[])this._getHelper()._extendArray(this.getXMLRegistries(), XMLRegistryMBean.class, var1));
         } else {
            var2 = new XMLRegistryMBean[]{var1};
         }

         try {
            this.setXMLRegistries(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public XMLRegistryMBean[] getXMLRegistries() {
      return this._XMLRegistries;
   }

   public boolean isXMLRegistriesSet() {
      return this._isSet(55);
   }

   public void removeXMLRegistry(XMLRegistryMBean var1) {
      this.destroyXMLRegistry(var1);
   }

   public void setXMLRegistries(XMLRegistryMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new XMLRegistryMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 55)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      XMLRegistryMBean[] var5 = this._XMLRegistries;
      this._XMLRegistries = (XMLRegistryMBean[])var4;
      this._postSet(55, var5, var4);
   }

   public XMLRegistryMBean createXMLRegistry(String var1) {
      XMLRegistryMBeanImpl var2 = new XMLRegistryMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addXMLRegistry(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyXMLRegistry(XMLRegistryMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 55);
         XMLRegistryMBean[] var2 = this.getXMLRegistries();
         XMLRegistryMBean[] var3 = (XMLRegistryMBean[])((XMLRegistryMBean[])this._getHelper()._removeElement(var2, XMLRegistryMBean.class, var1));
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
               this.setXMLRegistries(var3);
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

   public XMLRegistryMBean lookupXMLRegistry(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._XMLRegistries).iterator();

      XMLRegistryMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (XMLRegistryMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addFileRealm(FileRealmMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 56)) {
         FileRealmMBean[] var2;
         if (this._isSet(56)) {
            var2 = (FileRealmMBean[])((FileRealmMBean[])this._getHelper()._extendArray(this.getFileRealms(), FileRealmMBean.class, var1));
         } else {
            var2 = new FileRealmMBean[]{var1};
         }

         try {
            this.setFileRealms(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public FileRealmMBean[] getFileRealms() {
      return this._FileRealms;
   }

   public boolean isFileRealmsSet() {
      return this._isSet(56);
   }

   public void removeFileRealm(FileRealmMBean var1) {
      this.destroyFileRealm(var1);
   }

   public void setFileRealms(FileRealmMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new FileRealmMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 56)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      FileRealmMBean[] var5 = this._FileRealms;
      this._FileRealms = (FileRealmMBean[])var4;
      this._postSet(56, var5, var4);
   }

   public FileRealmMBean createFileRealm(String var1) {
      FileRealmMBeanImpl var2 = new FileRealmMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addFileRealm(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyFileRealm(FileRealmMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 56);
         FileRealmMBean[] var2 = this.getFileRealms();
         FileRealmMBean[] var3 = (FileRealmMBean[])((FileRealmMBean[])this._getHelper()._removeElement(var2, FileRealmMBean.class, var1));
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
               this.setFileRealms(var3);
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

   public FileRealmMBean lookupFileRealm(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._FileRealms).iterator();

      FileRealmMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (FileRealmMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addCachingRealm(CachingRealmMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 57)) {
         CachingRealmMBean[] var2;
         if (this._isSet(57)) {
            var2 = (CachingRealmMBean[])((CachingRealmMBean[])this._getHelper()._extendArray(this.getCachingRealms(), CachingRealmMBean.class, var1));
         } else {
            var2 = new CachingRealmMBean[]{var1};
         }

         try {
            this.setCachingRealms(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public CachingRealmMBean[] getCachingRealms() {
      return this._CachingRealms;
   }

   public boolean isCachingRealmsSet() {
      return this._isSet(57);
   }

   public void removeCachingRealm(CachingRealmMBean var1) {
      this.destroyCachingRealm(var1);
   }

   public void setCachingRealms(CachingRealmMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new CachingRealmMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 57)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      CachingRealmMBean[] var5 = this._CachingRealms;
      this._CachingRealms = (CachingRealmMBean[])var4;
      this._postSet(57, var5, var4);
   }

   public CachingRealmMBean createCachingRealm(String var1) {
      CachingRealmMBeanImpl var2 = new CachingRealmMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addCachingRealm(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyCachingRealm(CachingRealmMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 57);
         CachingRealmMBean[] var2 = this.getCachingRealms();
         CachingRealmMBean[] var3 = (CachingRealmMBean[])((CachingRealmMBean[])this._getHelper()._removeElement(var2, CachingRealmMBean.class, var1));
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
               this.setCachingRealms(var3);
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

   public CachingRealmMBean lookupCachingRealm(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._CachingRealms).iterator();

      CachingRealmMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (CachingRealmMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addRealm(RealmMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 58)) {
         RealmMBean[] var2;
         if (this._isSet(58)) {
            var2 = (RealmMBean[])((RealmMBean[])this._getHelper()._extendArray(this.getRealms(), RealmMBean.class, var1));
         } else {
            var2 = new RealmMBean[]{var1};
         }

         try {
            this.setRealms(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public RealmMBean[] getRealms() {
      return this._Realms;
   }

   public boolean isRealmsSet() {
      return this._isSet(58);
   }

   public void removeRealm(RealmMBean var1) {
      this.destroyRealm(var1);
   }

   public void setRealms(RealmMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new RealmMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 58)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      RealmMBean[] var5 = this._Realms;
      this._Realms = (RealmMBean[])var4;
      this._postSet(58, var5, var4);
   }

   public RealmMBean createRealm(String var1) {
      RealmMBeanImpl var2 = new RealmMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addRealm(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyRealm(RealmMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 58);
         RealmMBean[] var2 = this.getRealms();
         RealmMBean[] var3 = (RealmMBean[])((RealmMBean[])this._getHelper()._removeElement(var2, RealmMBean.class, var1));
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
               this.setRealms(var3);
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

   public RealmMBean lookupRealm(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._Realms).iterator();

      RealmMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (RealmMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addPasswordPolicy(PasswordPolicyMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 59)) {
         PasswordPolicyMBean[] var2;
         if (this._isSet(59)) {
            var2 = (PasswordPolicyMBean[])((PasswordPolicyMBean[])this._getHelper()._extendArray(this.getPasswordPolicies(), PasswordPolicyMBean.class, var1));
         } else {
            var2 = new PasswordPolicyMBean[]{var1};
         }

         try {
            this.setPasswordPolicies(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public PasswordPolicyMBean[] getPasswordPolicies() {
      return this._PasswordPolicies;
   }

   public boolean isPasswordPoliciesSet() {
      return this._isSet(59);
   }

   public void removePasswordPolicy(PasswordPolicyMBean var1) {
      this.destroyPasswordPolicy(var1);
   }

   public void setPasswordPolicies(PasswordPolicyMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new PasswordPolicyMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 59)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      PasswordPolicyMBean[] var5 = this._PasswordPolicies;
      this._PasswordPolicies = (PasswordPolicyMBean[])var4;
      this._postSet(59, var5, var4);
   }

   public PasswordPolicyMBean createPasswordPolicy(String var1) {
      PasswordPolicyMBeanImpl var2 = new PasswordPolicyMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addPasswordPolicy(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyPasswordPolicy(PasswordPolicyMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 59);
         PasswordPolicyMBean[] var2 = this.getPasswordPolicies();
         PasswordPolicyMBean[] var3 = (PasswordPolicyMBean[])((PasswordPolicyMBean[])this._getHelper()._removeElement(var2, PasswordPolicyMBean.class, var1));
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
               this.setPasswordPolicies(var3);
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

   public PasswordPolicyMBean lookupPasswordPolicy(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._PasswordPolicies).iterator();

      PasswordPolicyMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (PasswordPolicyMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addBasicRealm(BasicRealmMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 60)) {
         BasicRealmMBean[] var2 = (BasicRealmMBean[])((BasicRealmMBean[])this._getHelper()._extendArray(this.getBasicRealms(), BasicRealmMBean.class, var1));

         try {
            this.setBasicRealms(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public BasicRealmMBean[] getBasicRealms() {
      return this._customizer.getBasicRealms();
   }

   public boolean isBasicRealmsSet() {
      return this._isSet(60);
   }

   public void removeBasicRealm(BasicRealmMBean var1) {
      BasicRealmMBean[] var2 = this.getBasicRealms();
      BasicRealmMBean[] var3 = (BasicRealmMBean[])((BasicRealmMBean[])this._getHelper()._removeElement(var2, BasicRealmMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setBasicRealms(var3);
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            }

            throw new UndeclaredThrowableException(var5);
         }
      }

   }

   public void setBasicRealms(BasicRealmMBean[] var1) throws InvalidAttributeValueException {
      Object var2 = var1 == null ? new BasicRealmMBeanImpl[0] : var1;
      this._BasicRealms = (BasicRealmMBean[])var2;
   }

   public void addCustomRealm(CustomRealmMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 61)) {
         CustomRealmMBean[] var2;
         if (this._isSet(61)) {
            var2 = (CustomRealmMBean[])((CustomRealmMBean[])this._getHelper()._extendArray(this.getCustomRealms(), CustomRealmMBean.class, var1));
         } else {
            var2 = new CustomRealmMBean[]{var1};
         }

         try {
            this.setCustomRealms(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public CustomRealmMBean[] getCustomRealms() {
      return this._CustomRealms;
   }

   public boolean isCustomRealmsSet() {
      return this._isSet(61);
   }

   public void removeCustomRealm(CustomRealmMBean var1) {
      this.destroyCustomRealm(var1);
   }

   public void setCustomRealms(CustomRealmMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new CustomRealmMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 61)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      CustomRealmMBean[] var5 = this._CustomRealms;
      this._CustomRealms = (CustomRealmMBean[])var4;
      this._postSet(61, var5, var4);
   }

   public CustomRealmMBean createCustomRealm(String var1) {
      CustomRealmMBeanImpl var2 = new CustomRealmMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addCustomRealm(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyCustomRealm(CustomRealmMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 61);
         CustomRealmMBean[] var2 = this.getCustomRealms();
         CustomRealmMBean[] var3 = (CustomRealmMBean[])((CustomRealmMBean[])this._getHelper()._removeElement(var2, CustomRealmMBean.class, var1));
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
               this.setCustomRealms(var3);
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

   public CustomRealmMBean lookupCustomRealm(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._CustomRealms).iterator();

      CustomRealmMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (CustomRealmMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addLDAPRealm(LDAPRealmMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 62)) {
         LDAPRealmMBean[] var2;
         if (this._isSet(62)) {
            var2 = (LDAPRealmMBean[])((LDAPRealmMBean[])this._getHelper()._extendArray(this.getLDAPRealms(), LDAPRealmMBean.class, var1));
         } else {
            var2 = new LDAPRealmMBean[]{var1};
         }

         try {
            this.setLDAPRealms(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public LDAPRealmMBean[] getLDAPRealms() {
      return this._LDAPRealms;
   }

   public boolean isLDAPRealmsSet() {
      return this._isSet(62);
   }

   public void removeLDAPRealm(LDAPRealmMBean var1) {
      this.destroyLDAPRealm(var1);
   }

   public void setLDAPRealms(LDAPRealmMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new LDAPRealmMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 62)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      LDAPRealmMBean[] var5 = this._LDAPRealms;
      this._LDAPRealms = (LDAPRealmMBean[])var4;
      this._postSet(62, var5, var4);
   }

   public LDAPRealmMBean createLDAPRealm(String var1) {
      LDAPRealmMBeanImpl var2 = new LDAPRealmMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addLDAPRealm(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyLDAPRealm(LDAPRealmMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 62);
         LDAPRealmMBean[] var2 = this.getLDAPRealms();
         LDAPRealmMBean[] var3 = (LDAPRealmMBean[])((LDAPRealmMBean[])this._getHelper()._removeElement(var2, LDAPRealmMBean.class, var1));
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
               this.setLDAPRealms(var3);
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

   public LDAPRealmMBean lookupLDAPRealm(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._LDAPRealms).iterator();

      LDAPRealmMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (LDAPRealmMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addNTRealm(NTRealmMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 63)) {
         NTRealmMBean[] var2;
         if (this._isSet(63)) {
            var2 = (NTRealmMBean[])((NTRealmMBean[])this._getHelper()._extendArray(this.getNTRealms(), NTRealmMBean.class, var1));
         } else {
            var2 = new NTRealmMBean[]{var1};
         }

         try {
            this.setNTRealms(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public NTRealmMBean[] getNTRealms() {
      return this._NTRealms;
   }

   public boolean isNTRealmsSet() {
      return this._isSet(63);
   }

   public void removeNTRealm(NTRealmMBean var1) {
      this.destroyNTRealm(var1);
   }

   public void setNTRealms(NTRealmMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new NTRealmMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 63)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      NTRealmMBean[] var5 = this._NTRealms;
      this._NTRealms = (NTRealmMBean[])var4;
      this._postSet(63, var5, var4);
   }

   public NTRealmMBean createNTRealm(String var1) {
      NTRealmMBeanImpl var2 = new NTRealmMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addNTRealm(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyNTRealm(NTRealmMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 63);
         NTRealmMBean[] var2 = this.getNTRealms();
         NTRealmMBean[] var3 = (NTRealmMBean[])((NTRealmMBean[])this._getHelper()._removeElement(var2, NTRealmMBean.class, var1));
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
               this.setNTRealms(var3);
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

   public NTRealmMBean lookupNTRealm(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._NTRealms).iterator();

      NTRealmMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (NTRealmMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addRDBMSRealm(RDBMSRealmMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 64)) {
         RDBMSRealmMBean[] var2;
         if (this._isSet(64)) {
            var2 = (RDBMSRealmMBean[])((RDBMSRealmMBean[])this._getHelper()._extendArray(this.getRDBMSRealms(), RDBMSRealmMBean.class, var1));
         } else {
            var2 = new RDBMSRealmMBean[]{var1};
         }

         try {
            this.setRDBMSRealms(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public RDBMSRealmMBean[] getRDBMSRealms() {
      return this._RDBMSRealms;
   }

   public boolean isRDBMSRealmsSet() {
      return this._isSet(64);
   }

   public void removeRDBMSRealm(RDBMSRealmMBean var1) {
      this.destroyRDBMSRealm(var1);
   }

   public void setRDBMSRealms(RDBMSRealmMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new RDBMSRealmMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 64)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      RDBMSRealmMBean[] var5 = this._RDBMSRealms;
      this._RDBMSRealms = (RDBMSRealmMBean[])var4;
      this._postSet(64, var5, var4);
   }

   public RDBMSRealmMBean createRDBMSRealm(String var1) {
      RDBMSRealmMBeanImpl var2 = new RDBMSRealmMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addRDBMSRealm(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyRDBMSRealm(RDBMSRealmMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 64);
         RDBMSRealmMBean[] var2 = this.getRDBMSRealms();
         RDBMSRealmMBean[] var3 = (RDBMSRealmMBean[])((RDBMSRealmMBean[])this._getHelper()._removeElement(var2, RDBMSRealmMBean.class, var1));
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
               this.setRDBMSRealms(var3);
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

   public RDBMSRealmMBean lookupRDBMSRealm(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._RDBMSRealms).iterator();

      RDBMSRealmMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (RDBMSRealmMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addUnixRealm(UnixRealmMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 65)) {
         UnixRealmMBean[] var2;
         if (this._isSet(65)) {
            var2 = (UnixRealmMBean[])((UnixRealmMBean[])this._getHelper()._extendArray(this.getUnixRealms(), UnixRealmMBean.class, var1));
         } else {
            var2 = new UnixRealmMBean[]{var1};
         }

         try {
            this.setUnixRealms(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public UnixRealmMBean[] getUnixRealms() {
      return this._UnixRealms;
   }

   public boolean isUnixRealmsSet() {
      return this._isSet(65);
   }

   public void removeUnixRealm(UnixRealmMBean var1) {
      this.destroyUnixRealm(var1);
   }

   public void setUnixRealms(UnixRealmMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new UnixRealmMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 65)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      UnixRealmMBean[] var5 = this._UnixRealms;
      this._UnixRealms = (UnixRealmMBean[])var4;
      this._postSet(65, var5, var4);
   }

   public UnixRealmMBean createUnixRealm(String var1) {
      UnixRealmMBeanImpl var2 = new UnixRealmMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addUnixRealm(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyUnixRealm(UnixRealmMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 65);
         UnixRealmMBean[] var2 = this.getUnixRealms();
         UnixRealmMBean[] var3 = (UnixRealmMBean[])((UnixRealmMBean[])this._getHelper()._removeElement(var2, UnixRealmMBean.class, var1));
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
               this.setUnixRealms(var3);
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

   public UnixRealmMBean lookupUnixRealm(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._UnixRealms).iterator();

      UnixRealmMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (UnixRealmMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addTarget(TargetMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 66)) {
         TargetMBean[] var2 = (TargetMBean[])((TargetMBean[])this._getHelper()._extendArray(this.getTargets(), TargetMBean.class, var1));

         try {
            this.setTargets(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public TargetMBean[] getTargets() {
      return this._customizer.getTargets();
   }

   public boolean isTargetsSet() {
      return this._isSet(66);
   }

   public void removeTarget(TargetMBean var1) {
      TargetMBean[] var2 = this.getTargets();
      TargetMBean[] var3 = (TargetMBean[])((TargetMBean[])this._getHelper()._removeElement(var2, TargetMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setTargets(var3);
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            }

            throw new UndeclaredThrowableException(var5);
         }
      }

   }

   public void setTargets(TargetMBean[] var1) throws InvalidAttributeValueException {
      Object var2 = var1 == null ? new TargetMBeanImpl[0] : var1;
      this._Targets = (TargetMBean[])var2;
   }

   public TargetMBean lookupTarget(String var1) throws IllegalArgumentException {
      return this._customizer.lookupTarget(var1);
   }

   public void addJMSServer(JMSServerMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 67)) {
         JMSServerMBean[] var2;
         if (this._isSet(67)) {
            var2 = (JMSServerMBean[])((JMSServerMBean[])this._getHelper()._extendArray(this.getJMSServers(), JMSServerMBean.class, var1));
         } else {
            var2 = new JMSServerMBean[]{var1};
         }

         try {
            this.setJMSServers(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public JMSServerMBean[] getJMSServers() {
      return this._JMSServers;
   }

   public boolean isJMSServersSet() {
      return this._isSet(67);
   }

   public void removeJMSServer(JMSServerMBean var1) {
      this.destroyJMSServer(var1);
   }

   public void setJMSServers(JMSServerMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new JMSServerMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 67)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      JMSServerMBean[] var5 = this._JMSServers;
      this._JMSServers = (JMSServerMBean[])var4;
      this._postSet(67, var5, var4);
   }

   public JMSServerMBean createJMSServer(String var1) {
      JMSServerMBeanImpl var2 = new JMSServerMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addJMSServer(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyJMSServer(JMSServerMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 67);
         JMSServerMBean[] var2 = this.getJMSServers();
         JMSServerMBean[] var3 = (JMSServerMBean[])((JMSServerMBean[])this._getHelper()._removeElement(var2, JMSServerMBean.class, var1));
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
               this.setJMSServers(var3);
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

   public JMSServerMBean lookupJMSServer(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._JMSServers).iterator();

      JMSServerMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (JMSServerMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addJMSStore(JMSStoreMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 68)) {
         JMSStoreMBean[] var2 = (JMSStoreMBean[])((JMSStoreMBean[])this._getHelper()._extendArray(this.getJMSStores(), JMSStoreMBean.class, var1));

         try {
            this.setJMSStores(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public JMSStoreMBean[] getJMSStores() {
      return this._JMSStores;
   }

   public boolean isJMSStoresSet() {
      return this._isSet(68);
   }

   public void removeJMSStore(JMSStoreMBean var1) {
      JMSStoreMBean[] var2 = this.getJMSStores();
      JMSStoreMBean[] var3 = (JMSStoreMBean[])((JMSStoreMBean[])this._getHelper()._removeElement(var2, JMSStoreMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setJMSStores(var3);
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            }

            throw new UndeclaredThrowableException(var5);
         }
      }

   }

   public void setJMSStores(JMSStoreMBean[] var1) throws InvalidAttributeValueException {
      Object var2 = var1 == null ? new JMSStoreMBeanImpl[0] : var1;
      this._JMSStores = (JMSStoreMBean[])var2;
   }

   public JMSStoreMBean lookupJMSStore(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._JMSStores).iterator();

      JMSStoreMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (JMSStoreMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addJMSJDBCStore(JMSJDBCStoreMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 69)) {
         JMSJDBCStoreMBean[] var2;
         if (this._isSet(69)) {
            var2 = (JMSJDBCStoreMBean[])((JMSJDBCStoreMBean[])this._getHelper()._extendArray(this.getJMSJDBCStores(), JMSJDBCStoreMBean.class, var1));
         } else {
            var2 = new JMSJDBCStoreMBean[]{var1};
         }

         try {
            this.setJMSJDBCStores(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public JMSJDBCStoreMBean[] getJMSJDBCStores() {
      return this._JMSJDBCStores;
   }

   public boolean isJMSJDBCStoresSet() {
      return this._isSet(69);
   }

   public void removeJMSJDBCStore(JMSJDBCStoreMBean var1) {
      this.destroyJMSJDBCStore(var1);
   }

   public void setJMSJDBCStores(JMSJDBCStoreMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new JMSJDBCStoreMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 69)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      JMSJDBCStoreMBean[] var5 = this._JMSJDBCStores;
      this._JMSJDBCStores = (JMSJDBCStoreMBean[])var4;
      this._postSet(69, var5, var4);
   }

   public JMSJDBCStoreMBean createJMSJDBCStore(String var1) {
      JMSJDBCStoreMBeanImpl var2 = new JMSJDBCStoreMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addJMSJDBCStore(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyJMSJDBCStore(JMSJDBCStoreMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 69);
         JMSJDBCStoreMBean[] var2 = this.getJMSJDBCStores();
         JMSJDBCStoreMBean[] var3 = (JMSJDBCStoreMBean[])((JMSJDBCStoreMBean[])this._getHelper()._removeElement(var2, JMSJDBCStoreMBean.class, var1));
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
               this.setJMSJDBCStores(var3);
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

   public JMSJDBCStoreMBean lookupJMSJDBCStore(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._JMSJDBCStores).iterator();

      JMSJDBCStoreMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (JMSJDBCStoreMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addJMSFileStore(JMSFileStoreMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 70)) {
         JMSFileStoreMBean[] var2;
         if (this._isSet(70)) {
            var2 = (JMSFileStoreMBean[])((JMSFileStoreMBean[])this._getHelper()._extendArray(this.getJMSFileStores(), JMSFileStoreMBean.class, var1));
         } else {
            var2 = new JMSFileStoreMBean[]{var1};
         }

         try {
            this.setJMSFileStores(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public JMSFileStoreMBean[] getJMSFileStores() {
      return this._JMSFileStores;
   }

   public boolean isJMSFileStoresSet() {
      return this._isSet(70);
   }

   public void removeJMSFileStore(JMSFileStoreMBean var1) {
      this.destroyJMSFileStore(var1);
   }

   public void setJMSFileStores(JMSFileStoreMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new JMSFileStoreMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 70)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      JMSFileStoreMBean[] var5 = this._JMSFileStores;
      this._JMSFileStores = (JMSFileStoreMBean[])var4;
      this._postSet(70, var5, var4);
   }

   public JMSFileStoreMBean createJMSFileStore(String var1) {
      JMSFileStoreMBeanImpl var2 = new JMSFileStoreMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addJMSFileStore(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyJMSFileStore(JMSFileStoreMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 70);
         JMSFileStoreMBean[] var2 = this.getJMSFileStores();
         JMSFileStoreMBean[] var3 = (JMSFileStoreMBean[])((JMSFileStoreMBean[])this._getHelper()._removeElement(var2, JMSFileStoreMBean.class, var1));
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
               this.setJMSFileStores(var3);
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

   public JMSFileStoreMBean lookupJMSFileStore(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._JMSFileStores).iterator();

      JMSFileStoreMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (JMSFileStoreMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addJMSDestination(JMSDestinationMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 71)) {
         JMSDestinationMBean[] var2;
         if (this._isSet(71)) {
            var2 = (JMSDestinationMBean[])((JMSDestinationMBean[])this._getHelper()._extendArray(this.getJMSDestinations(), JMSDestinationMBean.class, var1));
         } else {
            var2 = new JMSDestinationMBean[]{var1};
         }

         try {
            this.setJMSDestinations(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public JMSDestinationMBean[] getJMSDestinations() {
      return this._JMSDestinations;
   }

   public String getJMSDestinationsAsString() {
      return this._getHelper()._serializeKeyList(this.getJMSDestinations());
   }

   public boolean isJMSDestinationsSet() {
      return this._isSet(71);
   }

   public void removeJMSDestination(JMSDestinationMBean var1) {
      JMSDestinationMBean[] var2 = this.getJMSDestinations();
      JMSDestinationMBean[] var3 = (JMSDestinationMBean[])((JMSDestinationMBean[])this._getHelper()._removeElement(var2, JMSDestinationMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setJMSDestinations(var3);
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            }

            throw new UndeclaredThrowableException(var5);
         }
      }

   }

   public void setJMSDestinationsAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         String[] var13 = this._getHelper()._splitKeyList(var1);
         List var3 = this._getHelper()._getKeyList(this._JMSDestinations);

         String var5;
         for(int var4 = 0; var4 < var13.length; ++var4) {
            var5 = var13[var4];
            var5 = var5 == null ? null : var5.trim();
            if (var3.contains(var5)) {
               var3.remove(var5);
            } else {
               this._getReferenceManager().registerUnresolvedReference(var5, JMSDestinationMBean.class, new ReferenceManager.Resolver(this, 71) {
                  public void resolveReference(Object var1) {
                     try {
                        DomainMBeanImpl.this.addJMSDestination((JMSDestinationMBean)var1);
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
               JMSDestinationMBean[] var6 = this._JMSDestinations;
               int var7 = var6.length;

               for(int var8 = 0; var8 < var7; ++var8) {
                  JMSDestinationMBean var9 = var6[var8];
                  if (var5.equals(var9.getName())) {
                     try {
                        this.removeJMSDestination(var9);
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
         JMSDestinationMBean[] var2 = this._JMSDestinations;
         this._initializeProperty(71);
         this._postSet(71, var2, this._JMSDestinations);
      }
   }

   public void setJMSDestinations(JMSDestinationMBean[] var1) throws InvalidAttributeValueException {
      Object var3 = var1 == null ? new JMSDestinationMBeanImpl[0] : var1;
      JMSDestinationMBean[] var2 = this._JMSDestinations;
      this._JMSDestinations = (JMSDestinationMBean[])var3;
      this._postSet(71, var2, var3);
   }

   public JMSDestinationMBean lookupJMSDestination(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._JMSDestinations).iterator();

      JMSDestinationMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (JMSDestinationMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addJMSQueue(JMSQueueMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 72)) {
         JMSQueueMBean[] var2;
         if (this._isSet(72)) {
            var2 = (JMSQueueMBean[])((JMSQueueMBean[])this._getHelper()._extendArray(this.getJMSQueues(), JMSQueueMBean.class, var1));
         } else {
            var2 = new JMSQueueMBean[]{var1};
         }

         try {
            this.setJMSQueues(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public JMSQueueMBean[] getJMSQueues() {
      return this._JMSQueues;
   }

   public boolean isJMSQueuesSet() {
      return this._isSet(72);
   }

   public void removeJMSQueue(JMSQueueMBean var1) {
      this.destroyJMSQueue(var1);
   }

   public void setJMSQueues(JMSQueueMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new JMSQueueMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 72)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      JMSQueueMBean[] var5 = this._JMSQueues;
      this._JMSQueues = (JMSQueueMBean[])var4;
      this._postSet(72, var5, var4);
   }

   public JMSQueueMBean createJMSQueue(String var1) {
      JMSQueueMBeanImpl var2 = new JMSQueueMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addJMSQueue(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyJMSQueue(JMSQueueMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 72);
         JMSQueueMBean[] var2 = this.getJMSQueues();
         JMSQueueMBean[] var3 = (JMSQueueMBean[])((JMSQueueMBean[])this._getHelper()._removeElement(var2, JMSQueueMBean.class, var1));
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
               this.setJMSQueues(var3);
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

   public JMSQueueMBean lookupJMSQueue(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._JMSQueues).iterator();

      JMSQueueMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (JMSQueueMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addJMSTopic(JMSTopicMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 73)) {
         JMSTopicMBean[] var2;
         if (this._isSet(73)) {
            var2 = (JMSTopicMBean[])((JMSTopicMBean[])this._getHelper()._extendArray(this.getJMSTopics(), JMSTopicMBean.class, var1));
         } else {
            var2 = new JMSTopicMBean[]{var1};
         }

         try {
            this.setJMSTopics(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public JMSTopicMBean[] getJMSTopics() {
      return this._JMSTopics;
   }

   public boolean isJMSTopicsSet() {
      return this._isSet(73);
   }

   public void removeJMSTopic(JMSTopicMBean var1) {
      this.destroyJMSTopic(var1);
   }

   public void setJMSTopics(JMSTopicMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new JMSTopicMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 73)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      JMSTopicMBean[] var5 = this._JMSTopics;
      this._JMSTopics = (JMSTopicMBean[])var4;
      this._postSet(73, var5, var4);
   }

   public JMSTopicMBean createJMSTopic(String var1) {
      JMSTopicMBeanImpl var2 = new JMSTopicMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addJMSTopic(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyJMSTopic(JMSTopicMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 73);
         JMSTopicMBean[] var2 = this.getJMSTopics();
         JMSTopicMBean[] var3 = (JMSTopicMBean[])((JMSTopicMBean[])this._getHelper()._removeElement(var2, JMSTopicMBean.class, var1));
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
               this.setJMSTopics(var3);
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

   public JMSTopicMBean lookupJMSTopic(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._JMSTopics).iterator();

      JMSTopicMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (JMSTopicMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addJMSDistributedQueue(JMSDistributedQueueMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 74)) {
         JMSDistributedQueueMBean[] var2;
         if (this._isSet(74)) {
            var2 = (JMSDistributedQueueMBean[])((JMSDistributedQueueMBean[])this._getHelper()._extendArray(this.getJMSDistributedQueues(), JMSDistributedQueueMBean.class, var1));
         } else {
            var2 = new JMSDistributedQueueMBean[]{var1};
         }

         try {
            this.setJMSDistributedQueues(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public JMSDistributedQueueMBean[] getJMSDistributedQueues() {
      return this._JMSDistributedQueues;
   }

   public boolean isJMSDistributedQueuesSet() {
      return this._isSet(74);
   }

   public void removeJMSDistributedQueue(JMSDistributedQueueMBean var1) {
      this.destroyJMSDistributedQueue(var1);
   }

   public void setJMSDistributedQueues(JMSDistributedQueueMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new JMSDistributedQueueMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 74)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      JMSDistributedQueueMBean[] var5 = this._JMSDistributedQueues;
      this._JMSDistributedQueues = (JMSDistributedQueueMBean[])var4;
      this._postSet(74, var5, var4);
   }

   public JMSDistributedQueueMBean createJMSDistributedQueue(String var1) {
      JMSDistributedQueueMBeanImpl var2 = new JMSDistributedQueueMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addJMSDistributedQueue(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyJMSDistributedQueue(JMSDistributedQueueMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 74);
         JMSDistributedQueueMBean[] var2 = this.getJMSDistributedQueues();
         JMSDistributedQueueMBean[] var3 = (JMSDistributedQueueMBean[])((JMSDistributedQueueMBean[])this._getHelper()._removeElement(var2, JMSDistributedQueueMBean.class, var1));
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
               this.setJMSDistributedQueues(var3);
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

   public JMSDistributedQueueMBean lookupJMSDistributedQueue(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._JMSDistributedQueues).iterator();

      JMSDistributedQueueMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (JMSDistributedQueueMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addJMSDistributedTopic(JMSDistributedTopicMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 75)) {
         JMSDistributedTopicMBean[] var2;
         if (this._isSet(75)) {
            var2 = (JMSDistributedTopicMBean[])((JMSDistributedTopicMBean[])this._getHelper()._extendArray(this.getJMSDistributedTopics(), JMSDistributedTopicMBean.class, var1));
         } else {
            var2 = new JMSDistributedTopicMBean[]{var1};
         }

         try {
            this.setJMSDistributedTopics(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public JMSDistributedTopicMBean[] getJMSDistributedTopics() {
      return this._JMSDistributedTopics;
   }

   public boolean isJMSDistributedTopicsSet() {
      return this._isSet(75);
   }

   public void removeJMSDistributedTopic(JMSDistributedTopicMBean var1) {
      this.destroyJMSDistributedTopic(var1);
   }

   public void setJMSDistributedTopics(JMSDistributedTopicMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new JMSDistributedTopicMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 75)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      JMSDistributedTopicMBean[] var5 = this._JMSDistributedTopics;
      this._JMSDistributedTopics = (JMSDistributedTopicMBean[])var4;
      this._postSet(75, var5, var4);
   }

   public JMSDistributedTopicMBean createJMSDistributedTopic(String var1) {
      JMSDistributedTopicMBeanImpl var2 = new JMSDistributedTopicMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addJMSDistributedTopic(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyJMSDistributedTopic(JMSDistributedTopicMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 75);
         JMSDistributedTopicMBean[] var2 = this.getJMSDistributedTopics();
         JMSDistributedTopicMBean[] var3 = (JMSDistributedTopicMBean[])((JMSDistributedTopicMBean[])this._getHelper()._removeElement(var2, JMSDistributedTopicMBean.class, var1));
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
               this.setJMSDistributedTopics(var3);
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

   public JMSDistributedTopicMBean lookupJMSDistributedTopic(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._JMSDistributedTopics).iterator();

      JMSDistributedTopicMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (JMSDistributedTopicMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addJMSTemplate(JMSTemplateMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 76)) {
         JMSTemplateMBean[] var2;
         if (this._isSet(76)) {
            var2 = (JMSTemplateMBean[])((JMSTemplateMBean[])this._getHelper()._extendArray(this.getJMSTemplates(), JMSTemplateMBean.class, var1));
         } else {
            var2 = new JMSTemplateMBean[]{var1};
         }

         try {
            this.setJMSTemplates(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public JMSTemplateMBean[] getJMSTemplates() {
      return this._JMSTemplates;
   }

   public boolean isJMSTemplatesSet() {
      return this._isSet(76);
   }

   public void removeJMSTemplate(JMSTemplateMBean var1) {
      this.destroyJMSTemplate(var1);
   }

   public void setJMSTemplates(JMSTemplateMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new JMSTemplateMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 76)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      JMSTemplateMBean[] var5 = this._JMSTemplates;
      this._JMSTemplates = (JMSTemplateMBean[])var4;
      this._postSet(76, var5, var4);
   }

   public JMSTemplateMBean createJMSTemplate(String var1) {
      JMSTemplateMBeanImpl var2 = new JMSTemplateMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addJMSTemplate(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyJMSTemplate(JMSTemplateMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 76);
         JMSTemplateMBean[] var2 = this.getJMSTemplates();
         JMSTemplateMBean[] var3 = (JMSTemplateMBean[])((JMSTemplateMBean[])this._getHelper()._removeElement(var2, JMSTemplateMBean.class, var1));
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
               this.setJMSTemplates(var3);
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

   public JMSTemplateMBean lookupJMSTemplate(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._JMSTemplates).iterator();

      JMSTemplateMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (JMSTemplateMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addNetworkChannel(NetworkChannelMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 77)) {
         NetworkChannelMBean[] var2;
         if (this._isSet(77)) {
            var2 = (NetworkChannelMBean[])((NetworkChannelMBean[])this._getHelper()._extendArray(this.getNetworkChannels(), NetworkChannelMBean.class, var1));
         } else {
            var2 = new NetworkChannelMBean[]{var1};
         }

         try {
            this.setNetworkChannels(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public NetworkChannelMBean[] getNetworkChannels() {
      return this._NetworkChannels;
   }

   public boolean isNetworkChannelsSet() {
      return this._isSet(77);
   }

   public void removeNetworkChannel(NetworkChannelMBean var1) {
      this.destroyNetworkChannel(var1);
   }

   public void setNetworkChannels(NetworkChannelMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new NetworkChannelMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 77)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      NetworkChannelMBean[] var5 = this._NetworkChannels;
      this._NetworkChannels = (NetworkChannelMBean[])var4;
      this._postSet(77, var5, var4);
   }

   public NetworkChannelMBean createNetworkChannel(String var1) {
      NetworkChannelMBeanImpl var2 = new NetworkChannelMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addNetworkChannel(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyNetworkChannel(NetworkChannelMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 77);
         NetworkChannelMBean[] var2 = this.getNetworkChannels();
         NetworkChannelMBean[] var3 = (NetworkChannelMBean[])((NetworkChannelMBean[])this._getHelper()._removeElement(var2, NetworkChannelMBean.class, var1));
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
               this.setNetworkChannels(var3);
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

   public NetworkChannelMBean lookupNetworkChannel(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._NetworkChannels).iterator();

      NetworkChannelMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (NetworkChannelMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addVirtualHost(VirtualHostMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 78)) {
         VirtualHostMBean[] var2;
         if (this._isSet(78)) {
            var2 = (VirtualHostMBean[])((VirtualHostMBean[])this._getHelper()._extendArray(this.getVirtualHosts(), VirtualHostMBean.class, var1));
         } else {
            var2 = new VirtualHostMBean[]{var1};
         }

         try {
            this.setVirtualHosts(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public VirtualHostMBean[] getVirtualHosts() {
      return this._VirtualHosts;
   }

   public boolean isVirtualHostsSet() {
      return this._isSet(78);
   }

   public void removeVirtualHost(VirtualHostMBean var1) {
      this.destroyVirtualHost(var1);
   }

   public void setVirtualHosts(VirtualHostMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new VirtualHostMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 78)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      VirtualHostMBean[] var5 = this._VirtualHosts;
      this._VirtualHosts = (VirtualHostMBean[])var4;
      this._postSet(78, var5, var4);
   }

   public VirtualHostMBean createVirtualHost(String var1) {
      VirtualHostMBeanImpl var2 = new VirtualHostMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addVirtualHost(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyVirtualHost(VirtualHostMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 78);
         VirtualHostMBean[] var2 = this.getVirtualHosts();
         VirtualHostMBean[] var3 = (VirtualHostMBean[])((VirtualHostMBean[])this._getHelper()._removeElement(var2, VirtualHostMBean.class, var1));
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
               this.setVirtualHosts(var3);
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

   public VirtualHostMBean lookupVirtualHost(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._VirtualHosts).iterator();

      VirtualHostMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (VirtualHostMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addMigratableTarget(MigratableTargetMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 79)) {
         MigratableTargetMBean[] var2;
         if (this._isSet(79)) {
            var2 = (MigratableTargetMBean[])((MigratableTargetMBean[])this._getHelper()._extendArray(this.getMigratableTargets(), MigratableTargetMBean.class, var1));
         } else {
            var2 = new MigratableTargetMBean[]{var1};
         }

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
      return this._MigratableTargets;
   }

   public boolean isMigratableTargetsSet() {
      return this._isSet(79);
   }

   public void removeMigratableTarget(MigratableTargetMBean var1) {
      this.destroyMigratableTarget(var1);
   }

   public void setMigratableTargets(MigratableTargetMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new MigratableTargetMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 79)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      MigratableTargetMBean[] var5 = this._MigratableTargets;
      this._MigratableTargets = (MigratableTargetMBean[])var4;
      this._postSet(79, var5, var4);
   }

   public MigratableTargetMBean createMigratableTarget(String var1) {
      MigratableTargetMBeanImpl var2 = new MigratableTargetMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addMigratableTarget(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyMigratableTarget(MigratableTargetMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 79);
         MigratableTargetMBean[] var2 = this.getMigratableTargets();
         MigratableTargetMBean[] var3 = (MigratableTargetMBean[])((MigratableTargetMBean[])this._getHelper()._removeElement(var2, MigratableTargetMBean.class, var1));
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
               this.setMigratableTargets(var3);
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

   public MigratableTargetMBean lookupMigratableTarget(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._MigratableTargets).iterator();

      MigratableTargetMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (MigratableTargetMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public EJBContainerMBean getEJBContainer() {
      return this._EJBContainer;
   }

   public boolean isEJBContainerSet() {
      return this._isSet(80);
   }

   public void setEJBContainer(EJBContainerMBean var1) throws InvalidAttributeValueException {
      if (var1 != null && this.getEJBContainer() != null && var1 != this.getEJBContainer()) {
         throw new BeanAlreadyExistsException(this.getEJBContainer() + " has already been created");
      } else {
         if (var1 != null) {
            AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
            if (this._setParent(var2, this, 80)) {
               this._getReferenceManager().registerBean(var2, true);
               this._postCreate(var2);
            }
         }

         EJBContainerMBean var3 = this._EJBContainer;
         this._EJBContainer = var1;
         this._postSet(80, var3, var1);
      }
   }

   public EJBContainerMBean createEJBContainer() {
      EJBContainerMBeanImpl var1 = new EJBContainerMBeanImpl(this, -1);

      try {
         this.setEJBContainer(var1);
         return var1;
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public void destroyEJBContainer() {
      try {
         AbstractDescriptorBean var1 = (AbstractDescriptorBean)this._EJBContainer;
         if (var1 != null) {
            List var2 = this._getReferenceManager().getResolvedReferences(var1);
            if (var2 != null && var2.size() > 0) {
               throw new BeanRemoveRejectedException(var1, var2);
            } else {
               this._getReferenceManager().unregisterBean(var1);
               this._markDestroyed(var1);
               this.setEJBContainer((EJBContainerMBean)null);
               this._unSet(80);
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

   public WebAppContainerMBean getWebAppContainer() {
      return this._WebAppContainer;
   }

   public boolean isWebAppContainerSet() {
      return this._isSet(81) || this._isAnythingSet((AbstractDescriptorBean)this.getWebAppContainer());
   }

   public void setWebAppContainer(WebAppContainerMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 81)) {
         this._postCreate(var2);
      }

      WebAppContainerMBean var3 = this._WebAppContainer;
      this._WebAppContainer = var1;
      this._postSet(81, var3, var1);
   }

   public JMXMBean getJMX() {
      return this._JMX;
   }

   public boolean isJMXSet() {
      return this._isSet(82) || this._isAnythingSet((AbstractDescriptorBean)this.getJMX());
   }

   public void setJMX(JMXMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 82)) {
         this._postCreate(var2);
      }

      JMXMBean var3 = this._JMX;
      this._JMX = var1;
      this._postSet(82, var3, var1);
   }

   public SelfTuningMBean getSelfTuning() {
      return this._SelfTuning;
   }

   public boolean isSelfTuningSet() {
      return this._isSet(83) || this._isAnythingSet((AbstractDescriptorBean)this.getSelfTuning());
   }

   public void setSelfTuning(SelfTuningMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 83)) {
         this._postCreate(var2);
      }

      SelfTuningMBean var3 = this._SelfTuning;
      this._SelfTuning = var1;
      this._postSet(83, var3, var1);
   }

   public void addPathService(PathServiceMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 84)) {
         PathServiceMBean[] var2;
         if (this._isSet(84)) {
            var2 = (PathServiceMBean[])((PathServiceMBean[])this._getHelper()._extendArray(this.getPathServices(), PathServiceMBean.class, var1));
         } else {
            var2 = new PathServiceMBean[]{var1};
         }

         try {
            this.setPathServices(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public PathServiceMBean[] getPathServices() {
      return this._PathServices;
   }

   public boolean isPathServicesSet() {
      return this._isSet(84);
   }

   public void removePathService(PathServiceMBean var1) {
      this.destroyPathService(var1);
   }

   public void setPathServices(PathServiceMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new PathServiceMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 84)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      PathServiceMBean[] var5 = this._PathServices;
      this._PathServices = (PathServiceMBean[])var4;
      this._postSet(84, var5, var4);
   }

   public PathServiceMBean createPathService(String var1) {
      PathServiceMBeanImpl var2 = new PathServiceMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addPathService(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyPathService(PathServiceMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 84);
         PathServiceMBean[] var2 = this.getPathServices();
         PathServiceMBean[] var3 = (PathServiceMBean[])((PathServiceMBean[])this._getHelper()._removeElement(var2, PathServiceMBean.class, var1));
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
               this.setPathServices(var3);
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

   public PathServiceMBean lookupPathService(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._PathServices).iterator();

      PathServiceMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (PathServiceMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addJMSDestinationKey(JMSDestinationKeyMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 85)) {
         JMSDestinationKeyMBean[] var2;
         if (this._isSet(85)) {
            var2 = (JMSDestinationKeyMBean[])((JMSDestinationKeyMBean[])this._getHelper()._extendArray(this.getJMSDestinationKeys(), JMSDestinationKeyMBean.class, var1));
         } else {
            var2 = new JMSDestinationKeyMBean[]{var1};
         }

         try {
            this.setJMSDestinationKeys(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public JMSDestinationKeyMBean[] getJMSDestinationKeys() {
      return this._JMSDestinationKeys;
   }

   public boolean isJMSDestinationKeysSet() {
      return this._isSet(85);
   }

   public void removeJMSDestinationKey(JMSDestinationKeyMBean var1) {
      this.destroyJMSDestinationKey(var1);
   }

   public void setJMSDestinationKeys(JMSDestinationKeyMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new JMSDestinationKeyMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 85)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      JMSDestinationKeyMBean[] var5 = this._JMSDestinationKeys;
      this._JMSDestinationKeys = (JMSDestinationKeyMBean[])var4;
      this._postSet(85, var5, var4);
   }

   public JMSDestinationKeyMBean createJMSDestinationKey(String var1) {
      JMSDestinationKeyMBeanImpl var2 = new JMSDestinationKeyMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addJMSDestinationKey(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyJMSDestinationKey(JMSDestinationKeyMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 85);
         JMSDestinationKeyMBean[] var2 = this.getJMSDestinationKeys();
         JMSDestinationKeyMBean[] var3 = (JMSDestinationKeyMBean[])((JMSDestinationKeyMBean[])this._getHelper()._removeElement(var2, JMSDestinationKeyMBean.class, var1));
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
               this.setJMSDestinationKeys(var3);
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

   public JMSDestinationKeyMBean lookupJMSDestinationKey(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._JMSDestinationKeys).iterator();

      JMSDestinationKeyMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (JMSDestinationKeyMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addJMSConnectionFactory(JMSConnectionFactoryMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 86)) {
         JMSConnectionFactoryMBean[] var2;
         if (this._isSet(86)) {
            var2 = (JMSConnectionFactoryMBean[])((JMSConnectionFactoryMBean[])this._getHelper()._extendArray(this.getJMSConnectionFactories(), JMSConnectionFactoryMBean.class, var1));
         } else {
            var2 = new JMSConnectionFactoryMBean[]{var1};
         }

         try {
            this.setJMSConnectionFactories(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public JMSConnectionFactoryMBean[] getJMSConnectionFactories() {
      return this._JMSConnectionFactories;
   }

   public boolean isJMSConnectionFactoriesSet() {
      return this._isSet(86);
   }

   public void removeJMSConnectionFactory(JMSConnectionFactoryMBean var1) {
      this.destroyJMSConnectionFactory(var1);
   }

   public void setJMSConnectionFactories(JMSConnectionFactoryMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new JMSConnectionFactoryMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 86)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      JMSConnectionFactoryMBean[] var5 = this._JMSConnectionFactories;
      this._JMSConnectionFactories = (JMSConnectionFactoryMBean[])var4;
      this._postSet(86, var5, var4);
   }

   public JMSConnectionFactoryMBean createJMSConnectionFactory(String var1) {
      JMSConnectionFactoryMBeanImpl var2 = new JMSConnectionFactoryMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addJMSConnectionFactory(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyJMSConnectionFactory(JMSConnectionFactoryMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 86);
         JMSConnectionFactoryMBean[] var2 = this.getJMSConnectionFactories();
         JMSConnectionFactoryMBean[] var3 = (JMSConnectionFactoryMBean[])((JMSConnectionFactoryMBean[])this._getHelper()._removeElement(var2, JMSConnectionFactoryMBean.class, var1));
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
               this.setJMSConnectionFactories(var3);
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

   public JMSConnectionFactoryMBean lookupJMSConnectionFactory(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._JMSConnectionFactories).iterator();

      JMSConnectionFactoryMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (JMSConnectionFactoryMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addJMSSessionPool(JMSSessionPoolMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 87)) {
         JMSSessionPoolMBean[] var2;
         if (this._isSet(87)) {
            var2 = (JMSSessionPoolMBean[])((JMSSessionPoolMBean[])this._getHelper()._extendArray(this.getJMSSessionPools(), JMSSessionPoolMBean.class, var1));
         } else {
            var2 = new JMSSessionPoolMBean[]{var1};
         }

         try {
            this.setJMSSessionPools(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public JMSSessionPoolMBean[] getJMSSessionPools() {
      return this._JMSSessionPools;
   }

   public boolean isJMSSessionPoolsSet() {
      return this._isSet(87);
   }

   public void removeJMSSessionPool(JMSSessionPoolMBean var1) {
      this.destroyJMSSessionPool(var1);
   }

   public void setJMSSessionPools(JMSSessionPoolMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new JMSSessionPoolMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 87)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      JMSSessionPoolMBean[] var5 = this._JMSSessionPools;
      this._JMSSessionPools = (JMSSessionPoolMBean[])var4;
      this._postSet(87, var5, var4);
   }

   public JMSSessionPoolMBean createJMSSessionPool(String var1) {
      JMSSessionPoolMBeanImpl var2 = new JMSSessionPoolMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addJMSSessionPool(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public JMSSessionPoolMBean createJMSSessionPool(String var1, JMSSessionPoolMBean var2) {
      return this._customizer.createJMSSessionPool(var1, var2);
   }

   public void destroyJMSSessionPool(JMSSessionPoolMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 87);
         JMSSessionPoolMBean[] var2 = this.getJMSSessionPools();
         JMSSessionPoolMBean[] var3 = (JMSSessionPoolMBean[])((JMSSessionPoolMBean[])this._getHelper()._removeElement(var2, JMSSessionPoolMBean.class, var1));
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
               this.setJMSSessionPools(var3);
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

   public JMSSessionPoolMBean lookupJMSSessionPool(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._JMSSessionPools).iterator();

      JMSSessionPoolMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (JMSSessionPoolMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public JMSBridgeDestinationMBean createJMSBridgeDestination(String var1) {
      JMSBridgeDestinationMBeanImpl var2 = new JMSBridgeDestinationMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addJMSBridgeDestination(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyJMSBridgeDestination(JMSBridgeDestinationMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 88);
         JMSBridgeDestinationMBean[] var2 = this.getJMSBridgeDestinations();
         JMSBridgeDestinationMBean[] var3 = (JMSBridgeDestinationMBean[])((JMSBridgeDestinationMBean[])this._getHelper()._removeElement(var2, JMSBridgeDestinationMBean.class, var1));
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
               this.setJMSBridgeDestinations(var3);
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

   public JMSBridgeDestinationMBean lookupJMSBridgeDestination(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._JMSBridgeDestinations).iterator();

      JMSBridgeDestinationMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (JMSBridgeDestinationMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addJMSBridgeDestination(JMSBridgeDestinationMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 88)) {
         JMSBridgeDestinationMBean[] var2;
         if (this._isSet(88)) {
            var2 = (JMSBridgeDestinationMBean[])((JMSBridgeDestinationMBean[])this._getHelper()._extendArray(this.getJMSBridgeDestinations(), JMSBridgeDestinationMBean.class, var1));
         } else {
            var2 = new JMSBridgeDestinationMBean[]{var1};
         }

         try {
            this.setJMSBridgeDestinations(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public JMSBridgeDestinationMBean[] getJMSBridgeDestinations() {
      return this._JMSBridgeDestinations;
   }

   public boolean isJMSBridgeDestinationsSet() {
      return this._isSet(88);
   }

   public void removeJMSBridgeDestination(JMSBridgeDestinationMBean var1) {
      this.destroyJMSBridgeDestination(var1);
   }

   public void setJMSBridgeDestinations(JMSBridgeDestinationMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new JMSBridgeDestinationMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 88)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      JMSBridgeDestinationMBean[] var5 = this._JMSBridgeDestinations;
      this._JMSBridgeDestinations = (JMSBridgeDestinationMBean[])var4;
      this._postSet(88, var5, var4);
   }

   public BridgeDestinationMBean createBridgeDestination(String var1) {
      BridgeDestinationMBeanImpl var2 = new BridgeDestinationMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addBridgeDestination(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyBridgeDestination(BridgeDestinationMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 89);
         BridgeDestinationMBean[] var2 = this.getBridgeDestinations();
         BridgeDestinationMBean[] var3 = (BridgeDestinationMBean[])((BridgeDestinationMBean[])this._getHelper()._removeElement(var2, BridgeDestinationMBean.class, var1));
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
               this.setBridgeDestinations(var3);
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

   public BridgeDestinationMBean lookupBridgeDestination(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._BridgeDestinations).iterator();

      BridgeDestinationMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (BridgeDestinationMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addBridgeDestination(BridgeDestinationMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 89)) {
         BridgeDestinationMBean[] var2;
         if (this._isSet(89)) {
            var2 = (BridgeDestinationMBean[])((BridgeDestinationMBean[])this._getHelper()._extendArray(this.getBridgeDestinations(), BridgeDestinationMBean.class, var1));
         } else {
            var2 = new BridgeDestinationMBean[]{var1};
         }

         try {
            this.setBridgeDestinations(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public BridgeDestinationMBean[] getBridgeDestinations() {
      return this._BridgeDestinations;
   }

   public boolean isBridgeDestinationsSet() {
      return this._isSet(89);
   }

   public void removeBridgeDestination(BridgeDestinationMBean var1) {
      this.destroyBridgeDestination(var1);
   }

   public void setBridgeDestinations(BridgeDestinationMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new BridgeDestinationMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 89)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      BridgeDestinationMBean[] var5 = this._BridgeDestinations;
      this._BridgeDestinations = (BridgeDestinationMBean[])var4;
      this._postSet(89, var5, var4);
   }

   public void addForeignJMSServer(ForeignJMSServerMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 90)) {
         ForeignJMSServerMBean[] var2;
         if (this._isSet(90)) {
            var2 = (ForeignJMSServerMBean[])((ForeignJMSServerMBean[])this._getHelper()._extendArray(this.getForeignJMSServers(), ForeignJMSServerMBean.class, var1));
         } else {
            var2 = new ForeignJMSServerMBean[]{var1};
         }

         try {
            this.setForeignJMSServers(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public ForeignJMSServerMBean[] getForeignJMSServers() {
      return this._ForeignJMSServers;
   }

   public boolean isForeignJMSServersSet() {
      return this._isSet(90);
   }

   public void removeForeignJMSServer(ForeignJMSServerMBean var1) {
      this.destroyForeignJMSServer(var1);
   }

   public void setForeignJMSServers(ForeignJMSServerMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new ForeignJMSServerMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 90)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      ForeignJMSServerMBean[] var5 = this._ForeignJMSServers;
      this._ForeignJMSServers = (ForeignJMSServerMBean[])var4;
      this._postSet(90, var5, var4);
   }

   public ForeignJMSServerMBean createForeignJMSServer(String var1) {
      ForeignJMSServerMBeanImpl var2 = new ForeignJMSServerMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addForeignJMSServer(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyForeignJMSServer(ForeignJMSServerMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 90);
         ForeignJMSServerMBean[] var2 = this.getForeignJMSServers();
         ForeignJMSServerMBean[] var3 = (ForeignJMSServerMBean[])((ForeignJMSServerMBean[])this._getHelper()._removeElement(var2, ForeignJMSServerMBean.class, var1));
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
               this.setForeignJMSServers(var3);
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

   public ForeignJMSServerMBean lookupForeignJMSServer(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._ForeignJMSServers).iterator();

      ForeignJMSServerMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (ForeignJMSServerMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addShutdownClass(ShutdownClassMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 91)) {
         ShutdownClassMBean[] var2;
         if (this._isSet(91)) {
            var2 = (ShutdownClassMBean[])((ShutdownClassMBean[])this._getHelper()._extendArray(this.getShutdownClasses(), ShutdownClassMBean.class, var1));
         } else {
            var2 = new ShutdownClassMBean[]{var1};
         }

         try {
            this.setShutdownClasses(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public ShutdownClassMBean[] getShutdownClasses() {
      return this._ShutdownClasses;
   }

   public boolean isShutdownClassesSet() {
      return this._isSet(91);
   }

   public void removeShutdownClass(ShutdownClassMBean var1) {
      this.destroyShutdownClass(var1);
   }

   public void setShutdownClasses(ShutdownClassMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new ShutdownClassMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 91)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      ShutdownClassMBean[] var5 = this._ShutdownClasses;
      this._ShutdownClasses = (ShutdownClassMBean[])var4;
      this._postSet(91, var5, var4);
   }

   public ShutdownClassMBean createShutdownClass(String var1) {
      ShutdownClassMBeanImpl var2 = new ShutdownClassMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addShutdownClass(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyShutdownClass(ShutdownClassMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 91);
         ShutdownClassMBean[] var2 = this.getShutdownClasses();
         ShutdownClassMBean[] var3 = (ShutdownClassMBean[])((ShutdownClassMBean[])this._getHelper()._removeElement(var2, ShutdownClassMBean.class, var1));
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
               this.setShutdownClasses(var3);
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

   public ShutdownClassMBean lookupShutdownClass(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._ShutdownClasses).iterator();

      ShutdownClassMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (ShutdownClassMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addStartupClass(StartupClassMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 92)) {
         StartupClassMBean[] var2;
         if (this._isSet(92)) {
            var2 = (StartupClassMBean[])((StartupClassMBean[])this._getHelper()._extendArray(this.getStartupClasses(), StartupClassMBean.class, var1));
         } else {
            var2 = new StartupClassMBean[]{var1};
         }

         try {
            this.setStartupClasses(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public StartupClassMBean[] getStartupClasses() {
      return this._StartupClasses;
   }

   public boolean isStartupClassesSet() {
      return this._isSet(92);
   }

   public void removeStartupClass(StartupClassMBean var1) {
      this.destroyStartupClass(var1);
   }

   public void setStartupClasses(StartupClassMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new StartupClassMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 92)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      StartupClassMBean[] var5 = this._StartupClasses;
      this._StartupClasses = (StartupClassMBean[])var4;
      this._postSet(92, var5, var4);
   }

   public StartupClassMBean createStartupClass(String var1) {
      StartupClassMBeanImpl var2 = new StartupClassMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addStartupClass(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyStartupClass(StartupClassMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 92);
         StartupClassMBean[] var2 = this.getStartupClasses();
         StartupClassMBean[] var3 = (StartupClassMBean[])((StartupClassMBean[])this._getHelper()._removeElement(var2, StartupClassMBean.class, var1));
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
               this.setStartupClasses(var3);
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

   public StartupClassMBean lookupStartupClass(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._StartupClasses).iterator();

      StartupClassMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (StartupClassMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addSingletonService(SingletonServiceMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 93)) {
         SingletonServiceMBean[] var2;
         if (this._isSet(93)) {
            var2 = (SingletonServiceMBean[])((SingletonServiceMBean[])this._getHelper()._extendArray(this.getSingletonServices(), SingletonServiceMBean.class, var1));
         } else {
            var2 = new SingletonServiceMBean[]{var1};
         }

         try {
            this.setSingletonServices(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public SingletonServiceMBean[] getSingletonServices() {
      return this._SingletonServices;
   }

   public boolean isSingletonServicesSet() {
      return this._isSet(93);
   }

   public void removeSingletonService(SingletonServiceMBean var1) {
      this.destroySingletonService(var1);
   }

   public void setSingletonServices(SingletonServiceMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new SingletonServiceMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 93)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      SingletonServiceMBean[] var5 = this._SingletonServices;
      this._SingletonServices = (SingletonServiceMBean[])var4;
      this._postSet(93, var5, var4);
   }

   public SingletonServiceMBean createSingletonService(String var1) {
      SingletonServiceMBeanImpl var2 = new SingletonServiceMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addSingletonService(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroySingletonService(SingletonServiceMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 93);
         SingletonServiceMBean[] var2 = this.getSingletonServices();
         SingletonServiceMBean[] var3 = (SingletonServiceMBean[])((SingletonServiceMBean[])this._getHelper()._removeElement(var2, SingletonServiceMBean.class, var1));
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
               this.setSingletonServices(var3);
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

   public SingletonServiceMBean lookupSingletonService(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._SingletonServices).iterator();

      SingletonServiceMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (SingletonServiceMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addMailSession(MailSessionMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 94)) {
         MailSessionMBean[] var2;
         if (this._isSet(94)) {
            var2 = (MailSessionMBean[])((MailSessionMBean[])this._getHelper()._extendArray(this.getMailSessions(), MailSessionMBean.class, var1));
         } else {
            var2 = new MailSessionMBean[]{var1};
         }

         try {
            this.setMailSessions(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public MailSessionMBean[] getMailSessions() {
      return this._MailSessions;
   }

   public boolean isMailSessionsSet() {
      return this._isSet(94);
   }

   public void removeMailSession(MailSessionMBean var1) {
      this.destroyMailSession(var1);
   }

   public void setMailSessions(MailSessionMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new MailSessionMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 94)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      MailSessionMBean[] var5 = this._MailSessions;
      this._MailSessions = (MailSessionMBean[])var4;
      this._postSet(94, var5, var4);
   }

   public MailSessionMBean createMailSession(String var1) {
      MailSessionMBeanImpl var2 = new MailSessionMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addMailSession(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyMailSession(MailSessionMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 94);
         MailSessionMBean[] var2 = this.getMailSessions();
         MailSessionMBean[] var3 = (MailSessionMBean[])((MailSessionMBean[])this._getHelper()._removeElement(var2, MailSessionMBean.class, var1));
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
               this.setMailSessions(var3);
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

   public MailSessionMBean lookupMailSession(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._MailSessions).iterator();

      MailSessionMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (MailSessionMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addJoltConnectionPool(JoltConnectionPoolMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 95)) {
         JoltConnectionPoolMBean[] var2;
         if (this._isSet(95)) {
            var2 = (JoltConnectionPoolMBean[])((JoltConnectionPoolMBean[])this._getHelper()._extendArray(this.getJoltConnectionPools(), JoltConnectionPoolMBean.class, var1));
         } else {
            var2 = new JoltConnectionPoolMBean[]{var1};
         }

         try {
            this.setJoltConnectionPools(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public JoltConnectionPoolMBean[] getJoltConnectionPools() {
      return this._JoltConnectionPools;
   }

   public boolean isJoltConnectionPoolsSet() {
      return this._isSet(95);
   }

   public void removeJoltConnectionPool(JoltConnectionPoolMBean var1) {
      this.destroyJoltConnectionPool(var1);
   }

   public void setJoltConnectionPools(JoltConnectionPoolMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new JoltConnectionPoolMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 95)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      JoltConnectionPoolMBean[] var5 = this._JoltConnectionPools;
      this._JoltConnectionPools = (JoltConnectionPoolMBean[])var4;
      this._postSet(95, var5, var4);
   }

   public JoltConnectionPoolMBean createJoltConnectionPool(String var1) {
      JoltConnectionPoolMBeanImpl var2 = new JoltConnectionPoolMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addJoltConnectionPool(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyJoltConnectionPool(JoltConnectionPoolMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 95);
         JoltConnectionPoolMBean[] var2 = this.getJoltConnectionPools();
         JoltConnectionPoolMBean[] var3 = (JoltConnectionPoolMBean[])((JoltConnectionPoolMBean[])this._getHelper()._removeElement(var2, JoltConnectionPoolMBean.class, var1));
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
               this.setJoltConnectionPools(var3);
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

   public JoltConnectionPoolMBean lookupJoltConnectionPool(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._JoltConnectionPools).iterator();

      JoltConnectionPoolMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (JoltConnectionPoolMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addLogFilter(LogFilterMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 96)) {
         LogFilterMBean[] var2;
         if (this._isSet(96)) {
            var2 = (LogFilterMBean[])((LogFilterMBean[])this._getHelper()._extendArray(this.getLogFilters(), LogFilterMBean.class, var1));
         } else {
            var2 = new LogFilterMBean[]{var1};
         }

         try {
            this.setLogFilters(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public LogFilterMBean[] getLogFilters() {
      return this._LogFilters;
   }

   public boolean isLogFiltersSet() {
      return this._isSet(96);
   }

   public void removeLogFilter(LogFilterMBean var1) {
      this.destroyLogFilter(var1);
   }

   public void setLogFilters(LogFilterMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new LogFilterMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 96)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      LogFilterMBean[] var5 = this._LogFilters;
      this._LogFilters = (LogFilterMBean[])var4;
      this._postSet(96, var5, var4);
   }

   public LogFilterMBean createLogFilter(String var1) {
      LogFilterMBeanImpl var2 = new LogFilterMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addLogFilter(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyLogFilter(LogFilterMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 96);
         LogFilterMBean[] var2 = this.getLogFilters();
         LogFilterMBean[] var3 = (LogFilterMBean[])((LogFilterMBean[])this._getHelper()._removeElement(var2, LogFilterMBean.class, var1));
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
               this.setLogFilters(var3);
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

   public LogFilterMBean lookupLogFilter(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._LogFilters).iterator();

      LogFilterMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (LogFilterMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addDomainLogFilter(DomainLogFilterMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 97)) {
         DomainLogFilterMBean[] var2;
         if (this._isSet(97)) {
            var2 = (DomainLogFilterMBean[])((DomainLogFilterMBean[])this._getHelper()._extendArray(this.getDomainLogFilters(), DomainLogFilterMBean.class, var1));
         } else {
            var2 = new DomainLogFilterMBean[]{var1};
         }

         try {
            this.setDomainLogFilters(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public DomainLogFilterMBean[] getDomainLogFilters() {
      return this._DomainLogFilters;
   }

   public boolean isDomainLogFiltersSet() {
      return this._isSet(97);
   }

   public void removeDomainLogFilter(DomainLogFilterMBean var1) {
      this.destroyDomainLogFilter(var1);
   }

   public void setDomainLogFilters(DomainLogFilterMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new DomainLogFilterMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 97)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      DomainLogFilterMBean[] var5 = this._DomainLogFilters;
      this._DomainLogFilters = (DomainLogFilterMBean[])var4;
      this._postSet(97, var5, var4);
   }

   public DomainLogFilterMBean createDomainLogFilter(String var1) {
      DomainLogFilterMBeanImpl var2 = new DomainLogFilterMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addDomainLogFilter(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyDomainLogFilter(DomainLogFilterMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 97);
         DomainLogFilterMBean[] var2 = this.getDomainLogFilters();
         DomainLogFilterMBean[] var3 = (DomainLogFilterMBean[])((DomainLogFilterMBean[])this._getHelper()._removeElement(var2, DomainLogFilterMBean.class, var1));
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
               this.setDomainLogFilters(var3);
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

   public DomainLogFilterMBean lookupDomainLogFilter(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._DomainLogFilters).iterator();

      DomainLogFilterMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (DomainLogFilterMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addFileStore(FileStoreMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 98)) {
         FileStoreMBean[] var2;
         if (this._isSet(98)) {
            var2 = (FileStoreMBean[])((FileStoreMBean[])this._getHelper()._extendArray(this.getFileStores(), FileStoreMBean.class, var1));
         } else {
            var2 = new FileStoreMBean[]{var1};
         }

         try {
            this.setFileStores(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public FileStoreMBean[] getFileStores() {
      return this._FileStores;
   }

   public boolean isFileStoresSet() {
      return this._isSet(98);
   }

   public void removeFileStore(FileStoreMBean var1) {
      this.destroyFileStore(var1);
   }

   public void setFileStores(FileStoreMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new FileStoreMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 98)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      FileStoreMBean[] var5 = this._FileStores;
      this._FileStores = (FileStoreMBean[])var4;
      this._postSet(98, var5, var4);
   }

   public FileStoreMBean createFileStore(String var1) {
      FileStoreMBeanImpl var2 = new FileStoreMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addFileStore(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyFileStore(FileStoreMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 98);
         FileStoreMBean[] var2 = this.getFileStores();
         FileStoreMBean[] var3 = (FileStoreMBean[])((FileStoreMBean[])this._getHelper()._removeElement(var2, FileStoreMBean.class, var1));
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
               this.setFileStores(var3);
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

   public FileStoreMBean lookupFileStore(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._FileStores).iterator();

      FileStoreMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (FileStoreMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addJDBCStore(JDBCStoreMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 99)) {
         JDBCStoreMBean[] var2;
         if (this._isSet(99)) {
            var2 = (JDBCStoreMBean[])((JDBCStoreMBean[])this._getHelper()._extendArray(this.getJDBCStores(), JDBCStoreMBean.class, var1));
         } else {
            var2 = new JDBCStoreMBean[]{var1};
         }

         try {
            this.setJDBCStores(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public JDBCStoreMBean[] getJDBCStores() {
      return this._JDBCStores;
   }

   public boolean isJDBCStoresSet() {
      return this._isSet(99);
   }

   public void removeJDBCStore(JDBCStoreMBean var1) {
      this.destroyJDBCStore(var1);
   }

   public void setJDBCStores(JDBCStoreMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new JDBCStoreMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 99)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      JDBCStoreMBean[] var5 = this._JDBCStores;
      this._JDBCStores = (JDBCStoreMBean[])var4;
      this._postSet(99, var5, var4);
   }

   public JDBCStoreMBean createJDBCStore(String var1) {
      JDBCStoreMBeanImpl var2 = new JDBCStoreMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addJDBCStore(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyJDBCStore(JDBCStoreMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 99);
         JDBCStoreMBean[] var2 = this.getJDBCStores();
         JDBCStoreMBean[] var3 = (JDBCStoreMBean[])((JDBCStoreMBean[])this._getHelper()._removeElement(var2, JDBCStoreMBean.class, var1));
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
               this.setJDBCStores(var3);
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

   public JDBCStoreMBean lookupJDBCStore(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._JDBCStores).iterator();

      JDBCStoreMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (JDBCStoreMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addJMSInteropModule(JMSInteropModuleMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 100)) {
         JMSInteropModuleMBean[] var2;
         if (this._isSet(100)) {
            var2 = (JMSInteropModuleMBean[])((JMSInteropModuleMBean[])this._getHelper()._extendArray(this.getJMSInteropModules(), JMSInteropModuleMBean.class, var1));
         } else {
            var2 = new JMSInteropModuleMBean[]{var1};
         }

         try {
            this.setJMSInteropModules(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public JMSInteropModuleMBean[] getJMSInteropModules() {
      return this._JMSInteropModules;
   }

   public boolean isJMSInteropModulesSet() {
      return this._isSet(100);
   }

   public void removeJMSInteropModule(JMSInteropModuleMBean var1) {
      this.destroyJMSInteropModule(var1);
   }

   public void setJMSInteropModules(JMSInteropModuleMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new JMSInteropModuleMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 100)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      JMSInteropModuleMBean[] var5 = this._JMSInteropModules;
      this._JMSInteropModules = (JMSInteropModuleMBean[])var4;
      this._postSet(100, var5, var4);
   }

   public JMSInteropModuleMBean createJMSInteropModule(String var1) {
      JMSInteropModuleMBeanImpl var2 = new JMSInteropModuleMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addJMSInteropModule(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyJMSInteropModule(JMSInteropModuleMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 100);
         JMSInteropModuleMBean[] var2 = this.getJMSInteropModules();
         JMSInteropModuleMBean[] var3 = (JMSInteropModuleMBean[])((JMSInteropModuleMBean[])this._getHelper()._removeElement(var2, JMSInteropModuleMBean.class, var1));
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
               this.setJMSInteropModules(var3);
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

   public JMSInteropModuleMBean lookupJMSInteropModule(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._JMSInteropModules).iterator();

      JMSInteropModuleMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (JMSInteropModuleMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addJMSSystemResource(JMSSystemResourceMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 101)) {
         JMSSystemResourceMBean[] var2;
         if (this._isSet(101)) {
            var2 = (JMSSystemResourceMBean[])((JMSSystemResourceMBean[])this._getHelper()._extendArray(this.getJMSSystemResources(), JMSSystemResourceMBean.class, var1));
         } else {
            var2 = new JMSSystemResourceMBean[]{var1};
         }

         try {
            this.setJMSSystemResources(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public JMSSystemResourceMBean[] getJMSSystemResources() {
      return this._JMSSystemResources;
   }

   public boolean isJMSSystemResourcesSet() {
      return this._isSet(101);
   }

   public void removeJMSSystemResource(JMSSystemResourceMBean var1) {
      this.destroyJMSSystemResource(var1);
   }

   public void setJMSSystemResources(JMSSystemResourceMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new JMSSystemResourceMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 101)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      JMSSystemResourceMBean[] var5 = this._JMSSystemResources;
      this._JMSSystemResources = (JMSSystemResourceMBean[])var4;
      this._postSet(101, var5, var4);
   }

   public JMSSystemResourceMBean createJMSSystemResource(String var1) {
      JMSSystemResourceMBeanImpl var2 = new JMSSystemResourceMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addJMSSystemResource(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public JMSSystemResourceMBean createJMSSystemResource(String var1, String var2) {
      JMSSystemResourceMBeanImpl var3 = new JMSSystemResourceMBeanImpl(this, -1);

      try {
         var3.setName(var1);
         var3.setDescriptorFileName(var2);
         this.addJMSSystemResource(var3);
         return var3;
      } catch (Exception var5) {
         if (var5 instanceof RuntimeException) {
            throw (RuntimeException)var5;
         } else {
            throw new UndeclaredThrowableException(var5);
         }
      }
   }

   public void destroyJMSSystemResource(JMSSystemResourceMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 101);
         JMSSystemResourceMBean[] var2 = this.getJMSSystemResources();
         JMSSystemResourceMBean[] var3 = (JMSSystemResourceMBean[])((JMSSystemResourceMBean[])this._getHelper()._removeElement(var2, JMSSystemResourceMBean.class, var1));
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
               this.setJMSSystemResources(var3);
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

   public JMSSystemResourceMBean lookupJMSSystemResource(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._JMSSystemResources).iterator();

      JMSSystemResourceMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (JMSSystemResourceMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addCustomResource(CustomResourceMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 102)) {
         CustomResourceMBean[] var2;
         if (this._isSet(102)) {
            var2 = (CustomResourceMBean[])((CustomResourceMBean[])this._getHelper()._extendArray(this.getCustomResources(), CustomResourceMBean.class, var1));
         } else {
            var2 = new CustomResourceMBean[]{var1};
         }

         try {
            this.setCustomResources(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public CustomResourceMBean[] getCustomResources() {
      return this._CustomResources;
   }

   public boolean isCustomResourcesSet() {
      return this._isSet(102);
   }

   public void removeCustomResource(CustomResourceMBean var1) {
      this.destroyCustomResource(var1);
   }

   public void setCustomResources(CustomResourceMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new CustomResourceMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 102)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      CustomResourceMBean[] var5 = this._CustomResources;
      this._CustomResources = (CustomResourceMBean[])var4;
      this._postSet(102, var5, var4);
   }

   public CustomResourceMBean createCustomResource(String var1, String var2, String var3) {
      CustomResourceMBeanImpl var4 = new CustomResourceMBeanImpl(this, -1);

      try {
         var4.setName(var1);
         var4.setResourceClass(var2);
         var4.setDescriptorBeanClass(var3);
         this.addCustomResource(var4);
         return var4;
      } catch (Exception var6) {
         if (var6 instanceof RuntimeException) {
            throw (RuntimeException)var6;
         } else {
            throw new UndeclaredThrowableException(var6);
         }
      }
   }

   public CustomResourceMBean createCustomResource(String var1, String var2, String var3, String var4) {
      CustomResourceMBeanImpl var5 = new CustomResourceMBeanImpl(this, -1);

      try {
         var5.setName(var1);
         var5.setResourceClass(var2);
         var5.setDescriptorBeanClass(var3);
         var5.setDescriptorFileName(var4);
         this.addCustomResource(var5);
         return var5;
      } catch (Exception var7) {
         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         } else {
            throw new UndeclaredThrowableException(var7);
         }
      }
   }

   public void destroyCustomResource(CustomResourceMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 102);
         CustomResourceMBean[] var2 = this.getCustomResources();
         CustomResourceMBean[] var3 = (CustomResourceMBean[])((CustomResourceMBean[])this._getHelper()._removeElement(var2, CustomResourceMBean.class, var1));
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
               this.setCustomResources(var3);
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

   public CustomResourceMBean lookupCustomResource(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._CustomResources).iterator();

      CustomResourceMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (CustomResourceMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addForeignJNDIProvider(ForeignJNDIProviderMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 103)) {
         ForeignJNDIProviderMBean[] var2;
         if (this._isSet(103)) {
            var2 = (ForeignJNDIProviderMBean[])((ForeignJNDIProviderMBean[])this._getHelper()._extendArray(this.getForeignJNDIProviders(), ForeignJNDIProviderMBean.class, var1));
         } else {
            var2 = new ForeignJNDIProviderMBean[]{var1};
         }

         try {
            this.setForeignJNDIProviders(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public ForeignJNDIProviderMBean[] getForeignJNDIProviders() {
      return this._ForeignJNDIProviders;
   }

   public boolean isForeignJNDIProvidersSet() {
      return this._isSet(103);
   }

   public void removeForeignJNDIProvider(ForeignJNDIProviderMBean var1) {
      this.destroyForeignJNDIProvider(var1);
   }

   public void setForeignJNDIProviders(ForeignJNDIProviderMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new ForeignJNDIProviderMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 103)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      ForeignJNDIProviderMBean[] var5 = this._ForeignJNDIProviders;
      this._ForeignJNDIProviders = (ForeignJNDIProviderMBean[])var4;
      this._postSet(103, var5, var4);
   }

   public ForeignJNDIProviderMBean lookupForeignJNDIProvider(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._ForeignJNDIProviders).iterator();

      ForeignJNDIProviderMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (ForeignJNDIProviderMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public ForeignJNDIProviderMBean createForeignJNDIProvider(String var1) {
      ForeignJNDIProviderMBeanImpl var2 = new ForeignJNDIProviderMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addForeignJNDIProvider(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyForeignJNDIProvider(ForeignJNDIProviderMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 103);
         ForeignJNDIProviderMBean[] var2 = this.getForeignJNDIProviders();
         ForeignJNDIProviderMBean[] var3 = (ForeignJNDIProviderMBean[])((ForeignJNDIProviderMBean[])this._getHelper()._removeElement(var2, ForeignJNDIProviderMBean.class, var1));
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
               this.setForeignJNDIProviders(var3);
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

   public String getAdminServerName() {
      return this._AdminServerName;
   }

   public boolean isAdminServerNameSet() {
      return this._isSet(104);
   }

   public void setAdminServerName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._AdminServerName;
      this._AdminServerName = var1;
      this._postSet(104, var2, var1);
   }

   public String getAdministrationProtocol() {
      return this._AdministrationProtocol;
   }

   public boolean isAdministrationProtocolSet() {
      return this._isSet(105);
   }

   public void setAdministrationProtocol(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"t3s", "https", "iiops", "t3", "http", "iiop"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("AdministrationProtocol", var1, var2);
      String var3 = this._AdministrationProtocol;
      this._AdministrationProtocol = var1;
      this._postSet(105, var3, var1);
   }

   public void addWLDFSystemResource(WLDFSystemResourceMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 106)) {
         WLDFSystemResourceMBean[] var2;
         if (this._isSet(106)) {
            var2 = (WLDFSystemResourceMBean[])((WLDFSystemResourceMBean[])this._getHelper()._extendArray(this.getWLDFSystemResources(), WLDFSystemResourceMBean.class, var1));
         } else {
            var2 = new WLDFSystemResourceMBean[]{var1};
         }

         try {
            this.setWLDFSystemResources(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public WLDFSystemResourceMBean[] getWLDFSystemResources() {
      return this._WLDFSystemResources;
   }

   public boolean isWLDFSystemResourcesSet() {
      return this._isSet(106);
   }

   public void removeWLDFSystemResource(WLDFSystemResourceMBean var1) {
      this.destroyWLDFSystemResource(var1);
   }

   public void setWLDFSystemResources(WLDFSystemResourceMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new WLDFSystemResourceMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 106)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      WLDFSystemResourceMBean[] var5 = this._WLDFSystemResources;
      this._WLDFSystemResources = (WLDFSystemResourceMBean[])var4;
      this._postSet(106, var5, var4);
   }

   public WLDFSystemResourceMBean createWLDFSystemResource(String var1) {
      WLDFSystemResourceMBeanImpl var2 = new WLDFSystemResourceMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addWLDFSystemResource(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public WLDFSystemResourceMBean createWLDFSystemResource(String var1, String var2) {
      WLDFSystemResourceMBeanImpl var3 = new WLDFSystemResourceMBeanImpl(this, -1);

      try {
         var3.setName(var1);
         var3.setDescriptorFileName(var2);
         this.addWLDFSystemResource(var3);
         return var3;
      } catch (Exception var5) {
         if (var5 instanceof RuntimeException) {
            throw (RuntimeException)var5;
         } else {
            throw new UndeclaredThrowableException(var5);
         }
      }
   }

   public void destroyWLDFSystemResource(WLDFSystemResourceMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 106);
         WLDFSystemResourceMBean[] var2 = this.getWLDFSystemResources();
         WLDFSystemResourceMBean[] var3 = (WLDFSystemResourceMBean[])((WLDFSystemResourceMBean[])this._getHelper()._removeElement(var2, WLDFSystemResourceMBean.class, var1));
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
               this.setWLDFSystemResources(var3);
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

   public WLDFSystemResourceMBean lookupWLDFSystemResource(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._WLDFSystemResources).iterator();

      WLDFSystemResourceMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (WLDFSystemResourceMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addJDBCSystemResource(JDBCSystemResourceMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 107)) {
         JDBCSystemResourceMBean[] var2;
         if (this._isSet(107)) {
            var2 = (JDBCSystemResourceMBean[])((JDBCSystemResourceMBean[])this._getHelper()._extendArray(this.getJDBCSystemResources(), JDBCSystemResourceMBean.class, var1));
         } else {
            var2 = new JDBCSystemResourceMBean[]{var1};
         }

         try {
            this.setJDBCSystemResources(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public JDBCSystemResourceMBean[] getJDBCSystemResources() {
      return this._JDBCSystemResources;
   }

   public boolean isJDBCSystemResourcesSet() {
      return this._isSet(107);
   }

   public void removeJDBCSystemResource(JDBCSystemResourceMBean var1) {
      this.destroyJDBCSystemResource(var1);
   }

   public void setJDBCSystemResources(JDBCSystemResourceMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new JDBCSystemResourceMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 107)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      JDBCSystemResourceMBean[] var5 = this._JDBCSystemResources;
      this._JDBCSystemResources = (JDBCSystemResourceMBean[])var4;
      this._postSet(107, var5, var4);
   }

   public JDBCSystemResourceMBean createJDBCSystemResource(String var1) {
      JDBCSystemResourceMBeanImpl var2 = new JDBCSystemResourceMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addJDBCSystemResource(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public JDBCSystemResourceMBean createJDBCSystemResource(String var1, String var2) {
      JDBCSystemResourceMBeanImpl var3 = new JDBCSystemResourceMBeanImpl(this, -1);

      try {
         var3.setName(var1);
         var3.setDescriptorFileName(var2);
         this.addJDBCSystemResource(var3);
         return var3;
      } catch (Exception var5) {
         if (var5 instanceof RuntimeException) {
            throw (RuntimeException)var5;
         } else {
            throw new UndeclaredThrowableException(var5);
         }
      }
   }

   public JDBCSystemResourceMBean lookupJDBCSystemResource(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._JDBCSystemResources).iterator();

      JDBCSystemResourceMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (JDBCSystemResourceMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void destroyJDBCSystemResource(JDBCSystemResourceMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 107);
         JDBCSystemResourceMBean[] var2 = this.getJDBCSystemResources();
         JDBCSystemResourceMBean[] var3 = (JDBCSystemResourceMBean[])((JDBCSystemResourceMBean[])this._getHelper()._removeElement(var2, JDBCSystemResourceMBean.class, var1));
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
               this.setJDBCSystemResources(var3);
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

   public void addSystemResource(SystemResourceMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 108)) {
         SystemResourceMBean[] var2 = (SystemResourceMBean[])((SystemResourceMBean[])this._getHelper()._extendArray(this.getSystemResources(), SystemResourceMBean.class, var1));

         try {
            this.setSystemResources(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public SystemResourceMBean[] getSystemResources() {
      return this._customizer.getSystemResources();
   }

   public boolean isSystemResourcesSet() {
      return this._isSet(108);
   }

   public void removeSystemResource(SystemResourceMBean var1) {
      SystemResourceMBean[] var2 = this.getSystemResources();
      SystemResourceMBean[] var3 = (SystemResourceMBean[])((SystemResourceMBean[])this._getHelper()._removeElement(var2, SystemResourceMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setSystemResources(var3);
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            }

            throw new UndeclaredThrowableException(var5);
         }
      }

   }

   public void setSystemResources(SystemResourceMBean[] var1) throws InvalidAttributeValueException {
      Object var2 = var1 == null ? new SystemResourceMBeanImpl[0] : var1;
      this._SystemResources = (SystemResourceMBean[])var2;
   }

   public SystemResourceMBean lookupSystemResource(String var1) {
      return this._customizer.lookupSystemResource(var1);
   }

   public void addSAFAgent(SAFAgentMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 109)) {
         SAFAgentMBean[] var2;
         if (this._isSet(109)) {
            var2 = (SAFAgentMBean[])((SAFAgentMBean[])this._getHelper()._extendArray(this.getSAFAgents(), SAFAgentMBean.class, var1));
         } else {
            var2 = new SAFAgentMBean[]{var1};
         }

         try {
            this.setSAFAgents(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public SAFAgentMBean[] getSAFAgents() {
      return this._SAFAgents;
   }

   public boolean isSAFAgentsSet() {
      return this._isSet(109);
   }

   public void removeSAFAgent(SAFAgentMBean var1) {
      this.destroySAFAgent(var1);
   }

   public void setSAFAgents(SAFAgentMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new SAFAgentMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 109)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      SAFAgentMBean[] var5 = this._SAFAgents;
      this._SAFAgents = (SAFAgentMBean[])var4;
      this._postSet(109, var5, var4);
   }

   public SAFAgentMBean createSAFAgent(String var1) {
      SAFAgentMBeanImpl var2 = new SAFAgentMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addSAFAgent(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroySAFAgent(SAFAgentMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 109);
         SAFAgentMBean[] var2 = this.getSAFAgents();
         SAFAgentMBean[] var3 = (SAFAgentMBean[])((SAFAgentMBean[])this._getHelper()._removeElement(var2, SAFAgentMBean.class, var1));
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
               this.setSAFAgents(var3);
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

   public SAFAgentMBean lookupSAFAgent(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._SAFAgents).iterator();

      SAFAgentMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (SAFAgentMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addWLECConnectionPool(WLECConnectionPoolMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 110)) {
         WLECConnectionPoolMBean[] var2;
         if (this._isSet(110)) {
            var2 = (WLECConnectionPoolMBean[])((WLECConnectionPoolMBean[])this._getHelper()._extendArray(this.getWLECConnectionPools(), WLECConnectionPoolMBean.class, var1));
         } else {
            var2 = new WLECConnectionPoolMBean[]{var1};
         }

         try {
            this.setWLECConnectionPools(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public WLECConnectionPoolMBean[] getWLECConnectionPools() {
      return this._WLECConnectionPools;
   }

   public boolean isWLECConnectionPoolsSet() {
      return this._isSet(110);
   }

   public void removeWLECConnectionPool(WLECConnectionPoolMBean var1) {
      this.destroyWLECConnectionPool(var1);
   }

   public void setWLECConnectionPools(WLECConnectionPoolMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new WLECConnectionPoolMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 110)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      WLECConnectionPoolMBean[] var5 = this._WLECConnectionPools;
      this._WLECConnectionPools = (WLECConnectionPoolMBean[])var4;
      this._postSet(110, var5, var4);
   }

   public WLECConnectionPoolMBean createWLECConnectionPool(String var1) {
      WLECConnectionPoolMBeanImpl var2 = new WLECConnectionPoolMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addWLECConnectionPool(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyWLECConnectionPool(WLECConnectionPoolMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 110);
         WLECConnectionPoolMBean[] var2 = this.getWLECConnectionPools();
         WLECConnectionPoolMBean[] var3 = (WLECConnectionPoolMBean[])((WLECConnectionPoolMBean[])this._getHelper()._removeElement(var2, WLECConnectionPoolMBean.class, var1));
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
               this.setWLECConnectionPools(var3);
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

   public WLECConnectionPoolMBean lookupWLECConnectionPool(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._WLECConnectionPools).iterator();

      WLECConnectionPoolMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (WLECConnectionPoolMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addErrorHandling(ErrorHandlingMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 111)) {
         ErrorHandlingMBean[] var2;
         if (this._isSet(111)) {
            var2 = (ErrorHandlingMBean[])((ErrorHandlingMBean[])this._getHelper()._extendArray(this.getErrorHandlings(), ErrorHandlingMBean.class, var1));
         } else {
            var2 = new ErrorHandlingMBean[]{var1};
         }

         try {
            this.setErrorHandlings(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public ErrorHandlingMBean[] getErrorHandlings() {
      return this._ErrorHandlings;
   }

   public boolean isErrorHandlingsSet() {
      return this._isSet(111);
   }

   public void removeErrorHandling(ErrorHandlingMBean var1) {
      this.destroyErrorHandling(var1);
   }

   public void setErrorHandlings(ErrorHandlingMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new ErrorHandlingMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 111)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      ErrorHandlingMBean[] var5 = this._ErrorHandlings;
      this._ErrorHandlings = (ErrorHandlingMBean[])var4;
      this._postSet(111, var5, var4);
   }

   public ErrorHandlingMBean createErrorHandling(String var1) {
      ErrorHandlingMBeanImpl var2 = new ErrorHandlingMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addErrorHandling(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyErrorHandling(ErrorHandlingMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 111);
         ErrorHandlingMBean[] var2 = this.getErrorHandlings();
         ErrorHandlingMBean[] var3 = (ErrorHandlingMBean[])((ErrorHandlingMBean[])this._getHelper()._removeElement(var2, ErrorHandlingMBean.class, var1));
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
               this.setErrorHandlings(var3);
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

   public ErrorHandlingMBean lookupErrorHandling(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._ErrorHandlings).iterator();

      ErrorHandlingMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (ErrorHandlingMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addRemoteSAFContext(RemoteSAFContextMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 112)) {
         RemoteSAFContextMBean[] var2;
         if (this._isSet(112)) {
            var2 = (RemoteSAFContextMBean[])((RemoteSAFContextMBean[])this._getHelper()._extendArray(this.getRemoteSAFContexts(), RemoteSAFContextMBean.class, var1));
         } else {
            var2 = new RemoteSAFContextMBean[]{var1};
         }

         try {
            this.setRemoteSAFContexts(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public RemoteSAFContextMBean[] getRemoteSAFContexts() {
      return this._RemoteSAFContexts;
   }

   public boolean isRemoteSAFContextsSet() {
      return this._isSet(112);
   }

   public void removeRemoteSAFContext(RemoteSAFContextMBean var1) {
      this.destroyRemoteSAFContext(var1);
   }

   public void setRemoteSAFContexts(RemoteSAFContextMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new RemoteSAFContextMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 112)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      RemoteSAFContextMBean[] var5 = this._RemoteSAFContexts;
      this._RemoteSAFContexts = (RemoteSAFContextMBean[])var4;
      this._postSet(112, var5, var4);
   }

   public RemoteSAFContextMBean createRemoteSAFContext(String var1) {
      RemoteSAFContextMBeanImpl var2 = new RemoteSAFContextMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addRemoteSAFContext(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyRemoteSAFContext(RemoteSAFContextMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 112);
         RemoteSAFContextMBean[] var2 = this.getRemoteSAFContexts();
         RemoteSAFContextMBean[] var3 = (RemoteSAFContextMBean[])((RemoteSAFContextMBean[])this._getHelper()._removeElement(var2, RemoteSAFContextMBean.class, var1));
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
               this.setRemoteSAFContexts(var3);
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

   public RemoteSAFContextMBean lookupRemoteSAFContext(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._RemoteSAFContexts).iterator();

      RemoteSAFContextMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (RemoteSAFContextMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addMigratableRMIService(MigratableRMIServiceMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 113)) {
         MigratableRMIServiceMBean[] var2;
         if (this._isSet(113)) {
            var2 = (MigratableRMIServiceMBean[])((MigratableRMIServiceMBean[])this._getHelper()._extendArray(this.getMigratableRMIServices(), MigratableRMIServiceMBean.class, var1));
         } else {
            var2 = new MigratableRMIServiceMBean[]{var1};
         }

         try {
            this.setMigratableRMIServices(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public MigratableRMIServiceMBean[] getMigratableRMIServices() {
      return this._MigratableRMIServices;
   }

   public boolean isMigratableRMIServicesSet() {
      return this._isSet(113);
   }

   public void removeMigratableRMIService(MigratableRMIServiceMBean var1) {
      this.destroyMigratableRMIService(var1);
   }

   public void setMigratableRMIServices(MigratableRMIServiceMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new MigratableRMIServiceMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 113)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      MigratableRMIServiceMBean[] var5 = this._MigratableRMIServices;
      this._MigratableRMIServices = (MigratableRMIServiceMBean[])var4;
      this._postSet(113, var5, var4);
   }

   public MigratableRMIServiceMBean createMigratableRMIService(String var1) {
      MigratableRMIServiceMBeanImpl var2 = new MigratableRMIServiceMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addMigratableRMIService(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyMigratableRMIService(MigratableRMIServiceMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 113);
         MigratableRMIServiceMBean[] var2 = this.getMigratableRMIServices();
         MigratableRMIServiceMBean[] var3 = (MigratableRMIServiceMBean[])((MigratableRMIServiceMBean[])this._getHelper()._removeElement(var2, MigratableRMIServiceMBean.class, var1));
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
               this.setMigratableRMIServices(var3);
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

   public MigratableRMIServiceMBean lookupMigratableRMIService(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._MigratableRMIServices).iterator();

      MigratableRMIServiceMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (MigratableRMIServiceMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public AdminServerMBean getAdminServerMBean() {
      return this._AdminServerMBean;
   }

   public boolean isAdminServerMBeanSet() {
      return this._isSet(114);
   }

   public void setAdminServerMBean(AdminServerMBean var1) throws InvalidAttributeValueException {
      if (var1 != null && this.getAdminServerMBean() != null && var1 != this.getAdminServerMBean()) {
         throw new BeanAlreadyExistsException(this.getAdminServerMBean() + " has already been created");
      } else {
         if (var1 != null) {
            AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
            if (this._setParent(var2, this, 114)) {
               this._getReferenceManager().registerBean(var2, false);
               this._postCreate(var2);
            }
         }

         AdminServerMBean var3 = this._AdminServerMBean;
         this._AdminServerMBean = var1;
         this._postSet(114, var3, var1);
      }
   }

   public AdminServerMBean createAdminServerMBean() {
      AdminServerMBeanImpl var1 = new AdminServerMBeanImpl(this, -1);

      try {
         this.setAdminServerMBean(var1);
         return var1;
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public void destroyAdminServerMBean() {
      try {
         AbstractDescriptorBean var1 = (AbstractDescriptorBean)this._AdminServerMBean;
         if (var1 != null) {
            List var2 = this._getReferenceManager().getResolvedReferences(var1);
            if (var2 != null && var2.size() > 0) {
               throw new BeanRemoveRejectedException(var1, var2);
            } else {
               this._getReferenceManager().unregisterBean(var1);
               this._markDestroyed(var1);
               this.setAdminServerMBean((AdminServerMBean)null);
               this._unSet(114);
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

   public void addJMSDistributedQueueMember(JMSDistributedQueueMemberMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 115)) {
         JMSDistributedQueueMemberMBean[] var2;
         if (this._isSet(115)) {
            var2 = (JMSDistributedQueueMemberMBean[])((JMSDistributedQueueMemberMBean[])this._getHelper()._extendArray(this.getJMSDistributedQueueMembers(), JMSDistributedQueueMemberMBean.class, var1));
         } else {
            var2 = new JMSDistributedQueueMemberMBean[]{var1};
         }

         try {
            this.setJMSDistributedQueueMembers(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public JMSDistributedQueueMemberMBean[] getJMSDistributedQueueMembers() {
      return this._JMSDistributedQueueMembers;
   }

   public boolean isJMSDistributedQueueMembersSet() {
      return this._isSet(115);
   }

   public void removeJMSDistributedQueueMember(JMSDistributedQueueMemberMBean var1) {
      this.destroyJMSDistributedQueueMember(var1);
   }

   public void setJMSDistributedQueueMembers(JMSDistributedQueueMemberMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new JMSDistributedQueueMemberMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 115)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      JMSDistributedQueueMemberMBean[] var5 = this._JMSDistributedQueueMembers;
      this._JMSDistributedQueueMembers = (JMSDistributedQueueMemberMBean[])var4;
      this._postSet(115, var5, var4);
   }

   public JMSDistributedQueueMemberMBean createJMSDistributedQueueMember(String var1) {
      JMSDistributedQueueMemberMBeanImpl var2 = new JMSDistributedQueueMemberMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addJMSDistributedQueueMember(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyJMSDistributedQueueMember(JMSDistributedQueueMemberMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 115);
         JMSDistributedQueueMemberMBean[] var2 = this.getJMSDistributedQueueMembers();
         JMSDistributedQueueMemberMBean[] var3 = (JMSDistributedQueueMemberMBean[])((JMSDistributedQueueMemberMBean[])this._getHelper()._removeElement(var2, JMSDistributedQueueMemberMBean.class, var1));
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
               this.setJMSDistributedQueueMembers(var3);
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

   public JMSDistributedQueueMemberMBean lookupJMSDistributedQueueMember(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._JMSDistributedQueueMembers).iterator();

      JMSDistributedQueueMemberMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (JMSDistributedQueueMemberMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addJMSDistributedTopicMember(JMSDistributedTopicMemberMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 116)) {
         JMSDistributedTopicMemberMBean[] var2;
         if (this._isSet(116)) {
            var2 = (JMSDistributedTopicMemberMBean[])((JMSDistributedTopicMemberMBean[])this._getHelper()._extendArray(this.getJMSDistributedTopicMembers(), JMSDistributedTopicMemberMBean.class, var1));
         } else {
            var2 = new JMSDistributedTopicMemberMBean[]{var1};
         }

         try {
            this.setJMSDistributedTopicMembers(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public JMSDistributedTopicMemberMBean[] getJMSDistributedTopicMembers() {
      return this._JMSDistributedTopicMembers;
   }

   public boolean isJMSDistributedTopicMembersSet() {
      return this._isSet(116);
   }

   public void removeJMSDistributedTopicMember(JMSDistributedTopicMemberMBean var1) {
      this.destroyJMSDistributedTopicMember(var1);
   }

   public void setJMSDistributedTopicMembers(JMSDistributedTopicMemberMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new JMSDistributedTopicMemberMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 116)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      JMSDistributedTopicMemberMBean[] var5 = this._JMSDistributedTopicMembers;
      this._JMSDistributedTopicMembers = (JMSDistributedTopicMemberMBean[])var4;
      this._postSet(116, var5, var4);
   }

   public JMSDistributedTopicMemberMBean createJMSDistributedTopicMember(String var1) {
      JMSDistributedTopicMemberMBeanImpl var2 = new JMSDistributedTopicMemberMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addJMSDistributedTopicMember(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyJMSDistributedTopicMember(JMSDistributedTopicMemberMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 116);
         JMSDistributedTopicMemberMBean[] var2 = this.getJMSDistributedTopicMembers();
         JMSDistributedTopicMemberMBean[] var3 = (JMSDistributedTopicMemberMBean[])((JMSDistributedTopicMemberMBean[])this._getHelper()._removeElement(var2, JMSDistributedTopicMemberMBean.class, var1));
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
               this.setJMSDistributedTopicMembers(var3);
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

   public JMSDistributedTopicMemberMBean lookupJMSDistributedTopicMember(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._JMSDistributedTopicMembers).iterator();

      JMSDistributedTopicMemberMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (JMSDistributedTopicMemberMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public SNMPTrapDestinationMBean createSNMPTrapDestination(String var1) {
      SNMPTrapDestinationMBeanImpl var2 = new SNMPTrapDestinationMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addSNMPTrapDestination(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroySNMPTrapDestination(SNMPTrapDestinationMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 117);
         SNMPTrapDestinationMBean[] var2 = this.getSNMPTrapDestinations();
         SNMPTrapDestinationMBean[] var3 = (SNMPTrapDestinationMBean[])((SNMPTrapDestinationMBean[])this._getHelper()._removeElement(var2, SNMPTrapDestinationMBean.class, var1));
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
               this.setSNMPTrapDestinations(var3);
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

   public void addSNMPTrapDestination(SNMPTrapDestinationMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 117)) {
         SNMPTrapDestinationMBean[] var2;
         if (this._isSet(117)) {
            var2 = (SNMPTrapDestinationMBean[])((SNMPTrapDestinationMBean[])this._getHelper()._extendArray(this.getSNMPTrapDestinations(), SNMPTrapDestinationMBean.class, var1));
         } else {
            var2 = new SNMPTrapDestinationMBean[]{var1};
         }

         try {
            this.setSNMPTrapDestinations(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public SNMPTrapDestinationMBean[] getSNMPTrapDestinations() {
      return this._SNMPTrapDestinations;
   }

   public boolean isSNMPTrapDestinationsSet() {
      return this._isSet(117);
   }

   public void removeSNMPTrapDestination(SNMPTrapDestinationMBean var1) {
      this.destroySNMPTrapDestination(var1);
   }

   public void setSNMPTrapDestinations(SNMPTrapDestinationMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new SNMPTrapDestinationMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 117)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      SNMPTrapDestinationMBean[] var5 = this._SNMPTrapDestinations;
      this._SNMPTrapDestinations = (SNMPTrapDestinationMBean[])var4;
      this._postSet(117, var5, var4);
   }

   public void addSNMPProxy(SNMPProxyMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 118)) {
         SNMPProxyMBean[] var2;
         if (this._isSet(118)) {
            var2 = (SNMPProxyMBean[])((SNMPProxyMBean[])this._getHelper()._extendArray(this.getSNMPProxies(), SNMPProxyMBean.class, var1));
         } else {
            var2 = new SNMPProxyMBean[]{var1};
         }

         try {
            this.setSNMPProxies(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public SNMPProxyMBean[] getSNMPProxies() {
      return this._SNMPProxies;
   }

   public boolean isSNMPProxiesSet() {
      return this._isSet(118);
   }

   public void removeSNMPProxy(SNMPProxyMBean var1) {
      this.destroySNMPProxy(var1);
   }

   public void setSNMPProxies(SNMPProxyMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new SNMPProxyMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 118)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      SNMPProxyMBean[] var5 = this._SNMPProxies;
      this._SNMPProxies = (SNMPProxyMBean[])var4;
      this._postSet(118, var5, var4);
   }

   public SNMPProxyMBean createSNMPProxy(String var1) {
      SNMPProxyMBeanImpl var2 = new SNMPProxyMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addSNMPProxy(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroySNMPProxy(SNMPProxyMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 118);
         SNMPProxyMBean[] var2 = this.getSNMPProxies();
         SNMPProxyMBean[] var3 = (SNMPProxyMBean[])((SNMPProxyMBean[])this._getHelper()._removeElement(var2, SNMPProxyMBean.class, var1));
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
               this.setSNMPProxies(var3);
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

   public void addSNMPGaugeMonitor(SNMPGaugeMonitorMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 119)) {
         SNMPGaugeMonitorMBean[] var2;
         if (this._isSet(119)) {
            var2 = (SNMPGaugeMonitorMBean[])((SNMPGaugeMonitorMBean[])this._getHelper()._extendArray(this.getSNMPGaugeMonitors(), SNMPGaugeMonitorMBean.class, var1));
         } else {
            var2 = new SNMPGaugeMonitorMBean[]{var1};
         }

         try {
            this.setSNMPGaugeMonitors(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public SNMPGaugeMonitorMBean[] getSNMPGaugeMonitors() {
      return this._SNMPGaugeMonitors;
   }

   public boolean isSNMPGaugeMonitorsSet() {
      return this._isSet(119);
   }

   public void removeSNMPGaugeMonitor(SNMPGaugeMonitorMBean var1) {
      this.destroySNMPGaugeMonitor(var1);
   }

   public void setSNMPGaugeMonitors(SNMPGaugeMonitorMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new SNMPGaugeMonitorMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 119)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      SNMPGaugeMonitorMBean[] var5 = this._SNMPGaugeMonitors;
      this._SNMPGaugeMonitors = (SNMPGaugeMonitorMBean[])var4;
      this._postSet(119, var5, var4);
   }

   public SNMPGaugeMonitorMBean createSNMPGaugeMonitor(String var1) {
      SNMPGaugeMonitorMBeanImpl var2 = new SNMPGaugeMonitorMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addSNMPGaugeMonitor(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroySNMPGaugeMonitor(SNMPGaugeMonitorMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 119);
         SNMPGaugeMonitorMBean[] var2 = this.getSNMPGaugeMonitors();
         SNMPGaugeMonitorMBean[] var3 = (SNMPGaugeMonitorMBean[])((SNMPGaugeMonitorMBean[])this._getHelper()._removeElement(var2, SNMPGaugeMonitorMBean.class, var1));
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
               this.setSNMPGaugeMonitors(var3);
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

   public void addSNMPStringMonitor(SNMPStringMonitorMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 120)) {
         SNMPStringMonitorMBean[] var2;
         if (this._isSet(120)) {
            var2 = (SNMPStringMonitorMBean[])((SNMPStringMonitorMBean[])this._getHelper()._extendArray(this.getSNMPStringMonitors(), SNMPStringMonitorMBean.class, var1));
         } else {
            var2 = new SNMPStringMonitorMBean[]{var1};
         }

         try {
            this.setSNMPStringMonitors(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public SNMPStringMonitorMBean[] getSNMPStringMonitors() {
      return this._SNMPStringMonitors;
   }

   public boolean isSNMPStringMonitorsSet() {
      return this._isSet(120);
   }

   public void removeSNMPStringMonitor(SNMPStringMonitorMBean var1) {
      this.destroySNMPStringMonitor(var1);
   }

   public void setSNMPStringMonitors(SNMPStringMonitorMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new SNMPStringMonitorMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 120)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      SNMPStringMonitorMBean[] var5 = this._SNMPStringMonitors;
      this._SNMPStringMonitors = (SNMPStringMonitorMBean[])var4;
      this._postSet(120, var5, var4);
   }

   public SNMPStringMonitorMBean createSNMPStringMonitor(String var1) {
      SNMPStringMonitorMBeanImpl var2 = new SNMPStringMonitorMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addSNMPStringMonitor(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroySNMPStringMonitor(SNMPStringMonitorMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 120);
         SNMPStringMonitorMBean[] var2 = this.getSNMPStringMonitors();
         SNMPStringMonitorMBean[] var3 = (SNMPStringMonitorMBean[])((SNMPStringMonitorMBean[])this._getHelper()._removeElement(var2, SNMPStringMonitorMBean.class, var1));
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
               this.setSNMPStringMonitors(var3);
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

   public void addSNMPCounterMonitor(SNMPCounterMonitorMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 121)) {
         SNMPCounterMonitorMBean[] var2;
         if (this._isSet(121)) {
            var2 = (SNMPCounterMonitorMBean[])((SNMPCounterMonitorMBean[])this._getHelper()._extendArray(this.getSNMPCounterMonitors(), SNMPCounterMonitorMBean.class, var1));
         } else {
            var2 = new SNMPCounterMonitorMBean[]{var1};
         }

         try {
            this.setSNMPCounterMonitors(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public SNMPCounterMonitorMBean[] getSNMPCounterMonitors() {
      return this._SNMPCounterMonitors;
   }

   public boolean isSNMPCounterMonitorsSet() {
      return this._isSet(121);
   }

   public void removeSNMPCounterMonitor(SNMPCounterMonitorMBean var1) {
      this.destroySNMPCounterMonitor(var1);
   }

   public void setSNMPCounterMonitors(SNMPCounterMonitorMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new SNMPCounterMonitorMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 121)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      SNMPCounterMonitorMBean[] var5 = this._SNMPCounterMonitors;
      this._SNMPCounterMonitors = (SNMPCounterMonitorMBean[])var4;
      this._postSet(121, var5, var4);
   }

   public SNMPCounterMonitorMBean createSNMPCounterMonitor(String var1) {
      SNMPCounterMonitorMBeanImpl var2 = new SNMPCounterMonitorMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addSNMPCounterMonitor(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroySNMPCounterMonitor(SNMPCounterMonitorMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 121);
         SNMPCounterMonitorMBean[] var2 = this.getSNMPCounterMonitors();
         SNMPCounterMonitorMBean[] var3 = (SNMPCounterMonitorMBean[])((SNMPCounterMonitorMBean[])this._getHelper()._removeElement(var2, SNMPCounterMonitorMBean.class, var1));
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
               this.setSNMPCounterMonitors(var3);
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

   public void addSNMPLogFilter(SNMPLogFilterMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 122)) {
         SNMPLogFilterMBean[] var2;
         if (this._isSet(122)) {
            var2 = (SNMPLogFilterMBean[])((SNMPLogFilterMBean[])this._getHelper()._extendArray(this.getSNMPLogFilters(), SNMPLogFilterMBean.class, var1));
         } else {
            var2 = new SNMPLogFilterMBean[]{var1};
         }

         try {
            this.setSNMPLogFilters(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public SNMPLogFilterMBean[] getSNMPLogFilters() {
      return this._SNMPLogFilters;
   }

   public boolean isSNMPLogFiltersSet() {
      return this._isSet(122);
   }

   public void removeSNMPLogFilter(SNMPLogFilterMBean var1) {
      this.destroySNMPLogFilter(var1);
   }

   public void setSNMPLogFilters(SNMPLogFilterMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new SNMPLogFilterMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 122)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      SNMPLogFilterMBean[] var5 = this._SNMPLogFilters;
      this._SNMPLogFilters = (SNMPLogFilterMBean[])var4;
      this._postSet(122, var5, var4);
   }

   public SNMPLogFilterMBean createSNMPLogFilter(String var1) {
      SNMPLogFilterMBeanImpl var2 = new SNMPLogFilterMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addSNMPLogFilter(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroySNMPLogFilter(SNMPLogFilterMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 122);
         SNMPLogFilterMBean[] var2 = this.getSNMPLogFilters();
         SNMPLogFilterMBean[] var3 = (SNMPLogFilterMBean[])((SNMPLogFilterMBean[])this._getHelper()._removeElement(var2, SNMPLogFilterMBean.class, var1));
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
               this.setSNMPLogFilters(var3);
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

   public void addSNMPAttributeChange(SNMPAttributeChangeMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 123)) {
         SNMPAttributeChangeMBean[] var2;
         if (this._isSet(123)) {
            var2 = (SNMPAttributeChangeMBean[])((SNMPAttributeChangeMBean[])this._getHelper()._extendArray(this.getSNMPAttributeChanges(), SNMPAttributeChangeMBean.class, var1));
         } else {
            var2 = new SNMPAttributeChangeMBean[]{var1};
         }

         try {
            this.setSNMPAttributeChanges(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public SNMPAttributeChangeMBean[] getSNMPAttributeChanges() {
      return this._SNMPAttributeChanges;
   }

   public boolean isSNMPAttributeChangesSet() {
      return this._isSet(123);
   }

   public void removeSNMPAttributeChange(SNMPAttributeChangeMBean var1) {
      this.destroySNMPAttributeChange(var1);
   }

   public void setSNMPAttributeChanges(SNMPAttributeChangeMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new SNMPAttributeChangeMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 123)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      SNMPAttributeChangeMBean[] var5 = this._SNMPAttributeChanges;
      this._SNMPAttributeChanges = (SNMPAttributeChangeMBean[])var4;
      this._postSet(123, var5, var4);
   }

   public SNMPAttributeChangeMBean createSNMPAttributeChange(String var1) {
      SNMPAttributeChangeMBeanImpl var2 = new SNMPAttributeChangeMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addSNMPAttributeChange(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroySNMPAttributeChange(SNMPAttributeChangeMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 123);
         SNMPAttributeChangeMBean[] var2 = this.getSNMPAttributeChanges();
         SNMPAttributeChangeMBean[] var3 = (SNMPAttributeChangeMBean[])((SNMPAttributeChangeMBean[])this._getHelper()._removeElement(var2, SNMPAttributeChangeMBean.class, var1));
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
               this.setSNMPAttributeChanges(var3);
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

   public void addWebserviceSecurity(WebserviceSecurityMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 124)) {
         WebserviceSecurityMBean[] var2;
         if (this._isSet(124)) {
            var2 = (WebserviceSecurityMBean[])((WebserviceSecurityMBean[])this._getHelper()._extendArray(this.getWebserviceSecurities(), WebserviceSecurityMBean.class, var1));
         } else {
            var2 = new WebserviceSecurityMBean[]{var1};
         }

         try {
            this.setWebserviceSecurities(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public WebserviceSecurityMBean[] getWebserviceSecurities() {
      return this._WebserviceSecurities;
   }

   public boolean isWebserviceSecuritiesSet() {
      return this._isSet(124);
   }

   public void removeWebserviceSecurity(WebserviceSecurityMBean var1) {
      this.destroyWebserviceSecurity(var1);
   }

   public void setWebserviceSecurities(WebserviceSecurityMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new WebserviceSecurityMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 124)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      WebserviceSecurityMBean[] var5 = this._WebserviceSecurities;
      this._WebserviceSecurities = (WebserviceSecurityMBean[])var4;
      this._postSet(124, var5, var4);
   }

   public WebserviceSecurityMBean lookupWebserviceSecurity(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._WebserviceSecurities).iterator();

      WebserviceSecurityMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (WebserviceSecurityMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public WebserviceSecurityMBean createWebserviceSecurity(String var1) {
      WebserviceSecurityMBeanImpl var2 = new WebserviceSecurityMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addWebserviceSecurity(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyWebserviceSecurity(WebserviceSecurityMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 124);
         WebserviceSecurityMBean[] var2 = this.getWebserviceSecurities();
         WebserviceSecurityMBean[] var3 = (WebserviceSecurityMBean[])((WebserviceSecurityMBean[])this._getHelper()._removeElement(var2, WebserviceSecurityMBean.class, var1));
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
               this.setWebserviceSecurities(var3);
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

   public void addForeignJMSConnectionFactory(ForeignJMSConnectionFactoryMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 125)) {
         ForeignJMSConnectionFactoryMBean[] var2;
         if (this._isSet(125)) {
            var2 = (ForeignJMSConnectionFactoryMBean[])((ForeignJMSConnectionFactoryMBean[])this._getHelper()._extendArray(this.getForeignJMSConnectionFactories(), ForeignJMSConnectionFactoryMBean.class, var1));
         } else {
            var2 = new ForeignJMSConnectionFactoryMBean[]{var1};
         }

         try {
            this.setForeignJMSConnectionFactories(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public ForeignJMSConnectionFactoryMBean[] getForeignJMSConnectionFactories() {
      return this._ForeignJMSConnectionFactories;
   }

   public boolean isForeignJMSConnectionFactoriesSet() {
      return this._isSet(125);
   }

   public void removeForeignJMSConnectionFactory(ForeignJMSConnectionFactoryMBean var1) {
      this.destroyForeignJMSConnectionFactory(var1);
   }

   public void setForeignJMSConnectionFactories(ForeignJMSConnectionFactoryMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new ForeignJMSConnectionFactoryMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 125)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      ForeignJMSConnectionFactoryMBean[] var5 = this._ForeignJMSConnectionFactories;
      this._ForeignJMSConnectionFactories = (ForeignJMSConnectionFactoryMBean[])var4;
      this._postSet(125, var5, var4);
   }

   public ForeignJMSConnectionFactoryMBean lookupForeignJMSConnectionFactory(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._ForeignJMSConnectionFactories).iterator();

      ForeignJMSConnectionFactoryMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (ForeignJMSConnectionFactoryMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public ForeignJMSConnectionFactoryMBean createForeignJMSConnectionFactory(String var1) {
      ForeignJMSConnectionFactoryMBeanImpl var2 = new ForeignJMSConnectionFactoryMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addForeignJMSConnectionFactory(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyForeignJMSConnectionFactory(ForeignJMSConnectionFactoryMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 125);
         ForeignJMSConnectionFactoryMBean[] var2 = this.getForeignJMSConnectionFactories();
         ForeignJMSConnectionFactoryMBean[] var3 = (ForeignJMSConnectionFactoryMBean[])((ForeignJMSConnectionFactoryMBean[])this._getHelper()._removeElement(var2, ForeignJMSConnectionFactoryMBean.class, var1));
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
               this.setForeignJMSConnectionFactories(var3);
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

   public void addForeignJMSDestination(ForeignJMSDestinationMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 126)) {
         ForeignJMSDestinationMBean[] var2;
         if (this._isSet(126)) {
            var2 = (ForeignJMSDestinationMBean[])((ForeignJMSDestinationMBean[])this._getHelper()._extendArray(this.getForeignJMSDestinations(), ForeignJMSDestinationMBean.class, var1));
         } else {
            var2 = new ForeignJMSDestinationMBean[]{var1};
         }

         try {
            this.setForeignJMSDestinations(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public ForeignJMSDestinationMBean[] getForeignJMSDestinations() {
      return this._ForeignJMSDestinations;
   }

   public boolean isForeignJMSDestinationsSet() {
      return this._isSet(126);
   }

   public void removeForeignJMSDestination(ForeignJMSDestinationMBean var1) {
      this.destroyForeignJMSDestination(var1);
   }

   public void setForeignJMSDestinations(ForeignJMSDestinationMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new ForeignJMSDestinationMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 126)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      ForeignJMSDestinationMBean[] var5 = this._ForeignJMSDestinations;
      this._ForeignJMSDestinations = (ForeignJMSDestinationMBean[])var4;
      this._postSet(126, var5, var4);
   }

   public ForeignJMSDestinationMBean lookupForeignJMSDestination(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._ForeignJMSDestinations).iterator();

      ForeignJMSDestinationMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (ForeignJMSDestinationMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public ForeignJMSDestinationMBean createForeignJMSDestination(String var1) {
      ForeignJMSDestinationMBeanImpl var2 = new ForeignJMSDestinationMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addForeignJMSDestination(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyForeignJMSDestination(ForeignJMSDestinationMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 126);
         ForeignJMSDestinationMBean[] var2 = this.getForeignJMSDestinations();
         ForeignJMSDestinationMBean[] var3 = (ForeignJMSDestinationMBean[])((ForeignJMSDestinationMBean[])this._getHelper()._removeElement(var2, ForeignJMSDestinationMBean.class, var1));
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
               this.setForeignJMSDestinations(var3);
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

   public void addJMSConnectionConsumer(JMSConnectionConsumerMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 127)) {
         JMSConnectionConsumerMBean[] var2;
         if (this._isSet(127)) {
            var2 = (JMSConnectionConsumerMBean[])((JMSConnectionConsumerMBean[])this._getHelper()._extendArray(this.getJMSConnectionConsumers(), JMSConnectionConsumerMBean.class, var1));
         } else {
            var2 = new JMSConnectionConsumerMBean[]{var1};
         }

         try {
            this.setJMSConnectionConsumers(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public JMSConnectionConsumerMBean[] getJMSConnectionConsumers() {
      return this._JMSConnectionConsumers;
   }

   public boolean isJMSConnectionConsumersSet() {
      return this._isSet(127);
   }

   public void removeJMSConnectionConsumer(JMSConnectionConsumerMBean var1) {
      this.destroyJMSConnectionConsumer(var1);
   }

   public void setJMSConnectionConsumers(JMSConnectionConsumerMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new JMSConnectionConsumerMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 127)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      JMSConnectionConsumerMBean[] var5 = this._JMSConnectionConsumers;
      this._JMSConnectionConsumers = (JMSConnectionConsumerMBean[])var4;
      this._postSet(127, var5, var4);
   }

   public JMSConnectionConsumerMBean lookupJMSConnectionConsumer(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._JMSConnectionConsumers).iterator();

      JMSConnectionConsumerMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (JMSConnectionConsumerMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public JMSConnectionConsumerMBean createJMSConnectionConsumer(String var1) {
      JMSConnectionConsumerMBeanImpl var2 = new JMSConnectionConsumerMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addJMSConnectionConsumer(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyJMSConnectionConsumer(JMSConnectionConsumerMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 127);
         JMSConnectionConsumerMBean[] var2 = this.getJMSConnectionConsumers();
         JMSConnectionConsumerMBean[] var3 = (JMSConnectionConsumerMBean[])((JMSConnectionConsumerMBean[])this._getHelper()._removeElement(var2, JMSConnectionConsumerMBean.class, var1));
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
               this.setJMSConnectionConsumers(var3);
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

   public ForeignJMSDestinationMBean createForeignJMSDestination(String var1, ForeignJMSDestinationMBean var2) {
      return this._customizer.createForeignJMSDestination(var1, var2);
   }

   public ForeignJMSConnectionFactoryMBean createForeignJMSConnectionFactory(String var1, ForeignJMSConnectionFactoryMBean var2) {
      return this._customizer.createForeignJMSConnectionFactory(var1, var2);
   }

   public JMSDistributedQueueMemberMBean createJMSDistributedQueueMember(String var1, JMSDistributedQueueMemberMBean var2) {
      return this._customizer.createJMSDistributedQueueMember(var1, var2);
   }

   public JMSDistributedTopicMemberMBean createJMSDistributedTopicMember(String var1, JMSDistributedTopicMemberMBean var2) {
      return this._customizer.createJMSDistributedTopicMember(var1, var2);
   }

   public JMSTopicMBean createJMSTopic(String var1, JMSTopicMBean var2) {
      return this._customizer.createJMSTopic(var1, var2);
   }

   public JMSQueueMBean createJMSQueue(String var1, JMSQueueMBean var2) {
      return this._customizer.createJMSQueue(var1, var2);
   }

   public boolean isAutoDeployForSubmodulesEnabled() {
      return this._AutoDeployForSubmodulesEnabled;
   }

   public boolean isAutoDeployForSubmodulesEnabledSet() {
      return this._isSet(128);
   }

   public void setAutoDeployForSubmodulesEnabled(boolean var1) {
      boolean var2 = this._AutoDeployForSubmodulesEnabled;
      this._AutoDeployForSubmodulesEnabled = var1;
      this._postSet(128, var2, var1);
   }

   public AdminConsoleMBean getAdminConsole() {
      return this._AdminConsole;
   }

   public boolean isAdminConsoleSet() {
      return this._isSet(129) || this._isAnythingSet((AbstractDescriptorBean)this.getAdminConsole());
   }

   public void setAdminConsole(AdminConsoleMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 129)) {
         this._postCreate(var2);
      }

      AdminConsoleMBean var3 = this._AdminConsole;
      this._AdminConsole = var1;
      this._postSet(129, var3, var1);
   }

   public boolean isInternalAppsDeployOnDemandEnabled() {
      if (!this._isSet(130)) {
         return !this._isProductionModeEnabled();
      } else {
         return this._InternalAppsDeployOnDemandEnabled;
      }
   }

   public boolean isInternalAppsDeployOnDemandEnabledSet() {
      return this._isSet(130);
   }

   public void setInternalAppsDeployOnDemandEnabled(boolean var1) {
      boolean var2 = this._InternalAppsDeployOnDemandEnabled;
      this._InternalAppsDeployOnDemandEnabled = var1;
      this._postSet(130, var2, var1);
   }

   public boolean isGuardianEnabled() {
      return this._GuardianEnabled;
   }

   public boolean isGuardianEnabledSet() {
      return this._isSet(131);
   }

   public void setGuardianEnabled(boolean var1) {
      boolean var2 = this._GuardianEnabled;
      this._GuardianEnabled = var1;
      this._postSet(131, var2, var1);
   }

   public boolean isOCMEnabled() {
      return this._OCMEnabled;
   }

   public boolean isOCMEnabledSet() {
      return this._isSet(132);
   }

   public void setOCMEnabled(boolean var1) {
      boolean var2 = this._OCMEnabled;
      this._OCMEnabled = var1;
      this._postSet(132, var2, var1);
   }

   public boolean isMsgIdPrefixCompatibilityEnabled() {
      return this._MsgIdPrefixCompatibilityEnabled;
   }

   public boolean isMsgIdPrefixCompatibilityEnabledSet() {
      return this._isSet(133);
   }

   public void setMsgIdPrefixCompatibilityEnabled(boolean var1) {
      boolean var2 = this._MsgIdPrefixCompatibilityEnabled;
      this._MsgIdPrefixCompatibilityEnabled = var1;
      this._postSet(133, var2, var1);
   }

   public void addCoherenceClusterSystemResource(CoherenceClusterSystemResourceMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 134)) {
         CoherenceClusterSystemResourceMBean[] var2;
         if (this._isSet(134)) {
            var2 = (CoherenceClusterSystemResourceMBean[])((CoherenceClusterSystemResourceMBean[])this._getHelper()._extendArray(this.getCoherenceClusterSystemResources(), CoherenceClusterSystemResourceMBean.class, var1));
         } else {
            var2 = new CoherenceClusterSystemResourceMBean[]{var1};
         }

         try {
            this.setCoherenceClusterSystemResources(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public CoherenceClusterSystemResourceMBean[] getCoherenceClusterSystemResources() {
      return this._CoherenceClusterSystemResources;
   }

   public boolean isCoherenceClusterSystemResourcesSet() {
      return this._isSet(134);
   }

   public void removeCoherenceClusterSystemResource(CoherenceClusterSystemResourceMBean var1) {
      this.destroyCoherenceClusterSystemResource(var1);
   }

   public void setCoherenceClusterSystemResources(CoherenceClusterSystemResourceMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new CoherenceClusterSystemResourceMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 134)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      CoherenceClusterSystemResourceMBean[] var5 = this._CoherenceClusterSystemResources;
      this._CoherenceClusterSystemResources = (CoherenceClusterSystemResourceMBean[])var4;
      this._postSet(134, var5, var4);
   }

   public CoherenceClusterSystemResourceMBean createCoherenceClusterSystemResource(String var1) {
      CoherenceClusterSystemResourceMBeanImpl var2 = new CoherenceClusterSystemResourceMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addCoherenceClusterSystemResource(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyCoherenceClusterSystemResource(CoherenceClusterSystemResourceMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 134);
         CoherenceClusterSystemResourceMBean[] var2 = this.getCoherenceClusterSystemResources();
         CoherenceClusterSystemResourceMBean[] var3 = (CoherenceClusterSystemResourceMBean[])((CoherenceClusterSystemResourceMBean[])this._getHelper()._removeElement(var2, CoherenceClusterSystemResourceMBean.class, var1));
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
               this.setCoherenceClusterSystemResources(var3);
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

   public CoherenceClusterSystemResourceMBean lookupCoherenceClusterSystemResource(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._CoherenceClusterSystemResources).iterator();

      CoherenceClusterSystemResourceMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (CoherenceClusterSystemResourceMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public RestfulManagementServicesMBean getRestfulManagementServices() {
      return this._RestfulManagementServices;
   }

   public boolean isRestfulManagementServicesSet() {
      return this._isSet(135) || this._isAnythingSet((AbstractDescriptorBean)this.getRestfulManagementServices());
   }

   public void setRestfulManagementServices(RestfulManagementServicesMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 135)) {
         this._postCreate(var2);
      }

      RestfulManagementServicesMBean var3 = this._RestfulManagementServices;
      this._RestfulManagementServices = var1;
      this._postSet(135, var3, var1);
   }

   public Object _getKey() {
      return this.getName();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
      DomainValidator.validateDomain(this);
      weblogic.descriptor.beangen.LegalChecks.checkIsSet("DomainVersion", this.isDomainVersionSet());
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
      return super._isAnythingSet() || this.isAdminConsoleSet() || this.isDeploymentConfigurationSet() || this.isEmbeddedLDAPSet() || this.isJMXSet() || this.isJPASet() || this.isJTASet() || this.isLogSet() || this.isRestfulManagementServicesSet() || this.isSNMPAgentSet() || this.isSecuritySet() || this.isSecurityConfigurationSet() || this.isSelfTuningSet() || this.isWebAppContainerSet();
   }

   private boolean _initializeProperty(int var1) {
      boolean var2 = var1 > -1;
      if (!var2) {
         var1 = 129;
      }

      try {
         switch (var1) {
            case 129:
               this._AdminConsole = new AdminConsoleMBeanImpl(this, 129);
               this._postCreate((AbstractDescriptorBean)this._AdminConsole);
               if (var2) {
                  break;
               }
            case 114:
               this._AdminServerMBean = null;
               if (var2) {
                  break;
               }
            case 104:
               this._AdminServerName = null;
               if (var2) {
                  break;
               }
            case 37:
               this._AdministrationPort = 9002;
               if (var2) {
                  break;
               }
            case 105:
               this._AdministrationProtocol = "t3s";
               if (var2) {
                  break;
               }
            case 46:
               this._AppDeployments = new AppDeploymentMBean[0];
               if (var2) {
                  break;
               }
            case 45:
               this._Applications = new ApplicationMBean[0];
               if (var2) {
                  break;
               }
            case 39:
               this._ArchiveConfigurationCount = 0;
               if (var2) {
                  break;
               }
            case 60:
               this._BasicRealms = new BasicRealmMBean[0];
               if (var2) {
                  break;
               }
            case 89:
               this._BridgeDestinations = new BridgeDestinationMBean[0];
               if (var2) {
                  break;
               }
            case 57:
               this._CachingRealms = new CachingRealmMBean[0];
               if (var2) {
                  break;
               }
            case 26:
               this._Clusters = new ClusterMBean[0];
               if (var2) {
                  break;
               }
            case 134:
               this._CoherenceClusterSystemResources = new CoherenceClusterSystemResourceMBean[0];
               if (var2) {
                  break;
               }
            case 25:
               this._CoherenceServers = new CoherenceServerMBean[0];
               if (var2) {
                  break;
               }
            case 43:
               this._ConfigurationAuditType = "none";
               if (var2) {
                  break;
               }
            case 41:
               this._ConfigurationVersion = null;
               if (var2) {
                  break;
               }
            case 21:
               this._ConsoleContextPath = "console";
               if (var2) {
                  break;
               }
            case 22:
               this._ConsoleExtensionDirectory = "console-ext";
               if (var2) {
                  break;
               }
            case 61:
               this._CustomRealms = new CustomRealmMBean[0];
               if (var2) {
                  break;
               }
            case 102:
               this._CustomResources = new CustomResourceMBean[0];
               if (var2) {
                  break;
               }
            case 14:
               this._DeploymentConfiguration = new DeploymentConfigurationMBeanImpl(this, 14);
               this._postCreate((AbstractDescriptorBean)this._DeploymentConfiguration);
               if (var2) {
                  break;
               }
            case 27:
               this._Deployments = new DeploymentMBean[0];
               if (var2) {
                  break;
               }
            case 49:
               this._DomainLibraries = new DomainLibraryMBean[0];
               if (var2) {
                  break;
               }
            case 97:
               this._DomainLogFilters = new DomainLogFilterMBean[0];
               if (var2) {
                  break;
               }
            case 7:
               this._DomainVersion = null;
               if (var2) {
                  break;
               }
            case 80:
               this._EJBContainer = null;
               if (var2) {
                  break;
               }
            case 35:
               this._EmbeddedLDAP = new EmbeddedLDAPMBeanImpl(this, 35);
               this._postCreate((AbstractDescriptorBean)this._EmbeddedLDAP);
               if (var2) {
                  break;
               }
            case 111:
               this._ErrorHandlings = new ErrorHandlingMBean[0];
               if (var2) {
                  break;
               }
            case 56:
               this._FileRealms = new FileRealmMBean[0];
               if (var2) {
                  break;
               }
            case 98:
               this._FileStores = new FileStoreMBean[0];
               if (var2) {
                  break;
               }
            case 28:
               this._FileT3s = new FileT3MBean[0];
               if (var2) {
                  break;
               }
            case 125:
               this._ForeignJMSConnectionFactories = new ForeignJMSConnectionFactoryMBean[0];
               if (var2) {
                  break;
               }
            case 126:
               this._ForeignJMSDestinations = new ForeignJMSDestinationMBean[0];
               if (var2) {
                  break;
               }
            case 90:
               this._ForeignJMSServers = new ForeignJMSServerMBean[0];
               if (var2) {
                  break;
               }
            case 103:
               this._ForeignJNDIProviders = new ForeignJNDIProviderMBean[0];
               if (var2) {
                  break;
               }
            case 47:
               this._InternalAppDeployments = new AppDeploymentMBean[0];
               if (var2) {
                  break;
               }
            case 50:
               this._InternalLibraries = new LibraryMBean[0];
               if (var2) {
                  break;
               }
            case 29:
               this._JDBCConnectionPools = new JDBCConnectionPoolMBean[0];
               if (var2) {
                  break;
               }
            case 52:
               this._JDBCDataSourceFactories = new JDBCDataSourceFactoryMBean[0];
               if (var2) {
                  break;
               }
            case 30:
               this._JDBCDataSources = new JDBCDataSourceMBean[0];
               if (var2) {
                  break;
               }
            case 32:
               this._JDBCMultiPools = new JDBCMultiPoolMBean[0];
               if (var2) {
                  break;
               }
            case 99:
               this._JDBCStores = new JDBCStoreMBean[0];
               if (var2) {
                  break;
               }
            case 107:
               this._JDBCSystemResources = new JDBCSystemResourceMBean[0];
               if (var2) {
                  break;
               }
            case 31:
               this._JDBCTxDataSources = new JDBCTxDataSourceMBean[0];
               if (var2) {
                  break;
               }
            case 88:
               this._JMSBridgeDestinations = new JMSBridgeDestinationMBean[0];
               if (var2) {
                  break;
               }
            case 127:
               this._JMSConnectionConsumers = new JMSConnectionConsumerMBean[0];
               if (var2) {
                  break;
               }
            case 86:
               this._JMSConnectionFactories = new JMSConnectionFactoryMBean[0];
               if (var2) {
                  break;
               }
            case 85:
               this._JMSDestinationKeys = new JMSDestinationKeyMBean[0];
               if (var2) {
                  break;
               }
            case 71:
               this._JMSDestinations = new JMSDestinationMBean[0];
               if (var2) {
                  break;
               }
            case 115:
               this._JMSDistributedQueueMembers = new JMSDistributedQueueMemberMBean[0];
               if (var2) {
                  break;
               }
            case 74:
               this._JMSDistributedQueues = new JMSDistributedQueueMBean[0];
               if (var2) {
                  break;
               }
            case 116:
               this._JMSDistributedTopicMembers = new JMSDistributedTopicMemberMBean[0];
               if (var2) {
                  break;
               }
            case 75:
               this._JMSDistributedTopics = new JMSDistributedTopicMBean[0];
               if (var2) {
                  break;
               }
            case 70:
               this._JMSFileStores = new JMSFileStoreMBean[0];
               if (var2) {
                  break;
               }
            case 100:
               this._JMSInteropModules = new JMSInteropModuleMBean[0];
               if (var2) {
                  break;
               }
            case 69:
               this._JMSJDBCStores = new JMSJDBCStoreMBean[0];
               if (var2) {
                  break;
               }
            case 72:
               this._JMSQueues = new JMSQueueMBean[0];
               if (var2) {
                  break;
               }
            case 67:
               this._JMSServers = new JMSServerMBean[0];
               if (var2) {
                  break;
               }
            case 87:
               this._JMSSessionPools = new JMSSessionPoolMBean[0];
               if (var2) {
                  break;
               }
            case 68:
               this._JMSStores = new JMSStoreMBean[0];
               if (var2) {
                  break;
               }
            case 101:
               this._JMSSystemResources = new JMSSystemResourceMBean[0];
               if (var2) {
                  break;
               }
            case 76:
               this._JMSTemplates = new JMSTemplateMBean[0];
               if (var2) {
                  break;
               }
            case 73:
               this._JMSTopics = new JMSTopicMBean[0];
               if (var2) {
                  break;
               }
            case 82:
               this._JMX = new JMXMBeanImpl(this, 82);
               this._postCreate((AbstractDescriptorBean)this._JMX);
               if (var2) {
                  break;
               }
            case 13:
               this._JPA = new JPAMBeanImpl(this, 13);
               this._postCreate((AbstractDescriptorBean)this._JPA);
               if (var2) {
                  break;
               }
            case 12:
               this._JTA = new JTAMBeanImpl(this, 12);
               this._postCreate((AbstractDescriptorBean)this._JTA);
               if (var2) {
                  break;
               }
            case 95:
               this._JoltConnectionPools = new JoltConnectionPoolMBean[0];
               if (var2) {
                  break;
               }
            case 62:
               this._LDAPRealms = new LDAPRealmMBean[0];
               if (var2) {
                  break;
               }
            case 8:
               this._LastModificationTime = 0L;
               if (var2) {
                  break;
               }
            case 48:
               this._Libraries = new LibraryMBean[0];
               if (var2) {
                  break;
               }
            case 16:
               this._Log = new LogMBeanImpl(this, 16);
               this._postCreate((AbstractDescriptorBean)this._Log);
               if (var2) {
                  break;
               }
            case 96:
               this._LogFilters = new LogFilterMBean[0];
               if (var2) {
                  break;
               }
            case 53:
               this._Machines = new MachineMBean[0];
               if (var2) {
                  break;
               }
            case 94:
               this._MailSessions = new MailSessionMBean[0];
               if (var2) {
                  break;
               }
            case 33:
               this._MessagingBridges = new MessagingBridgeMBean[0];
               if (var2) {
                  break;
               }
            case 113:
               this._MigratableRMIServices = new MigratableRMIServiceMBean[0];
               if (var2) {
                  break;
               }
            case 79:
               this._MigratableTargets = new MigratableTargetMBean[0];
               if (var2) {
                  break;
               }
            case 63:
               this._NTRealms = new NTRealmMBean[0];
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 77:
               this._NetworkChannels = new NetworkChannelMBean[0];
               if (var2) {
                  break;
               }
            case 59:
               this._PasswordPolicies = new PasswordPolicyMBean[0];
               if (var2) {
                  break;
               }
            case 84:
               this._PathServices = new PathServiceMBean[0];
               if (var2) {
                  break;
               }
            case 64:
               this._RDBMSRealms = new RDBMSRealmMBean[0];
               if (var2) {
                  break;
               }
            case 58:
               this._Realms = new RealmMBean[0];
               if (var2) {
                  break;
               }
            case 112:
               this._RemoteSAFContexts = new RemoteSAFContextMBean[0];
               if (var2) {
                  break;
               }
            case 135:
               this._RestfulManagementServices = new RestfulManagementServicesMBeanImpl(this, 135);
               this._postCreate((AbstractDescriptorBean)this._RestfulManagementServices);
               if (var2) {
                  break;
               }
            case 19:
               this._RootDirectory = null;
               if (var2) {
                  break;
               }
            case 109:
               this._SAFAgents = new SAFAgentMBean[0];
               if (var2) {
                  break;
               }
            case 17:
               this._SNMPAgent = new SNMPAgentMBeanImpl(this, 17);
               this._postCreate((AbstractDescriptorBean)this._SNMPAgent);
               if (var2) {
                  break;
               }
            case 18:
               this._SNMPAgentDeployments = new SNMPAgentDeploymentMBean[0];
               if (var2) {
                  break;
               }
            case 123:
               this._SNMPAttributeChanges = new SNMPAttributeChangeMBean[0];
               if (var2) {
                  break;
               }
            case 121:
               this._SNMPCounterMonitors = new SNMPCounterMonitorMBean[0];
               if (var2) {
                  break;
               }
            case 119:
               this._SNMPGaugeMonitors = new SNMPGaugeMonitorMBean[0];
               if (var2) {
                  break;
               }
            case 122:
               this._SNMPLogFilters = new SNMPLogFilterMBean[0];
               if (var2) {
                  break;
               }
            case 118:
               this._SNMPProxies = new SNMPProxyMBean[0];
               if (var2) {
                  break;
               }
            case 120:
               this._SNMPStringMonitors = new SNMPStringMonitorMBean[0];
               if (var2) {
                  break;
               }
            case 117:
               this._SNMPTrapDestinations = new SNMPTrapDestinationMBean[0];
               if (var2) {
                  break;
               }
            case 11:
               this._Security = new SecurityMBeanImpl(this, 11);
               this._postCreate((AbstractDescriptorBean)this._Security);
               if (var2) {
                  break;
               }
            case 10:
               this._SecurityConfiguration = new SecurityConfigurationMBeanImpl(this, 10);
               this._postCreate((AbstractDescriptorBean)this._SecurityConfiguration);
               if (var2) {
                  break;
               }
            case 83:
               this._SelfTuning = new SelfTuningMBeanImpl(this, 83);
               this._postCreate((AbstractDescriptorBean)this._SelfTuning);
               if (var2) {
                  break;
               }
            case 24:
               this._Servers = new ServerMBean[0];
               if (var2) {
                  break;
               }
            case 91:
               this._ShutdownClasses = new ShutdownClassMBean[0];
               if (var2) {
                  break;
               }
            case 93:
               this._SingletonServices = new SingletonServiceMBean[0];
               if (var2) {
                  break;
               }
            case 92:
               this._StartupClasses = new StartupClassMBean[0];
               if (var2) {
                  break;
               }
            case 108:
               this._SystemResources = new SystemResourceMBean[0];
               if (var2) {
                  break;
               }
            case 66:
               this._Targets = new TargetMBean[0];
               if (var2) {
                  break;
               }
            case 65:
               this._UnixRealms = new UnixRealmMBean[0];
               if (var2) {
                  break;
               }
            case 78:
               this._VirtualHosts = new VirtualHostMBean[0];
               if (var2) {
                  break;
               }
            case 106:
               this._WLDFSystemResources = new WLDFSystemResourceMBean[0];
               if (var2) {
                  break;
               }
            case 110:
               this._WLECConnectionPools = new WLECConnectionPoolMBean[0];
               if (var2) {
                  break;
               }
            case 51:
               this._WSReliableDeliveryPolicies = new WSReliableDeliveryPolicyMBean[0];
               if (var2) {
                  break;
               }
            case 15:
               this._WTCServers = new WTCServerMBean[0];
               if (var2) {
                  break;
               }
            case 81:
               this._WebAppContainer = new WebAppContainerMBeanImpl(this, 81);
               this._postCreate((AbstractDescriptorBean)this._WebAppContainer);
               if (var2) {
                  break;
               }
            case 124:
               this._WebserviceSecurities = new WebserviceSecurityMBean[0];
               if (var2) {
                  break;
               }
            case 54:
               this._XMLEntityCaches = new XMLEntityCacheMBean[0];
               if (var2) {
                  break;
               }
            case 55:
               this._XMLRegistries = new XMLRegistryMBean[0];
               if (var2) {
                  break;
               }
            case 9:
               this._Active = false;
               if (var2) {
                  break;
               }
            case 42:
               this._AdministrationMBeanAuditingEnabled = false;
               if (var2) {
                  break;
               }
            case 36:
               this._AdministrationPortEnabled = false;
               if (var2) {
                  break;
               }
            case 23:
               this._AutoConfigurationSaveEnabled = true;
               if (var2) {
                  break;
               }
            case 128:
               this._AutoDeployForSubmodulesEnabled = true;
               if (var2) {
                  break;
               }
            case 44:
               this._customizer.setClusterConstraintsEnabled(false);
               if (var2) {
                  break;
               }
            case 40:
               this._ConfigBackupEnabled = false;
               if (var2) {
                  break;
               }
            case 20:
               this._ConsoleEnabled = true;
               if (var2) {
                  break;
               }
            case 38:
               this._ExalogicOptimizationsEnabled = false;
               if (var2) {
                  break;
               }
            case 131:
               this._GuardianEnabled = false;
               if (var2) {
                  break;
               }
            case 130:
               this._InternalAppsDeployOnDemandEnabled = true;
               if (var2) {
                  break;
               }
            case 133:
               this._MsgIdPrefixCompatibilityEnabled = true;
               if (var2) {
                  break;
               }
            case 132:
               this._OCMEnabled = true;
               if (var2) {
                  break;
               }
            case 34:
               this._customizer.setProductionModeEnabled(false);
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
      return "Domain";
   }

   public void putValue(String var1, Object var2) {
      boolean var39;
      if (var1.equals("Active")) {
         var39 = this._Active;
         this._Active = (Boolean)var2;
         this._postSet(9, var39, this._Active);
      } else if (var1.equals("AdminConsole")) {
         AdminConsoleMBean var110 = this._AdminConsole;
         this._AdminConsole = (AdminConsoleMBean)var2;
         this._postSet(129, var110, this._AdminConsole);
      } else if (var1.equals("AdminServerMBean")) {
         AdminServerMBean var109 = this._AdminServerMBean;
         this._AdminServerMBean = (AdminServerMBean)var2;
         this._postSet(114, var109, this._AdminServerMBean);
      } else {
         String var34;
         if (var1.equals("AdminServerName")) {
            var34 = this._AdminServerName;
            this._AdminServerName = (String)var2;
            this._postSet(104, var34, this._AdminServerName);
         } else if (var1.equals("AdministrationMBeanAuditingEnabled")) {
            var39 = this._AdministrationMBeanAuditingEnabled;
            this._AdministrationMBeanAuditingEnabled = (Boolean)var2;
            this._postSet(42, var39, this._AdministrationMBeanAuditingEnabled);
         } else {
            int var107;
            if (var1.equals("AdministrationPort")) {
               var107 = this._AdministrationPort;
               this._AdministrationPort = (Integer)var2;
               this._postSet(37, var107, this._AdministrationPort);
            } else if (var1.equals("AdministrationPortEnabled")) {
               var39 = this._AdministrationPortEnabled;
               this._AdministrationPortEnabled = (Boolean)var2;
               this._postSet(36, var39, this._AdministrationPortEnabled);
            } else if (var1.equals("AdministrationProtocol")) {
               var34 = this._AdministrationProtocol;
               this._AdministrationProtocol = (String)var2;
               this._postSet(105, var34, this._AdministrationProtocol);
            } else {
               AppDeploymentMBean[] var84;
               if (var1.equals("AppDeployments")) {
                  var84 = this._AppDeployments;
                  this._AppDeployments = (AppDeploymentMBean[])((AppDeploymentMBean[])var2);
                  this._postSet(46, var84, this._AppDeployments);
               } else if (var1.equals("Applications")) {
                  ApplicationMBean[] var108 = this._Applications;
                  this._Applications = (ApplicationMBean[])((ApplicationMBean[])var2);
                  this._postSet(45, var108, this._Applications);
               } else if (var1.equals("ArchiveConfigurationCount")) {
                  var107 = this._ArchiveConfigurationCount;
                  this._ArchiveConfigurationCount = (Integer)var2;
                  this._postSet(39, var107, this._ArchiveConfigurationCount);
               } else if (var1.equals("AutoConfigurationSaveEnabled")) {
                  var39 = this._AutoConfigurationSaveEnabled;
                  this._AutoConfigurationSaveEnabled = (Boolean)var2;
                  this._postSet(23, var39, this._AutoConfigurationSaveEnabled);
               } else if (var1.equals("AutoDeployForSubmodulesEnabled")) {
                  var39 = this._AutoDeployForSubmodulesEnabled;
                  this._AutoDeployForSubmodulesEnabled = (Boolean)var2;
                  this._postSet(128, var39, this._AutoDeployForSubmodulesEnabled);
               } else if (var1.equals("BasicRealms")) {
                  BasicRealmMBean[] var106 = this._BasicRealms;
                  this._BasicRealms = (BasicRealmMBean[])((BasicRealmMBean[])var2);
                  this._postSet(60, var106, this._BasicRealms);
               } else if (var1.equals("BridgeDestinations")) {
                  BridgeDestinationMBean[] var105 = this._BridgeDestinations;
                  this._BridgeDestinations = (BridgeDestinationMBean[])((BridgeDestinationMBean[])var2);
                  this._postSet(89, var105, this._BridgeDestinations);
               } else if (var1.equals("CachingRealms")) {
                  CachingRealmMBean[] var104 = this._CachingRealms;
                  this._CachingRealms = (CachingRealmMBean[])((CachingRealmMBean[])var2);
                  this._postSet(57, var104, this._CachingRealms);
               } else if (var1.equals("ClusterConstraintsEnabled")) {
                  var39 = this._ClusterConstraintsEnabled;
                  this._ClusterConstraintsEnabled = (Boolean)var2;
                  this._postSet(44, var39, this._ClusterConstraintsEnabled);
               } else if (var1.equals("Clusters")) {
                  ClusterMBean[] var103 = this._Clusters;
                  this._Clusters = (ClusterMBean[])((ClusterMBean[])var2);
                  this._postSet(26, var103, this._Clusters);
               } else if (var1.equals("CoherenceClusterSystemResources")) {
                  CoherenceClusterSystemResourceMBean[] var102 = this._CoherenceClusterSystemResources;
                  this._CoherenceClusterSystemResources = (CoherenceClusterSystemResourceMBean[])((CoherenceClusterSystemResourceMBean[])var2);
                  this._postSet(134, var102, this._CoherenceClusterSystemResources);
               } else if (var1.equals("CoherenceServers")) {
                  CoherenceServerMBean[] var101 = this._CoherenceServers;
                  this._CoherenceServers = (CoherenceServerMBean[])((CoherenceServerMBean[])var2);
                  this._postSet(25, var101, this._CoherenceServers);
               } else if (var1.equals("ConfigBackupEnabled")) {
                  var39 = this._ConfigBackupEnabled;
                  this._ConfigBackupEnabled = (Boolean)var2;
                  this._postSet(40, var39, this._ConfigBackupEnabled);
               } else if (var1.equals("ConfigurationAuditType")) {
                  var34 = this._ConfigurationAuditType;
                  this._ConfigurationAuditType = (String)var2;
                  this._postSet(43, var34, this._ConfigurationAuditType);
               } else if (var1.equals("ConfigurationVersion")) {
                  var34 = this._ConfigurationVersion;
                  this._ConfigurationVersion = (String)var2;
                  this._postSet(41, var34, this._ConfigurationVersion);
               } else if (var1.equals("ConsoleContextPath")) {
                  var34 = this._ConsoleContextPath;
                  this._ConsoleContextPath = (String)var2;
                  this._postSet(21, var34, this._ConsoleContextPath);
               } else if (var1.equals("ConsoleEnabled")) {
                  var39 = this._ConsoleEnabled;
                  this._ConsoleEnabled = (Boolean)var2;
                  this._postSet(20, var39, this._ConsoleEnabled);
               } else if (var1.equals("ConsoleExtensionDirectory")) {
                  var34 = this._ConsoleExtensionDirectory;
                  this._ConsoleExtensionDirectory = (String)var2;
                  this._postSet(22, var34, this._ConsoleExtensionDirectory);
               } else if (var1.equals("CustomRealms")) {
                  CustomRealmMBean[] var100 = this._CustomRealms;
                  this._CustomRealms = (CustomRealmMBean[])((CustomRealmMBean[])var2);
                  this._postSet(61, var100, this._CustomRealms);
               } else if (var1.equals("CustomResources")) {
                  CustomResourceMBean[] var99 = this._CustomResources;
                  this._CustomResources = (CustomResourceMBean[])((CustomResourceMBean[])var2);
                  this._postSet(102, var99, this._CustomResources);
               } else if (var1.equals("DeploymentConfiguration")) {
                  DeploymentConfigurationMBean var98 = this._DeploymentConfiguration;
                  this._DeploymentConfiguration = (DeploymentConfigurationMBean)var2;
                  this._postSet(14, var98, this._DeploymentConfiguration);
               } else if (var1.equals("Deployments")) {
                  DeploymentMBean[] var97 = this._Deployments;
                  this._Deployments = (DeploymentMBean[])((DeploymentMBean[])var2);
                  this._postSet(27, var97, this._Deployments);
               } else if (var1.equals("DomainLibraries")) {
                  DomainLibraryMBean[] var96 = this._DomainLibraries;
                  this._DomainLibraries = (DomainLibraryMBean[])((DomainLibraryMBean[])var2);
                  this._postSet(49, var96, this._DomainLibraries);
               } else if (var1.equals("DomainLogFilters")) {
                  DomainLogFilterMBean[] var95 = this._DomainLogFilters;
                  this._DomainLogFilters = (DomainLogFilterMBean[])((DomainLogFilterMBean[])var2);
                  this._postSet(97, var95, this._DomainLogFilters);
               } else if (var1.equals("DomainVersion")) {
                  var34 = this._DomainVersion;
                  this._DomainVersion = (String)var2;
                  this._postSet(7, var34, this._DomainVersion);
               } else if (var1.equals("EJBContainer")) {
                  EJBContainerMBean var94 = this._EJBContainer;
                  this._EJBContainer = (EJBContainerMBean)var2;
                  this._postSet(80, var94, this._EJBContainer);
               } else if (var1.equals("EmbeddedLDAP")) {
                  EmbeddedLDAPMBean var93 = this._EmbeddedLDAP;
                  this._EmbeddedLDAP = (EmbeddedLDAPMBean)var2;
                  this._postSet(35, var93, this._EmbeddedLDAP);
               } else if (var1.equals("ErrorHandlings")) {
                  ErrorHandlingMBean[] var92 = this._ErrorHandlings;
                  this._ErrorHandlings = (ErrorHandlingMBean[])((ErrorHandlingMBean[])var2);
                  this._postSet(111, var92, this._ErrorHandlings);
               } else if (var1.equals("ExalogicOptimizationsEnabled")) {
                  var39 = this._ExalogicOptimizationsEnabled;
                  this._ExalogicOptimizationsEnabled = (Boolean)var2;
                  this._postSet(38, var39, this._ExalogicOptimizationsEnabled);
               } else if (var1.equals("FileRealms")) {
                  FileRealmMBean[] var91 = this._FileRealms;
                  this._FileRealms = (FileRealmMBean[])((FileRealmMBean[])var2);
                  this._postSet(56, var91, this._FileRealms);
               } else if (var1.equals("FileStores")) {
                  FileStoreMBean[] var90 = this._FileStores;
                  this._FileStores = (FileStoreMBean[])((FileStoreMBean[])var2);
                  this._postSet(98, var90, this._FileStores);
               } else if (var1.equals("FileT3s")) {
                  FileT3MBean[] var89 = this._FileT3s;
                  this._FileT3s = (FileT3MBean[])((FileT3MBean[])var2);
                  this._postSet(28, var89, this._FileT3s);
               } else if (var1.equals("ForeignJMSConnectionFactories")) {
                  ForeignJMSConnectionFactoryMBean[] var88 = this._ForeignJMSConnectionFactories;
                  this._ForeignJMSConnectionFactories = (ForeignJMSConnectionFactoryMBean[])((ForeignJMSConnectionFactoryMBean[])var2);
                  this._postSet(125, var88, this._ForeignJMSConnectionFactories);
               } else if (var1.equals("ForeignJMSDestinations")) {
                  ForeignJMSDestinationMBean[] var87 = this._ForeignJMSDestinations;
                  this._ForeignJMSDestinations = (ForeignJMSDestinationMBean[])((ForeignJMSDestinationMBean[])var2);
                  this._postSet(126, var87, this._ForeignJMSDestinations);
               } else if (var1.equals("ForeignJMSServers")) {
                  ForeignJMSServerMBean[] var86 = this._ForeignJMSServers;
                  this._ForeignJMSServers = (ForeignJMSServerMBean[])((ForeignJMSServerMBean[])var2);
                  this._postSet(90, var86, this._ForeignJMSServers);
               } else if (var1.equals("ForeignJNDIProviders")) {
                  ForeignJNDIProviderMBean[] var85 = this._ForeignJNDIProviders;
                  this._ForeignJNDIProviders = (ForeignJNDIProviderMBean[])((ForeignJNDIProviderMBean[])var2);
                  this._postSet(103, var85, this._ForeignJNDIProviders);
               } else if (var1.equals("GuardianEnabled")) {
                  var39 = this._GuardianEnabled;
                  this._GuardianEnabled = (Boolean)var2;
                  this._postSet(131, var39, this._GuardianEnabled);
               } else if (var1.equals("InternalAppDeployments")) {
                  var84 = this._InternalAppDeployments;
                  this._InternalAppDeployments = (AppDeploymentMBean[])((AppDeploymentMBean[])var2);
                  this._postSet(47, var84, this._InternalAppDeployments);
               } else if (var1.equals("InternalAppsDeployOnDemandEnabled")) {
                  var39 = this._InternalAppsDeployOnDemandEnabled;
                  this._InternalAppsDeployOnDemandEnabled = (Boolean)var2;
                  this._postSet(130, var39, this._InternalAppsDeployOnDemandEnabled);
               } else {
                  LibraryMBean[] var51;
                  if (var1.equals("InternalLibraries")) {
                     var51 = this._InternalLibraries;
                     this._InternalLibraries = (LibraryMBean[])((LibraryMBean[])var2);
                     this._postSet(50, var51, this._InternalLibraries);
                  } else if (var1.equals("JDBCConnectionPools")) {
                     JDBCConnectionPoolMBean[] var83 = this._JDBCConnectionPools;
                     this._JDBCConnectionPools = (JDBCConnectionPoolMBean[])((JDBCConnectionPoolMBean[])var2);
                     this._postSet(29, var83, this._JDBCConnectionPools);
                  } else if (var1.equals("JDBCDataSourceFactories")) {
                     JDBCDataSourceFactoryMBean[] var82 = this._JDBCDataSourceFactories;
                     this._JDBCDataSourceFactories = (JDBCDataSourceFactoryMBean[])((JDBCDataSourceFactoryMBean[])var2);
                     this._postSet(52, var82, this._JDBCDataSourceFactories);
                  } else if (var1.equals("JDBCDataSources")) {
                     JDBCDataSourceMBean[] var81 = this._JDBCDataSources;
                     this._JDBCDataSources = (JDBCDataSourceMBean[])((JDBCDataSourceMBean[])var2);
                     this._postSet(30, var81, this._JDBCDataSources);
                  } else if (var1.equals("JDBCMultiPools")) {
                     JDBCMultiPoolMBean[] var80 = this._JDBCMultiPools;
                     this._JDBCMultiPools = (JDBCMultiPoolMBean[])((JDBCMultiPoolMBean[])var2);
                     this._postSet(32, var80, this._JDBCMultiPools);
                  } else if (var1.equals("JDBCStores")) {
                     JDBCStoreMBean[] var79 = this._JDBCStores;
                     this._JDBCStores = (JDBCStoreMBean[])((JDBCStoreMBean[])var2);
                     this._postSet(99, var79, this._JDBCStores);
                  } else if (var1.equals("JDBCSystemResources")) {
                     JDBCSystemResourceMBean[] var78 = this._JDBCSystemResources;
                     this._JDBCSystemResources = (JDBCSystemResourceMBean[])((JDBCSystemResourceMBean[])var2);
                     this._postSet(107, var78, this._JDBCSystemResources);
                  } else if (var1.equals("JDBCTxDataSources")) {
                     JDBCTxDataSourceMBean[] var77 = this._JDBCTxDataSources;
                     this._JDBCTxDataSources = (JDBCTxDataSourceMBean[])((JDBCTxDataSourceMBean[])var2);
                     this._postSet(31, var77, this._JDBCTxDataSources);
                  } else if (var1.equals("JMSBridgeDestinations")) {
                     JMSBridgeDestinationMBean[] var76 = this._JMSBridgeDestinations;
                     this._JMSBridgeDestinations = (JMSBridgeDestinationMBean[])((JMSBridgeDestinationMBean[])var2);
                     this._postSet(88, var76, this._JMSBridgeDestinations);
                  } else if (var1.equals("JMSConnectionConsumers")) {
                     JMSConnectionConsumerMBean[] var75 = this._JMSConnectionConsumers;
                     this._JMSConnectionConsumers = (JMSConnectionConsumerMBean[])((JMSConnectionConsumerMBean[])var2);
                     this._postSet(127, var75, this._JMSConnectionConsumers);
                  } else if (var1.equals("JMSConnectionFactories")) {
                     JMSConnectionFactoryMBean[] var74 = this._JMSConnectionFactories;
                     this._JMSConnectionFactories = (JMSConnectionFactoryMBean[])((JMSConnectionFactoryMBean[])var2);
                     this._postSet(86, var74, this._JMSConnectionFactories);
                  } else if (var1.equals("JMSDestinationKeys")) {
                     JMSDestinationKeyMBean[] var73 = this._JMSDestinationKeys;
                     this._JMSDestinationKeys = (JMSDestinationKeyMBean[])((JMSDestinationKeyMBean[])var2);
                     this._postSet(85, var73, this._JMSDestinationKeys);
                  } else if (var1.equals("JMSDestinations")) {
                     JMSDestinationMBean[] var72 = this._JMSDestinations;
                     this._JMSDestinations = (JMSDestinationMBean[])((JMSDestinationMBean[])var2);
                     this._postSet(71, var72, this._JMSDestinations);
                  } else if (var1.equals("JMSDistributedQueueMembers")) {
                     JMSDistributedQueueMemberMBean[] var71 = this._JMSDistributedQueueMembers;
                     this._JMSDistributedQueueMembers = (JMSDistributedQueueMemberMBean[])((JMSDistributedQueueMemberMBean[])var2);
                     this._postSet(115, var71, this._JMSDistributedQueueMembers);
                  } else if (var1.equals("JMSDistributedQueues")) {
                     JMSDistributedQueueMBean[] var70 = this._JMSDistributedQueues;
                     this._JMSDistributedQueues = (JMSDistributedQueueMBean[])((JMSDistributedQueueMBean[])var2);
                     this._postSet(74, var70, this._JMSDistributedQueues);
                  } else if (var1.equals("JMSDistributedTopicMembers")) {
                     JMSDistributedTopicMemberMBean[] var69 = this._JMSDistributedTopicMembers;
                     this._JMSDistributedTopicMembers = (JMSDistributedTopicMemberMBean[])((JMSDistributedTopicMemberMBean[])var2);
                     this._postSet(116, var69, this._JMSDistributedTopicMembers);
                  } else if (var1.equals("JMSDistributedTopics")) {
                     JMSDistributedTopicMBean[] var68 = this._JMSDistributedTopics;
                     this._JMSDistributedTopics = (JMSDistributedTopicMBean[])((JMSDistributedTopicMBean[])var2);
                     this._postSet(75, var68, this._JMSDistributedTopics);
                  } else if (var1.equals("JMSFileStores")) {
                     JMSFileStoreMBean[] var67 = this._JMSFileStores;
                     this._JMSFileStores = (JMSFileStoreMBean[])((JMSFileStoreMBean[])var2);
                     this._postSet(70, var67, this._JMSFileStores);
                  } else if (var1.equals("JMSInteropModules")) {
                     JMSInteropModuleMBean[] var66 = this._JMSInteropModules;
                     this._JMSInteropModules = (JMSInteropModuleMBean[])((JMSInteropModuleMBean[])var2);
                     this._postSet(100, var66, this._JMSInteropModules);
                  } else if (var1.equals("JMSJDBCStores")) {
                     JMSJDBCStoreMBean[] var65 = this._JMSJDBCStores;
                     this._JMSJDBCStores = (JMSJDBCStoreMBean[])((JMSJDBCStoreMBean[])var2);
                     this._postSet(69, var65, this._JMSJDBCStores);
                  } else if (var1.equals("JMSQueues")) {
                     JMSQueueMBean[] var64 = this._JMSQueues;
                     this._JMSQueues = (JMSQueueMBean[])((JMSQueueMBean[])var2);
                     this._postSet(72, var64, this._JMSQueues);
                  } else if (var1.equals("JMSServers")) {
                     JMSServerMBean[] var63 = this._JMSServers;
                     this._JMSServers = (JMSServerMBean[])((JMSServerMBean[])var2);
                     this._postSet(67, var63, this._JMSServers);
                  } else if (var1.equals("JMSSessionPools")) {
                     JMSSessionPoolMBean[] var62 = this._JMSSessionPools;
                     this._JMSSessionPools = (JMSSessionPoolMBean[])((JMSSessionPoolMBean[])var2);
                     this._postSet(87, var62, this._JMSSessionPools);
                  } else if (var1.equals("JMSStores")) {
                     JMSStoreMBean[] var61 = this._JMSStores;
                     this._JMSStores = (JMSStoreMBean[])((JMSStoreMBean[])var2);
                     this._postSet(68, var61, this._JMSStores);
                  } else if (var1.equals("JMSSystemResources")) {
                     JMSSystemResourceMBean[] var60 = this._JMSSystemResources;
                     this._JMSSystemResources = (JMSSystemResourceMBean[])((JMSSystemResourceMBean[])var2);
                     this._postSet(101, var60, this._JMSSystemResources);
                  } else if (var1.equals("JMSTemplates")) {
                     JMSTemplateMBean[] var59 = this._JMSTemplates;
                     this._JMSTemplates = (JMSTemplateMBean[])((JMSTemplateMBean[])var2);
                     this._postSet(76, var59, this._JMSTemplates);
                  } else if (var1.equals("JMSTopics")) {
                     JMSTopicMBean[] var58 = this._JMSTopics;
                     this._JMSTopics = (JMSTopicMBean[])((JMSTopicMBean[])var2);
                     this._postSet(73, var58, this._JMSTopics);
                  } else if (var1.equals("JMX")) {
                     JMXMBean var57 = this._JMX;
                     this._JMX = (JMXMBean)var2;
                     this._postSet(82, var57, this._JMX);
                  } else if (var1.equals("JPA")) {
                     JPAMBean var56 = this._JPA;
                     this._JPA = (JPAMBean)var2;
                     this._postSet(13, var56, this._JPA);
                  } else if (var1.equals("JTA")) {
                     JTAMBean var55 = this._JTA;
                     this._JTA = (JTAMBean)var2;
                     this._postSet(12, var55, this._JTA);
                  } else if (var1.equals("JoltConnectionPools")) {
                     JoltConnectionPoolMBean[] var54 = this._JoltConnectionPools;
                     this._JoltConnectionPools = (JoltConnectionPoolMBean[])((JoltConnectionPoolMBean[])var2);
                     this._postSet(95, var54, this._JoltConnectionPools);
                  } else if (var1.equals("LDAPRealms")) {
                     LDAPRealmMBean[] var53 = this._LDAPRealms;
                     this._LDAPRealms = (LDAPRealmMBean[])((LDAPRealmMBean[])var2);
                     this._postSet(62, var53, this._LDAPRealms);
                  } else if (var1.equals("LastModificationTime")) {
                     long var52 = this._LastModificationTime;
                     this._LastModificationTime = (Long)var2;
                     this._postSet(8, var52, this._LastModificationTime);
                  } else if (var1.equals("Libraries")) {
                     var51 = this._Libraries;
                     this._Libraries = (LibraryMBean[])((LibraryMBean[])var2);
                     this._postSet(48, var51, this._Libraries);
                  } else if (var1.equals("Log")) {
                     LogMBean var50 = this._Log;
                     this._Log = (LogMBean)var2;
                     this._postSet(16, var50, this._Log);
                  } else if (var1.equals("LogFilters")) {
                     LogFilterMBean[] var49 = this._LogFilters;
                     this._LogFilters = (LogFilterMBean[])((LogFilterMBean[])var2);
                     this._postSet(96, var49, this._LogFilters);
                  } else if (var1.equals("Machines")) {
                     MachineMBean[] var48 = this._Machines;
                     this._Machines = (MachineMBean[])((MachineMBean[])var2);
                     this._postSet(53, var48, this._Machines);
                  } else if (var1.equals("MailSessions")) {
                     MailSessionMBean[] var47 = this._MailSessions;
                     this._MailSessions = (MailSessionMBean[])((MailSessionMBean[])var2);
                     this._postSet(94, var47, this._MailSessions);
                  } else if (var1.equals("MessagingBridges")) {
                     MessagingBridgeMBean[] var46 = this._MessagingBridges;
                     this._MessagingBridges = (MessagingBridgeMBean[])((MessagingBridgeMBean[])var2);
                     this._postSet(33, var46, this._MessagingBridges);
                  } else if (var1.equals("MigratableRMIServices")) {
                     MigratableRMIServiceMBean[] var45 = this._MigratableRMIServices;
                     this._MigratableRMIServices = (MigratableRMIServiceMBean[])((MigratableRMIServiceMBean[])var2);
                     this._postSet(113, var45, this._MigratableRMIServices);
                  } else if (var1.equals("MigratableTargets")) {
                     MigratableTargetMBean[] var44 = this._MigratableTargets;
                     this._MigratableTargets = (MigratableTargetMBean[])((MigratableTargetMBean[])var2);
                     this._postSet(79, var44, this._MigratableTargets);
                  } else if (var1.equals("MsgIdPrefixCompatibilityEnabled")) {
                     var39 = this._MsgIdPrefixCompatibilityEnabled;
                     this._MsgIdPrefixCompatibilityEnabled = (Boolean)var2;
                     this._postSet(133, var39, this._MsgIdPrefixCompatibilityEnabled);
                  } else if (var1.equals("NTRealms")) {
                     NTRealmMBean[] var43 = this._NTRealms;
                     this._NTRealms = (NTRealmMBean[])((NTRealmMBean[])var2);
                     this._postSet(63, var43, this._NTRealms);
                  } else if (var1.equals("Name")) {
                     var34 = this._Name;
                     this._Name = (String)var2;
                     this._postSet(2, var34, this._Name);
                  } else if (var1.equals("NetworkChannels")) {
                     NetworkChannelMBean[] var42 = this._NetworkChannels;
                     this._NetworkChannels = (NetworkChannelMBean[])((NetworkChannelMBean[])var2);
                     this._postSet(77, var42, this._NetworkChannels);
                  } else if (var1.equals("OCMEnabled")) {
                     var39 = this._OCMEnabled;
                     this._OCMEnabled = (Boolean)var2;
                     this._postSet(132, var39, this._OCMEnabled);
                  } else if (var1.equals("PasswordPolicies")) {
                     PasswordPolicyMBean[] var41 = this._PasswordPolicies;
                     this._PasswordPolicies = (PasswordPolicyMBean[])((PasswordPolicyMBean[])var2);
                     this._postSet(59, var41, this._PasswordPolicies);
                  } else if (var1.equals("PathServices")) {
                     PathServiceMBean[] var40 = this._PathServices;
                     this._PathServices = (PathServiceMBean[])((PathServiceMBean[])var2);
                     this._postSet(84, var40, this._PathServices);
                  } else if (var1.equals("ProductionModeEnabled")) {
                     var39 = this._ProductionModeEnabled;
                     this._ProductionModeEnabled = (Boolean)var2;
                     this._postSet(34, var39, this._ProductionModeEnabled);
                  } else if (var1.equals("RDBMSRealms")) {
                     RDBMSRealmMBean[] var38 = this._RDBMSRealms;
                     this._RDBMSRealms = (RDBMSRealmMBean[])((RDBMSRealmMBean[])var2);
                     this._postSet(64, var38, this._RDBMSRealms);
                  } else if (var1.equals("Realms")) {
                     RealmMBean[] var37 = this._Realms;
                     this._Realms = (RealmMBean[])((RealmMBean[])var2);
                     this._postSet(58, var37, this._Realms);
                  } else if (var1.equals("RemoteSAFContexts")) {
                     RemoteSAFContextMBean[] var36 = this._RemoteSAFContexts;
                     this._RemoteSAFContexts = (RemoteSAFContextMBean[])((RemoteSAFContextMBean[])var2);
                     this._postSet(112, var36, this._RemoteSAFContexts);
                  } else if (var1.equals("RestfulManagementServices")) {
                     RestfulManagementServicesMBean var35 = this._RestfulManagementServices;
                     this._RestfulManagementServices = (RestfulManagementServicesMBean)var2;
                     this._postSet(135, var35, this._RestfulManagementServices);
                  } else if (var1.equals("RootDirectory")) {
                     var34 = this._RootDirectory;
                     this._RootDirectory = (String)var2;
                     this._postSet(19, var34, this._RootDirectory);
                  } else if (var1.equals("SAFAgents")) {
                     SAFAgentMBean[] var33 = this._SAFAgents;
                     this._SAFAgents = (SAFAgentMBean[])((SAFAgentMBean[])var2);
                     this._postSet(109, var33, this._SAFAgents);
                  } else if (var1.equals("SNMPAgent")) {
                     SNMPAgentMBean var32 = this._SNMPAgent;
                     this._SNMPAgent = (SNMPAgentMBean)var2;
                     this._postSet(17, var32, this._SNMPAgent);
                  } else if (var1.equals("SNMPAgentDeployments")) {
                     SNMPAgentDeploymentMBean[] var31 = this._SNMPAgentDeployments;
                     this._SNMPAgentDeployments = (SNMPAgentDeploymentMBean[])((SNMPAgentDeploymentMBean[])var2);
                     this._postSet(18, var31, this._SNMPAgentDeployments);
                  } else if (var1.equals("SNMPAttributeChanges")) {
                     SNMPAttributeChangeMBean[] var30 = this._SNMPAttributeChanges;
                     this._SNMPAttributeChanges = (SNMPAttributeChangeMBean[])((SNMPAttributeChangeMBean[])var2);
                     this._postSet(123, var30, this._SNMPAttributeChanges);
                  } else if (var1.equals("SNMPCounterMonitors")) {
                     SNMPCounterMonitorMBean[] var29 = this._SNMPCounterMonitors;
                     this._SNMPCounterMonitors = (SNMPCounterMonitorMBean[])((SNMPCounterMonitorMBean[])var2);
                     this._postSet(121, var29, this._SNMPCounterMonitors);
                  } else if (var1.equals("SNMPGaugeMonitors")) {
                     SNMPGaugeMonitorMBean[] var28 = this._SNMPGaugeMonitors;
                     this._SNMPGaugeMonitors = (SNMPGaugeMonitorMBean[])((SNMPGaugeMonitorMBean[])var2);
                     this._postSet(119, var28, this._SNMPGaugeMonitors);
                  } else if (var1.equals("SNMPLogFilters")) {
                     SNMPLogFilterMBean[] var27 = this._SNMPLogFilters;
                     this._SNMPLogFilters = (SNMPLogFilterMBean[])((SNMPLogFilterMBean[])var2);
                     this._postSet(122, var27, this._SNMPLogFilters);
                  } else if (var1.equals("SNMPProxies")) {
                     SNMPProxyMBean[] var26 = this._SNMPProxies;
                     this._SNMPProxies = (SNMPProxyMBean[])((SNMPProxyMBean[])var2);
                     this._postSet(118, var26, this._SNMPProxies);
                  } else if (var1.equals("SNMPStringMonitors")) {
                     SNMPStringMonitorMBean[] var25 = this._SNMPStringMonitors;
                     this._SNMPStringMonitors = (SNMPStringMonitorMBean[])((SNMPStringMonitorMBean[])var2);
                     this._postSet(120, var25, this._SNMPStringMonitors);
                  } else if (var1.equals("SNMPTrapDestinations")) {
                     SNMPTrapDestinationMBean[] var24 = this._SNMPTrapDestinations;
                     this._SNMPTrapDestinations = (SNMPTrapDestinationMBean[])((SNMPTrapDestinationMBean[])var2);
                     this._postSet(117, var24, this._SNMPTrapDestinations);
                  } else if (var1.equals("Security")) {
                     SecurityMBean var23 = this._Security;
                     this._Security = (SecurityMBean)var2;
                     this._postSet(11, var23, this._Security);
                  } else if (var1.equals("SecurityConfiguration")) {
                     SecurityConfigurationMBean var22 = this._SecurityConfiguration;
                     this._SecurityConfiguration = (SecurityConfigurationMBean)var2;
                     this._postSet(10, var22, this._SecurityConfiguration);
                  } else if (var1.equals("SelfTuning")) {
                     SelfTuningMBean var21 = this._SelfTuning;
                     this._SelfTuning = (SelfTuningMBean)var2;
                     this._postSet(83, var21, this._SelfTuning);
                  } else if (var1.equals("Servers")) {
                     ServerMBean[] var20 = this._Servers;
                     this._Servers = (ServerMBean[])((ServerMBean[])var2);
                     this._postSet(24, var20, this._Servers);
                  } else if (var1.equals("ShutdownClasses")) {
                     ShutdownClassMBean[] var19 = this._ShutdownClasses;
                     this._ShutdownClasses = (ShutdownClassMBean[])((ShutdownClassMBean[])var2);
                     this._postSet(91, var19, this._ShutdownClasses);
                  } else if (var1.equals("SingletonServices")) {
                     SingletonServiceMBean[] var18 = this._SingletonServices;
                     this._SingletonServices = (SingletonServiceMBean[])((SingletonServiceMBean[])var2);
                     this._postSet(93, var18, this._SingletonServices);
                  } else if (var1.equals("StartupClasses")) {
                     StartupClassMBean[] var17 = this._StartupClasses;
                     this._StartupClasses = (StartupClassMBean[])((StartupClassMBean[])var2);
                     this._postSet(92, var17, this._StartupClasses);
                  } else if (var1.equals("SystemResources")) {
                     SystemResourceMBean[] var16 = this._SystemResources;
                     this._SystemResources = (SystemResourceMBean[])((SystemResourceMBean[])var2);
                     this._postSet(108, var16, this._SystemResources);
                  } else if (var1.equals("Targets")) {
                     TargetMBean[] var15 = this._Targets;
                     this._Targets = (TargetMBean[])((TargetMBean[])var2);
                     this._postSet(66, var15, this._Targets);
                  } else if (var1.equals("UnixRealms")) {
                     UnixRealmMBean[] var14 = this._UnixRealms;
                     this._UnixRealms = (UnixRealmMBean[])((UnixRealmMBean[])var2);
                     this._postSet(65, var14, this._UnixRealms);
                  } else if (var1.equals("VirtualHosts")) {
                     VirtualHostMBean[] var13 = this._VirtualHosts;
                     this._VirtualHosts = (VirtualHostMBean[])((VirtualHostMBean[])var2);
                     this._postSet(78, var13, this._VirtualHosts);
                  } else if (var1.equals("WLDFSystemResources")) {
                     WLDFSystemResourceMBean[] var12 = this._WLDFSystemResources;
                     this._WLDFSystemResources = (WLDFSystemResourceMBean[])((WLDFSystemResourceMBean[])var2);
                     this._postSet(106, var12, this._WLDFSystemResources);
                  } else if (var1.equals("WLECConnectionPools")) {
                     WLECConnectionPoolMBean[] var11 = this._WLECConnectionPools;
                     this._WLECConnectionPools = (WLECConnectionPoolMBean[])((WLECConnectionPoolMBean[])var2);
                     this._postSet(110, var11, this._WLECConnectionPools);
                  } else if (var1.equals("WSReliableDeliveryPolicies")) {
                     WSReliableDeliveryPolicyMBean[] var10 = this._WSReliableDeliveryPolicies;
                     this._WSReliableDeliveryPolicies = (WSReliableDeliveryPolicyMBean[])((WSReliableDeliveryPolicyMBean[])var2);
                     this._postSet(51, var10, this._WSReliableDeliveryPolicies);
                  } else if (var1.equals("WTCServers")) {
                     WTCServerMBean[] var9 = this._WTCServers;
                     this._WTCServers = (WTCServerMBean[])((WTCServerMBean[])var2);
                     this._postSet(15, var9, this._WTCServers);
                  } else if (var1.equals("WebAppContainer")) {
                     WebAppContainerMBean var8 = this._WebAppContainer;
                     this._WebAppContainer = (WebAppContainerMBean)var2;
                     this._postSet(81, var8, this._WebAppContainer);
                  } else if (var1.equals("WebserviceSecurities")) {
                     WebserviceSecurityMBean[] var7 = this._WebserviceSecurities;
                     this._WebserviceSecurities = (WebserviceSecurityMBean[])((WebserviceSecurityMBean[])var2);
                     this._postSet(124, var7, this._WebserviceSecurities);
                  } else if (var1.equals("XMLEntityCaches")) {
                     XMLEntityCacheMBean[] var6 = this._XMLEntityCaches;
                     this._XMLEntityCaches = (XMLEntityCacheMBean[])((XMLEntityCacheMBean[])var2);
                     this._postSet(54, var6, this._XMLEntityCaches);
                  } else if (var1.equals("XMLRegistries")) {
                     XMLRegistryMBean[] var5 = this._XMLRegistries;
                     this._XMLRegistries = (XMLRegistryMBean[])((XMLRegistryMBean[])var2);
                     this._postSet(55, var5, this._XMLRegistries);
                  } else if (var1.equals("customizer")) {
                     Domain var3 = this._customizer;
                     this._customizer = (Domain)var2;
                  } else {
                     super.putValue(var1, var2);
                  }
               }
            }
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("Active")) {
         return new Boolean(this._Active);
      } else if (var1.equals("AdminConsole")) {
         return this._AdminConsole;
      } else if (var1.equals("AdminServerMBean")) {
         return this._AdminServerMBean;
      } else if (var1.equals("AdminServerName")) {
         return this._AdminServerName;
      } else if (var1.equals("AdministrationMBeanAuditingEnabled")) {
         return new Boolean(this._AdministrationMBeanAuditingEnabled);
      } else if (var1.equals("AdministrationPort")) {
         return new Integer(this._AdministrationPort);
      } else if (var1.equals("AdministrationPortEnabled")) {
         return new Boolean(this._AdministrationPortEnabled);
      } else if (var1.equals("AdministrationProtocol")) {
         return this._AdministrationProtocol;
      } else if (var1.equals("AppDeployments")) {
         return this._AppDeployments;
      } else if (var1.equals("Applications")) {
         return this._Applications;
      } else if (var1.equals("ArchiveConfigurationCount")) {
         return new Integer(this._ArchiveConfigurationCount);
      } else if (var1.equals("AutoConfigurationSaveEnabled")) {
         return new Boolean(this._AutoConfigurationSaveEnabled);
      } else if (var1.equals("AutoDeployForSubmodulesEnabled")) {
         return new Boolean(this._AutoDeployForSubmodulesEnabled);
      } else if (var1.equals("BasicRealms")) {
         return this._BasicRealms;
      } else if (var1.equals("BridgeDestinations")) {
         return this._BridgeDestinations;
      } else if (var1.equals("CachingRealms")) {
         return this._CachingRealms;
      } else if (var1.equals("ClusterConstraintsEnabled")) {
         return new Boolean(this._ClusterConstraintsEnabled);
      } else if (var1.equals("Clusters")) {
         return this._Clusters;
      } else if (var1.equals("CoherenceClusterSystemResources")) {
         return this._CoherenceClusterSystemResources;
      } else if (var1.equals("CoherenceServers")) {
         return this._CoherenceServers;
      } else if (var1.equals("ConfigBackupEnabled")) {
         return new Boolean(this._ConfigBackupEnabled);
      } else if (var1.equals("ConfigurationAuditType")) {
         return this._ConfigurationAuditType;
      } else if (var1.equals("ConfigurationVersion")) {
         return this._ConfigurationVersion;
      } else if (var1.equals("ConsoleContextPath")) {
         return this._ConsoleContextPath;
      } else if (var1.equals("ConsoleEnabled")) {
         return new Boolean(this._ConsoleEnabled);
      } else if (var1.equals("ConsoleExtensionDirectory")) {
         return this._ConsoleExtensionDirectory;
      } else if (var1.equals("CustomRealms")) {
         return this._CustomRealms;
      } else if (var1.equals("CustomResources")) {
         return this._CustomResources;
      } else if (var1.equals("DeploymentConfiguration")) {
         return this._DeploymentConfiguration;
      } else if (var1.equals("Deployments")) {
         return this._Deployments;
      } else if (var1.equals("DomainLibraries")) {
         return this._DomainLibraries;
      } else if (var1.equals("DomainLogFilters")) {
         return this._DomainLogFilters;
      } else if (var1.equals("DomainVersion")) {
         return this._DomainVersion;
      } else if (var1.equals("EJBContainer")) {
         return this._EJBContainer;
      } else if (var1.equals("EmbeddedLDAP")) {
         return this._EmbeddedLDAP;
      } else if (var1.equals("ErrorHandlings")) {
         return this._ErrorHandlings;
      } else if (var1.equals("ExalogicOptimizationsEnabled")) {
         return new Boolean(this._ExalogicOptimizationsEnabled);
      } else if (var1.equals("FileRealms")) {
         return this._FileRealms;
      } else if (var1.equals("FileStores")) {
         return this._FileStores;
      } else if (var1.equals("FileT3s")) {
         return this._FileT3s;
      } else if (var1.equals("ForeignJMSConnectionFactories")) {
         return this._ForeignJMSConnectionFactories;
      } else if (var1.equals("ForeignJMSDestinations")) {
         return this._ForeignJMSDestinations;
      } else if (var1.equals("ForeignJMSServers")) {
         return this._ForeignJMSServers;
      } else if (var1.equals("ForeignJNDIProviders")) {
         return this._ForeignJNDIProviders;
      } else if (var1.equals("GuardianEnabled")) {
         return new Boolean(this._GuardianEnabled);
      } else if (var1.equals("InternalAppDeployments")) {
         return this._InternalAppDeployments;
      } else if (var1.equals("InternalAppsDeployOnDemandEnabled")) {
         return new Boolean(this._InternalAppsDeployOnDemandEnabled);
      } else if (var1.equals("InternalLibraries")) {
         return this._InternalLibraries;
      } else if (var1.equals("JDBCConnectionPools")) {
         return this._JDBCConnectionPools;
      } else if (var1.equals("JDBCDataSourceFactories")) {
         return this._JDBCDataSourceFactories;
      } else if (var1.equals("JDBCDataSources")) {
         return this._JDBCDataSources;
      } else if (var1.equals("JDBCMultiPools")) {
         return this._JDBCMultiPools;
      } else if (var1.equals("JDBCStores")) {
         return this._JDBCStores;
      } else if (var1.equals("JDBCSystemResources")) {
         return this._JDBCSystemResources;
      } else if (var1.equals("JDBCTxDataSources")) {
         return this._JDBCTxDataSources;
      } else if (var1.equals("JMSBridgeDestinations")) {
         return this._JMSBridgeDestinations;
      } else if (var1.equals("JMSConnectionConsumers")) {
         return this._JMSConnectionConsumers;
      } else if (var1.equals("JMSConnectionFactories")) {
         return this._JMSConnectionFactories;
      } else if (var1.equals("JMSDestinationKeys")) {
         return this._JMSDestinationKeys;
      } else if (var1.equals("JMSDestinations")) {
         return this._JMSDestinations;
      } else if (var1.equals("JMSDistributedQueueMembers")) {
         return this._JMSDistributedQueueMembers;
      } else if (var1.equals("JMSDistributedQueues")) {
         return this._JMSDistributedQueues;
      } else if (var1.equals("JMSDistributedTopicMembers")) {
         return this._JMSDistributedTopicMembers;
      } else if (var1.equals("JMSDistributedTopics")) {
         return this._JMSDistributedTopics;
      } else if (var1.equals("JMSFileStores")) {
         return this._JMSFileStores;
      } else if (var1.equals("JMSInteropModules")) {
         return this._JMSInteropModules;
      } else if (var1.equals("JMSJDBCStores")) {
         return this._JMSJDBCStores;
      } else if (var1.equals("JMSQueues")) {
         return this._JMSQueues;
      } else if (var1.equals("JMSServers")) {
         return this._JMSServers;
      } else if (var1.equals("JMSSessionPools")) {
         return this._JMSSessionPools;
      } else if (var1.equals("JMSStores")) {
         return this._JMSStores;
      } else if (var1.equals("JMSSystemResources")) {
         return this._JMSSystemResources;
      } else if (var1.equals("JMSTemplates")) {
         return this._JMSTemplates;
      } else if (var1.equals("JMSTopics")) {
         return this._JMSTopics;
      } else if (var1.equals("JMX")) {
         return this._JMX;
      } else if (var1.equals("JPA")) {
         return this._JPA;
      } else if (var1.equals("JTA")) {
         return this._JTA;
      } else if (var1.equals("JoltConnectionPools")) {
         return this._JoltConnectionPools;
      } else if (var1.equals("LDAPRealms")) {
         return this._LDAPRealms;
      } else if (var1.equals("LastModificationTime")) {
         return new Long(this._LastModificationTime);
      } else if (var1.equals("Libraries")) {
         return this._Libraries;
      } else if (var1.equals("Log")) {
         return this._Log;
      } else if (var1.equals("LogFilters")) {
         return this._LogFilters;
      } else if (var1.equals("Machines")) {
         return this._Machines;
      } else if (var1.equals("MailSessions")) {
         return this._MailSessions;
      } else if (var1.equals("MessagingBridges")) {
         return this._MessagingBridges;
      } else if (var1.equals("MigratableRMIServices")) {
         return this._MigratableRMIServices;
      } else if (var1.equals("MigratableTargets")) {
         return this._MigratableTargets;
      } else if (var1.equals("MsgIdPrefixCompatibilityEnabled")) {
         return new Boolean(this._MsgIdPrefixCompatibilityEnabled);
      } else if (var1.equals("NTRealms")) {
         return this._NTRealms;
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("NetworkChannels")) {
         return this._NetworkChannels;
      } else if (var1.equals("OCMEnabled")) {
         return new Boolean(this._OCMEnabled);
      } else if (var1.equals("PasswordPolicies")) {
         return this._PasswordPolicies;
      } else if (var1.equals("PathServices")) {
         return this._PathServices;
      } else if (var1.equals("ProductionModeEnabled")) {
         return new Boolean(this._ProductionModeEnabled);
      } else if (var1.equals("RDBMSRealms")) {
         return this._RDBMSRealms;
      } else if (var1.equals("Realms")) {
         return this._Realms;
      } else if (var1.equals("RemoteSAFContexts")) {
         return this._RemoteSAFContexts;
      } else if (var1.equals("RestfulManagementServices")) {
         return this._RestfulManagementServices;
      } else if (var1.equals("RootDirectory")) {
         return this._RootDirectory;
      } else if (var1.equals("SAFAgents")) {
         return this._SAFAgents;
      } else if (var1.equals("SNMPAgent")) {
         return this._SNMPAgent;
      } else if (var1.equals("SNMPAgentDeployments")) {
         return this._SNMPAgentDeployments;
      } else if (var1.equals("SNMPAttributeChanges")) {
         return this._SNMPAttributeChanges;
      } else if (var1.equals("SNMPCounterMonitors")) {
         return this._SNMPCounterMonitors;
      } else if (var1.equals("SNMPGaugeMonitors")) {
         return this._SNMPGaugeMonitors;
      } else if (var1.equals("SNMPLogFilters")) {
         return this._SNMPLogFilters;
      } else if (var1.equals("SNMPProxies")) {
         return this._SNMPProxies;
      } else if (var1.equals("SNMPStringMonitors")) {
         return this._SNMPStringMonitors;
      } else if (var1.equals("SNMPTrapDestinations")) {
         return this._SNMPTrapDestinations;
      } else if (var1.equals("Security")) {
         return this._Security;
      } else if (var1.equals("SecurityConfiguration")) {
         return this._SecurityConfiguration;
      } else if (var1.equals("SelfTuning")) {
         return this._SelfTuning;
      } else if (var1.equals("Servers")) {
         return this._Servers;
      } else if (var1.equals("ShutdownClasses")) {
         return this._ShutdownClasses;
      } else if (var1.equals("SingletonServices")) {
         return this._SingletonServices;
      } else if (var1.equals("StartupClasses")) {
         return this._StartupClasses;
      } else if (var1.equals("SystemResources")) {
         return this._SystemResources;
      } else if (var1.equals("Targets")) {
         return this._Targets;
      } else if (var1.equals("UnixRealms")) {
         return this._UnixRealms;
      } else if (var1.equals("VirtualHosts")) {
         return this._VirtualHosts;
      } else if (var1.equals("WLDFSystemResources")) {
         return this._WLDFSystemResources;
      } else if (var1.equals("WLECConnectionPools")) {
         return this._WLECConnectionPools;
      } else if (var1.equals("WSReliableDeliveryPolicies")) {
         return this._WSReliableDeliveryPolicies;
      } else if (var1.equals("WTCServers")) {
         return this._WTCServers;
      } else if (var1.equals("WebAppContainer")) {
         return this._WebAppContainer;
      } else if (var1.equals("WebserviceSecurities")) {
         return this._WebserviceSecurities;
      } else if (var1.equals("XMLEntityCaches")) {
         return this._XMLEntityCaches;
      } else if (var1.equals("XMLRegistries")) {
         return this._XMLRegistries;
      } else {
         return var1.equals("customizer") ? this._customizer : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 3:
               if (var1.equals("jmx")) {
                  return 82;
               }

               if (var1.equals("jpa")) {
                  return 13;
               }

               if (var1.equals("jta")) {
                  return 12;
               }

               if (var1.equals("log")) {
                  return 16;
               }
               break;
            case 4:
               if (var1.equals("name")) {
                  return 2;
               }
               break;
            case 5:
               if (var1.equals("realm")) {
                  return 58;
               }
               break;
            case 6:
               if (var1.equals("filet3")) {
                  return 28;
               }

               if (var1.equals("server")) {
                  return 24;
               }

               if (var1.equals("target")) {
                  return 66;
               }

               if (var1.equals("active")) {
                  return 9;
               }
               break;
            case 7:
               if (var1.equals("cluster")) {
                  return 26;
               }

               if (var1.equals("library")) {
                  return 48;
               }

               if (var1.equals("machine")) {
                  return 53;
               }
               break;
            case 8:
               if (var1.equals("nt-realm")) {
                  return 63;
               }

               if (var1.equals("security")) {
                  return 11;
               }
               break;
            case 9:
               if (var1.equals("jms-queue")) {
                  return 72;
               }

               if (var1.equals("jms-store")) {
                  return 68;
               }

               if (var1.equals("jms-topic")) {
                  return 73;
               }

               if (var1.equals("saf-agent")) {
                  return 109;
               }
               break;
            case 10:
               if (var1.equals("deployment")) {
                  return 27;
               }

               if (var1.equals("file-realm")) {
                  return 56;
               }

               if (var1.equals("file-store")) {
                  return 98;
               }

               if (var1.equals("jdbc-store")) {
                  return 99;
               }

               if (var1.equals("jms-server")) {
                  return 67;
               }

               if (var1.equals("ldap-realm")) {
                  return 62;
               }

               if (var1.equals("log-filter")) {
                  return 96;
               }

               if (var1.equals("snmp-agent")) {
                  return 17;
               }

               if (var1.equals("snmp-proxy")) {
                  return 118;
               }

               if (var1.equals("unix-realm")) {
                  return 65;
               }

               if (var1.equals("wtc-server")) {
                  return 15;
               }
               break;
            case 11:
               if (var1.equals("application")) {
                  return 45;
               }

               if (var1.equals("basic-realm")) {
                  return 60;
               }

               if (var1.equals("rdbms-realm")) {
                  return 64;
               }

               if (var1.equals("self-tuning")) {
                  return 83;
               }

               if (var1.equals("ocm-enabled")) {
                  return 132;
               }
               break;
            case 12:
               if (var1.equals("custom-realm")) {
                  return 61;
               }

               if (var1.equals("embeddedldap")) {
                  return 35;
               }

               if (var1.equals("jms-template")) {
                  return 76;
               }

               if (var1.equals("mail-session")) {
                  return 94;
               }

               if (var1.equals("path-service")) {
                  return 84;
               }

               if (var1.equals("virtual-host")) {
                  return 78;
               }

               if (var1.equals("xml-registry")) {
                  return 55;
               }
               break;
            case 13:
               if (var1.equals("admin-console")) {
                  return 129;
               }

               if (var1.equals("caching-realm")) {
                  return 57;
               }

               if (var1.equals("ejb-container")) {
                  return 80;
               }

               if (var1.equals("startup-class")) {
                  return 92;
               }
               break;
            case 14:
               if (var1.equals("app-deployment")) {
                  return 46;
               }

               if (var1.equals("domain-library")) {
                  return 49;
               }

               if (var1.equals("domain-version")) {
                  return 7;
               }

               if (var1.equals("error-handling")) {
                  return 111;
               }

               if (var1.equals("jms-file-store")) {
                  return 70;
               }

               if (var1.equals("jms-jdbc-store")) {
                  return 69;
               }

               if (var1.equals("root-directory")) {
                  return 19;
               }

               if (var1.equals("shutdown-class")) {
                  return 91;
               }
               break;
            case 15:
               if (var1.equals("custom-resource")) {
                  return 102;
               }

               if (var1.equals("jdbc-multi-pool")) {
                  return 32;
               }

               if (var1.equals("jms-destination")) {
                  return 71;
               }

               if (var1.equals("network-channel")) {
                  return 77;
               }

               if (var1.equals("password-policy")) {
                  return 59;
               }

               if (var1.equals("snmp-log-filter")) {
                  return 122;
               }

               if (var1.equals("system-resource")) {
                  return 108;
               }

               if (var1.equals("console-enabled")) {
                  return 20;
               }
               break;
            case 16:
               if (var1.equals("coherence-server")) {
                  return 25;
               }

               if (var1.equals("internal-library")) {
                  return 50;
               }

               if (var1.equals("jdbc-data-source")) {
                  return 30;
               }

               if (var1.equals("jms-session-pool")) {
                  return 87;
               }

               if (var1.equals("messaging-bridge")) {
                  return 33;
               }

               if (var1.equals("xml-entity-cache")) {
                  return 54;
               }

               if (var1.equals("guardian-enabled")) {
                  return 131;
               }
               break;
            case 17:
               if (var1.equals("admin-server-name")) {
                  return 104;
               }

               if (var1.equals("domain-log-filter")) {
                  return 97;
               }

               if (var1.equals("migratable-target")) {
                  return 79;
               }

               if (var1.equals("singleton-service")) {
                  return 93;
               }

               if (var1.equals("web-app-container")) {
                  return 81;
               }
               break;
            case 18:
               if (var1.equals("admin-serverm-bean")) {
                  return 114;
               }

               if (var1.equals("bridge-destination")) {
                  return 89;
               }

               if (var1.equals("foreign-jms-server")) {
                  return 90;
               }

               if (var1.equals("jms-interop-module")) {
                  return 100;
               }

               if (var1.equals("remote-saf-context")) {
                  return 112;
               }

               if (var1.equals("snmp-gauge-monitor")) {
                  return 119;
               }
               break;
            case 19:
               if (var1.equals("administration-port")) {
                  return 37;
               }

               if (var1.equals("jdbc-tx-data-source")) {
                  return 31;
               }

               if (var1.equals("jms-destination-key")) {
                  return 85;
               }

               if (var1.equals("jms-system-resource")) {
                  return 101;
               }

               if (var1.equals("snmp-string-monitor")) {
                  return 120;
               }

               if (var1.equals("webservice-security")) {
                  return 124;
               }
               break;
            case 20:
               if (var1.equals("console-context-path")) {
                  return 21;
               }

               if (var1.equals("jdbc-connection-pool")) {
                  return 29;
               }

               if (var1.equals("jdbc-system-resource")) {
                  return 107;
               }

               if (var1.equals("jolt-connection-pool")) {
                  return 95;
               }

               if (var1.equals("snmp-counter-monitor")) {
                  return 121;
               }

               if (var1.equals("wldf-system-resource")) {
                  return 106;
               }

               if (var1.equals("wlec-connection-pool")) {
                  return 110;
               }
               break;
            case 21:
               if (var1.equals("configuration-version")) {
                  return 41;
               }

               if (var1.equals("foreign-jndi-provider")) {
                  return 103;
               }

               if (var1.equals("jms-distributed-queue")) {
                  return 74;
               }

               if (var1.equals("jms-distributed-topic")) {
                  return 75;
               }

               if (var1.equals("migratablermi-service")) {
                  return 113;
               }

               if (var1.equals("snmp-agent-deployment")) {
                  return 18;
               }

               if (var1.equals("snmp-attribute-change")) {
                  return 123;
               }

               if (var1.equals("snmp-trap-destination")) {
                  return 117;
               }

               if (var1.equals("config-backup-enabled")) {
                  return 40;
               }
               break;
            case 22:
               if (var1.equals("jms-bridge-destination")) {
                  return 88;
               }

               if (var1.equals("jms-connection-factory")) {
                  return 86;
               }

               if (var1.equals("last-modification-time")) {
                  return 8;
               }

               if (var1.equals("security-configuration")) {
                  return 10;
               }
               break;
            case 23:
               if (var1.equals("administration-protocol")) {
                  return 105;
               }

               if (var1.equals("foreign-jms-destination")) {
                  return 126;
               }

               if (var1.equals("internal-app-deployment")) {
                  return 47;
               }

               if (var1.equals("jms-connection-consumer")) {
                  return 127;
               }

               if (var1.equals("production-mode-enabled")) {
                  return 34;
               }
               break;
            case 24:
               if (var1.equals("configuration-audit-type")) {
                  return 43;
               }

               if (var1.equals("deployment-configuration")) {
                  return 14;
               }

               if (var1.equals("jdbc-data-source-factory")) {
                  return 52;
               }
            case 25:
            case 26:
            case 29:
            case 32:
            case 36:
            default:
               break;
            case 27:
               if (var1.equals("archive-configuration-count")) {
                  return 39;
               }

               if (var1.equals("console-extension-directory")) {
                  return 22;
               }

               if (var1.equals("restful-management-services")) {
                  return 135;
               }

               if (var1.equals("ws-reliable-delivery-policy")) {
                  return 51;
               }

               if (var1.equals("administration-port-enabled")) {
                  return 36;
               }

               if (var1.equals("cluster-constraints-enabled")) {
                  return 44;
               }
               break;
            case 28:
               if (var1.equals("jms-distributed-queue-member")) {
                  return 115;
               }

               if (var1.equals("jms-distributed-topic-member")) {
                  return 116;
               }
               break;
            case 30:
               if (var1.equals("foreign-jms-connection-factory")) {
                  return 125;
               }

               if (var1.equals("exalogic-optimizations-enabled")) {
                  return 38;
               }
               break;
            case 31:
               if (var1.equals("auto-configuration-save-enabled")) {
                  return 23;
               }
               break;
            case 33:
               if (var1.equals("coherence-cluster-system-resource")) {
                  return 134;
               }
               break;
            case 34:
               if (var1.equals("auto-deploy-for-submodules-enabled")) {
                  return 128;
               }
               break;
            case 35:
               if (var1.equals("msg-id-prefix-compatibility-enabled")) {
                  return 133;
               }
               break;
            case 37:
               if (var1.equals("administrationm-bean-auditing-enabled")) {
                  return 42;
               }
               break;
            case 38:
               if (var1.equals("internal-apps-deploy-on-demand-enabled")) {
                  return 130;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 10:
               return new SecurityConfigurationMBeanImpl.SchemaHelper2();
            case 11:
               return new SecurityMBeanImpl.SchemaHelper2();
            case 12:
               return new JTAMBeanImpl.SchemaHelper2();
            case 13:
               return new JPAMBeanImpl.SchemaHelper2();
            case 14:
               return new DeploymentConfigurationMBeanImpl.SchemaHelper2();
            case 15:
               return new WTCServerMBeanImpl.SchemaHelper2();
            case 16:
               return new LogMBeanImpl.SchemaHelper2();
            case 17:
               return new SNMPAgentMBeanImpl.SchemaHelper2();
            case 18:
               return new SNMPAgentDeploymentMBeanImpl.SchemaHelper2();
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 27:
            case 34:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 47:
            case 50:
            case 60:
            case 66:
            case 68:
            case 71:
            case 104:
            case 105:
            case 108:
            case 128:
            case 130:
            case 131:
            case 132:
            case 133:
            default:
               return super.getSchemaHelper(var1);
            case 24:
               return new ServerMBeanImpl.SchemaHelper2();
            case 25:
               return new CoherenceServerMBeanImpl.SchemaHelper2();
            case 26:
               return new ClusterMBeanImpl.SchemaHelper2();
            case 28:
               return new FileT3MBeanImpl.SchemaHelper2();
            case 29:
               return new JDBCConnectionPoolMBeanImpl.SchemaHelper2();
            case 30:
               return new JDBCDataSourceMBeanImpl.SchemaHelper2();
            case 31:
               return new JDBCTxDataSourceMBeanImpl.SchemaHelper2();
            case 32:
               return new JDBCMultiPoolMBeanImpl.SchemaHelper2();
            case 33:
               return new MessagingBridgeMBeanImpl.SchemaHelper2();
            case 35:
               return new EmbeddedLDAPMBeanImpl.SchemaHelper2();
            case 45:
               return new ApplicationMBeanImpl.SchemaHelper2();
            case 46:
               return new AppDeploymentMBeanImpl.SchemaHelper2();
            case 48:
               return new LibraryMBeanImpl.SchemaHelper2();
            case 49:
               return new DomainLibraryMBeanImpl.SchemaHelper2();
            case 51:
               return new WSReliableDeliveryPolicyMBeanImpl.SchemaHelper2();
            case 52:
               return new JDBCDataSourceFactoryMBeanImpl.SchemaHelper2();
            case 53:
               return new MachineMBeanImpl.SchemaHelper2();
            case 54:
               return new XMLEntityCacheMBeanImpl.SchemaHelper2();
            case 55:
               return new XMLRegistryMBeanImpl.SchemaHelper2();
            case 56:
               return new FileRealmMBeanImpl.SchemaHelper2();
            case 57:
               return new CachingRealmMBeanImpl.SchemaHelper2();
            case 58:
               return new RealmMBeanImpl.SchemaHelper2();
            case 59:
               return new PasswordPolicyMBeanImpl.SchemaHelper2();
            case 61:
               return new CustomRealmMBeanImpl.SchemaHelper2();
            case 62:
               return new LDAPRealmMBeanImpl.SchemaHelper2();
            case 63:
               return new NTRealmMBeanImpl.SchemaHelper2();
            case 64:
               return new RDBMSRealmMBeanImpl.SchemaHelper2();
            case 65:
               return new UnixRealmMBeanImpl.SchemaHelper2();
            case 67:
               return new JMSServerMBeanImpl.SchemaHelper2();
            case 69:
               return new JMSJDBCStoreMBeanImpl.SchemaHelper2();
            case 70:
               return new JMSFileStoreMBeanImpl.SchemaHelper2();
            case 72:
               return new JMSQueueMBeanImpl.SchemaHelper2();
            case 73:
               return new JMSTopicMBeanImpl.SchemaHelper2();
            case 74:
               return new JMSDistributedQueueMBeanImpl.SchemaHelper2();
            case 75:
               return new JMSDistributedTopicMBeanImpl.SchemaHelper2();
            case 76:
               return new JMSTemplateMBeanImpl.SchemaHelper2();
            case 77:
               return new NetworkChannelMBeanImpl.SchemaHelper2();
            case 78:
               return new VirtualHostMBeanImpl.SchemaHelper2();
            case 79:
               return new MigratableTargetMBeanImpl.SchemaHelper2();
            case 80:
               return new EJBContainerMBeanImpl.SchemaHelper2();
            case 81:
               return new WebAppContainerMBeanImpl.SchemaHelper2();
            case 82:
               return new JMXMBeanImpl.SchemaHelper2();
            case 83:
               return new SelfTuningMBeanImpl.SchemaHelper2();
            case 84:
               return new PathServiceMBeanImpl.SchemaHelper2();
            case 85:
               return new JMSDestinationKeyMBeanImpl.SchemaHelper2();
            case 86:
               return new JMSConnectionFactoryMBeanImpl.SchemaHelper2();
            case 87:
               return new JMSSessionPoolMBeanImpl.SchemaHelper2();
            case 88:
               return new JMSBridgeDestinationMBeanImpl.SchemaHelper2();
            case 89:
               return new BridgeDestinationMBeanImpl.SchemaHelper2();
            case 90:
               return new ForeignJMSServerMBeanImpl.SchemaHelper2();
            case 91:
               return new ShutdownClassMBeanImpl.SchemaHelper2();
            case 92:
               return new StartupClassMBeanImpl.SchemaHelper2();
            case 93:
               return new SingletonServiceMBeanImpl.SchemaHelper2();
            case 94:
               return new MailSessionMBeanImpl.SchemaHelper2();
            case 95:
               return new JoltConnectionPoolMBeanImpl.SchemaHelper2();
            case 96:
               return new LogFilterMBeanImpl.SchemaHelper2();
            case 97:
               return new DomainLogFilterMBeanImpl.SchemaHelper2();
            case 98:
               return new FileStoreMBeanImpl.SchemaHelper2();
            case 99:
               return new JDBCStoreMBeanImpl.SchemaHelper2();
            case 100:
               return new JMSInteropModuleMBeanImpl.SchemaHelper2();
            case 101:
               return new JMSSystemResourceMBeanImpl.SchemaHelper2();
            case 102:
               return new CustomResourceMBeanImpl.SchemaHelper2();
            case 103:
               return new ForeignJNDIProviderMBeanImpl.SchemaHelper2();
            case 106:
               return new WLDFSystemResourceMBeanImpl.SchemaHelper2();
            case 107:
               return new JDBCSystemResourceMBeanImpl.SchemaHelper2();
            case 109:
               return new SAFAgentMBeanImpl.SchemaHelper2();
            case 110:
               return new WLECConnectionPoolMBeanImpl.SchemaHelper2();
            case 111:
               return new ErrorHandlingMBeanImpl.SchemaHelper2();
            case 112:
               return new RemoteSAFContextMBeanImpl.SchemaHelper2();
            case 113:
               return new MigratableRMIServiceMBeanImpl.SchemaHelper2();
            case 114:
               return new AdminServerMBeanImpl.SchemaHelper2();
            case 115:
               return new JMSDistributedQueueMemberMBeanImpl.SchemaHelper2();
            case 116:
               return new JMSDistributedTopicMemberMBeanImpl.SchemaHelper2();
            case 117:
               return new SNMPTrapDestinationMBeanImpl.SchemaHelper2();
            case 118:
               return new SNMPProxyMBeanImpl.SchemaHelper2();
            case 119:
               return new SNMPGaugeMonitorMBeanImpl.SchemaHelper2();
            case 120:
               return new SNMPStringMonitorMBeanImpl.SchemaHelper2();
            case 121:
               return new SNMPCounterMonitorMBeanImpl.SchemaHelper2();
            case 122:
               return new SNMPLogFilterMBeanImpl.SchemaHelper2();
            case 123:
               return new SNMPAttributeChangeMBeanImpl.SchemaHelper2();
            case 124:
               return new WebserviceSecurityMBeanImpl.SchemaHelper2();
            case 125:
               return new ForeignJMSConnectionFactoryMBeanImpl.SchemaHelper2();
            case 126:
               return new ForeignJMSDestinationMBeanImpl.SchemaHelper2();
            case 127:
               return new JMSConnectionConsumerMBeanImpl.SchemaHelper2();
            case 129:
               return new AdminConsoleMBeanImpl.SchemaHelper2();
            case 134:
               return new CoherenceClusterSystemResourceMBeanImpl.SchemaHelper2();
            case 135:
               return new RestfulManagementServicesMBeanImpl.SchemaHelper2();
         }
      }

      public String getRootElementName() {
         return "domainm";
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
               return "domain-version";
            case 8:
               return "last-modification-time";
            case 9:
               return "active";
            case 10:
               return "security-configuration";
            case 11:
               return "security";
            case 12:
               return "jta";
            case 13:
               return "jpa";
            case 14:
               return "deployment-configuration";
            case 15:
               return "wtc-server";
            case 16:
               return "log";
            case 17:
               return "snmp-agent";
            case 18:
               return "snmp-agent-deployment";
            case 19:
               return "root-directory";
            case 20:
               return "console-enabled";
            case 21:
               return "console-context-path";
            case 22:
               return "console-extension-directory";
            case 23:
               return "auto-configuration-save-enabled";
            case 24:
               return "server";
            case 25:
               return "coherence-server";
            case 26:
               return "cluster";
            case 27:
               return "deployment";
            case 28:
               return "filet3";
            case 29:
               return "jdbc-connection-pool";
            case 30:
               return "jdbc-data-source";
            case 31:
               return "jdbc-tx-data-source";
            case 32:
               return "jdbc-multi-pool";
            case 33:
               return "messaging-bridge";
            case 34:
               return "production-mode-enabled";
            case 35:
               return "embeddedldap";
            case 36:
               return "administration-port-enabled";
            case 37:
               return "administration-port";
            case 38:
               return "exalogic-optimizations-enabled";
            case 39:
               return "archive-configuration-count";
            case 40:
               return "config-backup-enabled";
            case 41:
               return "configuration-version";
            case 42:
               return "administrationm-bean-auditing-enabled";
            case 43:
               return "configuration-audit-type";
            case 44:
               return "cluster-constraints-enabled";
            case 45:
               return "application";
            case 46:
               return "app-deployment";
            case 47:
               return "internal-app-deployment";
            case 48:
               return "library";
            case 49:
               return "domain-library";
            case 50:
               return "internal-library";
            case 51:
               return "ws-reliable-delivery-policy";
            case 52:
               return "jdbc-data-source-factory";
            case 53:
               return "machine";
            case 54:
               return "xml-entity-cache";
            case 55:
               return "xml-registry";
            case 56:
               return "file-realm";
            case 57:
               return "caching-realm";
            case 58:
               return "realm";
            case 59:
               return "password-policy";
            case 60:
               return "basic-realm";
            case 61:
               return "custom-realm";
            case 62:
               return "ldap-realm";
            case 63:
               return "nt-realm";
            case 64:
               return "rdbms-realm";
            case 65:
               return "unix-realm";
            case 66:
               return "target";
            case 67:
               return "jms-server";
            case 68:
               return "jms-store";
            case 69:
               return "jms-jdbc-store";
            case 70:
               return "jms-file-store";
            case 71:
               return "jms-destination";
            case 72:
               return "jms-queue";
            case 73:
               return "jms-topic";
            case 74:
               return "jms-distributed-queue";
            case 75:
               return "jms-distributed-topic";
            case 76:
               return "jms-template";
            case 77:
               return "network-channel";
            case 78:
               return "virtual-host";
            case 79:
               return "migratable-target";
            case 80:
               return "ejb-container";
            case 81:
               return "web-app-container";
            case 82:
               return "jmx";
            case 83:
               return "self-tuning";
            case 84:
               return "path-service";
            case 85:
               return "jms-destination-key";
            case 86:
               return "jms-connection-factory";
            case 87:
               return "jms-session-pool";
            case 88:
               return "jms-bridge-destination";
            case 89:
               return "bridge-destination";
            case 90:
               return "foreign-jms-server";
            case 91:
               return "shutdown-class";
            case 92:
               return "startup-class";
            case 93:
               return "singleton-service";
            case 94:
               return "mail-session";
            case 95:
               return "jolt-connection-pool";
            case 96:
               return "log-filter";
            case 97:
               return "domain-log-filter";
            case 98:
               return "file-store";
            case 99:
               return "jdbc-store";
            case 100:
               return "jms-interop-module";
            case 101:
               return "jms-system-resource";
            case 102:
               return "custom-resource";
            case 103:
               return "foreign-jndi-provider";
            case 104:
               return "admin-server-name";
            case 105:
               return "administration-protocol";
            case 106:
               return "wldf-system-resource";
            case 107:
               return "jdbc-system-resource";
            case 108:
               return "system-resource";
            case 109:
               return "saf-agent";
            case 110:
               return "wlec-connection-pool";
            case 111:
               return "error-handling";
            case 112:
               return "remote-saf-context";
            case 113:
               return "migratablermi-service";
            case 114:
               return "admin-serverm-bean";
            case 115:
               return "jms-distributed-queue-member";
            case 116:
               return "jms-distributed-topic-member";
            case 117:
               return "snmp-trap-destination";
            case 118:
               return "snmp-proxy";
            case 119:
               return "snmp-gauge-monitor";
            case 120:
               return "snmp-string-monitor";
            case 121:
               return "snmp-counter-monitor";
            case 122:
               return "snmp-log-filter";
            case 123:
               return "snmp-attribute-change";
            case 124:
               return "webservice-security";
            case 125:
               return "foreign-jms-connection-factory";
            case 126:
               return "foreign-jms-destination";
            case 127:
               return "jms-connection-consumer";
            case 128:
               return "auto-deploy-for-submodules-enabled";
            case 129:
               return "admin-console";
            case 130:
               return "internal-apps-deploy-on-demand-enabled";
            case 131:
               return "guardian-enabled";
            case 132:
               return "ocm-enabled";
            case 133:
               return "msg-id-prefix-compatibility-enabled";
            case 134:
               return "coherence-cluster-system-resource";
            case 135:
               return "restful-management-services";
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 15:
               return true;
            case 16:
            case 17:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
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
            case 80:
            case 81:
            case 82:
            case 83:
            case 104:
            case 105:
            case 114:
            case 128:
            case 129:
            case 130:
            case 131:
            case 132:
            case 133:
            default:
               return super.isArray(var1);
            case 18:
               return true;
            case 24:
               return true;
            case 25:
               return true;
            case 26:
               return true;
            case 27:
               return true;
            case 28:
               return true;
            case 29:
               return true;
            case 30:
               return true;
            case 31:
               return true;
            case 32:
               return true;
            case 33:
               return true;
            case 45:
               return true;
            case 46:
               return true;
            case 47:
               return true;
            case 48:
               return true;
            case 49:
               return true;
            case 50:
               return true;
            case 51:
               return true;
            case 52:
               return true;
            case 53:
               return true;
            case 54:
               return true;
            case 55:
               return true;
            case 56:
               return true;
            case 57:
               return true;
            case 58:
               return true;
            case 59:
               return true;
            case 60:
               return true;
            case 61:
               return true;
            case 62:
               return true;
            case 63:
               return true;
            case 64:
               return true;
            case 65:
               return true;
            case 66:
               return true;
            case 67:
               return true;
            case 68:
               return true;
            case 69:
               return true;
            case 70:
               return true;
            case 71:
               return true;
            case 72:
               return true;
            case 73:
               return true;
            case 74:
               return true;
            case 75:
               return true;
            case 76:
               return true;
            case 77:
               return true;
            case 78:
               return true;
            case 79:
               return true;
            case 84:
               return true;
            case 85:
               return true;
            case 86:
               return true;
            case 87:
               return true;
            case 88:
               return true;
            case 89:
               return true;
            case 90:
               return true;
            case 91:
               return true;
            case 92:
               return true;
            case 93:
               return true;
            case 94:
               return true;
            case 95:
               return true;
            case 96:
               return true;
            case 97:
               return true;
            case 98:
               return true;
            case 99:
               return true;
            case 100:
               return true;
            case 101:
               return true;
            case 102:
               return true;
            case 103:
               return true;
            case 106:
               return true;
            case 107:
               return true;
            case 108:
               return true;
            case 109:
               return true;
            case 110:
               return true;
            case 111:
               return true;
            case 112:
               return true;
            case 113:
               return true;
            case 115:
               return true;
            case 116:
               return true;
            case 117:
               return true;
            case 118:
               return true;
            case 119:
               return true;
            case 120:
               return true;
            case 121:
               return true;
            case 122:
               return true;
            case 123:
               return true;
            case 124:
               return true;
            case 125:
               return true;
            case 126:
               return true;
            case 127:
               return true;
            case 134:
               return true;
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
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
            case 34:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 47:
            case 50:
            case 60:
            case 66:
            case 68:
            case 71:
            case 104:
            case 105:
            case 108:
            case 128:
            case 130:
            case 131:
            case 132:
            case 133:
            default:
               return super.isBean(var1);
            case 24:
               return true;
            case 25:
               return true;
            case 26:
               return true;
            case 28:
               return true;
            case 29:
               return true;
            case 30:
               return true;
            case 31:
               return true;
            case 32:
               return true;
            case 33:
               return true;
            case 35:
               return true;
            case 45:
               return true;
            case 46:
               return true;
            case 48:
               return true;
            case 49:
               return true;
            case 51:
               return true;
            case 52:
               return true;
            case 53:
               return true;
            case 54:
               return true;
            case 55:
               return true;
            case 56:
               return true;
            case 57:
               return true;
            case 58:
               return true;
            case 59:
               return true;
            case 61:
               return true;
            case 62:
               return true;
            case 63:
               return true;
            case 64:
               return true;
            case 65:
               return true;
            case 67:
               return true;
            case 69:
               return true;
            case 70:
               return true;
            case 72:
               return true;
            case 73:
               return true;
            case 74:
               return true;
            case 75:
               return true;
            case 76:
               return true;
            case 77:
               return true;
            case 78:
               return true;
            case 79:
               return true;
            case 80:
               return true;
            case 81:
               return true;
            case 82:
               return true;
            case 83:
               return true;
            case 84:
               return true;
            case 85:
               return true;
            case 86:
               return true;
            case 87:
               return true;
            case 88:
               return true;
            case 89:
               return true;
            case 90:
               return true;
            case 91:
               return true;
            case 92:
               return true;
            case 93:
               return true;
            case 94:
               return true;
            case 95:
               return true;
            case 96:
               return true;
            case 97:
               return true;
            case 98:
               return true;
            case 99:
               return true;
            case 100:
               return true;
            case 101:
               return true;
            case 102:
               return true;
            case 103:
               return true;
            case 106:
               return true;
            case 107:
               return true;
            case 109:
               return true;
            case 110:
               return true;
            case 111:
               return true;
            case 112:
               return true;
            case 113:
               return true;
            case 114:
               return true;
            case 115:
               return true;
            case 116:
               return true;
            case 117:
               return true;
            case 118:
               return true;
            case 119:
               return true;
            case 120:
               return true;
            case 121:
               return true;
            case 122:
               return true;
            case 123:
               return true;
            case 124:
               return true;
            case 125:
               return true;
            case 126:
               return true;
            case 127:
               return true;
            case 129:
               return true;
            case 134:
               return true;
            case 135:
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

   protected static class Helper extends ConfigurationMBeanImpl.Helper {
      private DomainMBeanImpl bean;

      protected Helper(DomainMBeanImpl var1) {
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
               return "DomainVersion";
            case 8:
               return "LastModificationTime";
            case 9:
               return "Active";
            case 10:
               return "SecurityConfiguration";
            case 11:
               return "Security";
            case 12:
               return "JTA";
            case 13:
               return "JPA";
            case 14:
               return "DeploymentConfiguration";
            case 15:
               return "WTCServers";
            case 16:
               return "Log";
            case 17:
               return "SNMPAgent";
            case 18:
               return "SNMPAgentDeployments";
            case 19:
               return "RootDirectory";
            case 20:
               return "ConsoleEnabled";
            case 21:
               return "ConsoleContextPath";
            case 22:
               return "ConsoleExtensionDirectory";
            case 23:
               return "AutoConfigurationSaveEnabled";
            case 24:
               return "Servers";
            case 25:
               return "CoherenceServers";
            case 26:
               return "Clusters";
            case 27:
               return "Deployments";
            case 28:
               return "FileT3s";
            case 29:
               return "JDBCConnectionPools";
            case 30:
               return "JDBCDataSources";
            case 31:
               return "JDBCTxDataSources";
            case 32:
               return "JDBCMultiPools";
            case 33:
               return "MessagingBridges";
            case 34:
               return "ProductionModeEnabled";
            case 35:
               return "EmbeddedLDAP";
            case 36:
               return "AdministrationPortEnabled";
            case 37:
               return "AdministrationPort";
            case 38:
               return "ExalogicOptimizationsEnabled";
            case 39:
               return "ArchiveConfigurationCount";
            case 40:
               return "ConfigBackupEnabled";
            case 41:
               return "ConfigurationVersion";
            case 42:
               return "AdministrationMBeanAuditingEnabled";
            case 43:
               return "ConfigurationAuditType";
            case 44:
               return "ClusterConstraintsEnabled";
            case 45:
               return "Applications";
            case 46:
               return "AppDeployments";
            case 47:
               return "InternalAppDeployments";
            case 48:
               return "Libraries";
            case 49:
               return "DomainLibraries";
            case 50:
               return "InternalLibraries";
            case 51:
               return "WSReliableDeliveryPolicies";
            case 52:
               return "JDBCDataSourceFactories";
            case 53:
               return "Machines";
            case 54:
               return "XMLEntityCaches";
            case 55:
               return "XMLRegistries";
            case 56:
               return "FileRealms";
            case 57:
               return "CachingRealms";
            case 58:
               return "Realms";
            case 59:
               return "PasswordPolicies";
            case 60:
               return "BasicRealms";
            case 61:
               return "CustomRealms";
            case 62:
               return "LDAPRealms";
            case 63:
               return "NTRealms";
            case 64:
               return "RDBMSRealms";
            case 65:
               return "UnixRealms";
            case 66:
               return "Targets";
            case 67:
               return "JMSServers";
            case 68:
               return "JMSStores";
            case 69:
               return "JMSJDBCStores";
            case 70:
               return "JMSFileStores";
            case 71:
               return "JMSDestinations";
            case 72:
               return "JMSQueues";
            case 73:
               return "JMSTopics";
            case 74:
               return "JMSDistributedQueues";
            case 75:
               return "JMSDistributedTopics";
            case 76:
               return "JMSTemplates";
            case 77:
               return "NetworkChannels";
            case 78:
               return "VirtualHosts";
            case 79:
               return "MigratableTargets";
            case 80:
               return "EJBContainer";
            case 81:
               return "WebAppContainer";
            case 82:
               return "JMX";
            case 83:
               return "SelfTuning";
            case 84:
               return "PathServices";
            case 85:
               return "JMSDestinationKeys";
            case 86:
               return "JMSConnectionFactories";
            case 87:
               return "JMSSessionPools";
            case 88:
               return "JMSBridgeDestinations";
            case 89:
               return "BridgeDestinations";
            case 90:
               return "ForeignJMSServers";
            case 91:
               return "ShutdownClasses";
            case 92:
               return "StartupClasses";
            case 93:
               return "SingletonServices";
            case 94:
               return "MailSessions";
            case 95:
               return "JoltConnectionPools";
            case 96:
               return "LogFilters";
            case 97:
               return "DomainLogFilters";
            case 98:
               return "FileStores";
            case 99:
               return "JDBCStores";
            case 100:
               return "JMSInteropModules";
            case 101:
               return "JMSSystemResources";
            case 102:
               return "CustomResources";
            case 103:
               return "ForeignJNDIProviders";
            case 104:
               return "AdminServerName";
            case 105:
               return "AdministrationProtocol";
            case 106:
               return "WLDFSystemResources";
            case 107:
               return "JDBCSystemResources";
            case 108:
               return "SystemResources";
            case 109:
               return "SAFAgents";
            case 110:
               return "WLECConnectionPools";
            case 111:
               return "ErrorHandlings";
            case 112:
               return "RemoteSAFContexts";
            case 113:
               return "MigratableRMIServices";
            case 114:
               return "AdminServerMBean";
            case 115:
               return "JMSDistributedQueueMembers";
            case 116:
               return "JMSDistributedTopicMembers";
            case 117:
               return "SNMPTrapDestinations";
            case 118:
               return "SNMPProxies";
            case 119:
               return "SNMPGaugeMonitors";
            case 120:
               return "SNMPStringMonitors";
            case 121:
               return "SNMPCounterMonitors";
            case 122:
               return "SNMPLogFilters";
            case 123:
               return "SNMPAttributeChanges";
            case 124:
               return "WebserviceSecurities";
            case 125:
               return "ForeignJMSConnectionFactories";
            case 126:
               return "ForeignJMSDestinations";
            case 127:
               return "JMSConnectionConsumers";
            case 128:
               return "AutoDeployForSubmodulesEnabled";
            case 129:
               return "AdminConsole";
            case 130:
               return "InternalAppsDeployOnDemandEnabled";
            case 131:
               return "GuardianEnabled";
            case 132:
               return "OCMEnabled";
            case 133:
               return "MsgIdPrefixCompatibilityEnabled";
            case 134:
               return "CoherenceClusterSystemResources";
            case 135:
               return "RestfulManagementServices";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("AdminConsole")) {
            return 129;
         } else if (var1.equals("AdminServerMBean")) {
            return 114;
         } else if (var1.equals("AdminServerName")) {
            return 104;
         } else if (var1.equals("AdministrationPort")) {
            return 37;
         } else if (var1.equals("AdministrationProtocol")) {
            return 105;
         } else if (var1.equals("AppDeployments")) {
            return 46;
         } else if (var1.equals("Applications")) {
            return 45;
         } else if (var1.equals("ArchiveConfigurationCount")) {
            return 39;
         } else if (var1.equals("BasicRealms")) {
            return 60;
         } else if (var1.equals("BridgeDestinations")) {
            return 89;
         } else if (var1.equals("CachingRealms")) {
            return 57;
         } else if (var1.equals("Clusters")) {
            return 26;
         } else if (var1.equals("CoherenceClusterSystemResources")) {
            return 134;
         } else if (var1.equals("CoherenceServers")) {
            return 25;
         } else if (var1.equals("ConfigurationAuditType")) {
            return 43;
         } else if (var1.equals("ConfigurationVersion")) {
            return 41;
         } else if (var1.equals("ConsoleContextPath")) {
            return 21;
         } else if (var1.equals("ConsoleExtensionDirectory")) {
            return 22;
         } else if (var1.equals("CustomRealms")) {
            return 61;
         } else if (var1.equals("CustomResources")) {
            return 102;
         } else if (var1.equals("DeploymentConfiguration")) {
            return 14;
         } else if (var1.equals("Deployments")) {
            return 27;
         } else if (var1.equals("DomainLibraries")) {
            return 49;
         } else if (var1.equals("DomainLogFilters")) {
            return 97;
         } else if (var1.equals("DomainVersion")) {
            return 7;
         } else if (var1.equals("EJBContainer")) {
            return 80;
         } else if (var1.equals("EmbeddedLDAP")) {
            return 35;
         } else if (var1.equals("ErrorHandlings")) {
            return 111;
         } else if (var1.equals("FileRealms")) {
            return 56;
         } else if (var1.equals("FileStores")) {
            return 98;
         } else if (var1.equals("FileT3s")) {
            return 28;
         } else if (var1.equals("ForeignJMSConnectionFactories")) {
            return 125;
         } else if (var1.equals("ForeignJMSDestinations")) {
            return 126;
         } else if (var1.equals("ForeignJMSServers")) {
            return 90;
         } else if (var1.equals("ForeignJNDIProviders")) {
            return 103;
         } else if (var1.equals("InternalAppDeployments")) {
            return 47;
         } else if (var1.equals("InternalLibraries")) {
            return 50;
         } else if (var1.equals("JDBCConnectionPools")) {
            return 29;
         } else if (var1.equals("JDBCDataSourceFactories")) {
            return 52;
         } else if (var1.equals("JDBCDataSources")) {
            return 30;
         } else if (var1.equals("JDBCMultiPools")) {
            return 32;
         } else if (var1.equals("JDBCStores")) {
            return 99;
         } else if (var1.equals("JDBCSystemResources")) {
            return 107;
         } else if (var1.equals("JDBCTxDataSources")) {
            return 31;
         } else if (var1.equals("JMSBridgeDestinations")) {
            return 88;
         } else if (var1.equals("JMSConnectionConsumers")) {
            return 127;
         } else if (var1.equals("JMSConnectionFactories")) {
            return 86;
         } else if (var1.equals("JMSDestinationKeys")) {
            return 85;
         } else if (var1.equals("JMSDestinations")) {
            return 71;
         } else if (var1.equals("JMSDistributedQueueMembers")) {
            return 115;
         } else if (var1.equals("JMSDistributedQueues")) {
            return 74;
         } else if (var1.equals("JMSDistributedTopicMembers")) {
            return 116;
         } else if (var1.equals("JMSDistributedTopics")) {
            return 75;
         } else if (var1.equals("JMSFileStores")) {
            return 70;
         } else if (var1.equals("JMSInteropModules")) {
            return 100;
         } else if (var1.equals("JMSJDBCStores")) {
            return 69;
         } else if (var1.equals("JMSQueues")) {
            return 72;
         } else if (var1.equals("JMSServers")) {
            return 67;
         } else if (var1.equals("JMSSessionPools")) {
            return 87;
         } else if (var1.equals("JMSStores")) {
            return 68;
         } else if (var1.equals("JMSSystemResources")) {
            return 101;
         } else if (var1.equals("JMSTemplates")) {
            return 76;
         } else if (var1.equals("JMSTopics")) {
            return 73;
         } else if (var1.equals("JMX")) {
            return 82;
         } else if (var1.equals("JPA")) {
            return 13;
         } else if (var1.equals("JTA")) {
            return 12;
         } else if (var1.equals("JoltConnectionPools")) {
            return 95;
         } else if (var1.equals("LDAPRealms")) {
            return 62;
         } else if (var1.equals("LastModificationTime")) {
            return 8;
         } else if (var1.equals("Libraries")) {
            return 48;
         } else if (var1.equals("Log")) {
            return 16;
         } else if (var1.equals("LogFilters")) {
            return 96;
         } else if (var1.equals("Machines")) {
            return 53;
         } else if (var1.equals("MailSessions")) {
            return 94;
         } else if (var1.equals("MessagingBridges")) {
            return 33;
         } else if (var1.equals("MigratableRMIServices")) {
            return 113;
         } else if (var1.equals("MigratableTargets")) {
            return 79;
         } else if (var1.equals("NTRealms")) {
            return 63;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("NetworkChannels")) {
            return 77;
         } else if (var1.equals("PasswordPolicies")) {
            return 59;
         } else if (var1.equals("PathServices")) {
            return 84;
         } else if (var1.equals("RDBMSRealms")) {
            return 64;
         } else if (var1.equals("Realms")) {
            return 58;
         } else if (var1.equals("RemoteSAFContexts")) {
            return 112;
         } else if (var1.equals("RestfulManagementServices")) {
            return 135;
         } else if (var1.equals("RootDirectory")) {
            return 19;
         } else if (var1.equals("SAFAgents")) {
            return 109;
         } else if (var1.equals("SNMPAgent")) {
            return 17;
         } else if (var1.equals("SNMPAgentDeployments")) {
            return 18;
         } else if (var1.equals("SNMPAttributeChanges")) {
            return 123;
         } else if (var1.equals("SNMPCounterMonitors")) {
            return 121;
         } else if (var1.equals("SNMPGaugeMonitors")) {
            return 119;
         } else if (var1.equals("SNMPLogFilters")) {
            return 122;
         } else if (var1.equals("SNMPProxies")) {
            return 118;
         } else if (var1.equals("SNMPStringMonitors")) {
            return 120;
         } else if (var1.equals("SNMPTrapDestinations")) {
            return 117;
         } else if (var1.equals("Security")) {
            return 11;
         } else if (var1.equals("SecurityConfiguration")) {
            return 10;
         } else if (var1.equals("SelfTuning")) {
            return 83;
         } else if (var1.equals("Servers")) {
            return 24;
         } else if (var1.equals("ShutdownClasses")) {
            return 91;
         } else if (var1.equals("SingletonServices")) {
            return 93;
         } else if (var1.equals("StartupClasses")) {
            return 92;
         } else if (var1.equals("SystemResources")) {
            return 108;
         } else if (var1.equals("Targets")) {
            return 66;
         } else if (var1.equals("UnixRealms")) {
            return 65;
         } else if (var1.equals("VirtualHosts")) {
            return 78;
         } else if (var1.equals("WLDFSystemResources")) {
            return 106;
         } else if (var1.equals("WLECConnectionPools")) {
            return 110;
         } else if (var1.equals("WSReliableDeliveryPolicies")) {
            return 51;
         } else if (var1.equals("WTCServers")) {
            return 15;
         } else if (var1.equals("WebAppContainer")) {
            return 81;
         } else if (var1.equals("WebserviceSecurities")) {
            return 124;
         } else if (var1.equals("XMLEntityCaches")) {
            return 54;
         } else if (var1.equals("XMLRegistries")) {
            return 55;
         } else if (var1.equals("Active")) {
            return 9;
         } else if (var1.equals("AdministrationMBeanAuditingEnabled")) {
            return 42;
         } else if (var1.equals("AdministrationPortEnabled")) {
            return 36;
         } else if (var1.equals("AutoConfigurationSaveEnabled")) {
            return 23;
         } else if (var1.equals("AutoDeployForSubmodulesEnabled")) {
            return 128;
         } else if (var1.equals("ClusterConstraintsEnabled")) {
            return 44;
         } else if (var1.equals("ConfigBackupEnabled")) {
            return 40;
         } else if (var1.equals("ConsoleEnabled")) {
            return 20;
         } else if (var1.equals("ExalogicOptimizationsEnabled")) {
            return 38;
         } else if (var1.equals("GuardianEnabled")) {
            return 131;
         } else if (var1.equals("InternalAppsDeployOnDemandEnabled")) {
            return 130;
         } else if (var1.equals("MsgIdPrefixCompatibilityEnabled")) {
            return 133;
         } else if (var1.equals("OCMEnabled")) {
            return 132;
         } else {
            return var1.equals("ProductionModeEnabled") ? 34 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         if (this.bean.getAdminConsole() != null) {
            var1.add(new ArrayIterator(new AdminConsoleMBean[]{this.bean.getAdminConsole()}));
         }

         if (this.bean.getAdminServerMBean() != null) {
            var1.add(new ArrayIterator(new AdminServerMBean[]{this.bean.getAdminServerMBean()}));
         }

         var1.add(new ArrayIterator(this.bean.getAppDeployments()));
         var1.add(new ArrayIterator(this.bean.getApplications()));
         var1.add(new ArrayIterator(this.bean.getBridgeDestinations()));
         var1.add(new ArrayIterator(this.bean.getCachingRealms()));
         var1.add(new ArrayIterator(this.bean.getClusters()));
         var1.add(new ArrayIterator(this.bean.getCoherenceClusterSystemResources()));
         var1.add(new ArrayIterator(this.bean.getCoherenceServers()));
         var1.add(new ArrayIterator(this.bean.getCustomRealms()));
         var1.add(new ArrayIterator(this.bean.getCustomResources()));
         if (this.bean.getDeploymentConfiguration() != null) {
            var1.add(new ArrayIterator(new DeploymentConfigurationMBean[]{this.bean.getDeploymentConfiguration()}));
         }

         var1.add(new ArrayIterator(this.bean.getDomainLibraries()));
         var1.add(new ArrayIterator(this.bean.getDomainLogFilters()));
         if (this.bean.getEJBContainer() != null) {
            var1.add(new ArrayIterator(new EJBContainerMBean[]{this.bean.getEJBContainer()}));
         }

         if (this.bean.getEmbeddedLDAP() != null) {
            var1.add(new ArrayIterator(new EmbeddedLDAPMBean[]{this.bean.getEmbeddedLDAP()}));
         }

         var1.add(new ArrayIterator(this.bean.getErrorHandlings()));
         var1.add(new ArrayIterator(this.bean.getFileRealms()));
         var1.add(new ArrayIterator(this.bean.getFileStores()));
         var1.add(new ArrayIterator(this.bean.getFileT3s()));
         var1.add(new ArrayIterator(this.bean.getForeignJMSConnectionFactories()));
         var1.add(new ArrayIterator(this.bean.getForeignJMSDestinations()));
         var1.add(new ArrayIterator(this.bean.getForeignJMSServers()));
         var1.add(new ArrayIterator(this.bean.getForeignJNDIProviders()));
         var1.add(new ArrayIterator(this.bean.getJDBCConnectionPools()));
         var1.add(new ArrayIterator(this.bean.getJDBCDataSourceFactories()));
         var1.add(new ArrayIterator(this.bean.getJDBCDataSources()));
         var1.add(new ArrayIterator(this.bean.getJDBCMultiPools()));
         var1.add(new ArrayIterator(this.bean.getJDBCStores()));
         var1.add(new ArrayIterator(this.bean.getJDBCSystemResources()));
         var1.add(new ArrayIterator(this.bean.getJDBCTxDataSources()));
         var1.add(new ArrayIterator(this.bean.getJMSBridgeDestinations()));
         var1.add(new ArrayIterator(this.bean.getJMSConnectionConsumers()));
         var1.add(new ArrayIterator(this.bean.getJMSConnectionFactories()));
         var1.add(new ArrayIterator(this.bean.getJMSDestinationKeys()));
         var1.add(new ArrayIterator(this.bean.getJMSDistributedQueueMembers()));
         var1.add(new ArrayIterator(this.bean.getJMSDistributedQueues()));
         var1.add(new ArrayIterator(this.bean.getJMSDistributedTopicMembers()));
         var1.add(new ArrayIterator(this.bean.getJMSDistributedTopics()));
         var1.add(new ArrayIterator(this.bean.getJMSFileStores()));
         var1.add(new ArrayIterator(this.bean.getJMSInteropModules()));
         var1.add(new ArrayIterator(this.bean.getJMSJDBCStores()));
         var1.add(new ArrayIterator(this.bean.getJMSQueues()));
         var1.add(new ArrayIterator(this.bean.getJMSServers()));
         var1.add(new ArrayIterator(this.bean.getJMSSessionPools()));
         var1.add(new ArrayIterator(this.bean.getJMSSystemResources()));
         var1.add(new ArrayIterator(this.bean.getJMSTemplates()));
         var1.add(new ArrayIterator(this.bean.getJMSTopics()));
         if (this.bean.getJMX() != null) {
            var1.add(new ArrayIterator(new JMXMBean[]{this.bean.getJMX()}));
         }

         if (this.bean.getJPA() != null) {
            var1.add(new ArrayIterator(new JPAMBean[]{this.bean.getJPA()}));
         }

         if (this.bean.getJTA() != null) {
            var1.add(new ArrayIterator(new JTAMBean[]{this.bean.getJTA()}));
         }

         var1.add(new ArrayIterator(this.bean.getJoltConnectionPools()));
         var1.add(new ArrayIterator(this.bean.getLDAPRealms()));
         var1.add(new ArrayIterator(this.bean.getLibraries()));
         if (this.bean.getLog() != null) {
            var1.add(new ArrayIterator(new LogMBean[]{this.bean.getLog()}));
         }

         var1.add(new ArrayIterator(this.bean.getLogFilters()));
         var1.add(new ArrayIterator(this.bean.getMachines()));
         var1.add(new ArrayIterator(this.bean.getMailSessions()));
         var1.add(new ArrayIterator(this.bean.getMessagingBridges()));
         var1.add(new ArrayIterator(this.bean.getMigratableRMIServices()));
         var1.add(new ArrayIterator(this.bean.getMigratableTargets()));
         var1.add(new ArrayIterator(this.bean.getNTRealms()));
         var1.add(new ArrayIterator(this.bean.getNetworkChannels()));
         var1.add(new ArrayIterator(this.bean.getPasswordPolicies()));
         var1.add(new ArrayIterator(this.bean.getPathServices()));
         var1.add(new ArrayIterator(this.bean.getRDBMSRealms()));
         var1.add(new ArrayIterator(this.bean.getRealms()));
         var1.add(new ArrayIterator(this.bean.getRemoteSAFContexts()));
         if (this.bean.getRestfulManagementServices() != null) {
            var1.add(new ArrayIterator(new RestfulManagementServicesMBean[]{this.bean.getRestfulManagementServices()}));
         }

         var1.add(new ArrayIterator(this.bean.getSAFAgents()));
         if (this.bean.getSNMPAgent() != null) {
            var1.add(new ArrayIterator(new SNMPAgentMBean[]{this.bean.getSNMPAgent()}));
         }

         var1.add(new ArrayIterator(this.bean.getSNMPAgentDeployments()));
         var1.add(new ArrayIterator(this.bean.getSNMPAttributeChanges()));
         var1.add(new ArrayIterator(this.bean.getSNMPCounterMonitors()));
         var1.add(new ArrayIterator(this.bean.getSNMPGaugeMonitors()));
         var1.add(new ArrayIterator(this.bean.getSNMPLogFilters()));
         var1.add(new ArrayIterator(this.bean.getSNMPProxies()));
         var1.add(new ArrayIterator(this.bean.getSNMPStringMonitors()));
         var1.add(new ArrayIterator(this.bean.getSNMPTrapDestinations()));
         if (this.bean.getSecurity() != null) {
            var1.add(new ArrayIterator(new SecurityMBean[]{this.bean.getSecurity()}));
         }

         if (this.bean.getSecurityConfiguration() != null) {
            var1.add(new ArrayIterator(new SecurityConfigurationMBean[]{this.bean.getSecurityConfiguration()}));
         }

         if (this.bean.getSelfTuning() != null) {
            var1.add(new ArrayIterator(new SelfTuningMBean[]{this.bean.getSelfTuning()}));
         }

         var1.add(new ArrayIterator(this.bean.getServers()));
         var1.add(new ArrayIterator(this.bean.getShutdownClasses()));
         var1.add(new ArrayIterator(this.bean.getSingletonServices()));
         var1.add(new ArrayIterator(this.bean.getStartupClasses()));
         var1.add(new ArrayIterator(this.bean.getUnixRealms()));
         var1.add(new ArrayIterator(this.bean.getVirtualHosts()));
         var1.add(new ArrayIterator(this.bean.getWLDFSystemResources()));
         var1.add(new ArrayIterator(this.bean.getWLECConnectionPools()));
         var1.add(new ArrayIterator(this.bean.getWSReliableDeliveryPolicies()));
         var1.add(new ArrayIterator(this.bean.getWTCServers()));
         if (this.bean.getWebAppContainer() != null) {
            var1.add(new ArrayIterator(new WebAppContainerMBean[]{this.bean.getWebAppContainer()}));
         }

         var1.add(new ArrayIterator(this.bean.getWebserviceSecurities()));
         var1.add(new ArrayIterator(this.bean.getXMLEntityCaches()));
         var1.add(new ArrayIterator(this.bean.getXMLRegistries()));
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
            var5 = this.computeChildHashValue(this.bean.getAdminConsole());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = this.computeChildHashValue(this.bean.getAdminServerMBean());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isAdminServerNameSet()) {
               var2.append("AdminServerName");
               var2.append(String.valueOf(this.bean.getAdminServerName()));
            }

            if (this.bean.isAdministrationPortSet()) {
               var2.append("AdministrationPort");
               var2.append(String.valueOf(this.bean.getAdministrationPort()));
            }

            if (this.bean.isAdministrationProtocolSet()) {
               var2.append("AdministrationProtocol");
               var2.append(String.valueOf(this.bean.getAdministrationProtocol()));
            }

            var5 = 0L;

            int var7;
            for(var7 = 0; var7 < this.bean.getAppDeployments().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getAppDeployments()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getApplications().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getApplications()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isArchiveConfigurationCountSet()) {
               var2.append("ArchiveConfigurationCount");
               var2.append(String.valueOf(this.bean.getArchiveConfigurationCount()));
            }

            if (this.bean.isBasicRealmsSet()) {
               var2.append("BasicRealms");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getBasicRealms())));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getBridgeDestinations().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getBridgeDestinations()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getCachingRealms().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getCachingRealms()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getClusters().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getClusters()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getCoherenceClusterSystemResources().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getCoherenceClusterSystemResources()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getCoherenceServers().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getCoherenceServers()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isConfigurationAuditTypeSet()) {
               var2.append("ConfigurationAuditType");
               var2.append(String.valueOf(this.bean.getConfigurationAuditType()));
            }

            if (this.bean.isConfigurationVersionSet()) {
               var2.append("ConfigurationVersion");
               var2.append(String.valueOf(this.bean.getConfigurationVersion()));
            }

            if (this.bean.isConsoleContextPathSet()) {
               var2.append("ConsoleContextPath");
               var2.append(String.valueOf(this.bean.getConsoleContextPath()));
            }

            if (this.bean.isConsoleExtensionDirectorySet()) {
               var2.append("ConsoleExtensionDirectory");
               var2.append(String.valueOf(this.bean.getConsoleExtensionDirectory()));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getCustomRealms().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getCustomRealms()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getCustomResources().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getCustomResources()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = this.computeChildHashValue(this.bean.getDeploymentConfiguration());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isDeploymentsSet()) {
               var2.append("Deployments");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getDeployments())));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getDomainLibraries().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getDomainLibraries()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getDomainLogFilters().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getDomainLogFilters()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isDomainVersionSet()) {
               var2.append("DomainVersion");
               var2.append(String.valueOf(this.bean.getDomainVersion()));
            }

            var5 = this.computeChildHashValue(this.bean.getEJBContainer());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = this.computeChildHashValue(this.bean.getEmbeddedLDAP());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getErrorHandlings().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getErrorHandlings()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getFileRealms().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getFileRealms()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getFileStores().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getFileStores()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getFileT3s().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getFileT3s()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getForeignJMSConnectionFactories().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getForeignJMSConnectionFactories()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getForeignJMSDestinations().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getForeignJMSDestinations()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getForeignJMSServers().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getForeignJMSServers()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getForeignJNDIProviders().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getForeignJNDIProviders()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isInternalAppDeploymentsSet()) {
               var2.append("InternalAppDeployments");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getInternalAppDeployments())));
            }

            if (this.bean.isInternalLibrariesSet()) {
               var2.append("InternalLibraries");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getInternalLibraries())));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getJDBCConnectionPools().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getJDBCConnectionPools()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getJDBCDataSourceFactories().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getJDBCDataSourceFactories()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getJDBCDataSources().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getJDBCDataSources()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getJDBCMultiPools().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getJDBCMultiPools()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getJDBCStores().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getJDBCStores()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getJDBCSystemResources().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getJDBCSystemResources()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getJDBCTxDataSources().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getJDBCTxDataSources()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getJMSBridgeDestinations().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getJMSBridgeDestinations()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getJMSConnectionConsumers().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getJMSConnectionConsumers()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getJMSConnectionFactories().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getJMSConnectionFactories()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getJMSDestinationKeys().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getJMSDestinationKeys()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isJMSDestinationsSet()) {
               var2.append("JMSDestinations");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getJMSDestinations())));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getJMSDistributedQueueMembers().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getJMSDistributedQueueMembers()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getJMSDistributedQueues().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getJMSDistributedQueues()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getJMSDistributedTopicMembers().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getJMSDistributedTopicMembers()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getJMSDistributedTopics().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getJMSDistributedTopics()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getJMSFileStores().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getJMSFileStores()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getJMSInteropModules().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getJMSInteropModules()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getJMSJDBCStores().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getJMSJDBCStores()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getJMSQueues().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getJMSQueues()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getJMSServers().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getJMSServers()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getJMSSessionPools().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getJMSSessionPools()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isJMSStoresSet()) {
               var2.append("JMSStores");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getJMSStores())));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getJMSSystemResources().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getJMSSystemResources()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getJMSTemplates().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getJMSTemplates()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getJMSTopics().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getJMSTopics()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = this.computeChildHashValue(this.bean.getJMX());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = this.computeChildHashValue(this.bean.getJPA());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = this.computeChildHashValue(this.bean.getJTA());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getJoltConnectionPools().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getJoltConnectionPools()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getLDAPRealms().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getLDAPRealms()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isLastModificationTimeSet()) {
               var2.append("LastModificationTime");
               var2.append(String.valueOf(this.bean.getLastModificationTime()));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getLibraries().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getLibraries()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = this.computeChildHashValue(this.bean.getLog());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getLogFilters().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getLogFilters()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getMachines().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getMachines()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getMailSessions().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getMailSessions()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getMessagingBridges().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getMessagingBridges()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getMigratableRMIServices().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getMigratableRMIServices()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getMigratableTargets().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getMigratableTargets()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getNTRealms().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getNTRealms()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getNetworkChannels().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getNetworkChannels()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getPasswordPolicies().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getPasswordPolicies()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getPathServices().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getPathServices()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getRDBMSRealms().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getRDBMSRealms()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getRealms().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getRealms()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getRemoteSAFContexts().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getRemoteSAFContexts()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = this.computeChildHashValue(this.bean.getRestfulManagementServices());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isRootDirectorySet()) {
               var2.append("RootDirectory");
               var2.append(String.valueOf(this.bean.getRootDirectory()));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getSAFAgents().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getSAFAgents()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = this.computeChildHashValue(this.bean.getSNMPAgent());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getSNMPAgentDeployments().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getSNMPAgentDeployments()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getSNMPAttributeChanges().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getSNMPAttributeChanges()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getSNMPCounterMonitors().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getSNMPCounterMonitors()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getSNMPGaugeMonitors().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getSNMPGaugeMonitors()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getSNMPLogFilters().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getSNMPLogFilters()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getSNMPProxies().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getSNMPProxies()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getSNMPStringMonitors().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getSNMPStringMonitors()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getSNMPTrapDestinations().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getSNMPTrapDestinations()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = this.computeChildHashValue(this.bean.getSecurity());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = this.computeChildHashValue(this.bean.getSecurityConfiguration());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = this.computeChildHashValue(this.bean.getSelfTuning());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getServers().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getServers()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getShutdownClasses().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getShutdownClasses()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getSingletonServices().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getSingletonServices()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getStartupClasses().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getStartupClasses()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isSystemResourcesSet()) {
               var2.append("SystemResources");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getSystemResources())));
            }

            if (this.bean.isTargetsSet()) {
               var2.append("Targets");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getTargets())));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getUnixRealms().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getUnixRealms()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getVirtualHosts().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getVirtualHosts()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getWLDFSystemResources().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getWLDFSystemResources()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getWLECConnectionPools().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getWLECConnectionPools()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getWSReliableDeliveryPolicies().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getWSReliableDeliveryPolicies()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getWTCServers().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getWTCServers()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = this.computeChildHashValue(this.bean.getWebAppContainer());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getWebserviceSecurities().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getWebserviceSecurities()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getXMLEntityCaches().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getXMLEntityCaches()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getXMLRegistries().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getXMLRegistries()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isActiveSet()) {
               var2.append("Active");
               var2.append(String.valueOf(this.bean.isActive()));
            }

            if (this.bean.isAdministrationMBeanAuditingEnabledSet()) {
               var2.append("AdministrationMBeanAuditingEnabled");
               var2.append(String.valueOf(this.bean.isAdministrationMBeanAuditingEnabled()));
            }

            if (this.bean.isAdministrationPortEnabledSet()) {
               var2.append("AdministrationPortEnabled");
               var2.append(String.valueOf(this.bean.isAdministrationPortEnabled()));
            }

            if (this.bean.isAutoConfigurationSaveEnabledSet()) {
               var2.append("AutoConfigurationSaveEnabled");
               var2.append(String.valueOf(this.bean.isAutoConfigurationSaveEnabled()));
            }

            if (this.bean.isAutoDeployForSubmodulesEnabledSet()) {
               var2.append("AutoDeployForSubmodulesEnabled");
               var2.append(String.valueOf(this.bean.isAutoDeployForSubmodulesEnabled()));
            }

            if (this.bean.isClusterConstraintsEnabledSet()) {
               var2.append("ClusterConstraintsEnabled");
               var2.append(String.valueOf(this.bean.isClusterConstraintsEnabled()));
            }

            if (this.bean.isConfigBackupEnabledSet()) {
               var2.append("ConfigBackupEnabled");
               var2.append(String.valueOf(this.bean.isConfigBackupEnabled()));
            }

            if (this.bean.isConsoleEnabledSet()) {
               var2.append("ConsoleEnabled");
               var2.append(String.valueOf(this.bean.isConsoleEnabled()));
            }

            if (this.bean.isExalogicOptimizationsEnabledSet()) {
               var2.append("ExalogicOptimizationsEnabled");
               var2.append(String.valueOf(this.bean.isExalogicOptimizationsEnabled()));
            }

            if (this.bean.isGuardianEnabledSet()) {
               var2.append("GuardianEnabled");
               var2.append(String.valueOf(this.bean.isGuardianEnabled()));
            }

            if (this.bean.isInternalAppsDeployOnDemandEnabledSet()) {
               var2.append("InternalAppsDeployOnDemandEnabled");
               var2.append(String.valueOf(this.bean.isInternalAppsDeployOnDemandEnabled()));
            }

            if (this.bean.isMsgIdPrefixCompatibilityEnabledSet()) {
               var2.append("MsgIdPrefixCompatibilityEnabled");
               var2.append(String.valueOf(this.bean.isMsgIdPrefixCompatibilityEnabled()));
            }

            if (this.bean.isOCMEnabledSet()) {
               var2.append("OCMEnabled");
               var2.append(String.valueOf(this.bean.isOCMEnabled()));
            }

            if (this.bean.isProductionModeEnabledSet()) {
               var2.append("ProductionModeEnabled");
               var2.append(String.valueOf(this.bean.isProductionModeEnabled()));
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
            DomainMBeanImpl var2 = (DomainMBeanImpl)var1;
            this.computeSubDiff("AdminConsole", this.bean.getAdminConsole(), var2.getAdminConsole());
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeChildDiff("AdminServerMBean", this.bean.getAdminServerMBean(), var2.getAdminServerMBean(), false);
            }

            this.computeDiff("AdminServerName", this.bean.getAdminServerName(), var2.getAdminServerName(), false);
            this.computeDiff("AdministrationPort", this.bean.getAdministrationPort(), var2.getAdministrationPort(), true);
            this.computeDiff("AdministrationProtocol", this.bean.getAdministrationProtocol(), var2.getAdministrationProtocol(), false);
            this.computeChildDiff("AppDeployments", this.bean.getAppDeployments(), var2.getAppDeployments(), true);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeChildDiff("Applications", this.bean.getApplications(), var2.getApplications(), true);
            }

            this.computeDiff("ArchiveConfigurationCount", this.bean.getArchiveConfigurationCount(), var2.getArchiveConfigurationCount(), false);
            this.computeChildDiff("BridgeDestinations", this.bean.getBridgeDestinations(), var2.getBridgeDestinations(), false);
            this.computeChildDiff("CachingRealms", this.bean.getCachingRealms(), var2.getCachingRealms(), false);
            this.computeChildDiff("Clusters", this.bean.getClusters(), var2.getClusters(), true);
            this.computeChildDiff("CoherenceClusterSystemResources", this.bean.getCoherenceClusterSystemResources(), var2.getCoherenceClusterSystemResources(), true);
            this.computeChildDiff("CoherenceServers", this.bean.getCoherenceServers(), var2.getCoherenceServers(), true);
            this.computeDiff("ConfigurationAuditType", this.bean.getConfigurationAuditType(), var2.getConfigurationAuditType(), true);
            this.computeDiff("ConfigurationVersion", this.bean.getConfigurationVersion(), var2.getConfigurationVersion(), true);
            this.computeDiff("ConsoleContextPath", this.bean.getConsoleContextPath(), var2.getConsoleContextPath(), false);
            this.computeDiff("ConsoleExtensionDirectory", this.bean.getConsoleExtensionDirectory(), var2.getConsoleExtensionDirectory(), false);
            this.computeChildDiff("CustomRealms", this.bean.getCustomRealms(), var2.getCustomRealms(), false);
            this.computeChildDiff("CustomResources", this.bean.getCustomResources(), var2.getCustomResources(), true);
            this.computeSubDiff("DeploymentConfiguration", this.bean.getDeploymentConfiguration(), var2.getDeploymentConfiguration());
            this.computeChildDiff("DomainLibraries", this.bean.getDomainLibraries(), var2.getDomainLibraries(), true);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeChildDiff("DomainLogFilters", this.bean.getDomainLogFilters(), var2.getDomainLogFilters(), true);
            }

            this.computeDiff("DomainVersion", this.bean.getDomainVersion(), var2.getDomainVersion(), true);
            this.computeChildDiff("EJBContainer", this.bean.getEJBContainer(), var2.getEJBContainer(), false);
            this.computeSubDiff("EmbeddedLDAP", this.bean.getEmbeddedLDAP(), var2.getEmbeddedLDAP());
            this.computeChildDiff("ErrorHandlings", this.bean.getErrorHandlings(), var2.getErrorHandlings(), false);
            this.computeChildDiff("FileRealms", this.bean.getFileRealms(), var2.getFileRealms(), false);
            this.computeChildDiff("FileStores", this.bean.getFileStores(), var2.getFileStores(), true);
            this.computeChildDiff("FileT3s", this.bean.getFileT3s(), var2.getFileT3s(), false);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeChildDiff("ForeignJMSConnectionFactories", this.bean.getForeignJMSConnectionFactories(), var2.getForeignJMSConnectionFactories(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeChildDiff("ForeignJMSDestinations", this.bean.getForeignJMSDestinations(), var2.getForeignJMSDestinations(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeChildDiff("ForeignJMSServers", this.bean.getForeignJMSServers(), var2.getForeignJMSServers(), false);
            }

            this.computeChildDiff("ForeignJNDIProviders", this.bean.getForeignJNDIProviders(), var2.getForeignJNDIProviders(), true);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeChildDiff("JDBCConnectionPools", this.bean.getJDBCConnectionPools(), var2.getJDBCConnectionPools(), true);
            }

            this.computeChildDiff("JDBCDataSourceFactories", this.bean.getJDBCDataSourceFactories(), var2.getJDBCDataSourceFactories(), true);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeChildDiff("JDBCDataSources", this.bean.getJDBCDataSources(), var2.getJDBCDataSources(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeChildDiff("JDBCMultiPools", this.bean.getJDBCMultiPools(), var2.getJDBCMultiPools(), true);
            }

            this.computeChildDiff("JDBCStores", this.bean.getJDBCStores(), var2.getJDBCStores(), true);
            this.computeChildDiff("JDBCSystemResources", this.bean.getJDBCSystemResources(), var2.getJDBCSystemResources(), true);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeChildDiff("JDBCTxDataSources", this.bean.getJDBCTxDataSources(), var2.getJDBCTxDataSources(), true);
            }

            this.computeChildDiff("JMSBridgeDestinations", this.bean.getJMSBridgeDestinations(), var2.getJMSBridgeDestinations(), true);
            this.computeChildDiff("JMSConnectionConsumers", this.bean.getJMSConnectionConsumers(), var2.getJMSConnectionConsumers(), true);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeChildDiff("JMSConnectionFactories", this.bean.getJMSConnectionFactories(), var2.getJMSConnectionFactories(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeChildDiff("JMSDestinationKeys", this.bean.getJMSDestinationKeys(), var2.getJMSDestinationKeys(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("JMSDestinations", this.bean.getJMSDestinations(), var2.getJMSDestinations(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeChildDiff("JMSDistributedQueueMembers", this.bean.getJMSDistributedQueueMembers(), var2.getJMSDistributedQueueMembers(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeChildDiff("JMSDistributedQueues", this.bean.getJMSDistributedQueues(), var2.getJMSDistributedQueues(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeChildDiff("JMSDistributedTopicMembers", this.bean.getJMSDistributedTopicMembers(), var2.getJMSDistributedTopicMembers(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeChildDiff("JMSDistributedTopics", this.bean.getJMSDistributedTopics(), var2.getJMSDistributedTopics(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeChildDiff("JMSFileStores", this.bean.getJMSFileStores(), var2.getJMSFileStores(), true);
            }

            this.computeChildDiff("JMSInteropModules", this.bean.getJMSInteropModules(), var2.getJMSInteropModules(), true);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeChildDiff("JMSJDBCStores", this.bean.getJMSJDBCStores(), var2.getJMSJDBCStores(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeChildDiff("JMSQueues", this.bean.getJMSQueues(), var2.getJMSQueues(), false);
            }

            this.computeChildDiff("JMSServers", this.bean.getJMSServers(), var2.getJMSServers(), true);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeChildDiff("JMSSessionPools", this.bean.getJMSSessionPools(), var2.getJMSSessionPools(), false);
            }

            this.computeChildDiff("JMSSystemResources", this.bean.getJMSSystemResources(), var2.getJMSSystemResources(), true);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeChildDiff("JMSTemplates", this.bean.getJMSTemplates(), var2.getJMSTemplates(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeChildDiff("JMSTopics", this.bean.getJMSTopics(), var2.getJMSTopics(), false);
            }

            this.computeSubDiff("JMX", this.bean.getJMX(), var2.getJMX());
            this.computeSubDiff("JPA", this.bean.getJPA(), var2.getJPA());
            this.computeSubDiff("JTA", this.bean.getJTA(), var2.getJTA());
            this.computeChildDiff("JoltConnectionPools", this.bean.getJoltConnectionPools(), var2.getJoltConnectionPools(), false);
            this.computeChildDiff("LDAPRealms", this.bean.getLDAPRealms(), var2.getLDAPRealms(), false);
            this.computeDiff("LastModificationTime", this.bean.getLastModificationTime(), var2.getLastModificationTime(), false);
            this.computeChildDiff("Libraries", this.bean.getLibraries(), var2.getLibraries(), true);
            this.computeSubDiff("Log", this.bean.getLog(), var2.getLog());
            this.computeChildDiff("LogFilters", this.bean.getLogFilters(), var2.getLogFilters(), true);
            this.computeChildDiff("Machines", this.bean.getMachines(), var2.getMachines(), true);
            this.computeChildDiff("MailSessions", this.bean.getMailSessions(), var2.getMailSessions(), true);
            this.computeChildDiff("MessagingBridges", this.bean.getMessagingBridges(), var2.getMessagingBridges(), true);
            this.computeChildDiff("MigratableRMIServices", this.bean.getMigratableRMIServices(), var2.getMigratableRMIServices(), true);
            this.computeChildDiff("MigratableTargets", this.bean.getMigratableTargets(), var2.getMigratableTargets(), true);
            this.computeChildDiff("NTRealms", this.bean.getNTRealms(), var2.getNTRealms(), false);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeChildDiff("NetworkChannels", this.bean.getNetworkChannels(), var2.getNetworkChannels(), false);
            }

            this.computeChildDiff("PasswordPolicies", this.bean.getPasswordPolicies(), var2.getPasswordPolicies(), false);
            this.computeChildDiff("PathServices", this.bean.getPathServices(), var2.getPathServices(), true);
            this.computeChildDiff("RDBMSRealms", this.bean.getRDBMSRealms(), var2.getRDBMSRealms(), false);
            this.computeChildDiff("Realms", this.bean.getRealms(), var2.getRealms(), false);
            this.computeChildDiff("RemoteSAFContexts", this.bean.getRemoteSAFContexts(), var2.getRemoteSAFContexts(), false);
            this.computeSubDiff("RestfulManagementServices", this.bean.getRestfulManagementServices(), var2.getRestfulManagementServices());
            this.computeDiff("RootDirectory", this.bean.getRootDirectory(), var2.getRootDirectory(), false);
            this.computeChildDiff("SAFAgents", this.bean.getSAFAgents(), var2.getSAFAgents(), true);
            this.computeSubDiff("SNMPAgent", this.bean.getSNMPAgent(), var2.getSNMPAgent());
            this.computeChildDiff("SNMPAgentDeployments", this.bean.getSNMPAgentDeployments(), var2.getSNMPAgentDeployments(), true);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeChildDiff("SNMPAttributeChanges", this.bean.getSNMPAttributeChanges(), var2.getSNMPAttributeChanges(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeChildDiff("SNMPCounterMonitors", this.bean.getSNMPCounterMonitors(), var2.getSNMPCounterMonitors(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeChildDiff("SNMPGaugeMonitors", this.bean.getSNMPGaugeMonitors(), var2.getSNMPGaugeMonitors(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeChildDiff("SNMPLogFilters", this.bean.getSNMPLogFilters(), var2.getSNMPLogFilters(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeChildDiff("SNMPProxies", this.bean.getSNMPProxies(), var2.getSNMPProxies(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeChildDiff("SNMPStringMonitors", this.bean.getSNMPStringMonitors(), var2.getSNMPStringMonitors(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeChildDiff("SNMPTrapDestinations", this.bean.getSNMPTrapDestinations(), var2.getSNMPTrapDestinations(), false);
            }

            this.computeSubDiff("Security", this.bean.getSecurity(), var2.getSecurity());
            this.computeSubDiff("SecurityConfiguration", this.bean.getSecurityConfiguration(), var2.getSecurityConfiguration());
            this.computeSubDiff("SelfTuning", this.bean.getSelfTuning(), var2.getSelfTuning());
            this.computeChildDiff("Servers", this.bean.getServers(), var2.getServers(), true);
            this.computeChildDiff("ShutdownClasses", this.bean.getShutdownClasses(), var2.getShutdownClasses(), true);
            this.computeChildDiff("SingletonServices", this.bean.getSingletonServices(), var2.getSingletonServices(), true);
            this.computeChildDiff("StartupClasses", this.bean.getStartupClasses(), var2.getStartupClasses(), true);
            this.computeChildDiff("UnixRealms", this.bean.getUnixRealms(), var2.getUnixRealms(), false);
            this.computeChildDiff("VirtualHosts", this.bean.getVirtualHosts(), var2.getVirtualHosts(), true);
            this.computeChildDiff("WLDFSystemResources", this.bean.getWLDFSystemResources(), var2.getWLDFSystemResources(), true);
            this.computeChildDiff("WLECConnectionPools", this.bean.getWLECConnectionPools(), var2.getWLECConnectionPools(), false);
            this.computeChildDiff("WSReliableDeliveryPolicies", this.bean.getWSReliableDeliveryPolicies(), var2.getWSReliableDeliveryPolicies(), true);
            this.computeChildDiff("WTCServers", this.bean.getWTCServers(), var2.getWTCServers(), true);
            this.computeSubDiff("WebAppContainer", this.bean.getWebAppContainer(), var2.getWebAppContainer());
            this.computeChildDiff("WebserviceSecurities", this.bean.getWebserviceSecurities(), var2.getWebserviceSecurities(), true);
            this.computeChildDiff("XMLEntityCaches", this.bean.getXMLEntityCaches(), var2.getXMLEntityCaches(), false);
            this.computeChildDiff("XMLRegistries", this.bean.getXMLRegistries(), var2.getXMLRegistries(), true);
            this.computeDiff("Active", this.bean.isActive(), var2.isActive(), false);
            this.computeDiff("AdministrationMBeanAuditingEnabled", this.bean.isAdministrationMBeanAuditingEnabled(), var2.isAdministrationMBeanAuditingEnabled(), true);
            this.computeDiff("AdministrationPortEnabled", this.bean.isAdministrationPortEnabled(), var2.isAdministrationPortEnabled(), true);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("AutoConfigurationSaveEnabled", this.bean.isAutoConfigurationSaveEnabled(), var2.isAutoConfigurationSaveEnabled(), false);
            }

            this.computeDiff("AutoDeployForSubmodulesEnabled", this.bean.isAutoDeployForSubmodulesEnabled(), var2.isAutoDeployForSubmodulesEnabled(), false);
            this.computeDiff("ClusterConstraintsEnabled", this.bean.isClusterConstraintsEnabled(), var2.isClusterConstraintsEnabled(), false);
            this.computeDiff("ConfigBackupEnabled", this.bean.isConfigBackupEnabled(), var2.isConfigBackupEnabled(), false);
            this.computeDiff("ConsoleEnabled", this.bean.isConsoleEnabled(), var2.isConsoleEnabled(), false);
            this.computeDiff("ExalogicOptimizationsEnabled", this.bean.isExalogicOptimizationsEnabled(), var2.isExalogicOptimizationsEnabled(), false);
            this.computeDiff("GuardianEnabled", this.bean.isGuardianEnabled(), var2.isGuardianEnabled(), false);
            this.computeDiff("InternalAppsDeployOnDemandEnabled", this.bean.isInternalAppsDeployOnDemandEnabled(), var2.isInternalAppsDeployOnDemandEnabled(), false);
            this.computeDiff("MsgIdPrefixCompatibilityEnabled", this.bean.isMsgIdPrefixCompatibilityEnabled(), var2.isMsgIdPrefixCompatibilityEnabled(), true);
            this.computeDiff("OCMEnabled", this.bean.isOCMEnabled(), var2.isOCMEnabled(), false);
            this.computeDiff("ProductionModeEnabled", this.bean.isProductionModeEnabled(), var2.isProductionModeEnabled(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            DomainMBeanImpl var3 = (DomainMBeanImpl)var1.getSourceBean();
            DomainMBeanImpl var4 = (DomainMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("AdminConsole")) {
                  if (var6 == 2) {
                     var3.setAdminConsole((AdminConsoleMBean)this.createCopy((AbstractDescriptorBean)var4.getAdminConsole()));
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3._destroySingleton("AdminConsole", var3.getAdminConsole());
                  }

                  var3._conditionalUnset(var2.isUnsetUpdate(), 129);
               } else if (var5.equals("AdminServerMBean")) {
                  if (var6 == 2) {
                     var3.setAdminServerMBean((AdminServerMBean)this.createCopy((AbstractDescriptorBean)var4.getAdminServerMBean()));
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3._destroySingleton("AdminServerMBean", var3.getAdminServerMBean());
                  }

                  var3._conditionalUnset(var2.isUnsetUpdate(), 114);
               } else if (var5.equals("AdminServerName")) {
                  var3.setAdminServerName(var4.getAdminServerName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 104);
               } else if (var5.equals("AdministrationPort")) {
                  var3.setAdministrationPort(var4.getAdministrationPort());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 37);
               } else if (var5.equals("AdministrationProtocol")) {
                  var3.setAdministrationProtocol(var4.getAdministrationProtocol());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 105);
               } else if (var5.equals("AppDeployments")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                     var3.addAppDeployment((AppDeploymentMBean)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeAppDeployment((AppDeploymentMBean)var2.getRemovedObject());
                  }

                  if (var3.getAppDeployments() == null || var3.getAppDeployments().length == 0) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 46);
                  }
               } else if (var5.equals("Applications")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                     var3.addApplication((ApplicationMBean)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeApplication((ApplicationMBean)var2.getRemovedObject());
                  }

                  if (var3.getApplications() == null || var3.getApplications().length == 0) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 45);
                  }
               } else if (var5.equals("ArchiveConfigurationCount")) {
                  var3.setArchiveConfigurationCount(var4.getArchiveConfigurationCount());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 39);
               } else if (!var5.equals("BasicRealms")) {
                  if (var5.equals("BridgeDestinations")) {
                     if (var6 == 2) {
                        var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                        var3.addBridgeDestination((BridgeDestinationMBean)var2.getAddedObject());
                     } else {
                        if (var6 != 3) {
                           throw new AssertionError("Invalid type: " + var6);
                        }

                        var3.removeBridgeDestination((BridgeDestinationMBean)var2.getRemovedObject());
                     }

                     if (var3.getBridgeDestinations() == null || var3.getBridgeDestinations().length == 0) {
                        var3._conditionalUnset(var2.isUnsetUpdate(), 89);
                     }
                  } else if (var5.equals("CachingRealms")) {
                     if (var6 == 2) {
                        var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                        var3.addCachingRealm((CachingRealmMBean)var2.getAddedObject());
                     } else {
                        if (var6 != 3) {
                           throw new AssertionError("Invalid type: " + var6);
                        }

                        var3.removeCachingRealm((CachingRealmMBean)var2.getRemovedObject());
                     }

                     if (var3.getCachingRealms() == null || var3.getCachingRealms().length == 0) {
                        var3._conditionalUnset(var2.isUnsetUpdate(), 57);
                     }
                  } else if (var5.equals("Clusters")) {
                     if (var6 == 2) {
                        var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                        var3.addCluster((ClusterMBean)var2.getAddedObject());
                     } else {
                        if (var6 != 3) {
                           throw new AssertionError("Invalid type: " + var6);
                        }

                        var3.removeCluster((ClusterMBean)var2.getRemovedObject());
                     }

                     if (var3.getClusters() == null || var3.getClusters().length == 0) {
                        var3._conditionalUnset(var2.isUnsetUpdate(), 26);
                     }
                  } else if (var5.equals("CoherenceClusterSystemResources")) {
                     if (var6 == 2) {
                        var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                        var3.addCoherenceClusterSystemResource((CoherenceClusterSystemResourceMBean)var2.getAddedObject());
                     } else {
                        if (var6 != 3) {
                           throw new AssertionError("Invalid type: " + var6);
                        }

                        var3.removeCoherenceClusterSystemResource((CoherenceClusterSystemResourceMBean)var2.getRemovedObject());
                     }

                     if (var3.getCoherenceClusterSystemResources() == null || var3.getCoherenceClusterSystemResources().length == 0) {
                        var3._conditionalUnset(var2.isUnsetUpdate(), 134);
                     }
                  } else if (var5.equals("CoherenceServers")) {
                     if (var6 == 2) {
                        var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                        var3.addCoherenceServer((CoherenceServerMBean)var2.getAddedObject());
                     } else {
                        if (var6 != 3) {
                           throw new AssertionError("Invalid type: " + var6);
                        }

                        var3.removeCoherenceServer((CoherenceServerMBean)var2.getRemovedObject());
                     }

                     if (var3.getCoherenceServers() == null || var3.getCoherenceServers().length == 0) {
                        var3._conditionalUnset(var2.isUnsetUpdate(), 25);
                     }
                  } else if (var5.equals("ConfigurationAuditType")) {
                     var3.setConfigurationAuditType(var4.getConfigurationAuditType());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 43);
                  } else if (var5.equals("ConfigurationVersion")) {
                     var3.setConfigurationVersion(var4.getConfigurationVersion());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 41);
                  } else if (var5.equals("ConsoleContextPath")) {
                     var3.setConsoleContextPath(var4.getConsoleContextPath());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 21);
                  } else if (var5.equals("ConsoleExtensionDirectory")) {
                     var3.setConsoleExtensionDirectory(var4.getConsoleExtensionDirectory());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 22);
                  } else if (var5.equals("CustomRealms")) {
                     if (var6 == 2) {
                        var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                        var3.addCustomRealm((CustomRealmMBean)var2.getAddedObject());
                     } else {
                        if (var6 != 3) {
                           throw new AssertionError("Invalid type: " + var6);
                        }

                        var3.removeCustomRealm((CustomRealmMBean)var2.getRemovedObject());
                     }

                     if (var3.getCustomRealms() == null || var3.getCustomRealms().length == 0) {
                        var3._conditionalUnset(var2.isUnsetUpdate(), 61);
                     }
                  } else if (var5.equals("CustomResources")) {
                     if (var6 == 2) {
                        var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                        var3.addCustomResource((CustomResourceMBean)var2.getAddedObject());
                     } else {
                        if (var6 != 3) {
                           throw new AssertionError("Invalid type: " + var6);
                        }

                        var3.removeCustomResource((CustomResourceMBean)var2.getRemovedObject());
                     }

                     if (var3.getCustomResources() == null || var3.getCustomResources().length == 0) {
                        var3._conditionalUnset(var2.isUnsetUpdate(), 102);
                     }
                  } else if (var5.equals("DeploymentConfiguration")) {
                     if (var6 == 2) {
                        var3.setDeploymentConfiguration((DeploymentConfigurationMBean)this.createCopy((AbstractDescriptorBean)var4.getDeploymentConfiguration()));
                     } else {
                        if (var6 != 3) {
                           throw new AssertionError("Invalid type: " + var6);
                        }

                        var3._destroySingleton("DeploymentConfiguration", var3.getDeploymentConfiguration());
                     }

                     var3._conditionalUnset(var2.isUnsetUpdate(), 14);
                  } else if (!var5.equals("Deployments")) {
                     if (var5.equals("DomainLibraries")) {
                        if (var6 == 2) {
                           var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                           var3.addDomainLibrary((DomainLibraryMBean)var2.getAddedObject());
                        } else {
                           if (var6 != 3) {
                              throw new AssertionError("Invalid type: " + var6);
                           }

                           var3.removeDomainLibrary((DomainLibraryMBean)var2.getRemovedObject());
                        }

                        if (var3.getDomainLibraries() == null || var3.getDomainLibraries().length == 0) {
                           var3._conditionalUnset(var2.isUnsetUpdate(), 49);
                        }
                     } else if (var5.equals("DomainLogFilters")) {
                        if (var6 == 2) {
                           var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                           var3.addDomainLogFilter((DomainLogFilterMBean)var2.getAddedObject());
                        } else {
                           if (var6 != 3) {
                              throw new AssertionError("Invalid type: " + var6);
                           }

                           var3.removeDomainLogFilter((DomainLogFilterMBean)var2.getRemovedObject());
                        }

                        if (var3.getDomainLogFilters() == null || var3.getDomainLogFilters().length == 0) {
                           var3._conditionalUnset(var2.isUnsetUpdate(), 97);
                        }
                     } else if (var5.equals("DomainVersion")) {
                        var3.setDomainVersion(var4.getDomainVersion());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 7);
                     } else if (var5.equals("EJBContainer")) {
                        if (var6 == 2) {
                           var3.setEJBContainer((EJBContainerMBean)this.createCopy((AbstractDescriptorBean)var4.getEJBContainer()));
                        } else {
                           if (var6 != 3) {
                              throw new AssertionError("Invalid type: " + var6);
                           }

                           var3._destroySingleton("EJBContainer", var3.getEJBContainer());
                        }

                        var3._conditionalUnset(var2.isUnsetUpdate(), 80);
                     } else if (var5.equals("EmbeddedLDAP")) {
                        if (var6 == 2) {
                           var3.setEmbeddedLDAP((EmbeddedLDAPMBean)this.createCopy((AbstractDescriptorBean)var4.getEmbeddedLDAP()));
                        } else {
                           if (var6 != 3) {
                              throw new AssertionError("Invalid type: " + var6);
                           }

                           var3._destroySingleton("EmbeddedLDAP", var3.getEmbeddedLDAP());
                        }

                        var3._conditionalUnset(var2.isUnsetUpdate(), 35);
                     } else if (var5.equals("ErrorHandlings")) {
                        if (var6 == 2) {
                           var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                           var3.addErrorHandling((ErrorHandlingMBean)var2.getAddedObject());
                        } else {
                           if (var6 != 3) {
                              throw new AssertionError("Invalid type: " + var6);
                           }

                           var3.removeErrorHandling((ErrorHandlingMBean)var2.getRemovedObject());
                        }

                        if (var3.getErrorHandlings() == null || var3.getErrorHandlings().length == 0) {
                           var3._conditionalUnset(var2.isUnsetUpdate(), 111);
                        }
                     } else if (var5.equals("FileRealms")) {
                        if (var6 == 2) {
                           var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                           var3.addFileRealm((FileRealmMBean)var2.getAddedObject());
                        } else {
                           if (var6 != 3) {
                              throw new AssertionError("Invalid type: " + var6);
                           }

                           var3.removeFileRealm((FileRealmMBean)var2.getRemovedObject());
                        }

                        if (var3.getFileRealms() == null || var3.getFileRealms().length == 0) {
                           var3._conditionalUnset(var2.isUnsetUpdate(), 56);
                        }
                     } else if (var5.equals("FileStores")) {
                        if (var6 == 2) {
                           var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                           var3.addFileStore((FileStoreMBean)var2.getAddedObject());
                        } else {
                           if (var6 != 3) {
                              throw new AssertionError("Invalid type: " + var6);
                           }

                           var3.removeFileStore((FileStoreMBean)var2.getRemovedObject());
                        }

                        if (var3.getFileStores() == null || var3.getFileStores().length == 0) {
                           var3._conditionalUnset(var2.isUnsetUpdate(), 98);
                        }
                     } else if (var5.equals("FileT3s")) {
                        if (var6 == 2) {
                           var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                           var3.addFileT3((FileT3MBean)var2.getAddedObject());
                        } else {
                           if (var6 != 3) {
                              throw new AssertionError("Invalid type: " + var6);
                           }

                           var3.removeFileT3((FileT3MBean)var2.getRemovedObject());
                        }

                        if (var3.getFileT3s() == null || var3.getFileT3s().length == 0) {
                           var3._conditionalUnset(var2.isUnsetUpdate(), 28);
                        }
                     } else if (var5.equals("ForeignJMSConnectionFactories")) {
                        if (var6 == 2) {
                           var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                           var3.addForeignJMSConnectionFactory((ForeignJMSConnectionFactoryMBean)var2.getAddedObject());
                        } else {
                           if (var6 != 3) {
                              throw new AssertionError("Invalid type: " + var6);
                           }

                           var3.removeForeignJMSConnectionFactory((ForeignJMSConnectionFactoryMBean)var2.getRemovedObject());
                        }

                        if (var3.getForeignJMSConnectionFactories() == null || var3.getForeignJMSConnectionFactories().length == 0) {
                           var3._conditionalUnset(var2.isUnsetUpdate(), 125);
                        }
                     } else if (var5.equals("ForeignJMSDestinations")) {
                        if (var6 == 2) {
                           var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                           var3.addForeignJMSDestination((ForeignJMSDestinationMBean)var2.getAddedObject());
                        } else {
                           if (var6 != 3) {
                              throw new AssertionError("Invalid type: " + var6);
                           }

                           var3.removeForeignJMSDestination((ForeignJMSDestinationMBean)var2.getRemovedObject());
                        }

                        if (var3.getForeignJMSDestinations() == null || var3.getForeignJMSDestinations().length == 0) {
                           var3._conditionalUnset(var2.isUnsetUpdate(), 126);
                        }
                     } else if (var5.equals("ForeignJMSServers")) {
                        if (var6 == 2) {
                           var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                           var3.addForeignJMSServer((ForeignJMSServerMBean)var2.getAddedObject());
                        } else {
                           if (var6 != 3) {
                              throw new AssertionError("Invalid type: " + var6);
                           }

                           var3.removeForeignJMSServer((ForeignJMSServerMBean)var2.getRemovedObject());
                        }

                        if (var3.getForeignJMSServers() == null || var3.getForeignJMSServers().length == 0) {
                           var3._conditionalUnset(var2.isUnsetUpdate(), 90);
                        }
                     } else if (var5.equals("ForeignJNDIProviders")) {
                        if (var6 == 2) {
                           var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                           var3.addForeignJNDIProvider((ForeignJNDIProviderMBean)var2.getAddedObject());
                        } else {
                           if (var6 != 3) {
                              throw new AssertionError("Invalid type: " + var6);
                           }

                           var3.removeForeignJNDIProvider((ForeignJNDIProviderMBean)var2.getRemovedObject());
                        }

                        if (var3.getForeignJNDIProviders() == null || var3.getForeignJNDIProviders().length == 0) {
                           var3._conditionalUnset(var2.isUnsetUpdate(), 103);
                        }
                     } else if (!var5.equals("InternalAppDeployments") && !var5.equals("InternalLibraries")) {
                        if (var5.equals("JDBCConnectionPools")) {
                           if (var6 == 2) {
                              var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                              var3.addJDBCConnectionPool((JDBCConnectionPoolMBean)var2.getAddedObject());
                           } else {
                              if (var6 != 3) {
                                 throw new AssertionError("Invalid type: " + var6);
                              }

                              var3.removeJDBCConnectionPool((JDBCConnectionPoolMBean)var2.getRemovedObject());
                           }

                           if (var3.getJDBCConnectionPools() == null || var3.getJDBCConnectionPools().length == 0) {
                              var3._conditionalUnset(var2.isUnsetUpdate(), 29);
                           }
                        } else if (var5.equals("JDBCDataSourceFactories")) {
                           if (var6 == 2) {
                              var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                              var3.addJDBCDataSourceFactory((JDBCDataSourceFactoryMBean)var2.getAddedObject());
                           } else {
                              if (var6 != 3) {
                                 throw new AssertionError("Invalid type: " + var6);
                              }

                              var3.removeJDBCDataSourceFactory((JDBCDataSourceFactoryMBean)var2.getRemovedObject());
                           }

                           if (var3.getJDBCDataSourceFactories() == null || var3.getJDBCDataSourceFactories().length == 0) {
                              var3._conditionalUnset(var2.isUnsetUpdate(), 52);
                           }
                        } else if (var5.equals("JDBCDataSources")) {
                           if (var6 == 2) {
                              var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                              var3.addJDBCDataSource((JDBCDataSourceMBean)var2.getAddedObject());
                           } else {
                              if (var6 != 3) {
                                 throw new AssertionError("Invalid type: " + var6);
                              }

                              var3.removeJDBCDataSource((JDBCDataSourceMBean)var2.getRemovedObject());
                           }

                           if (var3.getJDBCDataSources() == null || var3.getJDBCDataSources().length == 0) {
                              var3._conditionalUnset(var2.isUnsetUpdate(), 30);
                           }
                        } else if (var5.equals("JDBCMultiPools")) {
                           if (var6 == 2) {
                              var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                              var3.addJDBCMultiPool((JDBCMultiPoolMBean)var2.getAddedObject());
                           } else {
                              if (var6 != 3) {
                                 throw new AssertionError("Invalid type: " + var6);
                              }

                              var3.removeJDBCMultiPool((JDBCMultiPoolMBean)var2.getRemovedObject());
                           }

                           if (var3.getJDBCMultiPools() == null || var3.getJDBCMultiPools().length == 0) {
                              var3._conditionalUnset(var2.isUnsetUpdate(), 32);
                           }
                        } else if (var5.equals("JDBCStores")) {
                           if (var6 == 2) {
                              var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                              var3.addJDBCStore((JDBCStoreMBean)var2.getAddedObject());
                           } else {
                              if (var6 != 3) {
                                 throw new AssertionError("Invalid type: " + var6);
                              }

                              var3.removeJDBCStore((JDBCStoreMBean)var2.getRemovedObject());
                           }

                           if (var3.getJDBCStores() == null || var3.getJDBCStores().length == 0) {
                              var3._conditionalUnset(var2.isUnsetUpdate(), 99);
                           }
                        } else if (var5.equals("JDBCSystemResources")) {
                           if (var6 == 2) {
                              var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                              var3.addJDBCSystemResource((JDBCSystemResourceMBean)var2.getAddedObject());
                           } else {
                              if (var6 != 3) {
                                 throw new AssertionError("Invalid type: " + var6);
                              }

                              var3.removeJDBCSystemResource((JDBCSystemResourceMBean)var2.getRemovedObject());
                           }

                           if (var3.getJDBCSystemResources() == null || var3.getJDBCSystemResources().length == 0) {
                              var3._conditionalUnset(var2.isUnsetUpdate(), 107);
                           }
                        } else if (var5.equals("JDBCTxDataSources")) {
                           if (var6 == 2) {
                              var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                              var3.addJDBCTxDataSource((JDBCTxDataSourceMBean)var2.getAddedObject());
                           } else {
                              if (var6 != 3) {
                                 throw new AssertionError("Invalid type: " + var6);
                              }

                              var3.removeJDBCTxDataSource((JDBCTxDataSourceMBean)var2.getRemovedObject());
                           }

                           if (var3.getJDBCTxDataSources() == null || var3.getJDBCTxDataSources().length == 0) {
                              var3._conditionalUnset(var2.isUnsetUpdate(), 31);
                           }
                        } else if (var5.equals("JMSBridgeDestinations")) {
                           if (var6 == 2) {
                              var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                              var3.addJMSBridgeDestination((JMSBridgeDestinationMBean)var2.getAddedObject());
                           } else {
                              if (var6 != 3) {
                                 throw new AssertionError("Invalid type: " + var6);
                              }

                              var3.removeJMSBridgeDestination((JMSBridgeDestinationMBean)var2.getRemovedObject());
                           }

                           if (var3.getJMSBridgeDestinations() == null || var3.getJMSBridgeDestinations().length == 0) {
                              var3._conditionalUnset(var2.isUnsetUpdate(), 88);
                           }
                        } else if (var5.equals("JMSConnectionConsumers")) {
                           if (var6 == 2) {
                              var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                              var3.addJMSConnectionConsumer((JMSConnectionConsumerMBean)var2.getAddedObject());
                           } else {
                              if (var6 != 3) {
                                 throw new AssertionError("Invalid type: " + var6);
                              }

                              var3.removeJMSConnectionConsumer((JMSConnectionConsumerMBean)var2.getRemovedObject());
                           }

                           if (var3.getJMSConnectionConsumers() == null || var3.getJMSConnectionConsumers().length == 0) {
                              var3._conditionalUnset(var2.isUnsetUpdate(), 127);
                           }
                        } else if (var5.equals("JMSConnectionFactories")) {
                           if (var6 == 2) {
                              var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                              var3.addJMSConnectionFactory((JMSConnectionFactoryMBean)var2.getAddedObject());
                           } else {
                              if (var6 != 3) {
                                 throw new AssertionError("Invalid type: " + var6);
                              }

                              var3.removeJMSConnectionFactory((JMSConnectionFactoryMBean)var2.getRemovedObject());
                           }

                           if (var3.getJMSConnectionFactories() == null || var3.getJMSConnectionFactories().length == 0) {
                              var3._conditionalUnset(var2.isUnsetUpdate(), 86);
                           }
                        } else if (var5.equals("JMSDestinationKeys")) {
                           if (var6 == 2) {
                              var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                              var3.addJMSDestinationKey((JMSDestinationKeyMBean)var2.getAddedObject());
                           } else {
                              if (var6 != 3) {
                                 throw new AssertionError("Invalid type: " + var6);
                              }

                              var3.removeJMSDestinationKey((JMSDestinationKeyMBean)var2.getRemovedObject());
                           }

                           if (var3.getJMSDestinationKeys() == null || var3.getJMSDestinationKeys().length == 0) {
                              var3._conditionalUnset(var2.isUnsetUpdate(), 85);
                           }
                        } else if (var5.equals("JMSDestinations")) {
                           var3.setJMSDestinationsAsString(var4.getJMSDestinationsAsString());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 71);
                        } else if (var5.equals("JMSDistributedQueueMembers")) {
                           if (var6 == 2) {
                              var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                              var3.addJMSDistributedQueueMember((JMSDistributedQueueMemberMBean)var2.getAddedObject());
                           } else {
                              if (var6 != 3) {
                                 throw new AssertionError("Invalid type: " + var6);
                              }

                              var3.removeJMSDistributedQueueMember((JMSDistributedQueueMemberMBean)var2.getRemovedObject());
                           }

                           if (var3.getJMSDistributedQueueMembers() == null || var3.getJMSDistributedQueueMembers().length == 0) {
                              var3._conditionalUnset(var2.isUnsetUpdate(), 115);
                           }
                        } else if (var5.equals("JMSDistributedQueues")) {
                           if (var6 == 2) {
                              var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                              var3.addJMSDistributedQueue((JMSDistributedQueueMBean)var2.getAddedObject());
                           } else {
                              if (var6 != 3) {
                                 throw new AssertionError("Invalid type: " + var6);
                              }

                              var3.removeJMSDistributedQueue((JMSDistributedQueueMBean)var2.getRemovedObject());
                           }

                           if (var3.getJMSDistributedQueues() == null || var3.getJMSDistributedQueues().length == 0) {
                              var3._conditionalUnset(var2.isUnsetUpdate(), 74);
                           }
                        } else if (var5.equals("JMSDistributedTopicMembers")) {
                           if (var6 == 2) {
                              var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                              var3.addJMSDistributedTopicMember((JMSDistributedTopicMemberMBean)var2.getAddedObject());
                           } else {
                              if (var6 != 3) {
                                 throw new AssertionError("Invalid type: " + var6);
                              }

                              var3.removeJMSDistributedTopicMember((JMSDistributedTopicMemberMBean)var2.getRemovedObject());
                           }

                           if (var3.getJMSDistributedTopicMembers() == null || var3.getJMSDistributedTopicMembers().length == 0) {
                              var3._conditionalUnset(var2.isUnsetUpdate(), 116);
                           }
                        } else if (var5.equals("JMSDistributedTopics")) {
                           if (var6 == 2) {
                              var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                              var3.addJMSDistributedTopic((JMSDistributedTopicMBean)var2.getAddedObject());
                           } else {
                              if (var6 != 3) {
                                 throw new AssertionError("Invalid type: " + var6);
                              }

                              var3.removeJMSDistributedTopic((JMSDistributedTopicMBean)var2.getRemovedObject());
                           }

                           if (var3.getJMSDistributedTopics() == null || var3.getJMSDistributedTopics().length == 0) {
                              var3._conditionalUnset(var2.isUnsetUpdate(), 75);
                           }
                        } else if (var5.equals("JMSFileStores")) {
                           if (var6 == 2) {
                              var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                              var3.addJMSFileStore((JMSFileStoreMBean)var2.getAddedObject());
                           } else {
                              if (var6 != 3) {
                                 throw new AssertionError("Invalid type: " + var6);
                              }

                              var3.removeJMSFileStore((JMSFileStoreMBean)var2.getRemovedObject());
                           }

                           if (var3.getJMSFileStores() == null || var3.getJMSFileStores().length == 0) {
                              var3._conditionalUnset(var2.isUnsetUpdate(), 70);
                           }
                        } else if (var5.equals("JMSInteropModules")) {
                           if (var6 == 2) {
                              var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                              var3.addJMSInteropModule((JMSInteropModuleMBean)var2.getAddedObject());
                           } else {
                              if (var6 != 3) {
                                 throw new AssertionError("Invalid type: " + var6);
                              }

                              var3.removeJMSInteropModule((JMSInteropModuleMBean)var2.getRemovedObject());
                           }

                           if (var3.getJMSInteropModules() == null || var3.getJMSInteropModules().length == 0) {
                              var3._conditionalUnset(var2.isUnsetUpdate(), 100);
                           }
                        } else if (var5.equals("JMSJDBCStores")) {
                           if (var6 == 2) {
                              var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                              var3.addJMSJDBCStore((JMSJDBCStoreMBean)var2.getAddedObject());
                           } else {
                              if (var6 != 3) {
                                 throw new AssertionError("Invalid type: " + var6);
                              }

                              var3.removeJMSJDBCStore((JMSJDBCStoreMBean)var2.getRemovedObject());
                           }

                           if (var3.getJMSJDBCStores() == null || var3.getJMSJDBCStores().length == 0) {
                              var3._conditionalUnset(var2.isUnsetUpdate(), 69);
                           }
                        } else if (var5.equals("JMSQueues")) {
                           if (var6 == 2) {
                              var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                              var3.addJMSQueue((JMSQueueMBean)var2.getAddedObject());
                           } else {
                              if (var6 != 3) {
                                 throw new AssertionError("Invalid type: " + var6);
                              }

                              var3.removeJMSQueue((JMSQueueMBean)var2.getRemovedObject());
                           }

                           if (var3.getJMSQueues() == null || var3.getJMSQueues().length == 0) {
                              var3._conditionalUnset(var2.isUnsetUpdate(), 72);
                           }
                        } else if (var5.equals("JMSServers")) {
                           if (var6 == 2) {
                              var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                              var3.addJMSServer((JMSServerMBean)var2.getAddedObject());
                           } else {
                              if (var6 != 3) {
                                 throw new AssertionError("Invalid type: " + var6);
                              }

                              var3.removeJMSServer((JMSServerMBean)var2.getRemovedObject());
                           }

                           if (var3.getJMSServers() == null || var3.getJMSServers().length == 0) {
                              var3._conditionalUnset(var2.isUnsetUpdate(), 67);
                           }
                        } else if (var5.equals("JMSSessionPools")) {
                           if (var6 == 2) {
                              var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                              var3.addJMSSessionPool((JMSSessionPoolMBean)var2.getAddedObject());
                           } else {
                              if (var6 != 3) {
                                 throw new AssertionError("Invalid type: " + var6);
                              }

                              var3.removeJMSSessionPool((JMSSessionPoolMBean)var2.getRemovedObject());
                           }

                           if (var3.getJMSSessionPools() == null || var3.getJMSSessionPools().length == 0) {
                              var3._conditionalUnset(var2.isUnsetUpdate(), 87);
                           }
                        } else if (!var5.equals("JMSStores")) {
                           if (var5.equals("JMSSystemResources")) {
                              if (var6 == 2) {
                                 var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                 var3.addJMSSystemResource((JMSSystemResourceMBean)var2.getAddedObject());
                              } else {
                                 if (var6 != 3) {
                                    throw new AssertionError("Invalid type: " + var6);
                                 }

                                 var3.removeJMSSystemResource((JMSSystemResourceMBean)var2.getRemovedObject());
                              }

                              if (var3.getJMSSystemResources() == null || var3.getJMSSystemResources().length == 0) {
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 101);
                              }
                           } else if (var5.equals("JMSTemplates")) {
                              if (var6 == 2) {
                                 var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                 var3.addJMSTemplate((JMSTemplateMBean)var2.getAddedObject());
                              } else {
                                 if (var6 != 3) {
                                    throw new AssertionError("Invalid type: " + var6);
                                 }

                                 var3.removeJMSTemplate((JMSTemplateMBean)var2.getRemovedObject());
                              }

                              if (var3.getJMSTemplates() == null || var3.getJMSTemplates().length == 0) {
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 76);
                              }
                           } else if (var5.equals("JMSTopics")) {
                              if (var6 == 2) {
                                 var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                 var3.addJMSTopic((JMSTopicMBean)var2.getAddedObject());
                              } else {
                                 if (var6 != 3) {
                                    throw new AssertionError("Invalid type: " + var6);
                                 }

                                 var3.removeJMSTopic((JMSTopicMBean)var2.getRemovedObject());
                              }

                              if (var3.getJMSTopics() == null || var3.getJMSTopics().length == 0) {
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 73);
                              }
                           } else if (var5.equals("JMX")) {
                              if (var6 == 2) {
                                 var3.setJMX((JMXMBean)this.createCopy((AbstractDescriptorBean)var4.getJMX()));
                              } else {
                                 if (var6 != 3) {
                                    throw new AssertionError("Invalid type: " + var6);
                                 }

                                 var3._destroySingleton("JMX", var3.getJMX());
                              }

                              var3._conditionalUnset(var2.isUnsetUpdate(), 82);
                           } else if (var5.equals("JPA")) {
                              if (var6 == 2) {
                                 var3.setJPA((JPAMBean)this.createCopy((AbstractDescriptorBean)var4.getJPA()));
                              } else {
                                 if (var6 != 3) {
                                    throw new AssertionError("Invalid type: " + var6);
                                 }

                                 var3._destroySingleton("JPA", var3.getJPA());
                              }

                              var3._conditionalUnset(var2.isUnsetUpdate(), 13);
                           } else if (var5.equals("JTA")) {
                              if (var6 == 2) {
                                 var3.setJTA((JTAMBean)this.createCopy((AbstractDescriptorBean)var4.getJTA()));
                              } else {
                                 if (var6 != 3) {
                                    throw new AssertionError("Invalid type: " + var6);
                                 }

                                 var3._destroySingleton("JTA", var3.getJTA());
                              }

                              var3._conditionalUnset(var2.isUnsetUpdate(), 12);
                           } else if (var5.equals("JoltConnectionPools")) {
                              if (var6 == 2) {
                                 var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                 var3.addJoltConnectionPool((JoltConnectionPoolMBean)var2.getAddedObject());
                              } else {
                                 if (var6 != 3) {
                                    throw new AssertionError("Invalid type: " + var6);
                                 }

                                 var3.removeJoltConnectionPool((JoltConnectionPoolMBean)var2.getRemovedObject());
                              }

                              if (var3.getJoltConnectionPools() == null || var3.getJoltConnectionPools().length == 0) {
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 95);
                              }
                           } else if (var5.equals("LDAPRealms")) {
                              if (var6 == 2) {
                                 var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                 var3.addLDAPRealm((LDAPRealmMBean)var2.getAddedObject());
                              } else {
                                 if (var6 != 3) {
                                    throw new AssertionError("Invalid type: " + var6);
                                 }

                                 var3.removeLDAPRealm((LDAPRealmMBean)var2.getRemovedObject());
                              }

                              if (var3.getLDAPRealms() == null || var3.getLDAPRealms().length == 0) {
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 62);
                              }
                           } else if (var5.equals("LastModificationTime")) {
                              var3._conditionalUnset(var2.isUnsetUpdate(), 8);
                           } else if (var5.equals("Libraries")) {
                              if (var6 == 2) {
                                 var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                 var3.addLibrary((LibraryMBean)var2.getAddedObject());
                              } else {
                                 if (var6 != 3) {
                                    throw new AssertionError("Invalid type: " + var6);
                                 }

                                 var3.removeLibrary((LibraryMBean)var2.getRemovedObject());
                              }

                              if (var3.getLibraries() == null || var3.getLibraries().length == 0) {
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 48);
                              }
                           } else if (var5.equals("Log")) {
                              if (var6 == 2) {
                                 var3.setLog((LogMBean)this.createCopy((AbstractDescriptorBean)var4.getLog()));
                              } else {
                                 if (var6 != 3) {
                                    throw new AssertionError("Invalid type: " + var6);
                                 }

                                 var3._destroySingleton("Log", var3.getLog());
                              }

                              var3._conditionalUnset(var2.isUnsetUpdate(), 16);
                           } else if (var5.equals("LogFilters")) {
                              if (var6 == 2) {
                                 var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                 var3.addLogFilter((LogFilterMBean)var2.getAddedObject());
                              } else {
                                 if (var6 != 3) {
                                    throw new AssertionError("Invalid type: " + var6);
                                 }

                                 var3.removeLogFilter((LogFilterMBean)var2.getRemovedObject());
                              }

                              if (var3.getLogFilters() == null || var3.getLogFilters().length == 0) {
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 96);
                              }
                           } else if (var5.equals("Machines")) {
                              if (var6 == 2) {
                                 var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                 var3.addMachine((MachineMBean)var2.getAddedObject());
                              } else {
                                 if (var6 != 3) {
                                    throw new AssertionError("Invalid type: " + var6);
                                 }

                                 var3.removeMachine((MachineMBean)var2.getRemovedObject());
                              }

                              if (var3.getMachines() == null || var3.getMachines().length == 0) {
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 53);
                              }
                           } else if (var5.equals("MailSessions")) {
                              if (var6 == 2) {
                                 var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                 var3.addMailSession((MailSessionMBean)var2.getAddedObject());
                              } else {
                                 if (var6 != 3) {
                                    throw new AssertionError("Invalid type: " + var6);
                                 }

                                 var3.removeMailSession((MailSessionMBean)var2.getRemovedObject());
                              }

                              if (var3.getMailSessions() == null || var3.getMailSessions().length == 0) {
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 94);
                              }
                           } else if (var5.equals("MessagingBridges")) {
                              if (var6 == 2) {
                                 var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                 var3.addMessagingBridge((MessagingBridgeMBean)var2.getAddedObject());
                              } else {
                                 if (var6 != 3) {
                                    throw new AssertionError("Invalid type: " + var6);
                                 }

                                 var3.removeMessagingBridge((MessagingBridgeMBean)var2.getRemovedObject());
                              }

                              if (var3.getMessagingBridges() == null || var3.getMessagingBridges().length == 0) {
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 33);
                              }
                           } else if (var5.equals("MigratableRMIServices")) {
                              if (var6 == 2) {
                                 var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                 var3.addMigratableRMIService((MigratableRMIServiceMBean)var2.getAddedObject());
                              } else {
                                 if (var6 != 3) {
                                    throw new AssertionError("Invalid type: " + var6);
                                 }

                                 var3.removeMigratableRMIService((MigratableRMIServiceMBean)var2.getRemovedObject());
                              }

                              if (var3.getMigratableRMIServices() == null || var3.getMigratableRMIServices().length == 0) {
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 113);
                              }
                           } else if (var5.equals("MigratableTargets")) {
                              if (var6 == 2) {
                                 var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                 var3.addMigratableTarget((MigratableTargetMBean)var2.getAddedObject());
                              } else {
                                 if (var6 != 3) {
                                    throw new AssertionError("Invalid type: " + var6);
                                 }

                                 var3.removeMigratableTarget((MigratableTargetMBean)var2.getRemovedObject());
                              }

                              if (var3.getMigratableTargets() == null || var3.getMigratableTargets().length == 0) {
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 79);
                              }
                           } else if (var5.equals("NTRealms")) {
                              if (var6 == 2) {
                                 var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                 var3.addNTRealm((NTRealmMBean)var2.getAddedObject());
                              } else {
                                 if (var6 != 3) {
                                    throw new AssertionError("Invalid type: " + var6);
                                 }

                                 var3.removeNTRealm((NTRealmMBean)var2.getRemovedObject());
                              }

                              if (var3.getNTRealms() == null || var3.getNTRealms().length == 0) {
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 63);
                              }
                           } else if (var5.equals("Name")) {
                              var3.setName(var4.getName());
                              var3._conditionalUnset(var2.isUnsetUpdate(), 2);
                           } else if (var5.equals("NetworkChannels")) {
                              if (var6 == 2) {
                                 var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                 var3.addNetworkChannel((NetworkChannelMBean)var2.getAddedObject());
                              } else {
                                 if (var6 != 3) {
                                    throw new AssertionError("Invalid type: " + var6);
                                 }

                                 var3.removeNetworkChannel((NetworkChannelMBean)var2.getRemovedObject());
                              }

                              if (var3.getNetworkChannels() == null || var3.getNetworkChannels().length == 0) {
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 77);
                              }
                           } else if (var5.equals("PasswordPolicies")) {
                              if (var6 == 2) {
                                 var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                 var3.addPasswordPolicy((PasswordPolicyMBean)var2.getAddedObject());
                              } else {
                                 if (var6 != 3) {
                                    throw new AssertionError("Invalid type: " + var6);
                                 }

                                 var3.removePasswordPolicy((PasswordPolicyMBean)var2.getRemovedObject());
                              }

                              if (var3.getPasswordPolicies() == null || var3.getPasswordPolicies().length == 0) {
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 59);
                              }
                           } else if (var5.equals("PathServices")) {
                              if (var6 == 2) {
                                 var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                 var3.addPathService((PathServiceMBean)var2.getAddedObject());
                              } else {
                                 if (var6 != 3) {
                                    throw new AssertionError("Invalid type: " + var6);
                                 }

                                 var3.removePathService((PathServiceMBean)var2.getRemovedObject());
                              }

                              if (var3.getPathServices() == null || var3.getPathServices().length == 0) {
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 84);
                              }
                           } else if (var5.equals("RDBMSRealms")) {
                              if (var6 == 2) {
                                 var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                 var3.addRDBMSRealm((RDBMSRealmMBean)var2.getAddedObject());
                              } else {
                                 if (var6 != 3) {
                                    throw new AssertionError("Invalid type: " + var6);
                                 }

                                 var3.removeRDBMSRealm((RDBMSRealmMBean)var2.getRemovedObject());
                              }

                              if (var3.getRDBMSRealms() == null || var3.getRDBMSRealms().length == 0) {
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 64);
                              }
                           } else if (var5.equals("Realms")) {
                              if (var6 == 2) {
                                 var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                 var3.addRealm((RealmMBean)var2.getAddedObject());
                              } else {
                                 if (var6 != 3) {
                                    throw new AssertionError("Invalid type: " + var6);
                                 }

                                 var3.removeRealm((RealmMBean)var2.getRemovedObject());
                              }

                              if (var3.getRealms() == null || var3.getRealms().length == 0) {
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 58);
                              }
                           } else if (var5.equals("RemoteSAFContexts")) {
                              if (var6 == 2) {
                                 var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                 var3.addRemoteSAFContext((RemoteSAFContextMBean)var2.getAddedObject());
                              } else {
                                 if (var6 != 3) {
                                    throw new AssertionError("Invalid type: " + var6);
                                 }

                                 var3.removeRemoteSAFContext((RemoteSAFContextMBean)var2.getRemovedObject());
                              }

                              if (var3.getRemoteSAFContexts() == null || var3.getRemoteSAFContexts().length == 0) {
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 112);
                              }
                           } else if (var5.equals("RestfulManagementServices")) {
                              if (var6 == 2) {
                                 var3.setRestfulManagementServices((RestfulManagementServicesMBean)this.createCopy((AbstractDescriptorBean)var4.getRestfulManagementServices()));
                              } else {
                                 if (var6 != 3) {
                                    throw new AssertionError("Invalid type: " + var6);
                                 }

                                 var3._destroySingleton("RestfulManagementServices", var3.getRestfulManagementServices());
                              }

                              var3._conditionalUnset(var2.isUnsetUpdate(), 135);
                           } else if (var5.equals("RootDirectory")) {
                              var3._conditionalUnset(var2.isUnsetUpdate(), 19);
                           } else if (var5.equals("SAFAgents")) {
                              if (var6 == 2) {
                                 var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                 var3.addSAFAgent((SAFAgentMBean)var2.getAddedObject());
                              } else {
                                 if (var6 != 3) {
                                    throw new AssertionError("Invalid type: " + var6);
                                 }

                                 var3.removeSAFAgent((SAFAgentMBean)var2.getRemovedObject());
                              }

                              if (var3.getSAFAgents() == null || var3.getSAFAgents().length == 0) {
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 109);
                              }
                           } else if (var5.equals("SNMPAgent")) {
                              if (var6 == 2) {
                                 var3.setSNMPAgent((SNMPAgentMBean)this.createCopy((AbstractDescriptorBean)var4.getSNMPAgent()));
                              } else {
                                 if (var6 != 3) {
                                    throw new AssertionError("Invalid type: " + var6);
                                 }

                                 var3._destroySingleton("SNMPAgent", var3.getSNMPAgent());
                              }

                              var3._conditionalUnset(var2.isUnsetUpdate(), 17);
                           } else if (var5.equals("SNMPAgentDeployments")) {
                              if (var6 == 2) {
                                 var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                 var3.addSNMPAgentDeployment((SNMPAgentDeploymentMBean)var2.getAddedObject());
                              } else {
                                 if (var6 != 3) {
                                    throw new AssertionError("Invalid type: " + var6);
                                 }

                                 var3.removeSNMPAgentDeployment((SNMPAgentDeploymentMBean)var2.getRemovedObject());
                              }

                              if (var3.getSNMPAgentDeployments() == null || var3.getSNMPAgentDeployments().length == 0) {
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 18);
                              }
                           } else if (var5.equals("SNMPAttributeChanges")) {
                              if (var6 == 2) {
                                 var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                 var3.addSNMPAttributeChange((SNMPAttributeChangeMBean)var2.getAddedObject());
                              } else {
                                 if (var6 != 3) {
                                    throw new AssertionError("Invalid type: " + var6);
                                 }

                                 var3.removeSNMPAttributeChange((SNMPAttributeChangeMBean)var2.getRemovedObject());
                              }

                              if (var3.getSNMPAttributeChanges() == null || var3.getSNMPAttributeChanges().length == 0) {
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 123);
                              }
                           } else if (var5.equals("SNMPCounterMonitors")) {
                              if (var6 == 2) {
                                 var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                 var3.addSNMPCounterMonitor((SNMPCounterMonitorMBean)var2.getAddedObject());
                              } else {
                                 if (var6 != 3) {
                                    throw new AssertionError("Invalid type: " + var6);
                                 }

                                 var3.removeSNMPCounterMonitor((SNMPCounterMonitorMBean)var2.getRemovedObject());
                              }

                              if (var3.getSNMPCounterMonitors() == null || var3.getSNMPCounterMonitors().length == 0) {
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 121);
                              }
                           } else if (var5.equals("SNMPGaugeMonitors")) {
                              if (var6 == 2) {
                                 var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                 var3.addSNMPGaugeMonitor((SNMPGaugeMonitorMBean)var2.getAddedObject());
                              } else {
                                 if (var6 != 3) {
                                    throw new AssertionError("Invalid type: " + var6);
                                 }

                                 var3.removeSNMPGaugeMonitor((SNMPGaugeMonitorMBean)var2.getRemovedObject());
                              }

                              if (var3.getSNMPGaugeMonitors() == null || var3.getSNMPGaugeMonitors().length == 0) {
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 119);
                              }
                           } else if (var5.equals("SNMPLogFilters")) {
                              if (var6 == 2) {
                                 var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                 var3.addSNMPLogFilter((SNMPLogFilterMBean)var2.getAddedObject());
                              } else {
                                 if (var6 != 3) {
                                    throw new AssertionError("Invalid type: " + var6);
                                 }

                                 var3.removeSNMPLogFilter((SNMPLogFilterMBean)var2.getRemovedObject());
                              }

                              if (var3.getSNMPLogFilters() == null || var3.getSNMPLogFilters().length == 0) {
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 122);
                              }
                           } else if (var5.equals("SNMPProxies")) {
                              if (var6 == 2) {
                                 var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                 var3.addSNMPProxy((SNMPProxyMBean)var2.getAddedObject());
                              } else {
                                 if (var6 != 3) {
                                    throw new AssertionError("Invalid type: " + var6);
                                 }

                                 var3.removeSNMPProxy((SNMPProxyMBean)var2.getRemovedObject());
                              }

                              if (var3.getSNMPProxies() == null || var3.getSNMPProxies().length == 0) {
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 118);
                              }
                           } else if (var5.equals("SNMPStringMonitors")) {
                              if (var6 == 2) {
                                 var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                 var3.addSNMPStringMonitor((SNMPStringMonitorMBean)var2.getAddedObject());
                              } else {
                                 if (var6 != 3) {
                                    throw new AssertionError("Invalid type: " + var6);
                                 }

                                 var3.removeSNMPStringMonitor((SNMPStringMonitorMBean)var2.getRemovedObject());
                              }

                              if (var3.getSNMPStringMonitors() == null || var3.getSNMPStringMonitors().length == 0) {
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 120);
                              }
                           } else if (var5.equals("SNMPTrapDestinations")) {
                              if (var6 == 2) {
                                 var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                 var3.addSNMPTrapDestination((SNMPTrapDestinationMBean)var2.getAddedObject());
                              } else {
                                 if (var6 != 3) {
                                    throw new AssertionError("Invalid type: " + var6);
                                 }

                                 var3.removeSNMPTrapDestination((SNMPTrapDestinationMBean)var2.getRemovedObject());
                              }

                              if (var3.getSNMPTrapDestinations() == null || var3.getSNMPTrapDestinations().length == 0) {
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 117);
                              }
                           } else if (var5.equals("Security")) {
                              if (var6 == 2) {
                                 var3.setSecurity((SecurityMBean)this.createCopy((AbstractDescriptorBean)var4.getSecurity()));
                              } else {
                                 if (var6 != 3) {
                                    throw new AssertionError("Invalid type: " + var6);
                                 }

                                 var3._destroySingleton("Security", var3.getSecurity());
                              }

                              var3._conditionalUnset(var2.isUnsetUpdate(), 11);
                           } else if (var5.equals("SecurityConfiguration")) {
                              if (var6 == 2) {
                                 var3.setSecurityConfiguration((SecurityConfigurationMBean)this.createCopy((AbstractDescriptorBean)var4.getSecurityConfiguration()));
                              } else {
                                 if (var6 != 3) {
                                    throw new AssertionError("Invalid type: " + var6);
                                 }

                                 var3._destroySingleton("SecurityConfiguration", var3.getSecurityConfiguration());
                              }

                              var3._conditionalUnset(var2.isUnsetUpdate(), 10);
                           } else if (var5.equals("SelfTuning")) {
                              if (var6 == 2) {
                                 var3.setSelfTuning((SelfTuningMBean)this.createCopy((AbstractDescriptorBean)var4.getSelfTuning()));
                              } else {
                                 if (var6 != 3) {
                                    throw new AssertionError("Invalid type: " + var6);
                                 }

                                 var3._destroySingleton("SelfTuning", var3.getSelfTuning());
                              }

                              var3._conditionalUnset(var2.isUnsetUpdate(), 83);
                           } else if (var5.equals("Servers")) {
                              if (var6 == 2) {
                                 var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                 var3.addServer((ServerMBean)var2.getAddedObject());
                              } else {
                                 if (var6 != 3) {
                                    throw new AssertionError("Invalid type: " + var6);
                                 }

                                 var3.removeServer((ServerMBean)var2.getRemovedObject());
                              }

                              if (var3.getServers() == null || var3.getServers().length == 0) {
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 24);
                              }
                           } else if (var5.equals("ShutdownClasses")) {
                              if (var6 == 2) {
                                 var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                 var3.addShutdownClass((ShutdownClassMBean)var2.getAddedObject());
                              } else {
                                 if (var6 != 3) {
                                    throw new AssertionError("Invalid type: " + var6);
                                 }

                                 var3.removeShutdownClass((ShutdownClassMBean)var2.getRemovedObject());
                              }

                              if (var3.getShutdownClasses() == null || var3.getShutdownClasses().length == 0) {
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 91);
                              }
                           } else if (var5.equals("SingletonServices")) {
                              if (var6 == 2) {
                                 var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                 var3.addSingletonService((SingletonServiceMBean)var2.getAddedObject());
                              } else {
                                 if (var6 != 3) {
                                    throw new AssertionError("Invalid type: " + var6);
                                 }

                                 var3.removeSingletonService((SingletonServiceMBean)var2.getRemovedObject());
                              }

                              if (var3.getSingletonServices() == null || var3.getSingletonServices().length == 0) {
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 93);
                              }
                           } else if (var5.equals("StartupClasses")) {
                              if (var6 == 2) {
                                 var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                 var3.addStartupClass((StartupClassMBean)var2.getAddedObject());
                              } else {
                                 if (var6 != 3) {
                                    throw new AssertionError("Invalid type: " + var6);
                                 }

                                 var3.removeStartupClass((StartupClassMBean)var2.getRemovedObject());
                              }

                              if (var3.getStartupClasses() == null || var3.getStartupClasses().length == 0) {
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 92);
                              }
                           } else if (!var5.equals("SystemResources") && !var5.equals("Targets")) {
                              if (var5.equals("UnixRealms")) {
                                 if (var6 == 2) {
                                    var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                    var3.addUnixRealm((UnixRealmMBean)var2.getAddedObject());
                                 } else {
                                    if (var6 != 3) {
                                       throw new AssertionError("Invalid type: " + var6);
                                    }

                                    var3.removeUnixRealm((UnixRealmMBean)var2.getRemovedObject());
                                 }

                                 if (var3.getUnixRealms() == null || var3.getUnixRealms().length == 0) {
                                    var3._conditionalUnset(var2.isUnsetUpdate(), 65);
                                 }
                              } else if (var5.equals("VirtualHosts")) {
                                 if (var6 == 2) {
                                    var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                    var3.addVirtualHost((VirtualHostMBean)var2.getAddedObject());
                                 } else {
                                    if (var6 != 3) {
                                       throw new AssertionError("Invalid type: " + var6);
                                    }

                                    var3.removeVirtualHost((VirtualHostMBean)var2.getRemovedObject());
                                 }

                                 if (var3.getVirtualHosts() == null || var3.getVirtualHosts().length == 0) {
                                    var3._conditionalUnset(var2.isUnsetUpdate(), 78);
                                 }
                              } else if (var5.equals("WLDFSystemResources")) {
                                 if (var6 == 2) {
                                    var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                    var3.addWLDFSystemResource((WLDFSystemResourceMBean)var2.getAddedObject());
                                 } else {
                                    if (var6 != 3) {
                                       throw new AssertionError("Invalid type: " + var6);
                                    }

                                    var3.removeWLDFSystemResource((WLDFSystemResourceMBean)var2.getRemovedObject());
                                 }

                                 if (var3.getWLDFSystemResources() == null || var3.getWLDFSystemResources().length == 0) {
                                    var3._conditionalUnset(var2.isUnsetUpdate(), 106);
                                 }
                              } else if (var5.equals("WLECConnectionPools")) {
                                 if (var6 == 2) {
                                    var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                    var3.addWLECConnectionPool((WLECConnectionPoolMBean)var2.getAddedObject());
                                 } else {
                                    if (var6 != 3) {
                                       throw new AssertionError("Invalid type: " + var6);
                                    }

                                    var3.removeWLECConnectionPool((WLECConnectionPoolMBean)var2.getRemovedObject());
                                 }

                                 if (var3.getWLECConnectionPools() == null || var3.getWLECConnectionPools().length == 0) {
                                    var3._conditionalUnset(var2.isUnsetUpdate(), 110);
                                 }
                              } else if (var5.equals("WSReliableDeliveryPolicies")) {
                                 if (var6 == 2) {
                                    var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                    var3.addWSReliableDeliveryPolicy((WSReliableDeliveryPolicyMBean)var2.getAddedObject());
                                 } else {
                                    if (var6 != 3) {
                                       throw new AssertionError("Invalid type: " + var6);
                                    }

                                    var3.removeWSReliableDeliveryPolicy((WSReliableDeliveryPolicyMBean)var2.getRemovedObject());
                                 }

                                 if (var3.getWSReliableDeliveryPolicies() == null || var3.getWSReliableDeliveryPolicies().length == 0) {
                                    var3._conditionalUnset(var2.isUnsetUpdate(), 51);
                                 }
                              } else if (var5.equals("WTCServers")) {
                                 if (var6 == 2) {
                                    var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                    var3.addWTCServer((WTCServerMBean)var2.getAddedObject());
                                 } else {
                                    if (var6 != 3) {
                                       throw new AssertionError("Invalid type: " + var6);
                                    }

                                    var3.removeWTCServer((WTCServerMBean)var2.getRemovedObject());
                                 }

                                 if (var3.getWTCServers() == null || var3.getWTCServers().length == 0) {
                                    var3._conditionalUnset(var2.isUnsetUpdate(), 15);
                                 }
                              } else if (var5.equals("WebAppContainer")) {
                                 if (var6 == 2) {
                                    var3.setWebAppContainer((WebAppContainerMBean)this.createCopy((AbstractDescriptorBean)var4.getWebAppContainer()));
                                 } else {
                                    if (var6 != 3) {
                                       throw new AssertionError("Invalid type: " + var6);
                                    }

                                    var3._destroySingleton("WebAppContainer", var3.getWebAppContainer());
                                 }

                                 var3._conditionalUnset(var2.isUnsetUpdate(), 81);
                              } else if (var5.equals("WebserviceSecurities")) {
                                 if (var6 == 2) {
                                    var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                    var3.addWebserviceSecurity((WebserviceSecurityMBean)var2.getAddedObject());
                                 } else {
                                    if (var6 != 3) {
                                       throw new AssertionError("Invalid type: " + var6);
                                    }

                                    var3.removeWebserviceSecurity((WebserviceSecurityMBean)var2.getRemovedObject());
                                 }

                                 if (var3.getWebserviceSecurities() == null || var3.getWebserviceSecurities().length == 0) {
                                    var3._conditionalUnset(var2.isUnsetUpdate(), 124);
                                 }
                              } else if (var5.equals("XMLEntityCaches")) {
                                 if (var6 == 2) {
                                    var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                    var3.addXMLEntityCache((XMLEntityCacheMBean)var2.getAddedObject());
                                 } else {
                                    if (var6 != 3) {
                                       throw new AssertionError("Invalid type: " + var6);
                                    }

                                    var3.removeXMLEntityCache((XMLEntityCacheMBean)var2.getRemovedObject());
                                 }

                                 if (var3.getXMLEntityCaches() == null || var3.getXMLEntityCaches().length == 0) {
                                    var3._conditionalUnset(var2.isUnsetUpdate(), 54);
                                 }
                              } else if (var5.equals("XMLRegistries")) {
                                 if (var6 == 2) {
                                    var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                                    var3.addXMLRegistry((XMLRegistryMBean)var2.getAddedObject());
                                 } else {
                                    if (var6 != 3) {
                                       throw new AssertionError("Invalid type: " + var6);
                                    }

                                    var3.removeXMLRegistry((XMLRegistryMBean)var2.getRemovedObject());
                                 }

                                 if (var3.getXMLRegistries() == null || var3.getXMLRegistries().length == 0) {
                                    var3._conditionalUnset(var2.isUnsetUpdate(), 55);
                                 }
                              } else if (var5.equals("Active")) {
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 9);
                              } else if (var5.equals("AdministrationMBeanAuditingEnabled")) {
                                 var3.setAdministrationMBeanAuditingEnabled(var4.isAdministrationMBeanAuditingEnabled());
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 42);
                              } else if (var5.equals("AdministrationPortEnabled")) {
                                 var3.setAdministrationPortEnabled(var4.isAdministrationPortEnabled());
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 36);
                              } else if (var5.equals("AutoConfigurationSaveEnabled")) {
                                 var3.setAutoConfigurationSaveEnabled(var4.isAutoConfigurationSaveEnabled());
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 23);
                              } else if (var5.equals("AutoDeployForSubmodulesEnabled")) {
                                 var3.setAutoDeployForSubmodulesEnabled(var4.isAutoDeployForSubmodulesEnabled());
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 128);
                              } else if (var5.equals("ClusterConstraintsEnabled")) {
                                 var3.setClusterConstraintsEnabled(var4.isClusterConstraintsEnabled());
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 44);
                              } else if (var5.equals("ConfigBackupEnabled")) {
                                 var3.setConfigBackupEnabled(var4.isConfigBackupEnabled());
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 40);
                              } else if (var5.equals("ConsoleEnabled")) {
                                 var3.setConsoleEnabled(var4.isConsoleEnabled());
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 20);
                              } else if (var5.equals("ExalogicOptimizationsEnabled")) {
                                 var3.setExalogicOptimizationsEnabled(var4.isExalogicOptimizationsEnabled());
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 38);
                              } else if (var5.equals("GuardianEnabled")) {
                                 var3.setGuardianEnabled(var4.isGuardianEnabled());
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 131);
                              } else if (var5.equals("InternalAppsDeployOnDemandEnabled")) {
                                 var3.setInternalAppsDeployOnDemandEnabled(var4.isInternalAppsDeployOnDemandEnabled());
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 130);
                              } else if (var5.equals("MsgIdPrefixCompatibilityEnabled")) {
                                 var3.setMsgIdPrefixCompatibilityEnabled(var4.isMsgIdPrefixCompatibilityEnabled());
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 133);
                              } else if (var5.equals("OCMEnabled")) {
                                 var3.setOCMEnabled(var4.isOCMEnabled());
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 132);
                              } else if (var5.equals("ProductionModeEnabled")) {
                                 var3.setProductionModeEnabled(var4.isProductionModeEnabled());
                                 var3._conditionalUnset(var2.isUnsetUpdate(), 34);
                              } else {
                                 super.applyPropertyUpdate(var1, var2);
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
            DomainMBeanImpl var5 = (DomainMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("AdminConsole")) && this.bean.isAdminConsoleSet() && !var5._isSet(129)) {
               AdminConsoleMBean var4 = this.bean.getAdminConsole();
               var5.setAdminConsole((AdminConsoleMBean)null);
               var5.setAdminConsole(var4 == null ? null : (AdminConsoleMBean)this.createCopy((AbstractDescriptorBean)var4, var2));
            }

            if (var2 && (var3 == null || !var3.contains("AdminServerMBean")) && this.bean.isAdminServerMBeanSet() && !var5._isSet(114)) {
               AdminServerMBean var11 = this.bean.getAdminServerMBean();
               var5.setAdminServerMBean((AdminServerMBean)null);
               var5.setAdminServerMBean(var11 == null ? null : (AdminServerMBean)this.createCopy((AbstractDescriptorBean)var11, var2));
            }

            if ((var3 == null || !var3.contains("AdminServerName")) && this.bean.isAdminServerNameSet()) {
               var5.setAdminServerName(this.bean.getAdminServerName());
            }

            if ((var3 == null || !var3.contains("AdministrationPort")) && this.bean.isAdministrationPortSet()) {
               var5.setAdministrationPort(this.bean.getAdministrationPort());
            }

            if ((var3 == null || !var3.contains("AdministrationProtocol")) && this.bean.isAdministrationProtocolSet()) {
               var5.setAdministrationProtocol(this.bean.getAdministrationProtocol());
            }

            int var8;
            if ((var3 == null || !var3.contains("AppDeployments")) && this.bean.isAppDeploymentsSet() && !var5._isSet(46)) {
               AppDeploymentMBean[] var6 = this.bean.getAppDeployments();
               AppDeploymentMBean[] var7 = new AppDeploymentMBean[var6.length];

               for(var8 = 0; var8 < var7.length; ++var8) {
                  var7[var8] = (AppDeploymentMBean)((AppDeploymentMBean)this.createCopy((AbstractDescriptorBean)var6[var8], var2));
               }

               var5.setAppDeployments(var7);
            }

            if (var2 && (var3 == null || !var3.contains("Applications")) && this.bean.isApplicationsSet() && !var5._isSet(45)) {
               ApplicationMBean[] var17 = this.bean.getApplications();
               ApplicationMBean[] var23 = new ApplicationMBean[var17.length];

               for(var8 = 0; var8 < var23.length; ++var8) {
                  var23[var8] = (ApplicationMBean)((ApplicationMBean)this.createCopy((AbstractDescriptorBean)var17[var8], var2));
               }

               var5.setApplications(var23);
            }

            if ((var3 == null || !var3.contains("ArchiveConfigurationCount")) && this.bean.isArchiveConfigurationCountSet()) {
               var5.setArchiveConfigurationCount(this.bean.getArchiveConfigurationCount());
            }

            if ((var3 == null || !var3.contains("BridgeDestinations")) && this.bean.isBridgeDestinationsSet() && !var5._isSet(89)) {
               BridgeDestinationMBean[] var19 = this.bean.getBridgeDestinations();
               BridgeDestinationMBean[] var26 = new BridgeDestinationMBean[var19.length];

               for(var8 = 0; var8 < var26.length; ++var8) {
                  var26[var8] = (BridgeDestinationMBean)((BridgeDestinationMBean)this.createCopy((AbstractDescriptorBean)var19[var8], var2));
               }

               var5.setBridgeDestinations(var26);
            }

            if ((var3 == null || !var3.contains("CachingRealms")) && this.bean.isCachingRealmsSet() && !var5._isSet(57)) {
               CachingRealmMBean[] var21 = this.bean.getCachingRealms();
               CachingRealmMBean[] var29 = new CachingRealmMBean[var21.length];

               for(var8 = 0; var8 < var29.length; ++var8) {
                  var29[var8] = (CachingRealmMBean)((CachingRealmMBean)this.createCopy((AbstractDescriptorBean)var21[var8], var2));
               }

               var5.setCachingRealms(var29);
            }

            if ((var3 == null || !var3.contains("Clusters")) && this.bean.isClustersSet() && !var5._isSet(26)) {
               ClusterMBean[] var24 = this.bean.getClusters();
               ClusterMBean[] var32 = new ClusterMBean[var24.length];

               for(var8 = 0; var8 < var32.length; ++var8) {
                  var32[var8] = (ClusterMBean)((ClusterMBean)this.createCopy((AbstractDescriptorBean)var24[var8], var2));
               }

               var5.setClusters(var32);
            }

            if ((var3 == null || !var3.contains("CoherenceClusterSystemResources")) && this.bean.isCoherenceClusterSystemResourcesSet() && !var5._isSet(134)) {
               CoherenceClusterSystemResourceMBean[] var27 = this.bean.getCoherenceClusterSystemResources();
               CoherenceClusterSystemResourceMBean[] var35 = new CoherenceClusterSystemResourceMBean[var27.length];

               for(var8 = 0; var8 < var35.length; ++var8) {
                  var35[var8] = (CoherenceClusterSystemResourceMBean)((CoherenceClusterSystemResourceMBean)this.createCopy((AbstractDescriptorBean)var27[var8], var2));
               }

               var5.setCoherenceClusterSystemResources(var35);
            }

            if ((var3 == null || !var3.contains("CoherenceServers")) && this.bean.isCoherenceServersSet() && !var5._isSet(25)) {
               CoherenceServerMBean[] var30 = this.bean.getCoherenceServers();
               CoherenceServerMBean[] var38 = new CoherenceServerMBean[var30.length];

               for(var8 = 0; var8 < var38.length; ++var8) {
                  var38[var8] = (CoherenceServerMBean)((CoherenceServerMBean)this.createCopy((AbstractDescriptorBean)var30[var8], var2));
               }

               var5.setCoherenceServers(var38);
            }

            if ((var3 == null || !var3.contains("ConfigurationAuditType")) && this.bean.isConfigurationAuditTypeSet()) {
               var5.setConfigurationAuditType(this.bean.getConfigurationAuditType());
            }

            if ((var3 == null || !var3.contains("ConfigurationVersion")) && this.bean.isConfigurationVersionSet()) {
               var5.setConfigurationVersion(this.bean.getConfigurationVersion());
            }

            if ((var3 == null || !var3.contains("ConsoleContextPath")) && this.bean.isConsoleContextPathSet()) {
               var5.setConsoleContextPath(this.bean.getConsoleContextPath());
            }

            if ((var3 == null || !var3.contains("ConsoleExtensionDirectory")) && this.bean.isConsoleExtensionDirectorySet()) {
               var5.setConsoleExtensionDirectory(this.bean.getConsoleExtensionDirectory());
            }

            if ((var3 == null || !var3.contains("CustomRealms")) && this.bean.isCustomRealmsSet() && !var5._isSet(61)) {
               CustomRealmMBean[] var33 = this.bean.getCustomRealms();
               CustomRealmMBean[] var40 = new CustomRealmMBean[var33.length];

               for(var8 = 0; var8 < var40.length; ++var8) {
                  var40[var8] = (CustomRealmMBean)((CustomRealmMBean)this.createCopy((AbstractDescriptorBean)var33[var8], var2));
               }

               var5.setCustomRealms(var40);
            }

            if ((var3 == null || !var3.contains("CustomResources")) && this.bean.isCustomResourcesSet() && !var5._isSet(102)) {
               CustomResourceMBean[] var36 = this.bean.getCustomResources();
               CustomResourceMBean[] var42 = new CustomResourceMBean[var36.length];

               for(var8 = 0; var8 < var42.length; ++var8) {
                  var42[var8] = (CustomResourceMBean)((CustomResourceMBean)this.createCopy((AbstractDescriptorBean)var36[var8], var2));
               }

               var5.setCustomResources(var42);
            }

            if ((var3 == null || !var3.contains("DeploymentConfiguration")) && this.bean.isDeploymentConfigurationSet() && !var5._isSet(14)) {
               DeploymentConfigurationMBean var12 = this.bean.getDeploymentConfiguration();
               var5.setDeploymentConfiguration((DeploymentConfigurationMBean)null);
               var5.setDeploymentConfiguration(var12 == null ? null : (DeploymentConfigurationMBean)this.createCopy((AbstractDescriptorBean)var12, var2));
            }

            if ((var3 == null || !var3.contains("DomainLibraries")) && this.bean.isDomainLibrariesSet() && !var5._isSet(49)) {
               DomainLibraryMBean[] var39 = this.bean.getDomainLibraries();
               DomainLibraryMBean[] var44 = new DomainLibraryMBean[var39.length];

               for(var8 = 0; var8 < var44.length; ++var8) {
                  var44[var8] = (DomainLibraryMBean)((DomainLibraryMBean)this.createCopy((AbstractDescriptorBean)var39[var8], var2));
               }

               var5.setDomainLibraries(var44);
            }

            if (var2 && (var3 == null || !var3.contains("DomainLogFilters")) && this.bean.isDomainLogFiltersSet() && !var5._isSet(97)) {
               DomainLogFilterMBean[] var41 = this.bean.getDomainLogFilters();
               DomainLogFilterMBean[] var46 = new DomainLogFilterMBean[var41.length];

               for(var8 = 0; var8 < var46.length; ++var8) {
                  var46[var8] = (DomainLogFilterMBean)((DomainLogFilterMBean)this.createCopy((AbstractDescriptorBean)var41[var8], var2));
               }

               var5.setDomainLogFilters(var46);
            }

            if ((var3 == null || !var3.contains("DomainVersion")) && this.bean.isDomainVersionSet()) {
               var5.setDomainVersion(this.bean.getDomainVersion());
            }

            if ((var3 == null || !var3.contains("EJBContainer")) && this.bean.isEJBContainerSet() && !var5._isSet(80)) {
               EJBContainerMBean var13 = this.bean.getEJBContainer();
               var5.setEJBContainer((EJBContainerMBean)null);
               var5.setEJBContainer(var13 == null ? null : (EJBContainerMBean)this.createCopy((AbstractDescriptorBean)var13, var2));
            }

            if ((var3 == null || !var3.contains("EmbeddedLDAP")) && this.bean.isEmbeddedLDAPSet() && !var5._isSet(35)) {
               EmbeddedLDAPMBean var14 = this.bean.getEmbeddedLDAP();
               var5.setEmbeddedLDAP((EmbeddedLDAPMBean)null);
               var5.setEmbeddedLDAP(var14 == null ? null : (EmbeddedLDAPMBean)this.createCopy((AbstractDescriptorBean)var14, var2));
            }

            if ((var3 == null || !var3.contains("ErrorHandlings")) && this.bean.isErrorHandlingsSet() && !var5._isSet(111)) {
               ErrorHandlingMBean[] var43 = this.bean.getErrorHandlings();
               ErrorHandlingMBean[] var48 = new ErrorHandlingMBean[var43.length];

               for(var8 = 0; var8 < var48.length; ++var8) {
                  var48[var8] = (ErrorHandlingMBean)((ErrorHandlingMBean)this.createCopy((AbstractDescriptorBean)var43[var8], var2));
               }

               var5.setErrorHandlings(var48);
            }

            if ((var3 == null || !var3.contains("FileRealms")) && this.bean.isFileRealmsSet() && !var5._isSet(56)) {
               FileRealmMBean[] var45 = this.bean.getFileRealms();
               FileRealmMBean[] var50 = new FileRealmMBean[var45.length];

               for(var8 = 0; var8 < var50.length; ++var8) {
                  var50[var8] = (FileRealmMBean)((FileRealmMBean)this.createCopy((AbstractDescriptorBean)var45[var8], var2));
               }

               var5.setFileRealms(var50);
            }

            if ((var3 == null || !var3.contains("FileStores")) && this.bean.isFileStoresSet() && !var5._isSet(98)) {
               FileStoreMBean[] var47 = this.bean.getFileStores();
               FileStoreMBean[] var52 = new FileStoreMBean[var47.length];

               for(var8 = 0; var8 < var52.length; ++var8) {
                  var52[var8] = (FileStoreMBean)((FileStoreMBean)this.createCopy((AbstractDescriptorBean)var47[var8], var2));
               }

               var5.setFileStores(var52);
            }

            if ((var3 == null || !var3.contains("FileT3s")) && this.bean.isFileT3sSet() && !var5._isSet(28)) {
               FileT3MBean[] var49 = this.bean.getFileT3s();
               FileT3MBean[] var54 = new FileT3MBean[var49.length];

               for(var8 = 0; var8 < var54.length; ++var8) {
                  var54[var8] = (FileT3MBean)((FileT3MBean)this.createCopy((AbstractDescriptorBean)var49[var8], var2));
               }

               var5.setFileT3s(var54);
            }

            if (var2 && (var3 == null || !var3.contains("ForeignJMSConnectionFactories")) && this.bean.isForeignJMSConnectionFactoriesSet() && !var5._isSet(125)) {
               ForeignJMSConnectionFactoryMBean[] var51 = this.bean.getForeignJMSConnectionFactories();
               ForeignJMSConnectionFactoryMBean[] var56 = new ForeignJMSConnectionFactoryMBean[var51.length];

               for(var8 = 0; var8 < var56.length; ++var8) {
                  var56[var8] = (ForeignJMSConnectionFactoryMBean)((ForeignJMSConnectionFactoryMBean)this.createCopy((AbstractDescriptorBean)var51[var8], var2));
               }

               var5.setForeignJMSConnectionFactories(var56);
            }

            if (var2 && (var3 == null || !var3.contains("ForeignJMSDestinations")) && this.bean.isForeignJMSDestinationsSet() && !var5._isSet(126)) {
               ForeignJMSDestinationMBean[] var53 = this.bean.getForeignJMSDestinations();
               ForeignJMSDestinationMBean[] var58 = new ForeignJMSDestinationMBean[var53.length];

               for(var8 = 0; var8 < var58.length; ++var8) {
                  var58[var8] = (ForeignJMSDestinationMBean)((ForeignJMSDestinationMBean)this.createCopy((AbstractDescriptorBean)var53[var8], var2));
               }

               var5.setForeignJMSDestinations(var58);
            }

            if (var2 && (var3 == null || !var3.contains("ForeignJMSServers")) && this.bean.isForeignJMSServersSet() && !var5._isSet(90)) {
               ForeignJMSServerMBean[] var55 = this.bean.getForeignJMSServers();
               ForeignJMSServerMBean[] var60 = new ForeignJMSServerMBean[var55.length];

               for(var8 = 0; var8 < var60.length; ++var8) {
                  var60[var8] = (ForeignJMSServerMBean)((ForeignJMSServerMBean)this.createCopy((AbstractDescriptorBean)var55[var8], var2));
               }

               var5.setForeignJMSServers(var60);
            }

            if ((var3 == null || !var3.contains("ForeignJNDIProviders")) && this.bean.isForeignJNDIProvidersSet() && !var5._isSet(103)) {
               ForeignJNDIProviderMBean[] var57 = this.bean.getForeignJNDIProviders();
               ForeignJNDIProviderMBean[] var62 = new ForeignJNDIProviderMBean[var57.length];

               for(var8 = 0; var8 < var62.length; ++var8) {
                  var62[var8] = (ForeignJNDIProviderMBean)((ForeignJNDIProviderMBean)this.createCopy((AbstractDescriptorBean)var57[var8], var2));
               }

               var5.setForeignJNDIProviders(var62);
            }

            if (var2 && (var3 == null || !var3.contains("JDBCConnectionPools")) && this.bean.isJDBCConnectionPoolsSet() && !var5._isSet(29)) {
               JDBCConnectionPoolMBean[] var59 = this.bean.getJDBCConnectionPools();
               JDBCConnectionPoolMBean[] var64 = new JDBCConnectionPoolMBean[var59.length];

               for(var8 = 0; var8 < var64.length; ++var8) {
                  var64[var8] = (JDBCConnectionPoolMBean)((JDBCConnectionPoolMBean)this.createCopy((AbstractDescriptorBean)var59[var8], var2));
               }

               var5.setJDBCConnectionPools(var64);
            }

            if ((var3 == null || !var3.contains("JDBCDataSourceFactories")) && this.bean.isJDBCDataSourceFactoriesSet() && !var5._isSet(52)) {
               JDBCDataSourceFactoryMBean[] var61 = this.bean.getJDBCDataSourceFactories();
               JDBCDataSourceFactoryMBean[] var66 = new JDBCDataSourceFactoryMBean[var61.length];

               for(var8 = 0; var8 < var66.length; ++var8) {
                  var66[var8] = (JDBCDataSourceFactoryMBean)((JDBCDataSourceFactoryMBean)this.createCopy((AbstractDescriptorBean)var61[var8], var2));
               }

               var5.setJDBCDataSourceFactories(var66);
            }

            if (var2 && (var3 == null || !var3.contains("JDBCDataSources")) && this.bean.isJDBCDataSourcesSet() && !var5._isSet(30)) {
               JDBCDataSourceMBean[] var63 = this.bean.getJDBCDataSources();
               JDBCDataSourceMBean[] var68 = new JDBCDataSourceMBean[var63.length];

               for(var8 = 0; var8 < var68.length; ++var8) {
                  var68[var8] = (JDBCDataSourceMBean)((JDBCDataSourceMBean)this.createCopy((AbstractDescriptorBean)var63[var8], var2));
               }

               var5.setJDBCDataSources(var68);
            }

            if (var2 && (var3 == null || !var3.contains("JDBCMultiPools")) && this.bean.isJDBCMultiPoolsSet() && !var5._isSet(32)) {
               JDBCMultiPoolMBean[] var65 = this.bean.getJDBCMultiPools();
               JDBCMultiPoolMBean[] var70 = new JDBCMultiPoolMBean[var65.length];

               for(var8 = 0; var8 < var70.length; ++var8) {
                  var70[var8] = (JDBCMultiPoolMBean)((JDBCMultiPoolMBean)this.createCopy((AbstractDescriptorBean)var65[var8], var2));
               }

               var5.setJDBCMultiPools(var70);
            }

            if ((var3 == null || !var3.contains("JDBCStores")) && this.bean.isJDBCStoresSet() && !var5._isSet(99)) {
               JDBCStoreMBean[] var67 = this.bean.getJDBCStores();
               JDBCStoreMBean[] var72 = new JDBCStoreMBean[var67.length];

               for(var8 = 0; var8 < var72.length; ++var8) {
                  var72[var8] = (JDBCStoreMBean)((JDBCStoreMBean)this.createCopy((AbstractDescriptorBean)var67[var8], var2));
               }

               var5.setJDBCStores(var72);
            }

            if ((var3 == null || !var3.contains("JDBCSystemResources")) && this.bean.isJDBCSystemResourcesSet() && !var5._isSet(107)) {
               JDBCSystemResourceMBean[] var69 = this.bean.getJDBCSystemResources();
               JDBCSystemResourceMBean[] var74 = new JDBCSystemResourceMBean[var69.length];

               for(var8 = 0; var8 < var74.length; ++var8) {
                  var74[var8] = (JDBCSystemResourceMBean)((JDBCSystemResourceMBean)this.createCopy((AbstractDescriptorBean)var69[var8], var2));
               }

               var5.setJDBCSystemResources(var74);
            }

            if (var2 && (var3 == null || !var3.contains("JDBCTxDataSources")) && this.bean.isJDBCTxDataSourcesSet() && !var5._isSet(31)) {
               JDBCTxDataSourceMBean[] var71 = this.bean.getJDBCTxDataSources();
               JDBCTxDataSourceMBean[] var76 = new JDBCTxDataSourceMBean[var71.length];

               for(var8 = 0; var8 < var76.length; ++var8) {
                  var76[var8] = (JDBCTxDataSourceMBean)((JDBCTxDataSourceMBean)this.createCopy((AbstractDescriptorBean)var71[var8], var2));
               }

               var5.setJDBCTxDataSources(var76);
            }

            if ((var3 == null || !var3.contains("JMSBridgeDestinations")) && this.bean.isJMSBridgeDestinationsSet() && !var5._isSet(88)) {
               JMSBridgeDestinationMBean[] var73 = this.bean.getJMSBridgeDestinations();
               JMSBridgeDestinationMBean[] var78 = new JMSBridgeDestinationMBean[var73.length];

               for(var8 = 0; var8 < var78.length; ++var8) {
                  var78[var8] = (JMSBridgeDestinationMBean)((JMSBridgeDestinationMBean)this.createCopy((AbstractDescriptorBean)var73[var8], var2));
               }

               var5.setJMSBridgeDestinations(var78);
            }

            if ((var3 == null || !var3.contains("JMSConnectionConsumers")) && this.bean.isJMSConnectionConsumersSet() && !var5._isSet(127)) {
               JMSConnectionConsumerMBean[] var75 = this.bean.getJMSConnectionConsumers();
               JMSConnectionConsumerMBean[] var80 = new JMSConnectionConsumerMBean[var75.length];

               for(var8 = 0; var8 < var80.length; ++var8) {
                  var80[var8] = (JMSConnectionConsumerMBean)((JMSConnectionConsumerMBean)this.createCopy((AbstractDescriptorBean)var75[var8], var2));
               }

               var5.setJMSConnectionConsumers(var80);
            }

            if (var2 && (var3 == null || !var3.contains("JMSConnectionFactories")) && this.bean.isJMSConnectionFactoriesSet() && !var5._isSet(86)) {
               JMSConnectionFactoryMBean[] var77 = this.bean.getJMSConnectionFactories();
               JMSConnectionFactoryMBean[] var82 = new JMSConnectionFactoryMBean[var77.length];

               for(var8 = 0; var8 < var82.length; ++var8) {
                  var82[var8] = (JMSConnectionFactoryMBean)((JMSConnectionFactoryMBean)this.createCopy((AbstractDescriptorBean)var77[var8], var2));
               }

               var5.setJMSConnectionFactories(var82);
            }

            if (var2 && (var3 == null || !var3.contains("JMSDestinationKeys")) && this.bean.isJMSDestinationKeysSet() && !var5._isSet(85)) {
               JMSDestinationKeyMBean[] var79 = this.bean.getJMSDestinationKeys();
               JMSDestinationKeyMBean[] var84 = new JMSDestinationKeyMBean[var79.length];

               for(var8 = 0; var8 < var84.length; ++var8) {
                  var84[var8] = (JMSDestinationKeyMBean)((JMSDestinationKeyMBean)this.createCopy((AbstractDescriptorBean)var79[var8], var2));
               }

               var5.setJMSDestinationKeys(var84);
            }

            if (var2 && (var3 == null || !var3.contains("JMSDestinations")) && this.bean.isJMSDestinationsSet()) {
               var5._unSet(var5, 71);
               var5.setJMSDestinationsAsString(this.bean.getJMSDestinationsAsString());
            }

            if (var2 && (var3 == null || !var3.contains("JMSDistributedQueueMembers")) && this.bean.isJMSDistributedQueueMembersSet() && !var5._isSet(115)) {
               JMSDistributedQueueMemberMBean[] var81 = this.bean.getJMSDistributedQueueMembers();
               JMSDistributedQueueMemberMBean[] var86 = new JMSDistributedQueueMemberMBean[var81.length];

               for(var8 = 0; var8 < var86.length; ++var8) {
                  var86[var8] = (JMSDistributedQueueMemberMBean)((JMSDistributedQueueMemberMBean)this.createCopy((AbstractDescriptorBean)var81[var8], var2));
               }

               var5.setJMSDistributedQueueMembers(var86);
            }

            if (var2 && (var3 == null || !var3.contains("JMSDistributedQueues")) && this.bean.isJMSDistributedQueuesSet() && !var5._isSet(74)) {
               JMSDistributedQueueMBean[] var83 = this.bean.getJMSDistributedQueues();
               JMSDistributedQueueMBean[] var88 = new JMSDistributedQueueMBean[var83.length];

               for(var8 = 0; var8 < var88.length; ++var8) {
                  var88[var8] = (JMSDistributedQueueMBean)((JMSDistributedQueueMBean)this.createCopy((AbstractDescriptorBean)var83[var8], var2));
               }

               var5.setJMSDistributedQueues(var88);
            }

            if (var2 && (var3 == null || !var3.contains("JMSDistributedTopicMembers")) && this.bean.isJMSDistributedTopicMembersSet() && !var5._isSet(116)) {
               JMSDistributedTopicMemberMBean[] var85 = this.bean.getJMSDistributedTopicMembers();
               JMSDistributedTopicMemberMBean[] var90 = new JMSDistributedTopicMemberMBean[var85.length];

               for(var8 = 0; var8 < var90.length; ++var8) {
                  var90[var8] = (JMSDistributedTopicMemberMBean)((JMSDistributedTopicMemberMBean)this.createCopy((AbstractDescriptorBean)var85[var8], var2));
               }

               var5.setJMSDistributedTopicMembers(var90);
            }

            if (var2 && (var3 == null || !var3.contains("JMSDistributedTopics")) && this.bean.isJMSDistributedTopicsSet() && !var5._isSet(75)) {
               JMSDistributedTopicMBean[] var87 = this.bean.getJMSDistributedTopics();
               JMSDistributedTopicMBean[] var92 = new JMSDistributedTopicMBean[var87.length];

               for(var8 = 0; var8 < var92.length; ++var8) {
                  var92[var8] = (JMSDistributedTopicMBean)((JMSDistributedTopicMBean)this.createCopy((AbstractDescriptorBean)var87[var8], var2));
               }

               var5.setJMSDistributedTopics(var92);
            }

            if (var2 && (var3 == null || !var3.contains("JMSFileStores")) && this.bean.isJMSFileStoresSet() && !var5._isSet(70)) {
               JMSFileStoreMBean[] var89 = this.bean.getJMSFileStores();
               JMSFileStoreMBean[] var94 = new JMSFileStoreMBean[var89.length];

               for(var8 = 0; var8 < var94.length; ++var8) {
                  var94[var8] = (JMSFileStoreMBean)((JMSFileStoreMBean)this.createCopy((AbstractDescriptorBean)var89[var8], var2));
               }

               var5.setJMSFileStores(var94);
            }

            if ((var3 == null || !var3.contains("JMSInteropModules")) && this.bean.isJMSInteropModulesSet() && !var5._isSet(100)) {
               JMSInteropModuleMBean[] var91 = this.bean.getJMSInteropModules();
               JMSInteropModuleMBean[] var96 = new JMSInteropModuleMBean[var91.length];

               for(var8 = 0; var8 < var96.length; ++var8) {
                  var96[var8] = (JMSInteropModuleMBean)((JMSInteropModuleMBean)this.createCopy((AbstractDescriptorBean)var91[var8], var2));
               }

               var5.setJMSInteropModules(var96);
            }

            if (var2 && (var3 == null || !var3.contains("JMSJDBCStores")) && this.bean.isJMSJDBCStoresSet() && !var5._isSet(69)) {
               JMSJDBCStoreMBean[] var93 = this.bean.getJMSJDBCStores();
               JMSJDBCStoreMBean[] var98 = new JMSJDBCStoreMBean[var93.length];

               for(var8 = 0; var8 < var98.length; ++var8) {
                  var98[var8] = (JMSJDBCStoreMBean)((JMSJDBCStoreMBean)this.createCopy((AbstractDescriptorBean)var93[var8], var2));
               }

               var5.setJMSJDBCStores(var98);
            }

            if (var2 && (var3 == null || !var3.contains("JMSQueues")) && this.bean.isJMSQueuesSet() && !var5._isSet(72)) {
               JMSQueueMBean[] var95 = this.bean.getJMSQueues();
               JMSQueueMBean[] var100 = new JMSQueueMBean[var95.length];

               for(var8 = 0; var8 < var100.length; ++var8) {
                  var100[var8] = (JMSQueueMBean)((JMSQueueMBean)this.createCopy((AbstractDescriptorBean)var95[var8], var2));
               }

               var5.setJMSQueues(var100);
            }

            if ((var3 == null || !var3.contains("JMSServers")) && this.bean.isJMSServersSet() && !var5._isSet(67)) {
               JMSServerMBean[] var97 = this.bean.getJMSServers();
               JMSServerMBean[] var102 = new JMSServerMBean[var97.length];

               for(var8 = 0; var8 < var102.length; ++var8) {
                  var102[var8] = (JMSServerMBean)((JMSServerMBean)this.createCopy((AbstractDescriptorBean)var97[var8], var2));
               }

               var5.setJMSServers(var102);
            }

            if (var2 && (var3 == null || !var3.contains("JMSSessionPools")) && this.bean.isJMSSessionPoolsSet() && !var5._isSet(87)) {
               JMSSessionPoolMBean[] var99 = this.bean.getJMSSessionPools();
               JMSSessionPoolMBean[] var104 = new JMSSessionPoolMBean[var99.length];

               for(var8 = 0; var8 < var104.length; ++var8) {
                  var104[var8] = (JMSSessionPoolMBean)((JMSSessionPoolMBean)this.createCopy((AbstractDescriptorBean)var99[var8], var2));
               }

               var5.setJMSSessionPools(var104);
            }

            if ((var3 == null || !var3.contains("JMSSystemResources")) && this.bean.isJMSSystemResourcesSet() && !var5._isSet(101)) {
               JMSSystemResourceMBean[] var101 = this.bean.getJMSSystemResources();
               JMSSystemResourceMBean[] var106 = new JMSSystemResourceMBean[var101.length];

               for(var8 = 0; var8 < var106.length; ++var8) {
                  var106[var8] = (JMSSystemResourceMBean)((JMSSystemResourceMBean)this.createCopy((AbstractDescriptorBean)var101[var8], var2));
               }

               var5.setJMSSystemResources(var106);
            }

            if (var2 && (var3 == null || !var3.contains("JMSTemplates")) && this.bean.isJMSTemplatesSet() && !var5._isSet(76)) {
               JMSTemplateMBean[] var103 = this.bean.getJMSTemplates();
               JMSTemplateMBean[] var108 = new JMSTemplateMBean[var103.length];

               for(var8 = 0; var8 < var108.length; ++var8) {
                  var108[var8] = (JMSTemplateMBean)((JMSTemplateMBean)this.createCopy((AbstractDescriptorBean)var103[var8], var2));
               }

               var5.setJMSTemplates(var108);
            }

            if (var2 && (var3 == null || !var3.contains("JMSTopics")) && this.bean.isJMSTopicsSet() && !var5._isSet(73)) {
               JMSTopicMBean[] var105 = this.bean.getJMSTopics();
               JMSTopicMBean[] var110 = new JMSTopicMBean[var105.length];

               for(var8 = 0; var8 < var110.length; ++var8) {
                  var110[var8] = (JMSTopicMBean)((JMSTopicMBean)this.createCopy((AbstractDescriptorBean)var105[var8], var2));
               }

               var5.setJMSTopics(var110);
            }

            if ((var3 == null || !var3.contains("JMX")) && this.bean.isJMXSet() && !var5._isSet(82)) {
               JMXMBean var15 = this.bean.getJMX();
               var5.setJMX((JMXMBean)null);
               var5.setJMX(var15 == null ? null : (JMXMBean)this.createCopy((AbstractDescriptorBean)var15, var2));
            }

            if ((var3 == null || !var3.contains("JPA")) && this.bean.isJPASet() && !var5._isSet(13)) {
               JPAMBean var16 = this.bean.getJPA();
               var5.setJPA((JPAMBean)null);
               var5.setJPA(var16 == null ? null : (JPAMBean)this.createCopy((AbstractDescriptorBean)var16, var2));
            }

            if ((var3 == null || !var3.contains("JTA")) && this.bean.isJTASet() && !var5._isSet(12)) {
               JTAMBean var18 = this.bean.getJTA();
               var5.setJTA((JTAMBean)null);
               var5.setJTA(var18 == null ? null : (JTAMBean)this.createCopy((AbstractDescriptorBean)var18, var2));
            }

            if ((var3 == null || !var3.contains("JoltConnectionPools")) && this.bean.isJoltConnectionPoolsSet() && !var5._isSet(95)) {
               JoltConnectionPoolMBean[] var107 = this.bean.getJoltConnectionPools();
               JoltConnectionPoolMBean[] var112 = new JoltConnectionPoolMBean[var107.length];

               for(var8 = 0; var8 < var112.length; ++var8) {
                  var112[var8] = (JoltConnectionPoolMBean)((JoltConnectionPoolMBean)this.createCopy((AbstractDescriptorBean)var107[var8], var2));
               }

               var5.setJoltConnectionPools(var112);
            }

            if ((var3 == null || !var3.contains("LDAPRealms")) && this.bean.isLDAPRealmsSet() && !var5._isSet(62)) {
               LDAPRealmMBean[] var109 = this.bean.getLDAPRealms();
               LDAPRealmMBean[] var114 = new LDAPRealmMBean[var109.length];

               for(var8 = 0; var8 < var114.length; ++var8) {
                  var114[var8] = (LDAPRealmMBean)((LDAPRealmMBean)this.createCopy((AbstractDescriptorBean)var109[var8], var2));
               }

               var5.setLDAPRealms(var114);
            }

            if ((var3 == null || !var3.contains("LastModificationTime")) && this.bean.isLastModificationTimeSet()) {
            }

            if ((var3 == null || !var3.contains("Libraries")) && this.bean.isLibrariesSet() && !var5._isSet(48)) {
               LibraryMBean[] var111 = this.bean.getLibraries();
               LibraryMBean[] var116 = new LibraryMBean[var111.length];

               for(var8 = 0; var8 < var116.length; ++var8) {
                  var116[var8] = (LibraryMBean)((LibraryMBean)this.createCopy((AbstractDescriptorBean)var111[var8], var2));
               }

               var5.setLibraries(var116);
            }

            if ((var3 == null || !var3.contains("Log")) && this.bean.isLogSet() && !var5._isSet(16)) {
               LogMBean var20 = this.bean.getLog();
               var5.setLog((LogMBean)null);
               var5.setLog(var20 == null ? null : (LogMBean)this.createCopy((AbstractDescriptorBean)var20, var2));
            }

            if ((var3 == null || !var3.contains("LogFilters")) && this.bean.isLogFiltersSet() && !var5._isSet(96)) {
               LogFilterMBean[] var113 = this.bean.getLogFilters();
               LogFilterMBean[] var118 = new LogFilterMBean[var113.length];

               for(var8 = 0; var8 < var118.length; ++var8) {
                  var118[var8] = (LogFilterMBean)((LogFilterMBean)this.createCopy((AbstractDescriptorBean)var113[var8], var2));
               }

               var5.setLogFilters(var118);
            }

            if ((var3 == null || !var3.contains("Machines")) && this.bean.isMachinesSet() && !var5._isSet(53)) {
               MachineMBean[] var115 = this.bean.getMachines();
               MachineMBean[] var120 = new MachineMBean[var115.length];

               for(var8 = 0; var8 < var120.length; ++var8) {
                  var120[var8] = (MachineMBean)((MachineMBean)this.createCopy((AbstractDescriptorBean)var115[var8], var2));
               }

               var5.setMachines(var120);
            }

            if ((var3 == null || !var3.contains("MailSessions")) && this.bean.isMailSessionsSet() && !var5._isSet(94)) {
               MailSessionMBean[] var117 = this.bean.getMailSessions();
               MailSessionMBean[] var122 = new MailSessionMBean[var117.length];

               for(var8 = 0; var8 < var122.length; ++var8) {
                  var122[var8] = (MailSessionMBean)((MailSessionMBean)this.createCopy((AbstractDescriptorBean)var117[var8], var2));
               }

               var5.setMailSessions(var122);
            }

            if ((var3 == null || !var3.contains("MessagingBridges")) && this.bean.isMessagingBridgesSet() && !var5._isSet(33)) {
               MessagingBridgeMBean[] var119 = this.bean.getMessagingBridges();
               MessagingBridgeMBean[] var124 = new MessagingBridgeMBean[var119.length];

               for(var8 = 0; var8 < var124.length; ++var8) {
                  var124[var8] = (MessagingBridgeMBean)((MessagingBridgeMBean)this.createCopy((AbstractDescriptorBean)var119[var8], var2));
               }

               var5.setMessagingBridges(var124);
            }

            if ((var3 == null || !var3.contains("MigratableRMIServices")) && this.bean.isMigratableRMIServicesSet() && !var5._isSet(113)) {
               MigratableRMIServiceMBean[] var121 = this.bean.getMigratableRMIServices();
               MigratableRMIServiceMBean[] var126 = new MigratableRMIServiceMBean[var121.length];

               for(var8 = 0; var8 < var126.length; ++var8) {
                  var126[var8] = (MigratableRMIServiceMBean)((MigratableRMIServiceMBean)this.createCopy((AbstractDescriptorBean)var121[var8], var2));
               }

               var5.setMigratableRMIServices(var126);
            }

            if ((var3 == null || !var3.contains("MigratableTargets")) && this.bean.isMigratableTargetsSet() && !var5._isSet(79)) {
               MigratableTargetMBean[] var123 = this.bean.getMigratableTargets();
               MigratableTargetMBean[] var128 = new MigratableTargetMBean[var123.length];

               for(var8 = 0; var8 < var128.length; ++var8) {
                  var128[var8] = (MigratableTargetMBean)((MigratableTargetMBean)this.createCopy((AbstractDescriptorBean)var123[var8], var2));
               }

               var5.setMigratableTargets(var128);
            }

            if ((var3 == null || !var3.contains("NTRealms")) && this.bean.isNTRealmsSet() && !var5._isSet(63)) {
               NTRealmMBean[] var125 = this.bean.getNTRealms();
               NTRealmMBean[] var130 = new NTRealmMBean[var125.length];

               for(var8 = 0; var8 < var130.length; ++var8) {
                  var130[var8] = (NTRealmMBean)((NTRealmMBean)this.createCopy((AbstractDescriptorBean)var125[var8], var2));
               }

               var5.setNTRealms(var130);
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if (var2 && (var3 == null || !var3.contains("NetworkChannels")) && this.bean.isNetworkChannelsSet() && !var5._isSet(77)) {
               NetworkChannelMBean[] var127 = this.bean.getNetworkChannels();
               NetworkChannelMBean[] var132 = new NetworkChannelMBean[var127.length];

               for(var8 = 0; var8 < var132.length; ++var8) {
                  var132[var8] = (NetworkChannelMBean)((NetworkChannelMBean)this.createCopy((AbstractDescriptorBean)var127[var8], var2));
               }

               var5.setNetworkChannels(var132);
            }

            if ((var3 == null || !var3.contains("PasswordPolicies")) && this.bean.isPasswordPoliciesSet() && !var5._isSet(59)) {
               PasswordPolicyMBean[] var129 = this.bean.getPasswordPolicies();
               PasswordPolicyMBean[] var134 = new PasswordPolicyMBean[var129.length];

               for(var8 = 0; var8 < var134.length; ++var8) {
                  var134[var8] = (PasswordPolicyMBean)((PasswordPolicyMBean)this.createCopy((AbstractDescriptorBean)var129[var8], var2));
               }

               var5.setPasswordPolicies(var134);
            }

            if ((var3 == null || !var3.contains("PathServices")) && this.bean.isPathServicesSet() && !var5._isSet(84)) {
               PathServiceMBean[] var131 = this.bean.getPathServices();
               PathServiceMBean[] var136 = new PathServiceMBean[var131.length];

               for(var8 = 0; var8 < var136.length; ++var8) {
                  var136[var8] = (PathServiceMBean)((PathServiceMBean)this.createCopy((AbstractDescriptorBean)var131[var8], var2));
               }

               var5.setPathServices(var136);
            }

            if ((var3 == null || !var3.contains("RDBMSRealms")) && this.bean.isRDBMSRealmsSet() && !var5._isSet(64)) {
               RDBMSRealmMBean[] var133 = this.bean.getRDBMSRealms();
               RDBMSRealmMBean[] var138 = new RDBMSRealmMBean[var133.length];

               for(var8 = 0; var8 < var138.length; ++var8) {
                  var138[var8] = (RDBMSRealmMBean)((RDBMSRealmMBean)this.createCopy((AbstractDescriptorBean)var133[var8], var2));
               }

               var5.setRDBMSRealms(var138);
            }

            if ((var3 == null || !var3.contains("Realms")) && this.bean.isRealmsSet() && !var5._isSet(58)) {
               RealmMBean[] var135 = this.bean.getRealms();
               RealmMBean[] var140 = new RealmMBean[var135.length];

               for(var8 = 0; var8 < var140.length; ++var8) {
                  var140[var8] = (RealmMBean)((RealmMBean)this.createCopy((AbstractDescriptorBean)var135[var8], var2));
               }

               var5.setRealms(var140);
            }

            if ((var3 == null || !var3.contains("RemoteSAFContexts")) && this.bean.isRemoteSAFContextsSet() && !var5._isSet(112)) {
               RemoteSAFContextMBean[] var137 = this.bean.getRemoteSAFContexts();
               RemoteSAFContextMBean[] var142 = new RemoteSAFContextMBean[var137.length];

               for(var8 = 0; var8 < var142.length; ++var8) {
                  var142[var8] = (RemoteSAFContextMBean)((RemoteSAFContextMBean)this.createCopy((AbstractDescriptorBean)var137[var8], var2));
               }

               var5.setRemoteSAFContexts(var142);
            }

            if ((var3 == null || !var3.contains("RestfulManagementServices")) && this.bean.isRestfulManagementServicesSet() && !var5._isSet(135)) {
               RestfulManagementServicesMBean var22 = this.bean.getRestfulManagementServices();
               var5.setRestfulManagementServices((RestfulManagementServicesMBean)null);
               var5.setRestfulManagementServices(var22 == null ? null : (RestfulManagementServicesMBean)this.createCopy((AbstractDescriptorBean)var22, var2));
            }

            if ((var3 == null || !var3.contains("RootDirectory")) && this.bean.isRootDirectorySet()) {
            }

            if ((var3 == null || !var3.contains("SAFAgents")) && this.bean.isSAFAgentsSet() && !var5._isSet(109)) {
               SAFAgentMBean[] var139 = this.bean.getSAFAgents();
               SAFAgentMBean[] var144 = new SAFAgentMBean[var139.length];

               for(var8 = 0; var8 < var144.length; ++var8) {
                  var144[var8] = (SAFAgentMBean)((SAFAgentMBean)this.createCopy((AbstractDescriptorBean)var139[var8], var2));
               }

               var5.setSAFAgents(var144);
            }

            if ((var3 == null || !var3.contains("SNMPAgent")) && this.bean.isSNMPAgentSet() && !var5._isSet(17)) {
               SNMPAgentMBean var25 = this.bean.getSNMPAgent();
               var5.setSNMPAgent((SNMPAgentMBean)null);
               var5.setSNMPAgent(var25 == null ? null : (SNMPAgentMBean)this.createCopy((AbstractDescriptorBean)var25, var2));
            }

            if ((var3 == null || !var3.contains("SNMPAgentDeployments")) && this.bean.isSNMPAgentDeploymentsSet() && !var5._isSet(18)) {
               SNMPAgentDeploymentMBean[] var141 = this.bean.getSNMPAgentDeployments();
               SNMPAgentDeploymentMBean[] var146 = new SNMPAgentDeploymentMBean[var141.length];

               for(var8 = 0; var8 < var146.length; ++var8) {
                  var146[var8] = (SNMPAgentDeploymentMBean)((SNMPAgentDeploymentMBean)this.createCopy((AbstractDescriptorBean)var141[var8], var2));
               }

               var5.setSNMPAgentDeployments(var146);
            }

            if (var2 && (var3 == null || !var3.contains("SNMPAttributeChanges")) && this.bean.isSNMPAttributeChangesSet() && !var5._isSet(123)) {
               SNMPAttributeChangeMBean[] var143 = this.bean.getSNMPAttributeChanges();
               SNMPAttributeChangeMBean[] var148 = new SNMPAttributeChangeMBean[var143.length];

               for(var8 = 0; var8 < var148.length; ++var8) {
                  var148[var8] = (SNMPAttributeChangeMBean)((SNMPAttributeChangeMBean)this.createCopy((AbstractDescriptorBean)var143[var8], var2));
               }

               var5.setSNMPAttributeChanges(var148);
            }

            if (var2 && (var3 == null || !var3.contains("SNMPCounterMonitors")) && this.bean.isSNMPCounterMonitorsSet() && !var5._isSet(121)) {
               SNMPCounterMonitorMBean[] var145 = this.bean.getSNMPCounterMonitors();
               SNMPCounterMonitorMBean[] var150 = new SNMPCounterMonitorMBean[var145.length];

               for(var8 = 0; var8 < var150.length; ++var8) {
                  var150[var8] = (SNMPCounterMonitorMBean)((SNMPCounterMonitorMBean)this.createCopy((AbstractDescriptorBean)var145[var8], var2));
               }

               var5.setSNMPCounterMonitors(var150);
            }

            if (var2 && (var3 == null || !var3.contains("SNMPGaugeMonitors")) && this.bean.isSNMPGaugeMonitorsSet() && !var5._isSet(119)) {
               SNMPGaugeMonitorMBean[] var147 = this.bean.getSNMPGaugeMonitors();
               SNMPGaugeMonitorMBean[] var152 = new SNMPGaugeMonitorMBean[var147.length];

               for(var8 = 0; var8 < var152.length; ++var8) {
                  var152[var8] = (SNMPGaugeMonitorMBean)((SNMPGaugeMonitorMBean)this.createCopy((AbstractDescriptorBean)var147[var8], var2));
               }

               var5.setSNMPGaugeMonitors(var152);
            }

            if (var2 && (var3 == null || !var3.contains("SNMPLogFilters")) && this.bean.isSNMPLogFiltersSet() && !var5._isSet(122)) {
               SNMPLogFilterMBean[] var149 = this.bean.getSNMPLogFilters();
               SNMPLogFilterMBean[] var154 = new SNMPLogFilterMBean[var149.length];

               for(var8 = 0; var8 < var154.length; ++var8) {
                  var154[var8] = (SNMPLogFilterMBean)((SNMPLogFilterMBean)this.createCopy((AbstractDescriptorBean)var149[var8], var2));
               }

               var5.setSNMPLogFilters(var154);
            }

            if (var2 && (var3 == null || !var3.contains("SNMPProxies")) && this.bean.isSNMPProxiesSet() && !var5._isSet(118)) {
               SNMPProxyMBean[] var151 = this.bean.getSNMPProxies();
               SNMPProxyMBean[] var156 = new SNMPProxyMBean[var151.length];

               for(var8 = 0; var8 < var156.length; ++var8) {
                  var156[var8] = (SNMPProxyMBean)((SNMPProxyMBean)this.createCopy((AbstractDescriptorBean)var151[var8], var2));
               }

               var5.setSNMPProxies(var156);
            }

            if (var2 && (var3 == null || !var3.contains("SNMPStringMonitors")) && this.bean.isSNMPStringMonitorsSet() && !var5._isSet(120)) {
               SNMPStringMonitorMBean[] var153 = this.bean.getSNMPStringMonitors();
               SNMPStringMonitorMBean[] var158 = new SNMPStringMonitorMBean[var153.length];

               for(var8 = 0; var8 < var158.length; ++var8) {
                  var158[var8] = (SNMPStringMonitorMBean)((SNMPStringMonitorMBean)this.createCopy((AbstractDescriptorBean)var153[var8], var2));
               }

               var5.setSNMPStringMonitors(var158);
            }

            if (var2 && (var3 == null || !var3.contains("SNMPTrapDestinations")) && this.bean.isSNMPTrapDestinationsSet() && !var5._isSet(117)) {
               SNMPTrapDestinationMBean[] var155 = this.bean.getSNMPTrapDestinations();
               SNMPTrapDestinationMBean[] var160 = new SNMPTrapDestinationMBean[var155.length];

               for(var8 = 0; var8 < var160.length; ++var8) {
                  var160[var8] = (SNMPTrapDestinationMBean)((SNMPTrapDestinationMBean)this.createCopy((AbstractDescriptorBean)var155[var8], var2));
               }

               var5.setSNMPTrapDestinations(var160);
            }

            if ((var3 == null || !var3.contains("Security")) && this.bean.isSecuritySet() && !var5._isSet(11)) {
               SecurityMBean var28 = this.bean.getSecurity();
               var5.setSecurity((SecurityMBean)null);
               var5.setSecurity(var28 == null ? null : (SecurityMBean)this.createCopy((AbstractDescriptorBean)var28, var2));
            }

            if ((var3 == null || !var3.contains("SecurityConfiguration")) && this.bean.isSecurityConfigurationSet() && !var5._isSet(10)) {
               SecurityConfigurationMBean var31 = this.bean.getSecurityConfiguration();
               var5.setSecurityConfiguration((SecurityConfigurationMBean)null);
               var5.setSecurityConfiguration(var31 == null ? null : (SecurityConfigurationMBean)this.createCopy((AbstractDescriptorBean)var31, var2));
            }

            if ((var3 == null || !var3.contains("SelfTuning")) && this.bean.isSelfTuningSet() && !var5._isSet(83)) {
               SelfTuningMBean var34 = this.bean.getSelfTuning();
               var5.setSelfTuning((SelfTuningMBean)null);
               var5.setSelfTuning(var34 == null ? null : (SelfTuningMBean)this.createCopy((AbstractDescriptorBean)var34, var2));
            }

            if ((var3 == null || !var3.contains("Servers")) && this.bean.isServersSet() && !var5._isSet(24)) {
               ServerMBean[] var157 = this.bean.getServers();
               ServerMBean[] var162 = new ServerMBean[var157.length];

               for(var8 = 0; var8 < var162.length; ++var8) {
                  var162[var8] = (ServerMBean)((ServerMBean)this.createCopy((AbstractDescriptorBean)var157[var8], var2));
               }

               var5.setServers(var162);
            }

            if ((var3 == null || !var3.contains("ShutdownClasses")) && this.bean.isShutdownClassesSet() && !var5._isSet(91)) {
               ShutdownClassMBean[] var159 = this.bean.getShutdownClasses();
               ShutdownClassMBean[] var164 = new ShutdownClassMBean[var159.length];

               for(var8 = 0; var8 < var164.length; ++var8) {
                  var164[var8] = (ShutdownClassMBean)((ShutdownClassMBean)this.createCopy((AbstractDescriptorBean)var159[var8], var2));
               }

               var5.setShutdownClasses(var164);
            }

            if ((var3 == null || !var3.contains("SingletonServices")) && this.bean.isSingletonServicesSet() && !var5._isSet(93)) {
               SingletonServiceMBean[] var161 = this.bean.getSingletonServices();
               SingletonServiceMBean[] var166 = new SingletonServiceMBean[var161.length];

               for(var8 = 0; var8 < var166.length; ++var8) {
                  var166[var8] = (SingletonServiceMBean)((SingletonServiceMBean)this.createCopy((AbstractDescriptorBean)var161[var8], var2));
               }

               var5.setSingletonServices(var166);
            }

            if ((var3 == null || !var3.contains("StartupClasses")) && this.bean.isStartupClassesSet() && !var5._isSet(92)) {
               StartupClassMBean[] var163 = this.bean.getStartupClasses();
               StartupClassMBean[] var168 = new StartupClassMBean[var163.length];

               for(var8 = 0; var8 < var168.length; ++var8) {
                  var168[var8] = (StartupClassMBean)((StartupClassMBean)this.createCopy((AbstractDescriptorBean)var163[var8], var2));
               }

               var5.setStartupClasses(var168);
            }

            if ((var3 == null || !var3.contains("UnixRealms")) && this.bean.isUnixRealmsSet() && !var5._isSet(65)) {
               UnixRealmMBean[] var165 = this.bean.getUnixRealms();
               UnixRealmMBean[] var170 = new UnixRealmMBean[var165.length];

               for(var8 = 0; var8 < var170.length; ++var8) {
                  var170[var8] = (UnixRealmMBean)((UnixRealmMBean)this.createCopy((AbstractDescriptorBean)var165[var8], var2));
               }

               var5.setUnixRealms(var170);
            }

            if ((var3 == null || !var3.contains("VirtualHosts")) && this.bean.isVirtualHostsSet() && !var5._isSet(78)) {
               VirtualHostMBean[] var167 = this.bean.getVirtualHosts();
               VirtualHostMBean[] var172 = new VirtualHostMBean[var167.length];

               for(var8 = 0; var8 < var172.length; ++var8) {
                  var172[var8] = (VirtualHostMBean)((VirtualHostMBean)this.createCopy((AbstractDescriptorBean)var167[var8], var2));
               }

               var5.setVirtualHosts(var172);
            }

            if ((var3 == null || !var3.contains("WLDFSystemResources")) && this.bean.isWLDFSystemResourcesSet() && !var5._isSet(106)) {
               WLDFSystemResourceMBean[] var169 = this.bean.getWLDFSystemResources();
               WLDFSystemResourceMBean[] var174 = new WLDFSystemResourceMBean[var169.length];

               for(var8 = 0; var8 < var174.length; ++var8) {
                  var174[var8] = (WLDFSystemResourceMBean)((WLDFSystemResourceMBean)this.createCopy((AbstractDescriptorBean)var169[var8], var2));
               }

               var5.setWLDFSystemResources(var174);
            }

            if ((var3 == null || !var3.contains("WLECConnectionPools")) && this.bean.isWLECConnectionPoolsSet() && !var5._isSet(110)) {
               WLECConnectionPoolMBean[] var171 = this.bean.getWLECConnectionPools();
               WLECConnectionPoolMBean[] var176 = new WLECConnectionPoolMBean[var171.length];

               for(var8 = 0; var8 < var176.length; ++var8) {
                  var176[var8] = (WLECConnectionPoolMBean)((WLECConnectionPoolMBean)this.createCopy((AbstractDescriptorBean)var171[var8], var2));
               }

               var5.setWLECConnectionPools(var176);
            }

            if ((var3 == null || !var3.contains("WSReliableDeliveryPolicies")) && this.bean.isWSReliableDeliveryPoliciesSet() && !var5._isSet(51)) {
               WSReliableDeliveryPolicyMBean[] var173 = this.bean.getWSReliableDeliveryPolicies();
               WSReliableDeliveryPolicyMBean[] var178 = new WSReliableDeliveryPolicyMBean[var173.length];

               for(var8 = 0; var8 < var178.length; ++var8) {
                  var178[var8] = (WSReliableDeliveryPolicyMBean)((WSReliableDeliveryPolicyMBean)this.createCopy((AbstractDescriptorBean)var173[var8], var2));
               }

               var5.setWSReliableDeliveryPolicies(var178);
            }

            if ((var3 == null || !var3.contains("WTCServers")) && this.bean.isWTCServersSet() && !var5._isSet(15)) {
               WTCServerMBean[] var175 = this.bean.getWTCServers();
               WTCServerMBean[] var180 = new WTCServerMBean[var175.length];

               for(var8 = 0; var8 < var180.length; ++var8) {
                  var180[var8] = (WTCServerMBean)((WTCServerMBean)this.createCopy((AbstractDescriptorBean)var175[var8], var2));
               }

               var5.setWTCServers(var180);
            }

            if ((var3 == null || !var3.contains("WebAppContainer")) && this.bean.isWebAppContainerSet() && !var5._isSet(81)) {
               WebAppContainerMBean var37 = this.bean.getWebAppContainer();
               var5.setWebAppContainer((WebAppContainerMBean)null);
               var5.setWebAppContainer(var37 == null ? null : (WebAppContainerMBean)this.createCopy((AbstractDescriptorBean)var37, var2));
            }

            if ((var3 == null || !var3.contains("WebserviceSecurities")) && this.bean.isWebserviceSecuritiesSet() && !var5._isSet(124)) {
               WebserviceSecurityMBean[] var177 = this.bean.getWebserviceSecurities();
               WebserviceSecurityMBean[] var182 = new WebserviceSecurityMBean[var177.length];

               for(var8 = 0; var8 < var182.length; ++var8) {
                  var182[var8] = (WebserviceSecurityMBean)((WebserviceSecurityMBean)this.createCopy((AbstractDescriptorBean)var177[var8], var2));
               }

               var5.setWebserviceSecurities(var182);
            }

            if ((var3 == null || !var3.contains("XMLEntityCaches")) && this.bean.isXMLEntityCachesSet() && !var5._isSet(54)) {
               XMLEntityCacheMBean[] var179 = this.bean.getXMLEntityCaches();
               XMLEntityCacheMBean[] var183 = new XMLEntityCacheMBean[var179.length];

               for(var8 = 0; var8 < var183.length; ++var8) {
                  var183[var8] = (XMLEntityCacheMBean)((XMLEntityCacheMBean)this.createCopy((AbstractDescriptorBean)var179[var8], var2));
               }

               var5.setXMLEntityCaches(var183);
            }

            if ((var3 == null || !var3.contains("XMLRegistries")) && this.bean.isXMLRegistriesSet() && !var5._isSet(55)) {
               XMLRegistryMBean[] var181 = this.bean.getXMLRegistries();
               XMLRegistryMBean[] var184 = new XMLRegistryMBean[var181.length];

               for(var8 = 0; var8 < var184.length; ++var8) {
                  var184[var8] = (XMLRegistryMBean)((XMLRegistryMBean)this.createCopy((AbstractDescriptorBean)var181[var8], var2));
               }

               var5.setXMLRegistries(var184);
            }

            if ((var3 == null || !var3.contains("Active")) && this.bean.isActiveSet()) {
            }

            if ((var3 == null || !var3.contains("AdministrationMBeanAuditingEnabled")) && this.bean.isAdministrationMBeanAuditingEnabledSet()) {
               var5.setAdministrationMBeanAuditingEnabled(this.bean.isAdministrationMBeanAuditingEnabled());
            }

            if ((var3 == null || !var3.contains("AdministrationPortEnabled")) && this.bean.isAdministrationPortEnabledSet()) {
               var5.setAdministrationPortEnabled(this.bean.isAdministrationPortEnabled());
            }

            if (var2 && (var3 == null || !var3.contains("AutoConfigurationSaveEnabled")) && this.bean.isAutoConfigurationSaveEnabledSet()) {
               var5.setAutoConfigurationSaveEnabled(this.bean.isAutoConfigurationSaveEnabled());
            }

            if ((var3 == null || !var3.contains("AutoDeployForSubmodulesEnabled")) && this.bean.isAutoDeployForSubmodulesEnabledSet()) {
               var5.setAutoDeployForSubmodulesEnabled(this.bean.isAutoDeployForSubmodulesEnabled());
            }

            if ((var3 == null || !var3.contains("ClusterConstraintsEnabled")) && this.bean.isClusterConstraintsEnabledSet()) {
               var5.setClusterConstraintsEnabled(this.bean.isClusterConstraintsEnabled());
            }

            if ((var3 == null || !var3.contains("ConfigBackupEnabled")) && this.bean.isConfigBackupEnabledSet()) {
               var5.setConfigBackupEnabled(this.bean.isConfigBackupEnabled());
            }

            if ((var3 == null || !var3.contains("ConsoleEnabled")) && this.bean.isConsoleEnabledSet()) {
               var5.setConsoleEnabled(this.bean.isConsoleEnabled());
            }

            if ((var3 == null || !var3.contains("ExalogicOptimizationsEnabled")) && this.bean.isExalogicOptimizationsEnabledSet()) {
               var5.setExalogicOptimizationsEnabled(this.bean.isExalogicOptimizationsEnabled());
            }

            if ((var3 == null || !var3.contains("GuardianEnabled")) && this.bean.isGuardianEnabledSet()) {
               var5.setGuardianEnabled(this.bean.isGuardianEnabled());
            }

            if ((var3 == null || !var3.contains("InternalAppsDeployOnDemandEnabled")) && this.bean.isInternalAppsDeployOnDemandEnabledSet()) {
               var5.setInternalAppsDeployOnDemandEnabled(this.bean.isInternalAppsDeployOnDemandEnabled());
            }

            if ((var3 == null || !var3.contains("MsgIdPrefixCompatibilityEnabled")) && this.bean.isMsgIdPrefixCompatibilityEnabledSet()) {
               var5.setMsgIdPrefixCompatibilityEnabled(this.bean.isMsgIdPrefixCompatibilityEnabled());
            }

            if ((var3 == null || !var3.contains("OCMEnabled")) && this.bean.isOCMEnabledSet()) {
               var5.setOCMEnabled(this.bean.isOCMEnabled());
            }

            if ((var3 == null || !var3.contains("ProductionModeEnabled")) && this.bean.isProductionModeEnabledSet()) {
               var5.setProductionModeEnabled(this.bean.isProductionModeEnabled());
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
         this.inferSubTree(this.bean.getAdminConsole(), var1, var2);
         this.inferSubTree(this.bean.getAdminServerMBean(), var1, var2);
         this.inferSubTree(this.bean.getAppDeployments(), var1, var2);
         this.inferSubTree(this.bean.getApplications(), var1, var2);
         this.inferSubTree(this.bean.getBasicRealms(), var1, var2);
         this.inferSubTree(this.bean.getBridgeDestinations(), var1, var2);
         this.inferSubTree(this.bean.getCachingRealms(), var1, var2);
         this.inferSubTree(this.bean.getClusters(), var1, var2);
         this.inferSubTree(this.bean.getCoherenceClusterSystemResources(), var1, var2);
         this.inferSubTree(this.bean.getCoherenceServers(), var1, var2);
         this.inferSubTree(this.bean.getCustomRealms(), var1, var2);
         this.inferSubTree(this.bean.getCustomResources(), var1, var2);
         this.inferSubTree(this.bean.getDeploymentConfiguration(), var1, var2);
         this.inferSubTree(this.bean.getDeployments(), var1, var2);
         this.inferSubTree(this.bean.getDomainLibraries(), var1, var2);
         this.inferSubTree(this.bean.getDomainLogFilters(), var1, var2);
         this.inferSubTree(this.bean.getEJBContainer(), var1, var2);
         this.inferSubTree(this.bean.getEmbeddedLDAP(), var1, var2);
         this.inferSubTree(this.bean.getErrorHandlings(), var1, var2);
         this.inferSubTree(this.bean.getFileRealms(), var1, var2);
         this.inferSubTree(this.bean.getFileStores(), var1, var2);
         this.inferSubTree(this.bean.getFileT3s(), var1, var2);
         this.inferSubTree(this.bean.getForeignJMSConnectionFactories(), var1, var2);
         this.inferSubTree(this.bean.getForeignJMSDestinations(), var1, var2);
         this.inferSubTree(this.bean.getForeignJMSServers(), var1, var2);
         this.inferSubTree(this.bean.getForeignJNDIProviders(), var1, var2);
         this.inferSubTree(this.bean.getInternalAppDeployments(), var1, var2);
         this.inferSubTree(this.bean.getInternalLibraries(), var1, var2);
         this.inferSubTree(this.bean.getJDBCConnectionPools(), var1, var2);
         this.inferSubTree(this.bean.getJDBCDataSourceFactories(), var1, var2);
         this.inferSubTree(this.bean.getJDBCDataSources(), var1, var2);
         this.inferSubTree(this.bean.getJDBCMultiPools(), var1, var2);
         this.inferSubTree(this.bean.getJDBCStores(), var1, var2);
         this.inferSubTree(this.bean.getJDBCSystemResources(), var1, var2);
         this.inferSubTree(this.bean.getJDBCTxDataSources(), var1, var2);
         this.inferSubTree(this.bean.getJMSBridgeDestinations(), var1, var2);
         this.inferSubTree(this.bean.getJMSConnectionConsumers(), var1, var2);
         this.inferSubTree(this.bean.getJMSConnectionFactories(), var1, var2);
         this.inferSubTree(this.bean.getJMSDestinationKeys(), var1, var2);
         this.inferSubTree(this.bean.getJMSDestinations(), var1, var2);
         this.inferSubTree(this.bean.getJMSDistributedQueueMembers(), var1, var2);
         this.inferSubTree(this.bean.getJMSDistributedQueues(), var1, var2);
         this.inferSubTree(this.bean.getJMSDistributedTopicMembers(), var1, var2);
         this.inferSubTree(this.bean.getJMSDistributedTopics(), var1, var2);
         this.inferSubTree(this.bean.getJMSFileStores(), var1, var2);
         this.inferSubTree(this.bean.getJMSInteropModules(), var1, var2);
         this.inferSubTree(this.bean.getJMSJDBCStores(), var1, var2);
         this.inferSubTree(this.bean.getJMSQueues(), var1, var2);
         this.inferSubTree(this.bean.getJMSServers(), var1, var2);
         this.inferSubTree(this.bean.getJMSSessionPools(), var1, var2);
         this.inferSubTree(this.bean.getJMSStores(), var1, var2);
         this.inferSubTree(this.bean.getJMSSystemResources(), var1, var2);
         this.inferSubTree(this.bean.getJMSTemplates(), var1, var2);
         this.inferSubTree(this.bean.getJMSTopics(), var1, var2);
         this.inferSubTree(this.bean.getJMX(), var1, var2);
         this.inferSubTree(this.bean.getJPA(), var1, var2);
         this.inferSubTree(this.bean.getJTA(), var1, var2);
         this.inferSubTree(this.bean.getJoltConnectionPools(), var1, var2);
         this.inferSubTree(this.bean.getLDAPRealms(), var1, var2);
         this.inferSubTree(this.bean.getLibraries(), var1, var2);
         this.inferSubTree(this.bean.getLog(), var1, var2);
         this.inferSubTree(this.bean.getLogFilters(), var1, var2);
         this.inferSubTree(this.bean.getMachines(), var1, var2);
         this.inferSubTree(this.bean.getMailSessions(), var1, var2);
         this.inferSubTree(this.bean.getMessagingBridges(), var1, var2);
         this.inferSubTree(this.bean.getMigratableRMIServices(), var1, var2);
         this.inferSubTree(this.bean.getMigratableTargets(), var1, var2);
         this.inferSubTree(this.bean.getNTRealms(), var1, var2);
         this.inferSubTree(this.bean.getNetworkChannels(), var1, var2);
         this.inferSubTree(this.bean.getPasswordPolicies(), var1, var2);
         this.inferSubTree(this.bean.getPathServices(), var1, var2);
         this.inferSubTree(this.bean.getRDBMSRealms(), var1, var2);
         this.inferSubTree(this.bean.getRealms(), var1, var2);
         this.inferSubTree(this.bean.getRemoteSAFContexts(), var1, var2);
         this.inferSubTree(this.bean.getRestfulManagementServices(), var1, var2);
         this.inferSubTree(this.bean.getSAFAgents(), var1, var2);
         this.inferSubTree(this.bean.getSNMPAgent(), var1, var2);
         this.inferSubTree(this.bean.getSNMPAgentDeployments(), var1, var2);
         this.inferSubTree(this.bean.getSNMPAttributeChanges(), var1, var2);
         this.inferSubTree(this.bean.getSNMPCounterMonitors(), var1, var2);
         this.inferSubTree(this.bean.getSNMPGaugeMonitors(), var1, var2);
         this.inferSubTree(this.bean.getSNMPLogFilters(), var1, var2);
         this.inferSubTree(this.bean.getSNMPProxies(), var1, var2);
         this.inferSubTree(this.bean.getSNMPStringMonitors(), var1, var2);
         this.inferSubTree(this.bean.getSNMPTrapDestinations(), var1, var2);
         this.inferSubTree(this.bean.getSecurity(), var1, var2);
         this.inferSubTree(this.bean.getSecurityConfiguration(), var1, var2);
         this.inferSubTree(this.bean.getSelfTuning(), var1, var2);
         this.inferSubTree(this.bean.getServers(), var1, var2);
         this.inferSubTree(this.bean.getShutdownClasses(), var1, var2);
         this.inferSubTree(this.bean.getSingletonServices(), var1, var2);
         this.inferSubTree(this.bean.getStartupClasses(), var1, var2);
         this.inferSubTree(this.bean.getSystemResources(), var1, var2);
         this.inferSubTree(this.bean.getTargets(), var1, var2);
         this.inferSubTree(this.bean.getUnixRealms(), var1, var2);
         this.inferSubTree(this.bean.getVirtualHosts(), var1, var2);
         this.inferSubTree(this.bean.getWLDFSystemResources(), var1, var2);
         this.inferSubTree(this.bean.getWLECConnectionPools(), var1, var2);
         this.inferSubTree(this.bean.getWSReliableDeliveryPolicies(), var1, var2);
         this.inferSubTree(this.bean.getWTCServers(), var1, var2);
         this.inferSubTree(this.bean.getWebAppContainer(), var1, var2);
         this.inferSubTree(this.bean.getWebserviceSecurities(), var1, var2);
         this.inferSubTree(this.bean.getXMLEntityCaches(), var1, var2);
         this.inferSubTree(this.bean.getXMLRegistries(), var1, var2);
      }
   }
}
