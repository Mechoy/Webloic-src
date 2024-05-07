package weblogic.wsee.buffer;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerInfo;
import weblogic.jws.MessageBuffer;
import weblogic.wsee.async.AsyncUtil;
import weblogic.wsee.handler.HandlerException;
import weblogic.wsee.server.ServerUtil;
import weblogic.wsee.server.jms.WsDispatchMessageListener;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.init.WsDeploymentContext;
import weblogic.wsee.ws.init.WsDeploymentException;
import weblogic.wsee.ws.init.WsDeploymentListener;

public class ServerBufferDeploymentListener implements WsDeploymentListener {
   private static final boolean verbose = Verbose.isVerbose(ServerBufferDeploymentListener.class);
   private static final List BEFORE = Arrays.asList((Object[])(new String[]{"AUTHORIZATION_HANDLER"}));
   private static final List AFTER = Arrays.asList((Object[])(new String[]{"ONE_WAY_HANDLER"}));
   private static final String HANDLER_NAME = "SERVER_BUFFER_HANDLER";
   private static final HandlerInfo HANDLER_INFO = new HandlerInfo(ServerBufferingHandler.class, (Map)null, new QName[0]);

   public void process(WsDeploymentContext var1) throws WsDeploymentException {
      if (verbose) {
         Verbose.log((Object)"ServerBufferDeploymentListener firing");
      }

      Iterator var2 = var1.getWsService().getPorts();

      while(true) {
         WsPort var3;
         Class var4;
         do {
            do {
               if (!var2.hasNext()) {
                  return;
               }

               var3 = (WsPort)var2.next();
               var4 = var3.getEndpoint().getJwsClass();
            } while(var4 == null);
         } while(!this.hasBufferedMethods(var4));

         if (verbose) {
            Verbose.log((Object)(var4.getName() + " has buffered methods"));
         }

         try {
            var3.getInternalHandlerList().insert("SERVER_BUFFER_HANDLER", HANDLER_INFO, AFTER, BEFORE);
            if (verbose) {
               Verbose.log((Object)("Added buffering handlers for " + var4.getSimpleName()));
            }
         } catch (HandlerException var6) {
            throw new WsDeploymentException("Could not insert server buffering handler", var6);
         }

         this.setupBuffering(var4, var3, var1);
      }
   }

   private void setupBuffering(Class var1, WsPort var2, WsDeploymentContext var3) throws WsDeploymentException {
      ServerUtil.QueueInfo var4 = ServerUtil.getBufferQueueInfo(var1);
      StringBuffer var5 = new StringBuffer();
      WsDispatchMessageListener var6 = new WsDispatchMessageListener(var2);
      boolean var7 = true;

      for(int var8 = 0; var8 < var3.getServiceURIs().length; ++var8) {
         String var9 = var3.getContextPath();
         String var10 = var3.getServiceURIs()[var8];
         String var11 = AsyncUtil.calculateServiceTargetURI(var9, var10);
         BufferManager var12 = BufferManager.instance();
         synchronized(var12) {
            if (var12.getMessageListener(var11) != null) {
               var7 = false;
               continue;
            }

            var12.addMessageListener(var11, var6);
         }

         if (var4.getQueueName() != null) {
            var12.setTargetQueue(var11, var4);
         }

         var3.addBufferTargetURI(var11);
         if (var5.length() == 0) {
            var5.append(AsyncUtil.getAsyncSelector(var11));
         } else {
            var5.append(" OR (" + AsyncUtil.getAsyncSelector(var11) + ")");
         }
      }

      if (var7) {
         if (verbose) {
            Verbose.log((Object)("Set up dynamic MDB to queue: " + var4.getQueueName()));
         }

         AsyncUtil.setupDynamicMDB(var3, var5.toString(), var4.getQueueName(), var4.getMdbRunAsPrincipalName(), "weblogic.wsee.server.jms.MdbWS", 0);
      }

   }

   private boolean hasBufferedMethods(Class var1) {
      if (var1.getAnnotation(MessageBuffer.class) != null) {
         return true;
      } else {
         Method[] var2 = var1.getMethods();
         Method[] var3 = var2;
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Method var6 = var3[var5];
            if (var6.getAnnotation(MessageBuffer.class) != null) {
               return true;
            }
         }

         return false;
      }
   }
}
