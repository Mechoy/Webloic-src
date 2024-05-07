package weblogic.management.provider.internal;

import java.security.AccessController;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public class DomainAccessService extends AbstractServerService {
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public void start() throws ServiceFailureException {
      if (ManagementService.getRuntimeAccess(kernelId).isAdminServer()) {
         DomainAccessImpl var1 = new DomainAccessImpl();
         ManagementService.initializeDomain(var1);
         var1.initializeDomainRuntimeMBean();
      }
   }
}
