package weblogic.deploy.api.spi.deploy.internal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.deploy.shared.ModuleType;
import javax.enterprise.deploy.spi.TargetModuleID;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.deploy.api.internal.utils.ConfigHelper;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.internal.utils.InstallDir;
import weblogic.deploy.api.shared.WebLogicCommandType;
import weblogic.deploy.api.spi.DeploymentOptions;
import weblogic.deploy.api.spi.WebLogicDeploymentManager;
import weblogic.deploy.api.spi.exceptions.ServerConnectionException;
import weblogic.j2ee.descriptor.wl.ModuleDescriptorBean;
import weblogic.j2ee.descriptor.wl.ModuleOverrideBean;
import weblogic.utils.FileUtils;

public class UpdateOperation extends RedeployOperation {
   protected String path;
   protected boolean inPlace;
   private String[] delta;

   public UpdateOperation(WebLogicDeploymentManager var1, TargetModuleID[] var2, File var3, DeploymentOptions var4) {
      super(var1, var2, (File)null, var3, var4);
      this.tmids = var2;
      this.cmd = WebLogicCommandType.UPDATE;
   }

   public UpdateOperation(WebLogicDeploymentManager var1, TargetModuleID[] var2, File var3, String[] var4, DeploymentOptions var5) {
      super(var1, var2, (File)null, var3, var5);
      this.tmids = var2;
      this.cmd = WebLogicCommandType.UPDATE;
      this.delta = var4;
   }

   protected void validateParams() throws FailedOperationException {
      super.validateParams();

      try {
         ConfigHelper.checkParam("plan", this.plan);
      } catch (IllegalArgumentException var2) {
         throw new FailedOperationException(this.failOperation(var2));
      }
   }

   protected void uploadFiles() throws IOException {
      String var1 = this.uploadConfig();
      if (debug) {
         Debug.say("Updating " + this.appName + " from " + var1);
      }

   }

   protected void buildDeploymentData() {
      super.buildDeploymentData();
      this.info.setFile(this.delta);
      this.addDDPaths();
      this.info.setPlanUpdate(true);
   }

   protected void initializeTask() throws Throwable {
      this.task = this.dm.getServerConnection().getHelper().getDeployer().update(this.appName, this.info, this.dm.getTaskId(), false);
   }

   protected void setupPaths() throws FailedOperationException {
      try {
         this.paths = new InstallDir(ConfigHelper.getAppName(this.tmids, this.options), ConfigHelper.getAppRootFromPlan(this.planBean), false);
         this.paths.setPlan(this.plan.getCanonicalFile());
         ConfigHelper.initPlanDirFromPlan(this.planBean, this.paths);
      } catch (IOException var2) {
         throw new FailedOperationException(this.failOperation(var2));
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

   private void addDDPaths() {
      if (this.planBean != null) {
         ModuleOverrideBean[] var1 = this.planBean.getModuleOverrides();
         if (var1 != null) {
            String var2 = null;

            for(int var3 = 0; var3 < var1.length; ++var3) {
               if (this.planBean.rootModule(var1[var3].getModuleName())) {
                  var2 = var1[var3].getModuleName();
                  break;
               }
            }

            ArrayList var7 = new ArrayList();
            if (this.info.hasFiles()) {
               this.addFilesToList(this.info.getFiles(), var7);
            }

            for(int var8 = 0; var8 < var1.length; ++var8) {
               boolean var4 = ModuleType.EAR.toString().equals(var1[var8].getModuleType());
               boolean var10 = var1[var8].getModuleName().equals(var2);
               ModuleDescriptorBean[] var5 = var1[var8].getModuleDescriptors();
               if (var5 != null) {
                  for(int var9 = 0; var9 < var5.length; ++var9) {
                     String var6 = var5[var9].getUri();
                     if (!var10) {
                        var6 = var1[var8].getModuleName() + "/" + var6;
                     }

                     if (this.hasOverrides(var5[var9], var6) && !var7.contains(var6)) {
                        var7.add(var6);
                     }
                  }
               }
            }

            this.info.setFile((String[])((String[])var7.toArray(new String[var7.size()])));
            if (debug) {
               Debug.say(this.info.toString());
            }

         }
      }
   }

   private boolean hasOverrides(ModuleDescriptorBean var1, String var2) {
      if (var1.getVariableAssignments() != null && var1.getVariableAssignments().length > 0) {
         return true;
      } else if (var1.isExternal()) {
         File var3 = new File(this.paths.getConfigDir(), var2);
         if (debug) {
            Debug.say(var3.getPath() + " has external dd: " + var3.exists());
         }

         return var3.exists();
      } else {
         return false;
      }
   }

   private void addFilesToList(String[] var1, List var2) {
      if (var1 != null) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            var2.add(var1[var3]);
         }

      }
   }
}
