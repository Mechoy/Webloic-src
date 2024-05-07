package weblogic.ant.taskdefs.management;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.rmi.ConnectException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.QueryExp;
import javax.management.ReflectionException;
import javax.naming.CommunicationException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.taskdefs.Java;
import weblogic.Home;
import weblogic.rjvm.PeerGoneException;
import weblogic.rmi.extensions.RemoteRuntimeException;

public class WLServer extends Java implements Runnable, BuildListener {
   private static final String DEFAULT_POLICY_FILE = "weblogic.policy";
   private static final String WEBLOGIC_MAIN_CLASS = "weblogic.Server";
   private static final String DEFAULT_HOST = "localhost";
   private static final String DEFAULT_PROTOCOL = "t3";
   private static final int DEFAULT_PORT = 7001;
   private static final String LICENSE_FILE = "license.bea";
   private File beaHome;
   private File weblogicHome;
   private File configDir;
   private File outFile = null;
   private String configFile;
   private String serverName = "myserver";
   private String domainName;
   private boolean useBootProperties = false;
   private String managementUserName;
   private String managementPassword;
   private String pkPassword;
   private File securityPolicyFile;
   private String serverHost = "localhost";
   private String serverProtocol = "t3";
   private int serverPort = 7001;
   private boolean generateConfig = false;
   private boolean forceImplicitUpgrade = false;
   private String adminServerURL = null;
   private String action = "start";
   private long timeout = 0L;
   private double timeoutSecs = 0.0;
   private boolean productionModeEnabled = false;
   private boolean verbose = false;
   private boolean failOnError = false;
   private boolean execFailed = false;
   private boolean forceShutdown = false;
   private InitialContext ctx;
   private MBeanServerConnection connection;
   private ObjectName serverRuntimeObjectName;
   private String errorProperty;
   private boolean noExit = false;

   public void setNoExit(boolean var1) {
      this.noExit = var1;
   }

   public void setErrorProperty(String var1) {
      this.errorProperty = var1;
   }

   public void setPolicy(File var1) {
      this.securityPolicyFile = var1;
   }

   public void setDir(File var1) {
      this.configDir = var1;
      super.setDir(var1);
   }

   public void setOutput(File var1) {
      this.outFile = var1;
   }

   public void setConfigFile(String var1) {
      this.configFile = var1;
   }

   public void setBEAHome(File var1) {
      this.beaHome = var1;
   }

   public void setWebLogicHome(File var1) {
      this.weblogicHome = var1;
      if (this.beaHome == null) {
         File var2;
         for(var2 = var1; var2 != null && !(new File(var2, "license.bea")).exists(); var2 = var2.getParentFile()) {
         }

         if (var2 != null) {
            this.beaHome = var2;
         }
      }

   }

   public void setServerName(String var1) {
      this.serverName = var1;
   }

   public void setDomainName(String var1) {
      this.domainName = var1;
   }

   public void setAdminServerURL(String var1) {
      this.adminServerURL = var1;
   }

   public void setUseBootProperties(boolean var1) {
      this.useBootProperties = var1;
   }

   public void setUserName(String var1) {
      this.managementUserName = var1;
   }

   public void setPassword(String var1) {
      this.managementPassword = var1;
   }

   public void setPKPassword(String var1) {
      this.pkPassword = var1;
   }

   public void setTimeout(Long var1) {
      this.timeout = var1;
      this.timeoutSecs = var1.doubleValue();
      this.timeoutSecs /= 1000.0;
   }

   public void setTimeoutSeconds(Long var1) {
      this.timeout = var1 * 1000L;
      this.timeoutSecs = var1.doubleValue();
   }

   public void setProductionModeEnabled(boolean var1) {
      this.productionModeEnabled = var1;
   }

   public void setHost(String var1) {
      this.serverHost = var1;
   }

   public void setProtocol(String var1) {
      this.serverProtocol = var1;
   }

   public void setPort(int var1) throws BuildException {
      this.serverPort = var1;
   }

   public void setGenerateConfig(boolean var1) {
      this.generateConfig = var1;
   }

   public void setForceImplicitUpgrade(boolean var1) {
      this.forceImplicitUpgrade = var1;
   }

   public void setAction(String var1) {
      this.action = var1;
   }

   public void setVerbose(boolean var1) {
      this.verbose = var1;
   }

   public void setFailonerror(boolean var1) {
      this.failOnError = var1;
      super.setFailonerror(var1);
   }

   public void setForceShutdown(boolean var1) {
      this.forceShutdown = var1;
   }

   public void execute() throws BuildException {
      Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
      String var1;
      if (!this.action.equals("reboot") && !this.action.equals("shutdown") && !this.action.equals("connect") && !this.action.equals("start")) {
         var1 = "Invalid action specified: " + this.action;
         if (this.failOnError) {
            throw new BuildException(var1);
         }

         this.log(var1, 0);
      }

      if ((this.action.equals("reboot") || this.action.equals("shutdown")) && this.getInitialContext(1L) != null) {
         this.killServer();
      }

      if (this.action.equals("connect")) {
         if (this.getInitialContext(1L) != null) {
            return;
         }

         var1 = "Unable to connect to " + this.serverProtocol + "://" + this.serverHost + ":" + this.serverPort;
         if (this.failOnError) {
            throw new BuildException(var1);
         }

         if (this.errorProperty != null) {
            this.getProject().setProperty(this.errorProperty, var1);
         }
      }

      if (this.action.equals("start") || this.action.equals("reboot")) {
         if (this.noExit) {
            this.setSpawn(true);
         }

         if (this.weblogicHome == null) {
            this.setWebLogicHome(Home.getFile());
         }

         if (this.weblogicHome == null) {
            throw new BuildException("weblogichome not set");
         }

         if (this.beaHome != null && !this.beaHome.isDirectory()) {
            throw new BuildException("BEA home " + this.beaHome.getPath() + " not valid");
         }

         if (!this.generateConfig && this.adminServerURL == null) {
            var1 = this.configFile == null ? "config.xml" : this.configFile;
            File var2 = new File(this.configDir, var1);
            if (!var2.exists()) {
               var2 = new File(this.configDir, "config/" + var1);
               if (!var2.exists()) {
                  throw new BuildException("Server config file not found.");
               }
            }
         }

         if (this.securityPolicyFile == null) {
            this.securityPolicyFile = this.getSecurityPolicyFile();
         }

         Thread var4 = new Thread(this, "Execute-WLS");
         var4.setDaemon(true);
         var4.start();
         if (this.getInitialContext(this.timeout) != null) {
            this.getProject().addBuildListener(this);
            ArrayList var5 = new ArrayList();
            var5.add("RUNNING");
            if (!this.waitForServerState(var5, this.timeout)) {
               String var3 = this.execFailed ? "Error in server execution (" + this.serverName + ")" : "Server " + this.serverName + " not did not reach RUNNING state after " + this.timeoutSecs + " seconds";
               if (this.failOnError) {
                  throw new BuildException(var3);
               }

               this.log(var3, 0);
            }
         } else {
            String var6 = this.execFailed ? "Error in server execution (" + this.serverName + ")" : "Server " + this.serverName + " not listening after " + this.timeoutSecs + " seconds";
            if (this.failOnError) {
               throw new BuildException(var6);
            }

            this.log(var6, 0);
         }
      }

   }

   private InitialContext getInitialContext(long var1) {
      if (this.ctx != null) {
         return this.ctx;
      } else {
         String var3 = this.serverProtocol + "://" + this.serverHost + ":" + this.serverPort;
         Hashtable var4 = new Hashtable();
         var4.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
         var4.put("java.naming.provider.url", var3);
         if (this.managementUserName != null) {
            var4.put("java.naming.security.principal", this.managementUserName);
         }

         if (this.managementPassword != null) {
            var4.put("java.naming.security.credentials", this.managementPassword);
         }

         long var5 = System.currentTimeMillis() + var1;

         while(true) {
            try {
               this.ctx = new InitialContext(var4);
               return this.ctx;
            } catch (Exception var10) {
               if (!(var10 instanceof CommunicationException)) {
                  System.out.println("Unexpected Exception, retrying in 2 seconds");
                  var10.printStackTrace();
               }

               try {
                  Thread.sleep(2000L);
               } catch (InterruptedException var9) {
               }

               if (var1 != 0L && var5 <= System.currentTimeMillis() || this.execFailed) {
                  return null;
               }
            }
         }
      }
   }

   private void executeServer() {
      if (this.serverName != null) {
         String var1 = this.getClass().getName();
         String var2 = this.getClass().getPackage().getName();
         var1 = var1.substring(var2.length() + 1, var1.length());
         this.setTaskName(var1 + " " + this.serverName);
      }

      this.setFork(true);
      this.setClassname("weblogic.Server");
      this.setProperty("weblogic.Domain", this.domainName);
      if (this.configFile != null) {
         this.setProperty("weblogic.ConfigFile", this.configFile);
      }

      this.setProperty("weblogic.Name", this.serverName);
      if (this.beaHome != null) {
         this.setProperty("bea.home", this.beaHome);
      }

      this.setProperty("java.security.policy=", this.securityPolicyFile);
      if (!this.useBootProperties) {
         this.setProperty("weblogic.management.username", this.managementUserName);
         this.setProperty("weblogic.management.password", this.managementPassword);
      }

      this.setProperty("weblogic.pkpassword", this.pkPassword);
      if (this.generateConfig) {
         this.setProperty("weblogic.management.GenerateDefaultConfig", Boolean.toString(this.generateConfig));
      }

      this.setProperty("weblogic.management.server", this.adminServerURL);
      if (this.productionModeEnabled) {
         this.setProperty("weblogic.ProductionModeEnabled", Boolean.toString(this.productionModeEnabled));
      }

      if (this.forceImplicitUpgrade) {
         this.setProperty("weblogic.ForceImplicitUpgradeIfNeeded", Boolean.toString(this.forceImplicitUpgrade));
      }

      if (this.serverHost != "localhost") {
         this.setProperty("weblogic.ListenAddress", this.serverHost);
      }

      if (this.serverPort != 7001) {
         this.setProperty("weblogic.ListenPort", this.serverPort + "");
      }

      if (this.outFile != null) {
         if (this.noExit) {
            this.getProject().log("Ignoring output redirection as it is incompatibile with noExit");
         } else {
            super.setOutput(this.outFile);
         }
      }

      if (this.executeJava() != 0) {
         this.execFailed = true;
      }

   }

   private void setProperty(String var1, Object var2) {
      if (var2 != null) {
         this.createJvmarg().setValue("-D" + var1 + "=" + var2.toString());
      }

   }

   private void setProperty(String var1, boolean var2) {
      if (var2) {
         this.createJvmarg().setValue("-D" + var1);
      }

   }

   private File getSecurityPolicyFile() {
      File var1 = new File(this.weblogicHome, "lib/weblogic.policy");
      if (!var1.exists() && this.configDir != null) {
         var1 = new File(this.configDir, "weblogic.policy");
      }

      return !var1.exists() ? null : var1;
   }

   private void killServer() {
      if (this.getServerRuntimeName() != null) {
         try {
            if (this.forceShutdown) {
               this.connection.invoke(this.serverRuntimeObjectName, "forceShutdown", (Object[])null, (String[])null);
            } else {
               this.connection.invoke(this.serverRuntimeObjectName, "shutdown", (Object[])null, (String[])null);
            }

            ArrayList var1 = new ArrayList();
            var1.add("SHUTDOWN");
            var1.add("UNKNOWN");
            if (!this.waitForServerState(var1, this.timeout)) {
               String var2 = this.execFailed ? "Error in server execution (" + this.serverName + ")" : "Server " + this.serverName + " not complete force shutdown after " + this.timeoutSecs + " seconds.";
               if (this.failOnError) {
                  throw new BuildException(var2);
               }

               this.log(var2, 0);
            }
         } catch (SecurityException var3) {
            throw new BuildException(var3);
         } catch (Exception var4) {
            if (!(var4 instanceof CommunicationException) && !(var4 instanceof ConnectException) && !(var4 instanceof RemoteRuntimeException) && !(var4 instanceof PeerGoneException) && !(var4 instanceof SocketException)) {
               var4.printStackTrace();
            }
         }

         this.ctx = null;
      }

   }

   MBeanServerConnection getConnection() {
      if (this.connection != null) {
         return this.connection;
      } else if (this.getInitialContext(1L) == null) {
         return null;
      } else {
         try {
            this.connection = (MBeanServerConnection)this.ctx.lookup("weblogic.management.server");
         } catch (NamingException var2) {
            return null;
         }

         return this.connection;
      }
   }

   ObjectName getServerRuntimeName() {
      if (this.serverRuntimeObjectName != null) {
         return this.serverRuntimeObjectName;
      } else if (this.getConnection() == null) {
         return null;
      } else {
         try {
            ObjectName var1 = new ObjectName("*:Type=ServerRuntime,Name=" + this.serverName + ",*");
            if (this.verbose) {
               System.err.println("querying ");
            }

            Set var2 = this.connection.queryNames(var1, (QueryExp)null);
            if (this.verbose) {
               System.err.println("got a response of " + var2.size());
            }

            if (var2.size() != 1) {
               return null;
            }

            this.serverRuntimeObjectName = (ObjectName)var2.iterator().next();
            if (this.verbose) {
               System.err.println("got a response of " + this.serverRuntimeObjectName);
            }
         } catch (IOException var3) {
            this.connection = null;
         } catch (MalformedObjectNameException var4) {
            throw new BuildException(var4);
         }

         return this.serverRuntimeObjectName;
      }
   }

   private String getServerState() {
      if (this.getServerRuntimeName() == null) {
         return "unknown";
      } else if (this.getConnection() == null) {
         return "unknown";
      } else {
         try {
            return (String)this.connection.getAttribute(this.serverRuntimeObjectName, "State");
         } catch (MBeanException var2) {
            throw new BuildException(var2);
         } catch (AttributeNotFoundException var3) {
            throw new BuildException(var3);
         } catch (InstanceNotFoundException var4) {
            throw new BuildException(var4);
         } catch (ReflectionException var5) {
            throw new BuildException(var5);
         } catch (IOException var6) {
            return "unknown";
         }
      }
   }

   private boolean waitForServerState(List var1, long var2) {
      if (this.verbose) {
         System.err.println("waiting for " + this.serverName + "to transition to " + var1 + ", within " + var2 + " ms");
      }

      try {
         long var4 = 0L;
         String var6 = "<not-initialized>";
         String var7 = "";
         if (this.verbose) {
            System.err.println("querying " + this.serverName + " for state every 500ms, timeout " + var2 + " ms");
         }

         while(var4 < var2 || var2 == 0L) {
            var7 = this.getServerState();
            if (var1.contains(var7)) {
               break;
            }

            try {
               Thread.sleep(500L);
               var4 += 500L;
               if (!var6.equals(var7)) {
                  if (this.verbose) {
                     System.err.println("update: " + this.serverName + " state transition from " + var6 + " to " + var7 + ", after " + var4 + " ms");
                  }

                  var6 = var7;
               }
            } catch (InterruptedException var9) {
            }
         }

         if (var2 > 0L && var4 > var2) {
            if (this.verbose) {
               System.err.println("timeout: " + this.serverName + " failed to transition to state " + var1 + ", after " + var4 + " ms");
            }

            return false;
         } else {
            if (this.verbose) {
               System.err.println("ok: " + this.serverName + " transitioned to state " + var1 + ", after " + var4 + " ms");
            }

            return true;
         }
      } catch (Exception var10) {
         if (!(var10 instanceof CommunicationException) && !(var10 instanceof ConnectException) && !(var10 instanceof RemoteRuntimeException) && !(var10 instanceof SocketException) && !(var10 instanceof PeerGoneException)) {
            var10.printStackTrace();
            return false;
         } else {
            return true;
         }
      }
   }

   public void run() {
      this.executeServer();
   }

   public void buildStarted(BuildEvent var1) {
   }

   public void buildFinished(BuildEvent var1) {
      if (this.noExit) {
         this.log("Server will not be killed due to noExit flag" + this.serverName);
      } else {
         try {
            this.log("Killing WLS Server Instance " + this.serverName);
            this.killServer();
         } catch (Exception var3) {
            this.log("Exception occurred while shutting down the server.");
            var3.printStackTrace();
         }

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
