package weblogic.cacheprovider.coherence;

import java.io.File;
import weblogic.application.Deployment;
import weblogic.application.DeploymentFactory;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.CoherenceClusterSystemResourceMBean;
import weblogic.management.configuration.SystemResourceMBean;

public final class CoherenceClusterSystemResourceDeploymentFactory implements DeploymentFactory {
   public Deployment createDeployment(AppDeploymentMBean var1, File var2) throws DeploymentException {
      return null;
   }

   public Deployment createDeployment(SystemResourceMBean var1, File var2) throws DeploymentException {
      return var1 instanceof CoherenceClusterSystemResourceMBean ? new CoherenceClusterSystemResourceDeployment(var1, var2) : null;
   }
}
