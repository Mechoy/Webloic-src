package weblogic.wsee.jaxws.owsm;

import com.sun.xml.ws.api.WSBinding;
import weblogic.wsee.jaxws.framework.jaxrpc.EnvironmentFactory;
import weblogic.wsee.ws.init.WsDeploymentContext;

public class DeploymentContext extends WsDeploymentContext {
   private EnvironmentFactory envFactory;

   public DeploymentContext(EnvironmentFactory var1) {
      this.envFactory = var1;
   }

   public WSBinding getBinding() {
      return this.envFactory.getBinding();
   }

   public EnvironmentFactory getEnvFactory() {
      return this.envFactory;
   }
}
