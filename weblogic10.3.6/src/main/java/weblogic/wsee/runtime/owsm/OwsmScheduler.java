package weblogic.wsee.runtime.owsm;

import commonj.timers.TimerManager;

public interface OwsmScheduler {
   boolean isInitialized();

   void setScheduler(TimerManager var1);
}
