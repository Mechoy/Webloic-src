package weblogic.auddi.uddi.datastructure;

import weblogic.auddi.util.Util;

public class BusinessEntityExt extends UDDIListObject {
   private BusinessEntity m_businessEntity = null;

   public BusinessEntityExt(BusinessEntity var1) {
      this.m_businessEntity = var1;
   }

   public BusinessEntityExt() {
      this.m_businessEntity = null;
   }

   public BusinessEntity getBusinessEntity() {
      return this.m_businessEntity;
   }

   public void setBusinessEntity(BusinessEntity var1) {
      this.m_businessEntity = var1;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof BusinessEntityExt)) {
         return false;
      } else {
         BusinessEntityExt var2 = (BusinessEntityExt)var1;
         boolean var3 = true;
         var3 &= Util.isEqual((Object)this.m_businessEntity, (Object)var2.m_businessEntity);
         return var3;
      }
   }

   public String toXML() {
      return this.m_businessEntity == null ? "" : "<businessEntityExt>\n" + this.m_businessEntity.toXML() + "</businessEntityExt>";
   }
}
