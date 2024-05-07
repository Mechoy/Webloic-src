package weblogic.wsee.security.wssp.handlers;

import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPException;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.wss.WSSConstants;
import weblogic.xml.crypto.wss.WSSecurityException;

public class PreWssServerPolicyHandler extends WssHandler {
   protected boolean processRequest(SOAPMessageContext var1) throws PolicyException, SOAPException, WSSecurityException {
      try {
         this.preValidate(var1, true);
         return true;
      } catch (MarshalException var3) {
         throw new WSSecurityException(var3, WSSConstants.FAILURE_INVALID);
      }
   }

   protected boolean processResponse(SOAPMessageContext var1) throws PolicyException, SOAPException, WSSecurityException {
      return true;
   }
}
