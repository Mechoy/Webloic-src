package weblogic.nodemanager.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;
import weblogic.nodemanager.NMException;
import weblogic.nodemanager.NodeManagerTextTextFormatter;
import weblogic.nodemanager.ScriptExecutionFailureException;
import weblogic.nodemanager.common.Command;
import weblogic.nodemanager.util.Platform;
import weblogic.utils.StringUtils;

public class ShellClient extends NMClient {
   private Process proc;
   private BufferedWriter out;
   private BufferedReader in;
   private boolean connected;
   private String shellCommand;
   private String script;
   private ErrDrainer errDrainer;
   int connectTimeout;
   private static boolean debug = false;
   private static final String SCRIPT_SPECIFIER = "-e ";
   public static final String SHELL_COMMAND_PROP = "weblogic.nodemanager.ShellCommand";
   public static final String SHELL_COMMAND = "wlscontrol.sh -d %D -r %R -s %S %C";
   private static final NodeManagerTextTextFormatter nmText = NodeManagerTextTextFormatter.getInstance();
   private Vector removedOptions;

   public ShellClient() {
      this(System.getProperty("weblogic.nodemanager.ShellCommand", "wlscontrol.sh -d %D -r %R -s %S %C"));
   }

   public ShellClient(String var1) {
      this.shellCommand = var1;
   }

   public synchronized String getVersion() throws IOException {
      this.checkConnected(false);
      this.execCmd(Command.VERSION);

      String var1;
      String var2;
      for(var2 = null; (var1 = this.readLine()) != null; var2 = var1) {
      }

      this.checkResponse();
      return var2;
   }

   public synchronized String getState(int var1) throws IOException {
      this.checkConnected(true);
      String var2 = null;
      NMCommandRunner var3 = new NMCommandRunner(Command.STAT);
      var3.start();

      try {
         var3.join((long)var1);
         if (var3.isAlive()) {
            throw new IOException(nmText.getOperationTimedOut(Command.STAT.toString()));
         } else {
            IOException var4 = var3.getIOException();
            if (var4 != null) {
               throw var4;
            } else {
               var2 = var3.getResult();
               return var2;
            }
         }
      } catch (InterruptedException var5) {
         throw new IOException(nmText.getOperationInterrupted(Command.STAT.toString()));
      }
   }

   public synchronized String getStates(int var1) throws IOException {
      if (this.domainName == null) {
         throw new IllegalStateException(nmText.getDomainNotSet());
      } else {
         String var2 = null;
         NMCommandRunner var3 = new NMCommandRunner(Command.GETSTATES);
         var3.start();

         try {
            var3.join((long)var1);
            if (var3.isAlive()) {
               throw new IOException(nmText.getOperationTimedOut(Command.GETSTATES.toString()));
            } else {
               IOException var4 = var3.getIOException();
               if (var4 != null) {
                  throw var4;
               } else {
                  var2 = var3.getResult();
                  return var2;
               }
            }
         } catch (InterruptedException var5) {
            throw new IOException(nmText.getOperationInterrupted(Command.GETSTATES.toString()));
         }
      }
   }

   public synchronized void getNMLog(Writer var1) throws IOException {
      BufferedWriter var2 = new BufferedWriter(var1);
      var2.write(nmText.getCommandNotAvailable(Command.GETNMLOG.toString()));
      var2.newLine();
      var2.flush();
   }

   public synchronized void getLog(Writer var1) throws IOException {
      this.checkConnected(true);
      this.execCmd(Command.GETLOG);
      this.copyTo(new BufferedWriter(var1), false);
      this.checkResponse();
   }

   public synchronized void start() throws IOException {
      this.checkConnected(true);
      this.execCmd(Command.START);
      this.checkResponse();
   }

   public synchronized void start(Properties var1) throws IOException {
      this.checkConnected(true);
      this.execCmd(Command.STARTP);
      Iterator var2 = var1.keySet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         String var4 = var1.getProperty(var3);
         this.writeLine(var3 + "=" + var4);
      }

      this.endOutput();
      this.checkResponse();
   }

   public synchronized void kill() throws IOException {
      this.checkConnected(true);
      this.execCmd(Command.KILL);
      this.checkResponse();
   }

   public synchronized void quit() throws IOException {
      this.stdout.println(nmText.getCommandNotAvailable(Command.QUIT.toString()));
      this.stdout.flush();
   }

   protected void checkNotConnected() {
      assert !this.connected;

   }

   public void executeScript(String var1, long var2) throws IOException, ScriptExecutionFailureException {
      if (var1 != null && !var1.equals("")) {
         this.script = var1;
         this.execCmd(Command.EXECSCRIPT);
         this.checkResponse();
      } else {
         throw new IOException(nmText.getInvalidPath(var1));
      }
   }

   public void updateServerProps(Properties var1) throws IOException {
   }

   public void setShellCommand(String var1) {
      this.shellCommand = var1;
   }

   private void checkConnected(boolean var1) {
      if (!this.connected) {
         if (this.domainName == null) {
            throw new IllegalStateException(nmText.getDomainNotSet());
         }

         if (var1 && this.serverName == null) {
            throw new IllegalStateException(nmText.getServerNotSet());
         }

         this.connected = true;
      }

   }

   private void execCmd(Command var1) throws IOException {
      String[] var2 = this.getCommandLine(var1, this.shellCommand);
      if (this.verbose) {
         this.stdout.println("DEBUG: ShellClient: Executing shell command: " + StringUtils.join(var2, " "));
      }

      this.proc = Runtime.getRuntime().exec(var2);
      this.errDrainer = new ErrDrainer(this.proc.getErrorStream());
      this.errDrainer.start();
   }

   protected String[] getCommandLine(Command var1, String var2) {
      ArrayList var3 = new ArrayList();
      this.removedOptions = new Vector();
      String[] var4 = var2.split("\\s");

      for(int var5 = 0; var5 < var4.length; ++var5) {
         var3.add(this.parse(var1, var4[var5]));
      }

      String[] var9 = (String[])((String[])var3.toArray(new String[var3.size()]));
      if (this.removedOptions.size() > 0) {
         StringBuffer var6 = new StringBuffer();
         var6.append("-[");

         for(int var7 = 0; var7 < this.removedOptions.size(); ++var7) {
            var6.append(this.removedOptions.get(var7));
         }

         var6.append("]");
         String var10 = var6.toString();

         for(int var8 = 0; var8 < var9.length; ++var8) {
            if (var9[var8].matches(var10)) {
               var9[var8] = "";
            }
         }
      }

      return var9;
   }

   private String parse(Command var1, String var2) {
      StringBuffer var3 = new StringBuffer();
      int var4 = 0;

      while(var4 < var2.length()) {
         char var5 = var2.charAt(var4++);
         if (var5 == '%') {
            var5 = var2.charAt(var4++);
            switch (var5) {
               case 'C':
               case 'c':
                  if (this.script != null) {
                     var3.append("-e ");
                     var3.append(this.script + " ");
                  }

                  var3.append(var1.getName());
                  break;
               case 'D':
               case 'd':
                  var3.append(this.domainName);
                  break;
               case 'E':
               case 'F':
               case 'G':
               case 'I':
               case 'J':
               case 'K':
               case 'L':
               case 'M':
               case 'O':
               case 'Q':
               case 'T':
               case 'U':
               case 'V':
               case 'W':
               case 'X':
               case 'Y':
               case 'Z':
               case '[':
               case '\\':
               case ']':
               case '^':
               case '_':
               case '`':
               case 'a':
               case 'b':
               case 'e':
               case 'f':
               case 'g':
               case 'i':
               case 'j':
               case 'k':
               case 'l':
               case 'm':
               case 'o':
               case 'q':
               default:
                  var3.append('%');
                  var3.append((char)var5);
                  break;
               case 'H':
               case 'h':
                  var3.append(this.host);
                  break;
               case 'N':
               case 'n':
                  var3.append(Platform.isUnix() ? "\\'" : "\"");
                  var3.append(this.nmDir != null ? this.nmDir : System.getProperty("user.dir"));
                  var3.append(Platform.isUnix() ? "\\'" : "\"");
                  break;
               case 'P':
               case 'p':
                  var3.append(Integer.toString(this.port));
                  break;
               case 'R':
               case 'r':
                  if (this.domainDir != null) {
                     var3.append(Platform.isUnix() ? "\\'" : "\"");
                     var3.append(this.domainDir != null ? this.domainDir : ".");
                     var3.append(Platform.isUnix() ? "\\'" : "\"");
                  } else {
                     this.removedOptions.add(new String("Rr"));
                  }
                  break;
               case 'S':
               case 's':
                  if (this.serverName != null) {
                     var3.append(Platform.isUnix() ? "\\'" : "\"");
                     var3.append(this.serverName);
                     var3.append(Platform.isUnix() ? "\\'" : "\"");
                  } else {
                     this.removedOptions.add(new String("Ss"));
                  }
            }
         } else {
            var3.append((char)var5);
         }
      }

      String var6 = var3.toString();
      return var6;
   }

   public void done() {
   }

   private void checkResponse() throws IOException {
      String var1 = null;
      this.endOutput();

      while(this.readLine() != null) {
      }

      int var2;
      try {
         var2 = this.proc.waitFor();
         this.errDrainer.join();
      } catch (InterruptedException var4) {
         throw new IOException(nmText.getIOInterrupted());
      }

      if (var2 != 0 || (var1 = this.errDrainer.getLastLine()) != null) {
         if (var2 != 0) {
            var1 = this.errDrainer.getLastLine();
         }

         if (var1 == null) {
            var1 = "nmText.getUnknownSHError(rc)";
         }
      }

      this.proc.destroy();
      this.proc = null;
      this.out = null;
      this.in = null;
      this.connected = false;
      if (var2 == -100) {
         throw new AssertionError("Script is reporting a usage error: -100");
      } else if (var1 != null) {
         throw new NMException(var1);
      }
   }

   private void endOutput() throws IOException {
      if (this.out != null) {
         this.out.flush();
         this.out.close();
         this.out = null;
      }

   }

   private static void p(String var0) {
      if (debug) {
         System.out.println("ShellClient => " + var0);
      }

   }

   private BufferedReader getReader() {
      if (this.in == null) {
         this.in = new BufferedReader(new InputStreamReader(this.proc.getInputStream()));
      }

      return this.in;
   }

   private String readLine() throws IOException {
      String var1 = this.getReader().readLine();
      if (this.verbose && var1 != null) {
         this.stdout.println("DEBUG: ShellClient: STDOUT: " + var1);
      }

      return var1;
   }

   private void writeLine(String var1) throws IOException {
      BufferedWriter var2 = this.getWriter();
      var2.write(var1);
      var2.newLine();
      if (this.verbose) {
         this.stdout.println("DEBUG: ShellClient: STDIN: " + var1);
      }

   }

   private BufferedWriter getWriter() {
      if (this.out == null) {
         this.out = new BufferedWriter(new OutputStreamWriter(this.proc.getOutputStream()));
      }

      return this.out;
   }

   private void copyTo(BufferedWriter var1, boolean var2) throws IOException {
      String var3;
      while((var3 = this.readLine()) != null) {
         var1.write(var3);
         var1.newLine();
         if (var2) {
            var1.flush();
         }
      }

      var1.flush();
   }

   private class NMCommandRunner extends Thread {
      private Command cmd;
      private String result;
      private IOException ioe;

      NMCommandRunner(Command var2) {
         this.cmd = var2;
      }

      public void run() {
         try {
            ShellClient.this.execCmd(this.cmd);

            String var1;
            while((var1 = ShellClient.this.readLine()) != null) {
               this.result = var1;
            }

            ShellClient.this.checkResponse();
         } catch (IOException var2) {
            this.ioe = var2;
         } catch (Throwable var3) {
            this.ioe = (IOException)(new IOException("Exception: " + var3.getMessage())).initCause(var3);
         }

      }

      String getResult() {
         return this.result;
      }

      IOException getIOException() {
         return this.ioe;
      }

      void killProcess() {
         ShellClient.this.proc.destroy();
      }

      boolean stillRunning() {
         try {
            int var1 = ShellClient.this.proc.exitValue();
            return false;
         } catch (IllegalThreadStateException var2) {
            return true;
         }
      }
   }

   private class ErrDrainer extends Thread {
      private BufferedReader in;
      private String lastLine;

      ErrDrainer(InputStream var2) {
         this.in = new BufferedReader(new InputStreamReader(var2));
      }

      public void run() {
         while(true) {
            try {
               String var1;
               if ((var1 = this.in.readLine()) != null) {
                  if (ShellClient.this.verbose) {
                     ShellClient.this.stdout.println("DEBUG: ShellClient: STDERR: " + var1);
                  }

                  this.lastLine = var1;
                  continue;
               }
            } catch (IOException var2) {
            }

            return;
         }
      }

      String getLastLine() {
         return this.lastLine;
      }
   }
}
