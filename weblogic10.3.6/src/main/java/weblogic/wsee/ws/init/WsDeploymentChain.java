package weblogic.wsee.ws.init;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import weblogic.wsee.util.Verbose;

public class WsDeploymentChain implements WsDeploymentListener {
   private static final boolean verbose = Verbose.isVerbose(WsDeploymentChain.class);
   private List<WsDeploymentListenerConfig> listenerList = new ArrayList();

   private WsDeploymentChain(List<WsDeploymentListenerConfig> var1, List<WsDeploymentListenerConfig> var2) {
      if (var2 != null) {
         this.listenerList.addAll(var2);
      }

      if (var1 != null) {
         this.listenerList.addAll(var1);
      }

   }

   public static WsDeploymentListener newClientChain(List<WsDeploymentListenerConfig> var0) {
      return new WsDeploymentChain(var0, WsConfigManager.getInstance().getDeploymentListeners().getClientListeners());
   }

   public static WsDeploymentListener newServerChain(List<WsDeploymentListenerConfig> var0) {
      return new WsDeploymentChain(var0, WsConfigManager.getInstance().getDeploymentListeners().getServerListeners());
   }

   public void process(WsDeploymentContext var1) throws WsDeploymentException {
      WsDeploymentListener var3;
      for(Iterator var2 = this.loadListeners().iterator(); var2.hasNext(); var3.process(var1)) {
         var3 = (WsDeploymentListener)var2.next();
         if (verbose) {
            Verbose.log((Object)("Processing WsDeploymentListener: " + var3.getClass().getName()));
         }
      }

   }

   private List<WsDeploymentListener> loadListeners() {
      ArrayList var1 = new ArrayList(this.listenerList.size());
      Iterator var2 = this.listenerList.iterator();

      while(var2.hasNext()) {
         WsDeploymentListenerConfig var3 = (WsDeploymentListenerConfig)var2.next();
         var1.add(var3.newInstance());
      }

      return var1;
   }
}
