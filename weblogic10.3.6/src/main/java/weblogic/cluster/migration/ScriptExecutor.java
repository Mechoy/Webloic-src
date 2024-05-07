package weblogic.cluster.migration;

import java.io.File;
import java.io.IOException;
import java.security.AccessController;
import weblogic.cluster.ClusterLogger;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.nodemanager.mbean.NodeManagerRuntime;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class ScriptExecutor {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public static boolean isNMAvailable(ServerMBean var0) {
      return var0.getMachine() != null;
   }

   public static boolean runNMScript(String var0, MigratableTargetMBean var1) {
      return runNMScript(var0, var1, ManagementService.getRuntimeAccess(kernelId).getServer());
   }

   public static boolean runNMScript(String var0, MigratableTargetMBean var1, ServerMBean var2) {
      NodeManagerRuntime var3 = NodeManagerRuntime.getInstance(var2.getMachine());
      if (var1 == null) {
         return false;
      } else if (var2 == null) {
         return false;
      } else if (var0 != null && var0.trim().length() != 0) {
         if (!isNMAvailable(var2)) {
            ClusterLogger.logMissingMachine(var2.getName());
            return false;
         } else {
            try {
               long var4 = 0L;
               var3.runScript(new File(var0), var4);
               return true;
            } catch (IOException var6) {
               ClusterLogger.logScriptFailed(var0, var6);
               return false;
            }
         }
      } else {
         return true;
      }
   }
}
