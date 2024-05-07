package weblogic.transaction.internal;

import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public final class PostLoggingResourceService extends AbstractServerService {
   private static int state = 0;

   public void stop() throws ServiceFailureException {
      state = 4;
   }

   public void halt() throws ServiceFailureException {
      state = 3;
   }

   public void start() throws ServiceFailureException {
      ServerTransactionManagerImpl var1 = (ServerTransactionManagerImpl)TransactionManagerImpl.getTransactionManager();
      Throwable var2 = var1.getLLRBootException();
      if (var2 != null) {
         ServiceFailureException var5 = new ServiceFailureException(TXExceptionLogger.logFailedLLRRecoverLoggable(var2.toString()).getMessage());
         var5.initCause(var1.getLLRBootException());
         throw var5;
      } else {
         Throwable var3 = var1.getPrimaryStoreBootException();
         if (var3 != null) {
            ServiceFailureException var4 = new ServiceFailureException(TXExceptionLogger.logFailedPrimaryStoreRecoverLoggable(var3.toString()).getMessage());
            var4.initCause(var1.getPrimaryStoreBootException());
            throw var4;
         } else {
            state = 2;
         }
      }
   }

   static boolean isSuspending() {
      return state == 4;
   }

   static void suspendDone() {
      if (state == 4) {
         state = 3;
      }

   }
}
