package weblogic.wsee.security.wssc.v200502;

import javax.xml.namespace.QName;
import weblogic.wsee.security.wssc.base.WSCConstantsBase;

public final class WSCConstants extends WSCConstantsBase {
   public static final String XMLNS_WSC = "http://schemas.xmlsoap.org/ws/2005/02/sc";
   public static final String SCT_VALUE_TYPE = "http://schemas.xmlsoap.org/ws/2005/02/sc/sct";
   public static final String[] SCT_VALUE_TYPES = new String[]{"http://schemas.xmlsoap.org/ws/2005/02/sc/sct"};
   public static final QName SCT_QNAME = new QName("http://schemas.xmlsoap.org/ws/2005/02/sc", "SecurityContextToken");
   public static final QName[] SCT_QNAMES;
   public static final QName SCT_IDENTIFIER_QNAME;
   public static final String SCT_RST_ACTION = "http://schemas.xmlsoap.org/ws/2005/02/trust/RST/SCT";
   public static final String SCT_RSTR_ACTION = "http://schemas.xmlsoap.org/ws/2005/02/trust/RSTR/SCT";
   public static final String SCT_RST_AMEND_ACTION = "http://schemas.xmlsoap.org/ws/2005/02/trust/RST/SCT/Amend";
   public static final String SCT_RSTR_AMEND_ACTION = "http://schemas.xmlsoap.org/ws/2005/02/trust/RSTR/SCT/Amend";
   public static final String SCT_RST_RENEW_ACTION = "http://schemas.xmlsoap.org/ws/2005/02/trust/RST/SCT/Renew";
   public static final String SCT_RSTR_RENEW_ACTION = "http://schemas.xmlsoap.org/ws/2005/02/trust/RSTR/SCT/Renew";
   public static final String SCT_RST_CANCEL_ACTION = "http://schemas.xmlsoap.org/ws/2005/02/trust/RST/SCT/Cancel";
   public static final String SCT_RSTR_CANCEL_ACTION = "http://schemas.xmlsoap.org/ws/2005/02/trust/RSTR/SCT/Cancel";
   public static final String DK_VALUE_TYPE = "http://schemas.xmlsoap.org/ws/2005/02/sc/dk";
   public static final String[] DK_VALUE_TYPES;
   public static final QName DK_QNAME;
   public static final QName[] DK_QNAMES;
   public static final QName DK_PROPERTIES_QNAME;
   public static final QName DK_GENERATION_QNAME;
   public static final QName DK_OFFSET_QNAME;
   public static final QName DK_LENGTH_QNAME;
   public static final QName DK_LABEL_QNAME;
   public static final QName DK_NONCE_QNAME;
   public static final String URI_P_SHA1 = "http://schemas.xmlsoap.org/ws/2005/02/sc/dk/p_sha1";
   public static final String CANNED_POLICY_INCLUDE_SCT_FOR_IDENTITY = "AddSctToken.xml";

   static {
      SCT_QNAMES = new QName[]{SCT_QNAME};
      SCT_IDENTIFIER_QNAME = new QName("http://schemas.xmlsoap.org/ws/2005/02/sc", "Identifier");
      DK_VALUE_TYPES = new String[]{"http://schemas.xmlsoap.org/ws/2005/02/sc/dk"};
      DK_QNAME = new QName("http://schemas.xmlsoap.org/ws/2005/02/sc", "DerivedKeyToken");
      DK_QNAMES = new QName[]{DK_QNAME};
      DK_PROPERTIES_QNAME = new QName("http://schemas.xmlsoap.org/ws/2005/02/sc", "Properties");
      DK_GENERATION_QNAME = new QName("http://schemas.xmlsoap.org/ws/2005/02/sc", "Generation");
      DK_OFFSET_QNAME = new QName("http://schemas.xmlsoap.org/ws/2005/02/sc", "Offset");
      DK_LENGTH_QNAME = new QName("http://schemas.xmlsoap.org/ws/2005/02/sc", "Length");
      DK_LABEL_QNAME = new QName("http://schemas.xmlsoap.org/ws/2005/02/sc", "Label");
      DK_NONCE_QNAME = new QName("http://schemas.xmlsoap.org/ws/2005/02/sc", "Nonce");
   }
}
