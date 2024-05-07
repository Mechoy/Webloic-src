package weblogic.cluster.singleton;

import java.security.AccessController;
import weblogic.cluster.migration.MigrationManager;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.ServerService;
import weblogic.server.ServiceFailureException;

public class PreAdminSingletonServicesService implements ServerService {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public String getName() {
      return "Pre Admin Singleton Services Service";
   }

   public String getVersion() {
      return "1.0";
   }

   public synchronized void start() throws ServiceFailureException {
   }

   public void stop() throws ServiceFailureException {
      this.halt();
   }

   public void halt() throws ServiceFailureException {
      MigrationManager.singleton().handlePriorityShutDownTasks();
   }
}
