package weblogic.deploy.service.internal.targetserver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import weblogic.deploy.common.Debug;
import weblogic.deploy.service.DeploymentReceiver;
import weblogic.deploy.service.DeploymentReceiverV2;
import weblogic.deploy.service.internal.statemachines.StateMachinesManager;
import weblogic.deploy.service.internal.statemachines.targetserver.TargetServerState;

public final class TargetRequestStatus {
   private static final TargetDeploymentsManager deploymentsManager = TargetDeploymentsManager.getInstance();
   private static final TargetRequestManager requestManager = TargetRequestManager.getInstance();
   private TargetRequestImpl request;
   private long id = 0L;
   private TargetServerState currentState;
   private ArrayList targetServerStateList;
   private HashSet deploymentsToUpdateContext = new HashSet();
   private HashSet deploymentsToAckPrepare = new HashSet();
   private HashSet deploymentsToAckCommit = new HashSet();
   private HashSet deploymentsToAckCancel = new HashSet();
   private HashSet deploymentsToNotifySkip = new HashSet();
   private boolean isCanceled;
   private boolean isAborted;
   private boolean isTimedOut;
   private boolean cancelDispatched;
   private boolean serverStarting;
   private boolean restartPending;
   private Throwable savedException = null;
   Throwable commitFailureError = null;
   private Throwable cancelFailureError = null;

   private TargetRequestStatus(TargetRequestImpl var1) {
      this.request = var1;
      this.id = var1.getId();
   }

   private final void debug(String var1) {
      Debug.serviceDebug(var1);
   }

   private final boolean isDebugEnabled() {
      return Debug.isServiceDebugEnabled();
   }

   public static TargetRequestStatus createTargetRequestStatus(TargetRequestImpl var0) {
      TargetRequestStatus var1 = new TargetRequestStatus(var0);

      try {
         var1.setTargetServerStates(StateMachinesManager.createTargetServerStates(var1));
      } catch (ClassNotFoundException var3) {
      } catch (IllegalAccessException var4) {
      } catch (InstantiationException var5) {
      }

      var1.setCurrentState(var1.getTargetServerState(0));
      return var1;
   }

   private void setTargetServerStates(ArrayList var1) {
      this.targetServerStateList = var1;
   }

   public final long getId() {
      return this.id;
   }

   public final void setServerStarting() {
      this.serverStarting = true;
   }

   public final boolean isServerStarting() {
      return this.serverStarting;
   }

   public final synchronized void setCurrentState(TargetServerState var1) {
      this.currentState = var1;
   }

   public final synchronized TargetServerState getCurrentState() {
      return this.currentState;
   }

   public final synchronized TargetRequestImpl getDeploymentRequest() {
      return this.request;
   }

   public final TargetServerState getTargetServerState(int var1) {
      TargetServerState var2 = (TargetServerState)this.targetServerStateList.get(var1);
      return var2;
   }

   public final boolean isTimedOut() {
      return this.isTimedOut;
   }

   public final void setTimedOut() {
      this.isTimedOut = true;
   }

   public final synchronized void addToDeploymentsAckList(String var1) {
      if (deploymentsManager.getDeploymentReceiver(var1) == null) {
         if (this.isDebugEnabled()) {
            this.debug("TargetRequestStatus: '" + var1 + "' not added to ack list - no DeploymentReceiver registered");
         }

      } else {
         this.deploymentsToUpdateContext.add(var1);
         this.deploymentsToAckPrepare.add(var1);
         this.deploymentsToAckCancel.add(var1);
         this.deploymentsToAckCommit.add(var1);
         this.deploymentsToNotifySkip.add(var1);
         if (this.isDebugEnabled()) {
            this.debug("'" + var1 + "' added to DeploymentReceiver " + "ack for '" + this.id + "'");
         }

      }
   }

   public final synchronized void receivedContextUpdateCompletedFrom(String var1) {
      this.receivedContextUpdateCompletedFrom(var1, (Throwable)null);
   }

   public final synchronized void receivedContextUpdateCompletedFrom(String var1, Throwable var2) {
      this.deploymentsToUpdateContext.remove(var1);
      if (var2 != null) {
         this.savedException = var2;
      }

      TimeAuditorManager.getInstance().endDeploymentTransition(this.request.getId(), var1, 0);
   }

   public final synchronized boolean receivedAllContextUpdates() {
      boolean var1 = this.deploymentsToUpdateContext == null || this.deploymentsToUpdateContext.size() == 0;
      if (var1) {
         TimeAuditorManager.getInstance().endTransition(this.request.getId(), 0);
      }

      return var1;
   }

   public final synchronized void receivedPrepareAckFrom(String var1) {
      this.deploymentsToAckPrepare.remove(var1);
      Iterator var2 = this.deploymentsToAckPrepare.iterator();

      while(var2.hasNext()) {
         DeploymentReceiver var3 = deploymentsManager.getDeploymentReceiver((String)var2.next());
         var3.prepareCompleted(this.request.getDeploymentContext(), var1);
      }

      TimeAuditorManager.getInstance().endDeploymentTransition(this.request.getId(), var1, 1);
   }

   public final synchronized void receivedPrepareNakFrom(String var1, Throwable var2) {
      this.deploymentsToAckPrepare.remove(var1);
      this.deploymentsToAckCommit.remove(var1);
      if (this.savedException == null) {
         this.savedException = var2;
      }

      TimeAuditorManager.getInstance().endDeploymentTransition(this.request.getId(), var1, 1);
   }

   public final synchronized Throwable getSavedError() {
      return this.savedException;
   }

   public final synchronized boolean receivedAllPrepareCompletions() {
      boolean var1 = this.deploymentsToAckPrepare == null || this.deploymentsToAckPrepare.size() == 0;
      if (var1) {
         TimeAuditorManager.getInstance().endTransition(this.request.getId(), 1);
      }

      return var1;
   }

   public final synchronized void receivedCommitAckFrom(String var1) {
      if (!this.isCanceled()) {
         this.deploymentsToAckCommit.remove(var1);
         Iterator var2 = this.deploymentsToAckCommit.iterator();

         while(var2.hasNext()) {
            DeploymentReceiver var3 = deploymentsManager.getDeploymentReceiver((String)var2.next());
            var3.commitCompleted(this.request.getDeploymentContext(), var1);
         }

         TimeAuditorManager.getInstance().endDeploymentTransition(this.request.getId(), var1, 2);
      }
   }

   public final synchronized void receivedCommitFailureFrom(String var1, Throwable var2) {
      this.deploymentsToAckCommit.remove(var1);
      if (this.commitFailureError == null) {
         this.commitFailureError = var2;
      }

      TimeAuditorManager.getInstance().endDeploymentTransition(this.request.getId(), var1, 2);
   }

   public final synchronized Throwable getCommitFailureError() {
      return this.commitFailureError;
   }

   public final synchronized boolean receivedAllCommitResponses() {
      boolean var1 = this.deploymentsToAckCommit == null || this.deploymentsToAckCommit.size() == 0;
      if (var1) {
         TimeAuditorManager.getInstance().endTransition(this.request.getId(), 2);
      }

      return var1;
   }

   public final synchronized boolean isCanceled() {
      return this.isCanceled;
   }

   public final synchronized void setCanceled() {
      if (!this.isCanceled && this.request != null) {
         this.isCanceled = true;
         if (this.isDebugEnabled()) {
            this.debug("request '" + this.request.getId() + "' set to 'canceled' on target");
         }

      }
   }

   public final synchronized boolean isAborted() {
      return this.isAborted;
   }

   public final synchronized void setAborted() {
      if (!this.isAborted && this.request != null) {
         if (this.isDebugEnabled()) {
            this.debug("request '" + this.request.getId() + "' set to 'aborted' on target");
         }

         this.isAborted = true;
      }
   }

   public final synchronized boolean isCanceledOrAborted() {
      return this.isCanceled || this.isAborted;
   }

   public final synchronized boolean isCancelDispatched() {
      return this.cancelDispatched;
   }

   public final synchronized void setCancelDispatched() {
      this.cancelDispatched = true;
   }

   public final synchronized void cancelSuccessFrom(String var1) {
      this.deploymentsToAckCancel.remove(var1);
      TimeAuditorManager.getInstance().endDeploymentTransition(this.request.getId(), var1, 3);
   }

   public final synchronized void cancelFailureFrom(String var1, Throwable var2) {
      this.deploymentsToAckCancel.remove(var1);
      if (this.cancelFailureError == null) {
         this.cancelFailureError = var2;
      }

      TimeAuditorManager.getInstance().endDeploymentTransition(this.request.getId(), var1, 3);
   }

   public final Throwable getCancelFailureError() {
      return this.cancelFailureError;
   }

   public final synchronized Iterator getDeploymentsToBeCancelled() {
      if (this.deploymentsToAckCancel != null) {
         HashSet var1 = (HashSet)this.deploymentsToAckCancel.clone();
         return var1.iterator();
      } else {
         return null;
      }
   }

   public final synchronized boolean receivedAllCancelResponses() {
      boolean var1 = this.deploymentsToAckCancel.isEmpty();
      if (var1) {
         TimeAuditorManager.getInstance().endTransition(this.request.getId(), 2);
      }

      return var1;
   }

   public final synchronized void scheduleNextRequest() {
      if (this.request != null && this.request.getSyncToAdminDeployments() == null) {
         requestManager.scheduleNextRequest();
      }

   }

   public final void setRestartPending() {
      this.restartPending = true;
   }

   public final boolean isRestartPending() {
      return this.restartPending;
   }

   public final synchronized void commitSkipped() {
      Iterator var1 = this.deploymentsToNotifySkip.iterator();

      while(var1.hasNext()) {
         DeploymentReceiver var2 = deploymentsManager.getDeploymentReceiver((String)var1.next());
         if (var2 instanceof DeploymentReceiverV2) {
            ((DeploymentReceiverV2)var2).commitSkipped(this.request.getDeploymentContext());
         }
      }

   }

   public final synchronized void reset() {
      if (this.request != null) {
         long var1 = this.request.getId();
         if (this.isDebugEnabled()) {
            this.debug("resetting id '" + var1 + "' heartbeat?:" + this.request.isHeartbeatRequest() + " on target");
         }

         TimeAuditorManager.getInstance().printAuditor(var1, System.out);
         TimeAuditorManager.getInstance().endAuditor(var1);
         if (this.request.getSyncToAdminDeployments() != null) {
            this.request.resetSyncToAdminDeployments();
            if (!this.request.isHeartbeatRequest()) {
               if (this.isDebugEnabled()) {
                  this.debug("handling pending request '" + this.request.getId() + " on target");
               }

               TargetServerState var3 = this.getTargetServerState(0);
               this.setCurrentState(var3);
               this.getCurrentState().receivedPrepare();
               return;
            }
         }

         this.request.cancelTimeoutMonitor();
         this.deploymentsToUpdateContext.clear();
         this.deploymentsToUpdateContext = null;
         this.deploymentsToAckPrepare.clear();
         this.deploymentsToAckPrepare = null;
         this.deploymentsToAckCommit.clear();
         this.deploymentsToAckCommit = null;
         this.deploymentsToAckCancel.clear();
         this.deploymentsToAckCancel = null;
         this.deploymentsToNotifySkip.clear();
         this.deploymentsToNotifySkip = null;
         this.savedException = null;
         this.cancelFailureError = null;
         this.targetServerStateList.clear();
         this.targetServerStateList = null;
         requestManager.removeRequest(this.id);
         this.request = null;
         requestManager.scheduleNextRequest();
      }
   }
}
