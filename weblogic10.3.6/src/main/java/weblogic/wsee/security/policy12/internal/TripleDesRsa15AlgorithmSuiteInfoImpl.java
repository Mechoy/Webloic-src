package weblogic.wsee.security.policy12.internal;

public class TripleDesRsa15AlgorithmSuiteInfoImpl extends BaseAlgorithmSuiteInfoImpl {
   public String getDigUri() {
      return "http://www.w3.org/2000/09/xmldsig#sha1";
   }

   public String getEncUri() {
      return "http://www.w3.org/2001/04/xmlenc#tripledes-cbc";
   }

   public String getSymKwUri() {
      return "http://www.w3.org/2001/04/xmlenc#kw-tripledes";
   }

   public String getAsymKwUri() {
      return "http://www.w3.org/2001/04/xmlenc#rsa-1_5";
   }

   public String getSigKdUri() {
      return "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/dk/p_sha1";
   }

   public String getEncKdUri() {
      return "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/dk/p_sha1";
   }

   public int getMinSymKeyLength() {
      return 192;
   }
}
