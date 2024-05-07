package weblogic.t3.srvr;

import java.security.AccessController;
import java.util.Locale;
import weblogic.i18n.Localizer;
import weblogic.i18ntools.L10nLookup;
import weblogic.kernel.T3SrvrLogger;
import weblogic.management.provider.ManagementService;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.AuthorizationManager;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.ServerResource;

public final class ServerLockoutManager {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private final AuthorizationManager am;
   private boolean isLocked = false;
   private String lockedMessage;
   private static final int LOCKSERVER_CODE = 1;
   private static final int UNLOCKSERVER_CODE = 2;
   private static final int SHUTDOWN_CODE = 3;
   private static final int CANCELSHUTDOWN_CODE = 4;

   ServerLockoutManager() {
      this.am = SecurityServiceManager.getAuthorizationManager(kernelId, "weblogicDEFAULT");
   }

   public void checkServerLock() throws SecurityException {
      if (this.isLocked) {
         throw new SecurityException(this.lockedMessage);
      }
   }

   public String lockServer(String var1) throws SecurityException {
      AuthenticatedSubject var2 = SecurityServiceManager.getCurrentSubject(kernelId);
      this.simpleCheckSubject(var2, 1);
      T3SrvrLogger.logLockServerRequested(SubjectUtils.getUsername(var2));
      ServerResource var3 = new ServerResource((String)null, ManagementService.getRuntimeAccess(kernelId).getServerName(), "lock");
      if (!this.am.isAccessAllowed(var2, var3, (ContextHandler)null)) {
         throw new SecurityException("User: '" + SubjectUtils.getUsername(var2) + "' does not have permission to lock server");
      } else {
         this.lockedMessage = var1;
         this.isLocked = true;
         String var4 = T3SrvrLogger.logLockServerHappened();
         return this.getLocalMessage(var4);
      }
   }

   public String unlockServer() throws SecurityException {
      AuthenticatedSubject var1 = SecurityServiceManager.getCurrentSubject(kernelId);
      this.simpleCheckSubject(var1, 2);
      T3SrvrLogger.logUnlockServerRequested(SubjectUtils.getUsername(var1));
      ServerResource var2 = new ServerResource((String)null, ManagementService.getRuntimeAccess(kernelId).getServerName(), "unlock");
      if (!this.am.isAccessAllowed(var1, var2, (ContextHandler)null)) {
         throw new SecurityException("User: '" + SubjectUtils.getUsername(var1) + "' does not have permission to unlock server");
      } else {
         this.isLocked = false;
         this.lockedMessage = null;
         String var3 = T3SrvrLogger.logUnlockServerHappened();
         return this.getLocalMessage(var3);
      }
   }

   private void simpleCheckSubject(AuthenticatedSubject var1, int var2) throws SecurityException {
      if (var1 != null) {
         String var3 = SubjectUtils.getUsername(var1);
         if (var3 == null || var3.trim().length() <= 0) {
            switch (var2) {
               case 1:
                  T3SrvrLogger.logNoLockServerNamelessUser();
                  throw new SecurityException("Cannot disable server logins, the request was from a nameless user (Principal)");
               case 2:
                  T3SrvrLogger.logNoUnlockServerNamelessUser();
                  throw new SecurityException("Cannot enable server logins, the request was from a nameless user (Principal)");
               case 3:
               default:
                  T3SrvrLogger.logNoShutdownNamelessUser();
                  throw new SecurityException("Cannot shutdown the server, the request was from a nameless user (Principal)");
               case 4:
                  T3SrvrLogger.logNoCancelShutdownNamelessUser();
                  throw new SecurityException("Cannot cancel the server shutdown, the request was from a nameless user (Principal)");
            }
         }
      } else {
         switch (var2) {
            case 1:
               T3SrvrLogger.logNoLockServerNullUser();
               throw new SecurityException("Cannot disable server logins, the request was from a null Principal");
            case 2:
               T3SrvrLogger.logNoUnlockServerNullUser();
               throw new SecurityException("Cannot enable server logins, the request was from a null Principal");
            case 3:
            default:
               T3SrvrLogger.logNoShutdownNullUser();
               throw new SecurityException("Cannot shutdown, the request was from a null Principal");
            case 4:
               T3SrvrLogger.logNoCancelShutdownNullUser();
               throw new SecurityException("Cannot cancel the server shutdown, the request was from a null user (Principal)");
         }
      }
   }

   private String getLocalMessage(String var1) {
      String var2;
      try {
         int var3 = Integer.parseInt(var1);
         Localizer var4 = L10nLookup.getLocalizer(Locale.getDefault(), "weblogic.i18n.T3SrvrLogLocalizer");
         var2 = (String)var4.getObject("messagebody", var3);
      } catch (Exception var5) {
         T3SrvrLogger.logLocalizerProblem(var1, var5);
         var2 = "A message regarding the status of server shutdown or logins could not be retrieved, messageid " + var1;
      }

      return var2;
   }
}
