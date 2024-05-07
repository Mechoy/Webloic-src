package weblogic.auddi.uddi.datastructure;

import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.util.Util;

public class Email extends UDDIListObject {
   private String m_email;
   private String m_useType;

   public Email(String var1) {
      this(var1, (String)null);
   }

   public Email(String var1, String var2) {
      this.m_email = null;
      if (var1 != null) {
         this.m_email = this.truncateString(var1, 255);
         this.m_useType = this.truncateString(var2, 255);
      }
   }

   public Email(Email var1) {
      this.m_email = null;
      if (var1 == null) {
         throw new IllegalArgumentException(UDDIMessages.get("error.runtime.constructor.null"));
      } else {
         this.m_email = var1.m_email;
         this.m_useType = var1.m_useType;
      }
   }

   public String getUseType() {
      return this.m_useType;
   }

   public void setUseType(String var1) {
      this.m_useType = this.truncateString(var1, 255);
   }

   public String getEmail() {
      return this.m_email;
   }

   public String toString() {
      return this.m_email;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof Email)) {
         return false;
      } else {
         Email var2 = (Email)var1;
         boolean var3 = true;
         var3 &= Util.isEqual((Object)this.m_email, (Object)var2.m_email);
         var3 &= Util.isEqual((Object)this.m_useType, (Object)var2.m_useType);
         return var3;
      }
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<email");
      if (this.m_useType != null) {
         var1.append(" useType=\"").append(this.m_useType).append("\"");
      }

      var1.append(">");
      var1.append(this.m_email);
      var1.append("</email>");
      return var1.toString();
   }
}
