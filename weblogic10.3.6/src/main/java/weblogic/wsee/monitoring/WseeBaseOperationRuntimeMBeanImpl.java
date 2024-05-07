package weblogic.wsee.monitoring;

import java.util.Date;
import weblogic.management.ManagementException;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.WseeBaseOperationRuntimeMBean;

abstract class WseeBaseOperationRuntimeMBeanImpl<M extends WseeBaseOperationRuntimeMBean, D extends WseeBaseOperationRuntimeData> extends WseeAggregatableBaseOperationRuntimeMBeanImpl<M, D> implements OperationStats, WseeBaseOperationRuntimeMBean {
   public static final String WS_PROTOCOL_OP_NAME = "Ws-Protocol";

   public WseeBaseOperationRuntimeMBeanImpl() throws ManagementException {
      throw new AssertionError("Public constructor provided only for JMX compliance.");
   }

   WseeBaseOperationRuntimeMBeanImpl(String var1, RuntimeMBean var2, WseeRuntimeMBeanDelegate var3) throws ManagementException {
      super(var1, var2, var3);
   }

   WseeBaseOperationRuntimeMBeanImpl(String var1) throws ManagementException {
      super(var1);
   }

   public String getOperationName() {
      return ((WseeBaseOperationRuntimeData)this.getData()).getOperationName();
   }

   public void reportInvocation(long var1, long var3, long var5) {
      ((WseeBaseOperationRuntimeData)this.getData()).reportInvocation(var1, var3, var5);
   }

   public void reportOnewayInvocation(long var1, long var3) {
      ((WseeBaseOperationRuntimeData)this.getData()).reportOnewayInvocation(var1, var3);
   }

   public void reportDispatch(long var1) {
      ((WseeBaseOperationRuntimeData)this.getData()).reportDispatch(var1);
   }

   public void reportExecution(long var1) {
      ((WseeBaseOperationRuntimeData)this.getData()).reportExecution(var1);
   }

   public void reportResponse(long var1) {
      ((WseeBaseOperationRuntimeData)this.getData()).reportResponse(var1);
   }

   public void reportError(Throwable var1) {
      ((WseeBaseOperationRuntimeData)this.getData()).reportError(var1);
   }

   public void reportResponseError(Throwable var1) {
      ((WseeBaseOperationRuntimeData)this.getData()).reportResponseError(var1);
   }

   public void reset() {
      ((WseeBaseOperationRuntimeData)this.getData()).reset();
   }

   public Date getLastResetTime() {
      return ((WseeBaseOperationRuntimeData)this.getData()).getLastResetTime();
   }
}
