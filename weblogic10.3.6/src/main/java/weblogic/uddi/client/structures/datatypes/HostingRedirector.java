package weblogic.uddi.client.structures.datatypes;

public class HostingRedirector {
   private String bindingKey;

   public HostingRedirector() {
      this.bindingKey = null;
   }

   public HostingRedirector(String var1) {
      this.bindingKey = var1;
   }

   public void setBindingKey(String var1) {
      this.bindingKey = var1;
   }

   public String getBindingKey() {
      return this.bindingKey;
   }
}
