package weblogic.management.runtime;

public interface MaxThreadsConstraintRuntimeMBean extends RuntimeMBean {
   int getExecutingRequests();

   int getDeferredRequests();
}
