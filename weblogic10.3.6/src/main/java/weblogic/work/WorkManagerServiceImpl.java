package weblogic.work;

import java.security.AccessController;
import weblogic.management.configuration.WorkManagerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManagerFactory;
import weblogic.transaction.internal.ClientInitiatedTxShutdownService;
import weblogic.utils.StackTraceUtils;

public final class WorkManagerServiceImpl extends WorkManagerLifecycleImpl implements WorkManagerService {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final int PENDING_TX_TIMER_INTERVAL = 2000;
   private WorkManagerService.WorkListener rmiManager;
   private boolean allowRMIWork = false;

   public static WorkManagerService createService(String var0, String var1, String var2) {
      return createService(var0, var1, (String)var2, (StuckThreadManager)null);
   }

   public static WorkManagerService createService(String var0, String var1, String var2, StuckThreadManager var3) {
      if (ManagementService.getRuntimeAccess(kernelId).getServer().getUse81StyleExecuteQueues()) {
         WorkManagerFactory var7 = WorkManagerFactory.getInstance();
         WorkManager var6 = var7.findOrCreate(var0, -1, -1);
         return new WorkManagerServiceImpl(var6);
      } else {
         WorkManager var4 = ServerWorkManagerFactory.create(var0, var1, var2, var3);
         WorkManagerServiceImpl var5 = new WorkManagerServiceImpl(var4);
         ((ServerWorkManagerImpl)var4).setWorkManagerService(var5);
         return var5;
      }
   }

   public static WorkManagerService createService(String var0, String var1, WorkManagerMBean var2, StuckThreadManager var3) {
      WorkManagerFactory var4 = WorkManagerFactory.getInstance();
      if (ManagementService.getRuntimeAccess(kernelId).getServer().getUse81StyleExecuteQueues()) {
         int var8 = var2.getMaxThreadsConstraint() != null ? var2.getMaxThreadsConstraint().getCount() : -1;
         int var9 = var2.getMinThreadsConstraint() != null ? var2.getMinThreadsConstraint().getCount() : -1;
         WorkManager var7 = var4.findOrCreate(var2.getName(), var8, var9);
         return new WorkManagerServiceImpl(var7);
      } else {
         ServerWorkManagerImpl var5 = (ServerWorkManagerImpl)ServerWorkManagerFactory.create(var0, var1, var2, var3);
         WorkManagerServiceImpl var6 = new WorkManagerServiceImpl(var5);
         var5.setWorkManagerService(var6);
         return var6;
      }
   }

   public static WorkManagerService createService(String var0, String var1, String var2, RequestClass var3, MaxThreadsConstraint var4, MinThreadsConstraint var5, OverloadManager var6, StuckThreadManager var7) {
      if (ManagementService.getRuntimeAccess(kernelId).getServer().getUse81StyleExecuteQueues()) {
         WorkManagerFactory var10 = WorkManagerFactory.getInstance();
         WorkManager var11 = var10.findOrCreate(var0, var4 != null ? var4.getCount() : -1, var5 != null ? var5.getCount() : -1);
         return new WorkManagerServiceImpl(var11);
      } else {
         ServerWorkManagerImpl var8 = (ServerWorkManagerImpl)ServerWorkManagerFactory.create(var0, var1, var2, var3, var4, var5, var6, var7);
         WorkManagerServiceImpl var9 = new WorkManagerServiceImpl(var8);
         var8.setWorkManagerService(var9);
         return var9;
      }
   }

   private WorkManagerServiceImpl(WorkManager var1) {
      super(var1);
      if (debugEnabled()) {
         debug("-- wmservice created - " + this);
      }

   }

   public synchronized void shutdown(ShutdownCallback var1) {
      if (!this.internal && this.state != 3 && this.state != 2) {
         if (debugEnabled()) {
            debug("-- wmservice - " + this + " shutdown with callback " + var1 + "\nstack trace:\n" + StackTraceUtils.throwable2StackTrace((Throwable)null));
         }

         this.callback = var1;
         if (this.waitForPendingTransactions()) {
            if (debugEnabled()) {
               debug("-- wmservice - " + this + " is waiting for pending txn to complete before shutdown");
            }

            this.state = 2;
         } else if (!this.allowRMIWork) {
            if (debugEnabled()) {
               debug("-- wmservice - " + this + " has no pending txn. commencing shutdown ...");
            }

            this.state = 3;
            if (!this.workPending()) {
               if (debugEnabled()) {
                  debug("-- wmservice - " + this + " has no pending work and no pending txn. Invoking callback");
               }

               if (var1 != null) {
                  var1.completed();
               }

               var1 = null;
            }
         }
      } else {
         if (var1 != null) {
            var1.completed();
         }

      }
   }

   private boolean waitForPendingTransactions() {
      int var1 = ManagementService.getRuntimeAccess(kernelId).getServerRuntime().getStateVal();
      if (var1 == 2 && !ClientInitiatedTxShutdownService.isTxMapEmpty()) {
         if (debugEnabled()) {
            debug("-- wmservice - " + this + " has pending txn and will timeout in " + ClientInitiatedTxShutdownService.getTxTimeoutMillis() + "ms");
         }

         TimerManagerFactory.getTimerManagerFactory().getDefaultTimerManager().schedule(new TxEmptyChecker(ClientInitiatedTxShutdownService.getTxTimeoutMillis(), 2000), 0L, 2000L);
         return true;
      } else {
         return false;
      }
   }

   public WorkManager getDelegate() {
      return this.delegate;
   }

   public void cleanup() {
      if (this.delegate instanceof ServerWorkManagerImpl) {
         ((ServerWorkManagerImpl)this.delegate).cleanup();
      }

   }

   String getCancelMessage() {
      return WorkManagerLogger.logCancelBeforeEnqueueLoggable(this.getName(), this.getApplicationName()).getMessage();
   }

   public int getQueueDepth() {
      return this.workCount;
   }

   protected boolean permitSchedule(Runnable var1) {
      if (this.internal) {
         return true;
      } else {
         boolean var2 = false;
         synchronized(this) {
            if (this.state == 1 || !(var1 instanceof Work) || this.allowTransactionalWork(var1)) {
               ++this.workCount;
               var2 = true;
            }
         }

         if (this.allowRMIWork) {
            this.rmiManager.preScheduleWork();
            return true;
         } else if (var2) {
            return true;
         } else {
            if (debugEnabled()) {
               debug("-- wmservice - " + this + " is shutdown");
            }

            if (!this.isAdminUser(var1) && !this.isAdminChannelRequest(var1)) {
               return false;
            } else {
               synchronized(this) {
                  ++this.workCount;
                  return true;
               }
            }
         }
      }
   }

   private boolean allowTransactionalWork(Runnable var1) {
      if (this.state == 2 && var1 instanceof WorkAdapter) {
         boolean var2 = ((WorkAdapter)var1).isTransactional();
         if (debugEnabled()) {
            debug("-- wmservice - " + this + " is waiting for pending txn and current work has txn:" + var2);
         }

         return var2;
      } else {
         return false;
      }
   }

   private boolean isAdminChannelRequest(Runnable var1) {
      if (!(var1 instanceof WorkAdapter)) {
         return false;
      } else if (((WorkAdapter)var1).isAdminChannelRequest()) {
         if (debugEnabled()) {
            debug("-- wmservice - " + this + " is shutdown but accepted work from admin channel");
         }

         return true;
      } else {
         return false;
      }
   }

   private boolean isAdminUser(Runnable var1) {
      if (!(var1 instanceof ServerWorkAdapter)) {
         return false;
      } else {
         AuthenticatedSubject var2 = ((ServerWorkAdapter)var1).getAuthenticatedSubject();
         if (var2 != null && SubjectUtils.doesUserHaveAnyAdminRoles(var2)) {
            if (debugEnabled()) {
               debug("-- wmservice - " + this + " is shutdown but accepted work from " + var2);
            }

            return true;
         } else {
            return false;
         }
      }
   }

   public void workStuck() {
      if (!this.internal) {
         boolean var1;
         synchronized(this) {
            ++this.stuckThreadCount;
            if (this.state != 3) {
               return;
            }

            var1 = this.workPending();
         }

         if (!var1) {
            this.invokeCallback();
         }

      }
   }

   public void startRMIGracePeriod(WorkManagerService.WorkListener var1) {
      this.rmiManager = var1;
      if (var1 != null) {
         this.allowRMIWork = true;
      }

   }

   public void endRMIGracePeriod() {
      boolean var1 = false;
      synchronized(this) {
         this.allowRMIWork = false;
         if (this.state != 2) {
            this.state = 3;
         }

         var1 = this.workPending();
      }

      if (!var1) {
         this.invokeCallback();
      }

      this.rmiManager = null;
   }

   private void notifyTransactionCompletion() {
      if (this.callback != null) {
         boolean var1 = false;
         synchronized(this) {
            if (!this.allowRMIWork) {
               this.state = 3;
            }

            var1 = this.workPending();
         }

         if (!var1) {
            this.invokeCallback();
         }

      }
   }

   private final class TxEmptyChecker implements TimerListener {
      private final int totalExecutionCount;
      private int executionCount;

      TxEmptyChecker(int var2, int var3) {
         this.totalExecutionCount = var2 / var3;
      }

      public void timerExpired(Timer var1) {
         if (!ClientInitiatedTxShutdownService.isTxMapEmpty() && this.executionCount < this.totalExecutionCount) {
            if (WorkManagerLifecycleImpl.debugEnabled()) {
               WorkManagerLifecycleImpl.debug("-- wmservice - " + this + " timer expired: will wait for pending transactions");
            }

            ++this.executionCount;
         } else {
            if (WorkManagerLifecycleImpl.debugEnabled()) {
               WorkManagerLifecycleImpl.debug("-- wmservice - " + this + " timer expired: will no longer wait for pending transactions");
            }

            WorkManagerServiceImpl.this.notifyTransactionCompletion();
            var1.cancel();
         }

      }
   }
}
