package weblogic.wsee.jws.container;

import java.lang.reflect.InvocationTargetException;
import javax.xml.rpc.server.ServletEndpointContext;
import weblogic.wsee.message.WlMessageContext;

public class ContainerDispatcherImpl extends ContainerDispatcher {
   public Response dispatch(WlMessageContext var1, Request var2, ServletEndpointContext var3) throws Exception {
      Container var4 = ContainerFactory.getContainer(var1, var2.getTargetClass());
      var4.setServletContext(var3);
      return invoke(var1, var4, var2);
   }

   public Response dispatch(WlMessageContext var1, Request var2) throws Exception {
      Container var3 = ContainerFactory.getContainer(var1, var2.getTargetClass());
      return invoke(var1, var3, var2);
   }

   static Response invoke(WlMessageContext var0, Container var1, Request var2) throws Exception {
      Response var3 = new Response();
      boolean var4 = false;

      Response var5;
      try {
         preInvoke(var0, var1);
         var4 = true;
         var3.retval = var2.getMethod().invoke(var1.getTargetJWS(), var2.getMethodArgValues());
         var5 = var3;
      } catch (InvocationTargetException var13) {
         Throwable var6 = var13.getCause();
         if (var6 instanceof Exception) {
            Exception var7 = (Exception)var6;
            var1.getListeners().onException(var7, var2.getMethod().getName(), var2.getMethodArgValues());
            throw var7;
         }

         throw new InvokeException("Error Invoking " + var1.getTargetJWS().getClass().getName(), var6);
      } catch (Throwable var14) {
         throw new InvokeException("Error Invoking " + var1.getTargetJWS().getClass().getName(), var14);
      } finally {
         if (var4) {
            postInvoke(var0, var1);
         }

      }

      return var5;
   }

   private static void preInvoke(WlMessageContext var0, Container var1) throws Exception {
      if (var1 instanceof ConversationalContainer) {
         ConversationLifeCycleHandler var2 = new ConversationLifeCycleHandler();
         var2.handleRequest(var0);
      }

      ContainerListenerHandler var3 = new ContainerListenerHandler();
      var3.handleRequest(var0);
   }

   private static void postInvoke(WlMessageContext var0, Container var1) throws Exception {
      ContainerListenerHandler var2 = new ContainerListenerHandler();
      var2.handleResponse(var0);
      if (var1 instanceof ConversationalContainer) {
         ConversationLifeCycleHandler var3 = new ConversationLifeCycleHandler();
         var3.handleResponse(var0);
      }

   }
}
