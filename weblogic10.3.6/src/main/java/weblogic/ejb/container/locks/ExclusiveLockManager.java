package weblogic.ejb.container.locks;

import javax.ejb.EJBException;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.monitoring.EJBLockingRuntimeMBeanImpl;
import weblogic.ejb20.locks.LockTimedOutException;
import weblogic.logging.Loggable;
import weblogic.management.runtime.EJBLockingRuntimeMBean;

public final class ExclusiveLockManager implements LockManager {
   private static final DebugLogger debugLogger;
   private BeanInfo bi;
   private LockBucket[] buckets;
   private int bucketCount;
   private final EJBLockingRuntimeMBeanImpl mBean;

   private static int getBucketForPk(Object var0, int var1) {
      return Math.abs(var0.hashCode() % var1);
   }

   private int getBucketForPk(Object var1) {
      return getBucketForPk(var1, this.bucketCount);
   }

   public ExclusiveLockManager(EJBLockingRuntimeMBean var1) {
      this.mBean = (EJBLockingRuntimeMBeanImpl)var1;
   }

   public void setup(BeanInfo var1) {
      this.bi = var1;
      int var2 = var1.getCachingDescriptor().getMaxBeansInCache();
      this.setup(var2, this.bi.getEJBName());
   }

   private void setup(int var1, String var2) {
      this.bucketCount = var1 / 10 + 1;
      if (this.bucketCount < 11) {
         this.bucketCount = 11;
      }

      this.buckets = new LockBucket[this.bucketCount];

      for(int var3 = 0; var3 < this.bucketCount; ++var3) {
         this.buckets[var3] = new LockBucket(var2, this.mBean);
      }

   }

   public Object getOwner(Object var1) {
      int var2 = this.getBucketForPk(var1);
      return this.buckets[var2].getOwner(var1);
   }

   public boolean lock(Object var1, Object var2, int var3) throws LockTimedOutException {
      int var4 = this.getBucketForPk(var1);
      return this.buckets[var4].lock(var1, var2, var3);
   }

   public void unlock(Object var1, Object var2) {
      int var3 = this.getBucketForPk(var1);
      this.buckets[var3].unlock(var1, var2);
   }

   private static void debug(String var0) {
      debugLogger.debug("[ExclusiveLockManager] " + var0);
   }

   static {
      debugLogger = EJBDebugService.lockingLogger;
   }

   private static final class LockWaiter {
      private final long waitMS;
      private final Object lockClient;
      private volatile boolean youThaMan;
      private boolean isTimedOut = false;
      public LockWaiter next;

      public LockWaiter(long var1, Object var3) {
         this.waitMS = var1;
         this.lockClient = var3;
         this.youThaMan = false;
         this.next = null;
      }
   }

   private static final class LockEntry implements Cloneable {
      private final Object pk;
      private volatile Object owner;
      private LockWaiter waiters;
      public LockEntry next;

      public LockEntry(Object var1, Object var2, LockEntry var3) {
         this.pk = var1;
         this.owner = var2;
         this.next = var3;
      }

      public void addWaiter(LockWaiter var1) {
         if (this.waiters == null) {
            this.waiters = var1;
         } else {
            LockWaiter var2;
            for(var2 = this.waiters; var2.next != null; var2 = var2.next) {
            }

            var2.next = var1;
         }

      }

      public Object clone() throws CloneNotSupportedException {
         return super.clone();
      }

      public LockWaiter getNextValidWaiter() {
         for(; this.waiters != null; this.waiters = this.waiters.next) {
            if (!this.waiters.isTimedOut) {
               LockWaiter var1 = this.waiters.next;
               LockWaiter var2 = this.waiters;
               this.waiters = var1;
               if (ExclusiveLockManager.debugLogger.isDebugEnabled()) {
                  ExclusiveLockManager.debug("Returning Valid Waiter : " + var2.lockClient);
               }

               return var2;
            }

            if (ExclusiveLockManager.debugLogger.isDebugEnabled()) {
               ExclusiveLockManager.debug("Client : " + this.waiters.lockClient + " timedout ... after " + this.waiters.waitMS);
            }
         }

         return null;
      }
   }

   private static final class LockBucket {
      private LockEntry lockEntries;
      private final String ejbName;
      private final EJBLockingRuntimeMBeanImpl mBean;

      LockBucket(String var1, EJBLockingRuntimeMBeanImpl var2) {
         this.ejbName = var1;
         this.mBean = var2;
         this.lockEntries = null;
      }

      private static boolean eq(Object var0, Object var1) {
         return var0 == var1 || var0.equals(var1);
      }

      private LockEntry findEntryForPK(Object var1) {
         for(LockEntry var2 = this.lockEntries; var2 != null; var2 = var2.next) {
            if (eq(var1, var2.pk)) {
               return var2;
            }
         }

         return null;
      }

      public synchronized Object getOwner(Object var1) {
         LockEntry var2 = this.findEntryForPK(var1);
         return var2 == null ? null : var2.owner;
      }

      public boolean lock(Object var1, Object var2, int var3) throws LockTimedOutException {
         this.mBean.incrementLockManagerAccessCount();
         LockWaiter var5;
         Loggable var7;
         synchronized(this) {
            LockEntry var4 = this.findEntryForPK(var1);

            assert var4 == null || var4.owner != null : "Lock Entry for pk: " + var1 + " with lockClient: " + var2 + " was unowned.";

            if (var4 == null) {
               var4 = new LockEntry(var1, var2, this.lockEntries);
               this.lockEntries = var4;
               this.mBean.incrementLockEntriesCurrentCount();
               if (ExclusiveLockManager.debugLogger.isDebugEnabled()) {
                  ExclusiveLockManager.debug("** LOCK ACQUIRE --> SUCCESSFUL -- ejb-name: " + this.ejbName + " primary key: " + var1 + " lockClient: " + var2 + " wait (MS): " + var3);
               }

               return false;
            }

            if (eq(var2, var4.owner)) {
               if (ExclusiveLockManager.debugLogger.isDebugEnabled()) {
                  ExclusiveLockManager.debug("** LOCK ACQUIRE --> SUCCESSFUL (ALREADY OWNED) -- ejb-name: " + this.ejbName + " primary key: " + var1 + " lockClient: " + var2 + " wait (MS): " + var3);
               }

               return true;
            }

            if (var3 == 0) {
               if (ExclusiveLockManager.debugLogger.isDebugEnabled()) {
                  ExclusiveLockManager.debug("** LOCK ACQUIRE --> FAILED (NO_WAIT) -- ejb-name: " + this.ejbName + " primary key: " + var1 + " lockClient: " + var2 + " wait (MS): " + var3);
               }

               this.mBean.incrementTimeoutTotalCount();
               var7 = EJBLogger.loglockRequestTimeOutLoggable(this.ejbName, var1, var2, 0L);
               throw new LockTimedOutException(var7.getMessage());
            }

            var5 = new LockWaiter((long)var3, var2);
            var4.addWaiter(var5);
            if (ExclusiveLockManager.debugLogger.isDebugEnabled()) {
               ExclusiveLockManager.debug("** LOCK ACQUIRE --> WAITING -- ejb-name: " + this.ejbName + " primary key: " + var1 + " lockClient: " + var2 + " wait (MS): " + var3);
            }
         }

         assert var5 != null;

         synchronized(var5) {
            if (!var5.youThaMan) {
               this.mBean.incrementWaiterTotalCount();
               this.mBean.incrementWaiterCurrentCount();

               try {
                  var5.wait(var5.waitMS);
               } catch (InterruptedException var10) {
               }

               this.mBean.decrementWaiterCurrentCount();
            }

            if (!var5.youThaMan) {
               this.mBean.incrementTimeoutTotalCount();
               if (ExclusiveLockManager.debugLogger.isDebugEnabled()) {
                  ExclusiveLockManager.debug("** LOCK TIME OUT AFTER WAITING -- ejb-name: " + this.ejbName + " primary key: " + var1 + " lockClient: " + var2 + " wait (MS): " + var3);
               }

               var5.isTimedOut = true;
               var7 = EJBLogger.loglockRequestTimeOutLoggable(this.ejbName, var1, var2, (long)var3);
               throw new LockTimedOutException(var7.getMessage());
            } else {
               if (ExclusiveLockManager.debugLogger.isDebugEnabled()) {
                  ExclusiveLockManager.debug("** LOCK ACQUIRE (AFTER WAITING) -- ejb-name: " + this.ejbName + " primary key: " + var1 + " lockClient: " + var2 + " wait (MS): " + var3);
               }

               return false;
            }
         }
      }

      public synchronized void unlock(Object var1, Object var2) {
         LockEntry var3 = null;

         LockEntry var4;
         for(var4 = this.lockEntries; var4 != null && var4.pk != var1 && !var4.pk.equals(var1); var4 = var4.next) {
            var3 = var4;
         }

         if (var4 == null) {
            Loggable var9 = EJBLogger.logunlockCouldNotFindPkLoggable(this.ejbName, var1, var1.getClass().getName());
            throw new EJBException(var9.getMessage());
         } else {
            if (var4.waiters == null) {
               this.removeEntry(var4, var3);
               if (ExclusiveLockManager.debugLogger.isDebugEnabled()) {
                  ExclusiveLockManager.debug("** LOCK UNLOCK -- ejb-name: " + this.ejbName + " primary key: " + var1 + " lockClient: " + var2 + " waiters: NONE");
               }
            } else {
               LockWaiter var5 = var4.getNextValidWaiter();
               if (var5 == null) {
                  this.removeEntry(var4, var3);
                  if (ExclusiveLockManager.debugLogger.isDebugEnabled()) {
                     ExclusiveLockManager.debug("** LOCK UNLOCK -- ejb-name: " + this.ejbName + " primary key: " + var1 + " lockClient: " + var2 + " waiters: NONE");
                  }
               } else {
                  var4.owner = var5.lockClient;
                  synchronized(var5) {
                     var5.youThaMan = true;
                     var5.notify();
                  }

                  if (ExclusiveLockManager.debugLogger.isDebugEnabled()) {
                     ExclusiveLockManager.debug("** LOCK UNLOCK -- ejb-name: " + this.ejbName + " primary key: " + var1 + " lockClient: " + var2 + " waiters: YES" + " new owner: " + var5.lockClient);
                  }
               }
            }

         }
      }

      private void removeEntry(LockEntry var1, LockEntry var2) {
         this.mBean.decrementLockEntriesCurrentCount();
         if (var2 == null) {
            this.lockEntries = var1.next;
         } else {
            var2.next = var1.next;
         }

      }
   }
}
