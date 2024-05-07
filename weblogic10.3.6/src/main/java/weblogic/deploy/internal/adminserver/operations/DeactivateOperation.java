package weblogic.deploy.internal.adminserver.operations;

import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.management.ManagementException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.deploy.DeploymentData;

public final class DeactivateOperation extends AbstractOperation {
   public DeactivateOperation() {
      this.controlOperation = true;
      this.taskType = 3;
   }

   protected final AppDeploymentMBean updateConfiguration(String var1, String var2, String var3, DeploymentData var4, String var5, boolean var6) throws ManagementException {
      String var8 = OperationHelper.ensureAppName(var2);
      String var9 = OperationHelper.getTaskString(this.taskType);
      Object var10 = null;
      if (isDebugEnabled()) {
         this.printDebugStartMessage(var1, var8, (String)var10, var4, var5, var9, var3);
      }

      OperationHelper.assertNameIsNonNull(var8, var9);
      OperationHelper.validateVersionForDeprecatedOp(var4, var2, var9, 8);
      AppDeploymentMBean var7 = ApplicationVersionUtils.getAppDeployment(this.beanFactory.getEditableDomain(), var8, (String)null);
      OperationHelper.assertAppIsNonNull(var7, var8, (String)var10, var9);
      return var7;
   }

   protected AbstractOperation createCopy() {
      return new DeactivateOperation();
   }

   protected boolean isRemote(DeploymentData var1) {
      return false;
   }
}
