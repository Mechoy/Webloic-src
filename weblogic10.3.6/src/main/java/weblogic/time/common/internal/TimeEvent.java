package weblogic.time.common.internal;

import weblogic.work.WorkManager;

public abstract class TimeEvent {
   public long time = 0L;
   TimeEvent next = null;
   TimeEvent prev = null;
   WorkManager workManager;

   void insertAfter(TimeEvent var1) {
      var1.next = this.next;
      var1.prev = this;
      if (var1.next != null) {
         var1.next.prev = var1;
      }

      this.next = var1;
   }

   void remove() {
      if (this.prev != null) {
         this.prev.next = this.next;
      }

      if (this.next != null) {
         this.next.prev = this.prev;
      }

      this.next = null;
      this.prev = null;
   }

   public abstract void executeTimer(long var1, WorkManager var3, boolean var4);

   public void setWorkManager(WorkManager var1) {
      this.workManager = var1;
   }

   public WorkManager getWorkManager() {
      return this.workManager;
   }

   protected void destroy() {
      this.time = 0L;
      this.next = null;
      this.prev = null;
      this.workManager = null;
   }
}
