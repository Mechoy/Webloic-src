package weblogic.webservice.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import weblogic.utils.encoders.BASE64Encoder;
import weblogic.webservice.client.https.HostnameVerifier;
import weblogic.webservice.client.https.HttpsURLConnection;

/** @deprecated */
public final class WLSSLAdapter extends BaseWLSSLAdapter {
   private static final HostnameVerifier nonverifier = new NullVerifier((1)null);
   private HostnameVerifier verifier = HttpsURLConnection.getDefaultHostnameVerifier();

   public WLSSLAdapter() {
      this.setStrictChecking(this.getStrictCheckingDefault());
   }

   public WLSSLAdapter(boolean var1) {
      this.setStrictChecking(var1);
   }

   public final URLConnection openConnection(URL var1) throws IOException {
      Object var2 = null;
      if ("https".equalsIgnoreCase(var1.getProtocol())) {
         HttpsURLConnection var3 = new HttpsURLConnection(var1);
         var3.setSSLSocketFactory(this.getSocketFactory());
         if (verbose) {
            System.out.println("openConnection(" + var1 + ") returning " + var3);
            System.out.println("-- using HostnameVerifier " + var3.getHostnameVerifier());
         }

         var2 = var3;
      } else {
         var2 = var1.openConnection();
      }

      String var5 = var1.getUserInfo();
      if (var5 != null) {
         if (var5.indexOf(58) == -1) {
            var5 = var5 + ":";
         }

         String var4 = (new BASE64Encoder()).encodeBuffer(var5.getBytes());
         ((URLConnection)var2).setRequestProperty("Authorization", "Basic " + var4);
         if (verbose) {
            System.out.println("-- URL had username/password: " + var5);
            System.out.println("-- Set Authorization header on " + var2 + " to " + var4);
         }
      }

      return (URLConnection)var2;
   }

   public final void setStrictChecking(boolean var1) {
      if (this.adapterUsed()) {
         throw new IllegalArgumentException("Cannot change certificate checking once the adapter has been used");
      } else {
         if ((var1 || this.strictCertChecking) && (!var1 || !this.strictCertChecking)) {
            this._setStrictChecking(var1);
            if (var1) {
               this.setHostnameVerifier(this.verifier);
            } else {
               this.setHostnameVerifier(nonverifier);
            }
         }

      }
   }

   private final void setHostnameVerifier(HostnameVerifier var1) {
      this.verifier = var1;
      HttpsURLConnection.setDefaultHostnameVerifier(var1);
      if (verbose) {
         System.out.println("Set HostnameVerifier to " + var1);
      }

   }

   public static void main(String[] var0) {
      try {
         WLSSLAdapter var1 = new WLSSLAdapter();
         var1.setVerbose(true);
         System.out.println("Got adapter: " + var1);
         SSLSocket var2 = null;
         HttpsURLConnection var3 = null;
         byte[] var4 = new byte[32];
         var1.setStrictChecking(false);
         String[] var5 = new String[]{"https://miramar:7702/SecureSSLEchoService/SecureSSLEchoService?WSDL"};
         if (var0.length > 0) {
            var5 = var0;
         }

         for(int var6 = 0; var6 < var5.length; ++var6) {
            URL var7 = new URL(var5[var6]);
            var3 = (HttpsURLConnection)var1.openConnection(var7);
            System.out.println("for https, got this URLConnction: " + var3);
            if (var3.getHostnameVerifier() == nonverifier) {
               System.out.println("The connection has the NullVerifier");
            } else {
               System.out.println("!! The connection was not the NullVerifier");
            }

            try {
               var3.connect();
               InputStream var8 = var3.getInputStream();
               byte[] var9 = new byte[4096];
               System.out.println("Got input from connection" + var3 + ":");
               var8.read(var9);

               for(int var10 = 0; var10 < 32; ++var10) {
                  System.out.print(var4[var10]);
               }

               System.out.println();
               System.out.println("opened and read " + var3);
               SSLSocketFactory var13 = var3.getSSLSocketFactory();
               var2 = (SSLSocket)var13.createSocket(var7.getHost(), var7.getPort());
               System.out.println("created socket " + var2);
               var2.startHandshake();
               System.out.println("completed handshake on " + var2);
            } catch (Throwable var11) {
               System.out.println("Got exception: " + var11.getMessage());
            }
         }
      } catch (Throwable var12) {
         System.out.println("Caught exception:" + var12.getMessage());
         var12.printStackTrace();
      }

   }
}
