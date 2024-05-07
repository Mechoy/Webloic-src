package weblogic.deploy.api.spi.deploy.internal;

import java.io.File;
import java.io.IOException;
import javax.enterprise.deploy.shared.CommandType;
import javax.enterprise.deploy.spi.TargetModuleID;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.deploy.api.internal.SPIDeployerLogger;
import weblogic.deploy.api.internal.utils.ConfigHelper;
import weblogic.deploy.api.spi.DeploymentOptions;
import weblogic.deploy.api.spi.WebLogicDeploymentManager;

public class RedeployDeltaOperation extends BasicOperation {
   protected String path;
   protected boolean inPlace;
   private boolean delete;
   private String[] delta;

   public RedeployDeltaOperation(WebLogicDeploymentManager var1, TargetModuleID[] var2, File var3, String[] var4, boolean var5, DeploymentOptions var6) {
      super(var1, (File)var3, (File)null, var6);
      this.tmids = var2;
      this.cmd = CommandType.REDEPLOY;
      this.delete = var5;
      this.delta = var4;
   }

   protected void uploadFiles() throws IOException {
      String var1 = null;
      if (this.moduleArchive != null) {
         var1 = this.moduleArchive.getCanonicalPath();
      }

      String var2 = ApplicationVersionUtils.getApplicationId(this.appName, this.options.getVersionIdentifier());
      this.dm.getServerConnection().uploadApp(var1, var2, this.delta);
   }

   protected void buildDeploymentData() {
      super.buildDeploymentData();
      this.info.setFile(this.delta);
      this.info.setDelete(this.delete);
   }

   protected void initializeTask() throws Throwable {
      this.task = this.dm.getServerConnection().getHelper().getDeployer().redeploy(this.appName, this.info, this.dm.getTaskId(), false);
   }

   protected void validateParams() throws FailedOperationException {
      try {
         super.validateParams();
         if (!this.dm.isLocal()) {
            ConfigHelper.checkParam("moduleArchive", this.moduleArchive);
         }

         ConfigHelper.checkParam("delta", this.delta);
         if (this.delta.length == 0) {
            throw new IllegalArgumentException(SPIDeployerLogger.getNoDelta(this.cmd.toString()));
         }
      } catch (IllegalArgumentException var2) {
         throw new FailedOperationException(this.failOperation(var2));
      }
   }
}
