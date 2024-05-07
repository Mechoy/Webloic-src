package weblogic.deploy.internal.targetserver.datamanagement;

import java.io.File;
import java.io.IOException;
import weblogic.utils.FileUtils;
import weblogic.utils.jars.JarFileUtils;

public class PlanDataUpdate extends AppDataUpdate {
   private static final String stagePlanDir = "plan";
   private boolean isPlanExistsPreviously = false;
   private boolean isRestored = false;
   private boolean isBackupFileDeleted = false;
   private File backupFile = null;

   public PlanDataUpdate(Data var1, DataUpdateRequestInfo var2) {
      super(var1, var2);
   }

   protected void backup() {
      if (isDebugEnabled()) {
         debug(" +++ PlanDataUpdate.backup() : taking existing plan dir backup");
      }

      this.takePlanDirBackup();
   }

   protected void restore() {
      if (isDebugEnabled()) {
         debug(" +++ PlanDataUpdate.restore() : restoring plan dir from backup");
      }

      if (!this.isRestored) {
         this.restorePlanDir();
      }

   }

   protected void deleteBackup() {
      if (isDebugEnabled()) {
         debug(" +++ PlanDataUpdate.deleteBackup() : deleting backup file");
      }

      if (!this.isBackupFileDeleted && this.backupFile != null) {
         this.isBackupFileDeleted = FileUtils.remove(this.backupFile);
      }

   }

   private void takePlanDirBackup() {
      try {
         File var1 = this.getPlanDir();
         if (var1.exists()) {
            this.isPlanExistsPreviously = true;
            this.backupFile = File.createTempFile("plan", ".jar", this.getAppRoot());
            JarFileUtils.createJarFileFromDirectory(this.backupFile, var1, false);
         } else {
            this.isPlanExistsPreviously = false;
         }
      } catch (IOException var2) {
         var2.printStackTrace();
      }

   }

   private void restorePlanDir() {
      this.removeCurrentPlanDir();
      if (this.isPlanExistsPreviously) {
         File var1 = this.getPlanDir();
         var1.mkdirs();
         if (!var1.exists()) {
            return;
         }

         if (this.backupFile.exists()) {
            try {
               JarFileUtils.extract(this.backupFile, var1);
            } catch (IOException var7) {
               var7.printStackTrace();
            } finally {
               this.deleteBackup();
            }
         }
      }

      this.isRestored = true;
   }

   private boolean removeCurrentPlanDir() {
      File var1 = this.getPlanDir();
      return var1.exists() ? FileUtils.remove(var1) : false;
   }

   private File getPlanDir() {
      return new File(this.getAppRoot(), "plan");
   }

   private File getAppRoot() {
      String var1 = this.getLocalAppData().getRootLocation();
      return new File(var1);
   }
}
