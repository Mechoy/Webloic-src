package weblogic.wsee.security.saml;

import javax.xml.namespace.QName;

public class SAML2TokenHandler extends AbstractSAMLTokenHandler {
   public QName[] getQNames() {
      return SAML2Constants.SAML2_ASST_QNAMES;
   }

   public String[] getValueTypes() {
      return new String[]{"http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV2.0", "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLID"};
   }

   public boolean isSupportedTokenType(String var1) {
      return var1.equals("http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV2.0");
   }

   public boolean isSupportedValueType(String var1) {
      return var1.equals("http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLID");
   }

   public boolean isSaml2() {
      return true;
   }
}
