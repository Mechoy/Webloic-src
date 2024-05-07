package weblogic.wsee.reliability;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.soap.SOAPMessage;
import weblogic.wsee.async.AsyncPostCallContext;

class ReliabilityErrorContextImpl implements ReliabilityErrorContext {
   private final String operationName;
   private final String targetName;
   private final SOAPMessage soapMessage;
   private final String errorMessage;
   private final List<Throwable> errors;
   private final AsyncPostCallContext apc;

   ReliabilityErrorContextImpl(String var1, String var2, SOAPMessage var3, String var4, List var5, AsyncPostCallContext var6) {
      this.operationName = var1;
      this.targetName = var2;
      this.soapMessage = var3;
      this.errorMessage = var4;
      this.errors = new ArrayList();
      if (var5 != null) {
         Iterator var7 = var5.iterator();

         while(var7.hasNext()) {
            Object var8 = var7.next();
            this.errors.add((Throwable)var8);
         }
      }

      this.apc = var6;
   }

   public String getOperationName() {
      return this.operationName;
   }

   public String getTargetName() {
      return this.targetName;
   }

   public SOAPMessage getSOAPMessage() {
      return this.soapMessage;
   }

   public ReliableDeliveryException getFault() {
      return new ReliableDeliveryException(this.errorMessage);
   }

   public List<Throwable> getFaults() {
      return this.errors;
   }

   public String getFaultSummaryMessage() {
      return this.errorMessage;
   }

   public AsyncPostCallContext getAsyncPostCallContext() {
      return this.apc;
   }
}
