package weblogic.deploy.internal.adminserver.operations;

import weblogic.management.ManagementException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.deploy.DeploymentData;

public class DeployOperation extends ActivateOperation {
   public DeployOperation() {
      this.taskType = 11;
   }

   public DeployOperation(boolean var1) {
      super(var1);
      this.taskType = 11;
   }

   protected AppDeploymentMBean updateConfiguration(String var1, String var2, String var3, DeploymentData var4, String var5, boolean var6) throws ManagementException {
      return super.updateConfiguration(var1, var2, var3, var4, var5, var6);
   }

   protected AbstractOperation createCopy() {
      return new DeployOperation();
   }

   protected void checkVersionSupport(DeploymentData var1, String var2, String var3) throws ManagementException {
   }

   protected int getCreateTaskType() {
      return 11;
   }
}
