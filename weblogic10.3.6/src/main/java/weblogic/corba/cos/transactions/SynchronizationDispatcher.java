package weblogic.corba.cos.transactions;

import javax.transaction.Synchronization;

public class SynchronizationDispatcher implements Synchronization {
   org.omg.CosTransactions.Synchronization sync;

   public SynchronizationDispatcher(org.omg.CosTransactions.Synchronization var1) {
      this.sync = var1;
   }

   public void afterCompletion(int var1) {
      this.sync.after_completion(OTSHelper.jta2otsStatus(var1));
   }

   public void beforeCompletion() {
      this.sync.before_completion();
   }
}
