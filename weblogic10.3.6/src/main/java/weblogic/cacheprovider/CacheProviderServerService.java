package weblogic.cacheprovider;

import weblogic.application.ApplicationFactoryManager;
import weblogic.cacheprovider.coherence.CoherenceClusterManager;
import weblogic.cacheprovider.coherence.CoherenceClusterSystemResourceDeploymentFactory;
import weblogic.cacheprovider.coherence.CoherenceException;
import weblogic.cacheprovider.coherence.application.CoherenceApplicationDeploymentExtensionFactory;
import weblogic.management.configuration.CoherenceClusterSystemResourceMBean;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public class CacheProviderServerService extends AbstractServerService {
   public void start() throws ServiceFailureException {
      this.initialize();
   }

   private void initialize() throws ServiceFailureException {
      this.initFactories();
      this.initCoherence();
   }

   private void initFactories() {
      ApplicationFactoryManager var1 = ApplicationFactoryManager.getApplicationFactoryManager();
      var1.addDeploymentFactory(new CoherenceClusterSystemResourceDeploymentFactory());
      var1.addAppDeploymentExtensionFactory(new CoherenceApplicationDeploymentExtensionFactory());
   }

   private void initCoherence() throws ServiceFailureException {
      CoherenceClusterManager var1 = CoherenceClusterManager.getInstance();
      ClassLoader var2 = Thread.currentThread().getContextClassLoader();
      if (var1.isCoherenceAvailable(var2)) {
         CoherenceClusterSystemResourceMBean var3 = CoherenceClusterManager.getServerMBean().getCoherenceClusterSystemResource();
         if (var3 != null) {
            try {
               var1.configureClusterService(var2, var3.getName());
            } catch (CoherenceException var5) {
               throw new ServiceFailureException(var5.getMessage(), var5);
            }
         }
      }

   }
}
