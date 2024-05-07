package weblogic.wsee.connection.transport.https;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLStreamHandler;
import java.security.GeneralSecurityException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableEntryException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import weblogic.wsee.connection.transport.TransportInfo;
import weblogic.wsee.util.Verbose;

public class JdkSSLAdapter implements SSLAdapter {
   private static boolean verbose = Verbose.isVerbose(JdkSSLAdapter.class);
   private static final String SUN_JDK_PROTOCOL_HANDLER = "sun.net.www.protocol.https.Handler";
   private static final String IBM_JDK_PROTOCOL_HANDLER = "com.ibm.net.ssl.www2.protocol.https.Handler";
   private static Class PROTOCOL_HANDLER_CLASS = getProtocolHandlerClass();

   public HttpsURLConnection openConnection(URL var1, Proxy var2, TransportInfo var3) throws IOException {
      HttpsTransportInfo var4 = var3 != null && var3 instanceof HttpsTransportInfo ? (HttpsTransportInfo)var3 : HttpsTransportInfo.DEFAULT_TRANSPORTINFO;
      if (verbose) {
         Verbose.log((Object)("JdkSSLAdapter: opening connection to " + var1 + (var2 != null ? " via " + var2 : "")));
      }

      var1 = this.prepareURL(var1);
      HttpsURLConnection var5;
      if (var2 != null) {
         var5 = (HttpsURLConnection)var1.openConnection(var2);
      } else {
         var5 = (HttpsURLConnection)var1.openConnection();
      }

      if (verbose) {
         Verbose.say("JdkSSLAdapter.openConnection() got " + var5.getClass().getName());
      }

      this.prepareConnection(var5, var4);
      return var5;
   }

   public void setClientCert(String var1, char[] var2) throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableEntryException {
      throw new UnsupportedOperationException("Not Implemented for JDKSSLAdapter");
   }

   public void setKeystore(String var1, char[] var2, String var3) {
      throw new UnsupportedOperationException("Not Implemented for JDKSSLAdapter");
   }

   private URL prepareURL(URL var1) throws MalformedURLException, IOException {
      return new URL(var1, var1.toString(), this.getProtocolHandler());
   }

   private URLStreamHandler getProtocolHandler() throws IOException {
      try {
         if (PROTOCOL_HANDLER_CLASS != null) {
            return (URLStreamHandler)PROTOCOL_HANDLER_CLASS.newInstance();
         }
      } catch (Exception var2) {
         throw new IOException("Unable to instantiate URLStreamHandler class", var2);
      }

      throw new IOException("Unable to instantiate URLStreamHandler: class not found");
   }

   private static Class getProtocolHandlerClass() {
      Class var0 = null;

      try {
         var0 = Class.forName("sun.net.www.protocol.https.Handler");
      } catch (ClassNotFoundException var4) {
         try {
            var0 = Class.forName("com.ibm.net.ssl.www2.protocol.https.Handler");
         } catch (ClassNotFoundException var3) {
            var0 = null;
         }
      }

      return var0;
   }

   private void prepareConnection(HttpsURLConnection var1, HttpsTransportInfo var2) throws IOException {
      HostnameVerifier var3 = var2.getHostnameVerifier();
      if (var3 != null) {
         var1.setHostnameVerifier(var3);
      }

      try {
         SSLContext var4 = SSLContext.getInstance("SSL");
         var4.init(var2.getKeyManagers(), var2.getTrustManagers(), new SecureRandom());
         SSLSocketFactory var5 = var4.getSocketFactory();
         var1.setSSLSocketFactory(var5);
      } catch (GeneralSecurityException var6) {
         throw new IOException(var6.getMessage());
      }
   }
}
