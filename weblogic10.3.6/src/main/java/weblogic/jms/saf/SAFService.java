package weblogic.jms.saf;

import java.security.AccessController;
import weblogic.health.HealthMonitorService;
import weblogic.jms.JMSService;
import weblogic.jms.backend.BackEnd;
import weblogic.management.ManagementException;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.messaging.common.PrivilegedActionUtilities;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public class SAFService extends AbstractServerService {
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private final SAFAgentDeployer safDeployer = new SAFAgentDeployer();
   private static SAFService singleton;
   private SAFRuntimeMBeanImpl runtimeMBean;

   public SAFService() throws ManagementException {
      singleton = this;
      RuntimeAccess var1 = ManagementService.getRuntimeAccess(KERNEL_ID);
      String var2 = var1.getServerName() + ".saf";
      this.runtimeMBean = new SAFRuntimeMBeanImpl(var2);
      var1.getServerRuntime().setSAFRuntime(this.runtimeMBean);
      SAFOutgoingReplyHandler.init();
   }

   public static SAFService getSAFService() {
      return singleton;
   }

   public SAFRuntimeMBeanImpl getRuntimeMBean() {
      return this.runtimeMBean;
   }

   public SAFAgentDeployer getDeployer() {
      return this.safDeployer;
   }

   public void start() throws ServiceFailureException {
      HealthMonitorService.register(this.runtimeMBean.getName(), this.runtimeMBean, false);
      this.safDeployer.start();
   }

   public void stop() throws ServiceFailureException {
      this.safDeployer.stop();

      try {
         PrivilegedActionUtilities.unregister(this.runtimeMBean, KERNEL_ID);
      } catch (ManagementException var2) {
         throw new ServiceFailureException(var2);
      }
   }

   public void halt() throws ServiceFailureException {
      this.stop();
   }

   static BackEnd getBackEnd(String var0) {
      return JMSService.getJMSService().getBEDeployer().findBackEnd(var0);
   }
}
