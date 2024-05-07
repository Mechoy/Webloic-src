package weblogic.deploy.internal.adminserver;

import java.io.Serializable;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import weblogic.deploy.beans.factory.DeploymentBeanFactory;
import weblogic.deploy.common.Debug;
import weblogic.deploy.compatibility.NotificationBroadcaster;
import weblogic.deploy.internal.AggregateDeploymentVersion;
import weblogic.deploy.internal.Deployment;
import weblogic.deploy.internal.DeploymentVersion;
import weblogic.deploy.internal.adminserver.operations.AbstractOperation;
import weblogic.deploy.internal.adminserver.operations.OperationHelper;
import weblogic.deploy.internal.targetserver.state.DeploymentState;
import weblogic.deploy.service.ChangeDescriptor;
import weblogic.deploy.service.ConfigurationContext;
import weblogic.deploy.service.DeploymentException;
import weblogic.deploy.service.DeploymentFailureHandler;
import weblogic.deploy.service.DeploymentProvider;
import weblogic.deploy.service.DeploymentRequest;
import weblogic.deploy.service.FailureDescription;
import weblogic.deploy.service.RequiresRestartFailureDescription;
import weblogic.deploy.service.Version;
import weblogic.deploy.service.internal.adminserver.AdminDeploymentException;
import weblogic.logging.Loggable;
import weblogic.management.DeferredDeploymentException;
import weblogic.management.ManagementException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.SubDeploymentMBean;
import weblogic.management.configuration.SystemResourceMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.deploy.DeploymentData;
import weblogic.management.deploy.DeploymentTaskRuntime;
import weblogic.management.deploy.TargetStatus;
import weblogic.management.deploy.internal.AppRuntimeStateManager;
import weblogic.management.deploy.internal.AppRuntimeStateRuntimeMBeanImpl;
import weblogic.management.deploy.internal.ApplicationRuntimeState;
import weblogic.management.deploy.internal.DeployerRuntimeImpl;
import weblogic.management.deploy.internal.DeployerRuntimeLogger;
import weblogic.management.deploy.internal.DeploymentManagerLogger;
import weblogic.management.deploy.internal.DeploymentServerService;
import weblogic.management.provider.DomainAccess;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.DeploymentRequestTaskRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.utils.StackTraceUtils;

public final class DeploymentManager implements DeploymentProvider {
   private static final String DEPLOYMENT_SERVICE_CALLBACK_HANDLER_ID = "Application";
   private static final DeploymentManager theInstance = new DeploymentManager();
   private DeploymentServiceDriver driver;
   private AggregateDeploymentVersion adminServerAggregateDeploymentVersion;
   private static DeploymentBeanFactory beanFactory = DeploymentServerService.getDeploymentBeanFactory();
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private List pendingDeploymentsForLockOwner;
   private List pendingDeploymentsForLockAcquirer;
   private Map pendingControlDeployments;
   private Map requestInfoTable;
   private EditAccessHelper editAccessHelper;
   private DeployerRuntimeImpl deployerRuntime;
   private Map taskRuntimeToDeploymentTable;
   private Map taskRuntimeToDeploymentInfoTable;
   private static boolean initialized = false;

   private DeploymentManager() {
   }

   public static DeploymentManager getInstance(AuthenticatedSubject var0) {
      SecurityServiceManager.checkKernelIdentity(var0);
      return theInstance;
   }

   private void loadAppRuntimeStates() throws ManagementException {
      AppRuntimeStateManager.getManager().loadStartupState((Map)null);
      Map var1 = AppRuntimeStateManager.getManager().getDeploymentVersions();
      if (var1 != null) {
         Iterator var2 = var1.entrySet().iterator();

         while(var2.hasNext()) {
            Map.Entry var3 = (Map.Entry)var2.next();
            String var4 = (String)var3.getKey();
            DeploymentVersion var5 = (DeploymentVersion)var3.getValue();
            this.addOrUpdateAdminServerAggregateDeploymentVersion(var4, var5);
         }
      }

   }

   public void initialize() throws ManagementException {
      if (!initialized) {
         this.loadAppRuntimeStates();
         this.driver = DeploymentServiceDriver.getInstance();
         DomainAccess var1 = ManagementService.getDomainAccess(kernelId);
         this.deployerRuntime = (DeployerRuntimeImpl)var1.getDeployerRuntime();
         this.driver.initialize("Application", this.getAdminServerAggregateDeploymentsVersion(), this);
         this.requestInfoTable = new HashMap();
         this.pendingDeploymentsForLockOwner = new ArrayList();
         this.pendingDeploymentsForLockAcquirer = new ArrayList();
         this.pendingControlDeployments = new HashMap();
         this.taskRuntimeToDeploymentTable = new HashMap();
         this.taskRuntimeToDeploymentInfoTable = new HashMap();
         this.editAccessHelper = EditAccessHelper.getInstance(kernelId);
         AppRuntimeStateRuntimeMBeanImpl.initialize();
      }
   }

   public static void shutdown() {
      DeploymentServiceDriver.getInstance().shutdown();
   }

   public DeployerRuntimeImpl getDeployerRuntime() {
      return this.deployerRuntime;
   }

   public EditAccessHelper getEditAccessHelper(AuthenticatedSubject var1) {
      SecurityServiceManager.checkKernelIdentity(var1);
      return this.editAccessHelper;
   }

   public void restartSystemResource(SystemResourceMBean var1) throws ManagementException {
      ConfigChangesHandler.restartSystemResource(var1);
   }

   public String getIdentity() {
      return "Application";
   }

   public synchronized void addDeploymentsTo(DeploymentRequest var1, ConfigurationContext var2) {
      List[] var3;
      if (this.pendingDeploymentsForLockAcquirer.isEmpty()) {
         var3 = ConfigChangesHandler.configChanged(var1, var2);
      } else {
         var3 = new List[]{this.pendingDeploymentsForLockAcquirer};
      }

      this.updateRequestAndDeployments(var1, var3);
      this.clearPendingDeployments();
   }

   private void clearPendingDeployments() {
      this.pendingDeploymentsForLockAcquirer.clear();
      this.pendingDeploymentsForLockOwner.clear();
   }

   private void updateRequestAndDeployments(DeploymentRequest var1, List[] var2) {
      if (var2 != null && var2.length != 0) {
         DeploymentRequestInfo var3 = null;
         boolean var4 = true;
         boolean var5 = true;
         AuthenticatedSubject var6 = null;

         for(int var7 = 0; var7 < var2.length; ++var7) {
            List var8 = var2[var7];
            if (var8 != null && !var8.isEmpty()) {
               Iterator var9 = var8.iterator();

               while(var9.hasNext()) {
                  if (var3 == null) {
                     var3 = new DeploymentRequestInfo(var1);
                  }

                  Deployment var10 = (Deployment)var9.next();
                  var4 = var10.isCallerLockOwner();
                  var5 = var10.isAControlOperation();
                  var6 = var10.getInitiator();
                  var1.addDeployment(var10);
                  var10.setDeploymentRequestIdentifier(var1.getId());
                  DeploymentTaskRuntime var11 = var10.getDeploymentTaskRuntime();
                  String var12 = var11.getId();
                  var1.getTaskRuntime().addDeploymentRequestSubTask(var11, var12);
                  DeploymentData var13 = var11.getDeploymentData();
                  if (var13 != null) {
                     var1.setTimeoutInterval((long)var13.getTimeOut());
                  }

                  var3.addDeploymentStatusContainerFor(var12);
                  if (var10.isAnAppDeployment()) {
                     var10.setStaged(var11.getAppDeploymentMBean().getStagingMode());
                  }

                  this.taskRuntimeToDeploymentInfoTable.put(var12, var3);
                  this.taskRuntimeToDeploymentTable.put(var12, var10);
               }
            }
         }

         if (var3 != null) {
            var1.setInitiator(var6);
            if (!var4) {
               var1.setStartControl(true);
            }

            var1.setControlRequest(var5);
            if (!var5) {
               var3.setIsEditLockOwner(var4);
               if (Debug.isDeploymentDebugEnabled()) {
                  Debug.deploymentDebug("Request '" + var1.getId() + "' initiated by '" + var6 + "'");
               }

               DomainMBean var16 = this.editAccessHelper.getEditDomainBean(var6);
               var3.setEditableDomain(var16);
            }

            synchronized(this.requestInfoTable) {
               this.requestInfoTable.put(new Long(var1.getId()), var3);
            }
         }
      }
   }

   public Collection getPendingDeploymentsForEditLockOwner() {
      synchronized(this.pendingDeploymentsForLockOwner) {
         if (!this.pendingDeploymentsForLockOwner.isEmpty() && this.pendingDeploymentsForLockOwner.size() > 0) {
            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug("DeploymentManager: getPendingDeployments returning " + this.pendingDeploymentsForLockOwner.size() + " deployments");
            }

            ArrayList var2 = new ArrayList();
            var2.addAll(this.pendingDeploymentsForLockOwner);
            return var2;
         } else {
            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug("DeploymentManager: getPendingDeployments no deployments to return");
            }

            return null;
         }
      }
   }

   public DomainMBean getEditableDomainMBean(long var1) {
      DeploymentRequestInfo var3 = this.getDeploymentRequestInfo(var1);
      if (var3 == null) {
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("DeploymentManager: getEditableDomainMBean() ' requestInfo for request '" + var1 + "' is null");
         }

         return null;
      } else {
         DomainMBean var4 = var3.editableDomain;
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("DeploymentManager: getEditableDomainMBean() ' editableDomain for request '" + var1 + "' is " + var4);
         }

         return var4;
      }
   }

   private static String getCompositeKey(String var0, int var1) {
      return var0 + "###" + Integer.toString(var1);
   }

   private boolean lockOwnerMatchesPendingDeploymentsLockOwner(boolean var1) {
      synchronized(this.pendingDeploymentsForLockAcquirer) {
         if (var1 && !this.pendingDeploymentsForLockAcquirer.isEmpty()) {
            return false;
         }
      }

      synchronized(this.pendingDeploymentsForLockOwner) {
         return var1 || this.pendingDeploymentsForLockOwner.isEmpty();
      }
   }

   private static int getCanonicalOperation(int var0) {
      int var1 = var0;
      switch (var0) {
         case 1:
         case 6:
         case 11:
            var1 = 1;
         case 2:
         case 3:
         case 5:
         case 7:
         case 8:
         case 10:
         default:
            break;
         case 4:
         case 12:
            var1 = 4;
            break;
         case 9:
            var1 = 9;
      }

      return var1;
   }

   public synchronized Deployment createDeployment(String var1, DeploymentData var2, int var3, DeploymentTaskRuntime var4, DomainMBean var5, boolean var6, AuthenticatedSubject var7, boolean var8, boolean var9, boolean var10) throws ManagementException {
      if (!var9 && !this.lockOwnerMatchesPendingDeploymentsLockOwner(var8)) {
         String var18 = DeployerRuntimeLogger.configLocked();
         throw new ManagementException(var18);
      } else {
         Deployment var11 = this.createAndInitializeDeployment(var4, var5, var2, var3, var7, var8, var9, var10);
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("DeploymentManager: deployment '" + var11 + "' initiated by '" + var7 + "'");
         }

         if (var9) {
            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug("DeploymentManager: added deployment: " + var11 + " to list of control opeartions");
            }

            this.pendingControlDeployments.put(var4.getId(), var11);
         } else if (var8) {
            if (var6) {
               if (Debug.isDeploymentDebugEnabled()) {
                  Debug.deploymentDebug("DeploymentManager: not adding config change: " + var11 + " to list of pending  deployments for edit " + "lock owner");
               }
            } else {
               synchronized(this.pendingDeploymentsForLockOwner) {
                  if (Debug.isDeploymentDebugEnabled()) {
                     Debug.deploymentDebug("DeploymentManager: added deployment: " + var11 + " to list of pending deployments for edit " + "lock owner");
                  }

                  boolean var13 = this.removeDeployIfNoop(var11);
                  if (!var13) {
                     this.pendingDeploymentsForLockOwner.add(var11);
                  }
               }
            }
         } else {
            synchronized(this.pendingDeploymentsForLockAcquirer) {
               if (Debug.isDeploymentDebugEnabled()) {
                  Debug.deploymentDebug("DeploymentManager: added deployment: " + var11 + " to list of pending deployments for edit " + "lock acquirer");
               }

               this.pendingDeploymentsForLockAcquirer.add(var11);
            }
         }

         return var11;
      }
   }

   private Deployment createAndInitializeDeployment(DeploymentTaskRuntime var1, DomainMBean var2, DeploymentData var3, int var4, AuthenticatedSubject var5, boolean var6, boolean var7, boolean var8) {
      Deployment var9 = this.createDeployment(var1, var1.getDeploymentData(), var2, var5, var6, var7, var8);
      Map var10 = this.getAdminServerAggregateDeploymentsVersion().getVersionComponents();
      AggregateDeploymentVersion var11 = AggregateDeploymentVersion.createAggregateDeploymentVersion(var10);
      DeploymentVersion var12 = null;
      if (var1.getAppDeploymentMBean() != null) {
         boolean var13 = isAConfigurationChange(var1.getAppDeploymentMBean(), var3, var4);
         var12 = this.createDeploymentVersion(var9.getIdentity(), var13, var7);
         if (var4 != 4 && var4 != 12) {
            var11.addOrUpdateDeploymentVersion(var9.getIdentity(), var12);
         } else {
            var11.removeDeploymentVersionFor(var9.getIdentity());
         }
      }

      var9.setProposedVersion(var11);
      var9.setProposedDeploymentVersion(var12);
      ChangeDescriptor var14 = this.createChangeDescriptor(var9.getIdentity(), var12);
      var9.addChangeDescriptor(var14);
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("DeploymentManager: Created Deployment : '" + var9.toString() + "'");
      }

      return var9;
   }

   private DeploymentRequestInfo getDeploymentRequestInfo(long var1) {
      synchronized(this.requestInfoTable) {
         return (DeploymentRequestInfo)this.requestInfoTable.get(new Long(var1));
      }
   }

   public void deploymentRequestSucceeded(long var1, FailureDescription[] var3) {
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("Received deployment request success:  for deployment request id: " + var1);
      }

      DeploymentRequestInfo var4 = this.getDeploymentRequestInfo(var1);
      if (var4 == null) {
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("deploymentRequestSucceeded(): Request id: " + var1 + " has completed or timed out since there is no " + "active current deployment");
         }
      } else {
         synchronized(var4) {
            try {
               this.releaseEditLock(var4);
            } finally {
               this.updateTasksWithAccumulatedStatus(var1);
               byte var8 = 3;
               ArrayList var9 = new ArrayList();
               if (var3 != null && var3.length > 0) {
                  for(int var10 = 0; var10 < var3.length; ++var10) {
                     FailureDescription var11 = var3[var10];
                     if (!(var11 instanceof RequiresRestartFailureDescription)) {
                        var8 = 4;
                        String var12 = var3[var10].getAttemptedOperation();
                        String var13 = var3[var10].getServer();
                        DeferredDeploymentException var14 = new DeferredDeploymentException(var3[var10].getReason());
                        var9.add(new FailureDescription(var13, var14, var12));
                     }
                  }
               }

               FailureDescription[] var20 = new FailureDescription[var9.size()];
               var20 = (FailureDescription[])((FailureDescription[])var9.toArray(var20));
               this.updateTasksWithDeploymentStatus(var1, var20, var8, false);
               this.updateAdminServerRuntimeStateAndAggregateDeploymentVersion(var4);
            }
         }
      }

   }

   private void updateTasksWithDeploymentStatus(long var1, FailureDescription[] var3, int var4, boolean var5) {
      DeploymentRequestInfo var6 = this.getDeploymentRequestInfo(var1);
      if (var6 == null) {
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("DeploymentManager: updateTasksWithDeploymentStatus() couldn't find requestInfo for request : " + var1);
         }

      } else {
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("DeploymentManager: updateTasksWithDeploymentStatus for request : " + var1 + " with status: " + var4);
         }

         synchronized(var6) {
            DeploymentRequest var8 = var6.request;
            Iterator var9 = var8.getDeployments("Application");

            while(true) {
               DeploymentTaskRuntime var11;
               do {
                  if (!var9.hasNext()) {
                     return;
                  }

                  Deployment var10 = (Deployment)var9.next();
                  var11 = var10.getDeploymentTaskRuntime();
                  if (Debug.isDeploymentDebugEnabled()) {
                     StringBuffer var12 = new StringBuffer();
                     if (var3 != null) {
                        var12.append(Arrays.asList(var3));
                     } else {
                        var12.append("null");
                     }

                     Debug.deploymentDebug("DeploymentManager: Updating task '" + var11.getId() + "' with failures : " + var12.toString());
                  }

                  updateTaskWithFailures(var11, var3, var4);
                  if (!var5 && (var4 == 3 || var4 == 4)) {
                     this.updateTaskStatusOfRestartTargets(var1, var11, var8.getTaskRuntime().getServersToBeRestarted());
                  }

                  if (!var5 && var4 == 2) {
                     if (Debug.isDeploymentDebugEnabled()) {
                        Debug.deploymentDebug("DeploymentManager: Calling handleFailure on task '" + var11.getId() + "'");
                     }

                     var11.handleFailure();
                  }

                  if (var5) {
                     var11.updatePendingServersWithSuccess();
                  }
               } while(!var5 && var4 != 2);

               this.reset(var11.getId());
            }
         }
      }
   }

   private void releaseEditLock(DeploymentRequestInfo var1) {
      if (!var1.isControlOperation()) {
         long var2 = var1.request.getId();
         boolean var4 = var1.ownsEditLock;
         AuthenticatedSubject var5 = var1.request.getInitiator();
         beanFactory.resetDeployerInitiatedBeanUpdates();
         if (var5 != null && !var4) {
            try {
               if (Debug.isDeploymentDebugEnabled()) {
                  Debug.deploymentLogger.debug("DeploymentManager: stopping edit session for request id: " + var2);
               }

               this.editAccessHelper.stopEditSession(var5);
            } catch (ManagementException var7) {
               if (Debug.isDeploymentDebugEnabled()) {
                  Debug.deploymentLogger.debug("Failed to stop edit session on a successful deployment of request id: " + var2);
               }
            }
         }

      }
   }

   private void deploymentRequestFailedBeforeStart(DeploymentRequestInfo var1, DeploymentException var2) {
      if (var1 != null) {
         long var3 = var1.getRequestId();
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("Received deployment request failure : " + var2.toString() + " before start for request id: " + var3);
         }

         this.clearPendingDeployments();
         synchronized(var1) {
            DeploymentRequest var6 = var1.request;
            Set var7 = var6.getRegisteredFailureListeners();
            if (var7 != null) {
               Iterator var8 = var7.iterator();

               while(var8.hasNext()) {
                  DeploymentFailureHandler var9 = (DeploymentFailureHandler)var8.next();
                  var9.deployFailed(var3, var2);
               }
            }

            this.deploymentRequestFailed(var3, (DeploymentException)null, var2.getFailures());
         }
      }
   }

   public void deploymentRequestFailed(long var1, DeploymentException var3, FailureDescription[] var4) {
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("Received deployment request failure: " + var3 + " for " + "deployment request id: " + var1);
      }

      DeploymentRequestInfo var5 = this.getDeploymentRequestInfo(var1);
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("DeploymentManager.deploymentRequestFailed(): requestInfo : " + var5);
      }

      if (var5 == null) {
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("deploymentRequestFailed(): Request id: " + var1 + " has completed, timed out or was never started " + "since there is no active deployment information for this request");
         }
      } else {
         synchronized(var5) {
            this.undoUnactivatedChanges(var5);
            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug("DeploymentManager.deploymentRequestFailed(): Calling updateTasksWithDeploymentStatus() for request '" + var1 + "' with status failed");
            }

            this.updateTasksWithDeploymentStatus(var1, var4, 2, false);
         }
      }

      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("DeploymentManager.deploymentRequestFailed(): Returning from deploymentRequestFailed()...");
      }

   }

   public void undoChangesTriggeredByUser(Deployment var1) {
      this.pendingDeploymentsForLockOwner.remove(var1);
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("DeploymentManager: Removed entry: '" + var1 + "' from pendingDeploymentsForLockOwner if has one");
      }

   }

   private void undoUnactivatedChanges(DeploymentRequestInfo var1) {
      if (!var1.isControlOperation()) {
         long var2 = var1.request.getId();
         boolean var4 = var1.ownsEditLock;
         AuthenticatedSubject var5 = var1.request.getInitiator();
         if (var5 != null && !var4) {
            try {
               if (Debug.isDeploymentDebugEnabled()) {
                  Debug.deploymentDebug("DeploymentManager: undoUnactivatedChanges for request: " + var2);
               }

               this.editAccessHelper.undoUnactivatedChanges(var5);
            } catch (ManagementException var18) {
               Debug.deploymentLogger.debug("Failed to undo unactivated changes on a failed deployment of request id: " + var2 + " due to " + StackTraceUtils.throwable2StackTrace(var18));
            } catch (Throwable var19) {
               Debug.deploymentLogger.debug("Failed to undo unactivated changes on a failed deployment of request id: " + var2 + " due to " + StackTraceUtils.throwable2StackTrace(var19));
            } finally {
               try {
                  this.editAccessHelper.stopEditSession(var5);
               } catch (Exception var17) {
                  Debug.deploymentLogger.debug("Failed to stop edit session on a failed deployment of request id: " + var2 + " due to " + StackTraceUtils.throwable2StackTrace(var17));
               }

            }
         } else if (var5 != null) {
            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug("DeploymentManager: rollbackFailedTasks for request: " + var2);
            }

            this.rollBackFailedTasks(var2, var5);
         }

      }
   }

   private void rollBackFailedTasks(long var1, AuthenticatedSubject var3) {
      DeploymentRequestInfo var4 = this.getDeploymentRequestInfo(var1);
      if (var4 != null) {
         synchronized(var4) {
            DeploymentRequest var6 = var4.request;
            Iterator var7 = var6.getDeployments("Application");

            while(var7.hasNext()) {
               Deployment var8 = (Deployment)var7.next();
               DeploymentTaskRuntime var9 = var8.getDeploymentTaskRuntime();
               AbstractOperation var10 = var9.getAdminOperation();
               if (var10 != null) {
                  if (Debug.isDeploymentDebugEnabled()) {
                     Debug.deploymentDebug("DeploymentManager: rollback for task " + var9.getId());
                  }

                  var10.rollback(var3);
               }
            }

         }
      }
   }

   public Deployment[] getDeployments(Version var1, Version var2, String var3) {
      Deployment var4 = new Deployment();
      var4.setProposedVersion(this.getAdminServerAggregateDeploymentsVersion());
      var4.setCallbackHandlerId(this.driver.getHandlerIdentity());
      var4.enableSyncWithAdmin(AppRuntimeStateManager.getManager().getStartupStateForServer(var3));
      Deployment[] var5 = new Deployment[]{var4};
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("DeploymentManager: getDeployments from '" + var3 + "' returning '" + var5 + "'");
      }

      return var5;
   }

   public void deploymentRequestCommitFailed(long var1, FailureDescription[] var3) {
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("Received 'commit failure' for request id '" + var1 + "''");
      }

      DeploymentRequestInfo var4 = this.getDeploymentRequestInfo(var1);
      if (var4 != null) {
         synchronized(var4) {
            this.updateTasksWithDeploymentStatus(var1, var3, 2, true);
         }
      }
   }

   public void deploymentRequestCommitSucceeded(long var1) {
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("Received 'commit success' for request id '" + var1 + "''");
      }

      DeploymentRequestInfo var3 = this.getDeploymentRequestInfo(var1);
      if (var3 != null) {
         synchronized(var3) {
            this.updateTasksWithDeploymentStatus(var1, (FailureDescription[])null, 3, true);
         }
      }
   }

   public void deploymentRequestCancelSucceeded(long var1, FailureDescription[] var3) {
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("Request id '" + var1 + "' cancel succeeded");
      }

      this.updateTaskWithCancelState(var1, 8);
   }

   public void deploymentRequestCancelFailed(long var1, DeploymentException var3, FailureDescription[] var4) {
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("Request id '" + var1 + "' cancel failed " + "due to '" + var3.toString());
      }

      this.updateTaskWithCancelState(var1, 4);
   }

   public void handleReceivedStatus(long var1, DeploymentState var3, String var4) {
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("DeploymentManager: handleReceivedStatus for: " + var3 + " from " + var4 + " for request: " + var1);
      }

      DeploymentTaskRuntime var5 = this.getDeploymentTaskRuntime(var3);
      if (var5 == null) {
         if (var3 != null) {
            if ("__Lifecycle_taskid__".equals(var3.getTaskID())) {
               Debug.deploymentLogger.debug("DeploymentManager: handleReceivedStatus received status " + var3 + " for " + "lifecycle task from " + var4);
               this.updateRuntimeState(var3);
            } else {
               Deployment var6 = (Deployment)this.taskRuntimeToDeploymentTable.get(var3.getTaskID());
               if (var6 != null) {
                  var5 = var6.getDeploymentTaskRuntime();
               }

               if (var5 != null) {
                  this.updateRuntimeState(var3);
               } else {
                  Debug.deploymentLogger.debug("DeploymentManager: handleReceivedStatus ignoring received status " + var3 + " for non" + "existing task from " + var4);
                  this.updateRuntimeState(var3);
               }
            }
         }

      } else {
         this.updateStatus(var1, var5, var4, var3);
      }
   }

   private DeploymentTaskRuntime getDeploymentTaskRuntime(DeploymentState var1) {
      DeploymentTaskRuntime var2 = null;
      if (var1 != null) {
         var2 = this.getDeploymentTaskRuntime(var1.getTaskID());
      }

      return var2;
   }

   private DeploymentTaskRuntime getDeploymentTaskRuntime(String var1) {
      return (DeploymentTaskRuntime)this.deployerRuntime.query(var1);
   }

   public void startDeploymentTask(DeploymentTaskRuntime var1, String var2) throws ManagementException {
      if (var1 != null) {
         Deployment var3 = (Deployment)this.taskRuntimeToDeploymentTable.get(var1.getId());
         if (var3 == null) {
            var3 = (Deployment)this.pendingControlDeployments.remove(var1.getId());
            if (var3 == null) {
               if (Debug.isDeploymentDebugEnabled()) {
                  Debug.deploymentDebug("No deployment object available for task with id: " + var1.getId());
               }

               return;
            }
         }

         var3.setNotificationLevel(var1.getNotificationLevel());
         long var4 = var3.getDeploymentRequestId();
         DeploymentRequestInfo var6 = this.getDeploymentRequestInfo(var4);
         if (!var1.isAControlOperation() && var3.isCallerLockOwner()) {
            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug("Caller owns the edit lock for the task with id : " + var1.getId());
            }

         } else {
            try {
               DeploymentRequestTaskRuntimeMBean var7 = (DeploymentRequestTaskRuntimeMBean)var1.getMyParent();
               if (var7 != null) {
                  var7.start();
               } else if (var1.isAControlOperation()) {
                  this.startDeploymentForControlOperation(var1, var3);
               } else {
                  throw new AssertionError("DeploymentTaskRuntime: " + this + " does not have an associated parent DeploymentRequestTaskRuntime");
               }
            } catch (Exception var11) {
               ManagementException var8 = var11 instanceof ManagementException ? (ManagementException)var11 : new ManagementException(var11.getMessage(), var11);
               AuthenticatedSubject var9 = null;
               if (var6 != null && var6.request != null) {
                  var9 = var6.request.getInitiator();
               }

               this.deploymentFailedBeforeStart(var3, var8, var3.isCallerLockOwner(), var9, var1.isAControlOperation());
               String var10 = "An exception occurred while executing task " + var1.getDescription() + " for application " + var2 + ":" + StackTraceUtils.throwable2StackTrace(var8);
               if (Debug.isDeploymentDebugEnabled()) {
                  Debug.deploymentDebug(var10);
               }

               throw var8;
            }
         }
      }
   }

   public final void deploymentFailedBeforeStart(Deployment var1, Throwable var2, boolean var3, AuthenticatedSubject var4, boolean var5) {
      beanFactory.resetDeployerInitiatedBeanUpdates();
      this.deploymentFailedBeforeStart(var1, var2);
      this.abortSessionBeforeStart(var4, var3, var5);
      if (var1 != null) {
         String var6 = var1.getInternalDeploymentData().getDeploymentName();
         int var7 = var1.getInternalDeploymentData().getDeploymentOperation();
         OperationHelper.logTaskFailed(var6, var7);
      }
   }

   public final void deploymentRequestCancelledBeforeStart(DeploymentRequest var1) {
      if (var1 != null) {
         long var2 = var1.getId();
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("Request id '" + var2 + "' cancelled " + "before start");
         }

         DeploymentRequestInfo var4 = this.getDeploymentRequestInfo(var2);
         if (var4 != null) {
            synchronized(var4) {
               boolean var6 = var4.ownsEditLock;
               AuthenticatedSubject var7 = var4.request.getInitiator();
               boolean var8 = var4.isControlOperation();
               if (Debug.isDeploymentDebugEnabled()) {
                  Debug.deploymentDebug("calling abortEditSessionBeforeStart() for requestId : " + var2);
               }

               this.abortSessionBeforeStart(var7, var6, var8);
               Iterator var9 = var4.request.getDeployments("Application");

               while(var9.hasNext()) {
                  Deployment var10 = (Deployment)var9.next();
                  DeploymentTaskRuntime var11 = var10.getDeploymentTaskRuntime();
                  if (var11 != null && !var11.isComplete()) {
                     var11.setCancelState(8);
                  }
               }

            }
         }
      }
   }

   public final void deploymentFailedBeforeStart(Deployment var1, Throwable var2) {
      if (var1 != null) {
         AdminDeploymentException var3 = new AdminDeploymentException();
         FailureDescription var4 = new FailureDescription("adminServer", new Exception(var2.toString()), OperationHelper.getTaskString(var1.getInternalDeploymentData().getDeploymentOperation()));
         var3.addFailureDescription(var4);
         DeploymentRequestInfo var5 = this.getDeploymentRequestInfo(var1.getDeploymentRequestId());
         if (var5 != null) {
            this.deploymentRequestFailedBeforeStart(var5, var3);
         } else {
            DeploymentTaskRuntime var6 = var1.getDeploymentTaskRuntime();
            if (var6 == null) {
               return;
            }

            this.pendingControlDeployments.remove(var6.getId());
            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug("DeploymentManager: Removed task '" + var6.getId() + "' from pendingControlDeployments if has one");
            }

            this.pendingDeploymentsForLockOwner.remove(var1);
            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug("DeploymentManager: Removed entry: '" + var1 + "' from pendingDeploymentsForLockOwner if has one");
            }

            this.pendingDeploymentsForLockAcquirer.remove(var1);
            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug("DeploymentManager: Removed deployment '" + var1 + "' from pendingDeploymentsForLockAcquirer if has one");
            }

            FailureDescription[] var7 = var3.getFailures();
            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug("DeploymentManager: Updating task '" + var6.getId() + "' with failures : " + var7);
            }

            updateTaskWithFailures(var6, var7, 2);
            var6.handleFailure();
            this.reset(var6.getId());
         }

      }
   }

   public final List getExistingOperationsOnSameApp(AbstractOperation var1) {
      ArrayList var2 = new ArrayList();
      DeploymentTaskRuntime var3 = var1.getTaskRuntime();
      BasicDeploymentMBean var4 = var3.getDeploymentMBean();
      synchronized(this.pendingDeploymentsForLockOwner) {
         Iterator var6 = this.pendingDeploymentsForLockOwner.iterator();

         while(var6.hasNext()) {
            Deployment var7 = (Deployment)var6.next();
            DeploymentTaskRuntime var8 = var7.getDeploymentTaskRuntime();
            if (var8 != null) {
               BasicDeploymentMBean var9 = var8.getDeploymentMBean();
               if (var9 != null && var4 != null && var9.getName().equals(var4.getName())) {
                  AbstractOperation var10 = var8.getAdminOperation();
                  if (var10 != null) {
                     var2.add(var10);
                  }
               }
            }
         }

         return var2;
      }
   }

   public final List getExistingOperationsOnApp(String var1) {
      ArrayList var2 = new ArrayList();
      synchronized(this.pendingDeploymentsForLockOwner) {
         Iterator var4 = this.pendingDeploymentsForLockOwner.iterator();

         while(var4.hasNext()) {
            Deployment var5 = (Deployment)var4.next();
            DeploymentTaskRuntime var6 = var5.getDeploymentTaskRuntime();
            if (var6 != null) {
               BasicDeploymentMBean var7 = var6.getDeploymentMBean();
               if (var7 != null && var7.getName().equals(var1)) {
                  AbstractOperation var8 = var6.getAdminOperation();
                  if (var8 != null) {
                     var2.add(var8);
                  }
               }
            }
         }

         return var2;
      }
   }

   public final void removeDeploymentsForTasks(List var1) {
      if (var1 != null && !var1.isEmpty()) {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            synchronized(this.pendingDeploymentsForLockOwner) {
               Deployment var5 = this.findDeploymentForTask(var3);
               if (Debug.isDeploymentDebugEnabled()) {
                  Debug.deploymentDebug("Found deployment for '" + var3 + "' : " + var5);
               }

               if (var5 != null) {
                  if (Debug.isDeploymentDebugEnabled()) {
                     Debug.deploymentDebug("Removing deployment for '" + var3 + "' : " + var5);
                  }

                  this.pendingDeploymentsForLockOwner.remove(var5);
               }
            }

            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug("Removing task '" + var3 + "' and it's corresponding deployments...");
            }

            this.taskRuntimeToDeploymentTable.remove(var3);
            this.taskRuntimeToDeploymentInfoTable.remove(var3);
         }

      }
   }

   private synchronized Deployment findDeploymentForTask(String var1) {
      Iterator var2 = this.pendingDeploymentsForLockOwner.iterator();

      Deployment var3;
      DeploymentTaskRuntime var4;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (Deployment)var2.next();
         var4 = var3.getDeploymentTaskRuntime();
      } while(var4 == null || !var4.getId().equals(var1));

      return var3;
   }

   private final void startDeploymentForControlOperation(DeploymentTaskRuntime var1, Deployment var2) throws ManagementException {
      if (var1 != null && var2 != null) {
         String[] var3 = var2.getTargets();
         if (var3 != null && var3.length != 0) {
            DeploymentRequest var4 = this.driver.createDeploymentRequest();
            ArrayList var5 = new ArrayList();
            var5.add(var2);
            List[] var6 = new List[]{var5};
            this.updateRequestAndDeployments(var4, var6);
            DeploymentRequestInfo var7 = this.getDeploymentRequestInfo(var4.getId());
            var7.setControlOperation();
            var4.getTaskRuntime().start();
         } else {
            var1.updateTargetStatus((String)null, 3, (Exception)null);
         }
      }
   }

   private void resetDeploymentState(long var1) {
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("Resetting deployment(s) with request id: " + var1);
      }

      synchronized(this.requestInfoTable) {
         this.requestInfoTable.remove(new Long(var1));
      }
   }

   private void reset(String var1) {
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("Resetting deployment task ref " + var1);
      }

      this.taskRuntimeToDeploymentTable.remove(var1);
      DeploymentRequestInfo var2 = (DeploymentRequestInfo)this.taskRuntimeToDeploymentInfoTable.remove(var1);
      if (var2 != null) {
         synchronized(var2) {
            var2.removeDeploymentStatusContainerFor(var1);
            if (var2.removedAllStatusContainers()) {
               this.resetDeploymentState(var2.request.getId());
            }

         }
      }
   }

   private void addOrUpdateAdminServerAggregateDeploymentVersion(String var1, DeploymentVersion var2) {
      this.getAdminServerAggregateDeploymentsVersion().addOrUpdateDeploymentVersion(var1, var2);
   }

   private void updateAdminServerRuntimeStateAndAggregateDeploymentVersion(DeploymentRequestInfo var1) {
      Iterator var2 = var1.request.getDeployments("Application");

      while(true) {
         Deployment var3;
         do {
            do {
               if (!var2.hasNext()) {
                  return;
               }

               var3 = (Deployment)var2.next();
            } while(!var3.isAnAppDeployment());
         } while(var3.isAControlOperation());

         int var4 = var3.getOperation();
         boolean var5 = var4 != 4 && var4 != 12;
         if (var5) {
            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug("Updating runtime state and aggregate deployment version component for " + var3.getIdentity());
            }

            boolean var6 = !var1.request.isControlRequest();
            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug("Needs DeploymentVersion Update : " + var6);
            }

            if (var6) {
               updateRuntimeState(var3.getIdentity(), var3.getProposedDeploymentVersion());
               this.addOrUpdateAdminServerAggregateDeploymentVersion(var3.getIdentity(), var3.getProposedDeploymentVersion());
            }
         } else {
            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug("Removing runtime state and aggregate deployment version component for " + var3.getIdentity());
            }

            this.getAdminServerAggregateDeploymentsVersion().removeDeploymentVersionFor(var3.getIdentity());
         }
      }
   }

   private Deployment createDeployment(DeploymentTaskRuntime var1, DeploymentData var2, DomainMBean var3, AuthenticatedSubject var4, boolean var5, boolean var6, boolean var7) {
      Deployment var8 = new Deployment(this.driver.getHandlerIdentity(), var1, var2, var4, var5, var6, var7);
      TargetStatus[] var9 = var1.getTargets();
      if (var9 != null) {
         for(int var10 = 0; var10 < var9.length; ++var10) {
            String var11 = var9[var10].getTarget();
            String[] var12 = getServerNames(var11, var3);

            for(int var13 = 0; var13 < var12.length; ++var13) {
               var8.addTarget(var12[var13]);
            }
         }
      }

      return var8;
   }

   private DeploymentVersion createDeploymentVersion(String var1, boolean var2, boolean var3) {
      DeploymentVersion var4 = null;
      Map var5 = this.getAdminServerAggregateDeploymentsVersion().getVersionComponents();
      if (var5.size() > 0) {
         var4 = (DeploymentVersion)var5.get(var1);
      }

      long var7 = System.currentTimeMillis();
      long var9 = var7;
      if (var4 != null) {
         var7 = var4.getArchiveTimeStamp();
         var9 = var4.getPlanTimeStamp();
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("DeploymentManager: create deployment version for id: " + var1 + " version: " + var4 + " archive ts: " + var7 + " plan ts: " + var9);
         }
      } else if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("DeploymentManager: create deployment version for id: " + var1 + " archive ts: " + var7 + " plan ts: " + var7);
      }

      DeploymentVersion var6 = new DeploymentVersion(var1, var7, var9);
      var6.update(var2, var3);
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("DeploymentManager: update version: " + var4);
      }

      return var6;
   }

   private ChangeDescriptor createChangeDescriptor(Serializable var1, DeploymentVersion var2) {
      return this.driver.createChangeDescriptor(var1, var2);
   }

   private AggregateDeploymentVersion getAdminServerAggregateDeploymentsVersion() {
      if (this.adminServerAggregateDeploymentVersion == null) {
         this.adminServerAggregateDeploymentVersion = AggregateDeploymentVersion.createAggregateDeploymentVersion();
      }

      return this.adminServerAggregateDeploymentVersion;
   }

   private static String[] getServerNames(String var0, DomainMBean var1) {
      if (var1 != null) {
         TargetMBean var2 = var1.lookupTarget(var0);
         Object var3 = new HashSet();
         if (var2 != null) {
            var3 = var2.getServerNames();
         }

         return (String[])((String[])((Set)var3).toArray(new String[0]));
      } else {
         return new String[0];
      }
   }

   private static boolean isAlreadyTargeted(String var0) {
      DomainMBean var1 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      AppDeploymentMBean var2 = var1.lookupAppDeployment(var0);
      if (var2 != null) {
         if (var2.getTargets() != null && var2.getTargets().length > 0) {
            return true;
         }

         SubDeploymentMBean[] var3 = var2.getSubDeployments();
         if (var3 != null && var3.length > 0) {
            for(int var4 = 0; var4 < var3.length; ++var4) {
               if (var3[var4].getTargets() != null && var3[var4].getTargets().length > 0) {
                  return true;
               }

               SubDeploymentMBean[] var5 = var3[var4].getSubDeployments();
               if (var5 != null && var5.length > 0) {
                  for(int var6 = 0; var6 < var5.length; ++var6) {
                     if (var5[var6].getTargets() != null && var5[var6].getTargets().length > 0) {
                        return true;
                     }
                  }
               }
            }
         }
      }

      return false;
   }

   private static boolean isRedeployAConfigChange(AppDeploymentMBean var0, DeploymentData var1) {
      boolean var2 = var1 != null && (var1.hasFiles() || var1.hasSubModuleTargets());
      return !var2 || !isAlreadyTargeted(var0.getName());
   }

   private static boolean isAConfigurationChange(AppDeploymentMBean var0, DeploymentData var1, int var2) {
      boolean var3 = false;
      switch (var2) {
         case 1:
         case 4:
         case 6:
         case 11:
         case 12:
            var3 = true;
            break;
         case 2:
         case 3:
         case 5:
         case 7:
         case 8:
         case 10:
         default:
            var3 = false;
            break;
         case 9:
            var3 = isRedeployAConfigChange(var0, var1);
      }

      return var3;
   }

   private static void updateTaskWithFailures(DeploymentTaskRuntime var0, FailureDescription[] var1, int var2) {
      if (var1 != null && var1.length > 0) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug("DeploymentManager: updateTaskWithFailures:  for server: " + var1[var3].getServer() + " task: " + var0.getId() + " with status: " + var2);
            }

            var0.updateTargetStatus(var1[var3].getServer(), var2, var1[var3].getReason());
         }
      }

   }

   private void updateTaskWithCancelState(long var1, int var3) {
      DeploymentRequestInfo var4 = this.getDeploymentRequestInfo(var1);
      if (var4 != null) {
         synchronized(var4) {
            if (var3 == 8) {
               this.undoUnactivatedChanges(var4);
            }

            Iterator var6 = var4.request.getDeployments("Application");

            while(true) {
               DeploymentTaskRuntime var8;
               do {
                  do {
                     if (!var6.hasNext()) {
                        return;
                     }

                     Deployment var7 = (Deployment)var6.next();
                     var8 = var7.getDeploymentTaskRuntime();
                  } while(var8 == null);

                  var8.setCancelState(var3);
               } while(var3 != 8 && var3 != 4);

               this.reset(var8.getId());
            }
         }
      }
   }

   private void updateStatus(long var1, DeploymentTaskRuntime var3, String var4, DeploymentState var5) {
      sendNotifications(var5, var3);
      if (this.updateTaskStatus(var1, var3, var4, var5)) {
         this.updateRuntimeState(var5);
      }

   }

   private boolean updateTaskStatus(long var1, DeploymentTaskRuntime var3, String var4, DeploymentState var5) {
      DeploymentRequestInfo var6 = this.getDeploymentRequestInfo(var1);
      if (var6 == null) {
         var3.updateTargetStatus(var4, var5.getTaskState(), var5.getException());
         return true;
      } else {
         synchronized(var6) {
            return var6.updateDeploymentStatus(var3.getId(), var4, new DeploymentTargetStatus(var1, var5));
         }
      }
   }

   private static void sendNotifications(DeploymentState var0, DeploymentTaskRuntime var1) {
      try {
         NotificationBroadcaster.sendNotificationsFromManagedServer(var0, var1, Debug.deploymentLogger);
      } catch (Throwable var3) {
         Debug.deploymentLogger.debug("Error during send Notification for " + var1.getApplicationName());
         var3.printStackTrace();
      }

   }

   private void updateTaskStatusOfRestartTargets(long var1, DeploymentTaskRuntime var3, String[] var4) {
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("DeploymentManager: updateTasksWithRestartTargetStatus for request: " + var1);
      }

      if (var4 != null && var4.length > 0) {
         String var5 = DeployerRuntimeLogger.requiresRestart();

         for(int var6 = 0; var6 < var4.length; ++var6) {
            var3.updateTargetStatus(var4[var6], 3, new Exception(var5));
         }
      }

   }

   private void updateTasksWithAccumulatedStatus(long var1) {
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("DeploymentManager: updateTasksWithAccumulatedStatus for request: " + var1);
      }

      DeploymentRequestInfo var3 = this.getDeploymentRequestInfo(var1);
      if (var3 != null) {
         synchronized(var3) {
            Iterator var5 = var3.deploymentsStatus.keySet().iterator();

            while(var5.hasNext()) {
               String var6 = (String)var5.next();
               Map var7 = var3.getDeploymentStatus(var6);
               Iterator var8 = var7.keySet().iterator();

               while(var8.hasNext()) {
                  String var9 = (String)var8.next();
                  DeploymentTargetStatus var10 = (DeploymentTargetStatus)var7.get(var9);
                  DeploymentTaskRuntime var11 = this.getDeploymentTaskRuntime(var6);
                  if (var11 != null) {
                     var11.updateTargetStatus(var9, var10.deploymentStatus.getTaskState(), var10.deploymentStatus.getException());
                  }
               }
            }

         }
      }
   }

   private static void updateRuntimeState(String var0, DeploymentVersion var1) {
      if (var0 != null && var1 != null && !"?".equals(var0)) {
         try {
            synchronized(AppRuntimeStateManager.getManager()) {
               AppRuntimeStateManager.getManager().updateState(var0, var1);
            }
         } catch (NullPointerException var5) {
            var5.printStackTrace();
         }

      }
   }

   private void updateRuntimeState(DeploymentState var1) {
      if (var1 != null) {
         try {
            String var2 = var1.getId();
            if (var2 == null || "?".equals(var2)) {
               return;
            }

            synchronized(AppRuntimeStateManager.getManager()) {
               DeploymentTaskRuntime var4 = this.getDeploymentTaskRuntime(var1);
               if ("STATE_UPDATE_PENDING".equals(var1.getCurrentState()) && (var4 == null || var4.getState() > 1)) {
                  if (Debug.isDeploymentDebugEnabled()) {
                     Debug.deploymentDebug("DeploymentManager: Ignore update app runtime state to STATE_UPDATE_PENDING after task is completed");
                  }

                  return;
               }

               AppRuntimeStateManager var5 = AppRuntimeStateManager.getManager();
               ApplicationRuntimeState var6 = var5.get(var2);
               var5.updateState(var2, var1);
               if (var6 == null && var1.getCurrentState() == null) {
                  try {
                     var5.setRetireTimeMillis(var2, 0L);
                  } catch (ManagementException var9) {
                     DeploymentManagerLogger.logStatePersistenceFailed(var2, var9);
                  }
               }
            }
         } catch (NullPointerException var11) {
            var11.printStackTrace();
         }

      }
   }

   private void abortSessionBeforeStart(AuthenticatedSubject var1, boolean var2, boolean var3) {
      if (!var3 && var1 != null && !var2) {
         try {
            this.editAccessHelper.cancelActivateSession(var1);
            this.editAccessHelper.undoUnactivatedChanges(var1);
         } catch (ManagementException var10) {
            Loggable var5 = DeployerRuntimeLogger.logErrorOnAbortEditSessionLoggable(var1.toString(), var10);
            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug("abortEditSessionBeforeStart: Error occured while aborting the session : " + var5.getMessage());
            }

            var5.log();
         } finally {
            this.editAccessHelper.cancelEditSession(var1);
         }
      }

   }

   private boolean removeDeployIfNoop(Deployment var1) {
      boolean var2 = false;
      if (var1 != null && var1.isAnAppDeployment() && var1.getOperation() == 4 && !this.pendingDeploymentsForLockOwner.isEmpty()) {
         DomainMBean var3 = ManagementService.getRuntimeAccess(kernelId).getDomain();
         AppDeploymentMBean var4 = var1.getDeploymentTaskRuntime().getAppDeploymentMBean();
         if (var3 != null && var4 != null && var3.lookupAppDeployment(var4.getName()) == null) {
            Iterator var5 = this.pendingDeploymentsForLockOwner.iterator();

            while(var5.hasNext()) {
               Deployment var6 = (Deployment)var5.next();
               DeploymentTaskRuntime var7 = var6.getDeploymentTaskRuntime();
               if (var7 != null) {
                  AppDeploymentMBean var8 = var7.getAppDeploymentMBean();
                  if (var8 != null && var8.getName().equals(var4.getName())) {
                     var2 = true;
                     var5.remove();
                     var6.getDeploymentTaskRuntime().setState(2);
                  }
               }
            }

            if (var2) {
               var1.getDeploymentTaskRuntime().setState(2);
            }

            return var2;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private static final class DeploymentRequestInfo {
      final DeploymentRequest request;
      Map deploymentsStatus;
      boolean ownsEditLock;
      DomainMBean editableDomain;
      boolean controlOperation;

      private DeploymentRequestInfo(DeploymentRequest var1) {
         this.controlOperation = false;
         this.request = var1;
         this.deploymentsStatus = new HashMap();
      }

      private void setIsEditLockOwner(boolean var1) {
         this.ownsEditLock = var1;
      }

      private void setEditableDomain(DomainMBean var1) {
         this.editableDomain = var1;
      }

      private void addDeploymentStatusContainerFor(String var1) {
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("DeploymentRequestInfo: Created status containers for taskId: " + var1 + " associated with request: " + this.request.getId());
         }

         this.deploymentsStatus.put(var1, new HashMap());
      }

      private synchronized void removeDeploymentStatusContainerFor(String var1) {
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("DeploymentRequestInfo: Removing status containers for taskId: " + var1 + " associated with request: " + this.request.getId());
         }

         if (this.deploymentsStatus != null && this.deploymentsStatus.containsKey(var1)) {
            this.deploymentsStatus.remove(var1);
            if (this.deploymentsStatus.isEmpty()) {
               this.deploymentsStatus.clear();
               this.deploymentsStatus = null;
            }
         }

      }

      private synchronized boolean removedAllStatusContainers() {
         return this.deploymentsStatus == null;
      }

      private synchronized boolean updateDeploymentStatus(String var1, String var2, DeploymentTargetStatus var3) {
         if (this.deploymentsStatus == null) {
            return true;
         } else {
            HashMap var4 = (HashMap)this.deploymentsStatus.get(var1);
            if (var4 == null) {
               return true;
            } else {
               if (var4.get(var2) != null) {
                  DeploymentTargetStatus var5 = (DeploymentTargetStatus)var4.get(var2);
                  if (!var5.isUpdatableTo(var3) && Debug.isDeploymentDebugEnabled()) {
                     Debug.deploymentDebug("DeploymentRequestInfo: ignore update STATE_UPDATE_PENDING since state is already set to a complete state by same target in same deploy request.");
                     return false;
                  }
               }

               if (Debug.isDeploymentDebugEnabled()) {
                  Debug.deploymentDebug("DeploymentRequestInfo: adding status update from: " + var2 + " for task: " + var1 + " associated with request: " + this.request.getId());
               }

               var4.put(var2, var3);
               return true;
            }
         }
      }

      private synchronized Map getDeploymentStatus(String var1) {
         if (this.deploymentsStatus == null) {
            return null;
         } else {
            HashMap var2 = (HashMap)this.deploymentsStatus.get(var1);
            return var2 == null ? null : (Map)var2.clone();
         }
      }

      private final void setControlOperation() {
         this.controlOperation = true;
      }

      private final boolean isControlOperation() {
         return this.controlOperation;
      }

      private final long getRequestId() {
         return this.request.getId();
      }

      // $FF: synthetic method
      DeploymentRequestInfo(DeploymentRequest var1, Object var2) {
         this(var1);
      }
   }

   private static final class DeploymentTargetStatus {
      final long requestId;
      final DeploymentState deploymentStatus;

      private DeploymentTargetStatus(long var1, DeploymentState var3) {
         this.requestId = var1;
         this.deploymentStatus = var3;
      }

      private boolean isUpdatableTo(DeploymentTargetStatus var1) {
         String var2 = this.deploymentStatus.getCurrentState();
         String var3 = var1.deploymentStatus.getCurrentState();
         return this.requestId != var1.requestId || !"STATE_UPDATE_PENDING".equals(var3) || !"STATE_ACTIVE".equals(var2) && !"STATE_FAILED".equals(var2);
      }

      // $FF: synthetic method
      DeploymentTargetStatus(long var1, DeploymentState var3, Object var4) {
         this(var1, var3);
      }
   }
}
