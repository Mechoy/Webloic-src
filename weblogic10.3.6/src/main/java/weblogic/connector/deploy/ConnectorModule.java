package weblogic.connector.deploy;

import java.io.File;
import java.io.IOException;
import java.security.AccessController;
import java.util.Hashtable;
import java.util.Vector;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import weblogic.application.AdminModeCompletionBarrier;
import weblogic.application.ApplicationContext;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.ApplicationFileManager;
import weblogic.application.ConcurrentModule;
import weblogic.application.Module;
import weblogic.application.ModuleException;
import weblogic.application.UpdateListener;
import weblogic.application.internal.BaseJ2EEModule;
import weblogic.application.utils.AppFileOverrideUtils;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.connector.ConnectorLogger;
import weblogic.connector.common.Debug;
import weblogic.connector.common.RACollectionManager;
import weblogic.connector.common.RAInstanceManager;
import weblogic.connector.common.Utils;
import weblogic.connector.configuration.DDUtil;
import weblogic.connector.exception.RAConfigurationException;
import weblogic.connector.exception.RAException;
import weblogic.connector.external.AdapterListener;
import weblogic.connector.external.RAComplianceException;
import weblogic.connector.external.RAInfo;
import weblogic.connector.external.impl.RAInfoImpl;
import weblogic.connector.lifecycle.BootstrapContext;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.management.ManagementException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.ConnectorComponentMBean;
import weblogic.management.runtime.ComponentRuntimeMBean;
import weblogic.rmi.extensions.RemoteRuntimeException;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.classloaders.Annotation;
import weblogic.utils.classloaders.AugmentableClassLoaderManager;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.jars.VirtualJarFile;
import weblogic.work.ShutdownCallback;

public final class ConnectorModule extends BaseJ2EEModule implements Module, UpdateListener, ConcurrentModule {
   private final String uri;
   private RAInfo raInfo;
   private RAInfo newRAInfo;
   private ConnectorModuleChangePackage pendingChanges = null;
   String applicationName = null;
   String componentName = null;
   String componentURI = null;
   private ApplicationContextInternal appCtx;
   private Context linkCtx;
   private Context j2caCtx;
   private Context connectionFactoryCtx;
   private Context adminObjectCtx;
   private ApplicationFileManager appFileManager;
   private VirtualJarFile jarFile;
   private GenericClassLoader classLoader;
   private boolean usingSubClassLoader;
   private String altDD = null;
   private RAInstanceManager raIM;
   private String moduleName;
   private String appId;
   private Vector vectSubContexts = new Vector();
   private RarArchive explodedRar;

   public ConnectorModule(String var1) {
      Debug.deployment("Creating ConnectorModule with uri = " + var1);
      this.uri = var1;
   }

   public final void prepare() throws ModuleException {
      boolean var1 = false;
      this.debugModule("is in NEW state. Calling prepare.");
      Utils.startManagement();

      try {
         this.raInfo = this.loadDescriptors();
         String var2 = ApplicationVersionUtils.getActiveVersionId(this.appCtx.getApplicationName());
         RAInstanceManager var3 = RACollectionManager.getRAInstanceManagerByAppName(this.appCtx.getApplicationName(), var2);
         AuthenticatedSubject var4 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
         ClassFinder var5 = AppFileOverrideUtils.getFinderIfRequired(this.appCtx.getAppDeploymentMBean(), this.appCtx.getRuntime().isEAR() ? this.componentURI : null);
         if (var5 != null) {
            this.debugModule("Application File Overrides enabled");
            this.classLoader.addClassFinderFirst(var5);
            if (this.raInfo.isEnableGlobalAccessToClasses()) {
               this.debugModule("Application File Overrides applied to augmented system classloader");
               AugmentableClassLoaderManager.getAugmentableSystemClassLoader().addClassFinderFirst(var5);
            }
         }

         this.raIM = new RAInstanceManager(this.raInfo, this.classLoader, this.applicationName, this.componentName, this.uri, this.explodedRar, this.appCtx, this.adminObjectCtx, this.connectionFactoryCtx, this.uri, var3, var4);
         if (var5 != null) {
            this.raIM.getClassFinders().add(var5);
         }

         this.raIM.prepare();
         this.debugModule("has been PREPARED.");
      } catch (RAException var20) {
         var1 = true;
         throw new ModuleException(var20.toString());
      } catch (RAComplianceException var21) {
         var1 = true;
         throw new ModuleException(var21.toString());
      } catch (ModuleException var22) {
         var1 = true;
         throw var22;
      } catch (Throwable var23) {
         var1 = true;
         throw new ModuleException(var23.toString(), var23);
      } finally {
         if (var1) {
            try {
               this.unsetJarFile();
            } catch (ModuleException var19) {
            }

            try {
               this.raIM.closeClassFinders();
            } catch (Throwable var18) {
            }
         }

         Utils.stopManagement();
      }

   }

   public final void unprepare() throws ModuleException {
      this.debugModule("is being rolled back: calling unprepare.");
      Utils.startManagement();

      try {
         this.debugModule("is being rolled back. Calling Deployer.rollback( connMBean )");
         this.raIM.rollback();
      } catch (RAException var7) {
         String var2 = Debug.getExceptionRollbackModuleFailed(var7.toString());
         throw new ModuleException(var2, var7);
      } finally {
         Utils.stopManagement();
      }

      this.unsetJarFile();
      this.debugModule("has been rolled back: UNPREPARED");
   }

   public final void activate() throws ModuleException {
      ClassLoader var1 = Thread.currentThread().getContextClassLoader();
      boolean var2 = false;
      this.debugModule("is being ACTIVATED: calling activate.");
      Utils.startManagement();

      try {
         Thread.currentThread().setContextClassLoader(this.classLoader);
         this.raIM.activate();
         this.debugModule("has been ACTIVATED");
      } catch (Exception var12) {
         var2 = true;
         throw new ModuleException(var12.toString(), var12);
      } finally {
         if (var2) {
            try {
               this.unsetJarFile();
            } catch (ModuleException var11) {
            }
         }

         Utils.stopManagement();
         Thread.currentThread().setContextClassLoader(var1);
      }

   }

   public final void start() {
      this.debugModule("ConnectorModule.start() called; nothing is done here.");
   }

   public final void deactivate() throws ModuleException {
      this.debugModule("is being DEACTIVATED: calling deactivate.");
      Utils.startManagement();

      try {
         this.raIM.deactivate();
         this.debugModule("has been DEACTIVATED");
      } catch (RAException var6) {
         throw new ModuleException(var6.toString(), var6);
      } finally {
         Utils.stopManagement();
      }

   }

   public boolean acceptURI(String var1) {
      if (this.appCtx.isEar() && var1.equals(this.uri + "/" + "META-INF/weblogic-ra.xml")) {
         return true;
      } else {
         return !this.appCtx.isEar() && var1.equals("META-INF/weblogic-ra.xml");
      }
   }

   public void prepareUpdate(String var1) throws ModuleException {
      String var3;
      try {
         File var2 = this.resolveAltDD(this.appCtx, var1);
         DeploymentPlanBean var7 = this.appCtx.findDeploymentPlan();
         AppDeploymentMBean var4 = this.appCtx.getAppDeploymentMBean();
         this.newRAInfo = DDUtil.getRAInfo(this.jarFile, var2, this.getModuleName(), var4, var7);
         ((RAInfoImpl)this.newRAInfo).copyBaseRA((RAInfoImpl)this.raInfo);
         this.pendingChanges = DeployerUtil.enumerateChanges(this.raIM, this.raInfo, this.newRAInfo);
         this.preparePendingChanges();
      } catch (RAConfigurationException var5) {
         var3 = Debug.getExceptionPrepareUpdateFailed(var1, var5.toString());
         throw new ModuleException(var3, var5);
      } catch (RAException var6) {
         var3 = Debug.getExceptionPrepareUpdateFailed(var1, var6.toString());
         throw new ModuleException(var3, var6);
      }
   }

   public void activateUpdate(String var1) throws ModuleException {
      ClassLoader var2 = Thread.currentThread().getContextClassLoader();

      try {
         Thread.currentThread().setContextClassLoader(this.classLoader);
         this.activatePendingChanges();
      } catch (RAException var8) {
         throw new ModuleException(var8);
      } finally {
         Thread.currentThread().setContextClassLoader(var2);
      }

   }

   private void activatePendingChanges() throws RAException {
      if (this.pendingChanges != null) {
         this.raInfo = this.newRAInfo;
         this.newRAInfo = null;
         AuthenticatedSubject var1 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
         this.raIM.setRAInfo(var1, this.raInfo);
         this.pendingChanges.activate();
         this.pendingChanges = null;
      } else {
         this.debugModule("No pending changes for update invocation");
      }

   }

   private void preparePendingChanges() throws RAException {
      if (this.pendingChanges != null) {
         this.pendingChanges.prepare(this.newRAInfo);
      } else {
         this.debugModule("No pending changes for update invocation");
      }

   }

   public void rollbackUpdate(String var1) {
      this.newRAInfo = null;
      this.pendingChanges = null;
   }

   public void initUsingLoader(ApplicationContext var1, GenericClassLoader var2, UpdateListener.Registration var3) throws ModuleException {
      this.debugModule("calling initUsingLoader.");
      this.initAndSetClassLoader(var1, var2, var3, false);
      this.debugModule("has been INITED UsingLoader.");
   }

   public GenericClassLoader init(ApplicationContext var1, GenericClassLoader var2, UpdateListener.Registration var3) throws ModuleException {
      this.debugModule("calling init.");
      GenericClassLoader var4 = this.initAndSetClassLoader(var1, var2, var3, true);
      this.debugModule("has been INITED.");
      return var4;
   }

   public GenericClassLoader initAndSetClassLoader(ApplicationContext var1, GenericClassLoader var2, UpdateListener.Registration var3, boolean var4) throws ModuleException {
      this.usingSubClassLoader = var4;
      Utils.startManagement();

      try {
         this.appCtx = (ApplicationContextInternal)var1;
         ConnectorComponentMBean var5 = (ConnectorComponentMBean)this.findComponentMBean(this.appCtx, this.uri, ConnectorComponentMBean.class);
         this.applicationName = var5.getParent().getName();
         this.componentName = var5.getName();
         this.componentURI = var5.getURI();
         if (var4) {
            this.classLoader = new GenericClassLoader(var2);
            this.classLoader.setAnnotation(new Annotation(this.appCtx.getApplicationId(), this.componentName));
         } else {
            this.classLoader = var2;
         }

         var3.addUpdateListener(this);
         if (this.appCtx.getRuntime().isEAR()) {
            this.initJndiSubcontexts(var1);
         }

         this.appFileManager = this.appCtx.getApplicationFileManager();
      } finally {
         Utils.stopManagement();
      }

      return this.classLoader;
   }

   public void destroy(UpdateListener.Registration var1) throws ModuleException {
      this.debugModule("calling destroy.");
      Utils.startManagement();

      try {
         if (this.usingSubClassLoader && this.classLoader != null) {
            this.classLoader.close();
         }

         this.raInfo = null;
         var1.removeUpdateListener(this);
         this.remove();

         try {
            this.cleanupJNDISubContexts();
         } catch (NamingException var7) {
         }

         this.debugModule("has been DESTROIED.");
      } finally {
         Utils.stopManagement();
      }

   }

   private void cleanupJNDISubContexts() throws NamingException {
      for(int var1 = this.vectSubContexts.size() - 1; var1 >= 0; --var1) {
         Hashtable var2 = (Hashtable)this.vectSubContexts.get(var1);
         Context var3 = (Context)var2.get("ParentCtx");
         String var4 = (String)var2.get("Name");
         var3.destroySubcontext(var4);
      }

      this.vectSubContexts.clear();
      if (this.j2caCtx != null) {
         if (this.adminObjectCtx != null) {
            this.j2caCtx.destroySubcontext("AdminObject");
         }

         if (this.connectionFactoryCtx != null) {
            this.j2caCtx.destroySubcontext("ConnectionFactory");
         }

         this.appCtx.getEnvContext().destroySubcontext("j2ca");
      }

   }

   public Class loadClass(String var1) throws ClassNotFoundException {
      Utils.startManagement();

      Class var3;
      try {
         AuthenticatedSubject var2 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
         if (this.classLoader == null || var1 == null || var1.equals("")) {
            throw new ClassNotFoundException(var1);
         }

         var3 = this.raIM.getAdapterLayer().loadClass(this.classLoader, var1, var2);
      } finally {
         Utils.stopManagement();
      }

      return var3;
   }

   public void remove() throws ModuleException {
      this.debugModule("calling remove.");

      String var2;
      try {
         if (this.raIM != null) {
            this.raIM.cleanupRuntime();
         }

         if (this.explodedRar != null) {
            this.explodedRar.remove();
         }
      } catch (RemoteRuntimeException var3) {
         var2 = Debug.logFailedToFindModuleRuntimeMBean(var3.toString());
         Debug.logStackTrace(var2, var3);
         this.debugModule("Warning: couldn't find a runtime MBean for the module: " + var3.toString());
      } catch (ManagementException var4) {
         var2 = Debug.logFailedToUnregisterModuleRuntimeMBean(var4.toString());
         Debug.logStackTrace(var2, var4);
         this.debugModule("Warning: couldn't unregister a runtime MBean for the module: " + var4.toString());
      }

      this.debugModule("has been REMOVED.");
   }

   public void adminToProduction() {
      this.debugModule("calling adminToProduction.");
      this.debugModule("has been adminToProduction.");
   }

   public void gracefulProductionToAdmin(AdminModeCompletionBarrier var1) throws ModuleException {
      this.debugModule("calling gracefulProductionToAdmin.");
      if (this.raIM.isWaitingStartVersioningComplete()) {
         ConnectorLogger.logWaitingComplete(this.raIM.toString());
         ShutdownCallback var2 = var1.registerWMShutdown();
         this.addListenerAndSignalShutdown(new AdapterListenerImpl(var2));
      }

      this.debugModule("has been gracefulProductionToAdmin.");
   }

   public void forceProductionToAdmin() {
      this.debugModule("calling forceProductionToAdmin.");
      this.debugModule("has been forceProductionToAdmin.");
   }

   private RAInfo loadDescriptors() throws ModuleException {
      RAInfo var4;
      try {
         Debug.enter(this, ".loadDescriptors()");
         this.debugModule("ConnectorModule.loadDescriptors() loading descriptors for module  of application " + ApplicationVersionUtils.getDisplayName(this.getAppId()));
         this.setJarFile();
         File var1 = this.resolveAltDD(this.appCtx, this.uri);
         DeploymentPlanBean var2 = this.appCtx.getAppDeploymentMBean().getDeploymentPlanDescriptor();
         AppDeploymentMBean var3 = this.appCtx.getAppDeploymentMBean();

         try {
            this.raInfo = DDUtil.getRAInfo(this.jarFile, var1, this.getModuleName(), var3, var2);
         } catch (Exception var9) {
            this.debugModule("ConnectorModule.loadDescriptors() threw an exception: " + var9);
            throw new ModuleException(var9.toString(), var9);
         }

         this.debugModule("ConnectorModule.loadDescriptors() succeeded");
         var4 = this.raInfo;
      } finally {
         Debug.exit(this, ".loadDescriptors() returning with raInfo = " + this.raInfo);
      }

      return var4;
   }

   private void unsetJarFile() throws ModuleException {
      if (this.jarFile != null) {
         try {
            this.jarFile.close();
         } catch (IOException var7) {
            String var2 = Debug.getExceptionCloseVJarFailed(this.jarFile.getName(), var7.toString());
            throw new ModuleException(var2, var7);
         } finally {
            this.jarFile = null;
         }
      }

   }

   public void setAltDD(String var1) {
      this.altDD = var1;
   }

   private void setJarFile() throws ModuleException {
      try {
         this.jarFile = this.appFileManager.getVirtualJarFile(this.componentURI);
         this.explodedRar = new RarArchive(this.getAppId(), this.getModuleName(), this.jarFile);
      } catch (IOException var3) {
         String var2 = Debug.getExceptionCreateVJarFailed(this.componentURI, var3.toString());
         throw new ModuleException(var2, var3);
      }
   }

   public String getId() {
      return this.uri;
   }

   public String getType() {
      return WebLogicModuleType.MODULETYPE_RAR;
   }

   public ComponentRuntimeMBean[] getComponentRuntimeMBeans() {
      return new ComponentRuntimeMBean[]{this.getRuntimeMBean()};
   }

   public String getAltDD() {
      return this.altDD;
   }

   public ComponentRuntimeMBean getRuntimeMBean() {
      return this.raIM != null ? this.raIM.getRuntime() : null;
   }

   private void addListenerAndSignalShutdown(AdapterListener var1) throws ModuleException {
      BootstrapContext var2 = this.raIM.getBootstrapContext();
      if (var2 != null) {
         var2.addListener(var1);
         var2.signalShutdown();
      }

   }

   public void removeListener(AdapterListener var1) throws ModuleException {
      BootstrapContext var2 = this.raIM.getBootstrapContext();
      if (var2 != null) {
         var2.removeListener(var1);
      }

   }

   private String getModuleName() {
      if (this.moduleName != null) {
         return this.moduleName;
      } else if (this.componentName != null) {
         this.moduleName = this.componentName;
         return this.moduleName;
      } else {
         return this.uri;
      }
   }

   private String getAppId() {
      if (this.appId != null) {
         return this.appId;
      } else {
         if (this.appCtx != null) {
            this.appId = this.appCtx.getApplicationId();
         }

         return this.appId;
      }
   }

   private void debugModule(String var1) {
      if (Debug.isDeploymentEnabled()) {
         Debug.deployment("Module '" + this.getModuleName() + "' " + var1);
      }

   }

   private void initJndiSubcontexts(ApplicationContext var1) throws ModuleException {
      try {
         if (this.linkCtx == null) {
            this.linkCtx = this.getJndiContext(var1);
         }

         if (this.j2caCtx == null) {
            try {
               this.j2caCtx = (Context)var1.getEnvContext().lookup("j2ca");
            } catch (NameNotFoundException var7) {
               this.j2caCtx = var1.getEnvContext().createSubcontext("j2ca");
            }
         }

         if (this.adminObjectCtx == null) {
            try {
               this.adminObjectCtx = (Context)this.j2caCtx.lookup("AdminObject");
            } catch (NameNotFoundException var6) {
               this.adminObjectCtx = this.j2caCtx.createSubcontext("AdminObject");
            }
         }

         if (this.connectionFactoryCtx == null) {
            try {
               this.connectionFactoryCtx = (Context)this.j2caCtx.lookup("ConnectionFactory");
            } catch (NameNotFoundException var5) {
               this.connectionFactoryCtx = this.j2caCtx.createSubcontext("ConnectionFactory");
            }
         }

      } catch (NamingException var8) {
         this.debugModule("Failed to initialize the Connector-specific Jndi sub-contexts for the application, " + var1.getApplicationId() + " :  " + var8);
         String var3 = Debug.logInitJndiSubcontextsFailed(var1.getApplicationId(), var8.toString());
         Debug.logStackTrace(var3, var8);
         String var4 = Debug.getExceptionInitializeJndiSubcontextsFailed(var1.getApplicationId(), var8.toString());
         throw new ModuleException(var4, var8);
      }
   }

   private Context getJndiContext(ApplicationContext var1) throws NamingException {
      String[] var2 = var1.getApplicationId().split("\\.");
      Context var3 = (Context)(new InitialContext()).lookup("weblogic");

      for(int var4 = 0; var4 < var2.length; ++var4) {
         try {
            var3 = (Context)var3.lookup(var2[var4]);
         } catch (NameNotFoundException var8) {
            Context var6 = var3;
            var3 = var3.createSubcontext(var2[var4]);
            Hashtable var7 = new Hashtable();
            var7.put("ParentCtx", var6);
            var7.put("Name", var2[var4]);
            this.vectSubContexts.add(var7);
         }
      }

      return var3;
   }

   public DescriptorBean[] getDescriptors() {
      DescriptorBean[] var1 = new DescriptorBean[]{(DescriptorBean)this.raInfo.getConnectorBean(), (DescriptorBean)this.raInfo.getWeblogicConnectorBean()};
      return var1;
   }

   public boolean isParallelEnabled() {
      return true;
   }
}
