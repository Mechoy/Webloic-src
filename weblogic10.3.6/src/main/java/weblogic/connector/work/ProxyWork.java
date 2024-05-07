package weblogic.connector.work;

import javax.resource.spi.work.Work;

public class ProxyWork implements Work {
   private Runnable runnable;

   ProxyWork(Runnable var1) {
      this.runnable = var1;
   }

   public void run() {
      this.runnable.run();
   }

   public Runnable getRunnable() {
      return this.runnable;
   }

   public void release() {
   }
}
