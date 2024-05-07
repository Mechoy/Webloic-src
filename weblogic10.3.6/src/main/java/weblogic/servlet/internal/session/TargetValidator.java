package weblogic.servlet.internal.session;

import weblogic.logging.Loggable;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.configuration.VirtualHostMBean;
import weblogic.servlet.internal.WebAppModule;

final class TargetValidator {
   static void validateTargetting(WebAppModule var0, String var1) throws DeploymentException {
      TargetMBean[] var2 = var0.getTargets();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         TargetMBean var4 = var2[var3];
         if (var4 instanceof ServerMBean) {
            ClusterMBean var5 = ((ServerMBean)var4).getCluster();
            if (var5 == null) {
               continue;
            }

            if (!listContainsAllClusterMembers(var2, var5)) {
               Loggable var6 = HTTPSessionLogger.logInhomogeneousDeploymentForAppLoggable(var0.getId(), var0.getAppDisplayName(), var1, var5.getName());
               var6.log();
               throw new DeploymentException(var6.getMessage());
            }
         }

         if (var4 instanceof VirtualHostMBean) {
            validateTargetting((VirtualHostMBean)var4, var0, var1);
         }
      }

   }

   private static void validateTargetting(VirtualHostMBean var0, WebAppModule var1, String var2) throws DeploymentException {
      TargetMBean[] var3 = var0.getTargets();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         TargetMBean var5 = var3[var4];
         if (var5 instanceof ServerMBean) {
            ClusterMBean var6 = ((ServerMBean)var5).getCluster();
            if (var6 != null && !listContainsAllClusterMembers(var3, var6)) {
               Loggable var7 = HTTPSessionLogger.logInhomogeneousDeploymentForVHostLoggable(var1.getId(), var1.getAppDisplayName(), var2, var0.getName(), var6.getName());
               var7.log();
               throw new DeploymentException(var7.getMessage());
            }
         }
      }

   }

   private static boolean listContainsAllClusterMembers(TargetMBean[] var0, ClusterMBean var1) {
      ServerMBean[] var2 = var1.getServers();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (!contains(var0, var2[var3])) {
            return false;
         }
      }

      return true;
   }

   private static boolean contains(TargetMBean[] var0, ServerMBean var1) {
      for(int var2 = 0; var2 < var0.length; ++var2) {
         if (var0[var2] == var1) {
            return true;
         }
      }

      return false;
   }
}
