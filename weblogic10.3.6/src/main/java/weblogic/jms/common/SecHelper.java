package weblogic.jms.common;

import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import weblogic.utils.encoders.BASE64Decoder;
import weblogic.utils.encoders.BASE64Encoder;

public class SecHelper {
   private static final int SALT_SIZE = 8;
   private static final int OLD_ITERATIONS = 13;
   private static final int ITERATIONS = 1024;
   private static final int AES_KEY_SIZE = 128;
   private static final String OLD_BLOCK_CIPHER_ALGORITHM = "PBEWithMD5AndDES";
   private static final String BLOCK_CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
   private static final String SECRET_KEY_ALGORITHM = "PBKDF2WithHmacSHA1";
   private static final String ALGORITHM_TOKEN = "{Algorithm}";
   private static final String SALT_TOKEN = "{Salt}";
   private static final String IV_TOKEN = "{IV}";
   private static final String DATA_TOKEN = "{Data}";

   public static String encryptString(char[] var0, String var1) throws GeneralSecurityException {
      return var1 == null ? null : encryptPassword(var0, var1.toCharArray());
   }

   private static byte[] charsToBytes(char[] var0) {
      if (var0 == null) {
         return null;
      } else {
         int var1 = var0.length * 2;
         byte[] var2 = new byte[var1];

         for(int var3 = 0; var3 < var0.length; ++var3) {
            int var4 = var0[var3] & '\uffff';
            var2[var3 * 2] = (byte)((var4 & '\uff00') >> 8);
            var2[var3 * 2 + 1] = (byte)(var4 & 255);
         }

         return var2;
      }
   }

   private static char[] bytesToChars(byte[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length % 2 != 0) {
         throw new AssertionError("Invalid number of bytes: " + var0.length);
      } else {
         int var1 = var0.length / 2;
         char[] var2 = new char[var1];

         for(int var3 = 0; var3 < var1; ++var3) {
            var2[var3] = (char)(var0[var3 * 2] << 8 | var0[var3 * 2 + 1]);
         }

         return var2;
      }
   }

   private static SecretKey generateSecretKey(char[] var0, byte[] var1) throws GeneralSecurityException {
      PBEKeySpec var2 = new PBEKeySpec(var0, var1, 1024, 128);
      SecretKeyFactory var3 = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
      SecretKey var4 = var3.generateSecret(var2);
      SecretKeySpec var5 = new SecretKeySpec(var4.getEncoded(), "AES");
      return var5;
   }

   public static String encryptPassword(char[] var0, char[] var1) throws GeneralSecurityException {
      SecureRandom var2 = new SecureRandom();
      byte[] var3 = new byte[8];
      var2.nextBytes(var3);
      SecretKey var4 = generateSecretKey(var0, var3);
      Cipher var5 = Cipher.getInstance("AES/CBC/PKCS5Padding");
      var5.init(1, var4);
      AlgorithmParameters var6 = var5.getParameters();
      byte[] var7 = ((IvParameterSpec)var6.getParameterSpec(IvParameterSpec.class)).getIV();
      byte[] var8 = var5.doFinal(charsToBytes(var1));
      BASE64Encoder var9 = new BASE64Encoder();
      String var10 = var9.encodeBuffer(var3);
      String var11 = var9.encodeBuffer(var7);
      String var12 = var9.encodeBuffer(var8);
      return "{Algorithm}AES/CBC/PKCS5Padding{Salt}" + var10 + "{IV}" + var11 + "{Data}" + var12;
   }

   public static char[] decryptString(char[] var0, String var1) throws GeneralSecurityException, IOException {
      int var2 = var1.indexOf("{Algorithm}");
      int var3 = var2 + "{Algorithm}".length();
      int var4 = var1.indexOf("{Salt}");
      int var5 = var4 + "{Salt}".length();
      int var6 = var1.indexOf("{Data}");
      int var7 = var6 + "{Data}".length();
      if (var2 < 0) {
         throw new GeneralSecurityException("Algorithm cannot be found");
      } else {
         String var8 = var1.substring(var3, var4);
         if (!"AES/CBC/PKCS5Padding".equals(var8) && !"PBEWithMD5AndDES".equals(var8)) {
            throw new GeneralSecurityException("algorithm " + var8 + " is not supported");
         } else if (var4 < 0) {
            throw new GeneralSecurityException("Salt cannot be found");
         } else if (var6 < 0) {
            throw new GeneralSecurityException("Encrypted data cannot be found");
         } else {
            SecretKey var9 = null;
            Object var10 = null;
            Object var11 = null;
            byte[] var21;
            if ("AES/CBC/PKCS5Padding".equals(var8)) {
               int var12 = var1.indexOf("{IV}");
               int var13 = var12 + "{IV}".length();
               if (var12 < 0) {
                  throw new GeneralSecurityException("Initialzation vector cannot be found for " + var8);
               }

               String var14 = var1.substring(var5, var12);
               String var15 = var1.substring(var13, var6);
               String var16 = var1.substring(var7);
               BASE64Decoder var17 = new BASE64Decoder();
               byte[] var18 = var17.decodeBuffer(var14);
               byte[] var19 = var17.decodeBuffer(var15);
               var21 = var17.decodeBuffer(var16);
               var9 = generateSecretKey(var0, var18);
               var10 = new IvParameterSpec(var19);
            } else {
               String var22 = var1.substring(var5, var6);
               String var24 = var1.substring(var7);
               BASE64Decoder var26 = new BASE64Decoder();
               byte[] var27 = var26.decodeBuffer(var22);
               var21 = var26.decodeBuffer(var24);
               var10 = new PBEParameterSpec(var27, 13);
               PBEKeySpec var28 = new PBEKeySpec(var0);
               SecretKeyFactory var29 = SecretKeyFactory.getInstance(var8);
               var9 = var29.generateSecret(var28);
            }

            Cipher var23 = Cipher.getInstance(var8);
            var23.init(2, var9, (AlgorithmParameterSpec)var10);

            try {
               byte[] var25 = var23.doFinal(var21);
               return bytesToChars(var25);
            } catch (Exception var20) {
               throw new GeneralSecurityException("Error occured during decryption");
            }
         }
      }
   }
}
