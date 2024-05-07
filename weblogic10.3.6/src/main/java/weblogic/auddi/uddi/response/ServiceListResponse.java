package weblogic.auddi.uddi.response;

import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.util.Util;

public class ServiceListResponse extends UDDIResponse {
   private ServiceInfos serviceInfos = new ServiceInfos();

   public void addServiceInfo(ServiceInfo var1) throws UDDIException {
      this.serviceInfos.add(var1);
   }

   public void setServiceInfos(ServiceInfos var1) {
      this.serviceInfos = var1;
   }

   public ServiceInfos getServiceInfos() {
      return this.serviceInfos;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof ServiceListResponse)) {
         return false;
      } else {
         ServiceListResponse var2 = (ServiceListResponse)var1;
         boolean var3 = true;
         var3 &= Util.isEqual((Object)this.serviceInfos, (Object)var2.serviceInfos);
         return var3;
      }
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<serviceList " + super.toXML() + ">");
      if (this.serviceInfos != null) {
         var1.append(this.serviceInfos.toXML());
      }

      var1.append("</serviceList>");
      return var1.toString();
   }
}
