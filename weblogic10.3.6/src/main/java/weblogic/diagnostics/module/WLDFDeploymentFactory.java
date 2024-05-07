package weblogic.diagnostics.module;

import java.io.File;
import weblogic.application.Deployment;
import weblogic.application.DeploymentFactory;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.SystemResourceMBean;
import weblogic.management.configuration.WLDFSystemResourceMBean;

public class WLDFDeploymentFactory implements DeploymentFactory {
   private static WLDFDeploymentFactory singleton = null;

   public static synchronized WLDFDeploymentFactory getInstance() {
      if (singleton == null) {
         singleton = new WLDFDeploymentFactory();
      }

      return singleton;
   }

   private WLDFDeploymentFactory() {
   }

   public Deployment createDeployment(AppDeploymentMBean var1, File var2) throws DeploymentException {
      return null;
   }

   public Deployment createDeployment(SystemResourceMBean var1, File var2) throws DeploymentException {
      if (!(var1 instanceof WLDFSystemResourceMBean)) {
         return null;
      } else {
         WLDFSystemResourceMBean var3 = (WLDFSystemResourceMBean)var1;
         return new WLDFDeployment(var3, var2);
      }
   }
}
