package weblogic.wsee.jws.wlw;

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

public class WLW81CompatTxVoidReturnDeploymentListener implements WsDeploymentListener {
   private static final boolean verbose = Verbose.isVerbose(WLW81CompatTxVoidReturnDeploymentListener.class);
   private static final List BEFORE = Arrays.asList((Object[])(new String[]{"CONNECTION_HANDLER"}));
   private static final List AFTER = Arrays.asList((Object[])(new String[]{"ASYNC_HANDLER"}));
   private static final String HANDLER_NAME = "WLW81_COMPAT_TX_VOID_RETURN_CLIENT_HANDLER";
   private static final HandlerInfo HANDLER_INFO = new HandlerInfo(WLW81CompatTxVoidReturnClientHandler.class, (Map)null, new QName[0]);

   public void process(WsDeploymentContext var1) throws WsDeploymentException {
      if (verbose) {
         Verbose.log((Object)"WLW81CompatTxVoidReturnDeploymentListener firing");
      }

      Iterator var2 = var1.getWsService().getPorts();

      while(var2.hasNext()) {
         WsPort var3 = (WsPort)var2.next();
         if ("jms".equalsIgnoreCase(var3.getWsdlPort().getTransport())) {
            try {
               var3.getInternalHandlerList().insert("WLW81_COMPAT_TX_VOID_RETURN_CLIENT_HANDLER", HANDLER_INFO, AFTER, BEFORE);
               if (verbose) {
                  Verbose.log((Object)("Added client tx void return handler for " + var3.getWsdlPort().getName()));
               }
            } catch (HandlerException var5) {
               throw new WsDeploymentException("Could not insert WLW81CompatTxVoidReturnClientHandler handler", var5);
            }
         } else if (verbose) {
            Verbose.log((Object)(var3.getWsdlPort().getName() + " does not have jms transport. "));
         }
      }

   }
}
