package weblogic.auddi.uddi.response;

import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.BusinessEntityExt;
import weblogic.auddi.uddi.datastructure.BusinessEntityExts;
import weblogic.auddi.util.Util;

public class BusinessDetailExtResponse extends UDDIResponse {
   private BusinessEntityExts businessEntities;

   public BusinessEntityExts getBusinessEntityExts() {
      return this.businessEntities;
   }

   public void setBusinessEntityExts(BusinessEntityExts var1) {
      this.businessEntities = var1;
   }

   public void addBusinessEntityExt(BusinessEntityExt var1) throws UDDIException {
      if (var1 != null) {
         if (this.businessEntities == null) {
            this.businessEntities = new BusinessEntityExts();
         }

         this.businessEntities.add(var1);
      }
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof BusinessDetailExtResponse)) {
         return false;
      } else {
         BusinessDetailExtResponse var2 = (BusinessDetailExtResponse)var1;
         boolean var3 = true;
         var3 &= Util.isEqual((Object)this.businessEntities, (Object)var2.businessEntities);
         return var3;
      }
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<businessDetailExt" + super.toXML() + ">");
      if (this.businessEntities != null) {
         var1.append(this.businessEntities.toXML());
      }

      var1.append("</businessDetailExt>");
      return var1.toString();
   }
}
