package weblogic.wsee.monitoring;

import weblogic.management.ManagementException;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.WseeBaseOperationRuntimeMBean;
import weblogic.management.runtime.WseeOperationRuntimeMBean;
import weblogic.management.runtime.WseePortRuntimeMBean;
import weblogic.wsee.mc.mbean.WseeMcRuntimeMBeanImpl;
import weblogic.wsee.util.Verbose;

public final class WseePortRuntimeMBeanImpl extends WseeBasePortRuntimeMBeanImpl<WseePortRuntimeMBean, WseePortRuntimeData> implements WseePortRuntimeMBean {
   private static final boolean verbose = Verbose.isVerbose(WseePortRuntimeMBeanImpl.class);

   public WseePortRuntimeMBeanImpl() throws ManagementException {
      throw new AssertionError("Public constructor provided only for JMX compliance.");
   }

   WseePortRuntimeMBeanImpl(String var1, RuntimeMBean var2, WseePortRuntimeMBeanImpl var3) throws ManagementException {
      super(var1, var2, var3);
   }

   WseePortRuntimeMBeanImpl(String var1, String var2) throws ManagementException {
      super(var1, var2);
      WseePortRuntimeData var3 = new WseePortRuntimeData(var1, var2);
      this.setData(var3);
      this.setMetric(new WseePortPolicyRuntimeMBeanImpl(var1, this));
      this.setClusterRouting(new WseeClusterRoutingRuntimeMBeanImpl(var1, this));
      this.setWsrm(new WseeWsrmRuntimeMBeanImpl(var1, this));
      this.setMc(new WseeMcRuntimeMBeanImpl(var1, this));
      this.setAggregatedBaseOperations(new WseeBasePortAggregatedBaseOperationsRuntimeMBeanImpl(var1, this));
      if (verbose) {
         Verbose.log((Object)("WseePortRuntimeMbeanImpl[" + var1 + "]"));
      }

   }

   protected WseePortRuntimeMBeanImpl internalCreateProxy(String var1, RuntimeMBean var2) throws ManagementException {
      WseePortRuntimeMBeanImpl var3 = new WseePortRuntimeMBeanImpl(var1, var2, this);
      this.internalInitProxy(var3);
      return var3;
   }

   public String getPolicyAttachmentSupport() {
      return ((WseePortRuntimeData)this.getData()).getPolicyAttachmentSupport();
   }

   public String getPolicySubjectName() {
      return ((WseePortRuntimeData)this.getData()).getPolicySubjectName();
   }

   public String getPolicySubjectResourcePattern() {
      return ((WseePortRuntimeData)this.getData()).getPolicySubjectResourcePattern();
   }

   public String getPolicySubjectType() {
      return ((WseePortRuntimeData)this.getData()).getPolicySubjectType();
   }

   public WseeOperationRuntimeMBean[] getOperations() {
      WseeBaseOperationRuntimeMBean[] var1 = this.getBaseOperations();
      WseeOperationRuntimeMBean[] var2 = new WseeOperationRuntimeMBean[var1.length];
      System.arraycopy(var1, 0, var2, 0, var1.length);
      return var2;
   }

   void addOperation(WseeOperationRuntimeMBeanImpl var1) {
      super.addOperation(var1);
   }
}
