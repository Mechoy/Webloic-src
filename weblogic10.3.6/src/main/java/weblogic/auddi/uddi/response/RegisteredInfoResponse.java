package weblogic.auddi.uddi.response;

import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.util.Util;

public class RegisteredInfoResponse extends UDDIResponse {
   private BusinessInfos businessInfos = null;
   private TModelInfos tModelInfos = null;

   public RegisteredInfoResponse() {
      this.businessInfos = new BusinessInfos();
      this.tModelInfos = new TModelInfos();
   }

   public void addBusinessInfo(BusinessInfo var1) throws UDDIException {
      this.businessInfos.add(var1);
   }

   public void setBusinessInfos(BusinessInfos var1) {
      this.businessInfos = var1;
   }

   public BusinessInfos getBusinessInfos() {
      return this.businessInfos;
   }

   public TModelInfos getTModelInfos() {
      return this.tModelInfos;
   }

   public void setTModelInfos(TModelInfos var1) {
      this.tModelInfos = var1;
   }

   public void addTModelInfo(TModelInfo var1) throws UDDIException {
      this.tModelInfos.add(var1);
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof RegisteredInfoResponse)) {
         return false;
      } else {
         RegisteredInfoResponse var2 = (RegisteredInfoResponse)var1;
         boolean var3 = true;
         var3 &= Util.isEqual((Object)this.businessInfos, (Object)var2.businessInfos);
         var3 &= Util.isEqual((Object)this.tModelInfos, (Object)var2.tModelInfos);
         return var3;
      }
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<registeredInfo" + super.toXML() + ">");
      if (this.businessInfos != null) {
         var1.append(this.businessInfos.toXML());
      } else {
         var1.append("<businessInfos />");
      }

      if (this.tModelInfos != null) {
         var1.append(this.tModelInfos.toXML());
      } else {
         var1.append("<tModelInfos />");
      }

      var1.append("</registeredInfo>");
      return var1.toString();
   }
}
