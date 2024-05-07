package weblogic.deploy.api.spi.deploy.internal;

import java.io.File;
import java.io.InputStream;
import javax.enterprise.deploy.shared.CommandType;
import javax.enterprise.deploy.spi.Target;
import javax.enterprise.deploy.spi.TargetModuleID;
import weblogic.deploy.api.internal.utils.ConfigHelper;
import weblogic.deploy.api.spi.DeploymentOptions;
import weblogic.deploy.api.spi.WebLogicDeploymentManager;

public class DistributeOperation extends BasicOperation {
   protected boolean targetMode;
   protected boolean streams = false;

   protected DistributeOperation(WebLogicDeploymentManager var1, InputStream var2, InputStream var3, DeploymentOptions var4) {
      super(var1, var2, var3, var4);
   }

   public DistributeOperation(WebLogicDeploymentManager var1, Target[] var2, File var3, File var4, DeploymentOptions var5) {
      super(var1, var3, var4, var5);
      this.cmd = CommandType.DISTRIBUTE;
      this.targetList = var2;
      this.targetMode = true;
   }

   public DistributeOperation(WebLogicDeploymentManager var1, Target[] var2, InputStream var3, InputStream var4, DeploymentOptions var5) {
      super(var1, var3, var4, var5);
      this.cmd = CommandType.DISTRIBUTE;
      this.targetList = var2;
      this.targetMode = true;
   }

   public DistributeOperation(WebLogicDeploymentManager var1, TargetModuleID[] var2, File var3, File var4, DeploymentOptions var5) {
      super(var1, var3, var4, var5);
      this.cmd = CommandType.DISTRIBUTE;
      this.tmids = var2;
      this.targetMode = false;
   }

   protected void initializeTask() throws Throwable {
      this.task = this.dm.getServerConnection().getHelper().getDeployer().distribute(this.paths.getArchive().getPath(), this.appName, this.info, this.dm.getTaskId(), false);
   }

   protected void buildDeploymentData() {
      if (this.targetMode) {
         this.info = this.getDeploymentData();
      } else {
         this.info = this.createDeploymentData();
      }

   }

   protected void validateParams() throws FailedOperationException {
      super.validateParams();

      try {
         if (!this.streams) {
            ConfigHelper.checkParam("moduleArchive", this.moduleArchive);
         }

      } catch (IllegalArgumentException var2) {
         throw new FailedOperationException(this.failOperation(var2));
      }
   }
}
