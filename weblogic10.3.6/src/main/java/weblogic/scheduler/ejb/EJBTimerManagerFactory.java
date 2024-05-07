package weblogic.scheduler.ejb;

import weblogic.scheduler.ejb.internal.EJBTimerManagerImpl;

public final class EJBTimerManagerFactory {
   public static EJBTimerManagerFactory getInstance() {
      return EJBTimerManagerFactory.Factory.THE_ONE;
   }

   private EJBTimerManagerFactory() {
   }

   public EJBTimerManager create(String var1, String var2) {
      return new EJBTimerManagerImpl(var1, var2);
   }

   public EJBTimerManager create(String var1, String var2, String var3) {
      return new EJBTimerManagerImpl(var1, var2, var3);
   }

   // $FF: synthetic method
   EJBTimerManagerFactory(Object var1) {
      this();
   }

   private static final class Factory {
      static final EJBTimerManagerFactory THE_ONE = new EJBTimerManagerFactory();
   }
}
