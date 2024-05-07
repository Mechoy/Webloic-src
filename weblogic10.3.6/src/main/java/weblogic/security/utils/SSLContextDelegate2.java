package weblogic.security.utils;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;
import weblogic.security.SSL.WeblogicSSLEngine;

public interface SSLContextDelegate2 extends SSLContextDelegate {
   void enableUnencryptedNullCipher(boolean var1);

   boolean isUnencryptedNullCipherEnabled();

   /** @deprecated */
   SSLServerSocketFactory getSSLNioServerSocketFactory();

   /** @deprecated */
   SSLSocketFactory getSSLNioSocketFactory();

   String[] getDefaultCipherSuites();

   String[] getSupportedCipherSuites();

   String[] getDefaultProtocols();

   String[] getSupportedProtocols();

   WeblogicSSLEngine createSSLEngine() throws SSLException;

   WeblogicSSLEngine createSSLEngine(String var1, int var2) throws SSLException;
}
