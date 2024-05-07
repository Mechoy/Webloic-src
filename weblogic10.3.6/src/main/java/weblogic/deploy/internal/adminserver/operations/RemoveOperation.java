package weblogic.deploy.internal.adminserver.operations;

import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.deploy.beans.factory.InvalidTargetException;
import weblogic.deploy.common.Debug;
import weblogic.deploy.internal.TargetHelper;
import weblogic.logging.Loggable;
import weblogic.management.DeploymentException;
import weblogic.management.ManagementException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.deploy.DeploymentData;
import weblogic.management.deploy.DeploymentTaskRuntime;
import weblogic.management.deploy.internal.AppRuntimeStateManager;
import weblogic.management.deploy.internal.ApplicationRuntimeState;
import weblogic.management.deploy.internal.DeployerRuntimeLogger;
import weblogic.management.deploy.internal.RetirementManager;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.DeploymentTaskRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.StringUtils;

public final class RemoveOperation extends AbstractOperation {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private final String taskName;

   public RemoveOperation(int var1) {
      this.taskType = 4;
      this.taskName = OperationHelper.getTaskString(var1);
   }

   public final DeploymentTaskRuntimeMBean execute(String var1, String var2, String var3, DeploymentData var4, String var5, boolean var6, AuthenticatedSubject var7) throws ManagementException {
      String var8 = OperationHelper.ensureAppName(var2);
      String var9 = OperationHelper.getVersionIdFromData(var4, var2);
      if (isDebugEnabled()) {
         this.printDebugStartMessage(var1, var8, var9, var4, var5, this.taskName, var3);
      }

      if (var9 != null && var9.length() > 0) {
         return super.execute(var1, var2, var3, var4, var5, var6, var7);
      } else {
         ArrayList var10;
         if (OperationHelper.undeployAllVersions(var4)) {
            var10 = this.getNonActiveAppVersions(var2);
         } else {
            var10 = this.getRetiredAppVersions(var2);
         }

         if (var10 != null && var10.size() != 0) {
            boolean var11 = this.editAccessHelper.isCurrentEditor(var7);

            try {
               if (var11 && this.editAccessHelper.getEditDomainBean(var7) == null) {
                  var11 = false;
               }

               if (!var11) {
                  this.editAccessHelper.startEditSession(false);
               }
            } catch (ManagementException var19) {
               deploymentManager.deploymentFailedBeforeStart(this.deployment, var19, var11, var7, this.controlOperation);
               throw var19;
            }

            var4.getDeploymentOptions().setUseNonexclusiveLock(true);
            DeploymentTaskRuntimeMBean var12 = null;
            AppDeploymentMBean var13 = ApplicationVersionUtils.getActiveAppDeployment(var8);
            if (var13 != null) {
               if (Debug.isDeploymentDebugEnabled()) {
                  Debug.deploymentDebug("RemoveOperation.execute active app " + var13);
               }

               var12 = super.execute(var1, var13.getName(), var3, var4, var5, var6, var7);
            }

            DeploymentData var14 = new DeploymentData();
            var14.getDeploymentOptions().setUseNonexclusiveLock(true);
            Iterator var15 = var10.iterator();

            while(var15.hasNext()) {
               AppDeploymentMBean var16 = (AppDeploymentMBean)var15.next();
               if (Debug.isDeploymentDebugEnabled()) {
                  Debug.deploymentDebug("RemoveOperation.execute other app " + var16);
               }

               DeploymentTaskRuntimeMBean var17 = (new RemoveOperation(this.taskType)).execute((String)null, var16.getName(), var16.getStagingMode(), var14, RetirementManager.getRetireTaskId(var16.getName()), true, var7);
               if (var12 == null) {
                  var12 = var17;
               }
            }

            if (!var11) {
               try {
                  this.editAccessHelper.saveEditSessionChanges();
                  this.editAccessHelper.activateEditSessionChanges((long)var4.getTimeOut());
               } catch (ManagementException var18) {
                  deploymentManager.deploymentFailedBeforeStart(this.deployment, var18, var11, var7, this.controlOperation);
                  throw var18;
               }
            }

            return var12;
         } else {
            return super.execute(var1, var2, var3, var4, var5, var6, var7);
         }
      }
   }

   protected String getAutoDeployErrorMsg(String var1) {
      Loggable var2 = DeployerRuntimeLogger.invalidUndeployOnAutodeployedAppLoggable(var1);
      return var2.getMessage();
   }

   protected final AppDeploymentMBean updateConfiguration(String var1, String var2, String var3, DeploymentData var4, String var5, boolean var6) throws ManagementException {
      String var8 = OperationHelper.ensureAppName(var2);
      String var9 = OperationHelper.getVersionIdFromData(var4, var2);
      DomainMBean var10 = this.beanFactory.getEditableDomain();
      OperationHelper.assertNameIsNonNull(var2, this.taskName);
      AppDeploymentMBean var7 = ApplicationVersionUtils.getAppDeployment(var10, var8, var9);
      OperationHelper.assertAppIsNonNull(var7, var8, var9, this.taskName);
      this.verifyIfTargetsAreValid(var8, var9, var4, var7);
      var7 = OperationHelper.getActiveVersionIfNeeded(var10, var9, var7, var8, var4, this.taskName);
      var9 = var7.getVersionIdentifier();
      OperationHelper.validateUndeployWhileRetire(var8, var9, var7, var4, var5);
      return var7;
   }

   protected final void postTaskCreationConfigurationUpdate(AppDeploymentMBean var1, String var2, DeploymentData var3) throws ManagementException {
      DomainMBean var4 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      AppDeploymentMBean var5 = ApplicationVersionUtils.getAppDeployment(var4, var1.getName(), (String)null);
      if (var5 == null && Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("RemoveOperation.postTaskCreationConfigurationUpdate gets null AppMBean from runtime tree for " + var1.getName() + " !!!");
      }

      ((DeploymentTaskRuntime)this.deploymentTask).initMBeans(var5);

      try {
         this.beanFactory.removeTargetsInDeploymentData(var3, var1);
      } catch (InvalidTargetException var8) {
         Loggable var7 = DeployerRuntimeLogger.logAppNotTargetedLoggable(var8.getMessage(), var2);
         throw new ManagementException(var7.getMessage());
      }

      if (TargetHelper.getAllTargetedServers(var1).isEmpty()) {
         String var6 = var1.getName();
         this.beanFactory.removeMBean(var1);
         invalidateCache(var1);
      }

   }

   protected AbstractOperation createCopy() {
      return new RemoveOperation(this.taskType);
   }

   protected void mergeWithUndeploy(AbstractOperation var1) throws ManagementException {
      super.mergeWithUndeploy(var1);
      this.mergeWithSameOperationType(var1);
   }

   protected void mergeWithRedeploy(AbstractOperation var1) throws ManagementException {
      super.mergeWithRedeploy(var1);
      var1.mergeWithUndeploy(this);
   }

   protected void mergeWithDeploy(AbstractOperation var1) throws ManagementException {
      super.mergeWithDeploy(var1);
      var1.mergeWithUndeploy(this);
   }

   protected void mergeWithUpdate(AbstractOperation var1) throws ManagementException {
      super.mergeWithUpdate(var1);
      var1.mergeWithUndeploy(this);
   }

   protected void mergeWithDistribute(AbstractOperation var1) throws ManagementException {
      super.mergeWithDistribute(var1);
      var1.mergeWithUndeploy(this);
   }

   private ArrayList getNonActiveAppVersions(String var1) {
      String var2 = OperationHelper.ensureAppName(var1);
      AppDeploymentMBean var3 = ApplicationVersionUtils.getActiveAppDeployment(var2);
      if (var3 != null && var3.getVersionIdentifier() == null) {
         return null;
      } else {
         AppDeploymentMBean[] var4 = ApplicationVersionUtils.getAppDeployments(var2);
         if (var4 != null && var4.length != 0 && var4[0].getVersionIdentifier() != null) {
            ArrayList var5 = new ArrayList();

            for(int var6 = 0; var6 < var4.length; ++var6) {
               if (var3 == null || !var4[var6].getName().equals(var3.getName())) {
                  if (AppRuntimeStateManager.getManager().isRetiredVersion(var4[var6])) {
                     DeployerRuntimeLogger.logRemoveAllRetiredAppVersion(this.taskName, "allversions", var2, var4[var6].getVersionIdentifier());
                  } else {
                     DeployerRuntimeLogger.logRemoveAllRetiringAppVersion(this.taskName, "allversions", var2, var4[var6].getVersionIdentifier());
                  }

                  var5.add(var4[var6]);
               }
            }

            return var5;
         } else {
            return null;
         }
      }
   }

   private ArrayList getRetiredAppVersions(String var1) {
      String var2 = OperationHelper.ensureAppName(var1);
      AppDeploymentMBean var3 = ApplicationVersionUtils.getActiveAppDeployment(var2);
      if (var3 != null && var3.getVersionIdentifier() == null) {
         return null;
      } else {
         AppDeploymentMBean[] var4 = ApplicationVersionUtils.getAppDeployments(var2);
         if (var4 != null && var4.length != 0 && var4[0].getVersionIdentifier() != null) {
            ArrayList var5 = new ArrayList();

            for(int var6 = 0; var6 < var4.length; ++var6) {
               if (var3 == null || !var4[var6].getName().equals(var3.getName())) {
                  if (AppRuntimeStateManager.getManager().isRetiredVersion(var4[var6])) {
                     DeployerRuntimeLogger.logRemoveRetiredAppVersion(this.taskName, var2, var4[var6].getVersionIdentifier());
                     var5.add(var4[var6]);
                  } else {
                     DeployerRuntimeLogger.logRetiringAppVersionNotRemoved(this.taskName, var2, var4[var6].getVersionIdentifier());
                  }
               }
            }

            return var5;
         } else {
            return null;
         }
      }
   }

   private void verifyIfTargetsAreValid(String var1, String var2, DeploymentData var3, AppDeploymentMBean var4) throws DeploymentException, ManagementException {
      if (var3 != null) {
         if (var3.hasModuleTargets()) {
            this.verifyModuleTargets(var4, var3.getAllModuleTargets());
         }

         if (var3.getGlobalTargets().length > 0) {
            OperationHelper.checkForClusterTargetSubset(this.beanFactory.getEditableDomain(), var3, var4, this.taskName);
            this.verifyForPinnedDeployment(var1, var2, var3, var4);
         }

      }
   }

   private void verifyModuleTargets(AppDeploymentMBean var1, Map var2) throws DeploymentException {
      String var3 = var1.getName();
      ApplicationRuntimeState var4 = AppRuntimeStateManager.getManager().get(var3);
      if (var4 != null) {
         Map var5 = var4.getModules();
         Map var6 = OperationHelper.createTargetMap(var1);
         Set var7 = var5.keySet();
         Iterator var8 = var2.keySet().iterator();

         while(var8.hasNext()) {
            String var9 = (String)var8.next();
            if (!var7.contains(var9) && !var6.keySet().contains(var9)) {
               String var12 = StringUtils.join(AppRuntimeStateManager.getManager().getModuleIds(var3), ",");
               Loggable var13 = DeployerRuntimeLogger.logNoSuchModuleLoggable(var3, var9, var12);
               throw new DeploymentException(var13.getMessage());
            }

            Set var10 = var6.keySet();
            String[] var11 = (String[])((String[])var2.get(var9));
            if (var10.contains(var9)) {
               this.compareTargetNames(var9, var11, (Set)var6.get(var9));
            } else {
               this.compareTargetNames(var9, var11, (Set)var6.get(var3));
            }
         }

      }
   }

   private void compareTargetNames(String var1, String[] var2, Set var3) throws DeploymentException {
      for(int var4 = 0; var4 < var2.length; ++var4) {
         if (!var3.contains(var2[var4])) {
            Loggable var5 = DeployerRuntimeLogger.logAppNotTargetedLoggable(var2[var4], var1);
            throw new DeploymentException(var5.getMessage());
         }
      }

   }

   private void verifyForPinnedDeployment(String var1, String var2, DeploymentData var3, AppDeploymentMBean var4) throws DeploymentException {
      DomainMBean var5 = this.beanFactory.getEditableDomain();
      ClusterMBean[] var6 = var5.getClusters();
      if (var6 != null && var6.length != 0) {
         String[] var7 = var3.getGlobalTargets();
         if (var7 != null && var7.length != 0) {
            HashMap var8 = new HashMap();
            TargetMBean[] var9 = var4.getTargets();

            int var10;
            for(var10 = 0; var10 < var6.length; ++var10) {
               ClusterMBean var11 = var6[var10];
               List var12 = this.getPinnedServers(var11, var9);
               if (!var12.isEmpty()) {
                  var8.put(var11, var12);
               }
            }

            if (!var8.isEmpty()) {
               for(var10 = 0; var10 < var7.length; ++var10) {
                  Iterator var17 = var8.keySet().iterator();

                  while(var17.hasNext()) {
                     ClusterMBean var18 = (ClusterMBean)var17.next();
                     List var13 = (List)var8.get(var18);
                     if (!var13.contains(var7[var10])) {
                        Set var14 = var18.getServerNames();
                        if (var14.contains(var7[var10])) {
                           String var15 = ApplicationVersionUtils.getApplicationId(var1, var2);
                           Loggable var16 = DeployerRuntimeLogger.logInvalidTargetForPinnedAppUndeployLoggable(var15, var7[var10], this.taskName);
                           throw new DeploymentException(var16.getMessage());
                        }
                     }
                  }
               }

            }
         }
      }
   }

   private List getPinnedServers(ClusterMBean var1, TargetMBean[] var2) {
      ArrayList var3 = new ArrayList();
      if (var2 != null) {
         Set var4 = var1.getServerNames();

         for(int var5 = 0; var5 < var2.length; ++var5) {
            if (var2[var5] instanceof ServerMBean) {
               String var6 = var2[var5].getName();
               if (var4.contains(var6)) {
                  var3.add(var6);
               }
            }
         }
      }

      return var3;
   }

   protected void defaultSubModuleTargets(String var1, String var2, DeploymentData var3, DomainMBean var4) throws ManagementException {
   }

   protected boolean isRemote(DeploymentData var1) {
      return false;
   }
}
