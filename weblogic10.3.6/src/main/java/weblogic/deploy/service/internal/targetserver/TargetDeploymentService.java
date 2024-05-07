package weblogic.deploy.service.internal.targetserver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import weblogic.deploy.common.Debug;
import weblogic.deploy.service.Deployment;
import weblogic.deploy.service.DeploymentContext;
import weblogic.deploy.service.DeploymentReceiver;
import weblogic.deploy.service.DeploymentReceiversCoordinator;
import weblogic.deploy.service.RegistrationException;
import weblogic.deploy.service.StatusRelayer;
import weblogic.deploy.service.Version;
import weblogic.deploy.service.internal.DomainVersion;
import weblogic.deploy.service.internal.ServiceRequest;
import weblogic.deploy.service.internal.transport.CommonMessageSender;
import weblogic.deploy.service.internal.transport.DeploymentServiceMessage;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;
import weblogic.utils.StackTraceUtils;

public final class TargetDeploymentService extends AbstractServerService implements DeploymentReceiversCoordinator, StatusRelayer {
   private TargetRequestManager requestManager;
   private final TargetDeploymentsManager deploymentsManager;

   private TargetDeploymentService() {
      this.deploymentsManager = TargetDeploymentsManager.getInstance();
   }

   public static TargetDeploymentService getDeploymentService() {
      return TargetDeploymentService.Maker.SINGLETON;
   }

   private void debug(String var1) {
      Debug.serviceDebug(var1);
   }

   private boolean isDebugEnabled() {
      return Debug.isServiceDebugEnabled();
   }

   public DeploymentContext registerHandler(Version var1, DeploymentReceiver var2) throws RegistrationException {
      final String var3 = var2.getHandlerIdentity();
      final String var4 = "registerHandler called from DeploymentReceiver for '" + var3 + "'  with version '" + var1.toString() + "'";
      if (this.isDebugEnabled()) {
         this.debug(var4);
      }

      this.deploymentsManager.registerCallbackHandler(var1, var2);
      final TargetRequestImpl var5 = new TargetRequestImpl();
      var5.setId(System.currentTimeMillis());
      final TargetRequestManager var7 = this.requestManager;
      final RegistrationResponse var8 = new RegistrationResponse();
      this.requestManager.addRequest(new ServiceRequest() {
         public void run() {
            var7.addToRequestTable(var5);
            var5.setDeploymentStatus(TargetRequestStatus.createTargetRequestStatus(var5));
            var5.getDeploymentStatus().setServerStarting();
            CommonMessageSender var1 = var5.getMessageSender();
            DomainVersion var2 = TargetDeploymentService.this.deploymentsManager.getCurrentDomainVersion();

            try {
               DeploymentServiceMessage var3x = var1.sendBlockingGetDeploymentsMsg(var2, var3);
               DomainVersion var4x = var3x.getToVersion();
               Version var5x = var4x.getDeploymentVersion(var3);
               Iterator var6 = var3x.getItems().iterator();
               ArrayList var7x = new ArrayList();

               while(var6.hasNext()) {
                  Deployment var8x = (Deployment)var6.next();
                  String var9 = var8x.getCallbackHandlerId();
                  if (TargetDeploymentService.this.isDebugEnabled()) {
                     TargetDeploymentService.this.debug(" TargetDeploymentService: deployment identity = " + var9 + " : callback handler id from request = " + var3);
                  }

                  if (var9.equals(var3)) {
                     if (TargetDeploymentService.this.isDebugEnabled()) {
                        TargetDeploymentService.this.debug(" TargetDeploymentService: adding deployment : " + var8x);
                     }

                     var7x.add(var8x);
                  }
               }

               var5.setDeployments(var7x);
               DeploymentContextImpl var11 = new DeploymentContextImpl(var5);
               var5.setDeploymentContext(var11);
               if (var5x != null) {
                  var2.addOrUpdateDeploymentVersion(var3, var5x);
               }

               var8.setResponseReceived(var11);
            } catch (Throwable var10) {
               if (TargetDeploymentService.this.isDebugEnabled()) {
                  TargetDeploymentService.this.debug(StackTraceUtils.throwable2StackTrace(var10));
               }

               var8.setErrorEncountered(var10);
               TargetDeploymentService.this.resetRegistration(var5.getDeploymentStatus());
            }
         }

         public String toString() {
            return var4;
         }
      });
      var8.waitForResponse();
      if (var8.errorEncountered()) {
         this.resetRegistration(var5.getDeploymentStatus());
         throw new RegistrationException(var8.getError().toString());
      } else {
         this.resetRegistration(var5.getDeploymentStatus());
         return var8.getContext();
      }
   }

   public void unregisterHandler(final String var1) {
      final String var2 = "registerHandler called from DeploymentReceiver for '" + var1 + "' ";
      if (this.isDebugEnabled()) {
         this.debug(var2);
      }

      this.requestManager.addRequest(new ServiceRequest() {
         public void run() {
            TargetDeploymentService.this.deploymentsManager.unregisterCallbackHandler(var1);
            TargetDeploymentService.this.requestManager.scheduleNextRequest();
         }

         public String toString() {
            return var2;
         }
      });
   }

   public synchronized void notifyContextUpdated(long var1, String var3) {
      if (this.isDebugEnabled()) {
         this.debug("'context updated' received from DeploymentReceiver for '" + var3 + "'  for id '" + var1 + "'");
      }

      TargetRequestImpl var4 = this.requestManager.getRequest(var1);
      if (var4 == null) {
         if (this.isDebugEnabled()) {
            this.debug("'context updated' received from DeploymentReceiver for '" + var3 + "'  for id '" + var1 + "' does not have a corresponding request - ignoring notification");
         }
      } else {
         TargetRequestStatus var5 = var4.getDeploymentStatus();
         var5.receivedContextUpdateCompletedFrom(var3);
         if (var5.receivedAllContextUpdates()) {
            Throwable var6 = var5.getSavedError();
            if (var6 == null) {
               var5.getCurrentState().contextUpdated();
            } else {
               var5.getCurrentState().contextUpdateFailed(var6);
            }
         }
      }

   }

   public synchronized void notifyContextUpdateFailed(long var1, String var3, Throwable var4) {
      if (this.isDebugEnabled()) {
         this.debug("'context update failed' received from DeploymentReceiver for '" + var3 + "'  for id '" + var1 + "' due to '" + var4.toString() + "'");
      }

      TargetRequestImpl var5 = this.requestManager.getRequest(var1);
      if (var5 == null) {
         if (this.isDebugEnabled()) {
            this.debug("'context update failure' from DeploymentReceiver for '" + var3 + "'  for id '" + var1 + "' does not have a corresponding request - ignoring notification");
         }
      } else {
         TargetRequestStatus var6 = var5.getDeploymentStatus();
         var6.receivedContextUpdateCompletedFrom(var3, var4);
         if (var6.receivedAllContextUpdates()) {
            var6.getCurrentState().contextUpdateFailed(var4);
         }
      }

   }

   public synchronized void notifyPrepareSuccess(long var1, String var3) {
      if (this.isDebugEnabled()) {
         this.debug("'prepare ack' received from DeploymentReceiver for '" + var3 + "'  for id '" + var1 + "'");
      }

      TargetRequestImpl var4 = this.requestManager.getRequest(var1);
      if (var4 == null) {
         if (this.isDebugEnabled()) {
            this.debug("'prepare ack' from DeploymentReceiver for '" + var3 + "'  for id '" + var1 + "' does not have a corresponding request - ignoring notification");
         }
      } else {
         TargetRequestStatus var5 = var4.getDeploymentStatus();
         var5.receivedPrepareAckFrom(var3);
         if (var5.receivedAllPrepareCompletions()) {
            Throwable var6 = var5.getSavedError();
            if (var6 == null) {
               var5.getCurrentState().prepareSucceeded();
            } else {
               var5.getCurrentState().prepareFailed();
            }
         }
      }

   }

   public synchronized void notifyPrepareFailure(long var1, String var3, Throwable var4) {
      if (this.isDebugEnabled()) {
         this.debug("'prepare nak' received from DeploymentReceiver for '" + var3 + "'  for id '" + var1 + "' due to '" + var4.toString() + "'");
      }

      TargetRequestImpl var5 = this.requestManager.getRequest(var1);
      if (var5 == null) {
         if (this.isDebugEnabled()) {
            this.debug("'prepare nak' from DeploymentReceiver for '" + var3 + "'  for id '" + var1 + "' does not have a corresponding request - ignoring notification");
         }
      } else {
         TargetRequestStatus var6 = var5.getDeploymentStatus();
         var6.receivedPrepareNakFrom(var3, var4);
         if (var6.receivedAllPrepareCompletions()) {
            var6.getCurrentState().prepareFailed();
         }
      }

   }

   public synchronized void notifyCommitSuccess(long var1, String var3) {
      if (this.isDebugEnabled()) {
         this.debug("'commit success' received from DeploymentReceiver for '" + var3 + "'  for id '" + var1 + "'");
      }

      TargetRequestImpl var4 = this.requestManager.getRequest(var1);
      if (var4 == null) {
         if (this.isDebugEnabled()) {
            this.debug("'commit success' from DeploymentReceiver for '" + var3 + "'  for id '" + var1 + "' does not have a corresponding request - ignoring notification");
         }
      } else {
         TargetRequestStatus var5 = var4.getDeploymentStatus();
         var5.receivedCommitAckFrom(var3);
         if (var5.receivedAllCommitResponses()) {
            Throwable var6 = var5.getCommitFailureError();
            if (var6 == null) {
               var5.getCurrentState().commitSucceeded();
            } else {
               var5.getCurrentState().commitFailed();
            }
         }
      }

   }

   public synchronized void notifyCommitFailure(long var1, String var3, Throwable var4) {
      if (this.isDebugEnabled()) {
         this.debug("'commit failure' received from DeploymentReceiver for '" + var3 + "'  for id '" + var1 + "' due to '" + var4.toString() + "'");
      }

      TargetRequestImpl var5 = this.requestManager.getRequest(var1);
      if (var5 == null) {
         if (this.isDebugEnabled()) {
            this.debug("'commit failure' from DeploymentReceiver for '" + var3 + "'  for id '" + var1 + "' does not have a corresponding request - ignoring notification");
         }
      } else {
         TargetRequestStatus var6 = var5.getDeploymentStatus();
         var6.receivedCommitFailureFrom(var3, var4);
         if (var6.receivedAllCommitResponses()) {
            var6.getCurrentState().commitFailed();
         }
      }

   }

   public synchronized void notifyCancelSuccess(long var1, String var3) {
      if (this.isDebugEnabled()) {
         this.debug("'cancel success' received from DeploymentReceiver for '" + var3 + "'  for id '" + var1 + "'");
      }

      TargetRequestImpl var4 = this.requestManager.getRequest(var1);
      if (var4 == null) {
         if (this.isDebugEnabled()) {
            this.debug("'cancel success' from DeploymentReceiver for '" + var3 + "'  for id '" + var1 + "' does not have a corresponding request - ignoring notification");
         }
      } else {
         TargetRequestStatus var5 = var4.getDeploymentStatus();
         var5.cancelSuccessFrom(var3);
         if (var5.receivedAllCancelResponses()) {
            Throwable var6 = var5.getCancelFailureError();
            if (var6 == null) {
               var5.getCurrentState().cancelSucceeded();
            } else {
               var5.getCurrentState().cancelFailed();
            }
         }
      }

   }

   public synchronized void notifyCancelFailure(long var1, String var3, Throwable var4) {
      if (this.isDebugEnabled()) {
         this.debug("'cancel failure' received from DeploymentReceiver for '" + var3 + "'  for id '" + var1 + "' due to '" + var4.toString() + "'");
      }

      TargetRequestImpl var5 = this.requestManager.getRequest(var1);
      if (var5 == null) {
         if (this.isDebugEnabled()) {
            this.debug("'cancel failure' from DeploymentReceiver for '" + var3 + "'  for id '" + var1 + "' does not have a corresponding request - ignoring notification");
         }
      } else {
         TargetRequestStatus var6 = var5.getDeploymentStatus();
         var6.cancelFailureFrom(var3, var4);
         if (var6.receivedAllCancelResponses()) {
            var6.getCurrentState().cancelFailed();
         }
      }

   }

   public void notifyStatusUpdate(long var1, String var3, Serializable var4) {
      if (Debug.isServiceStatusDebugEnabled()) {
         Debug.serviceStatusDebug("'status update' received from DeploymentReceiver for '" + var3 + "'  for id '" + var1 + "'");
      }

      this.relayStatus(var1, var3, var4);
   }

   public void relayStatus(String var1, Serializable var2) {
      if (Debug.isServiceStatusDebugEnabled()) {
         Debug.serviceStatusDebug("'relaying status ' '" + var2 + "' on '" + var1 + "'");
      }

      CommonMessageSender var3 = CommonMessageSender.getInstance();
      var3.sendStatusMsg(var1, var2);
   }

   public void relayStatus(long var1, String var3, Serializable var4) {
      if (Debug.isServiceStatusDebugEnabled()) {
         Debug.serviceStatusDebug("'relaying status ' '" + var4 + "' with id '" + var1 + "' on '" + var3 + "'");
      }

      CommonMessageSender var5 = CommonMessageSender.getInstance();
      var5.sendStatusMsg(var1, var3, var4);
   }

   public void start() throws ServiceFailureException {
      this.requestManager = TargetRequestManager.getInstance();
   }

   private void resetRegistration(TargetRequestStatus var1) {
      var1.reset();
      this.requestManager.scheduleNextRequest();
   }

   // $FF: synthetic method
   TargetDeploymentService(Object var1) {
      this();
   }

   private final class RegistrationResponse {
      boolean responseReceived;
      DeploymentContext context;
      Throwable error;

      RegistrationResponse() {
      }

      synchronized void waitForResponse() {
         if (!this.responseReceived) {
            try {
               this.wait();
            } catch (InterruptedException var2) {
               if (Debug.isServiceStatusDebugEnabled()) {
                  Debug.serviceDebug("DeploymentService: registerHandler: Interrupted while waiting for response");
               }
            }

         }
      }

      DeploymentContext getContext() {
         return this.context;
      }

      synchronized void setResponseReceived(DeploymentContext var1) {
         this.context = var1;
         this.responseReceived = true;
         this.notify();
      }

      synchronized void setErrorEncountered(Throwable var1) {
         this.error = var1;
         this.responseReceived = true;
         this.notify();
      }

      boolean errorEncountered() {
         return this.error != null;
      }

      Throwable getError() {
         return this.error;
      }
   }

   static class Maker {
      static final TargetDeploymentService SINGLETON = new TargetDeploymentService();
   }
}
