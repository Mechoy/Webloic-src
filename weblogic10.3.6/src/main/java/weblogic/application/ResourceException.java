package weblogic.application;

public class ResourceException extends Exception {
   private final String resourceId;
   private final String moduleId;
   private final String servername;
   private final String descriptorURI;
   private final String xpath;

   public ResourceException(String var1, String var2, String var3, String var4, String var5, String var6) {
      super(var1);
      this.resourceId = var2;
      this.moduleId = var3;
      this.descriptorURI = var4;
      this.xpath = var6;
      this.servername = var5;
   }

   public String getResourceName() {
      return this.resourceId;
   }

   public String getTargetServerName() {
      return this.servername;
   }

   public String getModuleId() {
      return this.moduleId;
   }

   public String getDescriptorURI() {
      return this.descriptorURI;
   }

   public String getPropertyXPath() {
      return this.xpath;
   }
}
