package weblogic.upgrade.domain.directoryrestructure;

import com.bea.plateng.plugin.PlugInContext;
import com.bea.plateng.plugin.PlugInException;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.security.AccessController;
import weblogic.management.DomainDir;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.upgrade.domain.DomainPlugInConstants;
import weblogic.utils.FileUtils;

public class MoveSecurityFilesPlugin extends BaseFileRestructurePlugin {
   private File upgradeDir;
   private File upgradeAbsDir;
   private String upgradeDirPath;
   private DomainMBean domainBean;
   private String[] serverNames;
   private DefaultAuditLogFilenameFilter auditFilter;
   private static final String EMBEDDED_LDAP_DIR_NAME = "ldap";
   private static final String EMBEDDED_LDAP_CONF_DIR_NAME = "conf";
   private static final String EMBEDDED_LDAP_REPLICAS_NAME = "replicas.prop";
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
      this.updateStatus("DomainDirectoryFileReorgPlugIn.msg.InExecuteMethod", "SecurityFiles");
      this.moveDomainLevelFile("SerializedSystemIni.dat");
      this.moveLDIFTFiles();
      File var1 = new File(this.upgradeAbsDir.getAbsolutePath() + File.separator + "boot.properties");
      boolean var2 = var1.exists();
      String var3 = ManagementService.getPropertyService(kernelId).getServerName();

      for(int var4 = 0; var4 < this.serverNames.length; ++var4) {
         String var5 = this.serverNames[var4];
         this.domainBean.lookupServer(var5);
         if (var2 && var5.equals(var3)) {
            File var7 = new File(DomainDir.getPathRelativeServersSecurityDir(var5, "boot.properties"));
            this.copyFile(var1, var7);
         }

         this.moveAuditLogs(var5);
         this.moveLDAPTree(var5);
      }

      this.updateStatus("DomainDirectoryFileReorgPlugIn.msg.OutExecuteMethod", "SecurityFiles");
   }

   private void moveDomainLevelFile(String var1) {
      File var2 = new File(this.upgradeDirPath + File.separator + var1);
      if (var2.exists()) {
         File var3 = new File(DomainDir.getPathRelativeSecurityDir(var1));
         this.moveFile(var2, var3);
      }

   }

   private void moveDomainLevelFile(File var1) {
      if (var1.exists()) {
         File var2 = new File(DomainDir.getPathRelativeSecurityDir(var1.getName()));
         this.moveFile(var1, var2);
      }

   }

   private void moveLDIFTFiles() {
      File var1 = new File(this.upgradeAbsDir.getAbsolutePath());
      DefaultLDIFTFilenameFilter var2 = new DefaultLDIFTFilenameFilter(var1);
      File[] var3 = var1.listFiles(var2);
      if (var3 != null && var3.length != 0) {
         new File(DomainDir.getSecurityDir());

         for(int var5 = 0; var5 < var3.length; ++var5) {
            this.moveDomainLevelFile(var3[var5]);
         }

      }
   }

   private void moveAuditLogs(String var1) {
      File var2 = new File(this.upgradeAbsDir.getAbsolutePath() + File.separator + var1);
      this.auditFilter = new DefaultAuditLogFilenameFilter(var2);
      File[] var3 = var2.listFiles(this.auditFilter);
      if (var3 != null && var3.length != 0) {
         new File(DomainDir.getLogsDirForServer(var1));

         for(int var5 = 0; var5 < var3.length; ++var5) {
            File var6 = new File(DomainDir.getPathRelativeServersLogsDir(var1, var3[var5].getName()));
            this.moveFile(var3[var5], var6);
         }

      }
   }

   private void moveLDAPTree(String var1) {
      String var2 = this.upgradeDirPath + File.separator + var1 + File.separator + "ldap";
      File var3 = new File(var2);
      if (var3.exists()) {
         this.moveFile(var3, new File(DomainDir.getLDAPDataDirForServer(var1)));
         String var4 = "conf" + File.separator + "replicas.prop";
         File var5 = new File(DomainDir.getPathRelativeServersLDAPDataDir(var1, var4));
         if (var5.exists()) {
            this.deleteFile(var5);
         }

      }
   }

   private void deleteFile(File var1) {
      if (var1.canWrite()) {
         FileUtils.remove(var1);
      }

      if (!var1.exists()) {
         this.updateStatus("DomainDirectoryFileReorgPlugIn.msg.SecurityFileDeleteSucceeded", var1);
      } else {
         this.updateStatus("DomainDirectoryFileReorgPlugIn.msg.SecurityFileDeleteFailed", var1);
      }

   }

   private void copyFile(File var1, File var2) {
      String[] var3 = new String[2];

      try {
         var3[0] = var1.getCanonicalPath();
         var3[1] = var2.getCanonicalPath();
         FileUtils.copy(var1, var2);
         this.updateStatus("DomainDirectoryFileReorgPlugIn.msg.SecurityFileCopySucceeded", (Object[])var3);
      } catch (IOException var5) {
         this.updateStatus("DomainDirectoryFileReorgPlugIn.msg.IOException", var5.getMessage());
         this.updateStatus("DomainDirectoryFileReorgPlugIn.msg.SecurityFileCopyFailed", (Object[])var3);
      }

   }

   private void moveFile(File var1, File var2) {
      String[] var3 = new String[2];

      try {
         var3[0] = var1.getCanonicalPath();
         var3[1] = var2.getCanonicalPath();
         FileUtils.copy(var1, var2);
         if (var1.canWrite()) {
            FileUtils.remove(var1);
         }

         this.updateStatus("DomainDirectoryFileReorgPlugIn.msg.SecurityFileMoveSucceeded", (Object[])var3);
      } catch (IOException var5) {
         this.updateStatus("DomainDirectoryFileReorgPlugIn.msg.IOException", var5.getMessage());
         this.updateStatus("DomainDirectoryFileReorgPlugIn.msg.SecurityFileMoveFailed", (Object[])var3);
      }

   }

   private static class DefaultLDIFTFilenameFilter implements FilenameFilter {
      String absPath = null;

      public DefaultLDIFTFilenameFilter(File var1) {
         try {
            this.absPath = var1.getAbsolutePath();
         } catch (SecurityException var3) {
         }

      }

      public boolean accept(File var1, String var2) {
         if (var1 != null && this.absPath != null && var2 != null && var2.length() != 0) {
            try {
               if (!this.absPath.equals(var1.getAbsolutePath())) {
                  return false;
               }
            } catch (SecurityException var4) {
               return false;
            }

            return var2.endsWith("Init.ldift");
         } else {
            return false;
         }
      }
   }

   private static class DefaultAuditLogFilenameFilter implements FilenameFilter {
      String absPath = null;

      public DefaultAuditLogFilenameFilter(File var1) {
         try {
            this.absPath = var1.getAbsolutePath();
         } catch (SecurityException var3) {
         }

      }

      public boolean accept(File var1, String var2) {
         if (var1 != null && this.absPath != null && var2 != null && var2.length() != 0) {
            try {
               if (!this.absPath.equals(var1.getAbsolutePath())) {
                  return false;
               }
            } catch (SecurityException var4) {
               return false;
            }

            return var2.startsWith("DefaultAuditRecorder") && var2.endsWith(".log");
         } else {
            return false;
         }
      }
   }
}
