package weblogic.management.provider;

import weblogic.cluster.singleton.DomainMigrationHistory;
import weblogic.management.runtime.DeployerRuntimeMBean;
import weblogic.management.runtime.DeploymentManagerMBean;
import weblogic.management.runtime.MigratableServiceCoordinatorRuntimeMBean;
import weblogic.server.ServerLifeCycleService;

public interface DomainAccessSettable {
   void setServerMigrationCoordinator(MigratableServiceCoordinatorRuntimeMBean var1);

   void setDomainMigrationHistory(DomainMigrationHistory var1);

   void setLifecycleService(ServerLifeCycleService var1);

   void setDeployerRuntime(DeployerRuntimeMBean var1);

   void setDeploymentManager(DeploymentManagerMBean var1);
}
