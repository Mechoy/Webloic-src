package weblogic.cluster.singleton;

import weblogic.server.AbstractServerService;

public abstract class AbstractConsensusService extends AbstractServerService {
   protected static AbstractConsensusService instance;

   public abstract LeasingBasis createConsensusBasis(int var1, int var2);

   public abstract void addClusterLeaderListener(ClusterLeaderListener var1);

   public abstract void addConsensusServiceGroupViewListener(ConsensusServiceGroupViewListener var1);

   public abstract void removeConsensusServiceGroupViewListener(ConsensusServiceGroupViewListener var1);

   public abstract String getServerState(String var1);

   public abstract String getLeasingBasisLocation();

   public static AbstractConsensusService getInstance() {
      return instance;
   }
}
