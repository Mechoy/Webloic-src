package weblogic.wsee.jws.wlw;

import javax.transaction.Transaction;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import weblogic.transaction.TransactionHelper;
import weblogic.wsee.conversation.ConversationPhase;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.dispatch.Dispatcher;

public class WLW81CompatTxVoidReturnClientHandler extends GenericHandler {
   private static final boolean verbose = Verbose.isVerbose(WLW81CompatTxVoidReturnClientHandler.class);
   public static final String WLW81_UPGRADE = "weblogic.wsee.WLW81Upgrade";

   public boolean handleRequest(MessageContext var1) {
      WlMessageContext var2 = WlMessageContext.narrow(var1);
      Dispatcher var3 = var2.getDispatcher();
      if ("jms".equalsIgnoreCase(var3.getWsdlPort().getTransport()) && (var3.getOperation().getType() == 0 || var3.getOperation().getType() == 2) && !var1.containsProperty("weblogic.wsee.async.invoke") && this.isInTransaction() && var3.getWsMethod().getReturnType() == null) {
         WsPort var4 = var2.getDispatcher().getWsPort();
         ConversationPhase var5 = (ConversationPhase)var1.getProperty("weblogic.wsee.conversation.ConversationPhase");
         if ((var5 == null || var5 == ConversationPhase.NONE) && var1.getProperty("weblogic.wsee.WLW81Upgrade") != null) {
            var1.setProperty("weblogic.wsee.ws.dispatch.WLW81compatTxVoidReturn", "true");
         }
      }

      return true;
   }

   public QName[] getHeaders() {
      return new QName[0];
   }

   private boolean isInTransaction() {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransaction();
      return var1 != null;
   }
}
