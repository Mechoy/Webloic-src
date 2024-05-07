package weblogic.deploy.api.spi.deploy.internal;

import java.io.File;
import javax.enterprise.deploy.spi.TargetModuleID;
import weblogic.deploy.api.shared.WebLogicCommandType;
import weblogic.deploy.api.spi.DeploymentOptions;
import weblogic.deploy.api.spi.WebLogicDeploymentManager;

public class ActivateOperation extends DeployOperation {
   public ActivateOperation(WebLogicDeploymentManager var1, TargetModuleID[] var2, File var3, File var4, DeploymentOptions var5) {
      super(var1, var2, var3, var4, var5);
      this.cmd = WebLogicCommandType.ACTIVATE;
   }

   protected void initializeTask() throws Throwable {
      this.task = this.dm.getServerConnection().getHelper().getDeployer().activate(this.paths.getArchive().getPath(), this.appName, this.options.getStageMode(), this.info, this.dm.getTaskId(), false);
   }
}
