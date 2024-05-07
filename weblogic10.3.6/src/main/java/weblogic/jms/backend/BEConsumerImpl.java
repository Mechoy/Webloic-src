package weblogic.jms.backend;

import java.security.AccessController;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.OpenDataException;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NamingException;
import javax.transaction.RollbackException;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.diagnostics.instrumentation.PointcutHandlingInfo;
import weblogic.diagnostics.instrumentation.ValueHandlingInfo;
import weblogic.jms.JMSService;
import weblogic.jms.common.ConsumerReconnectInfo;
import weblogic.jms.common.DispatcherCompletionListener;
import weblogic.jms.common.DurableSubscription;
import weblogic.jms.common.JMSConsumerReceiveResponse;
import weblogic.jms.common.JMSConsumerSetListenerResponse;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSDiagnosticImageSource;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSMessageEventLogListener;
import weblogic.jms.common.JMSMessageId;
import weblogic.jms.common.JMSMessageLogHelper;
import weblogic.jms.common.JMSPushEntry;
import weblogic.jms.common.JMSSQLExpression;
import weblogic.jms.common.JMSSecurityException;
import weblogic.jms.common.JMSSecurityHelper;
import weblogic.jms.common.MessageImpl;
import weblogic.jms.common.NonDurableSubscription;
import weblogic.jms.common.SingularAggregatableManager;
import weblogic.jms.common.Subscription;
import weblogic.jms.common.TimedSecurityParticipant;
import weblogic.jms.dispatcher.InvocableManagerDelegate;
import weblogic.jms.dispatcher.JMSDispatcherManager;
import weblogic.jms.dispatcher.VoidResponse;
import weblogic.jms.extensions.ConsumerClosedException;
import weblogic.jms.extensions.ConsumerInfo;
import weblogic.logging.jms.JMSMessageLogger;
import weblogic.management.ManagementException;
import weblogic.messaging.ID;
import weblogic.messaging.common.PrivilegedActionUtilities;
import weblogic.messaging.dispatcher.InvocableMonitor;
import weblogic.messaging.dispatcher.Request;
import weblogic.messaging.kernel.Event;
import weblogic.messaging.kernel.EventListener;
import weblogic.messaging.kernel.Expression;
import weblogic.messaging.kernel.KernelException;
import weblogic.messaging.kernel.KernelRequest;
import weblogic.messaging.kernel.ListenRequest;
import weblogic.messaging.kernel.Listener;
import weblogic.messaging.kernel.MessageAddEvent;
import weblogic.messaging.kernel.MessageElement;
import weblogic.messaging.kernel.MessageEvent;
import weblogic.messaging.kernel.MessageExpirationEvent;
import weblogic.messaging.kernel.MessageReceiveEvent;
import weblogic.messaging.kernel.MessageRedeliveryLimitEvent;
import weblogic.messaging.kernel.MessageRemoveEvent;
import weblogic.messaging.kernel.MessageSendEvent;
import weblogic.messaging.kernel.Queue;
import weblogic.messaging.kernel.ReceiveRequest;
import weblogic.messaging.kernel.RedeliveryParameters;
import weblogic.messaging.runtime.DiagnosticImageTimeoutException;
import weblogic.security.WLSPrincipals;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.store.PersistentHandle;
import weblogic.transaction.TransactionHelper;
import weblogic.transaction.internal.TransactionImpl;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

public class BEConsumerImpl extends BEDeliveryList implements Listener, BEConsumerCommon, TimedSecurityParticipant, RedeliveryParameters, EventListener, JMSMessageEventLogListener {
   protected JMSID id;
   private InvocableMonitor invocableMonitor;
   private static final AuthenticatedSubject KERNEL_ID;
   private static final int LOG_CONSUMERCREATE = 1;
   private static final int LOG_CONSUMERDESTROY = 2;
   private String name;
   private String clientId;
   private String subscriptionName;
   private boolean durableSubscriber;
   private boolean multicastSubscriber;
   private boolean supportsClientResponsible;
   private boolean kernelAutoAcknowledge;
   private BESessionImpl session;
   protected BEDestinationImpl destination;
   protected Queue queue;
   volatile Queue unsubscribeQueue;
   protected ListenRequest listenRequest;
   private ReceiveRequest receiveRequest;
   private Expression filterExpression;
   private String selector;
   private boolean noLocal;
   private PersistentHandle persistentHandle;
   private long redeliveryDelay;
   private BEDurableSubscriberRuntimeMBeanImpl runtimeMBean;
   private int state = 1;
   protected int windowSize;
   private int pendingWindowSpace;
   private int unackedMessageCount;
   private final Object stateLock = new Object();
   private String subscriberUserInfo = null;
   private static final String CLIENTID_DELIMITER = "cid_";
   private static final String SUBSCRIPTION_DELIMITER = "_sid_";
   private static final int BLOCKING_RECV_PENDING = 101;
   private static final int BLOCKING_RECV_COMPLETE = 102;
   protected static final int STATE_STOPPED = 1;
   protected static final int STATE_HAS_LISTENER = 4;
   protected static final int STATE_BLOCKING_RECV = 8;
   protected static final int STATE_CLOSED = 16;
   protected static final int STATE_READY_FOR_PUSH = 4;
   protected static final int FLAG_CLIENT_MAY_BE_RESPONSIBLE = 4;
   protected static final int FLAG_DURABLE = 8;
   protected static final int FLAG_MULTICAST_CONSUMER = 16;
   public int messageAddEventLogCount;
   public int messageSendEventLogCount;
   public int messageRemoveEventLogCount;
   public int messageReceiveEventLogCount;
   public int messageExpirationEventLogCount;
   public int messageRedeliveryLimitEventLogCount;
   private boolean isRegisteredForSecurity = false;
   private AuthenticatedSubject authenticatedSubject = null;
   private static final boolean debug = false;
   private ConsumerReconnectInfo consumerReconnectInfo;
   private int clientIdPolicy = 0;
   private int subscriptionSharingPolicy = 0;
   private Subscription subscription;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;
   static final long serialVersionUID = 6093048509284814987L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.jms.backend.BEConsumerImpl");
   public static final DelegatingMonitor _WLDF$INST_FLD_JMS_Diagnostic_Volume_Before_Medium;
   public static final JoinPoint _WLDF$INST_JPFLD_0;

   protected BEConsumerImpl(BackEnd var1) {
      super(var1);
   }

   BEConsumerImpl(BESessionImpl var1, BEDestinationImpl var2, Queue var3, Expression var4, int var5, boolean var6, BEConsumerCreateRequest var7) throws JMSException {
      super(var2.getBackEnd());
      var7.setName((String)null);
      var7.setClientId((String)null);
      var7.setNoLocal(false);
      this.init(var1, var2, var3, var4, var5, var6, var7);
   }

   BEConsumerImpl(BESessionImpl var1, BEDestinationImpl var2, Queue var3, int var4, boolean var5, BEConsumerCreateRequest var6) throws JMSException {
      super(var2.getBackEnd());
      this.init(var1, var2, var3, (Expression)null, var4, var5, var6);
   }

   private boolean isWlsKernelId() {
      return WLSPrincipals.isKernelUsername(JMSSecurityHelper.getSimpleAuthenticatedName());
   }

   protected void init(BESessionImpl var1, BEDestinationImpl var2, Queue var3, Expression var4, int var5, boolean var6, BEConsumerCreateRequest var7) throws JMSException {
      if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
         JMSDebug.JMSBackEnd.debug("Initialize BEConsumer: destination=" + var2 + " subqueue = " + var3.getName() + " clientId =" + var7.getClientId() + " client Id policy = " + var7.getClientIdPolicy() + " selector=" + this.selector + " noLocal = " + this.noLocal);
      }

      this.authenticatedSubject = JMSSecurityHelper.getCurrentSubject();
      if (!this.isWlsKernelId()) {
         var2.getJMSDestinationSecurity().checkReceivePermission(JMSSecurityHelper.getCurrentSubject());
         this.checkSecurityRegistration(var2);
      }

      this.destination = var2;
      this.unsubscribeQueue = this.queue = var3;
      this.closeStaleConsumerSession(var7);
      this.id = var7.getConsumerId();
      this.session = var1;
      this.invocableMonitor = JMSService.getJMSService().getInvocableMonitor();
      this.setRedeliveryDelay(var7.getRedeliveryDelay());
      this.filterExpression = var4;
      this.selector = var7.getSelector();
      this.noLocal = var7.getNoLocal();
      this.subscriptionSharingPolicy = var7.getSubscriptionSharingPolicy();
      this.supportsClientResponsible = (var5 & 4) != 0;
      this.setWindowSize(var7.getMessagesMaximum());
      this.clientId = var7.getClientId();
      this.clientIdPolicy = var7.getClientIdPolicy();
      if (this.clientId != null && var7.getName() != null) {
         this.durableSubscriber = true;
         this.subscriptionName = var7.getName();
         this.name = clientIdPlusName(this.clientId, var7.getName(), this.clientIdPolicy, var2.getName(), var2.getBackEnd().getName());
         this.registerDurableSubscription(var6);
      } else if (this.clientId != null) {
         this.name = this.clientId;
         this.addNonDurableSubscription();
      } else {
         this.name = null;
         this.subscriptionName = var7.getName();
      }

      if ((var5 & 16) != 0) {
         this.multicastSubscriber = true;
      }

      this.subscriberUserInfo = JMSMessageLogHelper.addSubscriberInfo(this) + "#" + (var7.getSubject() != null ? var7.getSubject() : JMSSecurityHelper.getSimpleAuthenticatedName());
      if (!var6) {
         this.logEvent(1);
      }

   }

   private void closeStaleConsumerSession(BEConsumerCreateRequest var1) throws JMSException {
      if (var1.getConsumerReconnectInfo() != null && var1.getConsumerReconnectInfo().getInvokableID() != null) {
         BEConsumerImpl var2;
         try {
            var2 = (BEConsumerImpl)InvocableManagerDelegate.delegate.invocableFind(17, var1.getConsumerReconnectInfo().getInvokableID());
         } catch (JMSException var6) {
            return;
         }

         if (var2 != null && !var2.invalidateReconnectingConsumer(var1)) {
            JMSMessageId var3 = var1.getConsumerReconnectInfo().getLastAckMsgId();
            if (var3 != null) {
               long var4 = var2.getSession().sequenceFromMsgId(var3);
               if (var4 != Long.MAX_VALUE) {
                  if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
                     JMSDebug.JMSBackEnd.debug("refreshed consumer ack stale " + var4);
                  }

                  JMSDispatcherManager.getLocalDispatcher().dispatchSyncNoTran(new BESessionAcknowledgeRequest(var2.getSession().getJMSID(), var4));
               }
            }

            if (this.destination instanceof BETopicImpl && var1.getPersistentHandle() == null && var1.getName() == null) {
               if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
                  JMSDebug.JMSBackEnd.debug("transplant stale non-durable " + var2);
               }

               var2.unsubscribeQueue = this.queue;
               this.queue = var2.queue;
            } else {
               if (var1.getConsumerReconnectInfo().getLastExposedMsgId() != null) {
                  var2.getSession().close(var1.getConsumerReconnectInfo().getLastExposedMsgId());
               } else {
                  if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
                     JMSDebug.JMSBackEnd.debug("closing stale session " + var2.getSession().getJMSID());
                  }

                  var2.getSession().close();
               }

            }
         }
      }
   }

   synchronized ConsumerReconnectInfo registerConsumerReconnectInfo(ConsumerReconnectInfo var1) {
      if (var1 == null) {
         return this.consumerReconnectInfo = null;
      } else {
         var1 = var1.getClone();
         var1.setInvokableID(this.getJMSID());
         var1.setServerDestId(this.destination.getJMSID());
         var1.setServerDispatcherId(JMSDispatcherManager.getLocalDispatcher().getId());
         this.consumerReconnectInfo = var1;
         return var1.getClone();
      }
   }

   private boolean invalidateReconnectingConsumer(BEConsumerCreateRequest var1) {
      ConsumerReconnectInfo var2 = var1.getConsumerReconnectInfo();
      if (this.consumerReconnectInfo == null) {
         return true;
      } else {
         return !this.consumerReconnectInfo.getClientJMSID().equals(var2.getClientJMSID()) || !this.destination.getJMSID().equals(var2.getServerDestId()) || !this.consumerReconnectInfo.getClientDispatcherId().equals(var2.getClientDispatcherId()) || !JMSDispatcherManager.getLocalDispatcher().getId().equals(var2.getServerDispatcherId());
      }
   }

   synchronized long getDelayServerClose() {
      return this.consumerReconnectInfo == null ? 0L : this.consumerReconnectInfo.getDelayServerClose();
   }

   private void logEvent(int var1) {
      if (_WLDF$INST_FLD_JMS_Diagnostic_Volume_Before_Medium.isEnabledAndNotDyeFiltered()) {
         Object[] var3 = null;
         if (_WLDF$INST_FLD_JMS_Diagnostic_Volume_Before_Medium.isArgumentsCaptureNeeded()) {
            var3 = new Object[]{this, InstrumentationSupport.convertToObject(var1)};
         }

         DynamicJoinPoint var10000 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var3, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_JMS_Diagnostic_Volume_Before_Medium;
         InstrumentationSupport.process(var10000, var10001, var10001.getActions());
      }

      if (var1 == 1) {
         if (this.destination.isMessageLoggingEnabled() && (this.destination instanceof BEQueueImpl || this.isDurable() || JMSService.getJMSService().shouldMessageLogNonDurableSubscriber())) {
            JMSMessageLogHelper.logMessageEvent(this, new MessageConsumerCreationEventImpl((String)null, this.queue, this.selector, this.subscriberUserInfo));
         }
      } else if (var1 == 2 && this.destination.isMessageLoggingEnabled() && (this.destination instanceof BEQueueImpl || this.isDurable() || JMSService.getJMSService().shouldMessageLogNonDurableSubscriber())) {
         JMSMessageLogHelper.logMessageEvent(this, new MessageConsumerDestroyEventImpl((String)null, this.queue, this.subscriberUserInfo));
      }

   }

   public static String clientIdPlusName(String var0, String var1) {
      return clientIdPlusName(var0, var1, 0, (String)null, (String)null);
   }

   public static String clientIdPlusName(String var0, String var1, int var2, String var3, String var4) {
      StringBuffer var5 = new StringBuffer();
      var5.append("cid_");
      var5.append(var0);
      var5.append("_sid_");
      var5.append(var1);
      if (var2 == 1 && var3 != null) {
         var5.append("@" + var3 + "@" + var4);
      }

      return var5.toString();
   }

   public static String JNDINameForSubscription(String var0) {
      StringBuffer var1 = new StringBuffer();
      var1.append("weblogic.jms.internal.subscription");
      var1.append(".");
      var1.append(var0);
      return var1.toString();
   }

   Queue getKernelQueue() {
      return this.queue;
   }

   public Queue getUnsubscribeQueue() {
      return this.unsubscribeQueue;
   }

   synchronized Subscription getSubscription() {
      return this.subscription;
   }

   synchronized void setSubscription(Subscription var1) {
      this.subscription = var1;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public String getSubscriptionName() {
      return this.subscriptionName;
   }

   public String getClientID() {
      return this.clientId;
   }

   public int getClientIdPolicy() {
      return this.clientIdPolicy;
   }

   public BEDestinationImpl getDestination() {
      return this.destination;
   }

   public String getSelector() {
      return this.selector;
   }

   public boolean getNoLocal() {
      return this.noLocal;
   }

   PersistentHandle getPersistentHandle() {
      return this.persistentHandle;
   }

   void setPersistentHandle(PersistentHandle var1) {
      this.persistentHandle = var1;
   }

   boolean isKernelAutoAcknowledge() {
      return this.kernelAutoAcknowledge;
   }

   public int getSubscriptionSharingPolicy() {
      return this.subscriptionSharingPolicy;
   }

   public long getMessagesUnackedCount() {
      return this.queue == null ? 0L : (long)this.queue.getStatistics().getMessagesPending();
   }

   public long getMessagesReceivedCount() {
      return this.queue == null ? 0L : (long)this.queue.getStatistics().getMessagesReceived();
   }

   public long getBytesUnackedCount() {
      return this.queue == null ? 0L : this.queue.getStatistics().getBytesPending();
   }

   public long getBytesCurrentCount() {
      return this.queue == null ? 0L : this.queue.getStatistics().getBytesCurrent() - this.queue.getStatistics().getBytesPending();
   }

   public long getLastMessagesReceivedTime() {
      return this.queue == null ? 0L : this.queue.getLastMessagesReceivedTime();
   }

   public int getSize() {
      return this.queue == null ? 0 : this.queue.getStatistics().getMessagesCurrent() - this.queue.getStatistics().getMessagesPending();
   }

   public int getHighSize() {
      return this.queue == null ? 0 : this.queue.getStatistics().getMessagesHigh();
   }

   private void setWindowSize(int var1) {
      if (var1 < 0) {
         this.windowSize = Integer.MAX_VALUE;
      } else {
         this.windowSize = var1;
      }

   }

   public void close(long var1) throws JMSException {
      if (!this.checkStateFlag(16)) {
         this.stop();
         if (this.session != null) {
            this.session.removeConsumer(this, var1);
         }

         this.closeInternal();
      }

   }

   protected void closeWithError(String var1) throws JMSException {
      if (!this.checkStateFlag(16)) {
         this.stop();

         try {
            if (this.session != null) {
               ConsumerClosedException var2 = new ConsumerClosedException((MessageConsumer)null, var1);
               this.session.removeConsumerWithError(this, 0L, var2);
            }
         } finally {
            this.closeInternal();
         }
      }

   }

   private void closeInternal() throws JMSException {
      if (this.checkStateFlag(8) && this.receiveRequest != null) {
         this.receiveRequest.cancel();
      }

      if (!this.durableSubscriber) {
         this.removeConsumer(false);
         this.logEvent(2);
      } else {
         DurableSubscription var1 = this.destination.getBackEnd().getDurableSubscription(this.name);
         if (var1 != null) {
            boolean var2 = false;
            synchronized(var1) {
               var1.removeSubscriber(this.getJMSID());
               if (var1.getSubscribersCount() > 0) {
                  var2 = true;
               }
            }

            if (var2) {
               ((BETopicImpl)this.destination).removeConsumer(this, false, true);
            }
         }

         synchronized(this.stateLock) {
            this.clearStateFlag(12);
            this.setStateFlag(16);
         }
      }

   }

   private void removeConsumer(boolean var1) throws JMSException {
      this.setStateFlag(16);
      this.destination.removeConsumer(this, var1);
   }

   public JMSID getJMSID() {
      return this.id;
   }

   public ID getId() {
      return this.getJMSID();
   }

   public InvocableMonitor getInvocableMonitor() {
      return this.invocableMonitor;
   }

   public BESessionImpl getSession() {
      return this.session;
   }

   public boolean isDurable() {
      return this.durableSubscriber;
   }

   boolean isMulticastSubscriber() {
      return this.multicastSubscriber;
   }

   public boolean isActive() {
      return this.checkStateFlag(12);
   }

   public boolean isUsed() {
      return !this.checkStateFlag(16);
   }

   public BEDurableSubscriberRuntimeMBeanImpl getDurableSubscriberMbean() {
      return this.runtimeMBean;
   }

   public long getRedeliveryDelay() {
      long var1 = this.destination.getDirtyRedeliveryDelayOverride();
      return var1 >= 0L ? var1 : this.redeliveryDelay;
   }

   synchronized void setRedeliveryDelay(long var1) {
      if (var1 < 0L) {
         this.redeliveryDelay = 0L;
      } else {
         this.redeliveryDelay = var1;
      }

   }

   protected void setStateFlag(int var1) {
      synchronized(this.stateLock) {
         this.state |= var1;
      }
   }

   boolean hasListener() {
      synchronized(this.stateLock) {
         return (this.state & 4) != 0;
      }
   }

   private void clearStateFlag(int var1) {
      synchronized(this.stateLock) {
         this.state &= ~var1;
      }
   }

   private boolean checkStateFlag(int var1) {
      synchronized(this.stateLock) {
         return (this.state & var1) != 0;
      }
   }

   private boolean isReadyForPush() {
      synchronized(this.stateLock) {
         return this.state == 4;
      }
   }

   void adjustUnackedCount(int var1) {
      synchronized(this.stateLock) {
         this.unackedMessageCount += var1;
      }
   }

   private void adjustUnackedCountTransactionally(Transaction var1, int var2) throws JMSException {
      CountAdjuster var3 = new CountAdjuster(-var2, false, false);

      try {
         var1.registerSynchronization(var3);
      } catch (RollbackException var5) {
         var3.afterCompletion(1);
      } catch (IllegalStateException var6) {
         var3.afterCompletion(1);
      } catch (SystemException var7) {
         throw new weblogic.jms.common.JMSException(var7);
      }

   }

   private void addNonDurableSubscription() {
      NonDurableSubscription var1 = new NonDurableSubscription(this.clientId, this.destination.getDestinationImpl(), this.selector, this.noLocal, this.clientIdPolicy, this.subscriptionSharingPolicy, this.queue.getName());
      if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
         JMSDebug.JMSBackEnd.debug("Adding non-durable sub: clientId = " + this.clientId + " destination = " + this.destination.getDestinationImpl() + " selector = " + this.selector + " noLocal =  " + this.noLocal + " clientIdPolicy = " + this.clientIdPolicy + " subscriptionSharingPolicy = " + this.subscriptionSharingPolicy + " subQueueName = " + this.queue.getName());
      }

      this.setSubscription(((BETopicImpl)this.destination).addSharableNonDurableSubscriber(var1));
   }

   private void registerDurableSubscription(boolean var1) throws JMSException {
      DurableSubscription var2 = null;
      boolean var3 = false;

      while(true) {
         synchronized(this.destination.getBackEnd().getDurableSubscriptionsMap()) {
            var2 = this.destination.getBackEnd().getDurableSubscription(this.name);
            if (var2 == null) {
               var2 = new DurableSubscription(this.name, this.destination.getDestinationImpl(), this.selector, this.noLocal, this.clientIdPolicy, this.subscriptionSharingPolicy);
               var3 = true;
               this.destination.getBackEnd().addDurableSubscription(this.name, var2);
            }
         }

         synchronized(var2) {
            if (var3) {
               this.setSubscription(var2);
               this.createDurableSubscription(var2, var1);
               var2.addSubscriber(this);
               return;
            }

            if (var2.isPending()) {
               var2.incrementWaits();

               try {
                  var2.wait();
               } catch (InterruptedException var7) {
               }

               var2.decrementWaits();
            }

            if (!var2.isStale()) {
               if (this.subscriptionSharingPolicy == 0) {
                  throw new JMSException("Durable subscription " + this.subscriptionName + " is in use and cannot be shared");
               }

               this.persistentHandle = var2.getConsumer().getPersistentHandle();
               var2.addSubscriber(this);
               this.runtimeMBean = var2.getConsumer().getDurableSubscriberMbean();
               this.setSubscription(var2);
               return;
            }
         }
      }
   }

   private void createDurableSubscription(DurableSubscription var1, boolean var2) throws JMSException {
      boolean var3 = false;
      boolean var4 = false;
      JMSSQLExpression var5 = null;
      var5 = new JMSSQLExpression(this.selector, this.noLocal, this.session == null ? null : this.session.getConnection().getJMSID(), this.clientId, this.clientIdPolicy);
      if (!var2 && this.backEnd.isStoreEnabled()) {
         this.persistentHandle = this.backEnd.getDurableSubscriptionStore().createSubscription(this.destination.getName(), this.clientId, this.clientIdPolicy, this.subscriptionName, var5);
      }

      if (this.clientIdPolicy == 0) {
         try {
            SingularAggregatableManager var6 = SingularAggregatableManager.findOrCreate();
            String var7;
            if ((var7 = var6.singularBind(JNDINameForSubscription(this.name), var1)) != null) {
               throw new NameAlreadyBoundException(var7);
            }

            var3 = true;
         } catch (NamingException var15) {
            throw new weblogic.jms.common.JMSException("Error creating durable subscriber", var15);
         }
      }

      SecurityServiceManager.pushSubject(KERNEL_ID, KERNEL_ID);

      try {
         this.runtimeMBean = new BEDurableSubscriberRuntimeMBeanImpl(this.getDurableSubscriptionRuntimeMBeanName(this.clientId, this.subscriptionName, this.destination), this.destination, this);
      } catch (ManagementException var13) {
         throw new weblogic.jms.common.JMSException("Error registering durable subscriber RuntimeMBean", var13);
      } finally {
         SecurityServiceManager.popSubject(KERNEL_ID);
      }

      this.runtimeMBean.setMessageManagementDelegate(new BEMessageManagementImpl(this.name, this.queue, this.destination, this.runtimeMBean));
   }

   synchronized void restore(BEConsumerCreateRequest var1, BESessionImpl var2, boolean var3) throws JMSException {
      if (!this.checkStateFlag(1)) {
         throw new AssertionError("Restarting a consumer that was not stopped");
      } else {
         this.id = var1.getConsumerId();
         this.session = var2;
         this.subscriptionSharingPolicy = var1.getSubscriptionSharingPolicy();
         this.checkSecurityRegistration(this.destination);
         this.checkPermission(this.destination, true, true);
         this.clearStateFlag(16);
         DurableSubscription var4 = this.destination.getBackEnd().getDurableSubscription(this.name);
         if (var4 != null) {
            var4.addSubscriber(this);
         }

         this.setWindowSize(var1.getMessagesMaximum());
         this.subscriberUserInfo = JMSMessageLogHelper.addSubscriberInfo(this) + "#" + (var1.getSubject() != null ? var1.getSubject() : JMSSecurityHelper.getSimpleAuthenticatedName());
         if (var3) {
            this.start();
         }

      }
   }

   private void checkSecurityRegistration(BEDestinationImpl var1) {
      synchronized(this.stateLock) {
         if (this.isRegisteredForSecurity || this.isWlsKernelId()) {
            return;
         }

         this.isRegisteredForSecurity = true;
      }

      JMSService.getJMSService().registerSecurityParticipant(var1.getJMSDestinationSecurity().getJMSResourceForReceive(), this);
   }

   public void delete(boolean var1, boolean var2) throws JMSException {
      this.delete(var1, var2, true);
   }

   void delete(boolean var1, boolean var2, boolean var3) throws JMSException {
      if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
         JMSDebug.JMSBackEnd.debug("Deleting a durable subiscriber " + this);
      }

      if (this.checkStateFlag(12)) {
         throw new weblogic.jms.common.JMSException("Active topicSubscriber is using this subscription right now");
      } else {
         synchronized(this.stateLock) {
            if (var2 && this.unackedMessageCount > 0) {
               throw new weblogic.jms.common.JMSException("Subscription " + this.name + " in use, uncommitted/unacknowleged messages " + this.unackedMessageCount);
            }
         }

         this.cleanupDurableSubscription(true, var1, true, false, var3);
      }
   }

   public void closeDurableSubscription() throws JMSException {
      try {
         this.close(0L);
      } finally {
         this.cleanupDurableSubscription(true, false, false, false, true);
      }

   }

   public void cleanupDurableSubscription(boolean var1, boolean var2, boolean var3, boolean var4, boolean var5) throws JMSException {
      if (!this.durableSubscriber) {
         throw new weblogic.jms.common.JMSException("Not a durable subscription");
      } else {
         DurableSubscription var6 = null;
         synchronized(this.destination.getBackEnd().getDurableSubscriptionsMap()) {
            if (!var5) {
               var6 = this.destination.getBackEnd().getDurableSubscription(this.name);
               if (var6 == null) {
                  throw new weblogic.jms.common.JMSException("Subscription not found");
               }

               synchronized(var6) {
                  if (var6.isPending() || var6.getSubscribersCount() > 0) {
                     throw new weblogic.jms.common.JMSException("Subscription " + this.name + " is in use");
                  }

                  if (var6.isStale()) {
                     throw new weblogic.jms.common.JMSException("Subscription " + this.name + " is not found");
                  }

                  var6.setPending(true);
               }
            } else {
               while(true) {
                  var6 = this.destination.getBackEnd().getDurableSubscription(this.name);
                  if (var6 == null) {
                     return;
                  }

                  synchronized(var6) {
                     if (!var6.isStale()) {
                        if (var6.isPending()) {
                           var6.incrementWaits();

                           try {
                              var6.wait();
                           } catch (InterruptedException var13) {
                           }

                           var6.decrementWaits();
                           continue;
                        }

                        if (var6.isStale()) {
                           return;
                        }

                        var6.setPending(true);
                        break;
                     }
                  }

                  return;
               }
            }
         }

         this.doDurableSubscriptionCleanup(var6, var1, var2, var3, var4);
         this.logEvent(2);
      }
   }

   void doDurableSubscriptionCleanup(DurableSubscription var1, boolean var2, boolean var3, boolean var4, boolean var5) throws JMSException {
      try {
         if (!var3 && this.runtimeMBean != null) {
            BEDurableSubscriberRuntimeMBeanImpl var6 = this.runtimeMBean;
            this.runtimeMBean = null;
            PrivilegedActionUtilities.unregister(var6, KERNEL_ID);
         }
      } catch (ManagementException var184) {
         Object var7 = var184.getNestedException();
         if (var7 == null) {
            var7 = var184;
         }

         throw new weblogic.jms.common.JMSException("Error closing durable subscription. " + ((Throwable)var7).getMessage(), var184);
      } finally {
         try {
            if (var4) {
               this.removeConsumer(var5);
            }
         } catch (JMSException var182) {
            throw var182;
         } finally {
            try {
               if (var2 && this.clientIdPolicy == 0) {
                  SingularAggregatableManager var13 = SingularAggregatableManager.findOrCreate();
                  var13.singularUnbind(JNDINameForSubscription(this.name));
               }
            } catch (JMSException var180) {
               throw var180;
            } finally {
               synchronized(this.destination.getBackEnd().getDurableSubscriptionsMap()) {
                  this.destination.getBackEnd().removeDurableSubscription(this.name);
                  synchronized(var1) {
                     var1.setStale(true);
                     if (var1.isPending()) {
                        var1.setPending(false);
                        if (var1.hasWaits()) {
                           var1.notifyAll();
                        }
                     }
                  }

               }
            }

         }

      }

   }

   protected boolean allowsImplicitAcknowledge() {
      if (this.session == null) {
         return false;
      } else if (!this.supportsClientResponsible) {
         return false;
      } else if (!this.session.allowsImplicitAcknowledge()) {
         return false;
      } else if (this.getRedeliveryDelay() != 0L) {
         return false;
      } else if (this.destination.getDirtyRedeliveryLimit() >= 0 && this.destination.getDirtyRedeliveryLimit() != Integer.MAX_VALUE) {
         return false;
      } else {
         return this.subscriptionSharingPolicy != 1 || this.clientId == null;
      }
   }

   public int invoke(Request var1) throws JMSException {
      switch (var1.getMethodId()) {
         case 10001:
            this.close(((BEConsumerCloseRequest)var1).getLastSequenceNumber());
            break;
         case 10513:
            this.destination.checkShutdownOrSuspendedNeedLock("increment consumer window");
            this.incrementWindowCurrent((BEConsumerIncrementWindowCurrentRequest)var1);
            break;
         case 10769:
            var1.setResult(new BEConsumerIsActiveResponse(this.isActive()));
            var1.setState(Integer.MAX_VALUE);
            return Integer.MAX_VALUE;
         case 11025:
            this.destination.checkShutdownOrSuspendedNeedLock("receive message");
            return this.receive((BEConsumerReceiveRequest)var1);
         case 11281:
            return this.setListener((BEConsumerSetListenerRequest)var1);
         case 17169:
            throw new AssertionError("Not implemented");
         default:
            throw new AssertionError("No such method " + var1.getMethodId());
      }

      var1.setResult(VoidResponse.THE_ONE);
      var1.setState(Integer.MAX_VALUE);
      return Integer.MAX_VALUE;
   }

   private boolean blockingReceiveStart(BEConsumerReceiveRequest var1) throws JMSException {
      var1.setTransaction((TransactionImpl)TransactionHelper.getTransactionHelper().getTransaction());
      boolean var2;
      synchronized(this.stateLock) {
         if (this.checkStateFlag(12)) {
            throw new weblogic.jms.common.JMSException("Invalid blocking receive when another receive is in progress");
         }

         this.setStateFlag(8);
         var2 = !this.checkStateFlag(1);
      }

      long var3;
      if (var1.getTimeout() == 9223372036854775806L) {
         var3 = 0L;
      } else if (var1.getTimeout() == Long.MAX_VALUE) {
         var3 = Long.MAX_VALUE;
      } else {
         var3 = var1.getTimeout();
      }

      if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
         JMSDebug.JMSBackEnd.debug("Starting blocking receive for consumer");
      }

      try {
         this.receiveRequest = this.queue.receive(this.filterExpression, 1, this.allowsImplicitAcknowledge(), this.recoveryUnit(var1.getTransaction()), var3, var2, this.subscriberUserInfo);
      } catch (KernelException var8) {
         throw new weblogic.jms.common.JMSException(var8);
      }

      var1.setState(101);
      var1.setKernelRequest(this.receiveRequest);
      synchronized(this.receiveRequest) {
         if (!this.receiveRequest.hasResult()) {
            var1.needOutsideResult();
            var1.setWorkManager(this.getBackEnd().getWorkManager());
            this.receiveRequest.addListener(new DispatcherCompletionListener(var1), this.getBackEnd().getWorkManager());
            return true;
         } else {
            return false;
         }
      }
   }

   private Object recoveryUnit(TransactionImpl var1) {
      if (var1 != null) {
         return var1;
      } else {
         return this.session == null ? this : this.session.getRecoveryUnit();
      }
   }

   private boolean blockingReceiveProcessMessage(BEConsumerReceiveRequest var1) throws JMSException {
      List var2;
      try {
         var2 = (List)var1.getKernelRequest().getResult();
      } catch (KernelException var15) {
         this.clearStateFlag(8);
         throw new weblogic.jms.common.JMSException("Error in blocking receive", var15);
      }

      var1.setState(102);
      var1.setKernelRequest((KernelRequest)null);
      if (var2 != null && !var2.isEmpty()) {
         if (!$assertionsDisabled && var2.size() != 1) {
            throw new AssertionError();
         } else {
            MessageElement var3 = (MessageElement)var2.get(0);
            MessageImpl var4 = (MessageImpl)var3.getMessage();
            var3.setUserData(this);
            boolean var5 = this.allowsImplicitAcknowledge();
            boolean var6 = var5 || var1.isTransactional() || this.session.getAcknowledgeMode() == 4;
            long var7 = this.session.getNextSequenceNumber();
            var3.setUserSequenceNum(var7);
            if (!var6) {
               this.session.addPendingMessage(var3, this);
            }

            if (var3.getDeliveryCount() > 0 || var5) {
               var4 = var4.cloneit();
               var4.setDeliveryCount(var3.getDeliveryCount());
               var4.setClientResponsibleForAcknowledge(var5);
            }

            var1.setResult(new JMSConsumerReceiveResponse(var4, var7, var1.isTransactional()));
            if (var1.isTransactional()) {
               if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
                  JMSDebug.JMSBackEnd.debug("Associating message with transaction");
               }

               try {
                  this.queue.associate(var3, this);
                  this.adjustUnackedCount(1);
                  this.adjustUnackedCountTransactionally(var1.getTransaction(), -1);
               } catch (KernelException var14) {
                  this.clearStateFlag(8);
                  throw new weblogic.jms.common.JMSException(var14);
               }
            } else if (var6) {
               if (!var5) {
                  if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
                     JMSDebug.JMSBackEnd.debug("Acknowledging message");
                  }

                  try {
                     KernelRequest var9 = this.queue.acknowledge(var3);
                     if (var9 != null) {
                        synchronized(var9) {
                           if (!var9.hasResult()) {
                              var1.setKernelRequest(var9);
                              var1.needOutsideResult();
                              var9.addListener(new DispatcherCompletionListener(var1), this.getBackEnd().getWorkManager());
                              return true;
                           }
                        }
                     }
                  } catch (KernelException var13) {
                     this.clearStateFlag(8);
                     throw new weblogic.jms.common.JMSException(var13);
                  }
               }
            } else {
               this.adjustUnackedCount(1);
            }

            return false;
         }
      } else {
         var1.setResult(new JMSConsumerReceiveResponse((MessageImpl)null, 0L, var1.isTransactional()));
         return false;
      }
   }

   private void blockingReceiveComplete(BEConsumerReceiveRequest var1) throws JMSException {
      if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
         JMSDebug.JMSBackEnd.debug("Blocking receive for consumer complete");
      }

      if (var1.getKernelRequest() != null) {
         try {
            var1.getKernelRequest().getResult();
         } catch (KernelException var3) {
            this.clearStateFlag(8);
            throw new weblogic.jms.common.JMSException(var3);
         }
      }

      this.clearStateFlag(8);
      this.receiveRequest = null;
      var1.setState(Integer.MAX_VALUE);
   }

   private int receive(BEConsumerReceiveRequest var1) throws JMSException {
      this.checkSecurityRegistration(this.destination);
      this.checkPermission(this.destination, true, true);

      while(true) {
         if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
            JMSDebug.JMSBackEnd.debug("Blocking receive request: state = " + var1.getState());
         }

         switch (var1.getState()) {
            case 0:
               if (this.blockingReceiveStart(var1)) {
                  return var1.getState();
               }
               break;
            case 101:
               if (this.blockingReceiveProcessMessage(var1)) {
                  return var1.getState();
               }
               break;
            case 102:
               this.blockingReceiveComplete(var1);
               this.checkPermission(this.destination, true, true);
               return Integer.MAX_VALUE;
            default:
               throw new AssertionError("Invalid request state");
         }
      }
   }

   private int setListener(BEConsumerSetListenerRequest var1) throws JMSException {
      if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
         JMSDebug.JMSBackEnd.debug("Consumer got a setListener request: " + var1.getHasListener());
      }

      if (!WLSPrincipals.isAnonymousUsername(JMSSecurityHelper.getSimpleAuthenticatedName())) {
         this.checkSecurityRegistration(this.destination);
      }

      var1.setResult(new JMSConsumerSetListenerResponse(this.session.getSequenceNumber()));
      synchronized(this) {
         if (!(var1.getHasListener() ^ this.checkStateFlag(4))) {
            return Integer.MAX_VALUE;
         }

         if (var1.getHasListener()) {
            this.setStateFlag(4);
            if (this.isReadyForPush()) {
               this.startListening();
            }
         } else {
            this.clearStateFlag(4);
            this.stopListening();
         }
      }

      var1.setState(Integer.MAX_VALUE);
      return Integer.MAX_VALUE;
   }

   private void incrementWindowCurrent(BEConsumerIncrementWindowCurrentRequest var1) throws JMSException {
      if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
         JMSDebug.JMSBackEnd.debug("Consumer got incrementWindowCurrent. Increment = " + var1.getWindowIncrement());
      }

      this.makeWindowSpace(var1.getWindowIncrement(), true);
   }

   private void makeWindowSpace(int var1, boolean var2) throws JMSException {
      int var3 = 0;
      ListenRequest var4 = null;
      synchronized(this) {
         if (this.isReadyForPush() && this.listenRequest != null) {
            int var6 = this.windowSize - this.listenRequest.getCount();
            this.pendingWindowSpace += var1;
            if (var2 || this.pendingWindowSpace >= this.windowSize / 2) {
               var3 = Math.min(this.pendingWindowSpace, var6);
               if (var3 > 0) {
                  var4 = this.listenRequest;
               }

               this.pendingWindowSpace = 0;
            }
         }
      }

      if (var4 != null) {
         try {
            var4.incrementCount(var3);
         } catch (KernelException var8) {
            throw new weblogic.jms.common.JMSException(var8);
         }
      }

   }

   void incrementPendingCount(int var1, boolean var2) throws JMSException {
      this.adjustUnackedCount(-var1);
      this.makeWindowSpace(var1, var2);
   }

   void incrementPendingCountTransactionally(Transaction var1, int var2, boolean var3) throws JMSException {
      CountAdjuster var4 = new CountAdjuster(var2, true, var3);

      try {
         var1.registerSynchronization(var4);
      } catch (RollbackException var6) {
         var4.afterCompletion(1);
      } catch (IllegalStateException var7) {
         var4.afterCompletion(1);
      } catch (SystemException var8) {
         throw new weblogic.jms.common.JMSException(var8);
      }

   }

   JMSPushEntry createPushEntry(MessageElement var1, boolean var2, boolean var3) {
      if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
         JMSDebug.JMSBackEnd.debug("Pushing " + var1.getMessage() + ". implicitAcknowledge = " + var3);
      }

      JMSPushEntry var4 = new JMSPushEntry(this.session.getSequencerId(), this.id, var1.getUserSequenceNum(), 0L, var1.getDeliveryCount(), this.session.getPipelineGeneration());
      var4.setClientResponsibleForAcknowledge(var2);
      return var4;
   }

   void checkPermission(boolean var1) throws JMSSecurityException {
      this.checkPermission(this.destination, var1);
   }

   void checkPermission(boolean var1, boolean var2) throws JMSSecurityException {
      this.checkPermission(this.destination, var1, var2);
   }

   /** @deprecated */
   void checkPermission(BEDestinationImpl var1, boolean var2) throws JMSSecurityException {
      this.checkPermission(var1, var2, true);
   }

   void checkPermission(BEDestinationImpl var1, boolean var2, boolean var3) throws JMSSecurityException {
      if (!this.isWlsKernelId()) {
         AuthenticatedSubject var4 = this.getSubject();
         AuthenticatedSubject var5 = JMSSecurityHelper.getCurrentSubject();
         if (!JMSService.getJMSService().isSecurityCheckerStop()) {
            if (var3 && var4 != var5 && (var4 == null || !var4.equals(var5))) {
               var1.getJMSDestinationSecurity().checkReceivePermission(var5);
               this.setSubject(var5);
            }
         } else {
            try {
               var1.getJMSDestinationSecurity().checkReceivePermission(var4);
            } catch (JMSSecurityException var7) {
               if (var2) {
                  WorkManagerFactory.getInstance().getSystem().schedule(new ConsumerCloseThread());
               }

               throw var7;
            }
         }

      }
   }

   void checkPermission(boolean var1, MessageImpl var2) throws JMSSecurityException {
      this.checkPermission(this.destination, var1, false, var2);
   }

   void checkPermission(boolean var1, boolean var2, MessageImpl var3) throws JMSSecurityException {
      this.checkPermission(this.destination, var1, false, var3);
   }

   void checkPermission(BEDestinationImpl var1, boolean var2, boolean var3, MessageImpl var4) throws JMSSecurityException {
      this.checkPermission(var1, var2, var3);
      if (var4 != null && var4.getJMSType() != null && var4.getJMSType().equals("abcXXX")) {
         if (var2) {
            WorkManagerFactory.getInstance().getSystem().schedule(new ConsumerCloseThread());
         }

         throw new JMSSecurityException("security check simulation negative result");
      }
   }

   public Runnable deliver(ListenRequest var1, List var2) {
      try {
         this.checkPermission(this.destination, true, false);
      } catch (JMSSecurityException var5) {
         return null;
      }

      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         MessageElement var4 = (MessageElement)var3.next();
         var4.setUserData(this);
         var4.setUserSequenceNum(this.session.getNextSequenceNumber());
      }

      return this.session.deliver(var1, var2);
   }

   public Runnable deliver(ListenRequest var1, MessageElement var2) {
      try {
         this.checkPermission(this.destination, true, false);
      } catch (JMSSecurityException var4) {
         return null;
      }

      var2.setUserData(this);
      var2.setUserSequenceNum(this.session.getNextSequenceNumber());
      return this.session.deliver(var1, var2);
   }

   protected void pushMessages(List var1) {
      if (!$assertionsDisabled) {
         throw new AssertionError();
      }
   }

   private WorkManager findPushWorkManager() {
      WorkManager var1 = null;
      if (this.session != null && this.session.getPushWorkManager() != null) {
         var1 = WorkManagerFactory.getInstance().find(this.session.getPushWorkManager());
      }

      if (var1 == null) {
         var1 = this.getBackEnd().getAsyncPushWorkManager();
      }

      return var1;
   }

   synchronized void startListening() throws JMSException {
      if (!this.multicastSubscriber) {
         WorkManager var1 = this.findPushWorkManager();

         try {
            if (this.session != null) {
               this.session.setBackEnd(this.destination.getBackEnd());
               this.session.adjustWindowSize(this.windowSize);
               this.session.setWorkManager(var1);
            }

            this.kernelAutoAcknowledge = this.allowsImplicitAcknowledge();
            this.setWorkManager(var1);
            this.listenRequest = this.queue.listen(this.filterExpression, this.windowSize, this.kernelAutoAcknowledge, this.recoveryUnit((TransactionImpl)null), this, this.destination.getBackEnd().getMultiSender(), this.subscriberUserInfo, var1);
         } catch (KernelException var3) {
            throw new weblogic.jms.common.JMSException(var3);
         }
      }
   }

   synchronized boolean stopListening() {
      if (this.listenRequest != null) {
         if (this.session != null) {
            this.session.adjustWindowSize(-this.windowSize);
         }

         this.listenRequest.stop();
         this.listenRequest = null;
         return true;
      } else {
         return false;
      }
   }

   private synchronized void startInternal(int var1) throws JMSException {
      boolean var2 = this.isReadyForPush();
      this.clearStateFlag(var1);
      if (this.isReadyForPush() && !var2) {
         this.startListening();
      } else if (this.checkStateFlag(8) && this.receiveRequest != null) {
         try {
            this.receiveRequest.start();
         } catch (KernelException var4) {
            throw new weblogic.jms.common.JMSException(var4);
         }
      }

   }

   public void start() throws JMSException {
      if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
         JMSDebug.JMSBackEnd.debug("Got a start request on the consumer");
      }

      this.startInternal(1);
   }

   private synchronized void stopInternal(int var1) {
      this.setStateFlag(var1);
      if (this.checkStateFlag(8) && this.receiveRequest != null) {
         this.receiveRequest.stop();
      } else if (this.listenRequest != null) {
         this.stopListening();
      }

   }

   public void stop() {
      if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
         JMSDebug.JMSBackEnd.debug("Got a stop request on the consumer");
      }

      this.stopInternal(1);
   }

   public void securityLapsed() {
      try {
         this.closeWithError("ERROR: Security has lapsed for this consumer");
      } catch (JMSException var2) {
         System.out.println("ERROR: Could not push security exception to consumer: " + var2);
      }

   }

   public boolean isClosed() {
      return this.checkStateFlag(16);
   }

   public HashSet getAcceptedDestinations() {
      return null;
   }

   public void onEvent(Event var1) {
      if (var1 instanceof MessageSendEvent) {
         this.onMessageEvent((MessageEvent)var1);
      } else if (var1 instanceof MessageAddEvent) {
         this.onMessageEvent((MessageEvent)var1);
      } else if (var1 instanceof MessageReceiveEvent) {
         this.onMessageEvent((MessageEvent)var1);
      } else if (var1 instanceof MessageExpirationEvent) {
         this.onMessageEvent((MessageEvent)var1);
      } else if (var1 instanceof MessageRedeliveryLimitEvent) {
         this.onMessageEvent((MessageEvent)var1);
      } else if (var1 instanceof MessageRemoveEvent) {
         this.onMessageEvent((MessageEvent)var1);
      }

   }

   private final void onMessageEvent(MessageEvent var1) {
      JMSMessageLogHelper.logMessageEvent(this, var1);
   }

   public JMSMessageLogger getJMSMessageLogger() {
      return this.destination.getBackEnd().getJMSMessageLogger();
   }

   public final List getMessageLoggingJMSHeaders() {
      return this.destination.getMessageLoggingJMSHeaders();
   }

   public final List getMessageLoggingUserProperties() {
      return this.destination.getMessageLoggingUserProperties();
   }

   public final String getListenerName() {
      return this.destination.getName();
   }

   private String getDurableSubscriptionRuntimeMBeanName(String var1, String var2, BEDestinationImpl var3) {
      String var4 = var1 + "_" + var2;
      if (this.clientIdPolicy == 1) {
         var4 = var4 + "@" + var3.getName() + "@" + var3.getBackEnd().getName();
      }

      return var4;
   }

   public void dumpRef(JMSDiagnosticImageSource var1, XMLStreamWriter var2) throws XMLStreamException, DiagnosticImageTimeoutException {
      var1.checkTimeout();
      var2.writeAttribute("id", this.id != null ? this.id.toString() : "");
      String var3 = "";
      String var4 = "";
      if (this.session != null) {
         ID var5 = this.session.getId();
         if (var5 != null) {
            var3 = var5.toString();
         }

         BEConnection var6 = this.session.getConnection();
         if (var6 != null) {
            ID var7 = var6.getId();
            if (var7 != null) {
               var4 = var7.toString();
            }
         }
      }

      var2.writeAttribute("sessionID", var3);
      var2.writeAttribute("connectionID", var4);
   }

   protected void dumpCommon(XMLStreamWriter var1) throws XMLStreamException {
      var1.writeAttribute("name", this.name != null ? this.name : "");
      var1.writeAttribute("id", this.id != null ? this.id.toString() : "");
      var1.writeAttribute("state", String.valueOf(this.state));
      var1.writeAttribute("subscriptionName", this.subscriptionName != null ? this.subscriptionName : "");
      var1.writeAttribute("isDurable", String.valueOf(this.durableSubscriber));
      var1.writeAttribute("isActive", String.valueOf(this.isActive()));
      var1.writeAttribute("isUsed", String.valueOf(this.isUsed()));
      var1.writeAttribute("selector", this.selector != null ? this.selector : "");
      var1.writeAttribute("clientID", this.clientId != null ? this.clientId : "");
      var1.writeAttribute("noLocal", String.valueOf(this.noLocal));
      if (this.queue != null) {
         var1.writeAttribute("queueName", this.queue.getName());
      }

      if (this.persistentHandle != null) {
         var1.writeAttribute("persistentStoreHandle", this.persistentHandle.toString());
      }

      var1.writeAttribute("isMulticast", String.valueOf(this.isMulticastSubscriber()));
      var1.writeAttribute("supportsClientResponsible", String.valueOf(this.supportsClientResponsible));
      var1.writeAttribute("unackedMessageCount", String.valueOf(this.unackedMessageCount));
      var1.writeStartElement("Session");
      var1.writeAttribute("sessionID", this.session.getId().toString());
      var1.writeAttribute("connectionID", this.session.getConnection().getId().toString());
      var1.writeEndElement();
   }

   public void dump(JMSDiagnosticImageSource var1, XMLStreamWriter var2) throws XMLStreamException, DiagnosticImageTimeoutException {
      var1.checkTimeout();
      var2.writeStartElement("Consumer");
      this.dumpCommon(var2);
      var2.writeEndElement();
   }

   public CompositeData getCompositeData() throws OpenDataException {
      String var1 = null;
      BESessionImpl var2 = this.getSession();
      if (var2 != null) {
         BEConnection var3 = var2.getConnection();
         if (var3 != null) {
            var1 = var3.getAddress();
         }
      }

      ConsumerInfo var4 = new ConsumerInfo(this.getSubscriptionName(), this.isDurable(), this.getSelector(), this.getClientID(), this.getNoLocal(), var1);
      return var4.toCompositeData();
   }

   private void setSubject(AuthenticatedSubject var1) {
      if (var1 != null) {
         synchronized(this.stateLock) {
            this.authenticatedSubject = var1;
         }
      }

   }

   public AuthenticatedSubject getSubject() {
      synchronized(this.stateLock) {
         return this.authenticatedSubject;
      }
   }

   static {
      _WLDF$INST_FLD_JMS_Diagnostic_Volume_Before_Medium = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "JMS_Diagnostic_Volume_Before_Medium");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "BEConsumerImpl.java", "weblogic.jms.backend.BEConsumerImpl", "logEvent", "(I)V", 520, InstrumentationSupport.makeMap(new String[]{"JMS_Diagnostic_Volume_Before_Medium"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo(InstrumentationSupport.createValueHandlingInfo("this", "weblogic.diagnostics.instrumentation.gathering.JMSConsumerImplRenderer", false, true), (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("eventType", "weblogic.diagnostics.instrumentation.gathering.JMSEventTypeStringRenderer", false, true)})}), (boolean)0);
      $assertionsDisabled = !BEConsumerImpl.class.desiredAssertionStatus();
      KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   }

   private class ConsumerCloseThread implements Runnable {
      private ConsumerCloseThread() {
      }

      public void run() {
         BEConsumerImpl.this.securityLapsed();
      }

      // $FF: synthetic method
      ConsumerCloseThread(Object var2) {
         this();
      }
   }

   private final class CountAdjuster implements Synchronization {
      private int count;
      private boolean incrementWindow;
      private boolean force;

      CountAdjuster(int var2, boolean var3, boolean var4) {
         this.count = var2;
         this.incrementWindow = var3;
         this.force = var4;
      }

      public void afterCompletion(int var1) {
         BEConsumerImpl.this.adjustUnackedCount(-this.count);
         if (this.incrementWindow) {
            try {
               BEConsumerImpl.this.makeWindowSpace(this.count, this.force);
            } catch (JMSException var3) {
               if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
                  JMSDebug.JMSBackEnd.debug("cannot make window space ", var3);
               }
            }
         }

      }

      public void beforeCompletion() {
      }
   }
}
