package weblogic.transaction.internal;

import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public final class ClientInitiatedTxShutdownService extends AbstractServerService {
   private static int state = 0;
   private static final Object stateLock = new String("LifecycleState");
   private static final Object suspendLock = new String("LifecycleSuspend");

   public void stop() throws ServiceFailureException {
      if (TxDebug.JTALifecycle.isDebugEnabled()) {
         TxDebug.JTALifecycle.debug("SUSPENDING ...");
      }

      state = 4;
      ServerTransactionManagerImpl var1 = getTM();
      synchronized(suspendLock) {
         try {
            if (!var1.isTxMapEmpty()) {
               TXLogger.logPendingTxDuringShutdown();
               suspendLock.wait((long)(getTM().getTransactionTimeout() * 1000));
               state = 3;
            } else {
               state = 3;
            }
         } catch (InterruptedException var16) {
         } finally {
            synchronized(stateLock) {
               if (state == 3) {
                  if (TxDebug.JTALifecycle.isDebugEnabled()) {
                     TxDebug.JTALifecycle.debug("suspendSuccessfullyCompleted");
                  }

               }

               if (TxDebug.JTALifecycle.isDebugEnabled()) {
                  TxDebug.JTALifecycle.debug("suspendFailed");
               }

               throw new ServiceFailureException("ClientInitiatedTransactionService suspend failed");
            }
         }
      }

      if (TxDebug.JTALifecycle.isDebugEnabled()) {
         TxDebug.JTALifecycle.debug("SUSPEND DONE");
      }

   }

   public void halt() throws ServiceFailureException {
      state = 3;
   }

   public void start() throws ServiceFailureException {
      state = 2;
   }

   static boolean isSuspending() {
      return state == 4;
   }

   static void suspendDone() {
      synchronized(suspendLock) {
         if (state == 4) {
            state = 3;
         }

         suspendLock.notify();
      }
   }

   private static ServerTransactionManagerImpl getTM() {
      return (ServerTransactionManagerImpl)TransactionManagerImpl.getTransactionManager();
   }

   public static boolean isTxMapEmpty() {
      return getTM().isTxMapEmpty();
   }

   public static int getTxTimeoutMillis() {
      return getTM().getTransactionTimeout() * 1000;
   }
}
