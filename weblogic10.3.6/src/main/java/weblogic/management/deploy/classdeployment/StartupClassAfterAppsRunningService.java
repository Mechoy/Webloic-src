package weblogic.management.deploy.classdeployment;

import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public class StartupClassAfterAppsRunningService extends AbstractServerService {
   public void start() throws ServiceFailureException {
      ClassDeploymentManager.getInstance().runStartupsAfterAppsRunning();
   }
}
