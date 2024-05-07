package weblogic.webservice.async;

import java.util.EventObject;

/** @deprecated */
public class InvokeCompletedEvent extends EventObject {
   private FutureResult futureResult;

   public InvokeCompletedEvent(Object var1) {
      super(var1);
   }

   public FutureResult getFutureResult() {
      return this.futureResult;
   }

   public void setFutureResult(FutureResult var1) {
      this.futureResult = var1;
   }
}
