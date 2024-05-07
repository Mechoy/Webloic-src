package weblogic.cluster.messaging.protocol;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocket;
import weblogic.cluster.messaging.internal.Connection;
import weblogic.cluster.messaging.internal.Environment;
import weblogic.protocol.ServerChannel;
import weblogic.security.utils.SSLIOContext;
import weblogic.security.utils.SSLIOContextTable;
import weblogic.socket.JSSESocket;
import weblogic.socket.SSLFilter;
import weblogic.socket.utils.JSSEUtils;
import weblogic.utils.io.Chunk;

public final class MuxableSocketClusterBroadcastS extends MuxableSocketClusterBroadcast {
   private static final int PROTOCOL_LENGTH = "CLUSTER-BROADCAST-SECURE".length();

   public MuxableSocketClusterBroadcastS(Chunk var1, Socket var2, ServerChannel var3) throws IOException {
      super(var1, var2, var3);
   }

   public MuxableSocketClusterBroadcastS(InetAddress var1, int var2, ServerChannel var3, Connection var4) throws IOException {
      super(var1, var2, var3, var4);
   }

   public static MuxableSocketClusterBroadcast createConnection(InetAddress var0, int var1, Connection var2, ServerChannel var3) throws IOException {
      MuxableSocketClusterBroadcastS var4 = new MuxableSocketClusterBroadcastS(var0, var1, var3, var2);
      SSLSocket var5 = (SSLSocket)var4.getSocket();
      JSSESocket var6 = JSSEUtils.getJSSESocket(var5);
      if (var6 != null) {
         JSSEUtils.registerJSSEFilter(var6, var4);
         JSSEUtils.activate(var6, var4);
      } else {
         SSLIOContext var7 = SSLIOContextTable.findContext(var5);
         SSLFilter var8 = (SSLFilter)var7.getFilter();
         var4.setSocketFilter(var8);

         try {
            var5.startHandshake();
         } catch (SSLException var12) {
            if (!var5.isClosed()) {
               try {
                  var5.close();
               } catch (IOException var11) {
               }
            }

            throw var12;
         }

         var8.setDelegate(var4);
         if (DEBUG) {
            var4.debug("SSL socket initialized!");
         }

         var8.activate();
      }

      return var4;
   }

   protected int getProtocolLength() {
      return PROTOCOL_LENGTH;
   }

   protected void debug(String var1) {
      Environment.getLogService().debug("[UnicastMuxableSocketSecure] " + var1);
   }
}
