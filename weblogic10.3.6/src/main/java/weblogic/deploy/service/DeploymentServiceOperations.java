package weblogic.deploy.service;

import weblogic.management.runtime.DeploymentRequestTaskRuntimeMBean;

public interface DeploymentServiceOperations {
   void register(Version var1, DeploymentServiceCallbackHandler var2) throws RegistrationExistsException;

   DeploymentRequestTaskRuntimeMBean deploy(DeploymentRequest var1) throws RequiresTaskMediatedStartException;

   DeploymentRequestTaskRuntimeMBean startDeploy(DeploymentRequest var1);

   void cancel(DeploymentRequest var1);

   void unregister(String var1);
}
