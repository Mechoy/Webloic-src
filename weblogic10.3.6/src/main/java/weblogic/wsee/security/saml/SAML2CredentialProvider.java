package weblogic.wsee.security.saml;

public class SAML2CredentialProvider extends AbstractSAMLCredentialProvider {
   public String[] getValueTypes() {
      return new String[]{"http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV2.0", "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLID"};
   }
}
