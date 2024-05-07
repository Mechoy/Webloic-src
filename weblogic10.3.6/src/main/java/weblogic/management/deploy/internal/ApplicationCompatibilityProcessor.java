package weblogic.management.deploy.internal;

import java.io.File;
import java.security.AccessController;
import weblogic.deploy.internal.targetserver.AppDeployment;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.ApplicationMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.provider.ConfigurationProcessor;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.UpdateException;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class ApplicationCompatibilityProcessor implements ConfigurationProcessor {
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public void updateConfiguration(DomainMBean var1) throws UpdateException {
      AppDeploymentMBean[] var3 = var1.getAppDeployments();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         AppDeploymentMBean var5 = var3[var4];
         File var2;
         if (ManagementService.getRuntimeAccess(kernelId).isAdminServer()) {
            var2 = new File(var5.getAbsoluteSourcePath());
         } else {
            var2 = AppDeployment.getFile(var5);
         }

         if (var2.exists()) {
            ApplicationMBean var6 = null;

            try {
               var6 = MBeanConverter.createApplicationForAppDeployment(var1, var5, var2.getPath());
               if (var6 != null) {
                  var6.setAppDeployment(var5);
               }
            } catch (DeploymentException var9) {
               String var8 = DeploymentManagerLogger.logConfigureAppMBeanFailedLoggable(var5.getName()).getMessage();
               throw new UpdateException(var8, var9);
            }
         }
      }

   }
}
