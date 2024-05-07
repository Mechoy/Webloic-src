package weblogic.deploy.internal.adminserver.operations;

import java.io.FileNotFoundException;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.logging.Loggable;
import weblogic.management.ManagementException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.deploy.DeploymentData;
import weblogic.management.deploy.DeploymentTaskRuntime;
import weblogic.management.deploy.internal.DeployerRuntimeLogger;
import weblogic.security.acl.internal.AuthenticatedSubject;

public final class DistributeOperation extends AbstractOperation {
   private boolean removeBeansOnFailure = false;
   private AppDeploymentMBean createdApp = null;

   public DistributeOperation() {
      this.taskType = 6;
   }

   protected final AppDeploymentMBean updateConfiguration(String var1, String var2, String var3, DeploymentData var4, String var5, boolean var6) throws ManagementException {
      String var8 = null;
      String var9 = OperationHelper.getAppName(var2, var4, var1);
      String var10 = OperationHelper.getTaskString(this.taskType);
      DomainMBean var11 = this.beanFactory.getEditableDomain();
      boolean var12 = var4.getDeploymentOptions().isNoVersion();
      OperationHelper.assertNameIsNonNull(var9, var10);
      if (!var12) {
         var8 = OperationHelper.getAndValidateVersionIdWithSrc(var4, (String)null, var1, var9);
      }

      if (isDebugEnabled()) {
         this.printDebugStartMessage(var1, var9, var8, var4, var5, var10, var3);
      }

      AppDeploymentMBean var7 = ApplicationVersionUtils.getAppDeployment(this.beanFactory.getEditableDomain(), var9, var8);
      if (var7 != null) {
         OperationHelper.validateNonVersionWithVersion(var8, var7, var9, var10);
         OperationHelper.validateTargets(var11, var4, var7, var10);
         OperationHelper.assertNoChangeInStagingMode(var3, var7);
         OperationHelper.validatePath(var1, var7);
         var7 = this.createOrReconcileMBeans(var1, var9, var4, var8, var7, var3);
      } else {
         OperationHelper.validateRetireTimeout(var11, var9, var8, var4);
         OperationHelper.validateVersionWithNonVersion(var11, var8, var9, var10);
         addAdminServerAsDefaultTarget(var4);
         OperationHelper.assertSourceIsNonNull(var1, var9, var8);
         if (!var6) {
            this.removeBeansOnFailure = true;
         }

         var7 = this.createMBeansForDistribute(var7, var8, var9, var1, var4, var3);
         this.createdApp = var7;
      }

      return var7;
   }

   protected AbstractOperation createCopy() {
      return new DistributeOperation();
   }

   protected void mergeWithUndeploy(AbstractOperation var1) throws ManagementException {
      super.mergeWithUndeploy(var1);
      var1.mergeUndeployWithDistributeOrDeployOrRedeploy(this);
   }

   protected void mergeWithRedeploy(AbstractOperation var1) throws ManagementException {
      super.mergeWithRedeploy(var1);
      var1.mergeWithDistribute(this);
   }

   protected void mergeWithDeploy(AbstractOperation var1) throws ManagementException {
      super.mergeWithDeploy(var1);
      var1.mergeWithDistribute(this);
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
      this.mergeWithSameOperationType(var1);
   }

   private AppDeploymentMBean createMBeansForDistribute(AppDeploymentMBean var1, String var2, String var3, String var4, DeploymentData var5, String var6) throws ManagementException {
      try {
         var1 = this.createMBeans(var4, var3, var5, var2);
         if (var6 != null) {
            var1.setStagingMode(var6);
         }

         return var1;
      } catch (FileNotFoundException var9) {
         Loggable var8 = DeployerRuntimeLogger.logInvalidSourceLoggable(var4, var3, var9.getMessage());
         var8.log();
         if (var1 != null) {
            this.beanFactory.removeMBean(var1);
         }

         throw new ManagementException(var8.getMessage(), var9);
      }
   }

   protected final boolean removeBeans() {
      return this.removeBeansOnFailure;
   }

   public void rollback(AuthenticatedSubject var1) {
      if (this.removeBeans() && this.createdApp != null) {
         this.editAccessHelper.rollback(this.createdApp, this.beanFactory, var1);
      }

      this.createdApp = null;
   }

   protected String getAutoDeployErrorMsg(String var1) {
      Loggable var2 = DeployerRuntimeLogger.invalidDistributeOnAutodeployedAppLoggable(var1);
      return var2.getMessage();
   }
}
