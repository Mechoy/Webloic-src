package weblogic.deploy.service.internal.targetserver;

import java.security.AccessController;
import java.util.HashMap;
import java.util.Map;
import weblogic.deploy.common.Debug;
import weblogic.deploy.service.DeploymentReceiver;
import weblogic.deploy.service.RegistrationExistsException;
import weblogic.deploy.service.Version;
import weblogic.deploy.service.internal.DeploymentServiceLogger;
import weblogic.deploy.service.internal.DomainVersion;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.StackTraceUtils;

public final class TargetDeploymentsManager {
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private final Map registeredCallbackHandlers;
   private DomainVersion currentDomainVersion;
   private String localServerName;
   private ServerRuntimeMBean serverBean;

   private TargetDeploymentsManager() {
      this.registeredCallbackHandlers = new HashMap();
      this.currentDomainVersion = new DomainVersion();
   }

   public static TargetDeploymentsManager getInstance() {
      return TargetDeploymentsManager.Maker.SINGLETON;
   }

   public final synchronized void registerCallbackHandler(Version var1, DeploymentReceiver var2) throws RegistrationExistsException {
      String var3 = var2.getHandlerIdentity();
      if (this.registeredCallbackHandlers.get(var3) == null) {
         this.registeredCallbackHandlers.put(var3, new DeploymentReceiverCallbackDeliverer(var2));
         this.currentDomainVersion.addOrUpdateDeploymentVersion(var3, var1);
         this.dumpActiveUpdatesList();
      } else {
         throw new RegistrationExistsException(DeploymentServiceLogger.logCallbackAlreadyRegisteredLoggable(var3).getMessage());
      }
   }

   public final synchronized DeploymentReceiver getDeploymentReceiver(String var1) {
      return (DeploymentReceiver)this.registeredCallbackHandlers.get(var1);
   }

   public final synchronized void unregisterCallbackHandler(String var1) {
      if (Debug.isServiceDebugEnabled()) {
         Debug.serviceDebug("Unregistering DeploymentReceiver callback  handler for " + var1 + " from the target " + "DeploymentService");
      }

      this.registeredCallbackHandlers.remove(var1);
      this.currentDomainVersion.removeDeploymentVersion(var1);
   }

   public final synchronized DomainVersion getCurrentDomainVersion() {
      return this.currentDomainVersion;
   }

   public final synchronized void setCurrentDomainVersion(DomainVersion var1) {
      if (var1 == null) {
         if (Debug.isServiceDebugEnabled()) {
            String var2 = "Attempt to set the current domain version to 'null' ";
            Debug.serviceDebug(var2 + StackTraceUtils.throwable2StackTrace(new Exception(var2)));
         }

      } else {
         if (Debug.isServiceDebugEnabled()) {
            Debug.serviceDebug("Current domain version being set to: " + var1.toString());
         }

         this.currentDomainVersion = var1;
      }
   }

   public final String getLocalServerName() {
      if (this.localServerName == null) {
         this.localServerName = ManagementService.getRuntimeAccess(kernelId).getServerName();
      }

      return this.localServerName;
   }

   private final ServerRuntimeMBean getServerBean() {
      if (this.serverBean == null) {
         this.serverBean = ManagementService.getRuntimeAccess(kernelId).getServerRuntime();
      }

      return this.serverBean;
   }

   public final synchronized boolean restartPending() {
      return this.getServerBean().isRestartRequired();
   }

   private synchronized void dumpActiveUpdatesList() {
      if (this.registeredCallbackHandlers.size() > 0 && Debug.isServiceDebugEnabled()) {
         Debug.serviceDebug("Active DeploymentInfos on target : " + this.registeredCallbackHandlers);
      }

   }

   // $FF: synthetic method
   TargetDeploymentsManager(Object var1) {
      this();
   }

   static class Maker {
      static final TargetDeploymentsManager SINGLETON = new TargetDeploymentsManager();
   }
}
