package weblogic.security.SSL.jsseadapter;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import weblogic.management.provider.CommandLine;
import weblogic.security.SSL.SSLEnabledProtocolVersions;
import weblogic.security.SSL.SSLEnabledProtocolVersionsLogging;
import weblogic.security.utils.SSLSetup;
import weblogic.utils.encoders.BASE64Decoder;

public final class JaSSLSupport {
   private static final String SSL3 = "SSLv3";
   private static final String TLS_ONLY = "TLS";
   private static final String ALL_KEY = "ALL";
   private static final String SSL3_TLS = "SSL3_TLS";
   private static final String[] SSL3_ONLY = new String[]{"SSLv3"};
   private static volatile Map<String, String[]> SUPPORTED_PROTOCOLS = null;
   private static final boolean disableNullCipher = Boolean.getBoolean("weblogic.security.disableNullCipher");
   private static boolean allowUnencryptedNullCipher = Boolean.getBoolean("weblogic.ssl.AllowUnencryptedNullCipher");
   private static boolean sendEmptyCAList = false;
   private static volatile boolean x509BasicConstraintsStrict = false;
   private static volatile boolean noV1CAs = false;
   private static boolean anonymousCipherAllowed = Boolean.getBoolean("weblogic.security.SSL.AllowAnonymousCipher");
   static final String CERT_BEGIN_HEADER = "-----BEGIN CERTIFICATE-----";
   static final String CERT_END_HEADER = "-----END CERTIFICATE-----";

   private static void initSupportedProtocols(String[] var0) {
      if (SUPPORTED_PROTOCOLS == null) {
         HashMap var1 = new HashMap(4);
         var1.put("SSLv3", SSL3_ONLY);
         String[] var2 = grabTLSProtocols(var0);
         String[] var3 = new String[var2.length + 1];
         System.arraycopy(SSL3_ONLY, 0, var3, 0, SSL3_ONLY.length);
         System.arraycopy(var2, 0, var3, 1, var2.length);
         var1.put("TLS", var2);
         var1.put("SSL3_TLS", var3);
         var1.put("ALL", var0);
         SUPPORTED_PROTOCOLS = var1;
      }

   }

   static String[] getEnabledProtocols(String[] var0) {
      String var1 = CommandLine.getCommandLine().getSSLMinimumProtocolVersion();
      if (null != var1) {
         SSLEnabledProtocolVersionsLogging var6 = new SSLEnabledProtocolVersionsLogging();
         return SSLEnabledProtocolVersions.getJSSEProtocolVersions(var1, var0, var6);
      } else {
         initSupportedProtocols(var0);
         int var2 = SSLSetup.getProtocolVersion();
         String var3 = null;
         switch (var2) {
            case 0:
               var3 = "TLS";
               break;
            case 1:
               var3 = "SSLv3";
               break;
            case 2:
               var3 = "SSL3_TLS";
               break;
            case 3:
               var3 = "ALL";
         }

         String[] var4 = null;
         String[] var5 = null;
         if (var3 != null) {
            var5 = (String[])SUPPORTED_PROTOCOLS.get(var3);
         }

         if (var5 != null) {
            var4 = new String[var5.length];
            System.arraycopy(var5, 0, var4, 0, var5.length);
         }

         return var4;
      }
   }

   public static String[] combineCiphers(String[] var0, String[] var1) {
      int var2 = 0;
      if (var0 != null) {
         var2 += var0.length;
      }

      if (var1 != null) {
         var2 += var1.length;
      }

      ArrayList var3 = new ArrayList(var2);
      String[] var4;
      int var5;
      int var6;
      String var7;
      if (var0 != null && var0.length > 0) {
         var4 = var0;
         var5 = var0.length;

         for(var6 = 0; var6 < var5; ++var6) {
            var7 = var4[var6];
            var3.add(var7);
         }
      }

      if (var1 != null && var1.length > 0) {
         var4 = var1;
         var5 = var1.length;

         for(var6 = 0; var6 < var5; ++var6) {
            var7 = var4[var6];
            var3.add(var7);
         }
      }

      var4 = new String[var3.size()];
      return (String[])var3.toArray(var4);
   }

   public static synchronized boolean isUnEncrytedNullCipherAllowed() {
      if (disableNullCipher) {
         if (allowUnencryptedNullCipher) {
            throw new IllegalArgumentException("Can not start SSL due to conflicting configuration - System configure parameter of weblogic.security.disableNullCipher = true, and configure parameter weblogic.security.ssl.allowUnencryptedNullCipher = true");
         } else {
            return false;
         }
      } else {
         return allowUnencryptedNullCipher;
      }
   }

   static boolean isAnonymousCipherAllowed() {
      return anonymousCipherAllowed;
   }

   public static synchronized void setSendEmptyCAList(boolean var0) {
      sendEmptyCAList = var0;
   }

   public static synchronized boolean isSendEmptyCAListEnabled() {
      return sendEmptyCAList;
   }

   public static PrivateKey getLocalIdentityPrivateKey(InputStream var0, char[] var1) throws KeyManagementException {
      return RSAPKFactory.getPrivateKey(var0, var1);
   }

   static byte[] readFully(InputStream var0) throws IOException {
      DataInputStream var1 = new DataInputStream(var0);
      byte[] var2 = new byte[var1.available()];
      var1.readFully(var2);
      return var2;
   }

   static byte[] decodeData(String var0) throws IOException {
      BASE64Decoder var1 = new BASE64Decoder();
      return var1.decodeBuffer(var0);
   }

   static void loadCerts(KeyStore var0, Certificate[] var1) {
      Certificate[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Certificate var5 = var2[var4];

         try {
            var0.setCertificateEntry(var5.toString(), var5);
         } catch (KeyStoreException var7) {
            if (JaLogger.isLoggable(Level.SEVERE)) {
               JaLogger.log(Level.SEVERE, JaLogger.Component.TRUSTSTORE_MANAGER, var7, "Error loading CAs into trust KeyStore.");
            }
         }
      }

   }

   static void setX509BasicConstraintsStrict(boolean var0) {
      x509BasicConstraintsStrict = var0;
   }

   static boolean isX509BasicConstraintsStrict() {
      return x509BasicConstraintsStrict;
   }

   static void setNoV1CAs(boolean var0) {
      noV1CAs = var0;
   }

   static boolean isNoV1CAs() {
      return noV1CAs;
   }

   private static String[] grabTLSProtocols(String[] var0) {
      String[] var1 = new String[0];
      if (var0 != null && var0.length > 0) {
         ArrayList var2 = new ArrayList();
         String[] var3 = var0;
         int var4 = var0.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            if (var6.startsWith("TLS")) {
               var2.add(var6);
            }
         }

         if (var2.size() > 0) {
            var1 = (String[])((String[])var2.toArray(var1));
         }
      }

      return var1;
   }
}
