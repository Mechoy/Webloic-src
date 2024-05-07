package weblogic.auddi.uddi.request.inquiry;

import java.util.Enumeration;
import java.util.Vector;
import weblogic.auddi.uddi.datastructure.TModelKey;
import weblogic.auddi.uddi.request.UDDIRequest;

public class GetTModelDetailRequest extends UDDIRequest {
   private Vector tModelKeys = null;
   private Enumeration modelKeysEnum = null;

   public void addTModelKey(TModelKey var1) {
      if (this.tModelKeys == null) {
         this.tModelKeys = new Vector();
      }

      this.tModelKeys.add(var1);
   }

   public TModelKey getFirst() {
      if (this.tModelKeys.size() == 0) {
         return null;
      } else {
         this.modelKeysEnum = this.tModelKeys.elements();
         return (TModelKey)this.modelKeysEnum.nextElement();
      }
   }

   public TModelKey getNext() {
      return this.modelKeysEnum != null && this.modelKeysEnum.hasMoreElements() ? (TModelKey)this.modelKeysEnum.nextElement() : null;
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<get_tModelDetail");
      var1.append(super.toXML() + ">\n");
      TModelKey var2 = this.getFirst();
      if (var2 != null) {
         var1.append(var2.toXML());

         for(var2 = this.getNext(); var2 != null; var2 = this.getNext()) {
            var1.append(var2.toXML());
         }
      }

      var1.append("</get_tModelDetail>\n");
      return var1.toString();
   }
}
