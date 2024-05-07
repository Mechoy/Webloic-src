package weblogic.xml.crypto.encrypt;

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
import weblogic.kernel.Kernel;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;

public abstract class CipherWrapper {
   public static final int DECRYPT = 2;
   public static final int ENCRYPT = 1;
   private static final SecureRandom rand = new SecureRandom();
   private static final String JSAFE_PROVIDER = "Java";
   private static Set jceFailures = Collections.synchronizedSet(new HashSet());

   public static CipherWrapper getInstance(String var0, int var1, Key var2) throws XMLEncryptionException {
      return getInstance(var0, var1, var2, (byte[])null);
   }

   public static CipherWrapper getInstance(String var0, int var1, Key var2, byte[] var3) throws XMLEncryptionException {
      return new JSafeCipherWrapper(var0, var1, var2, var3);
   }

   public byte[] decrypt(byte[] var1) throws XMLEncryptionException {
      return this.decrypt(var1, 0, var1.length);
   }

   public byte[] decrypt(byte[] var1, int var2, int var3) throws XMLEncryptionException {
      byte[] var4 = new byte[this.getOutputSize(var3)];
      int var5 = this.update(var1, var2, var3, var4, 0);
      var5 += this.doFinal(var4, var5);
      if (var5 < var4.length) {
         byte[] var6 = new byte[var5];
         System.arraycopy(var4, 0, var6, 0, var5);
         var4 = var6;
      }

      return var4;
   }

   public byte[] encrypt(byte[] var1) throws XMLEncryptionException {
      return this.doFinal(var1);
   }

   public abstract int update(byte[] var1, int var2, int var3, byte[] var4, int var5) throws XMLEncryptionException;

   public abstract int doFinal(byte[] var1, int var2) throws XMLEncryptionException;

   public abstract byte[] doFinal() throws XMLEncryptionException;

   public abstract byte[] doFinal(byte[] var1) throws XMLEncryptionException;

   public abstract int getBlockSize();

   public abstract int getOutputSize(int var1);

   public abstract byte[] getIV() throws XMLEncryptionException;

   static {
      if (!Kernel.isServer() && Security.getProperty("com.rsa.crypto.default.random") == null && !Boolean.getBoolean("weblogic.security.allowCryptoJDefaultPRNG")) {
         Security.setProperty("com.rsa.crypto.default.random", "FIPS186PRNG");
      }

   }

   private static class JSafeCipherWrapper extends CipherWrapper {
      private JSAFE_SymmetricCipher symmetricCipher;
      private JSAFE_AsymmetricCipher asymmetricCipher;
      private final int mode;

      JSafeCipherWrapper(String var1, int var2, Key var3, byte[] var4) throws XMLEncryptionException {
         this.mode = var2;
         this.init(var1, var3, var4);
      }

      private void init(String var1, Key var2, byte[] var3) throws XMLEncryptionException {
         try {
            if (var2 instanceof PrivateKey) {
               if (this.mode != 2) {
                  throw new XMLEncryptionException("Invalid key for decryption");
               }

               this.asymmetricCipher = JSAFE_AsymmetricCipher.getInstance(var1, "Java");
               JSAFE_PrivateKey var4 = JSAFE_PrivateKey.getInstance(var2.getEncoded(), 0, "Java");
               this.asymmetricCipher.decryptInit(var4);
            } else if (var2 instanceof PublicKey) {
               if (this.mode != 1) {
                  throw new XMLEncryptionException("Invalid key for encryption");
               }

               this.asymmetricCipher = JSAFE_AsymmetricCipher.getInstance(var1, "Java");
               JSAFE_PublicKey var10 = JSAFE_PublicKey.getInstance(var2.getEncoded(), 0, "Java");
               this.asymmetricCipher.encryptInit(var10, JSAFE_SecureRandom.getInstance("FIPS186Random", "Java"));
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
            throw new XMLEncryptionException(var8);
         } catch (NoSuchAlgorithmException var9) {
            throw new XMLEncryptionException(var9);
         }
      }

      public int update(byte[] var1, int var2, int var3, byte[] var4, int var5) throws XMLEncryptionException {
         try {
            if (this.symmetricCipher != null) {
               return this.mode == 2 ? this.symmetricCipher.decryptUpdate(var1, var2, var3, var4, var5) : this.symmetricCipher.encryptUpdate(var1, var2, var3, var4, var5);
            } else {
               return this.mode == 2 ? this.asymmetricCipher.decryptUpdate(var1, var2, var3, var4, var5) : this.asymmetricCipher.encryptUpdate(var1, var2, var3, var4, var5);
            }
         } catch (JSAFE_Exception var7) {
            throw new XMLEncryptionException(var7);
         }
      }

      public int doFinal(byte[] var1, int var2) throws XMLEncryptionException {
         try {
            if (this.symmetricCipher != null) {
               return this.mode == 2 ? this.symmetricCipher.decryptFinal(var1, var2) : this.symmetricCipher.encryptFinal(var1, var2);
            } else {
               return this.mode == 2 ? this.asymmetricCipher.decryptFinal(var1, var2) : this.asymmetricCipher.encryptFinal(var1, var2);
            }
         } catch (JSAFE_Exception var4) {
            throw new XMLEncryptionException(var4);
         }
      }

      public byte[] doFinal() throws XMLEncryptionException {
         try {
            if (this.symmetricCipher != null) {
               return this.mode == 2 ? this.symmetricCipher.decryptFinal() : this.symmetricCipher.encryptFinal();
            } else {
               return this.mode == 2 ? this.asymmetricCipher.decryptFinal() : this.asymmetricCipher.encryptFinal();
            }
         } catch (JSAFE_Exception var2) {
            throw new XMLEncryptionException(var2);
         }
      }

      public byte[] doFinal(byte[] var1) throws XMLEncryptionException {
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
            throw new XMLEncryptionException(var5);
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

      public int getOutputSize(int var1) {
         int var2;
         if (this.symmetricCipher != null) {
            var2 = this.symmetricCipher.getOutputBufferSize(var1);
         } else {
            var2 = this.asymmetricCipher.getOutputBufferSize(var1);
         }

         return var2;
      }

      public byte[] getIV() throws XMLEncryptionException {
         if (this.symmetricCipher != null) {
            byte[] var1 = this.symmetricCipher.getIV();
            if (var1 == null) {
               try {
                  this.symmetricCipher.generateIV(CipherWrapper.rand);
               } catch (JSAFE_Exception var3) {
                  throw new XMLEncryptionException(var3);
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

      public int update(byte[] var1, int var2, int var3, byte[] var4, int var5) throws XMLEncryptionException {
         try {
            return this.cipher.update(var1, var2, var3, var4, var5);
         } catch (ShortBufferException var7) {
            throw new XMLEncryptionException(var7);
         }
      }

      public int doFinal(byte[] var1, int var2) throws XMLEncryptionException {
         try {
            return this.cipher.doFinal(var1, var2);
         } catch (GeneralSecurityException var4) {
            throw new XMLEncryptionException(var4);
         }
      }

      public byte[] doFinal() throws XMLEncryptionException {
         try {
            return this.cipher.doFinal();
         } catch (GeneralSecurityException var2) {
            throw new XMLEncryptionException(var2);
         }
      }

      public byte[] doFinal(byte[] var1) throws XMLEncryptionException {
         try {
            return this.cipher.doFinal(var1);
         } catch (GeneralSecurityException var3) {
            throw new XMLEncryptionException(var3);
         }
      }

      public int getBlockSize() {
         return this.cipher.getBlockSize();
      }

      public int getOutputSize(int var1) {
         return this.cipher.getOutputSize(var1);
      }

      public byte[] getIV() throws XMLEncryptionException {
         return this.cipher.getIV();
      }
   }
}
