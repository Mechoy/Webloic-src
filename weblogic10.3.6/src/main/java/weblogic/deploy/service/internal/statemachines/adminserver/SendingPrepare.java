package weblogic.deploy.service.internal.statemachines.adminserver;

import weblogic.deploy.service.internal.InvalidStateException;
import weblogic.deploy.service.internal.adminserver.TimeAuditorManager;

public final class SendingPrepare extends AdminServerState {
   public final AdminServerState start() throws InvalidStateException {
      this.fireStateTransitionEvent(this, "start", this.requestId);
      TimeAuditorManager.getInstance().startAuditor(this.requestId);
      TimeAuditorManager.getInstance().startTransition(this.requestId, 1);
      return this.sendDeploymentRequest();
   }

   public AdminServerState requestTimedOut() {
      this.fireStateTransitionEvent(this, "requestTimedout", this.requestId);
      return this.getCurrentState();
   }

   public final AdminServerState receivedPrepareSucceeded(String var1, boolean var2) {
      this.fireStateTransitionEvent(this, "receivedPrepareSucceeded", this.requestId);
      this.request.getStatus().receivedPrepareSucceededFrom(var1, var2);
      return this.getCurrentState();
   }

   public AdminServerState receivedPrepareFailed(String var1, Throwable var2, boolean var3) {
      this.fireStateTransitionEvent(this, "receivedPrepareFailed", this.requestId);
      this.request.getStatus().receivedPrepareFailedFrom(var1, var2, var3);
      return this.getCurrentState();
   }

   public final AdminServerState allPreparesDelivered() {
      this.fireStateTransitionEvent(this, "preparesDelivered", this.requestId);
      this.setExpectedNextState(1);
      this.getCurrentState().doPrepareCompletionCheck();
      return this.getCurrentState();
   }

   private AdminServerState sendDeploymentRequest() {
      this.sender.sendRequestPrepareMsg(this.getDeploymentRequest());
      return this.getCurrentState();
   }

   public final String toString() {
      return "SendingPrepare";
   }
}
