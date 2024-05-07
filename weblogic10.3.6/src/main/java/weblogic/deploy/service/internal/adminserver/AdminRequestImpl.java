package weblogic.deploy.service.internal.adminserver;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import weblogic.deploy.internal.TargetHelper;
import weblogic.deploy.service.Deployment;
import weblogic.deploy.service.DeploymentServiceCallbackHandler;
import weblogic.deploy.service.internal.DeploymentRequestTaskRuntimeMBeanImpl;
import weblogic.deploy.service.internal.DomainVersion;
import weblogic.deploy.service.internal.InvalidStateException;
import weblogic.deploy.service.internal.RequestImpl;
import weblogic.deploy.service.internal.statemachines.adminserver.AdminServerState;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.internal.ConfigurationDeployment;
import weblogic.management.runtime.DeploymentRequestTaskRuntimeMBean;

public final class AdminRequestImpl extends RequestImpl {
   private static final AdminRequestManager requestManager = AdminRequestManager.getInstance();
   private static final AdminDeploymentsManager deploymentsManager = AdminDeploymentsManager.getInstance();
   private final Set targets = new HashSet();
   private DomainVersion proposedDomainVersion;
   private transient AdminRequestStatus status;
   private transient DeploymentRequestTaskRuntimeMBeanImpl taskRuntime;
   private final Object requestLock = new Object();

   AdminRequestImpl() {
   }

   final void setId(long var1) {
      this.identifier = var1;
      if (isDebugEnabled()) {
         debug("Setting id for deployment request to '" + this.identifier + "'");
      }

   }

   public void setTaskRuntime(DeploymentRequestTaskRuntimeMBean var1) {
      synchronized(this.requestLock) {
         if (this.taskRuntime == null) {
            this.taskRuntime = (DeploymentRequestTaskRuntimeMBeanImpl)var1;
         } else if (isDebugEnabled()) {
            debug("attempting to set a duplicate task runtime for '" + this.identifier + "' - ignoring this");
         }

      }
   }

   public DeploymentRequestTaskRuntimeMBean getTaskRuntime() {
      synchronized(this.requestLock) {
         return this.taskRuntime;
      }
   }

   public final AdminRequestStatus getStatus() {
      synchronized(this.requestLock) {
         return this.status;
      }
   }

   public final DomainVersion getProposedDomainVersion() {
      synchronized(this.requestLock) {
         return this.proposedDomainVersion;
      }
   }

   public final synchronized boolean toBeCancelled() {
      synchronized(this.requestLock) {
         if (this.status == null) {
            return false;
         } else {
            return this.status.isCancelledByUser() || this.status.isCancelledByClusterConstraints() || this.status.timedOut() || this.status.failed();
         }
      }
   }

   public final void run() {
      if (isDebugEnabled()) {
         debug("DeploymentService call: Starting deploy for " + this.toString());
      }

      this.startTimeoutMonitor("admin request for id '" + this.getId() + "'");
      this.status = AdminRequestStatus.createAdminRequestStatus(this);
      if (!this.toBeCancelled() && !requestManager.isCancelPending(this.getId())) {
         this.processRequest();
      } else {
         if (isDebugEnabled()) {
            debug("request '" + this.getId() + "' has been cancelled" + " - will not proceed with the deploy");
         }

         requestManager.removePendingCancel(this.getId());
         synchronized(this.requestLock) {
            if (this.status != null) {
               this.status.signalCancelSucceeded();
            }

         }
      }
   }

   private void processRequest() {
      requestManager.addToRequestTable(this);
      this.proposedDomainVersion = deploymentsManager.getCurrentDomainVersion().getCopy();
      synchronized(this.requestLock) {
         this.processDeployments();

         try {
            AdminServerState var2 = this.getCurrentState();
            if (var2 != null) {
               var2.start();
            }
         } catch (InvalidStateException var4) {
            if (isDebugEnabled()) {
               debug("could not start deployment of id '" + this.identifier + "' due to '" + var4.toString());
            }
         }

      }
   }

   private void processDeployments() {
      for(int var1 = 0; var1 < this.deployments.size(); ++var1) {
         Deployment var2 = (Deployment)this.deployments.get(var1);
         if (var2 != null) {
            String var3 = var2.getCallbackHandlerId();
            this.proposedDomainVersion.addOrUpdateDeploymentVersion(var3, var2.getProposedVersion());
            DeploymentServiceCallbackHandler var4 = deploymentsManager.getCallbackHandler(var3);
            if (var4 == null && isDebugEnabled()) {
               debug("no DeploymentServiceCallbackHandler associated with '" + var3 + "'");
            }

            this.updateTargetList(var2);
         }
      }

   }

   private void updateTargetList(Deployment var1) {
      boolean var2 = var1 instanceof ConfigurationDeployment;
      String[] var3 = var1.getTargets();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         this.addToTargetList(var3[var4], var2);
      }

   }

   private void addToTargetList(String var1, boolean var2) {
      this.targets.add(var1);
      ClusterMBean var3 = TargetHelper.getTargetCluster(var1);
      if (var3 == null) {
         this.status.addTargetedServer(var1, false);
         if (isDebugEnabled()) {
            debug("Added '" + var1 + "' to target server list");
         }
      } else {
         ServerMBean[] var4 = var3.getServers();

         for(int var6 = 0; var6 < var4.length; ++var6) {
            String var5 = var4[var6].getName();
            this.status.addTargetedServer(var5, !var2);
            if (isDebugEnabled()) {
               debug("Added clustered server '" + var5 + "' to target server list");
            }
         }
      }

   }

   public final Iterator getTargets() {
      synchronized(this.requestLock) {
         return this.status == null ? null : this.targets.iterator();
      }
   }

   public final Iterator getTargetServers() {
      synchronized(this.requestLock) {
         return this.status == null ? null : this.status.getTargetedServers();
      }
   }

   public final void cancel() throws InvalidStateException {
      synchronized(this.requestLock) {
         if (this.status != null) {
            this.status.setCancelledByUser();
            AdminServerState var2 = this.getCurrentState();
            if (var2 != null) {
               var2.cancel();
            }

         }
      }
   }

   public final void cancelDueToClusterConstraints() throws InvalidStateException {
      synchronized(this.requestLock) {
         if (this.status != null) {
            this.status.setCancelledByClusterConstraints();
            AdminServerState var2 = this.getCurrentState();
            if (var2 != null) {
               var2.cancel();
            }

         }
      }
   }

   public final void requestTimedout() {
      synchronized(this.requestLock) {
         if (this.status != null) {
            if (isDebugEnabled()) {
               debug(this.identifier + " timed out on admin server");
            }

            this.status.setTimedOut();
            AdminServerState var2 = this.getCurrentState();
            if (var2 != null) {
               var2.requestTimedOut();
            }

         }
      }
   }

   public final AdminServerState getCurrentState() {
      AdminServerState var1 = null;
      synchronized(this.requestLock) {
         if (this.status != null) {
            var1 = this.status.getCurrentState();
         }

         return var1;
      }
   }

   public final boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else {
         return var1 instanceof AdminRequestImpl ? super.equals(var1) : false;
      }
   }

   public final int hashCode() {
      return super.hashCode();
   }

   public final String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("AdminDeploymentRequest id '" + this.getId() + "'");
      return var1.toString();
   }

   public void receivedPrepareSucceeded(long var1, String var3, boolean var4) {
      synchronized(this.requestLock) {
         AdminServerState var6 = this.getCurrentState();
         if (var6 != null) {
            var6.receivedPrepareSucceeded(var3, var4);
         }

      }
   }

   public void receivedPrepareFailed(String var1, Throwable var2) {
      this.receivedPrepareFailed(var1, var2, false);
   }

   public void receivedPrepareFailed(String var1, Throwable var2, boolean var3) {
      synchronized(this.requestLock) {
         AdminServerState var5 = this.getCurrentState();
         if (var5 != null) {
            var5.receivedPrepareFailed(var1, var2, var3);
         }

      }
   }

   public void receivedCommitSucceeded(String var1) {
      synchronized(this.requestLock) {
         AdminServerState var3 = this.getCurrentState();
         if (var3 != null) {
            var3.receivedCommitSucceeded(var1);
         }

      }
   }

   public void receivedCommitFailed(String var1, Throwable var2) {
      synchronized(this.requestLock) {
         AdminServerState var4 = this.getCurrentState();
         if (var4 != null) {
            var4.receivedCommitFailed(var1, var2);
         }

      }
   }

   public void receivedCancelSucceeded(String var1) {
      synchronized(this.requestLock) {
         AdminServerState var3 = this.getCurrentState();
         if (var3 != null) {
            var3.receivedCancelSucceeded(var1);
         }

      }
   }

   public void receivedCancelFailed(String var1, Throwable var2) {
      synchronized(this.requestLock) {
         AdminServerState var4 = this.getCurrentState();
         if (var4 != null) {
            var4.receivedCancelFailed(var1, var2);
         }

      }
   }

   public void prepareDeliveredTo(String var1) {
      synchronized(this.requestLock) {
         if (this.status != null) {
            this.status.prepareDeliveredTo(var1);
         }
      }
   }

   public void prepareDeliveryFailureWhenContacting(String var1, Exception var2) {
      synchronized(this.requestLock) {
         if (this.status != null) {
            this.status.prepareDeliveryFailureWhenContacting(var1, var2);
         }
      }
   }

   public void commitDeliveredTo(String var1) {
      synchronized(this.requestLock) {
         if (this.status != null) {
            this.status.commitDeliveredTo(var1);
         }
      }
   }

   public void commitDeliveryFailureWhenContacting(String var1, Exception var2) {
      synchronized(this.requestLock) {
         if (this.status != null) {
            this.status.commitDeliveryFailureWhenContacting(var1, var2);
         }
      }
   }

   public void cancelDeliveredTo(String var1) {
      synchronized(this.requestLock) {
         if (this.status != null) {
            this.status.cancelDeliveredTo(var1);
         }
      }
   }

   public void cancelDeliveryFailureWhenContacting(String var1, Exception var2) {
      synchronized(this.requestLock) {
         if (this.status != null) {
            this.status.cancelDeliveryFailureWhenContacting(var1, var2);
         }
      }
   }

   public final void reset() {
      synchronized(this.requestLock) {
         if (this.status != null) {
            this.targets.clear();
            this.proposedDomainVersion = null;
            this.status.reset();
            this.status = null;
         }

         if (this.taskRuntime != null) {
            this.taskRuntime.unregisterIfNoSubTasks();
            this.taskRuntime = null;
         }

      }
   }
}
