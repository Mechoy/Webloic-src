package weblogic.connector.deploy;

import java.io.File;
import weblogic.application.ComponentMBeanFactory;
import weblogic.application.Deployment;
import weblogic.application.DeploymentFactory;
import weblogic.application.internal.BaseComponentMBeanFactory;
import weblogic.connector.utils.RarUtils;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.ApplicationMBean;
import weblogic.management.configuration.ComponentMBean;
import weblogic.management.configuration.SystemResourceMBean;

public final class ConnectorDeploymentFactory extends BaseComponentMBeanFactory implements DeploymentFactory, ComponentMBeanFactory {
   public Deployment createDeployment(AppDeploymentMBean var1, File var2) throws DeploymentException {
      return RarUtils.isRar(var2) ? new ConnectorDeployment(var1, var2) : null;
   }

   public Deployment createDeployment(SystemResourceMBean var1, File var2) throws DeploymentException {
      return null;
   }

   public ComponentMBean[] findOrCreateComponentMBeans(ApplicationMBean var1, File var2, AppDeploymentMBean var3) throws DeploymentException {
      if (!RarUtils.isRar(var2)) {
         return null;
      } else {
         String var4 = var2.getName();
         String var5 = this.removeExtension(var4);
         if (var3 != null) {
            var5 = this.getCompatibilityName(var5, var3);
         }

         return new ComponentMBean[]{this.findOrCreateComponentMBean(CONNECTOR_COMP, var1, var5, var4)};
      }
   }
}
