package weblogic.nodemanager.client;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.util.Properties;
import weblogic.nodemanager.NodeManagerTextTextFormatter;
import weblogic.nodemanager.ScriptExecutionFailureException;
import weblogic.nodemanager.common.ServerType;

public abstract class NMClient {
   protected String host;
   protected int port;
   protected String nmDir;
   protected String domainName;
   protected String domainDir;
   protected String serverName;
   protected ServerType serverType;
   protected static final String ENCODING = "UTF-8";
   protected byte[] nmUser;
   protected byte[] nmPass;
   protected boolean verbose;
   protected PrintStream stdout;
   public static final String PLAIN = "plain";
   public static final String SSL = "ssl";
   public static final String SSH = "ssh";
   public static final String RSH = "rsh";
   public static final String SHELL = "shell";
   public static final String VMM = "vmm-";
   public static final String VMM_SECURE = "vmms-";
   private static final NodeManagerTextTextFormatter nmText = NodeManagerTextTextFormatter.getInstance();

   NMClient() {
      this.stdout = System.out;
      this.serverType = ServerType.WebLogic;
   }

   public static NMClient getInstance(String var0) {
      if ("plain".equalsIgnoreCase(var0)) {
         return new PlainClient();
      } else if ("ssl".equalsIgnoreCase(var0)) {
         return new SSLClient();
      } else if ("shell".equalsIgnoreCase(var0)) {
         return new ShellClient();
      } else if ("ssh".equalsIgnoreCase(var0)) {
         return new SSHClient();
      } else if ("rsh".equalsIgnoreCase(var0)) {
         return new RSHClient();
      } else if (!(var0 + "-").toLowerCase().startsWith("vmm-".toLowerCase()) && !(var0 + "-").toLowerCase().startsWith("vmms-".toLowerCase())) {
         throw new IllegalArgumentException(nmText.getUnknownClient(var0));
      } else {
         return new VMMClient(var0);
      }
   }

   protected void checkNullOrEmpty(String var1, String var2) throws IllegalArgumentException {
      if (var1 == null || var1.length() == 0) {
         throw new IllegalArgumentException(nmText.getNullOrEmpty(var2));
      }
   }

   public synchronized void setHost(String var1) {
      this.checkNullOrEmpty(var1, nmText.getInvalidHostName());
      this.checkNotConnected();
      this.host = var1;
   }

   public synchronized void setPort(int var1) {
      if (var1 <= 0) {
         throw new IllegalArgumentException(nmText.getInvalidPort(Integer.toString(var1)));
      } else {
         this.checkNotConnected();
         this.port = var1;
      }
   }

   public synchronized void setDomainName(String var1) {
      this.checkNullOrEmpty(var1, nmText.getInvalidDomain());
      this.checkNotConnected();
      this.domainName = var1;
   }

   public synchronized void setDomainDir(String var1) {
      this.checkNullOrEmpty(var1, nmText.getInvalidDomainDir());
      this.checkNotConnected();
      this.domainDir = var1;
   }

   public synchronized void setServerName(String var1) {
      this.checkNullOrEmpty(var1, nmText.getInvalidServerName());
      this.serverName = var1;
   }

   public synchronized void setServerType(ServerType var1) {
      this.serverType = var1;
   }

   public synchronized void setNMDir(String var1) {
      this.checkNullOrEmpty(var1, nmText.getInvalidNMHome());
      this.checkNotConnected();
      this.nmDir = var1;
   }

   public synchronized void setNMUser(String var1) {
      this.checkNullOrEmpty(var1, nmText.getInvalidUser());
      this.checkNotConnected();

      try {
         this.nmUser = var1.getBytes("UTF-8");
      } catch (IOException var3) {
         this.nmUser = null;
      }

   }

   public synchronized void setNMPass(String var1) {
      this.checkNullOrEmpty(var1, nmText.getInvalidPwd());
      this.checkNotConnected();

      try {
         this.nmPass = var1.getBytes("UTF-8");
      } catch (IOException var3) {
         this.nmPass = null;
      }

   }

   public void execScript(String var1, long var2) throws IOException, ScriptExecutionFailureException {
      this.checkNullOrEmpty(var1, nmText.getInvalidScriptPath());
      this.executeScript(var1, var2);
   }

   public synchronized void setVerbose(boolean var1) {
      this.verbose = var1;
   }

   public abstract String getStates(int var1) throws IOException;

   public abstract String getState(int var1) throws IOException;

   public abstract String getVersion() throws IOException;

   public abstract void getNMLog(Writer var1) throws IOException;

   public abstract void getLog(Writer var1) throws IOException;

   public abstract void start() throws IOException;

   public abstract void start(Properties var1) throws IOException;

   public abstract void kill() throws IOException;

   public abstract void done() throws IOException;

   public abstract void quit() throws IOException;

   protected abstract void checkNotConnected() throws IllegalStateException;

   public abstract void executeScript(String var1, long var2) throws IOException, ScriptExecutionFailureException;

   public abstract void updateServerProps(Properties var1) throws IOException;

   public void setOutputStream(PrintStream var1) {
      this.stdout = var1;
   }
}
