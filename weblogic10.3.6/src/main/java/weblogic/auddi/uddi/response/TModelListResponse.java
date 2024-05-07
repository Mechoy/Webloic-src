package weblogic.auddi.uddi.response;

import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.util.Util;

public class TModelListResponse extends UDDIResponse {
   TModelInfos tModelInfos = new TModelInfos();

   public void addTModelInfo(TModelInfo var1) throws UDDIException {
      this.tModelInfos.add(var1);
   }

   public TModelInfos getTModelInfos() {
      return this.tModelInfos;
   }

   public void setTModelInfos(TModelInfos var1) {
      this.tModelInfos = var1;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof TModelListResponse)) {
         return false;
      } else {
         TModelListResponse var2 = (TModelListResponse)var1;
         boolean var3 = true;
         var3 &= Util.isEqual((Object)this.tModelInfos, (Object)var2.tModelInfos);
         return var3;
      }
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<tModelList" + super.toXML() + ">");
      if (this.tModelInfos != null) {
         var1.append(this.tModelInfos.toXML());
      }

      var1.append("</tModelList>");
      return var1.toString();
   }
}
