package weblogic.jms.frontend;

import java.rmi.NoSuchObjectException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.List;
import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.event.EventContext;
import javax.naming.event.NamingEvent;
import javax.naming.event.NamingExceptionEvent;
import javax.naming.event.ObjectChangeListener;
import weblogic.application.ModuleException;
import weblogic.common.internal.PeerInfo;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.j2ee.descriptor.wl.JMSConnectionFactoryBean;
import weblogic.jms.JMSLogger;
import weblogic.jms.JMSService;
import weblogic.jms.client.JMSConnection;
import weblogic.jms.client.JMSConnectionFactory;
import weblogic.jms.client.JMSXAConnection;
import weblogic.jms.client.JMSXAConnectionFactory;
import weblogic.jms.client.WLConnectionImpl;
import weblogic.jms.common.BeanHelper;
import weblogic.jms.common.JMSConstants;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSServerUtilities;
import weblogic.jms.dispatcher.DispatcherWrapper;
import weblogic.jms.dispatcher.InvocableManagerDelegate;
import weblogic.jms.dispatcher.JMSDispatcher;
import weblogic.jms.dispatcher.JMSDispatcherManager;
import weblogic.jms.module.JMSBeanHelper;
import weblogic.jms.module.JMSModuleManagedEntity;
import weblogic.management.ManagementException;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.utils.GenericBeanListener;
import weblogic.messaging.common.PrivilegedActionUtilities;
import weblogic.messaging.dispatcher.DispatcherException;
import weblogic.rmi.server.UnicastRemoteObject;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.utils.StringUtils;

public final class FEConnectionFactory implements JMSModuleManagedEntity, ObjectChangeListener {
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final String EMPTY_STRING = "";
   public static final String DEFAULT_DELIVERY_MODE_STRING = "Persistent";
   public static final int DEFAULT_DELIVERY_MODE = 2;
   public static final int DEFAULT_PRIORITY = 4;
   public static final String DEFAULT_TIME_TO_DELIVER_STRING = "0";
   public static final long DEFAULT_TIME_TO_DELIVER = 0L;
   public static final long DEFAULT_TIME_TO_LIVE = 0L;
   public static final long DEFAULT_SEND_TIMEOUT = 10L;
   public static final long DEFAULT_REDELIVERY_DELAY = 0L;
   public static final int DEFAULT_TRANSACTION_TIMEOUT = 3600;
   public static final int DEFAULT_MESSAGES_MAXIMUM = 10;
   public static final boolean DEFAULT_USER_TRANSACTIONS_ENABLED = true;
   public static final String DEFAULT_MESSAGE_OVERRUN_POLICY_STRING = "KeepOld";
   public static final int DEFAULT_MESSAGE_OVERRUN_POLICY = 0;
   public static final int DEFAULT_ACKNOWLEDGE_POLICY = 1;
   public static final int DEFAULT_FLOW_MINIMUM = 50;
   public static final int DEFAULT_FLOW_MAXIMUM = 500;
   public static final int DEFAULT_FLOW_INTERVAL = 60;
   public static final int DEFAULT_FLOW_STEPS = 10;
   public static final int DEFAULT_CLIENT_ID_POLICY = 0;
   public static final int DEFAULT_SAF_TX_TIMEOUT = Integer.getInteger("weblogic.SAFTXTimeout", 3600);
   private transient GenericBeanListener connectionFactoryBeanListener;
   private transient GenericBeanListener defaultDeliveryParamsBeanListener;
   private transient GenericBeanListener clientParamsBeanListener;
   private transient GenericBeanListener transactionParamsBeanListener;
   private transient GenericBeanListener flowControlParamsBeanListener;
   private transient GenericBeanListener loadBalancingParamsBeanListener;
   private transient GenericBeanListener securityParamsBeanListener;
   private String fullyQualifiedName;
   private JMSConnectionFactory globalBoundConnectionFactory;
   private JMSConnectionFactory localBoundConnectionFactory;
   private JMSConnectionFactory appBoundConnectionFactory;
   private JMSConnectionFactoryBean connectionFactoryBean;
   private boolean bound;
   private boolean localBound;
   private boolean applicationBound;
   private boolean defaultConnectionFactory;
   private final FrontEnd frontEnd;
   private String name;
   private String moduleName;
   private String EARModuleName;
   private String jndiName;
   private String localJNDIName;
   private Context EARNamingContext;
   private Object jndiNameRebindLock;
   private boolean didUnbind;
   private boolean defaultTargetingEnabled;
   private String deliveryMode;
   private int priority;
   private String timeToDeliver;
   private long timeToLive;
   private long sendTimeout;
   private long redeliveryDelay;
   private long transactionTimeout;
   private boolean xaConnectionFactoryEnabled;
   private String clientId;
   private String clientIdPolicy;
   private String subscriptionSharingPolicy;
   private boolean allowCloseInOnMessage;
   private int messagesMaximum;
   private String overrunPolicy;
   private String acknowledgePolicy;
   private int compressionThreshold;
   private boolean flowControlEnabled;
   private int flowMinimum;
   private int flowMaximum;
   private int flowSteps;
   private int flowInterval;
   private boolean loadBalancingEnabled;
   private boolean serverAffinityEnabled;
   private boolean attachJMSXUserID;
   private String synchronousPrefetchMode;
   private String oneWaySendMode;
   private int oneWaySendWindowSize;
   private String reconnectPolicy;
   private long reconnectBlockingMillis;
   private long totalReconnectPeriodMillis;
   private String defaultUnitOfOrder;
   private final JMSID factoryId;
   private int state;

   public FEConnectionFactory(FrontEnd var1, String var2, String var3, boolean var4, boolean var5, String var6) {
      this(var1, var2, var3, var4, var5, var6, true, true);
   }

   public FEConnectionFactory(FrontEnd var1, String var2, String var3, boolean var4, boolean var5, String var6, boolean var7, boolean var8) {
      this.connectionFactoryBean = null;
      this.defaultConnectionFactory = false;
      this.jndiName = "";
      this.localJNDIName = "";
      this.jndiNameRebindLock = new Object();
      this.compressionThreshold = Integer.MAX_VALUE;
      this.loadBalancingEnabled = true;
      this.serverAffinityEnabled = true;
      this.attachJMSXUserID = false;
      this.synchronousPrefetchMode = "disabled";
      this.oneWaySendMode = "disabled";
      this.oneWaySendWindowSize = 1;
      this.reconnectPolicy = JMSConstants.RECONNECT_POLICY_PRODUCER;
      this.reconnectBlockingMillis = 60000L;
      this.totalReconnectPeriodMillis = -1L;
      this.state = 0;
      this.factoryId = var1.getService().getNextId();
      this.frontEnd = var1;
      this.name = var2;
      this.jndiName = StringUtils.isEmptyString(var3) ? "" : var3;
      this.jndiName = JMSServerUtilities.transformJNDIName(var3);
      this.setupDefaultDeliveryParams();
      this.setupDefaultClientParams();
      this.setupDefaultTransactionParams();
      this.setupDefaultFlowControlParams();
      this.setupDefaultLoadBalancingParams();
      this.setupDefaultSecurityParams();
      this.xaConnectionFactoryEnabled = var5;
      this.allowCloseInOnMessage = var4;
      this.acknowledgePolicy = var6;
      this.serverAffinityEnabled = var8;
      this.loadBalancingEnabled = var7;
      this.setFullyQualifiedName((String)null);
      this.defaultConnectionFactory = true;
   }

   public FEConnectionFactory(FrontEnd var1, JMSConnectionFactoryBean var2, String var3, String var4, Context var5) {
      this.connectionFactoryBean = null;
      this.defaultConnectionFactory = false;
      this.jndiName = "";
      this.localJNDIName = "";
      this.jndiNameRebindLock = new Object();
      this.compressionThreshold = Integer.MAX_VALUE;
      this.loadBalancingEnabled = true;
      this.serverAffinityEnabled = true;
      this.attachJMSXUserID = false;
      this.synchronousPrefetchMode = "disabled";
      this.oneWaySendMode = "disabled";
      this.oneWaySendWindowSize = 1;
      this.reconnectPolicy = JMSConstants.RECONNECT_POLICY_PRODUCER;
      this.reconnectBlockingMillis = 60000L;
      this.totalReconnectPeriodMillis = -1L;
      this.state = 0;
      this.factoryId = var1.getService().getNextId();
      this.frontEnd = var1;
      this.EARModuleName = var3;
      this.moduleName = var4;
      this.connectionFactoryBean = var2;
      this.name = JMSBeanHelper.getDecoratedName(var4, var2.getName());
      this.EARNamingContext = var5;
   }

   private void setFullyQualifiedName(String var1) {
      this.fullyQualifiedName = var1;
   }

   private JMSConnectionFactory computeJMSConnectionFactory() {
      return (JMSConnectionFactory)(this.xaConnectionFactoryEnabled ? new JMSXAConnectionFactory(new FEConnectionFactoryImpl(this), this.fullyQualifiedName) : new JMSConnectionFactory(new FEConnectionFactoryImpl(this), this.fullyQualifiedName));
   }

   public void setupDefaultDeliveryParams() {
      this.deliveryMode = "Persistent";
      this.priority = 4;
      this.timeToDeliver = "0";
      this.timeToLive = 0L;
      this.sendTimeout = 10L;
      this.redeliveryDelay = 0L;
   }

   public void setupDefaultClientParams() {
      this.messagesMaximum = 10;
      this.overrunPolicy = "KeepOld";
      this.clientIdPolicy = "Restricted";
      this.subscriptionSharingPolicy = weblogic.management.configuration.JMSConstants.SUBSCRIPTION_EXCLUSIVE;
   }

   public void setupDefaultTransactionParams() {
      this.transactionTimeout = 3600L;
      this.xaConnectionFactoryEnabled = false;
   }

   public void setupDefaultFlowControlParams() {
      this.flowControlEnabled = true;
      this.flowMinimum = 50;
      this.flowMaximum = 500;
      this.flowInterval = 60;
      this.flowSteps = 10;
      this.oneWaySendMode = "disabled";
      this.oneWaySendWindowSize = 1;
   }

   public void setupDefaultLoadBalancingParams() {
      this.loadBalancingEnabled = true;
      this.serverAffinityEnabled = true;
   }

   public void setupDefaultSecurityParams() {
      this.attachJMSXUserID = false;
   }

   private void initialize() throws ModuleException {
      try {
         this.initializeBeanUpdateListeners();
      } catch (ManagementException var3) {
         throw new ModuleException(var3.getMessage(), var3);
      }

      try {
         this.valJNDIName(this.jndiName);
         this.valLocalJNDIName(this.localJNDIName);
      } catch (BeanUpdateRejectedException var2) {
         throw new ModuleException(var2.getMessage(), var2);
      }
   }

   JMSConnection connectionCreateInternal(DispatcherWrapper var1, boolean var2) throws JMSException {
      this.checkShutdownOrSuspended();
      PeerInfo var3 = var1.getPeerInfo();
      DispatcherWrapper var4 = JMSDispatcherManager.getLocalDispatcherWrapper();
      final JMSID var6 = this.frontEnd.getService().getNextId();
      final String var7 = "connection" + var6.getCounter();

      final JMSDispatcher var8;
      try {
         var8 = JMSDispatcherManager.addDispatcherReference(var1);
      } catch (DispatcherException var16) {
         throw new weblogic.jms.common.JMSException(var16);
      }

      final int var9 = this.getDefaultDeliveryModeAsInt();
      final long var10 = this.transactionTimeout;
      final int var12 = this.messagesMaximum;
      final String var13 = this.clientId;

      FEConnection var5;
      try {
         var5 = (FEConnection)SecurityServiceManager.runAs(KERNEL_ID, KERNEL_ID, new PrivilegedExceptionAction() {
            public Object run() throws ManagementException, JMSException {
               return new FEConnection(FEConnectionFactory.this, var7, var6, var8, var9, FEConnectionFactory.this.priority, Long.parseLong(FEConnectionFactory.this.timeToDeliver), FEConnectionFactory.this.timeToLive, FEConnectionFactory.this.sendTimeout, FEConnectionFactory.this.redeliveryDelay, var13, FEConnectionFactory.this.getClientIdPolicyAsInt(), FEConnectionFactory.getSubscriptionSharingPolicyAsInt(FEConnectionFactory.this.subscriptionSharingPolicy), var10, FEConnectionFactory.this.xaConnectionFactoryEnabled, FEConnectionFactory.this.allowCloseInOnMessage, var12, FEConnectionFactory.this.getMulticastOverrunPolicyAsInt(), FEConnectionFactory.this.getAcknowledgePolicyAsInt(), FEConnectionFactory.this.loadBalancingEnabled, FEConnectionFactory.this.serverAffinityEnabled, FEConnectionFactory.this.defaultUnitOfOrder, FEConnectionFactory.this.compressionThreshold);
            }
         });
      } catch (PrivilegedActionException var17) {
         Exception var15 = var17.getException();
         if (var15 instanceof JMSException) {
            throw (JMSException)var15;
         }

         throw new weblogic.jms.common.JMSException("Error creating connection: " + var7, var15);
      }

      InvocableManagerDelegate.delegate.invocableAdd(7, var5);
      return (JMSConnection)(this.xaConnectionFactoryEnabled && var2 ? new JMSXAConnection(var5.getJMSID(), var5.getConnectionClientId(), this.getClientIdPolicyAsInt(), getSubscriptionSharingPolicyAsInt(this.subscriptionSharingPolicy), var5.getDeliveryMode(), var5.getPriority(), var5.getTimeToDeliver(), var5.getTimeToLive(), var5.getSendTimeout(), var5.getRedeliveryDelay(), var5.getTransactionTimeout(), var5.userTransactionsEnabled(), var5.getAllowCloseInOnMessage(), var5.getMessagesMaximum(), var5.getOverrunPolicy(), var5.getAcknowledgePolicy(), var5.isLocal(), var4, this.flowControlEnabled, this.flowMinimum, this.flowMaximum, this.flowInterval, this.flowSteps, this.defaultUnitOfOrder, var5, ManagementService.getRuntimeAccess(KERNEL_ID).getServerName(), var5.getRuntimeDelegate().getName(), var3, this.compressionThreshold, 0, 0, 1, WLConnectionImpl.validateAndConvertReconnectPolicy(this.reconnectPolicy), this.reconnectBlockingMillis, this.totalReconnectPeriodMillis) : new JMSConnection(var5.getJMSID(), var5.getConnectionClientId(), this.getClientIdPolicyAsInt(), getSubscriptionSharingPolicyAsInt(this.subscriptionSharingPolicy), var5.getDeliveryMode(), var5.getPriority(), var5.getTimeToDeliver(), var5.getTimeToLive(), var5.getSendTimeout(), var5.getRedeliveryDelay(), var5.getTransactionTimeout(), var5.userTransactionsEnabled(), var5.getAllowCloseInOnMessage(), var5.getMessagesMaximum(), var5.getOverrunPolicy(), var5.getAcknowledgePolicy(), var5.isLocal(), var4, this.flowControlEnabled, this.flowMinimum, this.flowMaximum, this.flowInterval, this.flowSteps, this.xaConnectionFactoryEnabled, this.defaultUnitOfOrder, var5, ManagementService.getRuntimeAccess(KERNEL_ID).getServerName(), var5.getRuntimeDelegate().getName(), var3, this.compressionThreshold, JMSConnection.convertPrefetchMode(this.synchronousPrefetchMode), JMSService.getJMSService().getUse81StyleExecuteQueues() ? 0 : JMSConnection.convertOneWaySendMode(this.oneWaySendMode), this.oneWaySendWindowSize, WLConnectionImpl.validateAndConvertReconnectPolicy(this.reconnectPolicy), this.reconnectBlockingMillis, this.totalReconnectPeriodMillis));
   }

   private void globalbind(String var1) throws JMSException {
      if (!this.bound && !StringUtils.isEmptyString(var1)) {
         try {
            JMSConnectionFactory var2 = this.computeJMSConnectionFactory();
            this.frontEnd.getService();
            PrivilegedActionUtilities.bindAsSU(JMSService.getContext(true), var1, var2, KERNEL_ID);
            this.globalBoundConnectionFactory = var2;
            this.bound = true;
         } catch (NamingException var3) {
            throw new weblogic.jms.common.JMSException("Error binding connection factory (name = " + this.name + ") to (jndi name = " + var1 + ")", var3);
         }
      }

   }

   private void localbind(String var1) throws JMSException {
      if (!this.localBound && !StringUtils.isEmptyString(var1)) {
         try {
            JMSConnectionFactory var2 = this.computeJMSConnectionFactory();
            this.frontEnd.getService();
            PrivilegedActionUtilities.bindAsSU(JMSService.getContext(false), var1, var2, KERNEL_ID);
            this.localBound = true;
            this.localBoundConnectionFactory = var2;
         } catch (NamingException var3) {
            throw new weblogic.jms.common.JMSException("Error binding connection factory (name = " + this.name + ") to (local jndi name = " + var1 + ")", var3);
         }
      }

   }

   private String constructAppscopedJNDIName() {
      return this.EARModuleName == null ? null : this.EARModuleName + "#" + this.getEntityName();
   }

   private void appscopedbind() throws JMSException {
      String var1 = this.constructAppscopedJNDIName();
      if (!this.applicationBound && var1 != null && this.EARNamingContext != null) {
         try {
            JMSConnectionFactory var2 = this.computeJMSConnectionFactory();
            PrivilegedActionUtilities.bindAsSU(this.EARNamingContext, var1, var2, KERNEL_ID);
            this.applicationBound = true;
            this.appBoundConnectionFactory = var2;
         } catch (NamingException var3) {
            throw new weblogic.jms.common.JMSException("Error binding connection factory (name = " + this.name + ") to (application jndi name = " + var1 + ")", var3);
         }
      }
   }

   private void globalunbind(String var1, String var2) {
      this.frontEnd.getService();
      EventContext var3 = (EventContext)JMSService.getContext(true);
      if (!StringUtils.isEmptyString(var1) && this.bound) {
         if (var2 != null) {
            try {
               var3.addNamingListener(var1, 0, this);
            } catch (NamingException var25) {
               JMSLogger.logErrorEstablishingJNDIListener(this.name, var25.toString());
               return;
            }

            synchronized(this.jndiNameRebindLock) {
               this.didUnbind = false;
            }
         }

         try {
            PrivilegedActionUtilities.unbindAsSU(var3, var1, KERNEL_ID);
            UnicastRemoteObject.unexportObject(this.globalBoundConnectionFactory, true);
            if (var2 != null) {
               synchronized(this.jndiNameRebindLock) {
                  for(int var5 = 0; var5 < 15; ++var5) {
                     try {
                        this.jndiNameRebindLock.wait(20000L);
                     } catch (InterruptedException var23) {
                     }

                     if (this.didUnbind) {
                        break;
                     }

                     JMSLogger.logInfoWaitForUnbind(this.name, var1, var2);
                  }

                  if (!this.didUnbind) {
                     JMSLogger.logErrorWaitForUnbind(this.name, var1, var2);
                  }
               }
            }

            this.bound = false;
         } catch (NoSuchObjectException var27) {
         } catch (NamingException var28) {
         } finally {
            if (var2 != null) {
               try {
                  var3.removeNamingListener(this);
               } catch (NamingException var22) {
                  JMSLogger.logErrorRemovingJNDIListener(this.name, var22.toString());
               }
            }

         }
      }

   }

   private void localunbind(String var1) {
      if (!StringUtils.isEmptyString(var1) && this.localBound) {
         try {
            this.frontEnd.getService();
            PrivilegedActionUtilities.unbindAsSU(JMSService.getContext(false), var1, KERNEL_ID);
            UnicastRemoteObject.unexportObject(this.localBoundConnectionFactory, true);
            this.localBound = false;
         } catch (NoSuchObjectException var3) {
         } catch (NamingException var4) {
         }
      }

   }

   private void appscopedunbind() {
      String var1 = this.constructAppscopedJNDIName();
      if (this.applicationBound && var1 != null && this.EARNamingContext != null) {
         try {
            PrivilegedActionUtilities.unbindAsSU(this.EARNamingContext, var1, KERNEL_ID);
            UnicastRemoteObject.unexportObject(this.appBoundConnectionFactory, true);
            this.applicationBound = false;
         } catch (NoSuchObjectException var3) {
         } catch (NamingException var4) {
         }

      }
   }

   public void bind() throws JMSException {
      this.globalbind(this.jndiName);
      this.localbind(this.localJNDIName);
      this.appscopedbind();
      synchronized(this) {
         this.state = 4;
      }
   }

   public void unbind() {
      this.globalunbind(this.jndiName, (String)null);
      this.localunbind(this.localJNDIName);
      this.appscopedunbind();
   }

   synchronized void markSuspending() {
      if ((this.state & 27) == 0) {
         this.state = 2;
      }
   }

   public void suspend() {
      synchronized(this) {
         if (this.state == 1) {
            return;
         }

         this.state = 1;
      }

      this.unbind();
   }

   synchronized void markShuttingDown() {
      if ((this.state & 24) == 0) {
         this.state = 8;
      }
   }

   public void shutdown() {
      synchronized(this) {
         if (this.state == 16) {
            return;
         }

         this.state = 16;
      }

      this.unbind();
   }

   private synchronized void checkShutdownOrSuspended() throws JMSException {
      if ((this.state & 27) != 0) {
         throw new weblogic.jms.common.JMSException("JMS server is shutdown or suspended");
      }
   }

   public FrontEnd getFrontEnd() {
      return this.frontEnd;
   }

   public JMSID getId() {
      return this.factoryId;
   }

   public String getModuleName() {
      return this.moduleName;
   }

   public String getName() {
      return this.name;
   }

   public void setDefaultTargetingEnabled(boolean var1) {
      this.defaultTargetingEnabled = var1;
   }

   public boolean isDefaultTargetingEnabled() {
      return this.defaultTargetingEnabled;
   }

   public String getJNDIName() {
      return this.jndiName;
   }

   public void valJNDIName(String var1) throws BeanUpdateRejectedException {
      var1 = JMSServerUtilities.transformJNDIName(var1);
      if (!StringUtils.isEmptyString(var1)) {
         if (this.state != 4 || this.jndiName == null || !this.jndiName.equals(var1)) {
            Context var2 = JMSService.getContext();

            Object var3;
            try {
               var3 = var2.lookup(var1);
            } catch (NameNotFoundException var6) {
               return;
            } catch (NamingException var7) {
               throw new BeanUpdateRejectedException(var7.getMessage(), var7);
            }

            if (!(var3 instanceof JMSConnectionFactory)) {
               throw new BeanUpdateRejectedException("The proposed JNDI name \"" + var1 + "\" for connection factory \"" + this.name + "\" is already bound by another object of type \"" + var3.getClass().getName() + "\"");
            } else {
               JMSConnectionFactory var4 = (JMSConnectionFactory)var3;
               String var5 = this.defaultConnectionFactory ? "" : this.name;
               if (!var4.getFullyQualifiedName().equals(var5)) {
                  throw new BeanUpdateRejectedException("The proposed JNDI name \"" + var1 + "\" for connection factory \"" + this.name + "\" is already bound by another connection factory \"" + var4.getFullyQualifiedName() + "\"");
               }
            }
         }
      }
   }

   public void setJNDIName(String var1) throws IllegalArgumentException {
      var1 = JMSServerUtilities.transformJNDIName(var1);
      String var2 = this.jndiName;
      this.jndiName = StringUtils.isEmptyString(var1) ? "" : var1;
      if (this.state == 4) {
         if (this.jndiName == "") {
            if (this.bound) {
               this.globalunbind(var2, this.jndiName);
            }

         } else {
            if (var2 == "" || !this.jndiName.equals(var2)) {
               if (this.bound) {
                  this.globalunbind(var2, this.jndiName);
               }

               try {
                  this.globalbind(this.jndiName);
               } catch (JMSException var4) {
                  JMSLogger.logErrorBindCF(this.name, var4);
                  this.frontEnd.connectionFactoryRemove(this);
                  throw new IllegalArgumentException("Error binding connection factory name : " + this.name + "to jndi name: " + this.jndiName);
               }
            }

         }
      }
   }

   public String getLocalJNDIName() {
      return this.localJNDIName;
   }

   public void valLocalJNDIName(String var1) throws BeanUpdateRejectedException {
      var1 = JMSServerUtilities.transformJNDIName(var1);
      if (!StringUtils.isEmptyString(var1)) {
         if (this.state != 4 || !this.localJNDIName.equals(var1)) {
            Context var2 = JMSService.getContext();

            Object var3;
            try {
               var3 = var2.lookup(var1);
            } catch (NameNotFoundException var6) {
               return;
            } catch (NamingException var7) {
               throw new BeanUpdateRejectedException(var7.getMessage(), var7);
            }

            if (!(var3 instanceof JMSConnectionFactory)) {
               throw new BeanUpdateRejectedException("The proposed JNDI name " + var1 + " for connection factory " + this.name + " is already bound by another object of type " + (var3 == null ? "null" : var3.getClass().getName()));
            } else {
               JMSConnectionFactory var4 = (JMSConnectionFactory)var3;
               String var5 = this.defaultConnectionFactory ? "" : this.name;
               if (!var4.getFullyQualifiedName().equals(var5)) {
                  throw new BeanUpdateRejectedException("The proposed JNDI name " + var1 + " for connection factory " + this.name + " is already bound by another connection factory " + var4.getFullyQualifiedName());
               }
            }
         }
      }
   }

   public void setLocalJNDIName(String var1) throws IllegalArgumentException {
      var1 = JMSServerUtilities.transformJNDIName(var1);
      String var2 = this.localJNDIName;
      this.localJNDIName = StringUtils.isEmptyString(var1) ? "" : var1;
      if (this.state == 4) {
         if (this.localJNDIName == "") {
            if (this.localBound) {
               this.localunbind(var2);
            }

         } else {
            if (var2 == "" || !this.localJNDIName.equals(var2)) {
               if (this.localBound) {
                  this.localunbind(var2);
               }

               try {
                  this.localbind(this.localJNDIName);
               } catch (JMSException var4) {
                  JMSLogger.logErrorBindCF(this.name, var4);
                  this.frontEnd.connectionFactoryRemove(this);
                  throw new IllegalArgumentException("Error binding connection factory name : " + this.name + " to local jndi name: " + this.localJNDIName);
               }
            }

         }
      }
   }

   public String getDefaultDeliveryMode() {
      return this.deliveryMode;
   }

   public int getDefaultDeliveryModeAsInt() {
      byte var1 = 2;
      if (this.deliveryMode != null && !this.deliveryMode.equalsIgnoreCase("No-Delivery")) {
         if (this.deliveryMode.equalsIgnoreCase("Persistent")) {
            var1 = 2;
         } else if (this.deliveryMode.equalsIgnoreCase("Non-Persistent")) {
            var1 = 1;
         }
      } else {
         var1 = 2;
      }

      return var1;
   }

   public void setDefaultDeliveryMode(String var1) throws IllegalArgumentException {
      if (var1 != null && !var1.equalsIgnoreCase("No-Delivery")) {
         if (!var1.equalsIgnoreCase("Persistent") && !var1.equalsIgnoreCase("Non-Persistent")) {
            throw new IllegalArgumentException("Invalid delivery mode");
         } else {
            this.deliveryMode = var1;
         }
      } else {
         this.deliveryMode = "Persistent";
      }
   }

   public int getDefaultPriority() {
      return this.priority;
   }

   public void setDefaultPriority(int var1) {
      this.priority = var1;
   }

   public long getDefaultTimeToDeliverAsLong() {
      return Long.parseLong(this.timeToDeliver);
   }

   public String getDefaultTimeToDeliver() {
      return this.timeToDeliver;
   }

   public void setDefaultTimeToDeliver(String var1) {
      this.timeToDeliver = var1;
   }

   public long getDefaultTimeToLive() {
      return this.timeToLive;
   }

   public void setDefaultTimeToLive(long var1) {
      this.timeToLive = var1;
   }

   public long getDefaultRedeliveryDelay() {
      return this.redeliveryDelay;
   }

   public void setDefaultRedeliveryDelay(long var1) {
      this.redeliveryDelay = var1;
   }

   public int getDefaultCompressionThreshold() {
      return this.compressionThreshold;
   }

   public void setDefaultCompressionThreshold(int var1) {
      this.compressionThreshold = var1;
   }

   public void setDefaultUnitOfOrder(String var1) {
      this.defaultUnitOfOrder = var1;
   }

   public String getDefaultUnitOfOrder() {
      return this.defaultUnitOfOrder;
   }

   public String getSynchronousPrefetchMode() {
      return this.synchronousPrefetchMode;
   }

   public void setSynchronousPrefetchMode(String var1) throws IllegalArgumentException {
      if (var1 != null && !var1.equals("enabled") && !var1.equals("disabled") && !var1.equals("topicSubscriberOnly")) {
         throw new IllegalArgumentException("Invalid synchronous prefetch mode");
      } else {
         this.synchronousPrefetchMode = var1;
      }
   }

   public String getOneWaySendMode() {
      return this.oneWaySendMode;
   }

   public void setOneWaySendMode(String var1) throws IllegalArgumentException {
      if (var1 != null && !var1.equals("enabled") && !var1.equals("disabled") && !var1.equals("topicOnly")) {
         throw new IllegalArgumentException("Invalid one way send mode");
      } else {
         this.oneWaySendMode = var1;
      }
   }

   public int getOneWaySendWindowSize() {
      return this.oneWaySendWindowSize;
   }

   public void setOneWaySendWindowSize(int var1) throws IllegalArgumentException {
      this.oneWaySendWindowSize = var1;
   }

   public String getReconnectPolicy() {
      return this.reconnectPolicy;
   }

   public void setReconnectPolicy(String var1) {
      WLConnectionImpl.validateAndConvertReconnectPolicy(var1);
      this.reconnectPolicy = var1;
   }

   public long getReconnectBlockingMillis() {
      return this.reconnectBlockingMillis;
   }

   public void setReconnectBlockingMillis(long var1) {
      WLConnectionImpl.validateReconnectMillis(var1);
      if (var1 == -1L || var1 > this.totalReconnectPeriodMillis) {
         var1 = this.totalReconnectPeriodMillis;
      }

      this.reconnectBlockingMillis = var1;
   }

   public long getTotalReconnectPeriodMillis() {
      return this.totalReconnectPeriodMillis;
   }

   public void setTotalReconnectPeriodMillis(long var1) {
      WLConnectionImpl.validateReconnectMillis(var1);
      this.totalReconnectPeriodMillis = var1;
   }

   public int getMessagesMaximum() {
      return this.messagesMaximum;
   }

   public void setMessagesMaximum(int var1) throws IllegalArgumentException {
      if (var1 >= -1 && var1 != 0) {
         this.messagesMaximum = var1;
      } else {
         throw new IllegalArgumentException("Invalid MessagesMaximum");
      }
   }

   public String getClientId() {
      return this.clientId;
   }

   public void setClientId(String var1) {
      if (var1 != null && var1.length() == 0) {
         this.clientId = null;
      } else {
         this.clientId = var1;
      }
   }

   public String getClientIdPolicy() {
      return this.clientIdPolicy;
   }

   public int getClientIdPolicyAsInt() {
      if (this.clientIdPolicy.equals("Restricted")) {
         return 0;
      } else {
         return this.clientIdPolicy.equals("Unrestricted") ? 1 : 0;
      }
   }

   public void setClientIdPolicy(String var1) throws IllegalArgumentException {
      if (var1 == null) {
         this.clientIdPolicy = "Restricted";
      } else if (!var1.equals("Restricted") && !var1.equals("Unrestricted")) {
         throw new IllegalArgumentException("Unrecognized ClientIdPolicy " + var1);
      } else {
         this.clientIdPolicy = var1;
      }
   }

   public String getSubscriptionSharingPolicy() {
      return this.subscriptionSharingPolicy;
   }

   public static int getSubscriptionSharingPolicyAsInt(String var0) {
      if (var0.equals(JMSConstants.SUBSCRIPTION_EXCLUSIVE)) {
         return 0;
      } else if (var0.equals(JMSConstants.SUBSCRIPTION_SHARABLE)) {
         return 1;
      } else {
         throw new IllegalArgumentException("Unrecognized SubscriptionSharingPolicy " + var0);
      }
   }

   public static String getSubscriptionSharingPolicyAsString(int var0) {
      switch (var0) {
         case 0:
            return JMSConstants.SUBSCRIPTION_EXCLUSIVE;
         case 1:
            return JMSConstants.SUBSCRIPTION_SHARABLE;
         default:
            throw new IllegalArgumentException("Unrecognized SubscriptionSharingPolicy " + var0);
      }
   }

   public void setSubscriptionSharingPolicy(String var1) throws IllegalArgumentException {
      if (var1 == null) {
         this.subscriptionSharingPolicy = JMSConstants.SUBSCRIPTION_EXCLUSIVE;
      } else if (!var1.equals(JMSConstants.SUBSCRIPTION_EXCLUSIVE) && !var1.equals(JMSConstants.SUBSCRIPTION_SHARABLE)) {
         throw new IllegalArgumentException("Unrecognized Subscription Sharing Policy " + var1);
      } else {
         this.subscriptionSharingPolicy = var1;
      }
   }

   public boolean isAllowCloseInOnMessage() {
      return this.allowCloseInOnMessage;
   }

   public void setAllowCloseInOnMessage(boolean var1) {
      this.allowCloseInOnMessage = var1;
   }

   public String getAcknowledgePolicy() {
      return this.acknowledgePolicy;
   }

   public int getAcknowledgePolicyAsInt() {
      byte var1 = 1;
      if (this.acknowledgePolicy == null) {
         var1 = 1;
      } else if (this.acknowledgePolicy.equalsIgnoreCase("All")) {
         var1 = 1;
      } else if (this.acknowledgePolicy.equalsIgnoreCase("Previous")) {
         var1 = 2;
      } else if (this.acknowledgePolicy.equalsIgnoreCase("One")) {
         var1 = 3;
      }

      return var1;
   }

   public void setAcknowledgePolicy(String var1) throws IllegalArgumentException {
      if (var1 == null) {
         this.acknowledgePolicy = "All";
      } else if (!var1.equalsIgnoreCase("All") && !var1.equalsIgnoreCase("Previous") && !var1.equalsIgnoreCase("One")) {
         throw new IllegalArgumentException("Invalid acknowledgePolicy");
      } else {
         this.acknowledgePolicy = var1;
      }
   }

   public String getMulticastOverrunPolicy() {
      return this.overrunPolicy;
   }

   public int getMulticastOverrunPolicyAsInt() {
      byte var1 = 0;
      if (this.overrunPolicy == null) {
         var1 = 0;
      } else if (this.overrunPolicy.equalsIgnoreCase("KeepOld")) {
         var1 = 0;
      } else if (this.overrunPolicy.equalsIgnoreCase("KeepNew")) {
         var1 = 1;
      }

      return var1;
   }

   public void setMulticastOverrunPolicy(String var1) throws IllegalArgumentException {
      if (var1 == null) {
         this.overrunPolicy = "KeepOld";
      } else if (!var1.equalsIgnoreCase("KeepOld") && !var1.equalsIgnoreCase("KeepNew")) {
         throw new IllegalArgumentException("Invalid multicast overrun policy for connection factory");
      } else {
         this.overrunPolicy = var1;
      }
   }

   public boolean isXAConnectionFactoryEnabled() {
      return this.xaConnectionFactoryEnabled;
   }

   public void setXAConnectionFactoryEnabled(boolean var1) {
      this.xaConnectionFactoryEnabled = var1;
   }

   public long getTransactionTimeout() {
      return this.transactionTimeout;
   }

   public void setTransactionTimeout(long var1) throws IllegalArgumentException {
      if (var1 >= 0L && var1 <= 2147483647L) {
         this.transactionTimeout = var1;
      } else {
         throw new IllegalArgumentException("Invalid TransactionTimeout");
      }
   }

   public boolean isFlowControlEnabled() {
      return this.flowControlEnabled;
   }

   public void setFlowControlEnabled(boolean var1) {
      this.flowControlEnabled = var1;
   }

   public int getFlowInterval() {
      return this.flowInterval;
   }

   public void setFlowInterval(int var1) {
      this.flowInterval = var1;
   }

   public int getFlowMaximum() {
      return this.flowMaximum;
   }

   public void setFlowMaximum(int var1) throws IllegalArgumentException {
      this.flowMaximum = var1;
   }

   public int getFlowMinimum() {
      return this.flowMinimum;
   }

   public void setFlowMinimum(int var1) throws IllegalArgumentException {
      this.flowMinimum = var1;
   }

   public int getFlowSteps() {
      return this.flowSteps;
   }

   public void setFlowSteps(int var1) {
      this.flowSteps = var1;
   }

   public boolean isLoadBalancingEnabled() {
      return this.loadBalancingEnabled;
   }

   public void setLoadBalancingEnabled(boolean var1) {
      this.loadBalancingEnabled = var1;
   }

   public boolean isServerAffinityEnabled() {
      return this.serverAffinityEnabled;
   }

   public void setServerAffinityEnabled(boolean var1) {
      this.serverAffinityEnabled = var1;
   }

   public void setAttachJMSXUserId(boolean var1) {
      this.attachJMSXUserID = var1;
   }

   public boolean isAttachJMSXUserId() {
      return this.attachJMSXUserID;
   }

   public long getSendTimeout() {
      return this.sendTimeout;
   }

   public void setSendTimeout(long var1) {
      this.sendTimeout = var1;
   }

   public JMSConnectionFactory getJMSConnectionFactory() {
      if (this.globalBoundConnectionFactory != null) {
         return this.globalBoundConnectionFactory;
      } else if (this.localBoundConnectionFactory != null) {
         return this.localBoundConnectionFactory;
      } else {
         return this.appBoundConnectionFactory != null ? this.appBoundConnectionFactory : this.computeJMSConnectionFactory();
      }
   }

   public void prepare() throws ModuleException {
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("Preparing connection factory: " + this.name);
      }

      if ("WebLogic_Debug_CON_fail_prepare".equals(this.connectionFactoryBean.getName())) {
         throw new ModuleException("DEBUG: A connection factory with name WebLogic_Debug_CON_fail_prepare will force the prepare to fail");
      } else {
         try {
            this.frontEnd.getService().ensureInitialized();
         } catch (JMSException var3) {
            throw new ModuleException("JMS Service is not initialized", var3);
         }

         if (this.frontEnd.connectionFactoryFind(this.name) != null) {
            throw new ModuleException("ConnectionFactory " + this.name + " already exists");
         } else {
            try {
               this.initialize();
               this.frontEnd.connectionFactoryAdd(this);
            } catch (Exception var2) {
               JMSLogger.logErrorCreateCF(this.name, var2);
               throw new ModuleException("Error preparing connection factory " + this.name, var2);
            }

            if (JMSDebug.JMSModule.isDebugEnabled()) {
               JMSDebug.JMSModule.debug("Successfully prepared connection factory: " + this.name);
            }

         }
      }
   }

   public void activate(JMSBean var1) throws ModuleException {
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("Activating connection factory " + this.name);
      }

      if ("WebLogic_Debug_CON_fail_activate".equals(this.connectionFactoryBean.getName())) {
         throw new ModuleException("DEBUG: A connection factory with name WebLogic_Debug_CON_fail_activate will force the activate to fail");
      } else {
         this.connectionFactoryBean = var1.lookupConnectionFactory(this.getEntityName());
         this.unregisterBeanUpdateListeners();
         this.registerBeanUpdateListeners();
         if (this.frontEnd.getService().isActive()) {
            try {
               this.setFullyQualifiedName(this.name);
               this.bind();
               JMSLogger.logCFactoryDeployed(this.name);
            } catch (JMSException var3) {
               JMSLogger.logErrorBindCF(this.name, var3);
               throw new ModuleException("Error binding connection factory " + this.name, var3);
            }
         }

         if (JMSDebug.JMSModule.isDebugEnabled()) {
            JMSDebug.JMSModule.debug("Successfully activated connection factory: " + this.name);
         }

      }
   }

   public void deactivate() throws ModuleException {
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("De-activating connection factory: " + this.name);
      }

      try {
         this.frontEnd.getService().ensureInitialized();
      } catch (JMSException var3) {
         throw new ModuleException("JMS Service is not initialized", var3);
      }

      FEConnectionFactory var1 = this.frontEnd.connectionFactoryFind(this.name);
      if (var1 == null) {
         if (JMSDebug.JMSModule.isDebugEnabled()) {
            JMSDebug.JMSModule.debug("Error de-activating ConnectionFactory " + this.name + ": instance not found");
         }

         throw new ModuleException("Error de-activating a non-existent connection factory " + this.name + "(jndi: " + this.jndiName + ")");
      } else {
         try {
            this.unregisterBeanUpdateListeners();
            var1.unbind();
         } catch (Exception var4) {
            if (JMSDebug.JMSModule.isDebugEnabled()) {
               JMSDebug.JMSModule.debug("Error de-activating ConnectionFactory " + this + ": not currently in ACTIVATED state");
            }

            throw new ModuleException("Error de-activating" + this.name + "(jndi: " + this.jndiName + ") not currently in ACTIVATED state");
         }

         if ("WebLogic_Debug_CON_fail_deactivate".equals(this.connectionFactoryBean.getName())) {
            throw new ModuleException("DEBUG: A connection factory with name WebLogic_Debug_CON_fail_deactivate will force the deactivate to fail");
         } else {
            if (JMSDebug.JMSModule.isDebugEnabled()) {
               JMSDebug.JMSModule.debug("Successfully de-activated connection factory: " + this.name);
            }

         }
      }
   }

   public void unprepare() throws ModuleException {
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("Un-preparing connection factory: " + this.name);
      }

      try {
         this.frontEnd.getService().ensureInitialized();
      } catch (JMSException var4) {
         throw new ModuleException("JMS Service is not initialized" + var4);
      }

      FEConnectionFactory var1 = this.frontEnd.connectionFactoryFind(this.name);
      if (var1 == null) {
         if (JMSDebug.JMSModule.isDebugEnabled()) {
            JMSDebug.JMSModule.debug("Error un-preparing ConnectionFactory " + this.name + ": instance not found");
         }

         throw new ModuleException("Error un-preparing a non-existent connection factory " + this.name + "(jndi: " + this.jndiName + ")");
      } else {
         try {
            this.frontEnd.connectionFactoryRemove(this);
         } catch (Exception var3) {
            JMSLogger.logErrorCreateCF(this.name, var3);
            throw new ModuleException("Error un-preparing connection factory " + this.name, var3);
         }

         if ("WebLogic_Debug_CON_fail_unprepare".equals(this.connectionFactoryBean.getName())) {
            throw new ModuleException("DEBUG: A connection factory with name WebLogic_Debug_CON_fail_unprepare will force the unprepare to fail");
         } else {
            if (JMSDebug.JMSModule.isDebugEnabled()) {
               JMSDebug.JMSModule.debug("Successfully un-prepared connection factory: " + this.name);
            }

         }
      }
   }

   public void remove() throws ModuleException {
      if ("WebLogic_Debug_CON_fail_remove".equals(this.connectionFactoryBean.getName())) {
         throw new ModuleException("DEBUG: A connection factory with name WebLogic_Debug_CON_fail_remove will force the remove to fail");
      }
   }

   public void destroy() throws ModuleException {
      if ("WebLogic_Debug_CON_fail_destroy".equals(this.connectionFactoryBean.getName())) {
         throw new ModuleException("DEBUG: A connection factory with name WebLogic_Debug_CON_fail_destroy will force the destroy to fail");
      }
   }

   public String getEntityName() {
      return this.connectionFactoryBean != null ? this.connectionFactoryBean.getName() : null;
   }

   public void prepareChangeOfTargets(List var1, DomainMBean var2) {
   }

   public void activateChangeOfTargets() {
   }

   public void rollbackChangeOfTargets() {
   }

   private void initializeBeanUpdateListeners() throws ManagementException {
      DescriptorBean var1 = (DescriptorBean)this.connectionFactoryBean;
      this.connectionFactoryBeanListener = new GenericBeanListener(var1, this, BeanHelper.connectionFactoryBeanSignatures);
      this.connectionFactoryBeanListener.initialize();
      var1 = (DescriptorBean)this.connectionFactoryBean.getDefaultDeliveryParams();
      this.defaultDeliveryParamsBeanListener = new GenericBeanListener(var1, this, BeanHelper.defaultDeliveryParamsBeanSignatures);
      this.defaultDeliveryParamsBeanListener.initialize();
      var1 = (DescriptorBean)this.connectionFactoryBean.getClientParams();
      this.clientParamsBeanListener = new GenericBeanListener(var1, this, BeanHelper.clientParamsBeanSignatures);
      this.clientParamsBeanListener.initialize();
      var1 = (DescriptorBean)this.connectionFactoryBean.getTransactionParams();
      this.transactionParamsBeanListener = new GenericBeanListener(var1, this, BeanHelper.transactionParamsBeanSignatures);
      this.transactionParamsBeanListener.initialize();
      var1 = (DescriptorBean)this.connectionFactoryBean.getFlowControlParams();
      this.flowControlParamsBeanListener = new GenericBeanListener(var1, this, BeanHelper.flowControlParamsBeanSignatures);
      this.flowControlParamsBeanListener.initialize();
      var1 = (DescriptorBean)this.connectionFactoryBean.getLoadBalancingParams();
      this.loadBalancingParamsBeanListener = new GenericBeanListener(var1, this, BeanHelper.loadBalancingParamsBeanSignatures);
      this.loadBalancingParamsBeanListener.initialize();
      var1 = (DescriptorBean)this.connectionFactoryBean.getSecurityParams();
      this.securityParamsBeanListener = new GenericBeanListener(var1, this, BeanHelper.securityParamsBeanSignatures);
      this.securityParamsBeanListener.initialize();
   }

   public void registerBeanUpdateListeners() {
      DescriptorBean var1;
      if (this.connectionFactoryBeanListener != null) {
         this.connectionFactoryBeanListener.open();
      } else {
         var1 = (DescriptorBean)this.connectionFactoryBean;
         this.connectionFactoryBeanListener = new GenericBeanListener(var1, this, BeanHelper.connectionFactoryBeanSignatures);
      }

      if (this.defaultDeliveryParamsBeanListener != null) {
         this.defaultDeliveryParamsBeanListener.open();
      } else {
         var1 = (DescriptorBean)this.connectionFactoryBean.getDefaultDeliveryParams();
         this.defaultDeliveryParamsBeanListener = new GenericBeanListener(var1, this, BeanHelper.defaultDeliveryParamsBeanSignatures);
      }

      if (this.clientParamsBeanListener != null) {
         this.clientParamsBeanListener.open();
      } else {
         var1 = (DescriptorBean)this.connectionFactoryBean.getClientParams();
         this.clientParamsBeanListener = new GenericBeanListener(var1, this, BeanHelper.clientParamsBeanSignatures);
      }

      if (this.transactionParamsBeanListener != null) {
         this.transactionParamsBeanListener.open();
      } else {
         var1 = (DescriptorBean)this.connectionFactoryBean.getTransactionParams();
         this.transactionParamsBeanListener = new GenericBeanListener(var1, this, BeanHelper.transactionParamsBeanSignatures);
      }

      if (this.flowControlParamsBeanListener != null) {
         this.flowControlParamsBeanListener.open();
      } else {
         var1 = (DescriptorBean)this.connectionFactoryBean.getFlowControlParams();
         this.flowControlParamsBeanListener = new GenericBeanListener(var1, this, BeanHelper.flowControlParamsBeanSignatures);
      }

      if (this.loadBalancingParamsBeanListener != null) {
         this.loadBalancingParamsBeanListener.open();
      } else {
         var1 = (DescriptorBean)this.connectionFactoryBean.getLoadBalancingParams();
         this.loadBalancingParamsBeanListener = new GenericBeanListener(var1, this, BeanHelper.loadBalancingParamsBeanSignatures);
      }

      if (this.securityParamsBeanListener != null) {
         this.securityParamsBeanListener.open();
      } else {
         var1 = (DescriptorBean)this.connectionFactoryBean.getSecurityParams();
         this.securityParamsBeanListener = new GenericBeanListener(var1, this, BeanHelper.securityParamsBeanSignatures);
      }

   }

   public void unregisterBeanUpdateListeners() {
      if (this.securityParamsBeanListener != null) {
         this.securityParamsBeanListener.close();
         this.securityParamsBeanListener = null;
      }

      if (this.loadBalancingParamsBeanListener != null) {
         this.loadBalancingParamsBeanListener.close();
         this.loadBalancingParamsBeanListener = null;
      }

      if (this.flowControlParamsBeanListener != null) {
         this.flowControlParamsBeanListener.close();
         this.flowControlParamsBeanListener = null;
      }

      if (this.transactionParamsBeanListener != null) {
         this.transactionParamsBeanListener.close();
         this.transactionParamsBeanListener = null;
      }

      if (this.clientParamsBeanListener != null) {
         this.clientParamsBeanListener.close();
         this.clientParamsBeanListener = null;
      }

      if (this.defaultDeliveryParamsBeanListener != null) {
         this.defaultDeliveryParamsBeanListener.close();
         this.defaultDeliveryParamsBeanListener = null;
      }

      if (this.connectionFactoryBeanListener != null) {
         this.connectionFactoryBeanListener.close();
         this.connectionFactoryBeanListener = null;
      }

   }

   public boolean isDefaultConnectionFactory() {
      return this.defaultConnectionFactory;
   }

   public void objectChanged(NamingEvent var1) {
      if (var1.getType() == 1) {
         if (JMSDebug.JMSModule.isDebugEnabled()) {
            JMSDebug.JMSModule.debug("Got the unadvertise for " + this.name + " jndiName=" + this.jndiName);
         }

         synchronized(this.jndiNameRebindLock) {
            this.didUnbind = true;
            this.jndiNameRebindLock.notify();
         }
      }

   }

   public void namingExceptionThrown(NamingExceptionEvent var1) {
      JMSLogger.logJNDIDynamicChangeException(this.name, var1.getException().toString());
      synchronized(this.jndiNameRebindLock) {
         this.jndiNameRebindLock.notify();
      }
   }
}
