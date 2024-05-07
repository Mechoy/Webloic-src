package weblogic.management.provider.internal;

import java.security.AccessController;
import weblogic.version;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ConfigurationProcessor;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class ManagementConfigProcessor implements ConfigurationProcessor {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public void updateConfiguration(DomainMBean var1) {
      String var2 = version.getReleaseBuildVersion();
      var1.setConfigurationVersion(var2);
      ServerMBean[] var3 = var1.getServers();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         var3[var4].unSet("ServerVersion");
      }

      if (ManagementService.getPropertyService(kernelId).isAdminServer()) {
         var1.setAdminServerName(ManagementService.getPropertyService(kernelId).getServerName());
      }
   }
}
