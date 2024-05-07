package weblogic.management.internal;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.DomainDir;
import weblogic.management.ManagementException;
import weblogic.management.bootstrap.BootStrap;
import weblogic.utils.FileUtils;

public class ServerLocks {
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugConfigurationRuntime");
   private static FileLock[] serverLocks;
   private static FileLock serverLock;
   private static File[] serverLockFiles;
   private static File serverLockFile;

   public static void main(String[] var0) throws Exception {
      getAllServerLocks();
      releaseAllServerLocks();
   }

   public static void getServerLock() throws ManagementException {
      String var0 = BootStrap.getServerName();
      if (var0 == null) {
         String[] var1 = getServers();
         if (var1.length == 1) {
            var0 = var1[0];
         }
      }

      if (var0 != null) {
         serverLockFile = getServerLockFile(var0);
      } else {
         serverLockFile = getDefaultServerLockFile();
      }

      try {
         serverLock = getServerLock(serverLockFile);
      } finally {
         if (serverLock == null) {
            serverLockFile = null;
         } else {
            FileUtils.registerLockFile(serverLockFile);
         }

      }

   }

   public static synchronized void releaseServerLock() {
      if (serverLock != null) {
         try {
            serverLock.release();
            serverLock.channel().close();
            serverLock = null;
            serverLockFile.delete();
            FileUtils.unregisterLockFile(serverLockFile);
            serverLockFile = null;
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("released " + serverLock);
            }
         } catch (IOException var1) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Exception releasing server lock " + var1, var1);
            }
         }
      }

   }

   public static synchronized void getAllServerLocks() throws ManagementException {
      if (serverLocks == null) {
         String[] var0 = getServers();
         serverLocks = new FileLock[var0.length + 1];
         serverLockFiles = new File[var0.length + 1];

         for(int var1 = 0; var1 < var0.length; ++var1) {
            serverLockFiles[var1] = getServerLockFile(var0[var1]);

            try {
               serverLocks[var1] = getServerLock(serverLockFiles[var1]);
            } finally {
               if (serverLocks[var1] == null) {
                  serverLockFiles[var1] = null;
               } else {
                  FileUtils.registerLockFile(serverLockFiles[var1]);
               }

            }
         }

         try {
            serverLockFiles[var0.length] = getDefaultServerLockFile();
            serverLocks[var0.length] = getServerLock(serverLockFiles[var0.length]);
         } finally {
            if (serverLocks[var0.length] == null) {
               serverLockFiles[var0.length] = null;
            } else {
               FileUtils.registerLockFile(serverLockFiles[var0.length]);
            }

         }

      }
   }

   public static synchronized void releaseAllServerLocks() {
      if (serverLocks != null) {
         for(int var0 = 0; var0 < serverLocks.length; ++var0) {
            if (serverLocks[var0] != null) {
               try {
                  serverLocks[var0].release();
                  serverLocks[var0].channel().close();
                  serverLocks[var0] = null;
                  serverLockFiles[var0].delete();
                  FileUtils.unregisterLockFile(serverLockFiles[var0]);
                  serverLockFiles[var0] = null;
               } catch (IOException var2) {
                  if (debugLogger.isDebugEnabled()) {
                     debugLogger.debug("Exception releasing all server locks " + var2, var2);
                  }
               }
            }
         }

         serverLocks = null;
         serverLockFiles = null;
      }
   }

   private static File getServerLockFile(String var0) {
      String var1 = DomainDir.getTempDirForServer(var0);
      File var2 = new File(var1);
      if (!var2.isAbsolute()) {
         File var3 = new File(DomainDir.getRootDir());
         new File(var3, var1);
      }

      if (!var2.exists()) {
         var2.mkdirs();
      }

      String var5 = var0 + ".lok";
      File var4 = new File(var2, var5);
      return var4;
   }

   private static FileLock getServerLock(File var0) throws ManagementException {
      FileLock var1 = null;

      try {
         var1 = Utils.getFileLock(var0, 185000L);
      } catch (Exception var3) {
         throw new ManagementException("Unable to obtain File lock on " + var0.getAbsolutePath() + " : " + var3);
      }

      if (var1 == null) {
         String var2 = "Unable to obtain lock on " + var0.getAbsolutePath() + ". Server may already be running";
         throw new ManagementException(var2);
      } else {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Got lock for " + var0);
         }

         return var1;
      }
   }

   private static File getDefaultServerLockFile() {
      File var0 = new File(DomainDir.getRootDir(), "default_server.lok");
      return var0;
   }

   private static String[] getServers() {
      ArrayList var0 = new ArrayList();
      File[] var1 = (new File(DomainDir.getServersDir())).listFiles();
      if (var1 == null) {
         return new String[0];
      } else {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (var1[var2].isDirectory()) {
               var0.add(var1[var2].getName());
            }
         }

         return (String[])((String[])var0.toArray(new String[var0.size()]));
      }
   }
}
