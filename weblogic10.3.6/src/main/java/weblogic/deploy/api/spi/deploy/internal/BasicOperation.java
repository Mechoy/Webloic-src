package weblogic.deploy.api.spi.deploy.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import javax.enterprise.deploy.shared.CommandType;
import javax.enterprise.deploy.shared.ModuleType;
import javax.enterprise.deploy.spi.Target;
import javax.enterprise.deploy.spi.TargetModuleID;
import javax.enterprise.deploy.spi.status.ProgressObject;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServerConnection;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.deploy.api.internal.SPIDeployerLogger;
import weblogic.deploy.api.internal.utils.ConfigHelper;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.internal.utils.InstallDir;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.deploy.api.spi.DeploymentOptions;
import weblogic.deploy.api.spi.WebLogicDeploymentManager;
import weblogic.deploy.api.spi.WebLogicTargetModuleID;
import weblogic.deploy.api.spi.config.DescriptorParser;
import weblogic.deploy.api.spi.deploy.TargetModuleIDImpl;
import weblogic.deploy.api.spi.exceptions.ServerConnectionException;
import weblogic.deploy.api.spi.status.ProgressObjectImpl;
import weblogic.deploy.internal.DeployerTextFormatter;
import weblogic.deploy.utils.TaskCompletionNotificationFilter;
import weblogic.deploy.utils.TaskCompletionNotificationListener;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.ModuleOverrideBean;
import weblogic.management.ManagementException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.deploy.DeploymentData;
import weblogic.management.runtime.DeploymentTaskRuntimeMBean;
import weblogic.utils.FileUtils;

public abstract class BasicOperation {
   protected static final boolean debug = Debug.isDebug("deploy");
   protected static final DeployerTextFormatter cat = new DeployerTextFormatter();
   private static final String PREFIX = "app";
   protected WebLogicDeploymentManager dm;
   protected Target[] targetList;
   protected File moduleArchive;
   protected File plan;
   protected DeploymentOptions options;
   protected ProgressObject po;
   protected CommandType cmd;
   protected DeploymentPlanBean planBean;
   protected InstallDir paths;
   protected DeploymentTaskRuntimeMBean task;
   protected String appName;
   protected DeploymentData info;
   protected TargetModuleID[] tmids;
   protected InputStream moduleStream;
   protected InputStream planStream;
   protected boolean initWithTmids;
   protected ModuleType distributeStreamModuleType;
   private TaskCompletionNotificationListener taskCompletionListener;
   protected boolean tmidsFromConfig;
   protected boolean isWLFullClient;

   protected BasicOperation() {
      this.po = null;
      this.initWithTmids = true;
      this.distributeStreamModuleType = null;
      this.taskCompletionListener = null;
      this.tmidsFromConfig = false;
      this.isWLFullClient = false;
   }

   protected BasicOperation(WebLogicDeploymentManager var1, DeploymentOptions var2) {
      this.po = null;
      this.initWithTmids = true;
      this.distributeStreamModuleType = null;
      this.taskCompletionListener = null;
      this.tmidsFromConfig = false;
      this.isWLFullClient = false;
      this.dm = var1;
      this.options = var2;
      if (this.options == null) {
         this.options = new DeploymentOptions();
      }

   }

   protected BasicOperation(WebLogicDeploymentManager var1, File var2, File var3, DeploymentOptions var4) {
      this(var1, var4);
      if (var2 != null && var2.getPath().length() == 0) {
         var2 = null;
      }

      if (var3 != null && var3.getPath().length() == 0) {
         var3 = null;
      }

      this.moduleArchive = var2;
      this.plan = var3;
   }

   protected BasicOperation(WebLogicDeploymentManager var1, InputStream var2, InputStream var3, DeploymentOptions var4) {
      this(var1, var4);
      this.moduleStream = var2;
      this.planStream = var3;
   }

   protected TargetModuleID[] createTmidsFromTargets() {
      this.initWithTmids = false;
      HashSet var1 = new HashSet();
      this.deriveAppName();

      for(int var2 = 0; var2 < this.targetList.length; ++var2) {
         Target var3 = this.targetList[var2];
         var1.add(this.dm.createTargetModuleID((String)this.appName, (ModuleType)WebLogicModuleType.UNKNOWN, (Target)var3));
      }

      return (TargetModuleID[])((TargetModuleID[])var1.toArray(new TargetModuleID[0]));
   }

   protected void deriveAppName() {
      if (this.initWithTmids && (this.options == null || !this.getNameFromAppFiles())) {
         try {
            this.appName = ConfigHelper.getAppName(this.tmids, this.options);
         } catch (Exception var2) {
         }
      }

      if (this.appName == null) {
         this.appName = ConfigHelper.getAppName(this.options, this.moduleArchive, this.planBean);
      }

      if (debug) {
         Debug.say("appname established as: " + this.appName);
      }

      if (this.appName == null) {
         throw new IllegalArgumentException(SPIDeployerLogger.nullAppName(this.cmd.toString()));
      }
   }

   private boolean getNameFromAppFiles() {
      return this.options.isLibrary() && this.options.isNameFromLibrary() || this.options.isNameFromSource();
   }

   public synchronized ProgressObject run() throws IllegalStateException {
      if (this.po != null) {
         return this.po;
      } else {
         try {
            this.checkConnection();
            this.validateParams();

            try {
               Class var1 = Class.forName("weblogic.j2ee.descriptor.wl.DeploymentPlanBeanImpl");
            } catch (ClassNotFoundException var2) {
               this.isWLFullClient = true;
            }

            if (!this.isWLFullClient && !this.options.isRemote()) {
               this.parsePlan();
            }

            this.initTmids();
            this.setupPaths();
            this.execute();
            this.po = this.createResults();
         } catch (FailedOperationException var3) {
            this.po = var3.getProgressObject();
         }

         return this.po;
      }
   }

   protected void logRequest() {
      String var1;
      if (this.tmids != null) {
         var1 = this.getTargetsFromTmids();
      } else {
         var1 = this.getTargetsFromTargets();
      }

      if (var1.length() == 0) {
         var1 = SPIDeployerLogger.getConfiguredTargetsLoggable().getMessageText();
      }

      String var2 = null;
      if (this.moduleStream != null) {
         SPIDeployerLogger.logInitStreamOperation(this.cmd.toString(), this.appName, var1);
      } else {
         if (this.moduleArchive != null) {
            var2 = this.moduleArchive.getPath();
         }

         SPIDeployerLogger.logInitOperation(this.cmd.toString(), this.appName, var2, var1);
      }

   }

   private String getTargetsFromTargets() {
      String var1 = "";

      for(int var2 = 0; var2 < this.targetList.length; ++var2) {
         var1 = var1 + this.targetList[var2].getName() + " ";
      }

      return var1;
   }

   private String getTargetsFromTmids() {
      String var1 = "";

      for(int var2 = 0; var2 < this.tmids.length; ++var2) {
         var1 = var1 + this.tmids[var2].getTarget().getName() + " ";
      }

      return var1;
   }

   protected void checkConnection() {
      if (!this.dm.isConnected()) {
         throw new IllegalStateException(SPIDeployerLogger.notConnected());
      }
   }

   protected void initTmids() {
      if (this.tmids == null) {
         this.tmids = this.createTmidsFromTargets();
      } else {
         this.targetList = this.createTargetsFromTmids();
      }

      this.dumpTmids(this.tmids);
   }

   protected void validateParams() throws FailedOperationException {
      try {
         if (this.tmids == null && this.targetList == null) {
            throw new IllegalArgumentException(SPIDeployerLogger.nullTMID(this.cmd.toString()));
         }
      } catch (IllegalArgumentException var2) {
         throw new FailedOperationException(this.failOperation(var2));
      }
   }

   protected void parsePlan() throws FailedOperationException {
      if (this.planBean == null) {
         Object var1 = null;

         try {
            if (this.planStream != null) {
               var1 = this.planStream;
            } else if (this.plan != null) {
               var1 = new FileInputStream(this.plan);
            }

            if (var1 != null) {
               this.planBean = DescriptorParser.parseDeploymentPlan((InputStream)var1);
            }
         } catch (IOException var11) {
            throw new FailedOperationException(this.failOperation(var11));
         } finally {
            try {
               if (var1 != null && this.planStream == null) {
                  ((InputStream)var1).close();
               }
            } catch (IOException var10) {
            }

         }

      }
   }

   protected void setupPaths() throws FailedOperationException {
      try {
         if (this.moduleArchive != null) {
            if (!this.options.isRemote()) {
               this.moduleArchive = ConfigHelper.normalize(this.moduleArchive).getAbsoluteFile();
            }

            this.paths = new InstallDir(ConfigHelper.getAppName(this.options, this.moduleArchive, this.planBean), ConfigHelper.getAppRootFromPlan(this.planBean), false);
            this.paths.setArchive(this.moduleArchive);
            if (this.plan != null) {
               this.paths.setPlan(this.plan.getAbsoluteFile());
            }

            ConfigHelper.initPlanDirFromPlan(this.planBean, this.paths);
            if (this.planBean != null && this.planBean.getConfigRoot() != null) {
               this.paths.setConfigDir((new File(this.planBean.getConfigRoot())).getAbsoluteFile());
            }

         }
      } catch (IOException var2) {
         throw new FailedOperationException(this.failOperation(var2));
      }
   }

   protected ProgressObject createResults() {
      try {
         ProgressObjectImpl var1 = this.newProgressObject();
         return var1;
      } catch (ServerConnectionException var2) {
         return this.failOperation(var2);
      }
   }

   protected final void startTask() throws ManagementException {
      if (!this.task.isPendingActivation()) {
         this.task.start();
      }

   }

   protected abstract void initializeTask() throws Throwable;

   protected void buildDeploymentData() {
      this.info = this.createDeploymentData();
   }

   protected String getAppIdForUpload() {
      String var1 = this.options.getVersionIdentifier();
      if (var1 == null) {
         var1 = this.info != null && this.info.getDeploymentOptions() != null && this.info.getDeploymentOptions().isLibrary() ? ApplicationVersionUtils.getLibVersionId(this.paths.getArchive().getPath()) : ApplicationVersionUtils.getManifestVersion(this.paths.getArchive().getPath());
      }

      return ApplicationVersionUtils.getApplicationId(this.appName, var1);
   }

   protected void uploadFiles() throws IOException {
      if (this.paths != null) {
         String var1 = this.getAppIdForUpload();
         this.paths = this.dm.getServerConnection().upload(this.paths, var1, (String[])null);
      }
   }

   protected ProgressObject failOperation(Throwable var1) {
      ProgressObjectImpl var2;
      if (var1 instanceof ServerConnectionException) {
         var2 = new ProgressObjectImpl(this.cmd, ((ServerConnectionException)var1).getRootCause(), this.dm);
         var2.setMessage(var1.toString());
      } else {
         var2 = new ProgressObjectImpl(this.cmd, var1, this.dm);
      }

      return var2;
   }

   protected DeploymentData getDeploymentData() {
      if (this.options == null) {
         this.options = new DeploymentOptions();
      }

      DeploymentData var1 = new DeploymentData();

      for(int var2 = 0; var2 < this.targetList.length; ++var2) {
         if (debug) {
            Debug.say("adding global target: " + this.targetList[var2].getName());
         }

         var1.addGlobalTarget(this.targetList[var2].getName());
      }

      this.loadGeneralOptions(var1, this.options.getName());
      return var1;
   }

   protected void validateForNoTMIDs() {
      if (this.tmids == null || this.tmids.length == 0) {
         throw new IllegalArgumentException(SPIDeployerLogger.noTargetInfo(this.appName));
      }
   }

   ProgressObjectImpl newProgressObject() throws ServerConnectionException {
      if (this.defaultTmidsWithOnlySubModules()) {
         this.createTmidForAdminServer();
      }

      if (this.tmids.length == 0) {
         this.createTmidsFromApp();
      }

      ProgressObjectImpl var1 = new ProgressObjectImpl(this.cmd, this.task.getId(), this.tmids, this.dm);
      var1.setTaskCompletionListener(this.taskCompletionListener);
      this.dm.getServerConnection().registerListener(var1);
      List var2 = this.task.getTaskMessages();
      StringBuffer var3 = new StringBuffer();
      Iterator var4 = var2.iterator();

      while(var4.hasNext()) {
         Object var5 = var4.next();
         var3.append(var5).append("\n");
      }

      if (var3.length() > 0) {
         var1.setMessage(var3.toString());
      }

      return var1;
   }

   private void createTmidsFromApp() {
      AppDeploymentMBean var1 = ApplicationVersionUtils.getAppDeployment(this.dm.getHelper().getDomain(), this.appName, (String)null);
      if (var1 != null) {
         List var2 = this.dm.getServerConnection().getModules(var1);
         this.tmids = (TargetModuleID[])((TargetModuleID[])var2.toArray(new TargetModuleID[0]));
         this.tmidsFromConfig = true;
      } else {
         this.createTmidForAdminServer();
      }

   }

   private void createTmidForAdminServer() throws IllegalArgumentException {
      TargetModuleID[] var1 = new TargetModuleID[this.tmids.length + 1];
      if (0 != this.tmids.length) {
         System.arraycopy(this.tmids, 0, var1, 1, this.tmids.length);
      }

      String var2 = this.dm.getHelper().getAdminServerName();
      Target var3 = this.dm.getTarget(var2);
      if (var3 == null) {
         throw new IllegalArgumentException(cat.errorNoSuchTarget(var2));
      } else {
         var1[0] = this.dm.createTargetModuleID((String)this.appName, (ModuleType)WebLogicModuleType.UNKNOWN, (Target)var3);
         this.tmids = var1;
      }
   }

   protected void execute() throws FailedOperationException {
      try {
         this.deriveAppName();
         this.logRequest();
         if (this.defaultTmidsWithOnlySubModules()) {
            this.createTmidForAdminServer();
         }

         if (this.tmids.length == 0) {
            this.createTmidsFromApp();
         }

         this.validateForNoTMIDs();
         if (debug) {
            this.dumpTmids(this.tmids);
         }

         this.updateOptions();
         this.buildDeploymentData();
         this.uploadFiles();
         if (this.paths != null) {
            if (this.paths.getPlan() != null) {
               this.info.setDeploymentPlan(this.paths.getPlan().getPath());
            }

            this.info.setConfigDirectory(this.paths.getConfigDir().getPath());
            this.info.setRootDirectory(this.paths.getInstallDir().getPath());
         }

         if (debug) {
            Debug.say("Initiating " + this.cmd.toString() + " operation for app, " + this.appName + ", on targets:");

            for(int var1 = 0; var1 < this.targetList.length; ++var1) {
               Debug.say("   " + this.targetList[var1].getName());
            }
         }

         this.initializeTask();
         this.initializeNotifiers();
         this.startTask();
      } catch (ManagementException var2) {
         throw new FailedOperationException(this.failOperation(ManagementException.unWrapExceptions(var2)));
      } catch (FailedOperationException var3) {
         throw var3;
      } catch (Throwable var4) {
         throw new FailedOperationException(this.failOperation(var4));
      }
   }

   protected void updateOptions() {
      if (this.options.usesNonExclusiveLock()) {
         this.options.setUseNonexclusiveLock(this.dm.getHelper().needsNonExclusiveLock());
      }

   }

   private TargetMBean[] getTargetMBeans() {
      this.targetList = this.createTargetsFromTmids();
      HashSet var1 = new HashSet();
      TargetMBean[] var2 = this.dm.getHelper().getDomain().getTargets();

      for(int var3 = 0; var3 < this.targetList.length; ++var3) {
         Target var4 = this.targetList[var3];
         TargetMBean var5 = this.findTarget(var4, var2);
         if (var5 == null) {
            throw new IllegalArgumentException(SPIDeployerLogger.noSuchTarget(var4.getName(), var4.getDescription()));
         }

         var1.add(var5);
      }

      return (TargetMBean[])((TargetMBean[])var1.toArray(new TargetMBean[0]));
   }

   private TargetMBean findTarget(Target var1, TargetMBean[] var2) {
      for(int var3 = 0; var3 < var2.length; ++var3) {
         TargetMBean var4 = var2[var3];
         if (var4.getName().equals(var1.getName())) {
            return var4;
         }
      }

      return null;
   }

   private boolean isTargetedSubmodule(WebLogicTargetModuleID var1) {
      if (var1.getValue() == WebLogicModuleType.SUBMODULE.getValue()) {
         return var1.isTargeted();
      } else {
         TargetModuleID[] var2 = var1.getChildTargetModuleID();
         if (var2 != null) {
            for(int var3 = 0; var3 < var2.length; ++var3) {
               TargetModuleID var4 = var2[var3];
               if (this.isTargetedSubmodule((WebLogicTargetModuleID)var4)) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   protected boolean defaultTmidsWithOnlySubModules() {
      return false;
   }

   protected boolean tmidsHaveOnlySubModules() {
      boolean var1 = false;

      for(int var2 = 0; var2 < this.tmids.length; ++var2) {
         if (((WebLogicTargetModuleID)this.tmids[var2]).isTargeted()) {
            if (((WebLogicTargetModuleID)this.tmids[var2]).getValue() != WebLogicModuleType.SUBMODULE.getValue()) {
               return false;
            }

            var1 = true;
         }

         TargetModuleID[] var3 = this.tmids[var2].getChildTargetModuleID();
         if (null != var3) {
            for(int var4 = 0; var4 < var3.length; ++var4) {
               if (((WebLogicTargetModuleID)var3[var4]).isTargeted()) {
                  if (((WebLogicTargetModuleID)var3[var4]).getValue() != WebLogicModuleType.SUBMODULE.getValue()) {
                     return false;
                  }

                  var1 = true;
               }

               TargetModuleID[] var5 = var3[var4].getChildTargetModuleID();
               if (null != var5) {
                  for(int var6 = 0; var6 < var5.length; ++var6) {
                     if (((WebLogicTargetModuleID)var5[var6]).isTargeted()) {
                        if (((WebLogicTargetModuleID)var3[var4]).getValue() != WebLogicModuleType.SUBMODULE.getValue()) {
                           return false;
                        }

                        var1 = true;
                     }
                  }
               }
            }
         }
      }

      return var1;
   }

   protected Target[] createTargetsFromTmids() {
      if (this.tmids == null) {
         return null;
      } else {
         HashSet var1 = new HashSet();

         for(int var2 = 0; var2 < this.tmids.length; ++var2) {
            var1.add(this.tmids[var2].getTarget());
         }

         return (Target[])((Target[])var1.toArray(new Target[0]));
      }
   }

   protected DeploymentData createDeploymentData() {
      String var1 = null;
      if (this.options == null) {
         this.options = new DeploymentOptions();
      }

      DeploymentData var4 = new DeploymentData();

      TargetModuleIDImpl var2;
      for(int var5 = 0; var5 < this.tmids.length; ++var5) {
         var2 = (TargetModuleIDImpl)this.tmids[var5];
         if (var2.isTargeted()) {
            var1 = var2.getModuleID();
            var4.addGlobalTarget(var2.getTarget().getName());
         }
      }

      for(int var8 = 0; var8 < this.tmids.length; ++var8) {
         var2 = (TargetModuleIDImpl)this.tmids[var8];
         if (!var2.isTargeted()) {
            TargetModuleID[] var11 = var2.getChildTargetModuleID();
            if (var11 != null) {
               for(int var9 = 0; var9 < var11.length; ++var9) {
                  TargetModuleIDImpl var3 = (TargetModuleIDImpl)var11[var9];
                  if (var3.isTargeted()) {
                     if (var3.getValue() == WebLogicModuleType.SUBMODULE.getValue()) {
                        var4.addSubModuleTarget((String)null, var3.getModuleID(), new String[]{var3.getTarget().getName()});
                     } else {
                        var4.addModuleTarget(var3.getModuleID(), var3.getTarget().getName());
                     }
                  } else {
                     TargetModuleID[] var6 = var3.getChildTargetModuleID();
                     String var7 = var3.getModuleID();
                     if (var6 != null) {
                        for(int var10 = 0; var10 < var6.length; ++var10) {
                           var3 = (TargetModuleIDImpl)var6[var10];
                           if (var3.isTargeted()) {
                              var4.addSubModuleTarget(var7, var3.getModuleID(), new String[]{var3.getTarget().getName()});
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      this.loadGeneralOptions(var4, var1);
      return var4;
   }

   private void loadGeneralOptions(DeploymentData var1, String var2) {
      if (this.paths != null) {
         if (this.paths.getPlan() != null) {
            var1.setDeploymentPlan(this.paths.getPlan().getPath());
         }

         var1.setConfigDirectory(this.paths.getConfigDir().getPath());
         var1.setRootDirectory(this.paths.getInstallDir().getPath());
      }

      this.setVersionInfo(var2);
      var1.setDeploymentOptions(this.options);
      if (debug) {
         Debug.say(var1.toString());
      }

      var1.setTargetsFromConfig(this.tmidsFromConfig);
      if (this.options.getTimeout() != 0L) {
         var1.setTimeOut((int)this.options.getTimeout());
      }

      var1.setRemote(this.options.isRemote());
      var1.setThinClient(this.isWLFullClient);
   }

   private void setVersionInfo(String var1) {
      if (this.options.getPlanVersion() == null) {
         String var2 = null;
         String var3 = ApplicationVersionUtils.getVersionId(var1);
         if (var3 != null) {
            var2 = ApplicationVersionUtils.getPlanVersion(var3);
         }

         this.setPlanVersion(var2);
      }
   }

   private void setPlanVersion(String var1) {
      if (var1 != null) {
         this.options.setPlanVersion(var1);
      } else if (this.options.getPlanVersion() == null && this.planBean != null) {
         this.options.setPlanVersion(this.planBean.getVersion());
      }

      this.assertPlanVersionValid();
   }

   private void assertPlanVersionValid() {
      if (this.planBean != null) {
         String var1 = this.planBean.getVersion();
         String var2 = this.options.getPlanVersion();
         if (var1 == null || var2 == null) {
            return;
         }

         if (var1 != var2) {
            throw new IllegalArgumentException(SPIDeployerLogger.versionMismatchPlan(var1, var2));
         }
      }

   }

   protected List validateTmids() throws IllegalArgumentException, ServerConnectionException {
      String var2 = null;
      ArrayList var3 = new ArrayList();

      for(int var4 = 0; var4 < this.tmids.length; ++var4) {
         TargetModuleIDImpl var1 = (TargetModuleIDImpl)this.tmids[var4];
         if (var1.getParentTargetModuleID() != null) {
            throw new IllegalArgumentException(SPIDeployerLogger.notRootTMID(this.cmd.toString(), var1.toString()));
         }

         if (var2 == null) {
            var2 = ConfigHelper.getAppName(var1);
            if (debug) {
               Debug.say("Using app name " + var2);
            }

            if (var2 == null) {
               throw new IllegalArgumentException(SPIDeployerLogger.noAppForTMID(var1.toString()));
            }
         } else if (!var2.equals(ConfigHelper.getAppName(var1))) {
            throw new IllegalArgumentException(SPIDeployerLogger.diffTMID(this.cmd.toString(), var1.toString(), var2));
         }

         var3.add(var1);
      }

      return var3;
   }

   private void dumpTmids(TargetModuleID[] var1) {
      if (debug && var1 != null) {
         Debug.say("Incoming tmids:");

         for(int var2 = 0; var2 < var1.length; ++var2) {
            Debug.say("  " + var1[var2].toString() + ", targeted=" + ((TargetModuleIDImpl)var1[var2]).isTargeted());
            this.dumpChildTmids(var1[var2].getChildTargetModuleID(), "  ");
         }
      }

   }

   private void dumpChildTmids(TargetModuleID[] var1, String var2) {
      if (var1 != null) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            Debug.say("   " + var2 + var1[var3].toString() + ", targeted=" + ((TargetModuleIDImpl)var1[var3]).isTargeted());
            this.dumpChildTmids(var1[var3].getChildTargetModuleID(), "    ");
         }

      }
   }

   protected InstallDir createRootFromStreams(InputStream var1, DeploymentPlanBean var2, DeploymentOptions var3) throws IOException {
      File var6 = FileUtils.createTempDir("app");
      String var4 = ConfigHelper.getAppName(var3, var6, var2);
      InstallDir var5 = new InstallDir(var4, var6);
      var6 = var5.getAppDir();
      var6.mkdir();
      var5.setAppDir(var6.getAbsoluteFile());
      if (debug) {
         Debug.say("App dir set to " + var5.getAppDir().toString());
      }

      var4 = this.getArchiveName(var2, var4);
      var6 = new File(var6, var4);
      this.copy(var1, var6);
      ModuleType var7 = this.distributeStreamModuleType == null ? WebLogicModuleType.getFileModuleType(var6) : this.distributeStreamModuleType;
      if (var7 != null) {
         File var8;
         if (!var4.endsWith(var7.getModuleExtension())) {
            var4 = var4.concat(var7.getModuleExtension());
            if (debug) {
               Debug.say("renaming " + var6.getName() + " to " + var4);
            }

            var8 = new File(var5.getAppDir(), var4);
            if (!var6.renameTo(var8)) {
               if (debug) {
                  Debug.say("rename failed for file, " + var6.getAbsolutePath() + "/" + var4);
               }

               throw new IOException(SPIDeployerLogger.getRenameError(var4));
            }

            var6 = var8;
         }

         var5.setArchive(var6.getAbsoluteFile());
         if (debug) {
            Debug.say("Archive set to " + var5.getArchive().toString());
         }

         var6 = var5.getConfigDir();
         var6.mkdir();
         var8 = new File(var6, "plan.xml");
         if (debug) {
            Debug.say("using for plan  " + var8.toString());
         }

         if (var2 != null) {
            (new EditableDescriptorManager()).writeDescriptorAsXML(((DescriptorBean)var2).getDescriptor(), new FileOutputStream(var8));
            var5.setPlan(var8.getAbsoluteFile());
            File var9 = ConfigHelper.getConfigRootFile(var2);
            if (var9 != null) {
               var9.mkdir();
               var5.setConfigDir(var9.getAbsoluteFile());
            } else {
               var5.setConfigDir(var6.getAbsoluteFile());
            }
         } else {
            if (debug) {
               Debug.say("no plan");
            }

            var5.setConfigDir(var6.getAbsoluteFile());
            var5.setPlan(var8.getAbsoluteFile());
         }

         if (debug) {
            Debug.say("Plan dir to " + var5.getPlan().toString());
         }

         if (debug) {
            Debug.say("Config dir to " + var5.getConfigDir().toString());
         }

         return var5;
      } else {
         throw new IOException(SPIDeployerLogger.getUnknownType(var4));
      }
   }

   private String getArchiveName(DeploymentPlanBean var1, String var2) {
      if (var1 != null) {
         ModuleOverrideBean[] var3 = var1.getModuleOverrides();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            ModuleOverrideBean var5 = var3[var4];
            if (var1.rootModule(var5.getModuleName())) {
               var2 = var5.getModuleName();
               break;
            }
         }
      }

      return var2;
   }

   private void copy(InputStream var1, File var2) throws IOException {
      if (debug) {
         Debug.say("copying stream to " + var2.getName());
      }

      byte[] var3 = new byte[1024];
      FileOutputStream var5 = new FileOutputStream(var2);

      int var4;
      try {
         while((var4 = var1.read(var3)) > 0) {
            var5.write(var3, 0, var4);
         }
      } finally {
         var5.close();
      }

   }

   private void initializeNotifiers() {
      this.task.setNotificationLevel(1);
   }

   private void registerTaskCompletionListener() {
      if (this.task != null) {
         try {
            TaskCompletionNotificationListener var1 = new TaskCompletionNotificationListener(this.task);
            MBeanServerConnection var2 = this.dm.getServerConnection().getMBeanServerConnection();
            if (var2 != null) {
               MBeanServerConnection var3 = this.dm.getServerConnection().getRuntimeServerConnection();
               if (var3 == null) {
                  if (debug) {
                     Debug.say("Not adding TaskCompletionNotification since we do not have runtime server mbean connection");
                  }

                  return;
               }

               if (debug) {
                  Debug.say("Adding TaskCompletionNotification Listener : " + var1);
               }

               var3.addNotificationListener(this.task.getObjectName(), var1, new TaskCompletionNotificationFilter(), (Object)null);
               if (debug) {
                  Debug.say("Added TaskCompletionNotification Listener : " + var1);
               }
            }

            this.taskCompletionListener = var1;
         } catch (InstanceNotFoundException var4) {
            if (debug) {
               var4.printStackTrace();
            }
         } catch (IOException var5) {
            if (debug) {
               var5.printStackTrace();
            }
         }
      }

   }
}
