package weblogic.xml.crypto.utils;

import java.security.Key;
import java.util.ArrayList;
import weblogic.xml.crypto.api.KeySelector;
import weblogic.xml.crypto.api.KeySelectorException;
import weblogic.xml.crypto.api.KeySelectorResult;
import weblogic.xml.crypto.api.XMLCryptoContext;
import weblogic.xml.crypto.common.keyinfo.KeyProvider;
import weblogic.xml.crypto.dsig.api.keyinfo.KeyInfo;
import weblogic.xml.crypto.encrypt.EncryptionAlgorithm;
import weblogic.xml.crypto.encrypt.api.EncryptionMethod;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;
import weblogic.xml.crypto.wss.WSSecurityContext;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss.provider.SecurityTokenReference;

public class EncryptedKeyUtils {
   public static final Key generateKey(EncryptionMethod var0) {
      Key var1 = null;

      try {
         var1 = ((EncryptionAlgorithm)var0).generateKey();
      } catch (XMLEncryptionException var3) {
      }

      return var1;
   }

   public static final KeyInfo getKeyInfo(WSSecurityContext var0, SecurityTokenReference var1) {
      ArrayList var2 = new ArrayList();
      var2.add(var1);
      KeyInfo var3 = var0.getSignatureFactory().getKeyInfoFactory().newKeyInfo(var2);
      return var3;
   }

   public static final Key getKey(KeySelector var0, KeyProvider var1, EncryptionMethod var2) throws WSSecurityException {
      KeySelectorResult var3 = null;

      try {
         if (var1 != null) {
            var3 = var1.getKey(var2.getAlgorithm(), KeySelector.Purpose.ENCRYPT);
         } else {
            var3 = var0.select((KeyInfo)null, KeySelector.Purpose.ENCRYPT, var2, (XMLCryptoContext)null);
         }
      } catch (KeySelectorException var5) {
         throw new WSSecurityException(var5);
      }

      if (var3 == null) {
         throw new WSSecurityException("Failed to select key for algorithm " + var2.getAlgorithm());
      } else {
         Key var4 = var3.getKey();
         return var4;
      }
   }
}
