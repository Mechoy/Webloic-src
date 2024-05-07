package weblogic.wsee.workarea;

import java.util.Arrays;
import java.util.Collections;
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

public class WorkAreaClientDeploymentListener implements WsDeploymentListener {
   private static final boolean verbose = Verbose.isVerbose(WorkAreaClientDeploymentListener.class);
   private static final List BEFORE;
   private static final List AFTER;
   private static final String HANDLER_NAME = "WorkAreaClientHandler";

   public void process(WsDeploymentContext var1) throws WsDeploymentException {
      if (verbose) {
         Verbose.log((Object)"Running WorkAreaClientDeploymentListener");
      }

      Iterator var2 = var1.getWsService().getPorts();

      while(var2.hasNext()) {
         WsPort var3 = (WsPort)var2.next();

         try {
            if (!var3.getInternalHandlerList().contains("WorkAreaClientHandler")) {
               if (verbose) {
                  Verbose.log((Object)"Adding workarea handler");
               }

               HandlerInfo var4 = new HandlerInfo(WorkAreaClientHandler.class, (Map)null, (QName[])null);
               var3.getInternalHandlerList().insert("WorkAreaClientHandler", var4, AFTER, BEFORE);
            } else if (verbose) {
               Verbose.log((Object)"Workarea handler is already added");
            }
         } catch (HandlerException var5) {
            throw new WsDeploymentException("Could not insert workarea server handler", var5);
         }
      }

   }

   static {
      BEFORE = Collections.EMPTY_LIST;
      AFTER = Arrays.asList((Object[])(new String[]{"CODEC_HANDLER"}));
   }
}
