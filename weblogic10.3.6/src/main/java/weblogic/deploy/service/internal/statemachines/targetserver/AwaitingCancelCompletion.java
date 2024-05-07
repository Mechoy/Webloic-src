package weblogic.deploy.service.internal.statemachines.targetserver;

import weblogic.deploy.service.internal.DeploymentServiceLogger;
import weblogic.deploy.service.internal.DomainVersion;

public class AwaitingCancelCompletion extends TargetServerState {
   public final TargetServerState cancelSucceeded() {
      this.fireStateTransitionEvent(this, "cancelSucceeded", this.getId());
      if (this.deploymentStatus != null) {
         if (this.deploymentStatus.isCanceled()) {
            this.sendCancelSucceeded();
         } else {
            if (!this.deploymentStatus.isTimedOut() && !this.deploymentStatus.isAborted()) {
               DomainVersion var1 = this.deploymentsManager.getCurrentDomainVersion();
               this.syncWithAdminServer(var1);
               return this.getCurrentState();
            }

            this.deploymentStatus.reset();
         }
      }

      return null;
   }

   public final TargetServerState cancelFailed() {
      this.fireStateTransitionEvent(this, "cancelFailed", this.getId());
      if (this.deploymentStatus != null) {
         if (this.deploymentStatus.isCanceled()) {
            this.sendCancelFailed(this.deploymentStatus.getCancelFailureError());
         } else if (this.deploymentStatus.isTimedOut()) {
            this.deploymentStatus.reset();
         } else {
            String var1 = DeploymentServiceLogger.optimisticConcurrencyErr(this.getId());
            Exception var2 = new Exception(var1, this.deploymentStatus.getCancelFailureError());
            this.sendCancelFailed(var2);
         }
      }

      return null;
   }

   public final String toString() {
      return "AwaitingCancelCompletion";
   }
}
