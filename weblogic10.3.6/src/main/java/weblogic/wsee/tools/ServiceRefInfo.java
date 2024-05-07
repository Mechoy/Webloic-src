package weblogic.wsee.tools;

public class ServiceRefInfo {
   private String serviceRefName;
   private String serviceInterface;
   private String wsdlFile;
   private String jaxrpcMappingFile;

   public String getServiceRefName() {
      return this.serviceRefName;
   }

   public void setServiceRefName(String var1) {
      this.serviceRefName = var1;
   }

   public String getServiceInterface() {
      return this.serviceInterface;
   }

   public void setServiceInterface(String var1) {
      this.serviceInterface = var1;
   }

   public String getWsdlFile() {
      return this.wsdlFile;
   }

   public void setWsdlFile(String var1) {
      this.wsdlFile = var1;
   }

   public String getJaxrpcMappingFile() {
      return this.jaxrpcMappingFile;
   }

   public void setJaxrpcMappingFile(String var1) {
      this.jaxrpcMappingFile = var1;
   }
}
