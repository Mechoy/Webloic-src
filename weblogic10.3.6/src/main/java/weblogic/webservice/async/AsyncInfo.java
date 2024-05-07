package weblogic.webservice.async;

import weblogic.webservice.WSServerService;
import weblogic.webservice.saf.StoreForwardException;

/** @deprecated */
public class AsyncInfo {
   private Object caller;
   private ResultListener resultListener;
   private boolean relDel = false;
   private boolean inOrderDel = false;

   public Object getCaller() {
      return this.caller;
   }

   public void setCaller(Object var1) {
      this.caller = var1;
   }

   public ResultListener getResultListener() {
      return this.resultListener;
   }

   public void setResultListener(ResultListener var1) {
      this.resultListener = var1;
   }

   public void setReliableDelivery(boolean var1) throws StoreForwardException {
      WSServerService.getWSServerService().verifySAFConfiguration();
      this.relDel = var1;
   }

   public boolean isReliableDelivery() {
      return this.relDel;
   }

   public void setInOrderDelivery(boolean var1) {
      this.inOrderDel = var1;
   }

   public boolean isInOrderDelivery() {
      return this.inOrderDel;
   }
}
