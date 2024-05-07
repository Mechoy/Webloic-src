package weblogic.xml.crypto.encrypt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;

public class Utils extends weblogic.xml.security.utils.Utils {
   private static final int DES_KEYLENGTH = 7;
   private static final int TRIPLEDES_KEYLENGTH = 21;
   private static final String ALGORITHM_ID_SHA1 = "HmacSHA1";
   private static final String KEY_FACTORY_ID = "AES";

   static byte[] stripLeadingZeros(byte[] var0) {
      int var1;
      for(var1 = 0; var1 < var0.length && var0[var1] == 0; ++var1) {
      }

      if (var1 > 0) {
         byte[] var2 = new byte[var0.length - var1];
         System.arraycopy(var0, var1, var2, 0, var2.length);
         return var2;
      } else {
         return var0;
      }
   }

   public static boolean readIV(InputStream var0, byte[] var1) throws XMLEncryptionException {
      int var2 = 0;

      int var6;
      for(boolean var3 = false; var2 < var1.length; var2 += var6) {
         try {
            var6 = var0.read(var1, var2, var1.length - var2);
         } catch (IOException var5) {
            var6 = -1;
         }

         if (var6 == -1) {
            throw new XMLEncryptionException("Could not read IV");
         }
      }

      return true;
   }

   public static void writeIV(OutputStream var0, byte[] var1) throws XMLEncryptionException {
      try {
         var0.write(var1);
      } catch (IOException var3) {
         throw new XMLEncryptionException("Could not write IV", var3);
      }
   }

   public static byte[] P_SHA1(byte[] var0, byte[] var1, int var2) throws NoSuchAlgorithmException, InvalidKeyException {
      Mac var3 = Mac.getInstance("HmacSHA1");
      byte[] var4 = new byte[var2];
      SecretKeySpec var5 = new SecretKeySpec(var0, "AES");
      byte[] var6 = var1;
      int var7 = 0;

      do {
         var3.init(var5);
         var6 = var3.doFinal(var6);
         var3.reset();
         var3.init(var5);
         var3.update(var6);
         byte[] var8 = var3.doFinal(var1);
         var3.reset();

         for(int var9 = 0; var7 < var2 && var9 < var8.length; ++var9) {
            var4[var7] = var8[var9];
            ++var7;
         }
      } while(var7 < var2);

      return var4;
   }

   public static byte[] DESParity(byte[] var0, byte[] var1) {
      if (var0.length != 7) {
         throw new IllegalArgumentException("key must be 56 bits");
      } else {
         byte[] var2;
         int var3;
         if (var1 != null && var1.length == 8) {
            var2 = var1;

            for(var3 = 0; var3 < 8; ++var3) {
               var2[var3] = 0;
            }
         } else {
            var2 = new byte[8];
         }

         var3 = 0;

         for(int var4 = 7; var4 >= 0; --var4) {
            boolean var5 = true;

            for(int var6 = 1; var6 < 8; ++var3) {
               boolean var7 = (var0[6 - var3 / 8] & 1 << var3 % 8) > 0;
               if (var7) {
                  var2[var4] = (byte)(var2[var4] | 1 << var6);
                  var5 = !var5;
               }

               ++var6;
            }

            if (var5) {
               var2[var4] = (byte)(var2[var4] | 1);
            }
         }

         return var2;
      }
   }

   public static byte[] tripleDESParity(byte[] var0) {
      if (var0.length != 21) {
         throw new IllegalArgumentException("key must be 168 bits");
      } else {
         byte[] var1 = new byte[24];
         byte[] var2 = new byte[7];
         byte[] var3 = new byte[8];

         for(int var4 = 0; var4 < 3; ++var4) {
            int var5;
            for(var5 = 0; var5 < 7; ++var5) {
               var2[var5] = var0[var4 * 7 + var5];
            }

            var3 = DESParity(var2, var3);

            for(var5 = 0; var5 < 8; ++var5) {
               var1[var5 + var4 * 8] = var3[var5];
            }
         }

         return var1;
      }
   }
}
