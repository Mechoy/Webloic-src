package weblogic.auddi.uddi.datastructure;

import weblogic.auddi.util.Util;

public class Operator extends UDDIElement {
   private String m_name = null;

   public Operator(String var1) {
      this.m_name = this.truncateString(var1, 255);
   }

   public String getName() {
      return this.m_name;
   }

   public String toString() {
      return this.getName();
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof Operator)) {
         return false;
      } else {
         Operator var2 = (Operator)var1;
         boolean var3 = true;
         var3 &= Util.isEqual((Object)this.m_name, (Object)var2.m_name);
         return var3;
      }
   }

   public String toXML() {
      return this.m_name == null ? "" : "<operator>" + this.m_name + "</" + "operator" + ">";
   }
}
