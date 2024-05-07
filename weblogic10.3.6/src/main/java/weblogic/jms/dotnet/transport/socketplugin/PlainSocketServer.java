package weblogic.jms.dotnet.transport.socketplugin;

import java.net.ServerSocket;
import java.net.Socket;
import weblogic.jms.dotnet.transport.MarshalReadableFactory;
import weblogic.jms.dotnet.transport.TransportThreadPool;

class PlainSocketServer implements Runnable {
   private final int port;
   private final ServerSocket serverSocket;
   private final MarshalReadableFactory marshalReadableFactory;
   private final Stats stats = new Stats("Server");
   private final TransportThreadPool pool;

   PlainSocketServer(int var1, MarshalReadableFactory var2, TransportThreadPool var3) throws Exception {
      this.port = var1;
      this.marshalReadableFactory = var2;
      this.pool = var3;
      this.serverSocket = new ServerSocket(var1);
   }

   public void run() {
      try {
         while(true) {
            Socket var1 = this.serverSocket.accept();
            PlainSocket var2 = new PlainSocket(this.stats, this.marshalReadableFactory, this.pool);
            var2.start(var1);
         }
      } catch (Throwable var3) {
         var3.printStackTrace();
      }
   }
}
