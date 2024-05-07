package weblogic.servlet.internal;

import weblogic.timers.NakedTimerListener;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;
import weblogic.work.WorkManagerFactory;

public abstract class NakedTimerListenerBase implements NakedTimerListener {
   protected final TimerManager timerManager;

   protected NakedTimerListenerBase(String var1, WebAppServletContext var2) {
      StringBuilder var3 = new StringBuilder();
      var3.append(var1).append("-Host='").append(var2.getServer().getName()).append("',appId='").append(var2.getApplicationId()).append("',contextPath='").append(var2.getContextPath()).append("'");
      this.timerManager = TimerManagerFactory.getTimerManagerFactory().getTimerManager(var3.toString(), WorkManagerFactory.getInstance().getSystem());
   }
}
