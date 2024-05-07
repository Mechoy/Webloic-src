package weblogic.wsee.security.policy12.internal;

public class Basic128Sha256AlgorithmSuiteInfoImpl extends BaseAlgorithmSuiteInfoImpl {
   public String getDigUri() {
      return "http://www.w3.org/2001/04/xmlenc#sha256";
   }

   public String getEncUri() {
      return "http://www.w3.org/2001/04/xmlenc#aes128-cbc";
   }

   public String getSymKwUri() {
      return "http://www.w3.org/2001/04/xmlenc#kw-aes128";
   }

   public String getAsymKwUri() {
      return "http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p";
   }

   public String getSigKdUri() {
      return "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/dk/p_sha1";
   }

   public String getEncKdUri() {
      return "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/dk/p_sha1";
   }

   public int getMinSymKeyLength() {
      return 128;
   }
}
