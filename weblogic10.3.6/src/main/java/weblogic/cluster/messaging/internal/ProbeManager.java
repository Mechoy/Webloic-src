package weblogic.cluster.messaging.internal;

import java.util.Iterator;
import java.util.LinkedList;

public final class ProbeManager {
   private LinkedList list = new LinkedList();
   private static ProbeManager clusterMasterProbeManager;
   private static ProbeManager clusterMemberProbeManager;

   public static ProbeManager getClusterMasterProbeManager() {
      if (clusterMasterProbeManager == null) {
         clusterMasterProbeManager = new ProbeManager();
         clusterMasterProbeManager.add(new HttpPingProbe());
         clusterMasterProbeManager.add(new NodeManagerQueryProbe());
      }

      return clusterMasterProbeManager;
   }

   public static ProbeManager getClusterMemberProbeManager() {
      if (clusterMemberProbeManager == null) {
         clusterMemberProbeManager = new ProbeManager();
         clusterMemberProbeManager.add(new LeaseTableReachabilityProbe());
      }

      return clusterMemberProbeManager;
   }

   protected ProbeManager() {
   }

   public synchronized void add(Probe var1) {
      this.list.add(var1);
   }

   public synchronized Probe remove(Probe var1) {
      int var2 = this.list.indexOf(var1);
      return (Probe)this.list.remove(var2);
   }

   synchronized LinkedList getProbes() {
      return new LinkedList(this.list);
   }

   public synchronized void invoke(ProbeContext var1) {
      Iterator var2 = this.list.iterator();

      while(var2.hasNext()) {
         Probe var3 = (Probe)var2.next();
         var3.invoke(var1);
         if (var1.getNextAction() != 1) {
            break;
         }
      }

   }
}
