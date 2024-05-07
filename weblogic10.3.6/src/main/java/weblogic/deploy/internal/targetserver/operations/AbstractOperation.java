package weblogic.deploy.internal.targetserver.operations;

import java.io.IOException;
import java.security.AccessController;
import java.util.Collections;
import java.util.Set;
import weblogic.application.Deployment;
import weblogic.application.ModuleListener;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.application.utils.TargetUtils;
import weblogic.deploy.beans.factory.InvalidTargetException;
import weblogic.deploy.common.Debug;
import weblogic.deploy.container.NonFatalDeploymentException;
import weblogic.deploy.event.BaseDeploymentEvent;
import weblogic.deploy.event.DeploymentEvent;
import weblogic.deploy.event.DeploymentEventManager;
import weblogic.deploy.event.VetoableDeploymentEvent;
import weblogic.deploy.internal.InternalDeploymentData;
import weblogic.deploy.internal.TargetHelper;
import weblogic.deploy.internal.targetserver.BasicDeployment;
import weblogic.deploy.internal.targetserver.DeployHelper;
import weblogic.deploy.internal.targetserver.DeploymentContextImpl;
import weblogic.deploy.internal.targetserver.DeploymentManager;
import weblogic.deploy.internal.targetserver.OrderedDeployments;
import weblogic.deploy.internal.targetserver.state.DeploymentState;
import weblogic.deploy.internal.targetserver.state.ListenerFactory;
import weblogic.logging.Loggable;
import weblogic.management.DeploymentException;
import weblogic.management.ManagementException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.ConfigurationMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.SubDeploymentMBean;
import weblogic.management.configuration.SystemResourceMBean;
import weblogic.management.configuration.TargetInfoMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.deploy.DeploymentData;
import weblogic.management.deploy.internal.AppRuntimeStateManager;
import weblogic.management.deploy.internal.DeployerRuntimeLogger;
import weblogic.management.deploy.internal.DeploymentServerService;
import weblogic.management.deploy.internal.SlaveDeployerLogger;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public abstract class AbstractOperation {
   private boolean completed;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   protected static final ServerMBean server;
   private ModuleListener ml;
   protected static final String serverName;
   protected static final ClusterMBean cluster;
   protected static final DeploymentManager deploymentManager;
   protected final long requestId;
   protected final String taskId;
   protected final InternalDeploymentData internalDeploymentData;
   protected final DeploymentData deploymentData;
   protected final BasicDeployment app;
   protected int operation;
   protected Deployment appcontainer;
   protected final BasicDeploymentMBean mbean;
   protected DomainMBean proposedDomain;
   private static weblogic.application.DeploymentManager appctrManager;
   private DeploymentState state;
   protected AuthenticatedSubject initiator;
   protected DeploymentContextImpl deploymentContext;
   protected boolean requiresRestart = false;
   protected boolean controlOperation = false;

   protected AbstractOperation(long var1, String var3, InternalDeploymentData var4, BasicDeploymentMBean var5, DomainMBean var6, AuthenticatedSubject var7, boolean var8) throws DeploymentException {
      this.requestId = var1;
      this.taskId = var3;
      if (var5 == null) {
         DeploymentException var11 = new DeploymentException("Attempt to create " + this.getClass().getName() + " with a null BasicDeploymentMBean. RequestId:'" + var1 + "' and taskId: '" + var3 + "'");
         this.complete(2, var11);
         throw var11;
      } else if (!DeploymentServerService.isStarted()) {
         Loggable var9 = DeployerRuntimeLogger.logDeploymentServiceNotStartedLoggable(ApplicationVersionUtils.getDisplayName(var5), var3);
         DeploymentException var10 = new DeploymentException(var9.getMessage());
         this.complete(2, var10);
         throw var10;
      } else {
         this.internalDeploymentData = var4;
         this.mbean = var5;
         this.deploymentData = var4.getExternalDeploymentData();
         this.proposedDomain = var6;
         this.initiator = var7;
         this.requiresRestart = var8;
         if (this.deploymentContext == null) {
            this.deploymentContext = new DeploymentContextImpl(var7);
            this.deploymentContext.setProposedDomain(var6);
            this.deploymentContext.setRequiresRestart(var8);
         }

         this.deploymentContext.setUserSuppliedTargets(this.getTargets());
         this.app = OrderedDeployments.getOrCreateBasicDeployment(this.mbean);
         if (this.app != null) {
            this.app.setTask(this);
            this.app.resetMBean(this.mbean);
            this.deploymentContext.setAppStaged(this.app.isAppStaged());
         } else if (this.isDebugEnabled()) {
            this.debug("BasicDeployment is null for " + var5.getName());
         }

      }
   }

   private void setTargetsFromConfig() {
      if (!this.deploymentData.isTargetsFromConfig()) {
         this.deploymentData.setTargetsFromConfig(this.deploymentData.getAllModuleTargets().isEmpty() && this.deploymentData.getAllSubModuleTargets().isEmpty());
      }

   }

   protected DeploymentState getState() {
      return this.state;
   }

   public DomainMBean getProposedDomain() {
      return this.proposedDomain;
   }

   public DeploymentContextImpl getDeploymentContext() {
      return this.deploymentContext;
   }

   public long getRequestId() {
      return this.requestId;
   }

   protected DomainMBean getDomain() {
      return this.proposedDomain != null ? this.proposedDomain : ManagementService.getRuntimeAccess(kernelId).getDomain();
   }

   protected BasicDeploymentMBean getMBean() {
      return this.mbean;
   }

   public BasicDeploymentMBean getDeploymentMBean() {
      return this.mbean;
   }

   public int getNotificationLevel() {
      return this.internalDeploymentData != null ? this.internalDeploymentData.getNotificationLevel() : 1;
   }

   public InternalDeploymentData getInternalDeploymentData() {
      return this.internalDeploymentData;
   }

   public final void prepare() throws DeploymentException {
      if (this.isDeploymentRequestValidForCurrentServer()) {
         this.dumpOperation();
         this.deploymentContext.setDeploymentOperation(this.operation);
         this.addContainerListener();

         try {
            this.fireVetoableDeploymentEvent();
            this.doPrepare();
            if (!this.isSupportingServer()) {
               this.relayState(1, (Exception)null);
            }
         } finally {
            this.removeContainerListener();
         }

      }
   }

   private void dumpOperation() {
      if (this.isDebugEnabled()) {
         this.debug("DeploymentData: " + this.deploymentData);
         this.debug("Proposed Mbean: ");
         if (this.mbean == null) {
            this.debug("   removed");
         } else {
            this.dumpModuleTargetInfo(this.mbean);
         }

      }
   }

   private void dumpModuleTargetInfo(ConfigurationMBean var1) {
      this.dumpTargetsForModule((TargetInfoMBean)var1);
      SubDeploymentMBean[] var2;
      if (var1 instanceof BasicDeploymentMBean) {
         var2 = ((BasicDeploymentMBean)var1).getSubDeployments();
      } else {
         var2 = ((SubDeploymentMBean)var1).getSubDeployments();
      }

      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            SubDeploymentMBean var4 = var2[var3];
            this.dumpModuleTargetInfo(var4);
         }
      }

   }

   private void dumpTargetsForModule(TargetInfoMBean var1) {
      String var2 = "";
      TargetMBean[] var3 = var1.getTargets();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         TargetMBean var5 = var3[var4];
         var2 = var2 + var5.getName();
      }

      this.debug("Module: " + var1.getName() + ", Targets: " + var2);
   }

   public void stageFilesFromAdminServer(String var1) throws DeploymentException {
      this.prepareDataUpdate(var1);
   }

   protected void ensureAppContainerSet() throws DeploymentException {
      if (this.appcontainer == null) {
         this.appcontainer = this.getApplication().findDeployment();
      }

   }

   protected void doPrepare() throws DeploymentException {
   }

   protected final void setupPrepare() throws DeploymentException {
      try {
         boolean var1 = true;
         this.getApplication().verifyLocalApp();
         if (this.isAppDeployment() && var1) {
            this.compatibilityProcessor();
         }

      } catch (Throwable var3) {
         DeploymentException var2 = DeployHelper.convertThrowable(var3);
         throw var2;
      }
   }

   protected void compatibilityProcessor() throws DeploymentException {
   }

   public final boolean isAppDeployment() {
      return this.mbean instanceof AppDeploymentMBean;
   }

   public final boolean isInternalApp() {
      return this.mbean instanceof AppDeploymentMBean && ((AppDeploymentMBean)this.mbean).isInternalApp();
   }

   public final void commit() throws DeploymentException {
      if (!this.completed) {
         if (!this.isDeploymentRequestValidForCurrentServer()) {
            this.complete(3, (Exception)null);
         } else {
            this.dumpOperation();
            this.addContainerListener();

            try {
               this.doCommit();
            } catch (Throwable var7) {
               DeploymentException var2 = DeployHelper.convertThrowable(var7);
               this.complete(2, var2);
               SlaveDeployerLogger.logCommitUpdateFailedLoggable("" + this.operation, this.app.getName()).log();
               throw var2;
            } finally {
               this.removeContainerListener();
            }

         }
      }
   }

   public final void cancel() throws DeploymentException {
      if (this.isCancelNecessary()) {
         if (!this.completed) {
            if (this.isDeploymentRequestValidForCurrentServer()) {
               if (this.isDebugEnabled()) {
                  this.debug("Operation.cancel() started for requestId : " + this.requestId);
               }

               this.addContainerListener();

               try {
                  this.doCancel();
               } catch (Throwable var7) {
                  Loggable var2 = SlaveDeployerLogger.logCancelFailedLoggable(this.taskId, serverName);
                  throw new DeploymentException(var2.getMessage(), var7);
               } finally {
                  this.removeContainerListener();
                  this.completed = true;
                  if (this.isDebugEnabled()) {
                     this.debug("Operation.cancel() finished for requestId : " + this.requestId);
                  }

               }

            }
         }
      }
   }

   private void addContainerListener() {
      this.state = new DeploymentState(this.mbean.getName(), this.taskId, this.getNotificationLevel());
      TargetMBean var1 = TargetUtils.findLocalTarget(this.mbean, server);
      this.state.setTarget(var1.getName());
      this.ml = ListenerFactory.createListener(this.getDeploymentMBean(), this.taskId, this.state);
      if (this.isDebugEnabled()) {
         this.debug("Adding " + this.ml);
      }

      appctrManager.addModuleListener(this.ml);
      this.getApplication().setStateRef(this.state);
   }

   private void removeContainerListener() {
      if (this.isDebugEnabled()) {
         this.debug("Removing " + this.ml);
      }

      appctrManager.removeModuleListener(this.ml);
      this.ml = null;
   }

   protected ModuleListener getListener() {
      return this.ml;
   }

   protected void doCommit() throws IOException, DeploymentException {
   }

   protected void doCancel() {
   }

   protected boolean isCancelNecessary() {
      return true;
   }

   protected final void silentCancelOnPrepareFailure() {
      try {
         this.doCancel();
      } catch (Throwable var2) {
      }

   }

   protected void complete(int var1, Exception var2) {
      this.completed = true;
      boolean var3 = var1 != 2;
      if (!var3) {
         if (var2 == null) {
            var2 = new DeploymentException("Task failed with unknown reason");
         }

         Loggable var4 = SlaveDeployerLogger.logTaskFailedLoggable(Long.toString(this.requestId), this.taskId, (Exception)var2);
         var4.log();
         this.cancelDataUpdate();
      }

      this.closeDataUpdate(var3);
      if (!this.isSupportingServer()) {
         this.relayState(var1, (Exception)var2);
      }

      this.fireDeploymentEvent();
   }

   protected final void silentRemove(Deployment var1) {
      try {
         var1.remove(this.deploymentContext);
      } catch (DeploymentException var3) {
         SlaveDeployerLogger.logOperationFailed("Remove", this.getMBean().getName(), var3);
      }

   }

   private void relayState(int var1, Exception var2) {
      if (this.state != null) {
         this.state.setTaskState(var1);
         this.state.setException(var2);
         TargetMBean var3 = TargetUtils.findLocalTarget(this.mbean, server);
         if (this.state.getTarget() == null) {
            this.state.setTarget(var3.getName());
         }

         try {
            if (var1 == 2 && var2 != null && !(var2 instanceof NonFatalDeploymentException)) {
               byte var4 = 0;
               this.state.setStagingState(var4);
               AppRuntimeStateManager.getManager().setStagingState(this.mbean.getName(), new String[]{var3.getName()}, var4, this.isInternalApp());
            }

            String var6 = this.deploymentData == null ? this.state.getIntendedState() : this.deploymentData.getIntendedState();
            if (!this.isAppDeleted()) {
               AppRuntimeStateManager.getManager().setState(this.mbean.getName(), new String[]{var3.getName()}, var6);
            }

            if (this.isDebugEnabled()) {
               this.debug("Updated intended state for " + this.mbean.getName() + " to " + var6);
            }
         } catch (ManagementException var5) {
            if (this.isDebugEnabled()) {
               this.debug("Failed to update intended state for " + this.mbean.getName());
            }
         }

         if (!ManagementService.getRuntimeAccess(kernelId).isAdminServer() && !this.isAppDeleted()) {
            AppRuntimeStateManager.getManager().updateState(this.mbean.getName(), this.state);
         }

         if (this.isDebugEnabled()) {
            this.debug("Relaying updated state for app, " + this.state.getId() + " to " + this.state.getCurrentState() + ", taskState: " + this.state.getTaskState());
         }

         deploymentManager.relayStatus(this.requestId, this.state);
      }
   }

   protected String[] getFiles() {
      return this.internalDeploymentData != null ? this.deploymentData.getFiles() : null;
   }

   public BasicDeployment getApplication() {
      return this.app;
   }

   public String getTaskId() {
      return this.taskId;
   }

   protected void debug(String var1) {
      Debug.deploymentDebug(this.getDebugPrefix() + var1);
   }

   private String getDebugPrefix() {
      String var1 = this.getClass().getName();
      var1 = var1.substring(this.getClass().getPackage().getName().length() + 1);
      return "[op=" + var1 + ",task=" + this.taskId + "]";
   }

   protected boolean isDebugEnabled() {
      return Debug.isDeploymentDebugEnabled();
   }

   protected boolean isAdminMode() {
      return this.deploymentData != null && this.deploymentData.getDeploymentOptions() != null && this.deploymentData.getDeploymentOptions().isTestMode();
   }

   protected boolean isAppContainerActive(Deployment var1) {
      if (var1 == null) {
         return false;
      } else {
         int var2 = this.getState(var1);
         return var2 == 3 || var2 == 4 || var2 == 2;
      }
   }

   protected void activate(Deployment var1) throws DeploymentException {
      boolean var2 = this.isAdminMode() || this.isServerInAdminMode();
      this.deploymentContext.setAdminModeTransition(var2);
      if (this.getState(var1) == 1) {
         var1.activate(this.deploymentContext);
      }

   }

   protected boolean isGracefulProductionToAdmin() {
      return this.deploymentData != null && this.deploymentData.getDeploymentOptions() != null && this.deploymentData.getDeploymentOptions().isGracefulProductionToAdmin();
   }

   private boolean isIgnoreSessions() {
      return this.deploymentData != null && this.deploymentData.getDeploymentOptions() != null && this.deploymentData.getDeploymentOptions().isGracefulIgnoreSessions();
   }

   private int getRMIGracePeriod() {
      return this.deploymentData != null && this.deploymentData.getDeploymentOptions() != null ? this.deploymentData.getDeploymentOptions().getRMIGracePeriodSecs() : -1;
   }

   protected void gracefulProductionToAdmin(Deployment var1) throws DeploymentException {
      boolean var2 = this.isIgnoreSessions();
      boolean var3 = this.isAdminMode();
      int var4 = this.getRMIGracePeriod();
      this.deploymentContext.setAdminModeTransition(var3);
      this.deploymentContext.setIgnoreSessions(var2);
      this.deploymentContext.setRMIGracePeriodSecs(var4);
      this.getApplication().gracefulProductionToAdmin(var1, this.deploymentContext);
   }

   protected void forceProductionToAdmin(Deployment var1) throws DeploymentException {
      boolean var2 = this.isAdminMode();
      this.deploymentContext.setAdminModeTransition(var2);
      this.getApplication().forceProductionToAdmin(var1, this.getForceUndeployTimeoutSecs(), this.deploymentContext);
   }

   protected boolean isNewApplication() {
      return this.deploymentData == null ? false : this.deploymentData.isNewApplication();
   }

   protected String[] getModules() {
      return this.deploymentData == null ? null : this.deploymentData.getModules();
   }

   protected String[] getTargets() {
      return this.deploymentData == null ? null : this.deploymentData.getTargets();
   }

   protected final boolean isTargetListContainsCurrentServer() {
      if (this.deploymentData == null) {
         return false;
      } else {
         DomainMBean var1 = this.proposedDomain != null ? this.proposedDomain : ManagementService.getRuntimeAccess(kernelId).getDomain();
         Set var2 = null;

         try {
            var2 = this.deploymentData.getAllTargetedServers(this.deploymentData.getAllLogicalTargets(), var1);
         } catch (InvalidTargetException var4) {
            var4.printStackTrace();
            var2 = Collections.EMPTY_SET;
         }

         if (this.isDebugEnabled()) {
            this.debug(" +++ TargetList : " + var2);
         }

         String var3 = server.getName();
         return var2.contains(var3);
      }
   }

   protected final boolean isAppTargetedToCurrentServer() {
      return TargetHelper.isAppTargetedToCurrentServer(this.mbean);
   }

   protected long getForceUndeployTimeoutSecs() {
      if (this.deploymentData == null) {
         return 0L;
      } else {
         return this.deploymentData.getDeploymentOptions() == null ? 0L : this.deploymentData.getDeploymentOptions().getForceUndeployTimeout();
      }
   }

   protected void silentDeactivate(Deployment var1) {
      try {
         var1.deactivate(this.deploymentContext);
      } catch (DeploymentException var3) {
         SlaveDeployerLogger.logOperationFailed("Deactivate", this.getMBean().getName(), var3);
      }

   }

   protected void silentUnprepare(Deployment var1) {
      try {
         var1.unprepare(this.deploymentContext);
      } catch (DeploymentException var3) {
         SlaveDeployerLogger.logOperationFailed("Unprepare", this.getMBean().getName(), var3);
      }

   }

   protected void silentProductionToAdmin(Deployment var1) {
      try {
         if (this.isGracefulProductionToAdmin()) {
            this.gracefulProductionToAdmin(var1);
         } else {
            this.forceProductionToAdmin(var1);
         }
      } catch (DeploymentException var3) {
         SlaveDeployerLogger.logOperationFailed("ProductionToAdmin", this.getMBean().getName(), var3);
      }

   }

   protected void silentStop(Deployment var1, String[] var2) {
      try {
         this.deploymentContext.setUpdatedResourceURIs(var2);
         var1.stop(this.deploymentContext);
      } catch (DeploymentException var4) {
         SlaveDeployerLogger.logOperationFailed("Stop", this.getMBean().getName(), var4);
         if (this.getState(var1) == 3) {
            this.silentProductionToAdmin(var1);
         }

         this.silentDeactivate(var1);
         this.silentUnprepare(var1);
      }

   }

   protected final void stop(Deployment var1, String[] var2) throws DeploymentException {
      if (this.isAppContainerActive(var1)) {
         try {
            this.deploymentContext.setUpdatedResourceURIs(var2);
            var1.stop(this.deploymentContext);
         } catch (DeploymentException var4) {
            SlaveDeployerLogger.logOperationFailed("Stop", this.getMBean().getName(), var4);
            if (var4 instanceof NonFatalDeploymentException) {
               throw var4;
            }

            if (this.getState(var1) == 3) {
               this.silentProductionToAdmin(var1);
            }

            this.silentDeactivate(var1);
            this.silentUnprepare(var1);
         }

      }
   }

   protected int getState(Deployment var1) {
      return DeployHelper.getState(var1);
   }

   protected boolean isAdminState(Deployment var1) {
      return DeployHelper.isAdminState(var1);
   }

   protected void fireVetoableDeploymentEvent() throws DeploymentException {
      if (this.isAppDeployment() && !ManagementService.getRuntimeAccess(kernelId).isAdminServer()) {
         BaseDeploymentEvent.EventType var1 = null;
         switch (this.operation) {
            case 1:
               var1 = VetoableDeploymentEvent.APP_ACTIVATE;
            case 2:
            case 3:
            case 5:
            case 6:
            case 8:
            case 10:
            default:
               break;
            case 4:
               var1 = VetoableDeploymentEvent.APP_UNDEPLOY;
               break;
            case 7:
               var1 = VetoableDeploymentEvent.APP_START;
               break;
            case 9:
            case 11:
               var1 = VetoableDeploymentEvent.APP_DEPLOY;
         }

         if (var1 != null) {
            try {
               DeploymentEventManager.sendVetoableDeploymentEvent(VetoableDeploymentEvent.create(this, var1, (AppDeploymentMBean)this.getApplication().getDeploymentMBean(), this.isNewApplication(), this.getModules(), this.getTargets()));
            } catch (DeploymentException var3) {
               this.complete(2, var3);
               throw var3;
            }
         }

      }
   }

   private void fireDeploymentEvent() {
      if (this.isAppDeployment() && !ManagementService.getRuntimeAccess(kernelId).isAdminServer()) {
         BaseDeploymentEvent.EventType var1 = null;
         switch (this.operation) {
            case 1:
               var1 = DeploymentEvent.APP_ACTIVATED;
            case 2:
            case 3:
            case 5:
            case 6:
            case 8:
            default:
               break;
            case 4:
               if (this.isAppDeleted()) {
                  var1 = DeploymentEvent.APP_DELETED;
               }
               break;
            case 7:
               var1 = DeploymentEvent.APP_STARTED;
               break;
            case 9:
            case 10:
               var1 = DeploymentEvent.APP_REDEPLOYED;
               break;
            case 11:
               var1 = DeploymentEvent.APP_DEPLOYED;
         }

         if (var1 != null) {
            DeploymentEventManager.sendDeploymentEvent(DeploymentEvent.create(this, var1, (AppDeploymentMBean)this.getApplication().getDeploymentMBean(), this.getModules(), this.getTargets()));
         }

      }
   }

   protected boolean isAppDeleted() {
      if (!this.isAppDeployment()) {
         return false;
      } else {
         DomainMBean var1 = this.getProposedDomain();
         BasicDeploymentMBean var2 = this.getApplication().getDeploymentMBean();
         return var1 != null && var2 != null && var1.lookupAppDeployment(var2.getName()) == null;
      }
   }

   protected boolean isServerInAdminMode() {
      ServerRuntimeMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServerRuntime();
      return var1 != null && var1.getStateVal() == 17;
   }

   protected final boolean isAppSystemResource() {
      return this.mbean instanceof SystemResourceMBean;
   }

   protected boolean isDeploymentRequestValidForCurrentServer() {
      boolean var1 = this.isTargetListContainsCurrentServer();
      if (this.isDebugEnabled()) {
         this.debug(" +++ Supplied TargetList contains current server : " + var1);
      }

      boolean var2 = this.isAppTargetedToCurrentServer();
      if (this.isDebugEnabled()) {
         this.debug(" +++ Application targeted to current server : " + var2);
      }

      if (!var1 && !var2) {
         return !this.isAppSystemResource();
      } else {
         return true;
      }
   }

   public final int getOperationType() {
      return this.operation;
   }

   public final boolean isControlOperation() {
      return this.controlOperation;
   }

   protected void initDataUpdate() throws DeploymentException {
   }

   protected final void prepareDataUpdate(String var1) throws DeploymentException {
      this.initDataUpdate();
      this.getApplication().prepareDataUpdate(var1);
   }

   protected final void commitDataUpdate() throws DeploymentException {
      this.getApplication().commitDataUpdate();
   }

   protected final void cancelDataUpdate() {
      this.getApplication().cancelDataUpdate(this.getRequestId());
   }

   protected final void closeDataUpdate(boolean var1) {
      this.getApplication().closeDataUpdate(this.getRequestId(), var1);
   }

   private boolean isSupportingServer() {
      if (this.isTargetListContainsCurrentServer()) {
         return false;
      } else if (!(this.mbean instanceof AppDeploymentMBean)) {
         return false;
      } else if (cluster == null) {
         return false;
      } else {
         Set var1 = TargetHelper.getAllTargetedServers(this.mbean);
         return !var1.contains(serverName);
      }
   }

   static {
      server = ManagementService.getRuntimeAccess(kernelId).getServer();
      serverName = server.getName();
      cluster = server.getCluster();
      deploymentManager = DeploymentManager.getInstance();
      appctrManager = weblogic.application.DeploymentManager.getDeploymentManager();
   }
}
