package weblogic.auddi.uddi.request.inquiry;

import java.util.Enumeration;
import java.util.Vector;
import weblogic.auddi.uddi.datastructure.BindingKey;
import weblogic.auddi.uddi.request.UDDIRequest;

public class GetBindingDetailRequest extends UDDIRequest {
   private Vector bindingKeys = null;
   private Enumeration bindingKeysEnum = null;

   public void addBindingKey(BindingKey var1) {
      if (this.bindingKeys == null) {
         this.bindingKeys = new Vector();
      }

      this.bindingKeys.add(var1);
   }

   public BindingKey getFirst() {
      if (this.bindingKeys.size() == 0) {
         return null;
      } else {
         this.bindingKeysEnum = this.bindingKeys.elements();
         return (BindingKey)this.bindingKeysEnum.nextElement();
      }
   }

   public BindingKey getNext() {
      return this.bindingKeysEnum != null && this.bindingKeysEnum.hasMoreElements() ? (BindingKey)this.bindingKeysEnum.nextElement() : null;
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<get_bindingDetail");
      var1.append(super.toXML() + ">\n");
      BindingKey var2 = this.getFirst();
      if (var2 != null) {
         var1.append(var2.toXML());

         for(var2 = this.getNext(); var2 != null; var2 = this.getNext()) {
            var1.append(var2.toXML());
         }
      }

      var1.append("</get_bindingDetail>\n");
      return var1.toString();
   }
}
