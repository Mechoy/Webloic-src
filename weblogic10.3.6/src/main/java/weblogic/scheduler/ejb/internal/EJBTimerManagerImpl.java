package weblogic.scheduler.ejb.internal;

import java.io.Serializable;
import java.util.Date;
import weblogic.scheduler.NoSuchObjectLocalException;
import weblogic.scheduler.TimerBasisAccess;
import weblogic.scheduler.TimerException;
import weblogic.scheduler.TimerHandle;
import weblogic.scheduler.TimerServiceImpl;
import weblogic.scheduler.ejb.EJBTimerListener;
import weblogic.scheduler.ejb.EJBTimerManager;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManager;

public final class EJBTimerManagerImpl implements EJBTimerManager {
   private final String name;
   private final String annotation;
   private final TimerManager timerManagerDelegate;
   private final String dispatchPolicy;

   public EJBTimerManagerImpl(String var1, String var2) {
      this.name = var1;
      this.annotation = var2;
      this.timerManagerDelegate = TimerServiceImpl.create(var1);
      this.dispatchPolicy = null;
   }

   public EJBTimerManagerImpl(String var1, String var2, String var3) {
      this.name = var1;
      this.annotation = var2;
      this.timerManagerDelegate = TimerServiceImpl.create(var1);
      this.dispatchPolicy = var3;
   }

   public Timer schedule(TimerListener var1, long var2) {
      weblogic.scheduler.Timer var4 = (weblogic.scheduler.Timer)this.timerManagerDelegate.schedule(this.getTimerListener(var1), var2);
      return new TimerWrapper(var4);
   }

   public Timer schedule(TimerListener var1, Date var2) {
      weblogic.scheduler.Timer var3 = (weblogic.scheduler.Timer)this.timerManagerDelegate.schedule(this.getTimerListener(var1), var2);
      return new TimerWrapper(var3);
   }

   public Timer schedule(TimerListener var1, long var2, long var4) {
      weblogic.scheduler.Timer var6 = (weblogic.scheduler.Timer)this.timerManagerDelegate.schedule(this.getTimerListener(var1), var2, var4);
      return new TimerWrapper(var6);
   }

   public Timer schedule(TimerListener var1, Date var2, long var3) {
      weblogic.scheduler.Timer var5 = (weblogic.scheduler.Timer)this.timerManagerDelegate.schedule(this.getTimerListener(var1), var2, var3);
      return new TimerWrapper(var5);
   }

   public Timer scheduleAtFixedRate(TimerListener var1, Date var2, long var3) {
      weblogic.scheduler.Timer var5 = (weblogic.scheduler.Timer)this.timerManagerDelegate.scheduleAtFixedRate(this.getTimerListener(var1), var2, var3);
      return new TimerWrapper(var5);
   }

   public Timer scheduleAtFixedRate(TimerListener var1, long var2, long var4) {
      weblogic.scheduler.Timer var6 = (weblogic.scheduler.Timer)this.timerManagerDelegate.scheduleAtFixedRate(this.getTimerListener(var1), var2, var4);
      return new TimerWrapper(var6);
   }

   public void resume() {
      this.timerManagerDelegate.resume();
   }

   public void suspend() {
      this.timerManagerDelegate.suspend();
   }

   public void stop() {
      try {
         TimerBasisAccess.getTimerBasis().cancelTimers(this.name);
      } catch (TimerException var2) {
      }

   }

   public boolean waitForStop(long var1) throws InterruptedException {
      return this.timerManagerDelegate.waitForStop(var1);
   }

   public boolean isStopping() {
      return this.timerManagerDelegate.isStopping();
   }

   public boolean isStopped() {
      return this.timerManagerDelegate.isStopped();
   }

   public boolean waitForSuspend(long var1) throws InterruptedException {
      return this.timerManagerDelegate.waitForSuspend(var1);
   }

   public boolean isSuspending() {
      return this.timerManagerDelegate.isSuspending();
   }

   public boolean isSuspended() {
      return this.timerManagerDelegate.isSuspended();
   }

   private TimerListener getTimerListener(TimerListener var1) {
      if (var1 instanceof EJBTimerListener) {
         return (TimerListener)(((EJBTimerListener)var1).isTransactional() ? new TransactionalEJBListenerWrapper(this.annotation, (EJBTimerListener)var1, this.dispatchPolicy) : new EJBListenerWrapper(this.annotation, (EJBTimerListener)var1, this.dispatchPolicy));
      } else {
         return var1;
      }
   }

   public Timer[] getTimers() {
      try {
         return this.getWrappers(TimerBasisAccess.getTimerBasis().getTimers(this.name));
      } catch (TimerException var2) {
         return null;
      }
   }

   public Timer[] getTimers(String var1) {
      try {
         return this.getWrappers(TimerBasisAccess.getTimerBasis().getTimers(this.name, var1 + "@@"));
      } catch (TimerException var3) {
         return null;
      }
   }

   private Timer[] getWrappers(weblogic.scheduler.Timer[] var1) {
      if (var1 == null) {
         return null;
      } else {
         TimerWrapper[] var2 = new TimerWrapper[var1.length];

         for(int var3 = 0; var3 < var1.length; ++var3) {
            var2[var3] = new TimerWrapper(var1[var3]);
         }

         return var2;
      }
   }

   private static class TimerHandleWrapper implements TimerHandle {
      private final TimerHandle handle;

      TimerHandleWrapper(TimerHandle var1) {
         this.handle = var1;
      }

      public weblogic.scheduler.Timer getTimer() throws NoSuchObjectLocalException, TimerException {
         return new TimerWrapper(this.handle.getTimer());
      }
   }

   private static class TimerWrapper implements weblogic.scheduler.Timer {
      private final weblogic.scheduler.Timer timer;

      TimerWrapper(weblogic.scheduler.Timer var1) {
         this.timer = var1;
      }

      public long getTimeout() {
         return this.timer.getTimeout();
      }

      public long getPeriod() {
         return this.timer.getPeriod();
      }

      public TimerListener getListener() {
         return ((EJBListenerWrapper)this.timer.getListener()).getEJBTimerListener();
      }

      public boolean cancel() {
         return this.timer.cancel();
      }

      public boolean isStopped() {
         return this.timer.isStopped();
      }

      public boolean isCancelled() {
         return this.timer.isCancelled();
      }

      public Serializable getInfo() throws NoSuchObjectLocalException, TimerException {
         return this.timer.getInfo();
      }

      public TimerHandle getHandle() throws NoSuchObjectLocalException, TimerException {
         return new TimerHandleWrapper(this.timer.getHandle());
      }
   }
}
