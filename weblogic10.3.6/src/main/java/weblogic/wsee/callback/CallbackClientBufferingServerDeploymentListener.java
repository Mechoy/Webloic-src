package weblogic.wsee.callback;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import weblogic.jws.Callback;
import weblogic.jws.MessageBuffer;
import weblogic.wsee.async.AsyncUtil;
import weblogic.wsee.buffer.BufferManager;
import weblogic.wsee.handler.InvocationException;
import weblogic.wsee.server.ServerUtil;
import weblogic.wsee.server.jms.WsClientBufferingErrorMessageListener;
import weblogic.wsee.server.jms.WsClientBufferingMessageListener;
import weblogic.wsee.util.JmsUtil;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.init.WsDeploymentContext;
import weblogic.wsee.ws.init.WsDeploymentException;
import weblogic.wsee.ws.init.WsDeploymentListener;

public class CallbackClientBufferingServerDeploymentListener implements WsDeploymentListener {
   private static final boolean verbose = Verbose.isVerbose(CallbackClientBufferingServerDeploymentListener.class);

   public void process(WsDeploymentContext var1) throws WsDeploymentException {
      if (verbose) {
         Verbose.log((Object)"CallbackClientBufferingServerDeploymentListener firing");
      }

      Iterator var2 = var1.getWsService().getPorts();

      while(var2.hasNext()) {
         WsPort var3 = (WsPort)var2.next();
         Class var4 = var3.getEndpoint().getJwsClass();
         if (var4 != null) {
            this.checkBufferedCallback(var4, var1);
         }
      }

   }

   private void checkBufferedCallback(Class var1, WsDeploymentContext var2) throws WsDeploymentException {
      Field[] var3 = var1.getDeclaredFields();
      Field[] var4 = var3;
      int var5 = var3.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Field var7 = var4[var6];
         if (var7.getAnnotation(Callback.class) != null && var7.getType().isInterface()) {
            Class var8 = var7.getType();
            boolean var9 = false;
            if (var8.getAnnotation(MessageBuffer.class) != null) {
               var9 = true;
            } else {
               Method[] var10 = var8.getMethods();
               Method[] var11 = var10;
               int var12 = var10.length;

               for(int var13 = 0; var13 < var12; ++var13) {
                  Method var14 = var11[var13];
                  if (var14.getAnnotation(MessageBuffer.class) != null) {
                     var9 = true;
                     break;
                  }
               }
            }

            if (var9) {
               this.setupBufferedCallback(var2, ServerUtil.getBufferQueueInfo(var8), var8);
               return;
            }
         }
      }

   }

   private void setupBufferedCallback(WsDeploymentContext var1, ServerUtil.QueueInfo var2, Class var3) throws WsDeploymentException {
      WsClientBufferingMessageListener var4 = new WsClientBufferingMessageListener();
      String var5 = var1.getContextPath() + "/" + var1.getServiceName() + ":/" + var3.getName().replace(".", "/");
      BufferManager var6 = BufferManager.instance();
      synchronized(var6) {
         if (var6.getMessageListener(var5) != null) {
            return;
         }

         var6.addMessageListener(var5, var4);
      }

      var1.addBufferTargetURI(var5);
      if (var2.getQueueName() != null) {
         var6.setTargetQueue(var5, var2);
      }

      String var7 = new String("ASYNC_URI = '" + var5 + "'");
      AsyncUtil.setupDynamicMDB(var1, var7, var2.getQueueName(), var2.getMdbRunAsPrincipalName(), "weblogic.wsee.server.jms.MdbWS", 0);
      this.setupErrorDestination(var1, var2, var5);
   }

   private void setupErrorDestination(WsDeploymentContext var1, ServerUtil.QueueInfo var2, String var3) throws WsDeploymentException {
      try {
         String var4 = JmsUtil.getErrorDestinationJNDI(var2.getQueueName());
         if (var4 != null) {
            WsClientBufferingErrorMessageListener var5 = new WsClientBufferingErrorMessageListener();
            BufferManager var6 = BufferManager.instance();
            var6.addErrorListener(var3, var5);
            String var7 = new String("ASYNC_URI = '" + var3 + "'");
            AsyncUtil.setupDynamicMDB(var1, var7, var4, var2.getMdbRunAsPrincipalName(), "weblogic.wsee.server.jms.MdbErrorWS", 0);
         }

      } catch (InvocationException var8) {
         if (verbose) {
            Verbose.logException(var8);
         }

         throw new WsDeploymentException(var8.getMessage(), var8);
      }
   }
}
