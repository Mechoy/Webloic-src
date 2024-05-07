package weblogic.ejb.container.deployer;

import java.io.File;
import java.io.IOException;
import weblogic.application.ComponentMBeanFactory;
import weblogic.application.Deployment;
import weblogic.application.DeploymentFactory;
import weblogic.application.internal.BaseComponentMBeanFactory;
import weblogic.ejb.spi.EJBJarUtils;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.ApplicationMBean;
import weblogic.management.configuration.ComponentMBean;
import weblogic.management.configuration.SystemResourceMBean;
import weblogic.utils.Debug;

public final class EJBDeploymentFactory extends BaseComponentMBeanFactory implements DeploymentFactory, ComponentMBeanFactory {
   private void s(String var1) {
      Debug.say("\n '" + var1 + "'\n");
   }

   private boolean isEJB(File var1) throws IOException {
      return EJBJarUtils.isEJB(var1);
   }

   public Deployment createDeployment(AppDeploymentMBean var1, File var2) throws DeploymentException {
      try {
         return this.isEJB(var2) ? new EJBDeployment(var1, var2) : null;
      } catch (IOException var4) {
         throw new DeploymentException(var4);
      }
   }

   public Deployment createDeployment(SystemResourceMBean var1, File var2) throws DeploymentException {
      return null;
   }

   public ComponentMBean[] findOrCreateComponentMBeans(ApplicationMBean var1, File var2, AppDeploymentMBean var3) throws DeploymentException {
      try {
         if (!this.isEJB(var2)) {
            return null;
         } else {
            String var4 = var2.getName();
            String var5 = this.removeExtension(var4);
            if (var3 != null) {
               var5 = this.getCompatibilityName(var5, var3);
            }

            return new ComponentMBean[]{this.findOrCreateComponentMBean(EJB_COMP, var1, var5, var4)};
         }
      } catch (IOException var6) {
         throw new DeploymentException(var6);
      }
   }
}
