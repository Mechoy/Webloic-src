package weblogic.auddi.uddi.request.publish;

import java.util.ArrayList;
import weblogic.auddi.uddi.datastructure.BusinessKey;

public class DeleteBusinessRequest extends UDDIPublishRequest {
   private ArrayList m_businessKeys = null;
   private int m_index = -1;

   public ArrayList getBusinessKeys() {
      return this.m_businessKeys;
   }

   public void addBusinessKey(BusinessKey var1) {
      if (this.m_businessKeys == null) {
         this.m_businessKeys = new ArrayList();
      }

      this.m_businessKeys.add(var1);
   }

   public BusinessKey getFirst() {
      if (this.m_businessKeys.size() == 0) {
         return null;
      } else {
         this.m_index = 0;
         return (BusinessKey)this.m_businessKeys.get(this.m_index);
      }
   }

   public BusinessKey getNext() {
      return this.m_index != -1 && this.m_index < this.m_businessKeys.size() - 1 ? (BusinessKey)this.m_businessKeys.get(++this.m_index) : null;
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<delete_business");
      var1.append(super.toXML() + ">");
      if (this.m_authInfo != null) {
         var1.append(this.m_authInfo.toXML());
      }

      BusinessKey var2 = this.getFirst();
      if (var2 != null) {
         var1.append(var2.toXML());

         for(var2 = this.getNext(); var2 != null; var2 = this.getNext()) {
            var1.append(var2.toXML());
         }
      }

      var1.append("</delete_business>");
      return var1.toString();
   }
}
