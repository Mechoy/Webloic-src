package weblogic.management.deploy.classdeployment;

import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public class ShutdownClassDeploymentService extends AbstractServerService {
   private boolean shutdown;

   public void stop() throws ServiceFailureException {
      this.halt();
   }

   public void halt() throws ServiceFailureException {
      synchronized(this) {
         if (this.shutdown) {
            return;
         }

         this.shutdown = true;
      }

      ClassDeploymentManager.getInstance().runShutdownClasses();
   }
}
