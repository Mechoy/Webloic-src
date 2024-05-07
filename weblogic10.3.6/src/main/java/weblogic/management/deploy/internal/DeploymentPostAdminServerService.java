package weblogic.management.deploy.internal;

import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public class DeploymentPostAdminServerService extends AbstractServerService {
   private static final DeploymentManagerLogger logger = new DeploymentManagerLogger();

   public void start() throws ServiceFailureException {
      try {
         ConfiguredDeployments.getConfigureDeploymentsHandler().adminToProduction();
      } catch (Throwable var2) {
         throw new ServiceFailureException(var2);
      }

      DeploymentServerService.startAutoDeploymentPoller();
   }

   public void stop() throws ServiceFailureException {
      try {
         ConfiguredDeployments.getConfigureDeploymentsHandler().productionToAdmin(true);
      } catch (Throwable var2) {
         throw new ServiceFailureException(var2);
      }
   }

   public void halt() throws ServiceFailureException {
      try {
         ConfiguredDeployments.getConfigureDeploymentsHandler().productionToAdmin(false);
      } catch (Throwable var2) {
         throw new ServiceFailureException(var2);
      }
   }
}
