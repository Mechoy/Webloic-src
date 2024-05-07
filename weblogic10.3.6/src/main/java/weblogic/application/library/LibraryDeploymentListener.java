package weblogic.application.library;

import java.security.AccessController;
import java.util.HashSet;
import weblogic.application.utils.LibraryUtils;
import weblogic.deploy.event.DeploymentVetoException;
import weblogic.deploy.event.VetoableDeploymentEvent;
import weblogic.deploy.event.VetoableDeploymentListener;
import weblogic.management.configuration.LibraryMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.LibraryRuntimeMBean;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class LibraryDeploymentListener implements VetoableDeploymentListener {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public void vetoableApplicationActivate(VetoableDeploymentEvent var1) throws DeploymentVetoException {
      if (var1.getAppDeployment() instanceof LibraryMBean) {
         if (!var1.isNewAppDeployment() && !var1.isStaticAppDeployment()) {
            LibraryMBean var2 = (LibraryMBean)var1.getAppDeployment();
            if (LibraryUtils.isDebugOn()) {
               LibraryUtils.debug("Got Library deploy notification: " + var2.getName());
            }

            ServerRuntimeMBean var3 = ManagementService.getRuntimeAccess(kernelId).getServerRuntime();
            if (this.verifyServerName(var1, var3, var2)) {
               this.verifyLibrary(var2, var3);
            }
         } else {
            if (LibraryUtils.isDebugOn()) {
               LibraryUtils.debug("First or static deploy " + var1.getAppDeployment().getName());
            }

         }
      }
   }

   public void vetoableApplicationDeploy(VetoableDeploymentEvent var1) throws DeploymentVetoException {
      this.vetoableApplicationActivate(var1);
   }

   public void vetoableApplicationUndeploy(VetoableDeploymentEvent var1) throws DeploymentVetoException {
      if (var1.getAppDeployment() instanceof LibraryMBean) {
         LibraryMBean var2 = (LibraryMBean)var1.getAppDeployment();
         if (LibraryUtils.isDebugOn()) {
            LibraryUtils.debug("Got Library undeploy notification: " + var2.getName());
         }

         ServerRuntimeMBean var3 = ManagementService.getRuntimeAccess(kernelId).getServerRuntime();
         if (this.verifyServerName(var1, var3, var2)) {
            this.verifyLibrary(var2, var3);
         }
      }
   }

   private boolean verifyServerName(VetoableDeploymentEvent var1, ServerRuntimeMBean var2, LibraryMBean var3) {
      String[] var4 = var1.getTargets();
      if (var4 == null) {
         var4 = this.getAppTargets(var3.getTargets());
      }

      for(int var5 = 0; var5 < var4.length; ++var5) {
         if (var2.getName().equals(var4[var5])) {
            return true;
         }
      }

      return false;
   }

   private void verifyLibrary(LibraryMBean var1, ServerRuntimeMBean var2) throws DeploymentVetoException {
      String var3 = var1.getName();
      if (LibraryUtils.isDebugOn()) {
         LibraryUtils.debug("Trying to get libruntimembean for lib " + var3);
      }

      LibraryRuntimeMBean var4 = var2.lookupLibraryRuntime(var3);
      if (var4 != null) {
         if (LibraryUtils.isDebugOn()) {
            LibraryUtils.debug("is " + var4.getName() + " used? " + var4.isReferenced());
         }

         if (var4.isReferenced()) {
            throw new DeploymentVetoException(this.formatErrorMessage(var4, var2.getName()));
         }
      }
   }

   private String formatErrorMessage(LibraryRuntimeMBean var1, String var2) {
      StringBuffer var3 = new StringBuffer();
      var3.append("Cannot undeploy library ").append(LibraryUtils.toString(var1.getLibraryName(), var1.getSpecificationVersion(), var1.getImplementationVersion())).append(" from server ").append(var2).append(", because the following deployed applications reference it: ");
      String[] var4 = var1.getReferencingNames();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         var3.append(var4[var5]);
         if (var5 < var4.length - 1) {
            var3.append(", ");
         }
      }

      return var3.toString();
   }

   private String[] getAppTargets(TargetMBean[] var1) {
      HashSet var2 = new HashSet();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2.addAll(var1[var3].getServerNames());
      }

      if (LibraryUtils.isDebugOn()) {
         LibraryUtils.debug("Targets from TargetMBean: " + var2);
      }

      return (String[])((String[])var2.toArray(new String[var2.size()]));
   }
}
