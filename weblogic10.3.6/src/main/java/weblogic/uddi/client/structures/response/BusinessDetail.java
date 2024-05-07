package weblogic.uddi.client.structures.response;

import java.util.Vector;
import weblogic.uddi.client.structures.datatypes.BusinessEntity;

public class BusinessDetail extends ListResponse {
   private Vector businessEntityVector = new Vector();

   public void addBusinessEntity(BusinessEntity var1) {
      this.businessEntityVector.add(var1);
   }

   public void setBusinessEntityVector(Vector var1) {
      this.businessEntityVector = var1;
   }

   public Vector getBusinessEntityVector() {
      return this.businessEntityVector;
   }
}
