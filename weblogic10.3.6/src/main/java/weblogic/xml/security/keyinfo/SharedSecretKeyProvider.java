package weblogic.xml.security.keyinfo;

import java.math.BigInteger;
import java.security.Key;
import java.util.Map;
import weblogic.utils.collections.ConcurrentHashMap;
import weblogic.xml.security.encryption.EncryptionAlgorithm;
import weblogic.xml.security.encryption.EncryptionException;
import weblogic.xml.security.encryption.EncryptionMethod;

public class SharedSecretKeyProvider extends BaseKeyProvider {
   private static final Map keyFactories = new ConcurrentHashMap();
   private final String secret;
   private final byte[] nonce;

   public SharedSecretKeyProvider(String secret, byte[] nonce, String name, byte[] id, String uri) {
      super(name, id, uri);
      this.secret = secret;
      this.nonce = nonce;
   }

   public KeyResult getKey(String alg, KeyPurpose purpose) {
      EncryptionAlgorithm ealg = (EncryptionAlgorithm)keyFactories.get(alg);
      KeyResult result = null;
      if (ealg != null) {
         try {
            Key key = ealg.generateKey(this.secret.getBytes(), this.nonce);
            result = this.getResult(key);
         } catch (EncryptionException var6) {
         }
      }

      return result;
   }

   protected KeyResult getResult(Key key) {
      return new KeyResult(key);
   }

   public KeyResult getKeyBySubjectName(String subjectName, String alg, KeyPurpose purpose) {
      return null;
   }

   public KeyResult getKeyByIssuerSerial(String issuerName, BigInteger serial, String alg, KeyPurpose purpose) {
      return null;
   }

   static {
      EncryptionAlgorithm alg;
      try {
         alg = (EncryptionAlgorithm)EncryptionMethod.get("http://www.w3.org/2001/04/xmlenc#aes128-cbc");
         keyFactories.put("http://www.w3.org/2001/04/xmlenc#aes128-cbc", alg);
         keyFactories.put("http://www.w3.org/2001/04/xmlenc#kw-aes128", alg);
      } catch (EncryptionException var5) {
      }

      try {
         alg = (EncryptionAlgorithm)EncryptionMethod.get("http://www.w3.org/2001/04/xmlenc#aes192-cbc");
         keyFactories.put("http://www.w3.org/2001/04/xmlenc#aes192-cbc", alg);
         keyFactories.put("http://www.w3.org/2001/04/xmlenc#kw-aes192", alg);
      } catch (EncryptionException var4) {
      }

      try {
         alg = (EncryptionAlgorithm)EncryptionMethod.get("http://www.w3.org/2001/04/xmlenc#aes256-cbc");
         keyFactories.put("http://www.w3.org/2001/04/xmlenc#aes256-cbc", alg);
         keyFactories.put("http://www.w3.org/2001/04/xmlenc#kw-aes256", alg);
         keyFactories.put("http://www.w3.org/2000/09/xmldsig#hmac-sha1", alg);
      } catch (EncryptionException var3) {
      }

      try {
         alg = (EncryptionAlgorithm)EncryptionMethod.get("http://www.w3.org/2001/04/xmlenc#tripledes-cbc");
         keyFactories.put("http://www.w3.org/2001/04/xmlenc#tripledes-cbc", alg);
         keyFactories.put("http://www.w3.org/2001/04/xmlenc#kw-tripledes", alg);
      } catch (EncryptionException var2) {
      }

   }
}
