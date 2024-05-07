package weblogic.wsee.security.wssc.v200502.dk;

import javax.xml.namespace.QName;
import weblogic.wsee.security.wssc.base.dk.DKTokenBase;
import weblogic.wsee.security.wssc.base.dk.DKTokenHandlerBase;
import weblogic.wsee.security.wssc.dk.DKCredential;
import weblogic.wsee.security.wssc.v200502.WSCConstants;
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
      return "http://schemas.xmlsoap.org/ws/2005/02/sc/dk";
   }

   protected String getSCT_RST_ACTION() {
      return "http://schemas.xmlsoap.org/ws/2005/02/trust/RST/SCT";
   }

   protected String getXMLNS_WSC() {
      return "http://schemas.xmlsoap.org/ws/2005/02/sc";
   }
}
