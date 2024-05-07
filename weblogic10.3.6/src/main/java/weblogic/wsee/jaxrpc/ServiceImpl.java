package weblogic.wsee.jaxrpc;

import com.bea.xbean.xb.xsdschema.SchemaDocument;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Remote;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.rpc.Call;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.Stub;
import javax.xml.rpc.encoding.TypeMappingRegistry;
import javax.xml.rpc.handler.HandlerRegistry;
import javax.xml.stream.XMLStreamException;
import weblogic.application.descriptor.NamespaceURIMunger;
import weblogic.descriptor.DescriptorManager;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.j2ee.descriptor.JavaWsdlMappingBean;
import weblogic.j2ee.descriptor.ServiceRefBean;
import weblogic.j2ee.descriptor.wl.PortInfoBean;
import weblogic.j2ee.descriptor.wl.PropertyNamevalueBean;
import weblogic.j2ee.descriptor.wl.ServiceReferenceDescriptionBean;
import weblogic.j2ee.descriptor.wl.WeblogicWseeStandaloneclientBean;
import weblogic.wsee.bind.runtime.RuntimeBindings;
import weblogic.wsee.connection.transport.TransportInfo;
import weblogic.wsee.context.ContextNotFoundException;
import weblogic.wsee.context.WebServiceContext;
import weblogic.wsee.deploy.ClientDeployInfo;
import weblogic.wsee.deploy.WsdlAddressInfo;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.framework.PolicyMath;
import weblogic.wsee.policy.framework.PolicyStatement;
import weblogic.wsee.policy.runtime.PolicyFinder;
import weblogic.wsee.security.policy.SecurityPolicyCustomizer;
import weblogic.wsee.security.policy.assertions.SecurityPolicyAssertionFactory;
import weblogic.wsee.security.wssp.SecurityPolicyAssertionInfoFactory;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsEndpoint;
import weblogic.wsee.ws.WsException;
import weblogic.wsee.ws.WsFactory;
import weblogic.wsee.ws.WsMethod;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.WsService;
import weblogic.wsee.ws.WsStub;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlFactory;
import weblogic.wsee.wsdl.WsdlOperation;
import weblogic.wsee.wsdl.WsdlPartnerLinkType;
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.wsee.wsdl.WsdlPortType;
import weblogic.wsee.wsdl.WsdlService;
import weblogic.wsee.wsdl.WsdlTypes;
import weblogic.wsee.wsdl.WsdlUtils;
import weblogic.wsee.wsdl.builder.WsdlBindingBuilder;
import weblogic.wsee.wsdl.builder.WsdlDefinitionsBuilder;
import weblogic.wsee.wsdl.builder.WsdlOperationBuilder;
import weblogic.wsee.wsdl.builder.WsdlPortBuilder;
import weblogic.wsee.wsdl.builder.WsdlPortTypeBuilder;
import weblogic.wsee.wsdl.builder.WsdlServiceBuilder;
import weblogic.wsee.wsdl.soap11.SoapAddress;
import weblogic.wsee.wsdl.validation.WsdlValidationException;
import weblogic.wsee.wsdl.validation.WsdlValidationRegistry;
import weblogic.xml.schema.binding.internal.NameUtil;

public class ServiceImpl implements WLService {
   private static final boolean verbose = Verbose.isVerbose(ServiceImpl.class);
   static final int FULL_WSDL = 1;
   static final int PARTIAL_WSDL = 2;
   static final int NO_WSDL = 3;
   private static final String DEFAULT_SERVICE_NAME = "weblogic_defalt_service_name_for_partial_wsdl";
   private WsService webservice;
   private String wsdlLocation;
   private QName serviceName;
   private int wsdlStatus;
   private Map portComponentLinks;
   private Map bareCallProps;
   private Map portCallProps;
   private Map portStubProps;
   private HandlerRegistry handlerRegistry;
   private boolean hasCallback;
   private boolean has81Callback;
   private QName callbackServiceQName;
   private boolean allowHandlerChain;
   private boolean policyListenerLoaded;
   protected TransportInfo transportInfo;
   private ClientDeployInfo clientDeployInfo;

   protected ServiceImpl(String var1, QName var2, String var3) throws ServiceException {
      this(var1, var2, var3, (TransportInfo)null);
   }

   protected ServiceImpl(String var1, QName var2, String var3, TransportInfo var4) throws ServiceException {
      this.portComponentLinks = new HashMap();
      this.bareCallProps = new HashMap();
      this.portCallProps = new HashMap();
      this.portStubProps = new HashMap();
      this.handlerRegistry = new HandlerRegistryImpl();
      this.hasCallback = false;
      this.has81Callback = false;
      this.callbackServiceQName = null;
      this.allowHandlerChain = true;
      this.policyListenerLoaded = false;
      this.transportInfo = null;
      if (verbose) {
         Verbose.logArgs("wsdl", var1, "service name", var2, "configuration", var3);
      }

      this.transportInfo = var4;
      String var5 = getWsdlUrl(var1);
      ClientDeployInfo var6 = new ClientDeployInfo();
      var6.setWsdlDef(this.loadWsdlDefinition(var5, var2));
      var6.setServiceName(var2);
      this.loadInternalDD(var6, var3);
      this.init(var6);
      this.parseCallAndStubProps(var6.getWlServiceRef());
   }

   ServiceImpl(ClientDeployInfo var1, String var2) throws ServiceException {
      this.portComponentLinks = new HashMap();
      this.bareCallProps = new HashMap();
      this.portCallProps = new HashMap();
      this.portStubProps = new HashMap();
      this.handlerRegistry = new HandlerRegistryImpl();
      this.hasCallback = false;
      this.has81Callback = false;
      this.callbackServiceQName = null;
      this.allowHandlerChain = true;
      this.policyListenerLoaded = false;
      this.transportInfo = null;
      if (verbose) {
         Verbose.log((Object)("Non generated Impl client created : wsdl " + var2));
      }

      if (var2 == null) {
         if (verbose) {
            Verbose.log((Object)"Service without WSDL");
         }

         this.wsdlStatus = 3;
         this.serviceName = var1.getServiceName();
      } else {
         this.wsdlLocation = var2;
         var1.setWsdlDef(this.loadWsdlDefinition(var2, var1.getServiceName()));
         this.init(var1);
      }

      this.parseCallAndStubProps(var1.getWlServiceRef());
   }

   private void init(ClientDeployInfo var1) throws ServiceException {
      try {
         this.checkWsdlDefinition(var1);
         if (var1.getMappingdd() != null) {
            WsFactory var2 = WsFactory.instance();
            this.webservice = var2.createClientService(var1);
         } else {
            this.clientDeployInfo = var1;
         }

         this.initHandlerRegistry(var1);
      } catch (WsdlValidationException var3) {
         throwServiceException(var3.getMessage(), var3);
      } catch (WsException var4) {
         throwServiceException(var4.getMessage(), var4);
      }

   }

   private void initHandlerRegistry(ClientDeployInfo var1) {
      ServiceRefBean var2 = var1.getServiceRef();
      if (var2 != null) {
         this.handlerRegistry = new HandlerRegistryImpl(var2.getHandlers());
      }

   }

   static void throwServiceException(String var0, Throwable var1) throws ServiceException {
      ServiceException var2 = new ServiceException(var0, var1);
      if (var2.getCause() == null) {
         var2.initCause(var1);
      }

      throw var2;
   }

   private void parseCallAndStubProps(ServiceReferenceDescriptionBean var1) throws ServiceException {
      if (var1 != null) {
         PropertyNamevalueBean[] var2 = var1.getCallProperties();
         this.populateCallProperties(var2);
         PortInfoBean[] var3 = var1.getPortInfos();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            PortInfoBean var5 = var3[var4];
            String var6 = var5.getPortName();
            this.populatePortProperties(var5, var6);
            this.populateStubProperties(var5, var6);
         }

      }
   }

   private void populateStubProperties(PortInfoBean var1, String var2) {
      PropertyNamevalueBean[] var3 = var1.getStubProperties();
      if (var3 != null && var3.length > 0) {
         HashMap var4 = new HashMap();

         for(int var5 = 0; var5 < var3.length; ++var5) {
            PropertyNamevalueBean var6 = var3[var5];
            var4.put(var6.getName(), var6.getValue());
         }

         this.portStubProps.put(var2, var4);
      }

   }

   private void populatePortProperties(PortInfoBean var1, String var2) {
      PropertyNamevalueBean[] var3 = var1.getCallProperties();
      if (var3 != null && var3.length > 0) {
         HashMap var4 = new HashMap();

         for(int var5 = 0; var5 < var3.length; ++var5) {
            PropertyNamevalueBean var6 = var3[var5];
            var4.put(var6.getName(), var6.getValue());
         }

         this.portCallProps.put(var2, var4);
      }

   }

   private void populateCallProperties(PropertyNamevalueBean[] var1) throws ServiceException {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         PropertyNamevalueBean var3 = var1[var2];
         if (!CallImpl.isValidProperty(var3.getName())) {
            throw new ServiceException(var3.getName() + " is not a " + "valid JAX RPC Call object property.");
         }

         this.bareCallProps.put(var3.getName(), var3.getValue());
      }

   }

   private void checkWsdlDefinition(ClientDeployInfo var1) throws WsdlValidationException, ServiceException {
      WsdlDefinitionsBuilder var2 = (WsdlDefinitionsBuilder)var1.getWsdlDef();
      if (var2.getServices().size() == 0) {
         this.wsdlStatus = 2;
         if (verbose) {
            Verbose.log((Object)"Service with partial WSDL");
         }

         this.createDummyService(var2);
      } else {
         this.wsdlStatus = 1;
         if (verbose) {
            Verbose.log((Object)"Service with full WSDL");
         }

         this.findServiceName(var1, var2);
      }

      WsdlPartnerLinkType var3 = (WsdlPartnerLinkType)var2.getExtension("PartnerLinkType");
      if (var3 != null) {
         this.setCallbackServiceQName(var2, var3);
      } else {
         this.checkAndSet81CallbackServiceQName(var2, this.serviceName);
      }

      var1.setServiceName(this.serviceName);
      WsdlValidationRegistry.getInstance().validate(var2);
   }

   private void checkAndSet81CallbackServiceQName(WsdlDefinitionsBuilder var1, QName var2) {
      WsdlServiceBuilder var3 = (WsdlServiceBuilder)var1.getServices().get(var2);
      if (var3 == null) {
         throw new IllegalArgumentException("Cannot find a service with QName " + var2 + " in provided WSDL");
      } else {
         Iterator var4 = var3.getPortTypes().iterator();

         while(var4.hasNext()) {
            WsdlPortTypeBuilder var5 = (WsdlPortTypeBuilder)var4.next();
            Iterator var6 = var5.getOperations().values().iterator();

            while(var6.hasNext()) {
               WsdlOperationBuilder var7 = (WsdlOperationBuilder)var6.next();
               if (var7.isWLW81CallbackOperation()) {
                  this.callbackServiceQName = var2;
                  this.has81Callback = true;
                  return;
               }
            }
         }

      }
   }

   private void setCallbackServiceQName(WsdlDefinitions var1, WsdlPartnerLinkType var2) {
      this.hasCallback = true;
      Iterator var3 = var1.getServices().values().iterator();

      while(var3.hasNext()) {
         WsdlService var4 = (WsdlService)var3.next();
         Iterator var5 = var4.getPortTypes().iterator();

         while(var5.hasNext()) {
            WsdlPortType var6 = (WsdlPortType)var5.next();

            try {
               if (verbose) {
                  Verbose.log((Object)("Port type " + var6.getName() + " link name " + var2.getRole2Name() + ", " + var2.getPortTypeName("Callback")));
               }

               if (var6.getName().equals(var2.getPortTypeName("Callback"))) {
                  this.callbackServiceQName = var4.getName();
                  return;
               }
            } catch (WsdlException var8) {
               throw new JAXRPCException("Failed to parse WSDL", var8);
            }
         }
      }

   }

   public boolean hasCallback() {
      return this.hasCallback;
   }

   public boolean has81Callback() {
      return this.has81Callback;
   }

   public QName getCallbackServiceName() {
      return this.callbackServiceQName;
   }

   private void findServiceName(ClientDeployInfo var1, WsdlDefinitions var2) throws ServiceException {
      if (var1.getServiceName() != null) {
         this.serviceName = var1.getServiceName();
      } else if (var1.getServiceRef().getServiceQname() != null) {
         this.serviceName = var1.getServiceRef().getServiceQname();
      } else {
         Iterator var3 = var2.getServices().values().iterator();
         WsdlService var4 = (WsdlService)var3.next();
         if (var3.hasNext()) {
            throw new ServiceException("Wsdl has more than one service, service-name is required.");
         }

         this.serviceName = var4.getName();
      }

   }

   private void createDummyService(WsdlDefinitionsBuilder var1) {
      if (this.serviceName == null) {
         this.serviceName = new QName(var1.getTargetNamespace(), "weblogic_defalt_service_name_for_partial_wsdl");
      }

      WsdlServiceBuilder var2 = var1.addService(this.serviceName);
      Iterator var3 = var1.getBindings().values().iterator();

      while(var3.hasNext()) {
         WsdlBindingBuilder var4 = (WsdlBindingBuilder)var3.next();
         String var5 = var4.getPortType().getName().getNamespaceURI();
         QName var6 = new QName(var5, var4.getName() + "port");
         WsdlPortBuilder var7 = var2.addPort(var6, var4);
         var7.setBinding(var4.getName());
      }

   }

   private void loadInternalDD(ClientDeployInfo var1, String var2) throws ServiceException {
      this.loadWeblogicDD(var2, var1);
      this.loadJaxrpcMappingDD(var1);
   }

   private void loadJaxrpcMappingDD(ClientDeployInfo var1) throws ServiceException {
      InputStream var2 = null;

      try {
         String var3 = var1.getServiceRef().getJaxrpcMappingFile();
         var2 = this.loadResource(var3);
         if (var2 == null) {
            throw new ServiceException("weblogic client JAX_RPC mapping file " + var3 + "not found. Please make sure all " + "clientgen generated files are in the classpath.");
         }

         DescriptorManager var4 = new DescriptorManager();
         JavaWsdlMappingBean var5 = (JavaWsdlMappingBean)var4.createDescriptor(var2).getRootBean();
         var1.setMappingdd(var5);
      } catch (IOException var9) {
         throwServiceException("Failed to load weblogic client's  JAX RPC mapping file. " + var9, var9);
      } finally {
         this.ignoreClose(var2);
      }

   }

   private void ignoreClose(InputStream var1) {
      try {
         if (var1 != null) {
            var1.close();
         }
      } catch (IOException var3) {
      }

   }

   private void loadWeblogicDD(String var1, ClientDeployInfo var2) throws ServiceException {
      InputStream var3 = null;

      try {
         var3 = this.loadResource(var1);
         if (var3 == null) {
            throw new ServiceException("weblogic client internal deployment descriptor " + var1 + " not found. Please make sure all " + "clientgen generated files are in the classpath.");
         }

         DescriptorManager var4 = new DescriptorManager();
         String[] var5 = new String[]{"http://www.bea.com/ns/weblogic/90", "http://www.bea.com/ns/weblogic/weblogic-wsee-standaloneclient"};
         WeblogicWseeStandaloneclientBean var6 = (WeblogicWseeStandaloneclientBean)var4.createDescriptor(new NamespaceURIMunger(var3, "http://xmlns.oracle.com/weblogic/weblogic-wsee-standaloneclient", var5)).getRootBean();
         var2.setServiceRef(var6.getServiceRef());
      } catch (XMLStreamException var11) {
         throwServiceException("Failed to load weblogic client internal deployment descriptor. " + var11, var11);
      } catch (IOException var12) {
         throwServiceException("Failed to load weblogic client internal deployment descriptor. " + var12, var12);
      } finally {
         this.ignoreClose(var3);
      }

   }

   static String getWsdlUrl(String var0) throws ServiceException {
      try {
         new URL(var0);
         return var0;
      } catch (MalformedURLException var2) {
         URL var1 = loadResourceAsSource(var0);
         if (var1 != null) {
            return var1.toString();
         } else {
            throw new ServiceException("Invalid wsdl location " + var0);
         }
      }
   }

   private WsdlDefinitions loadWsdlDefinition(String var1, QName var2) throws ServiceException {
      try {
         WsdlDefinitions var3 = WsdlFactory.getInstance().parse(this.transportInfo, var1, true);
         if (var2 != null && var3.getServices().get(var2) == null) {
            throw new ServiceException("Can't find service \"" + var2 + "\" in wsdl \"" + var1 + "\".");
         } else {
            return var3;
         }
      } catch (WsdlException var4) {
         throwServiceException("Failed to parse WSDL " + var1 + " " + var4, var4);
         return null;
      }
   }

   private InputStream loadResource(String var1) {
      ClassLoader var2 = Thread.currentThread().getContextClassLoader();
      if (var2 == null) {
         var2 = this.getClass().getClassLoader();
      }

      InputStream var3 = null;
      var3 = var2.getResourceAsStream(var1);
      if (var3 == null) {
         var3 = this.getClass().getResourceAsStream(var1);
      }

      return var3;
   }

   private static URL loadResourceAsSource(String var0) {
      ClassLoader var1 = Thread.currentThread().getContextClassLoader();
      if (var1 == null) {
         var1 = var0.getClass().getClassLoader();
      }

      URL var2 = null;
      var2 = var1.getResource(var0);
      if (var2 == null) {
         var2 = var0.getClass().getResource(var0);
      }

      return var2;
   }

   private void setEmptyJaxrpcMapping(ClientDeployInfo var1) {
      if (var1.getMappingdd() == null) {
         EditableDescriptorManager var2 = new EditableDescriptorManager();
         JavaWsdlMappingBean var3 = (JavaWsdlMappingBean)var2.createDescriptorRoot(JavaWsdlMappingBean.class).getRootBean();
         var1.setMappingdd(var3);
      }
   }

   int getWsdlStatus() {
      return this.wsdlStatus;
   }

   WsService getWebService() {
      if (this.webservice == null) {
         try {
            WsFactory var1 = WsFactory.instance();
            this.webservice = var1.createClientService(this.clientDeployInfo);
         } catch (WsException var2) {
            if (verbose) {
               Verbose.logException(var2);
            }

            throw new JAXRPCException("Faile to create ws runtime structure " + var2, var2);
         }
      }

      return this.webservice;
   }

   void setPortComponentLinks(Map var1) {
      if (var1 != null) {
         this.portComponentLinks = var1;
      }

   }

   WsService createWebService(WsdlDefinitions var1, QName var2, RuntimeBindings var3) {
      try {
         WsFactory var4 = WsFactory.instance();
         ClientDeployInfo var5 = new ClientDeployInfo();
         var5.setWsdlDef(var1);
         var5.setServiceName(var2);
         this.setEmptyJaxrpcMapping(var5);
         var5.setRuntimeBindings(var3);
         this.webservice = var4.createClientService(var5);
      } catch (WsException var6) {
         if (verbose) {
            Verbose.logException(var6);
         }

         throw new JAXRPCException("Faile to create ws runtime structure " + var6, var6);
      }

      return this.webservice;
   }

   WsStub getWsStub(WsdlPort var1) {
      return (WsStub)this.getWebService().getEndpoint(var1.getPortType().getName());
   }

   HandlerRegistry _internalGetHandlerRegistry() {
      return this.handlerRegistry;
   }

   public String _getPortTransport(String var1) {
      WsdlPort var2 = this._getPort(var1);
      return var2.getTransport();
   }

   public String _getPortLocation(String var1) {
      WsdlPort var2 = this._getPort(var1);
      return WsdlUtils.getSoapAddress(var2).getLocation();
   }

   public String _getPortBindingType(String var1) {
      WsdlPort var2 = this._getPort(var1);
      return var2.getBinding().getBindingType();
   }

   public boolean _has81Conversation() {
      if (this.webservice == null) {
         return false;
      } else {
         WsdlTypes var1 = this.webservice.getWsdlService().getDefinitions().getTypes();
         if (var1 == null) {
            return false;
         } else {
            SchemaDocument[] var2 = var1.getSchemaArray();
            SchemaDocument[] var3 = var2;
            int var4 = var2.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               SchemaDocument var6 = var3[var5];
               if ("http://www.openuri.org/2002/04/soap/conversation/".equals(var6.getSchema().getTargetNamespace())) {
                  return true;
               }
            }

            return false;
         }
      }
   }

   protected WsdlPort _getPort(String var1) {
      String var2 = this.getWebService().getWsdlService().getName().getNamespaceURI();
      WsdlPort var3 = (WsdlPort)this.getWebService().getWsdlService().getPorts().get(new QName(var2, var1));
      if (var3 == null) {
         throw new JAXRPCException("unable to find port:" + var1 + " This may be because the WSDL file and the generated stub is out " + " sync. Doing clientgen again may fix this problem.");
      } else {
         return var3;
      }
   }

   public Remote getPort(QName var1, Class var2) throws ServiceException {
      if (this.wsdlStatus != 1) {
         throw new ServiceException("This service is not created from a full wsdl,\"Remote getPort(QName port, Class SEI)\" is not allowed");
      } else if (var1 == null) {
         throw new ServiceException("Port name can not be null");
      } else {
         WsdlPort var3 = null;
         this.lazyLoadInternalDDAndService(var2);

         try {
            var3 = this._getPort(var1.getLocalPart());
         } catch (JAXRPCException var5) {
            throwServiceException("Failed to get port", var5);
         }

         return this.createStub(var2, var3);
      }
   }

   private void lazyLoadInternalDDAndService(Class var1) throws ServiceException {
      if (this.clientDeployInfo != null && this.clientDeployInfo.getMappingdd() == null) {
         String var2 = NameUtil.getJAXRPCClassName(this.serviceName.getLocalPart());
         if (var1 == null) {
            throw new JAXRPCException("Port type class can't be null! ");
         }

         String var3 = var1.getPackage().getName().replace(".", "/") + "/" + var2 + "_internaldd.xml";
         this.loadInternalDD(this.clientDeployInfo, var3);

         try {
            WsFactory var4 = WsFactory.instance();
            this.webservice = var4.createClientService(this.clientDeployInfo);
         } catch (WsException var5) {
            if (verbose) {
               Verbose.logException(var5);
            }

            throw new JAXRPCException("Faile to create ws runtime structure " + var5, var5);
         }
      }

   }

   public Remote getPort(Class var1) throws ServiceException {
      if (this.wsdlStatus == 3) {
         throw new ServiceException("This service is not created from a wsdl,\"Remote getPort(Class SEI)\" is not allowed");
      } else {
         this.lazyLoadInternalDDAndService(var1);
         Iterator var2 = this.getWebService().getEndpoints();

         WsEndpoint var3;
         Class var4;
         while(var2.hasNext()) {
            var3 = (WsEndpoint)var2.next();
            var4 = var3.getEndpointInterface();
            if (var4 != null && var1.isAssignableFrom(var4)) {
               return this.createStub(var1, var3);
            }
         }

         var2 = this.getWebService().getEndpoints();

         label49:
         while(var2.hasNext()) {
            var3 = (WsEndpoint)var2.next();
            var4 = var3.getEndpointInterface();
            Method[] var5 = var1.getMethods();
            int var6;
            if (var4 == null) {
               for(var6 = 0; var6 < var5.length; ++var6) {
                  if (var3.getMethod(var5[var6].getName()) == null) {
                     continue label49;
                  }
               }

               return this.createStub(var1, var3);
            } else {
               for(var6 = 0; var6 < var5.length; ++var6) {
                  if (!this.isInClass(var4, var5[var6])) {
                     continue label49;
                  }
               }

               return this.createStub(var1, var3);
            }
         }

         throw new ServiceException("unable to find any port that can implement service endpoint interface " + var1);
      }
   }

   private Remote createStub(Class var1, WsEndpoint var2) throws ServiceException {
      Iterator var3 = this.getWebService().getWsdlService().getPorts().values().iterator();

      WsdlPort var4;
      boolean var5;
      do {
         if (!var3.hasNext()) {
            throw new ServiceException("unable to find any port that use " + var2.getPortType().getName() + " as port type.");
         }

         var4 = (WsdlPort)var3.next();
         var5 = false;
         Iterator var6 = this.getWebService().getPorts();

         while(var6.hasNext()) {
            WsPort var7 = (WsPort)var6.next();
            if (var4.getName().equals(var7.getWsdlPort().getName())) {
               var5 = true;
               break;
            }
         }
      } while(!var5 || var2.getPortType() != var4.getPortType());

      Remote var8 = this.createStub(var1, var4);
      this.checkLinkedPort((Stub)var8, var1);
      return var8;
   }

   private void checkLinkedPort(Stub var1, Class var2) throws ServiceException {
      WsdlPort var3 = (WsdlPort)this.portComponentLinks.get(var2.getName());
      if (var3 != null) {
         SoapAddress var4 = WsdlUtils.getSoapAddress(var3);
         if (var4 == null) {
            throw new ServiceException("wsdl port " + var3 + " doesn't " + "have a soap address extension.");
         }

         WsdlAddressInfo var5 = new WsdlAddressInfo();
         var5.addWsdlPort(var3.getName(), var3.getPortAddress());
         WsdlUtils.updateAddress(var3.getDefinitions(), var5);
         var1._setProperty("javax.xml.rpc.service.endpoint.address", var4.getLocation());
      }

   }

   private Remote createStub(Class var1, WsdlPort var2) {
      Object var3 = (Map)this.portStubProps.get(var2.getName().getLocalPart());
      if (var3 == null) {
         var3 = new HashMap();
      }

      return (Remote)StubImpl.implementInterface(var1, var2, (Map)var3, this);
   }

   private boolean isInClass(Class var1, Method var2) {
      Method[] var3 = var1.getMethods();

      label36:
      for(int var4 = 0; var4 < var3.length; ++var4) {
         if (var2.getName().equals(var3[var4].getName()) && var2.getReturnType().isAssignableFrom(var3[var4].getReturnType())) {
            Class[] var5 = var2.getParameterTypes();
            Class[] var6 = var3[var4].getParameterTypes();
            if (var5.length == var6.length) {
               for(int var7 = 0; var7 < var5.length; ++var7) {
                  if (!var5[var7].isAssignableFrom(var6[var7])) {
                     continue label36;
                  }
               }

               return true;
            }
         }
      }

      return false;
   }

   void _setAllowHandlerChain(boolean var1) {
      this.allowHandlerChain = var1;
   }

   public HandlerRegistry getHandlerRegistry() {
      if (this.allowHandlerChain) {
         return this.handlerRegistry;
      } else {
         throw new UnsupportedOperationException("This operation is not supported  in a JSR 109 based web service");
      }
   }

   public Call createCall() throws ServiceException {
      CallImpl var1 = new CallImpl(this);
      Iterator var2 = this.bareCallProps.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry var3 = (Map.Entry)var2.next();
         var1.setProperty((String)var3.getKey(), var3.getValue());
      }

      return var1;
   }

   public Call createCall(QName var1) throws ServiceException {
      if (this.wsdlStatus != 1) {
         throw new ServiceException("This service is not created from a full wsdl, \"Call createCall(QName port)\" is not allowed.");
      } else if (var1 == null) {
         throw new ServiceException("portName can not be null");
      } else {
         CallImpl var2 = new CallImpl(this);
         WsdlPortBuilder var3 = (WsdlPortBuilder)this.getWebService().getWsdlService().getPorts().get(var1);
         if (var3 == null) {
            throw new ServiceException("Unable to find port " + var1 + " in this web service. Make sure that you have provided " + "the right WSDL url");
         } else {
            var2.setWsdlPort(var3);
            Map var4 = (Map)this.portCallProps.get(var1.getLocalPart());
            if (var4 != null) {
               Iterator var5 = var4.entrySet().iterator();

               while(var5.hasNext()) {
                  Map.Entry var6 = (Map.Entry)var5.next();
                  var2.setProperty((String)var6.getKey(), var6.getValue());
               }
            }

            return var2;
         }
      }
   }

   public Call createCall(QName var1, QName var2) throws ServiceException {
      if (this.wsdlStatus != 1) {
         throw new ServiceException("This service is not created from a full wsdl, \"Call createCall(QName port, QName operation)\" is not allowed.");
      } else {
         Call var3 = this.createCall(var1);
         var3.setOperationName(var2);
         return var3;
      }
   }

   public Call createCall(QName var1, String var2) throws ServiceException {
      if (this.wsdlStatus != 1) {
         throw new ServiceException("This service is not created from a full wsdl, \"Call createCall(QName port, String operation)\" is not allowed.");
      } else {
         String var3 = this.guessOperationNS(var1, var2);
         return this.createCall(var1, new QName(var3, var2));
      }
   }

   private String guessOperationNS(QName var1, String var2) throws ServiceException {
      WsdlPort var3 = (WsdlPort)this.webservice.getWsdlService().getPorts().get(var1);
      WsdlPortType var4 = var3.getPortType();
      Iterator var5 = var4.getOperations().values().iterator();

      WsdlOperation var6;
      do {
         if (!var5.hasNext()) {
            throw new ServiceException("Unable to find operation: " + var2 + " in port " + var4);
         }

         var6 = (WsdlOperation)var5.next();
      } while(!var2.equals(var6.getName().getLocalPart()));

      return var6.getName().getNamespaceURI();
   }

   public Call[] getCalls(QName var1) throws ServiceException {
      if (this.wsdlStatus != 1) {
         throw new ServiceException("This service is not created from a full wsdl, \"Call[] getCalls(QName port)\" is not allowed.");
      } else if (var1 == null) {
         throw new ServiceException("portName can not be null");
      } else {
         ArrayList var2 = new ArrayList();
         WsdlPort var3 = null;

         try {
            var3 = this._getPort(var1.getLocalPart());
         } catch (JAXRPCException var7) {
            throwServiceException("Failed to get port", var7);
         }

         Iterator var4 = var3.getPortType().getOperations().values().iterator();

         while(var4.hasNext()) {
            WsdlOperation var5 = (WsdlOperation)var4.next();
            Call var6 = this.createCall(var1, var5.getName());
            var2.add(var6);
         }

         return (Call[])((Call[])var2.toArray(new Call[var2.size()]));
      }
   }

   public QName getServiceName() {
      if (this.wsdlStatus == 3) {
         throw new JAXRPCException("This service is not created from a wsdl,\"QName getServiceName()\" is not allowed");
      } else {
         return this.serviceName;
      }
   }

   public Iterator getPorts() throws ServiceException {
      if (this.wsdlStatus == 3) {
         throw new ServiceException("This service is not created from a wsdl,\"Iterator getPorts()\" is not allowed");
      } else {
         ArrayList var1 = new ArrayList();
         WsdlService var2 = this.getWebService().getWsdlService();
         Iterator var3 = var2.getPorts().values().iterator();

         while(var3.hasNext()) {
            WsdlPort var4 = (WsdlPort)var3.next();
            var1.add(var4.getName());
         }

         if (var1.size() == 0) {
            throw new ServiceException("Unable to find port in this web service. Make sure that you have provided the right WSDL url");
         } else {
            return var1.iterator();
         }
      }
   }

   public URL getWSDLDocumentLocation() {
      if (this.wsdlStatus == 3) {
         throw new JAXRPCException("This service is not created from a wsdl,\"URL getWSDLDocumentLocation()\" is not allowed");
      } else {
         try {
            return this.wsdlLocation == null ? null : new URL(this.wsdlLocation);
         } catch (IOException var4) {
            try {
               return new URL("file://" + this.wsdlLocation);
            } catch (MalformedURLException var3) {
               throw new JAXRPCException("unable to create URL : " + this.wsdlLocation);
            }
         }
      }
   }

   public TypeMappingRegistry getTypeMappingRegistry() throws JAXRPCException {
      throw new UnsupportedOperationException("This operation is not support in JSR 109 web service implementation");
   }

   /** @deprecated */
   protected void _setUser(String var1, String var2, Object var3) {
      if (var1 == null) {
         throw new IllegalArgumentException("No username provided");
      } else if (var2 == null) {
         throw new IllegalArgumentException("No password provided");
      } else if (var3 != null && var3 instanceof Stub) {
         Stub var4 = (Stub)var3;
         var4._setProperty("javax.xml.rpc.security.auth.username", var1);
         var4._setProperty("javax.xml.rpc.security.auth.password", var2);
      } else {
         throw new IllegalArgumentException("Got a bad stub:" + var3);
      }
   }

   public void loadPolicy(String var1, String var2, InputStream[] var3, InputStream[] var4) throws ServiceException {
      this._loadPolicy(var1, var2, var3, var4);
   }

   protected void _loadPolicy(String var1, String var2, Set<InputStream> var3, Set<InputStream> var4) throws ServiceException {
      if (var3 == var4) {
         InputStream[] var5 = var3 == null ? null : (InputStream[])var3.toArray(new InputStream[0]);
         this._loadPolicy(var1, var2, var5, var5);
      } else {
         this._loadPolicy(var1, var2, var3 == null ? null : (InputStream[])var3.toArray(new InputStream[0]), var4 == null ? null : (InputStream[])var4.toArray(new InputStream[0]));
      }

   }

   protected void _loadPolicy(String var1, String var2, InputStream[] var3, InputStream[] var4) throws ServiceException {
      NormalizedExpression var5;
      NormalizedExpression var6;
      if (var3 == var4) {
         var5 = var6 = getNormalizedPolicies(var1, var3);
      } else {
         checkDuplicatePolicyStreams(var3, var4);
         var5 = getNormalizedPolicies(var1, var3);
         var6 = getNormalizedPolicies(var1, var4);
      }

      this.__loadPolicies(var1, var2, var5, var6);
   }

   protected void _loadPolicy(String var1, InputStream var2, boolean var3, boolean var4) throws ServiceException {
      NormalizedExpression var5 = getNormalizedPolicy(var1, var2);
      this.__loadPolicies(var1, (String)null, var3 ? var5 : null, var4 ? var5 : null);
   }

   private void __loadPolicies(String var1, String var2, NormalizedExpression var3, NormalizedExpression var4) throws ServiceException {
      if (this.webservice == null) {
         throw new ServiceException("Webservice is not yet initialized.");
      } else {
         WsPort var5 = this.webservice.getPort(var1);
         Iterator var7;
         if (var2 != null && !this.isSameWssPolicyVersion(var3, var4, var5)) {
            NormalizedExpression var6 = NormalizedExpression.createEmptyExpression();
            var7 = var5.getEndpoint().getMethods();

            while(var7.hasNext()) {
               setPolicyToMethod((WsMethod)var7.next(), var6, var6);
            }
         }

         this.webservice.setUsingPolicy(true);
         WsEndpoint var9 = var5.getEndpoint();
         if (var2 != null) {
            WsMethod var10 = var9.getMethod(var2);
            if (var10 == null) {
               throw new ServiceException("Can not find operation for " + var2 + " while loading the policy.");
            }

            setPolicyToMethod(var10, var3, var4);
         } else {
            var7 = var5.getEndpoint().getMethods();

            while(var7.hasNext()) {
               setPolicyToMethod((WsMethod)var7.next(), var3, var4);
            }
         }

         try {
            if (!this.policyListenerLoaded) {
               WsFactory.instance().loadPolicy(this.webservice);
               this.policyListenerLoaded = true;
            }

         } catch (WsException var8) {
            var8.printStackTrace();
            throw new ServiceException(var8.getMessage());
         }
      }
   }

   private boolean isSameWssPolicyVersion(NormalizedExpression var1, NormalizedExpression var2, WsPort var3) {
      if (this.webservice.isUsingPolicy()) {
         boolean var4 = var1 != null ? SecurityPolicyAssertionInfoFactory.hasSecurityPolicy(var1) : (var2 != null ? SecurityPolicyAssertionInfoFactory.hasSecurityPolicy(var2) : false);
         boolean var5 = var1 != null ? SecurityPolicyAssertionFactory.hasSecurityPolicy(var1) : (var2 != null ? SecurityPolicyAssertionFactory.hasSecurityPolicy(var2) : false);
         Iterator var6 = var3.getEndpoint().getMethods();

         while(var6.hasNext()) {
            WsMethod var7 = (WsMethod)var6.next();
            NormalizedExpression var8 = var7.getCachedEffectiveInboundPolicy();
            NormalizedExpression var9 = var7.getCachedEffectiveOutboundPolicy();
            boolean var10 = var8 != null ? SecurityPolicyAssertionInfoFactory.hasSecurityPolicy(var8) : (var9 != null ? SecurityPolicyAssertionInfoFactory.hasSecurityPolicy(var9) : false);
            boolean var11 = var8 != null ? SecurityPolicyAssertionFactory.hasSecurityPolicy(var8) : (var9 != null ? SecurityPolicyAssertionFactory.hasSecurityPolicy(var9) : false);
            if (var4 && var11 || var5 && var10) {
               return false;
            }
         }
      }

      return true;
   }

   private static NormalizedExpression getNormalizedPolicies(String var0, InputStream[] var1) throws ServiceException {
      NormalizedExpression var2 = null;
      if (var1 != null && var1.length > 0) {
         var2 = getNormalizedPolicy(var0, var1[0]);

         for(int var3 = 1; var3 < var1.length; ++var3) {
            var2 = PolicyMath.merge(var2, getNormalizedPolicy(var0, var1[var3]));
         }
      }

      return var2;
   }

   private static NormalizedExpression getNormalizedPolicy(String var0, InputStream var1) throws ServiceException {
      PolicyStatement var2 = null;

      try {
         var2 = PolicyFinder.readPolicyFromStream(var0, var1, true);
         if (SecurityPolicyCustomizer.isSecurityPolicyAbstract(var0, var2)) {
            throw new ServiceException("Abstract policy can not be attached to client dynamically.");
         } else {
            return var2.normalize();
         }
      } catch (PolicyException var4) {
         var4.printStackTrace();
         throw new ServiceException(var4);
      }
   }

   private static void setPolicyToMethod(WsMethod var0, NormalizedExpression var1, NormalizedExpression var2) throws ServiceException {
      boolean var3 = false;
      if (var1 != null) {
         var0.setCachedEffectiveInboundPolicy(var1);
         var3 = true;
      }

      if (var2 != null) {
         var0.setCachedEffectiveOutboundPolicy(var2);
         var3 = true;
      }

      if (!var3) {
         throw new ServiceException("Both inbound and outbound polcies are null.");
      }
   }

   private static void checkDuplicatePolicyStreams(InputStream[] var0, InputStream[] var1) throws ServiceException {
      if (var0 != null && var1 != null) {
         int var2 = Math.min(var0.length, var1.length);

         for(int var3 = 0; var3 < var2; ++var3) {
            if (var0[var3] == var1[var3]) {
               throw new ServiceException("Can not use the same input stream for both inbound and outbound policy");
            }
         }
      }

   }

   public WebServiceContext context() {
      throw new Error("NYI");
   }

   public WebServiceContext joinContext() throws ContextNotFoundException {
      throw new Error("NYI");
   }

   public Dispatch createDispatch(QName var1) throws ServiceException {
      return new DispatchImpl(var1, this);
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("ServiceImpl[\n");
      var1.append(this.getWebService());
      var1.append("]\n");
      return var1.toString();
   }
}
