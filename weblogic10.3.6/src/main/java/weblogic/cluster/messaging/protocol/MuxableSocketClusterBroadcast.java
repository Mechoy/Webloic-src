package weblogic.cluster.messaging.protocol;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.AccessController;
import weblogic.cluster.messaging.internal.Connection;
import weblogic.cluster.messaging.internal.Environment;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.ServerChannel;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.socket.AbstractMuxableSocket;
import weblogic.socket.SocketMuxer;
import weblogic.utils.io.Chunk;
import weblogic.utils.io.ChunkedInputStream;

public class MuxableSocketClusterBroadcast extends AbstractMuxableSocket {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final int DEFAULT_CONNECT_TIMEOUT = 10000;
   protected static final boolean DEBUG;
   private static final int PROTOCOL_LENGTH = 17;
   private static final int MESSAGE_LENGTH_SIZE = 4;
   private Connection connection;
   private static boolean enabled;

   MuxableSocketClusterBroadcast(Chunk var1, Socket var2, ServerChannel var3) throws IOException {
      super(var1, var2, var3);
      this.ensureEnabled(this.getSocket().getLocalAddress(), this.getSocket().getPort());
      this.connection = Environment.getConnectionManager().createConnection(var2);
      if (DEBUG) {
         this.debug("created connection " + this.connection);
      }

   }

   MuxableSocketClusterBroadcast(InetAddress var1, int var2, ServerChannel var3, Connection var4) throws IOException {
      super(var3);
      if (var3 != null && var3.getConnectTimeout() != 0) {
         this.connect(var1, var2);
      } else {
         this.connect(var1, var2, 10000);
      }

      this.ensureEnabled(this.getSocket().getLocalAddress(), this.getSocket().getPort());
      this.connection = var4;
   }

   public static MuxableSocketClusterBroadcast createConnection(InetAddress var0, int var1, Connection var2, ServerChannel var3) throws IOException {
      MuxableSocketClusterBroadcast var4 = new MuxableSocketClusterBroadcast(var0, var1, var3, var2);
      SocketMuxer.getMuxer().register(var4);
      SocketMuxer.getMuxer().read(var4);
      return var4;
   }

   protected int getHeaderLength() {
      return this.getProtocolLength() + 4;
   }

   protected int getMessageLength() {
      int var1 = this.getProtocolLength();
      int var2 = this.getHeaderByte(var1) & 255;
      if (DEBUG) {
         this.debug("r0=" + var2);
      }

      int var3 = this.getHeaderByte(var1 + 1) & 255;
      if (DEBUG) {
         this.debug("r1=" + var3);
      }

      int var4 = this.getHeaderByte(var1 + 2) & 255;
      if (DEBUG) {
         this.debug("r2=" + var4);
      }

      int var5 = this.getHeaderByte(var1 + 3) & 255;
      if (DEBUG) {
         this.debug("r3=" + var5);
      }

      if (DEBUG) {
         this.debug("length=" + (var2 << 24 | var3 << 16 | var4 << 8 | var5));
      }

      return var2 << 24 | var3 << 16 | var4 << 8 | var5;
   }

   protected int getProtocolLength() {
      return 17;
   }

   public void dispatch(Chunk var1) {
      try {
         if (DEBUG) {
            this.debug("dispatching with data [" + var1 + "]");
         }

         this.connection.handleIncomingMessage(new ChunkedInputStream(var1, this.getHeaderLength()));
      } catch (IOException var3) {
         SocketMuxer.getMuxer().deliverHasException(this.getSocketFilter(), var3);
      }

   }

   protected void debug(String var1) {
      Environment.getLogService().debug("[UnicastMuxableSocket] " + var1);
   }

   public void hasException(Throwable var1) {
      this.connection.close();
      this.close();
   }

   public void endOfStream() {
      this.connection.close();
      this.close();
   }

   public boolean timeout() {
      this.connection.close();
      this.close();
      return true;
   }

   public int getIdleTimeoutMillis() {
      return 0;
   }

   public static boolean isEnabled() {
      ServerMBean var0 = ManagementService.getRuntimeAccess(kernelId).getServer();
      if (var0 != null && var0.getCluster() != null) {
         return "unicast".equals(var0.getCluster().getClusterMessagingMode()) && Environment.isInitialized();
      } else {
         return false;
      }
   }

   private void ensureEnabled(InetAddress var1, int var2) throws IOException {
      if (!enabled) {
         Socket var3 = this.getSocket();
         if (var3 != null) {
            var3.close();
         }

         throw new IOException("An attempt to connect via CLUSTER-BROADCAST to: '" + var1 + "', on port: '" + var2 + "' was rejected because CLUSTER-BROADCAST is " + "not enabled.");
      }
   }

   static {
      DEBUG = Environment.DEBUG;
      enabled = true;
   }
}
