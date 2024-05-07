package weblogic.webservice.client;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

/** @deprecated */
public interface SSLAdapter {
   Socket createSocket(String var1, int var2) throws IOException;

   URLConnection openConnection(URL var1) throws IOException;
}
