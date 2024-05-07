package weblogic.wsee.jaxws.spi;

import java.util.concurrent.Executor;
import weblogic.work.ExecuteThread;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

public class WorkManagerExecutor implements Executor {
   private WorkManager wm;

   public static Executor getExecutor() {
      Thread var0 = Thread.currentThread();
      Object var1 = null;
      if (var0 instanceof ExecuteThread) {
         var1 = ((ExecuteThread)var0).getWorkManager();
      }

      if (var1 == null) {
         var1 = WorkManagerFactory.getInstance().getDefault();
      }

      return new WorkManagerExecutor((WorkManager)var1);
   }

   private WorkManagerExecutor(WorkManager var1) {
      this.wm = var1;
   }

   public void execute(Runnable var1) {
      this.wm.schedule(var1);
   }
}
