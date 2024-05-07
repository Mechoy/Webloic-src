package weblogic.upgrade.domain.directoryrestructure;

import com.bea.plateng.plugin.PlugInContext;
import com.bea.plateng.plugin.PlugInException;
import java.io.File;
import java.io.IOException;
import weblogic.management.bootstrap.BootStrap;
import weblogic.management.configuration.DomainMBean;
import weblogic.upgrade.domain.DomainPlugInConstants;
import weblogic.utils.FileUtils;
import weblogic.utils.io.GlobFilenameFilter;

public class MoveConfigurationFilesPlugin extends BaseFileRestructurePlugin {
   private static final String CONFIG_FILE = "config.xml";
   private static final String CONFIG_FILE_ORIGINAL = "config.xml.original";
   private static final String CONFIG_FILE_BOOTED = "config.xml.booted";
   private static final String CONFIG_FILE_MSI = "msi-config.xml";
   private static final String CONFIG_ARCHIVE_DIRECTORY_NAME = "configArchive";
   private static final String USER_CONFIG_DIRECTORY_NAME = "userConfig";
   private static final String RUNNING_MANAGED_SERVERS_FILE = "running-managed-servers.xml";
   private static final String COMMO_CONFIG_XML_BOOTED = "CommoConfig.xml.booted";
   private File upgradeDir;
   private String upgradeDirPath;
   private DomainMBean domainBean;
   private String[] serverNames;

   public void prepare(PlugInContext var1) throws PlugInException {
      try {
         super.prepare(var1);
      } catch (PlugInException var5) {
         throw var5;
      } catch (Exception var6) {
         throw new PlugInException(this.getName(), "Prepare Exception", var6);
      }

      this.domainBean = (DomainMBean)var1.get(DomainPlugInConstants.DOMAIN_BEAN_TREE_KEY);
      if (this.domainBean == null) {
         throw this.createException("DomainDirectoryFileReorgPlugIn.exc.NoBeanTree");
      } else {
         this.upgradeDir = (File)var1.get(DomainPlugInConstants.DOMAIN_DIRECTORY_KEY);
         if (this.upgradeDir == null) {
            throw this.createException("DomainDirectoryFileReorgPlugIn.exc.NoDomainDir");
         } else {
            String var2 = (String)var1.get(DomainPlugInConstants.DOMAIN_CONFIGURATION_VERSION_KEY);
            if (var2.startsWith("6.1")) {
               this.upgradeDir = new File(this.upgradeDir, "config");
               this.upgradeDir = new File(this.upgradeDir, this.domainBean.getName());
            }

            try {
               this.upgradeDirPath = this.upgradeDir.getCanonicalFile().getCanonicalPath();
            } catch (IOException var4) {
               throw this.createException("DomainDirectoryFileReorgPlugIn.exc.DirectoryProcessingException", var4);
            }

            this.serverNames = (String[])((String[])var1.get(DomainPlugInConstants.SERVER_NAMES_KEY));
            if (this.serverNames == null) {
               throw this.createException("DomainDirectoryFileReorgPlugIn.exc.NoServerNames");
            }
         }
      }
   }

   public void execute() throws PlugInException {
      this.updateStatus("DomainDirectoryFileReorgPlugIn.msg.InExecuteMethod", "ConfigFiles");
      String var1 = BootStrap.getConfigFileName();
      File var2 = new File(this.upgradeDirPath + File.separator + var1);
      if (var2.exists()) {
         this.deleteFile(var2);
      }

      GlobFilenameFilter var3 = new GlobFilenameFilter("config.xml*");
      File[] var4 = this.upgradeDir.listFiles(var3);

      for(int var5 = 0; var5 < var4.length; ++var5) {
         if (var4[var5].getName().equals("config.xml.original") || var4[var5].getName().equals("config.xml.booted")) {
            this.deleteFile(var4[var5]);
         }
      }

      File var10 = new File(this.upgradeDirPath + File.separator + "configArchive");
      if (var10.exists()) {
         this.deleteFile(var10);
      }

      File var6 = new File(this.upgradeDirPath + File.separator + "running-managed-servers.xml");
      if (var6.exists()) {
         this.deleteFile(var6);
      }

      File var7 = new File(this.upgradeDirPath + File.separator + "msi-config.xml");
      if (var7.exists()) {
         this.deleteFile(var7);
      }

      File var8 = new File(this.upgradeDirPath + File.separator + "userConfig");
      if (var8.exists()) {
         this.deleteFile(var8);
      }

      File var9 = new File(this.upgradeDirPath + File.separator + "CommoConfig.xml.booted");
      if (var9.exists()) {
         this.deleteFile(var9);
      }

      this.updateStatus("DomainDirectoryFileReorgPlugIn.msg.OutExecuteMethod", "ConfigFiles");
   }

   private void deleteFile(File var1) {
      FileUtils.remove(var1);
      if (!var1.exists()) {
         this.updateStatus("DomainDirectoryFileReorgPlugIn.msg.ConfigFileDeleteSucceeded", var1);
      } else {
         this.updateStatus("DomainDirectoryFileReorgPlugIn.msg.ConfigFileDeleteFailed", var1);
      }

   }
}
