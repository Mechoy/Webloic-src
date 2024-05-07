package weblogic.ejb.container.timer;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import weblogic.ejb.container.interfaces.TimerManager;
import weblogic.server.ActivatedService;
import weblogic.server.ServiceFailureException;

public final class EJBTimerStarter extends ActivatedService {
   private static boolean started = false;
   private static Set timerManagers = new HashSet();

   public synchronized void stopService() throws ServiceFailureException {
      started = false;
   }

   public synchronized void haltService() throws ServiceFailureException {
      this.stopService();
   }

   public synchronized boolean startService() throws ServiceFailureException {
      started = true;
      this.startTimerManagers();
      return true;
   }

   public static void addTimerManagerStarter(TimerManager var0) {
      synchronized(timerManagers) {
         if (!started) {
            timerManagers.add(var0);
            return;
         }
      }

      var0.start();
   }

   private void startTimerManagers() {
      synchronized(timerManagers) {
         Iterator var2 = timerManagers.iterator();

         while(var2.hasNext()) {
            TimerManager var3 = (TimerManager)var2.next();
            var3.start();
         }

         timerManagers.clear();
      }
   }
}
