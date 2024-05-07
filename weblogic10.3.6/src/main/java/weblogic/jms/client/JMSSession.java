package weblogic.jms.client;

import java.io.IOException;
import java.io.Serializable;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.HashMap;
import java.util.Iterator;
import javax.jms.BytesMessage;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.StreamMessage;
import javax.jms.TemporaryQueue;
import javax.jms.TemporaryTopic;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicPublisher;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.transaction.Transaction;
import org.w3c.dom.Document;
import weblogic.common.internal.PeerInfo;
import weblogic.jms.JMSClientExceptionLogger;
import weblogic.jms.common.AlreadyClosedException;
import weblogic.jms.common.ConsumerReconnectInfo;
import weblogic.jms.common.CrossDomainSecurityManager;
import weblogic.jms.common.Destination;
import weblogic.jms.common.DestinationImpl;
import weblogic.jms.common.DistributedDestinationImpl;
import weblogic.jms.common.IllegalStateException;
import weblogic.jms.common.InvalidDestinationException;
import weblogic.jms.common.JMSBrowserCreateResponse;
import weblogic.jms.common.JMSConsumerReceiveResponse;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSDestinationCreateResponse;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSMessageId;
import weblogic.jms.common.JMSPushEntry;
import weblogic.jms.common.JMSPushRequest;
import weblogic.jms.common.JMSServerUtilities;
import weblogic.jms.common.JMSSessionRecoverResponse;
import weblogic.jms.common.JMSWorkContextHelper;
import weblogic.jms.common.JMSWorkManager;
import weblogic.jms.common.LostServerException;
import weblogic.jms.common.MessageImpl;
import weblogic.jms.common.MessageList;
import weblogic.jms.common.MessageReference;
import weblogic.jms.common.TextMessageImpl;
import weblogic.jms.dispatcher.Invocable;
import weblogic.jms.dispatcher.InvocableManagerDelegate;
import weblogic.jms.extensions.DataOverrunException;
import weblogic.jms.extensions.JMSMessageFactoryImpl;
import weblogic.jms.extensions.WLAcknowledgeInfo;
import weblogic.jms.extensions.WLAsyncSession;
import weblogic.jms.extensions.WLMessageFactory;
import weblogic.jms.extensions.WLMessageProducer;
import weblogic.jms.extensions.XMLMessage;
import weblogic.jms.frontend.FEBrowserCloseRequest;
import weblogic.jms.frontend.FEBrowserCreateRequest;
import weblogic.jms.frontend.FEConsumerCloseRequest;
import weblogic.jms.frontend.FEConsumerCreateRequest;
import weblogic.jms.frontend.FEConsumerCreateResponse;
import weblogic.jms.frontend.FEConsumerIncrementWindowCurrentOneWayRequest;
import weblogic.jms.frontend.FEConsumerIncrementWindowCurrentRequest;
import weblogic.jms.frontend.FEConsumerReceiveRequest;
import weblogic.jms.frontend.FEDestinationCreateRequest;
import weblogic.jms.frontend.FEProducerCloseRequest;
import weblogic.jms.frontend.FEProducerCreateRequest;
import weblogic.jms.frontend.FEProducerCreateResponse;
import weblogic.jms.frontend.FESessionAcknowledgeRequest;
import weblogic.jms.frontend.FESessionCloseRequest;
import weblogic.jms.frontend.FESessionRecoverRequest;
import weblogic.jms.frontend.FESessionSetRedeliveryDelayRequest;
import weblogic.jms.frontend.FETemporaryDestinationCreateRequest;
import weblogic.jms.frontend.FETemporaryDestinationCreateResponse;
import weblogic.jms.multicast.JMSTDMSocket;
import weblogic.jms.multicast.JMSTDMSocketIPM;
import weblogic.jms.multicast.JMSTMSocket;
import weblogic.kernel.KernelStatus;
import weblogic.messaging.ID;
import weblogic.messaging.dispatcher.CompletionListener;
import weblogic.messaging.dispatcher.DispatcherException;
import weblogic.messaging.dispatcher.InvocableMonitor;
import weblogic.messaging.dispatcher.Request;
import weblogic.messaging.dispatcher.Response;
import weblogic.security.subject.AbstractSubject;
import weblogic.security.subject.SubjectManager;
import weblogic.transaction.TransactionHelper;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

public class JMSSession implements SessionInternal, Invocable, Reconnectable, Cloneable, WLAsyncSession, MMessageAsyncSession {
   private static final AbstractSubject KERNEL_ID = (AbstractSubject)AccessController.doPrivileged(SubjectManager.getKernelIdentityAction());
   private static final WLMessageFactory MESSAGEFACTORY = JMSMessageFactoryImpl.getFactory();
   private static int DUPSACKINTERVAL = 50;
   private static boolean IGNOREWINDOWCURRENT = false;
   private static boolean DECWINDOWCURRENT = true;
   private SubjectManager subjectManager = SubjectManager.getSubjectManager();
   private long expectedSequenceNumber = 1L;
   private long highMark = 1L;
   private JMSPushRequest firstPushRequest;
   private JMSPushRequest lastPushRequest;
   private JMSPushRequest firstReceivePushRequest;
   private JMSPushRequest lastReceivePushRequest;
   private UnackedMessage firstUnackedMessage;
   private PendingWTMessage firstPendingWTMessage;
   private volatile JMSID sessionId;
   private int messagesMaximum;
   private int overrunPolicy;
   private final int acknowledgePolicy;
   private final int subscriptionSharingPolicy;
   private int pushRequestCount;
   private final JMSConnection connection;
   private final boolean transacted;
   private final int acknowledgeMode;
   private int deliveryMode;
   private int priority;
   private long timeToLive;
   private final long sendTimeout;
   private boolean userTransactionsEnabled;
   private final boolean allowCloseInOnMessage;
   private long redeliveryDelay = -1L;
   private final String clientID;
   private boolean decrementWindow;
   private final WorkManager dispatchWorkManager;
   private final String dispatchPolicyName;
   private final Object lockObject = new Object();
   private boolean synchronousListener = false;
   private JMSPushRequest shortCutPushRequest = null;
   private boolean needToRemoveIt = true;
   private JMSPushRequest shortCutPrevPushRequest = null;
   private boolean waitForNewMessage = false;
   private boolean notifyNewMessage = false;
   private final Object synchronousListenerObject = new Object();
   private LockedMap consumers = new LockedMap("consumers", (Object)null);
   private LockedMap producers = new LockedMap("producers", (Object)null);
   private LockedMap browsers = new LockedMap("browsers", (Object)null);
   static final int IDLE = 0;
   static final int IN_CLOSE = 1;
   static final int IN_RECEIVE = 2;
   static final int IN_LISTENER = 4;
   static final int TYPE_UNSPECIFIED = 0;
   static final int TYPE_TOPIC = 1;
   static final int TYPE_QUEUE = 2;
   private JMSMessageContext messageContext;
   private JMSExceptionContext exceptionContext;
   private int consumerListenerCount;
   private int state = 0;
   private Thread listenerThread;
   private String runtimeMBeanName;
   private int waiterCount;
   private boolean stopped;
   private boolean running;
   private boolean recovering;
   private int type;
   private long lastSequenceNumber;
   private MessageList clientAckList;
   private JMSPushRequest recoverableClientAckMessages;
   private JMSPushRequest carryForwardOnReconnect;
   private JMSMessageReference mRefCache;
   private JMSTDMSocket dgmSock;
   private JMSTMSocket mSock;
   private int pipelineGeneration;
   private String unitOfOrder;
   private UseForRunnable useForRunnable;
   private WLSessionImpl wlSessionImpl;
   private JMSSession replacementSession;
   private boolean pendingWork;
   private volatile boolean refreshedWithPendingWork;
   private boolean prefetchStarted = false;
   private boolean prefetchDisabled = false;
   private int dupsOKAckCountDown;
   private int dupsOKAckCount = 0;
   private boolean allowDelayAckForDupsOK = true;
   private boolean requireAckForDupsOK = false;
   private boolean session_clientResponsibleForAck = false;
   private boolean connectionOlderThan90;
   private boolean recoversFor90HasBeenCalled = false;
   private JMSMessageId lastExposedMsgId;
   private JMSMessageId previousExposedMsgId;
   private JMSMessageId lastAckMsgId;
   private long lastAckSequenceNumber;
   private HashMap replacementConsumerMap;
   private boolean consumersReconnect;
   private boolean closeStarted;
   private JMSSession staleJMSSession;
   private static boolean IGNORE_JmsAsyncQueue = false;
   private boolean ignoreJmsAsyncQueue = false;
   private MMessageListener mmListener;
   private long realLastSequenceNumber;
   private boolean realLastSequenceNumberShouldApply = false;
   private int proxyGenerationForOlderServer = 0;
   private boolean closeWithExternalSequenceNumber = false;
   static final TextMessageImpl ASYNC_RESERVED_MSG;
   private boolean isRemoteDomain;
   int checkSeqGap = 1;
   int msgIndex = 1;

   protected JMSSession(JMSConnection var1, boolean var2, int var3, boolean var4) throws JMSException {
      this.connection = var1;
      this.transacted = var2;
      this.useForRunnable = new UseForRunnable(this);
      if (var2) {
         this.acknowledgeMode = 2;
      } else {
         this.acknowledgeMode = var3;
      }

      this.stopped = var4;
      this.userTransactionsEnabled = var1.getUserTransactionsEnabled();
      this.allowCloseInOnMessage = var1.getAllowCloseInOnMessage();
      this.messagesMaximum = var1.getMessagesMaximum();
      this.deliveryMode = var1.getDeliveryMode();
      this.priority = var1.getPriority();
      this.timeToLive = var1.getTimeToLive();
      this.acknowledgePolicy = var1.getAcknowledgePolicy();
      this.subscriptionSharingPolicy = var1.getSubscriptionSharingPolicyAsInt();
      this.overrunPolicy = var1.getOverrunPolicy();
      this.clientID = var1.getClientIDInternal();
      this.sendTimeout = var1.getSendTimeout();
      this.pipelineGeneration = var1.getPipelineGeneration();
      this.dispatchPolicyName = var1.getDispatchPolicy();
      this.dispatchWorkManager = WorkManagerFactory.getInstance().find(this.dispatchPolicyName);
      if (IGNORE_JmsAsyncQueue && this.dispatchPolicyName.equals("JmsAsyncQueue")) {
         this.ignoreJmsAsyncQueue = true;
      }

      if (this.userTransactionsEnabled) {
         this.allowDelayAckForDupsOK = false;
      }

      this.dupsOKAckCountDown = this.messagesMaximum == -1 && this.messagesMaximum > 100 ? DUPSACKINTERVAL : this.messagesMaximum / 2;
      this.dupsOKAckCount = this.dupsOKAckCountDown;

      try {
         if (System.getProperty("weblogic.jms.dupsOKCountDownSize") != null) {
            this.dupsOKAckCountDown = Integer.parseInt(System.getProperty("weblogic.jms.dupsOKCountDownSize"));
         }
      } catch (AccessControlException var7) {
      }

      this.connectionOlderThan90 = var1.getPeerVersion() < 5;
      if (this.pipelineGeneration == 0) {
         if (PeerInfo.VERSION_DIABLO.compareTo(this.getFEPeerInfo()) <= 0 && PeerInfo.VERSION_920.compareTo(this.getFEPeerInfo()) > 0) {
            this.pipelineGeneration = 15728640;
         }

         if (JMSDebug.JMSMessagePath.isDebugEnabled()) {
            JMSDebug.JMSMessagePath.debug("CLIENT/JMSSession (id: " + this.sessionId + ") : zero pipelineGeneration 0x" + Integer.toHexString(this.pipelineGeneration) + " peerinfo " + this.getFEPeerInfo());
         }
      }

      try {
         this.isRemoteDomain = CrossDomainSecurityManager.getCrossDomainSecurityUtil().isRemoteDomain(var1.getFrontEndDispatcher());
      } catch (IOException var6) {
      }

   }

   public Object clone() throws CloneNotSupportedException {
      JMSSession var1 = (JMSSession)super.clone();
      var1.consumers = (LockedMap)this.consumers.clone();
      var1.producers = (LockedMap)this.producers.clone();
      var1.browsers = (LockedMap)this.browsers.clone();
      return var1;
   }

   public ReconnectController getReconnectController() {
      return this.wlSessionImpl;
   }

   public Reconnectable getReconnectState(int var1) throws CloneNotSupportedException {
      JMSSession var2 = (JMSSession)this.clone();
      if (WLConnectionImpl.reconnectPolicyHas(4, var1)) {
         var2.producers = JMSConnection.recurseGetReconnectState(var2.producers, var1);
      } else {
         var2.producers = JMSConnection.recurseSetNoRetry(var2.producers, this.connection);
      }

      var2.staleJMSSession = this;
      if (WLConnectionImpl.reconnectPolicyHas(8, var1)) {
         var2.consumers = JMSConnection.recurseGetReconnectState(var2.consumers, var1);
         var2.consumersReconnect = true;
         var2.session_clientResponsibleForAck = this.session_clientResponsibleForAck;
      } else {
         if (ReconnectController.TODOREMOVEDebug && var2.consumersCount() > 0) {
            System.out.println("DEBUG JMSSession reconnectState recurseSetNoRetry consumers ignored " + var2.consumers);
         }

         var2.consumers = JMSConnection.recurseSetNoRetry(var2.consumers, this.connection);
         var2.session_clientResponsibleForAck = var2.consumersReconnect = false;
      }

      if (var2.consumersReconnect && !this.transacted && this.firstUnackedMessage != null && this.acknowledgeMode == 2) {
         var2.setPendingWork(true);
      }

      return var2;
   }

   public Reconnectable preCreateReplacement(Reconnectable var1) throws JMSException {
      JMSSession var2 = ((JMSConnection)var1).setupJMSSession(this.transacted, this.acknowledgeMode, this instanceof JMSXASession, this.type);
      var2.setMapLocks(this.producers.getLock());
      var2.exceptionContext = this.exceptionContext;
      var2.messagesMaximum = this.messagesMaximum;
      var2.overrunPolicy = this.overrunPolicy;
      var2.deliveryMode = this.deliveryMode;
      var2.priority = this.priority;
      var2.timeToLive = this.timeToLive;
      var2.synchronousListener = this.synchronousListener;
      var2.unitOfOrder = this.unitOfOrder;
      if (this.redeliveryDelay != -1L && this.redeliveryDelay != var2.redeliveryDelay) {
         var2.setRedeliveryDelay(this.redeliveryDelay);
      }

      if (this.staleJMSSession != null) {
         if (this.transacted) {
            var2.refreshedWithPendingWork = this.staleJMSSession.pendingWork;
         } else {
            var2.refreshedWithPendingWork = this.staleJMSSession.firstUnackedMessage != null;
         }
      }

      var2.subjectManager = this.subjectManager;
      var2.consumersReconnect = this.consumersReconnect;
      var2.replacementConsumerMap = new HashMap();
      JMSConnection.recursePreCreateReplacement(var2, this.consumers);
      JMSConnection.recursePreCreateReplacement(var2, this.producers);
      this.transferClientRspForAckMessages(var2);
      var2.replacementConsumerMap.clear();
      this.replacementSession = var2;
      return var2;
   }

   String debugMaps() {
      return this.producers + "\n" + this.consumers + "\n" + this.browsers;
   }

   public void postCreateReplacement() {
      JMSConnection.recursePostCreateReplacement(this.consumers);
      JMSConnection.recursePostCreateReplacement(this.producers);
      JMSSession var1 = this.replacementSession;
      var1.setWLSessionImpl(this.wlSessionImpl);
      var1.forgetReconnectState();
      this.wlSessionImpl.setPhysicalReconnectable(var1);
   }

   public void forgetReconnectState() {
      this.staleJMSSession = null;
      Iterator var1 = this.producers.cloneValuesIterator();

      Object var2;
      while(var1.hasNext()) {
         var2 = var1.next();
         if (var2 instanceof Reconnectable) {
            ((Reconnectable)var2).forgetReconnectState();
         }
      }

      var1 = this.consumers.cloneValuesIterator();

      while(var1.hasNext()) {
         var2 = var1.next();
         if (var2 instanceof Reconnectable) {
            ((Reconnectable)var2).forgetReconnectState();
         }
      }

      this.replacementSession = null;
   }

   boolean hasTemporaryDestination() {
      Iterator var1 = this.consumers.valuesIterator();

      WLSessionImpl var2;
      while(var1.hasNext()) {
         if (((JMSConsumer)var1.next()).hasTemporaryDestination()) {
            var2 = this.wlSessionImpl;
            return var2 != null && var2.getState() != -2304 && var2.getState() != -1280;
         }
      }

      var1 = this.producers.valuesIterator();

      do {
         if (!var1.hasNext()) {
            return false;
         }
      } while(!((JMSProducer)var1.next()).hasTemporaryDestination());

      var2 = this.wlSessionImpl;
      return var2 != null && var2.getState() != -2304 && var2.getState() != -1280;
   }

   public PeerInfo getFEPeerInfo() {
      return this.connection.getFEPeerInfo();
   }

   public final synchronized int getMessagesMaximum() throws JMSException {
      this.checkClosed();
      return this.messagesMaximum;
   }

   public final synchronized void setMessagesMaximum(int var1) throws JMSException {
      this.checkClosed();
      synchronized(this.lockObject) {
         if (var1 >= -1 && var1 != 0) {
            this.messagesMaximum = var1;
         } else {
            throw new weblogic.jms.common.JMSException(JMSClientExceptionLogger.logInvalidMessagesMaximumValueLoggable());
         }
      }
   }

   public int getSubscriptionSharingPolicy() throws JMSException {
      this.checkClosed();
      return this.subscriptionSharingPolicy;
   }

   public final synchronized int getOverrunPolicy() throws JMSException {
      this.checkClosed();
      return this.overrunPolicy;
   }

   public final synchronized void setOverrunPolicy(int var1) throws JMSException {
      this.checkClosed();
      if (var1 != 0 && var1 != 1) {
         throw new weblogic.jms.common.JMSException(JMSClientExceptionLogger.logInvalidOverrunPolicyLoggable(var1));
      } else {
         this.overrunPolicy = var1;
      }
   }

   public final long getRedeliveryDelay() throws JMSException {
      this.checkClosed();
      return this.getRedeliveryDelayInternal();
   }

   private long getRedeliveryDelayInternal() {
      return this.redeliveryDelay == -1L ? this.getConnection().getRedeliveryDelay() : this.redeliveryDelay;
   }

   public final void setRedeliveryDelay(long var1) throws JMSException {
      this.checkClosed();
      if (var1 < -1L) {
         throw new JMSException(JMSClientExceptionLogger.logInvalidRedeliveryDelayLoggable().getMessage());
      } else {
         long var3 = this.getRedeliveryDelayInternal();
         if (var1 != var3 && this.consumers.size() != 0) {
            this.redeliveryDelay = var1;
            Response var5 = this.connection.getFrontEndDispatcher().dispatchSync(new FESessionSetRedeliveryDelayRequest(this.sessionId, var1));
         }

      }
   }

   public final int getDeliveryMode() {
      return this.deliveryMode;
   }

   public final void setDeliveryMode(int var1) {
      this.deliveryMode = var1;
   }

   public final int getPriority() {
      return this.priority;
   }

   public final void setPriority(int var1) {
      this.priority = var1;
   }

   public final long getTimeToLive() {
      return this.timeToLive;
   }

   public final void setTimeToLive(long var1) {
      this.timeToLive = var1;
   }

   final long getSendTimeout() {
      return this.sendTimeout;
   }

   final void setId(JMSID var1) {
      this.sessionId = var1;
   }

   public final JMSID getJMSID() {
      return this.sessionId;
   }

   public ID getId() {
      return this.getJMSID();
   }

   public final void setType(int var1) {
      this.type = var1;
   }

   public void setPipelineGeneration(int var1) {
      this.pipelineGeneration = var1;
   }

   public synchronized int getPipelineGenerationFromProxy() {
      return this.connectionOlderThan90 ? this.proxyGenerationForOlderServer : this.pipelineGeneration;
   }

   final int getType() {
      return this.type;
   }

   public final InvocableMonitor getInvocableMonitor() {
      return null;
   }

   final long getLastSequenceNumber() {
      return this.realLastSequenceNumberShouldApply ? this.realLastSequenceNumber : this.lastSequenceNumber;
   }

   final synchronized void setRealLastSequenceNumber(long var1) {
      this.realLastSequenceNumber = var1;
      this.realLastSequenceNumberShouldApply = true;
   }

   final boolean isTransacted() {
      return this.transacted;
   }

   final boolean userTransactionsEnabled() {
      return this.userTransactionsEnabled;
   }

   final void setUserTransactionsEnabled(boolean var1) {
      this.userTransactionsEnabled = var1;
   }

   public String getWLSServerName() {
      return this.connection.getWLSServerName();
   }

   public ClientRuntimeInfo getParentInfo() {
      return this.connection;
   }

   public final void setRuntimeMBeanName(String var1) {
      this.runtimeMBeanName = var1;
   }

   public final String getRuntimeMBeanName() {
      return this.runtimeMBeanName;
   }

   public final JMSConnection getConnection() {
      return this.connection;
   }

   public final String toString() {
      return this.connection.getRuntimeMBeanName() + "." + this.getRuntimeMBeanName();
   }

   final MessageImpl receiveMessage(ConsumerInternal var1, long var2, CompletionListener var4) throws JMSException {
      JMSID var5 = var1.getJMSID();
      JMSConsumerReceiveResponsePrivate var6 = null;
      long var7 = System.currentTimeMillis();
      long var9 = var2;
      long var12;
      synchronized(this) {
         while(true) {
            if (!this.stopped) {
               synchronized(this.lockObject) {
                  JMSPushRequest var13 = null;

                  JMSPushRequest var15;
                  for(var15 = this.firstReceivePushRequest; var15 != null; var15 = (JMSPushRequest)var15.getNext()) {
                     JMSPushEntry var16 = var15.getFirstPushEntry();
                     if (var1.getJMSID().equals(var16.getConsumerId())) {
                        var16 = var15.removePushEntry();
                        var6 = new JMSConsumerReceiveResponsePrivate(var15.getMessage(), var16.getFrontEndSequenceNumber(), false, var16.getDeliveryCount());
                        break;
                     }

                     var13 = var15;
                  }

                  if (var6 != null && var15.getFirstPushEntry() == null) {
                     JMSPushRequest var14 = (JMSPushRequest)var15.getNext();
                     var15.setNext((Request)null);
                     if (var13 == null) {
                        this.firstReceivePushRequest = var14;
                     } else {
                        var13.setNext(var14);
                     }

                     if (var14 == null) {
                        this.lastReceivePushRequest = null;
                     }
                  }
                  break;
               }
            }

            if (!this.isClosed() && var2 != 9223372036854775806L) {
               if (this.firstReceivePushRequest != null) {
                  try {
                     this.wait(var9);
                  } catch (InterruptedException var27) {
                     throw new weblogic.jms.common.JMSException(var27);
                  }

                  if (var2 == Long.MAX_VALUE) {
                     continue;
                  }

                  var12 = System.currentTimeMillis() - var7;
                  if (var12 >= var2) {
                     return null;
                  }

                  var9 = var2 - var12;
                  if (var9 > 0L) {
                     continue;
                  }
               }

               if (!this.stopped && !this.isClosed()) {
                  continue;
               }

               return null;
            }

            return null;
         }
      }

      if (var6 != null) {
         return this.proccessReceiveResponse(var1, var6, var4);
      } else {
         if (var9 > 0L && var2 != Long.MAX_VALUE && var2 != 9223372036854775806L) {
            var9 = var2 - (var7 - System.currentTimeMillis());
         } else {
            var9 = var2;
         }

         boolean var31;
         if (!this.transacted && this.userTransactionsEnabled) {
            var12 = var2;
            var31 = false;
         } else {
            var12 = var9;
            var31 = true;
         }

         FEConsumerReceiveRequest var11 = new FEConsumerReceiveRequest(var5, var12, var4, var1);
         if (var4 == null) {
            return this.proccessReceiveResponse(var1, var31 ? this.connection.getFrontEndDispatcher().dispatchSyncNoTran(var11) : this.connection.getFrontEndDispatcher().dispatchSyncTran(var11), var4);
         } else {
            Transaction var32;
            if (var31) {
               var32 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();
            } else {
               var32 = null;
            }

            TextMessageImpl var33;
            try {
               this.connection.getFrontEndDispatcher().dispatchAsync(var11);
               var33 = ASYNC_RESERVED_MSG;
            } catch (DispatcherException var26) {
               throw new weblogic.jms.common.JMSException(var26);
            } finally {
               if (var32 != null) {
                  TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var32);
               }

            }

            return var33;
         }
      }
   }

   public MessageImpl proccessReceiveResponse(ConsumerInternal var1, Object var2, CompletionListener var3) throws JMSException {
      JMSConsumerReceiveResponse var4 = (JMSConsumerReceiveResponse)var2;
      MessageImpl var5 = null;
      MessageImpl var6 = var4.getMessage();
      if (var6 != null) {
         this.session_clientResponsibleForAck |= var6.getClientResponsibleForAcknowledge();
         int var7;
         if (var4 instanceof JMSConsumerReceiveResponsePrivate) {
            var7 = ((JMSConsumerReceiveResponsePrivate)var4).getDeliveryCount();
         } else {
            var7 = var6.getDeliveryCount();
         }

         if (this.connection.isLocal()) {
            var5 = var6.copy();
            var5.setSequenceNumber(var6.getSequenceNumber());
            var5.setClientResponsibleForAcknowledge(var6.getClientResponsibleForAcknowledge());
            var5.setDeliveryCount(var7);
         } else {
            var5 = var6;
            var6.setDeliveryCount(var7);
         }

         long var8 = var4.getSequenceNumber();
         var5.setSequenceNumber(var8);
         var5.setDDForwarded(false);
         if (this.transacted) {
            this.setPendingWork(true);
         }

         if (var4.isTransactional()) {
            synchronized(this) {
               this.rememberLastSequenceNumber(var8, var5.getId());
            }
         } else {
            synchronized(this) {
               this.rememberLastSequenceNumber(var8, var5.getId());
               this.addUnackedMessage(var1, var5);
            }

            if (this.acknowledgeMode == 2) {
               var5.setSession(this);
            } else if (this.acknowledgeMode != 4) {
               if (this.acknowledgeMode == 3 && this.checkDelayAckForDupsOK(var5) && --this.dupsOKAckCount > 0) {
                  this.requireAckForDupsOK = true;
               } else {
                  this.acknowledge(var5, this.acknowledgePolicy, false);
                  this.requireAckForDupsOK = false;
                  this.dupsOKAckCount = this.dupsOKAckCountDown;
               }
            }
         }
      }

      if (var5 != null) {
         var5.setJMSDestinationImpl((DestinationImpl)var1.getDestination());
         if (JMSDebug.JMSMessagePath.isDebugEnabled()) {
            JMSDebug.JMSMessagePath.debug("CLIENT/JMSSession (id: " + this.sessionId + ") : " + "Received message " + var5.getJMSMessageID());
         }
      }

      return this.afterReceive(var5, var1.getJMSID(), var3);
   }

   final MessageImpl afterReceive(MessageImpl var1, JMSID var2, CompletionListener var3) {
      if (var1 != null) {
         var1.setSerializeDestination(true);
         if (JMSDebug.JMSMessagePath.isDebugEnabled()) {
            JMSDebug.JMSMessagePath.debug("CLIENT/JMSConsumer (id: " + var2 + ") : Received message " + var1.getJMSMessageID());
         }

         var1.setForward(true);
      }

      if (var3 != null) {
         var3.onCompletion(var1);
      }

      return var1;
   }

   private void setPendingWorkOnMsgRecv() {
      if (this.acknowledgeMode == 2) {
         this.setPendingWork(true);
      }

   }

   private void rememberLastSequenceNumber(long var1, JMSMessageId var3) {
      this.previousExposedMsgId = this.lastExposedMsgId;
      this.lastSequenceNumber = var1;
      this.lastExposedMsgId = var3;
   }

   void rememberLastServerAck(JMSMessageId var1) {
      if (!this.transacted) {
         this.lastAckMsgId = var1;
      }
   }

   public JMSMessageId getLastAckMsgId() {
      return this.lastAckMsgId;
   }

   public JMSMessageId getLastExposedMsgId() {
      return this.lastExposedMsgId;
   }

   public final BytesMessage createBytesMessage() throws JMSException {
      this.checkClosed();
      return MESSAGEFACTORY.createBytesMessage();
   }

   public final MapMessage createMapMessage() throws JMSException {
      this.checkClosed();
      return MESSAGEFACTORY.createMapMessage();
   }

   public final Message createMessage() throws JMSException {
      this.checkClosed();
      return MESSAGEFACTORY.createMessage();
   }

   public final ObjectMessage createObjectMessage() throws JMSException {
      this.checkClosed();
      return MESSAGEFACTORY.createObjectMessage();
   }

   public final ObjectMessage createObjectMessage(Serializable var1) throws JMSException {
      this.checkClosed();
      return MESSAGEFACTORY.createObjectMessage(var1);
   }

   public final StreamMessage createStreamMessage() throws JMSException {
      this.checkClosed();
      return MESSAGEFACTORY.createStreamMessage();
   }

   public final TextMessage createTextMessage() throws JMSException {
      this.checkClosed();
      return MESSAGEFACTORY.createTextMessage();
   }

   public final TextMessage createTextMessage(String var1) throws JMSException {
      this.checkClosed();
      return MESSAGEFACTORY.createTextMessage(var1);
   }

   public final XMLMessage createXMLMessage() throws JMSException {
      this.checkClosed();
      return MESSAGEFACTORY.createXMLMessage();
   }

   public final XMLMessage createXMLMessage(String var1) throws JMSException {
      this.checkClosed();
      return MESSAGEFACTORY.createXMLMessage(var1);
   }

   public final XMLMessage createXMLMessage(Document var1) throws JMSException {
      this.checkClosed();
      return MESSAGEFACTORY.createXMLMessage(var1);
   }

   public final TextMessage createTextMessage(StringBuffer var1) throws JMSException {
      this.checkClosed();
      return MESSAGEFACTORY.createTextMessage(var1);
   }

   public boolean getTransacted() throws JMSException {
      this.checkClosed();
      return this.transacted;
   }

   public final int getAcknowledgeMode() throws JMSException {
      this.checkClosed();
      return this.transacted ? 0 : this.acknowledgeMode;
   }

   public void commit() throws JMSException {
      this.commit(-1L);
   }

   synchronized void commit(long var1) throws JMSException {
      this.checkClosed();
      if (var1 != -1L) {
         this.setRealLastSequenceNumber(var1);
      }

      if (!this.transacted) {
         throw new IllegalStateException(JMSClientExceptionLogger.logNoTransaction3Loggable());
      } else {
         this.firstUnackedMessage = null;
         this.firstPendingWTMessage = null;

         try {
            Response var3 = this.connection.getFrontEndDispatcher().dispatchSyncNoTran(new FESessionAcknowledgeRequest(this.getJMSID(), this.getLastSequenceNumber(), 1, true));
            this.decrementWindow = false;
         } catch (JMSException var7) {
            if ("ReservedRollbackOnly".equals(var7.getErrorCode())) {
               try {
                  this.recoverGuts(false, -1L);
               } catch (JMSException var6) {
               }
            }

            throw var7;
         }
      }
   }

   void rollback(long var1) throws JMSException {
      if (!this.transacted) {
         throw new IllegalStateException(JMSClientExceptionLogger.logNoTransaction4Loggable());
      } else {
         this.recoverGuts(false, var1);
      }
   }

   public void rollback() throws JMSException {
      this.rollback(-1L);
   }

   final void consumerIncrementWindowCurrent(final JMSID var1, final int var2, final boolean var3) throws JMSException {
      byte var5 = this.connection.getPeerVersion();
      if (var5 < 3) {
         Response var4 = this.connection.getFrontEndDispatcher().dispatchSync(new FEConsumerIncrementWindowCurrentRequest(var1, var2, var3));
      } else {
         final JMSConnection var6 = this.connection;
         CrossDomainSecurityManager.doAs(CrossDomainSecurityManager.getCrossDomainSecurityUtil().getRemoteSubject(this.getConnection().getFrontEndDispatcher(), CrossDomainSecurityManager.getCurrentSubject(), true), new PrivilegedExceptionAction() {
            public Object run() throws JMSException {
               var6.getFrontEndDispatcher().dispatchNoReply(new FEConsumerIncrementWindowCurrentOneWayRequest(var1, var2, var3));
               return null;
            }
         });
      }

   }

   final void stop() throws JMSException {
      synchronized(this) {
         if (!this.stopped) {
            if (!this.inListener() || !this.allowCloseInOnMessage) {
               this.waitForState(-5);
            }

            this.checkClosed();
            this.stopped = true;
         }
      }
   }

   final void start() throws JMSException {
      synchronized(this) {
         label47: {
            synchronized(this.lockObject) {
               if (!this.stopped) {
                  return;
               }

               this.checkClosed();
               this.stopped = false;
               this.notifyAll();
               if (!this.running && this.havePushRequests() || this.synchronousListener) {
                  this.running = true;
                  break label47;
               }
            }

            return;
         }
      }

      this.dispatchWorkManager.schedule(this.useForRunnable);
   }

   final void resume() throws JMSException {
      synchronized(this) {
         label45: {
            synchronized(this.lockObject) {
               this.stopped = false;
               if (this.havePushRequests() || this.synchronousListener) {
                  this.running = true;
                  break label45;
               }
            }

            return;
         }
      }

      this.dispatchWorkManager.schedule(this.useForRunnable);
   }

   public final void close() throws JMSException {
      this.close(-1L);
   }

   final void close(long var1) throws JMSException {
      JMSID var3 = null;
      boolean var4 = false;

      try {
         synchronized(this) {
            if (var1 == -1L) {
               this.realLastSequenceNumberShouldApply = false;
            } else {
               this.setRealLastSequenceNumber(var1);
            }

            this.closeStarted = true;
            this.realLastSequenceNumberShouldApply = false;
            if (!this.inListener() || !this.allowCloseInOnMessage) {
               this.waitForState(-5);
            }

            if (this.isClosed()) {
               return;
            }

            if (this.requireAckForDupsOK) {
               this.acknowledge(true, false);
            }

            var4 = true;
            var3 = this.poisonSession();
            if (this.mSock != null) {
               this.mSock.close();
               this.mSock = null;
            }

            this.poisonConsumersAndCloseLocaly();
            this.poisonProducers();
            this.poisonBrowsers();
            this.setState(1);
         }

         this.dispatchSessionCloseRequest(var3);
      } finally {
         try {
            synchronized(this) {
               this.waitForState(-3);

               try {
                  this.removeDurableConsumers();
               } finally {
                  this.consumers.clear();
                  this.producers.clear();
                  this.browsers.clear();
                  this.firstUnackedMessage = null;
                  this.firstPendingWTMessage = null;
                  this.clientAckList = null;
                  this.clearState(1);
                  this.notify();
               }

            }
         } finally {
            synchronized(this.synchronousListenerObject) {
               this.synchronousListenerObject.notifyAll();
            }

            if (var4) {
               this.connection.sessionRemove(var3);
            }

         }
      }
   }

   private JMSID poisonSession() {
      Object var1 = this.wlSessionImpl == null ? this : this.wlSessionImpl.getConnectionStateLock();
      synchronized(var1) {
         JMSID var3 = this.sessionId;
         this.sessionId = null;
         return var3;
      }
   }

   private void removeDurableConsumers() {
      Iterator var1 = this.consumers.cloneValuesIterator();

      while(var1.hasNext()) {
         ConsumerInternal var2 = (ConsumerInternal)var1.next();
         if (var2.isDurable()) {
            var2.removeDurableConsumer();
         }
      }

   }

   private void poisonBrowsers() {
      Iterator var1 = this.browsers.cloneValuesIterator();

      while(var1.hasNext()) {
         JMSQueueBrowser var2 = (JMSQueueBrowser)var1.next();
         var2.setId((JMSID)null);
      }

   }

   private void poisonConsumersAndCloseLocaly() throws JMSException {
      Iterator var1 = this.consumers.cloneValuesIterator();

      while(var1.hasNext()) {
         ConsumerInternal var2 = (ConsumerInternal)var1.next();
         this.consumerCloseLocal(var2, false);
      }

   }

   private void poisonProducers() {
      Iterator var1 = this.producers.cloneValuesIterator();

      while(var1.hasNext()) {
         JMSProducer var2 = (JMSProducer)var1.next();
         var2.setId((JMSID)null);
      }

   }

   private void dispatchSessionCloseRequest(JMSID var1) throws JMSException {
      if (this.connection.isConnected()) {
         Response var2 = this.connection.getFrontEndDispatcher().dispatchSyncNoTran(new FESessionCloseRequest(var1, this.getLastSequenceNumber()));
      }
   }

   private void waitForOutstandingReceives() throws JMSException {
      try {
         this.setState(1);
         this.waitForState(-3);
      } finally {
         this.clearState(1);
      }

   }

   public final void recover() throws JMSException {
      this.recover(-1L);
   }

   final void recover(long var1) throws JMSException {
      if (this.transacted) {
         throw new IllegalStateException(JMSClientExceptionLogger.logTransactedLoggable());
      } else {
         this.recoverGuts(true, var1);
      }
   }

   private void recoverGuts(boolean var1, long var2) throws JMSException {
      this.checkClosed();
      if (this.connection.getPeerVersion() < 5) {
         this.recoverGuts81(var1, var2);
      } else {
         this.recoverGuts90(var1, var2);
      }

   }

   private final synchronized void recoverGuts81(boolean var1, long var2) throws JMSException {
      if (var2 != -1L) {
         this.setRealLastSequenceNumber(var2);
      }

      if (this.requireAckForDupsOK) {
         this.acknowledge(false, false);
      }

      this.decrementWindow = false;
      long var4 = this.getLastSequenceNumber();
      this.rememberLastSequenceNumber(0L, (JMSMessageId)null);
      boolean var6 = this.transacted || this.firstUnackedMessage != null;
      ConsumerInternal var9;
      if (var6) {
         Response var7 = this.connection.getFrontEndDispatcher().dispatchSyncNoTran(new FESessionRecoverRequest(this.getJMSID(), var4, !var1, this.pipelineGeneration));
         this.setExpectedSequenceNumber81(((JMSSessionRecoverResponse)var7).getSequenceNumber(), false);
         Iterator var8 = this.consumers.cloneValuesIterator();

         while(var8.hasNext()) {
            var9 = (ConsumerInternal)var8.next();
            var9.setWindowCurrent(var9.getWindowMaximum());
            var9.setExpectedSequenceNumber(this.expectedSequenceNumber);
         }

         this.firstUnackedMessage = null;
         this.firstPendingWTMessage = null;
      }

      MessageList var19 = this.clientAckList;
      if (var19 != null) {
         this.clientAckList = null;
         JMSPushRequest var10 = null;
         JMSPushRequest var11 = null;
         JMSPushRequest var12 = null;
         long var15 = this.expectedSequenceNumber;

         JMSMessageReference var20;
         for(var20 = (JMSMessageReference)var19.getLast(); var20 != null; var20 = (JMSMessageReference)var20.getPrev()) {
            var9 = var20.getConsumer();
            var15 = var20.getSequenceNumber();
            var9.setExpectedSequenceNumber(var15, true);
         }

         this.setExpectedSequenceNumber81(var15, true);

         for(var20 = (JMSMessageReference)var19.getFirst(); var20 != null; var20 = (JMSMessageReference)var20.getNext()) {
            MessageImpl var14 = var20.getMessage();
            var14.incrementDeliveryCount();
            var14 = var20.getMessage();
            var14.reset();
            var14.setPropertiesWritable(false);
            var11 = new JMSPushRequest(0, (JMSID)null, var14);
            if (var10 != null) {
               var10.setNext(var11);
            } else {
               var12 = var11;
            }

            var10 = var11;
            var11 = null;
            var9 = var20.getConsumer();
            long var17 = var20.getSequenceNumber();
            JMSPushEntry var13 = new JMSPushEntry((JMSID)null, var9.getJMSID(), 0L, var17, var14.getDeliveryCount(), 2097152);
            var13.setClientResponsibleForAcknowledge(true);
            var10.setPushEntries(var13);
         }

         if (var12 != null) {
            this.pushMessage(var12, this.consumerListenerCount == 0);
         }
      }

      if (this.inState(4)) {
         this.recovering = true;
      }

      if (this.proxyGenerationForOlderServer == Integer.MAX_VALUE) {
         this.proxyGenerationForOlderServer = 0;
      } else {
         ++this.proxyGenerationForOlderServer;
      }

   }

   private final void recoverGuts90(boolean var1, long var2) throws JMSException {
      FESessionRecoverRequest var4;
      synchronized(this) {
         if (var2 != -1L) {
            this.setRealLastSequenceNumber(var2);
         }

         if (JMSDebug.JMSMessagePath.isDebugEnabled()) {
            JMSDebug.JMSMessagePath.debug("CLIENT/JMSSession (id: " + this.sessionId + ") : " + "Recover msg " + this.getLastSequenceNumber() + ", info " + (this.firstUnackedMessage == null) + "/" + (this.clientAckList == null) + "/" + this.transacted);
         }

         if (this.requireAckForDupsOK) {
            this.acknowledge(false, false);
         }

         long var6 = this.getLastSequenceNumber();
         this.rememberLastSequenceNumber(0L, (JMSMessageId)null);
         if (this.acknowledgeMode == 4 || this.acknowledgeMode == 128) {
            return;
         }

         if (this.inState(4)) {
            this.recovering = true;
         }

         this.decrementWindow = false;
         synchronized(this.lockObject) {
            int var9 = this.pipelineGeneration;
            this.pipelineGeneration = JMSPushEntry.nextRecoverGeneration(this.pipelineGeneration);
            this.recoversFor90HasBeenCalled = true;
            JMSPushRequest var10;
            if (this.session_clientResponsibleForAck) {
               var10 = this.firstPushRequest;
               this.setFirstPushRequest(this.lastPushRequest = null);
               this.pushRequestCount = 0;
               this.restoreClientAckMessages(var6, var10);
            } else {
               for(var10 = this.firstPushRequest; var10 != null; var10 = (JMSPushRequest)var10.getNext()) {
                  if (this.connection.isLocal() && var10.getFirstPushEntry().getPipelineGeneration() == 0) {
                     var10.getFirstPushEntry().setPipelineGeneration(1048576);
                  }
               }
            }

            Iterator var16 = this.consumers.cloneValuesIterator();

            while(var16.hasNext()) {
               ConsumerInternal var11 = (ConsumerInternal)var16.next();
               var11.setWindowCurrent(var11.getWindowMaximum());
            }
         }

         this.firstUnackedMessage = null;
         this.firstPendingWTMessage = null;
         var4 = new FESessionRecoverRequest(this.getJMSID(), var6, !var1, this.pipelineGeneration);
      }

      Response var5 = this.connection.getFrontEndDispatcher().dispatchSyncNoTran(var4);
   }

   private void restoreClientAckMessages(long var1, JMSPushRequest var3) {
      MessageList var4 = this.clientAckList;
      this.clientAckList = null;
      ConsumerInternal var5 = null;
      JMSID var6 = null;
      if (var4 != null) {
         for(JMSMessageReference var7 = (JMSMessageReference)var4.getFirst(); var7 != null; var7 = (JMSMessageReference)var7.getNext()) {
            if (!var7.getConsumer().isClosed()) {
               if (!var7.getConsumer().getJMSID().equals(var6)) {
                  var6 = var7.getConsumer().getJMSID();
                  var5 = this.consumerFind(var6);
               }

               if (var5 != null) {
                  MessageImpl var8 = var7.getMessage();

                  try {
                     var8.setMessageReference((MessageReference)null);
                     var8 = var8.copy();
                     var8.setMessageReference(var7);
                     var7.setMessage(var8);
                     var8.reset();
                     var8.setPropertiesWritable(false);
                  } catch (JMSException var10) {
                  }

                  if (var7.getSequenceNumber() <= var1) {
                     var7.incrementDeliveryCount();
                  }

                  JMSPushEntry var9 = new JMSPushEntry(this.getJMSID(), var7.getConsumer().getJMSID(), 0L, var7.getSequenceNumber(), var7.getDeliveryCount(), 2097152);
                  var9.setClientResponsibleForAcknowledge(true);
                  this.fabricateClientAckPushEntry(var5, var8, var9);
               }
            }
         }
      }

      JMSPushRequest var11;
      JMSPushEntry var12;
      for(var11 = this.recoverableClientAckMessages; var11 != null; var11 = (JMSPushRequest)var11.getNext()) {
         for(var12 = var11.removePushEntry(); var12 != null; var12 = var12.getNext()) {
            if (var12.getClientResponsibleForAcknowledge()) {
               if (!var12.getConsumerId().equals(var6)) {
                  var6 = var12.getConsumerId();
                  var5 = this.consumerFind(var6);
               }

               if (var5 != null) {
                  this.fabricateClientAckPushEntry(var5, var11.getMessage(), var12);
               }
            }
         }
      }

      for(var11 = var3; var11 != null; var11 = (JMSPushRequest)var11.getNext()) {
         for(var12 = var11.removePushEntry(); var12 != null; var12 = var12.getNext()) {
            if (var12.getClientResponsibleForAcknowledge()) {
               if (!var12.getConsumerId().equals(var6)) {
                  var6 = var12.getConsumerId();
                  var5 = this.consumerFind(var6);
               }

               if (var5 != null) {
                  this.fabricateClientAckPushEntry(var5, var11.getMessage(), var12);
               }
            }
         }
      }

   }

   private void fabricateClientAckPushEntry(ConsumerInternal var1, MessageImpl var2, JMSPushEntry var3) {
      var3.setPipelineGeneration(2097152);
      JMSPushRequest var4 = new JMSPushRequest(0, this.getJMSID(), var2, var3);
      if (var1.getMessageListenerContext() == null) {
         this.addPushRequests(var4, true);
         if (var1.getExpectedSequenceNumber() > this.firstReceivePushRequest.getFrontEndSequenceNumber()) {
            var1.setExpectedSequenceNumber(this.firstReceivePushRequest.getFrontEndSequenceNumber());
         }

      } else {
         if (this.lastPushRequest != null) {
            this.lastPushRequest.setNext(var4);
         } else {
            this.setFirstPushRequest(var4);
         }

         this.lastPushRequest = var4;
         ++this.pushRequestCount;
      }
   }

   private boolean duplicateMessage(MessageImpl var1, JMSID var2, MessageList var3) {
      if (var3 != null) {
         for(JMSMessageReference var4 = (JMSMessageReference)var3.getFirst(); var4 != null; var4 = (JMSMessageReference)var4.getNext()) {
            if (var4.getMessage() == var1) {
               return true;
            }
         }
      }

      for(JMSPushRequest var6 = this.firstPushRequest; var6 != null; var6 = (JMSPushRequest)var6.getNext()) {
         if (var1 == var6.getMessage()) {
            return true;
         }

         for(JMSPushEntry var5 = var6.getFirstPushEntry(); var5 != null; var5 = var5.getNext()) {
            if (var2.equals(var5.getConsumerId()) && var1.getId().equals(var6.getMessage().getId())) {
               return true;
            }
         }
      }

      return false;
   }

   public final void acknowledge() throws JMSException {
      this.acknowledge((WLAcknowledgeInfo)null, 1, false);
   }

   public final void acknowledge(Message var1) throws JMSException {
      this.throwForAckRefreshedSessionRules();
      this.acknowledge((WLAcknowledgeInfo)var1, this.acknowledgePolicy, false);
   }

   private void acknowledge(boolean var1, boolean var2) throws JMSException {
      synchronized(this) {
         long var4;
         JMSMessageId var6;
         if (this.inListener()) {
            var4 = this.getLastSequenceNumber() - 1L;
            var6 = this.previousExposedMsgId;
         } else {
            var4 = this.getLastSequenceNumber();
            var6 = this.lastExposedMsgId;
         }

         this.checkClosed();

         try {
            if (!var2) {
               this.rememberLastServerAck(var6);
               if (!var1 || this.connection.isConnected()) {
                  this.connection.getFrontEndDispatcher().dispatchSyncNoTran(new FESessionAcknowledgeRequest(this.getJMSID(), var4, 1, false));
               }

               this.rememberLastServerAck((JMSMessageId)null);
            }
         } catch (Exception var14) {
            handleException(var14);
         } finally {
            this.removePendingWTMessage(var4, IGNOREWINDOWCURRENT);
            this.removeUnackedMessage(var4, var2, false);
         }

      }
   }

   public final void acknowledge(WLAcknowledgeInfo var1) throws JMSException {
      this.throwForAckRefreshedSessionRules();
      this.acknowledge(var1, this.acknowledgePolicy, false);
   }

   void acknowledge(WLAcknowledgeInfo var1, int var2, boolean var3) throws JMSException {
      synchronized(this) {
         long var4;
         JMSMessageId var6;
         boolean var7;
         if (var1 == null) {
            var7 = false;
            var4 = this.getLastSequenceNumber();
            var6 = this.lastExposedMsgId;
         } else {
            var7 = var1.getClientResponsibleForAcknowledge();
            if (var2 == 1 && this.mmListener == null) {
               var4 = this.getLastSequenceNumber();
               var6 = this.lastExposedMsgId;
            } else {
               var4 = var1.getSequenceNumber();
               var6 = var1.getMessageId();
            }
         }

         this.checkClosed();
         if (!this.transacted) {
            try {
               if (!var7) {
                  if (var3) {
                     this.connection.getFrontEndDispatcher().dispatchSyncTran(new FESessionAcknowledgeRequest(this.getJMSID(), var4, var2, false));
                  } else {
                     this.rememberLastServerAck(var6);
                     this.connection.getFrontEndDispatcher().dispatchSyncNoTran(new FESessionAcknowledgeRequest(this.getJMSID(), var4, var2, false));
                     this.rememberLastServerAck((JMSMessageId)null);
                  }

                  if (var4 == this.getLastSequenceNumber()) {
                     this.decrementWindow = false;
                  }
               }
            } catch (Exception var16) {
               handleException(var16);
            } finally {
               this.removePendingWTMessage(var4, IGNOREWINDOWCURRENT);
               this.removeUnackedMessage(var4, var7, var3);
            }

         }
      }
   }

   void throwForAckRefreshedSessionRules() throws LostServerException {
      if (this.checkRefreshedWithPendingWork()) {
         throw new LostServerException(JMSClientExceptionLogger.logLostServerConnectionLoggable());
      }
   }

   public final void associateTransaction(Message var1) throws JMSException {
      MessageImpl var2 = (MessageImpl)var1;
      if (!this.userTransactionsEnabled && var2.getUnitOfOrder() != null) {
         throw new JMSException("associateTransaction with Unit of Order requires XASession");
      } else {
         this.acknowledge(var2, 1, true);
      }
   }

   public final MessageListener getMessageListener() throws JMSException {
      this.checkClosed();
      return this.messageContext != null ? this.messageContext.getMessageListener() : null;
   }

   public final synchronized JMSMessageContext getJMSMessageContext() {
      return this.messageContext;
   }

   public final synchronized void setMessageListener(MessageListener var1) throws JMSException {
      this.checkClosed();
      if (this.consumerListenerCount > 0) {
         throw new IllegalStateException(JMSClientExceptionLogger.logSessionHasConsumersLoggable());
      } else {
         this.messageContext = new JMSMessageContext(var1);
      }
   }

   public final synchronized void setMMessageListener(MMessageListener var1) {
      this.mmListener = var1;
   }

   public final void run() {
      JMSPushRequest var1;
      synchronized(this) {
         synchronized(this.lockObject) {
            var1 = this.firstPushRequest;
            this.setFirstPushRequest(this.lastPushRequest = null);
            this.pushRequestCount = 0;
         }

         this.setState(4);
      }

      JMSContext var3 = null;
      boolean var4 = false;

      try {
         MessageListener var2;
         if ((var2 = this.getMessageListener()) != null) {
            var3 = JMSContext.push(this.messageContext, true);

            for(var4 = true; var1 != null; var1 = (JMSPushRequest)var1.getNext()) {
               MessageImpl var5 = var1.getMessage().copy();
               var5.setDDForwarded(false);
               JMSPushEntry var6 = var1.getFirstPushEntry();
               ConsumerInternal var7 = this.consumerFind(var6.getConsumerId());
               var5.setSequenceNumber(var6.getFrontEndSequenceNumber());
               var5.setDeliveryCount(var6.getDeliveryCount());
               this.onMessage(var7, var2, var5);
            }

            return;
         }
      } catch (JMSException var16) {
         JMSClientExceptionLogger.logStackTrace(var16);
         return;
      } finally {
         this.clearState(4);
         if (var4) {
            JMSContext.pop(var3, true);
         }

      }

   }

   public final synchronized Topic createTopic(String var1) throws JMSException {
      if (this.type == 2) {
         throw new IllegalStateException(JMSClientExceptionLogger.logUnsupportedTopicOperationLoggable());
      } else {
         return (Topic)this.createDestination(var1, 2);
      }
   }

   public final synchronized TopicSubscriber createSubscriber(Topic var1) throws JMSException {
      this.checkClosed();
      byte var2 = 5;
      return (TopicSubscriber)this.createConsumer(var1, (String)null, false, (String)null, var2);
   }

   public final synchronized TopicSubscriber createSubscriber(Topic var1, String var2, boolean var3) throws JMSException {
      this.checkClosed();
      byte var4 = 5;
      return (TopicSubscriber)this.createConsumer(var1, var2, var3, (String)null, var4);
   }

   public final TopicSubscriber createDurableSubscriber(Topic var1, String var2) throws JMSException {
      if (this.type == 2) {
         throw new IllegalStateException(JMSClientExceptionLogger.logUnsupportedTopicOperation2Loggable());
      } else {
         return this.createDurableSubscriber(var1, var2, (String)null, false);
      }
   }

   public final TopicSubscriber createDurableSubscriber(Topic var1, String var2, String var3, boolean var4) throws JMSException {
      if (var2 == null) {
         throw new weblogic.jms.common.JMSException(JMSClientExceptionLogger.logNoSubscriberNameLoggable());
      } else if (var2.length() == 0) {
         throw new weblogic.jms.common.JMSException(JMSClientExceptionLogger.logZeroLengthSubscriberNameLoggable());
      } else if (var1 instanceof DistributedDestinationImpl) {
         throw new weblogic.jms.common.JMSException(JMSClientExceptionLogger.logInvalidDistributedTopicLoggable());
      } else {
         byte var5 = 5;
         return (TopicSubscriber)this.createConsumer(var1, var3, var4, var2, var5);
      }
   }

   public final TopicPublisher createPublisher(Topic var1) throws JMSException {
      byte var2 = 4;
      return (TopicPublisher)this.createProducer(var1, var2);
   }

   public final TemporaryTopic createTemporaryTopic() throws JMSException {
      if (this.type == 2) {
         throw new IllegalStateException(JMSClientExceptionLogger.logUnsupportedTopicOperation3Loggable());
      } else {
         return (TemporaryTopic)this.createTemporaryDestination(8);
      }
   }

   public final synchronized void unsubscribe(String var1) throws JMSException {
      this.checkClosed();
      if (this.type == 2) {
         throw new IllegalStateException(JMSClientExceptionLogger.logUnsupportedTopicOperation4Loggable());
      } else if (var1 != null && var1.length() != 0) {
         if (this.clientID == null) {
            throw new weblogic.jms.common.JMSException(JMSClientExceptionLogger.logInvalidUnsubscribeLoggable());
         } else if (this.connection.getClientIDPolicyInt() == 1) {
            throw new InvalidDestinationException(JMSClientExceptionLogger.logInvalidUnrestrictedUnsubscribeLoggable(var1, this.clientID));
         } else {
            this.subscriptionRemove(var1);
         }
      } else {
         throw new InvalidDestinationException(JMSClientExceptionLogger.logInvalidSubscriptionLoggable().getMessage());
      }
   }

   public synchronized void unsubscribe(Topic var1, String var2) throws JMSException {
      this.checkClosed();
      if (this.type == 2) {
         throw new IllegalStateException(JMSClientExceptionLogger.logUnsupportedTopicOperation4Loggable());
      } else if (var2 != null && var2.length() != 0) {
         if (this.clientID == null) {
            throw new weblogic.jms.common.JMSException(JMSClientExceptionLogger.logInvalidUnsubscribeLoggable());
         } else if (this.connection.getClientIDPolicyInt() != 1 || var1 != null && !(var1 instanceof DistributedDestinationImpl)) {
            this.subscriptionRemove((DestinationImpl)var1, var2);
         } else {
            throw new InvalidDestinationException(JMSClientExceptionLogger.logInvalidUnrestrictedUnsubscribe2Loggable(var2, this.clientID));
         }
      } else {
         throw new InvalidDestinationException(JMSClientExceptionLogger.logInvalidSubscriptionLoggable().getMessage());
      }
   }

   public final synchronized Queue createQueue(String var1) throws JMSException {
      if (this.type == 1) {
         throw new IllegalStateException(JMSClientExceptionLogger.logUnsupportedQueueOperationLoggable());
      } else {
         return (Queue)this.createDestination(var1, 1);
      }
   }

   public final QueueReceiver createReceiver(Queue var1) throws JMSException {
      byte var2 = 3;
      return (QueueReceiver)this.createConsumer(var1, (String)null, false, (String)null, var2);
   }

   public final QueueReceiver createReceiver(Queue var1, String var2) throws JMSException {
      byte var3 = 3;
      return (QueueReceiver)this.createConsumer(var1, var2, false, (String)null, var3);
   }

   public final QueueSender createSender(Queue var1) throws JMSException {
      byte var2 = 2;
      return (QueueSender)this.createProducer(var1, var2);
   }

   public final QueueBrowser createBrowser(Queue var1) throws JMSException {
      return this.createBrowser(var1, (String)null);
   }

   public final synchronized QueueBrowser createBrowser(Queue var1, String var2) throws JMSException {
      this.checkClosed();
      if (this.type == 1) {
         throw new IllegalStateException(JMSClientExceptionLogger.logUnsupportedQueueOperation2Loggable());
      } else {
         byte var3 = 3;
         Destination.checkDestinationType(var1, var3);
         JMSQueueBrowser var4 = new JMSQueueBrowser(var1, var2, this);
         this.browsers.put(var4.getJMSID(), var4);

         try {
            InvocableManagerDelegate.delegate.invocableAdd(22, var4);
         } catch (Exception var6) {
            JMSClientExceptionLogger.logStackTrace(var6);
         }

         return var4;
      }
   }

   final synchronized void closeBrowser(JMSID var1, boolean var2) throws JMSException {
      if (this.browsers.remove(var1) != null) {
         if (!var2) {
            Response var3 = this.connection.getFrontEndDispatcher().dispatchSync(new FEBrowserCloseRequest(var1));
         }

         InvocableManagerDelegate.delegate.invocableRemove(22, var1);
      }
   }

   public final MessageConsumer createConsumer(javax.jms.Destination var1) throws JMSException {
      this.checkClosed();
      return this.createConsumer(var1, (String)null, false, (String)null, (byte)0);
   }

   public final MessageConsumer createConsumer(javax.jms.Destination var1, String var2) throws JMSException {
      return this.createConsumer(var1, var2, false, (String)null, (byte)0);
   }

   public final MessageConsumer createConsumer(javax.jms.Destination var1, String var2, boolean var3) throws JMSException {
      return this.createConsumer(var1, var2, var3, (String)null, (byte)0);
   }

   private MessageConsumer createConsumer(javax.jms.Destination var1, String var2, boolean var3, String var4, byte var5) throws JMSException {
      JMSConsumer var6 = this.setupConsumer(var1, var2, var3, var4, var5, this.wlSessionImpl.getWLConnectionImpl().computeConsumerReconnectInfo());
      WLConsumerImpl var7 = new WLConsumerImpl(var6, this.wlSessionImpl);
      var6.setWlConsumerImpl(var7);
      return var7;
   }

   synchronized JMSConsumer setupConsumer(javax.jms.Destination var1, String var2, boolean var3, String var4, byte var5, ConsumerReconnectInfo var6) throws JMSException {
      this.checkClosed();
      if (var1 == null) {
         throw new InvalidDestinationException(JMSClientExceptionLogger.logNullDestinationLoggable().getMessage());
      } else {
         Destination.checkDestinationType(var1, var5);
         JMSConsumer var7;
         if (this.acknowledgeMode != 128) {
            var4 = this.throwWhenInvalidSubscriberName(var1, var4);
            var7 = new JMSConsumer(this, var4, (DestinationImpl)var1, var2, var3, this.messagesMaximum, var5);
         } else {
            if (var4 != null) {
               var4 = null;
            }

            String var8 = this.setupMulticastInternal(var1);
            var7 = new JMSConsumer(this, var4, (DestinationImpl)var1, var2, var3, this.messagesMaximum, var5);

            try {
               this.mSock.joinGroup((DestinationImpl)var1, var7);
            } catch (IOException var10) {
               throw new weblogic.jms.common.JMSException(JMSClientExceptionLogger.logCannotJoinMulticastGroupLoggable(var8, var10));
            }
         }

         if (var7.isDurable() && var6 != null) {
            var6.setDelayServerClose(0L);
         }

         FEConsumerCreateResponse var11 = this.consumerCreate(var4, (DestinationImpl)var1, var2, var3, this.messagesMaximum, var6);
         var7.setId(var11.getConsumerId());
         var7.setRuntimeMBeanName(var11.getRuntimeMBeanName());
         var7.setConsumerReconnectInfo(var11.getConsumerReconnectInfo());
         this.consumerAdd(var7);
         return var7;
      }
   }

   private String throwWhenInvalidSubscriberName(javax.jms.Destination var1, String var2) throws IllegalStateException {
      if (!((DestinationImpl)var1).isTopic()) {
         var2 = null;
      } else if (var2 != null) {
         if (var2.length() == 0) {
            var2 = null;
         } else if (this.clientID == null) {
            throw new IllegalStateException(JMSClientExceptionLogger.logInvalidConsumerCreationLoggable(var2));
         }
      }

      return var2;
   }

   private String setupMulticastInternal(javax.jms.Destination var1) throws weblogic.jms.common.JMSException {
      if (((DestinationImpl)var1).isQueue()) {
         throw new weblogic.jms.common.JMSException(JMSClientExceptionLogger.logNoMulticastForQueuesLoggable());
      } else {
         String var2 = ((DestinationImpl)var1).getMulticastAddress();
         int var3 = ((DestinationImpl)var1).getPort();
         if (var2 != null && var3 > 0) {
            try {
               if (this.mSock == null) {
                  this.dgmSock = new JMSTDMSocketIPM(var3);
                  this.dgmSock.setSoTimeout(1000);
                  this.mSock = new JMSTMSocket(this, this.dgmSock, 1, var3);
                  this.dispatchWorkManager.schedule(this.mSock);
               }

               return var2;
            } catch (IOException var5) {
               if (this.mSock != null) {
                  this.mSock.close();
               }

               if (this.dgmSock != null) {
                  this.dgmSock.close();
               }

               throw new weblogic.jms.common.JMSException(JMSClientExceptionLogger.logCannotOpenMulticastSocketLoggable(var5));
            }
         } else {
            throw new weblogic.jms.common.JMSException(JMSClientExceptionLogger.logTopicNoMulticastLoggable(var1.toString()));
         }
      }
   }

   public final synchronized MessageProducer createProducer(javax.jms.Destination var1) throws JMSException {
      return this.createProducer(var1, (byte)0);
   }

   synchronized JMSProducer setupJMSProducer(javax.jms.Destination var1, byte var2) throws JMSException {
      this.checkClosed();
      Destination.checkDestinationType(var1, var2);
      Response var3 = this.connection.getFrontEndDispatcher().dispatchSync(new FEProducerCreateRequest(this.sessionId, (DestinationImpl)var1));
      JMSID var4 = ((FEProducerCreateResponse)var3).getProducerId();
      JMSProducer var5 = new JMSProducer(this, var4, (DestinationImpl)var1, ((FEProducerCreateResponse)var3).getRuntimeMBeanName());
      if (this.unitOfOrder != null) {
         var5.setUnitOfOrder(this.unitOfOrder);
      }

      var5.setDestinationFlags(var2);
      this.producerAdd(var5);
      return var5;
   }

   private MessageProducer createProducer(javax.jms.Destination var1, byte var2) throws JMSException {
      JMSProducer var3 = this.setupJMSProducer(var1, var2);
      WLProducerImpl var4 = new WLProducerImpl(var3, this.wlSessionImpl);
      var3.setWlProducerImpl(var4);
      return var4;
   }

   public final TemporaryQueue createTemporaryQueue() throws JMSException {
      if (this.type == 1) {
         throw new IllegalStateException(JMSClientExceptionLogger.logUnsupportedQueueOperation3Loggable());
      } else {
         return (TemporaryQueue)this.createTemporaryDestination(4);
      }
   }

   public final synchronized void setExceptionListener(ExceptionListener var1) throws JMSException {
      this.checkClosed();
      this.exceptionContext = new JMSExceptionContext(var1);
   }

   public final synchronized JMSExceptionContext getJMSExceptionContext() {
      return this.exceptionContext;
   }

   public final synchronized ExceptionListener getExceptionListener() {
      return this.exceptionContext != null ? this.exceptionContext.getExceptionListener() : null;
   }

   public final void onException(JMSException var1) {
      JMSContext var2 = null;

      try {
         if (this.exceptionContext != null) {
            var2 = JMSContext.push(this.exceptionContext);
            this.exceptionContext.invokeListener(var1);
         } else if (var1 instanceof weblogic.jms.common.JMSException && !((weblogic.jms.common.JMSException)var1).isInformational()) {
            JMSExceptionContext var3 = this.getConnection().getJMSExceptionContext();
            if (var3 != null) {
               JMSConnection.onException(var1, var3);
            }
         }
      } catch (Throwable var8) {
         JMSClientExceptionLogger.logStackTrace(var8);
      } finally {
         if (var2 != null) {
            JMSContext.pop(var2);
         }

      }

   }

   private FEConsumerCreateResponse consumerCreate(String var1, DestinationImpl var2, String var3, boolean var4, int var5, ConsumerReconnectInfo var6) throws JMSException {
      if (var2.getType() != 4 && var2.getType() != 8 || var2.getConnection() != null && this.connection.getJMSID().equals(var2.getConnection().getJMSID())) {
         if (var1 != null && this.getConnection().getClientIDPolicyInt() == 0 && this.subscriptionSharingPolicy == 0 && !this.connection.markDurableSubscriber(var1)) {
            throw new weblogic.jms.common.JMSException(JMSClientExceptionLogger.logSubscriptionNameInUseLoggable(var1));
         } else {
            Response var7;
            try {
               var7 = this.connection.getFrontEndDispatcher().dispatchSync(new FEConsumerCreateRequest(this.sessionId, this.clientID, var1, var2, var3, var4, var5, this.getRedeliveryDelay(), var6, this.subscriptionSharingPolicy));
            } catch (JMSException var9) {
               if (var1 != null) {
                  this.connection.removeDurableSubscriber(var1);
               }

               throw var9;
            }

            if (!(var7 instanceof FEConsumerCreateResponse)) {
               if (var1 != null) {
                  this.connection.removeDurableSubscriber(var1);
               }

               throw new weblogic.jms.common.JMSException(JMSClientExceptionLogger.logInvalidFrontEndResponseLoggable(var7));
            } else {
               if (var1 != null && this.getConnection().getClientIDPolicyInt() == 0) {
                  this.connection.addDurableSubscriber(var1, ((FEConsumerCreateResponse)var7).getConsumerId());
               }

               return (FEConsumerCreateResponse)var7;
            }
         }
      } else {
         throw new InvalidDestinationException(JMSClientExceptionLogger.logInvalidConnectionLoggable().getMessage());
      }
   }

   private synchronized void consumerAdd(JMSConsumer var1) {
      this.consumers.put(var1.getJMSID(), var1);

      try {
         InvocableManagerDelegate.delegate.invocableAdd(6, var1);
      } catch (Exception var3) {
      }

   }

   public int consumersCount() {
      return this.consumers.size();
   }

   public int producersCount() {
      return this.producers.size();
   }

   final synchronized void consumerClose(ConsumerInternal var1) throws JMSException {
      this.consumerClose(var1, -1L);
   }

   final synchronized void consumerClose(ConsumerInternal var1, long var2) throws JMSException {
      if (var2 != -1L) {
         this.setRealLastSequenceNumber(var2);
      }

      if (!this.inListener() || !this.allowCloseInOnMessage) {
         this.waitForState(-5);
      }

      JMSID var4;
      synchronized(var1) {
         if (var1.isClosed()) {
            return;
         }

         var4 = this.consumerCloseLocal(var1, true);
      }

      Response var5 = this.connection.getFrontEndDispatcher().dispatchSyncNoTran(new FEConsumerCloseRequest(var4, this.getLastSequenceNumber()));
      this.waitForOutstandingReceives();
   }

   private JMSID consumerCloseLocal(ConsumerInternal var1, boolean var2) throws JMSException {
      synchronized(var1) {
         this.consumerRemove(var1, var2);
         JMSID var3 = var1.getJMSID();
         var1.setClosed(true);
         if (var2 && this.acknowledgeMode == 128) {
            this.leaveGroup((DestinationImpl)var1.getDestination(), var1);
         }

         return var3;
      }
   }

   private synchronized ConsumerInternal consumerFind(JMSID var1) {
      return (ConsumerInternal)this.consumers.get(var1);
   }

   private synchronized void consumerRemove(ConsumerInternal var1, boolean var2) {
      if (var2) {
         this.consumers.remove(var1.getJMSID());
      }

      InvocableManagerDelegate.delegate.invocableRemove(6, var1.getJMSID());
   }

   private synchronized void producerAdd(JMSProducer var1) {
      this.producers.put(var1.getJMSID(), var1);
   }

   final void producerClose(JMSID var1) throws JMSException {
      this.checkClosed();
      synchronized(this) {
         this.producers.remove(var1);
      }

      Response var2 = this.connection.getFrontEndDispatcher().dispatchSync(new FEProducerCloseRequest(var1));
   }

   public static final JMSException handleException(Exception var0) throws JMSException {
      if (var0 instanceof JMSException) {
         throw (JMSException)var0;
      } else {
         throw new weblogic.jms.common.JMSException(JMSClientExceptionLogger.logSystemErrorLoggable(var0), var0);
      }
   }

   private javax.jms.Destination createDestination(String var1, int var2) throws JMSException {
      this.checkClosed();
      Response var3 = this.connection.getFrontEndDispatcher().dispatchSyncNoTran(new FEDestinationCreateRequest(var1, var2, false));
      return ((JMSDestinationCreateResponse)var3).getDestination();
   }

   final JMSID createBackEndBrowser(DestinationImpl var1, String var2) throws JMSException {
      Response var3 = this.connection.getFrontEndDispatcher().dispatchSync(new FEBrowserCreateRequest(this.connection.getJMSID(), this.sessionId, var1, var2));
      return ((JMSBrowserCreateResponse)var3).getBrowserId();
   }

   private synchronized javax.jms.Destination createTemporaryDestination(int var1) throws JMSException {
      this.checkClosed();
      Response var2 = this.connection.getFrontEndDispatcher().dispatchSync(new FETemporaryDestinationCreateRequest(this.connection.getJMSID(), var1, true));
      ((FETemporaryDestinationCreateResponse)var2).getDestination().setConnection(this.connection);
      return ((FETemporaryDestinationCreateResponse)var2).getDestination();
   }

   private void subscriptionRemove(String var1) throws JMSException {
      if (!this.connection.markDurableSubscriber(var1)) {
         throw new weblogic.jms.common.JMSException(JMSClientExceptionLogger.logSubscriptionNameInUse2Loggable(var1));
      } else {
         try {
            this.connection.consumerRemove(var1);
         } finally {
            this.connection.removeDurableSubscriber(var1);
         }

      }
   }

   private void subscriptionRemove(DestinationImpl var1, String var2) throws JMSException {
      if (this.getConnection().getClientIDPolicyInt() == 0) {
         this.subscriptionRemove(var2);
      } else {
         this.connection.consumerRemove(var1, var2);
      }
   }

   public final void pushMessage(MessageImpl var1, JMSPushEntry var2) {
      JMSPushRequest var3 = new JMSPushRequest(0, this.sessionId, var1, var2);
      synchronized(this.lockObject) {
         this.addSelfSequencePushRequest(var3);
      }
   }

   public final JMSPushRequest getFirstPushRequest() {
      return this.firstPushRequest;
   }

   private void setFirstPushRequest(JMSPushRequest var1) {
      this.firstPushRequest = var1;
   }

   private final void addSelfSequencePushRequest(JMSPushRequest var1) {
      if (this.acknowledgeMode == 128) {
         if (this.messagesMaximum != -1 && this.pushRequestCount >= this.messagesMaximum) {
            if (this.overrunPolicy == 0) {
               this.onException(new DataOverrunException(JMSClientExceptionLogger.logDropNewerLoggable().getMessage(), var1.getMessage().getJMSMessageID(), var1.getMessage().getJMSCorrelationID(), var1.getMessage().getJMSDestination()));
               return;
            }

            this.onException(new DataOverrunException(JMSClientExceptionLogger.logDropOlderLoggable().getMessage(), this.firstPushRequest.getMessage().getJMSMessageID(), this.firstPushRequest.getMessage().getJMSCorrelationID(), this.firstPushRequest.getMessage().getJMSDestination()));
            this.setFirstPushRequest((JMSPushRequest)this.firstPushRequest.getNext());
            --this.pushRequestCount;
         }

         ++this.pushRequestCount;
      }

      if (this.firstPushRequest == null) {
         this.setFirstPushRequest(var1);
      } else {
         this.lastPushRequest.setNext(var1);
      }

      this.lastPushRequest = var1;
   }

   private final void addPushRequests(JMSPushRequest var1, boolean var2) {
      JMSPushRequest var3 = var1;
      JMSPushRequest var4 = null;
      JMSPushRequest var5 = var1;
      this.session_clientResponsibleForAck |= var1.getFirstPushEntry().getClientResponsibleForAcknowledge();
      JMSPushRequest var6;
      if (!this.connectionOlderThan90) {
         for(var6 = var1; var6 != null; var6 = (JMSPushRequest)var6.getNext()) {
            if (!var2 && this.connection.isLocal() && var6.getFirstPushEntry().getPipelineGeneration() == 0 && this.recoversFor90HasBeenCalled) {
               var6.getFirstPushEntry().setPipelineGeneration(1048576);
            }
         }
      }

      JMSPushEntry var8;
      while(!var2 && var5 != null && var5.getFrontEndSequenceNumber() < this.expectedSequenceNumber) {
         var8 = null;

         for(JMSPushEntry var7 = var5.getFirstPushEntry(); var7 != null; var7 = var7.getNext()) {
            if (var7.getClientResponsibleForAcknowledge()) {
               if (var8 == null) {
                  var5.setFirstPushEntry(var7);
               } else {
                  var8.setNext(var7);
               }

               var8 = var7;
            }
         }

         if (var8 == null) {
            if (var3 == var5) {
               var3 = (JMSPushRequest)var5.getNext();
            }

            var5 = (JMSPushRequest)var5.getNext();
         } else {
            var8.setNext((JMSPushEntry)null);
            var5.setLastPushEntry(var8);
            if (var4 == null) {
               var3 = var5;
            } else {
               var4.setNext(var5);
            }

            var4 = var5;
            var5 = (JMSPushRequest)var5.getNext();
         }
      }

      if (var3 != null) {
         if (var4 != null) {
            var4.setNext(var5);
            var5 = var4;
         }

         while(var5 != null) {
            for(var8 = var5.getFirstPushEntry(); var8 != null; var8 = var8.getNext()) {
               int var9 = var8.getPipelineGeneration();
               if (var9 != this.pipelineGeneration && var9 != 0 && !var8.getClientResponsibleForAcknowledge()) {
                  if (JMSDebug.JMSMessagePath.isDebugEnabled()) {
                     JMSDebug.JMSMessagePath.debug("ignore stale pipelineGeneration " + JMSPushEntry.displayRecoverGeneration(var9) + " when expecting " + JMSPushEntry.displayRecoverGeneration(this.pipelineGeneration));
                  }

                  var8.setPipelineGeneration(1048576);
               }
            }

            if (var5.getNext() == null) {
               break;
            }

            var5 = (JMSPushRequest)var5.getNext();
         }

         var6 = var5;
         if (var2) {
            if (this.firstReceivePushRequest == null) {
               this.firstReceivePushRequest = var3;
               this.lastReceivePushRequest = var5;
               return;
            }
         } else if (this.firstPushRequest == null) {
            this.setFirstPushRequest(var3);
            this.lastPushRequest = var5;
            return;
         }

         if (var2) {
            if (var5.getFrontEndSequenceNumber() < this.firstReceivePushRequest.getFrontEndSequenceNumber()) {
               var5.setNext(this.firstReceivePushRequest);
               this.firstReceivePushRequest = var3;
               return;
            }
         } else if (var5.getFrontEndSequenceNumber() < this.firstPushRequest.getFrontEndSequenceNumber()) {
            var5.setNext(this.firstPushRequest);
            this.firstPushRequest = var3;
            return;
         }

         if (var2) {
            if (var3.getFrontEndSequenceNumber() > this.lastReceivePushRequest.getFrontEndSequenceNumber()) {
               this.lastReceivePushRequest.setNext(var3);
               this.lastReceivePushRequest = var5;
               return;
            }
         } else if (var3.getFrontEndSequenceNumber() > this.lastPushRequest.getFrontEndSequenceNumber()) {
            this.lastPushRequest.setNext(var3);
            this.lastPushRequest = var5;
            return;
         }

         JMSPushRequest var10;
         if (var2) {
            var10 = this.firstReceivePushRequest;
         } else {
            var10 = this.firstPushRequest;
         }

         while(var10.getNext() != null) {
            if (var2 && var3.getFrontEndSequenceNumber() == ((JMSPushRequest)var10.getNext()).getFrontEndSequenceNumber()) {
               return;
            }

            if (var6.getFrontEndSequenceNumber() < ((JMSPushRequest)var10.getNext()).getFrontEndSequenceNumber() && var10.getFrontEndSequenceNumber() < var3.getFrontEndSequenceNumber()) {
               var6.setNext(var10.getNext());
               var10.setNext(var3);
               return;
            }

            var10 = (JMSPushRequest)var10.getNext();
         }

      }
   }

   private final JMSPushRequest removePushRequests() {
      JMSPushRequest var1;
      if ((var1 = this.firstPushRequest) == null) {
         return null;
      } else if (this.acknowledgeMode == 128) {
         this.setFirstPushRequest(this.lastPushRequest = null);
         this.pushRequestCount = 0;
         return var1;
      } else if (this.expectedSequenceNumber < var1.getFrontEndSequenceNumber()) {
         return null;
      } else {
         JMSPushRequest var2 = null;

         while(this.firstPushRequest != null && this.firstPushRequest.getLastPushEntry().getFrontEndSequenceNumber() < this.expectedSequenceNumber) {
            var2 = this.firstPushRequest;
            this.setFirstPushRequest((JMSPushRequest)this.firstPushRequest.getNext());
         }

         if (this.firstPushRequest == null) {
            this.lastPushRequest = null;
            return var1;
         } else {
            assert this.firstPushRequest.getFrontEndSequenceNumber() >= this.expectedSequenceNumber == this.firstPushRequest.getLastPushEntry().getFrontEndSequenceNumber() >= this.expectedSequenceNumber;

            if (this.expectedSequenceNumber < this.firstPushRequest.getFrontEndSequenceNumber()) {
               if (var2 != null) {
                  var2.setNext((Request)null);
               }

               return var1;
            } else {
               do {
                  this.expectedSequenceNumber = this.firstPushRequest.getLastPushEntry().getFrontEndSequenceNumber() + 1L;
                  var2 = this.firstPushRequest;
                  this.setFirstPushRequest((JMSPushRequest)this.firstPushRequest.getNext());
               } while(this.firstPushRequest != null && this.firstPushRequest.getFrontEndSequenceNumber() < this.expectedSequenceNumber);

               if (this.firstPushRequest == null) {
                  this.lastPushRequest = null;
               } else {
                  var2.setNext((Request)null);
               }

               return var1;
            }
         }
      }
   }

   private final void setExpectedSequenceNumber81(long var1, boolean var3) {
      synchronized(this.lockObject) {
         JMSPushRequest var5 = this.firstPushRequest;
         JMSPushRequest var6 = null;
         if (var3 || var1 > this.expectedSequenceNumber) {
            this.expectedSequenceNumber = var1;
         }

         if (!var3 && var5 != null) {
            for(; var5 != null && var5.getFrontEndSequenceNumber() < this.expectedSequenceNumber; var5 = (JMSPushRequest)var5.getNext()) {
               boolean var7 = true;
               JMSPushEntry var8 = var5.getFirstPushEntry();

               for(JMSPushEntry var9 = null; var8 != null; var8 = var8.getNext()) {
                  if (var8.getClientResponsibleForAcknowledge()) {
                     var7 = false;
                  } else if (var9 == null) {
                     var5.removePushEntry();
                  } else {
                     var9.setNext(var8.getNext());
                  }

                  var9 = var8;
               }

               if (var7) {
                  if (var6 == null) {
                     this.setFirstPushRequest((JMSPushRequest)var5.getNext());
                     if (this.firstPushRequest == null) {
                        this.lastPushRequest = null;
                        return;
                     }
                  } else {
                     var6.setNext(var5.getNext());
                  }
               } else {
                  var6 = var5;
               }
            }

         }
      }
   }

   private final boolean havePushRequests() {
      if (this.firstPushRequest == null) {
         return false;
      } else if (this.acknowledgeMode == 128) {
         return true;
      } else {
         return this.firstPushRequest.getFrontEndSequenceNumber() <= this.expectedSequenceNumber;
      }
   }

   public final void pushMessage(Request var1, boolean var2) {
      JMSPushRequest var3 = (JMSPushRequest)var1;
      synchronized(this.lockObject) {
         if (this.acknowledgeMode == 128) {
            this.addSelfSequencePushRequest(var3);
         } else {
            this.addPushRequests(var3, var2);
            if (!var2 && this.synchronousListener) {
               this.adjustHighMark();
            }
         }

         if (this.running || this.stopped || this.synchronousListener) {
            return;
         }

         if (!this.havePushRequests()) {
            return;
         }

         this.running = true;
      }

      Thread var4 = Thread.currentThread();
      boolean var5;
      if (JMSWorkManager.isThinclient()) {
         var5 = true;
      } else if (this.ignoreJmsAsyncQueue && !this.connection.isLocal()) {
         var5 = false;
      } else if (this.connection.isLocal() && this.dispatchWorkManager.getType() == 2) {
         var5 = true;
      } else if (!this.dispatchWorkManager.isThreadOwner(var4)) {
         var5 = true;
      } else {
         var5 = false;
      }

      if (var5) {
         this.dispatchWorkManager.schedule(this.useForRunnable);
      } else {
         this.executeMessage();
      }

   }

   private void executeMessage() {
      if (this.mmListener == null) {
         this.execute();
      } else {
         this.executeMM();
      }
   }

   private void adjustHighMark() {
      boolean var1 = true;
      if (this.firstPushRequest != null && this.highMark >= this.firstPushRequest.getFrontEndSequenceNumber()) {
         JMSPushRequest var2 = null;
         JMSPushRequest var3 = this.firstPushRequest;

         while(var3.getFrontEndSequenceNumber() <= this.highMark) {
            if (var3.getFrontEndSequenceNumber() == this.highMark) {
               ++this.highMark;
               if (var1 && var3.getFirstPushEntry().getPipelineGeneration() != 1048576) {
                  synchronized(this.synchronousListenerObject) {
                     if (this.waitForNewMessage) {
                        if (this.notifyNewMessage) {
                           if (var2 == null) {
                              this.setFirstPushRequest((JMSPushRequest)this.firstPushRequest.getNext());
                           } else {
                              var2.setNext(var3.getNext());
                           }

                           this.needToRemoveIt = false;
                        } else {
                           this.shortCutPrevPushRequest = var2;
                           this.needToRemoveIt = true;
                        }

                        this.shortCutPushRequest = var3;
                        this.waitForNewMessage = false;
                        this.synchronousListenerObject.notify();
                     }
                  }

                  var1 = false;
               }
            }

            var2 = var3;
            if ((var3 = (JMSPushRequest)var3.getNext()) == null) {
               break;
            }
         }

         this.expectedSequenceNumber = this.highMark;
      }
   }

   public Message getAsyncMessageForConsumer(ConsumerInternal var1, long var2) throws JMSException {
      JMSPushEntry var4 = null;
      MessageImpl var5 = null;
      boolean var6 = false;
      long var7 = System.currentTimeMillis();
      long var9 = var2;
      synchronized(this) {
         while(this.stopped) {
            if (this.isClosed() || var2 == 9223372036854775806L) {
               return null;
            }

            try {
               this.wait(var9);
            } catch (InterruptedException var24) {
               throw new weblogic.jms.common.JMSException(var24);
            }

            if (var2 != Long.MAX_VALUE) {
               long var12 = System.currentTimeMillis() - var7;
               if (var12 >= var2) {
                  return null;
               }

               var9 = var2 - var12;
               if (var9 > 0L) {
               }
            }
         }
      }

      synchronized(this.lockObject) {
         boolean var29 = false;

         label192:
         while(true) {
            while(true) {
               if (this.firstPushRequest == null || this.firstPushRequest.getFrontEndSequenceNumber() >= this.highMark) {
                  break label192;
               }

               var4 = this.firstPushRequest.getFirstPushEntry();
               if (!var4.getClientResponsibleForAcknowledge()) {
                  if (this.connection.isLocal()) {
                     var29 = true;
                     if (var4.getPipelineGeneration() == 0) {
                        var29 = false;
                     } else if (var4.getPipelineGeneration() == 1048576) {
                        var29 = true;
                     } else if (var4.getPipelineGeneration() == this.pipelineGeneration) {
                        var29 = false;
                     }
                  } else {
                     var29 = false;
                     if (var4.getPipelineGeneration() == 1048576 || var4.getPipelineGeneration() != this.pipelineGeneration) {
                        var29 = true;
                     }
                  }

                  if (var29) {
                     var4 = null;
                     this.setFirstPushRequest((JMSPushRequest)this.firstPushRequest.getNext());
                     continue;
                  }
               }

               if (var4.getConsumerId().equals(var1.getId())) {
                  var5 = this.firstPushRequest.getMessage();
                  if (this.connection.isLocal()) {
                     var5 = var5.copy();
                  }

                  this.setFirstPushRequest((JMSPushRequest)this.firstPushRequest.getNext());
                  break label192;
               }

               var4 = null;
               this.setFirstPushRequest((JMSPushRequest)this.firstPushRequest.getNext());
            }
         }

         if (var4 == null) {
            if (var2 != 9223372036854775806L) {
               this.waitForNewMessage = true;
               if (var2 == Long.MAX_VALUE) {
                  this.notifyNewMessage = true;
               } else {
                  this.notifyNewMessage = false;
               }
            } else {
               this.waitForNewMessage = this.notifyNewMessage = false;
            }
         }

         this.shortCutPushRequest = this.shortCutPrevPushRequest = null;
      }

      if (var4 == null && var2 != 9223372036854775806L) {
         boolean var13 = false;
         JMSPushRequest var11;
         JMSPushRequest var30;
         synchronized(this.synchronousListenerObject) {
            if (this.shortCutPushRequest == null) {
               try {
                  this.synchronousListenerObject.wait(var9);
               } catch (InterruptedException var23) {
               }
            }

            var11 = this.shortCutPushRequest;
            var30 = this.shortCutPrevPushRequest;
            this.shortCutPushRequest = this.shortCutPrevPushRequest = null;
            var13 = this.needToRemoveIt;
            this.needToRemoveIt = false;
         }

         if (var11 != null) {
            if (var13) {
               synchronized(this) {
                  synchronized(this.lockObject) {
                     if (var30 == null) {
                        this.setFirstPushRequest((JMSPushRequest)this.firstPushRequest.getNext());
                     } else {
                        var30.setNext(var11.getNext());
                     }
                  }
               }
            }

            var5 = var11.getMessage();
            if (this.connection.isLocal()) {
               var5 = var5.copy();
            }

            var4 = var11.getFirstPushEntry();
         } else {
            synchronized(this) {
               if (this.isClosed()) {
                  throw new IllegalStateException(JMSClientExceptionLogger.logSessionIsClosedLoggable());
               }
            }
         }
      }

      if (var4 == null) {
         return null;
      } else {
         boolean var28 = var4.getClientResponsibleForAcknowledge();
         var5.setJMSDestinationImpl((DestinationImpl)var1.getDestination());
         var5.setSequenceNumber(var4.getFrontEndSequenceNumber());
         var5.setDeliveryCount(var4.getDeliveryCount());
         var5.setClientResponsibleForAcknowledge(var28);
         this.rememberLastSequenceNumber(var4.getFrontEndSequenceNumber(), var5.getId());
         if (this.acknowledgeMode == 2) {
            var5.setSession(this);
            this.addUnackedMessage(var1, var5);
            var1.decrementWindowCurrent(var28);
         } else if (this.acknowledgeMode != 1 && this.acknowledgeMode != 3) {
            var1.decrementWindowCurrent(var28);
         } else {
            this.addUnackedMessage(var1, var5);
            if (this.acknowledgeMode == 3 && this.checkDelayAckForDupsOK(var5) && --this.dupsOKAckCount > 0) {
               this.requireAckForDupsOK = true;
            } else {
               this.requireAckForDupsOK = false;
               this.acknowledge(var5, 1, false);
               this.dupsOKAckCount = this.dupsOKAckCountDown;
            }

            var1.decrementWindowCurrent(var28);
         }

         return var5;
      }
   }

   protected final void execute() {
      boolean var2 = false;
      AbstractSubject var4 = null;
      ClassLoader var6 = null;
      Context var8 = null;
      ClassLoader var9 = null;
      if (KernelStatus.isServer()) {
         var9 = Thread.currentThread().getContextClassLoader();
         var6 = var9;
      }

      try {
         label1828:
         do {
            JMSPushRequest var1;
            synchronized(this) {
               synchronized(this.lockObject) {
                  if (this.stopped || var2) {
                     this.running = false;
                     return;
                  }

                  var1 = this.removePushRequests();
                  if (!this.connectionOlderThan90 && var1 != null) {
                     int var12 = var1.getFirstPushEntry().getPipelineGeneration();
                     if (var12 != this.pipelineGeneration && var12 != 2097152 && var12 != 0) {
                        var1 = (JMSPushRequest)var1.getNext();
                        this.assignClientAckCarryForward(var1);
                        continue;
                     }
                  }

                  this.assignClientAckCarryForward(var1);
                  if (var1 == null) {
                     this.running = false;
                     return;
                  }
               }

               this.setState(4);
            }

            while(true) {
               boolean var79 = false;

               try {
                  var79 = true;
                  MessageImpl var11 = var1.getMessage();
                  if (this.connection.isLocal()) {
                     var11 = var11.copy();
                  }

                  var11.setDDForwarded(false);

                  for(JMSPushEntry var125 = var1.removePushEntry(); var125 != null && var1 != null && !var2; var125 = var1 == null ? null : var1.removePushEntry()) {
                     ConsumerInternal var13 = this.consumerFind(var125.getConsumerId());
                     boolean var14 = var125.getClientResponsibleForAcknowledge();
                     if (var13 != null && !var13.isClosed()) {
                        if (!var14) {
                           if (this.pipelineGeneration == 0) {
                              if (var125.getFrontEndSequenceNumber() < var13.getExpectedSequenceNumber() && !var14) {
                                 continue;
                              }
                           } else if (var125.getPipelineGeneration() == 1048576) {
                              continue;
                           }
                        }

                        MessageListener var10;
                        if ((var10 = var13.getMessageListener()) != null) {
                           var11.setJMSDestinationImpl((DestinationImpl)var13.getDestination());
                           MessageImpl var15 = var125.getNext() == null ? var11 : var11.copy();
                           var15.setSequenceNumber(var125.getFrontEndSequenceNumber());
                           var15.setDeliveryCount(var125.getDeliveryCount());
                           var15.setClientResponsibleForAcknowledge(var14);
                           this.addUnackedMessage(var13, var15);
                           JMSMessageContext var16 = var13.getMessageListenerContext();
                           Context var7 = var16.getContext();
                           ClassLoader var5 = var16.getClassLoader();
                           AbstractSubject var3 = var16.getSubject();
                           if (var3 != var4) {
                              if (var4 != null) {
                                 this.subjectManager.popSubject(KERNEL_ID);
                              }

                              this.subjectManager.pushSubject(KERNEL_ID, var3);
                              var4 = var3;
                           }

                           if (KernelStatus.isServer()) {
                              if (var7 != var8) {
                                 if (var8 != null) {
                                    JMSServerUtilities.popLocalJNDIContext();
                                 }

                                 JMSServerUtilities.pushLocalJNDIContext(var7);
                                 var8 = var7;
                              }

                              if (var5 != var6) {
                                 Thread.currentThread().setContextClassLoader(var5);
                                 var6 = var5;
                              }
                           }

                           boolean var102 = false;

                           label1880: {
                              try {
                                 var102 = true;
                                 this.decrementWindow = true;
                                 this.recoverableClientAckMessages = var1;
                                 this.onMessage(var13, var10, var15);
                                 var102 = false;
                                 break label1880;
                              } catch (Throwable var118) {
                                 JMSClientExceptionLogger.logStackTrace(var118);
                                 var102 = false;
                              } finally {
                                 if (var102) {
                                    this.recoverableClientAckMessages = null;
                                    if (this.recovering) {
                                       synchronized(this) {
                                          synchronized(this.lockObject) {
                                             if (this.recovering) {
                                                this.recovering = false;
                                                var1 = this.carryForwardOnReconnect = null;
                                             }
                                          }
                                       }
                                    }

                                    if (this.decrementWindow) {
                                       var13.decrementWindowCurrent(var14);
                                    }

                                 }
                              }

                              this.recoverableClientAckMessages = null;
                              if (this.recovering) {
                                 synchronized(this) {
                                    synchronized(this.lockObject) {
                                       if (this.recovering) {
                                          this.recovering = false;
                                          var1 = this.carryForwardOnReconnect = null;
                                       }
                                    }
                                 }
                              }

                              if (this.decrementWindow) {
                                 var13.decrementWindowCurrent(var14);
                              }
                              continue;
                           }

                           this.recoverableClientAckMessages = null;
                           if (this.recovering) {
                              synchronized(this) {
                                 synchronized(this.lockObject) {
                                    if (this.recovering) {
                                       this.recovering = false;
                                       var1 = this.carryForwardOnReconnect = null;
                                    }
                                 }
                              }
                           }

                           if (this.decrementWindow) {
                              var13.decrementWindowCurrent(var14);
                           }
                        }
                     } else {
                        synchronized(this) {
                           var2 |= this.stopped || this.closeStarted;
                           if (var2) {
                              synchronized(this.lockObject) {
                                 var125.setNext(var1.getFirstPushEntry());
                                 var1.setFirstPushEntry(var125);

                                 for(JMSPushEntry var17 = var125; var17 != null; var17 = var17.getNext()) {
                                    var1.setLastPushEntry(var17);
                                 }
                                 break;
                              }
                           }
                        }
                     }
                  }

                  if (var2) {
                     var79 = false;
                     break;
                  }

                  if (var1 != null) {
                     var1 = (JMSPushRequest)var1.getNext();
                     synchronized(this.lockObject) {
                        this.assignClientAckCarryForward(var1);
                     }
                  }

                  if (var1 != null) {
                     if (!var2) {
                        continue;
                     }

                     var79 = false;
                     break;
                  }

                  var79 = false;
                  break;
               } catch (Throwable var120) {
                  var79 = false;
               } finally {
                  if (var79) {
                     synchronized(this) {
                        this.clearState(4);
                        synchronized(this.lockObject) {
                           if (this.stopped || var2 || !this.havePushRequests()) {
                              this.running = false;
                              return;
                           }
                        }

                     }
                  }
               }

               synchronized(this) {
                  this.clearState(4);
                  synchronized(this.lockObject) {
                     if (this.stopped || var2 || !this.havePushRequests()) {
                        this.running = false;
                        return;
                     }
                     continue label1828;
                  }
               }
            }

            synchronized(this) {
               this.clearState(4);
               synchronized(this.lockObject) {
                  if (this.stopped || var2 || !this.havePushRequests()) {
                     this.running = false;
                     return;
                  }
               }
            }
         } while(!this.dispatchWorkManager.scheduleIfBusy(this.useForRunnable));

      } finally {
         if (KernelStatus.isServer()) {
            Thread.currentThread().setContextClassLoader(var9);
            if (var8 != null) {
               JMSServerUtilities.popLocalJNDIContext();
            }

            if (var4 != null) {
               this.subjectManager.popSubject(KERNEL_ID);
            }
         }

      }
   }

   protected final void executeMM() {
      do {
         JMSPushRequest var1 = null;
         MessageWrapper var2 = null;
         MessageWrapper var3 = null;
         boolean var4 = false;
         boolean var5 = false;

         label548:
         try {
            while(true) {
               int var45;
               synchronized(this) {
                  if (this.stopped) {
                     break;
                  }

                  label562:
                  synchronized(this.lockObject) {
                     var45 = this.pipelineGeneration;
                     var1 = this.removePushRequests();
                     if (var1 != null) {
                        if (!this.connectionOlderThan90) {
                           int var8 = var1.getFirstPushEntry().getPipelineGeneration();
                           if (var8 != this.pipelineGeneration && var8 != 2097152 && var8 != 0) {
                              continue;
                           }
                        }
                        break label562;
                     }
                     break;
                  }

                  if (!var5) {
                     this.setState(4);
                  }

                  var5 = true;
               }

               while(var1 != null) {
                  MessageImpl var6 = var1.getMessage();
                  var6.setDDForwarded(false);

                  for(JMSPushEntry var7 = var1.removePushEntry(); var7 != null; var7 = var1.removePushEntry()) {
                     assert !var7.getClientResponsibleForAcknowledge();

                     ConsumerInternal var46 = this.consumerFind(var7.getConsumerId());
                     if (var46 != null && !var46.isClosed() && (this.pipelineGeneration != 0 || var7.getFrontEndSequenceNumber() >= var46.getExpectedSequenceNumber() && var7.getPipelineGeneration() != 1048576)) {
                        try {
                           if (var46.getMessageListener() == null) {
                              continue;
                           }
                        } catch (JMSException var41) {
                           continue;
                        }

                        var6.setJMSDestinationImpl((DestinationImpl)var46.getDestination());
                        var6.setClientResponsibleForAcknowledge(false);
                        var6.setSequenceNumber(var7.getFrontEndSequenceNumber());
                        this.addPendingWTMessage(var46, var7.getFrontEndSequenceNumber());
                        this.addUnackedMessage(var46, var6);
                        MessageWrapper var9 = new MessageWrapper(((JMSConsumer)var46).getWLConsumerImpl().getProxyID(), this.connectionOlderThan90 ? this.proxyGenerationForOlderServer : var45, var7.getFrontEndSequenceNumber(), var7.getDeliveryCount(), var6);
                        if (var3 == null) {
                           var2 = var9;
                        } else {
                           var3.next(var9);
                        }

                        var3 = var9;
                     }
                  }

                  var1 = (JMSPushRequest)var1.getNext();
               }
            }
         } finally {
            break label548;
         }

         label493:
         try {
            if (var3 != null) {
               synchronized(this) {
                  this.rememberLastSequenceNumber(var3.getSequence(), var3.getMessageImpl().getId());
               }

               this.mmListener.onMessages(var2, this.messagesMaximum);
            }
         } finally {
            break label493;
         }

         synchronized(this) {
            if (var5) {
               this.clearState(4);
            }

            synchronized(this.lockObject) {
               if (!this.stopped && this.havePushRequests()) {
                  continue;
               }

               this.running = false;
            }

            return;
         }
      } while(!this.dispatchWorkManager.scheduleIfBusy(this.useForRunnable));

   }

   private void assignClientAckCarryForward(JMSPushRequest var1) {
      if (var1 != null && var1.getMessage().getClientResponsibleForAcknowledge()) {
         this.carryForwardOnReconnect = var1;
      } else {
         this.carryForwardOnReconnect = var1;
      }

   }

   private void onMessage(ConsumerInternal var1, MessageListener var2, final MessageImpl var3) {
      if (!this.transacted && this.acknowledgeMode == 2) {
         var3.setSession(this);
      }

      synchronized(this) {
         this.rememberLastSequenceNumber(var3.getSequenceNumber(), var3.getId());
      }

      boolean var4 = var3.getClientResponsibleForAcknowledge();

      label130: {
         try {
            JMSWorkContextHelper.infectThread(var3);
            var3.setSerializeDestination(true);
            var3.setForward(true);
            var2.onMessage(var3);
            break label130;
         } catch (Throwable var14) {
            this.handleOnMessageFailure(var14);
         } finally {
            JMSWorkContextHelper.disinfectThread();
         }

         return;
      }

      if ((this.acknowledgeMode == 3 || this.acknowledgeMode == 1) && this.getLastSequenceNumber() != 0L && !this.transacted) {
         try {
            if (this.acknowledgeMode == 3 && this.checkDelayAckForDupsOK(var3) && --this.dupsOKAckCount > 0) {
               this.requireAckForDupsOK = true;
            } else {
               this.requireAckForDupsOK = false;
               this.refreshedWithPendingWork = false;
               if (this.isRemoteDomain) {
                  CrossDomainSecurityManager.doAs(CrossDomainSecurityManager.getCrossDomainSecurityUtil().getRemoteSubject(this.getConnection().getFrontEndDispatcher(), (AbstractSubject)null, false), new PrivilegedExceptionAction() {
                     public Object run() throws JMSException {
                        JMSSession.this.acknowledge((WLAcknowledgeInfo)var3);
                        return null;
                     }
                  });
               } else {
                  this.acknowledge((WLAcknowledgeInfo)var3);
               }

               this.dupsOKAckCount = this.dupsOKAckCountDown;
            }
         } catch (JMSException var13) {
            this.onException(var13);
         }

      }
   }

   private void handleOnMessageFailure(Throwable var1) {
      JMSClientExceptionLogger.logStackTrace(var1);

      try {
         if (!this.transacted && (this.acknowledgeMode == 1 || this.acknowledgeMode == 3)) {
            try {
               if (this.isRemoteDomain) {
                  CrossDomainSecurityManager.doAs(CrossDomainSecurityManager.getCrossDomainSecurityUtil().getRemoteSubject(this.getConnection().getFrontEndDispatcher(), (AbstractSubject)null, false), new PrivilegedExceptionAction() {
                     public Object run() throws JMSException {
                        JMSSession.this.recover();
                        return null;
                     }
                  });
               } else {
                  this.recover();
               }
            } catch (JMSException var9) {
               JMSClientExceptionLogger.logStackTrace(var9);
            }
         }
      } finally {
         RuntimeException var5 = new RuntimeException(JMSClientExceptionLogger.logClientThrowingExceptionLoggable().getMessage(), var1);
         weblogic.jms.common.JMSException var6 = new weblogic.jms.common.JMSException(var5);
         this.onException(var6);
      }

   }

   final void decrementConsumerListenerCount() {
      --this.consumerListenerCount;
   }

   final void incrementConsumerListenerCount() {
      ++this.consumerListenerCount;
   }

   final void setState(int var1) {
      this.state |= var1;
      if ((var1 & 4) != 0) {
         this.listenerThread = Thread.currentThread();
      }

   }

   private boolean inState(int var1) {
      return (this.state & var1) != 0;
   }

   private boolean inListener() {
      return Thread.currentThread().equals(this.listenerThread);
   }

   final synchronized void clearState(int var1) {
      this.state &= ~var1;
      if ((var1 & 4) != 0) {
         this.listenerThread = null;
      }

      if (this.waiterCount > 0) {
         this.notifyAll();
      }

   }

   private void waitForState(int var1) throws JMSException {
      while((this.state & ~var1) != 0) {
         ++this.waiterCount;

         try {
            this.wait();
         } catch (InterruptedException var7) {
            throw new weblogic.jms.common.JMSException(var7);
         } finally {
            --this.waiterCount;
         }
      }

   }

   public boolean isClosed() {
      return this.sessionId == null;
   }

   public boolean isReconnectControllerClosed() {
      return this.wlSessionImpl == null || this.wlSessionImpl.isClosed();
   }

   final synchronized void checkClosed() throws JMSException {
      if (this.isClosed()) {
         Object var1 = this.wlSessionImpl == null ? this : this.wlSessionImpl.getConnectionStateLock();
         synchronized(var1){}

         try {
            if (this.isReconnectControllerClosed()) {
               throw new AlreadyClosedException(JMSClientExceptionLogger.logSessionIsClosedLoggable());
            } else {
               throw new LostServerException(JMSClientExceptionLogger.logLostServerConnectionLoggable());
            }
         } finally {
            ;
         }
      }
   }

   public final void publicCheckClosed() throws JMSException {
      this.checkClosed();
   }

   public final synchronized void checkSAFClosed() throws JMSException {
      if (this.isClosed()) {
         throw new IllegalStateException(JMSClientExceptionLogger.logSessionIsClosedLoggable());
      }
   }

   private void leaveGroup(DestinationImpl var1, ConsumerInternal var2) throws JMSException {
      try {
         this.mSock.leaveGroup(var1, var2);
      } catch (IOException var4) {
         throw new weblogic.jms.common.JMSException(JMSClientExceptionLogger.logCannotLeaveMulticastGroupLoggable(var1.getMulticastAddress(), var4));
      }
   }

   private synchronized void addUnackedMessage(ConsumerInternal var1, MessageImpl var2) {
      if (this.acknowledgeMode != 4 && this.acknowledgeMode != 128) {
         if (!var2.getClientResponsibleForAcknowledge()) {
            UnackedMessage var3 = new UnackedMessage(var1, var2.getSequenceNumber());
            var3.next = this.firstUnackedMessage;
            this.firstUnackedMessage = var3;
            this.setPendingWorkOnMsgRecv();
         } else {
            if (this.clientAckList == null) {
               this.clientAckList = new MessageList();
            }

            long var6 = var2.getSequenceNumber();
            JMSMessageReference var5;
            if (this.mRefCache != null) {
               var5 = this.mRefCache;
               this.mRefCache = null;
               var5.reset(var2, var1);
            } else {
               var5 = new JMSMessageReference(var2, var1);
            }

            var2.setMessageReference(var5);
            var5.setSequenceNumber(var6);
            this.clientAckList.addLast(var5);
         }

      }
   }

   private synchronized void addPendingWTMessage(ConsumerInternal var1, long var2) {
      PendingWTMessage var4 = new PendingWTMessage(var1, var2);
      var4.next = this.firstPendingWTMessage;
      this.firstPendingWTMessage = var4;
   }

   public synchronized void removePendingWTMessage(long var1, boolean var3) throws JMSException {
      PendingWTMessage var4 = this.firstPendingWTMessage;

      PendingWTMessage var5;
      for(var5 = null; var4 != null && var1 != var4.sequenceNumber; var4 = var4.next) {
         var5 = var4;
      }

      if (var4 != null) {
         if (var4 == this.firstPendingWTMessage) {
            this.firstPendingWTMessage = null;
         } else {
            var5.next = null;
         }

         for(; var4 != null; var4 = var4.next) {
            ConsumerInternal var6 = var4.consumer;
            if (var3) {
               var6.decrementWindowCurrent(false);
            }
         }
      }

   }

   private synchronized void removeUnackedMessage(long var1, boolean var3, boolean var4) {
      if (var3) {
         if (this.clientAckList != null) {
            JMSMessageReference var8 = (JMSMessageReference)this.clientAckList.removeBeforeSequenceNumber(var1);
            if (this.mRefCache == null && var8 != null) {
               var8.prepareForCache();
               this.mRefCache = var8;
            }

         }
      } else {
         UnackedMessage var5 = this.firstUnackedMessage;

         UnackedMessage var6;
         for(var6 = null; var5 != null && var1 != var5.sequenceNumber; var5 = var5.next) {
            var6 = var5;
         }

         if (var5 != null) {
            if (var5 == this.firstUnackedMessage) {
               this.firstUnackedMessage = null;
            } else {
               var6.next = null;
            }

            for(; var5 != null; var5 = var5.next) {
               ConsumerInternal var7 = var5.consumer;
               if (!var4 && var7.getWindowCurrent() < var7.getWindowMaximum()) {
                  var7.setWindowCurrent(var7.getWindowCurrent() + 1);
               }
            }
         }

      }
   }

   public final int invoke(Request var1) throws JMSException {
      switch (var1.getMethodId()) {
         case 15620:
            this.pushMessage(var1, false);
            return Integer.MAX_VALUE;
         default:
            throw new weblogic.jms.common.JMSException(JMSClientExceptionLogger.logNoSuchMethod2Loggable(var1.getMethodId(), this.getClass().getName()));
      }
   }

   public static String unitOfOrderFromID(JMSID var0) {
      return "weblogicUOO.T" + Long.toString(var0.getTimestamp(), 36) + ".S" + Long.toString((long)var0.getSeed(), 36) + ".C" + var0.getCounter();
   }

   public void setUnitOfOrder(String var1) {
      if (".System".equals(var1)) {
         var1 = unitOfOrderFromID(this.getJMSID());
      }

      this.unitOfOrder = var1;
   }

   public void markAsSystemMessageListener(boolean var1) {
      this.synchronousListener = var1;
      this.prefetchStarted = false;
   }

   void setWLSessionImpl(WLSessionImpl var1) {
      this.wlSessionImpl = var1;
      this.setMapLocks(this.wlSessionImpl.getConnectionStateLock());
   }

   void setMapLocks(Object var1) {
      this.consumers.setLock(var1);
      this.producers.setLock(var1);
      this.browsers.setLock(var1);
   }

   void setPendingWork(boolean var1) {
      this.pendingWork = var1;
   }

   boolean checkRefreshedWithPendingWork() {
      this.setPendingWork(false);
      if (this.refreshedWithPendingWork) {
         this.refreshedWithPendingWork = false;
         return true;
      } else {
         return false;
      }
   }

   boolean prefetchStarted() {
      return this.prefetchStarted;
   }

   void startPrefetch() {
      this.prefetchStarted = true;
   }

   boolean prefetchDisabled() {
      return this.prefetchDisabled;
   }

   void disablePrefetch() {
      this.prefetchDisabled = true;
   }

   private boolean checkDelayAckForDupsOK(MessageImpl var1) {
      if (this.allowDelayAckForDupsOK && var1.getClientResponsibleForAcknowledge()) {
         this.allowDelayAckForDupsOK = false;
      }

      return this.allowDelayAckForDupsOK;
   }

   private void transferClientRspForAckMessages(JMSSession var1) {
      if (var1.session_clientResponsibleForAck && this.staleJMSSession != null && this.staleJMSSession.session_clientResponsibleForAck) {
         synchronized(this.staleJMSSession) {
            synchronized(this.staleJMSSession.lockObject) {
               this.carryForwardOnReconnect = this.staleJMSSession.carryForwardOnReconnect;
               if (this.carryForwardOnReconnect != null) {
                  if (this.firstPushRequest == null) {
                     this.setFirstPushRequest(this.firstPushRequest = this.carryForwardOnReconnect);
                     this.lastPushRequest = this.lastPushInList(this.firstPushRequest);
                  } else {
                     JMSPushRequest var4;
                     for(var4 = this.lastPushInList(this.carryForwardOnReconnect); var4.getNext() != null && ((JMSPushRequest)var4.getNext()).getFrontEndSequenceNumber() < this.firstPushRequest.getFrontEndSequenceNumber(); var4 = (JMSPushRequest)var4.getNext()) {
                     }

                     if (var4.getFrontEndSequenceNumber() < this.firstPushRequest.getFrontEndSequenceNumber()) {
                        var4.setNext(this.firstPushRequest);
                        this.setFirstPushRequest(this.carryForwardOnReconnect);
                     }
                  }
               }
            }
         }
      }

      if (this.consumersReconnect && this.session_clientResponsibleForAck) {
         var1.session_clientResponsibleForAck = true;
         long var2 = 0L;
         if (this.clientAckList != null) {
            JMSMessageReference var12 = (JMSMessageReference)this.clientAckList.getLast();
            if (var12 != null) {
               var2 = var12.getSequenceNumber();
            }
         }

         boolean var13 = this.firstPushRequest != null && this.firstPushRequest.getMessage().getClientResponsibleForAcknowledge();
         if (var13) {
            var2 = this.getMaxSequenceNumber(this.firstPushRequest, var2);
         }

         boolean var5 = this.firstReceivePushRequest != null && this.firstReceivePushRequest.getMessage().getClientResponsibleForAcknowledge();
         if (var5) {
            var2 = this.getMaxSequenceNumber(this.firstReceivePushRequest, var2);
         }

         if (this.clientAckList != null) {
            for(JMSMessageReference var6 = (JMSMessageReference)this.clientAckList.getFirst(); var6 != null; var6 = (JMSMessageReference)var6.getNext()) {
               MessageImpl var7 = var6.getMessage();
               var6.setSequenceNumber(var6.getSequenceNumber() - var2);
               var7.setSequenceNumber(var7.getSequenceNumber() - var2);
               ConsumerInternal var8 = var6.getConsumer();
               if (var8 != null) {
                  JMSConsumer var9 = (JMSConsumer)var1.replacementConsumerMap.get(var8.getJMSID());
                  if (var9 != null) {
                     var6.reset(var7, var9);
                  }
               }
            }
         }

         var1.clientAckList = this.clientAckList;
         if (var13) {
            var1.setFirstPushRequest(this.refreshPushRequests(var1, this.firstPushRequest, var2));
            var1.lastPushRequest = this.lastPushInList(var1.firstPushRequest);
         }

         if (var5) {
            var1.firstReceivePushRequest = this.refreshPushRequests(var1, this.firstReceivePushRequest, var2);
            var1.lastReceivePushRequest = this.lastPushInList(var1.firstReceivePushRequest);
         }

      }
   }

   private JMSPushRequest lastPushInList(JMSPushRequest var1) {
      JMSPushRequest var2 = var1;

      JMSPushRequest var3;
      for(var3 = var1; var2 != null; var2 = (JMSPushRequest)var2.getNext()) {
         var3 = var2;
      }

      return var3;
   }

   void mapReplacementConsumer(JMSConsumer var1, JMSConsumer var2) {
      this.replacementConsumerMap.put(var1.getJMSID(), var2);
   }

   private JMSPushRequest refreshPushRequests(JMSSession var1, JMSPushRequest var2, long var3) {
      while(var2 != null && var2.getFirstPushEntry() == null) {
         var2 = (JMSPushRequest)var2.getNext();
      }

      JMSPushRequest var5 = null;

      for(JMSPushRequest var6 = null; var2 != null; var2 = (JMSPushRequest)var2.getNext()) {
         MessageImpl var7 = var2.getMessage();
         JMSPushRequest var8 = new JMSPushRequest(0, (JMSID)null, var7);
         if (var5 == null) {
            var5 = var8;
         } else {
            var6.setNext(var8);
         }

         var7.setSequenceNumber(var7.getSequenceNumber() - var3);
         var6 = var8;

         for(JMSPushEntry var9 = var2.getFirstPushEntry(); var9 != null; var9 = var9.getNext()) {
            JMSConsumer var10 = (JMSConsumer)var1.replacementConsumerMap.get(var9.getConsumerId());
            if (var10 != null && var9.getClientResponsibleForAcknowledge()) {
               long var11 = var9.getFrontEndSequenceNumber() - var3;
               JMSPushEntry var13 = new JMSPushEntry((JMSID)null, var10.getJMSID(), 0L, var11, var7.getDeliveryCount(), 2097152);
               var13.setClientResponsibleForAcknowledge(true);
               var8.addPushEntry(var13);
            } else {
               var5 = var5;
            }
         }
      }

      return var5;
   }

   private long getMaxSequenceNumber(JMSPushRequest var1, long var2) {
      long var4 = var2;

      for(JMSPushRequest var6 = var1; var6 != null; var6 = (JMSPushRequest)var6.getNext()) {
         for(JMSPushEntry var7 = var6.getFirstPushEntry(); var7 != null; var7 = var7.getNext()) {
            if (var4 < var7.getFrontEndSequenceNumber()) {
               var4 = var7.getFrontEndSequenceNumber();
            }
         }
      }

      return var4;
   }

   public void acknowledgeAsync(WLAcknowledgeInfo var1, CompletionListener var2) {
      try {
         this.acknowledge(var1);
         var2.onCompletion((Object)null);
      } catch (Throwable var4) {
         var2.onException(var4);
      }

   }

   public void sendAsync(MessageProducer var1, Message var2, CompletionListener var3) {
      try {
         var1.send(var2);
      } catch (Throwable var5) {
         var3.onException(var5);
      }

   }

   public void sendAsync(WLMessageProducer var1, Message var2, int var3, int var4, long var5, CompletionListener var7) {
      try {
         var1.send(var2, var3, var4, var5);
      } catch (Throwable var9) {
         var7.onException(var9);
      }

   }

   public void sendAsync(WLMessageProducer var1, javax.jms.Destination var2, Message var3, CompletionListener var4) {
      try {
         var1.send(var2, var3);
      } catch (Throwable var6) {
         var4.onException(var6);
      }

   }

   public void sendAsync(WLMessageProducer var1, javax.jms.Destination var2, Message var3, int var4, int var5, long var6, CompletionListener var8) {
      try {
         var1.send(var2, var3, var4, var5, var6);
      } catch (Throwable var10) {
         var8.onException(var10);
      }

   }

   public void receiveAsync(MessageConsumer var1, CompletionListener var2) {
      ((WLConsumerImpl)var1).receiveAsync(var2);
   }

   public void receiveAsync(MessageConsumer var1, long var2, CompletionListener var4) {
      ((WLConsumerImpl)var1).receiveAsync(var2, var4);
   }

   public void receiveNoWaitAsync(MessageConsumer var1, CompletionListener var2) {
      ((WLConsumerImpl)var1).receiveNoWaitAsync(var2);
   }

   static {
      try {
         IGNORE_JmsAsyncQueue = System.getProperty("weblogic.jms.IGNORE_JmsAsyncQueue", "false").equalsIgnoreCase("true");
      } catch (SecurityException var1) {
      }

      ASYNC_RESERVED_MSG = new TextMessageImpl("internal ASYNC_RESERVED_MSG");
   }

   private class JMSConsumerReceiveResponsePrivate extends JMSConsumerReceiveResponse {
      static final long serialVersionUID = -7380653133580038280L;
      private int deliveryCount;

      JMSConsumerReceiveResponsePrivate(MessageImpl var2, long var3, boolean var5, int var6) {
         super(var2, var3, var5);
         this.deliveryCount = var6;
      }

      int getDeliveryCount() {
         return this.deliveryCount;
      }
   }

   private class UseForRunnable implements Runnable {
      private JMSSession session;

      protected UseForRunnable(JMSSession var2) {
         this.session = var2;
      }

      public void run() {
         this.session.executeMessage();
      }
   }

   static final class PendingWTMessage {
      final ConsumerInternal consumer;
      final long sequenceNumber;
      PendingWTMessage next;

      PendingWTMessage(ConsumerInternal var1, long var2) {
         this.consumer = var1;
         this.sequenceNumber = var2;
      }
   }

   static final class UnackedMessage {
      final ConsumerInternal consumer;
      final long sequenceNumber;
      UnackedMessage next;

      UnackedMessage(ConsumerInternal var1, long var2) {
         this.consumer = var1;
         this.sequenceNumber = var2;
      }
   }
}
