package weblogic.cluster;

import java.io.IOException;
import java.security.AccessController;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;
import weblogic.work.WorkManagerFactory;

public final class OutboundService extends AbstractServerService {
   private ClusterMBean clusterMBean;
   private boolean serverVisibleToCluster;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public void start() throws ServiceFailureException {
      this.clusterMBean = ManagementService.getRuntimeAccess(kernelId).getServer().getCluster();
      if (this.clusterMBean != null) {
         try {
            MulticastManager.theOne().resumeNonAdminMulticastSessions();
            AttributeManager.theOne().sendAttributes();
         } catch (IOException var2) {
            throw new ServiceFailureException("Unexpected exception sending attributes", var2);
         }

         this.sendServerRuntimeState();
         AnnouncementManager.theOne().unblockAnnouncements();
         if ("multicast".equals(this.clusterMBean.getClusterMessagingMode())) {
            ClusterLogger.logJoinedCluster(this.clusterMBean.getName(), this.clusterMBean.getMulticastAddress(), this.clusterMBean.getMulticastPort() + "");
         }

         this.serverVisibleToCluster = true;
      }

   }

   public void stop() throws ServiceFailureException {
      this.shutdownInternal(false);
   }

   public synchronized void halt() throws ServiceFailureException {
      this.shutdownInternal(true);
   }

   public synchronized void shutdownInternal(boolean var1) throws ServiceFailureException {
      if (this.clusterMBean != null && this.serverVisibleToCluster) {
         ClusterLogger.logOutboundClusterServiceStopped();
         if (var1) {
            WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
               public void run() {
                  if (OutboundService.this.isShuttingDown()) {
                     MulticastManager.theOne().stopHeartbeat();
                  } else {
                     try {
                        MulticastManager.theOne().suspendNonAdminMulticastSessions();
                        OutboundService.this.sendServerRuntimeState();
                     } catch (ServiceFailureException var2) {
                     }
                  }

               }
            });
         } else if (this.isShuttingDown()) {
            MulticastManager.theOne().stopHeartbeat();
         } else {
            MulticastManager.theOne().suspendNonAdminMulticastSessions();
            this.sendServerRuntimeState();
         }

         AnnouncementManager.theOne().blockAnnouncements();
         this.serverVisibleToCluster = false;
      }

   }

   private void sendServerRuntimeState() throws ServiceFailureException {
      if (this.clusterMBean != null) {
         try {
            MemberManager.theOne().sendMemberRuntimeState();
         } catch (IOException var2) {
            throw new ServiceFailureException("Unexpected exception sending  runtime state: ", var2);
         }
      }

   }

   private boolean isShuttingDown() {
      int var1 = ManagementService.getRuntimeAccess(kernelId).getServerRuntime().getStableState();
      return var1 == 9;
   }
}
