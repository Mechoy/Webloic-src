package weblogic.auddi.uddi.request.publish;

import java.io.Serializable;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.BusinessService;
import weblogic.auddi.uddi.datastructure.BusinessServices;

public class SaveServiceRequest extends UDDIPublishRequest implements Serializable {
   private BusinessServices businessServices = null;

   public void addBusinessService(BusinessService var1) throws UDDIException {
      if (this.businessServices == null) {
         this.businessServices = new BusinessServices();
      }

      this.businessServices.add(var1);
   }

   public BusinessServices getBusinessServices() {
      return this.businessServices;
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<save_service");
      var1.append(super.toXML() + ">");
      if (this.m_authInfo != null) {
         var1.append(this.m_authInfo.toXML());
      }

      if (this.businessServices != null) {
         for(BusinessService var2 = this.businessServices.getFirst(); var2 != null; var2 = this.businessServices.getNext()) {
            var1.append(var2.toXML());
         }
      }

      var1.append("</save_service>");
      return var1.toString();
   }
}
