package weblogic.uddi.client.structures.response;

import weblogic.uddi.client.structures.datatypes.BusinessInfos;

public class BusinessList extends ListResponse {
   private BusinessInfos businessInfos = null;

   public BusinessInfos getBusinessInfos() {
      return this.businessInfos;
   }

   public void setBusinessInfos(BusinessInfos var1) {
      this.businessInfos = var1;
   }
}
