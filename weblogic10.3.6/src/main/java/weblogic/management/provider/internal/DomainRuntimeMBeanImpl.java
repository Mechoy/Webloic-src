package weblogic.management.provider.internal;

import java.security.AccessController;
import java.util.Date;
import weblogic.deploy.internal.adminserver.DeploymentManager;
import weblogic.management.ManagementException;
import weblogic.management.configuration.SystemResourceMBean;
import weblogic.management.provider.DomainAccess;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.AppRuntimeStateRuntimeMBean;
import weblogic.management.runtime.CoherenceServerLifeCycleRuntimeMBean;
import weblogic.management.runtime.ConsoleRuntimeMBean;
import weblogic.management.runtime.DeployerRuntimeMBean;
import weblogic.management.runtime.DeploymentManagerMBean;
import weblogic.management.runtime.DomainRuntimeMBean;
import weblogic.management.runtime.DomainRuntimeMBeanDelegate;
import weblogic.management.runtime.LogRuntimeMBean;
import weblogic.management.runtime.MessageDrivenControlEJBRuntimeMBean;
import weblogic.management.runtime.MigratableServiceCoordinatorRuntimeMBean;
import weblogic.management.runtime.MigrationDataRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.SNMPAgentRuntimeMBean;
import weblogic.management.runtime.ServerLifeCycleRuntimeMBean;
import weblogic.management.runtime.ServiceMigrationDataRuntimeMBean;
import weblogic.management.runtime.WseePolicySubjectManagerRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public final class DomainRuntimeMBeanImpl extends DomainRuntimeMBeanDelegate implements DomainRuntimeMBean {
   private DomainAccess domainAccess;
   private LogRuntimeMBean logRuntime;
   private SNMPAgentRuntimeMBean snmpRuntime;
   private ConsoleRuntimeMBean consoleRuntime;
   private WseePolicySubjectManagerRuntimeMBean policyRuntime;
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private MessageDrivenControlEJBRuntimeMBean messageDrivenControlEJBRuntime;

   DomainRuntimeMBeanImpl() throws ManagementException {
      super(ManagementService.getRuntimeAccess(kernelId).getDomainName(), (RuntimeMBean)null);
      this.domainAccess = ManagementService.getDomainAccess(kernelId);
   }

   public Date getActivationTime() {
      return new Date(this.domainAccess.getActivationTime());
   }

   public DeployerRuntimeMBean getDeployerRuntime() {
      return this.domainAccess.getDeployerRuntime();
   }

   public DeploymentManagerMBean getDeploymentManager() {
      return this.domainAccess.getDeploymentManager();
   }

   public ServerLifeCycleRuntimeMBean lookupServerLifeCycleRuntime(String var1) {
      return this.domainAccess.lookupServerLifecycleRuntime(var1);
   }

   public ServerLifeCycleRuntimeMBean[] getServerLifeCycleRuntimes() {
      return this.domainAccess.getServerLifecycleRuntimes();
   }

   public CoherenceServerLifeCycleRuntimeMBean lookupCoherenceServerLifeCycleRuntime(String var1) {
      return this.domainAccess.lookupCoherenceServerLifecycleRuntime(var1);
   }

   public CoherenceServerLifeCycleRuntimeMBean[] getCoherenceServerLifeCycleRuntimes() {
      return this.domainAccess.getCoherenceServerLifecycleRuntimes();
   }

   public LogRuntimeMBean getLogRuntime() {
      return this.logRuntime;
   }

   public void setLogRuntime(LogRuntimeMBean var1) {
      this.logRuntime = var1;
   }

   public MigratableServiceCoordinatorRuntimeMBean getMigratableServiceCoordinatorRuntime() {
      return this.domainAccess.getMigratableServiceCoordinatorRuntime();
   }

   public MigrationDataRuntimeMBean[] getMigrationDataRuntimes() {
      return this.domainAccess.getMigrationDataRuntimes();
   }

   public AppRuntimeStateRuntimeMBean getAppRuntimeStateRuntime() {
      return this.domainAccess.getAppRuntimeStateRuntime();
   }

   public MessageDrivenControlEJBRuntimeMBean getMessageDrivenControlEJBRuntime() {
      return this.messageDrivenControlEJBRuntime;
   }

   public void setMessageDrivenControlEJBRuntime(MessageDrivenControlEJBRuntimeMBean var1) {
      this.messageDrivenControlEJBRuntime = var1;
   }

   public SNMPAgentRuntimeMBean getSNMPAgentRuntime() {
      return this.snmpRuntime;
   }

   public void setSNMPAgentRuntime(SNMPAgentRuntimeMBean var1) {
      this.snmpRuntime = var1;
   }

   public ConsoleRuntimeMBean getConsoleRuntime() {
      return this.consoleRuntime;
   }

   public void setConsoleRuntime(ConsoleRuntimeMBean var1) {
      this.consoleRuntime = var1;
   }

   public WseePolicySubjectManagerRuntimeMBean getPolicySubjectManagerRuntime() {
      return this.policyRuntime;
   }

   public void setPolicySubjectManagerRuntime(WseePolicySubjectManagerRuntimeMBean var1) {
      this.policyRuntime = var1;
   }

   public void restartSystemResource(SystemResourceMBean var1) throws ManagementException {
      DeploymentManager.getInstance(kernelId).restartSystemResource(var1);
   }

   public ServiceMigrationDataRuntimeMBean[] getServiceMigrationDataRuntimes() {
      return this.domainAccess.getServiceMigrationDataRuntimes();
   }
}
