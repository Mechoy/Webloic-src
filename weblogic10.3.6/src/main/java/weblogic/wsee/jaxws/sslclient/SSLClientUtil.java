package weblogic.wsee.jaxws.sslclient;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import javax.xml.ws.WebServiceException;
import weblogic.wsee.util.Verbose;

public final class SSLClientUtil {
   private static boolean verbose = Verbose.isVerbose(SSLClientUtil.class);
   private static final String KEYSTORE_PASSWORD_PROPERTY = "javax.net.ssl.keyStorePassword";
   private static final String KEYSTORE_PROPERTY = "javax.net.ssl.keyStore";
   private static final String TRUST_KEYSTORE_PROPERTY = "javax.net.ssl.trustStore";
   public static final String RELAXED_CHECKING_DEFAULT = "weblogic.wsee.client.ssl.relaxedtrustmanager";
   private static final TrustManager[] RELAXED_MANAGERS = new TrustManager[]{new RelaxedX509TrustManager()};

   private SSLClientUtil() {
   }

   public static SSLSocketFactory getSSLSocketFactoryFromSysProperties() {
      TrustManager[] var0 = null;
      KeyManager[] var1 = null;

      try {
         String var2 = System.getProperty("javax.net.ssl.keyStore");
         String var3 = System.getProperty("javax.net.ssl.keyStorePassword");
         var1 = getKeyManagers(var2, var3, (String)null, (String)null, (String)null);
         if ("true".equalsIgnoreCase(System.getProperty("weblogic.wsee.client.ssl.relaxedtrustmanager"))) {
            var0 = RELAXED_MANAGERS;
         } else {
            String var4 = System.getProperty("javax.net.ssl.trustStore", (String)null);
            var0 = getTrustManagers(var4, (String)null, (String)null, (String)null, (String)null);
         }

         return getSSLSocketFactory(var1, var0);
      } catch (Exception var5) {
         if (verbose) {
            var5.printStackTrace();
         }

         throw new WebServiceException(var5);
      }
   }

   public static PersistentSSLInfo getPersistentSSLInfoFromSysProperties() {
      PersistentSSLInfo var0 = new PersistentSSLInfo();

      try {
         String var1 = System.getProperty("javax.net.ssl.keyStore");
         String var2 = System.getProperty("javax.net.ssl.keyStorePassword");
         if (!"true".equalsIgnoreCase(System.getProperty("weblogic.wsee.client.ssl.relaxedtrustmanager"))) {
            String var3 = System.getProperty("javax.net.ssl.trustStore", (String)null);
            var0.setTrustKeystore(var3);
         }

         var0.setKeystore(var1);
         var0.setKeystorePassword(var2);
         return var0;
      } catch (Exception var4) {
         if (verbose) {
            var4.printStackTrace();
         }

         throw new WebServiceException(var4);
      }
   }

   public static SSLSocketFactory getSSLSocketFactory(KeyManager[] var0, TrustManager[] var1) {
      try {
         SSLContext var2 = SSLContext.getInstance("SSL");
         if ("true".equalsIgnoreCase(System.getProperty("weblogic.wsee.client.ssl.relaxedtrustmanager"))) {
            var1 = RELAXED_MANAGERS;
         }

         var2.init(var0, var1, new SecureRandom());
         SSLSocketFactory var3 = var2.getSocketFactory();
         return var3;
      } catch (GeneralSecurityException var4) {
         throw new WebServiceException("Get SSLSocketFactory failed:" + var4.getMessage());
      }
   }

   public static SSLSocketFactory getSSLSocketFactory(PersistentSSLInfo var0) {
      if (verbose) {
         Verbose.log((Object)("sslInfo=" + var0.toString()));
      }

      KeyManager[] var1 = getKeyManagers(var0.getKeystore(), var0.getKeystorePassword(), var0.getKeystoreType(), var0.getKeyAlias(), var0.getKeyPassword());
      TrustManager[] var2 = getTrustManagers(var0.getTrustKeystore(), var0.getTrustKeystorePassword(), var0.getTrustKeystoreType(), var0.getTrustKeystoreAlgorithm(), var0.getTrustKeystoreProvider());
      return getSSLSocketFactory(var1, var2);
   }

   private static KeyManager[] getKeyManagers(String var0, String var1, String var2, String var3, String var4) {
      try {
         String var5 = KeyManagerFactory.getDefaultAlgorithm();
         if (var4 == null) {
            var4 = var1;
         }

         KeyStore var6 = getKeystore(var0, var1, var2);
         KeyManagerFactory var7 = KeyManagerFactory.getInstance(var5);
         if (var3 == null) {
            var7.init(var6, var4 == null ? null : var4.toCharArray());
         } else {
            Certificate[] var8 = var6.getCertificateChain(var3);
            if (var8 == null) {
               throw new SecurityException("No such key with alias '" + var3 + "' in key-store '" + var0 + "'");
            }

            Key var9 = var6.getKey(var3, var4 == null ? null : var4.toCharArray());
            KeyStore var10 = KeyStore.getInstance(var2);
            var10.load((InputStream)null, var1 == null ? null : var1.toCharArray());
            var10.setKeyEntry(var3, var9, var4 == null ? null : var4.toCharArray(), var8);
            var7.init(var10, var4 == null ? null : var4.toCharArray());
         }

         return var7.getKeyManagers();
      } catch (Exception var11) {
         if (verbose) {
            var11.printStackTrace();
         }

         throw new WebServiceException("Get KeyManager[] failed:" + var11.getMessage());
      }
   }

   private static TrustManager[] getTrustManagers(String var0, String var1, String var2, String var3, String var4) {
      if (var0 == null) {
         return null;
      } else {
         if (verbose) {
            Verbose.log((Object)("trustKeystore=" + var0));
         }

         try {
            if (var3 == null) {
               var3 = TrustManagerFactory.getDefaultAlgorithm();
            }

            if (var2 == null) {
               var2 = "JKS";
            }

            TrustManagerFactory var5 = null;
            if (var4 == null) {
               var5 = TrustManagerFactory.getInstance(var3);
            } else {
               var5 = TrustManagerFactory.getInstance(var3, var4);
            }

            KeyStore var6 = getKeystore(var0, var1, var2);
            var5.init(var6);
            TrustManager[] var7 = var5.getTrustManagers();
            if (verbose) {
               Verbose.say("Loaded trust managers");
            }

            return var7;
         } catch (Exception var8) {
            if (verbose) {
               var8.printStackTrace();
            }

            throw new WebServiceException("Get TrustManager[] failed:" + var8.getMessage());
         }
      }
   }

   private static KeyStore getKeystore(String var0, String var1, String var2) {
      if (var0 == null) {
         return null;
      } else {
         if (verbose) {
            Verbose.log((Object)("loading keystore from: " + var0));
         }

         FileInputStream var3 = null;
         KeyStore var4 = null;

         try {
            var3 = new FileInputStream(var0);
            var4 = KeyStore.getInstance(var2 == null ? "JKS" : var2);
            var4.load(var3, var1 != null ? var1.toCharArray() : null);
         } catch (Exception var14) {
            throw new IllegalStateException("Unable to load keystore", var14);
         } finally {
            try {
               if (var3 != null) {
                  var3.close();
               }
            } catch (IOException var13) {
               var13.printStackTrace();
            }

         }

         return var4;
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

      public void checkClientTrusted(X509Certificate[] var1, String var2) throws CertificateException {
      }

      public void checkServerTrusted(X509Certificate[] var1, String var2) throws CertificateException {
      }

      // $FF: synthetic method
      RelaxedX509TrustManager(Object var1) {
         this();
      }
   }
}
