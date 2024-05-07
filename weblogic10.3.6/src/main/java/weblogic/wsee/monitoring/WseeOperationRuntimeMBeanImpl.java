package weblogic.wsee.monitoring;

import weblogic.management.ManagementException;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.WseeOperationRuntimeMBean;
import weblogic.wsee.util.Verbose;

public final class WseeOperationRuntimeMBeanImpl extends WseeBaseOperationRuntimeMBeanImpl<WseeOperationRuntimeMBean, WseeOperationRuntimeData> implements OperationStats, WseeOperationRuntimeMBean {
   private static final boolean verbose = Verbose.isVerbose(WseeOperationRuntimeMBeanImpl.class);

   public static WseeOperationRuntimeMBeanImpl createWsProtocolOp() throws ManagementException {
      return new WseeOperationRuntimeMBeanImpl("Ws-Protocol");
   }

   public WseeOperationRuntimeMBeanImpl() throws ManagementException {
      throw new AssertionError("Public constructor provided only for JMX compliance.");
   }

   WseeOperationRuntimeMBeanImpl(String var1, RuntimeMBean var2, WseeRuntimeMBeanDelegate var3) throws ManagementException {
      super(var1, var2, var3);
   }

   WseeOperationRuntimeMBeanImpl(String var1) throws ManagementException {
      super(var1);
      if (verbose) {
         Verbose.log((Object)("WseeOperationRuntimeMbeanImpl[" + var1 + "]"));
      }

      WseeOperationRuntimeData var2 = new WseeOperationRuntimeData(var1);
      this.setData(var2);
   }

   protected WseeOperationRuntimeMBeanImpl internalCreateProxy(String var1, RuntimeMBean var2) throws ManagementException {
      WseeOperationRuntimeMBeanImpl var3 = new WseeOperationRuntimeMBeanImpl(var1, var2, this);
      return var3;
   }

   public String getPolicyAttachmentSupport() {
      return ((WseeOperationRuntimeData)this.getData()).getPolicyAttachmentSupport();
   }

   public String getPolicySubjectName() {
      return ((WseeOperationRuntimeData)this.getData()).getPolicySubjectName();
   }

   public String getPolicySubjectResourcePattern() {
      return ((WseeOperationRuntimeData)this.getData()).getPolicySubjectResourcePattern();
   }

   public String getPolicySubjectType() {
      return ((WseeOperationRuntimeData)this.getData()).getPolicySubjectType();
   }
}
