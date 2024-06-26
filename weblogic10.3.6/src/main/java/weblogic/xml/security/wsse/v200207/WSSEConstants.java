package weblogic.xml.security.wsse.v200207;

import weblogic.xml.security.utils.ElementFactory;
import weblogic.xml.security.wsu.v200207.WSUConstants;
import weblogic.xml.stream.XMLName;

public interface WSSEConstants {
   String WSSE_URI_PROPERTY = "weblogic.webservice.namespace.wsse";
   String DEFAULT_URI = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
   String OASIS_INTEROP_URI = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
   String PROVIDED_URI = System.getProperty("weblogic.webservice.namespace.wsse");
   String WSSE_URI = PROVIDED_URI == null ? "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd" : PROVIDED_URI;
   String USERNAME_TOKEN_URI_PROP = "weblogic.webservice.namespace.usernametoken";
   String DEFAULT_USERNAME_TOKEN_URI = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0";
   String USERNAME_TOKEN_URI = System.getProperty("weblogic.webservice.namespace.usernametoken", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0");
   String X509_URI_PROP = "weblogic.webservice.namespace.x509";
   String DEFAULT_X509_URI = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0";
   String X509_URI = System.getProperty("weblogic.webservice.namespace.x509", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0");
   String WSSE_PREFIX_URI_PROP = "weblogic.webservice.namespace.wsseuri";
   String DEFAULT_WSSE_PREFIX_URI = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0";
   String WSSE_PREFIX_URI = System.getProperty("weblogic.webservice.namespace.wsseuri", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0");
   String DEFAULT_PREFIX = "wsse";
   String DEFAULT_ENCODING = "UTF-8";
   String ATTR_VALUETYPE = "ValueType";
   String ATTR_ENCODING = "EncodingType";
   String ATTR_TYPE = "Type";
   String ATTR_URI = "URI";
   XMLName QNAME_ATTR_VALUETYPE = ElementFactory.createXMLName("ValueType");
   String ATTR_ROLE = "role";
   String ATTR_MUST_UNDERSTAND = "mustUnderstand";
   String FAULT_UNSUPPORTEDSECURITYTOKEN = "UnsupportedSecurityToken";
   String FAULT_UNSUPPORTEDALGORITHM = "UnsupportedAlgorithm";
   String FAULT_INVALIDSECURITY = "InvalidSecurity";
   String FAULT_INVALIDSECURITYTOKEN = "InvalidSecurityToken";
   String FAULT_FAILEDAUTHENTICATION = "FailedAuthentication";
   String FAULT_FAILEDCHECK = "FailedCheck";
   String FAULT_SECURITYTOKENUNAVAILBLE = "SecurityTokenUnavailable";
   XMLName QNAME_FAULT_UNSUPPORTEDSECURITYTOKEN = ElementFactory.createXMLName(WSSE_URI, "UnsupportedSecurityToken");
   XMLName QNAME_FAULT_UNSUPPORTEDALGORITHM = ElementFactory.createXMLName(WSSE_URI, "UnsupportedAlgorithm");
   XMLName QNAME_FAULT_INVALIDSECURITY = ElementFactory.createXMLName(WSSE_URI, "InvalidSecurity");
   XMLName QNAME_FAULT_INVALIDSECURITYTOKEN = ElementFactory.createXMLName(WSSE_URI, "InvalidSecurityToken");
   XMLName QNAME_FAULT_FAILEDAUTHENTICATION = ElementFactory.createXMLName(WSSE_URI, "FailedAuthentication");
   XMLName QNAME_FAULT_FAILEDCHECK = ElementFactory.createXMLName(WSSE_URI, "FailedCheck");
   XMLName QNAME_FAULT_SECURITYTOKENUNAVAILBLE = ElementFactory.createXMLName(WSSE_URI, "SecurityTokenUnavailable");
   String VALUETYPE_X509SKID = X509_URI + "#X509SubjectKeyIdentifier";
   String VALUETYPE_X509V3 = X509_URI + "#X509v3";
   String VALUETYPE_PKCS7 = X509_URI + "#PKCS7";
   String VALUETYPE_PKIPATH = X509_URI + "#X509PKIPathv1";
   String ENCODING_BASE64 = WSSE_PREFIX_URI + "#Base64Binary";
   String PASSWORDTYPE_PASSWORDTEXT = USERNAME_TOKEN_URI + "#PasswordText";
   String PASSWORDTYPE_PASSWORDDIGEST = USERNAME_TOKEN_URI + "#PasswordDigest";
   XMLName QNAME_PASSWORDTYPE_PASSWORDTEXT = ElementFactory.createXMLName(WSSE_URI, "PasswordText", "wsse");
   String TAG_BINARY_SECURITY_TOKEN = "BinarySecurityToken";
   String TAG_KEY_IDENTIFIER = "KeyIdentifier";
   String TAG_PASSWORD = "Password";
   String TAG_REFERENCE = "Reference";
   String TAG_SECURITY = "Security";
   String TAG_SECURITY_TOKEN_REFERENCE = "SecurityTokenReference";
   String TAG_USERNAME = "Username";
   String TAG_USERNAME_TOKEN = "UsernameToken";
   String TAG_NONCE = "Nonce";
   int TC_BINARY_SECURITY_TOKEN = 0;
   int TC_KEY_IDENTIFIER = 1;
   int TC_PASSWORD = 2;
   int TC_REFERENCE = 3;
   int TC_SECURITY = 4;
   int TC_SECURITY_TOKEN_REFERNCE = 5;
   int TC_USERNAME = 6;
   int TC_USERNAME_TOKEN = 7;
   String C14N_INCLUSIVE_PROPERTY = "weblogic.xml.security.wsse.inclusive";
   boolean C14N_INCLUSIVE_NAMESPACES = !"false".equals(System.getProperty("weblogic.xml.security.wsse.inclusive"));
   String SIGN_BST_PROPERTY = "weblogic.xml.security.wsse.signToken";
   boolean SIGN_BST = Boolean.getBoolean("weblogic.xml.security.wsse.signToken");
   String[] ID_NAMESPACES = new String[]{WSUConstants.WSU_URI, "http://www.w3.org/2000/09/xmldsig#", "http://www.w3.org/2001/04/xmlenc#", WSSE_URI};
}
