package weblogic.nodemanager.server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import weblogic.nodemanager.common.StartupConfig;
import weblogic.nodemanager.system.NodeManagerSystem;
import weblogic.nodemanager.util.Platform;
import weblogic.nodemanager.util.ProcessControl;

public class WLSProcessBuilder {
   private ServerManagerI serverMgr;
   private StartupConfig conf;
   private String[] cmdLine;
   private Map<String, String> environment;
   private File directory;
   private File outFile;
   private ProcessControl processCtrl;
   private boolean stopCommand;
   public static final String CLASSPATH_ENV = "CLASSPATH";
   public static final String ADMIN_URL_ENV = "ADMIN_URL";
   public static final String JAVA_VENDOR_ENV = "JAVA_VENDOR";
   public static final String JAVA_HOME_ENV = "JAVA_HOME";
   public static final String JAVA_OPTIONS_ENV = "JAVA_OPTIONS";
   public static final String SERVER_NAME_ENV = "SERVER_NAME";
   public static final String SERVER_IP_ENV = "SERVER_IP";
   public static final String SECURITY_POLICY_ENV = "SECURITY_POLICY";
   private static final String PATH_ENV = "PATH";
   private static final String LANG_ENV = "LANG";
   private static final String WL_HOME_ENV = "WL_HOME";
   private static final String BEA_HOME_ENV = "BEA_HOME";
   public static final String SERVICE_ENABLED_PROP = "weblogic.nodemanager.ServiceEnabled";

   public WLSProcessBuilder(String[] var1, Map var2, File var3, File var4) {
      this.stopCommand = false;
      this.cmdLine = var1;
      this.environment = var2;
      this.directory = var3;
      this.outFile = var4;
   }

   public WLSProcessBuilder(ServerManagerI var1, StartupConfig var2) {
      this(var1, var2, false);
   }

   public WLSProcessBuilder(ServerManagerI var1, StartupConfig var2, boolean var3) {
      this.stopCommand = false;
      this.serverMgr = var1;
      this.conf = var2;
      this.stopCommand = var3;
      DomainManager var4 = var1.getDomainManager();
      NMServerConfig var5 = var4.getNMServer().getConfig();
      if (!var3 && !var5.isStartScriptEnabled()) {
         this.cmdLine = this.getJavaCommandLine();
      } else {
         this.cmdLine = this.getScriptCommandLine();
         this.environment = this.getScriptEnvironment();
      }

      this.directory = var4.getDomainDir();
      this.outFile = this.serverMgr.getServerDir().getOutFile();
      this.processCtrl = var5.getProcessControl();
   }

   public WLSProcess createProcess() throws IOException {
      Object var1 = null;
      if (this.processCtrl != null) {
         File var2 = new File(this.cmdLine[0]);
         if (!var2.exists() && var2.getName().lastIndexOf(46) < 0 && Platform.isWindows()) {
            var2 = new File(this.cmdLine[0] + ".exe");
         }

         if (!var2.exists()) {
            throw new IOException("Executable " + this.cmdLine[0] + " does not exist");
         }

         var1 = new WLSProcessNativeImpl(this.processCtrl, this.cmdLine, this.environment, this.directory, this.outFile);
      } else {
         var1 = new WLSProcessImpl(this.cmdLine, this.environment, this.directory, this.outFile);
      }

      return (WLSProcess)var1;
   }

   public WLSProcess createProcess(String var1) throws IOException {
      if (this.processCtrl != null) {
         return new WLSProcessNativeImpl(this.processCtrl, var1);
      } else {
         throw new IllegalStateException("Native process control unavailable");
      }
   }

   public String[] getCommandLine() {
      String[] var1 = new String[this.cmdLine.length];
      System.arraycopy(this.cmdLine, 0, var1, 0, this.cmdLine.length);
      return var1;
   }

   public Map<String, String> getEnvironment() {
      return this.environment;
   }

   public File getDirectory() {
      return this.directory;
   }

   public File getOutFile() {
      return this.outFile;
   }

   public ProcessControl getProcessControl() {
      return this.processCtrl;
   }

   public boolean isNative() {
      return this.processCtrl != null;
   }

   String[] getJavaCommandLine() {
      return this.getJavaCommandLine(this.serverMgr, this.conf);
   }

   String[] getJavaCommandLine(ServerManagerI var1, StartupConfig var2) {
      ArrayList var3 = new ArrayList();
      String var4;
      if ((var4 = var2.getJavaHome()) == null) {
         var4 = System.getProperty("java.home");
      }

      var3.add(var4 + File.separator + "bin" + File.separator + "java");
      var3.add("-Dweblogic.Name=" + var1.getServerName());
      if ((var4 = var2.getBeaHome()) == null) {
         var4 = System.getProperty("bea.home");
      }

      if (var4 != null) {
         var3.add("-Dbea.home=" + var4);
      }

      if ((var4 = var2.getSecurityPolicyFile()) == null) {
         var4 = System.getProperty("java.security.policy");
      }

      var3.add("-Djava.security.policy=" + var4);
      if ((var4 = var2.getAdminURL()) != null) {
         var3.add("-Dweblogic.management.server=" + var4);
      }

      if ((var4 = System.getProperty("java.library.path")) != null) {
         var3.add("-Djava.library.path=" + Platform.preparePathForCommand(var4));
      }

      String var5 = System.getProperty("java.class.path");
      if ((var4 = var2.getClassPath()) == null) {
         var4 = var5;
      } else {
         var4 = Platform.parseClassPath(var4, var5);
      }

      var3.add("-Djava.class.path=" + Platform.preparePathForCommand(var4));
      var3.addAll(this.getJavaOptions());
      if ((var4 = var2.getSSLArguments()) != null) {
         var3.addAll(this.toOptionsList(var4));
      }

      if ((var4 = var2.getArguments()) != null) {
         var3.addAll(this.toOptionsList(var4));
      }

      var3.add("weblogic.Server");
      return (String[])((String[])var3.toArray(new String[var3.size()]));
   }

   String[] getScriptCommandLine() {
      ArrayList var1 = new ArrayList();
      DomainManager var2 = this.serverMgr.getDomainManager();
      DomainDir var3 = var2.getDomainDir();
      String var4;
      if (this.stopCommand) {
         var4 = var2.getNMServer().getConfig().getStopScriptName();
      } else {
         var4 = var2.getNMServer().getConfig().getStartScriptName();
      }

      File var5 = new File(var4);
      if (!var5.isAbsolute()) {
         var5 = new File(new File(var3, "bin"), var4);
      }

      var1.add(var5.getPath());
      return (String[])((String[])var1.toArray(new String[var1.size()]));
   }

   Map<String, String> getScriptEnvironment() {
      HashMap var1 = new HashMap();
      var1.putAll(System.getenv());
      var1.put("SERVER_NAME", this.serverMgr.getServerName());
      String var2;
      if ((var2 = this.conf.getJavaVendor()) != null) {
         var1.put("JAVA_VENDOR", var2);
      }

      if ((var2 = this.conf.getJavaHome()) != null) {
         var1.put("JAVA_HOME", var2);
      }

      String var3 = this.toOptionsString(this.getJavaOptions());
      if ((var2 = this.conf.getSSLArguments()) != null) {
         var3 = var3 + ' ' + var2;
      }

      if ((var2 = this.conf.getArguments()) != null) {
         var3 = var3 + ' ' + var2;
      }

      String var4 = this.conf.getTransientScriptEnv();
      if (var4 != null) {
         var1.putAll(this.getTransientScriptMap(var4));
      }

      if ((var2 = this.conf.getSecurityPolicyFile()) != null) {
         var1.put("SECURITY_POLICY", var2);
      }

      var1.put("JAVA_OPTIONS", var3);
      if ((var2 = this.conf.getClassPath()) != null) {
         var1.put("CLASSPATH", var2);
      }

      if ((var2 = this.conf.getAdminURL()) != null) {
         var1.put("ADMIN_URL", var2);
      }

      return var1;
   }

   private Map<String, String> getTransientScriptMap(String var1) {
      HashMap var2 = new HashMap();
      StringTokenizer var3 = new StringTokenizer(var1, ",");

      while(var3.hasMoreTokens()) {
         String var4 = var3.nextToken();
         int var5 = var4.indexOf("=");
         if (var5 >= 1 && var5 <= var4.length() - 1) {
            String var6 = var4.substring(0, var4.indexOf("="));
            String var7 = var4.substring(var4.indexOf("=") + 1, var4.length());
            if (var6 != null && var6.length() != 0 && var7 != null && var7.length() != 0) {
               var2.put(var6, var7);
               continue;
            }

            throw new AssertionError("Missing either a name or a value for: " + var6 + " : " + var7);
         }

         throw new AssertionError("This property is not formed correctly and will be ignored: " + var4);
      }

      return var2;
   }

   private static Map inheritedEnv() {
      HashMap var0 = new HashMap();
      if (Platform.isUnix()) {
         String var1 = System.getenv("PATH");
         if (var1 != null && var1.length() > 0) {
            String[] var2 = new String[]{System.getenv("WL_HOME"), System.getenv("BEA_HOME"), System.getenv("JAVA_HOME")};
            String var3 = String.valueOf(File.pathSeparatorChar);
            StringTokenizer var4 = new StringTokenizer(var1, var3);
            StringBuilder var5 = new StringBuilder();

            while(true) {
               label46:
               while(var4.hasMoreTokens()) {
                  String var6 = var4.nextToken();

                  for(int var7 = 0; var7 < var2.length; ++var7) {
                     if (var2[var7] != null && var2[var7].length() > 0 && var6.startsWith(var2[var7])) {
                        continue label46;
                     }
                  }

                  if (var5.length() > 0) {
                     var5.append(var3);
                  }

                  var5.append(var6.trim());
               }

               if (var5.length() > 0) {
                  var0.put("PATH", var5.toString());
               }
               break;
            }
         }

         String var8 = System.getenv("LANG");
         if (var8 != null && var8.length() > 0) {
            var0.put("LANG", var8);
         }
      }

      return var0;
   }

   private List<String> getJavaOptions() {
      ArrayList var1 = new ArrayList();
      ServerDir var2 = this.serverMgr.getServerDir();
      File var3 = var2.getNMBootIdentityFile();
      if (!var3.exists()) {
         var3 = var2.getBootIdentityFile();
      }

      if (var3.exists()) {
         var1.add("-Dweblogic.system.BootIdentityFile=" + var3);
      }

      var1.addAll(NodeManagerSystem.getInstance().getAdditionalProcessArgs());
      return var1;
   }

   private String toOptionsString(List var1) {
      StringBuffer var2 = new StringBuffer();

      String var4;
      for(Iterator var3 = var1.iterator(); var3.hasNext(); var2.append(' ').append(var4)) {
         var4 = (String)var3.next();
         if (var4.startsWith("-D")) {
            int var5 = var4.indexOf(61);
            if (var5 != -1) {
               String var6 = var4.substring(0, var5);
               String var7 = var4.substring(var5);
               if (var7.indexOf(32) != -1 || var7.indexOf(9) != -1) {
                  var4 = var6 + "=\"" + var7 + '"';
               }
            }
         }
      }

      return var2.substring(1);
   }

   protected List<String> toOptionsList(String var1) {
      List var2 = Arrays.asList(var1.trim().split("\\s"));
      ArrayList var3 = new ArrayList(var2.size());

      for(int var4 = 0; var4 < var2.size(); ++var4) {
         String var5 = (String)var2.get(var4);
         if (var5.trim().length() != 0) {
            var3.add(var5);
         }
      }

      return var3;
   }
}
