package weblogic.management.deploy;

import java.security.AccessController;
import java.util.Set;
import javax.management.InstanceNotFoundException;
import weblogic.management.Helper;
import weblogic.management.MBeanHome;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.DeployerRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

/** @deprecated */
public final class DeployerRuntime {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public static DeployerRuntimeMBean getDeployerRuntime(String var0, String var1, String var2) throws IllegalArgumentException, InstanceNotFoundException {
      MBeanHome var3 = Helper.getAdminMBeanHome(var0, var1, var2);
      return getDeployerRuntime(var3);
   }

   public static DeployerRuntimeMBean getDeployerRuntime(MBeanHome var0) throws IllegalArgumentException, InstanceNotFoundException {
      Set var1 = var0.getMBeansByType("DeployerRuntime");
      if (var1 != null && var1.size() == 1) {
         return (DeployerRuntimeMBean)var1.iterator().next();
      } else {
         throw new InstanceNotFoundException("Could not find the DeployerRuntime");
      }
   }

   public static DeployerRuntimeMBean getDeployerRuntime() {
      return ManagementService.getDomainAccess(kernelId).getDeployerRuntime();
   }
}
