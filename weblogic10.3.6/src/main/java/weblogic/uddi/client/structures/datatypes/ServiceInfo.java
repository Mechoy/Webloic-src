package weblogic.uddi.client.structures.datatypes;

public class ServiceInfo {
   private Name name = null;
   private String serviceKey = null;
   private String businessKey = null;

   public Name getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = new Name(var1);
   }

   public void setName(Name var1) {
      this.name = var1;
   }

   public String getServiceKey() {
      return this.serviceKey;
   }

   public void setServiceKey(String var1) {
      this.serviceKey = var1;
   }

   public String getBusinessKey() {
      return this.businessKey;
   }

   public void setBusinessKey(String var1) {
      this.businessKey = var1;
   }
}
