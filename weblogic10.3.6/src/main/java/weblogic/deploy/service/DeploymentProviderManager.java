package weblogic.deploy.service;

import java.util.Set;

public interface DeploymentProviderManager {
   void registerDeploymentProvider(DeploymentProvider var1);

   Set getRegisteredDeploymentProviders();
}
