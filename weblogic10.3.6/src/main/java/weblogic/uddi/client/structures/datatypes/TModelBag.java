package weblogic.uddi.client.structures.datatypes;

import java.util.Vector;

public class TModelBag {
   private Vector tModelKey = new Vector();

   public void addTModelKey(String var1) {
      TModelKey var2 = new TModelKey(var1);
      this.tModelKey.add(var2);
   }

   public Vector getTModelKeyVector() {
      return this.tModelKey;
   }

   public void setTModelKeyVector(Vector var1) {
      this.tModelKey = var1;
   }
}
