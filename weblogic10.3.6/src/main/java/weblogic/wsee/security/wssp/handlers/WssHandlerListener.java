package weblogic.wsee.security.wssp.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.xml.rpc.handler.MessageContext;

public abstract class WssHandlerListener {
   private static final String WSS_HANDLER_LISTENERS = "weblogic.wsee.security.wssp.handlers.listeners";

   public static void register(MessageContext var0, WssHandlerListener var1) {
      Object var2 = (List)var0.getProperty("weblogic.wsee.security.wssp.handlers.listeners");
      if (var2 == null) {
         var2 = new ArrayList();
         var0.setProperty("weblogic.wsee.security.wssp.handlers.listeners", var2);
      }

      ((List)var2).add(var1);
   }

   public static void registerToProperties(Map var0, WssHandlerListener var1) {
      Object var2 = (List)var0.get("weblogic.wsee.security.wssp.handlers.listeners");
      if (var2 == null) {
         var2 = new ArrayList();
         var0.put("weblogic.wsee.security.wssp.handlers.listeners", var2);
      }

      ((List)var2).add(var1);
   }

   public static List<WssHandlerListener> retreive(MessageContext var0) {
      return (List)var0.getProperty("weblogic.wsee.security.wssp.handlers.listeners");
   }

   public void preHandlingRequest(MessageContext var1) {
   }

   public void preHandlingResponse(MessageContext var1) {
   }

   public void postHandlingRequest(MessageContext var1) {
   }

   public void postHandlingResponse(MessageContext var1) {
   }

   public abstract boolean isDisposed();
}
