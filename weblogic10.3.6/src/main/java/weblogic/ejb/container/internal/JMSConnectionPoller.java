package weblogic.ejb.container.internal;

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.XAConnection;
import javax.jms.XAConnectionFactory;
import javax.jms.XAQueueConnection;
import javax.jms.XAQueueConnectionFactory;
import javax.jms.XAQueueSession;
import javax.jms.XASession;
import javax.jms.XATopicConnection;
import javax.jms.XATopicConnectionFactory;
import javax.jms.XATopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.resource.spi.security.PasswordCredential;
import javax.transaction.SystemException;
import javax.transaction.xa.XAResource;
import weblogic.deployment.jms.ForeignOpaqueReference;
import weblogic.deployment.jms.JMSConnectionHelper;
import weblogic.deployment.jms.JMSSessionPoolManager;
import weblogic.deployment.jms.MDBSession;
import weblogic.deployment.jms.WrappedClassManager;
import weblogic.deployment.jms.WrappedTransactionalSession;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.interfaces.MessageDrivenBeanInfo;
import weblogic.ejb.spi.SecurityPlugin;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.jms.common.CrossDomainSecurityManager;
import weblogic.jms.common.DestinationImpl;
import weblogic.jms.common.JMSConstants;
import weblogic.jms.common.JMSServerUtilities;
import weblogic.jms.extensions.DestinationDetail;
import weblogic.jms.extensions.MDBTransaction;
import weblogic.jms.extensions.WLConnection;
import weblogic.jms.extensions.WLSession;
import weblogic.logging.Loggable;
import weblogic.management.runtime.MessageDrivenEJBRuntimeMBean;
import weblogic.rmi.cluster.ThreadPreferredHost;
import weblogic.rmi.spi.HostID;
import weblogic.security.UsernameAndPassword;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.CredentialManager;
import weblogic.security.service.EJBResource;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SecurityService.ServiceType;
import weblogic.transaction.TransactionManager;
import weblogic.transaction.TxHelper;
import weblogic.utils.AssertionError;
import weblogic.utils.PlatformConstants;
import weblogic.work.MaxThreadsConstraint;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerHelper;
import weblogic.work.WorkManagerImpl;

public class JMSConnectionPoller extends MDConnectionManager implements ExceptionListener {
   static final int MAX_JMS_ERRORS = 3;
   private static final int DEFAULT_MAX_ERRORS = 10;
   private static final String MAX_ERROR_COUNT_PROPERTY = "weblogic.ejb.container.MaxMDBErrors";
   static final int MAX_ERROR_COUNT = Integer.getInteger("weblogic.ejb.container.MaxMDBErrors", 10);
   private static final int DEFAULT_ERROR_SLEEP_TIME_SECS = 5;
   private static final String ERROR_SLEEP_TIME_PROPERTY = "weblogic.ejb.container.MDBErrorSleepTime";
   static final int ERROR_SLEEP_TIME = Integer.getInteger("weblogic.ejb.container.MDBErrorSleepTime", 5) * 1000;
   private static final String XA_RESOURCE_NAME_PREFIX = "weblogic.ejb.container.JMSConnectionPoller.";
   private static final int POLLER_EXIT_WAIT = 60000;
   private static final String PROVIDERS_NEED_CONTINUOUS_POLLING = System.getProperty("weblogic.mdb.JMSProviders.NeedContinuousPolling");
   private static final boolean USE_81_STYLE_POLLING = Boolean.getBoolean("weblogic.mdb.message.81StylePolling");
   private Context environmentContext;
   private Hashtable foreignJNDIEnv;
   private Hashtable foreignDestJNDIEnv;
   Destination destination = null;
   private static final int WORK_MODE_ASYNC_NOTRAN = 1;
   private static final int WORK_MODE_ASYNC_2PC = 2;
   private static final int WORK_MODE_SYNC_2PC = 3;
   private static final int WORK_MODE_SYNC_1PC = 4;
   private static final int WORK_MODE_SYNC_NOTRAN = 5;
   private boolean disconnectInProgress;
   private AuthenticatedSubject runAsSubject;
   private XAResource registeredResource;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private JMSMessagePoller poller;
   private JMSPollerManager pm;
   private String dispatchPolicyName = "weblogic.kernel.Default";
   private WorkManager wm;
   private ConnectionFactory connectionFactory;
   private XAConnectionFactory xaConnectionFactory;
   private Connection connection;
   private Session[] sessions;
   private XASession[] xaSessions;
   private MessageConsumer[] consumers;
   private String xaResourceName;
   private WrappedClassManager jmsWrapperManager;
   private final Object contextSubjectLock = new Object();
   private AuthenticatedSubject contextSubject;
   private boolean isForeign = false;
   private int workMode;
   private String messageSelector;
   private int acknowledgeMode;
   private boolean isAQJMS;
   private boolean dynamicSessionClose = false;
   private boolean needsContinuousPolling = false;
   private boolean needsContinuousPollingInitialized = false;

   public JMSConnectionPoller(MessageDrivenBeanInfo var1, Context var2, MessageDrivenEJBRuntimeMBean var3) throws WLDeploymentException {
      super(var1, var2, var3);
      this.environmentContext = var2;
      if (this.info.getDispatchPolicy() != null) {
         this.dispatchPolicyName = this.info.getDispatchPolicy();
      }

      this.wm = this.info.getCustomWorkManager();
      this.xaResourceName = "weblogic.ejb.container.JMSConnectionPoller." + this.info.getName() + this.mdManager.getUniqueGlobalID();
      this.jmsWrapperManager = new WrappedClassManager();

      try {
         this.runAsSubject = this.info.getRunAsSubject();
      } catch (Exception var5) {
         this.runtimeMBean.setLastException(var5);
         throw new WLDeploymentException(var5.toString());
      }
   }

   private boolean isDestinationQueue() {
      return this.info.isDestinationQueue();
   }

   private boolean isDestinationTopic() {
      return this.info.isDestinationTopic();
   }

   public static boolean getCredentials(MessageDrivenBeanInfo var0, StringBuffer var1, StringBuffer var2) throws Exception {
      boolean var3 = false;
      String var5;
      if (var0.getSecurityPlugin() != null) {
         String var15 = var0.getSecurityPlugin().getPluginClass();
         var5 = var0.getSecurityPlugin().getKey();
         if (debugLogger.isDebugEnabled()) {
            debug("getting username/password from info.getSecurityPlugin()");
         }

         Object var16 = null;

         try {
            var16 = Class.forName(var15).newInstance();
         } catch (ClassNotFoundException var12) {
            EJBLogger.logPluginClassNotFound(var0.getEJBName(), var15);
         } catch (InstantiationException var13) {
            EJBLogger.logPluginClassInstantiationError(var0.getEJBName(), var15);
         } catch (IllegalAccessException var14) {
            EJBLogger.logPluginClassIllegalAccess(var0.getEJBName(), var15);
         }

         if (var16 instanceof SecurityPlugin) {
            UsernameAndPassword var17 = ((SecurityPlugin)var16).getCredentials(var5);
            var1.append(var17.getUsername());
            var2.append(new String(var17.getPassword()));
            var3 = true;
            if (debugLogger.isDebugEnabled()) {
               debug("userName=" + var1 + "; password= ***..*** ");
            }
         } else {
            EJBLogger.logPluginClassNotImplment(var0.getEJBName(), var15);
         }

         return var3;
      } else {
         EJBResource var4 = new EJBResource(var0.getDeploymentInfo().getApplicationName(), var0.getDeploymentInfo().getEJBComponentName(), var0.getEJBName(), "onMessage", (String)null, (String[])null);
         var5 = "weblogicDEFAULT";
         CredentialManager var6 = (CredentialManager)SecurityServiceManager.getSecurityService(kernelId, var5, ServiceType.CREDENTIALMANAGER);
         String[] var7 = new String[]{"weblogic.UserPassword"};
         Vector var8 = var6.getCredentials(kernelId, var0.getRunAsSubject(), var4, var7);
         Iterator var9 = var8.iterator();

         while(var9.hasNext()) {
            if (debugLogger.isDebugEnabled()) {
               debug("getCredentials() get next credential");
            }

            Object var10 = var9.next();
            if (var10 instanceof PasswordCredential) {
               if (debugLogger.isDebugEnabled()) {
                  debug("found a PasswordCredential");
               }

               PasswordCredential var11 = (PasswordCredential)var10;
               var1.append(var11.getUserName());
               var2.append(var11.getPassword());
               if (debugLogger.isDebugEnabled()) {
                  debug("userName=" + var1 + "; password= ***..*** ");
               }

               var3 = true;
            }
         }

         return var3;
      }
   }

   private Connection getXAConnection(String var1, String var2, boolean var3) throws NamingException, JMSException, ClassCastException {
      if (debugLogger.isDebugEnabled()) {
         debug("Found a xaconnection factory of type " + this.xaConnectionFactory.getClass().getName());
      }

      if (this.isDestinationQueue()) {
         if (debugLogger.isDebugEnabled()) {
            debug("Creating XAQueueConnection from factory");
         }

         if (var3) {
            return (Connection)(this.xaConnectionFactory instanceof XAQueueConnectionFactory ? ((XAQueueConnectionFactory)this.xaConnectionFactory).createXAQueueConnection(var1, var2) : this.xaConnectionFactory.createXAConnection(var1, var2));
         } else {
            return (Connection)(this.xaConnectionFactory instanceof XAQueueConnectionFactory ? ((XAQueueConnectionFactory)this.xaConnectionFactory).createXAQueueConnection() : this.xaConnectionFactory.createXAConnection());
         }
      } else if (this.isDestinationTopic()) {
         if (debugLogger.isDebugEnabled()) {
            debug("Creating XATopicConnection from factory");
         }

         if (var3) {
            return (Connection)(this.xaConnectionFactory instanceof XATopicConnectionFactory ? ((XATopicConnectionFactory)this.xaConnectionFactory).createXATopicConnection(var1, var2) : this.xaConnectionFactory.createXAConnection(var1, var2));
         } else {
            return (Connection)(this.xaConnectionFactory instanceof XATopicConnectionFactory ? ((XATopicConnectionFactory)this.xaConnectionFactory).createXATopicConnection() : this.xaConnectionFactory.createXAConnection());
         }
      } else {
         throw new AssertionError("Unkown JMS destination type");
      }
   }

   private Connection getConnection(String var1, String var2, boolean var3) throws WLDeploymentException {
      try {
         if (debugLogger.isDebugEnabled()) {
            debug("Found a connection factory of type " + this.connectionFactory.getClass().getName());
         }

         if (this.isDestinationQueue()) {
            if (debugLogger.isDebugEnabled()) {
               debug("Creating QueueConnection from factory");
            }

            if (var3) {
               return (Connection)(this.connectionFactory instanceof QueueConnectionFactory ? ((QueueConnectionFactory)this.connectionFactory).createQueueConnection(var1, var2) : this.connectionFactory.createConnection(var1, var2));
            } else {
               return (Connection)(this.connectionFactory instanceof QueueConnectionFactory ? ((QueueConnectionFactory)this.connectionFactory).createQueueConnection() : this.connectionFactory.createConnection());
            }
         } else if (this.isDestinationTopic()) {
            if (debugLogger.isDebugEnabled()) {
               debug("Creating TopicConnection from factory");
            }

            if (var3) {
               return (Connection)(this.connectionFactory instanceof TopicConnectionFactory ? ((TopicConnectionFactory)this.connectionFactory).createTopicConnection(var1, var2) : this.connectionFactory.createConnection(var1, var2));
            } else {
               return (Connection)(this.connectionFactory instanceof TopicConnectionFactory ? ((TopicConnectionFactory)this.connectionFactory).createTopicConnection() : this.connectionFactory.createConnection());
            }
         } else {
            throw new AssertionError("Unkown JMS destination type");
         }
      } catch (JMSException var6) {
         if (debugLogger.isDebugEnabled()) {
            debug("JMSException looking up connection factory: " + var6);
         }

         Loggable var5 = EJBLogger.logJmsExceptionWhileCreatingConnectionLoggable(getAllExceptionText(var6), var6);
         throw new WLDeploymentException(var5.getMessage(), var6);
      }
   }

   private Destination getDestination(Context var1, String var2) throws WLDeploymentException {
      Loggable var4;
      try {
         if (this.isDestinationQueue()) {
            if (debugLogger.isDebugEnabled()) {
               debug("Looking up Queue in JNDI named \"" + var2 + "\"");
            }

            Queue var7 = (Queue)var1.lookup(var2);
            if (debugLogger.isDebugEnabled()) {
               debug("Found an object of type " + var7.getClass().getName());
            }

            if (var7 instanceof DestinationImpl && !((DestinationImpl)var7).isQueue()) {
               var4 = EJBLogger.logJndiNameWasNotAJMSDestinationLoggable(var2);
               throw new WLDeploymentException(var4.getMessage());
            } else {
               return var7;
            }
         } else if (this.isDestinationTopic()) {
            if (debugLogger.isDebugEnabled()) {
               debug("Looking up Topic in JNDI named \"" + var2 + "\"");
            }

            Topic var3 = (Topic)var1.lookup(var2);
            if (debugLogger.isDebugEnabled()) {
               debug("Found an object of type " + var3.getClass().getName());
            }

            if (var3 instanceof DestinationImpl && !((DestinationImpl)var3).isTopic()) {
               var4 = EJBLogger.logJndiNameWasNotAJMSDestinationLoggable(var2);
               throw new WLDeploymentException(var4.getMessage());
            } else {
               return var3;
            }
         } else {
            throw new AssertionError("Unknown JMS destination type");
         }
      } catch (NamingException var5) {
         if (debugLogger.isDebugEnabled()) {
            debug("NamingException looking up destination: " + var5);
         }

         var4 = EJBLogger.logJmsDestinationNotFoundLoggable(var2);
         throw new WLDeploymentException(var4.getMessage() + PlatformConstants.EOL + getAllExceptionText(var5), var5);
      } catch (ClassCastException var6) {
         if (debugLogger.isDebugEnabled()) {
            debug("ClassCastException looking up destination: " + var6);
         }

         var4 = EJBLogger.logJndiNameWasNotAJMSDestinationLoggable(var2);
         throw new WLDeploymentException(var4.getMessage(), var6);
      }
   }

   public void onException(JMSException var1) {
      this.runtimeMBean.setLastException(var1);
      if (debugLogger.isDebugEnabled()) {
         debug("** JMS Failure detected on destination:" + this.mdManager.getDestinationName() + ". The exception was: " + getAllExceptionText(var1));
      }

      synchronized(this.stateLock) {
         if (this.state == 2) {
            this.state = 6;
         } else if (this.state == 5) {
            this.state = 7;
         }
      }

      if (debugLogger.isDebugEnabled()) {
         this.debugState();
      }

      if (this.getState() == 6) {
         this.stopMessagePollerThread();
      }

      ClassLoader var2 = this.info.getClassLoader();
      Thread var3 = Thread.currentThread();
      ClassLoader var4 = var3.getContextClassLoader();
      var3.setContextClassLoader(var2);

      try {
         this.scheduleReconnection();
      } finally {
         if (var4 != null) {
            var3.setContextClassLoader(var4);
         }

      }

   }

   static String getAllExceptionText(Throwable var0) {
      StringBuffer var1 = new StringBuffer();
      var1.append(var0.toString());

      for(Throwable var2 = var0.getCause(); var2 != null; var2 = var2.getCause()) {
         var1.append(PlatformConstants.EOL);
         var1.append("Nested exception: ");
         var1.append(var2.toString());
      }

      if (var0 instanceof JMSException) {
         JMSException var3 = (JMSException)var0;
         if (var3.getLinkedException() != null && var3.getLinkedException() != var0.getCause()) {
            var1.append(PlatformConstants.EOL);
            var1.append("Linked exception: ");
            var1.append(getAllExceptionText(var3.getLinkedException()));
         }
      }

      return var1.toString();
   }

   protected void logException(Exception var1) {
      EJBLogger.logMDBUnableToConnectToJMS(this.info.getEJBName(), this.mdManager.getDestinationName(), getAllExceptionText(var1));
   }

   private void startMessagePollerThread() {
      if (this.poller != null) {
         if (debugLogger.isDebugEnabled()) {
            debug("Starting a thread to poll for messages for MDB " + this.info.getName());
         }

         this.poller.start();
         WorkManagerImpl.executeDaemonTask(this.info.getEJBName(), 10, this.poller);
      } else if (this.pm != null) {
         this.pm.start();
      }

   }

   protected synchronized void stopMessagePollerThread() {
      if (this.poller != null) {
         this.poller.stop();
      } else if (this.pm != null) {
         this.pm.stop();
      }

   }

   private synchronized void waitForMessagePollerExit(long var1, boolean var3) {
      if (this.poller != null) {
         synchronized(this.poller) {
            this.poller.stop();
            if (this.poller.getRunning()) {
               try {
                  this.poller.wait(var1);
               } catch (InterruptedException var7) {
               }
            }

            if (var3) {
               this.poller = null;
            }
         }
      } else if (this.pm != null) {
         this.pm.waitForPollersToStop();
         if (var3) {
            this.pm.cleanupTimerManager();
            this.pm = null;
         }
      }

   }

   protected void connect() throws WLDeploymentException, JMSException, SystemException {
      assert this.getState() != 2;

      try {
         if (debugLogger.isDebugEnabled()) {
            this.debugState();
         }

         ++this.reconnectionCount;
         this.createJMSConnection(false);
         if (this.getState() != 3) {
            this.setState(2);
         }

         if (debugLogger.isDebugEnabled()) {
            this.debugState();
         }

         if (debugLogger.isDebugEnabled()) {
            debug("\n\n +++++++++++  Got JMSConnection ++++++++++\n");
         }

         this.startJMSConnection();
      } catch (WLDeploymentException var9) {
         if (debugLogger.isDebugEnabled()) {
            debug("** FAILED TO CONNECT TO JMS WITH: " + var9);
            var9.printStackTrace(System.err);
         }

         throw var9;
      } catch (JMSException var10) {
         if (debugLogger.isDebugEnabled()) {
            debug("** FAILED TO CONNECT TO JMS WITH: " + getAllExceptionText(var10));
            var10.printStackTrace(System.err);
         }

         throw var10;
      } catch (SystemException var11) {
         if (debugLogger.isDebugEnabled()) {
            debug("** FAILED TO CONNECT TO JMS WITH: " + var11);
            var11.printStackTrace(System.err);
         }

         throw var11;
      } catch (RuntimeException var12) {
         if (debugLogger.isDebugEnabled()) {
            debug("** FAILED TO CONNECT TO JMS WITH: " + var12);
            var12.printStackTrace(System.err);
         }

         throw var12;
      } finally {
         this.runtimeMBean.setJMSConnectionAlive(this.getState() == 2);
         if (this.getState() == 2) {
            this.runtimeMBean.setConnectionStatus("Connected");
         } else {
            this.info.reSetUsernameAndPassword();
            this.runtimeMBean.setConnectionStatus("re-connecting");
         }

         if (this.getState() == 6) {
            this.stopMessagePollerThread();
         }

      }

   }

   protected void disconnect(boolean var1) throws JMSException {
      boolean var3 = false;
      if (debugLogger.isDebugEnabled()) {
         debug("disconnect is called");
      }

      if (debugLogger.isDebugEnabled()) {
         this.debugState();
      }

      synchronized(this.stateLock) {
         if (this.disconnectInProgress) {
            return;
         }

         this.disconnectInProgress = true;
      }

      try {
         synchronized(this) {
            this.stopMessagePollerThread();
         }

         SecurityHelper.pushRunAsSubject(this.getRightSubject(this.getContextSubject()));
         this.stopJMSConnection();
         this.waitForMessagePollerExit(60000L, true);
         if (this.getState() == 6 && this.registeredResource != null) {
            try {
               TxHelper.getTransactionManager().unregisterResource(this.xaResourceName, true);
            } catch (Exception var25) {
               if (debugLogger.isDebugEnabled()) {
                  debug("Error unregistering XA resource: " + this.xaResourceName + " " + var25);
               }
            }
         }

         this.registeredResource = null;
         int var2;
         if (this.consumers != null) {
            var2 = 0;

            while(true) {
               if (var2 >= this.consumers.length) {
                  this.consumers = null;
                  break;
               }

               if (this.consumers[var2] != null) {
                  try {
                     this.consumers[var2].close();
                  } catch (JMSException var26) {
                     if (var1) {
                        throw var26;
                     }
                  }
               }

               ++var2;
            }
         }

         if (this.sessions != null) {
            var2 = 0;

            while(true) {
               if (var2 >= this.sessions.length) {
                  this.sessions = null;
                  break;
               }

               if (this.sessions[var2] != null) {
                  try {
                     this.sessions[var2].setMessageListener((MessageListener)null);
                     this.sessions[var2].close();
                  } catch (JMSException var27) {
                     if (var1) {
                        throw var27;
                     }
                  }
               }

               ++var2;
            }
         }

         if (this.xaSessions != null) {
            var2 = 0;

            while(true) {
               if (var2 >= this.xaSessions.length) {
                  this.xaSessions = null;
                  break;
               }

               if (this.xaSessions[var2] != null) {
                  try {
                     this.xaSessions[var2].close();
                  } catch (JMSException var24) {
                     if (var1) {
                        throw var24;
                     }
                  }
               }

               ++var2;
            }
         }

         if (this.connection != null) {
            try {
               this.connection.close();
            } catch (JMSException var23) {
               if (var1) {
                  throw var23;
               }
            }
         }

         var3 = true;
      } finally {
         synchronized(this.stateLock) {
            this.disconnectInProgress = false;
            if (var3) {
               if (this.state != 3 && this.state != 4) {
                  if (this.state == 5) {
                     this.state = 7;
                  } else {
                     this.state = 1;
                  }
               } else {
                  this.state = 4;
               }
            }
         }

         this.destination = null;
         SecurityHelper.popRunAsSubject();
         if (debugLogger.isDebugEnabled()) {
            this.debugState();
         }

      }

   }

   public void deleteDurableSubscriber() {
      if (this.isDestinationTopic() && this.info.isDurableSubscriber()) {
         String var1 = debugLogger.isDebugEnabled() ? "JMSClientID=" + this.mdManager.getJMSClientID() + " ejb=" + this.info.getEJBName() : null;

         try {
            this.createJMSConnection(true);
            if (this.connection instanceof WLConnection && this.info.getTopicMessagesDistributionMode() > 0) {
               ((WLSession)this.sessions[0]).unsubscribe((Topic)this.destination, this.info.getEJBName());
            } else {
               this.sessions[0].unsubscribe(this.mdManager.getJMSClientID());
            }

            this.runtimeMBean.setJmsClientID("");
            EJBLogger.logMDBDurableSubscriptionDeletion(this.mdManager.getJMSClientID(), this.info.getEJBName());
         } catch (WLDeploymentException var16) {
            if (debugLogger.isDebugEnabled()) {
               debug("Exception is thrown while deleting durable subscriber " + var1 + ": " + var16);
            }
         } catch (JMSException var17) {
            if (debugLogger.isDebugEnabled()) {
               debug("Exception is thrown while deleting durable subscriber " + var1 + ": " + var17);
            }
         } catch (SystemException var18) {
            if (debugLogger.isDebugEnabled()) {
               debug("Exception is thrown while deleting durable subscriber " + var1 + ": " + var18);
            }
         } finally {
            try {
               this.disconnect(false);
            } catch (Exception var15) {
            }

         }
      }

   }

   public void startJMSConnection() {
      if (this.getState() == 2) {
         EJBLogger.logMDBReConnectedToJMS(this.info.getEJBName(), this.mdManager.getDestinationName());
         if (this.connection != null) {
            try {
               SecurityHelper.pushRunAsSubject(this.getContextSubject());
               this.connection.start();
               this.runtimeMBean.setMDBStatus("running");
            } catch (JMSException var6) {
               this.runtimeMBean.setLastException(var6);
               if (debugLogger.isDebugEnabled()) {
                  debug("JMS exception starting JMS connection: " + getAllExceptionText(var6));
               }

               this.setState(6);
            } finally {
               SecurityHelper.popRunAsSubject();
            }
         }
      }

   }

   public void stopJMSConnection() {
      if (this.connection != null) {
         try {
            this.connection.stop();
         } catch (JMSException var6) {
            if (debugLogger.isDebugEnabled()) {
               debug("Exception on stopping connection: " + var6);
            }

            if (this.getState() == 5) {
               this.setState(7);
            }
         } finally {
            if (!this.scheduleResume) {
               this.runtimeMBean.setMDBStatus("Suspended at " + new Date(System.currentTimeMillis()) + " by the user.");
            }

         }
      }

   }

   private int determineWorkMode() throws JMSException, WLDeploymentException {
      this.isAQJMS = this.connection != null && this.connection.getClass().getName().startsWith("oracle.jms");
      if (!this.info.isOnMessageTransacted()) {
         if (this.isAQJMS && this.info.getMinimizeAQSessions()) {
            if (debugLogger.isDebugEnabled()) {
               debug("MDB will poll synchronously with non transacted MDB");
            }

            if (!this.info.isDestinationTopic() || this.info.isDurableSubscriber()) {
               this.dynamicSessionClose = true;
            }

            return 5;
         } else {
            if (debugLogger.isDebugEnabled()) {
               debug("MDB will poll asynchronously with no transactions");
            }

            return 1;
         }
      } else if (!(this.connection instanceof XAConnection)) {
         if (debugLogger.isDebugEnabled()) {
            debug("MDB will poll synchronously using a one-phase commit transaction");
         }

         return 4;
      } else if (!this.isAQJMS) {
         if (this.info.getMaxMessagesInTransaction() > 1) {
            if (debugLogger.isDebugEnabled()) {
               debug("MDB will poll synchronously, two-phase transaction, multiple msg in tran");
            }

            return 3;
         } else {
            Object var1;
            if (this.connection instanceof XAQueueConnection) {
               var1 = ((XAQueueConnection)this.connection).createXAQueueSession();
            } else if (this.connection instanceof XATopicConnection) {
               var1 = ((XATopicConnection)this.connection).createXATopicSession();
            } else if (this.connection instanceof XAConnection) {
               var1 = ((XAConnection)this.connection).createXASession();
            } else if (this.connection instanceof QueueConnection) {
               var1 = ((QueueConnection)this.connection).createQueueSession(true, 1);
            } else if (this.connection instanceof TopicConnection) {
               var1 = ((TopicConnection)this.connection).createTopicSession(true, 1);
            } else {
               if (!(this.connection instanceof Connection)) {
                  throw new AssertionError("JMS Connection object of an unknown type");
               }

               var1 = this.connection.createSession(true, 1);
            }

            if (var1 instanceof MDBTransaction) {
               if (debugLogger.isDebugEnabled()) {
                  debug("MDB will poll asynchronously using MDBTransaction");
               }

               ((Session)var1).close();
               return 2;
            } else {
               ((Session)var1).close();
               if (debugLogger.isDebugEnabled()) {
                  debug("MDB will poll synchronously using XA");
               }

               return 3;
            }
         }
      } else {
         if (this.info.getMinimizeAQSessions() && (!this.info.isDestinationTopic() || this.info.isDurableSubscriber())) {
            this.dynamicSessionClose = true;
         }

         return 3;
      }
   }

   private void setUpTopicSessions(Connection var1, int var2, Destination var3, int var4) throws WLDeploymentException, JMSException, SystemException {
      boolean var5 = true;
      if (!this.isAQJMS && (var1 instanceof TopicConnection || var1 instanceof XATopicConnection)) {
         var5 = false;
      }

      boolean var6 = false;
      MDListener[] var7 = new MDListener[var2];
      this.sessions = new Session[var2];
      this.consumers = new MessageConsumer[var2];
      this.xaSessions = new XASession[var2];
      if ((this.acknowledgeMode == 1 || this.acknowledgeMode == 3) && var4 > 1 && this.workMode != 5) {
         this.acknowledgeMode = 2;
      }

      if ((this.workMode == 3 || this.workMode == 4) && var4 > 1) {
         if (this.mdManager.getTopicMessagesDistributionMode() == 0) {
            EJBLogger.logUsingSingleThreadForMDBTopic(this.info.getName());
         }

         var4 = 1;
      }

      if (this.workMode == 5 && var4 > 1) {
         var4 = 1;
      }

      if (debugLogger.isDebugEnabled()) {
         debug("Will create " + var4 + " message listener objects to process messages for MDB " + this.info.getName());
      }

      if (this.mdManager.needsSetForwardFilter()) {
         if (this.messageSelector != null && (this.messageSelector == null || this.messageSelector.trim().length() != 0)) {
            this.messageSelector = "NOT JMS_WL_DDForwarded AND (" + this.messageSelector + ")";
         } else {
            this.messageSelector = "NOT JMS_WL_DDForwarded";
         }

         if (debugLogger.isDebugEnabled()) {
            debug("message selector on " + this.info.getComponentName() + " is \"" + this.messageSelector + "\"");
         }
      }

      for(int var8 = 0; var8 < var2; ++var8) {
         MDBSession var9 = this.setUpTopicSessionAt(var8, var5);
         if (var8 == 0 && this.workMode == 1 && this.acknowledgeMode == 2 && this.sessions[var8] instanceof WLSession) {
            var6 = true;
         }

         var7[var8] = new MDListener((MDListener)null, this, var4 <= 1 ? 0 : var4, this.environmentContext, this.sessions[var8], var9, this.acknowledgeMode, this.runtimeMBean, this.info, this.mdManager.getPool(), var6, this.wm, this.workMode == 5);
         if (this.workMode != 3 && this.workMode != 4 && this.workMode != 5) {
            try {
               SecurityHelper.pushRunAsSubject(this.getContextSubject());
               this.consumers[var8].setMessageListener(var7[var8]);
               var1.setExceptionListener(this);
               if (this.sessions[var8] instanceof WLSession) {
                  ((WLSession)this.sessions[var8]).setExceptionListener(this);
               }
            } finally {
               SecurityHelper.popRunAsSubject();
            }
         } else {
            if (this.xaSessions[var8] != null && !(this.xaSessions[var8] instanceof WLSession) && this.registeredResource == null) {
               if (debugLogger.isDebugEnabled()) {
                  debug("Registering XA resource " + this.xaResourceName);
               }

               TransactionManager var10 = TxHelper.getTransactionManager();
               this.registeredResource = ((WrappedTransactionalSession)this.sessions[var8]).getXAResource();
               Hashtable var11 = new Hashtable();
               var11.put("weblogic.transaction.registration.type", "dynamic");
               var11.put("weblogic.transaction.registration.settransactiontimeout", "true");
               var10.registerResource(this.xaResourceName, this.registeredResource, var11);
               var10.setTransactionTimeout(this.info.getTransactionTimeoutMS() / 1000);
            }

            if (this.dynamicSessionClose) {
               assert var8 == 0 : "For dynamicSessionClose, there is only one session created";

               this.pm = new JMSPollerManager(this.info.getName(), this, this.consumers, var7, this.wm, false, var3, this.dynamicSessionClose);
            } else if (this.mdManager.getTopicMessagesDistributionMode() == 0) {
               this.poller = new JMSMessagePoller(this.info.getName(), this, (JMSMessagePoller)null, this.consumers[var8], var7[var8], this.wm, var8, this.dynamicSessionClose);
            } else if (var8 == var2 - 1 && (this.mdManager.getTopicMessagesDistributionMode() == 2 || this.mdManager.getTopicMessagesDistributionMode() == 1)) {
               this.pm = new JMSPollerManager(this.info.getName(), this, this.consumers, var7, this.wm, false, var3, this.dynamicSessionClose);
            }
         }
      }

      this.startMessagePollerThread();
   }

   private MDBSession setUpTopicSessionAt(int var1, boolean var2) throws JMSException, WLDeploymentException {
      Loggable var3;
      switch (this.workMode) {
         case 1:
            if (var2) {
               this.sessions[var1] = this.connection.createSession(false, this.acknowledgeMode);
            } else {
               this.sessions[var1] = ((TopicConnection)this.connection).createTopicSession(false, this.acknowledgeMode);
            }
            break;
         case 2:
            if (var2) {
               this.xaSessions[var1] = ((XAConnection)this.connection).createXASession();
               this.sessions[var1] = this.xaSessions[var1].getSession();
            } else {
               this.xaSessions[var1] = ((XATopicConnection)this.connection).createXATopicSession();
               this.sessions[var1] = ((XATopicSession)this.xaSessions[var1]).getTopicSession();
            }
            break;
         case 3:
            if (var2) {
               this.xaSessions[var1] = ((XAConnection)this.connection).createXASession();
               Session var5 = this.xaSessions[var1].getSession();
               this.sessions[var1] = (Session)JMSSessionPoolManager.getWrappedMDBPollerSession(var5, this.xaSessions[var1], 3, var5 instanceof WLSession, this.xaResourceName, this.jmsWrapperManager);
            } else {
               this.xaSessions[var1] = ((XATopicConnection)this.connection).createXATopicSession();
               TopicSession var6 = ((XATopicSession)this.xaSessions[var1]).getTopicSession();
               this.sessions[var1] = (Session)JMSSessionPoolManager.getWrappedMDBPollerSession(var6, this.xaSessions[var1], 2, var6 instanceof WLSession, this.xaResourceName, this.jmsWrapperManager);
            }
            break;
         case 4:
            var3 = EJBLogger.logproviderIsNotTransactedButMDBIsTransactedLoggable(this.info.getEJBName());
            throw new WLDeploymentException(var3.getMessage());
         case 5:
            if (var2) {
               this.sessions[var1] = this.connection.createSession(false, 2);
            } else {
               this.sessions[var1] = ((TopicConnection)this.connection).createTopicSession(false, 2);
            }
            break;
         default:
            throw new AssertionError("Internal error in JMSConnectionPoller: Unknown work mode");
      }

      var3 = null;
      MDBSession var7;
      if (var2) {
         var7 = JMSSessionPoolManager.getWrappedMDBSession(this.sessions[var1], 3, this.jmsWrapperManager);
      } else {
         var7 = JMSSessionPoolManager.getWrappedMDBSession(this.sessions[var1], 2, this.jmsWrapperManager);
      }

      String var4 = null;
      if (this.info.isDurableSubscriber()) {
         if (this.mdManager.getTopicMessagesDistributionMode() > 0 && this.info.isDurableSubscriber()) {
            var4 = this.info.getEJBName();
         } else {
            var4 = this.mdManager.getJMSClientID();
         }

         if (var2) {
            this.consumers[var1] = this.sessions[var1].createDurableSubscriber((Topic)this.destination, var4, this.messageSelector, this.info.noLocalMessages());
         } else {
            this.consumers[var1] = ((TopicSession)this.sessions[var1]).createDurableSubscriber((Topic)this.destination, var4, this.messageSelector, this.info.noLocalMessages());
         }
      } else if (var2) {
         this.consumers[var1] = this.sessions[var1].createConsumer((Topic)this.destination, this.messageSelector, this.info.noLocalMessages());
      } else {
         this.consumers[var1] = ((TopicSession)this.sessions[var1]).createSubscriber((Topic)this.destination, this.messageSelector, this.info.noLocalMessages());
      }

      return var7;
   }

   private void setUpQueueSessions(Destination var1, int var2) throws WLDeploymentException, JMSException, SystemException {
      boolean var3 = false;
      if (this.isAQJMS || !(this.connection instanceof QueueConnection) && !(this.connection instanceof XAQueueConnection)) {
         this.sessions = new Session[var2];
         this.xaSessions = new XASession[var2];
         this.consumers = new MessageConsumer[var2];
         var3 = true;
      } else {
         this.sessions = new QueueSession[var2];
         this.xaSessions = new XAQueueSession[var2];
         this.consumers = new QueueReceiver[var2];
      }

      MDListener[] var4 = new MDListener[var2];
      if (debugLogger.isDebugEnabled()) {
         debug("Will create " + var2 + " message listener objects to process messages for MDB " + this.info.getName());
      }

      int var5 = this.dynamicSessionClose ? 1 : var2;

      int var6;
      for(var6 = 0; var6 < var5; ++var6) {
         MDBSession var7 = this.setUpQueueSessionAt(var6, var3);
         var4[var6] = new MDListener((MDListener)null, this, 0, this.environmentContext, this.sessions[var6], var7, this.acknowledgeMode, this.runtimeMBean, this.info, this.mdManager.getPool(), false, this.wm, this.workMode == 5);
      }

      if (var5 < var2) {
         for(var6 = var5; var6 < var2; ++var6) {
            var4[var6] = new MDListener((MDListener)null, this, 0, this.environmentContext, (Session)null, (MDBSession)null, this.acknowledgeMode, this.runtimeMBean, this.info, this.mdManager.getPool(), false, this.wm, this.workMode == 5);
         }
      }

      if (this.workMode != 3 && this.workMode != 4 && this.workMode != 5) {
         try {
            SecurityHelper.pushRunAsSubject(this.getContextSubject());

            assert var5 == var2 : "dynamic close should not happen for async polling";

            for(var6 = 0; var6 < var2; ++var6) {
               this.consumers[var6].setMessageListener(var4[var6]);
               if (this.sessions[var6] instanceof WLSession) {
                  ((WLSession)this.sessions[var6]).setExceptionListener(this);
               }
            }

            this.connection.setExceptionListener(this);
         } finally {
            SecurityHelper.popRunAsSubject();
         }
      } else {
         if (this.xaSessions[0] != null && !(this.xaSessions[0] instanceof WLSession) && this.registeredResource == null) {
            if (debugLogger.isDebugEnabled()) {
               debug("Registering XA resource " + this.xaResourceName);
            }

            TransactionManager var13 = TxHelper.getTransactionManager();
            this.registeredResource = ((WrappedTransactionalSession)this.sessions[0]).getXAResource();
            Hashtable var14 = new Hashtable();
            var14.put("weblogic.transaction.registration.type", "standard");
            var14.put("weblogic.transaction.registration.settransactiontimeout", "true");
            var13.registerResource(this.xaResourceName, this.registeredResource, var14);
            var13.setTransactionTimeout(this.info.getTransactionTimeoutMS() / 1000);
         }

         boolean var15 = this.needsContinuousJMSMessagePolling();
         if (!USE_81_STYLE_POLLING && !this.info.getUse81StylePolling()) {
            this.pm = new JMSPollerManager(this.info.getName(), this, (MessageConsumer[])this.consumers, var4, this.wm, var15, var1, this.dynamicSessionClose);
         } else {
            int var16;
            if (var15) {
               this.poller = new ContinuousJMSMessagePoller(this.info.getName(), this, (JMSMessagePoller)null, this.consumers[0], var4[0], this.wm, 0);

               for(var16 = 1; var16 < var2; ++var16) {
                  ContinuousJMSMessagePoller var17 = new ContinuousJMSMessagePoller(this.info.getName(), this, this.poller, this.consumers[var16], var4[var16], this.wm, var16);
                  this.poller.addChild(var17);
               }
            } else {
               this.poller = new JMSMessagePoller(this.info.getName(), this, (JMSMessagePoller)null, this.consumers[0], var4[0], this.wm, 0, this.dynamicSessionClose);

               for(var16 = 1; var16 < var2; ++var16) {
                  JMSMessagePoller var8 = new JMSMessagePoller(this.info.getName(), this, this.poller, this.consumers[var16], var4[var16], this.wm, var16, this.dynamicSessionClose);
                  this.poller.addChild(var8);
               }
            }
         }

         this.startMessagePollerThread();
      }

   }

   private MDBSession setUpQueueSessionAt(int var1, boolean var2) throws JMSException, WLDeploymentException {
      this.xaSessions[var1] = null;
      Loggable var3;
      switch (this.workMode) {
         case 1:
            if (var2) {
               this.sessions[var1] = this.connection.createSession(false, this.acknowledgeMode);
            } else {
               this.sessions[var1] = ((QueueConnection)this.connection).createQueueSession(false, this.acknowledgeMode);
            }
            break;
         case 2:
            if (var2) {
               this.xaSessions[var1] = ((XAConnection)this.connection).createXASession();
               this.sessions[var1] = this.xaSessions[var1].getSession();
            } else {
               this.xaSessions[var1] = ((XAQueueConnection)this.connection).createXAQueueSession();
               this.sessions[var1] = ((XAQueueSession)this.xaSessions[var1]).getQueueSession();
            }
            break;
         case 3:
            if (var2) {
               this.xaSessions[var1] = ((XAConnection)this.connection).createXASession();
               Session var4 = this.xaSessions[var1].getSession();
               this.sessions[var1] = (Session)JMSSessionPoolManager.getWrappedMDBPollerSession(var4, this.xaSessions[var1], 3, var4 instanceof WLSession, this.xaResourceName, this.jmsWrapperManager);
            } else {
               this.xaSessions[var1] = ((XAQueueConnection)this.connection).createXAQueueSession();
               QueueSession var5 = ((XAQueueSession)this.xaSessions[var1]).getQueueSession();
               this.sessions[var1] = (QueueSession)JMSSessionPoolManager.getWrappedMDBPollerSession(var5, this.xaSessions[var1], 1, var5 instanceof WLSession, this.xaResourceName, this.jmsWrapperManager);
            }
            break;
         case 4:
            var3 = EJBLogger.logproviderIsNotTransactedButMDBIsTransactedLoggable(this.info.getEJBName());
            throw new WLDeploymentException(var3.getMessage());
         case 5:
            if (var2) {
               this.sessions[var1] = this.connection.createSession(false, 2);
            } else {
               this.sessions[var1] = ((QueueConnection)this.connection).createQueueSession(false, 2);
            }
            break;
         default:
            throw new AssertionError("Internal error in JMSConnectionPoller: Unknown work mode");
      }

      if (this.sessions[var1] == null) {
         throw new AssertionError("Internal error in JMSConnectionPoller: failed to create session");
      } else {
         var3 = null;
         MDBSession var6;
         if (var2) {
            var6 = JMSSessionPoolManager.getWrappedMDBSession(this.sessions[var1], 3, this.jmsWrapperManager);
            this.consumers[var1] = this.sessions[var1].createConsumer((Queue)this.destination, this.messageSelector);
         } else {
            var6 = JMSSessionPoolManager.getWrappedMDBSession(this.sessions[var1], 1, this.jmsWrapperManager);
            this.consumers[var1] = ((QueueSession)this.sessions[var1]).createReceiver((Queue)this.destination, this.messageSelector);
         }

         return var6;
      }
   }

   public MessageConsumer reCreateMessageConsumer(Destination var1, int var2) throws JMSException {
      String var3 = this.consumers[var2].getMessageSelector();
      this.consumers[var2].close();
      if (!(this.connection instanceof QueueConnection) && !(this.connection instanceof XAQueueConnection)) {
         this.consumers[var2] = this.sessions[var2].createConsumer((Queue)var1, var3);
      } else {
         this.consumers[var2] = ((QueueSession)this.sessions[var2]).createReceiver((Queue)var1, var3);
      }

      return this.consumers[var2];
   }

   void dynamicCloseSession(int var1) throws JMSException {
      assert this.workMode == 5 || this.workMode == 3 : "work mode is " + this.workMode + ", dynamicClosingSession is only supported for SYNC_NOTRAN and SYNC_2PC";

      MessageConsumer var2 = this.consumers[var1];
      this.consumers[var1] = null;
      Session var3 = this.sessions[var1];
      this.sessions[var1] = null;
      XASession var4 = this.xaSessions[var1];
      this.xaSessions[var1] = null;
      Throwable var5 = null;

      try {
         if (var2 != null) {
            var2.close();
         }
      } catch (Throwable var7) {
         var5 = var7;
      }

      try {
         if (var3 != null) {
            var3.close();
         }
      } catch (Throwable var9) {
         if (var5 == null) {
            var5 = var9;
         }
      }

      try {
         if (var4 != null) {
            var4.close();
         }
      } catch (Throwable var8) {
         if (var5 == null) {
            var5 = var8;
         }
      }

      if (var5 != null) {
         if (var5 instanceof JMSException) {
            throw (JMSException)var5;
         } else {
            JMSException var6 = new JMSException("Error in closing MDB session");
            var6.initCause(var5);
            throw var6;
         }
      }
   }

   CreateSessionResult dynamicCreateSession(int var1) throws JMSException {
      assert this.workMode == 5 || this.workMode == 3 : "work mode is " + this.workMode + ", dynamicCreateSession is only supported for SYNC_NOTRAN and SYNC_2PC";

      assert this.consumers[var1] == null : "MessageConsumer at index " + var1 + " is not null";

      assert this.sessions[var1] == null : "Session at index " + var1 + " is not null";

      assert this.consumers[var1] == null : "XASession at index " + var1 + " is not null";

      MDBSession var2 = null;

      try {
         boolean var3;
         if (this.isDestinationQueue()) {
            var3 = this.isAQJMS || !(this.connection instanceof QueueConnection) && !(this.connection instanceof XAQueueConnection);
            var2 = this.setUpQueueSessionAt(var1, var3);
         } else {
            var3 = this.isAQJMS || !(this.connection instanceof TopicConnection) && !(this.connection instanceof XATopicConnection);
            var2 = this.setUpTopicSessionAt(var1, var3);
         }
      } catch (WLDeploymentException var13) {
         assert false : "WLDeploymentException should not happen in dynamicCreateSession:" + var13;
      } finally {
         if (var2 == null) {
            try {
               this.dynamicCloseSession(var1);
            } catch (Throwable var12) {
            }
         }

      }

      return new CreateSessionResult(this.sessions[var1], var2, this.consumers[var1]);
   }

   private void checkNonCompliantConnectionUsage() throws JMSException {
      this.connection = JMSConnectionHelper.getXAConnectionToUse(this.connection);
   }

   private boolean needsContinuousJMSMessagePolling() {
      if (!this.needsContinuousPollingInitialized) {
         String var1 = this.connection.getClass().getName();
         if (debugLogger.isDebugEnabled()) {
            debug("connectionClassName :" + var1);
         }

         if (!var1.startsWith("com.tibco.tibjms") && !var1.startsWith("progress.message.jimpl.xa")) {
            try {
               String var2 = this.connection.getMetaData().getJMSProviderName().toLowerCase(Locale.ENGLISH);
               if (debugLogger.isDebugEnabled()) {
                  debug("connection provider name is: " + var2);
               }

               if (var2 != null && var2.contains("tibco")) {
                  this.needsContinuousPolling = true;
               } else if (PROVIDERS_NEED_CONTINUOUS_POLLING != null && PROVIDERS_NEED_CONTINUOUS_POLLING.toLowerCase(Locale.ENGLISH).contains(var2)) {
                  this.needsContinuousPolling = true;
                  if (debugLogger.isDebugEnabled()) {
                     debug("weblogic.mdb.JMSProviders.NeedContinuousPolling system property contains provider: " + var2);
                  }
               }
            } catch (Throwable var3) {
               if (debugLogger.isDebugEnabled()) {
                  debug("provider name not found. getMetaData may not implemented.");
               }
            }
         } else {
            this.needsContinuousPolling = true;
         }

         this.needsContinuousPollingInitialized = true;
      }

      if (debugLogger.isDebugEnabled()) {
         debug("needsContinuousPolling :" + this.needsContinuousPolling);
      }

      return this.needsContinuousPolling;
   }

   private void createJMSConnection(boolean var1) throws WLDeploymentException, JMSException, SystemException {
      this.messageSelector = this.mdManager.getMessageSelector();
      this.acknowledgeMode = this.info.getAcknowledgeMode();
      Context var2 = null;
      int var3 = 1;

      try {
         if (this.connection != null) {
            SecurityHelper.pushRunAsSubject(this.getRightSubject(this.getContextSubject()));

            try {
               this.connection.close();
            } finally {
               SecurityHelper.popRunAsSubject();
               this.connection = null;
            }
         }
      } catch (Exception var137) {
      }

      if (debugLogger.isDebugEnabled()) {
         debug("Starting MDB: acknowledge mode = " + this.acknowledgeMode);
         debug("Transacted = " + this.info.isOnMessageTransacted());
         debug("Bean-managed = " + this.info.usesBeanManagedTx());
      }

      int var4 = this.calculateMaxConcurrentInstances();

      try {
         SecurityHelper.pushRunAsSubject(this.runAsSubject);
         StringBuffer var5 = new StringBuffer();
         StringBuffer var6 = new StringBuffer();
         boolean var7 = false;
         String var8 = null;

         try {
            var7 = getCredentials(this.info, var5, var6);
            var8 = this.mdManager.getDestinationName();
         } catch (Exception var135) {
         }

         if (debugLogger.isDebugEnabled()) {
            debug(" DestinationName from poolInfo " + var8);
         }

         String var9 = this.mdManager.getConnectionFactoryJNDIName();

         try {
            if (var7) {
               if (debugLogger.isDebugEnabled()) {
                  debug("Obtained credentials user " + var5.toString() + " and " + "password ***..*** using Credential Mapper");
               }

               var2 = this.info.getInitialContext(var5.toString(), var6.toString());
               synchronized(this.contextSubjectLock) {
                  this.contextSubject = this.getRightSubject((AuthenticatedSubject)this.info.getSubject());
               }
            } else {
               if (debugLogger.isDebugEnabled()) {
                  debug("Check provider url " + this.mdManager.getProviderURL());
               }

               if (this.mdManager.getProviderURL() == null || this.mdManager.getProviderURL() != null && this.mdManager.getProviderURL().trim().length() == 0) {
                  if (debugLogger.isDebugEnabled()) {
                     debug("Check foreign credentials");
                  }

                  var7 = this.getForeignJMSCredentials(var5, var6);
                  if (var7 && debugLogger.isDebugEnabled()) {
                     debug("Obtained credentials user " + var5.toString() + " and password ***..*** using " + "ForeignJMSConnectionFactoryMBean");
                  }

                  var9 = this.getRemoteJndiName(var9);
                  var8 = this.getRemoteJndiName(var8);
                  if (debugLogger.isDebugEnabled()) {
                     debug(" destinationJNDIName for foreignJNDI " + var8 + " connFactoryJndiName " + var9);
                  }

                  var2 = this.getForeignContext(this.foreignJNDIEnv, true);
               }
            }

            if (var2 == null && !this.isForeign() || this.isConfiguredAQJMSForeignServer()) {
               var2 = this.getInitialContextFromInfo(this.info);
               var9 = this.mdManager.getConnectionFactoryJNDIName();
               var8 = this.mdManager.getDestinationName();
               synchronized(this.contextSubjectLock) {
                  this.contextSubject = this.getRightSubject((AuthenticatedSubject)this.info.getSubject());
               }
            }

            if (var2 == null) {
               throw new NamingException("JNDI Environment is unavailable");
            }
         } catch (NamingException var141) {
            Loggable var11 = EJBLogger.logFailedJNDIContextToJMSProviderLoggable(var141);
            throw new WLDeploymentException(var11.getMessage(), var141);
         }

         SecurityHelper.pushRunAsSubject(this.getContextSubject());

         try {
            DestinationDetail var10 = this.mdManager.getDestination();
            if (!this.isThe3rdJMSVendor(var10) && !this.mdManager.isNoneDDMD()) {
               this.destination = var10.getDestination();
            } else {
               Context var143 = var2;
               if (this.foreignDestJNDIEnv != null && !this.isConfiguredAQJMSForeignServer()) {
                  var143 = this.getForeignContext(this.foreignDestJNDIEnv, false);
               }

               this.destination = this.getDestination(var143, var8);
               if (!var143.equals(var2)) {
                  try {
                     var143.close();
                  } catch (NamingException var132) {
                  }
               }
            }

            HostID var144 = null;
            if (this.destination instanceof DestinationImpl) {
               var144 = ThreadPreferredHost.get();
               ThreadPreferredHost.set(JMSServerUtilities.getHostId(var2, (DestinationImpl)this.destination));
            }

            try {
               Loggable var13;
               try {
                  if (debugLogger.isDebugEnabled()) {
                     debug("Looking up connection factory in JNDI named \"" + var9 + "\"");
                  }

                  Object var12 = var2.lookup(var9);
                  if (this.mdManager.supportMultipleConncitons()) {
                     if (debugLogger.isDebugEnabled()) {
                        debug("Under this mode, container creates" + var4 + " connections to destionation " + var8);
                     }

                     var3 = var4;
                     var4 = 1;
                  }

                  if (!(var12 instanceof XAConnectionFactory) && !(var12 instanceof XAQueueConnectionFactory) && !(var12 instanceof XATopicConnectionFactory)) {
                     if (!(var12 instanceof ConnectionFactory) && !(var12 instanceof QueueConnectionFactory) && !(var12 instanceof TopicConnectionFactory)) {
                        var13 = EJBLogger.logJndiNameWasNotAJMSConnectionFactoryLoggable(var9);
                        throw new WLDeploymentException(var13.getMessage());
                     }

                     if (this.info.isOnMessageTransacted()) {
                        var13 = EJBLogger.logJndiNameWasNotAXAJMSConnectionFactoryLoggable(var9);
                        throw new WLDeploymentException(var13.getMessage());
                     }

                     this.connectionFactory = (ConnectionFactory)var12;
                     this.connection = this.getConnection(var5.toString(), var6.toString(), var7);
                  } else {
                     this.xaConnectionFactory = (XAConnectionFactory)var12;
                     this.connection = this.getXAConnection(var5.toString(), var6.toString(), var7);
                  }
               } catch (NamingException var138) {
                  if (debugLogger.isDebugEnabled()) {
                     debug("NamingException looking up connection factory: " + var138);
                  }

                  var13 = EJBLogger.logJmsConnectionFactoryNotFoundLoggable(var9);
                  throw new WLDeploymentException(var13.getMessage() + PlatformConstants.EOL + getAllExceptionText(var138), var138);
               }
            } finally {
               if (this.destination instanceof DestinationImpl) {
                  ThreadPreferredHost.set(var144);
               }

            }

            if (this.isDestinationTopic()) {
               this.setupConnection(this.connection);
            }

            if (var1) {
               this.sessions = (Session[])(this.connection instanceof TopicConnection ? new TopicSession[]{((TopicConnection)this.connection).createTopicSession(false, this.acknowledgeMode)} : new Session[]{this.connection.createSession(false, this.acknowledgeMode)});
               return;
            }

            this.workMode = this.determineWorkMode();
            if (this.connection instanceof WLConnection) {
               ((WLConnection)this.connection).setDispatchPolicy(this.dispatchPolicyName);
               ((WLConnection)this.connection).setReconnectPolicy(JMSConstants.RECONNECT_POLICY_NONE);
            } else if (!WorkManagerHelper.isDefault(this.wm) && this.isDestinationQueue() && (this.workMode == 1 || this.workMode == 2)) {
               EJBLogger.logMDBDispatchPolicyIgnored(this.info.getEJBName(), this.dispatchPolicyName);
            }

            if (this.isDestinationTopic()) {
               this.setUpTopicSessions(this.connection, var3, this.destination, var4);
            } else {
               if (!this.isDestinationQueue()) {
                  throw new AssertionError("Unknown JMS destination type");
               }

               this.setUpQueueSessions(this.destination, var4);
            }

            this.checkNonCompliantConnectionUsage();
         } finally {
            SecurityHelper.popRunAsSubject();
         }
      } finally {
         if (var2 != null) {
            SecurityHelper.pushRunAsSubject(this.getContextSubject());

            try {
               var2.close();
            } catch (NamingException var130) {
            } finally {
               SecurityHelper.popRunAsSubject();
            }
         }

         SecurityHelper.popRunAsSubject();
      }

   }

   private int calculateMaxConcurrentInstances() {
      MaxThreadsConstraint var2 = null;
      if (!WorkManagerHelper.isDefault(this.wm)) {
         var2 = WorkManagerHelper.getMaxThreadsConstraint(this.wm);
      }

      int var1;
      if (var2 != null) {
         var1 = var2.getCount();
         if (debugLogger.isDebugEnabled()) {
            debug("Custom WorkManager with MaxThreadsConstraint is specified, use MaxThreadsConstraint of maxConcurrentInstances=" + var1);
         }
      } else if (!WorkManagerHelper.isDefault(this.wm) && this.wm.getType() == 2) {
         var1 = this.wm.getConfiguredThreadCount();
         if (debugLogger.isDebugEnabled()) {
            debug("Custom ExecuteThread is specified, use ExecuteThreadCount maxConcurrentInstances=" + var1);
         }
      } else if (this.wm.getType() == 2) {
         var1 = this.wm.getConfiguredThreadCount() / 2 + 1;
         if (debugLogger.isDebugEnabled()) {
            debug("Default ExecuteThread, use ExecuteThreadCount/2+1 maxConcurrentInstances=" + var1);
         }
      } else {
         var1 = Integer.getInteger("weblogic.mdb.DefaultThreadCount", 16);
         if (debugLogger.isDebugEnabled()) {
            debug("no custom ExecuteThread or WorkManager with MaxThreadsConstraint is specified, use default thread size maxConcurrentInstances=" + var1);
         }
      }

      if (this.info.getMaxConcurrentInstances() < var1 && debugLogger.isDebugEnabled()) {
         debug("Thread size is limited by max-beans-in-free-pool, new maxConcurrentInstances=" + var1);
      }

      var1 = var1 == -1 ? 16 : var1;
      var1 = Math.min(this.info.getMaxConcurrentInstances(), var1);
      return var1;
   }

   public synchronized void signalBackgroundThreads() {
      if (debugLogger.isDebugEnabled()) {
         debug("JMS connection for MDB " + this.info.getName() + " signalled to exit");
      }

      synchronized(this.stateLock) {
         if (this.state == 2) {
            this.state = 5;
         } else if (this.state != 3 && this.state != 5) {
            this.state = 7;
         }
      }

      this.stopMessagePollerThread();
      if (this.timer != null) {
         this.timer.cancel();
         this.timer = null;
      }

   }

   public synchronized boolean suspend(boolean var1) {
      this.signalBackgroundThreads();
      if (debugLogger.isDebugEnabled()) {
         debug("JMS connection for MDB " + this.info.getName() + " is suspending");
      }

      if (var1 && this.scheduleResume) {
         this.initDeliveryFailureParams();
         this.timer.cancel();
         this.timer = null;
      }

      this.waitForMessagePollerExit(60000L, false);

      try {
         SecurityHelper.pushRunAsSubject(this.getRightSubject(this.getContextSubject()));
         this.stopJMSConnection();
      } finally {
         SecurityHelper.popRunAsSubject();
      }

      if (debugLogger.isDebugEnabled()) {
         this.debugState();
      }

      this.runtimeMBean.incrementSuspendCount();
      return this.getState() != 7;
   }

   public synchronized boolean resume(boolean var1) {
      if (debugLogger.isDebugEnabled()) {
         debug("JMS connection for MDB " + this.info.getName() + " is resuming.");
      }

      if (var1 && this.scheduleResume) {
         this.initDeliveryFailureParams();
         this.timer.cancel();
         this.timer = null;
      }

      if (debugLogger.isDebugEnabled()) {
         this.debugState();
      }

      int var2;
      synchronized(this.stateLock) {
         if (this.state == 5 && this.connection != null) {
            this.state = 2;
         } else {
            this.state = 6;
         }

         var2 = this.state;
      }

      this.startJMSConnection();
      if (var2 != 2) {
         this.scheduleReconnection();
      } else {
         this.startMessagePollerThread();
      }

      if (debugLogger.isDebugEnabled()) {
         this.debugState();
      }

      return this.getState() == 2;
   }

   public void shutdown() {
      this.suspend(true);
   }

   private static void debug(String var0) {
      debugLogger.debug("[JMSConnectionPoller] " + var0);
   }

   private boolean getForeignJMSCredentials(StringBuffer var1, StringBuffer var2) {
      boolean var3 = false;

      try {
         String var5 = this.mdManager.getConnectionFactoryJNDIName();
         Object var4 = this.lookupForeignObject(var5);
         if (debugLogger.isDebugEnabled()) {
            debug("getForeignJMSCredentials, lookup on " + var5);
         }

         SecurityServiceManager.pushSubject(kernelId, kernelId);

         try {
            if (var4 instanceof ForeignOpaqueReference) {
               ForeignOpaqueReference var6 = (ForeignOpaqueReference)var4;
               boolean var7 = var6.isFactory();
               if (var7) {
                  if (var6.getUsername() != null && var6.getUsername().length() > 0) {
                     var3 = true;
                     var1.append(var6.getUsername());
                     if (debugLogger.isDebugEnabled()) {
                        debug("Found credentials for connection factory with username " + var1.toString());
                     }
                  }

                  if (var6.getPassword() != null && var6.getPassword().length() > 0) {
                     var3 = true;
                     var2.append(var6.getPassword());
                  }
               }
            }
         } finally {
            SecurityServiceManager.popSubject(kernelId);
         }

         if (!var3 && !var3 && debugLogger.isDebugEnabled()) {
            debug("No credentials associated with foreign jms connection factory");
         }

         return var3;
      } catch (NamingException var13) {
         if (debugLogger.isDebugEnabled()) {
            debug("Can't get credentials associated with foreign jmsconnection factory: " + var13);
         }

         return false;
      }
   }

   private boolean isConfiguredAQJMSForeignServer() {
      return this.foreignJNDIEnv != null && "oracle.jms.AQjmsInitialContextFactory".equals(this.foreignJNDIEnv.get("java.naming.factory.initial"));
   }

   private String getRemoteJndiName(String var1) throws WLDeploymentException {
      String var2 = var1;

      try {
         Object var3 = this.lookupForeignObject(var1);
         if (var3 instanceof ForeignOpaqueReference) {
            ForeignOpaqueReference var4 = (ForeignOpaqueReference)var3;
            var2 = var4.getRemoteJNDIName();
            if (this.foreignJNDIEnv == null && var4.isFactory()) {
               this.foreignJNDIEnv = var4.getJNDIEnvironment();
               this.isForeign = true;
            } else if (this.foreignDestJNDIEnv == null && !var4.isFactory()) {
               this.foreignDestJNDIEnv = var4.getJNDIEnvironment();
               this.isForeign = true;
               if (this.foreignJNDIEnv == null) {
                  this.foreignJNDIEnv = this.foreignDestJNDIEnv;
               }
            }
         } else if (debugLogger.isDebugEnabled()) {
            debug("Can't find foreign JMS jndi name for " + var1);
         }
      } catch (NamingException var5) {
         if (debugLogger.isDebugEnabled()) {
            debug("Can't get foreign JMS jndi name " + var5);
         }
      }

      return var2;
   }

   private boolean isForeign() {
      if (!this.isForeign) {
         return false;
      } else if (this.foreignJNDIEnv != null) {
         return true;
      } else {
         return this.foreignJNDIEnv == null ? false : false;
      }
   }

   private Context getForeignContext(Hashtable var1, boolean var2) {
      InitialContext var3 = null;
      if (var1 != null) {
         SecurityHelper.pushRunAsSubject(this.getRightSubject(SecurityServiceManager.getCurrentSubject(kernelId)));

         try {
            var3 = new InitialContext(var1);
            synchronized(this.contextSubjectLock) {
               if (var2) {
                  this.contextSubject = this.getRightSubject(SecurityServiceManager.getCurrentSubject(kernelId));
               }
            }
         } catch (NamingException var12) {
            if (debugLogger.isDebugEnabled()) {
               debug("Can't get initial context using foreign JNDI properties " + this.foreignJNDIEnv + " " + var12);
            }
         } finally {
            SecurityHelper.popRunAsSubject();
         }
      }

      return var3;
   }

   private Context getInitialContextFromInfo(MessageDrivenBeanInfo var1) throws NamingException {
      AuthenticatedSubject var2 = SecurityServiceManager.getCurrentSubject(kernelId);
      SecurityHelper.pushRunAsSubject(this.getRightSubject(var2));

      Context var3;
      try {
         var3 = var1.getInitialContext();
      } finally {
         SecurityHelper.popRunAsSubject();
      }

      return var3;
   }

   private AuthenticatedSubject getRightSubject(AuthenticatedSubject var1) {
      return var1 != null && !SecurityServiceManager.isKernelIdentity(var1) && !SecurityServiceManager.isServerIdentity(var1) && (!this.info.getIsRemoteSubjectExists() || !var1.equals(this.runAsSubject)) ? var1 : (AuthenticatedSubject)CrossDomainSecurityManager.getCrossDomainSecurityUtil().getRemoteSubject(this.mdManager.getProviderURL(), var1);
   }

   private Object lookupForeignObject(String var1) throws NamingException {
      Context var2 = this.getInitialContextFromInfo(this.info);
      SecurityHelper.pushRunAsSubject(this.getRightSubject((AuthenticatedSubject)this.info.getSubject()));

      Object var3;
      try {
         var3 = var2.lookupLink(var1);
      } finally {
         var2.close();
         SecurityHelper.popRunAsSubject();
      }

      return var3;
   }

   AuthenticatedSubject getContextSubject() {
      synchronized(this.contextSubjectLock) {
         return this.contextSubject;
      }
   }

   Object doPrivilegedJMSAction(PrivilegedExceptionAction var1) throws JMSException {
      AuthenticatedSubject var2 = this.getRightSubject(this.getContextSubject());

      try {
         return var2.doAs(kernelId, var1);
      } catch (PrivilegedActionException var4) {
         if (var4.getCause() instanceof JMSException) {
            throw (JMSException)var4.getCause();
         } else {
            throw new AssertionError(var4);
         }
      }
   }

   boolean isCurrentSubjectKernelIdentity() {
      AuthenticatedSubject var1 = SecurityServiceManager.getCurrentSubject(kernelId);
      return SecurityServiceManager.isKernelIdentity(var1) || SecurityServiceManager.isServerIdentity(var1);
   }

   private void setupConnection(Connection var1) throws JMSException {
      if (var1 instanceof WLConnection && this.mdManager.getTopicMessagesDistributionMode() > 0 && this.mdManager.isAdvancedTopicSupported()) {
         if (this.mdManager.supportMultipleConncitons()) {
            ((WLConnection)var1).setClientID(this.mdManager.getJMSClientID(), WLConnection.CLIENT_ID_POLICY_UNRESTRICTED);
            ((WLConnection)var1).setSubscriptionSharingPolicy(WLConnection.SUBSCRIPTION_SHARABLE);
            if (debugLogger.isDebugEnabled()) {
               debug("MDB [" + this.info.getName() + "] JMS connection ClientID policy to " + this.mdManager.getDestinationName() + " is " + WLConnection.CLIENT_ID_POLICY_UNRESTRICTED);
               debug("jms connection SubscriptionSharingPolicy to " + this.mdManager.getDestinationName() + " is " + WLConnection.SUBSCRIPTION_SHARABLE);
            }
         } else {
            ((WLConnection)var1).setClientID(this.mdManager.getJMSClientID(), WLConnection.CLIENT_ID_POLICY_RESTRICTED);
            ((WLConnection)var1).setSubscriptionSharingPolicy(WLConnection.SUBSCRIPTION_EXCLUSIVE);
         }

         this.runtimeMBean.setJmsClientID(this.mdManager.getJMSClientID());
      } else if (this.info.isDurableSubscriber()) {
         this.runtimeMBean.setJmsClientID(this.mdManager.getJMSClientID());
         if (var1.getClientID() == null) {
            var1.setClientID(this.mdManager.getJMSClientID());
         }
      }

   }

   private boolean isThe3rdJMSVendor(DestinationDetail var1) {
      return var1.getType() == 2 || var1.getType() == 3;
   }

   static class CreateSessionResult {
      Session session;
      MDBSession wrappedSession;
      MessageConsumer consumer;

      CreateSessionResult(Session var1, MDBSession var2, MessageConsumer var3) {
         this.session = var1;
         this.wrappedSession = var2;
         this.consumer = var3;
      }
   }
}
