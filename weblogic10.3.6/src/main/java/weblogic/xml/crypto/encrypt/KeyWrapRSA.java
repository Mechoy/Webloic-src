package weblogic.xml.crypto.encrypt;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import javax.xml.stream.XMLStreamReader;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;

final class KeyWrapRSA extends KeyWrap implements WLEncryptionMethodFactory {
   private static final String ALGORITHM_ID = "RSA";
   private static final String KEY_FACTORY_ID = "RSA";

   private KeyWrapRSA() {
      super("http://www.w3.org/2001/04/xmlenc#rsa-1_5", (Integer)null, (AlgorithmParameterSpec)null);
   }

   private KeyWrapRSA(Integer var1, AlgorithmParameterSpec var2) {
      super("http://www.w3.org/2001/04/xmlenc#rsa-1_5", var1, var2);
   }

   public static void init() {
      WLEncryptionMethod.register(new KeyWrapRSA());
   }

   public byte[] decrypt(Key var1, byte[] var2) throws XMLEncryptionException {
      CipherWrapper var3 = CipherWrapper.getInstance("RSA", 2, var1);
      return var3.decrypt(var2);
   }

   public byte[] encrypt(Key var1, byte[] var2) throws XMLEncryptionException {
      CipherWrapper var3 = CipherWrapper.getInstance("RSA", 1, var1);
      return var3.encrypt(var2);
   }

   public WLEncryptionMethod getEncryptionMethod(AlgorithmParameterSpec var1, Integer var2) {
      return this.getKeyWrap(var1, var2);
   }

   public KeyWrap getKeyWrap(AlgorithmParameterSpec var1, Integer var2) {
      return var1 == null && var2 == null ? this : new KeyWrapRSA(var2, var1);
   }

   public EncryptionAlgorithm getEncryptionAlgorithm(AlgorithmParameterSpec var1, Integer var2) {
      throw new UnsupportedOperationException("Algorithm " + this.getAlgorithm() + " cannot be used for bulk encryption");
   }

   public AlgorithmParameterSpec readParameters(XMLStreamReader var1) {
      return null;
   }
}
