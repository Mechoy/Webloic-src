package weblogic.auddi.uddi.request.inquiry;

import java.util.Enumeration;
import java.util.Vector;
import weblogic.auddi.uddi.datastructure.ServiceKey;
import weblogic.auddi.uddi.request.UDDIRequest;

public class GetServiceDetailRequest extends UDDIRequest {
   private Vector serviceKeys = null;
   private Enumeration serviceKeysEnum = null;

   public void addServiceKey(ServiceKey var1) {
      if (this.serviceKeys == null) {
         this.serviceKeys = new Vector();
      }

      this.serviceKeys.add(var1);
   }

   public ServiceKey getFirst() {
      if (this.serviceKeys.size() == 0) {
         return null;
      } else {
         this.serviceKeysEnum = this.serviceKeys.elements();
         return (ServiceKey)this.serviceKeysEnum.nextElement();
      }
   }

   public ServiceKey getNext() {
      return this.serviceKeysEnum != null && this.serviceKeysEnum.hasMoreElements() ? (ServiceKey)this.serviceKeysEnum.nextElement() : null;
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<get_serviceDetail");
      var1.append(super.toXML() + ">\n");
      ServiceKey var2 = this.getFirst();
      if (var2 != null) {
         var1.append(var2.toXML());

         for(var2 = this.getNext(); var2 != null; var2 = this.getNext()) {
            var1.append(var2.toXML());
         }
      }

      var1.append("</get_serviceDetail>\n");
      return var1.toString();
   }
}
