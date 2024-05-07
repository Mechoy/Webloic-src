package weblogic.deploy.internal.adminserver.operations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.AccessController;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.j2ee.J2EEUtils;
import weblogic.logging.Loggable;
import weblogic.management.ApplicationException;
import weblogic.management.DeploymentException;
import weblogic.management.ManagementException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.deploy.DeploymentData;
import weblogic.management.deploy.DeploymentTaskRuntime;
import weblogic.management.deploy.internal.DeployerRuntimeLogger;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.FileUtils;

public final class RedeployOperation extends AbstractOperation {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public RedeployOperation() {
      this.taskType = 9;
   }

   protected String getAutoDeployErrorMsg(String var1) {
      Loggable var2 = DeployerRuntimeLogger.invalidRedeployOnAutodeployedAppLoggable(var1);
      return var2.getMessage();
   }

   protected final AppDeploymentMBean updateConfiguration(String var1, String var2, String var3, DeploymentData var4, String var5, boolean var6) throws ManagementException {
      String var8 = OperationHelper.getAppName(var2, var4, var1);
      String var9 = OperationHelper.getVersionIdFromData(var4, var2);
      String var10 = OperationHelper.getTaskString(this.taskType);
      DomainMBean var11 = this.beanFactory.getEditableDomain();
      if (isDebugEnabled()) {
         this.printDebugStartMessage(var1, var8, var9, var4, var5, var10, var3);
      }

      OperationHelper.assertNameIsNonNull(var8, var10);
      AppDeploymentMBean var7;
      if (var9 != null) {
         var7 = ApplicationVersionUtils.getAppDeployment(var11, var8, var9);
      } else {
         var7 = ApplicationVersionUtils.getActiveAppDeployment(var11, var8);
         if (var7 == null) {
            var7 = ApplicationVersionUtils.getActiveAppDeployment(var11, var8, true);
         }
      }

      Loggable var13;
      try {
         OperationHelper.assertAppIsNonNull(var7, var8, var9, var10);
         OperationHelper.assertAppIsActive(var7, var8, var9, var10);
         checkUpdateForArchiveApp(var4, var7);
         OperationHelper.validateTargets(var11, var4, var7, var10);
         if (var1 == null) {
            OperationHelper.assertNoMixedTargeting(var4);
            OperationHelper.assertNoChangedAltDDs(var4);
            var7 = OperationHelper.getActiveVersionIfNeeded(var11, var9, var7, var8, var4, var10);
            this.reconcileMBeans(var4, var7, true);
            return var7;
         } else {
            AppDeploymentMBean var12 = (new DeployOperation()).updateConfiguration(var1, var2, var3, var4, var5, var6);
            this.taskType = 1;
            return var12;
         }
      } catch (ApplicationException var14) {
         var13 = DeployerRuntimeLogger.logInvalidAppLoggable(var7.getAbsoluteSourcePath(), var8, var14.getMessage());
         var13.log();
         throw new ManagementException(var13.getMessage(), var14);
      } catch (FileNotFoundException var15) {
         var13 = DeployerRuntimeLogger.logInvalidSourceLoggable(var7.getAbsoluteSourcePath(), var8, var15.getMessage());
         var13.log();
         throw new ManagementException(var13.getMessage(), var15);
      }
   }

   protected final void postTaskCreationConfigurationUpdate(AppDeploymentMBean var1, String var2, DeploymentData var3) throws ManagementException {
      if (var3.getDelete()) {
         try {
            String var4 = ManagementService.getRuntimeAccess(kernelId).getServer().getUploadDirectoryName();
            var4 = (new File(var4)).getCanonicalPath().toLowerCase();
            String var5 = (new File(var1.getAbsoluteSourcePath())).getCanonicalPath().toLowerCase();
            if (var5.startsWith(var4)) {
               File var6 = new File(var1.getAbsoluteSourcePath());
               String[] var7 = var3.getFiles();

               for(int var8 = 0; var8 < var7.length; ++var8) {
                  File var9 = new File(var6, var7[var8]);
                  if (isDebugEnabled()) {
                     debugDeployer("Removing " + var9.getPath() + " from upload dir");
                  }

                  FileUtils.remove(var9);
               }
            }
         } catch (IOException var10) {
            throw new ManagementException(var10);
         }
      }

   }

   protected AbstractOperation createCopy() {
      return new RedeployOperation();
   }

   protected void mergeWithUndeploy(AbstractOperation var1) throws ManagementException {
      super.mergeWithDeploy(var1);
      var1.mergeUndeployWithDistributeOrDeployOrRedeploy(this);
   }

   protected void mergeWithRedeploy(AbstractOperation var1) throws ManagementException {
      super.mergeWithRedeploy(var1);
      this.mergeWithSameOperationType(var1);
   }

   protected void mergeWithDeploy(AbstractOperation var1) throws ManagementException {
      super.mergeWithDeploy(var1);
      if (!this.areTargetsSame(var1)) {
         DeploymentTaskRuntime var2 = this.getTaskRuntime();
         DeploymentData var3 = var2.getDeploymentData();
         DeploymentTaskRuntime var4 = this.getTaskRuntime();
         DeploymentData var5 = var4.getDeploymentData();
         var3.setDeploymentPlan(var5.getDeploymentPlan());
         var3.addGlobalTargets(var5.getGlobalTargets());
         if (var5.hasModuleTargets()) {
            var3.addOrUpdateModuleTargets(var5.getAllModuleTargets());
         }

         if (var5.hasSubModuleTargets()) {
            var3.addOrUpdateSubModuleTargets(var5.getAllSubModuleTargets());
         }
      }

      var1.setAsDelegatorTo(this);
   }

   protected void mergeWithUpdate(AbstractOperation var1) throws ManagementException {
      super.mergeWithUpdate(var1);
      DeploymentTaskRuntime var2 = this.getTaskRuntime();
      DeploymentData var3 = var2.getDeploymentData();
      DeploymentTaskRuntime var4 = this.getTaskRuntime();
      DeploymentData var5 = var4.getDeploymentData();
      if (var3.hasFiles()) {
         var3.setPlanUpdate(var5.isPlanUpdate());
      }

      var3.setDeploymentPlan(var5.getDeploymentPlan());
      var3.addGlobalTargets(var5.getGlobalTargets());
      if (var5.hasModuleTargets()) {
         var3.addOrUpdateModuleTargets(var5.getAllModuleTargets());
      }

      if (var5.hasSubModuleTargets()) {
         var3.addOrUpdateSubModuleTargets(var5.getAllSubModuleTargets());
      }

      var1.setAsDelegatorTo(this);
   }

   protected void mergeWithDistribute(AbstractOperation var1) throws ManagementException {
      super.mergeWithDistribute(var1);
      if (!this.areTargetsSame(var1)) {
         DeploymentTaskRuntime var2 = this.getTaskRuntime();
         DeploymentData var3 = var2.getDeploymentData();
         DeploymentTaskRuntime var4 = this.getTaskRuntime();
         DeploymentData var5 = var4.getDeploymentData();
         var3.setDeploymentPlan(var5.getDeploymentPlan());
         var3.addGlobalTargets(var5.getGlobalTargets());
         if (var5.hasModuleTargets()) {
            var3.addOrUpdateModuleTargets(var5.getAllModuleTargets());
         }

         if (var5.hasSubModuleTargets()) {
            var3.addOrUpdateSubModuleTargets(var5.getAllSubModuleTargets());
         }
      }

      var1.setAsDelegatorTo(this);
   }

   private static void checkUpdateForArchiveApp(DeploymentData var0, AppDeploymentMBean var1) throws DeploymentException {
      if (var0 != null) {
         if (OperationHelper.hasFiles(var0) || var0.hasModuleTargets()) {
            File var2 = new File(var1.getAbsoluteSourcePath());
            if (var2.isFile() && J2EEUtils.isValidArchiveName(var2.getName())) {
               Loggable var3;
               if (var1.getVersionIdentifier() == null) {
                  var3 = DeployerRuntimeLogger.logPartialRedeployOfArchiveLoggable(var1.getName());
               } else {
                  var3 = DeployerRuntimeLogger.logPartialRedeployOfVersionedArchiveLoggable(ApplicationVersionUtils.getDisplayName((BasicDeploymentMBean)var1));
               }

               throw new DeploymentException(var3.getMessage());
            }
         }

      }
   }
}
