package weblogic.wsee.workarea;

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

public class WorkAreaDeploymentListener implements WsDeploymentListener {
   private static final boolean verbose = Verbose.isVerbose(WorkAreaDeploymentListener.class);
   private static final List BEFORE = Arrays.asList((Object[])(new String[]{"PRE_INVOKE_HANDLER"}));
   private static final List AFTER = Arrays.asList((Object[])(new String[]{"OPERATION_LOOKUP_HANDLER"}));
   private static final String HANDLER_NAME = "WorkAreaHandler";

   public void process(WsDeploymentContext var1) throws WsDeploymentException {
      if (verbose) {
         Verbose.log((Object)"WorkAreaDeploymentListener firing");
      }

      Iterator var2 = var1.getWsService().getPorts();

      while(var2.hasNext()) {
         WsPort var3 = (WsPort)var2.next();
         HandlerInfo var4 = new HandlerInfo(WorkAreaServerHandler.class, (Map)null, (QName[])null);
         if (verbose) {
            Verbose.log((Object)("Adding workarea handlers for " + var1.getServiceURIs()[0]));
         }

         try {
            var3.getInternalHandlerList().insert("WorkAreaHandler", var4, AFTER, BEFORE);
         } catch (HandlerException var6) {
            throw new WsDeploymentException("Could not insert workarea server handler", var6);
         }
      }

   }
}
