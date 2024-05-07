package weblogic.deploy.service.internal.statemachines.adminserver;

import weblogic.deploy.service.internal.DeploymentServiceLogger;
import weblogic.deploy.service.internal.InvalidStateException;
import weblogic.deploy.service.internal.adminserver.AdminRequestManager;
import weblogic.deploy.service.internal.adminserver.TimeAuditorManager;

public final class AwaitingCommitResponses extends AdminServerState {
   public final AdminServerState cancel() throws InvalidStateException {
      throw new InvalidStateException(DeploymentServiceLogger.logTooLateToCancelLoggable(this.requestId, this.toString()).getMessage());
   }

   public final AdminServerState requestTimedOut() {
      this.fireStateTransitionEvent(this, "requestTimedout", this.requestId);
      synchronized(AdminRequestManager.getInstance()) {
         this.request.getStatus().signalCommitFailed();
         return null;
      }
   }

   public AdminServerState receivedCommitSucceeded(String var1) {
      this.fireStateTransitionEvent(this, "receivedCommitSucceeded", this.requestId);
      this.request.getStatus().receivedCommitSucceededFrom(var1);
      return this.doCommitCompletionCheck() ? null : this.getCurrentState();
   }

   public AdminServerState receivedCommitFailed(String var1, Throwable var2) {
      this.fireStateTransitionEvent(this, "receivedCommitFailed", this.requestId);
      this.request.getStatus().receivedCommitFailedFrom(var1, var2);
      return this.doCommitCompletionCheck() ? null : this.getCurrentState();
   }

   protected final boolean doCommitCompletionCheck() {
      if (this.request.getStatus().receivedAllCommitResponses()) {
         TimeAuditorManager.getInstance().endTransition(this.requestId, 2);
         this.signalCommitCompletion();
         return true;
      } else {
         return false;
      }
   }

   private final void signalCommitCompletion() {
      if (this.request.getStatus().commitFailed()) {
         this.request.getStatus().signalCommitFailed();
      } else {
         this.request.getStatus().signalCommitSucceeded();
      }

   }

   public final String toString() {
      return "AwaitingCommitResponses";
   }
}
