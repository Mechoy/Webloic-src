package weblogic.jms.backend;

import weblogic.messaging.util.DeliveryList;
import weblogic.utils.concurrent.atomic.AtomicFactory;
import weblogic.utils.concurrent.atomic.AtomicLong;

public abstract class BEDeliveryList extends DeliveryList {
   private final AtomicLong nextSequenceNumber = AtomicFactory.createAtomicLong();
   protected BackEnd backEnd;

   protected BEDeliveryList(BackEnd var1) {
      this.nextSequenceNumber.set(1L);
      this.setBackEnd(var1);
   }

   void setBackEnd(BackEnd var1) {
      if (var1 != null) {
         this.backEnd = var1;
         this.setWorkManager(var1.getAsyncPushWorkManager());
      }

   }

   BackEnd getBackEnd() {
      return this.backEnd;
   }

   protected void setSequenceNumber(long var1) {
      this.nextSequenceNumber.set(var1);
   }

   long getSequenceNumber() {
      return this.nextSequenceNumber.get();
   }

   long getNextSequenceNumber() {
      return this.nextSequenceNumber.getAndIncrement();
   }
}
