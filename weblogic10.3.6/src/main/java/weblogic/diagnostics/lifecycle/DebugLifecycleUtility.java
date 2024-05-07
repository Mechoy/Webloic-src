package weblogic.diagnostics.lifecycle;

import java.security.AccessController;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

final class DebugLifecycleUtility {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugDiagnosticLifecycleHandlers");

   private DebugLifecycleUtility() {
   }

   static void debugHandlerStates(DiagnosticComponentLifecycle[] var0) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Displaying server state and diagnostic component states:");
         debugLogger.debug("Server state: " + ManagementService.getRuntimeAccess(kernelId).getServerRuntime().getState());

         for(int var1 = 0; var1 < var0.length; ++var1) {
            DiagnosticComponentLifecycle var2 = var0[var1];
            debugLogger.debug("Name: " + ComponentRegistry.getWLDFComponentName(var2));
            debugLogger.debug("Status: " + DiagnosticLifecycleConstants.STATES[var2.getStatus()]);
         }
      }

   }
}
