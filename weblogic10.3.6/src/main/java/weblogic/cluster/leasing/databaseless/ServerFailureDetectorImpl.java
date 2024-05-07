package weblogic.cluster.leasing.databaseless;

import java.rmi.RemoteException;
import java.rmi.UnknownHostException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.naming.NamingException;
import weblogic.cluster.ClusterLogger;
import weblogic.cluster.ClusterMembersChangeEvent;
import weblogic.cluster.ClusterMembersChangeListener;
import weblogic.cluster.ClusterService;
import weblogic.cluster.messaging.internal.BaseClusterMessage;
import weblogic.cluster.messaging.internal.ClusterMessage;
import weblogic.cluster.messaging.internal.ClusterMessageEndPoint;
import weblogic.cluster.messaging.internal.ClusterMessageFactory;
import weblogic.cluster.messaging.internal.ClusterMessageSender;
import weblogic.cluster.messaging.internal.DebugLogger;
import weblogic.cluster.messaging.internal.MachineState;
import weblogic.cluster.messaging.internal.MessageDeliveryFailureListener;
import weblogic.cluster.messaging.internal.RMIClusterMessageEndPointImpl;
import weblogic.cluster.messaging.internal.SRMResult;
import weblogic.cluster.messaging.internal.ServerInformation;
import weblogic.cluster.messaging.internal.ServerInformationImpl;
import weblogic.jndi.Environment;
import weblogic.management.configuration.DatabaseLessLeasingBasisMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.MachineMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.URLManager;
import weblogic.rmi.extensions.DisconnectEvent;
import weblogic.rmi.extensions.DisconnectListener;
import weblogic.rmi.extensions.DisconnectMonitorListImpl;
import weblogic.rmi.extensions.DisconnectMonitorUnavailableException;
import weblogic.rmi.extensions.PortableRemoteObject;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManagerFactory;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;
import weblogic.utils.StackTraceUtils;
import weblogic.work.WorkManagerFactory;

public final class ServerFailureDetectorImpl implements ServerFailureDetector, DisconnectListener, MessageDeliveryFailureListener, ClusterMembersChangeListener {
   private static final AuthenticatedSubject kernelId;
   private static final ClusterMessage PING_REQUEST;
   private static final int PING_PERIOD = 10000;
   private static final int NODEMANAGER_QUERY_DELAY = 3000;
   private static final DebugCategory debugFailureDetector;
   private static final boolean DEBUG;
   private ClusterMessageEndPoint remote;
   private ServerInformation serverInfo;
   private ServerFailureListener listener;
   private boolean nmRetryTimerRunning;
   private boolean stopped;
   private Timer pingTimer;
   private long lastStateCheckTime;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public synchronized void start(ServerInformation var1, ServerFailureListener var2) {
      if (DEBUG) {
         debug("--- starting server failure detector for " + var1.getServerName());
      }

      this.serverInfo = var1;
      this.listener = var2;
      this.stopped = false;
      this.nmRetryTimerRunning = false;
      this.pingTimer = TimerManagerFactory.getTimerManagerFactory().getDefaultTimerManager().schedule(new PingTimer(), 10000L, 10000L);
      this.registerForDisconnect();
      ClusterMessageFactory.getInstance().registerMessageDeliveryFailureListener(this);
      ClusterService.getClusterService().addClusterMembersListener(this);
   }

   private static ServerInformation createLocalServerInformation() {
      return new ServerInformationImpl(ClusterService.getClusterService().getLocalMember());
   }

   public synchronized boolean stop() {
      if (this.stopped) {
         return false;
      } else {
         this.stopped = true;
         this.pingTimer.cancel();
         ClusterMessageFactory.getInstance().removeMessageDeliveryFailureListener(this);
         ClusterService.getClusterService().removeClusterMembersListener(this);

         try {
            DisconnectMonitorListImpl.getDisconnectMonitor().removeDisconnectListener(this.remote, this);
         } catch (DisconnectMonitorUnavailableException var2) {
            if (DEBUG) {
               debug("unable to stop !");
               var2.printStackTrace();
            }
         }

         return true;
      }
   }

   private void registerForDisconnect() {
      try {
         DatabaseLessLeasingBasisMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServer().getCluster().getDatabaseLessLeasingBasis();
         Environment var2 = new Environment();
         var2.setProviderUrl(URLManager.findAdministrationURL(this.serverInfo.getServerName()));
         var2.setRequestTimeout((long)var1.getMessageDeliveryTimeout());
         this.remote = (ClusterMessageEndPoint)PortableRemoteObject.narrow(var2.getInitialReference(RMIClusterMessageEndPointImpl.class), ClusterMessageEndPoint.class);
         DisconnectMonitorListImpl.getDisconnectMonitor().addDisconnectListener(this.remote, this);
         if (DEBUG) {
            debug("registered for disconnect events from " + this.serverInfo.getServerName());
         }
      } catch (DisconnectMonitorUnavailableException var3) {
         throw new AssertionError("Unable to register with the leader for disconnects!" + var3);
      } catch (UnknownHostException var4) {
         this.onDisconnect((DisconnectEvent)null);
      } catch (NamingException var5) {
         this.onDisconnect((DisconnectEvent)null);
      }

   }

   private static void debug(String var0) {
      DebugLogger.debug("[ServerFailureDetector] " + var0);
   }

   public synchronized void onDisconnect(DisconnectEvent var1) {
      if (this.stateCheckNeeded(var1)) {
         ServerMBean var2 = ManagementService.getRuntimeAccess(kernelId).getDomain().lookupServer(this.serverInfo.getServerName());
         if (!$assertionsDisabled && var2 == null) {
            throw new AssertionError();
         } else {
            MachineState var3 = getState(var2);
            if (!this.isServerDead(var3)) {
               this.lastStateCheckTime = System.currentTimeMillis();
            } else {
               long var4 = (long)(var2.getCluster().getDatabaseLessLeasingBasis().getFenceTimeout() * 1000);
               if (var3.isMachineUnavailable() && var4 > 0L) {
                  if (DEBUG) {
                     debug("unable to reach the NodeManager for the server " + var2.getName() + ". Fence timeout is " + var4 + "ms");
                  }

                  this.nmRetryTimerRunning = true;
                  TimerManagerFactory.getTimerManagerFactory().getDefaultTimerManager().schedule(new NMRetryTimer(), var4);
               } else {
                  this.invokeListener(var3);
               }

            }
         }
      }
   }

   private void invokeListener(MachineState var1) {
      if (DEBUG) {
         debug("Invoking onServerFailure() for " + this.serverInfo.getServerName());
      }

      if (this.stop()) {
         WorkManagerFactory.getInstance().getSystem().schedule(new ListenerRunnable(this.listener, this.serverInfo, var1));
      }

   }

   private boolean stateCheckNeeded(DisconnectEvent var1) {
      if (!this.stopped && !this.nmRetryTimerRunning) {
         String var2 = this.getConfiguredMachine().getNodeManager().getNMType();
         if (!"plain".equalsIgnoreCase(var2) && !"ssl".equalsIgnoreCase(var2)) {
            return var1 != null || System.currentTimeMillis() - this.lastStateCheckTime >= 3000L;
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   private static MachineState getState(ServerMBean var0) {
      SRMResult var2 = EnvironmentFactory.getServerReachabilityMajorityService().getLastSRMResult();
      MachineMBean var1;
      if (var2 != null) {
         String var3 = var2.getCurrentMachine(var0.getName());
         DomainMBean var4 = ManagementService.getRuntimeAccess(kernelId).getDomain();
         if (DEBUG) {
            ClusterLogger.logDebug("SRM returned " + var3 + " for " + var0.getName());
         }

         var1 = var4.lookupMachine(var3);
      } else {
         if (DEBUG) {
            debug("unable to get machine for " + var0.getName());
         }

         var1 = var0.getMachine();
      }

      MachineState var5 = MachineState.getMachineState(var1, true);
      if (DEBUG) {
         debug("Machine state for " + var0.getName() + " is " + var5);
      }

      return var5;
   }

   private boolean isServerDead(MachineState var1) {
      if (var1.isMachineUnavailable()) {
         return true;
      } else {
         String var2 = var1.getServerState(this.serverInfo.getServerName());
         if (DEBUG) {
            debug("serverstate for " + this.serverInfo.getServerName() + " is " + var2);
         }

         return var2 != null && !var2.equals("RUNNING") && !var2.equals("ADMIN") && !var2.equals("SUSPENDING") && !var2.equals("FORCE_SUSPENDING") && !var2.equals("RESUMING");
      }
   }

   public void onMessageDeliveryFailure(String var1, RemoteException var2) {
      if (DEBUG) {
         debug("received onMessageDeliveryFailure for " + var1 + " due to " + StackTraceUtils.throwable2StackTrace(var2));
      }

      if (this.serverInfo.getServerName().equals(var1)) {
         if (DEBUG) {
            debug("calling onDisconnect due to onMessageDeliveryFailure!");
         }

         this.onDisconnect((DisconnectEvent)null);
      }

   }

   public void clusterMembersChanged(ClusterMembersChangeEvent var1) {
      if (this.serverInfo.getServerName().equals(var1.getClusterMemberInfo().serverName()) && var1.getAction() == 1) {
         this.onDisconnect((DisconnectEvent)null);
      }

   }

   private MachineMBean getConfiguredMachine() {
      return ManagementService.getRuntimeAccess(kernelId).getDomain().lookupServer(this.serverInfo.getServerName()).getMachine();
   }

   private static boolean debugEnabled() {
      return debugFailureDetector.isEnabled() || DebugLogger.isDebugEnabled();
   }

   static {
      $assertionsDisabled = !ServerFailureDetectorImpl.class.desiredAssertionStatus();
      kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      PING_REQUEST = new BaseClusterMessage(createLocalServerInformation(), 9);
      debugFailureDetector = Debug.getCategory("weblogic.cluster.leasing.FailureDetector");
      DEBUG = debugEnabled();
   }

   private class NMRetryTimer implements TimerListener {
      private NMRetryTimer() {
      }

      public void timerExpired(Timer var1) {
         ServerMBean var2 = ManagementService.getRuntimeAccess(ServerFailureDetectorImpl.kernelId).getDomain().lookupServer(ServerFailureDetectorImpl.this.serverInfo.getServerName());

         assert var2 != null;

         MachineState var3 = ServerFailureDetectorImpl.getState(var2);
         if (!ServerFailureDetectorImpl.this.stopped && ServerFailureDetectorImpl.this.isServerDead(var3)) {
            if (ServerFailureDetectorImpl.DEBUG) {
               ServerFailureDetectorImpl.debug("NMRetryTimer invoking onServerFailure() for " + var2.getName());
            }

            ServerFailureDetectorImpl.this.invokeListener(var3);
         } else {
            ServerFailureDetectorImpl.this.nmRetryTimerRunning = false;
         }
      }

      // $FF: synthetic method
      NMRetryTimer(Object var2) {
         this();
      }
   }

   private static class ListenerRunnable implements Runnable {
      private final ServerFailureListener listener;
      private final ServerInformation serverInfo;
      private final MachineState machineState;

      ListenerRunnable(ServerFailureListener var1, ServerInformation var2, MachineState var3) {
         this.listener = var1;
         this.serverInfo = var2;
         this.machineState = var3;
      }

      public void run() {
         if (this.machineState.isMachineUnavailable()) {
            this.listener.onMachineFailure(new MachineFailureEvent() {
               public List getFailedServers() {
                  Object var1 = new ArrayList();
                  List var2 = ListenerRunnable.this.machineState.getServerNames();
                  SRMResult var3 = EnvironmentFactory.getServerReachabilityMajorityService().getLastSRMResult();
                  if (var3 != null) {
                     String var4 = ListenerRunnable.this.machineState.getMachineName();
                     Iterator var5 = var2.iterator();

                     while(var5.hasNext()) {
                        String var6 = (String)var5.next();
                        if (var3.getCurrentMachine(var6).equals(var4)) {
                           ((List)var1).add(var6);
                        }
                     }
                  } else {
                     var1 = var2;
                  }

                  return (List)var1;
               }

               public String getMachineName() {
                  return ListenerRunnable.this.machineState.getMachineName();
               }
            });
         } else {
            this.listener.onServerFailure(new ServerFailureEvent() {
               public ServerInformation getServerInformation() {
                  return ListenerRunnable.this.serverInfo;
               }
            });
         }

      }
   }

   private class PingTimer implements TimerListener {
      private PingTimer() {
      }

      public void timerExpired(Timer var1) {
         if (ServerFailureDetectorImpl.this.stopped) {
            var1.cancel();
         } else {
            ClusterMessageSender var2 = ClusterMessageFactory.getInstance().getDefaultMessageSender();

            try {
               var2.send(ServerFailureDetectorImpl.PING_REQUEST, ServerFailureDetectorImpl.this.serverInfo);
            } catch (RemoteException var4) {
            }

         }
      }

      // $FF: synthetic method
      PingTimer(Object var2) {
         this();
      }
   }
}
