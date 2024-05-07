package weblogic.wsee.ws.dispatch.server;

import java.io.IOException;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import weblogic.wsee.handler.InvocationException;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.ws.dispatch.Dispatcher;

public class OneWayHandler extends GenericHandler {
   public static final String ONEWAY_CONFIRMED = "weblogic.wsee.oneway.confirmed";

   public QName[] getHeaders() {
      return new QName[0];
   }

   public boolean handleRequest(MessageContext var1) {
      WlMessageContext var2 = WlMessageContext.narrow(var1);
      Dispatcher var3 = var2.getDispatcher();
      if (var3.getOperation().getType() == 1 || var3.getOperation().getType() == 3 || var1.getProperty("weblogic.wsee.reply.anonymous") == null && var1.getProperty("weblogic.wsee.fault.anonymous") == null) {
         try {
            var3.getConnection().getTransport().confirmOneway();
         } catch (IOException var5) {
            throw new InvocationException("Failed to confirm oneway", var5);
         }

         var2.setProperty("weblogic.wsee.oneway.confirmed", "true");
      }

      return true;
   }
}
