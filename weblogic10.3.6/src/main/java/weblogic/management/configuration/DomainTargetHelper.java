package weblogic.management.configuration;

import java.security.AccessController;
import java.util.Arrays;
import weblogic.management.WebLogicMBean;
import weblogic.management.internal.ManagementTextTextFormatter;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public final class DomainTargetHelper {
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public static TargetMBean[] getDefaultTargets(TargetInfoMBean var0, Object var1) {
      if (var0 instanceof DomainTargetedMBean) {
         ServerMBean var2 = getLocalServer(var0);
         if (var2 != null) {
            return new TargetMBean[]{var2};
         }
      }

      return (TargetMBean[])((TargetMBean[])var1);
   }

   private static ServerMBean getLocalServer(TargetInfoMBean var0) {
      DomainMBean var1 = getDomain(var0);
      if (var1 == null) {
         return null;
      } else {
         String var2 = ManagementService.getRuntimeAccess(kernelId).getServer().getName();
         return var1.lookupServer(var2);
      }
   }

   private static DomainMBean getDomain(TargetInfoMBean var0) {
      Object var1;
      for(var1 = var0; var1 != null && !(var1 instanceof DomainMBean); var1 = ((WebLogicMBean)var1).getParent()) {
      }

      return (DomainMBean)var1;
   }

   public static void validateTargets(TargetInfoMBean var0) throws IllegalArgumentException {
      if (var0 instanceof DomainTargetedMBean) {
         throw new IllegalArgumentException(ManagementTextTextFormatter.getInstance().getCannotModifyDomainTarget(var0.getName()));
      }
   }

   public static void validateTargets(TargetInfoMBean var0, TargetMBean[] var1) throws IllegalArgumentException {
      if (var0 instanceof DomainTargetedMBean) {
         TargetMBean[] var2 = var0.getTargets();
         int var3 = var2 == null ? 0 : var2.length;
         int var4 = var1 == null ? 0 : var1.length;
         if (var3 != var4 || !Arrays.equals(var2, var1)) {
            throw new IllegalArgumentException(ManagementTextTextFormatter.getInstance().getCannotModifyDomainTarget(var0.getName()));
         }
      }

   }
}
