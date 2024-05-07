package weblogic.security.internal.encryption;

import com.rsa.jsafe.CryptoJ;
import com.rsa.jsafe.FIPS140Context;
import com.rsa.jsafe.JSAFE_InvalidParameterException;
import com.rsa.jsafe.JSAFE_InvalidUseException;
import com.rsa.jsafe.JSAFE_SecretKey;
import com.rsa.jsafe.JSAFE_SecureRandom;
import com.rsa.jsafe.JSAFE_SymmetricCipher;
import com.rsa.jsafe.JSAFE_UnimplementedException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import weblogic.security.shared.LoggerWrapper;

public final class JSafeEncryptionServiceImpl implements EncryptionServiceV2 {
   static final String OVERALL_ALGORITHM = "3DES";
   static final String ALGORITHM_3DES = "3DES_EDE/CBC/PKCS5Padding";
   static final String ALGORITHM_AES = "AES/CBC/PKCS5Padding";
   static final String ALGORITHM_RANDOM = "FIPS186Random";
   private static final String PREFIX_3DES = "{3DES}";
   private static final int RANDOM_LEN_3DES = 0;
   private static final int KEY_LEN_3DES = 168;
   private static final String PREFIX_AES = "{AES}";
   private static final int RANDOM_LEN_AES = 16;
   private static final int KEY_LEN_AES = 128;
   static final String ENCODING = "UTF-8";
   private static LoggerWrapper logger = LoggerWrapper.getInstance("SecurityEncryptionService");
   private JSAFE_SecureRandom randomIV = null;
   private KeyContext keyContext3DES = null;
   private KeyContext keyContextAES = null;
   private HashMap keyContextMap = new HashMap(2);
   private static FIPS140Context NON_FIPS140_CONTEXT;
   private static final int INSTANCE_RANDOM_SEED_SIZE = 32;
   private static final Object seedingLock = new Object();
   private static JSAFE_SecureRandom seedingRandom = null;

   public byte[] encryptBytes(byte[] var1) throws EncryptionServiceException {
      return this.encryptBytes(var1, this.keyContext3DES);
   }

   private byte[] encryptBytes(byte[] var1, KeyContext var2) throws EncryptionServiceException {
      JSAFE_SymmetricCipher var3 = this.getEncryptCipher(var2);
      Object var4 = null;

      byte[] var8;
      try {
         if (logger.isDebugEnabled()) {
            log("starting encrypt operation " + var2.prefix);
         }

         int var5 = var2.randomLen;
         byte[] var15 = new byte[var5 + var3.getOutputBufferSize(var1.length)];
         if (var5 > 0) {
            this.getRandomIV(var15, 0, var5);
            var3.setIV(var15, 0, var5);
            var3.encryptReInit();
         }

         int var6 = var3.encryptUpdate(var1, 0, var1.length, var15, var5);
         var3.encryptFinal(var15, var6 + var5);
         if (logger.isDebugEnabled()) {
            log("done with encrypt operation " + var2.prefix);
         }

         var8 = var15;
      } catch (Exception var13) {
         throw new EncryptionServiceException(var13);
      } finally {
         var3.clearSensitiveData();
      }

      return var8;
   }

   public byte[] decryptBytes(byte[] var1) throws EncryptionServiceException {
      return this.decryptBytes(var1, this.keyContext3DES);
   }

   private byte[] decryptBytes(byte[] var1, KeyContext var2) throws EncryptionServiceException {
      JSAFE_SymmetricCipher var3 = this.getDecryptCipher(var2);
      Object var4 = null;

      byte[] var10;
      try {
         if (logger.isDebugEnabled()) {
            log("starting decrypt operation " + var2.prefix);
         }

         int var5 = var2.randomLen;
         int var6 = var1.length - var5;
         if (var6 < 0) {
            throw new IllegalStateException("Invalid input length");
         }

         byte[] var17 = new byte[var6];
         if (var5 > 0) {
            var3.setIV(var1, 0, var5);
            var3.decryptReInit();
         }

         int var7 = var3.decryptUpdate(var1, var5, var6, var17, 0);
         int var8 = var3.decryptFinal(var17, var7);
         int var9 = var7 + var8;
         if (var9 < var17.length) {
            var10 = new byte[var9];
            System.arraycopy(var17, 0, var10, 0, var9);
            var17 = var10;
         }

         if (logger.isDebugEnabled()) {
            log("done with decrypt operation " + var2.prefix);
         }

         var10 = var17;
      } catch (Exception var15) {
         if (logger.isDebugEnabled()) {
            log("Esception during decrypt operation " + var15.getMessage());
         }

         throw new EncryptionServiceException();
      } finally {
         var3.clearSensitiveData();
      }

      return var10;
   }

   public byte[] encryptString(String var1) throws EncryptionServiceException {
      try {
         return this.encryptBytes(var1.getBytes("UTF-8"));
      } catch (UnsupportedEncodingException var3) {
         throw new EncryptionServiceException(var3);
      }
   }

   public String decryptString(byte[] var1) throws EncryptionServiceException {
      try {
         return new String(this.decryptBytes(var1), "UTF-8");
      } catch (UnsupportedEncodingException var3) {
         throw new EncryptionServiceException(var3);
      }
   }

   public byte[] encryptBytes(String var1, byte[] var2) throws EncryptionServiceException {
      KeyContext var3 = this.getKeyContextFromString(var1);
      return this.encryptBytes(var2, var3);
   }

   public byte[] decryptBytes(String var1, byte[] var2) throws EncryptionServiceException {
      KeyContext var3 = this.getKeyContextFromString(var1);
      return this.decryptBytes(var2, var3);
   }

   public byte[] encryptString(String var1, String var2) throws EncryptionServiceException {
      KeyContext var3 = this.getKeyContextFromString(var1);

      try {
         return this.encryptBytes(var2.getBytes("UTF-8"), var3);
      } catch (UnsupportedEncodingException var5) {
         throw new EncryptionServiceException(var5);
      }
   }

   public String decryptString(String var1, byte[] var2) throws EncryptionServiceException {
      KeyContext var3 = this.getKeyContextFromString(var1);

      try {
         byte[] var4 = this.decryptBytes(var2, var3);
         return new String(var4, "UTF-8");
      } catch (UnsupportedEncodingException var5) {
         throw new EncryptionServiceException(var5);
      }
   }

   public boolean isKeyContextAvailable(String var1) {
      return this.keyContextMap.containsKey(var1);
   }

   public String getDefaultKeyContext() {
      return this.keyContextAES != null ? this.keyContextAES.prefix : this.keyContext3DES.prefix;
   }

   private KeyContext getKeyContextFromString(String var1) {
      KeyContext var2 = (KeyContext)this.keyContextMap.get(var1);
      if (var2 == null) {
         throw new IllegalStateException("KeyContext Unavailable!");
      } else {
         return var2;
      }
   }

   static byte[] createEncryptedSecretKey(String var0, byte[] var1) throws EncryptionServiceException {
      return createEncryptedSecretKey("3DES_EDE/CBC/PKCS5Padding", 168, var0, var1);
   }

   static byte[] createAESEncryptedSecretKey(String var0, byte[] var1) throws EncryptionServiceException {
      return createEncryptedSecretKey("AES/CBC/PKCS5Padding", 128, var0, var1);
   }

   private static byte[] createEncryptedSecretKey(String var0, int var1, String var2, byte[] var3) throws EncryptionServiceException {
      char[] var4 = new char[var2.length()];
      var2.getChars(0, var2.length(), var4, 0);
      JSAFE_SymmetricCipher var5 = null;
      JSAFE_SecureRandom var6 = null;
      JSAFE_SecretKey var7 = null;

      byte[] var10;
      try {
         log("creating new key: " + var0);
         int[] var8 = new int[]{var1};
         var5 = JSAFE_SymmetricCipher.getInstance(var0, "Java");
         var6 = getSeededSecureRandomInstance();
         var7 = var5.getBlankKey();
         var7.generateInit(var8, var6);
         var7.generate();
         log("created new key: " + var7.toString());
         byte[] var9 = JSafeSecretKeyEncryptor.encryptSecretKey(var7, var4, var3);
         log("new key (encrypted) key byte array length: " + var9.length);
         var10 = var9;
      } catch (Exception var16) {
         throw new EncryptionServiceException(var16);
      } finally {
         for(int var13 = 0; var13 < var4.length; ++var13) {
            var4[var13] = 0;
         }

         if (var5 != null) {
            var5.clearSensitiveData();
         }

         if (var6 != null) {
            var6.clearSensitiveData();
         }

         if (var7 != null) {
            var7.clearSensitiveData();
         }

      }

      return var10;
   }

   static byte[] reEncryptSecretKey(String var0, byte[] var1, String var2, byte[] var3, String var4, byte[] var5) throws EncryptionServiceException {
      JSAFE_SecretKey var6 = null;
      char[] var7 = new char[var2.length()];
      var2.getChars(0, var2.length(), var7, 0);
      char[] var8 = new char[var4.length()];
      var4.getChars(0, var4.length(), var8, 0);

      byte[] var9;
      try {
         var6 = JSafeSecretKeyEncryptor.decryptSecretKey(var0, var1, var7, var3);
         var9 = JSafeSecretKeyEncryptor.encryptSecretKey(var6, var8, var5);
      } catch (Exception var15) {
         throw new EncryptionServiceException(var15);
      } finally {
         int var12;
         for(var12 = 0; var12 < var7.length; ++var12) {
            var7[var12] = 0;
         }

         for(var12 = 0; var12 < var8.length; ++var12) {
            var8[var12] = 0;
         }

         if (var6 != null) {
            var6.clearSensitiveData();
         }

      }

      return var9;
   }

   public String getAlgorithm() {
      return "3DES";
   }

   JSafeEncryptionServiceImpl(byte[] var1, byte[] var2, String var3, byte[] var4) throws EncryptionServiceException {
      char[] var5 = new char[var3.length()];
      var3.getChars(0, var3.length(), var5, 0);

      try {
         log("Encryption service constructor called");
         this.create3DESKeyContext(var1, var5, var2);
         if (var4 != null) {
            log("Encryption service AES key avilable");
            var5 = new char[var3.length()];
            var3.getChars(0, var3.length(), var5, 0);
            this.createAESKeyContext(var4, var5, var2);
         }

      } catch (Exception var7) {
         throw new EncryptionServiceException(var7);
      }
   }

   private void create3DESKeyContext(byte[] var1, char[] var2, byte[] var3) throws EncryptionServiceException {
      KeyContext var4 = new KeyContext();
      var4.prefix = "{3DES}";
      var4.algorithm = "3DES_EDE/CBC/PKCS5Padding";
      var4.randomLen = 0;
      var4.salt = JSafeSecretKeyEncryptor.doubleSalt(var3);
      this.createCiphers(var4, var1, var2, var3);
      this.keyContext3DES = var4;
   }

   private void createAESKeyContext(byte[] var1, char[] var2, byte[] var3) throws EncryptionServiceException {
      KeyContext var4 = new KeyContext();
      var4.prefix = "{AES}";
      var4.algorithm = "AES/CBC/PKCS5Padding";
      var4.randomLen = 16;
      var4.salt = null;
      this.createCiphers(var4, var1, var2, var3);
      this.keyContextAES = var4;
   }

   private void createCiphers(KeyContext var1, byte[] var2, char[] var3, byte[] var4) throws EncryptionServiceException {
      JSAFE_SecretKey var5 = null;

      try {
         log("Initializing key: " + var1.prefix);
         var5 = JSafeSecretKeyEncryptor.decryptSecretKey(var1.algorithm, var2, var3, var4);
         log("key: " + var5.toString());
         log("Initializing encrypt cipher");
         var1.encryptCipher = JSAFE_SymmetricCipher.getInstance(var1.algorithm, "Java");
         if (var1.salt != null) {
            var1.encryptCipher.setIV(var1.salt, 0, var1.salt.length);
            log("encrypt IV Length: " + var1.salt.length);
         }

         var1.encryptCipher.encryptInit(var5);
         log("Initializing decrypt cipher");
         var1.decryptCipher = JSAFE_SymmetricCipher.getInstance(var1.algorithm, "Java");
         if (var1.salt != null) {
            byte[] var6 = var1.encryptCipher.getIV();
            var1.decryptCipher.setIV(var6, 0, var6.length);
            log("decrypt IV Length: " + var6.length);
         }

         var1.decryptCipher.decryptInit(var5);
         log("Placing KeyContext into Map: " + var1.prefix);
         this.keyContextMap.put(var1.prefix, var1);
      } catch (Exception var11) {
         throw new EncryptionServiceException(var11);
      } finally {
         if (var5 != null) {
            var5.clearSensitiveData();
         }

      }

   }

   private synchronized void initRandomIV() throws NoSuchAlgorithmException {
      if (this.randomIV == null) {
         this.randomIV = getSeededSecureRandomInstance();
      }

   }

   private synchronized void getRandomIV(byte[] var1, int var2, int var3) throws Exception {
      this.initRandomIV();
      this.randomIV.generateRandomBytes(var1, var2, var3);
   }

   private synchronized JSAFE_SymmetricCipher getEncryptCipher(KeyContext var1) throws EncryptionServiceException {
      JSAFE_SymmetricCipher var2 = null;

      try {
         var2 = (JSAFE_SymmetricCipher)var1.encryptCipher.clone();
         return var2;
      } catch (CloneNotSupportedException var4) {
         throw new EncryptionServiceException(var4);
      }
   }

   private synchronized JSAFE_SymmetricCipher getDecryptCipher(KeyContext var1) throws EncryptionServiceException {
      JSAFE_SymmetricCipher var2 = null;

      try {
         var2 = (JSAFE_SymmetricCipher)var1.decryptCipher.clone();
         return var2;
      } catch (CloneNotSupportedException var4) {
         throw new EncryptionServiceException(var4);
      }
   }

   public static EncryptionServiceFactory getFactory() throws EncryptionServiceException {
      return new JSafeEncryptionServiceFactory();
   }

   public static void log(String var0) {
      if (logger.isDebugEnabled()) {
         logger.debug(System.currentTimeMillis() + " : " + Thread.currentThread().getName() + " : " + var0);
      }

   }

   public static JSAFE_SymmetricCipher getSymmetricCipher(String var0, String var1) throws JSAFE_UnimplementedException, JSAFE_InvalidParameterException {
      JSAFE_SymmetricCipher var2 = null;
      if (getNonFIPS140Ctx() == null) {
         var2 = JSAFE_SymmetricCipher.getInstance(var0, var1);
      } else {
         var2 = JSAFE_SymmetricCipher.getInstance(var0, var1, getNonFIPS140Ctx());
      }

      return var2;
   }

   public static FIPS140Context getNonFIPS140Ctx() {
      if (CryptoJ.isFIPS140Compliant() && NON_FIPS140_CONTEXT == null) {
         try {
            FIPS140Context var0 = CryptoJ.getFIPS140Context();
            NON_FIPS140_CONTEXT = var0.setMode(1);
         } catch (JSAFE_InvalidUseException var1) {
            throw new EncryptionServiceException(var1.getMessage());
         }
      }

      return NON_FIPS140_CONTEXT;
   }

   private static JSAFE_SecureRandom getSeededSecureRandomInstance() throws NoSuchAlgorithmException {
      JSAFE_SecureRandom var0 = (JSAFE_SecureRandom)JSAFE_SecureRandom.getInstance("FIPS186Random", "Java");
      Object var1 = null;
      byte[] var5;
      synchronized(seedingLock) {
         if (seedingRandom == null) {
            seedingRandom = (JSAFE_SecureRandom)JSAFE_SecureRandom.getInstance("FIPS186Random", "Java");
            seedingRandom.autoseed();
         }

         var5 = seedingRandom.generateRandomBytes(32);
      }

      var0.setSeed(var5);
      return var0;
   }

   private class KeyContext {
      public String prefix;
      public String algorithm;
      public int randomLen;
      private byte[] salt;
      private JSAFE_SymmetricCipher encryptCipher;
      private JSAFE_SymmetricCipher decryptCipher;

      private KeyContext() {
      }

      // $FF: synthetic method
      KeyContext(Object var2) {
         this();
      }
   }
}
