package weblogic.wsee.callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerInfo;
import weblogic.wsee.handler.HandlerException;
import weblogic.wsee.handler.HandlerList;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.WsService;
import weblogic.wsee.ws.init.WsDeploymentContext;
import weblogic.wsee.ws.init.WsDeploymentException;
import weblogic.wsee.ws.init.WsDeploymentListener;

public class CallbackServiceDeploymentListener implements WsDeploymentListener {
   private static final boolean verbose = Verbose.isVerbose(CallbackServiceDeploymentListener.class);

   public void process(WsDeploymentContext var1) throws WsDeploymentException {
      WsService var2 = var1.getWsService();
      Iterator var3 = var2.getPorts();

      while(var3.hasNext()) {
         WsPort var4 = (WsPort)var3.next();
         HandlerList var5 = var4.getInternalHandlerList();
         if (verbose) {
            Verbose.log((Object)"Before CallbackServiceDeploymentListener");
            Verbose.log((Object)var5);
         }

         HandlerInfo var6 = new HandlerInfo(CallbackServiceHandler.class, new HashMap(), (QName[])null);
         ArrayList var7 = new ArrayList();
         ArrayList var8 = new ArrayList();
         var8.add("ONE_WAY_HANDLER");
         var7.add("PRE_INVOKE_HANDLER");

         try {
            var5.insert("CALLBACK_SERVICE_HANDLER", var6, var8, var7);
         } catch (HandlerException var10) {
            throw new WsDeploymentException("Failed to register handler", var10);
         }

         if (verbose) {
            Verbose.log((Object)"After CallbackServiceDeploymentListener");
            Verbose.log((Object)var5);
         }
      }

   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.end();
   }
}
