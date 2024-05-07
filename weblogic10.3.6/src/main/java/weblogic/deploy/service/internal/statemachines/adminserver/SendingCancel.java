package weblogic.deploy.service.internal.statemachines.adminserver;

import weblogic.deploy.service.internal.DeploymentServiceLogger;
import weblogic.deploy.service.internal.InvalidStateException;
import weblogic.deploy.service.internal.adminserver.AdminRequestManager;

public final class SendingCancel extends AdminServerState {
   public final AdminServerState cancel() throws InvalidStateException {
      throw new InvalidStateException(DeploymentServiceLogger.logAlreadyCancelledLoggable(this.requestId, this.toString()).getMessage());
   }

   public final AdminServerState requestTimedOut() {
      this.fireStateTransitionEvent(this, "requestTimedout", this.requestId);
      synchronized(AdminRequestManager.getInstance()) {
         this.request.getStatus().signalCancelFailed(true);
         return null;
      }
   }

   public final AdminServerState receivedPrepareSucceeded(String var1, boolean var2) {
      this.fireStateTransitionEvent(this, "receivedPrepareSucceeded", this.requestId);
      return this.getCurrentState();
   }

   public AdminServerState receivedPrepareFailed(String var1, Throwable var2, boolean var3) {
      this.fireStateTransitionEvent(this, "receivedPrepareFailed", this.requestId);
      return this.getCurrentState();
   }

   public final AdminServerState receivedCancelSucceeded(String var1) {
      this.fireStateTransitionEvent(this, "receivedCancelSucceeded", this.requestId);
      this.request.getStatus().receivedCancelSucceededFrom(var1);
      return this.getCurrentState();
   }

   public final AdminServerState receivedCancelFailed(String var1, Throwable var2) {
      this.fireStateTransitionEvent(this, "receivedCancelFailed", this.requestId);
      this.request.getStatus().receivedCancelFailedFrom(var1, var2);
      return this.getCurrentState();
   }

   public final AdminServerState allCancelsDelivered() {
      this.fireStateTransitionEvent(this, "cancelsDelivered", this.requestId);
      this.setExpectedNextState(5);
      AdminServerState var1 = this.getCurrentState();
      if (var1 != null) {
         var1.doCancelCompletionCheck();
      }

      return this.getCurrentState();
   }

   public final String toString() {
      return "SendingCancel";
   }
}
