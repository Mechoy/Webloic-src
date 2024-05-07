package weblogic.management.internal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.ManagementLogger;
import weblogic.management.bootstrap.BootStrap;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.utils.FileUtils;

public final class Utils {
   private static final String DEFAULT_SERVER = "myserver";
   private static File configLockFile = null;
   private static FileLock configFileLock = null;
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugConfigurationRuntime");

   public static String extractDomain(String var0) {
      int var1 = var0.indexOf(":");
      if (var1 < 1) {
         return null;
      } else {
         String var2 = var0.substring(0, var1);
         return var2.equals("null") ? null : var2;
      }
   }

   public static FileLock getConfigFileLock() {
      return getConfigFileLock(300000);
   }

   public static FileLock getConfigFileLock(int var0) {
      if (configFileLock != null) {
         return configFileLock;
      } else {
         try {
            configLockFile = new File(BootStrap.getConfigLockFileName());
            FileOutputStream var1 = new FileOutputStream(configLockFile);
            configFileLock = null;

            try {
               configFileLock = getFileLock(var1.getChannel(), (long)var0);
            } finally {
               if (configFileLock == null) {
                  configLockFile = null;
               } else {
                  FileUtils.registerLockFile(configLockFile);
               }

            }

            return configFileLock;
         } catch (FileNotFoundException var6) {
            return null;
         }
      }
   }

   public static void releaseConfigFileLock() {
      if (configFileLock != null) {
         try {
            configFileLock.release();
            configFileLock.channel().close();
            configFileLock = null;
            configLockFile.delete();
            FileUtils.unregisterLockFile(configLockFile);
            configLockFile = null;
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("released config lock " + configFileLock);
            }
         } catch (IOException var1) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Exception releasing config lock " + var1, var1);
            }
         }

      }
   }

   public static FileLock getFileLock(File var0) {
      return getFileLock(var0, 300000L);
   }

   public static FileLock getFileLock(File var0, long var1) {
      try {
         FileOutputStream var3 = new FileOutputStream(var0);
         return getFileLock(var3.getChannel(), var1);
      } catch (FileNotFoundException var4) {
         return null;
      }
   }

   public static FileLock getFileLock(File var0, boolean var1) throws IOException {
      if (var1) {
         return getFileLock(var0);
      } else {
         FileOutputStream var2 = new FileOutputStream(var0);
         return getFileLock(var2.getChannel(), 0L);
      }
   }

   public static String findServerName(DomainMBean var0) {
      String var1 = System.getProperty("weblogic.Name");
      if ("".equals(var1)) {
         var1 = null;
      }

      if (var1 == null) {
         ServerMBean[] var2 = var0.getServers();
         if (var2.length == 1) {
            var1 = var2[0].getName();
         } else {
            String var3 = var0.getAdminServerName();
            if (var3 == null || var3.length() == 0) {
               var3 = "myserver";
            }

            for(int var4 = 0; var4 < var2.length; ++var4) {
               if (var2[var4].getName().equals(var3)) {
                  var1 = var3;
               }
            }
         }
      }

      return var1;
   }

   static FileLock getFileLock(FileChannel var0, long var1) {
      long var3 = System.currentTimeMillis();
      long var5 = var3;
      long var7 = var3 + var1;

      FileLock var9;
      for(var9 = null; var9 == null && var3 <= var7; var3 = System.currentTimeMillis()) {
         try {
            var9 = var0.tryLock();
         } catch (Throwable var14) {
         }

         if (var9 != null) {
            break;
         }

         long var10 = var7 - var3;
         if (var10 <= 0L) {
            break;
         }

         if (var3 - var5 >= 10000L) {
            var5 = var3;
            ManagementLogger.logRetryFileLock();
         }

         try {
            Thread.sleep(var10 > 500L ? 500L : var10);
         } catch (InterruptedException var13) {
         }
      }

      return var9;
   }
}
