package weblogic.servlet.internal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import weblogic.version;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.ModuleException;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.cluster.ClusterService;
import weblogic.cluster.replication.ROID;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.diagnostics.instrumentation.PointcutHandlingInfo;
import weblogic.diagnostics.instrumentation.ValueHandlingInfo;
import weblogic.j2ee.J2EEApplicationRuntimeMBeanImpl;
import weblogic.jndi.Environment;
import weblogic.management.DeploymentException;
import weblogic.management.ManagementException;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.VirtualHostMBean;
import weblogic.management.configuration.WebAppComponentMBean;
import weblogic.management.configuration.WebAppContainerMBean;
import weblogic.management.configuration.WebServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.protocol.LocalServerIdentity;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.SecurityServiceManager;
import weblogic.servlet.HTTPLogger;
import weblogic.servlet.internal.session.ReplicatedSessionContext;
import weblogic.servlet.internal.session.SessionContext;
import weblogic.servlet.internal.session.SessionInternal;
import weblogic.servlet.logging.LogManagerHttp;
import weblogic.utils.StackTraceUtils;
import weblogic.utils.collections.WeakConcurrentHashMap;
import weblogic.workarea.WorkContextHelper;
import weblogic.workarea.WorkContextMap;
import weblogic.workarea.spi.WorkContextMapInterceptor;
import weblogic.workarea.utils.WorkContextInputAdapter;
import weblogic.workarea.utils.WorkContextOutputAdapter;

public final class HttpServer {
   public static final String SERVER_INFO;
   private static final AuthenticatedSubject kernelId;
   public static final byte[] FORBIDDEN_RESPONSE;
   private static final boolean PRODUCTION_MODE;
   private static final WebAppContainerMBean webAppContainer;
   private final WebServerMBean mbean;
   private final ServerMBean serverMBean;
   private final ClusterMBean clusterMBean;
   private String replicationChannel;
   private WebServerRuntimeMBeanImpl runtime;
   private final boolean defaultWebServer;
   private final String logContext;
   private final String serverHash;
   private final ServletContextManager servletContextManager;
   private final OnDemandManager onDemandManager;
   private final LogManagerHttp logmanager;
   private final Replicator replicator = new Replicator();
   private final WorkContextManager workCtxManager = new WorkContextManager();
   private final SessionLogin sessionLogin;
   private boolean weblogicPluginEnabled;
   private boolean httpTraceSupportEnabled;
   private boolean authCookieEnabled;
   private boolean wapEnabled;
   private String clientIpHeader;
   private static ROIDLookupImpl roidImpl;
   private final Map runtimes;
   private Context jndiContext;
   static final long serialVersionUID = 5593485415812104821L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.servlet.internal.HttpServer");
   public static final DelegatingMonitor _WLDF$INST_FLD_Servlet_UnloadWebApp_Around_Low;
   public static final DelegatingMonitor _WLDF$INST_FLD_Servlet_LoadWebApp_Around_Low;
   public static final JoinPoint _WLDF$INST_JPFLD_0;
   public static final JoinPoint _WLDF$INST_JPFLD_1;

   public HttpServer(WebServerMBean var1) {
      this.sessionLogin = new SessionLogin(this.workCtxManager);
      this.clientIpHeader = null;
      this.runtimes = new HashMap();
      this.mbean = var1;
      if (this.mbean instanceof VirtualHostMBean) {
         this.defaultWebServer = false;
      } else {
         this.defaultWebServer = true;
      }

      this.serverMBean = ManagementService.getRuntimeAccess(kernelId).getServer();
      this.clusterMBean = ManagementService.getRuntimeAccess(kernelId).getServer().getCluster();
      this.logContext = this.initLogContext();
      if (roidImpl == null) {
         Class var2 = HttpServer.class;
         synchronized(HttpServer.class) {
            if (roidImpl == null) {
               try {
                  roidImpl = new ROIDLookupImpl(this);
                  ClusterService var3 = (ClusterService)ClusterService.getServices();
                  if (var3 != null && var3.isReplicationTimeoutEnabled()) {
                     weblogic.rmi.extensions.server.ServerHelper.exportObject(roidImpl, var3.getHeartbeatTimeoutMillis());
                  } else {
                     weblogic.rmi.extensions.server.ServerHelper.exportObject(roidImpl);
                  }
               } catch (RemoteException var5) {
                  throw new AssertionError("ROIDLookupImpl initial reference could not be exported.");
               }
            }
         }
      }

      this.servletContextManager = new ServletContextManager(this.mbean);
      this.logmanager = new LogManagerHttp(this.mbean);
      this.onDemandManager = new OnDemandManager();
      this.serverHash = Integer.toString(LocalServerIdentity.getIdentity().hashCode());
      this.initConfigSwitches();
   }

   private void initConfigSwitches() {
      this.weblogicPluginEnabled = webAppContainer.isWeblogicPluginEnabled();
      this.httpTraceSupportEnabled = webAppContainer.isHttpTraceSupportEnabled();
      this.authCookieEnabled = webAppContainer.isAuthCookieEnabled();
      this.wapEnabled = webAppContainer.isWAPEnabled();
      if (this.clusterMBean != null) {
         this.replicationChannel = this.clusterMBean.getReplicationChannel();
         if (this.clusterMBean.isSet("WeblogicPluginEnabled")) {
            this.weblogicPluginEnabled = this.clusterMBean.isWeblogicPluginEnabled();
         }

         if (this.clusterMBean.isSet("HttpTraceSupportEnabled")) {
            this.httpTraceSupportEnabled = this.clusterMBean.isHttpTraceSupportEnabled();
         }
      }

      if (this.serverMBean.isSet("WeblogicPluginEnabled")) {
         this.weblogicPluginEnabled = this.serverMBean.isWeblogicPluginEnabled();
      }

      if (this.serverMBean.isSet("HttpTraceSupportEnabled")) {
         this.httpTraceSupportEnabled = this.serverMBean.isHttpTraceSupportEnabled();
      }

      if (this.mbean.isSet("AuthCookieEnabled")) {
         this.authCookieEnabled = this.mbean.isAuthCookieEnabled();
      }

      if (this.mbean.isSet("WAPEnabled")) {
         this.wapEnabled = this.mbean.isWAPEnabled();
      }

      if (this.mbean.isSet("ClientIpHeader")) {
         this.clientIpHeader = this.mbean.getClientIpHeader();
      }

   }

   private String initLogContext() {
      if (this.defaultWebServer) {
         return "HttpServer (defaultWebserver) name: " + this.getName();
      } else {
         StringBuffer var1 = new StringBuffer();
         var1.append("HttpServer (VirtualHost) name: ");
         var1.append(this.getName());
         var1.append(" hosts: [");
         String[] var2 = this.getVirtualHostNames();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            var1.append(var2[var3]);
            if (var3 + 1 < var2.length) {
               var1.append(", ");
            }
         }

         var1.append("] channel: ");
         var1.append(((VirtualHostMBean)this.mbean).getNetworkAccessPoint());
         return var1.toString();
      }
   }

   public boolean isWeblogicPluginEnabled() {
      return this.weblogicPluginEnabled;
   }

   public boolean isHttpTraceSupportEnabled() {
      return this.httpTraceSupportEnabled;
   }

   public static boolean isProductionModeEnabled() {
      return PRODUCTION_MODE;
   }

   public String[] getVirtualHostNames() {
      return this.defaultWebServer ? new String[]{this.getFrontendHost()} : ((VirtualHostMBean)this.mbean).getVirtualHostNames();
   }

   public Replicator getReplicator() {
      return this.replicator;
   }

   public String getReplicationChannel() {
      return this.replicationChannel;
   }

   public SessionLogin getSessionLogin() {
      return this.sessionLogin;
   }

   public LogManagerHttp getLogManager() {
      return this.logmanager;
   }

   public WorkContextManager getWorkContextManager() {
      return this.workCtxManager;
   }

   public ServletContextManager getServletContextManager() {
      return this.servletContextManager;
   }

   public OnDemandManager getOnDemandManager() {
      return this.onDemandManager;
   }

   public String getServerHash() {
      return this.serverHash;
   }

   public WebServerMBean getMBean() {
      return this.mbean;
   }

   public String getName() {
      return this.getMBean().getName();
   }

   public String getListenAddress() {
      return this.serverMBean.getListenAddress();
   }

   public String getFrontendHost() {
      return this.defaultWebServer && this.clusterMBean != null ? this.clusterMBean.getFrontendHost() : this.mbean.getFrontendHost();
   }

   public int getFrontendHTTPPort() {
      return this.defaultWebServer && this.clusterMBean != null ? this.clusterMBean.getFrontendHTTPPort() : this.mbean.getFrontendHTTPPort();
   }

   public int getFrontendHTTPSPort() {
      return this.defaultWebServer && this.clusterMBean != null ? this.clusterMBean.getFrontendHTTPSPort() : this.mbean.getFrontendHTTPSPort();
   }

   public RuntimeMBean initialize() {
      HTTPLogger.logInit(this.logContext);
      String var1 = ManagementService.getRuntimeAccess(kernelId).getServerName();
      String var2 = var1 + "_" + this.getName();

      try {
         this.runtime = new WebServerRuntimeMBeanImpl(var2, this, this.defaultWebServer);
         ManagementService.getRuntimeAccess(kernelId).getServerRuntime().addWebServerRuntime(this.runtime);
         return this.runtime;
      } catch (ManagementException var4) {
         HTTPLogger.logFailedToCreateWebServerRuntimeMBean(var2, var4);
         return null;
      }
   }

   public synchronized void start() {
      this.initURLResources();
      this.logmanager.start();
      if (HTTPDebugLogger.isEnabled()) {
         HTTPLogger.logStarted(this.logContext);
         HTTPDebugLogger.debug(this + " HttpServer started and is ready to receive http requests");
      }

   }

   private void initURLResources() {
      Hashtable var1 = new Hashtable();
      var1.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
      var1.put("weblogic.jndi.createIntermediateContexts", "true");

      try {
         this.jndiContext = new InitialContext(var1);
      } catch (Exception var9) {
         HTTPLogger.logNoJNDIContext(this.logContext, var9.toString());
         return;
      }

      Map var2 = this.getMBean().getURLResource();
      if (var2 != null) {
         Iterator var3 = var2.keySet().iterator();

         while(true) {
            while(var3.hasNext()) {
               String var4 = (String)var3.next();
               String var5 = (String)var2.get(var4);
               if (var5 != null && var5.length() != 0) {
                  URL var6;
                  try {
                     var6 = new URL(var5);
                  } catch (MalformedURLException var11) {
                     HTTPLogger.logURLParseError(this.logContext, var5);
                     continue;
                  }

                  try {
                     this.jndiContext.bind(var4, var6);
                  } catch (NamingException var10) {
                     HTTPLogger.logUnableToBindURL(this.logContext, var5, var4, var10.toString());
                     continue;
                  }

                  try {
                     URLResourceRuntimeMBeanImpl var7 = new URLResourceRuntimeMBeanImpl(var4);
                     this.runtimes.put(var4, var7);
                  } catch (ManagementException var8) {
                     HTTPLogger.logUnableToBindURL(this.logContext, var5, var4, var8.toString());
                  }

                  HTTPLogger.logBoundURL(this.logContext, var5, var4);
               } else {
                  HTTPLogger.logNullURL(this.logContext, var4);
               }
            }

            return;
         }
      }
   }

   private void destroyURLResources() {
      Iterator var1 = this.runtimes.keySet().iterator();

      while(var1.hasNext()) {
         String var2 = (String)var1.next();

         try {
            this.jndiContext.unbind(var2);
         } catch (NamingException var6) {
            if (HTTPDebugLogger.isEnabled()) {
               HTTPDebugLogger.debug("Error while unbinding URLResource: " + StackTraceUtils.throwable2StackTrace(var6));
            }
         }

         URLResourceRuntimeMBeanImpl var3 = (URLResourceRuntimeMBeanImpl)this.runtimes.get(var2);

         try {
            if (var3 != null) {
               var3.unregister();
            }
         } catch (ManagementException var5) {
            if (HTTPDebugLogger.isEnabled()) {
               HTTPDebugLogger.debug("Error while unregistering URLResource: " + StackTraceUtils.throwable2StackTrace(var5));
            }
         }
      }

   }

   public synchronized void shutdown() {
      this.destroyURLResources();

      try {
         if (this.runtime != null) {
            ManagementService.getRuntimeAccess(kernelId).getServerRuntime().removeWebServerRuntime(this.runtime);
            this.runtime.unregister();
         }
      } catch (ManagementException var2) {
         throw new AssertionError("Unable to unregister runtime mbean");
      }

      if (this.logmanager != null) {
         this.logmanager.close();
      }

      if (HTTPDebugLogger.isEnabled()) {
         HTTPDebugLogger.debug("HttpServer is shutting down: " + this.logContext);
         HTTPLogger.logShutdown(this.logContext);
      }

   }

   public synchronized WebAppServletContext loadWebApp(WebAppComponentMBean var1, ApplicationContextInternal var2, WebAppModule var3, String var4) throws ModuleException {
      boolean var14;
      boolean var10000 = var14 = _WLDF$INST_FLD_Servlet_LoadWebApp_Around_Low.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var15 = null;
      DiagnosticActionState[] var16 = null;
      Object var13 = null;
      DynamicJoinPoint var22;
      if (var10000) {
         Object[] var9 = null;
         if (_WLDF$INST_FLD_Servlet_LoadWebApp_Around_Low.isArgumentsCaptureNeeded()) {
            var9 = new Object[]{this, var1, var2, var3, var4};
         }

         var22 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var9, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_Servlet_LoadWebApp_Around_Low;
         DiagnosticAction[] var10002 = var15 = var10001.getActions();
         InstrumentationSupport.preProcess(var22, var10001, var10002, var16 = InstrumentationSupport.getActionStates(var10002));
      }

      boolean var19 = false;

      WebAppServletContext var23;
      try {
         var19 = true;
         if (HTTPDebugLogger.isEnabled()) {
            HTTPDebugLogger.debug(HTTPLogger.logLoadingWebAppLoggable(this.logContext, var3.getName()).getMessage());
         }

         WebAppServletContext var5;
         try {
            var5 = new WebAppServletContext(this, var2, var1, var3, var4);
         } catch (Exception var20) {
            String var7 = var3 == null ? "<internal>" : var3.getName();
            if (HTTPDebugLogger.isEnabled()) {
               HTTPDebugLogger.debug("Failed to load webapp: '" + var7 + "'", var20);
            }

            throw new ModuleException("Failed to load webapp: '" + var7 + "'", var20);
         }

         J2EEApplicationRuntimeMBeanImpl var6 = var2.getRuntime();
         this.doPostContextInit(var5, var6);
         var23 = var5;
         var19 = false;
      } finally {
         if (var19) {
            var22 = null;
            if (var14) {
               InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_Servlet_LoadWebApp_Around_Low, var15, var16);
            }

         }
      }

      if (var14) {
         InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_Servlet_LoadWebApp_Around_Low, var15, var16);
      }

      return var23;
   }

   private void doPostContextInit(WebAppServletContext var1, RuntimeMBean var2) throws ModuleException {
      try {
         this.servletContextManager.registerContext(var1);
      } catch (DeploymentException var5) {
         throw new ModuleException(var5.getMessage(), var5);
      }

      if (HTTPDebugLogger.isEnabled() && var1.isDefaultContext()) {
         HTTPDebugLogger.debug(HTTPLogger.logSetContextLoggable(this.logContext, var1.getName(), this.getName()).getMessage());
      }

      try {
         var1.prepare(var2);
      } catch (DeploymentException var4) {
         this.unloadWebApp(var1, var1.getVersionId());
         throw new ModuleException(var4.getMessage(), var4);
      }
   }

   public synchronized void unloadWebApp(WebAppServletContext var1, String var2) {
      boolean var9;
      boolean var10000 = var9 = _WLDF$INST_FLD_Servlet_UnloadWebApp_Around_Low.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var10 = null;
      DiagnosticActionState[] var11 = null;
      Object var8 = null;
      if (var10000) {
         Object[] var4 = null;
         if (_WLDF$INST_FLD_Servlet_UnloadWebApp_Around_Low.isArgumentsCaptureNeeded()) {
            var4 = new Object[]{this, var1, var2};
         }

         DynamicJoinPoint var14 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, var4, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_Servlet_UnloadWebApp_Around_Low;
         DiagnosticAction[] var10002 = var10 = var10001.getActions();
         InstrumentationSupport.preProcess(var14, var10001, var10002, var11 = InstrumentationSupport.getActionStates(var10002));
      }

      try {
         this.servletContextManager.destroyContext(var1, var2);
      } finally {
         if (var9) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_1, _WLDF$INST_FLD_Servlet_UnloadWebApp_Around_Low, var10, var11);
         }

      }

   }

   public synchronized WebAppServletContext createInternalContext(ApplicationContextInternal var1, String var2, ClassLoader var3) throws DeploymentException {
      Context var4 = var1.getEnvContext();

      try {
         var4.lookup("/webapp");
      } catch (NameNotFoundException var8) {
         try {
            var4.createSubcontext("webapp");
         } catch (NamingException var7) {
            throw new DeploymentException("Failed to create environment context", var7);
         }
      } catch (NamingException var9) {
         throw new AssertionError(var9);
      }

      WebAppServletContext var5 = new WebAppServletContext(this, var1, var2, var3);
      this.servletContextManager.registerContext(var5);
      return var5;
   }

   public String getServerName() {
      return this.serverMBean.getName();
   }

   public String toString() {
      return this.logContext;
   }

   public boolean isAuthCookieEnabled() {
      return this.authCookieEnabled;
   }

   public boolean isWAPEnabled() {
      return this.wapEnabled;
   }

   public String getClientIpHeader() {
      return this.clientIpHeader;
   }

   public int getPostTimeoutSecs() {
      return this.mbean.isPostTimeoutSecsSet() ? this.mbean.getPostTimeoutSecs() : webAppContainer.getPostTimeoutSecs();
   }

   public int getMaxPostTimeSecs() {
      return this.mbean.isMaxPostTimeSecsSet() ? this.mbean.getMaxPostTimeSecs() : webAppContainer.getMaxPostTimeSecs();
   }

   public int getMaxPostSize() {
      return this.mbean.isMaxPostSizeSet() ? this.mbean.getMaxPostSize() : webAppContainer.getMaxPostSize();
   }

   static {
      _WLDF$INST_FLD_Servlet_UnloadWebApp_Around_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Servlet_UnloadWebApp_Around_Low");
      _WLDF$INST_FLD_Servlet_LoadWebApp_Around_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Servlet_LoadWebApp_Around_Low");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "HttpServer.java", "weblogic.servlet.internal.HttpServer", "loadWebApp", "(Lweblogic/management/configuration/WebAppComponentMBean;Lweblogic/application/ApplicationContextInternal;Lweblogic/servlet/internal/WebAppModule;Ljava/lang/String;)Lweblogic/servlet/internal/WebAppServletContext;", 412, InstrumentationSupport.makeMap(new String[]{"Servlet_LoadWebApp_Around_Low"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{null, null, InstrumentationSupport.createValueHandlingInfo("module", "weblogic.diagnostics.instrumentation.gathering.WebAppModuleNameRenderer", false, true), null})}), (boolean)0);
      _WLDF$INST_JPFLD_1 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "HttpServer.java", "weblogic.servlet.internal.HttpServer", "unloadWebApp", "(Lweblogic/servlet/internal/WebAppServletContext;Ljava/lang/String;)V", 461, InstrumentationSupport.makeMap(new String[]{"Servlet_UnloadWebApp_Around_Low"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("ctx", "weblogic.diagnostics.instrumentation.gathering.WebAppModuleNameRenderer", false, true), null})}), (boolean)0);
      SERVER_INFO = version.getWebServerReleaseInfo();
      kernelId = WebAppConfigManager.KERNEL_ID;
      FORBIDDEN_RESPONSE = "HTTP/1.0 403 Forbidden\r\nWL-Result: UNAVAIL\r\nContent-Type: text/html\r\n\r\n<TITLE>403 Forbidden</TITLE>The Server is not licensed for this operation.".getBytes();
      PRODUCTION_MODE = ManagementService.getRuntimeAccess(kernelId).getDomain().isProductionModeEnabled();
      webAppContainer = ManagementService.getRuntimeAccess(kernelId).getDomain().getWebAppContainer();
      roidImpl = null;
   }

   private static class SessionIDToROIDMapHolder {
      private final HashSet set;
      private final ROID id;

      private SessionIDToROIDMapHolder(ROID var1) {
         this.set = new HashSet();
         this.id = var1;
      }

      private synchronized void add(String var1) {
         this.set.add(var1);
      }

      private synchronized boolean remove(String var1) {
         this.set.remove(var1);
         return this.set.size() == 0;
      }

      private ROID getROID() {
         return this.id;
      }

      // $FF: synthetic method
      SessionIDToROIDMapHolder(ROID var1, Object var2) {
         this(var1);
      }
   }

   public static final class WorkContextManager {
      private final Map csidWorkContextMap = new Hashtable();

      public void initOrRestoreThreadContexts(WebAppServletContext var1, ServletRequestImpl var2) throws IOException {
         if (!this.isThreadContextIgnorable(var2)) {
            SessionInternal var3 = (SessionInternal)var2.getSession(false);
            String var4 = var2.getSessionHelper().getSessionID();

            try {
               if (var4 != null) {
                  boolean var5 = false;
                  byte[] var6;
                  if (var3 != null) {
                     var6 = (byte[])((byte[])var3.getInternalAttribute("weblogic.workContexts"));
                     if (var6 != null) {
                        var5 = true;
                        this.restoreWorkContexts(var6);
                     }
                  }

                  var6 = (byte[])((byte[])this.csidWorkContextMap.get(var4));
                  if (var6 != null) {
                     var5 = true;
                     this.restoreWorkContexts(var6);
                  }

                  if (var5) {
                     if (HTTPDebugLogger.isEnabled()) {
                        HTTPDebugLogger.debug("*** HttpServer.WorkContextManager.restoreThreadContexts for CSID=" + var4 + ", app=" + var1.getAppDisplayName() + ", workCtxs=" + ApplicationVersionUtils.getDebugWorkContexts());
                     }

                     return;
                  }
               }
            } finally {
               if (var1.getVersionId() != null) {
                  ApplicationVersionUtils.setCurrentVersionId(var1.getApplicationName(), var1.getVersionId());
               }

               if (var1.isAdminMode()) {
                  ApplicationVersionUtils.setCurrentAdminMode(true);
               }

            }

         }
      }

      public void copyThreadContexts(WebAppServletContext var1, ServletRequestImpl var2) {
         if (!this.isThreadContextIgnorable(var2)) {
            try {
               WorkContextMapInterceptor var3 = WorkContextHelper.getWorkContextHelper().getLocalInterceptor();
               if (this.isWorkContextEmpty(var3) || !HttpServer.webAppContainer.isWorkContextPropagationEnabled()) {
                  return;
               }

               SessionInternal var4 = (SessionInternal)var2.getSession(false);
               if (var4 != null || !var2.getResponse().isCommitted()) {
                  if (var1.getVersionId() == null) {
                     if (var4 == null) {
                        var4 = (SessionInternal)var2.getSession(true);
                     }
                  } else if (var4 == null) {
                     ApplicationVersionUtils.removeAppWorkContextEntries();
                     if (this.isWorkContextEmpty(var3)) {
                        return;
                     }

                     var4 = (SessionInternal)var2.getSession(true);
                  } else if (!var4.hasStateAttributes()) {
                     ApplicationVersionUtils.removeAppWorkContextEntries();
                  }

                  byte[] var5 = this.getWorkContextsByteArray(var3);
                  if (var5 == null) {
                     return;
                  }

                  String var6 = var2.getSessionHelper().getSessionID();
                  this.csidWorkContextMap.put(var6, var5);
                  var4.setInternalAttribute("weblogic.workContexts", var5);
                  if (HTTPDebugLogger.isEnabled()) {
                     HTTPDebugLogger.debug("*** HttpServer.WorkContextManager.copyThreadContexts for CSID=" + var6 + (var1 == null ? "" : ", app=" + var1.getAppDisplayName()) + ", workCtxs=" + ApplicationVersionUtils.getDebugWorkContexts() + ", session=" + var4);
                  }

                  return;
               }
            } catch (IOException var10) {
               HTTPLogger.logFailedToSaveWorkContexts(var2.toString(), var10);
               return;
            } finally {
               if (var1.isAdminMode()) {
                  ApplicationVersionUtils.setCurrentAdminMode(false);
               }

            }

         }
      }

      private boolean isWorkContextEmpty(WorkContextMapInterceptor var1) {
         return var1 == null || !((WorkContextMap)var1).isPropagationModePresent(128);
      }

      private byte[] getWorkContextsByteArray(WorkContextMapInterceptor var1) throws IOException {
         ByteArrayOutputStream var2 = new ByteArrayOutputStream();
         ObjectOutputStream var3 = new ObjectOutputStream(var2);

         try {
            WorkContextOutputAdapter var4 = new WorkContextOutputAdapter(var3);
            var1.sendRequest(var4, 128);
         } finally {
            var3.flush();
            var3.close();
         }

         return var2.toByteArray();
      }

      private void restoreWorkContexts(byte[] var1) throws IOException {
         WorkContextMapInterceptor var2 = WorkContextHelper.getWorkContextHelper().getInterceptor();
         if (var2 != null) {
            ByteArrayInputStream var3 = new ByteArrayInputStream(var1);
            ObjectInputStream var4 = new ObjectInputStream(var3);

            try {
               WorkContextInputAdapter var5 = new WorkContextInputAdapter(var4);
               var2.receiveRequest(var5);
            } finally {
               try {
                  var4.close();
               } catch (IOException var11) {
               }

            }

         }
      }

      public void updateWorkContexts(String var1, byte[] var2) {
         this.csidWorkContextMap.put(var1, var2);
         if (HTTPDebugLogger.isEnabled()) {
            HTTPDebugLogger.debug("*** HttpServer.WorkContextManager.updateThreadContexts for CSID=" + var1 + ", workCtxs=" + var2);
         }

      }

      private void removeWorkContext(String var1) {
         this.csidWorkContextMap.remove(var1);
      }

      private boolean isThreadContextIgnorable(ServletRequestImpl var1) {
         return var1.getServletStub().isFileServlet() || var1.getServletStub().isClasspathServlet() || var1.getServletStub().isProxyServlet() || var1.getServletStub().isPubSubControllerServlet();
      }
   }

   public static final class SessionLogin {
      private final Map webapps;
      private final Map authUsers;
      private final Map authCookies;
      private final WorkContextManager workCtxManager;

      private SessionLogin(WorkContextManager var1) {
         this.webapps = new ConcurrentHashMap();
         this.authUsers = new ConcurrentHashMap();
         this.authCookies = new ConcurrentHashMap();
         this.workCtxManager = var1;
      }

      public AuthenticatedSubject getUser(String var1) {
         return (AuthenticatedSubject)this.authUsers.get(var1);
      }

      public void setUser(String var1, AuthenticatedSubject var2) {
         this.authUsers.put(var1, var2);
      }

      private void removeUser(String var1) {
         this.authUsers.remove(var1);
         this.authCookies.remove(var1);
      }

      public void register(String var1, String var2) {
         HashSet var3 = (HashSet)this.webapps.get(var1);
         if (var3 == null) {
            var3 = new HashSet();
            this.webapps.put(var1, var3);
         }

         var3.add(var2);
      }

      public void unregister(String var1, String var2) {
         Set var3 = (Set)this.webapps.get(var1);
         if (var3 != null) {
            var3.remove(var2);
         }

         if (var3 == null || var3.size() == 0) {
            this.removeUser(var1);
            this.webapps.remove(var1);
            this.workCtxManager.removeWorkContext(var1);
         }

      }

      public void unregister(String var1) {
         this.removeUser(var1);
         this.webapps.remove(var1);
         this.workCtxManager.removeWorkContext(var1);
      }

      public Set getAllIds() {
         HashSet var1 = new HashSet(this.webapps.keySet());
         return var1;
      }

      public void addCookieId(String var1, String var2) {
         this.authCookies.put(var1, var2);
      }

      public String getCookieId(String var1) {
         return (String)this.authCookies.get(var1);
      }

      // $FF: synthetic method
      SessionLogin(WorkContextManager var1, Object var2) {
         this(var1);
      }
   }

   public final class Replicator {
      private final Map sessionIDToROIDPrimaryMap = new ConcurrentHashMap(101);
      private final Map sessionIDToROIDSecondaryMap = new ConcurrentHashMap(101);
      private final Map secondaryROIDMap = new WeakConcurrentHashMap(101);

      public void updateROIDLastAccessTimes(String var1, ROID[] var2, long[] var3, String var4) {
         try {
            ROIDLookup var5 = this.getROIDLookup(var1);
            if (var5 == null) {
               return;
            }

            var5.updateLastAccessTimes(var2, var3, System.currentTimeMillis(), var4);
         } catch (RemoteException var6) {
            HTTPLogger.logFailedToPerformBatchedLATUpdate(HttpServer.this.getName(), (String)null, var1, 0, 0, var6);
         }

      }

      public ROID lookupROID(String var1, String var2, String var3, String var4) throws RemoteException {
         return this.lookupROID(var1, var2, var3, var4, false);
      }

      public ROID lookupROID(final String var1, String var2, final String var3, final String var4, boolean var5) throws RemoteException {
         if (!var5) {
            WebAppServletContext[] var6 = HttpServer.this.getServletContextManager().getAllContexts();
            if (var6 == null) {
               return null;
            }

            for(int var7 = 0; var7 < var6.length; ++var7) {
               SessionContext var8 = var6[var7].getSessionContext();
               if (var8.getPersistentStoreType() == "replicated" && var8.getConfigMgr().getCookieName().equals(var3) && var8.getConfigMgr().getCookiePath().equals(var4)) {
                  ReplicatedSessionContext var9 = (ReplicatedSessionContext)var8;
                  if (var9.getROID(var1) != null) {
                     return null;
                  }
               }
            }
         }

         final ROIDLookup var11 = this.getROIDLookup(var2);
         if (var11 == null) {
            return null;
         } else {
            AuthenticatedSubject var12 = SecurityServiceManager.getCurrentSubject(HttpServer.kernelId);
            if (var12 != null && SubjectUtils.isUserAnAdministrator(var12)) {
               try {
                  Object var13 = SecurityServiceManager.runAs(HttpServer.kernelId, SubjectUtils.getAnonymousSubject(), new PrivilegedExceptionAction() {
                     public Object run() throws RemoteException {
                        return var11.lookupROID(var1, var3, var4);
                     }
                  });
                  return (ROID)var13;
               } catch (PrivilegedActionException var10) {
                  throw (RemoteException)var10.getException();
               }
            } else {
               return var11.lookupROID(var1, var3, var4);
            }
         }
      }

      public void putPrimary(String var1, ROID var2, String var3) {
         this.removeSecondary(var1, var3);
         SessionIDToROIDMapHolder var4 = (SessionIDToROIDMapHolder)this.sessionIDToROIDPrimaryMap.get(var1);
         if (var4 == null) {
            var4 = new SessionIDToROIDMapHolder(var2);
            this.sessionIDToROIDPrimaryMap.put(var1, var4);
         }

         var4.add(var3);
      }

      public void putSecondary(String var1, ROID var2, String var3) {
         this.removePrimary(var1, var3);
         SessionIDToROIDMapHolder var4 = (SessionIDToROIDMapHolder)this.sessionIDToROIDSecondaryMap.get(var1);
         if (var4 == null) {
            var4 = new SessionIDToROIDMapHolder(var2);
            this.sessionIDToROIDSecondaryMap.put(var1, var4);
         }

         var4.add(var3);
         this.secondaryROIDMap.put(var2, (Object)null);
      }

      public ROID getPrimary(String var1) {
         SessionIDToROIDMapHolder var2 = (SessionIDToROIDMapHolder)this.sessionIDToROIDPrimaryMap.get(var1);
         return var2 == null ? null : var2.getROID();
      }

      public ROID getSecondary(String var1) {
         SessionIDToROIDMapHolder var2 = (SessionIDToROIDMapHolder)this.sessionIDToROIDSecondaryMap.get(var1);
         return var2 == null ? null : var2.getROID();
      }

      public void removePrimary(String var1, String var2) {
         SessionIDToROIDMapHolder var3 = (SessionIDToROIDMapHolder)this.sessionIDToROIDPrimaryMap.get(var1);
         if (var3 != null && var3.remove(var2)) {
            this.sessionIDToROIDPrimaryMap.remove(var1);
         }

      }

      public void removeSecondary(String var1, String var2) {
         SessionIDToROIDMapHolder var3 = (SessionIDToROIDMapHolder)this.sessionIDToROIDSecondaryMap.get(var1);
         if (var3 != null && var3.remove(var2)) {
            this.sessionIDToROIDSecondaryMap.remove(var1);
            this.secondaryROIDMap.remove(var3.getROID());
         }

      }

      public ROID[] getSecondaryIds() {
         ROID[] var1 = null;
         synchronized(this.secondaryROIDMap) {
            int var3 = this.secondaryROIDMap.size();
            if (var3 < 1) {
               return new ROID[0];
            } else {
               var1 = new ROID[var3];
               this.secondaryROIDMap.keySet().toArray(var1);
               return var1;
            }
         }
      }

      private ROIDLookup getROIDLookup(String var1) {
         try {
            Environment var2 = new Environment();
            var2.setProviderUrl(var1);
            var2.setProviderChannel(HttpServer.this.replicationChannel);
            return (ROIDLookup)var2.getInitialReference(ROIDLookupImpl.class);
         } catch (Throwable var3) {
            return null;
         }
      }
   }
}
