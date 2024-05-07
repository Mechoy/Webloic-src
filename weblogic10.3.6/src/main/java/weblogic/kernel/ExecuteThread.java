package weblogic.kernel;

public class ExecuteThread extends AuditableThread {
   private static final Throwable REQUEST_DEATH = new RequestDeath();
   private ExecuteThreadManager em;
   private int hashcode;
   private ExecuteRequest req = null;
   private boolean printStuckMessage = false;
   private boolean started = false;
   private int executeCount = 0;
   private long timeStamp = 0L;
   private boolean systemThread = false;

   ExecuteThread(int var1, ExecuteThreadManager var2) {
      super("ExecuteThread: '" + var1 + "' for queue: '" + var2.getName() + "'");
      this.init(var2);
   }

   ExecuteThread(int var1, ExecuteThreadManager var2, ThreadGroup var3) {
      super(var3, "ExecuteThread: '" + var1 + "' for queue: '" + var2.getName() + "'");
      this.init(var2);
   }

   protected void init(ExecuteThreadManager var1) {
      this.em = var1;
      this.hashcode = this.getName().hashCode();
   }

   public int hashCode() {
      return this.hashcode;
   }

   boolean isStarted() {
      return this.started;
   }

   public ExecuteRequest getCurrentRequest() {
      return this.req;
   }

   int getExecuteCount() {
      return this.executeCount;
   }

   public void setTimeStamp(long var1) {
      this.timeStamp = var1;
   }

   long getTimeStamp() {
      return this.timeStamp;
   }

   void setPrintStuckThreadMessage(boolean var1) {
      this.printStuckMessage = var1;
   }

   boolean getPrintStuckThreadMessage() {
      return this.printStuckMessage;
   }

   public void setSystemThread(boolean var1) {
      this.systemThread = var1;
   }

   boolean getSystemThread() {
      return this.systemThread;
   }

   public ExecuteThreadManager getExecuteThreadManager() {
      return this.em;
   }

   synchronized void notifyRequest(ExecuteRequest var1) {
      this.setTimeStamp(System.currentTimeMillis());
      this.req = var1;
      this.notify();
   }

   void setRequest(ExecuteRequest var1) {
      this.req = var1;
   }

   private synchronized void waitForRequest() {
      while(this.req == null) {
         try {
            this.wait();
         } catch (InterruptedException var2) {
         }
      }

   }

   protected synchronized void readyToRun() {
      this.started = true;
      super.readyToRun();
      this.notify();
   }

   public void run() {
      this.readyToRun();

      while(true) {
         while(true) {
            try {
               this.reset();
               this.em.registerIdle(this);
               if (this.req == null) {
                  this.waitForRequest();
               }

               this.execute(this.req);
            } catch (ExecuteThreadManager.ShutdownError var2) {
               if (Kernel.isServer() && !Kernel.isIntentionalShutdown()) {
                  KernelLogger.logStopped(this.getName());
               }

               return;
            } catch (ThreadDeath var3) {
               if (Kernel.isServer()) {
                  if (!Kernel.isIntentionalShutdown()) {
                     KernelLogger.logStopped(this.getName());
                  }

                  throw var3;
               }
            }
         }
      }
   }

   void execute(ExecuteRequest var1) {
      try {
         ++this.executeCount;
         this.setTimeStamp(System.currentTimeMillis());
         var1.execute(this);
      } catch (ThreadDeath var3) {
         throw var3;
      } catch (ExecuteThreadManager.ShutdownError var4) {
         throw var4;
      } catch (RequestDeath var5) {
         KernelLogger.logExecuteCancelled(var1.toString());
      } catch (Throwable var6) {
         if (!Kernel.isApplet()) {
            KernelLogger.logExecuteFailed(var6);
         } else {
            var6.printStackTrace();
         }
      }

   }

   protected void reset() {
      super.reset();
      this.req = null;
   }

   private static final class RequestDeath extends Error {
      private RequestDeath() {
      }

      // $FF: synthetic method
      RequestDeath(Object var1) {
         this();
      }
   }
}
