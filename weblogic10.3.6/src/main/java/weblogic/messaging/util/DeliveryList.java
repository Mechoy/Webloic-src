package weblogic.messaging.util;

import java.util.ArrayList;
import weblogic.messaging.kernel.ListenRequest;
import weblogic.messaging.kernel.MessageElement;
import weblogic.utils.concurrent.ConcurrentBlockingQueue;
import weblogic.utils.concurrent.ConcurrentFactory;
import weblogic.work.WorkManager;

public abstract class DeliveryList implements Runnable {
   private final Object lockObject = new Object();
   protected final ConcurrentBlockingQueue deliveryQueue = ConcurrentFactory.createConcurrentBlockingQueue();
   private static final int DEFAULT_PUSH_SIZE_LIMIT = getIntProperty("weblogic.messaging.PushSizeLimit", Integer.MAX_VALUE);
   private int pushSizeLimit;
   private int pushImpatiencePoint;
   private int pushDelay;
   private int pushDelayStart;
   private int maxTotalDelay;
   private int throughputEmphasis;
   private long lastTime;
   private int totalMessages;
   private int consecutiveEvents;
   private int consecutiveNegativeEventsRequired;
   private int consecutivePositiveEventsRequired;
   private boolean neverAggregate;
   private boolean running;
   private WorkManager workManager;

   public DeliveryList() {
      this.pushSizeLimit = DEFAULT_PUSH_SIZE_LIMIT;
      this.pushImpatiencePoint = Integer.MAX_VALUE;
      this.pushDelay = 0;
      this.pushDelayStart = 0;
      this.throughputEmphasis = 25;
      this.lastTime = 0L;
      this.totalMessages = 0;
      this.consecutiveEvents = 0;
      this.neverAggregate = false;
   }

   public void setWorkManager(WorkManager var1) {
      this.workManager = var1;
   }

   protected void initDeliveryList(int var1, int var2, int var3, int var4) {
      synchronized(this.lockObject) {
         this.pushDelay = var3;
         this.pushDelayStart = var3;
         this.maxTotalDelay = var4;
         if (var2 <= 12) {
            this.neverAggregate = true;
            this.pushDelay = 0;
            this.pushDelayStart = 0;
            this.pushSizeLimit = 1;
         } else if (var2 <= 37) {
            this.pushDelay = 0;
            this.pushDelayStart = 0;
            this.pushSizeLimit = DEFAULT_PUSH_SIZE_LIMIT;
            this.pushImpatiencePoint = 1;
         } else if (var2 <= 62) {
            this.pushSizeLimit = 10240;
            this.pushImpatiencePoint = var1 / 2 + 1;
            this.consecutiveNegativeEventsRequired = 3;
            this.consecutivePositiveEventsRequired = 5;
            if (var4 == 0) {
               this.maxTotalDelay = 10 * this.pushDelay;
            }
         } else if (var2 <= 87) {
            this.consecutiveNegativeEventsRequired = 5;
            this.consecutivePositiveEventsRequired = 5;
            this.pushSizeLimit = 20480;
            this.pushImpatiencePoint = var1 * 3 / 4 + 1;
            if (var4 == 0) {
               this.maxTotalDelay = 20 * this.pushDelay;
            }
         } else {
            this.consecutiveNegativeEventsRequired = 5;
            this.consecutivePositiveEventsRequired = 2;
            this.pushSizeLimit = 40960;
            this.pushImpatiencePoint = var1;
            if (var4 == 0) {
               this.maxTotalDelay = 100 * this.pushDelay;
            }
         }

      }
   }

   public Runnable deliver(ListenRequest var1, java.util.List var2) {
      return this.deliver(var2);
   }

   public Runnable deliver(ListenRequest var1, MessageElement var2) {
      return this.deliver(var2);
   }

   public Runnable deliver(java.util.List var1) {
      this.deliveryQueue.addAll(var1);
      return this.checkAndStartRunning();
   }

   public final Runnable deliver(MessageElement var1) {
      this.deliveryQueue.add(var1);
      return this.checkAndStartRunning();
   }

   private Runnable checkAndStartRunning() {
      synchronized(this.lockObject) {
         if (this.running) {
            return null;
         } else {
            this.running = true;
            return this;
         }
      }
   }

   protected java.util.List getPendingMessages() {
      ArrayList var1 = new ArrayList();
      long var2 = 0L;
      long var4 = 0L;
      if (this.pushDelayStart != 0) {
         var4 = System.currentTimeMillis();
         if (this.pushDelay != this.pushDelayStart && var4 - this.lastTime < (long)this.pushDelayStart) {
            ++this.consecutiveEvents;
            if (this.consecutiveEvents >= this.consecutivePositiveEventsRequired) {
               this.pushDelay = this.pushDelayStart;
               this.consecutiveEvents = 0;
            }
         } else {
            this.consecutiveEvents = 0;
         }
      }

      boolean var7 = false;
      int var8 = 0;

      do {
         Object var6 = null;

         try {
            var6 = this.deliveryQueue.poll();
            if (!this.neverAggregate && var6 == null && !var7 && this.pushDelay != 0) {
               int var9;
               if (var1.size() >= this.pushImpatiencePoint) {
                  var9 = 0;
               } else {
                  var9 = this.pushDelay;
               }

               var6 = this.deliveryQueue.poll((long)var9);
               if (var6 != null && this.maxTotalDelay != 0) {
                  long var10 = System.currentTimeMillis();
                  if (var10 - var4 >= (long)this.maxTotalDelay) {
                     var7 = true;
                  }
               }
            }
         } catch (InterruptedException var12) {
         }

         if (var6 == null || ((MessageElement)var6).getMessage() == null) {
            break;
         }

         var1.add(var6);
         var8 = (int)((long)var8 + ((MessageElement)var6).getMessage().size());
      } while(var8 < this.pushSizeLimit);

      if (this.pushDelay > 0) {
         if (var1.size() == 1) {
            ++this.consecutiveEvents;
            if (this.consecutiveEvents >= this.consecutiveNegativeEventsRequired) {
               this.pushDelay = 0;
               this.consecutiveEvents = 0;
            }
         } else {
            this.consecutiveEvents = 0;
         }
      }

      if (this.pushDelayStart != 0 && var8 < this.pushSizeLimit) {
         this.lastTime = var4;
      }

      this.totalMessages += var1.size();
      return var1;
   }

   protected java.util.List getAllPendingMessages() {
      ArrayList var1 = new ArrayList();

      while(true) {
         Object var2 = this.deliveryQueue.poll();
         if (var2 == null) {
            return var1;
         }

         var1.add(var2);
      }
   }

   public void run() {
      do {
         java.util.List var1 = this.getPendingMessages();
         if (!var1.isEmpty()) {
            this.pushMessages(var1);
         }

         synchronized(this.lockObject) {
            if (this.deliveryQueue.isEmpty()) {
               this.lockObject.notifyAll();
               this.running = false;
               return;
            }
         }
      } while(!this.workManager.scheduleIfBusy(this));

   }

   protected abstract void pushMessages(java.util.List var1);

   protected void waitUntilIdle() {
      synchronized(this.lockObject) {
         while(this.running) {
            try {
               this.lockObject.wait();
            } catch (InterruptedException var4) {
            }
         }

      }
   }

   private static int getIntProperty(String var0, int var1) {
      String var2 = System.getProperty(var0);
      if (var2 == null) {
         return var1;
      } else {
         int var3 = var1;
         String var4 = "Syntax Error, using default " + var1;

         try {
            var3 = Integer.parseInt(var2);
            var4 = "Changed from default " + var1 + " to " + var3;
         } catch (NumberFormatException var6) {
         }

         System.out.println("DeliveryList: Prop [" + var0 + "]" + var4 + ".");
         return var3;
      }
   }
}
