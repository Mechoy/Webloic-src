package weblogic.wsee.reliability;

import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.MessageContext;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.runtime.PolicyContext;
import weblogic.wsee.reliability.policy.WsrmPolicyClientRuntimeHandler;

public class WsrmPreprocessHandler extends WsrmHandler {
   public boolean handleRequest(MessageContext var1) {
      var1.setProperty("weblogic.wsee.enable.rm", "true");
      var1.setProperty("weblogic.wsee.complex", "true");

      try {
         (new WsrmPolicyClientRuntimeHandler()).processRequest(var1, PolicyContext.getRequestEffectivePolicy(var1));
         return true;
      } catch (PolicyException var3) {
         throw new JAXRPCException(var3.getMessage());
      }
   }
}
