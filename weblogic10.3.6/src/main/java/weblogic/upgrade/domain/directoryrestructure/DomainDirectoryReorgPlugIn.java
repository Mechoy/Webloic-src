package weblogic.upgrade.domain.directoryrestructure;

import com.bea.plateng.plugin.AbstractPlugIn;
import com.bea.plateng.plugin.PlugInContext;
import com.bea.plateng.plugin.PlugInDefinition;
import com.bea.plateng.plugin.PlugInException;
import com.bea.plateng.plugin.PlugInMessageObservation;
import java.io.File;
import java.io.IOException;
import weblogic.management.DomainDir;
import weblogic.management.configuration.DomainMBean;
import weblogic.upgrade.UpgradeHelper;
import weblogic.upgrade.domain.DomainPlugInConstants;

public class DomainDirectoryReorgPlugIn extends AbstractPlugIn {
   private File upgradeDir;
   private File upgradeAbsDir;
   private File parentDir;
   private String upgradeDirPath;
   private DomainMBean domainBean;
   private String[] serverNames;

   public DomainDirectoryReorgPlugIn(PlugInDefinition var1) throws PlugInException {
      super(var1);
   }

   public void prepare(PlugInContext var1) throws PlugInException {
      super.prepare(var1);
      this.domainBean = (DomainMBean)var1.get(DomainPlugInConstants.DOMAIN_BEAN_TREE_KEY);
      if (this.domainBean == null) {
         throw this.createException("DomainDirectoryReorgPlugIn.exc.NoBeanTree");
      } else {
         this.upgradeDir = (File)var1.get(DomainPlugInConstants.DOMAIN_DIRECTORY_KEY);
         if (this.upgradeDir == null) {
            throw this.createException("DomainDirectoryReorgPlugIn.exc.NoDomainDir");
         } else {
            try {
               this.upgradeAbsDir = this.upgradeDir.getCanonicalFile();
               this.parentDir = this.upgradeAbsDir.getParentFile();
               this.upgradeDirPath = this.upgradeDir.getCanonicalPath();
            } catch (IOException var3) {
               throw this.createException("DomainDirectoryReorgPlugIn.exc.DirectoryProcessingException", (Throwable)var3);
            }

            this.serverNames = (String[])((String[])var1.get(DomainPlugInConstants.SERVER_NAMES_KEY));
            if (this.serverNames == null) {
               throw this.createException("DomainDirectoryReorgPlugIn.exc.NoServerNames");
            }
         }
      }
   }

   public void execute() throws PlugInException {
      this.updateStatus("DomainDirectoryReorgPlugIn.msg.InExecuteMethod");
      this.createDomainConfigDirs();
      this.createServerConfigDirs();
      this.updateStatus("DomainDirectoryReorgPlugIn.msg.OutExecuteMethod");
   }

   private void createDomainConfigDirs() {
      this.updateStatus("DomainDirectoryReorgPlugIn.msg.BeginCreateDomainDirectories", this.domainBean.getName());
      this.createDirectory(DomainDir.getBinDir());
      this.createDirectory(DomainDir.getConfigDir());
      this.createDirectory(DomainDir.getPathRelativeRootDir("console-ext"));
      this.createDirectory(DomainDir.getLibDir());
      this.createDirectory(DomainDir.getPendingDir());
      this.createDirectory(DomainDir.getSecurityDir());
      this.createDirectory(DomainDir.getServersDir());
      this.createDirectory(DomainDir.getDeploymentsDir());
      this.createDirectory(DomainDir.getDiagnosticsDir());
      this.createDirectory(DomainDir.getJDBCDir());
      this.createDirectory(DomainDir.getJMSDir());
      this.createDirectory(DomainDir.getConfigSecurityDir());
      this.createDirectory(DomainDir.getConfigStartupDir());
      this.createDirectory(DomainDir.getLibModulesDir());
      this.updateStatus("DomainDirectoryReorgPlugIn.msg.EndCreateDomainDirectories", this.domainBean.getName());
   }

   private void createServerConfigDirs() {
      for(int var2 = 0; var2 < this.serverNames.length; ++var2) {
         String var1 = this.serverNames[var2];
         this.updateStatus("DomainDirectoryReorgPlugIn.msg.BeginCreateServerDirectories", var1);
         this.createDirectory(DomainDir.getDirForServer(var1));
         this.createDirectory(DomainDir.getBinDirForServer(var1));
         this.createDirectory(DomainDir.getCacheDirForServer(var1));
         this.createDirectory(DomainDir.getDataDirForServer(var1));
         this.createDirectory(DomainDir.getLogsDirForServer(var1));
         this.createDirectory(DomainDir.getSecurityDirForServer(var1));
         this.createDirectory(DomainDir.getTempDirForServer(var1));
         this.createDirectory(DomainDir.getLDAPDataDirForServer(var1));
         this.createDirectory(DomainDir.getStoreDataDirForServer(var1));
         this.updateStatus("DomainDirectoryReorgPlugIn.msg.EndCreateServerDirectories", var1);
      }

   }

   private void createDirectory(String var1) {
      File var2 = new File(var1);
      String var3 = var2.getAbsolutePath();
      if (!var2.exists()) {
         var2.mkdirs();
         this.updateStatus("DomainDirectoryReorgPlugIn.msg.CreatedDirectory", var3);
      } else {
         this.updateStatus("DomainDirectoryReorgPlugIn.msg.DirectoryAlreadyExists", var3);
      }

   }

   private void updateStatus(String var1) {
      this.log(UpgradeHelper.i18n(var1));
   }

   private void updateStatus(String var1, Object var2) {
      this.log(UpgradeHelper.i18n(var1, var2));
   }

   private PlugInException createException(String var1) {
      String var2 = UpgradeHelper.i18n(var1);
      this.log(var2);
      return new PlugInException(this.getName(), var2);
   }

   private PlugInException createException(String var1, Object var2) {
      String var3 = UpgradeHelper.i18n(var1, var2);
      this.log(var3);
      return new PlugInException(this.getName(), var3);
   }

   private PlugInException createException(String var1, Throwable var2) {
      StringBuffer var3 = new StringBuffer();

      for(Throwable var4 = var2; var4 != null; var4 = var4.getCause()) {
         var3.append(": ");
         var3.append(var4.toString());
      }

      String var5 = UpgradeHelper.i18n(var1, (Object)var3.toString());
      this.log(var5);
      return new PlugInException(this.getName(), var5);
   }

   private void log(String var1) {
      this.updateObservers(new PlugInMessageObservation(this.getName(), var1 + ""));
   }
}
