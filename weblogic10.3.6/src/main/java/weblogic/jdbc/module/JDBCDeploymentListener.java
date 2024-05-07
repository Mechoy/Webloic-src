package weblogic.jdbc.module;

import weblogic.application.ApplicationAccess;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.Module;
import weblogic.application.ModuleWrapper;
import weblogic.deploy.event.DeploymentVetoException;
import weblogic.deploy.event.VetoableDeploymentEvent;
import weblogic.deploy.event.VetoableDeploymentListener;
import weblogic.management.configuration.BasicDeploymentMBean;

public class JDBCDeploymentListener implements VetoableDeploymentListener {
   public void vetoableApplicationActivate(VetoableDeploymentEvent var1) throws DeploymentVetoException {
   }

   public void vetoableApplicationDeploy(VetoableDeploymentEvent var1) throws DeploymentVetoException {
   }

   public void vetoableApplicationUndeploy(VetoableDeploymentEvent var1) throws DeploymentVetoException {
      Object var2 = var1.getSystemResource();
      if (var2 == null) {
         var2 = var1.getAppDeployment();
      }

      if (var2 != null) {
         ApplicationContextInternal var3 = ApplicationAccess.getApplicationAccess().getApplicationContext(((BasicDeploymentMBean)var2).getName());
         if (var3 != null) {
            Module[] var4 = var3.getApplicationModules();
            if (var4 != null) {
               for(int var5 = 0; var5 < var4.length; ++var5) {
                  if (var4[var5] instanceof ModuleWrapper) {
                     Module var6 = ((ModuleWrapper)var4[var5]).unwrap();
                     if (var6 instanceof JDBCModule) {
                        ((JDBCModule)var6).checkDependencies();
                     }
                  }
               }

            }
         }
      }
   }
}
