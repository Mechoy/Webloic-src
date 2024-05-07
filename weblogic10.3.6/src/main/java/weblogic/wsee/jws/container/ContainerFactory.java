package weblogic.wsee.jws.container;

import javax.xml.rpc.handler.MessageContext;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.util.Verbose;

public class ContainerFactory {
   public static final String CONTAINER = "weblogic.wsee.jws.container";

   public static Container getContainer(WlMessageContext var0, Class var1) {
      Container var2 = getContainer(var0);
      if (var2 == null) {
         var2 = (Container)createContainer(var0, createTarget(var1), false);
      }

      return var2;
   }

   public static Container getContainer(WlMessageContext var0, Object var1) {
      Container var2 = getContainer(var0);
      if (var2 == null) {
         var2 = (Container)createContainer(var0, var1, false);
      }

      return var2;
   }

   public static Container getContainer(MessageContext var0) {
      Object var1 = (ContainerMarker)var0.getProperty("weblogic.wsee.jws.container");
      if (var1 instanceof ContainerPlaceholder) {
         String var2 = ((ContainerMarker)var1).getId();
         var1 = var2 != null ? new ConversationalContainer(((ContainerMarker)var1).getTargetJWS(), ((ContainerMarker)var1).getUnfilteredMessageContext(), var2) : new Container(((ContainerMarker)var1).getTargetJWS(), ((ContainerMarker)var1).getUnfilteredMessageContext());
      }

      if (var1 != null) {
         ((ContainerMarker)var1).setMessageContext(WlMessageContext.narrow(var0));
      }

      return (Container)var1;
   }

   static ContainerMarker createContainer(WlMessageContext var0, Object var1, boolean var2) {
      Object var3 = null;
      String var4 = (String)var0.getProperty("weblogic.wsee.conversation.ConversationId");
      if (var2) {
         var3 = new ContainerPlaceholder(var1, var0, var4);
      } else if (var4 == null) {
         var3 = new Container(var1, var0);
      } else {
         var3 = new ConversationalContainer(var1, var0, var4);
      }

      ((ContainerMarker)var3).setMessageContext(var0);
      return (ContainerMarker)var3;
   }

   private static Object createTarget(Class var0) {
      try {
         return var0.newInstance();
      } catch (InstantiationException var2) {
         Verbose.logException(var2);
         throw new InvokeException(var2.getMessage(), var2);
      } catch (IllegalAccessException var3) {
         Verbose.logException(var3);
         throw new InvokeException(var3.getMessage(), var3);
      }
   }
}
