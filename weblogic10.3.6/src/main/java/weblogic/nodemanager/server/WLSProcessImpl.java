package weblogic.nodemanager.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Map;
import weblogic.logging.WLLevel;

public class WLSProcessImpl extends WLSProcess {
   private Process proc;
   private Drainer outDrainer;
   private Drainer errDrainer;

   public WLSProcessImpl(String[] var1, Map var2, File var3, File var4) {
      super(var1, var2, var3, var4);
      this.setLogger(NMServer.nmLog);
      this.setErrorLevel(WLLevel.WARNING);
   }

   protected final void start() throws IOException {
      FileOutputStream var1 = new FileOutputStream(this.getOutFile(), true);
      OutputStreamWriter var2 = new OutputStreamWriter(var1);
      ProcessBuilder var3 = this.createProcessObject();
      this.proc = var3.start();
      this.proc.getOutputStream().close();
      this.outDrainer = new Drainer(this.proc.getInputStream(), var2);
      this.errDrainer = new Drainer(this.proc.getErrorStream(), var2);
      this.outDrainer.start();
      this.errDrainer.start();
   }

   ProcessBuilder createProcessObject() {
      ProcessBuilder var1 = new ProcessBuilder(Arrays.asList(this.getCommand()));
      Map var2 = var1.environment();
      Map var3;
      if ((var3 = this.getEnv()) != null) {
         var2.putAll(var3);
      }

      var1.directory(this.getDir());
      return var1;
   }

   public void destroy() {
      this.proc.destroy();
   }

   protected final void waitFor() throws InterruptedException {
      this.proc.waitFor();
      this.outDrainer.join();
      this.errDrainer.join();
   }

   public boolean isAlive() {
      try {
         this.proc.exitValue();
         return false;
      } catch (IllegalThreadStateException var2) {
         return true;
      }
   }

   public String getProcessId() {
      return null;
   }

   private static class Drainer extends Thread {
      private BufferedReader in;
      private Writer out;
      private static final String EOL = System.getProperty("line.separator");

      Drainer(InputStream var1, Writer var2) {
         this.in = new BufferedReader(new InputStreamReader(var1));
         this.out = var2;
      }

      public void run() {
         while(true) {
            try {
               String var1;
               if ((var1 = this.in.readLine()) != null) {
                  this.out.write(var1 + EOL);
                  this.out.flush();
                  continue;
               }
            } catch (Throwable var4) {
               NMServer.nmLog.log(WLLevel.WARNING, "Uncaught exception in process output drainer", var4);
            }

            try {
               this.out.close();
            } catch (IOException var3) {
               NMServer.nmLog.log(WLLevel.WARNING, "Unable to close server output log file", var3);
            }

            return;
         }
      }
   }
}
