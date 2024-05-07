package weblogic.wsee.connection.transport.https;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import weblogic.wsee.connection.transport.http.HttpTransportInfo;
import weblogic.wsee.util.Verbose;

public class HttpsTransportInfo extends HttpTransportInfo {
   private static final long serialVersionUID = 5277936336380367841L;
   public static final String STRICT_CHECKING_DEFAULT = "weblogic.wsee.client.ssl.stricthostchecking";
   public static final String RELAXED_CHECKING_DEFAULT = "weblogic.wsee.client.ssl.relaxedtrustmanager";
   private static boolean strictCheckingDefault = false;
   private static boolean relaxedTrustManagerDefault = false;
   private static final HostnameVerifier NONVERIFIER = new RelaxedVerifier();
   private static final TrustManager[] RELAXED_MANAGERS = new TrustManager[]{new RelaxedX509TrustManager()};
   public static final HttpsTransportInfo DEFAULT_TRANSPORTINFO = new HttpsTransportInfo();
   private static final boolean verbose = Verbose.isVerbose(HttpsTransportInfo.class);
   private transient KeyManager[] keyManagers;
   private transient TrustManager[] trustManagers;
   private transient HostnameVerifier hostnameVerifier;
   private static final String KEYSTORE_PASSWORD_PROPERTY = "javax.net.ssl.keyStorePassword";
   private static final String KEYSTORE_PROPERTY = "javax.net.ssl.keyStore";
   private static final String TRUST_KEYSTORE_PROPERTY = "javax.net.ssl.trustStore";
   private transient SSLAdapter sslAdapter = null;

   public HttpsTransportInfo() {
      HostnameVerifier var1;
      if (!strictCheckingDefault) {
         var1 = NONVERIFIER;
      } else {
         var1 = null;
      }

      this.hostnameVerifier = var1;

      try {
         TrustManager[] var2;
         if (relaxedTrustManagerDefault) {
            var2 = RELAXED_MANAGERS;
         } else {
            var2 = getDefaultTrustManagers();
         }

         this.trustManagers = var2;
      } catch (Exception var4) {
         if (verbose) {
            var4.printStackTrace();
         }
      }

      try {
         this.keyManagers = getDefaultKeyManagers();
      } catch (Exception var3) {
         if (verbose) {
            var3.printStackTrace();
         }
      }

   }

   public HttpsTransportInfo(KeyManager[] var1, TrustManager[] var2, HostnameVerifier var3) {
      if (var1 == null) {
         throw new IllegalArgumentException("KeyManagers cannot be null");
      } else if (var2 == null) {
         throw new IllegalArgumentException("TrustManagers cannot be null");
      } else {
         this.keyManagers = var1;
         this.trustManagers = var2;
         this.hostnameVerifier = var3;
      }
   }

   public HttpsTransportInfo(SSLAdapter var1) {
      this.sslAdapter = var1;
   }

   public void setSSLAdapter(SSLAdapter var1) {
      this.sslAdapter = var1;
   }

   public SSLAdapter getSSLAdapter() {
      return this.sslAdapter;
   }

   public KeyManager[] getKeyManagers() {
      return this.keyManagers;
   }

   public void setKeyManagers(KeyManager[] var1) {
      this.keyManagers = var1;
   }

   public TrustManager[] getTrustManagers() {
      return this.trustManagers;
   }

   public void setTrustManagers(TrustManager[] var1) {
      this.trustManagers = var1;
   }

   public HostnameVerifier getHostnameVerifier() {
      return this.hostnameVerifier;
   }

   public void setHostnameVerifier(HostnameVerifier var1) {
      this.hostnameVerifier = var1;
   }

   public static void setStrictHostnameCheckDefault(boolean var0) {
      strictCheckingDefault = var0;
      if (!var0) {
         DEFAULT_TRANSPORTINFO.setHostnameVerifier(NONVERIFIER);
      } else {
         HostnameVerifier var1 = DEFAULT_TRANSPORTINFO.getHostnameVerifier();
         if (var1 == NONVERIFIER) {
            DEFAULT_TRANSPORTINFO.setHostnameVerifier((HostnameVerifier)null);
         }
      }

   }

   public static void setRelaxedTrustManagerDefault(boolean var0) {
      relaxedTrustManagerDefault = var0;
      if (var0) {
         DEFAULT_TRANSPORTINFO.setTrustManagers(RELAXED_MANAGERS);
      } else {
         TrustManager[] var1 = DEFAULT_TRANSPORTINFO.getTrustManagers();
         if (var1 == RELAXED_MANAGERS) {
            try {
               DEFAULT_TRANSPORTINFO.setTrustManagers(getDefaultTrustManagers());
            } catch (Exception var3) {
               if (verbose) {
                  var3.printStackTrace();
               }
            }
         }
      }

   }

   private static KeyManager[] getDefaultKeyManagers() throws NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException {
      String var0 = KeyManagerFactory.getDefaultAlgorithm();
      KeyManagerFactory var1 = KeyManagerFactory.getInstance(var0);
      KeyStore var2 = getKeyStoreFromSystemProperty("javax.net.ssl.keyStore", "javax.net.ssl.keyStorePassword");
      String var3 = System.getProperty("javax.net.ssl.keyStorePassword");
      var1.init(var2, var3 != null ? var3.toCharArray() : null);
      KeyManager[] var4 = var1.getKeyManagers();
      if (verbose) {
         Verbose.say("Loaded keymanagers from system properties");
      }

      return var4;
   }

   private static TrustManager[] getDefaultTrustManagers() throws KeyStoreException, NoSuchProviderException, NoSuchAlgorithmException {
      TrustManagerFactory var1 = TrustManagerFactory.getInstance("SunX509", "SunJSSE");
      KeyStore var2 = getKeyStoreFromSystemProperty("javax.net.ssl.trustStore", (String)null);
      var1.init(var2);
      TrustManager[] var0 = var1.getTrustManagers();
      if (verbose) {
         Verbose.say("Loaded default trust managers");
      }

      return var0;
   }

   private static KeyStore getKeyStoreFromSystemProperty(String var0, String var1) {
      String var2 = System.getProperty(var0);
      String var3;
      if (var1 != null) {
         var3 = System.getProperty(var1);
      } else {
         var3 = null;
      }

      if (var2 == null || var1 != null && var3 == null) {
         return null;
      } else {
         if (verbose) {
            Verbose.log((Object)("loading keystore from: " + var2));
         }

         FileInputStream var4 = null;

         KeyStore var5;
         try {
            var4 = new FileInputStream(var2);
            var5 = KeyStore.getInstance("jks");
            var5.load(var4, var3 != null ? var3.toCharArray() : null);
         } catch (Exception var15) {
            throw new IllegalStateException("Unable to load keystore", var15);
         } finally {
            try {
               var4.close();
            } catch (IOException var14) {
               var14.printStackTrace();
            }

         }

         return var5;
      }
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
      if (this.sslAdapter instanceof Serializable) {
         var1.writeObject(this.sslAdapter);
      } else {
         Object var2 = null;
         var1.writeObject(var2);
      }

   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.sslAdapter = (SSLAdapter)var1.readObject();
   }

   static {
      try {
         if (verbose) {
            Verbose.log((Object)"HttpsTransportInfo verbose output enabled");
         }

         if ("true".equals(System.getProperty("weblogic.wsee.client.ssl.stricthostchecking"))) {
            strictCheckingDefault = true;
            if (verbose) {
               Verbose.log((Object)"JdkSSLAdapter strict hostname checking ENABLED by default");
            }
         }

         if ("true".equals(System.getProperty("weblogic.wsee.client.ssl.relaxedtrustmanager"))) {
            relaxedTrustManagerDefault = true;
            if (verbose) {
               Verbose.log((Object)"JdkSSLAdapter relaxed trust manager ENABLED by default");
            }
         }
      } catch (Throwable var1) {
         if (verbose) {
            Verbose.log((Object)("JdkSSLAdapter error: " + var1.getMessage()));
            Verbose.logException(var1);
         }
      }

   }

   private static class RelaxedX509TrustManager implements X509TrustManager {
      private RelaxedX509TrustManager() {
      }

      public boolean isClientTrusted(X509Certificate[] var1) {
         return true;
      }

      public boolean isServerTrusted(X509Certificate[] var1) {
         return true;
      }

      public X509Certificate[] getAcceptedIssuers() {
         return null;
      }

      public void checkClientTrusted(X509Certificate[] var1, String var2) {
      }

      public void checkServerTrusted(X509Certificate[] var1, String var2) {
      }

      // $FF: synthetic method
      RelaxedX509TrustManager(Object var1) {
         this();
      }
   }

   private static class RelaxedVerifier implements HostnameVerifier {
      private RelaxedVerifier() {
      }

      public boolean verify(String var1, SSLSession var2) {
         if (HttpsTransportInfo.verbose) {
            Verbose.log((Object)"HTTPSClientTransport RelaxedVerifier called.");
         }

         if (!var1.equals(var2.getPeerHost())) {
            if (HttpsTransportInfo.verbose) {
               Verbose.log((Object)("HTTPSClientTransport NullVerifyer certificate <" + var2.getPeerHost() + "> does not match host <" + var1 + "> however continuing anyway."));
            }
         } else if (HttpsTransportInfo.verbose) {
            Verbose.log((Object)("HTTPSClientTransport success URL: " + var1));
         }

         return true;
      }

      // $FF: synthetic method
      RelaxedVerifier(Object var1) {
         this();
      }
   }
}
