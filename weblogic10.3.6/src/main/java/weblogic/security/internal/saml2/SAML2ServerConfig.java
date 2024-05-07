package weblogic.security.internal.saml2;

import java.security.AccessController;
import weblogic.management.configuration.SingleSignOnServicesMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.shared.LoggerWrapper;

public class SAML2ServerConfig {
   public static final String SAML2_APP = "saml2";
   private static LoggerWrapper LOGGER = LoggerWrapper.getInstance("SecuritySAML2Service");
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   protected static void logDebug(String var0, String var1, String var2) {
      if (LOGGER.isDebugEnabled()) {
         LOGGER.debug(var0 + "." + var1 + "(): " + var2);
      }

   }

   private static void logDebug(String var0, String var1) {
      logDebug("SAML2ServerConfig", var0, var1);
   }

   public static boolean isApplicationConfigured(String var0) {
      if (!var0.equals("saml2")) {
         logDebug("isApplicationConfigured", "Unknown app '" + var0 + "', return false");
         return false;
      } else {
         SingleSignOnServicesMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServer().getSingleSignOnServices();
         if (var1 == null) {
            logDebug("isApplicationConfigured", "SingleSignOnServicesMBean not found, return false");
            return false;
         } else if (!var1.isServiceProviderEnabled() && !var1.isIdentityProviderEnabled()) {
            logDebug("isApplicationConfigured", "SingleSignOnMBean neither IdP nor SP is enabled, return false");
            return false;
         } else {
            logDebug("isApplicationConfigured", "SingleSignOnMBean IdP or SP is enabled, return true");
            return true;
         }
      }
   }
}
