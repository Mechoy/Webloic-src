package weblogic.management.runtime;

public interface EJBPoolRuntimeMBean extends RuntimeMBean {
   long getAccessTotalCount();

   long getMissTotalCount();

   long getDestroyedTotalCount();

   /** @deprecated */
   int getIdleBeansCount();

   int getPooledBeansCurrentCount();

   /** @deprecated */
   int getBeansInUseCount();

   int getBeansInUseCurrentCount();

   /** @deprecated */
   long getWaiterTotalCount();

   int getWaiterCurrentCount();

   long getTimeoutTotalCount();

   void initializePool();
}
