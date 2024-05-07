package weblogic.wsee.jaxws.owsm;

import com.sun.xml.ws.api.server.WSEndpoint;
import java.util.Map;
import weblogic.wsee.jaxws.framework.jaxrpc.PropertyConverter;
import weblogic.wsee.ws.init.WsDeploymentContext;

public class ServerEnvironmentFactory extends weblogic.wsee.jaxws.framework.jaxrpc.server.ServerEnvironmentFactory {
   private static Map<String, PropertyConverter> jaxrpcConverters;

   public ServerEnvironmentFactory(WSEndpoint<?> var1) {
      super(var1);
   }

   protected WsDeploymentContext newDeploymentContext() {
      return new DeploymentContext(this);
   }

   public WsDeploymentContext getDeploymentContext() {
      return this.buildDeploymentContext(this.getServiceUris(), this.getContextPath(), this.getServiceName());
   }

   public Map<String, PropertyConverter> getJAXRPCConverters() {
      if (jaxrpcConverters == null) {
         this.initializeConverters();
         jaxrpcConverters.putAll(super.getJAXRPCConverters());
      }

      return jaxrpcConverters;
   }

   private void initializeConverters() {
      PropertyConverters var1 = new PropertyConverters(this);
      jaxrpcConverters = var1.getConverters();
   }
}
