package weblogic.wsee.jaxws.owsm;

import com.sun.xml.ws.api.pipe.ClientTubeAssemblerContext;
import java.util.Map;
import weblogic.j2ee.descriptor.wl.PortInfoBean;
import weblogic.wsee.jaxws.framework.ConfigUtil;
import weblogic.wsee.jaxws.framework.jaxrpc.PropertyConverter;
import weblogic.wsee.ws.init.WsDeploymentContext;

public class ClientEnvironmentFactory extends weblogic.wsee.jaxws.framework.jaxrpc.client.ClientEnvironmentFactory {
   private static Map<String, PropertyConverter> jaxrpcConverters;
   private PortInfoBean portBean = null;

   public ClientEnvironmentFactory(ClientTubeAssemblerContext var1) {
      super(var1);
      this.portBean = ConfigUtil.getPortInfoBeanForClient(var1);
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

   public PortInfoBean getPortBean() {
      return this.portBean;
   }
}
