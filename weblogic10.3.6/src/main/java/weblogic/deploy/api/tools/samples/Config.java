package weblogic.deploy.api.tools.samples;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import javax.enterprise.deploy.shared.ModuleType;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.deploy.api.spi.DeploymentOptions;
import weblogic.utils.Getopt2;

class Config {
   static final String HOST = "host";
   static final String PORT = "port";
   static final String USER = "user";
   static final String PASSWORD = "password";
   static final String VERBOSE = "verbose";
   static final String DEBUG = "debug";
   static final String QUIET = "quiet";
   static final String USEPLAN = "useplan";
   static final String SAVEPLAN = "saveplan";
   static final String TARGETS = "targets";
   static final String PLUGIN = "plugin";
   static final String CONFIG = "config";
   static final String TYPE = "type";
   static final String REMOTE = "remote";
   static final String NAME = "name";
   static final String STAGE = "stage";
   static final String MODULES = "modules";
   static final String START = "start";
   static final String DEPLOY = "deploy";
   static final String STOP = "stop";
   static final String DISTRIBUTE = "distribute";
   static final String UNDEPLOY = "undeploy";
   static final String REDEPLOY = "redeploy";
   static final String UPDATE = "update";
   static final String LISTALL = "listall";
   static final String LISTSTARTED = "liststarted";
   static final String LISTSTOPPED = "liststopped";
   static final String LISTTARGETS = "listtargets";
   static final String LISTTYPES = "listtypes";
   static final String LISTCOMMANDS = "listcommands";
   static final String FACTORY = "J2EE-DeploymentFactory-Implementation-Class";
   static final String DEFAULT_CONFIG = ".config";
   static final String DEFAULT_FACTORY = "weblogic.deploy.api.spi.factories.internal.DeploymentFactoryImpl";
   String adminURL;
   String host = "localhost";
   String port = "8888";
   String user = "system";
   String password = "gumby1234";
   boolean verbose = false;
   boolean debug = false;
   boolean quiet = false;
   String usePlan = null;
   String savePlan = null;
   Set targets = new HashSet();
   String[] targetArray;
   String targs;
   String mods;
   String plugin = null;
   String config = ".config";
   String name = null;
   String[] arglist;
   Getopt2 opts;
   J2EEDeployerToolTextFormatter cat;
   String command = null;
   String fileOrApp = null;
   Set modules = new HashSet();
   String factoryClassName;
   ModuleType moduleType = null;
   boolean remote = false;
   String appName = null;
   String stage;
   Set delta;

   Config(String[] var1, String var2) throws IllegalArgumentException {
      this.stage = DeploymentOptions.STAGE_DEFAULT;
      this.delta = new HashSet();
      this.name = var2;
      this.arglist = var1;
      this.opts = new Getopt2();
      this.cat = J2EEDeployerToolTextFormatter.getInstance();
      this.opts.addOption("host", this.host, this.cat.hostOption());
      this.opts.addOption("port", this.port, this.cat.portOption());
      this.opts.addOption("user", "admin", this.cat.userOption());
      this.opts.addOption("password", "?????", this.cat.passwordOption());
      this.opts.addFlag("verbose", this.cat.verboseOption());
      this.opts.addFlag("debug", this.cat.debugOption());
      this.opts.addFlag("quiet", this.cat.quietOption());
      this.opts.addOption("useplan", "/plans/myapp.plan", this.cat.useplanOption());
      this.opts.addOption("saveplan", "/plans/myapp.plan", this.cat.saveplanOption());
      this.opts.addOption("targets", "server1,server2", this.cat.targetsOption());
      this.opts.addOption("plugin", "wldeploy.jar", this.cat.pluginOption());
      this.opts.addOption("config", "./.config", this.cat.configOption());
      this.opts.addOption("type", "EAR", this.cat.typeOption());
      this.opts.addOption("name", "myapp", this.cat.nameOption());
      this.opts.addOption("modules", "mod1,mod2", this.cat.modulesOption());
      this.opts.addOption("stage", "stage", this.cat.stageOption());
      this.opts.addFlag("remote", this.cat.remoteOption());
      this.opts.addFlag("start", this.cat.startCommand());
      this.opts.addFlag("deploy", this.cat.deployCommand());
      this.opts.addFlag("stop", this.cat.stopCommand());
      this.opts.addFlag("distribute", this.cat.distributeCommand());
      this.opts.addFlag("undeploy", this.cat.undeployCommand());
      this.opts.addFlag("redeploy", this.cat.redeployCommand());
      this.opts.addFlag("update", this.cat.updateCommand());
      this.opts.addFlag("listall", this.cat.listallCommand());
      this.opts.addFlag("liststarted", this.cat.liststartedCommand());
      this.opts.addFlag("liststopped", this.cat.liststoppedCommand());
      this.opts.addFlag("listtargets", this.cat.listtargetsCommand());
      this.opts.addFlag("listtypes", this.cat.listtypesCommand());
      this.opts.addFlag("listcommands", this.cat.listcommandsCommand());
      this.opts.setUsageArgs(this.cat.fileOrAppNameOption());
      this.opts.grok(this.arglist);
      if (var1.length == 0) {
         this.usage();
         System.exit(0);
      }

      if (this.opts.hasOption("config")) {
         this.config = this.opts.getOption("config");
      }

      this.initFromConfig();
      if (this.opts.hasOption("verbose")) {
         this.setVerbose(true);
      }

      if (this.opts.hasOption("quiet")) {
         this.setQuiet(true);
      }

      if (this.opts.hasOption("debug")) {
         this.setDebug(true);
      }

      if (this.opts.hasOption("remote")) {
         this.remote = true;
      }

      if (this.opts.hasOption("host")) {
         this.host = this.opts.getOption("host");
      }

      if (this.opts.hasOption("port")) {
         this.port = this.opts.getOption("port");
      }

      if (this.opts.hasOption("user")) {
         this.user = this.opts.getOption("user");
      }

      if (this.opts.hasOption("password")) {
         this.password = this.opts.getOption("password");
      }

      if (this.opts.hasOption("useplan")) {
         this.usePlan = this.opts.getOption("useplan");
      }

      if (this.opts.hasOption("saveplan")) {
         this.savePlan = this.opts.getOption("saveplan");
      }

      StringTokenizer var3;
      if (this.opts.hasOption("targets")) {
         this.targets = new HashSet();
         this.targs = this.trim(this.opts.getOption("targets"));
         var3 = new StringTokenizer(this.targs, ",");

         while(var3.hasMoreTokens()) {
            this.targets.add(var3.nextToken());
         }
      }

      if (this.opts.hasOption("plugin")) {
         this.plugin = this.opts.getOption("plugin");
      }

      if (this.opts.hasOption("type")) {
         this.setModuleType(this.opts.getOption("type"));
      }

      if (this.opts.hasOption("name")) {
         this.setAppName(this.opts.getOption("name"));
      }

      if (this.opts.hasOption("modules")) {
         this.mods = this.trim(this.opts.getOption("modules"));
         var3 = new StringTokenizer(this.mods, ",");

         while(var3.hasMoreTokens()) {
            this.modules.add(var3.nextToken());
         }
      }

      if (this.opts.hasOption("start")) {
         this.command = "start";
      }

      if (this.opts.hasOption("deploy")) {
         this.command = "deploy";
      }

      if (this.opts.hasOption("stop")) {
         this.command = "stop";
      }

      if (this.opts.hasOption("distribute")) {
         this.command = "distribute";
      }

      if (this.opts.hasOption("undeploy")) {
         this.command = "undeploy";
      }

      if (this.opts.hasOption("redeploy")) {
         this.command = "redeploy";
      }

      if (this.opts.hasOption("update")) {
         this.command = "update";
      }

      if (this.opts.hasOption("listall")) {
         this.command = "listall";
      }

      if (this.opts.hasOption("liststarted")) {
         this.command = "liststarted";
      }

      if (this.opts.hasOption("liststopped")) {
         this.command = "liststopped";
      }

      if (this.opts.hasOption("listtargets")) {
         this.command = "listtargets";
      }

      if (this.opts.hasOption("listtypes")) {
         this.command = "listtypes";
      }

      if (this.opts.hasOption("listcommands")) {
         this.command = "listcommands";
      }

      if (this.opts.hasOption("stage")) {
         this.setStage();
      }

      if (this.opts.args().length > 0) {
         this.fileOrApp = this.opts.args()[0];

         for(int var4 = 1; var4 < this.opts.args().length; ++var4) {
            this.delta.add(this.opts.args()[var4]);
         }
      }

      this.dumpArgs();
      this.factoryClassName = this.getFactoryName();
      this.validateArgs();
      this.targetArray = (String[])((String[])this.targets.toArray(new String[0]));
   }

   private void setStage() {
      String var1 = this.opts.getOption("stage");
      if ("stage".equals(var1)) {
         this.stage = "stage";
      } else if ("nostage".equals(var1)) {
         this.stage = "nostage";
      } else if ("external".equals(var1)) {
         this.stage = "external_stage";
      }

   }

   private void setAppName(String var1) {
      this.appName = var1;
   }

   private void setModuleType(String var1) throws IllegalArgumentException {
      if (var1.equalsIgnoreCase("EAR")) {
         this.moduleType = ModuleType.EAR;
      } else if (var1.equalsIgnoreCase("EJB")) {
         this.moduleType = ModuleType.EJB;
      } else if (var1.equalsIgnoreCase("CAR")) {
         this.moduleType = ModuleType.CAR;
      } else if (var1.equalsIgnoreCase("RAR")) {
         this.moduleType = ModuleType.RAR;
      } else if (var1.equalsIgnoreCase("WAR")) {
         this.moduleType = ModuleType.WAR;
      } else if (var1.equalsIgnoreCase("JMS")) {
         this.moduleType = WebLogicModuleType.JMS;
      } else if (var1.equalsIgnoreCase("JDBC")) {
         this.moduleType = WebLogicModuleType.JDBC;
      } else if (var1.equalsIgnoreCase("intercept")) {
         this.moduleType = WebLogicModuleType.INTERCEPT;
      } else if (var1.equalsIgnoreCase("config")) {
         this.moduleType = WebLogicModuleType.CONFIG;
      } else if (var1.equalsIgnoreCase("submodule")) {
         this.moduleType = WebLogicModuleType.SUBMODULE;
      } else {
         if (!var1.equalsIgnoreCase("wldf")) {
            throw new IllegalArgumentException(this.cat.badType(var1));
         }

         this.moduleType = WebLogicModuleType.WLDF;
      }

   }

   void setVerbose(boolean var1) {
      this.verbose = var1;
      if (this.verbose) {
         this.quiet = false;
      }

   }

   void setQuiet(boolean var1) {
      this.quiet = var1;
      if (this.quiet) {
         this.verbose = false;
      }

   }

   void setDebug(boolean var1) {
      this.debug = var1;
      if (this.debug) {
         this.setVerbose(true);
      }

      String var2 = this.debug ? "all:internal" : "";
      System.setProperty("weblogic.deployer.debug", var2);
   }

   private void usage() {
      this.opts.usageError(this.name);
   }

   private String getFactoryName() throws IllegalArgumentException {
      String var1 = null;
      if (this.plugin != null) {
         try {
            JarFile var2 = new JarFile(this.plugin);
            Manifest var3 = var2.getManifest();
            if (var3 != null) {
               Attributes var4 = var3.getMainAttributes();
               var1 = var4.getValue("J2EE-DeploymentFactory-Implementation-Class");
               if (this.debug) {
                  this.print("Getting factory from plugin");
               }

               this.inform(this.cat.factoryClassName(var1));
            }
         } catch (IOException var5) {
            throw new IllegalArgumentException(this.cat.badArchive(this.plugin, var5.toString()));
         }
      } else {
         var1 = "weblogic.deploy.api.spi.factories.internal.DeploymentFactoryImpl";
         if (this.debug) {
            this.print("Getting factory from defaults");
         }

         this.inform(this.cat.factoryClassName(var1));
      }

      return var1;
   }

   private void initFromConfig() throws IllegalArgumentException {
      File var1 = new File(".config");
      File var2 = new File(this.config);
      boolean var3 = var1.equals(var2);

      try {
         FileInputStream var5 = new FileInputStream(new File(this.config));
         Properties var6 = new Properties();
         var6.load(var5);
         String var4 = var6.getProperty("port");
         if (var4 != null) {
            this.port = var4;
         }

         var4 = var6.getProperty("host");
         if (var4 != null) {
            this.host = var4;
         }

         var4 = var6.getProperty("user");
         if (var4 != null) {
            this.user = var4;
         }

         var4 = var6.getProperty("password");
         if (var4 != null) {
            this.password = var4;
         }

         var4 = var6.getProperty("verbose");
         if (var4 != null) {
            this.setVerbose(Boolean.valueOf(var4));
         }

         var4 = var6.getProperty("remote");
         if (var4 != null) {
            this.remote = Boolean.valueOf(var4);
         }

         var4 = var6.getProperty("debug");
         if (var4 != null) {
            this.setDebug(Boolean.valueOf(var4));
         }

         var4 = var6.getProperty("quiet");
         if (var4 != null) {
            this.setQuiet(Boolean.valueOf(var4));
         }

         var4 = var6.getProperty("useplan");
         if (var4 != null) {
            this.usePlan = var4;
         }

         var4 = var6.getProperty("saveplan");
         if (var4 != null) {
            this.savePlan = var4;
         }

         var4 = var6.getProperty("targets");
         if (var4 != null) {
            this.targets = new HashSet();
            this.targs = this.trim(var4);
            StringTokenizer var7 = new StringTokenizer(this.targs, ",");

            while(var7.hasMoreTokens()) {
               this.targets.add(var7.nextToken());
            }
         }

         var4 = var6.getProperty("plugin");
         if (var4 != null) {
            this.plugin = var4;
         }

         var4 = var6.getProperty("type");
         if (var4 != null) {
            this.setModuleType(var4);
         }
      } catch (FileNotFoundException var8) {
         if (!var3) {
            throw new IllegalArgumentException(this.cat.noConfig(this.config));
         }
      } catch (IOException var9) {
         throw new IllegalArgumentException(this.cat.badConfig(this.config, var9.toString()));
      }

   }

   private String trim(String var1) {
      StringBuffer var2 = new StringBuffer();
      if (var1 != null) {
         char[] var3 = var1.toCharArray();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            char var5 = var3[var4];
            if (!Character.isWhitespace(var5)) {
               var2.append(var5);
            }
         }
      }

      return var2.toString();
   }

   private void validateArgs() throws IllegalArgumentException {
      String var1 = null;
      if (this.host == null || this.host.length() == 0) {
         var1 = this.cat.badHost();
         this.error(var1);
      }

      if (this.port == null || this.port.length() == 0) {
         var1 = this.cat.badPort();
         this.error(var1);
      }

      if (this.user == null || this.user.length() == 0) {
         var1 = this.cat.badUser();
         this.error(var1);
      }

      File var2;
      if (this.usePlan != null && this.usePlan.length() > 0) {
         var2 = new File(this.usePlan);
         if (!var2.exists() || var2.isDirectory() || !var2.canRead()) {
            var1 = this.cat.badUsePlan(this.usePlan);
            this.error(var1);
         }
      }

      if (this.savePlan != null && this.savePlan.length() > 0) {
         var2 = new File(this.savePlan);
         if (!var2.getParentFile().exists() || var2.isDirectory() || var2.exists() && !var2.canWrite()) {
            var1 = this.cat.badSavePlan(this.savePlan);
            this.error(var1);
         }
      }

      if (this.command == null) {
         var1 = this.cat.noCommand();
         this.error(var1);
      } else {
         if (!this.command.startsWith("list") && this.targets.isEmpty()) {
            var1 = this.cat.noTargets(this.command);
            this.error(var1);
         }

         if ((this.fileOrApp == null || this.fileOrApp.length() == 0) && ("start".equalsIgnoreCase(this.command) || "stop".equalsIgnoreCase(this.command) || "deploy".equalsIgnoreCase(this.command) || "distribute".equalsIgnoreCase(this.command) || "redeploy".equalsIgnoreCase(this.command) || "update".equalsIgnoreCase(this.command) || "undeploy".equalsIgnoreCase(this.command))) {
            var1 = this.cat.missingFileOrApp(this.command);
            this.error(var1);
         }

         if ((this.usePlan == null || this.usePlan.length() == 0) && "update".equalsIgnoreCase(this.command)) {
            var1 = this.cat.missingPlan();
            this.error(var1);
         }

         if (this.factoryClassName == null || this.factoryClassName.length() == 0) {
            var1 = this.cat.noFactory();
            this.error(var1);
         }

         if (this.moduleType == null && this.command.startsWith("LIST") && (!this.command.equals("listtargets") || !this.command.equals("listtypes") || !this.command.equals("listcommands"))) {
            var1 = this.cat.noType();
            this.error(var1);
         }

         if (this.moduleType == null && !this.command.startsWith("LIST")) {
            var1 = this.cat.noType();
            this.error(var1);
         }

         if (this.appName == null && "redeploy".equalsIgnoreCase(this.command)) {
            var1 = this.cat.noName();
            this.error(var1);
         }
      }

      for(int var3 = 0; var3 < this.opts.args().length; ++var3) {
         if (this.opts.args()[var3].startsWith("-")) {
            var1 = this.cat.unrecognizedArg(this.opts.args()[var3]);
            this.error(var1);
         }
      }

      if (var1 != null) {
         this.usage();
         throw new IllegalArgumentException(var1);
      }
   }

   void dumpArgs() {
      this.inform(this.cat.nameValue("host", this.host));
      this.inform(this.cat.nameValue("port", this.port));
      this.inform(this.cat.nameValue("user", this.user));
      this.inform(this.cat.nameValue("verbose", Boolean.toString(this.verbose)));
      this.inform(this.cat.nameValue("debug", Boolean.toString(this.debug)));
      this.inform(this.cat.nameValue("quiet", Boolean.toString(this.quiet)));
      this.inform(this.cat.nameValue("remote", Boolean.toString(this.remote)));
      this.inform(this.cat.nameValue("useplan", this.usePlan));
      this.inform(this.cat.nameValue("saveplan", this.savePlan));
      this.inform(this.cat.nameValue("targets", this.targs));
      this.inform(this.cat.nameValue("plugin", this.plugin));
      this.inform(this.cat.nameValue("config", this.config));
      String var1 = "";
      if (this.moduleType != null) {
         var1 = this.moduleType.toString();
      }

      this.inform(this.cat.nameValue("type", var1));
   }

   void print(String var1) {
      if (!this.quiet) {
         System.out.println(var1);
      }

   }

   void inform(String var1) {
      if (this.verbose) {
         System.out.println(var1);
      }

   }

   String error(String var1) {
      System.err.println(var1);
      return var1;
   }
}
