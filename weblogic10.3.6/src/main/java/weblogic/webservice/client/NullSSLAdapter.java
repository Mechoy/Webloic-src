package weblogic.webservice.client;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

/** @deprecated */
public class NullSSLAdapter implements SSLAdapter {
   public Socket createSocket(String var1, int var2) throws IOException {
      throw new IOException("https unsupported: SSL implementation not available or not configured correctly.");
   }

   public URLConnection openConnection(URL var1) throws IOException {
      return var1.openConnection();
   }
}
