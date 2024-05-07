package weblogic.kernel;

import weblogic.work.RequestManager;
import weblogic.work.WorkAdapter;
import weblogic.work.WorkManagerFactory;

public class WorkManagerWrapper extends ExecuteThreadManager {
   private boolean shutdownRequested = false;

   public WorkManagerWrapper(String var1) {
      super(var1);
   }

   public boolean isShutdownInProgress() {
      return this.shutdownRequested;
   }

   ExecuteThread[] getExecuteThreads() {
      return new ExecuteThread[0];
   }

   public int getExecuteQueueDepth() {
      return RequestManager.getInstance().getQueueDepth();
   }

   public int getExecuteQueueSize() {
      return 65000;
   }

   public int getExecuteQueueDepartures() {
      return (int)RequestManager.getInstance().getQueueDepartures();
   }

   public int getExecuteThreadCount() {
      return RequestManager.getInstance().getExecuteThreadCount();
   }

   public void setThreadCount(int var1) throws SecurityException {
   }

   synchronized ExecuteThread[] getStuckExecuteThreads(long var1) {
      return new ExecuteThread[0];
   }

   void shutdown() throws SecurityException {
      this.shutdownRequested = true;
   }

   public int getIdleThreadCount() {
      return RequestManager.getInstance().getIdleThreadCount();
   }

   void registerIdle(ExecuteThread var1) {
   }

   void execute(final ExecuteRequest var1, boolean var2) {
      WorkManagerFactory.getInstance().getDefault().schedule(new WorkAdapter() {
         public void run() {
            try {
               var1.execute((ExecuteThread)null);
            } catch (Exception var2) {
               throw new RuntimeException(var2);
            }
         }
      });
   }

   boolean executeIfIdle(final ExecuteRequest var1) {
      return WorkManagerFactory.getInstance().getDefault().executeIfIdle(new WorkAdapter() {
         public void run() {
            try {
               var1.execute((ExecuteThread)null);
            } catch (Exception var2) {
               throw new RuntimeException(var2);
            }
         }
      });
   }

   int getPendingTasksCount() {
      return 0;
   }
}
