package weblogic.wsee.security.saml;

public class SAMLCredentialProvider extends AbstractSAMLCredentialProvider {
   public String[] getValueTypes() {
      return SAMLConstants.SAML_VALUE_TYPES;
   }
}
