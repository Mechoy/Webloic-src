package weblogic.management.runtime;

public interface ServletRuntimeMBean extends RuntimeMBean {
   String getServletName();

   String getServletClassName();

   int getReloadTotalCount();

   int getInvocationTotalCount();

   int getPoolMaxCapacity();

   long getExecutionTimeTotal();

   int getExecutionTimeHigh();

   int getExecutionTimeLow();

   int getExecutionTimeAverage();

   String getServletPath();

   String getContextPath();

   String getURL();

   boolean isInternalServlet();

   String[] getURLPatterns();
}
