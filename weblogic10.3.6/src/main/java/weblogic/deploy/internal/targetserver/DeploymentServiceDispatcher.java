package weblogic.deploy.internal.targetserver;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import weblogic.deploy.common.Debug;
import weblogic.deploy.internal.Deployment;
import weblogic.deploy.service.DeploymentContext;
import weblogic.deploy.service.DeploymentReceiver;
import weblogic.deploy.service.Version;
import weblogic.deploy.service.internal.DeploymentService;
import weblogic.management.ManagementException;
import weblogic.management.deploy.internal.AppRuntimeStateManager;
import weblogic.management.provider.MSIService;

public final class DeploymentServiceDispatcher implements DeploymentReceiver {
   private String identity;
   private final DeploymentService service;
   private DeploymentManager deploymentManager;

   private DeploymentServiceDispatcher() {
      this.service = DeploymentService.getDeploymentService();
   }

   public static DeploymentServiceDispatcher getInstance() {
      return DeploymentServiceDispatcher.Maker.instance;
   }

   private void debug(String var1) {
      Debug.deploymentDebug(var1);
   }

   public void initialize(String var1, Version var2, DeploymentManager var3) {
      this.identity = var1;
      this.deploymentManager = var3;

      try {
         DeploymentContext var4 = this.service.registerHandler(var2, this);
         this.synchStateWithAdmin(var4, var1);
      } catch (Throwable var7) {
         if (Debug.isDeploymentDebugEnabled()) {
            this.debug("Error registering DeploymentReceiver for '" + var1 + "' with DeploymentService due to " + var7);
         }

         MSIService.getMSIService().setAdminServerAvailable(false);

         try {
            AppRuntimeStateManager.getManager().loadStartupState((Map)null);
         } catch (ManagementException var6) {
            var6.printStackTrace();
         }
      }

   }

   private void synchStateWithAdmin(DeploymentContext var1, String var2) throws ManagementException {
      Iterator var3 = var1.getDeploymentRequest().getDeployments(var2);
      boolean var4 = false;

      while(var3.hasNext()) {
         Deployment var5 = (Deployment)var3.next();
         if (var5 != null && var5.isSyncWithAdmin()) {
            Map var6 = var5.getSyncWithAdminState();
            if (var6 != null) {
               AppRuntimeStateManager.getManager().loadStartupState(var6);
               var4 = true;
            }
         }
      }

      if (!var4) {
         AppRuntimeStateManager.getManager().loadStartupState((Map)null);
      }

   }

   public final void shutdown() {
      this.service.unregisterHandler(this.getHandlerIdentity());
   }

   public final void notifyContextUpdated(long var1) {
      this.service.notifyContextUpdated(var1, this.getHandlerIdentity());
   }

   public final void notifyContextUpdateFailed(long var1, Throwable var3) {
      this.service.notifyContextUpdateFailed(var1, this.getHandlerIdentity(), var3);
   }

   public final void notifyPrepareSuccess(long var1) {
      this.service.notifyPrepareSuccess(var1, this.getHandlerIdentity());
   }

   public final void notifyPrepareFailure(long var1, Throwable var3) {
      this.service.notifyPrepareFailure(var1, this.getHandlerIdentity(), var3);
   }

   public final void notifyCommitSuccess(long var1) {
      this.service.notifyCommitSuccess(var1, this.getHandlerIdentity());
   }

   public final void notifyCommitFailure(long var1, Throwable var3) {
      this.service.notifyCommitFailure(var1, this.getHandlerIdentity(), var3);
   }

   public final void notifyCancelSuccess(long var1) {
      this.service.notifyCancelSuccess(var1, this.getHandlerIdentity());
   }

   public final void notifyCancelFailure(long var1, Throwable var3) {
      this.service.notifyCancelFailure(var1, this.getHandlerIdentity(), var3);
   }

   public final void notifyStatusUpdate(long var1, Serializable var3) {
      this.service.notifyStatusUpdate(var1, this.getHandlerIdentity(), var3);
   }

   public final String getHandlerIdentity() {
      return this.identity;
   }

   public void updateDeploymentContext(DeploymentContext var1) {
      this.deploymentManager.handleUpdateDeploymentContext(var1);
   }

   public final void prepare(DeploymentContext var1) {
      this.deploymentManager.handlePrepare(var1);
   }

   public final void commit(DeploymentContext var1) {
      this.deploymentManager.handleCommit(var1);
   }

   public final void cancel(DeploymentContext var1) {
      this.deploymentManager.handleCancel(var1);
   }

   public final void prepareCompleted(DeploymentContext var1, String var2) {
      if (var2.equals("Configuration")) {
         this.deploymentManager.configPrepareCompleted(var1);
      }

   }

   public final void commitCompleted(DeploymentContext var1, String var2) {
      if (var2.equals("Configuration")) {
         this.deploymentManager.configCommitCompleted(var1);
      }

   }

   // $FF: synthetic method
   DeploymentServiceDispatcher(Object var1) {
      this();
   }

   static class Maker {
      static final DeploymentServiceDispatcher instance = new DeploymentServiceDispatcher();
   }
}
