package weblogic.cluster.messaging.internal;

import weblogic.cluster.ClusterService;
import weblogic.cluster.singleton.LeaseManager;
import weblogic.cluster.singleton.LeasingException;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;

public class LeaseTableReachabilityProbe implements Probe {
   private static final DebugCategory debugDisconnectMonitor = Debug.getCategory("weblogic.cluster.leasing.DisconnectMonitor");
   private static final boolean DEBUG = debugEnabled();
   private LeaseManager servicesLeaseManager = ClusterService.getClusterService().getDefaultLeaseManager("service");

   public void invoke(ProbeContext var1) {
      if (DEBUG) {
         debug(" Checking Lease Table Reachability");
      }

      try {
         String var2 = this.servicesLeaseManager.findOwner("SINGLETON_MASTER");
      } catch (LeasingException var4) {
         String var3 = "Server cannot reach Lease table";
         var1.setMessage(var3);
         var1.setNextAction(0);
         var1.setResult(-1);
         return;
      }

      var1.setNextAction(1);
      var1.setResult(1);
   }

   private static boolean debugEnabled() {
      return debugDisconnectMonitor.isEnabled();
   }

   private static void debug(String var0) {
      System.out.println("[LeaseTableReachabilityProbe] " + var0);
   }
}
