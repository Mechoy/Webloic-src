package weblogic.jms.client;

import weblogic.jms.common.MessageImpl;

public class MessageWrapper {
   private MessageWrapper next = null;
   private long proxyConsumerId;
   private long sequence;
   private int generation;
   private int deliveryCount;
   private MessageImpl messageImpl;

   MessageWrapper(long var1, int var3, long var4, int var6, MessageImpl var7) {
      this.proxyConsumerId = var1;
      this.generation = var3;
      this.sequence = var4;
      this.deliveryCount = var6;
      this.messageImpl = var7;
   }

   void next(MessageWrapper var1) {
      this.next = var1;
   }

   public MessageWrapper next() {
      return this.next;
   }

   public long getProxyId() {
      return this.proxyConsumerId;
   }

   public long getSequence() {
      return this.sequence;
   }

   public int getGeneration() {
      return this.generation;
   }

   public int getDeliveryCount() {
      return this.deliveryCount;
   }

   public MessageImpl getMessageImpl() {
      return this.messageImpl;
   }
}
