package weblogic.wsee.ws;

import com.bea.staxb.buildtime.internal.bts.XmlTypeName;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.jws.WebParam.Mode;
import javax.servlet.ServletContext;
import javax.xml.namespace.QName;
import javax.xml.rpc.holders.ByteArrayHolder;
import weblogic.j2ee.descriptor.ConstructorParameterOrderBean;
import weblogic.j2ee.descriptor.ExceptionMappingBean;
import weblogic.j2ee.descriptor.JavaWsdlMappingBean;
import weblogic.j2ee.descriptor.JavaXmlTypeMappingBean;
import weblogic.j2ee.descriptor.MethodParamPartsMappingBean;
import weblogic.j2ee.descriptor.PackageMappingBean;
import weblogic.j2ee.descriptor.ServiceEndpointInterfaceMappingBean;
import weblogic.j2ee.descriptor.ServiceEndpointMethodMappingBean;
import weblogic.j2ee.descriptor.ServiceInterfaceMappingBean;
import weblogic.j2ee.descriptor.WsdlMessageMappingBean;
import weblogic.j2ee.descriptor.WsdlReturnValueMappingBean;
import weblogic.j2ee.descriptor.wl.PortInfoBean;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.wsee.policy.deployment.UsingPolicy;
import weblogic.wsee.util.ExceptionUtil;
import weblogic.wsee.util.HolderUtil;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.wsdl.WsdlBinding;
import weblogic.wsee.wsdl.WsdlBindingOperation;
import weblogic.wsee.wsdl.WsdlMessage;
import weblogic.wsee.wsdl.WsdlMethod;
import weblogic.wsee.wsdl.WsdlOperation;
import weblogic.wsee.wsdl.WsdlParameter;
import weblogic.wsee.wsdl.WsdlPart;
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.wsee.wsdl.WsdlPortType;
import weblogic.wsee.wsdl.WsdlService;
import weblogic.wsee.wsdl.soap11.SoapBinding;
import weblogic.wsee.wsdl.soap11.SoapBindingOperation;
import weblogic.wsee.wsdl.soap11.SoapBindingUtil;
import weblogic.wsee.wsdl.soap12.Soap12Binding;
import weblogic.wsee.wsdl.soap12.Soap12BindingOperation;
import weblogic.xml.schema.binding.internal.NameUtil;
import weblogic.xml.schema.binding.util.ClassUtil;

public class WSBuilderHelper {
   private static final String XSNS = "http://www.w3.org/2001/XMLSchema";
   private JavaWsdlMappingBean mappingdd;
   private WsServiceImpl wsService;
   private WsPort wsPort;
   private WsEndpoint endpoint;
   private WebAppServletContext wasc;
   private static boolean verbose = Verbose.isVerbose(WSBuilderHelper.class);

   WSBuilderHelper(JavaWsdlMappingBean var1) {
      this.mappingdd = var1;
   }

   public WSBuilderHelper() {
   }

   void setServletContext(ServletContext var1) {
      if (var1 instanceof WebAppServletContext) {
         this.wasc = (WebAppServletContext)var1;
      }

   }

   WebAppServletContext getServletContext() {
      return this.wasc;
   }

   public WsServiceImpl buildService(WsdlPort var1, boolean var2) throws WsException {
      this.wsService = new WsServiceImpl(var1.getService());
      if (UsingPolicy.narrow(var1.getService().getDefinitions()) != null) {
         this.wsService.setUsingPolicy(true);
      }

      this.buildWsPort(var1, var2);
      return this.wsService;
   }

   WsServiceImpl buildService(WsdlService var1, PortInfoBean[] var2, boolean var3) throws WsException {
      if (var2 != null && var2.length != 0) {
         if (verbose) {
            Verbose.log((Object)("Building web service:" + var1.getName()));
         }

         this.wsService = new WsServiceImpl(var1);
         if (UsingPolicy.narrow(var1.getDefinitions()) != null) {
            this.wsService.setUsingPolicy(true);
         }

         int var4 = 0;
         int var5 = 0;
         Iterator var6 = var1.getPorts().values().iterator();

         while(var6.hasNext()) {
            WsdlPort var7 = (WsdlPort)var6.next();
            ++var4;
            if (this.hasPort(var2, var7)) {
               ++var5;
               this.buildWsPort(var7, var3);
            }
         }

         if (var4 > 0 && var5 == 0) {
            throw new IllegalStateException("No port-name given in service-reference-description matched any port defined in service " + this.wsService.getWsdlService().getName() + ". You should check that the configuration of service-reference-description in weblogic-ejb-jar.xml is correct");
         } else {
            return this.wsService;
         }
      } else {
         return this.buildService(var1, var3);
      }
   }

   private boolean hasPort(PortInfoBean[] var1, WsdlPort var2) {
      for(int var3 = 0; var3 < var1.length; ++var3) {
         PortInfoBean var4 = var1[var3];
         if (var4.getPortName().equals(var2.getName().getLocalPart())) {
            return true;
         }
      }

      return false;
   }

   public WsServiceImpl buildService(WsdlService var1, boolean var2) throws WsException {
      if (verbose) {
         Verbose.log((Object)("Building web service:" + var1.getName()));
      }

      this.wsService = new WsServiceImpl(var1);
      if (UsingPolicy.narrow(var1.getDefinitions()) != null) {
         this.wsService.setUsingPolicy(true);
      }

      Iterator var3 = var1.getPorts().values().iterator();

      while(var3.hasNext()) {
         WsdlPort var4 = (WsdlPort)var3.next();
         this.buildWsPort(var4, var2);
      }

      return this.wsService;
   }

   private void buildWsPort(WsdlPort var1, boolean var2) throws WsException {
      if (SoapBinding.narrow(var1.getBinding()) != null || Soap12Binding.narrow(var1.getBinding()) != null) {
         if (verbose) {
            Verbose.log((Object)("Building port: " + var1.getName()));
         }

         if (var2) {
            this.endpoint = new WsSkel(this.wsService, var1.getPortType());
            if (this.wasc != null) {
               ((WsSkel)this.endpoint).setContext(this.wasc.getEnvironmentContext());
            }
         } else {
            this.endpoint = new WsStub(this.wsService, var1.getPortType());
         }

         this.wsService.addEndpoint(var1.getPortType().getName(), this.endpoint);
         this.wsPort = this.wsService.addPort(var1.getName().getLocalPart(), var1, this.endpoint);
         if (this.mappingdd != null) {
            this.populatePort();
         } else {
            this.buildWithoutMappingFile();
         }

      }
   }

   private void populatePort() throws WsException {
      ExceptionMappingBean[] var1 = this.mappingdd.getExceptionMappings();
      JavaXmlTypeMappingBean[] var2 = this.mappingdd.getJavaXmlTypeMappings();
      ServiceInterfaceMappingBean[] var3 = this.mappingdd.getServiceInterfaceMappings();
      ServiceEndpointInterfaceMappingBean[] var4 = this.mappingdd.getServiceEndpointInterfaceMappings();
      if ((var1 == null || var1.length == 0) && (var2 == null || var2.length == 0) && (var3 == null || var3.length == 0) && (var4 == null || var4.length == 0)) {
         this.buildSimpleCase();
      } else {
         WsdlBinding var5 = this.wsPort.getWsdlPort().getBinding();
         QName var6 = this.wsService.getWsdlService().getName();
         QName var7 = var5.getName();
         QName var8 = var5.getPortType().getName();

         int var9;
         for(var9 = 0; var9 < var3.length; ++var9) {
            if (var3[var9].getWsdlServiceName().equals(var6)) {
            }
         }

         HolderUtil.NameCollisionsFilter.getInstance().reset();
         this.consumeHolders(var4);

         for(var9 = 0; var9 < var4.length; ++var9) {
            if (var4[var9].getWsdlBinding().equals(var7) && var4[var9].getWsdlPortType().equals(var8)) {
               String var10 = var4[var9].getServiceEndpointInterface();
               ((WsEndpointImpl)this.endpoint).setEndpointInterface(this.loadClass(var10));
               this.populateOperations(var4[var9]);
               return;
            }
         }

         throw new WsException("Can't find service-endpoint-interface-mapping for wsdl port:" + this.wsPort.getWsdlPort());
      }
   }

   private void consumeHolders(ServiceEndpointInterfaceMappingBean[] var1) throws WsException {
      WsdlPortType var2 = this.wsPort.getWsdlPort().getPortType();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         ServiceEndpointMethodMappingBean[] var4 = var1[var3].getServiceEndpointMethodMappings();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            QName var6 = new QName(var2.getName().getNamespaceURI(), var4[var5].getWsdlOperation());
            WsdlOperation var7 = (WsdlOperation)var2.getOperations().get(var6);
            if (var7 != null) {
               MethodParamPartsMappingBean[] var8 = var4[var5].getMethodParamPartsMappings();

               for(int var9 = 0; var9 < var8.length; ++var9) {
                  WsdlMessageMappingBean var10 = var8[var9].getWsdlMessageMapping();
                  if (this.isHolder(var10)) {
                     String var11 = var8[var9].getParamType();
                     String[] var12 = this.getTypeHolders(var11, var10, var7);

                     for(int var13 = 0; var13 < var12.length; ++var13) {
                        HolderUtil.NameCollisionsFilter.getInstance().use(var12[var13]);
                     }
                  }
               }
            }
         }
      }

   }

   private Class loadClass(String var1) throws WsException {
      try {
         return ClassUtil.loadClass(var1);
      } catch (ClassUtil.ClassUtilException var3) {
         throw new WsException("Failed to load class " + var1 + ":" + var3, var3);
      }
   }

   private void buildSimpleCase() throws WsException {
      this.buildWithoutMappingFile();
   }

   private void buildWithoutMappingFile() throws WsException {
      WsdlPortType var1 = this.wsPort.getWsdlPort().getPortType();
      WsdlBinding var2 = this.wsPort.getWsdlPort().getBinding();
      Iterator var3 = var1.getOperations().values().iterator();

      while(var3.hasNext()) {
         WsdlOperation var4 = (WsdlOperation)var3.next();
         WsMethodImpl var5 = new WsMethodImpl(this.endpoint, var4);
         ((WsEndpointImpl)this.endpoint).addMethod(var4.getName().getLocalPart(), var5);
         var5.setMethodName(var4.getName().getLocalPart());
         this.populateParts(var5, var4, var2);
      }

   }

   private void populateOperations(ServiceEndpointInterfaceMappingBean var1) throws WsException {
      WsdlPortType var2 = this.wsPort.getWsdlPort().getPortType();
      WsdlBinding var3 = this.wsPort.getWsdlPort().getBinding();
      ServiceEndpointMethodMappingBean[] var4 = var1.getServiceEndpointMethodMappings();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         QName var6 = new QName(var2.getName().getNamespaceURI(), var4[var5].getWsdlOperation());
         WsdlOperation var7 = (WsdlOperation)var2.getOperations().get(var6);
         if (var7 == null) {
            throw new WsException("There is no operation \"" + var6 + "\"" + " defined in wsdl.");
         }

         this.populateOperation(var7, var3, var4[var5]);
      }

   }

   private void populateOperation(WsdlOperation var1, WsdlBinding var2, ServiceEndpointMethodMappingBean var3) throws WsException {
      if (verbose) {
         Verbose.log((Object)("Building operation: " + var1.getName()));
      }

      WsMethodImpl var4 = new WsMethodImpl(this.endpoint, var1);
      ((WsEndpointImpl)this.endpoint).addMethod(var1.getName().getLocalPart(), var4);
      Method var5 = this.findSeiMethod(var3, var1);
      var4.setMethodName(var3.getJavaMethodName());
      if (var3.getWrappedElement() != null) {
         this.populateWrapedOperation(var4, var1, var3);
         this.populateFaults(var1.getFaults(), var4);
      } else {
         this.populateParts(var4, var1, var2);
      }

      this.matchMethodParams(var4, var3, var1);
      this.matchReturnAndPart(var4, var1, var5, var3.getWsdlReturnValueMapping());
      this.matchException(var4, var5);
   }

   private void matchMethodParams(WsMethod var1, ServiceEndpointMethodMappingBean var2, WsdlOperation var3) throws WsException {
      MethodParamPartsMappingBean[] var4 = var2.getMethodParamPartsMappings();
      Iterator var5;
      if (var4.length == 0) {
         var5 = var1.getParameters();

         while(var5.hasNext()) {
            WsParameterType var6 = (WsParameterType)var5.next();
            if (var6.getMode() == 0) {
               throw new WsException("There is no method-param-parts-mapping defined for method:" + var2.getJavaMethodName());
            }
         }
      } else {
         var5 = var1.getParameters();

         for(int var8 = 0; var8 < var4.length; ++var8) {
            MethodParamPartsMappingBean var7 = var4[var8];
            if (!var5.hasNext()) {
               throw new WsException("For wsdl operation \"" + var3.getName() + "\"" + " java method \"" + "\"" + var1.getMethodName() + " wsdl " + "defined less parts than java parameters.");
            }

            var5.next();
            this.matchParameterAndPart(var1, var3, var7);
         }

         if (var5.hasNext()) {
            throw new WsException("For wsdl operation \"" + var3.getName() + "\"" + " java method \"" + "\"" + var1.getMethodName() + " wsdl " + "defined more parts than java parameters.");
         }
      }

   }

   private void populateWrapedOperation(WsMethodImpl var1, WsdlOperation var2, ServiceEndpointMethodMappingBean var3) throws WsException {
      MethodParamPartsMappingBean[] var4 = var3.getMethodParamPartsMappings();
      MethodParamPartsMappingBean[] var5 = new MethodParamPartsMappingBean[var4.length];

      int var8;
      for(int var6 = 0; var6 < var4.length; ++var6) {
         MethodParamPartsMappingBean var7 = var4[var6];
         var8 = var7.getParamPosition();
         var5[var8] = var7;
      }

      WsdlMessage var15 = var2.getInput();
      if (var15 == null) {
         throw new WsException("Wsdl operation " + var2.getName() + " is not " + "a wrapped operation as marked in JAXRPC mapping file. There is no" + " input wsdl message.");
      } else {
         XmlTypeName var16 = this.getWrapElement(var15);
         var1.setWrapperElement(var16.getQName());

         String var11;
         for(var8 = 0; var8 < var5.length; ++var8) {
            MethodParamPartsMappingBean var9 = var5[var8];
            WsdlMessageMappingBean var10 = var9.getWsdlMessageMapping();
            var11 = var10.getWsdlMessagePartName();
            WsParameterType var12 = new WsParameterType(var11, getTypeMode(var10.getParameterMode()));
            this.setSoapHeader(var10, var12);
            if (var12.isHeader()) {
               WsdlMessage var13 = var15;
               if (var12.getMode() == 1) {
                  var13 = var2.getOutput();
                  if (var13 == null) {
                     throw new WsException("Wsdl operation " + var2.getName() + " has no " + "ouput message, but JAXRPC mapping file has an output header defined.");
                  }
               }

               WsdlPart var14 = (WsdlPart)var13.getParts().get(var10.getWsdlMessagePartName());

               assert var14 != null;

               var12.setXmlName(XmlTypeName.forGlobalName('e', var14.getElement()));
               var12.setElementName(var14.getElement());
            } else {
               var12.setXmlName(var16);
            }

            var1.addParameter(var11, var12);
         }

         WsdlMessage var17 = var2.getOutput();
         WsdlReturnValueMappingBean var18 = var3.getWsdlReturnValueMapping();
         if (var17 == null) {
            if (var18 != null) {
               throw new WsException("Wsdl operation " + var2.getName() + " has no " + "ouput message, but JAXRPC mapping file has return value mapping.");
            }
         } else {
            XmlTypeName var19 = this.getWrapElement(var17);
            var1.setReturnWrapperElement(var19.getQName());
            if (var18 != null) {
               var11 = var18.getWsdlMessagePartName();
               WsReturnType var20 = new WsReturnType(var11);
               var20.setXmlName(var19);
               var1.setReturnType(var20);
            }
         }

      }
   }

   private static int getTypeMode(String var0) {
      if (var0.equals("INOUT")) {
         return 2;
      } else {
         return var0.equals("OUT") ? 1 : 0;
      }
   }

   private XmlTypeName getWrapElement(WsdlMessage var1) throws WsException {
      Iterator var2 = var1.getParts().values().iterator();
      if (!var2.hasNext()) {
         throw new WsException("WsdlMessage " + var1.getName() + " for a " + "doc-lit-wrap style operation doesn't have any part.");
      } else {
         WsdlPart var3 = (WsdlPart)var2.next();
         QName var4 = var3.getElement();
         if (var4 == null) {
            throw new WsException("WsdlMessage " + var1.getName() + ", part " + var3.getName() + ", for a doc-lit-wrap style operation should use " + " global element.");
         } else {
            return XmlTypeName.forGlobalName('e', var4);
         }
      }
   }

   private void matchException(WsMethod var1, Method var2) throws WsException {
      List var3 = Arrays.asList(var2.getExceptionTypes());
      Iterator var4 = var1.getExceptions();

      while(true) {
         while(var4.hasNext()) {
            WsFault var5 = (WsFault)var4.next();
            WsdlMessage var6 = var5.getFaultMessage();
            ExceptionMappingBean var7 = this.findExceptionMappingBean(var6);
            if (var7 == null) {
               var4.remove();
            } else {
               Class var8 = this.loadClass(var7.getExceptionType());
               if (!var3.contains(var8)) {
                  throw new WsException("Method " + var2.getName() + " doesn't " + " throw " + var8 + " as decribed in exception mapping.");
               }

               var5.setExceptionClass(var8);
               ConstructorParameterOrderBean var9 = var7.getConstructorParameterOrder();
               if (var9 != null && var9.getElementNames().length >= 2) {
                  if (verbose) {
                     Verbose.log((Object)("++++  exception " + var8.getName() + " is NOT a marshal property Exception. "));
                  }
               } else {
                  if (verbose) {
                     Verbose.log((Object)("++++  process single property java exception " + var8.getName()));
                  }

                  Constructor var10 = ExceptionUtil.getConstructorForSingleArgClass(var8);
                  if (var10 != null) {
                     Class[] var11 = var10.getParameterTypes();
                     if (var11.length > 0) {
                        Class var12 = var11[0];
                        if (var12 != null) {
                           if (ExceptionUtil.isMarshalPropertyJavaClass(var12)) {
                              Method var13 = this.getGetterForClass(var12, var8);
                              if (var13 == null) {
                                 Verbose.log((Object)(" warning.  In exception " + var8.getName() + ", could not " + "find getter method that returns the constructor " + "single param class " + var12.getName()));
                              } else if (verbose) {
                                 Verbose.log((Object)("++++  found marshalProperty Exception getter method " + var13.getName()));
                              }

                              var5.setMarshalPropertyGetterMethod(var13);
                              var5.setMarshalPropertyClass(var12);
                              var5.setMarshalPropertyExceptionConstructor(var10);
                              if (verbose) {
                                 Verbose.log((Object)("++++  marshalPropertyClass '" + var12 + "' on WsFault " + var5));
                              }
                           } else if (verbose) {
                              Verbose.log((Object)("++++  single exception parameter class '" + var12.getName() + "' is not a marshal property class.  The exception will be marshalled as " + "the actual exception class '" + var8.getName() + "'"));
                           }
                        }
                     }
                  }
               }
            }
         }

         return;
      }
   }

   private Method getGetterForClass(Class var1, Class var2) {
      Method var3 = null;
      Method[] var4 = var2.getDeclaredMethods();

      int var5;
      for(var5 = 0; var5 < var4.length; ++var5) {
         if (var4[var5].getName().startsWith("get") && var4[var5].getReturnType().equals(var1)) {
            var3 = var4[var5];
            break;
         }
      }

      if (var3 != null) {
         return var3;
      } else {
         var4 = var2.getMethods();

         for(var5 = 0; var5 < var4.length; ++var5) {
            if (var4[var5].getName().startsWith("get") && var4[var5].getReturnType().equals(var1)) {
               var3 = var4[var5];
               break;
            }
         }

         return var3;
      }
   }

   private ExceptionMappingBean findExceptionMappingBean(WsdlMessage var1) throws WsException {
      ExceptionMappingBean[] var2 = this.mappingdd.getExceptionMappings();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         ExceptionMappingBean var4 = var2[var3];
         if (var1.getName().equals(var4.getWsdlMessage())) {
            String var5 = var4.getWsdlMessagePartName();
            if (var5 != null && var1.getParts().get(var5) == null) {
               throw new WsException("Exception mapping for WSDL message " + var1.getName() + ", part " + var5 + " is not defined in WSDL.");
            }

            return var4;
         }
      }

      return null;
   }

   private Method findSeiMethod(ServiceEndpointMethodMappingBean var1, WsdlOperation var2) throws WsException {
      Class[] var3 = null;
      MethodParamPartsMappingBean[] var4 = var1.getMethodParamPartsMappings();
      int var5;
      if (var4 != null && var4.length != 0) {
         var3 = new Class[var4.length];

         for(var5 = 0; var5 < var4.length; ++var5) {
            int var6 = var4[var5].getParamPosition();
            if (var6 >= var4.length) {
               throw new WsException("For method:" + var1.getJavaMethodName() + " ParamPosition \"" + var6 + "\" is out of range.");
            }

            WsdlMessageMappingBean var7 = var4[var5].getWsdlMessageMapping();
            String var8 = var4[var5].getParamType();
            if (!this.isHolder(var7)) {
               var3[var6] = this.loadClass(var8);
            } else {
               String[] var9 = this.getTypeHolders(var8, var7, var2);
               int var10 = 0;

               while(var10 < var9.length) {
                  var8 = var9[var10];
                  var8 = HolderUtil.NameCollisionsFilter.getInstance().filterFullName(var8);

                  try {
                     Class var11 = this.loadClass(var8);
                     var3[var6] = var11;
                     break;
                  } catch (WsException var13) {
                     if (var10 == var9.length - 1) {
                        throw var13;
                     }

                     ++var10;
                  }
               }
            }
         }
      }

      for(var5 = 0; var5 < var4.length; ++var5) {
         if (var3[var5] == null) {
            throw new WsException("Method \"" + var1.getJavaMethodName() + "\"" + "'s paramter #" + var5 + " is not defined.");
         }
      }

      Class var14 = this.endpoint.getEndpointInterface();
      String var15 = var1.getJavaMethodName();

      try {
         return var14.getMethod(var15, var3);
      } catch (NoSuchMethodException var12) {
         throw new WsException("Can't find method \"" + this.methodSig(var15, var3) + "\"" + " in service end point interface '" + var14.getName() + "'. It must" + "be: " + this.methodDescription(var14));
      }
   }

   private String[] getTypeHolders(String var1, WsdlMessageMappingBean var2, WsdlOperation var3) throws WsException {
      WsdlMessage var4;
      if ("OUT".equalsIgnoreCase(var2.getParameterMode())) {
         var4 = var3.getOutput();
      } else {
         var4 = var3.getInput();
      }

      String var5 = var2.getWsdlMessagePartName();
      WsdlPart var6 = (WsdlPart)var4.getParts().get(var5);
      if (var6 == null) {
         throw new WsException("Failed to find part " + var5 + " in wsdl message :" + var4);
      } else {
         QName var7 = var6.getType();
         if (var7 == null) {
            var7 = var6.getElement();
         }

         return this.getHolderClasses(var1, var7);
      }
   }

   private String[] getHolderClasses(String var1, QName var2) throws WsException {
      String var3 = HolderUtil.getStandardHolder(var1);
      if (var3 != null) {
         return new String[]{var3};
      } else if (HolderUtil.isStandardHolderClass(var1)) {
         return new String[]{var1};
      } else {
         String var4 = var2.getNamespaceURI();
         if (!"http://www.w3.org/2001/XMLSchema".equals(var4) && !"http://schemas.xmlsoap.org/soap/encoding/".equals(var4) && !"http://www.w3.org/2003/05/soap-encoding".equals(var4)) {
            if (!var1.startsWith("java.") && !var1.startsWith("javax.") && !HolderUtil.isPrimitiveType(var1) && !var1.startsWith("[") && !var1.endsWith("[]")) {
               var3 = HolderUtil.getHolderClass(var1);
               return new String[]{var3};
            } else {
               PackageMappingBean[] var5 = this.mappingdd.getPackageMappings();
               String[] var6 = this.getPackageNames(var2, var5);

               for(int var7 = 0; var7 < var6.length; ++var7) {
                  String var8 = var6[var7];
                  if (StringUtil.isEmpty(var8)) {
                     var8 = "holders";
                  } else {
                     var8 = var8 + ".holders";
                  }

                  String var9 = this.getJaxrpcClassName(var2.getLocalPart());
                  var3 = var8 + "." + var9 + "Holder";
                  var6[var7] = var3;
               }

               return var6;
            }
         } else {
            var3 = this.getStandardSchemaHolder(var2);
            return new String[]{var3};
         }
      }
   }

   private String getJaxrpcClassName(String var1) {
      return NameUtil.getJAXRPCClassName(var1);
   }

   private String[] getPackageNames(QName var1, PackageMappingBean[] var2) throws WsException {
      ArrayList var3 = new ArrayList();

      for(int var4 = 0; var4 < var2.length; ++var4) {
         if (var1.getNamespaceURI().equals(var2[var4].getNamespaceURI())) {
            var3.add(var2[var4].getPackageType());
         }
      }

      if (var3.isEmpty()) {
         throw new WsException("unable to find pacakge mapping for: " + var1.getNamespaceURI());
      } else {
         return (String[])var3.toArray(new String[var3.size()]);
      }
   }

   private String getStandardSchemaHolder(QName var1) throws WsException {
      if (!"base64Binary".equals(var1.getLocalPart()) && !"hexBinary".equals(var1.getLocalPart()) && !"base64".equals(var1.getLocalPart())) {
         throw new WsException("Unknown standard schema type: " + var1);
      } else {
         return ByteArrayHolder.class.getName();
      }
   }

   private boolean isHolder(WsdlMessageMappingBean var1) {
      if (var1 == null) {
         return false;
      } else {
         String var2 = var1.getParameterMode();
         return "OUT".equalsIgnoreCase(var2) || "INOUT".equalsIgnoreCase(var2);
      }
   }

   private String methodSig(String var1, Class[] var2) {
      StringBuffer var3 = new StringBuffer();
      var3.append(var1);
      var3.append("(");
      if (var2 != null) {
         for(int var4 = 0; var4 < var2.length; ++var4) {
            var3.append(var2[var4].getName());
            if (var4 < var2.length - 1) {
               var3.append(",");
            }
         }
      }

      var3.append(")");
      return var3.toString();
   }

   private String methodDescription(Class var1) {
      StringBuffer var2 = new StringBuffer();
      Method[] var3 = var1.getMethods();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         var2.append(var3[var4].getName());
         var2.append(",");
      }

      return var2.toString();
   }

   private void matchParameterAndPart(WsMethod var1, WsdlOperation var2, MethodParamPartsMappingBean var3) throws WsException {
      int var4 = var3.getParamPosition();
      WsParameterType var5 = var1.getParameter(var4);
      if (verbose) {
         Verbose.log((Object)("WSDL java type: " + var5.getJavaType() + " JAX-RPC mapping java type: " + var3.getParamType()));
      }

      var5.setJavaType(this.loadClass(var3.getParamType()));
      WsdlMessageMappingBean var6 = var3.getWsdlMessageMapping();
      this.setSoapHeader(var6, var5);
      if ("IN".equals(var6.getParameterMode())) {
         this.checkInParameter(var1, var5, var2, var6);
      } else if ("OUT".equals(var6.getParameterMode())) {
         this.checkOutParameter(var1, var5, var2, var6);
      } else {
         if (!"INOUT".equals(var6.getParameterMode())) {
            throw new WsException("Parameter mode \"" + var6.getParameterMode() + "\"" + "is an invalid value.");
         }

         this.checkInOutParameter(var1, var5, var2, var6);
      }

      if (!var1.isWrapped()) {
         WsdlMessage var7 = (WsdlMessage)this.wsService.getWsdlService().getDefinitions().getMessages().get(var6.getWsdlMessage());
         if (var7 == null) {
            throw new WsException("Wsdl message " + var6.getWsdlMessage() + " for" + " Method " + var1.getMethodName() + " is not found in wsdl.");
         }

         String var8 = var6.getWsdlMessagePartName();
         if (var7.getParts().get(var8) == null) {
            throw new WsException("wsdl part name " + var8 + " for Method \"" + var1.getMethodName() + "\" is not found in wsdl message." + var7.getName() + " Please sync up part name \"" + var8 + "\" defined in" + " JAXRPC mapping file with part name \"" + var5.getName() + "\" defined in wsdl.");
         }
      }

   }

   private void setSoapHeader(WsdlMessageMappingBean var1, WsParameterType var2) {
      if (var1.getSoapHeader() != null) {
         var2.setHeader(true);
      }

   }

   private void checkInOutParameter(WsMethod var1, WsParameterType var2, WsdlOperation var3, WsdlMessageMappingBean var4) throws WsException {
      if (var1.isWrapped() && !var2.isHeader()) {
         throw new WsException("Method \"" + var1.getMethodName() + "\"" + " parameter \"" + var2.getName() + "\"" + " " + " wrap style method can't use INOUT parameter.");
      } else if (2 != var2.getMode()) {
         throw new WsException("Method \"" + var1.getMethodName() + "\"" + " parameter \"" + var2.getName() + "\"" + " " + "Parameter mode is not in-sync with wsdl." + " Expected mode " + var2.getModeAsString());
      } else {
         QName var5 = var4.getWsdlMessage();
         QName var6 = var3.getInput().getName();
         QName var7 = var3.getOutput().getName();
         if (!var5.equals(var6) && !var5.equals(var7)) {
            throw new WsException("wsdl message name for Method \"" + var1.getMethodName() + "\" parameter \"" + var2.getName() + "\" is not in-sync with wsdl.");
         } else {
            String[] var8 = this.getHolderClasses(var2.getJavaType().getName(), var2.getXmlName().getQName());
            int var9 = 0;

            while(var9 < var8.length) {
               String var10 = var8[var9];
               var10 = HolderUtil.NameCollisionsFilter.getInstance().filterFullName(var10);

               try {
                  Class var11 = this.loadClass(var10);
                  var2.setJavaHolderType(var11);
                  break;
               } catch (WsException var12) {
                  if (var9 == var8.length - 1) {
                     throw var12;
                  }

                  ++var9;
               }
            }

         }
      }
   }

   private void checkOutParameter(WsMethod var1, WsParameterType var2, WsdlOperation var3, WsdlMessageMappingBean var4) throws WsException {
      if (var1.isWrapped() && !var2.isHeader()) {
         throw new WsException("Method \"" + var1.getMethodName() + "\"" + " parameter \"" + var2.getName() + "\"" + " " + " wrap style method can't use OUT parameter.");
      } else if (1 != var2.getMode()) {
         throw new WsException("Method \"" + var1.getMethodName() + "\"" + " parameter \"" + var2.getName() + "\"" + " " + "Parameter mode is not in-sync with wsdl." + " Expected mode " + var2.getModeAsString());
      } else {
         QName var5 = var4.getWsdlMessage();
         if (!var3.getOutput().getName().equals(var5)) {
            throw new WsException("wsdl message name " + var5 + " for Method \"" + var1.getMethodName() + "\" parameter \"" + var2.getName() + "\" is not in-sync with wsdl. Expected message name " + var3.getOutput().getName());
         } else {
            String[] var6 = this.getHolderClasses(var2.getJavaType().getName(), var2.getXmlName().getQName());
            int var7 = 0;

            while(var7 < var6.length) {
               String var8 = var6[var7];
               var8 = HolderUtil.NameCollisionsFilter.getInstance().filterFullName(var8);

               try {
                  Class var9 = this.loadClass(var8);
                  var2.setJavaHolderType(var9);
                  break;
               } catch (WsException var10) {
                  if (var7 == var6.length - 1) {
                     throw var10;
                  }

                  ++var7;
               }
            }

         }
      }
   }

   private void checkInParameter(WsMethod var1, WsParameterType var2, WsdlOperation var3, WsdlMessageMappingBean var4) throws WsException {
      if (0 != var2.getMode()) {
         throw new WsException("Method \"" + var1.getMethodName() + "\"" + " parameter \"" + var2.getName() + "\"" + " " + "Parameter mode is not in-sync with wsdl." + " Expected mode " + var2.getModeAsString());
      } else {
         QName var5 = var4.getWsdlMessage();
         if (!var3.getInput().getName().equals(var5)) {
            throw new WsException("wsdl message name " + var5 + " for Method \"" + var1.getMethodName() + "\" parameter \"" + var2.getName() + "\" is not in-sync with wsdl. Expected message name " + var3.getInput());
         }
      }
   }

   private void matchReturnAndPart(WsMethod var1, WsdlOperation var2, Method var3, WsdlReturnValueMappingBean var4) throws WsException {
      if (var1.getReturnType() == null) {
         if (var3.getReturnType() != Void.TYPE) {
            throw new WsException("There is no return type defined in wsdl for method \"" + var1.getMethodName() + "\", but in Service" + "Endpoint Inteface, there is a return type.");
         } else if (var4 != null && !"void".equals(var4.getMethodReturnValue())) {
            throw new WsException("There is no return type defined in wsdl for method \"" + var1.getMethodName() + "\", but in jaxrpc mapping file, there is a return type mapping," + "with return value : " + var4.getMethodReturnValue());
         }
      } else {
         Class var5;
         if (var4 == null) {
            var5 = Void.TYPE;
         } else {
            var5 = this.loadClass(var4.getMethodReturnValue());
         }

         if (var5 != var3.getReturnType()) {
            if (verbose) {
               Verbose.log((Object)"WSBuilderHelper.matchReturnAndPart - type != seiMethod.getReturnType()");
               Verbose.log((Object)("type = " + var5));
               Verbose.log((Object)("classloader: " + var5.getClassLoader()));
               ProtectionDomain var8 = var5.getProtectionDomain();
               Verbose.log((Object)("domain:" + var8));
               if (var8 != null && var8.getCodeSource() != null) {
                  Verbose.log((Object)("loaded from :" + var8.getCodeSource().getLocation()));
               }

               Verbose.log((Object)("seiMethod.getReturnType() = " + var3.getReturnType()));
               Verbose.log((Object)("classloader: " + var3.getReturnType().getClassLoader()));
               ProtectionDomain var9 = var3.getReturnType().getProtectionDomain();
               Verbose.log((Object)("domain:" + var9));
               if (var9 != null && var9.getCodeSource() != null) {
                  Verbose.log((Object)("loaded from :" + var9.getCodeSource().getLocation()));
               }
            }

            throw new WsException("Method \"" + var3.getName() + "\"" + " has " + var5 + " as return type in DD, and " + var3.getReturnType() + " in service endpoint interface.");
         } else {
            var1.getReturnType().setJavaType(var5);
            if (var4 != null) {
               QName var6 = var2.getOutput().getName();
               String var7 = var4.getWsdlMessagePartName();
               if (!var6.equals(var4.getWsdlMessage())) {
                  throw new WsException("wsdl message name for Method \"" + var1.getMethodName() + "\" return type \"" + var7 + "\" is not in-sync with wsdl.");
               }

               if (!var1.isWrapped() && var7 != null && var2.getOutput().getParts().get(var7) == null) {
                  throw new WsException("wsdl part name for Method \"" + var1.getMethodName() + "\" return type \"" + var7 + "\" is not found in wsdl <output> message.");
               }
            }

         }
      }
   }

   private void populateParts(WsMethodImpl var1, WsdlOperation var2, WsdlBinding var3) {
      Object var4 = SoapBinding.narrow(var3);
      if (var4 == null) {
         var4 = Soap12Binding.narrow(var3);
      }

      WsdlBindingOperation var5 = (WsdlBindingOperation)var3.getOperations().get(var2.getName());
      Object var6 = SoapBindingOperation.narrow(var5);
      if (var6 == null) {
         var6 = Soap12BindingOperation.narrow(var5);
      }

      String var7 = SoapBindingUtil.getStyle((SoapBindingOperation)var6, (SoapBinding)var4);
      this.populateParts(var2, var1, var7);
      this.populateFaults(var2.getFaults(), var1);
   }

   private void populateFaults(Map var1, WsMethodImpl var2) {
      Iterator var3 = var1.entrySet().iterator();

      while(var3.hasNext()) {
         Map.Entry var4 = (Map.Entry)var3.next();
         WsFault var5 = new WsFault(var2, (String)var4.getKey(), (WsdlMessage)var4.getValue());
         var2.addException(var5);
      }

   }

   private void populateParts(WsdlOperation var1, WsMethodImpl var2, String var3) {
      WsdlMethod var4 = var1.getWsdlMethod(true);
      Iterator var5 = var4.getParameters().iterator();

      while(var5.hasNext()) {
         WsdlParameter var6 = (WsdlParameter)var5.next();
         WsParameterType var7 = this.createWsParameter(var6, var3);
         var2.addParameter(var7.getName(), var7);
      }

      WsdlPart var9 = var4.getResultPart();
      if (var9 != null) {
         XmlTypeName var10 = this.getPartXmlTypeName(var9);
         WsReturnType var8 = new WsReturnType(var9.getName());
         var8.setXmlName(var10);
         var2.setReturnType(var8);
      }

   }

   private WsParameterType createWsParameter(WsdlParameter var1, String var2) {
      byte var3 = 0;
      WsdlPart var4 = var1.getPrimaryPart();
      if (var1.getMode() == Mode.OUT) {
         var3 = 1;
      } else if (var1.getMode() == Mode.INOUT) {
         var3 = 2;
      }

      XmlTypeName var5 = this.getPartXmlTypeName(var4);
      WsParameterType var6 = new WsParameterType(var4.getName(), var3);
      var6.setXmlName(var5);
      return var6;
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
}
