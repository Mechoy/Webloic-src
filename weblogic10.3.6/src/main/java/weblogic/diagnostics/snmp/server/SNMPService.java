package weblogic.diagnostics.snmp.server;

import java.security.AccessController;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.snmp.agent.SNMPTrapUtil;
import weblogic.diagnostics.snmp.agent.SNMPV3Agent;
import weblogic.diagnostics.snmp.i18n.SNMPLogger;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.SNMPAgentMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public class SNMPService extends AbstractServerService {
   static final DebugLogger DEBUG = DebugLogger.getDebugLogger("DebugSNMPAgent");
   private String serverName;

   public void start() throws ServiceFailureException {
      if (DEBUG.isDebugEnabled()) {
         DEBUG.debug("Starting SNMP Service");
      }

      SNMPAgentDeploymentHandler var1 = SNMPAgentDeploymentHandler.getInstance();
      boolean var2 = false;
      AuthenticatedSubject var3 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      RuntimeAccess var4 = ManagementService.getRuntimeAccess(var3);
      DomainMBean var5 = var4.getDomain();
      var2 = var4.isAdminServer();
      this.serverName = var4.getServerName();
      if (var2) {
         SNMPAgentMBean var6 = var5.getSNMPAgent();
         var1.setDomainAgentConfig(var6);
      } else {
         SNMPTrapUtil.getInstance().setSNMPTrapSender(new SNMPAdminServerTrapSender());
      }

      try {
         var1.setSNMPServiceStarted(true);
         var1.activateSNMPAgent();
         var1.ensureSNMPAgentRuntimeInitialized();
      } catch (Exception var7) {
         SNMPLogger.logSNMPServiceFailure(var7);
      }

   }

   public void stop() throws ServiceFailureException {
      if (DEBUG.isDebugEnabled()) {
         DEBUG.debug("Stopping SNMPService");
      }

      SNMPAgentDeploymentHandler var1 = SNMPAgentDeploymentHandler.getInstance();

      try {
         SNMPV3Agent var2 = var1.getSNMPAgent();
         if (var2 != null) {
            ServerStateTrapUtil.sendServerLifecycleNotification(var2, this.serverName, "wlsServerShutDown");
         }
      } catch (Exception var3) {
      }

      var1.setSNMPServiceStarted(false);
   }

   public void halt() throws ServiceFailureException {
      if (DEBUG.isDebugEnabled()) {
         DEBUG.debug("Halting SNMPService");
      }

      this.stop();
   }
}
