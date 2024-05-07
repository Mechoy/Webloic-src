package weblogic.work;

import weblogic.management.ManagementException;
import weblogic.management.runtime.MaxThreadsConstraintRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;

public final class MaxThreadsConstraintRuntimeMBeanImpl extends RuntimeMBeanDelegate implements MaxThreadsConstraintRuntimeMBean {
   private final MaxThreadsConstraint mtc;

   MaxThreadsConstraintRuntimeMBeanImpl(MaxThreadsConstraint var1) throws ManagementException {
      super(var1.getName());
      this.mtc = var1;
   }

   MaxThreadsConstraintRuntimeMBeanImpl(MaxThreadsConstraint var1, RuntimeMBean var2) throws ManagementException {
      this(var1, var2, true);
   }

   MaxThreadsConstraintRuntimeMBeanImpl(MaxThreadsConstraint var1, RuntimeMBean var2, boolean var3) throws ManagementException {
      super(var1.getName(), var2, var3);
      this.mtc = var1;
   }

   public int getExecutingRequests() {
      return this.mtc.getExecutingCount();
   }

   public int getDeferredRequests() {
      return this.mtc.getQueueSize();
   }
}
