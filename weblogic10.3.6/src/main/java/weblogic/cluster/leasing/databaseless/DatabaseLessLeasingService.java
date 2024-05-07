package weblogic.cluster.leasing.databaseless;

import java.util.Iterator;
import weblogic.cluster.messaging.internal.SRMResult;
import weblogic.cluster.messaging.internal.ServerInformation;
import weblogic.cluster.singleton.AbstractConsensusService;
import weblogic.cluster.singleton.ClusterLeaderListener;
import weblogic.cluster.singleton.ConsensusServiceGroupViewListener;
import weblogic.cluster.singleton.LeasingBasis;
import weblogic.utils.collections.ArraySet;
import weblogic.work.WorkManagerFactory;

public final class DatabaseLessLeasingService extends AbstractConsensusService {
   private boolean isClusterLeader;
   private final ArraySet clusterLeaderListeners = new ArraySet();
   private final ArraySet consensusServiceGroupViewListeners = new ArraySet();

   public DatabaseLessLeasingService() {
      synchronized(DatabaseLessLeasingService.class) {
         if (instance != null) {
            throw new AssertionError("Duplicate DatabaseLessLeasingService instance");
         } else {
            instance = this;
         }
      }
   }

   public LeasingBasis createConsensusBasis(int var1, int var2) {
      return new LeaseClient();
   }

   public void addClusterLeaderListener(ClusterLeaderListener var1) {
      synchronized(this.clusterLeaderListeners) {
         if (this.isClusterLeader) {
            var1.localServerIsClusterLeader();
         } else {
            this.clusterLeaderListeners.add(var1);
         }

      }
   }

   public String getServerState(String var1) {
      String var2 = null;
      SRMResult var3 = EnvironmentFactory.getServerReachabilityMajorityService().getLastSRMResult();
      if (var3 != null) {
         var2 = var3.getServerState(var1);
      }

      return var2;
   }

   public void addConsensusServiceGroupViewListener(ConsensusServiceGroupViewListener var1) {
      this.consensusServiceGroupViewListeners.add(var1);
   }

   public void removeConsensusServiceGroupViewListener(ConsensusServiceGroupViewListener var1) {
      this.consensusServiceGroupViewListeners.remove(var1);
   }

   void fireConsensusServiceGroupViewListenerEvent(final ServerInformation var1, boolean var2) {
      Iterator var3 = this.consensusServiceGroupViewListeners.iterator();

      while(var3.hasNext()) {
         final ConsensusServiceGroupViewListener var4 = (ConsensusServiceGroupViewListener)var3.next();
         if (var2) {
            WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
               public void run() {
                  var4.memberAdded(var1.getServerName());
               }
            });
         } else {
            WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
               public void run() {
                  var4.memberRemoved(var1.getServerName());
               }
            });
         }
      }

   }

   public String getLeasingBasisLocation() {
      return ClusterLeaderService.getInstance().getLeaderName();
   }

   void localServerIsClusterLeader() {
      synchronized(this.clusterLeaderListeners) {
         Iterator var2 = this.clusterLeaderListeners.iterator();

         while(var2.hasNext()) {
            final ClusterLeaderListener var3 = (ClusterLeaderListener)var2.next();
            WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
               public void run() {
                  var3.localServerIsClusterLeader();
               }
            });
         }

         this.isClusterLeader = true;
      }
   }

   void localServerLostClusterLeadership() {
      synchronized(this.clusterLeaderListeners) {
         Iterator var2 = this.clusterLeaderListeners.iterator();

         while(var2.hasNext()) {
            final ClusterLeaderListener var3 = (ClusterLeaderListener)var2.next();
            WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
               public void run() {
                  var3.localServerLostClusterLeadership();
               }
            });
         }

         this.isClusterLeader = false;
      }
   }

   boolean isClusterLeader() {
      return this.isClusterLeader;
   }
}
