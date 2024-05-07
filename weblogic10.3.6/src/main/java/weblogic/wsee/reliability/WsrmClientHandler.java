package weblogic.wsee.reliability;

import java.util.Map;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.runtime.PolicyContext;
import weblogic.wsee.reliability.policy.WsrmPolicyClientRuntimeHandler;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.wsdl.WsdlOperation;

public class WsrmClientHandler extends WsrmHandler {
   private static final boolean verbose = Verbose.isVerbose(WsrmClientHandler.class);

   public boolean handleRequest(MessageContext var1) {
      assert var1 != null;

      if (!(var1 instanceof SOAPMessageContext)) {
         return true;
      } else {
         WlMessageContext var2 = WlMessageContext.narrow(var1);
         if (verbose) {
            Verbose.log((Object)"[WsrmClientHandler.handleRequest()] called");
         }

         boolean var3 = true;
         Map var4 = (Map)var2.getProperty("weblogic.wsee.invoke_properties");
         if (var4 != null) {
            String var5 = (String)var4.get("weblogic.wsee.sequenceid");
            if (var5 != null) {
               var3 = false;
            }
         }

         if (var3 && var2.containsProperty("weblogic.wsee.reliability.forceWSRM10Client")) {
            var2.setProperty("weblogic.wsee.wsrm.RMVersion", WsrmConstants.RMVersion.RM_10);
         }

         try {
            boolean var7;
            if (var2.getProperty("weblogic.wsee.async.res") != null) {
               var7 = (new WsrmPolicyClientRuntimeHandler()).processRequest(var1, PolicyContext.getResponseEffectivePolicy(var1));
            } else {
               var7 = (new WsrmPolicyClientRuntimeHandler()).processRequest(var1, PolicyContext.getRequestEffectivePolicy(var1));
            }

            if (!var7) {
               return false;
            }
         } catch (PolicyException var6) {
            throw new JAXRPCException(var6.getMessage());
         }

         if (var1.getProperty("weblogic.wsee.ackrequest") != null) {
            if (verbose) {
               Verbose.log((Object)"Acknowledgement request");
            }

            WsrmSAFManagerFactory.getWsrmSAFSendingManager().ackRequest(var2);
            return false;
         } else {
            WsdlOperation var8 = var2.getDispatcher().getOperation();
            if (var8 != null && (var8.getType() == 0 || var8.getType() == 2) && var2.getProperty("weblogic.wsee.async.invoke") == null && var2.getProperty("weblogic.wsee.async.res") == null) {
               throw new JAXRPCException("Reliable messaging will only work with one way messages or asynchronous request/response messages.");
            } else {
               WsrmSAFManagerFactory.getWsrmSAFSendingManager().storeAndForward(var2);
               return false;
            }
         }
      }
   }
}
