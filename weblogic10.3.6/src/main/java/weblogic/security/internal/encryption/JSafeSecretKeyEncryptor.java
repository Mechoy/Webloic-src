package weblogic.security.internal.encryption;

import com.rsa.jsafe.JSAFE_SecretKey;
import com.rsa.jsafe.JSAFE_SymmetricCipher;
import weblogic.security.SecurityLogger;
import weblogic.security.shared.LoggerWrapper;

final class JSafeSecretKeyEncryptor {
   private static final String KEY_ALGORITHM = "PBE/SHA1/RC2/CBC/PKCS12PBE-5-128";
   private static final int SALT_LENGTH = 8;
   private static LoggerWrapper logger = LoggerWrapper.getInstance("SecurityEncryptionService");

   static byte[] encryptSecretKey(JSAFE_SecretKey var0, char[] var1, byte[] var2) throws EncryptionServiceException {
      byte[] var3 = doubleSalt(var2);
      byte[] var4 = var0.getSecretKeyData();
      JSafeEncryptionServiceImpl.log("Key material length: " + var4.length);
      Object var5 = null;
      JSAFE_SecretKey var6 = null;
      JSAFE_SymmetricCipher var7 = null;

      byte[] var17;
      try {
         var7 = JSafeEncryptionServiceImpl.getSymmetricCipher("PBE/SHA1/RC2/CBC/PKCS12PBE-5-128", "Java");
         var7.setSalt(var2, 0, var2.length);
         var6 = var7.getBlankKey();
         var6.setPassword(var1, 0, var1.length);
         var7.encryptInit(var6);
         var17 = new byte[var7.getOutputBufferSize(var4.length)];
         int var8 = var7.encryptUpdate(var4, 0, var4.length, var17, 0);
         var7.encryptFinal(var17, var8);
      } catch (Exception var15) {
         throw new EncryptionServiceException(var15.toString());
      } finally {
         var7.clearSensitiveData();
         var6.clearSensitiveData();

         for(int var12 = 0; var12 < var1.length; ++var12) {
            var1[var12] = 0;
         }

      }

      return var17;
   }

   static JSAFE_SecretKey decryptSecretKey(String var0, byte[] var1, char[] var2, byte[] var3) throws EncryptionServiceException {
      byte[] var4 = doubleSalt(var3);
      byte[] var5 = new byte[var1.length];
      log("key material length: " + var5.length);
      JSAFE_SecretKey var6 = null;
      JSAFE_SymmetricCipher var7 = null;
      JSAFE_SecretKey var8 = null;

      try {
         var7 = JSafeEncryptionServiceImpl.getSymmetricCipher("PBE/SHA1/RC2/CBC/PKCS12PBE-5-128", "Java");
         var7.setSalt(var3, 0, var3.length);
         var6 = var7.getBlankKey();
         var6.setPassword(var2, 0, var2.length);
         var7.decryptInit(var6);
         int var9 = var7.decryptUpdate(var1, 0, var1.length, var5, 0);
         int var10 = var7.decryptFinal(var5, var9);
         int var11 = var9 + var10;
         log(var11 + " bytes of the array filled");
         byte[] var12 = new byte[var11];
         System.arraycopy(var5, 0, var12, 0, var12.length);
         log("getting cipher to generate key");
         var7 = JSAFE_SymmetricCipher.getInstance(var0, "Java");
         log("blank key from cipher");
         var8 = var7.getBlankKey();
         log("setting key data: " + var12.length);
         var8.setSecretKeyData(var12, 0, var12.length);
      } catch (Exception var18) {
         throw new EncryptionServiceException(SecurityLogger.getErrorDecryptingKey(var18.toString()));
      } finally {
         if (var6 != null) {
            var6.clearSensitiveData();
         }

         if (var7 != null) {
            var7.clearSensitiveData();
         }

         for(int var15 = 0; var15 < var2.length; ++var15) {
            var2[var15] = 0;
         }

      }

      return var8;
   }

   static byte[] doubleSalt(byte[] var0) {
      if (var0.length == 8) {
         return var0;
      } else {
         byte[] var1 = new byte[8];
         System.arraycopy(var0, 0, var1, 0, 4);
         System.arraycopy(var0, 0, var1, 4, 4);
         return var1;
      }
   }

   public static void log(String var0) {
      if (logger.isDebugEnabled()) {
         logger.debug(System.currentTimeMillis() + " : " + Thread.currentThread().getName() + " : " + var0);
      }

   }
}
