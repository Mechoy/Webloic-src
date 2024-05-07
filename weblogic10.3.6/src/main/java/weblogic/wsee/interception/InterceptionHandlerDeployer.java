package weblogic.wsee.interception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerInfo;
import weblogic.wsee.handler.HandlerException;
import weblogic.wsee.handler.HandlerList;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.WsService;
import weblogic.wsee.ws.init.WsDeploymentContext;
import weblogic.wsee.ws.init.WsDeploymentException;
import weblogic.wsee.ws.init.WsDeploymentListener;

public class InterceptionHandlerDeployer implements WsDeploymentListener {
   public void process(WsDeploymentContext var1) throws WsDeploymentException {
      try {
         WsService var2 = var1.getWsService();
         Iterator var3 = var2.getPorts();

         while(var3.hasNext()) {
            WsPort var4 = (WsPort)var3.next();
            HandlerList var5 = var4.getInternalHandlerList();
            HandlerInfo var6 = new HandlerInfo(InterceptionHandler.class, new HashMap(), (QName[])null);
            ArrayList var7 = new ArrayList();
            ArrayList var8 = new ArrayList();
            var8.add("CONNECTION_HANDLER");

            try {
               var5.insert("InterceptionHandler", var6, var8, var7);
            } catch (HandlerException var10) {
               throw new WsDeploymentException("Failed register handler", var10);
            }
         }
      } catch (Exception var11) {
         var11.printStackTrace();
      }

   }
}
