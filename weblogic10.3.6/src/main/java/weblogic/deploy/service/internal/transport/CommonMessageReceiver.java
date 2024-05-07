package weblogic.deploy.service.internal.transport;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Set;
import weblogic.deploy.common.Debug;
import weblogic.deploy.service.internal.DeploymentService;
import weblogic.deploy.service.internal.DeploymentServiceLogger;
import weblogic.deploy.service.internal.DomainVersion;
import weblogic.deploy.service.internal.ServiceRequest;
import weblogic.deploy.service.internal.adminserver.AdminDeploymentsManager;
import weblogic.deploy.service.internal.adminserver.AdminRequestImpl;
import weblogic.deploy.service.internal.adminserver.AdminRequestManager;
import weblogic.deploy.service.internal.adminserver.StatusDeliverer;
import weblogic.deploy.service.internal.targetserver.TargetDeploymentsManager;
import weblogic.deploy.service.internal.targetserver.TargetRequestImpl;
import weblogic.deploy.service.internal.targetserver.TargetRequestManager;
import weblogic.deploy.service.internal.targetserver.TargetRequestStatus;
import weblogic.deploy.service.internal.transport.http.HTTPMessageReceiver;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.StackTraceUtils;
import weblogic.work.WorkManagerFactory;

public final class CommonMessageReceiver implements AdminServerMessageReceiver, TargetServerMessageReceiver, MessageDispatcher {
   private final byte deploymentServiceVersion;
   private final MessageReceiver delegate;
   private final AdminRequestManager adminRequestManager;
   private final AdminDeploymentsManager adminDeploymentsManager;
   private final TargetRequestManager targetRequestManager;
   private final TargetDeploymentsManager targetDeploymentsManager;
   private final StatusDeliverer statusDeliverer;
   private CommonMessageSender messageSender;
   private boolean heartbeatServiceInitialized;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   private CommonMessageReceiver() {
      this.heartbeatServiceInitialized = false;
      DeploymentService var1 = DeploymentService.getDeploymentService();
      this.deploymentServiceVersion = DeploymentService.getVersionByte();
      this.delegate = HTTPMessageReceiver.getMessageReceiver();
      if (ManagementService.getPropertyService(kernelId).isAdminServer()) {
         this.adminRequestManager = AdminRequestManager.getInstance();
         this.adminDeploymentsManager = AdminDeploymentsManager.getInstance();
         this.statusDeliverer = StatusDeliverer.getInstance();
      } else {
         this.adminRequestManager = null;
         this.adminDeploymentsManager = null;
         this.statusDeliverer = null;
      }

      this.targetRequestManager = TargetRequestManager.getInstance();
      this.targetDeploymentsManager = TargetDeploymentsManager.getInstance();
      this.delegate.setDispatcher(this);
   }

   public static CommonMessageReceiver getInstance() {
      return CommonMessageReceiver.Maker.SINGLETON;
   }

   private synchronized CommonMessageSender getMessageSender() {
      if (this.messageSender == null) {
         this.messageSender = CommonMessageSender.getInstance();
      }

      return this.messageSender;
   }

   private final void debug(String var1) {
      Debug.serviceTransportDebug(var1);
   }

   private final boolean isDebugEnabled() {
      return Debug.isServiceTransportDebugEnabled();
   }

   private final void handlePendingCancel(long var1) {
      if (this.isDebugEnabled()) {
         this.debug("handling pending cancel for request with id '" + var1 + "'");
      }

      this.targetRequestManager.removePendingCancelFor(var1);
      this.getMessageSender().sendCancelSucceededMsg(var1);
   }

   public void setHeartbeatServiceInitialized() {
      this.heartbeatServiceInitialized = true;
   }

   public final void receivePrepareAckMsg(DeploymentServiceMessage var1) {
      long var2 = var1.getDeploymentId();
      String var4 = var1.getMessageSrc();
      boolean var5 = (Boolean)var1.getItems().get(0);
      if (this.isDebugEnabled()) {
         this.debug("received 'prepare succeeded' from '" + var4 + "' for id '" + var2 + "'");
      }

      if (this.adminRequestManager == null) {
         this.rejectAdminServerOperation(var4);
      }

      AdminRequestImpl var6 = this.adminRequestManager.getRequest(var2);
      if (var6 != null) {
         var6.receivedPrepareSucceeded(var2, var4, var5);
      } else if (this.isDebugEnabled()) {
         this.debug("'prepare succeeded' received for id '" + var2 + "' from '" + var4 + "' that has no corresponding request");
      }

   }

   public final void receivePrepareNakMsg(DeploymentServiceMessage var1) {
      long var2 = var1.getDeploymentId();
      String var4 = var1.getMessageSrc();
      Throwable var5 = (Throwable)var1.getItems().get(0);
      if (this.isDebugEnabled()) {
         this.debug("received 'prepare failed' from '" + var4 + "' for id '" + var2 + "' with reason '" + var5.getMessage() + "'");
      }

      if (this.adminRequestManager == null) {
         this.rejectAdminServerOperation(var4);
      }

      AdminRequestImpl var6 = this.adminRequestManager.getRequest(var2);
      if (var6 != null) {
         var6.receivedPrepareFailed(var4, var5);
      } else if (this.isDebugEnabled()) {
         this.debug("'prepare failed' received for id " + var2 + "' from '" + var4 + "' that has no corresponding request - it may have been cancelled");
      }

   }

   public final void receiveCommitSucceededMsg(DeploymentServiceMessage var1) {
      long var2 = var1.getDeploymentId();
      String var4 = var1.getMessageSrc();
      if (this.isDebugEnabled()) {
         this.debug("received 'commit succeeded' from '" + var4 + "' for id '" + var2 + "'");
      }

      if (this.adminRequestManager == null) {
         this.rejectAdminServerOperation(var4);
      }

      AdminRequestImpl var5 = this.adminRequestManager.getRequest(var2);
      if (var5 != null) {
         var5.receivedCommitSucceeded(var4);
      } else if (this.isDebugEnabled()) {
         this.debug("'commit success' from '" + var4 + "' for id '" + var2 + "' has no corresponding request - it may have been " + "cancelled");
      }

   }

   public final void receiveCommitFailedMsg(DeploymentServiceMessage var1) {
      long var2 = var1.getDeploymentId();
      String var4 = var1.getMessageSrc();
      Throwable var5 = (Throwable)var1.getItems().get(0);
      if (this.isDebugEnabled()) {
         this.debug("received 'commit failed' from '" + var4 + "' for id '" + var2 + "' with reason '" + var5.getMessage() + "'");
      }

      if (this.adminRequestManager == null) {
         this.rejectAdminServerOperation(var4);
      }

      AdminRequestImpl var6 = this.adminRequestManager.getRequest(var2);
      if (var6 != null) {
         var6.receivedCommitFailed(var4, var5);
      } else if (this.isDebugEnabled()) {
         this.debug("'commit failed' from '" + var4 + "' for id '" + var2 + "' has no corresponding request - it may have been " + "cancelled");
      }

   }

   public final void receiveCancelSucceededMsg(DeploymentServiceMessage var1) {
      long var2 = var1.getDeploymentId();
      String var4 = var1.getMessageSrc();
      if (this.isDebugEnabled()) {
         this.debug("received 'cancel succeeded' from '" + var4 + "' for id '" + var2 + "'");
      }

      if (this.adminRequestManager == null) {
         this.rejectAdminServerOperation(var4);
      }

      AdminRequestImpl var5 = this.adminRequestManager.getRequest(var2);
      if (var5 != null) {
         var5.receivedCancelSucceeded(var4);
      } else if (this.isDebugEnabled()) {
         this.debug("'cancel succeeded' from '" + var4 + "' for id '" + var2 + "' has no corresponding request - it may have been cancelled ");
      }

   }

   public final void receiveCancelFailedMsg(DeploymentServiceMessage var1) {
      long var2 = var1.getDeploymentId();
      String var4 = var1.getMessageSrc();
      Throwable var5 = (Throwable)var1.getItems().get(0);
      if (this.isDebugEnabled()) {
         this.debug("received 'cancel failed' from '" + var4 + "' for id '" + var2 + "' with reason '" + var5.getMessage() + "'");
      }

      if (this.adminRequestManager == null) {
         this.rejectAdminServerOperation(var4);
      }

      AdminRequestImpl var6 = this.adminRequestManager.getRequest(var2);
      if (var6 != null) {
         var6.receivedCancelFailed(var4, var5);
      } else if (this.isDebugEnabled()) {
         this.debug("'cancel failed' from '" + var4 + "' for id '" + var2 + "' has no corresponding request - it may be " + "already complete or have been cancelled");
      }

   }

   public final void receiveGetDeploymentsMsg(DeploymentServiceMessage var1) {
      if (this.isDebugEnabled()) {
         this.debug("received 'get deployments' from '" + var1.getMessageSrc() + "' to sync from version '" + var1.getFromVersion() + "' for id '" + var1.getDeploymentId() + "'");
      }

      String var2 = var1.getMessageSrc();
      if (this.adminRequestManager == null) {
         this.rejectAdminServerOperation(var2);
      }

      String var3 = var1.getMessageSrc();
      this.adminRequestManager.getDeployments(var1.getFromVersion(), var3, var1.getDeploymentId(), false, (String)null, this.getMessageSender().getHandlers(var3));
   }

   public final DeploymentServiceMessage receiveBlockingGetDeploymentsMsg(DeploymentServiceMessage var1) {
      if (this.isDebugEnabled()) {
         this.debug("received 'blocking get deployments' from '" + var1.getMessageSrc() + "'");
      }

      String var2 = var1.getMessageSrc();
      if (this.adminRequestManager == null) {
         this.rejectAdminServerOperation(var2);
      }

      DomainVersion var3 = var1.getFromVersion();
      Set var4 = var3.getDeploymentsVersionMap().keySet();
      this.getMessageSender().putHandlers(var2, var4);
      ArrayList var5 = this.adminRequestManager.getDeployments(var3, var2, var1.getDeploymentId(), true, var1.getDeploymentType(), var4);
      DeploymentServiceMessage var6 = new DeploymentServiceMessage(this.deploymentServiceVersion, (byte)5, var1.getDeploymentId(), var5);
      var6.setFromVersion(var1.getFromVersion());
      var6.setToVersion(this.adminDeploymentsManager.getCurrentDomainVersion().getFilteredVersion(var4));
      if (this.isDebugEnabled()) {
         this.debug("'get deployments response' being returned to '" + var1.getMessageSrc() + "' message --> " + var6);
      }

      return var6;
   }

   public final void receiveStatusMsg(DeploymentServiceMessage var1) {
      ArrayList var2 = var1.getItems();
      String var3 = (String)var2.get(0);
      Serializable var4 = (Serializable)var2.get(1);
      boolean var5 = false;
      long var6 = 0L;
      if (var2.size() > 2) {
         var5 = true;
         var6 = (Long)var2.get(2);
      }

      if (this.isDebugEnabled()) {
         Debug.serviceStatusLogger.debug("received 'status update' from '" + var1.getMessageSrc() + "' on channel id '" + var3 + "'");
      }

      String var8 = var1.getMessageSrc();
      if (this.statusDeliverer == null) {
         this.rejectAdminServerOperation(var8);
      }

      if (var5) {
         this.statusDeliverer.deliverStatus(var6, var3, var4, var1.getMessageSrc());
      } else {
         this.statusDeliverer.deliverStatus(var3, var4, var1.getMessageSrc());
      }

   }

   public final void receiveRequestPrepareMsg(DeploymentServiceMessage var1) {
      long var2 = var1.getDeploymentId();
      if (this.isDebugEnabled()) {
         this.debug("received 'prepare' with id '" + var2 + "'");
      }

      if (this.targetRequestManager.hasAPendingCancelFor(var2)) {
         this.handlePendingCancel(var2);
      } else {
         final TargetRequestImpl var4 = null;

         try {
            var4 = createTargetRequestImpl(var1);
            var4.setControlRequest(!var1.needsVersionUpdate());
            var4.setDomainVersion(var1.getFromVersion());
            this.targetRequestManager.addRequest(new ServiceRequest() {
               public void run() {
                  var4.run();
               }

               public String toString() {
                  return var4.toString();
               }
            });
         } catch (Throwable var8) {
            Throwable var5 = var8;
            if (this.isDebugEnabled()) {
               Debug.serviceDebug(var8.getMessage() + " " + StackTraceUtils.throwable2StackTrace(var8));
            }

            try {
               this.getMessageSender().sendPrepareNakMsg(var1.getDeploymentId(), var5);
            } catch (RemoteException var7) {
               var4.abort();
            }

            if (var4 != null && var4.getDeploymentStatus() != null && !var4.isAborted()) {
               var4.getDeploymentStatus().reset();
            }
         }

      }
   }

   public final void receiveRequestCommitMsg(DeploymentServiceMessage var1) {
      long var2 = var1.getDeploymentId();
      if (this.isDebugEnabled()) {
         this.debug("received 'commit' with id '" + var2 + "'");
      }

      TargetRequestImpl var4 = this.targetRequestManager.getRequest(var2);
      if (var4 == null) {
         if (this.isDebugEnabled()) {
            this.debug("'commit' with id '" + var2 + "' does not have a " + "corresponding request - ignoring message");
         }

      } else {
         TargetRequestStatus var5 = null;

         try {
            var5 = var4.getDeploymentStatus();
            var5.getCurrentState().receivedCommit();
         } catch (Throwable var12) {
            Throwable var6 = var12;

            try {
               this.getMessageSender().sendCommitFailedMsg(var2, var6);
            } catch (RemoteException var11) {
               String var8 = DeploymentServiceLogger.sendCommitFailMsgFailed(var2);

               try {
                  this.getMessageSender().sendCommitFailedMsg(var2, new Exception(var8));
               } catch (RemoteException var10) {
               }
            }

            if (var5 != null) {
               var5.reset();
            }

            if (this.isDebugEnabled()) {
               this.debug(var12.getMessage() + " " + StackTraceUtils.throwable2StackTrace(var12));
            }
         }

      }
   }

   public final void receiveRequestCancelMsg(DeploymentServiceMessage var1) {
      long var2 = var1.getDeploymentId();
      Throwable var4 = (Throwable)var1.getItems().get(0);
      if (this.isDebugEnabled()) {
         this.debug("Received 'cancel' for id '" + var2 + "' due to '" + var4 + "'");
      }

      if (this.targetRequestManager.hasAPendingCancelFor(var2)) {
         if (this.isDebugEnabled()) {
            this.debug("request with id '" + var2 + "' is a pending cancel so send back success message");
         }

         this.handlePendingCancel(var2);
      } else {
         TargetRequestImpl var5 = this.targetRequestManager.getRequest(var2);
         if (var5 == null) {
            if (this.isDebugEnabled()) {
               this.debug("'cancel' with id '" + var2 + "' does not have a corresponding request - saving to " + "'pending cancel' list");
            }

            this.targetRequestManager.addToPendingCancels(var2);
         } else {
            TargetRequestStatus var6 = null;

            try {
               var6 = var5.getDeploymentStatus();
               if (var6 != null) {
                  var6.setCanceled();
                  if (var6.isAborted()) {
                     if (this.isDebugEnabled()) {
                        this.debug("Request '" + var2 + "' aborted so cancel already performed. Just send back success");
                     }

                     this.getMessageSender().sendCancelSucceededMsg(var2);
                  } else {
                     var6.getCurrentState().receivedCancel();
                  }
               }
            } catch (Throwable var8) {
               this.getMessageSender().sendCancelFailedMsg(var2, var8);
               if (var6 != null) {
                  var6.reset();
               }

               if (this.isDebugEnabled()) {
                  this.debug(var8.getMessage() + " " + StackTraceUtils.throwable2StackTrace(var8));
               }
            }

         }
      }
   }

   public final void receiveHeartbeatMsg(final DeploymentServiceMessage var1) {
      if (!this.heartbeatServiceInitialized) {
         if (this.isDebugEnabled()) {
            this.debug("ignoring 'heartbeat' - heartbeat service not initialized");
         }

      } else if (this.targetRequestManager.handlingRequests()) {
         if (this.isDebugEnabled()) {
            this.debug("ignoring 'heartbeat' - requests still in progress");
         }

      } else {
         this.targetRequestManager.addRequest(new ServiceRequest() {
            public void run() {
               try {
                  CommonMessageReceiver.this.handleHeartbeatMessage(var1);
               } catch (Throwable var2) {
               }

            }

            public String toString() {
               return "Heartbeat request";
            }
         });
      }
   }

   private boolean serverInAdminState() {
      ServerRuntimeMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServerRuntime();
      return var1.getState() == "ADMIN";
   }

   private void handleHeartbeatMessage(DeploymentServiceMessage var1) {
      if (!this.targetRequestManager.handlingRequests() && !this.serverInAdminState()) {
         DomainVersion var2 = var1.getFromVersion();
         DomainVersion var3 = this.targetDeploymentsManager.getCurrentDomainVersion();
         if (var3.equals(var2)) {
            if (this.isDebugEnabled()) {
               this.debug("domain version in 'heartbeat' '" + var2 + "' is equal " + "to current domain version");
            }

         } else {
            if (this.isDebugEnabled()) {
               this.debug("domain version in 'heartbeat' '" + var2 + "' not equal " + "to current domain version - needs to sync with admin server");
            }

            TargetRequestImpl var4 = new TargetRequestImpl();
            long var5 = System.currentTimeMillis();
            var4.setId(var5);
            var4.setHeartbeatRequest();
            synchronized(this.targetRequestManager) {
               if (this.targetRequestManager.handlingRequests() || this.serverInAdminState()) {
                  if (this.isDebugEnabled()) {
                     this.debug(" 1 skipping 'heartbeat' handling");
                  }

                  return;
               }

               this.targetRequestManager.addToRequestTable(var4);
            }

            TargetRequestStatus var7 = TargetRequestStatus.createTargetRequestStatus(var4);
            var7.setCurrentState(var7.getTargetServerState(5));
            var4.setDeploymentStatus(var7);
            this.getMessageSender().sendGetDeploymentsMsg(var3, var5);
         }
      } else {
         if (this.isDebugEnabled()) {
            this.debug("skipping 'heartbeat' handling");
         }

      }
   }

   public final void receiveGetDeploymentsResponse(DeploymentServiceMessage var1) {
      if (this.isDebugEnabled()) {
         this.debug("received 'get deployments response' from '" + var1.getMessageSrc());
      }

      long var2 = var1.getDeploymentId();
      TargetRequestImpl var4 = this.targetRequestManager.getRequest(var2);
      if (var4 == null) {
         if (this.isDebugEnabled()) {
            this.debug("'get deployments response' does not have  corresponding request - ignoring message");
         }

      } else {
         TargetRequestStatus var5 = var4.getDeploymentStatus();
         var5.getCurrentState().receivedGetDeploymentsResponse(var1);
      }
   }

   public final void dispatch(DeploymentServiceMessage var1) {
      byte var2 = var1.getMessageType();
      switch (var2) {
         case 0:
            this.receiveHeartbeatMsg(var1);
            break;
         default:
            if (Debug.isServiceDebugEnabled()) {
               Debug.serviceDebug("received illegal message '" + var1.toString() + "'");
            }
      }

   }

   public final DeploymentServiceMessage blockingDispatch(final DeploymentServiceMessage var1) {
      final byte var3 = var1.getMessageType();
      if (var3 == 13) {
         return this.receiveBlockingGetDeploymentsMsg(var1);
      } else {
         WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
            public void run() {
               switch (var3) {
                  case 0:
                     CommonMessageReceiver.this.receiveHeartbeatMsg(var1);
                     break;
                  case 1:
                     CommonMessageReceiver.this.receiveRequestPrepareMsg(var1);
                     break;
                  case 2:
                     CommonMessageReceiver.this.receiveRequestCommitMsg(var1);
                     break;
                  case 3:
                     CommonMessageReceiver.this.receiveRequestCancelMsg(var1);
                     break;
                  case 4:
                     CommonMessageReceiver.this.receiveGetDeploymentsMsg(var1);
                     break;
                  case 5:
                     CommonMessageReceiver.this.receiveGetDeploymentsResponse(var1);
                     break;
                  case 6:
                     CommonMessageReceiver.this.receivePrepareAckMsg(var1);
                     break;
                  case 7:
                     CommonMessageReceiver.this.receivePrepareNakMsg(var1);
                     break;
                  case 8:
                     CommonMessageReceiver.this.receiveCommitSucceededMsg(var1);
                     break;
                  case 9:
                     CommonMessageReceiver.this.receiveCommitFailedMsg(var1);
                     break;
                  case 10:
                     CommonMessageReceiver.this.receiveCancelSucceededMsg(var1);
                     break;
                  case 11:
                     CommonMessageReceiver.this.receiveCancelFailedMsg(var1);
                     break;
                  case 12:
                     CommonMessageReceiver.this.receiveStatusMsg(var1);
                     break;
                  default:
                     if (Debug.isServiceDebugEnabled()) {
                        Debug.serviceDebug("blocking dispatch received illegal message '" + var1.toString() + "'");
                     }
               }

            }
         });
         return null;
      }
   }

   public final MessageReceiver getDelegate() {
      return this.delegate;
   }

   private static TargetRequestImpl createTargetRequestImpl(DeploymentServiceMessage var0) {
      TargetRequestImpl var1 = new TargetRequestImpl();
      ArrayList var2 = var0.getItems();
      long var3 = var0.getDeploymentId();
      var1.setDeployments(var2);
      var1.setId(var3);
      var1.setTimeoutInterval(var0.getTimeoutInterval());
      if (var0.isConfigurationProviderCalledLast()) {
         var1.setCallConfigurationProviderLast();
      }

      AuthenticatedSubject var5 = var0.getInitiator();
      if (var5 != null) {
         var1.setInitiator(var5);
      }

      return var1;
   }

   protected void rejectAdminServerOperation(String var1) {
      throw new UnsupportedOperationException(DeploymentServiceLogger.logExceptionInServletRequestIntendedForAdminServerLoggable(var1).getMessage());
   }

   // $FF: synthetic method
   CommonMessageReceiver(Object var1) {
      this();
   }

   static final class Maker {
      static final CommonMessageReceiver SINGLETON = new CommonMessageReceiver();
   }
}
