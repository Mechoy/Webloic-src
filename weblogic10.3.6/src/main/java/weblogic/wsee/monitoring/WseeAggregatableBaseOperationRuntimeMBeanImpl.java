package weblogic.wsee.monitoring;

import weblogic.management.ManagementException;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.WseeAggregatableBaseOperationRuntimeMBean;

abstract class WseeAggregatableBaseOperationRuntimeMBeanImpl<M extends WseeAggregatableBaseOperationRuntimeMBean, D extends WseeAggregatableBaseOperationRuntimeData> extends WseeRuntimeMBeanDelegate<M, D> implements WseeAggregatableBaseOperationRuntimeMBean {
   public WseeAggregatableBaseOperationRuntimeMBeanImpl() throws ManagementException {
      super((String)null, (RuntimeMBean)null, (WseeRuntimeMBeanDelegate)null, false);
      throw new AssertionError("Public constructor provided only for JMX compliance.");
   }

   WseeAggregatableBaseOperationRuntimeMBeanImpl(String var1, RuntimeMBean var2, WseeRuntimeMBeanDelegate var3) throws ManagementException {
      super(var1, var2, var3, false);
   }

   WseeAggregatableBaseOperationRuntimeMBeanImpl(String var1) throws ManagementException {
      super(var1, (RuntimeMBean)null, (WseeRuntimeMBeanDelegate)null, false);
   }

   public int getInvocationCount() {
      return ((WseeAggregatableBaseOperationRuntimeData)this.getData()).getInvocationCount();
   }

   public long getDispatchTimeTotal() {
      return ((WseeAggregatableBaseOperationRuntimeData)this.getData()).getDispatchTimeTotal();
   }

   public long getDispatchTimeHigh() {
      return ((WseeAggregatableBaseOperationRuntimeData)this.getData()).getDispatchTimeHigh();
   }

   public long getDispatchTimeLow() {
      return ((WseeAggregatableBaseOperationRuntimeData)this.getData()).getDispatchTimeLow();
   }

   public long getDispatchTimeAverage() {
      return ((WseeAggregatableBaseOperationRuntimeData)this.getData()).getDispatchTimeAverage();
   }

   public long getExecutionTimeTotal() {
      return ((WseeAggregatableBaseOperationRuntimeData)this.getData()).getExecutionTimeTotal();
   }

   public long getExecutionTimeHigh() {
      return ((WseeAggregatableBaseOperationRuntimeData)this.getData()).getExecutionTimeHigh();
   }

   public long getExecutionTimeLow() {
      return ((WseeAggregatableBaseOperationRuntimeData)this.getData()).getExecutionTimeLow();
   }

   public long getExecutionTimeAverage() {
      return ((WseeAggregatableBaseOperationRuntimeData)this.getData()).getExecutionTimeAverage();
   }

   public int getResponseCount() {
      return ((WseeAggregatableBaseOperationRuntimeData)this.getData()).getResponseCount();
   }

   public long getResponseTimeTotal() {
      return ((WseeAggregatableBaseOperationRuntimeData)this.getData()).getResponseTimeTotal();
   }

   public long getResponseTimeHigh() {
      return ((WseeAggregatableBaseOperationRuntimeData)this.getData()).getResponseTimeHigh();
   }

   public long getResponseTimeLow() {
      return ((WseeAggregatableBaseOperationRuntimeData)this.getData()).getResponseTimeLow();
   }

   public long getResponseTimeAverage() {
      return ((WseeAggregatableBaseOperationRuntimeData)this.getData()).getResponseTimeAverage();
   }

   public int getResponseErrorCount() {
      return ((WseeAggregatableBaseOperationRuntimeData)this.getData()).getResponseErrorCount();
   }

   public long getLastInvocationTime() {
      return ((WseeAggregatableBaseOperationRuntimeData)this.getData()).getLastInvocationTime();
   }

   public int getErrorCount() {
      return ((WseeAggregatableBaseOperationRuntimeData)this.getData()).getErrorCount();
   }

   public String getLastError() {
      return ((WseeAggregatableBaseOperationRuntimeData)this.getData()).getLastError();
   }

   public long getLastErrorTime() {
      return ((WseeAggregatableBaseOperationRuntimeData)this.getData()).getLastErrorTime();
   }

   public long getLastResponseTime() {
      return ((WseeAggregatableBaseOperationRuntimeData)this.getData()).getLastResponseTime();
   }

   public long getLastResponseErrorTime() {
      return ((WseeAggregatableBaseOperationRuntimeData)this.getData()).getLastResponseErrorTime();
   }

   public String getLastResponseError() {
      return ((WseeAggregatableBaseOperationRuntimeData)this.getData()).getLastResponseError();
   }
}
