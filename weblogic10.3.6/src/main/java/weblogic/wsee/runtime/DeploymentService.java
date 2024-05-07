package weblogic.wsee.runtime;

import weblogic.application.ApplicationFactoryManager;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;
import weblogic.wsee.deploy.AppDeploymentExtensionFactory;
import weblogic.wsee.runtime.owsm.OwsmSchedulerHelper;

public class DeploymentService extends AbstractServerService {
   public void start() throws ServiceFailureException {
      OwsmSchedulerHelper.setSchedulerIfRequired();
      ApplicationFactoryManager.getApplicationFactoryManager().addAppDeploymentExtensionFactory(AppDeploymentExtensionFactory.INSTANCE);
   }
}
