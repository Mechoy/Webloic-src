package weblogic.work;

import weblogic.utils.UnsyncCircularQueue;
import weblogic.utils.collections.WeakConcurrentHashMap;

public final class IDBasedConstraintEnforcement {
   private static final IDBasedConstraintEnforcement THE_ONE = new IDBasedConstraintEnforcement();
   private final WeakConcurrentHashMap wrappers = new WeakConcurrentHashMap();

   public static IDBasedConstraintEnforcement getInstance() {
      return THE_ONE;
   }

   public synchronized void schedule(WorkManager var1, Runnable var2, int var3) {
      RunnableWrapper var4 = this.get(var3);
      if (var4 == null) {
         var4 = this.create(var3, var2);
         var4.submitted();
         var1.schedule(var4);
      } else {
         boolean var5 = var4.add(var2);
         if (!var5) {
            var4.submitted();
            var1.schedule(var4);
         }
      }

   }

   public synchronized int getExecutingCount(int var1) {
      RunnableWrapper var2 = this.get(var1);
      return var2 != null ? var2.getExecutingCount() : 0;
   }

   public synchronized int getPendingCount(int var1) {
      RunnableWrapper var2 = this.get(var1);
      return var2 != null ? var2.getPendingCount() : 0;
   }

   private RunnableWrapper create(int var1, Runnable var2) {
      RunnableWrapper var3 = new RunnableWrapper(var1, var2);
      this.wrappers.put(var1, var3);
      return var3;
   }

   private RunnableWrapper get(int var1) {
      return (RunnableWrapper)this.wrappers.get(var1);
   }

   private static final class RunnableWrapper extends WorkAdapter {
      private int id;
      private boolean running;
      private boolean submitted;
      private Runnable initialRunnable;
      private UnsyncCircularQueue queue = null;

      RunnableWrapper(int var1, Runnable var2) {
         this.id = var1;
         this.initialRunnable = var2;
      }

      public void run() {
         synchronized(this) {
            if (this.running) {
               return;
            }

            this.running = true;
         }

         if (this.initialRunnable != null) {
            this.initialRunnable.run();
         }

         Runnable var1 = null;

         while((var1 = this.get()) != null) {
            var1.run();
         }

      }

      private synchronized Runnable get() {
         if (this.queue == null) {
            this.reset();
            return null;
         } else {
            Runnable var1 = (Runnable)this.queue.get();
            if (var1 == null) {
               this.reset();
            }

            return var1;
         }
      }

      private void reset() {
         this.running = false;
         this.submitted = false;
         this.initialRunnable = null;
         this.queue = null;
      }

      synchronized boolean add(Runnable var1) {
         if (this.initialRunnable == null) {
            this.initialRunnable = var1;
            return this.submitted;
         } else {
            if (this.queue == null) {
               this.queue = new UnsyncCircularQueue();
            }

            this.queue.put(var1);
            return this.submitted;
         }
      }

      synchronized int getPendingCount() {
         return this.queue != null ? this.queue.size() : 0;
      }

      synchronized int getExecutingCount() {
         return this.running ? 1 : 0;
      }

      public final int hashCode() {
         return this.id;
      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof RunnableWrapper)) {
            return false;
         } else {
            return ((RunnableWrapper)var1).id == this.id;
         }
      }

      synchronized void submitted() {
         this.submitted = true;
      }
   }
}
