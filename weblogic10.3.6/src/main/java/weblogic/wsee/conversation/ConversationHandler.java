package weblogic.wsee.conversation;

import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.GenericHandler;
import weblogic.wsee.conversation.wsdl.ConversationWsdlPhase;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.wsdl.WsdlBindingOperation;

public class ConversationHandler extends GenericHandler {
   public static final String CONVERSATION_ID = "weblogic.wsee.conversation.ConversationId";
   public static final String CALLBACK_LOCATION = "weblogic.wsee.callback.loc";
   public static final String CONVERSATION_PHASE = "weblogic.wsee.conversation.ConversationPhase";
   public static final String CONVERSATION_VERSION = "weblogic.wsee.conversation.ConversationVersion";

   public QName[] getHeaders() {
      return ConversationConstants.CONV_HEADERS;
   }

   protected ConversationPhase getConversationPhase(WlMessageContext var1) {
      WsdlBindingOperation var2 = var1.getDispatcher().getBindingOperation();

      assert var2 != null;

      ConversationWsdlPhase var3 = ConversationWsdlPhase.narrow(var2);
      if (var3 == null) {
         Iterator var4 = var2.getBinding().getOperations().values().iterator();

         WsdlBindingOperation var5;
         do {
            if (!var4.hasNext()) {
               return null;
            }

            var5 = (WsdlBindingOperation)var4.next();
         } while(ConversationWsdlPhase.narrow(var5) == null);

         return ConversationPhase.CONTINUE;
      } else {
         return var3.getPhase();
      }
   }

   protected int getConversationMajorVersion(WlMessageContext var1) {
      Integer var2 = (Integer)var1.getProperty("weblogic.wsee.conversation.ConversationVersion");
      return var2 == null ? 2 : var2;
   }
}
