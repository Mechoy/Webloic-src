package weblogic.auddi.uddi.response;

import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.BusinessService;
import weblogic.auddi.uddi.datastructure.BusinessServices;
import weblogic.auddi.util.Util;

public class ServiceDetailResponse extends UDDIResponse {
   private BusinessServices businessServices;

   public void addBusinessService(BusinessService var1) throws UDDIException {
      if (var1 != null) {
         if (this.businessServices == null) {
            this.businessServices = new BusinessServices();
         }

         this.businessServices.add(var1);
      }
   }

   public void setBusinessServices(BusinessServices var1) {
      this.businessServices = var1;
   }

   public BusinessServices getBusinessServices() {
      return this.businessServices;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof ServiceDetailResponse)) {
         return false;
      } else {
         ServiceDetailResponse var2 = (ServiceDetailResponse)var1;
         boolean var3 = true;
         var3 &= Util.isEqual((Object)this.businessServices, (Object)var2.businessServices);
         return var3;
      }
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<serviceDetail" + super.toXML() + ">");
      if (this.businessServices != null) {
         var1.append(this.businessServices.toXML(""));
      }

      var1.append("</serviceDetail>");
      return var1.toString();
   }
}
