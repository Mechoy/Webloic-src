package weblogic.uddi.client.structures.response;

import java.util.Vector;
import weblogic.uddi.client.structures.datatypes.BindingTemplate;

public class BindingDetail extends ListResponse {
   private Vector bindingTemplateVector = new Vector();

   public void addBindingTemplate(BindingTemplate var1) {
      this.bindingTemplateVector.add(var1);
   }

   public void setBindingTemplateVector(Vector var1) {
      this.bindingTemplateVector = var1;
   }

   public Vector getBindingTemplateVector() {
      return this.bindingTemplateVector;
   }
}
