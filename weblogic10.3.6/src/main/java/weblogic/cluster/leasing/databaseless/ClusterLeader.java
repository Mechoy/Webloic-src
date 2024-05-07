package weblogic.cluster.leasing.databaseless;

import java.rmi.RemoteException;
import java.security.AccessController;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import weblogic.cluster.ClusterMemberInfo;
import weblogic.cluster.ClusterMembersChangeEvent;
import weblogic.cluster.ClusterMembersChangeListener;
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
import weblogic.management.configuration.DatabaseLessLeasingBasisMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManagerFactory;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;
import weblogic.work.WorkManagerFactory;

public final class ClusterLeader implements ClusterMembersChangeListener, ClusterMessageReceiver, DisconnectActionListener {
   private static final DebugCategory debugClusterLeader = Debug.getCategory("weblogic.cluster.leasing.ClusterLeader");
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final boolean DEBUG = debugEnabled();
   private LeaseServer leaseServer;
   private ClusterGroupView groupView;
   private LeaseView leaseView;
   private Timer heartbeatTimer;
   private boolean stopped;

   synchronized void start(ClusterGroupView var1, LeaseView var2) {
      this.groupView = var1;
      if (var2 == null) {
         this.leaseView = new LeaseView(var1.getLeaderInformation().getServerName(), (HashMap)null);
      } else {
         this.leaseView = var2;
      }

      ClusterService.getClusterService().addClusterMembersListener(this);
      if (DEBUG) {
         debug("installed ClusterLeader for giving leases and managing cluster view. leader information " + var1.getLeaderInformation());
      }

      this.leaseServer = new LeaseServer(this, this.leaseView);
      ClusterMessageFactory.getInstance().registerReceiver(this);
      ((DatabaseLessLeasingService)DatabaseLessLeasingService.getInstance()).localServerIsClusterLeader();
      EnvironmentFactory.getClusterMemberDisconnectMonitor().start(this.groupView, this);
      this.joinMembersInWaiting();
      if (ClusterState.getInstance().getState() != "failed") {
         this.startClusterHeartBeats();
      }

   }

   private void startClusterHeartBeats() {
      if (!this.stopped) {
         HeartbeatTimer var1 = new HeartbeatTimer();
         DatabaseLessLeasingBasisMBean var2 = ManagementService.getRuntimeAccess(kernelId).getServer().getCluster().getDatabaseLessLeasingBasis();
         this.heartbeatTimer = TimerManagerFactory.getTimerManagerFactory().getDefaultTimerManager().schedule(var1, (long)(var2.getLeaderHeartbeatPeriod() * 1000), (long)(var2.getLeaderHeartbeatPeriod() * 1000));
      }
   }

   private void joinMembersInWaiting() {
      if (!this.stopped) {
         Collection var1 = ClusterService.getClusterService().getAllRemoteMembers();
         Set var2 = this.groupView.getMembers();
         if (!var1.isEmpty()) {
            Iterator var3 = var1.iterator();

            while(var3.hasNext()) {
               ClusterMemberInfo var4 = (ClusterMemberInfo)var3.next();
               ServerInformationImpl var5 = new ServerInformationImpl(var4);
               if (!var2.contains(var5)) {
                  this.joinServer(var5);
                  if (ClusterState.getInstance().getState() == "failed") {
                     break;
                  }
               }
            }

         }
      }
   }

   synchronized ServerInformation getLeaderInformation() {
      if (this.stopped) {
         return null;
      } else {
         return this.groupView != null ? this.groupView.getLeaderInformation() : null;
      }
   }

   public synchronized void clusterMembersChanged(ClusterMembersChangeEvent var1) {
      int var2 = var1.getAction();
      if (var2 == 0 || var2 == 3) {
         ServerInformationImpl var3 = new ServerInformationImpl(var1.getClusterMemberInfo());
         this.joinServer(var3);
      }

   }

   private synchronized void joinServer(ServerInformation var1) {
      if (!this.stopped && !this.groupView.getMembers().contains(var1)) {
         ClusterMessageSender var2 = ClusterMessageFactory.getInstance().getDefaultMessageSender();
         ServerInformation var4 = this.groupView.getLeaderInformation();
         boolean var5 = false;
         JoinResponseMessage var3;
         if (var4 != null && var4.compareTo(var1) < 0) {
            if (DEBUG) {
               debug("sending a join response message to " + var1);
            }

            this.groupView.addMember(var1);
            var3 = JoinResponseMessage.getAcceptedResponse(this.groupView, this.leaseView);
            var5 = true;
         } else {
            if (DEBUG) {
               debug("sending a join rejection message to " + var1);
            }

            var3 = JoinResponseMessage.getRejectedResponse(this.groupView.getLeaderInformation());
         }

         try {
            var2.send(var3, (String)var1.getServerName());
         } catch (LeaderAlreadyExistsException var8) {
            if (DEBUG) {
               debug(var1.getServerName() + " knows about the presence of another live cluster leader. " + "Marking this server as failed and stopping the cluster leader");
            }

            String var7 = " knows about the presence of another live cluster leader. Marking this server as failed and stopping the cluster leader ";
            ClusterState.getInstance().setState("failed", var7);
            this.handleExistenceofAnotherLeader(var1, var7);
            var5 = false;
         } catch (RemoteException var9) {
            this.groupView.removeMember(var1);
            var5 = false;
         }

         if (var5) {
            GroupViewUpdateMessage var6 = GroupViewUpdateMessage.createMemberAdded(this.getLeaderInformation(), var1, this.groupView.incrementVersionNumber());
            this.sendGroupMessage(var6);
            ((DatabaseLessLeasingService)DatabaseLessLeasingService.getInstance()).fireConsensusServiceGroupViewListenerEvent(var1, true);
         }

      }
   }

   private void handleExistenceofAnotherLeader(final ServerInformation var1, final String var2) {
      WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
         public void run() {
            synchronized(EnvironmentFactory.getClusterMemberDisconnectMonitor()) {
               ClusterLeader.this.stop();
               HealthMonitorService.subsystemFailedForceShutdown("Consensus-Leasing", var1.getServerName() + var2);
            }
         }
      });
   }

   public synchronized boolean sendGroupMessage(ClusterMessage var1) {
      if (this.stopped) {
         return false;
      } else {
         ServerInformation[] var2 = this.groupView.getRemoteMembers(this.getLeaderInformation());
         if (var2 != null && var2.length != 0) {
            ClusterMessageSender var3 = ClusterMessageFactory.getInstance().getDefaultMessageSender();
            if (DEBUG) {
               debug("sending " + var1 + " to " + var2.length + " running servers");
            }

            try {
               var3.send(var1, var2);
               return true;
            } catch (ClusterMessageProcessingException var5) {
               return false;
            }
         } else {
            return true;
         }
      }
   }

   public synchronized void stop() {
      this.stopped = true;
      this.groupView = null;
      EnvironmentFactory.getClusterMemberDisconnectMonitor().stop();
      if (this.heartbeatTimer != null) {
         this.heartbeatTimer.cancel();
      }

   }

   public void OnBecomingSeniorMostMember() {
   }

   public void OnLosingServerReachabilityMajority() {
      if (DEBUG) {
         debug("OnLosingServerReachabilityMajority() called ! marking the server as failed and stopping the cluster leader");
      }

      String var1 = "Server is not in the majority cluster partition";
      ClusterState.getInstance().setState("failed", var1);
      this.stop();
      HealthMonitorService.subsystemFailed("DatabaseLessLeasing", var1);
      ((DatabaseLessLeasingService)DatabaseLessLeasingService.getInstance()).localServerLostClusterLeadership();
   }

   public void onLosingLeader() {
      if (DEBUG) {
         debug("onLosingLeader() called on the leader itself ! marking the server as failed and stopping the cluster leader");
      }

      String var1 = "NodeManager associated with the local server is unreachable !";
      ClusterState.getInstance().setState("failed", var1);
      this.stop();
      HealthMonitorService.subsystemFailed("DatabaseLessLeasing", var1);
      ((DatabaseLessLeasingService)DatabaseLessLeasingService.getInstance()).localServerLostClusterLeadership();
   }

   public synchronized void onLosingMember(ServerInformation var1) {
      if (!this.stopped) {
         this.groupView.removeMember(var1);
         GroupViewUpdateMessage var2 = GroupViewUpdateMessage.createMemberRemoved(this.getLeaderInformation(), var1, this.groupView.incrementVersionNumber());
         this.sendGroupMessage(var2);
         ((DatabaseLessLeasingService)DatabaseLessLeasingService.getInstance()).fireConsensusServiceGroupViewListenerEvent(var1, false);
      }
   }

   public static ClusterLeader getInstance() {
      return ClusterLeader.Factory.THE_ONE;
   }

   public boolean accept(ClusterMessage var1) {
      return var1.getMessageType() == 4 || var1.getMessageType() == 2 || var1.getMessageType() == 8;
   }

   public synchronized ClusterResponse process(ClusterMessage var1) throws ClusterMessageProcessingException {
      if (this.stopped) {
         throw new ClusterMessageProcessingException("ClusterLeader is not running as it is not in the majority cluster partition");
      } else if (var1.getMessageType() == 4) {
         return this.leaseServer.process((LeaseMessage)var1);
      } else if (var1.getMessageType() == 2) {
         this.handleJoinRequest((JoinRequestMessage)var1);
         return null;
      } else if (var1.getMessageType() == 8) {
         if (DEBUG) {
            debug("sending a state dump response with group version " + this.groupView.getVersionNumber() + " and lease version " + this.leaseView.getVersionNumber());
         }

         return new StateDumpResponse(this.groupView, this.leaseView);
      } else {
         throw new AssertionError("Unknown message received by leader " + var1);
      }
   }

   private void handleJoinRequest(final JoinRequestMessage var1) {
      WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
         public void run() {
            ClusterLeader.this.joinServer(var1.getSenderInformation());
         }
      });
   }

   private static boolean debugEnabled() {
      return debugClusterLeader.isEnabled() || DebugLogger.isDebugEnabled();
   }

   private static void debug(String var0) {
      DebugLogger.debug("[ClusterLeader] " + var0);
   }

   private class HeartbeatTimer implements TimerListener {
      private HeartbeatTimer() {
      }

      public void timerExpired(Timer var1) {
         synchronized(ClusterLeader.this) {
            if (ClusterLeader.this.stopped) {
               var1.cancel();
            } else {
               HashSet var3 = new HashSet();
               ServerInformation[] var4 = this.getDiscoveredMembers();
               if (var4 != null) {
                  var3 = new HashSet(Arrays.asList(var4));
               }

               ServerInformation[] var5 = ClusterLeader.this.groupView.getRemoteMembers(ClusterLeader.this.groupView.getLeaderInformation());
               if (var5 != null) {
                  var3.addAll(Arrays.asList(var5));
               }

               if (var3.size() != 0) {
                  ServerInformation[] var6 = new ServerInformation[var3.size()];
                  var6 = (ServerInformation[])((ServerInformation[])var3.toArray(var6));
                  ClusterLeaderHeartbeatMessage var7 = ClusterLeaderHeartbeatMessage.create(ClusterLeader.this.groupView, ClusterLeader.this.leaseView);
                  ClusterMessageSender var8 = ClusterMessageFactory.getInstance().getOneWayMessageSender();
                  if (ClusterLeader.DEBUG) {
                     ClusterLeader.debug("sending " + var7 + " to " + var6.length + " running servers");
                  }

                  try {
                     var8.send(var7, (ServerInformation[])var6);
                  } catch (ClusterMessageProcessingException var11) {
                  }

               }
            }
         }
      }

      private ServerInformation[] getDiscoveredMembers() {
         Collection var1 = ClusterService.getClusterService().getAllRemoteMembers();
         if (var1.isEmpty()) {
            return null;
         } else {
            ServerInformation[] var2 = new ServerInformation[var1.size()];
            Iterator var3 = var1.iterator();

            ClusterMemberInfo var5;
            for(int var4 = 0; var3.hasNext(); var2[var4++] = new ServerInformationImpl(var5)) {
               var5 = (ClusterMemberInfo)var3.next();
            }

            return var2;
         }
      }

      // $FF: synthetic method
      HeartbeatTimer(Object var2) {
         this();
      }
   }

   private static final class Factory {
      static final ClusterLeader THE_ONE = new ClusterLeader();
   }
}
