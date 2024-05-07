package weblogic.nodemanager.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import weblogic.nodemanager.NodeManagerTextTextFormatter;
import weblogic.nodemanager.common.StartupConfig;
import weblogic.nodemanager.util.ProcessControl;
import weblogic.nodemanager.util.UnixProcessControl;

public class LogFileRotationUtil {
   private static final Logger nmLog = Logger.getLogger("weblogic.nodemanager");
   private static final NodeManagerTextTextFormatter nmText = NodeManagerTextTextFormatter.getInstance();

   static void rotateServerFiles(ServerManagerI var0, StartupConfig var1) throws IOException {
      DomainManager var2 = var0.getDomainManager();
      String var3 = var2.getDomainName();
      String var4 = var0.getServerName();
      ServerDir var5 = var0.getServerDir();
      changeDirOwnerships(var0, var1);
      File var6 = var5.getOutFile();
      File var7;
      if (var6.exists()) {
         var7 = rotateLogFile(var6);
         info(var3, var4, nmText.getRotatedOutputLog(var7.getPath()));
      }

      var5.getOutFile().createNewFile();
      changeOwnership(var0, var1, var5.getOutFile());
      var5.getDomainDir().getConfigFile().createNewFile();
      changeOwnership(var0, var1, var5.getDomainDir().getConfigFile());
      var7 = var5.getErrFile();
      boolean var8 = !var7.equals(var6);
      if (var8) {
         if (var7.exists()) {
            File var9 = rotateLogFile(var7);
            info(var3, var4, nmText.getRotatedMsg(var9.getPath()));
         }

         var5.getErrFile().createNewFile();
         changeOwnership(var0, var1, var5.getErrFile());
      } else {
         info(var3, var4, nmText.getRotatedErrorLogMsg());
      }

   }

   private static File rotateLogFile(File var0) throws IOException {
      if (!var0.exists()) {
         throw new FileNotFoundException(var0.toString());
      } else {
         File var1 = getRotatedFile(var0);
         var1.delete();
         if (!var0.renameTo(var1)) {
            throw new IOException(nmText.getRotationError(var0.toString(), var1.toString()));
         } else {
            return var1;
         }
      }
   }

   private static File getRotatedFile(File var0) {
      File var1 = var0.getAbsoluteFile().getParentFile();
      String var2 = var0.getName();
      int var3 = var2.length();
      int var4 = 0;
      File[] var5 = var1.listFiles();

      for(int var6 = 0; var6 < var5.length; ++var6) {
         File var7 = var5[var6];
         if (var7.isFile() && var7.getName().startsWith(var2)) {
            String var8 = var7.getName().substring(var3);

            try {
               int var9 = Integer.parseInt(var8);
               if (var9 > var4) {
                  var4 = var9;
               }
            } catch (NumberFormatException var10) {
            }
         }
      }

      ++var4;
      if (var4 > 99999) {
         var4 = 0;
      }

      var2 = var2 + var4 / 10000 % 10 + var4 / 1000 % 10 + var4 / 100 % 10 + var4 / 10 % 10 + var4 % 10;
      return new File(var1, var2);
   }

   private static void changeDirOwnerships(ServerManagerI var0, StartupConfig var1) {
      ServerDir var2 = var0.getServerDir();
      changeOwnership(var0, var1, var2);
      changeOwnership(var0, var1, var2.getLogsDir());
      changeOwnership(var0, var1, var2.getSecurityDir());
      changeOwnership(var0, var1, var2.getDataDir());
      changeOwnership(var0, var1, var2.getNMDataDir());
      changeOwnership(var0, var1, var2.getTmpDir());
      changeOwnership(var0, var1, var2.getDomainBakDir());
      changeOwnership(var0, var1, var2.getConfigPrevDir());
   }

   private static void changeOwnership(ServerManagerI var0, StartupConfig var1, File var2) {
      NMServer var3 = var0.getDomainManager().getNMServer();
      String var4 = var0.getDomainManager().getDomainName();
      String var5 = var0.getServerName();
      ProcessControl var6 = var3.getConfig().getProcessControl();
      if (var6 instanceof UnixProcessControl) {
         UnixProcessControl var7 = (UnixProcessControl)var6;
         String var8 = var1.getUid();
         String var9 = var1.getGid();
         if (var8 != null || var9 != null) {
            if (var7.changeFileOwnership(var2, var8, var9)) {
               info(var4, var5, nmText.getChangeFileOwnershipSucceeded(var2.getPath(), var8 == null ? "" : var8, var9 == null ? "" : var9));
            } else {
               warning(var4, var5, nmText.getChangeFileOwnershipFailed(var2.getPath(), var8 == null ? "" : var8, var9 == null ? "" : var9));
            }
         }
      }

   }

   private static void log(String var0, String var1, Level var2, String var3, Throwable var4) {
      LogRecord var5 = new LogRecord(var2, var3);
      var5.setParameters(new String[]{var0, var1});
      if (var4 != null) {
         var5.setThrown(var4);
      }

      nmLog.log(var5);
   }

   public static void log(String var0, String var1, Level var2, String var3) {
      log(var0, var1, var2, var3, (Throwable)null);
   }

   private static void info(String var0, String var1, String var2) {
      log(var0, var1, Level.INFO, var2);
   }

   private static void finest(String var0, String var1, String var2) {
      log(var0, var1, Level.FINEST, var2);
   }

   private static void warning(String var0, String var1, String var2) {
      log(var0, var1, Level.WARNING, var2);
   }

   private static void severe(String var0, String var1, String var2, Throwable var3) {
      log(var0, var1, Level.SEVERE, var2, var3);
   }

   private static void severe(String var0, String var1, String var2) {
      log(var0, var1, Level.SEVERE, var2);
   }
}
