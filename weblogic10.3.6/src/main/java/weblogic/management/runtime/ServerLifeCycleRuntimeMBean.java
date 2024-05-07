package weblogic.management.runtime;

import weblogic.server.ServerLifecycleException;

public interface ServerLifeCycleRuntimeMBean extends RuntimeMBean, ServerStates {
   ServerLifeCycleTaskRuntimeMBean start() throws ServerLifecycleException;

   ServerLifeCycleTaskRuntimeMBean start(String var1) throws ServerLifecycleException;

   ServerLifeCycleTaskRuntimeMBean startInStandby() throws ServerLifecycleException;

   ServerLifeCycleTaskRuntimeMBean resume() throws ServerLifecycleException;

   ServerLifeCycleTaskRuntimeMBean suspend() throws ServerLifecycleException;

   ServerLifeCycleTaskRuntimeMBean suspend(int var1, boolean var2) throws ServerLifecycleException;

   ServerLifeCycleTaskRuntimeMBean forceSuspend() throws ServerLifecycleException;

   ServerLifeCycleTaskRuntimeMBean shutdown() throws ServerLifecycleException;

   ServerLifeCycleTaskRuntimeMBean shutdown(int var1, boolean var2) throws ServerLifecycleException;

   ServerLifeCycleTaskRuntimeMBean forceShutdown() throws ServerLifecycleException;

   ServerLifeCycleTaskRuntimeMBean[] getTasks();

   String getState();

   int getNodeManagerRestartCount();

   void setState(String var1);

   int getStateVal();

   String getBulkQueryState();

   void setBulkQueryState(String var1);

   void clearOldServerLifeCycleTaskRuntimes();

   String getWeblogicHome();

   String getMiddlewareHome();

   String getIPv4URL(String var1);

   String getIPv6URL(String var1);
}
