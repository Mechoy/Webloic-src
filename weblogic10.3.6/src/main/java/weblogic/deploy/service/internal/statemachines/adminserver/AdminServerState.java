package weblogic.deploy.service.internal.statemachines.adminserver;

import weblogic.deploy.service.internal.DeploymentServiceLogger;
import weblogic.deploy.service.internal.InvalidStateException;
import weblogic.deploy.service.internal.adminserver.AdminRequestImpl;
import weblogic.deploy.service.internal.statemachines.State;

public abstract class AdminServerState extends State {
   protected long requestId;
   protected AdminRequestImpl request = null;

   public AdminServerState start() throws InvalidStateException {
      throw new InvalidStateException(DeploymentServiceLogger.logInvalidStateLoggable().getMessage());
   }

   public AdminServerState cancel() throws InvalidStateException {
      this.fireStateTransitionEvent(this, "requestCancelled", this.requestId);
      return this.getCurrentState();
   }

   public AdminServerState requestTimedOut() {
      this.fireStateTransitionEvent(this, "requestTimedout", this.requestId);
      return this.getCurrentState();
   }

   public AdminServerState receivedPrepareSucceeded(String var1, boolean var2) {
      this.fireStateTransitionEvent(this, "receivedPrepareSuceeded", this.requestId);
      if (this.isDebugEnabled()) {
         this.debug("deployment '" + this.requestId + "' is in ' " + this.getCurrentState() + "' - receivedPrepareSucceeded is an invalid +" + "transition event");
      }

      return this.getCurrentState();
   }

   public AdminServerState receivedPrepareFailed(String var1, Throwable var2, boolean var3) {
      this.fireStateTransitionEvent(this, "receivedPrepareFailed", this.requestId);
      if (this.isDebugEnabled()) {
         this.debug("deployment '" + this.requestId + "' is in ' " + this.getCurrentState() + "' - receivedPrepareFailed is an invalid " + "transition event");
      }

      return this.getCurrentState();
   }

   public AdminServerState receivedCommitSucceeded(String var1) {
      this.fireStateTransitionEvent(this, "receivedCommitSucceeded", this.requestId);
      if (this.isDebugEnabled()) {
         this.debug("deployment '" + this.requestId + "' is in ' " + this.getCurrentState() + "' - receivedCommitSucceeded is an invalid +" + "transition event");
      }

      return this.getCurrentState();
   }

   public AdminServerState receivedCommitFailed(String var1, Throwable var2) {
      this.fireStateTransitionEvent(this, "receivedCommitFailed", this.requestId);
      if (this.isDebugEnabled()) {
         this.debug("deployment '" + this.requestId + "' is in ' " + this.getCurrentState() + "' - receivedCommitFailed is an invalid +" + "transition event");
      }

      return this.getCurrentState();
   }

   public AdminServerState receivedCancelSucceeded(String var1) {
      this.fireStateTransitionEvent(this, "receivedCancelSucceeded", this.requestId);
      if (this.isDebugEnabled()) {
         this.debug("deployment '" + this.requestId + "' is in ' " + this.getCurrentState() + "' - receivedCancelSucceeded is an invalid +" + "transition event");
      }

      return this.getCurrentState();
   }

   public AdminServerState receivedCancelFailed(String var1, Throwable var2) {
      this.fireStateTransitionEvent(this, "receivedCancelFailed", this.requestId);
      if (this.isDebugEnabled()) {
         this.debug("deployment '" + this.requestId + "' is in ' " + this.getCurrentState() + "' - receivedCancelfailed is an invalid +" + "transition event");
      }

      return this.getCurrentState();
   }

   public AdminServerState allPreparesDelivered() {
      this.fireStateTransitionEvent(this, "preparesDelivered", this.requestId);
      if (this.isDebugEnabled()) {
         this.debug("deployment '" + this.requestId + "' is in ' " + this.getCurrentState() + "' - allPreparesDelivered is an invalid transition event");
      }

      return this.getCurrentState();
   }

   public AdminServerState allCommitsDelivered() {
      this.fireStateTransitionEvent(this, "commitsDelivered", this.requestId);
      if (this.isDebugEnabled()) {
         this.debug("deployment '" + this.requestId + "' is in ' " + this.getCurrentState() + "' - allCommitsDelivered is an invalid transition event");
      }

      return this.getCurrentState();
   }

   public AdminServerState allCancelsDelivered() {
      this.fireStateTransitionEvent(this, "cancelsDelivered", this.requestId);
      if (this.isDebugEnabled()) {
         this.debug("deployment '" + this.requestId + "' is in ' " + this.getCurrentState() + "' - allCancelsDelivered is an invalid transition event");
      }

      return this.getCurrentState();
   }

   public final void initialize(AdminRequestImpl var1) {
      this.request = var1;
      this.requestId = var1.getId();
   }

   protected void doPrepareCompletionCheck() {
   }

   protected boolean doCommitCompletionCheck() {
      return false;
   }

   protected boolean doCancelCompletionCheck() {
      return false;
   }

   protected final AdminServerState getCurrentState() {
      return this.request == null ? null : this.request.getCurrentState();
   }

   protected final AdminRequestImpl getDeploymentRequest() {
      return this.request;
   }

   protected final void setExpectedNextState(int var1) {
      AdminServerState var2 = this.request.getStatus().getAdminServerState(var1);
      if (this.isDebugEnabled()) {
         this.debug("Setting admin state of deployment '" + this.requestId + "' to : " + this.getAdminStateString(var1));
      }

      this.request.getStatus().setCurrentState(var2);
   }

   public void reset() {
      this.request = null;
   }

   protected final void fireStateTransitionEvent(State var1, String var2, long var3) {
      if (this.isDebugEnabled()) {
         this.debug("Admin DeploymentService event : '" + var1.toString() + "." + var2 + "()' for deployment id '" + var3 + "'");
      }

   }

   protected final String getAdminStateString(int var1) {
      String var2 = null;
      switch (var1) {
         case 0:
            var2 = "SendingPrepare";
            break;
         case 1:
            var2 = "AwaitingPrepareResponses";
            break;
         case 2:
            var2 = "SendingCommit";
            break;
         case 3:
            var2 = "AwaitingCommitResponses";
            break;
         case 4:
            var2 = "SendingCancel";
            break;
         case 5:
            var2 = "AwaitingCancelResponses";
      }

      return var2;
   }
}
