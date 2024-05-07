package weblogic.deploy.internal.targetserver;

import java.io.Serializable;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.deploy.common.Debug;
import weblogic.deploy.internal.AggregateDeploymentVersion;
import weblogic.deploy.internal.Deployment;
import weblogic.deploy.internal.DeploymentVersion;
import weblogic.deploy.internal.InternalDeploymentData;
import weblogic.deploy.internal.targetserver.operations.AbstractOperation;
import weblogic.deploy.internal.targetserver.operations.ActivateOperation;
import weblogic.deploy.internal.targetserver.operations.DynamicUpdateOperation;
import weblogic.deploy.internal.targetserver.operations.RedeployOperation;
import weblogic.deploy.internal.targetserver.operations.RemoveOperation;
import weblogic.deploy.internal.targetserver.operations.RetireOperation;
import weblogic.deploy.internal.targetserver.operations.StartOperation;
import weblogic.deploy.internal.targetserver.operations.StopOperation;
import weblogic.deploy.internal.targetserver.operations.UnprepareOperation;
import weblogic.deploy.internal.targetserver.state.DeploymentState;
import weblogic.deploy.service.DeploymentContext;
import weblogic.deploy.service.DeploymentRequest;
import weblogic.logging.Loggable;
import weblogic.management.DeploymentException;
import weblogic.management.ManagementException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.SystemResourceMBean;
import weblogic.management.deploy.internal.AppRuntimeStateManager;
import weblogic.management.deploy.internal.DeployerRuntimeLogger;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.StackTraceUtils;

public class DeploymentManager {
   private DeploymentServiceDispatcher dispatcher;
   private AggregateDeploymentVersion targetServerAggregateDeploymentVersion;
   private final String serverName;
   private final boolean isAdminServer;
   private Map requestInfoTable;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private ServerRuntimeMBean serverBean;

   private DeploymentManager() {
      this.serverName = ManagementService.getPropertyService(kernelId).getServerName();
      this.isAdminServer = ManagementService.getPropertyService(kernelId).isAdminServer();
      this.requestInfoTable = new HashMap();
   }

   public static DeploymentManager getInstance() {
      return DeploymentManager.Maker.MANAGER;
   }

   private void debug(String var1) {
      Debug.deploymentDebug(var1);
   }

   private boolean isDebugEnabled() {
      return Debug.isDeploymentDebugEnabled();
   }

   public void initialize() {
      this.dispatcher = DeploymentServiceDispatcher.getInstance();
      this.targetServerAggregateDeploymentVersion = AggregateDeploymentVersion.createAggregateDeploymentVersion();
      this.dispatcher.initialize("Application", this.targetServerAggregateDeploymentVersion, this);
   }

   public void shutdown() {
      if (this.dispatcher != null) {
         this.dispatcher.shutdown();
      }

   }

   public void addOrUpdateTargetDeploymentVersion(String var1, DeploymentVersion var2) {
      if (!this.isAdminServer) {
         this.getTargetServerAggregateDeploymentsVersion().addOrUpdateDeploymentVersion(var1, var2);
      }

   }

   public void removeTargetDeploymentVersionFor(String var1) {
      if (!this.isAdminServer) {
         this.getTargetServerAggregateDeploymentsVersion().removeDeploymentVersionFor(var1);
      }

   }

   public void handleUpdateDeploymentContext(DeploymentContext var1) {
      DeploymentRequest var2 = var1.getDeploymentRequest();
      long var3 = var2.getId();
      DeploymentRequestInfo var5 = new DeploymentRequestInfo(var3, var1);
      synchronized(this.requestInfoTable) {
         label38: {
            try {
               if (this.requestInfoTable.get(new Long(var3)) == null) {
                  this.requestInfoTable.put(new Long(var3), var5);
                  this.createOperations(var1);
                  break label38;
               }

               if (this.isDebugEnabled()) {
                  this.debug("DeploymentManagerT: prepare already received for request '" + var3 + "' - ignoring this request");
               }
            } catch (Throwable var9) {
               this.dispatcher.notifyContextUpdateFailed(var3, var9);
               return;
            }

            return;
         }
      }

      if (this.isDebugEnabled()) {
         this.debug("DeploymentManagerT: notifying 'context updated' for request '" + var3 + "'");
      }

      this.dispatcher.notifyContextUpdated(var3);
   }

   public void handlePrepare(DeploymentContext var1) {
      DeploymentRequest var2 = var1.getDeploymentRequest();
      long var3 = var2.getId();
      DeploymentRequestInfo var5 = this.getDeploymentRequestInfo(var3);
      if (var5 == null) {
         if (this.isDebugEnabled()) {
            this.debug("DeploymentManagerT: handlePrepare received for request '" + var3 + " that has already completed");
         }

         String var13 = DeployerRuntimeLogger.requestCompletedOrCancelled(var3);
         this.dispatcher.notifyPrepareFailure(var3, new Exception(var13));
      } else {
         if (this.isDebugEnabled()) {
            this.debug("DeploymentManagerT: handlePrepare for request: " + var3);
         }

         try {
            boolean var6 = this.assignToPreOrPostDeploymentHandlerList(var5);
            if (var6) {
               if (this.isDebugEnabled()) {
                  this.debug("DeploymentManagerT: handlePrepare handling 'preDeploymentHandlerList' of size " + var5.preDeploymentHandlerList.size() + " for request '" + var3 + "'");
               }

               this.prepareDeploymentList(var5.preDeploymentHandlerList, var1);
               if (var5.postDeploymentHandlerList.size() == 0) {
                  if (this.isDebugEnabled()) {
                     this.debug("DeploymentManagerT: notifying 'prepare' success for request '" + var3 + "'");
                  }

                  this.dispatcher.notifyPrepareSuccess(var3);
               } else if (this.isDebugEnabled()) {
                  this.debug("DeploymentManagerT: will await for 'configPrepareCompleted' callback to process 'prepare' of postDeploymentHandler list for request '" + var3 + "'");
               }
            } else {
               if (var5.postDeploymentHandlerList.size() > 0) {
                  if (this.isDebugEnabled()) {
                     this.debug("DeploymentManagerT: handlePrepare handling 'postDeploymentHandlerList' of size " + var5.postDeploymentHandlerList.size() + " for request '" + var3 + "'");
                  }

                  this.prepareDeploymentList(var5.postDeploymentHandlerList, var1);
               }

               if (this.isDebugEnabled()) {
                  this.debug("DeploymentManagerT: notifying 'prepare' success for request '" + var3 + "'");
               }

               this.dispatcher.notifyPrepareSuccess(var3);
            }
         } catch (Throwable var11) {
            this.removeSystemResourcesFromRestartList(var5);
            this.dispatcher.notifyPrepareFailure(var3, var11);
         } finally {
            var5.configPrepareCompletedLatch.countDown();
         }

      }
   }

   private boolean assignToPreOrPostDeploymentHandlerList(DeploymentRequestInfo var1) {
      boolean var2 = false;
      DeploymentRequest var3 = var1.context.getDeploymentRequest();
      Iterator var4 = var3.getDeployments("Application");
      if (var4 != null) {
         while(true) {
            while(true) {
               while(var4.hasNext()) {
                  Deployment var5 = (Deployment)var4.next();
                  if (this.targetedToThisServer(var5)) {
                     if (var5.isBeforeDeploymentHandler() && var5.isDeploy()) {
                        var2 = true;
                        if (this.isDebugEnabled()) {
                           this.debug("DeploymentManagerT: adding '" + var5 + "' to pre " + " DeploymentHandler list for request '" + var3.getId() + "'");
                        }

                        var1.preDeploymentHandlerList.add(var5);
                     } else {
                        if (this.isDebugEnabled()) {
                           this.debug("DeploymentManagerT: adding '" + var5 + "' to post " + " DeploymentHandler list for request '" + var3.getId() + "'");
                        }

                        var1.postDeploymentHandlerList.add(var5);
                     }
                  } else if (this.isDebugEnabled()) {
                     this.debug("DeploymentManagerT: application deployment: " + var5 + " of request id: " + var3.getId() + " not targeted on " + " this server");
                  }
               }

               return var2;
            }
         }
      } else {
         return var2;
      }
   }

   private DeploymentRequestInfo getDeploymentRequestInfo(long var1) {
      synchronized(this.requestInfoTable) {
         return (DeploymentRequestInfo)this.requestInfoTable.get(new Long(var1));
      }
   }

   public void handleCommit(DeploymentContext var1) {
      DeploymentRequest var2 = var1.getDeploymentRequest();
      long var3 = var2.getId();
      DeploymentRequestInfo var5 = this.getDeploymentRequestInfo(var3);
      if (var5 == null) {
         if (this.isDebugEnabled()) {
            this.debug("DeploymentManagerT: handleCommit received for request '" + var3 + " that has already completed");
         }

         String var15 = DeployerRuntimeLogger.requestCompletedOrCancelled(var3);
         this.notifyCommitFailure(var3, (DeploymentRequestInfo)null, new Exception(var15));
      } else {
         if (this.isDebugEnabled()) {
            this.debug("DeploymentManagerT: handleCommit for request: " + var3);
         }

         boolean var6 = false;
         Iterator var7 = null;

         try {
            var7 = var2.getDeployments("Application");
            if (var7 == null) {
               if (this.isDebugEnabled()) {
                  this.debug("DeploymentManagerT: handleCommit - no deployments to 'commit' - notifying 'commit' success for request '" + var3 + "'");
               }

               this.notifyCommitSuccess(var3, var5);
            } else {
               var6 = this.processDeploymentsForCommit(var7, var2);
               if (var6) {
                  if (this.isDebugEnabled()) {
                     this.debug("DeploymentManagerT: handleCommit handling 'preDeploymentHandlerList' of size " + var5.preDeploymentHandlerList.size() + " for request '" + var3 + "'");
                  }

                  this.activateDeploymentList(var5.preDeploymentHandlerList, var5);
                  if (var5.postDeploymentHandlerList.size() == 0) {
                     if (this.isDebugEnabled()) {
                        this.debug("DeploymentManagerT: notifying 'commit' success for request '" + var3 + "'");
                     }

                     this.notifyCommitSuccess(var3, var5);
                  } else if (this.isDebugEnabled()) {
                     this.debug("DeploymentManagerT: will await for 'configCommitCompleted' callback to process 'commit' of postDeploymentHandler list for request '" + var3 + "'");
                  }
               } else {
                  if (var5.postDeploymentHandlerList.size() > 0) {
                     if (this.isDebugEnabled()) {
                        this.debug("DeploymentManagerT: handleCommit handling 'postDeploymentHandlerList' of size " + var5.postDeploymentHandlerList.size() + " for request '" + var3 + "'");
                     }

                     this.activateDeploymentList(var5.postDeploymentHandlerList, var5);
                  }

                  if (this.isDebugEnabled()) {
                     this.debug("DeploymentManagerT: notifying 'commit' success for request '" + var3 + "'");
                  }

                  this.notifyCommitSuccess(var3, var5);
               }
            }
         } catch (Throwable var13) {
            this.notifyCommitFailure(var3, var5, var13);
         } finally {
            var5.configCommitCompletedLatch.countDown();
         }

      }
   }

   private final ServerRuntimeMBean getServerBean() {
      if (this.serverBean == null) {
         this.serverBean = ManagementService.getRuntimeAccess(kernelId).getServerRuntime();
      }

      return this.serverBean;
   }

   private boolean requiresRestart(String var1) {
      return this.getServerBean().isRestartPendingForSystemResource(var1);
   }

   private boolean processDeploymentsForCommit(Iterator var1, DeploymentRequest var2) {
      boolean var3 = false;

      while(var1.hasNext()) {
         Deployment var4 = (Deployment)var1.next();
         if (this.targetedToThisServer(var4)) {
            if (var4.isBeforeDeploymentHandler() && var4.isDeploy()) {
               var3 = true;
            }
         } else {
            String var5 = var4.getDeploymentTaskRuntimeId();
            if (var5 != null) {
               this.sendTaskCompletedNotification(var2.getId(), var5);
            }
         }
      }

      return var3;
   }

   public void handleCancel(DeploymentContext var1) {
      DeploymentRequest var2 = var1.getDeploymentRequest();
      long var3 = var2.getId();
      DeploymentRequestInfo var5 = this.getDeploymentRequestInfo(var3);
      if (var5 == null) {
         this.notifyCancelSuccess(var3, var5);
      } else {
         try {
            if (this.isDebugEnabled()) {
               this.debug("DeploymentManagerT: handleCancel for request: " + var2.getId());
            }

            this.removeSystemResourcesFromRestartList(var5);
            Iterator var6 = var2.getDeployments("Application");
            if (var6 != null) {
               while(var6.hasNext()) {
                  Deployment var7 = (Deployment)var6.next();
                  this.handleDeploymentCancel(var7, var5);
               }
            }

            if (this.isDebugEnabled()) {
               this.debug("DeploymentManagerT: notifying 'cancel' success for request '" + var3 + "'");
            }

            this.notifyCancelSuccess(var3, var5);
         } catch (Throwable var8) {
            this.notifyCancelFailure(var3, var5, var8);
         }

      }
   }

   public void relayStatus(long var1, Serializable var3) {
      if (this.isDebugEnabled()) {
         this.debug("DeploymentManagerT: notifying status '" + var3 + "' for " + " request '" + var1 + "'");
      }

      this.dispatcher.notifyStatusUpdate(var1, var3);
   }

   public void configPrepareCompleted(DeploymentContext var1) {
      DeploymentRequest var2 = var1.getDeploymentRequest();
      long var3 = var2.getId();
      DeploymentRequestInfo var5 = this.getDeploymentRequestInfo(var3);
      if (var5 != null) {
         try {
            if (this.isDebugEnabled()) {
               this.debug("DeploymentManagerT: configPrepareCompleted about to await " + var2.getTimeoutInterval() + " millis for request '" + var3 + "'");
            }

            var5.configPrepareCompletedLatch.await(var2.getTimeoutInterval(), TimeUnit.MILLISECONDS);
         } catch (InterruptedException var7) {
         }

         if (this.isDebugEnabled()) {
            this.debug("DeploymentManagerT: configPrepareCompleted for request '" + var3 + "'");
         }

         try {
            if (var5.preDeploymentHandlerList.size() > 0 && var5.postDeploymentHandlerList.size() > 0) {
               if (this.isDebugEnabled()) {
                  this.debug("DeploymentManagerT: configPrepareCompleted handling 'postDeploymentHandlerList' of size " + var5.postDeploymentHandlerList.size() + " for request '" + var3 + "'");
               }

               this.prepareDeploymentList(var5.postDeploymentHandlerList, var1);
               if (this.isDebugEnabled()) {
                  this.debug("DeploymentManagerT: notifying 'prepare' success for request '" + var3 + "'");
               }

               this.dispatcher.notifyPrepareSuccess(var3);
            }
         } catch (Throwable var8) {
            if (this.isDebugEnabled()) {
               Debug.deploymentLogger.debug("DeploymentManagerT: configPrepareCompleted encountered an exception: " + StackTraceUtils.throwable2StackTrace(var8) + " for request '" + var3 + "' notifying 'prepare' failure");
            }

            this.removeSystemResourcesFromRestartList(var5);
            this.dispatcher.notifyPrepareFailure(var3, new Exception(var8));
         }

      }
   }

   public void configCommitCompleted(DeploymentContext var1) {
      DeploymentRequest var2 = var1.getDeploymentRequest();
      long var3 = var2.getId();
      DeploymentRequestInfo var5 = this.getDeploymentRequestInfo(var3);
      if (var5 != null) {
         if (this.isDebugEnabled()) {
            this.debug("DeploymentManagerT: configCommitCompleted for request " + var3 + "'");
         }

         try {
            if (this.isDebugEnabled()) {
               this.debug("DeploymentManagerT: configCommitCompleted about to await " + var2.getTimeoutInterval() + " millis for request '" + var3 + "'");
            }

            var5.configCommitCompletedLatch.await(var2.getTimeoutInterval(), TimeUnit.MILLISECONDS);
         } catch (InterruptedException var7) {
         }

         try {
            if (var5.preDeploymentHandlerList.size() > 0 && var5.postDeploymentHandlerList.size() > 0) {
               if (this.isDebugEnabled()) {
                  this.debug("DeploymentManagerT: configCommitCompleted handling 'postDeploymentHandlerList' of size " + var5.postDeploymentHandlerList.size() + " for request '" + var3 + "'");
               }

               this.activateDeploymentList(var5.postDeploymentHandlerList, var5);
               if (this.isDebugEnabled()) {
                  this.debug("DeploymentManagerT: notifying 'commit' success for request '" + var3 + "'");
               }

               this.notifyCommitSuccess(var3, var5);
            }
         } catch (Throwable var8) {
            if (this.isDebugEnabled()) {
               this.debug("DeploymentManagerT: notifying 'commit' failure for request '" + var3 + "' due to '" + var8.toString());
            }

            this.notifyCommitFailure(var3, var5, var8);
         }

      }
   }

   private void sendTaskCompletedNotification(long var1, String var3) {
      DeploymentState var4 = new DeploymentState("?", var3, 0);
      var4.setTaskState(3);
      this.relayStatus(var1, var4);
   }

   private void handleDeploymentPrepare(Deployment var1, DeploymentRequestInfo var2) throws Throwable {
      if (var2 != null) {
         if (this.isDebugEnabled()) {
            this.debug("handleDeploymentPrepare for deployment: " + var1);
         }

         AbstractOperation var3 = (AbstractOperation)var2.operationsMap.get(var1.getDeploymentTaskRuntimeId());
         if (var3 == null) {
            throw new DeploymentException("Cannot find operation for deployment : " + var1);
         } else {
            if (var3.getDeploymentMBean() instanceof SystemResourceMBean && var1.requiresRestart()) {
               if (this.isDebugEnabled()) {
                  this.debug("handleDeploymentPrepare: adding SystemResource '" + var3.getDeploymentMBean().getName() + "' to pending restart " + "list on server");
               }

               this.getServerBean().addPendingRestartSystemResource(var3.getDeploymentMBean().getName());
               var2.systemResourcesToBeRestarted.add(var3.getDeploymentMBean());
            }

            var3.prepare();
         }
      }
   }

   private void removeSystemResourcesFromRestartList(DeploymentRequestInfo var1) {
      if (var1 != null) {
         SystemResourceMBean var3;
         if (!var1.systemResourcesToBeRestarted.isEmpty()) {
            for(Iterator var2 = var1.systemResourcesToBeRestarted.iterator(); var2.hasNext(); this.getServerBean().removePendingRestartSystemResource(var3.getName())) {
               var3 = (SystemResourceMBean)var2.next();
               if (this.isDebugEnabled()) {
                  this.debug("removing SystemResource '" + var3.getName() + "' from pending restart " + "list on server");
               }
            }
         }

      }
   }

   private void updateRuntimeStateAndAggregateVersion(String var1, DeploymentVersion var2, boolean var3) throws ManagementException {
      if (!this.isAdminServer) {
         if (var3) {
            if (this.isDebugEnabled()) {
               this.debug("Updating runtime state and aggregate deployment version component on target server for " + var1);
            }

            AppRuntimeStateManager.getManager().updateState(var1, var2);
            this.addOrUpdateTargetDeploymentVersion(var1, var2);
         } else {
            if (this.isDebugEnabled()) {
               this.debug("Removing runtime state and aggregate deployment version component on target server for " + var1);
            }

            AppRuntimeStateManager.getManager().remove(var1);
            this.removeTargetDeploymentVersionFor(var1);
         }
      }

   }

   private void handleDeploymentCommit(Deployment var1, AbstractOperation var2) throws Throwable {
      InternalDeploymentData var3 = var2.getInternalDeploymentData();
      String var4 = var1.getIdentity();
      if (var3 != null && var2.getDeploymentMBean() != null) {
         String var9 = var3.getDeploymentName();
         BasicDeploymentMBean var6 = var2.getDeploymentMBean();
         if (var6 == null) {
            if (this.isDebugEnabled()) {
               this.debug("Nothing to do to deploy app :" + var9);
            }

            DeploymentState var10 = new DeploymentState("?", var4, 0);
            var10.setTaskState(2);
            this.relayStatus(var1.getDeploymentRequestId(), var10);
            String var11 = DeployerRuntimeLogger.nothingToCommit(ApplicationVersionUtils.getDisplayName(var2.getDeploymentMBean()), this.serverName);
            throw new ManagementException(var11);
         } else {
            var2.commit();
            int var7 = var2.getOperationType();
            if (!var2.isControlOperation()) {
               boolean var8 = var6 instanceof AppDeploymentMBean && var7 != 4;
               this.updateRuntimeStateAndAggregateVersion(var1.getIdentity(), var1.getProposedDeploymentVersion(), var8);
            }

         }
      } else {
         if (this.isDebugEnabled()) {
            this.debug("Nothing to do to commit deployment :" + var4 + " associated with request id: " + var1.getDeploymentRequestId());
         }

         DeploymentState var5 = new DeploymentState("?", var1.getDeploymentTaskRuntimeId(), 0);
         var5.setTaskState(3);
         this.relayStatus(var1.getDeploymentRequestId(), var5);
      }
   }

   private boolean targetedToThisServer(Deployment var1) {
      String[] var2 = var1.getTargets();
      ServerMBean var3 = ManagementService.getRuntimeAccess(kernelId).getServer();
      ClusterMBean var4 = var3.getCluster();
      boolean var5 = var4 != null;

      for(int var6 = 0; var6 < var2.length; ++var6) {
         if (this.serverName.equals(var2[var6])) {
            return true;
         }

         if (var5) {
            Set var7 = var4.getServerNames();
            if (var7.contains(var2[var6])) {
               return true;
            }
         }
      }

      return false;
   }

   private void handleDeploymentCancel(Deployment var1, DeploymentRequestInfo var2) throws Throwable {
      if (!this.targetedToThisServer(var1)) {
         if (this.isDebugEnabled()) {
            this.debug("DeploymentManagerT: notifying 'cancel' success for request '" + var1.getDeploymentRequestId() + "'");
         }

      } else if (var2 == null) {
         if (this.isDebugEnabled()) {
            this.debug("DeploymentManagerT: nothing to cancel on request '" + var1.getDeploymentRequestId() + "' since there" + " is no requestInfo available");
         }

      } else {
         AbstractOperation var3 = (AbstractOperation)var2.operationsMap.get(var1.getDeploymentTaskRuntimeId());
         if (var3 == null) {
            if (this.isDebugEnabled()) {
               this.debug("DeploymentManagerT: nothing to cancel on request '" + var1.getDeploymentRequestId() + "' since there" + " is no operation available");
            }

         } else {
            var3.cancel();
         }
      }
   }

   private AggregateDeploymentVersion getTargetServerAggregateDeploymentsVersion() {
      if (this.targetServerAggregateDeploymentVersion != null) {
         this.targetServerAggregateDeploymentVersion = AggregateDeploymentVersion.createAggregateDeploymentVersion();
      }

      return this.targetServerAggregateDeploymentVersion;
   }

   private AbstractOperation createOperation(Deployment var1, DeploymentRequestInfo var2) throws Exception {
      DeploymentContext var3 = var2.context;
      String var4 = var1.getDeploymentTaskRuntimeId();
      InternalDeploymentData var5 = var1.getInternalDeploymentData();
      if (var5 == null) {
         if (this.isDebugEnabled()) {
            this.debug("Nothing to do to deploy '" + var1 + "'");
         }

         String var15 = DeployerRuntimeLogger.nothingToDoForTask(var4);
         throw new Exception(var15);
      } else {
         int var6 = var5.getDeploymentOperation();
         String var7 = var5.getDeploymentName();
         if (this.isDebugEnabled()) {
            this.debug(" +++ ctxt Obj = " + var3 + " type " + var3.getClass().getName());
         }

         DomainMBean var8 = (DomainMBean)var3.getContextComponent("PROPOSED_CONFIGURATION");
         boolean var9 = this.requiresRestart(var7);
         long var10 = var1.getDeploymentRequestId();
         AuthenticatedSubject var12 = var3.getDeploymentRequest().getInitiator();
         if (this.isDebugEnabled()) {
            this.debug("Creating target server deploy operation '" + DeployHelper.getTaskName(var6) + "' for '" + "[" + var4 + "]" + var7 + "' initiated by '" + var12);
         }

         BasicDeploymentMBean var13;
         switch (var6) {
            case 1:
            case 6:
            case 11:
               var13 = this.findDeploymentMBean(var8, var7, var6, true);
               AbstractOperation var14 = (new ActivateOperation(var10, var4, var5, var13, var8, var12, var9)).refine();
               if (var14 instanceof RedeployOperation && var6 != 6) {
                  var14 = ((RedeployOperation)var14).refine();
               }

               return var14;
            case 2:
            case 12:
            default:
               throw new AssertionError("Invalid Deployment operation provided " + var5.getDeploymentOperation());
            case 3:
            case 8:
               var13 = this.findDeploymentMBean(var8, var7, var6, false);
               return new StopOperation(var10, var4, var5, var13, var8, var12, var9);
            case 4:
               var13 = this.findDeploymentMBean(var8, var7, var6, false);
               return new RemoveOperation(var10, var4, var5, var13, var8, var12, var9);
            case 5:
               var13 = this.findDeploymentMBean(var8, var7, var6, false);
               return new UnprepareOperation(var10, var4, var5, var13, var8, var12, var9);
            case 7:
               var13 = this.findDeploymentMBean(var8, var7, var6, false);
               return new StartOperation(var10, var4, var5, var13, var8, var12, var9);
            case 9:
               var13 = this.findDeploymentMBean(var8, var7, var6, true);
               return (new RedeployOperation(var10, var4, var5, var13, var8, var12, var9)).refine();
            case 10:
               var13 = this.findDeploymentMBean(var8, var7, var6, true);
               return (new DynamicUpdateOperation(var10, var4, var5, var13, var8, var12, var9)).refine();
            case 13:
               var13 = this.findDeploymentMBean(var8, var7, var6, false);
               return new RetireOperation(var10, var4, var5, var13, var8, var12, var9);
         }
      }
   }

   private void resetRequest(DeploymentRequestInfo var1) {
      synchronized(this.requestInfoTable) {
         if (var1 != null) {
            long var3 = var1.theRequestId;
            if (this.isDebugEnabled()) {
               this.debug("DeploymentManagerT: resetting request: " + var3 + "'");
            }

            var1.preDeploymentHandlerList.clear();
            var1.postDeploymentHandlerList.clear();
            var1.operationsMap.clear();
            var1.context = null;
            var1.configPrepareCompletedLatch.countDown();
            var1.configCommitCompletedLatch.countDown();
            this.requestInfoTable.remove(new Long(var3));
         }

      }
   }

   private BasicDeploymentMBean getDeploymentMBean(DomainMBean var1, String var2) {
      BasicDeploymentMBean var3 = null;
      if (var1 != null) {
         var3 = ApplicationVersionUtils.getDeployment(var1, var2);
      }

      return var3;
   }

   private void prepareDeploymentList(ArrayList var1, DeploymentContext var2) throws Throwable {
      DeploymentRequest var3 = var2.getDeploymentRequest();
      long var4 = var3.getId();
      DeploymentRequestInfo var6 = this.getDeploymentRequestInfo(var4);
      synchronized(var1) {
         if (var1 != null && var1.size() > 0) {
            if (this.isDebugEnabled()) {
               this.debug("DeploymentManagerT: prepareDeploymentList - handling deployment list of size: " + var1.size() + " for request '" + var4 + "'");
            }

            for(int var8 = 0; var8 < var1.size(); ++var8) {
               Deployment var9 = (Deployment)var1.get(var8);
               this.handleDeploymentPrepare(var9, var6);
            }
         } else if (this.isDebugEnabled()) {
            this.debug("DeploymentManagerT: prepareDeploymentList - nothing to do since list is empty for request '" + var4 + "'");
         }

      }
   }

   private void activateDeploymentList(ArrayList var1, DeploymentRequestInfo var2) throws Throwable {
      long var3 = var2.context.getDeploymentRequest().getId();
      synchronized(var1) {
         if (var1 != null && var1.size() > 0) {
            if (this.isDebugEnabled()) {
               this.debug("DeploymentManagerT: activateDeploymentList - handling deployment list of size: " + var1.size() + " for request '" + var3 + "'");
            }

            for(int var6 = 0; var6 < var1.size(); ++var6) {
               Deployment var7 = (Deployment)var1.get(var6);
               this.handleDeploymentCommit(var7, (AbstractOperation)var2.operationsMap.get(var7.getDeploymentTaskRuntimeId()));
            }
         } else if (this.isDebugEnabled()) {
            this.debug("DeploymentManagerT: activateDeploymentList - nothing to do since list is empty for request '" + var3 + "'");
         }

      }
   }

   private void assertDeploymentMBeanIsNonNull(DomainMBean var1, BasicDeploymentMBean var2, String var3, String var4) throws DeploymentException {
      if (var2 == null) {
         if (this.isDebugEnabled()) {
            this.debug(var4 + ": Could not find mbean for " + var3);
         }

         Loggable var5 = DeployerRuntimeLogger.logNullDeploymentMBeanLoggable(var4, var3);
         var5.log();
         throw new DeploymentException(var5.getMessage());
      }
   }

   private BasicDeploymentMBean findDeploymentMBean(DomainMBean var1, String var2, int var3, boolean var4) throws DeploymentException {
      String var6 = DeployHelper.getTaskName(var3);
      BasicDeploymentMBean var5;
      if (var4) {
         if (this.isDebugEnabled()) {
            this.debug(var6 + ": Trying to find mbean for " + var2 + " in proposed domain");
         }

         var5 = this.getDeploymentMBean(var1, var2);
         if (var5 != null) {
            if (this.isDebugEnabled()) {
               this.debug(var6 + ": Found MBean for " + var2 + " in proposed domain");
            }

            return var5;
         }
      }

      DomainMBean var7 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      if (this.isDebugEnabled()) {
         this.debug(var6 + ": Trying to find mbean for " + var2 + " in runtime domain");
      }

      var5 = this.getDeploymentMBean(var7, var2);
      this.assertDeploymentMBeanIsNonNull(var7, var5, var2, var6);
      if (this.isDebugEnabled()) {
         this.debug(var6 + ": Found mbean for " + var2 + " in runtime domain");
      }

      return var5;
   }

   private void createOperations(DeploymentContext var1) throws Exception {
      DeploymentRequest var2 = var1.getDeploymentRequest();
      long var3 = var2.getId();
      DeploymentRequestInfo var5 = this.getDeploymentRequestInfo(var3);
      if (var5 == null) {
         if (this.isDebugEnabled()) {
            this.debug("DeploymentManagerT: createOperations() for request '" + var3 + " after prepare completed");
         }

         String var9 = DeployerRuntimeLogger.requestCompletedOrCancelled(var3);
         this.dispatcher.notifyPrepareFailure(var3, new Exception(var9));
      } else {
         if (this.isDebugEnabled()) {
            this.debug("DeploymentManagerT: createOperations() for request: " + var3);
         }

         Iterator var6 = var2.getDeployments("Application");
         if (var6 != null) {
            while(var6.hasNext()) {
               Deployment var7 = (Deployment)var6.next();
               if (this.targetedToThisServer(var7)) {
                  AbstractOperation var8 = this.createOperation(var7, var5);
                  var8.stageFilesFromAdminServer(var7.getDataTransferHandlerType());
                  var5.operationsMap.put(var7.getDeploymentTaskRuntimeId(), var8);
                  if (this.isDebugEnabled()) {
                     this.debug("DeploymentManagerT.createOperations(): created operation for deployment: " + var7 + " of request id " + var2.getId() + " targeted on this server " + var8);
                  }
               } else if (this.isDebugEnabled()) {
                  this.debug("DeploymentManagerT.createOperations(): application deployment: " + var7 + " of request id: " + var2.getId() + " not targeted on this server");
               }
            }
         }

      }
   }

   private void notifyCommitSuccess(long var1, DeploymentRequestInfo var3) {
      this.resetRequest(var3);
      this.dispatcher.notifyCommitSuccess(var1);
   }

   private void notifyCommitFailure(long var1, DeploymentRequestInfo var3, Throwable var4) {
      this.resetRequest(var3);
      this.dispatcher.notifyCommitFailure(var1, DeployHelper.convertThrowableForTransfer(var4));
   }

   private void notifyCancelSuccess(long var1, DeploymentRequestInfo var3) {
      this.resetRequest(var3);
      this.dispatcher.notifyCancelSuccess(var1);
   }

   private void notifyCancelFailure(long var1, DeploymentRequestInfo var3, Throwable var4) {
      this.resetRequest(var3);
      this.dispatcher.notifyCancelFailure(var1, DeployHelper.convertThrowable(var4));
   }

   // $FF: synthetic method
   DeploymentManager(Object var1) {
      this();
   }

   private static class DeploymentRequestInfo {
      private final long theRequestId;
      private DeploymentContext context;
      private Map operationsMap;
      private ArrayList preDeploymentHandlerList;
      private ArrayList postDeploymentHandlerList;
      private Set systemResourcesToBeRestarted;
      private CountDownLatch configPrepareCompletedLatch;
      private CountDownLatch configCommitCompletedLatch;

      private DeploymentRequestInfo(long var1, DeploymentContext var3) {
         this.systemResourcesToBeRestarted = new HashSet();
         this.theRequestId = var1;
         this.context = var3;
         this.operationsMap = new HashMap();
         this.preDeploymentHandlerList = new ArrayList();
         this.postDeploymentHandlerList = new ArrayList();
         this.configPrepareCompletedLatch = new CountDownLatch(1);
         this.configCommitCompletedLatch = new CountDownLatch(1);
      }

      // $FF: synthetic method
      DeploymentRequestInfo(long var1, DeploymentContext var3, Object var4) {
         this(var1, var3);
      }
   }

   static class Maker {
      static final DeploymentManager MANAGER = new DeploymentManager();
   }
}
