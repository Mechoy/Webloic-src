package weblogic.xml.security.keyinfo;

import java.security.Key;

public class Utils {
   public static final boolean supports(String[] var0, String var1) {
      for(int var2 = 0; var2 < var0.length; ++var2) {
         String var3 = var0[var2];
         if (var3 != null && var3.equals(var1)) {
            return true;
         }
      }

      return false;
   }

   public static final String[] getAlgorithms(Key var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("Provided null, expected key");
      } else {
         String var1 = var0.getAlgorithm();
         if ("DESede".equals(var1)) {
            return KeyProvider.TRIPLEDES_ALGORITHMS;
         } else if ("AES".equals(var1)) {
            return KeyProvider.AES_ALGORITHMS;
         } else if ("RSA".equals(var1)) {
            return KeyProvider.RSA_ALGORITHMS;
         } else if ("DSA".equals(var1)) {
            return KeyProvider.DSA_ALGORITHMS;
         } else {
            throw new IllegalArgumentException("Unsupported algorithm: " + var1);
         }
      }
   }

   public static boolean matches(byte[] var0, byte[] var1) {
      if (var0 != null && var1 != null) {
         if (var0.length != var1.length) {
            return false;
         } else {
            for(int var2 = 0; var2 < var0.length; ++var2) {
               byte var3 = var0[var2];
               byte var4 = var1[var2];
               if (var3 != var4) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }
}
