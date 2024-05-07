package weblogic.management.runtime;

public interface DeploymentManagerMBean extends RuntimeMBean {
   String APPDEPLOYMENT_CREATED = "weblogic.appdeployment.created";
   String APPDEPLOYMENT_DELETED = "weblogic.appdeployment.deleted";
   String APPDEPLOYMENT_NEW = "weblogic.appdeployment.state.new";
   String APPDEPLOYMENT_PREPARED = "weblogic.appdeployment.state.prepared";
   String APPDEPLOYMENT_ADMIN = "weblogic.appdeployment.state.admin";
   String APPDEPLOYMENT_ACTIVE = "weblogic.appdeployment.state.active";
   String APPDEPLOYMENT_RETIRED = "weblogic.appdeployment.state.retired";
   String APPDEPLOYMENT_FAILED = "weblogic.appdeployment.state.failed";
   String APPDEPLOYMENT_UPDATE_PENDING = "weblogic.appdeployment.state.update.pending";
   String APPDEPLOYMENT_UNKNOWN = "weblogic.appdeployment.state.unknown";

   AppDeploymentRuntimeMBean[] getAppDeploymentRuntimes();

   AppDeploymentRuntimeMBean lookupAppDeploymentRuntime(String var1);

   DeploymentProgressObjectMBean[] getDeploymentProgressObjects();

   void setMaximumDeploymentProgressObjectsCount(int var1);

   int getMaximumDeploymentProgressObjectsCount();

   void purgeCompletedDeploymentProgressObjects();

   void removeDeploymentProgressObject(String var1);
}
