package weblogic.auddi.uddi.datastructure;

import weblogic.auddi.util.Util;

public class AuthInfo extends UDDIElement {
   private String m_value = null;

   public AuthInfo(String var1) {
      this.setValue(var1);
   }

   public void setValue(String var1) {
      this.m_value = this.truncateString(var1, 4096);
   }

   public String getValue() {
      return this.m_value == null ? "" : this.m_value;
   }

   public String toString() {
      return this.getValue();
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof AuthInfo)) {
         return false;
      } else {
         AuthInfo var2 = (AuthInfo)var1;
         boolean var3 = true;
         var3 &= Util.isEqual((Object)this.m_value, (Object)var2.m_value);
         return var3;
      }
   }

   public String toXML() {
      return "<authInfo>" + this.toString() + "</authInfo>";
   }
}
