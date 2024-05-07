package weblogic.wsee.callback.controls;

import java.util.Iterator;
import java.util.LinkedHashMap;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import weblogic.wsee.callback.CallbackUtils;
import weblogic.wsee.callback.Wlw81CallbackHeader;
import weblogic.wsee.conversation.ContinueHeader;
import weblogic.wsee.message.WlMessageContext;

public class Wlw81ControlCallbackHeaderTranslationHandler extends GenericHandler {
   public QName[] getHeaders() {
      return new QName[0];
   }

   public boolean handleRequest(MessageContext var1) {
      WlMessageContext var2 = WlMessageContext.narrow(var1);
      this.convertFromWlw81ControlCallbackHeadersIfNecessary(var2);
      return true;
   }

   private void convertFromWlw81ControlCallbackHeadersIfNecessary(WlMessageContext var1) {
      ControlCallbackInfoHeader var2 = (ControlCallbackInfoHeader)var1.getHeaders().getHeader(ControlCallbackInfoHeader.TYPE);
      Wlw81CallbackHeader var3 = (Wlw81CallbackHeader)var1.getHeaders().getHeader(Wlw81CallbackHeader.TYPE);
      if (var2 == null && var3 != null) {
         String var4 = var3.getConversationId();
         String var5 = CallbackUtils.parseSendingSideConvId(var4);
         if (var5 != null) {
            ContinueHeader var6 = new ContinueHeader();
            var6.parseFromWlw81StringForm(var5);
            var1.getHeaders().addHeader(var6);
         }

         GenericControlCallbackReferenceData var10 = new GenericControlCallbackReferenceData();
         LinkedHashMap var7 = CallbackUtils.keyValuePairsForWlw81ControlsCallbackEncodedConvId(var4);
         Iterator var8 = var7.keySet().iterator();

         while(var8.hasNext()) {
            String var9 = (String)var8.next();
            var10.put(var9, (String)var7.get(var9));
         }

         ControlCallbackInfoHeader var11 = new ControlCallbackInfoHeader(var10);
         var1.getHeaders().addHeader(var11);
      }

   }
}
