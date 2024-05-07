package weblogic.auddi.uddi.response;

import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.BusinessKey;
import weblogic.auddi.uddi.datastructure.Name;
import weblogic.auddi.uddi.datastructure.ServiceKey;
import weblogic.auddi.uddi.datastructure.UDDIListObject;
import weblogic.auddi.uddi.datastructure.UniqueNames;
import weblogic.auddi.util.Util;

public class ServiceInfo extends UDDIListObject {
   private BusinessKey businessKey;
   private ServiceKey serviceKey;
   private UniqueNames names;

   public ServiceInfo(BusinessKey var1, ServiceKey var2) {
      this.businessKey = var1;
      this.serviceKey = var2;
   }

   public ServiceInfo() {
      this.businessKey = null;
      this.serviceKey = null;
   }

   public ServiceKey getServiceKey() {
      return this.serviceKey;
   }

   public BusinessKey getBusinessKey() {
      return this.businessKey;
   }

   public void setServiceKey(ServiceKey var1) {
      this.serviceKey = var1;
   }

   public void setBusinessKey(BusinessKey var1) {
      this.businessKey = var1;
   }

   public void addName(Name var1) throws UDDIException {
      if (this.names == null) {
         this.names = new UniqueNames();
      }

      this.names.add(var1);
   }

   public void setNames(UniqueNames var1) {
      this.names = var1;
   }

   public UniqueNames getNames() {
      return this.names;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof ServiceInfo)) {
         return false;
      } else {
         ServiceInfo var2 = (ServiceInfo)var1;
         boolean var3 = true;
         var3 &= Util.isEqual((Object)this.businessKey, (Object)var2.businessKey);
         var3 &= Util.isEqual((Object)this.names, (Object)var2.names);
         var3 &= Util.isEqual((Object)this.serviceKey, (Object)var2.serviceKey);
         return var3;
      }
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<serviceInfo");
      if (this.businessKey != null) {
         var1.append(" businessKey=\"" + this.businessKey.toString() + "\"");
      }

      if (this.serviceKey != null) {
         var1.append(" serviceKey=\"" + this.serviceKey.toString() + "\"");
      }

      var1.append(">");
      if (this.names != null) {
         var1.append(this.names.toXML(""));
      }

      var1.append("</serviceInfo>");
      return var1.toString();
   }
}
