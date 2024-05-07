package weblogic.deploy.internal.targetserver.datamanagement;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import weblogic.deploy.common.Debug;
import weblogic.management.DomainDir;
import weblogic.utils.FileUtils;
import weblogic.utils.StackTraceUtils;

final class ConfigBackupRecoveryManager {
   private static final String CONFIG_DIRECTORY = "config";
   private static final String CONFIG_BAK_DIRECTORY;
   private static final String CONFIG_PREV_DIRECTORY;
   private static final String CONFIG_PREV_BAK_DIRECTORY;
   private static final String DELETED_FILE_INDEX;
   private static final String DOMAIN_ROOT;
   private static final String DOMAIN_BAK_ROOT;
   private boolean configBakSavedToConfigPrev;

   static ConfigBackupRecoveryManager getInstance() {
      return ConfigBackupRecoveryManager.Maker.singleton;
   }

   void handleBackup(File var1, String var2) throws IOException {
      this.saveConfigPrevToConfigPrevBak();
      if (var1.exists()) {
         this.saveFileToConfigBakDir(var1, var2);
      } else {
         this.appendToDeletedFilesIndex(var2);
      }

   }

   void restoreFromBackup() throws IOException {
      if (this.isDebugEnabled()) {
         this.debug("Restoring from backup: " + CONFIG_BAK_DIRECTORY);
      }

      this.deleteAddedFiles();
      File var1 = new File(DOMAIN_BAK_ROOT, CONFIG_BAK_DIRECTORY);
      if (var1.exists()) {
         File var2 = new File(DOMAIN_ROOT, "config");
         String[] var3 = var1.list();
         if (var3 != null && var3.length > 0) {
            FileUtils.copy(var1, var2);
         }

         this.deleteConfigBakDir();
      }

      this.restoreConfigPrevFromConfigPrevBakDir();
      this.deleteConfigPrevBakDir();
      this.configBakSavedToConfigPrev = false;
      if (this.isDebugEnabled()) {
         this.debug("Restored from backup");
      }

   }

   void deleteConfigBakFile(String var1) {
      File var2 = new File(DOMAIN_BAK_ROOT, CONFIG_BAK_DIRECTORY + var1);
      if (var2.exists()) {
         this.deleteFileOrDir(var2);
      }
   }

   void saveConfigBakDirToConfigPrevDir() throws IOException {
      File var1 = new File(DOMAIN_BAK_ROOT, CONFIG_BAK_DIRECTORY);
      File var2 = new File(DOMAIN_BAK_ROOT, CONFIG_PREV_DIRECTORY);
      if (var1.exists()) {
         this.renameFileOrDir(var1, var2);
         this.deleteConfigPrevBakDir();
         this.configBakSavedToConfigPrev = false;
      }
   }

   private final void debug(String var1) {
      Debug.deploymentDebug(var1);
   }

   private final boolean isDebugEnabled() {
      return Debug.isDeploymentDebugEnabled();
   }

   private void saveFileToConfigBakDir(File var1, String var2) throws IOException {
      try {
         this.copyFileToConfigBakDir(var2, var1);
      } catch (Throwable var4) {
         this.deleteConfigPrevBakDir();
         throw new IOException(StackTraceUtils.throwable2StackTrace(var4));
      }
   }

   private void saveConfigPrevToConfigPrevBak() throws IOException {
      if (!this.configBakSavedToConfigPrev) {
         this.saveConfigPrevDirToConfigPrevBakDir();
         this.cleanConfigBakDir();
         this.configBakSavedToConfigPrev = true;
      }

   }

   private void copyFileToConfigBakDir(String var1, File var2) throws IOException {
      String var3;
      if (var1.startsWith("config")) {
         var3 = var1.substring("config".length() + 1);
      } else {
         var3 = var1;
      }

      String var4 = CONFIG_BAK_DIRECTORY + var3;
      File var5 = new File(DOMAIN_BAK_ROOT, var4);

      try {
         if (this.isDebugEnabled()) {
            this.debug("Saving file : '" + var2.getCanonicalPath() + "' to: '" + var5.getCanonicalPath() + "'");
         }

         FileUtils.copy(var2, var5);
      } catch (Throwable var7) {
         if (this.isDebugEnabled()) {
            this.debug("Back up failed due to " + var7.getMessage() + " " + StackTraceUtils.throwable2StackTrace(var7));
         }

         this.deleteConfigBakFile(var3);
         throw new IOException(StackTraceUtils.throwable2StackTrace(var7));
      }
   }

   private void saveConfigPrevDirToConfigPrevBakDir() throws IOException {
      this.deleteConfigPrevBakDir();
      File var1 = new File(DOMAIN_BAK_ROOT, CONFIG_PREV_BAK_DIRECTORY);
      File var2 = new File(DOMAIN_BAK_ROOT, CONFIG_PREV_DIRECTORY);
      this.renameFileOrDir(var2, var1);
   }

   private void appendToDeletedFilesIndex(String var1) throws IOException {
      BufferedWriter var2 = null;

      try {
         this.saveConfigPrevToConfigPrevBak();
         File var3 = new File(DOMAIN_BAK_ROOT, CONFIG_BAK_DIRECTORY);
         if (!var3.exists()) {
            var3.mkdirs();
         }

         File var4 = new File(DOMAIN_BAK_ROOT, DELETED_FILE_INDEX);
         if (!var4.exists()) {
            var4.createNewFile();
         }

         var2 = new BufferedWriter(new FileWriter(var4, true));
         var2.write(var1);
         var2.newLine();
         var2.flush();
      } catch (Throwable var12) {
         this.deleteConfigPrevBakDir();
         throw new IOException(StackTraceUtils.throwable2StackTrace(var12));
      } finally {
         if (var2 != null) {
            try {
               var2.close();
            } catch (Exception var11) {
            }
         }

      }

   }

   private void deleteAddedFiles() throws IOException {
      File var1 = new File(DOMAIN_BAK_ROOT, DELETED_FILE_INDEX);
      if (var1.exists()) {
         BufferedReader var2 = null;

         try {
            var2 = new BufferedReader(new InputStreamReader(new FileInputStream(var1)));

            String var3;
            while((var3 = var2.readLine()) != null) {
               FileUtils.remove(new File(DOMAIN_ROOT, var3));
            }
         } finally {
            if (var2 != null) {
               try {
                  var2.close();
               } catch (Exception var9) {
               }
            }

         }

         FileUtils.remove(var1);
      }
   }

   private void cleanConfigBakDir() {
      File var1 = new File(DOMAIN_BAK_ROOT, CONFIG_BAK_DIRECTORY);
      if (var1.exists()) {
         FileUtils.remove(var1, FileUtils.STAR);
      }

   }

   private void deleteConfigBakDir() {
      File var1 = new File(DOMAIN_BAK_ROOT, CONFIG_BAK_DIRECTORY);
      this.deleteFileOrDir(var1);
   }

   private void deleteConfigPrevBakDir() {
      File var1 = new File(DOMAIN_BAK_ROOT, CONFIG_PREV_BAK_DIRECTORY);
      if (var1.exists()) {
         this.deleteFileOrDir(var1);
      }
   }

   private void deleteFileOrDir(File var1) {
      if (var1.exists()) {
         FileUtils.remove(var1);
      }

   }

   private boolean renameFileOrDir(File var1, File var2) throws IOException {
      if (!var1.exists()) {
         if (this.isDebugEnabled()) {
            this.debug("Cannot rename '" + var1.getCanonicalPath() + "' as it " + "does not exist");
         }

         return false;
      } else {
         boolean var3 = var1.renameTo(var2);
         if (var3) {
            if (this.isDebugEnabled()) {
               this.debug("Renamed '" + var1.getCanonicalPath() + "' to: '" + var2.getCanonicalPath());
            }
         } else if (this.isDebugEnabled()) {
            this.debug("Failed to rename '" + var1.getCanonicalPath() + "' to: " + var2.getCanonicalPath());
         }

         return var3;
      }
   }

   private void restoreConfigPrevFromConfigPrevBakDir() throws IOException {
      File var1 = new File(DOMAIN_BAK_ROOT, CONFIG_PREV_DIRECTORY);
      File var2 = new File(DOMAIN_BAK_ROOT, CONFIG_PREV_BAK_DIRECTORY);
      if (var2.exists()) {
         this.renameFileOrDir(var2, var1);
      }
   }

   static {
      CONFIG_BAK_DIRECTORY = "config_bak" + File.separator;
      CONFIG_PREV_DIRECTORY = "config_prev" + File.separator;
      CONFIG_PREV_BAK_DIRECTORY = "config_prev_bak" + File.separator;
      DELETED_FILE_INDEX = CONFIG_BAK_DIRECTORY + File.separator + "__deleted_files_index__";
      DOMAIN_ROOT = DomainDir.getRootDir();
      DOMAIN_BAK_ROOT = DomainDir.getServersDir() + File.separator + "domain_bak";
   }

   private static class Maker {
      private static final ConfigBackupRecoveryManager singleton = new ConfigBackupRecoveryManager();
   }
}
