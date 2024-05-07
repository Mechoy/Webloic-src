package weblogic.upgrade.domain.directoryselection;

import com.bea.plateng.plugin.AbstractPlugIn;
import com.bea.plateng.plugin.PlugInContext;
import com.bea.plateng.plugin.PlugInDefinition;
import com.bea.plateng.plugin.PlugInException;
import com.bea.plateng.plugin.ValidationStatus;
import com.bea.plateng.plugin.helper.ProductRegistryHelper;
import com.bea.plateng.plugin.ia.DefaultFileSelectionInputAdapter;
import com.bea.plateng.plugin.ia.InputAdapter;
import com.oracle.cie.common.Version;
import com.oracle.cie.domain.DomainTypeDetector;
import com.oracle.cie.domain.PlatformDomainInfo;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import weblogic.management.DomainDir;
import weblogic.management.ManagementException;
import weblogic.management.bootstrap.BootStrap;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.internal.ServerLocks;
import weblogic.management.provider.internal.DescriptorManagerHelper;
import weblogic.management.provider.internal.DescriptorManagerHelperContext;
import weblogic.management.upgrade.ConfigFileHelper;
import weblogic.management.upgrade.ConfigParser;
import weblogic.management.upgrade.LogHandler;
import weblogic.upgrade.UpgradeHelper;
import weblogic.upgrade.domain.DomainPlugInConstants;
import weblogic.utils.FileUtils;

public class DomainDirectorySelectionPlugIn extends AbstractPlugIn implements LogHandler {
   private static String SERVER_LOCKS_INSTALLED_KEY = DomainDirectorySelectionPlugIn.class.getName() + ".SERVER_LOCKS_INSTALLED_KEY";

   public DomainDirectorySelectionPlugIn(PlugInDefinition var1) throws PlugInException {
      super(var1);
   }

   public void prepare(PlugInContext var1) throws PlugInException {
      super.prepare(var1);
      this.preselectDir();
   }

   private void preselectDir() {
      File var1 = (File)this._context.get(DomainPlugInConstants.DOMAIN_DIRECTORY_KEY);
      if (var1 == null) {
         String var2 = null;
         String var3 = (String)this._context.get(DomainPlugInConstants.DOMAIN_CONFIGURATION_VERSION_KEY);
         if (var3 == null || !var3.startsWith("6.1")) {
            var2 = DomainDir.getRootDir();
         }

         if (var2 == null) {
            var2 = ".";
         }

         var1 = new File(var2);
      }

      DefaultFileSelectionInputAdapter var4 = (DefaultFileSelectionInputAdapter)this._adapter;
      var4.setSelectionMode(1);
      var4.setMultipleSelection(false);
      var4.setSelectedFileNames(new String[]{var1.getPath()});
   }

   public InputAdapter getInputAdapter() {
      this.preselectDir();
      return super.getInputAdapter();
   }

   public ValidationStatus validateInputAdapter(InputAdapter var1) {
      String var2 = (String)this._context.get(DomainPlugInConstants.DOMAIN_CONFIGURATION_VERSION_KEY);
      DefaultFileSelectionInputAdapter var3 = (DefaultFileSelectionInputAdapter)this._adapter;
      ValidationStatus var4 = super.validateInputAdapter(var1);
      File[] var5 = var3.getSelectedFiles();
      File var6 = var5[0];
      if (var4.isValid()) {
         try {
            var6 = var6.getCanonicalFile();
         } catch (Exception var15) {
            var4.setErrorMessage(UpgradeHelper.i18n("DomainDirectorySelectionPlugIn.FileSelectionIA.config.invalid.text"));
            var4.setValid(false);
         }
      }

      if (var4.isValid()) {
         String var7 = BootStrap.getConfigFileName();
         File var8;
         File var9;
         int var11;
         if (var2.startsWith("6.1")) {
            var8 = new File(var6, "config");
            var9 = null;
            File[] var10 = var8.listFiles();
            var11 = 0;
            if (var10 != null && var10.length >= 1) {
               File var12 = null;

               for(int var13 = 0; var13 < var10.length; ++var13) {
                  if (var10[var13].isDirectory() && (var12 = new File(var10[var13], var7)).exists()) {
                     var9 = var12;
                     ++var11;
                  }
               }
            }

            if (var11 == 1 && var9 != null) {
               this._context.put(DomainPlugInConstants.DOMAIN_DIRECTORY_KEY, var6);
               this._context.put(DomainPlugInConstants.DOMAIN_CONFIGURATION_SOURCE_FILE, var9);
            } else {
               var4.setErrorMessage(UpgradeHelper.i18n("DomainDirectorySelectionPlugIn.FileSelectionIA.domain.invalid.61.text"));
               var4.setValid(false);
            }
         } else {
            var8 = new File(var6, var7);
            var9 = new File(var6, "msi-config.xml");
            File var16 = new File(var6, "config/" + var7);
            if (var16.exists()) {
               var11 = 0;

               try {
                  var11 = ConfigFileHelper.getConfigurationVersionFromNewFormat(var16);
               } catch (Exception var14) {
                  var4.setErrorMessage(var14.toString());
               }

               if (var11 == 0) {
                  var4.setValid(false);
               } else {
                  this._context.put(DomainPlugInConstants.DOMAIN_DIRECTORY_KEY, var6);
                  this._context.put(DomainPlugInConstants.DOMAIN_CONFIGURATION_SOURCE_FILE, var16);
                  this.validatePlatformDomain(var4, var6);
               }
            } else if (var8.exists()) {
               this._context.put(DomainPlugInConstants.DOMAIN_DIRECTORY_KEY, var6);
               this._context.put(DomainPlugInConstants.DOMAIN_CONFIGURATION_SOURCE_FILE, var8);
               this.validatePlatformDomain(var4, var6);
            } else if (var9.exists()) {
               this._context.put(DomainPlugInConstants.DOMAIN_DIRECTORY_KEY, var6);
               this._context.put(DomainPlugInConstants.DOMAIN_CONFIGURATION_SOURCE_FILE, var9);
            } else {
               var4.setErrorMessage(UpgradeHelper.i18n("DomainDirectorySelectionPlugIn.FileSelectionIA.config.invalid.text"));
               var4.setValid(false);
            }
         }
      }

      if (var4.isValid()) {
         this._context.put(DomainPlugInConstants.DOMAIN_DIRECTORY_SELECTION_DATE_KEY, new Date());
      }

      return var4;
   }

   public void execute() throws PlugInException {
      try {
         String var1 = (String)this._context.get(DomainPlugInConstants.DOMAIN_CONFIGURATION_VERSION_KEY);
         File var2 = (File)this._context.get(DomainPlugInConstants.DOMAIN_DIRECTORY_KEY);
         UpgradeHelper.log(this, UpgradeHelper.i18n("DomainDirectorySelectionPlugIn.execute.directory_selected", (Object)var2.getAbsolutePath()));
         DomainDir.resetRootDirForExplicitUpgrade(var2.getAbsolutePath());
         UpgradeHelper.addSummaryMessage(this._context, this.getName(), UpgradeHelper.i18n("DomainDirectorySelectionPlugIn.execute.directory_selected", (Object)var2.getAbsolutePath()));
         if (this._context.get(SERVER_LOCKS_INSTALLED_KEY) != null) {
            ServerLocks.releaseAllServerLocks();
         }

         ServerLocks.getAllServerLocks();
         ServerLocks.releaseAllServerLocks();
         this._context.put(SERVER_LOCKS_INSTALLED_KEY, var2);
         File var3 = (File)this._context.get(DomainPlugInConstants.DOMAIN_CONFIGURATION_SOURCE_FILE);
         DomainMBean var4 = null;
         if (var1.startsWith("6.1")) {
            FileUtils.copy(new File(var3.getParentFile(), "SerializedSystemIni.dat"), var2);
            FileUtils.copy(new File(var3.getParentFile(), "fileRealm.properties"), var2);
         }

         if (!var1.startsWith("6.") && !var1.startsWith("7.") && !var1.startsWith("8.")) {
            var4 = this.parseNewStyleConfig(var3);
         } else {
            ConfigParser var5 = new ConfigParser(var3, this);
            var5.parse(true);
            var4 = var5.getRoot();
            if (var1.startsWith("6.1") && var4.getName() == null) {
               var4.setName(var3.getParentFile().getName());
            }
         }

         String var9 = var4.getConfigurationVersion();
         UpgradeHelper.addSummaryMessage(this._context, this.getName(), UpgradeHelper.i18n("DomainDirectorySelectionPlugIn.execute.original_version_message") + var9);
         this._context.put(DomainPlugInConstants.DOMAIN_BEAN_TREE_KEY, var4);
         UpgradeHelper.resetLocalServerNames(this, this._context);
         String var6 = UpgradeHelper.i18n("DomainDirectorySelectionPlugIn.execute.message.text", (Object)var9);
         UpgradeHelper.log(this, var6);
         if (isWindowsPlatform() && var2.getAbsolutePath().length() > 50) {
            UpgradeHelper.addSummaryMessage(this._context, this.getName(), UpgradeHelper.i18n("DomainDirectorySelectionPlugIn.execute.windows_file_length_message"));
         }

      } catch (ManagementException var7) {
         if (var7.getCause() != null && var7.getCause() instanceof VersionException) {
            UpgradeHelper.log(this, var7.getCause().getMessage());
            throw new PlugInException(this.getName(), var7.getCause().getMessage());
         } else {
            throw new PlugInException(this.getName(), "Execute Exception ... " + var7.toString(), var7);
         }
      } catch (Exception var8) {
         throw new PlugInException(this.getName(), "Execute Exception ... " + var8.toString(), var8);
      }
   }

   public void undo() {
      this._context.remove(DomainPlugInConstants.DOMAIN_BEAN_TREE_KEY);
   }

   private static boolean isWindowsPlatform() {
      String var0 = System.getProperty("os.name");
      return var0 != null && var0.startsWith("Windows");
   }

   public void log(String var1) {
      UpgradeHelper.log(this, var1);
   }

   private void validatePlatformDomain(ValidationStatus var1, File var2) {
      PlatformDomainInfo var3 = null;
      ProductRegistryHelper var4 = null;

      try {
         var3 = DomainTypeDetector.inspectDomain(var2);
      } catch (FileNotFoundException var8) {
         System.out.println("" + this.getClass().getName() + ".validatePlatformDomain: " + "Failed to get Platform domain info." + var8.getMessage());
      }

      this._context.put(DomainPlugInConstants.PLATFORM_DOMAIN_INFO_KEY, var3);
      var4 = ProductRegistryHelper.factory();
      if (var4 == null) {
         System.out.println("" + this.getClass().getName() + ".validatePlatformDomain: " + "Failed to get info on installed products from product registry");
      }

      this._context.put(DomainPlugInConstants.PRODUCT_REGISTRY_INFO_KEY, var4);
      if (var4 != null && var3 != null && !var3.isPureWLS()) {
         boolean var5 = Boolean.valueOf(System.getProperty("weblogic.upgrade.domain.directoryselection.DomainDirectorySelectionPlugIn.disable_platform_domain_validation", "true"));
         if (var5) {
            System.out.println("" + this.getClass().getName() + ".validatePlatformDomain: " + "Property set to skip platform domain validation. Skipping...");
         } else {
            String var6 = var3.getDomainVersion();
            if (var6 != null && var6 != "") {
               Version var7 = new Version(var6);
               if (var7.isEarlier(new Version("8.1.2.0"))) {
                  var1.setErrorMessage(UpgradeHelper.i18n("DomainDirectorySelectionPlugIn.FileSelectionIA.config.invalid.version.text"));
                  var1.setValid(false);
                  return;
               }
            }

            if (var3.isLD()) {
               var1.setErrorMessage(UpgradeHelper.i18n("DomainDirectorySelectionPlugIn.FileSelectionIA.ld_unavailable.text"));
               var1.setValid(false);
            } else if (var3.isWLP() && !var4.isWebLogicPortalInstalled()) {
               var1.setErrorMessage(UpgradeHelper.i18n("DomainDirectorySelectionPlugIn.FileSelectionIA.cant_upgrade_wlp_domain.text"));
               var1.setValid(false);
            } else if (var3.isWLI() && !var4.isWebLogicIntegrationInstalled()) {
               var1.setErrorMessage(UpgradeHelper.i18n("DomainDirectorySelectionPlugIn.FileSelectionIA.cant_upgrade_wli_domain.text"));
               var1.setValid(false);
            } else if (var3.isLD() && !var4.isAquaLogicDataServicesPlatformInstalled()) {
               var1.setErrorMessage(UpgradeHelper.i18n("DomainDirectorySelectionPlugIn.FileSelectionIA.cant_upgrade_ld_domain.text"));
               var1.setValid(false);
            }
         }
      }
   }

   private DomainMBean parseNewStyleConfig(File var1) throws Exception {
      ArrayList var2 = new ArrayList();
      FileInputStream var3 = null;

      DomainMBean var6;
      try {
         var3 = new FileInputStream(var1);
         DescriptorManagerHelperContext var4 = new DescriptorManagerHelperContext();
         var4.setEditable(true);
         var4.setValidate(true);
         var4.setTransform(true);
         var4.setErrors(var2);
         DomainMBean var5 = (DomainMBean)DescriptorManagerHelper.loadDescriptor(var3, var4).getRootBean();
         if (var2.size() > 0) {
            Iterator var15 = var2.iterator();

            while(var15.hasNext()) {
               Object var7 = var15.next();
               if (var7 instanceof Exception) {
                  UpgradeHelper.log(this, ((Exception)var7).getMessage());
               } else {
                  UpgradeHelper.log(this, var7.toString());
               }
            }

            throw new PlugInException(this.getName(), "Validation errors occurred in parsing " + var1);
         }

         var6 = var5;
      } finally {
         if (var3 != null) {
            try {
               var3.close();
            } catch (Exception var13) {
            }
         }

      }

      return var6;
   }
}
