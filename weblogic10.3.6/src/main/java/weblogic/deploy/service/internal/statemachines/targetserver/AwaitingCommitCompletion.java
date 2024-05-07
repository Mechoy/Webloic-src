package weblogic.deploy.service.internal.statemachines.targetserver;

import java.rmi.RemoteException;
import weblogic.deploy.common.Debug;
import weblogic.deploy.service.internal.DeploymentServiceLogger;
import weblogic.deploy.service.internal.DomainVersion;
import weblogic.deploy.service.internal.targetserver.TargetRequestImpl;

public final class AwaitingCommitCompletion extends TargetServerState {
   public final TargetServerState receivedCancel() {
      this.fireStateTransitionEvent(this, "receivedCancel", this.getId());
      String var1 = DeploymentServiceLogger.cancelRejected(this.getId());
      this.sendCancelFailed(new Exception(var1));
      return this.getCurrentState();
   }

   public TargetServerState abort() {
      this.fireStateTransitionEvent(this, "abort", this.getId());
      if (this.isDebugEnabled()) {
         this.debug("already waiting for commit completion for id: " + this.getId() + " nothing to be done");
      }

      return this.getCurrentState();
   }

   private final TargetRequestImpl getDeploymentRequest(long var1) {
      return this.requestsManager.getRequest(var1);
   }

   public final TargetServerState commitSucceeded() {
      long var1 = this.getId();
      this.fireStateTransitionEvent(this, "commitSucceeded", var1);
      TargetRequestImpl var3 = this.getDeploymentRequest(var1);
      if (var3 == null) {
         if (this.isDebugEnabled()) {
            this.debug("'commit succeeded' received on target for id: " + var1 + " that has no request - it may be completed " + "or cancelled");
         }

         return null;
      } else {
         DomainVersion var4 = var3.getSyncToAdminVersion();
         if (var4 != null) {
            if (this.isDebugEnabled()) {
               this.debug("'commit success' of sync to admin version " + var4);
            }

            this.deploymentsManager.setCurrentDomainVersion(var4);
            this.deploymentStatus.reset();
         } else {
            if (this.isDebugEnabled()) {
               this.debug("'commit success' on target of id " + var1);
            }

            boolean var5 = !var3.isControlRequest();
            if (this.isDebugEnabled()) {
               this.debug("Needs VersionUpdate : " + var5);
            }

            if (var5) {
               this.deploymentsManager.setCurrentDomainVersion(var3.getProposedDomainVersion());
            }

            boolean var6 = this.deploymentStatus.isCanceled();
            this.sendCommitSucceeded();
            if (var6) {
               String var7 = DeploymentServiceLogger.cancelRejected(this.getId());
               this.sender.sendCancelFailedMsg(var1, new Exception(var7));
            }
         }

         return null;
      }
   }

   public final TargetServerState commitFailed() {
      long var1 = this.getId();
      this.fireStateTransitionEvent(this, "commitFailed", var1);
      TargetRequestImpl var3 = this.getDeploymentRequest();
      if (var3 == null) {
         String var12 = DeploymentServiceLogger.commitFailed(var1);
         if (this.isDebugEnabled()) {
            this.debug(var12);
         }

         try {
            this.sender.sendCommitFailedMsg(this.getId(), new Exception(var12));
         } catch (RemoteException var9) {
         }

         return null;
      } else {
         DomainVersion var4 = var3.getSyncToAdminVersion();
         if (var4 != null) {
            if (Debug.serviceLogger.isDebugEnabled()) {
               Debug.serviceLogger.debug("'commit failed' in sync to admin version " + var4 + " - transition server to admin state");
            }

            this.transitionServerToAdminState();
         } else {
            boolean var5 = this.deploymentStatus.isCanceled();

            try {
               this.sendCommitFailed(this.deploymentStatus.getCommitFailureError());
            } catch (RemoteException var11) {
               String var7 = DeploymentServiceLogger.sendCommitFailMsgFailed(var1);

               try {
                  this.sender.sendCommitFailedMsg(this.getId(), new Exception(var7));
               } catch (RemoteException var10) {
               }
            }

            if (var5) {
               this.sender.sendCancelFailedMsg(var1, new Exception("'commit' already called on deployment request with id '" + var1 + "'"));
            }
         }

         return null;
      }
   }

   public final String toString() {
      return "AwaitingCommitCompletion";
   }
}
