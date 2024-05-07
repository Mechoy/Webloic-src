package weblogic.servlet.internal;

import com.bea.wls.redef.ClassRedefinerFactory;
import com.bea.wls.redef.RedefiningClassLoader;
import com.bea.wls.redef.runtime.ClassRedefinitionRuntimeImpl;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;
import javax.management.InvalidAttributeValueException;
import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import weblogic.application.AdminModeCompletionBarrier;
import weblogic.application.AppClassLoaderManager;
import weblogic.application.ApplicationContext;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.ApplicationFileManager;
import weblogic.application.ConcurrentModule;
import weblogic.application.MergedDescriptorModule;
import weblogic.application.Module;
import weblogic.application.ModuleException;
import weblogic.application.ModuleLocationInfo;
import weblogic.application.ParentModule;
import weblogic.application.UpdateListener;
import weblogic.application.internal.BaseJ2EEModule;
import weblogic.application.io.DescriptorFinder;
import weblogic.application.utils.AppFileOverrideUtils;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.application.utils.CompositeWebAppFinder;
import weblogic.application.utils.EarUtils;
import weblogic.application.utils.TargetUtils;
import weblogic.cacheprovider.coherence.CoherenceClusterManager;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.deployment.AbstractPersistenceUnitRegistry;
import weblogic.deployment.EnvironmentException;
import weblogic.deployment.ModulePersistenceUnitRegistry;
import weblogic.deployment.PersistenceUnitInfoImpl;
import weblogic.deployment.PersistenceUnitRegistry;
import weblogic.deployment.PersistenceUnitRegistryInitializer;
import weblogic.deployment.PersistenceUnitRegistryProvider;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorManager;
import weblogic.descriptor.DescriptorUpdateFailedException;
import weblogic.descriptor.DescriptorUpdateRejectedException;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.j2ee.J2EEApplicationRuntimeMBeanImpl;
import weblogic.j2ee.descriptor.ParamValueBean;
import weblogic.j2ee.descriptor.ServletBean;
import weblogic.j2ee.descriptor.WebAppBean;
import weblogic.j2ee.descriptor.wl.CoherenceClusterRefBean;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.FastSwapBean;
import weblogic.j2ee.descriptor.wl.JspDescriptorBean;
import weblogic.j2ee.descriptor.wl.LibraryRefBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.kodo.monitoring.KodoPersistenceUnitParent;
import weblogic.logging.Loggable;
import weblogic.management.DeploymentException;
import weblogic.management.DistributedManagementException;
import weblogic.management.ManagementException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.ComponentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.NetworkAccessPointMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.configuration.VirtualHostMBean;
import weblogic.management.configuration.WebAppComponentMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.management.runtime.ComponentRuntimeMBean;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.management.runtime.WebAppComponentRuntimeMBean;
import weblogic.management.runtime.WorkManagerRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.servlet.HTTPLogger;
import weblogic.servlet.internal.dd.glassfish.GlassFishWebAppParser;
import weblogic.servlet.internal.session.GracefulShutdownHelper;
import weblogic.utils.Debug;
import weblogic.utils.FileUtils;
import weblogic.utils.NestedException;
import weblogic.utils.PlatformConstants;
import weblogic.utils.StringUtils;
import weblogic.utils.application.WarDetector;
import weblogic.utils.classloaders.Annotation;
import weblogic.utils.classloaders.ChangeAwareClassLoader;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.classloaders.MultiClassFinder;
import weblogic.utils.classloaders.Source;
import weblogic.utils.http.HttpParsing;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;
import weblogic.work.ShutdownCallback;
import weblogic.work.WorkManagerRuntimeMBeanImpl;
import weblogic.work.WorkManagerService;

public class WebAppModule extends BaseJ2EEModule implements Module, ModuleLocationInfo, ParentModule, UpdateListener, MergedDescriptorModule, PersistenceUnitRegistryProvider, ConcurrentModule {
   public static final String WEB_INF = "WEB-INF";
   public static final String WEB_XML_PATH = "WEB-INF/web.xml";
   public static final String WEBLOGIC_XML_PATH = "WEB-INF/weblogic.xml";
   public static final String APP_XML = "META-INF/application.xml";
   public static final String META_INF = "META-INF";
   public static final String PERSISTENCE_XML_PATH = "META-INF/persistence.xml";
   public static final String PERSISTENCE_CONFIGURATION_XML_PATH = "META-INF/persistence-configuration.xml";
   public static final String ROUTING_HANDLER_INIT_PARAM_NAME = "RoutingHandlerClassName";
   public static final String WSEE_SOAP_ROUTING_HANDLER_CLASS_NAME = "weblogic.wsee.jaxws.cluster.proxy.SOAPRoutingHandler";
   public static final String WSEE_PROXY_HTTP = "weblogic-wsee-proxy-channel-http";
   public static final String WSEE_PROXY_HTTPS = "weblogic-wsee-proxy-channel-https";
   private static final DebugLogger DEBUG = DebugLogger.getDebugLogger("DebugWebAppModule");
   private static final AppClassLoaderManager appClassLoaderManager = AppClassLoaderManager.getAppClassLoaderManager();
   private final String moduleURI;
   private final Map allContexts = new HashMap();
   private final String contextRootFromAppDD;
   private final ArrayList tmpDirs = new ArrayList();
   private final ArrayList archives = new ArrayList();
   private WebAppComponentMBean mbean;
   private WebAppBean webappBean;
   private WeblogicWebAppBean wlWebAppBean;
   private AppDeploymentMBean dmb;
   private ApplicationFileManager appFileManager;
   private ApplicationContextInternal appCtx;
   private File altDDFile;
   private WebAppInternalParser webAppParser;
   private GenericClassLoader parentClassLoader;
   private GenericClassLoader webClassLoader;
   private MultiClassFinder finder;
   private VirtualJarFile virtualJarFile;
   private boolean createdClassLoader;
   private boolean isSuspended = false;
   private boolean allowServletReload = true;
   private boolean webappsDestroyed = false;
   private boolean hasWebDescriptorFile = true;
   private ModuleException alreadyRegisteredException = null;
   private PersistenceUnitRegistry persistenceUnitRegistry;
   private PersistenceUnitRegistry proposedPersistenceUnitRegistry;
   private static Set redefinableWebApps = initRedefinableWebApps();
   private int fastSwapRefreshInterval;
   private boolean registerFastSwapFilter;

   public WebAppModule(String var1, String var2) {
      this.moduleURI = var1;
      var2 = EarUtils.fixAppContextRoot(var2);
      if (var2 != null) {
         var2 = WarDetector.instance.stem(var2);
      }

      this.contextRootFromAppDD = var2;
   }

   private void initClassFinder() throws ModuleException {
      this.finder = new MultiClassFinder();
      ClassFinder var1 = AppFileOverrideUtils.getFinderIfRequired(this.appCtx.getAppDeploymentMBean(), this.moduleURI);
      if (var1 != null) {
         this.finder.addFinder(var1);
      }

   }

   private void initClassLoader() {
      this.webClassLoader = new ChangeAwareClassLoader(this.finder, false, this.parentClassLoader);
      this.webClassLoader.setAnnotation(new Annotation(this.appCtx.getAppDeploymentMBean().getApplicationIdentifier(), this.normalizeId(this.getId(), this.moduleURI)));
   }

   private GenericClassLoader init(ApplicationContext var1, GenericClassLoader var2, UpdateListener.Registration var3, boolean var4) throws ModuleException {
      this.appCtx = (ApplicationContextInternal)var1;
      this.parentClassLoader = var2;
      var3.addUpdateListener(this);
      this.createdClassLoader = var4;
      this.initClassFinder();
      if (var4) {
         this.initClassLoader();
      } else {
         this.webClassLoader = var2;
         this.webClassLoader.addClassFinder(this.finder);
      }

      this.initJNDIContext();
      this.appFileManager = this.appCtx.getApplicationFileManager();
      return this.webClassLoader;
   }

   private void initMBeans() throws ModuleException {
      this.dmb = this.appCtx.getAppDeploymentMBean();
      ComponentMBean var1 = this.findComponentMBeanInternal(this.appCtx, this.moduleURI, WebAppComponentMBean.class);
      if (var1 instanceof WebAppComponentMBean) {
         this.mbean = (WebAppComponentMBean)var1;
      }

   }

   public TargetMBean[] getTargets() {
      return this.dmb.getTargets();
   }

   private void initJNDIContext() throws ModuleException {
      Context var1 = this.appCtx.getEnvContext();

      try {
         var1.lookup("/webapp");
      } catch (NameNotFoundException var5) {
         try {
            var1.createSubcontext("webapp");
         } catch (NamingException var4) {
            throw new ModuleException(var4);
         }
      } catch (NamingException var6) {
         throw new ModuleException(var6);
      }

   }

   public String getDescriptorURI() {
      return "WEB-INF";
   }

   public void initUsingLoader(ApplicationContext var1, GenericClassLoader var2, UpdateListener.Registration var3) throws ModuleException {
      this.init(var1, var2, var3, false);
   }

   public GenericClassLoader init(ApplicationContext var1, GenericClassLoader var2, UpdateListener.Registration var3) throws ModuleException {
      return this.init(var1, var2, var3, true);
   }

   public String getModuleURI() {
      return this.moduleURI;
   }

   public String getId() {
      return this.contextRootFromAppDD != null ? this.contextRootFromAppDD : this.moduleURI;
   }

   public String getType() {
      return WebLogicModuleType.MODULETYPE_WAR;
   }

   public GenericClassLoader getClassLoader() {
      return this.webClassLoader;
   }

   public String getContextRoot() {
      return this.contextRootFromAppDD;
   }

   public ComponentRuntimeMBean[] getComponentRuntimeMBeans() {
      int var1 = this.allContexts.size();
      ComponentRuntimeMBean[] var2 = new ComponentRuntimeMBean[var1];
      Iterator var3 = this.allContexts.values().iterator();

      for(int var4 = 0; var3.hasNext(); ++var4) {
         WebAppServletContext var5 = (WebAppServletContext)var3.next();
         var2[var4] = var5.getRuntimeMBean();
      }

      return var2;
   }

   public DescriptorBean[] getDescriptors() {
      if (this.webappBean != null && this.wlWebAppBean != null) {
         return new DescriptorBean[]{(DescriptorBean)this.webappBean, (DescriptorBean)this.wlWebAppBean};
      } else if (this.webappBean != null) {
         return new DescriptorBean[]{(DescriptorBean)this.webappBean};
      } else {
         return this.wlWebAppBean != null ? new DescriptorBean[]{(DescriptorBean)this.wlWebAppBean} : new DescriptorBean[0];
      }
   }

   public void prepare() throws ModuleException {
      this.alreadyRegisteredException = null;
      this.allContexts.clear();
      this.webappsDestroyed = false;
      this.initPersistenceUnitRegistry();
      this.initMBeans();
      if (DEBUG.isDebugEnabled()) {
         DEBUG.debug(HTTPLogger.logPreparingLoggable(this.getAppDisplayName(), this.getId()).getMessage());
      }

      this.allowServletReload = this.webClassLoader instanceof ChangeAwareClassLoader;
      this.loadDescriptor();
      this.loadFastSwapClassloader();
      boolean var1 = false;

      try {
         if (this.wlWebAppBean != null && this.appCtx != null) {
            this.appCtx.getWorkManagerCollection().populate(this.getId(), this.wlWebAppBean);
         }

         if (this.isDeployedLocally()) {
            this.registerWebApp(WebService.defaultHttpServer());
         }

         this.deployOnVirtualHosts();
         this.addWorkManagerRuntimes(this.appCtx.getWorkManagerCollection().getWorkManagers(this.getId()));
         this.initPersistenceMBean();
         this.setupCoherenceCaches();
         var1 = true;
      } catch (DeploymentException var9) {
         throw new ModuleException(var9.getMessage(), var9);
      } catch (ManagementException var10) {
         throw new ModuleException(var10.getMessage(), var10);
      } catch (EnvironmentException var11) {
         throw new ModuleException(var11);
      } finally {
         if (!var1) {
            this.closeVirtualJarFile();
            this.destroyContexts();
         }

      }

   }

   private void initPersistenceUnitRegistry() throws ModuleException {
      PersistenceUnitRegistryInitializer var1 = PersistenceUnitRegistryInitializer.getInstance(this.appCtx);
      var1.setupPersistenceUnitRegistries();
   }

   protected void initPersistenceMBean() throws EnvironmentException {
      AbstractPersistenceUnitRegistry var1 = (AbstractPersistenceUnitRegistry)this.getPersistenceUnitRegistry();
      ComponentRuntimeMBean[] var2 = this.getComponentRuntimeMBeans();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var2[var3] instanceof KodoPersistenceUnitParent) {
            var1.setParentRuntimeMBean((KodoPersistenceUnitParent)var2[var3]);
         }
      }

   }

   public void activate() throws IllegalStateException, ModuleException {
      if (DEBUG.isDebugEnabled()) {
         DEBUG.debug(HTTPLogger.logActivatingLoggable(this.getAppDisplayName(), this.getId()).getMessage());
      }

      if (this.webappsDestroyed || this.alreadyRegisteredException != null) {
         this.prepare();
      }

      if (this.alreadyRegisteredException != null) {
         this.unprepare();
         throw this.alreadyRegisteredException;
      } else {
         this.activateContexts();
         this.registerBeanUpdateListeners();
         this.reconfigPersistenceUnits();
         if (DEBUG.isDebugEnabled()) {
            DEBUG.debug(HTTPLogger.logReadyLoggable(this.getAppDisplayName(), this.getId()).getMessage());
         }

      }
   }

   private void reconfigPersistenceUnits() throws ModuleException {
      PersistenceUnitRegistry var1 = this.getPersistenceUnitRegistry();
      if (var1 != null) {
         Collection var2 = var1.getPersistenceUnitNames();
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            PersistenceUnitInfoImpl var5 = var1.getPersistenceUnit(var4);

            try {
               var5.activate(this.appCtx.getEnvContext());
            } catch (EnvironmentException var7) {
               throw new ModuleException("Error activating JPA deployment:", var7);
            }
         }

      }
   }

   public void start() throws ModuleException {
      if (DEBUG.isDebugEnabled()) {
         DEBUG.debug(HTTPLogger.logStartingLoggable(this.getAppDisplayName(), this.getId()).getMessage());
      }

      this.startContexts();
      if (this.dmb != null && this.dmb.getOnDemandContextPaths() != null && this.dmb.getOnDemandContextPaths().length > 0) {
         if (DEBUG.isDebugEnabled()) {
            DEBUG.debug("WebAppModule.activate: " + this.toString() + " unregister context paths ");
         }

         OnDemandManager var1 = WebService.defaultHttpServer().getOnDemandManager();
         var1.unregisterOnDemandContextPaths(this.dmb.getOnDemandContextPaths());
      }

   }

   public void deactivate() throws IllegalStateException, ModuleException {
      if (DEBUG.isDebugEnabled()) {
         DEBUG.debug(HTTPLogger.logDeactivatingLoggable(this.getAppDisplayName(), this.getId()).getMessage());
      }

      this.releaseCoherenceCaches();
      this.stopContexts();
      this.unregisterBeanUpdateListeners();
      this.destroyContexts();
      if (this.allowServletReload) {
         this.initClassFinder();
         this.initClassLoader();
         appClassLoaderManager.addModuleLoader(this.webClassLoader, this.getId());
      }

   }

   public void unprepare() throws IllegalStateException, ModuleException {
      if (DEBUG.isDebugEnabled()) {
         DEBUG.debug(HTTPLogger.logRollingBackLoggable(this.getAppDisplayName(), this.getId()).getMessage());
      }

      this.releaseCoherenceCaches();
      this.destroyContexts();
      if (this.appCtx != null) {
         this.appCtx.getWorkManagerCollection().removeModuleEntries(this.getId());
      }

      if (this.finder != null) {
         this.finder.close();
      }

      this.closeVirtualJarFile();
      this.closePersistenceUnitRegistry();
      Jdk6.clearCache(this.webClassLoader);
      this.cleanGeneratedJspClasses();
   }

   public void remove() throws ModuleException, IllegalStateException {
      if (DEBUG.isDebugEnabled()) {
         DEBUG.debug(HTTPLogger.logRemovingLoggable(this.getAppDisplayName(), this.moduleURI).getMessage());
      }

      if (this.appCtx != null) {
         this.appCtx.getWorkManagerCollection().removeModuleEntries(this.getId());
      }

      if (this.archives != null && this.archives.size() > 0) {
         Iterator var1 = this.archives.iterator();

         while(var1.hasNext()) {
            War var2 = (War)var1.next();
            var2.remove();
         }

         this.archives.clear();
      }

      this.cleanupTempDirs();
      this.removeSavedSessionState();
      this.allContexts.clear();
   }

   public void destroy(UpdateListener.Registration var1) throws ModuleException {
      if (this.createdClassLoader) {
         if (this.webClassLoader != null) {
            this.webClassLoader.close();
         }

         this.createdClassLoader = false;
      }

      this.finder = null;
      this.webClassLoader = null;
      var1.removeUpdateListener(this);
      this.closeVirtualJarFile();
   }

   public boolean acceptURI(String var1) {
      if (!var1.endsWith(".class") && !var1.endsWith(".jar")) {
         if (!this.appCtx.isEar()) {
            return this.acceptModuleUri(this.appCtx, this.moduleURI, var1);
         } else {
            boolean var2 = this.acceptModuleUri(this.appCtx, this.moduleURI, var1);
            if (!var2) {
               String var3 = this.appCtx.getStagingPath();
               String var4 = var3 + "/" + var1;
               Iterator var5 = this.allContexts.values().iterator();

               while(var5.hasNext()) {
                  WebAppServletContext var6 = (WebAppServletContext)var5.next();
                  War var7 = var6.getWarInstance();
                  if (var7.isKnownVirtualMappingUri(var4)) {
                     return true;
                  }
               }
            }

            return var2;
         }
      } else {
         return false;
      }
   }

   public void adminToProduction() {
      this.setSuspended(false);
   }

   public void forceProductionToAdmin() throws ModuleException {
      this.setSuspended(true);
      GracefulShutdownHelper.notifyGracefulProductionToAdmin(this.appCtx.getApplicationId(), this);
   }

   public void gracefulProductionToAdmin(AdminModeCompletionBarrier var1) throws ModuleException {
      ServerRuntimeMBean var2 = ManagementService.getRuntimeAccess(WebAppConfigManager.KERNEL_ID).getServerRuntime();
      if (var2.getStateVal() != 4 && var2.getStateVal() != 7 && !this.isSuspended()) {
         boolean var3 = ApplicationVersionUtils.getIgnoreSessionsAppCtxParam(this.appCtx);
         if (DEBUG.isDebugEnabled()) {
            DEBUG.debug("WebAppModule.gracefulProductionToAdmin: " + this.toString() + " received for " + this.moduleURI + ", ignoreSessions=" + var3);
         }

         ShutdownCallback var4 = var1.registerWMShutdown();
         GracefulShutdownHelper.waitForPendingSessions(this.appCtx.getApplicationId(), this, var3);
         this.setSuspended(true);
         var4.completed();
      }
   }

   public void prepareUpdate(String var1) throws ModuleException {
      if (DEBUG.isDebugEnabled()) {
         DEBUG.debug("WebAppModule: " + this.toString() + " received prepareUpdate for " + var1);
      }

      String var2 = this.unmangle(this.appCtx, this.moduleURI, var1);
      this.checkForDescriptorUpdate(var2);
      Iterator var3 = this.allContexts.values().iterator();

      while(var3.hasNext()) {
         WebAppServletContext var4 = (WebAppServletContext)var3.next();
         var4.precompileJspsOnUpdate(var2);
      }

   }

   public void activateUpdate(String var1) throws ModuleException {
   }

   public void rollbackUpdate(String var1) {
   }

   public String toString() {
      return "WebAppModule(" + this.getAppDisplayName() + ":" + this.getId() + ")";
   }

   boolean isServletReloadAllowed() {
      return this.allowServletReload;
   }

   public synchronized boolean isSuspended() {
      return this.isSuspended;
   }

   public synchronized void setSuspended(boolean var1) {
      this.isSuspended = var1;
   }

   private void registerBeanUpdateListeners() {
      if (this.wlWebAppBean != null) {
         Iterator var1 = this.allContexts.values().iterator();

         while(var1.hasNext()) {
            WebAppServletContext var2 = (WebAppServletContext)var1.next();
            BeanUpdateListener var3 = var2.getSessionContext().getConfigMgr().getBeanUpdateListener();
            ((DescriptorBean)this.wlWebAppBean).addBeanUpdateListener(var3);
            DescriptorBean var4;
            if (this.wlWebAppBean.getSessionDescriptors() != null && this.wlWebAppBean.getSessionDescriptors().length > 0) {
               var4 = (DescriptorBean)this.wlWebAppBean.getSessionDescriptors()[0];
               var4.addBeanUpdateListener(var3);
            }

            var3 = var2.getJSPManager().getBeanUpdateListener();
            ((DescriptorBean)this.wlWebAppBean).addBeanUpdateListener(var3);
            if (this.wlWebAppBean.getJspDescriptors() != null && this.wlWebAppBean.getJspDescriptors().length > 0) {
               var4 = (DescriptorBean)this.wlWebAppBean.getJspDescriptors()[0];
               var4.addBeanUpdateListener(var3);
            }

            var3 = var2.getConfigManager().getBeanUpdateListener();
            ((DescriptorBean)this.wlWebAppBean).addBeanUpdateListener(var3);
            if (this.wlWebAppBean.getContainerDescriptors() != null && this.wlWebAppBean.getContainerDescriptors().length > 0) {
               var4 = (DescriptorBean)this.wlWebAppBean.getContainerDescriptors()[0];
               var4.addBeanUpdateListener(var3);
            }
         }

      }
   }

   private void unregisterBeanUpdateListeners() {
      if (this.wlWebAppBean != null) {
         Iterator var1 = this.allContexts.values().iterator();

         while(var1.hasNext()) {
            WebAppServletContext var2 = (WebAppServletContext)var1.next();
            BeanUpdateListener var3 = var2.getSessionContext().getConfigMgr().getBeanUpdateListener();
            ((DescriptorBean)this.wlWebAppBean).removeBeanUpdateListener(var3);
            DescriptorBean var4;
            if (this.wlWebAppBean.getSessionDescriptors() != null && this.wlWebAppBean.getSessionDescriptors().length > 0) {
               var4 = (DescriptorBean)this.wlWebAppBean.getSessionDescriptors()[0];
               var4.removeBeanUpdateListener(var3);
            }

            var3 = var2.getJSPManager().getBeanUpdateListener();
            ((DescriptorBean)this.wlWebAppBean).removeBeanUpdateListener(var3);
            if (this.wlWebAppBean.getJspDescriptors() != null && this.wlWebAppBean.getJspDescriptors().length > 0) {
               var4 = (DescriptorBean)this.wlWebAppBean.getJspDescriptors()[0];
               var4.removeBeanUpdateListener(var3);
            }

            var3 = var2.getConfigManager().getBeanUpdateListener();
            ((DescriptorBean)this.wlWebAppBean).removeBeanUpdateListener(var3);
            if (this.wlWebAppBean.getContainerDescriptors() != null && this.wlWebAppBean.getContainerDescriptors().length > 0) {
               var4 = (DescriptorBean)this.wlWebAppBean.getContainerDescriptors()[0];
               var4.removeBeanUpdateListener(var3);
            }
         }

      }
   }

   private void cleanupTempDirs() {
      if (this.tmpDirs != null && this.tmpDirs.size() >= 1) {
         File var1 = null;
         Iterator var2 = this.tmpDirs.iterator();

         while(var2.hasNext()) {
            File var3 = (File)var2.next();
            FileUtils.remove(var3);
            if (var1 == null) {
               var1 = var3.getParentFile();
            }
         }

         this.tmpDirs.clear();
         if (var1 != null) {
            FileUtils.remove(var1);
         }

      }
   }

   private void deployOnVirtualHosts() throws ModuleException {
      VirtualHostMBean[] var1 = this.resolveWebServers();
      if (var1 != null) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            HttpServer var3 = WebService.getHttpServer(var1[var2].getName());
            if (var3 != null && var3 != WebService.defaultHttpServer()) {
               this.registerWebApp(var3);
            }
         }

      }
   }

   private void registerWebApp(HttpServer var1) throws ModuleException {
      String var2 = this.initAndValidateContextPath(var1);
      if (this.alreadyRegisteredException == null) {
         this.allContexts.put(var1, var1.loadWebApp(this.mbean, this.appCtx, this, var2));
         if (DEBUG.isDebugEnabled()) {
            DEBUG.debug("registered " + var2 + " with " + var1);
         }

         this.checkWseeRoutingHandler();
      }
   }

   private boolean checkWseeRoutingHandler() throws ModuleException {
      if (!this.isWseeClusterRoutingHandlerConfigured()) {
         return true;
      } else {
         DomainMBean var1 = this.getDomain();
         ServerMBean[] var2 = var1.getServers();
         ServerMBean[] var3 = var2;
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            ServerMBean var6 = var3[var5];
            NetworkAccessPointMBean[] var7 = var6.getNetworkAccessPoints();
            NetworkAccessPointMBean[] var8 = var7;
            int var9 = var7.length;

            for(int var10 = 0; var10 < var9; ++var10) {
               NetworkAccessPointMBean var11 = var8[var10];
               String var12 = var11.getName();
               if (var12.equals("weblogic-wsee-proxy-channel-http") || var12.equals("weblogic-wsee-proxy-channel-https")) {
                  return true;
               }
            }
         }

         throw new ModuleException("If a deployed (Cluster) Servlet has an  <init-param> with <param-name> = RoutingHandlerClassName and <param-value> = weblogic.wsee.jaxws.cluster.proxy.SOAPRoutingHandler in its <web.xml> file, Then the cluster domain must contain at least one <server> that is configured with a <network-access-point> named either 'weblogic-wsee-proxy-channel-http' or 'weblogic-wsee-proxy-channel-https'.  But neither has been found.  This is a deployment Error.");
      }
   }

   private boolean isWseeClusterRoutingHandlerConfigured() {
      WebAppBean var1 = this.getWebAppBean();
      ServletBean[] var2 = this.getWebAppBean().getServlets();
      ServletBean[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         ServletBean var6 = var3[var5];
         ParamValueBean var7 = var6.lookupInitParam("RoutingHandlerClassName");
         if (var7 != null && var7.getParamValue().equals("weblogic.wsee.jaxws.cluster.proxy.SOAPRoutingHandler")) {
            return true;
         }
      }

      return false;
   }

   private DomainMBean getDomain() {
      RuntimeAccess var1 = ManagementService.getRuntimeAccess(getKernelId());
      return var1.getDomain();
   }

   private void removeSavedSessionState() {
      Iterator var1 = this.allContexts.keySet().iterator();

      while(var1.hasNext()) {
         HttpServer var2 = (HttpServer)var1.next();
         var2.getServletContextManager().removeSavedSessionState(((WebAppServletContext)this.allContexts.get(var2)).getContextPath());
      }

   }

   private void checkForDescriptorUpdate(String var1) throws ModuleException {
      if ("WEB-INF/web.xml".equals(var1) || "WEB-INF/weblogic.xml".equals(var1)) {
         this.prepareUpdateWebAppBean();
      }

   }

   private void prepareUpdateWebAppBean() throws ModuleException {
      try {
         this.virtualJarFile = this.createVirtualJarFile();
      } catch (IOException var19) {
         throw new ModuleException(var19.getMessage(), var19);
      }

      WebAppParser var1 = this.getWebAppParser(this.virtualJarFile, this.appCtx.findDeploymentPlan());
      if (var1 == null) {
         this.closeVirtualJarFile();
      } else {
         WebAppBean var2 = null;
         WeblogicWebAppBean var3 = null;

         try {
            Loggable var5;
            try {
               var2 = var1.getWebAppBean();
               var3 = var1.getWeblogicWebAppBean();
            } catch (IOException var16) {
               var5 = HTTPLogger.logErrorReadingWebAppLoggable(this.toString(), this.getWarPath(), var16);
               var5.log();
               this.createModuleException(var5.getMessage(), var16);
            } catch (Exception var17) {
               var5 = HTTPLogger.logLoadErrorLoggable(this.toString(), this.getWarPath(), var17);
               var5.log();
               this.createModuleException(var5.getMessage(), var17);
            }
         } finally {
            this.closeVirtualJarFile();
         }

         try {
            Descriptor var4 = null;
            Descriptor var20 = null;
            if (var2 != null && this.webappBean != null) {
               var4 = ((DescriptorBean)this.webappBean).getDescriptor();
               var4.prepareUpdate(((DescriptorBean)var2).getDescriptor());
            }

            if (var3 != null && this.wlWebAppBean != null) {
               var20 = ((DescriptorBean)this.wlWebAppBean).getDescriptor();
               var20.prepareUpdate(((DescriptorBean)var3).getDescriptor());
            }

            if (var4 != null) {
               var4.activateUpdate();
            }

            if (var20 != null) {
               var20.activateUpdate();
            }

         } catch (DescriptorUpdateRejectedException var14) {
            throw new ModuleException(var14);
         } catch (DescriptorUpdateFailedException var15) {
            throw new ModuleException(var15);
         }
      }
   }

   private String initAndValidateContextPath(HttpServer var1) throws ModuleException {
      String var2 = null;
      if ("console".equals(this.getId())) {
         var2 = HttpParsing.ensureStartingSlash(ManagementService.getRuntimeAccess(WebAppConfigManager.KERNEL_ID).getDomain().getConsoleContextPath());
      } else {
         var2 = this.contextRootFromAppDD;
         if (this.wlWebAppBean != null) {
            String var3 = this.wlWebAppBean.getContextRoots().length > 0 ? this.wlWebAppBean.getContextRoots()[0] : null;
            if (var2 != null && !this.isSynthesizedApplicationXml()) {
               if (this.wlWebAppBean.getContextRoots().length > 0) {
                  HTTPLogger.logIgnoringWeblogicXMLContextRoot(this.getAppDisplayName(), this.getId(), var2, var3);
               }
            } else {
               var2 = var3;
            }
         }

         if (var2 == null) {
            var2 = WarDetector.instance.stem(this.moduleURI);
         }
      }

      var2 = this.fixupContextPath(var2);
      var2 = this.substituteAppName(var2);
      if (var1.getMBean().getDefaultWebAppContextRoot() != null && var1.getMBean().getDefaultWebAppContextRoot().equals(var2)) {
         var2 = "";
      }

      WebAppServletContext var7 = var1.getServletContextManager().getContextForContextPath(var2, this.dmb.getVersionIdentifier());
      if (var7 != null) {
         if (var7.getWebAppModule() != null) {
            this.alreadyRegisteredException = new ModuleException("Context path '" + var2 + "' is already in use by the module: " + var7.getWebAppModule().getModuleName() + " application: " + var7.getWebAppModule().getAppDisplayName());
         } else {
            this.alreadyRegisteredException = new ModuleException("Context path '" + var2 + "' is already in use by the module: " + var7.getName());
         }
      }

      try {
         if (this.mbean != null) {
            this.mbean.setContextPath(var2);
         }
      } catch (DistributedManagementException var5) {
         HTTPLogger.logFailedToSetContextPath(this.getAppDisplayName(), this.getId(), var2, var5);
      } catch (InvalidAttributeValueException var6) {
         HTTPLogger.logFailedToSetContextPath(this.getAppDisplayName(), this.getId(), var2, var6);
      }

      if (DEBUG.isDebugEnabled()) {
         DEBUG.debug("Context Path = " + var2);
      }

      if (this.appCtx.getApplicationDD() != null) {
         EarUtils.handleUnsetContextRoot(this.moduleURI, var2, this.appCtx.getApplicationDD(), this.wlWebAppBean);
      }

      return var2;
   }

   private boolean isSynthesizedApplicationXml() throws ModuleException {
      VirtualJarFile var1 = null;

      boolean var2;
      try {
         if (this.appCtx != null && this.appCtx.getApplicationFileManager() != null) {
            var1 = this.appCtx.getApplicationFileManager().getVirtualJarFile();
            var2 = var1.getEntry("META-INF/application.xml") == null;
            return var2;
         }

         var2 = true;
      } catch (IOException var12) {
         throw new ModuleException(var12.getMessage(), var12);
      } finally {
         if (var1 != null) {
            try {
               var1.close();
            } catch (IOException var11) {
            }
         }

      }

      return var2;
   }

   private String fixupContextPath(String var1) {
      if (!var1.equals("/") && !var1.equals("")) {
         return var1.startsWith("/") ? var1 : "/" + var1;
      } else {
         return "";
      }
   }

   private String substituteAppName(String var1) {
      return var1.indexOf("${APPNAME}") == -1 ? var1 : StringUtils.replaceGlobal(var1, "${APPNAME}", ApplicationVersionUtils.getApplicationName(this.appCtx.getApplicationId()));
   }

   protected WebAppParser getWebAppParser(VirtualJarFile var1, DeploymentPlanBean var2) throws ModuleException {
      this.altDDFile = this.resolveAltDD(this.appCtx, this.moduleURI);
      File var3 = null;
      if (this.dmb.getPlanDir() != null) {
         var3 = new File(this.dmb.getLocalPlanDir());
      }

      WebAppDescriptor var4 = null;
      if (this.altDDFile != null) {
         var4 = new WebAppDescriptor(this.altDDFile, var1, var3, var2, this.moduleURI);
      } else {
         var4 = new WebAppDescriptor(var1, var3, var2, this.moduleURI);
      }

      if (this.appCtx != null && this.appCtx.getAppDeploymentMBean().isInternalApp() && ManagementService.getRuntimeAccess(WebAppConfigManager.KERNEL_ID).getDomain().isInternalAppsDeployOnDemandEnabled()) {
         var4.setValidateSchema(false);
      }

      return var4;
   }

   private void loadDescriptor() throws ModuleException {
      if (DEBUG.isDebugEnabled()) {
         DEBUG.debug(HTTPLogger.logLoadingDescriptorsLoggable(this.getAppDisplayName(), this.getId()).getMessage());
      }

      try {
         Loggable var2;
         try {
            this.virtualJarFile = this.createVirtualJarFile();

            try {
               WebAppParser var1 = this.getWebAppParser(this.virtualJarFile, this.dmb.getDeploymentPlanDescriptor());
               this.webappBean = var1.getWebAppBean();
               this.wlWebAppBean = var1.getWeblogicWebAppBean();
               if (this.wlWebAppBean == null) {
                  this.wlWebAppBean = GlassFishWebAppParser.getParser(this.virtualJarFile).getWeblogicWebAppBean();
               }

               if (var1 instanceof WebAppInternalParser) {
                  this.webAppParser = (WebAppInternalParser)var1;
                  this.hasWebDescriptorFile = this.webAppParser.hasWebDescriptorFile();
               }
            } catch (FileNotFoundException var11) {
            } catch (IOException var12) {
               throw new ModuleException(var12.getMessage(), var12);
            }

            if (this.webappBean == null) {
               this.webappBean = (WebAppBean)(new DescriptorManager()).createDescriptorRoot(WebAppBean.class).getRootBean();
               this.webappBean.setVersion("2.5");
               this.hasWebDescriptorFile = false;
            }
         } catch (FileNotFoundException var13) {
            var2 = HTTPLogger.logUnableToFindWebAppLoggable(this.toString(), this.getWarPath(), var13);
            var2.log();
            this.createModuleException(var2.getMessage(), var13);
         } catch (IOException var14) {
            var2 = HTTPLogger.logErrorReadingWebAppLoggable(this.toString(), this.getWarPath(), var14);
            var2.log();
            this.createModuleException(var2.getMessage(), var14);
         } catch (Exception var15) {
            var2 = HTTPLogger.logLoadErrorLoggable(this.toString(), this.getWarPath(), var15);
            var2.log();
            this.createModuleException(var2.getMessage(), var15);
         }
      } finally {
         this.closeVirtualJarFile();
      }

   }

   private VirtualJarFile createVirtualJarFile() throws ModuleException, IOException {
      String var1 = this.getWarPath();
      if (!(new File(var1)).isDirectory() && WarDetector.instance.suffixed(var1)) {
         if (DEBUG.isDebugEnabled()) {
            DEBUG.debug(HTTPLogger.logLoadingFromWARLoggable(this.toString(), this.getModuleName(), var1).getMessage());
         }

         return VirtualJarFactory.createVirtualJar(new JarFile(var1));
      } else {
         if (DEBUG.isDebugEnabled()) {
            DEBUG.debug(HTTPLogger.logLoadingFromDirLoggable(this.toString(), this.toString(), var1).getMessage());
         }

         return this.appFileManager.getVirtualJarFile(this.moduleURI);
      }
   }

   private void closeVirtualJarFile() {
      if (this.virtualJarFile != null) {
         try {
            this.virtualJarFile.close();
         } catch (IOException var6) {
            if (DEBUG.isDebugEnabled()) {
               DEBUG.debug(var6.getMessage(), var6);
            }
         } finally {
            this.virtualJarFile = null;
         }
      }

   }

   protected boolean isDeployedLocally() {
      AppDeploymentMBean var1 = null;
      if (this.appCtx.getProposedDomain() != null) {
         var1 = this.appCtx.getProposedDomain().lookupAppDeployment(this.appCtx.getApplicationId());
      }

      TargetMBean[] var2 = TargetUtils.findModuleTargets(var1, this.appCtx.getBasicDeploymentMBean(), this.appCtx, this.getId());

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (!(var2[var3] instanceof VirtualHostMBean) && TargetUtils.findLocalServerTarget(new TargetMBean[]{var2[var3]}) != null) {
            return true;
         }
      }

      return false;
   }

   private VirtualHostMBean[] resolveWebServers() {
      AppDeploymentMBean var1 = null;
      if (this.appCtx.getProposedDomain() != null) {
         var1 = this.appCtx.getProposedDomain().lookupAppDeployment(this.appCtx.getApplicationId());
      }

      TargetMBean[] var2 = TargetUtils.findModuleTargets(var1, this.appCtx.getBasicDeploymentMBean(), this.getId());
      if (var2 != null && var2.length != 0) {
         ArrayList var3 = new ArrayList();

         for(int var4 = 0; var4 < var2.length; ++var4) {
            if (var2[var4] instanceof VirtualHostMBean) {
               VirtualHostMBean var5 = (VirtualHostMBean)var2[var4];
               if (TargetUtils.isDeployedLocally(var5.getTargets())) {
                  var3.add(var5);
               }
            }
         }

         return (VirtualHostMBean[])((VirtualHostMBean[])var3.toArray(new VirtualHostMBean[var3.size()]));
      } else {
         return new VirtualHostMBean[0];
      }
   }

   private void addWorkManagerRuntimes(List var1) throws ManagementException {
      Iterator var2 = this.allContexts.values().iterator();
      J2EEApplicationRuntimeMBeanImpl var3 = this.appCtx.getRuntime();

      while(var2.hasNext()) {
         WebAppServletContext var4 = (WebAppServletContext)var2.next();
         WebAppRuntimeMBeanImpl var5 = var4.getRuntimeMBean();
         Iterator var6 = var1.iterator();

         while(var6.hasNext()) {
            WorkManagerRuntimeMBean var7 = WorkManagerRuntimeMBeanImpl.getWorkManagerRuntime(((WorkManagerService)var6.next()).getDelegate(), var3, var5);
            if (var7 != null) {
               var5.addWorkManagerRuntime(var7);
            }
         }
      }

   }

   private void activateContexts() throws ModuleException {
      Iterator var1 = this.allContexts.values().iterator();

      while(var1.hasNext()) {
         WebAppServletContext var2 = (WebAppServletContext)var1.next();

         try {
            var2.activate();
         } catch (DeploymentException var4) {
            throw new ModuleException(var4.getMessage(), var4);
         } catch (Throwable var5) {
            throw new ModuleException(var5.getMessage(), var5);
         }
      }

   }

   private void startContexts() throws ModuleException {
      Iterator var1 = this.allContexts.values().iterator();

      while(var1.hasNext()) {
         WebAppServletContext var2 = (WebAppServletContext)var1.next();

         try {
            var2.start();
         } catch (DeploymentException var4) {
            throw new ModuleException(var4.getMessage(), var4);
         } catch (Throwable var5) {
            throw new ModuleException(var5.getMessage(), var5);
         }
      }

   }

   private void stopContexts() {
      Iterator var1 = this.allContexts.values().iterator();

      while(var1.hasNext()) {
         WebAppServletContext var2 = (WebAppServletContext)var1.next();
         var2.stop();
      }

   }

   private void destroyContexts() {
      if (!this.webappsDestroyed) {
         this.webappsDestroyed = true;
         this.tmpDirs.clear();
         this.archives.clear();
         Iterator var1 = this.allContexts.keySet().iterator();

         while(var1.hasNext()) {
            HttpServer var2 = (HttpServer)var1.next();
            WebAppServletContext var3 = (WebAppServletContext)this.allContexts.get(var2);
            var2.unloadWebApp(var3, this.dmb.getVersionIdentifier());
            this.tmpDirs.add(var3.getRootTempDir());
            this.archives.add(var3.getWarInstance());
         }

      }
   }

   public String getWarPath() throws ModuleException {
      Debug.assertion(this.appFileManager != null);
      Debug.assertion(this.appFileManager.getSourcePath(this.moduleURI) != null);
      return this.appFileManager.getSourcePath(this.moduleURI).getAbsolutePath();
   }

   public WebAppBean getWebAppBean() {
      return this.webappBean;
   }

   void setWebAppBean(WebAppBean var1) {
      this.webappBean = var1;
   }

   boolean hasWebDescriptorFile() {
      return this.hasWebDescriptorFile;
   }

   public WeblogicWebAppBean getWlWebAppBean() {
      return this.wlWebAppBean;
   }

   void setClassFinder(ClassFinder var1) {
      if (!(var1 instanceof MultiClassFinder)) {
         var1 = new MultiClassFinder((ClassFinder)var1);
      }

      this.finder = (MultiClassFinder)var1;
      this.webClassLoader.addClassFinder(this.finder);
   }

   ClassLoader getWebClassLoader(boolean var1) {
      if (var1 && this.webClassLoader != null) {
         try {
            Jdk6.clearCache(this.webClassLoader);
            GenericClassLoader var2 = this.webClassLoader;
            Annotation var3 = var2.getAnnotation();
            if (DEBUG.isDebugEnabled()) {
               DEBUG.debug("reloading servlet classloader for " + this.toString());
            }

            this.webClassLoader = new ChangeAwareClassLoader(this.finder, false, var2.getParent());
            this.webClassLoader.setAnnotation(var3);
            appClassLoaderManager.addModuleLoader(this.webClassLoader, this.getId());
         } catch (IllegalStateException var4) {
            HTTPLogger.logFailedToBounceClassLoader(this.getAppDisplayName(), this.getId(), var4);
         }
      }

      return this.webClassLoader;
   }

   private String getModuleName() {
      return this.getId();
   }

   public String getName() {
      return this.getId();
   }

   private void createModuleException(String var1, Exception var2) throws ModuleException {
      String var3 = var1 + PlatformConstants.EOL + (var2 instanceof NestedException ? var2.toString() : var2.getMessage());
      throw new ModuleException(var3, var2);
   }

   public String getAppDisplayName() {
      return ApplicationVersionUtils.getDisplayName((BasicDeploymentMBean)this.dmb);
   }

   public WebAppServletContext getServletContextForServer(HttpServer var1) {
      return (WebAppServletContext)this.allContexts.get(var1);
   }

   public Iterator getAllContexts() {
      return this.allContexts.values().iterator();
   }

   private String normalizeId(String var1, String var2) {
      if (var1 != null && !var1.trim().equals("") && !var1.equals("/")) {
         if (var1.startsWith("/")) {
            var1 = var1.substring(1);
         }

         return var1;
      } else {
         return var2;
      }
   }

   void mergeLibraryDescriptors(Source[] var1, String var2) throws IOException, Exception {
      if (var2 != null && this.webAppParser != null) {
         if (var2.equalsIgnoreCase("WEB-INF/web.xml")) {
            this.webappBean = (WebAppBean)this.webAppParser.mergeLibaryDescriptors(var1, var2);
            if (HTTPDebugLogger.isEnabled()) {
               this.dump((DescriptorBean)this.webappBean);
            }
         } else if (var2.equalsIgnoreCase("WEB-INF/weblogic.xml")) {
            this.wlWebAppBean = (WeblogicWebAppBean)this.webAppParser.mergeLibaryDescriptors(var1, var2);
            if (HTTPDebugLogger.isEnabled()) {
               this.dump((DescriptorBean)this.wlWebAppBean);
            }
         }

      }
   }

   private static Set initRedefinableWebApps() {
      String var0 = System.getProperty("weblogic.class.redef.webapps");
      if (var0 == null) {
         return Collections.emptySet();
      } else {
         HashSet var1 = new HashSet(Arrays.asList(var0.split(",")));
         return var1;
      }
   }

   private void dump(DescriptorBean var1) {
      try {
         System.out.println("dumping merged descriptor for " + this.moduleURI);
         ByteArrayOutputStream var2 = new ByteArrayOutputStream();
         (new DescriptorManager()).writeDescriptorAsXML(var1.getDescriptor(), var2);
         (new DescriptorManager()).writeDescriptorAsXML(var1.getDescriptor(), System.out);
         String var3 = var2.toString();
         HTTPLogger.logDebug("dumping merged descriptor for " + this.moduleURI);
         HTTPLogger.logDebug(var3);
      } catch (Exception var4) {
         System.out.println("unable to dump merged descriptor for " + this.moduleURI + var4.getMessage());
      }

   }

   public PersistenceUnitRegistry getPersistenceUnitRegistry() {
      return this.persistenceUnitRegistry;
   }

   private void cleanGeneratedJspClasses() {
      if (this.tmpDirs != null && this.tmpDirs.size() >= 1) {
         String var1 = this.getPackagePrefix();
         Iterator var2 = this.tmpDirs.iterator();

         while(var2.hasNext()) {
            File var3 = (File)var2.next();
            FileUtils.remove(new File(var3, var1));
         }

      }
   }

   private String getPackagePrefix() {
      if (this.wlWebAppBean == null) {
         return "jsp_servlet";
      } else {
         JspDescriptorBean[] var1 = this.wlWebAppBean.getJspDescriptors();
         if (var1 != null && var1.length != 0) {
            String var2 = var1[0].getPackagePrefix();
            if (var2 == null) {
               return "jsp_servlet";
            } else {
               int var3 = var2.indexOf(46);
               if (var3 != -1) {
                  var2 = var2.substring(0, var3);
               }

               return var2;
            }
         } else {
            return "jsp_servlet";
         }
      }
   }

   public Map getDescriptorMappings() {
      if (this.wlWebAppBean == null) {
         return null;
      } else {
         LibraryRefBean[] var1 = this.wlWebAppBean.getLibraryRefs();
         if (var1 != null && var1.length != 0) {
            HashMap var2 = new HashMap(2);
            var2.put("/WEB-INF/web.xml", (DescriptorBean)this.webappBean);
            if (this.wlWebAppBean != null) {
               var2.put("/WEB-INF/weblogic.xml", (DescriptorBean)this.wlWebAppBean);
            }

            return var2;
         } else {
            return null;
         }
      }
   }

   public void handleMergedFinder(ClassFinder var1) {
      Iterator var2 = this.getAllContexts();

      while(var2.hasNext()) {
         WebAppServletContext var3 = (WebAppServletContext)var2.next();
         War var4 = var3.getWarInstance();
         ClassFinder var5 = var4.getClassFinder();
         if (var5 instanceof CompositeWebAppFinder) {
            CompositeWebAppFinder var6 = (CompositeWebAppFinder)var5;
            DescriptorFinder var7 = new DescriptorFinder(var4.getURI(), var1);
            var6.addFinderFirst(var7);
            var6.addFinderFirst(var1);
         }
      }

   }

   public void loadFastSwapClassloader() throws ModuleException {
      if (this.createdClassLoader) {
         if (!HttpServer.isProductionModeEnabled()) {
            boolean var1 = redefinableWebApps.contains(this.getId());
            FastSwapBean var2 = null;
            if (this.wlWebAppBean != null && this.wlWebAppBean.getContainerDescriptors() != null) {
               var2 = this.wlWebAppBean.getFastSwap();
            }

            if (var2 == null && this.appCtx.getWLApplicationDD() != null) {
               var2 = this.appCtx.getWLApplicationDD().getFastSwap();
            }

            if (var2 != null && var2.isEnabled()) {
               var1 = true;
               this.registerFastSwapFilter = true;
               this.fastSwapRefreshInterval = var2.getRefreshInterval();
            }

            if (var1) {
               try {
                  this.webClassLoader = ClassRedefinerFactory.makeClassLoader(this.webClassLoader.getClassFinder(), this.webClassLoader.getParent());
                  this.webClassLoader.setAnnotation(new Annotation(this.appCtx.getAppDeploymentMBean().getApplicationIdentifier(), this.normalizeId(this.getId(), this.moduleURI)));
               } catch (Exception var8) {
                  throw new ModuleException("Cannot initailize ClassRedefinerFactory", var8);
               }

               appClassLoaderManager.addModuleLoader(this.webClassLoader, this.getId());

               try {
                  RedefiningClassLoader var3 = (RedefiningClassLoader)this.webClassLoader;
                  J2EEApplicationRuntimeMBeanImpl var4 = this.appCtx.getRuntime();
                  ClassRedefinitionRuntimeImpl var5 = (ClassRedefinitionRuntimeImpl)var4.getClassRedefinitionRuntime();
                  if (var5 != null) {
                     var5.registerClassLoader(var3);
                  } else {
                     var3.getRedefinitionRuntime().setRedefinitionTaskLimit(var2.getRedefinitionTaskLimit());
                     var5 = new ClassRedefinitionRuntimeImpl(var4, this.webClassLoader);
                     var4.setClassRedefinitionRuntime(var5);
                  }
               } catch (ManagementException var6) {
                  throw new ModuleException(var6.getMessage(), var6);
               } catch (ClassCastException var7) {
                  throw new ModuleException(var7.getMessage(), var7);
               }
            }

         }
      }
   }

   public int getFastSwapRefreshInterval() {
      return this.fastSwapRefreshInterval;
   }

   public boolean getRegisterFastSwapFilter() {
      return this.registerFastSwapFilter;
   }

   protected void setupPersistenceUnitRegistry() throws EnvironmentException, MalformedURLException {
      if (this.persistenceUnitRegistry == null) {
         this.persistenceUnitRegistry = new ModulePersistenceUnitRegistry(this.webClassLoader, this.appCtx, this, true);
      }

   }

   private void closePersistenceUnitRegistry() {
      if (this.persistenceUnitRegistry != null) {
         this.persistenceUnitRegistry.close();
         this.persistenceUnitRegistry = null;
      }

   }

   void reloadPersistenceUnitRegistry() throws EnvironmentException, MalformedURLException {
      this.closePersistenceUnitRegistry();
      this.setupPersistenceUnitRegistry();
   }

   public PersistenceUnitRegistry getProposedPersistenceUnitRegistry() {
      return this.proposedPersistenceUnitRegistry;
   }

   public void setProposedPersistenceUnitRegistry(PersistenceUnitRegistry var1) {
      this.proposedPersistenceUnitRegistry = var1;
   }

   public boolean isParallelEnabled() {
      return true;
   }

   private void setupCoherenceCaches() throws DeploymentException {
      try {
         GenericClassLoader var1 = this.webClassLoader;
         CoherenceClusterManager var2 = CoherenceClusterManager.getInstance();
         boolean var3 = var2.isCoherenceAvailable(var1);
         CoherenceClusterRefBean var4 = this.getCoherenceClusterRefBean();
         String var5 = null;
         if (var4 != null) {
            var5 = var4.getCoherenceClusterName();
            if (!var3) {
               throw new ModuleException("Missing Coherence jar or WebLogic Coherence Integration jar");
            }
         }

         if (var3) {
            ComponentRuntimeMBean[] var6 = (ComponentRuntimeMBean[])this.getComponentRuntimeMBeans();
            List var7 = Arrays.asList(var6);
            WebAppComponentRuntimeMBean[] var8 = new WebAppComponentRuntimeMBean[var7.size()];
            var7.toArray(var8);
            var2.registerWebAppComponentRuntimeMBean(var1, var8);
            var2.configureClusterService(var1, var5);
            var2.addCacheConfiguration(var1);
         }

      } catch (ModuleException var9) {
         throw var9;
      } catch (Exception var10) {
         throw new ModuleException(var10.getMessage(), var10);
      }
   }

   private CoherenceClusterRefBean getCoherenceClusterRefBean() {
      return this.wlWebAppBean != null ? this.wlWebAppBean.getCoherenceClusterRef() : null;
   }

   private void releaseCoherenceCaches() {
      if (!this.webappsDestroyed) {
         try {
            GenericClassLoader var1 = this.webClassLoader;
            CoherenceClusterManager var2 = CoherenceClusterManager.getInstance();
            if (var2.isCoherenceAvailable(var1)) {
               var2.releaseCacheConfiguration(var1);
               var2.shutdownClusterService(var1);
               ComponentRuntimeMBean[] var3 = (ComponentRuntimeMBean[])this.getComponentRuntimeMBeans();
               List var4 = Arrays.asList(var3);
               WebAppComponentRuntimeMBean[] var5 = new WebAppComponentRuntimeMBean[var4.size()];
               var4.toArray(var5);
               var2.unRegisterWebAppComponentRuntimeMBean(var1, var5);
            }
         } catch (Exception var6) {
            HTTPLogger.logError(this.toString(), "Failed to cleanly shutdown Coherence: " + var6);
         }

      }
   }

   static AuthenticatedSubject getKernelId() {
      return WebAppModule.KernelIdHolder.kernelId;
   }

   private static class KernelIdHolder {
      static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   }
}
