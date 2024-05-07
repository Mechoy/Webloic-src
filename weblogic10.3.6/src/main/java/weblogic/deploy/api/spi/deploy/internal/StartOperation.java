package weblogic.deploy.api.spi.deploy.internal;

import javax.enterprise.deploy.shared.CommandType;
import javax.enterprise.deploy.spi.TargetModuleID;
import weblogic.deploy.api.spi.DeploymentOptions;
import weblogic.deploy.api.spi.WebLogicDeploymentManager;

public class StartOperation extends BasicOperation {
   public StartOperation(WebLogicDeploymentManager var1, TargetModuleID[] var2, DeploymentOptions var3) {
      super(var1, var3);
      this.cmd = CommandType.START;
      this.tmids = var2;
   }

   protected void initializeTask() throws Throwable {
      this.task = this.dm.getServerConnection().getHelper().getDeployer().start(this.appName, this.info, this.dm.getTaskId(), false);
   }
}
