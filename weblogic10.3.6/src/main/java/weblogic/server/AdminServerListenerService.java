package weblogic.server;

import java.security.AccessController;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.utils.AdminServerListener;

public final class AdminServerListenerService extends AbstractServerService {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static boolean started;

   public void start() throws ServiceFailureException {
      if (!ManagementService.getRuntimeAccess(kernelId).isAdminServer()) {
         this.initializeAdminServerListener();
         started = true;
      }

   }

   private void initializeAdminServerListener() throws ServiceFailureException {
      try {
         AdminServerListener.start(ManagementService.getRuntimeAccess(kernelId).isAdminServerAvailable());
      } catch (Exception var2) {
         throw new ServiceFailureException(var2.getMessage(), var2);
      }
   }

   public static boolean isAdminServerAvailable() {
      if (!started) {
         throw new IllegalStateException("AdminServerListenerService is not yet started !");
      } else {
         return AdminServerListener.isAdminServerAvailable();
      }
   }
}
