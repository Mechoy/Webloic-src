package weblogic.corba.cos.transactions;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.transaction.RollbackException;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import weblogic.iiop.IIOPLogger;
import weblogic.time.common.Schedulable;
import weblogic.time.common.TimeTriggerException;
import weblogic.time.common.Triggerable;
import weblogic.time.common.internal.ScheduledTrigger;
import weblogic.work.WorkManagerFactory;

public final class RecoveryCoordinatorReleaser {
   private static RecoveryCoordinatorReleaser singleton;
   private RecoveryCoordinatorReleaseTrigger trigger;
   private Hashtable listeners = new Hashtable();

   public static RecoveryCoordinatorReleaser getReleaser() {
      return singleton == null ? createSingleton() : singleton;
   }

   private RecoveryCoordinatorReleaser() {
   }

   private static synchronized RecoveryCoordinatorReleaser createSingleton() {
      if (singleton == null) {
         singleton = new RecoveryCoordinatorReleaser();
      }

      return singleton;
   }

   public void register(Transaction var1, RecoveryCoordinatorImpl var2) {
      if (var1 != null) {
         if (this.trigger == null) {
            try {
               this.trigger = new RecoveryCoordinatorReleaseTrigger();
            } catch (TimeTriggerException var7) {
               IIOPLogger.logOTSError("Could not schedule RecoveryCoordinator release trigger", var7);
               return;
            }
         }

         TxListener var3 = (TxListener)this.listeners.get(var1);
         if (var3 == null) {
            var3 = new TxListener(var1);

            try {
               var1.registerSynchronization(var3);
               this.listeners.put(var1, var3);
            } catch (RollbackException var5) {
               IIOPLogger.logOTSError("Could not register synchronization", var5);
            } catch (SystemException var6) {
               IIOPLogger.logOTSError("Could not register synchronization", var6);
            }
         }

         var3.add(var2);
      }
   }

   private static class RecoveryCoordinatorReleaseTrigger implements Schedulable, Triggerable {
      private static final long RC_DEFUALT_RELEASE_SEC = 90000L;
      private static final long DEFAULT_TRIGGER_INTERVAL = 30000L;
      private long releaseSec = 90000L;
      private long triggerInterval = 30000L;
      private List[] array;
      private int currentIndex = 0;

      public RecoveryCoordinatorReleaseTrigger() throws TimeTriggerException {
         int var1 = this.releaseSec % this.triggerInterval == 0L ? 0 : 1;
         int var2 = (int)(this.releaseSec / this.triggerInterval) + var1 + 1;
         this.array = new LinkedList[var2];
         ScheduledTrigger var3 = new ScheduledTrigger(this, this, WorkManagerFactory.getInstance().getSystem());
         var3.schedule();
      }

      public synchronized void add(List var1) {
         List var2 = this.array[this.currentIndex];
         if (var2 == null) {
            var2 = this.array[this.currentIndex] = new LinkedList();
         }

         var2.addAll(var1);
      }

      public long schedule(long var1) {
         return var1 + this.triggerInterval;
      }

      public synchronized void trigger(Schedulable var1) {
         this.currentIndex = (this.currentIndex + 1) % this.array.length;
         List var2 = this.array[this.currentIndex];
         if (var2 != null) {
            Iterator var3 = var2.iterator();

            while(var3.hasNext()) {
               RecoveryCoordinatorImpl var4 = (RecoveryCoordinatorImpl)var3.next();
               var4.release();
            }

            this.array[this.currentIndex] = null;
         }

      }
   }

   private class TxListener implements Synchronization {
      private List recoverys = new LinkedList();
      private Transaction tx;

      TxListener(Transaction var2) {
         this.tx = var2;
      }

      public void add(RecoveryCoordinatorImpl var1) {
         this.recoverys.add(var1);
      }

      public void beforeCompletion() {
      }

      public void afterCompletion(int var1) {
         RecoveryCoordinatorReleaser.this.trigger.add(this.recoverys);
         RecoveryCoordinatorReleaser.this.listeners.remove(this.tx);
      }
   }
}
