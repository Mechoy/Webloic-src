package weblogic.ejb.container.timer;

import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.ejb.container.interfaces.TimerManager;

public final class EJBTimerManagerFactory {
   public static TimerManager createEJBTimerManager(BeanManager var0) {
      return (TimerManager)(var0.getBeanInfo().isClusteredTimers() ? new ClusteredEJBTimerManager(var0) : new EJBTimerManager(var0));
   }

   public static void removeAllTimers(BeanInfo var0) {
      if (var0.isClusteredTimers()) {
         ClusteredEJBTimerManager.removeAllTimers(var0);
      } else {
         EJBTimerManager.removeAllTimers(var0);
      }

   }
}
