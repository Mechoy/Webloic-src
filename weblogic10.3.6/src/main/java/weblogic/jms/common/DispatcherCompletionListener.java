package weblogic.jms.common;

import weblogic.messaging.dispatcher.Request;
import weblogic.messaging.kernel.KernelListener;
import weblogic.messaging.kernel.KernelRequest;

public class DispatcherCompletionListener implements KernelListener {
   protected Request request;

   public DispatcherCompletionListener(Request var1) {
      this.request = var1;
   }

   public void onCompletion(KernelRequest var1, Object var2) {
      this.request.resumeExecution(false);
   }

   public void onException(KernelRequest var1, Throwable var2) {
      this.request.resumeExecution(false);
   }
}
