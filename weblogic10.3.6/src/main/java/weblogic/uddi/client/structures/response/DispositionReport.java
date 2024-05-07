package weblogic.uddi.client.structures.response;

import java.util.Vector;

public class DispositionReport extends ListResponse {
   private Vector result = new Vector();

   public Vector getResultVector() {
      return this.result;
   }

   public void setResultVector(Vector var1) {
      this.result = var1;
   }
}
