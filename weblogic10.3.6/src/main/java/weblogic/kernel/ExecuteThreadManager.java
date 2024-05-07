package weblogic.kernel;

import java.util.ArrayList;
import weblogic.health.HealthState;
import weblogic.management.configuration.ExecuteQueueMBean;
import weblogic.utils.UnsyncCircularQueue;
import weblogic.utils.collections.Stack;

public class ExecuteThreadManager {
   private static boolean netscape = false;
   private static final ShutdownRequest SHUTDOWN_REQUEST = new ShutdownRequest();
   private final String name;
   private final ThreadGroup threadGroup;
   private final UnsyncCircularQueue q;
   private final ArrayList threads;
   private final Stack idleThreads;
   private boolean shutdownRequested = false;
   private int departures = 0;
   private final Object printOnceLock = new Object();
   private boolean capacityGreaterThanThreshold = false;
   private final String[] healthReason = new String[]{""};
   private int healthState = 0;
   private final ExecuteQueueMBean queueMBean;

   protected ExecuteThreadManager(String var1) {
      this.name = var1;
      this.threadGroup = null;
      this.q = null;
      this.threads = null;
      this.idleThreads = null;
      this.queueMBean = null;
   }

   ExecuteThreadManager(String var1, ExecuteQueueMBean var2) {
      this.name = var1;
      this.queueMBean = var2;
      this.q = new UnsyncCircularQueue(256, var2.getQueueLength());
      this.idleThreads = new Stack(var2.getThreadCount());
      this.threads = new ArrayList(var2.getThreadCount());
      ThreadGroup var3 = null;

      try {
         var3 = new ThreadGroup("Thread Group for Queue: '" + var1 + "'");
      } catch (SecurityException var5) {
         System.err.println("Caught a security exception. That's okay.");
         netscape = true;
      }

      this.threadGroup = var3;
      this.setThreadCount(var2.getThreadCount());
   }

   private int getThreadsIncrease() {
      return this.queueMBean != null ? this.queueMBean.getThreadsIncrease() : 0;
   }

   private int getThreadsMaximum() {
      return this.queueMBean != null ? this.queueMBean.getThreadsMaximum() : 0;
   }

   private int getCalculatedPercent() {
      return this.queueMBean == null ? 0 : Math.max(this.queueMBean.getQueueLength() * this.queueMBean.getQueueLengthThresholdPercent() / 100, 1);
   }

   public boolean isShutdownInProgress() {
      return this.shutdownRequested;
   }

   ExecuteThread[] getExecuteThreads() {
      return (ExecuteThread[])((ExecuteThread[])this.threads.toArray(new ExecuteThread[this.threads.size()]));
   }

   public int getExecuteQueueDepth() {
      return this.q.size();
   }

   public int getExecuteQueueSize() {
      return this.q.capacity();
   }

   public int getExecuteQueueDepartures() {
      return this.departures;
   }

   public int getExecuteThreadCount() {
      return this.threads.size();
   }

   public final String getName() {
      return this.name;
   }

   public void setThreadCount(int var1) throws SecurityException {
      int var2;
      synchronized(this) {
         if (this.shutdownRequested) {
            throw new IllegalStateException("Shutdown in progress");
         }

         int var4 = this.getThreadsMaximum();
         if (var1 > var4) {
            var1 = var4;
         }

         if (var1 <= this.threads.size()) {
            return;
         }

         int var5 = this.queueMBean != null ? this.queueMBean.getThreadPriority() : 5;
         var2 = this.threads.size();
         int var6 = var2;

         while(true) {
            if (var6 >= var1) {
               break;
            }

            ExecuteThread var7;
            if (netscape) {
               var7 = createExecuteThread(var6, this);
            } else {
               try {
                  var7 = createExecuteThread(var6, this, this.threadGroup);
                  var7.setDaemon(true);
               } catch (SecurityException var10) {
                  System.err.println("Caught a security exception. That's okay.");
                  netscape = true;
                  var7 = createExecuteThread(var6, this);
               }
            }

            var7.setPriority(var5);
            this.threads.add(var7);
            ++var6;
         }
      }

      this.startThreads(var2);
   }

   private void startThreads(int var1) {
      synchronized(this.threads) {
         for(int var3 = var1; var3 < this.threads.size(); ++var3) {
            ExecuteThread var4 = (ExecuteThread)this.threads.get(var3);
            if (var4 != null) {
               var4.start();
               if (!var4.isStarted()) {
                  try {
                     Thread.sleep(5L);
                  } catch (InterruptedException var7) {
                  }
               }
            }
         }

      }
   }

   private void expandThreadPool() {
      int var1 = this.getThreadsIncrease();
      if (this.threads.size() == 0) {
         this.setThreadCount(var1);
      } else if (this.q.size() + 1 >= this.getCalculatedPercent()) {
         this.capacityGreaterThanThreshold = true;
         this.setHealthState(1, "Queue Capacity greater than configured threshold of " + this.queueMBean.getQueueLengthThresholdPercent() + "%.  Will" + " try to allocate: '" + var1 + "' threads to help.");
         this.setThreadCount(this.threads.size() + var1);
      } else if (this.healthState != 0) {
         this.capacityGreaterThanThreshold = false;
         this.setHealthState(0, "");
      }

   }

   synchronized ExecuteThread[] getStuckExecuteThreads(long var1) {
      long var3 = System.currentTimeMillis();
      ArrayList var5 = null;
      if (this.threads.size() != 0 && var1 != 0L) {
         int var6;
         for(var6 = 0; var6 < this.threads.size(); ++var6) {
            ExecuteThread var7 = (ExecuteThread)this.threads.get(var6);
            if (var7 != null && !var7.getSystemThread() && var7.getCurrentRequest() != null) {
               long var8 = var7.getTimeStamp();
               if (var8 > 0L) {
                  long var10 = var3 - var8;
                  if (var10 > var1) {
                     var7.setPrintStuckThreadMessage(true);
                     if (var5 == null) {
                        var5 = new ArrayList();
                     }

                     var5.add(var7);
                  }
               }
            }
         }

         var6 = this.getThreadsIncrease();
         if (var5 != null && var5.size() == this.threads.size()) {
            this.setHealthState(2, "All Threads are stuck.  Will try to allocate: '" + var6 + "' threads to help.");
            this.setThreadCount(this.threads.size() + var6);
         } else if (this.healthState != 0) {
            this.setHealthState(0, "");
         }

         return (ExecuteThread[])(var5 != null ? var5.toArray(new ExecuteThread[var5.size()]) : null);
      } else {
         return null;
      }
   }

   synchronized void shutdown() throws SecurityException {
      if (!this.shutdownRequested) {
         this.shutdownRequested = true;

         while(this.idleThreads.size() != 0) {
            ExecuteThread var1 = (ExecuteThread)this.idleThreads.pop();
            var1.notifyRequest(SHUTDOWN_REQUEST);
         }

      }
   }

   public int getIdleThreadCount() {
      return this.idleThreads.size();
   }

   void registerIdle(ExecuteThread var1) {
      if (var1.getPrintStuckThreadMessage()) {
         T3SrvrLogger.logInfoUnstuckThread(var1.getName());
         var1.setPrintStuckThreadMessage(false);
      }

      Object var2;
      synchronized(this) {
         if (this.shutdownRequested) {
            var2 = SHUTDOWN_REQUEST;
         } else {
            var2 = (ExecuteRequest)this.q.get();
         }

         if (var2 == null) {
            this.idleThreads.push(var1);
            return;
         }
      }

      var1.setRequest((ExecuteRequest)var2);
      ++this.departures;
   }

   void execute(ExecuteRequest var1, boolean var2) {
      if (var2 && Kernel.isQueueThrottleAllowed()) {
         int var4 = this.queueMBean.getQueueLength();
         if (this.q.size() >= var4) {
            throw new QueueFullException(var4);
         }
      }

      ExecuteThread var3;
      label66: {
         try {
            synchronized(this) {
               if (this.idleThreads.size() != 0) {
                  var3 = (ExecuteThread)this.idleThreads.pop();
                  break label66;
               }

               this.expandThreadPool();
               this.q.put(var1);
            }
         } finally {
            this.logQueueCapacityWarning();
         }

         return;
      }

      ++this.departures;
      var3.notifyRequest(var1);
   }

   private void logQueueCapacityWarning() {
      boolean var1 = false;
      synchronized(this.printOnceLock) {
         if (this.capacityGreaterThanThreshold) {
            var1 = true;
            this.capacityGreaterThanThreshold = false;
         }
      }

      int var2 = this.getThreadsIncrease();
      if (var1 && var2 != 0 && this.threads.size() + var2 <= this.getThreadsMaximum()) {
         T3SrvrLogger.logWarnQueueCapacityGreaterThanThreshold(this.queueMBean.getQueueLengthThresholdPercent(), var2);
      }

   }

   boolean executeIfIdle(ExecuteRequest var1) {
      ExecuteThread var2;
      synchronized(this) {
         if (this.idleThreads.size() == 0) {
            return false;
         }

         var2 = (ExecuteThread)this.idleThreads.pop();
      }

      ++this.departures;
      var2.notifyRequest(var1);
      return true;
   }

   private void setHealthState(int var1, String var2) {
      this.healthState = var1;
      this.healthReason[0] = var2;
   }

   public HealthState getHealthState() {
      return new HealthState(this.healthState, this.healthReason);
   }

   int getPendingTasksCount() {
      int var1 = 0;
      if (this.threads.size() == 0) {
         return 0;
      } else {
         for(int var2 = 0; var2 < this.threads.size(); ++var2) {
            ExecuteThread var3 = (ExecuteThread)this.threads.get(var2);
            if (var3 != null && !var3.getSystemThread()) {
               ExecuteRequest var4 = var3.getCurrentRequest();
               if (var4 != null) {
                  ++var1;
               }
            }
         }

         var1 += this.getExecuteQueueDepth();
         return var1;
      }
   }

   public final synchronized String toString() {
      return super.toString() + " - name: '" + this.getName() + "' threads: '" + this.getExecuteThreadCount() + "' idle: '" + this.getIdleThreadCount() + " departures: '" + this.getExecuteQueueDepartures() + "' queue:\n\t" + this.q;
   }

   private static ExecuteThread createExecuteThread(int var0, ExecuteThreadManager var1) {
      return (ExecuteThread)(KernelStatus.isApplet() ? new ExecuteThread(var0, var1) : new ServerExecuteThread(var0, var1));
   }

   private static ExecuteThread createExecuteThread(int var0, ExecuteThreadManager var1, ThreadGroup var2) {
      return (ExecuteThread)(KernelStatus.isApplet() ? new ExecuteThread(var0, var1, var2) : new ServerExecuteThread(var0, var1, var2));
   }

   static final class ShutdownError extends Error {
   }

   private static final class ShutdownRequest implements ExecuteRequest {
      private ShutdownRequest() {
      }

      public void execute(ExecuteThread var1) {
         throw new ShutdownError();
      }

      // $FF: synthetic method
      ShutdownRequest(Object var1) {
         this();
      }
   }
}
