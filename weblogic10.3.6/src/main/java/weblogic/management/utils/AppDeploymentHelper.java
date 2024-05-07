package weblogic.management.utils;

import java.security.AccessController;
import java.util.ArrayList;
import java.util.Arrays;
import weblogic.deploy.internal.adminserver.DeploymentManager;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.ApplicationMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.LibraryMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public final class AppDeploymentHelper {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   private AppDeploymentHelper() {
   }

   public static AppDeploymentMBean[] getAppsAndLibs(DomainMBean var0) {
      ArrayList var1 = new ArrayList();
      LibraryMBean[] var2 = var0.getInternalLibraries();
      if (var2 != null) {
         var1.addAll(Arrays.asList(var2));
      }

      AppDeploymentMBean[] var3 = var0.getInternalAppDeployments();
      if (var3 != null) {
         var1.addAll(Arrays.asList(var3));
      }

      var2 = var0.getLibraries();
      if (var2 != null) {
         var1.addAll(Arrays.asList(var2));
      }

      var3 = var0.getAppDeployments();
      if (var3 != null) {
         var1.addAll(Arrays.asList(var3));
      }

      return (AppDeploymentMBean[])((AppDeploymentMBean[])var1.toArray(new AppDeploymentMBean[var1.size()]));
   }

   public static AppDeploymentMBean lookupAppOrLib(String var0, DomainMBean var1) {
      AppDeploymentMBean var2 = var1.lookupAppDeployment(var0);
      if (var2 != null) {
         return var2;
      } else {
         LibraryMBean var3 = var1.lookupLibrary(var0);
         if (var3 != null) {
            return var3;
         } else {
            var2 = var1.lookupInternalAppDeployment(var0);
            return (AppDeploymentMBean)(var2 != null ? var2 : var1.lookupInternalLibrary(var0));
         }
      }
   }

   public static void destroyAppOrLib(AppDeploymentMBean var0, DomainMBean var1) {
      if (!var0.isInternalApp()) {
         ApplicationMBean var2 = var1.lookupApplication(var0.getName());
         if (var2 != null) {
            var1.destroyApplication(var2);
         }

         if (var0 instanceof LibraryMBean) {
            var1.destroyLibrary((LibraryMBean)var0);
         } else {
            var1.destroyAppDeployment(var0);
         }

      }
   }

   public static BasicDeploymentMBean lookupBasicDeployment(String var0, DomainMBean var1) {
      if (var0 == null) {
         return null;
      } else if (var1 == null) {
         return null;
      } else {
         BasicDeploymentMBean[] var2 = var1.getBasicDeployments();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (var0.equals(var2[var3].getName())) {
               return var2[var3];
            }
         }

         return null;
      }
   }

   public static BasicDeploymentMBean lookupBasicDeployment(String var0, long var1) {
      BasicDeploymentMBean var3 = null;
      DomainMBean var4 = DeploymentManager.getInstance(kernelId).getEditableDomainMBean(var1);
      if (var4 != null) {
         var3 = lookupBasicDeployment(var0, var4);
      }

      if (var3 == null) {
         var3 = lookupBasicDeployment(var0, ManagementService.getRuntimeAccess(kernelId).getDomain());
      }

      return var3;
   }
}
