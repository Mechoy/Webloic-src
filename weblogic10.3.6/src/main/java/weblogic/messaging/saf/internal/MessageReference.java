package weblogic.messaging.saf.internal;

import java.util.ArrayList;
import weblogic.messaging.kernel.MessageElement;
import weblogic.messaging.saf.SAFRequest;
import weblogic.messaging.saf.common.SAFRequestImpl;

public final class MessageReference {
   private final SAFRequest message;
   private MessageElement element;
   private final ArrayList faultCodes = new ArrayList();
   private RetryController retryController;
   private MessageReference prev;
   private MessageReference next;

   MessageReference(MessageElement var1, double var2, long var4, long var6) {
      this.element = var1;
      this.message = (SAFRequestImpl)var1.getMessage();
      this.retryController = new RetryController(var4, var6, var2);
   }

   MessageReference(SAFRequest var1) {
      this.message = var1;
   }

   SAFRequest getMessage() {
      return this.message;
   }

   MessageElement getElement() {
      return this.element;
   }

   long getSequenceNumber() {
      return this.message.getSequenceNumber();
   }

   long getNextRetryDelay() {
      return this.retryController.getNextRetry();
   }

   void setFaultCode(int var1) {
      this.faultCodes.add(new Integer(var1));
   }

   ArrayList getFaultCodes() {
      return this.faultCodes;
   }

   boolean isExpired() {
      return ((SAFRequestImpl)this.message).isExpired();
   }

   synchronized boolean hasBeenHandled() {
      return this.message.getPayload() == null;
   }

   void setPrev(MessageReference var1) {
      this.prev = var1;
   }

   MessageReference getPrev() {
      return this.prev;
   }

   void setNext(MessageReference var1) {
      this.next = var1;
   }

   MessageReference getNext() {
      return this.next;
   }
}
