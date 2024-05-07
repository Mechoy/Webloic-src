package weblogic.ejb.container.timer;

import java.io.Serializable;
import java.util.Date;
import javax.ejb.NoSuchObjectLocalException;
import javax.ejb.Timer;
import javax.ejb.TimerHandle;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.interfaces.TimerIntf;
import weblogic.ejb.container.interfaces.WLEnterpriseBean;
import weblogic.ejb.container.internal.AllowedMethodsHelper;
import weblogic.logging.Loggable;

public final class TimerWrapper implements Timer {
   private TimerIntf delegate;

   public TimerWrapper(TimerIntf var1) {
      this.delegate = var1;
   }

   public void cancel() {
      this.checkTimerState();
      this.delegate.cancel();
   }

   public TimerHandle getHandle() {
      this.checkTimerState();
      return this.delegate.getHandle();
   }

   public Serializable getInfo() {
      this.checkTimerState();
      return this.delegate.getInfo();
   }

   public Date getNextTimeout() {
      this.checkTimerState();
      return this.delegate.getNextTimeout();
   }

   public long getTimeRemaining() {
      this.checkTimerState();
      return this.delegate.getTimeRemaining();
   }

   private void checkTimerState() {
      if (!this.delegate.exists()) {
         throw new NoSuchObjectLocalException("Timer has expired or has been cancelled");
      } else {
         WLEnterpriseBean var1 = AllowedMethodsHelper.getBean();
         if (var1 != null) {
            int var2 = var1.__WL_getMethodState();
            Loggable var3;
            if (var2 == 4) {
               var3 = EJBLogger.logCannotInvokeTimerObjectsFromEjbCreateLoggable();
               throw new IllegalStateException(var3.getMessage());
            }

            if (var2 == 1024) {
               var3 = EJBLogger.logCannotInvokeTimerObjectsFromAfterCompletionLoggable();
               throw new IllegalStateException(var3.getMessage());
            }
         }

      }
   }
}
