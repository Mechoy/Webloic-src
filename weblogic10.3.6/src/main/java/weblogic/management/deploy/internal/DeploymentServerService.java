package weblogic.management.deploy.internal;

import java.io.File;
import java.security.AccessController;
import weblogic.deploy.beans.factory.DeploymentBeanFactory;
import weblogic.deploy.beans.factory.internal.DeploymentBeanFactoryImpl;
import weblogic.deploy.internal.InternalAppProcessor;
import weblogic.deploy.internal.adminserver.DeploymentManager;
import weblogic.deploy.internal.diagnostics.DeploymentImageSource;
import weblogic.diagnostics.image.ImageManager;
import weblogic.j2ee.J2EEApplicationService;
import weblogic.logging.Loggable;
import weblogic.management.DeploymentException;
import weblogic.management.DomainDir;
import weblogic.management.ManagementException;
import weblogic.management.ManagementLogger;
import weblogic.management.deploy.ApplicationsDirPoller;
import weblogic.management.deploy.GenericAppPoller;
import weblogic.management.internal.SecurityHelper;
import weblogic.management.provider.DomainAccessSettable;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.management.runtime.DeployerRuntimeMBean;
import weblogic.management.runtime.DeploymentManagerMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public final class DeploymentServerService extends AbstractServerService {
   private static J2EEApplicationService j2eeApplicationService;
   private static ConfiguredDeployments configuredDeploymentsHandler = null;
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static ApplicationsDirPoller poller = null;
   private static DeployerRuntimeMBean deployerRuntime;
   private static DeploymentManagerMBean deploymentManager;
   private static DeploymentBeanFactory singleton;
   private static DeploymentManager adminServerDeploymentManager;
   private static weblogic.deploy.internal.targetserver.DeploymentManager targetServerDeploymentManager;
   private static boolean started = false;

   public DeploymentServerService() {
      ImageManager.getInstance().registerImageSource("Deployment", new DeploymentImageSource());
   }

   public static DeployerRuntimeMBean getDeployerRuntime() {
      SecurityHelper.assertIfNotKernel();
      return deployerRuntime;
   }

   public static DeploymentManagerMBean getDeploymentManager() {
      SecurityHelper.assertIfNotKernel();
      return deploymentManager;
   }

   public final void start() throws ServiceFailureException {
      resume();
      started = true;
   }

   public final void halt() {
   }

   public final void stop() {
      this.halt();
      started = false;
   }

   public static void shutdownHelper() throws ServiceFailureException {
      shutdownApps();

      try {
         shutdownService();
      } catch (Exception var2) {
         var2.printStackTrace();
         Loggable var1 = DeploymentManagerLogger.logShutdownFailureLoggable();
         var1.log();
      }

   }

   public static DeploymentBeanFactory getDeploymentBeanFactory() {
      if (singleton == null) {
         singleton = new DeploymentBeanFactoryImpl();
      }

      return singleton;
   }

   static final void init() throws ServiceFailureException {
      try {
         RuntimeAccess var0 = ManagementService.getRuntimeAccess(kernelId);
         var0.addAccessCallbackClass(ApplicationCompatibilityEditProcessor.class.getName());
         InternalAppProcessor var1 = new InternalAppProcessor();
         var1.updateConfiguration(var0.getDomain());
         j2eeApplicationService = new J2EEApplicationService();
         j2eeApplicationService.start();
         startAdminServerDeploymentService();
         startTargetServerDeploymentService();
      } catch (ManagementException var2) {
         shutdownService();
         throw new ServiceFailureException(var2);
      }
   }

   static void deployPreStandbyInternalApps() throws ServiceFailureException {
      configuredDeploymentsHandler.deployPreStandbyInternalApps();
   }

   static void undeployPreStandbyInternalApps() throws ServiceFailureException {
      configuredDeploymentsHandler.undeployPreStandbyInternalApps();
   }

   private static void resume() throws ServiceFailureException {
      try {
         configuredDeploymentsHandler.deploy();
         RetirementManager.retireAppsOnStartup();
      } catch (ManagementException var2) {
         Loggable var1 = DeploymentManagerLogger.logResumeFailureLoggable();
         var1.log();
         throw new ServiceFailureException(var1.getMessage(), var2);
      }
   }

   private static void shutdownApps() throws ServiceFailureException {
      if (j2eeApplicationService != null) {
         j2eeApplicationService.halt();
      }

      if (configuredDeploymentsHandler != null) {
         try {
            configuredDeploymentsHandler.undeploy();
         } catch (DeploymentException var1) {
            throw new ServiceFailureException(var1);
         }
      }

   }

   private static DeploymentManager getAdminServerDeploymentManager() {
      if (adminServerDeploymentManager == null) {
         adminServerDeploymentManager = DeploymentManager.getInstance(kernelId);
      }

      return adminServerDeploymentManager;
   }

   private static weblogic.deploy.internal.targetserver.DeploymentManager getTargetServerDeploymentManager() {
      if (targetServerDeploymentManager == null) {
         targetServerDeploymentManager = weblogic.deploy.internal.targetserver.DeploymentManager.getInstance();
      }

      return targetServerDeploymentManager;
   }

   private static void shutdownService() {
      shutdownTargetServerDeploymentManager();
      shutdownAdminServerDeploymentManager();
   }

   private static void startAdminServerDeploymentService() throws ManagementException {
      if (ManagementService.getRuntimeAccess(kernelId).isAdminServer()) {
         initializeDeployerRuntime();
         initializeDeploymentManager();
         initializeApplicationPoller();
         initializeAdminServerDeploymentManager();
      }

   }

   private static void initializeDeployerRuntime() throws ManagementException {
      try {
         deployerRuntime = new DeployerRuntimeImpl("DeployerRuntime");
         DomainAccessSettable var0 = (DomainAccessSettable)ManagementService.getDomainAccess(kernelId);
         var0.setDeployerRuntime(deployerRuntime);
      } catch (ManagementException var2) {
         Loggable var1 = DeployerRuntimeLogger.logInitFailedLoggable(var2);
         var1.log();
         throw new ManagementException(var1.getMessage(), var2);
      }
   }

   private static void initializeDeploymentManager() throws ManagementException {
      try {
         deploymentManager = new DeploymentManagerImpl("DeploymentManager");
         DomainAccessSettable var0 = (DomainAccessSettable)ManagementService.getDomainAccess(kernelId);
         var0.setDeploymentManager(deploymentManager);
      } catch (ManagementException var2) {
         Loggable var1 = DeploymentManagerLogger.logInitFailedLoggable(var2);
         var1.log();
         throw new ManagementException(var1.getMessage(), var2);
      }
   }

   private static void initializeApplicationPoller() {
      ApplicationsDirPoller.removeStagedFilesForAppsRemovedSinceLastShutdown();
   }

   private static void initializeAdminServerDeploymentManager() throws ManagementException {
      getAdminServerDeploymentManager().initialize();
   }

   private static void shutdownAdminServerDeploymentManager() {
      if (ManagementService.getRuntimeAccess(kernelId).isAdminServer()) {
         DeploymentManager.shutdown();
      }

   }

   private static void startTargetServerDeploymentService() {
      initializeTargetServerDeploymentManager();
      initializeConfiguredDeployments();
   }

   private static void initializeTargetServerDeploymentManager() {
      getTargetServerDeploymentManager().initialize();
   }

   private static void initializeConfiguredDeployments() {
      configuredDeploymentsHandler = ConfiguredDeployments.getConfigureDeploymentsHandler();
   }

   private static void shutdownTargetServerDeploymentManager() {
      getTargetServerDeploymentManager().shutdown();
   }

   static void startAutoDeploymentPoller() {
      if (ManagementService.getRuntimeAccess(kernelId).isAdminServer()) {
         short var0 = 3000;
         String var1 = DomainDir.getAppPollerDir();
         if (!ManagementService.getRuntimeAccess(kernelId).getDomain().isProductionModeEnabled()) {
            long var2 = (new Integer(var0)).longValue();
            poller = new ApplicationsDirPoller(new File(var1), false, var2);
            poller.start();
            ManagementLogger.logPollerStarted();
         } else {
            ManagementLogger.logPollerNotStarted();
         }

      }
   }

   public static boolean isStarted() {
      return started;
   }

   public static GenericAppPoller getApplicationDirPoller() {
      return poller;
   }
}
