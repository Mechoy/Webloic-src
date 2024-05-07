package weblogic.wsee.tools.clientgen.jaxrpc;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import weblogic.wsee.bind.TypeFamily;
import weblogic.wsee.util.Verbose;

public class Options {
   private String serviceName;
   private File handlerChain;
   private boolean generatePolicyMethods;
   private boolean jaxRPCWrappedArrayStyle = true;
   private boolean generateAsyncMethods = true;
   private TypeFamily typeFamily;
   private boolean autoDetectWrapped;
   private boolean includeGlobalTypes;
   private boolean sortSchemaTypes;
   private boolean fillIncompleteParameterOrderList;
   private Properties systemProperties;
   private boolean writeJavaTypes;
   private boolean useJaxRpcRules;
   private List<ClientGenProcessor> processors;
   private boolean allowWrappedArrayForDocLiteral;
   private static HashSet<String> supportedSysPropNames = new HashSet();

   public Options() {
      this.typeFamily = TypeFamily.TYLAR;
      this.autoDetectWrapped = true;
      this.includeGlobalTypes = false;
      this.sortSchemaTypes = false;
      this.fillIncompleteParameterOrderList = false;
      this.systemProperties = new Properties();
      this.writeJavaTypes = true;
      this.useJaxRpcRules = true;
      this.processors = new ArrayList();
      this.allowWrappedArrayForDocLiteral = false;
   }

   public void addSysemProperty(String var1, String var2) {
      if (supportedSysPropNames.contains(var1)) {
         this.systemProperties.setProperty(var1, var2);
      } else {
         Verbose.say("System property \"" + var2 + "\" is not supported");
      }

   }

   Properties getSystemProperties() {
      return this.systemProperties;
   }

   boolean isWriteJavaTypes() {
      return this.writeJavaTypes;
   }

   public void setWriteJavaTypes(boolean var1) {
      this.writeJavaTypes = var1;
   }

   String getServiceName() {
      return this.serviceName;
   }

   public void setServiceName(String var1) {
      if (var1 != null) {
         var1 = var1.trim();
      }

      this.serviceName = var1;
   }

   File getHandlerChain() {
      return this.handlerChain;
   }

   public void setHandlerChain(File var1) {
      this.handlerChain = var1;
   }

   boolean isGeneratePolicyMethods() {
      return this.generatePolicyMethods;
   }

   public void setGeneratePolicyMethods(boolean var1) {
      this.generatePolicyMethods = var1;
   }

   boolean isJaxRPCWrappedArrayStyle() {
      return this.jaxRPCWrappedArrayStyle;
   }

   public void setJaxRPCWrappedArrayStyle(boolean var1) {
      this.jaxRPCWrappedArrayStyle = var1;
   }

   boolean isGenerateAsyncMethods() {
      return this.generateAsyncMethods;
   }

   public void setGenerateAsyncMethods(boolean var1) {
      this.generateAsyncMethods = var1;
   }

   TypeFamily getTypeFamily() {
      return this.typeFamily;
   }

   public void setTypeFamily(TypeFamily var1) {
      this.typeFamily = var1;
   }

   boolean isAutoDetectWrapped() {
      return this.autoDetectWrapped;
   }

   public void setAutoDetectWrapped(boolean var1) {
      this.autoDetectWrapped = var1;
   }

   boolean isIncludeGlobalTypes() {
      return this.includeGlobalTypes;
   }

   public void setIncludeGlobalTypes(boolean var1) {
      this.includeGlobalTypes = var1;
   }

   boolean isSortSchemaTypes() {
      return this.sortSchemaTypes;
   }

   public void setSortSchemaTypes(boolean var1) {
      this.sortSchemaTypes = var1;
   }

   public void setFillIncompleteParameterOrderList(boolean var1) {
      this.fillIncompleteParameterOrderList = var1;
   }

   public boolean isFillIncompleteParameterOrderList() {
      return this.fillIncompleteParameterOrderList;
   }

   boolean isUseJaxRpcRules() {
      return this.useJaxRpcRules;
   }

   public void setUseJaxRpcRules(boolean var1) {
      this.useJaxRpcRules = var1;
   }

   public void addProcessor(ClientGenProcessor var1) {
      this.processors.add(var1);
   }

   List<ClientGenProcessor> getProcessors() {
      return this.processors;
   }

   public void setAllowWrappedArrayForDocLiteral(boolean var1) {
      this.allowWrappedArrayForDocLiteral = var1;
   }

   public boolean isAllowWrappedArrayForDocLiteral() {
      return this.allowWrappedArrayForDocLiteral;
   }

   static {
      supportedSysPropNames.add("javax.xml.rpc.security.auth.username");
      supportedSysPropNames.add("javax.xml.rpc.security.auth.password");
      supportedSysPropNames.add("javax.net.ssl.trustStore");
      supportedSysPropNames.add("javax.net.ssl.trustStorePassword");
      supportedSysPropNames.add("javax.net.ssl.keyStore");
      supportedSysPropNames.add("javax.net.ssl.keyStorePassword");
      supportedSysPropNames.add("weblogic.wsee.client.ssl.stricthostchecking");
      supportedSysPropNames.add("java.protocol.handler.pkgs");
      supportedSysPropNames.add("weblogic.security.SSL.trustedCAKeyStore");
      supportedSysPropNames.add("weblogic.security.SSL.ignoreHostnameVerification");
   }
}
