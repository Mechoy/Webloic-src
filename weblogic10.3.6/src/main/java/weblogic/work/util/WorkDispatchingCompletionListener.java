package weblogic.work.util;

import weblogic.utils.concurrent.CompletionListener;
import weblogic.utils.concurrent.Future;
import weblogic.work.WorkAdapter;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

public abstract class WorkDispatchingCompletionListener extends WorkAdapter implements CompletionListener {
   private Future future;
   private final WorkManager manager = WorkManagerFactory.getInstance().getSystem();

   public void done(Future var1) {
      this.future = var1;
      this.manager.schedule(this);
   }

   protected Future getFuture() {
      return this.future;
   }

   public abstract void run();
}
