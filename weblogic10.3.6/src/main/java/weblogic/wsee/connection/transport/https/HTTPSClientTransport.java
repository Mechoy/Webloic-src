package weblogic.wsee.connection.transport.https;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import weblogic.wsee.connection.transport.TransportInfo;
import weblogic.wsee.connection.transport.http.HTTPClientTransport;
import weblogic.wsee.util.Verbose;

public class HTTPSClientTransport extends HTTPClientTransport {
   private static final boolean verbose = Verbose.isVerbose(HTTPSClientTransport.class);
   public static final String USE_JDK_SSL_PROPERTY = "weblogic.wsee.client.ssl.usejdk";
   private static final boolean USE_JDK_SSL = Boolean.getBoolean("weblogic.wsee.client.ssl.usejdk");
   private static final String WLS_HTTPS_URL_CONNECTION = "weblogic.net.http.SOAPHttpsURLConnection";
   private SSLAdapter sslAdapter = null;

   public String getName() {
      return "https";
   }

   protected HttpURLConnection openConnection(URL var1, Proxy var2, TransportInfo var3) throws IOException {
      if (verbose) {
         Verbose.log((Object)("Opening connection to " + var1 + (var2 == null ? "" : " using " + var2)));
      }

      return this.getSSLAdapter().openConnection(var1, var2, var3);
   }

   public final void setSSLAdapter(SSLAdapter var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("SSLAdapter cannot be null");
      } else {
         this.sslAdapter = var1;
      }
   }

   private SSLAdapter getSSLAdapter() throws IOException {
      if (this.sslAdapter == null) {
         SSLAdapter var1;
         try {
            var1 = (SSLAdapter)getAdapterClass().newInstance();
         } catch (InstantiationException var3) {
            throw new IOException("Failed to instantiate SSL Adapter class: " + var3.getMessage());
         } catch (IllegalAccessException var4) {
            throw new IOException("Could not access SSL Adapter class: " + var4.getMessage());
         }

         this.sslAdapter = var1;
      }

      return this.sslAdapter;
   }

   private static Class getAdapterClass() throws IOException {
      Class var0;
      if (!USE_JDK_SSL && wlsSSLPresent()) {
         try {
            var0 = Class.forName("weblogic.wsee.connection.transport.https.WlsSSLAdapter");
         } catch (ClassNotFoundException var2) {
            throw new IOException("Failed to load SSLAdapter class: " + var2.getMessage());
         }
      } else {
         var0 = JdkSSLAdapter.class;
      }

      return var0;
   }

   private static boolean wlsSSLPresent() {
      URLConnection var0;
      try {
         URL var1 = new URL("https://127.0.0.1/");
         var0 = var1.openConnection();
      } catch (Exception var2) {
         throw new IllegalStateException(var2);
      }

      String var3 = var0.getClass().getName();
      return "weblogic.net.http.SOAPHttpsURLConnection".equals(var3);
   }
}
