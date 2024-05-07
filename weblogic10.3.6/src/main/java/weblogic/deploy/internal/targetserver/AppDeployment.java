package weblogic.deploy.internal.targetserver;

import java.io.File;
import java.io.IOException;
import java.security.AccessController;
import weblogic.application.Deployment;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.deploy.event.DeploymentEvent;
import weblogic.deploy.event.DeploymentEventManager;
import weblogic.deploy.internal.DeploymentVersion;
import weblogic.deploy.internal.TargetHelper;
import weblogic.deploy.internal.targetserver.datamanagement.AppData;
import weblogic.deploy.internal.targetserver.datamanagement.Data;
import weblogic.deploy.internal.targetserver.operations.AbstractOperation;
import weblogic.deploy.internal.targetserver.state.DeploymentState;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.logging.Loggable;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.deploy.internal.AppRuntimeStateManager;
import weblogic.management.deploy.internal.ApplicationRuntimeState;
import weblogic.management.deploy.internal.MBeanConverter;
import weblogic.management.deploy.internal.SlaveDeployerLogger;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;

public class AppDeployment extends BasicDeployment {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public AppDeployment(AppDeploymentMBean var1) {
      super(var1);
   }

   private AppDeploymentMBean getAppDeploymentMBean() {
      return (AppDeploymentMBean)this.getDeploymentMBean();
   }

   public void verifyAppVersionSecurity(AbstractOperation var1) throws DeploymentException {
      if (isDebugEnabled()) {
         debug("BasicDeployment.verifyAppVersionSecurity(" + this.name + ")");
      }

      AppDeploymentMBean var2 = this.getAppDeploymentMBean();
      if (var2 != null && var2.getVersionIdentifier() != null && !SecurityServiceManager.isApplicationVersioningSupported("weblogicDEFAULT")) {
         Loggable var3 = SlaveDeployerLogger.logSecurityRealmDoesNotSupportAppVersioningLoggable("weblogicDEFAULT", ApplicationVersionUtils.getDisplayName((BasicDeploymentMBean)var2));
         var3.log();
         throw new DeploymentException(var3.getMessage());
      }
   }

   private void updateAggregateDeploymentVersion() {
      String var1 = this.getAppDeploymentMBean().getName();
      ApplicationRuntimeState var2 = AppRuntimeStateManager.getManager().get(var1);
      if (var2 != null) {
         DeploymentVersion var3 = var2.getDeploymentVersion();
         if (var3 != null) {
            DeploymentManager.getInstance().addOrUpdateTargetDeploymentVersion(var1, var3);
         }
      }

   }

   public void prepare() throws IOException, DeploymentException {
      AppDeploymentMBean var1 = this.getAppDeploymentMBean();
      if (DeployHelper.isOkToTransition(var1, server, "STATE_PREPARED")) {
         if (isDebugEnabled()) {
            debug("Preparing " + this.name);
         }

         try {
            this.stageFilesForStatic();
            this.staticDeployValidationForNonVersion();
            MBeanConverter.setupNew81MBean(var1);
            this.fireVetoableDeploymentEvent();
            Deployment var2 = this.createDeployment(var1, (DeploymentState)null);
            var1.setDeploymentPlanDescriptor(this.parsePlan());
            DeploymentContextImpl var3;
            if (this.task == null) {
               var3 = DeployHelper.createDeploymentContext(var1);
               var3.setStaticDeploymentOperation(true);
               var3.setStoppedModules(AppRuntimeStateManager.getManager().getStoppedModuleIds(var1.getName(), serverName));
            } else {
               var3 = this.task.getDeploymentContext();
            }

            var3.setAdminModeTransition(true);
            this.startLifecycleStateManager();
            this.relayStagingState(this.getStagingState());
            this.updateAggregateDeploymentVersion();

            try {
               var2.prepare(var3);
            } catch (Throwable var17) {
               this.failDeployment();
               throw var17;
            } finally {
               this.finishLifecycleStateManager();
            }

            this.fireDeployedDeploymentEvent();
         } catch (Throwable var19) {
            this.failDeployment();
            this.finishLifecycleStateManager();
            DeploymentException var4 = DeployHelper.convertThrowable(var19);
            SlaveDeployerLogger.logIntialPrepareApplicationFailedLoggable(this.name, var4).log();
            throw var4;
         } finally {
            ;
         }
      }
   }

   private void staticDeployValidationForNonVersion() throws DeploymentException {
      AppDeploymentMBean var1 = this.getAppDeploymentMBean();
      String var2 = var1.getVersionIdentifier();
      if (var2 == null || var2.length() == 0) {
         String var3 = ApplicationVersionUtils.getManifestVersion(this.getLocalAppFileOrDir().getAbsolutePath());
         if (var3 != null && var3.length() != 0) {
            Loggable var4 = SlaveDeployerLogger.logStaticDeploymentOfNonVersionAppCheckLoggable(this.getName());
            throw new DeploymentException(var4.getMessage());
         }
      }
   }

   private void failDeployment() {
      if (this.getState() == null) {
         this.setState(new DeploymentState(this.deploymentMBean.getName(), "__Lifecycle_taskid__", 0));
      }

      this.getState().setCurrentState("STATE_FAILED", true);
   }

   public DeploymentPlanBean parsePlan() throws DeploymentException {
      try {
         AppDeploymentMBean var1 = this.getAppDeploymentMBean();
         if (!var1.isInternalApp()) {
            var1.setDeploymentPlanDescriptor((DeploymentPlanBean)null);
         }

         return var1.getDeploymentPlanDescriptor();
      } catch (IllegalArgumentException var2) {
         throw new DeploymentException(var2.toString(), var2);
      }
   }

   public static File getFile(AppDeploymentMBean var0) {
      File var2;
      if (DeployHelper.getStagingMode(serverName, var0).equals("nostage")) {
         String var1 = var0.getAbsoluteSourcePath();
         var2 = new File(var1);
      } else {
         var2 = new File(var0.getLocalSourcePath());
      }

      return var2;
   }

   public void removeDeployment() {
      AppDeploymentMBean var1 = this.getAppDeploymentMBean();
      if (var1 != null) {
         this.deploymentManager.removeDeployment((BasicDeploymentMBean)var1);
      }

   }

   public void updateDescriptorsPathInfo() {
      AppData var1 = (AppData)this.getLocalData();
      var1.updateDescriptorsPathInfo(this.getAppDeploymentMBean());
   }

   private void fireDeployedDeploymentEvent() {
      AppDeploymentMBean var1 = this.getAppDeploymentMBean();
      DeploymentEventManager.sendDeploymentEvent(DeploymentEvent.create(this, DeploymentEvent.APP_DEPLOYED, var1, true, (String[])null, TargetHelper.getTargetNames(var1.getTargets())));
   }

   protected Data createLocalData() {
      AppDeploymentMBean var1 = this.getAppDeploymentMBean();
      String var2 = DeployHelper.getStagingMode(serverName, var1);
      return new AppData(var1, this, var2, var1.getLocalSourcePath(), (String)null);
   }

   public String getRelativePlanPath() throws IOException {
      AppData var1 = (AppData)this.getLocalData();
      return var1.getRelativePlanPath();
   }
}
