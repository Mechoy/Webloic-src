package weblogic.auddi.uddi.request.publish;

import java.util.Enumeration;
import java.util.Vector;
import weblogic.auddi.uddi.datastructure.ServiceKey;

public class DeleteServiceRequest extends UDDIPublishRequest {
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
      var1.append("<delete_service");
      var1.append(super.toXML() + ">");
      if (this.m_authInfo != null) {
         var1.append(this.m_authInfo.toXML());
      }

      ServiceKey var2 = this.getFirst();
      if (var2 != null) {
         var1.append(var2.toXML());

         for(var2 = this.getNext(); var2 != null; var2 = this.getNext()) {
            var1.append(var2.toXML());
         }
      }

      var1.append("</delete_service>");
      return var1.toString();
   }
}
