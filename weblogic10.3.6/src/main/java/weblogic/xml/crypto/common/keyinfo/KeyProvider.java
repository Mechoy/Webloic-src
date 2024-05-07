package weblogic.xml.crypto.common.keyinfo;

import java.math.BigInteger;
import weblogic.xml.crypto.api.KeySelector;
import weblogic.xml.crypto.api.KeySelectorResult;
import weblogic.xml.crypto.wss.provider.SecurityTokenReference;

public interface KeyProvider {
   String[] RSA_ALGORITHMS = new String[]{"http://www.w3.org/2001/04/xmlenc#rsa-1_5", "http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p", "http://www.w3.org/2000/09/xmldsig#rsa-sha1"};
   String[] DSA_ALGORITHMS = new String[]{"http://www.w3.org/2000/09/xmldsig#dsa-sha1"};
   String[] AES_ALGORITHMS = new String[]{"http://www.w3.org/2001/04/xmlenc#aes128-cbc", "http://www.w3.org/2001/04/xmlenc#aes192-cbc", "http://www.w3.org/2001/04/xmlenc#aes256-cbc", "http://www.w3.org/2001/04/xmlenc#kw-aes128", "http://www.w3.org/2001/04/xmlenc#kw-aes192", "http://www.w3.org/2001/04/xmlenc#kw-aes256", "http://www.w3.org/2000/09/xmldsig#hmac-sha1"};
   String[] TRIPLEDES_ALGORITHMS = new String[]{"http://www.w3.org/2001/04/xmlenc#tripledes-cbc", "http://www.w3.org/2001/04/xmlenc#kw-tripledes", "http://www.w3.org/2000/09/xmldsig#hmac-sha1"};

   KeySelectorResult getKey(String var1, KeySelector.Purpose var2);

   KeySelectorResult getKeyByName(String var1, String var2, KeySelector.Purpose var3);

   KeySelectorResult getKeyByIdentifier(byte[] var1, String var2, KeySelector.Purpose var3);

   KeySelectorResult getKeyByURI(String var1, String var2, KeySelector.Purpose var3);

   KeySelectorResult getKeyBySubjectName(String var1, String var2, KeySelector.Purpose var3);

   KeySelectorResult getKeyByIssuerSerial(String var1, BigInteger var2, String var3, KeySelector.Purpose var4);

   KeySelectorResult getKeyBySTR(SecurityTokenReference var1, String var2, KeySelector.Purpose var3);
}
