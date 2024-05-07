package weblogic.deploy.api.spi.deploy.internal;

import java.io.IOException;
import java.io.InputStream;
import javax.enterprise.deploy.shared.CommandType;
import javax.enterprise.deploy.shared.ModuleType;
import javax.enterprise.deploy.spi.Target;
import weblogic.deploy.api.internal.utils.ConfigHelper;
import weblogic.deploy.api.spi.DeploymentOptions;
import weblogic.deploy.api.spi.WebLogicDeploymentManager;

public class DistributeStreamsOperation extends DistributeOperation {
   public DistributeStreamsOperation(WebLogicDeploymentManager var1, Target[] var2, ModuleType var3, InputStream var4, InputStream var5, DeploymentOptions var6) {
      super(var1, var4, var5, var6);
      this.cmd = CommandType.DISTRIBUTE;
      this.streams = true;
      this.distributeStreamModuleType = var3;
      this.targetList = var2;
      this.targetMode = true;
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
      } catch (IOException var2) {
         throw new FailedOperationException(this.failOperation(var2));
      }
   }
}
