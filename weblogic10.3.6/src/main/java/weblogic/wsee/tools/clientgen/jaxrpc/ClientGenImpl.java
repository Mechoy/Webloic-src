package weblogic.wsee.tools.clientgen.jaxrpc;

import com.bea.util.jam.JClass;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.xml.namespace.QName;
import weblogic.jws.wlw.UseWLW81BindingTypes;
import weblogic.wsee.tools.ToolsUtil;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.clientgen.ClientGen;
import weblogic.wsee.tools.clientgen.ClientGenUtil;
import weblogic.wsee.tools.clientgen.ClientGenUtilForWLW81Types;
import weblogic.wsee.tools.clientgen.ProcessInfo;
import weblogic.wsee.tools.clientgen.mapping.JaxrpcMappingProcessor;
import weblogic.wsee.tools.clientgen.serviceref.ServiceRefProcessor;
import weblogic.wsee.tools.clientgen.stubgen.StubGenProcessor;
import weblogic.wsee.tools.logging.DefaultLogger;
import weblogic.wsee.tools.logging.Logger;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.builder.WsdlDefinitionsBuilder;

public class ClientGenImpl implements ClientGen<Options> {
   private ProcessInfo processInfo = new ProcessInfo();
   private WsdlDefinitionsBuilder definitions;
   private Options options = new Options();
   private File[] bindingFiles = null;
   private Logger logger = new DefaultLogger();
   private final List<ClientGenProcessor> standardProcessors = Collections.unmodifiableList(new ArrayList(Arrays.asList(new StubGenProcessor(), new JaxrpcMappingProcessor(), new ServiceRefProcessor())));

   public ClientGenImpl() {
      this.syncProcessInfo();
   }

   public void setWsdl(String var1) {
      this.processInfo.setWsdlUrl(var1);
   }

   public void setLogger(Logger var1) {
      if (var1 == null) {
         this.logger = new DefaultLogger();
      } else {
         this.logger = var1;
      }

   }

   public void setWsdlDefinitions(WsdlDefinitions var1) {
      this.definitions = (WsdlDefinitionsBuilder)var1;
   }

   public void setBindingFiles(File[] var1) {
      this.bindingFiles = var1;
   }

   public void setOptions(Options var1) {
      if (var1 == null) {
         this.options = new Options();
      } else {
         this.options = var1;
      }

      this.syncProcessInfo();
   }

   private void syncProcessInfo() {
      this.processInfo.setServiceName(this.options.getServiceName());
      this.processInfo.setGeneratePolicyMethods(this.options.isGeneratePolicyMethods());
      this.processInfo.setJaxRPCWrappedArrayStyle(this.options.isJaxRPCWrappedArrayStyle());
      this.processInfo.setGenerateAsyncMethods(this.options.isGenerateAsyncMethods());
      this.processInfo.setHandlerChain(this.options.getHandlerChain());
      this.processInfo.setTypeFamily(this.options.getTypeFamily());
      this.processInfo.setWriteJavaTypes(this.options.isWriteJavaTypes());
   }

   public void setCallbackJClass(JClass var1) {
      this.processInfo.setCallbackJClass(var1);
   }

   public void setWebServiceJClass(JClass var1) {
      this.processInfo.setWebServiceJClass(var1);
   }

   public void setPackageName(String var1) {
      if (var1 != null) {
         var1 = var1.trim();
      }

      this.processInfo.setPackageName(var1);
   }

   public void setDestDir(File var1) {
      this.processInfo.setDestDir(var1);
   }

   public void execute() throws WsBuildException {
      this.setSystemProperties();

      try {
         this.validate();
         this.setupProcessInfo();
         Iterator var1 = this.getProcessors().iterator();

         while(var1.hasNext()) {
            ClientGenProcessor var2 = (ClientGenProcessor)var1.next();
            var2.process(this.processInfo);
         }

         this.cleanupMetaInf(this.processInfo.getDestDir());
         this.writeWsdl(this.processInfo);
      } finally {
         this.resetSystemProperties();
      }
   }

   private void validate() throws WsBuildException {
      this.validateWsdlUrl();
      this.validateTypeFamily();
      this.validateDestinationDir();
   }

   private void validateDestinationDir() throws WsBuildException {
      File var1 = this.processInfo.getDestDir();
      ToolsUtil.validateRequiredFile(var1, "destDir", this.logger);
      ToolsUtil.createDir(var1, "destDir");
   }

   private void validateTypeFamily() throws WsBuildException {
      if (this.processInfo.getTypeFamily() == null) {
         ToolsUtil.throwException("unknown type family - " + this.processInfo.getTypeFamily().getId(), this.logger);
      }

   }

   private void validateWsdlUrl() throws WsBuildException {
      if (this.definitions == null) {
         ToolsUtil.validateRequiredAttr(this.processInfo.getWsdlUrl(), "wsdl", this.logger);
      }

   }

   private List<ClientGenProcessor> getProcessors() {
      ArrayList var1 = new ArrayList(this.standardProcessors);
      var1.addAll(this.options.getProcessors());
      return var1;
   }

   private void setupProcessInfo() throws WsBuildException {
      boolean var1 = this.useWLW81Types();
      if (this.processInfo.getWsdlUrl() != null) {
         if (var1) {
            ClientGenUtilForWLW81Types.setupService(this.processInfo.getWsdlUrl(), this.processInfo.getServiceName(), this.processInfo.getWebServiceJClass(), this.processInfo, false);
         } else {
            ClientGenUtil.setupService(this.processInfo.getWsdlUrl(), this.processInfo.getServiceName(), this.processInfo, this.options.isAutoDetectWrapped(), this.options.isIncludeGlobalTypes(), this.bindingFiles, this.options.isAllowWrappedArrayForDocLiteral(), this.options.isSortSchemaTypes(), this.options.isFillIncompleteParameterOrderList());
         }
      } else {
         QName var2 = ClientGenUtil.findServiceName(this.definitions, this.processInfo.getServiceName(), this.processInfo.getWsdlUrl());
         if (var1) {
            ClientGenUtilForWLW81Types.setupBuildtimeBindingsFromScratch(this.definitions, var2, this.processInfo.getWebServiceJClass(), this.processInfo, false);
            ClientGenUtilForWLW81Types.setupJsService(this.definitions, var2, this.processInfo, this.processInfo.getWebServiceJClass());
         } else {
            ClientGenUtil.setupBuildtimeBindingsFromScratch(this.definitions, var2, this.processInfo, this.options.isAutoDetectWrapped(), this.options.isIncludeGlobalTypes(), this.options.isUseJaxRpcRules(), this.bindingFiles, this.options.isAllowWrappedArrayForDocLiteral(), this.options.isSortSchemaTypes());
            ClientGenUtil.setupJsService(this.definitions, var2, this.processInfo, this.options.isAutoDetectWrapped(), this.options.isFillIncompleteParameterOrderList());
         }
      }

   }

   private boolean useWLW81Types() {
      if (this.processInfo.getWebServiceJClass() == null) {
         return false;
      } else {
         return this.processInfo.getWebServiceJClass().getAnnotation(UseWLW81BindingTypes.class) != null;
      }
   }

   private void cleanupMetaInf(File var1) {
      File var2 = new File(var1, "META-INF/binding-file.xml");
      if (var2.exists()) {
         var2.delete();
      }

      File var3 = new File(var1, "META-INF/binding-file.ser");
      if (var3.exists()) {
         var3.delete();
      }

      File var4 = new File(var1, "META-INF/binding-mapping-file.ser");
      if (var4.exists()) {
         var4.delete();
      }

      File var5 = new File(var1, "META-INF");
      if (var5.exists() && var5.listFiles().length == 0) {
         var5.delete();
      }

   }

   private void writeWsdl(ProcessInfo var1) throws WsBuildException {
      try {
         String var2 = var1.getPackageName().replace('.', '/');
         String var3 = var2 + "/" + var1.getWsdlFileName();
         File var4 = var1.getDestDir();
         File var5 = new File(var4, var3);
         WsdlDefinitionsBuilder var6 = (WsdlDefinitionsBuilder)var1.getJsService().getWsdlService().getDefinitions();
         var6.writeToFile(var5, "UTF-8");
         String var7 = var1.getWsdlUrl();
         if (var7 != null) {
            File var8;
            try {
               var8 = new File((new URL(var7)).getPath());
            } catch (MalformedURLException var10) {
               var8 = new File(var7);
            }

            if (var8.exists()) {
               File var9 = new File(var4, var2 + "/" + var8.getName());
               var6.writeToFile(var9, "UTF-8");
            }
         }

      } catch (IOException var11) {
         throw new WsBuildException(var11);
      } catch (WsdlException var12) {
         var12.printStackTrace();
         throw new WsBuildException(var12);
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("\n*********** jax-rpc clientgen attribute settings ***************\n\n");
      var1.append("wsdlURI: " + this.processInfo.getWsdlUrl() + "\n");
      var1.append("serviceName : " + this.processInfo.getServiceName() + "\n");
      var1.append("packageName : " + this.processInfo.getPackageName() + "\n");
      var1.append("destDir : " + this.processInfo.getDestDir() + "\n");
      var1.append("handlerChainFile : " + this.processInfo.getHandlerChain() + "\n");
      var1.append("generatePolicyMethods : " + this.processInfo.getGeneratePolicyMethods() + "\n");
      var1.append("autoDetectWrapped : " + this.options.isAutoDetectWrapped() + "\n");
      var1.append("jaxRPCWrappedArrayStyle : " + this.processInfo.isJaxRPCWrappedArrayStyle() + "\n");
      var1.append("generateAsyncMethods : " + this.processInfo.isGenerateAsyncMethods() + "\n");
      var1.append("\n*********** jax-rpc clientgen attribute settings end ***************\n");
      return var1.toString();
   }

   private void setSystemProperties() {
      Properties var1 = this.options.getSystemProperties();
      Iterator var2 = var1.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry var3 = (Map.Entry)var2.next();
         System.setProperty((String)var3.getKey(), (String)var3.getValue());
      }

   }

   private void resetSystemProperties() {
      Properties var1 = this.options.getSystemProperties();
      Enumeration var2 = var1.keys();

      while(var2.hasMoreElements()) {
         System.setProperty((String)var2.nextElement(), "");
      }

   }
}
