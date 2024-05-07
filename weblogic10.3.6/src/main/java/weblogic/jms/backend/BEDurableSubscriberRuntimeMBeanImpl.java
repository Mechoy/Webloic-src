package weblogic.jms.backend;

import javax.jms.JMSException;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.OpenDataException;
import weblogic.jms.common.DurableSubscription;
import weblogic.jms.common.JMSConstants;
import weblogic.jms.extensions.DestinationInfo;
import weblogic.jms.extensions.WLDestination;
import weblogic.management.ManagementException;
import weblogic.management.runtime.JMSDestinationRuntimeMBean;
import weblogic.management.runtime.JMSDurableSubscriberRuntimeMBean;

public final class BEDurableSubscriberRuntimeMBeanImpl extends BEMessageManagementRuntimeDelegate implements JMSDurableSubscriberRuntimeMBean {
   static final long serialVersionUID = -8042739557848839705L;
   private final BEConsumerCommon consumer;
   private final BEDestinationImpl destination;
   private final DurableSubscription subscription;

   public BEDurableSubscriberRuntimeMBeanImpl(String var1, BEDestinationImpl var2, BEConsumerCommon var3) throws ManagementException {
      super(var1, var2.getRuntimeMBean());
      this.consumer = var3;
      this.destination = var2;
      this.subscription = (DurableSubscription)((BEConsumerImpl)var3).getSubscription();
   }

   public String getClientID() {
      return this.consumer.getClientID();
   }

   public String getClientIDPolicy() {
      return this.consumer.getClientIdPolicy() == 1 ? JMSConstants.CLIENT_ID_POLICY_UNRESTRICTED_STRING : JMSConstants.CLIENT_ID_POLICY_RESTRICTED_STRING;
   }

   public String getSubscriptionName() {
      return this.consumer.getSubscriptionName();
   }

   public String getSelector() {
      return this.consumer.getSelector();
   }

   public boolean isNoLocal() {
      return this.consumer.getNoLocal();
   }

   public boolean isActive() {
      return this.consumer.isUsed();
   }

   public String getSubscriptionSharingPolicy() {
      return getSubscriptionSharingPolicyAsString(this.consumer.getSubscriptionSharingPolicy());
   }

   public int getSubscribersTotalCount() {
      return this.subscription.getSubscribersTotalCount();
   }

   public int getSubscribersHighCount() {
      return this.subscription.getSubscribersHighCount();
   }

   public int getSubscribersCurrentCount() {
      return this.subscription.getSubscribersCount();
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

   public long getMessagesPendingCount() {
      return this.consumer.getMessagesUnackedCount();
   }

   public long getBytesPendingCount() {
      return this.consumer.getBytesUnackedCount();
   }

   public long getMessagesCurrentCount() {
      return (long)this.consumer.getSize();
   }

   public long getMessagesHighCount() {
      return (long)this.consumer.getHighSize();
   }

   public long getMessagesReceivedCount() {
      return this.consumer.getMessagesReceivedCount();
   }

   public long getBytesCurrentCount() {
      return this.consumer.getBytesCurrentCount();
   }

   public long getLastMessagesReceivedTime() {
      return this.consumer.getLastMessagesReceivedTime();
   }

   public void purge() {
      try {
         this.deleteMessages((String)null);
      } catch (ManagementException var2) {
         throw new RuntimeException(var2.getMessage() + ".  Cause " + var2.getCause().toString());
      }
   }

   public JMSDestinationRuntimeMBean getDestinationRuntime() {
      return this.destination.getRuntimeMBean();
   }

   public CompositeData getCurrentConsumerInfo() throws OpenDataException {
      return this.consumer == null ? null : this.consumer.getCompositeData();
   }

   public void destroy() throws JMSException {
      if (this.consumer.getDurableSubscriberMbean() != null) {
         if (!this.destination.isShutdownOrSuspended()) {
            this.subscription.delete();
         }
      }
   }

   public CompositeData getDestinationInfo() throws OpenDataException {
      if (this.destination == null) {
         return null;
      } else {
         WLDestination var1 = (WLDestination)this.destination.getDestination();
         if (var1 == null) {
            return null;
         } else {
            DestinationInfo var2 = new DestinationInfo(var1);
            return var2.toCompositeData();
         }
      }
   }
}
