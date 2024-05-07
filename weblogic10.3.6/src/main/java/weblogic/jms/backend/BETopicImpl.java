package weblogic.jms.backend;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.jms.InvalidSelectorException;
import javax.jms.JMSException;
import javax.jms.ServerSessionPool;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import weblogic.descriptor.BeanUpdateFailedException;
import weblogic.j2ee.descriptor.wl.TopicBean;
import weblogic.jms.JMSService;
import weblogic.jms.common.ConsumerReconnectInfo;
import weblogic.jms.common.DSManager;
import weblogic.jms.common.DestinationImpl;
import weblogic.jms.common.DurableSubscription;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSDestinationSecurity;
import weblogic.jms.common.JMSDiagnosticImageSource;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSSQLExpression;
import weblogic.jms.common.JMSSQLFilter;
import weblogic.jms.common.JMSSecurityHelper;
import weblogic.jms.common.NonDurableSubscription;
import weblogic.jms.common.Subscription;
import weblogic.jms.multicast.JMSTMSocket;
import weblogic.management.runtime.JMSDurableSubscriberRuntimeMBean;
import weblogic.messaging.kernel.InvalidExpressionException;
import weblogic.messaging.kernel.KernelException;
import weblogic.messaging.kernel.KernelRequest;
import weblogic.messaging.kernel.Queue;
import weblogic.messaging.kernel.Quota;
import weblogic.messaging.kernel.Topic;
import weblogic.messaging.runtime.DiagnosticImageTimeoutException;
import weblogic.security.WLSPrincipals;
import weblogic.store.PersistentHandle;

public final class BETopicImpl extends BEDestinationImpl {
   private static final String DURABLE_SUB_PREFIX = "_weblogic.jms.DS.";
   private static final String SHARABLE_NON_DURABLE_SUB_PREFIX = "_weblogic.jms.sharable.NDS.";
   private Topic topic;
   private final HashMap durableRuntimeMBeans = new HashMap();
   private int multicastConsumerCount;
   private boolean messageLogging;
   private String multicastAddress;
   private int multicastPort;
   private byte multicastTTL;
   private InetAddress multicastGroup;
   private BEMulticastConsumer multicastConsumer;
   private HashMap nonDurableSubscriptions = new HashMap();
   private TopicBean topicBean;

   public BETopicImpl(TopicBean var1, BackEnd var2, String var3, boolean var4, JMSDestinationSecurity var5) throws JMSException {
      super(var2, var3, var4, var5);
      this.topicBean = var1;
      Topic var6 = var2.findKernelTopic(var3);
      if (var6 == null) {
         var6 = var2.createKernelTopic(var3, (Map)null);
      }

      this.setKernel(var6);
   }

   protected void setKernel(Topic var1) throws JMSException {
      super.setKernel(var1);
      this.topic = var1;
   }

   public void open() throws JMSException {
      DestinationImpl var1 = this.getDestinationImpl();
      String var2 = this.getMulticastAddress();
      if (var2 != null && var2.length() != 0) {
         try {
            this.setMulticastGroup(InetAddress.getByName(var2));
         } catch (UnknownHostException var5) {
            throw new JMSException("MulticastAddress is not valid");
         }

         var1.setMulticastAddress(var2);
         var1.setPort(this.getMulticastPort());
         JMSService.getJMSService().openMulticastSendSocket();
      }

      super.open();

      try {
         this.topic.setFilter(new JMSSQLFilter(this.topic.getKernel()));
         this.topic.setProperty("RedirectionListener", this);
      } catch (KernelException var4) {
         throw new weblogic.jms.common.JMSException(var4);
      }

      if (this.isMessageLoggingEnabled() && !this.backEnd.isMemoryLow()) {
         this.messageLogging = true;
      }

   }

   public int getDestinationTypeIndicator() {
      return this.isTemporary() ? 8 : 2;
   }

   public Queue createSubscriptionQueue(String var1, boolean var2) throws JMSException {
      return this.createSubscriptionQueue(var1, var2, 0);
   }

   Queue createSubscriptionQueue(String var1, boolean var2, int var3) throws JMSException {
      HashMap var4 = new HashMap();
      var4.put("MaximumMessageSize", new Integer(this.maximumMessageSize));
      var4.put("Quota", this.topic.getProperty("Quota"));
      var4.put("RedirectionListener", this);
      if (!var2) {
         var4.put("StatisticsMode", "Bypass");
      }

      try {
         Queue var5 = null;
         synchronized(this.backEnd) {
            if (var2 || var3 == 1) {
               var5 = this.backEnd.findKernelQueue(var1);
            }

            if (var5 == null) {
               var5 = this.backEnd.createKernelQueue(var1, var4);
            } else if (var3 != 1) {
               var5.setProperties(var4);
            }

            var5.setComparator(this.comparator);
            return var5;
         }
      } catch (KernelException var9) {
         throw new weblogic.jms.common.JMSException(var9);
      }
   }

   public void activateSubscriptionQueue(Queue var1, BEConsumerImpl var2, JMSSQLExpression var3, boolean var4, boolean var5) throws JMSException {
      try {
         if (var4 && var2 != null) {
            var1.addListener(var2);
            if (this.isMessageLoggingEnabled() && !this.backEnd.isMemoryLow()) {
               var1.setProperty("Logging", new Integer(15));
            }
         }

         var1.setProperty("Durable", new Boolean(var5 && this.backEnd.isStoreEnabled()));
         var1.suspend(this.destination.getMask());
         var1.resume(16384);
         KernelRequest var6 = new KernelRequest();
         this.topic.subscribe(var1, var3, var6);
         var6.getResult();
      } catch (InvalidExpressionException var7) {
         throw new InvalidSelectorException(var7.toString());
      } catch (KernelException var8) {
         throw new weblogic.jms.common.JMSException(var8);
      }
   }

   public void setDestinationKeysList(List var1) {
      ArrayList var2;
      synchronized(this) {
         super.setDestinationKeysList(var1);
         var2 = new ArrayList(this.consumers);
      }

      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         Queue var4 = ((BEConsumerImpl)var3.next()).getKernelQueue();
         if (var4 != null) {
            var4.setComparator(this.comparator);
         }
      }

   }

   protected int getAdjustedExpirationPolicy(boolean var1) {
      return (this.expirationPolicy == 2 || this.expirationPolicy == 4) && var1 ? 1 : this.expirationPolicy;
   }

   public static String getSubscriptionQueueName(JMSID var0, String var1, String var2) {
      return getSubscriptionQueueName(var0, var1, 0, var2, 0, (String)null, (String)null);
   }

   private String getSubscriptionQueueName(BEConsumerCreateRequest var1, Subscription var2, String var3, String var4) {
      if (var1.getClientId() != null && var1.getName() == null && var1.getSubscriptionSharingPolicy() == 1) {
         return var2 != null ? ((NonDurableSubscription)var2).getSubscriptionQueueName() : this.getNextSharableNonDurableSubName(var1.getClientId());
      } else {
         return getSubscriptionQueueName(var1.getConsumerId(), var1.getClientId(), var1.getClientIdPolicy(), var1.getName(), var1.getSubscriptionSharingPolicy(), var3, var4);
      }
   }

   private static String getSubscriptionQueueName(JMSID var0, String var1, int var2, String var3, int var4, String var5, String var6) {
      if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
         JMSDebug.JMSBackEnd.debug("BETopicImpl: getSubscriptionQueueName: cleintId=" + var1 + " name =" + var3 + " client id policy = " + var2 + " subscriptionSharingPolicy = " + var4 + " topic name = " + var5 + " JMS Server name = " + var6);
      }

      if (var1 != null && var3 != null) {
         StringBuffer var7 = new StringBuffer();
         var7.append("_weblogic.jms.DS.");
         var7.append(var1);
         var7.append('.');
         var7.append(var3);
         if (var2 == 1) {
            var7.append("@" + var5 + "@" + var6);
         }

         return var7.toString();
      } else {
         return var0.toString();
      }
   }

   protected BEConsumerImpl createConsumer(BESessionImpl var1, boolean var2, BEConsumerCreateRequest var3) throws JMSException {
      return this.createConsumer(var1, var2, var3, (Subscription)null);
   }

   protected BEConsumerImpl createConsumer(BESessionImpl var1, boolean var2, BEConsumerCreateRequest var3, Subscription var4) throws JMSException {
      this.checkShutdownOrSuspendedNeedLock("create consumer");
      JMSID var5;
      if (var1 != null) {
         var5 = var1.getConnection().getJMSID();
      } else {
         var5 = null;
      }

      boolean var6 = var3.getClientId() != null && var3.getName() != null;
      boolean var7 = false;
      int var8 = 0;
      if (!var6) {
         var8 |= 4;
      }

      if (var1 != null && var1.getAcknowledgeMode() == 128) {
         if (this.multicastGroup == null) {
            throw new weblogic.jms.common.JMSException("Topic " + this.name + " does not support MULTICAST_NO_ACKNOWLEDGE delivery mode");
         }

         var8 |= 16;
         var7 = true;
      }

      JMSSQLExpression var9 = null;
      Queue var10 = null;
      if (!var7) {
         var9 = new JMSSQLExpression(var3.getSelector(), var3.getNoLocal(), var5, var3.getClientId(), var3.getClientIdPolicy());
         var10 = this.createSubscriptionQueue(this.getSubscriptionQueueName(var3, var4, this.getName(), this.getBackEnd().getName()), var6, var3.getSubscriptionSharingPolicy());
      }

      if (var6 && this.backEnd.isStoreEnabled()) {
         DurableSubscription var11 = (DurableSubscription)var4;
         if (!WLSPrincipals.isKernelUsername(JMSSecurityHelper.getSimpleAuthenticatedName())) {
            this.getJMSDestinationSecurity().checkReceivePermission(JMSSecurityHelper.getCurrentSubject());
         }
      }

      boolean var34 = false;
      BEConsumerImpl var12 = null;
      Object var13 = null;
      String var14 = BEConsumerImpl.clientIdPlusName(var3.getClientId(), var3.getName(), var3.getClientIdPolicy(), this.getName(), this.getBackEnd().getName());

      try {
         var12 = new BEConsumerImpl(var1, this, var10, var8, false, var3);
         this.addConsumer(var12);
         if (!var7) {
            var13 = var12.getSubscription();
            boolean var15 = true;
            if (var13 != null) {
               synchronized(var13) {
                  if (((Subscription)var13).getSubscribersCount() > 1 && var3.getSubscriptionSharingPolicy() == 1) {
                     var15 = false;
                  }
               }
            }

            if (var15) {
               boolean var16 = var6 || JMSService.getJMSService().shouldMessageLogNonDurableSubscriber();
               this.activateSubscriptionQueue(var10, var12, var9, var16, var6);
            }

            var34 = true;
         }
      } finally {
         if (!var34) {
            deleteFailedConsumer(var12, var6);
         } else if (var6) {
            synchronized(this.backEnd.getDurableSubscriptionsMap()) {
               if (var13 == null) {
                  var13 = this.backEnd.getDurableSubscription(var14);
               }

               if (var13 != null) {
                  synchronized(var13) {
                     if (((DurableSubscription)var13).isPending()) {
                        ((DurableSubscription)var13).setPending(false);
                        if (((DurableSubscription)var13).hasWaits()) {
                           var13.notifyAll();
                        }
                     }
                  }
               }
            }
         }

      }

      if (var2) {
         var12.start();
      }

      if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
         JMSDebug.JMSBackEnd.debug("Created a new consumer with ID " + var3.getConsumerId() + " on topic " + this.name);
      }

      return var12;
   }

   static void deleteFailedConsumer(BEConsumerImpl var0, boolean var1) {
      if (var0 != null) {
         try {
            if (var1) {
               var0.doDurableSubscriptionCleanup(var0.getDestination().getBackEnd().getDurableSubscription(var0.getName()), true, false, true, false);
            } else {
               var0.close(0L);
            }
         } catch (JMSException var3) {
         }

      }
   }

   void recoverDurableSubscription(PersistentHandle var1, String var2, int var3, String var4, JMSSQLExpression var5) throws JMSException {
      if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
         JMSDebug.JMSBackEnd.debug("Recovered a durable subscription " + var4 + " on topic " + this.name);
      }

      Queue var6 = this.createSubscriptionQueue(getSubscriptionQueueName((JMSID)null, var2, var3, var4, 0, this.getName(), this.backEnd.getName()), true);
      BEConsumerCreateRequest var7 = new BEConsumerCreateRequest((JMSID)null, (JMSID)null, (JMSID)null, var2, var3, var4, (JMSID)null, var5.getSelector(), var5.isNoLocal(), 0, 0, this.getRedeliveryDelay(), (String)null, (ConsumerReconnectInfo)null, 0);
      BEConsumerImpl var8 = null;

      try {
         var8 = new BEConsumerImpl((BESessionImpl)null, this, var6, 0, true, var7);
         var8.close(0L);
         var8.setPersistentHandle(var1);
         this.addConsumer(var8);
         this.activateSubscriptionQueue(var6, var8, var5, true, true);
         if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
            JMSDebug.JMSBackEnd.debug("Recovered a durable subscription on topic " + this.name);
         }
      } finally {
         if (var8 != null && var8.getSubscription() != null) {
            synchronized(var8.getSubscription()) {
               ((DurableSubscription)var8.getSubscription()).resetSubscribersCount();
               if (((DurableSubscription)var8.getSubscription()).isPending()) {
                  ((DurableSubscription)var8.getSubscription()).setPending(false);
                  if (((DurableSubscription)var8.getSubscription()).hasWaits()) {
                     var8.getSubscription().notifyAll();
                  }
               }
            }
         }

      }

   }

   BEConnectionConsumerImpl createConnectionConsumer(JMSID var1, ServerSessionPool var2, String var3, String var4, String var5, boolean var6, int var7, long var8, boolean var10, boolean var11) throws JMSException {
      this.checkShutdownOrSuspendedNeedLock("create connection consumer");
      int var12 = 0;
      if (var3 == null || var4 == null) {
         var12 |= 4;
      }

      if (var10) {
         var12 |= 8;
         var4 = null;
      }

      JMSSQLExpression var13 = new JMSSQLExpression(var5);
      Queue var14 = this.createSubscriptionQueue(getSubscriptionQueueName(var1, var3, var4), var10);
      BEConnectionConsumerImpl var15 = null;

      try {
         var15 = new BEConnectionConsumerImpl(var1, this, var2, var14, var5, var6, var3, var4, var7, var8, var12);
         this.addConsumer(var15);
         boolean var16 = var10 || JMSService.getJMSService().shouldMessageLogNonDurableSubscriber();
         this.activateSubscriptionQueue(var14, var15, var13, var16, var10);
      } catch (JMSException var17) {
         deleteFailedConsumer(var15, var10);
         throw var17;
      }

      if (var11) {
         var15.start();
      }

      if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
         JMSDebug.JMSBackEnd.debug("Created a new ConnectionConsumer with ID " + var1 + " on topic " + this.name);
      }

      return var15;
   }

   DurableSubscription findDurableSubscriber(String var1, String var2, String var3, boolean var4, int var5, int var6, int var7) throws JMSException {
      String var8 = BEConsumerImpl.clientIdPlusName(var1, var2, var6, this.getName(), this.getBackEnd().getName());
      DurableSubscription var9 = this.backEnd.getDurableSubscription(var8);
      DurableSubscription var10 = var9;
      if (var9 != null && var5 == 1) {
         DurableSubscription var11;
         if (var6 != 0) {
            var11 = new DurableSubscription(var8, this.destinationImpl, var3, var4, var6, var7);
            if (var9.equals(var11)) {
               if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
                  JMSDebug.JMSBackEnd.debug("Found existing durable subscription " + var8 + " on topic " + this.name);
               }

               return var9;
            } else if (var9.getSubscribersCount() > 0) {
               throw new JMSException("Cannot change the details of a durable subscription when it is in use");
            } else {
               try {
                  if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
                     JMSDebug.JMSBackEnd.debug("Deleting existing durable subscription " + var8 + " on topic " + this.name);
                  }

                  var10.getConsumer().delete(false, true);
                  return null;
               } catch (JMSException var15) {
                  throw new weblogic.jms.common.JMSException("Old subscription can not be removed", var15);
               }
            }
         } else {
            var10 = DSManager.manager().lookup(BEConsumerImpl.JNDINameForSubscription(var8));
            if (var10 != null) {
               var11 = new DurableSubscription(var8, this.destinationImpl, var3, var4, var6, var7);
               Vector var12 = var10.getDSVector();

               for(int var13 = 0; var13 < var12.size(); ++var13) {
                  DurableSubscription var14 = (DurableSubscription)var12.elementAt(var13);
                  if (var14.equalsForSerialized(var11)) {
                     if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
                        JMSDebug.JMSBackEnd.debug("Found existing durable subscription " + var8 + " on topic " + this.name);
                     }

                     return var9;
                  }
               }
            }

            return null;
         }
      } else {
         if (JMSDebug.JMSBackEnd.isDebugEnabled() && var9 != null) {
            JMSDebug.JMSBackEnd.debug("Found existing durable subscription " + var8 + " on topic " + this.name);
         }

         return var9;
      }
   }

   NonDurableSubscription findNonDurableSubscriber(String var1, String var2, boolean var3, int var4, int var5) throws JMSException {
      NonDurableSubscription var6 = new NonDurableSubscription(var1, this.destinationImpl, var2, var3, var4, var5);
      return this.getSharableNonDurableSubscription(var6);
   }

   public synchronized void addConsumer(BEConsumerCommon var1) throws JMSException {
      super.addConsumer(var1);
      BEConsumerImpl var2 = (BEConsumerImpl)var1;
      if (var2.isDurable()) {
         this.durableRuntimeMBeans.put(var2.getName(), var2.getDurableSubscriberMbean());
      }

      if (var2.isMulticastSubscriber()) {
         ++this.multicastConsumerCount;
         if (this.multicastConsumerCount > 0 && this.multicastConsumer == null) {
            this.startMulticastConsumer();
         }
      }

   }

   public void unsubscribe(Queue var1, boolean var2) throws JMSException {
      try {
         KernelRequest var3 = new KernelRequest();
         synchronized(var1) {
            this.topic.unsubscribe(var1, var3);
            var3.getResult();
            var3 = new KernelRequest();
            var1.delete(var3);
         }

         if (var2) {
            var3.getResult();
         }

      } catch (KernelException var7) {
         throw new weblogic.jms.common.JMSException("Error deleting a topic subscription", var7);
      }
   }

   public void removeConsumer(BEConsumerImpl var1, boolean var2) throws JMSException {
      synchronized(this) {
         if (var1.isDurable() && var1.getPersistentHandle() != null) {
            this.durableRuntimeMBeans.remove(var1.getName());
            this.backEnd.getDurableSubscriptionStore().deleteSubscription(var1.getPersistentHandle());
         }

         if (var1.isMulticastSubscriber()) {
            --this.multicastConsumerCount;
            if (this.multicastConsumerCount == 0) {
               this.stopMulticastConsumer();
            }
         }
      }

      if (!var1.isDurable() && var1.getClientID() != null) {
         this.removeSharableNonDurableSubscriber(var1);
      }

      if (!var1.isMulticastSubscriber() && (var1.getSubscription() == null || var1.getSubscription().getSubscribersCount() == 0)) {
         this.unsubscribe(var1.getUnsubscribeQueue(), var2);
      }

      synchronized(this) {
         if (!this.consumers.contains(var1)) {
            return;
         }
      }

      super.removeConsumer(var1, var2);
   }

   public void removeConsumer(BEConsumerImpl var1, boolean var2, boolean var3) throws JMSException {
      if (var3) {
         super.removeConsumer(var1, var2);
      } else {
         this.removeConsumer(var1, var2);
      }

   }

   private synchronized List getConsumerQueues() {
      ArrayList var1 = new ArrayList(this.consumers.size() + 1);
      Iterator var2 = this.consumers.iterator();

      while(var2.hasNext()) {
         Queue var3 = ((BEConsumerImpl)var2.next()).getKernelQueue();
         if (var3 != null) {
            var1.add(var3);
         }
      }

      if (this.multicastConsumer != null) {
         var1.add(this.multicastConsumer.getQueue());
      }

      return var1;
   }

   protected void suspendKernelDestination(int var1) throws JMSException {
      super.suspendKernelDestination(var1);
      Iterator var2 = this.getConsumerQueues().iterator();
      KernelException var3 = null;

      while(var2.hasNext()) {
         try {
            ((Queue)var2.next()).suspend(var1);
         } catch (KernelException var5) {
            var3 = var5;
         }
      }

      if (var3 != null) {
         throw new weblogic.jms.common.JMSException(var3);
      }
   }

   protected void resumeKernelDestination(int var1) throws JMSException {
      super.resumeKernelDestination(var1);
      KernelException var2 = null;
      Iterator var3 = this.getConsumerQueues().iterator();

      while(var3.hasNext()) {
         try {
            ((Queue)var3.next()).resume(var1);
         } catch (KernelException var5) {
            var2 = var5;
         }
      }

      if (var2 != null) {
         throw new weblogic.jms.common.JMSException(var2);
      }
   }

   protected void closeAllConsumers(String var1) {
      super.closeAllConsumers(var1);
      this.stopMulticastConsumer();
   }

   public String getMulticastAddress() {
      return this.multicastAddress;
   }

   public void setMulticastAddress(String var1) {
      if (JMSDebug.JMSBackEnd.isDebugEnabled() && var1 != null && var1.length() != 0) {
         JMSDebug.JMSBackEnd.debug("Topic " + this.name + " setting multicastAddress to " + var1);
      }

      this.multicastAddress = var1;
   }

   public int getMulticastPort() {
      return this.multicastPort;
   }

   public void setMulticastPort(int var1) {
      this.multicastPort = var1;
   }

   public void setMulticastTimeToLive(int var1) {
      this.multicastTTL = (byte)var1;
   }

   public int getMulticastTimeToLive() {
      return this.multicastTTL;
   }

   public void setMulticastGroup(InetAddress var1) {
      if (JMSDebug.JMSBackEnd.isDebugEnabled() && var1 != null) {
         JMSDebug.JMSBackEnd.debug("Topic " + this.name + " setting multicast group to " + var1);
      }

      this.multicastGroup = var1;
   }

   public synchronized JMSDurableSubscriberRuntimeMBean[] getDurableSubscribers() {
      if (this.durableRuntimeMBeans.isEmpty()) {
         return new JMSDurableSubscriberRuntimeMBean[0];
      } else {
         JMSDurableSubscriberRuntimeMBean[] var1 = new JMSDurableSubscriberRuntimeMBean[this.durableRuntimeMBeans.size()];
         this.durableRuntimeMBeans.values().toArray(var1);
         return var1;
      }
   }

   public void createDurableSubscriber(String var1, String var2, String var3, boolean var4) throws JMSException {
      this.createDurableSubscriber(var1, 0, var2, var3, var4, 0);
   }

   public void createDurableSubscriber(String var1, int var2, String var3, String var4, boolean var5, int var6) throws JMSException {
      this.checkShutdownOrSuspendedNeedLock("create durable subscriber");
      JMSService.getJMSService().reserveClientID(var1);

      try {
         DurableSubscription var7 = this.findDurableSubscriber(var1, var3, var4, var5, 1, var2, var6);
         if (var7 != null) {
            if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
               JMSDebug.JMSBackEnd.debug("createDurableSubscriber(): found a sub: sub's sharingpolicy = " + var7.getSubscriptionSharingPolicy() + "request's sharingpolicy = " + var6);
            }

            if (var6 != var7.getSubscriptionSharingPolicy() && var7.getSubscribersCount() != 0) {
               throw new JMSException("Cannot change the sharing policy on an active subscriptions");
            }
         }

         if (var7 == null || var6 == 1) {
            BEConsumerCreateRequest var8 = new BEConsumerCreateRequest((JMSID)null, (JMSID)null, (JMSID)null, var1, var2, var3, (JMSID)null, var4, var5, 0, 0, -1L, (String)null, (ConsumerReconnectInfo)null, var6);
            BEConsumerImpl var9 = this.createConsumer((BESessionImpl)null, false, var8, var7);
            var9.close(0L);
         }
      } finally {
         JMSService.releaseClientID(var1);
      }

   }

   private synchronized void startMulticastConsumer() throws JMSException {
      if (this.multicastConsumer == null) {
         if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
            JMSDebug.JMSBackEnd.debug("Starting multicasting for the JMS topic " + this.getName());
         }

         JMSID var1 = JMSService.getJMSService().getNextId();
         JMSTMSocket var2 = JMSService.getJMSService().getMulticastSocket();
         if (var2 == null) {
            throw new JMSException("Failed to start multicasting for JMS Topic " + this.getName());
         } else {
            Queue var3 = this.createSubscriptionQueue(var1.toString(), false);
            this.multicastConsumer = new BEMulticastConsumer(this.backEnd, var3, this.destinationImpl, this.multicastGroup, this.multicastPort, this.multicastTTL, var2);
            this.activateSubscriptionQueue(var3, (BEConsumerImpl)null, new JMSSQLExpression(), false, false);

            try {
               this.multicastConsumer.start();
            } catch (JMSException var5) {
               this.multicastConsumer = null;
               throw var5;
            }
         }
      }
   }

   private synchronized void stopMulticastConsumer() {
      if (this.multicastConsumer != null) {
         if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
            JMSDebug.JMSBackEnd.debug("Stopping multicasting for the JMS topic");
         }

         try {
            this.multicastConsumer.stop();
            this.unsubscribe(this.multicastConsumer.getQueue(), false);
            this.multicastConsumer = null;
         } catch (JMSException var2) {
            if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
               JMSDebug.JMSBackEnd.debug("Error stopping multicast consumer: " + var2);
            }
         }

      }
   }

   public final void setMessageLoggingEnabled(boolean var1) {
      if ((!super.isMessageLoggingEnabled() || !var1) && (super.isMessageLoggingEnabled() || var1)) {
         super.setMessageLoggingEnabled(var1);

         try {
            if (var1 && !this.backEnd.isMemoryLow()) {
               this.resumeMessageLogging();
            } else if (!var1) {
               this.suspendMessageLogging();
            }
         } catch (JMSException var3) {
         }

      }
   }

   public void resumeMessageLogging() throws JMSException {
      this.messageLogging = true;
      Map var1 = this.getConsumersClone();
      Iterator var2 = var1.values().iterator();

      while(true) {
         BEConsumerImpl var3;
         do {
            if (!var2.hasNext()) {
               return;
            }

            var3 = (BEConsumerImpl)var2.next();
         } while(!JMSService.getJMSService().shouldMessageLogNonDurableSubscriber() && !var3.isDurable());

         addPropertyFlags(var3.getKernelQueue(), "Logging", 15);
      }
   }

   public void suspendMessageLogging() throws JMSException {
      this.messageLogging = false;
      Map var1 = this.getConsumersClone();
      Iterator var2 = var1.values().iterator();

      while(true) {
         BEConsumerImpl var3;
         do {
            if (!var2.hasNext()) {
               return;
            }

            var3 = (BEConsumerImpl)var2.next();
         } while(!JMSService.getJMSService().shouldMessageLogNonDurableSubscriber() && !var3.isDurable());

         removePropertyFlags(var3.getKernelQueue(), "Logging", 15);
      }
   }

   public void setQuota(Quota var1) throws BeanUpdateFailedException {
      HashMap var2 = new HashMap();

      try {
         var2.put("Quota", var1);
         this.getKernelDestination().setProperties(var2);
         Map var3 = this.getConsumersClone();
         Iterator var4 = var3.keySet().iterator();

         while(var4.hasNext()) {
            BEConsumerImpl var5 = (BEConsumerImpl)var4.next();
            if (!var5.isMulticastSubscriber()) {
               Queue var6 = var5.getKernelQueue();
               var6.setProperties(var2);
            }
         }

      } catch (KernelException var7) {
         throw new BeanUpdateFailedException("Messaging Kernel failed to act on the quota" + var1);
      }
   }

   NonDurableSubscription getSharableNonDurableSubscription(NonDurableSubscription var1) {
      synchronized(this.nonDurableSubscriptions) {
         return (NonDurableSubscription)this.nonDurableSubscriptions.get(var1);
      }
   }

   NonDurableSubscription addSharableNonDurableSubscriber(NonDurableSubscription var1) {
      synchronized(this.nonDurableSubscriptions) {
         NonDurableSubscription var3 = this.getSharableNonDurableSubscription(var1);
         if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
            JMSDebug.JMSBackEnd.debug("BETopicImpl: addSharableNonDurableSubscriber subFound=" + var3);
         }

         if (var3 != null && var3.equals(var1)) {
            var3.addSubscriber((BEConsumerImpl)null);
            return var3;
         } else {
            var1.addSubscriber((BEConsumerImpl)null);
            this.nonDurableSubscriptions.put(var1, var1);
            return var1;
         }
      }
   }

   private void removeSharableNonDurableSubscriber(BEConsumerImpl var1) {
      NonDurableSubscription var2 = new NonDurableSubscription(var1.getClientID(), var1.getDestination().getDestinationImpl(), var1.getSelector(), var1.getNoLocal(), var1.getClientIdPolicy(), var1.getSubscriptionSharingPolicy());
      synchronized(this.nonDurableSubscriptions) {
         NonDurableSubscription var4 = this.getSharableNonDurableSubscription(var2);
         if (var4 != null) {
            synchronized(var4) {
               var4.removeSubscriber((JMSID)null);
               if (var4.getSubscribersCount() <= 0) {
                  this.nonDurableSubscriptions.remove(var4);
               }
            }

         }
      }
   }

   private String getNextSharableNonDurableSubName(String var1) {
      return "_weblogic.jms.sharable.NDS." + var1 + "." + JMSService.getJMSService().getNextId().toString() + "@" + this.name + "@" + this.backEnd.getName();
   }

   public boolean isMessageLogging() {
      return this.messageLogging;
   }

   public void dump(JMSDiagnosticImageSource var1, XMLStreamWriter var2) throws XMLStreamException, DiagnosticImageTimeoutException {
      var1.checkTimeout();
      var2.writeStartElement("Topic");
      super.dump(var1, var2);
      var2.writeEndElement();
   }
}
