package weblogic.cluster.singleton;

import java.io.IOException;
import java.rmi.RemoteException;
import java.security.AccessController;
import java.util.HashSet;
import java.util.Vector;
import weblogic.management.configuration.MachineMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.nodemanager.mbean.NodeManagerRuntime;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.work.WorkManagerFactory;

final class MigratableServerState {
   private static final boolean DEBUG = MigrationDebugLogger.isDebugEnabled();
   private final ServerMBean server;
   private final Vector candidateMachines;
   private MachineMBean currentMachine;
   private MachineMBean previousMachine;
   private int serverMigrationAttemptsCount = 0;
   private int numberOfTimesAllMachinesTriedCount = 0;
   private final RestartTask task;
   private MigratableServersMonitorImpl monitor;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public MigratableServerState(ServerMBean var1, MachineMBean var2, MigratableServersMonitorImpl var3) {
      this.server = var1;
      this.candidateMachines = getCandidateMachines(var1);
      this.currentMachine = var2;
      this.task = new RestartTask(this);
      this.monitor = var3;
   }

   public String toString() {
      return this.server + " on " + this.currentMachine;
   }

   private static Vector getCandidateMachines(ServerMBean var0) {
      HashSet var1 = new HashSet();
      MachineMBean[] var2 = var0.getCandidateMachines();
      int var3;
      if (var2 != null) {
         for(var3 = 0; var3 < var2.length; ++var3) {
            var1.add(var2[var3]);
         }
      }

      var2 = var0.getCluster().getCandidateMachinesForMigratableServers();
      if (var2 != null) {
         for(var3 = 0; var3 < var2.length; ++var3) {
            var1.add(var2[var3]);
         }
      }

      return new Vector(var1);
   }

   public ServerMBean getServer() {
      return this.server;
   }

   MachineMBean getCurrentMachine() {
      return this.currentMachine;
   }

   MachineMBean getPreviousMachine() {
      return this.previousMachine;
   }

   private void migrateToANewMachine() {
      if (this.candidateMachines.size() > 0) {
         ++this.serverMigrationAttemptsCount;
         int var1 = this.candidateMachines.indexOf(this.currentMachine);
         int var2 = (var1 + 1) % this.candidateMachines.size();
         this.previousMachine = this.currentMachine;
         this.currentMachine = (MachineMBean)this.candidateMachines.get(var2);
         if (DEBUG) {
            p(this.server + " is migrating from " + this.previousMachine + " to " + this.currentMachine);
         }

         try {
            this.monitor.setServerLocation(this.server.getName(), this.currentMachine.getName());
         } catch (RemoteException var4) {
         }

      }
   }

   void serverUnresponsive() throws ServerMigrationException {
      if (this.serverMigrationAttemptsCount > this.candidateMachines.size()) {
         throw new ServerMigrationException("Failed to start the migratable server on one of the candidate machines", (Throwable)null);
      } else {
         if (!this.task.isTaskRunning()) {
            this.task.changeRunningState(true);
            String var1 = this.monitor.getCurrentMachine(this.server.getName());
            if (var1 != null && !var1.equals(this.getCurrentMachine().getName())) {
               this.currentMachine = this.monitor.getMachine(var1);
               if (DEBUG) {
                  p("Resetting MigratableServerState current machine to  " + var1 + " for server " + this.server);
               }
            }

            if (DEBUG) {
               p("Restarting server " + this.server);
            }

            WorkManagerFactory.getInstance().getSystem().schedule(this.task);
         }

      }
   }

   private boolean isRestartable() {
      if (this.serverMigrationAttemptsCount != 0 && this.serverMigrationAttemptsCount % this.candidateMachines.size() == 0) {
         try {
            Thread.sleep(this.getMillisToSleepBetweenRetryAttempts());
         } catch (InterruptedException var2) {
         }
      }

      if (this.getAdditionalRetryAttempts() == -1) {
         return true;
      } else {
         return this.serverMigrationAttemptsCount <= this.candidateMachines.size() * (this.getAdditionalRetryAttempts() + 1);
      }
   }

   private void startServer() throws IOException {
      NodeManagerRuntime var1 = NodeManagerRuntime.getInstance(this.getCurrentMachine());
      ServerMigrationRuntimeMBeanImpl.getInstance().migrationStarted(this);

      try {
         if (DEBUG) {
            p("Sending start command to nm for " + this.server);
         }

         var1.start(this.server).waitForFinish();
      } catch (InterruptedException var3) {
      }

   }

   private void migrationComplete() {
      this.markAsMigratableAgain();
      ServerMigrationRuntimeMBeanImpl.getInstance().migrationCompleted(this);
   }

   void markAsMigratableAgain() {
      this.serverMigrationAttemptsCount = 0;
   }

   private String getCurrentStatus() {
      try {
         return NodeManagerRuntime.getInstance(this.getCurrentMachine()).getState(this.server);
      } catch (IOException var2) {
         return "UNKNOWN";
      }
   }

   private int getAdditionalRetryAttempts() {
      return this.server.getCluster().getAdditionalAutoMigrationAttempts();
   }

   private long getMillisToSleepBetweenRetryAttempts() {
      return this.server.getCluster().getMillisToSleepBetweenAutoMigrationAttempts();
   }

   private static void p(Object var0) {
      System.out.println("<MigratableServerState> " + var0);
   }

   private static class RestartTask implements Runnable {
      private final MigratableServerState serverState;
      private boolean isTaskRunning;

      private RestartTask(MigratableServerState var1) {
         this.isTaskRunning = false;
         this.serverState = var1;
      }

      public void run() {
         try {
            String var1 = this.serverState.getCurrentStatus();
            if (MigratableServerState.DEBUG) {
               MigratableServerState.p("Current status of " + this.serverState + " is " + var1);
            }

            if (var1.equals("RUNNING")) {
               this.serverState.migrationComplete();
               return;
            }

            MachineMBean var2;
            for(var2 = this.serverState.currentMachine; var1.equals("UNKNOWN") || var1.equals("FAILED_NOT_RESTARTABLE"); var1 = this.serverState.getCurrentStatus()) {
               try {
                  if (MigratableServerState.DEBUG) {
                     MigratableServerState.p("Current status of " + this.serverState + " is " + var1);
                  }

                  if (MigratableServerState.DEBUG) {
                     MigratableServerState.p(this.serverState + " is restartable? " + this.serverState.isRestartable());
                  }

                  if (!this.serverState.isRestartable()) {
                     SingletonLogger.logServerMigrationFailed(this.serverState.server.getName(), var2.getName());
                     break;
                  }

                  SingletonLogger.logServerMigrationStarting(this.serverState.server.getName(), var2.getName(), this.serverState.currentMachine.getName());
                  this.serverState.migrateToANewMachine();
                  this.serverState.startServer();
               } catch (IOException var8) {
                  SingletonLogger.logServerMigrationTargetUnreachable(this.serverState.server.getName(), var2.getName(), this.serverState.currentMachine.getName());
               }
            }

            if (var1.equals("RUNNING")) {
               SingletonLogger.logServerMigrationFinished(this.serverState.server.getName(), var2.getName(), this.serverState.currentMachine.getName());
               this.serverState.migrationComplete();
            }
         } finally {
            this.changeRunningState(false);
         }

      }

      private synchronized void changeRunningState(boolean var1) {
         this.isTaskRunning = var1;
      }

      private synchronized boolean isTaskRunning() {
         return this.isTaskRunning;
      }

      // $FF: synthetic method
      RestartTask(MigratableServerState var1, Object var2) {
         this(var1);
      }
   }
}
