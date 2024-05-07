package weblogic.ejb.container.cache;

import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.EJBLogger;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;

public class CacheScrubberTimer {
   private TimerListener listener;
   private static final DebugLogger debugLogger;
   private Timer timer;
   private String cacheName;
   private long scrubInterval;

   public CacheScrubberTimer(TimerListener var1, long var2, String var4) {
      this.listener = var1;
      this.cacheName = var4;
      this.scrubInterval = var2;
   }

   public void setScrubInterval(long var1) {
      this.scrubInterval = var1;
   }

   public void resetScrubInterval(long var1) {
      if (this.timer == null) {
         this.startScrubber(var1, var1);
      } else {
         assert this.timer != null;

         while(!this.timer.isCancelled()) {
            try {
               this.timer.cancel();
            } catch (Exception var7) {
               EJBLogger.logErrorStoppingCacheTimer(this.cacheName, var7.getMessage());
            }
         }

         long var3 = this.timer.getTimeout() - System.currentTimeMillis();
         long var5 = var1 - var3;
         if (var5 < 0L) {
            var5 = 0L;
         }

         this.timer = null;
         this.startScrubber(var1, var5);
      }
   }

   public void stopScrubber() {
      if (this.timer != null) {
         if (debugLogger.isDebugEnabled()) {
            this.debug(this.cacheName + "  " + " STOP SCRUBBER \n");
         }

         try {
            this.timer.cancel();
         } catch (Exception var2) {
            EJBLogger.logErrorStoppingCacheTimer(this.cacheName, var2.getMessage());
         }

         this.timer = null;
      }

   }

   public void startScrubber() {
      this.startScrubber(this.scrubInterval, this.scrubInterval);
   }

   private void startScrubber(long var1, long var3) {
      if (var1 > 0L) {
         if (debugLogger.isDebugEnabled()) {
            this.debug(this.cacheName + " startScrubber()  period == " + var1 + " delay == " + var3 + " register timer.");
         }

         if (this.timer == null) {
            try {
               TimerManagerFactory var5 = TimerManagerFactory.getTimerManagerFactory();
               TimerManager var6 = var5.getDefaultTimerManager();
               this.timer = var6.scheduleAtFixedRate(this.listener, var3, var1);
            } catch (Exception var7) {
               EJBLogger.logErrorStartingCacheTimer(this.cacheName, var7.getMessage());
            }

         }
      }
   }

   private void debug(String var1) {
      debugLogger.debug("[" + this.getClass().getName() + "]" + var1);
   }

   static {
      debugLogger = EJBDebugService.cachingLogger;
   }
}
