package weblogic.wsee.tools.jws.mapping;

import com.bea.staxb.buildtime.WildcardUtil;
import com.bea.staxb.buildtime.internal.bts.XmlTypeName;
import com.bea.util.jam.JAnnotation;
import com.bea.util.jam.JClass;
import com.bea.xml.SchemaGlobalElement;
import com.bea.xml.SchemaParticle;
import com.bea.xml.SchemaType;
import com.bea.xml.SchemaTypeLoader;
import com.bea.xml.XmlBeans;
import com.bea.xml.XmlException;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.jws.WebParam.Mode;
import javax.xml.namespace.QName;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.j2ee.descriptor.ConstructorParameterOrderBean;
import weblogic.j2ee.descriptor.ExceptionMappingBean;
import weblogic.j2ee.descriptor.JavaWsdlMappingBean;
import weblogic.j2ee.descriptor.MethodParamPartsMappingBean;
import weblogic.j2ee.descriptor.PackageMappingBean;
import weblogic.j2ee.descriptor.PortMappingBean;
import weblogic.j2ee.descriptor.ServiceEndpointInterfaceMappingBean;
import weblogic.j2ee.descriptor.ServiceEndpointMethodMappingBean;
import weblogic.j2ee.descriptor.ServiceInterfaceMappingBean;
import weblogic.j2ee.descriptor.WsdlMessageMappingBean;
import weblogic.j2ee.descriptor.WsdlReturnValueMappingBean;
import weblogic.jws.WildcardBindings;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.jws.decl.WebMethodDecl;
import weblogic.wsee.tools.jws.decl.WebParamDecl;
import weblogic.wsee.tools.jws.decl.WebServiceDecl;
import weblogic.wsee.tools.jws.decl.WebTypeDecl;
import weblogic.wsee.tools.jws.decl.port.PortDecl;
import weblogic.wsee.tools.jws.jaxrpc.JAXRPCProcessor;
import weblogic.wsee.tools.jws.jaxrpc.JAXRPCWebServiceInfo;
import weblogic.wsee.util.ExceptionInfo;
import weblogic.wsee.util.ExceptionUtil;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlMessage;
import weblogic.wsee.wsdl.WsdlOperation;
import weblogic.wsee.wsdl.WsdlParameter;
import weblogic.wsee.wsdl.WsdlPart;
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.wsee.wsdl.WsdlPortType;
import weblogic.wsee.wsdl.WsdlService;
import weblogic.wsee.wsdl.WsdlTypes;
import weblogic.wsee.wsdl.WsdlUtils;

public class JaxrpcMappingBuilder extends JAXRPCProcessor {
   private static final boolean VERBOSE = Verbose.isVerbose(JaxrpcMappingBuilder.class);
   private static final boolean IGNORE_WEBRESULT = Boolean.getBoolean("weblogic.wsee.jaxrpc.ignore.webresult");
   private static final int NOT_BOUND_TO_WILDCARD = 0;
   private static final int BOUND_TO_ANY = 1;
   private static final int BOUND_TO_ANYTYPE = 2;

   protected void processImpl(JAXRPCWebServiceInfo var1) throws WsBuildException {
      if (!this.moduleInfo.isWsdlOnly()) {
         EditableDescriptorManager var2 = new EditableDescriptorManager();
         JavaWsdlMappingBean var3 = (JavaWsdlMappingBean)var2.createDescriptorRoot(JavaWsdlMappingBean.class).getRootBean();
         var1.createBindings().generate109Descriptor(var3);
         this.addPackageMapping(var3, var1.getWebService());
         this.addServiceInterfaceMapping(var3, var1.getWebService(), var1.getDefinitions());
         this.addServiceEndpointInterfaceMapping(var3, var1);
         var1.setJavaWsdlMappingBean(var3);
      }
   }

   private void addJWSExceptionMapping(JavaWsdlMappingBean var1, JAXRPCWebServiceInfo var2, WebMethodDecl var3) throws WsBuildException {
      JClass[] var4 = var3.getJMethod().getExceptionTypes();
      JClass[] var5 = var4;
      int var6 = var4.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         JClass var8 = var5[var7];
         if (ExceptionUtil.isUserException(var8)) {
            this.createExceptionMapping(var1, var8, new QName(var3.getWebService().getTargetNamespace(), var8.getSimpleName()), var2);
         }
      }

   }

   private void addWSDLExceptionMapping(JavaWsdlMappingBean var1, JAXRPCWebServiceInfo var2, WebMethodDecl var3, WsdlOperation var4) throws WsBuildException {
      Map var5 = var4.getFaults();
      Iterator var6 = var5.values().iterator();

      while(var6.hasNext()) {
         WsdlMessage var7 = (WsdlMessage)var6.next();
         ExceptionInfo var8 = ExceptionUtil.getExceptionInfo(var2.createBindings(), var7);
         if (VERBOSE) {
            Verbose.log((Object)var8.toString());
         }

         String var9 = var8.getJsr109ExceptionClassName();
         JClass var10 = var3.getWebService().getJClass().getClassLoader().loadClass(var9);
         this.createExceptionMapping(var1, var10, var7.getName(), var2);
      }

   }

   private void createExceptionMapping(JavaWsdlMappingBean var1, JClass var2, QName var3, JAXRPCWebServiceInfo var4) throws WsBuildException {
      ExceptionMappingBean var5 = var1.createExceptionMapping();
      var5.setExceptionType(var2.getQualifiedName());
      var5.setWsdlMessage(var3);
      List var6 = var4.createBindings().getElementNamesCtorOrderFromException(var2);
      if (VERBOSE) {
         Verbose.log((Object)(" for exception " + var2.getQualifiedName() + ", got back ordered list of " + var6.size() + " elements."));
      }

      if (var6.size() > 1) {
         ConstructorParameterOrderBean var7 = var5.createConstructorParameterOrder();

         QName var9;
         for(Iterator var8 = var6.iterator(); var8.hasNext(); var7.addElementName(var9.getLocalPart())) {
            var9 = (QName)var8.next();
            if (VERBOSE) {
               Verbose.log((Object)(" element name " + var9.getLocalPart() + " add to ConstructorParameterOrderBean"));
            }
         }
      }

   }

   private void addServiceInterfaceMapping(JavaWsdlMappingBean var1, WebServiceDecl var2, WsdlDefinitions var3) throws WsBuildException {
      ServiceInterfaceMappingBean var4 = var1.createServiceInterfaceMapping();
      var4.setServiceInterface("javax.xml.rpc.Service");
      WsdlService var5 = (WsdlService)var3.getServices().get(var2.getServiceQName());
      if (var5 == null) {
         throw new WsBuildException("service: " + var2.getServiceQName() + " was not found in wsdl.");
      } else {
         var4.setWsdlServiceName(var5.getName());
         Iterator var6 = var5.getPorts().values().iterator();

         while(var6.hasNext()) {
            WsdlPort var7 = (WsdlPort)var6.next();
            PortMappingBean var8 = var4.createPortMapping();
            var8.setPortName(var7.getName().getLocalPart());
            var8.setJavaPortName("get" + var7.getName().getLocalPart());
         }

      }
   }

   private void addServiceEndpointInterfaceMapping(JavaWsdlMappingBean var1, JAXRPCWebServiceInfo var2) throws WsBuildException {
      Iterator var3 = var2.getWebService().getDDPorts();

      assert var3.hasNext() : "Web service has no ports";

      while(var3.hasNext()) {
         PortDecl var4 = (PortDecl)var3.next();
         this.addServiceEndpointInterfaceMapping(var1, var2, var4);
      }

   }

   private void addServiceEndpointInterfaceMapping(JavaWsdlMappingBean var1, JAXRPCWebServiceInfo var2, PortDecl var3) throws WsBuildException {
      ServiceEndpointInterfaceMappingBean var4 = var1.createServiceEndpointInterfaceMapping();
      var4.setServiceEndpointInterface(var2.getWebService().getEndpointInterfaceName());
      WsdlPortType var5 = (WsdlPortType)var2.getDefinitions().getPortTypes().get(var2.getWebService().getPortTypeQName());
      var4.setWsdlPortType(var5.getName());
      WsdlPort var6 = (WsdlPort)var2.getDefinitions().getPorts().get(new QName(var2.getWebService().getTargetNamespace(), var3.getPortName()));
      if (var6 == null) {
         throw new WsBuildException("port: " + var3.getPortName() + " was not found in wsdl.");
      } else {
         var4.setWsdlBinding(var6.getBinding().getName());
         Iterator var7 = var2.getWebService().getWebMethods();

         while(var7.hasNext()) {
            WebMethodDecl var8 = (WebMethodDecl)var7.next();
            WsdlOperation var9 = (WsdlOperation)var5.getOperations().get(new QName(var5.getName().getNamespaceURI(), var8.getName()));
            this.addMethodMapping(var4, var8, var9, var2.getDefinitions().getTypes());
            if (var2.getWebService().getCowReader() != null) {
               this.addWSDLExceptionMapping(var1, var2, var8, var9);
            } else {
               this.addJWSExceptionMapping(var1, var2, var8);
            }
         }

      }
   }

   private void addMethodMapping(ServiceEndpointInterfaceMappingBean var1, WebMethodDecl var2, WsdlOperation var3, WsdlTypes var4) {
      ServiceEndpointMethodMappingBean var5 = var1.createServiceEndpointMethodMapping();
      var5.setJavaMethodName(var2.getMethodName());
      var5.setWsdlOperation(var3.getName().getLocalPart());
      if (var2.getSoapBinding().isDocLiteralWrapped()) {
         var5.createWrappedElement();
      }

      if (var2.getSoapBinding().isDocumentStyle()) {
         this.addDocumentParams(var2, var3, var5, var4);
      } else {
         this.addRPCParams(var2, var3, var5, var4);
      }

   }

   private void addDocumentParams(WebMethodDecl var1, WsdlOperation var2, ServiceEndpointMethodMappingBean var3, WsdlTypes var4) {
      if (var1.getWebResult().hasReturn()) {
         String var5 = null;
         if (var1.getSoapBinding().isDocLiteralWrapped()) {
            String var6 = var1.getWebResult().getType();
            if (!WildcardUtil.WILDCARD_CLASSNAMES.contains(var6)) {
               var5 = var1.getWebResult().getName();
               if (!IGNORE_WEBRESULT && var1.getWebResult().isAnnotationSet()) {
                  var5 = var1.getWebResult().getWebTypeDeclName();
               }
            } else {
               if (!this.useWildcardBindingsAnnotation(var1.getWebResult())) {
                  WsdlPart var7 = var2.getWsdlMethod().getResultPart();
                  if (var7 != null) {
                     XmlTypeName var8 = this.getPartXmlTypeName(var7);
                     SchemaTypeLoader var9 = null;

                     try {
                        var9 = XmlBeans.loadXsd(var4.getSchemaArray());
                     } catch (XmlException var11) {
                     }

                     if (var9 != null) {
                        if (this.isBoundToAnyType(var9, var8, 0)) {
                           var5 = var1.getWebResult().getName();
                           if (!IGNORE_WEBRESULT && var1.getWebResult().isAnnotationSet()) {
                              var5 = var1.getWebResult().getWebTypeDeclName();
                           }
                        } else {
                           var5 = "";
                        }
                     }
                  }
               }

               if (var5 == null) {
                  if (var1.getWebResult().isBoundToAnyType()) {
                     var5 = var1.getWebResult().getName();
                     if (!IGNORE_WEBRESULT && var1.getWebResult().isAnnotationSet()) {
                        var5 = var1.getWebResult().getWebTypeDeclName();
                     }
                  } else {
                     var5 = "";
                  }
               }
            }
         } else {
            WsdlPart var12 = (WsdlPart)var2.getOutput().getParts().values().iterator().next();
            var5 = var12.getName();
         }

         this.addReturnTypeMapping(var3, var1, var2, var5);
      }

      this.addParams(var1, var2, var3, var4);
   }

   private void addRPCParams(WebMethodDecl var1, WsdlOperation var2, ServiceEndpointMethodMappingBean var3, WsdlTypes var4) {
      if (var1.getWebResult().hasReturn()) {
         WsdlPart var5 = (WsdlPart)var2.getOutput().getParts().get(WsdlUtils.findReturnPart(var2));
         this.addReturnTypeMapping(var3, var1, var2, var5.getName());
      }

      this.addParams(var1, var2, var3, var4);
   }

   private void addParams(WebMethodDecl var1, WsdlOperation var2, ServiceEndpointMethodMappingBean var3, WsdlTypes var4) {
      int var5 = 0;
      int var6 = -1;
      SchemaTypeLoader var7 = null;

      for(Iterator var8 = var1.getWebParams(); var8.hasNext(); ++var5) {
         ++var6;
         WebParamDecl var9 = (WebParamDecl)var8.next();
         WsdlMessage var10;
         if (var9.getMode() == Mode.OUT) {
            var10 = var2.getOutput();
         } else {
            var10 = var2.getInput();
         }

         String var11 = null;
         String var12 = var9.getType();
         if (!WildcardUtil.WILDCARD_CLASSNAMES.contains(var12)) {
            var11 = var9.getPartName();
         } else {
            if (!this.useWildcardBindingsAnnotation(var9)) {
               if (var1.getSoapBinding().isDocLiteralWrapped()) {
                  if (var9.isHeader()) {
                     --var6;
                     if (VERBOSE) {
                        Verbose.log((Object)("  for doclitwrapped anyType set partName = '" + var9.getPartName() + "'"));
                     }

                     var11 = var9.getPartName();
                  } else {
                     WsdlPart var13 = null;
                     Iterator var14 = var2.getWsdlMethod().getParameters().iterator();

                     while(var14.hasNext()) {
                        WsdlParameter var15 = (WsdlParameter)var14.next();
                        boolean var16 = true;
                        Iterator var17 = var1.getHeaderParams();

                        while(var17.hasNext()) {
                           WebParamDecl var18 = (WebParamDecl)var17.next();
                           if (var15.getPrimaryPart().getName().equals(var18.getPartName())) {
                              var16 = false;
                              break;
                           }
                        }

                        if (var16) {
                           var13 = var15.getPrimaryPart();
                           break;
                        }
                     }

                     if (var13 != null) {
                        XmlTypeName var21 = this.getPartXmlTypeName(var13);
                        if (var7 == null) {
                           try {
                              var7 = XmlBeans.loadXsd(var4.getSchemaArray());
                           } catch (XmlException var19) {
                           }
                        }

                        if (var7 != null) {
                           if (this.isBoundToAnyType(var7, var21, var6)) {
                              if (VERBOSE) {
                                 Verbose.log((Object)("  for doclitwrapped anyType set partName = '" + var9.getPartName() + "'"));
                              }

                              var11 = var9.getPartName();
                           } else {
                              if (VERBOSE) {
                                 Verbose.log((Object)"  for doclitwrapped <any/>set  partName to empty");
                              }

                              var11 = "";
                           }
                        }
                     }
                  }
               } else {
                  if (VERBOSE) {
                     Verbose.log((Object)("  for doc-lit-bare or rpc anyType set partName = '" + var9.getName() + "'"));
                  }

                  var11 = var9.getPartName();
               }
            }

            if (var11 == null) {
               if (var9.isBoundToAnyType()) {
                  if (VERBOSE) {
                     Verbose.log((Object)("  for doclitwrapped anyType set partName = '" + var9.getName() + "'"));
                  }

                  var11 = var9.getPartName();
               } else {
                  if (VERBOSE) {
                     Verbose.log((Object)"  for doclitwrapped <any/>set  partName to empty");
                  }

                  var11 = "";
               }
            }
         }

         MethodParamPartsMappingBean var20 = var3.createMethodParamPartsMapping();
         var20.setParamPosition(var5);
         var20.setParamType(var9.getRealType().getQualifiedName());
         WsdlMessageMappingBean var22 = var20.createWsdlMessageMapping();
         var22.setParameterMode(var9.getMode().toString());
         if (var9.isHeader()) {
            var22.createSoapHeader();
         }

         var22.setWsdlMessage(var10.getName());
         var22.setWsdlMessagePartName(var11);
      }

   }

   private void addReturnTypeMapping(ServiceEndpointMethodMappingBean var1, WebMethodDecl var2, WsdlOperation var3, String var4) {
      WsdlReturnValueMappingBean var5 = var1.createWsdlReturnValueMapping();
      var5.setMethodReturnValue(var2.getWebResult().getType());
      WsdlMessage var6 = var3.getOutput();
      var5.setWsdlMessage(var6.getName());
      var5.setWsdlMessagePartName(var4);
   }

   private void addPackageMapping(JavaWsdlMappingBean var1, WebServiceDecl var2) {
      PackageMappingBean var3 = var1.createPackageMapping();
      var3.setPackageType(var2.getPackageName());
      var3.setNamespaceURI(var2.getTargetNamespace());
   }

   private XmlTypeName getPartXmlTypeName(WsdlPart var1) {
      XmlTypeName var2;
      if (var1.getType() != null) {
         var2 = XmlTypeName.forGlobalName('t', var1.getType());
      } else {
         var2 = XmlTypeName.forGlobalName('e', var1.getElement());
      }

      return var2;
   }

   private boolean useWildcardBindingsAnnotation(WebTypeDecl var1) {
      JClass var2 = var1.getWebMethodDecl().getWebService().getJClass();
      if (var2 != null) {
         JAnnotation var3 = var2.getAnnotation(WildcardBindings.class);
         return var3 != null;
      } else {
         return false;
      }
   }

   private boolean isBoundToAnyType(SchemaTypeLoader var1, XmlTypeName var2, int var3) {
      SchemaType var4 = null;
      if (var2.isElement()) {
         SchemaGlobalElement var5 = var1.findElement(var2.getQName());
         if (var5 == null) {
            throw new IllegalArgumentException("GlobalElement " + var2 + " doesn't exist in the schema.");
         }

         var4 = var5.getType();
      } else if (var2.isType()) {
         var4 = var1.findType(var2.getQName());
         if (var4 == null) {
            throw new IllegalArgumentException("GlobalType " + var2 + " doesn't exist int the schema.");
         }
      }

      SchemaParticle var7 = var4.getContentModel();
      if (var7 != null) {
         SchemaParticle[] var6 = var7.getParticleChildren();
         if (var7.getParticleType() == 4 || var7.getParticleType() == 5) {
            var6 = new SchemaParticle[]{var7};
         }

         if (var6 != null && var6.length > var3) {
            return this.getWildcardBoundType(var6[var3]) == 2;
         }
      }

      return false;
   }

   private int getWildcardBoundType(SchemaParticle var1) {
      switch (var1.getParticleType()) {
         case 4:
            if (var1.getType().getBuiltinTypeCode() == 1) {
               return 2;
            } else {
               SchemaType var2 = var1.getType();
               SchemaParticle var3 = var2.getContentModel();
               if (var3 != null && var3.getParticleType() == 4 && var3.getType().getBuiltinTypeCode() == 1 && !BigInteger.ZERO.equals(var3.getMaxOccurs()) && !BigInteger.ONE.equals(var3.getMaxOccurs())) {
                  return 2;
               }
            }
         default:
            return 0;
         case 5:
            return 1;
      }
   }
}
