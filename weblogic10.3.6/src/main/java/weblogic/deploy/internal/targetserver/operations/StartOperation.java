package weblogic.deploy.internal.targetserver.operations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import weblogic.application.Deployment;
import weblogic.application.internal.DeploymentStateChecker;
import weblogic.deploy.api.spi.DeploymentOptions;
import weblogic.deploy.container.NonFatalDeploymentException;
import weblogic.deploy.internal.InternalDeploymentData;
import weblogic.deploy.internal.TargetHelper;
import weblogic.deploy.internal.targetserver.DeployHelper;
import weblogic.deploy.internal.targetserver.state.TargetModuleState;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.deploy.internal.DeployerRuntimeLogger;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.utils.StackTraceUtils;

public class StartOperation extends ActivateOperation {
   private String[] moduleIds = null;
   private boolean isModuleLevelStart = false;

   public StartOperation(long var1, String var3, InternalDeploymentData var4, BasicDeploymentMBean var5, DomainMBean var6, AuthenticatedSubject var7, boolean var8) throws DeploymentException {
      super(var1, var3, var4, var5, var6, var7, var8);
      this.operation = 7;
      DeploymentOptions var9 = this.deploymentData.getDeploymentOptions();
      if (var9 == null || !var9.isDisableModuleLevelStartStop()) {
         this.moduleIds = TargetHelper.getModulesForTarget(this.deploymentData, var6);
      }

      if (!this.isDistributed()) {
         throw new DeploymentException("Application must be distributed before a start operation.");
      } else {
         this.controlOperation = true;
      }
   }

   private boolean isDistributed() {
      return true;
   }

   protected void createAndPrepareContainer() throws DeploymentException {
      this.appcontainer = this.getApplication().findDeployment();
      if (this.appcontainer != null && this.moduleIds != null) {
         String[] var1 = this.getFilteredModIds();
         this.deploymentContext.setUpdatedResourceURIs(var1);
         if (var1.length != 0) {
            this.appcontainer.start(this.deploymentContext);
            this.isModuleLevelStart = true;
         }
      } else if (!this.isAdminState() || this.isAdminMode()) {
         if (this.appcontainer == null || this.getState(this.appcontainer) < 1) {
            super.createAndPrepareContainer();
         }
      }
   }

   protected void doPrepare() throws DeploymentException {
      this.validatePrepare();
      if (this.isDebugEnabled()) {
         this.debug("Preparing application " + this.getApplication().getName());
      }

      try {
         this.setupPrepare();
         this.createAndPrepareContainer();
      } catch (Throwable var3) {
         if (this.isDebugEnabled()) {
            this.debug("Preparing application " + this.getApplication().getName() + " Failed and Exception is : " + StackTraceUtils.throwable2StackTrace(var3));
         }

         if (!(var3 instanceof NonFatalDeploymentException)) {
            this.silentCancelOnPrepareFailure();
         }

         DeploymentException var2 = DeployHelper.convertThrowable(var3);
         this.complete(2, var2);
         throw var2;
      }
   }

   protected void validatePrepare() throws DeploymentException {
      this.appcontainer = this.getApplication().findDeployment();
      if (this.appcontainer != null) {
         int var1 = this.getState(this.appcontainer);
         if (var1 == 4 || var1 == 3 && this.isAdminMode()) {
            String var2 = DeployerRuntimeLogger.illegalStateForStart(DeploymentStateChecker.state2String(var1));
            this.isFailedInPrepareValidation = true;
            throw new DeploymentException(var2);
         }
      }

   }

   protected void doCommit() throws IOException, DeploymentException {
      Deployment var1 = this.getApplication().findDeployment();
      if (!this.isModuleLevelStart && this.getState(var1) != 3) {
         super.doCommit();
      } else {
         this.complete(3, (Exception)null);
      }

   }

   protected final void doCancel() {
      if (this.appcontainer != null) {
         if (this.isDebugEnabled()) {
            this.debug("StartOperation: Invoking undeploy on Container.");
         }

         if (this.getState(this.appcontainer) == 3) {
            this.silentProductionToAdmin(this.appcontainer);
         }

         if (this.getState(this.appcontainer) > 1) {
            this.silentDeactivate(this.appcontainer);
         }

         if (this.getState(this.appcontainer) >= 1) {
            this.silentUnprepare(this.appcontainer);
         }

         this.silentRemove(this.appcontainer);
         if (this.isDebugEnabled()) {
            this.debug("StartOperation: undeploy on Container finished.");
         }
      }

      this.getApplication().remove(false);
   }

   protected final boolean isDeploymentRequestValidForCurrentServer() {
      return this.isTargetListContainsCurrentServer();
   }

   private String[] getFilteredModIds() {
      ArrayList var1 = new ArrayList();
      Map var2 = this.app.getAppRuntimeState().getModules();
      Map var3 = this.deploymentData.getAllModuleTargets();

      for(int var4 = 0; var4 < this.moduleIds.length; ++var4) {
         if (var2.get(this.moduleIds[var4]) != null && var3.get(this.moduleIds[var4]) != null) {
            Map var5 = (Map)var2.get(this.moduleIds[var4]);
            String[] var6 = (String[])((String[])var3.get(this.moduleIds[var4]));

            for(int var7 = 0; var7 < var6.length; ++var7) {
               Map var8 = null;
               if (var5.get(var6[var7]) != null) {
                  var8 = (Map)var5.get(var6[var7]);
                  Object var9 = var8.get(serverName);
                  if (var9 != null) {
                     TargetModuleState var10 = null;
                     if (var9 instanceof TargetModuleState) {
                        var10 = (TargetModuleState)var9;
                     } else if (var9 instanceof Map) {
                        var10 = (TargetModuleState)((TargetModuleState)((Map)var9).get(serverName));
                     }

                     if (this.isDebugEnabled() && var10 != null) {
                        this.debug("Module: " + this.moduleIds[var4] + " state is " + var10.getCurrentState());
                     }

                     if ("STATE_NEW".equals(var10.getCurrentState())) {
                        var1.add(this.moduleIds[var4]);
                        break;
                     }
                  }
               }
            }
         }
      }

      return (String[])((String[])var1.toArray(new String[0]));
   }

   public void initDataUpdate() throws DeploymentException {
   }
}
