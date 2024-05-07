package weblogic.jms.client;

import weblogic.jms.common.MessageImpl;
import weblogic.jms.common.MessageReference;

final class JMSMessageReference extends MessageReference {
   private ConsumerInternal consumer;

   JMSMessageReference(MessageImpl var1, ConsumerInternal var2) {
      super(var1);
      this.consumer = var2;
   }

   ConsumerInternal getConsumer() {
      return this.consumer;
   }

   public void prepareForCache() {
      super.prepareForCache();
      this.consumer = null;
   }

   public void reset(MessageImpl var1, ConsumerInternal var2) {
      super.reset(var1);
      this.consumer = var2;
   }

   public String toString() {
      return "(JMSmRef id=" + this.getMessage().getId() + " consumer=" + (this.consumer == null ? "null" : "" + this.consumer.getJMSID()) + ")";
   }
}
