package weblogic.cluster.messaging.internal.server;

import java.io.IOException;
import java.net.Socket;
import java.security.AccessController;
import javax.net.ssl.SSLSocket;
import weblogic.cluster.messaging.internal.AbstractConnectionManager;
import weblogic.cluster.messaging.internal.Connection;
import weblogic.cluster.messaging.internal.ConnectionImpl;
import weblogic.cluster.messaging.internal.Environment;
import weblogic.cluster.messaging.internal.ServerConfigurationInformation;
import weblogic.cluster.messaging.protocol.MuxableSocketClusterBroadcast;
import weblogic.cluster.messaging.protocol.MuxableSocketClusterBroadcastS;
import weblogic.cluster.messaging.protocol.ProtocolHandlerClusterBroadcast;
import weblogic.cluster.messaging.protocol.ProtocolHandlerClusterBroadcastS;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.ServerChannel;
import weblogic.protocol.ServerChannelManager;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class ConnectionManagerImpl extends AbstractConnectionManager {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final boolean DEBUG;

   public static ConnectionManagerImpl getInstance() {
      return ConnectionManagerImpl.Factory.THE_ONE;
   }

   private ConnectionManagerImpl() {
   }

   public Connection createConnection(ServerConfigurationInformation var1) throws IOException {
      ServerMBean var2 = ManagementService.getRuntimeAccess(kernelId).getServer();
      String var3 = var2.getCluster().getClusterBroadcastChannel();
      if (var3 != null && var3.trim().length() == 0) {
         var3 = null;
      }

      ServerChannel var4;
      if (var3 != null) {
         var4 = ServerChannelManager.findOutboundServerChannel(var3);
      } else if (!var1.isUsingSSL()) {
         var4 = ServerChannelManager.findOutboundServerChannel(ProtocolHandlerClusterBroadcast.PROTOCOL_CLUSTER);
      } else {
         var4 = ServerChannelManager.findOutboundServerChannel(ProtocolHandlerClusterBroadcastS.PROTOCOL_CLUSTER_SECURE);
      }

      if (DEBUG) {
         this.debug("trying to create connection using outbound channel " + var4);
      }

      if (var4 == null) {
         throw new IOException("Channel not yet bound!");
      } else {
         Object var5;
         MuxableSocketClusterBroadcast var6;
         if (var4.supportsTLS()) {
            var5 = new SSLConnectionImpl(var1);
            var6 = MuxableSocketClusterBroadcastS.createConnection(var1.getAddress(), var1.getPort(), (Connection)var5, var4);
         } else {
            var5 = new ConnectionImpl(var1);
            var6 = MuxableSocketClusterBroadcast.createConnection(var1.getAddress(), var1.getPort(), (Connection)var5, var4);
         }

         ((ConnectionImpl)var5).setSocket(var6.getSocket());
         return (Connection)var5;
      }
   }

   private void debug(String var1) {
      Environment.getLogService().debug("[ConnectionManager] " + var1);
   }

   public Connection createConnection(Socket var1) throws IOException {
      return (Connection)(var1 instanceof SSLSocket ? new SSLConnectionImpl(var1) : super.createConnection(var1));
   }

   // $FF: synthetic method
   ConnectionManagerImpl(Object var1) {
      this();
   }

   static {
      DEBUG = Environment.DEBUG;
   }

   private static final class Factory {
      static final ConnectionManagerImpl THE_ONE = new ConnectionManagerImpl();
   }
}
