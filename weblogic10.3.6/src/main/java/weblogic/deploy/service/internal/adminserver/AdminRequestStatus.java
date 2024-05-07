package weblogic.deploy.service.internal.adminserver;

import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import weblogic.deploy.common.Debug;
import weblogic.deploy.service.FailureDescription;
import weblogic.deploy.service.RequiresRestartFailureDescription;
import weblogic.deploy.service.internal.DeploymentRequestTaskRuntimeMBeanImpl;
import weblogic.deploy.service.internal.DeploymentServiceLogger;
import weblogic.deploy.service.internal.InvalidStateException;
import weblogic.deploy.service.internal.statemachines.StateMachinesManager;
import weblogic.deploy.service.internal.statemachines.adminserver.AdminServerState;
import weblogic.logging.Loggable;
import weblogic.logging.NonCatalogLogger;
import weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.rmi.extensions.DisconnectEvent;
import weblogic.rmi.extensions.DisconnectListener;
import weblogic.rmi.extensions.ServerDisconnectEvent;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.StackTraceUtils;
import weblogic.work.WorkManagerFactory;

public final class AdminRequestStatus {
   private static final NonCatalogLogger timeoutLogger = new NonCatalogLogger("DeploymentRequestTimeoutLogger");
   private static final AdminDeploymentsManager adminDeploymentsManager = AdminDeploymentsManager.getInstance();
   private static final AdminRequestManager adminRequestManager = AdminRequestManager.getInstance();
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final String localServer;
   private AdminRequestImpl request;
   private final long requestId;
   private AdminServerState currentState;
   private ArrayList adminServerStateList;
   private boolean timedOut;
   private static boolean clusterConstraintsInitialized;
   private static boolean clusterConstraintsEnabled;
   private HashMap prepareDisconnectListeners = new HashMap();
   private HashMap commitDisconnectListeners = new HashMap();
   private HashMap cancelDisconnectListeners = new HashMap();
   private HashSet targetedServers = new HashSet();
   private HashSet targetsToBeCommited = new HashSet();
   private Set targetsToRespondToPrepare = Collections.synchronizedSet(new HashSet());
   private Map prepareDeliveryFailureTargets = new HashMap();
   private int prepareTargetsCount;
   private Throwable prepareFailure = null;
   private String prepareFailureSource = null;
   private Map prepareFailuresMap = Collections.synchronizedMap(new HashMap());
   private Set targetsToBeRestarted = new HashSet();
   private Set clusterConstraintTargets = new HashSet();
   private Set targetsToRespondToCommit = Collections.synchronizedSet(new HashSet());
   private Map commitDeliveryFailureTargets = new HashMap();
   private Map commitFailureSet = new HashMap();
   private int commitTargetsCount;
   private int commitDeliveryCount = 0;
   private HashSet targetsToBeCanceled = new HashSet();
   private Set targetsToRespondToCancel = Collections.synchronizedSet(new HashSet());
   private Map cancelDeliveryFailureTargets = new HashMap();
   private Set cancelFailureSet = new HashSet();
   private int cancelTargetsCount;
   private int cancelDeliveryCount = 0;
   private boolean isCancelledByUser;
   private boolean isCancelledByClusterConstraints;

   private AdminRequestStatus(AdminRequestImpl var1) {
      this.request = var1;
      this.requestId = var1.getId();
   }

   public static AdminRequestStatus createAdminRequestStatus(AdminRequestImpl var0) {
      AdminRequestStatus var1 = new AdminRequestStatus(var0);

      try {
         StateMachinesManager.getStateMachinesManager();
         var1.setAdminServerStates(StateMachinesManager.createAdminServerStates(var0));
      } catch (ClassNotFoundException var3) {
      } catch (IllegalAccessException var4) {
      } catch (InstantiationException var5) {
      }

      var1.setCurrentState(var1.getAdminServerState(0));
      return var1;
   }

   public final AdminRequestImpl getRequest() {
      return this.request;
   }

   public final long getId() {
      return this.requestId;
   }

   protected Map getPrepareDeliveryFailureTargets() {
      return this.prepareDeliveryFailureTargets;
   }

   protected Set getTargetsToRespondToPrepare() {
      return this.targetsToRespondToPrepare;
   }

   protected Set getTargetsToBeRestarted() {
      return this.targetsToBeRestarted;
   }

   protected Set getTargetsToBeCommited() {
      return this.targetsToBeCommited;
   }

   protected Map getCommitDeliveryFailureTargets() {
      return this.commitDeliveryFailureTargets;
   }

   protected Set getTargetsToRespondToCommit() {
      return this.targetsToRespondToCommit;
   }

   protected Map getCommitFailureSet() {
      return this.commitFailureSet;
   }

   protected Set getTargetsToBeCanceled() {
      return this.targetsToBeCanceled;
   }

   protected Map getCancelDeliveryFailureTargets() {
      return this.cancelDeliveryFailureTargets;
   }

   protected Set getTargetsToRespondToCancel() {
      return this.targetsToRespondToCancel;
   }

   protected Set getCancelFailureSet() {
      return this.cancelFailureSet;
   }

   protected String getPrepareFailureSource() {
      return this.prepareFailureSource;
   }

   private static final void debug(String var0) {
      Debug.serviceDebug(var0);
   }

   private static final boolean isDebugEnabled() {
      return Debug.isServiceDebugEnabled();
   }

   private void setAdminServerStates(ArrayList var1) {
      this.adminServerStateList = var1;
   }

   public final void setCurrentState(AdminServerState var1) {
      if (isDebugEnabled()) {
         debug("admin state set to '" + var1.toString() + "' for " + "id '" + this.requestId);
      }

      this.currentState = var1;
   }

   public final AdminServerState getCurrentState() {
      return this.currentState;
   }

   public final AdminServerState getAdminServerState(int var1) {
      return (AdminServerState)this.adminServerStateList.get(var1);
   }

   public final void addTargetedServer(String var1, boolean var2) {
      if (var2 && isClusterConstraintsEnabled()) {
         this.clusterConstraintTargets.add(var1);
      }

      if (!this.targetedServers.contains(var1)) {
         this.targetedServers.add(var1);
         ++this.prepareTargetsCount;
         this.targetsToRespondToPrepare.add(var1);
         this.targetsToBeCanceled.add(var1);
      }
   }

   public final Iterator getTargetedServers() {
      return this.targetedServers.iterator();
   }

   public final void prepareDeliveredTo(String var1) {
      if (isDebugEnabled()) {
         debug("prepare delivered to '" + var1 + "' for request '" + this.requestId + "'");
      }

      this.targetsToBeCommited.add(var1);
      if (isDebugEnabled()) {
         debug(this.dumpStatus());
      }

      if (this.allPreparesDelivered()) {
         this.getCurrentState().allPreparesDelivered();
      }

      TimeAuditorManager.getInstance().startTargetTransition(this.requestId, var1, 1);
   }

   public final void prepareDeliveryFailureWhenContacting(String var1, Exception var2) {
      if (isDebugEnabled()) {
         debug("prepare delivery to '" + var1 + "' failed for request '" + this.requestId + "'");
      }

      String var3 = DeploymentServiceLogger.operationDelivery(DeploymentServiceLogger.prepareOperation());
      FailureDescription var4 = new FailureDescription(var1, var2, var3);
      this.targetsToRespondToPrepare.remove(var1);
      this.targetsToBeCanceled.remove(var1);
      this.prepareDeliveryFailureTargets.put(var1, var4);
      this.removePrepareDisconnectListener(var1);
      if (isDebugEnabled()) {
         debug(this.dumpStatus());
      }

      if (isClusterConstraintsEnabled() && this.clusterConstraintTargets.contains(var1)) {
         if (isDebugEnabled()) {
            debug("ClusterConstraints is enabled and not all targets are reachable, so cancelling request '" + this.requestId + "'");
         }

         try {
            this.request.cancelDueToClusterConstraints();
            if (this.allPreparesDelivered()) {
               this.getCurrentState().allPreparesDelivered();
            }

            return;
         } catch (InvalidStateException var6) {
            if (isDebugEnabled()) {
               debug("attempt to 'cancel' id '" + this.requestId + "' failed due to '" + var6.getMessage() + "'");
            }

            this.signalCancelFailed(false);
         }
      }

      if (this.allTargetsUnreachableForPrepareDelivery()) {
         this.signalDeployDeferredDueToUnreachableTargets();
      } else {
         if (this.allPreparesDelivered()) {
            this.getCurrentState().allPreparesDelivered();
         }

      }
   }

   private final int currentTargetCount() {
      return this.targetsToBeCommited.size() + this.prepareDeliveryFailureTargets.size();
   }

   private final boolean allPreparesDelivered() {
      return this.prepareTargetsCount > 0 && this.currentTargetCount() == this.prepareTargetsCount;
   }

   public final void receivedPrepareSucceededFrom(String var1, boolean var2) {
      if (isDebugEnabled()) {
         debug("prepare succeeded on '" + var1 + "' for request '" + this.requestId + "'");
      }

      this.targetsToRespondToPrepare.remove(var1);
      if (var2) {
         this.targetsToBeRestarted.add(var1);
      }

      this.removePrepareDisconnectListener(var1);
      if (isDebugEnabled()) {
         debug(this.dumpStatus());
      }

      adminRequestManager.deliverRequestStatusUpdateCallback(this.request, "PrepareSuccessReceived", var1);
      TimeAuditorManager.getInstance().endTargetTransition(this.requestId, var1, 1);
   }

   public final void receivedPrepareFailedFrom(String var1, Throwable var2, boolean var3) {
      if (isDebugEnabled()) {
         debug("prepare failed on '" + var1 + "' for request '" + this.requestId + "' due to '" + var2.toString() + "'");
      }

      this.targetsToRespondToPrepare.remove(var1);
      if (this.prepareFailure == null) {
         this.prepareFailure = var2;
         this.prepareFailureSource = var1;
      } else {
         this.prepareFailuresMap.put(var1, var2);
      }

      DeploymentRequestTaskRuntimeMBeanImpl var4 = (DeploymentRequestTaskRuntimeMBeanImpl)this.request.getTaskRuntime();
      Exception var5 = var2 instanceof Exception ? (Exception)var2 : new Exception(var2);
      var4.addFailedTarget(var1, var5);
      if (var3) {
         this.targetsToBeCanceled.remove(var1);
         if (isDebugEnabled()) {
            debug("Removed server '" + var1 + "' from toBeCancelled list since" + " prepare failed on this server while sending...");
         }
      }

      this.removePrepareDisconnectListener(var1);
      if (isDebugEnabled()) {
         debug(this.dumpStatus());
      }

      adminRequestManager.deliverRequestStatusUpdateCallback(this.request, "PrepareFailedReceived", var1);
      TimeAuditorManager.getInstance().endTargetTransition(this.requestId, var1, 1);
   }

   public final Throwable getPrepareFailure() {
      return this.prepareFailure;
   }

   public final boolean failed() {
      return this.prepareFailure != null;
   }

   public final boolean receivedAllPrepareResponses() {
      return this.targetsToRespondToPrepare.isEmpty() || this.targetsToRespondToPrepare.size() == 0;
   }

   private boolean allTargetsUnreachableForPrepareDelivery() {
      return this.prepareDeliveryFailureTargets.size() == this.prepareTargetsCount;
   }

   public final Iterator getTargetsToBeCommitted() {
      this.commitTargetsCount = this.targetsToBeCommited.size();
      Iterator var1 = this.targetsToBeCommited.iterator();

      while(var1.hasNext()) {
         this.targetsToRespondToCommit.add(var1.next());
      }

      return ((HashSet)this.targetsToBeCommited.clone()).iterator();
   }

   public final void commitDeliveredTo(String var1) {
      if (isDebugEnabled()) {
         debug("'commit' delivered to target '" + var1 + "' for id '" + this.requestId + "'");
      }

      ++this.commitDeliveryCount;
      if (this.allCommitsDelivered()) {
         this.getCurrentState().allCommitsDelivered();
      }

      TimeAuditorManager.getInstance().startTargetTransition(this.requestId, var1, 2);
   }

   public final void receivedCommitSucceededFrom(String var1) {
      if (isDebugEnabled()) {
         debug("commit succeeded on '" + var1 + "' for request '" + this.requestId + "'");
      }

      this.targetsToRespondToCommit.remove(var1);
      if (isDebugEnabled()) {
         debug(this.dumpStatus());
      }

      this.removeCommitDisconnectListener(var1);
      adminRequestManager.deliverRequestStatusUpdateCallback(this.request, "CommitSuccessReceived", var1);
      TimeAuditorManager.getInstance().endTargetTransition(this.requestId, var1, 2);
   }

   public final void receivedCommitFailedFrom(String var1, Throwable var2) {
      if (isDebugEnabled()) {
         debug("commit failed on '" + var1 + "' for request '" + this.requestId + "' due to '" + var2.toString() + "'");
      }

      String var3 = DeploymentServiceLogger.commitOperation();
      Exception var4 = var2 instanceof Exception ? (Exception)var2 : new Exception(var2);
      FailureDescription var5 = new FailureDescription(var1, var4, var3);
      this.targetsToRespondToCommit.remove(var1);
      this.commitFailureSet.put(var1, var5);
      if (isDebugEnabled()) {
         debug(this.dumpStatus());
      }

      this.removeCommitDisconnectListener(var1);
      adminRequestManager.deliverRequestStatusUpdateCallback(this.request, "CommitFailedReceived", var1);
      TimeAuditorManager.getInstance().endTargetTransition(this.requestId, var1, 2);
   }

   public final boolean commitFailed() {
      return !this.commitFailureSet.isEmpty();
   }

   public final void commitDeliveryFailureWhenContacting(String var1, Exception var2) {
      if (isDebugEnabled()) {
         debug("commit delivery to '" + var1 + "' failed for request '" + this.requestId + "'");
      }

      String var3 = DeploymentServiceLogger.operationDelivery(DeploymentServiceLogger.commitOperation());
      FailureDescription var4 = new FailureDescription(var1, var2, var3);
      this.commitDeliveryFailureTargets.put(var1, var4);
      this.targetsToRespondToCommit.remove(var1);
      this.removeCommitDisconnectListener(var1);
      if (isDebugEnabled()) {
         debug(this.dumpStatus());
      }

      if (isClusterConstraintsEnabled() && this.clusterConstraintTargets.contains(var1)) {
         if (isDebugEnabled()) {
            debug("ClusterConstraints is enabled and not all targets are reachable, so cancelling request '" + this.requestId + "'");
         }

         try {
            this.request.cancelDueToClusterConstraints();
            if (this.allCommitsDelivered()) {
               this.getCurrentState().allCommitsDelivered();
            }

            return;
         } catch (InvalidStateException var6) {
            if (isDebugEnabled()) {
               debug("attempt to 'cancel' id '" + this.requestId + "' failed due to '" + var6.getMessage() + "'");
            }

            this.signalCancelFailed(false);
         }
      }

      if (this.allTargetsUnreachableForCommitDelivery()) {
         this.signalCommitFailed();
      } else {
         if (this.allCommitsDelivered()) {
            this.getCurrentState().allCommitsDelivered();
         }

      }
   }

   private boolean allTargetsUnreachableForCommitDelivery() {
      return this.commitDeliveryFailureTargets.size() == this.commitTargetsCount;
   }

   public final boolean receivedAllCommitResponses() {
      boolean var1 = this.targetsToRespondToCommit.isEmpty() || this.targetsToRespondToCommit.size() == 0;
      if (isDebugEnabled()) {
         debug(" **** Request '" + this.getRequest().getId() + "' receivedAllCommitResponses() : " + var1);
      }

      return var1;
   }

   private final boolean allCommitsDelivered() {
      return this.commitTargetsCount > 0 && this.commitDeliveryFailureTargets != null && this.commitDeliveryCount + this.commitDeliveryFailureTargets.size() == this.commitTargetsCount;
   }

   public final boolean isCancelledByUser() {
      return this.isCancelledByUser;
   }

   final void setCancelledByUser() {
      this.isCancelledByUser = true;
   }

   public final boolean isCancelledByClusterConstraints() {
      return this.isCancelledByClusterConstraints;
   }

   final void setCancelledByClusterConstraints() {
      this.isCancelledByClusterConstraints = true;
   }

   public final Iterator getTargetsToBeCancelled() {
      this.cancelTargetsCount = this.targetsToBeCanceled.size();
      Iterator var1 = this.targetsToBeCanceled.iterator();

      while(var1.hasNext()) {
         this.targetsToRespondToCancel.add(var1.next());
      }

      return ((HashSet)this.targetsToBeCanceled.clone()).iterator();
   }

   public final boolean hasTargetsToBeCancelled() {
      return !this.targetsToBeCanceled.isEmpty();
   }

   public final void cancelDeliveredTo(String var1) {
      ++this.cancelDeliveryCount;
      if (isDebugEnabled()) {
         debug("'cancel' delivered to target '" + var1 + "' for id '" + this.requestId + "'");
      }

      if (this.allCancelsDelivered()) {
         this.getCurrentState().allCancelsDelivered();
      }

      TimeAuditorManager.getInstance().startTargetTransition(this.requestId, var1, 3);
   }

   public final void receivedCancelSucceededFrom(String var1) {
      if (isDebugEnabled()) {
         debug("cancel succeeded on '" + var1 + "' for request '" + this.requestId + "'");
      }

      this.targetsToRespondToCancel.remove(var1);
      if (isDebugEnabled()) {
         debug(this.dumpStatus());
      }

      this.removeCancelDisconnectListener(var1);
      adminRequestManager.deliverRequestStatusUpdateCallback(this.request, "CancelSuccessReceived", var1);
      TimeAuditorManager.getInstance().endTargetTransition(this.requestId, var1, 3);
   }

   public final void receivedCancelFailedFrom(String var1, Throwable var2) {
      if (isDebugEnabled()) {
         debug("cancel failed on '" + var1 + "' for request '" + this.requestId + "' due to '" + var2.toString() + "'");
      }

      String var3 = DeploymentServiceLogger.cancelOperation();
      Exception var4 = var2 instanceof Exception ? (Exception)var2 : new Exception(var2);
      FailureDescription var5 = new FailureDescription(var1, var4, var3);
      this.targetsToRespondToCancel.remove(var1);
      this.cancelFailureSet.add(var5);
      if (isDebugEnabled()) {
         debug(this.dumpStatus());
      }

      this.removeCancelDisconnectListener(var1);
      adminRequestManager.deliverRequestStatusUpdateCallback(this.request, "CancelFailedReceived", var1);
      TimeAuditorManager.getInstance().endTargetTransition(this.requestId, var1, 3);
   }

   public final boolean cancelFailed() {
      return !this.cancelFailureSet.isEmpty();
   }

   public final void cancelDeliveryFailureWhenContacting(String var1, Exception var2) {
      if (isDebugEnabled()) {
         debug("cancel delivery to '" + var1 + "' failed for request '" + this.requestId + "'");
      }

      String var3 = DeploymentServiceLogger.operationDelivery(DeploymentServiceLogger.cancelOperation());
      FailureDescription var4 = new FailureDescription(var1, var2, var3);
      this.cancelDeliveryFailureTargets.put(var1, var4);
      this.targetsToRespondToCancel.remove(var1);
      this.removeCancelDisconnectListener(var1);
      if (isDebugEnabled()) {
         debug(this.dumpStatus());
      }

      if (this.allTargetsUnreachableForCancelDelivery()) {
         this.signalCancelFailed(true);
      } else {
         if (this.allCancelsDelivered()) {
            this.getCurrentState().allCancelsDelivered();
         }

      }
   }

   private boolean allTargetsUnreachableForCancelDelivery() {
      return this.cancelDeliveryFailureTargets.size() == this.cancelTargetsCount;
   }

   public final boolean receivedAllCancelResponses() {
      return this.targetsToRespondToCancel.isEmpty() || this.targetsToRespondToCancel.size() == 0;
   }

   private final boolean allCancelsDelivered() {
      return this.cancelTargetsCount > 0 && this.cancelDeliveryCount + this.cancelDeliveryFailureTargets.size() == this.cancelTargetsCount;
   }

   public final boolean timedOut() {
      return this.timedOut;
   }

   public final void setTimedOut() {
      this.timedOut = true;
      DeploymentRequestTaskRuntimeMBeanImpl var1 = (DeploymentRequestTaskRuntimeMBeanImpl)this.request.getTaskRuntime();
      String var2 = DeploymentServiceLogger.timedOut(this.request.getId());
      Exception var3 = new Exception(var2);
      var1.addFailedTarget(localServer, var3);
   }

   public final void signalDeploySucceeded() {
      this.updateTargetsToRestartInTaskRuntime();
      ArrayList var1 = new ArrayList();
      if (this.targetsToBeRestarted != null && !this.targetsToBeRestarted.isEmpty()) {
         Iterator var2 = this.targetsToBeRestarted.iterator();

         while(var2.hasNext()) {
            var1.add(new RequiresRestartFailureDescription((String)var2.next()));
         }
      }

      adminRequestManager.deliverDeploySucceededCallback(this.request, this.prepareDeliveryFailureTargets, var1);
      boolean var4 = !this.request.isControlRequest();
      if (isDebugEnabled()) {
         debug("AdminRequestStatus.signalDeploySucceeded(): Needs Version Update for request '" + this.request.getId() + "' is : " + var4);
      }

      if (var4) {
         adminDeploymentsManager.setCurrentDomainVersion(this.request.getProposedDomainVersion());
      }

      DeploymentRequestTaskRuntimeMBeanImpl var3 = (DeploymentRequestTaskRuntimeMBeanImpl)this.request.getTaskRuntime();
      var3.setState(2);
   }

   private void updateTargetsToRestartInTaskRuntime() {
      if (!this.targetsToBeRestarted.isEmpty()) {
         StringBuffer var1 = new StringBuffer();
         var1.append("*** The following servers need to be restarted for deployment request '" + this.requestId + "' to complete: ");
         DeploymentRequestTaskRuntimeMBeanImpl var2 = (DeploymentRequestTaskRuntimeMBeanImpl)this.request.getTaskRuntime();
         Iterator var3 = this.targetsToBeRestarted.iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            var2.addServerToRestartSet(var4);
            var1.append(var4);
            var1.append(" ");
         }

         if (isDebugEnabled()) {
            debug(var1.toString());
         }
      }

   }

   private final void signalDeployDeferredDueToUnreachableTargets() {
      if (isDebugEnabled()) {
         debug("'deploy deferred' for id '" + this.requestId + "' since no targets are reachable");
      }

      this.signalDeploySucceeded();
      this.scheduleNextRequest();
      this.request.reset();
   }

   public final void signalCommitSucceeded() {
      long var1 = this.request.getId();
      DeploymentRequestTaskRuntimeMBeanImpl var3 = (DeploymentRequestTaskRuntimeMBeanImpl)this.request.getTaskRuntime();
      if (this.commitDeliveryFailureTargets.size() <= 0) {
         if (isDebugEnabled()) {
            debug("'commit succeeded' for id '" + var1 + "'");
         }

         adminRequestManager.deliverCommitSucceededCallback(this.request);
         var3.setState(8);
      } else {
         Iterator var4 = this.commitDeliveryFailureTargets.keySet().iterator();
         StringBuffer var5 = new StringBuffer();

         while(var4.hasNext()) {
            var5.append((String)var4.next());
            var5.append(" ");
         }

         if (isDebugEnabled()) {
            debug("'commit' succeeded but could not be delivered to the following targets '" + var5.toString() + "' for id '" + var1 + "'");
         }

         String var6 = DeploymentServiceLogger.commitDeliveryFailure(var5.toString());
         var3.addFailedTarget(localServer, new Exception(var6));
         adminRequestManager.deliverCommitFailureCallback(this.request, this.commitDeliveryFailureTargets);
         var3.setState(8);
      }

      this.request.reset();
   }

   public final void signalCommitFailed() {
      long var1 = this.request.getId();
      if (isDebugEnabled()) {
         debug("'commit failed' for id '" + var1 + "'");
      }

      DeploymentRequestTaskRuntimeMBeanImpl var3 = (DeploymentRequestTaskRuntimeMBeanImpl)this.request.getTaskRuntime();
      StringBuffer var4 = new StringBuffer();
      String var7;
      Exception var9;
      if (this.timedOut()) {
         String var5 = DeploymentServiceLogger.commitTimedOut(var1);
         Set var6 = this.targetsToRespondToCommit;
         var7 = var5 + " : Targets to respond : " + var6;
         timeoutLogger.notice(var7);
         String var8 = getThreadDumpsOnServers(var6);
         timeoutLogger.notice(var8);
         if (isDebugEnabled()) {
            debug(var5);
         }

         var9 = new Exception(var5);
         String var10 = DeploymentServiceLogger.operationTimeout(DeploymentServiceLogger.commitOperation());
         FailureDescription var11 = new FailureDescription(localServer, var9, var10);
         this.commitDeliveryFailureTargets.put(localServer, var11);
         var4.append(var5);
      }

      if (this.commitDeliveryFailureTargets.size() > 0) {
         Iterator var12 = this.commitDeliveryFailureTargets.keySet().iterator();
         StringBuffer var14 = new StringBuffer();

         while(var12.hasNext()) {
            var14.append((String)var12.next());
            var14.append(" ");
         }

         var7 = DeploymentServiceLogger.commitDeliveryFailure(var14.toString());
         if (isDebugEnabled()) {
            debug(var7);
         }

         var4.append(var14.toString());
         Exception var16 = new Exception(var7);
         if (!this.timedOut()) {
            var3.addFailedTarget(localServer, var16);
         }
      }

      if (this.commitFailureSet.size() > 0) {
         StringBuffer var13 = new StringBuffer();
         Iterator var15 = this.commitFailureSet.keySet().iterator();

         while(var15.hasNext()) {
            var7 = (String)var15.next();
            var13.append(var7);
            var13.append(" ");
            FailureDescription var17 = (FailureDescription)this.commitFailureSet.get(var7);
            var9 = var17.getReason();
            this.commitDeliveryFailureTargets.put(var7, var17);
            if (!this.timedOut()) {
               var3.addFailedTarget(var7, var9);
            }
         }

         if (isDebugEnabled()) {
            debug("'commit' failed on these targets '" + var13.toString() + "'");
         }

         var4.append(var13.toString());
      }

      adminRequestManager.deliverCommitFailureCallback(this.request, this.commitDeliveryFailureTargets);
      var3.setState(8);
      this.request.reset();
   }

   public final void signalCancelSucceeded() {
      if (isDebugEnabled()) {
         debug("'cancel success' for id '" + this.requestId + "'");
      }

      try {
         DeploymentRequestTaskRuntimeMBeanImpl var1 = (DeploymentRequestTaskRuntimeMBeanImpl)this.request.getTaskRuntime();
         if (this.isCancelledByUser()) {
            adminRequestManager.deliverDeployCancelSucceededCallback(this.request, this.cancelDeliveryFailureTargets);
            var1.setState(6);
         } else {
            AdminDeploymentException var2;
            if (this.isCancelledByClusterConstraints()) {
               var1.setState(6);
               var2 = new AdminDeploymentException();
               boolean var3 = true;
               Map var4 = this.getPrepareDeliveryFailureTargets();
               if (var4.isEmpty()) {
                  var3 = false;
                  var4 = this.getCommitDeliveryFailureTargets();
               }

               StringBuffer var5 = new StringBuffer();
               if (!var4.isEmpty()) {
                  Iterator var6 = var4.entrySet().iterator();

                  while(var6.hasNext()) {
                     Map.Entry var7 = (Map.Entry)var6.next();
                     String var8 = (String)var7.getKey();
                     if (this.clusterConstraintTargets.contains(var8)) {
                        var5.append(var8);
                        if (var6.hasNext()) {
                           var5.append(", ");
                        }

                        var2.addFailureDescription((FailureDescription)var7.getValue());
                     }
                  }
               }

               Exception var13 = new Exception(DeploymentServiceLogger.cancelledDueToClusterConstraints(this.requestId, var5.toString()));
               FailureDescription var14 = new FailureDescription(localServer, var13, var3 ? "prepare" : "commit");
               var2.addFailureDescription(var14);
               adminRequestManager.deliverDeployFailedCallback(this.request, var2);
               var1.setState(3);
            } else {
               var2 = new AdminDeploymentException();
               this.includePrepareDeliveryFailure(var2);
               this.includeCancelDeliveryFailures(var2);
               this.includeDeploymentFailure(var2);
               adminRequestManager.deliverDeployFailedCallback(this.request, var2);
               var1.setState(3);
            }
         }
      } finally {
         this.scheduleNextRequest();
         this.request.reset();
      }

   }

   private void includePrepareDeliveryFailure(AdminDeploymentException var1) {
      if (this.prepareDeliveryFailureTargets.size() > 0) {
         Iterator var2 = this.prepareDeliveryFailureTargets.values().iterator();

         while(var2.hasNext()) {
            var1.addFailureDescription((FailureDescription)var2.next());
         }
      }

   }

   private void includeDeploymentFailure(AdminDeploymentException var1) {
      String var3 = localServer;
      Object var2;
      if (this.timedOut()) {
         var2 = new Exception(DeploymentServiceLogger.timedOutAdmin(this.request.getId()));
      } else {
         var2 = this.prepareFailure;
         var3 = this.prepareFailureSource;
      }

      String var4 = DeploymentServiceLogger.prepareOperation();
      Exception var5 = var2 instanceof Exception ? (Exception)var2 : new Exception((Throwable)var2);
      FailureDescription var6 = new FailureDescription(var3, var5, var4);
      var1.addFailureDescription(var6);
      Iterator var7 = this.prepareFailuresMap.entrySet().iterator();

      while(var7.hasNext()) {
         Map.Entry var8 = (Map.Entry)var7.next();
         Exception var9 = var8.getValue() instanceof Exception ? (Exception)var8.getValue() : new Exception((Throwable)var8.getValue());
         FailureDescription var10 = new FailureDescription((String)var8.getKey(), var9, var4);
         var1.addFailureDescription(var10);
      }

   }

   public final void signalCancelFailed(boolean var1) {
      if (isDebugEnabled()) {
         debug("'cancel failed' for id '" + this.requestId + "'");
      }

      AdminDeploymentException var2 = new AdminDeploymentException();
      String var4 = localServer;
      DeploymentRequestTaskRuntimeMBeanImpl var5 = (DeploymentRequestTaskRuntimeMBeanImpl)this.request.getTaskRuntime();
      if (!this.isCancelledByUser() && !this.isCancelledByClusterConstraints()) {
         this.includePrepareFailure(var2);
         this.includeTimeoutFailure(var2);
         this.includeCancelDeliveryFailures(var2);
         this.includeCancelFailures(var2);
         adminRequestManager.deliverDeployFailedCallback(this.request, var2);
         var5.setState(3);
      } else {
         String var6 = DeploymentServiceLogger.prepareOperation();
         Exception var3 = new Exception(DeploymentServiceLogger.deploymentCancelled(this.requestId));
         FailureDescription var7 = new FailureDescription(var4, var3, var6);
         var2.addFailureDescription(var7);
         var5.addFailedTarget(localServer, var5.getError());
         adminRequestManager.deliverDeployCancelFailedCallback(this.request, var2);
         var5.setState(7);
      }

      if (var1) {
         this.scheduleNextRequest();
         this.request.reset();
      }

   }

   DisconnectListener getPrepareDisconnectListener(String var1) {
      Object var2 = this.prepareDisconnectListeners.get(var1);
      if (var2 == null) {
         synchronized(this.prepareDisconnectListeners) {
            var2 = this.prepareDisconnectListeners.get(var1);
            if (var2 == null) {
               var2 = new PrepareDisconnectListenerImpl(var1);
               this.prepareDisconnectListeners.put(var1, var2);
            }
         }
      }

      if (isDebugEnabled()) {
         debug(" +++ Returning Prepare DisconnectListener : " + var2);
      }

      return (DisconnectListener)var2;
   }

   DisconnectListener getCommitDisconnectListener(String var1) {
      Object var2 = this.commitDisconnectListeners.get(var1);
      if (var2 == null) {
         synchronized(this.commitDisconnectListeners) {
            var2 = this.commitDisconnectListeners.get(var1);
            if (var2 == null) {
               var2 = new CommitDisconnectListenerImpl(var1);
               this.commitDisconnectListeners.put(var1, var2);
            }
         }
      }

      if (isDebugEnabled()) {
         debug(" +++ Returning Commit DisconnectListener : " + var2);
      }

      return (DisconnectListener)var2;
   }

   DisconnectListener getCancelDisconnectListener(String var1) {
      Object var2 = this.cancelDisconnectListeners.get(var1);
      if (var2 == null) {
         synchronized(this.cancelDisconnectListeners) {
            var2 = this.cancelDisconnectListeners.get(var1);
            if (var2 == null) {
               var2 = new CancelDisconnectListenerImpl(var1);
               this.cancelDisconnectListeners.put(var1, var2);
            }
         }
      }

      if (isDebugEnabled()) {
         debug(" +++ Returning Cancel DisconnectListener : " + var2);
      }

      return (DisconnectListener)var2;
   }

   private void includeCancelFailures(AdminDeploymentException var1) {
      if (this.cancelFailureSet.size() > 0) {
         Iterator var2 = this.cancelFailureSet.iterator();

         while(var2.hasNext()) {
            var1.addFailureDescription((FailureDescription)var2.next());
         }
      }

   }

   private void includeCancelDeliveryFailures(AdminDeploymentException var1) {
      if (this.cancelDeliveryFailureTargets.size() > 0) {
         Iterator var2 = this.cancelDeliveryFailureTargets.values().iterator();

         while(var2.hasNext()) {
            var1.addFailureDescription((FailureDescription)var2.next());
         }
      }

   }

   private void includeTimeoutFailure(AdminDeploymentException var1) {
      if (this.timedOut()) {
         String var2;
         if (this.prepareFailure == null) {
            var2 = DeploymentServiceLogger.timedOutDuring(this.request.getId(), DeploymentServiceLogger.prepareOperation());
         } else {
            var2 = DeploymentServiceLogger.timedOutDuring(this.request.getId(), DeploymentServiceLogger.cancelOperation());
         }

         Exception var3 = new Exception(var2);
         String var4 = localServer;
         String var5 = DeploymentServiceLogger.prepareOperation();
         FailureDescription var6 = new FailureDescription(var4, var3, var5);
         var1.addFailureDescription(var6);
      }

   }

   private void includePrepareFailure(AdminDeploymentException var1) {
      if (this.prepareFailure != null) {
         Throwable var2 = this.prepareFailure;
         String var3 = this.prepareFailureSource;
         String var4 = DeploymentServiceLogger.prepareOperation();
         Exception var5 = var2 instanceof Exception ? (Exception)var2 : new Exception(var2);
         FailureDescription var6 = new FailureDescription(var3, var5, var4);
         var1.addFailureDescription(var6);
      }

   }

   public final void scheduleNextRequest() {
      if (this.request != null && isDebugEnabled()) {
         debug("scheduling next admin request after id '" + this.request.getId() + "'");
      }

      adminRequestManager.scheduleNextRequest();
   }

   final void reset() {
      if (this.request != null) {
         if (isDebugEnabled()) {
            debug("resetting admin request of id '" + this.request.getId() + "'");
         }

         WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
            public void run() {
               TimeAuditorManager.getInstance().printAuditor(AdminRequestStatus.this.requestId, System.out);
               TimeAuditorManager.getInstance().endAuditor(AdminRequestStatus.this.requestId);
            }
         });
         this.request.cancelTimeoutMonitor();
         this.targetedServers.clear();
         this.targetedServers = null;
         this.targetsToRespondToPrepare.clear();
         this.targetsToRespondToPrepare = null;
         this.targetsToBeRestarted.clear();
         this.targetsToBeRestarted = null;
         this.prepareDeliveryFailureTargets.clear();
         this.prepareDeliveryFailureTargets = null;
         this.targetsToBeCommited.clear();
         this.targetsToBeCommited = null;
         this.targetsToRespondToCommit.clear();
         this.targetsToRespondToCommit = null;
         this.commitDeliveryFailureTargets.clear();
         this.commitDeliveryFailureTargets = null;
         this.commitFailureSet.clear();
         this.commitFailureSet = null;
         this.targetsToBeCanceled.clear();
         this.targetsToBeCanceled = null;
         this.targetsToRespondToCancel.clear();
         this.targetsToRespondToCancel = null;
         this.cancelDeliveryFailureTargets.clear();
         this.cancelDeliveryFailureTargets = null;
         this.cancelFailureSet.clear();
         this.cancelFailureSet = null;
         Iterator var1 = this.adminServerStateList.iterator();

         while(var1.hasNext()) {
            AdminServerState var2 = (AdminServerState)var1.next();
            var2.reset();
         }

         this.adminServerStateList.clear();
         this.adminServerStateList = null;
         this.currentState = null;
         this.prepareFailure = null;
         this.prepareFailureSource = null;
         this.prepareFailuresMap.clear();
         adminRequestManager.removeRequest(this.request.getId());
         this.request = null;
      }

   }

   private String dumpStatus() {
      StringBuffer var1 = new StringBuffer();
      var1.append("admin request status [ ");
      var1.append("\n\tid: ");
      var1.append(this.requestId);
      var1.append(", ");
      if (this.request == null) {
         var1.append(" is reset");
         var1.append("\n]");
         return var1.toString();
      } else {
         var1.append("\n \tcurrent state: ");
         var1.append(this.currentState.toString());
         Iterator var2 = this.targetedServers.iterator();
         var1.append("\n \ttargets: ");

         while(var2.hasNext()) {
            var1.append((String)var2.next());
            var1.append(" ");
         }

         if (!this.prepareDeliveryFailureTargets.isEmpty()) {
            dumpCollection(var1, this.prepareDeliveryFailureTargets.keySet().iterator(), "prepare failed to be delivered to: ");
         }

         if (!this.targetsToRespondToPrepare.isEmpty()) {
            dumpCollection(var1, this.targetsToRespondToPrepare.iterator(), "targets to respond to prepare: ");
         }

         if (!this.targetsToBeRestarted.isEmpty()) {
            dumpCollection(var1, this.targetsToBeRestarted.iterator(), "targets to be restarted: ");
         }

         if (!this.targetsToBeCommited.isEmpty()) {
            dumpCollection(var1, this.targetsToBeCommited.iterator(), "targets that are to be commited: ");
         }

         if (!this.commitDeliveryFailureTargets.isEmpty()) {
            dumpCollection(var1, this.commitDeliveryFailureTargets.keySet().iterator(), "commit failed to be delivered to: ");
         }

         if (!this.targetsToRespondToCommit.isEmpty()) {
            dumpCollection(var1, this.targetsToRespondToCommit.iterator(), "targets to respond to commit: ");
         }

         if (!this.commitFailureSet.isEmpty()) {
            dumpCollection(var1, this.commitFailureSet.keySet().iterator(), "targets commit failed on : ");
         }

         if (!this.targetsToBeCanceled.isEmpty()) {
            dumpCollection(var1, this.targetsToBeCanceled.iterator(), "targets that are to be canceled: ");
         }

         if (!this.cancelDeliveryFailureTargets.isEmpty()) {
            dumpCollection(var1, this.cancelDeliveryFailureTargets.keySet().iterator(), "cancel failed to be delivered to: ");
         }

         if (!this.targetsToRespondToCancel.isEmpty()) {
            dumpCollection(var1, this.targetsToRespondToCancel.iterator(), "targets to respond to cancel: ");
         }

         if (!this.cancelFailureSet.isEmpty()) {
            var1.append("\n \t");
            var1.append("targets cancel failed on : ");
            Iterator var3 = this.cancelFailureSet.iterator();

            while(var3.hasNext()) {
               FailureDescription var4 = (FailureDescription)var3.next();
               var1.append(var4.getServer());
               var1.append(" ");
            }
         }

         if (this.prepareFailure != null) {
            var1.append("\n \t prepare failure: " + this.prepareFailure.toString() + " on " + this.prepareFailureSource);
         }

         if (this.timedOut()) {
            var1.append("\n \t has timed out");
         }

         if (this.isCancelledByUser()) {
            var1.append("\n \t was cancelled by the user / administrator");
         }

         if (this.isCancelledByClusterConstraints()) {
            var1.append("\n \t was cancelled due to all targets not available and cluster-constraints-enabled being set to true");
         }

         var1.append("\n]");
         return var1.toString();
      }
   }

   private static void dumpCollection(StringBuffer var0, Iterator var1, String var2) {
      var0.append("\n \t");
      var0.append(var2);

      while(var1.hasNext()) {
         var0.append((String)var1.next());
         var0.append(" ");
      }

   }

   private void prepareFailedDueToServerDisconnect(String var1) {
      this.targetsToBeCommited.remove(var1);
      Loggable var2 = DeploymentServiceLogger.logDeferredDueToDisconnectLoggable(Long.toString(this.requestId), var1);
      String var3 = DeploymentServiceLogger.operationDelivery(DeploymentServiceLogger.prepareOperation());
      FailureDescription var4 = new FailureDescription(var1, new Exception(var2.getMessage()), var3);
      this.targetsToRespondToPrepare.remove(var1);
      this.targetsToBeCanceled.remove(var1);
      this.prepareDeliveryFailureTargets.put(var1, var4);
      if (isClusterConstraintsEnabled() && this.clusterConstraintTargets.contains(var1)) {
         if (isDebugEnabled()) {
            debug("ClusterConstraints is enabled and not all targets are reachable, so cancelling request '" + this.requestId + "'");
         }

         try {
            this.request.cancelDueToClusterConstraints();
            if (this.allPreparesDelivered()) {
               this.getCurrentState().allPreparesDelivered();
            }

            return;
         } catch (InvalidStateException var6) {
            if (isDebugEnabled()) {
               debug("attempt to 'cancel' id '" + this.requestId + "' failed due to '" + var6.getMessage() + "'");
            }

            this.signalCancelFailed(false);
         }
      }

      if (isDebugEnabled()) {
         debug(this.dumpStatus());
      }

      this.request.receivedPrepareSucceeded(this.requestId, var1, false);
   }

   private void commitFailedDueToServerDisconnect(String var1) {
      Loggable var2 = DeploymentServiceLogger.logDeferredDueToDisconnectLoggable(Long.toString(this.requestId), var1);
      String var3 = DeploymentServiceLogger.operationDelivery(DeploymentServiceLogger.commitOperation());
      FailureDescription var4 = new FailureDescription(var1, new Exception(var2.getMessage()), var3);
      this.targetsToRespondToCommit.remove(var1);
      this.targetsToBeCanceled.remove(var1);
      this.commitDeliveryFailureTargets.put(var1, var4);
      if (isDebugEnabled()) {
         debug(this.dumpStatus());
      }

      if (isClusterConstraintsEnabled() && this.clusterConstraintTargets.contains(var1)) {
         if (isDebugEnabled()) {
            debug("ClusterConstraints is enabled and not all targets are reachable, so cancelling request '" + this.requestId + "'");
         }

         try {
            this.request.cancelDueToClusterConstraints();
            if (this.allCommitsDelivered()) {
               this.getCurrentState().allCommitsDelivered();
            }

            return;
         } catch (InvalidStateException var6) {
            if (isDebugEnabled()) {
               debug("attempt to 'cancel' id '" + this.requestId + "' failed due to '" + var6.getMessage() + "'");
            }

            this.signalCancelFailed(false);
         }
      }

      this.request.receivedCommitSucceeded(var1);
   }

   private void cancelFailedDueToServerDisconnect(String var1) {
      Loggable var2 = DeploymentServiceLogger.logDeferredDueToDisconnectLoggable(Long.toString(this.requestId), var1);
      String var3 = DeploymentServiceLogger.operationDelivery(DeploymentServiceLogger.cancelOperation());
      FailureDescription var4 = new FailureDescription(var1, new Exception(var2.getMessage()), var3);
      this.targetsToRespondToCancel.remove(var1);
      this.cancelDeliveryFailureTargets.put(var1, var4);
      if (isDebugEnabled()) {
         debug(this.dumpStatus());
      }

      if (this.allTargetsUnreachableForCancelDelivery()) {
         this.signalCancelFailed(true);
      } else {
         this.request.receivedCancelSucceeded(var1);
      }
   }

   private void removePrepareDisconnectListener(String var1) {
      DisconnectListener var2 = null;
      synchronized(this.prepareDisconnectListeners) {
         var2 = (DisconnectListener)((HashMap)this.prepareDisconnectListeners.clone()).get(var1);
      }

      if (var2 != null) {
         adminRequestManager.removeDisconnectListener(var1, var2);
      }

   }

   private void removeCommitDisconnectListener(String var1) {
      DisconnectListener var2 = null;
      synchronized(this.commitDisconnectListeners) {
         var2 = (DisconnectListener)((HashMap)this.commitDisconnectListeners.clone()).get(var1);
      }

      if (var2 != null) {
         adminRequestManager.removeDisconnectListener(var1, var2);
      }

   }

   private void removeCancelDisconnectListener(String var1) {
      DisconnectListener var2 = null;
      synchronized(this.cancelDisconnectListeners) {
         var2 = (DisconnectListener)((HashMap)this.cancelDisconnectListeners.clone()).get(var1);
      }

      if (var2 != null) {
         adminRequestManager.removeDisconnectListener(var1, var2);
      }

   }

   private static boolean isClusterConstraintsEnabled() {
      if (!clusterConstraintsInitialized) {
         clusterConstraintsEnabled = ManagementService.getRuntimeAccess(kernelId).getDomain().isClusterConstraintsEnabled();
         clusterConstraintsInitialized = true;
      }

      return clusterConstraintsEnabled;
   }

   private static String getThreadDumpsOnServers(Set var0) {
      if (var0 == null) {
         return null;
      } else {
         Iterator var1 = var0.iterator();
         StringBuffer var2 = new StringBuffer();
         var2.append("ThreadDumps on servers ").append(var0).append("{\n");

         while(var1.hasNext()) {
            String var3 = (String)var1.next();

            try {
               DomainRuntimeServiceMBean var4 = ManagementService.getDomainAccess(kernelId).getDomainRuntimeService();
               ServerRuntimeMBean var5 = var4.lookupServerRuntime(var3);
               String var6 = var5.getJVMRuntime().getThreadStackDump();
               var2.append(" ThreadDump for ").append(var3).append("<\n");
               var2.append(var6);
               var2.append("\n>\n\n");
            } catch (Throwable var7) {
               var2.append(" Exception while getting ThreadDump for ");
               var2.append(var3).append(" :: ");
               var2.append(StackTraceUtils.throwable2StackTrace(var7));
               var2.append("\n\n");
            }
         }

         var2.append("\n}\n");
         return var2.toString();
      }
   }

   static {
      localServer = ManagementService.getRuntimeAccess(kernelId).getServerName();
      clusterConstraintsInitialized = false;
      clusterConstraintsEnabled = false;
   }

   private class CancelDisconnectListenerImpl extends DisconnectListenerImpl {
      CancelDisconnectListenerImpl(String var2) {
         super(var2);
      }

      public void onDisconnect(DisconnectEvent var1) {
         String var2 = this.serverName;
         if (var1 instanceof ServerDisconnectEvent) {
            var2 = ((ServerDisconnectEvent)var1).getServerName();
         }

         if (AdminRequestStatus.isDebugEnabled()) {
            AdminRequestStatus.debug(" +++ Got Disconnect event... : " + var1 + " on : " + this.toString());
         }

         boolean var3 = AdminRequestStatus.this.targetsToRespondToCancel != null && AdminRequestStatus.this.targetsToRespondToCancel.contains(var2);
         if (var3) {
            AdminRequestStatus.this.cancelFailedDueToServerDisconnect(var2);
         }

      }
   }

   private class CommitDisconnectListenerImpl extends DisconnectListenerImpl {
      CommitDisconnectListenerImpl(String var2) {
         super(var2);
      }

      public void onDisconnect(DisconnectEvent var1) {
         String var2 = this.serverName;
         if (var1 instanceof ServerDisconnectEvent) {
            var2 = ((ServerDisconnectEvent)var1).getServerName();
         }

         if (AdminRequestStatus.isDebugEnabled()) {
            AdminRequestStatus.debug(" +++ Got Disconnect event... : " + var1 + " on : " + this.toString());
         }

         boolean var3 = AdminRequestStatus.this.targetsToRespondToCommit != null && AdminRequestStatus.this.targetsToRespondToCommit.contains(var2);
         if (var3) {
            AdminRequestStatus.this.commitFailedDueToServerDisconnect(var2);
         }

      }
   }

   private class PrepareDisconnectListenerImpl extends DisconnectListenerImpl {
      PrepareDisconnectListenerImpl(String var2) {
         super(var2);
      }

      public void onDisconnect(DisconnectEvent var1) {
         String var2 = this.serverName;
         if (var1 instanceof ServerDisconnectEvent) {
            var2 = ((ServerDisconnectEvent)var1).getServerName();
         }

         if (AdminRequestStatus.isDebugEnabled()) {
            AdminRequestStatus.debug(" +++ Got Disconnect event... : " + var1 + " on : " + this.toString());
         }

         boolean var3 = AdminRequestStatus.this.targetsToRespondToPrepare != null && AdminRequestStatus.this.targetsToRespondToPrepare.contains(var2);
         if (var3) {
            AdminRequestStatus.this.prepareFailedDueToServerDisconnect(var2);
         }

      }
   }

   private abstract class DisconnectListenerImpl implements DisconnectListener {
      protected String serverName = null;

      DisconnectListenerImpl(String var2) {
         this.serverName = var2;
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         var1.append(super.toString()).append("(");
         var1.append("Server=").append(this.serverName).append(", ");
         var1.append("requestId=").append(AdminRequestStatus.this.requestId);
         return var1.toString();
      }
   }
}
