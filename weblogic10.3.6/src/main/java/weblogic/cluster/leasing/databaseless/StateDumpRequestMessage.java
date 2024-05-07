package weblogic.cluster.leasing.databaseless;

import weblogic.cluster.messaging.internal.BaseClusterMessage;
import weblogic.cluster.messaging.internal.ServerInformation;

public final class StateDumpRequestMessage extends BaseClusterMessage {
   private final long groupVersion;
   private final long leaseVersion;
   private static final long serialVersionUID = 2701981993990033628L;

   public StateDumpRequestMessage(ServerInformation var1, long var2, long var4) {
      super(var1, 8);
      this.groupVersion = var2;
      this.leaseVersion = var4;
   }

   public static StateDumpRequestMessage create(ServerInformation var0, ClusterGroupView var1, LeaseView var2) {
      return new StateDumpRequestMessage(var0, var1.getVersionNumber(), var2.getVersionNumber());
   }

   public String toString() {
      return "[StateDumpRequestMessage with group version " + this.groupVersion + ", leaseVersion " + this.leaseVersion + "]";
   }
}
