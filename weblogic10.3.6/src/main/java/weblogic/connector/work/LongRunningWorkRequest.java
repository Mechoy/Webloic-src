package weblogic.connector.work;

import javax.resource.spi.work.ExecutionContext;
import javax.resource.spi.work.Work;
import javax.resource.spi.work.WorkException;
import javax.resource.spi.work.WorkListener;
import weblogic.connector.common.Debug;
import weblogic.connector.common.RAInstanceManager;

public class LongRunningWorkRequest extends WorkRequest {
   private LongRunningWorkManager lrwm;
   private boolean finished;

   LongRunningWorkRequest(Work var1, long var2, ExecutionContext var4, WorkListener var5, RAInstanceManager var6, LongRunningWorkManager var7) throws WorkException {
      super(var1, var2, var4, var5, var6);
      this.lrwm = var7;
   }

   public void run() {
      try {
         if (Debug.isWorkEnabled()) {
            Debug.work("start to run LongRunning work: " + this.getWork());
         }

         super.run();
      } finally {
         this.lrwm.unregister(this);
         Debug.work("finished run LongRunning work: " + this.getWork());
         synchronized(this) {
            this.finished = true;
            this.notifyAll();
         }
      }

   }

   protected void release() {
      if (Debug.isWorkEnabled()) {
         Debug.work("release LongRunning work: " + this.getWork());
      }

      this.getWork().release();
   }

   void blockTillCompletion() {
      super.blockTillCompletion();
      synchronized(this) {
         while(!this.finished) {
            try {
               this.wait();
            } catch (InterruptedException var4) {
            }
         }

      }
   }
}
