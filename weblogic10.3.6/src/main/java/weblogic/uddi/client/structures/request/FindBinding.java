package weblogic.uddi.client.structures.request;

import weblogic.uddi.client.structures.datatypes.TModelBag;

public class FindBinding extends FindRequest {
   private String serviceKey = null;
   private TModelBag tModelBag = null;

   public void setServiceKey(String var1) {
      this.serviceKey = var1;
   }

   public String getServiceKey() {
      return this.serviceKey;
   }

   public void setTModelBag(TModelBag var1) {
      this.tModelBag = var1;
   }

   public TModelBag getTModelBag() {
      return this.tModelBag;
   }
}
