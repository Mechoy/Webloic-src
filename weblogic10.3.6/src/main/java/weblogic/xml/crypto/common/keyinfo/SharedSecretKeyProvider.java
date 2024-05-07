package weblogic.xml.crypto.common.keyinfo;

import java.math.BigInteger;
import java.security.Key;
import java.util.Map;
import weblogic.utils.collections.ConcurrentHashMap;
import weblogic.xml.crypto.api.KeySelector;
import weblogic.xml.crypto.api.KeySelectorResult;
import weblogic.xml.security.encryption.EncryptionAlgorithm;
import weblogic.xml.security.encryption.EncryptionException;
import weblogic.xml.security.encryption.EncryptionMethod;

public class SharedSecretKeyProvider extends BaseKeyProvider {
   private static final Map keyFactories = new ConcurrentHashMap();
   private final String secret;
   private final byte[] nonce;

   public SharedSecretKeyProvider(String var1, byte[] var2, String var3, byte[] var4, String var5) {
      super(var3, var4, var5);
      this.secret = var1;
      this.nonce = var2;
   }

   public KeySelectorResult getKey(String var1, KeySelector.Purpose var2) {
      EncryptionAlgorithm var3 = (EncryptionAlgorithm)keyFactories.get(var1);
      KeySelectorResult var4 = null;
      if (var3 != null) {
         try {
            Key var5 = var3.generateKey(this.secret.getBytes(), this.nonce);
            var4 = this.getResult(var5);
         } catch (EncryptionException var6) {
         }
      }

      return var4;
   }

   protected KeySelectorResult getResult(Key var1) {
      return new KeySelectorResultImpl(var1);
   }

   public KeySelectorResult getKeyBySubjectName(String var1, String var2, KeySelector.Purpose var3) {
      return null;
   }

   public KeySelectorResult getKeyByIssuerSerial(String var1, BigInteger var2, String var3, KeySelector.Purpose var4) {
      return null;
   }

   static {
      EncryptionAlgorithm var0;
      try {
         var0 = (EncryptionAlgorithm)EncryptionMethod.get("http://www.w3.org/2001/04/xmlenc#aes128-cbc");
         keyFactories.put("http://www.w3.org/2001/04/xmlenc#aes128-cbc", var0);
         keyFactories.put("http://www.w3.org/2001/04/xmlenc#kw-aes128", var0);
      } catch (EncryptionException var5) {
      }

      try {
         var0 = (EncryptionAlgorithm)EncryptionMethod.get("http://www.w3.org/2001/04/xmlenc#aes192-cbc");
         keyFactories.put("http://www.w3.org/2001/04/xmlenc#aes192-cbc", var0);
         keyFactories.put("http://www.w3.org/2001/04/xmlenc#kw-aes192", var0);
      } catch (EncryptionException var4) {
      }

      try {
         var0 = (EncryptionAlgorithm)EncryptionMethod.get("http://www.w3.org/2001/04/xmlenc#aes256-cbc");
         keyFactories.put("http://www.w3.org/2001/04/xmlenc#aes256-cbc", var0);
         keyFactories.put("http://www.w3.org/2001/04/xmlenc#kw-aes256", var0);
         keyFactories.put("http://www.w3.org/2000/09/xmldsig#hmac-sha1", var0);
      } catch (EncryptionException var3) {
      }

      try {
         var0 = (EncryptionAlgorithm)EncryptionMethod.get("http://www.w3.org/2001/04/xmlenc#tripledes-cbc");
         keyFactories.put("http://www.w3.org/2001/04/xmlenc#tripledes-cbc", var0);
         keyFactories.put("http://www.w3.org/2001/04/xmlenc#kw-tripledes", var0);
      } catch (EncryptionException var2) {
      }

   }
}
