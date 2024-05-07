package weblogic.deploy.internal.adminserver.operations;

import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.management.ManagementException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.deploy.DeploymentData;

public final class StopOperation extends AbstractOperation {
   public StopOperation() {
      this.controlOperation = true;
      this.taskType = 8;
   }

   protected final AppDeploymentMBean updateConfiguration(String var1, String var2, String var3, DeploymentData var4, String var5, boolean var6) throws ManagementException {
      String var8 = OperationHelper.ensureAppName(var2);
      String var9 = OperationHelper.getTaskString(this.taskType);
      String var10 = OperationHelper.getVersionIdFromData(var4, var2);
      DomainMBean var11 = this.beanFactory.getEditableDomain();
      OperationHelper.assertNameIsNonNull(var8, var9);
      Object var12 = null;
      if (isDebugEnabled()) {
         this.printDebugStartMessage(var1, var8, var10, var4, var5, var9, (String)var12);
      }

      AppDeploymentMBean var7 = ApplicationVersionUtils.getAppDeployment(var11, var8, var10);
      OperationHelper.assertAppIsNonNull(var7, var8, var10, var9);
      OperationHelper.checkForClusterTargetSubset(var11, var4, var7, var9, this.controlOperation);
      this.validateAllTargets(var7, var4, var8, var10, var9);
      var7 = OperationHelper.getActiveVersionIfNeeded(var11, var10, var7, var8, var4, var9);
      return var7;
   }

   protected AbstractOperation createCopy() {
      return new StopOperation();
   }

   protected boolean isRemote(DeploymentData var1) {
      return false;
   }
}
