package weblogic.wsee.conversation;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerInfo;
import weblogic.wsee.handler.HandlerException;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.init.WsDeploymentContext;
import weblogic.wsee.ws.init.WsDeploymentException;
import weblogic.wsee.ws.init.WsDeploymentListener;

public class ConversationClientDeploymentListener implements WsDeploymentListener {
   private static final boolean verbose = Verbose.isVerbose(ConversationClientDeploymentListener.class);
   private static final HandlerInfo HANDLER = new HandlerInfo(ConversationClientHandler.class, (Map)null, (QName[])null);
   private static final HandlerInfo GET_ID_HANDLER = new HandlerInfo(ConversationGetIdHandler.class, (Map)null, (QName[])null);
   private static final List BEFORE_CONVERSATION = Arrays.asList((Object[])(new String[]{"MMEHEADER_HANDLER", "JAX_RPC_CHAIN_HANDLER"}));
   private static final List AFTER_CONVERSATION = Arrays.asList((Object[])(new String[]{"ADDRESSING_HANDLER"}));
   private static final List BEFORE_CONVERSATION_GET_ID = Arrays.asList((Object[])(new String[]{"ADDRESSING_HANDLER"}));
   private static final List AFTER_CONVERSATION_GET_ID = Arrays.asList((Object[])(new String[]{"CODEC_HANDLER", "WSRM_PREPROCESS_HANDLER"}));

   public void process(WsDeploymentContext var1) throws WsDeploymentException {
      if (verbose) {
         Verbose.log((Object)"Running ConversationClientDeploymentListener");
      }

      Iterator var2 = var1.getWsService().getPorts();

      while(var2.hasNext()) {
         WsPort var3 = (WsPort)var2.next();
         if (ConversationUtils.isConversational(var3)) {
            try {
               var3.getInternalHandlerList().lenientInsert("CONVERSATION_HANDLER", HANDLER, AFTER_CONVERSATION, BEFORE_CONVERSATION);
               var3.getInternalHandlerList().lenientInsert("CONVERSATION_GET_ID_HANDLER", GET_ID_HANDLER, AFTER_CONVERSATION_GET_ID, BEFORE_CONVERSATION_GET_ID);
            } catch (HandlerException var5) {
               throw new WsDeploymentException("Could not insert client-side conversation handler", var5);
            }
         }
      }

   }
}
