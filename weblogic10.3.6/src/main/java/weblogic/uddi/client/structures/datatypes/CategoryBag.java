package weblogic.uddi.client.structures.datatypes;

import java.util.Vector;

public class CategoryBag {
   private Vector keyedReference = new Vector();

   public void addKeyedReference(String var1, String var2, String var3) {
      KeyedReference var4 = new KeyedReference(var1, var2, var3);
      this.keyedReference.add(var4);
   }

   public Vector getKeyedReferenceVector() {
      return this.keyedReference;
   }

   public void setKeyedReferenceVector(Vector var1) {
      this.keyedReference = var1;
   }
}
