package weblogic.management.extension.internal;

import java.io.File;
import weblogic.application.Deployment;
import weblogic.application.DeploymentFactory;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.CustomResourceMBean;
import weblogic.management.configuration.SystemResourceMBean;

public class CustomResourceDeploymentFactory implements DeploymentFactory {
   public Deployment createDeployment(AppDeploymentMBean var1, File var2) throws DeploymentException {
      return null;
   }

   public Deployment createDeployment(SystemResourceMBean var1, File var2) throws DeploymentException {
      if (!(var1 instanceof CustomResourceMBean)) {
         return null;
      } else {
         CustomResourceMBean var3 = (CustomResourceMBean)var1;
         return new CustomResourceDeployment(var3, var2);
      }
   }
}
