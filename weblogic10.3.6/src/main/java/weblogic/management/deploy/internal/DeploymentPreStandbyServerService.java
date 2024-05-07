package weblogic.management.deploy.internal;

import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public class DeploymentPreStandbyServerService extends AbstractServerService {
   private static final DeploymentManagerLogger logger = new DeploymentManagerLogger();

   public void start() throws ServiceFailureException {
      DeploymentServerService.init();
      DeploymentServerService.deployPreStandbyInternalApps();
   }

   public void stop() throws ServiceFailureException {
      this.halt();
   }

   public void halt() throws ServiceFailureException {
      try {
         DeploymentServerService.undeployPreStandbyInternalApps();
      } catch (Throwable var2) {
         throw new ServiceFailureException(var2);
      }
   }
}
