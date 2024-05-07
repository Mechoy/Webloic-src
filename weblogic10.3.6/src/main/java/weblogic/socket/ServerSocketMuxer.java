package weblogic.socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.ServerSocketChannel;
import weblogic.socket.utils.SDPSocketUtils;

public abstract class ServerSocketMuxer extends SocketMuxer {
   protected ServerSocketMuxer() throws IOException {
   }

   public static ServerSocketMuxer getMuxer() {
      return (ServerSocketMuxer)SocketMuxer.SingletonMaker.singleton;
   }

   public ServerSocket newServerSocket(InetAddress var1, int var2, int var3, boolean var4) throws IOException {
      ServerSocket var5 = null;
      if (enableSocketChannels && !var4) {
         var5 = ServerSocketChannel.open().socket();
         var5.getChannel().configureBlocking(true);
      } else {
         var5 = new ServerSocket();
      }

      if (var1 == null) {
         var5.bind(new InetSocketAddress(var2), var3);
      } else {
         var5.bind(new InetSocketAddress(var1, var2), var3);
      }

      return new WeblogicServerSocket(var5, false);
   }

   public ServerSocket newSDPServerSocket(InetAddress var1, int var2, int var3, boolean var4) throws IOException {
      ServerSocket var5 = null;
      if (enableSocketChannels) {
         throw new AssertionError("SDP server sockets not supported with socket channels");
      } else {
         var5 = SDPSocketUtils.createSDPServerSocket();
         if (var1 == null) {
            var5.bind(new InetSocketAddress(var2), var3);
         } else {
            var5.bind(new InetSocketAddress(var1, var2), var3);
         }

         return new WeblogicServerSocket(var5, false);
      }
   }
}
