package weblogic.auddi.uddi.request.publish;

import java.util.ArrayList;
import weblogic.auddi.uddi.datastructure.TModelKey;

public class DeleteTModelRequest extends UDDIPublishRequest {
   private ArrayList m_tModelKeys = null;
   private int m_index = -1;

   public ArrayList getTModelKeys() {
      return this.m_tModelKeys;
   }

   public void addTModelKey(TModelKey var1) {
      if (this.m_tModelKeys == null) {
         this.m_tModelKeys = new ArrayList();
      }

      this.m_tModelKeys.add(var1);
   }

   public TModelKey getFirst() {
      if (this.m_tModelKeys.size() == 0) {
         return null;
      } else {
         this.m_index = 0;
         return (TModelKey)this.m_tModelKeys.get(this.m_index);
      }
   }

   public TModelKey getNext() {
      return this.m_index != -1 && this.m_index < this.m_tModelKeys.size() - 1 ? (TModelKey)this.m_tModelKeys.get(++this.m_index) : null;
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<delete_tModel");
      var1.append(super.toXML() + ">");
      if (super.m_authInfo != null) {
         var1.append(super.m_authInfo.toXML());
      }

      TModelKey var2 = this.getFirst();
      if (var2 != null) {
         var1.append(var2.toXML());

         for(var2 = this.getNext(); var2 != null; var2 = this.getNext()) {
            var1.append(var2.toXML());
         }
      }

      var1.append("</delete_tModel>");
      return var1.toString();
   }
}
