package weblogic.wsee.jws.container;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerInfo;
import weblogic.wsee.conversation.ConversationUtils;
import weblogic.wsee.handler.HandlerException;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.init.WsDeploymentContext;
import weblogic.wsee.ws.init.WsDeploymentException;
import weblogic.wsee.ws.init.WsDeploymentListener;

public class ConversationLifeCycleDeploymentListener implements WsDeploymentListener {
   private static final boolean verbose = Verbose.isVerbose(ConversationLifeCycleDeploymentListener.class);

   public void process(WsDeploymentContext var1) throws WsDeploymentException {
      if (verbose) {
         Verbose.log((Object)"ConversationLifeCycleDeploymentListener firing");
      }

      Iterator var2 = var1.getWsService().getPorts();

      while(var2.hasNext()) {
         WsPort var3 = (WsPort)var2.next();
         if (ConversationUtils.isConversational(var3)) {
            if (verbose) {
               Verbose.log((Object)("Adding " + this.getClass().getName() + " for " + var1.getServiceURIs()[0]));
            }

            try {
               HandlerInfo var4 = new HandlerInfo(ConversationLifeCycleHandler.class, new HashMap(), (QName[])null);
               var3.getInternalHandlerList().insert("CONVERSATION_LIFE_CYCLE_HANDLER", var4, Arrays.asList("CONTAINER_HANDLER"), Arrays.asList("CONTAINER_LISTENER_HANDLER"));
            } catch (HandlerException var5) {
               throw new WsDeploymentException("Unable to register handler", var5);
            }
         }
      }

   }
}
