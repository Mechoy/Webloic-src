package weblogic.cluster.singleton;

import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.health.HealthMonitorService;
import weblogic.jndi.Environment;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.LocalServerIdentity;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

class SingletonMaster implements MigratableServiceConstants, LeaseObtainedListener, LeaseLostListener, ClusterLeaderListener {
   private boolean isSingletonMaster = false;
   private final LeaseManager manager;
   private final SingletonMonitor monitor;
   public static final String SINGLETON_MASTER = "SINGLETON_MASTER";
   private static final boolean DEBUG = SingletonServicesDebugLogger.isDebugEnabled();
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   SingletonMaster(LeaseManager var1, int var2) {
      this.manager = var1;
      this.monitor = new SingletonMonitor(var1, var2);
      this.bind("weblogic.cluster.singleton.SingletonServicesStateManager", this.monitor.getSingletonServicesStateManager(), false);
   }

   void start() {
      if (DEBUG) {
         p("Starting Singleton Master Service. Beginning attempts to claim the SingletonMaster lock.");
      }

      if ("consensus".equals(MigratableServerService.theOne().getLeasingType())) {
         AbstractConsensusService.getInstance().addClusterLeaderListener(this);
      } else {
         try {
            this.manager.acquire("SINGLETON_MASTER", this);
         } catch (LeasingException var2) {
            this.manager.stop();
         }
      }

   }

   void stop() {
      if (DEBUG) {
         p("Stopping Singleton Master service.");
      }

      this.cleanup();
   }

   public synchronized boolean isSingletonMaster() {
      return this.isSingletonMaster;
   }

   public void localServerIsClusterLeader() {
      String var2;
      try {
         if (DEBUG) {
            p("Local Server is the Cluster Leader. Going to acquire the SingletonMaster lease.");
         }

         String var1 = this.manager.findOwner("SINGLETON_MASTER");
         if (var1 != null) {
            var2 = LeaseManager.getServerNameFromOwnerIdentity(var1);
            if (!LocalServerIdentity.getIdentity().getServerName().equals(var2)) {
               String var3 = AbstractConsensusService.getInstance().getServerState(var2);
               if (SingletonMonitor.canMigrateLease(var3)) {
                  if (DEBUG) {
                     p(var2 + " is marked as " + var3 + ". Voiding all its leases");
                  }

                  this.manager.voidLeases(var1);
               }
            }
         }

         this.manager.acquire("SINGLETON_MASTER", this);
      } catch (LeasingException var4) {
         var2 = "Got LeasingException " + var4 + " while trying to acquire SingletonMaster lease";
         HealthMonitorService.subsystemFailed("DatabaseLessLeasing", var2);
      }

   }

   public void localServerLostClusterLeadership() {
      this.onRelease();
   }

   public synchronized void onAcquire(String var1) {
      if (!this.isSingletonMaster) {
         this.isSingletonMaster = true;
         if (DEBUG) {
            p("Acquired the SingletonMaster lease. This server is now responsible for SingletonService management.");
         }

         this.manager.addLeaseLostListener(this);
         this.monitor.start();
         this.bind("weblogic/cluster/singleton/SingletonMonitorRemote", this.monitor, false);
         if ("consensus".equals(MigratableServerService.theOne().getLeasingType())) {
            AbstractConsensusService.getInstance().addConsensusServiceGroupViewListener(this.monitor);
         }

      }
   }

   public void onException(Exception var1, String var2) {
      if (DEBUG) {
         p("Encountered an exeption while trying to get the SingletonMaster lease. We are ignoring the exception and continuing to try to get the lease.", var1);
      }

   }

   public synchronized void onRelease() {
      if (this.isSingletonMaster) {
         if (DEBUG) {
            p("The SingletonMaster lease has been lost. This server will no longer perform SingletonService monitoring. It is unusual to lose a lease on a running server.This message often indicates a network connectivity problem.");
         }

         this.cleanup();
      }
   }

   private synchronized void cleanup() {
      if (this.isSingletonMaster) {
         this.isSingletonMaster = false;
         this.monitor.stop();
         this.manager.removeLeaseLostListener(this);

         try {
            this.manager.release("SINGLETON_MASTER");
         } catch (LeasingException var2) {
         }

         this.unbindMigrator();
         if (!"consensus".equals(MigratableServerService.theOne().getLeasingType())) {
            if (!ManagementService.getRuntimeAccess(KERNEL_ID).getServerRuntime().isShuttingDown()) {
               this.start();
            }
         } else {
            AbstractConsensusService.getInstance().removeConsensusServiceGroupViewListener(this.monitor);
         }

      }
   }

   private void bind(final String var1, final Object var2, final boolean var3) {
      try {
         AccessController.doPrivileged(new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               Context var1x = null;

               try {
                  Environment var2x = new Environment();
                  var2x.setCreateIntermediateContexts(true);
                  var2x.setReplicateBindings(var3);
                  var1x = var2x.getInitialContext();
                  var1x.rebind(var1, var2);
               } catch (NamingException var11) {
                  throw new AssertionError("Unexpected exception" + var11);
               } finally {
                  if (var1x != null) {
                     try {
                        var1x.close();
                     } catch (NamingException var10) {
                     }
                  }

               }

               return null;
            }
         });
      } catch (Exception var5) {
         throw new AssertionError("Unexpected exception" + var5);
      }
   }

   private void unbindMigrator() {
      try {
         AccessController.doPrivileged(new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               Context var1 = null;

               try {
                  Environment var2 = new Environment();
                  var1 = var2.getContext();
                  var1.unbind("weblogic/cluster/singleton/SingletonMonitorRemote");
               } catch (NamingException var11) {
                  throw new AssertionError("Unexpected exception" + var11);
               } finally {
                  if (var1 != null) {
                     try {
                        var1.close();
                     } catch (NamingException var10) {
                     }
                  }

               }

               return null;
            }
         });
      } catch (Exception var2) {
         throw new AssertionError("Unexpected exception" + var2);
      }
   }

   SingletonMonitor getSingletonMonitor() {
      return this.monitor;
   }

   private static final void p(String var0) {
      SingletonServicesDebugLogger.debug("SingletonMaster: " + var0);
   }

   private static final void p(String var0, Exception var1) {
      SingletonServicesDebugLogger.debug("SingletonMaster: " + var0, var1);
   }
}
