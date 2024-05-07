package weblogic.jms.common;

public class MessageList {
   protected MessageReference first;
   protected MessageReference last;
   private int count;

   public void addLast(MessageReference var1) {
      var1.setPrev(this.last);
      var1.setNext((MessageReference)null);
      if (this.last == null) {
         this.first = var1;
      } else {
         this.last.setNext(var1);
      }

      this.last = var1;
      this.incSize();
   }

   public void addFirst(MessageReference var1) {
      var1.setPrev((MessageReference)null);
      var1.setNext(this.first);
      if (this.first == null) {
         this.last = var1;
      } else {
         this.first.setPrev(var1);
      }

      this.first = var1;
      this.incSize();
   }

   public final void remove(MessageReference var1) {
      if (var1.getPrev() == null) {
         this.first = var1.getNext();
      } else {
         var1.getPrev().setNext(var1.getNext());
      }

      if (var1.getNext() == null) {
         this.last = var1.getPrev();
      } else {
         var1.getNext().setPrev(var1.getPrev());
      }

      var1.setNext((MessageReference)null);
      var1.setPrev((MessageReference)null);
      --this.count;
   }

   public final MessageReference removeFirst() {
      MessageReference var1 = this.first;
      if (var1 != null) {
         --this.count;
         this.first = var1.getNext();
         var1.setNext((MessageReference)null);
         if (this.first == null) {
            this.last = null;
         } else {
            this.first.setPrev((MessageReference)null);
         }
      }

      return var1;
   }

   public final MessageReference removeBeforeSequenceNumber(long var1) {
      MessageReference var3 = this.first;

      for(int var4 = 0; var3 != null; var3 = var3.getNext()) {
         if (var3.getSequenceNumber() == var1) {
            if ((this.first = var3.getNext()) == null) {
               this.last = null;
            } else {
               this.first.setPrev((MessageReference)null);
            }

            var3.setNext((MessageReference)null);
            ++var4;
            this.count -= var4;
            break;
         }

         ++var4;
      }

      return var3;
   }

   public final MessageReference removeAll() {
      MessageReference var1 = this.first;
      this.first = this.last = null;
      this.count = 0;
      return var1;
   }

   public final MessageReference getFirst() {
      return this.first;
   }

   public final MessageReference getLast() {
      return this.last;
   }

   public final boolean isEmpty() {
      return this.first == null;
   }

   public final int getSize() {
      return this.count;
   }

   protected void incSize() {
      ++this.count;
   }
}
