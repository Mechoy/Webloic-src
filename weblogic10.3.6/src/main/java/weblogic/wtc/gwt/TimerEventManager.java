package weblogic.wtc.gwt;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class TimerEventManager extends Thread {
   private WTCService myWTC;
   private static long tick = 0L;
   static final ReentrantReadWriteLock myLock = new ReentrantReadWriteLock();
   static final Lock r;
   static final Lock w;
   private boolean goon = true;
   private long milli;

   public TimerEventManager(WTCService var1) {
      this.myWTC = var1;
      this.milli = System.currentTimeMillis();
   }

   public void shutdown() {
      this.goon = false;
      synchronized(this) {
         this.notifyAll();
      }
   }

   public void run() {
      while(this.goon) {
         try {
            synchronized(this) {
               this.wait(1000L);
            }
         } catch (InterruptedException var8) {
         } catch (Exception var9) {
            this.goon = false;
         }

         if (this.goon) {
            long var1 = System.currentTimeMillis();
            long var3 = var1 - this.milli;
            if (var3 < 0L) {
               var3 = Long.MAX_VALUE - this.milli + var1;
               if (var3 < 1000L) {
                  continue;
               }
            }

            if (var3 >= 1000L) {
               var3 /= 1000L;
               this.milli = var1;
               w.lock();
               if ((tick += var3) == Long.MAX_VALUE || tick < 0L) {
                  tick = 0L;
               }

               w.unlock();
               this.myWTC.processTSessionKAEvents(tick);
            }
         }
      }

   }

   public static long getClockTick() {
      r.lock();
      long var0 = tick;
      r.unlock();
      return var0;
   }

   static {
      r = myLock.readLock();
      w = myLock.writeLock();
   }
}
