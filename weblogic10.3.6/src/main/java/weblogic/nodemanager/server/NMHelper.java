package weblogic.nodemanager.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import weblogic.nodemanager.NodeManagerTextTextFormatter;
import weblogic.nodemanager.system.NodeManagerSystem;
import weblogic.nodemanager.util.Platform;

public final class NMHelper {
   public static final String WINDOWS_IFCONFIG_SCRIPT = "wlsifconfig.cmd";
   public static final String UNIX_IFCONFIG_SCRIPT = "wlsifconfig.sh";
   public static final String IFCONFIG_ADD = "-addif";
   public static final String IFCONFIG_REMOVE = "-removeif";
   public static final String SERVER_NAME_PROP = "ServerName";
   public static final String SERVER_DIR_PROP = "ServerDir";
   public static final String MAC_BROADCAST_PROP = "UseMACBroadcast";
   private static final NodeManagerTextTextFormatter nmText = NodeManagerTextTextFormatter.getInstance();

   public static int executeScript(String[] var0, Properties var1, File var2, long var3) throws IOException {
      ProcessRunner var5 = new ProcessRunner(var0, var1, var2);
      var5.start();
      long var6 = System.currentTimeMillis();

      while(var5.isAlive() && waitingForTimeout(var6, var3)) {
         try {
            var5.join(var3);
         } catch (InterruptedException var14) {
         }

         if (!waitingForTimeout(var6, var3) && var5.isAlive()) {
            return -101;
         }
      }

      int var8;
      try {
         var8 = var5.getResponseCode();
      } finally {
         var5.cleanup();
      }

      return var8;
   }

   private static boolean waitingForTimeout(long var0, long var2) {
      if (var2 <= 0L) {
         return true;
      } else {
         return System.currentTimeMillis() < var0 + var2;
      }
   }

   public static String getIFControlScriptName() {
      return Platform.isWindows() ? "wlsifconfig.cmd" : "wlsifconfig.sh";
   }

   public static String[] buildAddMigrationCommand(String var0, String var1, String var2, String var3) {
      ArrayList var4 = new ArrayList();
      File var5 = new File(var3, getIFControlScriptName());
      var4.add(var5.getPath());
      var4.add(getIFControlAddParam());
      var4.add(wrapSpacesForCmdLine(var1));
      var4.add(var0);
      if (var2 != null) {
         var4.add(var2);
      }

      return (String[])((String[])var4.toArray(new String[var4.size()]));
   }

   public static String[] buildRemoveMigrationCommand(String var0, String var1, String var2) {
      ArrayList var3 = new ArrayList();
      File var4 = new File(var2, getIFControlScriptName());
      var3.add(var4.getPath());
      var3.add(getIFControlRemoveParam());
      var3.add(wrapSpacesForCmdLine(var1));
      var3.add(var0);
      return (String[])((String[])var3.toArray(new String[var3.size()]));
   }

   public static Properties buildMigrationEnv(String var0, String var1) {
      Properties var2 = new Properties();
      Iterator var3 = System.getenv().entrySet().iterator();

      while(var3.hasNext()) {
         Map.Entry var4 = (Map.Entry)var3.next();
         var2.put(var4.getKey(), var4.getValue());
      }

      var2.put("ServerName", var0);
      var2.put("ServerDir", var1);
      return var2;
   }

   public static Properties buildMigrationEnv(String var0, String var1, boolean var2) {
      Properties var3 = buildMigrationEnv(var0, var1);
      var3.put("UseMACBroadcast", var2);
      return var3;
   }

   private static String getIFControlAddParam() {
      return "-addif";
   }

   private static String getIFControlRemoveParam() {
      return "-removeif";
   }

   private static String wrapSpacesForCmdLine(String var0) {
      if (var0.indexOf(" ") > -1) {
         if (var0.startsWith("\"") && var0.endsWith("\"")) {
            return var0;
         }

         var0 = "\"" + var0 + "\"";
      }

      return var0;
   }

   private static boolean forceProcessCleanup() {
      return false;
   }

   private static class Drainer extends Thread {
      private BufferedReader in;
      private Level level;

      Drainer(InputStream var1, Level var2) {
         this.in = new BufferedReader(new InputStreamReader(var1));
         this.level = var2;
      }

      public void run() {
         while(true) {
            try {
               String var1;
               if ((var1 = this.in.readLine()) != null) {
                  NodeManagerSystem.getInstance().getLogger().log(this.level, var1);
                  continue;
               }
            } catch (IOException var3) {
               NodeManagerSystem.getInstance().getLogger().log(Level.FINEST, "problem logging script output due to exception", var3);
            }

            return;
         }
      }

      public void cleanup() {
      }
   }

   private static class ProcessRunner extends Thread {
      private Process proc;
      private IOException caughtException;
      private String[] cmd;
      private String[] env;
      private File workingDir;
      private Drainer outDrainer;
      private Drainer errDrainer;

      public ProcessRunner(String[] var1, Properties var2, File var3) {
         this.cmd = var1;
         this.workingDir = var3;
         if (var2 == null) {
            this.env = null;
         } else {
            Iterator var4 = var2.entrySet().iterator();
            ArrayList var5 = new ArrayList();

            while(var4.hasNext()) {
               Map.Entry var6 = (Map.Entry)var4.next();
               var5.add(var6.getKey() + "=" + var6.getValue());
            }

            this.env = (String[])((String[])var5.toArray(new String[var5.size()]));
         }

      }

      public void run() {
         try {
            this.proc = Runtime.getRuntime().exec(this.cmd, this.env, this.workingDir);
            this.proc.getOutputStream().close();
            this.outDrainer = new Drainer(this.proc.getInputStream(), Level.INFO);
            this.errDrainer = new Drainer(this.proc.getErrorStream(), Level.WARNING);
            this.outDrainer.start();
            this.errDrainer.start();

            try {
               this.proc.waitFor();
               this.outDrainer.join();
               this.errDrainer.join();
            } catch (InterruptedException var2) {
               var2.printStackTrace();
            }
         } catch (IOException var3) {
            this.caughtException = var3;
         }

      }

      public int getResponseCode() throws IOException {
         if (this.caughtException != null) {
            throw this.caughtException;
         } else if (this.proc == null) {
            throw new AssertionError("Process never got set!");
         } else {
            return this.proc.exitValue();
         }
      }

      public void cleanup() {
         if (NMHelper.forceProcessCleanup()) {
            this.proc.destroy();
         }

         if (this.outDrainer != null) {
            this.outDrainer.cleanup();
         }

         if (this.errDrainer != null) {
            this.errDrainer.cleanup();
         }

      }
   }
}
