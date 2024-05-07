package weblogic.auddi.uddi.datastructure;

import weblogic.auddi.uddi.FatalErrorException;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.util.Util;

public class Address extends UDDIListObject {
   private String m_useType = null;
   private AddressLines m_addressLines = null;
   private String m_sortCode = null;
   private TModelKey m_tModelKey;

   public Address() {
   }

   public Address(Address var1) throws UDDIException {
      if (var1 == null) {
         throw new IllegalArgumentException(UDDIMessages.get("error.runtime.constructor.null"));
      } else {
         this.m_useType = var1.m_useType;
         this.m_sortCode = var1.m_sortCode;
         if (var1.m_tModelKey != null) {
            this.m_tModelKey = new TModelKey(var1.m_tModelKey);
         }

         if (var1.m_addressLines != null) {
            this.m_addressLines = new AddressLines(var1.m_addressLines);
         }

      }
   }

   public void setTModelKey(TModelKey var1) {
      this.m_tModelKey = var1;
   }

   public TModelKey getTModelKey() {
      return this.m_tModelKey;
   }

   public void setAddressLines(AddressLines var1) throws UDDIException {
      if (this.m_tModelKey != null && var1 != null) {
         for(AddressLine var2 = var1.getFirst(); var2 != null; var2 = var1.getNext()) {
            if (var2.getKeyName() == null || var2.getKeyValue() == null) {
               throw new FatalErrorException(UDDIMessages.get("error.fatalError.addressLine"));
            }
         }
      }

      this.m_addressLines = var1;
   }

   public AddressLines getAddressLines() {
      return this.m_addressLines;
   }

   public String getSortCode() {
      return this.m_sortCode;
   }

   public String getUseType() {
      return this.m_useType;
   }

   public void setUseType(String var1) {
      this.m_useType = this.truncateString(var1, 80);
   }

   public void setSortCode(String var1) {
      this.m_sortCode = this.truncateString(var1, 10);
   }

   public void addAddressLine(AddressLine var1) throws UDDIException {
      if (this.m_addressLines == null) {
         this.m_addressLines = new AddressLines();
      }

      if (this.m_tModelKey == null || var1.getKeyName() != null && var1.getKeyValue() != null) {
         this.m_addressLines.add(var1);
      } else {
         throw new FatalErrorException(UDDIMessages.get("error.fatalError.addressLine"));
      }
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof Address)) {
         return false;
      } else {
         Address var2 = (Address)var1;
         boolean var3 = true;
         var3 &= this.m_addressLines == null ? var2.m_addressLines == null : this.m_addressLines.hasEqualContent(var2.m_addressLines);
         var3 &= Util.isEqual((Object)this.m_sortCode, (Object)var2.m_sortCode);
         var3 &= Util.isEqual((Object)this.m_tModelKey, (Object)var2.m_tModelKey);
         var3 &= Util.isEqual((Object)this.m_useType, (Object)var2.m_useType);
         return var3;
      }
   }

   public int hashCode() {
      int var1 = 0;
      if (this.m_addressLines != null) {
         var1 ^= this.m_addressLines.hashCode();
      }

      if (this.m_sortCode != null) {
         var1 ^= this.m_sortCode.hashCode();
      }

      if (this.m_tModelKey != null) {
         var1 ^= this.m_tModelKey.hashCode();
      }

      if (this.m_useType != null) {
         var1 ^= this.m_useType.hashCode();
      }

      return var1;
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<").append("address").append(" ");
      if (this.m_useType != null) {
         var1.append("useType").append("=\"").append(this.m_useType).append("\" ");
      }

      if (this.m_sortCode != null) {
         var1.append("sortCode").append("=\"").append(this.m_sortCode).append("\" ");
      }

      if (this.m_tModelKey != null) {
         var1.append("tModelKey").append("=\"").append(this.m_tModelKey).append("\" ");
      }

      var1.append(">");
      if (this.m_addressLines != null) {
         var1.append(this.m_addressLines.toXML());
      }

      var1.append("</").append("address").append(">");
      return var1.toString();
   }
}
