package weblogic.nodemanager.mbean;

import java.io.IOException;
import java.util.Properties;
import weblogic.kernel.ExecuteRequest;
import weblogic.kernel.ExecuteThread;
import weblogic.nodemanager.NMConnectException;
import weblogic.nodemanager.NodeManagerLogger;
import weblogic.nodemanager.client.NMClient;

class StartRequest implements ExecuteRequest, NodeManagerTask {
   private NMClient nmc;
   private Properties props;
   private Exception error;
   private long beginTime;
   private long endTime;
   private String status;
   private boolean finished;
   private String serverName;

   StartRequest(String var1, NMClient var2, Properties var3) {
      this.nmc = var2;
      this.props = var3;
      this.serverName = var1;
      this.status = "TASK IN PROGRESS";
      this.beginTime = System.currentTimeMillis();
   }

   public void execute(ExecuteThread var1) throws Exception {
      IOException var3 = null;

      String var2;
      try {
         this.start();
         var2 = "TASK COMPLETED";
      } catch (IOException var8) {
         var3 = var8;
         var2 = "FAILED";
      }

      synchronized(this) {
         if (var3 != null) {
            if (var3 instanceof NMConnectException) {
               NMConnectException var5 = (NMConnectException)var3;
               NodeManagerLogger.logNMNotRunning(var5.getHost(), Integer.toString(var5.getPort()));
            } else {
               NodeManagerLogger.logServerStartFailure(this.serverName, var3.getMessage());
            }
         }

         this.status = var2;
         this.error = var3;
         this.endTime = System.currentTimeMillis();
         this.finished = true;
         this.nmc = null;
         this.notifyAll();
      }
   }

   private void start() throws IOException {
      try {
         this.nmc.start(this.props);
      } finally {
         this.nmc.done();
      }

   }

   public synchronized boolean isFinished() {
      return this.finished;
   }

   public synchronized void waitForFinish(long var1) throws InterruptedException {
      long var3 = System.currentTimeMillis();

      for(long var5 = 0L; var5 < var1 && !this.finished; var5 = System.currentTimeMillis() - var3) {
         this.wait(var1 - var5);
      }

   }

   public synchronized void waitForFinish() throws InterruptedException {
      while(!this.finished) {
         this.wait();
      }

   }

   public void cancel() {
      throw new UnsupportedOperationException();
   }

   public synchronized long getBeginTime() {
      return this.beginTime;
   }

   public synchronized long getEndTime() {
      return this.endTime;
   }

   public synchronized String getStatus() {
      return this.status;
   }

   public synchronized Exception getError() {
      return this.error;
   }

   public synchronized void cleanup() {
      if (!this.finished) {
         throw new IllegalStateException("Task is still executing");
      }
   }
}
