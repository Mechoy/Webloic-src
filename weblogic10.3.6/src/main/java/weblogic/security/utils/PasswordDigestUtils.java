package weblogic.security.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public final class PasswordDigestUtils {
   public static String UTF_8 = "UTF-8";

   private PasswordDigestUtils() {
   }

   public static byte[] passwordDigest(byte[] var0, String var1, byte[] var2) throws NoSuchAlgorithmException {
      if (var2 == null) {
         return null;
      } else {
         MessageDigest var3 = null;
         var3 = MessageDigest.getInstance("SHA-1");
         if (var0 != null) {
            var3.update(var0);
         }

         try {
            if (var1 != null) {
               var3.update(var1.getBytes(UTF_8));
            }
         } catch (UnsupportedEncodingException var5) {
            throw new NoSuchAlgorithmException("Unable to create password digest - UTF-8 encoding required but unavailable: " + var5.getMessage());
         }

         var3.update(var2);
         byte[] var4 = var3.digest();
         return var4;
      }
   }

   public static boolean verifyDigest(byte[] var0, String var1, byte[] var2, byte[] var3) throws NoSuchAlgorithmException {
      if (var2 != null && var3 != null) {
         MessageDigest var4 = null;
         var4 = MessageDigest.getInstance("SHA-1");
         if (var0 != null) {
            var4.update(var0);
         }

         try {
            if (var1 != null) {
               var4.update(var1.getBytes(UTF_8));
            }

            var4.update(var2);
         } catch (UnsupportedEncodingException var6) {
            throw new NoSuchAlgorithmException("Unable to create password digest - UTF-8 encoding required but unavailable: " + var6.getMessage());
         }

         byte[] var5 = var4.digest();
         return Arrays.equals(var5, var3);
      } else {
         return false;
      }
   }

   public static byte[] derivedKey(byte[] var0, int var1, byte[] var2) throws NoSuchAlgorithmException {
      if (var2 == null) {
         return null;
      } else if (var0 != null && var1 > 0) {
         MessageDigest var3 = null;
         var3 = MessageDigest.getInstance("SHA-1");
         var3.update(var2);
         var3.update(var0);
         byte[] var4 = var3.digest();

         for(int var5 = 1; var5 != var1; ++var5) {
            var3.update(var4);
            var4 = var3.digest();
         }

         return var4;
      } else {
         return null;
      }
   }
}
