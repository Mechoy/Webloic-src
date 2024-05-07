package weblogic.wsee.buffer;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.util.Verbose;

public class Wlw81CompatBufferingResponseBlockerHandler extends GenericHandler {
   private static final boolean verbose = Verbose.isVerbose(Wlw81CompatBufferingHandler.class);

   public boolean handleRequest(MessageContext var1) {
      return true;
   }

   public boolean handleResponse(MessageContext var1) {
      WlMessageContext var2 = WlMessageContext.narrow(var1);
      return !var2.containsProperty("weblogic.wsee.handler.wlw81BufferCompatFlat") || var2.containsProperty("weblogic.wsee.queued.invoke");
   }

   public boolean handleFault(MessageContext var1) {
      return this.handleResponse(var1);
   }

   public QName[] getHeaders() {
      return new QName[0];
   }
}
