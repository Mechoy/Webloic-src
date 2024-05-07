package weblogic.work;

import weblogic.management.ManagementException;
import weblogic.management.runtime.RequestClassRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;

public final class RequestClassRuntimeMBeanImpl extends RuntimeMBeanDelegate implements RequestClassRuntimeMBean {
   private final ServiceClassSupport rc;

   RequestClassRuntimeMBeanImpl(RequestClass var1) throws ManagementException {
      super(var1.getName());
      this.rc = (ServiceClassSupport)var1;
   }

   RequestClassRuntimeMBeanImpl(RequestClass var1, RuntimeMBean var2) throws ManagementException {
      this(var1, var2, true);
   }

   RequestClassRuntimeMBeanImpl(RequestClass var1, RuntimeMBean var2, boolean var3) throws ManagementException {
      super(var1.getName(), var2, var3);
      this.rc = (ServiceClassSupport)var1;
   }

   public String getRequestClassType() {
      if (this.rc instanceof FairShareRequestClass) {
         return "fairshare";
      } else {
         return this.rc instanceof ResponseTimeRequestClass ? "responsetime" : "context";
      }
   }

   public long getCompletedCount() {
      return (long)this.rc.getCompleted();
   }

   public long getTotalThreadUse() {
      return this.rc.getThreadUse();
   }

   public long getThreadUseSquares() {
      return this.rc.getThreadUseSquares();
   }

   public long getDeltaFirst() {
      return this.rc.getDeltaFirst();
   }

   public long getDeltaRepeat() {
      return this.rc.getDelta();
   }

   public long getMyLast() {
      return this.rc.getMyLast();
   }

   public int getPendingRequestCount() {
      int var1 = this.rc.getPendingRequestsCount();
      return var1 >= 0 ? var1 : 0;
   }

   public long getVirtualTimeIncrement() {
      return this.rc.getVirtualTimeIncrement();
   }

   public double getInterval() {
      return !(this.rc instanceof ResponseTimeRequestClass) ? -1.0 : ((ResponseTimeRequestClass)this.rc).getInterval();
   }
}
