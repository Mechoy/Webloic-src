package weblogic.wsee.security.saml;

import javax.xml.namespace.QName;

public class SAMLTokenHandler extends AbstractSAMLTokenHandler {
   public QName[] getQNames() {
      return SAMLConstants.SAML_ASST_QNAMES;
   }

   public String[] getValueTypes() {
      return SAMLConstants.SAML_VALUE_TYPES;
   }

   public boolean isSupportedTokenType(String var1) {
      for(int var2 = 0; var2 < SAMLConstants.SAML_VALUE_TYPES.length; ++var2) {
         if (SAMLConstants.SAML_VALUE_TYPES[var2].equals(var1)) {
            return true;
         }
      }

      return false;
   }

   public boolean isSupportedValueType(String var1) {
      return var1.equals("http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.0#SAMLAssertionID") || var1.equals("http://docs.oasis-open.org/wss/2004/01/oasis-2004-01-saml-token-profile-1.0#SAMLAssertionID");
   }

   public boolean isSaml2() {
      return false;
   }
}
