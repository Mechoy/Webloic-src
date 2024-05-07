package weblogic.wsee.ws;

import javax.naming.Context;
import weblogic.jndi.factories.java.javaURLContextFactory;
import weblogic.wsee.component.Component;
import weblogic.wsee.connection.Connection;
import weblogic.wsee.connection.transport.jms.JMSServerTransport;
import weblogic.wsee.connection.transport.rmi.RMIServerTransport;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.ws.dispatch.server.ServerDispatcher;
import weblogic.wsee.wsdl.WsdlPortType;

public class WsSkel extends WsEndpointImpl {
   private Component component;
   private ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
   private Context envCtx;

   WsSkel(WsService var1, WsdlPortType var2) {
      super(var1, var2);
   }

   public Context getContext() {
      return this.envCtx;
   }

   void setContext(Context var1) {
      this.envCtx = var1;
   }

   public Component getComponent() {
      return this.component;
   }

   void setComponent(Component var1) {
      assert var1 != null;

      this.component = var1;
   }

   public ClassLoader getClassLoader() {
      return this.classLoader;
   }

   public void invoke(Connection var1, WsPort var2) throws WsException {
      Thread var3 = Thread.currentThread();
      ClassLoader var4 = var3.getContextClassLoader();

      try {
         var3.setContextClassLoader(this.classLoader);
         ServerDispatcher var5 = new ServerDispatcher();
         var5.setWsPort(var2);
         var5.setConnection(var1);
         var5.dispatch();
      } finally {
         var3.setContextClassLoader(var4);
      }

   }

   public void invoke(Connection var1, WsPort var2, WlMessageContext var3) throws WsException {
      Thread var4 = Thread.currentThread();
      ClassLoader var5 = var4.getContextClassLoader();
      boolean var6 = var3.getProperty("weblogic.wsee.local.transport.prior.context") != null;
      boolean var7 = false;
      boolean var8 = false;

      try {
         var4.setContextClassLoader(this.classLoader);
         var7 = true;
         if ((var6 || var1.getTransport() instanceof RMIServerTransport || var1.getTransport() instanceof JMSServerTransport) && this.envCtx != null) {
            javaURLContextFactory.pushContext(this.envCtx);
            var8 = true;
         }

         ServerDispatcher var9 = new ServerDispatcher(var3);
         var9.setWsPort(var2);
         var9.setConnection(var1);
         var9.dispatch();
      } finally {
         if (var7) {
            var4.setContextClassLoader(var5);
         }

         if (var8) {
            javaURLContextFactory.popContext();
         }

      }

   }
}
