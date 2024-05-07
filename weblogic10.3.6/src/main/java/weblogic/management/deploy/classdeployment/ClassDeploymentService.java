package weblogic.management.deploy.classdeployment;

import weblogic.management.internal.DeploymentHandlerHome;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public class ClassDeploymentService extends AbstractServerService {
   public void start() throws ServiceFailureException {
      DeploymentHandlerHome.addDeploymentHandler(ClassDeploymentManager.getInstance());
      ClassDeploymentManager.getInstance().runStartupsBeforeAppDeployments();
   }
}
