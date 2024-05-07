package weblogic.auddi.uddi.request.publish;

import java.util.ArrayList;
import weblogic.auddi.uddi.datastructure.BindingKey;

public class DeleteBindingRequest extends UDDIPublishRequest {
   private ArrayList m_bindingKeys = null;
   private int m_index = -1;

   public ArrayList getBindingKeys() {
      return this.m_bindingKeys;
   }

   public void addBindingKey(BindingKey var1) {
      if (this.m_bindingKeys == null) {
         this.m_bindingKeys = new ArrayList();
      }

      this.m_bindingKeys.add(var1);
   }

   public BindingKey getFirst() {
      if (this.m_bindingKeys.size() == 0) {
         return null;
      } else {
         this.m_index = 0;
         return (BindingKey)this.m_bindingKeys.get(this.m_index);
      }
   }

   public BindingKey getNext() {
      return this.m_index != -1 && this.m_index < this.m_bindingKeys.size() - 1 ? (BindingKey)this.m_bindingKeys.get(++this.m_index) : null;
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<delete_binding");
      var1.append(super.toXML() + ">");
      if (this.m_authInfo != null) {
         var1.append(this.m_authInfo.toXML());
      }

      BindingKey var2 = this.getFirst();
      if (var2 != null) {
         var1.append(var2.toXML());

         for(var2 = this.getNext(); var2 != null; var2 = this.getNext()) {
            var1.append(var2.toXML());
         }
      }

      var1.append("</delete_binding>");
      return var1.toString();
   }
}
