package weblogic.deploy.api.tools.samples;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.enterprise.deploy.shared.CommandType;
import javax.enterprise.deploy.shared.ModuleType;
import javax.enterprise.deploy.shared.StateType;
import javax.enterprise.deploy.shared.factories.DeploymentFactoryManager;
import javax.enterprise.deploy.spi.DeploymentManager;
import javax.enterprise.deploy.spi.Target;
import javax.enterprise.deploy.spi.TargetModuleID;
import javax.enterprise.deploy.spi.exceptions.TargetException;
import javax.enterprise.deploy.spi.factories.DeploymentFactory;
import javax.enterprise.deploy.spi.status.DeploymentStatus;
import javax.enterprise.deploy.spi.status.ProgressEvent;
import javax.enterprise.deploy.spi.status.ProgressListener;
import javax.enterprise.deploy.spi.status.ProgressObject;
import weblogic.deploy.api.shared.WebLogicCommandType;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.deploy.api.spi.DeploymentOptions;
import weblogic.deploy.api.spi.WebLogicDeploymentManager;
import weblogic.deploy.api.spi.WebLogicTargetModuleID;
import weblogic.deploy.api.spi.factories.WebLogicDeploymentFactory;

public class J2EEDeployer {
   private Config cfg;
   private J2EEDeployerToolTextFormatter cat;
   private boolean failed = false;
   private DeploymentOptions options;
   private int indent = 0;

   public static void main(String[] var0) {
      try {
         J2EEDeployer var1 = new J2EEDeployer(new Config(var0, J2EEDeployer.class.getName()));
         var1.run();
      } catch (Exception var2) {
         System.err.println(var2.toString());
         if (var2 instanceof RuntimeException && !(var2 instanceof IllegalArgumentException)) {
            var2.printStackTrace();
         }

         System.exit(1);
      }

   }

   private J2EEDeployer(Config var1) {
      this.cfg = var1;
      this.cat = J2EEDeployerToolTextFormatter.getInstance();
   }

   private void fail() {
      System.exit(1);
   }

   private void dumpStack(Throwable var1) {
      var1.printStackTrace();

      for(Throwable var2 = var1.getCause(); var2 != null; var2 = var2.getCause()) {
         this.cfg.error("Nested cause:");
         var2.printStackTrace();
      }

   }

   private DeploymentOptions createOptions() {
      DeploymentOptions var1 = new DeploymentOptions();
      var1.setStageMode(this.cfg.stage);
      return var1;
   }

   private void run() throws IllegalArgumentException {
      this.options = this.createOptions();
      if (this.cfg.command.equalsIgnoreCase("listtypes")) {
         this.dumpModuleTypes();
      } else if (this.cfg.command.equalsIgnoreCase("listcommands")) {
         this.dumpCommandTypes();
      } else {
         this.cfg.inform(this.cat.registerFactory());
         WebLogicDeploymentFactory var1 = (WebLogicDeploymentFactory)this.registerFactory();

         try {
            this.cfg.inform(this.cat.getDM());
            WebLogicDeploymentManager var2 = (WebLogicDeploymentManager)var1.getDeploymentManager(this.getConnectedURI(var1), this.cfg.user, this.cfg.password);
            if (this.cfg.command.equalsIgnoreCase("listtargets")) {
               this.doListTargets(var2);
            } else {
               this.cfg.inform(this.cat.collectTargets());
               Target[] var3 = this.getTargets(var2, this.cfg.targets);
               if (this.cfg.command.equalsIgnoreCase("distribute")) {
                  this.doDistribute(var2, var3);
               } else if (this.cfg.command.equalsIgnoreCase("start")) {
                  this.doStart(var2, var3);
               } else if (this.cfg.command.equalsIgnoreCase("deploy")) {
                  this.doDeploy(var2, var3);
               } else if (this.cfg.command.equalsIgnoreCase("stop")) {
                  this.doStop(var2, var3);
               } else if (this.cfg.command.equalsIgnoreCase("undeploy")) {
                  this.doUndeploy(var2, var3);
               } else if (this.cfg.command.equalsIgnoreCase("listall")) {
                  this.doListAll(var2, var3);
               } else if (this.cfg.command.equalsIgnoreCase("liststarted")) {
                  this.doListStarted(var2, var3);
               } else if (this.cfg.command.equalsIgnoreCase("liststopped")) {
                  this.doListStopped(var2, var3);
               } else if (this.cfg.command.equalsIgnoreCase("redeploy")) {
                  this.doRedeploy(var2, var3);
               } else if (this.cfg.command.equalsIgnoreCase("update")) {
                  this.doUpdate(var2, var3);
               }
            }
         } catch (Exception var4) {
            this.cfg.error(var4.toString());
            if (var4 instanceof RuntimeException || this.cfg.debug) {
               this.dumpStack(var4);
            }

            this.fail();
         }

         if (this.failed) {
            this.cfg.inform("Exiting with failed status");
            this.fail();
         }

      }
   }

   private String getConnectedURI(WebLogicDeploymentFactory var1) {
      byte var2;
      if (this.cfg.remote) {
         var2 = 1;
      } else {
         var2 = 0;
      }

      return var1.createUri(var1.getUris()[var2], this.cfg.host, this.cfg.port);
   }

   private DeploymentFactory registerFactory() throws IllegalArgumentException {
      try {
         Class var1 = Class.forName(this.cfg.factoryClassName);
         if (DeploymentFactory.class.isAssignableFrom(var1)) {
            DeploymentFactory var2 = (DeploymentFactory)var1.newInstance();
            DeploymentFactoryManager.getInstance().registerDeploymentFactory(var2);
            this.cfg.inform(this.cat.registeredFactory(this.cfg.factoryClassName));
            return var2;
         } else {
            throw new IllegalArgumentException(this.cfg.error(this.cat.invalidFactory(this.cfg.factoryClassName)));
         }
      } catch (ClassNotFoundException var3) {
         if (this.cfg.debug) {
            this.dumpStack(var3);
         }

         throw new IllegalArgumentException(this.cfg.error(this.cat.invalidFactoryException(this.cfg.factoryClassName, var3.toString())));
      } catch (InstantiationException var4) {
         if (this.cfg.debug) {
            this.dumpStack(var4);
         }

         throw new IllegalArgumentException(this.cfg.error(this.cat.invalidFactoryException(this.cfg.factoryClassName, var4.toString())));
      } catch (IllegalAccessException var5) {
         if (this.cfg.debug) {
            this.dumpStack(var5);
         }

         throw new IllegalArgumentException(this.cfg.error(this.cat.invalidFactoryException(this.cfg.factoryClassName, var5.toString())));
      }
   }

   private Target[] getTargets(DeploymentManager var1, Set var2) throws IllegalStateException {
      Target[] var4 = var1.getTargets();
      int var5;
      if (this.cfg.debug) {
         this.cfg.print("Configured targets:");

         for(var5 = 0; var5 < var4.length; ++var5) {
            this.cfg.print("  " + var4[var5].getName() + "(" + var4[var5].getDescription() + ")");
         }
      }

      if (var2.isEmpty()) {
         return var4;
      } else {
         ArrayList var3 = new ArrayList();

         for(var5 = 0; var5 < var4.length; ++var5) {
            if (var2.contains(var4[var5].getName())) {
               var3.add(var4[var5]);
               if (this.cfg.debug) {
                  this.cfg.print("Targeting " + var4[var5].getName() + "(" + var4[var5].getDescription() + ")");
               }
            }
         }

         if (var3.isEmpty()) {
            String var6 = this.cfg.error(this.cat.noValidTargets());
            this.cfg.dumpArgs();
            throw new IllegalArgumentException(var6);
         } else {
            if (var3.size() < var2.size()) {
            }

            return (Target[])((Target[])var3.toArray(new Target[0]));
         }
      }
   }

   private void doListTargets(DeploymentManager var1) {
      Target[] var2 = var1.getTargets();
      var2 = var1.getTargets();
      this.cfg.inform("Configured targets:");

      for(int var3 = 0; var3 < var2.length; ++var3) {
         this.cfg.print(var2[var3].toString());
      }

   }

   private void doDistribute(WebLogicDeploymentManager var1, Target[] var2) throws IllegalStateException {
      this.cfg.inform(this.cat.distribute(this.cfg.fileOrApp));
      boolean var4 = true;
      ProgressObject var3;
      if (var4) {
         try {
            var3 = var1.distribute(var2, (InputStream)(new FileInputStream(new File(this.cfg.fileOrApp))), (InputStream)(this.cfg.usePlan == null ? null : new FileInputStream(new File(this.cfg.usePlan))));
         } catch (Exception var9) {
            this.cfg.error(var9.toString());
            return;
         }
      } else {
         var3 = var1.distribute(var2, new File(this.cfg.fileOrApp), this.cfg.usePlan == null ? null : new File(this.cfg.usePlan));
      }

      this.wait(var3);
      this.results(var3);
      TargetModuleID[] var5 = new TargetModuleID[0];

      try {
         var5 = var1.getAvailableModules(this.cfg.moduleType, var2);
      } catch (TargetException var8) {
         var8.printStackTrace();
      }

      this.cfg.inform("+++++++++++++++++available modules:");

      for(int var6 = 0; var6 < var5.length; ++var6) {
         TargetModuleID var7 = var5[var6];
         this.cfg.inform(var7.toString());
      }

   }

   private void doDeploy(WebLogicDeploymentManager var1, Target[] var2) throws IllegalStateException {
      this.cfg.inform(this.cat.deploy(this.cfg.fileOrApp));

      try {
         Set var3 = this.getTmids(var2, this.cfg.appName, var1);
         if (var3 == null) {
            this.cfg.error(this.cat.noTmidsToDeploy(this.cfg.fileOrApp, this.cfg.moduleType.toString()));
            return;
         }

         ProgressObject var4 = var1.deploy((TargetModuleID[])((TargetModuleID[])var3.toArray(new TargetModuleID[0])), new File(this.cfg.fileOrApp), this.cfg.usePlan == null ? null : new File(this.cfg.usePlan), this.options);
         this.wait(var4);
         this.results(var4);
      } catch (TargetException var5) {
         this.cfg.error(var5.toString());
      }

   }

   private void doStart(WebLogicDeploymentManager var1, Target[] var2) throws IllegalStateException {
      this.cfg.inform(this.cat.start(this.cfg.fileOrApp));
      Set var4 = this.getTmids(var2, this.cfg.appName, var1);
      if (var4 == null) {
         this.cfg.error(this.cat.noTmidsToDeploy(this.cfg.fileOrApp, this.cfg.moduleType.toString()));
      } else {
         ProgressObject var3 = var1.start((TargetModuleID[])((TargetModuleID[])var4.toArray(new TargetModuleID[0])), this.options);
         this.wait(var3);
         this.results(var3);
      }
   }

   private void doRedeploy(WebLogicDeploymentManager var1, Target[] var2) throws IllegalStateException {
      this.cfg.inform(this.cat.redeploy(this.cfg.fileOrApp));
      Set var4 = this.getTmids(var2, this.cfg.appName, var1);
      if (var4.isEmpty()) {
         this.cfg.error(this.cat.noTmidsToDeploy(this.cfg.fileOrApp, this.cfg.moduleType.toString()));
      } else {
         ProgressObject var3;
         if (!this.cfg.delta.isEmpty()) {
            var3 = var1.redeploy((TargetModuleID[])((TargetModuleID[])var4.toArray(new TargetModuleID[0])), new File(this.cfg.fileOrApp), (String[])((String[])this.cfg.delta.toArray(new String[0])), this.options);
         } else {
            var3 = var1.redeploy((TargetModuleID[])((TargetModuleID[])var4.toArray(new TargetModuleID[0])), new File(this.cfg.fileOrApp), this.cfg.usePlan == null ? null : new File(this.cfg.usePlan), this.options);
         }

         this.wait(var3);
         this.results(var3);
      }
   }

   private void doUpdate(WebLogicDeploymentManager var1, Target[] var2) throws IllegalStateException {
      this.cfg.inform(this.cat.update(this.cfg.fileOrApp, this.cfg.usePlan));
      Set var4 = this.getTmids(var2, this.cfg.appName, var1);
      if (var4.isEmpty()) {
         this.cfg.error(this.cat.noTmidsToDeploy(this.cfg.fileOrApp, this.cfg.moduleType.toString()));
      } else {
         ProgressObject var3 = var1.update((TargetModuleID[])((TargetModuleID[])var4.toArray(new TargetModuleID[0])), new File(this.cfg.usePlan), this.options);
         this.wait(var3);
         this.results(var3);
      }
   }

   private Set getTmids(Target[] var1, String var2, WebLogicDeploymentManager var3) {
      String var4 = var2;
      if (var2 == null) {
         var4 = this.cfg.fileOrApp;
      }

      File var5 = new File(var4);
      var4 = var5.getName();
      if (this.cfg.debug) {
         this.cfg.inform("getting tmids for " + var4);
      }

      HashSet var6 = new HashSet();

      for(int var8 = 0; var8 < var1.length; ++var8) {
         Target var7 = var1[var8];
         WebLogicTargetModuleID var9 = var3.createTargetModuleID(var4, this.cfg.moduleType, var7);
         if (!this.cfg.modules.isEmpty()) {
            Iterator var10 = this.cfg.modules.iterator();

            while(var10.hasNext()) {
               String var11 = (String)var10.next();
               var9 = var3.createTargetModuleID((TargetModuleID)var9, (String)var11, (ModuleType)WebLogicModuleType.UNKNOWN);
               var6.add(var9);
            }
         } else {
            var6.add(var9);
         }
      }

      return var6;
   }

   private void doStop(WebLogicDeploymentManager var1, Target[] var2) throws IllegalStateException {
      this.cfg.inform(this.cat.stop(this.cfg.fileOrApp));
      Set var3 = this.getTmids(var2, this.cfg.appName, var1);
      if (var3 == null) {
         this.cfg.error(this.cat.noTmidsToDeploy(this.cfg.fileOrApp, this.cfg.moduleType.toString()));
      } else {
         ProgressObject var4 = var1.stop((TargetModuleID[])((TargetModuleID[])var3.toArray(new TargetModuleID[0])));
         this.wait(var4);
         this.results(var4);
      }
   }

   private void doUndeploy(WebLogicDeploymentManager var1, Target[] var2) throws IllegalStateException {
      this.cfg.inform(this.cat.undeploy(this.cfg.fileOrApp));
      Set var3 = this.getTmids(var2, this.cfg.appName, var1);
      if (var3 == null) {
         this.cfg.error(this.cat.noTmidsToDeploy(this.cfg.fileOrApp, this.cfg.moduleType.toString()));
      } else {
         ProgressObject var4;
         if (!this.cfg.delta.isEmpty()) {
            var4 = var1.undeploy((TargetModuleID[])((TargetModuleID[])var3.toArray(new TargetModuleID[0])), new File(this.cfg.fileOrApp), (String[])((String[])this.cfg.delta.toArray(new String[0])), this.options);
         } else {
            var4 = var1.undeploy((TargetModuleID[])((TargetModuleID[])var3.toArray(new TargetModuleID[0])));
         }

         this.wait(var4);
         this.results(var4);
      }
   }

   private void doListAll(DeploymentManager var1, Target[] var2) throws TargetException, IllegalStateException {
      this.cfg.inform(this.cat.listAll());
      TargetModuleID[] var3 = var1.getAvailableModules(this.cfg.moduleType, var2);
      this.showTMIDs(var3);
   }

   private void doListStopped(DeploymentManager var1, Target[] var2) throws TargetException, IllegalStateException {
      this.cfg.inform(this.cat.listStopped());
      TargetModuleID[] var3 = var1.getNonRunningModules(this.cfg.moduleType, var2);
      this.showTMIDs(var3);
   }

   private void doListStarted(DeploymentManager var1, Target[] var2) throws TargetException, IllegalStateException {
      this.cfg.inform(this.cat.listStarted());
      TargetModuleID[] var3 = var1.getRunningModules(this.cfg.moduleType, var2);
      this.showTMIDs(var3);
   }

   private void results(ProgressObject var1) {
      this.cfg.print("Modules processed:");
      this.showTMIDs(var1.getResultTargetModuleIDs());
   }

   private void showTMIDs(TargetModuleID[] var1) {
      String var2 = "";

      int var3;
      for(var3 = 0; var3 < this.indent; ++var3) {
         var2 = var2 + "   ";
      }

      if (this.indent == 0) {
         this.cfg.inform(this.cat.listHeader());
      }

      ++this.indent;
      if (var1 != null) {
         for(var3 = 0; var3 < var1.length; ++var3) {
            this.cfg.print(var2 + var1[var3].toString());
            TargetModuleID[] var4 = var1[var3].getChildTargetModuleID();
            if (var4 != null) {
               this.showTMIDs(var4);
            }
         }
      }

      --this.indent;
   }

   void wait(ProgressObject var1) {
      ProgressHandler var2 = new ProgressHandler();
      if (!var1.getDeploymentStatus().isRunning()) {
         this.showStatus(var1.getDeploymentStatus(), true);
         this.failed = var1.getDeploymentStatus().isFailed();
      } else {
         var1.addProgressListener(var2);
         var2.start();

         while(var2.getCompletionState() == null) {
            try {
               var2.join();
            } catch (InterruptedException var4) {
               if (!var2.isAlive()) {
                  break;
               }
            }
         }

         StateType var3 = var2.getCompletionState();
         this.failed = var3 == null || var3.getValue() == StateType.FAILED.getValue();
         var1.removeProgressListener(var2);
      }
   }

   void showStatus(DeploymentStatus var1, boolean var2) {
      if (var2 || !this.cfg.quiet) {
         String var3 = var1.getCommand().toString();
         String var4 = var1.getState().toString();
         String var5 = var1.getMessage();
         this.cfg.print(this.cat.showStatus(var3, var4, var5));
      }

   }

   private void dumpModuleTypes() {
      String[] var1 = WebLogicModuleType.JMS.getStringTable();
      ModuleType[] var2 = WebLogicModuleType.JMS.getEnumValueTable();
      this.cfg.print("Available types:");

      for(int var3 = 0; var3 < var1.length; ++var3) {
         this.cfg.print(var2[var3].getValue() + ":" + var1[var3]);
      }

   }

   private void dumpCommandTypes() {
      String[] var1 = WebLogicCommandType.DEPLOY.getStringTable();
      CommandType[] var2 = WebLogicCommandType.DEPLOY.getEnumValueTable();
      this.cfg.print("Available commands:");

      for(int var3 = 0; var3 < var1.length; ++var3) {
         this.cfg.print(var2[var3].getValue() + ":" + var1[var3]);
      }

   }

   class ProgressHandler extends Thread implements ProgressListener {
      boolean progressDone = false;
      StateType finalState = null;

      public void run() {
         while(!this.progressDone) {
            try {
               Thread.sleep(100L);
            } catch (InterruptedException var2) {
            }
         }

      }

      public void handleProgressEvent(ProgressEvent var1) {
         DeploymentStatus var2 = var1.getDeploymentStatus();
         TargetModuleID var3 = var1.getTargetModuleID();
         if (var3 != null) {
            J2EEDeployer.this.cfg.inform(J2EEDeployer.this.cat.progressTmid(var3.toString()));
         }

         J2EEDeployer.this.showStatus(var2, J2EEDeployer.this.cfg.verbose);
         if (var2.getState().getValue() != StateType.RUNNING.getValue()) {
            this.progressDone = true;
            this.finalState = var2.getState();
         }

      }

      public StateType getCompletionState() {
         return this.finalState;
      }
   }
}
