package weblogic.deploy.service.internal.statemachines.targetserver;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import weblogic.deploy.common.Debug;
import weblogic.deploy.service.DeploymentReceiver;
import weblogic.deploy.service.internal.DomainVersion;
import weblogic.deploy.service.internal.targetserver.DeploymentContextImpl;
import weblogic.deploy.service.internal.targetserver.TargetRequestImpl;
import weblogic.deploy.service.internal.transport.DeploymentServiceMessage;
import weblogic.management.deploy.internal.DeployerRuntimeLogger;
import weblogic.utils.StackTraceUtils;

public final class AwaitingGetDeploymentsResponse extends TargetServerState {
   public synchronized TargetServerState receivedCancel() {
      this.fireStateTransitionEvent(this, "receivedCancel", this.getId());
      return this.getCurrentState();
   }

   public final synchronized TargetServerState receivedGetDeploymentsResponse(DeploymentServiceMessage var1) {
      this.fireStateTransitionEvent(this, "receivedGetDeltasResponse", 0L);
      TargetRequestImpl var2 = this.getDeploymentRequest();

      try {
         this.handleGetDeploymentsResponse(var2, var1);
      } catch (Throwable var4) {
         if (Debug.serviceLogger.isDebugEnabled()) {
            Debug.serviceLogger.debug("'" + StackTraceUtils.throwable2StackTrace(var4) + "' failure when handling a 'get deployments response' - this is an " + "unexpected failure & probably requires a server reboot to recover");
         }

         this.transitionServerToAdminState();
         return null;
      }

      return this.getCurrentState();
   }

   private final void handleGetDeploymentsResponse(TargetRequestImpl var1, DeploymentServiceMessage var2) throws Exception {
      this.doPreHandleResponseValidation(var1, var2);
      DomainVersion var3 = this.deploymentsManager.getCurrentDomainVersion();
      var1.setSyncToAdminMessage(var2);
      DomainVersion var4 = var1.getSyncToAdminVersion();
      this.setExpectedNextState(1);
      this.setupDeploymentRequest(var3, var4, var1, var1.getSyncToAdminDeployments().iterator());
      this.setupDeploymentContext();
      Map var5 = var1.getSyncToAdminDeploymentsMap();
      if (var5 != null && var5.size() != 0) {
         this.callDeploymentReceivers();
      } else {
         this.doCommit(var1);
      }
   }

   private final void doPreHandleResponseValidation(TargetRequestImpl var1, DeploymentServiceMessage var2) throws Exception {
      String var3;
      if (var1 == null) {
         var3 = DeployerRuntimeLogger.invalidHandleResponse(this.getId());
         if (Debug.serviceLogger.isDebugEnabled()) {
            Debug.serviceLogger.debug(var3);
         }

         throw new Exception(var3);
      } else if (var1.getSyncToAdminDeployments() != null) {
         var3 = DeployerRuntimeLogger.duplicateHandleResponse(var2.toString());
         if (Debug.serviceLogger.isDebugEnabled()) {
            Debug.serviceLogger.debug(var3);
         }

         throw new Exception(var3);
      }
   }

   protected final void setupDeploymentRequest(DomainVersion var1, DomainVersion var2, TargetRequestImpl var3, Iterator var4) throws Exception {
      LinkedHashMap var5 = new LinkedHashMap();
      this.getDeploymentReceiversInfo(var4, var1, var2, var5);
      var3.setSyncToAdminDeploymentsMap(var5);
   }

   protected final void callDeploymentReceivers() throws Exception {
      TargetRequestImpl var1 = this.getDeploymentRequest();
      Map var2 = var1.getDeploymentsMap();
      DeploymentContextImpl var3 = var1.getDeploymentContext();
      boolean var4 = true;
      Iterator var5 = var2.keySet().iterator();

      while(var5.hasNext()) {
         String var6 = (String)var5.next();
         DeploymentReceiver var7 = this.deploymentsManager.getDeploymentReceiver(var6);
         if (var7 != null) {
            var4 = false;
            var7.updateDeploymentContext(var3);
         }

         if (var4) {
            this.deploymentStatus.reset();
         }
      }

   }

   private final void doCommit(TargetRequestImpl var1) {
      DomainVersion var2 = var1.getSyncToAdminVersion();
      if (var2 != null) {
         this.deploymentsManager.setCurrentDomainVersion(var2);
      }

      this.setExpectedNextState(4);
      this.deploymentStatus.getCurrentState().commitSucceeded();
   }

   public final String toString() {
      return "AwaitingGetDeploymentsResponse";
   }
}
