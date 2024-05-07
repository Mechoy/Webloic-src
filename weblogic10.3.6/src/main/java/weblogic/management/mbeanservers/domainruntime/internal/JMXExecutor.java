package weblogic.management.mbeanservers.domainruntime.internal;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

class JMXExecutor implements Executor {
   private static final int DELAY_PERIOD = 5000;
   private Timer timer = null;
   private final TimerManager timerManager;
   private boolean suspended = false;

   JMXExecutor() {
      WorkManager var1 = WorkManagerFactory.getInstance().find("weblogic.admin.RMI");
      TimerManagerFactory var2 = TimerManagerFactory.getTimerManagerFactory();
      this.timerManager = var2.getTimerManager("JMXExecutor", var1);
      this.timerManager.resume();
      this.suspended = false;
   }

   public void execute(Runnable var1) throws RejectedExecutionException {
      if (!this.suspended) {
         this.timer = this.timerManager.schedule(this.getTimerListener(var1), 5000L);
      }
   }

   public void cancel() {
      this.suspended = true;
      if (this.timer != null) {
         this.timer.cancel();
      }

   }

   private TimerListener getTimerListener(final Runnable var1) {
      return new TimerListener() {
         public void timerExpired(Timer var1x) {
            if (!JMXExecutor.this.suspended) {
               var1.run();
            }
         }
      };
   }
}
