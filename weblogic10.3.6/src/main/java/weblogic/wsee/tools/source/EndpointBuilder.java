package weblogic.wsee.tools.source;

import com.bea.staxb.buildtime.FaultMessage;
import com.bea.staxb.buildtime.WrappedOperationInfo;
import com.bea.xbean.common.NameUtil;
import com.bea.xbean.xb.xsdschema.SchemaDocument;
import com.bea.xml.XmlException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.rpc.holders.ByteArrayHolder;
import weblogic.wsee.bind.TypeFamily;
import weblogic.wsee.bind.buildtime.BuildtimeBindings;
import weblogic.wsee.bind.buildtime.S2JBindingsBuilder;
import weblogic.wsee.bind.buildtime.TylarS2JBindingsBuilder;
import weblogic.wsee.jaxrpc.Mime2JavaMapping;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.util.ExceptionInfo;
import weblogic.wsee.util.ExceptionUtil;
import weblogic.wsee.util.HolderUtil;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.UniqueNameSet;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.wsdl.WsdlBinding;
import weblogic.wsee.wsdl.WsdlBindingMessage;
import weblogic.wsee.wsdl.WsdlBindingOperation;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlExtension;
import weblogic.wsee.wsdl.WsdlMessage;
import weblogic.wsee.wsdl.WsdlMethod;
import weblogic.wsee.wsdl.WsdlOperation;
import weblogic.wsee.wsdl.WsdlParameter;
import weblogic.wsee.wsdl.WsdlPart;
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.wsee.wsdl.WsdlPortType;
import weblogic.wsee.wsdl.WsdlUtils;
import weblogic.wsee.wsdl.builder.WsdlBindingBuilder;
import weblogic.wsee.wsdl.builder.WsdlDefinitionsBuilder;
import weblogic.wsee.wsdl.builder.WsdlMessageBuilder;
import weblogic.wsee.wsdl.builder.WsdlOperationBuilder;
import weblogic.wsee.wsdl.builder.WsdlPortBuilder;
import weblogic.wsee.wsdl.builder.WsdlPortTypeBuilder;
import weblogic.wsee.wsdl.builder.WsdlServiceBuilder;
import weblogic.wsee.wsdl.builder.WsdlTypesBuilder;
import weblogic.wsee.wsdl.mime.MimeContent;
import weblogic.wsee.wsdl.mime.MimeMultipartRelated;
import weblogic.wsee.wsdl.mime.MimePart;
import weblogic.wsee.wsdl.soap11.SoapBinding;
import weblogic.wsee.wsdl.soap11.SoapBindingOperation;
import weblogic.wsee.wsdl.soap11.SoapBindingUtil;
import weblogic.wsee.wsdl.soap11.SoapHeader;
import weblogic.wsee.wsdl.soap12.Soap12Binding;
import weblogic.wsee.wsdl.soap12.Soap12BindingOperation;

public class EndpointBuilder {
   private static final boolean verbose = Verbose.isVerbose(EndpointBuilder.class);
   private WsdlDefinitionsBuilder definitions;
   private BuildtimeBindings bindingProvider;
   private String packageName;
   private boolean autoDetectWrapped;
   private boolean jaxRPCWrappedArrayStyle;
   private boolean generateAsyncMethods;
   private boolean alwaysUseDataHandlerForMimeTypes;
   private boolean allowWrappedArrayForDocLiteral;
   private boolean sortSchemaTypes;
   private boolean includeGlobalTypes;
   private boolean fillIncompleteParameterOrderList;
   private Map<QName, JsClass> endpoints;
   private boolean wlw81CallbackGen;

   public EndpointBuilder(WsdlDefinitions var1, File var2, String var3) throws IOException, XmlException {
      this(var1, var2, var3, true, true);
   }

   public EndpointBuilder(WsdlDefinitions var1, File var2, String var3, boolean var4, boolean var5) throws IOException, XmlException {
      this(var1, var2, var3, var4, var5, TypeFamily.TYLAR, (ClassLoader)null, (File[])null, false, false, false, false);
   }

   public EndpointBuilder(WsdlDefinitions var1, File var2, String var3, boolean var4, boolean var5, TypeFamily var6, ClassLoader var7, boolean var8) throws IOException, XmlException {
      this(var1, var2, var3, var4, var5, var6, var7, (File[])null, var8, false, false, false);
   }

   public EndpointBuilder(WsdlDefinitions var1, File var2, String var3, boolean var4, boolean var5, TypeFamily var6, ClassLoader var7, File[] var8, boolean var9, boolean var10, boolean var11, boolean var12) throws IOException, XmlException {
      this.autoDetectWrapped = true;
      this.jaxRPCWrappedArrayStyle = true;
      this.generateAsyncMethods = true;
      this.alwaysUseDataHandlerForMimeTypes = false;
      this.allowWrappedArrayForDocLiteral = false;
      this.sortSchemaTypes = false;
      this.includeGlobalTypes = false;
      this.fillIncompleteParameterOrderList = false;
      this.wlw81CallbackGen = false;
      this.definitions = (WsdlDefinitionsBuilder)var1;
      this.packageName = var3;
      this.setJaxRPCWrappedArrayStyle(var4);
      this.setAutoDetectWrapped(var5);
      this.setAllowWrappedArrayForDocLiteral(var9);
      this.setSortSchemaTypes(var10);
      this.setFillIncompleteParameterOrderList(var11);
      this.setIncludeGlobalTypes(var12);
      S2JBindingsBuilder var13 = this.createBindingsBuilder(var6, var8);
      if (var7 != null) {
         var13.addBaseTypeLibrary(var7);
      }

      this.bindingProvider = setupBindingProvider(var13, this.definitions, var2, (WsdlServiceBuilder)null, var5, var12, this.allowWrappedArrayForDocLiteral, var10);
   }

   public void setWlw81CallbackGen(boolean var1) {
      this.wlw81CallbackGen = var1;
   }

   private S2JBindingsBuilder createBindingsBuilder(TypeFamily var1, File[] var2) {
      Object var3 = null;
      if (var1.equals(TypeFamily.TYLAR)) {
         var3 = S2JBindingsBuilder.Factory.createTylarBindingsBuilder();
         ((TylarS2JBindingsBuilder)var3).setJaxRPCWrappedArrayStyle(this.jaxRPCWrappedArrayStyle);
      } else if (var1.equals(TypeFamily.XMLBEANS)) {
         var3 = S2JBindingsBuilder.Factory.createXmlBeansBindingsBuilder();
      } else if (var1.equals(TypeFamily.XMLBEANS_APACHE)) {
         var3 = S2JBindingsBuilder.Factory.createXmlBeansApacheBindingsBuilder();
      }

      ((S2JBindingsBuilder)var3).setXsdConfig(var2);
      return (S2JBindingsBuilder)var3;
   }

   public EndpointBuilder(WsdlDefinitions var1, BuildtimeBindings var2, String var3) {
      this.autoDetectWrapped = true;
      this.jaxRPCWrappedArrayStyle = true;
      this.generateAsyncMethods = true;
      this.alwaysUseDataHandlerForMimeTypes = false;
      this.allowWrappedArrayForDocLiteral = false;
      this.sortSchemaTypes = false;
      this.includeGlobalTypes = false;
      this.fillIncompleteParameterOrderList = false;
      this.wlw81CallbackGen = false;
      this.definitions = (WsdlDefinitionsBuilder)var1;
      this.packageName = var3;
      this.bindingProvider = var2;
   }

   public BuildtimeBindings getBuildtimeBindings() {
      return this.bindingProvider;
   }

   public String getPackageName() {
      if (StringUtil.isEmpty(this.packageName)) {
         this.packageName = NameUtil.getPackageFromNamespace(this.definitions.getTargetNamespace(), true);
      }

      return this.packageName;
   }

   public void setAutoDetectWrapped(boolean var1) {
      this.autoDetectWrapped = var1;
   }

   public void setJaxRPCWrappedArrayStyle(boolean var1) {
      this.jaxRPCWrappedArrayStyle = var1;
   }

   public void setGenerateAsyncMethods(boolean var1) {
      this.generateAsyncMethods = var1;
   }

   public void setAlwaysUseDataHandlerForMimeTypes(boolean var1) {
      this.alwaysUseDataHandlerForMimeTypes = var1;
   }

   public Map<QName, JsClass> build() throws WsdlException {
      this.endpoints = new HashMap();
      Iterator var1 = this.definitions.getServices().values().iterator();

      while(var1.hasNext()) {
         WsdlServiceBuilder var2 = (WsdlServiceBuilder)var1.next();
         Iterator var3 = var2.getPorts().values().iterator();

         while(var3.hasNext()) {
            WsdlPortBuilder var4 = (WsdlPortBuilder)var3.next();
            WsdlBindingBuilder var5 = var4.getBinding();
            if (this.endpoints.get(var5.getName()) == null && this.getAnySoapBinding(var5) != null) {
               JsClass var6 = this.buildEndpoint(var5);
               this.markSoapHeaderPart(var5, var6);
               if (verbose) {
                  Verbose.log((Object)("Endpoint class: " + var6));
               }

               this.endpoints.put(var5.getName(), var6);
            }
         }
      }

      return this.endpoints;
   }

   private SoapBinding getAnySoapBinding(WsdlBinding var1) {
      Object var2 = SoapBinding.narrow(var1);
      if (var2 == null) {
         var2 = Soap12Binding.narrow(var1);
      }

      return (SoapBinding)var2;
   }

   private SoapBindingOperation getAnySoapBindingOperation(WsdlBindingOperation var1) {
      Object var2 = SoapBindingOperation.narrow(var1);
      if (var2 == null) {
         var2 = Soap12BindingOperation.narrow(var1);
      }

      return (SoapBindingOperation)var2;
   }

   public JsClass buildJsClass(QName var1) throws WsBuildException {
      Iterator var2 = this.definitions.getServices().values().iterator();

      while(var2.hasNext()) {
         WsdlServiceBuilder var3 = (WsdlServiceBuilder)var2.next();
         WsdlPort var4 = (WsdlPort)var3.getPorts().get(var1);
         if (var4 != null) {
            WsdlBinding var5 = var4.getBinding();
            JsClass var6 = (JsClass)this.endpoints.get(var5.getName());
            if (var6 != null) {
               return var6;
            }
         }
      }

      throw new WsBuildException("Could not find Endpoint for port " + var1);
   }

   public JsService buildJsService(QName var1) throws WsdlException {
      WsdlServiceBuilder var2 = (WsdlServiceBuilder)this.definitions.getServices().get(var1);
      if (var2 == null) {
         throw new IllegalArgumentException("Wsdl service " + var1 + " is not found in wsdl definition");
      } else {
         JsService var3 = new JsService(var2);
         HashMap var4 = new HashMap();
         Iterator var5 = var2.getPorts().values().iterator();

         while(var5.hasNext()) {
            WsdlPortBuilder var6 = (WsdlPortBuilder)var5.next();
            if (this.getAnySoapBinding(var6.getBinding()) != null) {
               JsClass var7 = (JsClass)var4.get(var6.getBinding().getPortType().getName());
               if (var7 == null) {
                  var7 = this.buildEndpoint(var6.getBinding());
                  this.markSoapHeaderPart(var6.getBinding(), var7);
                  var4.put(var6.getBinding().getPortType().getName(), var7);
               }

               var3.addPort(var6.getName().getLocalPart(), var6, var7);
            }
         }

         return var3;
      }
   }

   private void markSoapHeaderPart(WsdlBinding var1, JsClass var2) throws WsdlException {
      WsdlPortType var3 = var1.getPortType();
      SoapBinding var4 = this.getAnySoapBinding(var1);
      if (var4 != null) {
         Iterator var5 = var1.getOperations().values().iterator();

         while(var5.hasNext()) {
            WsdlBindingOperation var6 = (WsdlBindingOperation)var5.next();

            JsMethod var7;
            try {
               var7 = var2.getMethod(var6.getName());
            } catch (IllegalArgumentException var10) {
               var7 = null;
            }

            if (var7 != null) {
               QName var8 = var6.getName();
               WsdlOperation var9 = (WsdlOperation)var3.getOperations().get(var8);
               if (var9 == null) {
                  throw new WsdlException("There is a problem with the wsdl; the 'wsdl:binding' operation: \n" + var8 + "\n" + "does not have a matching 'wsdl:portType' operation.\n");
               }

               if (var6.getInput() != null) {
                  this.findSoapHeaderPart(var7, var8, var6.getInput(), var9.getInput());
                  this.findMimeBinding(var7, var6.getInput());
               }

               if (var6.getOutput() != null) {
                  this.findSoapHeaderPart(var7, var8, var6.getOutput(), var9.getOutput());
                  this.findMimeBinding(var7, var6.getOutput());
                  this.findMimeBindingForReturnType(var7, var6.getOutput());
               }
            }
         }

      }
   }

   private static String findMimeContent(String var0, List<MimeContent> var1) {
      Iterator var2 = var1.iterator();

      MimeContent var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (MimeContent)var2.next();
      } while(!var0.equals(var3.getPart()));

      return var3.getType();
   }

   private void findMimeBinding(JsMethod var1, WsdlBindingMessage var2) {
      MimeMultipartRelated var3 = MimeMultipartRelated.narrow(var2);
      if (var3 != null) {
         JsParameterType[] var4 = var1.getArguments();
         List var5 = var3.getParts();
         ArrayList var6 = new ArrayList();
         var1.resetArgument(var6);
         ArrayList var7 = new ArrayList();
         Iterator var8 = var5.iterator();

         while(var8.hasNext()) {
            MimePart var9 = (MimePart)var8.next();
            var7.addAll(MimeContent.narrow(var9));
         }

         for(int var11 = 0; var11 < var4.length; ++var11) {
            JsParameterType var12 = var4[var11];
            String var10 = null;
            if ((var10 = findMimeContent(var12.getPartName(), var7)) != null) {
               var1.addJsParameterType(this.updateParam(var12, var10));
            } else {
               var1.addJsParameterType(var12);
            }
         }

         this.resolveUniqueParamName(var1);
      }

   }

   private void findMimeBindingForReturnType(JsMethod var1, WsdlBindingMessage var2) {
      if (!"void".equals(var1.getReturnType().getType())) {
         MimeMultipartRelated var3 = MimeMultipartRelated.narrow(var2);
         if (var3 != null) {
            JsReturnType var4 = var1.getReturnType();
            List var5 = var3.getParts();
            ArrayList var6 = new ArrayList();
            Iterator var7 = var5.iterator();

            while(var7.hasNext()) {
               MimePart var8 = (MimePart)var7.next();
               var6.addAll(MimeContent.narrow(var8));
            }

            var7 = null;
            String var9;
            if ((var9 = findMimeContent(var4.getPartName(), var6)) != null) {
               var4.setType(this.getJavaType(var9));
            }
         }
      }

   }

   private JsParameterType updateParam(JsParameterType var1, String var2) {
      if (var1.getMode() > 0) {
         String var3 = this.getJavaType(var2);
         var1.setNonHolderType(var3);
         var1.setType(HolderUtil.getMimeBindingJavaHolderTypeFromMimeBindingJavaType(var3));
      } else {
         var1.setType(this.getJavaType(var2));
      }

      return var1;
   }

   private String getJavaType(String var1) {
      String var2;
      if (this.alwaysUseDataHandlerForMimeTypes) {
         var2 = "javax.activation.DataHandler";
      } else {
         var2 = Mime2JavaMapping.getJavaTypeFromMime(var1);
      }

      if (var2 == null) {
         var2 = "javax.activation.DataHandler";
      }

      return var2;
   }

   private void findSoapHeaderPart(JsMethod var1, QName var2, WsdlBindingMessage var3, WsdlMessage var4) {
      QName var5 = var4.getName();
      Iterator var6 = var3.getExtensions().values().iterator();

      label43:
      while(var6.hasNext()) {
         List var7 = (List)var6.next();
         Iterator var8 = var7.iterator();

         while(true) {
            while(true) {
               WsdlExtension var9;
               do {
                  if (!var8.hasNext()) {
                     continue label43;
                  }

                  var9 = (WsdlExtension)var8.next();
               } while(!(var9 instanceof SoapHeader));

               SoapHeader var10 = (SoapHeader)var9;
               if (var5.equals(var10.getMessage())) {
                  String var11 = var10.getPart();
                  JsParameterType[] var12 = var1.getArguments();

                  for(int var13 = 0; var13 < var12.length; ++var13) {
                     JsParameterType var14 = var12[var13];
                     if (var11.equals(var14.getPartName())) {
                        var14.setSoapHeader(true);
                     }
                  }
               } else {
                  System.out.println("[WARNING] The binding soap:header element for binding operation: \n" + var2 + "\n" + "is being IGNORED because its message: \n" + var10.getMessage() + "\n" + "does not match the corresponding input/output portType message: \n" + var5 + "\n" + "To handle this soap header, you need to add a handler to your client.\n");
               }
            }
         }
      }

   }

   private JsClass buildEndpoint(WsdlBindingBuilder var1) {
      WsdlPortTypeBuilder var2 = var1.getPortType();
      if (verbose) {
         Verbose.log((Object)("building port type:" + var2.getName()));
      }

      JsClass var3 = new JsClass();
      var3.setName(var2.getName().getLocalPart());
      var3.setNamespaceURI(var2.getName().getNamespaceURI());
      var3.setBindingName(var1.getName());
      var3.setPackageName(this.getPackageName());
      SoapBinding var4 = this.getAnySoapBinding(var1);
      if (var4 == null) {
         throw new IllegalArgumentException("Wsdl binding " + var1.getName() + " is not a soap doesn't have a soap binding.");
      } else {
         WsdlOperationBuilder var6;
         String var8;
         String var9;
         String var10;
         for(Iterator var5 = var2.getOperations().values().iterator(); var5.hasNext(); this.addMethod(var3, var6, var8, var10, var9)) {
            var6 = (WsdlOperationBuilder)var5.next();
            WsdlBindingOperation var7 = (WsdlBindingOperation)var1.getOperations().get(var6.getName());
            var8 = null;
            var9 = null;
            var10 = "";
            if (var7 != null) {
               SoapBindingOperation var11 = this.getAnySoapBindingOperation(var7);
               var8 = SoapBindingUtil.getStyle(var11, var4);
               var9 = SoapBindingUtil.getUse(var7.getInput(), var7.getOutput());
               if (var8.equals("document") && var9 != null && var9.equals("encoded")) {
                  throw new IllegalArgumentException("Document Encoded style webservice is not supported");
               }

               if (var11 != null) {
                  var10 = var11.getSoapAction();
               }
            }
         }

         resolveMethodNames(var3);
         return var3;
      }
   }

   private static void resolveMethodNames(JsClass var0) {
      UniqueNameSet var1 = new UniqueNameSet();
      JsMethod[] var2 = var0.getMethods();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         JsMethod var4 = var2[var3];
         String var5 = weblogic.xml.schema.binding.internal.NameUtil.getJAXRPCMethodName(var4.getOperationName().getLocalPart());
         var4.setMethodName(var1.add(var5));
      }

   }

   protected void addMethod(JsClass var1, WsdlOperationBuilder var2, String var3, String var4, String var5) {
      if (verbose) {
         Verbose.log((Object)("Building operation: " + var2.getName()));
      }

      boolean var6 = var2.isWLW81CallbackOperation();
      if ((!this.wlw81CallbackGen || var6) && (this.wlw81CallbackGen || !var6)) {
         boolean var7 = this.detectWrappedVirtual(this.autoDetectWrapped, var2);
         JsMethod var8 = var1.addMethod(var2.getName());
         var8.setStyle(var3);
         var8.setUse(var5);
         if (var2.getType() == 1) {
            var8.setOneWay(true);
         }

         var8.setGenerateAsync(this.generateAsyncMethods);
         var8.setWrapped(var7);
         var8.setSoapAction(var4);
         if ("document".equals(var3)) {
            this.createDocumentParams(var2, var8);
         } else {
            this.createParams(false, var8, var2);
         }

         if (this.bindingProvider.useCheckedExceptionFromWsdlFault()) {
            this.addExceptions(var8, var2);
         }

         this.resolveUniqueParamName(var8);
      }
   }

   private void createDocumentParams(WsdlOperationBuilder var1, JsMethod var2) {
      if (var2.isWrapped()) {
         WsdlMessageBuilder var3;
         WsdlMessageBuilder var4;
         if (var1.isWrappedWLW81Callback()) {
            var3 = var1.getOutput();
            var4 = var1.getInput();
         } else {
            var3 = var1.getInput();
            var4 = var1.getOutput();
         }

         WsdlPart var5 = (WsdlPart)var3.getParts().values().iterator().next();
         LinkedHashMap var6 = this.bindingProvider.getJavaTypesForWrapperElement(var5.getElement());
         LinkedHashMap var7 = this.getRtnTypesFromWrappedOutputMessage(var4);
         if (var6 != null && var7 != null && var7.size() <= 1) {
            Iterator var8 = var6.entrySet().iterator();

            while(var8.hasNext()) {
               Map.Entry var9 = (Map.Entry)var8.next();
               JsParameterType var10 = var2.addArgument((String)var9.getKey(), (String)var9.getValue());
               var10.setMode(0);
            }

            if (var4 != null && var7.size() == 1) {
               Map.Entry var11 = (Map.Entry)var7.entrySet().iterator().next();
               JsReturnType var12 = var2.setReturnType((String)var11.getValue());
               var12.setPartName((String)var11.getKey());
            }

            return;
         }
      }

      var2.setWrapped(false);
      this.createParams(true, var2, var1);
   }

   private LinkedHashMap getRtnTypesFromWrappedOutputMessage(WsdlMessage var1) {
      if (var1 != null) {
         WsdlPart var2 = (WsdlPart)var1.getParts().values().iterator().next();
         LinkedHashMap var3 = this.bindingProvider.getJavaTypesForWrapperElement(var2.getElement());
         return var3;
      } else {
         return new LinkedHashMap();
      }
   }

   private void createParams(boolean var1, JsMethod var2, WsdlOperation var3) {
      WsdlMethod var4 = var3.getWsdlMethod(this.fillIncompleteParameterOrderList);
      Iterator var5 = var4.getParameters().iterator();

      while(var5.hasNext()) {
         WsdlParameter var6 = (WsdlParameter)var5.next();
         this.addPart(var2, var6.getInPart(), var6.getOutPart(), var1);
      }

      WsdlPart var7 = var4.getResultPart();
      if (var7 != null) {
         this.setReturnType(var2, var7, var1);
      }

   }

   private void addExceptions(JsMethod var1, WsdlOperation var2) {
      var2.getFaults().values();
      Iterator var3 = var2.getFaults().values().iterator();

      WsdlMessage var4;
      Iterator var5;
      do {
         if (!var3.hasNext()) {
            return;
         }

         var4 = (WsdlMessage)var3.next();
         var5 = var4.getParts().values().iterator();
         if (!var5.hasNext()) {
            throw new IllegalStateException("Fault WsdlMessage " + var4.getName() + " doens't have a single part.");
         }

         WsdlPart var6 = (WsdlPart)var5.next();
         ExceptionInfo var7 = ExceptionUtil.getExceptionInfo(this.bindingProvider, var4);
         if (verbose) {
            Verbose.log((Object)var7.toString());
         }

         String var8 = var7.getOnWireClassName();
         String var9 = var7.getSimpleExceptionWrapperClassName();
         List var10 = var7.getConstructorElementNames();
         if (var8 == null) {
            throw new AssertionError("Java exception mapping for wsdl message " + var4.getName() + " is not found.");
         }

         var1.addFault(var4.getName(), var6.getName(), var8, var9, var10);
      } while(!var5.hasNext());

      throw new IllegalStateException("Fault WsdlMessage " + var4.getName() + " has more than one part.");
   }

   private void resolveUniqueParamName(JsMethod var1) {
      UniqueNameSet var2 = new UniqueNameSet();
      JsParameterType[] var3 = var1.getArguments();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         JsParameterType var5 = var3[var4];
         String var6 = var2.add(JsTypeBase.getJavaName(var5.getPartName()));
         var5.setParamName(var6);
         var5.setPartName(var5.getPartName());
      }

   }

   private void setReturnType(JsMethod var1, WsdlPart var2, boolean var3) {
      if (verbose) {
         Verbose.log((Object)("building return type: " + var2.getName()));
      }

      String var4 = this.getJavaClassName(var2);
      JsReturnType var5 = var1.setReturnType(var4);
      var5.setPartName(var2.getName());
      if (var3) {
         var5.setElement(var2.getElement());
      } else {
         var5.setDocStyle(false);
      }

   }

   private void addPart(JsMethod var1, WsdlPart var2, WsdlPart var3, boolean var4) {
      JsParameterType var5;
      QName var6;
      String var7;
      if (var2 != null) {
         var6 = var2.getElement();
         var7 = this.getJavaClassName(var2);
         var5 = var1.addArgument(var2.getName(), var7);
         if (var3 != null && (!var4 || var6 != null)) {
            var5.setMode(2);
            var5.setNonHolderType(var7);
            if (var6 != null) {
               var5.setType(HolderUtil.NameCollisionsFilter.getInstance().filterFullName(this.getHolderClass(var7, var6)));
            } else {
               var5.setType(HolderUtil.NameCollisionsFilter.getInstance().filterFullName(this.getHolderClass(var7, var2.getType())));
            }
         } else {
            var5.setMode(0);
         }
      } else {
         var6 = var3.getElement();
         var7 = this.getJavaClassName(var3);
         var5 = var1.addArgument(var3.getName(), var7);
         var5.setMode(1);
         var5.setNonHolderType(var7);
         if (var6 != null) {
            var5.setType(HolderUtil.NameCollisionsFilter.getInstance().filterFullName(this.getHolderClass(var7, var6)));
         } else {
            var5.setType(HolderUtil.NameCollisionsFilter.getInstance().filterFullName(this.getHolderClass(var7, var3.getType())));
         }
      }

      if (var4) {
         var5.setElement(var6);
      } else {
         var5.setDocStyle(false);
      }

   }

   private String getHolderClass(String var1, QName var2) {
      String var3 = HolderUtil.getStandardHolder(var1);
      if (var3 != null) {
         return var3;
      } else {
         String var4 = var2.getNamespaceURI();
         if ("http://www.w3.org/2001/XMLSchema".equals(var4) || "http://schemas.xmlsoap.org/soap/encoding/".equals(var4) || "http://www.w3.org/2003/05/soap-encoding".equals(var4)) {
            var3 = this.getStandardSchemaHolder(var2);
            if (var3 != null) {
               return var3;
            }
         }

         if (!var1.startsWith("java.") && !var1.startsWith("javax.") && !HolderUtil.isPrimitiveType(var1) && !var1.startsWith("[") && !var1.endsWith("[]")) {
            var3 = HolderUtil.getHolderClass(var1);
         } else {
            String var5 = NameUtil.getPackageFromNamespace(var2.getNamespaceURI(), true);
            if (StringUtil.isEmpty(var5)) {
               var5 = "holders";
            } else {
               var5 = var5 + ".holders";
            }

            String var6 = weblogic.xml.schema.binding.internal.NameUtil.getJAXRPCClassName(var2.getLocalPart());
            var3 = var5 + "." + var6 + "Holder";
         }

         return var3;
      }
   }

   private String getStandardSchemaHolder(QName var1) {
      return !"base64Binary".equals(var1.getLocalPart()) && !"hexBinary".equals(var1.getLocalPart()) && !"base64".equals(var1.getLocalPart()) ? null : ByteArrayHolder.class.getName();
   }

   private String getJavaClassName(WsdlPart var1) {
      String var2 = null;
      if (var1.getElement() != null) {
         var2 = this.bindingProvider.getClassFromSchemaElement(var1.getElement());
      } else {
         var2 = this.bindingProvider.getClassFromSchemaType(var1.getType());
      }

      return var2;
   }

   public static BuildtimeBindings setupBindingProvider(S2JBindingsBuilder var0, WsdlDefinitionsBuilder var1, File var2, WsdlServiceBuilder var3, boolean var4, boolean var5, boolean var6, boolean var7) throws XmlException, IOException {
      WsdlServiceBuilder[] var8 = null;
      if (var3 != null) {
         var8 = new WsdlServiceBuilder[]{var3};
      }

      return setupBindingProviderWithServices(var0, var1, var2, var8, var4, var5, var6, var7);
   }

   public static BuildtimeBindings setupBindingProvider(S2JBindingsBuilder var0, WsdlDefinitionsBuilder var1, File var2, WsdlServiceBuilder var3, boolean var4, boolean var5, boolean var6) throws XmlException, IOException {
      return setupBindingProvider(var0, var1, var2, var3, var4, false, var5, var6);
   }

   public static BuildtimeBindings setupBindingProviderWithServices(S2JBindingsBuilder var0, WsdlDefinitionsBuilder var1, File var2, WsdlServiceBuilder[] var3, boolean var4, boolean var5, boolean var6, boolean var7) throws XmlException, IOException {
      if (verbose) {
         Verbose.log((Object)"Setting up binding provider");
      }

      var0.setCodegenDir(var2);
      WsdlTypesBuilder var8 = var1.getTypes();
      int var10;
      if (var8 != null) {
         SchemaDocument[] var9 = var8.getSchemaArrayWithoutImport();

         for(var10 = 0; var10 < var9.length; ++var10) {
            var0.addSchemaDocument(var9[var10]);
         }
      }

      var0.includeGlobalTypes(var5);
      var0.sortSchemaTypes(var7);
      if (var3 != null && var3.length > 0) {
         WsdlServiceBuilder[] var15 = var3;
         var10 = var3.length;

         for(int var11 = 0; var11 < var10; ++var11) {
            WsdlServiceBuilder var12 = var15[var11];
            addTypesByWsdlService(var12, var0, var4, var6);
         }
      } else {
         Iterator var14 = var1.getServices().values().iterator();

         while(var14.hasNext()) {
            WsdlServiceBuilder var13 = (WsdlServiceBuilder)var14.next();
            addTypesByWsdlService(var13, var0, var4, var6);
         }
      }

      return var0.createBuildtimeBindings(var2, var1);
   }

   private static void addTypesByWsdlService(WsdlServiceBuilder var0, S2JBindingsBuilder var1, boolean var2, boolean var3) {
      if (var0 == null) {
         throw new RuntimeException("Service passed is null!");
      } else {
         if (verbose) {
            Verbose.log((Object)"Setting up binding provider");
         }

         Iterator var4 = var0.getPortTypes().iterator();

         while(var4.hasNext()) {
            WsdlPortTypeBuilder var5 = (WsdlPortTypeBuilder)var4.next();
            Iterator var6 = var5.getOperations().values().iterator();

            while(var6.hasNext()) {
               WsdlOperationBuilder var7 = (WsdlOperationBuilder)var6.next();
               if (detectWrapped(var2, var7)) {
                  WsdlMessageBuilder var8;
                  WsdlMessageBuilder var9;
                  if (var7.isWrappedWLW81Callback()) {
                     var8 = var7.getOutput();
                     var9 = var7.getInput();
                  } else {
                     var8 = var7.getInput();
                     var9 = var7.getOutput();
                  }

                  WsdlPart var10 = (WsdlPart)var8.getParts().values().iterator().next();
                  QName var11 = null;
                  if (var9 != null) {
                     WsdlPart var12 = (WsdlPart)var9.getParts().values().iterator().next();
                     var11 = var12.getElement();
                  }

                  var1.addWrapperOperation(new WrappedOperationInfo(var10.getElement(), var11, var3));
               } else {
                  if (var7.getInput() != null) {
                     addParamsToBinding(var7.getInput(), var1);
                  }

                  if (var7.getOutput() != null) {
                     addParamsToBinding(var7.getOutput(), var1);
                  }

                  if (var7.getInput() != null && var7.getOutput() != null) {
                     addInoutTypes(var7, var1);
                  }
               }

               Iterator var13 = var7.getFaults().values().iterator();

               while(var13.hasNext()) {
                  addFaultsToBinding((WsdlMessage)var13.next(), var1);
               }
            }
         }

      }
   }

   private static void addInoutTypes(WsdlOperation var0, S2JBindingsBuilder var1) {
      WsdlMessage var2 = var0.getOutput();
      String var3 = WsdlUtils.findReturnPart(var0);
      Iterator var4 = var2.getParts().values().iterator();

      while(var4.hasNext()) {
         WsdlPart var5 = (WsdlPart)var4.next();
         if (!var5.getName().equals(var3)) {
            if (var5.getType() != null) {
               var1.addHolderType(var5.getType());
            } else {
               var1.addHolderElement(var5.getElement());
            }
         }
      }

   }

   private static void addParamsToBinding(WsdlMessage var0, S2JBindingsBuilder var1) {
      Iterator var2 = var0.getParts().values().iterator();

      while(var2.hasNext()) {
         WsdlPart var3 = (WsdlPart)var2.next();
         if (var3.getType() != null) {
            var1.addParamType(var3.getType());
         } else {
            var1.addParamElement(var3.getElement());
         }
      }

   }

   private static void addFaultsToBinding(WsdlMessage var0, S2JBindingsBuilder var1) {
      Iterator var2 = var0.getParts().values().iterator();
      if (!var2.hasNext()) {
         throw new IllegalStateException("Fault WsdlMessage " + var0.getName() + " doens't have a single part.");
      } else {
         WsdlPart var3 = (WsdlPart)var2.next();
         if (var3.getType() != null) {
            var1.addFaultType(new FaultMessage(var3.getType(), var0.getName(), var3.getName()));
         } else {
            var1.addFaultElement(new FaultMessage(var3.getElement(), var0.getName(), var3.getName()));
         }

         if (var2.hasNext()) {
            throw new IllegalStateException("Fault WsdlMessage " + var0.getName() + " has more than one part.");
         }
      }
   }

   protected boolean detectWrappedVirtual(boolean var1, WsdlOperation var2) {
      return detectWrapped(var1, var2);
   }

   private static boolean detectWrapped(boolean var0, WsdlOperation var1) {
      return var0 && var1.isWrapped();
   }

   public void setAllowWrappedArrayForDocLiteral(boolean var1) {
      this.allowWrappedArrayForDocLiteral = var1;
   }

   public void setSortSchemaTypes(boolean var1) {
      this.sortSchemaTypes = var1;
   }

   public void setFillIncompleteParameterOrderList(boolean var1) {
      this.fillIncompleteParameterOrderList = var1;
   }

   public void setIncludeGlobalTypes(boolean var1) {
      this.includeGlobalTypes = var1;
   }
}
