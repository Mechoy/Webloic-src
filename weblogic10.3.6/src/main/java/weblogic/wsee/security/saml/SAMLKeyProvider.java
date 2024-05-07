package weblogic.wsee.security.saml;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.api.KeySelector;
import weblogic.xml.crypto.api.KeySelectorResult;
import weblogic.xml.crypto.common.keyinfo.BaseKeyProvider;
import weblogic.xml.crypto.common.keyinfo.KeySelectorResultImpl;
import weblogic.xml.crypto.utils.KeyUtils;
import weblogic.xml.crypto.wss.provider.SecurityToken;

public class SAMLKeyProvider extends BaseKeyProvider {
   private String[] algs;
   private PublicKey publicKey;
   private PrivateKey privateKey;
   private static final boolean verbose = Verbose.isVerbose(SAMLKeyProvider.class);

   public SAMLKeyProvider(PublicKey var1, PrivateKey var2, String var3, SecurityToken var4) {
      super((String)null, var3.getBytes(), (String)("#" + var3), var4);
      if (verbose) {
         Verbose.log((Object)("Public key is: " + (var1 == null ? "null" : "not null")));
         Verbose.log((Object)("Private key is: " + (var2 == null ? "null" : "not null")));
         Verbose.log((Object)("Assertion ID is: " + var3));
         Verbose.log((Object)("SecurityToken is: " + var4));
      }

      this.publicKey = var1;
      this.privateKey = var2;
      if (this.publicKey != null) {
         this.algs = KeyUtils.getAlgorithms(this.publicKey);
      } else {
         this.algs = KeyUtils.getAlgorithms(var2);
      }

   }

   public KeySelectorResult getKey(String var1, KeySelector.Purpose var2) {
      if (KeyUtils.supports(this.algs, var1)) {
         if (this.privateKey != null && KeyUtils.serves(KeyUtils.getPurposes(this.privateKey), var2)) {
            return this.getResult(this.privateKey);
         }

         if (this.publicKey != null && KeyUtils.serves(KeyUtils.getPurposes(this.publicKey), var2)) {
            return this.getResult(this.publicKey);
         }
      }

      return null;
   }

   protected KeySelectorResult getResult(Key var1) {
      KeySelectorResultImpl var2 = new KeySelectorResultImpl(var1);
      var2.setSecurityToken(this.getSecurityToken());
      return var2;
   }
}
