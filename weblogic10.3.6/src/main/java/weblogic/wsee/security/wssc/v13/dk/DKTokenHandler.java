package weblogic.wsee.security.wssc.v13.dk;

import javax.xml.namespace.QName;
import weblogic.wsee.security.wssc.base.dk.DKTokenBase;
import weblogic.wsee.security.wssc.base.dk.DKTokenHandlerBase;
import weblogic.wsee.security.wssc.dk.DKCredential;
import weblogic.wsee.security.wssc.v13.WSCConstants;
import weblogic.xml.crypto.wss.provider.SecurityTokenHandler;

public class DKTokenHandler extends DKTokenHandlerBase implements SecurityTokenHandler {
   protected QName[] getDK_QNAMES() {
      return WSCConstants.DK_QNAMES;
   }

   protected String[] getDK_VALUE_TYPES() {
      return WSCConstants.DK_VALUE_TYPES;
   }

   protected DKTokenBase newDKToken() {
      return new DKToken();
   }

   protected DKTokenBase newDKToken(DKCredential var1) {
      return new DKToken(var1);
   }

   protected String getDK_VALUE_TYPE() {
      return "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/dk";
   }

   protected String getSCT_RST_ACTION() {
      return "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/SCT";
   }

   protected String getXMLNS_WSC() {
      return "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512";
   }
}
