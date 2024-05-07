package weblogic.deploy.internal.adminserver.operations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import javax.enterprise.deploy.shared.ModuleType;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.application.ApplicationFileManager;
import weblogic.application.DeploymentManager;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.deploy.api.internal.SPIDeployerLogger;
import weblogic.deploy.api.internal.utils.ConfigHelper;
import weblogic.deploy.api.internal.utils.InstallDir;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.deploy.api.spi.DeploymentOptions;
import weblogic.deploy.api.spi.config.DescriptorParser;
import weblogic.deploy.api.tools.ModuleInfo;
import weblogic.deploy.api.tools.SessionHelper;
import weblogic.deploy.beans.factory.DeploymentBeanFactory;
import weblogic.deploy.beans.factory.InvalidTargetException;
import weblogic.deploy.common.Debug;
import weblogic.deploy.internal.Deployment;
import weblogic.deploy.internal.adminserver.EditAccessHelper;
import weblogic.deploy.service.datatransferhandlers.SourceCache;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.jms.module.DefaultingHelper;
import weblogic.logging.Loggable;
import weblogic.management.ApplicationException;
import weblogic.management.DeploymentException;
import weblogic.management.ManagementException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.SubDeploymentMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.deploy.DeploymentData;
import weblogic.management.deploy.DeploymentTaskRuntime;
import weblogic.management.deploy.internal.DeployerRuntimeImpl;
import weblogic.management.deploy.internal.DeployerRuntimeLogger;
import weblogic.management.deploy.internal.DeploymentServerService;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.management.runtime.DeploymentTaskRuntimeMBean;
import weblogic.management.utils.AppDeploymentHelper;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.jars.VirtualJarFile;

public abstract class AbstractOperation {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final DeploymentManager appDeploymentManager = DeploymentManager.getDeploymentManager();
   protected static final weblogic.deploy.internal.adminserver.DeploymentManager deploymentManager;
   protected final DeploymentBeanFactory beanFactory = DeploymentServerService.getDeploymentBeanFactory();
   protected int taskType;
   protected DeploymentTaskRuntimeMBean deploymentTask = null;
   protected Deployment deployment = null;
   protected final EditAccessHelper editAccessHelper;
   protected boolean controlOperation = false;
   protected File moduleArchive;
   protected File plan;
   protected DeploymentPlanBean planBean;
   protected InstallDir paths;
   protected DeploymentOptions options;
   private AuthenticatedSubject authSubject;
   private static int nextDelegateId;
   private static final String DELEGATE_PREFIX = "_DELEGATE_";

   AbstractOperation() {
      this.editAccessHelper = deploymentManager.getEditAccessHelper(kernelId);
   }

   protected boolean isRemote(DeploymentData var1) {
      return var1.isRemote();
   }

   protected boolean isThinClient(DeploymentData var1) {
      return var1.isThinClient();
   }

   public DeploymentTaskRuntimeMBean execute(String var1, String var2, String var3, DeploymentData var4, String var5, boolean var6, AuthenticatedSubject var7) throws ManagementException {
      this.authSubject = var7;
      if (this.isRemote(var4) || this.isThinClient(var4)) {
         if (var1 != null) {
            this.moduleArchive = new File(var1);
         }

         if (var4.getDeploymentPlan() != null) {
            this.plan = new File(ConfigHelper.normalize(var4.getDeploymentPlan()));
         }

         this.options = var4.getDeploymentOptions();
         this.parsePlan();
         if (this.moduleArchive != null) {
            var2 = ConfigHelper.getAppName(this.options, this.moduleArchive, this.planBean);
         }

         this.setupPaths(var2);
         if (this.paths != null) {
            if (this.paths.getPlan() != null) {
               var4.setDeploymentPlan(this.paths.getPlan().getPath());
            }

            var4.setConfigDirectory(this.paths.getConfigDir().getPath());
            var4.setRootDirectory(this.paths.getInstallDir().getPath());
         }

         this.setVersionInfo(var2);
         this.setupDDPaths(var4);
      }

      if (this.isControlOperation()) {
         return this.executeControlOperation(var1, var2, var3, var4, var5, var6, var7);
      } else {
         boolean var8 = this.editAccessHelper.isCurrentEditor(var7);
         long var9 = this.editAccessHelper.getEditorExpirationTime();
         if (var8 && var9 > 0L && var9 < System.currentTimeMillis() && (var4 == null || var4.getDeploymentOptions() == null || !var4.getDeploymentOptions().usesExpiredLock())) {
            var8 = false;
         }

         boolean var11 = var4 != null && var4.usesNonExclusiveLock();
         boolean var12 = this.editAccessHelper.isEditorExclusive();
         boolean var13 = false;
         if (isDebugEnabled()) {
            this.debugSay("Deployment operation lock settings:  acquire lock in non-exclusive mode?: " + var11 + ", caller owns lock?: " + var8 + "', as exclusive editor:? " + var12);
         }

         boolean var14 = false;

         try {
            DomainMBean var15;
            if (var8) {
               String var16;
               if (var12) {
                  var16 = DeployerRuntimeLogger.exclusiveModeLock();
                  throw new ManagementException(var16);
               }

               var15 = this.editAccessHelper.getEditDomainBean(var7);
               if (!var11) {
                  var16 = DeployerRuntimeLogger.nonExclusiveModeLock();
                  throw new ManagementException(var16);
               }

               var13 = true;
            } else {
               if (isDebugEnabled()) {
                  this.debugSay("Caller does not own edit lock - deployment subsystem acquiring edit lock in exlusive mode for " + var2);
               }

               var15 = this.editAccessHelper.startEditSession(!var11);
            }

            this.beanFactory.setEditableDomain(var15, var8);
            AppDeploymentMBean var19 = this.updateConfigurationAndInitializeDeployment(var1, var2, var3, var4, var5, var15, var7, var8);
            if (var19.isAutoDeployedApp() && var4 != null) {
               DeploymentOptions var17 = var4.getDeploymentOptions();
               if (!var17.isOperationInitiatedByAutoDeployPoller()) {
                  throw new DeploymentException(this.getAutoDeployErrorMsg(var19.getName()));
               }
            }

            this.postTaskCreationConfigurationUpdate(var19, var2, this.deploymentTask.getDeploymentData());
            if (var13) {
               ((DeploymentTaskRuntime)this.deploymentTask).setPendingActivation(true);
            }

            if (var8) {
               this.mergeWithExistingOperationsOnSameApp();
            }

            if (!var8 || var13) {
               this.editAccessHelper.saveEditSessionChanges();
            }

            if (!var8) {
               this.editAccessHelper.activateEditSessionChanges((long)var4.getTimeOut());
            }

            this.beanFactory.resetEditableDomain();
            if (!var8 && var6) {
               var14 = true;
               this.deploymentTask.start();
            }

            return this.deploymentTask;
         } catch (Throwable var18) {
            if (!var14) {
               deploymentManager.deploymentFailedBeforeStart(this.deployment, var18, var8, var7, this.controlOperation);
            }

            if (this.deployment == null) {
               OperationHelper.logTaskFailed(var2, this.taskType, var18);
            }

            if (var18 instanceof ManagementException) {
               throw (ManagementException)var18;
            } else {
               throw new ManagementException(var18);
            }
         }
      }
   }

   public DeploymentTaskRuntime getTaskRuntime() {
      return (DeploymentTaskRuntime)this.deploymentTask;
   }

   public void rollback(AuthenticatedSubject var1) {
   }

   protected String getAutoDeployErrorMsg(String var1) {
      return var1;
   }

   protected DeploymentTaskRuntimeMBean executeControlOperation(String var1, String var2, String var3, DeploymentData var4, String var5, boolean var6, AuthenticatedSubject var7) throws ManagementException {
      DomainMBean var8 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      boolean var9 = false;

      try {
         this.updateConfigurationAndInitializeDeployment(var1, var2, var3, var4, var5, var8, var7, false);
         if (var6) {
            var9 = true;
            this.deploymentTask.start();
         }

         return this.deploymentTask;
      } catch (Throwable var11) {
         if (!var9) {
            deploymentManager.deploymentFailedBeforeStart(this.deployment, var11, false, var7, this.controlOperation);
         }

         if (this.deployment == null) {
            OperationHelper.logTaskFailed(var2, this.taskType, var11);
         }

         if (var11 instanceof ManagementException) {
            throw (ManagementException)var11;
         } else {
            throw new ManagementException(var11);
         }
      }
   }

   private AppDeploymentMBean updateConfigurationAndInitializeDeployment(String var1, String var2, String var3, DeploymentData var4, String var5, DomainMBean var6, AuthenticatedSubject var7, boolean var8) throws ManagementException {
      var1 = OperationHelper.normalizePaths(var1, var4);
      if (var4.getDeploymentOptions() != null && var4.getDeploymentOptions().isDefaultSubmoduleTargets()) {
         this.defaultSubModuleTargets(var2, var1, var4, var6);
      }

      AppDeploymentMBean var9 = this.updateConfiguration(var1, var2, var3, var4, var5, var8);
      doDeploymentTypeValidations(var6, var2, var4, var9);
      this.createRuntimeObjects(var1, var5, var9, var4, this.getCreateTaskType(), var6, var7, var8);
      return var9;
   }

   protected void defaultSubModuleTargets(String var1, String var2, DeploymentData var3, DomainMBean var4) throws ManagementException {
      SessionHelper var5 = null;
      AppDeploymentMBean var6 = null;
      boolean var7 = false;

      try {
         File var8 = null;
         if (var2 != null) {
            var8 = new File(var2);
         } else {
            var6 = AppDeploymentHelper.lookupAppOrLib(var1, var4);
            var7 = true;
            if (var6 == null) {
               return;
            }

            var8 = new File(var6.getSourcePath());
         }

         if (!this.hasJMSModules(var8)) {
            return;
         }

         if (!var7) {
            var6 = AppDeploymentHelper.lookupAppOrLib(var1, var4);
         }

         if (isDebugEnabled()) {
            this.debugSay(var8 + " has JMS modules");
         }

         var5 = SessionHelper.getInstance(SessionHelper.getDisconnectedDeploymentManager());
         var5.setApplication(var8);
         var5.setPlan(this.plan);
         var5.inspect();
         boolean var9 = false;
         ModuleInfo var10 = var5.getModuleInfo();
         ArrayList var11 = new ArrayList();
         int var13;
         ModuleInfo var14;
         if (var10.getType().getValue() == WebLogicModuleType.JMS.getValue()) {
            var11.add(var10);
            var9 = true;
         } else if (var10.getType().getValue() == WebLogicModuleType.EAR.getValue()) {
            ModuleInfo[] var12 = var10.getSubModules();

            for(var13 = 0; var13 < var12.length; ++var13) {
               var14 = var12[var13];
               if (var14.getType().getValue() == WebLogicModuleType.JMS.getValue()) {
                  var11.add(var14);
               }
            }
         }

         TargetMBean[] var28 = this.getTargetMBeans(var4, var3);

         for(var13 = 0; var13 < var11.size(); ++var13) {
            var14 = (ModuleInfo)var11.get(var13);
            if (var6 == null || !this.checkAppTargetting(var6, var14.getName(), var5)) {
               Map var15 = this.getDefaultJMSTargets(var4, var28, var14.getName(), var5);
               Iterator var16 = var15.keySet().iterator();

               while(var16.hasNext()) {
                  String var17 = (String)var16.next();
                  TargetMBean[] var18 = (TargetMBean[])((TargetMBean[])var15.get(var17));

                  for(int var19 = 0; var19 < var18.length; ++var19) {
                     TargetMBean var20 = var18[var19];
                     String var21 = var14.getName();
                     if (var9) {
                        var21 = null;
                     }

                     if (!var3.isSubModuleTargeted(var21, var17)) {
                        var3.addSubModuleTarget(var21, var17, new String[]{var20.getName()});
                     }
                  }
               }
            }
         }
      } catch (Throwable var26) {
         throw new ManagementException(var26);
      } finally {
         if (var5 != null) {
            var5.close();
         }

      }

   }

   private Map getDefaultJMSTargets(DomainMBean var1, TargetMBean[] var2, String var3, SessionHelper var4) throws ConfigurationException {
      JMSBean var5 = null;

      try {
         var5 = var4.getJMSDescriptor(var3);
      } catch (FileNotFoundException var7) {
      }

      return var5 == null ? null : DefaultingHelper.getJMSDefaultTargets(var5, var1, var2);
   }

   private boolean checkAppTargetting(AppDeploymentMBean var1, String var2, SessionHelper var3) {
      SubDeploymentMBean[] var4;
      if (var3.getDeployableObject().getType().getValue() == ModuleType.EAR.getValue()) {
         var4 = var1.getSubDeployments();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            SubDeploymentMBean var6 = var4[var5];
            if (var6.getName().equals(var2)) {
               return this.checkIfTargeted(var6);
            }
         }
      } else {
         var4 = var1.getSubDeployments();
         if (var4 != null && var4.length != 0) {
            return true;
         }
      }

      return false;
   }

   private boolean checkIfTargeted(SubDeploymentMBean var1) {
      SubDeploymentMBean[] var2 = var1.getSubDeployments();
      return var2 != null && var2.length != 0;
   }

   private boolean hasJMSModules(File var1) {
      if (var1.isFile() && var1.getPath().endsWith("-jms.xml")) {
         return true;
      } else {
         VirtualJarFile var2 = null;

         try {
            ApplicationFileManager var3 = ApplicationFileManager.newInstance(var1);
            var2 = var3.getVirtualJarFile();
            Iterator var4 = var2.entries();

            while(var4.hasNext()) {
               JarEntry var5 = (JarEntry)var4.next();
               if (var5.getName().endsWith("-jms.xml")) {
                  if (isDebugEnabled()) {
                     this.debugSay(" found JMS module: " + var5.getName());
                  }

                  boolean var6 = true;
                  return var6;
               }
            }
         } catch (IOException var17) {
         } finally {
            if (var2 != null) {
               try {
                  var2.close();
               } catch (IOException var16) {
               }
            }

         }

         return false;
      }
   }

   private TargetMBean[] getTargetMBeans(DomainMBean var1, DeploymentData var2) {
      String[] var3 = var2.getTargets();
      if (var3 == null || var3.length < 1) {
         var3 = new String[]{var1.getAdminServerName()};
      }

      HashSet var4 = new HashSet();
      TargetMBean[] var5 = var1.getTargets();

      for(int var6 = 0; var6 < var3.length; ++var6) {
         TargetMBean var7 = this.findTarget(var3[var6], var5);
         if (var7 != null) {
            var4.add(var7);
         }
      }

      return (TargetMBean[])((TargetMBean[])var4.toArray(new TargetMBean[0]));
   }

   private TargetMBean findTarget(String var1, TargetMBean[] var2) {
      for(int var3 = 0; var3 < var2.length; ++var3) {
         TargetMBean var4 = var2[var3];
         if (var4.getName().equals(var1)) {
            return var4;
         }
      }

      return null;
   }

   private static void doDeploymentTypeValidations(DomainMBean var0, String var1, DeploymentData var2, AppDeploymentMBean var3) throws ManagementException {
      OperationHelper.validateAutoDeployTarget(var0, var1, var2);
      OperationHelper.validateSplitDirTarget(var0, var1, var2);
      if (isInternalApp(var1)) {
         Loggable var4 = DeployerRuntimeLogger.logNoOpOnInternalAppLoggable(var3.getName());
         var4.log();
         throw new ManagementException(var4.getMessage());
      }
   }

   private boolean isControlOperation() {
      return this.controlOperation;
   }

   private static boolean isInternalApp(String var0) {
      DomainMBean var1 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      AppDeploymentMBean var2 = var1.lookupAppDeployment(var0);
      return null == var2 ? false : var2.isInternalApp();
   }

   protected boolean removeBeans() {
      return false;
   }

   protected int getCreateTaskType() {
      return this.taskType;
   }

   protected abstract AppDeploymentMBean updateConfiguration(String var1, String var2, String var3, DeploymentData var4, String var5, boolean var6) throws ManagementException;

   protected void postTaskCreationConfigurationUpdate(AppDeploymentMBean var1, String var2, DeploymentData var3) throws ManagementException {
   }

   protected static final void addAdminServerAsDefaultTarget(DeploymentData var0) {
      if (var0 != null) {
         if (var0.isActionFromDeployer()) {
            RuntimeAccess var1 = ManagementService.getRuntimeAccess(kernelId);
            if (!var0.hasTargets()) {
               var0.addGlobalTarget(var1.getServerName());
            }
         }

      }
   }

   protected final AppDeploymentMBean createOrReconcileMBeans(String var1, String var2, DeploymentData var3, String var4, AppDeploymentMBean var5, String var6) throws ManagementException {
      Loggable var8;
      try {
         if (var5 == null) {
            var5 = this.createMBeans(var1, var2, var3, var4);
            this.setStagingMode(var6, var5);
         } else {
            this.reconcileMBeans(var3, var5);
         }

         if (isDebugEnabled()) {
            this.debugSay("Deploying app " + var5.getApplicationName() + (var5.getVersionIdentifier() == null ? "" : " versionId: " + var5.getVersionIdentifier()) + " with path: " + var5.getAbsoluteSourcePath());
         }

         return var5;
      } catch (FileNotFoundException var9) {
         var8 = DeployerRuntimeLogger.logInvalidSourceLoggable(var1, var2, var9.getMessage());
         var8.log();
         if (var3.isNewApplication() && var5 != null) {
            this.beanFactory.removeMBean(var5);
         }

         throw new ManagementException(var8.getMessage(), var9);
      } catch (ApplicationException var10) {
         var8 = DeployerRuntimeLogger.logInvalidAppLoggable(var1, var2, var10.getMessage());
         var8.log();
         throw new ManagementException(var8.getMessage(), var10);
      }
   }

   protected final AppDeploymentMBean createMBeans(String var1, String var2, DeploymentData var3, String var4) throws FileNotFoundException, ManagementException {
      File var5 = new File(var1);
      if (isDebugEnabled()) {
         this.debugSay("Creating mbeans for " + var1);
      }

      var3.setNewApp(true);
      String var6 = ApplicationVersionUtils.getApplicationId(var2, var4);

      try {
         AppDeploymentMBean var7 = this.beanFactory.createAppDeploymentMBean(var6, var5, var3);
         return var7;
      } catch (InvalidTargetException var10) {
         String var9 = DeployerRuntimeLogger.invalidTarget();
         throw new ManagementException(var9, var10);
      }
   }

   protected final void validateAllTargets(AppDeploymentMBean var1, DeploymentData var2, String var3, String var4, String var5) throws DeploymentException {
      String[] var6 = var2.getGlobalTargets();
      if (var6 != null && var6.length != 0) {
         for(int var7 = 0; var7 < var6.length; ++var7) {
            if (!this.isValidTarget(var6[var7], var1)) {
               String var8 = ApplicationVersionUtils.getApplicationId(var3, var4);
               Loggable var9 = DeployerRuntimeLogger.logInvalidTargetForOperationLoggable(var8, var6[var7], var5);
               throw new DeploymentException(var9.getMessage());
            }
         }

      }
   }

   private boolean isValidTarget(String var1, AppDeploymentMBean var2) {
      TargetMBean[] var3 = var2.getTargets();
      if (var3 != null && var3.length != 0) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            TargetMBean var5 = var3[var4];
            if (var5.getName().equals(var1)) {
               return true;
            }

            if (var5 instanceof ClusterMBean) {
               ClusterMBean var6 = (ClusterMBean)var3[var4];
               ServerMBean[] var7 = var6.getServers();

               for(int var8 = 0; var8 < var7.length; ++var8) {
                  if (var7[var8].getName().equals(var1)) {
                     return true;
                  }
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private void deleteFailedApplication(AppDeploymentMBean var1) {
      appDeploymentManager.getMBeanFactory().cleanupMBeans(this.beanFactory.getEditableDomain(), var1.getAppMBean());
   }

   protected final void reconcileMBeans(DeploymentData var1, AppDeploymentMBean var2) throws ApplicationException, FileNotFoundException, ManagementException {
      this.reconcileMBeans(var1, var2, false);
   }

   protected void reconcileMBeans(DeploymentData var1, AppDeploymentMBean var2, boolean var3) throws ApplicationException, FileNotFoundException, ManagementException {
      boolean var4 = false;
      String var5;
      if (!OperationHelper.hasFiles(var1)) {
         if (var1.getDeploymentPlan() != null) {
            var5 = var1.getConfigDirectory();
            if (var5 != null) {
               File var6 = new File(var5);
               String[] var7 = var6.list();
               if (var7 == null || var7.length == 0) {
                  var5 = null;
               }
            }

            var2.setPlanDir(var5);
            var2.setPlanPath(var1.getDeploymentPlan());
            var4 = true;
         } else if (!var3 && var2.getPlanPath() != null) {
            var2.setPlanPath((String)null);
            var2.setPlanDir((String)null);
            var4 = true;
         }
      }

      if (var1.getAltDescriptorPath() != null) {
         var2.setAltDescriptorPath(var1.getAltDescriptorPath());
         var4 = true;
      }

      if (var1.getAltWLSDescriptorPath() != null) {
         var2.setAltWLSDescriptorPath(var1.getAltWLSDescriptorPath());
         var4 = true;
      }

      if (var1.getIsNameFromSource()) {
         var1.setFile((String[])null);
      }

      var5 = var1.getSecurityModel();
      if (var5 != null && var5.length() > 0 && !var5.equals(var2.getSecurityDDModel())) {
         DeployerRuntimeLogger.logDiffSecurityModelIgnoredForRedeploy(ApplicationVersionUtils.getDisplayName((BasicDeploymentMBean)var2), var2.getSecurityDDModel(), var5);
      }

      if (var1.getDeploymentPrincipalName() != null) {
         var2.setDeploymentPrincipalName(var1.getDeploymentPrincipalName());
         var4 = true;
      }

      if (isDebugEnabled()) {
         this.debugSay("Reloading mbeans for " + var2.getAbsoluteSourcePath());
      }

      try {
         this.beanFactory.addTargetsInDeploymentData(var1, var2);
      } catch (InvalidTargetException var8) {
         String var9 = DeployerRuntimeLogger.invalidTarget();
         throw new ManagementException(var9, var8);
      }

      if (var4) {
         SourceCache.updateDescriptorsInCache(var2);
      }

   }

   protected static final void debugDeployer(String var0) {
      Debug.deploymentDebug(var0);
   }

   private void setStagingMode(String var1, AppDeploymentMBean var2) throws ManagementException {
      if (var1 != null) {
         if (!var1.equals("nostage") && !var1.equals("stage") && !var1.equals("external_stage")) {
            Loggable var3 = DeployerRuntimeLogger.logInvalidStagingModeLoggable(var1);
            var3.log();
            this.deleteFailedApplication(var2);
            throw new ManagementException(var3.getMessage());
         }

         if (isDebugEnabled()) {
            this.debugSay("Staging Mode is " + var1);
         }

         var2.setStagingMode(var1);
      }

   }

   protected final void printDebugStartMessage(String var1, String var2, String var3, DeploymentData var4, String var5, String var6, String var7) {
      if (isDebugEnabled()) {
         StringBuffer var8 = new StringBuffer();
         var8.append(var6);
         var8.append(" invoked with:\n");
         if (var1 != null) {
            var8.append("  Path = <" + var1 + ">\n");
         }

         var8.append("  name = <" + var2 + ">\n");
         if (var3 != null) {
            var8.append("  version = <" + var3 + ">\n");
         }

         if (var7 != null) {
            var8.append("  StagingMode = <" + var7 + ">\n");
         }

         if (var4 != null) {
            var8.append("  info = <" + var4.toString() + ">\n");
         }

         var8.append("  id = <" + var5 + ">\n");
         this.debugSay(var8.toString());
      }

   }

   protected final void createRuntimeObjects(String var1, String var2, AppDeploymentMBean var3, DeploymentData var4, int var5, DomainMBean var6, AuthenticatedSubject var7, boolean var8) throws ManagementException {
      this.deploymentTask = this.createAndRegisterDeploymentTaskRuntime(var1, var2, var3, var4, var5, var6);
      if (var8) {
         ((DeploymentTaskRuntime)this.deploymentTask).setSubject(var7);
      }

      ((DeploymentTaskRuntime)this.deploymentTask).setAdminOperation(this);
      this.deployment = deploymentManager.createDeployment(var2, var4, var5, (DeploymentTaskRuntime)this.deploymentTask, var6, false, var7, var8, this.controlOperation, false);
   }

   private DeploymentTaskRuntime createAndRegisterDeploymentTaskRuntime(String var1, String var2, AppDeploymentMBean var3, DeploymentData var4, int var5, DomainMBean var6) throws ManagementException {
      DeployerRuntimeImpl var7 = deploymentManager.getDeployerRuntime();
      DeploymentTaskRuntime var8 = new DeploymentTaskRuntime(var1, var3, var4, var2, var5, var6, this.controlOperation, false);
      var7.registerTaskRuntime(var2, var8);
      return var8;
   }

   protected static final void invalidateCache(BasicDeploymentMBean var0) {
      SourceCache.invalidateCache(var0);
   }

   protected void parsePlan() throws ManagementException {
      if (this.planBean == null) {
         FileInputStream var1 = null;

         try {
            if (this.plan != null) {
               var1 = new FileInputStream(this.plan);
            }

            if (var1 != null) {
               this.planBean = DescriptorParser.parseDeploymentPlan(var1);
            }
         } catch (IOException var11) {
            throw new ManagementException(var11);
         } finally {
            try {
               if (var1 != null) {
                  var1.close();
               }
            } catch (IOException var10) {
            }

         }

      }
   }

   protected void setupPaths(String var1) throws ManagementException {
      try {
         if (this.moduleArchive != null) {
            this.moduleArchive = ConfigHelper.normalize(this.moduleArchive).getCanonicalFile();
            this.paths = new InstallDir(var1, ConfigHelper.getAppRootFromPlan(this.planBean));
            this.paths.setArchive(this.moduleArchive);
            if (this.plan != null) {
               this.paths.setPlan(this.plan.getCanonicalFile());
            }

            ConfigHelper.initPlanDirFromPlan(this.planBean, this.paths);
            if (this.planBean != null && this.planBean.getConfigRoot() != null) {
               this.paths.setConfigDir((new File(this.planBean.getConfigRoot())).getCanonicalFile());
            }

         }
      } catch (IOException var3) {
         throw new ManagementException(var3);
      }
   }

   protected boolean isSameOperationType(AbstractOperation var1) {
      return this.getClass().equals(var1.getClass());
   }

   protected final boolean areTargetsSame(AbstractOperation var1) {
      DeploymentTaskRuntime var2 = this.getTaskRuntime();
      DeploymentData var3 = var2.getDeploymentData();
      DeploymentTaskRuntime var4 = var1.getTaskRuntime();
      DeploymentData var5 = var4.getDeploymentData();
      List var6 = Arrays.asList(var3.getGlobalTargets());
      List var7 = Arrays.asList(var5.getGlobalTargets());
      boolean var8 = var6.equals(var7);
      if (isDebugEnabled()) {
         this.debugSay(" Global Targets are same : " + var8);
      }

      if (!var8) {
         return false;
      } else {
         var8 = var3.getAllModuleTargets().equals(var5.getAllModuleTargets());
         if (isDebugEnabled()) {
            this.debugSay(" Module Targets are same : " + var8);
         }

         if (!var8) {
            return false;
         } else {
            var8 = var3.getAllSubModuleTargets().equals(var5.getAllSubModuleTargets());
            if (isDebugEnabled()) {
               this.debugSay(" Submodule Targets are same : " + var8);
               this.debugSay(" Targets are same : " + var8);
            }

            return var8;
         }
      }
   }

   protected final void setAsDelegatorTo(AbstractOperation var1) throws DeploymentException {
      DeploymentTaskRuntime var2 = this.getTaskRuntime();
      ArrayList var3 = new ArrayList();
      var3.add(var2.getId());
      weblogic.deploy.internal.adminserver.DeploymentManager.getInstance(kernelId).removeDeploymentsForTasks(var3);
      DeploymentTaskRuntime var4 = var1.getTaskRuntime();
      var4.addDelegator(var2);
   }

   protected void removeSelf() throws DeploymentException {
      DeploymentTaskRuntime var1 = this.getTaskRuntime();
      ArrayList var2 = new ArrayList();
      var2.add(var1.getId());
      weblogic.deploy.internal.adminserver.DeploymentManager.getInstance(kernelId).removeDeploymentsForTasks(var2);
      var1.updateAllTargetsWithSuccessForMerging();
   }

   protected final void mergeWithSameOperationType(AbstractOperation var1) throws ManagementException {
      if (this.isSameOperationType(var1)) {
         if (this.areTargetsSame(var1)) {
            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug(" Operation '" + this + "' is same type and has same targets as '" + var1 + "'");
            }

            this.setAsDelegatorTo(var1);
         } else {
            AbstractOperation var2 = this.createCopy();
            DeploymentTaskRuntime var3 = this.getTaskRuntime();
            DeploymentData var4 = var3.getDeploymentData();
            DeploymentTaskRuntime var5 = var1.getTaskRuntime();
            DeploymentData var6 = var5.getDeploymentData();
            DeploymentData var7 = var4.copy();
            var7.addGlobalTargets(var6.getGlobalTargets());
            if (var6.hasModuleTargets()) {
               var7.addOrUpdateModuleTargets(var6.getAllModuleTargets());
            }

            if (var6.hasSubModuleTargets()) {
               var7.addOrUpdateSubModuleTargets(var6.getAllSubModuleTargets());
            }

            if (var6.hasFiles()) {
               var7.addFiles(var6.getFiles());
            }

            String var8 = var6.getDeploymentPlan();
            if (var8 != null) {
               var7.setDeploymentPlan(var8);
            }

            String var9 = var3.getSource();
            String var10 = "_DELEGATE_" + nextDelegateId++;
            AuthenticatedSubject var11 = this.authSubject;
            var2.createRuntimeObjects(var9, var10, (AppDeploymentMBean)var3.getDeploymentMBean(), var7, this.getCreateTaskType(), this.editAccessHelper.getEditDomainBean(var11), var11, true);
            ArrayList var12 = new ArrayList();
            var12.add(this);
            var12.add(var1);
            var2.setAsDelegateTo(var12);
            if (isDebugEnabled()) {
               this.debugSay(" Operations '" + this + "' and '" + var1 + "' are merged to '" + var2 + "'");
            }
         }

      }
   }

   protected final void createCopyAndConstructNewRuntime() throws ManagementException {
      AbstractOperation var1 = this.createCopy();
      DeploymentTaskRuntime var2 = this.getTaskRuntime();
      DeploymentData var3 = var2.getDeploymentData();
      String var4 = var2.getSource();
      String var5 = "_DELEGATE_" + nextDelegateId++;
      AuthenticatedSubject var6 = this.authSubject;
      var1.createRuntimeObjects(var4, var5, (AppDeploymentMBean)var2.getDeploymentMBean(), var3, this.getCreateTaskType(), this.editAccessHelper.getEditDomainBean(var6), var6, true);
      ArrayList var7 = new ArrayList();
      var7.add(this);
      var1.setAsDelegateTo(var7);
   }

   protected void setAsDelegateTo(List var1) throws DeploymentException {
      if (var1 != null && !var1.isEmpty()) {
         ArrayList var2 = new ArrayList();
         DeploymentTaskRuntime var3 = this.getTaskRuntime();
         Iterator var4 = var1.iterator();

         while(var4.hasNext()) {
            AbstractOperation var5 = (AbstractOperation)var4.next();
            DeploymentTaskRuntime var6 = var5.getTaskRuntime();
            var2.add(var6.getId());
            var3.addDelegator(var6);
         }

         weblogic.deploy.internal.adminserver.DeploymentManager.getInstance(kernelId).removeDeploymentsForTasks(var2);
      }
   }

   protected abstract AbstractOperation createCopy();

   protected final void mergeUndeployWithDistributeOrDeployOrRedeploy(AbstractOperation var1) throws ManagementException {
      if (!(this instanceof RemoveOperation)) {
         throw new DeploymentException("current operation must be undeploy");
      } else if (!(var1 instanceof DistributeOperation) && !(var1 instanceof RedeployOperation) && !(var1 instanceof ActivateOperation)) {
         throw new DeploymentException("other operation must be one of distribute or redeploy or deploy operations");
      } else {
         if (this.areTargetsSame(var1)) {
            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug("Targets are same. So, cancelling operations");
            }

            this.removeSelf();
            var1.removeSelf();
         } else {
            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug("Targets are not same. So, making redeploy operation and keeing undeploy operation with some targets");
            }

            boolean var2 = this.haveCommonTargets(var1);
            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug("operations '" + this + "' and '" + var1 + "' have common targets: " + var2);
            }

            if (var2) {
               DeploymentTaskRuntime var3 = this.getTaskRuntime();
               DeploymentData var4 = var3.getDeploymentData();
               DeploymentTaskRuntime var5 = var1.getTaskRuntime();
               DeploymentData var6 = var5.getDeploymentData();
               var4.removeCommonTargets(var6, true);
               if (var4.hasNoTargets()) {
                  var1.createCopyAndConstructNewRuntime();
                  this.removeSelf();
               } else if (var6.hasNoTargets()) {
                  this.createCopyAndConstructNewRuntime();
                  var1.removeSelf();
               } else {
                  this.createCopyAndConstructNewRuntime();
                  var1.createCopyAndConstructNewRuntime();
               }
            }
         }

      }
   }

   protected void mergeWithUndeploy(AbstractOperation var1) throws ManagementException {
      if (isDebugEnabled()) {
         this.debugSay("Invoking mergeWithUndeploy(" + var1 + ")...");
      }

      if (!(var1 instanceof RemoveOperation)) {
         throw new DeploymentException("Other operation '" + var1 + "' is not RemoveOperation");
      }
   }

   protected void mergeWithRedeploy(AbstractOperation var1) throws ManagementException {
      if (isDebugEnabled()) {
         this.debugSay("Invoking mergeWithRedeploy(" + var1 + ")...");
      }

      if (!(var1 instanceof RedeployOperation)) {
         throw new DeploymentException("Other operation '" + var1 + "' is not RedeployOperation");
      }
   }

   protected void mergeWithDeploy(AbstractOperation var1) throws ManagementException {
      if (isDebugEnabled()) {
         this.debugSay("Invoking mergeWithDeploy(" + var1 + ")...");
      }

      if (!(var1 instanceof ActivateOperation)) {
         throw new DeploymentException("Other operation '" + var1 + "' is not ActivateOperation");
      }
   }

   protected void mergeWithUpdate(AbstractOperation var1) throws ManagementException {
      if (isDebugEnabled()) {
         this.debugSay("Invoking mergeWithUpdate(" + var1 + ")...");
      }

      if (!(var1 instanceof UpdateOperation)) {
         throw new DeploymentException("Other operation '" + var1 + "' is not UpdateOperation");
      }
   }

   protected void mergeWithDistribute(AbstractOperation var1) throws ManagementException {
      if (isDebugEnabled()) {
         this.debugSay("Invoking mergeWithDistribute(" + var1 + ")...");
      }

      if (!(var1 instanceof DistributeOperation)) {
         throw new DeploymentException("Other operation '" + var1 + "' is not DistributeOperation");
      }
   }

   private boolean haveCommonTargets(AbstractOperation var1) {
      DeploymentTaskRuntime var2 = this.getTaskRuntime();
      DeploymentData var3 = var2.getDeploymentData();
      DeploymentTaskRuntime var4 = var1.getTaskRuntime();
      DeploymentData var5 = var4.getDeploymentData();
      return haveCommonTargets(var3, var5);
   }

   private static boolean haveCommonTargets(DeploymentData var0, DeploymentData var1) {
      if (haveCommonGlobalTargets(var0, var1)) {
         return true;
      } else if (haveCommonModuleTargets(var0, var1)) {
         return true;
      } else {
         return haveCommonSubModuleTargets(var0, var1);
      }
   }

   private static boolean haveCommonGlobalTargets(DeploymentData var0, DeploymentData var1) {
      String[] var2 = var0.getGlobalTargets();
      if (var2 != null && var2.length != 0) {
         ExtendedArrayList var3 = new ExtendedArrayList(var2);
         String[] var4 = var1.getGlobalTargets();
         return var4 != null && var4.length != 0 ? var3.containsOne(var4) : false;
      } else {
         return false;
      }
   }

   private static boolean haveCommonModuleTargets(DeploymentData var0, DeploymentData var1) {
      Map var2 = var0.getAllModuleTargets();
      Map var3 = var1.getAllModuleTargets();
      if (!var3.isEmpty() && !var2.isEmpty()) {
         Set var4 = var3.keySet();
         Iterator var5 = var4.iterator();

         while(var5.hasNext()) {
            String var6 = (String)var5.next();
            if (var2.containsKey(var6)) {
               String[] var7 = (String[])((String[])var3.get(var6));
               ExtendedArrayList var8 = new ExtendedArrayList((String[])((String[])var2.get(var6)));
               if (var8.containsOne(var7)) {
                  return true;
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private static boolean haveCommonSubModuleTargets(DeploymentData var0, DeploymentData var1) {
      Map var2 = var0.getAllSubModuleTargets();
      Map var3 = var1.getAllSubModuleTargets();
      if (!var3.isEmpty() && !var2.isEmpty()) {
         Set var4 = var3.keySet();
         Iterator var5 = var4.iterator();

         while(true) {
            Map var7;
            Map var8;
            do {
               do {
                  String var6;
                  do {
                     do {
                        do {
                           if (!var5.hasNext()) {
                              return false;
                           }

                           var6 = (String)var5.next();
                        } while(!var2.containsKey(var6));

                        var7 = (Map)var3.get(var6);
                     } while(var7 == null);
                  } while(var7.isEmpty());

                  var8 = (Map)var2.get(var6);
               } while(var8 == null);
            } while(var8.isEmpty());

            Iterator var9 = var7.keySet().iterator();

            while(var9.hasNext()) {
               String var10 = (String)var9.next();
               if (var8.containsKey(var10)) {
                  String[] var11 = (String[])((String[])var7.get(var10));
                  ExtendedArrayList var12 = new ExtendedArrayList((String[])((String[])var8.get(var10)));
                  if (var12.containsOne(var11)) {
                     return true;
                  }
               }
            }
         }
      } else {
         return false;
      }
   }

   private static void mergeOperationsOnAppAgain(String var0) throws ManagementException {
      List var1 = weblogic.deploy.internal.adminserver.DeploymentManager.getInstance(kernelId).getExistingOperationsOnApp(var0);
      if (isDebugEnabled()) {
         Debug.deploymentDebug(" <merge2> Found Same Operations : " + var1);
      }

      if (var1 != null && var1.size() >= 2) {
         if (var1.size() > 2) {
            throw new ManagementException("Contains more than two operations after merge");
         } else {
            AbstractOperation var2 = (AbstractOperation)var1.get(0);
            AbstractOperation var3 = (AbstractOperation)var1.get(1);
            if (var2.haveCommonTargets(var3)) {
               var2.mergeWithOperation(var3);
            }

         }
      }
   }

   private void mergeWithExistingOperationsOnSameApp() throws ManagementException {
      List var1 = weblogic.deploy.internal.adminserver.DeploymentManager.getInstance(kernelId).getExistingOperationsOnSameApp(this);
      var1.remove(this);
      if (isDebugEnabled()) {
         this.debugSay(" Found Same Operations : " + var1);
      }

      if (!var1.isEmpty()) {
         AbstractOperation var2 = (AbstractOperation)var1.get(0);
         DeploymentTaskRuntime var3 = this.getTaskRuntime();
         AppDeploymentMBean var4 = var3.getAppDeploymentMBean();
         String var5 = var4 == null ? null : var4.getName();
         this.mergeWithOperation(var2);
         if (var5 != null) {
            mergeOperationsOnAppAgain(var5);
         }

      }
   }

   private void mergeWithOperation(AbstractOperation var1) throws ManagementException {
      if (this != var1) {
         if (var1 instanceof ActivateOperation) {
            this.mergeWithDeploy(var1);
         } else if (var1 instanceof DistributeOperation) {
            this.mergeWithDistribute(var1);
         } else if (var1 instanceof UpdateOperation) {
            this.mergeWithUpdate(var1);
         } else if (var1 instanceof RedeployOperation) {
            this.mergeWithRedeploy(var1);
         } else if (var1 instanceof RemoveOperation) {
            this.mergeWithUndeploy(var1);
         }

      }
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

   public void undoChangesTriggeredByUser() {
      if (this.deployment != null) {
         deploymentManager.undoChangesTriggeredByUser(this.deployment);
      }
   }

   protected static boolean isDebugEnabled() {
      return Debug.isDeploymentDebugEnabled();
   }

   protected void debugSay(String var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append("(").append(this).append("): ").append(var1);
      Debug.deploymentDebug(var2.toString());
   }

   protected void setupDDPaths(DeploymentData var1) {
   }

   static {
      deploymentManager = weblogic.deploy.internal.adminserver.DeploymentManager.getInstance(kernelId);
      nextDelegateId = 0;
   }

   private static class ExtendedArrayList extends ArrayList {
      ExtendedArrayList(Collection var1) {
         super(var1);
      }

      ExtendedArrayList(String[] var1) {
         this((Collection)(var1 != null ? Arrays.asList(var1) : Collections.EMPTY_LIST));
      }

      boolean containsOne(String[] var1) {
         if (this.isEmpty()) {
            return false;
         } else if (var1 != null && var1.length != 0) {
            for(int var2 = 0; var2 < var1.length; ++var2) {
               if (this.contains(var1[var2])) {
                  return true;
               }
            }

            return false;
         } else {
            return false;
         }
      }
   }
}
