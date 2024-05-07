package weblogic.upgrade.domain.directoryrestructure;

import com.bea.plateng.plugin.PlugInContext;
import com.bea.plateng.plugin.PlugInException;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import weblogic.management.DomainDir;
import weblogic.management.configuration.DomainMBean;
import weblogic.upgrade.domain.DomainPlugInConstants;
import weblogic.utils.FileUtils;

public class MoveBinFilesPlugin extends BaseFileRestructurePlugin {
   private File upgradeDir;
   private File upgradeAbsDir;
   private String upgradeDirPath;
   private DomainMBean domainBean;
   private String[] serverNames;

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
      this.updateStatus("DomainDirectoryFileReorgPlugIn.msg.InExecuteMethod", "BinFiles");
      FileFilter var3 = FileUtils.makeExtensionFilter("cmd");
      FileFilter var4 = FileUtils.makeExtensionFilter("sh");
      File[] var5 = FileUtils.find(this.upgradeDir, var3);

      for(int var6 = 0; var6 < var5.length; ++var6) {
         File var1 = new File(DomainDir.getPathRelativeBinDir(var5[var6].getName()));
         this.moveFile(var5[var6], var1);
      }

      File[] var8 = FileUtils.find(this.upgradeDir, var4);

      for(int var7 = 0; var7 < var8.length; ++var7) {
         File var2 = new File(DomainDir.getPathRelativeBinDir(var8[var7].getName()));
         this.moveFile(var8[var7], var2);
      }

      this.updateStatus("DomainDirectoryFileReorgPlugIn.msg.OutExecuteMethod", "BinFiles");
   }

   private void moveFile(File var1, File var2) {
      try {
         FileUtils.copy(var1, var2);
         this.updateStatus("DomainDirectoryFileReorgPlugIn.msg.BinFileMoveSucceeded", var2);
      } catch (IOException var4) {
         this.updateStatus("DomainDirectoryFileReorgPlugIn.msg.IOException", var4.getMessage());
         this.updateStatus("DomainDirectoryFileReorgPlugIn.msg.BinFileMoveFailed", var1);
      }

   }
}
