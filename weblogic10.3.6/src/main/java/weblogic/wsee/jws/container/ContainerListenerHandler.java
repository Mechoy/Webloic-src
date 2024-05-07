package weblogic.wsee.jws.container;

import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import weblogic.wsee.handler.WLHandler;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.ws.WsMethodImpl;

public class ContainerListenerHandler extends GenericHandler implements WLHandler {
   public static final String HANDLER_NAME = "CONTAINER_LISTENER_HANDLER";

   public boolean handleRequest(MessageContext var1) {
      WlMessageContext var2 = WlMessageContext.narrow(var1);

      try {
         Container var3 = getContainer(var1);
         var3.getListeners().preInvoke(var2);
         return true;
      } catch (RuntimeException var4) {
         throw var4;
      } catch (Exception var5) {
         throw new JAXRPCException(var5);
      }
   }

   private static Container getContainer(MessageContext var0) {
      Container var1 = ContainerFactory.getContainer(var0);
      if (var1 == null) {
         throw new IllegalStateException("Container not found in message context");
      } else {
         return var1;
      }
   }

   public boolean handleResponse(MessageContext var1) {
      try {
         this.notifyListeners(var1);
         return true;
      } catch (RuntimeException var3) {
         throw var3;
      } catch (Exception var4) {
         throw new JAXRPCException(var4);
      }
   }

   public boolean handleFault(MessageContext var1) {
      return this.handleResponse(var1);
   }

   public boolean handleClosure(MessageContext var1) {
      return this.handleResponse(var1);
   }

   public QName[] getHeaders() {
      return new QName[0];
   }

   private void notifyListeners(MessageContext var1) throws Exception {
      WlMessageContext var2 = WlMessageContext.narrow(var1);
      Container var3 = getContainer(var2);

      try {
         Exception var4 = (Exception)var2.getProperty("weblogic.wsee.component.AppException");
         if (var4 != null) {
            WsMethodImpl var5 = (WsMethodImpl)var2.getDispatcher().getWsMethod();
            var3.getListeners().onException(var4, var5.getMethodName(), var5.getMethodArgs(var2.getDispatcher().getInParams()));
         }
      } finally {
         var3.getListeners().postInvoke();
      }

   }
}
