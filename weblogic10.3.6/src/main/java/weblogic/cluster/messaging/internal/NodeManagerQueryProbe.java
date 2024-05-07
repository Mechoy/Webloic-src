package weblogic.cluster.messaging.internal;

import java.io.IOException;
import java.security.AccessController;
import weblogic.cluster.ClusterService;
import weblogic.cluster.singleton.LeaseManager;
import weblogic.cluster.singleton.SingletonMonitor;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.nodemanager.mbean.NodeManagerRuntime;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;

public class NodeManagerQueryProbe implements Probe {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final DebugCategory debugDisconnectMonitor = Debug.getCategory("weblogic.cluster.leasing.DisconnectMonitor");
   private static final boolean DEBUG = debugEnabled();

   public void invoke(ProbeContext var1) {
      SuspectedMemberInfo var2 = var1.getSuspectedMemberInfo();
      if (DEBUG) {
         debug("NodeManagerQueryProbe of server: " + var2.getServerName());
      }

      ServerMBean var3 = ManagementService.getRuntimeAccess(kernelId).getDomain().lookupServer(var1.getSuspectedMemberInfo().getServerName());
      NodeManagerRuntime var4 = NodeManagerRuntime.getInstance(var3);
      String var5 = "UNKNOWN";

      try {
         var5 = var4.getState(var3);
      } catch (IOException var8) {
         var1.setNextAction(1);
         var1.setResult(-1);
         return;
      }

      if (DEBUG) {
         debug("NodeManagerQueryProbe runtime state: " + var5 + " for server: " + var3.getName());
      }

      if (!var2.hasVoidedSingletonServices() && SingletonMonitor.canMigrateLease(var5)) {
         LeaseManager var6 = ClusterService.getClusterService().getDefaultLeaseManager("service");
         String var7 = LeaseManager.getOwnerIdentity(var2.getServerIdentity());
         var6.voidLeases(var7);
         if (DEBUG) {
            debug("NodeManagerQueryProbe " + var7 + " is marked as " + var5 + ". Voiding all its singleton services leases");
         }

         var2.voidedSingletonServices();
      }

      if (MemberDeathDetectorImpl.isServerMigratable(var5, var3)) {
         if (DEBUG) {
            debug("NodeManagerQueryProbe " + var3 + " is marked as " + var5 + ". Voiding all its leases");
         }

         var1.setNextAction(0);
         var1.setResult(-1);
      } else {
         var1.setNextAction(0);
         var1.setResult(0);
      }
   }

   private static void debug(String var0) {
      System.out.println("[NodeManagerQueryProbe] " + var0);
   }

   private static boolean debugEnabled() {
      return debugDisconnectMonitor.isEnabled();
   }
}
