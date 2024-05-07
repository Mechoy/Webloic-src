package weblogic.connector.deploy;

import java.io.File;
import weblogic.application.Deployment;
import weblogic.application.Module;
import weblogic.application.internal.SingleModuleDeployment;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.connector.common.Debug;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.ComponentMBean;

public final class ConnectorDeployment extends SingleModuleDeployment implements Deployment {
   public ConnectorDeployment(AppDeploymentMBean var1, File var2) throws DeploymentException {
      super(var1, createModule(var1), var2);
   }

   private static Module createModule(AppDeploymentMBean var0) throws DeploymentException {
      ComponentMBean[] var1 = var0.getAppMBean().getComponents();
      String var2;
      if (var1 != null && var1.length != 0) {
         if (var1.length > 1) {
            var2 = Debug.getExceptionMoreThanOneComponent(ApplicationVersionUtils.getDisplayName((BasicDeploymentMBean)var0));
            throw new DeploymentException(var2);
         } else {
            return new ConnectorModule(var1[0].getURI());
         }
      } else {
         var2 = Debug.getExceptionNoComponents(ApplicationVersionUtils.getDisplayName((BasicDeploymentMBean)var0));
         throw new DeploymentException(var2);
      }
   }
}
