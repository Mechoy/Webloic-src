package weblogic.deploy.service.internal.statemachines.targetserver;

import java.rmi.RemoteException;
import weblogic.deploy.common.Debug;
import weblogic.deploy.service.internal.targetserver.TargetRequestImpl;

public final class AwaitingPrepareCompletion extends TargetServerState {
   public final synchronized TargetServerState prepareSucceeded() {
      this.fireStateTransitionEvent(this, "prepareSucceeded", this.getId());
      if (this.cancelIfNecessary()) {
         return null;
      } else {
         this.setExpectedNextState(3);
         TargetRequestImpl var1 = this.getDeploymentRequest();
         RemoteException var2;
         if (var1 == null) {
            if (this.isDebugEnabled()) {
               this.debug("'prepare succeeded' received for id: " + this.getId() + " that has no request - it may " + "completed or been cancelled ");
            }

            if (this.getId() != -1L) {
               try {
                  this.sender.sendPrepareAckMsg(this.getId(), false);
               } catch (RemoteException var5) {
                  var2 = var5;

                  try {
                     this.sender.sendPrepareNakMsg(this.getId(), var2);
                  } catch (Exception var4) {
                  }
               }
            }

            return null;
         } else {
            if (var1.getSyncToAdminDeployments() != null) {
               this.deploymentStatus.getCurrentState().receivedCommit();
            } else {
               try {
                  this.sender.sendPrepareAckMsg(this.getId(), this.deploymentStatus.isRestartPending());
               } catch (RemoteException var7) {
                  var2 = var7;

                  try {
                     this.sender.sendPrepareNakMsg(this.getId(), var2);
                  } catch (Exception var6) {
                  }

                  this.deploymentStatus.setAborted();
                  this.cancelIfNecessary();
               }
            }

            return this.getCurrentState();
         }
      }
   }

   public final synchronized TargetServerState prepareFailed() {
      this.fireStateTransitionEvent(this, "prepareFailed", this.getId());
      if (this.cancelIfNecessary()) {
         return null;
      } else {
         TargetRequestImpl var1 = this.getDeploymentRequest();
         if (var1.getSyncToAdminVersion() != null) {
            if (Debug.serviceLogger.isDebugEnabled()) {
               Debug.serviceLogger.debug("'prepare' failed in sync to admin version " + var1.getSyncToAdminVersion() + " - transitioning server to admin state");
            }

            this.transitionServerToAdminState();
            return null;
         } else {
            this.setExpectedNextState(6);
            this.sendPrepareNak(this.deploymentStatus.getSavedError());
            return this.getCurrentState();
         }
      }
   }

   public final String toString() {
      return "AwaitingPrepareCompletion";
   }
}
