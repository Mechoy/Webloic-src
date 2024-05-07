package weblogic.work;

public final class WorkManagerHelper {
   public static MaxThreadsConstraint getMaxThreadsConstraint(WorkManager var0) {
      if (var0 instanceof ServerWorkManagerImpl) {
         return ((ServerWorkManagerImpl)var0).getMaxThreadsConstraint();
      } else {
         if (var0 instanceof WorkManagerService) {
            WorkManager var1 = ((WorkManagerService)var0).getDelegate();
            if (var1 instanceof ServerWorkManagerImpl) {
               return ((ServerWorkManagerImpl)var1).getMaxThreadsConstraint();
            }
         }

         return null;
      }
   }

   public static boolean isDefault(WorkManager var0) {
      if (var0 == null) {
         return false;
      } else {
         String var1 = var0.getName().intern();
         return var1 == "weblogic.kernel.Default" || var1 == "default";
      }
   }

   public static void currentThreadMakingProgress() {
      if (Thread.currentThread() instanceof ExecuteThread) {
         ExecuteThread var0 = (ExecuteThread)Thread.currentThread();
         if (var0.getWorkManager() instanceof SelfTuningWorkManagerImpl) {
            SelfTuningWorkManagerImpl var1 = var0.getWorkManager();
            long var2 = RequestManager.updateRequestClass((ServiceClassStatsSupport)var1.getRequestClass(), var0);
            var0.timeStamp = var2;
         }
      }

   }
}
