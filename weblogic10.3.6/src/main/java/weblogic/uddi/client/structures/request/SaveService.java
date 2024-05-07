package weblogic.uddi.client.structures.request;

import java.util.Vector;
import weblogic.uddi.client.structures.datatypes.BusinessService;

public class SaveService extends UpdateRequest {
   private Vector businessServiceVector = new Vector();

   public void addBusinessService(BusinessService var1) {
      this.businessServiceVector.add(var1);
   }

   public void setBusinessServiceVector(Vector var1) {
      this.businessServiceVector = var1;
   }

   public Vector getBusinessServiceVector() {
      return this.businessServiceVector;
   }
}
