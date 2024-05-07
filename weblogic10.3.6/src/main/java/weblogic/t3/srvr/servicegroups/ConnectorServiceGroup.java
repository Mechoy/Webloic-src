package weblogic.t3.srvr.servicegroups;

import weblogic.connector.common.ConnectorServiceActivator;
import weblogic.server.servicegraph.Service;
import weblogic.server.servicegraph.ServiceGroup;

public class ConnectorServiceGroup extends ServiceGroup {
   static final Service connectorService;

   public static Service getConnectorService() {
      return connectorService;
   }

   public ConnectorServiceGroup(boolean var1) {
      super(var1);
      this.addService(connectorService);
      connectorService.addDependency(CoreServiceGroup.getTransactionService());
      connectorService.addSuccessor(CoreServiceGroup.getStandbyState());
      connectorService.addSuccessor(CoreServiceGroup.getDeploymentService());
   }

   public boolean isAvailable() {
      return this.isConfigured();
   }

   static {
      connectorService = new Service(ConnectorServiceActivator.INSTANCE);
   }
}
