package weblogic.application.internal;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;
import weblogic.application.Deployment;
import weblogic.application.DeploymentFactory;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.SystemResourceMBean;

public final class CarDeploymentFactory implements DeploymentFactory {
   private boolean isCar(File var1) {
      if (var1.isDirectory()) {
         return (new File(var1, "META-INF/application-client.xml")).exists();
      } else if (var1.getName().endsWith(".jar")) {
         JarFile var2 = null;

         boolean var4;
         try {
            var2 = new JarFile(var1);
            boolean var3 = var2.getEntry("META-INF/application-client.xml") != null;
            return var3;
         } catch (IOException var14) {
            var4 = false;
         } finally {
            try {
               if (var2 != null) {
                  var2.close();
               }
            } catch (IOException var13) {
            }

         }

         return var4;
      } else {
         return false;
      }
   }

   public Deployment createDeployment(AppDeploymentMBean var1, File var2) throws DeploymentException {
      return this.isCar(var2) ? new CarDeployment(var1, var2) : null;
   }

   public Deployment createDeployment(SystemResourceMBean var1, File var2) throws DeploymentException {
      return null;
   }
}
