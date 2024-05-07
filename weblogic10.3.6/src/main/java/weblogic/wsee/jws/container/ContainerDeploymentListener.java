package weblogic.wsee.jws.container;

import java.util.HashMap;
import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerInfo;
import weblogic.wsee.handler.HandlerException;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.init.WsDeploymentContext;
import weblogic.wsee.ws.init.WsDeploymentException;
import weblogic.wsee.ws.init.WsDeploymentListener;

public class ContainerDeploymentListener implements WsDeploymentListener {
   private static final boolean verbose = Verbose.isVerbose(ContainerDeploymentListener.class);

   public void process(WsDeploymentContext var1) throws WsDeploymentException {
      if (verbose) {
         Verbose.log((Object)"ContainerDeploymentListener firing");
      }

      Iterator var2 = var1.getWsService().getPorts();

      while(var2.hasNext()) {
         WsPort var3 = (WsPort)var2.next();
         if (Container.isContainerRequired(var3)) {
            if (verbose) {
               Verbose.log((Object)("Adding " + this.getClass().getName() + " for " + var1.getServiceURIs()[0]));
            }

            try {
               HandlerInfo var4 = new HandlerInfo(ContainerHandler.class, new HashMap(), (QName[])null);
               var3.getInternalHandlerList().insert("CONTAINER_HANDLER", var3.getInternalHandlerList().size() - 1, var4);
            } catch (HandlerException var5) {
               throw new WsDeploymentException("Unable to register handler", var5);
            }
         }
      }

   }
}
