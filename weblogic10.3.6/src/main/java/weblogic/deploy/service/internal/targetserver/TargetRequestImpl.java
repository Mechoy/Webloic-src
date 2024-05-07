package weblogic.deploy.service.internal.targetserver;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import weblogic.deploy.common.Debug;
import weblogic.deploy.service.Deployment;
import weblogic.deploy.service.internal.DomainVersion;
import weblogic.deploy.service.internal.RequestImpl;
import weblogic.deploy.service.internal.transport.CommonMessageSender;
import weblogic.deploy.service.internal.transport.DeploymentServiceMessage;
import weblogic.utils.StackTraceUtils;

public final class TargetRequestImpl extends RequestImpl {
   private static final TargetRequestManager requestManager = TargetRequestManager.getInstance();
   private static final CommonMessageSender messageSender = CommonMessageSender.getInstance();
   private TargetRequestStatus deploymentStatus;
   private DomainVersion domainVersion;
   private DomainVersion proposedDomainVersion;
   private DomainVersion preparingFromVersion;
   private DeploymentContextImpl context;
   private Map deploymentsMap;
   private DomainVersion syncToAdminVersion;
   private ArrayList syncToAdminDeployments;
   private Map syncToAdminDeploymentsMap;
   private boolean heartbeatRequest;
   private boolean isAborted = false;

   public void setId(long var1) {
      this.identifier = var1;
      if (isDebugEnabled()) {
         debug("setting id for request to '" + this.identifier + "' on target");
      }

   }

   public final void setDeployments(List var1) {
      this.deployments = var1;
   }

   public final Iterator getDeployments() {
      return this.syncToAdminVersion != null ? ((List)this.syncToAdminDeployments.clone()).iterator() : super.getDeployments();
   }

   public final void setDeploymentsMap(Map var1) {
      this.deploymentsMap = var1;
   }

   public final Map getDeploymentsMap() {
      return this.syncToAdminVersion != null ? this.syncToAdminDeploymentsMap : this.deploymentsMap;
   }

   public final Iterator getDeployments(String var1) {
      if (this.syncToAdminVersion != null) {
         ArrayList var2 = new ArrayList();
         Iterator var3 = this.syncToAdminDeployments.iterator();

         while(var3.hasNext()) {
            Deployment var4 = (Deployment)var3.next();
            if (var1.equals(var4.getCallbackHandlerId())) {
               var2.add(var4);
            }
         }

         return var2.iterator();
      } else {
         return super.getDeployments(var1);
      }
   }

   public final void setHeartbeatRequest() {
      this.heartbeatRequest = true;
   }

   public final boolean isHeartbeatRequest() {
      return this.heartbeatRequest;
   }

   public final void setDomainVersion(DomainVersion var1) {
      this.domainVersion = var1;
   }

   public final DomainVersion getDomainVersion() {
      return this.domainVersion;
   }

   public final void setProposedDomainVersion(DomainVersion var1) {
      this.proposedDomainVersion = var1;
   }

   public final DomainVersion getProposedDomainVersion() {
      return this.proposedDomainVersion;
   }

   public final void setPreparingFromVersion(DomainVersion var1) {
      this.preparingFromVersion = var1;
   }

   public final DomainVersion getPreparingFromVersion() {
      return this.preparingFromVersion;
   }

   public final DomainVersion getSyncToAdminVersion() {
      return this.syncToAdminVersion;
   }

   public final void setSyncToAdminMessage(DeploymentServiceMessage var1) {
      this.syncToAdminVersion = var1.getToVersion();
      this.syncToAdminDeployments = var1.getItems();
   }

   public final List getSyncToAdminDeployments() {
      return this.syncToAdminDeployments;
   }

   public final void setSyncToAdminDeploymentsMap(Map var1) {
      this.syncToAdminDeploymentsMap = var1;
   }

   public final Map getSyncToAdminDeploymentsMap() {
      return this.syncToAdminDeploymentsMap;
   }

   public final void resetSyncToAdminDeployments() {
      this.syncToAdminVersion = null;
      this.syncToAdminDeployments = null;
      this.syncToAdminDeploymentsMap = null;
   }

   public final void setDeploymentStatus(TargetRequestStatus var1) {
      this.deploymentStatus = var1;
   }

   public final TargetRequestStatus getDeploymentStatus() {
      return this.deploymentStatus;
   }

   public final CommonMessageSender getMessageSender() {
      return messageSender;
   }

   public final void setDeploymentContext(DeploymentContextImpl var1) {
      this.context = var1;
   }

   public final DeploymentContextImpl getDeploymentContext() {
      return this.context;
   }

   public void run() {
      requestManager.addToRequestTable(this);
      if (isDebugEnabled()) {
         debug("DeploymentService call: Starting target side deploy for '" + this.getId() + "'");
      }

      this.deploymentStatus = TargetRequestStatus.createTargetRequestStatus(this);
      this.startTimeoutMonitor("TargetRequest for id '" + this.identifier + "'");

      try {
         this.deploymentStatus.getCurrentState().receivedPrepare();
      } catch (Throwable var4) {
         Throwable var1 = var4;
         if (isDebugEnabled()) {
            Debug.serviceDebug(StackTraceUtils.throwable2StackTrace(var4));
         }

         try {
            messageSender.sendPrepareNakMsg(this.getId(), var1);
         } catch (RemoteException var3) {
            this.abort();
         }

         if (!this.isAborted()) {
            this.deploymentStatus.reset();
         }
      }

   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else {
         return var1 instanceof TargetRequestImpl ? super.equals(var1) : false;
      }
   }

   public int hashCode() {
      return super.hashCode();
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("TargetDeploymentRequest id '" + this.getId() + "'");
      return var1.toString();
   }

   public void requestTimedout() {
      if (this.deploymentStatus != null) {
         if (isDebugEnabled()) {
            debug(this.identifier + " timed out on target server");
         }

         this.deploymentStatus.setTimedOut();
         this.abort();
      }
   }

   public void abort() {
      this.isAborted = true;
      this.deploymentStatus.getCurrentState().abort();
   }

   public boolean isAborted() {
      return this.isAborted;
   }
}
