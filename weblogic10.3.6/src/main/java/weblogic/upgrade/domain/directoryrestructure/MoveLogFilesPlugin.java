package weblogic.upgrade.domain.directoryrestructure;

import com.bea.plateng.plugin.PlugInContext;
import com.bea.plateng.plugin.PlugInException;
import java.io.File;
import java.io.IOException;
import java.security.AccessController;
import weblogic.logging.WeblogicLogfileFilter;
import weblogic.management.DomainDir;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.LogMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.upgrade.domain.DomainPlugInConstants;
import weblogic.utils.FileUtils;

public class MoveLogFilesPlugin extends BaseFileRestructurePlugin {
   private static final String SAVED_LOGS_DIR = "pre-90-logs";
   private File upgradeDir;
   private File upgradeAbsDir;
   private String upgradeDirPath;
   private DomainMBean domainBean;
   private String[] serverNames;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public void prepare(PlugInContext var1) throws PlugInException {
      try {
         super.prepare(var1);
      } catch (PlugInException var4) {
         throw var4;
      } catch (Exception var5) {
         throw new PlugInException(this.getName(), "Prepare Exception", var5);
      }

      this.domainBean = (DomainMBean)var1.get(DomainPlugInConstants.DOMAIN_BEAN_TREE_KEY);
      if (this.domainBean == null) {
         throw this.createException("DomainDirectoryFileReorgPlugIn.exc.NoBeanTree");
      } else {
         this.upgradeDir = (File)var1.get(DomainPlugInConstants.DOMAIN_DIRECTORY_KEY);
         if (this.upgradeDir == null) {
            throw this.createException("DomainDirectoryFileReorgPlugIn.exc.NoDomainDir");
         } else {
            try {
               this.upgradeAbsDir = this.upgradeDir.getCanonicalFile();
               this.upgradeDirPath = this.upgradeDir.getCanonicalPath();
            } catch (IOException var3) {
               throw this.createException("DomainDirectoryFileReorgPlugIn.exc.DirectoryProcessingException", var3);
            }

            this.serverNames = (String[])((String[])var1.get(DomainPlugInConstants.SERVER_NAMES_KEY));
            if (this.serverNames == null) {
               throw this.createException("DomainDirectoryFileReorgPlugIn.exc.NoServerNames");
            }
         }
      }
   }

   public void execute() throws PlugInException {
      this.updateStatus("DomainDirectoryFileReorgPlugIn.msg.InExecuteMethod", "LogFile");
      this.moveDomainLogs();
      this.moveServerLogs();
      this.updateStatus("DomainDirectoryFileReorgPlugIn.msg.OutExecuteMethod", "LogFile");
   }

   private void moveDomainLogs() {
      boolean var1 = false;
      boolean var2 = false;
      LogMBean var3 = this.domainBean.getLog();
      File var4 = new File(var3.getFileName());
      String var5 = var4.getName();
      if (var3.isSet("FileName")) {
         var1 = true;
      }

      if (var4.isAbsolute()) {
         var2 = true;
      }

      File var6 = null;
      if (!var2) {
         var6 = new File(this.upgradeAbsDir, var5);
      } else {
         var6 = var4;
      }

      if (var6.exists()) {
         if (ManagementService.getPropertyService(kernelId).isAdminServer()) {
            String var7 = ManagementService.getPropertyService(kernelId).getServerName();
            this.saveOldLogs(var2, var6, var7);
         } else {
            this.updateStatus("DomainDirectoryFileReorgPlugIn.msg.DomainLogFileNotProcessedOnMS", var6);
         }
      } else {
         this.updateStatus("DomainDirectoryFileReorgPlugIn.msg.LogFileNotInDefaultLocation", var6);
      }

   }

   private void moveServerLogs() {
      boolean var1 = false;
      boolean var2 = false;

      for(int var3 = 0; var3 < this.serverNames.length; ++var3) {
         String var4 = this.serverNames[var3];
         ServerMBean var5 = this.domainBean.lookupServer(var4);
         LogMBean var6 = var5.getLog();
         File var7 = new File(var6.getFileName());
         String var8 = var7.getName();
         if (var6.isSet("FileName")) {
            var1 = true;
         } else {
            var1 = false;
         }

         if (var7.isAbsolute()) {
            var2 = true;
         } else {
            var2 = false;
         }

         new File(this.upgradeAbsDir, var4);
         File var10 = new File(this.upgradeAbsDir, var4 + File.separator + var8);
         if (var10.exists()) {
            this.saveOldLogs(var2, var10, var4);
         } else {
            this.updateStatus("DomainDirectoryFileReorgPlugIn.msg.LogFileNotInDefaultLocation", var10);
         }
      }

   }

   private void saveOldLogs(boolean var1, File var2, String var3) {
      File var4 = null;
      if (!var1) {
         var4 = new File(DomainDir.getPathRelativeServersLogsDir(var3, "pre-90-logs"));
      } else {
         var4 = new File(var2.getParentFile(), "pre-90-logs");
      }

      if (!var4.exists()) {
         var4.mkdirs();
      }

      this.moveFile(var2, var4);
      File[] var5 = this.getExistingRotatedLogFiles(var2.getParentFile(), var2.getName());
      if (var5 != null) {
         for(int var6 = 0; var6 < var5.length; ++var6) {
            this.moveFile(var5[var6], var4);
         }

      }
   }

   private File[] getExistingRotatedLogFiles(File var1, String var2) {
      File[] var3 = var1.listFiles(new WeblogicLogfileFilter(new File(var2)));
      return var3;
   }

   private void moveFile(File var1, File var2) {
      String[] var3 = new String[2];

      try {
         var3[0] = var1.getCanonicalPath();
         var3[1] = var2.getCanonicalPath();
         FileUtils.copy(var1, var2);
         var1.delete();
         this.updateStatus("DomainDirectoryFileReorgPlugIn.msg.LogFileMoveSucceeded", (Object[])var3);
      } catch (IOException var5) {
         this.updateStatus("DomainDirectoryFileReorgPlugIn.msg.IOException", var5.getMessage());
         this.updateStatus("DomainDirectoryFileReorgPlugIn.msg.LogFileMoveFailed", (Object[])var3);
      }

   }
}
