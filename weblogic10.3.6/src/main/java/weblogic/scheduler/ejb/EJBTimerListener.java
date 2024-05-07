package weblogic.scheduler.ejb;

import java.io.Serializable;
import weblogic.timers.TimerListener;

public interface EJBTimerListener extends TimerListener, Serializable {
   String getGroupName();

   boolean isTransactional();
}
