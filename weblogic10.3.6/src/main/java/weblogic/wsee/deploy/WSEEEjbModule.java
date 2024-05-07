package weblogic.wsee.deploy;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.xml.stream.XMLStreamException;
import weblogic.application.ApplicationContext;
import weblogic.application.ModuleException;
import weblogic.ejb.spi.BeanInfo;
import weblogic.ejb.spi.DeploymentInfo;
import weblogic.ejb.spi.EJBDeploymentException;
import weblogic.ejb.spi.EjbDescriptorBean;
import weblogic.ejb.spi.SessionBeanInfo;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.ejb.spi.WSObjectFactory;
import weblogic.j2ee.descriptor.EnvEntryBean;
import weblogic.j2ee.descriptor.JavaWsdlMappingBean;
import weblogic.j2ee.descriptor.PortComponentBean;
import weblogic.j2ee.descriptor.ServiceImplBeanBean;
import weblogic.j2ee.descriptor.SessionBeanBean;
import weblogic.j2ee.descriptor.WebserviceDescriptionBean;
import weblogic.j2ee.descriptor.WebservicesBean;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.WebserviceAddressBean;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.wsee.async.AsyncResponseBean;
import weblogic.wsee.jws.container.JWSSessionBean;
import weblogic.wsee.policy.deployment.WsPolicyDescriptor;
import weblogic.wsee.policy.runtime.PolicyFinder;
import weblogic.wsee.policy.runtime.PolicyServer;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsException;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlFactory;
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.wsee.wsdl.WsdlUtils;

public class WSEEEjbModule extends WSEEModule {
   public static final String EJB_WRAPPER_LEGACY_DEPLOY_MSG = "9.0 and 9.1 compliled WLS Web Services need to be recompiled for deployment \n if the JWS contains any of the following annotations.\n\nweblogic.jws.Conversation\nweblogic.jws.Conversational\nweblogic.jws.Context\nweblogic.jws.Callback\nweblogic.jws.ServiceClient\norg.apache.beehive.controls.api.bean.Control\nPlease note that the defaults for weblogic.jws.Conversational.runAsStartUser and weblogic.jws.Conversational.singlePrincipal have changed to false.\n";
   private String moduleName = null;
   private DeploymentInfo ejbDeploymentInfo = null;
   private EjbDescriptorBean ejbDescriptor = null;
   private List<WseeWebappModule> wseeWebappModules = new LinkedList();

   WSEEEjbModule(String var1, ApplicationContext var2) {
      super(var2);
      this.moduleName = var1;
   }

   DeployInfo createDeployInfo() {
      return new EJBDeployInfo();
   }

   String getModuleName() {
      return this.moduleName;
   }

   void setEjbDeploymentInfo(DeploymentInfo var1) {
      this.ejbDeploymentInfo = var1;
   }

   void setEjbDescriptorBean(EjbDescriptorBean var1) {
      this.ejbDescriptor = var1;
   }

   WSEEDescriptor loadDescriptor(File var1, DeploymentPlanBean var2) throws IOException, XMLStreamException {
      return new WSEEDescriptor(this.ejbDeploymentInfo.getVirtualJarFile(), var1, var2, this.moduleName);
   }

   WsPolicyDescriptor loadWsPolicyDescriptor(File var1, DeploymentPlanBean var2) throws IOException, XMLStreamException {
      return new WsPolicyDescriptor(this.ejbDeploymentInfo.getVirtualJarFile(), var1, var2, this.moduleName);
   }

   protected Map<String, Class> getLinkMap() {
      HashMap var1 = new HashMap();
      Iterator var2 = this.ejbDeploymentInfo.getBeanInfos().iterator();

      while(var2.hasNext()) {
         BeanInfo var3 = (BeanInfo)var2.next();
         if (var3 instanceof SessionBeanInfo) {
            SessionBeanInfo var4 = (SessionBeanInfo)var3;
            if (!var4.isStateful()) {
               var1.put(var4.getEJBName(), var4.getBeanClass());
            }
         }
      }

      return var1;
   }

   protected String getLinkName(ServiceImplBeanBean var1) {
      return var1.getEjbLink();
   }

   protected void setLinkName(ServiceImplBeanBean var1, String var2) {
      var1.setEjbLink(var2);
   }

   protected EnvEntryBean[] getEnvEntries(ServiceImplBeanBean var1) {
      SessionBeanBean[] var2 = this.ejbDescriptor.getEjbJarBean().getEnterpriseBeans().getSessions();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var2[var3].getEjbName().equals(var1.getEjbLink())) {
            return var2[var3].getEnvEntries();
         }
      }

      return null;
   }

   WsdlDefinitions loadWsdlDefinitions(String var1) throws WsException {
      URL var2 = this.ejbDeploymentInfo.getVirtualJarFile().getResource(var1);
      if (var2 == null && !var1.toUpperCase(Locale.ENGLISH).startsWith("META-INF")) {
         var2 = this.ejbDeploymentInfo.getVirtualJarFile().getResource("META-INF" + (var1.startsWith("/") ? "" : "/") + var1);
      }

      if (var2 == null && var1.startsWith("/")) {
         var1 = var1.substring(1);
         var2 = this.ejbDeploymentInfo.getVirtualJarFile().getResource(var1);
      }

      if (var2 == null) {
         throw new WsException("wsdl \"" + var1 + "\" is not found in the deployment " + this.ejbDeploymentInfo.getVirtualJarFile());
      } else {
         try {
            return WsdlFactory.getInstance().parse(var2.toString());
         } catch (WsdlException var4) {
            throw new WsException(var4.getMessage(), var4);
         }
      }
   }

   JavaWsdlMappingBean loadMappingFile(String var1) throws WsException {
      File var2 = null;
      DeploymentPlanBean var3 = null;
      if (this.appCtx != null) {
         AppDeploymentMBean var4 = this.appCtx.getAppDeploymentMBean();
         var3 = var4.getDeploymentPlanDescriptor();
         if (var4.getPlanDir() != null) {
            var2 = new File(var4.getLocalPlanDir());
         }
      }

      JavaWsdlMappingDescriptor var7 = new JavaWsdlMappingDescriptor(this.ejbDeploymentInfo.getVirtualJarFile(), var2, var3, this.moduleName, var1);

      try {
         return var7.getJavaWsdlMappingBean();
      } catch (Exception var6) {
         throw new WsException("Failed to load Jaxrp mapping file \"" + var1 + "\" " + "in " + this.ejbDeploymentInfo.getVirtualJarFile() + " " + var6, var6);
      }
   }

   void loadModulePolicies(PolicyServer var1) throws Exception {
      PolicyFinder.loadPolicies(this.ejbDeploymentInfo.getVirtualJarFile(), var1);
   }

   void registerEndpoint(WebservicesBean var1) throws Exception {
      this.verifyEjbDD();
      WebserviceDescriptionBean[] var2 = var1.getWebserviceDescriptions();
      WebserviceDescriptionBean[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         WebserviceDescriptionBean var6 = var3[var5];
         WsdlAddressInfo var7 = new WsdlAddressInfo();
         PortComponentBean[] var8 = var6.getPortComponents();
         WsdlDefinitions var9 = null;
         PortComponentBean[] var10 = var8;
         int var11 = var8.length;

         for(int var12 = 0; var12 < var11; ++var12) {
            PortComponentBean var13 = var10[var12];
            WsdlAddressInfo.PortAddress var14 = var7.addWsdlPort(var13.getWsdlPort());
            String var15 = var13.getServiceImplBean().getEjbLink();
            EJBDeployInfo var16 = (EJBDeployInfo)this.deployInfoMap.get(var15);
            String var17 = null;
            if (var13.getWsdlService() != null) {
               var17 = var13.getWsdlService().getLocalPart();
            }

            this.registerContext(var16, var14, var17);
            var9 = var16.getWsdlDef();
            if (var9 != null) {
               this.bindInternalPort(var16, var13, (WsdlPort)var9.getPorts().get(var13.getWsdlPort()));
            }
         }

         if (var9 != null) {
            WsdlUtils.updateAddress(var9, var7);
         }
      }

   }

   private void verifyEjbDD() throws EJBDeploymentException {
      HashSet var1 = new HashSet();
      var1.addAll(this.deployInfoMap.keySet());
      Iterator var2 = this.ejbDeploymentInfo.getBeanInfos().iterator();

      while(var2.hasNext()) {
         BeanInfo var3 = (BeanInfo)var2.next();
         if (var3 instanceof SessionBeanInfo) {
            SessionBeanInfo var4 = (SessionBeanInfo)var3;
            String var5 = var4.getEJBName();
            if (var4.getBeanClass().getSuperclass().equals(JWSSessionBean.class)) {
               throw new EJBDeploymentException(var5, this.moduleName, new WLDeploymentException("9.0 and 9.1 compliled WLS Web Services need to be recompiled for deployment \n if the JWS contains any of the following annotations.\n\nweblogic.jws.Conversation\nweblogic.jws.Conversational\nweblogic.jws.Context\nweblogic.jws.Callback\nweblogic.jws.ServiceClient\norg.apache.beehive.controls.api.bean.Control\nPlease note that the defaults for weblogic.jws.Conversational.runAsStartUser and weblogic.jws.Conversational.singlePrincipal have changed to false.\n"));
            }

            WSObjectFactory var6 = var4.getWSObjectFactory();
            if (var6 == null) {
               if (var1.contains(var5)) {
                  throw new EJBDeploymentException(var5, this.moduleName, new WLDeploymentException("ejb \"" + var5 + "\" " + "doesn't have a web service view in ejb-jar.xml."));
               }
            } else {
               assert !var4.isStateful();

               if (var1.size() == 0) {
                  throw new EJBDeploymentException(var5, this.moduleName, new WLDeploymentException("ejb \"" + var5 + "\" has a web service view in ejb-jar.xml, but webservices.xml " + "file is not found."));
               }

               EJBDeployInfo var7 = (EJBDeployInfo)this.deployInfoMap.get(var5);
               if (var7 == null) {
                  throw new EJBDeploymentException(var5, this.moduleName, new WLDeploymentException("ejb \"" + var5 + "\" " + "is not defined as web service in webservices.xml."));
               }

               var7.setBeanFactory(var6);
               var7.setJwsClass(var4.getBeanClass());
               var7.setEJBInfo(var4);
               var1.remove(var5);
            }
         }
      }

      if (var1.size() != 0) {
         throw new EJBDeploymentException(this.moduleName, this.moduleName, new WLDeploymentException("<ejb-link> \"" + var1 + "\" " + "defined in webservices.xml don't exist or don't have a valid " + "web service view in ejb-jar.xml."));
      }
   }

   private void bindInternalPort(EJBDeployInfo var1, PortComponentBean var2, WsdlPort var3) throws EJBDeploymentException {
      String var4 = "wsee/" + this.moduleName + "#" + var2.getPortComponentName();

      try {
         Context var5 = this.appCtx.getEnvContext();
         var5.bind(var4, var3);
      } catch (NamingException var6) {
         throw new EJBDeploymentException(var1.getEjbName(), this.moduleName, new WsException("Failed to bind wsdl port to internal name " + var4 + " " + var6, var6));
      }
   }

   private void registerContext(EJBDeployInfo var1, WsdlAddressInfo.PortAddress var2, String var3) throws EJBDeploymentException {
      String var4 = null;
      String var5 = null;
      weblogic.j2ee.descriptor.wl.PortComponentBean var6 = var1.getWlPortComp();
      if (var6 != null) {
         WebserviceAddressBean var7 = var6.getServiceEndpointAddress();
         if (var7 != null) {
            var4 = var7.getWebserviceContextpath();
            var5 = var7.getWebserviceServiceuri();
         }
      }

      if (var4 == null) {
         var4 = var1.getContextPathFromWsdl();
      } else if (var1.getJwsClass() != null && var1.getJwsClass().getName().equals(AsyncResponseBean.class.getName())) {
         try {
            WSEEProperties var10 = new WSEEProperties(this.ejbDeploymentInfo.getModuleClassLoader());
            var4 = var10.getNormalizedProperty("AsyncResponseBean.contextPath", var4);
            var5 = var10.getNormalizedProperty("AsyncResponseBean.serviceUri", var5);
         } catch (IOException var9) {
            throw new EJBDeploymentException(var1.getEjbName(), this.moduleName, var9);
         }
      }

      if (var4 == null) {
         throw new EJBDeploymentException(var1.getEjbName(), this.moduleName, new WLDeploymentException("please specify contextpath and serviceURI information using weblogic-webservices.xml."));
      } else {
         if (!var4.startsWith("/")) {
            var4 = "/" + var4;
         }

         if (var4.endsWith("/")) {
            var4 = var4.substring(0, var4.length() - 1);
         }

         var1.setContextPath(var4);
         var1.setApplication(this.ejbDeploymentInfo.getApplicationId());
         var1.setSecurityRealmName(this.ejbDeploymentInfo.getSecurityRealmName());
         String var11;
         if (var5 == null) {
            if (var3 != null) {
               var5 = "/" + var3;
               var11 = var4 + var5;
               var1.setServiceURIs(new String[]{var5, "/"});
            } else {
               var5 = "/";
               var11 = var4;
               var1.setServiceURI(var5);
            }
         } else {
            if (!var5.startsWith("/")) {
               var5 = "/" + var5;
            }

            var11 = var4 + var5;
            var1.setServiceURI(var5);
         }

         if (verbose) {
            Verbose.banner("Deploy Web Service EJB: " + var1.getEjbName());
            Verbose.log((Object)("context path:" + var4));
            Verbose.log((Object)("service URI:" + var5));
         }

         WseeWebappModule var8 = new WseeWebappModule(this.appCtx, this.getModuleName(), var4, var11, var1, var6, this.ejbDeploymentInfo);
         this.wseeWebappModules.add(var8);
         if (this.sslRequired(var6)) {
            var2.setProtocol("https");
         } else {
            var2.setProtocol("http");
         }

         var2.setServiceuri(var11);
      }
   }

   private boolean sslRequired(weblogic.j2ee.descriptor.wl.PortComponentBean var1) {
      if (var1 != null) {
         String var2 = var1.getTransportGuarantee();
         if (var2 != null) {
            var2 = var2.trim();
            if ("INTEGRAL".equals(var2) || "CONFIDENTIAL".equals(var2)) {
               return true;
            }
         }
      }

      return false;
   }

   void activate() throws EJBDeploymentException {
      try {
         Iterator var1 = this.wseeWebappModules.iterator();

         while(var1.hasNext()) {
            WseeWebappModule var2 = (WseeWebappModule)var1.next();
            var2.activate();
         }

      } catch (DeploymentException var3) {
         throw new EJBDeploymentException(this.moduleName, this.moduleName, var3);
      }
   }

   void deactivate() {
      try {
         Iterator var1 = this.wseeWebappModules.iterator();

         while(var1.hasNext()) {
            WseeWebappModule var2 = (WseeWebappModule)var1.next();
            var2.deactivate();
         }
      } catch (ModuleException var3) {
         var3.printStackTrace();
      }

   }

   void destroy() throws DeploymentException {
      try {
         Iterator var1 = this.wseeWebappModules.iterator();

         while(var1.hasNext()) {
            WseeWebappModule var2 = (WseeWebappModule)var1.next();
            var2.removeWebApp();
         }
      } catch (ModuleException var3) {
         var3.printStackTrace();
      }

      this.wseeWebappModules.clear();
      super.destroy();
   }

   protected ClassLoader getClassLoader() {
      return this.ejbDeploymentInfo.getModuleClassLoader();
   }
}
