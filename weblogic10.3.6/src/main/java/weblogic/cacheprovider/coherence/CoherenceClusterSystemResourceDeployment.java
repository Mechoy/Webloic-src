package weblogic.cacheprovider.coherence;

import java.io.File;
import weblogic.application.Deployment;
import weblogic.application.Module;
import weblogic.application.internal.SingleModuleDeployment;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.SystemResourceMBean;

public class CoherenceClusterSystemResourceDeployment extends SingleModuleDeployment implements Deployment {
   public CoherenceClusterSystemResourceDeployment(SystemResourceMBean var1, File var2) throws DeploymentException {
      super(var1, createModule(var1), var2);
   }

   private static Module createModule(SystemResourceMBean var0) throws DeploymentException {
      String var1 = var0.getDescriptorFileName();
      if (var1 == null) {
         throw new DeploymentException("CoherenceClusterSystemResource " + ApplicationVersionUtils.getDisplayName((BasicDeploymentMBean)var0) + " does not have a descriptor file name");
      } else {
         return new CoherenceClusterModule(var1);
      }
   }
}
