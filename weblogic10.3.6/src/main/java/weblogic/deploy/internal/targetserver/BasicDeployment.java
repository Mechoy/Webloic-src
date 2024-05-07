package weblogic.deploy.internal.targetserver;

import java.io.File;
import java.io.IOException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.List;
import weblogic.application.Deployment;
import weblogic.application.ModuleListener;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.application.utils.EarUtils;
import weblogic.application.utils.TargetUtils;
import weblogic.deploy.common.Debug;
import weblogic.deploy.container.DeploymentContext;
import weblogic.deploy.event.DeploymentEventManager;
import weblogic.deploy.event.VetoableDeploymentEvent;
import weblogic.deploy.internal.TargetHelper;
import weblogic.deploy.internal.targetserver.datamanagement.Data;
import weblogic.deploy.internal.targetserver.datamanagement.DataUpdateRequestInfo;
import weblogic.deploy.internal.targetserver.operations.AbstractOperation;
import weblogic.deploy.internal.targetserver.state.DeploymentState;
import weblogic.deploy.internal.targetserver.state.ListenerFactory;
import weblogic.logging.Loggable;
import weblogic.management.DeploymentException;
import weblogic.management.ManagementException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.deploy.internal.AppRuntimeStateManager;
import weblogic.management.deploy.internal.ApplicationRuntimeState;
import weblogic.management.deploy.internal.DeployerRuntimeLogger;
import weblogic.management.deploy.internal.SlaveDeployerLogger;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public abstract class BasicDeployment {
   protected BasicDeploymentMBean deploymentMBean;
   protected final String name;
   protected final String appId;
   private final File localAppFileOrDir;
   private AppContainerInvoker appctrInvoker;
   protected boolean isStaticDeployment = false;
   private GracefulAdminModeHandler gracefulAdminModeHandler = new GracefulAdminModeHandler();
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   protected static final ServerMBean server;
   protected static final String serverName;
   protected static final ClusterMBean cluster;
   protected final weblogic.application.DeploymentManager deploymentManager = weblogic.application.DeploymentManager.getDeploymentManager();
   protected AbstractOperation task;
   private DeploymentState state;
   private ModuleListener ml;
   private Data localData = null;

   public BasicDeployment(BasicDeploymentMBean var1) {
      this.deploymentMBean = var1;
      this.name = ApplicationVersionUtils.getDisplayName(this.deploymentMBean);
      this.appId = var1.getName();
      this.localData = this.createLocalData();
      this.localAppFileOrDir = this.localData.getSourceFile();
      this.dump();
   }

   public void setStatic() {
      this.isStaticDeployment = true;
   }

   public void dump() {
      if (isDebugEnabled()) {
         debug("Basic Deployment name: " + this.name);
         debug("Staging location: " + this.localAppFileOrDir);
      }

   }

   public String getName() {
      return this.name;
   }

   public abstract void verifyAppVersionSecurity(AbstractOperation var1) throws DeploymentException;

   public void setTask(AbstractOperation var1) {
      if (this.task != null) {
         this.task = var1;
      }

   }

   public AbstractOperation getTask() {
      return this.task;
   }

   public Deployment findDeployment() {
      return this.appctrInvoker;
   }

   public void verifyLocalApp() throws DeploymentException {
      if (!this.localAppFileOrDir.exists()) {
         String var1 = DeployerRuntimeLogger.noAppFilesExist(this.localAppFileOrDir.toString());
         throw new DeploymentException(DeployerRuntimeLogger.logInvalidSourceLoggable(this.localAppFileOrDir.getAbsolutePath(), this.getName(), var1).getMessage());
      }
   }

   public Deployment createDeployment(BasicDeploymentMBean var1, DeploymentState var2) throws DeploymentException {
      if (isDebugEnabled()) {
         debug("Creating Deployment with Path " + this.localAppFileOrDir);
      }

      this.verifyLocalApp();
      this.appctrInvoker = new AppContainerInvoker(this.deploymentManager.createDeployment(var1, this.localAppFileOrDir), var1, var2);
      this.setStateRef(var2);
      return this.appctrInvoker;
   }

   public abstract void removeDeployment();

   public abstract void prepare() throws IOException, DeploymentException;

   private void activate(DeploymentContext var1) throws DeploymentException {
      try {
         Deployment var2 = this.findDeployment();
         if (var2 != null) {
            if (!TargetHelper.isTargetedLocaly(this.deploymentMBean)) {
               if (isDebugEnabled()) {
                  debug("server not targeted");
               }

               return;
            }

            if (isDebugEnabled()) {
               debug("Activate for app=" + this.name);
            }

            if (var1 == null) {
               if (this.task != null && this.task.getDeploymentContext() != null) {
                  var1 = this.task.getDeploymentContext();
               } else {
                  var1 = DeployHelper.createDeploymentContext(this.deploymentMBean);
               }
            }

            var2.activate((DeploymentContext)var1);
         }

      } catch (Throwable var5) {
         DeploymentException var3 = DeployHelper.convertThrowable(var5);
         SlaveDeployerLogger.logSetActivationStateFailedLoggable(this.name, true, var3).log();
         throw (DeploymentException)var3;
      }
   }

   public void gracefulProductionToAdmin(Deployment var1, DeploymentContext var2) throws DeploymentException {
      if (DeployHelper.isActiveState(var1)) {
         AdminModeCallback var3 = this.gracefulAdminModeHandler.create();
         if (var3 != null) {
            if (var2 == null) {
               if (this.task != null && this.task.getDeploymentContext() != null) {
                  var2 = this.task.getDeploymentContext();
               } else {
                  var2 = DeployHelper.createDeploymentContext(this.deploymentMBean);
               }
            }

            ((DeploymentContextImpl)var2).setAdminModeCallback(var3);

            try {
               var1.gracefulProductionToAdmin((DeploymentContext)var2);
               var3.waitForCompletion(0L);
            } finally {
               if (!this.gracefulAdminModeHandler.isInterrupted()) {
                  this.gracefulAdminModeHandler.remove(var3);
               }

            }

         }
      }
   }

   public void forceProductionToAdmin(Deployment var1, long var2, DeploymentContext var4) throws DeploymentException {
      if (DeployHelper.isActiveState(var1) || DeployHelper.isAdminState(var1)) {
         this.gracefulAdminModeHandler.notifyPending();
         if (var2 > 0L) {
            AdminModeCallback var5 = new AdminModeCallback();
            if (var4 == null) {
               if (this.task != null && this.task.getDeploymentContext() != null) {
                  var4 = this.task.getDeploymentContext();
               } else {
                  var4 = DeployHelper.createDeploymentContext(this.deploymentMBean);
               }
            }

            ((DeploymentContextImpl)var4).setAdminModeCallback(var5);
            var1.forceProductionToAdmin((DeploymentContext)var4);
            var5.waitForCompletion(var2);
         } else {
            ((DeploymentContextImpl)var4).setAdminModeCallback(EarUtils.noopAdminModeCallback);
            var1.forceProductionToAdmin((DeploymentContext)var4);
         }

      }
   }

   private void deactivate(DeploymentContext var1) {
      try {
         Deployment var3 = this.findDeployment();
         if (var3 != null && DeployHelper.isAdminState(var3)) {
            if (isDebugEnabled()) {
               debug("Deactivating base deployment " + this.name);
            }

            var3.deactivate(var1);
         }
      } catch (Throwable var5) {
         DeploymentException var2 = DeployHelper.convertThrowable(var5);
         SlaveDeployerLogger.logSetActivationStateFailedLoggable(this.name, false, var2).log();
      }

   }

   public void unprepare() {
      if (isDebugEnabled()) {
         debug(" unpreparing application  - " + this.name);
      }

      Deployment var1 = this.findDeployment();
      if (var1 != null && DeployHelper.isPreparedState(var1)) {
         try {
            DeploymentContextImpl var2;
            if (this.task != null && this.task.getDeploymentContext() != null) {
               var2 = this.task.getDeploymentContext();
            } else {
               var2 = DeployHelper.createDeploymentContext(this.deploymentMBean);
               var2.setStaticDeploymentOperation(true);
            }

            var1.unprepare(var2);
         } catch (Throwable var3) {
            SlaveDeployerLogger.logUnprepareFailed(this.name, DeployHelper.convertThrowable(var3));
         }
      }

      if (isDebugEnabled()) {
         debug(" unprepared application  - " + this.name);
      }

   }

   public void remove() {
      this.remove(true);
   }

   public void remove(boolean var1) {
      try {
         if (var1) {
            boolean var2 = this.removeStagedFiles();
            if (!var2) {
               Loggable var3 = SlaveDeployerLogger.logRemoveStagedFilesFailedLoggable(this.name, this.localAppFileOrDir.toString());
               var3.log();
            }
         }

         if (isDebugEnabled()) {
            debug(" removing basic deployment  - " + this.name);
         }

         this.removeDeployment();
         OrderedDeployments.removeBasicDeployment(this.deploymentMBean);
         if (isDebugEnabled()) {
            debug(" removed basic deployment  - " + this.name);
         }
      } catch (Throwable var4) {
         if (isDebugEnabled()) {
            debug("Unexpected exception while removing a deployment");
            var4.printStackTrace();
         }
      }

   }

   protected static void debug(String var0) {
      Debug.deploymentDebug(var0);
   }

   protected static boolean isDebugEnabled() {
      return Debug.isDeploymentDebugEnabled();
   }

   public boolean isInternalApp() {
      return this.deploymentMBean instanceof AppDeploymentMBean && ((AppDeploymentMBean)this.deploymentMBean).isInternalApp();
   }

   public BasicDeploymentMBean getDeploymentMBean() {
      return this.deploymentMBean;
   }

   public void activateFromServerLifecycle() throws Exception {
      if (!(this.deploymentMBean instanceof AppDeploymentMBean) || DeployHelper.isOkToTransition((AppDeploymentMBean)this.deploymentMBean, server, "STATE_ADMIN")) {
         Deployment var1 = this.findDeployment();
         if (var1 != null) {
            try {
               DeploymentContextImpl var2;
               if (this.task != null && this.task.getDeploymentContext() != null) {
                  var2 = this.task.getDeploymentContext();
               } else {
                  var2 = DeployHelper.createDeploymentContext(this.deploymentMBean);
               }

               var2.setAdminModeTransition(true);
               var2.setStaticDeploymentOperation(true);
               this.startLifecycleStateManager();
               this.activate(var2);
            } catch (Exception var10) {
               try {
                  this.unprepare();
               } catch (Throwable var9) {
               }

               if (this.getState() != null) {
                  this.getState().setCurrentState("STATE_FAILED", true);
               }

               throw var10;
            } finally {
               this.finishLifecycleStateManager();
            }

         }
      }
   }

   public void adminToProductionFromServerLifecycle() throws DeploymentException {
      if (!(this.deploymentMBean instanceof AppDeploymentMBean) || DeployHelper.isOkToTransition((AppDeploymentMBean)this.deploymentMBean, server, "STATE_ACTIVE")) {
         if (isDebugEnabled()) {
            debug("AdminToRunning for app=" + this.name);
         }

         Deployment var1 = this.findDeployment();
         if (var1 != null) {
            if (!TargetHelper.isTargetedLocaly(this.deploymentMBean)) {
               if (isDebugEnabled()) {
                  debug("server not targeted");
               }

            } else {
               DeploymentContextImpl var2;
               if (this.task != null && this.task.getDeploymentContext() != null) {
                  var2 = this.task.getDeploymentContext();
               } else {
                  var2 = DeployHelper.createDeploymentContext(this.deploymentMBean);
               }

               var2.setStaticDeploymentOperation(true);

               try {
                  this.startLifecycleStateManager();
                  var1.adminToProduction(var2);
               } catch (Throwable var13) {
                  try {
                     this.deactivate(var2);
                  } catch (Throwable var12) {
                  }

                  try {
                     this.unprepare();
                  } catch (Throwable var11) {
                  }

                  this.removeDeployment();
                  DeploymentException var4 = DeployHelper.convertThrowable(var13);
                  SlaveDeployerLogger.logTransitionAppFromAdminToRunningFailed(ApplicationVersionUtils.getDisplayName(this.deploymentMBean), var4);
                  throw var4;
               } finally {
                  this.finishLifecycleStateManager();
               }

            }
         }
      }
   }

   public void productionToAdminFromServerLifecycle(boolean var1) throws DeploymentException {
      if (isDebugEnabled()) {
         debug("RunningToAdmin for app=" + this.name + ", graceful=" + var1);
      }

      Deployment var2 = this.findDeployment();
      if (var2 != null) {
         try {
            DeploymentContextImpl var3;
            if (this.task != null && this.task.getDeploymentContext() != null) {
               var3 = this.task.getDeploymentContext();
            } else {
               var3 = DeployHelper.createDeploymentContext(this.deploymentMBean);
            }

            var3.setAdminModeTransition(true);
            var3.setStaticDeploymentOperation(true);
            this.startLifecycleStateManager();
            if (var1) {
               this.gracefulProductionToAdmin(var2, var3);
            } else {
               this.forceProductionToAdmin(var2, 0L, var3);
            }
         } catch (Throwable var9) {
            DeploymentException var4 = DeployHelper.convertThrowable(var9);
            SlaveDeployerLogger.logTransitionAppFromRunningToAdminFailed(ApplicationVersionUtils.getDisplayName(this.deploymentMBean), var4);
            throw var4;
         } finally {
            this.finishLifecycleStateManager();
         }

      }
   }

   public void deactivateFromServerLifecycle() throws Exception {
      Deployment var1 = this.findDeployment();
      if (var1 != null) {
         try {
            DeploymentContextImpl var2;
            if (this.task != null && this.task.getDeploymentContext() != null) {
               var2 = this.task.getDeploymentContext();
            } else {
               var2 = DeployHelper.createDeploymentContext(this.deploymentMBean);
            }

            var2.setAdminModeTransition(true);
            var2.setStaticDeploymentOperation(true);
            this.startLifecycleStateManager();
            this.deactivate(var2);
         } catch (Exception var10) {
            try {
               this.unprepare();
            } catch (Throwable var9) {
            }

            throw var10;
         } finally {
            this.finishLifecycleStateManager();
         }

      }
   }

   protected void startLifecycleStateManager() {
      if (this.deploymentMBean instanceof AppDeploymentMBean) {
         if (!((AppDeploymentMBean)this.deploymentMBean).isInternalApp()) {
            this.setStateRef(new DeploymentState(this.deploymentMBean.getName(), "__Lifecycle_taskid__", 0));
            this.ml = ListenerFactory.createListener(this.getDeploymentMBean(), "__Lifecycle_taskid__", this.state);
            this.deploymentManager.addModuleListener(this.ml);
         }
      }
   }

   protected void finishLifecycleStateManager() {
      if (this.ml != null) {
         this.deploymentManager.removeModuleListener(this.ml);
      }

      this.relayState();
      this.setStateRef((DeploymentState)null);
      this.ml = null;
   }

   protected final void relayState() {
      if (this.state != null) {
         if (isDebugEnabled()) {
            debug("Relaying updated state for app, " + this.state.getId() + " to " + this.state.getCurrentState());
         }

         if (this.state.getTarget() == null) {
            this.state.setTarget(TargetUtils.findLocalTarget(this.deploymentMBean, server).getName());
         }

         if (!ManagementService.getRuntimeAccess(kernelId).isAdminServer()) {
            AppRuntimeStateManager.getManager().updateState(this.state.getId(), this.state);
         }

         DeploymentManager.getInstance().relayStatus(-1L, this.state);
      }
   }

   protected final void setStagingOnRuntimeState(int var1) {
      try {
         AppRuntimeStateManager.getManager().setStagingState(this.deploymentMBean.getName(), new String[]{serverName}, var1, this.isInternalApp());
      } catch (ManagementException var3) {
         var3.printStackTrace();
      }

   }

   public final void relayStagingState(int var1) {
      this.setStagingOnRuntimeState(var1);
      DeploymentState var2 = this.getState();
      if (var2 != null) {
         var2.setStagingState(var1);
         this.relayState();
      }

   }

   public boolean needRetirement() {
      this.gracefulAdminModeHandler.remove();
      if (isDebugEnabled()) {
         debug("needRetirement for " + this.getName() + "=" + !this.gracefulAdminModeHandler.isInterrupted());
      }

      return !this.gracefulAdminModeHandler.isInterrupted();
   }

   public boolean hasPendingGraceful() {
      return this.gracefulAdminModeHandler.hasPending();
   }

   public boolean isGracefulInterrupted() {
      return this.gracefulAdminModeHandler.isInterrupted();
   }

   final File getLocalAppFileOrDir() {
      return this.localAppFileOrDir;
   }

   public void resetMBean(BasicDeploymentMBean var1) {
      this.deploymentMBean = var1;
   }

   public void setStateRef(DeploymentState var1) {
      this.state = var1;
      AppContainerInvoker var2 = (AppContainerInvoker)this.findDeployment();
      if (var2 != null) {
         var2.setStateRef(this.state);
      }

   }

   protected void setState(DeploymentState var1) {
      this.state = var1;
   }

   public DeploymentState getState() {
      return this.state;
   }

   protected void fireVetoableDeploymentEvent() throws Throwable {
      try {
         BasicDeploymentMBean var1 = this.getDeploymentMBean();
         if (var1 != null) {
            DeploymentEventManager.sendVetoableDeploymentEvent(VetoableDeploymentEvent.create(this, VetoableDeploymentEvent.APP_DEPLOY, var1, true, false, (String[])null, TargetHelper.getTargetNames(var1.getTargets())));
         }
      } catch (ManagementException var2) {
         throw ManagementException.unWrapExceptions(var2);
      }
   }

   public final int getStagingState() {
      TargetMBean var1 = TargetUtils.findLocalTarget(this.deploymentMBean, server);
      int var2 = AppRuntimeStateManager.getManager().getStagingState(this.deploymentMBean.getName(), var1.getName());
      if (var1 instanceof ClusterMBean && var2 < 0) {
         if (isDebugEnabled()) {
            debug(" Trying to find Staging State On Application '" + this.deploymentMBean.getName() + "' for target : " + serverName);
         }

         var2 = AppRuntimeStateManager.getManager().getStagingState(this.deploymentMBean.getName(), serverName);
      }

      if (isDebugEnabled()) {
         debug(" Staging State On Application '" + this.deploymentMBean.getName() + "' : " + var2);
      }

      return var2;
   }

   public ApplicationRuntimeState getAppRuntimeState() {
      return AppRuntimeStateManager.getManager().get(this.appId);
   }

   public final void initDataUpdate(DataUpdateRequestInfo var1) {
      weblogic.utils.Debug.assertion(this.localData != null);
      this.localData.initDataUpdate(var1);
   }

   public final void prepareDataUpdate(String var1) throws DeploymentException {
      this.localData.prepareDataUpdate(var1);
   }

   public final void commitDataUpdate() throws DeploymentException {
      this.localData.commitDataUpdate();
   }

   public void cancelDataUpdate(long var1) {
      this.localData.cancelDataUpdate(var1);
   }

   public void closeDataUpdate(long var1, boolean var3) {
      this.localData.closeDataUpdate(var1, var3);
   }

   public abstract void updateDescriptorsPathInfo();

   protected final Data getLocalData() {
      return this.localData;
   }

   protected abstract Data createLocalData();

   protected void stageFilesForStatic() throws DeploymentException {
      this.initDataUpdate(new DataUpdateRequestInfo() {
         public List getDeltaFiles() {
            return new ArrayList();
         }

         public long getRequestId() {
            return 0L;
         }

         public boolean isStatic() {
            return true;
         }

         public boolean isDelete() {
            return false;
         }

         public boolean isPlanUpdate() {
            return false;
         }
      });
      this.prepareDataUpdate((String)null);
      this.commitDataUpdate();
   }

   public boolean isAppStaged() {
      return this.localData.isStagingEnabled();
   }

   public boolean removeStagedFiles() {
      return this.localData.removeStagedFiles();
   }

   public final long getArchiveTimeStamp() {
      if (isDebugEnabled()) {
         debug(" Getting archive timestamp for app " + this.appId);
      }

      ApplicationRuntimeState var1 = AppRuntimeStateManager.getManager().get(this.appId);
      if (var1 != null && var1.getDeploymentVersion() != null) {
         long var2 = var1.getDeploymentVersion().getArchiveTimeStamp();
         if (isDebugEnabled()) {
            debug(" Returning archive timestamp for app " + this.appId + " = " + var2);
         }

         return var2;
      } else {
         return 0L;
      }
   }

   public final long getPlanTimeStamp() {
      if (isDebugEnabled()) {
         debug(" Getting plan timestamp for app " + this.appId);
      }

      ApplicationRuntimeState var1 = AppRuntimeStateManager.getManager().get(this.appId);
      if (var1 != null && var1.getDeploymentVersion() != null) {
         long var2 = var1.getDeploymentVersion().getPlanTimeStamp();
         if (isDebugEnabled()) {
            debug(" Returning plan timestamp for app " + this.appId + " = " + var2);
         }

         return var2;
      } else {
         return 0L;
      }
   }

   static {
      server = ManagementService.getRuntimeAccess(kernelId).getServer();
      serverName = server.getName();
      cluster = server.getCluster();
   }

   private class GracefulAdminModeHandler {
      private AdminModeCallback pending;
      private boolean interrupted;

      private GracefulAdminModeHandler() {
         this.interrupted = false;
      }

      private synchronized AdminModeCallback create() {
         if (this.pending != null) {
            return null;
         } else {
            this.pending = BasicDeployment.this.new AdminModeCallback();
            return this.pending;
         }
      }

      private synchronized void remove(AdminModeCallback var1) {
         if (var1 == this.pending) {
            this.pending = null;
         }

      }

      private synchronized void remove() {
         this.pending = null;
      }

      private synchronized void notifyPending() {
         if (this.pending != null) {
            if (BasicDeployment.isDebugEnabled()) {
               BasicDeployment.debug("GracefulAdminModeHandler.notifyPending for: " + BasicDeployment.this.getName());
            }

            this.interrupted = true;
            this.pending.stop();
         }
      }

      private synchronized boolean hasPending() {
         return this.pending != null;
      }

      private synchronized boolean isInterrupted() {
         return this.interrupted;
      }

      // $FF: synthetic method
      GracefulAdminModeHandler(Object var2) {
         this();
      }
   }

   private class AdminModeCallback implements Deployment.AdminModeCallback {
      private boolean completed;

      private AdminModeCallback() {
         this.completed = false;
      }

      public synchronized void stop() {
         this.notify();
      }

      public synchronized void completed() {
         this.completed = true;
         this.notify();
      }

      public synchronized void waitForCompletion(long var1) {
         if (!this.completed) {
            try {
               this.wait(var1 * 1000L);
               if (BasicDeployment.isDebugEnabled()) {
                  BasicDeployment.debug("AdminModeCallback.waitForCompletion done for: " + BasicDeployment.this.getName());
               }
            } catch (InterruptedException var4) {
               if (BasicDeployment.isDebugEnabled()) {
                  BasicDeployment.debug("AdminModeCallback.waitForCompletion interrupted for: " + BasicDeployment.this.getName());
               }
            }

         }
      }

      // $FF: synthetic method
      AdminModeCallback(Object var2) {
         this();
      }
   }
}
