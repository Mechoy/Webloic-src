package weblogic.ejb.container.monitoring;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import weblogic.ejb.container.interfaces.TimerManager;
import weblogic.management.ManagementException;
import weblogic.management.runtime.EJBRuntimeMBean;
import weblogic.management.runtime.EJBTimerRuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;

public final class EJBTimerRuntimeMBeanImpl extends RuntimeMBeanDelegate implements EJBTimerRuntimeMBean {
   private static final long serialVersionUID = 1L;
   private TimerManager timerManager;
   private AtomicLong timeoutCount = new AtomicLong(0L);
   private AtomicLong cancelledTimerCount = new AtomicLong(0L);
   private AtomicInteger activeTimerCount = new AtomicInteger(0);
   private AtomicInteger disabledTimerCount = new AtomicInteger(0);

   public EJBTimerRuntimeMBeanImpl(String var1, EJBRuntimeMBean var2, TimerManager var3) throws ManagementException {
      super(var1, var2, true, "TimerRuntime");
      this.timerManager = var3;
   }

   public long getTimeoutCount() {
      return this.timeoutCount.get();
   }

   public void incrementTimeoutCount() {
      this.timeoutCount.incrementAndGet();
   }

   public long getCancelledTimerCount() {
      return this.cancelledTimerCount.get();
   }

   public void incrementCancelledTimerCount() {
      this.cancelledTimerCount.incrementAndGet();
   }

   public int getActiveTimerCount() {
      return this.activeTimerCount.get();
   }

   public void incrementActiveTimerCount() {
      this.activeTimerCount.incrementAndGet();
   }

   public void decrementActiveTimerCount() {
      this.activeTimerCount.decrementAndGet();
   }

   public int getDisabledTimerCount() {
      return this.disabledTimerCount.get();
   }

   public void incrementDisabledTimerCount() {
      this.disabledTimerCount.incrementAndGet();
   }

   public void resetDisabledTimerCount() {
      this.disabledTimerCount.set(0);
   }

   public void activateDisabledTimers() {
      this.timerManager.enableDisabledTimers();
   }
}
