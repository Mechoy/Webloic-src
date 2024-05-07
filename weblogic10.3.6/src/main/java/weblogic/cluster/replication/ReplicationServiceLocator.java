package weblogic.cluster.replication;

import java.security.AccessController;
import javax.naming.NamingException;
import weblogic.cluster.ClusterService;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.Protocol;
import weblogic.protocol.ProtocolManager;
import weblogic.protocol.ServerChannel;
import weblogic.protocol.ServerChannelManager;
import weblogic.protocol.ServerIdentity;
import weblogic.protocol.URLManager;
import weblogic.security.SSL.SSLClientInfo;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.acl.internal.Security;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.utils.SSLContextManager;

public class ReplicationServiceLocator {
   private boolean isReplicationSecured = false;
   private String replicationChannel;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final String REPLICATION_SECURED_PROP = "weblogic.replication.secured";

   public ReplicationServiceLocator() {
      ClusterMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServer().getCluster();
      if (var1 != null) {
         this.isReplicationSecured = var1.isSecureReplicationEnabled();
         this.replicationChannel = var1.getReplicationChannel();
      }

      if (System.getProperty("weblogic.replication.secured") != null) {
         this.isReplicationSecured = Boolean.getBoolean("weblogic.replication.secured");
      }

   }

   ReplicationServicesInternal replicationServicesLookup(ServerIdentity var1, Class var2) throws NamingException {
      return this.replicationServicesLookup(var1, this.replicationChannel, var2);
   }

   ReplicationServicesInternal replicationServicesLookup(ServerIdentity var1, String var2, Class var3) throws NamingException {
      int var4 = -1;
      if (ClusterService.getClusterService().isReplicationTimeoutEnabled()) {
         var4 = ClusterService.getClusterService().getHeartbeatTimeoutMillis();
      }

      return this.getReplicationServiceInternal(var1, var2, var3, var4);
   }

   private ServerChannel getOutBoundChannel(String var1, boolean var2) {
      ServerChannel var3 = ServerChannelManager.findOutboundServerChannel(var1);
      if (var3 == null) {
         Protocol var4 = ProtocolManager.getDefaultProtocol();
         Protocol var5 = ProtocolManager.getDefaultSecureProtocol();
         if (var2) {
            var4 = ProtocolManager.getDefaultSecureProtocol();
            var5 = ProtocolManager.getDefaultProtocol();
         }

         var3 = ServerChannelManager.findLocalServerChannel(var4);
         if (var3 == null) {
            var3 = ServerChannelManager.findLocalServerChannel(var5);
         }
      }

      return var3;
   }

   private SSLClientInfo getOutChannelSSLClientInfo(ServerChannel var1) {
      SSLClientInfo var2 = null;
      if (var1 != null) {
         try {
            var2 = SSLContextManager.getChannelSSLClientInfo(var1, kernelId);
         } catch (Exception var4) {
            if (ReplicationDebugLogger.isDebugEnabled()) {
               ReplicationDebugLogger.debug((String)("Failed to load Channel Certificates. Exception: " + var4), (Throwable)var4);
            }
         }
      }

      return var2;
   }

   private ReplicationServicesInternal getReplicationServiceInternal(ServerIdentity var1, String var2, Class var3, int var4) throws NamingException {
      SSLClientInfo var5 = null;
      SSLClientInfo var6 = null;
      boolean var7 = false;

      ReplicationServicesInternal var10;
      try {
         String var8 = URLManager.findURL(var1, var2, this.isReplicationSecured);
         ServerChannel var9 = this.getOutBoundChannel(var2, this.isReplicationSecured);
         if (var9 != null) {
            var7 = var9.getProtocol().isSecure();
            if (var7) {
               var5 = Security.getThreadSSLClientInfo();
               var6 = this.getOutChannelSSLClientInfo(var9);
               if (var6 != null) {
                  Security.setThreadSSLClientInfo(var6);
               }
            }

            var10 = SecureReplicationInvocationHandler.lookupService(var8, var9.getChannelName(), var4, var3, var7);
            return var10;
         }

         var10 = null;
      } finally {
         if (var7 && var6 != null) {
            Security.setThreadSSLClientInfo(var5);
         }

      }

      return var10;
   }
}
