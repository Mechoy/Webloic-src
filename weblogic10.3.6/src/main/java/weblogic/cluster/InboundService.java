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

public class InboundService extends AbstractServerService {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static boolean started;

   public void start() throws ServiceFailureException {
      startListening();
      ClusterMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServer().getCluster();
      if (var1 != null) {
         MemberManager.theOne().waitToSyncWithCurrentMembers();
         MulticastManager.theOne().resume();
      }

   }

   public static synchronized void startListening() throws ServiceFailureException {
      if (!started) {
         ClusterMBean var0 = ManagementService.getRuntimeAccess(kernelId).getServer().getCluster();
         if (var0 != null) {
            try {
               MulticastManager.theOne().startListening();
            } catch (IOException var2) {
               ClusterLogger.logFailedToJoinClusterError(var0.getName(), var0.getMulticastAddress(), var2);
               throw new ServiceFailureException("Failed to listen on multicast address", var2);
            }

            if ("unicast".equals(var0.getClusterMessagingMode())) {
               ClusterLogger.logUnicastEnabled();
            } else {
               ClusterLogger.logListeningToCluster(var0.getName(), var0.getMulticastAddress(), var0.getMulticastPort() + "");
            }

            MulticastManager.theOne().startHeartbeat();
            started = true;
         }

      }
   }

   public void stop() {
      this.shutdownInternal(false);
   }

   public synchronized void halt() {
      this.shutdownInternal(true);
   }

   private synchronized void shutdownInternal(boolean var1) {
      if (started) {
         if (var1) {
            WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
               public void run() {
                  MulticastManager.theOne().stopHeartbeat();
               }
            });
         } else {
            MulticastManager.theOne().stopHeartbeat();
         }
      }

   }
}
