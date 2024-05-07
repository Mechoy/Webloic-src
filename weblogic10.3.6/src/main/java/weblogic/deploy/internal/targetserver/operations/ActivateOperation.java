package weblogic.deploy.internal.targetserver.operations;

import java.io.IOException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.List;
import weblogic.application.Deployment;
import weblogic.application.internal.DeploymentStateChecker;
import weblogic.deploy.internal.InternalDeploymentData;
import weblogic.deploy.internal.targetserver.AppDeployment;
import weblogic.deploy.internal.targetserver.BasicDeployment;
import weblogic.deploy.internal.targetserver.DeployHelper;
import weblogic.deploy.internal.targetserver.datamanagement.DataUpdateRequestInfo;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.SystemResourceMBean;
import weblogic.management.deploy.internal.MBeanConverter;
import weblogic.management.deploy.internal.SlaveDeployerLogger;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.AssertionError;

public class ActivateOperation extends AbstractOperation {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   boolean isFailedInPrepareValidation = false;

   public ActivateOperation(long var1, String var3, InternalDeploymentData var4, BasicDeploymentMBean var5, DomainMBean var6, AuthenticatedSubject var7, boolean var8) throws DeploymentException {
      super(var1, var3, var4, var5, var6, var7, var8);
      this.operation = 1;
   }

   public AbstractOperation refine() throws DeploymentException {
      BasicDeployment var1 = this.getApplication();
      if (var1 == null) {
         String var2 = SlaveDeployerLogger.logFailedToFindDeploymentLoggable(this.internalDeploymentData.getDeploymentName()).getMessage();
         throw new AssertionError(var2);
      } else {
         this.appcontainer = var1.findDeployment();
         return (AbstractOperation)(this.appcontainer == null ? this : new RedeployOperation(this.requestId, this.taskId, this.internalDeploymentData, this.mbean, this.proposedDomain, this.initiator, this.requiresRestart));
      }
   }

   protected void compatibilityProcessor() throws DeploymentException {
      MBeanConverter.setupNew81MBean((AppDeploymentMBean)this.mbean);
   }

   protected void doPrepare() throws DeploymentException {
      this.validatePrepare();
      if (this.isDebugEnabled()) {
         this.debug("Preparing application " + this.getApplication().getName());
      }

      if (!this.isAdminState() || this.isAdminMode()) {
         try {
            this.commitDataUpdate();
            this.setupPrepare();
            this.createAndPrepareContainer();
            this.resetPendingRestartForSystemResource();
         } catch (Throwable var3) {
            this.silentCancelOnPrepareFailure();
            DeploymentException var2 = DeployHelper.convertThrowable(var3);
            this.complete(2, var2);
            throw var2;
         }
      }
   }

   protected void doCommit() throws IOException, DeploymentException {
      this.appcontainer = this.getApplication().findDeployment();
      if (this.internalDeploymentData.getDeploymentOperation() != 6) {
         this.activateDeployment();
      }

      this.complete(3, (Exception)null);
   }

   protected void doCancel() {
      if (this.appcontainer != null) {
         int var1 = this.getState(this.appcontainer);
         if (var1 == 1) {
            if (this.isDebugEnabled()) {
               this.debug("ActivateOperation: Invoking unprepare() on Container.");
            }

            this.silentUnprepare(this.appcontainer);
            if (this.isDebugEnabled()) {
               this.debug("ActivateOperation: Invoking unprepare() on Container.");
            }
         }

         this.silentRemove(this.appcontainer);
      }

      this.getApplication().remove();
   }

   protected boolean isCancelNecessary() {
      return !this.isFailedInPrepareValidation;
   }

   protected void activateDeployment() throws DeploymentException {
      if (this.appcontainer != null) {
         if (this.isDebugEnabled()) {
            this.debug(" ActivateOperation - Activating application " + this.getApplication().getName());
         }

         try {
            this.activate(this.appcontainer);
         } catch (DeploymentException var3) {
            this.recoverOnActivateFailure(this.appcontainer);
            throw var3;
         }

         if (this.getState(this.appcontainer) == 3) {
            return;
         }

         if (!this.isAdminMode()) {
            try {
               if (this.isServerInAdminMode()) {
                  if (this.getApplication().getState() != null) {
                     this.getApplication().getState().setIntendedState("STATE_ACTIVE");
                  }
               } else {
                  this.appcontainer.adminToProduction(this.deploymentContext);
               }
            } catch (DeploymentException var2) {
               this.silentDeactivate(this.appcontainer);
               this.recoverOnActivateFailure(this.appcontainer);
               throw var2;
            }
         }
      }

   }

   protected void recoverOnActivateFailure(Deployment var1) {
      this.silentUnprepare(var1);
      this.silentRemove(var1);
      this.getApplication().remove();
      if (this.getApplication().getState() != null) {
         this.getApplication().getState().setCurrentState("STATE_FAILED", true);
      }

   }

   protected void unprepareDeployment() throws DeploymentException {
      if (this.appcontainer != null) {
         if (this.isDebugEnabled()) {
            this.debug(" ActivateOperation - unpreparing application " + this.getApplication().getName());
         }

         this.appcontainer.unprepare(this.deploymentContext);
      }

   }

   protected void createAndPrepareContainer() throws DeploymentException {
      if (this.isDebugEnabled()) {
         this.debug(" Creating application container for " + this.getApplication().getName());
      }

      this.appcontainer = this.getApplication().createDeployment(this.mbean, this.getState());
      this.initializeDeploymentPlan();
      boolean var1 = this.isAdminMode();
      this.deploymentContext.setAdminModeTransition(var1);
      this.appcontainer.prepare(this.deploymentContext);
   }

   protected boolean isAdminState() {
      if (this.appcontainer == null) {
         this.appcontainer = this.getApplication().findDeployment();
      }

      return this.appcontainer != null && this.isAdminState(this.appcontainer);
   }

   private void initializeDeploymentPlan() throws DeploymentException {
      if (this.isAppDeployment() && this.mbean != null) {
         AppDeployment var1 = (AppDeployment)this.getApplication();
         String var2 = ((AppDeploymentMBean)this.mbean).getPlanPath();
         DeploymentPlanBean var3 = var1.parsePlan();
         ((AppDeploymentMBean)this.mbean).setDeploymentPlanDescriptor(var3);
      }

   }

   protected void validatePrepare() throws DeploymentException {
      this.appcontainer = this.getApplication().findDeployment();
      if (this.appcontainer != null && this.getState(this.appcontainer) > 0) {
         String var1 = SlaveDeployerLogger.illegalStateForDeploy(DeploymentStateChecker.state2String(this.getState(this.appcontainer)));
         this.isFailedInPrepareValidation = true;
         throw new DeploymentException(var1);
      }
   }

   public void initDataUpdate() throws DeploymentException {
      this.getApplication().initDataUpdate(new DataUpdateRequestInfo() {
         public List getDeltaFiles() {
            return new ArrayList();
         }

         public long getRequestId() {
            return ActivateOperation.this.requestId;
         }

         public boolean isStatic() {
            return false;
         }

         public boolean isDelete() {
            return false;
         }

         public boolean isPlanUpdate() {
            return false;
         }
      });
   }

   protected void resetPendingRestartForSystemResource() {
      if (this.mbean instanceof SystemResourceMBean) {
         ServerRuntimeMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServerRuntime();
         var1.removePendingRestartSystemResource(this.mbean.getName());
      }

   }
}
