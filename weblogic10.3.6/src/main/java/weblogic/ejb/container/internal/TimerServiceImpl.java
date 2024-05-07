package weblogic.ejb.container.internal;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import weblogic.ejb.WLTimerInfo;
import weblogic.ejb.WLTimerService;
import weblogic.ejb.container.interfaces.TimerManager;
import weblogic.utils.Debug;

public final class TimerServiceImpl implements TimerService, WLTimerService {
   private final TimerManager timerManager;
   private final BaseEJBContext ejbCtx;
   private final boolean isClustered;

   public TimerServiceImpl(TimerManager var1, BaseEJBContext var2, boolean var3) {
      Debug.assertion(var1 != null);
      this.timerManager = var1;
      this.ejbCtx = var2;
      this.isClustered = var3;
   }

   public Timer createTimer(Date var1, long var2, Serializable var4) {
      return this.createTimer(var1, var2, var4, (WLTimerInfo)null);
   }

   public Timer createTimer(Date var1, Serializable var2) {
      return this.createTimer(var1, var2, (WLTimerInfo)null);
   }

   public Timer createTimer(long var1, long var3, Serializable var5) {
      return this.createTimer(var1, var3, var5, (WLTimerInfo)null);
   }

   public Timer createTimer(long var1, Serializable var3) {
      return this.createTimer(var1, var3, (WLTimerInfo)null);
   }

   public Timer createTimer(Date var1, long var2, Serializable var4, WLTimerInfo var5) {
      this.ejbCtx.checkAllowedToUseTimerService();
      this.ensureWLTimerServiceSupport(var5);
      if (var2 <= 0L) {
         throw new IllegalArgumentException("The intervalDuration argument must be positive.  The value specified was: " + var2);
      } else if (var1 == null) {
         throw new IllegalArgumentException("The initialExpiration argument cannot be null.");
      } else if (var1.getTime() < 0L) {
         throw new IllegalArgumentException("The value of initialExpiration.getTime() cannot be negative.  The value specified was: " + var1.getTime());
      } else {
         return this.timerManager.createTimer(this.getPK(), var1, var2, var4, var5);
      }
   }

   public Timer createTimer(Date var1, Serializable var2, WLTimerInfo var3) {
      this.ejbCtx.checkAllowedToUseTimerService();
      this.ensureWLTimerServiceSupport(var3);
      if (var1 == null) {
         throw new IllegalArgumentException("The expiration argument cannot be null.");
      } else if (var1.getTime() < 0L) {
         throw new IllegalArgumentException("The value of initialExpiration.getTime() cannot be negative.  The value specified was: " + var1.getTime());
      } else {
         if (var3 != null) {
            if (var3.getMaxTimeouts() > 0) {
               throw new IllegalArgumentException("The maxTimeouts property on the WLTimerInfo object can only be set for interval timers.  You are attempting to create a single-event timer.");
            }

            if (var3.getTimeoutFailureAction() == 3) {
               throw new IllegalArgumentException("The ejbTimeout failure action, 'SKIP_TIMEOUT_ACTION', can only be set for interval timers.  You are attempting to create a single-event timer.  Please reconfigure the ejbTimeout failure action on your WLTimerInfo object.");
            }
         }

         return this.timerManager.createTimer(this.getPK(), var1, var2, var3);
      }
   }

   public Timer createTimer(long var1, long var3, Serializable var5, WLTimerInfo var6) {
      this.ejbCtx.checkAllowedToUseTimerService();
      this.ensureWLTimerServiceSupport(var6);
      if (var1 < 0L) {
         throw new IllegalArgumentException("The initialDuration argument must be positive.  The value specified was: " + var1);
      } else if (var3 <= 0L) {
         throw new IllegalArgumentException("The intervalDuration argument must be positive.  The value specified was: " + var3);
      } else {
         return this.timerManager.createTimer(this.getPK(), var1, var3, var5, var6);
      }
   }

   public Timer createTimer(long var1, Serializable var3, WLTimerInfo var4) {
      this.ejbCtx.checkAllowedToUseTimerService();
      this.ensureWLTimerServiceSupport(var4);
      if (var1 < 0L) {
         throw new IllegalArgumentException("The duration argument must be positive.  The value specified was: " + var1);
      } else {
         if (var4 != null) {
            if (var4.getMaxTimeouts() > 0) {
               throw new IllegalArgumentException("The maxTimeouts property on the WLTimerInfo object can only be set for interval timers.  You are attempting to create a single-event timer.");
            }

            if (var4.getTimeoutFailureAction() == 3) {
               throw new IllegalArgumentException("The ejbTimeout failure action, 'SKIP_TIMEOUT_ACTION', can only be set for interval timers.  You are attempting to create a single-event timer.  Please reconfigure the ejbTimeout failure action on your WLTimerInfo object.");
            }
         }

         return this.timerManager.createTimer(this.getPK(), var1, var3, var4);
      }
   }

   public Collection getTimers() {
      this.ejbCtx.checkAllowedToUseTimerService();
      return this.timerManager.getTimers(this.getPK());
   }

   private Object getPK() {
      return this.ejbCtx instanceof EntityEJBContextImpl ? ((EntityEJBContextImpl)this.ejbCtx).__WL_getPrimaryKey() : new Integer(1);
   }

   private void ensureWLTimerServiceSupport(WLTimerInfo var1) {
      if (this.isClustered && var1 != null) {
         throw new IllegalArgumentException("Error: the clustered EJB timer service does not support the methods of the WLTimerService interface.  Only methods declared on the javax.ejb.TimerService interface may be invoked.");
      }
   }
}
