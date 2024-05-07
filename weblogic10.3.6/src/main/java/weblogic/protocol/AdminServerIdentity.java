package weblogic.protocol;

import java.security.AccessController;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public final class AdminServerIdentity {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static ServerIdentity adminIdentity;

   public static ServerIdentity getIdentity() {
      return ManagementService.getRuntimeAccess(kernelId).isAdminServer() ? LocalServerIdentity.getIdentity() : ServerIdentityManager.findServerIdentity(ManagementService.getRuntimeAccess(kernelId).getDomainName(), ManagementService.getRuntimeAccess(kernelId).getAdminServerName());
   }

   public static ServerIdentity getBootstrapIdentity() {
      return ManagementService.getRuntimeAccess(kernelId).isAdminServer() ? LocalServerIdentity.getIdentity() : adminIdentity;
   }

   public static void setBootstrapIdentity(ServerIdentity var0) {
      adminIdentity = var0;
   }
}
