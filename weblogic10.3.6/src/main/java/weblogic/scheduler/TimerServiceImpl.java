package weblogic.scheduler;

import java.security.AccessController;
import java.util.Date;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManager;

public class TimerServiceImpl implements TimerManager {
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   static final long ILLEGAL_INTERVAL = -1L;
   private TimerBasis basis;
   private String name;

   public static TimerManager create(String var0) {
      return new TimerServiceImpl(var0);
   }

   private TimerServiceImpl(String var1) {
      this.name = var1;
   }

   public weblogic.timers.Timer schedule(TimerListener var1, long var2) {
      if (var2 < 0L) {
         throw new IllegalArgumentException("Duration is negative");
      } else {
         try {
            return this.createTimerInternal(var1, var2, -1L);
         } catch (TimerException var5) {
            throw new IllegalStateException(var5);
         }
      }
   }

   public weblogic.timers.Timer schedule(TimerListener var1, long var2, long var4) {
      if (var2 < 0L) {
         throw new IllegalArgumentException("Initial duration is negative");
      } else if (var4 < 0L) {
         throw new IllegalArgumentException("Interval duration is negative");
      } else {
         try {
            return this.createTimerInternal(var1, var2, var4);
         } catch (TimerException var7) {
            throw new IllegalStateException(var7);
         }
      }
   }

   public weblogic.timers.Timer schedule(TimerListener var1, Date var2) throws IllegalArgumentException {
      if (var2 == null) {
         throw new IllegalArgumentException("Expiration is null");
      } else {
         return this.schedule(var1, var2.getTime() - System.currentTimeMillis());
      }
   }

   public weblogic.timers.Timer schedule(TimerListener var1, Date var2, long var3) throws IllegalArgumentException {
      if (var2 == null) {
         throw new IllegalArgumentException("Expiration is null");
      } else {
         return this.schedule(var1, var2.getTime() - System.currentTimeMillis(), var3);
      }
   }

   public weblogic.timers.Timer scheduleAtFixedRate(TimerListener var1, Date var2, long var3) {
      throw new UnsupportedOperationException("Job scheduler does not support scheduleAtFixedRate(). It only supports schedule()");
   }

   public weblogic.timers.Timer scheduleAtFixedRate(TimerListener var1, long var2, long var4) {
      throw new UnsupportedOperationException("Job scheduler does not support scheduleAtFixedRate(). It only supports schedule()");
   }

   public void resume() {
      throw new IllegalStateException("job scheduler is already running!");
   }

   public void suspend() {
      throw new UnsupportedOperationException("job scheduler cannot be suspended!");
   }

   public void stop() {
      throw new UnsupportedOperationException("job scheduler cannot be stopped!");
   }

   public boolean waitForStop(long var1) throws InterruptedException {
      throw new UnsupportedOperationException("job scheduler cannot be stopped!");
   }

   public boolean isStopping() {
      return false;
   }

   public boolean isStopped() {
      return false;
   }

   public boolean waitForSuspend(long var1) throws InterruptedException {
      throw new UnsupportedOperationException("job scheduler cannot be suspended!");
   }

   public boolean isSuspending() {
      return false;
   }

   public boolean isSuspended() {
      return false;
   }

   private weblogic.timers.Timer createTimerInternal(TimerListener var1, long var2, long var4) throws TimerException {
      AuthenticatedSubject var6 = SecurityServiceManager.getCurrentSubject(KERNEL_ID);
      TimerBasis var7 = TimerBasisAccess.getTimerBasis();
      String var8 = var7.createTimer(this.name, var1, var2, var4, var6);
      return new TimerImpl(var8);
   }
}
