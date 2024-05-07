package weblogic.auddi.uddi.response;

import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.BindingTemplate;
import weblogic.auddi.uddi.datastructure.BindingTemplates;
import weblogic.auddi.util.Util;

public class BindingDetailResponse extends UDDIResponse {
   private BindingTemplates bindingTemplates;

   public void addBindingTemplate(BindingTemplate var1) throws UDDIException {
      if (var1 != null) {
         if (this.bindingTemplates == null) {
            this.bindingTemplates = new BindingTemplates();
         }

         this.bindingTemplates.add(var1);
      }
   }

   public void setBindingTemplates(BindingTemplates var1) {
      this.bindingTemplates = var1;
   }

   public BindingTemplates getBindingTemplates() {
      return this.bindingTemplates;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof BindingDetailResponse)) {
         return false;
      } else {
         BindingDetailResponse var2 = (BindingDetailResponse)var1;
         boolean var3 = true;
         var3 &= Util.isEqual((Object)this.bindingTemplates, (Object)var2.bindingTemplates);
         return var3;
      }
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<bindingDetail" + super.toXML() + ">");
      if (this.bindingTemplates != null) {
         var1.append(this.bindingTemplates.toXML(""));
      }

      var1.append("</bindingDetail>");
      return var1.toString();
   }
}
