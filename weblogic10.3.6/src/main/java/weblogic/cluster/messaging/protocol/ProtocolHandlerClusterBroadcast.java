package weblogic.cluster.messaging.protocol;

import java.io.IOException;
import java.net.ProtocolException;
import java.net.Socket;
import weblogic.cluster.messaging.internal.Environment;
import weblogic.cluster.messaging.internal.LogService;
import weblogic.protocol.Protocol;
import weblogic.protocol.ProtocolHandler;
import weblogic.protocol.ProtocolManager;
import weblogic.protocol.ServerChannel;
import weblogic.server.channels.ServerChannelImpl;
import weblogic.socket.MuxableSocket;
import weblogic.socket.SocketLogger;
import weblogic.utils.io.Chunk;

public class ProtocolHandlerClusterBroadcast implements ProtocolHandler {
   private static final String PROTOCOL_NAME = "CLUSTER-BROADCAST";
   private static final ProtocolHandler theOne = new ProtocolHandlerClusterBroadcast();
   public static final Protocol PROTOCOL_CLUSTER = ProtocolManager.createProtocol((byte)12, "CLUSTER-BROADCAST", "CLUSTER-BROADCAST", false, getProtocolHandler());
   private static final boolean DEBUG;

   public static ProtocolHandler getProtocolHandler() {
      return theOne;
   }

   public ServerChannel getDefaultServerChannel() {
      return ProtocolHandlerClusterBroadcast.ChannelInitializer.CHANNEL;
   }

   public int getHeaderLength() {
      return "CLUSTER-BROADCAST".length();
   }

   public int getPriority() {
      return 1;
   }

   public Protocol getProtocol() {
      return PROTOCOL_CLUSTER;
   }

   public MuxableSocket createSocket(Chunk var1, Socket var2, ServerChannel var3) throws IOException {
      if (!MuxableSocketClusterBroadcast.isEnabled()) {
         SocketLogger.logConnectionRejectedProtocol(var3.getChannelName(), var3.getConfiguredProtocol());
         throw new ProtocolException("unicast cluster messaging is disabled");
      } else {
         MuxableSocketClusterBroadcast var4 = new MuxableSocketClusterBroadcast(var1, var2, var3);
         return var4;
      }
   }

   public boolean claimSocket(Chunk var1) {
      return this.claimSocket(var1, "CLUSTER-BROADCAST");
   }

   protected boolean claimSocket(Chunk var1, String var2) {
      int var3 = var2.length();
      if (var1.end < var3) {
         return false;
      } else {
         byte[] var4 = var1.buf;

         for(int var5 = 0; var5 < var3; ++var5) {
            if (var4[var5] != var2.charAt(var5)) {
               return false;
            }
         }

         return true;
      }
   }

   protected void debug(String var1) {
      LogService var2 = Environment.getLogService();
      if (var2 != null) {
         var2.debug("[UnicastProtocolHandler] " + var1);
      }

   }

   static {
      DEBUG = Environment.DEBUG;
   }

   private static final class ChannelInitializer {
      private static final ServerChannel CHANNEL;

      static {
         CHANNEL = ServerChannelImpl.createDefaultServerChannel(ProtocolHandlerClusterBroadcast.PROTOCOL_CLUSTER);
      }
   }
}
