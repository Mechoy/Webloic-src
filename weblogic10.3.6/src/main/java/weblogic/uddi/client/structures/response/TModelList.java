package weblogic.uddi.client.structures.response;

import weblogic.uddi.client.structures.datatypes.TModelInfos;

public class TModelList extends ListResponse {
   private TModelInfos tModelInfos = null;

   public TModelInfos getTModelInfos() {
      return this.tModelInfos;
   }

   public void setTModelInfos(TModelInfos var1) {
      this.tModelInfos = var1;
   }
}
