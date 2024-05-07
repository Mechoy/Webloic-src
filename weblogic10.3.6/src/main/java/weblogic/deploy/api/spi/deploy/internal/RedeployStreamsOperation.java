package weblogic.deploy.api.spi.deploy.internal;

import java.io.IOException;
import java.io.InputStream;
import javax.enterprise.deploy.shared.CommandType;
import javax.enterprise.deploy.spi.TargetModuleID;
import weblogic.deploy.api.internal.utils.ConfigHelper;
import weblogic.deploy.api.spi.DeploymentOptions;
import weblogic.deploy.api.spi.WebLogicDeploymentManager;

public class RedeployStreamsOperation extends RedeployOperation {
   public RedeployStreamsOperation(WebLogicDeploymentManager var1, TargetModuleID[] var2, InputStream var3, InputStream var4, DeploymentOptions var5) {
      super(var1, var3, var4, var5);
      this.streams = true;
      this.tmids = var2;
      this.cmd = CommandType.REDEPLOY;
   }

   protected void validateParams() throws FailedOperationException {
      super.validateParams();

      try {
         ConfigHelper.checkParam("moduleStream", this.moduleStream);
      } catch (IllegalArgumentException var2) {
         throw new FailedOperationException(this.failOperation(var2));
      }
   }

   protected void setupPaths() throws FailedOperationException {
      try {
         this.paths = this.createRootFromStreams(this.moduleStream, this.planBean, this.options);
         this.moduleArchive = this.paths.getArchive();
         super.setupPaths();
      } catch (IOException var2) {
         throw new FailedOperationException(this.failOperation(var2));
      }
   }
}
