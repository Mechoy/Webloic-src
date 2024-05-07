package weblogic.wsee.conversation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerInfo;
import weblogic.wsee.cluster.ClusterDispatcher;
import weblogic.wsee.cluster.ForwardingHandler;
import weblogic.wsee.handler.HandlerException;
import weblogic.wsee.reliability.ReliableConversationMsgClusterService;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.init.WsDeploymentContext;
import weblogic.wsee.ws.init.WsDeploymentException;
import weblogic.wsee.ws.init.WsDeploymentListener;

public class ConversationDeploymentListener implements WsDeploymentListener {
   private static final boolean verbose = Verbose.isVerbose(ConversationDeploymentListener.class);
   private static final List CONVERSATION_HANDLER_BEFORE = Arrays.asList((Object[])(new String[]{"PRE_INVOKE_HANDLER"}));
   private static final List CONVERSATION_HANDLER_AFTER = Arrays.asList((Object[])(new String[]{"ONE_WAY_HANDLER"}));
   private static final List FORWARDING_HANDLER_BEFORE = Arrays.asList((Object[])(new String[]{"ADDRESSING_HANDLER", "SECURITY_HANDLER"}));
   private static final List FORWARDING_HANDLER_AFTER = Arrays.asList((Object[])(new String[]{"CONNECTION_HANDLER"}));

   public void process(WsDeploymentContext var1) throws WsDeploymentException {
      if (verbose) {
         Verbose.log((Object)"ConversationDeploymentListener firing");
      }

      Iterator var2 = var1.getWsService().getPorts();
      boolean var3 = false;

      while(var2.hasNext()) {
         WsPort var4 = (WsPort)var2.next();
         if (ConversationUtils.isConversational(var4)) {
            if (verbose) {
               Verbose.log((Object)("Adding conversation handlers for " + var1.getServiceURIs()[0]));
            }

            HandlerInfo var5 = new HandlerInfo(ForwardingHandler.class, new HashMap(), (QName[])null);
            HandlerInfo var6 = new HandlerInfo(ConversationServerHandler.class, (Map)null, (QName[])null);
            HandlerInfo var7 = new HandlerInfo(ConversationCMPHandler.class, (Map)null, (QName[])null);

            try {
               var4.getInternalHandlerList().lenientInsert("FORWARDING_HANDLER", var5, FORWARDING_HANDLER_AFTER, FORWARDING_HANDLER_BEFORE);
               int var8 = var4.getInternalHandlerList().insert("ConversationServerHandler", var6, CONVERSATION_HANDLER_AFTER, CONVERSATION_HANDLER_BEFORE);
               var4.getInternalHandlerList().insert("ConversationCMPHandler", var8 + 1, var7);
            } catch (HandlerException var12) {
               throw new WsDeploymentException("Unable to register handler", var12);
            }

            Class var13 = ConversationDeploymentListener.class;
            synchronized(ConversationDeploymentListener.class) {
               ClusterDispatcher var9 = ClusterDispatcher.getInstance();
               if (!var3) {
                  var9.registerClusterService(new ConversationMsgClusterService());
                  var9.registerClusterService(new ReliableConversationMsgClusterService());
                  var3 = true;
               }
            }
         }
      }

   }
}
