package weblogic.wsee.security.wssc.v13.sct;

import javax.xml.namespace.QName;
import weblogic.wsee.security.wssc.base.sct.SCTokenBase;
import weblogic.wsee.security.wssc.base.sct.SCTokenHandlerBase;
import weblogic.wsee.security.wssc.sct.SCCredential;
import weblogic.wsee.security.wssc.v13.WSCConstants;
import weblogic.xml.crypto.wss.provider.SecurityTokenHandler;

public class SCTokenHandler extends SCTokenHandlerBase implements SecurityTokenHandler {
   protected String getSCT_IDENTIFIER() {
      return "Identifier";
   }

   protected String getSCT_RST_ACTION() {
      return "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/SCT";
   }

   protected String getSCT_RST_CANCEL_ACTION() {
      return "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/SCT/Cancel";
   }

   protected String getSCT_RST_RENEW_ACTION() {
      return "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/SCT/Renew";
   }

   protected QName[] getSCT_QNAMES() {
      return WSCConstants.SCT_QNAMES;
   }

   protected String[] getSCT_VALUE_TYPES() {
      return WSCConstants.SCT_VALUE_TYPES;
   }

   protected String getSCT_VALUE_TYPE() {
      return "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/sct";
   }

   protected String getXMLNS_WSC() {
      return "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512";
   }

   protected SCTokenBase newSCToken() {
      return new SCToken();
   }

   protected SCTokenBase newSCToken(SCCredential var1) {
      return new SCToken(var1);
   }

   protected SCCredential newSCCredential() {
      SCCredential var1 = new SCCredential();
      var1.setScNamespace(this.getXMLNS_WSC());
      return var1;
   }

   protected String getCANNED_POLICY_INCLUDE_SCT_FOR_IDENTITY() {
      return "AddSctV13Token.xml";
   }
}
