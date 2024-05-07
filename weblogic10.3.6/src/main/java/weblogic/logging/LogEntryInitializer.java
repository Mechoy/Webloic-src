package weblogic.logging;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.AccessController;
import weblogic.diagnostics.context.DiagnosticContextHelper;
import weblogic.kernel.AuditableThreadLocal;
import weblogic.kernel.AuditableThreadLocalFactory;
import weblogic.management.provider.ManagementService;
import weblogic.security.Security;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.transaction.TxHelper;

public final class LogEntryInitializer {
   private static final String UNKNOWN = "Unknown";
   private static String currentMachineName = null;
   private static String currentServerName = null;
   private static boolean serverInitialized = false;
   private static final AuditableThreadLocal threadLocal = AuditableThreadLocalFactory.createThreadLocal();
   private static final boolean DEBUG = false;

   public static void initializeLogEntry(LogEntry var0) {
      if (threadLocal.get() == null) {
         threadLocal.set(new Boolean(true));

         try {
            var0.setUserId(getCurrentUserId());
            var0.setTransactionId(getCurrentTransactionId());
            var0.setMachineName(getCurrentMachineName());
            var0.setServerName(getCurrentServerName());
            var0.setDiagnosticContextId(getCurrentDiagnosticContextId());
         } catch (Throwable var2) {
         }

         threadLocal.set((Object)null);
      }
   }

   public static String getCurrentMachineName() {
      if (currentMachineName == null) {
         try {
            currentMachineName = InetAddress.getLocalHost().getHostName();
         } catch (UnknownHostException var1) {
            currentMachineName = "Unknown";
         }
      }

      return currentMachineName;
   }

   public static String getCurrentServerName() {
      if (!serverInitialized) {
         return "";
      } else {
         if (currentServerName == null) {
            currentServerName = ManagementService.getRuntimeAccess(LogEntryInitializer.KernelIdInitializer.KERNEL_ID).getServerName();
         }

         return currentServerName;
      }
   }

   public static String getCurrentTransactionId() {
      if (!serverInitialized) {
         return "";
      } else {
         String var0 = TxHelper.getTransactionId();
         if (var0 == null) {
            var0 = "";
         }

         return var0;
      }
   }

   public static String getCurrentUserId() {
      if (!serverInitialized) {
         return "";
      } else {
         String var0 = SubjectUtils.getUsername(Security.getCurrentSubject());
         if (var0 == null) {
            var0 = "";
         }

         return var0;
      }
   }

   public static String getCurrentDiagnosticContextId() {
      if (!serverInitialized) {
         return "";
      } else {
         String var0 = DiagnosticContextHelper.getContextId();
         if (var0 == null) {
            var0 = "";
         }

         return var0;
      }
   }

   static boolean isServerInitialized() {
      return serverInitialized;
   }

   static void setServerInitialized(boolean var0) {
      serverInitialized = var0;
   }

   private static class KernelIdInitializer {
      private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   }
}
