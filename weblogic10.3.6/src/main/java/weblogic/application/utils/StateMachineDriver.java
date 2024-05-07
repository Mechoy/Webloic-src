package weblogic.application.utils;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.utils.ErrorCollectionException;
import weblogic.work.ContextWrap;
import weblogic.work.WorkManagerFactory;

public final class StateMachineDriver {
   private static final AuthenticatedSubject kernelID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final boolean DEBUG_TIMINGS = Boolean.getBoolean("weblogic.DEBUG_TIMINGS");
   private static final boolean USE_WORKMANAGER = !Boolean.getBoolean("weblogic.application.prepare.USE_JAVA_THREADS");

   public void nextState(StateChange var1, Object[] var2) throws StateChangeException {
      long var3 = System.nanoTime();
      long var5 = var3;

      for(int var7 = 0; var7 < var2.length; ++var7) {
         try {
            long var8 = System.nanoTime();
            var1.next(var2[var7]);
            if (DEBUG_TIMINGS) {
               var5 = System.nanoTime();
               System.out.println("JEEINST:application.StateMachine.nextState:" + (var5 - var8) + ":" + var1 + ":" + var2[var7]);
            }
         } catch (Throwable var11) {
            try {
               this.previousState(var1, var2, var7);
            } catch (StateChangeException var10) {
               var1.logRollbackError(var10);
            }

            throw new StateChangeException(var11);
         }
      }

      if (DEBUG_TIMINGS) {
         System.out.println("JEEINST:application.StateMachine.nextState(elapsed):" + (var5 - var3) + ":" + var1);
      }

   }

   public void nextStateInParallel(StateChange var1, Object[] var2) throws StateChangeException {
      long var3 = System.nanoTime();
      if (var2 != null && var2.length != 0) {
         CountDownLatch var5 = new CountDownLatch(var2.length);
         ParallelChange[] var6 = new ParallelChange[var2.length];
         AuthenticatedSubject var7 = null;
         if (!USE_WORKMANAGER) {
            var7 = SecurityServiceManager.getCurrentSubject(kernelID);
         }

         for(int var8 = 0; var8 < var2.length; ++var8) {
            var6[var8] = new ParallelChange(var2[var8], var1, var7, var5);
            if (USE_WORKMANAGER) {
               ContextWrap var9 = new ContextWrap(var6[var8]);
               WorkManagerFactory.getInstance().getSystem().schedule(var9);
            } else {
               Thread var18 = new Thread(var6[var8]);
               var18.start();
            }
         }

         try {
            var5.await();
         } catch (InterruptedException var16) {
            throw new StateChangeException(var16);
         }

         ArrayList var17 = new ArrayList();
         ParallelChange[] var19 = var6;
         int var10 = var6.length;

         int var11;
         ParallelChange var12;
         for(var11 = 0; var11 < var10; ++var11) {
            var12 = var19[var11];
            if (var12.result != null) {
               var17.add(var12.result);
            }
         }

         if (DEBUG_TIMINGS) {
            long var20 = System.nanoTime();
            System.out.println("JEEINST:application.StateMachine.nextStateInParallel(elapsed):" + (var20 - var3) + ":" + var1);
         }

         if (!var17.isEmpty()) {
            var19 = var6;
            var10 = var6.length;

            for(var11 = 0; var11 < var10; ++var11) {
               var12 = var19[var11];
               if (var12.result == null) {
                  try {
                     var1.previous(var12.target);
                  } catch (StateChangeException var14) {
                     var17.add(var14);
                  } catch (Exception var15) {
                     var17.add(var15);
                  }
               }
            }

            ErrorCollectionException var21 = new ErrorCollectionException();
            Iterator var22 = var17.iterator();

            while(var22.hasNext()) {
               Throwable var23 = (Throwable)var22.next();
               var21.add(var23);
            }

            throw new StateChangeException(var21);
         }
      }
   }

   public void previousState(StateChange var1, Object[] var2) throws StateChangeException {
      this.previousState(var1, var2, var2.length);
   }

   private void previousState(StateChange var1, Object[] var2, int var3) throws StateChangeException {
      ErrorCollectionException var4 = null;

      for(int var5 = var3 - 1; var5 >= 0; --var5) {
         try {
            var1.previous(var2[var5]);
         } catch (Throwable var7) {
            if (var4 == null) {
               var4 = new ErrorCollectionException();
            }

            var4.addError(var7);
         }
      }

      if (var4 != null) {
         throw new StateChangeException(var4);
      }
   }

   static class ParallelChange implements Runnable {
      final Object target;
      final StateChange change;
      final AuthenticatedSubject subject;
      CountDownLatch latch;
      Throwable result;

      ParallelChange(Object var1, StateChange var2, AuthenticatedSubject var3, CountDownLatch var4) {
         this.target = var1;
         this.change = var2;
         this.subject = var3;
         this.latch = var4;
      }

      public Object doWork() {
         try {
            long var1 = System.nanoTime();
            this.change.next(this.target);
            if (StateMachineDriver.DEBUG_TIMINGS) {
               long var3 = System.nanoTime();
               System.out.println("JEEINST:application.StateMachine.nextState(ParallelChange):" + (var3 - var1) + ":" + this.change + ":" + this.target);
               var1 = System.nanoTime();
            }
         } catch (Throwable var9) {
            this.result = var9;
         } finally {
            this.latch.countDown();
         }

         return null;
      }

      public void run() {
         if (StateMachineDriver.USE_WORKMANAGER) {
            this.doWork();
         } else {
            SecurityServiceManager.runAs(StateMachineDriver.kernelID, this.subject, new PrivilegedAction() {
               public Object run() {
                  return ParallelChange.this.doWork();
               }
            });
         }

      }
   }
}
