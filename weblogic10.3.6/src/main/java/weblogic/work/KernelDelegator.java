package weblogic.work;

import weblogic.kernel.ExecuteThread;
import weblogic.kernel.Kernel;
import weblogic.management.configuration.ExecuteQueueMBean;
import weblogic.utils.UnsyncCircularQueue;

public final class KernelDelegator extends WorkManagerImpl {
   private int dispatchId;

   KernelDelegator() {
      this.wmName = "direct";
   }

   KernelDelegator(String var1, ExecuteQueueMBean var2) {
      Kernel.addExecuteQueue(var1, var2, true);
      this.dispatchId = Kernel.getDispatchPolicyIndex(var1);
      this.wmName = var1;
   }

   KernelDelegator(String var1, int var2) {
      if (var2 > 0) {
         Kernel.addExecuteQueue(var1, var2);
         this.dispatchId = Kernel.getDispatchPolicyIndex(var1);
      } else {
         this.dispatchId = Kernel.getDispatchPolicyIndex("weblogic.kernel.Default");
      }

      this.wmName = var1;
   }

   public int getType() {
      return 2;
   }

   public int getConfiguredThreadCount() {
      return Kernel.getExecuteThreadManager(this.wmName).getExecuteThreadCount();
   }

   public void setThreadCount(int var1) {
      Kernel.getExecuteThreadManager(this.wmName).setThreadCount(var1);
   }

   public void schedule(Runnable var1) {
      if ("direct" == this.wmName) {
         var1.run();
      } else {
         try {
            Kernel.execute(new ExecuteRequestAdapter(var1), this.dispatchId);
         } catch (UnsyncCircularQueue.FullQueueException var4) {
            Runnable var3 = null;
            if (var1 instanceof Work) {
               var3 = ((Work)var1).overloadAction(var4.getMessage());
            }

            if (var3 == null) {
               WorkManagerLogger.logScheduleFailed(this.wmName, var4);
               throw var4;
            }

            var3.run();
         }

      }
   }

   public boolean executeIfIdle(Runnable var1) {
      Kernel.executeIfIdle(new ExecuteRequestAdapter(var1), this.dispatchId);
      return true;
   }

   public boolean scheduleIfBusy(Runnable var1) {
      if (Kernel.getExecuteQueueDepth(this.dispatchId) > 0) {
         this.schedule(var1);
         return true;
      } else {
         Thread var2 = Thread.currentThread();
         if (var2 instanceof ExecuteThread) {
            ((ExecuteThread)var2).setTimeStamp(System.currentTimeMillis());
         }

         return false;
      }
   }

   public int getQueueDepth() {
      return Kernel.getExecuteQueueDepth(this.dispatchId);
   }

   public boolean isThreadOwner(Thread var1) {
      if (!(var1 instanceof ExecuteThread)) {
         return false;
      } else {
         ExecuteThread var2 = (ExecuteThread)var1;
         if (var2.getExecuteThreadManager() == null) {
            return false;
         } else {
            return this.wmName.equalsIgnoreCase(var2.getExecuteThreadManager().getName());
         }
      }
   }
}
