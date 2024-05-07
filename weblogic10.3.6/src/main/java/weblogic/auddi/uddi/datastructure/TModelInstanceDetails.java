package weblogic.auddi.uddi.datastructure;

import java.io.Serializable;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;

public class TModelInstanceDetails extends UDDIBag implements Serializable {
   public TModelInstanceDetails() {
   }

   public TModelInstanceDetails(TModelInstanceDetails var1) throws UDDIException {
      if (var1 == null) {
         throw new IllegalArgumentException(UDDIMessages.get("error.runtime.constructor.null"));
      } else {
         for(TModelInstanceInfo var2 = var1.getFirst(); var2 != null; var2 = var1.getNext()) {
            this.add(new TModelInstanceInfo(var2));
         }

      }
   }

   public void add(TModelInstanceInfo var1) {
      super.add(var1, var1.getTModelKey().getKey());
   }

   public TModelInstanceInfo getFirst() {
      return (TModelInstanceInfo)super.getMFirst();
   }

   public TModelInstanceInfo getNext() {
      return (TModelInstanceInfo)super.getMNext();
   }

   public String toXML() {
      return super.toXML("tModelInstanceDetails");
   }
}
