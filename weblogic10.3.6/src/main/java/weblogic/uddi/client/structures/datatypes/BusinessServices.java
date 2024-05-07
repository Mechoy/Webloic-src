package weblogic.uddi.client.structures.datatypes;

import java.util.Vector;

public class BusinessServices {
   private Vector businessService = new Vector();

   public void addBusinessService(BusinessService var1) {
      this.businessService.add(var1);
   }

   public void setBusinessServiceVector(Vector var1) {
      this.businessService = var1;
   }

   public Vector getBusinessServiceVector() {
      return this.businessService;
   }
}
