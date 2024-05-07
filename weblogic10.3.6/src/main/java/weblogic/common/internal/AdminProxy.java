package weblogic.common.internal;

import java.security.AccessController;
import weblogic.version;
import weblogic.common.CommonTextTextFormatter;
import weblogic.common.T3Exception;
import weblogic.common.T3Executable;
import weblogic.management.provider.ManagementService;
import weblogic.platform.VM;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.server.ServerLifecycleException;
import weblogic.t3.srvr.ExecutionContext;
import weblogic.t3.srvr.T3Srvr;

public final class AdminProxy implements T3Executable {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public void initialize() {
   }

   public void destroy() {
   }

   public Object execute(ExecutionContext var1, Object var2) throws Exception {
      AdminMsg var4 = (AdminMsg)var2;
      AuthenticatedSubject var3;
      AdminProxyWatchDog var6;
      switch (var4.cmd) {
         case 1:
            AdminProxyWatchDog var8 = (AdminProxyWatchDog)var1.get("WEBLOGIC.WATCHDOG");
            if (var8 != null) {
               var8.echoReceived();
            }

            return "It's alive!";
         case 2:
            var3 = SecurityServiceManager.getCurrentSubject(kernelId);
            if (!SubjectUtils.isUserAnAdministrator(var3) && !SubjectUtils.isUserInGroup(var3.getSubject(), "Operators")) {
               throw new SecurityException("User is not an administrator");
            } else {
               if (var4.intervalSecs() >= 0) {
                  T3Srvr.getT3Srvr().setShutdownWaitSecs(var4.intervalSecs());
               }

               try {
                  ManagementService.getRuntimeAccess(kernelId).getServerRuntime().shutdown();
               } catch (ServerLifecycleException var7) {
                  throw new ServerLifecycleException(var7);
               }

               CommonTextTextFormatter var5 = new CommonTextTextFormatter();
               return var5.getServerShutdownSuccessfully(ManagementService.getRuntimeAccess(kernelId).getServerRuntime().getName());
            }
         case 3:
         case 4:
         case 8:
         default:
            throw new T3Exception("Unknown AdminMsg Command: " + var4.cmd);
         case 5:
            return version.getVersions();
         case 6:
            var6 = (AdminProxyWatchDog)var1.get("WEBLOGIC.WATCHDOG");
            if (var6 == null) {
               var6 = new AdminProxyWatchDog(SecurityServiceManager.getCurrentSubject(kernelId), var4.intervalSecs);
               var6.initialize();
               var1.put("WEBLOGIC.WATCHDOG", var6);
            }

            return null;
         case 7:
            var6 = (AdminProxyWatchDog)var1.remove("WEBLOGIC.WATCHDOG");
            if (var6 != null) {
               var6.disable();
            }

            return null;
         case 9:
            var3 = SecurityServiceManager.getCurrentSubject(kernelId);
            if (SubjectUtils.isUserAnAdministrator(var3)) {
               return null;
            }

            throw new SecurityException("Invalid password for system administrator.");
         case 10:
            return T3Srvr.getT3Srvr().getLockoutManager().lockServer(var4.argString());
         case 11:
            return T3Srvr.getT3Srvr().getLockoutManager().unlockServer();
         case 12:
            return T3Srvr.getT3Srvr().cancelShutdown();
         case 13:
            this.checkThreadDumpPrivileges();
            VM.getVM().threadDump();
            return null;
      }
   }

   private void checkThreadDumpPrivileges() throws SecurityException {
      if (ManagementService.getRuntimeAccess(kernelId).getDomain().isProductionModeEnabled()) {
         AuthenticatedSubject var1 = SecurityServiceManager.getCurrentSubject(kernelId);
         if (!SubjectUtils.doesUserHaveAnyAdminRoles(var1)) {
            throw new SecurityException("User: '" + SubjectUtils.getUsername(var1) + "' does not have permission to take thread dumps. " + "Only Admin role users are permitted to take thread dumps in " + "production mode servers");
         }
      }
   }
}
