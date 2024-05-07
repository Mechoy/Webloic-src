package weblogic.cluster;

import java.security.AccessController;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.platform.OperatingSystem;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public final class ClusterValidator {
   private static final ClusterTextTextFormatter fmt = new ClusterTextTextFormatter();
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public static void validateMulticastAddress(String var0) {
      String[] var1 = var0.split("\\.");
      int[] var2 = new int[var1.length];

      try {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            var2[var3] = Integer.parseInt(var1[var3]);
         }
      } catch (NumberFormatException var4) {
         return;
      }

      if (var2.length != 4) {
         throw new IllegalArgumentException("Multicast addresses must be of format: ddd.ddd.ddd.ddd where d is a digit.");
      } else if (var2[0] < 224 || var2[0] > 239 || var2[1] < 0 || var2[1] > 255 || var2[2] < 0 || var2[2] > 255 || var2[3] < 0 || var2[3] > 255) {
         throw new IllegalArgumentException("Illegal value for MulticastAddress, the address must be in the range 224.0.0.0 - 239.255.255.255.");
      }
   }

   public static void validateAutoMigration(boolean var0) {
      if (var0) {
         String var1 = OperatingSystem.getOSName();
         if (!var1.equals("linux") && !var1.equals("solaris") && !var1.equals("sun") && !var1.equals("solarisx86") && !var1.equals("hp-ux") && !var1.equals("windows") && !var1.equals("aix")) {
            throw new IllegalArgumentException("Automatic Migration is only supported on Linux, Solaris and HP-UX operating systems.");
         }
      }
   }

   public static void canSetCluster(ServerMBean var0, ClusterMBean var1) {
      if (ManagementService.isRuntimeAccessInitialized()) {
         RuntimeAccess var2 = ManagementService.getRuntimeAccess(kernelId);
         if (var2 != null && !var2.isAdminServer()) {
            return;
         }

         DomainMBean var3 = var2.getDomain();
         ServerMBean var4 = var3.lookupServer(var0.getName());
         if (var4 == null) {
            return;
         }

         MigratableTargetMBean[] var5 = var3.getMigratableTargets();

         for(int var6 = 0; var6 < var5.length; ++var6) {
            if (var5[var6].getParent().getType().equals("Domain")) {
               String var7 = var0.getName() + " (migratable)";
               if (!var5[var6].getName().equals(var7)) {
                  boolean var8 = var5[var6].getUserPreferredServer().getName().equals(var0.getName());
                  String var9 = var4.getCluster() == null ? null : var4.getCluster().getName();
                  String var10 = var1 == null ? null : var1.getName();
                  if (var8 && (var9 != null || var10 != null)) {
                     if (var9 != null && var10 != null && !var9.equals(var10)) {
                        throw new IllegalArgumentException(fmt.getCannotChangeClusterWhileServerReferredToInMigratableTarget(var9, var0.getName()));
                     }

                     if (var9 != null && var10 == null) {
                        throw new IllegalArgumentException(fmt.getCannotChangeClusterWhileServerReferredToInMigratableTarget(var9, var0.getName()));
                     }
                  }
               }
            }
         }
      }

   }
}
