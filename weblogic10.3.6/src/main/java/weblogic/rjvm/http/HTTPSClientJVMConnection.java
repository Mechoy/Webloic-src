package weblogic.rjvm.http;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.X509Certificate;
import weblogic.net.http.HttpsURLConnection;
import weblogic.protocol.ServerChannel;
import weblogic.socket.ChannelSSLSocketFactory;

public final class HTTPSClientJVMConnection extends HTTPClientJVMConnection {
   private ChannelSSLSocketFactory factory;

   HTTPSClientJVMConnection(ServerChannel var1) throws IOException {
      super(var1);
      this.factory = new ChannelSSLSocketFactory(var1);
      this.factory.initializeFromThread();
   }

   URLConnection createURLConnection(URL var1) {
      HttpsURLConnection var2 = new HttpsURLConnection(var1, this.factory.getSSLClientInfo());
      var2.setSSLSocketFactory(this.factory);
      var2.u11();
      return var2;
   }

   public X509Certificate[] getPeerCertChain() {
      return new X509Certificate[0];
   }
}
