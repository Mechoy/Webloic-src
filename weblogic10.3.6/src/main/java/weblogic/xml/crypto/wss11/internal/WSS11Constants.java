package weblogic.xml.crypto.wss11.internal;

import javax.xml.namespace.QName;

public class WSS11Constants {
   public static final String XMLNS_WSS11 = "http://docs.oasis-open.org/wss/oasis-wss-wssecurity-secext-1.1.xsd";
   public static final String PREFIX_WSS11 = "wsse11";
   public static final String WSS11_BASE_URI = "http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1";
   public static final String ENC_KEY_VALUE_TYPE = "http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#EncryptedKeySHA1";
   public static final String ENC_KEY_TOKEN_TYPE = "http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#EncryptedKey";
   public static final String THUMBPRINT_URI = "http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#ThumbprintSHA1";
   public static final String SIG_CONF_ELEMENT = "SignatureConfirmation";
   public static final QName SIG_CONF_QNAME = new QName("http://docs.oasis-open.org/wss/oasis-wss-wssecurity-secext-1.1.xsd", "SignatureConfirmation");
   public static final String VALUE_ATTR = "Value";
   public static final QName VALUE_QNAME = new QName((String)null, "Value");
   public static final String ENC_HEADER_ELEMENT = "EncryptedHeader";
   public static final QName ENC_HEADER_QNAME = new QName("http://docs.oasis-open.org/wss/oasis-wss-wssecurity-secext-1.1.xsd", "EncryptedHeader");
   public static final String TOKEN_TYPE_ATTR = "TokenType";
   public static final QName TOKEN_TYPE_QNAME = new QName("http://docs.oasis-open.org/wss/oasis-wss-wssecurity-secext-1.1.xsd", "TokenType");
   public static final String[] ENCRYPTED_KEY_VALUE_TYPES = new String[]{"http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#EncryptedKeySHA1", "http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#EncryptedKey"};
}
