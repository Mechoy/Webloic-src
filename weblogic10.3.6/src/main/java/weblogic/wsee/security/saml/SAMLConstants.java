package weblogic.wsee.security.saml;

import javax.xml.namespace.QName;

public class SAMLConstants {
   public static final String POLICY_URI = "http://www.bea.com/wls90/security/policy";
   public static final String SAML_TOKEN_URI = "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.0";
   public static final String SAML_TOKEN_URI_2004_01 = "http://docs.oasis-open.org/wss/2004/01/oasis-2004-01-saml-token-profile-1.0";
   public static final String SAML_VALUE_TYPE = "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.0#SAMLAssertionID";
   public static final String SAML_VALUE_TYPE_2004_01 = "http://docs.oasis-open.org/wss/2004/01/oasis-2004-01-saml-token-profile-1.0#SAMLAssertionID";
   public static final String[] SAML_VALUE_TYPES = new String[]{"http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV1.1", "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.0#SAMLAssertionID", "http://docs.oasis-open.org/wss/2004/01/oasis-2004-01-saml-token-profile-1.0#SAMLAssertionID"};
   public static final String CONFIRMATION_METHOD = "ConfirmationMethod";
   public static final QName CONFIRMATION_METHOD_QNAME = new QName("http://www.bea.com/wls90/security/policy", "ConfirmationMethod");
   public static final String HOLDER_OF_KEY = "holder-of-key";
   public static final String SENDER_VOUCHES = "sender-vouches";
   public static final String BEARER = "bearer";
   public static final QName SAML_ASST_QNAME = new QName("urn:oasis:names:tc:SAML:1.0:assertion", "Assertion", "saml");
   public static final QName[] SAML_ASST_QNAMES;

   static {
      SAML_ASST_QNAMES = new QName[]{SAML_ASST_QNAME};
   }
}
