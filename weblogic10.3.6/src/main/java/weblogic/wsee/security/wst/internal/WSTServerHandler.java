package weblogic.wsee.security.wst.internal;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import weblogic.wsee.addressing.ActionHeader;
import weblogic.wsee.message.MsgHeaders;
import weblogic.wsee.message.WlMessageContext;

public abstract class WSTServerHandler extends GenericHandler {
   protected static final String RST_ACTION_V200502 = "/trust/RST/";
   protected static final String RSTR_ACTION_V200502 = "/trust/RSTR/";
   protected static final String RST_ACTION_V13 = "ws-trust/200512/RST/";
   protected static final String RSTR_ACTION_V13 = "ws-trust/200512/RSTR/";

   public abstract boolean handleTrustRequest(SOAPMessageContext var1, String var2);

   public boolean handleRequest(MessageContext var1) {
      if (!(var1 instanceof SOAPMessageContext)) {
         return true;
      } else {
         SOAPMessageContext var2 = (SOAPMessageContext)var1;
         String var3 = getActionFromContext(var2);
         return !isRST(var3) ? true : this.handleTrustRequest(var2, var3);
      }
   }

   public QName[] getHeaders() {
      return new QName[0];
   }

   private static boolean isRST(String var0) {
      return var0 != null && (var0.indexOf("/trust/RST/") > -1 || var0.indexOf("ws-trust/200512/RST/") > -1);
   }

   private static String getActionFromContext(MessageContext var0) {
      String var1 = (String)var0.getProperty("weblogic.wsee.addressing.Action");
      if (var1 == null) {
         WlMessageContext var2 = WlMessageContext.narrow(var0);
         MsgHeaders var3 = var2.getHeaders();
         ActionHeader var4 = (ActionHeader)var3.getHeader(ActionHeader.TYPE);
         if (var4 != null) {
            var1 = var4.getActionURI();
            var0.setProperty("weblogic.wsee.addressing.Action", var1);
         }
      }

      return var1;
   }
}
