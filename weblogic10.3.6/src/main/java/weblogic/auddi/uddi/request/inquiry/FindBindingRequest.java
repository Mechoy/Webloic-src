package weblogic.auddi.uddi.request.inquiry;

import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.ServiceKey;
import weblogic.auddi.uddi.datastructure.TModelBag;
import weblogic.auddi.uddi.datastructure.TModelKey;
import weblogic.auddi.uddi.request.UDDIRequest;

public class FindBindingRequest extends UDDIRequest {
   private ServiceKey serviceKey = null;
   private FindQualifiers findQualifiers = null;
   private TModelBag tModelBag = null;

   public void addFindQualifier(FindQualifier var1) throws UDDIException {
      if (this.findQualifiers == null) {
         this.findQualifiers = new FindQualifiers();
      }

      this.findQualifiers.add(var1);
   }

   public void setFindQualifiers(FindQualifiers var1) {
      this.findQualifiers = var1;
   }

   public FindQualifiers getFindQualifiers() {
      return this.findQualifiers;
   }

   public void setServiceKey(ServiceKey var1) {
      this.serviceKey = var1;
   }

   public ServiceKey getServiceKey() {
      return this.serviceKey;
   }

   public void addTModelKey(TModelKey var1) {
      if (this.tModelBag == null) {
         this.tModelBag = new TModelBag();
      }

      this.tModelBag.add(var1);
   }

   public TModelBag getTModelBag() {
      return this.tModelBag;
   }

   public void setTModelBag(TModelBag var1) {
      this.tModelBag = var1;
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<find_binding serviceKey=\"");
      var1.append(this.serviceKey.toString() + "\"" + super.toXML() + ">\n");
      if (this.findQualifiers != null) {
         var1.append(this.findQualifiers.toXML());
      }

      if (this.tModelBag != null) {
         var1.append(this.tModelBag.toXML());
      }

      var1.append("</find_binding>\n");
      return var1.toString();
   }
}
