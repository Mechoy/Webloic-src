package weblogic.uddi.client.structures.request;

import java.util.Vector;
import weblogic.uddi.client.structures.datatypes.TModelKey;

public class GetTModelDetail extends Request {
   private Vector tModelKeyVector = new Vector();

   public void addTModelKey(String var1) {
      TModelKey var2 = new TModelKey(var1);
      this.tModelKeyVector.add(var2);
   }

   public void setTModelKeyVector(Vector var1) {
      this.tModelKeyVector = var1;
   }

   public Vector getTModelKeyVector() {
      return this.tModelKeyVector;
   }
}
