package weblogic.auddi.uddi.response;

import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.BusinessKey;
import weblogic.auddi.uddi.datastructure.Description;
import weblogic.auddi.uddi.datastructure.Descriptions;
import weblogic.auddi.uddi.datastructure.Name;
import weblogic.auddi.uddi.datastructure.UDDIListObject;
import weblogic.auddi.uddi.datastructure.UniqueNames;
import weblogic.auddi.util.Util;

public class BusinessInfo extends UDDIListObject {
   private BusinessKey businessKey = null;
   private UniqueNames names = null;
   private Descriptions descriptions = null;
   private ServiceInfos serviceInfos = null;

   public UniqueNames getUniqueNames() {
      return this.names;
   }

   public BusinessKey getBusinessKey() {
      return this.businessKey;
   }

   public Descriptions getDescriptions() {
      return this.descriptions;
   }

   public ServiceInfos getServiceInfos() {
      return this.serviceInfos;
   }

   public BusinessInfo(BusinessKey var1) {
      this.businessKey = var1;
   }

   public BusinessInfo() {
   }

   public void setKey(BusinessKey var1) {
      this.businessKey = var1;
   }

   public void addName(Name var1) throws UDDIException {
      if (this.names == null) {
         this.names = new UniqueNames();
      }

      this.names.add(var1);
   }

   public void setNames(UniqueNames var1) throws UDDIException {
      this.names = var1;
   }

   public void setDescriptions(Descriptions var1) {
      this.descriptions = var1;
   }

   public void addDescription(Description var1) throws UDDIException {
      if (var1 != null) {
         if (this.descriptions == null) {
            this.descriptions = new Descriptions();
         }

         this.descriptions.add(var1);
      }
   }

   public void addServiceInfo(ServiceInfo var1) throws UDDIException {
      if (this.serviceInfos == null) {
         this.serviceInfos = new ServiceInfos();
      }

      this.serviceInfos.add(var1);
   }

   public void setServiceInfos(ServiceInfos var1) {
      this.serviceInfos = var1;
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<businessInfo businessKey=\"");
      if (this.businessKey != null) {
         var1.append(this.businessKey.toString());
      }

      var1.append("\">");
      if (this.names != null) {
         var1.append(this.names.toXML(""));
      }

      if (this.descriptions != null) {
         var1.append(this.descriptions.toXML());
      }

      if (this.serviceInfos != null) {
         var1.append(this.serviceInfos.toXML());
      } else {
         var1.append("<serviceInfos />");
      }

      var1.append("</businessInfo>");
      return var1.toString();
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof BusinessInfo)) {
         return false;
      } else {
         BusinessInfo var2 = (BusinessInfo)var1;
         boolean var3 = true;
         var3 &= Util.isEqual((Object)this.businessKey, (Object)var2.businessKey);
         var3 &= Util.isEqual((Object)this.names, (Object)var2.names);
         var3 &= Util.isEqual((Object)this.descriptions, (Object)var2.descriptions);
         var3 &= Util.isEqual((Object)this.serviceInfos, (Object)var2.serviceInfos);
         return var3;
      }
   }
}
