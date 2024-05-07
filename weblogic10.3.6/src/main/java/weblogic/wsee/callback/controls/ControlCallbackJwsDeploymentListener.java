package weblogic.wsee.callback.controls;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerInfo;
import weblogic.jws.wlw.WLWServiceControlCallbackJWS;
import weblogic.wsee.cluster.ForwardingHandler;
import weblogic.wsee.handler.HandlerException;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.init.WsDeploymentContext;
import weblogic.wsee.ws.init.WsDeploymentException;
import weblogic.wsee.ws.init.WsDeploymentListener;

public class ControlCallbackJwsDeploymentListener implements WsDeploymentListener {
   private static final boolean verbose = Verbose.isVerbose(ControlCallbackJwsDeploymentListener.class);
   private static final List HANDLER_BEFORE = Arrays.asList((Object[])(new String[]{"ADDRESSING_HANDLER"}));
   private static final List HANDLER_AFTER = Arrays.asList((Object[])(new String[]{"CONNECTION_HANDLER", "SECURITY_HANDLER"}));

   public void process(WsDeploymentContext var1) throws WsDeploymentException {
      if (verbose) {
         Verbose.log((Object)"ControlCallbackJwsDeploymentListener firing");
      }

      Iterator var2 = var1.getWsService().getPorts();

      while(var2.hasNext()) {
         WsPort var3 = (WsPort)var2.next();
         Class var4 = var3.getEndpoint().getJwsClass();
         if (var4 != null && this.isCallbackJws(var4) && !var3.getInternalHandlerList().contains("FORWARDING_HANDLER")) {
            HandlerInfo var5 = new HandlerInfo(ForwardingHandler.class, new HashMap(), (QName[])null);
            HandlerInfo var6 = new HandlerInfo(Wlw81ControlCallbackHeaderTranslationHandler.class, new HashMap(), (QName[])null);

            try {
               if (verbose) {
                  try {
                     Verbose.log((Object)("Adding ForwardingHandler for " + var3.getWsdlPort().getPortAddress().getServiceuri()));
                  } catch (Exception var8) {
                     Verbose.log((Object)("Adding ForwardingHandler for " + var1.getServiceURIs()[0]));
                  }
               }

               int var7 = var3.getInternalHandlerList().lenientInsert("WLW81_CONTROL_CALLBACK_TRANSLATION_HANDLER", var6, HANDLER_AFTER, HANDLER_BEFORE);
               var3.getInternalHandlerList().insert("FORWARDING_HANDLER", var7 + 1, var5);
            } catch (HandlerException var9) {
               throw new WsDeploymentException("Could not insert ForwardingHandler", var9);
            }
         }
      }

   }

   private boolean isCallbackJws(Class var1) {
      return var1.getAnnotation(WLWServiceControlCallbackJWS.class) != null;
   }
}
