package weblogic.server.channels;

import java.security.AccessController;
import weblogic.kernel.Kernel;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.ServerLogger;
import weblogic.utils.concurrent.Semaphore;

public final class ServerThrottle {
   private static final boolean DEBUG = false;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private final Semaphore lock;
   private volatile boolean enabled;

   public static ServerThrottle getServerThrottle() {
      return ServerThrottle.ServerThrottleMaker.SINGLETON;
   }

   private ServerThrottle() {
      this.enabled = false;
      int var1 = this.getMaxOpenSockets();
      this.enabled = var1 > 0;
      this.lock = new Semaphore(var1);
   }

   void changeMaxOpenSockets(int var1) {
      this.enabled = var1 > 0;
      this.lock.changePermits(var1);
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void acquireSocketPermit() {
      if (!this.lock.tryAcquire()) {
         ServerLogger.logMaxOpenSockets(this.getMaxOpenSockets(), this.getMaxOpenSockets());
         RuntimeAccess var1 = ManagementService.getRuntimeAccess(kernelId);
         var1.getServerRuntime().setHealthState(1, "Max Threshold Reached for Open Sockets.  MaxOpenSockCount can be tuned.");
         this.lock.acquire();
         ServerLogger.logAcceptingConnections();
         var1.getServerRuntime().setHealthState(0, "");
      }

   }

   public void decrementOpenSocketCount() {
      this.lock.release();
   }

   private int getMaxOpenSockets() {
      int var1 = Kernel.getConfig().getMaxOpenSockCount();
      return var1;
   }

   private void debug(String var1) {
      System.out.println("[ServerThrottle] " + var1);
   }

   // $FF: synthetic method
   ServerThrottle(Object var1) {
      this();
   }

   private static final class ServerThrottleMaker {
      private static final ServerThrottle SINGLETON = new ServerThrottle();
   }
}
