package weblogic.time.common.internal;

import java.util.Date;
import java.util.Hashtable;
import weblogic.common.T3MiscLogger;
import weblogic.kernel.Kernel;
import weblogic.time.common.ScheduledTriggerDef;
import weblogic.work.WorkManagerFactory;

public final class TimeEventGenerator implements Runnable {
   private static final int HIGH_THREAD_PRIORITY = 9;
   private static final String RUNTIME_MBEAN_NAME = "TimeEventGenerator";
   private static final Date LAUNCH = new Date();
   private static TimeEventGenerator singleton;
   private final Hashtable allTriggers = new Hashtable();
   private final Thread theThread;
   private final TimeTable timeTriggers;
   private long nexttime;
   private long startMillis;
   private int triggerInstanceCount;
   private int triggerExpiredCount;

   public static Date getLaunch() {
      return LAUNCH;
   }

   public static synchronized TimeEventGenerator init(ThreadGroup var0) {
      if (singleton != null) {
         throw new IllegalStateException("Attempt to double initialize");
      } else {
         singleton = new TimeEventGenerator(var0, 9);
         singleton.start();
         return singleton;
      }
   }

   public static synchronized TimeEventGenerator getOne() {
      return getHighPriorityOne();
   }

   public static synchronized TimeEventGenerator getHighPriorityOne() {
      return singleton != null ? singleton : init((ThreadGroup)null);
   }

   public static int deltaMillis(long var0) {
      return var0 == 0L ? Integer.MAX_VALUE : (int)(System.currentTimeMillis() - var0);
   }

   public static int deltaSecs(long var0) {
      return var0 == 0L ? Integer.MAX_VALUE : (int)(getCurrentSecs() - var0);
   }

   public static int deltaMins(long var0) {
      return var0 == 0L ? Integer.MAX_VALUE : (int)(getCurrentMins() - var0);
   }

   public static long getCurrentMillis() {
      return System.currentTimeMillis();
   }

   public static long getCurrentSecs() {
      return (System.currentTimeMillis() + 500L) / 1000L;
   }

   public static long getCurrentMins() {
      return (getCurrentSecs() + 30L) / 60L;
   }

   private TimeEventGenerator(ThreadGroup var1, int var2) {
      this.theThread = new Thread(var1, this, "weblogic.time.TimeEventGenerator");
      this.theThread.setDaemon(true);
      this.theThread.setPriority(var2);
      this.timeTriggers = new TimeTable(100, 1000L, System.currentTimeMillis());
   }

   public void start() {
      this.theThread.start();
   }

   public void stop() {
      this.theThread.stop();
      if (this == singleton) {
         singleton = null;
      }

   }

   public void run() {
      this.startMillis = System.currentTimeMillis();

      while(true) {
         while(true) {
            try {
               this.timeTriggers.execute(System.currentTimeMillis(), WorkManagerFactory.getInstance().getDefault(), true);
               this.timeTriggers.snooze();
            } catch (ThreadDeath var2) {
               if (Kernel.isServer()) {
                  throw var2;
               }
            } catch (Throwable var3) {
               T3MiscLogger.logThrowable(var3);
            }
         }
      }
   }

   public void insert(ScheduledTrigger var1) {
      this.timeTriggers.insert(var1);
   }

   public boolean delete(ScheduledTrigger var1) {
      if (this.timeTriggers.delete(var1)) {
         this.dropTriggerID();
         return true;
      } else {
         return false;
      }
   }

   public void deleted(ScheduledTrigger var1) {
      this.dropTriggerID();
   }

   public void register(Object var1, ScheduledTriggerDef var2) {
      this.allTriggers.put(var1, var2);
   }

   public ScheduledTriggerDef unregister(Object var1) {
      return (ScheduledTriggerDef)this.allTriggers.remove(var1);
   }

   public ScheduledTriggerDef registered(Object var1) {
      return (ScheduledTriggerDef)this.allTriggers.get(var1);
   }

   public final synchronized int nextTriggerID() {
      return this.triggerInstanceCount++;
   }

   public final synchronized void dropTriggerID() {
      ++this.triggerExpiredCount;
   }

   public int getTriggerInstanceCount() {
      return this.triggerInstanceCount;
   }

   public int getTriggerExpiredCount() {
      return this.triggerExpiredCount;
   }

   public final int getAvgExecCount() {
      int var1 = (int)(System.currentTimeMillis() - this.startMillis);
      return this.timeTriggers.executeCount() * 1000 * 60 / var1;
   }

   public int getExecuteCount() {
      return this.timeTriggers.executeCount();
   }

   public int getExceptionCount() {
      return this.timeTriggers.exceptionCount();
   }

   public String getRuntimeName() {
      return "TimeEventGenerator";
   }
}
