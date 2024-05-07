package weblogic.t3.srvr;

import java.security.AccessController;
import weblogic.kernel.T3SrvrLogger;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.ServiceFailureException;
import weblogic.server.channels.DynamicListenThreadManager;

final class EnableListenersHelper {
   private static final boolean DEBUG = false;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private boolean started;

   private EnableListenersHelper() {
   }

   static EnableListenersHelper getInstance() {
      return EnableListenersHelper.Factory.THE_ONE;
   }

   synchronized void start() throws ServiceFailureException {
      if (!this.started) {
         this.started = true;
         if (!ManagementService.getRuntimeAccess(kernelId).getServer().getListenersBindEarly()) {
            ListenerService.getInstance().bindListeners();
         }

         DynamicListenThreadManager.getInstance().enableListeners();
         SetUIDRendezvous.finish();
         logStartedMessage();
      }
   }

   void stop() {
      this.halt();
   }

   synchronized void halt() {
      if (this.started) {
         this.started = false;
         DynamicListenThreadManager.getInstance().stop();
      }
   }

   private static boolean isClassCacheTurnedOn() {
      return ClassLoader.getSystemClassLoader().getClass().getName().equals("com.oracle.classloader.PolicyClassLoader");
   }

   private static void logStartedMessage() {
      RuntimeAccess var0 = ManagementService.getRuntimeAccess(kernelId);
      String var1 = var0.getServerName();
      String var2 = var0.getDomainName();
      boolean var3 = var0.getDomain().isProductionModeEnabled();
      if (var0.isAdminServer()) {
         if (var3) {
            T3SrvrLogger.logStartedAdminServerProduction(var1, var2);
         } else {
            T3SrvrLogger.logStartedAdminServerDevelopment(var1, var2);
         }
      } else if (!var0.isAdminServerAvailable()) {
         if (var3) {
            T3SrvrLogger.logStartedIndependentManagedServerProdMode(var1, var2);
         } else {
            T3SrvrLogger.logStartedIndependentManagedServerDevMode(var1, var2);
         }
      } else if (var3) {
         T3SrvrLogger.logStartedManagedServerProduction(var1, var2);
      } else {
         T3SrvrLogger.logStartedManagedServerDevelopment(var1, var2);
      }

      if (isClassCacheTurnedOn() && var3) {
         T3SrvrLogger.logClassCacheEnabledWarning(var1, var2);
      }

   }

   private static void debug(String var0) {
      System.out.println("<LISTENER_DEBUG>" + var0);
   }

   // $FF: synthetic method
   EnableListenersHelper(Object var1) {
      this();
   }

   private static final class Factory {
      static final EnableListenersHelper THE_ONE = new EnableListenersHelper();
   }
}
