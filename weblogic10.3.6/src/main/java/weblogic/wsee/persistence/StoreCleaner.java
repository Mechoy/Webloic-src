package weblogic.wsee.persistence;

import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;
import weblogic.work.WorkManagerFactory;
import weblogic.wsee.WseePersistLogger;

public class StoreCleaner {
   public static final Logger LOGGER = Logger.getLogger(StoreCleaner.class.getName());
   private static final String STATE_CLEAN_INTERVAL_PROP = "weblogic.wsee.StateCleanInterval";
   private static final String STATE_CLEAN_DISABLE_PROP = "weblogic.wsee.DisableStateCleaner";
   private static final String STATE_TIMER_MANAGER_NAME = "WseeStoreCleanerTimerManager";
   private long cleanerIntervalMillis;
   private long maxObjectLifetimeMillis;
   private long maxIdleTimeMillis;
   private boolean suspended = false;
   private boolean started = false;
   private boolean disabled = false;
   private TimerManager tm;
   private Timer timer;
   private final StoreConnection stg;
   private boolean cleaning = false;

   StoreCleaner(StoreConnection var1, long var2, long var4, long var6) {
      this.stg = var1;
      if (var2 <= 0L) {
         var2 = getDefaultCleanerIntervalMillis();
      }

      this.cleanerIntervalMillis = var2;
      this.maxObjectLifetimeMillis = var4;
      this.maxIdleTimeMillis = var6;
      this.disabled = Boolean.getBoolean("weblogic.wsee.DisableStateCleaner");
      if (LOGGER.isLoggable(Level.INFO)) {
         LOGGER.info("StoreCLeaner created for " + var1 + " with interval=" + var2 + " msecs, maxObjectLifetime=" + var4 + " msecs maxIdleTimeMillis=" + var6 + " msecs with disabled = " + this.disabled);
      }

      this.tm = TimerManagerFactory.getTimerManagerFactory().getTimerManager("WseeStoreCleanerTimerManager", WorkManagerFactory.getInstance().getSystem());
   }

   public static long getDefaultCleanerIntervalMillis() {
      long var0 = 60000L;
      String var2 = System.getProperty("weblogic.wsee.StateCleanInterval", "60000");
      if (var2 != null) {
         var2 = var2.trim();
         if (var2.length() != 0) {
            try {
               int var3 = Integer.parseInt(var2);
               if (var3 < 30000) {
                  var3 = 30000;
               }

               var0 = (long)var3;
            } catch (NumberFormatException var4) {
               var4.printStackTrace();
            }
         }
      }

      return var0;
   }

   public long getCleanerIntervalMillis() {
      return this.cleanerIntervalMillis;
   }

   public long getMaxObjectLifetimeMillis() {
      return this.maxObjectLifetimeMillis;
   }

   public void setMaxObjectLifetimeMillis(long var1) {
      this.maxObjectLifetimeMillis = var1;
   }

   public long getMaxIdleTimeMillis() {
      return this.maxIdleTimeMillis;
   }

   public void setMaxIdleTimeMillis(long var1) {
      this.maxIdleTimeMillis = var1;
   }

   void startCleanup() {
      if (!this.disabled) {
         if (!this.started) {
            this.timer = this.tm.schedule(new TimerListenerImpl(), 0L, this.cleanerIntervalMillis);
            if (LOGGER.isLoggable(Level.INFO)) {
               LOGGER.info("StoreCLeaner starting for " + this.stg + " with interval=" + this.cleanerIntervalMillis + " msecs, maxObjectLifetime=" + this.maxObjectLifetimeMillis + " msecs maxIdleTimeMillis=" + this.maxIdleTimeMillis + " msecs with disabled = " + this.disabled);
            }

            this.started = true;
         }

      }
   }

   public void setCleanerIntervalMillis(long var1) {
      if (var1 > 0L && var1 != this.cleanerIntervalMillis) {
         this.cleanerIntervalMillis = var1;
         if (this.timer != null) {
            this.timer.cancel();
         }

         this.timer = this.tm.schedule(new TimerListenerImpl(), 0L, this.cleanerIntervalMillis);
      }

   }

   void stopCleanup() {
      if (!this.disabled) {
         if (this.started) {
            this.tm.stop();
            if (LOGGER.isLoggable(Level.INFO)) {
               LOGGER.info("StoreCLeaner stopping for " + this.stg);
            }

            this.started = false;
         }

      }
   }

   void suspendCleanup() {
      if (!this.disabled) {
         if (this.started && !this.suspended) {
            this.tm.suspend();
            if (LOGGER.isLoggable(Level.INFO)) {
               LOGGER.info("StoreCLeaner for " + this.stg + " suspended");
            }

            this.suspended = true;
         }

      }
   }

   void resumeCleanup() {
      if (!this.disabled) {
         if (this.started && this.suspended) {
            this.tm.resume();
            if (LOGGER.isLoggable(Level.INFO)) {
               LOGGER.info("StoreCLeaner for " + this.stg + " resumed");
            }

            this.suspended = false;
         }

      }
   }

   boolean isDisabled() {
      return this.disabled;
   }

   void setDisabled(boolean var1) {
      if (!this.disabled && var1) {
         this.stopCleanup();
      }

      this.disabled = var1;
   }

   private class TimerListenerImpl implements TimerListener {
      private TimerListenerImpl() {
      }

      public void timerExpired(Timer var1) {
         if (StoreCleaner.LOGGER.isLoggable(Level.FINE)) {
            StoreCleaner.LOGGER.fine("StoreCleaner for " + StoreCleaner.this.stg + " timer popped");
         }

         synchronized(this) {
            if (StoreCleaner.this.cleaning) {
               if (StoreCleaner.LOGGER.isLoggable(Level.FINE)) {
                  StoreCleaner.LOGGER.fine("StoreCleaner for " + StoreCleaner.this.stg + " is already cleaning, bypassing this round of cleaning");
               }

               return;
            }

            StoreCleaner.this.cleaning = true;
         }

         boolean var15 = false;

         label138: {
            try {
               var15 = true;
               this.doClean();
               var15 = false;
               break label138;
            } catch (Throwable var19) {
               WseePersistLogger.logUnexpectedException(var19.toString(), var19);
               var15 = false;
            } finally {
               if (var15) {
                  synchronized(this) {
                     StoreCleaner.this.cleaning = false;
                  }
               }
            }

            synchronized(this) {
               StoreCleaner.this.cleaning = false;
               return;
            }
         }

         synchronized(this) {
            StoreCleaner.this.cleaning = false;
         }

      }

      private void doClean() throws StoreException {
         if (StoreCleaner.LOGGER.isLoggable(Level.FINE)) {
            StoreCleaner.LOGGER.fine("Now checking persistent state objects");
         }

         Set var1 = StoreCleaner.this.stg.keySet();
         if (var1 != null && !var1.isEmpty()) {
            if (StoreCleaner.LOGGER.isLoggable(Level.FINE)) {
               StoreCleaner.LOGGER.fine("StoreCleaner(" + StoreCleaner.this.stg + ") - persistent store has " + var1.size() + " invocation objects to check..");
            }

            Iterator var2 = var1.iterator();

            while(true) {
               while(var2.hasNext()) {
                  Object var3 = var2.next();
                  Storable var4 = StoreCleaner.this.stg.get(var3);
                  if (!Storable.class.isInstance(var4)) {
                     if (StoreCleaner.LOGGER.isLoggable(Level.FINE)) {
                        StoreCleaner.LOGGER.fine("Value " + var4 + "stored in persistent store is not a Storable instance, but instead is a " + (var4 == null ? "null" : var4.getClass().toString()));
                     }
                  } else {
                     Storable var5 = (Storable)Storable.class.cast(var4);
                     boolean var6 = false;
                     boolean var7 = false;
                     if (var5 != null) {
                        if (var5.hasExplicitExpiration() && var5.isExpired()) {
                           StoreCleaner.LOGGER.fine("Storable explicitly indicated it is expired: " + var4);
                           var6 = true;
                        } else if (!var5.hasExplicitExpiration()) {
                           if (var5.getCreationTime() > 0L && StoreCleaner.this.maxObjectLifetimeMillis > 0L && System.currentTimeMillis() - var5.getCreationTime() > StoreCleaner.this.maxObjectLifetimeMillis) {
                              StoreCleaner.LOGGER.fine("Storable max lifetime exceeded: " + var4);
                              var6 = true;
                           }

                           if (var5.getLastUpdatedTime() > 0L && StoreCleaner.this.maxIdleTimeMillis > 0L && System.currentTimeMillis() - var5.getLastUpdatedTime() > StoreCleaner.this.maxIdleTimeMillis) {
                              StoreCleaner.LOGGER.fine("Storable max idle time exceeded: " + var4);
                              var7 = true;
                           }
                        }
                     }

                     if (var6 || var7) {
                        if (StoreCleaner.LOGGER.isLoggable(Level.FINE)) {
                           StoreCleaner.LOGGER.fine("Removing expired " + (var7 ? " (idle timeout)" : "") + " Storable " + var5);
                        }

                        try {
                           var2.remove();
                           StoreCleaner.this.stg.remove(var3);
                        } catch (Exception var9) {
                           if (StoreCleaner.LOGGER.isLoggable(Level.FINE)) {
                              StoreCleaner.LOGGER.log(Level.FINE, var9.toString(), var9);
                           } else {
                              var9.printStackTrace();
                           }
                        }

                        if (StoreCleaner.LOGGER.isLoggable(Level.FINE)) {
                           StoreCleaner.LOGGER.fine("After StoreCleaner removal, current state object count: " + var1.size());
                        }
                     }
                  }
               }

               return;
            }
         } else if (StoreCleaner.LOGGER.isLoggable(Level.FINE)) {
            StoreCleaner.LOGGER.fine("StoreCleaner(" + StoreCleaner.this.stg + ") - persistent store empty.");
         }

      }

      // $FF: synthetic method
      TimerListenerImpl(Object var2) {
         this();
      }
   }
}
