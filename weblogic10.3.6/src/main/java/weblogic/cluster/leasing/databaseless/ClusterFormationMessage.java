package weblogic.cluster.leasing.databaseless;

import weblogic.cluster.messaging.internal.BaseClusterMessage;

public class ClusterFormationMessage extends BaseClusterMessage {
   private final ClusterGroupView groupView;

   public ClusterFormationMessage(ClusterGroupView var1) {
      super(var1.getLeaderInformation(), 1);
      this.groupView = var1;
   }

   ClusterGroupView getGroupView() {
      return this.groupView;
   }
}
