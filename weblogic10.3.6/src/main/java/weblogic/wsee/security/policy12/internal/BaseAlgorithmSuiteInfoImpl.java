package weblogic.wsee.security.policy12.internal;

import weblogic.wsee.security.wssp.AlgorithmSuiteInfo;

public abstract class BaseAlgorithmSuiteInfoImpl implements AlgorithmSuiteInfo {
   protected static final String HMAC_SHA1 = "http://www.w3.org/2000/09/xmldsig#hmac-sha1";
   protected static final String RSA_SHA1 = "http://www.w3.org/2000/09/xmldsig#rsa-sha1";
   protected static final String P_SHA1 = "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/dk/p_sha1";
   protected static final String SHA1 = "http://www.w3.org/2000/09/xmldsig#sha1";
   protected static final String SHA256 = "http://www.w3.org/2001/04/xmlenc#sha256";
   protected static final String SHA512 = "http://www.w3.org/2001/04/xmlenc#sha512";
   protected static final String AES128 = "http://www.w3.org/2001/04/xmlenc#aes128-cbc";
   protected static final String AES192 = "http://www.w3.org/2001/04/xmlenc#aes192-cbc";
   protected static final String AES256 = "http://www.w3.org/2001/04/xmlenc#aes256-cbc";
   protected static final String TRIPLE_DES = "http://www.w3.org/2001/04/xmlenc#tripledes-cbc";
   protected static final String KW_AES_128 = "http://www.w3.org/2001/04/xmlenc#kw-aes128";
   protected static final String KW_AES_192 = "http://www.w3.org/2001/04/xmlenc#kw-aes192";
   protected static final String KW_AES_256 = "http://www.w3.org/2001/04/xmlenc#kw-aes256";
   protected static final String KW_TRIPLE_DES = "http://www.w3.org/2001/04/xmlenc#kw-tripledes";
   protected static final String KW_RSA_OAEP = "http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p";
   protected static final String KW_RSA_15 = "http://www.w3.org/2001/04/xmlenc#rsa-1_5";
   protected static final String PSHA_1_L128 = "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/dk/p_sha1";
   protected static final String PSHA_1_L192 = "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/dk/p_sha1";
   protected static final String PSHA_1_L256 = "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/dk/p_sha1";
   protected static final String XPATH = "http://www.w3.org/TR/1999/REC-xpath-19991116";
   protected static final String XPATH20 = "http://www.w3.org/2002/06/xmldsig-filter2";
   protected static final String C14N = "http://www.w3.org/2001/10/xml-c14n#";
   protected static final String EXC14N = "http://www.w3.org/2001/10/xml-exc-c14n#";
   protected static final String SNT = "http://www.w3.org/TR/soap12-n11n";
   protected static final String STRT10 = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#STR-Transform";

   public String getSymSigUri() {
      return "http://www.w3.org/2000/09/xmldsig#hmac-sha1";
   }

   public String getAsymSigUri() {
      return "http://www.w3.org/2000/09/xmldsig#rsa-sha1";
   }

   public String getCompKeyUri() {
      return "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/dk/p_sha1";
   }

   public int getMaxSymKeyLength() {
      return 256;
   }

   public int getMinAsymKeyLength() {
      return 1024;
   }

   public int getMaxAsymKeyLength() {
      return 4096;
   }

   public String getC14nAlgUri() {
      return "http://www.w3.org/2001/10/xml-exc-c14n#";
   }
}
