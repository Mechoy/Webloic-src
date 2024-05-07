package weblogic.cluster.leasing.databaseless;

import weblogic.cluster.messaging.internal.BaseClusterMessage;
import weblogic.cluster.messaging.internal.ServerInformation;

public final class ClusterLeaderHeartbeatMessage extends BaseClusterMessage {
   private final long groupViewVersion;
   private final long leaseViewVersion;
   private static final long serialVersionUID = -3226950838012588670L;

   public ClusterLeaderHeartbeatMessage(ServerInformation var1, long var2, long var4) {
      super(var1, 7);
      this.groupViewVersion = var2;
      this.leaseViewVersion = var4;
   }

   public static ClusterLeaderHeartbeatMessage create(ClusterGroupView var0, LeaseView var1) {
      return new ClusterLeaderHeartbeatMessage(var0.getLeaderInformation(), var0.getVersionNumber(), var1.getVersionNumber());
   }

   public long getGroupViewVersion() {
      return this.groupViewVersion;
   }

   public long getLeaseViewVersion() {
      return this.leaseViewVersion;
   }

   public String toString() {
      return "[leader heartbeat message with group version " + this.groupViewVersion + " and lease version " + this.leaseViewVersion + "]";
   }
}
