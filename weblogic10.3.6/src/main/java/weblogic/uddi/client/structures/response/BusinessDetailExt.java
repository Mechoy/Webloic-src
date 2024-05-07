package weblogic.uddi.client.structures.response;

import java.util.Vector;
import weblogic.uddi.client.structures.datatypes.BusinessEntityExt;

public class BusinessDetailExt extends ListResponse {
   private Vector businessEntityExtVector = new Vector();

   public void addBusinessEntityExt(BusinessEntityExt var1) {
      this.businessEntityExtVector.add(var1);
   }

   public void setBusinessEntityExtVector(Vector var1) {
      this.businessEntityExtVector = var1;
   }

   public Vector getBusinessEntityExtVector() {
      return this.businessEntityExtVector;
   }
}
