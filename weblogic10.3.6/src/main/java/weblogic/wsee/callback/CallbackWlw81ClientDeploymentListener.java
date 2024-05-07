package weblogic.wsee.callback;

import java.util.Arrays;
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
import weblogic.wsee.wsdl.builder.WsdlOperationBuilder;
import weblogic.wsee.wsdl.builder.WsdlPortBuilder;

public class CallbackWlw81ClientDeploymentListener implements WsDeploymentListener {
   private static final boolean verbose = Verbose.isVerbose(CallbackWlw81ClientDeploymentListener.class);
   private static final List BEFORE = Arrays.asList((Object[])(new String[]{"SECURITY_HANDLER", "CONVERSATION_HANDLER", "CONNECTION_HANDLER"}));
   private static final List AFTER = Arrays.asList((Object[])(new String[]{"CODEC_HANDLER"}));

   public void process(WsDeploymentContext var1) throws WsDeploymentException {
      if (verbose) {
         Verbose.log((Object)"CallbackWlw81ClientDeploymentListener firing");
      }

      Iterator var2 = var1.getWsService().getPorts();

      while(var2.hasNext()) {
         WsPort var3 = (WsPort)var2.next();
         if (!ConversationUtils.isConversational(var3) && this.hasWlw81Operations(var3)) {
            HandlerInfo var4 = new HandlerInfo(CallbackWlw81ClientHandler.class, (Map)null, (QName[])null);

            try {
               var3.getInternalHandlerList().lenientInsert("CALLBACK_WLW81_CLIENT_HANDLER", var4, AFTER, BEFORE);
            } catch (HandlerException var6) {
               throw new WsDeploymentException("Could not insert WLW81 callback client handler", var6);
            }
         }
      }

   }

   private boolean hasWlw81Operations(WsPort var1) {
      Iterator var2 = ((WsdlPortBuilder)var1.getWsdlPort()).getPortType().getOperations().values().iterator();

      WsdlOperationBuilder var3;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         var3 = (WsdlOperationBuilder)var2.next();
      } while(!var3.isWLW81CallbackOperation());

      return true;
   }
}
