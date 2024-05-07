package weblogic.wsee.server;

import java.util.HashSet;
import java.util.Iterator;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;
import weblogic.work.WorkManagerFactory;
import weblogic.wsee.util.Verbose;

public class StoreCleaner {
   private int WSEE_STATE_CLEAN_INTERVAL = 120000;
   private static final String STATE_CLEAN_INTERVAL_PROP = "weblogic.wsee.StateCleanInterval";
   private static final String STATE_CLEAN_DISABLE_PROP = "weblogic.wsee.DisableStateCleaner";
   private static final String STATE_TIMER_MANAGER_NAME = "WseeStoreCleanerTimerManager";
   private boolean suspended = false;
   private boolean started = false;
   private boolean disabled = false;
   private static final boolean verbose = Verbose.isVerbose(StoreCleaner.class);
   private TimerManager tm;
   private Timer timer;
   private WsStorage stg;
   private boolean cleaning = false;

   StoreCleaner(WsStorage var1) {
      this.stg = var1;
      String var2 = System.getProperty("weblogic.wsee.StateCleanInterval");
      if (var2 != null) {
         var2 = var2.trim();
         if (var2.length() != 0) {
            try {
               int var3 = Integer.parseInt(var2);
               if (var3 < 30000) {
                  var3 = 30000;
               }

               this.WSEE_STATE_CLEAN_INTERVAL = var3;
            } catch (NumberFormatException var4) {
            }
         }
      }

      this.disabled = Boolean.getBoolean("weblogic.wsee.DisableStateCleaner");
      if (verbose) {
         Verbose.say("StoreCLeaner created for " + var1 + " with interval " + this.WSEE_STATE_CLEAN_INTERVAL + " msecs  with disabled = " + this.disabled);
      }

      this.tm = TimerManagerFactory.getTimerManagerFactory().getTimerManager("WseeStoreCleanerTimerManager", WorkManagerFactory.getInstance().getSystem());
   }

   void startCleanup() {
      if (!this.disabled) {
         if (!this.started) {
            this.timer = this.tm.schedule(new TimerListenerImpl(), 0L, (long)this.WSEE_STATE_CLEAN_INTERVAL);
            if (verbose) {
               Verbose.say("StoreCLeaner for " + this.stg + " started up");
            }

            this.started = true;
         }

      }
   }

   int getInterval() {
      return this.WSEE_STATE_CLEAN_INTERVAL;
   }

   void setInterval(int var1) {
      if (var1 > 0 && var1 != this.WSEE_STATE_CLEAN_INTERVAL) {
         this.WSEE_STATE_CLEAN_INTERVAL = var1;
         if (this.timer != null) {
            this.timer.cancel();
         }

         this.timer = this.tm.schedule(new TimerListenerImpl(), 0L, (long)this.WSEE_STATE_CLEAN_INTERVAL);
      }

   }

   void stopCleanup() {
      if (!this.disabled) {
         if (this.started) {
            this.tm.stop();
            if (verbose) {
               Verbose.say("StoreCLeaner for " + this.stg + " stopped");
            }

            this.started = false;
         }

      }
   }

   void suspendCleanup() {
      if (!this.disabled) {
         if (this.started && !this.suspended) {
            this.tm.suspend();
            if (verbose) {
               Verbose.say("StoreCLeaner for " + this.stg + " suspended");
            }

            this.suspended = true;
         }

      }
   }

   void resumeCleanup() {
      if (!this.disabled) {
         if (this.started && this.suspended) {
            this.tm.resume();
            if (verbose) {
               Verbose.say("StoreCLeaner for " + this.stg + " resumed");
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
         if (StoreCleaner.verbose) {
            Verbose.say("StoreCleaner for " + StoreCleaner.this.stg + " timer popped");
         }

         synchronized(this) {
            if (StoreCleaner.this.cleaning) {
               if (StoreCleaner.verbose) {
                  Verbose.say("StoreCleaner for " + StoreCleaner.this.stg + " is already cleaning, bypassing this round of cleaning");
               }

               return;
            }

            StoreCleaner.this.cleaning = true;
         }

         boolean var12 = false;

         try {
            var12 = true;
            this.doClean();
            var12 = false;
         } finally {
            if (var12) {
               synchronized(this) {
                  StoreCleaner.this.cleaning = false;
               }
            }
         }

         synchronized(this) {
            StoreCleaner.this.cleaning = false;
         }
      }

      private void doClean() {
         if (StoreCleaner.verbose) {
            Verbose.log((Object)"Now checking persistent state objects");
         }

         HashSet var1;
         synchronized(StoreCleaner.this.stg) {
            var1 = new HashSet(StoreCleaner.this.stg.memKeys());
         }

         if (var1 != null && !var1.isEmpty()) {
            if (StoreCleaner.verbose) {
               Verbose.say("StoreCleaner(" + StoreCleaner.this.stg + ") - persistent store has " + var1.size() + " invocation objects to check..");
            }

            Iterator var2 = var1.iterator();

            while(true) {
               while(var2.hasNext()) {
                  Object var3 = var2.next();
                  Object var4 = StoreCleaner.this.stg.get(var3);
                  if (!(var4 instanceof StateExpiration)) {
                     if (StoreCleaner.verbose) {
                        Verbose.say("Value " + var4 + "stored in persistent store not an invocation state, but " + (var4 == null ? "null" : var4.getClass().toString()));
                     }
                  } else {
                     StateExpiration var5 = (StateExpiration)var4;
                     if (var5 != null && var5.isExpired()) {
                        if (StoreCleaner.verbose) {
                           Verbose.log((Object)("Removing expired state " + var5));
                        }

                        try {
                           StoreCleaner.this.stg.persistentRemove(var3);
                           var2.remove();
                        } catch (Exception var8) {
                           if (StoreCleaner.verbose) {
                              Verbose.logException(var8);
                           } else {
                              var8.printStackTrace();
                           }
                        }

                        if (StoreCleaner.verbose) {
                           Verbose.log((Object)("After StoreCleaner removal, current state object count: " + var1.size()));
                        }
                     }
                  }
               }

               return;
            }
         } else if (StoreCleaner.verbose) {
            Verbose.say("StoreCleaner(" + StoreCleaner.this.stg + ") - persistent store empty.");
         }

      }

      // $FF: synthetic method
      TimerListenerImpl(Object var2) {
         this();
      }
   }
}
