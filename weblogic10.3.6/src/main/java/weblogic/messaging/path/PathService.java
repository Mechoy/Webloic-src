package weblogic.messaging.path;

import weblogic.diagnostics.image.ImageManager;
import weblogic.diagnostics.image.ImageSource;
import weblogic.diagnostics.image.ImageSourceNotFoundException;
import weblogic.management.configuration.PathServiceMBean;
import weblogic.management.utils.GenericManagedService;
import weblogic.management.utils.GenericServiceManager;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public class PathService extends AbstractServerService {
   public static final String PATH_SERVICE = "PathService";
   public static final String HASH_POLICY = "Hash";
   private static PathService singleton;
   private PathServiceAdmin pathServiceAdmin;
   private boolean registered;
   private GenericManagedService pathServiceService;
   static final String IMAGE_NAME = "PathService";
   private final ImageSource IMAGE_SOURCE = new PathServiceDiagnosticImageSource(this);

   public PathService() {
      singleton = this;
   }

   public static PathService getService() {
      return singleton;
   }

   public PathServiceAdmin getPathServiceAdmin() {
      return this.pathServiceAdmin;
   }

   public void setPathServiceAdmin(PathServiceAdmin var1) {
      this.pathServiceAdmin = var1;
   }

   public boolean isRegistered() {
      return this.registered;
   }

   public synchronized void start() throws ServiceFailureException {
      if (!this.registered) {
         GenericServiceManager var1 = GenericServiceManager.getManager();
         this.pathServiceService = var1.register(PathServiceMBean.class, PathServiceAdmin.class, true);
         this.registered = true;
      }

      this.pathServiceService.start();
      this.registerDiagnosticImageSource();
   }

   public synchronized void halt() throws ServiceFailureException {
      this.stop();
   }

   public void stop() throws ServiceFailureException {
      if (this.registered) {
         this.pathServiceService.stop();
         this.registered = false;
         this.unregisterDiagnosticImageSource();
      }
   }

   public void registerDiagnosticImageSource() {
      ImageManager var1 = ImageManager.getInstance();
      var1.registerImageSource("PathService", this.IMAGE_SOURCE);
   }

   private void unregisterDiagnosticImageSource() {
      ImageManager var1 = ImageManager.getInstance();

      try {
         var1.unregisterImageSource("PathService");
      } catch (ImageSourceNotFoundException var3) {
      }

   }
}
