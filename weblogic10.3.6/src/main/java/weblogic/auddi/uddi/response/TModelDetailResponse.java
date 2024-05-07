package weblogic.auddi.uddi.response;

import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.TModel;
import weblogic.auddi.uddi.datastructure.TModels;
import weblogic.auddi.util.Util;

public class TModelDetailResponse extends UDDIResponse {
   private TModels tModels;

   public void addTModel(TModel var1) throws UDDIException {
      if (this.tModels == null) {
         this.tModels = new TModels();
      }

      this.tModels.add(var1);
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<tModelDetail" + super.toXML() + ">");
      if (this.tModels != null) {
         var1.append(this.tModels.toXML());
      }

      var1.append("</tModelDetail>");
      return var1.toString();
   }

   public void setTModels(TModels var1) {
      this.tModels = var1;
   }

   public TModels getTModels() {
      return this.tModels;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof TModelDetailResponse)) {
         return false;
      } else {
         TModelDetailResponse var2 = (TModelDetailResponse)var1;
         boolean var3 = true;
         var3 &= Util.isEqual((Object)this.tModels, (Object)var2.tModels);
         return var3;
      }
   }
}
