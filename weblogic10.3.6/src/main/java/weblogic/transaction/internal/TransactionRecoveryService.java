package weblogic.transaction.internal;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import weblogic.cluster.migration.JTAMigrationHandler;
import weblogic.cluster.migration.Migratable;
import weblogic.cluster.migration.MigrationException;
import weblogic.cluster.migration.MigrationManager;
import weblogic.management.ManagementException;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.DeploymentMBean;
import weblogic.management.configuration.JTAMigratableTargetMBean;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.JTARecoveryRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;
import weblogic.work.WorkAdapter;
import weblogic.work.WorkManagerFactory;

public final class TransactionRecoveryService extends AbstractServerService implements Migratable {
   private String serverName;
   private boolean active = false;
   private boolean failBackInProgress = false;
   private JTARecoveryRuntimeMBeanImpl runtimeMBean;
   private MigratableTargetMBean mtConfig;
   private static final HashMap mServices = new HashMap(3);
   private static boolean ownRecoveryService = false;
   private static final Object startLock = new Object();
   private static boolean started;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   static final int TRS_MIGRATABLE_ORDER = -900;

   public TransactionRecoveryService() {
   }

   private TransactionRecoveryService(String var1) {
      this.serverName = var1;

      try {
         this.runtimeMBean = new JTARecoveryRuntimeMBeanImpl(this);
      } catch (ManagementException var3) {
      }

   }

   public void migratableInitialize() {
      if (TxDebug.JTAMigration.isDebugEnabled()) {
         TxDebug.JTAMigration.debug("migratableInitialize called for '" + this.serverName + "'");
      }

   }

   public void migratableActivate() throws MigrationException {
      if (TxDebug.JTAMigration.isDebugEnabled()) {
         TxDebug.JTAMigration.debug("migratableActivate called for '" + this.serverName + "', active:" + this.active);
      }

      ServerTransactionManagerImpl var1 = getTM();

      try {
         synchronized(this) {
            if (this.active) {
               return;
            }

            this.active = true;
         }

         if (this.serverName.equals(getLocalServerName())) {
            ownRecoveryService = true;
            var1.recover();
         } else {
            var1.recover(this.serverName);
         }

      } catch (Exception var7) {
         synchronized(this) {
            this.active = false;
         }

         TXLogger.logRecoveryServiceActivationFailed(this.serverName, var7);
         throw new MigrationException("Activation of TransactionRecoveryService for server '" + this.serverName + "' failed", var7);
      }
   }

   public void migratableDeactivate() throws MigrationException {
      if (TxDebug.JTAMigration.isDebugEnabled()) {
         TxDebug.JTAMigration.debug("migratableDeactivate called for '" + this.serverName + "', active:" + this.active);
      }

      if (!this.serverName.equals(getLocalServerName())) {
         synchronized(this) {
            if (!this.active) {
               return;
            }

            this.active = false;
         }

         getTM().suspendRecovery(this.serverName);
      } else {
         if (TransactionService.isRunning()) {
            TXLogger.logMigrateRecoveryServiceWhileServerActive();
            throw new MigrationException("Current server is still active.  Cannot migrate Transaction Recovery Service from current server.  For controlled migration, please shut down the server and then perform the manual migration.");
         }

         if (TransactionService.isSuspending() || TransactionService.isForceSuspending() || TransactionService.isShuttingDown()) {
            synchronized(this) {
               if (!this.active) {
                  return;
               }

               this.active = false;
            }

            getTM().checkpoint();
         }
      }

   }

   public int getOrder() {
      return -900;
   }

   String getServerName() {
      return this.serverName;
   }

   static final boolean isInCluster() {
      return ManagementService.getRuntimeAccess(kernelId).getServer().getCluster() != null;
   }

   boolean isActive() {
      return this.active;
   }

   private JTARecoveryRuntime getRuntimeMBean() {
      return this.runtimeMBean;
   }

   static void resume() throws ServiceFailureException {
      startOwnRecoveryIfNeeded();
      if (isInCluster()) {
         try {
            deployAllTransactionRecoveryServices();
         } catch (MigrationException var1) {
            throw new ServiceFailureException("Error occurred while registering or activating the Transaction Recovery Service for the current server.", var1);
         }

         checkTransactionLogOwnership();
         started = true;
      }
   }

   public void start() throws ServiceFailureException {
   }

   public void stop() {
      this.halt();
   }

   public void halt() {
   }

   static void forceSuspend() {
      synchronized(startLock) {
         if (!started) {
            return;
         }
      }

      stopOwnRecoveryIfNeeded();
      if (isInCluster()) {
         Iterator var0 = mServices.values().iterator();

         while(var0.hasNext()) {
            TransactionRecoveryService var1 = (TransactionRecoveryService)var0.next();
            var1.cleanup();
            var0.remove();
         }

      }
   }

   private static void startOwnRecoveryIfNeeded() throws ServiceFailureException {
      if (!isInCluster()) {
         ServerTransactionManagerImpl var0 = getTM();
         TransactionRecoveryService var1 = getOrCreate(getLocalServerName());

         try {
            var0.recover();
            var1.active = true;
            ownRecoveryService = true;
            var0.setJdbcTLogInitialized(true);
         } catch (Exception var3) {
            throw new ServiceFailureException("Fatal error creating or processing transaction log during crash recovery", var3);
         }
      }
   }

   private static void stopOwnRecoveryIfNeeded() {
      if (!isInCluster()) {
         getTM().checkpoint();
      }

   }

   static void scheduleFailBack(String var0) {
      if (requestFailBack(var0)) {
         if (TxDebug.JTAMigration.isDebugEnabled()) {
            TxDebug.JTAMigration.debug("MigratedTLog.scheduleFailBack for '" + var0 + "'");
         }

         WorkManagerFactory.getInstance().getSystem().schedule(new FailBackRequest(var0));
      }

   }

   static JTARecoveryRuntime getRuntimeMBean(String var0) {
      TransactionRecoveryService var1 = get(var0);
      return var1 != null ? var1.getRuntimeMBean() : null;
   }

   static JTARecoveryRuntimeMBean[] getAllRuntimeMBeans() {
      Vector var0 = new Vector(mServices.size());
      synchronized(mServices) {
         Iterator var2 = mServices.values().iterator();

         while(true) {
            if (!var2.hasNext()) {
               break;
            }

            TransactionRecoveryService var3 = (TransactionRecoveryService)var2.next();
            var0.add(var3.getRuntimeMBean());
         }
      }

      JTARecoveryRuntimeMBean[] var1 = new JTARecoveryRuntimeMBean[var0.size()];
      var0.toArray(var1);
      return var1;
   }

   private void cleanup() {
      if (this.mtConfig != null) {
         try {
            if (TxDebug.JTAMigration.isDebugEnabled()) {
               TxDebug.JTAMigration.debug("Unregistering migratable for '" + this.serverName + "', isManualActiveOn: " + this.mtConfig.isManualActiveOn(ManagementService.getRuntimeAccess(kernelId).getServer()) + "', userPreferredServer: " + this.mtConfig.getUserPreferredServer().getName() + ", migrationPolicy: " + this.mtConfig.getMigrationPolicy());
            }

            MigrationManager.singleton().unregister(this, this.mtConfig);
            if (TxDebug.JTAMigration.isDebugEnabled()) {
               TxDebug.JTAMigration.debug("Migratable for '" + this.serverName + "' unregistered");
            }
         } catch (MigrationException var3) {
            if (TxDebug.JTAMigration.isDebugEnabled()) {
               TxDebug.JTAMigration.debug("Migratable for '" + this.serverName + "' unregistration failed", var3);
            }

            TXLogger.logRecoveryServiceUnregistrationFailed(this.serverName);
         }
      }

      if (this.runtimeMBean != null) {
         try {
            this.runtimeMBean.unregister();
         } catch (ManagementException var2) {
         }

         this.runtimeMBean = null;
      }

   }

   private static TransactionRecoveryService get(String var0) {
      synchronized(mServices) {
         return (TransactionRecoveryService)mServices.get(var0);
      }
   }

   private static TransactionRecoveryService getOrCreate(String var0) {
      synchronized(mServices) {
         TransactionRecoveryService var1 = (TransactionRecoveryService)mServices.get(var0);
         if (var1 != null) {
            return var1;
         } else {
            var1 = new TransactionRecoveryService(var0);
            mServices.put(var0, var1);
            return var1;
         }
      }
   }

   private static TransactionRecoveryService getOrCreate(ServerMBean var0) throws MigrationException {
      TransactionRecoveryService var1 = getOrCreate(var0.getName());
      var1.mtConfig = var0.getJTAMigratableTarget();
      if (var1.mtConfig != null) {
         String var2 = var0.getName();

         try {
            if (TxDebug.JTAMigration.isDebugEnabled()) {
               TxDebug.JTAMigration.debug("Registering migratable for '" + var2 + "', isManualActiveOn: " + var1.mtConfig.isManualActiveOn(ManagementService.getRuntimeAccess(kernelId).getServer()) + "', userPreferredServer: " + var1.mtConfig.getUserPreferredServer().getName() + ", migrationPolicy: " + var1.mtConfig.getMigrationPolicy());
            }

            MigrationManager.singleton().register(var1, var1.mtConfig);
            if (TxDebug.JTAMigration.isDebugEnabled()) {
               TxDebug.JTAMigration.debug("Migratable for '" + var2 + "' registered");
            }
         } catch (MigrationException var4) {
            if (TxDebug.JTAMigration.isDebugEnabled()) {
               TxDebug.JTAMigration.debug("Migratable for '" + var2 + "' registration failed", var4);
            }

            TXLogger.logRecoveryServiceRegistrationFailed(var2);
            if (var2.equals(getLocalServerName())) {
               throw var4;
            }
         }
      }

      return var1;
   }

   private static void remove(String var0) {
      TransactionRecoveryService var1 = get(var0);
      if (var1 != null) {
         var1.cleanup();
         synchronized(mServices) {
            mServices.remove(var0);
         }
      }

   }

   private static void deployAllTransactionRecoveryServices() throws MigrationException {
      ClusterMBean var0 = ManagementService.getRuntimeAccess(kernelId).getServer().getCluster();
      ServerMBean[] var1 = var0.getServers();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         ServerMBean var3 = var1[var2];
         String var4 = var1[var2].getName();
         JTAMigratableTargetMBean var5 = var3.getJTAMigratableTarget();
         if (isLocalServerIncluded(var5.getAllCandidateServers())) {
            getOrCreate(var3);
         }

         if (!var4.equals(getLocalServerName())) {
            var5.addPropertyChangeListener(new DeploymentChangeListener(var4));
            if (TxDebug.JTAMigration.isDebugEnabled()) {
               TxDebug.JTAMigration.debug("Added DeploymentChangeListener for '" + var4 + "'");
            }
         }
      }

   }

   private static void checkTransactionLogOwnership() throws ServiceFailureException {
      if (ownRecoveryService) {
         if (TxDebug.JTAMigration.isDebugEnabled()) {
            TxDebug.JTAMigration.debug("Successfully gained TLOG ownership.");
         }

      } else {
         TXLogger.logGetTransactionLogOwnershipError();
         throw new ServiceFailureException("Cannot get ownership of Transaction Log.  Make sure that the Transaction Recovery Migratable Service is migrated back to the current server before restarting it.");
      }
   }

   public void prepareDeployments(Collection<DeploymentMBean> var1, boolean var2) {
   }

   public void activateDeployments() {
      initialize();
   }

   public void rollbackDeployments() {
   }

   public void destroyDeployments(Collection<DeploymentMBean> var1) {
   }

   public static void initialize() {
      try {
         synchronized(startLock) {
            if (!started) {
               PlatformHelper.getPlatformHelper().openPrimaryStore(false);
               if (PlatformHelper.getPlatformHelper().getPrimaryStore() == null) {
                  String var1 = "Transaction Log PrimaryStore can not be set";
                  getTM().registerFailedPrimaryStore(new ServiceFailureException(var1));
                  TXLogger.logFailedTLOGBoot(var1);
               } else {
                  resume();
               }
            }
         }
      } catch (ServiceFailureException var4) {
         TXLogger.logFailedActivateDeployments(var4);
      }

   }

   private static boolean requestFailBack(String var0) {
      TransactionRecoveryService var1 = get(var0);
      if (var1 != null) {
         synchronized(var1) {
            if (!var1.failBackInProgress && var1.active) {
               var1.failBackInProgress = true;
               return true;
            } else {
               return false;
            }
         }
      } else {
         return false;
      }
   }

   private static void failBack(String var0, boolean var1) {
      boolean var16 = false;

      TransactionRecoveryService var2;
      label174: {
         try {
            var16 = true;
            if (JTAMigrationHandler.isAvailable(var0)) {
               try {
                  migrateJTA(var0, var0, true, var1);
                  if (TxDebug.JTAMigration.isDebugEnabled()) {
                     TxDebug.JTAMigration.debug("Successfully failed back to '" + var0 + "'");
                     var16 = false;
                  } else {
                     var16 = false;
                  }
               } catch (MigrationException var21) {
                  if (TxDebug.JTAMigration.isDebugEnabled()) {
                     TxDebug.JTAMigration.debug("Recovery is done, but fail-back to '" + var0 + "'" + " failed.", var21);
                  }

                  if (var1) {
                     TXLogger.logRecoveryServiceFailbackFailed(var0);
                     var16 = false;
                  } else {
                     TxDebug.JTAMigration.debug("Try again with destinationUp=true");

                     try {
                        migrateJTA(var0, var0, true, true);
                        if (TxDebug.JTAMigration.isDebugEnabled()) {
                           TxDebug.JTAMigration.debug("Successfully failed back to '" + var0 + "'");
                           var16 = false;
                        } else {
                           var16 = false;
                        }
                     } catch (MigrationException var20) {
                        if (TxDebug.JTAMigration.isDebugEnabled()) {
                           TxDebug.JTAMigration.debug("Recovery is done, but fail-back to '" + var0 + "'" + " failed.", var20);
                        }

                        TXLogger.logRecoveryServiceFailbackRetryFailed(var0);
                        var16 = false;
                     }
                  }
               }
               break label174;
            }

            if (TxDebug.JTAMigration.isDebugEnabled()) {
               TxDebug.JTAMigration.debug("Migrator is not available. Will skip failback.");
               var16 = false;
            } else {
               var16 = false;
            }
         } finally {
            if (var16) {
               TransactionRecoveryService var7 = get(var0);
               if (var7 != null) {
                  synchronized(var7) {
                     var7.failBackInProgress = false;
                  }
               }

            }
         }

         var2 = get(var0);
         if (var2 != null) {
            synchronized(var2) {
               var2.failBackInProgress = false;
            }
         }

         return;
      }

      var2 = get(var0);
      if (var2 != null) {
         synchronized(var2) {
            var2.failBackInProgress = false;
         }
      }

   }

   static final void failbackIfNeeded() {
      try {
         if (isInCluster()) {
            if (ManagementService.getRuntimeAccess(kernelId).isAdminServer() && !isAutomaticMigrationMode()) {
               if (TxDebug.JTAMigration.isDebugEnabled()) {
                  TxDebug.JTAMigration.debug("AdminServer itself is in cluster, and it is manual JTA migration policy. Will skip TRS failback.");
               }

            } else {
               if (TxDebug.JTAMigration.isDebugEnabled()) {
                  TxDebug.JTAMigration.debug("Going to deactivate JTAMT...");
               }

               JTAMigrationHandler.deactivateJTA(getLocalServerName(), getLocalServerName());
               if (TxDebug.JTAMigration.isDebugEnabled()) {
                  TxDebug.JTAMigration.debug("Deactivated JTAMT");
               }

            }
         }
      } catch (Exception var1) {
         if (TxDebug.JTAMigration.isDebugEnabled()) {
            TxDebug.JTAMigration.debug("Failed to deactivate JTAMT", var1);
         }

         if (!isStrictOwnershipCheck() && !JTAMigrationHandler.isAvailable(getLocalServerName())) {
            TXLogger.logMigratorNotAvailable(getLocalServerName());
         } else {
            throw new MigrationException("Could not start JTAMT on local server because it could not be deactivated on the current host.", var1);
         }
      }
   }

   private static void migrateJTA(String var0, String var1, boolean var2, boolean var3) throws MigrationException {
      try {
         SecurityServiceManager.runAs(kernelId, kernelId, new MigrateJTAAction(var0, var1, var2, var3));
      } catch (PrivilegedActionException var6) {
         Exception var5 = var6.getException();
         if (var5 instanceof RuntimeException) {
            throw (RuntimeException)var5;
         } else if (var5 instanceof MigrationException) {
            throw (MigrationException)var5;
         } else {
            throw new MigrationException("Unexpected exception thrown during migrate operation.", var5);
         }
      }
   }

   private static ServerMBean getServerConfigMBean(String var0) {
      return ManagementService.getRuntimeAccess(kernelId).getDomain().lookupServer(var0);
   }

   static final String getLocalServerName() {
      return ManagementService.getRuntimeAccess(kernelId).getServer().getName();
   }

   static final ServerMBean getLocalServer() {
      return ManagementService.getRuntimeAccess(kernelId).getServer();
   }

   static final boolean isAutomaticMigrationMode() {
      return getLocalServer().getJTAMigratableTarget() != null && "failure-recovery".equals(getLocalServer().getJTAMigratableTarget().getMigrationPolicy());
   }

   private static final boolean isStrictOwnershipCheck() {
      return getLocalServer().getJTAMigratableTarget() != null && getLocalServer().getJTAMigratableTarget().isStrictOwnershipCheck();
   }

   private static ServerTransactionManagerImpl getTM() {
      return (ServerTransactionManagerImpl)ServerTransactionManagerImpl.getTransactionManager();
   }

   private static boolean isLocalServerIncluded(ServerMBean[] var0) {
      for(int var1 = 0; var1 < var0.length; ++var1) {
         if (var0[var1].getName().equals(getLocalServerName())) {
            return true;
         }
      }

      return false;
   }

   private static final class DeploymentChangeListener implements PropertyChangeListener {
      final String serverName;

      DeploymentChangeListener(String var1) {
         this.serverName = var1;
      }

      public void propertyChange(PropertyChangeEvent var1) {
         if (var1.getPropertyName().equals("ConstrainedCandidateServers")) {
            boolean var2 = TransactionRecoveryService.isLocalServerIncluded((ServerMBean[])((ServerMBean[])var1.getNewValue()));
            if (TxDebug.JTAMigration.isDebugEnabled()) {
               TxDebug.JTAMigration.debug("Received deployment change notification for '" + this.serverName + "', localServerIncluded: " + var2);
            }

            if (var2) {
               try {
                  TransactionRecoveryService.getOrCreate(TransactionRecoveryService.getServerConfigMBean(this.serverName));
               } catch (MigrationException var4) {
                  if (TxDebug.JTAMigration.isDebugEnabled()) {
                     TxDebug.JTAMigration.debug("Processing of deployment change notification for '" + this.serverName + "' failed", var4);
                  }
               } catch (Exception var5) {
                  if (TxDebug.JTAMigration.isDebugEnabled()) {
                     TxDebug.JTAMigration.debug("Processing of deployment change notification for '" + this.serverName + "' failed", var5);
                  }
               }
            } else {
               TransactionRecoveryService.remove(this.serverName);
            }
         }

      }
   }

   private static class MigrateJTAAction implements PrivilegedExceptionAction {
      String migratableName;
      String serverName;
      boolean sourceUp;
      boolean destinationUp;

      MigrateJTAAction(String var1, String var2, boolean var3, boolean var4) {
         this.migratableName = var1;
         this.serverName = var2;
         this.sourceUp = var3;
         this.destinationUp = var4;
      }

      public Object run() throws Exception {
         JTAMigrationHandler.migrateJTA(this.migratableName, this.serverName, this.sourceUp, this.destinationUp);
         return null;
      }
   }

   private static final class FailBackRequest extends WorkAdapter {
      final String serverName;

      FailBackRequest(String var1) {
         this.serverName = var1;
      }

      public void run() {
         TransactionRecoveryService.failBack(this.serverName, false);
      }

      public String toString() {
         return "Recovery fail-back request for '" + this.serverName + "'";
      }
   }
}
