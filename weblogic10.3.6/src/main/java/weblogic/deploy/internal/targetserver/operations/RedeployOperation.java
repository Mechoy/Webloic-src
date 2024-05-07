package weblogic.deploy.internal.targetserver.operations;

import java.security.AccessController;
import java.util.Set;
import weblogic.application.Deployment;
import weblogic.application.utils.TargetUtils;
import weblogic.deploy.internal.InternalDeploymentData;
import weblogic.deploy.internal.TargetHelper;
import weblogic.deploy.internal.targetserver.AppDeployment;
import weblogic.deploy.internal.targetserver.DeployHelper;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.deploy.internal.AppRuntimeStateManager;
import weblogic.management.deploy.internal.MBeanConverter;
import weblogic.management.deploy.internal.SlaveDeployerLogger;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.StackTraceUtils;

public class RedeployOperation extends ActivateOperation {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private final String[] moduleIds;

   public RedeployOperation(long var1, String var3, InternalDeploymentData var4, BasicDeploymentMBean var5, DomainMBean var6, AuthenticatedSubject var7, boolean var8) throws DeploymentException {
      super(var1, var3, var4, var5, var6, var7, var8);
      this.operation = 9;
      this.appcontainer = this.getApplication().findDeployment();
      this.moduleIds = TargetHelper.getModulesForTarget(this.deploymentData, var6);
   }

   public AbstractOperation refine() throws DeploymentException {
      boolean var1 = false;
      if (this.deploymentData != null) {
         var1 = this.deploymentData.hasFiles();
         if (!var1) {
            String[] var2 = this.deploymentData.getGlobalTargets();
            boolean var3 = var2 != null && var2.length > 0;
            var1 = !var3 && this.deploymentData.hasSubModuleTargets();
         }
      }

      if (var1 && this.moduleIds != null) {
         String var4 = SlaveDeployerLogger.logBothStaticFileRedeployAndModuleRedeployLoggable().getMessage();
         throw new DeploymentException(var4);
      } else if (var1 && this.isAppContainerActive(this.appcontainer)) {
         return new DynamicUpdateOperation(this.requestId, this.taskId, this.internalDeploymentData, this.mbean, this.proposedDomain, this.initiator, this.requiresRestart);
      } else {
         return (AbstractOperation)(this.moduleIds != null && !var1 ? new ModuleRedeployOperation(this.requestId, this.taskId, this.internalDeploymentData, this.mbean, this.proposedDomain, this.moduleIds, this.initiator, this.requiresRestart) : this);
      }
   }

   protected void createAndPrepareContainer() throws DeploymentException {
      this.appcontainer = this.getApplication().createDeployment(this.mbean, this.getState());
      this.initializeDeploymentPlan();
      this.appcontainer.prepare(this.deploymentContext);
   }

   protected void doPrepare() throws DeploymentException {
      this.validatePrepare();
      this.ensureAppContainerSet();
      if (this.isDebugEnabled()) {
         this.debug("Preparing application " + this.getApplication().getName());
      }

      if (!this.isAdminState() || this.isAdminMode()) {
         if (this.isDebugEnabled()) {
            this.debug(" Redeploying " + this.getApplication().getName());
         }

         if (this.appcontainer != null) {
            this.unprepareDeployment(this.appcontainer);
         }

         try {
            this.commitDataUpdate();
            this.setupPrepare();
            this.createAndPrepareContainer();
            this.resetPendingRestartForSystemResource();
         } catch (Throwable var3) {
            if (this.isDebugEnabled()) {
               this.debug(StackTraceUtils.throwable2StackTrace(var3));
            }

            this.silentCancelOnPrepareFailure();
            DeploymentException var2 = DeployHelper.convertThrowable(var3);
            this.complete(2, var2);
            throw var2;
         }
      }
   }

   protected final void doCancel() {
      if (this.appcontainer != null) {
         if (this.isDebugEnabled()) {
            this.debug("RedeployOperation: Invoking undeploy on Container.");
         }

         this.unprepareDeployment(this.appcontainer);
         this.silentRemove(this.appcontainer);
         if (this.isDebugEnabled()) {
            this.debug("RedeployOperation: undeploy on Container finished.");
         }
      }

      this.getApplication().remove(false);
   }

   protected void validatePrepare() throws DeploymentException {
      if (this.internalDeploymentData.getDeploymentOperation() == 6 && this.getState(this.appcontainer) > 2) {
         String var1 = SlaveDeployerLogger.logInvalidDistributeLoggable(this.mbean.getName()).getMessage();
         this.isFailedInPrepareValidation = true;
         throw new DeploymentException(var1);
      }
   }

   private void initializeDeploymentPlan() throws DeploymentException {
      if (this.isAppDeployment() && this.mbean != null) {
         AppDeployment var1 = (AppDeployment)this.getApplication();
         DeploymentPlanBean var2 = var1.parsePlan();
         ((AppDeploymentMBean)this.mbean).setDeploymentPlanDescriptor(var2);
      }

   }

   protected void compatibilityProcessor() throws DeploymentException {
      MBeanConverter.reconcile81MBeans(this.deploymentData, (AppDeploymentMBean)this.mbean);
   }

   private void unprepareDeployment(Deployment var1) {
      if (this.getState(var1) == 3) {
         this.silentProductionToAdmin(var1);
      }

      if (this.getState(var1) > 1) {
         this.silentDeactivate(var1);
      }

      if (this.getState(var1) >= 1) {
         this.silentUnprepare(var1);
      }

      if (!this.isHomogenousDeployment()) {
         this.relayState();
      } else {
         try {
            AppRuntimeStateManager.getManager().remove(this.mbean.getName());
         } catch (Throwable var3) {
         }
      }

   }

   protected void recoverOnActivateFailure(Deployment var1) {
      this.silentUnprepare(var1);
   }

   private void relayState() {
      TargetMBean var1 = TargetUtils.findLocalTarget(this.mbean, server);
      if (this.getState().getTarget() == null) {
         this.getState().setTarget(var1.getName());
      }

      if (this.deploymentData == null) {
         this.getState().getIntendedState();
      } else {
         this.deploymentData.getIntendedState();
      }

      if (!ManagementService.getRuntimeAccess(kernelId).isAdminServer() && !this.isAppDeleted()) {
         AppRuntimeStateManager.getManager().updateStateForRedeployOperationOnCluster(this.mbean.getName(), this.getState());
      }

      if (this.isDebugEnabled()) {
         this.debug("Relaying updated state for app, " + this.getState().getId() + " to " + this.getState().getCurrentState() + ", taskState: " + this.getState().getTaskState());
      }

      deploymentManager.relayStatus(this.requestId, this.getState());
   }

   private boolean isHomogenousDeployment() {
      if (!(this.mbean instanceof AppDeploymentMBean)) {
         return true;
      } else if (cluster == null) {
         return true;
      } else {
         Set var1 = cluster.getServerNames();
         Set var2 = TargetHelper.getAllTargetedServers(this.mbean);
         return var2.containsAll(var1);
      }
   }
}
