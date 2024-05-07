package weblogic.uddi.client.structures.response;

import weblogic.uddi.client.structures.datatypes.BusinessInfos;
import weblogic.uddi.client.structures.datatypes.TModelInfos;

public class RegisteredInfo extends ListResponse {
   private BusinessInfos businessInfos = null;
   private TModelInfos tModelInfos = null;

   public BusinessInfos getBusinessInfos() {
      return this.businessInfos;
   }

   public void setBusinessInfos(BusinessInfos var1) {
      this.businessInfos = var1;
   }

   public TModelInfos getTModelInfos() {
      return this.tModelInfos;
   }

   public void setTModelInfos(TModelInfos var1) {
      this.tModelInfos = var1;
   }
}
