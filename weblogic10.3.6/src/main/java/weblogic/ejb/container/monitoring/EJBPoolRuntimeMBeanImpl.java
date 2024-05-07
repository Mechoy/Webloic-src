package weblogic.ejb.container.monitoring;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import weblogic.ejb.container.pool.Pool;
import weblogic.management.ManagementException;
import weblogic.management.runtime.EJBPoolRuntimeMBean;
import weblogic.management.runtime.EJBRuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;

public final class EJBPoolRuntimeMBeanImpl extends RuntimeMBeanDelegate implements EJBPoolRuntimeMBean {
   private static final long serialVersionUID = 6012017468151169855L;
   private AtomicInteger beansInUseCount = new AtomicInteger(0);
   private AtomicInteger waiterCount = new AtomicInteger(0);
   private AtomicLong destroyedTotalCount = new AtomicLong(0L);
   private AtomicLong timeoutTotalCount = new AtomicLong(0L);
   private Pool pool;

   public EJBPoolRuntimeMBeanImpl(String var1, EJBRuntimeMBean var2) throws ManagementException {
      super(var1, var2, true, "PoolRuntime");
   }

   public void setPool(Pool var1) {
      this.pool = var1;
   }

   public void initializePool() {
      if (this.pool != null) {
         this.pool.reInitializePool();
      }
   }

   public int getIdleBeansCount() {
      return this.pool == null ? 0 : this.pool.getFreeCount();
   }

   public int getPooledBeansCurrentCount() {
      return this.pool == null ? 0 : this.pool.getFreeCount();
   }

   public long getAccessTotalCount() {
      return this.pool == null ? 0L : this.pool.getAccessCount();
   }

   public long getMissTotalCount() {
      return this.pool == null ? 0L : this.pool.getMissCount();
   }

   public int getBeansInUseCount() {
      return this.beansInUseCount.get();
   }

   public int getBeansInUseCurrentCount() {
      return this.beansInUseCount.get();
   }

   public void incrementBeansInUseCount() {
      this.beansInUseCount.incrementAndGet();
   }

   public void decrementBeansInUseCount() {
      this.beansInUseCount.decrementAndGet();
   }

   public long getDestroyedTotalCount() {
      return this.destroyedTotalCount.get();
   }

   public void incrementDestroyedTotalCount() {
      this.destroyedTotalCount.incrementAndGet();
   }

   public long getWaiterTotalCount() {
      return (long)this.waiterCount.get();
   }

   public int getWaiterCurrentCount() {
      return this.waiterCount.get();
   }

   public void incrementWaiterCount() {
      this.waiterCount.incrementAndGet();
   }

   public void decrementWaiterCount() {
      this.waiterCount.decrementAndGet();
   }

   public long getTimeoutTotalCount() {
      return this.timeoutTotalCount.get();
   }

   public void incrementTotalTimeoutCount() {
      this.timeoutTotalCount.incrementAndGet();
   }
}
