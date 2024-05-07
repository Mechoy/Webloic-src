package weblogic.auddi.uddi.datastructure;

import java.io.Serializable;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.util.Logger;

public class BusinessServices extends UDDIList implements Serializable {
   public void add(BusinessService var1) throws UDDIException {
      super.add(var1);
   }

   public BusinessService getFirst() {
      return (BusinessService)super.getVFirst();
   }

   public BusinessService getNext() {
      return (BusinessService)super.getVNext();
   }

   public boolean containsService(BusinessService var1) {
      Logger.debug("service : " + var1);
      if (var1 == null) {
         return false;
      } else {
         Logger.debug("service-key : " + var1.getServiceKey());

         for(BusinessService var2 = this.getFirst(); var2 != null; var2 = this.getNext()) {
            if (var2.getServiceKey() != null && var2.getServiceKey().equals(var1.getServiceKey())) {
               return true;
            }
         }

         return false;
      }
   }

   public String toXML() {
      return super.toXML("businessServices");
   }
}
