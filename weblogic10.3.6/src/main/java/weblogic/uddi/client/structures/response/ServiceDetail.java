package weblogic.uddi.client.structures.response;

import java.util.Vector;

public class ServiceDetail extends ListResponse {
   private Vector businessServiceVector = new Vector();

   public Vector getBusinessServiceVector() {
      return this.businessServiceVector;
   }

   public void setBusinessServiceVector(Vector var1) {
      this.businessServiceVector = var1;
   }
}
