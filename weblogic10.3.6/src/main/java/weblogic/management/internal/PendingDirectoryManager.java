package weblogic.management.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.DomainDir;
import weblogic.management.ManagementLogger;
import weblogic.management.bootstrap.BootStrap;
import weblogic.utils.ArrayUtils;

public class PendingDirectoryManager {
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugConfigurationEdit");
   private static final String sep;
   private static String pendingDir;
   private static String configDir;
   private static PendingDirectoryManager dirManager;

   private PendingDirectoryManager() {
      String var1 = DomainDir.getRootDir();
      pendingDir = DomainDir.getPendingDir();
      configDir = DomainDir.getConfigDir();
   }

   public static PendingDirectoryManager getInstance() {
      return dirManager;
   }

   public InputStream getFileAsStream(String var1) throws IOException {
      this.validateRelativePath(var1);
      File var2 = this.getPendingFile(var1);
      return var2 != null ? new FileInputStream(var2) : new FileInputStream(configDir + sep + var1);
   }

   public boolean fileExists(String var1) {
      this.validateRelativePath(var1);
      File var2 = this.getPendingFile(var1);
      return var2 != null;
   }

   public OutputStream getFileOutputStream(String var1) throws IOException {
      this.validateRelativePath(var1);
      String var2 = pendingDir + sep + var1;
      File var3 = new File(var2);
      String var4 = var3.getParent();
      if (var4 != null) {
         File var5 = new File(var4);
         var5.mkdirs();
      }

      return new FileOutputStream(var2);
   }

   public InputStream getConfigAsStream() throws IOException {
      return this.getFileAsStream(BootStrap.getDefaultConfigFileName());
   }

   public boolean configExists() {
      return this.fileExists(BootStrap.getDefaultConfigFileName());
   }

   public OutputStream getConfigOutputStream() throws IOException {
      return this.getFileOutputStream(BootStrap.getDefaultConfigFileName());
   }

   public InputStream getSecurityConfigAsStream(String var1) throws IOException {
      this.validateFilename(var1);
      return this.getFileAsStream("security" + sep + var1);
   }

   public boolean securityConfigExists(String var1) {
      this.validateFilename(var1);
      return this.fileExists("security" + sep + var1);
   }

   public OutputStream getSecurityConfigOutputStream(String var1) throws IOException {
      this.validateFilename(var1);
      return this.getFileOutputStream("security" + sep + var1);
   }

   public InputStream getGlobalDescriptorAsStream(String var1) throws IOException {
      this.validateFilename(var1);
      return this.getFileAsStream(var1);
   }

   public boolean globalDescriptorExists(String var1) {
      this.validateFilename(var1);
      return this.fileExists(var1);
   }

   public OutputStream getGlobalDescriptorOutputStream(String var1) throws IOException {
      this.validateFilename(var1);
      return this.getFileOutputStream(var1);
   }

   public InputStream getDeploymentPlanAsStream(String var1, String var2) throws IOException {
      this.validateAppName(var1);
      this.validateFilename(var2);
      return this.getFileAsStream("deployments" + sep + var1 + sep + var2);
   }

   public boolean deploymentPlanExists(String var1, String var2) {
      this.validateAppName(var1);
      this.validateFilename(var2);
      return this.fileExists("deployments" + sep + var1 + sep + var2);
   }

   public OutputStream getDeploymentPlanOutputStream(String var1, String var2) throws IOException {
      this.validateAppName(var1);
      this.validateFilename(var2);
      return this.getFileOutputStream("deployments" + sep + var1 + sep + var2);
   }

   public File getFile(String var1) {
      this.validateRelativePath(var1);
      File var2 = this.getPendingFile(var1);
      return var2;
   }

   public boolean deleteAll() {
      boolean var1 = true;
      File var2 = new File(pendingDir);
      if (!this.remove(var2, true)) {
         var1 = false;
      }

      return var1;
   }

   public boolean deleteFile(String var1) {
      this.validateRelativePath(var1);
      File var2 = this.getPendingFile(var1);
      return var2 == null ? false : var2.delete();
   }

   public boolean deleteConfig() {
      return this.deleteFile(BootStrap.getDefaultConfigFileName());
   }

   public boolean deleteSecurityConfig(String var1) {
      this.validateFilename(var1);
      return this.deleteFile("security" + sep + var1);
   }

   public boolean deleteGlobalDescriptor(String var1) {
      this.validateFilename(var1);
      return this.deleteFile(var1);
   }

   public boolean deleteDeploymentPlan(String var1, String var2) {
      this.validateAppName(var1);
      this.validateFilename(var2);
      return this.deleteFile("deployments" + sep + var1 + sep + var2);
   }

   public File[] getAllFiles() {
      File var1 = new File(pendingDir);
      return this.find(var1);
   }

   public File getConfigFile() {
      return this.getPendingFile(BootStrap.getDefaultConfigFileName());
   }

   public File[] getConfigFiles() {
      File var1 = new File(pendingDir);
      return this.find(var1, false);
   }

   public File[] getSecurityConfigFiles() {
      File var1 = new File(pendingDir + sep + "security");
      return this.find(var1);
   }

   public File[] getDeploymentFiles() {
      File var1 = new File(pendingDir + sep + "deployments");
      return this.find(var1);
   }

   public String removePendingDirectoryFromPath(String var1) {
      String var2 = pendingDir + sep;
      return !var1.startsWith(var2) && !(new File(var1)).getPath().startsWith((new File(var2)).getPath()) ? var1 : DomainDir.getConfigDir() + sep + var1.substring(var2.length(), var1.length());
   }

   private void validateRelativePath(String var1) {
      if (var1 == null) {
         throw new AssertionError("filename should not be null");
      }
   }

   private void validateFilename(String var1) {
      if (var1 == null) {
         throw new AssertionError("filename should not be null");
      }
   }

   private void validateAppName(String var1) {
      if (var1 == null) {
         throw new AssertionError("application name should not be null");
      }
   }

   private File getPendingFile(String var1) {
      String var2 = pendingDir + sep + var1;
      File var3 = new File(var2);
      return var3.exists() ? var3 : null;
   }

   private boolean remove(File var1, boolean var2) {
      boolean var3 = true;
      File[] var4 = var1.listFiles();

      for(int var5 = 0; var4 != null && var5 < var4.length; ++var5) {
         File var6 = var4[var5];
         if (var6.isDirectory() && var2 && !this.remove(var6, var2)) {
            var3 = false;
         }

         if (!var6.delete()) {
            if (!var6.isDirectory()) {
               var3 = false;
               ManagementLogger.logPendingDeleteFileFailed(var6.getAbsolutePath());
            }

            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Failed to delete file " + var6);
            }
         } else if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Deleted file " + var6);
         }
      }

      return var3;
   }

   private File[] find(File var1) {
      return this.find(var1, true);
   }

   private File[] find(File var1, boolean var2) {
      if (!var1.exists()) {
         return new File[0];
      } else if (!var1.isDirectory()) {
         throw new IllegalArgumentException(var1 + " is not a directory.");
      } else {
         ArrayList var3 = new ArrayList();
         File[] var4 = var1.listFiles();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            File var6 = var4[var5];
            if (var6.isDirectory()) {
               if (var2) {
                  ArrayUtils.addAll(var3, this.find(var6));
               }
            } else {
               var3.add(var6);
            }
         }

         return (File[])((File[])var3.toArray(new File[var3.size()]));
      }
   }

   static {
      sep = File.separator;
      dirManager = new PendingDirectoryManager();
   }
}
