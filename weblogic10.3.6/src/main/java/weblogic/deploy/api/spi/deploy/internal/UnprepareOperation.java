package weblogic.deploy.api.spi.deploy.internal;

import javax.enterprise.deploy.spi.TargetModuleID;
import weblogic.deploy.api.spi.DeploymentOptions;
import weblogic.deploy.api.spi.WebLogicDeploymentManager;

public final class UnprepareOperation extends StopOperation {
   public UnprepareOperation(WebLogicDeploymentManager var1, TargetModuleID[] var2, DeploymentOptions var3) {
      super(var1, var2, var3);
   }

   protected void initializeTask() throws Throwable {
      this.task = this.dm.getServerConnection().getHelper().getDeployer().unprepare(this.appName, this.info, this.dm.getTaskId(), false);
   }
}
