package weblogic.nodemanager.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

class PlainClient extends NMServerClient {
   public static final int LISTEN_PORT = 5556;

   public PlainClient() {
      this.port = 5556;
   }

   protected Socket createSocket(String var1, int var2, int var3) throws IOException {
      Socket var4 = new Socket();
      InetAddress var5 = null;
      if (var1 == null) {
         var5 = InetAddress.getLocalHost();
      } else {
         var5 = InetAddress.getByName(var1);
      }

      var4.connect(new InetSocketAddress(var5, var2), var3);
      return var4;
   }
}
