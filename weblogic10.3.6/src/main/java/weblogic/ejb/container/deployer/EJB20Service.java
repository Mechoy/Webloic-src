package weblogic.ejb.container.deployer;

import java.security.AccessController;
import weblogic.application.ApplicationFactoryManager;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.monitoring.MessageDrivenControlEJBRuntimeMBeanImpl;
import weblogic.ejb.spi.EJBLibraryFactory;
import weblogic.management.ManagementException;
import weblogic.management.configuration.ServerDebugMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.DomainAccess;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.DomainRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.ActivatedService;
import weblogic.server.ServiceFailureException;

public final class EJB20Service extends ActivatedService {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static EJB20Service singleton;
   private ServerMBean serverMBean;
   private Class ejbDebugServiceClass = null;
   private boolean shutdown;

   private void initialize() {
      singleton = this;
      this.serverMBean = ManagementService.getRuntimeAccess(kernelId).getServer();
      this.ejbDebugServiceClass = EJBDebugService.class;
      ApplicationFactoryManager var1 = ApplicationFactoryManager.getApplicationFactoryManager();
      EJBDeploymentFactory var2 = new EJBDeploymentFactory();
      var1.addLibraryFactory(new EJBLibraryFactory());
      var1.addLastDeploymentFactory(var2);
      var1.addModuleFactory(new EJBModuleFactory());
      if (ManagementService.getRuntimeAccess(kernelId).isAdminServer()) {
         try {
            DomainAccess var3 = ManagementService.getDomainAccess(kernelId);
            DomainRuntimeMBean var4 = var3.getDomainRuntime();
            MessageDrivenControlEJBRuntimeMBeanImpl var5 = new MessageDrivenControlEJBRuntimeMBeanImpl();
            var4.setMessageDrivenControlEJBRuntime(var5);
         } catch (ManagementException var6) {
            EJBLogger.logFailedToCreateRuntimeMBeanLoggable(var6);
         }
      }

   }

   public synchronized void haltService() throws ServiceFailureException {
      if (!this.shutdown) {
         this.shutdown = true;
      }
   }

   public synchronized void stopService() throws ServiceFailureException {
      this.haltService();
   }

   public synchronized boolean startService() throws ServiceFailureException {
      this.initialize();
      this.shutdown = false;
      return true;
   }

   static EJB20Service getEJB20Service() {
      return singleton;
   }

   ServerMBean getServer() {
      return this.serverMBean;
   }

   ServerDebugMBean getServerDebug() {
      return this.serverMBean.getServerDebug();
   }
}
