package weblogic.ejb.container.monitoring;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import weblogic.management.ManagementException;
import weblogic.management.runtime.EJBLockingRuntimeMBean;
import weblogic.management.runtime.EJBRuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;

public final class EJBLockingRuntimeMBeanImpl extends RuntimeMBeanDelegate implements EJBLockingRuntimeMBean {
   private static final long serialVersionUID = 8726655835786434637L;
   private AtomicInteger lockEntriesCurrentCount = new AtomicInteger(0);
   private AtomicLong lockManagerAccessCount = new AtomicLong(0L);
   private AtomicLong waiterTotalCount = new AtomicLong(0L);
   private AtomicInteger waiterCurrentCount = new AtomicInteger(0);
   private AtomicLong timeoutTotalCount = new AtomicLong(0L);

   public EJBLockingRuntimeMBeanImpl(String var1, EJBRuntimeMBean var2) throws ManagementException {
      super(var1, var2, true, "LockingRuntime");
   }

   public int getLockEntriesCurrentCount() {
      return this.lockEntriesCurrentCount.get();
   }

   public void incrementLockEntriesCurrentCount() {
      this.lockEntriesCurrentCount.incrementAndGet();
   }

   public void decrementLockEntriesCurrentCount() {
      this.lockEntriesCurrentCount.decrementAndGet();
   }

   public long getLockManagerAccessCount() {
      return this.lockManagerAccessCount.get();
   }

   public void incrementLockManagerAccessCount() {
      this.lockManagerAccessCount.incrementAndGet();
   }

   public long getWaiterTotalCount() {
      return this.waiterTotalCount.get();
   }

   public void incrementWaiterTotalCount() {
      this.waiterTotalCount.incrementAndGet();
   }

   public void decrementWaiterTotalCount() {
   }

   public int getWaiterCurrentCount() {
      return this.waiterCurrentCount.get();
   }

   public void incrementWaiterCurrentCount() {
      this.waiterCurrentCount.incrementAndGet();
   }

   public void decrementWaiterCurrentCount() {
      this.waiterCurrentCount.decrementAndGet();
   }

   public long getTimeoutTotalCount() {
      return this.timeoutTotalCount.get();
   }

   public void incrementTimeoutTotalCount() {
      this.timeoutTotalCount.incrementAndGet();
   }
}
