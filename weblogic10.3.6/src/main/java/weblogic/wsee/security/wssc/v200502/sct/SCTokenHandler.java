package weblogic.wsee.security.wssc.v200502.sct;

import javax.xml.namespace.QName;
import weblogic.wsee.security.wssc.base.sct.SCTokenBase;
import weblogic.wsee.security.wssc.base.sct.SCTokenHandlerBase;
import weblogic.wsee.security.wssc.v200502.WSCConstants;
import weblogic.xml.crypto.wss.provider.SecurityTokenHandler;

public class SCTokenHandler extends SCTokenHandlerBase implements SecurityTokenHandler {
   protected String getSCT_IDENTIFIER() {
      return "Identifier";
   }

   protected String getSCT_RST_ACTION() {
      return "http://schemas.xmlsoap.org/ws/2005/02/trust/RST/SCT";
   }

   protected String getSCT_RST_CANCEL_ACTION() {
      return "http://schemas.xmlsoap.org/ws/2005/02/trust/RST/SCT/Cancel";
   }

   protected String getSCT_RST_RENEW_ACTION() {
      return "http://schemas.xmlsoap.org/ws/2005/02/trust/RST/SCT/Renew";
   }

   protected QName[] getSCT_QNAMES() {
      return WSCConstants.SCT_QNAMES;
   }

   protected String[] getSCT_VALUE_TYPES() {
      return WSCConstants.SCT_VALUE_TYPES;
   }

   protected String getSCT_VALUE_TYPE() {
      return "http://schemas.xmlsoap.org/ws/2005/02/sc/sct";
   }

   protected String getXMLNS_WSC() {
      return "http://schemas.xmlsoap.org/ws/2005/02/sc";
   }

   protected SCTokenBase newSCToken() {
      return new SCToken();
   }

   protected SCTokenBase newSCToken(weblogic.wsee.security.wssc.sct.SCCredential var1) {
      return new SCToken(var1);
   }

   protected SCCredential newSCCredential() {
      SCCredential var1 = new SCCredential();
      var1.setScNamespace(this.getXMLNS_WSC());
      return var1;
   }

   protected String getCANNED_POLICY_INCLUDE_SCT_FOR_IDENTITY() {
      return "AddSctToken.xml";
   }
}
