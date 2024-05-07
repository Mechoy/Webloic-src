package weblogic.cluster.leasing.databaseless;

import java.rmi.RemoteException;
import java.util.HashMap;
import weblogic.cluster.ClusterService;
import weblogic.cluster.messaging.internal.ClusterMessage;
import weblogic.cluster.messaging.internal.ClusterMessageFactory;
import weblogic.cluster.messaging.internal.ClusterMessageProcessingException;
import weblogic.cluster.messaging.internal.ClusterMessageReceiver;
import weblogic.cluster.messaging.internal.ClusterMessageSender;
import weblogic.cluster.messaging.internal.ClusterResponse;
import weblogic.cluster.messaging.internal.DebugLogger;
import weblogic.cluster.messaging.internal.ServerInformation;
import weblogic.cluster.messaging.internal.ServerInformationImpl;
import weblogic.health.HealthMonitorService;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;

public class ClusterMember implements ClusterMessageReceiver, DisconnectActionListener {
   private static final DebugCategory debugClusterMember;
   private static final boolean DEBUG;
   private final ServerInformation localInformation;
   private ClusterGroupView groupView;
   private LeaseView leaseView;
   private ServerInformation leaderInformation;
   private ClusterFormationMessage acceptedFormationMessage;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public static ClusterMember getInstance() {
      return ClusterMember.Factory.THE_ONE;
   }

   private ClusterMember() {
      this.localInformation = createLocalServerInformation();
      ClusterMessageFactory.getInstance().registerReceiver(this);
   }

   private static ServerInformation createLocalServerInformation() {
      return new ServerInformationImpl(ClusterService.getClusterService().getLocalMember());
   }

   public ServerInformation getLeaderInformation() {
      return this.leaderInformation;
   }

   ServerInformation getLocalServerInformation() {
      return this.localInformation;
   }

   ClusterGroupView getGroupView() {
      return this.groupView;
   }

   LeaseView getLeaseView() {
      return this.leaseView;
   }

   void addMember(ServerInformation var1) {
      this.groupView.addMember(var1);
   }

   void removeMember(ServerInformation var1) {
      this.groupView.removeMember(var1);
   }

   public String getLeaderName() {
      ServerInformation var1 = this.getLeaderInformation();
      return var1 != null ? var1.getServerName() : null;
   }

   public boolean accept(ClusterMessage var1) {
      int var2 = var1.getMessageType();
      return var2 == 1 || var2 == 3 || var2 == 5 || var2 == 6 || var2 == 7 || var2 == 10;
   }

   public synchronized ClusterResponse process(ClusterMessage var1) throws ClusterMessageProcessingException {
      if (DEBUG) {
         debug("received remote message " + var1);
      }

      if ("failed".equals(ClusterState.getInstance().getState())) {
         throw new ClusterMessageProcessingException("cannot process message '" + var1 + "' as the server is in failed state");
      } else if (var1.getMessageType() == 1) {
         return this.handleFormationRequest((ClusterFormationMessage)var1);
      } else if (var1.getMessageType() == 3) {
         return this.handleJoinResponseRequest((JoinResponseMessage)var1);
      } else if (var1.getMessageType() == 5) {
         return this.handleLeaseTableUpdateRequest((LeaseTableUpdateMessage)var1);
      } else if (var1.getMessageType() == 6) {
         return this.handleGroupViewUpdateRequest((GroupViewUpdateMessage)var1);
      } else if (var1.getMessageType() == 7) {
         return this.handleLeaderHeartbeatRequest((ClusterLeaderHeartbeatMessage)var1);
      } else if (var1.getMessageType() == 10) {
         return this.handleLeaderQuery((LeaderQueryMessage)var1);
      } else {
         throw new AssertionError("Received an unsolicited request " + var1);
      }
   }

   private void sendJoinRequestMessage(ServerInformation var1) {
      JoinRequestMessage var2 = JoinRequestMessage.create(this.localInformation);
      ClusterMessageSender var3 = ClusterMessageFactory.getInstance().getOneWayMessageSender();
      if (DEBUG) {
         debug("sending join request message to " + var1);
      }

      try {
         var3.send(var2, (ServerInformation)var1);
      } catch (RemoteException var5) {
         if (DEBUG) {
            debug("join request message to " + var1 + " failed with " + var5.getMessage());
            var5.printStackTrace();
         }
      }

   }

   private ClusterResponse handleLeaderHeartbeatRequest(ClusterLeaderHeartbeatMessage var1) {
      if (ClusterState.getInstance().getState() == "discovery") {
         this.sendJoinRequestMessage(var1.getSenderInformation());
         return null;
      } else if (ClusterState.getInstance().getState() != "stable") {
         return null;
      } else {
         if (var1.getGroupViewVersion() != this.groupView.getVersionNumber() || var1.getLeaseViewVersion() != this.leaseView.getVersionNumber()) {
            StateDumpRequestMessage var2 = StateDumpRequestMessage.create(this.localInformation, this.groupView, this.leaseView);
            ClusterMessageSender var3 = ClusterMessageFactory.getInstance().getDefaultMessageSender();
            if (DEBUG) {
               debug("sending " + var2 + " to leader " + this.leaderInformation);
            }

            try {
               StateDumpResponse var4 = (StateDumpResponse)var3.send(var2, (ServerInformation)this.leaderInformation);
               this.leaseView.processStateDump(var4.getLeaseView());
               this.groupView.processStateDump(var4.getGroupView());
            } catch (RemoteException var5) {
            }
         }

         return null;
      }
   }

   private synchronized ClusterResponse handleGroupViewUpdateRequest(GroupViewUpdateMessage var1) throws ClusterMessageProcessingException {
      if (this.groupView == null) {
         throw new ClusterMessageProcessingException("Unacceptable group view update.  Cluster member has stopped.  Received:" + var1);
      } else if (this.groupView.getVersionNumber() + 1L != var1.getVersionNumber()) {
         throw new ClusterMessageProcessingException("unacceptable group view update. local version " + this.groupView.getVersionNumber() + " and received version is " + var1.getVersionNumber());
      } else {
         if (var1.getOperation() == 1) {
            this.addMember(var1.getServerInformation());
            this.groupView.incrementVersionNumber();
         } else {
            if (var1.getOperation() != 2) {
               throw new AssertionError("unsupported group view update message " + var1);
            }

            this.removeMember(var1.getServerInformation());
            this.groupView.incrementVersionNumber();
         }

         return null;
      }
   }

   private ClusterResponse handleLeaseTableUpdateRequest(LeaseTableUpdateMessage var1) throws ClusterMessageProcessingException {
      if (!$assertionsDisabled && this.leaseView == null) {
         throw new AssertionError();
      } else {
         this.leaseView.process(var1);
         return null;
      }
   }

   private ClusterResponse handleJoinResponseRequest(JoinResponseMessage var1) throws LeaderAlreadyExistsException {
      String var2 = ClusterState.getInstance().getState();
      if (var2.equals("stable")) {
         if (this.leaderInformation == null || !this.leaderInformation.equals(var1.getSenderInformation())) {
            throw new LeaderAlreadyExistsException(ClusterState.getInstance().getErrorMessage("stable"));
         }
      } else if (!ClusterState.getInstance().setState("stable")) {
         throw new LeaderAlreadyExistsException(ClusterState.getInstance().getErrorMessage("stable"));
      }

      if (var2.equals("discovery")) {
         EnvironmentFactory.getDiscoveryService().stop();
      } else {
         EnvironmentFactory.getClusterMemberDisconnectMonitor().stop();
      }

      if (!var1.isAccepted()) {
         this.fatalError();
         return null;
      } else {
         this.groupView = var1.getGroupView();
         if (this.leaseView == null) {
            this.leaseView = new LeaseView(this.localInformation.getServerName(), (HashMap)null);
         }

         this.leaseView.processStateDump(var1.getLeaseView());
         this.leaderInformation = this.groupView.getLeaderInformation();
         EnvironmentFactory.getClusterMemberDisconnectMonitor().start(this.groupView, this);
         if (DEBUG) {
            debug("installed leader with group view " + this.groupView);
         }

         return null;
      }
   }

   private synchronized ClusterResponse handleFormationRequest(ClusterFormationMessage var1) {
      String var2 = ClusterState.getInstance().getState();
      if (this.acceptFormationRequest(var1) && ClusterState.getInstance().setState("formation")) {
         this.acceptedFormationMessage = var1;
         this.groupView = this.acceptedFormationMessage.getGroupView();
         if (var2.equals("discovery")) {
            EnvironmentFactory.getDiscoveryService().stop();
         }

         if (DEBUG) {
            debug("stopping current disconnect monitor during formation");
         }

         EnvironmentFactory.getClusterMemberDisconnectMonitor().stop();
         if (DEBUG) {
            debug("starting new disconnect monitor...");
         }

         EnvironmentFactory.getClusterMemberDisconnectMonitor().start(this.acceptedFormationMessage.getGroupView(), this);
         if (DEBUG) {
            debug("sending accepted formation response");
         }

         return ClusterFormationResponse.getAcceptedResponse(var1, this.acceptedFormationMessage, this.localInformation, this.leaseView);
      } else if (this.acceptedFormationMessage != null && this.acceptedFormationMessage.getSenderInformation().equals(var1.getSenderInformation())) {
         return ClusterFormationResponse.getAcceptedResponse(var1, this.acceptedFormationMessage, this.localInformation, this.leaseView);
      } else {
         if (DEBUG) {
            debug("sending rejected formation response for message " + var1);
         }

         return ClusterFormationResponse.getRejectedResponse(var1, this.acceptedFormationMessage, this.getLeaderInformation(), this.localInformation);
      }
   }

   private boolean acceptFormationRequest(ClusterFormationMessage var1) {
      if (this.acceptedFormationMessage == null && this.leaderInformation == null) {
         return this.localInformation.compareTo(var1.getSenderInformation()) > 0;
      } else {
         return false;
      }
   }

   public synchronized void stop() {
      this.groupView = null;
      this.acceptedFormationMessage = null;
      this.leaderInformation = null;
      EnvironmentFactory.getClusterMemberDisconnectMonitor().stop();
   }

   private void fatalError() {
      if (DEBUG) {
         debug("OnLosingServerReachabilityMajority() called ! marking the server as failed");
      }

      String var1 = "Server is not in the majority cluster partition";
      ClusterState.getInstance().setState("failed", var1);
      this.stop();
      HealthMonitorService.subsystemFailed("DatabaseLessLeasing", var1);
   }

   public void OnBecomingSeniorMostMember() {
      if (!$assertionsDisabled && this.groupView == null) {
         throw new AssertionError();
      } else {
         ClusterGroupView var1 = this.groupView;
         this.stop();
         EnvironmentFactory.getClusterFormationService().start(var1, this.leaseView);
      }
   }

   public void OnLosingServerReachabilityMajority() {
      this.fatalError();
   }

   public synchronized void onLosingLeader() {
      if (!ClusterState.getInstance().setState("discovery")) {
         throw new AssertionError(ClusterState.getInstance().getErrorMessage("discovery"));
      } else if (!$assertionsDisabled && this.groupView == null) {
         throw new AssertionError();
      } else {
         this.groupView.removeLeader();
         this.acceptedFormationMessage = null;
         this.leaderInformation = null;
      }
   }

   public void onLosingMember(ServerInformation var1) {
      this.groupView.removeMember(var1);
   }

   private ClusterResponse handleLeaderQuery(LeaderQueryMessage var1) {
      DatabaseLessLeasingService var2 = (DatabaseLessLeasingService)DatabaseLessLeasingService.getInstance();
      if (var2.isClusterLeader()) {
         return new LeaderQueryResponse(this.localInformation);
      } else {
         return this.leaderInformation != null ? new LeaderQueryResponse(this.leaderInformation) : new LeaderQueryResponse();
      }
   }

   public String toString() {
      return "[ClusterMember with view " + this.groupView + "]";
   }

   private static void debug(String var0) {
      DebugLogger.debug("[ClusterMember] " + var0);
   }

   private static boolean debugEnabled() {
      return debugClusterMember.isEnabled() || DebugLogger.isDebugEnabled();
   }

   // $FF: synthetic method
   ClusterMember(Object var1) {
      this();
   }

   static {
      $assertionsDisabled = !ClusterMember.class.desiredAssertionStatus();
      debugClusterMember = Debug.getCategory("weblogic.cluster.leasing.ClusterMember");
      DEBUG = debugEnabled();
   }

   private static final class Factory {
      static final ClusterMember THE_ONE = new ClusterMember();
   }
}
