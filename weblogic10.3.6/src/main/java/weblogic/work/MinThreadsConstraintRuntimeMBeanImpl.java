package weblogic.work;

import weblogic.management.ManagementException;
import weblogic.management.runtime.MinThreadsConstraintRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;

public final class MinThreadsConstraintRuntimeMBeanImpl extends RuntimeMBeanDelegate implements MinThreadsConstraintRuntimeMBean {
   private final MinThreadsConstraint mtc;

   MinThreadsConstraintRuntimeMBeanImpl(MinThreadsConstraint var1) throws ManagementException {
      super(var1.getName());
      this.mtc = var1;
   }

   MinThreadsConstraintRuntimeMBeanImpl(MinThreadsConstraint var1, RuntimeMBean var2) throws ManagementException {
      this(var1, var2, true);
   }

   MinThreadsConstraintRuntimeMBeanImpl(MinThreadsConstraint var1, RuntimeMBean var2, boolean var3) throws ManagementException {
      super(var1.getName(), var2, var3);
      this.mtc = var1;
   }

   public int getExecutingRequests() {
      return this.mtc.getExecutingCount();
   }

   public long getCompletedRequests() {
      return this.mtc.getCompletedCount();
   }

   public int getPendingRequests() {
      return this.mtc.getPendingCount();
   }

   public long getOutOfOrderExecutionCount() {
      return this.mtc.getOutOfOrderExecutionCount();
   }

   public int getMustRunCount() {
      return this.mtc.getMustRunCount();
   }

   public long getMaxWaitTime() {
      return this.mtc.getMaxWaitTime();
   }

   public long getCurrentWaitTime() {
      return this.mtc.getCurrentWaitTime();
   }
}
