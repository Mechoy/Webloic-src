package weblogic.management.runtime;

public interface MinThreadsConstraintRuntimeMBean extends RuntimeMBean {
   long getCompletedRequests();

   int getPendingRequests();

   int getExecutingRequests();

   long getOutOfOrderExecutionCount();

   int getMustRunCount();

   long getMaxWaitTime();

   long getCurrentWaitTime();
}
