package weblogic.management.runtime;

import java.util.Properties;

public interface AppDeploymentRuntimeMBean extends RuntimeMBean {
   String CLUSTER_DEPLOYMENT_TIMEOUT = "clusterDeploymentTimeout";
   String GRACEFUL_IGNORE_SESSIONS = "gracefulIgnoreSessions";
   String GRACEFUL_PRODUCTION_TO_ADMIN = "gracefulProductionToAdmin";
   String RETIRE_GRACEFULLY = "retireGracefully";
   String RETIRE_TIMEOUT = "retireTimeout";
   String ADMIN_MODE = "adminMode";
   String TIMEOUT = "timeout";

   String getApplicationName();

   String getApplicationVersion();

   DeploymentProgressObjectMBean start() throws RuntimeException;

   DeploymentProgressObjectMBean start(String[] var1, Properties var2) throws RuntimeException;

   DeploymentProgressObjectMBean stop() throws RuntimeException;

   DeploymentProgressObjectMBean stop(String[] var1, Properties var2) throws RuntimeException;

   String[] getModules();

   String getState(String var1);
}
