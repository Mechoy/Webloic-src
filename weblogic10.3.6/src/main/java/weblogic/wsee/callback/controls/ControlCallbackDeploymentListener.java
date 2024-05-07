package weblogic.wsee.callback.controls;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerInfo;
import weblogic.wsee.conversation.ConversationUtils;
import weblogic.wsee.handler.HandlerException;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.init.WsDeploymentContext;
import weblogic.wsee.ws.init.WsDeploymentException;
import weblogic.wsee.ws.init.WsDeploymentListener;

public class ControlCallbackDeploymentListener implements WsDeploymentListener {
   private static final boolean verbose = Verbose.isVerbose(ControlCallbackDeploymentListener.class);
   private static final List BEFORE = Arrays.asList((Object[])(new String[]{"OPERATION_LOOKUP_HANDLER"}));
   private static final List AFTER = Arrays.asList((Object[])(new String[]{"ADDRESSING_HANDLER"}));

   public void process(WsDeploymentContext var1) throws WsDeploymentException {
      if (verbose) {
         Verbose.log((Object)"ControlCallbackDeploymentListener firing");
      }

      Iterator var2 = var1.getWsService().getPorts();

      while(var2.hasNext()) {
         WsPort var3 = (WsPort)var2.next();
         boolean var4 = ConversationUtils.isConversational(var3);
         HashMap var5 = new HashMap();
         String var6 = var1.getContextPath();
         if (var6 != null) {
            var5.put("control.callback.context.path", var6);
         }

         HandlerInfo var7 = new HandlerInfo(ControlCallbackTransactionHandler.class, (Map)null, (QName[])null);
         HandlerInfo var8 = new HandlerInfo(ControlCallbackHandler.class, var5, (QName[])null);
         if (verbose) {
            if (var4) {
               Verbose.log((Object)("Adding ConversationControlCallbackHandler and ControlCallbackHandler for " + var1.getServiceURIs()[0]));
            } else {
               Verbose.log((Object)("Adding ControlCallbackHandler for " + var1.getServiceURIs()[0]));
            }
         }

         try {
            int var9 = var3.getInternalHandlerList().insert("CONTROL_CALLBACK_TRANSACTION_HANDLER", var7, AFTER, BEFORE);
            if (var4) {
               HandlerInfo var10 = new HandlerInfo(ConversationControlCallbackHandler.class, (Map)null, (QName[])null);
               var3.getInternalHandlerList().insert("CONVERSATION_CONTROL_CALLBACK_HANDLER", var9 + 1, var10);
               var3.getInternalHandlerList().insert("CONTROL_CALLBACK_HANDLER", var9 + 2, var8);
            } else {
               var3.getInternalHandlerList().insert("CONTROL_CALLBACK_HANDLER", var9 + 1, var8);
            }
         } catch (HandlerException var11) {
            if (var4) {
               throw new WsDeploymentException("Could not insert ControlCallbackHandler or ConversationalControlCallbackHandler", var11);
            }

            throw new WsDeploymentException("Could not insert ControlCallbackHandler", var11);
         }
      }

   }
}
