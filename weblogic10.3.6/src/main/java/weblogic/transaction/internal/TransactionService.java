package weblogic.transaction.internal;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.AccessController;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.transaction.SystemException;
import weblogic.diagnostics.image.ImageManager;
import weblogic.management.ManagementException;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JTAMBean;
import weblogic.management.provider.ManagementService;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;
import weblogic.transaction.ClientTransactionManager;
import weblogic.transaction.TransactionHelper;
import weblogic.work.WorkManagerFactory;

public class TransactionService extends AbstractServerService implements Constants {
   private static ClientTransactionManagerImpl ctm = null;
   public static final int FORCE_SUSPENDING = 1004;
   private static int state = 0;
   private static Object stateLock = new String("LifecycleState");
   private static Object suspendLock = new String("LifecycleSuspend");
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static TransactionService singleton = null;

   private void initialize() throws ServiceFailureException {
      if (!this.initStateTimerAndTxHelper()) {
         if (ctm == null) {
            this.initTM();
         }

         this.initCheckTimerRegisterWithHealthServiceAndSetState();
      }
   }

   boolean initStateTimerAndTxHelper() {
      synchronized(stateLock) {
         if (state != 0) {
            return true;
         }

         state = 1;
      }

      if (TxDebug.JTALifecycle.isDebugEnabled()) {
         TxDebug.JTALifecycle.debug("INITIALIZING ...");
      }

      WLSTimer.initialize();
      initializeEarly();
      return false;
   }

   private void initTM() throws ServiceFailureException {
      JTAMBean var1 = this.initJNDIConfigMBeanAndTM();
      this.initJTACoordinatorWM();
      this.initCoordinatorAndSetTxTimeoutThread(var1);
   }

   JTAMBean initJNDIConfigMBeanAndTM() throws ServiceFailureException {
      try {
         JNDIAdvertiser.initialize(getServerName());
      } catch (NamingException var2) {
         throw new ServiceFailureException(var2);
      }

      JTAMBean var1 = getConfiguration();
      ctm = createTransactionManager(var1);
      return var1;
   }

   void initJTACoordinatorWM() {
      WorkManagerFactory.getInstance().findOrCreate("JTACoordinatorWM", 100, 3, -1);
      WorkManagerFactory.getInstance().findOrCreate("OneWayJTACoordinatorWM", 100, 3, -1);
   }

   void initCoordinatorAndSetTxTimeoutThread(JTAMBean var1) throws ServiceFailureException {
      createCoordinator(ctm);
      setTransactionTimeoutMainThread(var1);
   }

   void initCheckTimerRegisterWithHealthServiceAndSetState() throws ServiceFailureException {
      if (!ctm.isTimerStarted()) {
         ClientTransactionManagerImpl var10003 = ctm;
         throw new ServiceFailureException("JTA Timer did not start: ", ClientTransactionManagerImpl.getTimerFailureReason());
      } else {
         ((JTARuntimeImpl)getTM().getRuntime()).registerWithHealthService();
         synchronized(stateLock) {
            if (state == 1) {
               state = 3;
            }
         }

         if (TxDebug.JTALifecycle.isDebugEnabled()) {
            TxDebug.JTALifecycle.debug("INITIALIZING DONE");
         }

      }
   }

   public void start() throws ServiceFailureException {
      this.initialize();
      this.resume();
   }

   private void resume() {
      if (TxDebug.JTALifecycle.isDebugEnabled()) {
         TxDebug.JTALifecycle.debug("RESUMING ...");
      }

      synchronized(stateLock) {
         if (state != 3) {
            if (TxDebug.JTALifecycle.isDebugEnabled()) {
               TxDebug.JTALifecycle.debug("Skip resuming, state:" + state);
            }

            return;
         }

         state = 6;
      }

      TransactionImpl.setAbandonGraceTimeEndSec((int)(System.currentTimeMillis() / 1000L) + 600);
      synchronized(stateLock) {
         if (state == 6) {
            state = 2;
         }
      }

      if (TxDebug.JTALifecycle.isDebugEnabled()) {
         TxDebug.JTALifecycle.debug("RESUMING DONE");
      }

   }

   public static void initializeEarly() {
      TransactionHelper.setTransactionHelper(new TransactionHelperImpl());
   }

   public void stop() throws ServiceFailureException {
      if (TxDebug.JTALifecycle.isDebugEnabled()) {
         TxDebug.JTALifecycle.debug("SUSPENDING ...");
      }

      synchronized(stateLock) {
         if (state != 2) {
            if (TxDebug.JTALifecycle.isDebugEnabled()) {
               TxDebug.JTALifecycle.debug("Skip suspending, state:" + state);
            }

            return;
         }

         state = 4;
      }

      TransactionRecoveryService.forceSuspend();
      ServerTransactionManagerImpl var1 = getTM();
      synchronized(suspendLock) {
         label251: {
            boolean var17 = false;

            label242: {
               try {
                  var17 = true;
                  if (!var1.isTxMapEmpty()) {
                     suspendLock.wait();
                     var17 = false;
                  } else {
                     state = 3;
                     var17 = false;
                  }
                  break label242;
               } catch (InterruptedException var21) {
                  var17 = false;
               } finally {
                  if (var17) {
                     synchronized(stateLock) {
                        if (state == 3) {
                           if (TxDebug.JTALifecycle.isDebugEnabled()) {
                              TxDebug.JTALifecycle.debug("suspendSuccessfullyCompleted");
                           }

                        }

                        if (TxDebug.JTALifecycle.isDebugEnabled()) {
                           TxDebug.JTALifecycle.debug("suspendFailed");
                        }

                        throw new ServiceFailureException("TransactionService suspend failed");
                     }
                  }
               }

               synchronized(stateLock) {
                  if (state != 3) {
                     if (TxDebug.JTALifecycle.isDebugEnabled()) {
                        TxDebug.JTALifecycle.debug("suspendFailed");
                     }

                     throw new ServiceFailureException("TransactionService suspend failed");
                  }

                  if (TxDebug.JTALifecycle.isDebugEnabled()) {
                     TxDebug.JTALifecycle.debug("suspendSuccessfullyCompleted");
                  }
                  break label251;
               }
            }

            synchronized(stateLock) {
               if (state != 3) {
                  if (TxDebug.JTALifecycle.isDebugEnabled()) {
                     TxDebug.JTALifecycle.debug("suspendFailed");
                  }

                  throw new ServiceFailureException("TransactionService suspend failed");
               }

               if (TxDebug.JTALifecycle.isDebugEnabled()) {
                  TxDebug.JTALifecycle.debug("suspendSuccessfullyCompleted");
               }
            }
         }
      }

      if (TxDebug.JTALifecycle.isDebugEnabled()) {
         TxDebug.JTALifecycle.debug("SUSPEND DONE");
      }

      this.shutdown();
   }

   public void halt() throws ServiceFailureException {
      if (TxDebug.JTALifecycle.isDebugEnabled()) {
         TxDebug.JTALifecycle.debug("FORCE SUSPENDING ...");
      }

      synchronized(stateLock) {
         if (state != 2 && state != 4) {
            if (TxDebug.JTALifecycle.isDebugEnabled()) {
               TxDebug.JTALifecycle.debug("Skip force suspending, state:" + state);
            }

            return;
         }

         state = 1004;
      }

      performForceSuspend();
      synchronized(stateLock) {
         if (state == 1004) {
            state = 3;
         }
      }

      if (TxDebug.JTALifecycle.isDebugEnabled()) {
         TxDebug.JTALifecycle.debug("FORCE SUSPEND DONE");
      }

      this.shutdown();
   }

   public void shutdown() throws ServiceFailureException {
      if (TxDebug.JTALifecycle.isDebugEnabled()) {
         TxDebug.JTALifecycle.debug("SHUTTING DOWN ...");
      }

      synchronized(stateLock) {
         if (state == 7 || state == 0) {
            if (TxDebug.JTALifecycle.isDebugEnabled()) {
               TxDebug.JTALifecycle.debug("Skip shutdown, state:" + state);
            }

            return;
         }

         state = 7;
      }

      ((JTARuntimeImpl)getTM().getRuntime()).unregisterFromHealthService();
      synchronized(stateLock) {
         state = 0;
      }

      if (TxDebug.JTALifecycle.isDebugEnabled()) {
         TxDebug.JTALifecycle.debug("SHUTDOWN DONE");
      }

   }

   static boolean isRunning() {
      return state == 2;
   }

   static boolean isSuspending() {
      return state == 4;
   }

   static boolean isForceSuspending() {
      return state == 1004;
   }

   static boolean isShuttingDown() {
      return state == 7;
   }

   static void suspendDone() {
      synchronized(suspendLock) {
         if (state == 4) {
            state = 3;
         }

         suspendLock.notify();
      }
   }

   private static void performForceSuspend() {
      if (TxDebug.JTALifecycle.isDebugEnabled()) {
         TxDebug.JTALifecycle.debug("Performing forceSuspend ...");
      }

      synchronized(suspendLock) {
         suspendLock.notify();
      }

      TransactionRecoveryService.forceSuspend();
      getTM().dropAllTransactions();
   }

   private static ClientTransactionManagerImpl createTransactionManager(JTAMBean var0) throws ServiceFailureException {
      ServerTransactionManagerImpl var1 = new ServerTransactionManagerImpl(new JTAConfigImpl(var0), getServerName());
      var1.setLocalCoordinatorDescriptor(getLocalCoordinatorDescriptor());
      var1.setProcessManager(new ProcessManagerImpl());

      try {
         var1.setJTARuntime(new JTARuntimeImpl("JTARuntime", var1));
      } catch (ManagementException var3) {
         throw new ServiceFailureException(var3);
      }

      ImageManager var2 = ImageManager.getInstance();
      var2.registerImageSource("JTA", new JTADiagnosticImageSource(var1));
      ctm = new ClientTransactionManagerImpl();
      ctm.setCoordinatorURL(getLocalCoordinatorURL());
      ctm.setAbandonTimeoutSeconds(var0.getAbandonTimeoutSeconds());
      ctm.setDefaultTimeoutSeconds(var0.getTimeoutSeconds());
      ClientTransactionManagerImpl var10000 = ctm;
      ClientTransactionManagerImpl.initialized = true;
      JNDIAdvertiser.advertiseTransactionManager(ctm);
      JNDIAdvertiser.advertiseUserTransaction(ctm);
      JNDIAdvertiser.advertiseTransactionSynchronizationRegistry(ctm);
      return ctm;
   }

   private static void setTransactionTimeoutMainThread(JTAMBean var0) throws ServiceFailureException {
      try {
         TransactionHelper var1 = TransactionHelper.getTransactionHelper();
         ClientTransactionManager var2 = var1.getTransactionManager();
         var2.setTransactionTimeout(var0.getTimeoutSeconds());
      } catch (SystemException var3) {
         throw new ServiceFailureException("Setting transaction timeout " + var3);
      }
   }

   private static String getServerName() {
      return ManagementService.getRuntimeAccess(kernelId).getServer().getName();
   }

   private static CoordinatorImpl createCoordinator(ClientTransactionManagerImpl var0) throws ServiceFailureException {
      CoordinatorImpl var1 = new CoordinatorImpl();
      var0.setCoordinator(var1);
      getTM().setLocalCoordinator(var1);

      try {
         Context var2 = JNDIAdvertiser.getServerContext();
         var2.bind(JNDIAdvertiser.getServerName(), var0);
         ServerHelper.exportObject((Remote)var0.getCoordinator());
         return var1;
      } catch (NamingException var3) {
         TXLogger.logAdvertiseCoordinatorError(var3);
         throw new ServiceFailureException(var3);
      } catch (RemoteException var4) {
         TXLogger.logExportCoordinatorObjIDError(var4);
         throw new ServiceFailureException(var4);
      }
   }

   private static JTAMBean getConfiguration() throws ServiceFailureException {
      DomainMBean var0 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      JTAMBean var1 = var0.getJTA();
      if (var1 == null) {
         throw new ServiceFailureException("Unable to obtain configuration for transaction service");
      } else {
         return var1;
      }
   }

   private static CoordinatorDescriptor getLocalCoordinatorDescriptor() throws ServiceFailureException {
      try {
         return ((ServerCoordinatorDescriptorManager)PlatformHelper.getPlatformHelper().getCoordinatorDescriptorManager()).getLocalCoordinatorDescriptor();
      } catch (Exception var1) {
         throw new ServiceFailureException("Transaction service startup failure", var1);
      }
   }

   private static String getLocalCoordinatorURL() throws ServiceFailureException {
      try {
         return ((ServerCoordinatorDescriptorManager)PlatformHelper.getPlatformHelper().getCoordinatorDescriptorManager()).getLocalCoordinatorURL();
      } catch (Exception var1) {
         throw new ServiceFailureException("Transaction service startup failure", var1);
      }
   }

   private static ServerTransactionManagerImpl getTM() {
      return (ServerTransactionManagerImpl)TransactionManagerImpl.getTransactionManager();
   }

   public TransactionService() {
      singleton = this;
   }

   public static TransactionService getTransactionService() {
      return singleton;
   }

   public String getName() {
      return "Transaction Service";
   }

   public String getVersion() {
      return "JTA 1.1";
   }
}
