package weblogic.xml.security.encryption;

import java.security.Key;

public class KeyWrapRSA extends KeyWrap implements KeyWrapFactory {
   public static final String URI = "http://www.w3.org/2001/04/xmlenc#rsa-1_5";
   private static final String ALGORITHM_ID = "RSA";
   private static final String KEY_FACTORY_ID = "RSA";
   private final String uri;

   private KeyWrapRSA(String var1) {
      this.uri = var1;
   }

   public String getURI() {
      return this.uri;
   }

   public String getAlgorithm() {
      return "RSA";
   }

   public static void init() {
      EncryptionMethod.register(new KeyWrapRSA("http://www.w3.org/2001/04/xmlenc#rsa-1_5"));
   }

   public EncryptionMethod newEncryptionMethod() {
      return this;
   }

   public KeyWrap newKeyWrap() {
      return this;
   }

   public byte[] unwrap(Key var1, byte[] var2) throws EncryptionException {
      CipherWrapper var3 = CipherWrapper.getInstance("RSA", 2, var1);
      return var3.decrypt(var2);
   }

   public byte[] wrap(Key var1, byte[] var2) throws EncryptionException {
      CipherWrapper var3 = CipherWrapper.getInstance("RSA", 1, var1);
      return var3.encrypt(var2);
   }
}
