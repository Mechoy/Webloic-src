package weblogic.wsee.tools.clientgen;

import com.bea.util.jam.JClass;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import weblogic.wsee.bind.TypeFamily;
import weblogic.wsee.bind.buildtime.BuildtimeBindings;
import weblogic.wsee.tools.source.JsService;
import weblogic.wsee.util.UniqueNameSet;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.wsee.wsdl.WsdlService;
import weblogic.xml.schema.binding.internal.NameUtil;

public class ProcessInfo {
   private String wsdlUrl;
   private WsdlDefinitions wsdlDefs;
   private String serviceName;
   private String packageName;
   private String mappingFileUri;
   private File destDir;
   private File handlerChain;
   private boolean partialWsdl;
   private JsService jsService;
   private JsService callbackService;
   private StubInfo stubInfo;
   private BuildtimeBindings buildtimeBindings;
   private boolean generatePolicyMethods;
   private JClass callbackJClass;
   private JClass webserviceJClass;
   private boolean jaxRPCWrappedArrayStyle;
   private boolean generateAsyncMethods;
   private boolean writeJavaTypes = true;
   private TypeFamily typeFamily;
   private File callbackDestDir;
   private String callbackContextRoot;

   public ProcessInfo() {
      this.typeFamily = TypeFamily.TYLAR;
   }

   public String getCallbackContextRoot() {
      return this.callbackContextRoot;
   }

   public void setCallbackContextRoot(String var1) {
      this.callbackContextRoot = var1;
   }

   public File getCallbackDestDir() {
      return this.callbackDestDir;
   }

   public void setCallbackDestDir(File var1) {
      this.callbackDestDir = var1;
   }

   public BuildtimeBindings getBuildtimeBindings() {
      return this.buildtimeBindings;
   }

   public void setBuildtimeBindings(BuildtimeBindings var1) {
      this.buildtimeBindings = var1;
   }

   public String getMappingFileUri() {
      return this.mappingFileUri;
   }

   public void setMappingFileUri(String var1) {
      this.mappingFileUri = var1;
   }

   public String getMappingFileName() {
      String var1 = this.getStubInfo().getServiceName();
      return var1 + "_java_wsdl_mapping.xml";
   }

   public String getWsdlFileName() {
      String var1 = this.getStubInfo().getServiceName();
      return var1 + "_saved_wsdl.wsdl";
   }

   public WsdlDefinitions getWsdlDefs() {
      return this.wsdlDefs;
   }

   public String getWsdlUrl() {
      return this.wsdlUrl;
   }

   public void setWsdlUrl(String var1) {
      this.wsdlUrl = var1;
   }

   public void setWsdlDefs(WsdlDefinitions var1) {
      this.wsdlDefs = var1;
   }

   public boolean isPartialWsdl() {
      return this.partialWsdl;
   }

   public void setPartialWsdl(boolean var1) {
      this.partialWsdl = var1;
   }

   public String getServiceName() {
      return this.serviceName;
   }

   public void setServiceName(String var1) {
      this.serviceName = var1;
   }

   public String getPackageName() {
      return this.packageName;
   }

   public void setPackageName(String var1) {
      this.packageName = var1;
   }

   public JsService getJsService() {
      return this.jsService;
   }

   public void setJsService(JsService var1) {
      this.jsService = var1;
   }

   public JsService getJsCallbackService() {
      return this.callbackService;
   }

   public void setJsCallbackService(JsService var1) {
      this.callbackService = var1;
   }

   public File getDestDir() {
      return this.destDir;
   }

   public void setDestDir(File var1) {
      this.destDir = var1;
   }

   public File getHandlerChain() {
      return this.handlerChain;
   }

   public void setHandlerChain(File var1) {
      this.handlerChain = var1;
   }

   public StubInfo getStubInfo() {
      return this.stubInfo;
   }

   public void setStubInfo(StubInfo var1) {
      this.stubInfo = var1;
   }

   public void setGeneratePolicyMethods(boolean var1) {
      this.generatePolicyMethods = var1;
   }

   public boolean getGeneratePolicyMethods() {
      return this.generatePolicyMethods;
   }

   public void setGenerateAsyncMethods(boolean var1) {
      this.generateAsyncMethods = var1;
   }

   public boolean isGenerateAsyncMethods() {
      return this.generateAsyncMethods;
   }

   public void setCallbackJClass(JClass var1) {
      this.callbackJClass = var1;
   }

   public JClass getCallbackJClass() {
      return this.callbackJClass;
   }

   public void setWebServiceJClass(JClass var1) {
      this.webserviceJClass = var1;
   }

   public JClass getWebServiceJClass() {
      return this.webserviceJClass;
   }

   public void setJaxRPCWrappedArrayStyle(boolean var1) {
      this.jaxRPCWrappedArrayStyle = var1;
   }

   public boolean isJaxRPCWrappedArrayStyle() {
      return this.jaxRPCWrappedArrayStyle;
   }

   public void setTypeFamily(TypeFamily var1) {
      this.typeFamily = var1;
   }

   public TypeFamily getTypeFamily() {
      return this.typeFamily;
   }

   public boolean isWriteJavaTypes() {
      return this.writeJavaTypes;
   }

   public void setWriteJavaTypes(boolean var1) {
      this.writeJavaTypes = var1;
   }

   public static class StubInfo {
      private String serviceName;
      private String serviceImplName;
      private Map<String, String> portNameMap = new HashMap();
      private Map<String, String> portTypeNameMap = new HashMap();
      private Map<String, String> stubNameMap = new HashMap();

      public StubInfo(WsdlService var1) {
         this.initPorts(var1);
         String var2 = NameUtil.getJAXRPCClassName(var1.getName().getLocalPart());
         UniqueNameSet var3 = new UniqueNameSet();
         if (this.portTypeNameMap.containsValue(var2)) {
            this.serviceName = var3.add(var2 + "_Service");
         } else {
            this.serviceName = var3.add(var2);
         }

         this.serviceImplName = var3.add(this.serviceName + "_Impl");
         this.initStubs(var2, var3);
      }

      private void initStubs(String var1, UniqueNameSet var2) {
         Set var3 = this.portTypeNameMap.entrySet();
         Iterator var4 = var3.iterator();

         while(var4.hasNext()) {
            Map.Entry var5 = (Map.Entry)var4.next();
            String var6 = (String)var5.getValue();
            if (var1.equals(var6)) {
               var6 = var6 + "_PortType";
            }

            var6 = var2.add(var6);
            var5.setValue(var6);
            String var7 = var2.add(var6 + "_Stub");
            this.stubNameMap.put(var5.getKey(), var7);
         }

      }

      private void initPorts(WsdlService var1) {
         UniqueNameSet var2 = new UniqueNameSet();
         Iterator var3 = var1.getPorts().values().iterator();

         while(var3.hasNext()) {
            WsdlPort var4 = (WsdlPort)var3.next();
            this.portNameMap.put(var4.getName().getLocalPart(), var2.add(NameUtil.getJAXRPCClassName(var4.getName().getLocalPart())));
            String var5 = var4.getPortType().getName().getLocalPart();
            if (!this.portTypeNameMap.containsKey(var5)) {
               this.portTypeNameMap.put(var5, NameUtil.getJAXRPCClassName(var5));
            }
         }

      }

      public Map<String, String> getPortNameMap() {
         return this.portNameMap;
      }

      public Map<String, String> getPortTypeNameMap() {
         return this.portTypeNameMap;
      }

      public Map<String, String> getStubNameMap() {
         return this.stubNameMap;
      }

      public String getServiceName() {
         return this.serviceName;
      }

      public void setServiceName(String var1) {
         this.serviceName = var1;
      }

      public String getServiceImplName() {
         return this.serviceImplName;
      }
   }
}
