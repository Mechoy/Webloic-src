package weblogic.auddi.uddi.datastructure;

import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.util.Util;

public class Phone extends UDDIListObject {
   private String m_phoneNumber;
   private String m_useType;

   public Phone(String var1) {
      this(var1, (String)null);
   }

   public Phone(String var1, String var2) {
      this.m_useType = null;
      if (var1 != null) {
         this.m_phoneNumber = this.truncateString(var1, 50);
         this.m_useType = this.truncateString(var2, 255);
      }
   }

   public Phone(Phone var1) {
      this.m_useType = null;
      if (var1 == null) {
         throw new IllegalArgumentException(UDDIMessages.get("error.runtime.constructor.null"));
      } else {
         this.m_phoneNumber = var1.m_phoneNumber;
         this.m_useType = var1.m_useType;
      }
   }

   public void setUseType(String var1) {
      this.m_useType = var1;
   }

   public String getUseType() {
      return this.m_useType;
   }

   public String getPhoneNumber() {
      return this.m_phoneNumber;
   }

   public String toString() {
      return this.m_phoneNumber;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof Phone)) {
         return false;
      } else {
         Phone var2 = (Phone)var1;
         boolean var3 = true;
         var3 &= Util.isEqual((Object)this.m_phoneNumber, (Object)var2.m_phoneNumber);
         var3 &= Util.isEqual((Object)this.m_useType, (Object)var2.m_useType);
         return var3;
      }
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<phone");
      if (this.m_useType != null) {
         var1.append(" useType=\"").append(this.m_useType).append("\"");
      }

      var1.append(">");
      var1.append(this.m_phoneNumber);
      var1.append("</phone>");
      return var1.toString();
   }
}
