package weblogic.upgrade.domain.directoryrestructure;

import com.bea.plateng.plugin.PlugInContext;
import com.bea.plateng.plugin.PlugInException;
import java.io.File;
import java.io.IOException;
import weblogic.management.configuration.DomainMBean;
import weblogic.upgrade.domain.DomainPlugInConstants;

public class DirectoryRestructureCleanupPlugin extends BaseFileRestructurePlugin {
   private File upgradeDir;
   private File upgradeAbsDir;
   private String upgradeDirPath;
   private File parentDir;
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
               this.parentDir = this.upgradeAbsDir.getParentFile();
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
      this.updateStatus("DomainDirectoryFileReorgPlugIn.msg.InExecuteMethod", "DirectoryRestructureCleanup");

      for(int var1 = 0; var1 < this.serverNames.length; ++var1) {
         String var2 = this.serverNames[var1];
         this.domainBean.lookupServer(var2);
      }

      this.updateStatus("DomainDirectoryFileReorgPlugIn.msg.OutExecuteMethod", "DirectoryRestructureCleanup");
   }
}
