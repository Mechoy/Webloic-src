package weblogic.jms.bridge.internal;

import java.security.AccessController;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import javax.jms.BytesMessage;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.resource.ResourceException;
import javax.transaction.SystemException;
import javax.transaction.xa.Xid;
import weblogic.cluster.migration.Migratable;
import weblogic.cluster.migration.MigrationException;
import weblogic.jms.BridgeLogger;
import weblogic.jms.BridgeService;
import weblogic.jms.bridge.AdapterConnection;
import weblogic.jms.bridge.AdapterConnectionFactory;
import weblogic.jms.bridge.AdapterMetaData;
import weblogic.jms.bridge.ConnectionSpec;
import weblogic.jms.bridge.LocalTransaction;
import weblogic.jms.bridge.ResourceTransactionRolledBackException;
import weblogic.jms.bridge.SourceConnection;
import weblogic.jms.bridge.TargetConnection;
import weblogic.jndi.Environment;
import weblogic.management.ManagementException;
import weblogic.management.configuration.BridgeDestinationCommonMBean;
import weblogic.management.configuration.BridgeDestinationMBean;
import weblogic.management.configuration.BridgeLegalHelper;
import weblogic.management.configuration.JMSBridgeDestinationMBean;
import weblogic.management.configuration.MessagingBridgeMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.MessagingBridgeRuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.utils.GenericBeanListener;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;
import weblogic.transaction.Transaction;
import weblogic.transaction.TransactionManager;
import weblogic.transaction.TxHelper;
import weblogic.utils.StackTraceUtils;
import weblogic.work.WorkManager;

public final class MessagingBridge extends RuntimeMBeanDelegate implements Runnable, MessageListener, ExceptionListener, Migratable, MessagingBridgeRuntimeMBean, TimerListener {
   static final long serialVersionUID = 289399667450808114L;
   private static final String JNDI_FACTORY = "weblogic.jndi.WLInitialContextFactory";
   private static final int STATE_INITIALIZING = 0;
   private static final int STATE_INITIALIZED = 1;
   private static final int STATE_STARTED = 2;
   private static final int STATE_CONNECTED = 3;
   private static final int STATE_RUNNING = 4;
   private static final int STATE_CONTINUE = 5;
   private static final int STATE_STOPPING = 6;
   private static final int STATE_STOPPED = 7;
   private static final int STATE_TOBESTARTED = 8;
   private static final int STATE_RESTARTING = 9;
   private static final int STATE_SHUTTING_DOWN = 10;
   private static final int STATE_CLOSED = 11;
   private static final int STATE_SUSPENDED = 12;
   private static final int TRANSACTION_MODE_NONE = 1;
   private static final int TRANSACTION_MODE_LOCAL = 2;
   private static final int TRANSACTION_MODE_XARESOURCE = 3;
   private static final int QOS_EXACTLY_ONCE = 0;
   private static final int QOS_DUP_OKAY = 1;
   private static final int QOS_ATMOST_ONCE = 2;
   private static final int POLICY_AUTO = 0;
   private static final int POLICY_SCHEDULED = 1;
   private static final int POLICY_MANUAL = 2;
   private static final int MAX_BATCHES_TO_PROCESS = 10;
   private static final int SCANUNIT_INTERNAL = 1000;
   private static final int AUTO_ACK_IGNORE_XA = 99;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private String targetAdapterJndi;
   private Properties sourceProps;
   private AdapterMetaData sourceMetaData;
   private String sourceAdapterJndi;
   private Properties targetProps;
   private AdapterMetaData targetMetaData;
   private AdapterConnectionFactory sourceAdapterFactory;
   private ConnectionSpec sourceConnSpec;
   private SourceConnection sourceConn;
   private AdapterConnectionFactory targetAdapterFactory;
   private TargetConnection targetConn;
   private ConnectionSpec targetConnSpec;
   private MessagingBridgeMBean mbean;
   private int qos;
   private int forwardingPolicy;
   private boolean qosDegradAllowed;
   private boolean asyncEnabled = true;
   private boolean durabilityEnabled = true;
   private int transactionTimeout;
   private long maximumIdleTime;
   private String selector;
   private String scheduleTime;
   private boolean preserveMsgProperty;
   private int batchSize;
   private long batchInterval;
   private boolean idle;
   private boolean async;
   private boolean xaSupported;
   private boolean sourceXASupported;
   private boolean localTXSupported;
   private boolean stopped;
   private static final int EXACTLY_ONCE = 4;
   private static final int DUPLICATE_OKAY_LOCAL_TX = 3;
   private static final int DUPLICATE_OKAY_ACK = 2;
   private static final int DUPLICATE_OKAY_XA = 1;
   private static final int ATMOST_ONCE = 0;
   private int workMode;
   private RetryTimeController retryController;
   private boolean running;
   private boolean logBeginForwarding;
   private WorkManager workManager;
   private Context ctx;
   private TransactionManager tm;
   private BridgeService bridgeService;
   private static final long RELOOKUP_ADAPTERS_MILLISECONDS = 10000L;
   private long scanUnit = 1000L;
   private long lookupRetryTimeCurrent;
   private long connRetryTimeCurrent;
   private long connRetryTimeNext;
   private long onMessageIdleCurrent;
   private int logCount;
   private long flushingTime;
   private int state;
   private boolean sameMessageFormat;
   int health = 0;
   private ArrayList reasons = new ArrayList();
   private String[] stateCache = new String[2];
   private ClassLoader savedClassLoader;
   private static final String EOL = getEOL();
   private static final HashMap bridgeSignatures = new HashMap();
   private TimerManager timerManager;
   private Timer timer;
   private boolean lookupAdapterRetry = false;
   GenericBeanListener bridgeListener;
   private int count = 0;

   public MessagingBridge(MessagingBridgeMBean var1, BridgeService var2) throws ManagementException {
      super(var1.getName());
      this.mbean = var1;
      this.bridgeService = var2;
      this.state = 0;
      TimerManagerFactory var3 = TimerManagerFactory.getTimerManagerFactory();
      this.timerManager = var3.getDefaultTimerManager();
      this.timer = this.timerManager.scheduleAtFixedRate(this, 0L, this.scanUnit);
      this.retryController = new RetryTimeController();
      this.logBeginForwarding = true;
      this.health = 0;
      this.reasons.add(0, "Created but not yet active.");
      ManagementService.getRuntimeAccess(kernelId).getServerRuntime().setMessagingBridgeRuntime(this);
      Thread var4 = Thread.currentThread();
      this.savedClassLoader = var4.getContextClassLoader();
   }

   public void initialize() throws MessagingBridgeException {
      BridgeDestinationCommonMBean var1 = this.mbean.getSourceDestination();
      if (var1 == null) {
         BridgeLogger.logErrorNoSource(this.name);
         throw new MessagingBridgeConfigurationException("A messaging bridge must have a source destination configured");
      } else {
         this.sourceAdapterJndi = var1.getAdapterJNDIName();
         if (!(var1 instanceof JMSBridgeDestinationMBean)) {
            this.sourceProps = this.copyProperties(((BridgeDestinationMBean)var1).getProperties());
         } else {
            JMSBridgeDestinationMBean var2 = (JMSBridgeDestinationMBean)var1;
            if ((var2.getConnectionURL() == null || var2.getConnectionURL().length() == 0) && !var2.getInitialContextFactory().equals("weblogic.jndi.WLInitialContextFactory")) {
               BridgeLogger.logErrorInvalidURL(var2.getName());
               throw new MessagingBridgeConfigurationException("A bridge destination's connection URL cannot be null if the initial context factory is not weblogic.jndi.WLInitialContextFactory");
            }

            if (var2.getConnectionFactoryJNDIName() == null || var2.getDestinationJNDIName() == null) {
               BridgeLogger.logErrorNeedsJNDINames(var2.getName());
               throw new MessagingBridgeConfigurationException("A bridge destination for JMS has to have a connection factory JNDI name and a destination JNDI name configured.");
            }

            this.sourceProps = BridgeLegalHelper.createProperties(((JMSBridgeDestinationMBean)var1).getConnectionURL(), ((JMSBridgeDestinationMBean)var1).getInitialContextFactory(), ((JMSBridgeDestinationMBean)var1).getConnectionFactoryJNDIName(), ((JMSBridgeDestinationMBean)var1).getDestinationJNDIName(), ((JMSBridgeDestinationMBean)var1).getDestinationType());
         }

         if (BridgeDebug.MessagingBridgeStartup.isDebugEnabled()) {
            String var11 = new String();
            var11 = var11 + EOL + "  AdapterJNDIName=" + this.sourceAdapterJndi;
            var11 = var11 + EOL + "  Classpath=" + var1.getClasspath();

            String var4;
            for(Enumeration var3 = this.sourceProps.propertyNames(); var3.hasMoreElements(); var11 = var11 + EOL + "  " + var4 + " = " + this.sourceProps.get(var4)) {
               var4 = (String)var3.nextElement();
            }

            BridgeDebug.MessagingBridgeStartup.debug("Bridge " + this.name + "'s source configurations are:" + var11 + EOL);
         }

         BridgeDestinationCommonMBean var13 = this.mbean.getTargetDestination();
         if (var13 == null) {
            BridgeLogger.logErrorNoTarget(this.name);
            throw new MessagingBridgeConfigurationException("A messaging bridge must have a target destination configured");
         } else {
            this.targetAdapterJndi = var13.getAdapterJNDIName();
            if (!(var13 instanceof JMSBridgeDestinationMBean)) {
               this.targetProps = this.copyProperties(((BridgeDestinationMBean)var13).getProperties());
            } else {
               JMSBridgeDestinationMBean var12 = (JMSBridgeDestinationMBean)var13;
               if ((var12.getConnectionURL() == null || var12.getConnectionURL().length() == 0) && !var12.getInitialContextFactory().equals("weblogic.jndi.WLInitialContextFactory")) {
                  BridgeLogger.logErrorInvalidURL(var12.getName());
                  throw new MessagingBridgeConfigurationException("A bridge destination's connection URL cannot be null if the initial context factory is not weblogic.jndi.WLInitialContextFactory");
               }

               if (var12.getConnectionFactoryJNDIName() == null || var12.getDestinationJNDIName() == null) {
                  BridgeLogger.logErrorNeedsJNDINames(var12.getName());
                  throw new MessagingBridgeConfigurationException("A bridge destination for JMS has to have a connection factory  JNDI name and a destination JNDI name configured.");
               }

               this.targetProps = BridgeLegalHelper.createProperties(((JMSBridgeDestinationMBean)var13).getConnectionURL(), ((JMSBridgeDestinationMBean)var13).getInitialContextFactory(), ((JMSBridgeDestinationMBean)var13).getConnectionFactoryJNDIName(), ((JMSBridgeDestinationMBean)var13).getDestinationJNDIName(), ((JMSBridgeDestinationMBean)var13).getDestinationType());
            }

            if (BridgeDebug.MessagingBridgeStartup.isDebugEnabled()) {
               String var14 = new String();
               var14 = var14 + EOL + "  AdapterJNDIName=" + this.targetAdapterJndi;
               var14 = var14 + EOL + "  Classpath=" + var13.getClasspath();

               String var5;
               for(Enumeration var15 = this.targetProps.propertyNames(); var15.hasMoreElements(); var14 = var14 + EOL + "  " + var5 + " = " + this.targetProps.get(var5)) {
                  var5 = (String)var15.nextElement();
               }

               BridgeDebug.MessagingBridgeStartup.debug("Bridge " + this.name + "'s target configurations are:" + var14 + EOL);
            }

            if (!BridgeLegalHelper.notSameDestinations(this.sourceProps, this.targetProps)) {
               BridgeLogger.logErrorSameSourceTarget(this.name);
               throw new MessagingBridgeConfigurationException("A messaging bridge's source destination cannot be the same as the target destination.");
            } else {
               this.stopped = !this.mbean.isStarted();
               this.asyncEnabled = this.mbean.isAsyncEnabled();
               this.durabilityEnabled = this.mbean.isDurabilityEnabled();
               long var16 = (long)(this.mbean.getReconnectDelayMinimum() * 1000);
               long var17 = (long)(this.mbean.getReconnectDelayMaximum() * 1000);
               long var7 = (long)(this.mbean.getReconnectDelayIncrease() * 1000);
               this.transactionTimeout = this.mbean.getTransactionTimeout();
               this.maximumIdleTime = (long)(this.mbean.getIdleTimeMaximum() * 1000);
               if (this.transactionTimeout <= 0) {
                  this.transactionTimeout = 1;
               }

               if (var16 > var17) {
                  var17 = var16;
               }

               this.retryController.init(var16, var7, var17);
               this.qosDegradAllowed = this.mbean.isQOSDegradationAllowed();
               this.selector = this.mbean.getSelector();
               this.batchSize = this.mbean.getBatchSize();
               this.batchInterval = this.mbean.getBatchInterval();
               if (this.batchInterval < 0L) {
                  this.flushingTime = (long)(800 * this.transactionTimeout);
               } else {
                  this.flushingTime = this.batchInterval;
               }

               this.scheduleTime = this.mbean.getScheduleTime();
               this.preserveMsgProperty = this.mbean.getPreserveMsgProperty();
               String var9 = this.mbean.getForwardingPolicy();
               if (var9 == null) {
                  this.forwardingPolicy = 0;
               } else if (var9.equalsIgnoreCase("AUTOMATIC")) {
                  this.forwardingPolicy = 0;
               } else if (var9.equalsIgnoreCase("MANUAL")) {
                  this.forwardingPolicy = 2;
               } else if (var9.equalsIgnoreCase("SCHEDULED")) {
                  this.forwardingPolicy = 1;
               }

               String var10 = this.mbean.getQualityOfService();
               if (var10 == null) {
                  this.qos = 0;
               } else if (var10.equalsIgnoreCase("EXACTLY-ONCE")) {
                  this.qos = 0;
               } else if (var10.equalsIgnoreCase("DUPLICATE-OKAY")) {
                  this.qos = 1;
               } else if (var10.equalsIgnoreCase("ATMOST-ONCE")) {
                  this.qos = 2;
               }

               this.bridgeListener = new GenericBeanListener(this.mbean, this, bridgeSignatures);
               this.state = 1;
               if (BridgeDebug.MessagingBridgeStartup.isDebugEnabled()) {
                  BridgeDebug.MessagingBridgeStartup.debug("Bridge " + this.name + " is successfully initialized");
               }

            }
         }
      }
   }

   public void resume() throws MessagingBridgeException {
      synchronized(this) {
         if (this.state != 1 && this.state != 12) {
            throw new MessagingBridgeException("Failed to initialize bridge " + this.name);
         }
      }

      this.workManager = this.bridgeService.getWorkManager();
      if (!this.bridgeService.findAdapterAndRegister(this.sourceAdapterJndi, this)) {
         if (this.lookupAdapterRetry) {
            BridgeLogger.logWarningAdapterNotFound(this.name, this.sourceAdapterJndi);
            synchronized(this.reasons) {
               this.health = 1;
               this.reasons.add(0, "WARN: Failed to find the source adapter.");
            }
         } else {
            this.lookupAdapterRetry = true;
         }

      } else if (!this.bridgeService.findAdapterAndRegister(this.targetAdapterJndi, this)) {
         BridgeLogger.logWarningAdapterNotFound(this.name, this.targetAdapterJndi);
         synchronized(this.reasons) {
            this.health = 1;
            this.reasons.add(0, "WARN: Failed to find the target adapter.");
         }
      } else {
         synchronized(this.reasons) {
            this.health = 0;
            this.reasons.add(0, "Found both of the adapters and making the connections");
         }

         try {
            this.ctx = getContext();
         } catch (NamingException var9) {
            throw new MessagingBridgeException("Failed to get initial context");
         }

         if (this.timer != null) {
            this.timer.cancel();
         }

         this.timer = this.timerManager.scheduleAtFixedRate(this, 0L, this.scanUnit);
         synchronized(this) {
            if (this.isStopped()) {
               this.health = 1;
               this.reasons.add(0, "Stopped by the administrator.");
               BridgeLogger.logInfoInitiallyStopped(this.name);
               return;
            }

            this.state = 1;
            this.running = true;
         }

         this.workManager.schedule(this);
      }
   }

   private void startInternal() throws MessagingBridgeException {
      boolean var1 = false;

      String var2;
      String var5;
      try {
         if (this.ctx == null) {
            try {
               this.ctx = getContext();
            } catch (NamingException var15) {
               throw new MessagingBridgeException("Failed to get initial context");
            }
         }

         if (this.sourceAdapterFactory == null) {
            this.sourceAdapterFactory = (AdapterConnectionFactory)this.ctx.lookup(this.sourceAdapterJndi);
         }

         if (this.targetAdapterFactory == null) {
            this.targetAdapterFactory = (AdapterConnectionFactory)this.ctx.lookup(this.targetAdapterJndi);
         }

         this.sourceMetaData = this.sourceAdapterFactory.getMetaData();
         this.targetMetaData = this.targetAdapterFactory.getMetaData();
         this.sameMessageFormat = this.sourceMetaData.getNativeMessageFormat().equals(this.targetMetaData.getNativeMessageFormat());
         var2 = null;
         var5 = this.sourceAdapterFactory.getTransactionSupport();
         byte var3;
         if (var5.equals("XATransaction")) {
            var3 = 3;
         } else if (var5.equals("LocalTransaction")) {
            var3 = 2;
         } else {
            var3 = 1;
         }

         String var6 = this.targetAdapterFactory.getTransactionSupport();
         byte var4;
         if (var6.equals("XATransaction")) {
            var4 = 3;
         } else if (var6.equals("LocalTransaction")) {
            var4 = 2;
         } else {
            var4 = 1;
         }

         if (var3 == 3) {
            this.sourceXASupported = true;
            if (var4 == 3) {
               this.xaSupported = true;
            }
         } else if (var3 == 2) {
            this.localTXSupported = true;
         }
      } catch (NamingException var16) {
         var1 = true;
         if (this.logCount++ == 5) {
            BridgeLogger.logInfoAdaptersLookupFailed(this.name, var16);
            this.logCount = 0;
         }
      } catch (ResourceException var17) {
         BridgeLogger.logErrorFailGetAdpInfo(this.name, var17);
         var1 = true;
      } catch (Throwable var18) {
         BridgeLogger.logStackTrace(var18);
         var1 = true;
      }

      if (var1) {
         synchronized(this.reasons) {
            this.health = 1;
            if (this.sourceAdapterFactory == null) {
               this.reasons.add(0, "WARN: failed to look up the source adapter.");
            } else {
               this.reasons.add(0, "WARN: failed to look up the target adapter.");
            }

         }
      } else {
         this.logCount = 0;
         BridgeLogger.logInfoAdaptersFound(this.name);
         synchronized(this.reasons) {
            this.health = 0;
            this.reasons.add(0, "Found two adapters and about to make connections.");
         }

         this.sourceConnSpec = null;
         var2 = this.mbean.getSourceDestination().getUserName();
         String var19;
         if (var2 != null && var2.length() > 0) {
            this.sourceProps.put(new String("username"), var2);
            var19 = this.mbean.getSourceDestination().getUserPassword();
            if (var19 == null) {
               var19 = "";
            }

            this.sourceProps.put(new String("password"), var19);
         }

         this.sourceProps.put(new String("name"), this.name);
         if (this.durabilityEnabled) {
            this.sourceProps.put(new String("durability"), new String("true"));
         } else {
            this.sourceProps.put(new String("durability"), new String("false"));
         }

         if (this.selector != null) {
            this.sourceProps.put(new String("selector"), this.selector);
         }

         var19 = this.mbean.getSourceDestination().getClasspath();
         if (var19 != null) {
            this.sourceProps.put(new String("classpath"), var19);
         }

         if (this.sourceProps != null && this.sourceProps.size() > 0) {
            try {
               this.sourceConnSpec = this.sourceAdapterFactory.createConnectionSpec(this.sourceProps);
            } catch (ResourceException var13) {
               BridgeLogger.logErrorInvalidSourceProps(this.name);
               return;
            }
         }

         this.targetConnSpec = null;
         this.targetProps.put(new String("name"), this.name);
         String var20 = this.mbean.getTargetDestination().getUserName();
         if (var20 != null && var20.length() > 0) {
            this.targetProps.put(new String("username"), var20);
            var5 = this.mbean.getTargetDestination().getUserPassword();
            if (var5 == null) {
               var5 = "";
            }

            this.targetProps.put(new String("password"), var5);
         }

         var19 = this.mbean.getTargetDestination().getClasspath();
         if (var19 != null) {
            this.targetProps.put(new String("classpath"), var19);
         }

         if (this.preserveMsgProperty) {
            this.targetProps.put(new String("preserveMsgProperty"), "true");
         }

         if (this.targetProps != null && this.targetProps.size() > 0) {
            try {
               this.targetConnSpec = this.targetAdapterFactory.createConnectionSpec(this.targetProps);
            } catch (ResourceException var12) {
               BridgeLogger.logErrorInvalidTargetProps(this.name);
               return;
            }
         }

         if (BridgeDebug.MessagingBridgeStartup.isDebugEnabled()) {
            BridgeDebug.MessagingBridgeStartup.debug("BridgeBridge " + this.name + " forwarding policy is " + this.policyToString(this.forwardingPolicy));
         }

         synchronized(this) {
            this.state = 2;
         }

         this.retryController.reset();
         this.connRetryTimeNext = this.retryController.getNextRetryTime();
      }
   }

   private void getConnections() {
      BridgeLogger.logInfoGetConnections(this.name);
      synchronized(this) {
         if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
            BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Bridge " + this.name + " In getConnections: isStopped = " + this.isStopped());
         }

         if (this.isShutdownOrSuspended() || this.isStopped()) {
            this.logBeginForwarding = true;
            BridgeLogger.logInfoShuttingdown(this.name);
            return;
         }
      }

      boolean var1 = false;
      Throwable var2 = null;

      try {
         if (BridgeDebug.MessagingBridgeRuntime.isDebugEnabled()) {
            BridgeDebug.MessagingBridgeRuntime.debug("Bridge " + this.name + " Getting source connection");
         }

         this.sourceConn = this.sourceAdapterFactory.getSourceConnection(this.sourceConnSpec);
      } catch (Throwable var18) {
         var1 = true;
         var2 = var18;
      }

      if (!var1 && this.sourceConn != null) {
         if (BridgeDebug.MessagingBridgeRuntime.isDebugEnabled()) {
            BridgeDebug.MessagingBridgeRuntime.debug("Bridge " + this.name + " Successfully got connection to the source destination");
         }

         synchronized(this.reasons) {
            this.health = 0;
            this.reasons.add(0, "Connected to the source.");
         }

         if (BridgeDebug.MessagingBridgeRuntime.isDebugEnabled()) {
            BridgeDebug.MessagingBridgeRuntime.debug("Bridge " + this.name + " Getting target connection");
         }

         try {
            this.targetConn = this.targetAdapterFactory.getTargetConnection(this.targetConnSpec);
         } catch (Throwable var15) {
            var1 = true;
            var2 = var15;
         }

         if (!var1 && this.targetConn != null) {
            if (BridgeDebug.MessagingBridgeRuntime.isDebugEnabled()) {
               BridgeDebug.MessagingBridgeRuntime.debug("Bridge " + this.name + " Successfully got connection to the target destination");
            }

            synchronized(this.reasons) {
               this.health = 0;
               this.reasons.add(0, "Connected to the target.");
            }

            try {
               this.workMode = this.determineWorkMode(this.sourceMetaData);
            } catch (MessagingBridgeException var19) {
               BridgeLogger.logErrorQOSNotAvail(this.name, this.mbean.getQualityOfService());
               if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
                  var19.printStackTrace();
               }

               try {
                  this.shutdown();
               } catch (MessagingBridgeException var10) {
               }

               return;
            }

            if (BridgeDebug.MessagingBridgeRuntime.isDebugEnabled()) {
               BridgeDebug.MessagingBridgeRuntime.debug("Bridge " + this.name + " WorkMode = " + this.workModeToString(this.workMode));
            }

            BridgeLogger.logInfoWorkMode(this.name, this.mbean.getQualityOfService(), this.workModeToQOS(this.workMode));

            try {
               if (this.workMode == 2) {
                  this.sourceConn.setAcknowledgeMode(2);
               }

               if (this.workMode == 0) {
                  this.sourceConn.setAcknowledgeMode(99);
               }
            } catch (Throwable var12) {
               this.prepareForRebegin(var12);
            }

            synchronized(this) {
               this.state = 3;
            }
         } else {
            synchronized(this) {
               if (this.sourceConn != null) {
                  this.closeConnection(this.sourceConn);
                  this.sourceConn = null;
               }
            }

            this.connRetryTimeNext = this.retryController.getNextRetryTime();
            BridgeLogger.logErrorFailedToConnectToTarget(this.name, this.createExceptionWithLinkedExceptionInfo(var2), this.connRetryTimeNext / 1000L);
            synchronized(this.reasons) {
               this.health = 1;
               this.reasons.add(0, "WARN: failed to connect to the target.");
            }
         }
      } else {
         this.connRetryTimeNext = this.retryController.getNextRetryTime();
         BridgeLogger.logErrorFailedToConnectToSource(this.name, this.createExceptionWithLinkedExceptionInfo(var2), this.connRetryTimeNext / 1000L);
         synchronized(this.reasons) {
            this.health = 1;
            this.reasons.add(0, "WARN: failed to connect to the source.");
         }
      }
   }

   private void beginForwarding() {
      if (this.logBeginForwarding) {
         BridgeLogger.logInfoBeginForwaring(this.name);
         this.logBeginForwarding = false;
      } else if (BridgeDebug.MessagingBridgeRuntime.isDebugEnabled()) {
         BridgeLogger.logInfoBeginForwaring(this.name);
      }

      synchronized(this) {
         if (this.isNotAvail()) {
            this.logBeginForwarding = true;
            BridgeLogger.logInfoShuttingdown(this.name);
            return;
         }
      }

      synchronized(this.reasons) {
         this.health = 0;
         this.reasons.add(0, "Forwarding messages.");
      }

      try {
         this.sourceConn.setExceptionListener(this);
         this.targetConn.setExceptionListener(this);
         if (this.async) {
            this.idle = true;
            if (this.maximumIdleTime <= 0L) {
               this.maximumIdleTime = 60000L;
            }

            this.sourceConn.setMessageListener(this);
            synchronized(this) {
               this.state = 4;
               this.running = false;
            }
         } else {
            this.processMessages();
         }
      } catch (NullPointerException var6) {
         this.prepareForRebegin(var6);
      } catch (Throwable var7) {
         this.prepareForRebegin(var7);
      }

   }

   public void run() {
      if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
         BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Bridge " + this.name + " in run(): state = " + this.state);
      }

      label251: {
         switch (this.state) {
            case 1:
               synchronized(this) {
                  if (this.isStopped()) {
                     this.running = false;
                     return;
                  }
               }

               try {
                  this.startInternal();
               } catch (Exception var26) {
                  this.running = false;
                  return;
               }

               synchronized(this) {
                  if (this.state != 2) {
                     this.running = false;
                     return;
                  }
               }
            case 2:
               break;
            case 3:
            case 5:
               break label251;
            case 4:
            case 10:
            default:
               synchronized(this) {
                  this.running = false;
               }

               if (BridgeDebug.MessagingBridgeRuntime.isDebugEnabled()) {
                  BridgeDebug.MessagingBridgeRuntime.debug("Internal error -- Invalid state: " + this.state);
               }

               return;
            case 6:
               try {
                  this.stopForwarding();
               } catch (Exception var21) {
               }

               synchronized(this) {
                  this.running = false;
                  return;
               }
            case 7:
            case 11:
            case 12:
               synchronized(this) {
                  this.running = false;
                  return;
               }
            case 8:
               try {
                  this.stopForwarding();
               } catch (Exception var18) {
               }

               synchronized(this) {
                  this.state = 2;
                  this.running = false;
                  return;
               }
            case 9:
               synchronized(this) {
                  if (this.isStopped()) {
                     this.running = false;
                     return;
                  }
               }

               try {
                  if (this.async && this.onMessageIdleCurrent >= this.maximumIdleTime) {
                     if (BridgeDebug.MessagingBridgeRuntime.isDebugEnabled()) {
                        BridgeLogger.logInfoAsyncReconnect(this.name);
                     }
                  } else {
                     BridgeLogger.logInfoSyncReconnect(this.name);
                  }

                  this.stopForwarding();
                  synchronized(this) {
                     this.state = 2;
                  }

                  this.getConnections();
                  this.beginForwarding();
               } catch (Exception var29) {
               }

               synchronized(this) {
                  this.running = false;
                  return;
               }
         }

         synchronized(this) {
            if (this.isStopped()) {
               this.running = false;
               return;
            }
         }

         try {
            this.getConnections();
         } catch (Exception var28) {
            if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
               BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Bridge " + this.name + " Failed to get connections because of " + var28);
            }

            this.running = false;
            return;
         }

         synchronized(this) {
            if (this.state != 3) {
               this.running = false;
               return;
            }
         }
      }

      synchronized(this) {
         if (this.isStopped()) {
            this.running = false;
            return;
         }
      }

      try {
         this.beginForwarding();
      } catch (Exception var23) {
      }

      synchronized(this) {
         this.running = false;
      }
   }

   private void getTransactionManager() {
      this.tm = TxHelper.getTransactionManager();

      try {
         this.tm.setTransactionTimeout(this.transactionTimeout);
      } catch (SystemException var2) {
      }

   }

   private void closeConnection(AdapterConnection var1) {
      try {
         var1.close();
      } catch (ResourceException var3) {
      }

   }

   private void cleanup() {
      if (this.sourceConn != null) {
         synchronized(this.sourceConn) {
            if (!this.async) {
               try {
                  this.sourceConn.recover();
               } catch (Exception var6) {
               }
            }

            this.closeConnection(this.sourceConn);
         }

         this.sourceConn = null;
      }

      if (this.targetConn != null) {
         synchronized(this.targetConn) {
            this.closeConnection(this.targetConn);
         }

         this.targetConn = null;
      }

   }

   private void stopForwarding() {
      SourceConnection var1 = null;
      TargetConnection var2 = null;
      synchronized(this) {
         if (this.isShutdownOrSuspended() || this.state == 7) {
            return;
         }

         this.state = 7;
         if (this.sourceConn != null) {
            var1 = this.sourceConn;
         }

         if (this.targetConn != null) {
            var2 = this.targetConn;
         }
      }

      if (var1 != null) {
         try {
            if (!this.async) {
               ((SourceConnection)var1).recover();
            }
         } catch (Exception var9) {
         }

         synchronized(var1) {
            this.closeConnection(var1);
         }
      }

      if (var2 != null) {
         synchronized(var2) {
            this.closeConnection(var2);
         }
      }

      BridgeLogger.logInfoStopped(this.name);
   }

   public void onMessage(Message var1) {
      Thread var2 = Thread.currentThread();
      ClassLoader var3 = var2.getContextClassLoader();

      try {
         var2.setContextClassLoader(this.savedClassLoader);
         this.onMessageInternal(var1);
      } finally {
         var2.setContextClassLoader(var3);
      }

   }

   private void onMessageInternal(Message var1) {
      String var2 = null;
      if (BridgeDebug.MessagingBridgeRuntime.isDebugEnabled()) {
         BridgeDebug.MessagingBridgeRuntime.debug("Bridge: " + this.name + " (onMessage()) received message: " + EOL + this.constructLogForReceivedMessage(var1));

         try {
            var2 = var1.getJMSMessageID();
         } catch (JMSException var7) {
         }
      }

      boolean var3 = false;
      synchronized(this) {
         this.idle = false;
         if (this.isNotAvail()) {
            var3 = true;
         }
      }

      if (var3 && this.sourceConn != null) {
         this.logBeginForwarding = true;
         BridgeLogger.logInfoShuttingdown(this.name);

         try {
            this.sourceConn.recover();
            if (BridgeDebug.MessagingBridgeRuntime.isDebugEnabled()) {
               BridgeDebug.MessagingBridgeRuntime.debug("Bridge: " + this.name + " (onMessage()) recovered: " + var1.getJMSMessageID());
            }
         } catch (Exception var6) {
         }

      } else {
         try {
            if (this.workMode == 4 || this.workMode == 1) {
               this.getTransactionManager();
               this.tm.begin("Messaging Bridge");
               this.sourceConn.associateTransaction(var1);
               if (BridgeDebug.MessagingBridgeRuntime.isDebugEnabled()) {
                  Transaction var4 = TxHelper.getTransaction();
                  Xid var5 = var4 == null ? null : var4.getXID();
                  BridgeDebug.MessagingBridgeRuntime.debug("Bridge: " + this.name + " (onMessage()) associated msg " + var1.getJMSMessageID() + " with transaction " + var5);
               }
            }

            Message var11 = this.targetConn.createMessage(var1);
            this.targetConn.send(var11);
            if (BridgeDebug.MessagingBridgeRuntime.isDebugEnabled()) {
               BridgeDebug.MessagingBridgeRuntime.debug("Bridge: " + this.name + " (onMessage())" + " successfully sent message: " + EOL + this.constructLogForSentMessage(var2, var1));
            }

            if (this.workMode != 4 && this.workMode != 1) {
               if (this.workMode == 2) {
                  this.sourceConn.acknowledge(var1);
                  if (BridgeDebug.MessagingBridgeRuntime.isDebugEnabled()) {
                     BridgeDebug.MessagingBridgeRuntime.debug("Bridge: " + this.name + " (onMessage()) acknowledged the msg");
                  }
               }
            } else {
               this.tm.commit();
               if (BridgeDebug.MessagingBridgeRuntime.isDebugEnabled()) {
                  BridgeDebug.MessagingBridgeRuntime.debug("Bridge: " + this.name + " (onMessage()) committed the transaction");
               }
            }
         } catch (Throwable var9) {
            try {
               if (this.workMode != 4 && this.workMode != 1) {
                  if (this.workMode == 2) {
                     if (BridgeDebug.MessagingBridgeRuntime.isDebugEnabled()) {
                        BridgeDebug.MessagingBridgeRuntime.debug("Bridge: " + this.name + " (onMessage()) calling recover");
                     }

                     this.sourceConn.recover();
                  }
               } else {
                  this.tm.rollback();
                  if (BridgeDebug.MessagingBridgeRuntime.isDebugEnabled()) {
                     BridgeDebug.MessagingBridgeRuntime.debug("Bridge: " + this.name + " (onMessage()) rolled back the transaction");
                  }
               }
            } catch (Exception var8) {
            }

            this.prepareForRebegin(var9);
         }

      }
   }

   void processMessages() throws MessagingBridgeException {
      if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
         BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Bridge " + this.name + " Entering processMessages() ------ ");
      }

      if (this.async) {
         this.throwMessagingBridgeException("Messaging bridge internal error", (Exception)null);
      }

      for(int var1 = 0; var1 < 10; ++var1) {
         long var2 = System.currentTimeMillis();
         LocalTransaction var5 = null;
         synchronized(this) {
            if (this.isNotAvail() || this.sourceConn == null || this.targetConn == null) {
               this.logBeginForwarding = true;
               BridgeLogger.logInfoShuttingdown(this.name);
               return;
            }

            this.state = 5;
         }

         synchronized(this.sourceConn) {
            synchronized(this.targetConn) {
               try {
                  Message var4;
                  if (this.workMode != 4 && this.workMode != 1) {
                     if (this.workMode == 3) {
                        var5 = this.sourceConn.getLocalTransaction();
                        var5.begin();
                        var4 = this.sourceConn.receive(this.flushingTime);
                     } else {
                        var4 = this.sourceConn.receive(this.maximumIdleTime);
                     }
                  } else {
                     this.getTransactionManager();
                     this.tm.begin("Messaging Bridge");
                     var4 = this.sourceConn.receive(this.flushingTime);
                  }

                  String var8 = null;
                  if (var4 == null) {
                     try {
                        if (this.workMode != 4 && this.workMode != 1) {
                           if (this.workMode == 3) {
                              var5.rollback();
                           } else if (this.workMode == 2) {
                              this.sourceConn.recover();
                           }
                        } else {
                           this.tm.rollback();
                        }
                     } catch (Exception var19) {
                     }
                  } else {
                     int var9 = 0;
                     Message var10;
                     if (this.workMode == 4 || this.workMode == 1 || this.workMode == 3) {
                        for(; var4 != null && var9 < this.batchSize - 1; ++var9) {
                           if (BridgeDebug.MessagingBridgeRuntime.isDebugEnabled()) {
                              BridgeDebug.MessagingBridgeRuntime.debug("Bridge: " + this.name + " (processMessages()) received message: " + EOL + this.constructLogForReceivedMessage(var4));

                              try {
                                 var8 = var4.getJMSMessageID();
                              } catch (JMSException var18) {
                              }
                           }

                           var10 = this.targetConn.createMessage(var4);
                           this.targetConn.send(var10);
                           if (BridgeDebug.MessagingBridgeRuntime.isDebugEnabled()) {
                              BridgeDebug.MessagingBridgeRuntime.debug("Bridge: " + this.name + " (processMessages())" + " successfully sent message: " + EOL + this.constructLogForSentMessage(var8, var4));
                           }

                           if (this.workMode == 2) {
                              this.sourceConn.acknowledge(var4);
                              if (BridgeDebug.MessagingBridgeRuntime.isDebugEnabled()) {
                                 BridgeDebug.MessagingBridgeRuntime.debug("Bridge: " + this.name + " (processMessages())" + " successfully acknowledged message: " + var8);
                              }
                           }

                           if (this.workMode != 4 && this.workMode != 1) {
                              var4 = this.sourceConn.receive(0L);
                           } else {
                              long var11 = this.flushingTime - (System.currentTimeMillis() - var2);
                              if (var11 < 0L) {
                                 var11 = 0L;
                              }

                              var4 = this.sourceConn.receive(var11);
                           }
                        }
                     }

                     if (var4 != null) {
                        if (BridgeDebug.MessagingBridgeRuntime.isDebugEnabled()) {
                           BridgeDebug.MessagingBridgeRuntime.debug("Bridge: " + this.name + " (processMessages()) received message: " + EOL + this.constructLogForReceivedMessage(var4));

                           try {
                              var8 = var4.getJMSMessageID();
                           } catch (JMSException var17) {
                           }
                        }

                        var10 = this.targetConn.createMessage(var4);
                        this.targetConn.send(var10);
                        if (BridgeDebug.MessagingBridgeRuntime.isDebugEnabled()) {
                           BridgeDebug.MessagingBridgeRuntime.debug("Bridge: " + this.name + " (processMessages())" + " successfully sent message: " + EOL + this.constructLogForSentMessage(var8, var4));
                        }

                        if (this.workMode == 2) {
                           this.sourceConn.acknowledge(var4);
                           if (BridgeDebug.MessagingBridgeRuntime.isDebugEnabled()) {
                              BridgeDebug.MessagingBridgeRuntime.debug("Bridge: " + this.name + " (processMessages())" + " successfully acknowledged message: " + var8);
                           }
                        }
                     }

                     if (this.workMode != 4 && this.workMode != 1) {
                        if (this.workMode == 3) {
                           var5.commit();
                        }
                     } else {
                        this.tm.commit();
                        if (BridgeDebug.MessagingBridgeRuntime.isDebugEnabled()) {
                           BridgeDebug.MessagingBridgeRuntime.debug("Bridge: " + this.name + " (processMessages())" + " committed the transaction");
                        }
                     }
                  }
               } catch (Throwable var21) {
                  if (!(var21 instanceof ResourceTransactionRolledBackException)) {
                     BridgeLogger.logErrorProcessMsgs(this.name, this.createExceptionWithLinkedExceptionInfo(var21));

                     try {
                        if (this.workMode != 4 && this.workMode != 1) {
                           if (this.workMode == 3) {
                              var5.rollback();
                           } else if (this.workMode == 2) {
                              this.sourceConn.recover();
                           }
                        } else {
                           this.tm.rollback();
                        }
                     } catch (Exception var20) {
                     }

                     synchronized(this) {
                        this.state = 9;
                     }

                     this.throwMessagingBridgeException("Messaging bridge operation failed", var21);
                  }
               }
            }
         }
      }

   }

   public void onException(JMSException var1) {
      this.prepareForRebegin(var1);
   }

   private void prepareForRebegin(Throwable var1) {
      synchronized(this) {
         if (this.state == 8 || this.state == 2) {
            return;
         }
      }

      BridgeLogger.logInfoReconnect(this.name, this.createExceptionWithLinkedExceptionInfo(var1));
      boolean var2 = false;
      synchronized(this) {
         this.state = 8;
         if (this.async) {
            this.running = false;
         }

         if (!this.running) {
            var2 = true;
            this.running = true;
         }

         this.logBeginForwarding = true;
      }

      synchronized(this.reasons) {
         this.health = 1;
         this.reasons.add(0, "WARN: failed and will reconnect later.");
      }

      this.retryController.reset();
      this.connRetryTimeNext = this.retryController.getNextRetryTime();
      if (var2) {
         this.workManager.schedule(this);
      }

   }

   private int determineWorkMode(AdapterMetaData var1) throws MessagingBridgeException {
      if (var1.supportsAsynchronousMode() && this.asyncEnabled) {
         this.async = true;
      } else {
         this.async = false;
      }

      if (BridgeDebug.MessagingBridgeRuntime.isDebugEnabled()) {
         BridgeDebug.MessagingBridgeRuntime.debug("Bridge " + this.name + ": both source and target adapters support XA = " + this.xaSupported);
      }

      if (this.qosDegradAllowed) {
         BridgeLogger.logInfoQOSDegradationAllowed(this.name);
      } else {
         BridgeLogger.logInfoQOSDegradationNotAllowed(this.name);
      }

      boolean var2 = false;
      boolean var3 = false;

      try {
         var2 = var1.supportsMDBTransaction() && this.sourceConn.getMetaData().implementsMDBTransaction();
         var3 = this.xaSupported && this.sourceConn.getMetaData().isXAConnection() && this.targetConn.getMetaData().isXAConnection();
      } catch (ResourceException var5) {
      }

      if (BridgeDebug.MessagingBridgeRuntime.isDebugEnabled()) {
         BridgeDebug.MessagingBridgeRuntime.debug("Bridge " + this.name + " supportsMDBTX = " + var2 + " supportsXA = " + var3 + " async = " + this.async);
      }

      switch (this.qos) {
         case 0:
            if (var3) {
               if (this.async && !var2) {
                  this.async = false;
               }

               return 4;
            } else if (!this.qosDegradAllowed) {
               throw new MessagingBridgeException("QOS cannot be satisfied");
            }
         case 1:
            if (var1.supportsAcknowledgement()) {
               return 2;
            } else if (this.sourceXASupported) {
               if (!var2) {
                  this.async = false;
               }

               return 1;
            } else if (this.localTXSupported) {
               this.async = false;
               return 3;
            } else if (!this.qosDegradAllowed) {
               throw new MessagingBridgeException("QOS cannot be satisfied");
            }
         case 2:
            return 0;
         default:
            throw new MessagingBridgeException("Invalid quality of service");
      }
   }

   private String workModeToString(int var1) {
      switch (var1) {
         case 0:
            return "Atmost-once";
         case 1:
            return "Duplicate-okay-xa";
         case 2:
            return "Duplicate-okay-ack";
         case 3:
            return "Duplicate-okay-local";
         case 4:
            return "Exactly-once";
         default:
            return "Mode not supported";
      }
   }

   private String workModeToQOS(int var1) {
      switch (var1) {
         case 0:
            return "Atmost_once";
         case 1:
         case 2:
         case 3:
            return "Duplicate-okay";
         case 4:
            return "Exactly-once";
         default:
            return "Mode not supported";
      }
   }

   public void suspend(boolean var1) {
      synchronized(this) {
         if (this.state == 11 || this.state == 12) {
            return;
         }

         this.state = 12;
      }

      synchronized(this.reasons) {
         this.health = 1;
         this.reasons.add(0, "WARN: Bridge " + this.name + " is suspended because of server suspension.");
      }

      this.timer.cancel();
      this.cleanup();
      this.sourceAdapterFactory = null;
      this.targetAdapterFactory = null;
   }

   public void shutdown() throws MessagingBridgeException {
      synchronized(this) {
         if (this.state == 11) {
            return;
         }

         this.state = 11;
      }

      try {
         if (this.bridgeListener != null) {
            this.bridgeListener.close();
         }

         this.timer.cancel();
         this.cleanup();
      } catch (Exception var12) {
      } finally {
         try {
            this.unregister();
         } catch (Exception var11) {
         }

      }

      this.logBeginForwarding = true;
      BridgeLogger.logInfoShutdown(this.name);
   }

   public MessagingBridgeMBean getMBean() {
      return this.mbean;
   }

   public synchronized void markShuttingDown() {
      if (this.state != 10 && this.state != 11) {
         this.state = 10;
      }

   }

   private synchronized boolean isShutdownOrSuspended() {
      return this.state == 11 || this.state == 10 || this.state == 12;
   }

   private boolean isStopped() {
      return this.stopped;
   }

   private boolean isNotAvail() {
      return this.state == 11 || this.state == 10 || this.state == 12 || this.state == 8 || this.state == 2 || this.state == 7 || this.stopped;
   }

   public void migratableActivate() throws MigrationException {
      if (BridgeDebug.MessagingBridgeRuntime.isDebugEnabled()) {
         BridgeDebug.MessagingBridgeRuntime.debug("Activating bridge " + this.name);
      }

      try {
         this.resume();
      } catch (Exception var2) {
         BridgeLogger.logStackTrace(var2);
         throw new MigrationException("Failed to activate Bridge " + this.name, var2);
      }

      if (BridgeDebug.MessagingBridgeRuntime.isDebugEnabled()) {
         BridgeDebug.MessagingBridgeRuntime.debug("Bridge " + this.name + " has been successfully activated.");
      }

   }

   public void migratableDeactivate() throws MigrationException {
      if (BridgeDebug.MessagingBridgeRuntime.isDebugEnabled()) {
         BridgeDebug.MessagingBridgeRuntime.debug("Deactivating bridge " + this.name);
      }

      try {
         this.suspend(true);
      } catch (Exception var2) {
         throw new MigrationException("Failed to suspend Messaging Bridge " + this.name, var2);
      }

      if (BridgeDebug.MessagingBridgeRuntime.isDebugEnabled()) {
         BridgeDebug.MessagingBridgeRuntime.debug("Bridge " + this.name + " has been successfully deactivated.");
      }

   }

   public void migratableInitialize() throws MigrationException {
      if (BridgeDebug.MessagingBridgeRuntime.isDebugEnabled()) {
         BridgeDebug.MessagingBridgeRuntime.debug("Initializging bridge " + this.name + " as a migratable");
      }

      try {
         this.initialize();
      } catch (Exception var2) {
         throw new MigrationException("Failed to initialize bridge " + this.name + " as a mibratable.", var2);
      }

      if (BridgeDebug.MessagingBridgeRuntime.isDebugEnabled()) {
         BridgeDebug.MessagingBridgeRuntime.debug("Bridge " + this.name + " has been successfully initialized as a migratable");
      }

   }

   private void throwMessagingBridgeException(String var1, Throwable var2) throws MessagingBridgeException {
      MessagingBridgeException var3 = new MessagingBridgeException(var1);
      if (var2 != null) {
         if (var2 instanceof Exception) {
            var3.setLinkedException((Exception)var2);
         } else {
            var3.setLinkedThrowable(var2);
         }
      }

      throw var3;
   }

   private String policyToString(int var1) {
      switch (var1) {
         case 0:
            return new String("AUTO");
         case 1:
            return new String("SCHEDULED");
         case 2:
            return new String("MANUAL");
         default:
            return null;
      }
   }

   private Properties copyProperties(Properties var1) {
      Properties var2 = new Properties();
      if (var1 != null) {
         Enumeration var3 = var1.propertyNames();

         while(var3.hasMoreElements()) {
            String var4 = (String)var3.nextElement();
            var2.put(var4, var1.get(var4));
         }
      }

      return var2;
   }

   public synchronized void setReconnectDelayMinimum(int var1) {
      int var2 = (int)this.retryController.getInitial();
      this.retryController.setInitial((long)var1 * 1000L);
      BridgeLogger.logInfoAttributeChanged(this.name, "ReconnectDelayMinimum", (long)var2, (long)var1);
   }

   public synchronized void setReconnectDelayIncrease(int var1) {
      int var2 = (int)this.retryController.getIncrement();
      this.retryController.setIncrement((long)var1 * 1000L);
      BridgeLogger.logInfoAttributeChanged(this.name, "ReconnectDelayIncrease", (long)var2, (long)var1);
   }

   public synchronized void setReconnectDelayMaximum(int var1) {
      int var2 = (int)this.retryController.getMaximum();
      this.retryController.setMaximum((long)var1 * 1000L);
      BridgeLogger.logInfoAttributeChanged(this.name, "ReconnectDelayMaximum", (long)var2, (long)var1);
   }

   public synchronized void setTransactionTimeout(int var1) {
      int var2 = this.transactionTimeout;
      if (var1 <= 0) {
         var1 = 1;
      }

      if (this.batchInterval < 0L) {
         this.flushingTime = (long)(800 * var1);
      }

      this.transactionTimeout = var1;
      BridgeLogger.logInfoAttributeChanged(this.name, "TransactionTimeout", (long)var2, (long)this.transactionTimeout);
   }

   public synchronized void setIdleTimeMaximum(int var1) {
      int var2 = (int)(this.maximumIdleTime / 1000L);
      this.maximumIdleTime = (long)var1 * 1000L;
      if (this.maximumIdleTime <= 0L && this.async) {
         this.maximumIdleTime = 60000L;
      }

      BridgeLogger.logInfoAttributeChanged(this.name, "IdleTimeMaximum", (long)var2, this.maximumIdleTime / 1000L);
   }

   public synchronized void setBatchSize(int var1) {
      int var2 = this.batchSize;
      this.batchSize = var1;
      BridgeLogger.logInfoAttributeChanged(this.name, "BatchSize", (long)var2, (long)this.batchSize);
   }

   public synchronized void setBatchInterval(long var1) {
      int var3 = (int)this.batchInterval;
      this.batchInterval = var1;
      if (this.batchInterval < 0L) {
         this.flushingTime = (long)(800 * this.transactionTimeout);
      } else {
         this.flushingTime = this.batchInterval;
      }

      BridgeLogger.logInfoAttributeChanged(this.name, "BatchInterval", (long)var3, (long)((int)this.batchInterval));
   }

   public synchronized void setStarted(boolean var1) {
      if (var1) {
         if (!this.isStopped()) {
            return;
         }

         if (this.isShutdownOrSuspended()) {
            BridgeLogger.logFailedStart(this.name);
            return;
         }

         this.health = 0;
         this.reasons.add(0, "Started by administrator.");
         BridgeLogger.logInfoAttributeStartedChanged(this.name, "false", "true");
         this.stopped = false;
         if (this.state == 1) {
            if (this.timer == null) {
               this.timer = this.timerManager.scheduleAtFixedRate(this, 0L, this.scanUnit);
            }

            this.workManager.schedule(this);
         }

         if (this.state == 6) {
            this.state = 3;
         }

         if (this.state == 7) {
            this.state = 2;
         }

         if (this.async) {
            this.running = false;
         }

         if (!this.running) {
            this.running = true;
            this.workManager.schedule(this);
         }
      } else {
         if (this.isStopped()) {
            return;
         }

         this.stopped = true;
         this.logBeginForwarding = true;
         this.health = 1;
         this.reasons.add(0, "Stopped by administrator.");
         BridgeLogger.logInfoAttributeStartedChanged(this.name, "true", "false");
         if (this.state == 1 || this.state == 2) {
            return;
         }

         if (this.state == 4 || this.state == 5 || this.state == 8 || this.state == 3) {
            this.state = 6;

            try {
               this.stopForwarding();
            } catch (Exception var3) {
            }

            this.running = false;
         }
      }

   }

   private void doTrigger() {
      if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
         BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Bridge " + this.name + " doTrigger(): state = " + this.state + " stopped = " + this.stopped);
      }

      synchronized(this) {
         if (this.isStopped()) {
            return;
         }

         switch (this.state) {
            case 1:
               if (this.lookupRetryTimeCurrent >= 10000L) {
                  this.lookupRetryTimeCurrent = 0L;
               }

               this.lookupRetryTimeCurrent += this.scanUnit;
               if (this.lookupRetryTimeCurrent < 10000L) {
                  return;
               }

               if (this.running) {
                  return;
               }

               this.running = true;
               break;
            case 2:
               this.connRetryTimeCurrent += this.scanUnit;
               if (this.connRetryTimeCurrent < this.connRetryTimeNext) {
                  return;
               }

               if (this.connRetryTimeCurrent >= this.connRetryTimeNext) {
                  this.connRetryTimeCurrent = 0L;
               }

               if (this.running) {
                  return;
               }

               this.running = true;
               break;
            case 3:
            case 5:
            case 6:
            case 7:
            case 8:
               if (this.running) {
                  return;
               }

               this.running = true;
               break;
            case 4:
               if (!this.idle) {
                  return;
               }

               if (this.onMessageIdleCurrent >= this.maximumIdleTime) {
                  this.onMessageIdleCurrent = 0L;
               }

               this.onMessageIdleCurrent += this.scanUnit;
               if (this.onMessageIdleCurrent < this.maximumIdleTime) {
                  return;
               }

               this.state = 9;
               if (this.running) {
                  return;
               }

               this.running = true;
               break;
            default:
               return;
         }
      }

      if (BridgeDebug.MessagingBridgeRuntimeVerbose.isDebugEnabled()) {
         BridgeDebug.MessagingBridgeRuntimeVerbose.debug("Bridge " + this.name + " doTrigger(): about to run workManager.schedule() state = " + this.state);
      }

      this.workManager.schedule(this);
   }

   public static synchronized Context getContext() throws NamingException {
      Environment var0 = new Environment();
      var0.setCreateIntermediateContexts(true);
      var0.setReplicateBindings(true);
      return var0.getInitialContext();
   }

   public void stop() {
      throw new UnsupportedOperationException("This method is not implemented on runtime mbean. The way of start/stop a bridge at runtime is to change the Started attribute on the configuration mbean");
   }

   public void start() {
      throw new UnsupportedOperationException("This method is not implemented on runtime mbean. The way of start/stop a bridge at runtime is to change the Started attribute on the configuration mbean");
   }

   public String getState() {
      String var1 = null;
      synchronized(this.stateCache) {
         if (this.stateCache[0] == null || this.stateCache[1] == null) {
            switch (this.health) {
               case 0:
                  this.stateCache[0] = "Active";
                  this.stateCache[1] = (String)this.reasons.get(0);
                  break;
               case 1:
                  this.stateCache[0] = "Inactive";
                  this.stateCache[1] = (String)this.reasons.get(0);
            }
         }

         if (this.stateCache[0] != null) {
            var1 = this.stateCache[0];
            this.stateCache[0] = null;
         }

         return var1;
      }
   }

   public String getDescription() {
      String var1 = null;
      synchronized(this.stateCache) {
         if (this.stateCache[0] == null || this.stateCache[1] == null) {
            switch (this.health) {
               case 0:
                  this.stateCache[0] = "Active";
                  this.stateCache[1] = (String)this.reasons.get(0);
                  break;
               case 1:
                  this.stateCache[0] = "Inactive";
                  this.stateCache[1] = (String)this.reasons.get(0);
            }
         }

         if (this.stateCache[1] != null) {
            var1 = this.stateCache[1];
            this.stateCache[1] = null;
         }

         return var1;
      }
   }

   private Exception createExceptionWithLinkedExceptionInfo(Throwable var1) {
      if (!BridgeDebug.MessagingBridgeStartup.isDebugEnabled() && !BridgeDebug.MessagingBridgeRuntime.isDebugEnabled()) {
         return var1 instanceof Exception ? (Exception)var1 : new Exception(var1.getMessage());
      } else {
         String var2 = StackTraceUtils.throwable2StackTrace(var1);
         if (var1 instanceof ResourceException) {
            Exception var3 = ((ResourceException)var1).getLinkedException();
            if (var3 != null) {
               var2 = var2 + "-------------- Linked Exception ------------" + EOL + StackTraceUtils.throwable2StackTrace(var3);
               Exception var4 = null;
               if (var3 instanceof ResourceException) {
                  var4 = ((ResourceException)var3).getLinkedException();
               }

               if (var3 instanceof JMSException) {
                  var4 = ((JMSException)var3).getLinkedException();
               }

               if (var4 != null) {
                  var2 = var2 + "-------------- Linked Exception 2 ------------" + EOL + StackTraceUtils.throwable2StackTrace(var4);
                  Exception var5 = null;
                  if (var4 instanceof JMSException) {
                     var5 = ((JMSException)var4).getLinkedException();
                  }

                  if (var5 != null) {
                     var2 = var2 + "-------------- Linked Exception 3 ------------" + EOL + StackTraceUtils.throwable2StackTrace(var5);
                  }
               }
            }
         }

         return new Exception(var2);
      }
   }

   private String constructLogForReceivedMessage(Message var1) {
      String var2 = null;
      var2 = this.getJMSHeaders(var1);
      Transaction var3 = TxHelper.getTransaction();
      Xid var4 = var3 == null ? null : var3.getXID();
      var2 = var2 + EOL + "  Transaction Id: " + var4;
      if (var1 instanceof TextMessage) {
         try {
            String var5 = ((TextMessage)var1).getText();
            var2 = var2 + EOL + "  " + (var5 == null ? "null" : (var5.length() < 53 ? var5 : var5.substring(0, 50) + "..."));
         } catch (JMSException var6) {
            var2 = var2 + EOL + "  Failed to get text. " + var6.toString();
         }
      }

      var2 = var2 + EOL;
      return var2;
   }

   private String constructLogForSentMessage(String var1, Message var2) {
      String var3 = "  JMS Message Class: ";
      if (var2 instanceof TextMessage) {
         var3 = var3 + "TextMessage";
      }

      if (var2 instanceof ObjectMessage) {
         var3 = var3 + "ObjectMessage";
      }

      if (var2 instanceof BytesMessage) {
         var3 = var3 + "BytesMessage";
      }

      if (var2 instanceof MapMessage) {
         var3 = var3 + "MapMessage";
      }

      if (var2 instanceof StreamMessage) {
         var3 = var3 + "StreamMessage";
      }

      try {
         var3 = var3 + EOL + "  Old JMS MessageID: " + var1;
         var3 = var3 + EOL + "  New JMS MessageID: " + var2.getJMSMessageID();
         if (var2 instanceof TextMessage) {
            String var4 = ((TextMessage)var2).getText();
            var3 = var3 + EOL + "  " + (var4 == null ? "null" : (var4.length() <= 53 ? var4 : var4.substring(0, 50) + "..."));
         }
      } catch (JMSException var5) {
         var3 = var3 + EOL + "  Failed to get fields. " + var5.toString();
      }

      var3 = var3 + EOL;
      return var3;
   }

   private String getJMSHeaders(Message var1) {
      String var2 = "  JMS Message Class: ";
      if (var1 instanceof TextMessage) {
         var2 = var2 + "TextMessage";
      }

      if (var1 instanceof ObjectMessage) {
         var2 = var2 + "ObjectMessage";
      }

      if (var1 instanceof BytesMessage) {
         var2 = var2 + "BytesMessage";
      }

      if (var1 instanceof MapMessage) {
         var2 = var2 + "MapMessage";
      }

      if (var1 instanceof StreamMessage) {
         var2 = var2 + "StreamMessage";
      }

      try {
         String var4 = var1.getJMSCorrelationID();
         if (var4 != null && var4.length() > 53) {
            var4 = var4.substring(0, 50) + "...";
         }

         String var5 = var1.getJMSType();
         if (var5 != null && var5.length() > 53) {
            var5 = var5.substring(0, 50) + "...";
         }

         var2 = var2 + EOL + "  JMSMessageID: " + var1.getJMSMessageID();
         var2 = var2 + EOL + "  JMSCorrelationID: " + var4;
         var2 = var2 + EOL + "  JMSDeliveryMode: " + (var1.getJMSDeliveryMode() == 1 ? "NON_PERSISTENT" : "PERSISTENT");
         var2 = var2 + EOL + "  JMSDestination: " + var1.getJMSDestination();
         var2 = var2 + EOL + "  JMSExpiration: " + dateToString(var1.getJMSExpiration());
         var2 = var2 + EOL + "  JMSPriority: " + var1.getJMSPriority();
         var2 = var2 + EOL + "  JMSRedelivered: " + var1.getJMSRedelivered();
         var2 = var2 + EOL + "  JMSReplyTo: " + var1.getJMSReplyTo();
         var2 = var2 + EOL + "  JMSTimestamp: " + dateToString(var1.getJMSTimestamp());
         var2 = var2 + EOL + "  JMSType: " + var5;
      } catch (JMSException var6) {
         var2 = var2 + EOL + "  Failed to get all headers. " + var6.toString();
      }

      return var2;
   }

   private static String dateToString(long var0) {
      return var0 <= 0L ? "0" : var0 + " (" + new Date(var0) + ")";
   }

   private static String getEOL() {
      String var0 = System.getProperty("line.separator");
      if (var0 == null) {
         var0 = "\n";
      }

      return var0;
   }

   public void timerExpired(Timer var1) {
      this.doTrigger();
   }

   public int getOrder() {
      return 100;
   }

   static {
      bridgeSignatures.put("ReconnectDelayMinimum", Integer.TYPE);
      bridgeSignatures.put("ReconnectDelayIncrease", Integer.TYPE);
      bridgeSignatures.put("ReconnectDelayMaximum", Integer.TYPE);
      bridgeSignatures.put("TransactionTimeout", Integer.TYPE);
      bridgeSignatures.put("IdleTimeMaximum", Integer.TYPE);
      bridgeSignatures.put("BatchSize", Integer.TYPE);
      bridgeSignatures.put("BatchInterval", Long.TYPE);
      bridgeSignatures.put("Started", Boolean.TYPE);
   }

   class RetryTimeController {
      long nextTime;
      long initial;
      long inc;
      long max;

      void init(long var1, long var3, long var5) {
         this.initial = var1;
         this.nextTime = var1;
         this.inc = var3;
         this.max = var5;
      }

      synchronized void reset() {
         this.nextTime = this.initial - this.inc;
      }

      synchronized long getNextRetryTime() {
         if (this.nextTime <= this.max) {
            this.nextTime += this.inc;
         } else {
            this.nextTime = this.max + this.inc;
         }

         return this.nextTime - this.inc;
      }

      synchronized void setInitial(long var1) {
         this.initial = var1;
         if (this.initial > this.max) {
            this.max = this.initial;
         }

      }

      long getInitial() {
         return this.initial;
      }

      synchronized void setIncrement(long var1) {
         this.inc = var1;
      }

      long getIncrement() {
         return this.inc;
      }

      synchronized void setMaximum(long var1) {
         this.max = var1;
         if (this.initial > this.max) {
            this.initial = this.max;
         }

      }

      long getMaximum() {
         return this.max;
      }
   }
}
