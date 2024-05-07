package weblogic.connector.work;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import weblogic.work.WorkManagerImpl;

public class LongRunningWorkManager {
   private final String name;
   private final List<LongRunningWorkRequest> activeWorks = new ArrayList();
   private volatile int activeWorkCount = 0;
   private volatile int completedWorkCount = 0;
   private volatile int workCount = 0;

   LongRunningWorkManager(String var1) {
      this.name = var1;
   }

   public void schedule(LongRunningWorkRequest var1) {
      int var2;
      synchronized(this) {
         var2 = this.workCount++;
         ++this.activeWorkCount;
         this.activeWorks.add(var1);
      }

      WorkManagerImpl.executeDaemonTask("LongRunning Work-" + this.name + "-" + var2, 5, var1);
   }

   public synchronized void cleanup() {
      Iterator var1 = this.activeWorks.iterator();

      while(var1.hasNext()) {
         LongRunningWorkRequest var2 = (LongRunningWorkRequest)var1.next();
         var2.release();
      }

      this.activeWorks.clear();
   }

   public int getActiveWorkCount() {
      return this.activeWorkCount;
   }

   public int getCompletedWorkCount() {
      return this.completedWorkCount;
   }

   protected synchronized void unregister(LongRunningWorkRequest var1) {
      this.activeWorks.remove(var1);
      --this.activeWorkCount;
      ++this.completedWorkCount;
   }
}
