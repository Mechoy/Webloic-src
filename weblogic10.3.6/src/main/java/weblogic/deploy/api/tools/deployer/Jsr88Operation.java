package weblogic.deploy.api.tools.deployer;

import java.io.File;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.enterprise.deploy.shared.StateType;
import javax.enterprise.deploy.spi.Target;
import javax.enterprise.deploy.spi.TargetModuleID;
import javax.enterprise.deploy.spi.exceptions.DeploymentManagerCreationException;
import javax.enterprise.deploy.spi.status.DeploymentStatus;
import javax.enterprise.deploy.spi.status.ProgressObject;
import weblogic.deploy.api.internal.utils.ConfigHelper;
import weblogic.deploy.api.spi.DeploymentOptions;
import weblogic.deploy.api.spi.WebLogicDeploymentManager;
import weblogic.deploy.api.spi.status.ProgressObjectImpl;
import weblogic.deploy.api.spi.status.WebLogicDeploymentStatus;
import weblogic.deploy.api.tools.SessionHelper;
import weblogic.deploy.utils.MBeanHomeTool;
import weblogic.deploy.utils.MBeanHomeToolException;
import weblogic.deploy.utils.TaskCompletionNotificationListener;
import weblogic.management.ManagementException;
import weblogic.management.deploy.DeploymentData;
import weblogic.management.runtime.DeploymentTaskRuntimeMBean;
import weblogic.rmi.extensions.RemoteRuntimeException;

public abstract class Jsr88Operation extends Operation implements Serializable {
   private static final long serialVersionUID = 1L;
   private transient String id;
   protected transient WebLogicDeploymentManager dm;
   protected transient ProgressObject po;
   protected transient Target[] targets;
   protected transient File src = null;
   protected transient File plan = null;
   protected transient DeploymentOptions dOpts;
   protected transient ModuleTargetInfo[] targetInfos;
   protected transient List tmids;

   protected Jsr88Operation(MBeanHomeTool var1, Options var2) {
      super(var1, var2);
   }

   protected Jsr88Operation(Options var1) {
      super(var1);
   }

   protected Jsr88Operation() {
   }

   protected void init() {
      super.init();
      this.tmids = new ArrayList();
      this.dOpts = new DeploymentOptions();
      this.targets = new Target[0];
   }

   public void setAllowedOptions() throws IllegalArgumentException {
      this.allowedOptions.add("name");
      this.allowedOptions.add("id");
      this.allowedOptions.add("targets");
      this.allowedOptions.add("submoduletargets");
      this.allowedOptions.add("appversion");
      this.allowedOptions.add("planversion");
      this.allowedOptions.add("libspecver");
      this.allowedOptions.add("libimplver");
      this.allowedOptions.add("library");
   }

   public void validate() throws IllegalArgumentException, DeployerException {
      super.validate();
      this.validateSource();
      this.validateName();
      this.validateDelta();
      this.validateGraceful();
      this.validateRetireTimeout();
      this.validateAllVersions();
      this.validateAltDDs();
      this.validateDeleteFiles();
      this.validateVersions();
      this.validateLibVersions();
      this.validatePlan();
      this.validateTargets();
   }

   private void validateTargets() throws IllegalArgumentException {
   }

   protected void validatePlan() throws IllegalArgumentException {
      if (this.isPlanRequired() && this.options.plan == null) {
         throw new IllegalArgumentException(cat.errorNoPlan(this.getOperation()));
      } else {
         if (!this.options.remote && this.options.plan != null) {
            File var1 = new File(this.options.plan);
            if (!var1.exists()) {
               throw new IllegalArgumentException(cat.errorNoSuchFile(this.options.plan));
            }
         }

      }
   }

   protected void validateLibVersions() throws IllegalArgumentException {
      this.validateLibVersion(this.options.libSpecVersion);
   }

   private void validateLibVersion(String var1) throws IllegalArgumentException {
      if (var1 != null && !var1.matches("[0-9]+(\\.[0-9]+)*")) {
         throw new IllegalArgumentException(cat.invalidLibVersion(var1));
      }
   }

   protected void validateVersions() throws IllegalArgumentException {
      this.validateFileVersion(this.options.appVersion);
      this.validateFileVersion(this.options.planVersion);
      this.validateVersionNoVersion();
   }

   protected void validateVersionNoVersion() throws IllegalArgumentException {
      if (this.options.noVersion && (this.options.appVersion != null || this.options.planVersion != null)) {
         throw new IllegalArgumentException(cat.invalidVersionNoVersion());
      }
   }

   private void validateFileVersion(String var1) throws IllegalArgumentException {
      if (var1 != null && (var1.length() >= 215 || !var1.matches("[a-zA-Z0-9\\._-]*") || var1.equals(".") || var1.equals(".."))) {
         throw new IllegalArgumentException(cat.invalidFileVersion(this.options.name, var1));
      }
   }

   protected void validateName() throws IllegalArgumentException {
   }

   protected void validateSource() throws IllegalArgumentException {
      if (this.isSourceRequired()) {
         if (this.options.source == null) {
            throw new IllegalArgumentException(cat.errorNoSourceSpecified());
         }

         if (!this.options.remote) {
            File var1 = new File(this.options.source);
            if (!var1.exists()) {
               throw new IllegalArgumentException(cat.errorNoSuchFile(this.options.source));
            }
         }
      }

   }

   protected boolean isSourceRequired() {
      return this.options.upload;
   }

   protected boolean isPlanRequired() {
      return false;
   }

   protected void validateDelta() throws IllegalArgumentException {
      if (this.options.delta != null) {
         if (this.options.delta.length > 1) {
            throw new IllegalArgumentException(cat.errorMultipleSourceSpecified());
         }

         if (this.options.sourceFromOpts && this.options.delta.length > 0) {
            throw new IllegalArgumentException(cat.errorFilesIllegalWithSource());
         }
      }

   }

   protected void validateDeleteFiles() throws IllegalArgumentException {
      if (this.options.deleteFiles) {
         if (this.options.delta == null) {
            throw new IllegalArgumentException(cat.errorMissingDelta());
         }

         if (this.options.upload) {
            throw new IllegalArgumentException(cat.errorUploadDelete(this.getOperation()));
         }
      }

   }

   protected void validateAltDDs() {
      this.options.altAppDD = this.validateAltDD(this.options.altAppDD);
      this.options.altWlsAppDD = this.validateAltDD(this.options.altWlsAppDD);
   }

   private String validateAltDD(String var1) {
      if (var1 != null && !this.options.remote) {
         File var2 = new File(var1);
         if (!var2.exists() || var2.isDirectory()) {
            throw new IllegalArgumentException(cat.noSourceAltAppDD(var1));
         }

         if (var1.length() > 1 && var1.charAt(1) != ':') {
            var1 = var2.getAbsolutePath();
         }
      }

      return var1;
   }

   protected void validateRetireTimeout() {
      if (this.options.retireTimeout != -1 && this.options.libModule) {
         throw new IllegalArgumentException(cat.errorRetireTimeoutIllegal("retiretimeout"));
      }
   }

   protected void validateGraceful() throws IllegalArgumentException {
      if (!this.options.graceful && this.options.ignoreSessions) {
         throw new IllegalArgumentException(cat.errorIgnoreSessionsIllegal("graceful", "ignoresessions"));
      } else if (!this.options.redeployOp && !this.options.graceful && this.options.rmiGracePeriod != -1) {
         throw new IllegalArgumentException(cat.errorRMIGracePeriodIllegal("graceful", "rmigraceperiod"));
      } else if (this.options.graceful && this.options.retireTimeout != -1) {
         throw new IllegalArgumentException(cat.errorRetireTimeoutGracefulIllegal("retiretimeout", "graceful"));
      }
   }

   protected void validateAllVersions() throws IllegalArgumentException {
      if (this.options.allVersions) {
         if (!this.options.undeployOp) {
            throw new IllegalArgumentException(cat.errorAllVersionsIllegal1("undeploy", "allversions"));
         } else if (this.options.graceful) {
            throw new IllegalArgumentException(cat.errorAllVersionsIllegal2("graceful", "allversions"));
         } else if (this.options.appVersion != null) {
            throw new IllegalArgumentException(cat.errorAllVersionsIllegal2("appversion", "allversions"));
         }
      }
   }

   public void prepare() throws DeployerException {
      try {
         this.helper = this.dm.getHelper();
         this.src = this.prepareFile(this.options.source);
         this.plan = this.prepareFile(this.options.plan);
         this.prepareTargets();
         this.prepareTmids();
         this.prepareDeploymentOptions();
         if (this.options.id != null) {
            this.dm.setTaskId(this.options.id);
         }

      } catch (IllegalArgumentException var3) {
         DeployerException var2 = new DeployerException(var3.toString());
         var2.initCause(var3);
         throw var2;
      }
   }

   public void connect() throws DeployerException {
      URI var1 = this.getUri();

      DeployerException var3;
      try {
         this.tool.processUsernameAndPassword();
         if (this.options.upload) {
            this.dm = SessionHelper.getRemoteDeploymentManager(var1.getScheme(), var1.getHost(), (new Integer(var1.getPort())).toString(), this.tool.getUser(), this.tool.getPassword());
         } else {
            this.dm = SessionHelper.getDeploymentManager(var1.getScheme(), var1.getHost(), (new Integer(var1.getPort())).toString(), this.tool.getUser(), this.tool.getPassword());
         }

      } catch (MBeanHomeToolException var4) {
         var3 = new DeployerException(var4.toString());
         var3.initCause(var4);
         throw var3;
      } catch (DeploymentManagerCreationException var5) {
         var3 = new DeployerException(cat.errorUnableToAccessDeployer(var1.toString(), ManagementException.unWrapExceptions(var5).getMessage()));
         if (this.options.debug || this.options.verbose) {
            var3.initCause(var5);
         }

         throw var3;
      }
   }

   protected void prepareTmids() {
      String var1 = null;
      if (this.targetInfos.length > 0) {
         var1 = this.deriveName();
      }

      for(int var2 = 0; var2 < this.targetInfos.length; ++var2) {
         ModuleTargetInfo var3 = this.targetInfos[var2];
         this.tmids.add(var3.createTmid(var1, this.findTarget(var3, this.targets), this.dm));
      }

   }

   private String deriveName() {
      String var1 = this.options.name;
      if ((var1 == null || var1.length() == 0) && this.options.source != null) {
         var1 = this.getNameFromSource(this.options.source);
         this.dOpts.setNameFromSource(true);
      }

      return var1;
   }

   private String getNameFromSource(String var1) {
      if (var1 == null) {
         return null;
      } else {
         File var2 = new File(ConfigHelper.normalize(var1));
         if (var1.equals(".")) {
            var2 = (new File(var2.getAbsolutePath())).getParentFile();
         }

         return var2.getName();
      }
   }

   private void prepareDeploymentOptions() throws IllegalArgumentException {
      if (this.options.stageMode != null) {
         this.dOpts.setStageMode(this.options.stageMode);
      }

      this.dOpts.setTestMode(this.options.adminMode);
      this.dOpts.setGracefulProductionToAdmin(this.options.graceful);
      this.dOpts.setGracefulIgnoreSessions(this.options.ignoreSessions);
      this.dOpts.setRMIGracePeriodSecs(this.options.rmiGracePeriod);
      this.dOpts.setUndeployAllVersions(this.options.allVersions);
      this.dOpts.setSecurityValidationEnabled(this.options.securityValidation);
      if (this.options.securityModel != null) {
         this.dOpts.setSecurityModel(this.options.securityModel);
      }

      this.dOpts.setLibrary(this.options.libModule);
      if (this.options.appVersion != null) {
         this.dOpts.setArchiveVersion(this.options.appVersion);
      }

      if (this.options.planVersion != null) {
         this.dOpts.setPlanVersion(this.options.planVersion);
      }

      this.dOpts.setNoVersion(this.options.noVersion);
      this.dOpts.setRetireTime(this.options.retireTimeout);
      if (this.options.libSpecVersion != null) {
         this.dOpts.setLibSpecVersion(this.options.libSpecVersion);
      }

      if (this.options.libImplVersion != null) {
         this.dOpts.setLibImplVersion(this.options.libImplVersion);
      }

      if (this.options.name != null) {
         this.dOpts.setName(this.options.name);
      }

      this.dOpts.setAltDD(this.options.altAppDD);
      this.dOpts.setAltWlsDD(this.options.altWlsAppDD);
      this.dOpts.setForceUndeployTimeout(this.options.timeout);
      this.dOpts.setDefaultSubmoduleTargets(this.options.isDefaultSubmoduleTargets);
      this.dOpts.setUseNonexclusiveLock(this.options.useNonExclusiveLock);
      this.dOpts.setTimeout(this.options.timeout * 1000L);
      this.dOpts.setDeploymentOrder(this.options.deploymentOrder);
      this.dOpts.setRemote(this.options.remote);
   }

   private File prepareFile(String var1) {
      if (var1 == null) {
         return null;
      } else {
         File var2 = new File(ConfigHelper.normalize(var1));
         if (!this.options.remote && var1 != null) {
            if (var1.equals(".")) {
               var2 = (new File(var2.getAbsolutePath())).getParentFile();
            }

            if (var1.length() > 1 && var1.charAt(1) != ':') {
               var2 = var2.getAbsoluteFile();
            }
         }

         return var2;
      }
   }

   private void prepareTargets() throws DeployerException {
      if (this.options.moduleTargets == null) {
         this.options.moduleTargets = new String[0];
      }

      ArrayList var1 = new ArrayList();
      this.targetInfos = this.getTargetInfos(this.options.moduleTargets, this.options.submoduleTargets);

      for(int var2 = 0; var2 < this.targetInfos.length; ++var2) {
         ModuleTargetInfo var3 = this.targetInfos[var2];
         Target var4 = this.findTarget(var3);
         if (var4 == null) {
            throw new DeployerException(cat.errorNoSuchTarget(var3.getTarget()));
         }

         var1.add(var4);
      }

      this.targets = (Target[])((Target[])var1.toArray(new Target[0]));
   }

   private Target findTarget(ModuleTargetInfo var1, Target[] var2) {
      for(int var3 = 0; var3 < var2.length; ++var3) {
         Target var4 = var2[var3];
         if (var4.getName().equals(var1.getTarget())) {
            return var4;
         }
      }

      return null;
   }

   private Target findTarget(ModuleTargetInfo var1) {
      return this.getDm().getTarget(var1.getTarget());
   }

   private ModuleTargetInfo[] getTargetInfos(String[] var1, String[] var2) {
      HashSet var3 = new HashSet();

      int var4;
      for(var4 = 0; var4 < var1.length; ++var4) {
         var3.add(new ModuleTargetInfo(var1[var4]));
      }

      for(var4 = 0; var4 < var2.length; ++var4) {
         var3.add(new SubModuleTargetInfo(var2[var4]));
      }

      return (ModuleTargetInfo[])((ModuleTargetInfo[])var3.toArray(new ModuleTargetInfo[0]));
   }

   protected URI getUri() throws DeployerException {
      String var1 = this.tool.getOpts().getOption("adminurl", "t3://localhost:7001");
      int var2 = var1.indexOf("://");
      if (var2 == -1) {
         var1 = "t3://" + var1;
      }

      try {
         return new URI(var1);
      } catch (URISyntaxException var4) {
         throw new DeployerException(var4.toString());
      }
   }

   public void cleanUp() {
      if (this.dm != null) {
         this.dm.release();
      }

      super.cleanUp();
   }

   public int report() throws DeployerException {
      if (!this.dm.isConnected()) {
         this.println(cat.errorLostConnection());
         DeploymentStatus var1 = this.po.getDeploymentStatus();
         if (var1.getMessage() != null) {
            this.println(var1.getMessage());
         }

         this.println(cat.lastKnownStatus(var1.getCommand().toString(), var1.getState().toString()));
      }

      this.println(cat.messageStartedTask(this.task.getId(), this.task.getDescription()));
      if (this.options.nowait) {
         return 0;
      } else if (this.dOpts.usesNonExclusiveLock() && this.po.getDeploymentStatus().isRunning()) {
         this.println(cat.pendingOperation());
         return 0;
      } else {
         this.id = this.options.id;
         if (this.id == null) {
            this.id = this.task.getId();
         }

         long var6 = 0L;
         if (this.options.timeout != 0L) {
            var6 = System.currentTimeMillis() + 1000L * this.options.timeout;
         }

         TaskCompletionNotificationListener var3 = ((ProgressObjectImpl)this.po).getTaskCompletionListener();
         if (var3 != null) {
            var3.waitForTaskCompletion(1000L * this.options.timeout);
            if (this.task.isRunning()) {
               this.inform(cat.timeOut(this.task.getId()));
            }

            if (this.options.formatted) {
               this.showTaskInformationHeader();
            }

            this.failures = this.showTaskInformation(this.task);
            this.reportState(this.task);
            if (this.options.noexit && this.failures != 0) {
               throw new DeployerException(this.outstr == null ? cat.deploymentFailed() : this.outstr.toString());
            } else {
               return this.failures;
            }
         } else {
            try {
               this.waitForTaskCompletion(this.task, var6);
               if (this.task.isRunning()) {
                  this.inform(cat.timeOut(this.task.getId()));
               }

               if (this.options.formatted) {
                  this.showTaskInformationHeader();
               }

               this.failures = this.showTaskInformation(this.task);
               this.reportState(this.task);
               if (this.options.noexit && this.failures != 0) {
                  throw new DeployerException(this.outstr == null ? cat.deploymentFailed() : this.outstr.toString());
               } else {
                  return this.failures;
               }
            } catch (RemoteRuntimeException var5) {
               throw new DeployerException(cat.errorLostConnection());
            }
         }
      }
   }

   protected void postExecute() throws Exception {
      if (((ProgressObjectImpl)this.po).getTask() == null) {
         WebLogicDeploymentStatus var1 = (WebLogicDeploymentStatus)this.po.getDeploymentStatus();
         Iterator var2 = var1.getRootException();
         Throwable var3 = (Throwable)var2.next();
         if (var3 == null) {
            throw new DeployerException(cat.errorFailedOp());
         } else if (var3 instanceof Exception) {
            throw (Exception)var3;
         } else {
            DeployerException var4 = new DeployerException(var3.getMessage());
            var4.initCause(var3);
            throw var4;
         }
      } else {
         this.task = this.helper.getTaskMBean(((ProgressObjectImpl)this.po).getTask());
         if (this.task == null) {
            throw new DeployerException(cat.noTask(((ProgressObjectImpl)this.po).getTask().toString()));
         }
      }
   }

   protected TargetModuleID[] getTmids() {
      return (TargetModuleID[])((TargetModuleID[])this.tmids.toArray(new TargetModuleID[0]));
   }

   public WebLogicDeploymentManager getDm() {
      return this.dm;
   }

   public void setDm(WebLogicDeploymentManager var1) {
      this.dm = var1;
   }

   private void reportState(DeploymentTaskRuntimeMBean var1) {
      int var2 = var1.getState();
      switch (var2) {
         case 2:
         case 4:
            ((ProgressObjectImpl)this.po).setState(StateType.COMPLETED);
            break;
         case 3:
            ((ProgressObjectImpl)this.po).setState(StateType.FAILED);
      }

   }

   private void waitForTaskCompletion(DeploymentTaskRuntimeMBean var1, long var2) throws DeployerException {
      try {
         while(var1.isRunning() && (var2 <= 0L || var2 > System.currentTimeMillis())) {
            try {
               Thread.sleep(100L);
            } catch (InterruptedException var5) {
            }
         }

      } catch (RemoteRuntimeException var6) {
         throw new DeployerException(cat.errorLostConnection());
      }
   }

   public int showTaskInformation(DeploymentTaskRuntimeMBean var1) {
      int var2 = super.showTaskInformation(var1);
      if (!this.options.verbose) {
         return var2;
      } else {
         String var3 = this.getOperation();
         if (!"deploy".equals(var3) && !"distribute".equals(var3)) {
            return var2;
         } else {
            DeploymentData var4 = var1.getDeploymentData();
            this.println(cat.showTargetAssignmentsHeader());
            this.printTargetAssignment(var1.getApplicationName(), var4.getGlobalTargets(), 0);
            Map var5 = var4.getAllSubModuleTargets();
            Iterator var6 = var5.keySet().iterator();

            while(true) {
               Map var8;
               do {
                  if (!var6.hasNext()) {
                     Map var11 = var4.getAllModuleTargets();
                     Iterator var12 = var11.keySet().iterator();

                     while(var12.hasNext()) {
                        String var13 = (String)var12.next();
                        if (!var5.containsKey(var13)) {
                           this.printTargetAssignment(var13, var4.getModuleTargets(var13), 1);
                        }
                     }

                     return var2;
                  }

                  String var7 = (String)var6.next();
                  this.printTargetAssignment(var7, var4.getModuleTargets(var7), 1);
                  var8 = (Map)var5.get(var7);
               } while(var8 == null);

               Iterator var9 = var8.keySet().iterator();

               while(var9.hasNext()) {
                  String var10 = (String)var9.next();
                  this.printTargetAssignment(var10, (String[])((String[])var8.get(var10)), 2);
               }
            }
         }
      }
   }

   private void printTargetAssignment(String var1, String[] var2, int var3) {
      StringBuffer var4 = new StringBuffer();

      int var5;
      for(var5 = 0; var5 < var3; ++var5) {
         var4.append("   ");
      }

      var4.append("+ ");
      var4.append(var1);
      var4.append("  ");
      if (var2 != null && var2.length != 0) {
         for(var5 = 0; var5 < var2.length; ++var5) {
            if (var5 != 0) {
               var4.append(",");
            }

            var4.append(var2[var5]);
         }
      } else {
         var4.append(cat.noneSpecified());
      }

      this.println(var4.toString());
   }
}
