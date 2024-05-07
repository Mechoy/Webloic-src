package weblogic.wsee.conversation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerInfo;
import weblogic.wsee.callback.Wlw81CallbackHeaderTranslationHandler;
import weblogic.wsee.cluster.ForwardingHandler;
import weblogic.wsee.handler.HandlerException;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.init.WsDeploymentContext;
import weblogic.wsee.ws.init.WsDeploymentException;
import weblogic.wsee.ws.init.WsDeploymentListener;

public class ConversationCallbackDeploymentListener implements WsDeploymentListener {
   private static final boolean verbose = Verbose.isVerbose(ConversationCallbackDeploymentListener.class);
   private static final HandlerInfo F_Handler = new HandlerInfo(ForwardingHandler.class, new HashMap(), (QName[])null);
   private static final HandlerInfo T_Handler = new HandlerInfo(Wlw81CallbackHeaderTranslationHandler.class, new HashMap(), (QName[])null);
   private static final HandlerInfo CC_HANDLER = new HandlerInfo(ConversationCallbackHandler.class, (Map)null, (QName[])null);
   private static final List BEFORE = Arrays.asList((Object[])(new String[]{"PRE_INVOKE_HANDLER"}));
   private static final List AFTER = Arrays.asList((Object[])(new String[]{"ONE_WAY_HANDLER"}));
   private static final List TRANSLATION_HANDLER_BEFORE = Arrays.asList((Object[])(new String[]{"ADDRESSING_HANDLER", "SECURITY_HANDLER"}));
   private static final List TRANSLATION_HANDLER_AFTER = Arrays.asList((Object[])(new String[]{"CONNECTION_HANDLER"}));

   public void process(WsDeploymentContext var1) throws WsDeploymentException {
      if (verbose) {
         Verbose.log((Object)"Running ConversationCallbackDeploymentListener");
      }

      Iterator var2 = var1.getWsService().getPorts();

      while(var2.hasNext()) {
         WsPort var3 = (WsPort)var2.next();

         try {
            var3.getInternalHandlerList().insert("CONVERSATION_HANDLER", CC_HANDLER, AFTER, BEFORE);
            int var4 = var3.getInternalHandlerList().lenientInsert("WLW81_TRANSLATION_HANDLER", T_Handler, TRANSLATION_HANDLER_AFTER, TRANSLATION_HANDLER_BEFORE);
            var3.getInternalHandlerList().insert("FORWARDING_HANDLER", var4 + 1, F_Handler);
         } catch (HandlerException var5) {
            throw new WsDeploymentException("Could not insert conversation cmp handler", var5);
         }
      }

   }
}
