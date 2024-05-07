package weblogic.jms.client;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.GeneralSecurityException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;
import javax.jms.ConnectionConsumer;
import javax.jms.ConnectionMetaData;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueSession;
import javax.jms.ServerSessionPool;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicSession;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.jms.JMSClientExceptionLogger;
import weblogic.jms.common.DestinationImpl;
import weblogic.jms.common.DestroyConnectionException;
import weblogic.jms.common.IllegalStateException;
import weblogic.jms.common.InvalidDestinationException;
import weblogic.jms.common.JMSConnectionConsumerCreateResponse;
import weblogic.jms.common.JMSConstants;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSPushEntry;
import weblogic.jms.common.JMSPushExceptionRequest;
import weblogic.jms.common.JMSServerId;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.common.JMSWorkManager;
import weblogic.jms.common.LostServerException;
import weblogic.jms.common.PasswordStore;
import weblogic.jms.common.PeerVersionable;
import weblogic.jms.dispatcher.DispatcherWrapper;
import weblogic.jms.dispatcher.Invocable;
import weblogic.jms.dispatcher.InvocableManagerDelegate;
import weblogic.jms.dispatcher.JMSDispatcher;
import weblogic.jms.dispatcher.JMSDispatcherManager;
import weblogic.jms.frontend.FEConnectionCloseRequest;
import weblogic.jms.frontend.FEConnectionConsumerCreateRequest;
import weblogic.jms.frontend.FEConnectionSetClientIdRequest;
import weblogic.jms.frontend.FEConnectionStartRequest;
import weblogic.jms.frontend.FEConnectionStopRequest;
import weblogic.jms.frontend.FERemoveSubscriptionRequest;
import weblogic.jms.frontend.FESessionCreateRequest;
import weblogic.jms.frontend.FESessionCreateResponse;
import weblogic.jms.frontend.FETemporaryDestinationDestroyRequest;
import weblogic.kernel.KernelStatus;
import weblogic.messaging.ID;
import weblogic.messaging.dispatcher.Dispatcher;
import weblogic.messaging.dispatcher.DispatcherException;
import weblogic.messaging.dispatcher.DispatcherId;
import weblogic.messaging.dispatcher.DispatcherImpl;
import weblogic.messaging.dispatcher.DispatcherStateChangeListener;
import weblogic.messaging.dispatcher.DispatcherWrapperState;
import weblogic.messaging.dispatcher.InvocableMonitor;
import weblogic.messaging.dispatcher.Request;
import weblogic.messaging.dispatcher.Response;
import weblogic.security.subject.AbstractSubject;
import weblogic.security.subject.SubjectManager;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;
import weblogic.utils.NestedThrowable;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

public class JMSConnection implements ConnectionInternal, DispatcherStateChangeListener, Externalizable, Invocable, Runnable, Reconnectable, Cloneable, TimerListener {
   static final long serialVersionUID = 7025750175126041724L;
   private static final AbstractSubject KernelID = (AbstractSubject)AccessController.doPrivileged(SubjectManager.getKernelIdentityAction());
   public static final byte EXTVERSION61 = 1;
   public static final byte EXTVERSION70 = 3;
   public static final byte EXTVERSION81 = 4;
   public static final byte EXTVERSION90 = 5;
   public static final byte EXTVERSION910 = 6;
   public static final byte EXTVERSION920 = 7;
   public static final byte EXTVERSION1033 = 8;
   private static final byte VERSION_MASK = 15;
   private static final byte TIMEOUT_MASK = 64;
   private static final byte EXTVERSION = 8;
   private static final byte COMPRESSION_MASK = 1;
   private static final byte UNIT_OF_ORDER_MASK = 2;
   private static final String JMS_PROVIDER_NAME = "BEA Systems, Inc.";
   private static final String PROVIDER_VERSION = "9.0.0";
   private static final int PROVIDER_MAJOR_VERSION = 9;
   private static final int PROVIDER_MINOR_VERSION = 0;
   public static final int SYNCHRONOUS_PREFETCH_DISABLED = 0;
   public static final int SYNCHRONOUS_PREFETCH_ENABLED = 1;
   public static final int SYNCHRONOUS_PREFETCH_TOPIC_SUBSCRIBER_ONLY = 2;
   public static final int ONE_WAY_SEND_DISABLED = 0;
   public static final int ONE_WAY_SEND_ENABLED = 1;
   public static final int ONE_WAY_SEND_TOPIC_ONLY = 2;
   static final int TYPE_UNSPECIFIED = 0;
   static final int TYPE_QUEUE = 1;
   static final int TYPE_TOPIC = 2;
   private JMSID connectionId;
   private String clientId;
   private int clientIdPolicy = 0;
   private int subscriptionSharingPolicy = 0;
   private int type;
   private int deliveryMode;
   private int priority;
   private long timeToDeliver;
   private long timeToLive;
   private long sendTimeout;
   private long redeliveryDelay;
   private boolean userTransactionsEnabled;
   private boolean allowCloseInOnMessage;
   private long transactionTimeout;
   private boolean isLocal;
   private int messagesMaximum;
   private int overrunPolicy;
   private int acknowledgePolicy;
   private String wlsServerName;
   private String runtimeMBeanName;
   private transient int refCount;
   private boolean flowControl;
   private int flowMinimum;
   private int flowMaximum;
   private double flowDecrease;
   private int flowIncrease;
   private long flowInterval;
   private boolean xaServerEnabled;
   private String unitOfOrder;
   private int synchronousPrefetchMode = 0;
   private boolean stopped = true;
   private final Object sublock = new Object();
   private LockedMap sessions = new LockedMap("sessions", (Object)null);
   private LockedMap durableSubscribers = new LockedMap("durables", (Object)null);
   private JMSDispatcher dispatcher;
   private DispatcherWrapper dispatcherWrapper;
   private JMSExceptionContext exceptionContext;
   private boolean firedExceptoinListener;
   private PeerVersionable peerVersionable;
   private byte version = 8;
   private PeerInfo peerInfo;
   private String dispatchPolicyName = JMSWorkManager.getJMSSessionOnMessageWMName();
   private int compressionThreshold = Integer.MAX_VALUE;
   private int pipelineGeneration = 0;
   private WLConnectionImpl wlConnectionImpl;
   private JMSConnection preDisconnectState;
   private JMSConnection replacementConnection;
   private PasswordStore pwdStore;
   private Object uHandle;
   private Object pHandle;
   public int oneWaySendMode;
   public int oneWaySendWindowSize;
   private int reconnectPolicy = 7;
   private long reconnectBlockingMillis = 60000L;
   private long totalReconnectPeriodMillis = -1L;
   private boolean wantXAConnection;
   private volatile boolean isClosed;
   private static int INITIAL_RETRY_DELAY = 125;
   private long nextRetry;
   private static final long RETRY_DELAY_MAXIMUM = 300000L;
   private static final int RETRY_DELAY_LEFT_SHIFT = 1;
   private static final int RETRY_DELAY_MIN_INTERVAL = 5000;
   private volatile Timer timer;
   private static final String RECONNECT_ID = "weblogic.jms.Reconnect";
   private static final Object TIMER_WORKMANAGER_LOCK = new Object();
   private static WorkManager RECONNECT_WORK_MANAGER;
   private static TimerManager RECONNECT_TIMER_MANAGER;
   private static final boolean isT3Client;
   private final Object acknowledgePolicyLock;

   public JMSConnection(JMSID var1, String var2, int var3, int var4, int var5, int var6, long var7, long var9, long var11, long var13, long var15, boolean var17, boolean var18, int var19, int var20, int var21, boolean var22, DispatcherWrapper var23, boolean var24, int var25, int var26, int var27, int var28, boolean var29, String var30, PeerVersionable var31, String var32, String var33, PeerInfo var34, int var35, int var36, int var37, int var38, int var39, long var40, long var42) {
      this.nextRetry = (long)INITIAL_RETRY_DELAY;
      this.acknowledgePolicyLock = new Object();
      this.peerVersionable = var31;
      this.connectionId = var1;
      this.clientId = var2;
      this.clientIdPolicy = var3;
      this.subscriptionSharingPolicy = var4;
      this.unitOfOrder = var30;
      this.deliveryMode = var5;
      this.priority = var6;
      this.timeToDeliver = var7;
      this.timeToLive = var9;
      this.sendTimeout = var11;
      this.redeliveryDelay = var13;
      this.transactionTimeout = var15;
      this.userTransactionsEnabled = var17;
      this.allowCloseInOnMessage = var18;
      this.messagesMaximum = var19;
      this.overrunPolicy = var20;
      this.acknowledgePolicy = var21;
      this.isLocal = var22;
      this.dispatcherWrapper = var23;
      this.xaServerEnabled = var29;
      this.flowControl = var24;
      this.flowMinimum = var25;
      this.flowMaximum = var26;
      this.wlsServerName = var32;
      this.runtimeMBeanName = var33;
      this.flowIncrease = (var26 - var25) / var28;
      if (this.flowIncrease < 1) {
         this.flowIncrease = 1;
      }

      this.flowDecrease = (double)var25 / (double)var26;
      this.flowDecrease = Math.pow(this.flowDecrease, 1.0 / (double)var28);
      this.flowInterval = (long)(var27 * 1000 / var28);
      if (this.flowInterval < 1L) {
         this.flowInterval = 1L;
      }

      this.peerInfo = var34;
      if (PeerInfo.VERSION_DIABLO.compareTo(var34) <= 0) {
         this.pipelineGeneration = 15728640;
      }

      if (JMSDebug.JMSCommon.isDebugEnabled()) {
         JMSDebug.JMSCommon.debug("pipelineGeneration is " + JMSPushEntry.displayRecoverGeneration(this.pipelineGeneration));
      }

      this.compressionThreshold = var35;
      this.synchronousPrefetchMode = var36;
      this.oneWaySendMode = var37;
      this.oneWaySendWindowSize = var38;
      this.internalSetReconnect(var39, var40, var42);
   }

   private void internalSetReconnect(int var1, long var2, long var4) {
      if (isT3Client()) {
         this.reconnectPolicy = var1;
         this.reconnectBlockingMillis = var2;
         this.totalReconnectPeriodMillis = var4;
      } else {
         this.reconnectPolicy = 0;
         this.reconnectBlockingMillis = 0L;
         this.totalReconnectPeriodMillis = 0L;
      }

   }

   static boolean isT3Client() {
      return isT3Client;
   }

   final void setupDispatcher() throws DispatcherException {
      if (!(this.dispatcherWrapper.getRemoteDispatcher() instanceof DispatcherImpl)) {
         DispatcherId var1 = new DispatcherId(this.dispatcherWrapper.getId(), this.connectionId.getCounter());
         this.dispatcherWrapper.setId(var1);
      }

      this.dispatcher = JMSDispatcherManager.addDispatcherReference(this.dispatcherWrapper);
      this.dispatcher.addDispatcherPeerGoneListener(this);
   }

   final void setType(int var1) {
      this.type = var1;
   }

   final int getType() {
      return this.type;
   }

   public final ConnectionConsumer createConnectionConsumer(Topic var1, String var2, ServerSessionPool var3, int var4) throws JMSException {
      return this.createConnectionConsumer((Destination)var1, var2, var3, var4);
   }

   public final ConnectionConsumer createDurableConnectionConsumer(Topic var1, String var2, String var3, ServerSessionPool var4, int var5) throws JMSException {
      this.checkClosed();
      if (this.type == 1) {
         throw new IllegalStateException(JMSClientExceptionLogger.logUnsupportedTopicOperation5Loggable());
      } else {
         throw new weblogic.jms.common.JMSException(JMSClientExceptionLogger.logUnsupportedSubscriptionLoggable());
      }
   }

   public final QueueSession createQueueSession(boolean var1, int var2) throws JMSException {
      if (var2 == 128) {
         throw new weblogic.jms.common.JMSException(JMSClientExceptionLogger.logNoMulticastOnQueueSessionsLoggable());
      } else {
         SessionInternal var3 = this.createSessionInternal(var1, var2, false, 2);
         return var3;
      }
   }

   public final TopicSession createTopicSession(boolean var1, int var2) throws JMSException {
      SessionInternal var3 = this.createSessionInternal(var1, var2, false, 1);
      return var3;
   }

   public final Session createSession(boolean var1, int var2) throws JMSException {
      return this.createSessionInternal(var1, var2, false, 0);
   }

   protected final SessionInternal createSessionInternal(boolean var1, int var2, boolean var3, int var4) throws JMSException {
      JMSSession var5 = this.setupJMSSession(var1, var2, var3, var4);
      Object var6;
      if (var3) {
         var6 = new XASessionInternalImpl((JMSXASession)var5, (XAConnectionInternalImpl)this.wlConnectionImpl);
      } else {
         var6 = new WLSessionImpl(var5, this.wlConnectionImpl);
      }

      var5.setWLSessionImpl((WLSessionImpl)var6);
      return (SessionInternal)var6;
   }

   JMSSession setupJMSSession(boolean var1, int var2, boolean var3, int var4) throws JMSException {
      Object var5;
      if (var3) {
         var1 = false;
         var2 = 2;
         var5 = new JMSXASession(this, var1, this.isStopped());
      } else {
         var5 = new JMSSession(this, var1, var2, this.isStopped());
      }

      ((JMSSession)var5).setType(var4);
      if (this.xaServerEnabled && KernelStatus.isServer()) {
         ((JMSSession)var5).setUserTransactionsEnabled(true);
      }

      Response var6 = this.getFrontEndDispatcher().dispatchSync(new FESessionCreateRequest(this.connectionId, var1, var3, var2, (String)null));
      ((JMSSession)var5).setId(((FESessionCreateResponse)var6).getSessionId());
      ((JMSSession)var5).setRuntimeMBeanName(((FESessionCreateResponse)var6).getRuntimeMBeanName());
      ((JMSSession)var5).setUnitOfOrder(this.unitOfOrder);
      synchronized(this) {
         this.sessionAdd((JMSSession)var5);
         return (JMSSession)var5;
      }
   }

   public final JMSDispatcher getFrontEndDispatcher() throws JMSException {
      if (this.isClosed()) {
         synchronized(this.wlConnectionImpl.getConnectionStateLock()){}

         try {
            if (this.isReconnectControllerClosed()) {
               throw new IllegalStateException(JMSClientExceptionLogger.logClosedConnectionLoggable());
            } else {
               throw new LostServerException(JMSClientExceptionLogger.logLostServerConnectionLoggable());
            }
         } finally {
            ;
         }
      } else {
         return this.dispatcher;
      }
   }

   private void sessionAdd(JMSSession var1) throws JMSException {
      if (this.sessions.put(var1.getJMSID(), var1) != null) {
         throw new weblogic.jms.common.JMSException(JMSClientExceptionLogger.logDuplicateSessionLoggable());
      } else {
         InvocableManagerDelegate.delegate.invocableAdd(4, var1);
      }
   }

   final synchronized void sessionRemove(JMSID var1) {
      if (this.sessions.remove(var1) != null) {
         InvocableManagerDelegate.delegate.invocableRemove(4, var1);
      }

   }

   public final ConnectionConsumer createConnectionConsumer(Queue var1, String var2, ServerSessionPool var3, int var4) throws JMSException {
      return this.createConnectionConsumer((Destination)var1, var2, var3, var4);
   }

   public final synchronized String getClientID() throws JMSException {
      this.checkClosed();
      return this.clientId;
   }

   final synchronized String getClientIDInternal() {
      return this.clientId;
   }

   public final synchronized void setClientID(String var1) throws JMSException {
      this.setClientIDInternal(var1, this.clientIdPolicy);
   }

   public final synchronized void setClientID(String var1, String var2) throws IllegalArgumentException, JMSException {
      this.setClientIDInternal(var1, WLConnectionImpl.validateAndConvertClientIdPolicy(var2));
   }

   public final synchronized void setClientIDInternal(String var1, int var2) throws JMSException {
      if (this.clientId != null) {
         throw new IllegalStateException(JMSClientExceptionLogger.logClientIDSetLoggable(var1, this.clientId));
      } else if (var1 == null) {
         throw new weblogic.jms.common.JMSException(JMSClientExceptionLogger.logNullClientIDLoggable());
      } else if (var1.length() == 0) {
         throw new weblogic.jms.common.JMSException(JMSClientExceptionLogger.logZeroClientIDLoggable());
      } else {
         Response var3 = this.getFrontEndDispatcher().dispatchSync(new FEConnectionSetClientIdRequest(this.connectionId, var1, var2));
         this.clientId = var1;
         this.clientIdPolicy = var2;
      }
   }

   public String getClientIDPolicy() {
      return WLConnectionImpl.convertClientIdPolicy(this.clientIdPolicy);
   }

   public String getSubscriptionSharingPolicy() {
      return getSubscriptionSharingPolicyAsString(this.subscriptionSharingPolicy);
   }

   public int getSubscriptionSharingPolicyAsInt() {
      return this.subscriptionSharingPolicy;
   }

   public void setSubscriptionSharingPolicy(String var1) throws JMSException, IllegalArgumentException {
      int var2 = getSubscriptionSharingPolicyAsInt(var1);
      this.subscriptionSharingPolicy = var2;
   }

   public int getClientIDPolicyInt() {
      return this.clientIdPolicy;
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

   public final ConnectionMetaData getMetaData() throws JMSException {
      this.checkClosed();
      return this;
   }

   public final synchronized void setExceptionListener(ExceptionListener var1) throws JMSException {
      this.checkClosed();
      this.exceptionContext = new JMSExceptionContext(var1);
   }

   void copyExceptionContext(JMSConnection var1) {
      this.exceptionContext = var1.exceptionContext;
   }

   synchronized boolean isStopped() {
      return this.stopped;
   }

   public final synchronized void start() throws JMSException {
      JMSException var1 = null;
      this.checkClosed();
      if (this.stopped) {
         Iterator var2 = this.sessions.values().iterator();

         while(var2.hasNext()) {
            JMSSession var3 = (JMSSession)var2.next();

            try {
               var3.start();
            } catch (JMSException var5) {
               if (var1 == null) {
                  var1 = var5;
               }
            }
         }

         Response var6 = this.getFrontEndDispatcher().dispatchSync(new FEConnectionStartRequest(this.connectionId));
         this.stopped = false;
         if (var1 != null) {
            throw var1;
         }
      }
   }

   private synchronized void resume(JMSConnection var1) throws JMSException {
      Iterator var2 = this.sessions.values().iterator();

      while(var2.hasNext()) {
         JMSSession var3 = (JMSSession)var2.next();
         var3.resume();
      }

      this.stopped = true;
      if (!var1.stopped) {
         Response var4 = this.getFrontEndDispatcher().dispatchSync(new FEConnectionStartRequest(this.connectionId));
         this.stopped = false;
      }
   }

   public final synchronized void stop() throws JMSException {
      this.checkClosed();
      JMSException var1 = null;
      if (!this.stopped) {
         this.stopped = true;
         if (this.isConnected()) {
            Response var2 = this.getFrontEndDispatcher().dispatchSync(new FEConnectionStopRequest(this.connectionId));
         }

         Iterator var6 = this.sessions.values().iterator();

         while(var6.hasNext()) {
            JMSSession var3 = (JMSSession)var6.next();

            try {
               var3.stop();
            } catch (JMSException var5) {
               if (var1 == null) {
                  var1 = var5;
               }
            }
         }

         if (var1 != null) {
            throw var1;
         }
      }
   }

   public final void close() throws JMSException {
      this.mergedCloseAndOnException((JMSException)null, true);
   }

   public final boolean isLocal() {
      return this.isLocal;
   }

   public final ConnectionConsumer createConnectionConsumer(Destination var1, String var2, ServerSessionPool var3, int var4) throws JMSException {
      if (!KernelStatus.isServer()) {
         throw new IllegalStateException(JMSClientExceptionLogger.logConnectionConsumerOnClientLoggable());
      } else {
         this.checkClosed();
         if (var4 != 0 && var4 >= -1) {
            if (var1 == null) {
               throw new InvalidDestinationException(JMSClientExceptionLogger.logNullDestinationLoggable().getMessage());
            } else if (!(var1 instanceof DestinationImpl)) {
               throw new InvalidDestinationException(JMSClientExceptionLogger.logForeignDestinationLoggable().getMessage());
            } else {
               Response var5 = this.getFrontEndDispatcher().dispatchSync(new FEConnectionConsumerCreateRequest(this.getJMSID(), var3, (DestinationImpl)var1, false, var2, var4, true));
               return ((JMSConnectionConsumerCreateResponse)var5).getConnectionConsumer();
            }
         } else {
            throw new weblogic.jms.common.JMSException(JMSClientExceptionLogger.logInvalidMessagesMaximumLoggable(var4));
         }
      }
   }

   private void cleanupConnection(JMSException var1, boolean var2) throws JMSException {
      synchronized(this) {
         synchronized(this.wlConnectionImpl.getConnectionStateLock()) {
            this.markClosed();
         }

         this.stopped = true;
      }

      try {
         try {
            if (this.isConnected()) {
               Response var3 = this.dispatcher.dispatchSync(new FEConnectionCloseRequest(this.connectionId));
            }
         } catch (JMSException var15) {
            if (var2) {
               throw var15;
            }
         }

         if (var2 && var1 != null) {
            throw var1;
         }
      } finally {
         InvocableManagerDelegate.delegate.invocableRemove(3, this.getJMSID());
         JMSDispatcherManager.unexportLocalDispatcher();
         JMSDispatcherManager.removeDispatcherReference(this.dispatcher);
         this.dispatcher.removeDispatcherPeerGoneListener(this);
      }

   }

   boolean isConnected() {
      return this.dispatcher.isLocal() || this.dispatcher.getDelegate() instanceof DispatcherWrapperState && !((DispatcherWrapperState)this.dispatcher.getDelegate()).getPeerGoneCache();
   }

   private void mergedCloseAndOnException(JMSException var1, boolean var2) throws JMSException {
      Iterator var3 = null;
      boolean var4 = false;
      boolean var5 = false;
      JMSExceptionContext var6 = null;
      boolean var7 = var1 == null;
      if (ReconnectController.TODOREMOVEDebug) {
         displayExceptionCauses("DEBUG JMSConnection onException", var1);
      }

      Object var8 = this.wlConnectionImpl.getConnectionStateLock();

      label1886:
      try {
         label1893: {
            synchronized(this) {
               synchronized(var8) {
                  if (this.wlConnectionImpl.getPhysical() != this) {
                     break label1893;
                  }

                  var6 = this.exceptionContext;
                  var7 = var6 == null || var1 == null || this.firedExceptoinListener;
                  this.firedExceptoinListener = true;
                  var3 = this.sessions.cloneValuesIterator();
                  if (var1 != null) {
                     this.wlConnectionImpl.setLastProblem(var1);

                     for(Object var11 = var1; var11 != null; var11 = ((Throwable)var11).getCause()) {
                        if (var11 instanceof DestroyConnectionException) {
                           this.setPreDisconnectState((JMSConnection)null);
                           this.wlConnectionImpl.setRecursiveStateNotify(-2304);
                           break;
                        }
                     }
                  }

                  var4 = this.isClosed();
                  if (var4) {
                     break label1893;
                  }

                  int var122 = this.wlConnectionImpl.getState();
                  if (var1 != null) {
                     if (var122 == 0) {
                        var5 = this.wlConnectionImpl.rememberReconnectState(this, 1040);
                     } else if (var122 == 1028) {
                        var5 = this.getPreDisconnectState() != null;
                        if (var5) {
                           this.wlConnectionImpl.setRecursiveStateNotify(1040);
                        } else {
                           this.setPreDisconnectState((JMSConnection)null);
                           this.recurseSetNoRetry(this.wlConnectionImpl);
                        }
                     }
                  } else if (var122 == 0 || var122 == 1028) {
                     this.wlConnectionImpl.setRecursiveStateNotify(1040);
                  }
               }
            }

            this.closeSessions(var3);
         }
      } finally {
         break label1886;
      }

      boolean var91 = false;

      boolean var19;
      label1898: {
         try {
            var91 = true;
            if (var4) {
               var91 = false;
               break label1898;
            }

            if (this.wlConnectionImpl.getPhysical() != this) {
               var91 = false;
               break label1898;
            }

            this.cleanupConnection((JMSException)null, var2);
            var91 = false;
         } catch (JMSException var117) {
            var91 = false;
         } finally {
            if (var91) {
               synchronized(var8) {
                  if (var4 || this.wlConnectionImpl.getPhysical() != this) {
                     return;
                  }

                  var19 = this.updateState(var5);
                  if (var7) {
                     this.wlConnectionImpl.updateFirstReconnectTime();
                     if (var19) {
                        this.scheduleReconnectTimer();
                     }

                     return;
                  }
               }

               try {
                  onException(var1, var6);
               } finally {
                  if (var19) {
                     synchronized(var8) {
                        if (this == this.wlConnectionImpl.getPhysical() && this.wlConnectionImpl.getReconnectPolicyInternal() != 0 && this.wlConnectionImpl.getState() == 512) {
                           this.wlConnectionImpl.updateFirstReconnectTime();
                           this.scheduleReconnectTimer();
                        }
                     }
                  }

               }
            }
         }

         synchronized(var8) {
            label1814: {
               if (!var4 && this.wlConnectionImpl.getPhysical() == this) {
                  var19 = this.updateState(var5);
                  if (!var7) {
                     break label1814;
                  }

                  this.wlConnectionImpl.updateFirstReconnectTime();
                  if (var19) {
                     this.scheduleReconnectTimer();
                  }

                  return;
               }

               return;
            }
         }

         boolean var59 = false;

         try {
            var59 = true;
            onException(var1, var6);
            var59 = false;
         } finally {
            if (var59) {
               if (var19) {
                  synchronized(var8) {
                     if (this == this.wlConnectionImpl.getPhysical() && this.wlConnectionImpl.getReconnectPolicyInternal() != 0 && this.wlConnectionImpl.getState() == 512) {
                        this.wlConnectionImpl.updateFirstReconnectTime();
                        this.scheduleReconnectTimer();
                     }
                  }
               }

            }
         }

         if (var19)
         synchronized(var8) {
            if (this == this.wlConnectionImpl.getPhysical() && this.wlConnectionImpl.getReconnectPolicyInternal() != 0 && this.wlConnectionImpl.getState() == 512) {
               this.wlConnectionImpl.updateFirstReconnectTime();
               this.scheduleReconnectTimer();
            }

         }
      }

      synchronized(var8) {
         if (var4 || this.wlConnectionImpl.getPhysical() != this) {
            return;
         }

         var19 = this.updateState(var5);
         if (var7) {
            this.wlConnectionImpl.updateFirstReconnectTime();
            if (var19) {
               this.scheduleReconnectTimer();
            }

            return;
         }
      }

      try {
         onException(var1, var6);
      } finally {
         if (var19) {
            synchronized(var8) {
               if (this == this.wlConnectionImpl.getPhysical() && this.wlConnectionImpl.getReconnectPolicyInternal() != 0 && this.wlConnectionImpl.getState() == 512) {
                  this.wlConnectionImpl.updateFirstReconnectTime();
                  this.scheduleReconnectTimer();
               }
            }
         }

      }

   }

   private boolean updateState(boolean var1) {
      int var2 = this.wlConnectionImpl.getState();
      int var3;
      if (var2 != 1040 && var2 != 1028) {
         var3 = var2;
      } else if (var1 && this.preDisconnectState != null && this.wlConnectionImpl.getReconnectPolicyInternal() != 0) {
         var3 = 512;
      } else {
         var3 = -2304;
      }

      if (var3 == -2304) {
         recurseSetNoRetry(this.sessions, this);
      }

      this.wlConnectionImpl.setRecursiveStateNotify(var3);
      return 512 == var3;
   }

   static void displayExceptionCauses(String var0, Throwable var1) {
      String var2 = " exception  ";
      if (var1 == null) {
         System.err.println(var0 + var2 + " null argument");
      }

      for(int var3 = 1; var3 < 40 && var1 != null; ++var3) {
         String var4;
         if (var1 instanceof LostServerException && ((LostServerException)var1).isReplayLastException()) {
            var4 = " and isReplay";
         } else {
            var4 = "";
         }

         System.err.println(var0 + var2 + var3 + ", " + var1.getClass() + var4);
         NestedThrowable var5;
         if (var1 instanceof NestedThrowable && (var5 = (NestedThrowable)var1).getNested() != null) {
            if (null != var1.getCause()) {
               displayExceptionCauses(var0 + "[has funky getCause()" + var3 + "]", var1.getCause());
            }

            var1 = var5.getNested();
            var2 = " NestedThrowable.getNested() ";
         } else {
            var1 = var1.getCause();
            var2 = " with cause ";
         }
      }

   }

   private void closeSessions(Iterator var1) throws JMSException {
      JMSException var2 = null;

      while(var1 != null && var1.hasNext()) {
         JMSSession var3 = (JMSSession)var1.next();

         try {
            var3.close();
         } catch (JMSException var5) {
            if (var2 == null) {
               var2 = var5;
            }
         }
      }

      if (var2 != null) {
         throw var2;
      }
   }

   Timer clearTimerInfo() {
      Timer var1 = this.timer;
      this.timer = null;
      return var1;
   }

   private Timer resetIntervalClearTimer() {
      this.nextRetry = (long)INITIAL_RETRY_DELAY;
      return this.clearTimerInfo();
   }

   boolean scheduleReconnectTimer() {
      if (ReconnectController.TODOREMOVEDebug) {
         (new Exception("DEBUG 1054")).printStackTrace();
      }

      long var1 = System.currentTimeMillis();
      long var3 = this.nextRetry;
      if (this.wlConnectionImpl.getLastReconnectTimer() > 0L && this.wlConnectionImpl.getLastReconnectTimer() - var1 < var3) {
         this.wlConnectionImpl.clearLastReconnectTimer();
         this.wlConnectionImpl.getConnectionStateLock().notifyAll();
         this.clearTimerInfo();
         return false;
      } else {
         this.nextRetry <<= 1;
         if (this.nextRetry > 300000L) {
            this.nextRetry = 300000L;
         } else if (this.nextRetry < 5000L) {
            this.nextRetry = 5000L;
         }

         if ((long)INITIAL_RETRY_DELAY == var3) {
            getReconnectWorkManager().schedule(this);
         } else {
            this.timer = this.getReconnectTimerManager(getReconnectWorkManager()).schedule(this, var3);
         }

         return true;
      }
   }

   static WorkManager getReconnectWorkManager() {
      synchronized(TIMER_WORKMANAGER_LOCK) {
         if (RECONNECT_WORK_MANAGER == null) {
            RECONNECT_WORK_MANAGER = WorkManagerFactory.getInstance().findOrCreate("weblogic.jms.Reconnect", 1, 5);
         }

         return RECONNECT_WORK_MANAGER;
      }
   }

   private TimerManager getReconnectTimerManager(WorkManager var1) {
      synchronized(TIMER_WORKMANAGER_LOCK) {
         if (RECONNECT_TIMER_MANAGER == null) {
            RECONNECT_TIMER_MANAGER = TimerManagerFactory.getTimerManagerFactory().getTimerManager("weblogic.jms.Reconnect", var1);
         }

         return RECONNECT_TIMER_MANAGER;
      }
   }

   private boolean isWithinReconnectTime() {
      long var1 = System.currentTimeMillis();
      long var3 = this.wlConnectionImpl.getTotalReconnectPeriodMillis();
      return var3 == -1L || var1 - this.wlConnectionImpl.getFirstReconnectTime() <= var3;
   }

   public void timerExpired(Timer var1) {
      if (this.isWithinReconnectTime()) {
         this.wlConnectionImpl.processReconnectTimer(this);
      } else {
         this.setPreDisconnectState((JMSConnection)null);
         this.recurseSetNoRetry(this.wlConnectionImpl);
         LostServerException var2 = new LostServerException("Failed to reconnect to Server within the configured reconnect time of " + this.getTotalReconnectPeriodMillis() / 1000L + " seconds");
         if (this.exceptionContext != null) {
            onException(var2, this.exceptionContext);
         } else if (ReconnectController.TODOREMOVEDebug) {
            displayExceptionCauses("Failed to reconnect to Server within the configured reconnect time of " + this.getTotalReconnectPeriodMillis() / 1000L + " seconds", new LostServerException("Network or Server down"));
         }
      }

   }

   public void run() {
      this.wlConnectionImpl.processReconnectTimer(this);
   }

   public static final void onException(JMSException var0, JMSExceptionContext var1) {
      if (ReconnectController.TODOREMOVEDebug) {
         (new Exception("DEBUG 1137")).printStackTrace();
      }

      JMSContext var2 = JMSContext.push(var1, true);

      try {
         var1.invokeListener(var0);
      } catch (Exception var8) {
         JMSClientExceptionLogger.logStackTrace(var8);
      } finally {
         JMSContext.pop(var2, true);
      }

   }

   private static boolean isJMS110() {
      Field var0;
      try {
         Class var1 = Class.forName("javax.jms.Session");
         var0 = var1.getDeclaredField("SESSION_TRANSACTED");
      } catch (Throwable var2) {
         var0 = null;
      }

      return var0 != null;
   }

   public final String getJMSVersion() {
      return isJMS110() ? "1.1" : "1.0.2b";
   }

   public final int getJMSMajorVersion() {
      return 1;
   }

   public final int getJMSMinorVersion() {
      return isJMS110() ? 1 : 0;
   }

   public final String getJMSProviderName() {
      return "BEA Systems, Inc.";
   }

   public final String getProviderVersion() {
      return "9.0.0";
   }

   public final int getProviderMajorVersion() {
      return 9;
   }

   public final int getProviderMinorVersion() {
      return 0;
   }

   void setWlConnectionImpl(WLConnectionImpl var1) {
      if (this.wlConnectionImpl == null || var1.getConnectionStateLock() != this.wlConnectionImpl.getConnectionStateLock()) {
         this.sessions.setLock(var1.getConnectionStateLock());
         this.durableSubscribers.setLock(var1.getConnectionStateLock());
      }

      this.wlConnectionImpl = var1;
   }

   JMSConnection getReplacementConnection() {
      return this.replacementConnection;
   }

   void setReplacementConnection(JMSConnection var1) {
      this.replacementConnection = var1;
   }

   public final Enumeration getJMSXPropertyNames() {
      Vector var1 = new Vector();
      var1.add("JMSXDeliveryCount");
      var1.add("JMSXGroupID");
      var1.add("JMSXGroupSeq");
      var1.add("JMSXUserID");
      return var1.elements();
   }

   public int incrementRefCount() {
      return ++this.refCount;
   }

   public int decrementRefCount() {
      return --this.refCount;
   }

   public void stateChangeListener(DispatcherStateChangeListener var1, Throwable var2) {
      if (var1 == this) {
         synchronized(this) {
            synchronized(this.wlConnectionImpl.getConnectionStateLock()) {
               if (this.wlConnectionImpl.getPhysical() == this && this.wlConnectionImpl.getState() == 0) {
                  this.wlConnectionImpl.rememberReconnectState(this, 1028);
               }
            }

         }
      }
   }

   public boolean holdsLock() {
      if (Thread.holdsLock(this)) {
         return true;
      } else {
         WLConnectionImpl var1 = this.wlConnectionImpl;
         if (var1 == null) {
            return false;
         } else {
            Object var2 = var1.getConnectionStateLock();
            return var2 == null ? false : Thread.holdsLock(var2);
         }
      }
   }

   public final void dispatcherPeerGone(Exception var1, Dispatcher var2) {
      LostServerException var3 = new LostServerException(var1);
      AbstractSubject var4 = SubjectManager.getSubjectManager().getCurrentSubject(KernelID);

      try {
         if (var4.equals(KernelID)) {
            SubjectManager.getSubjectManager().pushSubject(KernelID, SubjectManager.getSubjectManager().getAnonymousSubject());
         }

         this.mergedCloseAndOnException(var3, false);
      } catch (JMSException var10) {
      } finally {
         if (var4.equals(KernelID)) {
            SubjectManager.getSubjectManager().popSubject(KernelID);
         }

      }

   }

   private void markClosed() {
      this.isClosed = true;
   }

   public ReconnectController getReconnectController() {
      return this.wlConnectionImpl;
   }

   JMSConnection getPreDisconnectState() {
      return this.preDisconnectState;
   }

   void setPreDisconnectState(JMSConnection var1) {
      this.preDisconnectState = var1;
   }

   public Reconnectable getReconnectState(int var1) throws CloneNotSupportedException {
      JMSConnection var2 = (JMSConnection)this.clone();
      if (WLConnectionImpl.reconnectPolicyHas(2, var1)) {
         var2.sessions = recurseGetReconnectState(var2.sessions, var1);
      } else {
         var2.sessions = recurseSetNoRetry(var2.sessions, this);
      }

      var2.preDisconnectState = null;
      this.preDisconnectState = var2;
      return var2;
   }

   static LockedMap recurseGetReconnectState(LockedMap var0, int var1) throws CloneNotSupportedException {
      Iterator var2 = var0.valuesIterator();
      LockedMap var3 = new LockedMap(var0.getName(), var0.getLock());

      while(var2.hasNext()) {
         Reconnectable var4 = (Reconnectable)var2.next();
         if (!var4.isReconnectControllerClosed()) {
            var4 = var4.getReconnectState(var1);
            if (var4 != null) {
               var3.put(var4, var4);
            }
         }
      }

      return var3;
   }

   static LockedMap recurseSetNoRetry(LockedMap var0, JMSConnection var1) {
      boolean var2 = false;
      Iterator var3 = var0.valuesIterator();

      while(var3.hasNext()) {
         Object var4 = var3.next();
         if (var4 instanceof Reconnectable) {
            ReconnectController var5 = ((Reconnectable)var4).getReconnectController();
            if (var5 != null && var5.getWLConnectionImpl().getPhysical() == var1) {
               var2 = true;
               var5.setRecursiveState(-2304);
            }

            ((Reconnectable)var4).forgetReconnectState();
         }
      }

      if (var2) {
         var1.wlConnectionImpl.getConnectionStateLock().notifyAll();
      }

      return new LockedMap(var0.getName(), var0.getLock());
   }

   void recurseSetNoRetry(WLConnectionImpl var1) {
      recurseSetNoRetry(this.sessions, this);
      var1.setRecursiveStateNotify(-2304);
   }

   boolean hasTemporaryDestination() {
      Iterator var1 = this.sessions.valuesIterator();

      do {
         if (!var1.hasNext()) {
            return false;
         }
      } while(!((JMSSession)var1.next()).hasTemporaryDestination());

      return true;
   }

   static void recursePreCreateReplacement(Reconnectable var0, LockedMap var1) throws JMSException {
      Iterator var2 = var1.cloneValuesIterator();

      while(var2.hasNext()) {
         Reconnectable var3 = (Reconnectable)var2.next();
         if (!var3.isReconnectControllerClosed()) {
            var3.preCreateReplacement(var0);
         }
      }

   }

   static void recursePostCreateReplacement(LockedMap var0) {
      Iterator var1 = var0.cloneValuesIterator();

      while(var1.hasNext()) {
         Reconnectable var2 = (Reconnectable)var1.next();
         if (!var2.isReconnectControllerClosed()) {
            var2.postCreateReplacement();
         }
      }

   }

   void rememberCredentials(String var1, String var2, boolean var3) {
      this.wantXAConnection = var3;

      try {
         this.pwdStore = new PasswordStore();
         this.uHandle = this.pwdStore.storePassword(var1);
         this.pHandle = this.pwdStore.storePassword(var2);
      } catch (GeneralSecurityException var5) {
         this.pwdStore = null;
         this.uHandle = var1;
         this.pHandle = var2;
      }

   }

   public Reconnectable preCreateReplacement(Reconnectable var1) throws JMSException {
      JMSConnection var4 = this.preDisconnectState;
      if (var4 == null) {
         return null;
      } else {
         WLConnectionImpl var5 = var4.wlConnectionImpl;
         String var2;
         String var3;
         synchronized(var5.getConnectionStateLock()) {
            if (var4.pwdStore != null) {
               try {
                  var2 = (String)var4.pwdStore.retrievePassword(var4.uHandle);
                  var3 = (String)var4.pwdStore.retrievePassword(var4.pHandle);
               } catch (GeneralSecurityException var23) {
                  throw new weblogic.jms.common.JMSException(var23);
               } catch (IOException var24) {
                  throw new weblogic.jms.common.JMSException(var24);
               }
            } else {
               var2 = (String)var4.uHandle;
               var3 = (String)var4.pHandle;
            }
         }

         JMSConnection var6 = ((JMSConnectionFactory)var1).setupJMSConnection(var2, var3, var4.wantXAConnection, var4.type);

         try {
            var6.setWlConnectionImpl(var5);
            if (var4.clientId != null && var6.clientId == null) {
               var6.setClientID(var4.clientId);
            }

            var6.setAllowCloseInOnMessage(var4.allowCloseInOnMessage);
            var6.setDispatchPolicy(var4.dispatchPolicyName);
            recursePreCreateReplacement(var6, var4.sessions);
            int var7;
            synchronized(var5.getConnectionStateLock()) {
               var7 = var5.getState();
               if (var7 == 1536) {
                  JMSConnection var9 = var6;
                  this.setReplacementConnection(var6);
                  var6 = null;
                  JMSConnection var10 = var9;
                  return var10;
               }
            }

            throw new LostServerException(var5.wrongStateString(var7));
         } finally {
            try {
               if (var6 != null) {
                  var6.close();
               }
            } catch (JMSException var22) {
            }

         }
      }
   }

   public void postCreateReplacement() {
      if (ReconnectController.TODOREMOVEDebug) {
         System.err.println("debug JMSConnection stale " + this.preDisconnectState.debugMaps());
      }

      Timer var1 = this.resetIntervalClearTimer();
      if (var1 != null) {
         var1.cancel();
      }

      recursePostCreateReplacement(this.preDisconnectState.sessions);
      JMSContext var2 = this.preDisconnectState.wlConnectionImpl.getConnectionEstablishContext();
      this.getReplacementConnection().setWlConnectionImpl(this.preDisconnectState.wlConnectionImpl);
      JMSConnection var3 = this.preDisconnectState;
      var3.forgetReconnectState();
      this.preDisconnectState = null;
      this.getReplacementConnection().wlConnectionImpl.setConnectionEstablishContext(var2);
      this.wlConnectionImpl.setPhysicalReconnectable(this.getReplacementConnection());
      if (ReconnectController.TODOREMOVEDebug) {
         System.err.println("debug JMSConnection reconnect " + this.getReplacementConnection().debugMaps());
      }

   }

   String debugMaps() {
      String var1 = null;
      Iterator var2 = this.sessions.valuesIterator();

      while(var2.hasNext()) {
         String var3 = ((JMSSession)((JMSSession)var2.next())).debugMaps();
         if (var1 == null) {
            var1 = "< " + var3;
         } else {
            var1 = var1 + "\n  " + var3;
         }
      }

      return var1 + "\n>";
   }

   public Object clone() throws CloneNotSupportedException {
      JMSConnection var1 = (JMSConnection)super.clone();
      var1.sessions = (LockedMap)this.sessions.clone();
      var1.durableSubscribers = (LockedMap)this.durableSubscribers.clone();
      return var1;
   }

   public boolean isReconnectControllerClosed() {
      return this.wlConnectionImpl.isClosed();
   }

   public final synchronized ExceptionListener getExceptionListener() throws JMSException {
      this.checkClosed();
      return this.exceptionContext != null ? this.exceptionContext.getExceptionListener() : null;
   }

   public final synchronized JMSExceptionContext getJMSExceptionContext() throws JMSException {
      this.checkClosed();
      return this.exceptionContext;
   }

   public final ClientRuntimeInfo getParentInfo() {
      return this;
   }

   public final String getWLSServerName() {
      return this.wlsServerName;
   }

   public final String getRuntimeMBeanName() {
      return this.runtimeMBeanName;
   }

   public final String toString() {
      return this.getRuntimeMBeanName();
   }

   final int getDeliveryMode() {
      return this.deliveryMode;
   }

   final int getPriority() {
      return this.priority;
   }

   final long getTimeToDeliver() {
      return this.timeToDeliver;
   }

   final long getTimeToLive() {
      return this.timeToLive;
   }

   final long getSendTimeout() {
      return this.sendTimeout;
   }

   final long getRedeliveryDelay() {
      return this.redeliveryDelay;
   }

   public final void setAcknowledgePolicy(int var1) {
      synchronized(this.acknowledgePolicyLock) {
         this.acknowledgePolicy = var1;
      }
   }

   public final int getAcknowledgePolicy() {
      synchronized(this.acknowledgePolicyLock) {
         return this.acknowledgePolicy;
      }
   }

   final int getOverrunPolicy() {
      return this.overrunPolicy;
   }

   public final boolean isXAServerEnabled() {
      return this.xaServerEnabled;
   }

   final boolean isFlowControlEnabled() {
      return this.flowControl;
   }

   final int getFlowMinimum() {
      return this.flowMinimum;
   }

   final int getFlowMaximum() {
      return this.flowMaximum;
   }

   final int getFlowIncrease() {
      return this.flowIncrease;
   }

   final double getFlowDecrease() {
      return this.flowDecrease;
   }

   final long getFlowInterval() {
      return this.flowInterval;
   }

   final int getMessagesMaximum() {
      return this.messagesMaximum;
   }

   public final boolean getUserTransactionsEnabled() {
      return this.userTransactionsEnabled;
   }

   public final boolean getAllowCloseInOnMessage() {
      return this.allowCloseInOnMessage;
   }

   public final void setAllowCloseInOnMessage(boolean var1) {
      this.allowCloseInOnMessage = var1;
   }

   public void setReconnectPolicy(String var1) {
      this.wlConnectionImpl.setReconnectPolicy(var1);
   }

   public String getReconnectPolicy() {
      return this.wlConnectionImpl.getReconnectPolicy();
   }

   public void setReconnectBlockingMillis(long var1) {
      this.wlConnectionImpl.setReconnectBlockingMillis(var1);
   }

   public long getReconnectBlockingMillis() {
      return this.wlConnectionImpl.getReconnectBlockingMillis();
   }

   public long getTotalReconnectPeriodMillis() {
      return this.wlConnectionImpl.getTotalReconnectPeriodMillis();
   }

   public void setTotalReconnectPeriodMillis(long var1) {
      this.wlConnectionImpl.setTotalReconnectPeriodMillis(var1);
   }

   public int getPipelineGeneration() {
      return this.pipelineGeneration;
   }

   final boolean markDurableSubscriber(String var1) {
      synchronized(this.sublock) {
         if (this.durableSubscribers.get(var1) != null) {
            return false;
         } else {
            return this.durableSubscribers.put(var1, new Object()) == null;
         }
      }
   }

   final void addDurableSubscriber(String var1, Object var2) {
      synchronized(this.sublock) {
         this.durableSubscribers.put(var1, var2);
      }
   }

   final boolean removeDurableSubscriber(String var1) {
      synchronized(this.sublock) {
         return this.durableSubscribers.remove(var1) != null;
      }
   }

   final synchronized String getDispatchPolicy() {
      return this.dispatchPolicyName;
   }

   public final synchronized void setDispatchPolicy(String var1) {
      this.dispatchPolicyName = var1;
   }

   public final JMSID getJMSID() {
      return this.connectionId;
   }

   public ID getId() {
      return this.getJMSID();
   }

   final int getCompressionThreshold() {
      return this.compressionThreshold;
   }

   final int getSynchronousPrefetchMode() {
      return this.synchronousPrefetchMode;
   }

   final int getOneWaySendMode() {
      return this.oneWaySendMode;
   }

   final int getOneWaySendWindowSize() {
      return this.oneWaySendWindowSize;
   }

   public final InvocableMonitor getInvocableMonitor() {
      return null;
   }

   final int getReconnectPolicyInternal() {
      if (PeerInfo.VERSION_901.compareTo(this.getFEPeerInfo()) > 0) {
         this.reconnectPolicy = 0;
      }

      return this.reconnectPolicy;
   }

   final long getReconnectBlockingMillisInternal() {
      return this.reconnectBlockingMillis;
   }

   final long getTotalReconnectPeriodMillisInternal() {
      return this.totalReconnectPeriodMillis;
   }

   public final void destroyTemporaryDestination(JMSServerId var1, JMSID var2) throws JMSException {
      this.checkClosed();
      Response var3 = this.dispatcher.dispatchSync(new FETemporaryDestinationDestroyRequest(this.getJMSID(), var2));
   }

   final void consumerRemove(String var1) throws JMSException {
      Response var2 = this.dispatcher.dispatchSync(new FERemoveSubscriptionRequest(this.clientId, var1));
   }

   final void consumerRemove(DestinationImpl var1, String var2) throws JMSException {
      Response var3 = this.dispatcher.dispatchSync(new FERemoveSubscriptionRequest(this.clientId, var2, this.clientIdPolicy, var1));
   }

   public boolean isClosed() {
      return this.isClosed;
   }

   private void checkClosed() throws JMSException {
      this.getFrontEndDispatcher();
   }

   public final void publicCheckClosed() throws JMSException {
      this.checkClosed();
   }

   public final byte getPeerVersion() {
      return this.version;
   }

   public JMSConnection() {
      this.nextRetry = (long)INITIAL_RETRY_DELAY;
      this.acknowledgePolicyLock = new Object();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      byte var2;
      byte var3 = var2 = this.getVersion(var1);
      int var4;
      if (var2 >= 4) {
         if (this.sendTimeout != 10L) {
            var3 = (byte)(var3 | 64);
         }

         if (var2 >= 5) {
            var4 = 1 | this.pipelineGeneration;
            if (this.unitOfOrder != null) {
               var4 |= 2;
            }
         } else {
            var4 = 0;
         }
      } else {
         var4 = 0;
      }

      if (this.peerVersionable != null) {
         this.peerVersionable.setPeerVersion(var2);
      }

      var1.writeByte(var3);
      this.connectionId.writeExternal(var1);
      if (this.clientId == null) {
         var1.writeBoolean(false);
      } else {
         var1.writeBoolean(true);
         var1.writeUTF(this.clientId);
      }

      var1.writeInt(this.deliveryMode);
      var1.writeInt(this.priority);
      var1.writeInt(this.messagesMaximum);
      var1.writeInt(this.overrunPolicy);
      var1.writeLong(this.timeToDeliver);
      var1.writeLong(this.timeToLive);
      var1.writeLong(this.redeliveryDelay);
      var1.writeLong(this.transactionTimeout);
      var1.writeBoolean(this.userTransactionsEnabled);
      var1.writeBoolean(this.allowCloseInOnMessage);
      var1.writeInt(this.acknowledgePolicy);
      this.dispatcherWrapper.writeExternal(var1);
      if (var2 >= 3) {
         var1.writeBoolean(this.flowControl);
         if (this.flowControl) {
            var1.writeInt(this.flowMinimum);
            var1.writeInt(this.flowMaximum);
            var1.writeInt(this.flowIncrease);
            var1.writeDouble(this.flowDecrease);
            var1.writeLong(this.flowInterval);
         }

         var1.writeBoolean(this.xaServerEnabled);
      }

      if (var2 >= 4) {
         var1.writeUTF(this.wlsServerName);
         var1.writeUTF(this.runtimeMBeanName);
         if (this.sendTimeout != 10L) {
            var1.writeLong(this.sendTimeout);
         }
      }

      if (var2 >= 5) {
         var1.writeInt(var4);
         if ((var4 & 1) != 0) {
            var1.writeInt(this.compressionThreshold);
         }

         if ((var4 & 2) != 0) {
            var1.writeUTF(this.unitOfOrder);
         }

         if (var2 >= 6) {
            var1.writeInt(this.synchronousPrefetchMode);
         }

         if (var2 >= 7) {
            var1.writeInt(this.oneWaySendMode);
            var1.writeInt(this.oneWaySendWindowSize);
            var1.writeInt(this.reconnectPolicy);
            var1.writeLong(this.reconnectBlockingMillis);
            var1.writeLong(this.totalReconnectPeriodMillis);
         }
      }

      if (var2 >= 8) {
         var1.writeInt(this.clientIdPolicy);
         var1.writeInt(this.subscriptionSharingPolicy);
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      byte var3 = var1.readByte();
      byte var2 = (byte)(var3 & 15);
      switch (var2) {
         case 1:
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
            this.version = var2;
            this.connectionId = new JMSID();
            this.connectionId.readExternal(var1);
            if (var1.readBoolean()) {
               this.clientId = var1.readUTF();
            }

            this.deliveryMode = var1.readInt();
            this.priority = var1.readInt();
            this.messagesMaximum = var1.readInt();
            this.overrunPolicy = var1.readInt();
            this.timeToDeliver = var1.readLong();
            this.timeToLive = var1.readLong();
            this.redeliveryDelay = var1.readLong();
            this.transactionTimeout = var1.readLong();
            this.userTransactionsEnabled = var1.readBoolean();
            this.allowCloseInOnMessage = var1.readBoolean();
            this.acknowledgePolicy = var1.readInt();
            this.dispatcherWrapper = new DispatcherWrapper();
            this.dispatcherWrapper.readExternal(var1);
            if (var2 >= 3) {
               if (this.flowControl = var1.readBoolean()) {
                  this.flowMinimum = var1.readInt();
                  this.flowMaximum = var1.readInt();
                  this.flowIncrease = var1.readInt();
                  this.flowDecrease = var1.readDouble();
                  this.flowInterval = var1.readLong();
               }

               this.xaServerEnabled = var1.readBoolean();
            }

            if (var2 >= 4) {
               this.wlsServerName = var1.readUTF();
               this.runtimeMBeanName = var1.readUTF();
               if ((var3 & 64) != 0) {
                  this.sendTimeout = var1.readLong();
               } else {
                  this.sendTimeout = 10L;
               }
            }

            if (var2 >= 5) {
               int var4 = var1.readInt();
               this.pipelineGeneration = 15728640 & var4;
               if ((var4 & 1) != 0) {
                  this.compressionThreshold = var1.readInt();
               }

               if ((var4 & 2) != 0) {
                  this.unitOfOrder = var1.readUTF();
               }

               if (var2 >= 6) {
                  this.synchronousPrefetchMode = var1.readInt();
               }

               if (var2 >= 7) {
                  this.oneWaySendMode = var1.readInt();
                  this.oneWaySendWindowSize = var1.readInt();
                  this.internalSetReconnect(var1.readInt(), var1.readLong(), var1.readLong());
               }

               if (var2 >= 8) {
                  this.clientIdPolicy = var1.readInt();
                  this.subscriptionSharingPolicy = var1.readInt();
               }
            }

            return;
         case 2:
         default:
            throw JMSUtilities.versionIOException(var2, 1, 8);
      }
   }

   private int pushException(Request var1) {
      JMSPushExceptionRequest var2 = (JMSPushExceptionRequest)var1;

      try {
         this.mergedCloseAndOnException(var2.getException(), false);
      } catch (Throwable var4) {
      }

      var2.setState(Integer.MAX_VALUE);
      return var2.getState();
   }

   public final int invoke(Request var1) throws JMSException {
      switch (var1.getMethodId()) {
         case 15363:
            return this.pushException(var1);
         default:
            throw new weblogic.jms.common.JMSException(JMSClientExceptionLogger.logNoSuchMethodLoggable(var1.getMethodId()));
      }
   }

   private byte getVersion(Object var1) {
      PeerInfo var2 = this.peerInfo;
      if (var1 instanceof PeerInfoable) {
         PeerInfo var3 = ((PeerInfoable)var1).getPeerInfo();
         if (var3 != null) {
            var2 = var3;
         }
      }

      if (var2 != null) {
         if (var2.compareTo(PeerInfo.VERSION_1033) >= 0) {
            return 8;
         } else if (var2.compareTo(PeerInfo.VERSION_920) >= 0) {
            return 7;
         } else if (var2.compareTo(PeerInfo.VERSION_910) >= 0) {
            return 6;
         } else if (var2.compareTo(PeerInfo.VERSION_901) >= 0) {
            return 6;
         } else if (var2.compareTo(PeerInfo.VERSION_DIABLO) >= 0) {
            return 5;
         } else if (var2.compareTo(PeerInfo.VERSION_81) >= 0) {
            return 4;
         } else {
            return (byte)(var2.compareTo(PeerInfo.VERSION_70) >= 0 ? 3 : 1);
         }
      } else {
         return 8;
      }
   }

   public void forgetReconnectState() {
      JMSConnection var1 = this.preDisconnectState;
      if (var1 != null) {
         var1.forgetReconnectState();
         this.preDisconnectState = null;
      }

      JMSConnection var2 = this.replacementConnection;
      if (var2 != null) {
         var2.forgetReconnectState();
         this.replacementConnection = null;
      }

      PasswordStore var3 = this.pwdStore;
      if (var3 != null) {
         Object var4 = this.pHandle;
         Object var5 = this.uHandle;
         if (var4 != null) {
            var3.removePassword(var4);
         }

         if (var5 != null) {
            var3.removePassword(var5);
         }

         this.pwdStore = null;
      }

      this.uHandle = this.pHandle = null;
      this.wlConnectionImpl.setConnectionEstablishContext((JMSContext)null);
   }

   public PeerInfo getFEPeerInfo() {
      return this.dispatcherWrapper.getPeerInfo();
   }

   public static int convertPrefetchMode(String var0) {
      if (var0 != null && !var0.equals("disabled")) {
         if (var0.equals("enabled")) {
            return 1;
         } else {
            return var0.equals("topicSubscriberOnly") ? 2 : 0;
         }
      } else {
         return 0;
      }
   }

   public static int convertOneWaySendMode(String var0) {
      if (var0 != null && !var0.equals("disabled")) {
         if (var0.equals("enabled")) {
            return 1;
         } else {
            return var0.equals("topicOnly") ? 2 : 0;
         }
      } else {
         return 0;
      }
   }

   void reconnect() {
      JMSConnection var1 = null;
      Object var2 = null;
      JMSContext var3 = this.wlConnectionImpl.getConnectionEstablishContext();

      try {
         JMSContext var4;
         if (var3 != null) {
            var4 = JMSContext.push(var3, true);
         } else {
            var4 = null;
         }

         try {
            var1 = (JMSConnection)this.preCreateReplacement(this.wlConnectionImpl.getJmsConnectionFactory());
            JMSConnection var5 = this.preDisconnectState;
            if (var1 != null && var5 != null) {
               var1.resume(var5);
            }
         } catch (JMSException var20) {
            if (ReconnectController.TODOREMOVEDebug) {
               var20.printStackTrace();
            }

            var2 = var20;
         } catch (Error var21) {
            if (ReconnectController.TODOREMOVEDebug) {
               var21.printStackTrace();
            }

            var2 = var21;
            throw var21;
         } catch (RuntimeException var22) {
            if (ReconnectController.TODOREMOVEDebug) {
               var22.printStackTrace();
            }

            var2 = var22;
            throw var22;
         } finally {
            if (var3 != null) {
               JMSContext.pop(var4, true);
            }

         }
      } finally {
         this.wlConnectionImpl.reconnectComplete(this, var1, (Throwable)var2);
      }

   }

   static {
      boolean var0 = false;

      try {
         Class.forName("weblogic.rjvm.RJVM");
         var0 = true;
      } catch (ClassNotFoundException var2) {
      }

      isT3Client = var0;
   }
}
