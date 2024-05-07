package weblogic.cluster.singleton;

import java.util.HashMap;

public class LeaseManagerFactory {
   private static final LeaseManagerFactory singleton = new LeaseManagerFactory();
   private LeasingBasis basis;
   private int heartbeatPeriod;
   private int healthCheckPeriod;
   private int gracePeriod;
   private HashMap leaseManagers;

   private LeaseManagerFactory() {
   }

   public static LeaseManagerFactory singleton() {
      return singleton;
   }

   public void initialize(LeasingBasis var1, int var2, int var3, int var4) {
      this.basis = var1;
      this.heartbeatPeriod = var2;
      this.healthCheckPeriod = var3;
      this.gracePeriod = var4;
      this.leaseManagers = new HashMap();
   }

   public synchronized LeaseManager getLeaseManager(String var1) {
      if (this.leaseManagers.get(var1) != null) {
         return (LeaseManager)this.leaseManagers.get(var1);
      } else {
         LeaseManager var2 = new LeaseManager(this.basis, this.heartbeatPeriod, this.healthCheckPeriod, this.gracePeriod, var1);
         this.leaseManagers.put(var1, var2);
         return var2;
      }
   }
}
