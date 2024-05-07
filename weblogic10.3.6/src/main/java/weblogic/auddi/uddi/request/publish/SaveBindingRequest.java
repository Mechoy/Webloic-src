package weblogic.auddi.uddi.request.publish;

import java.io.Serializable;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.BindingTemplate;
import weblogic.auddi.uddi.datastructure.BindingTemplates;

public class SaveBindingRequest extends UDDIPublishRequest implements Serializable {
   private BindingTemplates bindingTemplates = null;

   public void addBindingTemplate(BindingTemplate var1) throws UDDIException {
      if (this.bindingTemplates == null) {
         this.bindingTemplates = new BindingTemplates();
      }

      this.bindingTemplates.add(var1);
   }

   public BindingTemplates getBindingTemplates() {
      return this.bindingTemplates;
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<save_binding");
      var1.append(super.toXML() + ">");
      if (this.m_authInfo != null) {
         var1.append(this.m_authInfo.toXML());
      }

      if (this.bindingTemplates != null) {
         var1.append(this.bindingTemplates.toXML(""));
      }

      var1.append("</save_binding>");
      return var1.toString();
   }
}
