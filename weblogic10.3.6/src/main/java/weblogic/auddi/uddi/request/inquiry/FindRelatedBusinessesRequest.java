package weblogic.auddi.uddi.request.inquiry;

import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.BusinessKey;
import weblogic.auddi.uddi.datastructure.KeyedReference;
import weblogic.auddi.uddi.request.UDDIRequest;

public class FindRelatedBusinessesRequest extends UDDIRequest {
   private FindQualifiers findQualifiers = null;
   private BusinessKey businessKey = null;
   private KeyedReference keyedReference = null;

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

   public void setKeyedReference(KeyedReference var1) {
      this.keyedReference = var1;
   }

   public KeyedReference getKeyedReference() {
      return this.keyedReference;
   }

   public void setBusinessKey(BusinessKey var1) {
      this.businessKey = var1;
   }

   public BusinessKey getBusinessKey() {
      return this.businessKey;
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<find_relatedBusinesses");
      var1.append(super.toXML() + ">\n");
      if (this.findQualifiers != null) {
         var1.append(this.findQualifiers.toXML());
      }

      if (this.businessKey != null) {
         var1.append(this.businessKey.toXML());
      }

      if (this.keyedReference != null) {
         var1.append(this.keyedReference.toXML());
      }

      var1.append("</find_relatedBusinesses>\n");
      return var1.toString();
   }
}
