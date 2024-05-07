package weblogic.wsee.monitoring;

import weblogic.management.ManagementException;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.WseeBaseOperationRuntimeMBean;
import weblogic.management.runtime.WseeClientOperationRuntimeMBean;
import weblogic.management.runtime.WseeClientPortRuntimeMBean;
import weblogic.management.runtime.WseePortConfigurationRuntimeMBean;
import weblogic.wsee.mc.mbean.WseeMcRuntimeMBeanImpl;
import weblogic.wsee.util.Verbose;

public final class WseeClientPortRuntimeMBeanImpl extends WseeBasePortRuntimeMBeanImpl<WseeClientPortRuntimeMBean, WseeClientPortRuntimeData> implements WseeClientPortRuntimeMBean {
   private static final boolean verbose = Verbose.isVerbose(WseeClientPortRuntimeMBeanImpl.class);
   private WseePortConfigurationRuntimeMBean portConfigRuntime;

   public WseeClientPortRuntimeMBeanImpl() throws ManagementException {
      throw new AssertionError("Public constructor provided only for JMX compliance.");
   }

   WseeClientPortRuntimeMBeanImpl(String var1, RuntimeMBean var2, WseeClientPortRuntimeMBeanImpl var3) throws ManagementException {
      super(var1, var2, var3);
   }

   WseeClientPortRuntimeMBeanImpl(String var1, String var2) throws ManagementException {
      super(var1, var2);
      WseeClientPortRuntimeData var3 = new WseeClientPortRuntimeData(var1, var2);
      this.setData(var3);
      this.setMetric(new WseePortPolicyRuntimeMBeanImpl(var1, this));
      this.setClusterRouting(new WseeClusterRoutingRuntimeMBeanImpl(var1, this));
      this.setWsrm(new WseeWsrmRuntimeMBeanImpl(var1, this));
      this.setMc(new WseeMcRuntimeMBeanImpl(var1, this));
      this.setAggregatedBaseOperations(new WseeBasePortAggregatedBaseOperationsRuntimeMBeanImpl(var1, this));
      if (verbose) {
         Verbose.log((Object)("WseeClientPortRuntimeMBeanImpl[" + var1 + "]"));
      }

   }

   protected WseeClientPortRuntimeMBeanImpl internalCreateProxy(String var1, RuntimeMBean var2) throws ManagementException {
      WseeClientPortRuntimeMBeanImpl var3 = new WseeClientPortRuntimeMBeanImpl(var1, var2, this);
      this.internalInitProxy(var3);
      return var3;
   }

   public int getPoolCapacity() {
      return ((WseeClientPortRuntimeData)this.getData()).getPoolCapacity();
   }

   public int getPoolFreeCount() {
      return ((WseeClientPortRuntimeData)this.getData()).getPoolFreeCount();
   }

   public int getPoolTakenCount() {
      return ((WseeClientPortRuntimeData)this.getData()).getPoolTakenCount();
   }

   public int getPoolTotalPooledClientTakeCount() {
      return ((WseeClientPortRuntimeData)this.getData()).getPoolTotalPooledClientTakeCount();
   }

   public int getPoolTotalConversationalClientTakeCount() {
      return ((WseeClientPortRuntimeData)this.getData()).getPoolTotalConversationalClientTakeCount();
   }

   public int getPoolTotalSimpleClientCreateCount() {
      return ((WseeClientPortRuntimeData)this.getData()).getPoolTotalSimpleClientCreateCount();
   }

   public WseeClientOperationRuntimeMBean[] getOperations() {
      WseeBaseOperationRuntimeMBean[] var1 = this.getBaseOperations();
      WseeClientOperationRuntimeMBean[] var2 = new WseeClientOperationRuntimeMBean[var1.length];
      System.arraycopy(var1, 0, var2, 0, var1.length);
      return var2;
   }

   void addOperation(WseeClientOperationRuntimeMBeanImpl var1) {
      super.addOperation(var1);
   }

   public WseePortConfigurationRuntimeMBean getWseePortConfigurationRuntimeMBean() {
      return this.portConfigRuntime;
   }

   public void setWseePortConfigurationRuntimeMBean(WseePortConfigurationRuntimeMBean var1) {
      this.portConfigRuntime = var1;
   }
}
