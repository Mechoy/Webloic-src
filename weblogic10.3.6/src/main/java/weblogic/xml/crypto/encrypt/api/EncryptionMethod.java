package weblogic.xml.crypto.encrypt.api;

import weblogic.xml.crypto.api.AlgorithmMethod;
import weblogic.xml.crypto.api.XMLStructure;

public interface EncryptionMethod extends AlgorithmMethod, XMLStructure {
   String AES128_CBC = "http://www.w3.org/2001/04/xmlenc#aes128-cbc";
   String AES192_CBC = "http://www.w3.org/2001/04/xmlenc#aes192-cbc";
   String AES256_CBC = "http://www.w3.org/2001/04/xmlenc#aes256-cbc";
   String TRIPLEDES_CBC = "http://www.w3.org/2001/04/xmlenc#tripledes-cbc";
   String KW_AES128 = "http://www.w3.org/2001/04/xmlenc#kw-aes128";
   String KW_AES192 = "http://www.w3.org/2001/04/xmlenc#kw-aes192";
   String KW_AES256 = "http://www.w3.org/2001/04/xmlenc#kw-aes256";
   String KW_TRIPLEDES = "http://www.w3.org/2001/04/xmlenc#kw-tripledes";
   String RSA_1_5 = "http://www.w3.org/2001/04/xmlenc#rsa-1_5";
   String RSA_OAEP_MGFLP = "http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p";

   Integer getKeySize();
}
