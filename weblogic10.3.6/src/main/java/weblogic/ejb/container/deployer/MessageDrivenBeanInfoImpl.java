package weblogic.ejb.container.deployer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import weblogic.application.ApplicationContextInternal;
import weblogic.cluster.migration.Migratable;
import weblogic.cluster.migration.MigrationManager;
import weblogic.deployment.BaseEnvironmentBuilder;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateFailedException;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.descriptor.DescriptorBean;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.compliance.ComplianceException;
import weblogic.ejb.container.compliance.EJBComplianceTextFormatter;
import weblogic.ejb.container.compliance.Ejb30MessageDrivenBeanClassChecker;
import weblogic.ejb.container.dd.DDConstants;
import weblogic.ejb.container.dd.DDDefaults;
import weblogic.ejb.container.deployer.mbimpl.MethodInfoImpl;
import weblogic.ejb.container.interfaces.BaseEJBHomeIntf;
import weblogic.ejb.container.interfaces.BaseEJBLocalHomeIntf;
import weblogic.ejb.container.interfaces.BaseEJBRemoteHomeIntf;
import weblogic.ejb.container.interfaces.CachingDescriptor;
import weblogic.ejb.container.interfaces.DeploymentInfo;
import weblogic.ejb.container.interfaces.MessageDrivenBeanInfo;
import weblogic.ejb.container.interfaces.MessageDrivenManagerIntf;
import weblogic.ejb.container.interfaces.MethodInfo;
import weblogic.ejb.container.interfaces.PoolIntf;
import weblogic.ejb.container.interfaces.TimerManager;
import weblogic.ejb.container.internal.EJBComponentRuntimeMBeanImpl;
import weblogic.ejb.container.internal.EJBRuntimeUtils;
import weblogic.ejb.container.internal.JMSConnectionPoller;
import weblogic.ejb.container.internal.MethodDescriptor;
import weblogic.ejb.container.internal.SecurityHelper;
import weblogic.ejb.container.manager.MessageDrivenManager;
import weblogic.ejb.container.timer.MDBTimerManagerFactory;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.j2ee.J2EEUtils;
import weblogic.j2ee.descriptor.ActivationConfigBean;
import weblogic.j2ee.descriptor.ActivationConfigPropertyBean;
import weblogic.j2ee.descriptor.MessageDrivenBeanBean;
import weblogic.j2ee.descriptor.wl.MessageDestinationDescriptorBean;
import weblogic.j2ee.descriptor.wl.MessageDrivenDescriptorBean;
import weblogic.j2ee.descriptor.wl.PoolBean;
import weblogic.j2ee.descriptor.wl.SecurityPluginBean;
import weblogic.j2ee.descriptor.wl.TransactionDescriptorBean;
import weblogic.j2ee.descriptor.wl.WeblogicEnterpriseBeanBean;
import weblogic.jms.common.CrossDomainSecurityManager;
import weblogic.jms.extensions.DestinationAvailabilityListener;
import weblogic.jms.extensions.DestinationDetail;
import weblogic.jms.extensions.JMSDestinationAvailabilityHelper;
import weblogic.jms.extensions.RegistrationHandle;
import weblogic.kernel.Kernel;
import weblogic.logging.Loggable;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.subject.AbstractSubject;
import weblogic.utils.AssertionError;
import weblogic.utils.StackTraceUtils;
import weblogic.utils.annotation.BeaSynthetic.Helper;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.collections.CombinedIterator;
import weblogic.utils.reflect.MethodText;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

final class MessageDrivenBeanInfoImpl extends BeanInfoImpl implements MessageDrivenBeanInfo {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private String connectionFactoryResourceLink = null;
   private String destinationResourceLink = null;
   private String messageDestinationLink = null;
   private boolean m_usesBeanManagedTx = false;
   private String m_messageSelector = null;
   private int m_acknowledgeMode = 1;
   private boolean m_isDestinationQueue = false;
   private boolean m_isDestinationTopic = false;
   private boolean m_isDurableSubscriber = false;
   private boolean m_isInactive = false;
   private int m_maxConcurrentInstances;
   private CachingDescriptor m_cachingDescriptor = null;
   private String m_destinationJNDIName = null;
   private String m_initialContextFactory;
   private String m_providerURL;
   private String m_connectionFactoryJNDIName;
   private int jmsPollingIntervalSeconds;
   private int initSuspendSeconds;
   private int maxSuspendSeconds;
   private MDBTimerManagerFactory mdbTimerManagerFactory;
   private int distributedDestinationConnection = -1;
   private SecurityPluginBean securityPlugin;
   private boolean isGenerateUniqueJmsClientId;
   private ClusterMBean clusterMBean = null;
   private String currServerName = "";
   private String currClusterName = "";
   private String domainName = "";
   private String jmsClientIDBase;
   private String jmsClientID;
   private boolean isDurableSubscriptionDeletion;
   private int maxMessagesInTransaction;
   private boolean use81StylePolling;
   private boolean minimizeAQSessions = Boolean.getBoolean("weblogic.mdb.message.MinimizeAQSessions");
   private String resourceAdapterJndiName = "";
   private final boolean callByReference;
   private boolean m_noLocal = false;
   private MethodInfo m_onMessageMethodInfo;
   private String messagingTypeInterfaceName;
   private String messageDrivenLocalObjectClassName;
   private Class messagingTypeInterfaceClass;
   private Class messageDrivenLocalObjectClass;
   private final Map messagingTypeMethods = new HashMap();
   private Map mdbMethodDescriptors = new HashMap();
   private boolean isResourceAdapter;
   private ActivationConfigBean activationConfigBean;
   private Context environmentContext;
   private AtomicInteger contextBoundCounter = new AtomicInteger(0);
   private List<MessageDrivenManagerIntf> mdManagerList = null;
   private List<DestinationDetail> backupDestMembersList = null;
   private boolean oneMDBDeployedForDT = false;
   private EJBComponentRuntimeMBeanImpl ejbComponentRuntime;
   private HashMap pseudoMDMBeans = new HashMap();
   private AbstractSubject subject = null;
   private boolean indirectlyImplMessageListener = false;
   private String generatedBeanClassName = null;
   private Class generatedBeanClass;
   private StringBuffer userName = null;
   private StringBuffer password = null;
   private boolean hasCredentials = false;
   private boolean isRemoteSubjectExists = false;
   private Map<String, String> activationMap = null;
   private Hashtable<String, String> jmsRegisterEnv = new Hashtable();
   private WorkManager wm;
   private AtomicReference<MessageDrivenManagerIntf> defaultMDManager = new AtomicReference();
   private AtomicReference<DestinationEventHandler> destinationHandler = new AtomicReference();
   private int topicMessagesDistributionMode = 0;

   public MessageDrivenBeanInfoImpl(DeploymentInfo var1, CompositeMBeanDescriptor var2, GenericClassLoader var3) throws ClassNotFoundException, WLDeploymentException {
      super(var1, var2, var3);
      MessageDrivenBeanBean var4 = (MessageDrivenBeanBean)var2.getBean();
      WeblogicEnterpriseBeanBean var5 = var2.getWl60Bean();
      MessageDrivenDescriptorBean var6 = var5.getMessageDrivenDescriptor();
      this.messagingTypeInterfaceName = var2.getMessagingTypeName();
      if (this.messagingTypeInterfaceName == null) {
         this.messagingTypeInterfaceName = "javax.jms.MessageListener";
      }

      this.m_usesBeanManagedTx = "bean".equalsIgnoreCase(var4.getTransactionType());
      if (var4.getMessageDestinationType() != null) {
         this.setDestination(var4.getMessageDestinationType());
      }

      this.activationConfigBean = var4.getActivationConfig();
      this.activationMap = new HashMap();
      int var8;
      if (this.activationConfigBean != null) {
         ActivationConfigPropertyBean[] var7 = this.activationConfigBean.getActivationConfigProperties();

         for(var8 = 0; var8 < var7.length; ++var8) {
            String var9 = var7[var8].getActivationConfigPropertyName();
            String var10 = var7[var8].getActivationConfigPropertyValue();
            this.activationMap.put(var9.toUpperCase(Locale.ENGLISH), var10);
         }
      }

      try {
         Ejb30MessageDrivenBeanClassChecker.validateForConflictingConfiguration(var2, this.activationMap.keySet(), this.getDisplayName());
      } catch (ComplianceException var15) {
         throw new WLDeploymentException(var15.getMessage(), var15);
      }

      if (debugLogger.isDebugEnabled()) {
         this.dumpActivationConfigValues();
      }

      if (this.activationMap.get("RESOURCEADAPTERJNDINAME") != null && var6.getResourceAdapterJNDIName() == null) {
         this.resourceAdapterJndiName = (String)this.activationMap.get("RESOURCEADAPTERJNDINAME");
         this.debugPropertyValue("resourceAdapterJNDIName", this.resourceAdapterJndiName);
      } else {
         this.resourceAdapterJndiName = var6.getResourceAdapterJNDIName();
         if (var6.getResourceAdapterJNDIName() != null && this.activationMap.get("RESOURCEADAPTERJNDINAME") != null) {
            EJBLogger.logOverridenActivationConfigProperty(this.getDestinationName(), "resourceAdapterJNDIName", this.resourceAdapterJndiName);
         }
      }

      if (this.resourceAdapterJndiName != null) {
         this.isResourceAdapter = true;
      }

      this.m_messageSelector = (String)this.activationMap.get("MESSAGESELECTOR");
      if (this.m_messageSelector == null) {
         this.m_messageSelector = "";
      }

      if (this.activationMap.get("ACKNOWLEDGEMODE") != null) {
         this.m_acknowledgeMode = this.acknowledgeMode2Int((String)this.activationMap.get("ACKNOWLEDGEMODE"));
      }

      if (this.activationMap.get("DESTINATIONTYPE") != null) {
         this.setDestination((String)this.activationMap.get("DESTINATIONTYPE"));
      }

      if (this.activationMap.get("SUBSCRIPTIONDURABILITY") != null) {
         this.m_isDurableSubscriber = "durable".equalsIgnoreCase((String)this.activationMap.get("SUBSCRIPTIONDURABILITY"));
      }

      if (this.activationMap.get("INACTIVE") != null) {
         this.m_isInactive = Boolean.parseBoolean((String)this.activationMap.get("INACTIVE"));
         if (this.m_isInactive) {
            EJBLogger.logMDBInactive(this.getEJBName());
         } else {
            EJBLogger.logMDBActive(this.getEJBName());
         }
      }

      this.messageDestinationLink = var4.getMessageDestinationLink();
      this.m_cachingDescriptor = var2.getCachingDescriptor();
      this.m_maxConcurrentInstances = this.m_cachingDescriptor.getMaxBeansInFreePool();
      if (this.activationMap.get("DESTINATIONJNDINAME") != null && var2.getDestinationJNDIName() == null) {
         this.m_destinationJNDIName = (String)this.activationMap.get("DESTINATIONJNDINAME");
         this.debugPropertyValue("destinationJNDIName", this.m_destinationJNDIName);
      } else {
         this.m_destinationJNDIName = var2.getDestinationJNDIName();
         if (var2.getDestinationJNDIName() != null && this.activationMap.get("DESTINATIONJNDINAME") != null) {
            EJBLogger.logOverridenActivationConfigProperty(this.getDisplayName(), "destinationJNDIName", this.m_destinationJNDIName);
         }
      }

      if (this.activationMap.get("INITIALCONTEXTFACTORY") != null && !this.isSet("InitialContextFactory", var6)) {
         this.m_initialContextFactory = (String)this.activationMap.get("INITIALCONTEXTFACTORY");
         this.debugPropertyValue("initialContextFactory", this.m_initialContextFactory);
      } else {
         this.m_initialContextFactory = var6.getInitialContextFactory();
         if (this.isSet("InitialContextFactory", var6) && this.activationMap.get("INITIALCONTEXTFACTORY") != null) {
            EJBLogger.logOverridenActivationConfigProperty(this.getDisplayName(), "initialContextFactory", this.m_initialContextFactory);
         }
      }

      if (this.activationMap.get("PROVIDERURL") != null && var6.getProviderUrl() == null) {
         this.m_providerURL = (String)this.activationMap.get("PROVIDERURL");
         this.debugPropertyValue("providerUrl", this.m_providerURL);
      } else {
         this.m_providerURL = var6.getProviderUrl();
         if (var6.getProviderUrl() != null && this.activationMap.get("PROVIDERURL") != null) {
            EJBLogger.logOverridenActivationConfigProperty(this.getDisplayName(), "providerUrl", this.m_providerURL);
         }
      }

      if (this.activationMap.get("CONNECTIONFACTORYJNDINAME") != null && !this.isSet("ConnectionFactoryJNDIName", var6)) {
         this.m_connectionFactoryJNDIName = (String)this.activationMap.get("CONNECTIONFACTORYJNDINAME");
         this.debugPropertyValue("connectionFactoryJNDIName", this.m_connectionFactoryJNDIName);
      } else {
         this.m_connectionFactoryJNDIName = var6.getConnectionFactoryJNDIName();
         if (this.isSet("ConnectionFactoryJNDIName", var6) && this.activationMap.get("CONNECTIONFACTORYJNDINAME") != null) {
            EJBLogger.logOverridenActivationConfigProperty(this.getDisplayName(), "connectionFactoryJNDIName", this.m_connectionFactoryJNDIName);
         }
      }

      if (this.activationMap.get("CONNECTIONFACTORYRESOURCELINK") != null && var6.getConnectionFactoryResourceLink() == null) {
         this.connectionFactoryResourceLink = (String)this.activationMap.get("CONNECTIONFACTORYRESOURCELINK");
         this.debugPropertyValue("connectionFactoryResourceLink", this.connectionFactoryResourceLink);
      } else {
         this.connectionFactoryResourceLink = var6.getConnectionFactoryResourceLink();
         if (var6.getConnectionFactoryResourceLink() != null && this.activationMap.get("CONNECTIONFACTORYRESOURCELINK") != null) {
            EJBLogger.logOverridenActivationConfigProperty(this.getDisplayName(), "connectionFactoryResourceLink", this.connectionFactoryResourceLink);
         }
      }

      if (this.activationMap.get("DESTINATIONRESOURCELINK") != null && var6.getDestinationResourceLink() == null) {
         this.destinationResourceLink = (String)this.activationMap.get("DESTINATIONRESOURCELINK");
         this.debugPropertyValue("destinationResourceLink", this.destinationResourceLink);
      } else {
         this.destinationResourceLink = var6.getDestinationResourceLink();
         if (var6.getDestinationResourceLink() != null && this.activationMap.get("DESTINATIONRESOURCELINK") != null) {
            EJBLogger.logOverridenActivationConfigProperty(this.getDisplayName(), "destinationResourceLink", this.destinationResourceLink);
         }
      }

      if (this.activationMap.get("JMSPOLLINGINTERVALSECONDS") != null && !this.isSet("JmsPollingIntervalSeconds", var6)) {
         try {
            this.jmsPollingIntervalSeconds = Integer.parseInt((String)this.activationMap.get("JMSPOLLINGINTERVALSECONDS"));
            if (this.jmsPollingIntervalSeconds <= 0) {
               this.jmsPollingIntervalSeconds = var6.getJmsPollingIntervalSeconds();
            }
         } catch (NumberFormatException var16) {
            throw new WLDeploymentException("the value for jmsPollingIntervalSeconds must be numberic", var16);
         }

         this.debugPropertyValue("jmsPollingIntervalSeconds", Integer.toString(this.jmsPollingIntervalSeconds));
      } else {
         this.jmsPollingIntervalSeconds = var6.getJmsPollingIntervalSeconds();
         if (this.isSet("JmsPollingIntervalSeconds", var6) && this.activationMap.get("JMSPOLLINGINTERVALSECONDS") != null) {
            EJBLogger.logOverridenActivationConfigProperty(this.getDisplayName(), "jmsPollingIntervalSeconds", Integer.toString(this.jmsPollingIntervalSeconds));
         }
      }

      if (this.activationMap.get("INITSUSPENDSECONDS") != null && !this.isSet("InitSuspendSeconds", var6)) {
         try {
            this.initSuspendSeconds = Integer.parseInt((String)this.activationMap.get("INITSUSPENDSECONDS"));
         } catch (NumberFormatException var14) {
            throw new WLDeploymentException("the value for 'initSuspendSeconds' must be numberic", var14);
         }

         this.debugPropertyValue("initSuspendSeconds", Integer.toString(this.initSuspendSeconds));
      } else {
         this.initSuspendSeconds = var6.getInitSuspendSeconds();
         if (this.isSet("InitSuspendSeconds", var6) && this.activationMap.get("INITSUSPENDSECONDS") != null) {
            EJBLogger.logOverridenActivationConfigProperty(this.getDisplayName(), "initSuspendSeconds", Integer.toString(this.initSuspendSeconds));
         }
      }

      if (this.activationMap.get("MAXSUSPENDSECONDS") != null && !this.isSet("MaxSuspendSeconds", var6)) {
         try {
            this.maxSuspendSeconds = Integer.parseInt((String)this.activationMap.get("MAXSUSPENDSECONDS"));
         } catch (NumberFormatException var13) {
            throw new WLDeploymentException("the value for MAXSUSPENDSECONDS must be numberic", var13);
         }

         this.debugPropertyValue("maxSuspendSeconds", Integer.toString(this.maxSuspendSeconds));
      } else {
         this.maxSuspendSeconds = var6.getMaxSuspendSeconds();
         if (this.isSet("MaxSuspendSeconds", var6) && this.activationMap.get("MAXSUSPENDSECONDS") != null) {
            EJBLogger.logOverridenActivationConfigProperty(this.getDisplayName(), "maxSuspendSeconds", Integer.toString(this.maxSuspendSeconds));
         }
      }

      if (this.activationMap.get("MAXMESSAGESINTRANSACTION") != null && !this.isSet("MaxMessagesInTransaction", var6)) {
         try {
            this.maxMessagesInTransaction = Integer.parseInt((String)this.activationMap.get("MAXMESSAGESINTRANSACTION"));
         } catch (NumberFormatException var12) {
            throw new WLDeploymentException("the value for MAXMESSAGESINTRANSACTION must be numberic", var12);
         }

         this.debugPropertyValue("maxMessagesInTransaction", Integer.toString(this.maxMessagesInTransaction));
      } else {
         this.maxMessagesInTransaction = var6.getMaxMessagesInTransaction();
         if (this.isSet("MaxMessagesInTransaction", var6) && this.activationMap.get("MAXMESSAGESINTRANSACTION") != null) {
            EJBLogger.logOverridenActivationConfigProperty(this.getDisplayName(), "maxMessagesInTransaction", Integer.toString(this.maxMessagesInTransaction));
         }
      }

      this.securityPlugin = var6.getSecurityPlugin();
      if (isServer()) {
         ServerMBean var17 = ManagementService.getRuntimeAccess(kernelId).getServer();
         this.currServerName = var17.getName();
         this.clusterMBean = var17.getCluster();
         if (this.clusterMBean != null) {
            this.currClusterName = this.clusterMBean.getName();
         }

         this.domainName = ManagementService.getRuntimeAccess(kernelId).getDomain().getName();
      } else {
         this.currServerName = "";
      }

      if (this.activationMap.get("JMSCLIENTID") != null && var6.getJmsClientId() == null) {
         this.jmsClientIDBase = (String)this.activationMap.get("JMSCLIENTID");
         this.debugPropertyValue("jmsClientId", this.jmsClientIDBase);
      } else {
         this.jmsClientIDBase = var6.getJmsClientId();
         if (var6.getJmsClientId() != null && this.activationMap.get("JMSCLIENTID") != null) {
            EJBLogger.logOverridenActivationConfigProperty(this.getDisplayName(), "jmsClientId", this.jmsClientIDBase);
         }
      }

      if (this.jmsClientIDBase == null) {
         this.jmsClientIDBase = this.getEJBName();
      }

      this.jmsClientID = this.jmsClientIDBase;
      this.isGenerateUniqueJmsClientId = var6.isGenerateUniqueJmsClientId();
      if (this.activationMap.get("DURABLESUBSCRIPTIONDELETION") != null && !this.isSet("DurableSubscriptionDeletion", var6)) {
         this.isDurableSubscriptionDeletion = Boolean.parseBoolean((String)this.activationMap.get("DURABLESUBSCRIPTIONDELETION"));
         this.debugPropertyValue("durableSubscriptionDeletion", Boolean.toString(this.isDurableSubscriptionDeletion));
      } else {
         this.isDurableSubscriptionDeletion = var6.isDurableSubscriptionDeletion();
         if (this.isSet("DurableSubscriptionDeletion", var6) && this.activationMap.get("DURABLESUBSCRIPTIONDELETION") != null) {
            EJBLogger.logOverridenActivationConfigProperty(this.getDisplayName(), "durableSubscriptionDeletion", Boolean.toString(this.isDurableSubscriptionDeletion));
         }
      }

      if (this.activationMap.get("USE81STYLEPOLLING") != null && !this.isSet("Use81StylePolling", var6)) {
         this.use81StylePolling = Boolean.parseBoolean((String)this.activationMap.get("USE81STYLEPOLLING"));
         this.debugPropertyValue("use81StylePolling", Boolean.toString(this.use81StylePolling));
      } else {
         this.use81StylePolling = var6.isUse81StylePolling();
         if (this.isSet("Use81StylePolling", var6) && this.activationMap.get("USE81STYLEPOLLING") != null) {
            EJBLogger.logOverridenActivationConfigProperty(this.getDisplayName(), "use81StylePolling", Boolean.toString(this.use81StylePolling));
         }
      }

      String var18 = (String)this.activationMap.get("MINIMIZEAQSESSIONS");
      if (var18 != null) {
         this.minimizeAQSessions = Boolean.parseBoolean(var18);
         this.debugPropertyValue("minimizeAQSessions", Boolean.toString(this.minimizeAQSessions));
      }

      boolean var19 = false;
      Loggable var21;
      if (this.activationMap.get("DISTRIBUTEDDESTINATIONCONNECTION") != null && !this.isSet("DistributedDestinationConnection", var6)) {
         var8 = this.convertToInt("DISTRIBUTEDDESTINATIONCONNECTION", DDConstants.DISTRIBUTEDDESTINATIONCONNECTIONS, (String)this.activationMap.get("DISTRIBUTEDDESTINATIONCONNECTION"));
         if (var8 < 0) {
            var21 = EJBLogger.logIllegalDistributedDestinationConnectionValueLoggable(this.getDisplayName(), (String)this.activationMap.get("DISTRIBUTEDDESTINATIONCONNECTION"));
            throw new WLDeploymentException(var21.getMessage());
         }

         this.distributedDestinationConnection = var8;
         this.debugPropertyValue("distributedDestinationConnection", (String)this.activationMap.get("DISTRIBUTEDDESTINATIONCONNECTION"));
      } else {
         this.distributedDestinationConnection = this.convertToInt("distributedDestinationConnection", DDConstants.DISTRIBUTEDDESTINATIONCONNECTIONS, var6.getDistributedDestinationConnection());
         if (this.isSet("DistributedDestinationConnection", var6) && this.activationMap.get("DISTRIBUTEDDESTINATIONCONNECTION") != null) {
            EJBLogger.logOverridenActivationConfigProperty(this.getDisplayName(), "distributedDestinationConnection", var6.getDistributedDestinationConnection());
         }
      }

      if (this.activationMap.get("TOPICMESSAGESDISTRIBUTIONMODE") != null) {
         var8 = this.convertToInt("TOPICMESSAGESDISTRIBUTIONMODE", DDConstants.TOPICMESSAGEDISTRIBUTIONMODES, (String)this.activationMap.get("TOPICMESSAGESDISTRIBUTIONMODE"));
         if (var8 < 0) {
            var21 = EJBLogger.logIllegalTopicMessagesDistributionModeValueLoggable(this.getDisplayName(), (String)this.activationMap.get("TOPICMESSAGESDISTRIBUTIONMODE"));
            throw new WLDeploymentException(var21.getMessage());
         }

         this.topicMessagesDistributionMode = var8;
         this.debugPropertyValue("topicMessagesDistributionMode", (String)this.activationMap.get("TOPICMESSAGESDISTRIBUTIONMODE"));
      }

      this.callByReference = var2.useCallByReference();
      NamingConvention var20 = new NamingConvention(this.getBeanClassName(), this.getEJBName());
      if (!this.getIsWeblogicJMS() || var2.isMessageDriven()) {
         this.messageDrivenLocalObjectClassName = var20.getMdLocalObjectClassName(this.getIsWeblogicJMS());
      }

      this.messagingTypeInterfaceClass = this.loadClass(this.messagingTypeInterfaceName);
      this.messageDrivenLocalObjectClass = null;
      if (this.getIsWeblogicJMS()) {
         this.initializeOnMessageMethodInfo();
      } else {
         this.initializeMethodInfos();
      }

      this.mdManagerList = new ArrayList();
      this.backupDestMembersList = new ArrayList();
      if (this.isEJB30()) {
         Class var22 = this.getBeanClass();
         Class var11 = this.getMessagingTypeInterfaceClass();
         if (!var11.isAssignableFrom(var22)) {
            this.setIndirectlyImplMessageListener(true);
            this.generatedBeanClassName = var20.getGeneratedBeanClassName();
         }
      }

      if (this.isTimerDriven()) {
         this.mdbTimerManagerFactory = new MDBTimerManagerFactory();
      }

   }

   private void dumpActivationConfigValues() {
      if (this.activationMap != null) {
         Iterator var1 = this.activationMap.keySet().iterator();

         while(var1.hasNext()) {
            String var2 = (String)var1.next();
            String var3 = (String)this.activationMap.get(var2);
            debugLogger.debug("Activation Config: <" + var2 + "--" + var3 + ">");
         }

      }
   }

   private void detectDestinationByJNDIName() {
      if (!this.isDestinationQueue() && !this.isDestinationTopic()) {
         try {
            Destination var1 = this.lookupWithRightSubject(this.m_destinationJNDIName);
            if (var1 instanceof Destination) {
               if (var1 instanceof Queue) {
                  this.setDestination("javax.jms.Queue");
               } else if (var1 instanceof Topic) {
                  this.setDestination("javax.jms.Topic");
               } else if (debugLogger.isDebugEnabled()) {
                  debug("Unknown message destination type " + var1);
               }
            } else if (debugLogger.isDebugEnabled()) {
               debug("Unknown message destination type " + var1);
            }
         } catch (Exception var2) {
            if (debugLogger.isDebugEnabled()) {
               debug("No message destination found to associate with MessageDrivenBean " + this.ejbName + " : " + var2);
            }
         }
      }

   }

   public void init() {
   }

   protected void setIndirectlyImplMessageListener(boolean var1) {
      this.indirectlyImplMessageListener = var1;
   }

   public boolean isIndirectlyImplMessageListener() {
      return this.indirectlyImplMessageListener;
   }

   public String getGeneratedBeanClassName() {
      return this.generatedBeanClassName;
   }

   public Class getGeneratedBeanClass() {
      try {
         if (this.generatedBeanClass == null) {
            this.generatedBeanClass = this.loadClass(this.generatedBeanClassName);
         }

         return this.generatedBeanClass;
      } catch (ClassNotFoundException var2) {
         throw new AssertionError("Unable to load class " + this.generatedBeanClassName, var2);
      }
   }

   private void setDestination(String var1) {
      if (!this.isDestinationQueue() && !this.isDestinationTopic()) {
         String var2 = "javax.jms.Queue";
         String var3 = "javax.jms.Topic";
         if (var2.equals(var1)) {
            this.m_isDestinationQueue = true;
         } else if (var3.equals(var1)) {
            this.m_isDestinationTopic = true;
         } else if (this.getIsWeblogicJMS()) {
            throw new AssertionError("Unknown destination type : " + var1);
         }

      }
   }

   private int acknowledgeMode2Int(String var1) throws WLDeploymentException {
      byte var2 = 1;
      if ("dups-ok-acknowledge".equalsIgnoreCase(var1)) {
         var2 = 3;
      } else if (!"auto-acknowledge".equalsIgnoreCase(var1)) {
         if ("no_acknowledge".equalsIgnoreCase(var1)) {
            throw new WLDeploymentException(var1 + " is no longer a valid acknowledgement mode.");
         }

         if ("multicast_no_acknowledge".equals(var1)) {
            throw new WLDeploymentException(var1 + " is no longer a valid acknowledgement mode.");
         }

         if (null != var1 && this.getIsWeblogicJMS()) {
            throw new AssertionError("Unknown ACKNOWLEDGE MODE : " + var1);
         }
      }

      return var2;
   }

   public Context getInitialContext() throws NamingException {
      AuthenticatedSubject var1 = null;

      try {
         var1 = this.getRunAsSubject();
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      if (this.userName == null || this.password == null || var1 != null && (this.userName.length() == 0 || this.password.length() == 0)) {
         this.reSetUsernameAndPassword();
      }

      if (this.hasCredentials) {
         return this.getInitialContext(this.userName.toString(), this.password.toString());
      } else {
         if (var1 == null) {
            var1 = SecurityServiceManager.getCurrentSubject(kernelId);
         }

         return (Context)this.doPrivilagedAction(var1, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               Hashtable var1 = new Hashtable();
               var1.put("java.naming.factory.initial", MessageDrivenBeanInfoImpl.this.m_initialContextFactory);
               var1.put("weblogic.jndi.allowGlobalResourceLookup", "true");
               if (null != MessageDrivenBeanInfoImpl.this.m_providerURL) {
                  var1.put("java.naming.provider.url", MessageDrivenBeanInfoImpl.this.m_providerURL);
               }

               InitialContext var2 = new InitialContext(var1);
               synchronized(MessageDrivenBeanInfoImpl.this) {
                  MessageDrivenBeanInfoImpl.this.subject = SecurityServiceManager.getCurrentSubject(MessageDrivenBeanInfoImpl.kernelId);
                  return var2;
               }
            }
         });
      }
   }

   public void reSetUsernameAndPassword() {
      this.userName = new StringBuffer();
      this.password = new StringBuffer();
      this.hasCredentials = false;

      try {
         this.hasCredentials = JMSConnectionPoller.getCredentials(this, this.userName, this.password);
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public Context getInitialContext(final String var1, final String var2) throws NamingException {
      return (Context)this.doPrivilagedAction(SecurityServiceManager.getCurrentSubject(kernelId), new PrivilegedExceptionAction() {
         public Object run() throws Exception {
            Hashtable var1x = new Hashtable();
            var1x.put("java.naming.factory.initial", MessageDrivenBeanInfoImpl.this.m_initialContextFactory);
            var1x.put("weblogic.jndi.allowGlobalResourceLookup", "true");
            if (null != MessageDrivenBeanInfoImpl.this.m_providerURL) {
               var1x.put("java.naming.provider.url", MessageDrivenBeanInfoImpl.this.m_providerURL);
            }

            var1x.put("java.naming.security.principal", var1);
            var1x.put("java.naming.security.credentials", var2);
            InitialContext var2x = new InitialContext(var1x);
            synchronized(MessageDrivenBeanInfoImpl.this) {
               MessageDrivenBeanInfoImpl.this.subject = SecurityServiceManager.getCurrentSubject(MessageDrivenBeanInfoImpl.kernelId);
               return var2x;
            }
         }
      });
   }

   public boolean isDeliveryTransacted(Method var1) throws NoSuchMethodException {
      this.getMessagingTypeInterfaceClass().getDeclaredMethod(var1.getName(), (Class[])var1.getParameterTypes());
      if (this.usesBeanManagedTx()) {
         return false;
      } else {
         MethodInfo var2 = this.getMessagingTypeMethodInfo(var1);
         short var3 = var2.getTransactionAttribute();
         return 1 == var3;
      }
   }

   public String getName() {
      return this.getEJBName();
   }

   public boolean usesBeanManagedTx() {
      return this.m_usesBeanManagedTx;
   }

   public String getMessageSelector() {
      return this.m_messageSelector;
   }

   public int getAcknowledgeMode() {
      return this.m_acknowledgeMode;
   }

   public boolean isDestinationQueue() {
      return this.m_isDestinationQueue;
   }

   public boolean isDestinationTopic() {
      return this.m_isDestinationTopic;
   }

   public boolean isDurableSubscriber() {
      return this.m_isDurableSubscriber;
   }

   public int getMaxConcurrentInstances() {
      return this.m_maxConcurrentInstances;
   }

   public String getProviderURL() {
      return this.m_providerURL;
   }

   public String getDestinationName() {
      return this.m_destinationJNDIName;
   }

   public String getConnectionFactoryJNDIName() {
      return this.m_connectionFactoryJNDIName;
   }

   public int getJMSPollingIntervalSeconds() {
      return this.jmsPollingIntervalSeconds;
   }

   public int getInitSuspendSeconds() {
      return this.initSuspendSeconds;
   }

   public int getMaxSuspendSeconds() {
      return this.maxSuspendSeconds;
   }

   public SecurityPluginBean getSecurityPlugin() {
      return this.securityPlugin;
   }

   public String getJMSClientID() {
      return this.jmsClientID;
   }

   public boolean isDurableSubscriptionDeletion() {
      return this.isDurableSubscriptionDeletion;
   }

   public int getMaxMessagesInTransaction() {
      return this.maxMessagesInTransaction;
   }

   public boolean getUse81StylePolling() {
      return this.use81StylePolling;
   }

   public boolean getMinimizeAQSessions() {
      return this.minimizeAQSessions;
   }

   public String getResourceAdapterJndiName() {
      return this.resourceAdapterJndiName;
   }

   public boolean useCallByReference() {
      return this.callByReference;
   }

   public boolean noLocalMessages() {
      return this.m_noLocal;
   }

   public boolean getIsWeblogicJMS() {
      return !this.isResourceAdapter;
   }

   public ActivationConfigBean getActivationConfigBean() {
      return this.activationConfigBean;
   }

   public MessageDrivenManagerIntf getBeanManagerInstance(EJBComponentRuntimeMBeanImpl var1) {
      return new MessageDrivenManager(var1);
   }

   public MessageDrivenManagerIntf getBeanManager() {
      return (MessageDrivenManagerIntf)super.getBeanManager();
   }

   public List<MessageDrivenManagerIntf> getMDManagerList() {
      return this.mdManagerList;
   }

   public List<DestinationDetail> getBackupDestMembersList() {
      return this.backupDestMembersList;
   }

   public synchronized AbstractSubject getSubject() {
      return this.subject;
   }

   public boolean getIsRemoteSubjectExists() {
      if (!this.isRemoteSubjectExists) {
         this.isRemoteSubjectExists = CrossDomainSecurityManager.getCrossDomainSecurityUtil().ifRemoteSubjectExists(this.m_providerURL);
      }

      return this.isRemoteSubjectExists;
   }

   public boolean isOnMessageTransacted() {
      return 1 == this.m_onMessageMethodInfo.getTransactionAttribute();
   }

   public Integer getOnMessageTxIsolationLevel() {
      int var1 = this.m_onMessageMethodInfo.getTxIsolationLevel();
      return var1 == -1 ? null : new Integer(var1);
   }

   public MethodInfo getOnMessageMethodInfo() {
      return this.m_onMessageMethodInfo;
   }

   private void initializeOnMessageMethodInfo() throws WLDeploymentException {
      Class var1 = this.getBeanClass();

      assert var1 != null;

      Method var2 = null;

      try {
         Class[] var3 = new Class[]{Message.class};
         var2 = var1.getMethod("onMessage", var3);
      } catch (NoSuchMethodException var5) {
         EJBComplianceTextFormatter var4 = new EJBComplianceTextFormatter();
         throw new WLDeploymentException(var4.BEAN_CLASS_IMPLEMENTS_MESSAGE_LISTENER(this.getDisplayName(), this.getMessagingTypeInterfaceName()));
      }

      this.m_onMessageMethodInfo = MethodInfoImpl.createMethodInfoImpl(var2, this.jaccPolicyContextId);

      assert this.m_onMessageMethodInfo != null;

   }

   public void assignDefaultTXAttributesIfNecessary() {
      HashSet var1 = new HashSet();
      if (this.getIsWeblogicJMS()) {
         var1.add(this.m_onMessageMethodInfo);
      } else {
         var1.addAll(this.getAllMessagingTypeMethodInfos());
      }

      var1.addAll(this.getAllBeanMethodInfos());
      if (this.usesBeanManagedTx()) {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            ((MethodInfo)var2.next()).setTransactionAttribute((short)0);
         }
      } else {
         StringBuffer var4 = new StringBuffer();
         short var3 = DDDefaults.getBeanMethodTransactionAttribute(this);
         var4.append(this.assignDefaultTXAttributesIfNecessary("beanClass", var1, var3));
         if (var4.length() > 0 && !this.isEJB30()) {
            EJBLogger.logEJBUsesDefaultTXAttribute(this.getDisplayName(), "NotSupported", var4.toString());
         }
      }

   }

   protected short getTxAttribute(MethodInfo var1, Class var2) {
      return this.usesBeanManagedTx() ? 0 : var1.getTransactionAttribute();
   }

   public Iterator getAllMethodInfosIterator() {
      ArrayList var1 = new ArrayList();
      if (this.getIsWeblogicJMS()) {
         ArrayList var2 = new ArrayList();
         var2.add(this.m_onMessageMethodInfo);
         var1.add(var2.iterator());
      } else {
         var1.add(this.messagingTypeMethods.values().iterator());
      }

      var1.add(this.getAllBeanMethodInfos().iterator());
      return new CombinedIterator(var1);
   }

   public MethodDescriptor getMDBMethodDescriptor(Method var1) {
      return (MethodDescriptor)this.mdbMethodDescriptors.get(var1.toString());
   }

   public Class getMessageDrivenLocalObjectClass() {
      try {
         if (this.messageDrivenLocalObjectClass == null) {
            this.messageDrivenLocalObjectClass = this.loadClass(this.messageDrivenLocalObjectClassName);
         }

         return this.messageDrivenLocalObjectClass;
      } catch (ClassNotFoundException var2) {
         throw new AssertionError(var2);
      }
   }

   public String getMessagingTypeInterfaceName() {
      return this.messagingTypeInterfaceName;
   }

   public Class getMessagingTypeInterfaceClass() {
      return this.messagingTypeInterfaceClass;
   }

   public MethodInfo getMessagingTypeMethodInfo(String var1) {
      return (MethodInfo)this.messagingTypeMethods.get(var1);
   }

   public MethodInfo getMessagingTypeMethodInfo(String var1, String[] var2) {
      return (MethodInfo)this.messagingTypeMethods.get(getMethodSignature(var1, var2));
   }

   public MethodInfo getMessagingTypeMethodInfo(Method var1) {
      return (MethodInfo)this.messagingTypeMethods.get(getMethodSignature(var1));
   }

   public Collection getAllMessagingTypeMethodInfos() {
      return this.messagingTypeMethods.values();
   }

   public Map getMessagingTypeMethods() {
      return this.messagingTypeMethods;
   }

   private void initializeMethodInfos() throws WLDeploymentException {
      List var1 = null;

      try {
         var1 = Arrays.asList((Object[])this.messagingTypeInterfaceClass.getMethods());
      } catch (Throwable var5) {
         Loggable var3 = EJBLogger.logunableToInitializeInterfaceMethodInfoLoggable(this.getEJBName(), StackTraceUtils.throwable2StackTrace(var5));
         throw new WLDeploymentException(var3.getMessage(), var5);
      }

      MethodInfoImpl var4;
      for(Iterator var2 = var1.iterator(); var2.hasNext(); this.messagingTypeMethods.put(var4.getSignature(), var4)) {
         Method var6 = (Method)var2.next();
         var4 = MethodInfoImpl.createMethodInfoImpl(var6, "MessagingType", this.jaccPolicyContextId);
         if (debugLogger.isDebugEnabled()) {
            debug(".....messagingTypeMethods.put(" + var4.getSignature() + ")");
         }
      }

   }

   protected void setMethodDescriptors(BaseEJBHomeIntf var1, Class var2, String var3) throws WLDeploymentException {
      Method[] var4 = var2.getMethods();
      Class var6 = this.getMessagingTypeInterfaceClass();

      for(int var8 = 0; var8 < var4.length; ++var8) {
         Method var9 = var4[var8];
         if (!Helper.isBeaSyntheticMethod(var9)) {
            boolean var10 = false;
            MethodInfo var5 = null;
            if (var3.equals("MessagingType")) {
               var5 = this.getMessagingTypeMethodInfo(var9);
               var10 = true;
            }

            if (var5 != null) {
               MethodText var11 = new MethodText();
               var11.setMethod(var9);
               var11.setOptions(128);
               String var12 = var11.toString();
               var12 = "eo_" + var12;
               MethodDescriptor var13 = this.setMethodDescriptor(var1, var9, var2, var5, var10, var12, var3);
               Method var7 = null;

               try {
                  var7 = var6.getMethod(var9.getName(), (Class[])var9.getParameterTypes());
               } catch (Exception var15) {
               }

               if (var7 != null) {
                  this.mdbMethodDescriptors.put(var7.toString(), var13);
               }
            }
         }
      }

   }

   public void dumpMethodDescriptors() {
      if (!this.getIsWeblogicJMS()) {
         Field[] var1 = this.getMessageDrivenLocalObjectClass().getFields();
         debug("** Dumping Message Driven MethodDescriptor for: " + this.getMessagingTypeInterfaceName());
         this.dumpMethodDescriptorFields(var1, (Object)null);
      }

   }

   public void dumpMethodInfos() {
      if (!this.getIsWeblogicJMS()) {
         debug("Dumping Messging Type MethodInfos for: " + this.getDisplayName());
         StringBuffer var1 = new StringBuffer();
         var1.append("Messging Type Methods: ");
         Iterator var2 = this.getAllMessagingTypeMethodInfos().iterator();

         while(var2.hasNext()) {
            var1.append(var2.next().toString());
            if (var2.hasNext()) {
               var1.append(", ");
            }
         }

         debug(var1.toString());
      }

   }

   public void prepare(ApplicationContextInternal var1, DeploymentInfo var2) throws WLDeploymentException {
      super.prepare(var1);
      this.ensureMDBHasDestinationConfigured();
      if (!this.getIsWeblogicJMS()) {
         this.setMethodDescriptors((BaseEJBHomeIntf)null, this.getMessageDrivenLocalObjectClass(), "MessagingType");
      }

      if (debugLogger.isDebugEnabled()) {
         this.dumpMethodInfos();
         this.dumpMethodDescriptors();
      }

   }

   public void activate(Context var1, Map var2, Map var3, DeploymentInfo var4, Context var5) throws WLDeploymentException {
      this.environmentContext = var5;
      this.isRemoteSubjectExists = CrossDomainSecurityManager.getCrossDomainSecurityUtil().ifRemoteSubjectExists(this.m_providerURL);
      this.detectDestinationByJNDIName();
      this.resolveMessageDestinationLink(var5, this.getDeploymentInfo());
      this.resolveDestinationResourceLink();
      this.resolveConnectionFactoryResourceLink();
      String var6 = this.getDispatchPolicy();
      WorkManager var7 = WorkManagerFactory.getInstance().find("weblogic.kernel.Default");
      this.wm = WorkManagerFactory.getInstance().find(var6, this.getDeploymentInfo().getApplicationName(), (String)null);
      if (var6 != null) {
         if (var7.getType() == 1 && this.wm == var7) {
            EJBLogger.logMDBUnknownDispatchPolicyWM(this.getEJBName(), var6);
         } else if (var7.getType() == 2 && !Kernel.isDispatchPolicy(var6)) {
            EJBLogger.logMDBUnknownDispatchPolicy(this.getEJBName(), var6);
         }
      }

      if (!this.getIsWeblogicJMS()) {
         this.createJCAManager();
      }

   }

   private void createJCAManager() throws WLDeploymentException {
      this.setupBeanManager(this.ejbComponentRuntime);
      MessageDrivenManagerIntf var1 = this.getBeanManager();
      var1.setup((BaseEJBRemoteHomeIntf)null, (BaseEJBLocalHomeIntf)null, this, this.environmentContext);
      this.mdManagerList.add(var1);
      this.postBeanManagerSetup(var1);
      if (debugLogger.isDebugEnabled()) {
         debug("Deploying JCA based MD bean, resourceAdapterJNDIName:" + this.resourceAdapterJndiName);
      }

   }

   public void deployMessageDrivenBeans() throws Exception {
      if (!this.getIsWeblogicJMS()) {
         if (this.mdManagerList.size() < 1) {
            EJBLogger.logErrorOnStartMDBs(this.getDisplayName());
         } else {
            MessageDrivenManagerIntf var1 = (MessageDrivenManagerIntf)this.mdManagerList.get(0);

            try {
               this.mdManagerStart(var1);
            } catch (WLDeploymentException var3) {
               throw var3;
            } catch (Exception var4) {
               EJBLogger.logStackTrace(var4);
            }

         }
      } else {
         while(this.destinationHandler.get() == null && !this.destinationHandler.compareAndSet((Object)null, new DestinationEventHandler())) {
         }

         if (this.destinationHandler.get() != null) {
            ((DestinationEventHandler)this.destinationHandler.get()).register();
         }

      }
   }

   private void mdManagerStart(MessageDrivenManagerIntf var1) throws WLDeploymentException {
      ClassLoader var2 = Thread.currentThread().getContextClassLoader();

      try {
         EJBRuntimeUtils.pushEnvironment(this.environmentContext);
         Thread.currentThread().setContextClassLoader(this.getClassLoader());
         var1.start();
         var1.setIsDeployed(true);
      } finally {
         EJBRuntimeUtils.popEnvironment();
         Thread.currentThread().setContextClassLoader(var2);
      }

   }

   public void onUndeploy() {
      if (this.isEJB30() && this.contextBoundCounter.getAndDecrement() <= 1) {
         if (debugLogger.isDebugEnabled()) {
            debug("Undeploying MDB destination:" + this.getDestinationName());
         }

         try {
            this.getBeanManager().getEnvironmentContext().unbind("comp/EJBContext");
         } catch (NamingException var2) {
            if (debugLogger.isDebugEnabled()) {
               debug("error unbinding EJBContext from local environment: " + var2);
            }
         }
      }

      super.onUndeploy();
   }

   private void resolveDestinationResourceLink() {
      if (this.destinationResourceLink != null) {
         this.m_destinationJNDIName = this.m_desc.resolveResourceLink(this.deploymentInfo.getApplicationId(), this.destinationResourceLink);
      }

   }

   private void resolveConnectionFactoryResourceLink() {
      if (this.connectionFactoryResourceLink != null) {
         this.m_connectionFactoryJNDIName = this.m_desc.resolveResourceLink(this.deploymentInfo.getApplicationId(), this.connectionFactoryResourceLink);
      }

   }

   private void resolveMessageDestinationLink(Context var1, DeploymentInfo var2) throws WLDeploymentException {
      if (this.messageDestinationLink != null) {
         MessageDestinationDescriptorBean var3 = null;

         try {
            Context var4 = (Context)var1.lookup("app/messageDestination");
            String var5 = J2EEUtils.getAppScopedLinkPath(this.messageDestinationLink, var2.getModuleURI(), var4);
            if (var5 == null) {
               Loggable var6 = EJBLogger.logUnableToResolveMDBMessageDestinationLinkLoggable(this.messageDestinationLink, this.getEJBName(), var2.getModuleURI());
               throw new WLDeploymentException(var6.getMessage());
            }

            try {
               var3 = (MessageDestinationDescriptorBean)var4.lookup(var5);
            } catch (NamingException var8) {
               Loggable var7 = EJBLogger.logUnableToResolveMDBMessageDestinationLinkLoggable(this.messageDestinationLink, this.getEJBName(), var2.getModuleURI());
               throw new WLDeploymentException(var7.getMessage());
            }
         } catch (NamingException var9) {
            throw new WLDeploymentException("Error resolving message-destination-link", var9);
         }

         this.destinationResourceLink = var3.getDestinationResourceLink();
         if (this.destinationResourceLink == null) {
            String var10 = var3.getDestinationJNDIName();
            var10 = BaseEnvironmentBuilder.transformJNDIName(var10, var2.getApplicationName());
            this.m_destinationJNDIName = var10;
            this.m_initialContextFactory = var3.getInitialContextFactory();
            this.m_providerURL = var3.getProviderUrl();
         } else {
            this.m_destinationJNDIName = this.m_desc.resolveResourceLink(var2.getApplicationId(), this.destinationResourceLink);
         }
      }

   }

   public AuthenticatedSubject getRightSubject(String var1) throws NamingException {
      return (AuthenticatedSubject)CrossDomainSecurityManager.getCrossDomainSecurityUtil().getRemoteSubject(var1, (AbstractSubject)null);
   }

   private Destination lookupWithRightSubject(String var1) throws NamingException {
      final Context var2 = this.getInitialContext();
      return (Destination)this.doPrivilagedAction((AuthenticatedSubject)this.subject, new PrivilegedExceptionAction() {
         public Object run() throws NamingException {
            return var2.lookup(MessageDrivenBeanInfoImpl.this.m_destinationJNDIName);
         }
      });
   }

   private void postBeanManagerSetup(MessageDrivenManagerIntf var1) throws WLDeploymentException {
      if (this.isEJB30() && this.contextBoundCounter.getAndIncrement() < 1) {
         try {
            this.environmentContext.bind("comp/EJBContext", var1.getMessageDrivenContext());
         } catch (NamingException var3) {
            throw new WLDeploymentException("Error binding EJBContext: " + var3, var3);
         }
      }

   }

   public MigratableTargetMBean getMtMBean(String var1) {
      MigratableTargetMBean var2 = null;
      if (var1 != null) {
         DomainMBean var3 = ManagementService.getRuntimeAccess(kernelId).getDomain();
         var2 = var3.lookupMigratableTarget(var1);
      }

      if (var2 != null && var2.getServerNames() != null && !var2.getServerNames().contains(this.currServerName)) {
         var2 = null;
      }

      return var2;
   }

   public boolean isGenerateUniqueJmsClientId() {
      return this.isGenerateUniqueJmsClientId;
   }

   private void ensureMDBHasDestinationConfigured() throws WLDeploymentException {
      if (this.destinationResourceLink == null && this.messageDestinationLink == null && this.m_destinationJNDIName == null && this.resourceAdapterJndiName == null) {
         Loggable var1 = EJBLogger.logNoMdbDestinationConfiguredLoggable(this.getDisplayName());
         throw new WLDeploymentException(var1.getMessage());
      }
   }

   private void printDDMemberInfo(List<DestinationDetail> var1) {
      if (var1 != null) {
         int var2 = 0;

         for(Iterator var3 = var1.iterator(); var3.hasNext(); ++var2) {
            DestinationDetail var4 = (DestinationDetail)var3.next();
            debug("destination member[" + var2 + "]:" + var4.toString());
         }

      }
   }

   public void updateImplClassLoader() throws WLDeploymentException {
      super.updateImplClassLoader();
      ArrayList var1 = new ArrayList(this.mdManagerList);
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         MessageDrivenManagerIntf var3 = (MessageDrivenManagerIntf)var2.next();
         PoolIntf var4 = var3.getPool();
         var4.reset();
         ((MessageDrivenManager)var3).updateImplCL();
      }

   }

   public void updateJMSPollingIntervalSeconds(int var1) {
      this.jmsPollingIntervalSeconds = var1;
      ArrayList var2 = new ArrayList(this.mdManagerList);
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         MessageDrivenManagerIntf var4 = (MessageDrivenManagerIntf)var3.next();
         var4.updateJMSPollingIntervalSeconds(var1);
      }

      if (debugLogger.isDebugEnabled()) {
         debug("updated JMSPollingIntervalSeconds to " + var1 + " for EJB " + this.getDisplayName());
      }

   }

   public void updateMaxBeansInFreePool(int var1) {
      this.m_maxConcurrentInstances = var1;
      ArrayList var2 = new ArrayList(this.mdManagerList);
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         MessageDrivenManagerIntf var4 = (MessageDrivenManagerIntf)var3.next();
         PoolIntf var5 = var4.getPool();
         var5.updateMaxBeansInFreePool(var1);
      }

      if (debugLogger.isDebugEnabled()) {
         debug("updated MaxBeansInFreePool to " + var1 + " for EJB " + this.getDisplayName());
      }

   }

   private void resetMessageConsumers(List<MessageDrivenManagerIntf> var1, boolean var2) {
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         MessageDrivenManagerIntf var4 = (MessageDrivenManagerIntf)var3.next();
         var4.resetMessageConsumer(var2);
      }

   }

   public synchronized void undeployManagers(boolean var1) {
      ArrayList var2 = new ArrayList(this.mdManagerList);
      this.mdManagerList.clear();
      this.backupDestMembersList.clear();
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         MessageDrivenManagerIntf var4 = (MessageDrivenManagerIntf)var3.next();
         var4.undeploy();
      }

      MessageDrivenManagerIntf var6 = (MessageDrivenManagerIntf)this.defaultMDManager.getAndSet((Object)null);
      if (var6 != null) {
         var6.undeploy();
         var2.add(var6);
      }

      if (this.isDurableSubscriber() && var1) {
         Iterator var7 = var2.iterator();

         while(var7.hasNext()) {
            MessageDrivenManagerIntf var5 = (MessageDrivenManagerIntf)var7.next();
            var5.remove();
         }
      }

   }

   public void updatePoolIdleTimeoutSeconds(int var1) {
      ArrayList var2 = new ArrayList(this.mdManagerList);
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         MessageDrivenManagerIntf var4 = (MessageDrivenManagerIntf)var3.next();
         PoolIntf var5 = var4.getPool();
         var5.updateIdleTimeoutSeconds(var1);
      }

      if (debugLogger.isDebugEnabled()) {
         debug("updated Pool IdleTimeoutSeconds to " + var1 + " for EJB " + this.getDisplayName());
      }

   }

   public void prepareUpdate(BeanUpdateEvent var1) throws BeanUpdateRejectedException {
      if (debugLogger.isDebugEnabled()) {
         debug("prepareUpdate: " + var1);
      }

   }

   private void stopAndUnregisterJMS() {
      Iterator var1 = this.mdManagerList.iterator();

      while(var1.hasNext()) {
         MessageDrivenManagerIntf var2 = (MessageDrivenManagerIntf)var1.next();
         var2.stop();
         var2.setMDBStatus(DDConstants.MDBStatus[5]);
      }

      if (this.getIsWeblogicJMS()) {
         ((DestinationEventHandler)this.destinationHandler.get()).unRegister();
      }

      EJBLogger.logMDBInactive(this.getEJBName());
   }

   public void rollbackUpdate(BeanUpdateEvent var1) {
      if (debugLogger.isDebugEnabled()) {
         debug("rollbackUpdate: " + var1);
      }

   }

   public void activateUpdate(BeanUpdateEvent var1) throws BeanUpdateFailedException {
      if (debugLogger.isDebugEnabled()) {
         debug("activateUpdate: " + var1);
      }

      UpdateOperation var2 = MessageDrivenBeanInfoImpl.UpdateOperation.NOOPERATION;
      BeanUpdateEvent.PropertyUpdate[] var3 = var1.getUpdateList();
      DescriptorBean var4 = var1.getProposedBean();
      boolean var5 = this.topicMessagesDistributionMode == 2 || this.topicMessagesDistributionMode == 1;
      boolean var6 = false;
      int var7 = 0;

      while(var7 < var3.length) {
         BeanUpdateEvent.PropertyUpdate var8 = var3[var7];
         switch (var8.getUpdateType()) {
            case 1:
               String var9 = var8.getPropertyName();
               if (debugLogger.isDebugEnabled()) {
                  debug("Changing property of type: " + var9);
               }

               PoolBean var10;
               if (var9.equals("MaxBeansInFreePool")) {
                  var10 = (PoolBean)var4;
                  this.updateMaxBeansInFreePool(var10.getMaxBeansInFreePool());
               } else if (var9.equals("TransTimeoutSeconds")) {
                  TransactionDescriptorBean var25 = (TransactionDescriptorBean)var4;
                  this.updateTransactionTimeoutSeconds(var25.getTransTimeoutSeconds());
               } else if (var9.equals("InitSuspendSeconds")) {
                  this.initSuspendSeconds = ((MessageDrivenDescriptorBean)var4).getInitSuspendSeconds();
               } else if (var9.equals("MaxSuspendSeconds")) {
                  this.maxSuspendSeconds = ((MessageDrivenDescriptorBean)var4).getMaxSuspendSeconds();
               } else if (var9.equals("JmsPollingIntervalSeconds")) {
                  this.updateJMSPollingIntervalSeconds(((MessageDrivenDescriptorBean)var4).getJmsPollingIntervalSeconds());
               } else if (var9.equals("MaxMessagesInTransaction")) {
                  this.maxMessagesInTransaction = ((MessageDrivenDescriptorBean)var4).getMaxMessagesInTransaction();
                  var2 = MessageDrivenBeanInfoImpl.UpdateOperation.compareAndSet(MessageDrivenBeanInfoImpl.UpdateOperation.RECONNECTION, var2);
               } else if (var9.equals("IdleTimeoutSeconds")) {
                  var10 = (PoolBean)var4;
                  this.updatePoolIdleTimeoutSeconds(var10.getIdleTimeoutSeconds());
               } else if (var9.equals("ProviderUrl")) {
                  this.m_providerURL = ((MessageDrivenDescriptorBean)var4).getProviderUrl();
                  var2 = MessageDrivenBeanInfoImpl.UpdateOperation.compareAndSet(MessageDrivenBeanInfoImpl.UpdateOperation.REREGISTER, var2);
               } else if (var9.equals("DestinationJNDIName")) {
                  this.m_destinationJNDIName = ((MessageDrivenDescriptorBean)var4).getDestinationJNDIName();
                  var2 = MessageDrivenBeanInfoImpl.UpdateOperation.compareAndSet(MessageDrivenBeanInfoImpl.UpdateOperation.REREGISTER, var2);
               } else if (var9.equals("InitialContextFactory")) {
                  this.m_initialContextFactory = ((MessageDrivenDescriptorBean)var4).getInitialContextFactory();
                  var2 = MessageDrivenBeanInfoImpl.UpdateOperation.compareAndSet(MessageDrivenBeanInfoImpl.UpdateOperation.REREGISTER, var2);
               } else if (var9.equals("ConnectionFactoryJNDIName")) {
                  this.m_connectionFactoryJNDIName = ((MessageDrivenDescriptorBean)var4).getConnectionFactoryJNDIName();
                  var2 = MessageDrivenBeanInfoImpl.UpdateOperation.compareAndSet(MessageDrivenBeanInfoImpl.UpdateOperation.REREGISTER, var2);
               } else if (var9.equals("DestinationResourceLink")) {
                  this.destinationResourceLink = ((MessageDrivenDescriptorBean)var4).getDestinationResourceLink();
                  this.resolveDestinationResourceLink();
                  var2 = MessageDrivenBeanInfoImpl.UpdateOperation.compareAndSet(MessageDrivenBeanInfoImpl.UpdateOperation.REREGISTER, var2);
               } else if (var9.equals("ConnectionFactoryResourceLink")) {
                  this.connectionFactoryResourceLink = ((MessageDrivenDescriptorBean)var4).getConnectionFactoryResourceLink();
                  this.resolveConnectionFactoryResourceLink();
                  var2 = MessageDrivenBeanInfoImpl.UpdateOperation.compareAndSet(MessageDrivenBeanInfoImpl.UpdateOperation.REREGISTER, var2);
               } else if (var9.equals("JmsClientId")) {
                  this.jmsClientID = ((MessageDrivenDescriptorBean)var4).getJmsClientId();
                  var6 = this.needUnsubscribe(var5, var6);
                  var2 = MessageDrivenBeanInfoImpl.UpdateOperation.compareAndSet(MessageDrivenBeanInfoImpl.UpdateOperation.RECONNECTION, var2);
               } else if (var9.equals("GenerateUniqueJmsClientId")) {
                  this.isGenerateUniqueJmsClientId = ((MessageDrivenDescriptorBean)var4).isGenerateUniqueJmsClientId();
                  var2 = MessageDrivenBeanInfoImpl.UpdateOperation.compareAndSet(MessageDrivenBeanInfoImpl.UpdateOperation.RECONNECTION, var2);
               } else if (var9.equals("Use81StylePolling")) {
                  this.use81StylePolling = ((MessageDrivenDescriptorBean)var4).isUse81StylePolling();
                  var2 = MessageDrivenBeanInfoImpl.UpdateOperation.compareAndSet(MessageDrivenBeanInfoImpl.UpdateOperation.RECONNECTION, var2);
               } else if (var9.equals("DistributedDestinationConnection")) {
                  this.distributedDestinationConnection = this.convertToInt("distributedDestinationConnection", DDConstants.DISTRIBUTEDDESTINATIONCONNECTIONS, ((MessageDrivenDescriptorBean)var4).getDistributedDestinationConnection());
                  var2 = MessageDrivenBeanInfoImpl.UpdateOperation.compareAndSet(MessageDrivenBeanInfoImpl.UpdateOperation.REREGISTER, var2);
               } else if (var9.equals("DurableSubscriptionDeletion")) {
                  this.isDurableSubscriptionDeletion = ((MessageDrivenDescriptorBean)var4).isDurableSubscriptionDeletion();
                  var2 = MessageDrivenBeanInfoImpl.UpdateOperation.compareAndSet(MessageDrivenBeanInfoImpl.UpdateOperation.RECONNECTION, var2);
               } else if (var9.equals("TopicMessageDistributionMode")) {
                  this.topicMessagesDistributionMode = this.convertToInt("TOPICMESSAGESDISTRIBUTIONMODE", DDConstants.TOPICMESSAGEDISTRIBUTIONMODES, ((MessageDrivenDescriptorBean)var4).toString());
                  var6 = true;
                  var2 = MessageDrivenBeanInfoImpl.UpdateOperation.compareAndSet(MessageDrivenBeanInfoImpl.UpdateOperation.REREGISTER, var2);
               } else if (var9.equals("ResourceAdapterJNDIName")) {
                  this.resourceAdapterJndiName = ((MessageDrivenDescriptorBean)var4).getResourceAdapterJNDIName();
                  var2 = MessageDrivenBeanInfoImpl.UpdateOperation.compareAndSet(MessageDrivenBeanInfoImpl.UpdateOperation.REREGISTER, var2);
               } else {
                  if (!var9.equals("ActivationConfigPropertyValue")) {
                     throw new AssertionError("Unexpected propertyName: " + var9);
                  }

                  ActivationConfigPropertyBean var26 = (ActivationConfigPropertyBean)var4;
                  if (var26.getActivationConfigPropertyName().equalsIgnoreCase("MESSAGESELECTOR")) {
                     this.m_messageSelector = var26.getActivationConfigPropertyValue();
                     if (debugLogger.isDebugEnabled()) {
                        debug("updated MessageSelector to " + this.m_messageSelector + " for EJB " + this.getDisplayName());
                     }

                     var6 = this.needUnsubscribe(var5, var6);
                     var2 = MessageDrivenBeanInfoImpl.UpdateOperation.compareAndSet(MessageDrivenBeanInfoImpl.UpdateOperation.RECONNECTION, var2);
                  } else if (var26.getActivationConfigPropertyName().equalsIgnoreCase("TOPICMESSAGESDISTRIBUTIONMODE")) {
                     this.topicMessagesDistributionMode = this.convertToInt("TOPICMESSAGESDISTRIBUTIONMODE", DDConstants.TOPICMESSAGEDISTRIBUTIONMODES, var26.getActivationConfigPropertyValue());
                     var6 = true;
                     var2 = MessageDrivenBeanInfoImpl.UpdateOperation.REREGISTER;
                  } else {
                     if (!var26.getActivationConfigPropertyName().equalsIgnoreCase("INACTIVE")) {
                        throw new AssertionError("unexpected ActivationConfigProperty");
                     }

                     this.m_isInactive = Boolean.parseBoolean(var26.getActivationConfigPropertyValue());
                     if (!this.m_isInactive) {
                        if (this.getIsWeblogicJMS()) {
                           ((DestinationEventHandler)this.destinationHandler.get()).registerToJMS(this.jmsRegisterEnv);
                        } else {
                           try {
                              Iterator var11 = this.mdManagerList.iterator();

                              while(var11.hasNext()) {
                                 MessageDrivenManagerIntf var28 = (MessageDrivenManagerIntf)var11.next();
                                 this.mdManagerStart(var28);
                              }
                           } catch (WLDeploymentException var20) {
                              Loggable var12 = EJBLogger.logInactiveMDBStartFailLoggable(this.getEJBName(), var20);
                              EJBLogger.logStackTraceAndMessage(var12.getMessageText(), var20);
                              this.stopAndUnregisterJMS();
                              this.m_isInactive = true;
                              throw new BeanUpdateFailedException(var12.getMessage());
                           }
                        }

                        EJBLogger.logMDBActive(this.getEJBName());
                     } else {
                        this.stopAndUnregisterJMS();
                     }
                  }
               }
            default:
               ++var7;
               break;
            case 2:
            case 3:
               throw new AssertionError("Unexpected BeanUpdateEvent: " + var1);
         }
      }

      if (var2.equals(MessageDrivenBeanInfoImpl.UpdateOperation.REREGISTER)) {
         this.undeployManagers(var6);
         ClassLoader var21 = Thread.currentThread().getContextClassLoader();

         try {
            Thread.currentThread().setContextClassLoader(this.moduleCL);
            if (this.getIsWeblogicJMS()) {
               this.unRegister();
               if (this.destinationHandler.get() == null) {
                  throw new AssertionError("The MDB application does not exist at all");
               }
            } else {
               this.createJCAManager();
            }

            this.deployMessageDrivenBeans();
         } catch (Throwable var18) {
            EJBLogger.logStackTraceLoggable(var18);
         } finally {
            Thread.currentThread().setContextClassLoader(var21);
         }
      } else if (var2.equals(MessageDrivenBeanInfoImpl.UpdateOperation.RECONNECTION)) {
         ArrayList var22 = new ArrayList(this.mdManagerList);
         MessageDrivenManagerIntf var23 = (MessageDrivenManagerIntf)this.defaultMDManager.getAndSet((Object)null);
         if (var23 != null) {
            var22.add(var23);
         }

         Iterator var24 = var22.iterator();

         while(var24.hasNext()) {
            MessageDrivenManagerIntf var27 = (MessageDrivenManagerIntf)var24.next();
            var27.stop();
         }

         this.resetMessageConsumers(var22, var6);
      }

   }

   private boolean needUnsubscribe(boolean var1, boolean var2) {
      return var2 ? var2 : var1;
   }

   private static void debug(String var0) {
      debugLogger.debug("[MessageDrivenBeanInfoImpl] " + var0);
   }

   public String getDestinationResourceLink() {
      return this.destinationResourceLink;
   }

   public void setEJBComponentRuntime(EJBComponentRuntimeMBeanImpl var1) {
      this.ejbComponentRuntime = var1;
   }

   public Object doPrivilagedAction(AuthenticatedSubject var1, PrivilegedExceptionAction var2) throws NamingException {
      if (var1 == null || SecurityServiceManager.isKernelIdentity(var1) || SecurityServiceManager.isServerIdentity(var1) || this.getIsRemoteSubjectExists()) {
         var1 = this.getRightSubject(this.m_providerURL);
      }

      try {
         return var1.doAs(kernelId, var2);
      } catch (PrivilegedActionException var4) {
         if (var4.getCause() instanceof NamingException) {
            throw (NamingException)var4.getCause();
         } else {
            throw new AssertionError(var4);
         }
      }
   }

   private boolean isSet(String var1, Object var2) {
      return ((DescriptorBean)var2).isSet(var1);
   }

   public int convertToInt(String var1, String[] var2, String var3) {
      for(int var4 = 0; var4 < var2.length; ++var4) {
         if (var2[var4].equalsIgnoreCase(var3)) {
            return var4;
         }
      }

      return -1;
   }

   public int getDistributedDestinationConnection() {
      return this.distributedDestinationConnection;
   }

   public int getTopicMessagesDistributionMode() {
      return this.topicMessagesDistributionMode;
   }

   public void unRegister() {
      if (this.destinationHandler.get() != null) {
         ((DestinationEventHandler)this.destinationHandler.get()).unRegister();
      }

   }

   private void debugPropertyValue(String var1, String var2) {
      if (debugLogger.isDebugEnabled()) {
         debug("In the EJB " + this.getDisplayName() + ", the value for the activation config property '" + var1 + "' is '" + var2 + "'");
      }

   }

   protected MessageDrivenManagerIntf createMDManager(Context var1, MigratableTargetMBean var2, DestinationDetail var3) throws WLDeploymentException {
      Object var4 = null;
      if (debugLogger.isDebugEnabled()) {
         if (var3 != null && isDistributedDestination(var3)) {
            debug("Creating MDManager for member: " + var3.toString());
         } else {
            debug("Creating MDManager for destination: " + this.m_destinationJNDIName);
         }
      }

      this.setupBeanManager(this.ejbComponentRuntime);
      MessageDrivenManagerIntf var5 = this.getBeanManager();
      var5.setup(this, var1, this.m_destinationJNDIName, this.domainName, this.currServerName, this.jmsClientIDBase, var2, var3);
      this.postBeanManagerSetup(var5);
      return var5;
   }

   private static boolean isDistributedDestination(DestinationDetail var0) {
      return var0.getType() == 4 || var0.getType() == 6 || var0.getType() == 5;
   }

   public boolean getIsInactive() {
      return this.m_isInactive;
   }

   public WorkManager getCustomWorkManager() {
      return this.wm;
   }

   public MDBTimerManagerFactory getTimerManagerFactory() {
      return this.mdbTimerManagerFactory;
   }

   class QueueConnectionHandler extends DestinationResovler {
      public QueueConnectionHandler() {
         this(false);
      }

      public QueueConnectionHandler(boolean var2) {
         super();
         this.isDD = var2;
      }

      public boolean complianceCheck(List<DestinationDetail> var1) {
         if (MessageDrivenBeanInfoImpl.this.topicMessagesDistributionMode > 0) {
            EJBLogger.logInvalidConfigurationForTopicMessagesDistributionMode(MessageDrivenBeanInfoImpl.this.getDisplayName());
            return false;
         } else {
            if (MessageDrivenBeanInfoImpl.this.distributedDestinationConnection < 0) {
               MessageDrivenBeanInfoImpl.this.distributedDestinationConnection = 0;
            }

            return true;
         }
      }

      void handleDD(List<DestinationDetail> var1) {
         if (var1.size() >= 1) {
            DestinationDetail var2 = (DestinationDetail)var1.get(0);
            if (!var2.isLocalCluster()) {
               this.connectionMode = 1;
            } else if (MessageDrivenBeanInfoImpl.this.distributedDestinationConnection <= 0) {
               this.connectionMode = 2;
            } else {
               this.connectionMode = 1;
            }

            this.activateMDManagerList(this.createDDMDManagers(var1));
         }

      }

      void handleNoneDD(List<DestinationDetail> var1) {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            DestinationDetail var3 = (DestinationDetail)var2.next();
            this.activateNoneDDMDManager(var3);
         }

      }
   }

   class TopicConnectionHandler extends DestinationResovler {
      private boolean isPartitioned;

      public TopicConnectionHandler() {
         this(false, false);
      }

      public TopicConnectionHandler(boolean var2, boolean var3) {
         super();
         this.isPartitioned = false;
         this.isDD = var2;
         this.isPartitioned = var3;
      }

      public boolean complianceCheck(List<DestinationDetail> var1) {
         if (MessageDrivenBeanInfoImpl.this.topicMessagesDistributionMode < 0) {
            this.detectDefauleMode();
         }

         Iterator var2;
         DestinationDetail var3;
         if (MessageDrivenBeanInfoImpl.this.topicMessagesDistributionMode == 0) {
            if (this.isPartitioned) {
               EJBLogger.logIllegalPermutationOnPDTAndComp(MessageDrivenBeanInfoImpl.this.getDisplayName());
               return false;
            }

            if (MessageDrivenBeanInfoImpl.this.isDurableSubscriber()) {
               var2 = var1.iterator();

               while(var2.hasNext()) {
                  var3 = (DestinationDetail)var2.next();
                  if (var3.getType() == 5 && !var3.isLocalCluster()) {
                     EJBLogger.logIllegalSubscriptionOnDurRemoteRDT(MessageDrivenBeanInfoImpl.this.getDisplayName(), MessageDrivenBeanInfoImpl.this.getDestinationName());
                     return false;
                  }
               }
            }

            if (MessageDrivenBeanInfoImpl.this.distributedDestinationConnection < 0) {
               MessageDrivenBeanInfoImpl.this.distributedDestinationConnection = 0;
            }
         } else {
            var2 = var1.iterator();

            while(var2.hasNext()) {
               var3 = (DestinationDetail)var2.next();
               if (var3.getType() == 3 || !var3.isAdvancedTopicSupported()) {
                  EJBLogger.logInvalidConfigurationForPre1033(MessageDrivenBeanInfoImpl.this.getDisplayName(), "topicMessagesDistributionMode");
                  return false;
               }
            }
         }

         return true;
      }

      void handleDD(List<DestinationDetail> var1) {
         if (var1.size() >= 1) {
            DestinationDetail var2 = (DestinationDetail)var1.get(0);
            if (MessageDrivenBeanInfoImpl.this.topicMessagesDistributionMode == 0) {
               if (!var2.isLocalCluster()) {
                  this.connectionMode = 3;
                  if (BeanInfoImpl.debugLogger.isDebugEnabled()) {
                     MessageDrivenBeanInfoImpl.debug("The JMS destination at the JNDI name " + var2.getJNDIName() + " is a remote Distributed Topic and '" + DDConstants.TOPICMESSAGEDISTRIBUTIONMODES[MessageDrivenBeanInfoImpl.this.topicMessagesDistributionMode] + "' is configured so a connection will be made to one member of the Distributed Topic.");
                  }
               } else {
                  this.connectionMode = 4;
                  if (BeanInfoImpl.debugLogger.isDebugEnabled()) {
                     MessageDrivenBeanInfoImpl.debug("The JMS destination at the JNDI name " + var2.getJNDIName() + " is a local Distributed Topic and '" + DDConstants.TOPICMESSAGEDISTRIBUTIONMODES[MessageDrivenBeanInfoImpl.this.topicMessagesDistributionMode] + "' is configured so a connection will be made to every member of the Distributed Topic.");
                  }
               }
            } else if (!var2.isLocalCluster()) {
               if (!MessageDrivenBeanInfoImpl.this.isDurableSubscriber() && var2.getType() == 5 && MessageDrivenBeanInfoImpl.this.topicMessagesDistributionMode == 1) {
                  this.connectionMode = 3;
                  MessageDrivenBeanInfoImpl.this.distributedDestinationConnection = 0;
               } else {
                  this.connectionMode = 1;
                  MessageDrivenBeanInfoImpl.this.distributedDestinationConnection = 1;
               }
            } else if (MessageDrivenBeanInfoImpl.this.distributedDestinationConnection == 1) {
               if (BeanInfoImpl.debugLogger.isDebugEnabled()) {
                  MessageDrivenBeanInfoImpl.debug("The JMS destination at the JNDI name " + var2.getJNDIName() + " is a local Distributed Topic and '" + DDConstants.TOPICMESSAGEDISTRIBUTIONMODES[MessageDrivenBeanInfoImpl.this.topicMessagesDistributionMode] + "' is configured so a connection will be made to every member of the Distributed Topic.");
               }

               this.connectionMode = 1;
            } else if (MessageDrivenBeanInfoImpl.this.distributedDestinationConnection == 0) {
               if (this.isPartitioned && MessageDrivenBeanInfoImpl.this.topicMessagesDistributionMode == 1) {
                  EJBLogger.logOverridenLocalOnlyWithEveryMember(MessageDrivenBeanInfoImpl.this.getDisplayName());
                  this.connectionMode = 1;
               } else {
                  if (BeanInfoImpl.debugLogger.isDebugEnabled()) {
                     MessageDrivenBeanInfoImpl.debug("The JMS destination at the JNDI name " + var2.getJNDIName() + " is a local Distributed Topic and '" + DDConstants.TOPICMESSAGEDISTRIBUTIONMODES[MessageDrivenBeanInfoImpl.this.topicMessagesDistributionMode] + "' is configured so a connection will be made to every member on local server.");
                  }

                  this.connectionMode = 2;
               }
            } else if (2 == MessageDrivenBeanInfoImpl.this.topicMessagesDistributionMode) {
               if (BeanInfoImpl.debugLogger.isDebugEnabled()) {
                  MessageDrivenBeanInfoImpl.debug("The JMS Destination " + var2.getJNDIName() + " is replicated distributed topic, so 'EveryMember' is the defaule value for 'DistributionConnection'");
               }

               this.connectionMode = 2;
            } else if (1 == MessageDrivenBeanInfoImpl.this.topicMessagesDistributionMode) {
               if (this.isPartitioned) {
                  if (BeanInfoImpl.debugLogger.isDebugEnabled()) {
                     MessageDrivenBeanInfoImpl.debug("The JMS Destination " + var2.getJNDIName() + " is partitioned distributed topic, so 'EveryMember' is the defaule value for 'DistributionConnection'");
                  }

                  this.connectionMode = 1;
               } else {
                  if (BeanInfoImpl.debugLogger.isDebugEnabled()) {
                     MessageDrivenBeanInfoImpl.debug("The JMS Destination " + var2.getJNDIName() + " is replicated distributed topic, so 'EveryMember' is the defaule value for 'DistributionConnection'");
                  }

                  this.connectionMode = 2;
               }
            }

            this.activateMDManagerList(this.createDDMDManagers(var1));
         }

      }

      void handleNoneDD(List<DestinationDetail> var1) {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            DestinationDetail var3 = (DestinationDetail)var2.next();
            this.activateNoneDDMDManager(var3);
         }

      }

      protected void undeployAndActivate(MessageDrivenManagerIntf var1) {
         if (this.isDD && !this.isPartitioned) {
            ArrayList var2 = new ArrayList(MessageDrivenBeanInfoImpl.this.backupDestMembersList);
            if (this.connectionMode != 3 && this.connectionMode != 4) {
               var1.undeploy();
               if (this.connectionMode == 2 && var2.size() > 0) {
                  this.activateBackupMemeber(var2);
               }
            } else {
               if (this.releaseOne()) {
                  var1.undeploy();
               } else {
                  MessageDrivenBeanInfoImpl.debug("duplicate remove event on destination " + var1.getDDMemberName());
               }

               if (var2.size() > 0) {
                  this.activateBackupMemeber(var2);
               }
            }
         } else {
            var1.undeploy();
         }

      }

      private void activateBackupMemeber(List<DestinationDetail> var1) {
         MigratableTargetMBean var2 = null;
         MessageDrivenManagerIntf var3 = null;

         try {
            DestinationDetail var4;
            if (this.connectionMode == 2) {
               var4 = (DestinationDetail)var1.remove(0);
               var2 = MessageDrivenBeanInfoImpl.this.getMtMBean(var4.getMigratableTargetName());
               var3 = MessageDrivenBeanInfoImpl.this.createMDManager(MessageDrivenBeanInfoImpl.this.environmentContext, var2, var4);
            } else if (this.connectionMode != 3) {
               if (this.connectionMode == 4) {
                  var4 = (DestinationDetail)var1.remove(0);
                  var2 = MessageDrivenBeanInfoImpl.this.getMtMBean(var4.getMigratableTargetName());
                  if (var2 == null) {
                     synchronized(this) {
                        if (this.createOne()) {
                           var3 = MessageDrivenBeanInfoImpl.this.createMDManager(MessageDrivenBeanInfoImpl.this.environmentContext, var2, var4);
                        }
                     }
                  } else {
                     var3 = MessageDrivenBeanInfoImpl.this.createMDManager(MessageDrivenBeanInfoImpl.this.environmentContext, var2, var4);
                  }
               }
            } else {
               var4 = null;

               for(int var5 = 0; var5 < var1.size(); ++var5) {
                  var4 = (DestinationDetail)var1.get(var5);
                  if (var4.isLocalWLSServer()) {
                     break;
                  }
               }

               var1.remove(var4);
               var2 = MessageDrivenBeanInfoImpl.this.getMtMBean(var4.getMigratableTargetName());
               synchronized(this) {
                  if (this.createOne()) {
                     var3 = MessageDrivenBeanInfoImpl.this.createMDManager(MessageDrivenBeanInfoImpl.this.environmentContext, var2, var4);
                  }
               }
            }

            if (var3 != null) {
               try {
                  MessageDrivenBeanInfoImpl.this.mdManagerStart(var3);
                  MessageDrivenBeanInfoImpl.this.mdManagerList.add(var3);
               } catch (Exception var8) {
                  EJBLogger.logStackTrace(var8);
               }
            }
         } catch (WLDeploymentException var11) {
         }

      }

      private void detectDefauleMode() {
         if (this.isPartitioned) {
            MessageDrivenBeanInfoImpl.this.topicMessagesDistributionMode = 2;
            if (BeanInfoImpl.debugLogger.isDebugEnabled()) {
               MessageDrivenBeanInfoImpl.debug("Activation config proerty TOPICMESSAGESDISTRIBUTIONMODE on EJB '" + MessageDrivenBeanInfoImpl.this.getDisplayName() + " is not specified, default value is " + DDConstants.TOPICMESSAGEDISTRIBUTIONMODES[MessageDrivenBeanInfoImpl.this.topicMessagesDistributionMode]);
            }
         } else {
            MessageDrivenBeanInfoImpl.this.topicMessagesDistributionMode = 0;
            if (BeanInfoImpl.debugLogger.isDebugEnabled()) {
               MessageDrivenBeanInfoImpl.debug("Activation config proerty TOPICMESSAGESDISTRIBUTIONMODE on EJB " + MessageDrivenBeanInfoImpl.this.getDisplayName() + " is not specified, the default value is " + DDConstants.TOPICMESSAGEDISTRIBUTIONMODES[MessageDrivenBeanInfoImpl.this.topicMessagesDistributionMode]);
            }
         }

      }
   }

   abstract class DestinationResovler {
      boolean isDD = false;
      protected int connectionMode;
      protected static final int EVERYMEMBER_MODE = 1;
      protected static final int LOCALMEMBER_MODE = 2;
      protected static final int ONLYONEOF_MODE = 3;
      protected static final int LOCALMEMBER_PRE10_3_3_MODE = 4;
      protected boolean activedOne = false;

      protected boolean createOne() {
         if (!this.activedOne) {
            this.activedOne = true;
            return true;
         } else {
            return false;
         }
      }

      protected synchronized boolean releaseOne() {
         if (this.activedOne) {
            this.activedOne = false;
            return true;
         } else {
            return false;
         }
      }

      boolean resolveDestnationWorkMode(List<DestinationDetail> var1) {
         if (!this.complianceCheck(var1)) {
            return false;
         } else {
            ClassLoader var2 = Thread.currentThread().getContextClassLoader();

            try {
               EJBRuntimeUtils.pushEnvironment(MessageDrivenBeanInfoImpl.this.environmentContext);
               Thread.currentThread().setContextClassLoader(MessageDrivenBeanInfoImpl.this.getClassLoader());
               if (this.isDD) {
                  this.handleDD(var1);
               } else {
                  this.handleNoneDD(var1);
               }
            } finally {
               EJBRuntimeUtils.popEnvironment();
               Thread.currentThread().setContextClassLoader(var2);
            }

            return true;
         }
      }

      abstract boolean complianceCheck(List<DestinationDetail> var1);

      abstract void handleDD(List<DestinationDetail> var1);

      abstract void handleNoneDD(List<DestinationDetail> var1);

      void activateMDManagerList(List<MessageDrivenManagerIntf> var1) {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            MessageDrivenManagerIntf var3 = (MessageDrivenManagerIntf)var2.next();

            try {
               var3.setMDBStatus(DDConstants.MDBStatus[1]);
               MessageDrivenBeanInfoImpl.this.mdManagerStart(var3);
            } catch (Exception var5) {
               EJBLogger.logStackTrace(var5);
            }
         }

         MessageDrivenBeanInfoImpl.this.mdManagerList.addAll(var1);
         MessageDrivenManagerIntf var6 = (MessageDrivenManagerIntf)MessageDrivenBeanInfoImpl.this.defaultMDManager.getAndSet((Object)null);
         if (var6 != null) {
            var6.undeploy();
         }

      }

      private MessageDrivenManagerIntf createDefaultMDManager() {
         Object var1 = null;
         MessageDrivenManagerIntf var2 = null;

         try {
            var2 = MessageDrivenBeanInfoImpl.this.createMDManager(MessageDrivenBeanInfoImpl.this.environmentContext, (MigratableTargetMBean)null, (DestinationDetail)null);
            var2.setMDBStatus(DDConstants.MDBStatus[0]);
         } catch (WLDeploymentException var4) {
         }

         return var2;
      }

      protected void activateNoneDDMDManager(DestinationDetail var1) {
         Object var2 = null;
         MessageDrivenManagerIntf var3 = (MessageDrivenManagerIntf)MessageDrivenBeanInfoImpl.this.defaultMDManager.getAndSet((Object)null);
         if (var3 == null) {
            throw new AssertionError("no creating MDB manager in deployment");
         } else {
            try {
               var3.enableDestination(var1);
               var3.setMDBStatus(DDConstants.MDBStatus[1]);
               boolean var4 = ((MessageDrivenManager)var3).isNoneDDMD();
               if (var4 && var2 != null) {
                  MigrationManager.singleton().register((Migratable)var3, (MigratableTargetMBean)var2);
               } else {
                  var3.start();
                  var3.setIsDeployed(true);
               }
            } catch (Exception var7) {
               EJBLogger.logStackTraceAndMessage("Error in starting the MDB " + MessageDrivenBeanInfoImpl.this.getName(), var7);
            }

            TimerManager var8 = var3.getTimerManager();
            if (var8 != null) {
               try {
                  var8.perhapsStart();
               } catch (Exception var6) {
                  EJBLogger.logStackTrace(var6);
               }
            }

            if (!MessageDrivenBeanInfoImpl.this.mdManagerList.contains(var3)) {
               MessageDrivenBeanInfoImpl.this.mdManagerList.add(var3);
            }

         }
      }

      private boolean filterMultipleJMSServer(DestinationDetail var1, List<MessageDrivenManagerIntf> var2) {
         if (var1.getType() == 5 && MessageDrivenBeanInfoImpl.this.distributedDestinationConnection != 1 && MessageDrivenBeanInfoImpl.this.topicMessagesDistributionMode != 2) {
            Iterator var3 = var2.iterator();
            MessageDrivenManagerIntf var4;
            if (var3.hasNext()) {
               var4 = (MessageDrivenManagerIntf)var3.next();
               var1.getWLSServerName().equals(var4.getDestination().getWLSServerName());
               return true;
            }

            var3 = MessageDrivenBeanInfoImpl.this.mdManagerList.iterator();
            if (var3.hasNext()) {
               var4 = (MessageDrivenManagerIntf)var3.next();
               var1.getWLSServerName().equals(var4.getDestination().getWLSServerName());
               return true;
            }
         }

         return false;
      }

      protected void removeMDmanagers(List<DestinationDetail> var1) {
         ArrayList var2 = new ArrayList(MessageDrivenBeanInfoImpl.this.mdManagerList);
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            DestinationDetail var4 = (DestinationDetail)var3.next();
            Iterator var5 = var2.iterator();

            while(var5.hasNext()) {
               MessageDrivenManagerIntf var6 = (MessageDrivenManagerIntf)var5.next();
               if (var6.getDDMemberName().equals(var4.getMemberConfigName())) {
                  if (BeanInfoImpl.debugLogger.isDebugEnabled()) {
                     MessageDrivenBeanInfoImpl.debug("The EJB " + MessageDrivenBeanInfoImpl.this.getDisplayName() + " removes MDManager for connection to destination " + var6.getDestinationName());
                  }

                  this.undeployAndActivate(var6);
               }
            }
         }

         if (MessageDrivenBeanInfoImpl.this.mdManagerList.size() == 0) {
            MessageDrivenBeanInfoImpl.this.defaultMDManager.compareAndSet((Object)null, this.createDefaultMDManager());
         }

      }

      protected void undeployAndActivate(MessageDrivenManagerIntf var1) {
         var1.undeploy();
      }

      protected List<MessageDrivenManagerIntf> createDDMDManagers(List<DestinationDetail> var1) {
         MigratableTargetMBean var2 = null;
         ArrayList var3 = new ArrayList();

         try {
            MessageDrivenManagerIntf var4;
            Iterator var7;
            DestinationDetail var8;
            Iterator var14;
            DestinationDetail var15;
            switch (this.connectionMode) {
               case 1:
                  var14 = var1.iterator();

                  while(var14.hasNext()) {
                     var15 = (DestinationDetail)var14.next();
                     var2 = MessageDrivenBeanInfoImpl.this.getMtMBean(var15.getMigratableTargetName());
                     var4 = MessageDrivenBeanInfoImpl.this.createMDManager(MessageDrivenBeanInfoImpl.this.environmentContext, var2, var15);
                     var3.add(var4);
                  }

                  return var3;
               case 2:
                  var14 = var1.iterator();

                  while(var14.hasNext()) {
                     var15 = (DestinationDetail)var14.next();
                     if (var15.isLocalWLSServer()) {
                        if (!this.filterMultipleJMSServer(var15, var3)) {
                           var2 = MessageDrivenBeanInfoImpl.this.getMtMBean(var15.getMigratableTargetName());
                           var4 = MessageDrivenBeanInfoImpl.this.createMDManager(MessageDrivenBeanInfoImpl.this.environmentContext, var2, var15);
                           var3.add(var4);
                        } else {
                           MessageDrivenBeanInfoImpl.this.backupDestMembersList.add(var15);
                        }
                     }
                  }

                  return var3;
               case 3:
                  DestinationDetail var5 = null;
                  ArrayList var6 = new ArrayList(var1);
                  var7 = var6.iterator();

                  while(var7.hasNext()) {
                     var8 = (DestinationDetail)var7.next();
                     var5 = var8;
                     if (var8.isLocalWLSServer()) {
                        break;
                     }
                  }

                  synchronized(this) {
                     if (this.createOne()) {
                        var6.remove(var5);
                        var2 = MessageDrivenBeanInfoImpl.this.getMtMBean(var5.getMigratableTargetName());
                        var3.add(MessageDrivenBeanInfoImpl.this.createMDManager(MessageDrivenBeanInfoImpl.this.environmentContext, var2, var5));
                     }
                  }

                  MessageDrivenBeanInfoImpl.this.backupDestMembersList.addAll(var6);
                  break;
               case 4:
                  var7 = var1.iterator();

                  while(var7.hasNext()) {
                     var8 = (DestinationDetail)var7.next();
                     var2 = MessageDrivenBeanInfoImpl.this.getMtMBean(var8.getMigratableTargetName());
                     if (var2 != null) {
                        var3.add(MessageDrivenBeanInfoImpl.this.createMDManager(MessageDrivenBeanInfoImpl.this.environmentContext, var2, var8));
                     } else {
                        synchronized(this) {
                           if (this.createOne()) {
                              var3.add(MessageDrivenBeanInfoImpl.this.createMDManager(MessageDrivenBeanInfoImpl.this.environmentContext, var2, var8));
                           } else {
                              MessageDrivenBeanInfoImpl.this.backupDestMembersList.add(var8);
                           }
                        }
                     }
                  }
            }
         } catch (WLDeploymentException var13) {
            var13.printStackTrace();
         }

         return var3;
      }
   }

   class DestinationEventHandler implements DestinationAvailabilityListener {
      private RegistrationHandle handle;
      private DestinationResovler resolver;

      public void onDestinationsAvailable(String var1, List<DestinationDetail> var2) {
         if (BeanInfoImpl.debugLogger.isDebugEnabled()) {
            MessageDrivenBeanInfoImpl.debug("New DDMember Available Event, JMS destination on " + var1 + " is available");
            MessageDrivenBeanInfoImpl.this.printDDMemberInfo(var2);
         }

         if (var2 != null && var2.size() > 0) {
            if (this.resolver == null) {
               synchronized(this) {
                  if (this.resolver == null) {
                     DestinationDetail var4 = (DestinationDetail)var2.get(0);
                     int var5 = var4.getType();
                     switch (var5) {
                        case 0:
                        case 2:
                           this.resolver = MessageDrivenBeanInfoImpl.this.new QueueConnectionHandler();
                           break;
                        case 1:
                        case 3:
                           this.resolver = MessageDrivenBeanInfoImpl.this.new TopicConnectionHandler();
                           break;
                        case 4:
                           this.resolver = MessageDrivenBeanInfoImpl.this.new QueueConnectionHandler(true);
                           break;
                        case 5:
                           this.resolver = MessageDrivenBeanInfoImpl.this.new TopicConnectionHandler(true, false);
                           break;
                        case 6:
                           this.resolver = MessageDrivenBeanInfoImpl.this.new TopicConnectionHandler(true, true);
                     }
                  }
               }
            }

            if (this.resolver != null && !this.resolver.resolveDestnationWorkMode(var2)) {
               this.unRegister();
            }
         }

      }

      public void onDestinationsUnavailable(String var1, List<DestinationDetail> var2) {
         if (BeanInfoImpl.debugLogger.isDebugEnabled()) {
            MessageDrivenBeanInfoImpl.debug("DDMember unavailable event on destination [" + var1 + "]");
            MessageDrivenBeanInfoImpl.this.printDDMemberInfo(var2);
         }

         if (var2 != null && var2.size() > 0) {
            ArrayList var3 = new ArrayList();
            ArrayList var4 = new ArrayList(MessageDrivenBeanInfoImpl.this.backupDestMembersList);
            Iterator var5 = var2.iterator();

            while(true) {
               while(var5.hasNext()) {
                  DestinationDetail var6 = (DestinationDetail)var5.next();
                  Iterator var7 = var4.iterator();

                  while(var7.hasNext()) {
                     DestinationDetail var8 = (DestinationDetail)var7.next();
                     if (var8.getMemberConfigName().equals(var6.getMemberConfigName())) {
                        if (BeanInfoImpl.debugLogger.isDebugEnabled()) {
                           MessageDrivenBeanInfoImpl.debug("The distributed destination member [" + var6.getMemberConfigName() + "] has been removed");
                        }

                        var3.add(var8);
                        break;
                     }
                  }
               }

               MessageDrivenBeanInfoImpl.this.backupDestMembersList.removeAll(var3);
               this.resolver.removeMDmanagers(var2);
               break;
            }
         }

      }

      public void onFailure(String var1, Exception var2) {
         if (!BeanInfoImpl.debugLogger.isDebugEnabled() && var2 instanceof NameNotFoundException) {
            EJBLogger.logMDBUnableToConnectToJMS(MessageDrivenBeanInfoImpl.this.getEJBName(), var1, "The destination for the MDB " + MessageDrivenBeanInfoImpl.this.displayName + " could not be resolved at this time.  Please ensure the destination is available at the JNDI name " + var1 + ".  The EJB container will periodically attempt to resolve this MDB destination and additional warnings may be issued.");
         } else {
            EJBLogger.logMDBUnableToConnectToJMS(MessageDrivenBeanInfoImpl.this.getEJBName(), var1, StackTraceUtils.throwable2StackTrace(var2));
         }

      }

      public void register() throws WLDeploymentException {
         String var1 = MessageDrivenBeanInfoImpl.this.m_destinationJNDIName;
         if (MessageDrivenBeanInfoImpl.this.isOnMessageTransacted()) {
            MessageDrivenBeanInfoImpl.this.m_acknowledgeMode = 2;
         }

         if (var1 == null && MessageDrivenBeanInfoImpl.this.messageDestinationLink == null) {
            Loggable var10 = EJBLogger.logNoDestinationJNDINameSpecifiedLoggable();
            throw new WLDeploymentException(var10.getMessage());
         } else {
            MessageDrivenBeanInfoImpl.this.reSetUsernameAndPassword();
            MessageDrivenBeanInfoImpl.this.jmsRegisterEnv.put("java.naming.factory.initial", MessageDrivenBeanInfoImpl.this.m_initialContextFactory);
            MessageDrivenBeanInfoImpl.this.jmsRegisterEnv.put("weblogic.jndi.allowGlobalResourceLookup", "true");
            if (null != MessageDrivenBeanInfoImpl.this.m_providerURL) {
               MessageDrivenBeanInfoImpl.this.jmsRegisterEnv.put("java.naming.provider.url", MessageDrivenBeanInfoImpl.this.m_providerURL);
            }

            if (MessageDrivenBeanInfoImpl.this.hasCredentials) {
               MessageDrivenBeanInfoImpl.this.jmsRegisterEnv.put("java.naming.security.principal", MessageDrivenBeanInfoImpl.this.userName.toString());
               MessageDrivenBeanInfoImpl.this.jmsRegisterEnv.put("java.naming.security.credentials", MessageDrivenBeanInfoImpl.this.password.toString());
               this.register(MessageDrivenBeanInfoImpl.this.jmsRegisterEnv);
            } else {
               AuthenticatedSubject var2 = null;

               try {
                  var2 = MessageDrivenBeanInfoImpl.this.getRunAsSubject();
                  if (var2 == null) {
                     var2 = SecurityServiceManager.getCurrentSubject(MessageDrivenBeanInfoImpl.kernelId);
                  }

                  if (var2 == null || SecurityServiceManager.isKernelIdentity(var2) || SecurityServiceManager.isServerIdentity(var2) || MessageDrivenBeanInfoImpl.this.getIsRemoteSubjectExists()) {
                     var2 = MessageDrivenBeanInfoImpl.this.getRightSubject(MessageDrivenBeanInfoImpl.this.m_providerURL);
                  }
               } catch (Exception var9) {
                  EJBLogger.logStackTrace(var9);
               }

               try {
                  SecurityHelper.pushRunAsSubject(var2);
                  this.register(MessageDrivenBeanInfoImpl.this.jmsRegisterEnv);
               } finally {
                  SecurityHelper.popRunAsSubject();
               }
            }

            if (BeanInfoImpl.debugLogger.isDebugEnabled()) {
               MessageDrivenBeanInfoImpl.debug("Deploying JMS based MD bean, dest:" + var1 + " transacted:" + MessageDrivenBeanInfoImpl.this.isOnMessageTransacted());
            }

         }
      }

      private void register(Hashtable var1) throws WLDeploymentException {
         try {
            if (BeanInfoImpl.debugLogger.isDebugEnabled()) {
               MessageDrivenBeanInfoImpl.debug("Registering into JMS destination helper");
            }

            try {
               MessageDrivenBeanInfoImpl.this.defaultMDManager.set(MessageDrivenBeanInfoImpl.this.createMDManager(MessageDrivenBeanInfoImpl.this.environmentContext, (MigratableTargetMBean)null, (DestinationDetail)null));
               ((MessageDrivenManagerIntf)MessageDrivenBeanInfoImpl.this.defaultMDManager.get()).setMDBStatus(DDConstants.MDBStatus[0]);
            } catch (WLDeploymentException var3) {
            }

            if (MessageDrivenBeanInfoImpl.this.m_isInactive) {
               ((MessageDrivenManagerIntf)MessageDrivenBeanInfoImpl.this.defaultMDManager.get()).setMDBStatus(DDConstants.MDBStatus[5]);
            } else {
               this.registerToJMS(var1);
            }
         } catch (Exception var4) {
            throw new WLDeploymentException("Error registering DestinationAvailabilityListener", var4);
         }
      }

      private void registerToJMS(Hashtable var1) {
         this.handle = JMSDestinationAvailabilityHelper.getInstance().register(var1, MessageDrivenBeanInfoImpl.this.m_destinationJNDIName, this);
      }

      public void unRegister() {
         this.resolver = null;
         if (this.handle != null) {
            this.handle.unregister();
         }

      }
   }

   private static enum UpdateOperation {
      NOOPERATION,
      RECONNECTION,
      REREGISTER;

      private static UpdateOperation compareAndSet(UpdateOperation var0, UpdateOperation var1) {
         return var1.ordinal() < var0.ordinal() ? var0 : var1;
      }
   }
}
