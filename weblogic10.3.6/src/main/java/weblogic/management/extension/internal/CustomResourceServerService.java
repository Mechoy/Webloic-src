package weblogic.management.extension.internal;

import weblogic.application.ApplicationFactoryManager;
import weblogic.server.ServerService;
import weblogic.server.ServiceFailureException;

public class CustomResourceServerService implements ServerService {
   public String getName() {
      return "CustomResourceServerService";
   }

   public String getVersion() {
      return "1.0.0.0";
   }

   public void start() throws ServiceFailureException {
      ApplicationFactoryManager var1 = ApplicationFactoryManager.getApplicationFactoryManager();
      var1.addDeploymentFactory(new CustomResourceDeploymentFactory());
   }

   public void stop() throws ServiceFailureException {
   }

   public void halt() throws ServiceFailureException {
   }
}
