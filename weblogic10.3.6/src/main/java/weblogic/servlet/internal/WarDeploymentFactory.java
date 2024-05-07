package weblogic.servlet.internal;

import java.io.File;
import java.io.IOException;
import weblogic.application.ComponentMBeanFactory;
import weblogic.application.Deployment;
import weblogic.application.DeploymentFactory;
import weblogic.application.internal.BaseComponentMBeanFactory;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.ApplicationMBean;
import weblogic.management.configuration.ComponentMBean;
import weblogic.management.configuration.SystemResourceMBean;
import weblogic.servlet.utils.WarUtils;

public final class WarDeploymentFactory extends BaseComponentMBeanFactory implements DeploymentFactory, ComponentMBeanFactory {
   public Deployment createDeployment(AppDeploymentMBean var1, File var2) throws DeploymentException {
      try {
         return WarUtils.isWar(var2) ? new WarDeployment(var1, var2) : null;
      } catch (IOException var4) {
         throw new DeploymentException(var4);
      }
   }

   public Deployment createDeployment(SystemResourceMBean var1, File var2) throws DeploymentException {
      return null;
   }

   public ComponentMBean[] findOrCreateComponentMBeans(ApplicationMBean var1, File var2, AppDeploymentMBean var3) throws DeploymentException {
      try {
         if (!WarUtils.isWar(var2)) {
            return null;
         } else {
            String var4 = var2.getName();
            String var5 = this.removeExtension(var4);
            if (var3 != null) {
               var5 = this.getCompatibilityName(var5, var3);
            }

            ComponentMBeanFactory.MBeanFactory var6 = WarUtils.isWebServices(var2) ? WEB_SERVICE_COMP : WEB_COMP;
            ComponentMBean var7 = this.findOrCreateComponentMBean(var6, var1, var5, var4);
            return new ComponentMBean[]{var7};
         }
      } catch (IOException var8) {
         throw new DeploymentException(var8);
      }
   }
}
