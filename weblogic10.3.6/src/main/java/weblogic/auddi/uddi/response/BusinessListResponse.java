package weblogic.auddi.uddi.response;

import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.util.Util;

public class BusinessListResponse extends UDDIResponse {
   private BusinessInfos businessInfos = new BusinessInfos();

   public void addBusinessInfo(BusinessInfo var1) throws UDDIException {
      this.businessInfos.add(var1);
   }

   public void setBusinessInfos(BusinessInfos var1) {
      this.businessInfos = var1;
   }

   public BusinessInfos getBusinessInfos() {
      return this.businessInfos;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof BusinessListResponse)) {
         return false;
      } else {
         BusinessListResponse var2 = (BusinessListResponse)var1;
         boolean var3 = true;
         var3 &= Util.isEqual((Object)this.businessInfos, (Object)var2.businessInfos);
         return var3;
      }
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<businessList" + super.toXML() + ">");
      if (this.businessInfos != null) {
         var1.append(this.businessInfos.toXML());
      } else {
         var1.append("<businessInfos />");
      }

      var1.append("</businessList>");
      return var1.toString();
   }
}
