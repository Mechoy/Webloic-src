package weblogic.wsee.security.wssc.base;

import javax.xml.namespace.QName;

public class WSCConstantsBase {
   public static final String PREFIX_WSC = "wsc";
   public static final String SCT_IDENTIFIER = "Identifier";
   public static final String SCT_INSTANCE = "Instance";
   public static final String DK_ELEMENT = "DerivedKeyToken";
   public static final String DK_ALGORITHM_ATTR = "Algorithm";
   public static final QName DK_ALGORITHM_QNAME = new QName("Algorithm");
   public static final String DK_PROPERTIES_ELEMENT = "Properties";
   public static final String DK_GENERATION_ELEMENT = "Generation";
   public static final String DK_OFFSET_ELEMENT = "Offset";
   public static final String DK_LENGTH_ELEMENT = "Length";
   public static final String DK_LABEL_ELEMENT = "Label";
   public static final String DK_NONCE_ELEMENT = "Nonce";
   public static final String DK_DEFAULT_LABEL = "WS-SecureConversation";
   public static final String DK_DEFAULT_LABEL_CORRECT = "WS-SecureConversationWS-SecureConversation";
   public static final int DK_DEFAULT_LENGTH = 32;
   public static final int DK_DEFAULT_OFFSET = 0;
   public static final String DK_P_SHA1 = "/dk/p_sha1";
   public static final String AES_ALGORITHM = "AES";
   public static final String NET_SCT_COOKIE_LOCALNAME = "Cookie";
   public static final String NET_SCT_COOKIE_PREFIX = "wlsNetCookie";
   public static final String UTIL_NAMESPACE_URI = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd";
   public static final String DELAYED_SCTOKEN_CANCEL = "DELAYED_SCTOKEN_CANCEL";
   public static final int DEFAULT_LIFE_MINUTE_TIME = 1;
   public static final int DEFAULT_CHECKING_TOLERANT_SECOND_TIME = 60;
   public static final String NEED_CHECKING_SCT_EXPIRATION = "weblogic.wsee.security.wssc.needCheckSCTExpiration";
}
