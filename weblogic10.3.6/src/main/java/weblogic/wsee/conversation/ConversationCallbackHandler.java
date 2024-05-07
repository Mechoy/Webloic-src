package weblogic.wsee.conversation;

import java.lang.reflect.Method;
import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.MessageContext;
import weblogic.jws.CallbackMethod;
import weblogic.wsee.callback.CallbackInfoHeader;
import weblogic.wsee.component.pojo.JavaClassComponent;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.WsRegistry;
import weblogic.wsee.ws.WsSkel;

public class ConversationCallbackHandler extends ConversationHandler {
   private static final boolean verbose = Verbose.isVerbose(ConversationCallbackHandler.class);

   public boolean handleRequest(MessageContext var1) {
      WlMessageContext var2 = WlMessageContext.narrow(var1);
      CallbackInfoHeader var3 = (CallbackInfoHeader)var2.getHeaders().getHeader(CallbackInfoHeader.TYPE);
      if (var3 == null) {
         throw new JAXRPCException("No callback information header specified");
      } else {
         String var4 = var3.getStubName();
         String var5 = var3.getServiceURI();
         String var6 = null;
         if ((var6 = var3.getAppVersion()) != null) {
            if (verbose) {
               Verbose.log((Object)("Setting version to send callback to " + var6));
            }

            var2.setProperty("weblogic.wsee.callback.appversion", var6);
         } else if (verbose) {
            Verbose.log((Object)"No app version in callback info header");
         }

         var2.setProperty("weblogic.wsee.enclosing.jws.serviceuri", var5);
         WsPort var7 = WsRegistry.instance().lookup(var5, var6);
         if (var7 == null) {
            throw new JAXRPCException("No port found for " + var5);
         } else {
            var2.setProperty("weblogic.wsee.callback.class", var7.getEndpoint().getJwsClass().getName());
            JavaClassComponent var8 = (JavaClassComponent)((WsSkel)var7.getEndpoint()).getComponent();
            Class var9 = var8.getTargetClass();
            Method[] var10 = var9.getDeclaredMethods();
            QName var11 = (QName)var2.getProperty("weblogic.wsee.ws.server.OperationName");

            assert var11 != null;

            String var12 = var11.getLocalPart();
            boolean var13 = false;

            for(int var14 = 0; var14 < var10.length; ++var14) {
               CallbackMethod var15 = null;
               if ((var15 = (CallbackMethod)var10[var14].getAnnotation(CallbackMethod.class)) != null) {
                  String var16;
                  if (var4 != null) {
                     var16 = var15.target();
                     if (!var4.equals(var16)) {
                        continue;
                     }
                  }

                  var16 = var15.operation();

                  assert var16 != null;

                  if (var16.equals(var12)) {
                     var2.setProperty("weblogic.wsee.callback.method", var10[var14].getName());
                     var13 = true;
                     break;
                  }
               }
            }

            if (!var13) {
               throw new JAXRPCException("No callback method found");
            } else {
               ContinueHeader var17 = (ContinueHeader)var2.getHeaders().getHeader(ContinueHeader.TYPE);
               if (var17 != null) {
                  var2.setProperty("weblogic.wsee.conversation.ConversationId", var17.getConversationId());
                  var2.setProperty("weblogic.wsee.conversation.IsServerAssigned", new Boolean(var17.isServerAssigned()));
                  var2.setProperty("weblogic.wsee.conversation.ConversationPhase", ConversationPhase.CONTINUE);
               }

               return true;
            }
         }
      }
   }
}
