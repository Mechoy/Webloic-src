package weblogic.jms.common;

public class MessageReference {
   private MessageImpl message;
   private long sequenceNumber;
   private int deliveryCount = 1;
   private MessageReference prev;
   private MessageReference next;

   public MessageReference(MessageImpl var1) {
      this.message = var1;
      this.deliveryCount = var1.getDeliveryCount();
   }

   public MessageReference(MessageReference var1) {
      this.message = var1.message;
      this.deliveryCount = var1.deliveryCount;
   }

   public final void setMessage(MessageImpl var1) {
      this.message = var1;
   }

   public final MessageImpl getMessage() {
      return this.message;
   }

   public final void setSequenceNumber(long var1) {
      this.sequenceNumber = var1;
   }

   public final long getSequenceNumber() {
      return this.sequenceNumber;
   }

   public final void setPrev(MessageReference var1) {
      this.prev = var1;
   }

   public final MessageReference getPrev() {
      return this.prev;
   }

   public final void setNext(MessageReference var1) {
      this.next = var1;
   }

   public final MessageReference getNext() {
      return this.next;
   }

   public final int getDeliveryCount() {
      return this.deliveryCount;
   }

   public final void incrementDeliveryCount() {
      if (this.deliveryCount != -1) {
         ++this.deliveryCount;
      }

   }

   public final boolean getRedelivered() {
      return this.deliveryCount != 1;
   }

   public final void setDeliveryCount(int var1) {
      this.deliveryCount = var1;
   }

   public void prepareForCache() {
      if (this.message != null) {
         this.message.setMessageReference((MessageReference)null);
      }

      this.message = null;
      this.sequenceNumber = 0L;
      this.deliveryCount = 1;
      this.prev = null;
      this.next = null;
   }

   public final void reset(MessageImpl var1) {
      this.message = var1;
      if (var1.getJMSRedelivered()) {
         this.deliveryCount = -1;
      } else {
         this.deliveryCount = 1;
      }

   }

   public String toString() {
      return "(mRef id=" + this.message.getId() + ")";
   }
}
