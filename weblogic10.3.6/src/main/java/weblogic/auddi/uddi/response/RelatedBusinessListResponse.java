package weblogic.auddi.uddi.response;

import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.BusinessKey;
import weblogic.auddi.util.Util;

public class RelatedBusinessListResponse extends UDDIResponse {
   private BusinessKey businessKey;
   private RelatedBusinessInfos relatedBusinessInfos = new RelatedBusinessInfos();

   public void addRelatedBusinessInfo(RelatedBusinessInfo var1) throws UDDIException {
      this.relatedBusinessInfos.add(var1);
   }

   public void setRelatedBusinessInfos(RelatedBusinessInfos var1) {
      this.relatedBusinessInfos = var1;
   }

   public RelatedBusinessInfos getRelatedBusinessInfos() {
      return this.relatedBusinessInfos;
   }

   public void setBusinessKey(BusinessKey var1) {
      this.businessKey = var1;
   }

   public BusinessKey getBusinessKey() {
      return this.businessKey;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof RelatedBusinessListResponse)) {
         return false;
      } else {
         RelatedBusinessListResponse var2 = (RelatedBusinessListResponse)var1;
         boolean var3 = true;
         var3 &= Util.isEqual((Object)this.businessKey, (Object)var2.businessKey);
         var3 &= Util.isEqual((Object)this.relatedBusinessInfos, (Object)var2.relatedBusinessInfos);
         return var3;
      }
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<relatedBusinessesList" + super.toXML() + ">");
      var1.append("<businessKey>" + this.businessKey.toString() + "</businessKey>");
      if (this.relatedBusinessInfos != null) {
         var1.append(this.relatedBusinessInfos.toXML());
      } else {
         var1.append("<relatedBusinessInfos />");
      }

      var1.append("</relatedBusinessesList>");
      return var1.toString();
   }
}
