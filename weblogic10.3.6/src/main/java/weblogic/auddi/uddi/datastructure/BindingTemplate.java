package weblogic.auddi.uddi.datastructure;

import java.io.Serializable;
import java.util.Date;
import weblogic.auddi.uddi.TooManyOptionsException;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.util.Util;

public class BindingTemplate extends UDDIListObject implements Serializable {
   private BindingKey m_bindingKey = null;
   private BusinessKey m_businessKey = null;
   private ServiceKey m_serviceKey = null;
   private Descriptions m_descriptions = null;
   private AccessPoint m_accessPoint = null;
   private HostingRedirector m_hostingRedirector = null;
   private TModelInstanceDetails m_tModelInstanceDetails = new TModelInstanceDetails();

   public BindingTemplate(BindingKey var1) {
      this.m_bindingKey = var1;
   }

   public BindingTemplate(BindingTemplate var1) throws UDDIException {
      if (var1 == null) {
         throw new IllegalArgumentException(UDDIMessages.get("error.runtime.constructor.null"));
      } else {
         if (var1.m_bindingKey != null) {
            this.m_bindingKey = new BindingKey(var1.m_bindingKey);
         }

         if (var1.m_serviceKey != null) {
            this.m_serviceKey = new ServiceKey(var1.m_serviceKey);
         }

         if (var1.m_businessKey != null) {
            this.m_businessKey = new BusinessKey(var1.m_businessKey);
         }

         if (var1.m_descriptions != null) {
            this.m_descriptions = new Descriptions(var1.m_descriptions);
         }

         if (var1.m_accessPoint != null) {
            this.m_accessPoint = new AccessPoint(var1.m_accessPoint);
         }

         if (var1.m_hostingRedirector != null) {
            this.m_hostingRedirector = new HostingRedirector(var1.m_hostingRedirector);
         }

         if (var1.m_tModelInstanceDetails != null) {
            this.m_tModelInstanceDetails = var1.m_tModelInstanceDetails;
         }

         if (var1.m_date != null) {
            this.m_date = new Date(var1.m_date.getTime());
         }

      }
   }

   public BindingTemplate() {
   }

   public void setBindingKey(BindingKey var1) {
      this.m_bindingKey = var1;
   }

   public BindingKey getBindingKey() {
      return this.m_bindingKey;
   }

   public void setDescriptions(Descriptions var1) {
      this.m_descriptions = var1;
   }

   public void setServiceKey(ServiceKey var1) {
      this.m_serviceKey = var1;
   }

   public ServiceKey getServiceKey() {
      return this.m_serviceKey;
   }

   public void setBusinessKey(BusinessKey var1) {
      this.m_businessKey = var1;
   }

   public BusinessKey getBusinessKey() {
      return this.m_businessKey;
   }

   public void addDescription(Description var1) throws UDDIException {
      if (var1 != null) {
         if (this.m_descriptions == null) {
            this.m_descriptions = new Descriptions();
         }

         this.m_descriptions.add(var1);
      }
   }

   public Descriptions getDescriptions() {
      return this.m_descriptions;
   }

   public void setAccessPoint(AccessPoint var1) {
      this.m_accessPoint = var1;
   }

   public AccessPoint getAccessPoint() {
      return this.m_accessPoint;
   }

   public HostingRedirector getHostingRedirector() {
      return this.m_hostingRedirector;
   }

   public void setHostingRedirector(HostingRedirector var1) throws UDDIException {
      if (this.m_accessPoint != null) {
         throw new TooManyOptionsException(UDDIMessages.get("error.tooManyOptions.AP_HR"));
      } else {
         this.m_hostingRedirector = var1;
      }
   }

   public void addTModelInstanceInfo(TModelInstanceInfo var1) throws UDDIException {
      if (this.m_tModelInstanceDetails == null) {
         this.m_tModelInstanceDetails = new TModelInstanceDetails();
      }

      this.m_tModelInstanceDetails.add(var1, var1.getTModelKey().toString());
   }

   public void setTModelInstanceDetails(TModelInstanceDetails var1) {
      this.m_tModelInstanceDetails = var1;
   }

   public TModelInstanceDetails getTModelInstanceDetails() {
      return this.m_tModelInstanceDetails;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof BindingTemplate)) {
         return false;
      } else {
         BindingTemplate var2 = (BindingTemplate)var1;
         boolean var3 = true;
         var3 &= Util.isEqual((Object)this.m_accessPoint, (Object)var2.m_accessPoint);
         var3 &= Util.isEqual((Object)this.m_bindingKey, (Object)var2.m_bindingKey);
         var3 &= this.m_descriptions == null ? var2.m_descriptions == null : this.m_descriptions.hasEqualContent(var2.m_descriptions);
         var3 &= Util.isEqual((Object)this.m_hostingRedirector, (Object)var2.m_hostingRedirector);
         var3 &= Util.isEqual((Object)this.m_serviceKey, (Object)var2.m_serviceKey);
         var3 &= Util.isEqual((Object)this.m_tModelInstanceDetails, (Object)var2.m_tModelInstanceDetails);
         return var3;
      }
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<bindingTemplate");
      if (this.m_bindingKey != null) {
         var1.append(" bindingKey=\"").append(this.m_bindingKey.toString()).append("\"");
      } else {
         var1.append(" bindingKey=\"\"");
      }

      if (this.m_serviceKey != null) {
         var1.append(" serviceKey=\"").append(this.m_serviceKey.toString()).append("\"");
      }

      var1.append(">");
      if (this.m_descriptions != null) {
         var1.append(this.m_descriptions.toXML());
      }

      if (this.m_accessPoint != null) {
         var1.append(this.m_accessPoint.toXML());
      }

      if (this.m_hostingRedirector != null) {
         var1.append(this.m_hostingRedirector.toXML());
      }

      if (this.m_tModelInstanceDetails != null) {
         var1.append(this.m_tModelInstanceDetails.toXML());
      } else {
         var1.append("<").append("tModelInstanceDetails").append(" />");
      }

      var1.append("</bindingTemplate>");
      return var1.toString();
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("bk:").append(this.m_bindingKey);
      var1.append(", sk:").append(this.m_serviceKey);
      var1.append(", bk:").append(this.m_businessKey);
      return var1.toString();
   }
}
