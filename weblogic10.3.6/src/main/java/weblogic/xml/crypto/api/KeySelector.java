package weblogic.xml.crypto.api;

import java.security.Key;
import weblogic.xml.crypto.dsig.api.keyinfo.KeyInfo;

public abstract class KeySelector {
   private Key key;

   protected KeySelector() {
   }

   private KeySelector(Key var1) {
      this.key = var1;
   }

   public abstract KeySelectorResult select(KeyInfo var1, Purpose var2, AlgorithmMethod var3, XMLCryptoContext var4) throws KeySelectorException;

   public static KeySelector singletonKeySelector(final Key var0) {
      return new KeySelector() {
         public KeySelectorResult select(KeyInfo var1, Purpose var2, AlgorithmMethod var3, XMLCryptoContext var4) {
            return new KeySelectorResult() {
               public Key getKey() {
                  return var0;
               }
            };
         }
      };
   }

   public static class Purpose {
      private String purpose;
      public static Purpose DECRYPT = new Purpose("decrypt");
      public static Purpose ENCRYPT = new Purpose("encrypt");
      public static Purpose SIGN = new Purpose("sign");
      public static Purpose VERIFY = new Purpose("verify");

      private Purpose(String var1) {
         this.purpose = var1;
      }

      public String toString() {
         return this.purpose;
      }
   }
}
