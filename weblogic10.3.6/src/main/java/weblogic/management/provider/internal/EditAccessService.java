package weblogic.management.provider.internal;

import java.security.AccessController;
import weblogic.management.provider.EditAccess;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.ManagementServiceRestricted;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public class EditAccessService extends AbstractServerService {
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public void start() throws ServiceFailureException {
      if (ManagementService.getRuntimeAccess(kernelId).isAdminServer() || !ManagementService.getRuntimeAccess(kernelId).isAdminServerAvailable()) {
         EditAccessImpl.initialize();
      }
   }

   public void stop() {
      EditAccess var1 = ManagementServiceRestricted.getEditAccess(kernelId);
      if (var1 != null) {
         var1.shutdown();
      }

   }
}
