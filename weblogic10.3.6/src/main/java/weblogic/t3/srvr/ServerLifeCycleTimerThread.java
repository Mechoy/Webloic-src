package weblogic.t3.srvr;

import java.security.AccessController;
import weblogic.kernel.T3SrvrLogger;
import weblogic.management.provider.ManagementService;
import weblogic.nodemanager.NMService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.ServiceFailureException;
import weblogic.utils.FileUtils;

final class ServerLifeCycleTimerThread extends Thread {
   private static ServerLifeCycleTimerThread THE_ONE;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private int timeout;

   private ServerLifeCycleTimerThread() {
      if (ManagementService.isRuntimeAccessInitialized()) {
         this.timeout = ManagementService.getRuntimeAccess(kernelId).getServer().getServerLifeCycleTimeoutVal();
      } else {
         this.timeout = 30;
      }

   }

   static synchronized void startTimeBomb() {
      if (THE_ONE == null) {
         THE_ONE = new ServerLifeCycleTimerThread();
         THE_ONE.setDaemon(true);
         THE_ONE.start();
      }
   }

   public void run() {
      int var1 = 10000;
      int var2;
      if (this.timeout == 0) {
         var2 = Integer.MAX_VALUE;
      } else if (this.timeout < 2147483) {
         var2 = this.timeout * 1000;
      } else {
         var2 = Integer.MAX_VALUE;
      }

      if (var1 > var2) {
         var1 = var2;
      }

      while(var1 > 0) {
         try {
            Thread.sleep((long)var1);
            var2 -= var1;
            if (var2 <= 0) {
               break;
            }

            if (var2 < var1) {
               var1 = var2;
            }
         } catch (InterruptedException var6) {
         }
      }

      T3SrvrLogger.logShutdownTimedOut(this.timeout);
      FileUtils.removeLockFiles();
      T3Srvr.logThreadDump();
      NMService var3 = NMService.getInstance();
      if (var3 != null) {
         try {
            var3.hardShutdown();
         } catch (ServiceFailureException var5) {
         }
      }

      Runtime.getRuntime().halt(-1);
   }
}
