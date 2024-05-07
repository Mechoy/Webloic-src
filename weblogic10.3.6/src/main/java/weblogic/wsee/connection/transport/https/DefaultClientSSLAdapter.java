package weblogic.wsee.connection.transport.https;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import weblogic.wsee.connection.transport.TransportInfo;

public class DefaultClientSSLAdapter implements SSLAdapter {
   public HttpURLConnection openConnection(URL var1, Proxy var2, TransportInfo var3) throws IOException {
      HTTPSClientTransport var4 = new HTTPSClientTransport();
      return var4.openConnection(var1, var2, var3);
   }

   public void setClientCert(String var1, char[] var2) throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableEntryException {
      throw new UnsupportedOperationException("Not Implemented for JDKSSLAdapter");
   }

   public void setKeystore(String var1, char[] var2, String var3) {
      throw new UnsupportedOperationException("Not Implemented for JDKSSLAdapter");
   }
}
