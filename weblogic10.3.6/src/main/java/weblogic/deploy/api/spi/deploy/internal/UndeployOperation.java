package weblogic.deploy.api.spi.deploy.internal;

import javax.enterprise.deploy.shared.CommandType;
import javax.enterprise.deploy.spi.TargetModuleID;
import weblogic.deploy.api.spi.DeploymentOptions;
import weblogic.deploy.api.spi.WebLogicDeploymentManager;

public class UndeployOperation extends BasicOperation {
   public UndeployOperation(WebLogicDeploymentManager var1, TargetModuleID[] var2, DeploymentOptions var3) {
      super(var1, var3);
      this.cmd = CommandType.UNDEPLOY;
      this.tmids = var2;
   }

   protected void initializeTask() throws Throwable {
      this.task = this.dm.getServerConnection().getHelper().getDeployer().undeploy(this.appName, this.info, this.dm.getTaskId(), false);
   }

   protected final void validateForNoTMIDs() {
   }
}
