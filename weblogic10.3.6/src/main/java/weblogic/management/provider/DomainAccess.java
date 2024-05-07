package weblogic.management.provider;

import weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean;
import weblogic.management.runtime.AppRuntimeStateRuntimeMBean;
import weblogic.management.runtime.CoherenceServerLifeCycleRuntimeMBean;
import weblogic.management.runtime.DeployerRuntimeMBean;
import weblogic.management.runtime.DeploymentManagerMBean;
import weblogic.management.runtime.DomainRuntimeMBean;
import weblogic.management.runtime.MigratableServiceCoordinatorRuntimeMBean;
import weblogic.management.runtime.MigrationDataRuntimeMBean;
import weblogic.management.runtime.ServerLifeCycleRuntimeMBean;
import weblogic.management.runtime.ServiceMigrationDataRuntimeMBean;

public interface DomainAccess extends RegistrationManager {
   DomainRuntimeMBean getDomainRuntime();

   long getActivationTime();

   DeployerRuntimeMBean getDeployerRuntime();

   DeploymentManagerMBean getDeploymentManager();

   ServerLifeCycleRuntimeMBean[] getServerLifecycleRuntimes();

   CoherenceServerLifeCycleRuntimeMBean lookupCoherenceServerLifecycleRuntime(String var1);

   CoherenceServerLifeCycleRuntimeMBean[] getCoherenceServerLifecycleRuntimes();

   ServerLifeCycleRuntimeMBean lookupServerLifecycleRuntime(String var1);

   String[] getClusters();

   MigratableServiceCoordinatorRuntimeMBean getMigratableServiceCoordinatorRuntime();

   AppRuntimeStateRuntimeMBean getAppRuntimeStateRuntime();

   void setAppRuntimeStateRuntime(AppRuntimeStateRuntimeMBean var1);

   DomainRuntimeServiceMBean getDomainRuntimeService();

   void setDomainRuntimeService(DomainRuntimeServiceMBean var1);

   MigrationDataRuntimeMBean[] getMigrationDataRuntimes();

   ServiceMigrationDataRuntimeMBean[] getServiceMigrationDataRuntimes();
}
