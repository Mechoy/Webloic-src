package weblogic.xml.security.keyinfo;

import java.math.BigInteger;
import java.security.Key;

public class SecretKeyProvider extends BaseKeyProvider {
   private final Key key;
   private final String[] algs;
   private final KeyPurpose[] purposes;

   public SecretKeyProvider(Key key, String name, byte[] id, String uri) {
      super(name, id, uri);
      if (key == null) {
         throw new IllegalArgumentException("Key cannot be null");
      } else {
         this.key = key;
         this.algs = Utils.getAlgorithms(key);
         this.purposes = KeyPurpose.getPurposes(key);
      }
   }

   public KeyResult getKey(String algorithm, KeyPurpose purpose) {
      return this.serves(purpose) && this.supports(algorithm) ? new KeyResult(this.key) : null;
   }

   public KeyResult getKeyBySubjectName(String subjectName, String alg, KeyPurpose purpose) {
      return null;
   }

   public KeyResult getKeyByIssuerSerial(String issuerName, BigInteger serial, String alg, KeyPurpose purpose) {
      return null;
   }

   protected final boolean supports(String alg) {
      return Utils.supports(this.algs, alg);
   }

   protected final boolean serves(KeyPurpose purpose) {
      return KeyPurpose.serves(this.purposes, purpose);
   }
}
