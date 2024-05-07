package weblogic.xml.security.keyinfo;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

public class KeypairProvider extends BaseKeyProvider {
   private final String[] algs;
   private final PublicKey pubKey;
   private final PrivateKey privKey;

   public KeypairProvider(PublicKey var1, PrivateKey var2, String var3, byte[] var4, String var5) {
      super(var3, var4, var5);
      if (var1 == null && var2 == null) {
         throw new IllegalArgumentException("Received null for public key and private key");
      } else {
         this.pubKey = var1;
         this.privKey = var2;
         if (var1 != null) {
            this.algs = Utils.getAlgorithms(var1);
         } else {
            this.algs = Utils.getAlgorithms(var2);
         }

      }
   }

   protected KeyResult getResult(Key var1) {
      return new KeyResult(var1);
   }

   public KeyResult getKey(String var1, KeyPurpose var2) {
      if (Utils.supports(this.algs, var1)) {
         if (this.pubKey != null && var2.served(KeyPurpose.getPurposes(this.pubKey))) {
            return this.getResult(this.pubKey);
         }

         if (this.privKey != null && var2.served(KeyPurpose.getPurposes(this.privKey))) {
            return this.getResult(this.privKey);
         }
      }

      return null;
   }
}
