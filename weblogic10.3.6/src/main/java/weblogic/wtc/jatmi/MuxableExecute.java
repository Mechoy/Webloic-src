package weblogic.wtc.jatmi;

import weblogic.kernel.ExecuteRequest;
import weblogic.kernel.ExecuteThread;

public final class MuxableExecute implements ExecuteRequest {
   private rdsession myTuxReadSession;
   private tfmh myTmmsg;

   public MuxableExecute(rdsession var1) {
      this.myTuxReadSession = var1;
   }

   public MuxableExecute(rdsession var1, tfmh var2) {
      this.myTuxReadSession = var1;
      this.myTmmsg = var2;
   }

   private void prepareForCache() {
      this.myTmmsg = null;
   }

   public void execute(ExecuteThread var1) throws Exception {
      if (this.myTuxReadSession != null && this.myTmmsg != null) {
         this.myTuxReadSession.dispatch(this.myTmmsg);
         this.prepareForCache();
         this.myTuxReadSession.restoreExecuteRequestToCache(this);
      }
   }

   public void setTmmsg(tfmh var1) {
      this.myTmmsg = var1;
   }
}
