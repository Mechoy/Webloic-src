package weblogic.uddi.client.structures.datatypes;

import java.util.Vector;

public class BindingTemplate {
   private Vector description = new Vector();
   private AccessPoint accessPoint = null;
   private HostingRedirector hostingRedirector = null;
   private TModelInstanceDetails tModelInstanceDetails = null;
   private String bindingKey = null;
   private String serviceKey = null;

   public void addDescription(String var1) {
      this.description.add(new Description(var1));
   }

   public void setDescriptionVector(Vector var1) {
      this.description = var1;
   }

   public Vector getDescriptionVector() {
      return this.description;
   }

   public void setAccessPoint(AccessPoint var1) {
      if (var1 != null) {
         this.hostingRedirector = null;
      }

      this.accessPoint = var1;
   }

   public AccessPoint getAccessPoint() {
      return this.accessPoint;
   }

   public void setHostingRedirector(HostingRedirector var1) {
      if (var1 != null) {
         this.accessPoint = null;
      }

      this.hostingRedirector = var1;
   }

   public HostingRedirector getHostingRedirector() {
      return this.hostingRedirector;
   }

   public void setTModelInstanceDetails(TModelInstanceDetails var1) {
      this.tModelInstanceDetails = var1;
   }

   public TModelInstanceDetails getTModelInstanceDetails() {
      return this.tModelInstanceDetails;
   }

   public void setBindingKey(String var1) {
      this.bindingKey = var1;
   }

   public String getBindingKey() {
      return this.bindingKey;
   }

   public void setServiceKey(String var1) {
      this.serviceKey = var1;
   }

   public String getServiceKey() {
      return this.serviceKey;
   }
}
