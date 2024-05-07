package weblogic.jms.frontend;

import weblogic.jms.common.MessageImpl;

public final class UnackedMessage {
   private final FEConsumer consumer;
   private final FEProducer producer;
   private final long messageSize;
   private UnackedMessage next;
   private UnackedMessage prev;

   UnackedMessage(FEConsumer var1, FEProducer var2, MessageImpl var3) {
      this.consumer = var1;
      this.producer = var2;
      this.messageSize = var3.getPayloadSize() + (long)var3.getUserPropertySize();
   }

   UnackedMessage getNext() {
      return this.next;
   }

   UnackedMessage getPrev() {
      return this.prev;
   }

   void setNext(UnackedMessage var1) {
      this.next = var1;
   }

   void setPrev(UnackedMessage var1) {
      this.prev = var1;
   }

   void commitTransactedStatistics(FESession var1) {
      var1.getStatistics().decrementPendingCount(this.messageSize);
      if (this.consumer != null) {
         this.consumer.statistics.decrementPendingCount(this.messageSize);
         this.consumer.statistics.incrementReceivedCount(this.messageSize);
         var1.getStatistics().incrementReceivedCount(this.messageSize);
      } else {
         this.producer.decMessagesPendingCount(this.messageSize);
         this.producer.incMessagesSentCount(this.messageSize);
         var1.getStatistics().incrementSentCount(this.messageSize);
      }

   }

   void rollbackTransactedStatistics(FESession var1) {
      var1.getStatistics().decrementPendingCount(this.messageSize);
      if (this.consumer != null) {
         this.consumer.statistics.decrementPendingCount(this.messageSize);
      } else {
         this.producer.decMessagesPendingCount(this.messageSize);
      }

   }
}
