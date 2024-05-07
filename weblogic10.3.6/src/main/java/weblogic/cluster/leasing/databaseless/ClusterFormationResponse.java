package weblogic.cluster.leasing.databaseless;

import weblogic.cluster.messaging.internal.ClusterResponse;
import weblogic.cluster.messaging.internal.ServerInformation;

public final class ClusterFormationResponse implements ClusterResponse {
   private final ClusterFormationMessage clusterFormationMessage;
   private final ClusterFormationMessage acceptedFormationMessage;
   private final boolean accepted;
   private final ServerInformation leaderInformation;
   private ServerInformation localInformation;
   private final LeaseView leaseView;
   private static final long serialVersionUID = -2750818906106645449L;

   private ClusterFormationResponse(ClusterFormationMessage var1, ClusterFormationMessage var2, ServerInformation var3, ServerInformation var4, LeaseView var5, boolean var6) {
      this.clusterFormationMessage = var1;
      this.acceptedFormationMessage = var2;
      this.leaderInformation = var3;
      this.localInformation = var4;
      this.leaseView = var5;
      this.accepted = var6;
   }

   public static ClusterFormationResponse getAcceptedResponse(ClusterFormationMessage var0, ClusterFormationMessage var1, ServerInformation var2, LeaseView var3) {
      return new ClusterFormationResponse(var0, var1, (ServerInformation)null, var2, var3, true);
   }

   public static ClusterResponse getRejectedResponse(ClusterFormationMessage var0, ClusterFormationMessage var1, ServerInformation var2, ServerInformation var3) {
      return new ClusterFormationResponse(var0, var1, var2, var3, (LeaseView)null, false);
   }

   ClusterFormationMessage getAcceptedFormationMessage() {
      return this.acceptedFormationMessage;
   }

   boolean isAccepted() {
      return this.accepted;
   }

   ServerInformation getLeaderInformation() {
      return this.leaderInformation;
   }

   ServerInformation getReceiverInformation() {
      return this.localInformation;
   }

   public String toString() {
      return "[ClusterFormationResponse accepted " + this.accepted + " with leader info " + this.leaderInformation + " and accepted formation msg " + this.acceptedFormationMessage + "]";
   }

   public LeaseView getLeaseView() {
      return this.leaseView;
   }
}
