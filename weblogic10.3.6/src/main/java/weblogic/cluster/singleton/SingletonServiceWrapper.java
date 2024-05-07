package weblogic.cluster.singleton;

public class SingletonServiceWrapper implements SingletonService, LeaseLostListener {
   private SingletonService delegate;
   private boolean active = false;

   public SingletonServiceWrapper(SingletonService var1) {
      this.delegate = var1;
   }

   public void onRelease() {
      this.deactivate();
   }

   public synchronized void activate() {
      if (!this.active) {
         this.delegate.activate();
         this.registerWithSingletonMonitorLeaseManager();
         this.active = true;
      }
   }

   public synchronized void deactivate() {
      if (this.active) {
         this.unregisterWithSingletonMonitorLeaseManager();
         this.delegate.deactivate();
         this.active = false;
      }
   }

   private void registerWithSingletonMonitorLeaseManager() {
      LeaseManager var1 = LeaseManagerFactory.singleton().getLeaseManager("service");
      var1.addLeaseLostListener(this);
   }

   private void unregisterWithSingletonMonitorLeaseManager() {
      LeaseManager var1 = LeaseManagerFactory.singleton().getLeaseManager("service");
      var1.removeLeaseLostListener(this);
   }
}
