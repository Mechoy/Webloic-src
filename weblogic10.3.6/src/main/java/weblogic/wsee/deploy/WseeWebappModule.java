package weblogic.wsee.deploy;

import java.util.HashMap;
import javax.servlet.ServletContext;
import weblogic.application.ApplicationContext;
import weblogic.application.ModuleException;
import weblogic.application.UpdateListener;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorManager;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.spi.DeploymentInfo;
import weblogic.ejb.spi.EJBDeploymentException;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.j2ee.descriptor.LoginConfigBean;
import weblogic.j2ee.descriptor.ParamValueBean;
import weblogic.j2ee.descriptor.SecurityConstraintBean;
import weblogic.j2ee.descriptor.ServletBean;
import weblogic.j2ee.descriptor.ServletMappingBean;
import weblogic.j2ee.descriptor.UserDataConstraintBean;
import weblogic.j2ee.descriptor.WebAppBean;
import weblogic.j2ee.descriptor.WebResourceCollectionBean;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.PortComponentBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.servlet.internal.HttpServer;
import weblogic.servlet.internal.WebAppModule;
import weblogic.servlet.internal.WebAppParser;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.servlet.internal.WebService;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.jars.VirtualJarFile;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsRegistry;

class WseeWebappModule {
   static boolean verbose = Verbose.isVerbose(WseeWebappModule.class);
   private static final String SERVLET_NAME = "WSEE_SERVLET";
   private ApplicationContext appCtx;
   private VirtualWebApp virtualWebApp;
   private String contextPath;
   private String serviceUrl;
   private EJBDeployInfo deployInfo;
   private String version;

   WseeWebappModule(ApplicationContext var1, String var2, String var3, String var4, EJBDeployInfo var5, PortComponentBean var6, DeploymentInfo var7) throws EJBDeploymentException {
      this.appCtx = var1;
      this.contextPath = var3;
      this.serviceUrl = var4;
      this.deployInfo = var5;
      HttpServer var8 = WebService.defaultHttpServer();
      synchronized(var1) {
         WebAppServletContext var10 = var8.getServletContextManager().getContextForContextPath(var3, var1.getAppDeploymentMBean().getVersionIdentifier());
         if (var10 != null) {
            WebAppServletContext var11 = (WebAppServletContext)var10;
            if (var1 != var11.getApplicationContext()) {
               throw new EJBDeploymentException(this.deployInfo.getEjbName(), var2, new WLDeploymentException("contextpath " + var3 + " is " + "used in other web application. Please specify a different one."));
            }

            if (hasSecurity(var6)) {
               throw new EJBDeploymentException(this.deployInfo.getEjbName(), var2, new WLDeploymentException("contextpath " + var3 + " is " + "used in other web application. Please specify a different one."));
            }

            if (verbose) {
               Verbose.log((Object)("using existing web app with context path:" + var11.getContextPath()));
            }

            AppDeploymentMBean var12 = var1.getAppDeploymentMBean();
            if (var12 != null) {
               this.version = var12.getVersionIdentifier();
            }
         } else {
            this.createVirtualWebApp(var8, var6, var7);
         }

      }
   }

   private static boolean hasSecurity(PortComponentBean var0) {
      if (var0 != null) {
         String var1 = var0.getTransportGuarantee();
         LoginConfigBean var2 = var0.getLoginConfig();
         if (var2 != null) {
            return true;
         }

         if (var1 != null) {
            var1 = var1.trim();
            if (!"NONE".equals(var1)) {
               return true;
            }
         }
      }

      return false;
   }

   void activate() throws ModuleException, DeploymentException {
      if (this.virtualWebApp != null) {
         this.virtualWebApp.activate();
      }

      WebAppServletContext var1 = WebService.defaultHttpServer().getServletContextManager().getContextForContextPath(this.contextPath, this.appCtx.getAppDeploymentMBean().getVersionIdentifier());
      if (var1 == null) {
         throw new AssertionError("In webservice EJB activate, servlet context with contextpath " + this.contextPath + " is not found.");
      } else {
         this.deployInfo.store(this.serviceUrl, (ServletContext)var1);
         if (this.virtualWebApp != null) {
            this.virtualWebApp.start();
         } else {
            String[] var2 = this.deployInfo.getServiceURIs();
            HashMap var3 = new HashMap();
            var3.put("weblogic.wsee.EJBServiceURL", this.serviceUrl);
            byte var4 = 1;
            var1.registerServlet(this.deployInfo.getServiceURIs()[0], this.deployInfo.getServlet(), var2, var3, var4);
         }

      }
   }

   void deactivate() throws ModuleException {
      if (this.virtualWebApp != null) {
         this.virtualWebApp.deactivate();
      }

   }

   void removeWebApp() throws ModuleException {
      if (this.virtualWebApp != null) {
         this.virtualWebApp.unprepare();
         this.virtualWebApp.destroy(UpdateListener.Registration.NOOP);
         this.virtualWebApp.remove();
      }

      WsRegistry.instance().unregister(this.serviceUrl, this.version);
   }

   private void createVirtualWebApp(HttpServer var1, PortComponentBean var2, DeploymentInfo var3) throws EJBDeploymentException {
      this.virtualWebApp = new VirtualWebApp("", this.contextPath, var3);

      try {
         this.virtualWebApp.createWebDD(var2, this.serviceUrl, this.deployInfo);
         this.virtualWebApp.initUsingLoader(this.appCtx, (GenericClassLoader)var3.getModuleClassLoader(), UpdateListener.Registration.NOOP);
         this.virtualWebApp.prepare();
         WebAppServletContext var4 = var1.getServletContextManager().getContextForContextPath(this.contextPath, this.appCtx.getAppDeploymentMBean().getVersionIdentifier());
         if (var4 == null) {
            throw new AssertionError("Webapp context for context-root=" + this.contextPath + " is null after creating");
         }
      } catch (ModuleException var5) {
         throw new EJBDeploymentException(this.deployInfo.getEjbName(), this.deployInfo.getEjbName(), var5);
      }
   }

   private static class VirtualWebApp extends WebAppModule {
      private WebAppParser webAppParser;
      private DeploymentInfo ejbDeploymentInfo;
      static final String DISPATCH_POLICY = "wl-dispatch-policy";

      private VirtualWebApp(String var1, String var2, DeploymentInfo var3) {
         this(var1, var2);
         this.ejbDeploymentInfo = var3;
      }

      VirtualWebApp(String var1, String var2) {
         super(var1, var2);
      }

      protected WebAppParser getWebAppParser(VirtualJarFile var1, DeploymentPlanBean var2) {
         return this.webAppParser;
      }

      public void createWebDD(PortComponentBean var1, String var2, EJBDeployInfo var3) {
         DescriptorBean var4 = (new DescriptorManager()).createDescriptorRoot(WebAppBean.class).getRootBean();
         WebAppBean var5 = (WebAppBean)var4;
         ServletBean var6 = var5.createServlet();
         var6.setServletName("WSEE_SERVLET");
         var6.setServletClass(var3.getServlet());
         var6.setLoadOnStartup("1");
         ParamValueBean var7 = var6.createInitParam();
         var7.setParamName("weblogic.wsee.EJBServiceURL");
         var7.setParamValue(var2);
         String var8 = var3.getEjbName();
         String var10;
         if (this.ejbDeploymentInfo != null) {
            BeanInfo var9 = ((weblogic.ejb.container.interfaces.DeploymentInfo)this.ejbDeploymentInfo).getBeanInfo(var8);
            if (var9 != null) {
               var10 = var9.getDispatchPolicy();
               if (var10 != null) {
                  ParamValueBean var11 = var6.createInitParam();
                  var11.setParamName("wl-dispatch-policy");
                  var11.setParamValue(var10);
               }
            }
         }

         ServletMappingBean var17 = var5.createServletMapping();
         var17.setServletName("WSEE_SERVLET");
         var17.setUrlPatterns(var3.getServiceURIs());
         var10 = var3.getServiceURIs()[0];
         if (var1 != null) {
            LoginConfigBean var16 = var1.getLoginConfig();
            if (var16 != null) {
               this.copyLoginConfig(var16, var5.createLoginConfig());
            }

            String var12 = var1.getTransportGuarantee();
            if (var12 != null) {
               SecurityConstraintBean var13 = var5.createSecurityConstraint();
               WebResourceCollectionBean var14 = var13.createWebResourceCollection();
               var14.setWebResourceName("WSEE_SERVLETResource");
               var14.setUrlPatterns(new String[]{var10});
               var14.setHttpMethods(new String[]{"POST", "GET", "PUT"});
               UserDataConstraintBean var15 = var13.createUserDataConstraint();
               var15.setTransportGuarantee(var12);
            }
         }

         this.webAppParser = new WseeWebappParser(var5, (WeblogicWebAppBean)null);
      }

      protected boolean isDeployedLocally() {
         return true;
      }

      protected void initPersistenceMBean() {
      }

      protected void setupPersistenceUnitRegistry() {
      }

      private void copyLoginConfig(LoginConfigBean var1, LoginConfigBean var2) {
         var2.setAuthMethod(var1.getAuthMethod());
         var2.setRealmName(var1.getRealmName());
      }

      // $FF: synthetic method
      VirtualWebApp(String var1, String var2, DeploymentInfo var3, Object var4) {
         this(var1, var2, var3);
      }
   }
}
