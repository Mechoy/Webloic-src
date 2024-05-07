package weblogic.messaging.interception.module;

import java.io.File;
import weblogic.application.Deployment;
import weblogic.application.DeploymentFactory;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.SystemResourceMBean;

public final class InterceptionDeploymentFactory implements DeploymentFactory {
   private boolean isInterception(File var1) {
      if (var1.isDirectory()) {
         return false;
      } else {
         return var1.getName().endsWith("-interception.xml");
      }
   }

   public Deployment createDeployment(AppDeploymentMBean var1, File var2) throws DeploymentException {
      return !this.isInterception(var2) ? null : new InterceptionDeployment(var1, var2);
   }

   public Deployment createDeployment(SystemResourceMBean var1, File var2) throws DeploymentException {
      return null;
   }
}
