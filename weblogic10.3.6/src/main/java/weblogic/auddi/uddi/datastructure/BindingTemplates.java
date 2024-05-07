package weblogic.auddi.uddi.datastructure;

import java.io.Serializable;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;

public class BindingTemplates extends UDDIList implements Serializable {
   public BindingTemplates() {
   }

   public BindingTemplates(BindingTemplates var1) throws UDDIException {
      if (var1 == null) {
         throw new IllegalArgumentException(UDDIMessages.get("error.runtime.constructor.null"));
      } else {
         for(int var2 = 0; var2 < var1.size(); ++var2) {
            this.add(new BindingTemplate((BindingTemplate)var1.get(var2)));
         }

      }
   }

   public void add(BindingTemplate var1) {
      super.add(var1);
   }

   public BindingTemplate getFirst() {
      return (BindingTemplate)super.getVFirst();
   }

   public BindingTemplate getNext() {
      return (BindingTemplate)super.getVNext();
   }

   public String toXML() {
      return super.toXML("bindingTemplates");
   }
}
