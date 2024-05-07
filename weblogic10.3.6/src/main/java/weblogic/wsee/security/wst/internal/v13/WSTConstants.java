package weblogic.wsee.security.wst.internal.v13;

import javax.xml.namespace.QName;

public final class WSTConstants extends weblogic.wsee.security.wst.framework.WSTConstants {
   public static final String XMLNS_TRUST = "http://docs.oasis-open.org/ws-sx/ws-trust/200512";
   public static final String PREFIX_WST = "wst";
   public static final String TRUST_ACTION_ROOT = "http://docs.oasis-open.org/ws-sx/ws-trust/200512";
   public static final String KEY_TYPE_PUBLIC = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/PublicKey";
   public static final String KEY_TYPE_SYM = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/SymmetricKey";
   public static final String KEY_TYPE_BEARER = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/Bearer";
   public static final QName T13_TOKEN_TYPE = new QName("http://docs.oasis-open.org/ws-sx/ws-trust/200512", "TokenType");
   public static final QName T13_KEY_TYPE = new QName("http://docs.oasis-open.org/ws-sx/ws-trust/200512", "KeyType");
   public static final QName T13_KEY_SIZE = new QName("http://docs.oasis-open.org/ws-sx/ws-trust/200512", "KeySize");
   public static final QName T13_C14N_ALGO = new QName("http://docs.oasis-open.org/ws-sx/ws-trust/200512", "CanonicalizationAlgorithm");
   public static final QName T13_ENC_ALGO = new QName("http://docs.oasis-open.org/ws-sx/ws-trust/200512", "EncryptionAlgorithm");
   public static final QName T13_ENC_WITH = new QName("http://docs.oasis-open.org/ws-sx/ws-trust/200512", "EncryptWith");
   public static final QName T13_SIGN_WITH = new QName("http://docs.oasis-open.org/ws-sx/ws-trust/200512", "SignWith");
}
