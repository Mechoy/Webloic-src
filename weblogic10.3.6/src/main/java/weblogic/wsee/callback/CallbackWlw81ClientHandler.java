package weblogic.wsee.callback;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import weblogic.wsee.conversation.StartHeader;
import weblogic.wsee.message.WlMessageContext;

public class CallbackWlw81ClientHandler extends GenericHandler {
   public QName[] getHeaders() {
      return new QName[0];
   }

   public boolean handleRequest(MessageContext var1) {
      if (!(var1 instanceof SOAPMessageContext)) {
         return true;
      } else {
         WlMessageContext var2 = WlMessageContext.narrow(var1);
         String var3 = (String)var2.getProperty("weblogic.wsee.conversation.ConversationId");
         String var4 = (String)var2.getProperty("weblogic.wsee.callback.loc");
         if (var3 != null && var4 != null) {
            var2.getHeaders().addHeader(new StartHeader(var3, var4));
         }

         return true;
      }
   }
}
