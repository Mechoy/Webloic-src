package weblogic.deploy.service.internal.statemachines.targetserver;

import java.util.Iterator;
import java.util.Map;
import weblogic.deploy.common.Debug;
import weblogic.deploy.service.DeploymentReceiver;
import weblogic.deploy.service.internal.targetserver.TargetRequestImpl;
import weblogic.deploy.service.internal.targetserver.TimeAuditorManager;
import weblogic.utils.StackTraceUtils;

public class AwaitingContextUpdateCompletion extends TargetServerState {
   public final synchronized TargetServerState contextUpdated() {
      this.fireStateTransitionEvent(this, "contextUpdated", this.getId());
      this.handleContextUpdateSuccess();
      return this.getCurrentState();
   }

   public final synchronized TargetServerState contextUpdateFailed(Throwable var1) {
      this.fireStateTransitionEvent(this, "contextUpdated", this.getId());
      this.handleContextUpdateFailure(var1);
      return this.getCurrentState();
   }

   private void handleContextUpdateSuccess() {
      if (!this.cancelIfNecessary()) {
         TargetRequestImpl var1 = this.getDeploymentRequest();
         if (var1.getDeploymentContext().isRestartRequired()) {
            this.deploymentStatus.setRestartPending();
         }

         this.setExpectedNextState(2);

         try {
            this.callDeploymentReceivers(var1);
         } catch (Exception var3) {
            if (this.isDebugEnabled()) {
               this.debug("AwaitingContextUpdateCompletion:handleContextUpdateSuccess: encountered an error " + StackTraceUtils.throwable2StackTrace(var3));
            }

            if (var1.getSyncToAdminDeployments() != null) {
               if (Debug.serviceLogger.isDebugEnabled()) {
                  Debug.serviceLogger.debug("'context update' sync to admin version " + var1.getSyncToAdminVersion() + " - handling failed - transitioning server to admin state");
               }

               this.transitionServerToAdminState();
               return;
            }

            if (this.cancelIfNecessary()) {
               return;
            }

            this.setExpectedNextState(6);
            this.sendPrepareNak(new Exception(var3.getMessage()));
         }

      }
   }

   private void handleContextUpdateFailure(Throwable var1) {
      if (!this.cancelIfNecessary()) {
         TargetRequestImpl var2 = this.getDeploymentRequest();
         if (var2.getSyncToAdminVersion() != null) {
            if (Debug.serviceLogger.isDebugEnabled()) {
               Debug.serviceLogger.debug("'context update' failed in sync to admin version " + var2.getSyncToAdminVersion() + " - transitioning server to admin state");
            }

            this.transitionServerToAdminState();
         } else {
            this.setExpectedNextState(6);
            this.sendPrepareNak(var1);
         }
      }
   }

   protected final void callDeploymentReceivers(TargetRequestImpl var1) throws Exception {
      TimeAuditorManager.getInstance().startTransition(var1.getId(), 1);
      DeploymentReceiver var2 = null;
      Map var3 = var1.getDeploymentsMap();
      Iterator var4 = var3.keySet().iterator();

      while(true) {
         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            DeploymentReceiver var6 = this.deploymentsManager.getDeploymentReceiver(var5);
            if (var1.isConfigurationProviderCalledLast() && var2 == null && var5.equals("Configuration")) {
               var2 = var6;
               if (this.isDebugEnabled()) {
                  this.debug("'call config last' set for id: " + var1.getId());
               }
            } else {
               TimeAuditorManager.getInstance().startDeploymentTransition(var1.getId(), var5, 1);
               var6.prepare(var1.getDeploymentContext());
            }
         }

         if (var2 != null) {
            TimeAuditorManager.getInstance().startDeploymentTransition(var1.getId(), var2.getHandlerIdentity(), 1);
            var2.prepare(var1.getDeploymentContext());
         }

         return;
      }
   }

   public final String toString() {
      return "AwaitingContextUpdateCompletion";
   }
}
