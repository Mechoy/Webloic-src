package weblogic.cluster.messaging.internal;

import java.rmi.RemoteException;
import java.security.AccessController;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import weblogic.cluster.ClusterMemberInfo;
import weblogic.cluster.ClusterMembersChangeEvent;
import weblogic.cluster.ClusterMembersChangeListener;
import weblogic.cluster.ClusterService;
import weblogic.cluster.singleton.LeaseManager;
import weblogic.cluster.singleton.MemberDeathDetector;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.rjvm.PeerGoneEvent;
import weblogic.rjvm.PeerGoneListener;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManagerFactory;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;
import weblogic.utils.StackTraceUtils;
import weblogic.utils.collections.ConcurrentHashSet;
import weblogic.work.WorkAdapter;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

public class MemberDeathDetectorImpl implements MemberDeathDetector, ClusterMembersChangeListener, PeerGoneListener, MessageDeliveryFailureListener {
   private static Map members = Collections.synchronizedMap(new LinkedHashMap());
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final DebugCategory debugDisconnectMonitor = Debug.getCategory("weblogic.cluster.leasing.DisconnectMonitor");
   private static final boolean DEBUG = debugEnabled();
   private static final String WLS_SERVER_LEASE_TYPE = "wlsserver";
   private static long SUSPECT_TIMEOUT_INTERVAL = 300000L;
   private static long SUSPECTED_MEMBER_MONITOR_INTERVAL = 60000L;
   private SuspectedMemberInfo localServerInfo;
   private LeaseManager servicesLeaseManager;
   private LeaseManager serverLeaseManager;
   private Timer heartbeatTimerManager;
   private HeartbeatTimer heartbeatTimer;
   private static final Map suspectedMembers = Collections.synchronizedMap(new LinkedHashMap());
   private boolean started;
   protected WorkManager workManager;
   private static final ClusterMessage HEARTBEAT_REQUEST = new BaseClusterMessage(createLocalServerInformation(), 9);
   private static int HEARTBEAT_INTERVAL;
   private static final ConcurrentHashSet pendingProbes = new ConcurrentHashSet();
   private Timer suspectedMemberTimer;

   public static MemberDeathDetectorImpl getInstance() {
      return MemberDeathDetectorImpl.SingletonMaker.THE_ONE;
   }

   private MemberDeathDetectorImpl() {
      this.started = false;
      this.localServerInfo = new SuspectedMemberInfoImpl(ClusterService.getClusterService().getLocalMember());
      this.servicesLeaseManager = ClusterService.getClusterService().getDefaultLeaseManager("service");
      this.serverLeaseManager = ClusterService.getClusterService().getDefaultLeaseManager("wlsserver");
      this.workManager = WorkManagerFactory.getInstance().getDefault();
      ClusterMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServer().getCluster();
      HEARTBEAT_INTERVAL = var1.getDeathDetectorHeartbeatPeriod() * 1000;
   }

   private String getLocalServerName() {
      ServerMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServer();
      return var1.getName();
   }

   public void start() {
      Collection var1 = ClusterService.getClusterService().getRemoteMembers();
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         ClusterMemberInfo var3 = (ClusterMemberInfo)var2.next();
         members.put(var3.serverName(), new SuspectedMemberInfoImpl(var3));
      }

      if (DEBUG) {
         debug(" initial set of members: " + members);
      }

      ClusterMessageFactory.getInstance().registerMessageDeliveryFailureListener(this);
      ClusterService.getClusterService().addClusterMembersListener(this);
      this.heartbeatTimer = new HeartbeatTimer();
      this.heartbeatTimerManager = TimerManagerFactory.getTimerManagerFactory().getDefaultTimerManager().schedule(this.heartbeatTimer, 0L, (long)HEARTBEAT_INTERVAL);
      this.suspectedMemberTimer = TimerManagerFactory.getTimerManagerFactory().getDefaultTimerManager().schedule(new SuspectedMemberListMonitor(), SUSPECTED_MEMBER_MONITOR_INTERVAL, SUSPECTED_MEMBER_MONITOR_INTERVAL);
      this.started = true;
   }

   public void stop() {
      if (this.heartbeatTimerManager != null) {
         this.heartbeatTimerManager.cancel();
      }

      if (this.suspectedMemberTimer != null) {
         this.suspectedMemberTimer.cancel();
      }

      this.started = false;
      if (DEBUG) {
         debug("Halting Member Death Detector");
      }

   }

   public void clusterMembersChanged(ClusterMembersChangeEvent var1) {
      String var2 = var1.getClusterMemberInfo().serverName();
      ServerMBean var3 = ManagementService.getRuntimeAccess(kernelId).getDomain().lookupServer(var2);
      switch (var1.getAction()) {
         case 0:
            this.removeSuspect(var2);
            members.put(var2, new SuspectedMemberInfoImpl(var1.getClusterMemberInfo()));
            if (DEBUG) {
               debug("MemberDeathDetectorImpl.clusterMembersChanged: Adding member: " + LeaseManager.getOwnerIdentity(var1.getClusterMemberInfo().identity()));
            }
            break;
         case 1:
            SuspectedMemberInfo var4 = (SuspectedMemberInfo)members.remove(var2);
            if (var4 != null && !suspectedMembers.containsKey(var4.getServerName())) {
               suspectedMembers.put(var4.getServerName(), var4);
               var4.setSuspectedStartTime(System.currentTimeMillis());
               if (DEBUG) {
                  debug("MemberDeathDetectorImpl.clusterMembersChanged: Suspecting member: " + LeaseManager.getOwnerIdentity(var4.getServerIdentity()));
               }
            }
            break;
         case 2:
            this.removeSuspect(var2);
            members.put(var2, new SuspectedMemberInfoImpl(var1.getClusterMemberInfo()));
            if (DEBUG) {
               debug("MemberDeathDetectorImpl.clusterMembersChanged: Update member: " + var1.getClusterMemberInfo().serverName());
            }
            break;
         case 3:
            if (DEBUG) {
               debug("MemberDeathDetectorImpl.clusterMembersChanged: Discover member: " + var1.getClusterMemberInfo().serverName());
            }

            return;
         default:
            if (DEBUG) {
               debug("MemberDeathDetectorImpl.clusterMembersChanged: Unknown ClusterMembersChangeEvent: " + var1.getAction() + " for members: " + var1.getClusterMemberInfo().serverName());
            }

            return;
      }

      if (DEBUG) {
         debug("MemberDeathDetectorImpl.clusterMembersChanged: members: " + members);
      }

   }

   public void peerGone(PeerGoneEvent var1) {
      if (DEBUG) {
         debug("MemberDeathDetectorImpl.peerGone event: " + var1);
      }

   }

   static boolean isServerMigratable(String var0, ServerMBean var1) {
      if (DEBUG) {
         debug("MemberDeathDetectorImpl.isServerDead serverstate for " + var1 + " is " + var0);
      }

      return var0 != null && (var0.equals("FAILED_NOT_RESTARTABLE") || var0.equals("FAILED_MIGRATABLE"));
   }

   private static ServerInformation createLocalServerInformation() {
      return new ServerInformationImpl(ClusterService.getClusterService().getLocalMember());
   }

   public void onMessageDeliveryFailure(String var1, RemoteException var2) {
      if (DEBUG) {
         debug("received onMessageDeliveryFailure for " + var1 + " due to " + StackTraceUtils.throwable2StackTrace(var2));
      }

      final SuspectedMemberInfo var3 = (SuspectedMemberInfo)suspectedMembers.get(var1);
      if (var3 == null) {
         if (DEBUG) {
            debug(" Suspected member: " + var1 + " not found! Was probably suspended or shutdown");
         }

      } else if (pendingProbes.contains(var3)) {
         if (DEBUG) {
            debug("There is already a probe pending for " + var3);
         }

      } else {
         pendingProbes.add(var3);
         WorkAdapter var4 = new WorkAdapter() {
            public void run() {
               ProbeContextImpl var1 = new ProbeContextImpl(var3);

               try {
                  ProbeManager.getClusterMasterProbeManager().invoke(var1);
               } finally {
                  MemberDeathDetectorImpl.pendingProbes.remove(var3);
               }

               if (MemberDeathDetectorImpl.DEBUG) {
                  MemberDeathDetectorImpl.debug("Probe of server: " + LeaseManager.getOwnerIdentity(var3.getServerIdentity()) + " returned result: " + var1.getResult());
               }

               if (var1.getResult() == 1) {
                  MemberDeathDetectorImpl.this.removeSuspect(var1.getSuspectedMemberInfo().getServerName());
               } else if (var1.getResult() != 0) {
                  var1 = new ProbeContextImpl(MemberDeathDetectorImpl.this.localServerInfo);

                  label156: {
                     try {
                        if (!MemberDeathDetectorImpl.pendingProbes.contains(MemberDeathDetectorImpl.this.localServerInfo)) {
                           MemberDeathDetectorImpl.pendingProbes.add(MemberDeathDetectorImpl.this.localServerInfo);
                           ProbeManager.getClusterMemberProbeManager().invoke(var1);
                           break label156;
                        }

                        if (MemberDeathDetectorImpl.DEBUG) {
                           MemberDeathDetectorImpl.debug("There is already a probe pending for " + MemberDeathDetectorImpl.this.localServerInfo);
                        }
                     } finally {
                        MemberDeathDetectorImpl.pendingProbes.remove(MemberDeathDetectorImpl.this.localServerInfo);
                     }

                     return;
                  }

                  if (MemberDeathDetectorImpl.DEBUG) {
                     MemberDeathDetectorImpl.debug("Probe of server: " + LeaseManager.getOwnerIdentity(MemberDeathDetectorImpl.this.localServerInfo.getServerIdentity()) + " returned result: " + var1.getResult());
                  }

                  if (var1.getResult() != 1 && var1.getResult() != 0) {
                     MemberDeathDetectorHeartbeatReceiver.fatalError(var1.getMessage());
                  } else {
                     MemberDeathDetectorImpl.this.voidMemberLeases(var3);
                  }
               }
            }

            public String toString() {
               return "Invoking probes for: " + LeaseManager.getOwnerIdentity(var3.getServerIdentity());
            }
         };
         this.workManager.schedule(var4);
      }
   }

   private void voidMemberLeases(SuspectedMemberInfo var1) {
      if (DEBUG) {
         debug("WorkAdapter removing suspected member: " + var1.getServerName());
      }

      this.removeSuspect(var1.getServerName());
      String var2 = LeaseManager.getOwnerIdentity(var1.getServerIdentity());
      if (DEBUG) {
         debug(" Voiding all its leases with ownerIdentity: " + var2);
      }

      if (!var1.hasVoidedSingletonServices()) {
         this.servicesLeaseManager.voidLeases(var2);
         var1.voidedSingletonServices();
      }

      this.serverLeaseManager.voidLeases(var2);
   }

   public String removeMember(String var1) {
      SuspectedMemberInfo var2 = (SuspectedMemberInfo)members.remove(var1);
      this.removeSuspect(var1);
      String var3 = var2 != null ? var2.getServerName() : null;
      return var3;
   }

   SuspectedMemberInfo removeSuspect(String var1) {
      SuspectedMemberInfo var2 = (SuspectedMemberInfo)suspectedMembers.remove(var1);
      if (var2 != null) {
         if (DEBUG) {
            debug("removeSuspect suspect: " + LeaseManager.getOwnerIdentity(var2.getServerIdentity()));
         }
      } else if (DEBUG) {
         debug("removeSuspect attempted to remove suspect: " + var1 + " but SuspectedMemberInfo not found");
      }

      return var2;
   }

   private static void debug(String var0) {
      System.out.println("[MemberDeathDetectorImpl] " + var0);
   }

   private static boolean debugEnabled() {
      return debugDisconnectMonitor.isEnabled();
   }

   public boolean isStarted() {
      return this.started;
   }

   // $FF: synthetic method
   MemberDeathDetectorImpl(Object var1) {
      this();
   }

   private class SuspectedMemberListMonitor implements TimerListener {
      private SuspectedMemberListMonitor() {
      }

      public void timerExpired(Timer var1) {
         HashSet var2 = new HashSet(MemberDeathDetectorImpl.suspectedMembers.values());
         Iterator var3 = var2.iterator();

         while(true) {
            SuspectedMemberInfo var4;
            do {
               if (!var3.hasNext()) {
                  return;
               }

               var4 = (SuspectedMemberInfo)var3.next();
            } while(System.currentTimeMillis() <= var4.getSuspectedStartTime() + MemberDeathDetectorImpl.SUSPECT_TIMEOUT_INTERVAL);

            try {
               if (MemberDeathDetectorImpl.DEBUG) {
                  MemberDeathDetectorImpl.debug(var4.getServerName() + " has been marked suspect for more than " + MemberDeathDetectorImpl.SUSPECT_TIMEOUT_INTERVAL / 60000L + " minutes. Removing it from suspect list.");
               }

               MemberDeathDetectorImpl.suspectedMembers.remove(var4);
            } catch (Exception var6) {
               if (MemberDeathDetectorImpl.DEBUG) {
                  var6.printStackTrace();
               }
            }
         }
      }

      // $FF: synthetic method
      SuspectedMemberListMonitor(Object var2) {
         this();
      }
   }

   private class HeartbeatTimer implements TimerListener {
      private final RMIClusterMessageSenderImpl messageSender = (RMIClusterMessageSenderImpl)ClusterMessageFactory.getInstance().getOneWayMessageSender();
      private final String localServerName = MemberDeathDetectorImpl.this.getLocalServerName();

      HeartbeatTimer() {
      }

      public void timerExpired(Timer var1) {
         HashSet var2 = new HashSet(MemberDeathDetectorImpl.members.values());
         var2.addAll(MemberDeathDetectorImpl.suspectedMembers.values());
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            SuspectedMemberInfo var4 = null;

            try {
               var4 = (SuspectedMemberInfo)var3.next();
               if (!this.localServerName.equals(var4.getServerName())) {
                  this.messageSender.send(MemberDeathDetectorImpl.HEARTBEAT_REQUEST, var4.getServerInformation().getServerName(), 1000);
               }
            } catch (RemoteException var6) {
               if (MemberDeathDetectorImpl.DEBUG) {
                  MemberDeathDetectorImpl.debug(var6.getMessage());
               }

               if (var4 != null) {
                  MemberDeathDetectorImpl.this.onMessageDeliveryFailure(var4.getServerInformation().getServerName(), var6);
               }
            }
         }

      }
   }

   private static final class SingletonMaker {
      static final MemberDeathDetectorImpl THE_ONE = new MemberDeathDetectorImpl();
   }
}
