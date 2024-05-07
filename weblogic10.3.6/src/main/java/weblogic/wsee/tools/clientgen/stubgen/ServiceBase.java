package weblogic.wsee.tools.clientgen.stubgen;

import java.util.Map;
import weblogic.wsee.wsdl.WsdlService;

public abstract class ServiceBase extends JavaFileGenBase {
   protected String location;
   protected String serviceName;
   protected WsdlService service;
   protected Map portNameMap;
   protected Map portTypeNameMap;
   protected Map stubNameMap;
   protected boolean generatePolicyMethods;

   public ServiceBase() {
      this.out = System.out;
      this.location = "http://you.forgot.to/set/wsdl/location/";
   }

   public void setService(WsdlService var1) {
      this.service = var1;
   }

   public void setWSDLLocation(String var1) {
      this.location = var1;
   }

   public void setPortNameMap(Map var1) {
      this.portNameMap = var1;
   }

   public void setPortTypeNameMap(Map var1) {
      this.portTypeNameMap = var1;
   }

   public void setStubNameMap(Map var1) {
      this.stubNameMap = var1;
   }

   public void setServiceName(String var1) {
      this.serviceName = var1;
   }

   public void setGeneratePolicyMethods(boolean var1) {
      this.generatePolicyMethods = var1;
   }

   protected String getInternalddPath() {
      return this.packageName.replace('.', '/') + "/" + this.serviceName + "_internaldd.xml";
   }
}
