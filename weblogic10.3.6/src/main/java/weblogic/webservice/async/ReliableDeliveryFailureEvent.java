package weblogic.webservice.async;

import weblogic.webservice.saf.StoreForwardException;

/** @deprecated */
public class ReliableDeliveryFailureEvent extends InvokeCompletedEvent {
   private String message;
   private StoreForwardException exception;

   public ReliableDeliveryFailureEvent(StoreForwardException var1) {
      super(var1);
      this.exception = var1;
      this.message = var1.getMessage();
   }

   public ReliableDeliveryFailureEvent(String var1) {
      super(var1);
      this.message = var1;
   }

   public String getErrorMessage() {
      return this.message;
   }

   public StoreForwardException getException() {
      return this.exception;
   }
}
