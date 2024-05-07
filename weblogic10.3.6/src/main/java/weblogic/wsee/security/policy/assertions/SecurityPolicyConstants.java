package weblogic.wsee.security.policy.assertions;

import javax.xml.namespace.QName;

public interface SecurityPolicyConstants {
   String WLS_SECURITY_POLICY_PREFIX = "wssp";
   String WLS_DIALECT_EXT_PREFIX = "wls";
   String WLS_SECURITY_POLICY_ASSERTIONS_URI = "http://www.bea.com/wls90/security/policy";
   String WLS_DIALECT_EXT_URI = "http://www.bea.com/wls90/security/policy/wsee#part";
   QName SECURITY_TOKEN_QNAME = new QName("http://www.bea.com/wls90/security/policy", "SecurityToken");
   QName SUPPORTED_TOKENS_QNAME = new QName("http://www.bea.com/wls90/security/policy", "SupportedTokens");
   QName USE_PASSWD_QNAME = new QName("http://www.bea.com/wls90/security/policy", "UsePassword");
   QName TOKEN_TYPE_QNAME = new QName("TokenType");
   QName USE_PASSWD_TYPE_QNAME = new QName("Type");
   QName INCLUDE_IN_MESSAGE_QNAME = new QName("IncludeInMessage");
   String KEYINFO = "KeyInfo";
}
