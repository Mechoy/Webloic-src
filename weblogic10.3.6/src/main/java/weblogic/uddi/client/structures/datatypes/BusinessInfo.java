package weblogic.uddi.client.structures.datatypes;

import java.util.Vector;

public class BusinessInfo {
   private Name name = null;
   private String businessKey = null;
   private Vector description = new Vector();
   private ServiceInfos serviceInfos = null;

   public Name getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = new Name(var1);
   }

   public void setName(Name var1) {
      this.name = var1;
   }

   public String getBusinessKey() {
      return this.businessKey;
   }

   public void setBusinessKey(String var1) {
      this.businessKey = var1;
   }

   public Vector getDescriptionVector() {
      return this.description;
   }

   public void addDescription(String var1) {
      this.description.add(new Description(var1));
   }

   public void setDescriptionVector(Vector var1) {
      this.description = var1;
   }

   public ServiceInfos getServiceInfos() {
      return this.serviceInfos;
   }

   public void setServiceInfos(ServiceInfos var1) {
      this.serviceInfos = var1;
   }
}
