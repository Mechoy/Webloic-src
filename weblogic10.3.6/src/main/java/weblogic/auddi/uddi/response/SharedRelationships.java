package weblogic.auddi.uddi.response;

import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.KeyedReference;
import weblogic.auddi.uddi.datastructure.TModelKey;
import weblogic.auddi.uddi.datastructure.UDDIBag;
import weblogic.auddi.util.Util;

public class SharedRelationships extends UDDIBag {
   private String m_direction = null;

   public void add(KeyedReference var1) {
      super.add(var1, var1);
   }

   public KeyedReference getFirst() {
      return (KeyedReference)super.getMFirst();
   }

   public KeyedReference getNext() {
      return (KeyedReference)super.getMNext();
   }

   public void setDirection(String var1) {
      this.m_direction = var1;
   }

   public String getDirection() {
      return this.m_direction;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof SharedRelationships)) {
         return false;
      } else {
         SharedRelationships var2 = (SharedRelationships)var1;
         boolean var3 = true;
         var3 &= Util.isEqual((Object)this.m_direction, (Object)var2.m_direction);
         var3 &= super.equals(var1);
         return var3;
      }
   }

   public String toXML() {
      String var1 = "sharedRelationships";
      String var2 = super.toXML(var1);
      if (this.m_direction != null) {
         StringBuffer var3 = new StringBuffer(var2);
         int var4 = var2.indexOf(var1) + var1.length();
         var3.insert(var4, " direction=\"" + this.m_direction + "\"");
         var2 = var3.toString();
      }

      return var2;
   }

   public static void main(String[] var0) throws UDDIException {
      SharedRelationships var1 = new SharedRelationships();
      var1.setDirection("fromKey");
      System.out.println(var1.toXML() + "\n");
      var1.add(new KeyedReference((TModelKey)null, "keyName", "value"));
      System.out.println(var1.toXML());
   }
}
