package weblogic.deploy.api.tools.deployer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import weblogic.deploy.internal.DeployerTextFormatter;
import weblogic.deploy.utils.DeployerHelper;
import weblogic.deploy.utils.MBeanHomeTool;
import weblogic.management.ApplicationException;
import weblogic.management.MBeanHome;
import weblogic.management.ManagementException;
import weblogic.management.deploy.DeploymentData;
import weblogic.management.deploy.TargetStatus;
import weblogic.management.runtime.DeployerRuntimeMBean;
import weblogic.management.runtime.DeploymentTaskRuntimeMBean;
import weblogic.rmi.extensions.RemoteRuntimeException;
import weblogic.utils.StackTraceUtils;

public class OldDeployer extends MBeanHomeTool {
   public static final long MAX_NOTIFICATION_WAIT = 1000L;
   private static final String OPTION_OUTPUT = "output";
   private static final String OPTION_VERBOSE = "verbose";
   private static final String OPTION_DEBUG = "debug";
   private static final String OPTION_EXAMPLES = "examples";
   private static final String OPTION_NOWAIT = "nowait";
   private static final String OPTION_NOSTAGE = "nostage";
   private static final String OPTION_STAGE = "stage";
   private static final String OPTION_EXTERNAL_STAGE = "external_stage";
   private static final String OPTION_SOURCE = "source";
   private static final String OPTION_UPLOAD = "upload";
   private static final String OPTION_DELETE_FILES = "delete_files";
   private static final String OPTION_REMOTE = "remote";
   private static final String OPTION_NAME = "name";
   private static final String OPTION_APP_VERSION = "appversion";
   private static final String OPTION_PLAN_VERSION = "planversion";
   private static final String OPTION_RETIRE_TIMEOUT = "retiretimeout";
   private static final String OPTION_ADMIN_MODE = "adminmode";
   private static final String OPTION_GRACEFUL = "graceful";
   private static final String OPTION_IGNORE_SESSIONS = "ignoresessions";
   private static final String OPTION_TARGETS = "targets";
   private static final String OPTION_SUBMODULE_TARGETS = "submoduletargets";
   private static final String OPTION_SECURITY_MODEL = "securityModel";
   private static final String OPTION_SECURITY_VALIDATE = "enableSecurityValidation";
   private static final String OPTION_LIB_MODULE = "library";
   private static final String OPTION_LIB_SPEC_VERSION = "libspecver";
   private static final String OPTION_LIB_IMPL_VERSION = "libimplver";
   private static final String OPTION_ID = "id";
   private static final String OPTION_TIMEOUT = "timeout";
   private static final String OPTION_ALT_APP_DD = "altappdd";
   private static final String OPTION_ALT_WEB_DD = "altwlsappdd";
   private static final String OPTION_ACTIVATE = "activate";
   private static final String OPTION_DEACTIVATE = "deactivate";
   private static final String OPTION_REMOVE = "remove";
   private static final String OPTION_CANCEL = "cancel";
   private static final String OPTION_LIST = "list";
   private static final String OPTION_UNPREPARE = "unprepare";
   private static final String OPTION_DEPLOY = "deploy";
   private static final String OPTION_UNDEPLOY = "undeploy";
   private static final String OPTION_DISTRIBUTE = "distribute";
   private static final String OPTION_START = "start";
   private static final String OPTION_STOP = "stop";
   private static final String OPTION_REDEPLOY = "redeploy";
   private static final String OPTION_LIST_APP = "listapps";
   private static final String OPTION_LIST_TASK = "listtask";
   private static final int ACTION_UNSPECIFIED = -1;
   private static final int ACTION_ACTIVATE = 0;
   private static final int ACTION_DEACTIVATE = 1;
   private static final int ACTION_REMOVE = 2;
   private static final int ACTION_CANCEL = 3;
   private static final int ACTION_LIST = 4;
   private static final int ACTION_UNPREPARE = 5;
   private static final int ACTION_DISTRIBUTE = 6;
   private static final int ACTION_START = 7;
   private static final int ACTION_STOP = 8;
   private static final int ACTION_REDEPLOY = 9;
   private static final int ACTION_UNDEPLOY = 10;
   private static final int ACTION_DEPLOY = 11;
   private static final int ACTION_LIST_APPS = 12;
   private static final int ACTION_LIST_TASK = 13;
   private static final int ACTION_MAX = 13;
   private static boolean debug = false;
   private static boolean verbose = false;
   private static final boolean DEFAULT_FMT = false;
   private boolean formatted = false;
   private boolean raw = true;
   private static DeployerTextFormatter messageFormatter;
   private static String[] options = new String[]{"activate", "deactivate", "remove", "cancel", "list", "unprepare", "distribute", "start", "stop", "redeploy", "undeploy", "deploy", "listapps", "listtask"};
   private boolean isNameFromSource = false;
   private boolean isSourceFromOpts = false;
   StringBuffer outstr;

   public OldDeployer(String[] var1) {
      super(var1);
      messageFormatter = new DeployerTextFormatter();
   }

   public OldDeployer(MBeanHome var1, String[] var2) {
      super(var1, var2);
      messageFormatter = new DeployerTextFormatter();
   }

   public static void main(String[] var0) {
      try {
         runMain(var0);
      } catch (Exception var2) {
         println(messageFormatter.errorInitDeployer(var2.toString()));
      }

   }

   private static void runMain(String[] var0) throws Exception {
      OldDeployer var1 = new OldDeployer(var0);
      var1.run();
   }

   public static void mainWithExceptions(String[] var0) throws Exception {
      runMain(var0);
   }

   public void prepare() {
      super.prepare();
      this.opts.addFlag("advanced", "Advanced options are:");
      this.opts.markPrivate("advanced");
      this.opts.addFlag("version", "version 1.1");
      this.opts.markPrivate("version");
      this.opts.addFlag("help", "Displays help for weblogic.Deployer");
      this.opts.markPrivate("help");
      this.opts.setUsageArgs(messageFormatter.usageArgs());
      this.opts.addAdvancedFlag("verbose", messageFormatter.usageVerbose());
      this.opts.addAdvancedOption("output", "raw", messageFormatter.usageOutput());
      this.opts.addAdvancedFlag("debug", messageFormatter.usageDebug());
      this.opts.addFlag("examples", messageFormatter.usageExamples());
      this.opts.addAdvancedFlag("upload", messageFormatter.usageUpload());
      this.opts.addAdvancedFlag("delete_files", messageFormatter.usageDeleteFiles());
      this.opts.addAdvancedFlag("remote", messageFormatter.usageRemote());
      this.opts.addAdvancedFlag("nostage", messageFormatter.usageNoStage());
      this.opts.addAdvancedFlag("external_stage", messageFormatter.usageExternalStage());
      this.opts.addAdvancedFlag("stage", messageFormatter.usageStage());
      this.opts.addAdvancedFlag("nowait", messageFormatter.usageNoWait());
      this.opts.addAdvancedOption("timeout", messageFormatter.exampleTimeout(), messageFormatter.usageTimeout());
      this.opts.addAdvancedOption("source", messageFormatter.exampleSource(), messageFormatter.usageSource());
      this.opts.addAdvancedOption("altappdd", messageFormatter.exampleAltAppDD(), messageFormatter.usageAltAppDD());
      this.opts.addAdvancedOption("altwlsappdd", messageFormatter.exampleAltWebDD(), messageFormatter.usageAltWebDD());
      this.opts.addOption("name", messageFormatter.exampleName(), messageFormatter.usageName());
      this.opts.addAdvancedOption("appversion", messageFormatter.exampleAppVersion(), messageFormatter.usageAppVersion());
      this.opts.addAdvancedOption("retiretimeout", messageFormatter.exampleRetireTimeout(), messageFormatter.usageRetireTimeout());
      this.opts.addOption("targets", messageFormatter.exampleTargets(), messageFormatter.usageAdTargets());
      this.opts.addAdvancedOption("id", messageFormatter.exampleId(), messageFormatter.usageId());
      this.opts.addAdvancedFlag("activate", messageFormatter.usageActivate());
      this.opts.markPrivate("activate");
      this.opts.addAdvancedFlag("deactivate", messageFormatter.usageDeactivate());
      this.opts.markPrivate("deactivate");
      this.opts.addAdvancedFlag("unprepare", messageFormatter.usageUnprepare());
      this.opts.markPrivate("unprepare");
      this.opts.addAdvancedFlag("remove", messageFormatter.usageRemove());
      this.opts.markPrivate("remove");
      this.opts.addAdvancedFlag("cancel", messageFormatter.usageCancel());
      this.opts.addAdvancedFlag("list", messageFormatter.usageList());
      this.opts.addAdvancedFlag("listtask", messageFormatter.usageList());
      this.opts.addAdvancedFlag("listapps", messageFormatter.usageListApps());
      this.opts.addAdvancedFlag("adminmode", messageFormatter.usageAdminMode());
      this.opts.addAdvancedFlag("graceful", messageFormatter.usageGraceful());
      this.opts.addAdvancedFlag("ignoresessions", messageFormatter.usageIgnoreSessions("graceful"));
      this.opts.addFlag("deploy", messageFormatter.usageDeploy());
      this.opts.addAdvancedFlag("distribute", messageFormatter.usageDistribute());
      this.opts.addAdvancedFlag("start", messageFormatter.usageStart());
      this.opts.addAdvancedFlag("stop", messageFormatter.usageStop());
      this.opts.addFlag("redeploy", messageFormatter.usageRedeploy());
      this.opts.addFlag("undeploy", messageFormatter.usageUndeploy());
      this.opts.addAdvancedOption("submoduletargets", messageFormatter.paramSubModuleTargets(), messageFormatter.usageSubModuleTargets());
      this.opts.addAdvancedOption("securityModel", "DDOnly|CustomRoles|CustomRolesAndPolicies|Advanced", messageFormatter.usageSecurityModel());
      this.opts.addAdvancedFlag("enableSecurityValidation", messageFormatter.usageSecurityEnabled());
      this.opts.addAdvancedFlag("library", messageFormatter.usageLibrary());
      this.opts.addAdvancedOption("libspecver", messageFormatter.exampleLibSpecVersion(), messageFormatter.usageLibSpecVersion());
      this.opts.addAdvancedOption("libimplver", messageFormatter.exampleLibImplVersion(), messageFormatter.usageLibImplVersion());
      this.setRequireExtraArgs(false);
   }

   public void runBody() throws Exception {
      boolean var1 = this.opts.getBooleanOption("examples", false);
      if (var1) {
         this.showDetailedMessage();
      } else {
         this.checkOptions();
         String var2 = this.opts.getOption("source");
         verbose = this.opts.getBooleanOption("verbose", false);
         String var3 = this.opts.getOption("output");
         if (var3 != null) {
            if (var3.equals("raw")) {
               this.formatted = false;
               this.raw = true;
            } else if (var3.equals("formatted")) {
               this.formatted = true;
               this.raw = false;
            }
         }

         debug = this.opts.getBooleanOption("debug", false);
         boolean var4 = this.opts.getBooleanOption("nowait", false);
         boolean var5 = this.opts.getBooleanOption("upload", false);
         long var6 = (long)this.opts.getIntegerOption("timeout", 0);
         boolean var8 = this.opts.getBooleanOption("nostage", false);
         boolean var9 = this.opts.getBooleanOption("external_stage", false);
         boolean var10 = this.opts.getBooleanOption("stage", false);
         boolean var11 = this.opts.getBooleanOption("remote", false);
         this.setShowStackTrace(debug);
         MBeanHome var12 = null;
         var12 = this.getMBeanHome();

         try {
            DeployerHelper var13 = new DeployerHelper((MBeanHome)var12);
            var13.setFormatted(this.formatted);
            DeployerRuntimeMBean var14 = var13.getDeployer();
            if (var14 == null) {
               throw new DeployerException(messageFormatter.errorUnableToAccessDeployer(this.opts.getOption("adminurl", "t3://localhost:7001"), (String)null));
            }

            String var15 = this.opts.getOption("id");
            DeploymentTaskRuntimeMBean var16 = null;
            String var17 = null;
            int var18 = this.getActionFromOpts(false);
            String[] var19 = this.opts.args();
            String var22;
            switch (var18) {
               case 0:
               case 9:
               case 11:
                  var17 = this.getNameRequired();
                  var2 = this.getSourceFromOpts();
                  String var20;
                  if (var5) {
                     if (var2 == null) {
                        throw new IllegalArgumentException(messageFormatter.errorMissingSourceForUpload());
                     }

                     var20 = this.opts.getOption("adminurl", "t3://localhost:7001");
                     String var21 = this.getUser();
                     var22 = this.getPassword();
                     String[] var35;
                     if (this.isSourceFromOpts) {
                        var35 = null;
                     } else {
                        var35 = this.getFilesFromOpts();
                     }

                     var2 = var13.uploadSource(var20, var21, var22, var2, var35, var17);
                  } else {
                     var2 = this.getAbsoluteSourcePath(var11, var2);
                  }

                  var20 = null;
                  if (var10) {
                     var20 = "stage";
                  } else if (var8) {
                     var20 = "nostage";
                  } else if (var9) {
                     var20 = "external_stage";
                  }

                  DeploymentData var30 = this.getDeploymentDataFromOpts();
                  if (var18 == 9) {
                     if (var2 == null) {
                        var16 = var14.redeploy(var17, var30, var15, false);
                     } else {
                        var16 = var14.redeploy(var2, var17, var30, var15, false);
                     }
                  } else {
                     var16 = var14.deploy(var2, var17, var20, var30, var15, false);
                  }

                  if (verbose) {
                     var16.setNotificationLevel(2);
                     var13.initiateListening(var16);
                  }

                  var13.registerTaskCompletionNotificationListener(var16);
                  var16.start();
                  break;
               case 1:
                  this.rejectFileList(var19);
                  var17 = this.getNameRequired();
                  var16 = var14.deactivate(var17, this.getDeploymentDataFromOpts(), var15, false);
                  if (verbose) {
                     var16.setNotificationLevel(2);
                     var13.initiateListening(var16);
                  }

                  var13.registerTaskCompletionNotificationListener(var16);
                  var16.start();
                  break;
               case 2:
               case 10:
                  this.rejectFileList(var19);
                  var17 = this.getNameRequired();
                  var16 = var14.remove(var17, this.getDeploymentDataFromOpts(), var15, false);
                  if (verbose) {
                     var16.setNotificationLevel(2);
                     var13.initiateListening(var16);
                  }

                  var13.registerTaskCompletionNotificationListener(var16);
                  var16.start();
                  break;
               case 3:
                  if (var15 == null) {
                     throw new IllegalArgumentException(messageFormatter.errorMissingId());
                  }

                  var16 = var13.getTaskByID(var15);
                  if (var16 == null) {
                     throw new DeployerException(messageFormatter.errorTaskNotFound(var15));
                  }

                  var16.cancel();
                  this.processCancel(var16);
                  break;
               case 4:
               case 13:
                  if (var15 != null) {
                     var16 = var13.getTaskByID(var15);
                     if (var16 == null) {
                        throw new DeployerException(messageFormatter.errorTaskNotFound(var15));
                     }

                     if (this.formatted) {
                        this.showTaskInformationHeader();
                     }

                     this.showTaskInformation(var16);
                     return;
                  } else {
                     DeploymentTaskRuntimeMBean[] var32 = var13.getAllTasks();

                     for(int var34 = 0; var34 < var32.length; ++var34) {
                        this.showTaskInformation(var32[var34]);
                     }

                     return;
                  }
               case 5:
                  var17 = this.getNameRequired();
                  var16 = var14.unprepare(var17, this.getDeploymentDataFromOpts(), var15, false);
                  if (verbose) {
                     var16.setNotificationLevel(2);
                     var13.initiateListening(var16);
                  }

                  var13.registerTaskCompletionNotificationListener(var16);
                  var16.start();
                  break;
               case 6:
                  var17 = this.getNameRequired();
                  var2 = this.getSourceFromOpts();
                  if (var5) {
                     if (var2 == null) {
                        throw new IllegalArgumentException(messageFormatter.errorMissingSourceForUpload());
                     }

                     var22 = this.opts.getOption("adminurl", "t3://localhost:7001");
                     String var23 = this.getUser();
                     String var24 = this.getPassword();
                     var13.uploadSource(var22, var23, var24, var2, this.getFilesFromOpts(), var17);
                  }

                  var2 = this.getAbsoluteSourcePath(var11, this.getSourceFromOpts());
                  var16 = var14.distribute(var2, var17, this.getDeploymentDataFromOpts(), var15, false);
                  if (verbose) {
                     var16.setNotificationLevel(2);
                     var13.initiateListening(var16);
                  }

                  var13.registerTaskCompletionNotificationListener(var16);
                  var16.start();
                  break;
               case 7:
                  this.rejectFileListWithSourceArgument(var19);
                  var17 = this.getNameRequired();
                  var16 = var14.start(var17, this.getDeploymentDataFromOpts(), var15, false);
                  if (verbose) {
                     var16.setNotificationLevel(2);
                     var13.initiateListening(var16);
                  }

                  var13.registerTaskCompletionNotificationListener(var16);
                  var16.start();
                  break;
               case 8:
                  this.rejectFileList(var19);
                  var17 = this.getNameRequired();
                  var16 = var14.stop(var17, this.getDeploymentDataFromOpts(), var15, false);
                  if (verbose) {
                     var16.setNotificationLevel(2);
                     var13.initiateListening(var16);
                  }

                  var13.registerTaskCompletionNotificationListener(var16);
                  var16.start();
                  break;
               case 12:
                  this.listApps(var13);
            }

            println(messageFormatter.messageStartedTask(var16.getId(), var16.getDescription()));
            if (!var4) {
               if (var15 == null) {
                  var15 = var16.getId();
               }

               long var31 = 0L;
               if (var6 != 0L) {
                  var31 = System.currentTimeMillis() + 1000L * var6;
               }

               var13.waitForTaskCompletion(var16, var31);
               if (var16.isRunning()) {
                  inform(messageFormatter.timeOut(var16.getId()));
               }

               if (this.formatted) {
                  this.showTaskInformationHeader();
               }

               int var33 = this.showTaskInformation(var16);
               if (!this.opts.hasOption("noexit")) {
                  System.exit(var33);
                  return;
               }

               if (var33 == 0) {
                  return;
               }

               throw new DeployerException(this.outstr == null ? "Deployment failed" : this.outstr.toString());
            }
         } catch (Exception var28) {
            if (var28 instanceof RemoteRuntimeException) {
               throw new DeployerException(messageFormatter.errorLostConnection());
            }

            throw var28;
         } finally {
            this.reset();
         }

      }
   }

   private void rejectFileList(String[] var1) {
      if (var1 != null && var1.length > 0) {
         throw new IllegalArgumentException(messageFormatter.errorFilesIllegalInDeactivate());
      }
   }

   private String getAbsoluteSourcePath(boolean var1, String var2) {
      if (!var1 && var2 != null) {
         File var3;
         if (var2.equals(".")) {
            var3 = new File((new File(var2)).getAbsolutePath());
            var2 = var3.getParent();
         }

         if (var2.length() > 1 && var2.charAt(1) != ':') {
            var3 = new File(var2);
            var2 = var3.getAbsolutePath();
         }
      }

      return var2;
   }

   void checkOptions() throws DeployerException {
      int var1 = this.getActionFromOpts(false);
      String var2 = this.getSourceFromOpts();
      if (var1 == 4) {
         System.out.println(messageFormatter.warningListDeprecated());
      }

      String var3 = this.opts.getOption("name");
      if (var3 != null && (4 == var1 || 3 == var1)) {
         throw new IllegalArgumentException(messageFormatter.errorNameNotAllowed(this.translateAction(var1)));
      } else {
         String[] var4 = this.getFilesFromOpts();
         if (var3 == null && var2 == null && 0 == var1 && var4 == null) {
            throw new IllegalArgumentException(messageFormatter.errorMissingName());
         } else if (var3 != null || 1 != var1 && 2 != var1 && 5 != var1) {
            if (var1 != 11 && var1 != 6) {
               if (this.opts.hasOption("securityModel")) {
                  throw new IllegalArgumentException(messageFormatter.errorOptionOnlyWithDeploy("securityModel"));
               }

               if (this.opts.hasOption("enableSecurityValidation")) {
                  throw new IllegalArgumentException(messageFormatter.errorOptionOnlyWithDeploy("enableSecurityValidation"));
               }

               if (this.opts.hasOption("library")) {
                  throw new IllegalArgumentException(messageFormatter.errorOptionOnlyWithDeploy("library"));
               }
            }

         } else {
            throw new IllegalArgumentException(messageFormatter.errorMissingName());
         }
      }
   }

   private void showTaskInformationHeader() {
      println("");
      println(messageFormatter.showListHeader());
   }

   private static int getActionFromTask(int var0) {
      switch (var0) {
         case 1:
            return 11;
         case 2:
         default:
            return -1;
         case 3:
            return 10;
         case 4:
            return 2;
         case 5:
            return 5;
         case 6:
            return 6;
         case 7:
            return 7;
         case 8:
            return 8;
         case 9:
            return 9;
      }
   }

   private int showRawTaskInfo(DeploymentTaskRuntimeMBean var1) {
      int var2 = 0;
      this.outstr = new StringBuffer();
      String var3 = this.getTaskStatus(var1);
      this.outstr.append(messageFormatter.allTaskStatus(var1.getId(), var3, var1.getDescription()));
      this.outstr.append("\n");
      TargetStatus[] var4 = var1.getTargets();

      for(int var9 = 0; var9 < var4.length; ++var9) {
         TargetStatus var5 = var4[var9];
         if (var5.getState() == 2) {
            ++var2;
         }

         String var6 = this.getTargetType(var5.getTargetType());
         String var7 = this.getTargetState(var5.getState());
         this.outstr.append(messageFormatter.showTargetState(var6, var5.getTarget(), var7, this.translateAction(var1.getTask())));
         this.outstr.append("\n");
         Exception[] var8 = var5.getMessages();

         for(int var10 = 0; var10 < var8.length; ++var10) {
            this.outstr.append(this.translateDeploymentMessage(var8[var10]));
         }
      }

      this.outstr.append("\n");
      return var2;
   }

   private String getTargetState(int var1) {
      switch (var1) {
         case 0:
            return messageFormatter.stateInit();
         case 1:
            return messageFormatter.stateRunning();
         case 2:
            return messageFormatter.stateFailed();
         case 3:
            return messageFormatter.stateCompleted();
         case 4:
            return messageFormatter.stateDeferred();
         default:
            return messageFormatter.unknown();
      }
   }

   private String getTargetType(int var1) {
      switch (var1) {
         case 1:
            return messageFormatter.messageServer();
         case 2:
            return messageFormatter.messageCluster();
         default:
            return messageFormatter.unknown();
      }
   }

   private String getTaskStatus(DeploymentTaskRuntimeMBean var1) {
      int var2 = var1.getState();
      switch (var2) {
         case 0:
            return messageFormatter.stateInit();
         case 1:
            return messageFormatter.stateRunning();
         case 2:
            return messageFormatter.stateCompleted();
         case 3:
            return messageFormatter.stateFailed();
         case 4:
            return messageFormatter.stateDeferred();
         default:
            return messageFormatter.unknown();
      }
   }

   private int showTaskInformation(DeploymentTaskRuntimeMBean var1) {
      int var2;
      if (this.raw) {
         var2 = this.showRawTaskInfo(var1);
         print(this.outstr.toString());
         return var2;
      } else {
         var2 = 0;
         String var3 = var1.getId();
         int var4 = this.getActionFromOpts(false);
         if (var4 == 4 || var4 == 3) {
            var4 = getActionFromTask(var1.getTask());
         }

         String var5 = this.translateTask(var4);
         String var6 = var1.getSource();
         String var7 = var1.getApplicationName();
         TargetStatus[] var8 = var1.getTargets();
         int var9;
         String var11;
         if (var8.length == 0) {
            var9 = var1.getState();
            if (var9 != 2 && var9 != 4) {
               if (var9 == 3) {
                  Exception var18 = var1.getError();
                  var11 = messageFormatter.noMessage();
                  if (var18 != null) {
                     var11 = var18.getMessage();
                  }

                  messageFormatter.errorNoRealTargets(var11);
                  return 1;
               } else {
                  messageFormatter.messageNoTargetsRunning();
                  return 0;
               }
            } else {
               messageFormatter.messageNoRealTargets();
               return 0;
            }
         } else {
            for(var9 = 0; var9 < var8.length; ++var9) {
               TargetStatus var10 = var8[var9];
               var11 = var10.getTarget();
               int var12 = var10.getType();
               int var13 = var10.getState();
               String var14 = this.translateTargetType(var12);
               String var15 = this.translateStatus(var13);
               print(var3 + "\t" + var5 + "\t" + var15 + "\t" + var11 + "\t" + var14 + "\t" + var7 + "\t" + var6 + "\t");
               if (var13 == 2) {
                  ++var2;
               }

               Exception[] var16 = var10.getMessages();

               for(int var17 = 0; var17 < var16.length && var16[var17] != null; ++var17) {
                  print(this.translateDeploymentMessage(var16[var17]));
               }

               println("");
            }

            return var2;
         }
      }
   }

   private String translateTask(int var1) {
      switch (var1) {
         case 0:
            return messageFormatter.messageActivate();
         case 1:
            return messageFormatter.messageDeactivate();
         case 2:
            return messageFormatter.messageRemove();
         case 3:
            return messageFormatter.messageCancel();
         case 4:
            return messageFormatter.messageList();
         case 5:
            return messageFormatter.messageUnprepare();
         case 6:
            return messageFormatter.messageDistribute();
         case 7:
            return messageFormatter.messageStart();
         case 8:
            return messageFormatter.messageStop();
         case 9:
            return messageFormatter.messageRedeploy();
         case 10:
            return messageFormatter.messageUndeploy();
         case 11:
            return messageFormatter.messageDeploy();
         case 12:
            return messageFormatter.messageListApps();
         default:
            return null;
      }
   }

   private String translateAction(int var1) {
      return options[var1];
   }

   private String translateTargetType(int var1) {
      if (var1 == 1) {
         return messageFormatter.messageServer();
      } else {
         return var1 == 2 ? messageFormatter.messageCluster() : messageFormatter.messageUnknown();
      }
   }

   private String translateStatus(int var1) {
      switch (var1) {
         case 0:
            return messageFormatter.messageStateInit();
         case 1:
            return messageFormatter.messageStateInProgress();
         case 2:
            return messageFormatter.messageStateFailed();
         case 3:
            return messageFormatter.messageStateSuccess();
         case 4:
            return messageFormatter.messageStateDeferred();
         default:
            return null;
      }
   }

   private String translateDeploymentMessage(Exception var1) {
      StringBuffer var2;
      String var7;
      Throwable var10;
      if (this.formatted) {
         if (var1 instanceof ApplicationException) {
            ApplicationException var3 = (ApplicationException)var1;
            var2 = new StringBuffer("\nException:");
            var2.append(var3.getClass().getName() + ": ");
            var2.append(var3.getApplicationMessage());
            var2.append("\n");
            Hashtable var4 = var3.getModuleErrors();
            if (var4 != null && var4.size() != 0) {
               Iterator var5 = var4.keySet().iterator();

               while(var5.hasNext()) {
                  String var6 = (String)var5.next();
                  var7 = (String)var4.get(var6);
                  var2.append("\tModule: ");
                  var2.append(var6);
                  var2.append("\tError: ");
                  var2.append(var7);
                  var2.append("\n");
                  if (debug) {
                     Exception var8 = var3.getTargetException(var6);
                     if (var8 != null) {
                        var2.append(StackTraceUtils.throwable2StackTrace(var8));
                        var2.append("\n");
                     }
                  }
               }
            }

            if (var3.getNestedException() != null) {
               var2.append("Nested Exception:\n");
               var2.append(StackTraceUtils.throwable2StackTrace(var3.getNestedException()));
               var2.append("\n");
            }
         } else if (var1 instanceof ManagementException) {
            ManagementException var10000 = (ManagementException)var1;
            var10 = ManagementException.unWrapExceptions(var1);
            if (var10 != var1) {
               if (var10 instanceof Exception) {
                  return this.translateDeploymentMessage((Exception)var10);
               }

               var2 = new StringBuffer("\n" + var10.toString());
            } else {
               var2 = new StringBuffer("\n" + var1.toString());
            }
         } else {
            var2 = new StringBuffer("\n" + var1.toString());
         }
      } else {
         var2 = new StringBuffer();
         var10 = ManagementException.unWrapExceptions(var1);
         if (var10 instanceof ApplicationException) {
            ApplicationException var11 = (ApplicationException)var10;
            debug("dumping ApplicationException message");
            var2.append(var11.getApplicationMessage());
            var2.append("\n");
            Hashtable var12 = var11.getModuleErrors();
            if (var12 != null && var12.size() != 0) {
               Iterator var13 = var12.keySet().iterator();

               while(var13.hasNext()) {
                  var7 = (String)var13.next();
                  String var14 = (String)var12.get(var7);
                  debug("dumping ModuleException message");
                  var2.append(messageFormatter.moduleException(var7, var14));
                  var2.append("\n");
                  if (debug) {
                     Exception var9 = var11.getTargetException(var7);
                     if (var9 != null) {
                        debug("dumping ModuleException stack");
                        var2.append(StackTraceUtils.throwable2StackTrace(var9));
                        var2.append("\n");
                     }
                  }
               }
            }
         } else if (debug) {
            debug("dumping Exception stack");
            var2.append(StackTraceUtils.throwable2StackTrace(var10));
            var2.append("\n");
         } else {
            debug("dumping Exception message");
            var2.append(var10.toString());
            var2.append("\n");
         }
      }

      return var2.toString();
   }

   private void showDetailedMessage() {
      int var1 = this.getActionFromOpts(true);
      switch (var1) {
         case 0:
         case 11:
            println(messageFormatter.usageAdDeploy());
            break;
         case 1:
         case 2:
         case 3:
         case 4:
         case 5:
         default:
            println(messageFormatter.showExamples());
            break;
         case 6:
            println(messageFormatter.usageAdDistribute());
            break;
         case 7:
            println(messageFormatter.usageAdStart());
            break;
         case 8:
            println(messageFormatter.usageAdStop());
            break;
         case 9:
            println(messageFormatter.usageAdRedeploy());
            break;
         case 10:
            println(messageFormatter.usageAdUndeploy());
      }

   }

   private String getNameRequired() throws DeployerException {
      String var1 = this.opts.getOption("name");
      if (!this.opts.hasOption("library") && (var1 == null || var1.length() == 0)) {
         String var2 = this.opts.getOption("source");
         String[] var3 = this.getFilesFromOpts();
         if (var2 == null && var3 == null) {
            throw new DeployerException(messageFormatter.errorMissingName());
         }

         if (var2 == null && var3.length > 0) {
            var2 = var3[0];
         }

         File var4 = new File(var2);

         try {
            var1 = var4.getCanonicalFile().getName();
         } catch (IOException var6) {
            throw new DeployerException(messageFormatter.messageIOException(var1));
         }

         int var5 = var1.indexOf(46);
         if (var5 != -1) {
            var1 = var1.substring(0, var5);
         }

         this.isNameFromSource = true;
      }

      return var1;
   }

   String[] getFilesFromOpts() {
      String[] var1 = this.opts.args();
      return var1 != null && var1.length == 0 ? null : var1;
   }

   private int getActionFromOpts(boolean var1) throws IllegalArgumentException {
      String var2 = null;
      int var3 = -1;

      for(int var4 = 0; var4 <= 13; ++var4) {
         if (this.opts.containsOption(options[var4])) {
            if (var3 == -1) {
               var3 = var4;
            } else {
               if (var2 == null) {
                  var2 = new String(options[var3]);
               }

               var2 = var2 + "," + options[var4];
            }
         }
      }

      if (var3 == -1) {
         if (var1) {
            return var3;
         } else {
            throw new IllegalArgumentException(messageFormatter.errorMissingAction());
         }
      } else if (var2 != null) {
         throw new IllegalArgumentException(messageFormatter.errorMultipleActions(var2));
      } else {
         return var3;
      }
   }

   private DeploymentData getDeploymentDataFromOpts() throws IllegalArgumentException, DeployerException {
      int var1 = this.getActionFromOpts(false);
      DeploymentData var2 = new DeploymentData();
      var2.setActionFromDeployer(true);
      if (var1 == 0 || var1 == 9) {
         var2.setFile(this.getFilesFromOpts());
      }

      var2.setIsNameFromSource(this.isNameFromSource);
      this.setVersionParams(var1, var2);
      this.setAdminMode(var1, var2);
      this.setAltAppDD(var2);
      this.setAltWeblogicDD(var2);
      int var3 = this.opts.getIntegerOption("timeout", 0);
      if (var3 != 0) {
         var2.setTimeOut(var3 * 1000);
      }

      boolean var4 = this.opts.getBooleanOption("delete_files", false);
      var2.setDelete(var4);
      debug("DeploymentData : " + var2.toString());
      String var5 = this.opts.getOption("targets");
      if (var5 != null) {
         this.setTargets(var2, var5);
      }

      this.setSubModuleTargets(var2, var1, var5 == null);
      var2.setLibrary(this.opts.hasOption("library"));
      this.setSecurityParams(var2);
      return var2;
   }

   private void setSecurityParams(DeploymentData var1) {
      if (this.opts.hasOption("enableSecurityValidation")) {
         var1.setSecurityValidationEnabled(true);
      }

      String var2 = this.opts.getOption("securityModel");
      if (var2 != null) {
         var1.setSecurityModel(var2);
      }

   }

   private void setTargets(DeploymentData var1, String var2) {
      HashMap var3 = new HashMap();
      StringTokenizer var4 = new StringTokenizer(var2, ",");

      String var6;
      while(var4.hasMoreTokens()) {
         String var5 = var4.nextToken();
         if (var5 != null && var5.length() != 0) {
            var6 = null;
            int var7 = var5.indexOf(64);
            if (var7 != -1) {
               var6 = var5.substring(0, var7);
               var5 = var5.substring(var7 + 1);
            }

            ArrayList var8 = (ArrayList)var3.get(var5);
            if (var8 == null) {
               var8 = new ArrayList();
               var3.put(var5, var8);
            }

            if (var6 != null) {
               var8.add(var6);
            }
         }
      }

      Iterator var9 = var3.keySet().iterator();

      while(var9.hasNext()) {
         var6 = (String)var9.next();
         ArrayList var10 = (ArrayList)var3.get(var6);
         if (!var10.isEmpty()) {
            String[] var11 = new String[var10.size()];
            var11 = (String[])((String[])var10.toArray(var11));
            var1.addTarget(var6, var11);
         } else {
            var1.addTarget(var6, (String[])null);
         }
      }

   }

   private void setVersionParams(int var1, DeploymentData var2) {
      var2.getDeploymentOptions().setArchiveVersion(this.opts.getOption("appversion"));
      var2.getDeploymentOptions().setPlanVersion(this.opts.getOption("planversion"));
      int var3 = this.opts.getIntegerOption("retiretimeout", -1);
      if (var3 != -1) {
         if (var1 != 6 && var1 != 11 && (var1 != 0 && var1 != 9 || this.hasFiles(var2)) || this.opts.getOption("library") != null) {
            throw new IllegalArgumentException(messageFormatter.errorRetireTimeoutIllegal("retiretimeout"));
         }

         var2.getDeploymentOptions().setRetireTime(var3);
      }

      var2.getDeploymentOptions().setLibSpecVersion(this.opts.getOption("libspecver"));
      var2.getDeploymentOptions().setLibImplVersion(this.opts.getOption("libimplver"));
   }

   private void setAdminMode(int var1, DeploymentData var2) {
      boolean var3 = this.opts.getBooleanOption("adminmode", false);
      if (var3) {
         if (var1 != 6 && var1 != 11 && var1 != 10 && (var1 != 0 && var1 != 9 && var1 != 7 && var1 != 8 || this.hasFiles(var2))) {
            throw new IllegalArgumentException(messageFormatter.errorAdminModeIllegal("adminmode"));
         }

         var2.getDeploymentOptions().setTestMode(var3);
      }

      boolean var4 = this.opts.getBooleanOption("graceful", false);
      boolean var5 = this.opts.getBooleanOption("ignoresessions", false);
      if (!var4) {
         if (var5) {
            throw new IllegalArgumentException(messageFormatter.errorIgnoreSessionsIllegal("graceful", "ignoresessions"));
         }
      } else if (var1 != 8 && var1 != 10) {
         throw new IllegalArgumentException(messageFormatter.errorGracefulIllegal("graceful"));
      } else {
         var2.getDeploymentOptions().setGracefulProductionToAdmin(var4);
         var2.getDeploymentOptions().setGracefulIgnoreSessions(var5);
      }
   }

   private void setAltWeblogicDD(DeploymentData var1) {
      String var2 = this.opts.getOption("altwlsappdd");
      if (var2 != null) {
         File var3;
         if (var2.length() > 1 && var2.charAt(1) != ':') {
            var3 = new File(var2);
            var2 = var3.getAbsolutePath();
         }

         var3 = new File(var2);
         if (!var3.exists() || var3.isDirectory()) {
            throw new IllegalArgumentException(messageFormatter.noSourceAltWebDD(var2));
         }

         var1.setAltWLSDescriptorPath(var2);
      }

   }

   private void setAltAppDD(DeploymentData var1) {
      String var2 = this.opts.getOption("altappdd");
      if (var2 != null) {
         File var3;
         if (var2.length() > 1 && var2.charAt(1) != ':') {
            var3 = new File(var2);
            var2 = var3.getAbsolutePath();
         }

         var3 = new File(var2);
         if (!var3.exists() || var3.isDirectory()) {
            throw new IllegalArgumentException(messageFormatter.noSourceAltAppDD(var2));
         }

         var1.setAltDescriptorPath(var2);
      }

   }

   private void setSubModuleTargets(DeploymentData var1, int var2, boolean var3) throws DeployerException {
      String var4 = this.opts.getOption("submoduletargets");
      if (var4 != null) {
         if (!var3 && var2 == 9) {
            throw new DeployerException("-targets & -submoduletargets are mutually exclusive for -redeploy operation.");
         } else {
            StringTokenizer var5 = new StringTokenizer(var4, ",");

            while(var5.hasMoreTokens()) {
               String var6 = var5.nextToken();
               if (var6 != null && var6.length() != 0) {
                  String var7 = null;
                  String var8 = null;
                  int var9 = var6.indexOf(64);
                  if (var9 == -1) {
                     throw new DeployerException("SubModuleTargets must have atleast one '@' symbol");
                  }

                  var7 = var6.substring(0, var9);
                  int var10 = var6.indexOf(64, var9 + 1);
                  if (var10 == -1) {
                     var6 = var6.substring(var9 + 1);
                  } else {
                     var8 = var6.substring(var9 + 1, var10);
                     var6 = var6.substring(var10 + 1);
                  }

                  var1.addSubModuleTarget(var8, var7, new String[]{var6});
               }
            }

         }
      }
   }

   private String getSourceFromOpts() {
      String var1 = this.opts.getOption("source");
      int var2 = this.getActionFromOpts(false);
      String[] var3;
      if (var1 == null) {
         if (11 == var2 || 6 == var2) {
            var3 = this.opts.args();
            if (var3 != null && var3.length > 1) {
               throw new IllegalArgumentException(messageFormatter.errorMultipleSourceSpecified());
            }

            if (var3 != null && var3.length == 1) {
               var1 = var3[0];
               this.isSourceFromOpts = true;
            }
         }
      } else {
         switch (var2) {
            case 0:
               break;
            case 6:
            case 11:
               var3 = this.opts.args();
               if (var3 != null && var3.length > 0) {
                  throw new IllegalArgumentException(messageFormatter.errorFilesIllegal());
               }
               break;
            case 9:
               var3 = this.opts.args();
               if (var3 != null && var3.length > 0) {
                  throw new IllegalArgumentException(messageFormatter.errorFilesIllegalWithSource());
               }
               break;
            default:
               throw new IllegalArgumentException(messageFormatter.errorSourceIllegal());
         }
      }

      return var1;
   }

   private void listApps(DeployerHelper var1) {
      this.printApps(var1.getAppDeployments());
      System.exit(0);
   }

   private void printApps(List var1) {
      int var2 = var1.size();
      if (var2 == 0) {
         System.out.println(messageFormatter.noAppToList());
      } else {
         StringBuffer var3 = new StringBuffer();
         var3.append("\n").append(messageFormatter.appsFound()).append(" ");
         var3.append(var2);
         var3.append("\n");
         String[] var4 = new String[var2];
         var4 = (String[])((String[])var1.toArray(var4));

         for(int var5 = 0; var5 < var4.length; ++var5) {
            if (var5 != 0) {
               var3.append("\n");
            }

            var3.append("\t").append(var4[var5]);
         }

         var3.append("\n");
         System.out.println(var3.toString());
      }
   }

   private void processCancel(DeploymentTaskRuntimeMBean var1) {
      String var2 = var1.getId();

      while(true) {
         while(var1.isRunning()) {
         }

         int var3 = var1.getCancelState();
         if (var3 != 0 && var3 != 2 && var3 != 4) {
            System.out.println(messageFormatter.cancelSucceeded(var2));
         } else {
            System.out.println(messageFormatter.cancelFailed(var2));
         }

         System.exit(0);
      }
   }

   private void rejectFileListWithSourceArgument(String[] var1) throws IllegalArgumentException {
      if (this.opts.getOption("source") != null) {
         if (var1 != null && var1.length > 0) {
            throw new IllegalArgumentException(messageFormatter.errorFilesIllegal());
         }
      }
   }

   private boolean hasFiles(DeploymentData var1) {
      return var1.getFiles() != null && var1.getFiles().length > 0;
   }

   private static void print(String var0) {
      System.out.print(var0);
   }

   private static void println(String var0) {
      System.out.println(var0);
   }

   private static void debug(String var0) {
      if (debug) {
         System.out.println(var0);
      }

   }

   private static void inform(String var0) {
      if (verbose) {
         System.out.println(var0);
      }

   }
}
