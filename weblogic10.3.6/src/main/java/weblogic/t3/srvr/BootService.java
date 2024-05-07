package weblogic.t3.srvr;

import java.security.AccessController;
import weblogic.version;
import weblogic.kernel.Kernel;
import weblogic.kernel.KernelLogManager;
import weblogic.kernel.T3SrvrLogger;
import weblogic.management.ManagementException;
import weblogic.management.configuration.ConfigurationException;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccessSettable;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;
import weblogic.time.common.internal.TimeEventGenerator;
import weblogic.time.server.TimerMBean;
import weblogic.work.RequestManager;
import weblogic.work.ServerWorkManagerFactory;

public class BootService extends AbstractServerService {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public String getName() {
      return "Kernel";
   }

   public String getVersion() {
      return "Commonj WorkManager v1.1";
   }

   public void start() throws ServiceFailureException {
      try {
         ServerRuntime var1 = ServerRuntime.init();
         ((RuntimeAccessSettable)ManagementService.getRuntimeAccess(kernelId)).setServerRuntime(var1);
         SetUIDRendezvous.initialize();
         ServerMBean var2 = ManagementService.getRuntimeAccess(kernelId).getServer();
         if (!var2.getUse81StyleExecuteQueues()) {
            if (var2.isUseConcurrentQueueForRequestManager()) {
               RequestManager.enableBufferQueue(true);
            }

            ServerWorkManagerFactory.initialize(ManagementService.getRuntimeAccess(kernelId).getServer());
         }

         KernelLogManager.initialize(ManagementService.getRuntimeAccess(kernelId).getServer());
         Kernel.initialize(ManagementService.getRuntimeAccess(kernelId).getServer());
         TimeEventGenerator var3 = TimeEventGenerator.init(T3Srvr.getT3Srvr().getStartupThreadGroup());
         new TimerMBean(var3);
         JVMRuntime.init();
         T3SrvrLogger.logStartupBuildName(version.getVersions(), ManagementService.getRuntimeAccess(kernelId).getServer().getName());
         new ExecutionContext("systemContext");
         new Scavenger();
      } catch (ConfigurationException var4) {
         T3SrvrLogger.logConfigFailure(var4.getMessage());
         throw new ServiceFailureException(var4);
      } catch (ManagementException var5) {
         throw new ServiceFailureException(var5);
      }
   }

   public void stop() {
      this.shutdown();
   }

   public void halt() {
      this.shutdown();
   }

   public void shutdown() {
      T3SrvrLogger.logKernelShutdown();
      Kernel.shutdown();
   }
}
