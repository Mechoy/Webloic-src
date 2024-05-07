package weblogic.wsee.jws.container;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import weblogic.wsee.component.ComponentException;
import weblogic.wsee.component.pojo.JavaClassComponent;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.ws.WsSkel;

public class ContainerHandler extends GenericHandler {
   static final String HANDLER_NAME = "CONTAINER_HANDLER";

   public boolean handleRequest(MessageContext var1) {
      WlMessageContext var2 = WlMessageContext.narrow(var1);
      JavaClassComponent var3 = (JavaClassComponent)((WsSkel)var2.getDispatcher().getWsPort().getEndpoint()).getComponent();
      ContainerMarker var4 = this.getContainer(var2, var3);
      var4.setMessageContext(var2);
      return true;
   }

   private ContainerMarker getContainer(WlMessageContext var1, JavaClassComponent var2) {
      Object var3 = ContainerFactory.getContainer(var1);
      if (var3 == null) {
         try {
            var3 = ContainerFactory.createContainer(var1, var2.createTarget(), true);
         } catch (ComponentException var5) {
            throw new InvokeException(var5.getMessage(), var5);
         }
      } else {
         ((ContainerMarker)var3).setMessageContext(var1);
      }

      return (ContainerMarker)var3;
   }

   public boolean handleResponse(MessageContext var1) {
      ContainerMarker var2 = (ContainerMarker)var1.getProperty("weblogic.wsee.jws.container");
      if (var2 != null && var1.getProperty("weblogic.wsee.conversation.ConversationId") == null) {
         var2.destroy();
      }

      return true;
   }

   public QName[] getHeaders() {
      return new QName[0];
   }
}
