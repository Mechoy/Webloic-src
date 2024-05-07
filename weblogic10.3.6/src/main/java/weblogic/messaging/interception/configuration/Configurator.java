package weblogic.messaging.interception.configuration;

import weblogic.application.ApplicationFactoryManager;
import weblogic.messaging.interception.module.InterceptionDeploymentFactory;
import weblogic.messaging.interception.module.InterceptionModuleFactory;
import weblogic.server.AbstractServerService;

public final class Configurator extends AbstractServerService {
   public Configurator() {
      ApplicationFactoryManager var1 = ApplicationFactoryManager.getApplicationFactoryManager();
      var1.addDeploymentFactory(new InterceptionDeploymentFactory());
      var1.addWblogicModuleFactory(new InterceptionModuleFactory());
   }

   public void start() {
   }
}
