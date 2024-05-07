package weblogic.management.provider.internal;

import java.io.IOException;
import java.security.AccessController;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public class ConfigBackupService extends AbstractServerService {
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public void start() throws ServiceFailureException {
      RuntimeAccess var1 = ManagementService.getRuntimeAccess(kernelId);
      if (var1.isAdminServer()) {
         try {
            if (var1.getDomain().isConfigBackupEnabled()) {
               ConfigBackup.saveBooted();
            }
         } catch (IOException var3) {
         }
      }

   }
}
