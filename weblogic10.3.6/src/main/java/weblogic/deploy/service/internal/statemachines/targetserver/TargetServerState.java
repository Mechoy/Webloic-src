package weblogic.deploy.service.internal.statemachines.targetserver;

import java.rmi.RemoteException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import weblogic.deploy.service.Deployment;
import weblogic.deploy.service.DeploymentReceiver;
import weblogic.deploy.service.internal.DeploymentServiceLogger;
import weblogic.deploy.service.internal.DomainVersion;
import weblogic.deploy.service.internal.statemachines.State;
import weblogic.deploy.service.internal.targetserver.DeploymentContextImpl;
import weblogic.deploy.service.internal.targetserver.TargetDeploymentsManager;
import weblogic.deploy.service.internal.targetserver.TargetRequestImpl;
import weblogic.deploy.service.internal.targetserver.TargetRequestManager;
import weblogic.deploy.service.internal.targetserver.TargetRequestStatus;
import weblogic.deploy.service.internal.targetserver.TimeAuditorManager;
import weblogic.deploy.service.internal.transport.DeploymentServiceMessage;
import weblogic.management.deploy.internal.DeployerRuntimeLogger;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.ServerLifecycleException;

public class TargetServerState extends State {
   protected TargetRequestStatus deploymentStatus = null;
   protected TargetRequestManager requestsManager = null;
   protected TargetDeploymentsManager deploymentsManager = null;
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public TargetServerState() {
      this.deploymentsManager = TargetDeploymentsManager.getInstance();
      this.requestsManager = TargetRequestManager.getInstance();
   }

   public synchronized TargetServerState contextUpdated() {
      this.fireStateTransitionEvent(this, "contextUpdated", this.getId());
      return this.getCurrentState();
   }

   public synchronized TargetServerState contextUpdateFailed(Throwable var1) {
      this.fireStateTransitionEvent(this, "contextUpdateFailed", this.getId());
      return this.getCurrentState();
   }

   public synchronized TargetServerState prepareSucceeded() {
      this.fireStateTransitionEvent(this, "prepareSucceeded", this.getId());
      return this.getCurrentState();
   }

   public synchronized TargetServerState prepareFailed() {
      this.fireStateTransitionEvent(this, "prepareFailed", this.getId());
      return this.getCurrentState();
   }

   public synchronized TargetServerState commitSucceeded() {
      this.fireStateTransitionEvent(this, "commitSucceeded", this.getId());
      return this.getCurrentState();
   }

   public synchronized TargetServerState commitFailed() {
      this.fireStateTransitionEvent(this, "commitFailed", this.getId());
      return this.getCurrentState();
   }

   public synchronized TargetServerState cancelSucceeded() {
      this.fireStateTransitionEvent(this, "cancelSucceeded", this.getId());
      return this.getCurrentState();
   }

   public synchronized TargetServerState cancelFailed() {
      this.fireStateTransitionEvent(this, "cancelFailed", this.getId());
      return this.getCurrentState();
   }

   public synchronized TargetServerState receivedPrepare() {
      this.fireStateTransitionEvent(this, "receivedPrepare", 0L);
      return this.getCurrentState();
   }

   public synchronized TargetServerState receivedCommit() {
      this.fireStateTransitionEvent(this, "receivedCommit", this.getId());
      return this.getCurrentState();
   }

   public synchronized TargetServerState receivedCancel() {
      this.fireStateTransitionEvent(this, "receivedCancel", this.getId());
      return this.getCurrentState();
   }

   public synchronized TargetServerState receivedGetDeploymentsResponse(DeploymentServiceMessage var1) {
      this.fireStateTransitionEvent(this, "receivedGetDeltasResponse", 0L);
      return this.getCurrentState();
   }

   public synchronized TargetServerState abort() {
      this.fireStateTransitionEvent(this, "abort", this.getId());
      if (this.deploymentStatus != null) {
         this.deploymentStatus.setAborted();
      }

      if (this.deploymentStatus != null && !this.deploymentStatus.isCanceled()) {
         if (this.getId() != -1L) {
            this.requestsManager.addToPendingCancels(this.getId());
            if (this.isDebugEnabled()) {
               this.debug("'abort' with id '" + this.getId() + "' added as pending cancel for future cancel request");
            }
         }

         this.cancelIfNecessary();
      }

      return this.getCurrentState();
   }

   public final void setDeploymentStatus(TargetRequestStatus var1) {
      this.deploymentStatus = var1;
   }

   protected final void transitionServerToAdminState() {
      ServerRuntimeMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServerRuntime();

      try {
         DeploymentServiceLogger.transitioningServerToAdminState();
         var1.setRestartRequired(true);
         var1.suspend();
      } catch (ServerLifecycleException var11) {
         if (this.isDebugEnabled()) {
            this.debug("Attempt to 'suspend' server failed due to '" + var11 + "' will attempt to 'force suspend' the server");
         }

         try {
            var1.forceSuspend();
         } catch (Exception var10) {
            if (this.isDebugEnabled()) {
               this.debug("Attempt to 'force suspend' server failed due to '" + var10 + "' ignoring the event");
            }
         }
      } finally {
         if (this.deploymentStatus != null) {
            TargetRequestImpl var6 = this.deploymentStatus.getDeploymentRequest();
            var6.resetSyncToAdminDeployments();
            this.deploymentStatus.reset();
         }

      }

   }

   protected final TargetServerState getCurrentState() {
      TargetServerState var1 = null;
      if (this.deploymentStatus != null) {
         var1 = this.deploymentStatus.getCurrentState();
      }

      return var1;
   }

   protected final TargetRequestImpl getDeploymentRequest() {
      return this.requestsManager.getRequest(this.getId());
   }

   protected final DeploymentContextImpl getDeploymentContext() {
      TargetRequestImpl var1 = this.getDeploymentRequest();
      return var1 == null ? null : var1.getDeploymentContext();
   }

   protected final long getId() {
      long var1 = -1L;
      if (this.deploymentStatus != null) {
         var1 = this.deploymentStatus.getId();
      }

      return var1;
   }

   protected final String getTargetStateString(int var1) {
      String var2 = null;
      switch (var1) {
         case 0:
            var2 = "ReceivedPrepare";
            break;
         case 1:
            var2 = "AwaitingContextUpdateCompletion";
            break;
         case 2:
            var2 = "AwaitingPrepareCompletion";
            break;
         case 3:
            var2 = "AwaitingCommit";
            break;
         case 4:
            var2 = "AwaitingCommitCompletion";
            break;
         case 5:
            var2 = "AwaitingGetDeploymentsResponse";
            break;
         case 6:
            var2 = "AwaitingCancel";
            break;
         case 7:
            var2 = "AwaitingCancelCompletion";
      }

      return var2;
   }

   protected final void setExpectedNextState(int var1) {
      TargetServerState var2 = this.deploymentStatus.getTargetServerState(var1);
      if (this.isDebugEnabled()) {
         this.debug("Setting target state of deployment '" + this.getId() + "' to : " + this.getTargetStateString(var1));
      }

      this.deploymentStatus.setCurrentState(var2);
   }

   private final void dispatchCancelToDeploymentReceivers() {
      TargetRequestImpl var1 = this.getDeploymentRequest();
      TimeAuditorManager.getInstance().startTransition(var1.getId(), 3);
      if (var1 != null && this.deploymentStatus != null && !this.deploymentStatus.isCancelDispatched()) {
         Iterator var2 = this.deploymentStatus.getDeploymentsToBeCancelled();
         this.deploymentStatus.setCancelDispatched();
         if (var2 == null) {
            if (this.deploymentStatus.isCanceled()) {
               if (this.isDebugEnabled()) {
                  this.debug("No deployments to cancel for request '" + var1.getId() + "'");
               }

               this.sendCancelSucceeded();
            }
         } else {
            while(var2.hasNext()) {
               String var3 = (String)var2.next();
               DeploymentReceiver var4 = this.deploymentsManager.getDeploymentReceiver(var3);
               DeploymentContextImpl var5 = var1.getDeploymentContext();
               TimeAuditorManager.getInstance().startDeploymentTransition(var1.getId(), var3, 3);
               var4.cancel(var5);
            }
         }

      }
   }

   protected final void sendPrepareNak(Throwable var1) {
      TargetRequestImpl var2 = this.getDeploymentRequest();
      if (var2 != null) {
         try {
            this.sender.sendPrepareNakMsg(var2.getId(), var1);
         } catch (RemoteException var4) {
            this.deploymentStatus.setAborted();
            this.cancelIfNecessary();
         }

      }
   }

   protected final void sendCommitSucceeded() {
      long var1 = this.getId();
      TargetRequestImpl var3 = this.requestsManager.getRequest(var1);
      if (var3 == null) {
         if (this.isDebugEnabled()) {
            this.debug("no request corresponding to id '" + var1 + "' - aborting " + "'commit success' send attempt");
         }

      } else {
         this.sender.sendCommitSucceededMsg(var3.getId());
         this.deploymentStatus.reset();
      }
   }

   protected final void sendCommitFailed(Throwable var1) throws RemoteException {
      long var2 = this.getId();
      TargetRequestImpl var4 = this.requestsManager.getRequest(var2);
      if (var4 == null) {
         if (this.isDebugEnabled()) {
            this.debug("no request corresponding to id '" + var2 + "' - aborting " + "'commit failure' send attempt");
         }

      } else {
         try {
            this.sender.sendCommitFailedMsg(var4.getId(), var1);
         } finally {
            this.deploymentStatus.reset();
         }

      }
   }

   protected final void sendCancelSucceeded() {
      TargetRequestImpl var1 = this.getDeploymentRequest();
      if (var1 != null) {
         this.sender.sendCancelSucceededMsg(var1.getId());
         this.deploymentStatus.reset();
      }
   }

   protected final void sendCancelFailed(Throwable var1) {
      TargetRequestImpl var2 = this.getDeploymentRequest();
      if (var2 != null) {
         this.sender.sendCancelFailedMsg(var2.getId(), var1);
         this.deploymentStatus.reset();
      }
   }

   protected void setupDeploymentRequest(DomainVersion var1, DomainVersion var2, TargetRequestImpl var3, Iterator var4) throws Exception {
      LinkedHashMap var5 = new LinkedHashMap();
      this.getDeploymentReceiversInfo(var4, var1, var2, var5);
      var3.setProposedDomainVersion(var2);
      var3.setPreparingFromVersion(var1);
      var3.setDeploymentsMap(var5);
   }

   protected void callDeploymentReceivers() throws Exception {
   }

   protected final void getDeploymentReceiversInfo(Iterator var1, DomainVersion var2, DomainVersion var3, Map var4) throws Exception {
      while(var1.hasNext()) {
         Deployment var5 = (Deployment)var1.next();
         if (var5 != null) {
            String var6 = var5.getCallbackHandlerId();
            if (this.deploymentsManager.getDeploymentReceiver(var6) == null) {
               String var8 = DeployerRuntimeLogger.receiverNotFound(var6);
               throw new Exception(var8);
            }

            this.deploymentStatus.addToDeploymentsAckList(var6);
            Object var7 = (List)var4.get(var6);
            if (var7 == null) {
               var7 = new ArrayList();
               var4.put(var6, var7);
            }

            ((List)var7).add(var5);
            var3.addOrUpdateDeploymentVersion(var6, var5.getProposedVersion());
         }
      }

   }

   protected final void setupDeploymentContext() throws Exception {
      TargetRequestImpl var1 = this.getDeploymentRequest();
      if (var1 == null) {
         String var3 = DeployerRuntimeLogger.noDeploymentRequest();
         throw new Exception(var3);
      } else {
         DeploymentContextImpl var2 = var1.getDeploymentContext();
         if (var2 == null) {
            var2 = new DeploymentContextImpl(var1);
            var1.setDeploymentContext(var2);
         }

      }
   }

   protected final void syncWithAdminServer(DomainVersion var1) {
      if (this.isDebugEnabled()) {
         this.debug("Deployment '" + this.getId() + "' in '" + this.getCurrentState() + "' state needs to sync with admin to catch up from '" + var1.toString());
      }

      TargetServerState var2 = this.deploymentStatus.getTargetServerState(5);
      this.deploymentStatus.setCurrentState(var2);
      this.sender.sendGetDeploymentsMsg(var1, this.getId());
   }

   protected boolean cancelIfNecessary() {
      boolean var1 = false;
      if (this.deploymentStatus != null && this.deploymentStatus.isCanceledOrAborted()) {
         var1 = true;
         this.doCancel();
      }

      return var1;
   }

   private void doCancel() {
      this.setExpectedNextState(7);
      this.dispatchCancelToDeploymentReceivers();
   }

   protected final void fireStateTransitionEvent(State var1, String var2, long var3) {
      if (this.isDebugEnabled()) {
         this.debug("Target DeploymentService event : '" + var1.toString() + "." + var2 + "()' for deployment id '" + var3 + "'");
      }

   }
}
