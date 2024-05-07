package weblogic.servlet.internal;

import java.util.Collection;
import java.util.Iterator;
import weblogic.application.ApplicationFactoryManager;
import weblogic.logging.Loggable;
import weblogic.management.DeploymentException;
import weblogic.management.UndeploymentException;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.DeploymentMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.VirtualHostMBean;
import weblogic.management.configuration.WebServerMBean;
import weblogic.management.internal.DeploymentHandler;
import weblogic.management.internal.DeploymentHandlerContext;
import weblogic.management.internal.DeploymentHandlerHome;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.protocol.ServerChannel;
import weblogic.protocol.ServerChannelManager;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;
import weblogic.servlet.HTTPLogger;
import weblogic.utils.Debug;
import weblogic.utils.collections.ConcurrentHashMap;

public final class WebService extends AbstractServerService implements DeploymentHandler {
   private static HttpServer defaultHttpServer = null;
   private static WebService singleton;
   private static final ConcurrentHashMap httpServers = new ConcurrentHashMap();
   private static final ConcurrentHashMap virtualHostsVsNames = new ConcurrentHashMap();
   private static final ConcurrentHashMap virtualHostsVsChannels = new ConcurrentHashMap();
   public static final String INTERNAL_WEB_APP_CONTEXT_PATH = "/bea_wls_internal";
   public static final String MGMT_WEB_APP_CONTEXT_PATH = "/wl_management_internal";

   private void initialize() throws ServiceFailureException {
      HTTPLogger.logHTTPInit();
      initWebServers();
      ApplicationFactoryManager var1 = ApplicationFactoryManager.getApplicationFactoryManager();
      var1.addLibraryFactory(new WarLibraryFactory());
      var1.addDeploymentFactory(new WarDeploymentFactory());
      var1.addModuleFactory(new WebAppModuleFactory());
   }

   public String getName() {
      return "Servlet Container";
   }

   public String getVersion() {
      return "Servlet 2.5, JSP 2.1";
   }

   public void start() throws ServiceFailureException {
      singleton = this;
      this.initialize();
      HTTPLogger.logWebInit();
      ClusterMBean var1 = ManagementService.getRuntimeAccess(WebAppConfigManager.KERNEL_ID).getServer().getCluster();
      if (var1 != null) {
         MembershipControllerImpl var2 = (MembershipControllerImpl)MembershipControllerImpl.getInstance();
         var2.startService();
      }

      if (HTTPDebugLogger.isEnabled()) {
         HTTPDebugLogger.debug("start/resume web servers");
      }

      startWebServers();
      Debug.assertion(defaultHttpServer != null);
      DeploymentHandlerHome.addDeploymentHandler(this);
   }

   public void stop() throws ServiceFailureException {
      ClusterMBean var1 = ManagementService.getRuntimeAccess(WebAppConfigManager.KERNEL_ID).getServer().getCluster();
      if (var1 != null) {
         MembershipControllerImpl var2 = (MembershipControllerImpl)MembershipControllerImpl.getInstance();
         var2.stopService();
      }

   }

   public void halt() throws ServiceFailureException {
   }

   private static void initWebServers() throws ServiceFailureException {
      RuntimeAccess var0 = ManagementService.getRuntimeAccess(WebAppConfigManager.KERNEL_ID);
      ServerMBean var1 = var0.getServer();
      WebServerMBean var2 = var1.getWebServer();

      try {
         initWebServer(var2);
      } catch (DeploymentException var4) {
         throw new ServiceFailureException("Error starting web service", var4);
      }
   }

   private static void startWebServers() {
      Iterator var0 = httpServers.values().iterator();

      while(var0.hasNext()) {
         HttpServer var1 = (HttpServer)var0.next();
         var1.start();
         if (HTTPDebugLogger.isEnabled()) {
            HTTPDebugLogger.debug("started web server " + var1);
         }
      }

   }

   public static WebService getWebService() {
      return singleton;
   }

   public static final HttpServer defaultHttpServer() {
      return defaultHttpServer;
   }

   public static final HttpServer getHttpServer(String var0) {
      HttpServer var1 = (HttpServer)httpServers.get(var0);
      return var1;
   }

   public static final Collection getHttpServers() {
      return httpServers.values();
   }

   public static final HttpServer getVirtualHost(String var0) {
      return (HttpServer)virtualHostsVsNames.get(var0.toLowerCase());
   }

   public static final HttpServer getVirtualHost(ServerChannel var0) {
      return (HttpServer)virtualHostsVsChannels.get(var0.getChannelName());
   }

   public static final HttpServer findHttpServer(String var0) {
      if (var0 == null) {
         if (HTTPDebugLogger.isEnabled()) {
            HTTPDebugLogger.debug(HTTPLogger.logNoHostInHeaderLoggable().getMessage());
         }

         return defaultHttpServer();
      } else {
         HttpServer var1 = getVirtualHost(var0);
         if (var1 == null) {
            if (HTTPDebugLogger.isEnabled()) {
               HTTPDebugLogger.debug(HTTPLogger.logHostNotFoundLoggable(var0).getMessage());
            }

            var1 = defaultHttpServer();
         } else if (HTTPDebugLogger.isEnabled()) {
            HTTPDebugLogger.debug(HTTPLogger.logDispatchRequestLoggable(var0).getMessage());
         }

         return var1;
      }
   }

   public static final String getInternalWebAppContextPath() {
      return "/bea_wls_internal";
   }

   public static final String getManagementContextPath() {
      return "/wl_management_internal";
   }

   private static HttpServer initWebServer(WebServerMBean var0) throws DeploymentException {
      if (HTTPDebugLogger.isEnabled()) {
         HTTPDebugLogger.debug(HTTPLogger.logInitWebLoggable(var0.getName()).getMessage());
      }

      HttpServer var1 = (HttpServer)httpServers.get(var0.getName());
      if (var1 != null) {
         return var1;
      } else {
         var1 = new HttpServer(var0);
         if (var0 instanceof VirtualHostMBean) {
            registerVirtualHost(var0, var1);
         } else {
            HTTPLogger.logDefaultName(var1.getName());
            if (defaultHttpServer != null) {
               throw new DeploymentException("Could not load web server " + var1.getName() + " as the default web server, web server " + defaultHttpServer.getName() + " is already deployed as the default " + "web server.");
            }

            defaultHttpServer = var1;
            if (HTTPDebugLogger.isEnabled()) {
               HTTPDebugLogger.debug(var0 + " is the default web server");
            }
         }

         httpServers.put(var0.getName(), var1);
         var1.initialize();
         if (HTTPDebugLogger.isEnabled()) {
            HTTPDebugLogger.debug("initialized web server " + var0);
         }

         return var1;
      }
   }

   private static void registerVirtualHost(WebServerMBean var0, HttpServer var1) throws DeploymentException {
      VirtualHostMBean var2 = (VirtualHostMBean)var0;
      String[] var3 = var2.getVirtualHostNames();
      if (var3 != null) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            HTTPLogger.logRegisterVirtualHost(var3[var4]);
            validateHostName(var3[var4], var0);
            virtualHostsVsNames.put(var3[var4].toLowerCase(), var1);
         }
      }

      String var5 = var2.getNetworkAccessPoint();
      if (var5 != null) {
         HTTPLogger.logRegisterVirtualHost(" with server ServerChannelName: " + var5);
         validateChannel(var5, var0);
         virtualHostsVsChannels.put(var5, var1);
      }

   }

   private static void validateHostName(String var0, WebServerMBean var1) throws DeploymentException {
      HttpServer var2 = (HttpServer)virtualHostsVsNames.get(var0.toLowerCase());
      if (var2 != null) {
         Loggable var3 = HTTPLogger.logVirtualHostNameAlreadyUsedLoggable(var0, var1.getName(), var2.getName());
         var3.log();
         throw new DeploymentException(var3.getMessage());
      }
   }

   private static void validateChannel(String var0, WebServerMBean var1) throws DeploymentException {
      HttpServer var2 = (HttpServer)virtualHostsVsChannels.get(var0);
      Loggable var3;
      if (var2 != null) {
         var3 = HTTPLogger.logVirtualHostServerChannelNameAlreadyUsedLoggable(var0, var1.getName(), var2.getName());
         var3.log();
         throw new DeploymentException(var3.getMessage());
      } else if (ServerChannelManager.findLocalServerChannel(var0) == null) {
         var3 = HTTPLogger.logVirtualHostServerChannelUndefinedLoggable(var0, var1.getName());
         var3.log();
         throw new DeploymentException(var3.getMessage());
      }
   }

   public void prepareDeployment(DeploymentMBean var1, DeploymentHandlerContext var2) throws DeploymentException {
      if (var1 instanceof WebServerMBean) {
         initWebServer((WebServerMBean)var1);
      }

   }

   public void activateDeployment(DeploymentMBean var1, DeploymentHandlerContext var2) throws DeploymentException {
      if (var1 instanceof WebServerMBean) {
         WebServerMBean var3 = (WebServerMBean)var1;
         HttpServer var4 = (HttpServer)httpServers.get(var3.getName());
         if (var4 != null) {
            var4.start();
         }
      }

   }

   public void deactivateDeployment(DeploymentMBean var1, DeploymentHandlerContext var2) throws UndeploymentException {
      if (var1 instanceof WebServerMBean) {
         WebServerMBean var3 = (WebServerMBean)var1;
         if (var3.getName().equals(defaultHttpServer().getName())) {
            throw new UndeploymentException("Cannot undeploy default HTTP server");
         }

         HttpServer var4 = (HttpServer)httpServers.get(var3.getName());
         if (var4 != null) {
            var4.shutdown();
         }
      }

   }

   public void unprepareDeployment(DeploymentMBean var1, DeploymentHandlerContext var2) throws UndeploymentException {
      if (var1 instanceof WebServerMBean) {
         WebServerMBean var3 = (WebServerMBean)var1;
         if (var3.getName().equals(defaultHttpServer().getName())) {
            throw new UndeploymentException("Cannot undeploy default HTTP server");
         }

         httpServers.remove(var3.getName());
         if (var3 instanceof VirtualHostMBean) {
            VirtualHostMBean var4 = (VirtualHostMBean)var3;
            String[] var5 = var4.getVirtualHostNames();
            if (var5 != null) {
               for(int var6 = 0; var6 < var5.length; ++var6) {
                  String var7 = var5[var6];
                  if (var7 != null) {
                     virtualHostsVsNames.remove(var5[var6].toLowerCase());
                  }
               }
            }

            String var8 = var4.getNetworkAccessPoint();
            if (var8 != null) {
               virtualHostsVsChannels.remove(var8);
            }
         }
      }

   }
}
