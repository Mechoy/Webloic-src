package weblogic.timers.internal;

import java.security.AccessController;
import weblogic.management.ManagementException;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.Timer;
import weblogic.management.runtime.TimerRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public final class TimerRuntimeMBeanImpl extends RuntimeMBeanDelegate implements TimerRuntimeMBean {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   TimerRuntimeMBeanImpl() throws ManagementException {
      super("TimerRuntime");
      ManagementService.getRuntimeAccess(kernelId).getServerRuntime().setTimerRuntime(this);
   }

   public Timer[] getTimers() {
      TimerImpl[] var1 = TimerImpl.getTimers();
      if (var1 != null && var1.length != 0) {
         Timer[] var2 = new Timer[var1.length];

         for(int var3 = 0; var3 < var1.length; ++var3) {
            var2[var3] = new TimerRuntime(var1[var3]);
         }

         return var2;
      } else {
         return null;
      }
   }

   public static final class TimerRuntime implements Timer {
      static final long serialVersionUID = -1639099960837088234L;
      private final String timerManagerName;
      private final long timeout;
      private final long period;
      private final boolean stopped;
      private final boolean cancelled;
      private final long[] pastExpirationTimes;

      public TimerRuntime(TimerImpl var1) {
         this.timerManagerName = var1.getTimerManager().getName() + "[" + var1.toString() + "]";
         this.timeout = var1.getTimeout();
         this.period = Math.abs(var1.getPeriod());
         this.stopped = var1.isStopped();
         this.cancelled = var1.isCancelled();
         this.pastExpirationTimes = var1.sortedExpirationTimes();
      }

      public String getTimerManagerName() {
         return this.timerManagerName;
      }

      public long getTimeout() {
         return this.timeout;
      }

      public long getPeriod() {
         return this.period;
      }

      public boolean isStopped() {
         return this.stopped;
      }

      public boolean isCancelled() {
         return this.cancelled;
      }

      public long getExpirationCount() {
         return (long)this.pastExpirationTimes.length;
      }

      public long[] getPastExpirationTimes() {
         return this.pastExpirationTimes;
      }
   }
}
