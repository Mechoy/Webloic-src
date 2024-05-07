package weblogic.cluster.leasing.databaseless;

import java.security.AccessController;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import weblogic.cluster.messaging.internal.DebugLogger;
import weblogic.cluster.messaging.internal.SRMResult;
import weblogic.cluster.messaging.internal.ServerInformation;
import weblogic.management.configuration.DatabaseLessLeasingBasisMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManagerFactory;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;

public class RMIBasedDisconnectMonitorImpl implements ClusterMemberDisconnectMonitor, ServerFailureListener, GroupViewListener, TimerListener {
   private static final DebugCategory debugDisconnectMonitor;
   private static final boolean DEBUG;
   private static final AuthenticatedSubject kernelId;
   private ClusterGroupView groupView;
   private DisconnectActionListener listener;
   private ServerInformation localInformation;
   private Map detectorMap = new HashMap();
   private ServerInformation leaderInfo;
   private boolean isLeader;
   private Timer srmCheckTimer;
   private Set deadServers;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public static ClusterMemberDisconnectMonitor getInstance() {
      return RMIBasedDisconnectMonitorImpl.Factory.THE_ONE;
   }

   public synchronized void start(ClusterGroupView var1, DisconnectActionListener var2) {
      if (DEBUG) {
         debug("inside start()");
      }

      if ($assertionsDisabled || this.listener == null && this.groupView == null) {
         this.groupView = var1;
         this.listener = var2;
         this.deadServers = new HashSet();
         this.leaderInfo = var1.getLeaderInformation();
         if (!$assertionsDisabled && this.leaderInfo == null) {
            throw new AssertionError();
         } else {
            this.localInformation = ClusterLeaderService.getInstance().getLocalServerInformation();
            if (this.leaderInfo.equals(this.localInformation)) {
               this.isLeader = true;
               ServerInformation[] var3 = var1.getRemoteMembers(this.localInformation);
               if (DEBUG) {
                  debug("we are the leader ! create server failure detectors for all members in the group view");
               }

               int var4;
               for(var4 = 0; var4 < var3.length; ++var4) {
                  this.createFailureDetector(var3[var4]);
               }

               var4 = getLeaderSRMCheckPeriod();
               this.srmCheckTimer = TimerManagerFactory.getTimerManagerFactory().getDefaultTimerManager().schedule(this, 0L, (long)var4);
            } else {
               if (DEBUG) {
                  debug("creating server failure detector for " + this.leaderInfo);
               }

               this.createFailureDetector(this.leaderInfo);
               this.srmCheckTimer = TimerManagerFactory.getTimerManagerFactory().getDefaultTimerManager().schedule(this, 0L);
            }

            this.groupView.setGroupViewListener(this);
         }
      } else {
         throw new AssertionError();
      }
   }

   private static int getLeaderSRMCheckPeriod() {
      DatabaseLessLeasingBasisMBean var0 = ManagementService.getRuntimeAccess(kernelId).getServer().getCluster().getDatabaseLessLeasingBasis();
      if (!var0.isPeriodicSRMCheckEnabled()) {
         if (DEBUG) {
            debug("periodic SRM check is disabled !");
         }

         return 0;
      } else {
         if (DEBUG) {
            debug("periodic SRM check is enabled !");
         }

         int var1 = var0.getMemberDiscoveryTimeout();
         return var1 * 1000 / 2;
      }
   }

   public synchronized void stop() {
      if (this.srmCheckTimer != null) {
         this.srmCheckTimer.cancel();
      }

      Iterator var1 = this.detectorMap.values().iterator();

      while(var1.hasNext()) {
         ((ServerFailureDetector)var1.next()).stop();
      }

      this.listener = null;
      if (this.groupView != null) {
         this.groupView.setGroupViewListener((GroupViewListener)null);
      }

      this.groupView = null;
      this.deadServers = null;
   }

   public synchronized void memberAdded(ServerInformation var1) {
      if (this.isLeader) {
         this.createFailureDetector(var1);
      }

   }

   public void memberRemoved(ServerInformation var1) {
      if (this.detectorMap.get(var1) == null) {
         this.onServerFailure(var1);
      }

   }

   public void onServerFailure(ServerFailureEvent var1) {
      this.onServerFailure(var1.getServerInformation());
   }

   public synchronized void onMachineFailure(MachineFailureEvent var1) {
      if (this.listener != null) {
         List var2 = var1.getFailedServers();
         if (var2 != null) {
            Iterator var3 = var2.iterator();

            while(var3.hasNext()) {
               ServerInformation var4 = this.groupView.getServerInformation((String)var3.next());
               if (var4 != null && !this.deadServers.contains(var4)) {
                  this.deadServers.add(var4);
                  if (this.leaderInfo.equals(var4)) {
                     if (DEBUG) {
                        debug("MachineFailure: leader is really dead ! Invoking onLosingLeader()");
                     }

                     this.listener.onLosingLeader();
                  } else {
                     if (DEBUG) {
                        debug("MachineFailure: " + var4.getServerName() + " is dead ! Invoking onLosingMember()");
                     }

                     this.listener.onLosingMember(var4);
                  }
               }
            }

            this.reachabilityCheckHelper(var1.getMachineName());
         }
      }
   }

   private synchronized void onServerFailure(ServerInformation var1) {
      if (this.listener != null && !this.deadServers.contains(var1)) {
         this.deadServers.add(var1);
         if (this.leaderInfo.equals(var1)) {
            if (DEBUG) {
               debug("leader is really dead ! Invoking onLosingLeader()");
            }

            this.listener.onLosingLeader();
         } else {
            if (DEBUG) {
               debug(var1.getServerName() + " is dead ! Invoking onLosingMember()");
            }

            this.listener.onLosingMember(var1);
         }

         this.reachabilityCheckHelper((String)null);
      }
   }

   private void reachabilityCheckHelper(String var1) {
      SRMResult var2 = this.getSRMResult(var1, true);
      if (!var2.hasReachabilityMajority()) {
         if (DEBUG) {
            debug("a member is dead but this server does not have reachability majority. OnLosingServerReachabilityMajority()");
         }

         this.listener.OnLosingServerReachabilityMajority();
      } else if (this.groupView.isSeniorMost(this.localInformation)) {
         if (DEBUG) {
            debug("local server is the seniormost member !");
         }

         this.listener.OnBecomingSeniorMostMember();
      } else {
         ServerInformation var3 = this.groupView.getSeniorMost();
         if (this.detectorMap.get(var3) == null) {
            this.createFailureDetector(var3);
         }

      }
   }

   private void createFailureDetector(ServerInformation var1) {
      ServerFailureDetector var2 = EnvironmentFactory.getFailureDetector(var1.getServerName());
      var2.start(var1, this);
      this.detectorMap.put(var1, var2);
   }

   private SRMResult getSRMResult(String var1, boolean var2) {
      String var3 = ManagementService.getRuntimeAccess(kernelId).getServer().getCluster().getName();
      return EnvironmentFactory.getServerReachabilityMajorityService().performSRMCheck(this.leaderInfo, var3, var1, var2);
   }

   public void timerExpired(Timer var1) {
      if (this.listener != null) {
         SRMResult var2 = this.getSRMResult((String)null, false);
         if (!var2.hasReachabilityMajority()) {
            if (DEBUG) {
               debug("SRM timer determined that the leader does not have reachability majority. OnLosingServerReachabilityMajority()");
            }

            synchronized(this) {
               if (this.listener != null) {
                  this.listener.OnLosingServerReachabilityMajority();
               }
            }
         }
      }
   }

   private static void debug(String var0) {
      DebugLogger.debug("[RMIDisconnectMonitor] " + var0);
   }

   private static boolean debugEnabled() {
      return debugDisconnectMonitor.isEnabled() || DebugLogger.isDebugEnabled();
   }

   static {
      $assertionsDisabled = !RMIBasedDisconnectMonitorImpl.class.desiredAssertionStatus();
      debugDisconnectMonitor = Debug.getCategory("weblogic.cluster.leasing.DisconnectMonitor");
      DEBUG = debugEnabled();
      kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   }

   private static final class Factory {
      static final RMIBasedDisconnectMonitorImpl THE_ONE = new RMIBasedDisconnectMonitorImpl();
   }
}
