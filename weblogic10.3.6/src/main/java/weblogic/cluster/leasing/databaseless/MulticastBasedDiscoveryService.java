package weblogic.cluster.leasing.databaseless;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;
import weblogic.cluster.ClusterMemberInfo;
import weblogic.cluster.ClusterService;
import weblogic.cluster.messaging.internal.DebugLogger;
import weblogic.cluster.messaging.internal.ServerInformation;
import weblogic.cluster.messaging.internal.ServerInformationImpl;
import weblogic.health.HealthMonitorService;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManagerFactory;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;

final class MulticastBasedDiscoveryService implements DiscoveryService, TimerListener, DisconnectActionListener {
   private static final DebugCategory debugDiscoveryService = Debug.getCategory("weblogic.cluster.leasing.DiscoveryService");
   private static final boolean DEBUG = debugEnabled();
   private ServerInformation localInformation;
   private boolean stopped;
   private ClusterGroupView groupView;

   public static MulticastBasedDiscoveryService getInstance() {
      return MulticastBasedDiscoveryService.Factory.THE_ONE;
   }

   public void start(int var1) {
      if (!ClusterState.getInstance().setState("discovery")) {
         if (DEBUG) {
            debug("unable to transition from " + ClusterState.getInstance().getState() + " to " + "discovery");
         }

      } else {
         this.localInformation = createLocalServerInformation();
         if (DEBUG) {
            debug("starting discovery timer that will expire in " + var1 + " seconds");
         }

         TimerManagerFactory.getTimerManagerFactory().getDefaultTimerManager().schedule(this, (long)(var1 * 1000));
      }
   }

   private static ServerInformation createLocalServerInformation() {
      return new ServerInformationImpl(ClusterService.getClusterService().getLocalMember());
   }

   private ClusterGroupView discoveredGroupView() {
      Collection var1 = ClusterService.getClusterService().getAllRemoteMembers();
      if (DEBUG) {
         debug("ClusterService has " + var1.size() + " members");
      }

      if (var1.size() == 0) {
         if (DEBUG) {
            debug("this is the only running cluster member !");
         }

         return new ClusterGroupView(this.localInformation, (ServerInformation[])null);
      } else {
         Iterator var2 = var1.iterator();
         TreeSet var3 = new TreeSet();
         var3.add(this.localInformation);

         ServerInformationImpl var5;
         for(; var2.hasNext(); var3.add(var5)) {
            ClusterMemberInfo var4 = (ClusterMemberInfo)var2.next();
            var5 = new ServerInformationImpl(var4);
            if (DEBUG) {
               debug("discovered " + var5);
            }
         }

         return new ClusterGroupView(var3);
      }
   }

   private static void debug(String var0) {
      DebugLogger.debug("[DiscoveryService] " + var0);
   }

   public void timerExpired(Timer var1) {
      String var2 = ClusterState.getInstance().getState().intern();
      if (DEBUG) {
         debug("discovery timer detected state " + var2);
      }

      if (var2 != "discovery") {
         if (DEBUG) {
            debug("discovery timer quit as state is " + var2);
         }

      } else {
         this.groupView = this.discoveredGroupView();
         if (DEBUG) {
            debug("discovered group view " + this.groupView);
         }

         if (this.groupView.isSeniorMost(this.localInformation)) {
            if (DEBUG) {
               debug("we are the seniormost member. try forming the cluster");
            }

            EnvironmentFactory.getClusterFormationService().start(this.groupView, (LeaseView)null);
         } else {
            this.registerForDisconnects();
         }

      }
   }

   private synchronized void registerForDisconnects() {
      if (!this.stopped) {
         if (DEBUG) {
            debug("we are not the senior most. registering for disconnects");
         }

         EnvironmentFactory.getClusterMemberDisconnectMonitor().start(this.groupView, this);
      }
   }

   public synchronized void stop() {
      if (DEBUG) {
         debug("stopped !");
      }

      if (!this.stopped) {
         this.stopped = true;
         EnvironmentFactory.getClusterMemberDisconnectMonitor().stop();
      }
   }

   public void OnBecomingSeniorMostMember() {
      if (DEBUG) {
         debug("DisconnectMonitor notified that we are seniormost! trying to form the cluster");
      }

      if (this.groupView.isSeniorMost(this.localInformation)) {
         if (DEBUG) {
            debug("we are the seniormost member. try forming the cluster");
         }

         EnvironmentFactory.getClusterMemberDisconnectMonitor().stop();
         EnvironmentFactory.getClusterFormationService().start(this.groupView, (LeaseView)null);
      }

   }

   public synchronized void OnLosingServerReachabilityMajority() {
      if (DEBUG) {
         debug("OnLosingServerReachabilityMajority() called ! marking the server as failed");
      }

      String var1 = "Server is not in the majority cluster partition";
      ClusterState.getInstance().setState("failed", var1);
      this.stop();
      HealthMonitorService.subsystemFailed("DatabaseLessLeasing", var1);
   }

   public synchronized void onLosingLeader() {
      this.groupView.removeLeader();
   }

   public synchronized void onLosingMember(ServerInformation var1) {
      this.groupView.removeMember(var1);
   }

   private static boolean debugEnabled() {
      return debugDiscoveryService.isEnabled() || DebugLogger.isDebugEnabled();
   }

   private static final class Factory {
      static final MulticastBasedDiscoveryService THE_ONE = new MulticastBasedDiscoveryService();
   }
}
