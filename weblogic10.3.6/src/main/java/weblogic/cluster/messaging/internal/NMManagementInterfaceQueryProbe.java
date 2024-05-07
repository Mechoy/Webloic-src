package weblogic.cluster.messaging.internal;

import java.io.IOException;
import java.security.AccessController;
import weblogic.cluster.singleton.SingletonMonitor;
import weblogic.management.configuration.NetworkAccessPointMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.nodemanager.mbean.NodeManagerRuntime;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class NMManagementInterfaceQueryProbe implements Probe {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public void invoke(ProbeContext var1) {
      if (ManagementService.getRuntimeAccess(kernelId).getDomain().isExalogicOptimizationsEnabled()) {
         debug("probe server: " + var1.getSuspectedMemberInfo().getServerName());
         NetworkAccessPointMBean var2 = null;
         ServerMBean var3 = ManagementService.getRuntimeAccess(kernelId).getDomain().lookupServer(var1.getSuspectedMemberInfo().getServerName());
         NetworkAccessPointMBean[] var4 = var3.getNetworkAccessPoints();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            if ("management-interface".equals(var4[var5].getName())) {
               var2 = var4[var5];
               break;
            }
         }

         if (var2 != null) {
            NodeManagerRuntime var9 = NodeManagerRuntime.getInstance(var2.getListenAddress(), var3.getMachine().getNodeManager().getListenPort(), "ssl");
            String var6 = null;

            try {
               var6 = var9.getState(var3);
            } catch (IOException var8) {
               var1.setNextAction(1);
               var1.setResult(-1);
               return;
            }

            debug("runtime state: " + var6 + " for server: " + var3.getName());
            MemberDeathDetectorImpl.getInstance();
            if (MemberDeathDetectorImpl.isServerMigratable(var6, var3) && SingletonMonitor.canMigrateLease(var6)) {
               debug(var3 + " is marked as " + var6 + ". Voiding all its leases");
               var1.setNextAction(0);
               var1.setResult(-1);
            } else {
               var1.setNextAction(0);
               var1.setResult(1);
            }
         }
      }
   }

   private static void debug(String var0) {
      System.out.println("[NMManagementInterfaceQueryProbe] " + var0);
   }
}
