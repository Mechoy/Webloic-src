package weblogic.cluster.migration.management;

import java.rmi.RemoteException;
import java.rmi.UnknownHostException;
import java.security.AccessController;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.cluster.migration.RemoteMigratableServiceCoordinatorImpl;
import weblogic.cluster.migration.RemoteMigrationControl;
import weblogic.cluster.singleton.SingletonMonitorRemote;
import weblogic.jndi.Environment;
import weblogic.management.ManagementException;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JTAMigratableTargetMBean;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.SingletonServiceMBean;
import weblogic.management.provider.ActivateTask;
import weblogic.management.provider.DomainAccess;
import weblogic.management.provider.EditAccess;
import weblogic.management.provider.EditChangesValidationException;
import weblogic.management.provider.EditFailedException;
import weblogic.management.provider.EditNotEditorException;
import weblogic.management.provider.EditSaveChangesFailedException;
import weblogic.management.provider.EditWaitTimedOutException;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.ManagementServiceRestricted;
import weblogic.management.runtime.DomainRuntimeMBeanDelegate;
import weblogic.management.runtime.MigratableServiceCoordinatorRuntimeMBean;
import weblogic.management.runtime.MigrationException;
import weblogic.management.runtime.MigrationTaskRuntimeMBean;
import weblogic.protocol.URLManager;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.store.DefaultObjectHandler;
import weblogic.store.PersistentMap;
import weblogic.store.PersistentStore;
import weblogic.store.PersistentStoreException;
import weblogic.store.PersistentStoreManager;
import weblogic.utils.Debug;

public final class MigratableServiceCoordinatorRuntime extends DomainRuntimeMBeanDelegate implements MigratableServiceCoordinatorRuntimeMBean {
   private static final int TASK_AFTERLIFE_TIME_MILLIS = 1800000;
   private static final int POLLING_DELAY = 1000;
   public static final String STORE_NAME = "weblogic_migratable_services_store";
   private boolean sysTask = false;
   private DomainMBean domain;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   public static RemoteMigratableServiceCoordinatorImpl remoteCoordinator;
   Map taskMap = new ConcurrentHashMap();
   private static final AuthenticatedSubject KERNELID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public MigratableServiceCoordinatorRuntime() throws ManagementException {
      super("the-MigratableServiceCoordinator");

      try {
         this.domain = ManagementService.getRuntimeAccess(kernelId).getDomain();
         remoteCoordinator = new RemoteMigratableServiceCoordinatorImpl(this);
      } catch (NamingException var2) {
         throw new ManagementException("Failed to create remote migratable service coordinator", var2);
      }
   }

   public boolean isSystemTask() {
      return this.sysTask;
   }

   public void setSystemTask(boolean var1) {
      this.sysTask = var1;
   }

   public void migrateSingleton(SingletonServiceMBean var1, ServerMBean var2) throws MigrationException {
      ServerMBean[] var3 = var1.getAllCandidateServers();
      SingletonMonitorRemote var4 = null;
      DomainAccess var5 = ManagementService.getDomainAccess(kernelId);

      for(int var6 = 0; var6 < var3.length; ++var6) {
         String var7 = var5.lookupServerLifecycleRuntime(var3[var6].getName()).getState();
         if ("RUNNING".equals(var7)) {
            Context var8 = null;
            Environment var9 = new Environment();

            try {
               String var10 = URLManager.findAdministrationURL(var3[var6].getName());
               var9.setProviderUrl(var10);
               var8 = var9.getInitialContext();
               var4 = (SingletonMonitorRemote)var8.lookup("weblogic.cluster.singleton.SingletonMonitorRemote");
            } catch (NamingException var13) {
            } catch (UnknownHostException var14) {
            }
         }
      }

      if (var4 == null) {
         throw new MigrationException("No candidate server for " + var1 + " is reachable. " + "The service cannot be migrated until at least one is up.");
      } else {
         try {
            if (!var4.migrate(var1.getName(), var2.getName())) {
               throw new MigrationException("Failed to migrate " + var1.getName());
            } else {
               ServerMBean var15 = var1.getUserPreferredServer();
               if (var15 != null && !var15.getName().equals(var2.getName())) {
                  this.changeMigratableTargetsConfiguration(var1.getName(), var2.getName());
               }

            }
         } catch (weblogic.cluster.migration.MigrationException var11) {
            throw new MigrationException(var11.getMessage());
         } catch (RemoteException var12) {
            throw new MigrationException("Error while communicating with the Singleton Monitor: " + var12, var12);
         }
      }
   }

   public void migrate(MigratableTargetMBean var1, ServerMBean var2) throws MigrationException {
      this.doMigrate(var1, var2, true, true, false);
   }

   public boolean migrate(String var1, String var2) throws RemoteException {
      try {
         this.migrate(this.domain.lookupMigratableTarget(var1), this.domain.lookupServer(var2));
         return true;
      } catch (MigrationException var4) {
         throw new RemoteException("Error: " + var4, var4);
      }
   }

   public void migrate(MigratableTargetMBean var1, ServerMBean var2, boolean var3, boolean var4) throws MigrationException {
      this.doMigrate(var1, var2, !var3, !var4, false);
   }

   public boolean migrate(String var1, String var2, boolean var3, boolean var4) throws RemoteException {
      try {
         this.migrate(this.domain.lookupMigratableTarget(var1), this.domain.lookupServer(var2), var3, var4);
         return true;
      } catch (MigrationException var6) {
         throw new RemoteException("Error: " + var6, var6);
      }
   }

   public void migrateJTA(MigratableTargetMBean var1, ServerMBean var2, boolean var3, boolean var4) throws MigrationException {
      this.doMigrate(var1, var2, !var3, !var4, true);
   }

   public boolean migrateJTA(String var1, String var2, boolean var3, boolean var4) throws RemoteException {
      try {
         ServerMBean[] var5 = this.domain.getServers();
         JTAMigratableTargetMBean var6 = null;

         for(int var7 = 0; var7 < var5.length; ++var7) {
            if (var1.equals(var5[var7].getJTAMigratableTarget())) {
               var6 = var5[var7].getJTAMigratableTarget();
               break;
            }
         }

         this.migrateJTA((MigratableTargetMBean)var6, (ServerMBean)this.domain.lookupServer(var2), var3, var4);
         return true;
      } catch (MigrationException var8) {
         throw new RemoteException("Error: " + var8, var8);
      }
   }

   public MigrationTaskRuntimeMBean startMigrateTask(MigratableTargetMBean var1, ServerMBean var2, boolean var3) throws ManagementException {
      MigrationTask var4 = new MigrationTask(var1, var2, var3, false, false, this);
      this.taskMap.put(var4.getName(), var4);
      var4.run();
      return var4;
   }

   public void deactivateJTATarget(MigratableTargetMBean var1, String var2) throws MigrationException {
      try {
         PersistentMap var3 = this.getStoreMap();
         String var4 = (String)var3.get(var1.getName());
         if (var4 != null && !var4.equals(var2)) {
            RemoteMigrationControl var5 = this.getMigrationControl(var2);
            var5.deactivateTarget(var1.getName(), var2);
         }
      } catch (PersistentStoreException var6) {
         throw new MigrationException("Unexpected exception opening store ", var6);
      } catch (RemoteException var7) {
         throw new MigrationException("Failed to deactivate " + var1.getName() + " on " + var2, var7);
      } catch (weblogic.cluster.migration.MigrationException var8) {
         throw new MigrationException("Failed to deactivate " + var1.getName() + " on " + var2, var8);
      }
   }

   private RemoteMigrationControl getMigrationControl(String var1) throws MigrationException {
      Context var2 = null;

      try {
         Environment var3 = new Environment();
         var3.setProviderUrl(URLManager.findAdministrationURL(var1));
         var2 = var3.getInitialContext();
         return (RemoteMigrationControl)var2.lookup("weblogic.cluster.migrationControl");
      } catch (UnknownHostException var4) {
         throw new MigrationException("Failed to reach " + var1 + " to deactivate" + " JTAMigratableTarget", var4);
      } catch (NamingException var5) {
         throw new MigrationException("Unexpected naming exception", var5);
      }
   }

   public MigrationTaskRuntimeMBean startMigrateTask(MigratableTargetMBean var1, ServerMBean var2, boolean var3, boolean var4, boolean var5) throws ManagementException {
      MigrationTask var6 = new MigrationTask(var1, var2, var3, var4, var5, this);
      this.taskMap.put(var6.getName(), var6);
      var6.run();
      return var6;
   }

   public void driveMigrateTaskToEnd(String var1, boolean var2, boolean var3) throws ManagementException {
      MigrationTaskRuntimeMBean var4 = this.getTaskRuntimeMBean(var1);
      this.driveMigrateTaskToEnd(var4, var2, var3);
   }

   private void doMigrate(MigratableTargetMBean var1, ServerMBean var2, boolean var3, boolean var4, boolean var5) throws MigrationException {
      try {
         MigrationTaskRuntimeMBean var6 = this.startMigrateTask(var1, var2, var5, var3, var4);
         this.driveMigrateTaskToEnd(var6, var3, var4);
         if (var6.getError() != null) {
            if (var6.getError() instanceof MigrationException) {
               throw (MigrationException)var6.getError();
            } else {
               throw new MigrationException(var6.getError());
            }
         }
      } catch (ManagementException var7) {
         throw new MigrationException(var7);
      }
   }

   private void driveMigrateTaskToEnd(MigrationTaskRuntimeMBean var1, boolean var2, boolean var3) throws ManagementException {
      while(var1.isRunning()) {
         try {
            Thread.currentThread();
            Thread.sleep(1000L);
         } catch (InterruptedException var5) {
         }
      }

      if (!var1.isTerminal()) {
         Debug.assertion(var1.isWaitingForUser());
         if (var1.getStatusCode() == 3) {
            var1.continueWithSourceServerDown(var2);
         } else if (var1.getStatusCode() == 4) {
            var1.continueWithDestinationServerDown(var3);
         }

         this.driveMigrateTaskToEnd(var1, var2, var3);
      }

   }

   void updateState(String var1, String var2) throws MigrationException {
      try {
         PersistentMap var3 = this.getStoreMap();
         var3.put(var1, var2);
      } catch (PersistentStoreException var4) {
         throw new MigrationException("Failed to update store", var4);
      }
   }

   private PersistentMap getStoreMap() throws PersistentStoreException {
      PersistentStoreManager var1 = PersistentStoreManager.getManager();
      PersistentStore var2 = var1.getDefaultStore();
      return var2.createPersistentMap("weblogic_migratable_services_store", DefaultObjectHandler.THE_ONE);
   }

   public MigrationTaskRuntimeMBean[] getMigrationTaskRuntimes() {
      return (MigrationTaskRuntimeMBean[])((MigrationTaskRuntimeMBean[])this.taskMap.values().toArray(new MigrationTaskRuntimeMBean[this.taskMap.size()]));
   }

   public void clearOldMigrationTaskRuntimes() {
      Iterator var1 = this.taskMap.values().iterator();

      while(var1.hasNext()) {
         MigrationTaskRuntimeMBean var2 = (MigrationTaskRuntimeMBean)var1.next();
         if (System.currentTimeMillis() - var2.getEndTime() > 1800000L) {
            this.taskMap.remove(var2.getName());
         }
      }

   }

   private MigrationTaskRuntimeMBean getTaskRuntimeMBean(String var1) {
      return (MigrationTaskRuntimeMBean)this.taskMap.get(var1);
   }

   synchronized void changeMigratableTargetsConfiguration(String var1, String var2) throws MigrationException {
      try {
         DomainMBean var3 = null;
         EditAccess var4 = ManagementServiceRestricted.getEditAccess(KERNELID);
         boolean var5 = false;
         if (var4.getEditor() != null && !var4.isEditorExclusive()) {
            var3 = var4.getDomainBeanWithoutLock();
            var5 = false;
         } else {
            var3 = var4.startEdit(120000, 120000);
            var5 = true;
         }

         if (var2 != null) {
            ServerMBean var6 = getServer(var3, var2);
            MigratableTargetMBean var7 = getMigratableTarget(var3, var1);
            if (var7 != null) {
               var7.setUserPreferredServer(var6);
            } else {
               SingletonServiceMBean var8 = var3.lookupSingletonService(var1);
               if (var8 != null) {
                  var8.setUserPreferredServer(var6);
               }
            }
         }

         if (var5) {
            ActivateTask var24 = null;
            boolean var17 = false;

            try {
               var17 = true;
               var4.saveChanges();
               var24 = var4.activateChangesAndWaitForCompletion(120000L);
               var17 = false;
            } finally {
               if (var17) {
                  if (var24 != null && var24.getState() != 4) {
                     var4.cancelEdit();
                     String var10 = var24.getError() != null ? var24.getError().toString() : "Failed to update config";
                     throw new MigrationException(var10, var24.getError());
                  }

               }
            }

            if (var24 != null && var24.getState() != 4) {
               var4.cancelEdit();
               String var25 = var24.getError() != null ? var24.getError().toString() : "Failed to update config";
               throw new MigrationException(var25, var24.getError());
            }
         }

      } catch (EditNotEditorException var19) {
         throw new MigrationException("Failed to update config", var19);
      } catch (EditFailedException var20) {
         throw new MigrationException("Failed to update config", var20);
      } catch (EditChangesValidationException var21) {
         throw new MigrationException("Failed to update config", var21);
      } catch (EditSaveChangesFailedException var22) {
         throw new MigrationException("Failed to update config", var22);
      } catch (EditWaitTimedOutException var23) {
         throw new MigrationException("Failed to update config", var23);
      }
   }

   private static MigratableTargetMBean getMigratableTarget(DomainMBean var0, String var1) {
      MigratableTargetMBean[] var2 = var0.getMigratableTargets();
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (var2[var3].getName().equals(var1)) {
               return var2[var3];
            }
         }
      }

      return null;
   }

   private static ServerMBean getServer(DomainMBean var0, String var1) {
      ServerMBean[] var2 = var0.getServers();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var2[var3].getName().equals(var1)) {
            return var2[var3];
         }
      }

      return null;
   }
}
