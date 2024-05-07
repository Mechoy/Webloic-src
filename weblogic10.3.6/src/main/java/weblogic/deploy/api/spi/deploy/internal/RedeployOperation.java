package weblogic.deploy.api.spi.deploy.internal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.enterprise.deploy.shared.CommandType;
import javax.enterprise.deploy.spi.TargetModuleID;
import javax.enterprise.deploy.spi.status.ProgressObject;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.deploy.api.internal.utils.ConfigHelper;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.internal.utils.InstallDir;
import weblogic.deploy.api.spi.DeploymentOptions;
import weblogic.deploy.api.spi.WebLogicDeploymentManager;
import weblogic.deploy.api.spi.exceptions.ServerConnectionException;
import weblogic.utils.FileUtils;

public class RedeployOperation extends BasicOperation {
   protected String path;
   protected boolean inPlace;
   protected boolean streams = false;

   protected RedeployOperation(WebLogicDeploymentManager var1, InputStream var2, InputStream var3, DeploymentOptions var4) {
      super(var1, var2, var3, var4);
   }

   public RedeployOperation(WebLogicDeploymentManager var1, TargetModuleID[] var2, File var3, File var4, DeploymentOptions var5) {
      super(var1, var3, var4, var5);
      this.tmids = var2;
      this.cmd = CommandType.REDEPLOY;
   }

   public ProgressObject run() throws IllegalStateException {
      return super.run();
   }

   protected void initializeTask() throws Throwable {
      if (debug) {
         Debug.say("Starting task with path: " + this.path);
      }

      if (this.path == null) {
         this.task = this.dm.getServerConnection().getHelper().getDeployer().redeploy(this.appName, this.info, this.dm.getTaskId(), false);
      } else {
         this.task = this.dm.getServerConnection().getHelper().getDeployer().redeploy(this.path, this.appName, this.info, this.dm.getTaskId(), false);
      }

   }

   protected void setupPaths() throws FailedOperationException {
      if (!this.streams) {
         try {
            this.paths = new InstallDir(ConfigHelper.getAppName(this.tmids, this.options), ConfigHelper.getAppRootFromPlan(this.planBean), false);
            if (this.moduleArchive != null) {
               this.paths.setArchive(this.moduleArchive.getCanonicalFile());
            }

            if (this.plan != null) {
               this.paths.setPlan(this.plan.getCanonicalFile());
            }

            ConfigHelper.initPlanDirFromPlan(this.planBean, this.paths);
         } catch (IOException var2) {
            throw new FailedOperationException(this.failOperation(var2));
         }
      }

      this.inPlace = this.moduleArchive == null;
      if (debug) {
         Debug.say("in place redeploy: " + this.inPlace + " from moduleArchive: " + this.moduleArchive);
      }

      if (this.inPlace) {
         this.path = null;
      } else {
         this.path = this.paths.getArchive().getPath();
         this.appName = ApplicationVersionUtils.getApplicationName(this.appName);
      }

      if (debug) {
         Debug.say("redeploy src path: " + this.path);
      }

   }

   protected boolean isInPlaceApp(String var1, String var2, String var3) {
      String var4 = ApplicationVersionUtils.getVersionId(var2, var3);
      String var5 = ApplicationVersionUtils.getVersionId(var1);
      return this.isInPlace(var4, var5);
   }

   protected boolean isInPlace(String var1, String var2) {
      if (var1 != null) {
         return var1.equals(var2);
      } else {
         return var2 != null ? var2.equals(var1) : true;
      }
   }

   protected void uploadFiles() throws IOException {
      if (this.paths != null) {
         String var1;
         if (this.moduleArchive != null) {
            var1 = this.getAppIdForUpload();
            this.paths = this.dm.getServerConnection().upload(this.paths, var1, (String[])null);
            this.path = this.paths.getArchive().getPath();
         } else if (this.plan != null) {
            var1 = this.uploadConfig();
            if (debug) {
               Debug.say("Updating " + this.appName + " from " + var1);
            }
         }

      }
   }

   private String uploadConfig() throws ServerConnectionException {
      String var2 = this.plan.getAbsoluteFile().getPath();
      String var1 = var2;
      if (!this.dm.isLocal()) {
         File var3 = new File(var2);
         if (!this.paths.isInConfigDir(var3)) {
            File var4 = new File(this.paths.getConfigDir(), var3.getName());
            if (debug) {
               Debug.say("Copying " + var3.getPath() + " to config area, " + var4.getPath());
            }

            try {
               FileUtils.copy(var3, var4);
            } catch (IOException var6) {
               throw new ServerConnectionException(var6.toString());
            }

            this.paths.setPlan(var4);
         }

         var2 = this.paths.getConfigDir().getPath();
         String var7 = ApplicationVersionUtils.getApplicationId(this.appName, this.options.getVersionIdentifier());
         var1 = this.dm.getServerConnection().uploadPlan(var2, var7);
         this.paths.setConfigDir(new File(var1));
         this.paths.setPlan(new File(var1, this.plan.getName()));
      }

      return var1;
   }
}
