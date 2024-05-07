package weblogic.server;

import java.security.AccessController;
import weblogic.kernel.T3SrvrLogger;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.ServerLifeCycleRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;

public class RemoteLifeCycleOperationsImpl implements RemoteLifeCycleOperations {
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final DebugCategory debugSLC = Debug.getCategory("weblogic.slc");

   public static RemoteLifeCycleOperationsImpl getInstance() {
      return RemoteLifeCycleOperationsImpl.Singleton.SINGLETON;
   }

   private RemoteLifeCycleOperationsImpl() {
   }

   public void shutdown() throws ServerLifecycleException {
      debug("executing shutdown(). Requested by '" + this.getUserName() + "'");
      ManagementService.getRuntimeAccess(kernelId).getServerRuntime().shutdown();
   }

   public void shutdown(int var1, boolean var2) throws ServerLifecycleException {
      debug("executing shutdown(timeout). Requested by '" + this.getUserName() + "'");
      ManagementService.getRuntimeAccess(kernelId).getServerRuntime().shutdown(var1, var2);
   }

   public void forceShutdown() throws ServerLifecycleException {
      debug("executing forceShutdown(). Requested by '" + this.getUserName() + "'");
      ManagementService.getRuntimeAccess(kernelId).getServerRuntime().forceShutdown();
   }

   public void suspend() throws ServerLifecycleException {
      debug("executing suspend(). Requested by '" + this.getUserName() + "'");
      ManagementService.getRuntimeAccess(kernelId).getServerRuntime().suspend();
   }

   public void suspend(int var1, boolean var2) throws ServerLifecycleException {
      debug("executing suspend(timeout). Requested by '" + this.getUserName() + "'");
      ManagementService.getRuntimeAccess(kernelId).getServerRuntime().suspend(var1, var2);
   }

   public void forceSuspend() throws ServerLifecycleException {
      debug("executing forceSuspend(). Requested by '" + this.getUserName() + "'");
      ManagementService.getRuntimeAccess(kernelId).getServerRuntime().forceSuspend();
   }

   public void resume() throws ServerLifecycleException {
      debug("executing resume(). Requested by '" + this.getUserName() + "'");
      ManagementService.getRuntimeAccess(kernelId).getServerRuntime().resume();
   }

   public String getState() {
      debug("executing getState(). Requested by '" + this.getUserName() + "'");
      return ManagementService.getRuntimeAccess(kernelId).getServerRuntime().getState();
   }

   public void setState(String var1, String var2) {
      if (ServerLifeCycleService.isStarted()) {
         ServerLifeCycleRuntimeMBean var3 = ManagementService.getDomainAccess(kernelId).getDomainRuntime().lookupServerLifeCycleRuntime(var1);
         debug("got slcRuntime '" + var3 + "' for serverName '" + var1 + "'. Updating state to " + var2);
         if (var3 == null) {
            debug("slcRuntime is null, cannot update state to " + var2);
         } else {
            var3.setState(var2);
         }
      }
   }

   public String getWeblogicHome() {
      debug("executing getWeblogicHome(). Requested by '" + this.getUserName() + "'");
      return ManagementService.getRuntimeAccess(kernelId).getServerRuntime().getWeblogicHome();
   }

   public String getMiddlewareHome() {
      debug("executing getMiddlewareHome(). Requested by '" + this.getUserName() + "'");
      return ManagementService.getRuntimeAccess(kernelId).getServerRuntime().getMiddlewareHome();
   }

   private static void debug(String var0) {
      if (debugSLC.isEnabled()) {
         T3SrvrLogger.logDebugSLC("<RemoteSLCOperationsImpl>" + var0);
      }

   }

   private AuthenticatedSubject getUserName() {
      return debugSLC.isEnabled() ? SecurityServiceManager.getCurrentSubject(kernelId) : null;
   }

   // $FF: synthetic method
   RemoteLifeCycleOperationsImpl(Object var1) {
      this();
   }

   private static class Singleton {
      private static final RemoteLifeCycleOperationsImpl SINGLETON = new RemoteLifeCycleOperationsImpl();
   }
}
