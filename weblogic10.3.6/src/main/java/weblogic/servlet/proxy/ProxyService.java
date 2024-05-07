package weblogic.servlet.proxy;

import java.security.AccessController;
import weblogic.management.configuration.HTTPProxyMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;
import weblogic.utils.Debug;
import weblogic.utils.StringUtils;

public final class ProxyService extends AbstractServerService {
   private static final String MBEAN_TYPE = "HTTPProxy";
   private static final boolean DEBUG = true;
   private final String localServer;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public ProxyService() {
      this.localServer = ManagementService.getRuntimeAccess(kernelId).getServerName();
   }

   public void initialize() throws ServiceFailureException {
   }

   public void start() throws ServiceFailureException {
   }

   private void parseAndTriggerHealthChecker(String var1, HTTPProxyMBean var2) {
      String[] var3 = StringUtils.split(var1, ',');

      for(int var4 = 0; var4 < var3.length; ++var4) {
         if (var3[var4].length() != 0) {
            String[] var5 = StringUtils.split(var3[var4].trim(), ':');
            HealthCheckTrigger var6 = new HealthCheckTrigger(var5[0].trim(), var5[1].trim(), var2.getHealthCheckInterval(), var2.getMaxRetries(), var2.getMaxHealthCheckInterval());
            Debug.say("REGISTERED TRIGGER FOR " + var6);
         }
      }

   }
}
