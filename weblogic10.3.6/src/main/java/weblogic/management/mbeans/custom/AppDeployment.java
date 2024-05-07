package weblogic.management.mbeans.custom;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.AccessController;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.xml.stream.XMLStreamException;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.deploy.internal.DeploymentPlanDescriptorLoader;
import weblogic.deploy.internal.TargetHelper;
import weblogic.deploy.internal.targetserver.DeployHelper;
import weblogic.deploy.internal.targetserver.StagingDirectory;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.ModuleDescriptorBean;
import weblogic.j2ee.descriptor.wl.ModuleOverrideBean;
import weblogic.logging.Loggable;
import weblogic.management.DomainDir;
import weblogic.management.ManagementException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.ApplicationMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.deploy.ApplicationsDirPoller;
import weblogic.management.deploy.internal.DeployerRuntimeLogger;
import weblogic.management.deploy.internal.SlaveDeployerLogger;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.provider.custom.ConfigurationMBeanCustomizer;
import weblogic.management.security.RealmMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.FileUtils;

public class AppDeployment extends ConfigurationMBeanCustomizer {
   private static final boolean debug = false;
   private static final String APP_DIR = "app";
   private static final String DEPLOYMENTS_DIR = "config/deployments";
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private transient String appName = null;
   private transient String versionId = null;
   private transient StagingDirectory sd = null;
   private transient DeploymentPlanBean plan = null;
   private String installDir = null;
   private String planPath = null;
   private String planDir = null;
   private String sourcePath = null;
   private String configPath = null;
   private String stagingMode = null;

   public AppDeployment(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   private String getDeploymentsDir() {
      if (this.configPath == null) {
         this.configPath = this.resolveToBase("config/deployments", (String)null);
      }

      return this.configPath;
   }

   private String resolveToBase(String var1, String var2) {
      var1 = FileUtils.normalize(var1);
      var2 = FileUtils.normalize(var2);
      String var3 = var1;
      if (var1 != null) {
         File var4 = new File(var1);
         if (!FileUtils.isAbsolute(var4)) {
            if (var2 == null) {
               var3 = (new File(DomainDir.getRootDir(), var1)).getAbsolutePath();
            } else {
               var3 = (new File(var2, var1)).getAbsolutePath();
            }
         }
      }

      return var3;
   }

   public String getInstalldir() {
      return this.installDir;
   }

   public void setInstalldir(String var1) {
      String var2 = var1;
      if (verifyIfPathIsInRootDir(var1)) {
         var2 = makePathRelativeToRootDir(var1);
      }

      this.installDir = var2;
   }

   public String getPlanPath() {
      return this.planPath;
   }

   public void setPlanPath(String var1) {
      String var2 = var1;
      String var3 = this.getAbsolutePlanDir();
      File var4;
      if (var3 != null) {
         var4 = new File(var3);
         if (verifyIfPathIsIn(var1, var4)) {
            var2 = makePathRelativeTo(var1, var4);
         }
      } else {
         var4 = new File(DomainDir.getRootDir());
         if (verifyIfPathIsIn(var1, var4)) {
            var2 = makePathRelativeTo(var1, var4);
         }
      }

      this.planPath = var2;
   }

   public String getPlanDir() {
      return this.planDir;
   }

   public void setPlanDir(String var1) {
      String var2 = var1;
      String var3 = this.getAbsoluteInstallDir();
      if (var1 != null) {
         File var4;
         if (var3 != null && (new File(var3)).exists()) {
            var4 = new File(var3);
            if (verifyIfPathIsIn(var1, var4)) {
               var2 = makePathRelativeTo(var1, var4);
            }
         } else {
            var4 = new File(DomainDir.getRootDir());
            if (verifyIfPathIsIn(var1, var4)) {
               var2 = makePathRelativeTo(var1, var4);
            }
         }
      }

      this.planDir = var2;
      if (this.planPath != null && this.planDir != null && FileUtils.isAbsolute(new File(this.planPath))) {
         this.setPlanPath(this.planPath);
      }

   }

   public String getSourcePath() {
      return this.sourcePath;
   }

   public void setSourcePath(String var1) {
      String var2 = var1;
      String var3 = this.getAbsoluteAppDir();
      if (var3 != null && (new File(var3)).exists()) {
         File var4 = new File(var3);
         if (verifyIfPathIsIn(var1, var4)) {
            var2 = makePathRelativeTo(var1, var4);
         }
      } else if (verifyIfPathIsInRootDir(var1)) {
         var2 = makePathRelativeToRootDir(var1);
      }

      this.sourcePath = var2;
   }

   public String getAbsoluteInstallDir() {
      return this.resolveToBase(this.installDir, (String)null);
   }

   public String getAbsolutePlanPath() {
      return this.resolveToBase(this.planPath, this.getAbsolutePlanDir());
   }

   public String getAbsolutePlanDir() {
      return this.resolveToBase(this.planDir, this.getAbsoluteInstallDir());
   }

   private String getAbsoluteAppDir() {
      String var1 = this.getAbsoluteInstallDir();
      return var1 != null ? this.resolveToBase("app", var1) : null;
   }

   public String getAbsoluteSourcePath() {
      return this.resolveToBase(this.sourcePath, this.getAbsoluteAppDir());
   }

   public String getApplicationIdentifier() {
      return this.getMbean().getName();
   }

   public String getApplicationName() {
      if (this.appName == null) {
         this.appName = ApplicationVersionUtils.getApplicationName(this.getApplicationIdentifier());
      }

      return this.appName;
   }

   public String getVersionIdentifier() {
      if (this.versionId == null) {
         this.versionId = ApplicationVersionUtils.getVersionId(this.getApplicationIdentifier());
      }

      return this.versionId;
   }

   public void setStagingMode(String var1) throws ManagementException {
      if (this.stagingMode != null && !this.stagingMode.equals(var1)) {
         Loggable var2 = DeployerRuntimeLogger.logRejectStagingChangeLoggable(this.getApplicationName());
         var2.log();
         throw new ManagementException(var2.getMessage());
      } else {
         this.stagingMode = var1;
      }
   }

   public String getStagingMode() {
      return this.stagingMode;
   }

   public boolean isAutoDeployedApp() {
      return ApplicationsDirPoller.isInAppsDir(new File(DomainDir.getAppPollerDir()), this.getAbsoluteSourcePath());
   }

   public ApplicationMBean getAppMBean() {
      DomainMBean var1 = (DomainMBean)this.getMbean().getParent();
      if (var1 == null) {
         return null;
      } else {
         String var2 = this.getMbean().getName();
         ApplicationMBean var3 = var1.lookupApplication(var2);
         return var3;
      }
   }

   public static String getInitialSecurityDDModel(AppDeploymentMBean var0) {
      String var1 = "DDOnly";
      DomainMBean var2 = (DomainMBean)var0.getParent();
      if (var2 != null) {
         RealmMBean var3 = var2.getSecurityConfiguration().getDefaultRealm();
         if (var3 != null) {
            var1 = var3.getSecurityDDModel();
         }
      }

      return var1;
   }

   private StagingDirectory getStagingDirectory() throws IOException {
      if (this.sd == null) {
         this.sd = new StagingDirectory(this.getAbsolutePlanPath(), (new File(this.getAbsoluteSourcePath())).getName(), this.getRootStagingDir());
      }

      return this.sd;
   }

   public String getLocalInstallDir() {
      this.assertOnServer();
      String var1 = this.getAbsoluteInstallDir();
      if (this.isStagedOnThisServer()) {
         try {
            var1 = this.getStagingDirectory().getRoot();
         } catch (IOException var3) {
         }
      }

      return var1;
   }

   public String getLocalPlanPath() {
      this.assertOnServer();
      String var1 = this.getAbsolutePlanPath();
      if (var1 != null && this.isStagedOnThisServer()) {
         try {
            var1 = this.getStagingDirectory().getPlan();
         } catch (IOException var3) {
         }
      }

      return var1;
   }

   public String getLocalPlanDir() throws IOException {
      this.assertOnServer();
      String var1 = this.getAbsolutePlanDir();
      if (var1 != null && this.isStagedOnThisServer()) {
         var1 = this.getStagingDirectory().getPlanDir();
      }

      return var1;
   }

   public String getLocalSourcePath() {
      this.assertOnServer();
      String var1 = this.getAbsoluteSourcePath();
      if (var1 != null && this.isStagedOnThisServer()) {
         try {
            var1 = this.getStagingDirectory().getSource();
         } catch (IOException var3) {
            throw new IllegalArgumentException(var3.getMessage(), var3);
         }
      }

      return var1;
   }

   private void assertOnServer() {
      if (ManagementService.getPropertyService(kernelId) == null) {
         throw new IllegalStateException();
      }
   }

   private boolean isStaged() {
      String var1 = this.getStagingMode(ManagementService.getPropertyService(kernelId).getServerName());
      return !"nostage".equals(var1);
   }

   private static String makePathRelativeToRootDir(String var0) {
      return makePathRelativeTo(var0, new File(DomainDir.getRootDir()));
   }

   private static String makePathRelativeTo(String var0, File var1) {
      if (!var1.exists()) {
         return var0;
      } else if (var0 == null) {
         return var0;
      } else {
         try {
            String var2 = var1.getCanonicalPath();
            String var3 = (new File(var0)).getCanonicalPath();
            if (!var3.startsWith(var2)) {
               return var0;
            } else if (var2.length() == var3.length()) {
               return ".";
            } else {
               String var4 = var3.substring(var2.length() + 1);
               File var5 = new File(var1, var4);
               return !var5.exists() ? var0 : var4;
            }
         } catch (IOException var6) {
            return var0;
         }
      }
   }

   private static boolean verifyIfPathIsInRootDir(String var0) {
      return verifyIfPathIsIn(var0, new File(DomainDir.getRootDir()));
   }

   private static boolean verifyIfPathIsIn(String var0, File var1) {
      if (!var1.exists()) {
         return false;
      } else if (var0 == null) {
         return false;
      } else {
         File var2 = new File(var0);
         if (!FileUtils.isAbsolute(var2)) {
            return false;
         } else {
            try {
               File var3 = var1.getCanonicalFile();

               File var5;
               for(File var4 = var2; (var5 = var4.getParentFile()) != null; var4 = var4.getParentFile()) {
                  var5 = var5.getCanonicalFile();
                  if (var5.equals(var3)) {
                     return true;
                  }
               }

               return false;
            } catch (IOException var6) {
               return false;
            }
         }
      }
   }

   public String getRootStagingDir() {
      this.assertOnServer();
      String var1 = ManagementService.getRuntimeAccess(kernelId).getServer().getStagingDirectoryName();
      File var2 = new File(var1, this.getApplicationName());
      if (this.getVersionIdentifier() != null) {
         var2 = new File(var2, this.getVersionIdentifier());
      }

      return var2.getPath();
   }

   public String getStagingMode(String var1) {
      this.assertOnServer();
      String var2 = this.getStagingMode();
      if (var2 == null || var2.length() == 0) {
         var2 = DeployHelper.getServerStagingMode(var1);
      }

      return var2;
   }

   public DeploymentPlanBean getDeploymentPlanDescriptor() {
      if (this.plan != null) {
         return this.plan;
      } else {
         String var1 = this.getLocalPlanPath();
         if (var1 != null) {
            File var2 = new File(var1);

            try {
               DeploymentPlanDescriptorLoader var3 = new DeploymentPlanDescriptorLoader(var2);
               this.plan = var3.getDeploymentPlanBean();
            } catch (XMLStreamException var5) {
               throw new IllegalArgumentException(var5.getMessage(), var5);
            } catch (IOException var6) {
               throw new IllegalArgumentException(var6.getMessage(), var6);
            } catch (ClassCastException var7) {
               Loggable var4 = SlaveDeployerLogger.logUnknownPlanLoggable(var1, this.getName());
               throw new IllegalArgumentException(var4.getMessage(), var7);
            }
         }

         return this.plan;
      }
   }

   public void setDeploymentPlanDescriptor(DeploymentPlanBean var1) {
      this.plan = var1;
   }

   public byte[] getDeploymentPlan() {
      String var1 = this.getLocalPlanPath();
      if (var1 != null) {
         File var2 = new File(var1);
         if (var2.exists()) {
            try {
               FileInputStream var3 = new FileInputStream(var2);
               int var4 = var3.available();
               byte[] var5 = new byte[var4];
               var3.read(var5);
               var3.close();
               return var5;
            } catch (IOException var6) {
               throw new IllegalArgumentException(var6.getMessage(), var6);
            }
         }
      }

      return null;
   }

   public byte[] getDeploymentPlanExternalDescriptors() {
      DeploymentPlanBean var1 = this.getDeploymentPlanDescriptor();
      if (var1 == null) {
         return null;
      } else {
         try {
            boolean var2 = false;
            ByteArrayOutputStream var3 = new ByteArrayOutputStream();
            ZipOutputStream var4 = new ZipOutputStream(var3);
            String var5 = var1.getConfigRoot();
            ModuleOverrideBean[] var6 = var1.getModuleOverrides();
            ModuleOverrideBean[] var7 = var6;
            int var8 = var6.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               ModuleOverrideBean var10 = var7[var9];
               ModuleDescriptorBean[] var11 = var10.getModuleDescriptors();
               ModuleDescriptorBean[] var12 = var11;
               int var13 = var11.length;

               for(int var14 = 0; var14 < var13; ++var14) {
                  ModuleDescriptorBean var15 = var12[var14];
                  if (var15.isExternal()) {
                     File var16;
                     String var17;
                     if (var1.rootModule(var10.getModuleName())) {
                        var16 = new File(var5);
                        var17 = "";
                     } else {
                        var16 = new File(var5, var10.getModuleName());
                        var17 = var10.getModuleName() + "/";
                     }

                     File var18 = new File(var16, var15.getUri());
                     if (var18.exists()) {
                        var2 = true;
                        var17 = var17 + var15.getUri();
                        var4.putNextEntry(new ZipEntry(var17));
                        FileInputStream var19 = new FileInputStream(new File(var16, var15.getUri()));
                        int var20 = var19.available();
                        byte[] var21 = new byte[var20];
                        var19.read(var21);
                        var19.close();
                        var4.write(var21);
                        var4.closeEntry();
                     }
                  }
               }
            }

            if (!var2) {
               return null;
            } else {
               var4.close();
               return var3.toByteArray();
            }
         } catch (IOException var22) {
            throw new IllegalArgumentException(var22.getMessage(), var22);
         }
      }
   }

   private boolean isStagedOnThisServer() {
      if (!this.isStaged()) {
         return false;
      } else {
         AppDeploymentMBean var1 = (AppDeploymentMBean)this.getMbean();
         return TargetHelper.isTargetedLocaly(var1) || TargetHelper.isPinnedToServerInCluster(var1);
      }
   }
}
