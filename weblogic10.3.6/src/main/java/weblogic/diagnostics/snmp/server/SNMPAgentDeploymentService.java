package weblogic.diagnostics.snmp.server;

import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.internal.DeploymentHandlerHome;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public class SNMPAgentDeploymentService extends AbstractServerService {
   private DebugLogger DEBUG;

   public SNMPAgentDeploymentService() {
      this.DEBUG = SNMPService.DEBUG;
   }

   public void start() throws ServiceFailureException {
      if (this.DEBUG.isDebugEnabled()) {
         this.DEBUG.debug("Starting SNMPAgentDeploymentService");
      }

      DeploymentHandlerHome.addDeploymentHandler(SNMPAgentDeploymentHandler.getInstance());
   }
}
