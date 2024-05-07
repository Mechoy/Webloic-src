package weblogic.management.runtime;

public interface PageFlowActionRuntimeMBean extends RuntimeMBean {
   /** @deprecated */
   String getActionName();

   /** @deprecated */
   long getSuccessCount();

   /** @deprecated */
   long getExceptionCount();

   /** @deprecated */
   long getHandledExceptionCount();

   /** @deprecated */
   long getSuccessDispatchTimeTotal();

   /** @deprecated */
   long getSuccessDispatchTimeHigh();

   /** @deprecated */
   long getSuccessDispatchTimeLow();

   /** @deprecated */
   long getSuccessDispatchTimeAverage();

   /** @deprecated */
   long getHandledExceptionDispatchTimeTotal();

   /** @deprecated */
   long getHandledExceptionDispatchTimeHigh();

   /** @deprecated */
   long getHandledExceptionDispatchTimeLow();

   /** @deprecated */
   long getHandledExceptionDispatchTimeAverage();

   /** @deprecated */
   PageFlowError[] getLastExceptions();
}
