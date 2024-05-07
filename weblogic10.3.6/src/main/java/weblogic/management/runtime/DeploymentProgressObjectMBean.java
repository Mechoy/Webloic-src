package weblogic.management.runtime;

import weblogic.management.configuration.AppDeploymentMBean;

public interface DeploymentProgressObjectMBean extends RuntimeMBean {
   int OPERATION_START = 1;
   int OPERATION_STOP = 2;
   String STATE_INITIALIZED = "STATE_INITIALIZED";
   String STATE_RUNNING = "STATE_RUNNING";
   String STATE_COMPLETED = "STATE_COMPLETED";
   String STATE_FAILED = "STATE_FAILED";
   String STATE_DEFERRED = "STATE_DEFERRED";

   String getId();

   int getOperationType();

   String getApplicationName();

   AppDeploymentMBean getAppDeploymentMBean();

   String getState();

   String[] getTargets();

   String[] getFailedTargets();

   String[] getMessages();

   RuntimeException[] getExceptions(String var1);

   RuntimeException[] getRootExceptions();

   void cancel() throws RuntimeException;

   long getBeginTime();

   long getEndTime();
}
