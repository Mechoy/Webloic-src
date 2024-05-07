package weblogic.xml.security.encryption;

import com.rsa.jsafe.JSAFE_AsymmetricCipher;
import com.rsa.jsafe.JSAFE_Exception;
import com.rsa.jsafe.JSAFE_PrivateKey;
import com.rsa.jsafe.JSAFE_PublicKey;
import com.rsa.jsafe.JSAFE_SecretKey;
import com.rsa.jsafe.JSAFE_SecureRandom;
import com.rsa.jsafe.JSAFE_SymmetricCipher;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.crypto.Cipher;
import javax.crypto.ShortBufferException;

public abstract class CipherWrapper {
   public static final int DECRYPT = 2;
   public static final int ENCRYPT = 1;
   private static final SecureRandom rand = new SecureRandom();
   private static final String JSAFE_PROVIDER = "Java";
   private static Set jceFailures = Collections.synchronizedSet(new HashSet());

   public static CipherWrapper getInstance(String var0, int var1, Key var2) throws EncryptionException {
      return getInstance(var0, var1, var2, (byte[])null);
   }

   public static CipherWrapper getInstance(String var0, int var1, Key var2, byte[] var3) throws EncryptionException {
      return new JSafeCipherWrapper(var0, var1, var2, var3);
   }

   public byte[] decrypt(byte[] var1) throws EncryptionException {
      return this.decrypt(var1, 0, var1.length);
   }

   public byte[] decrypt(byte[] var1, int var2, int var3) throws EncryptionException {
      int var4 = this.update(var1, var2, var3, var1, 0);
      var4 += this.doFinal(var1, var4);
      if (var4 < var1.length) {
         byte[] var5 = new byte[var4];
         System.arraycopy(var1, 0, var5, 0, var4);
         var1 = var5;
      }

      return var1;
   }

   public byte[] encrypt(byte[] var1) throws EncryptionException {
      return this.doFinal(var1);
   }

   public abstract int update(byte[] var1, int var2, int var3, byte[] var4, int var5) throws EncryptionException;

   public abstract int doFinal(byte[] var1, int var2) throws EncryptionException;

   public abstract byte[] doFinal() throws EncryptionException;

   public abstract byte[] doFinal(byte[] var1) throws EncryptionException;

   public abstract int getBlockSize();

   public abstract byte[] getIV() throws EncryptionException;

   static {
      String var0 = Security.getProperty("com.rsa.crypto.default.random");
      if ((var0 == null || var0.trim().length() == 0) && !Boolean.getBoolean("weblogic.security.allowCryptoJDefaultPRNG")) {
         Security.setProperty("com.rsa.crypto.default.random", "FIPS186PRNG");
      }

   }

   private static class JSafeCipherWrapper extends CipherWrapper {
      private JSAFE_SymmetricCipher symmetricCipher;
      private JSAFE_AsymmetricCipher asymmetricCipher;
      private final int mode;

      JSafeCipherWrapper(String var1, int var2, Key var3, byte[] var4) throws EncryptionException {
         this.mode = var2;
         this.init(var1, var3, var4);
      }

      private void init(String var1, Key var2, byte[] var3) throws EncryptionException {
         try {
            if (var2 instanceof PrivateKey) {
               if (this.mode != 2) {
                  throw new EncryptionException("Invalid key for decryption");
               }

               this.asymmetricCipher = JSAFE_AsymmetricCipher.getInstance(var1, "Java");
               JSAFE_PrivateKey var4 = JSAFE_PrivateKey.getInstance(var2.getEncoded(), 0, "Java");
               this.asymmetricCipher.decryptInit(var4);
            } else if (var2 instanceof PublicKey) {
               if (this.mode != 1) {
                  throw new EncryptionException("Invalid key for encryption");
               }

               this.asymmetricCipher = JSAFE_AsymmetricCipher.getInstance(var1, "Java");
               JSAFE_PublicKey var10 = JSAFE_PublicKey.getInstance(var2.getEncoded(), 0, "Java");
               this.asymmetricCipher.encryptInit(var10, JSAFE_SecureRandom.getInstance("SHA1Random", "Java"));
            } else {
               int var11 = var1.indexOf("/");
               String var5 = var11 != -1 ? var1.substring(0, var11) : var1;
               JSAFE_SecretKey var6 = JSAFE_SecretKey.getInstance(var5, "Java");
               byte[] var7 = var2.getEncoded();
               var6.setSecretKeyData(var7, 0, var7.length);
               this.symmetricCipher = JSAFE_SymmetricCipher.getInstance(var1, "Java");
               if (var3 != null) {
                  this.symmetricCipher.setIV(var3, 0, var3.length);
               }

               if (this.mode == 2) {
                  this.symmetricCipher.decryptInit(var6);
               } else {
                  this.symmetricCipher.encryptInit(var6);
               }
            }

         } catch (JSAFE_Exception var8) {
            throw new EncryptionException(var8);
         } catch (NoSuchAlgorithmException var9) {
            throw new EncryptionException(var9);
         }
      }

      public int update(byte[] var1, int var2, int var3, byte[] var4, int var5) throws EncryptionException {
         try {
            if (this.symmetricCipher != null) {
               return this.mode == 2 ? this.symmetricCipher.decryptUpdate(var1, var2, var3, var4, var5) : this.symmetricCipher.encryptUpdate(var1, var2, var3, var4, var5);
            } else {
               return this.mode == 2 ? this.asymmetricCipher.decryptUpdate(var1, var2, var3, var4, var5) : this.asymmetricCipher.encryptUpdate(var1, var2, var3, var4, var5);
            }
         } catch (JSAFE_Exception var7) {
            throw new EncryptionException(var7);
         }
      }

      public int doFinal(byte[] var1, int var2) throws EncryptionException {
         try {
            if (this.symmetricCipher != null) {
               return this.mode == 2 ? this.symmetricCipher.decryptFinal(var1, var2) : this.symmetricCipher.encryptFinal(var1, var2);
            } else {
               return this.mode == 2 ? this.asymmetricCipher.decryptFinal(var1, var2) : this.asymmetricCipher.encryptFinal(var1, var2);
            }
         } catch (JSAFE_Exception var4) {
            throw new EncryptionException(var4);
         }
      }

      public byte[] doFinal() throws EncryptionException {
         try {
            if (this.symmetricCipher != null) {
               return this.mode == 2 ? this.symmetricCipher.decryptFinal() : this.symmetricCipher.encryptFinal();
            } else {
               return this.mode == 2 ? this.asymmetricCipher.decryptFinal() : this.asymmetricCipher.encryptFinal();
            }
         } catch (JSAFE_Exception var2) {
            throw new EncryptionException(var2);
         }
      }

      public byte[] doFinal(byte[] var1) throws EncryptionException {
         byte[] var2;
         byte[] var3;
         try {
            if (this.symmetricCipher != null) {
               if (this.mode == 2) {
                  var2 = this.symmetricCipher.decryptUpdate(var1, 0, var1.length);
                  var3 = this.symmetricCipher.decryptFinal();
               } else {
                  var2 = this.symmetricCipher.encryptUpdate(var1, 0, var1.length);
                  var3 = this.symmetricCipher.encryptFinal();
               }
            } else if (this.mode == 2) {
               var2 = this.asymmetricCipher.decryptUpdate(var1, 0, var1.length);
               var3 = this.asymmetricCipher.decryptFinal();
            } else {
               var2 = this.asymmetricCipher.encryptUpdate(var1, 0, var1.length);
               var3 = this.asymmetricCipher.encryptFinal();
            }
         } catch (JSAFE_Exception var5) {
            throw new EncryptionException(var5);
         }

         byte[] var4 = new byte[var2.length + var3.length];
         System.arraycopy(var2, 0, var4, 0, var2.length);
         System.arraycopy(var3, 0, var4, var2.length, var3.length);
         return var4;
      }

      public int getBlockSize() {
         if (this.symmetricCipher != null) {
            return this.symmetricCipher.getBlockSize();
         } else {
            return this.mode == 2 ? this.asymmetricCipher.getOutputBlockSize() : this.asymmetricCipher.getInputBlockSize();
         }
      }

      public byte[] getIV() throws EncryptionException {
         if (this.symmetricCipher != null) {
            byte[] var1 = this.symmetricCipher.getIV();
            if (var1 == null) {
               try {
                  this.symmetricCipher.generateIV(CipherWrapper.rand);
               } catch (JSAFE_Exception var3) {
                  throw new EncryptionException(var3);
               }

               var1 = this.symmetricCipher.getIV();
            }

            return var1;
         } else {
            return null;
         }
      }
   }

   private static class JCECipherWrapper extends CipherWrapper {
      private final Cipher cipher;

      JCECipherWrapper(Cipher var1) {
         this.cipher = var1;
      }

      public int update(byte[] var1, int var2, int var3, byte[] var4, int var5) throws EncryptionException {
         try {
            return this.cipher.update(var1, var2, var3, var4, var5);
         } catch (ShortBufferException var7) {
            throw new EncryptionException(var7);
         }
      }

      public int doFinal(byte[] var1, int var2) throws EncryptionException {
         try {
            return this.cipher.doFinal(var1, var2);
         } catch (GeneralSecurityException var4) {
            throw new EncryptionException(var4);
         }
      }

      public byte[] doFinal() throws EncryptionException {
         try {
            return this.cipher.doFinal();
         } catch (GeneralSecurityException var2) {
            throw new EncryptionException(var2);
         }
      }

      public byte[] doFinal(byte[] var1) throws EncryptionException {
         try {
            return this.cipher.doFinal(var1);
         } catch (GeneralSecurityException var3) {
            throw new EncryptionException(var3);
         }
      }

      public int getBlockSize() {
         return this.cipher.getBlockSize();
      }

      public byte[] getIV() throws EncryptionException {
         return this.cipher.getIV();
      }
   }
}
