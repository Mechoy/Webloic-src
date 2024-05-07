package weblogic.wsee.deploy;

import com.sun.xml.ws.api.server.InstanceResolver;
import com.sun.xml.ws.transport.http.servlet.ServletAdapterList;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.jws.WebService;
import javax.servlet.ServletContext;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceProvider;
import weblogic.application.ApplicationContext;
import weblogic.application.ApplicationContextInternal;
import weblogic.ejb.spi.DynamicEJBModule;
import weblogic.j2ee.ComponentRuntimeMBeanImpl;
import weblogic.j2ee.J2EEApplicationRuntimeMBeanImpl;
import weblogic.j2ee.descriptor.JavaWsdlMappingBean;
import weblogic.j2ee.descriptor.PortComponentBean;
import weblogic.j2ee.descriptor.wl.WebservicePolicyRefBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.wsee.WebServiceType;
import weblogic.wsee.async.AsyncUtil;
import weblogic.wsee.buffer.BufferManager;
import weblogic.wsee.jaxws.handler.BindingIdTranslator;
import weblogic.wsee.jaxws.handler.ServerHandlerChainsResolver;
import weblogic.wsee.jaxws.injection.WSEEComponentContributor;
import weblogic.wsee.jaxws.tubeline.standard.ClientContainerUtil;
import weblogic.wsee.monitoring.WseeRuntimeMBeanManager;
import weblogic.wsee.policy.deployment.WsdlPolicySubject;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.security.policy.WssPolicyContext;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsException;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.WsRegistry;
import weblogic.wsee.ws.WsService;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlImport;
import weblogic.wsee.wsdl.WsdlPort;

public abstract class DeployInfo {
   static boolean verbose = Verbose.isVerbose(DeployInfo.class);
   public static final String CATALOG = "jax-ws-catalog.xml";
   private JavaWsdlMappingBean mappingdd;
   private WsdlDefinitions wsdlDef;
   private PortComponentBean portComp;
   private WebservicePolicyRefBean wpr;
   private weblogic.j2ee.descriptor.wl.PortComponentBean wlPortComp;
   private Class jwsClass;
   private WsdlPolicySubject policySubject;
   private String wssConfigMBeanName;
   private WssPolicyContext wssPolicyCtx;
   private List<DynamicEJBModule> dynamicEjbs;
   private List<String> bufferTargetURIs;
   private ApplicationContextInternal applicationCtx;
   private String contextPath = null;
   private String application = null;
   private String securityRealmName = null;
   private String serviceName;
   private String wdName = null;
   private WebServiceType webServicesType = null;
   private String version = null;
   protected String linkName = null;
   protected String moudleName = null;
   protected ServletContext servletContext;
   protected String[] serviceURIs;
   private ServletAdapterList adpaterList;
   private ServerHandlerChainsResolver handlerChainsResolver;

   protected DeployInfo() {
   }

   WebServiceType getWebServicesType() {
      if (this.webServicesType != null) {
         return this.webServicesType;
      } else {
         return this.getJwsClass().getAnnotation(WebService.class) == null && this.getJwsClass().getAnnotation(WebServiceProvider.class) == null ? WebServiceType.JAXRPC : WebServiceType.JAXWS;
      }
   }

   void setWebServicesType(WebServiceType var1) {
      this.webServicesType = var1;
   }

   public List<DynamicEJBModule> getDynamicEjbs() {
      return this.dynamicEjbs;
   }

   public void setDynamicEjbs(List<DynamicEJBModule> var1) {
      this.dynamicEjbs = var1;
   }

   public List<String> getBufferTargetURIs() {
      return this.bufferTargetURIs;
   }

   public void setBufferTargetURIs(List<String> var1) {
      this.bufferTargetURIs = var1;
   }

   public JavaWsdlMappingBean getMappingdd() {
      return this.mappingdd;
   }

   void setMappingdd(JavaWsdlMappingBean var1) {
      this.mappingdd = var1;
   }

   public PortComponentBean getPortComp() {
      return this.portComp;
   }

   void setPortComp(PortComponentBean var1) {
      this.portComp = var1;
   }

   public WebservicePolicyRefBean getPolicyRef() {
      return this.wpr;
   }

   void setPolicyRef(WebservicePolicyRefBean var1) {
      this.wpr = var1;
   }

   public weblogic.j2ee.descriptor.wl.PortComponentBean getWlPortComp() {
      return this.wlPortComp;
   }

   void setWlPortComp(weblogic.j2ee.descriptor.wl.PortComponentBean var1) {
      this.wlPortComp = var1;
   }

   public WsdlDefinitions getWsdlDef() {
      return this.wsdlDef;
   }

   void setWsdlDef(WsdlDefinitions var1) {
      this.wsdlDef = var1;
      if (var1 != null) {
         this.policySubject = new WsdlPolicySubject(var1);
      }

   }

   void setJwsClass(Class var1) {
      this.jwsClass = var1;
   }

   public Class getJwsClass() {
      return this.jwsClass;
   }

   public RuntimeMBean getParentRuntimeMBean() {
      return this.applicationCtx.getRuntime();
   }

   public void setWssConfigMBeanName(String var1) {
      this.wssConfigMBeanName = var1;
   }

   String getWssConfigMBeanName() {
      return this.wssConfigMBeanName;
   }

   void setWssPolicyContext(WssPolicyContext var1) {
      this.wssPolicyCtx = var1;
      if (this.policySubject != null) {
         this.wssPolicyCtx.getPolicyServer().addPolicies(this.policySubject.getPolicies());
      }

   }

   public WssPolicyContext getWssPolicyContext() {
      return this.wssPolicyCtx;
   }

   public String getContextPath() {
      if (this.contextPath == null) {
         if (this.servletContext instanceof WebAppServletContext) {
            return ((WebAppServletContext)this.servletContext).getContextPath();
         } else {
            throw new AssertionError("Unable to determine ContextPath from ServletContext");
         }
      } else {
         return this.contextPath;
      }
   }

   void setContextPath(String var1) {
      this.contextPath = var1;
   }

   public String getApplication() {
      return this.application;
   }

   void setApplication(String var1) {
      this.application = var1;
   }

   public String getSecurityRealmName() {
      return this.securityRealmName;
   }

   void setSecurityRealmName(String var1) {
      this.securityRealmName = var1;
   }

   void setServiceName(String var1) {
      this.serviceName = var1;
   }

   public String getServiceName() {
      return this.serviceName;
   }

   void setWebServiceDescriptionName(String var1) {
      this.wdName = var1;
   }

   public String getWebserviceDescriptionName() {
      return this.wdName;
   }

   void setApplicationContext(ApplicationContext var1) {
      if (var1 instanceof ApplicationContextInternal) {
         this.applicationCtx = (ApplicationContextInternal)var1;
      }

   }

   public ApplicationContextInternal getApplicationContext() {
      return this.applicationCtx;
   }

   public WsdlPolicySubject gePolicySubject() {
      return this.policySubject;
   }

   public NormalizedExpression getEndpointPolicy() throws PolicyException {
      QName var1 = new QName(this.wsdlDef.getTargetNamespace(), this.serviceName);
      WsdlPort var2 = (WsdlPort)this.wsdlDef.getPorts().get(var1);
      if (var2 == null) {
         var2 = this.getPortFromImportedDefinition(this.wsdlDef, this.wsdlDef.getImports(), this.serviceName);
      }

      return this.wssPolicyCtx.getPolicyServer().getEndpointPolicy(var2);
   }

   private WsdlPort getPortFromImportedDefinition(WsdlDefinitions var1, List<? extends WsdlImport> var2, String var3) throws PolicyException {
      Iterator var4 = var2.iterator();

      WsdlPort var7;
      do {
         if (!var4.hasNext()) {
            throw new PolicyException("Fail to find port(" + var3 + ") from wsdl " + var1.getWsdlLocation());
         }

         WsdlImport var5 = (WsdlImport)var4.next();
         QName var6 = new QName(var5.getNamespace(), var3);
         var7 = (WsdlPort)var1.getPorts().get(var6);
      } while(var7 == null);

      return var7;
   }

   public String[] getServiceURIs() {
      return this.serviceURIs;
   }

   abstract WsService createWsService() throws WsException;

   public String getVersion() {
      return this.version;
   }

   public void setVersion(String var1) {
      this.version = var1;
   }

   public ServletAdapterList getAdpaterList() {
      return this.adpaterList;
   }

   public void setAdpaterList(ServletAdapterList var1) {
      this.adpaterList = var1;
   }

   public void validate() throws WsException {
      if (verbose) {
         Verbose.log((Object)this.toString());
      }

      if (this.serviceURIs == null) {
         throw new WsException("No Service URI specifed.");
      } else if (this.getJwsClass().getPackage() == null) {
         throw new WsException("A jws [" + this.getJwsClass() + "] should have a package declaration.");
      } else {
         if (this.getWebServicesType() == WebServiceType.JAXRPC) {
            if (this.mappingdd == null) {
               throw new WsException("Required JAXRPC mapping file not found.");
            }

            if (this.wsdlDef == null) {
               throw new WsException("Required WSDL file entry in webservices.xml not found.");
            }
         }

      }
   }

   public WsPort createWsPort() throws WsException {
      this.validate();
      WsService var1 = this.createWsService();
      WsPort var2 = (WsPort)var1.getPorts().next();
      WsRegistry var3 = WsRegistry.instance();
      String[] var4 = this.getServiceURIs();
      StringBuilder var5 = new StringBuilder();

      for(int var6 = 0; var6 < var4.length; ++var6) {
         String var7 = AsyncUtil.calculateServiceTargetURI(this.getContextPath(), var4[var6]);
         var3.unregisterMBean(var7, this.getVersion());
         var3.register(var7, this.getVersion(), var2);
         var5.append(var7);
         if (var6 + 1 < var4.length) {
            var5.append(";");
         }
      }

      String var8 = var5.toString();
      this.createMBean(var1, var8, this.getApplication(), this.getVersion());
      return var2;
   }

   public String getUri() {
      String[] var1 = this.getServiceURIs();
      StringBuilder var2 = new StringBuilder();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2.append(this.getContextPath());
         var2.append(var1[var3]);
         if (var3 + 1 < var1.length) {
            var2.append(";");
         }
      }

      return var2.toString();
   }

   private void createMBean(WsService var1, String var2, String var3, String var4) {
      try {
         WebAppServletContext var5 = (WebAppServletContext)this.servletContext;
         ApplicationContextInternal var6 = var5.getApplicationContext();
         J2EEApplicationRuntimeMBeanImpl var7 = var6.getRuntime();
         String var8 = this.getModuleName();
         ComponentRuntimeMBeanImpl var9 = ClientContainerUtil.getContainingComponentRuntimeByModuleName(var8);
         WseeRuntimeMBeanManager.createJaxRpcMBean(var7, var9, var1, this, this.getWebserviceDescriptionName(), var2, var3, var4);
      } catch (Throwable var10) {
         Verbose.log((Object)var10);
      }

   }

   public void clean() {
      Iterator var1;
      if (this.getDynamicEjbs() != null) {
         var1 = this.getDynamicEjbs().iterator();

         while(var1.hasNext()) {
            DynamicEJBModule var2 = (DynamicEJBModule)var1.next();
            var2.undeployDynamicEJB();
         }

         if (verbose) {
            Verbose.log((Object)"Undeployed dynamic MDBs");
         }
      }

      if (this.getBufferTargetURIs() != null) {
         var1 = this.getBufferTargetURIs().iterator();

         while(var1.hasNext()) {
            String var9 = (String)var1.next();
            if (verbose) {
               Verbose.log((Object)("Removed listener at: " + var9));
            }

            BufferManager.instance().removeMessageListener(var9);
            BufferManager.instance().removeErrorListener(var9);
            BufferManager.instance().removeRetryDelay(var9);
         }
      }

      WssPolicyContext var8 = this.getWssPolicyContext();
      if (var8 != null) {
         var8.getWssConfiguration().destroy();
      }

      String[] var10 = this.getServiceURIs();
      if (var10 != null) {
         WsRegistry var3 = WsRegistry.instance();
         String[] var4 = var10;
         int var5 = var10.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String var7 = var4[var6];
            var3.unregister(this.getContextPath() + var7, this.getVersion());
         }
      }

   }

   public void setServletContext(ServletContext var1) {
      this.servletContext = var1;
   }

   public ServletContext getServletContext() {
      return this.servletContext;
   }

   public URL getCatalog() throws MalformedURLException {
      return this.getResource("/jax-ws-catalog.xml");
   }

   public URL getResource(String var1) throws MalformedURLException {
      return this.servletContext.getResource(var1);
   }

   public String getLinkName() {
      return this.linkName;
   }

   public void setLinkName(String var1) {
      this.linkName = var1;
   }

   abstract String getServlet();

   abstract ServletAdapterList createServletAdapterList();

   public abstract InstanceResolver createInstanceResolver();

   public ServerHandlerChainsResolver createServerHandlerChainsResolver() {
      if (this.handlerChainsResolver == null) {
         PortComponentBean var1 = this.getPortComp();
         this.handlerChainsResolver = new ServerHandlerChainsResolver(var1.getWsdlService(), var1.getWsdlPort(), this.getProtocolBinding(), var1.getHandlerChains());
      }

      return this.handlerChainsResolver;
   }

   public String getProtocolBinding() {
      String var1 = this.getPortComp().getProtocolBinding();
      var1 = BindingIdTranslator.translate(var1);
      if (StringUtil.isEmpty(var1)) {
         var1 = "http://schemas.xmlsoap.org/wsdl/soap/http";
      }

      return var1;
   }

   public abstract WSEEComponentContributor loadComponentContributor();

   public String getModuleName() {
      return this.moudleName;
   }

   void setModuleName(String var1) {
      this.moudleName = var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("DeployInfo: \n");
      var1.append("  jwsClass=" + this.jwsClass + "\n");
      var1.append("  webServicesType=" + this.getWebServicesType() + "\n");
      var1.append("  application=" + this.application + "\n");
      var1.append("  version=" + this.version + "\n");
      var1.append("  contextPath=" + this.getContextPath() + "\n");
      var1.append("  servletContext=" + this.servletContext + "\n");
      var1.append("  serviceName=" + this.serviceName + "\n");
      var1.append("  serviceURIs=" + Arrays.toString(this.serviceURIs) + "\n");
      var1.append("  wsdlDef=" + this.wsdlDef + "\n");
      var1.append("  mappingdd=" + this.mappingdd + "\n");
      var1.append("  portComp=" + this.portComp + "\n");
      var1.append("  wlPortComp=" + this.wlPortComp + "\n");
      var1.append("  policySubject=" + this.policySubject + "\n");
      var1.append("  wssConfigMBeanName=" + this.wssConfigMBeanName + "\n");
      var1.append("  securityRealmName=" + this.securityRealmName + "\n");
      var1.append("  wssPolicyCtx=" + this.wssPolicyCtx + "\n");
      var1.append("  dynamicEjbs=" + this.dynamicEjbs + "\n");
      var1.append("  bufferTargetURIs=" + this.bufferTargetURIs + "\n");
      var1.append("  linkName=" + this.linkName + "\n");
      var1.append("  componentName=" + this.getModuleName() + "\n");
      var1.append("  applicationCtx=" + this.applicationCtx + "\n\n");
      return var1.toString();
   }
}
