package weblogic.cluster.leasing.databaseless;

import weblogic.cluster.messaging.internal.BaseClusterMessage;
import weblogic.cluster.messaging.internal.ServerInformation;

public final class JoinResponseMessage extends BaseClusterMessage {
   private static final String ACCEPTED = "accepted";
   private static final String REJECTED = "rejected";
   private final ClusterGroupView groupView;
   private final LeaseView leaseView;
   private final String decision;
   private static final long serialVersionUID = -1916452981168517591L;

   private JoinResponseMessage(ClusterGroupView var1, LeaseView var2) {
      super(var1.getLeaderInformation(), 3);
      this.groupView = var1;
      this.leaseView = var2;
      this.decision = "accepted";
   }

   private JoinResponseMessage(ServerInformation var1) {
      super(var1, 3);
      this.groupView = null;
      this.leaseView = null;
      this.decision = "rejected";
   }

   static JoinResponseMessage getRejectedResponse(ServerInformation var0) {
      return new JoinResponseMessage(var0);
   }

   static JoinResponseMessage getAcceptedResponse(ClusterGroupView var0, LeaseView var1) {
      return new JoinResponseMessage(var0, var1);
   }

   ClusterGroupView getGroupView() {
      return this.groupView;
   }

   LeaseView getLeaseView() {
      return this.leaseView;
   }

   boolean isAccepted() {
      return "accepted".equals(this.decision);
   }
}
