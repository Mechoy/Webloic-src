package weblogic.ant.taskdefs.management;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.Java;
import org.apache.tools.ant.taskdefs.PumpStreamHandler;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.types.CommandlineJava;
import org.apache.tools.ant.types.Environment;
import weblogic.nodemanager.client.NMClient;

public class NMServer extends Java implements Runnable, BuildListener {
   private String home = null;
   private String config = null;
   private File domainDir = null;
   private boolean verbose = false;
   private boolean debug = false;
   private String listenPort = "5555";
   private String listenAddress = "localhost";
   private boolean noExit = false;
   private String action = "start";
   private String userName;
   private String password;
   private String domainName = "mydomain";
   private boolean quitEnabled = true;
   private boolean acceptDemoTrust = false;
   private Process process = null;
   private static final String NM_MAIN_CLASS = "weblogic.nodemanager.server.NMServer";
   private boolean authenticationEnabled = true;

   public void setAcceptDemoTrust(boolean var1) {
      this.acceptDemoTrust = var1;
   }

   public void setQuitEnabled(boolean var1) {
      this.quitEnabled = var1;
   }

   public void setDomainName(String var1) {
      this.domainName = var1;
   }

   public void setNoExit(boolean var1) {
      this.noExit = var1;
   }

   public void setListenPort(String var1) {
      this.listenPort = var1;
   }

   public void setListenAddress(String var1) {
      this.listenAddress = var1;
   }

   public void setUserName(String var1) {
      this.userName = var1;
   }

   public void setPassword(String var1) {
      this.password = var1;
   }

   public void setAction(String var1) {
      this.action = var1;
   }

   public void setHome(String var1) {
      this.home = var1;
   }

   public void setAuthenticationEnabled(boolean var1) {
      this.authenticationEnabled = var1;
   }

   public void setConfig(String var1) {
      this.config = var1;
   }

   public void setDir(File var1) {
      this.domainDir = var1;
      super.setDir(var1);
   }

   public void setVerbose(boolean var1) {
      this.verbose = var1;
   }

   public void setDebug(boolean var1) {
      this.debug = var1;
   }

   public void execute() throws BuildException {
      if (this.action.equals("start")) {
         this.startAction();
      } else if (this.action.equals("shutdown")) {
         this.shutdownAction();
      } else {
         System.err.println("Unknown action " + this.action);
         throw new BuildException("Unknown action " + this.action);
      }
   }

   private void startAction() throws BuildException {
      this.getProject().addBuildListener(this);
      this.setClassname("weblogic.nodemanager.server.NMServer");
      String var1 = this.getClass().getName();
      String var2 = this.getClass().getPackage().getName();
      var1 = var1.substring(var2.length() + 1, var1.length());
      this.setTaskName(var1);
      this.setFork(true);
      this.setSpawn(true);
      if (this.domainDir != null && !this.domainDir.exists()) {
         throw new BuildException(this.domainDir + " doesn't exist.");
      } else {
         this.checkSuppliedCredentials();
         CommandlineJava var3 = new CommandlineJava();
         var3.setClassname("weblogic.nodemanager.server.NMServer");
         this.setNMProperty(var3, "ListenPort", this.listenPort);
         this.setNMProperty(var3, "ListenAddress", this.listenAddress);
         this.setNMProperty(var3, "QuitEnabled", Boolean.toString(this.quitEnabled));
         if (this.home != null) {
            this.setCMDLArg(var3, "NodeManagerHome", this.home);
         }

         if (this.config != null) {
            this.setCMDLArg(var3, "PropertiesFile", this.config);
         }

         if (!this.authenticationEnabled) {
            this.setCMDLArg(var3, "AuthenticationEnabled", "false");
         }

         if (this.verbose) {
            var3.createArgument().setValue("-v");
         }

         if (this.debug) {
            var3.createArgument().setValue("-d");
         }

         this.startProcess(var3);
         if (this.waitForNMStart()) {
            System.out.println("Nodemanager is started");
         } else {
            System.out.println("Nodemanager is not started. It may still be booting or has failed. Check nodemanager log for details.");
         }

      }
   }

   private boolean waitForNMStart() {
      String var1 = System.getProperty("weblogic.security.TrustKeyStore");
      boolean var2 = false;
      if (this.acceptDemoTrust && (var1 == null || !var1.equals("DemoTrust"))) {
         System.setProperty("weblogic.security.TrustKeyStore", "DemoTrust");
         var2 = true;
      }

      NMClient var3 = NMClient.getInstance("ssl");
      var3.setDomainName(this.domainName);
      var3.setPort(Integer.parseInt(this.listenPort));
      var3.setHost(this.listenAddress);
      var3.setVerbose(true);
      var3.setNMUser(this.userName);
      var3.setNMPass(this.password);
      boolean var4 = false;

      for(int var5 = 0; !var4 && var5 < 20; ++var5) {
         try {
            String var6 = var3.getVersion();
            var3.done();
            var4 = true;
         } catch (IOException var9) {
            System.out.println("Nodemanager is not started. Waiting...");

            try {
               Thread.sleep(5000L);
            } catch (InterruptedException var8) {
            }
         }
      }

      if (var2) {
         if (var1 == null) {
            System.clearProperty("weblogic.security.TrustKeyStore");
         } else {
            System.setProperty("weblogic.security.TrustKeyStore", var1);
         }
      }

      return var4;
   }

   private void startProcess(CommandlineJava var1) {
      try {
         System.out.println("Starting CommandLine " + var1.describeJavaCommand());
         this.process = Execute.launch(this.getProject(), var1.getCommandline(), (new Environment()).getVariables(), this.domainDir, true);
         if (Os.isFamily("windows")) {
            try {
               Thread.sleep(5000L);
            } catch (InterruptedException var4) {
               this.getProject().log("interruption in the sleep after having spawned a process", 3);
            }
         }

         OutputStream var2 = new OutputStream() {
            public void write(int var1) {
            }
         };
         PumpStreamHandler var3 = new PumpStreamHandler(var2);
         var3.setProcessErrorStream(this.process.getErrorStream());
         var3.setProcessOutputStream(this.process.getInputStream());
         var3.start();
         this.process.getOutputStream().close();
         this.getProject().log("spawned process " + this.process.toString(), 3);
      } catch (IOException var5) {
         System.err.println("Unable to start a process for " + var1);
         var5.printStackTrace();
      }

   }

   private void shutdownAction() throws BuildException {
      String var1 = System.getProperty("weblogic.security.TrustKeyStore");
      boolean var2 = false;
      if (this.acceptDemoTrust && (var1 == null || !var1.equals("DemoTrust"))) {
         System.setProperty("weblogic.security.TrustKeyStore", "DemoTrust");
         var2 = true;
      }

      NMClient var3 = NMClient.getInstance("ssl");
      var3.setDomainName(this.domainName);
      var3.setPort(Integer.parseInt(this.listenPort));
      var3.setHost(this.listenAddress);
      var3.setVerbose(true);
      this.checkSuppliedCredentials();
      var3.setNMUser(this.userName);
      var3.setNMPass(this.password);

      try {
         System.out.println("Shutting down the node manager");
         var3.quit();
         var3.done();
      } catch (IOException var5) {
         throw new BuildException(var5);
      }

      if (var2) {
         if (var1 == null) {
            System.clearProperty("weblogic.security.TrustKeyStore");
         } else {
            System.setProperty("weblogic.security.TrustKeyStore", var1);
         }
      }

   }

   private void checkSuppliedCredentials() throws BuildException {
      if (this.userName == null || this.password == null) {
         throw new BuildException("NodeManager username and password must be supplied");
      }
   }

   private void setNMProperty(CommandlineJava var1, String var2, Object var3) {
      if (var3 != null) {
         String var4 = "-Dweblogic.nodemanager." + var2 + "=" + var3;
         System.out.println("Adding NM argument" + var4);
         var1.createVmArgument().setValue(var4);
      }

   }

   private void setCMDLArg(CommandlineJava var1, String var2, Object var3) {
      if (var3 != null) {
         var1.createVmArgument().setValue("-D" + var2 + "=" + var3.toString());
      }

   }

   public void run() {
   }

   public void buildStarted(BuildEvent var1) {
   }

   public void buildFinished(BuildEvent var1) {
      if (!this.noExit) {
         System.out.println("Killing NodeManager Instance");
         this.shutdownAction();
      } else {
         System.out.println("Leaving the NodeManager running");
      }

   }

   public void targetStarted(BuildEvent var1) {
   }

   public void targetFinished(BuildEvent var1) {
   }

   public void taskStarted(BuildEvent var1) {
   }

   public void taskFinished(BuildEvent var1) {
   }

   public void messageLogged(BuildEvent var1) {
   }
}
