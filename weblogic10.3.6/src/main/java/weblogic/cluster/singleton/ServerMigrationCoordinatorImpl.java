package weblogic.cluster.singleton;

import java.security.AccessController;
import java.util.Map;
import weblogic.management.provider.DomainAccess;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.ServerLifeCycleRuntimeMBean;
import weblogic.management.runtime.ServerLifeCycleTaskRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.ServerLifecycleException;
import weblogic.utils.collections.ConcurrentHashMap;

public final class ServerMigrationCoordinatorImpl implements ServerMigrationCoordinator, MigratableServiceConstants {
   private final Map taskMap = new ConcurrentHashMap();
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public void migrate(String var1, String var2, String var3, boolean var4, boolean var5) throws ServerMigrationException {
      ServerMigrationTask var6 = (ServerMigrationTask)this.taskMap.get(var1);
      if (var6 == null) {
         var6 = new ServerMigrationTask(var1, var3);
         if (MigrationDebugLogger.isDebugEnabled()) {
            MigrationDebugLogger.debug(var1 + " New Migration Task " + var6);
         }

         this.taskMap.put(var1, var6);

         try {
            this.stopServer(var4, var6);
            this.startServer(var5, var6);
         } finally {
            this.taskMap.remove(var1);
         }

      } else {
         throw new ServerMigrationException("Migration operation in progress", (Throwable)null);
      }
   }

   private void stopServer(boolean var1, ServerMigrationTask var2) throws ServerMigrationException {
      try {
         var2.stopMigratableServer();
      } catch (ServerLifecycleException var4) {
         if (MigrationDebugLogger.isDebugEnabled()) {
            MigrationDebugLogger.debug("Task failed", var4);
         }

         if (!var1) {
            throw new ServerMigrationException("Migration failed trying to stop the server", var4);
         }
      }

      if (MigrationDebugLogger.isDebugEnabled()) {
         MigrationDebugLogger.debug("Stopped server");
      }

   }

   private void startServer(boolean var1, ServerMigrationTask var2) throws ServerMigrationException {
      try {
         var2.startMigratableServer();
      } catch (ServerLifecycleException var4) {
         if (MigrationDebugLogger.isDebugEnabled()) {
            MigrationDebugLogger.debug("Task failed", var4);
         }

         if (!var1) {
            throw new ServerMigrationException("Migration failed trying to start the server", var4);
         }
      }

      if (MigrationDebugLogger.isDebugEnabled()) {
         MigrationDebugLogger.debug("Restarted server on target destination");
      }

   }

   void taskComplete(String var1) {
      if (MigrationDebugLogger.isDebugEnabled()) {
         MigrationDebugLogger.debug("Removing task " + var1);
      }

      Object var2 = this.taskMap.remove(var1);
      if (MigrationDebugLogger.isDebugEnabled()) {
         MigrationDebugLogger.debug("Removing task " + var2);
      }

   }

   private static class ServerMigrationTask {
      private final DomainAccess domainAccess;
      private final String serverName;
      private final String destinationMachine;

      private ServerMigrationTask(String var1, String var2) {
         this.domainAccess = ManagementService.getDomainAccess(ServerMigrationCoordinatorImpl.kernelId);
         this.serverName = var1;
         this.destinationMachine = var2;
      }

      private void stopMigratableServer() throws ServerLifecycleException {
         ServerLifeCycleRuntimeMBean var1 = this.domainAccess.lookupServerLifecycleRuntime(this.serverName);
         ServerLifeCycleTaskRuntimeMBean var2 = var1.shutdown();
         this.waitForTask(var2);
      }

      private void startMigratableServer() throws ServerLifecycleException {
         ServerLifeCycleRuntimeMBean var1 = this.domainAccess.lookupServerLifecycleRuntime(this.serverName);
         if (MigrationDebugLogger.isDebugEnabled()) {
            MigrationDebugLogger.debug(this.serverName + " is going to be started on " + this.destinationMachine);
         }

         ServerLifeCycleTaskRuntimeMBean var2 = var1.start(this.destinationMachine);
         this.waitForTask(var2);
      }

      private synchronized void waitForTask(ServerLifeCycleTaskRuntimeMBean var1) {
         while(var1.isRunning()) {
            try {
               this.wait(10000L);
            } catch (InterruptedException var3) {
            }
         }

      }

      // $FF: synthetic method
      ServerMigrationTask(String var1, String var2, Object var3) {
         this(var1, var2);
      }
   }
}
