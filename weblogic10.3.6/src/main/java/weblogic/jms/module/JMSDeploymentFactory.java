package weblogic.jms.module;

import java.io.File;
import java.io.IOException;
import weblogic.application.Deployment;
import weblogic.application.DeploymentFactory;
import weblogic.jms.common.JMSDebug;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.JMSSystemResourceMBean;
import weblogic.management.configuration.SystemResourceMBean;

public final class JMSDeploymentFactory implements DeploymentFactory {
   private boolean isJMS(File var1) throws IOException {
      if (var1.isDirectory()) {
         return false;
      } else {
         return var1.getName().endsWith("-jms.xml");
      }
   }

   public Deployment createDeployment(AppDeploymentMBean var1, File var2) throws DeploymentException {
      try {
         if (!this.isJMS(var2)) {
            return null;
         } else {
            if (JMSDebug.JMSModule.isDebugEnabled()) {
               JMSDebug.JMSModule.debug("Creating a standalone JMS Module with file : " + var2.getAbsolutePath());
            }

            return new JMSDeployment(var1, var2);
         }
      } catch (IOException var4) {
         throw new DeploymentException(var4);
      }
   }

   public Deployment createDeployment(SystemResourceMBean var1, File var2) throws DeploymentException {
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("Creating a system resource JMS Module with file : " + var2.getAbsolutePath());
      }

      return var1 instanceof JMSSystemResourceMBean ? new JMSDeployment(var1, var2) : null;
   }
}
