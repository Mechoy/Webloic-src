package weblogic.deploy.internal.adminserver.operations;

import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.logging.Loggable;
import weblogic.management.ManagementException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.deploy.DeploymentData;
import weblogic.management.deploy.DeploymentTaskRuntime;
import weblogic.management.deploy.internal.DeployerRuntimeLogger;
import weblogic.security.acl.internal.AuthenticatedSubject;

public class ActivateOperation extends AbstractOperation {
   private boolean removeBeansOnFailure;
   private boolean redeployWithSource;
   private AppDeploymentMBean createdApp;

   public ActivateOperation() {
      this.removeBeansOnFailure = false;
      this.redeployWithSource = false;
      this.createdApp = null;
      this.taskType = 1;
   }

   public ActivateOperation(boolean var1) {
      this();
      this.redeployWithSource = var1;
   }

   protected AppDeploymentMBean updateConfiguration(String var1, String var2, String var3, DeploymentData var4, String var5, boolean var6) throws ManagementException {
      String var8 = null;
      String var9 = OperationHelper.getAppName(var2, var4, var1);
      String var10 = OperationHelper.getTaskString(this.taskType);
      DomainMBean var11 = this.beanFactory.getEditableDomain();
      boolean var12 = var4.getDeploymentOptions().isNoVersion();
      OperationHelper.assertNameIsNonNull(var9, var10);
      if (isDebugEnabled()) {
         this.printDebugStartMessage(var1, var9, var8, var4, var5, var10, var3);
      }

      if (!var12) {
         var8 = OperationHelper.getAndValidateVersionIdWithSrc(var4, var2, var1, var9);
      }

      AppDeploymentMBean var7 = ApplicationVersionUtils.getAppDeployment(var11, var9, var8);
      if (var7 != null) {
         if (var1 == null) {
            var7 = OperationHelper.getActiveVersionIfNeeded(var11, var8, var7, var9, var4, var10);
            var8 = var7.getVersionIdentifier();
            if (!var12) {
               OperationHelper.validateSourceVersion(var7, var4, var9);
            }
         }

         OperationHelper.validateNonVersionWithVersion(var8, var7, var9, var10);
         OperationHelper.assertNoChangeInStagingMode(var3, var7);
         OperationHelper.validateTargets(var11, var4, var7, var10);
         OperationHelper.validatePath(var1, var7);
      } else {
         OperationHelper.validateRetireTimeout(var11, var9, var8, var4);
         OperationHelper.validateVersionIdFormat(var9, var8);
         OperationHelper.validateVersionWithNonVersion(var11, var8, var9, var10);
         if (!OperationHelper.isLibrary(var4)) {
            OperationHelper.validateMaxAppVersions(var11, var9, var8);
            OperationHelper.validateVersionTargets(var11, var9, var4, var8);
         }

         OperationHelper.validateVersionStagingAndPath(var11, var1, var9, var8);
         OperationHelper.assertSourceIsNonNull(var1, var9, var8);
         if (!var6) {
            this.removeBeansOnFailure = true;
         }
      }

      OperationHelper.validateDeployWhileRetire(var9, var8, var7);
      if (var1 != null) {
         addAdminServerAsDefaultTarget(var4);
         var7 = this.createOrReconcileMBeans(var1, var9, var4, var8, var7, var3);
         if (this.removeBeans()) {
            this.createdApp = var7;
         }

         invalidateCache(var7);
      }

      OperationHelper.validateModuleType(var9, var8, var7);
      return var7;
   }

   protected AbstractOperation createCopy() {
      return new ActivateOperation();
   }

   protected void mergeWithUndeploy(AbstractOperation var1) throws ManagementException {
      super.mergeWithUndeploy(var1);
      var1.mergeUndeployWithDistributeOrDeployOrRedeploy(this);
   }

   protected void mergeWithRedeploy(AbstractOperation var1) throws ManagementException {
      super.mergeWithRedeploy(var1);
      var1.mergeWithDeploy(this);
   }

   protected void mergeWithDeploy(AbstractOperation var1) throws ManagementException {
      super.mergeWithDeploy(var1);
      this.mergeWithSameOperationType(var1);
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

   protected boolean isSameOperationType(AbstractOperation var1) {
      return var1 instanceof ActivateOperation;
   }

   protected void checkVersionSupport(DeploymentData var1, String var2, String var3) throws ManagementException {
      OperationHelper.validateVersionForDeprecatedOp(var1, var2, var3, 7);
   }

   protected boolean removeBeans() {
      return this.removeBeansOnFailure;
   }

   public void rollback(AuthenticatedSubject var1) {
      if (this.removeBeans() && this.createdApp != null) {
         this.editAccessHelper.rollback(this.createdApp, this.beanFactory, var1);
      }

      this.createdApp = null;
   }

   protected String getAutoDeployErrorMsg(String var1) {
      Loggable var2 = DeployerRuntimeLogger.invalidRedeployOnAutodeployedAppLoggable(var1);
      return var2.getMessage();
   }
}
