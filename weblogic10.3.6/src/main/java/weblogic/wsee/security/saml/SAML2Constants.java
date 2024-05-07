package weblogic.wsee.security.saml;

import javax.xml.namespace.QName;

public interface SAML2Constants {
   String SAML_VERSION_11 = "1.1";
   String SAML_VERSION_20 = "2.0";
   String SAML11 = "SAMLV1.1";
   String SAML20 = "SAMLV2.0";
   String SAML_TOKEN10_URI = "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.0";
   String SAML_TOKEN11_URI = "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1";
   String SAML10_VALUE_TYPE = "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.0#SAMLAssertionID";
   String SAML11_VALUE_TYPE = "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLID";
   String SAML11_TOKEN_TYPE = "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV1.1";
   String SAML20_TOKEN_TYPE = "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV2.0";
   String SAML10_TOKEN_TYPE = "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.0#SAMLV1.1";
   String[] SAML_TOKEN_TYPES = new String[]{"http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV1.1", "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV2.0"};
   String[] SAML_VALUE_TYPES = new String[]{"http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.0#SAMLAssertionID", "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV1.1", "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV2.0"};
   String CONFIRMATION_METHOD = "ConfirmationMethod";
   String SAML2_NS = "urn:oasis:names:tc:SAML:2.0:assertion";
   String SAML2_PREFIX = "saml2";
   String BEARER = "bearer";
   String HOLDER_OF_KEY = "holder-of-key";
   String SENDER_VOUCHES = "sender-vouches";
   String SAML10CM = "urn:oasis:names:tc:SAML:1.0:cm:";
   String SAML10_BEARER = "urn:oasis:names:tc:SAML:1.0:cm:bearer";
   String SAML10_SENDER_VOUCHES = "urn:oasis:names:tc:SAML:1.0:cm:sender-vouches";
   String SAML10_HOLDER_OF_KEY = "urn:oasis:names:tc:SAML:1.0:cm:holder-of-key";
   String SAML20CM = "urn:oasis:names:tc:SAML:2.0:cm:";
   String SAML20_BEARER = "urn:oasis:names:tc:SAML:2.0:cm:bearer";
   String SAML20_SENDER_VOUCHES = "urn:oasis:names:tc:SAML:2.0:cm:sender-vouches";
   String SAML20_HOLDER_OF_KEY = "urn:oasis:names:tc:SAML:2.0:cm:holder-of-key";
   QName SAML2_ASST_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "Assertion", "saml2");
   QName[] SAML2_ASST_QNAMES = new QName[]{SAML2_ASST_QNAME};
   String WSS_SAML11_TOKEN10 = "WssSamlV11Token10";
   String WSS_SAML11_TOKEN11 = "WssSamlV11Token11";
   String WSS_SAML20_TOKEN11 = "WssSamlV20Token11";
}
