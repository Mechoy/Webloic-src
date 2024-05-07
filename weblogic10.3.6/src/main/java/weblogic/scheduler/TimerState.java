package weblogic.scheduler;

import java.io.Serializable;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import javax.naming.NamingException;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import weblogic.cluster.ClusterLogger;
import weblogic.cluster.singleton.LeaseLostListener;
import weblogic.cluster.singleton.LeaseManager;
import weblogic.cluster.singleton.LeasingException;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityManager;
import weblogic.timers.NakedTimerListener;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManagerFactory;
import weblogic.transaction.TxHelper;
import weblogic.utils.Debug;
import weblogic.utils.StackTraceUtils;
import weblogic.work.WorkAdapter;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

public class TimerState implements NakedTimerListener, LeaseLostListener {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final boolean DEBUG = Debug.getCategory("weblogic.JobScheduler").isEnabled();
   private static final String TIMER_MANAGER = "weblogic.scheduler.TimerState";
   private final String id;
   private final TimerListener to;
   private final long duration;
   private final long interval;
   private final long timeout;
   private final AuthenticatedSubject user;
   private boolean timerLeaseLost = false;

   public TimerState(String var1, TimerListener var2, long var3, long var5, AuthenticatedSubject var7) {
      this.id = var1;
      this.to = var2;
      this.timeout = var3;
      this.duration = var3 - System.currentTimeMillis();
      this.interval = var5;
      if (var7 == null) {
         this.user = SubjectUtils.getAnonymousSubject();
      } else {
         this.user = var7;
      }

   }

   LeaseManager getLeasing() {
      try {
         return TimerMaster.getLeaseManager();
      } catch (NamingException var2) {
         var2.printStackTrace();
         return null;
      }
   }

   public String getId() {
      return this.id;
   }

   public TimerListener getTimedObject() {
      return this.to;
   }

   public long getDuration() {
      return this.duration;
   }

   public long getTimeout() {
      return this.timeout;
   }

   public long getInterval() {
      return this.interval;
   }

   public Serializable getInfo() {
      return null;
   }

   public AuthenticatedSubject getUser() {
      return this.user;
   }

   public void fireWhenReady() {
      LeaseManager var1 = this.getLeasing();

      assert var1 != null;

      try {
         if (!var1.tryAcquire(this.id)) {
            if (DEBUG) {
               debug("failed to claim ownership of timer " + this.id);
            }

            return;
         }

         if (DEBUG) {
            debug("claimed ownership of timer " + this.id);
         }
      } catch (LeasingException var3) {
         if (DEBUG) {
            debug("failed to claim ownership of timer " + this.id);
         }

         return;
      }

      var1.addLeaseLostListener(this);
      if (this.duration <= 0L) {
         if (DEBUG) {
            debug("timer will execute immediately " + this.id);
         }

         this.enqueueTimer(this);
      } else {
         if (DEBUG) {
            debug("execute timer after " + this.duration + "ms");
         }

         TimerManagerFactory.getTimerManagerFactory().getTimerManager("weblogic.scheduler.TimerState", "weblogic.kernel.System").schedule(this, this.duration);
      }

   }

   public void timerExpired(weblogic.timers.Timer var1) {
      this.enqueueTimer(this);
   }

   private void enqueueTimer(final TimerState var1) {
      WorkAdapter var2 = new WorkAdapter() {
         public String toString() {
            return "Execute job " + var1.getId() + " for Job Scheduler";
         }

         public Runnable cancel(String var1x) {
            if (TimerState.DEBUG) {
               TimerState.debug("timer execution is cancelled because of '" + var1x + "'");
            }

            return new Runnable() {
               public void run() {
               }
            };
         }

         public void run() {
            boolean var1x = false;
            boolean var2 = false;
            if (TimerState.this.isLeaseOwner()) {
               if (var1.getTimedObject() instanceof TransactionalTimerListener) {
                  var2 = true;

                  try {
                     TimerState.this.preInvoke();
                  } catch (SystemException var18) {
                     if (TimerState.DEBUG) {
                        TimerState.debug("tx begin failed with " + StackTraceUtils.throwable2StackTrace(var18));
                     }

                     return;
                  } catch (NotSupportedException var19) {
                     if (TimerState.DEBUG) {
                        TimerState.debug("tx begin failed with " + StackTraceUtils.throwable2StackTrace(var19));
                     }

                     return;
                  }
               }

               try {
                  try {
                     SecurityManager.runAs(TimerState.kernelId, var1.getUser(), new PrivilegedExceptionAction() {
                        public Object run() throws Exception {
                           var1.getTimedObject().timerExpired(new TimerImpl(var1.getId()));
                           return null;
                        }
                     });
                  } catch (PrivilegedActionException var17) {
                     throw new AssertionError(var17);
                  }

                  try {
                     TimerBasis var3 = TimerBasisAccess.getTimerBasis();
                     if (var1.getInterval() == -1L) {
                        var3.cancelTimer(var1.getId());
                     } else {
                        if (!TimerState.this.isLeaseOwner()) {
                           return;
                        }

                        var3.advanceIntervalTimer(var1.getId(), var1.getTimedObject());
                     }

                     var1x = true;
                     JobSchedulerRuntimeMBeanImpl.getInstance().timerExecuted(var1.getId(), var1.getTimedObject().toString(), var1.getInterval());
                  } catch (NoSuchObjectLocalException var20) {
                     throw new AssertionError(var20);
                  } catch (TimerException var21) {
                     throw new AssertionError(var21);
                  }
               } finally {
                  if (var2) {
                     if (var1x) {
                        TimerState.this.postInvokeSuccess();
                     } else {
                        TimerState.this.postInvokeFailure();
                     }
                  }

                  try {
                     var1.timerLeaseLost = false;
                     LeaseManager var6 = TimerState.this.getLeasing();

                     assert var6 != null;

                     var6.removeLeaseLostListener(var1);
                     var6.release(var1.getId());
                  } catch (LeasingException var16) {
                  }

               }

            }
         }
      };
      WorkManager var3 = WorkManagerFactory.getInstance().getSystem();
      if (this.to instanceof TimerListenerExtension) {
         TimerListenerExtension var4 = (TimerListenerExtension)this.to;
         if (var4.getDispatchPolicy() != null) {
            var3 = WorkManagerFactory.getInstance().find(var4.getDispatchPolicy(), var4.getApplicationName(), var4.getModuleName());
         }
      }

      var3.schedule(var2);
   }

   private boolean isLeaseOwner() {
      try {
         return this.isLeaseLost() ? false : this.getLeasing().tryAcquire(this.id);
      } catch (LeasingException var2) {
         if (DEBUG) {
            debug("failed to claim ownership of timer " + this.id);
         }

         return false;
      }
   }

   private static void debug(String var0) {
      ClusterLogger.logDebug("[TimerState] " + var0);
   }

   synchronized boolean isLeaseLost() {
      return this.timerLeaseLost;
   }

   public synchronized void onRelease() {
      LeaseManager var1 = this.getLeasing();

      assert var1 != null;

      var1.removeLeaseLostListener(this);
      this.timerLeaseLost = true;
   }

   public void preInvoke() throws SystemException, NotSupportedException {
      TxHelper.getTransactionManager().begin();
   }

   public void postInvokeSuccess() {
      try {
         TxHelper.getTransactionManager().commit();
      } catch (Exception var2) {
         if (DEBUG) {
            debug("tx commit failed with " + StackTraceUtils.throwable2StackTrace(var2));
         }
      }

   }

   public void postInvokeFailure() {
      try {
         TxHelper.getTransactionManager().rollback();
      } catch (SystemException var2) {
         if (DEBUG) {
            debug("tx rollback failed with " + StackTraceUtils.throwable2StackTrace(var2));
         }
      }

   }
}
