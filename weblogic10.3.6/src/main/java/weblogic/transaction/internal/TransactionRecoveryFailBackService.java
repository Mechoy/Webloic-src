package weblogic.transaction.internal;

import weblogic.cluster.migration.MigrationException;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public final class TransactionRecoveryFailBackService extends AbstractServerService {
   public void start() throws ServiceFailureException {
      try {
         TransactionRecoveryService.failbackIfNeeded();
      } catch (MigrationException var2) {
         throw new ServiceFailureException("Error occurred while trying to fail back Transaction Recovery Service.", var2);
      }
   }

   public void stop() {
   }

   public void halt() {
   }
}
