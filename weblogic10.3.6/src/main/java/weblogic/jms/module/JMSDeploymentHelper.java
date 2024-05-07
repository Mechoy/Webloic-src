package weblogic.jms.module;

import java.security.AccessController;
import weblogic.j2ee.descriptor.wl.DistributedDestinationMemberBean;
import weblogic.management.configuration.JMSServerMBean;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class JMSDeploymentHelper {
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public static String getMemberName(String var0, DistributedDestinationMemberBean var1) {
      String var2 = var1.getPhysicalDestinationName();
      return "interop-jms".equals(var0) ? var2 : var0 + "!" + var2;
   }

   public static String getMigratableTargetName(String var0) {
      JMSServerMBean var1 = ManagementService.getRuntimeAccess(KERNEL_ID).getDomain().lookupJMSServer(var0);
      if (var1 == null) {
         return null;
      } else {
         TargetMBean[] var2 = var1.getTargets();
         if (var2 != null && var2.length != 0) {
            return !(var2[0] instanceof MigratableTargetMBean) ? null : var2[0].getName();
         } else {
            return null;
         }
      }
   }

   public static String getDomainName() {
      return ManagementService.getRuntimeAccess(KERNEL_ID) != null ? ManagementService.getRuntimeAccess(KERNEL_ID).getDomain().getName() : null;
   }
}
