package weblogic.cluster.replication;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.security.AccessController;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import weblogic.cluster.ClusterExtensionLogger;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.timers.NakedTimerListener;
import weblogic.timers.Timer;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

public class AsyncQueueManager implements PropertyChangeListener {
   private static final int DEFAULT_MIN_CONCURRENCY = 1;
   private static final int DEFAULT_MAX_CONCURRENCY = 1;
   private long asyncSessionQueueTimeout;
   private int UPDATE_SIZE;
   private BlockingQueue updateSet;
   private WorkManager workManager;
   private AsyncFlush flushManager;
   private int updateIndex = 0;
   private Timer updateTimer;
   private long timeAtLastUpdateFlush = 0L;
   private int sessionFlushInterval;
   private TimerManager sessionUpdateFlushTimerManager;
   private boolean greedy = false;

   public AsyncQueueManager(AsyncFlush var1, boolean var2) {
      AuthenticatedSubject var3 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      ClusterMBean var4 = ManagementService.getRuntimeAccess(var3).getServer().getCluster();
      this.UPDATE_SIZE = var4.getSessionFlushThreshold() == -1 ? 0 : var4.getSessionFlushThreshold();
      if (var2) {
         this.sessionFlushInterval = var4.getGreedySessionFlushInterval() == -1 ? 0 : var4.getGreedySessionFlushInterval() * 1000;
      } else {
         this.sessionFlushInterval = var4.getSessionFlushInterval() == -1 ? 0 : var4.getSessionFlushInterval() * 1000;
      }

      this.asyncSessionQueueTimeout = var4.getAsyncSessionQueueTimeout() == -1 ? 0L : (long)var4.getAsyncSessionQueueTimeout();
      var4.addPropertyChangeListener(this);
      this.init(var1, var2);
   }

   public AsyncQueueManager(AsyncFlush var1, boolean var2, int var3, int var4, int var5) {
      this.sessionFlushInterval = var3 * 1000;
      this.UPDATE_SIZE = var4;
      this.asyncSessionQueueTimeout = (long)var5;
      this.init(var1, var2);
   }

   private void init(AsyncFlush var1, boolean var2) {
      this.workManager = WorkManagerFactory.getInstance().findOrCreate("ASYNC_REP_FLUSH_WM", 1, 1);
      this.updateSet = createUpdateSet(this.UPDATE_SIZE);
      this.flushManager = var1;
      this.greedy = var2;
      this.sessionUpdateFlushTimerManager = TimerManagerFactory.getTimerManagerFactory().getTimerManager("asyncSessionUpdateFlushTimerManager", this.workManager);
      this.updateTimer = this.scheduleSessionUpdateTimer();
   }

   public synchronized void propertyChange(PropertyChangeEvent var1) {
      Integer var2;
      if ("SessionFlushThreshold".equals(var1.getPropertyName())) {
         if (AsyncQueueDebugLogger.isDebugEnabled()) {
            AsyncQueueDebugLogger.debug("SessionFlushThreshold property change has occurred with new value: " + var1.getNewValue() + " Resetting AsyncQueue max size.");
         }

         var2 = (Integer)var1.getNewValue();
         this.UPDATE_SIZE = var2;
      } else if ("SessionFlushInterval".equals(var1.getPropertyName())) {
         if (AsyncQueueDebugLogger.isDebugEnabled()) {
            AsyncQueueDebugLogger.debug("SessionFlushInterval property change has occurred with new value: " + var1.getNewValue() + " Will reset timer as appropriate.");
         }

         if (this.greedy) {
            return;
         }

         var2 = (Integer)var1.getNewValue();
         this.sessionFlushInterval = var2 * 1000;
         this.updateTimer.cancel();
         this.updateTimer = this.scheduleSessionUpdateTimer();
      } else if ("GreedySessionFlushInterval".equals(var1.getPropertyName())) {
         if (AsyncQueueDebugLogger.isDebugEnabled()) {
            AsyncQueueDebugLogger.debug("GreedySessionFlushInterval property change has occurred with new value: " + var1.getNewValue() + " This will only change for Greedy Async Queues.");
         }

         if (!this.greedy) {
            return;
         }

         var2 = (Integer)var1.getNewValue();
         this.sessionFlushInterval = var2 * 1000;
         this.updateTimer.cancel();
         this.updateTimer = this.scheduleSessionUpdateTimer();
      } else if ("AsyncSessionQueueTimeout".equals(var1.getPropertyName())) {
         if (AsyncQueueDebugLogger.isDebugEnabled()) {
            AsyncQueueDebugLogger.debug("AsyncSessionQueueTimeout property change has occurred with new value: " + var1.getNewValue() + " Resetting queue blocking timeout.");
         }

         var2 = (Integer)var1.getNewValue();
         this.asyncSessionQueueTimeout = (long)var2;
      }

   }

   private Timer scheduleSessionUpdateTimer() {
      if (AsyncQueueDebugLogger.isDebugEnabled()) {
         AsyncQueueDebugLogger.debug("Session Flush Interval " + this.sessionFlushInterval + "ms" + " and threshold is " + this.UPDATE_SIZE);
      }

      return this.sessionUpdateFlushTimerManager.schedule(new SessionUpdateFlushTrigger(this, this.sessionFlushInterval), (long)this.sessionFlushInterval, (long)this.sessionFlushInterval);
   }

   private static BlockingQueue createUpdateSet(int var0) {
      assert var0 >= 1;

      return new ArrayBlockingQueue(var0);
   }

   public void addToUpdates(Object var1) {
      try {
         if (!this.updateSet.offer(var1, this.asyncSessionQueueTimeout, TimeUnit.SECONDS)) {
            ClusterExtensionLogger.logAsyncReplicationRequestTimeout(var1.toString());
            return;
         }

         ++this.updateIndex;
         boolean var2 = this.updateIndex == this.UPDATE_SIZE;
         if (var2) {
            if (AsyncQueueDebugLogger.isDebugEnabled()) {
               AsyncQueueDebugLogger.debug("The AsyncQueue has reached its maximum size and will schedule a flush");
            }

            this.workManager.schedule(new FlushWork(this));
         }
      } catch (InterruptedException var3) {
         this.addToUpdates(var1);
      }

   }

   long getTimeAtLastUpdateFlush() {
      return this.timeAtLastUpdateFlush;
   }

   int getQueueSize() {
      return this.updateSet != null ? this.updateSet.size() : 0;
   }

   public Iterator iterator() {
      return this.updateSet.iterator();
   }

   public void remove(Object var1) {
      this.updateSet.remove(var1);
   }

   public void flushOnce() {
      if (!this.updateSet.isEmpty()) {
         synchronized(this) {
            this.updateIndex = 0;
            this.timeAtLastUpdateFlush = System.currentTimeMillis();
            this.flushManager.flushQueue(this.updateSet);
         }
      }
   }

   public void flush() {
      if (AsyncQueueDebugLogger.isDebugEnabled()) {
         AsyncQueueDebugLogger.debug("AsyncQueueManager flushing with queue size: " + this.updateSet.size() + " for flushManager: " + this.flushManager);
      }

      this.flushOnce();
      if (this.greedy && !this.updateSet.isEmpty()) {
         AsyncQueueDebugLogger.debug("greedy flush again");
         this.flush();
      }

   }

   private static final class FlushWork implements Runnable {
      private final AsyncQueueManager manager;

      private FlushWork(AsyncQueueManager var1) {
         this.manager = var1;
      }

      public void run() {
         this.manager.flush();
      }

      // $FF: synthetic method
      FlushWork(AsyncQueueManager var1, Object var2) {
         this(var1);
      }
   }

   private static final class SessionUpdateFlushTrigger implements NakedTimerListener {
      private final AsyncQueueManager manager;
      private final int flushPeriod;

      private SessionUpdateFlushTrigger(AsyncQueueManager var1, int var2) {
         this.manager = var1;
         this.flushPeriod = var2;
      }

      public void timerExpired(Timer var1) {
         if (System.currentTimeMillis() - this.manager.getTimeAtLastUpdateFlush() > (long)this.flushPeriod) {
            this.manager.flush();
         }

      }

      // $FF: synthetic method
      SessionUpdateFlushTrigger(AsyncQueueManager var1, int var2, Object var3) {
         this(var1, var2);
      }
   }
}
