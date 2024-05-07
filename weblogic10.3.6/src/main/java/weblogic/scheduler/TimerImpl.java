package weblogic.scheduler;

import java.io.Serializable;
import weblogic.timers.TimerListener;

public class TimerImpl implements Timer {
   private final String id;

   TimerImpl(String var1) {
      this.id = var1;
   }

   String getID() {
      return this.id;
   }

   public boolean cancel() {
      try {
         return TimerBasisAccess.getTimerBasis().cancelTimer(this.id);
      } catch (NoSuchObjectLocalException var2) {
         return false;
      } catch (TimerException var3) {
         return false;
      }
   }

   public boolean isStopped() {
      return this.isCancelled();
   }

   public boolean isCancelled() {
      return this.getTimerState() == null;
   }

   public long getTimeout() {
      TimerState var1 = this.getTimerState();
      return var1 == null ? -1L : var1.getTimeout();
   }

   public long getPeriod() {
      TimerState var1 = this.getTimerState();
      return var1 == null ? -1L : var1.getInterval();
   }

   public TimerListener getListener() {
      TimerState var1 = this.getTimerState();
      return var1 == null ? null : this.getTimerState().getTimedObject();
   }

   public Serializable getInfo() throws NoSuchObjectLocalException, TimerException {
      TimerState var1 = TimerBasisAccess.getTimerBasis().getTimerState(this.id);
      return var1.getInfo();
   }

   public TimerHandle getHandle() throws NoSuchObjectLocalException, TimerException {
      return new TimerHandleImpl(this.id);
   }

   private TimerState getTimerState() {
      try {
         return TimerBasisAccess.getTimerBasis().getTimerState(this.id);
      } catch (NoSuchObjectLocalException var2) {
         return null;
      } catch (TimerException var3) {
         return null;
      }
   }

   public int hashCode() {
      int var1 = 17;
      if (this.id != null) {
         var1 *= this.id.hashCode();
      }

      return var1;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof TimerImpl)) {
         return false;
      } else {
         TimerImpl var2 = (TimerImpl)var1;
         if (this.id == null && var2.id == null) {
            return true;
         } else {
            return this.id != null && this.id.equals(var2.id);
         }
      }
   }

   public String toString() {
      return this.id;
   }
}
