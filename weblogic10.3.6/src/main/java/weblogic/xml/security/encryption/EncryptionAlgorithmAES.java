package weblogic.xml.security.encryption;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.spec.SecretKeySpec;

final class EncryptionAlgorithmAES extends EncryptionAlgorithm implements EncryptionMethodFactory {
   static final String URI_128 = "http://www.w3.org/2001/04/xmlenc#aes128-cbc";
   static final String URI_192 = "http://www.w3.org/2001/04/xmlenc#aes192-cbc";
   static final String URI_256 = "http://www.w3.org/2001/04/xmlenc#aes256-cbc";
   private static final String ALGORITHM_ID = "AES/CBC/NoPadding";
   private static final String KEY_FACTORY_ID = "AES";
   private static final int IV_LEN = 16;
   private final String uri;
   private final int keyLen;

   private EncryptionAlgorithmAES(String var1, int var2) {
      this.uri = var1;
      this.keyLen = var2;
   }

   public String getURI() {
      return this.uri;
   }

   static void init() {
      EncryptionMethod.register(new EncryptionAlgorithmAES("http://www.w3.org/2001/04/xmlenc#aes128-cbc", 16));
      EncryptionMethod.register(new EncryptionAlgorithmAES("http://www.w3.org/2001/04/xmlenc#aes192-cbc", 24));
      EncryptionMethod.register(new EncryptionAlgorithmAES("http://www.w3.org/2001/04/xmlenc#aes256-cbc", 32));
   }

   public EncryptionMethod newEncryptionMethod() {
      return this;
   }

   public InputStream decrypt(Key var1, InputStream var2) throws EncryptionException {
      byte[] var3 = new byte[16];
      Utils.readIV(var2, var3);
      CipherWrapper var4 = CipherWrapper.getInstance("AES/CBC/NoPadding", 2, var1, var3);
      return new CipherWrapperInputStream(var2, var4);
   }

   public OutputStream encrypt(Key var1, OutputStream var2) throws EncryptionException {
      CipherWrapper var3 = CipherWrapper.getInstance("AES/CBC/NoPadding", 1, var1);
      Utils.writeIV(var2, var3.getIV());
      return new CipherWrapperOutputStream(var2, var3);
   }

   public Key createKey(byte[] var1) throws EncryptionException {
      return new SecretKeySpec(var1, "AES");
   }

   public Key generateKey() throws EncryptionException {
      byte[] var1 = new byte[this.keyLen];
      rand.nextBytes(var1);
      return this.createKey(var1);
   }

   public Key generateKey(byte[] var1, byte[] var2) throws EncryptionException {
      try {
         if (var2 != null) {
            byte[] var3 = Utils.P_SHA1(var1, var2, this.keyLen);
            return this.createKey(var3);
         } else {
            return this.createKey(var1);
         }
      } catch (NoSuchAlgorithmException var4) {
         throw new EncryptionException("unable to generate key", var4);
      } catch (InvalidKeyException var5) {
         throw new EncryptionException("unable to generate key", var5);
      }
   }
}
