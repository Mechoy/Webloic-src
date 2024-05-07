package weblogic.deploy.service.internal.statemachines.targetserver;

import java.security.AccessController;
import java.util.Iterator;
import java.util.Map;
import weblogic.deploy.common.Debug;
import weblogic.deploy.service.DeploymentReceiver;
import weblogic.deploy.service.internal.DomainVersion;
import weblogic.deploy.service.internal.targetserver.DeploymentContextImpl;
import weblogic.deploy.service.internal.targetserver.TargetRequestImpl;
import weblogic.deploy.service.internal.targetserver.TimeAuditorManager;
import weblogic.management.deploy.internal.DeployerRuntimeLogger;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.StackTraceUtils;

public final class ReceivedPrepare extends TargetServerState {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public final TargetServerState receivedPrepare() {
      this.fireStateTransitionEvent(this, "receivedPrepare", this.getId());
      TargetRequestImpl var1 = this.getDeploymentRequest();

      try {
         this.handlePrepare(var1);
      } catch (Throwable var3) {
         if (Debug.serviceLogger.isDebugEnabled()) {
            Debug.serviceLogger.debug(var3.getMessage());
         }

         if (this.isDebugEnabled()) {
            this.debug(StackTraceUtils.throwable2StackTrace(var3));
         }

         this.setExpectedNextState(6);
         this.sendPrepareNak(new Exception(var3.getMessage()));
      }

      return this.getCurrentState();
   }

   protected final void handlePrepare(TargetRequestImpl var1) throws Exception {
      TimeAuditorManager.getInstance().startAuditor(var1.getId());
      TimeAuditorManager.getInstance().startTransition(var1.getId(), 0);
      if (!this.handlePendingCancel(var1)) {
         DomainVersion var2 = this.deploymentsManager.getCurrentDomainVersion();
         if (!this.syncWithAdminIfNecessary(var2, var1)) {
            this.setExpectedNextState(1);
            DomainVersion var3 = var2.getCopy();
            this.setupDeploymentRequest(var2, var3, var1, var1.getDeployments());
            this.setupDeploymentContext();
            if (this.isDebugEnabled()) {
               this.debug("'Receivedprepare' for id '" + var1 + "' from version '" + var1.getPreparingFromVersion() + "' to version '" + var1.getProposedDomainVersion());
            }

            this.callDeploymentReceivers();
         }
      }
   }

   private final boolean handlePendingCancel(TargetRequestImpl var1) {
      long var2 = var1.getId();
      if (this.requestsManager.hasAPendingCancelFor(var2)) {
         if (this.isDebugEnabled()) {
            this.debug("ReceivedPrepare: handling pending cancel for request with id '" + var2 + "'");
         }

         this.requestsManager.removePendingCancelFor(var2);
         this.sendCancelSucceeded();
         return true;
      } else {
         return false;
      }
   }

   private final boolean syncWithAdminIfNecessary(DomainVersion var1, TargetRequestImpl var2) {
      if (ManagementService.getPropertyService(kernelId).isAdminServer()) {
         return false;
      } else {
         boolean var3 = false;
         if (!var1.equals(var2.getDomainVersion())) {
            var3 = true;
            this.syncWithAdminServer(var1);
         }

         return var3;
      }
   }

   protected final void doPrePrepareValidation(TargetRequestImpl var1) throws Exception {
      if (var1 == null) {
         String var2 = DeployerRuntimeLogger.invalidPrepare(this.getId());
         if (this.isDebugEnabled()) {
            this.debug(var2);
         }

         throw new Exception(var2);
      }
   }

   protected final void callDeploymentReceivers() throws Exception {
      TargetRequestImpl var1 = this.getDeploymentRequest();
      Map var2 = var1.getDeploymentsMap();
      DeploymentContextImpl var3 = var1.getDeploymentContext();
      Iterator var4 = var2.keySet().iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         DeploymentReceiver var6 = this.deploymentsManager.getDeploymentReceiver(var5);
         TimeAuditorManager.getInstance().startDeploymentTransition(var1.getId(), var5, 0);
         var6.updateDeploymentContext(var3);
      }

   }

   public final String toString() {
      return "ReceivedPrepare";
   }
}
