package weblogic.deploy.service.internal.statemachines.adminserver;

import weblogic.deploy.service.internal.DeploymentServiceLogger;
import weblogic.deploy.service.internal.InvalidStateException;
import weblogic.deploy.service.internal.adminserver.TimeAuditorManager;

public final class AwaitingPrepareResponses extends AdminServerState {
   public AdminServerState cancel() throws InvalidStateException {
      this.fireStateTransitionEvent(this, "requestCancelled", this.requestId);
      return this.getCurrentState();
   }

   public AdminServerState requestTimedOut() {
      this.fireStateTransitionEvent(this, "requestTimedout", this.requestId);
      this.doCancelCheck();
      return this.getCurrentState();
   }

   public final AdminServerState receivedPrepareSucceeded(String var1, boolean var2) {
      this.fireStateTransitionEvent(this, "receivedPrepareSucceeded", this.requestId);
      this.request.getStatus().receivedPrepareSucceededFrom(var1, var2);
      this.doPrepareCompletionCheck();
      return this.getCurrentState();
   }

   public final AdminServerState receivedPrepareFailed(String var1, Throwable var2, boolean var3) {
      this.fireStateTransitionEvent(this, "receivedPrepareFailed", this.requestId);
      this.request.getStatus().receivedPrepareFailedFrom(var1, var2, var3);
      this.doPrepareCompletionCheck();
      return this.getCurrentState();
   }

   private final boolean doCancelCheck() {
      if (this.request.toBeCancelled()) {
         this.sendCancel();
         return true;
      } else {
         return false;
      }
   }

   protected final void doPrepareCompletionCheck() {
      if (this.request.getStatus().receivedAllPrepareResponses()) {
         if (this.doCancelCheck()) {
            return;
         }

         TimeAuditorManager.getInstance().endTransition(this.requestId, 1);
         this.sendCommit();
      }

   }

   private final void sendCommit() {
      this.request.getStatus().signalDeploySucceeded();
      this.setExpectedNextState(2);
      TimeAuditorManager.getInstance().startTransition(this.requestId, 2);
      this.sender.sendRequestCommitMsg(this.request);
      this.request.getStatus().scheduleNextRequest();
   }

   private final void sendCancel() {
      this.setExpectedNextState(4);
      Object var1;
      if (this.request.getStatus().isCancelledByUser()) {
         var1 = new Exception(DeploymentServiceLogger.logCancelledLoggable(this.requestId).getMessage());
      } else if (this.request.getStatus().isCancelledByClusterConstraints()) {
         var1 = new Exception(DeploymentServiceLogger.cancelledDueToClusterConstraints(this.requestId, ""));
      } else if (this.request.getStatus().timedOut()) {
         var1 = new Exception(DeploymentServiceLogger.logRequestTimedOutLoggable(this.requestId).getMessage());
      } else {
         var1 = this.request.getStatus().getPrepareFailure();
      }

      TimeAuditorManager.getInstance().startTransition(this.requestId, 3);
      if (this.request.getStatus().hasTargetsToBeCancelled()) {
         this.sender.sendRequestCancelMsg(this.getDeploymentRequest(), (Throwable)var1);
      } else {
         this.getCurrentState().allCancelsDelivered();
      }

   }

   public final String toString() {
      return "AwaitingPrepareResponses";
   }
}
