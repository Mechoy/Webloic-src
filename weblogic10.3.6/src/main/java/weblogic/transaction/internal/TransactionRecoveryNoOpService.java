package weblogic.transaction.internal;

import weblogic.cluster.migration.Migratable;
import weblogic.cluster.migration.MigrationException;
import weblogic.cluster.migration.MigrationManager;
import weblogic.management.configuration.JTAMigratableTargetMBean;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public class TransactionRecoveryNoOpService extends AbstractServerService implements Migratable {
   private static final boolean DEBUG = false;

   public void start() throws ServiceFailureException {
      if (TransactionRecoveryService.isInCluster()) {
         JTAMigratableTargetMBean var1 = TransactionRecoveryService.getLocalServer().getJTAMigratableTarget();
         if (TxDebug.JTAMigration.isDebugEnabled()) {
            TxDebug.JTAMigration.debug("Register no-op migratable on JTAMT [" + TransactionRecoveryService.getLocalServerName() + "] ...");
         }

         try {
            MigrationManager.singleton().register(this, var1);
         } catch (MigrationException var3) {
            throw new ServiceFailureException("Error occurred while registering Transaction Recovery No-Op Service.", var3);
         }
      }
   }

   public void stop() throws ServiceFailureException {
      this.halt();
   }

   public void halt() throws ServiceFailureException {
      if (TransactionRecoveryService.isInCluster()) {
         JTAMigratableTargetMBean var1 = TransactionRecoveryService.getLocalServer().getJTAMigratableTarget();
         if (TxDebug.JTAMigration.isDebugEnabled()) {
            TxDebug.JTAMigration.debug("UnRegister no-op migratable on JTAMT [" + TransactionRecoveryService.getLocalServerName() + "] ...");
         }

         try {
            MigrationManager.singleton().unregister(this, var1);
         } catch (MigrationException var3) {
            throw new ServiceFailureException("Error occurred while unregistering Transaction Recovery No-Op Service.", var3);
         }
      }
   }

   public int getOrder() {
      return -901;
   }

   public void migratableActivate() throws MigrationException {
   }

   public void migratableDeactivate() throws MigrationException {
   }

   public void migratableInitialize() {
   }
}
