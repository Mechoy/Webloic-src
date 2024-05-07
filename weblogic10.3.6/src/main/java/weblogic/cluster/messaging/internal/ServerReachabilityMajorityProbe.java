package weblogic.cluster.messaging.internal;

import java.security.AccessController;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;

public class ServerReachabilityMajorityProbe implements Probe {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final DebugCategory debugDisconnectMonitor = Debug.getCategory("weblogic.cluster.leasing.DisconnectMonitor");
   private static final boolean DEBUG = debugEnabled();

   public void invoke(ProbeContext var1) {
      if (DEBUG) {
         debug("check ServerReachabilityMajority");
      }

      ServerReachabilityMajorityService var2 = ServerReachabilityMajorityServiceImpl.getInstance();
      String var3 = ManagementService.getRuntimeAccess(kernelId).getServer().getCluster().getName();
      SRMResult var4 = var2.performSRMCheck((ServerInformation)null, var3);
      if (!var4.hasReachabilityMajority()) {
         String var5 = "Server is not in the majority cluster partition";
         var1.setMessage(var5);
         var1.setNextAction(0);
         var1.setResult(-1);
      } else {
         var1.setNextAction(1);
         var1.setResult(1);
      }
   }

   private static boolean debugEnabled() {
      return debugDisconnectMonitor.isEnabled();
   }

   private static void debug(String var0) {
      System.out.println("[ServerReachabilityMajorityProbe] " + var0);
   }
}
