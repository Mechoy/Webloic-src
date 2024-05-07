package weblogic.cluster.migration;

import java.io.File;
import java.security.AccessController;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import weblogic.cluster.ClusterTextTextFormatter;
import weblogic.common.internal.VersionInfo;
import weblogic.management.WebLogicMBean;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JTAMigratableTargetMBean;
import weblogic.management.configuration.MachineMBean;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.DomainAccess;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.ServerLifeCycleRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public final class MTCustomValidator {
   private static final ClusterTextTextFormatter fmt = new ClusterTextTextFormatter();
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   public static boolean ASM_JMS_DISABLED = false;

   public static void validateMigratableTarget(MigratableTargetMBean var0) {
      String var1 = var0.getType();
      ClusterMBean var2 = var0.getCluster();
      ServerMBean var3 = var0.getUserPreferredServer();
      String var4 = var0.getName();
      if (var3 == null) {
         throw new IllegalArgumentException(fmt.getMigratableTargetInvViolation_1B(var4));
      } else {
         String var5 = var3.getName() + " (migratable)";
         if (var3.getCluster() != null || !var4.equals(var5) && !(var0 instanceof JTAMigratableTargetMBean)) {
            if (var2 == null) {
               throw new IllegalArgumentException(fmt.getMigratableTargetInvViolation_1A(var4));
            } else {
               String var6 = var3.getName();
               if (!isUserPreferredServerIsPartOfCluster(var6, var2.getServerNames())) {
                  throw new IllegalArgumentException(fmt.getMigratableTargetInvViolation_1C(var4));
               } else if (!var3.getCluster().getName().equals(var2.getName())) {
                  throw new IllegalArgumentException(fmt.getMigratableTargetInvViolation_2(var4));
               } else {
                  ServerMBean[] var7 = var0.getConstrainedCandidateServers();
                  if (var7.length > 0) {
                     DomainMBean var8 = (DomainMBean)var3.getParent();
                     String var9 = var8.getConfigurationVersion();
                     if ((var9 == null || (new VersionInfo(var9)).getMajor() > 8) && !isUserPreferredServersIsPartOfCandidateServers(var3, var7)) {
                        throw new IllegalArgumentException(fmt.getMigratableTargetInvViolation_3(var4));
                     }

                     if (!areAllCandidateServersPartOfCluster(var7, var2)) {
                        throw new IllegalArgumentException(fmt.getMigratableTargetInvViolation_4(var4));
                     }
                  }

               }
            }
         }
      }
   }

   private static boolean isUserPreferredServerIsPartOfCluster(String var0, Set var1) {
      boolean var2 = false;
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         if (var4.equals(var0)) {
            var2 = true;
            break;
         }
      }

      return var2;
   }

   private static boolean isUserPreferredServersIsPartOfCandidateServers(ServerMBean var0, ServerMBean[] var1) {
      String var2 = var0.getName();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         if (var2.equals(var1[var3].getName())) {
            return true;
         }
      }

      return false;
   }

   private static boolean areAllCandidateServersPartOfCluster(ServerMBean[] var0, ClusterMBean var1) {
      HashSet var2 = new HashSet();

      int var3;
      for(var3 = 0; var3 < var0.length; ++var3) {
         var2.add(var0[var3].getName());
      }

      for(var3 = 0; var3 < var0.length; ++var3) {
         if (!var2.contains(var0[var3].getName())) {
            return false;
         }
      }

      return true;
   }

   public static void destroyMigratableTarget(MigratableTargetMBean var0) {
      ServerMBean var1 = var0.getUserPreferredServer();
      if (ManagementService.isRuntimeAccessInitialized()) {
         ServerLifeCycleRuntimeMBean var2 = getServerLifeCycleRuntime(var1.getName());
         String var3 = var2 == null ? "UNKNOWN" : var2.getState();
         if (var3.equals("RUNNING") && ManagementService.getDomainAccess(kernelId).getDeployerRuntime().getDeployments(var0).length != 0) {
            throw new IllegalArgumentException(fmt.getCannotDeleteMigratableTargetException(var0.getName()));
         }
      }
   }

   private static ServerLifeCycleRuntimeMBean getServerLifeCycleRuntime(String var0) {
      if (ManagementService.getRuntimeAccess(kernelId).isAdminServer()) {
         DomainAccess var1 = ManagementService.getDomainAccess(kernelId);
         return var1.lookupServerLifecycleRuntime(var0);
      } else {
         return null;
      }
   }

   public static void destroyServer(ServerMBean var0) {
      MigratableTargetMBean[] var1 = ManagementService.getRuntimeAccess(kernelId).getDomain().getMigratableTargets();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         ServerMBean var3 = var1[var2].getUserPreferredServer();
         if (var0.getName().equals(var3.getName())) {
            throw new IllegalArgumentException(fmt.getCannotDeleteServerException(var0.getName()));
         }
      }

   }

   public static void destroyCluster(ClusterMBean var0) {
      MigratableTargetMBean[] var1 = ManagementService.getRuntimeAccess(kernelId).getDomain().getMigratableTargets();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         ClusterMBean var3 = var1[var2].getCluster();
         if (var3.getName().equals(var0.getName()) && ManagementService.getDomainAccess(kernelId).getDeployerRuntime().getDeployments(var1[var2]).length > 0) {
            throw new IllegalArgumentException(fmt.getCannotDeleteClusterException(var0.getName()));
         }
      }

   }

   public static void removeConstrainedCandidateServer(MigratableTargetMBean var0, ServerMBean var1) {
      ServerMBean var2 = var0.getUserPreferredServer();
      if (var2.getName().equals(var1.getName())) {
         throw new IllegalArgumentException(fmt.getCannotRemoveUserPreferredServerException(var0.getName()));
      }
   }

   public static void canSetCluster(MigratableTargetMBean var0, ClusterMBean var1) {
      ClusterMBean var2 = var0.getCluster();
      if (var2 != null && !var2.getName().equals(var1.getName())) {
         throw new IllegalArgumentException(fmt.getCannotSetClusterException(var0.getName()));
      }
   }

   public static void validateMigrationPolicy(MigratableTargetMBean var0, String var1) {
      if (!"manual".equals(var1)) {
         if (ASM_JMS_DISABLED && !(var0 instanceof JTAMigratableTargetMBean)) {
            throw new IllegalArgumentException(fmt.getIllegalMigrationPolicy(var0.getName()));
         } else {
            ClusterMBean var2 = var0.getCluster();
            if (var2 == null) {
               WebLogicMBean var3 = var0.getParent();
               if (!(var3 instanceof ServerMBean)) {
                  return;
               }

               ServerMBean var4 = (ServerMBean)var3;
               var2 = var4.getCluster();
               if (var2 == null) {
                  return;
               }
            }

            String var7 = var2.getMigrationBasis();
            if ("database".equals(var7) && var2.getDataSourceForAutomaticMigration() == null) {
               throw new IllegalArgumentException(fmt.getCannotEnableAutoMigrationWithoutLeasing(var0.getName()));
            } else {
               if ("consensus".equals(var7)) {
                  ServerMBean[] var8 = var0.getConstrainedCandidateServers();
                  if (var8 == null) {
                     return;
                  }

                  for(int var5 = 0; var5 < var8.length; ++var5) {
                     MachineMBean var6 = var8[var5].getMachine();
                     if (var6 == null) {
                        throw new IllegalArgumentException(fmt.getNodemanagerRequiredOnCandidateServers(var8[var5].getName()));
                     }

                     if (var6.getNodeManager() == null) {
                        throw new IllegalArgumentException(fmt.getNodemanagerRequiredOnCandidateServers(var8[var5].getName()));
                     }
                  }
               }

            }
         }
      }
   }

   public static void validateScriptPath(String var0) {
      File var1 = new File(var0);
      if (var1.isAbsolute()) {
         throw new IllegalArgumentException("This value must specify a path relative to the service_migration directory in your domain." + var0);
      } else if (var0.indexOf("..") > -1) {
         throw new IllegalArgumentException("This value must specify a path relative to the service_migration directory in your domain." + var0);
      }
   }

   public static void validatePostScriptFailureFatal(MigratableTargetMBean var0, boolean var1) {
      if (ASM_JMS_DISABLED && !(var0 instanceof JTAMigratableTargetMBean)) {
         throw new IllegalArgumentException(fmt.getIllegalAttemptToSetPostScriptFailure(var0.getName()));
      }
   }

   public static void validateRestartOnFailure(MigratableTargetMBean var0, boolean var1) {
      if (ASM_JMS_DISABLED && !(var0 instanceof JTAMigratableTargetMBean)) {
         throw new IllegalArgumentException(fmt.getIllegalAttemptToSetRestartOnFailure(var0.getName()));
      }
   }

   public static void validateSecondsBetweenRestarts(MigratableTargetMBean var0, int var1) {
      if (ASM_JMS_DISABLED && !(var0 instanceof JTAMigratableTargetMBean)) {
         throw new IllegalArgumentException(fmt.getIllegalAttemptToSetSecondsBetweenRestarts(var0.getName()));
      }
   }

   public static void validateNumberOfRestartAttempts(MigratableTargetMBean var0, int var1) {
      if (ASM_JMS_DISABLED && !(var0 instanceof JTAMigratableTargetMBean)) {
         throw new IllegalArgumentException(fmt.getIllegalAttemptToSetNumberOfRestartAttempts(var0.getName()));
      }
   }
}
