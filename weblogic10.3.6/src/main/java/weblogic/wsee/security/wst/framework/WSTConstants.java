package weblogic.wsee.security.wst.framework;

public class WSTConstants {
   public static final String BS_TYPE_ASYMMETRIC = "/AsymmetricKey";
   public static final String BS_TYPE_SYMMETRIC = "/SymmetricKey";
   public static final String BS_TYPE_NONCE = "/Nonce";
   public static final String PSHA1 = "/CK/PSHA1";
   public static final String REQUEST_TYPE_ISSUE = "/Issue";
   public static final String REQUEST_TYPE_RENEW = "/Renew";
   public static final String REQUEST_TYPE_CANCEL = "/Cancel";
   public static final String REQUEST_TYPE_VALIDATE = "/Validate";
   public static final String ACTION_RST = "/RST";
   public static final String ACTION_RSTR = "/RSTR";
   public static final String AES_ALGORITHM = "AES";
   public static final String SCT_CONSTANT = "sct";
   public static final long DEFAULT_SCT_TOKEN_LIFE_TIME = 1800000L;
}
