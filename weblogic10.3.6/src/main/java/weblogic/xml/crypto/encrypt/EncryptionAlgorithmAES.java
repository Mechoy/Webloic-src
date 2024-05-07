package weblogic.xml.crypto.encrypt;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.stream.XMLStreamReader;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;

final class EncryptionAlgorithmAES extends EncryptionAlgorithm implements WLEncryptionMethodFactory {
   private static final String ALGORITHM_ID = "AES/CBC/NoPadding";
   private static final String KEY_FACTORY_ID = "AES";
   private static final int IV_LEN = 16;
   private int keyLen;

   private EncryptionAlgorithmAES(String var1, int var2) {
      super(var1, (Integer)null, (AlgorithmParameterSpec)null);
      this.keyLen = var2;
   }

   private EncryptionAlgorithmAES(String var1, Integer var2, AlgorithmParameterSpec var3) {
      super(var1, var2, var3);
      this.keyLen = var2;
   }

   static void init() {
      WLEncryptionMethod.register(new EncryptionAlgorithmAES("http://www.w3.org/2001/04/xmlenc#aes128-cbc", 16));
      WLEncryptionMethod.register(new EncryptionAlgorithmAES("http://www.w3.org/2001/04/xmlenc#aes192-cbc", 24));
      WLEncryptionMethod.register(new EncryptionAlgorithmAES("http://www.w3.org/2001/04/xmlenc#aes256-cbc", 32));
   }

   public InputStream decrypt(Key var1, InputStream var2) throws XMLEncryptionException {
      byte[] var3 = new byte[16];
      Utils.readIV(var2, var3);
      CipherWrapper var4 = CipherWrapper.getInstance("AES/CBC/NoPadding", 2, var1, var3);
      return new CipherWrapperInputStream(var2, var4);
   }

   public OutputStream encrypt(Key var1, OutputStream var2) throws XMLEncryptionException {
      CipherWrapper var3 = CipherWrapper.getInstance("AES/CBC/NoPadding", 1, var1);
      Utils.writeIV(var2, var3.getIV());
      return new CipherWrapperOutputStream(var2, var3);
   }

   public Key createKey(byte[] var1) {
      return new SecretKeySpec(var1, "AES");
   }

   public Key generateKey() {
      byte[] var1 = new byte[this.keyLen];
      rand.nextBytes(var1);
      return this.createKey(var1);
   }

   public Key generateKey(byte[] var1, byte[] var2) throws XMLEncryptionException {
      try {
         byte[] var3 = Utils.P_SHA1(var1, var2, this.keyLen);
         return this.createKey(var3);
      } catch (NoSuchAlgorithmException var4) {
         throw new XMLEncryptionException("unable to generate key", var4);
      } catch (InvalidKeyException var5) {
         throw new XMLEncryptionException("unable to generate key", var5);
      }
   }

   public EncryptionAlgorithm getEncryptionAlgorithm(AlgorithmParameterSpec var1, Integer var2) {
      return var1 == null && var2 == null ? this : new EncryptionAlgorithmAES(this.getAlgorithm(), var2, var1);
   }

   public KeyWrap getKeyWrap(AlgorithmParameterSpec var1, Integer var2) {
      throw new UnsupportedOperationException("Algorithm " + this.getAlgorithm() + " cannot be used for key wrapping");
   }

   public AlgorithmParameterSpec readParameters(XMLStreamReader var1) {
      return null;
   }

   public WLEncryptionMethod getEncryptionMethod(AlgorithmParameterSpec var1, Integer var2) {
      return this.getEncryptionAlgorithm(var1, var2);
   }
}
