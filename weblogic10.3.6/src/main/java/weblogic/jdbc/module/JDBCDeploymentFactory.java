package weblogic.jdbc.module;

import java.io.File;
import java.io.IOException;
import weblogic.application.Deployment;
import weblogic.application.DeploymentFactory;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.JDBCSystemResourceMBean;
import weblogic.management.configuration.SystemResourceMBean;

public final class JDBCDeploymentFactory implements DeploymentFactory {
   private boolean isJDBC(File var1) throws IOException {
      if (var1.isDirectory()) {
         return false;
      } else {
         return var1.getName().endsWith("-jdbc.xml");
      }
   }

   public Deployment createDeployment(AppDeploymentMBean var1, File var2) throws DeploymentException {
      try {
         return this.isJDBC(var2) ? new JDBCDeployment(var1, var2) : null;
      } catch (IOException var4) {
         throw new DeploymentException(var4);
      }
   }

   public Deployment createDeployment(SystemResourceMBean var1, File var2) throws DeploymentException {
      return var1 instanceof JDBCSystemResourceMBean ? new JDBCDeployment(var1, var2) : null;
   }
}
