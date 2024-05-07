package weblogic.management.provider.internal;

import java.io.File;
import java.io.IOException;
import java.security.AccessController;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.kernel.Kernel;
import weblogic.management.DomainDir;
import weblogic.management.ManagementLogger;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.jars.JarFileUtils;

public class ConfigBackup {
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugConfigurationEdit");
   private static final File configDir = new File(DomainDir.getConfigDir());
   private static final File archiveDir = new File(DomainDir.getRootDir(), "configArchive");
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final String CONFIG_BASE = "config-";

   static void saveBooted() throws IOException {
      File var0 = new File(DomainDir.getRootDir(), "config-booted.jar");
      createJarFileFromConfig(var0);
   }

   static void saveOriginal() throws IOException {
      File var0 = new File(DomainDir.getRootDir(), "config-original.jar");
      createJarFileFromConfig(var0);
   }

   static void saveVersioned() throws IOException {
      int var0 = 3;
      if (Kernel.isServer()) {
         DomainMBean var1 = ManagementService.getRuntimeAccess(kernelId).getDomain();
         if (var1 == null) {
            return;
         }

         var0 = var1.getArchiveConfigurationCount();
      }

      if (var0 > 0) {
         if (!archiveDir.exists()) {
            archiveDir.mkdir();
         }

         if (!archiveDir.isDirectory()) {
            throw new IOException(archiveDir.getCanonicalPath() + " is not a directory");
         } else {
            File[] var9 = archiveDir.listFiles();
            if (var9 != null && var9.length != 0) {
               int var10 = -1;
               int var3 = -1;
               int var4 = 0;

               int var5;
               for(var5 = 0; var5 < var9.length; ++var5) {
                  if (debugLogger.isDebugEnabled()) {
                     debugLogger.debug("saveVersioned: existing archive file = " + var9[var5]);
                  }

                  int var6 = getVersion(var9[var5]);
                  if (var6 >= 0) {
                     ++var4;
                     if (var10 == -1 || var10 > var6) {
                        var10 = var6;
                     }

                     if (var3 == -1 || var3 < var6) {
                        var3 = var6;
                     }
                  }
               }

               if (var4 > 0) {
                  var5 = var3 + 1;
                  File var11;
                  if (var4 >= var0) {
                     var11 = getVersionedArchiveName(var10);
                     if (debugLogger.isDebugEnabled()) {
                        debugLogger.debug("saveVersioned: delete existing archive file = " + var11);
                     }

                     boolean var7 = !var11.delete();
                     if (var7) {
                        String var8 = "Unable to delete old archive " + var11.getCanonicalPath();
                        throw new IOException(var8);
                     }
                  }

                  var11 = getVersionedArchiveName(var5);
                  createJarFileFromConfig(var11);
               }
            } else {
               File var2 = getVersionedArchiveName(1);
               createJarFileFromConfig(var2);
            }

         }
      }
   }

   private static int getVersion(File var0) {
      if (var0 == null) {
         return -1;
      } else {
         String var1 = var0.getName();
         if (var1.startsWith("config-") && var1.endsWith(".jar")) {
            int var2 = var1.indexOf("config-") + "config-".length();
            String var3 = var1.substring(var2);
            int var4 = var3.indexOf(".jar");
            var3 = var3.substring(0, var4);

            try {
               return Integer.parseInt(var3);
            } catch (NumberFormatException var6) {
               return -1;
            }
         } else {
            return -1;
         }
      }
   }

   private static File getVersionedArchiveName(int var0) {
      return new File(archiveDir, "config-" + var0 + ".jar");
   }

   private static void createJarFileFromConfig(File var0) throws IOException {
      createJarFileFromConfig(var0.getPath());
   }

   private static void createJarFileFromConfig(String var0) throws IOException {
      String var1 = (new File(var0)).getAbsolutePath();
      if (!configDir.isDirectory()) {
         IOException var2 = new IOException(configDir + " is not a directory");
         ManagementLogger.logCouldNotBackupConfiguration(var1, var2);
         throw var2;
      } else {
         ManagementLogger.logBackingUpConfiguration(var1);

         try {
            JarFileUtils.createJarFileFromDirectory(var0, configDir);
         } catch (IOException var3) {
            ManagementLogger.logCouldNotBackupConfiguration(var1, var3);
            throw var3;
         }
      }
   }

   public static void main(String[] var0) throws Exception {
      saveBooted();
      saveOriginal();
      saveVersioned();
   }
}
