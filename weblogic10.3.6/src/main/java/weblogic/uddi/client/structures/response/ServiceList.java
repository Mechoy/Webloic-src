package weblogic.uddi.client.structures.response;

import weblogic.uddi.client.structures.datatypes.ServiceInfos;

public class ServiceList extends ListResponse {
   private ServiceInfos serviceInfos = null;

   public ServiceInfos getServiceInfos() {
      return this.serviceInfos;
   }

   public void setServiceInfos(ServiceInfos var1) {
      this.serviceInfos = var1;
   }
}
