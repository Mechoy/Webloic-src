package weblogic.wsee.callback;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerInfo;
import weblogic.wsee.handler.HandlerException;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.init.WsDeploymentContext;
import weblogic.wsee.ws.init.WsDeploymentException;
import weblogic.wsee.ws.init.WsDeploymentListener;

public class CallbackClientBufferingDeploymentListener implements WsDeploymentListener {
   private static final boolean verbose = Verbose.isVerbose(CallbackClientBufferingDeploymentListener.class);
   private static final List BEFORE = Arrays.asList((Object[])(new String[]{"SECURITY_HANDLER", "CONNECTION_HANDLER"}));
   private static final List AFTER = Arrays.asList((Object[])(new String[]{"ASYNC_HANDLER"}));

   public void process(WsDeploymentContext var1) throws WsDeploymentException {
      if (verbose) {
         Verbose.log((Object)"CallbackClientBufferingDeploymentListener firing");
      }

      Iterator var2 = var1.getWsService().getPorts();

      while(var2.hasNext()) {
         WsPort var3 = (WsPort)var2.next();
         if (CallbackUtils.isCallbackService(var3)) {
            HandlerInfo var4 = new HandlerInfo(CallbackClientBufferingHandler.class, (Map)null, (QName[])null);

            try {
               var3.getInternalHandlerList().lenientInsert("CALLBACK_CLIENT_BUFFERING_HANDLER", var4, AFTER, BEFORE);
            } catch (HandlerException var6) {
               throw new WsDeploymentException("Could not insert callback client buffering handler", var6);
            }
         }
      }

   }
}
