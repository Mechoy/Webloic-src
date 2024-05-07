package weblogic.management.provider.internal;

import java.security.AccessController;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;
import weblogic.work.WorkManagerFactory;

public class WorkManagerService extends AbstractServerService {
   public static final String ADMIN_RMI_QUEUE = "weblogic.admin.RMI";

   public void start() throws ServiceFailureException {
      byte var1 = 2;
      AuthenticatedSubject var2 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      ServerMBean var3 = ManagementService.getRuntimeAccess(var2).getServer();
      if (var3.getUse81StyleExecuteQueues()) {
         var1 = 5;
      }

      WorkManagerFactory.getInstance().findOrCreate("weblogic.admin.RMI", -1, var1, -1);
   }
}
