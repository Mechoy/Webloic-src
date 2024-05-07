package weblogic.wsee.monitoring;

import weblogic.management.ManagementException;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.WseeAggregatableBaseOperationRuntimeMBean;

public final class WseeBasePortAggregatedBaseOperationsRuntimeMBeanImpl extends WseeAggregatableBaseOperationRuntimeMBeanImpl<WseeAggregatableBaseOperationRuntimeMBean, WseeBasePortAggregatedBaseOperationsRuntimeData> implements WseeAggregatableBaseOperationRuntimeMBean {
   public WseeBasePortAggregatedBaseOperationsRuntimeMBeanImpl() throws ManagementException {
      throw new AssertionError("Public constructor provided only for JMX compliance.");
   }

   public WseeBasePortAggregatedBaseOperationsRuntimeMBeanImpl(String var1, RuntimeMBean var2, WseeRuntimeMBeanDelegate var3) throws ManagementException {
      super(var1, var2, var3);
   }

   public WseeBasePortAggregatedBaseOperationsRuntimeMBeanImpl(String var1, RuntimeMBean var2) throws ManagementException {
      super(var1, var2, (WseeRuntimeMBeanDelegate)null);
      WseeBasePortAggregatedBaseOperationsRuntimeData var3;
      if (var2 instanceof WseeRuntimeMBeanDelegate) {
         var3 = new WseeBasePortAggregatedBaseOperationsRuntimeData(var1, ((WseeRuntimeMBeanDelegate)var2).getData());
      } else {
         var3 = new WseeBasePortAggregatedBaseOperationsRuntimeData(var1, (WseeBaseRuntimeData)null);
      }

      this.setData(var3);
   }

   protected WseeBasePortAggregatedBaseOperationsRuntimeMBeanImpl internalCreateProxy(String var1, RuntimeMBean var2) throws ManagementException {
      WseeBasePortAggregatedBaseOperationsRuntimeMBeanImpl var3 = new WseeBasePortAggregatedBaseOperationsRuntimeMBeanImpl(var1, var2, this);
      return var3;
   }
}
