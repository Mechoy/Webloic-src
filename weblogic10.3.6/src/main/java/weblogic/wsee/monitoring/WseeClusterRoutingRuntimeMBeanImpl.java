package weblogic.wsee.monitoring;

import weblogic.management.ManagementException;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.WseeClusterRoutingRuntimeMBean;

public final class WseeClusterRoutingRuntimeMBeanImpl extends WseeRuntimeMBeanDelegate<WseeClusterRoutingRuntimeMBean, WseeClusterRoutingRuntimeData> implements WseeClusterRoutingRuntimeMBean {
   public WseeClusterRoutingRuntimeMBeanImpl(String var1, RuntimeMBean var2, WseeRuntimeMBeanDelegate var3) throws ManagementException {
      super(var1, var2, var3, false);
   }

   public WseeClusterRoutingRuntimeMBeanImpl(String var1, RuntimeMBean var2) throws ManagementException {
      super(var1, var2, (WseeRuntimeMBeanDelegate)null, false);
      WseeClusterRoutingRuntimeData var3;
      if (var2 instanceof WseeRuntimeMBeanDelegate) {
         var3 = new WseeClusterRoutingRuntimeData(var1, ((WseeRuntimeMBeanDelegate)var2).getData());
      } else {
         var3 = new WseeClusterRoutingRuntimeData(var1, (WseeBaseRuntimeData)null);
      }

      this.setData(var3);
   }

   protected WseeClusterRoutingRuntimeMBeanImpl internalCreateProxy(String var1, RuntimeMBean var2) throws ManagementException {
      WseeClusterRoutingRuntimeMBeanImpl var3 = new WseeClusterRoutingRuntimeMBeanImpl(var1, var2, this);
      return var3;
   }

   public int getRequestCount() {
      return ((WseeClusterRoutingRuntimeData)this.getData()).getRequestCount();
   }

   public int getRoutedRequestCount() {
      return ((WseeClusterRoutingRuntimeData)this.getData()).getRoutedRequestCount();
   }

   public int getResponseCount() {
      return ((WseeClusterRoutingRuntimeData)this.getData()).getResponseCount();
   }

   public int getRoutedResponseCount() {
      return ((WseeClusterRoutingRuntimeData)this.getData()).getRoutedResponseCount();
   }

   public int getRoutingFailureCount() {
      return ((WseeClusterRoutingRuntimeData)this.getData()).getRoutingFailureCount();
   }

   public String getLastRoutingFailure() {
      return ((WseeClusterRoutingRuntimeData)this.getData()).getLastRoutingFailure();
   }

   public long getLastRoutingFailureTime() {
      return ((WseeClusterRoutingRuntimeData)this.getData()).getLastRoutingFailureTime();
   }

   public void incrementRequestCount() {
      ((WseeClusterRoutingRuntimeData)this.getData()).incrementRequestCount();
   }

   public void incrementRoutedRequestCount() {
      ((WseeClusterRoutingRuntimeData)this.getData()).incrementRoutedRequestCount();
   }

   public void incrementResponseCount() {
      ((WseeClusterRoutingRuntimeData)this.getData()).incrementResponseCount();
   }

   public void incrementRoutedResponseCount() {
      ((WseeClusterRoutingRuntimeData)this.getData()).incrementRoutedResponseCount();
   }

   public void incrementRoutingFailureCount() {
      ((WseeClusterRoutingRuntimeData)this.getData()).incrementRoutingFailureCount();
   }

   public void setLastRoutingFailure(String var1) {
      ((WseeClusterRoutingRuntimeData)this.getData()).setLastRoutingFailure(var1);
   }

   public void setLastRoutingFailureTime(long var1) {
      ((WseeClusterRoutingRuntimeData)this.getData()).setLastRoutingFailureTime(var1);
   }
}
