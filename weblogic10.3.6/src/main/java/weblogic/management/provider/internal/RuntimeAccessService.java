package weblogic.management.provider.internal;

import java.security.AccessController;
import weblogic.version;
import weblogic.management.ManagementException;
import weblogic.management.ManagementLogger;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.context.JMXContextAccessImpl;
import weblogic.management.context.JMXContextHelper;
import weblogic.management.provider.MSIService;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public class RuntimeAccessService extends AbstractServerService {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public void start() throws ServiceFailureException {
      ManagementLogger.logStartupBuildName(version.getVersions());

      try {
         RuntimeAccessImpl var1 = new RuntimeAccessImpl();
         ManagementService.initializeRuntime(var1);
         RuntimeMBeanHelperImpl var2 = new RuntimeMBeanHelperImpl();
         RuntimeMBeanDelegate.setRuntimeMBeanHelper(var2);
         ManagementService.getPropertyService(kernelId).doPostParseInitialization(var1.getDomain());
         ManagementService.getPropertyService(kernelId).initializeSecurityProperties(false);
         var1.initialize();
         if (!ManagementService.getPropertyService(kernelId).isAdminServer()) {
            MSIService.getMSIService().doPostParseInitialization(var1.getDomain());
         } else {
            DomainMBean var3 = var1.getDomain();
            String var4 = var3.getAdminServerName();
            String var5;
            if (var4 == null) {
               var5 = "There is an attempt to boot Admin Server. However Admin Server Name is not specified in config.xml Please fix your config.xml before starting the Admin Server";
               throw new ManagementException(var5);
            }

            var5 = ManagementService.getPropertyService(kernelId).getServerName();
            if (!var5.equals(var4)) {
               String var6 = "Booting as admin server, but servername, " + var5 + ", does not match the admin server name, " + var4;
               throw new ManagementException(var6);
            }
         }

         JMXContextHelper.setJMXContextAccess(new JMXContextAccessImpl());
      } catch (RuntimeAccessImpl.SchemaValidationException var7) {
         throw new ServiceFailureException(var7.getMessage());
      } catch (RuntimeAccessImpl.ParseException var8) {
         throw new ServiceFailureException(var8.getMessage());
      } catch (ManagementException var9) {
         throw new ServiceFailureException(var9);
      }
   }
}
