package weblogic.cluster.leasing.databaseless;

import java.security.AccessController;
import weblogic.cluster.ClusterService;
import weblogic.cluster.messaging.internal.ClusterMessageFactory;
import weblogic.cluster.messaging.internal.ClusterMessageProcessingException;
import weblogic.cluster.messaging.internal.ClusterMessageSender;
import weblogic.cluster.messaging.internal.ClusterResponse;
import weblogic.cluster.messaging.internal.DebugLogger;
import weblogic.cluster.messaging.internal.SRMResult;
import weblogic.cluster.messaging.internal.ServerInformation;
import weblogic.cluster.messaging.internal.ServerInformationImpl;
import weblogic.health.HealthMonitorService;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.ServerLifecycleException;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManagerFactory;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;

public class ClusterFormationServiceImpl implements ClusterFormationService, TimerListener, DisconnectActionListener {
   private static final DebugCategory debugClusterFormation = Debug.getCategory("weblogic.cluster.leasing.ClusterFormation");
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final boolean DEBUG = debugEnabled();
   private static final int FORMATION_RETRY_PERIOD = 5000;
   private ServerInformation localInformation;
   private ClusterGroupView groupView;
   private LeaseView leaseView;
   private SRMResult result;

   public void start(ClusterGroupView var1, LeaseView var2) {
      if (!ClusterState.getInstance().setState("formation_leader")) {
         if (DEBUG) {
            debug("unable to transition from " + ClusterState.getInstance().getState() + " to " + "formation_leader");
         }

      } else {
         this.groupView = var1;
         this.leaseView = var2;
         this.localInformation = createLocalServerInformation();
         if (DEBUG) {
            debug("starting cluster formation with group view " + var1 + " and leaseView " + var2);
         }

         if (!this.ensureServerReachabilityMajority()) {
            if (DEBUG) {
               debug("SRM CHECK RETURNED FALSE ! cannot create the cluster");
            }

            this.OnLosingServerReachabilityMajority();
         } else {
            EnvironmentFactory.getClusterMemberDisconnectMonitor().start(this.groupView, this);

            try {
               if (!this.formClusterInternal()) {
                  TimerManagerFactory.getTimerManagerFactory().getDefaultTimerManager().schedule(this, 5000L, 5000L);
               }
            } catch (IllegalStateException var4) {
               this.formationFailed(var4);
            }

         }
      }
   }

   private boolean formClusterInternal() {
      ServerInformation[] var1 = this.groupView.getRemoteMembers(this.localInformation);
      if (var1.length == 0) {
         if (DEBUG) {
            debug("there are no other running servers. group formation is complete");
         }

         this.consolidateLeaseView((ClusterResponse[])null);
         this.leaderInitialization();
         return true;
      } else {
         try {
            ClusterResponse[] var2 = this.sendFormationMessage(var1);
            if (DEBUG) {
               debug("received responses to the formation message from all servers. checking responses ...");
            }

            if (this.isFormationRejectionPresent(var2)) {
               if (DEBUG) {
                  debug("formation message rejected !!");
               }

               this.handleFormationRejection(var2);
               return false;
            } else {
               this.consolidateLeaseView(var2);
               this.becomeLeader(var1);
               return true;
            }
         } catch (ClusterMessageProcessingException var3) {
            if (DEBUG) {
               debug("formation message rejected !!");
               var3.printStackTrace();
            }

            this.handleFormationRejection(var3.getResponses());
            return false;
         }
      }
   }

   private void consolidateLeaseView(ClusterResponse[] var1) {
      if (this.leaseView != null) {
         if (var1 != null) {
            for(int var2 = 0; var2 < var1.length; ++var2) {
               ClusterFormationResponse var3 = (ClusterFormationResponse)var1[var2];
               this.leaseView.merge(var3.getLeaseView());
            }
         }

         this.leaseView.prepareToBecomeLeader();
         this.leaseView = new LeaseView(this.localInformation.getServerName(), this.leaseView.getLeaseTableReplica(), this.leaseView.getVersionNumber());
      }
   }

   private ClusterResponse[] sendFormationMessage(ServerInformation[] var1) throws ClusterMessageProcessingException {
      ClusterMessageSender var2 = ClusterMessageFactory.getInstance().getDefaultMessageSender();
      ClusterGroupView var3 = new ClusterGroupView(this.localInformation, var1);
      ClusterFormationMessage var4 = new ClusterFormationMessage(var3);
      if (DEBUG) {
         debug("sending " + var4 + " to " + var1.length + " running servers");
      }

      return var2.send(var4, (ServerInformation[])var1);
   }

   private boolean isFormationRejectionPresent(ClusterResponse[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         ClusterFormationResponse var3 = (ClusterFormationResponse)var1[var2];
         if (var3 == null || !var3.isAccepted()) {
            return true;
         }
      }

      return false;
   }

   private void handleFormationRejection(ClusterResponse[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         ClusterFormationResponse var3 = (ClusterFormationResponse)var1[var2];
         if (var3 != null && !var3.isAccepted()) {
            if (DEBUG) {
               debug("got a rejection formation response " + var3);
            }

            if (var3.getLeaderInformation() != null) {
               if (this.isServerAlive(var3.getLeaderInformation().getServerName())) {
                  if (DEBUG) {
                     debug("rejection was caused by the presence of a leader.restarting the server ...");
                  }

                  this.forceShutdown();
               }
            } else {
               if (DEBUG) {
                  debug("rejection was caused by a senior server which is not yet the leader. restarting the server ...");
               }

               this.forceShutdown();
            }
         }
      }

   }

   private boolean isServerAlive(String var1) {
      if (this.result == null) {
         return true;
      } else {
         String var2 = this.result.getServerState(var1);
         if (var2 == null) {
            return false;
         } else {
            return var2.equals("RUNNING") || var2.equals("ADMIN");
         }
      }
   }

   private void forceShutdown() {
      try {
         ManagementService.getRuntimeAccess(kernelId).getServerRuntime().forceShutdown();
         throw new IllegalStateException("Server failed formation due to the presence of another leader in the cluster!");
      } catch (ServerLifecycleException var2) {
         System.exit(1);
      }
   }

   private void becomeLeader(ServerInformation[] var1) {
      ClusterMessageSender var2 = ClusterMessageFactory.getInstance().getDefaultMessageSender();
      JoinResponseMessage var3 = JoinResponseMessage.getAcceptedResponse(this.groupView, this.leaseView);
      if (DEBUG) {
         debug("sending join responses to running members with the leadership information");
      }

      try {
         var2.send(var3, (ServerInformation[])var1);
      } catch (ClusterMessageProcessingException var5) {
         if (DEBUG) {
            debug("got exception while sending join responses. reason:" + var5);
         }
      }

      if (DEBUG) {
         debug("starting cluster leader locally after sending join responses");
      }

      this.leaderInitialization();
      this.groupView = null;
   }

   private boolean ensureServerReachabilityMajority() {
      String var1 = ManagementService.getRuntimeAccess(kernelId).getServer().getCluster().getName();
      this.result = EnvironmentFactory.getServerReachabilityMajorityService().performSRMCheck((ServerInformation)null, var1);
      return this.result.hasReachabilityMajority();
   }

   private void leaderInitialization() {
      if (!ClusterState.getInstance().setState("stable_leader")) {
         throw new AssertionError(ClusterState.getInstance().getErrorMessage("stable_leader"));
      } else {
         if (DEBUG) {
            debug("starting the ClusterLeader on local server");
         }

         this.stop();
         EnvironmentFactory.getClusterLeader().start(this.groupView, this.leaseView);
      }
   }

   private void stop() {
      EnvironmentFactory.getClusterMemberDisconnectMonitor().stop();
   }

   private static ServerInformation createLocalServerInformation() {
      return new ServerInformationImpl(ClusterService.getClusterService().getLocalMember());
   }

   public void timerExpired(Timer var1) {
      try {
         if (this.formClusterInternal()) {
            var1.cancel();
         }
      } catch (IllegalStateException var3) {
         this.formationFailed(var3);
         var1.cancel();
      }

   }

   private void formationFailed(IllegalStateException var1) {
      ClusterState.getInstance().setState("discovery");
      if (DEBUG) {
         debug("Server cannot form cluster due to " + var1.getMessage());
         var1.printStackTrace();
      }

   }

   public void OnBecomingSeniorMostMember() {
   }

   public void OnLosingServerReachabilityMajority() {
      String var1 = "Server is not in the majority cluster partition";
      ClusterState.getInstance().setState("failed", var1);
      this.stop();
      HealthMonitorService.subsystemFailed("DatabaseLessLeasing", var1);
   }

   public void onLosingLeader() {
      throw new AssertionError("onLosingLeader() invoked on the ClusterFormationService");
   }

   public void onLosingMember(ServerInformation var1) {
      this.groupView.removeMember(var1);
   }

   public static ClusterFormationService getInstance() {
      return ClusterFormationServiceImpl.Factory.THE_ONE;
   }

   private static void debug(String var0) {
      DebugLogger.debug("[ClusterFormationService] " + var0);
   }

   private static boolean debugEnabled() {
      return debugClusterFormation.isEnabled() || DebugLogger.isDebugEnabled();
   }

   private static final class Factory {
      static final ClusterFormationServiceImpl THE_ONE = new ClusterFormationServiceImpl();
   }
}
