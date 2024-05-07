package weblogic.scheduler.ejb;

import weblogic.timers.Timer;
import weblogic.timers.TimerManager;

public interface EJBTimerManager extends TimerManager {
   Timer[] getTimers();

   Timer[] getTimers(String var1);
}
