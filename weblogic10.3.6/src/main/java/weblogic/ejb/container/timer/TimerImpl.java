package weblogic.ejb.container.timer;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import javax.ejb.EJBException;
import javax.ejb.EnterpriseBean;
import javax.ejb.NoSuchEntityException;
import javax.ejb.NoSuchObjectLocalException;
import javax.ejb.TimedObject;
import javax.ejb.Timer;
import javax.ejb.TimerHandle;
import javax.transaction.xa.Xid;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.WLTimer;
import weblogic.ejb.WLTimerInfo;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.ejb.container.interfaces.DeploymentInfo;
import weblogic.ejb.container.interfaces.EntityBeanInfo;
import weblogic.ejb.container.interfaces.MessageDrivenBeanInfo;
import weblogic.ejb.container.interfaces.TimerIntf;
import weblogic.ejb.container.interfaces.WLEnterpriseBean;
import weblogic.ejb.container.internal.AllowedMethodsHelper;
import weblogic.ejb.container.internal.EJBContextHandler;
import weblogic.ejb.container.internal.InvocationWrapper;
import weblogic.ejb.container.internal.MethodDescriptor;
import weblogic.ejb.container.internal.RuntimeHelper;
import weblogic.ejb.container.internal.TimerDrivenLocalObject;
import weblogic.ejb.container.manager.BaseEntityManager;
import weblogic.ejb.container.monitoring.EJBTimerRuntimeMBeanImpl;
import weblogic.ejb.container.persistence.spi.CMPBean;
import weblogic.ejb20.timer.TimerHandleImpl;
import weblogic.logging.Loggable;
import weblogic.store.PersistentHandle;
import weblogic.store.PersistentStoreException;
import weblogic.store.gxa.GXAException;
import weblogic.timers.NakedTimerListener;
import weblogic.transaction.Transaction;
import weblogic.transaction.TxHelper;

public final class TimerImpl extends TimerDrivenLocalObject implements Timer, NakedTimerListener, WLTimer, TimerIntf {
   private static final DebugLogger debugLogger;
   public static final int READY_STATE = 1;
   public static final int EJB_TIMEOUT_STATE = 2;
   public static final int CANCEL_PENDING_STATE = 3;
   public static final int CREATE_PENDING_STATE = 4;
   public static final int TIMEOUT_PENDING_STATE = 5;
   public static final int TIMEOUT_CANCEL_STATE = 6;
   public static final int DOES_NOT_EXIST_STATE = 7;
   private EJBTimerManager timerManager;
   private BeanManager beanManager;
   private EJBTimerRuntimeMBeanImpl timerRtMBean;
   private weblogic.timers.Timer timer;
   private PersistentHandle handle;
   private boolean isTransactional;
   private int txTimeout;
   private int retryAttempt = 0;
   private Date retryExpiration;
   private int state;
   private boolean isMessageDrivenBean = false;
   private boolean isEntityBean = false;
   private boolean isCMPBean = false;
   private boolean entityRemovedFromTimeout = false;
   private Xid pendingTx;
   private TimerData data;
   private String lastThrowableText = null;
   private boolean consecutiveThrowable = false;

   public TimerImpl(EJBTimerManager var1, BeanManager var2, boolean var3, TimerData var4) {
      this.initialize(var1, var2, var3, var4);
   }

   public TimerImpl(EJBTimerManager var1, BeanManager var2, Object var3, Serializable var4, boolean var5, Date var6, long var7, Long var9, WLTimerInfo var10) {
      TimerData var11 = new TimerData();
      var11.setPk(var3);
      var11.setInfo(var4);
      var11.setNextExpiration(var6);
      var11.setIntervalDuration(var7);
      var11.setTimerId(var9);
      if (var10 != null) {
         var11.setMaxRetryAttempts(var10.getMaxRetryAttempts());
         var11.setRetryDelay(var10.getRetryDelay());
         var11.setFailureAction(var10.getTimeoutFailureAction());
         var11.setMaxTimeouts(var10.getMaxTimeouts());
      }

      this.initialize(var1, var2, var5, var11);
   }

   public void initialize(EJBTimerManager var1, BeanManager var2, boolean var3, TimerData var4) {
      BeanInfo var5 = var2.getBeanInfo();
      super.setBeanManager(var2);
      super.setBeanInfo(var5);
      this.timerManager = var1;
      this.beanManager = var2;
      this.isTransactional = var3;
      this.timerRtMBean = var1.getTimerRuntimeMBean();
      this.txTimeout = var5.getTransactionTimeoutMS();
      if (var5 instanceof EntityBeanInfo) {
         this.isEntityBean = true;
         if (!((EntityBeanInfo)var5).getIsBeanManagedPersistence()) {
            this.isCMPBean = true;
         }
      } else if (var5 instanceof MessageDrivenBeanInfo) {
         this.isMessageDrivenBean = true;
      }

      this.data = var4;
      if (debugLogger.isDebugEnabled()) {
         debug("Initialized EJB timer: " + this);
      }

   }

   public void setTimer(weblogic.timers.Timer var1) {
      this.timer = var1;
   }

   public weblogic.timers.Timer getTimer() {
      return this.timer;
   }

   public void setPersistentHandle(PersistentHandle var1) {
      this.handle = var1;
   }

   public PersistentHandle getPersistentHandle() {
      return this.handle;
   }

   public Object getPK() {
      return this.data.getPk();
   }

   public Long getID() {
      return this.data.getTimerId();
   }

   public Date getNextExpiration() {
      return this.retryExpiration != null ? this.retryExpiration : this.data.getNextExpiration();
   }

   public boolean isIntervalTimer() {
      return this.data.getIntervalDuration() != -1L;
   }

   public synchronized void timerExpired(weblogic.timers.Timer var1) {
      if (debugLogger.isDebugEnabled()) {
         debug("ejbTimeout for Timer: " + this);
      }

      if (!this.ensureReadyState()) {
         if (debugLogger.isDebugEnabled()) {
            debug("Unable to get ready states for Timer: " + this);
         }

         if (!this.isCancelled()) {
            this.handleTimeoutFailure((TimerData)null);
         }
      } else {
         MethodDescriptor var2 = this.timerManager.getMethodDescriptor();
         boolean var3 = true;
         boolean var4 = false;

         try {
            InvocationWrapper var5;
            try {
               var5 = this.preInvoke(this.data.getPk(), var2, new EJBContextHandler(var2, new Object[]{var1}));
            } catch (Throwable var21) {
               if (var21 instanceof EJBException) {
                  Exception var7 = ((EJBException)var21).getCausedByException();
                  if (var7 instanceof NoSuchEntityException) {
                     this.handleNoSuchEntity(false);
                     return;
                  }
               }

               if (this.shouldLogThrowable(var21)) {
                  EJBLogger.logExceptionBeforeInvokingEJBTimeout(this.beanInfo.getDisplayName(), var21);
               }

               var1.cancel();
               this.handleTimeoutFailure((TimerData)null);
               return;
            }

            var5.skipLoggingException();
            EnterpriseBean var6 = var5.getBean();
            WLEnterpriseBean var25 = null;
            if (!this.isMessageDrivenBean) {
               var25 = (WLEnterpriseBean)var6;
            }

            int var8 = 0;
            if (var25 != null) {
               var8 = var25.__WL_getMethodState();
            }

            Object var9 = null;

            try {
               if (var25 != null) {
                  var25.__WL_setMethodState(65536);
               }

               this.setState(2);
               var4 = this.timerManager.registerTimerExpirationOperation(this);

               assert var4 == this.isTransactional;

               ((TimedObject)var6).ejbTimeout(this);
               if (!this.isTransactional) {
                  this.timerRtMBean.incrementTimeoutCount();
               }

               this.data.incrementSuccessfulTimeouts();
               if (this.data.getSuccessfulTimeouts() == this.data.getMaxTimeouts()) {
                  this.cancel();
               }

               var3 = false;
               if (!this.isCancelled()) {
                  if (this.isIntervalTimer()) {
                     this.data.setNextExpiration(new Date(this.data.getNextExpiration().getTime() + this.data.getIntervalDuration()));
                     this.accountForSkippedIntervals();
                     if (!this.isTransactional) {
                        this.timerManager.updatePersistentStoreEntry(this);
                     }
                  } else if (!this.isTransactional) {
                     this.doCancel();
                     this.timerManager.removePersistentStoreEntry(this);
                  }
               }
            } catch (Throwable var22) {
               Throwable var10 = var22;
               if (var22 instanceof InvocationTargetException) {
                  var10 = var22.getCause();
               }

               if (debugLogger.isDebugEnabled()) {
                  debug("ejbTimeout failed due to Exception for Timer: " + this, var10);
               }

               var9 = var10;
            } finally {
               if (var25 != null) {
                  var25.__WL_setMethodState(var8);
               }

               if (!this.isCancelled()) {
                  if (this.isTransactional) {
                     this.setState(5);
                  } else {
                     this.setState(1);
                  }
               }

            }

            if (this.isEntityBean && !this.entityRemovedFromTimeout) {
               try {
                  if (this.isCMPBean) {
                     ((CMPBean)var6).__WL_doCheckExistsOnMethod();
                  } else if (var9 != null) {
                     ((BaseEntityManager)this.beanManager).ensureDBExistence(this.data.getPk());
                  }
               } catch (NoSuchEntityException var19) {
                  this.handleNoSuchEntity(true);
                  var9 = var19;
               } catch (Throwable var20) {
                  var9 = var20;
               }
            }

            this.entityRemovedFromTimeout = false;
            this.postInvoke(var5, (Throwable)var9);

            assert var9 == null;
         } catch (Throwable var24) {
            if (this.shouldLogThrowable(var24)) {
               EJBLogger.logExceptionInvokingEJBTimeout(this.beanInfo.getDisplayName(), var24);
            }
         }

         if (!this.isTransactional && !this.isCancelled()) {
            if (var3) {
               this.handleTimeoutFailure((TimerData)null);
            } else if (this.isIntervalTimer()) {
               this.handleTimeoutSuccess();
            }
         }

      }
   }

   private void handleNoSuchEntity(boolean var1) {
      this.doCancel();
      if (!this.isTransactional || !var1) {
         try {
            this.timerManager.removePersistentStoreEntry(this);
         } catch (PersistentStoreException var3) {
            EJBLogger.logErrorRemovingTimer(this.beanInfo.getDisplayName(), var3);
         }
      }

   }

   public void accountForSkippedIntervals() {
      assert this.isIntervalTimer();

      long var1 = this.data.getNextExpiration().getTime();
      long var3 = System.currentTimeMillis();
      if (var1 < var3) {
         long var5 = (var3 - var1) / this.data.getIntervalDuration();
         var1 += var5 * this.data.getIntervalDuration();
         this.data.setNextExpiration(new Date(var1));
      }

   }

   public synchronized void handleTimeoutSuccess() {
      assert this.isIntervalTimer();

      if (!this.isCancelled()) {
         this.setState(1);
         this.retryAttempt = 0;
         this.resetErrorLogging();
         if (this.retryExpiration != null) {
            if (this.timer != null) {
               this.timer.cancel();
            }

            this.retryExpiration = null;
            this.timer = null;
         }

         if (this.timer == null) {
            this.timerManager.scheduleTimer(this);
         }

      }
   }

   public synchronized void handleTimeoutFailure(TimerData var1) {
      if (debugLogger.isDebugEnabled()) {
         debug("Executing handleTimeoutFailure for Timer: " + this);
      }

      if (this.exists()) {
         if (var1 != null) {
            this.data.setNextExpiration(var1.getNextExpiration());
            this.data.setInfo(var1.getInfo());
         }

         if (this.isCancelled()) {
            this.timer = null;
         }

         this.setState(1);
         int var2 = this.data.getMaxRetryAttempts();
         if (this.retryAttempt >= var2 && var2 != -1) {
            this.retryAttempt = 0;
            this.retryExpiration = null;
            switch (this.data.getFailureAction()) {
               case 1:
                  this.doCancel();

                  try {
                     this.timerManager.removePersistentStoreEntry(this);
                  } catch (PersistentStoreException var6) {
                     EJBLogger.logErrorRemovingTimer(this.beanInfo.getDisplayName(), var6);
                  }
                  break;
               case 2:
                  this.timerManager.disableTimer(this);
                  break;
               case 3:
                  this.data.setNextExpiration(new Date(this.data.getNextExpiration().getTime() + this.data.getIntervalDuration()));
                  this.accountForSkippedIntervals();
                  this.handleTimeoutSuccess();
                  break;
               default:
                  throw new AssertionError("Unknown action");
            }
         } else {
            int var3;
            if (this.data.getRetryDelay() > 0L) {
               this.retryExpiration = new Date(System.currentTimeMillis() + this.data.getRetryDelay());
               var3 = this.retryAttempt + 1;
               EJBLogger.logConfiguredEJBTimeoutDelayApplied(this.getDisplayString(), var3, this.data.getRetryDelay());
            } else {
               var3 = this.retryAttempt + 1;
               if (var3 >= 10) {
                  int var4 = var3 - 10;
                  boolean var5 = false;
                  int var7;
                  if (var4 > 3) {
                     var7 = 60;
                  } else {
                     var7 = 5 * (int)StrictMath.pow(2.0, (double)var4);
                  }

                  EJBLogger.logEJBTimeoutDelayAutomaticallyApplied(this.getDisplayString(), var3, var7);
                  this.retryExpiration = new Date(System.currentTimeMillis() + (long)(var7 * 1000));
               }
            }

            ++this.retryAttempt;
            this.timerManager.scheduleTimer(this);
         }

      }
   }

   public void remove() {
      if (this.state == 2 || this.state == 6) {
         this.entityRemovedFromTimeout = true;
      }

      if (!this.isCancelled()) {
         this.cancel();
      }

   }

   public synchronized void cancel() {
      if (debugLogger.isDebugEnabled()) {
         debug("cancel called for Timer: " + this);
      }

      this.checkAllowedInvoke();
      if (this.state == 2 && !this.isIntervalTimer()) {
         Loggable var1 = EJBLogger.logSingleExpirationTimerCannotBeCancelledLoggable();
         throw new IllegalStateException(var1.getMessage());
      } else {
         if (this.state == 2 && this.isTransactional) {
            this.setState(6);
         } else {
            Loggable var2;
            EJBException var3;
            try {
               if (this.timerManager.registerTimerCancellationOperation(this)) {
                  if (this.state == 2) {
                     this.setState(6);
                  } else {
                     this.setState(3);
                  }
               } else {
                  this.timerManager.removePersistentStoreEntry(this);
                  this.setState(7);
                  this.timerManager.removeTimerFromMaps(this);
                  this.timerRtMBean.incrementCancelledTimerCount();
               }
            } catch (GXAException var4) {
               if (debugLogger.isDebugEnabled()) {
                  debug("Error cancelling timer", var4);
               }

               var2 = EJBLogger.logErrorCacelTimerLoggable();
               var3 = new EJBException(var2.getMessage(), var4);
               var3.initCause(var4);
               throw var3;
            } catch (PersistentStoreException var5) {
               if (debugLogger.isDebugEnabled()) {
                  debug("Error cancelling timer", var5);
               }

               var2 = EJBLogger.logErrorCacelTimerLoggable();
               var3 = new EJBException(var2.getMessage(), var5);
               var3.initCause(var5);
               throw var3;
            }
         }

         this.timerManager.cancelTimer(this);
      }
   }

   public synchronized TimerHandle getHandle() {
      this.checkAllowedInvoke();
      BeanInfo var1 = this.beanManager.getBeanInfo();
      DeploymentInfo var2 = var1.getDeploymentInfo();
      return new TimerHandleImpl(this, this.data.getTimerId(), var2.getModuleURI(), var1.getEJBName());
   }

   public synchronized Serializable getInfo() {
      this.checkAllowedInvoke();
      return this.data.getInfo();
   }

   public synchronized Date getNextTimeout() {
      this.checkAllowedInvoke();
      return this.getNextExpiration();
   }

   public synchronized long getTimeRemaining() {
      this.checkAllowedInvoke();
      return this.getNextExpiration().getTime() - System.currentTimeMillis();
   }

   public int getRetryAttemptCount() {
      return this.retryAttempt;
   }

   public int getMaximumRetryAttempts() {
      return this.data.getMaxRetryAttempts();
   }

   public int getCompletedTimeoutCount() {
      return this.data.getSuccessfulTimeouts();
   }

   public TimerData getTimerData() {
      return this.data;
   }

   public String toString() {
      BeanInfo var1 = this.beanManager.getBeanInfo();
      return "[EJB Timer] id: " + this.data.getTimerId() + " pk: " + this.data.getPk() + " info: " + this.data.getInfo() + " timer: " + this.timer + " state: " + this.state + " ejb: " + var1.getDisplayName() + " Thread: " + Thread.currentThread();
   }

   private String getDisplayString() {
      BeanInfo var1 = this.beanManager.getBeanInfo();
      return "(timer id: " + this.data.getTimerId() + ", info: " + this.data.getInfo() + ", ejb: " + var1.getDisplayName() + ")";
   }

   private void checkAllowedInvoke() {
      WLEnterpriseBean var1 = AllowedMethodsHelper.getBean();
      int var2 = RuntimeHelper.getCurrentState(var1);
      Loggable var3;
      if (var2 == 4) {
         var3 = EJBLogger.logCannotInvokeTimerObjectsFromEjbCreateLoggable();
         throw new IllegalStateException(var3.getMessage());
      } else if (var2 == 16 && !(this.beanManager instanceof BaseEntityManager)) {
         var3 = EJBLogger.logIllegalInvokeTimerMethodInEJbRemoveOrPreDestroyLoggable();
         throw new IllegalStateException(var3.getMessage());
      } else if (var2 == 32) {
         var3 = EJBLogger.logIllegalInvokeTimerMethodInEJbRAvitvateOrPostActivateLoggable();
         throw new IllegalStateException(var3.getMessage());
      } else if (var2 == 64) {
         var3 = EJBLogger.logIllegalInvokeTimerMethodInEjbPassivateOrPrePassivateLoggable();
         throw new IllegalStateException(var3.getMessage());
      } else if (var2 == 1024) {
         var3 = EJBLogger.logCannotInvokeTimerObjectsFromAfterCompletionLoggable();
         throw new IllegalStateException(var3.getMessage());
      } else if (var2 == 1) {
         var3 = EJBLogger.logIllegalInvokeTimerMethodDuringDILoggable();
         throw new IllegalStateException(var3.getMessage());
      } else if (!this.ensureReadyState()) {
         if (this.isCancelled()) {
            var3 = EJBLogger.logIllegalAttemptToUseCancelledTimerLoggable();
            throw new NoSuchObjectLocalException(var3.getMessage());
         } else {
            var3 = EJBLogger.logInvovationTimeoutLoggable();
            throw new EJBException(var3.getMessage());
         }
      }
   }

   private boolean ensureReadyState() {
      if (this.pendingTx == null) {
         return !this.isCancelled();
      } else {
         Transaction var1 = TxHelper.getTransaction();
         if (var1 != null && this.pendingTx.equals(var1.getXID())) {
            return !this.isCancelled();
         } else {
            long var2 = System.currentTimeMillis() + (long)this.txTimeout;

            for(boolean var4 = false; this.pendingTx != null && !var4; var4 = System.currentTimeMillis() > var2) {
               try {
                  if (debugLogger.isDebugEnabled()) {
                     debug("****Waiting.  State: " + this.state);
                  }

                  this.wait((long)this.txTimeout);
               } catch (InterruptedException var6) {
               }

               if (debugLogger.isDebugEnabled()) {
                  debug("****Done Waiting.  State: " + this.state);
               }
            }

            return !this.isCancelled() && this.pendingTx == null;
         }
      }
   }

   public synchronized void finalizeCancel() {
      this.setState(7);
      this.timerManager.removeTimerFromMaps(this);
   }

   public synchronized void undoCancel() {
      if (this.state != 7) {
         if (this.state == 6) {
            this.setState(2);
            this.timer = null;
         } else {
            assert this.state == 3;

            this.setState(1);
            this.timerManager.scheduleTimer(this);
         }

      }
   }

   public synchronized void finalizeCreate() {
      if (!this.isCancelled()) {
         this.setState(1);
         this.timerManager.scheduleTimer(this);
      }

   }

   public synchronized void undoCreate() {
      this.setState(7);
      this.timerManager.cancelTimer(this);
      this.timerManager.removeTimerFromMaps(this);
   }

   private void doCancel() {
      this.setState(7);
      this.timerManager.cancelTimer(this);
      this.timerManager.removeTimerFromMaps(this);
   }

   public boolean isCancelled() {
      return this.state == 7 || this.state == 3 || this.state == 6;
   }

   public boolean exists() {
      return this.state != 7;
   }

   public void setState(int var1) {
      if (this.state == 4 || this.state == 3 || this.state == 5 || this.state == 6) {
         this.pendingTx = null;
         this.notifyAll();
      }

      this.state = var1;
   }

   public int getState() {
      return this.state;
   }

   public void setXid(Xid var1) {
      this.pendingTx = var1;
   }

   private boolean shouldLogThrowable(Throwable var1) {
      Throwable var2 = var1.getCause();
      if (var2 != null) {
         var1 = var2;
      }

      String var3 = var1.toString();
      if (var3 == null) {
         var3 = var1.getClass().getName();
      }

      if (var3.equals(this.lastThrowableText)) {
         if (!this.consecutiveThrowable) {
            EJBLogger.logSuppressingEJBTimeoutErrors(this.getDisplayString());
            this.consecutiveThrowable = true;
         }

         return false;
      } else {
         this.lastThrowableText = var3;
         this.consecutiveThrowable = false;
         return true;
      }
   }

   private void resetErrorLogging() {
      this.lastThrowableText = null;
      this.consecutiveThrowable = false;
   }

   private static void debug(String var0) {
      debugLogger.debug("[TimerImpl] " + var0);
   }

   private static void debug(String var0, Throwable var1) {
      debugLogger.debug("[TimerImpl] " + var0, var1);
   }

   static {
      debugLogger = EJBDebugService.timerLogger;
   }
}
