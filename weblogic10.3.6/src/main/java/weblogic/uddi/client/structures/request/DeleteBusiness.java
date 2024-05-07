package weblogic.uddi.client.structures.request;

import java.util.Vector;
import weblogic.uddi.client.structures.datatypes.BusinessKey;

public class DeleteBusiness extends UpdateRequest {
   private Vector businessKeyVector = new Vector();

   public void addBusinessKey(String var1) {
      BusinessKey var2 = new BusinessKey(var1);
      this.businessKeyVector.add(var2);
   }

   public void setBusinessKeyVector(Vector var1) {
      this.businessKeyVector = var1;
   }

   public Vector getBusinessKeyVector() {
      return this.businessKeyVector;
   }
}
