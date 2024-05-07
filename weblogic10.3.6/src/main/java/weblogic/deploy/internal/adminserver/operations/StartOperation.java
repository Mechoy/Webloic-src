package weblogic.deploy.internal.adminserver.operations;

import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.management.ManagementException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.deploy.DeploymentData;

public final class StartOperation extends AbstractOperation {
   public StartOperation() {
      this.controlOperation = true;
      this.taskType = 7;
   }

   protected final AppDeploymentMBean updateConfiguration(String var1, String var2, String var3, DeploymentData var4, String var5, boolean var6) throws ManagementException {
      String var8 = OperationHelper.getVersionIdFromData(var4, (String)null);
      String var9 = OperationHelper.getTaskString(this.taskType);
      DomainMBean var10 = this.beanFactory.getEditableDomain();
      Object var11 = null;
      if (isDebugEnabled()) {
         this.printDebugStartMessage(var1, var2, var8, var4, var5, var9, (String)var11);
      }

      AppDeploymentMBean var7 = ApplicationVersionUtils.getAppDeployment(var10, var2, var8);
      OperationHelper.assertAppIsNonNull(var7, var2, var8, var9);
      OperationHelper.assertAppIsNotRetired(var7, var2, var8, var9);
      OperationHelper.checkForClusterTargetSubset(var10, var4, var7, var9, this.controlOperation);
      this.validateAllTargets(var7, var4, var2, var8, var9);
      var7 = OperationHelper.getActiveVersionIfNeeded(var10, var8, var7, var2, var4, var9);
      return var7;
   }

   protected AbstractOperation createCopy() {
      return new StartOperation();
   }

   protected boolean isRemote(DeploymentData var1) {
      return false;
   }
}
