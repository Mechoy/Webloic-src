package weblogic.wsee.connection.transport.https;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import weblogic.wsee.connection.transport.TransportInfo;

public interface SSLAdapter {
   HttpURLConnection openConnection(URL var1, Proxy var2, TransportInfo var3) throws IOException;

   void setClientCert(String var1, char[] var2) throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableEntryException;

   void setKeystore(String var1, char[] var2, String var3);
}
