package weblogic.ejb.container.timer;

import javax.ejb.EJBException;
import javax.ejb.TimerHandle;
import weblogic.scheduler.NoSuchObjectLocalException;
import weblogic.scheduler.Timer;
import weblogic.scheduler.TimerException;

public final class ClusteredTimerHandleImpl implements TimerHandle {
   private static final long serialVersionUID = -6940054380113951006L;
   private weblogic.scheduler.TimerHandle handle;

   public ClusteredTimerHandleImpl(Timer var1) {
      try {
         this.handle = var1.getHandle();
      } catch (NoSuchObjectLocalException var4) {
         throw new javax.ejb.NoSuchObjectLocalException("Error getting TimerHandle: " + var4, var4);
      } catch (TimerException var5) {
         EJBException var3 = new EJBException("Error getting TimerHandle: " + var5, var5);
         var3.initCause(var5);
         throw var3;
      }
   }

   public javax.ejb.Timer getTimer() {
      try {
         Timer var1 = this.handle.getTimer();
         ClusteredTimerImpl var5 = (ClusteredTimerImpl)var1.getListener();
         var5.initialize(var1);
         return new TimerWrapper(var5);
      } catch (NoSuchObjectLocalException var3) {
         throw new javax.ejb.NoSuchObjectLocalException("Error getting Timer: " + var3, var3);
      } catch (TimerException var4) {
         EJBException var2 = new EJBException("Error getting Timer: " + var4, var4);
         var2.initCause(var4);
         throw var2;
      }
   }

   public int hashCode() {
      return this.handle.hashCode();
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof ClusteredTimerHandleImpl)) {
         return false;
      } else {
         ClusteredTimerHandleImpl var2 = (ClusteredTimerHandleImpl)var1;
         return this.handle.equals(var2.handle);
      }
   }
}
