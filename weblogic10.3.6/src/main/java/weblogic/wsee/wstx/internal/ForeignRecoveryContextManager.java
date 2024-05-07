package weblogic.wsee.wstx.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.transaction.Transaction;
import javax.transaction.xa.Xid;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;
import weblogic.work.WorkAdapter;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;
import weblogic.wsee.wstx.wsat.WSATHelper;
import weblogic.wsee.wstx.wsat.common.CoordinatorIF;
import weblogic.wsee.wstx.wsat.common.WSATVersion;

public class ForeignRecoveryContextManager {
   private static final int REPLAY_TIMER_INTERVAL_MS = new Integer(System.getProperty("wsee.wstx.wsat.indoubt.timeout.interval", "10000"));
   private static final int INDOUBT_TIMEOUT = new Integer(System.getProperty("wsee.wstx.wsat.indoubt.timeout", "90000"));
   private static ForeignRecoveryContextManager singleton = new ForeignRecoveryContextManager();
   final DebugLogger debugWSAT = DebugLogger.getDebugLogger("DebugWSAT");
   private TimerManager timerManager;
   private WorkManager workManager;
   private Map<Xid, RecoveryContextWorker> recoveredContexts = new HashMap();

   private ForeignRecoveryContextManager() {
   }

   public static ForeignRecoveryContextManager getInstance() {
      return singleton;
   }

   public synchronized ForeignRecoveryContext addAndGetForeignRecoveryContextForTidByteArray(Xid var1) {
      RecoveryContextWorker var2 = (RecoveryContextWorker)this.recoveredContexts.get(var1);
      if (var2 != null) {
         return var2.context;
      } else {
         ForeignRecoveryContext var3 = new ForeignRecoveryContext(var1);
         this.add(var3, false);
         return var3;
      }
   }

   void start() {
      this.workManager = WorkManagerFactory.getInstance().getSystem();
      if (this.workManager == null) {
         throw new AssertionError("WorkManager not initialized");
      } else {
         this.timerManager = TimerManagerFactory.getTimerManagerFactory().getDefaultTimerManager();
         if (this.timerManager == null) {
            throw new AssertionError("TimerManager not initialized");
         } else {
            this.timerManager.schedule(new ContextTimerListener(), (long)REPLAY_TIMER_INTERVAL_MS, (long)REPLAY_TIMER_INTERVAL_MS);
         }
      }
   }

   synchronized void add(ForeignRecoveryContext var1) {
      this.add(var1, true);
   }

   synchronized void add(ForeignRecoveryContext var1, boolean var2) {
      this.recoveredContexts.put(var1.getXid(), new RecoveryContextWorker(var1, var2 ? -1 : 0));
   }

   Map<Xid, RecoveryContextWorker> getRecoveredContexts() {
      return this.recoveredContexts;
   }

   synchronized void remove(Xid var1) {
      this.recoveredContexts.remove(var1);
   }

   private class RecoveryContextWorker extends WorkAdapter {
      final DebugLogger debugWSAT = DebugLogger.getDebugLogger("DebugWSAT");
      ForeignRecoveryContext context;
      long lastReplayMillis;
      boolean scheduled;
      private int retryCount = 1;

      RecoveryContextWorker(ForeignRecoveryContext var2, int var3) {
         this.context = var2;
         this.lastReplayMillis = (long)var3;
      }

      synchronized long getLastReplayMillis() {
         return this.lastReplayMillis;
      }

      synchronized void setLastReplayMillis(long var1) {
         this.lastReplayMillis = var1;
      }

      synchronized boolean isScheduled() {
         return this.scheduled;
      }

      synchronized void setScheduled(boolean var1) {
         this.scheduled = var1;
      }

      public void run() {
         boolean var16 = false;

         label179: {
            label169: {
               try {
                  var16 = true;
                  Xid var1 = this.context.getXid();
                  if (var1 == null) {
                     if (this.debugWSAT.isDebugEnabled()) {
                        this.debugWSAT.debug("no Xid mapping for recovered context " + this.context);
                        var16 = false;
                     } else {
                        var16 = false;
                     }
                     break label179;
                  }

                  if (this.debugWSAT.isDebugEnabled()) {
                     this.debugWSAT.debug("about to send Prepared recovery call for " + this.context);
                  }

                  CoordinatorIF var2 = WSATHelper.getInstance(this.context.getVersion()).getCoordinatorPort(this.context.getEndpointReference(), var1);
                  if (this.debugWSAT.isDebugEnabled()) {
                     this.debugWSAT.debug("About to send Prepared recovery call for " + this.context + " with coordinatorPort:" + var2);
                  }

                  Object var3 = WSATVersion.getInstance(this.context.getVersion()).newNotificationBuilder().build();
                  Transaction var4 = this.context.getTransaction();
                  if (var4 != null && var4.getStatus() == 2) {
                     var2.preparedOperation(var3);
                  }

                  if (this.debugWSAT.isDebugEnabled()) {
                     this.debugWSAT.debug("Prepared recovery call for " + this.context + " returned successfully");
                     var16 = false;
                  } else {
                     var16 = false;
                  }
                  break label169;
               } catch (Throwable var21) {
                  this.debugWSAT.debug("Prepared recovery call error for " + this.context, var21);
                  var16 = false;
               } finally {
                  if (var16) {
                     synchronized(this) {
                        this.scheduled = false;
                        this.lastReplayMillis = System.currentTimeMillis();
                     }
                  }
               }

               synchronized(this) {
                  this.scheduled = false;
                  this.lastReplayMillis = System.currentTimeMillis();
                  return;
               }
            }

            synchronized(this) {
               this.scheduled = false;
               this.lastReplayMillis = System.currentTimeMillis();
            }

            return;
         }

         synchronized(this) {
            this.scheduled = false;
            this.lastReplayMillis = System.currentTimeMillis();
         }
      }

      void incrementRetryCount() {
         if (this.retryCount * 2 * ForeignRecoveryContextManager.INDOUBT_TIMEOUT < 715827882) {
            this.retryCount *= 2;
         }

         if (this.debugWSAT.isDebugEnabled()) {
            this.debugWSAT.debug("Next recovery call for " + this.context + " in:" + this.retryCount * ForeignRecoveryContextManager.INDOUBT_TIMEOUT);
         }

      }

      int getRetryCount() {
         return this.retryCount;
      }
   }

   private class ContextTimerListener implements TimerListener {
      private ContextTimerListener() {
      }

      public void timerExpired(Timer var1) {
         ArrayList var2 = new ArrayList();
         synchronized(ForeignRecoveryContextManager.this) {
            Iterator var4 = ForeignRecoveryContextManager.this.recoveredContexts.values().iterator();

            while(true) {
               if (!var4.hasNext()) {
                  break;
               }

               RecoveryContextWorker var5 = (RecoveryContextWorker)var4.next();
               long var6 = var5.getLastReplayMillis();
               if (var6 == -1L) {
                  var2.add(var5);
               } else {
                  try {
                     Transaction var8 = var5.context.getTransaction();
                     if (var8 != null && var8.getStatus() == 2) {
                        if (var6 == 0L) {
                           var5.setLastReplayMillis(System.currentTimeMillis());
                        }

                        var2.add(var5);
                     }
                  } catch (Throwable var10) {
                     ForeignRecoveryContextManager.this.debugWSAT.debug("ForeignRecoveryContextManager$ContextTimerListener.timerExpired error scheduling work for recovery context:" + var5.context + " Exception getting transaction status, transaction may be null:" + var10);
                  }
               }
            }
         }

         if (ForeignRecoveryContextManager.this.debugWSAT.isDebugEnabled() && !var2.isEmpty()) {
            ForeignRecoveryContextManager.this.debugWSAT.debug("ForeignRecoveryContextManager$ContextTimerListener.timerExpired replayList.size():" + var2.size());
         }

         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            RecoveryContextWorker var12 = (RecoveryContextWorker)var3.next();
            boolean var13 = var12.isScheduled();
            if (!var13 && System.currentTimeMillis() - var12.getLastReplayMillis() > (long)(ForeignRecoveryContextManager.INDOUBT_TIMEOUT * var12.getRetryCount())) {
               var12.setScheduled(true);
               var12.incrementRetryCount();
               ForeignRecoveryContextManager.this.workManager.schedule(var12);
            }
         }

      }

      // $FF: synthetic method
      ContextTimerListener(Object var2) {
         this();
      }
   }
}
