package weblogic.diagnostics.lifecycle;

import java.security.AccessController;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.image.ImageManager;
import weblogic.diagnostics.watch.WatchManager;
import weblogic.management.ManagementException;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class WatchLifecycleImpl implements DiagnosticComponentLifecycle {
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugDiagnosticLifecycleHandlers");
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static WatchLifecycleImpl singleton = new WatchLifecycleImpl();
   private static final long WAIT_INCREMENT = 1000L;
   long maxImageCaptureWait = 30000L;

   public static final DiagnosticComponentLifecycle getInstance() {
      return singleton;
   }

   public int getStatus() {
      return 4;
   }

   public void initialize() throws DiagnosticComponentLifecycleException {
      try {
         WatchManager.getInstance();
         if (ManagementService.isRuntimeAccessInitialized()) {
            this.maxImageCaptureWait = (long)((double)(ManagementService.getRuntimeAccess(kernelId).getServer().getServerLifeCycleTimeoutVal() * 1000) * 0.75);
         }

      } catch (ManagementException var2) {
         throw new DiagnosticComponentLifecycleException(var2);
      }
   }

   public void enable() throws DiagnosticComponentLifecycleException {
   }

   public void disable() throws DiagnosticComponentLifecycleException {
      this.waitForImageTasks();
   }

   private void waitForImageTasks() throws DiagnosticComponentLifecycleException {
      try {
         if (WatchManager.getInstance().getNumActiveImageNotifications() > 0) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Waiting for any active DIMG notifications to complete");
            }

            try {
               Thread.sleep(1000L);
            } catch (InterruptedException var5) {
            }

            long var1 = 0L;

            while(ImageManager.getInstance().tasksInProgress() && var1 < this.maxImageCaptureWait) {
               if (debugLogger.isDebugEnabled()) {
                  debugLogger.debug("Image capture in progress, waiting...");
               }

               try {
                  Thread.sleep(1000L);
                  var1 += 1000L;
               } catch (InterruptedException var4) {
               }
            }
         }

      } catch (ManagementException var6) {
         throw new DiagnosticComponentLifecycleException(var6);
      }
   }
}
