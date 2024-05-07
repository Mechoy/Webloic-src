package weblogic.deploy.api.tools.deployer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.StringTokenizer;
import weblogic.deploy.internal.DeployerTextFormatter;
import weblogic.utils.Getopt2;

public class Options implements Serializable {
   private static final long serialVersionUID = 1L;
   private transient Getopt2 opts;
   private static transient DeployerTextFormatter cat;
   public static final String OPTION_CANCEL = "cancel";
   public static final String OPTION_LIST = "list";
   public static final String OPTION_DEPLOY = "deploy";
   public static final String OPTION_UNDEPLOY = "undeploy";
   public static final String OPTION_DISTRIBUTE = "distribute";
   public static final String OPTION_START = "start";
   public static final String OPTION_STOP = "stop";
   public static final String OPTION_REDEPLOY = "redeploy";
   public static final String OPTION_LIST_APP = "listapps";
   public static final String OPTION_LIST_TASK = "listtask";
   public static final String OPTION_PURGE_TASKS = "purgetasks";
   public static final String OPTION_UPDATE = "update";
   public static final String OPTION_ACTIVATE = "activate";
   public static final String OPTION_DEACTIVATE = "deactivate";
   public static final String OPTION_UNPREPARE = "unprepare";
   public static final String OPTION_REMOVE = "remove";
   public static final String OPTION_VERBOSE = "verbose";
   public static final String OPTION_DEBUG = "debug";
   public static final String OPTION_EXAMPLES = "examples";
   public static final String OPTION_NOWAIT = "nowait";
   public static final String OPTION_NOSTAGE = "nostage";
   public static final String OPTION_STAGE = "stage";
   public static final String OPTION_EXTERNAL_STAGE = "external_stage";
   public static final String OPTION_UPLOAD = "upload";
   public static final String OPTION_DELETE_FILES = "delete_files";
   public static final String OPTION_REMOTE = "remote";
   public static final String OPTION_ADMIN_MODE = "adminmode";
   public static final String OPTION_GRACEFUL = "graceful";
   public static final String OPTION_IGNORE_SESSIONS = "ignoresessions";
   public static final String OPTION_RMI_GRACE_PERIOD = "rmigraceperiod";
   public static final String OPTION_ALL_VERSIONS = "allversions";
   public static final String OPTION_SECURITY_VALIDATE = "enableSecurityValidation";
   public static final String OPTION_LIB_MODULE = "library";
   public static final String OPTION_NOEXIT = "noexit";
   public static final String OPTION_NO_VERSION = "noversion";
   public static final String OPTION_DEFAULT_SUBMODULE_TARGETS = "defaultsubmoduletargets";
   public static final String OPTION_NO_DEFAULT_SUBMODULE_TARGETS = "nodefaultsubmoduletargets";
   public static final String OPTION_USE_NONEXCLUSIVE_LOCK = "usenonexclusivelock";
   public static final String OPTION_SOURCE = "source";
   public static final String OPTION_NAME = "name";
   public static final String OPTION_APP_VERSION = "appversion";
   public static final String OPTION_PLAN_VERSION = "planversion";
   public static final String OPTION_RETIRE_TIMEOUT = "retiretimeout";
   public static final String OPTION_TARGETS = "targets";
   public static final String OPTION_SUBMODULE_TARGETS = "submoduletargets";
   public static final String OPTION_SECURITY_MODEL = "securityModel";
   public static final String OPTION_LIB_SPEC_VERSION = "libspecver";
   public static final String OPTION_LIB_IMPL_VERSION = "libimplver";
   public static final String OPTION_ID = "id";
   public static final String OPTION_TIMEOUT = "timeout";
   public static final String OPTION_PLAN = "plan";
   public static final String OPTION_DEPLOYMENT_ORDER = "deploymentorder";
   public static final String OPTION_ALT_APP_DD = "altappdd";
   public static final String OPTION_ALT_WLS_DD = "altwlsappdd";
   public static final String OPTION_OUTPUT = "output";
   public static final String RAW_OUTPUT = "raw";
   public static final String FORMATTED_OUTPUT = "formatted";
   public static final String OPTION_SRCROOT = "sourcerootforupload";
   static final transient HashSet allOptions = new HashSet();
   boolean verbose;
   boolean debug;
   boolean examples;
   boolean nowait;
   String stageMode = null;
   boolean upload;
   boolean deleteFiles;
   boolean remote;
   boolean adminMode;
   boolean graceful;
   boolean ignoreSessions;
   int rmiGracePeriod;
   boolean allVersions;
   boolean securityValidation;
   boolean libModule;
   String source;
   String name;
   String appVersion;
   String planVersion;
   boolean noVersion;
   int retireTimeout = -1;
   String securityModel;
   String libSpecVersion;
   String libImplVersion;
   String id;
   long timeout = 0L;
   int deploymentOrder = 100;
   String altAppDD;
   String altWlsAppDD;
   boolean sourceFromOpts = false;
   boolean removeOp;
   boolean cancelOp;
   boolean listOp;
   boolean deployOp;
   boolean undeployOp;
   boolean distributeOp;
   boolean startOp;
   boolean stopOp;
   boolean redeployOp;
   boolean listappOp;
   boolean listtaskOp;
   boolean purgetasksOp;
   boolean updateOp;
   boolean activateOp;
   boolean deactivateOp;
   boolean unprepareOp;
   String[] moduleTargets;
   String[] submoduleTargets;
   String[] delta;
   boolean sourceFromArgs = false;
   boolean formatted;
   boolean noexit;
   String plan;
   boolean isDefaultSubmoduleTargets;
   boolean useNonExclusiveLock = false;
   static final long MAX_NOTIFICATION_WAIT = 1000L;

   Options(Getopt2 var1) throws IllegalArgumentException {
      this.opts = var1;
      this.prepare();
   }

   Options() {
   }

   Getopt2 getOpts() {
      return this.opts;
   }

   private void prepare() {
      this.opts.setUsageArgs(cat.usageArgs());
      this.opts.setUsageFooter(cat.usageTrailer());
      this.opts.addFlag("distribute", cat.usageDistribute());
      this.opts.addFlag("start", cat.usageStart());
      this.opts.addFlag("stop", cat.usageStop());
      this.opts.addFlag("redeploy", cat.usageRedeploy());
      this.opts.addFlag("undeploy", cat.usageUndeploy());
      this.opts.addFlag("deploy", cat.usageDeploy());
      this.opts.addFlag("update", cat.usageUpdate());
      this.opts.addAdvancedFlag("cancel", cat.usageCancel());
      this.opts.addAdvancedFlag("list", cat.usageList());
      this.opts.addAdvancedFlag("listtask", cat.usageList());
      this.opts.addAdvancedFlag("listapps", cat.usageListApps());
      this.opts.addAdvancedFlag("purgetasks", cat.usageAdPurgeTasks());
      this.opts.addFlag("examples", cat.usageExamples());
      this.opts.addOption("name", cat.exampleName(), cat.usageName());
      this.opts.addOption("targets", cat.exampleTargets(), cat.usageAdTargets());
      this.opts.addOption("plan", cat.examplePlan(), cat.usagePlan());
      this.opts.addFlag("library", cat.usageLibrary());
      this.opts.addAdvancedFlag("verbose", cat.usageVerbose());
      this.opts.addAdvancedOption("output", "raw", cat.usageOutput());
      this.opts.addAdvancedFlag("debug", cat.usageDebug());
      this.opts.addAdvancedFlag("upload", cat.usageUpload());
      this.opts.addAdvancedFlag("delete_files", cat.usageDeleteFiles());
      this.opts.addAdvancedFlag("remote", cat.usageRemote());
      this.opts.addAdvancedFlag("nostage", cat.usageNoStage());
      this.opts.addAdvancedFlag("external_stage", cat.usageExternalStage());
      this.opts.addAdvancedFlag("stage", cat.usageStage());
      this.opts.addAdvancedFlag("nowait", cat.usageNoWait());
      this.opts.addAdvancedOption("timeout", cat.exampleTimeout(), cat.usageTimeout());
      this.opts.addAdvancedOption("deploymentorder", cat.exampleDeploymentOrder(), cat.usageDeploymentOrder());
      this.opts.addAdvancedOption("source", cat.exampleSource(), cat.usageSource());
      this.opts.addAdvancedOption("altappdd", cat.exampleAltAppDD(), cat.usageAltAppDD());
      this.opts.addAdvancedOption("altwlsappdd", cat.exampleAltWebDD(), cat.usageAltWebDD());
      this.opts.addAdvancedOption("appversion", cat.exampleAppVersion(), cat.usageAppVersion());
      this.opts.addAdvancedOption("planversion", cat.examplePlanVersion(), cat.usagePlanVersion());
      this.opts.addAdvancedFlag("noversion", cat.usageNoVersion());
      this.opts.addAdvancedOption("retiretimeout", cat.exampleRetireTimeout(), cat.usageRetireTimeout());
      this.opts.addAdvancedOption("id", cat.exampleId(), cat.usageId());
      this.opts.addAdvancedFlag("adminmode", cat.usageAdminMode());
      this.opts.addAdvancedFlag("graceful", cat.usageGraceful());
      this.opts.addAdvancedFlag("ignoresessions", cat.usageIgnoreSessions("graceful"));
      this.opts.addAdvancedOption("rmigraceperiod", cat.exampleRmiGracePeriod(), cat.usageRmiGracePeriod("graceful"));
      this.opts.addAdvancedFlag("allversions", cat.usageAllVersions("allversions"));
      this.opts.addAdvancedOption("submoduletargets", cat.paramSubModuleTargets(), cat.usageSubModuleTargets());
      this.opts.addAdvancedOption("securityModel", "DDOnly|CustomRoles|CustomRolesAndPolicies|Advanced", cat.usageSecurityModel());
      this.opts.addAdvancedFlag("enableSecurityValidation", cat.usageSecurityEnabled());
      this.opts.addAdvancedOption("libspecver", cat.exampleLibSpecVersion(), cat.usageLibSpecVersion());
      this.opts.addAdvancedOption("libimplver", cat.exampleLibImplVersion(), cat.usageLibImplVersion());
      this.opts.addAdvancedFlag("usenonexclusivelock", cat.usageUseNonexclusiveLock());
      this.opts.addAdvancedFlag("defaultsubmoduletargets", cat.usageDefaultTargets());
      this.opts.markPrivate("defaultsubmoduletargets");
      this.opts.addAdvancedFlag("nodefaultsubmoduletargets", cat.usageNoDefaultTargets());
      this.opts.markPrivate("nodefaultsubmoduletargets");
      this.opts.addAdvancedFlag("activate", cat.usageActivate());
      this.opts.markPrivate("activate");
      this.opts.addAdvancedFlag("deactivate", cat.usageDeactivate());
      this.opts.markPrivate("deactivate");
      this.opts.addAdvancedFlag("unprepare", cat.usageUnprepare());
      this.opts.markPrivate("unprepare");
      this.opts.addAdvancedFlag("remove", cat.usageRemove());
      this.opts.markPrivate("remove");
      this.opts.addAdvancedOption("sourcerootforupload", cat.exampleSourceRootForUpload(), cat.usageSourceRootForUpload());
      this.opts.markPrivate("sourcerootforupload");
   }

   void extractOptions() throws IllegalArgumentException {
      this.verbose = this.opts.getBooleanOption("verbose", false);
      this.debug = this.opts.getBooleanOption("debug", false);
      String var1;
      if (this.debug) {
         var1 = System.getProperty("weblogic.deployer.debug");
         if (var1 == null) {
            System.setProperty("weblogic.deployer.debug", "deploy");
         }
      }

      this.examples = this.opts.getBooleanOption("examples", false);
      this.nowait = this.opts.getBooleanOption("nowait", false);
      if (this.opts.getBooleanOption("stage", false)) {
         this.stageMode = "stage";
      } else if (this.opts.getBooleanOption("nostage", false)) {
         this.stageMode = "nostage";
      } else if (this.opts.getBooleanOption("external_stage", false)) {
         this.stageMode = "external_stage";
      }

      this.upload = this.opts.getBooleanOption("upload", false);
      this.deleteFiles = this.opts.getBooleanOption("delete_files", false);
      this.remote = this.opts.getBooleanOption("remote", false);
      this.adminMode = this.opts.getBooleanOption("adminmode", false);
      this.graceful = this.opts.getBooleanOption("graceful", false);
      this.ignoreSessions = this.opts.getBooleanOption("ignoresessions", false);
      this.allVersions = this.opts.getBooleanOption("allversions", false);
      this.securityValidation = this.opts.getBooleanOption("enableSecurityValidation", false);
      this.libModule = this.opts.getBooleanOption("library", false);
      this.noexit = this.opts.getBooleanOption("noexit", false);
      this.isDefaultSubmoduleTargets = !this.opts.hasOption("nodefaultsubmoduletargets");
      this.useNonExclusiveLock = this.opts.hasOption("usenonexclusivelock");
      this.formatted = this.getOutputFormat();
      this.source = this.opts.getOption("source");
      if (this.source != null) {
         this.sourceFromOpts = true;
      }

      this.name = this.opts.getOption("name");
      this.appVersion = this.opts.getOption("appversion");
      this.planVersion = this.opts.getOption("planversion");
      this.noVersion = this.opts.getBooleanOption("noversion", false);
      this.retireTimeout = this.opts.getIntegerOption("retiretimeout", this.retireTimeout);
      this.rmiGracePeriod = this.opts.getIntegerOption("rmigraceperiod", -1);
      this.securityModel = this.opts.getOption("securityModel");
      this.libSpecVersion = this.opts.getOption("libspecver");
      this.libImplVersion = this.opts.getOption("libimplver");
      this.id = this.opts.getOption("id");
      this.timeout = (long)this.opts.getIntegerOption("timeout", 0);
      this.deploymentOrder = this.opts.getIntegerOption("deploymentorder", 100);
      this.altAppDD = this.opts.getOption("altappdd");
      this.altWlsAppDD = this.opts.getOption("altwlsappdd");
      this.plan = this.opts.getOption("plan");
      var1 = this.opts.getOption("sourcerootforupload");
      if (var1 != null) {
         if (this.source != null || this.upload) {
            throw new IllegalArgumentException(cat.errorSourceAndSourceRootNotAllowed());
         }

         this.source = var1;
         this.sourceFromOpts = true;
         this.upload = true;
      }

      this.cancelOp = this.opts.hasOption("cancel");
      this.deployOp = this.opts.hasOption("deploy");
      this.undeployOp = this.opts.hasOption("undeploy");
      this.distributeOp = this.opts.hasOption("distribute");
      this.startOp = this.opts.hasOption("start");
      this.stopOp = this.opts.hasOption("stop");
      this.redeployOp = this.opts.hasOption("redeploy");
      this.listappOp = this.opts.hasOption("listapps");
      this.listtaskOp = this.opts.hasOption("listtask");
      this.purgetasksOp = this.opts.hasOption("purgetasks");
      this.updateOp = this.opts.hasOption("update");
      this.deployOp = this.opts.hasOption("deploy");
      this.stopOp = this.opts.hasOption("stop");
      this.activateOp = this.checkDeprecated("activate", "deploy");
      this.deactivateOp = this.checkDeprecated("deactivate", "stop");
      this.unprepareOp = this.checkDeprecated("unprepare", "stop");
      this.removeOp = this.checkDeprecated("remove", "undeploy");
      this.listOp = this.opts.hasOption("list");
      if (this.listOp) {
         System.out.println(cat.warningListDeprecated());
      }

      this.checkDeprecated("sourcerootforupload", "upload");
      this.moduleTargets = this.getTargets("targets");
      this.submoduleTargets = this.getTargets("submoduletargets");
      this.delta = this.opts.args();
      if (this.delta != null && this.delta.length == 0) {
         this.delta = null;
      }

      if (this.delta != null && this.delta.length > 0 && this.source == null && this.delta.length == 1) {
         if (this.opts.hasOption("sourcerootforupload")) {
            throw new IllegalArgumentException(cat.errorSourceAndSourceRootNotAllowed());
         }

         this.source = this.delta[0];
         this.sourceFromArgs = true;
      }

      this.setImpliedLibrary();
   }

   private void setImpliedLibrary() {
      if (this.libImplVersion != null || this.libSpecVersion != null) {
         this.libModule = true;
      }

   }

   private boolean getOutputFormat() throws IllegalArgumentException {
      String var1 = this.opts.getOption("output", "raw");
      if ("raw".equals(var1)) {
         return false;
      } else if ("formatted".equals(var1)) {
         return true;
      } else {
         System.out.println(cat.badFormat(var1));
         return false;
      }
   }

   private boolean checkDeprecated(String var1, String var2) {
      boolean var3 = this.opts.hasOption(var1);
      if (var3) {
         System.out.println(cat.deprecated(var1, var2));
      }

      return var3;
   }

   private String[] getTargets(String var1) {
      ArrayList var2 = new ArrayList();
      String var3 = this.opts.getOption(var1);
      if (var3 != null) {
         StringTokenizer var4 = new StringTokenizer(var3, ",");

         while(var4.hasMoreTokens()) {
            String var5 = var4.nextToken();
            if (var5 != null && var5.length() != 0) {
               var2.add(var5);
            }
         }
      }

      return (String[])((String[])var2.toArray(new String[0]));
   }

   public boolean isRemote() {
      return this.remote;
   }

   public void setRemote(boolean var1) {
      this.remote = var1;
   }

   public boolean isVerbose() {
      return this.verbose;
   }

   public void setVerbose(boolean var1) {
      this.verbose = var1;
   }

   public boolean isDebug() {
      return this.debug;
   }

   public void setDebug(boolean var1) {
      this.debug = var1;
   }

   public boolean isUpload() {
      return this.upload;
   }

   static {
      allOptions.add("plan");
      allOptions.add("altwlsappdd");
      allOptions.add("altappdd");
      allOptions.add("id");
      allOptions.add("libimplver");
      allOptions.add("libspecver");
      allOptions.add("securityModel");
      allOptions.add("submoduletargets");
      allOptions.add("targets");
      allOptions.add("retiretimeout");
      allOptions.add("planversion");
      allOptions.add("appversion");
      allOptions.add("noversion");
      allOptions.add("name");
      allOptions.add("source");
      allOptions.add("library");
      allOptions.add("enableSecurityValidation");
      allOptions.add("ignoresessions");
      allOptions.add("rmigraceperiod");
      allOptions.add("graceful");
      allOptions.add("allversions");
      allOptions.add("adminmode");
      allOptions.add("delete_files");
      allOptions.add("stage");
      allOptions.add("nostage");
      allOptions.add("defaultsubmoduletargets");
      allOptions.add("nodefaultsubmoduletargets");
      allOptions.add("usenonexclusivelock");
      cat = new DeployerTextFormatter();
   }
}
