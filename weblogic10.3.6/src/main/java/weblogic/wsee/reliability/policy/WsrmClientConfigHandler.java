package weblogic.wsee.reliability.policy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerInfo;
import weblogic.wsee.handler.HandlerException;
import weblogic.wsee.handler.HandlerList;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.provider.ClientConfigurationHandler;
import weblogic.wsee.policy.runtime.PolicyServer;
import weblogic.wsee.reliability.WsrmClientHandler;
import weblogic.wsee.reliability.WsrmPreprocessHandler;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.WsService;
import weblogic.wsee.ws.init.WsDeploymentContext;

public class WsrmClientConfigHandler implements ClientConfigurationHandler {
   private static final boolean verbose = Verbose.isVerbose(WsrmClientConfigHandler.class);
   private static final List BEFORE_PREPROCESS = Arrays.asList((Object[])(new String[]{"ADDRESSING_HANDLER", "CONVERSATION_GET_ID_HANDLER"}));
   private static final List AFTER_PREPROCESS = Arrays.asList((Object[])(new String[]{"CODEC_HANDLER"}));
   private static final List BEFORE_RELIABILITY = Arrays.asList((Object[])(new String[]{"SECURITY_HANDLER", "CONNECTION_HANDLER"}));
   private static final List AFTER_RELIABILITY = Arrays.asList((Object[])(new String[]{"ASYNC_HANDLER"}));

   public void process(WsDeploymentContext var1) throws PolicyException {
      WsService var2 = var1.getWsService();
      if (verbose) {
         Verbose.log((Object)"Calling WsrmClientConfigHandler");
      }

      PolicyServer var3 = var2.getPolicyServer();
      Iterator var4 = var2.getPorts();

      while(var4.hasNext()) {
         WsPort var5 = (WsPort)var4.next();
         if (ReliabilityPolicyAssertionsFactory.hasRMPolicy(var5, var3, var3.getCachedPolicies())) {
            if (verbose) {
               Verbose.log((Object)("WSRM enabled for port " + var5.getWsdlPort().getName()));
            }

            HandlerList var6 = var5.getInternalHandlerList();
            HandlerInfo var7 = new HandlerInfo(WsrmPreprocessHandler.class, new HashMap(), (QName[])null);
            HandlerInfo var8 = new HandlerInfo(WsrmClientHandler.class, new HashMap(), (QName[])null);

            try {
               var6.lenientInsert("WSRM_PREPROCESS_HANDLER", var7, AFTER_PREPROCESS, BEFORE_PREPROCESS);
               var6.lenientInsert("RELIABILITY_HANDLER", var8, AFTER_RELIABILITY, BEFORE_RELIABILITY);
            } catch (HandlerException var10) {
               throw new PolicyException("Failed to register handler", var10);
            }
         }
      }

   }
}
