package weblogic.cluster.leasing.databaseless;

import java.io.IOException;
import java.rmi.RemoteException;
import java.security.AccessController;
import weblogic.cluster.MemberManager;
import weblogic.cluster.messaging.internal.DebugLogger;
import weblogic.cluster.messaging.internal.RMIClusterMessageEndPointImpl;
import weblogic.cluster.messaging.internal.ServerInformation;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;
import weblogic.t3.srvr.EnableListenersIfAdminChannelAbsentService;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;

public final class ClusterLeaderService extends AbstractServerService {
   private static final DebugCategory debugClusterLeaderService;
   private static final AuthenticatedSubject kernelId;
   private static ClusterLeaderService THE_ONE;
   private static final boolean DEBUG;
   private boolean started = false;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public ClusterLeaderService() {
      synchronized(ClusterLeaderService.class) {
         if (!$assertionsDisabled && THE_ONE != null) {
            throw new AssertionError();
         } else {
            THE_ONE = this;
         }
      }
   }

   public static ClusterLeaderService getInstance() {
      if (!$assertionsDisabled && THE_ONE == null) {
         throw new AssertionError();
      } else {
         return THE_ONE;
      }
   }

   public String getLeaderName() {
      ServerInformation var1 = this.getLeaderInformation();
      return var1 != null ? var1.getServerName() : null;
   }

   public void start() throws ServiceFailureException {
      if (!this.started) {
         PrimordialClusterLeaderService.getInstance().stop();
         ClusterMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServer().getCluster();
         if (var1 != null) {
            if ("consensus".equalsIgnoreCase(var1.getMigrationBasis())) {
               if (DEBUG) {
                  debug("--- DATABASE LESS LEASING IS TURNED ON ---");
               }

               ensureServersHaveMachines(var1);
               if (!Boolean.getBoolean("weblogic.nodemanager.ServiceEnabled")) {
                  DatabaseLessLeasingLogger.logServerNotStartedByNodeManager();
                  throw new ServiceFailureException("Server must be started by NodeManager when consensus leasing is enabled");
               } else {
                  try {
                     ServerHelper.exportObject(RMIClusterMessageEndPointImpl.getInstance());
                  } catch (RemoteException var3) {
                     throw new ServiceFailureException(var3);
                  }

                  try {
                     if (!EnableListenersIfAdminChannelAbsentService.startInRunningState()) {
                        MemberManager.theOne().sendMemberRuntimeState();
                     }
                  } catch (IOException var4) {
                     throw new ServiceFailureException("Failed to send runtime state message", var4);
                  }

                  int var2 = ManagementService.getRuntimeAccess(kernelId).getServer().getCluster().getDatabaseLessLeasingBasis().getMemberDiscoveryTimeout();
                  if (DEBUG) {
                     debug("Initialize EnvironmentFoctory and start the discovery timer");
                  }

                  EnvironmentFactory.initialize();
                  EnvironmentFactory.getDiscoveryService().start(var2);
                  this.started = true;
               }
            }
         }
      }
   }

   private static void ensureServersHaveMachines(ClusterMBean var0) throws ServiceFailureException {
      ServerMBean[] var1 = var0.getServers();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2].getMachine() == null) {
            throw new ServiceFailureException("server " + var1[var2] + " is not associated with a machine");
         }
      }

   }

   private ServerInformation getLeaderInformation() {
      ServerInformation var1 = EnvironmentFactory.getClusterMember().getLeaderInformation();
      return var1 != null ? var1 : EnvironmentFactory.getClusterLeader().getLeaderInformation();
   }

   static ServerInformation getLeader() {
      return THE_ONE != null && THE_ONE.getLeaderInformation() != null ? THE_ONE.getLeaderInformation() : PrimordialClusterLeaderService.getInstance().getLeaderInformation();
   }

   ServerInformation getLocalServerInformation() {
      return ClusterMember.getInstance().getLocalServerInformation();
   }

   private static boolean debugEnabled() {
      return debugClusterLeaderService.isEnabled() || DebugLogger.isDebugEnabled();
   }

   private static void debug(String var0) {
      DebugLogger.debug("[ClusterLeaderService] " + var0);
   }

   static {
      $assertionsDisabled = !ClusterLeaderService.class.desiredAssertionStatus();
      debugClusterLeaderService = Debug.getCategory("weblogic.cluster.leasing.ClusterLeaderService");
      kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      THE_ONE = null;
      DEBUG = debugEnabled();
   }
}
