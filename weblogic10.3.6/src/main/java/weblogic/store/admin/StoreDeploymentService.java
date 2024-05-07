package weblogic.store.admin;

import java.security.AccessController;
import weblogic.diagnostics.image.ImageManager;
import weblogic.diagnostics.image.ImageSource;
import weblogic.diagnostics.image.ImageSourceNotFoundException;
import weblogic.management.configuration.FileStoreMBean;
import weblogic.management.configuration.JDBCStoreMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.utils.GenericManagedService;
import weblogic.management.utils.GenericServiceManager;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;
import weblogic.store.common.StoreDebug;

public class StoreDeploymentService extends AbstractServerService {
   static final String IMAGE_NAME = "PERSISTENT_STORE";
   private final ImageSource IMAGE_SOURCE = new PersistentStoreImageSource();
   private boolean registered;
   private GenericManagedService fileStoreService;
   private GenericManagedService jdbcStoreService;

   public synchronized void start() throws ServiceFailureException {
      StoreDebug.storeAdmin.debug("StoreService starting");
      if (!this.registered) {
         AuthenticatedSubject var1 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
         ManagementService.getRuntimeAccess(var1).addAccessCallbackClass(StoreCompatibilityUpgrader.class.getName());
         GenericServiceManager var2 = GenericServiceManager.getManager();
         this.fileStoreService = var2.register(FileStoreMBean.class, FileAdminHandler.class, true);
         this.jdbcStoreService = var2.register(JDBCStoreMBean.class, JDBCAdminHandler.class, true);
         this.registered = true;
      }

      this.fileStoreService.start();
      this.jdbcStoreService.start();
      this.registerDiagnosticImageSource();
   }

   public synchronized void halt() throws ServiceFailureException {
      this.fileStoreService.stop();
      this.jdbcStoreService.stop();
      this.unregisterDiagnosticImageSource();
   }

   public void stop() throws ServiceFailureException {
      this.halt();
   }

   private void registerDiagnosticImageSource() {
      ImageManager var1 = ImageManager.getInstance();
      var1.registerImageSource("PERSISTENT_STORE", this.IMAGE_SOURCE);
   }

   private void unregisterDiagnosticImageSource() {
      ImageManager var1 = ImageManager.getInstance();

      try {
         var1.unregisterImageSource("PERSISTENT_STORE");
      } catch (ImageSourceNotFoundException var3) {
      }

   }
}
