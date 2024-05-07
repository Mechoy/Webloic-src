package weblogic.auddi.uddi.response;

import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.BusinessEntities;
import weblogic.auddi.uddi.datastructure.BusinessEntity;
import weblogic.auddi.util.Util;

public class BusinessDetailResponse extends UDDIResponse {
   private BusinessEntities m_businessEntities;

   public void addBusinessEntity(BusinessEntity var1) throws UDDIException {
      if (var1 != null) {
         if (this.m_businessEntities == null) {
            this.m_businessEntities = new BusinessEntities();
         }

         this.m_businessEntities.add(var1);
      }
   }

   public BusinessEntities getBusinessEntities() {
      return this.m_businessEntities;
   }

   public void setBusinessEntities(BusinessEntities var1) {
      this.m_businessEntities = var1;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof BusinessDetailResponse)) {
         return false;
      } else {
         BusinessDetailResponse var2 = (BusinessDetailResponse)var1;
         boolean var3 = true;
         var3 &= Util.isEqual((Object)this.m_businessEntities, (Object)var2.m_businessEntities);
         return var3;
      }
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<businessDetail" + super.toXML() + ">");
      if (this.m_businessEntities != null) {
         var1.append(this.m_businessEntities.toXML());
      }

      var1.append("</businessDetail>");
      return var1.toString();
   }
}
