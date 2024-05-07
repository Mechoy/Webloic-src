package weblogic.wsee.security.wssc.v13.sct;

import javax.xml.rpc.handler.soap.SOAPMessageContext;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.security.wssc.base.sct.SCTHelperBase;
import weblogic.wsee.security.wssc.base.sct.SCTokenBase;

public class SCTHelper {
   public static final SCToken performWSSCHandshake(SoapMessageContext var0) {
      SCTokenBase var1 = SCTHelperBase.performWSSCHandshake(var0, new SCTokenHandler());
      return null == var1 ? null : (SCToken)var1;
   }

   public static final String getCredentialIdentifier(SOAPMessageContext var0) {
      return SCTHelperBase.getCredentialIdentifier(var0, "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512", "Identifier");
   }
}
