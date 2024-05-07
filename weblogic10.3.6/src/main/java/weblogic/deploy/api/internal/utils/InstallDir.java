package weblogic.deploy.api.internal.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import weblogic.deploy.api.internal.SPIDeployerLogger;
import weblogic.management.DomainDir;

public class InstallDir {
   private static final boolean debug = Debug.isDebug("utils");
   public static final String CONFIG_DIR = "plan";
   public static final String APP_DIR = "app";
   private static final String DEFAULT_INSTALL_DIR = DomainDir.getDeploymentsDirNonCanonical();
   private File installDir;
   private String appName;
   private File configDir;
   private File appDir;
   private File plan;
   private File app;

   public InstallDir(String var1, String var2) throws IOException {
      this(var1, var2 == null ? null : new File(var2));
   }

   public InstallDir(String var1, File var2) throws IOException {
      this(var1, var2, true);
   }

   public InstallDir(String var1, File var2, boolean var3) throws IOException {
      this.configDir = null;
      this.appDir = null;
      this.plan = null;
      this.app = null;
      ConfigHelper.checkParam("appName", var1);
      this.appName = var1;
      this.installDir = var2;
      boolean var4 = true;
      if (this.installDir == null) {
         this.installDir = this.create();
         if (!var3) {
            var4 = false;
         }
      }

      if (var4) {
         if (!this.installDir.exists()) {
            this.installDir.mkdirs();
         }

         if (!this.installDir.exists()) {
            this.installDir.mkdirs();
         }

         if (!this.installDir.exists() || this.installDir.isFile()) {
            throw new IOException(SPIDeployerLogger.invalidInstallDir(this.installDir.getPath()));
         }
      }

      this.installDir = this.installDir.getAbsoluteFile();
      if (debug) {
         Debug.say("Install dir defined at " + this.installDir.getPath());
      }

   }

   public InstallDir(File var1) throws IOException {
      this(var1.getName(), var1);
   }

   public File getDDFile(String var1, String var2) {
      return new File(new File(this.getConfigDir(), var1), var2);
   }

   public File getAppDDFile(String var1) {
      return new File((new File(this.getConfigDir(), var1)).getPath());
   }

   public File getInstallDir() {
      return this.installDir;
   }

   public void resetInstallDir(File var1) {
      this.installDir = var1;
      this.configDir = null;
      this.appDir = null;
      this.app = null;
      this.plan = null;
   }

   public File getConfigDir() {
      if (this.configDir == null) {
         this.configDir = new File(this.installDir, "plan");
      }

      return this.configDir;
   }

   public void setConfigDir(File var1) {
      this.configDir = var1;
   }

   public File getAppDir() {
      if (this.appDir == null) {
         this.appDir = new File(this.installDir, "app");
      }

      return this.appDir;
   }

   public void setAppDir(File var1) {
      this.appDir = var1;
   }

   public File getArchive() {
      if (this.app == null) {
         this.app = new File(this.getAppDir(), this.appName);
      }

      return this.app;
   }

   public void setArchive(File var1) {
      this.app = var1;
   }

   public File getPlan() {
      return this.plan;
   }

   public void setPlan(File var1) {
      this.plan = var1;
   }

   public boolean isInAppDir(File var1) {
      if (var1 == null) {
         return true;
      } else {
         File var2 = new File(var1.getAbsolutePath());
         return var2.getParentFile().equals(this.getAppDir().getAbsoluteFile());
      }
   }

   public boolean isInConfigDir(File var1) {
      if (var1 == null) {
         return true;
      } else {
         File var2 = null;
         var2 = new File(var1.getAbsolutePath());
         return var2.getParentFile().equals(this.getConfigDir().getAbsoluteFile());
      }
   }

   public boolean isInInstallDir(File var1) {
      if (var1 == null) {
         return true;
      } else {
         File var2 = null;
         var2 = new File(var1.getAbsolutePath());
         return var2.getParentFile().equals(this.getInstallDir());
      }
   }

   public FileOutputStream getOutputStream(File var1) throws IOException {
      if (!var1.getParentFile().exists()) {
         var1.getParentFile().mkdirs();
      }

      return new FileOutputStream(var1);
   }

   private File create() {
      File var1 = new File(DEFAULT_INSTALL_DIR);
      String var2 = System.getProperty("user.name");
      if (!var1.exists() && !var1.isAbsolute()) {
         new File(var1, var2);
         String var3 = System.getProperty("java.io.tmpdir");
         if (var3 == null) {
            var3 = "/tmp";
         }

         var1 = new File(var3, var2);
         var1 = new File(var1, DEFAULT_INSTALL_DIR);
      }

      return new File(var1, this.appName);
   }

   public boolean isProper() {
      return this.isInInstallDir(this.getAppDir()) && this.isInInstallDir(this.getConfigDir()) && this.isInAppDir(this.getArchive()) && this.isInConfigDir(this.getPlan());
   }
}
