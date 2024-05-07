package weblogic.wsee.runtime.owsm;

import com.sun.xml.ws.util.ServiceFinder;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.timers.TimerManagerFactory;

public final class OwsmSchedulerHelper {
   private static final Logger LOGGER = Logger.getLogger(OwsmSchedulerHelper.class.getName());

   public static void setSchedulerIfRequired() {
      Iterator var0 = ServiceFinder.find(OwsmScheduler.class).iterator();
      if (var0.hasNext()) {
         OwsmScheduler var1 = (OwsmScheduler)var0.next();
         if (!var1.isInitialized()) {
            TimerManagerFactory var2 = TimerManagerFactory.getTimerManagerFactory();
            var1.setScheduler(var2.getCommonjTimerManager(var2.getDefaultTimerManager()));
         }

         if (var0.hasNext() && LOGGER.isLoggable(Level.WARNING)) {
            LOGGER.log(Level.WARNING, "More than one OWSM Scheduler service found.");
         }
      }

   }
}
