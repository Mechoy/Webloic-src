package weblogic.management.deploy.internal;

import java.security.AccessController;
import java.util.ArrayList;
import weblogic.management.configuration.ApplicationMBean;
import weblogic.management.configuration.ComponentMBean;
import weblogic.management.configuration.DeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class AppMBeanUsages {
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public static DeploymentMBean[] getDeployments(TargetMBean var0) {
      ArrayList var1 = new ArrayList();
      DomainMBean var2 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      DeploymentMBean[] var3 = var2.getDeployments();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         if (isTargeted(var3[var4], var0)) {
            var1.add(var3[var4]);
         }
      }

      ApplicationMBean[] var8 = var2.getApplications();

      for(int var5 = 0; var5 < var8.length; ++var5) {
         ComponentMBean[] var6 = var8[var5].getComponents();

         for(int var7 = 0; var7 < var6.length; ++var7) {
            if (isTargeted(var6[var7], var0)) {
               var1.add(var3[var7]);
            }
         }
      }

      DeploymentMBean[] var9 = new DeploymentMBean[var1.size()];
      return (DeploymentMBean[])((DeploymentMBean[])var1.toArray(var9));
   }

   private static boolean isTargeted(DeploymentMBean var0, TargetMBean var1) {
      TargetMBean[] var2 = var0.getTargets();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var1.getName().equals(var2[var3].getName())) {
            return true;
         }
      }

      return false;
   }
}
