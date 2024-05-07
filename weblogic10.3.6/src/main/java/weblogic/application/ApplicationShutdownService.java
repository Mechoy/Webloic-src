package weblogic.application;

import java.security.AccessController;
import java.util.Iterator;
import java.util.List;
import weblogic.application.internal.AppClientModuleFactory;
import weblogic.application.internal.CarDeploymentFactory;
import weblogic.application.internal.EarDeploymentFactory;
import weblogic.application.internal.OptionalPackageProviderImpl;
import weblogic.application.internal.library.EarLibraryFactory;
import weblogic.application.internal.library.JarLibraryFactory;
import weblogic.application.internal.library.LibraryDeploymentFactory;
import weblogic.application.library.LibraryDeploymentListener;
import weblogic.deploy.event.DeploymentEventManager;
import weblogic.diagnostics.image.ImageManager;
import weblogic.kernel.Kernel;
import weblogic.kernel.T3SrvrLogger;
import weblogic.management.DeploymentException;
import weblogic.management.deploy.internal.DeploymentServerService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;
import weblogic.utils.OptionalPackageProvider;

public final class ApplicationShutdownService extends AbstractServerService {
   private Object syncObj = new Object();
   private boolean shutdown;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public void start() throws ServiceFailureException {
      ApplicationFactoryManager var1 = ApplicationFactoryManager.getApplicationFactoryManager();
      var1.addDeploymentFactory(new EarDeploymentFactory());
      var1.addDeploymentFactory(new CarDeploymentFactory());
      var1.addLibraryFactory(new EarLibraryFactory());
      var1.addDefaultLibraryFactory(new JarLibraryFactory());
      var1.addModuleFactory(new AppClientModuleFactory());
      var1.addDeploymentFactoryFirst(new LibraryDeploymentFactory());

      try {
         DeploymentEventManager.addVetoableDeploymentListener(new LibraryDeploymentListener());
      } catch (DeploymentException var3) {
         throw new ServiceFailureException(var3);
      }

      OptionalPackageProvider.set(new OptionalPackageProviderImpl());
      ImageManager.getInstance().registerImageSource("APPLICATION", new ApplicationManagerImageSource());
   }

   public void stop() throws ServiceFailureException {
      while(true) {
         if (this.checkPendingWorkInQueues()) {
            try {
               synchronized(this.syncObj) {
                  this.syncObj.wait(30000L);
                  continue;
               }
            } catch (InterruptedException var4) {
            }
         }

         this.shutdown = true;
         DeploymentServerService.shutdownHelper();
         return;
      }
   }

   private boolean checkPendingWorkInQueues() {
      List var1 = Kernel.getApplicationDispatchPolicies();
      boolean var2 = false;
      if (var1 != null) {
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            int var5 = Kernel.getPendingTasksCount(var4);
            if (var5 > 0) {
               T3SrvrLogger.logPendingWorkInQueues(var4, var5);
               var2 = true;
            }
         }
      }

      return var2;
   }

   public void halt() throws ServiceFailureException {
      if (!this.shutdown) {
         DeploymentServerService.shutdownHelper();
      }
   }
}
