package weblogic.t3.srvr.servicegroups;

import weblogic.cluster.ClusterServiceActivator;
import weblogic.server.ServiceActivator;
import weblogic.server.servicegraph.Service;
import weblogic.server.servicegraph.ServiceGroup;

public class CoreServiceGroup extends ServiceGroup {
   private static final Service ADMIN_STATE_SERVICE = new Service("admin_state");
   private static final Service STANDBY_STATE_SERVICE = new Service("standby_state");
   private static final Service clusterInboundService = new Service("weblogic.cluster.InboundService");
   private static final Service clusterService;
   private static final Service defaultStoreService;
   private static final Service deploymentPostAdminServerService;
   private static final Service deploymentService;
   private static final Service diagnosticFoundationService;
   private static final Service enableListenersService;
   private static final Service jdbcService;
   private static final Service namingService;
   private static final Service storeDeploymentService;
   private static final Service transactionService;
   private static final Service jobSchedulerService;
   private static final Service webService;

   public static Service getStandbyState() {
      return STANDBY_STATE_SERVICE;
   }

   public static Service getDiagnosticFoundationService() {
      return diagnosticFoundationService;
   }

   public static Service getJDBCService() {
      return jdbcService;
   }

   public static Service getDefaultStoreService() {
      return defaultStoreService;
   }

   public static Service getStoreDeploymentService() {
      return storeDeploymentService;
   }

   public static Service getNamingService() {
      return namingService;
   }

   public static Service getDeploymentPostAdminServerService() {
      return deploymentPostAdminServerService;
   }

   public static Service getDeploymentService() {
      return deploymentService;
   }

   public static Service getClusterInboundService() {
      return clusterInboundService;
   }

   public static Service getEnableListenersService() {
      return enableListenersService;
   }

   public static Service getTransactionService() {
      return transactionService;
   }

   public static Service getJobSchedulerService() {
      return jobSchedulerService;
   }

   public static Service getWebService() {
      return webService;
   }

   public CoreServiceGroup(boolean var1) {
      super(var1);
      Service var2 = JMSServiceGroup.getJMSService();
      this.addServiceClass("weblogic.t3.srvr.PreConfigBootService");
      this.addServiceClass("weblogic.management.provider.internal.BeanInfoAccessService");
      Service var3 = this.addServiceClass("weblogic.management.provider.PropertyService");
      this.addServiceClass("weblogic.management.internal.DomainDirectoryService");
      this.addServiceClass("weblogic.management.provider.MSIService");
      this.addServiceClass("weblogic.upgrade.domain.DomainUpgradeServerService");
      this.addServiceClass("weblogic.deploy.service.internal.DeploymentService");
      this.addServiceClass("weblogic.deploy.internal.targetserver.datamanagement.ConfigRecoveryService");
      this.addServiceClass("weblogic.management.provider.internal.RuntimeAccessDeploymentReceiverService");
      Service var4 = this.addServiceClass("weblogic.management.provider.internal.RuntimeAccessService");
      var4.addDependency(var3);
      this.addServiceClass("weblogic.management.mbeanservers.runtime.internal.DiagnosticSupportService");
      Service var5 = this.addServiceClass("weblogic.t3.srvr.BootService");
      var5.addDependency(var4);
      this.addServiceClass("weblogic.management.provider.internal.WorkManagerService");
      this.addServiceClass("weblogic.management.provider.internal.DomainAccessService");
      this.addService(diagnosticFoundationService);
      this.addServiceClass("weblogic.nodemanager.NMService");
      this.addServiceClass("weblogic.timers.internal.TimerService");
      this.addServiceClass("weblogic.rjvm.RJVMService");
      this.addServiceClass("weblogic.protocol.ProtocolService");
      this.addServiceClass("weblogic.server.channels.ChannelService");
      this.addServiceClass("weblogic.socket.SocketMuxerServerService");
      this.addServiceClass("weblogic.server.channels.AdminPortService");
      this.addServiceClass("weblogic.t3.srvr.ListenerService");
      this.addServiceClass("weblogic.transaction.internal.PrimordialTransactionService");
      this.addServiceClass("weblogic.rmi.internal.RMIServerService");
      this.addService(namingService);
      this.addServiceClass("weblogic.iiop.IIOPClientService");
      this.addServiceClass("weblogic.diagnostics.lifecycle.DiagnosticInstrumentationService");
      Service var6 = this.addServiceClass("weblogic.ldap.EmbeddedLDAP");
      Service var7 = this.addServiceClass("weblogic.security.SecurityService");
      var7.addDependency(var6);
      Service var8 = this.addServiceClass("weblogic.jndi.internal.RemoteNamingService");
      var8.addDependency(var7);
      this.addServiceClass("weblogic.security.acl.internal.RemoteSecurityService");
      this.addServiceClass("weblogic.rmi.cluster.RemoteBinderFactoryService");
      this.addServiceClass("weblogic.cluster.leasing.databaseless.DatabaseLessLeasingService");
      this.addService(clusterService);
      this.addServiceClass("weblogic.cluster.leasing.databaseless.PrimordialClusterLeaderService");
      this.addServiceClass("weblogic.jndi.internal.ForeignJNDIManagerService");
      Service var9 = this.addServiceClass("weblogic.iiop.IIOPService");
      var9.addDependency(clusterService);
      this.addServiceClass("weblogic.protocol.ProtocolHandlerService");
      this.addServiceClass("weblogic.xml.registry.XMLService");
      this.addServiceClass("weblogic.messaging.interception.MessageInterceptionService");
      this.addServiceClass("weblogic.cluster.migration.rmiservice.MigratableRMIService");
      this.addServiceClass("weblogic.management.provider.internal.EditAccessService");
      this.addServiceClass("weblogic.management.mbeanservers.compatibility.internal.CompatibilityMBeanServerService");
      this.addServiceClass("weblogic.health.HealthMonitorService");
      this.addServiceClass("weblogic.cluster.singleton.MigratableServerService");
      this.addServiceClass("weblogic.cluster.migration.MigrationService");
      Service var10 = this.addServiceClass("weblogic.t3.srvr.T3InitializationService");
      var10.addDependency(var7);
      this.addServiceClass("weblogic.jms.dotnet.t3.server.CSharpInitializationService");
      this.addServiceClass("weblogic.server.channels.ChannelRuntimeService");
      this.addServiceClass("weblogic.transaction.internal.TransactionRecoveryFailBackService");
      this.addServiceClass("weblogic.transaction.internal.TransactionRecoveryNoOpService");
      this.addService(defaultStoreService);
      this.addService(transactionService);
      this.addService(jdbcService);
      jdbcService.addDependency(transactionService);
      this.addService(storeDeploymentService);
      storeDeploymentService.addDependency(jdbcService);
      storeDeploymentService.addDependency(transactionService);
      this.addServiceClass("weblogic.management.extension.internal.CustomResourceServerService");
      this.addServiceClass("weblogic.cacheprovider.CacheProviderServerService");
      Service var11 = this.addServiceClass("weblogic.application.ApplicationShutdownService");
      var11.addPredecessor(JMSServiceGroup.getJMSService());
      this.addServiceClass("weblogic.io.common.internal.FileService");
      this.addServiceClass("weblogic.time.server.TimerService");
      this.addServiceClass("weblogic.rmi.internal.HeartbeatHelperService");
      this.addService(webService);
      webService.addPredecessor(JMSServiceGroup.getJMSService());
      webService.addDependency(transactionService);
      webService.addDependency(storeDeploymentService);
      webService.addPredecessor(EJBServiceGroup.getEJBService());
      webService.addPredecessor(JMSServiceGroup.getSAFService());
      Service var12 = this.addServiceClass("weblogic.webservice.conversation.internal.ConversationServiceImpl");
      var12.addPredecessor(var2);
      var12.addDependency(webService);
      this.addServiceClass("weblogic.wtc.gwt.WTCServerLifeCycleImpl");
      Service var13 = this.addServiceClass("weblogic.webservice.WSServerService");
      var13.addPredecessor(var2);
      this.addServiceClass("weblogic.wsee.wstx.internal.WSATTransactionService");
      this.addServiceClass("weblogic.management.mbeanservers.runtime.internal.RuntimeServerService");
      this.addServiceClass("weblogic.management.mbeanservers.edit.internal.EditServerService");
      this.addServiceClass("weblogic.management.mbeanservers.domainruntime.internal.DomainRuntimeServerService");
      this.addServiceClass("weblogic.management.deploy.classdeployment.ClassDeploymentService");
      this.addServiceClass("weblogic.server.ServerLifeCycleService");
      this.addServiceClass("weblogic.server.channels.EnableAdminListenersService");
      this.addServiceClass("weblogic.management.provider.internal.ConfigImageSourceService");
      this.addServiceClass("weblogic.messaging.path.PathService");
      this.addServiceClass("weblogic.diagnostics.snmp.server.SNMPAgentDeploymentService");
      this.addServiceClass("weblogic.management.deploy.internal.DeploymentPreStandbyServerService");
      this.addService(STANDBY_STATE_SERVICE);
      STANDBY_STATE_SERVICE.addPredecessor(EJBServiceGroup.getEJBService());
      this.addServiceClass("weblogic.transaction.internal.TransactionRecoveryService");
      this.addServiceClass("weblogic.scheduler.JobSchedulerBindingService");
      this.addServiceClass("weblogic.server.AdminServerListenerService");
      this.addService(deploymentService);
      this.addServiceClass("weblogic.diagnostics.lifecycle.DiagnosticSystemService");
      this.addServiceClass("weblogic.transaction.internal.PostLoggingResourceService");
      this.addServiceClass("weblogic.management.deploy.classdeployment.StartupClassPrelistenService");
      this.addServiceClass("weblogic.management.internal.ConfigurationAuditorService");
      this.addService(clusterInboundService);
      clusterInboundService.addPredecessor(STANDBY_STATE_SERVICE);
      this.addServiceClass("weblogic.t3.srvr.CoreHealthService");
      this.addServiceClass("weblogic.management.deploy.classdeployment.ShutdownClassDeploymentService");
      this.addServiceClass("weblogic.diagnostics.snmp.server.SNMPService");
      this.addServiceClass("weblogic.diagnostics.harvester.HarvesterService");
      this.addServiceClass("weblogic.console.internal.ConsoleMBeanService");
      this.addServiceClass("weblogic.management.j2ee.internal.InternalAppDataCacheService");
      this.addServiceClass("weblogic.management.provider.internal.ConfigBackupService");
      Service var14 = this.addServiceClass("weblogic.t3.srvr.EnableListenersIfAdminChannelAbsentService");
      var14.addDependency(clusterInboundService);
      this.addServiceClass("weblogic.management.mbeanservers.runtime.internal.RegisterWithDomainRuntimeServiceEarly");
      this.addServiceClass("weblogic.application.internal.BackgroundDeploymentService");
      this.addServiceClass("weblogic.cluster.singleton.PreAdminSingletonServicesService");
      this.addServiceClass("weblogic.cluster.leasing.databaseless.ClusterLeaderService");
      this.addService(ADMIN_STATE_SERVICE);
      this.addService(deploymentPostAdminServerService);
      this.addServiceClass("weblogic.management.deploy.classdeployment.StartupClassAfterAppsRunningService");
      this.addService(jobSchedulerService);
      this.addServiceClass("weblogic.cluster.replication.ReplicationService");
      this.addServiceClass("weblogic.cluster.singleton.SingletonServicesBatchManager");
      this.addService(enableListenersService);
      enableListenersService.addDependency(clusterInboundService);
      this.addServiceClass("weblogic.management.mbeanservers.runtime.internal.RegisterWithDomainRuntimeServiceLate");
      this.addServiceClass("weblogic.cluster.OutboundService");
      this.addServiceClass("weblogic.cluster.singleton.PostAdminSingletonServicesService");
      this.addServiceClass("weblogic.rmi.internal.RMIShutdownService");
      this.addServiceClass("weblogic.transaction.internal.ClientInitiatedTxShutdownService");
      this.addServiceClass("weblogic.servlet.internal.WebAppShutdownService");
      this.addServiceClass("weblogic.rmi.internal.NonTxRMIShutdownService");
      this.addServiceClass("weblogic.deploy.service.internal.adminserver.HeartbeatService");
      this.addServiceClass("weblogic.application.internal.BackgroundDeploymentService$WaitForBackgroundCompletion");
      this.addServiceClass("weblogic.cache.management.CacheServerService");
      this.addServiceClass("weblogic.management.internal.OCMService");
   }

   public boolean isAvailable() {
      return true;
   }

   static {
      clusterService = CoreServiceGroup.ClusterService.INSTANCE;
      defaultStoreService = new Service("weblogic.store.admin.DefaultStoreService");
      deploymentPostAdminServerService = new Service("weblogic.management.deploy.internal.DeploymentPostAdminServerService");
      deploymentService = CoreServiceGroup.DeploymentService.INSTANCE;
      diagnosticFoundationService = new Service("weblogic.diagnostics.lifecycle.DiagnosticFoundationService");
      enableListenersService = new Service("weblogic.t3.srvr.EnableListenersService");
      jdbcService = new Service("weblogic.jdbc.common.internal.JDBCService");
      namingService = new Service("weblogic.jndi.internal.NamingService");
      storeDeploymentService = new Service("weblogic.store.admin.StoreDeploymentService");
      transactionService = CoreServiceGroup.TransactionService.INSTANCE;
      jobSchedulerService = new Service("weblogic.scheduler.JobSchedulerService");
      webService = new Service("weblogic.servlet.internal.WebService");
   }

   public static class TransactionService extends Service {
      static final TransactionService INSTANCE = new TransactionService("weblogic.transaction.internal.TransactionService");

      private TransactionService(String var1) {
         super(var1);
      }
   }

   public static class ClusterService extends Service {
      static final ClusterService INSTANCE;

      private ClusterService(ServiceActivator var1) {
         super(var1);
      }

      static {
         INSTANCE = new ClusterService(ClusterServiceActivator.INSTANCE);
      }
   }

   public static class DeploymentService extends Service {
      static final DeploymentService INSTANCE = new DeploymentService("weblogic.management.deploy.internal.DeploymentServerService");

      private DeploymentService(String var1) {
         super(var1);
      }
   }
}
