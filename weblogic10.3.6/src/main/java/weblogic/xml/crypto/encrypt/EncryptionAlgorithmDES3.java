package weblogic.xml.crypto.encrypt;

import com.rsa.jsafe.JSAFE_Exception;
import com.rsa.jsafe.JSAFE_SecretKey;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.xml.stream.XMLStreamReader;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;

final class EncryptionAlgorithmDES3 extends EncryptionAlgorithm implements WLEncryptionMethodFactory {
   private static final String ALGORITHM_ID = "3DES_EDE/CBC/NoPad";
   private static final String KEY_FACTORY_ID = "DESEDE";
   private static final String JSAFE_KEY_FACTORY_ID = "3DES_EDE";
   private static final String JSAFE_PROVIDER = "Java";
   private static final int IV_LEN = 8;
   private static final int TRIPLE_DES_KEYLENGTH = 21;

   private EncryptionAlgorithmDES3(String var1) {
      super(var1, (Integer)null, (AlgorithmParameterSpec)null);
   }

   private EncryptionAlgorithmDES3(String var1, Integer var2, AlgorithmParameterSpec var3) {
      super(var1, var2, var3);
   }

   public static void init() {
      WLEncryptionMethod.register(new EncryptionAlgorithmDES3("http://www.w3.org/2001/04/xmlenc#tripledes-cbc"));
   }

   public InputStream decrypt(Key var1, InputStream var2) throws XMLEncryptionException {
      byte[] var3 = new byte[8];
      Utils.readIV(var2, var3);
      CipherWrapper var4 = CipherWrapper.getInstance("3DES_EDE/CBC/NoPad", 2, var1, var3);
      return new CipherWrapperInputStream(var2, var4);
   }

   public OutputStream encrypt(Key var1, OutputStream var2) throws XMLEncryptionException {
      CipherWrapper var3 = CipherWrapper.getInstance("3DES_EDE/CBC/NoPad", 1, var1);
      Utils.writeIV(var2, var3.getIV());
      return new CipherWrapperOutputStream(var2, var3);
   }

   public Key createKey(byte[] var1) throws XMLEncryptionException {
      try {
         DESedeKeySpec var2 = new DESedeKeySpec(var1);
         SecretKeyFactory var3 = SecretKeyFactory.getInstance("DESEDE");
         return var3.generateSecret(var2);
      } catch (InvalidKeyException var4) {
         throw new XMLEncryptionException("Invalid key supplied to " + this.getAlgorithm(), var4);
      } catch (NoSuchAlgorithmException var5) {
         throw new XMLEncryptionException("Cannot locate JCE algorithm (DESEDE) necessary for generating a secret key for: " + this.getAlgorithm(), var5);
      } catch (InvalidKeySpecException var6) {
         throw new XMLEncryptionException("Unexpected exception when generating key for: " + this.getAlgorithm(), var6);
      }
   }

   public Key generateKey() throws XMLEncryptionException {
      try {
         JSAFE_SecretKey var1 = JSAFE_SecretKey.getInstance("3DES_EDE", "Java");
         var1.generateInit((int[])null, Utils.getRNG());
         var1.generate();
         return this.createKey(var1.getSecretKeyData());
      } catch (JSAFE_Exception var2) {
         throw new XMLEncryptionException(var2);
      }
   }

   public Key generateKey(byte[] var1, byte[] var2) throws XMLEncryptionException {
      try {
         byte[] var3 = Utils.P_SHA1(var1, var2, 21);
         byte[] var4 = Utils.tripleDESParity(var3);
         return this.createKey(var4);
      } catch (NoSuchAlgorithmException var5) {
         throw new XMLEncryptionException("unable to generate key", var5);
      } catch (InvalidKeyException var6) {
         throw new XMLEncryptionException("unable to generate key", var6);
      }
   }

   public WLEncryptionMethod getEncryptionMethod(AlgorithmParameterSpec var1, Integer var2) {
      return this.getEncryptionAlgorithm(var1, var2);
   }

   public KeyWrap getKeyWrap(AlgorithmParameterSpec var1, Integer var2) {
      throw new UnsupportedOperationException("Algorithm " + this.getAlgorithm() + "does not support key wrapping");
   }

   public EncryptionAlgorithm getEncryptionAlgorithm(AlgorithmParameterSpec var1, Integer var2) {
      return var1 == null && var2 == null ? this : new EncryptionAlgorithmDES3(this.getAlgorithm(), var2, var1);
   }

   public AlgorithmParameterSpec readParameters(XMLStreamReader var1) {
      return null;
   }
}
