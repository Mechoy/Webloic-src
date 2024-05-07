package weblogic.work;

import weblogic.kernel.ExecuteRequest;
import weblogic.kernel.ExecuteThread;

public final class ExecuteRequestAdapter implements ExecuteRequest {
   private Runnable work;

   public ExecuteRequestAdapter(Runnable var1) {
      this.work = var1;
   }

   public void execute(ExecuteThread var1) throws Exception {
      this.work.run();
   }

   public String toString() {
      return this.work.toString();
   }
}
