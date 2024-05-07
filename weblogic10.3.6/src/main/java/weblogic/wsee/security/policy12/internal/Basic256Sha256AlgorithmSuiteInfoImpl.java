package weblogic.wsee.security.policy12.internal;

public class Basic256Sha256AlgorithmSuiteInfoImpl extends BaseAlgorithmSuiteInfoImpl {
   public String getDigUri() {
      return "http://www.w3.org/2001/04/xmlenc#sha256";
   }

   public String getEncUri() {
      return "http://www.w3.org/2001/04/xmlenc#aes256-cbc";
   }

   public String getSymKwUri() {
      return "http://www.w3.org/2001/04/xmlenc#kw-aes256";
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
      return 256;
   }
}
