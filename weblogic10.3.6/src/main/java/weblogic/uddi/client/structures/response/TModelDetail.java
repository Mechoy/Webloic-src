package weblogic.uddi.client.structures.response;

import java.util.Vector;

public class TModelDetail extends ListResponse {
   private Vector tModelVector = new Vector();

   public Vector getTModelVector() {
      return this.tModelVector;
   }

   public void setTModelVector(Vector var1) {
      this.tModelVector = var1;
   }
}
