package weblogic.wsee.buffer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerInfo;
import weblogic.jws.MessageBuffer;
import weblogic.jws.wlw.UseWLW81BindingTypes;
import weblogic.wsee.handler.HandlerException;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.init.WsDeploymentContext;
import weblogic.wsee.ws.init.WsDeploymentException;
import weblogic.wsee.ws.init.WsDeploymentListener;

public class Wlw81CompatBufferDeploymentListener implements WsDeploymentListener {
   private static final boolean verbose = Verbose.isVerbose(Wlw81CompatBufferDeploymentListener.class);
   private static final List BEFORE_RESPONDER = Arrays.asList((Object[])(new String[]{"SERVER_BUFFER_HANDLER"}));
   private static final List AFTER_RESPONDER = Arrays.asList((Object[])(new String[]{"ONE_WAY_HANDLER"}));
   private static final List BEFORE_RESPONSE_BLOCKER = new ArrayList();
   private static final List AFTER_RESPONSE_BLOCKER = Arrays.asList((Object[])(new String[]{"CONNECTION_HANDLER"}));
   private static final String RESPONDER_HANDLER_NAME = "WLW81_COMPAT_SERVER_BUFFER_HANDLER";
   private static final String RESPONSE_BLOCKER_HANDLER_NAME = "WLW81_COMPAT_SERVER_RESPONSE_BLOCKER_HANDLER";
   private static final HandlerInfo RESPONDER_HANDLER_INFO = new HandlerInfo(Wlw81CompatBufferingHandler.class, (Map)null, new QName[0]);
   private static final HandlerInfo RESPONSE_BLOCKER_HANDLER_INFO = new HandlerInfo(Wlw81CompatBufferingResponseBlockerHandler.class, (Map)null, new QName[0]);

   public void process(WsDeploymentContext var1) throws WsDeploymentException {
      if (verbose) {
         Verbose.log((Object)"Wlw81CompatBufferDeploymentListener firing");
      }

      Iterator var2 = var1.getWsService().getPorts();

      while(true) {
         while(var2.hasNext()) {
            WsPort var3 = (WsPort)var2.next();
            Class var4 = var3.getEndpoint().getJwsClass();
            if (var4 != null && this.isWlw81UpgradedService(var4)) {
               if (this.hasBufferedMethods(var4)) {
                  if (verbose) {
                     Verbose.log((Object)(var4.getName() + " is wlw81 AND has buffered methods"));
                  }

                  try {
                     var3.getInternalHandlerList().insert("WLW81_COMPAT_SERVER_BUFFER_HANDLER", RESPONDER_HANDLER_INFO, AFTER_RESPONDER, BEFORE_RESPONDER);
                     var3.getInternalHandlerList().insert("WLW81_COMPAT_SERVER_RESPONSE_BLOCKER_HANDLER", RESPONSE_BLOCKER_HANDLER_INFO, AFTER_RESPONSE_BLOCKER, BEFORE_RESPONSE_BLOCKER);
                     if (verbose) {
                        Verbose.log((Object)("Added WLW buffering handlers for " + var4.getSimpleName()));
                     }
                  } catch (HandlerException var6) {
                     throw new WsDeploymentException("Could not insert WLW buffering handlers", var6);
                  }
               } else if (verbose) {
                  Verbose.log((Object)(var4.getClass().getName() + " is upgraded 8.1 JWS, but has no buffered methods"));
               }
            } else if (verbose && var4 != null) {
               Verbose.log((Object)(var4.getClass().getName() + " is not an upgraded 8.1 JWS"));
            }
         }

         return;
      }
   }

   private boolean isWlw81UpgradedService(Class var1) {
      return var1.isAnnotationPresent(UseWLW81BindingTypes.class);
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
