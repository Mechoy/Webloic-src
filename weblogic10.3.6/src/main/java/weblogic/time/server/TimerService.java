package weblogic.time.server;

import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public final class TimerService extends AbstractServerService {
   public void halt() throws ServiceFailureException {
      weblogic.time.common.internal.ScheduledTrigger.cancelAppTriggers(true);
   }

   public void stop() throws ServiceFailureException {
      this.halt();
   }
}
