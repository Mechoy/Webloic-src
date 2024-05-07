package weblogic.deploy.service;

public interface DeploymentProvider {
   String getIdentity();

   void addDeploymentsTo(DeploymentRequest var1, ConfigurationContext var2);
}
