package weblogic.store.admin;

import java.security.AccessController;
import weblogic.management.DeploymentException;
import weblogic.management.UndeploymentException;
import weblogic.management.configuration.DeploymentMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;
import weblogic.store.common.StoreDebug;

public class DefaultStoreService extends AbstractServerService {
   private boolean running;
   private FileAdminHandler defaultStoreHandler;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public synchronized void start() throws ServiceFailureException {
      StoreDebug.storeAdmin.debug("DefaultStoreService starting");
      this.running = true;

      try {
         if (this.defaultStoreHandler == null) {
            ServerMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServer();
            this.defaultStoreHandler = new FileAdminHandler();
            this.defaultStoreHandler.prepareDefaultStore(var1, true);
         }

         this.defaultStoreHandler.activate((DeploymentMBean)null);
      } catch (DeploymentException var2) {
         throw new ServiceFailureException(var2);
      }
   }

   public synchronized void halt() throws ServiceFailureException {
      if (this.running) {
         StoreDebug.storeAdmin.debug("DefaultStoreService suspending");
         this.running = false;

         try {
            if (this.defaultStoreHandler != null) {
               this.defaultStoreHandler.deactivate((DeploymentMBean)null);
            }

         } catch (UndeploymentException var2) {
            throw new ServiceFailureException(var2);
         }
      }
   }

   public void stop() throws ServiceFailureException {
      this.halt();
   }
}
