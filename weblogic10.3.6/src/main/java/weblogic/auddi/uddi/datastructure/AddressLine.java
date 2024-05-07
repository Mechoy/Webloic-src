package weblogic.auddi.uddi.datastructure;

import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.util.Util;

public class AddressLine extends UDDIListObject {
   private String m_line;
   private String m_keyName;
   private String m_keyValue;

   public AddressLine(String var1, String var2, String var3) {
      this.m_line = null;
      this.m_keyName = null;
      this.m_keyValue = null;
      this.m_line = this.truncateString(var1, 80);
      this.m_keyName = this.truncateString(var2, 255);
      this.m_keyValue = this.truncateString(var3, 255);
   }

   public AddressLine(String var1) {
      this(var1, (String)null, (String)null);
   }

   public AddressLine(AddressLine var1) {
      this.m_line = null;
      this.m_keyName = null;
      this.m_keyValue = null;
      if (var1 == null) {
         throw new IllegalArgumentException(UDDIMessages.get("error.runtime.constructor.null"));
      } else {
         this.m_line = var1.m_line;
         this.m_keyName = var1.m_keyName;
         this.m_keyValue = var1.m_keyValue;
      }
   }

   public void setKeyName(String var1) {
      this.m_keyName = this.truncateString(var1, 255);
   }

   public String getKeyName() {
      return this.m_keyName;
   }

   public void setKeyValue(String var1) {
      this.m_keyValue = this.truncateString(var1, 255);
   }

   public String getKeyValue() {
      return this.m_keyValue;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof AddressLine)) {
         return false;
      } else {
         AddressLine var2 = (AddressLine)var1;
         boolean var3 = true;
         var3 &= Util.isEqual((Object)this.m_line, (Object)var2.m_line);
         var3 &= Util.isEqual((Object)this.m_keyName, (Object)var2.m_keyName);
         var3 &= Util.isEqual((Object)this.m_keyValue, (Object)var2.m_keyValue);
         return var3;
      }
   }

   public int hashCode() {
      int var1 = 0;
      if (this.m_line != null) {
         var1 ^= this.m_line.hashCode();
      }

      if (this.m_keyName != null) {
         var1 ^= this.m_keyName.hashCode();
      }

      if (this.m_keyValue != null) {
         var1 ^= this.m_keyValue.hashCode();
      }

      return var1;
   }

   public String toString() {
      return this.m_line == null ? "" : this.m_line;
   }

   public String getLine() {
      return this.m_line;
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<").append("addressLine");
      if (this.m_keyName != null) {
         var1.append(" ").append("keyName").append("=\"").append(this.m_keyName).append("\"");
      }

      if (this.m_keyValue != null) {
         var1.append(" ").append("keyValue").append("=\"").append(this.m_keyValue).append("\"");
      }

      var1.append(">");
      var1.append(this.fixStringForXML(this.toString()));
      var1.append("</").append("addressLine").append(">");
      return var1.toString();
   }
}
