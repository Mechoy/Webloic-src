package weblogic.deploy.api.tools.deployer;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import weblogic.deploy.api.internal.utils.JMXDeployerHelper;
import weblogic.deploy.internal.DeployerTextFormatter;
import weblogic.deploy.utils.MBeanHomeTool;
import weblogic.management.ApplicationException;
import weblogic.management.DeploymentNotification;
import weblogic.management.ManagementException;
import weblogic.management.deploy.TargetStatus;
import weblogic.management.runtime.DeploymentTaskRuntimeMBean;
import weblogic.utils.Getopt2;
import weblogic.utils.StackTraceUtils;

public abstract class Operation {
   public transient MBeanHomeTool tool;
   public transient Options options;
   public static transient DeployerTextFormatter cat;
   public transient DeploymentTaskRuntimeMBean task;
   public transient String id;
   public transient int failures;
   public transient StringBuffer outstr;
   public transient JMXDeployerHelper helper;
   public transient HashSet allowedOptions;
   public transient PrintStream out;

   protected Operation(MBeanHomeTool var1, Options var2) {
      this(var2);
      this.tool = var1;
      this.setAllowedOptions();
   }

   protected Operation(Options var1) {
      this();
      this.options = var1;
   }

   protected Operation() {
      this.task = null;
      this.out = System.out;
      this.init();
   }

   protected void init() {
      cat = new DeployerTextFormatter();
      this.allowedOptions = new HashSet();
   }

   public void setOptions(Options var1) {
      this.options = var1;
   }

   public abstract void setAllowedOptions() throws IllegalArgumentException;

   public void validate() throws IllegalArgumentException, DeployerException {
      HashSet var1 = (HashSet)Options.allOptions.clone();
      var1.removeAll(this.allowedOptions);
      Iterator var2 = var1.iterator();
      Getopt2 var3 = this.options.getOpts();

      String var4;
      do {
         if (!var2.hasNext()) {
            return;
         }

         var4 = (String)var2.next();
      } while(!var3.hasOption(var4));

      throw new IllegalArgumentException(cat.errorOptionNotAllowed(var4, this.getOperation()));
   }

   public abstract void connect() throws DeployerException;

   public abstract void prepare() throws DeployerException;

   public abstract void execute() throws Exception;

   public abstract int report() throws DeployerException;

   public void debug(String var1) {
      if (this.options.debug) {
         this.println(var1);
      }

   }

   public void inform(String var1) {
      if (this.options.verbose) {
         this.println(var1);
      }

   }

   private int showRawTaskInfo(DeploymentTaskRuntimeMBean var1) {
      int var2 = 0;
      this.outstr = new StringBuffer();
      String var3 = this.getTaskStatus(var1);
      this.outstr.append(cat.allTaskStatus(var1.getId(), var3, var1.getDescription()));
      this.outstr.append("\n");
      TargetStatus[] var4 = var1.getTargets();
      if (var4 == null) {
         return var2;
      } else {
         for(int var9 = 0; var9 < var4.length; ++var9) {
            TargetStatus var5 = var4[var9];
            if (var5.getState() == 2) {
               ++var2;
            }

            String var6 = this.getTargetType(var5.getTargetType());
            String var7 = this.getTargetState(var5.getState());
            this.outstr.append(cat.showTargetState(var6, var5.getTarget(), var7, this.getOperation()));
            this.outstr.append("\n");
            Exception[] var8 = var5.getMessages();

            for(int var10 = 0; var10 < var8.length; ++var10) {
               this.outstr.append(this.translateDeploymentMessage(var8[var10]));
            }
         }

         this.outstr.append("\n");
         return var2;
      }
   }

   public int showTaskInformation(DeploymentTaskRuntimeMBean var1) {
      int var2;
      if (!this.options.formatted) {
         var2 = this.showRawTaskInfo(var1);
         this.print(this.outstr.toString());
         return var2;
      } else {
         var2 = 0;
         String var3 = var1.getId();
         String var4 = this.getOperation();
         String var5 = var1.getSource();
         String var6 = var1.getApplicationName();
         TargetStatus[] var7 = var1.getTargets();
         int var8 = var1.getState();
         if (var7.length == 0) {
            if (var8 != 2 && var8 != 4) {
               if (var8 == 3) {
                  Exception var18 = var1.getError();
                  String var19 = cat.noMessage();
                  if (var18 != null) {
                     var19 = var18.getMessage();
                  }

                  cat.errorNoRealTargets(var19);
                  return 1;
               } else {
                  cat.messageNoTargetsRunning();
                  return 0;
               }
            } else {
               cat.messageNoRealTargets();
               return 0;
            }
         } else {
            for(int var9 = 0; var9 < var7.length; ++var9) {
               TargetStatus var10 = var7[var9];
               String var11 = var10.getTarget();
               int var12 = var10.getType();
               int var13 = var10.getState();
               String var14 = this.translateTargetType(var12);
               String var15 = this.translateStatus(var13);
               this.print(var3 + "\t" + var4 + "\t" + var15 + "\t" + var11 + "\t" + var14 + "\t" + var6 + "\t" + var5 + "\t");
               if (var13 == 2) {
                  ++var2;
               }

               Exception[] var16 = var10.getMessages();

               for(int var17 = 0; var17 < var16.length && var16[var17] != null; ++var17) {
                  this.print(this.translateDeploymentMessage(var16[var17]));
               }

               this.println("");
            }

            return var2;
         }
      }
   }

   public abstract String getOperation();

   public void showTaskInformationHeader() {
      this.println("");
      this.println(cat.showListHeader());
   }

   private String getTaskStatus(DeploymentTaskRuntimeMBean var1) {
      int var2 = var1.getState();
      switch (var2) {
         case 0:
            return cat.stateInit();
         case 1:
            return cat.stateRunning();
         case 2:
            return cat.stateCompleted();
         case 3:
            return cat.stateFailed();
         case 4:
            return cat.stateDeferred();
         default:
            return cat.unknown();
      }
   }

   private String translateDeploymentMessage(Exception var1) {
      StringBuffer var2;
      String var7;
      Throwable var10;
      if (this.options.formatted) {
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
                  if (this.options.debug) {
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
            this.debug("dumping ApplicationException message");
            var2.append(var11.getApplicationMessage());
            var2.append("\n");
            Hashtable var12 = var11.getModuleErrors();
            if (var12 != null && var12.size() != 0) {
               Iterator var13 = var12.keySet().iterator();

               while(var13.hasNext()) {
                  var7 = (String)var13.next();
                  String var14 = (String)var12.get(var7);
                  this.debug("dumping ModuleException message");
                  var2.append(cat.moduleException(var7, var14));
                  var2.append("\n");
                  if (this.options.debug) {
                     Exception var9 = var11.getTargetException(var7);
                     if (var9 != null) {
                        this.debug("dumping ModuleException stack");
                        var2.append(StackTraceUtils.throwable2StackTrace(var9));
                        var2.append("\n");
                     }
                  }
               }
            }
         } else if (this.options.debug) {
            this.debug("dumping Exception stack");
            var2.append(StackTraceUtils.throwable2StackTrace(var10));
            var2.append("\n");
         } else {
            var2.append(var10.toString() + "\n");
            if (var10.getMessage() == null) {
               var2.append(StackTraceUtils.throwable2StackTrace(var10));
            }

            var2.append("\n");
         }
      }

      return var2.toString();
   }

   private String translateStatus(int var1) {
      switch (var1) {
         case 0:
            return cat.messageStateInit();
         case 1:
            return cat.messageStateInProgress();
         case 2:
            return cat.messageStateFailed();
         case 3:
            return cat.messageStateSuccess();
         case 4:
            return cat.messageStateDeferred();
         default:
            return null;
      }
   }

   private String translateTargetType(int var1) {
      if (var1 == 1) {
         return cat.messageServer();
      } else {
         return var1 == 2 ? cat.messageCluster() : cat.messageUnknown();
      }
   }

   private String getTargetState(int var1) {
      switch (var1) {
         case 0:
            return cat.stateInit();
         case 1:
            return cat.stateRunning();
         case 2:
            return cat.stateFailed();
         case 3:
            return cat.stateCompleted();
         case 4:
            return cat.stateDeferred();
         default:
            return cat.unknown();
      }
   }

   private String getTargetType(int var1) {
      switch (var1) {
         case 1:
            return cat.messageServer();
         case 2:
            return cat.messageCluster();
         case 3:
            return cat.messageJMSServer();
         case 4:
            return cat.messageHost();
         case 5:
            return cat.messageSAFAgent();
         default:
            return cat.unknown();
      }
   }

   public void showDeploymentNotificationInformation(String var1, DeploymentNotification var2) {
      String var3;
      if (this.options.formatted) {
         var3 = this.translateNotificationType(var2.getPhase());
         if (var2.isAppNotification()) {
            this.println(cat.showDeploymentNotification(var1, var3, var2.getAppName(), var2.getServerName()));
         }
      } else {
         var3 = var2.getAppName();
         String var4 = var2.getServerName();
         String var5 = null;
         String var6 = null;
         String var7;
         if (var2.isModuleNotification()) {
            var6 = var2.getModuleName();
            var7 = var2.getCurrentState();
            String var8 = var2.getTargetState();
            String var9 = var2.getTransition();
            if (var9.equals("end")) {
               var5 = cat.successfulTransition(var6, var7, var8, var4);
            } else if (var9.equals("failed")) {
               var5 = cat.failedTransition(var6, var7, var8, var4);
            }

            if (var5 != null) {
               this.println(var5);
            }
         } else {
            var7 = var2.getPhase();
            this.println(cat.appNotification(var3, var4, var7));
         }
      }

   }

   private String translateNotificationType(String var1) {
      if ("activated".equals(var1)) {
         return cat.messageNotificationActivated();
      } else if ("activating".equals(var1)) {
         return cat.messageNotificationActivating();
      } else if ("deactivated".equals(var1)) {
         return cat.messageNotificationDeactivated();
      } else if ("deactivating".equals(var1)) {
         return cat.messageNotificationDeactivating();
      } else if ("prepared".equals(var1)) {
         return cat.messageNotificationPrepared();
      } else if ("preparing".equals(var1)) {
         return cat.messageNotificationPreparing();
      } else if ("unprepared".equals(var1)) {
         return cat.messageNotificationUnprepared();
      } else if ("unpreparing".equals(var1)) {
         return cat.messageNotificationUnpreparing();
      } else if ("distributing".equals(var1)) {
         return cat.messageNotificationDistributing();
      } else if ("distributed".equals(var1)) {
         return cat.messageNotificationDistributed();
      } else {
         return "failed".equals(var1) ? cat.messageNotificationFailed() : var1;
      }
   }

   public void cleanUp() {
   }

   public void setOut(PrintStream var1) {
      this.out = var1;
   }

   public PrintStream getOut() {
      return this.out;
   }

   public void println(String var1) {
      this.out.println(var1);
   }

   public void print(String var1) {
      this.out.print(var1);
   }
}
