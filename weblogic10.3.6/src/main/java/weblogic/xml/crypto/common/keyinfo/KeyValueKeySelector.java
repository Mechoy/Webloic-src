package weblogic.xml.crypto.common.keyinfo;

import java.security.Key;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Iterator;
import weblogic.xml.crypto.api.AlgorithmMethod;
import weblogic.xml.crypto.api.KeySelector;
import weblogic.xml.crypto.api.KeySelectorException;
import weblogic.xml.crypto.api.KeySelectorResult;
import weblogic.xml.crypto.api.XMLCryptoContext;
import weblogic.xml.crypto.dsig.api.keyinfo.KeyInfo;
import weblogic.xml.crypto.dsig.keyinfo.KeyInfoImpl;

public class KeyValueKeySelector extends KeySelector {
   public KeySelectorResult select(KeyInfo var1, KeySelector.Purpose var2, AlgorithmMethod var3, XMLCryptoContext var4) throws KeySelectorException {
      Object var5 = null;
      Iterator var6 = ((KeyInfoImpl)var1).getPublicKeys();

      while(var6.hasNext()) {
         Object var7 = var6.next();
         if (var7 instanceof DSAPublicKey && "http://www.w3.org/2000/09/xmldsig#dsa-sha1".equals(var3.getAlgorithm())) {
            var5 = (DSAPublicKey)var7;
            break;
         }

         if (var7 instanceof RSAPublicKey && "http://www.w3.org/2000/09/xmldsig#rsa-sha1".equals(var3.getAlgorithm())) {
            var5 = (RSAPublicKey)var7;
            break;
         }
      }

      return new KeySelectorResultImpl((Key)var5);
   }
}
