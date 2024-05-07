package weblogic.wsee.ws.init;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import weblogic.wsee.util.HashCodeBuilder;

public class WsDeploymentListeners {
   private List<WsDeploymentListenerConfig> serverListeners = new ArrayList();
   private List<WsDeploymentListenerConfig> clientListeners = new ArrayList();

   WsDeploymentListeners() {
   }

   void addClientListener(WsDeploymentListenerConfig var1) {
      this.clientListeners.add(var1);
   }

   void addServerListener(WsDeploymentListenerConfig var1) {
      this.serverListeners.add(var1);
   }

   public List<WsDeploymentListenerConfig> getClientListeners() {
      return Collections.unmodifiableList(this.clientListeners);
   }

   public List<WsDeploymentListenerConfig> getServerListeners() {
      return Collections.unmodifiableList(this.serverListeners);
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof WsDeploymentListeners)) {
         return false;
      } else {
         WsDeploymentListeners var2 = (WsDeploymentListeners)var1;
         boolean var3 = this.clientListeners.equals(var2.clientListeners);
         var3 = var3 && this.serverListeners.equals(var2.serverListeners);
         return var3;
      }
   }

   public int hashCode() {
      HashCodeBuilder var1 = new HashCodeBuilder();
      var1.add(this.clientListeners);
      var1.add(this.serverListeners);
      return var1.hashCode();
   }
}
