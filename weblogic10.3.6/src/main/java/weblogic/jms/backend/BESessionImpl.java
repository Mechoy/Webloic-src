package weblogic.jms.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import javax.jms.JMSException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import weblogic.jms.JMSLogger;
import weblogic.jms.JMSService;
import weblogic.jms.common.DispatcherCompletionListener;
import weblogic.jms.common.DurableSubscription;
import weblogic.jms.common.InvalidSubscriptionSharingException;
import weblogic.jms.common.JMSBrowserCreateResponse;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSDiagnosticImageSource;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSMessageId;
import weblogic.jms.common.JMSPushExceptionRequest;
import weblogic.jms.common.JMSPushRequest;
import weblogic.jms.common.JMSSecurityHelper;
import weblogic.jms.common.JMSServerUtilities;
import weblogic.jms.common.JMSSessionRecoverResponse;
import weblogic.jms.common.MessageImpl;
import weblogic.jms.common.NonDurableSubscription;
import weblogic.jms.dispatcher.Invocable;
import weblogic.jms.dispatcher.InvocableManagerDelegate;
import weblogic.jms.dispatcher.VoidResponse;
import weblogic.jms.extensions.WLMessage;
import weblogic.jms.utils.Simple;
import weblogic.jms.utils.tracing.AggregationCounter;
import weblogic.jms.utils.tracing.MessageTimeStamp;
import weblogic.messaging.ID;
import weblogic.messaging.common.MessageIDImpl;
import weblogic.messaging.dispatcher.InvocableMonitor;
import weblogic.messaging.dispatcher.Request;
import weblogic.messaging.kernel.GroupOwner;
import weblogic.messaging.kernel.KernelException;
import weblogic.messaging.kernel.KernelRequest;
import weblogic.messaging.kernel.Listener;
import weblogic.messaging.kernel.MessageElement;
import weblogic.messaging.kernel.Queue;
import weblogic.messaging.kernel.Sequence;
import weblogic.messaging.runtime.DiagnosticImageTimeoutException;
import weblogic.security.WLSPrincipals;
import weblogic.timers.NakedTimerListener;
import weblogic.timers.Timer;
import weblogic.transaction.TransactionHelper;
import weblogic.utils.collections.CircularQueue;
import weblogic.utils.concurrent.atomic.AtomicFactory;
import weblogic.utils.concurrent.atomic.AtomicInteger;
import weblogic.work.WorkManager;

public final class BESessionImpl extends BEDeliveryList implements BESession, Listener, NakedTimerListener {
   private final JMSID sessionId;
   private final JMSID sequencerId;
   private BEConnection connection;
   private final InvocableMonitor invocableMonitor;
   private String pushWorkManager;
   private final Set<BEConsumerImpl> consumers = new HashSet();
   private final Map<JMSID, BEBrowser> browsers = new HashMap();
   private final CircularQueue pendingMessages = new CircularQueue();
   private boolean stopped;
   private boolean closed;
   private boolean isPeerGone;
   private CloseWait closeInProgressLock;
   private final boolean transacted;
   private Object recoveryUnit;
   private final int acknowledgeMode;
   private int totalWindowSize;
   private AtomicInteger pipelineGeneration = AtomicFactory.createAtomicInteger();
   private final byte clientVersion;
   private static final boolean PUSH_STATS = false;
   private final AtomicInteger messagesPushed;
   private final AtomicInteger pushRequests;
   private static final int PUSH_DELAY;
   private static final int MAX_TOTAL_DELAY;
   private static final int DEFAULT_PUSH_DELAY = 3;
   private AggregationCounter aggregationCounter;
   private boolean noAggregation = false;
   private String aDestination = null;
   private int throughputEmphasis = -1;
   private WorkManager workManager;
   private long lastSeqAcked = 0L;

   BESessionImpl(BEConnection var1, JMSID var2, JMSID var3, boolean var4, boolean var5, int var6, byte var7, String var8) {
      super((BackEnd)null);
      this.sessionId = var2;
      this.sequencerId = var3;
      this.transacted = var4;
      this.acknowledgeMode = var6;
      this.connection = var1;
      this.pushWorkManager = var8;
      this.stopped = var1.isStopped();
      this.clientVersion = var7;
      if (var7 >= 5) {
         this.pipelineGeneration.set(15728640);
      }

      this.invocableMonitor = JMSService.getJMSService().getInvocableMonitor();
      this.messagesPushed = this.pushRequests = null;
      if (var5) {
         this.recoveryUnit = new XASessionOwner();
      } else {
         this.recoveryUnit = this;
      }

   }

   public JMSID getJMSID() {
      return this.sessionId;
   }

   public ID getId() {
      return this.getJMSID();
   }

   public JMSID getSequencerId() {
      return this.sequencerId;
   }

   public InvocableMonitor getInvocableMonitor() {
      return this.invocableMonitor;
   }

   public void setConnection(BEConnection var1) {
      this.connection = var1;
   }

   public BEConnection getConnection() {
      return this.connection;
   }

   int getAcknowledgeMode() {
      return this.acknowledgeMode;
   }

   String getPushWorkManager() {
      return this.pushWorkManager;
   }

   public int getPipelineGeneration() {
      return this.pipelineGeneration.get();
   }

   synchronized void adjustWindowSize(int var1) {
      this.totalWindowSize += var1;
      this.initDeliveryList(this.totalWindowSize, this.throughputEmphasis, PUSH_DELAY, MAX_TOTAL_DELAY);
   }

   Object getRecoveryUnit() {
      return this.recoveryUnit;
   }

   public int invoke(Request var1) throws JMSException {
      switch (var1.getMethodId()) {
         case 8464:
            return this.createBrowser((BEBrowserCreateRequest)var1);
         case 10256:
            return this.createConsumer((BEConsumerCreateRequest)var1);
         case 13072:
            return this.acknowledge((BESessionAcknowledgeRequest)var1);
         case 13328:
            this.close((BESessionCloseRequest)var1);
            break;
         case 13840:
            return this.recover((BESessionRecoverRequest)var1);
         case 14096:
            this.setRedeliveryDelay(((BESessionSetRedeliveryDelayRequest)var1).getRedeliveryDelay());
            break;
         case 14352:
            this.start();
            break;
         default:
            throw new AssertionError("No such method " + var1.getMethodId());
      }

      var1.setResult(VoidResponse.THE_ONE);
      var1.setState(Integer.MAX_VALUE);
      return Integer.MAX_VALUE;
   }

   private BEConsumerImpl createBEConsumer(BEConsumerCreateRequest var1) throws JMSException {
      synchronized(this) {
         if (this.closedOrPeerGone()) {
            throw new JMSException("Session is closed");
         }
      }

      BEDestinationImpl var2 = (BEDestinationImpl)InvocableManagerDelegate.delegate.invocableFind(20, var1.getDestinationId());
      if (var2 == null) {
         throw new weblogic.jms.common.JMSException("Destination not found");
      } else {
         this.aDestination = var2.getName();
         this.throughputEmphasis = Math.max(this.throughputEmphasis, var2.getMessagingPerformancePreference());
         BackEnd var3 = var2.getBackEnd();
         if (var3 != null) {
            var3.checkShutdownOrSuspendedNeedLock("create consumer");
            if (this.workManager == null) {
               this.workManager = var3.getWorkManager();
            }
         }

         boolean var4;
         synchronized(this) {
            var4 = this.stopped;
         }

         if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
            JMSDebug.JMSBackEnd.debug("Creating consumer to Destination " + var2.getName() + ": clientId = " + var1.getClientId() + " subscriptionName=" + var1.getName());
         }

         if (var2 instanceof BETopicImpl && var1.getClientId() != null) {
            if (var1.getName() != null) {
               DurableSubscription var5 = ((BETopicImpl)var2).findDurableSubscriber(var1.getClientId(), var1.getName(), var1.getSelector(), var1.getNoLocal(), var1.getFlag(), var1.getClientIdPolicy(), var1.getSubscriptionSharingPolicy());
               if (var5 != null) {
                  if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
                     JMSDebug.JMSBackEnd.debug("Found a durable subscriber sub.subscriptionPolicy = " + var5.getSubscriptionSharingPolicy() + " request.subscriptionpolicy= " + var1.getSubscriptionSharingPolicy());
                  }

                  if (var1.getSubscriptionSharingPolicy() != var5.getSubscriptionSharingPolicy() && var5.getSubscribersCount() > 0) {
                     throw new InvalidSubscriptionSharingException("Cannot change the sharing policy on an active durable subscription");
                  }

                  BEConsumerImpl var6 = var5.getConsumer();
                  if (!WLSPrincipals.isKernelUsername(JMSSecurityHelper.getSimpleAuthenticatedName())) {
                     var2.getJMSDestinationSecurity().checkReceivePermission(JMSSecurityHelper.getCurrentSubject());
                     JMSService.getJMSService().registerSecurityParticipant(var2.getJMSDestinationSecurity().getJMSResourceForReceive(), var6);
                  }

                  if (var6.isClosed()) {
                     var6.restore(var1, this, !var4);
                     return var6;
                  }

                  if (var1.getSubscriptionSharingPolicy() == 0) {
                     throw new InvalidSubscriptionSharingException("Durable Subscription " + var1.getName() + " is in use and cannot be shared");
                  }

                  return ((BETopicImpl)var2).createConsumer(this, !var4, var1, var5);
               }

               if (var1.getFlag() == 0) {
                  throw new weblogic.jms.common.JMSException("Subscription not found");
               }
            } else if (var1.getClientId() != null) {
               NonDurableSubscription var10 = ((BETopicImpl)var2).findNonDurableSubscriber(var1.getClientId(), var1.getSelector(), var1.getNoLocal(), var1.getClientIdPolicy(), var1.getSubscriptionSharingPolicy());
               if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
                  JMSDebug.JMSBackEnd.debug("createBEConsumer: found non durable " + var10 + " for " + var1.getClientId() + " " + var1.getSelector() + " " + var1.getNoLocal() + " " + var1.getClientIdPolicy() + " " + var1.getSubscriptionSharingPolicy());
               }

               if (var10 != null) {
                  if (var10.getSubscriptionSharingPolicy() != var1.getSubscriptionSharingPolicy() && var10.getSubscribersCount() > 0) {
                     throw new InvalidSubscriptionSharingException("There is an active subscriber that uses a different subscription sharing policy");
                  }

                  if (var10.getSubscriptionSharingPolicy() != 0) {
                     return ((BETopicImpl)var2).createConsumer(this, !var4, var1, var10);
                  }
               }
            }
         }

         try {
            return var2.createConsumer(this, !var4, var1);
         } catch (ClassCastException var7) {
            throw new weblogic.jms.common.JMSException("Invalid destination type", var7);
         }
      }
   }

   private int createConsumer(BEConsumerCreateRequest var1) throws JMSException {
      BEConsumerImpl var2 = this.createBEConsumer(var1);
      this.registerConsumer(var2);
      var1.setResult(new BEConsumerCreateResponse(var2.registerConsumerReconnectInfo(var1.getConsumerReconnectInfo())));
      var1.setState(Integer.MAX_VALUE);
      return Integer.MAX_VALUE;
   }

   private void registerConsumer(BEConsumerImpl var1) throws JMSException {
      synchronized(this) {
         this.consumers.add(var1);
      }

      InvocableManagerDelegate.delegate.invocableAdd(17, var1);
   }

   public void peerGone() throws JMSException {
      this.performDelayedClose();
   }

   private void performDelayedClose() throws JMSException {
      BEConsumerImpl var3 = null;
      ArrayList var1;
      ArrayList var2;
      Iterator var5;
      BEConsumerImpl var6;
      synchronized(this) {
         if (this.isPeerGone) {
            return;
         }

         this.isPeerGone = true;
         var5 = this.consumers.iterator();

         while(true) {
            if (var5.hasNext()) {
               var6 = (BEConsumerImpl)var5.next();
               long var7 = var6.getDelayServerClose();
               if (var7 >= 1L) {
                  if (var3 == null || var7 < var3.getDelayServerClose()) {
                     var3 = var6;
                  }
                  continue;
               }

               var3 = null;
            }

            if (var3 != null) {
               var1 = new ArrayList(this.consumers);
               var2 = new ArrayList(this.browsers.values());
               this.browsers.clear();
            } else {
               var2 = null;
               var1 = null;
            }
            break;
         }
      }

      if (var3 == null) {
         this.close();
      } else {
         this.consumersStop(var1);
         JMSException var4 = null;
         var5 = var1.iterator();

         while(true) {
            do {
               if (!var5.hasNext()) {
                  this.browsersClose(var2);
                  var3.getDestination().getBackEnd().getTimerManager().schedule(this, var3.getDelayServerClose());
                  if (var4 != null) {
                     throw var4;
                  }

                  return;
               }

               var6 = (BEConsumerImpl)var5.next();
            } while(var6.getDelayServerClose() >= 1L);

            try {
               var6.close(0L);
            } catch (JMSException var10) {
               if (var4 == null) {
                  var4 = var10;
               }
            }
         }
      }
   }

   public void timerExpired(Timer var1) {
      synchronized(this) {
         if (this.closed) {
            return;
         }
      }

      try {
         this.close();
      } catch (JMSException var4) {
         JMSLogger.logJMSServerShutdownError(this.getConnection().getDispatcher().getId().getName(), var4.getMessage(), var4);
      }

   }

   long sequenceFromMsgId(MessageIDImpl var1) {
      long var2 = Long.MAX_VALUE;
      synchronized(this) {
         Iterator var5 = this.pendingMessages.iterator();

         while(var5.hasNext()) {
            MessageElement var6 = (MessageElement)var5.next();
            if (var1.differentiatedEquals(var6.getMessage().getMessageID())) {
               var2 = var6.getUserSequenceNum();
               if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
                  JMSDebug.JMSBackEnd.debug("reconnect closing lastSequenceNumber " + var2);
               }
               break;
            }

            if (JMSDebug.JMSBackEnd.isDebugEnabled() && !var6.getMessage().getMessageID().getClass().equals(var1.getClass())) {
               JMSDebug.JMSBackEnd.debug("comparing wrong classes me.id " + var6.getMessage().getMessageID().getClass() + " against last " + var1.getClass());
            }
         }

         return var2;
      }
   }

   private void close(BESessionCloseRequest var1) throws JMSException {
      try {
         JMSService.getJMSService().checkShutdown();
      } catch (JMSException var3) {
         return;
      }

      if (var1.allowDelayClose()) {
         this.performDelayedClose();
      } else {
         this.close(var1.getLastSequenceNumber(), false);
      }
   }

   public void close() throws JMSException {
      this.close(Long.MAX_VALUE, false);
   }

   void close(JMSMessageId var1) throws JMSException {
      this.close(this.sequenceFromMsgId(var1), true);
   }

   private void close(long var1, boolean var3) throws JMSException {
      CloseWait var6 = null;
      boolean var7 = false;

      try {
         ArrayList var4;
         ArrayList var5;
         synchronized(this) {
            if (this.closed) {
               if (var3) {
                  var6 = this.closeInProgressLock;
               }

               return;
            }

            var6 = this.closeInProgressLock = new CloseWait();
            var7 = this.closed = true;
            var4 = new ArrayList(this.consumers);
            this.consumers.clear();
            var5 = new ArrayList(this.browsers.values());
            this.browsers.clear();
         }

         this.closeInternal(var4, var1, var5);
      } finally {
         if (var7) {
            var6.complete();
            this.connection.sessionRemove(this.getJMSID());
         } else if (var6 != null) {
            var6.waitUntilClosed();
         }

      }

   }

   private void closeInternal(ArrayList<Invocable> var1, long var2, ArrayList<Invocable> var4) throws JMSException {
      this.consumersStop(var1);
      this.waitUntilIdle();
      this.recover(var2, this.pipelineGeneration.get());
      JMSException var6 = null;
      Iterator var5 = var1.iterator();

      while(var5.hasNext()) {
         try {
            ((BEConsumerImpl)var5.next()).close(0L);
         } catch (JMSException var8) {
            var6 = var8;
         }
      }

      this.browsersClose(var4);
      if (var6 != null) {
         throw var6;
      }
   }

   private void consumersStop(ArrayList<Invocable> var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         ((BEConsumerImpl)var2.next()).stop();
      }

   }

   private void browsersClose(ArrayList<Invocable> var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         ((BEBrowserImpl)var2.next()).close();
      }

   }

   private int createBrowser(BEBrowserCreateRequest var1) throws JMSException {
      synchronized(this) {
         if (this.closedOrPeerGone()) {
            throw new JMSException("Session is closed");
         }
      }

      BEDestinationImpl var2 = (BEDestinationImpl)InvocableManagerDelegate.delegate.invocableFind(20, var1.getDestinationId());
      var2.checkShutdownOrSuspended("create browser");
      BEBrowser var3 = var2.createBrowser(this, var1.getSelector());
      JMSService.getJMSService().registerSecurityParticipant(var2.getJMSDestinationSecurity().getJMSResourceForBrowse(), (BEBrowserImpl)var3);
      this.browserAdd(var3);
      var1.setResult(new JMSBrowserCreateResponse(var3.getJMSID()));
      var1.setState(Integer.MAX_VALUE);
      return var1.getState();
   }

   private void browserAdd(BEBrowser var1) throws JMSException {
      InvocableManagerDelegate.delegate.invocableAdd(18, var1);
      synchronized(this) {
         this.browsers.put(var1.getJMSID(), var1);
      }
   }

   public void browserRemove(JMSID var1) {
      synchronized(this) {
         this.browsers.remove(var1);
      }

      InvocableManagerDelegate.delegate.invocableRemove(18, var1);
   }

   public void start() throws JMSException {
      this.connection.checkShutdownOrSuspendedNeedLock("start session");
      ArrayList var1;
      synchronized(this) {
         if (this.closedOrPeerGone() || !this.stopped) {
            return;
         }

         var1 = new ArrayList(this.consumers);
      }

      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         BEConsumerImpl var3 = (BEConsumerImpl)var2.next();
         var3.start();
      }

      synchronized(this) {
         this.stopped = false;
      }
   }

   public void stop() {
      ArrayList var1;
      synchronized(this) {
         if (this.stopped) {
            return;
         }

         this.stopped = true;
         var1 = new ArrayList(this.consumers);
      }

      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         BEConsumerImpl var3 = (BEConsumerImpl)var2.next();
         var3.stop();
      }

   }

   private void setRedeliveryDelay(long var1) throws JMSException {
      ArrayList var3;
      synchronized(this) {
         if (this.closedOrPeerGone()) {
            throw new weblogic.jms.common.JMSException("Session is closed");
         }

         var3 = new ArrayList(this.consumers);
      }

      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         BEConsumerImpl var5 = (BEConsumerImpl)var4.next();
         var5.setRedeliveryDelay(var1);
      }

   }

   private synchronized List<MessageElement> removeBefore(long var1) {
      if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
         JMSDebug.JMSBackEnd.debug("Removing pending messages through " + (var1 == Long.MAX_VALUE ? "MAX" : "" + var1) + " sessionId " + this.sessionId + " sequenceId " + this.sequencerId + " connection " + this.connection);
      }

      int var3 = this.pendingMessages.size();
      ArrayList var4 = new ArrayList(var3);

      for(int var5 = 0; var5 < var3; ++var5) {
         MessageElement var6 = (MessageElement)this.pendingMessages.remove();
         if (var6.getUserSequenceNum() <= var1) {
            var4.add(var6);
         } else {
            this.pendingMessages.add(var6);
         }
      }

      return var4;
   }

   private synchronized List<MessageElement> removeAfter(long var1, BEConsumerImpl var3) {
      if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
         JMSDebug.JMSBackEnd.debug("Removing pending and outgoing messages after " + var1);
      }

      int var4 = this.pendingMessages.size();
      ArrayList var5 = new ArrayList(var4);

      for(int var6 = 0; var6 < var4; ++var6) {
         MessageElement var7 = (MessageElement)this.pendingMessages.remove();
         if (var7.getUserSequenceNum() > var1 && (BEConsumerImpl)var7.getUserData() == var3) {
            var5.add(var7);
         } else {
            this.pendingMessages.add(var7);
         }
      }

      return var5;
   }

   private void acknowledgeStart(BESessionAcknowledgeRequest var1) throws JMSException {
      if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
         JMSDebug.JMSBackEnd.debug("Starting an acknowledge request for sequence number " + var1.getLastSequenceNumber());
      }

      var1.setWorkManager(this.workManager);
      var1.setTransaction(TransactionHelper.getTransactionHelper().getTransaction());
      List var2 = this.removeBefore(var1.getLastSequenceNumber());
      if (var2.isEmpty()) {
         synchronized(this) {
            if (this.lastSeqAcked < var1.getLastSequenceNumber()) {
               throw new JMSException("Cannot acknowledge messages: likely server is shutting down or consumer is closed");
            }

            var1.setState(11001);
         }
      } else {
         var1.setIterator(var2.listIterator());
         var1.setState(11000);
      }

   }

   private static KernelRequest acknowledgeContinue(BESessionAcknowledgeRequest var0) throws JMSException {
      int var1 = 0;
      Queue var2 = null;
      BEConsumerImpl var3 = null;
      ArrayList var4 = new ArrayList();

      ListIterator var5;
      MessageElement var6;
      for(var5 = var0.getIterator(); var5.hasNext(); var2 = var6.getQueue()) {
         var6 = (MessageElement)var5.next();
         if (var2 != null && var2 != var6.getQueue()) {
            var5.previous();
            break;
         }

         var4.add(var6);
         if (var3 == var6.getUserData()) {
            ++var1;
         } else {
            if (var3 != null) {
               if (!var0.isTransactional()) {
                  var3.incrementPendingCount(var1, false);
               } else {
                  BESessionImpl var7 = var3.getSession();
                  if (var6.getGroup() != null && (var7 == null || var7.recoveryUnit == var7 || !var3.hasListener())) {
                     throw new JMSException("Cannot change group recover owner.");
                  }

                  var3.incrementPendingCountTransactionally(var0.getTransaction(), var1, false);
               }
            }

            var3 = (BEConsumerImpl)var6.getUserData();
            var1 = 1;
         }
      }

      if (var4.isEmpty()) {
         var0.setState(11001);
         return null;
      } else {
         if (var3 != null) {
            if (var0.isTransactional()) {
               var3.incrementPendingCountTransactionally(var0.getTransaction(), var1, false);
            } else {
               var3.incrementPendingCount(var1, false);
            }
         }

         if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
            JMSDebug.JMSBackEnd.debug("Acknowledging " + var4.size() + " messages for " + var2.getName() + " session " + var3.getSession());
         }

         KernelRequest var9 = null;

         try {
            if (var0.isTransactional()) {
               var2.associate(var4, var3);
            } else {
               var9 = var2.acknowledge(var4);
            }
         } catch (KernelException var8) {
            throw new weblogic.jms.common.JMSException(var8);
         }

         if (!var5.hasNext()) {
            var0.setState(11001);
         }

         return var9;
      }
   }

   private int acknowledge(BESessionAcknowledgeRequest var1) throws JMSException {
      while(true) {
         switch (var1.getState()) {
            case 0:
               this.acknowledgeStart(var1);
               break;
            case 11000:
               if (var1.getKernelRequest() != null) {
                  try {
                     var1.getKernelRequest().getResult();
                     var1.setKernelRequest((KernelRequest)null);
                  } catch (KernelException var9) {
                     throw new weblogic.jms.common.JMSException(var9);
                  }
               }

               KernelRequest var2 = acknowledgeContinue(var1);
               if (var2 == null) {
                  break;
               }

               synchronized(var2) {
                  if (!var2.hasResult()) {
                     var1.setKernelRequest(var2);
                     var1.needOutsideResult();
                     var2.addListener(new DispatcherCompletionListener(var1));
                     return var1.getState();
                  }
                  break;
               }
            case 11001:
               if (var1.getKernelRequest() != null) {
                  try {
                     var1.getKernelRequest().getResult();
                     var1.setKernelRequest((KernelRequest)null);
                  } catch (KernelException var7) {
                     throw new weblogic.jms.common.JMSException(var7);
                  }
               }

               if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
                  JMSDebug.JMSBackEnd.debug("Acknowledgement complete");
               }

               synchronized(this) {
                  this.lastSeqAcked = var1.getLastSequenceNumber();
               }

               var1.setIterator((ListIterator)null);
               var1.setResult(VoidResponse.THE_ONE);
               var1.setState(Integer.MAX_VALUE);
               return Integer.MAX_VALUE;
            default:
               throw new AssertionError("Invalid request state");
         }
      }
   }

   private int recover(BESessionRecoverRequest var1) throws JMSException {
      if (this.closed) {
         throw new weblogic.jms.common.JMSException("Session is closed");
      } else {
         JMSService.getJMSService().checkShutdown();
         if (var1.getPipelineGeneration() == 0) {
            this.recover81(var1);
         } else {
            this.recover90(var1);
         }

         var1.setResult(new JMSSessionRecoverResponse(var1.getLastSequenceNumber()));
         var1.setState(Integer.MAX_VALUE);
         return var1.getState();
      }
   }

   private boolean closedOrPeerGone() {
      return this.closed || this.isPeerGone;
   }

   private void recover81(BESessionRecoverRequest var1) throws JMSException {
      this.recover(var1.getLastSequenceNumber(), this.pipelineGeneration.get());
   }

   private void recover90(BESessionRecoverRequest var1) throws JMSException {
      ArrayList var3;
      synchronized(this) {
         var3 = new ArrayList(this.consumers);
      }

      HashMap var4 = new HashMap(var3.size());
      Iterator var2 = var3.iterator();

      BEConsumerImpl var5;
      while(var2.hasNext()) {
         var5 = (BEConsumerImpl)var2.next();
         if (var5.stopListening()) {
            var4.put(var5.getId(), var5);
         }
      }

      this.waitUntilIdle();
      this.recover(var1.getLastSequenceNumber(), var1.getPipelineGeneration());
      var2 = var3.iterator();

      while(var2.hasNext()) {
         var5 = (BEConsumerImpl)var2.next();
         if (var4.get(var5.getId()) != null) {
            var5.startListening();
         }
      }

   }

   private void replaceMessages(List<MessageElement> var1, boolean var2) throws JMSException {
      if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
         JMSDebug.JMSBackEnd.debug("Replacing " + var1.size() + " messages on the kernel queue," + (var2 ? " incrementing" : " without incrementing"));
      }

      KernelRequest var3 = new KernelRequest();
      Iterator var4 = var1.iterator();

      try {
         while(var4.hasNext()) {
            MessageElement var5 = (MessageElement)var4.next();
            BEConsumerImpl var6 = (BEConsumerImpl)var5.getUserData();
            var3.reset();
            var5.getQueue().negativeAcknowledge(var5, var6.getRedeliveryDelay(), var2, var3);
            var3.getResult();
            var6.incrementPendingCount(1, true);
         }

      } catch (KernelException var7) {
         throw new weblogic.jms.common.JMSException(var7);
      }
   }

   private void recover(long var1, int var3) throws JMSException {
      if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
         JMSDebug.JMSBackEnd.debug("Got a recover request for sequence num " + var1);
      }

      List var4;
      List var5;
      synchronized(this) {
         this.pipelineGeneration.set(var3);
         var4 = this.removeBefore(var1);
         if (this.pendingMessages.isEmpty()) {
            var5 = null;
         } else {
            var5 = this.removeBefore(Long.MAX_VALUE);
         }
      }

      this.replaceMessages(var4, true);
      if (var5 != null) {
         this.replaceMessages(var5, false);
      }

      if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
         if (var5 == null) {
            JMSDebug.JMSBackEnd.debug("recovered " + var4.size() + " messages before " + var1);
         } else {
            JMSDebug.JMSBackEnd.debug("recovered " + var4.size() + " messages before " + var1 + ", and " + var5.size() + " afterwards");
         }
      }

   }

   synchronized void addPendingMessage(MessageElement var1, BEConsumerImpl var2) {
      assert var1.getUserSequenceNum() > 0L;

      this.pendingMessages.add(var1);
   }

   void removeConsumer(BEConsumerImpl var1, long var2) throws JMSException {
      this.replaceMessages(this.removeAfter(var2, var1), var2 == 0L);
      InvocableManagerDelegate.delegate.invocableRemove(17, var1.getJMSID());
      synchronized(this) {
         this.consumers.remove(var1);
      }
   }

   void removeConsumerWithError(BEConsumerImpl var1, long var2, weblogic.jms.common.JMSException var4) throws JMSException {
      this.removeConsumer(var1, var2);
      if (this.connection != null) {
         JMSServerUtilities.anonDispatchNoReply(new JMSPushExceptionRequest(10, var1.getJMSID(), var4), this.connection.getDispatcher());
      }

   }

   boolean allowsImplicitAcknowledge() {
      if (this.transacted) {
         return false;
      } else {
         return this.clientVersion >= 3;
      }
   }

   private void decorateMessageWithSequence(WLMessage var1, String var2, long var3) {
      var1.setSAFSeqNumber(var3);
      var1.setSAFSequenceName(var2);
   }

   protected void pushMessages(List var1) {
      JMSPushRequest var2 = null;
      JMSPushRequest var3 = null;
      Iterator var4 = var1.iterator();

      while(true) {
         MessageElement var5;
         BEConsumerImpl var7;
         boolean var9;
         do {
            do {
               if (!var4.hasNext()) {
                  if (var2 == null) {
                     return;
                  }

                  if (this.aDestination != null && this.aggregationCounter == null && !this.noAggregation) {
                     MessageTimeStamp.newAggregationCounter(this.aDestination + "-" + this.toString(), 51);
                  }

                  if (this.aggregationCounter != null) {
                     this.aggregationCounter.increment(var1.size());
                  } else {
                     this.noAggregation = true;
                  }

                  try {
                     JMSServerUtilities.anonDispatchNoReply(var2, this.getConnection().getDispatcher());
                  } catch (JMSException var12) {
                     JMSLogger.logErrorPushingMessage(var12.toString(), var12);
                  }

                  return;
               }

               var5 = (MessageElement)var4.next();
               Sequence var6 = var5.getSequence();
               if (var6 != null) {
                  this.decorateMessageWithSequence((WLMessage)var5.getMessage(), var6.getName(), var5.getSequenceNum());
               }

               var7 = (BEConsumerImpl)var5.getUserData();
               boolean var8 = var7.allowsImplicitAcknowledge();
               var9 = var8 || this.acknowledgeMode == 4;
               if (!var9) {
                  this.addPendingMessage(var5, var7);
                  var7.adjustUnackedCount(1);
               }

               if (JMSDebug.JMSMessagePath.isDebugEnabled()) {
                  JMSDebug.JMSMessagePath.debug("BACKEND/BESession (id: " + this.sessionId + ") : " + "BACKEND/BEConsumer (id: " + var7.getClientID() + ", sub: " + var7.getSubscriptionName() + ") : " + "Pushing to the frontend, message " + ((MessageImpl)var5.getMessage()).getJMSMessageID());
               }

               JMSPushRequest var10 = new JMSPushRequest(13, this.sequencerId, (MessageImpl)var5.getMessage(), var7.createPushEntry(var5, var8, var9));
               if (var2 == null) {
                  var3 = var10;
                  var2 = var10;
               } else {
                  var3.setNext(var10);
                  var3 = var10;
               }

               MessageTimeStamp.record(4, var10.getMessage());
            } while(!var9);
         } while(var7.isKernelAutoAcknowledge());

         try {
            KernelRequest var11 = var7.getKernelQueue().acknowledge(var5);
            if (var11 != null) {
               var11.getResult();
            }
         } catch (KernelException var13) {
            if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
               JMSDebug.JMSBackEnd.debug("Unexpected exception while implicitly acknowledging: " + var13, var13);
            }
         }
      }
   }

   public void dump(JMSDiagnosticImageSource var1, XMLStreamWriter var2) throws XMLStreamException, DiagnosticImageTimeoutException {
      var1.checkTimeout();
      var2.writeStartElement("Session");
      var2.writeAttribute("id", this.sessionId != null ? this.sessionId.toString() : "");
      var2.writeAttribute("pendingMessagesCurrentCount", String.valueOf(this.pendingMessages.size()));
      var2.writeAttribute("browsersCurrentCount", String.valueOf(this.browsers.size()));
      var2.writeStartElement("Consumers");

      assert this.consumers instanceof HashSet;

      HashSet var3 = (HashSet)((HashSet)this.consumers).clone();
      var2.writeAttribute("currentCount", String.valueOf(var3.size()));
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         BEConsumerCommon var5 = (BEConsumerCommon)var4.next();
         var5.dump(var1, var2);
      }

      var2.writeEndElement();
      var2.writeEndElement();
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (var1 != null && var1 instanceof BESessionImpl) {
         BESessionImpl var2 = (BESessionImpl)var1;
         return var2.sessionId != null ? var2.sessionId.equals(this.sessionId) : var2.sessionId == this.sessionId;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.sessionId != null ? this.sessionId.hashCode() : 0;
   }

   static {
      String var0 = Simple.getenv("weblogic.jms.PushDelay");
      int var1 = 3;
      if (var0 != null) {
         try {
            var1 = Integer.parseInt(var0);
         } catch (NumberFormatException var6) {
         }
      }

      PUSH_DELAY = var1;
      int var2 = 0;
      if (PUSH_DELAY != 0) {
         String var3 = Simple.getenv("weblogic.jms.MaxTotalDelay");
         if (var3 != null) {
            try {
               var2 = Integer.parseInt(var3);
            } catch (NumberFormatException var5) {
            }
         }
      }

      MAX_TOTAL_DELAY = var2;
   }

   private static class CloseWait {
      boolean completed;
      boolean waiters;

      private CloseWait() {
      }

      private synchronized void complete() {
         this.completed = true;
         if (this.waiters) {
            this.notifyAll();
         }

      }

      private synchronized void waitUntilClosed() {
         if (!this.completed) {
            this.waiters = true;

            try {
               this.wait();
            } catch (InterruptedException var2) {
               throw new AssertionError(var2);
            }
         }
      }

      // $FF: synthetic method
      CloseWait(Object var1) {
         this();
      }
   }

   private class XASessionOwner implements GroupOwner {
      private XASessionOwner() {
      }

      public boolean exposeOnlyOneMessage() {
         return true;
      }

      // $FF: synthetic method
      XASessionOwner(Object var2) {
         this();
      }
   }
}
