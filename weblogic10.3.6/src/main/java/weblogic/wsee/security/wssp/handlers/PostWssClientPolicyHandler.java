package weblogic.wsee.security.wssp.handlers;

import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPException;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.wss.WSSecurityContext;
import weblogic.xml.crypto.wss.WSSecurityException;

public class PostWssClientPolicyHandler extends WssHandler {
   protected boolean processRequest(SOAPMessageContext var1) throws PolicyException, SOAPException, WSSecurityException {
      return true;
   }

   protected boolean processResponse(SOAPMessageContext var1) throws PolicyException, SOAPException, WSSecurityException {
      WSSecurityContext var2 = WSSecurityContext.getSecurityContext(var1);

      try {
         if (var2 != null) {
            WSSecurityContext.pushContext(var2);
         }

         this.preValidate(var1, false);
      } catch (MarshalException var7) {
         throw new SOAPException(var7);
      } finally {
         if (var2 != null) {
            WSSecurityContext.popContext();
         }

      }

      this.reportInboundWSSSuccessToWsspStats(this.getWsspStats(var1), var1);
      return true;
   }
}
