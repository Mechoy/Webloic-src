package weblogic.jms.common;

import weblogic.jms.backend.BEConsumerImpl;

public final class NonDurableSubscription implements Subscription {
   private String clientId;
   private DestinationImpl destinationImpl;
   private String selector;
   private boolean noLocal;
   private String subscriptionQueueName;
   private int clientIdPolicy;
   private int subscriptionSharingPolicy;
   private int numSubscribers;
   private int subscribersTotalCount;
   private int subscribersHighCount;

   public synchronized int getSubscribersTotalCount() {
      return this.subscribersTotalCount;
   }

   public synchronized int getSubscribersHighCount() {
      return this.subscribersHighCount;
   }

   public NonDurableSubscription(String var1, DestinationImpl var2, String var3, boolean var4, int var5, int var6) {
      this(var1, var2, var3, var4, var5, var6, (String)null);
   }

   public NonDurableSubscription(String var1, DestinationImpl var2, String var3, boolean var4, int var5, int var6, String var7) {
      this.clientId = var1;
      this.destinationImpl = var2;
      if (var3 != null && var3.trim().length() > 0) {
         this.selector = var3;
      }

      this.noLocal = var4;
      this.clientIdPolicy = var5;
      this.subscriptionSharingPolicy = var6;
      this.subscriptionQueueName = var7;
   }

   public synchronized void addSubscriber(BEConsumerImpl var1) {
      ++this.numSubscribers;
      ++this.subscribersTotalCount;
      if (this.numSubscribers > this.subscribersHighCount) {
         this.subscribersHighCount = this.numSubscribers;
      }

   }

   public synchronized void removeSubscriber(JMSID var1) {
      --this.numSubscribers;
   }

   public synchronized int getSubscribersCount() {
      return this.numSubscribers;
   }

   public boolean isNoLocal() {
      return this.noLocal;
   }

   public String getSelector() {
      return this.selector;
   }

   public DestinationImpl getDestinationImpl() {
      return this.destinationImpl;
   }

   public int getSubscriptionSharingPolicy() {
      return this.subscriptionSharingPolicy;
   }

   public String getSubscriptionQueueName() {
      return this.subscriptionQueueName;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof NonDurableSubscription)) {
         return false;
      } else {
         NonDurableSubscription var2 = (NonDurableSubscription)var1;
         return DurableSubscription.noLocalAndSelectorMatch(this, var2.noLocal, var2.selector) && (this.destinationImpl != null || var2.destinationImpl == null) && (this.destinationImpl == null || var2.destinationImpl != null) && this.clientId.equals(var2.clientId) && this.clientIdPolicy == var2.clientIdPolicy ? Destination.equalsForDS(this.destinationImpl, var2.destinationImpl) : false;
      }
   }

   public int hashCode() {
      int var1 = this.clientId.hashCode();
      if (this.selector != null) {
         var1 = var1 * 31 + this.selector.hashCode();
      }

      if (this.noLocal) {
         var1 = var1 * 31 + 1;
      }

      var1 = var1 * 31 + this.clientIdPolicy;
      var1 = var1 * 31 + this.destinationImpl.getName().hashCode();
      return var1;
   }

   public String toString() {
      return "NonDurableSubscription((" + this.clientId + ") " + ":" + this.clientIdPolicy + this.destinationImpl + ":" + this.selector + ":" + this.noLocal + ":" + this.subscriptionSharingPolicy + ")";
   }
}
