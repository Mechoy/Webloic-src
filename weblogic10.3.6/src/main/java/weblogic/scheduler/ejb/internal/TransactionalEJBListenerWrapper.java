package weblogic.scheduler.ejb.internal;

import weblogic.scheduler.TransactionalTimerListener;
import weblogic.scheduler.ejb.EJBTimerListener;

public final class TransactionalEJBListenerWrapper extends EJBListenerWrapper implements TransactionalTimerListener {
   public TransactionalEJBListenerWrapper(String var1, EJBTimerListener var2, String var3) {
      super(var1, var2, var3);
   }

   public TransactionalEJBListenerWrapper() {
   }
}
