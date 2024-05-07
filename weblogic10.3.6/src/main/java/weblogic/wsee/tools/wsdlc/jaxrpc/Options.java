package weblogic.wsee.tools.wsdlc.jaxrpc;

import javax.xml.namespace.QName;
import weblogic.wsee.bind.TypeFamily;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.wsdl.WsdlDefinitions;

public class Options {
   private static final String DEFAULT_IMPLEMENTATION_TEMPLATE = "weblogic.wsee.tools.wsdlc.jaxrpc.JwsImpl";
   private static final String DEFAULT_INTERFACE_TEMPLATE = "weblogic.wsee.tools.wsdlc.jaxrpc.JwsInterface";
   private QName bindingName;
   private QName serviceName;
   private String implTemplate = "weblogic.wsee.tools.wsdlc.jaxrpc.JwsImpl";
   private String interfaceTemplate = "weblogic.wsee.tools.wsdlc.jaxrpc.JwsInterface";
   private WsdlDefinitions definitions;
   private boolean autoDetectWrapped = true;
   private boolean jaxRPCWrappedArrayStyle = true;
   private boolean sortSchemaTypes = false;
   private boolean fillIncompleteParameterOrderList = false;
   private Object codeGenBaseData;
   private TypeFamily typeFamily;
   private boolean wlw81CallbackGen;
   private boolean upgraded81Jws;
   private boolean allowWrappedArrayForDocLiteral;
   private boolean includeGlobalTypes;

   public Options() {
      this.typeFamily = TypeFamily.TYLAR;
      this.wlw81CallbackGen = false;
      this.upgraded81Jws = false;
      this.allowWrappedArrayForDocLiteral = false;
      this.includeGlobalTypes = false;
   }

   QName getBindingName() {
      return this.bindingName;
   }

   /** @deprecated */
   @Deprecated
   public void setBindingName(QName var1) {
      this.bindingName = var1;
   }

   QName getServiceName() {
      return this.serviceName;
   }

   public void setServiceName(QName var1) {
      this.serviceName = var1;
   }

   String getImplTemplate() {
      return this.implTemplate;
   }

   public void setImplTemplate(String var1) {
      if (StringUtil.isEmpty(var1)) {
         this.implTemplate = "weblogic.wsee.tools.wsdlc.jaxrpc.JwsImpl";
      } else {
         this.implTemplate = var1;
      }

   }

   String getInterfaceTemplate() {
      return this.interfaceTemplate;
   }

   public void setInterfaceTemplate(String var1) {
      if (StringUtil.isEmpty(var1)) {
         this.interfaceTemplate = "weblogic.wsee.tools.wsdlc.jaxrpc.JwsInterface";
      } else {
         this.interfaceTemplate = var1;
      }

   }

   WsdlDefinitions getDefinitions() {
      return this.definitions;
   }

   public void setDefinitions(WsdlDefinitions var1) {
      if (var1 == null) {
         throw new NullPointerException("No definitions specified");
      } else {
         this.definitions = var1;
      }
   }

   boolean isAutoDetectWrapped() {
      return this.autoDetectWrapped;
   }

   public void setAutoDetectWrapped(boolean var1) {
      this.autoDetectWrapped = var1;
   }

   boolean isJaxRPCWrappedArrayStyle() {
      return this.jaxRPCWrappedArrayStyle;
   }

   public void setJaxRPCWrappedArrayStyle(boolean var1) {
      this.jaxRPCWrappedArrayStyle = var1;
   }

   boolean isIncludeGlobalTypes() {
      return this.includeGlobalTypes;
   }

   public void setIncludeGlobalTypes(boolean var1) {
      this.includeGlobalTypes = var1;
   }

   Object getCodeGenBaseData() {
      return this.codeGenBaseData;
   }

   public void setCodeGenBaseData(Object var1) {
      this.codeGenBaseData = var1;
   }

   public TypeFamily getTypeFamily() {
      return this.typeFamily;
   }

   public void setTypeFamily(TypeFamily var1) {
      if (var1 == null) {
         var1 = TypeFamily.TYLAR;
      } else {
         this.typeFamily = var1;
      }

   }

   boolean isWlw81CallbackGen() {
      return this.wlw81CallbackGen;
   }

   public void setWlw81CallbackGen(boolean var1) {
      this.wlw81CallbackGen = var1;
   }

   public boolean isUpgraded81Jws() {
      return this.upgraded81Jws;
   }

   public void setUpgraded81Jws(boolean var1) {
      this.upgraded81Jws = var1;
   }

   public void setAllowWrappedArrayForDocLiteral(boolean var1) {
      this.allowWrappedArrayForDocLiteral = var1;
   }

   public boolean isAllowWrappedArrayForDocLiteral() {
      return this.allowWrappedArrayForDocLiteral;
   }

   public void setSortSchemaTypes(boolean var1) {
      this.sortSchemaTypes = var1;
   }

   public boolean isSortSchemaTypes() {
      return this.sortSchemaTypes;
   }

   public void setFillIncompleteParameterOrderList(boolean var1) {
      this.fillIncompleteParameterOrderList = var1;
   }

   public boolean isFillIncompleteParameterOrderList() {
      return this.fillIncompleteParameterOrderList;
   }
}
