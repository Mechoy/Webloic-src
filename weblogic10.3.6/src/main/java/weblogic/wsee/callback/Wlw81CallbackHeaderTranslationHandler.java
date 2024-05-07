package weblogic.wsee.callback;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import weblogic.wsee.conversation.ContinueHeader;
import weblogic.wsee.conversation.ConversationConstants;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.util.Verbose;

public class Wlw81CallbackHeaderTranslationHandler extends GenericHandler {
   private static final boolean verbose = Verbose.isVerbose(Wlw81CallbackHeaderTranslationHandler.class);

   public QName[] getHeaders() {
      return ConversationConstants.WLW81_CALLBACK_HEADERS;
   }

   public boolean handleRequest(MessageContext var1) {
      WlMessageContext var2 = WlMessageContext.narrow(var1);
      Wlw81CallbackHeader var3 = (Wlw81CallbackHeader)var2.getHeaders().getHeader(Wlw81CallbackHeader.TYPE);
      if (var3 != null) {
         CallbackInfoHeader var4 = new CallbackInfoHeader();
         var4.parseFromWlw81StringForm(var3.getConversationId());
         var2.getHeaders().addHeader(var4);
         String var5 = CallbackUtils.parseSendingSideConvId(var3.getConversationId());
         if (var5 != null) {
            ContinueHeader var6 = new ContinueHeader();
            var6.parseFromWlw81StringForm(var5);
            var6.setAppVersionId(var4.getAppVersion());
            var2.getHeaders().addHeader(var6);
         }
      }

      return true;
   }
}
