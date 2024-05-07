package weblogic.deploy.api.spi.deploy.internal;

import java.io.File;
import javax.enterprise.deploy.spi.Target;
import javax.enterprise.deploy.spi.TargetModuleID;
import weblogic.deploy.api.shared.WebLogicCommandType;
import weblogic.deploy.api.spi.DeploymentOptions;
import weblogic.deploy.api.spi.WebLogicDeploymentManager;

public class DeployOperation extends DistributeOperation {
   public DeployOperation(WebLogicDeploymentManager var1, Target[] var2, File var3, File var4, DeploymentOptions var5) {
      super(var1, var2, var3, var4, var5);
      this.cmd = WebLogicCommandType.DEPLOY;
   }

   public DeployOperation(WebLogicDeploymentManager var1, TargetModuleID[] var2, File var3, File var4, DeploymentOptions var5) {
      super(var1, var2, var3, var4, var5);
      this.cmd = WebLogicCommandType.DEPLOY;
   }

   protected void initializeTask() throws Throwable {
      this.task = this.dm.getServerConnection().getHelper().getDeployer().deploy(this.paths.getArchive().getPath(), this.appName, this.options.getStageMode(), this.info, this.dm.getTaskId(), false);
   }

   protected boolean defaultTmidsWithOnlySubModules() {
      return this.tmidsHaveOnlySubModules();
   }
}
