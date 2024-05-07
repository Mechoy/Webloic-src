package weblogic.nodemanager.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.Channel;
import java.nio.channels.ServerSocketChannel;
import weblogic.nodemanager.NodeManagerTextTextFormatter;

class Listener {
   protected NMServer server;
   protected InetAddress host;
   protected int port;
   protected int backlog;
   protected ServerSocket serverSocket;
   protected Channel inheritedChannel;
   private static final NodeManagerTextTextFormatter nmText = NodeManagerTextTextFormatter.getInstance();

   Listener(NMServer var1, Channel var2) throws IOException {
      this.server = var1;
      NMServerConfig var3 = var1.getConfig();
      String var4 = var3.getListenAddress();
      if (var4 != null) {
         this.host = InetAddress.getByName(var4);
      }

      this.port = var3.getListenPort();
      this.backlog = var3.getListenBacklog();
      this.inheritedChannel = var2;
      if (var2 != null) {
         this.inheritedChannel = var2;

         assert this.inheritedChannel instanceof ServerSocketChannel;

         ServerSocketChannel var5 = (ServerSocketChannel)this.inheritedChannel;
         this.serverSocket = var5.socket();
         NMServer.nmLog.info(nmText.getInheritedSocket(this.serverSocket.toString()));
      }

   }

   public void init() throws IOException {
      if (this.inheritedChannel == null) {
         if (this.host != null) {
            this.serverSocket = new ServerSocket(this.port, this.backlog, this.host);
         } else {
            this.serverSocket = new ServerSocket(this.port, this.backlog);
         }
      }

   }

   public void run() throws IOException {
      NMServer.nmLog.info(this.host != null ? nmText.getPlainListenerStartedHost(Integer.toString(this.port), this.host.getHostName()) : nmText.getPlainListenerStarted(Integer.toString(this.port)));

      while(true) {
         while(true) {
            try {
               Socket var1;
               if ((var1 = this.serverSocket.accept()) != null) {
                  Handler var2 = new Handler(this.server, var1);
                  Thread var3 = new Thread(var2);
                  var3.start();
               }
            } catch (IOException var4) {
               NMServer.nmLog.warning(nmText.getFailedConnection(Integer.toString(this.port), this.host.getHostName()) + var4);
            }
         }
      }
   }
}
