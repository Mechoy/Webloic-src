package weblogic.security.utils;

import java.util.HashMap;
import java.util.Map;

public final class SSLCipherUtility {
   private static final Map cipherSuitesMap = initCipherSuites();

   public static int getKeySize(String var0) {
      CipherSuite var1 = getCipherSuite(var0);
      return var1 == null ? -1 : var1.getKeySize();
   }

   public static String[] removeNullCipherSuites(String[] var0) {
      int var1 = countNullCipherSuites(var0);
      if (var1 == 0) {
         return var0;
      } else {
         String[] var2 = new String[var0.length - var1];
         int var3 = 0;

         for(int var4 = 0; var3 < var0.length; ++var3) {
            if (!isNullCipherSuite(var0[var3])) {
               var2[var4++] = var0[var3];
            }
         }

         return var2;
      }
   }

   public static void normalizeNames(String[] var0) {
      if (var0 != null) {
         for(int var1 = 0; var1 < var0.length; ++var1) {
            var0[var1] = normalizeName(var0[var1]);
         }
      }

   }

   private static final String normalizeName(String var0) {
      if (!var0.startsWith("TLS_")) {
         if (var0.startsWith("SSL_")) {
            var0 = var0.substring(4);
         }

         var0 = "TLS_" + var0;
      }

      return var0;
   }

   private static final boolean isNullCipherSuite(String var0) {
      return var0.equals("SSL_NULL_WITH_NULL_NULL") || var0.equals("TLS_NULL_WITH_NULL_NULL");
   }

   private static final int countNullCipherSuites(String[] var0) {
      int var1 = 0;
      if (var0 != null) {
         for(int var2 = 0; var2 < var0.length; ++var2) {
            if (isNullCipherSuite(var0[var2])) {
               ++var1;
            }
         }
      }

      return var1;
   }

   private static final CipherSuite getCipherSuite(String var0) {
      return (CipherSuite)cipherSuitesMap.get(normalizeName(var0));
   }

   private static final Map initCipherSuites() {
      CipherSuite[] var0 = new CipherSuite[]{new CipherSuite("TLS_NULL_WITH_NULL_NULL", 0, 0, true), new CipherSuite("TLS_RSA_WITH_NULL_MD5", 1, 0, true), new CipherSuite("TLS_RSA_WITH_NULL_SHA", 2, 0, true), new CipherSuite("TLS_RSA_WITH_RC4_128_MD5", 4, 128, false), new CipherSuite("TLS_RSA_WITH_RC4_128_SHA", 5, 128, false), new CipherSuite("TLS_RSA_WITH_DES_CBC_SHA", 9, 56, true), new CipherSuite("TLS_RSA_WITH_3DES_EDE_CBC_SHA", 10, 168, false), new CipherSuite("TLS_RSA_EXPORT_WITH_RC4_40_MD5", 3, 40, true), new CipherSuite("TLS_RSA_EXPORT_WITH_DES40_CBC_SHA", 8, 40, true), new CipherSuite("TLS_RSA_EXPORT_WITH_DES_40_CBC_SHA", 8, 40, true), new CipherSuite("TLS_RSA_EXPORT1024_WITH_DES_CBC_SHA", 98, 56, true), new CipherSuite("TLS_RSA_EXPORT1024_WITH_RC4_56_SHA", 100, 56, true), new CipherSuite("TLS_DHE_DSS_WITH_DES_CBC_SHA", 18, 56, false), new CipherSuite("TLS_DHE_DSS_WITH_3DES_EDE_CBC_SHA", 19, 168, false), new CipherSuite("TLS_DHE_DSS_WITH_RC4_128_SHA", 102, 128, false), new CipherSuite("TLS_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA", 17, 40, true), new CipherSuite("TLS_DHE_DSS_EXPORT_WITH_DES_40_CBC_SHA", 17, 40, true), new CipherSuite("TLS_DHE_DSS_EXPORT1024_WITH_DES_CBC_SHA", 99, 56, true), new CipherSuite("TLS_DHE_DSS_EXPORT1024_WITH_RC4_56_SHA", 101, 56, true), new CipherSuite("TLS_DH_anon_WITH_RC4_128_MD5", 24, 128, false), new CipherSuite("TLS_DH_anon_WITH_DES_CBC_SHA", 26, 56, false), new CipherSuite("TLS_DH_anon_WITH_3DES_EDE_CBC_SHA", 27, 168, false), new CipherSuite("TLS_DH_anon_EXPORT_WITH_RC4_40_MD5", 23, 40, true), new CipherSuite("TLS_DH_anon_EXPORT_WITH_DES40_CBC_SHA", 25, 40, true), new CipherSuite("TLS_DH_anon_EXPORT_WITH_DES_40_CBC_SHA", 25, 40, true), new CipherSuite("TLS_ECDH_ECDSA_WITH_RC4_128_SHA", 72, 128, false), new CipherSuite("TLS_ECDH_ECDSA_WITH_DES_CBC_SHA", 73, 56, false), new CipherSuite("TLS_ECDH_ECDSA_WITH_3DES_EDE_CBC_SHA", 74, 168, false), new CipherSuite("TLS_ECDH_ECDSA_EXPORT_WITH_RC4_40_SHA", 65412, 40, true), new CipherSuite("TLS_ECDH_ECDSA_EXPORT_WITH_RC4_56_SHA", 65413, 56, true), new CipherSuite("TLS_RSA_WITH_AES_128_CBC_SHA", 47, 128, false), new CipherSuite("TLS_RSA_WITH_AES_256_CBC_SHA", 53, 256, false)};
      HashMap var1 = new HashMap(var0.length);

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1.put(var0[var2].name, var0[var2]);
      }

      return var1;
   }

   private static final class CipherSuite {
      private String name;
      private int tag;
      private int keySize;
      private boolean exportable;

      public CipherSuite(String var1, int var2, int var3, boolean var4) {
         this.name = var1;
         this.tag = var2;
         this.keySize = var3;
         this.exportable = var4;
      }

      public String getName() {
         return this.name;
      }

      public int getTag() {
         return this.tag;
      }

      public final int getKeySize() {
         return this.keySize;
      }

      public final boolean isExportable() {
         return this.exportable;
      }
   }
}
