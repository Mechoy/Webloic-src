package weblogic.wsee.security.wssc.v13.sct;

import java.io.Serializable;
import javax.xml.namespace.QName;
import weblogic.wsee.security.wssc.base.sct.SCTokenBase;
import weblogic.wsee.security.wssc.sct.SCCredential;
import weblogic.wsee.security.wssc.v13.WSCConstants;
import weblogic.wsee.security.wst.framework.TrustToken;

public class SCToken extends SCTokenBase implements TrustToken, Serializable {
   public SCToken() {
   }

   public SCToken(SCCredential var1) {
      super(var1);
   }

   protected QName getSCT_IDENTIFIER_QNAME() {
      return WSCConstants.SCT_IDENTIFIER_QNAME;
   }

   protected QName getSCT_QNAME() {
      return WSCConstants.SCT_QNAME;
   }

   protected String getSCT_VALUE_TYPE() {
      return "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/sct";
   }

   protected String getXMLNS_WSC() {
      return "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512";
   }
}
