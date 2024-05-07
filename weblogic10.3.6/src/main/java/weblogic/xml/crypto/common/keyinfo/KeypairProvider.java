package weblogic.xml.crypto.common.keyinfo;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import weblogic.xml.crypto.api.KeySelector;
import weblogic.xml.crypto.api.KeySelectorResult;
import weblogic.xml.crypto.utils.KeyUtils;
import weblogic.xml.crypto.wss.provider.SecurityToken;

public class KeypairProvider extends BaseKeyProvider {
   private final String[] algs;
   private final PublicKey pubKey;
   private final PrivateKey privKey;

   public KeypairProvider(PublicKey var1, PrivateKey var2, String var3, byte[] var4, String var5) {
      this(var1, var2, var3, var4, var5, (SecurityToken)null);
   }

   public KeypairProvider(PublicKey var1, PrivateKey var2, String var3, byte[] var4, String var5, SecurityToken var6) {
      super(var3, var4, var5, var6);
      if (var1 == null && var2 == null) {
         throw new IllegalArgumentException("Received null for public key and private key");
      } else {
         this.pubKey = var1;
         this.privKey = var2;
         if (var1 != null) {
            this.algs = KeyUtils.getAlgorithms(var1);
         } else {
            this.algs = KeyUtils.getAlgorithms(var2);
         }

      }
   }

   protected KeySelectorResult getResult(Key var1) {
      return new KeySelectorResultImpl(var1);
   }

   public KeySelectorResult getKey(String var1, KeySelector.Purpose var2) {
      if (KeyUtils.supports(this.algs, var1)) {
         if (this.pubKey != null && KeyUtils.serves(KeyUtils.getPurposes(this.pubKey), var2)) {
            return this.getResult(this.pubKey);
         }

         if (this.privKey != null && KeyUtils.serves(KeyUtils.getPurposes(this.privKey), var2)) {
            return this.getResult(this.privKey);
         }
      }

      return null;
   }
}
