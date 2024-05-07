package weblogic.cluster.messaging.internal;

import java.rmi.RemoteException;
import java.security.AccessController;
import java.util.Collection;
import java.util.Iterator;
import weblogic.cluster.ClusterMemberInfo;
import weblogic.cluster.ClusterService;
import weblogic.cluster.singleton.MemberDeathDetectorHeartbeatReceiverIntf;
import weblogic.health.HealthMonitorService;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManagerFactory;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;

public class MemberDeathDetectorHeartbeatReceiver implements MemberDeathDetectorHeartbeatReceiverIntf, PingMessageListener {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final DebugCategory debugDisconnectMonitor = Debug.getCategory("weblogic.cluster.leasing.DisconnectMonitor");
   private static final boolean DEBUG = debugEnabled();
   private long lastHeartbeatReceived;
   private SuspectedMemberInfo leader;
   long healthCheckInterval;
   boolean timerStarted;
   private Timer heartbeatMonitorTimer;

   public static MemberDeathDetectorHeartbeatReceiver getInstance() {
      return MemberDeathDetectorHeartbeatReceiver.SingletonMaker.THE_ONE;
   }

   private MemberDeathDetectorHeartbeatReceiver() {
      this.timerStarted = false;
      ServerMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServer();
      ClusterMBean var2 = var1.getCluster();
      this.healthCheckInterval = (long)var2.getHealthCheckIntervalMillis();
   }

   public synchronized void pingReceived(ServerInformation var1) {
      if (this.leader == null || !var1.getServerName().equals(this.leader.getServerName())) {
         ClusterMemberInfo var2 = null;
         Collection var3 = ClusterService.getClusterService().getAllRemoteMembers();
         Iterator var4 = var3.iterator();

         while(var4.hasNext()) {
            var2 = (ClusterMemberInfo)var4.next();
            if (var2.serverName().equals(var1.getServerName())) {
               break;
            }
         }

         if (var2 != null) {
            this.leader = new SuspectedMemberInfoImpl(var2);
         }
      }

      this.lastHeartbeatReceived = System.currentTimeMillis();
      if (!this.timerStarted) {
         this.timerStarted = true;
         debug("Starting Heartbeat Monitor");
         this.heartbeatMonitorTimer = TimerManagerFactory.getTimerManagerFactory().getDefaultTimerManager().schedule(new HeartbeatMonitor(), this.healthCheckInterval, this.healthCheckInterval);
      }
   }

   public void stop() {
      if (this.heartbeatMonitorTimer != null) {
         this.heartbeatMonitorTimer.cancel();
      }

      this.timerStarted = false;
      if (DEBUG) {
         debug("Halting Member Death Detector HeartbeatReceiver");
      }

   }

   public boolean isStarted() {
      return this.timerStarted;
   }

   public long getHealthCheckInterval() {
      return this.healthCheckInterval;
   }

   static void fatalError(String var0) {
      debug(" fatalError: " + var0);
      HealthMonitorService.subsystemFailed("MemberDeathDetectorHeartbeatReceiver", var0);
   }

   private static void debug(String var0) {
      System.out.println("[MemberDeathDetectorHeartbeatReceiver] " + var0);
   }

   private static boolean debugEnabled() {
      return debugDisconnectMonitor.isEnabled();
   }

   public static void enableHeartbeatReceiver() {
      RMIClusterMessageEndPointImpl var0 = RMIClusterMessageEndPointImpl.getInstance();
      MemberDeathDetectorHeartbeatReceiver var1 = getInstance();
      var0.registerPingMessageListener(var1);

      try {
         ServerHelper.exportObject(var0);
      } catch (RemoteException var3) {
         var3.printStackTrace();
      }

   }

   // $FF: synthetic method
   MemberDeathDetectorHeartbeatReceiver(Object var1) {
      this();
   }

   private class HeartbeatMonitor implements TimerListener {
      private HeartbeatMonitor() {
      }

      public void timerExpired(Timer var1) {
         if (System.currentTimeMillis() > MemberDeathDetectorHeartbeatReceiver.this.lastHeartbeatReceived + MemberDeathDetectorHeartbeatReceiver.this.healthCheckInterval) {
            ProbeContextImpl var2 = new ProbeContextImpl(MemberDeathDetectorHeartbeatReceiver.this.leader);
            ProbeManager.getClusterMemberProbeManager().invoke(var2);
            if (var2.getResult() == 1 || var2.getResult() == 0) {
               return;
            }

            MemberDeathDetectorHeartbeatReceiver.fatalError(var2.getMessage());
         }

      }

      // $FF: synthetic method
      HeartbeatMonitor(Object var2) {
         this();
      }
   }

   private static final class SingletonMaker {
      static final MemberDeathDetectorHeartbeatReceiver THE_ONE = new MemberDeathDetectorHeartbeatReceiver();
   }
}
