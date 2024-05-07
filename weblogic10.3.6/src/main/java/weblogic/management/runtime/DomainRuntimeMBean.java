package weblogic.management.runtime;

import java.util.Date;
import weblogic.management.ManagementException;
import weblogic.management.configuration.SystemResourceMBean;

public interface DomainRuntimeMBean extends RuntimeMBean {
   Date getActivationTime();

   ServerLifeCycleRuntimeMBean[] getServerLifeCycleRuntimes();

   ServerLifeCycleRuntimeMBean lookupServerLifeCycleRuntime(String var1);

   CoherenceServerLifeCycleRuntimeMBean[] getCoherenceServerLifeCycleRuntimes();

   CoherenceServerLifeCycleRuntimeMBean lookupCoherenceServerLifeCycleRuntime(String var1);

   /** @deprecated */
   DeployerRuntimeMBean getDeployerRuntime();

   DeploymentManagerMBean getDeploymentManager();

   AppRuntimeStateRuntimeMBean getAppRuntimeStateRuntime();

   MigratableServiceCoordinatorRuntimeMBean getMigratableServiceCoordinatorRuntime();

   MigrationDataRuntimeMBean[] getMigrationDataRuntimes();

   LogRuntimeMBean getLogRuntime();

   void setLogRuntime(LogRuntimeMBean var1);

   MessageDrivenControlEJBRuntimeMBean getMessageDrivenControlEJBRuntime();

   void setMessageDrivenControlEJBRuntime(MessageDrivenControlEJBRuntimeMBean var1);

   SNMPAgentRuntimeMBean getSNMPAgentRuntime();

   void setSNMPAgentRuntime(SNMPAgentRuntimeMBean var1);

   ConsoleRuntimeMBean getConsoleRuntime();

   void setConsoleRuntime(ConsoleRuntimeMBean var1);

   WseePolicySubjectManagerRuntimeMBean getPolicySubjectManagerRuntime();

   void setPolicySubjectManagerRuntime(WseePolicySubjectManagerRuntimeMBean var1);

   void restartSystemResource(SystemResourceMBean var1) throws ManagementException;

   ServiceMigrationDataRuntimeMBean[] getServiceMigrationDataRuntimes();
}
