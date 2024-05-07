package weblogic.wsee.security.wssc.v13;

import javax.xml.namespace.QName;
import weblogic.wsee.security.wssc.base.WSCConstantsBase;

public final class WSCConstants extends WSCConstantsBase {
   public static final String XMLNS_WSC = "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512";
   public static final String SCT_VALUE_TYPE = "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/sct";
   public static final String[] SCT_VALUE_TYPES = new String[]{"http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/sct"};
   public static final QName SCT_QNAME = new QName("http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512", "SecurityContextToken");
   public static final QName[] SCT_QNAMES;
   public static final QName SCT_IDENTIFIER_QNAME;
   public static final String SCT_RST_ACTION = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/SCT";
   public static final String SCT_RSTR_ACTION = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RSTR/SCT";
   public static final String SCT_RST_AMEND_ACTION = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/SCT/Amend";
   public static final String SCT_RSTR_AMEND_ACTION = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RSTR/SCT/Amend";
   public static final String SCT_RST_RENEW_ACTION = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/SCT/Renew";
   public static final String SCT_RSTR_RENEW_ACTION = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RSTR/SCT/Renew";
   public static final String SCT_RST_CANCEL_ACTION = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/SCT/Cancel";
   public static final String SCT_RSTR_CANCEL_ACTION = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RSTR/SCT/Cancel";
   public static final String DK_VALUE_TYPE = "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/dk";
   public static final String[] DK_VALUE_TYPES;
   public static final QName DK_QNAME;
   public static final QName[] DK_QNAMES;
   public static final QName DK_PROPERTIES_QNAME;
   public static final QName DK_GENERATION_QNAME;
   public static final QName DK_OFFSET_QNAME;
   public static final QName DK_LENGTH_QNAME;
   public static final QName DK_LABEL_QNAME;
   public static final QName DK_NONCE_QNAME;
   public static final String URI_P_SHA1 = "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/dk/p_sha1";
   public static final String CANNED_POLICY_INCLUDE_SCT_FOR_IDENTITY = "AddSctV13Token.xml";

   static {
      SCT_QNAMES = new QName[]{SCT_QNAME};
      SCT_IDENTIFIER_QNAME = new QName("http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512", "Identifier");
      DK_VALUE_TYPES = new String[]{"http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/dk"};
      DK_QNAME = new QName("http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512", "DerivedKeyToken");
      DK_QNAMES = new QName[]{DK_QNAME};
      DK_PROPERTIES_QNAME = new QName("http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512", "Properties");
      DK_GENERATION_QNAME = new QName("http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512", "Generation");
      DK_OFFSET_QNAME = new QName("http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512", "Offset");
      DK_LENGTH_QNAME = new QName("http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512", "Length");
      DK_LABEL_QNAME = new QName("http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512", "Label");
      DK_NONCE_QNAME = new QName("http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512", "Nonce");
   }
}
