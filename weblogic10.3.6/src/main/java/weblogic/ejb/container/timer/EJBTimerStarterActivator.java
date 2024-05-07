package weblogic.ejb.container.timer;

import weblogic.ejb.container.EJBServiceActivator;

public final class EJBTimerStarterActivator extends EJBServiceActivator {
   public static final EJBTimerStarterActivator INSTANCE = new EJBTimerStarterActivator();

   private EJBTimerStarterActivator() {
      super("weblogic.ejb.container.timer.EJBTimerStarter");
   }

   public String getName() {
      return "EJBTimerService";
   }
}
