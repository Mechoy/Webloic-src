package weblogic.application.utils;

import java.security.AccessController;
import weblogic.application.ApplicationContextInternal;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.JMSServerMBean;
import weblogic.management.configuration.SAFAgentMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.SubDeploymentMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.configuration.VirtualHostMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public final class TargetUtils {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public static TargetMBean findLocalServerTarget(TargetMBean[] var0) {
      if (var0 == null) {
         return null;
      } else {
         ServerMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServer();
         return findLocalTarget(var0, var1);
      }
   }

   public static TargetMBean findLocalTarget(TargetMBean[] var0, ServerMBean var1) {
      if (var0 == null) {
         return null;
      } else {
         String var2 = var1.getName();

         for(int var3 = 0; var3 < var0.length; ++var3) {
            if (var0[var3] instanceof ClusterMBean) {
               if (var1.getCluster() != null && var1.getCluster().getName().equals(var0[var3].getName())) {
                  return var0[var3];
               }
            } else if (var0[var3] instanceof VirtualHostMBean) {
               VirtualHostMBean var4 = (VirtualHostMBean)var0[var3];
               if (isDeployedLocally(var4.getTargets())) {
                  return var4;
               }
            } else if (var0[var3] instanceof JMSServerMBean) {
               JMSServerMBean var5 = (JMSServerMBean)var0[var3];
               if (isDeployedLocally(var5.getTargets())) {
                  return var5;
               }
            } else {
               if (var0[var3] instanceof SAFAgentMBean) {
                  SAFAgentMBean var6 = (SAFAgentMBean)var0[var3];
                  return findLocalTarget(var6.getTargets(), var1);
               }

               if (var0[var3].getName().equals(var2)) {
                  return var0[var3];
               }
            }
         }

         return null;
      }
   }

   public static boolean isDeployedLocally(TargetMBean[] var0) {
      ServerMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServer();
      return findLocalTarget(var0, var1) != null;
   }

   private static SubDeploymentMBean findSubDeployment(SubDeploymentMBean[] var0, ApplicationContextInternal var1, String var2) {
      if (var0 == null) {
         return null;
      } else {
         for(int var3 = 0; var3 < var0.length; ++var3) {
            String var4 = var1 != null ? EarUtils.toModuleId(var1, var0[var3].getName()) : var0[var3].getName();
            if (var2.equals(var4)) {
               return var0[var3];
            }
         }

         return null;
      }
   }

   public static TargetMBean[] findModuleTargets(BasicDeploymentMBean var0, BasicDeploymentMBean var1, String var2) {
      return findModuleTargets(var0, var1, (ApplicationContextInternal)null, var2);
   }

   public static TargetMBean[] findModuleTargets(BasicDeploymentMBean var0, BasicDeploymentMBean var1, ApplicationContextInternal var2, String var3) {
      BasicDeploymentMBean var4 = var0 != null ? var0 : var1;
      SubDeploymentMBean var5 = findSubDeployment(var4.getSubDeployments(), var2, var3);
      return var5 != null && var5.getTargets() != null && var5.getTargets().length != 0 ? var5.getTargets() : var4.getTargets();
   }

   public static boolean isModuleDeployedHere(BasicDeploymentMBean var0, String var1) {
      SubDeploymentMBean var2 = findSubDeployment(var0.getSubDeployments(), (ApplicationContextInternal)null, var1);
      return var2 != null && var2.getTargets() != null && var2.getTargets().length != 0 ? isDeployedLocally(var2.getTargets()) : isDeployedLocally(var0.getTargets());
   }

   public static TargetMBean findLocalTarget(BasicDeploymentMBean var0, ServerMBean var1) {
      TargetMBean[] var2 = var0.getTargets();
      Object var3 = null;
      if (var2 != null) {
         var3 = findLocalTarget(var2, var1);
      }

      if (var3 == null) {
         SubDeploymentMBean[] var4 = var0.getSubDeployments();
         if (var3 != null) {
            for(int var5 = 0; var5 < var4.length; ++var5) {
               SubDeploymentMBean var6 = var4[var5];
               var3 = findLocalTarget(var6.getTargets(), var1);
               if (var3 != null) {
                  break;
               }
            }
         }
      }

      if (var3 == null) {
         var3 = var1;
      }

      return (TargetMBean)var3;
   }
}
