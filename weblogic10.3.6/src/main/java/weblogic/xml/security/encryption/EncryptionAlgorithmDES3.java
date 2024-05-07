package weblogic.xml.security.encryption;

import com.rsa.jsafe.JSAFE_Exception;
import com.rsa.jsafe.JSAFE_SecretKey;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

public final class EncryptionAlgorithmDES3 extends EncryptionAlgorithm implements EncryptionMethodFactory {
   static final String URI = "http://www.w3.org/2001/04/xmlenc#tripledes-cbc";
   private static final String ALGORITHM_ID = "3DES_EDE/CBC/NoPad";
   private static final String KEY_FACTORY_ID = "DESEDE";
   private static final String JSAFE_KEY_FACTORY_ID = "3DES_EDE";
   private static final String JSAFE_PROVIDER = "Java";
   private static final int IV_LEN = 8;
   private final String uri;
   private static final int TRIPLE_DES_KEYLENGTH = 21;

   private EncryptionAlgorithmDES3(String var1) {
      this.uri = var1;
   }

   public String getURI() {
      return this.uri;
   }

   public static void init() {
      EncryptionMethod.register(new EncryptionAlgorithmDES3("http://www.w3.org/2001/04/xmlenc#tripledes-cbc"));
   }

   public EncryptionMethod newEncryptionMethod() {
      return this;
   }

   public InputStream decrypt(Key var1, InputStream var2) throws EncryptionException {
      byte[] var3 = new byte[8];
      Utils.readIV(var2, var3);
      CipherWrapper var4 = CipherWrapper.getInstance("3DES_EDE/CBC/NoPad", 2, var1, var3);
      return new CipherWrapperInputStream(var2, var4);
   }

   public OutputStream encrypt(Key var1, OutputStream var2) throws EncryptionException {
      CipherWrapper var3 = CipherWrapper.getInstance("3DES_EDE/CBC/NoPad", 1, var1);
      Utils.writeIV(var2, var3.getIV());
      return new CipherWrapperOutputStream(var2, var3);
   }

   public Key createKey(byte[] var1) throws EncryptionException {
      try {
         DESedeKeySpec var2 = new DESedeKeySpec(var1);
         SecretKeyFactory var3 = SecretKeyFactory.getInstance("DESEDE");
         return var3.generateSecret(var2);
      } catch (InvalidKeyException var4) {
         throw new EncryptionException("Invalid key supplied to " + this.getURI(), var4);
      } catch (NoSuchAlgorithmException var5) {
         throw new EncryptionException("Cannot locate JCE algorithm (DESEDE) necessary for generating a secret key for: " + this.getURI(), var5);
      } catch (InvalidKeySpecException var6) {
         throw new EncryptionException("Unexpected exception when generating key for: " + this.getURI(), var6);
      }
   }

   public Key generateKey() throws EncryptionException {
      try {
         JSAFE_SecretKey var1 = JSAFE_SecretKey.getInstance("3DES_EDE", "Java");
         var1.generateInit((int[])null, Utils.getRNG());
         var1.generate();
         return this.createKey(var1.getSecretKeyData());
      } catch (JSAFE_Exception var2) {
         throw new EncryptionException(var2);
      }
   }

   public Key generateKey(byte[] var1, byte[] var2) throws EncryptionException {
      try {
         byte[] var3 = Utils.P_SHA1(var1, var2, 21);
         byte[] var4 = Utils.tripleDESParity(var3);
         return this.createKey(var4);
      } catch (NoSuchAlgorithmException var5) {
         throw new EncryptionException("unable to generate key", var5);
      } catch (InvalidKeyException var6) {
         throw new EncryptionException("unable to generate key", var6);
      }
   }
}
