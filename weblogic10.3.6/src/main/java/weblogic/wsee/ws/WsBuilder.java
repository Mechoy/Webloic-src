package weblogic.wsee.ws;

import com.bea.staxb.buildtime.WildcardUtil;
import com.bea.staxb.buildtime.internal.bts.XmlTypeName;
import com.bea.staxb.buildtime.internal.tylar.Tylar;
import com.bea.util.jam.JClass;
import com.bea.xbean.xb.xsdschema.SchemaDocument;
import com.bea.xbeanmarshal.buildtime.internal.bts.BindingFile;
import com.bea.xbeanmarshal.buildtime.internal.bts.BindingTypeName;
import com.bea.xbeanmarshal.buildtime.internal.bts.BuiltinBindingLoader;
import com.bea.xbeanmarshal.buildtime.internal.bts.JavaTypeName;
import com.bea.xbeanmarshal.buildtime.internal.bts.WrappedArrayType;
import com.bea.xbeanmarshal.buildtime.internal.bts.XmlBeanDocumentType;
import com.bea.xbeanmarshal.buildtime.internal.bts.XmlBeanType;
import com.bea.xbeanmarshal.buildtime.internal.bts.XmlBeansBuiltinBindingLoader;
import com.bea.xml.SchemaProperty;
import com.bea.xml.SchemaType;
import com.bea.xml.XmlException;
import com.bea.xml.XmlObject;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import weblogic.j2ee.descriptor.JavaWsdlMappingBean;
import weblogic.j2ee.descriptor.ParamValueBean;
import weblogic.j2ee.descriptor.PortComponentBean;
import weblogic.j2ee.descriptor.PortComponentHandlerBean;
import weblogic.j2ee.descriptor.ServiceInterfaceMappingBean;
import weblogic.j2ee.descriptor.ServiceRefBean;
import weblogic.j2ee.descriptor.ServiceRefHandlerBean;
import weblogic.j2ee.descriptor.wl.ServiceReferenceDescriptionBean;
import weblogic.jws.Transactional;
import weblogic.jws.wlw.UseWLW81BindingTypes;
import weblogic.jws.wlw.WLWRollbackOnCheckedException;
import weblogic.kernel.KernelStatus;
import weblogic.wsee.addressing.ClientAddressingHandler;
import weblogic.wsee.addressing.ServerAddressingHandler;
import weblogic.wsee.async.AsyncClientHandler;
import weblogic.wsee.bind.runtime.RuntimeBindings;
import weblogic.wsee.bind.runtime.RuntimeBindingsBuilder;
import weblogic.wsee.bind.runtime.internal.LiteralArrayHelper;
import weblogic.wsee.bind.runtime.internal.RuntimeBindingsImpl;
import weblogic.wsee.codec.soap11.SoapCodec;
import weblogic.wsee.component.ComponentException;
import weblogic.wsee.component.ejb.EjbComponent;
import weblogic.wsee.component.pojo.JavaClassComponent;
import weblogic.wsee.component.pojo.PojoTransactionHandler;
import weblogic.wsee.deploy.ClientDeployInfo;
import weblogic.wsee.deploy.DeployInfo;
import weblogic.wsee.deploy.EJBDeployInfo;
import weblogic.wsee.deploy.ServletDeployInfo;
import weblogic.wsee.handler.DirectInvokeHandler;
import weblogic.wsee.handler.GlobalHandlerChainHelper;
import weblogic.wsee.handler.HandlerException;
import weblogic.wsee.handler.HandlerList;
import weblogic.wsee.handler.HandlerListImpl;
import weblogic.wsee.handler.JaxrpcHandlerChain;
import weblogic.wsee.handler.MessageContextInitHandler;
import weblogic.wsee.handler.VersionRedirectHandler;
import weblogic.wsee.jaxrpc.Provider;
import weblogic.wsee.jws.wlw.WLWRollbackOnCheckedExceptionHandler;
import weblogic.wsee.policy.deployment.WsdlPolicySubject;
import weblogic.wsee.reliability.policy.WsrmPolicyClientRuntimeHandler;
import weblogic.wsee.security.AuthorizationHandler;
import weblogic.wsee.security.policy.WssPolicyContext;
import weblogic.wsee.util.JamUtil;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.util.WLSOAPFactory;
import weblogic.wsee.util.XBeanUtil;
import weblogic.wsee.ws.dispatch.client.MimeHeaderHandler;
import weblogic.wsee.ws.dispatch.server.CodecHandler;
import weblogic.wsee.ws.dispatch.server.ComponentHandler;
import weblogic.wsee.ws.dispatch.server.ConnectionHandler;
import weblogic.wsee.ws.dispatch.server.JaxrpcChainHandler;
import weblogic.wsee.ws.dispatch.server.OneWayHandler;
import weblogic.wsee.ws.dispatch.server.OperationLookupHandler;
import weblogic.wsee.ws.dispatch.server.PreinvokeHandler;
import weblogic.wsee.wsdl.WsdlBindingMessage;
import weblogic.wsee.wsdl.WsdlBindingOperation;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlMessage;
import weblogic.wsee.wsdl.WsdlOperation;
import weblogic.wsee.wsdl.WsdlPart;
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.wsee.wsdl.WsdlService;
import weblogic.wsee.wsdl.soap11.SoapBinding;
import weblogic.wsee.wsdl.soap11.SoapBindingOperation;
import weblogic.wsee.wsdl.soap11.SoapBindingUtil;
import weblogic.wsee.wsdl.soap11.SoapBody;
import weblogic.wsee.wsdl.soap12.Soap12Binding;
import weblogic.wsee.wsdl.soap12.Soap12BindingOperation;
import weblogic.wsee.wsdl.soap12.Soap12Body;
import weblogic.xml.schema.binding.util.ClassUtil;

final class WsBuilder {
   private static final boolean verbose = Verbose.isVerbose(WsBuilder.class);
   private static final String APACHE_XMLOBJECT_CLASSNAME = "org.apache.xmlbeans.XmlObject";
   private static final String BEA_XMLOBJECT_CLASSNAME = "com.bea.xml.XmlObject";

   public WsService buildService(ServletDeployInfo var1) throws WsException {
      boolean var2 = var1.getJwsClass().getAnnotation(UseWLW81BindingTypes.class) != null;
      PortComponentBean var3 = var1.getPortComp();
      QName var4 = var3.getWsdlPort();
      WsdlPort var5 = (WsdlPort)var1.getWsdlDef().getPorts().get(var4);
      if (var5 == null) {
         throw new WsException("There is no wsdl port named \"" + var4 + "\" in deployed wsdl.");
      } else {
         WSBuilderHelper var6 = new WSBuilderHelper(var1.getMappingdd());
         var6.setServletContext(var1.getServletContext());
         WsServiceImpl var7 = var6.buildService(var5, true);
         this.createRuntimeBindingProvider(var7, this.getThreadTylar(), var1.getMappingdd(), var1.getWsdlDef(), var2);
         this.verifySEI(var7, var3);
         this.setInternalServerHandler(var7, var3, var1);
         this.setComponent(var7.getPort(var4.getLocalPart()), var1);
         ((WsEndpointImpl)var7.getPort(var4.getLocalPart()).getEndpoint()).setJwsClass(var1.getJwsClass());
         var7.setWssPolicyContext(var1.getWssPolicyContext());
         createSoapDispatchMap((WsPortImpl)var7.getPort(var4.getLocalPart()), var3);
         WsPort var8 = var7.getPort(var4.getLocalPart());
         var8.setPortComponent(var1.getWlPortComp());
         return var7;
      }
   }

   public WsServiceImpl buildService(EJBDeployInfo var1) throws WsException {
      PortComponentBean var2 = var1.getPortComp();
      QName var3 = var2.getWsdlPort();
      WsdlPort var4 = (WsdlPort)var1.getWsdlDef().getPorts().get(var3);
      if (var4 == null) {
         throw new WsException("There is no wsdl port named \"" + var3 + "\" in deployed wsdl.");
      } else {
         WSBuilderHelper var5 = new WSBuilderHelper(var1.getMappingdd());
         WsServiceImpl var6 = var5.buildService(var4, true);
         this.createRuntimeBindingProvider(var6, (Tylar)null, var1.getMappingdd(), var1.getWsdlDef(), false);
         this.verifySEI(var6, var2);
         this.setInternalServerHandler(var6, var2, var1);
         this.setComponent(var6.getPort(var3.getLocalPart()), var1);
         ((WsEndpointImpl)var6.getPort(var3.getLocalPart()).getEndpoint()).setJwsClass(var1.getJwsClass());
         var6.setWssPolicyContext(var1.getWssPolicyContext());
         createSoapDispatchMap((WsPortImpl)var6.getPort(var3.getLocalPart()), var2);
         WsPort var7 = var6.getPort(var3.getLocalPart());
         var7.setPortComponent(var1.getWlPortComp());
         return var6;
      }
   }

   public WsService buildService(ClientDeployInfo var1) throws WsException {
      boolean var2 = false;

      try {
         JavaWsdlMappingBean var3 = var1.getMappingdd();
         if (var3 != null) {
            ServiceInterfaceMappingBean[] var4 = var3.getServiceInterfaceMappings();
            if (var4 != null && var4.length >= 0) {
               String var5 = var4[0].getServiceInterface();
               JClass var6 = JamUtil.loadJClass(var5, Thread.currentThread().getContextClassLoader());
               var2 = var6.getAnnotation(UseWLW81BindingTypes.class) != null;
            }
         }
      } catch (Exception var11) {
         if (verbose) {
            Verbose.log((Object)("Could not determine if UseWLW81BindingTypes is present, assuming false. " + var11));
         }
      }

      WSBuilderHelper var12 = new WSBuilderHelper(var1.getMappingdd());
      QName var13 = var1.getServiceName();

      assert var13 != null;

      WsdlDefinitions var14 = var1.getWsdlDef();
      WsdlService var15 = (WsdlService)var14.getServices().get(var13);
      if (var15 == null) {
         Verbose.log((Object)var14);
         throw new WsException("Unable to find wsdl service for service name: " + var13);
      } else {
         ServiceReferenceDescriptionBean var7 = var1.getWlServiceRef();
         WsServiceImpl var8 = var12.buildService(var15, var7 != null ? var7.getPortInfos() : null, false);
         if (var1.getRuntimeBindings() != null) {
            var8.setBindingProvider(var1.getRuntimeBindings());
            this.completeTypeInfo(var8);
         } else {
            this.createRuntimeBindingProvider(var8, (Tylar)null, var1.getMappingdd(), var1.getWsdlDef(), var2);
         }

         WsdlPolicySubject var9 = new WsdlPolicySubject(var14);
         WssPolicyContext var10 = new WssPolicyContext(false);
         var10.getPolicyServer().addPolicies(var9.getPolicies());
         var8.setWssPolicyContext(var10);
         this.setInternalClientHandler(var8, var1.getServiceRef());
         return var8;
      }
   }

   private static void createSoapDispatchMap(WsPortImpl var0, PortComponentBean var1) throws WsException {
      HashMap var2 = new HashMap();
      HashMap var3 = new HashMap();
      WsdlPort var4 = var0.getWsdlPort();
      SOAPFactory var5 = WLSOAPFactory.createSOAPFactory();
      Iterator var6 = var4.getPortType().getOperations().values().iterator();

      while(true) {
         WsdlOperation var7;
         WsdlBindingOperation var8;
         Object var9;
         Object var10;
         do {
            do {
               if (!var6.hasNext()) {
                  var3.put("", var2);
                  var3.put((Object)null, var2);
                  var0.setActionDispatchMap(var3);
                  var0.setSoapDispatchMap(var2);
                  return;
               }

               var7 = (WsdlOperation)var6.next();
               var8 = (WsdlBindingOperation)var4.getBinding().getOperations().get(var7.getName());
               var9 = SoapBinding.narrow(var8.getBinding());
               if (var9 == null) {
                  var9 = Soap12Binding.narrow(var8.getBinding());
               }
            } while(var9 == null);

            var10 = SoapBindingOperation.narrow(var8);
            if (var10 == null) {
               var10 = Soap12BindingOperation.narrow(var8);
            }
         } while(var10 == null);

         String var11 = SoapBindingUtil.getStyle((SoapBindingOperation)var10, (SoapBinding)var9);

         try {
            Name var12;
            if ("rpc".equals(var11)) {
               WsdlBindingMessage var13 = var8.getInput();
               Object var14 = SoapBody.narrow(var13);
               if (var14 == null) {
                  var14 = Soap12Body.narrow(var13);
               }

               QName var15 = var7.getName();
               var12 = var5.createName(var15.getLocalPart(), (String)null, ((SoapBody)var14).getNamespace());
            } else {
               WsMethod var19 = var0.getEndpoint().getMethod(var7.getName().getLocalPart());
               QName var20;
               if (var19.isWrapped()) {
                  var20 = var19.getWrapperElement();
                  var12 = var5.createName(var20.getLocalPart(), (String)null, var20.getNamespaceURI());
               } else if (!var19.getParameters().hasNext()) {
                  var12 = SoapCodec.VOID_NAME_KEY;
               } else {
                  var20 = var19.getParameter(0).getXmlName().getQName();
                  Class var21 = var0.getEndpoint().getEndpointInterface();
                  if (var21 == null) {
                     var21 = loadClass(var1.getServiceEndpointInterface());
                  }

                  if (!isGenericService(var21) && var19.getParameterSize() > 1) {
                     for(int var16 = 1; var16 < var19.getParameterSize(); ++var16) {
                        WsParameterType var17 = var19.getParameter(var16);
                        if (!var17.isHeader() && var17.getMode() == 0 && !isAttachmentParam(var17.getJavaType().getName())) {
                           var20 = var17.getXmlName().getQName();
                           break;
                        }
                     }
                  }

                  var12 = var5.createName(var20.getLocalPart(), (String)null, var20.getNamespaceURI());
               }
            }

            buildActionDispatchMap(var7, var3, var12);
            var2.put(var12, var7.getName());
         } catch (SOAPException var18) {
            throw new WsException(var18.getMessage(), var18);
         }
      }
   }

   private static void buildActionDispatchMap(WsdlOperation var0, Map<String, Map<Name, QName>> var1, Name var2) {
      String var3 = var0.getInputAction();
      if (!StringUtil.isEmpty(var3)) {
         Object var4 = (Map)var1.get(var3);
         if (var4 == null) {
            var4 = new HashMap();
            var1.put(var3, var4);
         }

         ((Map)var4).put(var2, var0.getName());
      }

   }

   private static boolean isAttachmentParam(String var0) {
      return "javax.activation.DataHandler".equals(var0) || "[Ljavax.activation.DataHandler;".equals(var0) || "javax.xml.transform.Source".equals(var0) || "[Ljavax.xml.transform.Source;".equals(var0) || "javax.mail.internet.MimeMultipart".equals(var0) || "[Ljavax.mail.internet.MimeMultipart;".equals(var0) || "java.awt.Image".equals(var0) || "[Ljava.awt.Image;".equals(var0);
   }

   private void createRuntimeBindingProvider(WsServiceImpl var1, Tylar var2, JavaWsdlMappingBean var3, WsdlDefinitions var4, boolean var5) throws WsException {
      RuntimeBindingsBuilder var6 = RuntimeBindingsBuilder.Factory.newInstance();
      if (var4.getTypes() != null && var4.getTypes().getSchemaArray() != null) {
         SchemaDocument[] var7 = var4.getTypes().getSchemaArrayWithoutImport();

         for(int var8 = 0; var8 < var7.length; ++var8) {
            var6.addSchema(var7[var8]);
         }

         if (verbose) {
            Verbose.log((Object)("Found schema docs: " + var7.length));
         }
      }

      var6.set109Mappings(var3);
      var6.setWsdl(var4);
      var6.setTreatEnumsAsSimpleTypes(var5);
      if (var2 != null) {
         var6.addTylar(var2);
      }

      this.addTypesToBuilder(var1, var6);

      try {
         RuntimeBindings var11 = var6.createRuntimeBindings();
         var1.setBindingProvider(var11);
      } catch (IOException var9) {
         throw new WsException("Failed to create binding provider " + var9, var9);
      } catch (XmlException var10) {
         throw new WsException("Failed to create binding provider " + var10, var10);
      }

      this.completeTypeInfo(var1);
   }

   private BuiltinBindingLoader getXmlBeansBuiltinBindings() {
      return XmlBeansBuiltinBindingLoader.getInstance();
   }

   private void createXmlBeansBindings(WsServiceImpl var1) throws WsException {
      Set var2 = this.getAllXmlBeansTypes(var1);
      Iterator var3 = var2.iterator();
      if (var3.hasNext()) {
         BindingFile var4 = new BindingFile();

         while(true) {
            while(var3.hasNext()) {
               WsType var5 = (WsType)var3.next();
               if (var5.isXmlObjectForTypeOrDocument()) {
                  this.addXmlObject(var5, var1, var4);
               } else if (var5.isArrayOfXmlObjectForType() || var5.isArrayOfXmlObjectForDocument()) {
                  this.addXmlBeanArray(var5, var4, var1);
               }
            }

            ((RuntimeBindingsImpl)var1.getBindingProvider()).setXmlBeansBindingLoader(var4);
            return;
         }
      }
   }

   private void addXmlBeanArray(WsType var1, BindingFile var2, WsServiceImpl var3) throws WsException {
      XmlTypeName var4 = var1.getXmlName();
      int var5 = 1;

      Class var6;
      for(var6 = var1.getJavaType().getComponentType(); var6.isArray(); ++var5) {
         var6 = var6.getComponentType();
      }

      String var7 = var6.getName();
      if ((var7.equals("org.apache.xmlbeans.XmlObject") || var7.equals("com.bea.xml.XmlObject")) && var4.isElementWildcardArrayType()) {
         if (var5 != 1) {
            throw new IllegalArgumentException("WLS runtime cannot handle arrays of XmlObject mapped to the Element Wildcard <any maxOccurs='unbounded'/> of dimension other than 1.   We've encountered an array of XmlObject of dimension '" + var5 + "' and most abort deployment.");
         } else {
            WrappedArrayType var10 = WrappedArrayType.wrappedArrayTypeFor(var6);
            var2.addBindingType(var10, true, true);
         }
      } else {
         JavaTypeName var8 = JavaTypeName.forClassName(var1.getJavaType().getName());
         SchemaType var9 = var3.getBindingProvider().getSchemaTypeForXmlTypeName(var1.getXmlName());
         if (!LiteralArrayHelper.isLiteralArray(var9)) {
            throw new IllegalArgumentException(" array of XmlBeans type '" + var1.getJavaType().getName() + "' is NOT a literal array (perhaps it's a SOAP Encoded array) " + "non-literal arrays are not implemented.");
         } else {
            this.addLiteralArrayToXBeansBindingFile(var2, var1.getJavaType(), var8, var9);
         }
      }
   }

   private void addXmlObject(WsType var1, WsServiceImpl var2, BindingFile var3) {
      JavaTypeName var4 = JavaTypeName.forClassName(var1.getJavaType().getName());
      XmlTypeName var5 = var1.getXmlName();
      if (var5.isElement()) {
         var5 = XmlTypeName.forNestedAnonymous('t', var5);
      }

      SchemaType var6 = var2.getBindingProvider().getSchemaTypeForXmlTypeName(var5);
      QName var7 = var5.getQName();
      if (var7 != null) {
         com.bea.xbeanmarshal.buildtime.internal.bts.XmlTypeName var8 = com.bea.xbeanmarshal.buildtime.internal.bts.XmlTypeName.forTypeNamed(var7);
         if (var8 != null && this.getXmlBeansBuiltinBindings().lookupPojoFor(var8) != null) {
            return;
         }
      }

      this.addXmlObjectToXBeansBindingFile(var3, var1.getJavaType(), var4, var6);
   }

   private void addLiteralArrayToXBeansBindingFile(BindingFile var1, Class var2, JavaTypeName var3, SchemaType var4) throws WsException {
      if (!var2.isArray()) {
         throw new WsException("ERROR !  While creating XmlBeans Binding Types,  attempted to process as a Literal Array Type, the javaClass '" + var2.getName() + "' which is NOT an array.  " + "The corresponding JavaTypeName is '" + var3 + "'");
      } else {
         com.bea.xbeanmarshal.buildtime.internal.bts.XmlTypeName var5 = com.bea.xbeanmarshal.buildtime.internal.bts.XmlTypeName.forSchemaType(var4);
         BindingTypeName var6 = BindingTypeName.forPair(var3, var5);
         JavaTypeName var7 = var3.getArrayTypeMinus1Dim(var3.getArrayDepth());
         SchemaType var8 = LiteralArrayHelper.getLiteralArrayItemType(var4);
         com.bea.xbeanmarshal.buildtime.internal.bts.XmlTypeName var9 = com.bea.xbeanmarshal.buildtime.internal.bts.XmlTypeName.forSchemaType(var8);
         BindingTypeName var10 = BindingTypeName.forPair(var7, var9);
         WrappedArrayType var11 = new WrappedArrayType(var6);
         SchemaProperty[] var12 = var4.getProperties();
         QName var13 = var12[0].getName();
         var11.setItemName(var13);
         var11.setItemType(var10);
         var11.setItemNillable(true);
         if (verbose) {
            Verbose.log((Object)("\n for BTN '" + var6 + "' created xbean WrappedArrayType '" + var11 + "'  itemQName='" + var13 + "' and itemBtn='" + var10 + "'\n"));
         }

         var1.addBindingType(var11, true, true);
         Class var14 = var2.getComponentType();
         if (var14 == null) {
            throw new WsException("Unexpected Condition while building xmlBeans bindings for arrays.  For java array class '" + var2.getName() + "' we got a NULL array item java class when we didn't expect one.");
         } else {
            if (var7.getArrayDepth() > 0) {
               this.addLiteralArrayToXBeansBindingFile(var1, var14, var7, var8);
            } else if (var7.getArrayDepth() == 0) {
               this.addXmlObjectToXBeansBindingFile(var1, var14, var7, var8);
            }

         }
      }
   }

   private void addXmlObjectToXBeansBindingFile(BindingFile var1, Class var2, JavaTypeName var3, SchemaType var4) {
      if (this.getXmlBeansBuiltinBindings().lookupTypeFor(var3) != null) {
         if (verbose) {
            Verbose.log((Object)(" javaTypeName '" + var3 + "' is an XmlBeans builtin.  Skipping explicit type bind."));
         }

      } else {
         com.bea.xbeanmarshal.buildtime.internal.bts.XmlTypeName var5 = com.bea.xbeanmarshal.buildtime.internal.bts.XmlTypeName.forSchemaType(var4);
         BindingTypeName var6 = BindingTypeName.forPair(var3, var5);
         if (XBeanUtil.xmlBeanIsDocumentType(var2)) {
            if (verbose) {
               Verbose.log((Object)("\n adding XmlBeanDocumentType to xbeanBindings for '" + var6 + "'\n"));
            }

            XmlBeanDocumentType var7 = new XmlBeanDocumentType(var6);
            var1.addBindingType(var7, true, true);
         } else {
            if (!XBeanUtil.isXmlBean(var2)) {
               throw new IllegalArgumentException(" Unexpected type '" + var2.getName() + "' encountered while building XmlBeansBindings for Xml[Document]Object");
            }

            if (verbose) {
               Verbose.log((Object)("\n adding XmlBeanType to xbeanBindings for '" + var6 + "'\n"));
            }

            XmlBeanType var8 = new XmlBeanType(var6);
            var1.addBindingType(var8, true, true);
         }

      }
   }

   private Set getAllXmlBeansTypes(WsService var1) {
      HashSet var2 = new HashSet();
      Iterator var3 = var1.getPorts();

      while(true) {
         WsEndpoint var5;
         do {
            if (!var3.hasNext()) {
               return var2;
            }

            WsPort var4 = (WsPort)var3.next();
            var5 = var4.getEndpoint();
         } while(var5 == null);

         Iterator var6 = var5.getMethods();

         while(var6.hasNext()) {
            WsMethod var7 = (WsMethod)var6.next();
            WsReturnType var8 = var7.getReturnType();
            if (var8 != null && var8.isAnyXmlObject()) {
               var2.add(var8);
            }

            Iterator var9 = var7.getParameters();

            while(var9.hasNext()) {
               WsParameterType var10 = (WsParameterType)var9.next();
               if (var10.isAnyXmlObject()) {
                  var2.add(var10);
               }
            }
         }
      }
   }

   private void verifySEI(WsService var1, PortComponentBean var2) throws WsException {
      WsEndpoint var3 = (WsEndpoint)var1.getEndpoints().next();
      Class var4 = var3.getEndpointInterface();
      if (var4 != null) {
         if (!var4.getName().equals(var2.getServiceEndpointInterface())) {
            throw new WsException("Service endpoint inteface " + var2.getServiceEndpointInterface() + " specified in webservcies.xml is not matched with " + "Jaxrpc mapping file " + var4.getName());
         }
      } else {
         var4 = loadClass(var2.getServiceEndpointInterface());
         if (isGenericService(var4)) {
            return;
         }

         ((WsEndpointImpl)var3).setEndpointInterface(var4);
         Method[] var5 = var4.getMethods();

         for(int var6 = 0; var6 < var5.length; ++var6) {
            WsMethod var7 = var3.getMethod(var5[var6].getName());
            if (var7 == null) {
               throw new WsException("Method " + var5[var6].getName() + " is " + "not found in wsdl");
            }

            this.verifyParamType(var7, var5[var6]);
         }
      }

   }

   private void verifyParamType(WsMethod var1, Method var2) throws WsException {
      Class[] var3 = var2.getParameterTypes();
      int var4 = 0;

      for(Iterator var5 = var1.getParameters(); var5.hasNext(); ++var4) {
         WsParameterType var6 = (WsParameterType)var5.next();
         if (var4 >= var3.length) {
            throw new WsException("For method " + var2.getName() + " wsdl has" + " more parts defined than in service endpoint interface.");
         }

         if (var3[var4] != var6.getJavaType()) {
            throw new WsException("Java type mismatch. For method " + var2.getName() + " parameter" + var6.getName() + " SEI's type: " + var3[var4] + " type" + " defered from wsdl:" + var6.getJavaType());
         }
      }

      if (var4 < var3.length) {
         throw new WsException("For method " + var2.getName() + " wsdl has" + " less parts defined than in service endpoint interface.");
      }
   }

   private void addTypesToBuilder(WsService var1, RuntimeBindingsBuilder var2) {
      Iterator var3 = var1.getEndpoints();

      while(var3.hasNext()) {
         WsEndpoint var4 = (WsEndpoint)var3.next();
         Iterator var5 = var4.getMethods();

         while(var5.hasNext()) {
            WsMethod var6 = (WsMethod)var5.next();
            Iterator var7 = var6.getParameters();

            while(var7.hasNext()) {
               WsParameterType var8 = (WsParameterType)var7.next();
               this.addParamType(var6, var2, var8);
            }

            WsReturnType var9 = var6.getReturnType();
            if (var9 != null && Void.TYPE != var9.getJavaType()) {
               this.addParamType(var6, var2, var9);
            }
         }
      }

   }

   private void addParamType(WsMethod var1, RuntimeBindingsBuilder var2, WsType var3) {
      if (var1.isWrapped() && !var3.isHeader()) {
         var2.addLocalElementToBind(var3.getXmlName().getQName(), var3.getName(), var3.getJavaType());
      } else {
         XmlTypeName var4 = var3.getXmlName();
         if (var4.isElement()) {
            if (!isXmlBean(var3.getJavaType())) {
               var2.addGlobalElementToBind(var4.getQName(), var3.getJavaType());
            }
         } else if (!"http://www.bea.com/servers/wls90/wsee/attachment".equals(var4.getQName().getNamespaceURI()) && !isXmlBean(var3.getJavaType())) {
            var2.addGlobalTypeToBind(var4.getQName(), var3.getJavaType());
         }
      }

   }

   private static boolean isXmlBean(Class var0) {
      if (var0 == null) {
         return false;
      } else if (var0.isArray()) {
         Class var4 = var0.getComponentType();
         return isXmlBean(var4);
      } else if (XmlObject.class.isAssignableFrom(var0)) {
         return true;
      } else {
         try {
            ClassLoader var1 = var0.getClassLoader();
            if (var1 != null) {
               Class var2 = var1.loadClass("org.apache.xmlbeans.XmlObject");
               if (var2 != null && var2.isAssignableFrom(var0)) {
                  return true;
               }
            }
         } catch (ClassNotFoundException var3) {
         }

         return false;
      }
   }

   private void completeTypeInfo(WsServiceImpl var1) throws WsException {
      RuntimeBindings var2 = var1.getBindingProvider();
      Iterator var3 = var1.getPorts();

      while(true) {
         WsEndpoint var5;
         do {
            if (!var3.hasNext()) {
               this.createXmlBeansBindings(var1);
               return;
            }

            WsPort var4 = (WsPort)var3.next();
            var5 = var4.getEndpoint();
         } while(var5 == null);

         Iterator var6 = var5.getMethods();

         while(var6.hasNext()) {
            WsMethod var7 = (WsMethod)var6.next();
            Iterator var8 = var7.getParameters();

            while(var8.hasNext()) {
               WsParameterType var9 = (WsParameterType)var8.next();
               this.fillParamType(var7, var9, var2);
            }

            WsReturnType var16 = var7.getReturnType();
            if (var16 != null && Void.TYPE != var16.getJavaType()) {
               this.fillParamType(var7, var16, var2);
            }

            Iterator var17 = var7.getExceptions();

            while(var17.hasNext()) {
               WsFault var10 = (WsFault)var17.next();
               WsdlMessage var11 = var10.getFaultMessage();
               Iterator var12 = var11.getParts().values().iterator();
               if (var12.hasNext()) {
                  WsdlPart var13 = (WsdlPart)var12.next();
                  if (var13 != null) {
                     QName var14 = var13.getType();
                     if (var14 == null) {
                        QName var15 = var13.getElement();
                        if (var15 != null) {
                           var14 = var2.getTypeForElement(var15);
                        } else if (verbose) {
                           Verbose.log((Object)(" WARNING !  In WsService " + var1 + "\n, WsEndpoint " + var5 + "\n, WsMethod " + var7 + "\n, WsFault " + var10 + "\n, WsdlPart " + var13 + "\n  the WSDL fault part has neither an 'element' nor a 'type' attribute !\n"));
                        }
                     }

                     if (var14 != null) {
                        var10.setMarshalPropertyQName(var14);
                        if (var2.isSimpleType(var14)) {
                           var10.setIsSimpleType(true);
                        } else {
                           var10.setIsSimpleType(false);
                        }

                        var10.computeMarshalProperty();
                        if (verbose) {
                           Verbose.log((Object)("+++faultType '" + var14 + ", marshalProperty setting is " + var10.marshalProperty()));
                        }
                     } else if (verbose) {
                        Verbose.log((Object)(" WARNING !  In WsService " + var1 + "\n, WsEndpoint " + var5 + "\n, WsMethod " + var7 + "\n, WsFault " + var10 + "\n, WsdlPart " + var13 + "\n +++  COULD NOT GET QNAME of TYPE for the either " + "the 'element' or 'type' attribute !\n"));
                     }
                  } else if (verbose) {
                     Verbose.log((Object)(" WARNING !  In WsService " + var1 + "\n, WsEndpoint " + var5 + "\n, WsMethod " + var7 + "\n, WsFault " + var10 + "+++  COULD NOT FIND a PART for FAULT:  " + var10));
                  }
               } else if (verbose) {
                  Verbose.log((Object)(" WARNING !  In WsService " + var1 + "\n, WsEndpoint " + var5 + "\n, WsMethod " + var7 + "\n, WsFault " + var10 + "+++  COULD NOT FIND a PART for FAULT:  " + var10));
               }
            }
         }
      }
   }

   private void fillParamType(WsMethod var1, WsType var2, RuntimeBindings var3) throws WsException {
      if (var1.isWrapped() && !var2.isHeader()) {
         XmlTypeName var4 = var2.getXmlName();
         var2.setOptionalElement(var3.isOptionalLocalElement(var4, var2.getName(), var1, var2));
         if (var3.elementIsSingleWildcard(var4) == 1) {
            var2.setXmlName(XmlTypeName.forElementWildCardType());
            var2.setElementName(XmlTypeName.forElementWildCardElement().getQName());
         } else if (var3.elementIsSingleWildcard(var4) == 2) {
            var2.setXmlName(XmlTypeName.forElementWildCardArrayType());
            var2.setElementName(XmlTypeName.forElementWildCardArrayElement().getQName());
         } else if (StringUtil.isEmpty(var2.getName()) && WildcardUtil.WILDCARD_CLASSNAMES.contains(var2.getJavaType().getCanonicalName())) {
            if (var2.getJavaType().isArray()) {
               var2.setXmlName(XmlTypeName.forElementWildCardArrayType());
               var2.setElementName(XmlTypeName.forElementWildCardArrayElement().getQName());
            } else {
               var2.setXmlName(XmlTypeName.forElementWildCardType());
               var2.setElementName(XmlTypeName.forElementWildCardElement().getQName());
            }
         } else {
            try {
               var3.getLocalElementType(var4, var2.getName());
            } catch (IllegalArgumentException var6) {
            }

            var2.setXmlName(var3.getLocalElementType(var4, var2.getName()));
            var2.setElementName(var3.getLocalElementName(var4, var2.getName()));
         }
      } else if (var2.getJavaType() == null) {
         var2.setJavaType(loadClass(var3.getJavaType(var2.getXmlName())));
      }

   }

   private void setComponent(WsPort var1, ServletDeployInfo var2) throws WsException {
      Class var3 = var2.getJwsClass();
      WsSkel var4 = (WsSkel)var1.getEndpoint();

      try {
         var4.setComponent(new JavaClassComponent(var3, var2.getServletContext()));
         if (!isGenericService(var3)) {
            this.registerOperation(var4);
         }

         this.setDefaultMethod(var3);
      } catch (ComponentException var6) {
         throw new WsException("Failed to create component", var6);
      }
   }

   private void setDefaultMethod(Class var1) {
      if (isGenericService(var1) && verbose) {
         Verbose.log((Object)"This is a generic service with default method 'invoke'");
      }

   }

   private static boolean isGenericService(Class var0) {
      return Provider.class.isAssignableFrom(var0);
   }

   private void setComponent(WsPort var1, EJBDeployInfo var2) throws WsException {
      WsSkel var3 = (WsSkel)var1.getEndpoint();

      try {
         Class var4 = var1.getEndpoint().getEndpointInterface();
         var3.setComponent(new EjbComponent(var2.getBeanFactory(), var4));
         this.registerOperation(var3);
         this.setDefaultMethod(var4);
      } catch (ComponentException var5) {
         throw new WsException("Failed to create component " + var5, var5);
      }
   }

   private void registerOperation(WsSkel var1) throws ComponentException {
      Iterator var2 = var1.getMethods();

      while(var2.hasNext()) {
         WsMethod var3 = (WsMethod)var2.next();
         var1.getComponent().registerOperation(var3.getOperationName().getLocalPart(), var3.getMethodName(), this.toClassArray(var3.getParameters()));
      }

   }

   private Class[] toClassArray(Iterator var1) {
      ArrayList var2 = new ArrayList();

      while(var1.hasNext()) {
         WsParameterType var3 = (WsParameterType)var1.next();
         var2.add(var3.getJavaType());
      }

      return (Class[])((Class[])var2.toArray(new Class[var2.size()]));
   }

   private void setInternalClientHandler(WsService var1, ServiceRefBean var2) throws WsException {
      WsPort var4;
      HandlerListImpl var5;
      for(Iterator var3 = var1.getPorts(); var3.hasNext(); var4.setInternalHandlerList(var5)) {
         var4 = (WsPort)var3.next();
         var5 = new HandlerListImpl();

         try {
            if (var2 != null) {
               if (verbose) {
                  Verbose.log((Object)"Got Service Ref");
               }

               this.checkHandlerPortName(var1, var2.getHandlers());
               JaxrpcHandlerChain var6 = this.createJaxrpcHandlerChain(var2.getHandlers(), var4.getWsdlPort().getName().getLocalPart());
               this.createClientHandlerInfo(var6, var5, var1.isUsingPolicy());
            } else {
               this.createClientHandlerInfo((JaxrpcHandlerChain)null, var5, var1.isUsingPolicy());
            }
         } catch (HandlerException var7) {
            throw new WsException("Failed to create handler chain", var7);
         }
      }

   }

   private void checkHandlerPortName(WsService var1, ServiceRefHandlerBean[] var2) throws WsException {
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            String[] var4 = var2[var3].getPortNames();
            if (var4 != null) {
               for(int var5 = 0; var5 < var4.length; ++var5) {
                  if (var1.getPort(var4[var5]) == null) {
                     throw new WsException("port name \"" + var4[var3] + "\" " + "referenced by handler \"" + var2[var3].getHandlerName() + "\"" + " is not defined in wsdl");
                  }
               }
            }
         }

      }
   }

   private void setInternalServerHandler(WsService var1, PortComponentBean var2, DeployInfo var3) throws WsException {
      JaxrpcHandlerChain var4 = null;

      try {
         var4 = this.createJaxrpcHandlerChain(var2.getHandlers());
      } catch (HandlerException var10) {
         throw new WsException("Failed to create handler chain", var10);
      }

      WsPort var6;
      HandlerListImpl var7;
      for(Iterator var5 = var1.getPorts(); var5.hasNext(); var6.setInternalHandlerList(var7)) {
         var6 = (WsPort)var5.next();
         var7 = null;

         try {
            var7 = new HandlerListImpl();
            this.createServerHandlerInfo(var4, var7, var1.isUsingPolicy(), var3);
         } catch (HandlerException var9) {
            throw new WsException("Failed to create handler chain", var9);
         }
      }

   }

   private JaxrpcHandlerChain createJaxrpcHandlerChain(ServiceRefHandlerBean[] var1, String var2) throws WsException, HandlerException {
      if (var1 != null && var1.length != 0) {
         ArrayList var3 = new ArrayList(var1.length);

         for(int var4 = 0; var4 < var1.length; ++var4) {
            if (this.containName(var1[var4].getPortNames(), var2)) {
               String var5 = var1[var4].getHandlerClass();
               QName[] var6 = var1[var4].getSoapHeaders();
               Map var7 = this.buildInitParams(var1[var4].getInitParams());
               var3.add(new HandlerInfo(loadClass(var5), var7, var6));
            }
         }

         if (var3.size() == 0) {
            return null;
         } else {
            HandlerListImpl var8 = new HandlerListImpl();
            int var9 = 0;

            for(Iterator var10 = var3.iterator(); var10.hasNext(); ++var9) {
               HandlerInfo var12 = (HandlerInfo)var10.next();
               var8.add("USER-HANDLER[" + var9 + "]", var12);
            }

            JaxrpcHandlerChain var11 = new JaxrpcHandlerChain(var8);
            var11.setServerSide(false);
            return var11;
         }
      } else {
         if (verbose) {
            Verbose.log((Object)"No handler found");
         }

         return null;
      }
   }

   private boolean containName(String[] var1, String var2) {
      if (var1 != null && var1.length != 0) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            if (var2.equals(var1[var3])) {
               return true;
            }
         }

         return false;
      } else {
         return true;
      }
   }

   private JaxrpcHandlerChain createJaxrpcHandlerChain(PortComponentHandlerBean[] var1) throws WsException, HandlerException {
      HandlerListImpl var2 = new HandlerListImpl();
      int var3 = 0;
      List var4 = GlobalHandlerChainHelper.getInstance().getInboundHandlerInfos();

      for(Iterator var5 = var4.iterator(); var5.hasNext(); ++var3) {
         HandlerInfo var6 = (HandlerInfo)var5.next();
         var2.add("GLOBAL-INBOUND-HANDLER[" + var3 + "]", var6);
      }

      if (var1 != null && var1.length > 0) {
         ArrayList var10 = new ArrayList(var1.length);

         for(int var12 = 0; var12 < var1.length; ++var12) {
            String var7 = var1[var12].getHandlerClass();
            QName[] var8 = var1[var12].getSoapHeaders();
            Map var9 = this.buildInitParams(var1[var12].getInitParams());
            var10.add(new HandlerInfo(loadClass(var7), var9, var8));
         }

         var3 = 0;

         for(Iterator var13 = var10.iterator(); var13.hasNext(); ++var3) {
            HandlerInfo var14 = (HandlerInfo)var13.next();
            var2.add("USER-HANDLER[" + var3 + "]", var14);
         }
      }

      if (var2.size() == 0) {
         return null;
      } else {
         JaxrpcHandlerChain var11 = new JaxrpcHandlerChain(var2);
         var11.setServerSide(false);
         return var11;
      }
   }

   private Map buildInitParams(ParamValueBean[] var1) {
      HashMap var2 = new HashMap();
      if (var1 == null) {
         return var2;
      } else {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            String var4 = var1[var3].getParamName();
            String var5 = var1[var3].getParamValue();
            var2.put(var4, var5);
         }

         return var2;
      }
   }

   private static boolean isTransactional(Class var0) {
      boolean var1 = false;
      if (var0 == null) {
         return false;
      } else {
         if (var0.getAnnotation(Transactional.class) != null) {
            Transactional var2 = (Transactional)var0.getAnnotation(Transactional.class);
            if (var2.value()) {
               return true;
            }
         }

         Method[] var4 = var0.getMethods();

         for(int var3 = 0; var3 < var4.length; ++var3) {
            if (var4[var3].getAnnotation(Transactional.class) != null) {
               var1 = true;
            }
         }

         return var1;
      }
   }

   private void createServerHandlerInfo(JaxrpcHandlerChain var1, HandlerList var2, boolean var3, DeployInfo var4) throws HandlerException {
      boolean var5 = false;
      boolean var6 = false;
      if (var4.getJwsClass().getAnnotation(WLWRollbackOnCheckedException.class) != null) {
         var5 = true;
      }

      if (var4 instanceof ServletDeployInfo && isTransactional(var4.getJwsClass())) {
         var6 = true;
      }

      var2.add("MESSAGE_CONTEXT_INIT_HANDLER", new HandlerInfo(MessageContextInitHandler.class, this.getMessageContextPropsMap(var4), (QName[])null));
      var2.add("CONNECTION_HANDLER", new HandlerInfo(ConnectionHandler.class, (Map)null, (QName[])null));
      var2.add("WS_VERSION_REDIRECT_HANDLER", new HandlerInfo(VersionRedirectHandler.class, (Map)null, (QName[])null));
      var2.add("DIRECT_INVOKE_HANDLER", new HandlerInfo(DirectInvokeHandler.class, (Map)null, (QName[])null));
      var2.add("ADDRESSING_HANDLER", new HandlerInfo(ServerAddressingHandler.class, (Map)null, (QName[])null));
      var2.add("OPERATION_LOOKUP_HANDLER", new HandlerInfo(OperationLookupHandler.class, (Map)null, (QName[])null));
      var2.add("ONE_WAY_HANDLER", new HandlerInfo(OneWayHandler.class, (Map)null, (QName[])null));
      var2.add("PRE_INVOKE_HANDLER", new HandlerInfo(PreinvokeHandler.class, (Map)null, (QName[])null));
      if (var1 != null) {
         var2.add("JAX_RPC_CHAIN_HANDLER", new HandlerInfo(JaxrpcChainHandler.class, createJAXRPCAttributes(var1, "32"), (QName[])null));
      }

      var2.add("CODEC_HANDLER", new HandlerInfo(CodecHandler.class, (Map)null, (QName[])null));
      var2.add("AUTHORIZATION_HANDLER", new HandlerInfo(AuthorizationHandler.class, (Map)null, (QName[])null));
      if (var5) {
         var2.add("WLW_ROLLBACK_ON_CHECKED_EXCEPTION_HANDLER", new HandlerInfo(WLWRollbackOnCheckedExceptionHandler.class, (Map)null, (QName[])null));
      }

      if (var6) {
         var2.add("POJO_TRANSACTION_HANDLER", new HandlerInfo(PojoTransactionHandler.class, (Map)null, (QName[])null));
      }

      var2.add("COMPONENT_HANDLER", new HandlerInfo(ComponentHandler.class, (Map)null, (QName[])null));
   }

   private static Map createJAXRPCAttributes(JaxrpcHandlerChain var0, String var1) {
      HashMap var2 = new HashMap();
      var2.put("weblogic.wsee.handler.jaxrpcHandlerChain", var0);
      var2.put("weblogic.wsee.handler.jaxrpcHandlerChainPoolSize", var1);
      return var2;
   }

   private void createClientHandlerInfo(JaxrpcHandlerChain var1, HandlerList var2, boolean var3) throws HandlerException {
      var2.add("CODEC_HANDLER", new HandlerInfo(weblogic.wsee.ws.dispatch.client.CodecHandler.class, (Map)null, (QName[])null));
      var2.add("ADDRESSING_HANDLER", new HandlerInfo(ClientAddressingHandler.class, (Map)null, (QName[])null));
      if (var3) {
         var2.add("POLICY_CLIENT_RT_HANDLER", new HandlerInfo(WsrmPolicyClientRuntimeHandler.class, (Map)null, (QName[])null));
      }

      var2.add("JAX_RPC_CHAIN_HANDLER", new HandlerInfo(JaxrpcChainHandler.class, createJAXRPCAttributes(var1, "5"), (QName[])null));
      var2.add("MMEHEADER_HANDLER", new HandlerInfo(MimeHeaderHandler.class, (Map)null, (QName[])null));
      var2.add("ASYNC_HANDLER", new HandlerInfo(AsyncClientHandler.class, (Map)null, (QName[])null));
      if (KernelStatus.isServer()) {
         List var4 = GlobalHandlerChainHelper.getInstance().getOutboundHandlerInfos();
         int var5 = 0;

         for(Iterator var6 = var4.iterator(); var6.hasNext(); ++var5) {
            HandlerInfo var7 = (HandlerInfo)var6.next();
            var2.add("GLOBAL-OUTBOUND-HANDLER[" + var5 + "]", var7);
         }
      }

      var2.add("CONNECTION_HANDLER", new HandlerInfo(weblogic.wsee.ws.dispatch.client.ConnectionHandler.class, (Map)null, (QName[])null));
   }

   private Tylar getThreadTylar() {
      return null;
   }

   private static Class loadClass(String var0) throws WsException {
      try {
         return ClassUtil.loadClass(var0);
      } catch (ClassUtil.ClassUtilException var2) {
         throw new WsException("Failed to load class", var2);
      }
   }

   private HashMap getMessageContextPropsMap(DeployInfo var1) {
      HashMap var2 = new HashMap();
      var2.put("weblogic.wsee.context_path", var1.getContextPath());
      var2.put("weblogic.wsee.security_realm", var1.getSecurityRealmName());
      var2.put("weblogic.wsee.application_id", var1.getApplication());
      var2.put("weblogic.wsee.service_name", var1.getServiceName());
      boolean var3 = false;
      boolean var4 = Boolean.getBoolean("weblogic.wsee.validate_request");
      if (var1.getWlPortComp() != null) {
         var3 = Boolean.valueOf(var1.getWlPortComp().getStreamAttachments());
         var4 = var4 || Boolean.valueOf(var1.getWlPortComp().isValidateRequest());
      }

      var2.put("weblogic.wsee.stream_attachments", var3);
      if (verbose) {
         Verbose.log((Object)("Request Validation of Webservice PortComponentName= (" + var1.getPortComp().getPortComponentName() + ") is set to " + var4));
      }

      var2.put("weblogic.wsee.validate_request", var4);
      return var2;
   }

   public String toString() {
      return "WsBuilder{}";
   }
}
