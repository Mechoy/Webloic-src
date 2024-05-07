package weblogic.uddi.client.structures.request;

import java.util.Vector;
import weblogic.uddi.client.structures.datatypes.BindingKey;

public class GetBindingDetail extends Request {
   private Vector bindingKeyVector = new Vector();

   public void addBindingKey(String var1) {
      BindingKey var2 = new BindingKey(var1);
      this.bindingKeyVector.add(var2);
   }

   public void setBindingKeyVector(Vector var1) {
      this.bindingKeyVector = var1;
   }

   public Vector getBindingKeyVector() {
      return this.bindingKeyVector;
   }
}
