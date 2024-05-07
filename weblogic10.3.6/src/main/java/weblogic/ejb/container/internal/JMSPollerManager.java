package weblogic.ejb.container.internal;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;
import weblogic.utils.Debug;
import weblogic.work.WorkManager;

public class JMSPollerManager {
   private static final String TIMER_MANAGER_NAME = "JMSPoller-";
   private static final DebugLogger debugLogger;
   private final NewJMSMessagePoller[] allPollers;
   private final List<NewJMSMessagePoller> availablePollers = Collections.synchronizedList(new LinkedList());
   private final WorkManager wm;
   private final TimerManager timerMgr;
   private final AtomicInteger token = new AtomicInteger(-1);
   private volatile boolean hasErrors;
   private final Object numErrorsLock = new Object();
   private int numErrors = 0;

   public JMSPollerManager(String var1, JMSConnectionPoller var2, MessageConsumer[] var3, MDListener[] var4, WorkManager var5, boolean var6, Destination var7, boolean var8) {
      this.wm = var5;

      for(int var9 = 0; var9 < var3.length; ++var9) {
         NewJMSMessagePoller var10 = new NewJMSMessagePoller(var9, var1, var2, this, var3[var9], var4[var9], var6, var7, var8);
         this.availablePollers.add(var10);
      }

      this.allPollers = (NewJMSMessagePoller[])this.availablePollers.toArray(new NewJMSMessagePoller[this.availablePollers.size()]);
      this.timerMgr = TimerManagerFactory.getTimerManagerFactory().getTimerManager("JMSPoller-" + var1 + this, var5);
   }

   synchronized void stop() {
      for(int var1 = 0; var1 < this.allPollers.length; ++var1) {
         this.allPollers[var1].stop();
      }

   }

   synchronized void start() {
      int var1;
      for(var1 = 0; var1 < this.allPollers.length; ++var1) {
         this.allPollers[var1].start();
      }

      var1 = this.getTokenHolderId();
      if (var1 != -1) {
         if (debugLogger.isDebugEnabled()) {
            Debug.say("Start found token holder" + this.allPollers[var1] + "- scheduling");
         }

         this.wm.schedule(this.allPollers[var1]);
      } else {
         this.wakeUpPoller((NewJMSMessagePoller)null, this.allPollers[0]);
      }

   }

   void waitForPollersToStop() {
      int var1 = this.allPollers.length;

      do {
         if (debugLogger.isDebugEnabled()) {
            Debug.say("JMSPollerManager.waitForPollersToStop() size " + var1 + " availablePollers.size() " + this.availablePollers.size());
            Debug.assertion(this.availablePollers.size() <= var1, "JMSPollerManager.waitForPollersToStop() availablePollers.size() " + this.availablePollers.size() + " > " + var1 + " leaking pollers");
         }

         try {
            Thread.sleep(1000L);
         } catch (InterruptedException var3) {
         }
      } while(this.availablePollers.size() < var1);

   }

   void cleanupTimerManager() {
      if (debugLogger.isDebugEnabled()) {
         Debug.say("cleanupTimerManager() is called");
      }

      this.timerMgr.stop();
   }

   synchronized void wakeUpPoller(NewJMSMessagePoller var1, NewJMSMessagePoller var2) {
      int var3 = var1 != null ? var1.getId() : -1;
      if (var2 == null) {
         var2 = this.getFromPool((NewJMSMessagePoller)null);
      } else {
         var2 = this.getFromPool(var2);

         assert var2 != null : "The new poller parameter of the wakeUpPoller method should be in the available poller list";
      }

      if (var2 != null) {
         if (debugLogger.isDebugEnabled()) {
            Debug.say("Scheduling new token holder :" + var2);
         }

         this.changeOwner(var3, var2.getId());
         this.wm.schedule(var2);
      } else {
         if (debugLogger.isDebugEnabled()) {
            Debug.say("All pollers busy, releasing token from :" + var1);
         }

         this.releaseToken(var3);
      }

   }

   private NewJMSMessagePoller getFromPool(NewJMSMessagePoller var1) {
      if (this.availablePollers.isEmpty()) {
         return null;
      } else if (var1 == null) {
         return (NewJMSMessagePoller)this.availablePollers.remove(0);
      } else {
         return this.availablePollers.remove(var1) ? var1 : null;
      }
   }

   void returnToPool(NewJMSMessagePoller var1) {
      this.availablePollers.add(var1);
   }

   void scheduleTimer(TimerListener var1, long var2) {
      this.timerMgr.schedule(var1, var2);
   }

   boolean scheduleIfBusy(NewJMSMessagePoller var1) {
      return this.wm.scheduleIfBusy(var1);
   }

   int getBatchSize(int var1) {
      int var2 = var1;
      if (this.hasErrors) {
         synchronized(this.numErrorsLock) {
            if (this.numErrors > 0) {
               --this.numErrors;
               var2 = 1;
               if (this.numErrors == 0) {
                  this.hasErrors = false;
               }
            }
         }
      }

      return var2;
   }

   void notifyError(int var1) {
      this.hasErrors = true;
      synchronized(this.numErrorsLock) {
         this.numErrors += var1;
      }
   }

   final int getTokenHolderId() {
      return this.token.get();
   }

   final boolean holdsToken(int var1) {
      return this.token.get() == var1;
   }

   final boolean acquireToken(int var1) {
      return this.token.compareAndSet(-1, var1);
   }

   final boolean releaseToken(int var1) {
      return this.token.compareAndSet(var1, -1);
   }

   private final boolean changeOwner(int var1, int var2) {
      return this.token.compareAndSet(var1, var2);
   }

   static {
      debugLogger = EJBDebugService.invokeLogger;
   }
}
