package weblogic.auddi.uddi.request.inquiry;

import java.util.Enumeration;
import java.util.Vector;
import weblogic.auddi.uddi.datastructure.BusinessKey;
import weblogic.auddi.uddi.request.UDDIRequest;

public class GetBusinessDetailRequest extends UDDIRequest {
   private Vector businessKeys = null;
   private Enumeration businessKeysEnum = null;

   public void addBusinessKey(BusinessKey var1) {
      if (this.businessKeys == null) {
         this.businessKeys = new Vector();
      }

      this.businessKeys.add(var1);
   }

   public BusinessKey getFirst() {
      if (this.businessKeys.size() == 0) {
         return null;
      } else {
         this.businessKeysEnum = this.businessKeys.elements();
         return (BusinessKey)this.businessKeysEnum.nextElement();
      }
   }

   public BusinessKey getNext() {
      return this.businessKeysEnum != null && this.businessKeysEnum.hasMoreElements() ? (BusinessKey)this.businessKeysEnum.nextElement() : null;
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<get_businessDetail");
      var1.append(super.toXML() + ">\n");
      BusinessKey var2 = this.getFirst();
      if (var2 != null) {
         var1.append(var2.toXML());

         for(var2 = this.getNext(); var2 != null; var2 = this.getNext()) {
            var1.append(var2.toXML());
         }
      }

      var1.append("</get_businessDetail>\n");
      return var1.toString();
   }
}
