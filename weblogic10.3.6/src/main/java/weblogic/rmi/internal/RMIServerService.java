package weblogic.rmi.internal;

import java.rmi.RemoteException;
import java.security.AccessController;
import weblogic.common.internal.RMIBootServiceImpl;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JTAMBean;
import weblogic.management.provider.ManagementService;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.rmi.provider.WorkContextAccessController;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.server.RemoteLifeCycleOperationsImpl;
import weblogic.server.ServiceFailureException;

public final class RMIServerService extends AbstractServerService {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public void start() throws ServiceFailureException {
      try {
         OIDManager.getInstance().initialize();
         ServerHelper.exportObject(new RMIBootServiceImpl());
         WorkContextAccessController.initialize();
         ServerHelper.exportObject(RemoteLifeCycleOperationsImpl.getInstance());
      } catch (RemoteException var2) {
         throw new ServiceFailureException(var2);
      }
   }

   public static int getTransactionTimeoutMillis() {
      DomainMBean var0 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      JTAMBean var1 = var0.getJTA();
      return var1.getTimeoutSeconds() * 1000;
   }
}
