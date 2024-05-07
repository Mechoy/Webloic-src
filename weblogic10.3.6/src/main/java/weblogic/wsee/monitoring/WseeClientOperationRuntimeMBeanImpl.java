package weblogic.wsee.monitoring;

import weblogic.management.ManagementException;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.WseeClientOperationRuntimeMBean;
import weblogic.wsee.util.Verbose;

public final class WseeClientOperationRuntimeMBeanImpl extends WseeBaseOperationRuntimeMBeanImpl<WseeClientOperationRuntimeMBean, WseeClientOperationRuntimeData> implements OperationStats, WseeClientOperationRuntimeMBean {
   private static final boolean verbose = Verbose.isVerbose(WseeClientOperationRuntimeMBeanImpl.class);

   public static WseeClientOperationRuntimeMBeanImpl createWsProtocolOp() throws ManagementException {
      return new WseeClientOperationRuntimeMBeanImpl("Ws-Protocol");
   }

   public WseeClientOperationRuntimeMBeanImpl() throws ManagementException {
      throw new AssertionError("Public constructor provided only for JMX compliance.");
   }

   WseeClientOperationRuntimeMBeanImpl(String var1, RuntimeMBean var2, WseeRuntimeMBeanDelegate var3) throws ManagementException {
      super(var1, var2, var3);
   }

   WseeClientOperationRuntimeMBeanImpl(String var1) throws ManagementException {
      super(var1);
      if (verbose) {
         Verbose.log((Object)("WseeOperationRuntimeMbeanImpl[" + var1 + "]"));
      }

      WseeClientOperationRuntimeData var2 = new WseeClientOperationRuntimeData(var1);
      this.setData(var2);
   }

   protected WseeClientOperationRuntimeMBeanImpl internalCreateProxy(String var1, RuntimeMBean var2) throws ManagementException {
      WseeClientOperationRuntimeMBeanImpl var3 = new WseeClientOperationRuntimeMBeanImpl(var1, var2, this);
      return var3;
   }
}
