package weblogic.wsee.ws.init;

class WsConfig {
   private WsDeploymentListeners deploymentListeners = new WsDeploymentListeners();

   WsDeploymentListeners getDeploymentListeners() {
      return this.deploymentListeners;
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof WsConfig)) {
         return false;
      } else {
         WsConfig var2 = (WsConfig)var1;
         return this.deploymentListeners.equals(var2);
      }
   }

   public int hashCode() {
      return this.deploymentListeners.hashCode();
   }
}
