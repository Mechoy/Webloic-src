package weblogic.deploy.service.internal.transport;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import weblogic.deploy.common.Debug;
import weblogic.deploy.service.internal.DeploymentService;
import weblogic.deploy.service.internal.DomainVersion;
import weblogic.deploy.service.internal.adminserver.AdminDeploymentsManager;
import weblogic.deploy.service.internal.adminserver.AdminRequestImpl;
import weblogic.deploy.service.internal.adminserver.AdminRequestManager;
import weblogic.deploy.service.internal.adminserver.AdminRequestStatus;
import weblogic.deploy.service.internal.transport.http.HTTPMessageSender;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.StackTraceUtils;
import weblogic.work.WorkManagerFactory;

public final class CommonMessageSender implements AdminServerMessageSender, TargetServerMessageSender {
   private MessageSender delegate;
   private final byte deploymentServiceVersion;
   private final AdminDeploymentsManager adminDeploymentsManager;
   private final AdminRequestManager adminRequestManager;
   private final Map serverToHandlers;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private String localServerName;

   private CommonMessageSender() {
      this.serverToHandlers = Collections.synchronizedMap(new HashMap());
      DeploymentService var1 = DeploymentService.getDeploymentService();
      this.deploymentServiceVersion = DeploymentService.getVersionByte();
      this.adminDeploymentsManager = AdminDeploymentsManager.getInstance();
      this.adminRequestManager = AdminRequestManager.getInstance();
      this.setDelegate(HTTPMessageSender.getMessageSender());
   }

   public static CommonMessageSender getInstance() {
      return CommonMessageSender.Maker.SINGLETON;
   }

   private static final void debug(String var0) {
      Debug.serviceTransportDebug(var0);
   }

   private static final boolean isDebugEnabled() {
      return Debug.isServiceTransportDebugEnabled();
   }

   private void setDelegate(MessageSender var1) {
      if (this.delegate == null) {
         this.delegate = var1;
      }

   }

   public final MessageSender getDelegate() {
      return this.delegate;
   }

   private String getLocalServerName() {
      if (this.localServerName == null) {
         this.localServerName = ManagementService.getRuntimeAccess(kernelId).getServerName();
      }

      return this.localServerName;
   }

   public void sendHeartbeatMsg(List var1) {
      if (var1 != null && !var1.isEmpty()) {
         DeploymentServiceMessage var2 = this.createHeartbeatMessage();
         DomainVersion var3 = this.adminDeploymentsManager.getCurrentDomainVersion();
         Iterator var4 = var1.iterator();

         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            if (!this.getLocalServerName().equals(var5)) {
               Set var6 = this.getHandlers(var5);
               var2.setFromVersion(var3.getFilteredVersion(var6));
               this.sendOutHeartbeatMsg(var2, var5);
            }
         }

      }
   }

   private DeploymentServiceMessage createHeartbeatMessage() {
      ArrayList var1 = new ArrayList();
      DeploymentServiceMessage var2 = new DeploymentServiceMessage(this.deploymentServiceVersion, (byte)0, -1L, var1);
      return var2;
   }

   public final void sendRequestPrepareMsg(AdminRequestImpl var1) {
      if (isDebugEnabled()) {
         debug("start send 'prepare' for id '" + var1.getId() + "'");
      }

      Iterator var3 = var1.getTargetServers();
      if (var3 != null) {
         String var2;
         DeploymentServiceMessage var5;
         for(DomainVersion var4 = this.adminDeploymentsManager.getCurrentDomainVersion(); var3.hasNext(); this.sendOutPrepareMsg(var5, var2, var1)) {
            var2 = (String)var3.next();
            var5 = new DeploymentServiceMessage(this.deploymentServiceVersion, (byte)1, var1, var2);
            DomainVersion var6 = var4;
            if (!this.getLocalServerName().equals(var2)) {
               Set var7 = this.getHandlers(var2);
               var6 = var4.getFilteredVersion(var7);
            }

            var5.setFromVersion(var6);
            if (isDebugEnabled()) {
               debug("sending 'prepare' for id '" + var1.getId() + "' to '" + var2 + "' message -->" + var5);
            }
         }

      }
   }

   public final void sendRequestCommitMsg(AdminRequestImpl var1) {
      if (isDebugEnabled()) {
         debug("start send 'commit' for id '" + var1.getId() + "'");
      }

      AdminRequestStatus var2 = var1.getStatus();
      DeploymentServiceMessage var3 = new DeploymentServiceMessage(this.deploymentServiceVersion, (byte)2, var1.getId(), new ArrayList());

      String var5;
      for(Iterator var4 = var2.getTargetsToBeCommitted(); var4.hasNext(); this.sendOutCommitMsg(var3, var5, var1)) {
         var5 = (String)var4.next();
         if (isDebugEnabled()) {
            debug("sending 'commit' for id '" + var1.getId() + "' to '" + var5 + "'");
         }
      }

   }

   public final void sendRequestCancelMsg(AdminRequestImpl var1, Throwable var2) {
      if (isDebugEnabled()) {
         debug("start send 'cancel' for id '" + var1.getId() + "'");
      }

      AdminRequestStatus var3 = var1.getStatus();
      ArrayList var4 = new ArrayList();
      var4.add(var2);
      DeploymentServiceMessage var5 = new DeploymentServiceMessage(this.deploymentServiceVersion, (byte)3, var1.getId(), var4);

      String var6;
      for(Iterator var7 = var3.getTargetsToBeCancelled(); var7.hasNext(); this.sendOutCancelMsg(var5, var6, var1)) {
         var6 = (String)var7.next();
         if (isDebugEnabled()) {
            debug("sending 'cancel' for id '" + var1.getId() + "' to '" + var6 + "'");
         }
      }

   }

   public final void sendGetDeploymentsResponse(ArrayList var1, String var2, DomainVersion var3, long var4) {
      DeploymentServiceMessage var6 = new DeploymentServiceMessage(this.deploymentServiceVersion, (byte)5, var4, var1);
      var6.setToVersion(var3);

      try {
         if (this.getLocalServerName().equals(var2)) {
            this.delegate.sendMessageToAdminServer(var6);
         } else {
            Set var7 = this.getHandlers(var2);
            DomainVersion var8 = var3.getFilteredVersion(var7);
            var6.setToVersion(var8);
            this.delegate.sendMessageToTargetServer(var6, var2);
         }

         if (isDebugEnabled()) {
            debug("start send 'get deployments response' '" + var1 + "' to '" + var2 + " to version '" + var6.getToVersion() + "'");
         }
      } catch (Exception var9) {
         if (Debug.isServiceTransportDebugEnabled()) {
            Debug.serviceTransportDebug("send 'get deployments response' to '" + var2 + "' failed due to '" + var9.getMessage() + "'");
         }
      }

   }

   private final void sendOutPrepareMsg(final DeploymentServiceMessage var1, final String var2, final AdminRequestImpl var3) {
      WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
         public void run() {
            long var1x = var3.getId();
            AdminRequestImpl var3x = CommonMessageSender.this.adminRequestManager.getRequest(var1x);

            try {
               if (CommonMessageSender.this.getLocalServerName().equals(var2)) {
                  var3.prepareDeliveredTo(var2);
                  CommonMessageSender.this.delegate.sendMessageToAdminServer(var1);
               } else {
                  if (var3x != null) {
                     CommonMessageSender.this.adminRequestManager.addPrepareDisconnectListener(var2, var3x);
                  }

                  CommonMessageSender.this.delegate.sendMessageToTargetServer(var1, var2);
                  var3.prepareDeliveredTo(var2);
               }
            } catch (Throwable var6) {
               if (CommonMessageSender.isDebugEnabled()) {
                  CommonMessageSender.debug("send 'prepare' of id '" + var1x + "' to '" + var2 + "' failed due to '" + StackTraceUtils.throwable2StackTrace(var6) + "'");
               }

               if (var3x == null) {
                  if (CommonMessageSender.isDebugEnabled()) {
                     CommonMessageSender.debug("prepare delivery failure to '" + var2 + "' for request '" + var1x + "' could not be " + "dispatched since request is no longer available");
                  }

                  return;
               }

               Exception var5 = var6 instanceof Exception ? (Exception)var6 : new Exception(var6);
               if (var5 instanceof UnreachableHostException) {
                  var3x.prepareDeliveryFailureWhenContacting(var2, var5);
               } else {
                  var3x.prepareDeliveredTo(var2);
                  var3x.receivedPrepareFailed(var2, var5, true);
               }
            }

         }
      });
   }

   private final void sendOutCommitMsg(final DeploymentServiceMessage var1, final String var2, final AdminRequestImpl var3) {
      final long var4 = var3.getId();
      WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
         public void run() {
            AdminRequestImpl var1x = CommonMessageSender.this.adminRequestManager.getRequest(var4);

            try {
               if (CommonMessageSender.this.getLocalServerName().equals(var2)) {
                  var3.commitDeliveredTo(var2);
                  CommonMessageSender.this.delegate.sendMessageToAdminServer(var1);
               } else {
                  if (var1x != null) {
                     CommonMessageSender.this.adminRequestManager.addCommitDisconnectListener(var2, var1x);
                  }

                  CommonMessageSender.this.delegate.sendMessageToTargetServer(var1, var2);
                  var3.commitDeliveredTo(var2);
               }
            } catch (Throwable var4x) {
               if (CommonMessageSender.isDebugEnabled()) {
                  CommonMessageSender.debug("send 'commit' of id '" + var3.getId() + "' to '" + var2 + "' failed due to '" + StackTraceUtils.throwable2StackTrace(var4x) + "'");
               }

               if (var1x == null) {
                  if (CommonMessageSender.isDebugEnabled()) {
                     CommonMessageSender.debug("commit delivery failure to '" + var2 + "' for request '" + var4 + "' could not be " + "dispatched since request is no longer available");
                  }

                  return;
               }

               Exception var3x = var4x instanceof Exception ? (Exception)var4x : new Exception(var4x);
               if (var3x instanceof UnreachableHostException) {
                  var1x.commitDeliveryFailureWhenContacting(var2, var3x);
               } else {
                  var1x.commitDeliveredTo(var2);
                  var1x.receivedCommitFailed(var2, var3x);
               }
            }

         }
      });
   }

   private void sendOutCancelMsg(final DeploymentServiceMessage var1, final String var2, final AdminRequestImpl var3) {
      final long var4 = var3.getId();
      WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
         public void run() {
            AdminRequestImpl var1x = CommonMessageSender.this.adminRequestManager.getRequest(var4);

            try {
               if (CommonMessageSender.this.getLocalServerName().equals(var2)) {
                  var3.cancelDeliveredTo(var2);
                  CommonMessageSender.this.delegate.sendMessageToAdminServer(var1);
               } else {
                  if (var1x != null) {
                     CommonMessageSender.this.adminRequestManager.addCancelDisconnectListener(var2, var1x);
                  }

                  CommonMessageSender.this.delegate.sendMessageToTargetServer(var1, var2);
                  var3.cancelDeliveredTo(var2);
               }
            } catch (Throwable var4x) {
               if (CommonMessageSender.isDebugEnabled()) {
                  CommonMessageSender.debug("send 'cancel' of id '" + var1.getDeploymentId() + "' to '" + var2 + "' failed due to '" + var4x.getMessage() + "'");
               }

               if (var1x == null) {
                  if (CommonMessageSender.isDebugEnabled()) {
                     CommonMessageSender.debug("cancel delivery failure to '" + var2 + "' for request '" + var4 + "' could not be " + "dispatched since request is no longer available");
                  }

                  return;
               }

               Exception var3x = var4x instanceof Exception ? (Exception)var4x : new Exception(var4x);
               if (var3x instanceof UnreachableHostException) {
                  var1x.cancelDeliveryFailureWhenContacting(var2, var3x);
               } else {
                  var1x.cancelDeliveredTo(var2);
                  var1x.receivedCancelFailed(var2, var3x);
               }
            }

         }
      });
   }

   private void sendOutHeartbeatMsg(final DeploymentServiceMessage var1, final String var2) {
      WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
         public void run() {
            try {
               if (CommonMessageSender.isDebugEnabled()) {
                  CommonMessageSender.debug("sending heartbeat to server '" + var2 + "', message -->" + var1);
               }

               CommonMessageSender.this.delegate.sendHeartbeatMessage(var1, var2);
            } catch (Throwable var2x) {
               if (CommonMessageSender.isDebugEnabled()) {
                  CommonMessageSender.debug("Failed to send heartbeat message to server '" + var2 + "' due to: " + var2x);
               }
            }

         }
      });
   }

   public final void sendPrepareAckMsg(long var1, boolean var3) throws RemoteException {
      if (isDebugEnabled()) {
         debug("sending 'prepare ack' for id '" + var1 + "'");
      }

      ArrayList var4 = new ArrayList();
      var4.add(var3);
      DeploymentServiceMessage var5 = new DeploymentServiceMessage(this.deploymentServiceVersion, (byte)6, var1, var4);

      try {
         this.delegate.sendMessageToAdminServer(var5);
      } catch (UnreachableHostException var7) {
         throw var7;
      } catch (Exception var8) {
         if (isDebugEnabled()) {
            debug("send 'prepare ack' of id '" + var1 + "' failed due to " + var8.getMessage() + "'");
         }

         throw new RemoteException("Error sending prepare ack", var8);
      }
   }

   public final void sendPrepareNakMsg(long var1, Throwable var3) throws RemoteException {
      if (isDebugEnabled()) {
         debug("sending 'prepare nak' for id '" + var1 + "' with reason '" + var3.getMessage() + "'");
      }

      ArrayList var4 = new ArrayList();
      var4.add(var3);
      DeploymentServiceMessage var5 = new DeploymentServiceMessage(this.deploymentServiceVersion, (byte)7, var1, var4);

      try {
         this.delegate.sendMessageToAdminServer(var5);
      } catch (UnreachableHostException var7) {
         throw var7;
      } catch (Exception var8) {
         if (isDebugEnabled()) {
            debug("send 'prepare nak' of id '" + var1 + "' failed due to '" + var8.getMessage() + "'");
         }
      }

   }

   public final DeploymentServiceMessage sendBlockingGetDeploymentsMsg(DomainVersion var1, String var2) throws Exception {
      ArrayList var3 = new ArrayList();
      DeploymentServiceMessage var4 = new DeploymentServiceMessage(this.deploymentServiceVersion, (byte)13, -1L, var3);
      var4.setFromVersion(var1);
      var4.setDeploymentType(var2);
      if (isDebugEnabled()) {
         debug("sending 'blocking get deployments' request to catch up from version '" + var1 + "' for deployment type '" + var2 + "'");
      }

      DeploymentServiceMessage var5 = this.delegate.sendBlockingMessageToAdminServer(var4);
      if (isDebugEnabled()) {
         debug("received 'blocking get deployments' response to catch up from version '" + var1 + "' for deployment type '" + var2 + "'");
      }

      return var5;
   }

   public final void sendGetDeploymentsMsg(final DomainVersion var1, final long var2) {
      WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
         public void run() {
            if (CommonMessageSender.isDebugEnabled()) {
               CommonMessageSender.debug("sending 'get deployments' request to catch up from version '" + var1 + "'");
            }

            ArrayList var1x = new ArrayList();
            DeploymentServiceMessage var2x = new DeploymentServiceMessage(CommonMessageSender.this.deploymentServiceVersion, (byte)4, var2, var1x);
            var2x.setFromVersion(var1);

            try {
               CommonMessageSender.this.delegate.sendMessageToAdminServer(var2x);
            } catch (Exception var4) {
               if (CommonMessageSender.isDebugEnabled()) {
                  CommonMessageSender.debug("send 'get deployments' request to catch up from version '" + var1 + "' failed due to '" + var4.getMessage() + "'");
               }
            }

         }
      });
   }

   public final void sendCommitSucceededMsg(long var1) {
      if (isDebugEnabled()) {
         debug("sending 'commit success' for id '" + var1 + "'");
      }

      ArrayList var3 = new ArrayList();
      DeploymentServiceMessage var4 = new DeploymentServiceMessage(this.deploymentServiceVersion, (byte)8, var1, var3);

      try {
         this.delegate.sendMessageToAdminServer(var4);
      } catch (Exception var6) {
         if (isDebugEnabled()) {
            debug("send 'commit success' of id '" + var1 + "' failed due to " + var6.getMessage() + "'");
         }
      }

   }

   public final void sendCommitFailedMsg(long var1, Throwable var3) throws RemoteException {
      if (isDebugEnabled()) {
         debug("sending 'commit failed' for id '" + var1 + "' with reason '" + var3.getMessage() + "'");
      }

      ArrayList var4 = new ArrayList();
      var4.add(var3);
      DeploymentServiceMessage var5 = new DeploymentServiceMessage(this.deploymentServiceVersion, (byte)9, var1, var4);

      try {
         this.delegate.sendMessageToAdminServer(var5);
      } catch (Exception var7) {
         if (isDebugEnabled()) {
            debug("send 'commit failed' of id '" + var1 + "' failed due to '" + var7.getMessage() + "'");
         }

         throw new RemoteException("Error sending commit failed", var7);
      }
   }

   public final void sendCancelSucceededMsg(long var1) {
      if (isDebugEnabled()) {
         debug("sending 'cancel success' for id '" + var1 + "'");
      }

      ArrayList var3 = new ArrayList();
      DeploymentServiceMessage var4 = new DeploymentServiceMessage(this.deploymentServiceVersion, (byte)10, var1, var3);

      try {
         this.delegate.sendMessageToAdminServer(var4);
      } catch (Exception var6) {
         if (isDebugEnabled()) {
            debug("send 'cancel success' of id '" + var1 + "' failed due to '" + var6.getMessage() + "'");
         }
      }

   }

   public final void sendCancelFailedMsg(long var1, Throwable var3) {
      if (isDebugEnabled()) {
         debug("sending 'cancel failed' for id '" + var1 + "' with reason '" + var3.getMessage() + "'");
      }

      ArrayList var4 = new ArrayList();
      var4.add(var3);
      DeploymentServiceMessage var5 = new DeploymentServiceMessage(this.deploymentServiceVersion, (byte)11, var1, var4);

      try {
         this.delegate.sendMessageToAdminServer(var5);
      } catch (Exception var7) {
         if (Debug.isServiceDebugEnabled()) {
            Debug.serviceDebug("send 'cancel failed' of id '" + var1 + "' failed due to '" + var7.getMessage() + "'");
         }
      }

   }

   public final void sendStatusMsg(String var1, Serializable var2) {
      if (Debug.isServiceStatusDebugEnabled()) {
         Debug.serviceStatusDebug("send 'status' '" + var2 + "' for channel '" + var1 + "'");
      }

      try {
         ArrayList var3 = new ArrayList();
         var3.add(var1);
         var3.add(var2);
         this.createAndSendStatusMessage(var3);
      } catch (Exception var4) {
         if (Debug.isServiceDebugEnabled()) {
            Debug.serviceDebug("send 'status' for channel '" + var1 + "' failed due to '" + var4.getMessage() + "'");
         }
      }

   }

   public final void sendStatusMsg(long var1, String var3, Serializable var4) {
      if (Debug.isServiceStatusDebugEnabled()) {
         Debug.serviceStatusDebug("send 'status' '" + var4 + "' for channel '" + var1 + "' and handler id '" + var3 + "'");
      }

      try {
         ArrayList var5 = new ArrayList();
         var5.add(var3);
         var5.add(var4);
         var5.add(new Long(var1));
         this.createAndSendStatusMessage(var5);
      } catch (Exception var6) {
         if (Debug.isServiceDebugEnabled()) {
            Debug.serviceDebug("send 'status' for channel '" + var1 + "' failed due to '" + var6.getMessage() + "'");
         }
      }

   }

   private void createAndSendStatusMessage(ArrayList var1) throws Exception {
      DeploymentServiceMessage var2 = new DeploymentServiceMessage(this.deploymentServiceVersion, (byte)12, -1L, var1);
      this.delegate.sendMessageToAdminServer(var2);
   }

   Set putHandlers(String var1, Set var2) {
      return (Set)this.serverToHandlers.put(var1, Collections.unmodifiableSet(var2));
   }

   Set getHandlers(String var1) {
      return (Set)this.serverToHandlers.get(var1);
   }

   // $FF: synthetic method
   CommonMessageSender(Object var1) {
      this();
   }

   static final class Maker {
      static final CommonMessageSender SINGLETON = new CommonMessageSender();
   }
}
