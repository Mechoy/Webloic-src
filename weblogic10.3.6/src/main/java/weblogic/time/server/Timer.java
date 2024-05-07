package weblogic.time.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.TimerTask;
import weblogic.time.common.Schedulable;
import weblogic.time.common.TimeTriggerException;
import weblogic.time.common.Triggerable;
import weblogic.utils.AssertionError;

public class Timer {
   private Collection triggers = new ArrayList();

   public Timer() {
   }

   public Timer(boolean var1) {
   }

   public void schedule(TimerTask var1, long var2) {
      if (var2 < 0L) {
         throw new IllegalArgumentException("Negative delay.");
      } else {
         this.sched(var1, System.currentTimeMillis() + var2, 0L);
      }
   }

   public void schedule(TimerTask var1, Date var2) {
      this.sched(var1, var2.getTime(), 0L);
   }

   public void schedule(TimerTask var1, long var2, long var4) {
      if (var2 < 0L) {
         throw new IllegalArgumentException("Negative delay.");
      } else if (var4 <= 0L) {
         throw new IllegalArgumentException("Non-positive period.");
      } else {
         this.sched(var1, System.currentTimeMillis() + var2, -var4);
      }
   }

   public void schedule(TimerTask var1, Date var2, long var3) {
      if (var3 <= 0L) {
         throw new IllegalArgumentException("Non-positive period.");
      } else {
         this.sched(var1, var2.getTime(), -var3);
      }
   }

   public void scheduleAtFixedRate(TimerTask var1, long var2, long var4) {
      if (var2 < 0L) {
         throw new IllegalArgumentException("Negative delay.");
      } else if (var4 <= 0L) {
         throw new IllegalArgumentException("Non-positive period.");
      } else {
         this.sched(var1, System.currentTimeMillis() + var2, var4);
      }
   }

   public void scheduleAtFixedRate(TimerTask var1, Date var2, long var3) {
      if (var3 <= 0L) {
         throw new IllegalArgumentException("Non-positive period.");
      } else {
         this.sched(var1, var2.getTime(), var3);
      }
   }

   private void sched(TimerTask var1, long var2, long var4) {
      if (var2 < 0L) {
         throw new IllegalArgumentException("Illegal execution time.");
      } else if (this.triggers == null) {
         throw new IllegalStateException("Timer already cancelled.");
      } else {
         PeriodicTask var6 = new PeriodicTask(var1, var2, var4);
         ScheduledTrigger var7 = new ScheduledTrigger(var6, var6);
         this.triggers.add(var7);

         try {
            var7.schedule();
         } catch (TimeTriggerException var10) {
            IllegalStateException var9 = new IllegalStateException("scheduling problem: ");
            var9.initCause(var10);
            throw var9;
         }
      }
   }

   public void cancel() {
      if (this.triggers != null) {
         Iterator var1 = this.triggers.iterator();
         this.triggers = null;

         while(var1.hasNext()) {
            ScheduledTrigger var2 = (ScheduledTrigger)var1.next();

            try {
               var2.cancel();
            } catch (TimeTriggerException var4) {
               throw new AssertionError("Problem canceling timer", var4);
            }
         }

      }
   }

   static class PeriodicTask implements Schedulable, Triggerable {
      private final TimerTask task;
      private final long period;
      private long executionTime;

      PeriodicTask(TimerTask var1, long var2, long var4) {
         this.task = var1;
         this.period = var4;
         this.executionTime = var2;
      }

      public final void trigger(Schedulable var1) {
         this.task.run();
      }

      public final long schedule(long var1) {
         long var3 = this.executionTime;
         if (this.period == 0L) {
            this.executionTime = 0L;
         } else if (this.period < 0L) {
            this.executionTime -= this.period;
         } else {
            this.executionTime = var1 + this.period;
         }

         return var3;
      }
   }
}
