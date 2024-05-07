package weblogic.nodemanager.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import weblogic.security.SSL.SSLSocketFactory;

class SSLClient extends NMServerClient {
   public static final int LISTEN_PORT = 5556;

   SSLClient() {
      this.port = 5556;
   }

   protected Socket createSocket(String var1, int var2, int var3) throws IOException {
      SSLSocketFactory var4 = (SSLSocketFactory)SSLSocketFactory.getDefault();
      Socket var5 = new Socket();
      InetAddress var6 = null;
      if (var1 == null) {
         var6 = InetAddress.getLocalHost();
      } else {
         var6 = InetAddress.getByName(var1);
      }

      var5.connect(new InetSocketAddress(var6, var2), var3);
      return var4.createSocket(var5, var6.getHostAddress(), var2, true);
   }
}
