package weblogic.deploy.service.internal.adminserver;

import java.util.HashMap;
import java.util.Map;
import weblogic.deploy.common.Debug;
import weblogic.deploy.service.DeploymentServiceCallbackHandler;
import weblogic.deploy.service.RegistrationExistsException;
import weblogic.deploy.service.Version;
import weblogic.deploy.service.internal.DeploymentServiceLogger;
import weblogic.deploy.service.internal.DomainVersion;

public final class AdminDeploymentsManager {
   private Map registeredCallbackHandlers;
   private DomainVersion currentDomainVersion;

   private AdminDeploymentsManager() {
      this.registeredCallbackHandlers = null;
      this.registeredCallbackHandlers = new HashMap();
      this.currentDomainVersion = new DomainVersion();
   }

   public static AdminDeploymentsManager getInstance() {
      return AdminDeploymentsManager.Maker.SINGLETON;
   }

   public void registerCallbackHandler(Version var1, DeploymentServiceCallbackHandler var2) throws RegistrationExistsException {
      String var3 = var2.getHandlerIdentity();
      synchronized(this.registeredCallbackHandlers) {
         if (this.registeredCallbackHandlers.get(var3) != null) {
            throw new RegistrationExistsException(DeploymentServiceLogger.logCallbackAlreadyRegisteredLoggable(var3).getMessage());
         }

         this.registeredCallbackHandlers.put(var3, new DeploymentServiceCallbackDeliverer(var2));
         this.currentDomainVersion.addOrUpdateDeploymentVersion(var3, var1);
      }

      this.dumpActiveUpdatesList();
      if (Debug.isServiceDebugEnabled()) {
         Debug.serviceDebug("Current Version updated to: " + this.currentDomainVersion.toString());
      }

   }

   public DeploymentServiceCallbackHandler getCallbackHandler(String var1) {
      synchronized(this.registeredCallbackHandlers) {
         return (DeploymentServiceCallbackHandler)this.registeredCallbackHandlers.get(var1);
      }
   }

   public void unregisterCallbackHandler(String var1) {
      if (Debug.isServiceDebugEnabled()) {
         Debug.serviceDebug("unregistering DeploymentInfo for '" + var1 + "'");
      }

      synchronized(this.registeredCallbackHandlers) {
         this.registeredCallbackHandlers.remove(var1);
      }

      this.currentDomainVersion.removeDeploymentVersion(var1);
   }

   public synchronized DomainVersion getCurrentDomainVersion() {
      return this.currentDomainVersion;
   }

   public synchronized void setCurrentDomainVersion(DomainVersion var1) {
      this.currentDomainVersion = var1;
      if (Debug.isServiceDebugEnabled()) {
         Debug.serviceDebug("setting current domain version on admin to '" + this.currentDomainVersion.toString() + "'");
      }

   }

   private void dumpActiveUpdatesList() {
      synchronized(this.registeredCallbackHandlers) {
         if (this.registeredCallbackHandlers.size() > 0 && Debug.isServiceDebugEnabled()) {
            Debug.serviceDebug("registered DeploymentServiceCallbackHandlers : " + this.registeredCallbackHandlers);
         }

      }
   }

   // $FF: synthetic method
   AdminDeploymentsManager(Object var1) {
      this();
   }

   static class Maker {
      static final AdminDeploymentsManager SINGLETON = new AdminDeploymentsManager();
   }
}
