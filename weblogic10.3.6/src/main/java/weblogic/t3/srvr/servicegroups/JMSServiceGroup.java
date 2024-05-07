package weblogic.t3.srvr.servicegroups;

import weblogic.jms.JMSServiceActivator;
import weblogic.server.servicegraph.Service;
import weblogic.server.servicegraph.ServiceGroup;

public class JMSServiceGroup extends ServiceGroup {
   static final Service jmsService;
   static final Service jmsBridgeService;
   static final Service safService;

   public static Service getJMSService() {
      return jmsService;
   }

   public static Service getJMSBridgeServe() {
      return jmsBridgeService;
   }

   public static Service getSAFService() {
      return safService;
   }

   public JMSServiceGroup(boolean var1) {
      super(var1);
      Service var2 = this.addServiceClass("weblogic.messaging.interception.configuration.Configurator");
      var2.addSuccessor(CoreServiceGroup.getWebService());
      this.addService(jmsService);
      jmsService.addDependency(CoreServiceGroup.getTransactionService());
      jmsService.addDependency(CoreServiceGroup.getDiagnosticFoundationService());
      jmsService.addDependency(CoreServiceGroup.getJDBCService());
      jmsService.addDependency(CoreServiceGroup.getStoreDeploymentService());
      jmsService.addDependency(CoreServiceGroup.getDefaultStoreService());
      jmsService.addDependency(CoreServiceGroup.getNamingService());
      this.addService(jmsBridgeService);
      jmsBridgeService.addDependency(jmsService);
      jmsBridgeService.addDependency(CoreServiceGroup.getNamingService());
      jmsBridgeService.addSuccessor(CoreServiceGroup.getStandbyState());
      this.addService(safService);
      safService.addDependency(jmsBridgeService);
      Service var3 = this.addServiceClass("weblogic.messaging.saf.internal.SAFServerService");
      var3.addDependency(safService);
      var3.addSuccessor(CoreServiceGroup.getDeploymentService());
      Service var4 = this.addServiceClass("weblogic.jms.JMSServicePostDeploymentImpl");
      var4.addDependency(CoreServiceGroup.getDeploymentPostAdminServerService());
   }

   public boolean isAvailable() {
      return this.isConfigured();
   }

   static {
      jmsService = new Service(JMSServiceActivator.INSTANCE);
      jmsBridgeService = new Service("weblogic.jms.BridgeService");
      safService = new Service("weblogic.jms.saf.SAFService");
   }
}
