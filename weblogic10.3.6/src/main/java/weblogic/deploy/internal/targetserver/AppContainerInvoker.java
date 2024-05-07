package weblogic.deploy.internal.targetserver;

import weblogic.application.ApplicationContext;
import weblogic.application.Deployment;
import weblogic.application.ModuleListener;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.deploy.common.Debug;
import weblogic.deploy.compatibility.NotificationBroadcaster;
import weblogic.deploy.container.DeploymentContext;
import weblogic.deploy.internal.targetserver.state.DeploymentState;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.deploy.internal.AppRuntimeStateManager;
import weblogic.utils.StackTraceUtils;

public class AppContainerInvoker implements Deployment {
   private final Deployment delegate;
   private DeploymentState state;
   private final String id;
   private static final AppRuntimeStateManager appRTStateMgr = AppRuntimeStateManager.getManager();

   AppContainerInvoker(Deployment var1, BasicDeploymentMBean var2, DeploymentState var3) {
      this.delegate = var1;
      this.state = var3;
      this.id = var2.getName();
   }

   private void debug(String var1) {
      Debug.deploymentDebug(var1);
   }

   private boolean isDebugEnabled() {
      return Debug.isDeploymentDebugEnabled();
   }

   public void setStateRef(DeploymentState var1) {
      this.state = var1;
   }

   public Deployment getDelegate() {
      return this.delegate;
   }

   public void prepare(DeploymentContext var1) throws DeploymentException {
      this.sendJMXNotification("preparing");

      try {
         if (this.isDebugEnabled()) {
            this.debug("Calling app container 'prepare' for '" + this.id + "'");
         }

         this.delegate.prepare(var1);
         if (this.state != null) {
            this.state.setCurrentState(ModuleListener.STATE_PREPARED.toString());
         }
      } catch (Throwable var3) {
         this.sendJMXNotification("failed");
         throw this.getOrCreateDeploymentException(var3);
      }

      this.sendJMXNotification("prepared");
   }

   public void activate(DeploymentContext var1) throws DeploymentException {
      this.sendJMXNotification("activating");

      try {
         if (this.isDebugEnabled()) {
            this.debug("Calling app container 'activate' for '" + this.id + "'");
         }

         this.delegate.activate(var1);
         if (this.state != null) {
            this.state.setCurrentState(ModuleListener.STATE_ADMIN.toString());
         }

      } catch (Throwable var3) {
         this.sendJMXNotification("failed");
         throw this.getOrCreateDeploymentException(var3);
      }
   }

   public void deactivate(DeploymentContext var1) throws DeploymentException {
      this.sendJMXNotification("deactivating");

      try {
         if (this.isDebugEnabled()) {
            this.debug("Calling app container 'deactivate' for '" + this.id + "'");
         }

         this.delegate.deactivate(var1);
         if (this.state != null) {
            this.state.setCurrentState(ModuleListener.STATE_PREPARED.toString());
         }
      } catch (Throwable var3) {
         this.sendJMXNotification("failed");
         throw this.getOrCreateDeploymentException(var3);
      }

      this.sendJMXNotification("deactivated");
   }

   public void unprepare(DeploymentContext var1) throws DeploymentException {
      this.sendJMXNotification("unpreparing");

      try {
         if (this.isDebugEnabled()) {
            this.debug("Calling app container 'unprepare' for '" + this.id + "'");
         }

         this.delegate.unprepare(var1);
         if (this.state != null) {
            this.state.setCurrentState(ModuleListener.STATE_NEW.toString());
         }
      } catch (Throwable var3) {
         this.sendJMXNotification("failed");
         throw this.getOrCreateDeploymentException(var3);
      }

      this.sendJMXNotification("unprepared");
   }

   public void remove(DeploymentContext var1) throws DeploymentException {
      if (this.isDebugEnabled()) {
         this.debug("Calling app container 'remove' for '" + this.id + "'");
      }

      try {
         this.delegate.remove(var1);
      } catch (Throwable var3) {
         throw this.getOrCreateDeploymentException(var3);
      }
   }

   public void prepareUpdate(DeploymentContext var1) throws DeploymentException {
      if (this.isDebugEnabled()) {
         this.debug("Calling app container 'prepareUpdate' for '" + this.id + "'");
      }

      try {
         this.delegate.prepareUpdate(var1);
         if (this.state != null) {
            this.state.setCurrentState(ModuleListener.STATE_UPDATE_PENDING.toString());
         }

      } catch (Throwable var3) {
         throw this.getOrCreateDeploymentException(var3);
      }
   }

   public void activateUpdate(DeploymentContext var1) throws DeploymentException {
      if (this.isDebugEnabled()) {
         this.debug("Calling app container 'activateUpdate' for '" + this.id + "'");
      }

      try {
         this.delegate.activateUpdate(var1);
         if (this.state != null) {
            this.state.setCurrentState(ModuleListener.STATE_ACTIVE.toString());
         }

      } catch (Throwable var3) {
         throw this.getOrCreateDeploymentException(var3);
      }
   }

   public void rollbackUpdate(DeploymentContext var1) {
      if (this.isDebugEnabled()) {
         this.debug("Calling app container 'rollbackUpdate' for '" + this.id + "'");
      }

      try {
         this.delegate.rollbackUpdate(var1);
         if (this.state != null) {
            this.state.setCurrentState(ModuleListener.STATE_ACTIVE.toString());
         }
      } catch (Throwable var3) {
         if (this.isDebugEnabled()) {
            this.debug("App container 'rollbackUpdate' for id '" + this.id + "' failed - " + StackTraceUtils.throwable2StackTrace(var3));
         }
      }

   }

   public void adminToProduction(DeploymentContext var1) throws DeploymentException {
      try {
         if (this.isDebugEnabled()) {
            this.debug("Calling app container 'adminToProd' for '" + this.id + "'");
         }

         this.delegate.adminToProduction(var1);
         if (this.state != null) {
            this.state.setCurrentState(ModuleListener.STATE_ACTIVE.toString());
         }

         this.setActiveAppVersionIfNeeded();
      } catch (Throwable var3) {
         this.sendJMXNotification("failed");
         throw this.getOrCreateDeploymentException(var3);
      }

      this.sendJMXNotification("activated");
   }

   public void gracefulProductionToAdmin(DeploymentContext var1) throws DeploymentException {
      if (this.isDebugEnabled()) {
         this.debug("Calling app container 'graceful prodToAdmin' for '" + this.id + "'");
      }

      try {
         this.delegate.gracefulProductionToAdmin(var1);
         if (this.state != null) {
            this.state.setCurrentState(ModuleListener.STATE_ADMIN.toString());
         }

      } catch (Throwable var3) {
         throw this.getOrCreateDeploymentException(var3);
      }
   }

   public void forceProductionToAdmin(DeploymentContext var1) throws DeploymentException {
      if (this.isDebugEnabled()) {
         this.debug("Calling app container 'force prodToAdmin' for '" + this.id + "'");
      }

      try {
         this.delegate.forceProductionToAdmin(var1);
         if (this.state != null) {
            this.state.setCurrentState(ModuleListener.STATE_ADMIN.toString());
         }

      } catch (Throwable var3) {
         throw this.getOrCreateDeploymentException(var3);
      }
   }

   public void stop(DeploymentContext var1) throws DeploymentException {
      if (this.isDebugEnabled()) {
         this.debug("Calling app container 'stop' for '" + this.id + "'");
      }

      try {
         this.delegate.stop(var1);
      } catch (Throwable var3) {
         throw this.getOrCreateDeploymentException(var3);
      }
   }

   public void start(DeploymentContext var1) throws DeploymentException {
      if (this.isDebugEnabled()) {
         this.debug("Calling app container 'start' for '" + this.id + "'");
      }

      try {
         this.delegate.start(var1);
      } catch (Throwable var3) {
         throw this.getOrCreateDeploymentException(var3);
      }
   }

   public ApplicationContext getApplicationContext() {
      return this.delegate.getApplicationContext();
   }

   private void sendJMXNotification(String var1) {
      if (this.state != null) {
         NotificationBroadcaster.sendAppNotification(var1, this.id, (String)null);
         this.state.addAppXition(var1);
      }

   }

   private void setActiveAppVersionIfNeeded() {
      if (ApplicationVersionUtils.getVersionId(this.id) != null) {
         try {
            appRTStateMgr.setActiveVersion(this.id);
         } catch (Exception var2) {
         }

      }
   }

   private DeploymentException getOrCreateDeploymentException(Throwable var1) {
      return var1 instanceof DeploymentException ? (DeploymentException)var1 : new DeploymentException("Unexpected error encountered", var1);
   }
}
