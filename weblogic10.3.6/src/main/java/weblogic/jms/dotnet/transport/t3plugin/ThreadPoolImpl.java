package weblogic.jms.dotnet.transport.t3plugin;

import weblogic.jms.dotnet.transport.TransportExecutable;
import weblogic.jms.dotnet.transport.TransportThreadPool;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

class ThreadPoolImpl implements TransportThreadPool {
   private final WorkManager wm = WorkManagerFactory.getInstance().getDefault();

   public ThreadPoolImpl() {
   }

   public void schedule(TransportExecutable var1) {
      this.wm.schedule(new ThreadTask(var1));
   }
}
