package weblogic.wsee.monitoring;

import weblogic.management.ManagementException;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.WseeClusterFrontEndRuntimeMBean;
import weblogic.management.runtime.WseeClusterRoutingRuntimeMBean;

public class WseeClusterFrontEndRuntimeMBeanImpl extends RuntimeMBeanDelegate implements WseeClusterFrontEndRuntimeMBean {
   private WseeClusterRoutingRuntimeMBeanImpl _clusterRouting = new WseeClusterRoutingRuntimeMBeanImpl("Default", this);

   public WseeClusterFrontEndRuntimeMBeanImpl(String var1, RuntimeMBean var2) throws ManagementException {
      super(var1, var2, false);
   }

   public WseeClusterRoutingRuntimeMBean getClusterRouting() {
      return this._clusterRouting;
   }

   public void register() throws ManagementException {
      if (!this.isRegistered()) {
         super.register();
         if (this._clusterRouting != null) {
            this._clusterRouting.register();
         }

      }
   }

   public void unregister() throws ManagementException {
      super.unregister();
      if (this._clusterRouting != null) {
         this._clusterRouting.unregister();
         this._clusterRouting = null;
      }

   }
}
