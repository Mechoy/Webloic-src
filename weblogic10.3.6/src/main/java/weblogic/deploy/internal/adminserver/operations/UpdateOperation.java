package weblogic.deploy.internal.adminserver.operations;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.deploy.service.datatransferhandlers.SourceCache;
import weblogic.j2ee.descriptor.wl.ModuleDescriptorBean;
import weblogic.j2ee.descriptor.wl.ModuleOverrideBean;
import weblogic.logging.Loggable;
import weblogic.management.ApplicationException;
import weblogic.management.DeploymentException;
import weblogic.management.ManagementException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.deploy.DeploymentData;
import weblogic.management.deploy.internal.DeployerRuntimeLogger;

public class UpdateOperation extends AbstractOperation {
   public UpdateOperation() {
      this.taskType = 10;
   }

   protected String getAutoDeployErrorMsg(String var1) {
      Loggable var2 = DeployerRuntimeLogger.invalidRedeployOnAutodeployedAppLoggable(var1);
      return var2.getMessage();
   }

   protected AppDeploymentMBean updateConfiguration(String var1, String var2, String var3, DeploymentData var4, String var5, boolean var6) throws ManagementException {
      String var8 = OperationHelper.ensureAppName(var2);
      String var9 = OperationHelper.getVersionIdFromData(var4, var2);
      String var10 = OperationHelper.getTaskString(this.taskType);
      DomainMBean var11 = this.beanFactory.getEditableDomain();
      if (isDebugEnabled()) {
         this.printDebugStartMessage(var1, var8, var9, var4, var5, var10, var3);
      }

      OperationHelper.assertNameIsNonNull(var8, var10);
      AppDeploymentMBean var7 = ApplicationVersionUtils.getAppDeployment(var11, var8, var9);

      Loggable var13;
      try {
         OperationHelper.assertAppIsNonNull(var7, var8, var9, var10);
         OperationHelper.assertInfoIsNonNull(var4, var8, var9);
         OperationHelper.assertPlanIsNonNull(var4);
         var7 = OperationHelper.getActiveVersionIfNeeded(var11, var9, var7, var8, var4, var10);
         this.reconcileMBeans(var4, var7);
         return var7;
      } catch (ApplicationException var14) {
         var13 = DeployerRuntimeLogger.logInvalidAppLoggable(var7.getAbsoluteSourcePath(), var8, var14.getMessage());
         var13.log();
         throw new ManagementException(var13.getMessage(), var14);
      } catch (FileNotFoundException var15) {
         var13 = DeployerRuntimeLogger.logInvalidSourceLoggable(var7.getAbsoluteSourcePath(), var8, var15.getMessage());
         var13.log();
         throw new ManagementException(var13.getMessage(), var15);
      }
   }

   protected void reconcileMBeans(DeploymentData var1, AppDeploymentMBean var2, boolean var3) throws ApplicationException, FileNotFoundException, ManagementException {
      super.reconcileMBeans(var1, var2, var3);
      String var4 = var1.getConfigDirectory();
      if (var4 != null) {
         File var5 = new File(var4);
         String[] var6 = var5.list();
         if (var6 == null || var6.length == 0) {
            var4 = null;
         }
      }

      var2.setPlanDir(var4);
      var2.setPlanPath(var1.getDeploymentPlan());
      SourceCache.updateDescriptorsInCache(var2);
   }

   protected AbstractOperation createCopy() {
      return new UpdateOperation();
   }

   protected void mergeWithUndeploy(AbstractOperation var1) throws ManagementException {
      super.mergeWithUndeploy(var1);
      throw new DeploymentException("UpdateOperation cannot be merged with UndeployOperation");
   }

   protected void mergeWithRedeploy(AbstractOperation var1) throws ManagementException {
      super.mergeWithRedeploy(var1);
      var1.mergeWithUpdate(this);
   }

   protected void mergeWithDeploy(AbstractOperation var1) throws ManagementException {
      super.mergeWithDeploy(var1);
      var1.mergeWithUpdate(this);
   }

   protected void mergeWithUpdate(AbstractOperation var1) throws ManagementException {
      super.mergeWithUpdate(var1);
      this.mergeWithSameOperationType(var1);
   }

   protected void mergeWithDistribute(AbstractOperation var1) throws ManagementException {
      super.mergeWithDistribute(var1);
      var1.mergeWithUpdate(this);
   }

   protected void setupDDPaths(DeploymentData var1) {
      if (this.planBean != null && var1 != null && !var1.hasFiles()) {
         ModuleOverrideBean[] var2 = this.planBean.getModuleOverrides();
         if (var2 != null) {
            String var3 = null;

            for(int var4 = 0; var4 < var2.length; ++var4) {
               if (this.planBean.rootModule(var2[var4].getModuleName())) {
                  var3 = var2[var4].getModuleName();
                  break;
               }
            }

            ArrayList var7 = new ArrayList();

            for(int var8 = 0; var8 < var2.length; ++var8) {
               boolean var10 = var2[var8].getModuleName().equals(var3);
               ModuleDescriptorBean[] var5 = var2[var8].getModuleDescriptors();
               if (var5 != null) {
                  for(int var9 = 0; var9 < var5.length; ++var9) {
                     String var6 = var5[var9].getUri();
                     if (!var10) {
                        var6 = var2[var8].getModuleName() + "/" + var6;
                     }

                     if (this.hasOverrides(var5[var9], var6, var1) && !var7.contains(var6)) {
                        var7.add(var6);
                     }
                  }
               }
            }

            var1.setFile((String[])((String[])var7.toArray(new String[var7.size()])));
         }
      }
   }

   private boolean hasOverrides(ModuleDescriptorBean var1, String var2, DeploymentData var3) {
      if (var1.getVariableAssignments() != null && var1.getVariableAssignments().length > 0) {
         return true;
      } else if (var1.isExternal() && var3.getConfigDirectory() != null) {
         File var4 = new File(var3.getConfigDirectory(), var2);
         return var4.exists();
      } else {
         return false;
      }
   }
}
