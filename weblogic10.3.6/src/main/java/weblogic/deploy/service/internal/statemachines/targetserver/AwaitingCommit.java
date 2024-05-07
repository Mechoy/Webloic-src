package weblogic.deploy.service.internal.statemachines.targetserver;

import java.util.Iterator;
import java.util.Map;
import weblogic.deploy.service.DeploymentReceiver;
import weblogic.deploy.service.internal.DeploymentServiceLogger;
import weblogic.deploy.service.internal.DomainVersion;
import weblogic.deploy.service.internal.targetserver.DeploymentContextImpl;
import weblogic.deploy.service.internal.targetserver.TargetRequestImpl;
import weblogic.deploy.service.internal.targetserver.TimeAuditorManager;

public final class AwaitingCommit extends TargetServerState {
   public final synchronized TargetServerState receivedCancel() {
      this.fireStateTransitionEvent(this, "receivedCancel", this.getId());
      this.cancelIfNecessary();
      return this.getCurrentState();
   }

   public final synchronized TargetServerState receivedCommit() {
      this.fireStateTransitionEvent(this, "receivedCommit", this.getId());

      try {
         this.handleCommit();
      } catch (Throwable var4) {
         Throwable var1 = var4;

         try {
            this.sendCommitFailed(new Exception(var1.getMessage()));
         } catch (Exception var3) {
         }
      }

      return this.getCurrentState();
   }

   private final void handleRestartIsPending() {
      DeploymentServiceLogger.logCommitPendingRestart(this.getId());
      TargetRequestImpl var1 = this.getDeploymentRequest();
      boolean var2 = !var1.isControlRequest();
      if (this.isDebugEnabled()) {
         this.debug("Needs VersionUpdate : " + var2);
      }

      if (var2) {
         this.deploymentsManager.setCurrentDomainVersion(var1.getProposedDomainVersion());
      }

      if (this.isDebugEnabled()) {
         this.debug("Treating request id " + var1.getId() + " on this target as 'Commit Success'");
      }

      this.deploymentStatus.commitSkipped();
      this.sendCommitSucceeded();
      if (this.deploymentStatus != null) {
         this.deploymentStatus.reset();
      }

   }

   private final void handleCommit() throws Exception {
      if (this.deploymentStatus != null && this.deploymentStatus.isRestartPending()) {
         this.handleRestartIsPending();
      } else if (this.deploymentStatus == null || !this.deploymentStatus.isCanceledOrAborted()) {
         TargetRequestImpl var1 = this.getDeploymentRequest();
         this.doPreCommitValidation(var1);
         if (!this.handleOptimisticConcurrencyViolationIfNecessary(var1)) {
            DomainVersion var2 = var1.getSyncToAdminVersion();
            if (var2 != null) {
               this.deploymentsManager.setCurrentDomainVersion(var2);
            } else {
               boolean var3 = !var1.isControlRequest();
               if (this.isDebugEnabled()) {
                  this.debug("Needs VersionUpdate : " + var3);
               }

               if (var3) {
                  this.deploymentsManager.setCurrentDomainVersion(var1.getProposedDomainVersion());
               }
            }

            this.setExpectedNextState(4);
            if (this.isDebugEnabled()) {
               this.debug("'commit' for id '" + var1 + "' from version '" + var1.getPreparingFromVersion() + "' to version '" + var1.getProposedDomainVersion());
            }

            this.callDeploymentReceivers();
            this.deploymentStatus.scheduleNextRequest();
         }
      }
   }

   private boolean handleOptimisticConcurrencyViolationIfNecessary(TargetRequestImpl var1) {
      boolean var2 = false;
      if (var1.getSyncToAdminDeployments() != null) {
         return var2;
      } else {
         DomainVersion var3 = this.deploymentsManager.getCurrentDomainVersion();
         if (!var3.equals(var1.getPreparingFromVersion())) {
            if (this.isDebugEnabled()) {
               this.debug("request '" + var1.getId() + "' was 'prepare'-d " + "against version '" + var1.getPreparingFromVersion() + "' but is being 'commit'-ted against version '" + var3 + "' - request will be aborted locally and an " + "attempt will be made to resynchronize with the admin after which " + "the request will be retried");
            }

            var2 = true;
            this.deploymentStatus.setAborted();
            this.cancelIfNecessary();
         }

         return var2;
      }
   }

   private final void doPreCommitValidation(TargetRequestImpl var1) throws Exception {
      if (var1 == null) {
         String var2 = DeploymentServiceLogger.commitNoRequest();
         if (this.isDebugEnabled()) {
            this.debug(var2);
         }

         throw new Exception(var2);
      }
   }

   protected final void callDeploymentReceivers() throws Exception {
      TargetRequestImpl var1 = this.getDeploymentRequest();
      TimeAuditorManager.getInstance().startTransition(var1.getId(), 2);
      Map var2 = var1.getDeploymentsMap();
      DeploymentReceiver var3 = null;
      DeploymentContextImpl var4 = var1.getDeploymentContext();
      Iterator var5 = var2.keySet().iterator();

      while(true) {
         while(true) {
            String var6;
            DeploymentReceiver var7;
            do {
               if (!var5.hasNext()) {
                  if (var3 != null) {
                     if (this.isDebugEnabled()) {
                        this.debug("calling DeploymentReceiver 'Configuration' to 'commit' for id '" + this.getId());
                     }

                     TimeAuditorManager.getInstance().startDeploymentTransition(var1.getId(), var3.getHandlerIdentity(), 2);
                     var3.commit(var4);
                  }

                  return;
               }

               var6 = (String)var5.next();
               var7 = this.deploymentsManager.getDeploymentReceiver(var6);
            } while(var7 == null);

            if (var1.isConfigurationProviderCalledLast() && var3 == null && var6.equals("Configuration")) {
               var3 = var7;
            } else {
               if (this.isDebugEnabled()) {
                  this.debug("calling DeploymentReceiver '" + var6 + "' to 'commit' for id '" + this.getId());
               }

               TimeAuditorManager.getInstance().startDeploymentTransition(var1.getId(), var6, 2);
               var7.commit(var4);
            }
         }
      }
   }

   public final String toString() {
      return "AwaitingCommit";
   }
}
