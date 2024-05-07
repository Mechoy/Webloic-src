package weblogic.management.provider.internal;

import java.security.AccessController;
import weblogic.cluster.singleton.DomainMigrationHistory;
import weblogic.cluster.singleton.ServerMigrationRuntimeMBeanImpl;
import weblogic.cluster.singleton.ServiceMigrationRuntimeMBeanImpl;
import weblogic.management.ManagementException;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean;
import weblogic.management.provider.DomainAccess;
import weblogic.management.provider.DomainAccessSettable;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.AppRuntimeStateRuntimeMBean;
import weblogic.management.runtime.CoherenceServerLifeCycleRuntimeMBean;
import weblogic.management.runtime.DeployerRuntimeMBean;
import weblogic.management.runtime.DeploymentManagerMBean;
import weblogic.management.runtime.DomainRuntimeMBean;
import weblogic.management.runtime.MigratableServiceCoordinatorRuntimeMBean;
import weblogic.management.runtime.MigrationDataRuntimeMBean;
import weblogic.management.runtime.ServerLifeCycleRuntimeMBean;
import weblogic.management.runtime.ServerMigrationRuntimeMBean;
import weblogic.management.runtime.ServiceMigrationDataRuntimeMBean;
import weblogic.management.runtime.ServiceMigrationRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.ServerLifeCycleService;

public class DomainAccessImpl extends RegistrationManagerImpl implements DomainAccess, DomainAccessSettable {
   private final long activationTime = System.currentTimeMillis();
   private DeployerRuntimeMBean deployerRuntime;
   private DeploymentManagerMBean deploymentManager;
   private ServerLifeCycleService lifecycleService;
   private MigratableServiceCoordinatorRuntimeMBean serverMigrationCoordinator;
   private DomainRuntimeMBean domainRuntime;
   private DomainRuntimeServiceMBean domainRuntimeService;
   private AppRuntimeStateRuntimeMBean appRuntimeStateRuntime;
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private DomainMigrationHistory domainMigrationHistory;

   DomainAccessImpl() {
   }

   void initializeDomainRuntimeMBean() {
      try {
         this.domainRuntime = new DomainRuntimeMBeanImpl();
      } catch (ManagementException var2) {
         throw new Error(var2);
      }
   }

   public AppRuntimeStateRuntimeMBean getAppRuntimeStateRuntime() {
      return this.appRuntimeStateRuntime;
   }

   public DeployerRuntimeMBean getDeployerRuntime() {
      return this.deployerRuntime;
   }

   public DeploymentManagerMBean getDeploymentManager() {
      return this.deploymentManager;
   }

   public ServerLifeCycleRuntimeMBean[] getServerLifecycleRuntimes() {
      return this.lifecycleService.getServerLifecycleRuntimes();
   }

   public ServerLifeCycleRuntimeMBean lookupServerLifecycleRuntime(String var1) {
      return this.lifecycleService == null ? null : this.lifecycleService.lookupServerLifecycleRuntime(var1);
   }

   public CoherenceServerLifeCycleRuntimeMBean[] getCoherenceServerLifecycleRuntimes() {
      return this.lifecycleService.getCoherenceServerLifecycleRuntimes();
   }

   public CoherenceServerLifeCycleRuntimeMBean lookupCoherenceServerLifecycleRuntime(String var1) {
      return this.lifecycleService == null ? null : this.lifecycleService.lookupCoherenceServerLifecycleRuntime(var1);
   }

   public DomainRuntimeMBean getDomainRuntime() {
      return this.domainRuntime;
   }

   public long getActivationTime() {
      return this.activationTime;
   }

   public String[] getClusters() {
      ClusterMBean[] var1 = ManagementService.getRuntimeAccess(kernelId).getDomain().getClusters();
      String[] var2 = new String[var1.length];

      for(int var3 = 0; var3 < var1.length; ++var3) {
         ClusterMBean var4 = var1[var3];
         var2[var3] = var4.getName();
      }

      return var2;
   }

   public MigratableServiceCoordinatorRuntimeMBean getMigratableServiceCoordinatorRuntime() {
      return this.serverMigrationCoordinator;
   }

   public DomainRuntimeServiceMBean getDomainRuntimeService() {
      return this.domainRuntimeService;
   }

   public void setDomainRuntimeService(DomainRuntimeServiceMBean var1) {
      this.domainRuntimeService = var1;
   }

   public MigrationDataRuntimeMBean[] getMigrationDataRuntimes() {
      return this.domainMigrationHistory.getMigrationDataRuntimes();
   }

   public ServiceMigrationDataRuntimeMBean[] getServiceMigrationDataRuntimes() {
      return this.domainMigrationHistory != null ? this.domainMigrationHistory.getServiceMigrationDataRuntimes() : null;
   }

   public ServerMigrationRuntimeMBean ServerMigrationRuntime() {
      return ServerMigrationRuntimeMBeanImpl.getInstance();
   }

   public ServiceMigrationRuntimeMBean ServiceMigrationRuntime() {
      return ServiceMigrationRuntimeMBeanImpl.getInstance();
   }

   public void setDeployerRuntime(DeployerRuntimeMBean var1) {
      if (this.deployerRuntime != null) {
         throw new Error("DeployerRuntime may only be initialized onced during during server startup");
      } else {
         this.deployerRuntime = var1;
      }
   }

   public void setDeploymentManager(DeploymentManagerMBean var1) {
      if (this.deploymentManager != null) {
         throw new Error("DeploymentManager may only be initialized onced during during server startup");
      } else {
         this.deploymentManager = var1;
      }
   }

   public void setLifecycleService(ServerLifeCycleService var1) {
      if (this.lifecycleService != null) {
         throw new Error("Lifecycle Service may only be initialized onced during during server startup");
      } else {
         this.lifecycleService = var1;
      }
   }

   public void setDomainMigrationHistory(DomainMigrationHistory var1) {
      this.domainMigrationHistory = var1;
   }

   public void setServerMigrationCoordinator(MigratableServiceCoordinatorRuntimeMBean var1) {
      if (this.serverMigrationCoordinator != null) {
         throw new Error("The ServerMigrationCoordinator may only be initialized onced during during server startup");
      } else {
         this.serverMigrationCoordinator = var1;
      }
   }

   public void setAppRuntimeStateRuntime(AppRuntimeStateRuntimeMBean var1) {
      if (this.appRuntimeStateRuntime != null) {
         throw new Error("The AppRuntimeStateRuntime may only be initialized onced during during server startup");
      } else {
         this.appRuntimeStateRuntime = var1;
      }
   }
}
