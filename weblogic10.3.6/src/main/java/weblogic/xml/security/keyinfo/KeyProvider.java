package weblogic.xml.security.keyinfo;

import java.math.BigInteger;

public interface KeyProvider {
   String[] RSA_ALGORITHMS = new String[]{"http://www.w3.org/2001/04/xmlenc#rsa-1_5", "http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p", "http://www.w3.org/2000/09/xmldsig#rsa-sha1"};
   String[] DSA_ALGORITHMS = new String[]{"http://www.w3.org/2000/09/xmldsig#dsa-sha1"};
   String[] AES_ALGORITHMS = new String[]{"http://www.w3.org/2001/04/xmlenc#aes128-cbc", "http://www.w3.org/2001/04/xmlenc#aes192-cbc", "http://www.w3.org/2001/04/xmlenc#aes256-cbc", "http://www.w3.org/2001/04/xmlenc#kw-aes128", "http://www.w3.org/2001/04/xmlenc#kw-aes192", "http://www.w3.org/2001/04/xmlenc#kw-aes256", "http://www.w3.org/2000/09/xmldsig#hmac-sha1"};
   String[] TRIPLEDES_ALGORITHMS = new String[]{"http://www.w3.org/2001/04/xmlenc#tripledes-cbc", "http://www.w3.org/2001/04/xmlenc#kw-tripledes", "http://www.w3.org/2000/09/xmldsig#hmac-sha1"};

   KeyResult getKey(String var1, KeyPurpose var2);

   KeyResult getKeyByName(String var1, String var2, KeyPurpose var3);

   KeyResult getKeyByIdentifier(byte[] var1, String var2, KeyPurpose var3);

   KeyResult getKeyByURI(String var1, String var2, KeyPurpose var3);

   KeyResult getKeyBySubjectName(String var1, String var2, KeyPurpose var3);

   KeyResult getKeyByIssuerSerial(String var1, BigInteger var2, String var3, KeyPurpose var4);
}
