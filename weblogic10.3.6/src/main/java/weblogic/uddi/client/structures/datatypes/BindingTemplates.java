package weblogic.uddi.client.structures.datatypes;

import java.util.Vector;

public class BindingTemplates {
   private Vector bindingTemplate = new Vector();

   public void addBindingTemplate(BindingTemplate var1) {
      this.bindingTemplate.add(var1);
   }

   public void setBindingTemplateVector(Vector var1) {
      this.bindingTemplate = var1;
   }

   public Vector getBindingTemplateVector() {
      return this.bindingTemplate;
   }
}
